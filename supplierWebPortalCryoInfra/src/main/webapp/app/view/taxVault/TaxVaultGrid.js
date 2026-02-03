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
	        refresh: function(view) {
	            var grid = view.up('grid');
	            if (!grid) return;
	            // Usar la función centralizada
	            GridUtils.adjustGridLayout(grid, true);
	        },
	        
	        resize: function(view) {
	            var grid = view.up('grid');
	            if (!grid) return;
	            // Usar la función centralizada
	            GridUtils.adjustGridLayout(grid, false);
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
                        hidden:role==('ROLE_SUPPLIER'),
                        listeners: {
                            specialkey: function(field, e) {
                                if (e.getKey() === e.ENTER) {
                                    // Buscar el botón en el grid completo
                                    var grid = field.up('grid');
                                    if (grid) {
                                        var button = grid.down('button[action="parSearch"]');
                                        if (button) {
                                            // Disparar el evento click del botón
                                            button.fireEvent('click', button);
                                        }
                                    }
                                }
                            }
                        }
                	},{
            			xtype: 'textfield',
                        fieldLabel: SuppAppMsg.taxvaultIssuerRFC,
                        id: 'rfcEmisor',
                        itemId: 'rfcEmisor',
                        name:'rfcEmisor',
                        hidden:role==('ROLE_FISCAL_PRD'),
                        listeners: {
                            specialkey: function(field, e) {
                                if (e.getKey() === e.ENTER) {
                                    // Buscar el botón en el grid completo
                                    var grid = field.up('grid');
                                    if (grid) {
                                        var button = grid.down('button[action="parSearch"]');
                                        if (button) {
                                            // Disparar el evento click del botón
                                            button.fireEvent('click', button);
                                        }
                                    }
                                }
                            }
                        }
            		},{
            			xtype: 'textfield',
                        fieldLabel: 'UUID',
                        id: 'tvUUID',
                        itemId: 'tvUUID',
                        name:'tvUUID',
                        listeners: {
                            specialkey: function(field, e) {
                                if (e.getKey() === e.ENTER) {
                                    // Buscar el botón en el grid completo
                                    var grid = field.up('grid');
                                    if (grid) {
                                        var button = grid.down('button[action="parSearch"]');
                                        if (button) {
                                            // Disparar el evento click del botón
                                            button.fireEvent('click', button);
                                        }
                                    }
                                }
                            }
                        }
            		},{
						xtype: 'comboType',
						listeners: {
                            specialkey: function(field, e) {
                                if (e.getKey() === e.ENTER) {
                                    // Buscar el botón en el grid completo
                                    var grid = field.up('grid');
                                    if (grid) {
                                        var button = grid.down('button[action="parSearch"]');
                                        if (button) {
                                            // Disparar el evento click del botón
                                            button.fireEvent('click', button);
                                        }
                                    }
                                }
                            }
                        }
					},{
            			xtype: 'datefield',
                        fieldLabel: SuppAppMsg.purchaseOrderDesde,
                        id: 'tvFromDate',
                        itemId: 'tvFromDate',
                        name:'tvFromDate',
                        allowBlank:false,
                        format: 'd/m/Y',
                        maxValue: new Date(), // Fecha máxima, hoy
                        value: Ext.Date.add(new Date(), Ext.Date.MONTH, -3), //Fecha inicial Desde 3 meses hacia atrás
                        	listeners:{
            					change: function(field, newValue, oldValue){
            						Ext.getCmp("tvToDate").setMinValue(newValue);
            					},
            					specialkey: function(field, e) {
                                    if (e.getKey() === e.ENTER) {
                                        // Buscar el botón en el grid completo
                                        var grid = field.up('grid');
                                        if (grid) {
                                            var button = grid.down('button[action="parSearch"]');
                                            if (button) {
                                                // Disparar el evento click del botón
                                                button.fireEvent('click', button);
                                            }
                                        }
                                    }
                                }
            				},
            		},{
            			xtype: 'datefield',
                        fieldLabel: SuppAppMsg.purchaseOrderHasta,
                        id: 'tvToDate',
                        itemId: 'tvToDate',
                        name:'tvToDate',
                        maxValue: new Date(), // Fecha máxima, hoy
                      //  value: new Date(), //Fecha inicial hoy
                       // maxValue: serverDate, // Fecha máxima, hoy
                        value: serverDate, //Fecha inicial hoy
                        allowBlank:false,
                        format: 'd/m/Y',
                        listeners: {
                            specialkey: function(field, e) {
                                if (e.getKey() === e.ENTER) {
                                    // Buscar el botón en el grid completo
                                    var grid = field.up('grid');
                                    if (grid) {
                                        var button = grid.down('button[action="parSearch"]');
                                        if (button) {
                                            // Disparar el evento click del botón
                                            button.fireEvent('click', button);
                                        }
                                    }
                                }
                            }
                        }
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