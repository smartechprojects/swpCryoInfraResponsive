Ext.define('SupplierApp.view.customBroker.CustomBrokerForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.customBrokerForm',
	border : false,
	frame : false,
	style: 'border: solid #ccc 1px',
	autoScroll : true,
	initComponent : function() {
		this.items = [{
						xtype : 'hidden',
						name : 'id'
					},{
					xtype: 'container',
					layout: 'vbox',
					defaults : {
						labelWidth : 120,
						xtype : 'textfield',
						margin: '0 0 0 10',
						align: 'stretch',
						width:380,
						fieldStyle: 'padding-bottom:5px;font-size:12px;vertical-align:top;border:none;background:transparent;color:black;font-weight:bold',
						readOnly:true
					},
			        items:[{
							fieldLabel : SuppAppMsg.fiscalTitle5,
							name : 'uuidFactura',
							width : 500
				        },{
				        	fieldLabel : SuppAppMsg.suppliersName,
				            name: 'supplierName',
				            width: 500
			        	}, {
							fieldLabel : SuppAppMsg.suppliersNumber,
							name : 'addressNumber'
						}, {
							fieldLabel : SuppAppMsg.fiscalTitle10,
							name : 'rfcEmisor'
						}, {
							fieldLabel : SuppAppMsg.fiscalTitle11,
							name : 'rfcReceptor'
						}, {
							fieldLabel : SuppAppMsg.fiscalTitle3,
							name : 'folio'
						}, {
							fieldLabel : SuppAppMsg.fiscalTitle4,
							name : 'serie'
						}, {
							fieldLabel : SuppAppMsg.purchaseOrderCurrency,
							name : 'moneda'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitle6,
							name : 'subtotal',
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						}, /*{
							fieldLabel : SuppAppMsg.fiscalTitle7,
							name : 'descuento'
						},*/ {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitle8,
							name : 'impuestos',
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitle23,
							name : 'advancePayment',
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitle9,
							name : 'amount',
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlMainInvoice',
							height: 30,							
							margin: '0 0 10 140',
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitle20,
							name : 'conceptTotalAmount',
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleCNT,
							name : 'conceptAmount001',
							id : 'conceptAmount001',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept001',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleValidation,
							name : 'conceptAmount002',
							id : 'conceptAmount002',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept002',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleManeuvers,
							name : 'conceptAmount003',
							id : 'conceptAmount003',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept003',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation,
							name : 'conceptAmount004',
							id : 'conceptAmount004',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept004',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleRedMan,
							name : 'conceptAmount005',
							id : 'conceptAmount005',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept005',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleFumigation,
							name : 'conceptAmount006',
							id : 'conceptAmount006',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept006',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleDocking,
							name : 'conceptAmount007',
							id : 'conceptAmount007',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept007',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleStorage,
							name : 'conceptAmount008',
							id : 'conceptAmount008',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept008',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleDelays,
							name : 'conceptAmount009',
							id : 'conceptAmount009',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept009',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleDragging,
							name : 'conceptAmount010',
							id : 'conceptAmount010',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept010',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitlePermissions,
							name : 'conceptAmount011',
							id : 'conceptAmount011',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept011',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleDuties,
							name : 'conceptAmount012',
							id : 'conceptAmount012',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept012',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1',
							name : 'conceptAmount013',
							id : 'conceptAmount013',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept013',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2',
							name : 'conceptAmount014',
							id : 'conceptAmount014',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept014',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3',
							name : 'conceptAmount015',
							id : 'conceptAmount015',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept015',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleNoPECEAcc,
							name : 'conceptAmount016',
							id : 'conceptAmount016',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept016',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleDTA,
							name : 'conceptAmount017',
							id : 'conceptAmount017',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept017',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleIVA,
							name : 'conceptAmount018',
							id : 'conceptAmount018',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept018',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleIGI,
							name : 'conceptAmount019',
							id : 'conceptAmount019',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept019',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitlePRV,
							name : 'conceptAmount020',
							id : 'conceptAmount020',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept020',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleIVAPRV,
							name : 'conceptAmount021',
							id : 'conceptAmount021',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept021',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleManeuvers + ' (No Fiscal)',
							name : 'conceptAmount022',
							id : 'conceptAmount022',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept022',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation + ' (No Fiscal)',
							name : 'conceptAmount023',
							id : 'conceptAmount023',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept023',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1 (No Fiscal)',
							name : 'conceptAmount024',
							id : 'conceptAmount024',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept024',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2 (No Fiscal)',
							name : 'conceptAmount025',
							id : 'conceptAmount025',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept025',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						}, {
							xtype : 'numericfield',
							fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3 (No Fiscal)',
							name : 'conceptAmount026',
							id : 'conceptAmount026',
							labelAlign: 'right',
							hidden: true,
							alwaysDisplayDecimals: true,
							useThousandSeparator: true,							
                            decimalPrecision: 2,
                            currencySymbol:'$'
						},{
							xtype : 'displayfield',
							id:'fileListHtmlInvoiceConcept026',
							height: 30,							
							margin: '0 0 10 140',
							hidden: true,
							fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
						},/* {
							fieldLabel : SuppAppMsg.fiscalTitle12,
							name : 'invoiceDate'
						}, {
							fieldLabel : SuppAppMsg.fiscalTitle13,
							name : 'uuidPago'
						},{
							fieldLabel : SuppAppMsg.fiscalTitle14 ,
							name : 'uuidNotaCredito'
						}, {
							fieldLabel : 'Centro de Costos',
							name : 'centroCostos'
						}, {
							fieldLabel : 'Concepto/Articulo',
							name : 'conceptoArticulo'
						}, {
							fieldLabel : 'Compañia',
							name : 'companyFD'
						}, {
							fieldLabel : SuppAppMsg.purchaseOrderNumber,
							name : 'orderNumber'
						}, {
							fieldLabel : SuppAppMsg.purchaseOrderType,
							name : 'orderType'
						}*/
					]}
			];

		this.callParent(arguments);
	}

});