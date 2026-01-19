Ext.define('SupplierApp.controller.PurchaseOrder', {
    extend: 'Ext.app.Controller',
    stores: ['PurchaseOrder','PaymentCalendar','LogData','Receipt'],
    models: ['PurchaseOrder','PurchaseOrderDetail','PaymentCalendar','InvoiceCodesDTO','Receipt'],
    views: ['purchaseOrder.PurchaseOrderPanel','purchaseOrder.PurchaseOrderGrid','purchaseOrder.PurchaseOrderForm',
            'purchaseOrder.PurchaseOrderDetailPanel','purchaseOrder.PurchaseOrderDetailGrid','purchaseOrder.ReceiptOrderForm',
            'purchaseOrder.DeliverPurchaseOrderGrid', 'purchaseOrder.DeliverPurchaseOrderPanel',
            'purchaseOrder.ComplementoPagoPanel','purchaseOrder.SelInvGrid','purchaseOrder.AcceptInvGrid','purchaseOrder.ForeignOrderForm',
            'purchaseOrder.ChangeBuyerGrid','paymentCalendar.PaymentCalendarGrid','purchaseOrder.LogDataGrid', 'purchaseOrder.LogDataPanel',
            'receipt.ReceiptPanel','receipt.ReceiptGrid','receipt.ReceiptForm','creditNote.CreditNotePanel','creditNote.CreditNoteForm','creditNote.CreditNoteGrid'],
    refs: [{
        	ref: 'purchaseOrderForm',
        	selector: 'purchaseOrderForm'
	    },
	    {
	        ref: 'purchaseOrderGrid',
	        selector: 'purchaseOrderGrid'
	    },
	    {
	        ref: 'purchaseOrderDetailGrid',
	        selector: 'purchaseOrderDetailGrid'
	    },
	    {
	        ref: 'receiptOrderForm',
	        selector: 'receiptOrderForm'
	    },
	    {
	        ref: 'deliverPurchaseOrderGrid',
	        selector: 'deliverPurchaseOrderGrid'
	    },
	    {
	        ref: 'selInvGrid',
	        selector: 'selInvGrid'
	    },
	    {
	        ref: 'acceptInvGrid',
	        selector: 'acceptInvGrid'
	    },{
        	ref: 'foreignOrderForm',
        	selector: 'foreignOrderForm'
	    },{
        	ref: 'changeBuyerGrid',
        	selector: 'changeBuyerGrid'
	    },{
        	ref: 'paymentCalendarGrid',
        	selector: 'paymentCalendarGrid'
	    },{
        	ref: 'logDataGrid',
        	selector: 'logDataGrid'
	    },{
        	ref: 'receiptGrid',
        	selector: 'receiptGrid'
	    },{
        	ref: 'receiptForm',
        	selector: 'receiptForm'
	    },
	    {
        	ref: 'creditNoteForm',
        	selector: 'creditNoteForm'
	    },
	    {
        	ref: 'creditNoteGrid',
        	selector: 'creditNoteGrid'
	    }
	    
	    ],
 
	    
    init: function() {
		this.requestWindow = null;
		this.orderDetailWindow = null;
		this.winLoadInv = null;
		this.receiptWindow=null;
		this.receipOrderWindow=null;
		this.gblMassiveLoadEx = null;
		this.outSourcingWindow = null;
		this.winOSFiles = null;
	    var receiptOtherDocs;
	    var valReceiptOtherDocs;
	  //  var docsOtherUUID;
	    
		
        this.control({
		            'purchaseOrderGrid': {
		            	itemdblclick: this.gridSelectionChange
		            },
		            'deliverPurchaseOrderGrid': {
		            	itemdblclick: this.gridSelectionChange
		            },
					'purchaseOrderForm button[action=uploadFiscalDoc]' : {
						click : this.invLoad
					},
					'purchaseOrderForm button[action=uploadMultipleFiscalDoc]' : {
						click: this.invMultipleLoad
					},
					'purchaseOrderForm button[action=uploadFiscalDocNoPayment]' : {
						click : this.invLoadNoPayment
					},
					'#uploadPayment' : {
						"buttonclick" : this.uploadAdditional
					},
					'#approvePendingInvoice' : {
						"buttonclick" : this.approvePendingInvoice
					},
					'#rejectPendingInvoice' : {
						"buttonclick" : this.rejectPendingInvoice
					},
					'purchaseOrderGrid button[action=poSearch]' : {
						click : this.poSearch
					},
					'purchaseOrderGrid button[action=poInvAccept]' : {
						click : this.poInvAccept
					},
					'purchaseOrderGrid button[action=poInvReject]' : {
						click : this.poInvReject
					},
					'deliverPurchaseOrderGrid button[action=delPoSearch]' : {
						click : this.delPoSearch
					},
					'deliverPurchaseOrderGrid button[action=delPoConfirm]' : {
						click : this.delPoConfirm
					},
					'purchaseOrderGrid button[action=poLoadPurchases]' : {
						click : this.poLoadList
					},
					'purchaseOrderGrid button[action=poLoadFTPInv]' : {
						click : this.poLoadFTPInv
					},
					'purchaseOrderGrid button[action=poLoadPayment]' : {
						click : this.poLoadPayment
					},
					'purchaseOrderGrid button[action=poReasignPurchases]' : {
						click : this.poReasignPurchases
					},
					'purchaseOrderGrid button[action=poPaymentCalendar]' : {
						click : this.poPaymentCalendar
					},
					'purchaseOrderGrid button[action=uploadSATInvoiceZip]' : {
						click : this.uploadSATInvoiceZip
					},					
					'paymentCalendarGrid button[action=poLoadPaymentCalendar]' : {
						click : this.poLoadPaymentCalendar
					},
					'purchaseOrderGrid button[action=poLoadCompl]' : {
						click : this.poLoadCompl
					}/*,
					'purchaseOrderDetailGrid' :{
		                edit: this.afterGrupoEdit,
		                canceledit: this.onCancelGrupoEdit
		            }*/,
					'purchaseOrderDetailGrid button[action=updateOrder]' : {
						click : this.updateOrder
					},
					'purchaseOrderDetailGrid' : {
						itemdblclick: this.openReceiptForm
					},
					'receiptOrderForm button[action=insertReceipt]' : {
						click: this.insertReceipt
					},
					'selInvGrid button[action=invNbrSearch]' : {
						click: this.invNbrSearch
					},
					'acceptInvGrid button[action=loadComplFile]' : {
						click: this.loadComplFile
					}
					,
					'acceptInvGrid button[action=loadComplFileMassive]' : {
						click: this.loadComplFileMassive
					}
					,
					'#acceptSelInv' : {
						"buttonclick" : this.acceptSelInv
					},
					'#rejectSelInv' : {
						"buttonclick" : this.rejectSelInv
					},
					'foreignOrderForm button[action=acceptForeignRecord]' : {
						click: this.acceptForeignRecord
					},
					'foreignOrderForm button[action=uploadForeignDocs]' : {
						click: this.uploadForeignAdditional
					},
					'foreignOrderForm button[action=sendForeignRecord]' : {
						click: this.sendForeignRecord
					},
					'purchaseOrderForm button[action=openForeignDoc]' : {
						click: this.openForeignDoc
					},
					'purchaseOrderForm button[action=getInvoiceCodes]' : {
						click: this.getInvoiceCodes
					},
					'changeBuyerGrid button[action=fromBuyerSearch]' : {
						click: this.fromBuyerSearch
					},
					'changeBuyerGrid button[action=toBuyerExecute]' : {
						click: this.toBuyerExecute
					},
	        		'logDataGrid button[action=refreshLog]' : {
	        			click : this.refreshLog
	        		},
	        		'logDataGrid button[action=exportExcelLog]' : {
	        			click : this.exportExcelLog
	        		},
					'#showReceipts' : {
						"buttonclick" : this.showReceipts
					},
					'#showCreditNotes' : {
						"buttonclick" : this.showCreditNotes
					},
					'receiptForm button[action=uploadReceiptInvoice]' : {
						click : this.uploadReceiptInvoice
					},
					'receiptForm button[action=uploadReceiptCreditNote]' : {
						click : this.uploadReceiptCreditNote
					},
					'receiptForm button[action=uploadReceiptInvoiceZip]' : {
						click : this.uploadReceiptInvoiceZip
					},
					'receiptForm button[action=showOutSourcingWindow]' : {
						click : this.showOutSourcingWindow
					},
					'receiptForm button[action=testDACG]' : {
						click : this.testDACG
					},
					'#openPONotes' : {
						"buttonclick" : this.openPONotes
					},
					'creditNoteForm button[action=uploadReceiptInvoice]' : {
						click : this.uploadReceiptInvoice
					},
					'creditNoteForm button[action=uploadReceiptCreditNote]' : {
						click : this.uploadReceiptCreditNote
					},
					'creditNoteForm button[action=uploadReceiptInvoiceZip]' : {
						click : this.uploadReceiptInvoiceZip
					},
					'creditNoteForm button[action=showOutSourcingWindow]' : {
						click : this.showOutSourcingWindow
					},
					'creditNoteForm button[action=testDACG]' : {
						click : this.testDACG
					}
					
        });
    },    
    


    showReceipts: function(grid, record){
    	
    	var me = this;
    	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);    	
		Ext.Ajax.request({
		    url: 'supplier/getByAddressNumber.action',
		    method: 'POST',
		    params: {		    	
		    	addressNumber : record.data.addressNumber
	        },
		    success: function(fp, o) {		    	
		    	var res = Ext.decode(fp.responseText);
		    	var sup = Ext.create('SupplierApp.model.Supplier',res.data);
		    	if(role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN') {
					var isValidSupplier = false;
					for(var i=0; i < gblMassiveLoadEx.data.length; i++){
						if(gblMassiveLoadEx.data.items[i].data.udcKey == sup.data.rfc){
							isValidSupplier = true;
							break;
						}
					}
		    	}

		    	Ext.Ajax.request({
					url : 'receipt/getOrderReceipts.action',
					method : 'GET',
						params : {
							addressBook: record.data.addressNumber, 
							orderNumber: record.data.orderNumber, 
							orderType:record.data.orderType,
							orderCompany:record.data.orderCompany
						},
						success : function(response,opts) {
							var resp = Ext.decode(response.responseText);
							if(resp){
								var data = resp.data;
								me.receiptWindow = new Ext.Window({
						    		layout : 'fit',
						    		title : SuppAppMsg.purchaseReceipt,
						    		width : 1600,
						    		height : 500,
						    		//width: Ext.Element.getViewportWidth() * 0.90,   // 👈 40% de pantalla
					                //maxWidth: 1600,                                // 👈 ancho mínimo
					                //height: Ext.Element.getViewportHeight() * 0.70, // 👈 40% de alto
					                //maxHeight: 500,
						    		modal : true,
						    		closeAction : 'destroy',
						    		resizable : true,
						    		minimizable : false,
						    		maximizable : false,
						    		plain : true,
						    		items : [ {
						    			xtype : 'receiptPanel',
						    			border : true,
						    			//height : 415
						    		}  ]
						    	});
								var form = me.getReceiptForm().getForm();
						    	form.loadRecord(record);
						    	var g = me.getReceiptGrid();
						    	g.store.loadData([], false);
						    	g.getView().refresh();
						    	for(var i = 0; i < data.length; i++){
						    		var r = Ext.create('SupplierApp.model.Receipt',data[i]);
						    		g.store.insert(i, r);
						    	}
						    	me.receiptWindow.show(); 
						    	Ext.getCmp('optionType').setValue('Factura');
						    	
						    	if (role!='ROLE_PURCHASE_READ' && role!='ROLE_AUDIT_USR'){ 
						    		Ext.getCmp('uploadReceiptInvoice').setVisible(true);
						    		
							    	if(sup.data.outSourcing == true && sup.data.outSourcingAccept == true){
							    		Ext.getCmp('showOutSourcingWindow').setVisible(true);
							    	}
							    	
									if(isValidSupplier == true) {
										Ext.getCmp('uploadReceiptInvoiceZip').setVisible(true);
									}
						    	}
						    	Ext.getCmp('uploadReceiptCreditNote').setVisible(false);						    	
							}					
							
							box.hide();
						},
						failure : function() {
							box.hide();
						}
					});
		    },
	        failure: function(fp, o) {
	        	box.hide();
	        }
		});
    
    },
    
    showCreditNotes: function(grid, record){
    	var me = this;
    	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    	
		Ext.Ajax.request({
		    url: 'supplier/getByAddressNumber.action',
		    method: 'POST',
		    params: {		    	
		    	addressNumber : record.data.addressNumber
	        },
		    success: function(fp, o) {
		    	box.hide();
		    	var res = Ext.decode(fp.responseText);
		    	var sup = Ext.create('SupplierApp.model.Supplier',res.data);
		    	if(role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN') {
					var isValidSupplier = false;
					for(var i=0; i < gblMassiveLoadEx.data.length; i++){
						if(gblMassiveLoadEx.data.items[i].data.udcKey == sup.data.rfc){
							isValidSupplier = true;
							break;
						}
					}
		    	}
		    	Ext.Ajax.request({
					url : 'receipt/getCreditNotes.action',
					method : 'GET',
						params : {
							addressBook: record.data.addressNumber, 
							orderNumber: record.data.orderNumber, 
							orderType:record.data.orderType,
							orderCompany:record.data.orderCompany
						},
						success : function(response,opts) {
							var resp = Ext.decode(response.responseText);
							if(resp){
								var data = resp.data;
								me.receiptWindow = new Ext.Window({
						    		layout : 'fit',
						    		title : SuppAppMsg.purchaseReceiptCreditNoteTitle,
						    		width : 1120,
						    		height : 500,
						    		modal : true,
						    		closeAction : 'destroy',
						    		resizable : true,
						    		minimizable : false,
						    		maximizable : false,
						    		plain : true,
						    		items : [ {
						    			xtype : 'creditNotePanel',
						    			border : true,
						    			height : 415
						    		}  ]
						    	});
								var form = me.getCreditNoteForm().getForm();
						    	form.loadRecord(record);
						    	var g = me.getCreditNoteGrid();
						    	g.store.loadData([], false);
						    	g.getView().refresh();
						    	for(var i = 0; i < data.length; i++){
						    		var r = Ext.create('SupplierApp.model.PurchaseOrderDetail',data[i]);
						    		g.store.insert(i, r);
						    	}
						    	me.receiptWindow.show();
						    	Ext.getCmp('optionType').setValue('NotaCredito');

						    	if (role!='ROLE_PURCHASE_READ' && role!='ROLE_AUDIT_USR'){
						    		Ext.getCmp('uploadReceiptCreditNote').setVisible(true);
							    	if(isValidSupplier == true){
							    		Ext.getCmp('uploadReceiptInvoiceZip').setVisible(true);
							    	}
						    	}
						    	Ext.getCmp('uploadReceiptInvoice').setVisible(false);
							}
							
							box.hide();
						},
						failure : function() {
							box.hide();
						}
					});
		    },  
	        failure: function(fp, o) {
	        	box.hide();
	        }
		});
    },
    

    uploadReceiptInvoice : function(grid, rowIndex, colIndex, record) {
    	
    	var orderForm = this.getReceiptForm().getForm();

    	this.receiptOtherDocs = orderForm;
        var me = this;
        //var poDetails = orderForm.record.data;
        var po = orderForm._record.data;
        var poDetails = po.purchaseOrderDetail;
        var objAccount;
        var objAccountText;

    
    for (let valor of poDetails) {
    	if(valor.currency == po.currecyCode && valor.orderType == po.orderType && valor.orderCompany == po.companyKey ){
    		objAccount  = valor.objectAccount.trim();
            break;
        }
    }
    
    
   // objAccount = 2905;
    var applyPlant = true;
    if( (objAccount >= 2900 && objAccount <= 2999) || objAccount == 2802 ){
    	applyPlant = false;
    	
    	objAccountText = (objAccount >= 2900 && objAccount <= 2999) ? 'SP2900' : 'SP2802';
    	
    }
    

    	
    	var values = orderForm.getFieldValues();
    	this.valReceiptOtherDocs = values;
    	var grid = this.getReceiptGrid();
    	var store = grid.getStore();
    	var s = grid.getSelectionModel().getSelection();    	
    	var selected = [];
    	var receiptSubtotal = 0;
    	    	
    	Ext.each(s, function (item) {    		
    		selected.push(item.data.documentNumber);//JSC:Se sustituyen Ids por Numeros de Recibo para Facturas Nacionales y Foráneas
    		receiptSubtotal = receiptSubtotal + item.data.foreignAmountReceived;
    	});    	
    	
    	if(selected.length > 0){
    		
    		var idSelected = selected.toString();
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    		Ext.Ajax.request({
    		    url: 'supplier/getByAddressNumber.action',
    		    method: 'POST',
    		    params: {
    		    	addressNumber : orderForm.findField('addressNumber').getValue()
    	        },
    		    success: function(fp, o) {
    		    	box.hide();
    		    	var res = Ext.decode(fp.responseText);
    		    	var sup = Ext.create('SupplierApp.model.Supplier',res.data);
    	    		if(sup.data.country == "MX"){
    	    			
    			    	var filePanel = Ext.create(
    			    					'Ext.form.Panel',
    			    					{
    			    						//width : 900,
    			    						scrollable: true,
    			    						items : [
	    			    							{
				    									xtype : 'textfield',
				    									name : 'orderCompany',
				    									hidden : true,
				    									value : orderForm.findField('orderCompany').getValue()
				    								},{
    			    									xtype : 'textfield',
    			    									name : 'documentNumber',
    			    									hidden : true,
    			    									value : orderForm.findField('orderNumber').getValue()
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'documentType',
    			    									hidden : true,
    			    									value : orderForm.findField('orderType').getValue()
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'addressBook',
    			    									hidden : true,
    			    									value : orderForm.findField('addressNumber').getValue()
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'receiptIdList',
    			    									hidden : true,
    			    									value : idSelected
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'tipoComprobante',
    			    									hidden : true,
    			    									value : 'Factura'
    			    								},{
    			    									xtype : 'filefield',
    			    									name : 'file',
    			    									id : 'file',
    			    									fieldLabel : SuppAppMsg.purchaseFileXML + '*:',
    			    									labelWidth : 120,
    			    									msgTarget : 'side',
    			    									allowBlank : false,
    			    									margin:'20 0 20 0',
    			    									anchor : '90%',
    			    									multiple: true,
    			    									buttonText : SuppAppMsg.suppliersSearch,
    			    									buttonConfig: {
    			    										cls: 'buttonStyle'
    			    								    },
    			    									listeners: {  
    			    					                      change: function (value) {  
    			    					                    	  
    			    					                           var file =   Ext.getCmp('file').getEl().down('input[type=file]').dom.files[0]
    			    						                       if (file.size > 10485760) {
    			    					                    	   Ext.getCmp('file').setRawValue("");
    			    					                    	   Ext.MessageBox.alert({ 
    			    					                    		   //maxWidth: 700, minWidth: 650, 
    			    					                    		   width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
		    							    				            maxWidth: 500,                                // 👈 ancho mínimo
		    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
		    							    				            maxHeight: 100,
    			    					                    		   title: SuppAppMsg.supplierMsgValidationLoad, msg:   'El archivo supera los 10 MB'});
    			    					                    		                    	   
    			    					                       }
    			    					                      
    			    					                      }  
    			    					                    }, 
    			    										
    			    								},
    			    								{
    			    									xtype : 'filefield',
    			    									name : 'fileTwo',
    			    									id : 'fileTwo',
    			    									fieldLabel : SuppAppMsg.purchaseInvoice + '(.pdf)*:',
    			    									labelWidth : 120,
    			    									msgTarget : 'side',
    			    									allowBlank : false,
    			    									anchor : '90%',
    			    									buttonText : SuppAppMsg.suppliersSearch,
    			    									buttonConfig: {
    			    										cls: 'buttonStyle'
    			    								    },
    			    									margin:'10 0 0 0',
    			    									listeners: {  
  			    					                      change: function (value) {  
  			    					                           var file =   Ext.getCmp('fileTwo').getEl().down('input[type=file]').dom.files[0]
  			    						                       if (file.size > 10485760) {
  			    					                    	   Ext.getCmp('fileTwo').setRawValue("");
  			    					                    	   Ext.MessageBox.alert({ 
  			    					                    		  // maxWidth: 700, minWidth: 650, 
  			    					                    		    width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
	    							    				            maxWidth: 500,                                // 👈 ancho mínimo
	    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
	    							    				            maxHeight: 100,
  			    					                    		   title: SuppAppMsg.supplierMsgValidationLoad, msg:   'El archivo supera los 10 MB'});
  			    					                    		                    	   
  			    					                       }
  			    					                      
  			    					                      }  
  			    					                    }, 
    			    								},
    			    								{
    			    									xtype : 'filefield',
    			    									name : 'fileThree',
    			    									id : 'fileThree',
    			    									fieldLabel : SuppAppMsg.purchaseEvidence + '(.pdf)*:',
    			    									labelWidth : 120,
    			    									msgTarget : 'side',
    			    									allowBlank : false,
    			    									anchor : '90%',
    			    									buttonText : SuppAppMsg.suppliersSearch,
    			    									buttonConfig: {
    			    										cls: 'buttonStyle'
    			    								    },
    			    									margin:'20 0 20 0',
    			    									listeners: {  
    			    					                      change: function (value) {  
    			    					                           var file =   Ext.getCmp('fileThree').getEl().down('input[type=file]').dom.files[0]
    			    						                       if (file.size > 10485760) {
    			    					                    	   Ext.getCmp('fileThree').setRawValue("");
    			    					                    	   Ext.MessageBox.alert({ 
    			    					                    		   //maxWidth: 700, minWidth: 650, 
    			    					                    		   width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
		    							    				            maxWidth: 500,                                // 👈 ancho mínimo
		    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
		    							    				            maxHeight: 100,
    			    					                    		   title: SuppAppMsg.supplierMsgValidationLoad, msg:   'El archivo supera los 10 MB'});
    			    					                    		                    	   
    			    					                       }
    			    					                      
    			    					                      }  
    			    					                    }, 
    			    								},{
    			    									xtype : 'combo',
    			    								//	hideLabel:true,
    			    									fieldLabel : SuppAppMsg.purchasePlant,
    			    									id : 'plantCodeComboPO',
    			    								//	itemId : 'plantCodeComboPO',
    			    									name : 'plantCodePO',
    			    									store : getUDCStore('PLANTCRYO', '', '', ''),
    			    									valueField : 'udcKey',
    			    									displayField: 'strValue1',
    			    									emptyText : applyPlant ? SuppAppMsg.purchaseTitle19 : objAccountText,
    			    					                typeAhead: true,
    			    					                minChars: 1,
    			    					                forceSelection: applyPlant ? true : false,
    			    					                triggerAction: 'all',
    			    					                labelWidth:120,
    			    					                //width : 490,
    			    					                anchor : '90%',
    			    									allowBlank : applyPlant ? false : true,
    			    									margin:'20 0 20 0',
    			    									hidden: applyPlant  ? false : true
    					                                //fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    					                               // readOnly: true
			    								},{
			    									xtype : 'textfield',
			    									name : 'docsOtherUUID',
			    									id :'docsOtherUUID',
			    									hidden : true,
			    									value : '',
			    									scrollable : true
			    								},
    			    								
    			    								
    			    								/*{
    			    									xtype : 'displayfield',
    			    									value : SuppAppMsg.fiscalTitleOther,
    			    									height:204,
    			    									id:'listOthersHtmlReceipt',
    			    									margin: '0 0 8 15',
    			    									fieldStyle: 'font-size:11px;color:#blue;padding-bottom:10px;'
    			    									
    			    								},*/{
    			    									xtype : 'displayfield',
    			    									value : SuppAppMsg.purchaseTitle23,
    			    									height:204,
    			    									id:'fileListOthersHtmlReceipt',
    			    									margin: '0 0 8 15',
    			    									fieldStyle: 'font-size:11px;color:#blue;padding-bottom:10px;',
    			    									scrollable : true,
    			    								},
    			    								{
    			    									xtype : 'textfield',
    			    									name : 'attachmentFlag',
    			    									id : 'attachmentFlagReceipt',
    			    									value:'',
    			    									hidden:true,
    			    									scrollable : true
    			    								},/*,
    			    								{
    													xtype: 'button',
    													iconCls : 'icon-document',
    													id:"fileOther",
    													width:110,
    													hidden:false,
    													text: SuppAppMsg.fiscalTitleOther,
    													action:'poSearch',*/
    													/*handler: function() {
    													
    														this.fireEvent('buttonclick', grid, record);
    			
    														//win.show();
    													}*/
    												//S}
    			    								/*,{
    			    									iconCls:'icon-document',
    			    									text: SuppAppMsg.purchaseTitle25,
    			    									action : 'uploadFilesOthers',
    			    									itemId: 'uploadFilesOthers',
    			    									id: 'uploadForeignDocsCB'
    			    								}*/],
    			
    			    						buttons : [ {
    			    							text : SuppAppMsg.supplierLoad,
    			    							margin:'10 0 0 0',
    			    							cls: 'buttonStyle',
    			    							handler : function() {
    			    								
    			    								/*var plantCode = Ext.getCmp('plantCodeComboPO').getValue();
    			    								if(plantCode == null || plantCode == '')
    			    									Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError11});	*/
    			    										
    			    								var form = this.up('form').getForm();
    			    								if (form.isValid()) {
    			    									
    			    									//var formValues = form.getFieldValues();
    			    									
    			    									form.submit({
    			    												url : 'uploadInvoiceFromReceipt.action',
    			    												waitMsg : SuppAppMsg.supplierLoadFile,
    			    												success : function(fp, o) {
    			    													var res = Ext.decode(o.response.responseText);
    			    													me.winLoadInv.destroy();
    			    													if(me.receiptWindow){
    			    														me.receiptWindow.close();
    			    													}
    			    													Ext.MessageBox.alert({ 
    			    														//maxWidth: 700, minWidth: 650, 
    			    														width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
			    							    				            maxHeight: 100,
    			    														title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
    			    													
    			    												},       // If you don't pass success:true, it will always go here
    			    										        failure: function(fp, o) {
    			    										        	var res = o.response.responseText;
    			    										        	var result = Ext.decode(res);
    			    										        	var msgResp = result.message
    			    										        	
    			    										        	if(msgResp == "Error_1"){
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError1});
    			    										        	}else if(msgResp == "Error_2"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError2});
    			    										        	}else if(msgResp == "Error_3"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError3});
    			    										        	}else if(msgResp == "Error_4"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError4});
    			    										        	}else if(msgResp == "Error_5"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError5});
    			    										        	}else if(msgResp == "Error_6"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError6});
    			    										        	}else if(msgResp == "Error_7"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError7});
    			    										        	}else if(msgResp == "Error_8"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO1});
    													        	    }else if(msgResp == "Error_9"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO2});
    			    										        	}else if(msgResp == "Error_10"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO3});
    			    										        	}else if(msgResp == "Error_11"){
    													        		Ext.MessageBox.alert({ 
    													        			//maxWidth: 700, minWidth: 650, 
    													        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
			    							    				            maxHeight: 100,
    													        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError6});
    													        	    }else if(msgResp == "Error_12"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO4});
    			    										        	}else if(msgResp == "Error_13"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO5});
    			    										        	}else if(msgResp == "Error_14"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO6});
    			    										        	}else if(msgResp == "Error_15"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO7});
    			    										        	}else if(msgResp == "Error_16"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO8});
    													        	    }else if(msgResp == "Error_17"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO9});
    			    										        	}else if(msgResp == "Error_18"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP10});
    			    										        	}else if(msgResp == "Error_19"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP11});
    													        	    }else if(msgResp.startsWith("Error_20_", 0)){
    			    										        		var dataResp = msgResp.substring(9,msgResp.length);
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP12 + ' ' + dataResp});
    			    										        	}else if(msgResp.startsWith("Error_21_", 0)){
    			    										        		var dataResp = msgResp.substring(9,msgResp.length);
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP13_a + ' ' + dataResp + ' ' + SuppAppMsg.msgErrorP13_b});
    			    										        	}else if(msgResp.startsWith("Error_22_", 0)){
    			    										        		var data_resp = msgResp.substring(9,msgResp.length);
    			    										        		var r1 = data_resp.split("_:_");
    			    										        		var invCurrency = r1[0];
    			    										        		var oCurr = r1[1];
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP13_a + ' ' + invCurrency + ' ' + SuppAppMsg.msgErrorP14 + ' ' + oCurr});
    			    										        	}else if(msgResp.startsWith("Error_23_", 0)){
    			    										        		var dataResp = msgResp.substring(9,msgResp.length);
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP15 + ' ' + dataResp});
    			    										        	}else if(msgResp.startsWith("Error_24_", 0)){
    			    										        		var dataResp = msgResp.substring(9,msgResp.length);
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP16_a + ' ' + dataResp + SuppAppMsg.msgErrorP16_b});
    			    										        	}else if(msgResp.startsWith("Error_25_", 0)){
    			    										        		var dataResp = msgResp.substring(9,msgResp.length);
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP17 + ' ' + dataResp + SuppAppMsg.msgErrorP16_b});
    			    										        	}else if(msgResp.startsWith("Error_26_", 0)){
    			    										        		var dataResp = msgResp.substring(9,msgResp.length);
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP18 + ' ' + dataResp});
    			    										        	}else if(msgResp.startsWith("Error_27_", 0)){
    			    										        		var dataResp = msgResp.substring(9,msgResp.length);
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP19_a + ' ' + dataResp + ' ' + SuppAppMsg.msgErrorP19_b});
    			    										        	}else if(msgResp.startsWith("Error_28_", 0)){
    			    										        		var data_resp = msgResp.substring(9,msgResp.length);
    			    										        		var r1 = data_resp.split("_:_");
    			    										        		var receiptTaxCode = r1[0];
    			    										        		var tasasRequeridas = r1[1];
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP20_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP20_b + ' ' + tasasRequeridas});
    			    										        	}else if(msgResp.startsWith("Error_29_", 0)){
    			    										        		var data_resp = msgResp.substring(9,msgResp.length);
    			    										        		var r1 = data_resp.split("_:_");
    			    										        		var receiptTaxCode = r1[0];
    			    										        		var tasasRequeridas = r1[1];
    			    										        		var transTaxValue = r1[2];
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP21_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP21_b + ' ' + tasasRequeridas+ ' ' + SuppAppMsg.msgErrorP21_c + ' ' + transTaxValue});
    			    										        	}else if(msgResp.startsWith("Error_30_", 0)){
    			    										        		var data_resp = msgResp.substring(9,msgResp.length);
    			    										        		var r1 = data_resp.split("_:_");
    			    										        		var receiptTaxCode = r1[0];
    			    										        		var tasasRequeridas = r1[1];
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP22_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP22_b + ' ' + tasasRequeridas});
    			    										        	}else if(msgResp.startsWith("Error_31_", 0)){
    			    										        		var data_resp = msgResp.substring(9,msgResp.length);
    			    										        		var r1 = data_resp.split("_:_");
    			    										        		var receiptTaxCode = r1[0];
    			    										        		var tasasRequeridas = r1[1];
    			    										        		var transTaxValue = r1[2];
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP23_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP22_b + ' ' + tasasRequeridas+ ' ' + SuppAppMsg.msgErrorP21_c + ' ' + transTaxValue});
    			    										        	}else if(msgResp.startsWith("Error_32_", 0)){
    			    										        		var dataResp = msgResp.substring(9,msgResp.length);
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP24 + ' ' + dataResp});
    			    										        	}else if(msgResp.startsWith("Error_33_", 0)){
    			    										        		var dataResp = msgResp.substring(9,msgResp.length);
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP25 + ' ' + dataResp});
    			    										        	}else if(msgResp.startsWith("Error_34_", 0)){
    			    										        		var data_resp = msgResp.substring(9,msgResp.length);
    			    										        		var r1 = data_resp.split("_:_");
    			    										        		var receiptTaxCode = r1[0];
    			    										        		var tasasRequeridasT = r1[1];
    			    										        		var tasasRequeridasR = r1[2];
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP26_a + ' ' + tasasRequeridasT + ' ' + SuppAppMsg.msgErrorP26_b + ' ' + tasasRequeridasR});
    			    										        	}else if(msgResp == "Error_35"){
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650,
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP27});
    			    										        	}else{
    			    										        		Ext.MessageBox.alert({ 
    			    										        			//maxWidth: 700, minWidth: 650, 
    			    										        			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    			    							    				            maxWidth: 500,                                // 👈 ancho mínimo
    			    							    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    			    							    				            maxHeight: 100,
    			    										        			title: SuppAppMsg.supplierMsgValidationLoad, msg:  msgResp});
    			    										        	}
    			    										        	
    			    										        	
    			    										        }
    			    											});
    			    								}

    			    							}
    			    						} ]
    			    					});
    			    	
    	   			    	me.winLoadInv = new Ext.Window({
    			    		layout : 'fit',
    			    		title : SuppAppMsg.purchaseUploadInvoice,
    			    		width : 500,
    			    		height : 500,
    			    		//width: Ext.Element.getViewportWidth() * 0.40,   // 👈 40% de pantalla
    		                //maxWidth: 500,                                // 👈 ancho mínimo
    		                //height: Ext.Element.getViewportHeight() * 0.5, // 👈 40% de alto
    		                //maxHeight: 500,
    			    		modal : true,
    			    		scrollable: true,
    			    		closeAction : 'destroy',
    			    		resizable : true,
    			    		minimizable : false,
    			    		maximizable : false,
    			    		plain : true,
    			    		items : [ filePanel ],   			    		
    	   			    	tbar : [{
    	   						iconCls:'icon-document',
    	   						text: SuppAppMsg.fiscalTitleOther,
    	   						//action : 'testDACG',
    	   						itemId: 'uploadOthersDocsPO',
    	   						id: 'uploadOthersDocsPO',
    	   						cls: 'buttonStyle',
    	   						handler : function() {
    	   							
    	   						
    	   					        var form = me.receiptOtherDocs;
    	   					
    	   							//if (form.isValid()) {
    	   								
    	   							
    	   					    	//	var val = form.getFieldValues();
    	   								var val = me.valReceiptOtherDocs;
    	   					    		var filePanelOther = Ext.create(
    	   										'Ext.form.Panel',
    	   										{
    	   											//width : 900,
    	   											items : [
    	   													/*{
    	   														xtype : 'textfield',
    	   														name : 'documentNumber',
    	   														hidden : true,
    	   														value : val.orderNumber
    	   													},{
    	   														xtype : 'textfield',
    	   														name : 'documentType',
    	   														hidden : true,
    	   														value : val.orderType
    	   													},*/{
    	   														xtype : 'textfield',
    	   														name : 'addressBook',
    	   														hidden : true,
    	   														value : val.addressNumber
    	   													},{
    	   														xtype : 'textfield',
    	   														name : 'company',
    	   														hidden : true,
    	   														value : val.orderCompany
    	   													},{
    	   														xtype : 'textfield',
    	   														name : 'currentUuid',
    	   														hidden : true,
    	   														value : val.currentUuid
    	   													},{
    	   														xtype : 'textfield',
    	   														name : 'invoiceNumber',
    	   														hidden : true,
    	   														value : val.orderNumber
    	   													},{
    	   														xtype : 'textfield',
    	   														name : 'orderType',
    	   														hidden : true,
    	   														value : val.orderType
    	   													},{
    	   														xtype : 'filefield',
    	   														name : 'file',
    	   														id : 'fileOther',
    	   														fieldLabel : SuppAppMsg.purchaseFile + ':',
    	   														labelWidth : 80,
    	   														msgTarget : 'side',
    	   														allowBlank : false,
    	   														margin:'20 0 30 0',
    	   														anchor : '90%',
    	   														buttonText : SuppAppMsg.suppliersSearch,
    	   														buttonConfig: {
    	   				    										cls: 'buttonStyle'
    	   				    								    },
    	   														listeners: {  
    	   									                      change: function (value) {  
    	   									                           var file =   Ext.getCmp('fileOther').getEl().down('input[type=file]').dom.files[0]
    	   										                       if (file.size > 10485760) {
    	   									                    	   Ext.getCmp('fileOther').setRawValue("");
    	   									                    	   Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:   'El archivo supera los 10 MB'});
    	   									                    		                    	   
    	   									                       }
    	   									                      
    	   									                      }  
    	   									                    }, 
    	   													} ],

    	   											buttons : [ {
    	   												text : SuppAppMsg.supplierLoad,
    	   												margin:'10 0 0 0',
    	   												cls: 'buttonStyle',
    	   												handler : function() {
    	   													
    	   													var form = this.up('form').getForm();
    	   													if (form.isValid()) {
    	   														form.submit({
    	   																	url : 'uploadOthersDocsReceipt.action',
    	   																	waitMsg : SuppAppMsg.supplierLoadFile,
    	   																	success : function(fp, o) {
    	   																		
    	   																		var res = Ext.decode(o.response.responseText);
    	   																	//	Ext.getCmp('currentUuidCB').setValue(res.uuid);
    	   															     		val.currentUuid = res.uuid;  
    	   																		var docsOtherUUIDVal = Ext.getCmp('docsOtherUUID').getValue();
    	   																		
    	   																		docsOtherUUIDVal = docsOtherUUIDVal +res.uuid + ',';
    	   																		Ext.getCmp('docsOtherUUID').setValue(docsOtherUUIDVal);
    	   																		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
    	   																		Ext.Ajax.request({														
    	   																			url : 'documents/listDocumentsByFiscalRef.action',
    	   																			method : 'GET',
    	   																				params : {
    	   																					start : 0,
    	   																					limit : 20,
    	   																					addresNumber : val.addressNumber,																
    	   																					uuid: val.currentUuid																
    	   																				},
    	   																				success : function(response,opts) {
    	   																					
    	   																					response = Ext.decode(response.responseText);
    	   																					var index = 0;
    	   																					var files = "";
    	   																					var accepted ="ACEPTADO";
    	   																					for (index = 0; index < response.data.length; index++) {
    	   																							var href = "documents/openDocument.action?id=" + response.data[index].id;
    	   																							var fileHref = "<a href= '" + href + "' target='_blank'>" +  response.data[index].name + "</a>";
    	   																                            files = files + "> " + fileHref + " - " + response.data[index].size + " bytes : " + response.data[index].fiscalType + " - " + accepted +  "<br />";
    	   																					}
    	   																					Ext.getCmp('fileListOthersHtmlReceipt').setValue(files);
    	   																					Ext.getCmp('attachmentFlagReceipt').setValue("ATTACH");																																
    	   																				},
    	   																				failure : function() {
    	   																				}
    	   																			});
    	   																		
    	   																    	var win = Ext.WindowManager.getActive();
    	   																    	if (win) {
    	   																    	    win.close();
    	   																    	}
    	   																		me.winLoadInvFile.destroy();
    	   																		if(me.receiptWindow){
    	   																			me.receiptWindow.close();
    	   																		}
    	   																	},   
    	   															        failure: function(fp, o) {
    	   															        	var res = o.response.responseText;
    	   															        	var res = Ext.decode(o.response.responseText);
    	   															        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
    	   															        }
    	   																});
    	   													}
    	   												}
    	   											} ]
    	   										});
    	   					    		
    	   					    		me.winLoadInvFile = new Ext.Window({
    	   									layout : 'fit',
    	   									title : SuppAppMsg.purchaseUploadDocumentsAditional,
    	   									//width : 600,
    	   									//height : 150,
    	   									width: Ext.Element.getViewportWidth() * 0.45,   // 👈 40% de pantalla
    	   					                maxWidth: 680,                                // 👈 ancho mínimo
    	   					                height: Ext.Element.getViewportHeight() * 0.30, // 👈 40% de alto
    	   					                maxHeight: 150,
    	   									modal : true,
    	   									closeAction : 'destroy',
    	   									resizable : false,
    	   									minimizable : false,
    	   									maximizable : false,
    	   									plain : true,
    	   									items : [ filePanelOther ]
    	   							
    	   								});
    	   								me.winLoadInvFile.show();
    	   							//}
    	   						}
    	   					}],
    	   					/*items : [{
    			    	        xtype: 'form',
    			    	        layout: 'anchor',
    			    	        bodyPadding: 10,
    			    	        autoScroll: true,
    			    	        defaults: {
    			    	            anchor: '100%',   // <-- hace que todos los campos se adapten
    			    	            labelWidth: 120,
    			    	            margin: '10 0 10 0'
    			    	        },
    			    	        items: filePanel.items, // tus items existentes
    			    	        buttons: filePanel.buttons
    			    	    }],*/
    			
    			    	});
    	   			    	
    	   					
    			    	me.winLoadInv.show();
    		
    			    }else if(sup.data.country != "" && sup.data.country != null){
			    		
    			    	me.winLoadInv = new Ext.Window({
				    		layout : 'fit',
				    		title : SuppAppMsg.purchaseUploadInvoiceForeing,
				    		width : 740,
				    		height : 680,
				    		//width: Ext.Element.getViewportWidth() * 0.5,   // 👈 40% de pantalla
			                //maxWidth: 740,                                // 👈 ancho mínimo
			                //height: Ext.Element.getViewportHeight() * 0.5, // 👈 40% de alto
			                //maxHeight: 680,
				    		modal : true,
				    		closeAction : 'destroy',
				    		resizable : true,
				    		minimizable : false,
				    		maximizable : false,
				    		plain : true,
				    		items : [ 
				    				{
				    				xtype:'foreignOrderForm'
				    				} 
				    			]
				    	});
			    		
			        	var foreignForm = me.getForeignOrderForm().getForm();
			        	foreignForm.setValues({
			        		addressNumber: orderForm.findField('addressNumber').getValue(),
			        		orderNumber: values.orderNumber,
			        		orderType: values.orderType,
			        		name:  sup.data.razonSocial,
			        		taxId:  sup.data.taxId,
			        		address: sup.data.calleNumero + ", " + sup.data.colonia + ", " + sup.data.delegacionMncipio + ", C.P. " + sup.data.codigoPostal,
			        		country:  sup.data.country,
			        		receiptIdList: idSelected,
			        		foreignSubtotal:0,
			        		//foreignSubtotal:receiptSubtotal,
			        		attachmentFlag:''
			        	});

			        	setTimeout(function(){
		
			           },2000); //delay is in milliseconds 
		
		        		me.winLoadInv.show();
    			    	}else{
    			    		Ext.MessageBox.alert({ 
    			    			//maxWidth: 700, minWidth: 650,
    			    			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
    				            maxWidth: 500,                                // 👈 ancho mínimo
    				            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
    				            maxHeight: 100,
    			    			title: 'Error', msg:  SuppAppMsg.purchaseErrorNonSupplier});
    			    	}
    		    },  
		        failure: function(fp, o) {
		        	box.hide();
		        }
    		});

    	}else{
    		Ext.MessageBox.alert({ 
    			//maxWidth: 700, minWidth: 650, 
    			width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
	            maxWidth: 500,                                // 👈 ancho mínimo
	            height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
	            maxHeight: 100,
    			title: 'Error', msg: SuppAppMsg.purchaseUploadBillErrorTitle  });
    		return false;
    	}
 	
    },

    uploadReceiptCreditNote : function(grid, rowIndex, colIndex, record) {
    	
    	var orderForm = this.getCreditNoteForm().getForm();
        var me = this;
    	var values = orderForm.getFieldValues();
    	
    	var grid = this.getCreditNoteGrid();
    	var store = grid.getStore();
    	var s = grid.getSelectionModel().getSelection();
    	var selected = [];
    	var receiptSubtotal = 0;
    	Ext.each(s, function (item) {
    		selected.push(item.data.id);
    		receiptSubtotal = receiptSubtotal + item.data.amountReceived;
    	});
    	
    	var addressNumber = orderForm.findField('addressNumber').getValue();
    	if(selected.length > 0){
    		
    		var idSelected = selected.toString();
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    		Ext.Ajax.request({
    		    url: 'supplier/getByAddressNumber.action',
    		    method: 'POST',
    		    params: {
    		    	addressNumber : addressNumber
    	        },
    		    success: function(fp, o) {
    		    	box.hide();
    		    	var res = Ext.decode(fp.responseText);
    		    	var sup = Ext.create('SupplierApp.model.Supplier',res.data);
    	    		if(sup.data.country == "MX"){
    			    	var filePanel = Ext.create(
    			    					'Ext.form.Panel',
    			    					{
    			    						width : 900,
    			    						items : [
    			    								{
    			    									xtype : 'textfield',
    			    									name : 'documentNumber',
    			    									hidden : true,
    			    									value : orderForm.findField('orderNumber').getValue()
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'documentType',
    			    									hidden : true,
    			    									value : orderForm.findField('orderType').getValue()
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'addressBook',
    			    									hidden : true,
    			    									value : orderForm.findField('addressNumber').getValue()
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'receiptIdList',
    			    									hidden : true,
    			    									value : idSelected
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'tipoComprobante',
    			    									hidden : true,
    			    									value : 'NotaCredito'
    			    								},{
    			    									xtype : 'filefield',
    			    									name : 'file',
    			    									fieldLabel : SuppAppMsg.purchaseFileXML + '*:',
    			    									labelWidth : 120,
    			    									msgTarget : 'side',
    			    									allowBlank : false,
    			    									margin:'20 0 20 0',
    			    									anchor : '90%',
    			    									multiple: true,
    			    									buttonText : SuppAppMsg.suppliersSearch
    			    								},
    			    								{
    			    									xtype : 'filefield',
    			    									name : 'fileTwo',
    			    									fieldLabel : SuppAppMsg.purchaseFilePDF + '*:',
    			    									labelWidth : 120,
    			    									msgTarget : 'side',
    			    									allowBlank : false,
    			    									anchor : '90%',
    			    									buttonText : SuppAppMsg.suppliersSearch,
    			    									margin:'10 0 0 0'
    			    								}],
    			
    			    						buttons : [ {
    			    							text : SuppAppMsg.supplierLoad,
    			    							margin:'10 0 0 0',
    			    							handler : function() {
    			    								var form = this.up('form').getForm();
    			    								if (form.isValid()) {
    			    									form.submit({
    			    												url : 'uploadCreditNoteFromReceipt.action',
    			    												waitMsg : SuppAppMsg.supplierLoadFile,
    			    												success : function(fp, o) {
    			    													
    			    													var res = Ext.decode(o.response.responseText);
    			    													me.winLoadInv.destroy();
    			    													if(me.receiptWindow){
    			    														me.receiptWindow.close();
    			    													}
    			    													Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
    			    												},       // If you don't pass success:true, it will always go here
    			    										        failure: function(fp, o) {
    			    										        	
    			    										        	var res = o.response.responseText;
    			    										        	var result = Ext.decode(res);
    			    										        	
    			    										        	var msgResp = SuppAppMsg[result.message]!=undefined && SuppAppMsg[result.message]!=null?SuppAppMsg[result.message]:result.message;
    			    										        	
    			    										        	msg=result.message[window.navigator.language.startsWith("es", 0)?"es":"en"];
    			    										        	if(msg!=undefined){
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  msg});
    			    										        		return
    			    										        	}
    			    										        	
    			    										        	
    			    										        	if(msgResp == "Error_1"){
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError1});
    			    										        	}else if(msgResp == "Error_2"){
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError2});
    			    										        	}else if(msgResp == "Error_8"){
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError8});
    			    										        	}else if(msgResp == "Error_4"){
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError4});
    			    										        	}else if(msgResp == "Error_5"){
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError5});
    			    										        	}else if(msgResp == "Error_6"){
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError6});
    			    										        	}else if(msgResp == "Error_7"){
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError7});
    			    										        	}else{
    			    										        		
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  msgResp});
    			    										        	}
    			    										        }
    			    											});
    			    								}
    			    							}
    			    						} ]
    			    					});
    			
    			    	me.winLoadInv = new Ext.Window({
    			    		layout : 'fit',
    			    		title : SuppAppMsg.purchaseUploadCreditNotesTitle,
    			    		width : 600,
    			    		height : 200,
    			    		modal : true,
    			    		closeAction : 'destroy',
    			    		resizable : false,
    			    		minimizable : false,
    			    		maximizable : false,
    			    		plain : true,
    			    		items : [ filePanel ]
    			
    			    	});
    			    	me.winLoadInv.show();
    			    }else if(sup.data.country !== ""){
    			    	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg:  SuppAppMsg.purchaseErrorSupplierCountryNotMetohDef+": "+sup.data.country});
    			    	
    			    }else if(sup.data.country == "" || sup.data.country == null){
			    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg:  SuppAppMsg.purchaseErrorNonSupplier});
			    	}
    		    },  
		        failure: function(fp, o) {
		        	box.hide();
		        }
    		});

    	}else{
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg:  SuppAppMsg.purchaseReceiptNotSelected});
    		return false;
    	}
 	
    },
    
    uploadReceiptInvoiceZip : function(grid, rowIndex, colIndex, record) {
    	    	
    	var orderForm = this.getReceiptForm().getForm();
        var me = this;
    	var values = orderForm.getFieldValues();
    	
    	var grid = this.getReceiptGrid();
    	var store = grid.getStore();
    	var s = grid.getSelectionModel().getSelection();
    	var selected = [];
    	var receiptSubtotal = 0;
    	var orderNumber = 0;
    	var isValidReceipt = true;
    	Ext.each(s, function (item) {
    		selected.push(item.data.id);
    		receiptSubtotal = receiptSubtotal + item.data.amountReceived;
    		if(orderNumber == 0) {
    			orderNumber = item.data.documentNumber;
    		} else if (orderNumber !== item.data.documentNumber) {
        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg:  SuppAppMsg.purchaseReceiptSameReceipt});
        		isValidReceipt = false;
    		}
    	});
    	
    	if(isValidReceipt == false) {
    		return false;
    	}
    	
    	var addressNumber = orderForm.findField('addressNumber').getValue();
    	
    	if(selected.length > 0){    		
    		var idSelected = selected.toString();
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    		
    		Ext.Ajax.request({
    		    url: 'supplier/getByAddressNumber.action',
    		    method: 'POST',
    		    params: {
    		    	addressNumber : addressNumber
    	        },
    		    success: function(fp, o) {    		    	
    		    	box.hide();
    		    	var res = Ext.decode(fp.responseText);
    		    	var sup = Ext.create('SupplierApp.model.Supplier',res.data);
    	    		if(sup.data.country == "MX"){
    			    	var filePanel = Ext.create(
    			    					'Ext.form.Panel',
    			    					{
    			    						width : 900,
    			    						items : [
    			    								{
    			    									xtype : 'textfield',
    			    									name : 'documentNumber',
    			    									hidden : true,
    			    									value : orderForm.findField('orderNumber').getValue()
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'documentType',
    			    									hidden : true,
    			    									value : orderForm.findField('orderType').getValue()
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'addressBook',
    			    									hidden : true,
    			    									value : orderForm.findField('addressNumber').getValue()
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'receiptIdList',
    			    									hidden : true,
    			    									value : idSelected
    			    								},{
    			    									xtype : 'textfield',
    			    									name : 'tipoComprobante',
    			    									hidden : true,
    			    									value : Ext.getCmp('optionType').getValue()
    			    								},{
    			    									xtype : 'filefield',
    			    									name : 'file',
    			    									fieldLabel : SuppAppMsg.purchaseFile + '(.zip)*:',
    			    									labelWidth : 120,
    			    									msgTarget : 'side',
    			    									allowBlank : false,
    			    									margin:'20 0 20 0',
    			    									anchor : '90%',
    			    									multiple: true,
    			    									buttonText : SuppAppMsg.suppliersSearch
    			    								}],
    			
    			    						buttons : [ {
    			    							text : SuppAppMsg.supplierLoad,
    			    							margin:'10 0 0 0',
    			    							handler : function() {
    			    								var form = this.up('form').getForm();
    			    								if (form.isValid()) {
    			    									form.submit({
    			    												url : 'uploadReceiptInvoiceZip.action',
    			    												waitMsg : SuppAppMsg.supplierLoadFile,
    			    												success : function(fp, o) {    			    													
    			    													var res = Ext.decode(o.response.responseText);
    			    													me.winLoadInv.destroy();
    			    													if(me.receiptWindow){
    			    														me.receiptWindow.close();
    			    													}
    			    													
    			    													Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.supplierLoadDocSuccBatch});    			    													
    			    												},
    			    										        failure: function(fp, o) {
    			    										        	var res = Ext.decode(o.response.responseText);

    			    										        	if(res.message != null && res.message != "") {
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: res.message});
    			    										        	} else {
    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSuccBatchError});
    			    										        	}    			    										        		    			    										        
    			    										        }
    			    											});
    			    								}
    			    							}
    			    						} ]
    			    					});
    			
    			    	me.winLoadInv = new Ext.Window({
    			    		layout : 'fit',
    			    		title : SuppAppMsg.purchaseUploadCreditNotesTitle,
    			    		width : 600,
    			    		height : 150,
    			    		modal : true,
    			    		closeAction : 'destroy',
    			    		resizable : false,
    			    		minimizable : false,
    			    		maximizable : false,
    			    		plain : true,
    			    		items : [ filePanel ]
    			
    			    	});
    			    	me.winLoadInv.show();
    			    }else if(sup.data.country == "" || sup.data.country == null){
			    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg:  SuppAppMsg.purchaseErrorNonSupplier});
			    	}
    		    },  
		        failure: function(fp, o) {
		        	box.hide();
		        }
    		});

    	}else{
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg:  SuppAppMsg.purchaseReceiptNotSelectedMassive});
    		return false;
    	}
 	
    },
    
    invMultipleLoad : function(grid, rowIndex, colIndex, record) {
    	var orderForm = this.getPurchaseOrderForm().getForm();
        var me = this;
        var grid = this.getPurchaseOrderGrid();
    	var store = grid.getStore();
    	
    	var filePanel = Ext.create(
				'Ext.form.Panel',
				{
					width : 900,
					items : [
							{
								xtype : 'textfield',
								name : 'documentNumber',
								hidden : true,
								value : orderForm.findField('orderNumber').getValue()
							},{
								xtype : 'textfield',
								name : 'documentType',
								hidden : true,
								value : orderForm.findField('orderType').getValue()
							},{
								xtype : 'textfield',
								name : 'addressBook',
								hidden : true,
								value : orderForm.findField('addressNumber').getValue()
							},{
								xtype : 'textfield',
								name : 'tipoComprobante',
								hidden : true,
								value : 'Factura'
							},{
								xtype : 'filefield',
								name : 'file',
								fieldLabel : SuppAppMsg.purchaseInvoice +'(xml):',
								labelWidth : 120,
								msgTarget : 'side',
								allowBlank : false,
								margin:'20 0 5 0',
								anchor : '90%',
								buttonText : SuppAppMsg.suppliersSearch
							},
							{
								xtype : 'filefield',
								name : 'fileTwo',
								fieldLabel : SuppAppMsg.purchaseCreditNote+'(xml):',
								labelWidth : 120,
								msgTarget : 'side',
								allowBlank : false,
								anchor : '90%',
								buttonText : SuppAppMsg.suppliersSearch
							} ],

					buttons : [ {
						text : SuppAppMsg.supplierLoad,
						margin:'10 0 0 0',
						handler : function() {
							var form = this.up('form').getForm();
							if (form.isValid()) {
								form.submit({
											url : 'uploadResources.action',
											waitMsg : SuppAppMsg.supplierLoadFile,
											success : function(fp, o) {
												var res = Ext.decode(o.response.responseText);
												me.winLoadInv.close();
												Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
												if(me.orderDetailWindow){
													me.orderDetailWindow.close();
												}
												alert(res.res);
												store.load();
											},       // If you don't pass success:true, it will always go here
									        failure: function(fp, o) {
									        	var res = Ext.decode(o.response.responseText);
									        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
									        }
										});
							}
						}
					} ]
				});
    	
    	this.winLoadInv = new Ext.Window({
    		layout : 'fit',
    		title : SuppAppMsg.purchaseUploadInvoicewCreditNotesTitle ,
    		width : 600,
    		height : 160,
    		modal : true,
    		closeAction : 'destroy',
    		resizable : false,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		items : [ filePanel ]

    	});
    	this.winLoadInv.show();
    	
    },
    
    approvePendingInvoice : function(grid, record) {
    	var grid = this.getPurchaseOrderGrid();
    	var store = grid.getStore();
    	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    	Ext.Ajax.request({
			url : 'processInvoiceFromOrderWithoutPayment.action',
			method : 'POST',
				params : {
					addressBook: record.data.addressNumber, 
					documentNumber: record.data.orderNumber, 
					documentType:record.data.orderType
				},
				success : function(response,opts) {
					//No muestra mensajes en pantalla
					var resp = Ext.decode(response.responseText);
					store.load();
					box.hide();
				},
				failure : function() {
					box.hide();
				}
			});

    },

    rejectPendingInvoice : function(grid, record) {
    	var grid = this.getPurchaseOrderGrid();
    	var store = grid.getStore();
    	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    	Ext.Ajax.request({
			url : 'rejectInvoiceFromOrderWithoutPayment.action',
			method : 'POST',
				params : {
					addressBook: record.data.addressNumber, 
					documentNumber: record.data.orderNumber, 
					documentType:record.data.orderType
				},
				success : function(response,opts) {
					var resp = Ext.decode(response.responseText);
					store.load();
					box.hide();
				},
				failure : function() {
					box.hide();
				}
			});

    },
    
    acceptSelInv : function(grid, record) {
    	var gridAccept = this.getAcceptInvGrid();
    	var storeAccept = gridAccept.getStore();
    	
		storeAccept.each(function(rec) {
			if(rec){
		       if (rec.data.uuid == record.data.uuid) {
		    	   storeAccept.remove(rec)
		      }
			}
		});
		
		storeAccept.insert(0, record);
    	
    	
    },
    
    rejectSelInv : function(grid, record) {
    	var gridAccept = this.getAcceptInvGrid();
    	var storeAccept = gridAccept.getStore();
		storeAccept.remove(record);
    },

    updateOrder: function(button){
     	var form = this.getPurchaseOrderForm().getForm();
    	var values = form.getFieldValues();
    	var grid = this.getPurchaseOrderDetailGrid();
    	var store = grid.getStore();
    	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    	var itemArray = [];
    	var orderId = values.id;
    	var orderAmount = values.orderAmount;
    	var supplierRole = Ext.getCmp('addressNumberRole').getValue();
    	var toReceive = values.toReceive;
    	var me = this;
    	
    	var pendingQuantity = 0;
    	var toReceiveAmt = 0;
    	var totalAmount = 0;
    	if(supplierRole != "ROLE_SUPPLIER_OPEN"){
        	store.each(function(rec){
        		if(rec.data.toReceive > 0 &&  rec.data.pending != 0){
        			pendingQuantity = pendingQuantity + rec.data.pending;
        		}else if(rec.data.toReceive == 0){
        			pendingQuantity = pendingQuantity + rec.data.pending;
        		}
        		
        		if(rec.data.status != 'ERROR'){
            		var row = Ext.create('SupplierApp.model.PurchaseOrderDetail',rec.data);
            		itemArray.push(row.data);
        		}
        		
        	});
        	

        	if(pendingQuantity == 0){
        		if(itemArray.length > 0){
            		Ext.Ajax.request({
            		    url: 'supplier/receipt/receipt.action',
            		    method: 'POST',
            		    params:{
            		    	orderId:orderId
            		    },
            		    jsonData: itemArray,
            		    success: function(response, opts) {
                            var resp = Ext.decode(response.responseText);
                            box.hide();
                            Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseProductReceipt, msg:  SuppAppMsg.purchaseReceiptProcessSucc});
                            if(me.orderDetailWindow){
    							me.orderDetailWindow.close();
    						}     		
            		    },
            		    failure: function() {
            		        alert('Error');
            		        box.hide();
            		    }
            		}); 
            	}else{
            		box.hide();
            		Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseProductReceiptErrorTitle, msg:  SuppAppMsg.purchaseCaptureError1});
            	}
        	}else{
        		box.hide();
        		Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseProductReceiptErrorTitle, msg:  SuppAppMsg.purchaseCaptureError2});
        	}
    	}else{
    		store.each(function(rec){
        		if(rec.data.status != 'ERROR'){
        			//if(rec.data.toReceive > 0){
        				var row = Ext.create('SupplierApp.model.PurchaseOrderDetail',rec.data);
	            		itemArray.push(row.data);
	            		toReceiveAmt = toReceiveAmt + row.data.toReceive;
        			//}	
        		}
        	});
    		
    		if(toReceiveAmt > 0){
        		if(itemArray.length > 0){
            		Ext.Ajax.request({
            		    url: 'supplier/receipt/receipt.action',
            		    method: 'POST',
            		    params:{
            		    	orderId:orderId
            		    },
            		    jsonData: itemArray,
            		    success: function(response, opts) {
                            var resp = Ext.decode(response.responseText);
                            box.hide();
                            Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseProductReceipt, msg:  SuppAppMsg.purchaseReceiptProcessSucc});
                            if(me.orderDetailWindow){
    							me.orderDetailWindow.close();
    						}     		
            		    },
            		    failure: function() {
            		        alert('Error');
            		        box.hide();
            		    }
            		}); 
            	}else{
            		box.hide();
            		Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseProductReceiptErrorTitle, msg:  SuppAppMsg.purchaseCaptureError3});
            	}
        	}else{
        		box.hide();
        		Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseProductReceiptErrorTitle, msg:  SuppAppMsg.purchaseCaptureError4});
        	}
    	}
    },
    
    openReceiptForm: function(model, record){
    	
    	return true;
    	var me = this;
    	me.receiptWindow = new Ext.Window({
    		layout : 'fit',
    		title : SuppAppMsg.purchaseReceiptDetails,
    		width : 920,
    		height : 500,
    		modal : true,
    		closeAction : 'destroy',
    		resizable : true,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		items : [ {
    			xtype : 'receiptOrderForm',
    			border : true,
    			height : 415
    		}  ]
    	});

    	me.receiptWindow.show();  
    	var form = this.getReceiptOrderForm().getForm();

    	form.loadRecord(record);
    	if("CERRADA" == record.data.status){
    		form.findField('toReceive').setReadOnly(true);
    		form.findField('toReject').setReadOnly(true);
    		form.findField('reason').setReadOnly(true);
    		form.findField('notes').setReadOnly(true);
    	}else{
    		form.findField('toReceive').setReadOnly(false);
    		form.findField('toReject').setReadOnly(false);
    		form.findField('reason').setReadOnly(false);
    		form.findField('notes').setReadOnly(false);
    	}
    	

    	if(record.data.tolerances != null){
    		form.findField('tolerances_qtyUnits').setValue(record.data.tolerances.qtyUnits);
    		form.findField('tolerances_qtyPercentage').setValue(record.data.tolerances.qtyPercentage);
    		//form.findField('tolerances_unitCostPercentage').setValue(record.data.tolerances.unitCostPercentage);
    		//form.findField('tolerances_unitCostAmount').setValue(record.data.tolerances.unitCostAmount);
    		//form.findField('tolerances_extendedAmtPercentage').setValue(record.data.tolerances.extendedAmtPercentage);
    		//form.findField('tolerances_extendedAmtAmount').setValue(record.data.tolerances.extendedAmtAmount);
    	}
    	
    	
    },
    
    insertReceipt: function(button) {
    	var form = this.getReceiptOrderForm().getForm();
    	var values = form.getFieldValues();
    	var received = values.received;
    	var rejected = values.rejected;
    	var quantity = values.quantity;
    	var pending = values.pending;
    	var toReceive = values.toReceive == ""?0:values.toReceive;
    	var toReject = values.toReject == ""?0:values.toReject;
    	var reason = values.reason;
    	var notes = values.notes;
    	var extendedPrice = values.extendedPrice;
    	var tolerances_qtyUnits = values.tolerances_qtyUnits; 
    	var tolerances_qtyPercentage = values.tolerances_qtyPercentage; 
    	var tolerances_extendedAmtAmount = values.tolerances_extendedAmtAmount;
    	var tolerances_extendedAmtPercentage = values.tolerances_extendedAmtPercentage;
    	var unitCost = values.unitCost;
    	
    	var supplierRole = Ext.getCmp('addressNumberRole').getValue();
    	
    	//Caculate tolerances per quantity:
    	var hasTolerances = false;
    	
		var qtyMax = quantity;
		var qtyMin = quantity;
    	if(tolerances_qtyUnits > 0){
    		hasTolerances = true;
    		qtyMax = quantity + tolerances_qtyUnits;
    		qtyMin = quantity - tolerances_qtyUnits;
    	}
    	
    	var qtyPerc = 0;
    	if(tolerances_qtyPercentage > 0){
    		hasTolerances = true;
    		qtyPerc = quantity * (tolerances_qtyPercentage / 100);
    		if(qtyPerc > tolerances_qtyUnits){
    			qtyMax = quantity + qtyPerc;
        		qtyMin = quantity - qtyPerc;
    		}
    	}

    	if(toReceive == 0 && toReject == 0){
    		return true;
    	}
    	
    	if(pending == 0){
    		Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.supplierMessage, msg: SuppAppMsg.purchaseOrdersMsg3 });
    		return false;
    	}
    	
    	if(toReject > 0){
			if(reason == ""){
				Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseCaptureErrorTitle, msg:  SuppAppMsg.purchaseCaptureError5});
				return false;
			}
			
			if(reason =="Otro (Registrelo en las notas adicionales)"){
				if(notes=""){
					Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseCaptureErrorTitle, msg:  SuppAppMsg.purchaseCaptureError6});
					return false;
				}
			}
    	}
    	
    	var recOpen = 0;
    	var recOpenAmount = 0;
    	var recAmountReceived = 0;
    	
    	var totRec = toReject + toReceive;
    	if(hasTolerances){
    		if(tolerances_qtyPercentage > 0){
    			
    	    	if(supplierRole == "ROLE_SUPPLIER_OPEN"){
                	// Valida tolerancias en cantidades
            		if(totRec > qtyMax){
            			Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseCaptureErrorTitle, msg:  SuppAppMsg.purchaseCaptureError7 + ' Max = ' + qtyMax + ' y Min = ' + qtyMin});
            			return false;
            		}else{
            	    	recOpen = quantity - received - rejected - toReceive - toReject;
            	    	if(recOpen < 0){
            	    		recOpen = 0;
            	    	}
            	    	recOpenAmount = quantity - received - rejected - toReceive - toReject;
            	    	if(recOpenAmount < 0){
            	    		recOpenAmount = 0;
            	    	}
            	    	recAmountReceived = (received + toReceive);
            		}
    	    	}else{
                	// Valida tolerancias en cantidades
            		if(totRec > qtyMax || totRec < qtyMin ){
            			Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseCaptureErrorTitle, msg:  SuppAppMsg.purchaseCaptureError7 + ' Max = ' + qtyMax + ' y Min = ' + qtyMin});
            			return false;
            		}else{
            				
            	    	recOpenAmount = 0;
            	    	recAmountReceived = (received + toReceive);
            		}
    	    	}
    		}

    	}else{
    		
    		if(toReceive > quantity){
    			Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseCaptureErrorTitle, msg:  SuppAppMsg.purchaseCaptureError8});
    			return false;
    		}
    		
    		if((toReceive + toReject) > quantity){
    			Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseCaptureErrorTitle, msg:  SuppAppMsg.purchaseCaptureError9});
    			return false;
    		}
    		
    		if((toReceive + toReject) > pending){
    			Ext.MessageBox.alert({ maxWidth: 400, minWidth: 200, title: SuppAppMsg.purchaseCaptureErrorTitle, msg:  SuppAppMsg.purchaseCaptureError10});
    			return false;
    		}
    		
    		recOpen = quantity - received - rejected - toReceive - toReject;
	    	recOpenAmount = (quantity - received - rejected - toReceive - toReject);
	    	recAmountReceived = (received + toReceive);
    	}
 
    	var store = this.getPurchaseOrderDetailGrid().getStore();
    	store.each(function(rec){
    		 if(rec.data.lineNumber == values.lineNumber && rec.data.itemNumber == values.itemNumber){
    			 rec.set("toReceive",toReceive);
    			 rec.set("toReject",toReject);
    			 rec.set("reason",reason);
    			 rec.set("notes",notes);
    			 rec.set("pending", recOpen);
    			 rec.set("openAmount", recOpenAmount * rec.data.unitCost);
    			 rec.set("amuntReceived", recAmountReceived * rec.data.unitCost);

    			 if(recOpen != 0){
    				 rec.set("status",'PARCIAL'); 
    			 }else{
    				 rec.set("status",'COMPLETA'); 
    			 }

 		    }
        });
    	this.receiptWindow.close();
    	
    },
    
    poSearch: function(button) {

    	var status = {
    	        STATUS_OC_RECEIVED: 'OC RECIBIDA',
    	        STATUS_OC_APPROVED: 'OC APROBADA',
    	        STATUS_OC_SENT: 'OC ENVIADA',
    	        STATUS_OC_CLOSED: 'OC CERRADA',
    	        STATUS_OC_INVOICED: 'OC FACTURADA',
    	        STATUS_OC_PROCESSED: 'OC PROCESADA',
    	        STATUS_OC_PAID: 'OC PAGADA',
    	        STATUS_OC_PAYMENT_COMPL: 'OC COMPLEMENTO',
    	        STATUS_OC_CANCEL: 'OC CANCELADA'
    	}
    	
    	var grid = this.getPurchaseOrderGrid();
    	var store = grid.getStore();
    	var poNumber = Ext.getCmp('poNumber').getValue() ==''?'0':Ext.getCmp('poNumber').getValue();
    	var supNumber = Ext.getCmp('supNumber').getValue() == ''?'':Ext.getCmp('supNumber').getValue();
    	var poFromDate = Ext.getCmp('poFromDate').getValue();
    	var poToDate = Ext.getCmp('poToDate').getValue();
    	var poTypeCombo = Ext.getCmp('poTypeCombo').getValue();
    	//var poStatus = Ext.getCmp('combostatus').getValue();
    	var poComStatus = Ext.getCmp('poComboStatus').getValue();
    	var comboStatus = status[poComStatus];
    	
    	grid.headerCt.items.getAt(0).hide();
    	
    	/*if(comboStatus == status.STATUS_OC_INVOICED){
        	grid.headerCt.items.getAt(0).show();
    	}
    	
    	
    	if((comboStatus == status.STATUS_OC_SENT && role=='ROLE_TAX') || (comboStatus == status.STATUS_OC_SENT && role=='ROLE_ADMIN')){
        	grid.headerCt.items.getAt(0).show();
    	}
    	
    	if(comboStatus == status.STATUS_OC_PROCESSED && role == 'ROLE_WNS'){
        	grid.headerCt.items.getAt(0).show();
    	}*/
    	
    	if(comboStatus){
    		
    	}else{
    		comboStatus='';
    	}

    	store.removeAll();
    	store.proxy.extraParams = { 
				    			poNumber : poNumber?poNumber:0,
				    			supNumber : supNumber?supNumber:"",
				    	    	poFromDate : poFromDate?poFromDate:null,
				    	    	poToDate : poToDate?poToDate:null,
				    	    	poTypeCombo : poTypeCombo?poTypeCombo:null,		
				    	    	status:comboStatus,
				    	    	userName:userName
    	    			        }
    	store.loadPage(1);
    	grid.getView().refresh()
    	
    	gblMassiveLoadEx = getUDCStore('MASSIVEUP', '', '', '');
    	gblMassiveLoadEx.load();
    },
    
    delPoSearch: function(button) {
    	var grid = this.getDeliverPurchaseOrderGrid();
    	var store = grid.getStore();
    	var poNumber = Ext.getCmp('delPoNumber').getValue() ==''?'0':Ext.getCmp('delPoNumber').getValue();
    	var supNumber = Ext.getCmp('delSupNumber').getValue() == ''?'':Ext.getCmp('delSupNumber').getValue();
    	var poFromDate = Ext.getCmp('delPoFromDate').getValue();
    	var poToDate = Ext.getCmp('delPoToDate').getValue();

    	store.proxy.extraParams = { 
    			poNumber : poNumber?poNumber:0,
    			supNumber : supNumber?supNumber:"",
    	    	poFromDate : poFromDate?poFromDate:null,
    	    	poToDate : poToDate?poToDate:null,
    	    	status:'OC RECIBIDA',
    	    	userName:userName
    	    			}
    	
    	store.load();
  
    },
    
    delPoConfirm: function(button) {
    	var grid = this.getDeliverPurchaseOrderGrid();
    	var store = grid.getStore();
    	var s = grid.getSelectionModel().getSelection();
    	var selected = [];
    	Ext.each(s, function (item) {
    	  
    		if(item.data.dateRequested != null){
    			item.data.dateRequested.addHours(23);
        	}
    		
    		if(item.data.promiseDelivery != null){
    			item.data.promiseDelivery.addHours(23);
        	}
    		
    		if(item.data.invoiceUploadDate != null){
    			item.data.invoiceUploadDate.addHours(23);
        	}
    		
    		if(item.data.paymentUploadDate != null){
    			item.data.paymentUploadDate.addHours(23);
        	}
    		
    		if(item.data.paymentUploadDate != null){
    			item.data.estimatedPaymentDate.addHours(23);
        	}
    		
    		selected.push(item.data);
    	});
    	
    	if(selected.length > 0){
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
	    	Ext.Ajax.request({
				url : 'supplier/orders/confirmEmailOrders.action',
				method : 'POST',
				jsonData: selected,
				success : function(response,opts) {
					box.hide();
					var resp = Ext.decode(response.responseText);
					store.load();
					Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.approvalResponse , msg: SuppAppMsg.purchaseOrdersReleased });
				},
				failure : function() {
					box.hide();
					Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg:  SuppAppMsg.purchaseCaptureError11});
				}
			});
    	}
    	
    	
  
    },
    
    poLoadPayment: function(button) {
		    var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
	    	Ext.Ajax.request({
				url : 'supplier/orders/importPayments.action',
				method : 'POST',
				success : function(response,opts) {
					box.hide();
					var resp = Ext.decode(response.responseText);
					Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.approvalResponse, msg: SuppAppMsg.purchaseImportBranch  });
				},
				failure : function() {
					box.hide();
					Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: SuppAppMsg.purchaseCaptureError12 });
				}
			});   	
    },

    poInvAccept: function(button) {
    	var grid = this.getPurchaseOrderGrid();
    	var s = grid.getSelectionModel().getSelection();
    	var store = grid.getStore();
    	var selected = [];
    	Ext.each(s, function (item) {

    		if(item.data.dateRequested != null){
    			item.data.dateRequested.addHours(23);
        	}
    		
    		if(item.data.promiseDelivery != null){
    			item.data.promiseDelivery.addHours(23);
        	}
    		
    		if(item.data.invoiceUploadDate != null){
    			item.data.invoiceUploadDate.addHours(23);
        	}
    		
    		if(item.data.paymentUploadDate != null){
    			item.data.paymentUploadDate.addHours(23);
        	}
    		
    		if(item.data.paymentUploadDate != null){
    			item.data.estimatedPaymentDate.addHours(23);
        	}

    	  selected.push(item.data);
    	});
    	
    	if(selected.length > 0){
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
	    	Ext.Ajax.request({
				url : 'supplier/orders/confirmOrderInvoice.action',
				method : 'POST',
				jsonData: selected,
				success : function(response,opts) {
					box.hide();
					var resp = Ext.decode(response.responseText);
					store.load();
					Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.approvalResponse, msg: SuppAppMsg.purchaseSuccessMsg1 });
				},
				failure : function() {
					box.hide();
					Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg:  SuppAppMsg.purchaseCaptureError13});
				}
			});
    	}
    	
    	
  
    },
    
    poInvReject: function(button) {

    	var me = this;
    	
    	Ext.create('Ext.window.MessageBox', {
            listeners: {
                show: function (msg) {
                    var bbar = msg.bottomTb;
                    bbar.insert(bbar.items.length, {
                        xtype: 'checkbox',
                        value:false,
                        id:'sendSupEmailCheck',
                        boxLabel: 'Enviar email al proveedor',
                        margin:'0 0 0 30',
                        hidden:role == 'ROLE_WNS'?false:true
                    });
                }
            }
        }).show({
        	title : SuppAppMsg.purchaseOrdersTitle1,
			msg : SuppAppMsg.purchaseOrdersMsg4,
			buttons : Ext.MessageBox.YESNO,
			multiline: true,
			width:500,
			buttonText : {
				yes : SuppAppMsg.approvalAcept,
				no : SuppAppMsg.approvalExit
			},
			fn : function(btn, text) {
				if (btn === 'yes') {
					if(text != ""){
						
						if(text.length > 1000){
							Ext.Msg.alert('Error',SuppAppMsg.purchaseCaptureError14 );
							return false;
						}
						
						var sendProviderEmail = true;
						var rejectType = "";
						if(role == 'ROLE_WNS'){
							sendProviderEmail = Ext.getCmp('sendSupEmailCheck').getValue();
							rejectType = "WNS";
						}

						var box = Ext.MessageBox.wait(
								SuppAppMsg.supplierProcessRequest,
								SuppAppMsg.approvalExecution);
						var notes = text;
				    	var currentDateTime = new Date().toLocaleString();
						
						var grid = me.getPurchaseOrderGrid();
				    	var s = grid.getSelectionModel().getSelection();
				    	var store = grid.getStore();
				    	var selected = [];
				    	Ext.each(s, function (item) {
				    	  if(item.data.rejectNotes != null){
				    		  item.data.rejectNotes = item.data.rejectNotes + "<br />" + currentDateTime + " : " + text;  
				    	  }else{
				    		  item.data.rejectNotes =  currentDateTime + " : " + text;
				    	  }
				    	  
				    	  if(item.data.dateRequested != null){
				    			item.data.dateRequested.addHours(23);
				        	}
				    		
				    		if(item.data.promiseDelivery != null){
				    			item.data.promiseDelivery.addHours(23);
				        	}
				    		
				    		if(item.data.invoiceUploadDate != null){
				    			item.data.invoiceUploadDate.addHours(23);
				        	}
				    		
				    		if(item.data.paymentUploadDate != null){
				    			item.data.paymentUploadDate.addHours(23);
				        	}
				    		
				    		if(item.data.paymentUploadDate != null){
				    			item.data.estimatedPaymentDate.addHours(23);
				        	}
				    	  
				    	  selected.push(item.data);
				    	});

				    	
				    	if(selected.length > 0){
				    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
					    	Ext.Ajax.request({
								url : 'supplier/orders/rejectOrderInvoice.action',
								method : 'POST',
								params:{
								    sendEmailToSupplier:sendProviderEmail,
								    rejectType:rejectType
								},
								jsonData: selected,
								success : function(response,opts) {
									box.hide();
									var resp = Ext.decode(response.responseText);
									store.load();
									Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.approvalResponse, msg:  SuppAppMsg.purchaseOrdersMsg2});
								},
								failure : function() {
									box.hide();
									Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg:  SuppAppMsg.purchaseCaptureError13});
								}
							});
				    	}
				    	

					}else{
            			Ext.Msg.alert(SuppAppMsg.approvalAlert, SuppAppMsg.purchaseOrdersMsg1 );
            		}

				}
			}
        });
    	

    },
    
    poLoadList: function(button) {
    	var grid = this.getPurchaseOrderGrid();
    	var store = grid.getStore();
    	var fromDate = Ext.getCmp('fromDate').getValue();    	
    	var toDate = Ext.getCmp('toDate').getValue();
    	var addressNumber = Ext.getCmp('addressNumber').getValue();
    	var orderNumber = Ext.getCmp('orderNumber').getValue() == null?0:Ext.getCmp('orderNumber').getValue();
    	
		if(fromDate != "" && fromDate != null && toDate != "" && toDate != null){
			fromDate = fromDate.toLocaleDateString("es-MX");
			toDate = toDate.toLocaleDateString("es-MX");
			
			if((addressNumber == "" || addressNumber == null) && (orderNumber == "" || orderNumber == null || orderNumber ==0) ){
				Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: "Mensaje", msg: "Debe especificar un proveedor, orden de compra o ambos antes de someter la replicación de órdenes" });
				return false;
			}
				
			var box = Ext.MessageBox.wait("Ejecutando...", "Mensaje");
	    	Ext.Ajax.request({
				url : 'supplier/orders/replicatePurchaseOrderBySelection.action',
				method : 'GET',
				params:{
					orderNumber:orderNumber,
					addressNumber:addressNumber,
					fromDate:fromDate,
					toDate:toDate
				},
					success : function(response,opts) {
						Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: "Mensaje", msg: "El proceso de carga se ha iniciado en modo Batch" });
						box.hide();
					},
					failure : function() {
						box.hide();
					}
				});
    	}else{
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: "Mensaje", msg: "Los campos de fecha son obligatorios" });
    	}
    },
    
    poLoadFTPInv: function(button) {
    			var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    	    	Ext.Ajax.request({
    				url : 'supplier/orders/loadFTPInvoices.action',
    				method : 'GET',
    					success : function(response,opts) {
    						var resp = Ext.decode(response.responseText);
    						box.hide();
    						Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMessage, msg:  "El proceso batch ha sido sometido exitosamente"});
    					},
    					failure : function() {
    						box.hide();
    					}
    				});
     },
    
    poLoadCompl: function(button) {
    	
    	var supNumber = Ext.getCmp('supNumber').getValue() == ''?'':Ext.getCmp('supNumber').getValue();
    	if(supNumber != ""){

        	new Ext.Window({
        		  //width        : 1115,
        		  //height       : 465,
        		/*width: Ext.Element.getViewportWidth() * 0.7,   // 👈 40% de pantalla
                maxWidth: 1115,                                // 👈 ancho mínimo
                height: Ext.Element.getViewportHeight() * 0.5, // 👈 40% de alto
                maxHeight: 465,*/
        		width        : 1115,
      		    height       : 465,
                scrollable: true,
        		  title        : 'Complementos de Pago',
        		  border       : false,
    	      		modal : true,
    	    		closeAction : 'destroy',
    	    		resizable : true,
    	    		minimizable : false,
    	    		maximizable : false,
    	    		plain : true,
        		  items : [
        			  {
        	    			xtype : 'complementoPagoPanel',
        	    			border : true,
        	    			height : 460
        	    		}
        			  ]
        		}).show();
    		
    		var gridSel = this.getSelInvGrid();
        	var gridAccept = this.getAcceptInvGrid();
        	var store = gridSel.getStore();
        	
    		store.proxy.extraParams = { 
			        addressBook:supNumber?supNumber:""
        	}
    		
    		store.on('load', function() {
    			/*
    		    store.filter({
    		        filterFn: function(rec) {
    		        	if(rec.get('paymentType') == "PPD"){
    		        		return true
    		        	}else{
    		        		return false;
    		        	}
    		        }
    		    });
    		    */
    		});
    		store.load();
    	}else{
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.purchaseOrdersMsg5 });
    	}
    	
    },
    
    invNbrSearch: function(button) {
    	var grid = this.getSelInvGrid();
    	var store = grid.getStore();
    	var poNumber = Ext.getCmp('invNbr').getValue();
    	var supNumber = Ext.getCmp('supNumber').getValue();

    	store.proxy.extraParams = { 
    			poNumber : poNumber?poNumber:0,
    			supNumber : supNumber?supNumber:"",
    	    	poFromDate : null,
    	    	poToDate : null,
    	    	status:'OC PAGADA',
    	    	userName: userName
    	    			}
    	
    	store.load();
    },
    
    gridSelectionChange: function(model, record) {
    	var orderWithAddress = false;
        if (record) {
        	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
        	var me = this;
        	if(record.data.addressNumber){
        		if(record.data.addressNumber != ""){
                	Ext.Ajax.request({
        				url : 'supplier/getByAddressNumber.action',
        				method : 'GET',
        					params : {
        						addressNumber : record.data.addressNumber
        					},
        					success : function(response,opts) {
        						var resp = Ext.decode(response.responseText);
        						if(resp.data != null){
        							
	        						Ext.getCmp('supplierCountry').setValue(resp.data.country);
	        						Ext.getCmp('supplierTaxId').setValue(resp.data.taxId==null?"":resp.data.taxId);
	        						var address = resp.data.calleNumero + ', ' + resp.data.colonia + ', ' + resp.data.delegacionMnicipio + ', ' + resp.data.estado + ', CP:' + resp.data.codigoPostal;
	        						Ext.getCmp('supplierAddress').setValue(address);
	        						Ext.getCmp('supplierName').setValue(resp.data.razonSocial);
	        						orderWithAddress = true;
        						}else{
        							
        				        		Ext.getCmp('updateOrder').setVisible(false);
        				        		Ext.getCmp('uploadFiscalDoc').setVisible(false);
        				        		Ext.getCmp('uploadMultipleFiscalDoc').setVisible(false);
        				        		Ext.getCmp('uploadFiscalDocNoPayment').setVisible(false);
        				        		var win = new Ext.Window({
        				        			  title: 'Mensaje del sistema',
        				        			  //width: 540,
        				        			  //height: 100,
        				        			  width: Ext.Element.getViewportWidth() * 0.45,   // 👈 40% de pantalla
        				                      minWidth: 540,                                // 👈 ancho mínimo
        				                      height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
        				                      minHeight: 100,
        				              		modal : true,
        				        			  modal:true,
        				        			  preventBodyReset: true,
        				        			  html: '<br /><center>La OC existe, pero el proveedor no está registrado en el sistema</center>'
        				        			});
        				        			win.show();
        				        	
        						}
        					},
        					failure : function() {
        					}
        				});
        		}
        	}

        	me.orderDetailWindow = new Ext.Window({
        		layout : 'fit',
        		title : SuppAppMsg.purchaseOrdersTitle2 ,
        		width : 1150,
        		height : 750,
        		//width: Ext.Element.getViewportWidth() * 0.80,   // 👈 40% de pantalla
                //maxWidth: 1150,                                // 👈 ancho mínimo
                //height: Ext.Element.getViewportHeight() * 0.80, // 👈 40% de alto
                //maxHeight: 750,
        		modal : true,
        		modal : true,
        		closeAction : 'destroy',
        		resizable : true,
        		minimizable : false,
        		maximizable : false,
        		plain : true,
        		items : [ {
        			xtype : 'purchaseOrderDetailPanel',
        			border : true,
        			height : 425
        		}  ]

        	});
        	
        	me.orderDetailWindow.show();        	
        	var form = this.getPurchaseOrderForm().getForm();
        	
        	if(record.data.currecyCode != "MXP"){
        		record.data.orderAmount = record.data.foreignAmount;
        	}
        	form.loadRecord(record);
        	var g = this.getPurchaseOrderDetailGrid();
        	g.store.loadData([], false);
        	g.getView().refresh();
        	
        	var itemList = record.get('purchaseOrderDetail');
        	Ext.each(itemList, function(rec, index) {
        		rec.toReceive = 0;
        		rec.toReject = 0;
        		rec.reason ="";
   				var r = Ext.create('SupplierApp.model.PurchaseOrderDetail',rec);
   				g.store.insert(index, r);
            });
        	
        	
        	Ext.getCmp('getInvoiceCodes').setVisible(false);
        	
        	if(record.data.rejectNotes == null || record.data.rejectNotes == ''){
        		Ext.getCmp('rejectNotesButton').setVisible(false);	
        	}else{
        		Ext.getCmp('rejectNotesButton').setVisible(true);
        	}
        	
        	if(record.data.headerNotes == null || record.data.headerNotes == ''){
        		Ext.getCmp('headerNotesButton').setVisible(false);	
        	}else{
        		Ext.getCmp('headerNotesButton').setVisible(true);
        	}
        	        	
        	g.store.sort('lineNumber','ASC');
        	Ext.getCmp('openForeignDoc').setVisible(false);
        	Ext.getCmp('fileListHtml').update("");
        	if(record.data.status == "OC FACTURADA" && role == 'ROLE_SUPPLIER_OPEN'){
        		Ext.getCmp('uploadFiscalDoc').setVisible(true);
        	}else if(record.data.status == "OC FACTURADA" && role != 'ROLE_SUPPLIER_OPEN'){
        		Ext.getCmp('updateOrder').setVisible(false);
        		Ext.getCmp('uploadFiscalDoc').setVisible(false);
        		Ext.getCmp('uploadMultipleFiscalDoc').setVisible(false);
        		Ext.getCmp('uploadFiscalDocNoPayment').setVisible(false);

        	}else{
            	if(record.data.orderStauts == "OC ENVIADA"){
            		var country = form.findField('supplierCountry').getValue();
            		if(country != 'MX' && role == 'ROLE_TAX' && record.data.invoiceNumber != null){
            			Ext.getCmp('openForeignDoc').setVisible(true);
            		}
            		
            		Ext.getCmp('updateOrder').setVisible(false);
            		Ext.getCmp('uploadFiscalDoc').setVisible(true);
            		if(invException === "Y")
            			Ext.getCmp('uploadMultipleFiscalDoc').setVisible(true);
            		else
            			Ext.getCmp('uploadMultipleFiscalDoc').setVisible(false);
            		
            		//Ext.getCmp('uploadFiscalDocNoPayment').setVisible(true);
            		
            	}else if (record.data.orderStauts == "PARCIAL"){
            		//Ext.getCmp('updateOrder').setVisible(true);
            		Ext.getCmp('uploadFiscalDoc').setVisible(true);
            		
            		if(invException === "Y")
            			Ext.getCmp('uploadMultipleFiscalDoc').setVisible(true);
            		else
            			Ext.getCmp('uploadMultipleFiscalDoc').setVisible(false);
            		
            		//Ext.getCmp('uploadFiscalDocNoPayment').setVisible(true);
            	}else{
            		//Ext.getCmp('updateOrder').setVisible(true);
            		Ext.getCmp('uploadFiscalDoc').setVisible(false);
            		Ext.getCmp('uploadMultipleFiscalDoc').setVisible(false);
            		//Ext.getCmp('uploadFiscalDocNoPayment').setVisible(false);
            	}
        	}

        	
        	if(role == 'ROLE_SUPPLIER'|| role == 'ROLE_SUPPLIER_OPEN'){
        		 // Ext.getCmp('updateOrder').setVisible(false);	
        	}
        	
        	var isInvoice = false;
        	Ext.Ajax.request({
				url : 'documents/listDocumentsByOrder.action',
				method : 'GET',
					params : {
						start : 0,
						limit : 15,
						orderNumber : record.data.orderNumber,
						orderType : record.data.orderType,
						addressNumber : record.data.addressNumber
					},
					success : function(response,opts) {
						
						response = Ext.decode(response.responseText);
						var index = 0;
						var files = "";
						for (index = 0; index < response.data.length; index++) {
							var accepted = " ACEPTADO";
							if(response.data[index].accept == false){
								accepted = " POR VALIDAR"
							}
							
							var dateObj = new Date(response.data[index].uploadDate);

							var formattedDate = dateObj.toLocaleString('es-ES', {
							    day: '2-digit',
							    month: '2-digit',
							    year: 'numeric',
							    hour: '2-digit',
							    minute: '2-digit',
							    second: '2-digit'
							});
							
							var showDocs = true;
							if(showDocs){	
								if(!response.data[index].uuid.includes('-REJECTED-')){
									if(role == 'ROLE_ADMIN'){
										if(record.data.orderStauts == "OC FACTURADA" && response.data[index].fiscalType == "Otros"){
											var href = "documents/openDocument.action?id=" + response.data[index].id;
											var fileHref = "<a href= '" + href + "' target='_blank'>" +  response.data[index].name + "</a>";
				                            files = files + "> " + fileHref + " - " + response.data[index].size + " bytes : " + response.data[index].fiscalType + accepted + "-fecha "+formattedDate +  "&nbsp;&nbsp;&nbsp;" + "<A HREF='javascript:deleteDocument(" + response.data[index].id + ")'>Eliminar</A>" + "<br />";
										}else{
											var href = "documents/openDocument.action?id=" + response.data[index].id;
											var fileHref = "<a href= '" + href + "' target='_blank'>" +  response.data[index].name + "</a>";
				                            files = files + "> " + fileHref + " - " + response.data[index].size + " bytes : " + response.data[index].fiscalType + accepted + "-fecha "+formattedDate +  "<br />";
										}
										
									}else{
										var href = "documents/openDocument.action?id=" + response.data[index].id;
										var fileHref = "<a href= '" + href + "' target='_blank'>" +  response.data[index].name + "</a>";
			                            files = files + "> " + fileHref + " - " + response.data[index].size + " bytes : " + response.data[index].fiscalType + accepted + "-fecha "+formattedDate +  "<br />";
									}	
								}
							}
                            
						} 
						 box.hide();
						 if(isInvoice == true){
							 Ext.getCmp('uploadFiscalDoc').setVisible(false);
				        	 //Ext.getCmp('uploadFiscalDocNoPayment').setVisible(false);
						 }
							 
						      
						 //Ext.getCmp('fileListHtml').setValue(files);
						 Ext.getCmp('fileListHtml').update(files);

					},
					failure : function() {
					}
				});
        	
        	if(record.data.addressNumber){
        		if(record.data.addressNumber != ""){
                	Ext.Ajax.request({
        				url : 'admin/users/searchUser.action',
        				method : 'GET',
        					params : {
        						user : record.data.addressNumber
        					},
        					success : function(response,opts) {
        						var resp = Ext.decode(response.responseText);
        						Ext.getCmp('addressNumberRole').setValue(resp.data.role);
        					},
        					failure : function() {
        						var i = 0;
        					}
        				});
        		}
        	}
        	
            box.hide();
        	
        }
    },
    
    
    invLoad : function(grid, rowIndex, colIndex, record) {
    	var orderForm = this.getPurchaseOrderForm().getForm();
        var me = this;
        var grid = this.getPurchaseOrderGrid();
    	var store = grid.getStore();
    	var values = orderForm.getFieldValues();
    	
    	if(values.supplierCountry == "MX"){
		    	var filePanel = Ext.create(
		    					'Ext.form.Panel',
		    					{
		    						width : 900,
		    						items : [
		    								{
		    									xtype : 'textfield',
		    									name : 'documentNumber',
		    									hidden : true,
		    									value : orderForm.findField('orderNumber').getValue()
		    								},{
		    									xtype : 'textfield',
		    									name : 'documentType',
		    									hidden : true,
		    									value : orderForm.findField('orderType').getValue()
		    								},{
		    									xtype : 'textfield',
		    									name : 'addressBook',
		    									hidden : true,
		    									value : orderForm.findField('addressNumber').getValue()
		    								},{
		    									xtype : 'textfield',
		    									name : 'tipoComprobante',
		    									hidden : true,
		    									value : 'Factura'
		    								},{
		    									xtype : 'filefield',
		    									name : 'file',
		    									fieldLabel : SuppAppMsg.purchaseFileXML + '*:',
		    									labelWidth : 120,
		    									msgTarget : 'side',
		    									allowBlank : false,
		    									margin:'20 0 20 0',
		    									anchor : '90%',
		    									multiple: true,
		    									buttonText : SuppAppMsg.suppliersSearch
		    								},
		    								{
		    									xtype : 'filefield',
		    									name : 'fileTwo',
		    									fieldLabel : SuppAppMsg.purchaseInvoice + '(.pdf)*:',
		    									labelWidth : 120,
		    									msgTarget : 'side',
		    									allowBlank : false,
		    									anchor : '90%',
		    									buttonText : SuppAppMsg.suppliersSearch,
		    									margin:'10 0 0 0'
		    								}],
		
		    						buttons : [ {
		    							text : SuppAppMsg.supplierLoad,
		    							margin:'10 0 0 0',
		    							handler : function() {
		    								var form = this.up('form').getForm();
		    								if (form.isValid()) {
		    									form.submit({
		    												url : 'uploadInvoiceFromOrder.action',
		    												waitMsg : SuppAppMsg.supplierLoadFile,
		    												success : function(fp, o) {
		    													var res = Ext.decode(o.response.responseText);
		    													me.winLoadInv.close();
		    													Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
		    													if(me.orderDetailWindow){
		    														me.orderDetailWindow.close();
		    													}
		    													store.load();
		    												},       // If you don't pass success:true, it will always go here
		    										        failure: function(fp, o) {
		    										        	
		    										        	var res = o.response.responseText;
		    										        	var result = Ext.decode(res);
		    										        	var msgResp = result.message
		    										        	
		    										        	if(msgResp == "Error_1"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError1});
		    										        	}else if(msgResp == "Error_2"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError2});
		    										        	}else if(msgResp == "Error_3"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError3});
		    										        	}else if(msgResp == "Error_4"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError4});
		    										        	}else if(msgResp == "Error_5"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError5});
		    										        	}else if(msgResp == "Error_6"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError6});
		    										        	}else if(msgResp == "Error_7"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError7});
		    										        	}else if(msgResp == "Error_8"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO1});
												        	    }else if(msgResp == "Error_9"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO2});
		    										        	}else if(msgResp == "Error_10"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO3});
		    										        	}else if(msgResp == "Error_11"){
												        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError6});
												        	    }else if(msgResp == "Error_12"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO4});
		    										        	}else if(msgResp == "Error_13"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO5});
		    										        	}else if(msgResp == "Error_14"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO6});
		    										        	}else if(msgResp == "Error_15"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO7});
		    										        	}else if(msgResp == "Error_16"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO8});
												        	    }else if(msgResp == "Error_17"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO9});
		    										        	}else if(msgResp == "Error_18"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP10});
		    										        	}else if(msgResp == "Error_19"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP11});
												        	    }else if(msgResp.startsWith("Error_20_", 0)){
		    										        		var dataResp = msgResp.substring(9,msgResp.length);
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP12 + ' ' + dataResp});
		    										        	}else if(msgResp.startsWith("Error_21_", 0)){
		    										        		var dataResp = msgResp.substring(9,msgResp.length);
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP13_a + ' ' + dataResp + ' ' + SuppAppMsg.msgErrorP13_b});
		    										        	}else if(msgResp.startsWith("Error_22_", 0)){
		    										        		var data_resp = msgResp.substring(9,msgResp.length);
		    										        		var r1 = data_resp.split("_:_");
		    										        		var invCurrency = r1[0];
		    										        		var oCurr = r1[1];
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP13_a + ' ' + invCurrency + ' ' + SuppAppMsg.msgErrorP14 + ' ' + oCurr});
		    										        	}else if(msgResp.startsWith("Error_23_", 0)){
		    										        		var dataResp = msgResp.substring(9,msgResp.length);
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP15 + ' ' + dataResp});
		    										        	}else if(msgResp.startsWith("Error_24_", 0)){
		    										        		var dataResp = msgResp.substring(9,msgResp.length);
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP16_a + ' ' + dataResp + SuppAppMsg.msgErrorP16_b});
		    										        	}else if(msgResp.startsWith("Error_25_", 0)){
		    										        		var dataResp = msgResp.substring(9,msgResp.length);
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP17 + ' ' + dataResp + SuppAppMsg.msgErrorP16_b});
		    										        	}else if(msgResp.startsWith("Error_26_", 0)){
		    										        		var dataResp = msgResp.substring(9,msgResp.length);
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP18 + ' ' + dataResp});
		    										        	}else if(msgResp.startsWith("Error_27_", 0)){
		    										        		var dataResp = msgResp.substring(9,msgResp.length);
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP19_a + ' ' + dataResp + ' ' + SuppAppMsg.msgErrorP19_b});
		    										        	}else if(msgResp.startsWith("Error_28_", 0)){
		    										        		var data_resp = msgResp.substring(9,msgResp.length);
		    										        		var r1 = data_resp.split("_:_");
		    										        		var receiptTaxCode = r1[0];
		    										        		var tasasRequeridas = r1[1];
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP20_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP20_b + ' ' + tasasRequeridas});
		    										        	}else if(msgResp.startsWith("Error_29_", 0)){
		    										        		var data_resp = msgResp.substring(9,msgResp.length);
		    										        		var r1 = data_resp.split("_:_");
		    										        		var receiptTaxCode = r1[0];
		    										        		var tasasRequeridas = r1[1];
		    										        		var transTaxValue = r1[2];
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP21_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP21_b + ' ' + tasasRequeridas+ ' ' + SuppAppMsg.msgErrorP21_c + ' ' + transTaxValue});
		    										        	}else if(msgResp.startsWith("Error_30_", 0)){
		    										        		var data_resp = msgResp.substring(9,msgResp.length);
		    										        		var r1 = data_resp.split("_:_");
		    										        		var receiptTaxCode = r1[0];
		    										        		var tasasRequeridas = r1[1];
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP22_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP22_b + ' ' + tasasRequeridas});
		    										        	}else if(msgResp.startsWith("Error_31_", 0)){
		    										        		var data_resp = msgResp.substring(9,msgResp.length);
		    										        		var r1 = data_resp.split("_:_");
		    										        		var receiptTaxCode = r1[0];
		    										        		var tasasRequeridas = r1[1];
		    										        		var transTaxValue = r1[2];
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP23_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP22_b + ' ' + tasasRequeridas+ ' ' + SuppAppMsg.msgErrorP21_c + ' ' + transTaxValue});
		    										        	}else if(msgResp.startsWith("Error_32_", 0)){
		    										        		var dataResp = msgResp.substring(9,msgResp.length);
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP24 + ' ' + dataResp});
		    										        	}else if(msgResp.startsWith("Error_33_", 0)){
		    										        		var dataResp = msgResp.substring(9,msgResp.length);
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP25 + ' ' + dataResp});
		    										        	}else if(msgResp.startsWith("Error_34_", 0)){
		    										        		var data_resp = msgResp.substring(9,msgResp.length);
		    										        		var r1 = data_resp.split("_:_");
		    										        		var receiptTaxCode = r1[0];
		    										        		var tasasRequeridasT = r1[1];
		    										        		var tasasRequeridasR = r1[2];
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP26_a + ' ' + tasasRequeridasT + ' ' + SuppAppMsg.msgErrorP26_b + ' ' + tasasRequeridasR});
		    										        	}else if(msgResp == "Error_35"){
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP27});
		    										        	}else{
		    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  msgResp});
		    										        	}
		    										        }
		    											});
		    								}
		    							}
		    						} ]
		    					});
		
		    	this.winLoadInv = new Ext.Window({
		    		layout : 'fit',
		    		title : SuppAppMsg.purchaseUploadInvoice,
		    		width : 600,
		    		height : 200,
		    		modal : true,
		    		closeAction : 'destroy',
		    		resizable : false,
		    		minimizable : false,
		    		maximizable : false,
		    		plain : true,
		    		items : [ filePanel ]
		
		    	});
		    	this.winLoadInv.show();
	
    }else{
    		var me = this;
        	
	    		this.winLoadInv = new Ext.Window({
		    		layout : 'fit',
		    		title : SuppAppMsg.purchaseUploadInvoiceForeing,
		    		width : 740,
		    		height : 680,
		    		modal : true,
		    		closeAction : 'destroy',
		    		resizable : false,
		    		minimizable : false,
		    		maximizable : false,
		    		plain : true,
		    		items : [ 
		    				{
		    				xtype:'foreignOrderForm'
		    				} 
		    			]
		    	});
	    		
	        	var foreignForm = this.getForeignOrderForm().getForm();
	        	foreignForm.setValues({
	        		addressNumber: values.addressNumber,
	        		orderNumber: values.orderNumber,
	        		orderType: values.orderType,
	        		name: values.supplierName,
	        		taxId: values.supplierTaxId,
	        		address: values.supplierAddress,
	        		country: values.supplierCountry
	        	});
		    	foreignForm.findField('usuarioImpuestos').setVisible(true);
	        	
	        	setTimeout(function(){

	           },2000); //delay is in milliseconds 

        		me.winLoadInv.show();
    	}

    	
    },
    
    uploadAdditional : function(grid, record) {
    	
        var me = this;
        
        var grid = this.getPurchaseOrderGrid();
    	var store = grid.getStore();
    	
    	var filePanel = Ext.create(
    					'Ext.form.Panel',
    					{
    						//width : 900,
    						bodyPadding: 10,
    				        layout: 'anchor',
    				        defaults: {
    				            anchor: '100%',
    				            margin: '20 0 30 0'
    				        },
    						items : [
    								{
    									xtype : 'textfield',
    									name : 'documentNumber',
    									hidden : true,
    									value : record.data.orderNumber
    								},{
    									xtype : 'textfield',
    									name : 'documentType',
    									hidden : true,
    									value : record.data.orderType
    								},{
    									xtype : 'textfield',
    									name : 'addressBook',
    									hidden : true,
    									value : userName
    								},{
		                                xtype: 'textfield',
		                                name: 'tipoComprobante',
    									hidden : true,
		                                value: 'Otros'
		                            },{
    									xtype : 'filefield',
    									name : 'file',
    									fieldLabel : SuppAppMsg.purchaseFilePDF,
    									//labelWidth : 80,
    									msgTarget : 'side',
    									allowBlank : false,
    									//margin:'20 0 30 0',
    									//anchor : '90%',
    									buttonText : SuppAppMsg.suppliersSearch,
    									buttonConfig: {
    										cls: 'buttonStyle'
    								    }
    								} ],

    						buttons : [ {
    							text : SuppAppMsg.supplierLoad,
    							margin:'10 0 0 0',
    							cls: 'buttonStyle',
    							handler : function() {
    								var form = this.up('form').getForm();
    								if (form.isValid()) {
    									form.submit({
    												url : 'uploadInvoiceFromOrder.action',
    												waitMsg : SuppAppMsg.supplierLoadFile,
    												success : function(fp, o) {
    													var res = Ext.decode(o.response.responseText);
    													Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
    													me.winLoadInv.close();
    													store.load();
    												},       // If you don't pass success:true, it will always go here
    										        failure: function(fp, o) {
    										        	var res = o.response.responseText;
    										        	var result = Ext.decode(res);
    										        	var msgResp = result.message
    										        	
    										        	if(msgResp == "Error_1"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError1});
    										        	}else if(msgResp == "Error_2"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError2});
    										        	}else if(msgResp == "Error_3"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError3});
    										        	}else if(msgResp == "Error_4"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError4});
    										        	}else if(msgResp == "Error_5"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError5});
    										        	}else if(msgResp == "Error_6"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError6});
    										        	}else if(msgResp == "Error_7"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError7});
    										        	}else if(msgResp == "Error_8"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO1});
										        	    }else if(msgResp == "Error_9"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO2});
    										        	}else if(msgResp == "Error_10"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO3});
    										        	}else if(msgResp == "Error_11"){
										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError6});
										        	    }else if(msgResp == "Error_12"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO4});
    										        	}else if(msgResp == "Error_13"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO5});
    										        	}else if(msgResp == "Error_14"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO6});
    										        	}else if(msgResp == "Error_15"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO7});
    										        	}else if(msgResp == "Error_16"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO8});
										        	    }else if(msgResp == "Error_17"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorPO9});
    										        	}else if(msgResp == "Error_18"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP10});
    										        	}else if(msgResp == "Error_19"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP11});
										        	    }else if(msgResp.startsWith("Error_20_", 0)){
    										        		var dataResp = msgResp.substring(9,msgResp.length);
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP12 + ' ' + dataResp});
    										        	}else if(msgResp.startsWith("Error_21_", 0)){
    										        		var dataResp = msgResp.substring(9,msgResp.length);
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP13_a + ' ' + dataResp + ' ' + SuppAppMsg.msgErrorP13_b});
    										        	}else if(msgResp.startsWith("Error_22_", 0)){
    										        		var data_resp = msgResp.substring(9,msgResp.length);
    										        		var r1 = data_resp.split("_:_");
    										        		var invCurrency = r1[0];
    										        		var oCurr = r1[1];
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP13_a + ' ' + invCurrency + ' ' + SuppAppMsg.msgErrorP14 + ' ' + oCurr});
    										        	}else if(msgResp.startsWith("Error_23_", 0)){
    										        		var dataResp = msgResp.substring(9,msgResp.length);
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP15 + ' ' + dataResp});
    										        	}else if(msgResp.startsWith("Error_24_", 0)){
    										        		var dataResp = msgResp.substring(9,msgResp.length);
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP16_a + ' ' + dataResp + SuppAppMsg.msgErrorP16_b});
    										        	}else if(msgResp.startsWith("Error_25_", 0)){
    										        		var dataResp = msgResp.substring(9,msgResp.length);
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP17 + ' ' + dataResp + SuppAppMsg.msgErrorP16_b});
    										        	}else if(msgResp.startsWith("Error_26_", 0)){
    										        		var dataResp = msgResp.substring(9,msgResp.length);
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP18 + ' ' + dataResp});
    										        	}else if(msgResp.startsWith("Error_27_", 0)){
    										        		var dataResp = msgResp.substring(9,msgResp.length);
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP19_a + ' ' + dataResp + ' ' + SuppAppMsg.msgErrorP19_b});
    										        	}else if(msgResp.startsWith("Error_28_", 0)){
    										        		var data_resp = msgResp.substring(9,msgResp.length);
    										        		var r1 = data_resp.split("_:_");
    										        		var receiptTaxCode = r1[0];
    										        		var tasasRequeridas = r1[1];
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP20_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP20_b + ' ' + tasasRequeridas});
    										        	}else if(msgResp.startsWith("Error_29_", 0)){
    										        		var data_resp = msgResp.substring(9,msgResp.length);
    										        		var r1 = data_resp.split("_:_");
    										        		var receiptTaxCode = r1[0];
    										        		var tasasRequeridas = r1[1];
    										        		var transTaxValue = r1[2];
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP21_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP21_b + ' ' + tasasRequeridas+ ' ' + SuppAppMsg.msgErrorP21_c + ' ' + transTaxValue});
    										        	}else if(msgResp.startsWith("Error_30_", 0)){
    										        		var data_resp = msgResp.substring(9,msgResp.length);
    										        		var r1 = data_resp.split("_:_");
    										        		var receiptTaxCode = r1[0];
    										        		var tasasRequeridas = r1[1];
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP22_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP22_b + ' ' + tasasRequeridas});
    										        	}else if(msgResp.startsWith("Error_31_", 0)){
    										        		var data_resp = msgResp.substring(9,msgResp.length);
    										        		var r1 = data_resp.split("_:_");
    										        		var receiptTaxCode = r1[0];
    										        		var tasasRequeridas = r1[1];
    										        		var transTaxValue = r1[2];
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP23_a + ' ' + receiptTaxCode + ' ' + SuppAppMsg.msgErrorP22_b + ' ' + tasasRequeridas+ ' ' + SuppAppMsg.msgErrorP21_c + ' ' + transTaxValue});
    										        	}else if(msgResp.startsWith("Error_32_", 0)){
    										        		var dataResp = msgResp.substring(9,msgResp.length);
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP24 + ' ' + dataResp});
    										        	}else if(msgResp.startsWith("Error_33_", 0)){
    										        		var dataResp = msgResp.substring(9,msgResp.length);
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP25 + ' ' + dataResp});
    										        	}else if(msgResp.startsWith("Error_34_", 0)){
    										        		var data_resp = msgResp.substring(9,msgResp.length);
    										        		var r1 = data_resp.split("_:_");
    										        		var receiptTaxCode = r1[0];
    										        		var tasasRequeridasT = r1[1];
    										        		var tasasRequeridasR = r1[2];
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP26_a + ' ' + tasasRequeridasT + ' ' + SuppAppMsg.msgErrorP26_b + ' ' + tasasRequeridasR});
    										        	}else if(msgResp == "Error_35"){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.msgErrorP27});
    										        	}else{
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  msgResp});
    										        	}
    										        }
    											});
    								}
    							}
    						} ]
    					});

    	this.winLoadInv = new Ext.Window({
    		layout : 'fit',
    		title : SuppAppMsg.purchaseUploadDocumentsAditional,
    		//width : 600,
    		//height : 150,
    		width: Ext.Element.getViewportWidth() * 0.35,   //  40% de pantalla
            maxWidth: 520,                                //  ancho mínimo
            height: Ext.Element.getViewportHeight() * 0.35, //  40% de alto
            maxHeight: 150,
    		modal : true,
    		closeAction : 'destroy',
    		resizable : false,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		items : [ filePanel ]

    	});
    	this.winLoadInv.show();
    },
    
    loadComplFile : function(grid, record) {
    	
        var me = this;    	
    	var orderArray = [];
    	var invoiceArray = [];
    	var gridAccept = this.getAcceptInvGrid();
    	var storeAccept = gridAccept.getStore();
    	
    	var gridSelect = this.getSelInvGrid();
    	var storeSelect = gridSelect.getStore();
    	
    	var supNumber = Ext.getCmp('supNumber').getValue() == ''?'':Ext.getCmp('supNumber').getValue();
		storeAccept.each(function(rec) {
			if(rec){
		       orderArray.push(rec.data.id);
		       invoiceArray.push(rec.data.uuid);
			}
		});
		
		if(orderArray.length > 0){
			var filePanel = Ext.create(
					'Ext.form.Panel',
					{
						//width : 900,
						items : [
								{
									xtype : 'textfield',
									name : 'addressBook',
									hidden : true,
									value : supNumber
			                    },{
									xtype : 'textfield',
									name : 'orders',
									hidden : true,
									value : orderArray
			                    },{
									xtype : 'textfield',
									name : 'invoices',
									hidden : true,
									value : invoiceArray
			                    },{
									xtype : 'filefield',
									name : 'file',
									fieldLabel : SuppAppMsg.purchaseFileXML +':',
									labelWidth : 80,
									msgTarget : 'side',
									allowBlank : false,
									margin:'30 0 10 20',
									anchor : '90%',
									buttonText : SuppAppMsg.suppliersSearch
								},{
									xtype : 'filefield',
									name : 'fileTwo',
									fieldLabel : SuppAppMsg.purchaseFilePDF,
									labelWidth : 80,
									msgTarget : 'side',
									allowBlank : false,
									margin:'20 0 70 20',
									anchor : '90%',
									buttonText : SuppAppMsg.suppliersSearch
								} ],

						buttons : [ {
							text : SuppAppMsg.supplierLoad,
							margin:'10 0 0 0',
							handler : function() {
								var form = this.up('form').getForm();
								if (form.isValid()) {
									form.submit({
												url : 'uploadComplPago.action',
												waitMsg : SuppAppMsg.supplierLoadFile,
												success : function(fp, o) {
													Ext.MessageBox.alert({ 
														//maxWidth: 700, minWidth: 650, 
										        		width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
											            maxWidth: 420,                                // 👈 ancho mínimo
											            height: Ext.Element.getViewportHeight() * 0.35, // 👈 40% de alto
											            maxHeight: 150,
														title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
													storeAccept.loadData([],false);
													storeSelect.load();
												},       // If you don't pass success:true, it will always go here
										        failure: function(fp, o) {
										        	var res = o.response.responseText;
										        	var result = Ext.decode(res);
										        	Ext.MessageBox.alert({ 
										        		//maxWidth: 700, minWidth: 650, 
										        		width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
											            maxWidth: 420,                                // 👈 ancho mínimo
											            height: Ext.Element.getViewportHeight() * 0.35, // 👈 40% de alto
											            maxHeight: 150,
										        		title: SuppAppMsg.supplierMsgValidationLoad, msg:  result.message});
										        }
											});
								}
							}
						} ]
					});

					this.winLoadInv = new Ext.Window({
						layout : 'fit',
						title : SuppAppMsg.purchaseOrdersTitle3,
						//width : 600,
						//height : 200,
		        		width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
			            maxWidth: 420,                                // 👈 ancho mínimo
			            height: Ext.Element.getViewportHeight() * 0.35, // 👈 40% de alto
			            maxHeight: 150,
						modal : true,
						closeAction : 'destroy',
						resizable : false,
						minimizable : false,
						maximizable : false,
						plain : true,
						items : [ filePanel ]
				
					});
					this.winLoadInv.show();
		}else{
			Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.purchaseOrdersMsg6 });
		}
    	
    },
    loadComplFileMassive : function(grid, record) {
    	
        var me = this;    	
    	var orderArray = [];
    	var invoiceArray = [];
    	var gridAccept = this.getAcceptInvGrid();
    	var storeAccept = gridAccept.getStore();
    	
    	var gridSelect = this.getSelInvGrid();
    	var storeSelect = gridSelect.getStore();
    	
    	var supNumber = Ext.getCmp('supNumber').getValue() == ''?'':Ext.getCmp('supNumber').getValue();
    	storeSelect.each(function(rec) {
			if(rec.data.selected){
		       orderArray.push(rec.data.id);
		       invoiceArray.push(rec.data.uuid);
			}
		});
		
		if(orderArray.length > 0){
			var filePanel = Ext.create(
					'Ext.form.Panel',
					{
						//width : 900,
						items : [
								{
									xtype : 'textfield',
									name : 'addressBook',
									hidden : true,
									value : supNumber
			                    },{
									xtype : 'textfield',
									name : 'orders',
									hidden : true,
									value : orderArray
			                    },{
									xtype : 'textfield',
									name : 'invoices',
									hidden : true,
									value : invoiceArray
			                    },{
									xtype : 'filefield',
									name : 'file',
									fieldLabel : SuppAppMsg.purchaseFileZip +':',
									labelWidth : 80,
									msgTarget : 'side',
									allowBlank : false,
									margin:'30 0 10 20',
									anchor : '90%',
									buttonText : SuppAppMsg.suppliersSearch,
									listeners: {
								        change: function(field, value) {
								            var allowedExtensions = /(\.zip)$/i;
								            var fileSizeLimit = 25 * 1024 * 1024; // 25 MB
								            var file = field.fileInputEl.dom.files[0];

								            if (!allowedExtensions.test(value)) {
								                Ext.Msg.alert('Error', 'Solo se permiten archivos zip.');
								                field.reset();
								                return;
								            }

								            if (file && file.size > fileSizeLimit) {
								                Ext.Msg.alert('Error', 'El archivo debe ser menor de 25 MB.');
								                field.reset();
								                return;
								            }
								        }
								    }
								},{
									xtype : 'filefield',
									name : 'fileTwo',
									fieldLabel : SuppAppMsg.purchaseFilePDF,
									labelWidth : 80,
									msgTarget : 'side',
									hidden : true,
									margin:'20 0 70 20',
									anchor : '90%',
									buttonText : SuppAppMsg.suppliersSearch
								} ],

						buttons : [ {
							text : SuppAppMsg.supplierLoad,
							margin:'10 0 0 0',
							handler : function() {
								var form = this.up('form').getForm();
								if (form.isValid()) {
									form.submit({
												url : 'uploadComplPagoMassive.action',
												waitMsg : SuppAppMsg.supplierLoadFile,
												success : function(fp, o) {
													
													// Crear una tabla HTML con los datos del arreglo y aplicar estilos de "striped"
													var tableHtml = '<table style="font-size: small; width: 1000px; border-collapse: collapse; border: 1px solid #ccc;">'; // Aplica el estilo para reducir el tamaño de letra y colapsar bordes
													tableHtml += '<tr><th style="background-color: #f2f2f2; border: 1px solid #ccc;">Archivo</th><th style="background-color: #f2f2f2; border: 1px solid #ccc;">Mensaje</th></tr>'; // Establece el color de fondo para las cabeceras y añade borde
													for (var i = 0; i < o.result.detail.length; i++) {
													    var item = o.result.detail[i];
													    var bgColor = i % 2 === 0 ? '#ffffff' : '#ccc'; // Alterna los colores de fondo de las filas
													    tableHtml += '<tr style="background-color: ' + bgColor + '; border: 1px solid #ccc;">'; // Aplica el color de fondo alternativo a cada fila y añade borde
													    tableHtml += '<td style="border: 1px solid #ccc;padding: 8px;">' + item.Archivo + '</td><td style="border: 1px solid #ccc;padding: 8px;">' + item.message + '</td>'; // Aplica el borde a cada celda
													    tableHtml += '</tr>';
													}
													tableHtml += '</table>';

													// Crear un div contenedor para la tabla con scroll y estilos adicionales
													var containerHtml = '<div style="max-height: 360px; overflow-y: auto; background-color: white; border: 1px solid #ccc; padding: 10px;">' + tableHtml + '</div>';

													// Mostrar la tabla dentro del mensaje del Ext.MessageBox.alert
													Ext.MessageBox.alert({
													    //maxWidth: 1150,
													    //minWidth: 1150,
													    //minHeight: 460,
													    //maxHeight: 460,
													    width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
											            maxWidth: 420,                                // 👈 ancho mínimo
											            height: Ext.Element.getViewportHeight() * 0.35, // 👈 40% de alto
											            maxHeight: 150,
													    title: SuppAppMsg.supplierMsgValidationLoad,
													    msg: 'Archivo procesado:<br><br>' + containerHtml
													});

													
//													Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
													storeAccept.loadData([],false);
													storeSelect.load();
												},       // If you don't pass success:true, it will always go here
										        failure: function(fp, o) {
										        	var res = o.response.responseText;
										        	var result = Ext.decode(res);
										        	Ext.MessageBox.alert({ 
										        		//maxWidth: 700, minWidth: 650,
										        		width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
											            maxWidth: 420,                                // 👈 ancho mínimo
											            height: Ext.Element.getViewportHeight() * 0.35, // 👈 40% de alto
											            maxHeight: 150,
										        		title: SuppAppMsg.supplierMsgValidationLoad, msg:  result.message});
										        }
											});
								}
							}
						} ]
					});

					this.winLoadInv = new Ext.Window({
						layout : 'fit',
						title : SuppAppMsg.purchaseOrdersTitle3,
						//width : 600,
						//height : 200,
						width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
			            maxWidth: 420,                                // 👈 ancho mínimo
			            height: Ext.Element.getViewportHeight() * 0.35, // 👈 40% de alto
			            maxHeight: 150,
						modal : true,
						closeAction : 'destroy',
						resizable : false,
						minimizable : false,
						maximizable : false,
						plain : true,
						items : [ filePanel ]
				
					});
					this.winLoadInv.show();
		}else{
			Ext.MessageBox.alert({ 
				//maxWidth: 700, minWidth: 650, 
				width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
	            maxWidth: 420,                                // 👈 ancho mínimo
	            height: Ext.Element.getViewportHeight() * 0.35, // 👈 40% de alto
	            maxHeight: 150,
				title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.purchaseOrdersMsg6 });
		}
    	
    },
    
    invLoadNoPayment : function(grid, rowIndex, colIndex, record) {
    	var orderForm = this.getPurchaseOrderForm().getForm();
        var me = this;
        var grid = this.getPurchaseOrderGrid();
    	var store = grid.getStore();
    	
		var filePanel = Ext.create(
				'Ext.form.Panel',
				{
					width : 900,
					items : [
							{
								xtype : 'textfield',
								name : 'documentNumber',
								hidden : true,
								value : orderForm.findField('orderNumber').getValue()
							},{
								xtype : 'textfield',
								name : 'documentType',
								hidden : true,
								value : orderForm.findField('orderType').getValue()
							},{
								xtype : 'textfield',
								name : 'addressBook',
								hidden : true,
								value : orderForm.findField('addressNumber').getValue()
							},{
								xtype : 'textfield',
								name : 'tipoComprobante',
								hidden : true,
								value : 'Factura'
							},{
								xtype : 'filefield',
								name : 'file',
								fieldLabel : SuppAppMsg.purchaseFileXML +':',
								labelWidth : 120,
								msgTarget : 'side',
								allowBlank : false,
								margin:'20 0 70 0',
								anchor : '90%',
								buttonText : SuppAppMsg.suppliersSearch
							} ],

					buttons : [ {
						text : 'Cargar factura',
						margin:'10 0 0 0',
						handler : function() {
							var form = this.up('form').getForm();
							if (form.isValid()) {
								form.submit({
											url : 'uploadInvoiceFromOrderWithoutPayment.action',
											waitMsg : SuppAppMsg.supplierLoadFile,
											success : function(fp, o) {
												var res = Ext.decode(o.response.responseText);
												me.winLoadInv.close();
												Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
												if(me.orderDetailWindow){
													me.orderDetailWindow.close();
												}
												store.load();
											},       // If you don't pass success:true, it will always go here
									        failure: function(fp, o) {
									        	var res = Ext.decode(o.response.responseText);
									        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
									        }
										});
							}
						}
					} ]
				});
        
    	me.winLoadInv = new Ext.Window({
    		layout : 'fit',
    		title : SuppAppMsg.purchaseOrdersTitle4 ,
    		width : 650,
    		height : 160,
    		modal : true,
    		closeAction : 'destroy',
    		resizable : false,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		items : [ filePanel ]

    	});
        
		Ext.MessageBox.show({
			title : SuppAppMsg.purchaseOrdersTitle5,
			msg : SuppAppMsg.purchaseOrdersMsg7 ,
			buttons : Ext.MessageBox.YESNO,
			width:500,
			buttonText : {
				yes : SuppAppMsg.approvalAcept,
				no : SuppAppMsg.approvalExit
			},
			fn : function(btn, text) {
				if (btn === 'yes') {
		    	    me.winLoadInv.show();
				}
			}
		});
        	
    },
    
    uploadSATInvoiceZip : function(button) {
    	var me = this;
    	var filePanel = Ext.create(
				'Ext.form.Panel',
				{
					width : 900,
					items : [
							{
								xtype : 'filefield',
								name : 'file',
								fieldLabel : SuppAppMsg.purchaseFile + '(.zip)*:',
								labelWidth : 120,
								msgTarget : 'side',
								allowBlank : false,
								margin:'20 0 20 0',
								anchor : '90%',
								multiple: true,
								buttonText : SuppAppMsg.suppliersSearch
							}],

					buttons : [ {
						text : SuppAppMsg.supplierLoad,
						margin:'10 0 0 0',
						handler : function() {
							var form = this.up('form').getForm();
							if (form.isValid()) {
								form.submit({
											url : 'uploadSATInvoiceZip.action',
											waitMsg : SuppAppMsg.supplierLoadFile,
											success : function(fp, o) {    			    													
												var res = Ext.decode(o.response.responseText);
												me.winLoadInv.destroy();
												if(me.receiptWindow){
													me.receiptWindow.close();
												}
												
												Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.supplierLoadDocSuccBatch});    			    													
											},
									        failure: function(fp, o) {
									        	var res = Ext.decode(o.response.responseText);

									        	if(res.message != null && res.message != "") {
									        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: res.message});
									        	} else {
									        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSuccBatchError});
									        	}    			    										        		    			    										        
									        }
										});
							}
						}
					} ]
				});

		me.winLoadInv = new Ext.Window({
			layout : 'fit',
			title : SuppAppMsg.purchaseUploadCreditNotesTitle,
			width : 600,
			height : 150,
			modal : true,
			closeAction : 'destroy',
			resizable : false,
			minimizable : false,
			maximizable : false,
			plain : true,
			items : [ filePanel ]
		
		});
		me.winLoadInv.show();
    },
    
    acceptForeignRecord : function(button) {
    	var form = this.getForeignOrderForm().getForm();
        var me = this;
    	if (form.isValid()) {
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    		var values = form.getFieldValues();
    		var record = Ext.create('SupplierApp.model.ForeingInvoice');
    		var updatedRecord = populateObj(record, values);
    		
    		record.set(updatedRecord);
			record.save(
					{
					    callback: function (records, o, success) { 
					    	if(success){
						    	var res = Ext.decode(o.response.responseText);
						    	box.hide();
						    	if(res.message != ""){
							    	Ext.MessageBox.show({
				    	        	    title: 'Error',
				    	        	    msg: res.message,
				    	        	    buttons: Ext.MessageBox.OK
				    	        	});
						    	}else{
						    		Ext.MessageBox.show({
				    	        	    title: SuppAppMsg.supplierMessage,
				    	        	    msg: SuppAppMsg.purchaseInvoiceAcceptSend,
				    	        	    buttons: Ext.MessageBox.OK
				    	        	});
						    		form.reset();
						    		
						        	var win = Ext.WindowManager.getActive();
						        	if (win) {
						        	    win.close();
						        	}
						    		
					        		Ext.getCmp('uploadFiscalDoc').setVisible(false);
						    	}
					    	}else{
					    		var text = o.request.proxy.getReader().rawData.msg;
						    	box.hide();
						    	Ext.MessageBox.minWidth = 500;
					    		Ext.MessageBox.show({
			    	        	    title: 'Error',
			    	        	    msg: text,
			    	        	    buttons: Ext.MessageBox.OK
			    	        	});
					    	}
					    }
			});

    	}
    },
    
    rejectForeignRecord : function(button) {
    	var form = this.getForeignOrderForm().getForm();
        var me = this;
    	if (form.isValid()) {
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    		var values = form.getFieldValues();
    		var record = Ext.create('SupplierApp.model.ForeingInvoice');
    		var updatedRecord = populateObj(record, values);
    		
    		record.set(updatedRecord);
			record.destroy(
					{
					    callback: function (records, o, success) { 
					    	if(success){
						    	var res = Ext.decode(o.response.responseText);
						    	box.hide();
						    	if(res.message != ""){
							    	Ext.MessageBox.show({
				    	        	    title: 'Error',
				    	        	    msg: res.message,
				    	        	    buttons: Ext.MessageBox.OK
				    	        	});
						    	}else{
						    		Ext.MessageBox.show({
				    	        	    title: SuppAppMsg.supplierMessage,
				    	        	    msg: SuppAppMsg.purchaseInvoiceAcceptSend,
				    	        	    buttons: Ext.MessageBox.OK
				    	        	});
						    		form.reset();
						    		
						        	var win = Ext.WindowManager.getActive();
						        	if (win) {
						        	    win.close();
						        	}
						    		
					        		Ext.getCmp('uploadFiscalDoc').setVisible(false);
						    	}
					    	}else{
					    		var text = o.request.proxy.getReader().rawData.msg;
						    	box.hide();
						    	Ext.MessageBox.minWidth = 500;
					    		Ext.MessageBox.show({
			    	        	    title: 'Error',
			    	        	    msg: text,
			    	        	    buttons: Ext.MessageBox.OK
			    	        	});
					    	}
					    }
			});

    	}
    },
    
    uploadForeignAdditional : function(button) {
       
    	var me = this;
        var form = this.getForeignOrderForm().getForm();
        
    	if (form.isValid()) {
    		var val = form.getFieldValues();
    		var filePanel = Ext.create(
					'Ext.form.Panel',
					{
						width : 900,
						items : [
								{
									xtype : 'textfield',
									name : 'documentNumber',
									hidden : true,
									value : val.orderNumber
								},{
									xtype : 'textfield',
									name : 'documentType',
									hidden : true,
									value : val.orderType
								},{
									xtype : 'textfield',
									name : 'addressBook',
									hidden : true,
									value : val.addressNumber
								},{
									xtype : 'filefield',
									name : 'file',
									fieldLabel : SuppAppMsg.purchaseFile + ':',
									labelWidth : 80,
									msgTarget : 'side',
									allowBlank : false,
									margin:'20 0 30 0',
									anchor : '90%',
									buttonText : SuppAppMsg.suppliersSearch
								} ],

						buttons : [ {
							text : SuppAppMsg.supplierLoad,
							margin:'10 0 0 0',
							handler : function() {
								var form = this.up('form').getForm();
								if (form.isValid()) {
									form.submit({
												url : 'uploadForeignAdditional.action',
												waitMsg : SuppAppMsg.supplierLoadFile,
												success : function(fp, o) {
													var res = Ext.decode(o.response.responseText);
													
													Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.supplierLoadDocSucc});
													Ext.Ajax.request({
														url : 'documents/listDocumentsByOrder.action',
														method : 'GET',
															params : {
																start : 0,
																limit : 15,
																orderNumber : val.orderNumber,
																orderType : val.orderType,
																addressNumber : val.addressNumber
															},
															success : function(response,opts) {
																
																response = Ext.decode(response.responseText);
																var index = 0;
																var files = "";
																var accepted ="ACEPTADO";
																for (index = 0; index < response.data.length; index++) {
																	
																	var dateObj = new Date(response.data[index].uploadDate);

																	var formattedDate = dateObj.toLocaleString('es-ES', {
																	    day: '2-digit',
																	    month: '2-digit',
																	    year: 'numeric',
																	    hour: '2-digit',
																	    minute: '2-digit',
																	    second: '2-digit'
																	});
																	
																		var href = "documents/openDocument.action?id=" + response.data[index].id;
																		var fileHref = "<a href= '" + href + "' target='_blank'>" +  response.data[index].name + "</a>";
											                            files = files + "> " + fileHref + " - " + response.data[index].size + " bytes : " + response.data[index].fiscalType + accepted + "-fecha "+formattedDate + "<br />";
																} 
																Ext.getCmp('fileListForeignHtml').setValue(files);
																Ext.getCmp('attachmentFlag').setValue("ATTACH");
																
															},
															failure : function() {
															}
														});
													
											    	var win = Ext.WindowManager.getActive();
											    	if (win) {
											    	    win.close();
											    	}
													

												},   
										        failure: function(fp, o) {
										        	var res = o.response.responseText;
										        	var result = Ext.decode(res);
										        	var msgResp = result.message
										        	
										        	if(msgResp == "Error_1"){
										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError1});
										        	}else if(msgResp == "Error_2"){
										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError2});
										        	}else if(msgResp == "Error_3"){
										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError3});
										        	}else if(msgResp == "Error_4"){
										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError4});
										        	}else if(msgResp == "Error_5"){
										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError5});
										        	}else if(msgResp == "Error_6"){
										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError6});
										        	}else if(msgResp == "Error_7"){
										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.fileUploadError7});
										        	}else{
										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  msgResp});
										        	}
										        	
										        }
											});
								}
							}
						} ]
					});

						this.winLoadInv = new Ext.Window({
							layout : 'fit',
							title : SuppAppMsg.purchaseUploadDocumentsAditional,
							width : 600,
							height : 150,
							modal : true,
							closeAction : 'destroy',
							resizable : false,
							minimizable : false,
							maximizable : false,
							plain : true,
							items : [ filePanel ]
					
						});
						this.winLoadInv.show();
    	}
        
    	
    	
    },
    
    sendForeignRecord : function(button) {
    	
    	var form = this.getForeignOrderForm().getForm();
    	var attach = Ext.getCmp('attachmentFlag').getValue();
    	
    	
    	if(attach == ''){
    		Ext.MessageBox.show({
        	    title: "ERROR",
        	    msg: SuppAppMsg.purchaseOrdersMsg9,
        	    buttons: Ext.MessageBox.OK
        	});
    		return false;
    	}

        var me = this;
    	if (form.isValid()) {
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    		var values = form.getFieldValues();
    		var record = Ext.create('SupplierApp.model.ForeingInvoice');
    		var updatedRecord = populateObj(record, values);

    		record.set(updatedRecord);
			record.save({
			    callback: function (records, o, success, msg) { 
			    	if(success == true){
			    		
			    		var r1 = Ext.decode(o._response.responseText);
				    	var res = Ext.decode(r1);
				    	box.hide();

				    	if(res.message != ""){
				        	var msgResp = res.message
				        	
				        	if(msgResp == "ForeignInvoiceError_1"){
				        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError1});
				        	}else if(msgResp == "ForeignInvoiceError_2"){
				        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError2});
				        	}else if(msgResp == "ForeignInvoiceError_3"){
				        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError3});
				        	}else if(msgResp == "ForeignInvoiceError_4"){
				        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError4});
				        	}else if(msgResp == "ForeignInvoiceError_5"){
				        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError5});
				        	}else if(msgResp == "ForeignInvoiceError_6"){
				        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError6});
				        	}else if(msgResp == "ForeignInvoiceError_7"){
				        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError7});
				        	}else if(msgResp == "ForeignInvoiceError_8"){
				        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError8});
				        	}else{
				        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  msgResp});
				        	}
					    	return false;
				    	}else{
				    		form.reset();
					    	if(me.winLoadInv){
					    		me.winLoadInv.close();
					    	}
				    		Ext.MessageBox.show({
		    	        	    title: SuppAppMsg.supplierMessage,
		    	        	    msg: SuppAppMsg.purchaseInvoiceSendSucc ,
		    	        	    buttons: Ext.MessageBox.OK
		    	        	});
			        		Ext.getCmp('uploadFiscalDoc').setVisible(false);
			        		Ext.getCmp('openForeignDoc').setVisible(false);
				    		return true;
				    	}
			    	}else{
			    		
			    	}
			    	
			    }
			});

    	}
    },
    
    openForeignDoc : function(button) {

    	var orderForm = this.getPurchaseOrderForm().getForm();
        var me = this;
        var grid = this.getPurchaseOrderGrid();
    	var store = grid.getStore();
    	var values = orderForm.getFieldValues();
    														   
    	Ext.Ajax.request({
			url : 'supplier/orders/getForeignInvoice.action',
			method : 'GET',
				params : {
					addressBook: values.addressNumber,
					orderNumber: values.orderNumber,
					orderType: values.orderType
				},
				success : function(response,opts) {
					var resp = Ext.decode(response.responseText);
					me.winLoadInv = new Ext.Window({
			    		layout : 'fit',
			    		title : SuppAppMsg.purchaseUploadInvoiceForeing,
			    		width : 740,
			    		height : 680,
			    		modal : true,
			    		closeAction : 'destroy',
			    		resizable : false,
			    		minimizable : false,
			    		maximizable : false,
			    		plain : true,
			    		items : [ 
			    				{
			    				xtype:'foreignOrderForm'
			    				} 
			    			]
			    	});
					

			    	var foreignForm = me.getForeignOrderForm().getForm();

			    	Ext.getCmp('foreignCurrencyCombo').getStore().load();
			    	Ext.getCmp('receptCompanyCombo').getStore().load();
			    	
			    	var record = Ext.create('SupplierApp.model.ForeingInvoice', resp.data);
			    	foreignForm.loadRecord(record);
			    	
			    	foreignForm.findField('name').setValue(values.supplierName);
			    	foreignForm.findField('address').setValue(values.supplierAddress);
			    	foreignForm.findField('taxId').setValue(values.supplierTaxId);
			    	
			    	Ext.getCmp('foreignCurrencyCombo').setRawValue(record.foreignCurrency);
			    	Ext.getCmp('receptCompanyCombo').setRawValue(record.receptCompany);
			    	
			    	foreignForm.findField('usuarioImpuestos').setVisible(false);
			    	
			    	foreignForm.applyToFields({readOnly:true});
			    	
			    	foreignForm.findField('foreignNotes').setReadOnly(false);
			    	foreignForm.findField('foreignRetention').setReadOnly(false);
	
					me.winLoadInv.show();
				},
				failure : function() {
				}
			});
    },
    
    getInvoiceCodes : function(button) {

    	var orderForm = this.getPurchaseOrderForm().getForm();
        var me = this;
        var grid = this.getPurchaseOrderGrid();
    	var store = grid.getStore();
    	var values = orderForm.getFieldValues();
		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    					
    	Ext.Ajax.request({
			url : 'orders/invoice/getInvoiceCodes.action',
			method : 'GET',
				params : {
					addressBook: values.addressNumber,
					orderNumber: values.orderNumber,
					orderType: values.orderType
				},
				success : function(response,opts) {
					var resp = Ext.decode(response.responseText);
					var localStore = Ext.create('Ext.data.Store', {                
						 model: 'SupplierApp.model.InvoiceCodesDTO',
                        data : resp.data
                    })
					me.winLoadInv = new Ext.Window({
			    		layout : 'fit',
			    		title : SuppAppMsg.purchaseInvoiceConcepts ,
			    		width : 910,
			    		height : 500,
			    		modal : true,
			    		closeAction : 'destroy',
			    		resizable : false,
			    		minimizable : false,
			    		maximizable : false,
			    		plain : true,
			    		items : [{
	                        xtype : 'grid',
	                        width : 905,
	                        columns : [{
	                            text : SuppAppMsg.purchaseSATKey,
	                            dataIndex : 'code',
	                            width:100,
	                        },
	                        {
	                            text : SuppAppMsg.purchaseSATDescription,
	                            dataIndex : 'descriptionSAT',
	                            width:300
	                        },
	                        {
	                            text : SuppAppMsg.purchaseInvoiceConcept,
	                            dataIndex : 'description',
	                            width:600
	                        }],
	                        store : localStore
	                    }]
			    	});
						
					box.hide();
					me.winLoadInv.show();
				},
				failure : function() {
				box.hide();
				}
			});
    },
    
    poReasignPurchases: function(button) {
    	new Ext.Window({
  		  width        : 1015,
  		  height       : 455,
  		  title        : SuppAppMsg.purchaseReasingPurchases,
  		  border       : false,
	      modal : true,
	      closeAction : 'destroy',
	      resizable : false,
	      minimizable : false,
	      maximizable : false,
	      plain : true,
  		  items : [
  			  {
  	    			xtype : 'changeBuyerGrid',
  	    			height       : 430
  	    		}
  			  ]
  		}).show();
	 
    	
	},
	
    fromBuyerSearch: function(button) {
    	var buyerEmail = Ext.getCmp('fromBuyerCombo').getRawValue();
    	var grid = this.getChangeBuyerGrid();
    	var store = grid.getStore();
		store.proxy.extraParams = { 
				buyerEmail :buyerEmail
		        }
		store.load();
    },
    
    toBuyerExecute: function(button) {
    	var buyerEmail = Ext.getCmp('toBuyerCombo').getRawValue();
    	var grid = this.getChangeBuyerGrid();
    	var s = grid.getSelectionModel().getSelection();
    	var store = grid.getStore();
    	var selected = [];
    	Ext.each(s, function (item) {
    	  item.data.email = buyerEmail;	
    	  selected.push(item.data);
    	});
    	
    	if(selected.length > 0 && buyerEmail != ""){
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
	    	Ext.Ajax.request({
				url : 'supplier/orders/reasignOrders.action',
				method : 'POST',
				jsonData: selected,
				success : function(response,opts) {
					box.hide();
					var resp = Ext.decode(response.responseText);
					store.load();
					Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.approvalResponse, msg:  resp.message});
				},
				failure : function() {
					box.hide();
					Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: SuppAppMsg.purchaseModifyOrdersError });
				}
			});
    	}
    	
    },
    
    poPaymentCalendar: function(button) {
        
        var calendarPanel = Ext.create('Ext.form.Panel', {
            layout: 'fit',
            flex: 1,
            width: '100%',
            items: [{
                xtype: 'paymentCalendarGrid'
            }]
        });

        var grid = this.getPaymentCalendarGrid();
        var store = grid.getStore();
        var view = grid.getView();

        new Ext.Window({
            /*width: Ext.Element.getViewportWidth() * 0.40,
            maxWidth: 385,
            height: Ext.Element.getViewportHeight() * 0.45,
            maxHeight: 500,*/
        	width: 385,
        	height: 500,
            title: SuppAppMsg.purchasePaymentCalendar,
            border: false,
            modal: true,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            scrollable: false,
            closeAction: 'destroy',
            resizable: true,
            minimizable: false,
            maximizable: false,
            plain: true,
            items: [
                {
                    xtype: 'container',
                    layout: 'hbox',
                    padding: 5,
                    items: [
                        {
                            xtype:'button',
                            text: SuppAppMsg.purchaseLoadCalendar,
                            iconCls: 'icon-doSearch',
                            margin:'0 10 0 0',
                            cls: 'buttonStyle',
                            handler: function() {
                                var filePanel = Ext.create('Ext.form.Panel', {
                                    bodyPadding: 10,
                                    layout: 'anchor',
                                    defaults: {
                                        anchor: '100%',
                                        margin: '20 0 20 0'
                                    },
                                    items : [{
                                        xtype : 'filefield',
                                        name : 'file',
                                        fieldLabel : SuppAppMsg.purchaseFile + '(.xls, .xlsx)*:',
                                        msgTarget : 'side',
                                        allowBlank : false,
                                        multiple: true,
                                        buttonText : SuppAppMsg.suppliersSearch,
                                        buttonConfig: {
    										cls: 'buttonStyle'
    								    }
                                    }],
                                    buttons : [{
                                        text : SuppAppMsg.supplierLoad,
                                        margin:'10 0 0 0',
                                        cls: 'buttonStyle',
                                        handler : function() {
                                            var form = this.up('form').getForm();
                                            if (form.isValid()) {
                                                form.submit({
                                                    url : 'paymentCalendar/uploadCalendar.action',
                                                    waitMsg : SuppAppMsg.supplierLoadFile,
                                                    success : function(fp, o) {
                                                        Ext.MessageBox.alert({
                                                            //maxWidth: 700,
                                                            //minWidth: 650,
                                                            width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
                                                            maxWidth: 520,                                // 👈 ancho mínimo
                                                            height: Ext.Element.getViewportHeight() * 0.35, // 👈 40% de alto
                                                            maxHeight: 100,
                                                            title: SuppAppMsg.supplierMsgValidationLoad,
                                                            msg:  SuppAppMsg.supplierLoadDocSucc
                                                        });
                                                        store.load();
                                                    },
                                                    failure: function(fp, o) {
                                                        var result = Ext.decode(o.response.responseText);
                                                        Ext.MessageBox.alert({
                                                            //maxWidth: 700,
                                                            //minWidth: 650,
                                                        	width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
                                                            maxWidth: 520,                                // 👈 ancho mínimo
                                                            height: Ext.Element.getViewportHeight() * 0.35, // 👈 40% de alto
                                                            maxHeight: 100,
                                                            title: SuppAppMsg.supplierMsgValidationLoad,
                                                            msg:  result.message
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    }]
                                });

                                var winLoadFileInv = new Ext.Window({
                                    layout : 'fit',
                                    title : SuppAppMsg.purchaseLoadPaymentCalendar,
                                    width: Ext.Element.getViewportWidth() * 0.35,
                                    maxWidth: 500,
                                    height: Ext.Element.getViewportHeight() * 0.45,
                                    maxHeight: 160,
                                    modal : true,
                                    closeAction : 'destroy',
                                    resizable : false,
                                    minimizable : false,
                                    maximizable : false,
                                    plain : true,
                                    items : [ filePanel ]
                                });
                                winLoadFileInv.show();
                            }
                        },
                        {
                            xtype:'button',
                            text: SuppAppMsg.purchaseDeleteInputs,
                            iconCls: 'icon-delete',
                            margin:'0 0 0 10',
                            cls: 'buttonStyle',
                            handler: function() {
                                Ext.Msg.prompt(SuppAppMsg.purchaseDataRequest, SuppAppMsg.purchaseSpecifyYear , function(btnText, sInput){
                                    if(btnText === 'ok'){
                                        var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
                                        Ext.Ajax.request({
                                            url : 'paymentCalendar/deleteCalendar.action',
                                            method : 'POST',
                                            params : { year: sInput },
                                            success : function(response,opts) {
                                                store.currentPage = 1;
                                                store.load();
                                                box.hide();
                                            },
                                            failure : function() {
                                                box.hide();
                                            }
                                        });
                                    }
                                }, this);
                            }
                        }
                    ]
                },
                calendarPanel
            ]
        }).show();
    },

  	
  	refreshLog: function(button) {
    	
    	var grid = this.getLogDataGrid();
    	var store = grid.getStore();
    	store.loadData([], false);
    	grid.getView().refresh();
    	
    	var fromDate = Ext.getCmp('fromDateGrid');
    	var toDate = Ext.getCmp('toDateGrid');
    	var logType = Ext.getCmp('logType').getValue();
    	if(fromDate != '' & toDate != ''){
	    	store.proxy.extraParams = { 
	    			fromDate : fromDate?fromDate.getValue():"",
	    			toDate : toDate?toDate.getValue():"",
	    			logType: logType?logType:""
			        }
	    	store.load();
    	}
    },
  	
  	exportExcelLog: function(button) {
    	
    	var grid = this.getLogDataGrid();
    	var store = grid.getStore();
    	var itemArray = [];
    	var rowArray = [];

    	store.each(function(rec) {
			if(rec){
		       var recData = [formatDate(rec.data.date), rec.data.logType,rec.data.mesage];
		       rowArray.push(recData);
			}
		});
    	

    	data = rowArray;
    	if(data.length > 0) {
        	var csvContent = '';
        	data.forEach(function(infoArray, index) {
        	  dataString = infoArray.join(',');
        	  csvContent += index < data.length ? dataString + '\n' : dataString;
        	});

        	var download = function(content, fileName, mimeType) {
        	  var a = document.createElement('a');
        	  mimeType = mimeType || 'application/octet-stream';

        	  if (navigator.msSaveBlob) { // IE10
        	    navigator.msSaveBlob(new Blob([content], {
        	      type: mimeType
        	    }), fileName);
        	  } else if (URL && 'download' in a) { //html5 A[download]
        	    a.href = URL.createObjectURL(new Blob([content], {
        	      type: mimeType
        	    }));
        	    a.setAttribute('download', fileName);
        	    document.body.appendChild(a);
        	    a.click();
        	    document.body.removeChild(a);
        	  } else {
        	    location.href = 'data:application/octet-stream,' + encodeURIComponent(content); // only this mime type is supported
        	  }
        	}

        	download(csvContent, 'umg_logFile.csv', 'text/csv;encoding:utf-8');
    	}
    },
    
    showOutSourcingWindow : function(button) {

    	if(pendingDocs != ''){
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'No se permite la carga de facturas para Servicios Especializados', msg:  'Nuestros registros indican que tiene los siguentes documentos de Servicios Especializados pendientes de aprobar: <br /><br />' + pendingDocs + '<br /><br />Le recordamos que toda la documentación deberá ser aprobada por CRYOINFRA antes de las facturas puedan ser enviadas'});
    		return false;
    	}

    	var me = this;
    	var orderForm = this.getReceiptForm().getForm();
		var orderNumber = orderForm.findField('orderNumber').getValue();
		var orderType = orderForm.findField('orderType').getValue();
		var addressNumber = orderForm.findField('addressNumber').getValue();
		var orderCompany = orderForm.findField('orderCompany').getValue();

        var parentGrid = this.getPurchaseOrderGrid();
    	var parentStore = parentGrid.getStore();
		
    	var grid = this.getReceiptGrid();
    	var store = grid.getStore();
    	var s = grid.getSelectionModel().getSelection();    	
    	var selected = [];
    	var receiptSubtotal = 0;
    	
    	Ext.each(s, function (item) {
    		selected.push(item.data.id);
    		receiptSubtotal = receiptSubtotal + item.data.foreignAmountReceived;
    	});    	
    	
    	if(selected.length > 0){
    		var filePanel = Ext.create(
    				'Ext.form.Panel',
    				{
    					width : 900,
    					items : [
	    						{
									xtype : 'textfield',
									name : 'orderCompany',
									hidden : true,
									value : orderCompany
								},
    							{
    								xtype : 'textfield',
    								name : 'documentNumber',
    								hidden : true,
    								value : orderNumber
    							},{
    								xtype : 'textfield',
    								name : 'documentType',
    								hidden : true,
    								value : orderType
    							},{
    								xtype : 'textfield',
    								name : 'addressBook',
    								hidden : true,
    								value : addressNumber
    							},{
    								xtype : 'textfield',
    								name : 'tipoComprobante',
    								hidden : true,
    								value : 'Factura'
    							},{
									xtype : 'textfield',
									name : 'receiptIdList',
									hidden : true,
									value : selected
								},{
    								xtype : 'filefield',
    								name : 'uploadedFiles[0]',
    								fieldLabel : SuppAppMsg.purchaseFileXML + '*:',
    								labelWidth : 120,
    								msgTarget : 'side',
    								allowBlank : false,
    								margin:'10 0 0 10',
    								anchor : '85%',
    								multiple: true,
    								buttonText : SuppAppMsg.suppliersSearch,
    								buttonConfig: {
										cls: 'buttonStyle'
								    }
    									
    							},{
    								xtype : 'filefield',
    								name : 'uploadedFiles[1]',
    								fieldLabel : SuppAppMsg.purchaseInvoice + '(.pdf)*:',
    								labelWidth : 120,
    								msgTarget : 'side',
    								allowBlank : false,
    								anchor : '85%',
    								buttonText : SuppAppMsg.suppliersSearch,
    								margin:'10 0 0 10',
    								buttonConfig: {
										cls: 'buttonStyle'
								    }
    							},{
    								xtype : 'filefield',
    								name : 'uploadedFiles[2]',
    								fieldLabel : SuppAppMsg.outsourcingFilePayroll + '*:',
    								labelWidth : 120,
    								msgTarget : 'side',
    								allowBlank : false,
    								anchor : '85%',
    								buttonText : SuppAppMsg.suppliersSearch,
    								margin:'10 0 0 10',
    								buttonConfig: {
										cls: 'buttonStyle'
								    }
    							}],

    					buttons : [ {
    						text : SuppAppMsg.supplierLoad,
    						cls: 'buttonStyle',
    						margin:'10 0 0 0',
    						handler : function() {
    							var form = this.up('form').getForm();
    							if (form.isValid()) {
    								form.submit({
    											url : 'uploadOutSourcingInvoiceDocuments.action',
    											waitMsg : SuppAppMsg.supplierLoadFile,
    											success : function(fp, o) {
    												var res = Ext.decode(o.response.responseText);
    												if(me.receiptWindow){
														me.receiptWindow.close();
													}
    												
    												if(me.outSourcingWindow){
														me.outSourcingWindow.close();
													}
    												
    												parentStore.load();
    												
    												Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.outsourcingFileLoadSuccess});
    												
    											},       
    									        failure: function(fp, o) {
    									        	var res = o.response.responseText;
    									        	var result = Ext.decode(res);
    									        	var msgResp = result.message
    									        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  msgResp});
    									        }
    										});
    							}
    						}
    					}]
    				});

    			me.outSourcingWindow = new Ext.Window({
    			layout : 'fit',
    			title : SuppAppMsg.outsourcingButton,
    			width : 600,
    			height : 230,
    			modal : true,
    			closeAction : 'destroy',
    			resizable : false,
    			minimizable : false,
    			maximizable : false,
    			plain : true,
    			items : [ filePanel ]
    		
    		});
    		me.outSourcingWindow.show();
    	}else{
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: SuppAppMsg.purchaseUploadBillErrorTitle  });
    		return false;
    	}	
    },
  	
    openPONotes : function(grid, rowIndex, colIndex, record) {
    	var me = this;
    	//var record = grid.store.getAt(rowIndex);
    	var record = rowIndex.data;
    	
     	var notesWindow = new Ext.Window({
    		layout : 'fit',
    		title : 'Notas',
    		width : 600,
    		height : 260,
    		modal : true,
    		closeAction : 'destroy',
    		resizable : false,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		html: record.notes
    	});
     	
     	notesWindow.show();
    }
    
 });