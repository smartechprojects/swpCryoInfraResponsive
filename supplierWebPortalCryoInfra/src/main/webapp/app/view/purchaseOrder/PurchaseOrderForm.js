Ext.define('SupplierApp.view.purchaseOrder.PurchaseOrderForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.purchaseOrderForm',
	border : false,
	frame : false,
	style: 'border: solid #ccc 1px',
	scrollable : true,
	initComponent : function() {
		
		
		this.items = [{
						xtype : 'hidden',
						name : 'id'
					},{
						xtype : 'displayfield',
						value : SuppAppMsg.purchaseOrdersTitle2,
						fieldStyle: 'font-size:16px;font-weight:bold',
						hidden:role!='ROLE_SUPPLIER' && role!='ROLE_SUPPLIER_OPEN'?false:true
						},{
					xtype: 'container',
					layout: 'hbox',
					width: '100%',
					defaults : {
						//labelWidth : 120,
						flex : 1,
						xtype : 'textfield',
						margin: '10 0 0 10',
						align: 'stretch',
						labelAlign: 'top',
						fieldStyle: 'padding-bottom:5px;font-size:13px;vertical-align:top;border:none;background:transparent;color:black;font-weight:bold',
						readOnly:true
					},
			        items:[{
							name : 'orderCompany',
							width : 195,
							labelWidth : 90,
							hidden:true
							},{
							fieldLabel : SuppAppMsg.purchaseOrderNumber,
							name : 'orderNumber',
							maxWidth : 100,
							//labelWidth : 90,
				            flex:1
							},{
							fieldLabel : SuppAppMsg.purchaseOrderType,
							//labelWidth : 30,
							name : 'orderType',
							maxWidth : 100,
							// minWidth: 80,        // no más chico que esto
				             //maxWidth: 80,
				             flex:1
							},{
							xtype : 'textfield',
							//labelWidth : 70,
							maxWidth : 150,
							fieldLabel : SuppAppMsg.purchaseOrderSupplier,
							name : 'addressNumber',
				             flex:1
							},{
							fieldLabel : SuppAppMsg.paymentTitle1,
							name : 'longCompanyName',
					        flex:1,
					        maxWidth : 400,
							},{
							xtype : 'datefield',
							fieldLabel : SuppAppMsg.purchaseOrderFechaAprovacion,
							name : 'promiseDelivery',
				            flex:1,
				            format: 'd-m-Y',
				            maxWidth : 150,
							},{
							xtype : 'textfield',
							width:450,
							id : 'description',
							hidden : true
							},{
							xtype : 'textfield',
							hidden:true,
							name : 'addressNumberRole',
							itemId: 'addressNumberRole',
							id: 'addressNumberRole'
							},{
								hidden:true,
								name : 'supplierCountry',
								itemId: 'supplierCountry',
								id: 'supplierCountry'
							},{
								hidden:true,
								name : 'supplierTaxId',
								itemId: 'supplierTaxId',
								id: 'supplierTaxId'
							},{
								hidden:true,
								name : 'supplierAddress',
								itemId: 'supplierAddress',
								id: 'supplierAddress'
							},{
								hidden:true,
								name : 'supplierName',
								itemId: 'supplierName',
								id: 'supplierName'
							}
					      ]},{
							xtype: 'container',
							layout: 'hbox',
							width: '100%',
							defaults : {
								//labelWidth : 120,
								xtype : 'datefield',
								align: 'stretch',
								margin: '10 0 10 10',
								readOnly:true,
								flex:1,
								border: false, 
								//width : 280,
								labelAlign: 'top',
								format: 'd-m-Y',
								fieldStyle: 'padding-bottom:5px;font-size:13px;border:none;background:transparent;color:black;font-weight:bold'
							},
					        items:[/*{
									fieldLabel : SuppAppMsg.purchaseOrderFechaAprovacion,
									name : 'promiseDelivery',
						             flex:1,
									}*/,{
									fieldLabel : 'Fecha de la orden',
									name : 'rateRequested',
									hidden:true
									},{
									//labelWidth : 50,
								    maxWidth:150,
						            flex:1,
									fieldLabel : SuppAppMsg.fiscalTitle16,
									name : 'orderAmount',
									xtype: 'numericfield',
									currencySymbol: '$ ',
				                    useThousandSeparator: true,
				                    decimalSeparator: '.',
				                    thousandSeparator: ',',
				                    alwaysDisplayDecimals: true
								},{
						        	xtype : 'textfield',
						        	fieldLabel : SuppAppMsg.purchaseOrderCurrency,
						        	name : 'currecyCode',
									maxWidth:100,
						            flex:1,
						            margin: '10 0 10 10',
						            fieldStyle: 'padding-bottom:5px;font-size:13px;border:none;background:transparent;color:black;font-weight:bold',
					                labelAlign: 'top'
								},{
						        	labelWidth : 140,
						        	fieldLabel : 'Fecha estimada de pago',
						        	name : 'estimatedPaymentDate',
						        	id : 'estimatedPaymentDateId',
						        	hidden:true,
									width:280
								},{
									xtype : 'textfield',
									name : 'headerNotes',
									id: 'headerNotes',
									itemId:'headerNotes',
									hidden:true,
									width : 20
								},{
									xtype : 'textfield',
									name : 'rejectNotes',
									id: 'rejectNotes',
									itemId:'rejectNotes',
									hidden:true,
									width : 20
								}
							]},{
								xtype: 'container',
								layout: 'hbox',
								width: '100%',
								defaults : {
									//labelWidth : 70,
									xtype : 'textfield',
									align: 'stretch',
									margin: '10 0 10 10',
									readOnly:true,
									maxWidth : 400,
									border: false, 
									flex:1,
                                    labelAlign: 'top',
									fieldStyle: 'padding-bottom:5px;font-size:13px;border:none;background:transparent;color:black;font-weight:bold'
								},
						        items:[/*{
										fieldLabel : SuppAppMsg.paymentTitle1,
										name : 'longCompanyName',
								             flex:1
										}*/,
										{
											xtype: 'button',
											iconCls : 'icon-document',
											id:"headerNotesButton",
											width:110,
											hidden:true,
											text: SuppAppMsg.purchaseTitle33,
											handler: function() {
												var content = Ext.getCmp('headerNotes');
												var win=new Ext.Window({
													title: SuppAppMsg.purchaseTitle33, 
													layout:'fit',
													autoScroll: true,
							                        width:1050,
							                        height:440,
							                        modal : true,
							                		closeAction : 'destroy',
							                		resizable : false,
							                		minimizable : false,
							                		maximizable : false,
							                		plain : true,
							                		bodyStyle:"padding:10 10 0 10px;background:#fff;",
							                		html: content.value
												});
	
												win.show();
											}
										},
										{
											xtype: 'button',
											iconCls : 'icon-appgo',
											id:"rejectNotesButton",
											width:110,
											hidden:true,
											text: SuppAppMsg.purchaseTitle34,
											handler: function() {
												var content = Ext.getCmp('rejectNotes');
												var win=new Ext.Window({
													title: SuppAppMsg.purchaseTitle35, 
													layout:'fit',
													autoScroll: true,
							                        width:850,
							                        height:440,
							                        modal : true,
							                		closeAction : 'destroy',
							                		resizable : false,
							                		minimizable : false,
							                		maximizable : false,
							                		plain : true,
							                		bodyStyle:"padding:10 10 0 10px;background:#fff;",
							                		html: content.value
												});
	
												win.show();
											}
										}
								]}
			];

		this.tbar = [ {
			iconCls : 'icon-save',
			itemId : 'uploadFiscalDoc',
			id : 'uploadFiscalDoc',
			text : SuppAppMsg.purchaseTitle36,
			action : 'uploadFiscalDoc'
		},{
			iconCls : 'icon-save',
			itemId : 'uploadMultipleFiscalDoc',
			id : 'uploadMultipleFiscalDoc',
			text : 'Cargar Factura con Nota de Crédito',
			action : 'uploadMultipleFiscalDoc',
			hidden: true
		},{
			iconCls : 'icon-add',
			itemId : 'uploadFiscalDocNoPayment',
			id : 'uploadFiscalDocNoPayment',
			text : 'Cargar Factura con metodo de pago diferente',
			action : 'uploadFiscalDocNoPayment',
			hidden:true
		},{
			iconCls : 'icon-save',
			itemId : 'openForeignDoc',
			id : 'openForeignDoc',
			text : 'Abrir factura foránea',
			action : 'openForeignDoc',
			hidden:true
		},{
			iconCls : 'icon-grid',
			itemId : 'getInvoiceCodes',
			id : 'getInvoiceCodes',
			text : 'Conceptos Factura',
			action : 'getInvoiceCodes',
			hidden:true
		}];
		this.callParent(arguments);
	}

});