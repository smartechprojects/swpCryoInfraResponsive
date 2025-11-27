Ext.define('SupplierApp.view.taxVault.TaxVaultGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.taxVaultGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',  
    store : {
		type:'taxvault'
	},
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
	                var pageSize = 1;
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

		                 // Número real de filas visibles
		                 var realRowCount = rows.length;

		                 // Número deseado (12 máximo)
		                 var targetRowCount = 12;

		                 // Calcular altura disponible total
		                 var rowContainerHeight = availableHeight;
		                 
		                 //Cuando el registro es una fila 
		                 if (realRowCount === 1) {

		                     var uniformHeight = rowContainerHeight / targetRowCount;

		                     Ext.get(rows[0]).setHeight(uniformHeight);

		                     Ext.defer(function () {
		                         Ext.get(rows[0]).setHeight(uniformHeight);
		                         grid.updateLayout();
		                     }, 50);

		                     return;
		                 }

		                 // Para 2..11 filas
		                 if (realRowCount > 1 && realRowCount < targetRowCount) {

		                     var uniformHeight = rowContainerHeight / targetRowCount;

		                     Ext.each(rows, function(row) {
		                         Ext.get(row).setHeight(uniformHeight);
		                     });

		                     grid.updateLayout();
		                     return;
		                 }

		                 // Lógica normal para 12 o más filas
		                 var totalContentHeight = 0;
		                 var rowHeights = [];

		                 Ext.each(rows, function(row, index) {
		                     var rowHeight = 25; 
		                     var cells = Ext.get(row).query('.x-grid-cell');
		                     
		                     Ext.each(cells, function(cell) {
		                         var cellEl = Ext.get(cell);
		                         cellEl.setStyle('height', 'auto');
		                         var contentHeight = cellEl.dom.scrollHeight;
		                         if (contentHeight > rowHeight) {
		                             rowHeight = contentHeight + 8; 
		                         }
		                     });

		                     rowHeights[index] = rowHeight;
		                     totalContentHeight += rowHeight;
		                 });

		                 if (totalContentHeight < availableHeight && rows.length > 0) {
		                     var extraHeight = (availableHeight - totalContentHeight) / rows.length;
		                     
		                     Ext.each(rows, function(row, index) {
		                         Ext.get(row).setHeight(rowHeights[index] + extraHeight);
		                     });
		                 } else {
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
    	var apController = SupplierApp.app.getController("SupplierApp.controller.TaxVault");
    	
    	//  this.store = Ext.create('SupplierApp.store.TaxVault');
    	docType = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'name_es','name_en'],
    	    data : [
    	        {"id":"FACTURA", "name_es":"Factura","name_en":"Invoice"},
    	        {"id":"COMPLEMENTO", "name_es":"Complemento Pago","name_en":"Payment Plugin"},
    	        {"id":"NOTA_CREDITO", "name_es":"Nota Crédito","name_en":"Credit Note"},
    	        {"id":"NOTA_CARGO", "name_es":"Nota Cargo","name_en":"Charge Note"}    	        
    	    ]
    	});
    	
    	Ext.define('statusCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.fiscalTitle21,
    	    store: docType,
    	    //editable:false,
    	    alias: 'widget.comboType',
    	    queryMode: 'local',
    	    displayField: window.navigator.language.startsWith("es", 0)? 'name_es':'name_en',
    	    flex:.2,
    	    valueField: 'id',
    	    id:'comboType'
    	});
    	
    	//var docType = null;
    	var invStatus = null;
    	
    	//if(role!=('ROLE_FISCAL_PRD')){
        this.columns = [
					{
						text : 'No.',
						width : 100,
						hidden:true,
						dataIndex : 'id' 
					},
					{
			            text     : 'UUID',
			            maxWidth: 280,
			            flex: 2,
			            dataIndex: 'uuid'
			        },
		        	{
			            text     : SuppAppMsg.taxvaultIssuerRFC,
			            maxWidth: 150,
			            flex: 1,
			            dataIndex: 'rfcEmisor'
			        },{
			            text     : SuppAppMsg.taxvaultreceiverRFC,
			            maxWidth: 150,
			            flex: 1,
			            dataIndex: 'rfcReceptor'
			        },{
			            text     : SuppAppMsg.fiscalTitle21,
			            maxWidth: 150,
			            flex: 1.5,
			            dataIndex: 'type'
			        },{
			            text     : window.navigator.language.startsWith("es", 0)? 'Fecha de Carga':'Upload date',
			            maxWidth: 150,
			            flex: 1.5,
			            dataIndex: 'origen'
			        },{
			            text     : SuppAppMsg.taxvaulUser,
			            //width: 100,
			            flex: 1,
			            dataIndex: 'usuario'
					},{
						text : SuppAppMsg.usersSupplier,
						//width : 100,
						flex: 1,
						dataIndex : 'addressNumber'
			        },{
			            text     : SuppAppMsg.taxvaulStatus,
			            //width: 150,
			            flex: 1,
			            dataIndex: 'documentStatus'
			        }/*,{
			            text     : window.navigator.language.startsWith("es", 0)? 'Fecha de carga':'Upload date',
			            width: 150,
			            dataIndex: 'origen'
			        }*/, 	{
			        	xtype: 'actioncolumn', 
			            //width: 90,
			        	flex: 1,
			            header: SuppAppMsg.taxvaulRequest,
			            align: 'center',
						name : 'taxVaultReportBatch',
						hidden: false,
						itemId : 'taxVaultReportBatch',
			            style: 'text-align:center;',
			            items: [
			            	{
			            		icon:'resources/images/archivo-pdf.png',
			            		getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			            			if(r.data.documentStatus != "COMPLETED" ){
				              			return "x-hidden-display";
				              		}
				              	          if(!(role=='ROLE_ADMIN'||role=='ROLE_TAX'||role=='ROLE_TREASURY'||role=='ROLE_ACCOUNTING'||role=='ROLE_MANAGER'||role=='ROLE_BF_ADMIN'||r.data.usuario==userName)) {
				              	        	  
				              	              return "x-hidden-display";
				              	          }
				              	      },
			             	      text: SuppAppMsg.freightApprovalReportBatch,
				                  handler: function(grid, rowIndex, colIndex) {
				                	 
				                	  var record = grid.store.getAt(rowIndex);
										var href = "taxVault/taxVaultPDF.action?id=" + record.data.id;
										var newWindow = window.open(href, '_blank');
										setTimeout(function(){ newWindow.document.title = 'Factura PDF'; }, 10);
			                  }}]
			        },{
			        	xtype: 'actioncolumn', 
			            //width: 150,
			        	flex: 1,
			            header: SuppAppMsg.taxvaultUploandDocuments,
			            align: 'center',
						name : 'upFileExtTaxVault',
						itemId : 'upFileExtTaxVault',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/add.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		if(r.data.documentStatus != "COMPLETED" ){
			              			return "x-hidden-display";
			              		}
			              		  
			              	          if(
              	        		  !(role=='ROLE_ADMIN'||role=='ROLE_BF_ADMIN'||r.data.usuario==userName)) {
              	        	  
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
			            header: SuppAppMsg.taxvaultResendMail,
			            align: 'center',
						name : 'reenvioMailTaxDocument',
						itemId : 'reenvioMailTaxDocument',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/accept.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		if(r.data.documentStatus != "COMPLETED" ){
			              			return "x-hidden-display";
			              		}
			              	          if(
              	        		  !(role=='ROLE_ADMIN'||role=='ROLE_TAX'||role=='ROLE_TREASURY'||role=='ROLE_ACCOUNTING'||role=='ROLE_MANAGER'||role=='ROLE_BF_ADMIN')) {
              	        	  
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
			            header: SuppAppMsg.taxvaultDelete,
			            align: 'center',
						name : 'eliminarTaxVaultDocument',
						hidden: role=='ROLE_AUDIT_USR' ? true:false,
						itemId : 'eliminarTaxVaultDocument',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/close.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		if(r.data.documentStatus != "COMPLETED" ){
			              			return "x-hidden-display";
			              		}
			              		   if(
              	        		  !(role=='ROLE_ADMIN'||role=='ROLE_BF_ADMIN')) {
                      	        	  
                      	              return "x-hidden-display";
                      	          }
			              	      },
			              	      text: SuppAppMsg.approvalReject,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        }];
    	
    	this.dockedItems = [
    		{
                xtype: 'toolbar',
                dock: 'top',
                padding: '0',
                layout: { 
                	type: 'hbox'
              		  },
                defaults: { 
                        labelAlign: 'top'
                		},
                items: [
                	{
						xtype : 'combo',
						fieldLabel : SuppAppMsg.taxvaultreceiverRFC,
						id : 'companyBF',
						store: Ext.create('SupplierApp.store.Company'),
						valueField : 'company',
						displayField : 'company',
						//editable:false,
                        hidden:role==('ROLE_SUPPLIER')
                	},{
            			xtype: 'textfield',
                        fieldLabel: SuppAppMsg.taxvaultIssuerRFC,
                        id: 'rfcEmisor',
                        itemId: 'rfcEmisor',
                        name:'rfcEmisor',
                        hidden:role==('ROLE_FISCAL_PRD')
            		},{
            			xtype: 'textfield',
                        fieldLabel: 'UUID',
                        id: 'tvUUID',
                        itemId: 'tvUUID',
                        name:'tvUUID'
            		},{
						xtype: 'comboType'
					},{
            			xtype: 'datefield',
                        fieldLabel: SuppAppMsg.purchaseOrderDesde,
                        id: 'tvFromDate',
                        itemId: 'tvFromDate',
                        name:'tvFromDate',
                        allowBlank:false,
                        maxValue: new Date(), // Fecha máxima, hoy
                        value: Ext.Date.add(new Date(), Ext.Date.MONTH, -3), //Fecha inicial Desde 3 meses hacia atrás
                        	listeners:{
            					change: function(field, newValue, oldValue){
            						Ext.getCmp("tvToDate").setMinValue(newValue);
            					}
            				},
            		},{
            			xtype: 'datefield',
                        fieldLabel: SuppAppMsg.purchaseOrderHasta,
                        id: 'tvToDate',
                        itemId: 'tvToDate',
                        name:'tvToDate',
                        maxValue: new Date(), // Fecha máxima, hoy
                        value: new Date(), //Fecha inicial hoy
                        allowBlank:false,
            		},{
				        xtype: 'displayfield',
				        value: '',
				        flex:.4
				    }
                ]
    		},
    		{
                xtype: 'toolbar',
                dock: 'top',
                padding: '0',
                defaults: {
                    margin: '5 5 10 0' 
                },
                layout: {
                    type: 'hbox'
                },
                items: [
                	{
                   		xtype:'button',
                        text: SuppAppMsg.suppliersSearch,
                        iconCls: 'icon-appgo',
                        action:'parSearch',
                        cls: 'buttonStyle',
                        listeners: {
    	                    tap: function (button) {
    	                    	apController.parSearch(button);
    	                    }
    	                }
            		},
            		{
            			iconCls : 'icon-add',
            			itemId : 'addTaxVaultRequest',
            			id : 'addTaxVaultRequest',
            			text : window.navigator.language.startsWith("es", 0)? 'Cargar documento':'Upload document', 
            			action : 'addNewTaxVaultRequest',
                        cls: 'buttonStyle',
            			hidden:!(role==('ROLE_ADMIN')||role==('ROLE_FISCAL_USR')||role==('ROLE_SUPPLIER')||role==('ROLE_BF_ADMIN') ||role=='ROLE_TAX'||role=='ROLE_TREASURY'||role=='ROLE_ACCOUNTING'||role=='ROLE_MANAGER'||role=='ROLE_AUDIT_USR'),
            		},{
            			iconCls : 'icon-add',
            			itemId : 'FiscalPeriodTaxVAult',
            			id : 'FiscalPeriodTaxVAult',
            			text :SuppAppMsg.taxvaultAddPeriod,
            			action : 'addFiscalPeriodTaxVAult',
                        cls: 'buttonStyle',
            			hidden:!(role==('ROLE_ADMIN')||role==('ROLE_FISCAL_PRD')||role==('ROLE_BF_ADMIN')||role=='ROLE_TREASURY'),
            		},{
            	        xtype: 'displayfield',
            	        value: '<div style="color: red;font-size:.8em;">IMPORTANTE Si su factura es por OC u OS no validar por bóveda fiscal, realizar la carga directamente en Órdenes de Compra</div>',
            	        flex:.5
            	    }                
                ]
    		},
		    getPagingContent()	
    	];
    	
		
        this.callParent(arguments);
    }
});