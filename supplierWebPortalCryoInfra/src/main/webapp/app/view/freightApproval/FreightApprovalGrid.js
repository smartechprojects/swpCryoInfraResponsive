Ext.define('SupplierApp.view.freightApproval.FreightApprovalGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.freightApprovalGrid',
    loadMask: true,
	frame:false,
	border:false,
	//flex: 1,
	//autoScroll: true,
	cls: 'extra-large-cell-grid',  
	store : {
		type:'freightapproval'
	},
    scroll : true,
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
    	var apController = SupplierApp.app.getController("SupplierApp.controller.FreightApproval");
    	
    	var docType = null;
    	var banderaAprobacion = false;
    	var invStatus = null;
    	
    	docType = Ext.create('Ext.data.Store', {
        	    fields: ['id', 'name'],
        	    data : [
        	        {"id":"FACTURA", "name":"Factura"},
        	        {"id":"FACT EXTRANJERO", "name":"Factura Extranjeros"}//,{"id":"NOTACREDITO", "name":"Nota de Credito"}
        	    ]
        	});

    	invStatus = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'name'],
    	    data : [    	        
    	        {"id":"PENDIENTE", "name":"PENDIENTE"},
    	        {"id":"APROBADO", "name":"APROBADO"}
    	    ]
    	});
    	
    	ctaPPagar = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'name'],
    	    data : [    	        
    	        {"id":"DP", "name":"DP"},
    	        {"id":"DN", "name":"DN"}
    	    ]
    	});
    	
    	Ext.define('ctaPPagarCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.freightApprovalBillToPay,
    	    store: ctaPPagar,
    	    alias: 'widget.ctaPPagar',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    //editable: false,
    	    displayField: 'name',
			width:200,
    	    labelWidth:120,
    	    valueField: 'id',
    	    margin:'20 20 0 10',
    	    id:'comboCtaPPagarFA',
    	   /* listeners: {
    	        afterrender: function() {
    	           if(role == 'ROLE_WNS'){
    	        	   this.setValue("STATUS_OC_PROCESSED");    
    	           }
    	        }
    	    }*/
    	});
    	
    	Ext.define('docTypeCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.fiscalTitle21,
    	    store: docType,
    	    alias: 'widget.combodoctype',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    //editable: false,
    	    displayField: 'name',
			width:230,
    	    labelWidth:90,
    	    valueField: 'id',
    	    margin:'20 20 0 10',
    	    id:'comboDocumentTypeFA',    	    
    	   /* listeners: {
    	        afterrender: function() {
    	           if(role == 'ROLE_WNS'){
    	        	   this.setValue("STATUS_OC_PROCESSED");    
    	           }
    	        }
    	    }*/
    	});
    	
    	Ext.define('statusCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.fiscalTitle22,
    	    store: invStatus,
    	    alias: 'widget.combostatus',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    //editable: false,
    	    displayField: 'name',
			//width:150,
    	    //labelWidth:40,
    	    valueField: 'id',
    	    //margin:'20 20 0 10',
    	    flex:.3,
    	    id:'comboInvoiceStatusFA',
    	   /* listeners: {
    	        afterrender: function() {
    	           if(role == 'ROLE_WNS'){
    	        	   this.setValue("STATUS_OC_PROCESSED");    
    	           }
    	        }
    	    }*/
    	});
    	
        this.columns = [
        	
		        	{
			        	xtype: 'actioncolumn', 
			            maxWidth: 110,
			        	flex: 1,
			            header: SuppAppMsg.freightApprovalCover,
			            align: 'center',
						name : 'freightApprovalCover',
						hidden: false,
						itemId : 'freightApprovalCover',
			            style: 'text-align:center;',
			           /* items: [
			            	{
			            	icon:'resources/images/archivo-pdf.png',
			              	      text: SuppAppMsg.freightApprovalCover,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]*/
			            items: [
			            	{
			            	icon:'resources/images/archivo-pdf.png',
		             	      text: SuppAppMsg.freightApprovalTitle8,
			                  handler: function(grid, rowIndex, colIndex) {
			                	 
			                	  var record = grid.store.getAt(rowIndex);
			                	
			                	  //var href = "documents/openDocument.action?id=" + 46;
									var href = "freight/openCoverBatchReport.action?batchID=" + record.data.id;
									var newWindow = window.open(href, '_blank');
									//var newWindow = window.open(newdata, "_blank");
									//setTimeout(function(){ newWindow.document.title = 'Caratula Batch'; }, 10);
									setTimeout(function(){ newWindow.document.title = 'Caratula PDF'; }, 10);
			                  //this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        }, 	{
			        	xtype: 'actioncolumn', 
			            maxWidth: 90,
			        	flex: 1,
			            header: SuppAppMsg.freightApprovalReportBatch,
			            align: 'center',
						name : 'freightApprovalReportBatch',
						hidden:role == 'ROLE_SUPPLIER',
						itemId : 'freightApprovalReportBatch',
			            style: 'text-align:center;',
			           /* items: [
			            	{
			            	icon:'resources/images/archivo-pdf.png',
			              	      text: SuppAppMsg.freightApprovalCover,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]*/
			            items: [
			            	{
			            		icon:'resources/images/archivo-pdf.png',
			             	      text: SuppAppMsg.freightApprovalReportBatch,
				                  handler: function(grid, rowIndex, colIndex) {
				                	 
				                	  var record = grid.store.getAt(rowIndex);
				                	
				                	  //var href = "documents/openDocument.action?id=" + 46;
										var href = "freight/openCoverBatchReportBatch.action?batchID=" + record.data.id;
										var newWindow = window.open(href, '_blank');
										//var newWindow = window.open(newdata, "_blank");
										//setTimeout(function(){ newWindow.document.title = 'Caratula Batch'; }, 10);
										setTimeout(function(){ newWindow.document.title = 'Batch PDF'; }, 10);
				                  //this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        },{
			            text     : SuppAppMsg.freightApprovalKey,
			            maxWidth: 100,
			            flex: 1,
			            dataIndex: 'id',
			            itemId : 'freightApprovalId',
			            hidden:role == 'ROLE_SUPPLIER'
			        },{
			            text     : SuppAppMsg.freightApprovalAmount,
			            maxWidth: 120,
			            flex: 1,
			            dataIndex: 'amount',
			            renderer: function(value) {
			            	// formateo de dos ceimales	
			            	var formattedValue = Ext.util.Format.number(value, '0,000.00');
			                return formattedValue;
			            },
			            align: 'right'
			        },{
			            text     : SuppAppMsg.freightApprovalBillToPay,
			            maxWidth: 150,
			            flex: 1,
			            dataIndex: 'serie'
			        },{
			            text     : SuppAppMsg.freightApprovalBudgetAccount,
			            //width: 150,
			            flex: 1,
			            dataIndex: 'accountingAccount',
			            hidden:role == 'ROLE_SUPPLIER'	
			        },{
			            text     : SuppAppMsg.fiscalTitle22,
			            minWidth: 70,
			            flex: 1,
			            dataIndex: 'status'
			        },{
			            text     : SuppAppMsg.approvalLevel,
			            //width: 80,
			            flex: 1,
			            dataIndex: 'approvalStep'
			        },
			        {
			            text     : SuppAppMsg.freightApprovalTitle10,
			            //width: 230,
			            flex: 2,
			            dataIndex: 'semanaPago'
			        },
			        {
			            text     : SuppAppMsg.approvalCurrentApprover,
			            //width: 150,
			            flex: 1,
			            dataIndex: 'currentApprover'
			        },{
			        	xtype: 'actioncolumn', 
			            //width: 90,
			        	flex: 1,
			            header: SuppAppMsg.approvalApprove,
			            align: 'center',
						name : 'approveInvoiceFDA',
						hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'|| role=='ROLE_MANAGER'?false:true,
						itemId : 'approveInvoiceFDA',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/accept.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		  
			              	          if(role == 'ROLE_SUPPLIER' || r.data.status != "PENDIENTE" || (r.data.status == "PENDIENTE" && !r.data.currentApprover.includes(userName))) {
			              	              return "x-hidden-display";
			              	          }else if (r.data.currentApprover.includes(userName)){
			              	        	banderaAprobacion=true;
			              	          }
			              	      },
			              	      text: SuppAppMsg.approvalApprove,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        },{
			        	xtype: 'actioncolumn',
			        	//hidden:role=='ROLE_ADMIN'?false:true,
			            //width: 90,
			        	flex: 1,
			            header: SuppAppMsg.reasignRequest,
			            align: 'center',
						name : 'reasignRequest',
						itemId : 'reasignRequest',
			            style: 'text-align:center;',
			            hidden:true,
			            items: [{
			                text: 'REASIGNAR',
			                iconCls:'user-group',
			                handler: function(grid, rowIndex, colIndex) {
			                	
			                	var record = grid.store.getAt(rowIndex);
			                	var me = this;
			                    var usrstore = searchByRoleAprover('APPROVALFREIGHT',record.data.approvalStep);
			                	var formPanel =  {
			                		        xtype       : 'form',
			                		        height      : 125,
			                		        items       : [
			                		        	{
													fieldLabel : SuppAppMsg.newApprover,
													xtype: 'combobox',
													id:'newApproverId',
													store : usrstore,
									                displayField: 'name',
									                valueField: 'userName',
									                labelWidth:100,
									                width : 350,
									                margin: '10 10 0 10'
												},{
													xtype: 'button',
													width:100,
													text : SuppAppMsg.reasignRequest,
													margin: '12 10 0 10',
													cls: 'buttonStyle',
													iconCls : 'icon-appgo',
													listeners: {
													    click: function() {
													    	
													    	if(Ext.getCmp('newApproverId').getValue() != null && Ext.getCmp('newApproverId').getValue() != ""){
													    		var box = Ext.MessageBox.wait(
										    							SuppAppMsg.supplierProcessRequest,
										    							SuppAppMsg.approvalExecution);
												    			Ext.Ajax.request({
																    url: 'approval/reasignApproverFletes.action',
																    method: 'POST',
																    params: {
																    	id:record.data.id,
																    	newApprover: Ext.getCmp('newApproverId').getValue(),
																    	etapa:record.data.approvalStep
															        },
																    success: function(fp, o) {
																    	var res = Ext.decode(fp.responseText); 
																    	box.hide();
																    	if(res.message == "success"){
																    		grid.store.load();
																    		me.reasignWindow.close();
																    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.newApproverSuccess);
																    	}
															        	
																    },
																    failure: function() {
																    	box.hide();
																    	Ext.MessageBox.show({
															                title: 'Error',
															                msg: 'Surgio un error al actualizar',
															                buttons: Ext.Msg.OK
															            });
																    }
																});
													    	}
													    	
													    }
												    }
												}
			                		        ]
			                		    };
			                	
			                	
			                	me.reasignWindow = new Ext.Window({
			                		layout : 'fit',
			                		title : SuppAppMsg.reasignapproverRequest + record.data.ticketId ,
			                		width : 400,
			                		height : 120,
			                		modal : true,
			                		closeAction : 'destroy',
			                		resizable : true,
			                		minimizable : false,
			                		maximizable : false,
			                		autoScroll: true,
			                		plain : true,
			                		items : [formPanel]
			                	});
			                	
			                	me.reasignWindow.show();
			                }
			            }]
			        }
			        
			        /*,{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.approvalReject,
			            align: 'center',
						name : 'rejectInvoiceFDA',
						hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
						itemId : 'rejectInvoiceFDA',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/close.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		if(role == 'ROLE_SUPPLIER' || r.data.status != "PENDIENTE" || (r.data.status == "PENDIENTE" && r.data.currentApprover != userName)) {
			              	              return "x-hidden-display";
			              	          }
			              	      },
			              	      text: SuppAppMsg.approvalReject,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        }*/];
        
        
        
        
        this.dockedItems = [
            /*{
              xtype: 'toolbar',
              dock: 'top',
              items: [
            	  {
          			iconCls : 'icon-save',
          			itemId : 'uploadNewFiscalDocFA',
          			id : 'uploadNewFiscalDocFA',
          			text : SuppAppMsg.fiscalTitle18,
          			hidden:true,
          			action : 'uploadNewFiscalDoc'
          		},{
          			name : 'searchFiscalDocumentsFA',
          			itemId : 'searchFiscalDocumentsFA',
          			emptyText : SuppAppMsg.fiscalTitle19,
          			xtype : 'trigger',
          			width : 300,
          			hidden:true,
          			triggerCls : 'x-form-search-trigger',
          			onTriggerClick : function(e) {
          				this.fireEvent("ontriggerclick", this, event);
          			},
          			enableKeyEvents : true,
          			listeners : {
          				specialkey : function(field, e) {
          					if (e.ENTER === e.getKey()) {
          						field.onTriggerClick();
          					}
          				}
          			}
          		}
              ]},*/
             {
                xtype: 'toolbar',
                dock: 'top',
                layout: {
                    type: 'hbox',
                    align: 'middle',
                    pack: 'start'
                },
                //padding: 5,
                defaults: {
                    labelAlign: 'top'
                },
                items: [
                	{
            			xtype: 'textfield',
                        fieldLabel: SuppAppMsg.freightApprovalTitle10,
                        id: 'semanaPagoFA',
                        itemId: 'semanaPagoFA',
                        name:'semanaPagoFA',
                        //width:300,
                        //labelWidth:120,
                        //margin:'20 20 0 10'
                        flex:.3,
            		},{
            			xtype: 'textfield',
                        fieldLabel: SuppAppMsg.freightApprovalBudgetAccount,
                        id: 'ctaPresupuestalFA',
                        itemId: 'ctaPresupuestalFA',
                        name:'ctaPresupuestalFA',
                        //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
                        //fieldStyle: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?'border:none;background-color: #ddd; background-image: none;':'',
                        //readOnly: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?true:false,
                        hidden:role == 'ROLE_SUPPLIER',
                        //width:300,
                        //labelWidth:120,
                        //margin:'20 20 0 10'
                        flex:.5,
            		},{
            			xtype: 'textfield',
                        fieldLabel: SuppAppMsg.freightApprovalTitle6,
                        id: 'batchIdParam',
                        itemId: 'batchIdParam',
                        name:'batchIdParam',
                        hidden:role == 'ROLE_SUPPLIER',
                        //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
                        //fieldStyle: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?'border:none;background-color: #ddd; background-image: none;':'',
                        //readOnly: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?true:false,
                        //width:300,
                        //labelWidth:120,
                        //margin:'20 20 0 10'
                        flex:.3,
            		},{ 
            			xtype: 'combostatus',
            			value: role == 'ROLE_SUPPLIER'?'APROBADO':'',
            			readOnly:  role == 'ROLE_SUPPLIER'		       		
            		},{
						xtype: 'displayfield',
			            value: '',
			            flex:.8
		            	}
            		
                	 
              ]},
              {
                  xtype: 'toolbar',
                  dock: 'top',
                  layout: {
                      type: 'hbox',
                      //align: 'middle'
                  },
                  //padding: 5,
                  items: [
                	  {
                     		xtype:'button',
                          text: SuppAppMsg.suppliersSearch,
                          iconCls: 'icon-appgo',
                          action:'fdSearch',
                          cls: 'buttonStyle',
                          listeners: {
      	                    tap: function (button) {
      	                    	apController.fdSearch(button);
      	                    }
      	                },
              		}     	 
                ]},
                getPagingContent()
      ];

 
        this.callParent(arguments);
        if(banderaAprobacion){
        	 Ext.getCmp('approveInvoiceFDA').hidden=false;
        }
    },
    listeners: {
        afterlayout: function (grid) {
            var view = grid.getView();
            if (!view || !view.rendered) return;

            var totalWidth = 0;
            grid.columns.forEach(function (col) {
                if (!col.hidden) totalWidth += col.getWidth();
            });

            var viewWidth = view.getEl().getWidth();
            if (viewWidth - totalWidth > 20) {
                var extra = (viewWidth - totalWidth) / grid.columns.length;
                Ext.suspendLayouts();
                grid.columns.forEach(function (col) {
                    if (!col.hidden) col.setWidth(col.getWidth() + extra);
                });
                Ext.resumeLayouts(true);
            }
        }
    },
});