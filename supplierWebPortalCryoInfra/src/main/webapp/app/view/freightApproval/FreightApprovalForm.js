Ext.define('SupplierApp.view.freightApproval.FreightApprovalForm',	{
	extend : 'Ext.form.Panel',
	alias : 'widget.freightApprovalForm',
	border : false,
	frame : false,
	style : 'border: solid #ccc 1px',
	autoScroll : true,
	initComponent : function() {
	
		this.items = [
								{
									xtype : 'hidden',
									name : 'id'
								},
								{
									xtype : 'container',
									layout : 'hbox',
									defaults : {
										labelWidth : 120,
										xtype : 'textfield',
										margin : '10 0 0 10',
										align : 'stretch',
										fieldStyle : 'padding-bottom:5px;font-size:18px;vertical-align:top;border:none;background:transparent;color:black;font-weight:bold',
										readOnly : true
									},
									items : [
											{
												fieldLabel : SuppAppMsg.freightApprovalTitle6,
												name : 'idFDC',
												width : 195,
												labelWidth : 90,
												itemId : 'idFDC',
												id: 'idFDC'
												
											},
											{
												fieldLabel : SuppAppMsg.freightApprovalTitle7,
												labelWidth : 80,
												name : 'amountFDC',
												width : 250,
												itemId : 'amountFDC',
												id: 'amountFDC',
												xtype : 'numericfield',
												currencySymbol : '$ ',
												useThousandSeparator : true,
												decimalSeparator : '.',
												thousandSeparator : ',',
												alwaysDisplayDecimals : true
											}/*,
											{
												xtype : 'textfield',
												labelWidth : 70,
												width : 200,
												hidden : true,
												fieldLabel : SuppAppMsg.purchaseOrderSupplier,
												name : 'addressNumber'
											}, {
												xtype : 'textfield',
												width : 450,
												hidden : true,
												id : 'description'
													
											}, {
												xtype : 'textfield',
												hidden : true,
												name : 'addressNumberRole',
												itemId : 'addressNumberRole',
												id : 'addressNumberRole'
											}, {
												hidden : true,
												name : 'supplierCountry',
												itemId : 'supplierCountry',
												id : 'supplierCountry'
											}, {
												hidden : true,
												name : 'supplierTaxId',
												itemId : 'supplierTaxId',
												id : 'supplierTaxId'
											}, {
												hidden : true,
												name : 'supplierAddress',
												itemId : 'supplierAddress',
												id : 'supplierAddress'
											}, {
												hidden : true,
												name : 'supplierName',
												itemId : 'supplierName',
												id : 'supplierName'
											} */]
								}/*,
								{
									xtype : 'container',
									layout : 'hbox',
									defaults : {
										labelWidth : 120,
										xtype : 'datefield',
										align : 'stretch',
										margin : '10 0 0 10',
										readOnly : true,
										width : 280,
										format : 'd-m-Y',
										fieldStyle : 'padding-bottom:5px;font-size:15px;border:none;background:transparent;color:black;font-weight:bold'
									},
									items : [
											{
												fieldLabel : SuppAppMsg.purchaseOrderFechaAprovacion,
												hidden : true,
												name : 'promiseDelivery'
											},
											{
												fieldLabel : 'Fecha de la orden',
												name : 'rateRequested',
												hidden : true
											},
											{
												labelWidth : 50,
												width : 250,
												fieldLabel : SuppAppMsg.fiscalTitle16,
												name : 'orderAmount',
												xtype : 'numericfield',
												currencySymbol : '$ ',
												useThousandSeparator : true,
												decimalSeparator : '.',
												thousandSeparator : ',',
												hidden : true,
												alwaysDisplayDecimals : true
											},
											{
												xtype : 'textfield',
												name : 'currecyCode',
												width : 100
											},
											{
												labelWidth : 140,
												fieldLabel : 'Fecha estimada de pago',
												name : 'estimatedPaymentDate',
												id : 'estimatedPaymentDateId',
												hidden : true,
												width : 280
											}, {
												xtype : 'textfield',
												name : 'headerNotes',
												id : 'headerNotes',
												itemId : 'headerNotes',
												hidden : true,
												width : 20
											}, {
												xtype : 'textfield',
												name : 'rejectNotes',
												id : 'rejectNotes',
												itemId : 'rejectNotes',
												hidden : true,
												width : 20
											} ]
								},
								{
									xtype : 'container',
									layout : 'hbox',
									defaults : {
										labelWidth : 70,
										xtype : 'textfield',
										align : 'stretch',
										margin : '10 0 0 10',
										readOnly : true,
										width : 880,
										fieldStyle : 'padding-bottom:5px;font-size:15px;border:none;background:transparent;color:black;font-weight:bold'
									},
									items : [
											{
												fieldLabel : SuppAppMsg.paymentTitle1,
												hidden : true,
												name : 'longCompanyName'
											},
											{
												xtype : 'button',
												iconCls : 'icon-document',
												id : "headerNotesButton",
												width : 110,
												hidden : true,
												text : SuppAppMsg.purchaseTitle33,
												handler : function() {
													var content = Ext
															.getCmp('headerNotes');
													var win = new Ext.Window(
															{
																title : SuppAppMsg.purchaseTitle33,
																layout : 'fit',
																autoScroll : true,
																width : 1050,
																height : 440,
																modal : true,
																closeAction : 'destroy',
																resizable : false,
																minimizable : false,
																maximizable : false,
																plain : true,
																bodyStyle : "padding:10 10 0 10px;background:#fff;",
																html : content.value
															});

													win.show();
												}
											},
											{
												xtype : 'button',
												iconCls : 'icon-appgo',
												id : "rejectNotesButton",
												width : 110,
												hidden : true,
												text : SuppAppMsg.purchaseTitle34,
												handler : function() {
													var content = Ext
															.getCmp('rejectNotes');
													var win = new Ext.Window(
															{
																title : SuppAppMsg.purchaseTitle35,
																layout : 'fit',
																autoScroll : true,
																width : 850,
																height : 440,
																modal : true,
																closeAction : 'destroy',
																resizable : false,
																minimizable : false,
																maximizable : false,
																plain : true,
																bodyStyle : "padding:10 10 0 10px;background:#fff;",
																html : content.value
															});

													win.show();
												}
											} ]
								} */];

						this.tbar = [
								{
									iconCls : 'icon-save',
									itemId : 'uploadFiscalDocFDC',
									id : 'uploadFiscalDocFDC',
									text : SuppAppMsg.purchaseTitle36,
									action : 'uploadFiscalDocFDC',
									hidden: true
},{
									iconCls : 'icon-save',
									itemId : 'uploadMultipleFiscalDocFDC',
									id : 'uploadMultipleFiscalDocFDC',
									text : 'Cargar Factura con Nota de Crédito',
									action : 'uploadMultipleFiscalDocFDC',
									hidden: true
},{
									iconCls : 'icon-add',
									itemId : 'uploadFiscalDocNoPaymentFDC',
									id : 'uploadFiscalDocNoPaymentFDC',
									text : 'Cargar Factura con metodo de pago diferente',
									action : 'uploadFiscalDocNoPaymentFDC',
									hidden : true
								}, {
									iconCls : 'icon-save',
									itemId : 'openForeignDocFDC',
									id : 'openForeignDocFDC',
									text : 'Abrir factura foránea',
									action : 'openForeignDocFDC',
									hidden : true
								}, {
									iconCls : 'icon-grid',
									itemId : 'getInvoiceCodesFDC',
									id : 'getInvoiceCodesFDC',
									text : 'Conceptos Factura',
									action : 'getInvoiceCodesFDC',
									hidden : true
								} ];
						this.callParent(arguments);
					}

});