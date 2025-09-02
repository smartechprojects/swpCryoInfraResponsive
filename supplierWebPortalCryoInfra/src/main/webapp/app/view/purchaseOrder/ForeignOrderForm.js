Ext.define('SupplierApp.view.purchaseOrder.ForeignOrderForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.foreignOrderForm',
	border : false,
	frame : false,
	style: 'border: solid #ccc 1px',
	autoScroll : true,
	initComponent : function() {

		this.items = [{
						xtype: 'container',
						layout: 'vbox',
						defaults : {
							labelWidth : 120,
							xtype : 'textfield',
							margin: '10 0 0 10',
							align: 'stretch',
							labelWidth : 70
						},
				        items:[{
									name : 'addressNumber',
									hidden: true
								},{
									name : 'orderNumber',
									hidden: true
								},{
									name : 'orderType',
									hidden: true
								},{
									name : 'name',
									width : 680,
									readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true,
									fieldStyle:'border: 0px; background-color:#fff;font-size:16px;'
								},
								{
									name : 'taxId',
									fieldLabel : 'TAX ID',
									width : 200,
									readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true,
									fieldStyle:'border: 0px; background-color:#fff;'
								},
								{
									name : 'uuid',
									hidden:true
								},
								{
									name : 'address',
									fieldLabel : SuppAppMsg.purchaseTitle14,
									width : 680,
									readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true,
									fieldStyle:'border: 0px; background-color:#fff;'
								},
								{
									name : 'country',
									fieldLabel : SuppAppMsg.purchaseTitle15,
									width : 160,
									readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true,
									fieldStyle:'border: 0px; background-color:#fff;'
								},{
									xtype: 'datefield',
						            fieldLabel: SuppAppMsg.purchaseTitle16,
						            id: 'expeditionDate',
						            itemId: 'expeditionDate',
						            name:'expeditionDate',
						            width:270,
						            labelWidth:120,
						            margin:'15 40 0 10',
						            allowBlank:false,
						            readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true
								},
								{
									name : 'invoiceNumber',
									labelWidth:120,
									fieldLabel : SuppAppMsg.invoiceNumber,
									allowBlank:false,
									width : 350
								},
								{
									name : 'foreignDescription',
									labelWidth:120,
									fieldLabel : SuppAppMsg.purchaseTitle17,
									width : 660,
									allowBlank : false,
								    listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
									readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true
								},{
									xtype : 'combo',
									fieldLabel : SuppAppMsg.purchaseOrderCurrency,
									id : 'foreignCurrencyCombo',
									name : 'foreignCurrency',
									store : getUDCStore('CURRENCY', '', '', ''),
									valueField : 'udcKey',
									displayField: 'strValue1',
									emptyText : SuppAppMsg.purchaseTitle19, 
					                typeAhead: true,
					                minChars: 2,
					                forceSelection: true,
					                triggerAction: 'all',
									width : 300,
									labelWidth:120,
									allowBlank : false,
						            readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true
								},{
									xtype: 'numericfield',
									name : 'foreignSubtotal',
									itemId : 'foreignSubtotal',
									id : 'foreignSubtotal',
									labelWidth:120,
									fieldLabel : SuppAppMsg.purchaseTitle18,
									width : 250,
									useThousandSeparator: true,
                                    decimalPrecision: 3,
                                    alwaysDisplayDecimals: true,
                                    allowNegative: false,
                                    currencySymbol:'$',
                                    hideTrigger:true,
                                    thousandSeparator: ',',
                                    allowBlank : false,
                                    readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true,
				            		listeners:{
										change: function(field, newValue, oldValue){
											
											var tax = Ext.getCmp('foreignTaxes').getValue();
											var retention = Ext.getCmp('foreignRetention').getValue();
											var total = newValue + tax - retention;
											
											Ext.getCmp('foreignDebit').setValue(total);
											
										}
									}
								},
								{
									xtype: 'numericfield',
									name : 'foreignTaxes',
									itemId : 'foreignTaxes',
									id : 'foreignTaxes',
									labelWidth:120,
									fieldLabel : SuppAppMsg.purchaseTitle20,
									width : 250,
									useThousandSeparator: true,
                                    decimalPrecision: 3,
                                    alwaysDisplayDecimals: true,
                                    allowNegative: false,
                                    currencySymbol:'$',
                                    value:0,
                                    hideTrigger:true,
                                    thousandSeparator: ',',
                                    allowBlank:false,
						            hidden:true,
				            		listeners:{
										change: function(field, newValue, oldValue){
											
											var subTotal = Ext.getCmp('foreignSubtotal').getValue();
											var retention = Ext.getCmp('foreignRetention').getValue();
											var total = newValue + subTotal - retention;
											
											Ext.getCmp('foreignDebit').setValue(total);
											
										}
									}
								},
								{
									xtype: 'numericfield',
									name : 'foreignRetention',
									itemId : 'foreignRetention',
									id : 'foreignRetention',
									labelWidth:120,
									fieldLabel : SuppAppMsg.purchaseTitle21,
									width : 250,
									maxLength : 20,
									useThousandSeparator: true,
                                    decimalPrecision: 6,
                                    alwaysDisplayDecimals: true,
                                    allowNegative: false,
                                    currencySymbol:'$',
                                    hideTrigger:true,
                                    value:0,
                                    hidden:true,
                                    thousandSeparator: ',',
				            		listeners:{
										change: function(field, newValue, oldValue){
											
											var subTotal = Ext.getCmp('foreignSubtotal').getValue();
											var tax = Ext.getCmp('foreignTaxes').getValue();
											var total =  subTotal + tax - newValue;
											
											Ext.getCmp('foreignDebit').setValue(total);
											
										}
									}
								},
								{
									xtype: 'numericfield',
									name : 'foreignDebit',
									itemId : 'foreignDebit',
									id : 'foreignDebit',
									labelWidth:120,
									fieldLabel : SuppAppMsg.purchaseTitle22,
									width : 250,
									maxLength : 20,
									useThousandSeparator: true,
                                    decimalPrecision: 3,
                                    alwaysDisplayDecimals: true,
                                    allowNegative: false,
                                    currencySymbol:'$',
                                    hideTrigger:true,
                                    value:0,
                                    thousandSeparator: ',',
									//allowBlank : false,
									hidden: true,
									readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true
								},{
									xtype : 'textfield',
									name : 'receiptIdList',
									hidden:true
								},{
									xtype : 'textfield',
									name : 'attachmentFlag',
									id : 'attachmentFlag',
									value:'',
									hidden:true
								},
								{
									name : 'foreignNotes',
									labelWidth:120,
									fieldLabel : SuppAppMsg.purchaseOrdersMsg4,
									width : 660
								},{
									xtype : 'combo',
    									fieldLabel : SuppAppMsg.purchasePlant,
    									id : 'plantCodeComboFO',
    									//itemId : 'plantCodeComboFO',
    									name : 'plantCode',
    									store : getUDCStore('PLANTCRYO', '', '', ''),
    									valueField : 'udcKey',
    									displayField: 'strValue1',
    									emptyText : SuppAppMsg.purchaseTitle19,
    					                typeAhead: true,
    					                minChars: 2,
    					                forceSelection: true,
    					                triggerAction: 'all',
    					                labelWidth:120,
    					                width : 490,
    									allowBlank : false//,
    									//margin:'20 0 20 0',
		                                //fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
		                               // readOnly: true
								},{
									xtype : 'displayfield',
									value : SuppAppMsg.purchaseTitle23,
									height:100,
									id:'fileListForeignHtml',
									margin: '0 0 0 15',
									fieldStyle: 'font-size:11px;color:#blue;padding-bottom:10px;'
									
								}]
					}];

		this.tbar = [{
			iconCls : 'icon-save',
			itemId : 'sendForeignRecord',
			id : 'sendForeignRecord',
			text : SuppAppMsg.purchaseTitle24,
			action : 'sendForeignRecord'
		},'-',{
			iconCls:'icon-document',
			text: SuppAppMsg.purchaseTitle25,
			action : 'uploadForeignDocs',
			itemId: 'uploadForeignDocs',
			id: 'uploadForeignDocs'
		}];
		this.callParent(arguments);
	}

});