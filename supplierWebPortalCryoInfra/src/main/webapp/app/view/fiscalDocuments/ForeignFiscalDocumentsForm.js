Ext.define('SupplierApp.view.fiscalDocuments.ForeignFiscalDocumentsForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.foreignFiscalDocumentsForm',
	border : false,
	frame : false,
	style: 'border: solid #ccc 1px',
	autoScroll : false,
	initComponent : function() {

		this.items = [{
			xtype : 'container',
			layout : 'hbox',
			margin : '0 0 0 0',
			style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
			defaults : {
					labelWidth : 100,
					xtype : 'textfield',
					labelAlign: 'left'
			},
			items : [{
	            xtype: 'fieldset',
	            id: 'principalFieldsFD',
	            title: SuppAppMsg.fiscalTitleMainInv,
	            defaultType: 'textfield',
	            margin:'10 0 0 10',
	            defaults: {
	                anchor: '100%'
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
							width : 460,
							readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true,
							fieldStyle:'border: 0px; background-color:#fff;font-size:16px;'
						},{
							name : 'taxId',
							fieldLabel : 'TAX ID',
							width : 200,
							readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true,
							fieldStyle:'border: 0px; background-color:#fff;'
						},{
							name : 'uuid',
							hidden:true
						},{
							name : 'address',
							fieldLabel : SuppAppMsg.purchaseTitle14,
							width : 460,
							readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true,
							fieldStyle:'border: 0px; background-color:#fff;'
						},{
							name : 'country',
							fieldLabel : SuppAppMsg.purchaseTitle15,
							width : 160,
							readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true,
							fieldStyle:'border: 0px; background-color:#fff;'
						},{
							xtype: 'datefield',
				            fieldLabel: SuppAppMsg.purchaseTitle16,
				            name:'expeditionDate',
				            id: 'expeditionDateFD',
				            itemId: 'expeditionDateFD',				            
				            width : 350,
				            labelWidth:120,
				            //margin:'15 40 0 10',
				            allowBlank:false,
				            readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true
						},{
							name : 'invoiceNumber',
							labelWidth:120,
							fieldLabel : SuppAppMsg.invoiceNumber,
							allowBlank:false,
							width : 350
						},{
							xtype : 'combo',
							fieldLabel : SuppAppMsg.paymentTitle1,
							id : 'foreignCompanyCombo',
							itemId : 'foreignCompanyCombo',
							name : 'foreignCompany',
							store : getUDCStore('COMPANYCB', '', '', ''),
							valueField : 'udcKey',
							displayField: 'strValue2',
							emptyText : SuppAppMsg.purchaseTitle19, 
			                typeAhead: true,
			                minChars: 2,
			                forceSelection: true,
			                triggerAction: 'all',
			                labelWidth:120,
			                width : 460,
							allowBlank : false,
							margin:'10 0 10 0'
						},{
							name : 'foreignDescription',
							labelWidth:120,
							fieldLabel : SuppAppMsg.purchaseTitle17,
							width : 460,
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
							id : 'foreignCurrencyComboFD',
							name : 'foreignCurrency',
							store : getUDCStore('CURRENCY', '', '', ''),
							valueField : 'udcKey',
							displayField: 'strValue1',
							emptyText : SuppAppMsg.purchaseTitle19, 
			                typeAhead: true,
			                minChars: 2,
			                forceSelection: true,
			                triggerAction: 'all',
			                width : 350,
							labelWidth:120,
							allowBlank : false,
				            readOnly: role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE' || role=='ROLE_ADMIN'?false:true
						},{
							xtype: 'numericfield',
							name : 'foreignSubtotal',
							itemId : 'foreignSubtotalFD',
							id : 'foreignSubtotalFD',
							labelWidth:120,
							fieldLabel : SuppAppMsg.purchaseTitle18,
							width : 350,
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
									
									var tax = Ext.getCmp('foreignTaxesFD').getValue();
									var retention = Ext.getCmp('foreignRetentionFD').getValue();
									var total = newValue + tax - retention;
									
									Ext.getCmp('foreignDebitFD').setValue(total);
									
								}
							}
						},{
							xtype: 'numericfield',
							name : 'foreignTaxes',
							itemId : 'foreignTaxesFD',
							id : 'foreignTaxesFD',
							labelWidth:120,
							fieldLabel : SuppAppMsg.purchaseTitle20,
							width : 350,
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
									
									var subTotal = Ext.getCmp('foreignSubtotalFD').getValue();
									var retention = Ext.getCmp('foreignRetentionFD').getValue();
									var total = newValue + subTotal - retention;
									
									Ext.getCmp('foreignDebitFD').setValue(total);
									
								}
							}
						},{
							xtype: 'numericfield',
							name : 'foreignRetention',
							itemId : 'foreignRetentionFD',
							id : 'foreignRetentionFD',
							labelWidth:120,
							fieldLabel : SuppAppMsg.purchaseTitle21,
							width : 350,
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
									
									var subTotal = Ext.getCmp('foreignSubtotalFD').getValue();
									var tax = Ext.getCmp('foreignTaxesFD').getValue();
									var total =  subTotal + tax - newValue;
									
									Ext.getCmp('foreignDebitFD').setValue(total);
									
								}
							}
						},
						{
							xtype: 'numericfield',
							name : 'foreignDebit',
							itemId : 'foreignDebitFD',
							id : 'foreignDebitFD',
							labelWidth:120,
							fieldLabel : SuppAppMsg.purchaseTitle22,
							width : 350,
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
							xtype: 'numericfield',
							name : 'advancePayment',
							itemId : 'advancePaymentForeign',
							id : 'advancePaymentForeign',				    			    									
							fieldLabel : SuppAppMsg.fiscalTitle23,
							labelWidth:120,
							width : 350,
							value: 0,
							useThousandSeparator: true,
                            decimalPrecision: 3,
                            alwaysDisplayDecimals: true,
                            allowNegative: false,
                            currencySymbol:'$',
                            hideTrigger:true,
                            thousandSeparator: ',',
                            allowBlank : false
						},{							
							xtype : 'textfield',
							name : 'receiptIdList',
							hidden:true
						},{
							xtype : 'textfield',
							name : 'attachmentFlag',
							id : 'attachmentFlagFD',
							value:'',
							hidden:true
						},{
							xtype : 'textfield',
							name : 'currentUuid',
							id : 'currentUuid',
							value:'',
							hidden:true
						},{
							name : 'foreignNotes',
							labelWidth:120,
							fieldLabel : SuppAppMsg.purchaseOrdersMsg4,
							width : 460
						},{
							xtype : 'displayfield',
							value : SuppAppMsg.purchaseTitle23,
							height:204,
							id:'fileListForeignHtmlFD',
							margin: '0 0 8 15',
							fieldStyle: 'font-size:11px;color:#blue;padding-bottom:10px;'
							
						}]
			},{
		            xtype: 'fieldset',
		            id: 'conceptsFieldsForeign',
		            title: SuppAppMsg.fiscalTitleExtraCons,
		            defaultType: 'textfield',		            
		            margin:'10 0 0 10',
		            defaults: {
		                anchor: '100%'
		            },
		            items: [{
	            		xtype: 'tabpanel',
	            		plain: true,
	            		border:false,
	            		items:[{		    			    				            		
	            			title: SuppAppMsg.fiscalTitleWithoutTaxInvoice + ' 1',
	            			items:[{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//CNT
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport1',
												itemId : 'conceptImport1FD',
												id : 'conceptImport1FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleCNT,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'5 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept1_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleCNT,				    			    									
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'5 0 0 5'	
										},{
												xtype : 'filefield',
												name : 'fileConcept1_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleCNT,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'5 0 0 5'
										}]
					            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Validación
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport2',
												itemId : 'conceptImport2FD',
												id : 'conceptImport2FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleValidation,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept2_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleValidation,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept2_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleValidation,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Maniobras
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport3',
												itemId : 'conceptImport3FD',
												id : 'conceptImport3FD',
												fieldLabel : SuppAppMsg.fiscalTitleManeuvers,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept3_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleManeuvers,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept3_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleManeuvers,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Desconsolidación
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport4',
												itemId : 'conceptImport4FD',
												id : 'conceptImport4FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept4_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'5 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept4_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'5 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Maniobras en rojo
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport5',
												itemId : 'conceptImport5FD',
												id : 'conceptImport5FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleRedMan,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept5_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleRedMan,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept5_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleRedMan,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Fumigación
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport6',
												itemId : 'conceptImport6FD',
												id : 'conceptImport6FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleFumigation,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept6_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleFumigation,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept6_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleFumigation,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Muellaje
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport7',
												itemId : 'conceptImport7FD',
												id : 'conceptImport7FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleDocking,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept7_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDocking,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept7_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDocking,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Almacenaje
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport8',
												itemId : 'conceptImport8FD',
												id : 'conceptImport8FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleStorage,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept8_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleStorage,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept8_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleStorage,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Demoras
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport9',
												itemId : 'conceptImport9FD',
												id : 'conceptImport9FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleDelays,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept9_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDelays,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept9_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDelays,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Arrastres
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport10',
												itemId : 'conceptImport10FD',
												id : 'conceptImport10FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleDragging,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept10_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDragging,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept10_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDragging,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}
										]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Permisos
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport11',
												itemId : 'conceptImport11FD',
												id : 'conceptImport11FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitlePermissions,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept11_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitlePermissions,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept11_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitlePermissions,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Derechos
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport12',
												itemId : 'conceptImport12FD',
												id : 'conceptImport12FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleDuties,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept12_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDuties,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept12_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDuties,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Otros1
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport13',
												itemId : 'conceptImport13FD',
												id : 'conceptImport13FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1',
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept13_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept13_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Otros2
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport14',
												itemId : 'conceptImport14FD',
												id : 'conceptImport14FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2',
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept14_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept14_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Otros3
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport15',
												itemId : 'conceptImport15FD',
												id : 'conceptImport15FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3',
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept15_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept15_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            }]
	            		},{
	            			title: SuppAppMsg.fiscalTitleWithoutTaxInvoice + ' 2',
	            			items:[{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Impuestos no pagados con cuenta PECE
										items : [{
		    									xtype: 'label',
		    									text : SuppAppMsg.fiscalTitleNoPECEAcc + ':',
		    									margin: '10 0 10 0',
		    									labelWidth : 500
										},{
												xtype: 'numericfield',
												name : 'conceptImport16',
												itemId : 'conceptImport16FD',
												id : 'conceptImport16FD',
												hidden: true,
												fieldLabel : SuppAppMsg.fiscalTitleNoPECEAcc,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'10 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept16_1',
												hidden: true,
												hideLabel: true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleNoPECEAcc,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'22 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept16_2',
												hidden: true,
												hideLabel: true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleNoPECEAcc,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'22 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//DTA
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport17',
												itemId : 'conceptImport17FD',
												id : 'conceptImport17FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleDTA,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'
										},{
												xtype : 'filefield',
												name : 'fileConcept17_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDTA,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept17_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDTA,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//IVA
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport18',
												itemId : 'conceptImport18FD',
												id : 'conceptImport18FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleIVA,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'
										},{
												xtype : 'filefield',
												name : 'fileConcept18_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleIVA,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept18_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleIVA,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//IGI
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport19',
												itemId : 'conceptImport19FD',
												id : 'conceptImport19FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleIGI,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept19_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleIGI,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept19_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleIGI,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//PRV
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport20',
												itemId : 'conceptImport20FD',
												id : 'conceptImport20FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitlePRV,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept20_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitlePRV,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept20_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitlePRV,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//IVA/PRV
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport21',
												itemId : 'conceptImport21FD',
												id : 'conceptImport21FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleIVAPRV,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept21_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleIVAPRV,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept21_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleIVAPRV,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//ManiobrasNoF
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport22',
												itemId : 'conceptImport22FD',
												id : 'conceptImport22FD',
												fieldLabel : SuppAppMsg.fiscalTitleManeuvers,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept22_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleManeuvers,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept22_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleManeuvers,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//DesconsolidaciónNoF
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport23',
												itemId : 'conceptImport23FD',
												id : 'conceptImport23FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation,
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept23_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept23_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation,
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Otros1NoF
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport24',
												itemId : 'conceptImport24FD',
												id : 'conceptImport24FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1',
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept24_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept24_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Otros2NoF
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport25',
												itemId : 'conceptImport25FD',
												id : 'conceptImport25FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2',
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept25_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept25_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            },{
										xtype : 'container',
										layout : 'hbox',
										margin : '0 0 0 0',
										style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
										defaults : {
												labelWidth : 100,
												xtype : 'textfield',
												labelAlign: 'left'
										},//Otros3NoF
										items : [{
												xtype: 'numericfield',
												name : 'conceptImport26',
												itemId : 'conceptImport26FD',
												id : 'conceptImport26FD',				    			    									
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3',
												labelWidth:120,
												width : 210,
												value: 0,
												useThousandSeparator: true,
				                                decimalPrecision: 2,
				                                alwaysDisplayDecimals: true,
				                                allowNegative: false,
				                                currencySymbol:'$',
				                                hideTrigger:true,
				                                thousandSeparator: ',',
				                                allowBlank : false,
				                                margin:'0 0 0 0'	
										},{
												xtype : 'filefield',
												name : 'fileConcept26_1',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										},{
												xtype : 'filefield',
												name : 'fileConcept26_2',
												hideLabel:true,
												clearOnSubmit: false,
												fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3',
												labelWidth : 120,
												width : 210,
												msgTarget : 'side',
												buttonText : SuppAppMsg.suppliersSearch,
												margin:'0 0 0 5'
										}]
				            }]
	            		}]
		            }]
			}]
		}];

		this.tbar = [{
			iconCls : 'icon-save',
			text : SuppAppMsg.purchaseTitle24,
			itemId : 'sendForeignRecordFD',
			id : 'sendForeignRecordFD',			
			action : 'sendForeignRecordFD'
		},'-',{
			iconCls:'icon-document',
			text: SuppAppMsg.purchaseTitle25,
			action : 'uploadForeignDocsFD',
			itemId: 'uploadForeignDocsFD',
			id: 'uploadForeignDocsFD'
		}];
		this.callParent(arguments);
	}

});