	Ext.define('SupplierApp.view.plantAccess.PlantAccessGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.plantAccessGrid',
    itemId: 'paMainGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',  
	//store: Ext.create('SupplierApp.store.PlantAccess'),
	//scrollable : true,
	scroll :  true,
	viewConfig: {
	    stripeRows: true,
	    style : { overflow: 'auto', overflowX: 'hidden' },
	    enableTextSelection: true,
	    markDirty: false,
	    listeners: {
	        refresh: function (view) {
	            var grid = view.up('grid');
	            if (!grid) return;

	            Ext.defer(function () {
	                // Autoajuste de columnas según contenido
	                Ext.each(grid.columns, function (col) {
	                    if (col.autoSize) col.autoSize();
	                    else if (col.autoSizeColumn) col.autoSizeColumn();

	                    // Ajuste adicional según header (por texto largo)
	                    var headerText = col.text || '';
	                    if (headerText && col.getEl()) {
	                        var headerEl = col.getEl().down('.x-column-header-text');
	                        if (headerEl) {
	                            var textWidth = Ext.util.TextMetrics.measure(headerEl, headerText).width + 20;
	                            if (textWidth > col.getWidth()) {
	                                col.setWidth(textWidth);
	                            }
	                        }
	                    }
	                });

	                // Repartir espacio sobrante solo si sobra
	                Ext.defer(function () {
	                    var totalWidth = 0;
	                    var gridWidth = grid.getWidth();

	                    // Calcular ancho total de columnas visibles
	                    Ext.each(grid.columns, function (col) {
	                        if (!col.hidden) totalWidth += col.getWidth();
	                    });

	                    // Si sobra espacio, lo repartimos
	                    if (totalWidth < gridWidth) {
	                        var diff = gridWidth - totalWidth - 10; // margen visual
	                        var visibles = Ext.Array.filter(grid.columns, function (col) {
	                            return !col.hidden;
	                        });
	                        var extra = diff / visibles.length;

	                        Ext.each(visibles, function (col) {
	                            col.setWidth(col.getWidth() + extra);
	                        });

	                        grid.updateLayout();
	                    }
	                }, 100);
	                
	                // VALIDACIONES PARA APLICAR AJUSTE DE ALTURA:
	                // 1. Pantalla grande
	                var screenWidth = Ext.Element.getViewportWidth();
	                var screenHeight = Ext.Element.getViewportHeight();
	                var isLargeScreen = screenWidth >= 1000;
	                
	                // 2. Verificar si los registros de la página son iguales al pageSize
	                var store = grid.getStore();
	                var currentRecords = store.getCount();
	               // var pageSize = store.pageSize || 2; // Usar 2 como default si no existe
	                var pageSize = 2;
	                //var isFullPage = currentRecords === pageSize;
	                var isFullPage = currentRecords >= pageSize;
	                
	                // Aplicar ajuste de altura solo si ambas condiciones se cumplen
	                if (isLargeScreen && isFullPage) {
	                    // Ajuste de altura de filas
	                    var containerHeight = grid.getHeight();
	                    
	                    // Calcular altura de los headers
	                    var headerHeight = 0;
	                    var headerContainer = grid.headerCt;
	                    if (headerContainer && headerContainer.getHeight()) {
	                        headerHeight = headerContainer.getHeight();
	                    }
	                    
	                    // Calcular altura de los docked items (toolbars)
	                    var dockedHeight = 0;
	                    if (grid.dockedItems) {
	                        grid.dockedItems.each(function(item) {
	                            if (item.isVisible() && item.getHeight) {
	                                dockedHeight += item.getHeight();
	                            }
	                        });
	                    }
	                    
	                    var availableHeight = containerHeight - headerHeight - dockedHeight - 10; // margen
	                    
	                    var rows = view.getNodes();
	                    var totalContentHeight = 0;
	                    var rowHeights = [];
	                    
	                    // Calcular altura necesaria para cada fila
	                    Ext.each(rows, function(row, index) {
	                        var rowHeight = 25; // mínima
	                        var cells = Ext.get(row).query('.x-grid-cell');
	                        
	                        Ext.each(cells, function(cell) {
	                            var cellEl = Ext.get(cell);
	                            cellEl.setStyle('height', 'auto');
	                            var contentHeight = cellEl.dom.scrollHeight;
	                            if (contentHeight > rowHeight) {
	                                rowHeight = contentHeight + 8; // padding
	                            }
	                        });
	                        
	                        rowHeights[index] = rowHeight;
	                        totalContentHeight += rowHeight;
	                    });
	                    
	                    // Distribuir espacio sobrante si hay
	                    if (totalContentHeight < availableHeight && rows.length > 0) {
	                        var extraHeight = (availableHeight - totalContentHeight) / rows.length;
	                        
	                        Ext.each(rows, function(row, index) {
	                            Ext.get(row).setHeight(rowHeights[index] + extraHeight);
	                        });
	                    } else {
	                        // Usar alturas calculadas por contenido
	                        Ext.each(rows, function(row, index) {
	                            Ext.get(row).setHeight(rowHeights[index]);
	                        });
	                    }
	                    
	                    grid.updateLayout();
	                }	                
	            }, 200);
	        }
	    }
	},
    initComponent: function() {
    	 this.emptyText = SuppAppMsg.emptyMsg;
    	 this.store = Ext.create('SupplierApp.store.PlantAccess');
    	 var plantAccessController = SupplierApp.app.getController("SupplierApp.controller.PlantAccess");
    	 
    	var docType = null;
    	var invStatus = null;
    	

    	invStatus = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'name'],
    	    data : [    	        
    	    	{"id":"", "name":"SELECCIONAR"},
    	    	{"id":"PENDIENTE", "name":"PENDIENTE"},
    	        {"id":"APROBADO", "name":"APROBADO"},
    	        {"id":"RECHAZADO", "name":"RECHAZADO"},
    	        {"id":"GUARDADO", "name":"GUARDADO"},
    	       // {"id":"CANCELADO", "name":"CANCELADO"}
    	    ]
    	});
    	
    	
    	
    	Ext.define('statusCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.fiscalTitle22,
    	    store: invStatus,
    	    alias: 'widget.combostatus',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    editable: false,
    	    displayField: 'name',
			//width:150,
    	    flex : .2,
    	    //labelWidth:40,
    	    valueField: 'id',
    	    //margin:'20 20 0 10',
    	    id:'combostatus',
    	    listeners: {
    	        afterrender: function() {
    	        	   this.setValue("");    
    	        }
    	    }
    	});
    	
        this.columns = [
		        	{
			            text     : 'Id de Solicitud',
			            //width: 100,
			            flex: 1,
			            dataIndex: 'id',
			            hidden:true
			        },{
			            text     : SuppAppMsg.approvalRequestDate,
			            maxWidth: 130,
			            flex: 1,
			            dataIndex: 'fechaSolicitudStr',
			            //renderer : Ext.util.Format.dateRenderer("d-m-Y"),
			        },{
			            text     : 'UUID',
			            //width: 230,
			            flex: 1,
			            dataIndex: 'rfc',
			            hidden:true
			        },{
			            text     : SuppAppMsg.plantAccess51,
			            maxWidth: 150,
			            flex: 1,
			            dataIndex: 'addressNumberPA',
			        },{
			            text     : SuppAppMsg.plantAccess64,
			            //width: 230,
			            flex: 1,
			            dataIndex: 'razonSocial', 
			        },{
			            text     : SuppAppMsg.plantAccess2,
			            //width: 230,
			            flex: 1,
			            dataIndex: 'nameRequest',
			        },{
			            text     : SuppAppMsg.fiscalTitle22,
			            //width: 230,
			            flex: 1,
			            dataIndex: 'status'
			        },{
			            text     : 'Vigente',
			            //width: 110,
			            flex: 1,
			            hidden:true,
			            dataIndex: 'status'
			        },{
			            text     : SuppAppMsg.plantAccess3,
			            //width: 110,
			            flex: 1,
			            dataIndex: 'aprovUser'
			        }, 	{
			        	xtype: 'actioncolumn', 
			            //width: 90,
			        	flex: 1,
			            header: SuppAppMsg.taxvaulRequest,
			            align: 'center',
						name : 'plantAccessReportBatch',
//						hidden: role == 'ROLE_SUPPLIER'?true:false,
						itemId : 'plantAccessReportBatch',
			            style: 'text-align:center;',
			            items: [
			            	{
			            		icon:'resources/images/archivo-pdf.png',
			            		getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			            				if(r.data.status != "APROBADO") {
				              	              return "x-hidden-display";
				              	          }
				              	      },
			             	      text: SuppAppMsg.freightApprovalReportBatch,
				                  handler: function(grid, rowIndex, colIndex) {
				                	 
				                	  var record = grid.store.getAt(rowIndex);
										var href = "plantAccess/plantAccessPDF.action?uuid=" + record.data.rfc;
										window.open(href, '_blank');
										//setTimeout(function(){ newWindow.document.title = 'Plant Access PDF'; }, 10);
			                  }}]
			        },{
			        	xtype: 'actioncolumn', 
			            //width: 90,
			        	flex: 1,
			            header: SuppAppMsg.approvalApprove,
			            align: 'center',
			            hidden: role=='ROLE_PURCHASE_READ' || role == 'ROLE_SUPPLIER'?true:false,
						name : 'approvePlantAccess',
						itemId : 'approvePlantAccess',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/accept.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              	          if(!(r.data.status == "PENDIENTE" && r.data.aprovUser.includes(userName) )) {
			              	              return "x-hidden-display";
			              	          }
			              	      },
			              	      text: SuppAppMsg.approvalApprove,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        },{
			        	xtype: 'actioncolumn', 
			            //width: 90,
			        	flex: 1,
			            header: SuppAppMsg.approvalReject,
			            align: 'center',
						name : 'rejectInvoiceFDA',
						//hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
						hidden: role=='ROLE_PURCHASE_READ' || role == 'ROLE_SUPPLIER'?true:false,
						itemId : 'rejectInvoiceFDA',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/close.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		 if(!(r.data.status == "PENDIENTE" && r.data.aprovUser.includes(userName) )) {
		              	              return "x-hidden-display";
		              	          }
			              	      },
			              	      text: SuppAppMsg.approvalReject,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        }];
        
        this.dockedItems = [
        	{   xtype: 'toolbar',
                dock: 'top',
                layout: {
                    type: 'hbox',
                    align: 'middle',
                    pack: 'start'
                },
                defaults: {
                    labelAlign: 'top'
                },
                items: [
                	{
    			xtype: 'textfield',
                fieldLabel: 'Aprobador',
                id: 'approverPlantAccess',
                itemId: 'approverPlantAccess',
                name:'approverPlantAccess',
                //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
                value: role != 'ROLE_SUPPLIER' ?userName:'',
                readOnly: true,
                hidden:true,
                //width:200,
                //labelWidth:50,
                flex :.3 ,
                //margin:'20 20 0 10'
    		},{
    			xtype: 'textfield',
                fieldLabel: SuppAppMsg.plantAccess51, 
                id: 'addressNumberGrid',
                itemId: 'addressNumberGrid',
                name:'addressNumberGrid',
                //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
                value: role.includes('ROLE_SUPPLIER') ?addressNumber:'',
                readOnly: role.includes('ROLE_SUPPLIER') ?true:false,
                hidden: role.includes('ROLE_SUPPLIER') ?true:false,		
                //hidden: role.includes('ROLE_SUPPLIER')?true:false,
                //width:200,
                //labelWidth:50,
                flex :.3 ,
                //margin:'20 20 0 10'
    		},{
			xtype: 'textfield',
            fieldLabel: SuppAppMsg.supplierForm5.substring(0,SuppAppMsg.supplierForm5.length - 1), 
            id: 'RFC',
            itemId: 'RFC',
            name:'RFC',
            //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
            //value: role == 'ROLE_SUPPLIER'?displayName:'',
            readOnly: role == 'ROLE_SUPPLIER' ?true:false,
            hidden: role == 'ROLE_SUPPLIER' ?true:false,
            //hidden: role == 'ROLE_SUPPLIER'?true:false,
            //width:200,
            flex :.4		
            //labelWidth:50,
            //margin:'20 20 0 10'
		},{ 
			xtype: 'combostatus'
		},{
	        xtype: 'displayfield',
	        value: '',
	        flex:.5
	    }]
        	},        
        	{
            	xtype: 'toolbar',
                dock: 'top',
                layout: {
                    type: 'hbox',
                    align: 'middle',
                    pack: 'start'
                },
                defaults: {
                    //margin: '0 20 0 10' 
                },
                items: [{
       		xtype:'button',
            text: SuppAppMsg.suppliersSearch,
            iconCls: 'icon-appgo',
            action:'parSearch',
            cls: 'buttonStyle',
           // margin:'0 20 0 10',
            listeners: {
                tap: function (button) {
                	plantAccessController.parSearch(button);
                }
            }
		},{
			iconCls : 'icon-add',
			itemId : 'addPlantAccessRequest',
			id : 'addPlantAccessRequest',
			text : SuppAppMsg.plantAccess1,
			action : 'addNewPlantAccessRequest',
			 cls: 'buttonStyle'
//			hidden: role == 'ROLE_SUPPLIER'?false:true,
		}
		]
        	},
        	getPagingContent()
		];
        
		/*
        this.bbar = Ext.create('Ext.PagingToolbar', {
			store: this.store,
			displayInfo : true,
			beforePageText : SuppAppMsg.page,
			afterPageText :SuppAppMsg.de + ' {0}',
			emptyMsg  : SuppAppMsg.emptyMsg ,
			displayMsg :SuppAppMsg.displayMsg + ' {0} - {1} '+ SuppAppMsg.de +' {2}'
		});
		*/
		
        this.callParent(arguments);
    }
});