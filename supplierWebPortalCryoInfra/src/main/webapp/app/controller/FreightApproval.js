Ext.define('SupplierApp.controller.FreightApproval', {
    extend: 'Ext.app.Controller',
    stores: ['FreightApproval'],
    models: ['FreightApproval','FreightApprovalDetail'],
    views: ['freightApproval.FreightApprovalPanel','freightApproval.FreightApprovalGrid',
    	'freightApproval.FreightApprovalForm','freightApproval.FreightApprovalDetailPanel',
    	'freightApproval.FreightApprovalDetailGrid'],
    refs: [
	    {
	        ref: 'freightApprovalGrid',
	        selector: 'freightApprovalGrid'
	    },   
	    {
	        ref: 'freightApprovalDetailGrid',
	        selector: 'freightApprovalDetailGrid'
	    },
	    {
	        ref: 'freightApprovalForm',
	        selector: 'freightApprovalForm'
	    }],
 
    init: function() {
		this.winLoadInv = null;
		this.winLoadInvFile = null;
		this.winDetail = null;
        this.control({
	            'freightApprovalGrid': {
	            	itemdblclick: this.gridSelectionChange
	            },
				'freightApprovalGrid button[action=uploadNewFiscalDoc]' : {
					click : this.invLoad
				},
				'freightApprovalDocumentsForm button[action=uploadForeignDocsFD]' : {
					click: this.uploadForeignAdditional
				},
				'freightApprovalDocumentsForm button[action=sendForeignRecordFD]' : {
					click: this.sendForeignRecord
				},
				'#uploadAdditional' : {
					"buttonclick" : this.uploadAdditional
				},
				'#approveInvoiceFDA' : {
					"buttonclick" : this.approveInvoiceFDA
				},
				'#rejectInvoiceFDA' : {
					"buttonclick" : this.rejectInvoiceFDA
				},
				'#acceptSelInvFD' : {
					"buttonclick" : this.acceptSelInvFD
				},
				'#rejectSelInvFD' : {
					"buttonclick" : this.rejectSelInvFD
				},
        		'#viewPDFFDA' : {
			     "buttonclick" : this.viewPDFFDA
		        },
				'acceptInvGridFD button[action=loadComplFileFD]' : {
					click: this.loadComplFileFD
				},
				'freightApprovalGrid button[action=fdSearch]' : {
					click : this.fdSearch
				},
				'fiscalDocumentsGrid button[action=poUploadInvoiceFile]' : {
					click : this.poUploadInvoiceFile
				},
				'fiscalDocumentsGrid button[action=poUploadCreditNoteFile]' : {
					click : this.poUploadCreditNoteFile
				},
				'fiscalDocumentsGrid button[action=fdLoadCompl]' : {
					click : this.fdLoadCompl
				},
				'freightApprovalDetailGrid button[action=updateOrder]' : {
					click : this.updateOrder
				},
				'freightApprovalDetailGrid' : {
					itemdblclick: this.openReceiptForm
				},
        });
    },
    
    gridSelectionChange: function(model, record) {
    	
        if (record) {
        	var batchID = record.data.id;
        	var amount = record.data.amount;
        	
        	
        	var me = this;   	
        	//var box = Ext.MessageBox.wait(SuppAppMsg.approvalLoadRegistrer, SuppAppMsg.approvalExecution);

        		Ext.Ajax.request({
    				url : 'freight/listInvoiceByBatchID.action',
    				method : 'GET',
    					params : {
    						start : 0,
    						limit : 20,
    						batchID: batchID
    					},
    					success : function(response,opts) {
    						var resp = Ext.decode(response.responseText);
    						if(resp.data !=null){
    							
    							var controller = _AppGlobSupplierApp.getController('FreightApproval');
    							
    							this.winDetail = new Ext.Window({
    				        		layout : 'fit',
    				        		title : SuppAppMsg.freightApprovalDetailBatch,
    				        		//width : 1180,
    				        		//height : 550,
    				        		minWidth: 400,
    				        	    minHeight: 300,
    				        	    width: Ext.Element.getViewportWidth() * 0.4,  // 80% del ancho
    				        	    height: Ext.Element.getViewportHeight() * 0.4, // 80% del alto
    				        	    responsiveConfig: {
    				        	        'width < 600': {
    				        	            width: '95%',
    				        	            height: '75%'
    				        	        },
    				        	        'width >= 600 && width < 1000': {
    				        	            width: '85%',
    				        	            height: '70%'
    				        	        },
    				        	        'width >= 1000': {
    				        	            width: '80%',
    				        	            height: '80%'
    				        	        }
				        	        },    
    				        		modal : true,
    				        		closeAction : 'destroy',
    				        		resizable : false,
    				        		minimizable : false,
    				        		maximizable : false,
    				        		plain : true,
    				        		items : [ {
    				        			xtype : 'freightApprovalDetailPanel',
    				        			border : true
    				        			//height : 415
    				        		}  ]

    				        	});
    							
    							Ext.getCmp('idFDC').setValue(batchID);
    							Ext.getCmp('amountFDC').setValue(amount);
    							record.data.concepts=resp.data;
    							
    							this.winDetail.show();        	
    				        	
    				        	var form = controller.getFreightApprovalForm().getForm();
    				        	form.loadRecord(record);

    				       
    				        	var g = controller.getFreightApprovalDetailGrid();
    				        	g.store.loadData([], false);
    				        	g.getView().refresh();

    				        	var itemList = record.get('concepts');
    				        	Ext.each(itemList, function(rec, index) {
    				        		rec.toReceive = 0;
    				        		rec.toReject = 0;
    				        		rec.reason ="";
    				   				var r = Ext.create('SupplierApp.model.FreightApprovalDetail',rec);
    				   				g.store.insert(index, r);
    				            });
    						}else{
    				     		var win = new Ext.Window({
				        			  title: 'Mensaje del sistema',
				        			  width: 540,
				        			  height: 100,
				        			  modal:true,
				        			  preventBodyReset: true,
				        			  html: '<br /><center>El batch existe, pero no tiene facturas registradas en el sistema</center>'
				        			});
				        			win.show();
    						}
    					},
    					failure : function() {
    						}
    					});
        	
          //  box.hide();
        }
    },
    
    approveInvoiceFDA : function(grid, rowIndex, colIndex, record) {
    	
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var orderNumber = record.data.orderNumber;
    	var dto = Ext.create('SupplierApp.model.FreightApproval',record.data);
    	var status ="APROBADO";
    	var currentApprover = addressNumber;
    	var nextApprover = record.data.nextApprover;
    	var step = record.data.approvalStep;
    	
    	var filePanel = Ext.create(
				'Ext.form.Panel',
				{
					//width: 510,
					layout: {
			            type: 'vbox',
			            align: 'stretch'
			        },
			        bodyPadding: 10,
			        defaults: {
			            xtype: 'textfield',
			            anchor: '100%',
			            margin: '5 0 0 0',
			            labelWidth: 160
			        },
					items : [{
								fieldLabel : 'Centro de Costos',
								xtype : 'textfield',
								name : 'centroCostos',
								id:'centroCostosFDA',
								//width:300,
								//margin:'5 0 0 5',
								//colspan:3,
								//allowBlank:orderNumber==0?false:true,
								//hidden:orderNumber==0?false:true,
								hidden:true								
							},{
								fieldLabel : 'Concepto/Articulo',
								xtype : 'textfield',
								name : 'conceptoArticulo',
								id:'conceptoArticuloFDA',
								//width:300,
								//colspan:3,
								//margin:'5 0 0 5',
								//allowBlank:orderNumber==0?false:true,
								//hidden:orderNumber==0?false:true,
								hidden:true								
							},{
								fieldLabel : 'Compañia',
								xtype : 'textfield',
								name : 'companyFDA',
								id:'companyFDA',
								//width:300,
								//colspan:3,
								//margin:'5 0 0 5',
								//allowBlank:orderNumber==0?false:true,
								//hidden:orderNumber==0?false:true,
								hidden:true								
							},{
								fieldLabel : SuppAppMsg.purchaseTitle30,
								xtype : 'textfield',
								name : 'notes',
								id : 'notesFDA',
								//width:500,
								//colspan:3,
								//margin:'10 0 0 10'
							}],

					buttons : [{
						text : SuppAppMsg.approvalApprove,
						cls: 'buttonStyle',
						//margin:'0 5 0 0',
						handler : function() {
							var form = this.up('form').getForm();
							if (form.isValid()) {
								var centroCostos = Ext.getCmp('centroCostosFDA').getValue();
								var concepto = Ext.getCmp('conceptoArticuloFDA').getValue();
								var company = Ext.getCmp('companyFDA').getValue();
								var note = Ext.getCmp('notesFDA').getValue();
								
								Ext.MessageBox.show({
									title : SuppAppMsg.approvalInvRespTittle,
									msg : SuppAppMsg.approvalInvRespMessage,
									buttons : Ext.MessageBox.YESNO,
									//multiline: true,
									//width:100,
									buttonText : {
										yes : SuppAppMsg.approvalAcept,
										no : SuppAppMsg.approvalExit
									},
									fn : function(btn, text) {
										if (btn === 'yes') {
											if(text != ""){
												
												var box = Ext.MessageBox.wait(
														SuppAppMsg.approvalUpdateData,
														SuppAppMsg.approvalExecution);
												//var notes = text;
												Ext.Ajax.request({
												    url: 'freight/update.action',
												    method: 'POST',
												    params: {
												    	centroCostos:centroCostos,
												    	status:status,
												    	conceptoArticulo:concepto,
												    	company:company,
												    	note:note,
												    	documentType: record.data.type,
												    	currentApprover: currentApprover,
												    	nextApprover: nextApprover,
												    	step: step,
												    	cancelOrder:false
											        },
												    jsonData: dto.data,
												    success: function(fp, o) {
												    	var res = Ext.decode(fp.responseText);
												    	grid.store.load();
												    	box.hide();
												    	me.winLoadInv.close(); 
												    	if(res.message == "Success"){
												    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalInvRespUpdate);
												    	}else if(res.message == "Error JDE"){
												    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalInvRespErrorJDE);
												    	}else if(res.message == "Succ Update"){
												    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalInvRespAprobadoSucc);
												    	}else if(res.message == "Rejected"){
												    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalInvRespRejected);
												    	}else{
												    		Ext.Msg.alert(SuppAppMsg.approvalResponse, res.message);
												    	}
											        	//Ext.Msg.alert('Respuesta', res.message);
												    },
												    failure: function() {
												    	box.hide();
												    	Ext.MessageBox.show({
											                title: 'Error',
											                msg: SuppAppMsg.approvalUpdateError,
											                buttons: Ext.Msg.OK
											            });
												    }
												}); 
												
												
											}else{
						            			Ext.Msg.alert(SuppAppMsg.approvalAlert, SuppAppMsg.approvalMessages);
						            		}

										}
									}
								});
								
							}else{
								Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error de aprobación', msg:  'Los campos anteriores deben ser llenados correctamente.'});
							}
						}
					}]
				});

    	me.winLoadInv = new Ext.Window({
    		layout : 'fit',
    		title : SuppAppMsg.taxvaultAdditionalInformation,
    		maxWidth : 550,
    		maxHeight : 130,
    		width: Ext.Element.getViewportWidth() * 0.3,  // 50% del ancho de la pantalla
            height: Ext.Element.getViewportHeight() * 0.3, // 40% del alto
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
    
    viewPDFFDA : function(grid, rowIndex, colIndex, record) {
    	
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var uuid = record.data.uuid;
    	var dto = Ext.create('SupplierApp.model.FreightApproval',record.data);
    	
    	Ext.Ajax.request({
			url : 'documents/openDocumentByUuid.action',
			method : 'GET',
			params: {
				orderNumber:0,
		    	uuid:uuid,
		    	type:"application/pdf"
	        },
	        jsonData: dto.data,
				success : function(response,opts) {
					
					//var resp = Ext.decode(response.responseText);
					var index = 0;
					var files = "";
					//for (index = 0; index < response.data.length; index++) {
						var href = "documents/openDocument.action?id=" + 46;
						
						window.open(href, '_blank');
						//var fileHref = "<a href= '" + href + "' target='_blank'>" +  "ASD" + "</a>";
                       // files = files + "> " + fileHref + " - " + response.data[index].size + " bytes : " + response.data[index].fiscalType + " - " + accepted +  "<br />";
				//}
				},
				failure : function() {
				//	box.hide();
				}
			});

    	
    },
    
    rejectInvoiceFDA : function(grid, rowIndex, colIndex, record) {
    	
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var dto = Ext.create('SupplierApp.model.FreightApproval',record.data);
    	var status ="RECHAZADO";
    	var currentApprover = addressNumber;
    	var nextApprover = record.data.nextApprover;
    	var step = record.data.approvalStep;
    	
    	var dlgRejected = Ext.MessageBox.show({
    		title : SuppAppMsg.rejectDoc,
			msg : SuppAppMsg.approvalNoteReject,
			buttons : Ext.MessageBox.YESNO,
			multiline: true,
			width:500,
			buttonText : {
				yes : SuppAppMsg.approvalAcept,
				no : SuppAppMsg.approvalExit
			},
			fn : function(btn, text) {
				if (btn === 'yes') {
					var cancelOrder = false;
					//if (document.getElementById('cancel_orders').checked){
					//	cancelOrder = true;
					//}
					
					if(text != ""){
						var box = Ext.MessageBox.wait(
								SuppAppMsg.approvalUpdateData,
								SuppAppMsg.approvalExecution);
						var notes = text;
						Ext.Ajax.request({
						    url: 'freight/update.action',
						    method: 'POST',
						    params: {
						    	centroCostos:'',
						    	status:status,
						    	conceptoArticulo:'',
						    	company:'',
						    	note:notes,
						    	documentType: record.data.type,
						    	currentApprover: currentApprover,
						    	nextApprover: nextApprover,
						    	step: step,
						    	cancelOrder: cancelOrder
					        },
						    jsonData: dto.data,
						    success: function(fp, o) {
						    	var res = Ext.decode(fp.responseText);
						    	grid.store.load();
						    	box.hide();
						    	if(res.message == "Success"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalInvRespUpdate);
						    	}else if(res.message == "Error JDE"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalInvRespErrorJDE);
						    	}else if(res.message == "Succ Update"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalInvRespAprobadoSucc);
						    	}else if(res.message == "Rejected"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.freightApprovalRespRejected);
						    	}else{
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, res.message);
						    	}
						    },
						    failure: function() {
						    	box.hide();
						    	Ext.MessageBox.show({
					                title: 'Error',
					                msg: SuppAppMsg.approvalUpdateError,
					                buttons: Ext.Msg.OK
					            });
						    }
						}); 
						
						
					}else{
            			Ext.Msg.alert(SuppAppMsg.approvalAlert, SuppAppMsg.approvalMessages);
            		}

				}
			}
		});
    	
    	dlgRejected.textArea.inputEl.set({
		    maxLength: 255
		});
    },
    
    invLoad : function(grid, rowIndex, colIndex, record) {
        var me = this;
    	var filePanel = Ext.create(
    					'Ext.form.Panel',
    					{
    						width : 900,
    						items : [
    						         {
    									xtype : 'textfield',
    									name : 'addressBook',
    									hidden : true,
    									value : userName
    								},
    								{
    									xtype : 'textfield',
    									name : 'tipoComprobante',
    									hidden : true,
    									value : 'Factura'
    								},{
    									xtype : 'textfield',
    									name : 'fdId',
    									hidden : true,
    									value : 0
    								},{
    									xtype : 'filefield',
    									name : 'file',
    									fieldLabel : 'Archivo(xml):',
    									labelWidth : 120,
    									msgTarget : 'side',
    									allowBlank : false,
    									margin:'15 0 70 0',
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
    												url : 'uploadInvoice.action',
    												waitMsg : SuppAppMsg.supplierLoadFile,
    												success : function(fp, o) {
    													var res = Ext.decode(o.response.responseText);
    													Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
    													
    													me.winLoadInv.close();
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
    		title : 'Informacion complementaria',
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
    },
    
    poUploadInvoiceFile:  function(button) {
    	var supNumber = Ext.getCmp('supNumberFD').getValue() == ''?'':Ext.getCmp('supNumberFD').getValue();
    	var documentType = 'Factura';
    	var documentNumber = 0;
    	var isFormOrigin = false;
    	
    	this.uploadInvoiceFile(supNumber, documentType, documentNumber, isFormOrigin);
    	
    },
    
    poUploadCreditNoteFile:  function(button) {
    	var supNumber = Ext.getCmp('supNumberFD').getValue() == ''?'':Ext.getCmp('supNumberFD').getValue();
    	var documentType = 'NotaCredito';
    	var documentNumber = 0;
    	var isFormOrigin = false;
    	var me = this;
    	
    	this.uploadInvoiceFile(supNumber, documentType, documentNumber, isFormOrigin);
    	
    },
    
    uploadInvoiceFile:  function(supNumber, documentType, documentNumber, isFormOrigin) {
    	var isTransportCB = false;
    	var me = this;
    	
    	if(supNumber != ""){
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    		Ext.Ajax.request({
    		    url: 'supplier/getTransportCustomBroker.action',
    		    method: 'POST',
    		    params: {
    		    	addressNumber : supNumber
    	        },
    		    success: function(fp, o) {
    		    	var res = Ext.decode(fp.responseText);
    		    	me.isTransportCB = res.data;
    	    		Ext.Ajax.request({
    	    		    url: 'supplier/getByAddressNumber.action',
    	    		    method: 'POST',
    	    		    params: {
    	    		    	addressNumber : supNumber
    	    	        },
    	    		    success: function(fp, o) {
    	    		    	box.hide();
    	    		    	var res = Ext.decode(fp.responseText);
    	    		    	var sup = Ext.create('SupplierApp.model.Supplier',res.data);
    	    	    		if(sup.data.country.trim() == "MX"){// || me.isStorageOnly == true
    	    			    	var filePanel = Ext.create(
    	    			    					'Ext.form.Panel',
    	    			    					{
    	    			    						width : 1000,
    	    			    						items : [{
    			    			    				            xtype: 'fieldset',
    			    			    				            id: 'principalFields',
    			    			    				            title: SuppAppMsg.fiscalTitleMainInv,
    			    			    				            defaultType: 'textfield',
    			    			    				            margin:'0 10 0 10',
    			    			    				            defaults: {
    			    			    				                anchor: '95%'
    			    			    				            },
    		    			    						        items:[{
    						    									xtype : 'textfield',
    						    									name : 'addressBook',
    						    									hidden : true,
    						    									value : supNumber
    						    								},/*{
    						    									xtype : 'textfield',
    						    									name : 'documentNumber',
    						    									hidden : true,
    						    									value : documentNumber
    						    								},*/{
    						    									xtype : 'textfield',
    						    									name : 'tipoComprobante',
    						    									hidden : true,
    						    									value : documentType
    						    								}/*,{
    						    									xtype : 'textfield',
    						    									name : 'isFormOrigin',
    						    									hidden : true,
    						    									value : isFormOrigin
    						    								},{
    						    									xtype : 'textfield',
    						    									name : 'isStorageOnly',
    						    									hidden : true,
    						    									value : me.isStorageOnly
    						    								}*/,{
    		    			    									xtype : 'filefield',
    		    			    									name : 'file',
    		    			    									fieldLabel : SuppAppMsg.purchaseFileXML + '*:',
    		    			    									labelWidth : 120,
    		    			    									anchor : '100%',
    		    			    									msgTarget : 'side',
    		    			    									allowBlank : false,
    		    			    									clearOnSubmit: false,
    		    			    									margin:'10 0 0 0',
    		    			    									buttonText : SuppAppMsg.suppliersSearch
    		    			    								},{
    		    			    									xtype : 'filefield',
    		    			    									name : 'fileTwo',
    		    			    									fieldLabel : SuppAppMsg.purchaseFilePDF + '*:',
    		    			    									labelWidth : 120,
    		    			    									anchor : '100%',
    		    			    									msgTarget : 'side',
    		    			    									allowBlank : false,
    		    			    									clearOnSubmit: false,
    		    			    									buttonText : SuppAppMsg.suppliersSearch,
    		    			    									margin:'10 0 0 0'
    		    			    								},{
    		    			    									xtype : 'container',
    		    			    									layout : 'hbox',
    		    			    									margin : '0 0 0 0',
    		    			    									anchor : '100%',
    		    			    									style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
    		    			    									defaults : {
    		    			    											labelWidth : 100,
    		    			    											xtype : 'textfield',
    		    			    											labelAlign: 'left'
    		    			    									},
    		    			    									items : [{
	        		    			    									xtype : 'combo',
	        		    			    									fieldLabel : SuppAppMsg.paymentTitle1,
	        		    			    									id : 'supCompanyCombo',
	        		    			    									itemId : 'supCompanyCombo',
	        		    			    									name : 'supCompany',
	        		    			    									store : getUDCStore('COMPANYCB', '', '', ''),
	        		    			    									valueField : 'udcKey',
	        		    			    									displayField: 'strValue2',
	        		    			    									emptyText : SuppAppMsg.purchaseTitle19,
	        		    			    					                typeAhead: true,
	        		    			    					                minChars: 2,
	        		    			    					                forceSelection: true,
	        		    			    					                triggerAction: 'all',
	        		    			    					                labelWidth:120,
	        		    			    					                width : 700,
	        		    			    					                //anchor : '100%',
	        		    			    									allowBlank : false,
	        		    			    									margin:'10 0 10 0'
    		    			    									},{
	    			    			    									xtype: 'numericfield',
	    			    			    									name : 'advancePayment',
	    			    			    									itemId : 'advancePayment',
	    			    			    									id : 'advancePayment',				    			    									
	    			    			    									fieldLabel : SuppAppMsg.fiscalTitle23,
	    			    			    									labelWidth:50,
	    			    			    									width : 130,
	    			    			    									value: 0,
	    			    			    									useThousandSeparator: true,
	    			    			                                        decimalPrecision: 2,
	    			    			                                        alwaysDisplayDecimals: true,
	    			    			                                        allowNegative: false,
	    			    			                                        currencySymbol:'$',
	    			    			                                        hideTrigger:true,
	    			    			                                        thousandSeparator: ',',
	    			    			                                        allowBlank : false,
	    			    			                                        margin:'10 0 10 10'
    		    			    									}]
    		    			    						        }]
    		    			    				            },{
    			    			    				            xtype: 'fieldset',
    			    			    				            id: 'conceptsFields',
    			    			    				            title: SuppAppMsg.fiscalTitleExtraCons,
    			    			    				            defaultType: 'textfield',
    			    			    				            margin:'0 10 0 10',
    			    			    				            hidden: me.isTransportCB==true?true:false,
    			    			    				            defaults: {
    			    			    				                anchor: '100%'
    			    			    				            },
    			    			    				            items: [{
    			    			    				            		xtype: 'tabpanel',
    			    			    				            		plain: true,
    			    			    				            		border:false,
    			    			    				            		items:[{
    			    			    				            			title: SuppAppMsg.fiscalTitleWithTaxInvoice,
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
    								    			    									itemId : 'conceptImport1',
    								    			    									id : 'conceptImport1',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'10 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept1_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleCNT,				    			    									
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'10 0 0 5'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept1_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleCNT,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
    								    			    									margin:'10 0 0 5'
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
    								    			    									itemId : 'conceptImport2',
    								    			    									id : 'conceptImport2',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept2_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleValidation,
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept2_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleValidation,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
    								    			    									itemId : 'conceptImport3',
    								    			    									id : 'conceptImport3',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept3_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleManeuvers,
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept3_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleManeuvers,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
    								    			    									itemId : 'conceptImport4',
    								    			    									id : 'conceptImport4',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept4_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation,
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept4_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
    							    			    								},//Maniobras en rojo
    							    			    								items : [{
    								    			    									xtype: 'numericfield',
    								    			    									name : 'conceptImport5',
    								    			    									itemId : 'conceptImport5',
    								    			    									id : 'conceptImport5',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept5_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleRedMan,
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept5_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleRedMan,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
    								    			    									itemId : 'conceptImport6',
    								    			    									id : 'conceptImport6',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept6_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleFumigation,
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept6_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleFumigation,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
    								    			    									itemId : 'conceptImport7',
    								    			    									id : 'conceptImport7',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept7_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleDocking,
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept7_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleDocking,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
    								    			    									itemId : 'conceptImport8',
    								    			    									id : 'conceptImport8',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept8_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleStorage,
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept8_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleStorage,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
    								    			    									itemId : 'conceptImport9',
    								    			    									id : 'conceptImport9',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept9_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleDelays,
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept9_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleDelays,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
    								    			    									itemId : 'conceptImport10',
    								    			    									id : 'conceptImport10',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept10_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleDragging,
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept10_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleDragging,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
    							    			    								},//Permisos
    							    			    								items : [{
    								    			    									xtype: 'numericfield',
    								    			    									name : 'conceptImport11',
    								    			    									itemId : 'conceptImport11',
    								    			    									id : 'conceptImport11',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept11_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitlePermissions,
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept11_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitlePermissions,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
    								    			    									itemId : 'conceptImport12',
    								    			    									id : 'conceptImport12',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept12_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleDuties,
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept12_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleDuties,
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
    								    			    									itemId : 'conceptImport13',
    								    			    									id : 'conceptImport13',				    			    									
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
    								    			                                        hidden: true,
    								    			                                        margin:'0 0 0 0'	
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept13_1',
    								    			    									//hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1',
    								    			    									labelWidth : 120,
    								    			    									width : 480,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.xml)',
    								    			    									margin:'0 0 0 5'
    							    			    								},{
    								    			    									xtype : 'filefield',
    								    			    									name : 'fileConcept13_2',
    								    			    									hideLabel:true,
    								    			    									clearOnSubmit: false,
    								    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1',
    								    			    									labelWidth : 120,
    								    			    									width : 350,
    								    			    									msgTarget : 'side',
    								    			    									buttonText : SuppAppMsg.suppliersSearch,
    								    			    									emptyText : '(.pdf)',
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
									    			    									itemId : 'conceptImport14',
									    			    									id : 'conceptImport14',				    			    									
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
									    			                                        hidden: true,
									    			                                        margin:'0 0 0 0'	
								    			    								},{
									    			    									xtype : 'filefield',
									    			    									name : 'fileConcept14_1',
									    			    									//hideLabel:true,
									    			    									clearOnSubmit: false,
									    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2',
									    			    									labelWidth : 120,
									    			    									width : 480,
									    			    									msgTarget : 'side',
									    			    									buttonText : SuppAppMsg.suppliersSearch,
									    			    									emptyText : '(.xml)',
									    			    									margin:'0 0 0 5'
								    			    								},{
									    			    									xtype : 'filefield',
									    			    									name : 'fileConcept14_2',
									    			    									hideLabel:true,
									    			    									clearOnSubmit: false,
									    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2',
									    			    									labelWidth : 120,
									    			    									width : 350,
									    			    									msgTarget : 'side',
									    			    									buttonText : SuppAppMsg.suppliersSearch,
									    			    									emptyText : '(.pdf)',
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
									    			    									itemId : 'conceptImport15',
									    			    									id : 'conceptImport15',				    			    									
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
									    			                                        hidden: true,
									    			                                        margin:'0 0 0 0'	
								    			    								},{
									    			    									xtype : 'filefield',
									    			    									name : 'fileConcept15_1',
									    			    									//hideLabel:true,
									    			    									clearOnSubmit: false,
									    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3',
									    			    									labelWidth : 120,
									    			    									width : 480,
									    			    									msgTarget : 'side',
									    			    									buttonText : SuppAppMsg.suppliersSearch,
									    			    									emptyText : '(.xml)',
									    			    									margin:'0 0 0 5'
								    			    								},{
									    			    									xtype : 'filefield',
									    			    									name : 'fileConcept15_2',
									    			    									hideLabel:true,
									    			    									clearOnSubmit: false,
									    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3',
									    			    									labelWidth : 120,
									    			    									width : 350,
									    			    									msgTarget : 'side',
									    			    									buttonText : SuppAppMsg.suppliersSearch,
									    			    									emptyText : '(.pdf)',
									    			    									margin:'0 0 0 5'
								    			    								}]
    									    				            }]
    			    			    				            		},{
    			    			    				            			title: SuppAppMsg.fiscalTitleWithoutTaxInvoiceOrUSD,
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
											    			    									margin: '10 0 10 5'
										    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptImport16',
											    			    									itemId : 'conceptImport16',
											    			    									id : 'conceptImport16',
											    			    									hidden: true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitleNoPECEAcc,
											    			    									labelWidth:110,
											    			    									width : 200,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'10 0 0 5',
											    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
											    					                                readOnly: true
										    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptSubtotal16',
											    			    									itemId : 'conceptSubtotal16',
											    			    									id : 'conceptSubtotal16',
											    			    									hidden: true,
											    			    									hideLabel:true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitleNoPECEAcc,
											    			    									labelWidth:110,
											    			    									width : 200,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'10 0 0 5',
											    								            		listeners:{
											    														change: function(field, newValue, oldValue){
											    															Ext.getCmp('conceptImport16').setValue(newValue);    										    															
											    														}
											    													}
										    			    								},{
							        		    			    									xtype : 'combo',
							        		    			    									hidden: true,
							        		    			    									hideLabel:true,
							        		    			    									fieldLabel : SuppAppMsg.fiscalTitle24,
							        		    			    									id : 'taxCodeCombo16',
							        		    			    									itemId : 'taxCodeCombo16',
							        		    			    									name : 'taxCode16',
							        		    			    									store : getUDCStore('INVTAXCODE', '', '', ''),
							        		    			    									valueField : 'strValue1',
							        		    			    									displayField: 'udcKey',
							        		    			    									//emptyText : SuppAppMsg.purchaseTitle19,
							        		    			    									emptyText : 'MX0',
							        		    			    									typeAhead: true,
							        		    			    					                minChars: 1,
							        		    			    					                forceSelection: true,
							        		    			    					                triggerAction: 'all',
							        		    			    					                labelWidth:120,
							        		    			    					                width : 100,
							        		    			    									allowBlank : true,
							        		    			    									margin:'0 0 0 5',
											    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
											    					                                readOnly: true
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
											    			    									margin:'21 0 0 5'
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
											    			    									margin:'21 0 0 5'
										    			    								}]
    			    			    				            				},{
    									    			    								xtype : 'container',
    									    			    								layout : 'hbox',
    									    			    								margin : '0 0 0 0',
    									    			    								style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
    									    			    								//DESCRIPCIÓN COLUMNAS
    									    			    								items : [{
											    			    									xtype: 'label',
											    			    									text : '',
											    			    									margin: '0 0 10 5',
											    			    									width : 130
    									    			    								},{
											    			    									xtype: 'label',
											    			    									text : SuppAppMsg.fiscalTitle9,
											    			    									margin: '0 0 10 5',
											    			    									width : 90
    									    			    								},{
											    			    									xtype: 'label',
											    			    									text : SuppAppMsg.fiscalTitle6,
											    			    									margin: '0 0 10 5',
											    			    									width : 90
    									    			    								},{
											    			    									xtype: 'label',
											    			    									text : SuppAppMsg.fiscalTitle24,
											    			    									margin: '0 0 10 5',
											    			    									width : 100
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
    										    			    									itemId : 'conceptImport17',
    										    			    									id : 'conceptImport17',				    			    									
    										    			    									fieldLabel : SuppAppMsg.fiscalTitleDTA,
    										    			    									labelWidth:110,
    										    			    									width : 200,
    										    			    									value: 0,
    										    			    									useThousandSeparator: true,
    										    			                                        decimalPrecision: 2,
    										    			                                        alwaysDisplayDecimals: true,
    										    			                                        allowNegative: false,
    										    			                                        currencySymbol:'$',
    										    			                                        hideTrigger:true,
    										    			                                        thousandSeparator: ',',
    										    			                                        allowBlank : false,
    										    			                                        margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
    									    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptSubtotal17',
											    			    									itemId : 'conceptSubtotal17',
											    			    									id : 'conceptSubtotal17',
											    			    									hideLabel:true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitleDTA,
											    			    									labelWidth:90,
											    			    									width : 90,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
    										    								            		listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															Ext.getCmp('conceptImport17').setValue(newValue);    										    															
    										    														}
    										    													}
    									    			    								},{
	    					        		    			    									xtype : 'combo',
	    					        		    			    									hideLabel:true,
	    					        		    			    									fieldLabel : SuppAppMsg.fiscalTitle24,
	    					        		    			    									id : 'taxCodeCombo17',
	    					        		    			    									itemId : 'taxCodeCombo17',
	    					        		    			    									name : 'taxCode17',
	    					        		    			    									store : getUDCStore('INVTAXCODE', '', '', ''),
	    					        		    			    									valueField : 'strValue1',
	    					        		    			    									displayField: 'udcKey',
	    					        		    			    									//emptyText : SuppAppMsg.purchaseTitle19,
	    					        		    			    									emptyText : 'MX0',
	    					        		    			    					                typeAhead: true,
	    					        		    			    					                minChars: 1,
	    					        		    			    					                forceSelection: true,
	    					        		    			    					                triggerAction: 'all',
	    					        		    			    					                labelWidth:120,
	    					        		    			    					                width : 100,
	    					        		    			    									allowBlank : true,
	    					        		    			    									margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
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
    										    			    									itemId : 'conceptImport18',
    										    			    									id : 'conceptImport18',				    			    									
    										    			    									fieldLabel : SuppAppMsg.fiscalTitleIVA,
    										    			    									labelWidth:110,
    										    			    									width : 200,
    										    			    									value: 0,
    										    			    									useThousandSeparator: true,
    										    			                                        decimalPrecision: 2,
    										    			                                        alwaysDisplayDecimals: true,
    										    			                                        allowNegative: false,
    										    			                                        currencySymbol:'$',
    										    			                                        hideTrigger:true,
    										    			                                        thousandSeparator: ',',
    										    			                                        allowBlank : false,
    										    			                                        margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
    									    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptSubtotal18',
											    			    									itemId : 'conceptSubtotal18',
											    			    									id : 'conceptSubtotal18',
											    			    									hideLabel:true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitleIVA,
											    			    									labelWidth:90,
											    			    									width : 90,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
    										    								            		listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															Ext.getCmp('conceptImport18').setValue(newValue);    										    															
    										    														}
    										    													}
    									    			    								},{
	    					        		    			    									xtype : 'combo',
	    					        		    			    									hideLabel:true,
	    					        		    			    									fieldLabel : SuppAppMsg.fiscalTitle24,
	    					        		    			    									id : 'taxCodeCombo18',
	    					        		    			    									itemId : 'taxCodeCombo18',
	    					        		    			    									name : 'taxCode18',
	    					        		    			    									store : getUDCStore('INVTAXCODE', '', '', ''),
	    					        		    			    									valueField : 'strValue1',
	    					        		    			    									displayField: 'udcKey',
	    					        		    			    									//emptyText : SuppAppMsg.purchaseTitle19,
	    					        		    			    									emptyText : 'MX0',
	    					        		    			    									typeAhead: true,
	    					        		    			    					                minChars: 1,
	    					        		    			    					                forceSelection: true,
	    					        		    			    					                triggerAction: 'all',
	    					        		    			    					                labelWidth:120,
	    					        		    			    					                width : 100,
	    					        		    			    									allowBlank : true,
	    					        		    			    									margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
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
    										    			    									itemId : 'conceptImport19',
    										    			    									id : 'conceptImport19',				    			    									
    										    			    									fieldLabel : SuppAppMsg.fiscalTitleIGI,
    										    			    									labelWidth:110,
    										    			    									width : 200,
    										    			    									value: 0,
    										    			    									useThousandSeparator: true,
    										    			                                        decimalPrecision: 2,
    										    			                                        alwaysDisplayDecimals: true,
    										    			                                        allowNegative: false,
    										    			                                        currencySymbol:'$',
    										    			                                        hideTrigger:true,
    										    			                                        thousandSeparator: ',',
    										    			                                        allowBlank : false,
    										    			                                        margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
    									    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptSubtotal19',
											    			    									itemId : 'conceptSubtotal19',
											    			    									id : 'conceptSubtotal19',
											    			    									hideLabel:true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitleIGI,
											    			    									labelWidth:90,
											    			    									width : 90,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
    										    								            		listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															Ext.getCmp('conceptImport19').setValue(newValue);    										    															
    										    														}
    										    													}
    									    			    								},{
	    					        		    			    									xtype : 'combo',
	    					        		    			    									hideLabel:true,
	    					        		    			    									fieldLabel : SuppAppMsg.fiscalTitle24,
	    					        		    			    									id : 'taxCodeCombo19',
	    					        		    			    									itemId : 'taxCodeCombo19',
	    					        		    			    									name : 'taxCode19',
	    					        		    			    									store : getUDCStore('INVTAXCODE', '', '', ''),
	    					        		    			    									valueField : 'strValue1',
	    					        		    			    									displayField: 'udcKey',
	    					        		    			    									//emptyText : SuppAppMsg.purchaseTitle19,
	    					        		    			    									emptyText : 'MX0',
	    					        		    			    									typeAhead: true,
	    					        		    			    					                minChars: 1,
	    					        		    			    					                forceSelection: true,
	    					        		    			    					                triggerAction: 'all',
	    					        		    			    					                labelWidth:120,
	    					        		    			    					                width : 100,
	    					        		    			    									allowBlank : true,
	    					        		    			    									margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
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
    										    			    									itemId : 'conceptImport20',
    										    			    									id : 'conceptImport20',				    			    									
    										    			    									fieldLabel : SuppAppMsg.fiscalTitlePRV,
    										    			    									labelWidth:110,
    										    			    									width : 200,
    										    			    									value: 0,
    										    			    									useThousandSeparator: true,
    										    			                                        decimalPrecision: 2,
    										    			                                        alwaysDisplayDecimals: true,
    										    			                                        allowNegative: false,
    										    			                                        currencySymbol:'$',
    										    			                                        hideTrigger:true,
    										    			                                        thousandSeparator: ',',
    										    			                                        allowBlank : false,
    										    			                                        margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
    									    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptSubtotal20',
											    			    									itemId : 'conceptSubtotal20',
											    			    									id : 'conceptSubtotal20',
											    			    									hideLabel:true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitlePRV,
											    			    									labelWidth:90,
											    			    									width : 90,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
    										    								            		listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															Ext.getCmp('conceptImport20').setValue(newValue);    										    															
    										    														}
    										    													}
    									    			    								},{
	    					        		    			    									xtype : 'combo',
	    					        		    			    									hideLabel:true,
	    					        		    			    									fieldLabel : SuppAppMsg.fiscalTitle24,
	    					        		    			    									id : 'taxCodeCombo20',
	    					        		    			    									itemId : 'taxCodeCombo20',
	    					        		    			    									name : 'taxCode20',
	    					        		    			    									store : getUDCStore('INVTAXCODE', '', '', ''),
	    					        		    			    									valueField : 'strValue1',
	    					        		    			    									displayField: 'udcKey',
	    					        		    			    									//emptyText : SuppAppMsg.purchaseTitle19,
	    					        		    			    									emptyText : 'MX0',
	    					        		    			    									typeAhead: true,
	    					        		    			    					                minChars: 1,
	    					        		    			    					                forceSelection: true,
	    					        		    			    					                triggerAction: 'all',
	    					        		    			    					                labelWidth:120,
	    					        		    			    					                width : 100,
	    					        		    			    									allowBlank : true,
	    					        		    			    									margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
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
    										    			    									itemId : 'conceptImport21',
    										    			    									id : 'conceptImport21',				    			    									
    										    			    									fieldLabel : SuppAppMsg.fiscalTitleIVAPRV,
    										    			    									labelWidth:110,
    										    			    									width : 200,
    										    			    									value: 0,
    										    			    									useThousandSeparator: true,
    										    			                                        decimalPrecision: 2,
    										    			                                        alwaysDisplayDecimals: true,
    										    			                                        allowNegative: false,
    										    			                                        currencySymbol:'$',
    										    			                                        hideTrigger:true,
    										    			                                        thousandSeparator: ',',
    										    			                                        allowBlank : false,
    										    			                                        margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
    									    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptSubtotal21',
											    			    									itemId : 'conceptSubtotal21',
											    			    									id : 'conceptSubtotal21',
											    			    									hideLabel:true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitleIVAPRV,
											    			    									labelWidth:90,
											    			    									width : 90,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
    										    								            		listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															Ext.getCmp('conceptImport21').setValue(newValue);    										    															
    										    														}
    										    													}
    									    			    								},{
	    					        		    			    									xtype : 'combo',
	    					        		    			    									hideLabel:true,
	    					        		    			    									fieldLabel : SuppAppMsg.fiscalTitle24,
	    					        		    			    									id : 'taxCodeCombo21',
	    					        		    			    									itemId : 'taxCodeCombo21',
	    					        		    			    									name : 'taxCode21',
	    					        		    			    									store : getUDCStore('INVTAXCODE', '', '', ''),
	    					        		    			    									valueField : 'strValue1',
	    					        		    			    									displayField: 'udcKey',
	    					        		    			    									//emptyText : SuppAppMsg.purchaseTitle19,
	    					        		    			    									emptyText : 'MX0',
	    					        		    			    									typeAhead: true,
	    					        		    			    					                minChars: 1,
	    					        		    			    					                forceSelection: true,
	    					        		    			    					                triggerAction: 'all',
	    					        		    			    					                labelWidth:120,
	    					        		    			    					                width : 100,
	    					        		    			    									allowBlank : true,
	    					        		    			    									margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
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
										    			    								},//Maniobras2
										    			    								items : [{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptImport22',
											    			    									itemId : 'conceptImport22',
											    			    									id : 'conceptImport22',				    			    									
											    			    									fieldLabel : SuppAppMsg.fiscalTitleManeuvers,
											    			    									labelWidth:110,
											    			    									width : 200,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
    									    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptSubtotal22',
											    			    									itemId : 'conceptSubtotal22',
											    			    									id : 'conceptSubtotal22',
											    			    									hideLabel:true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitleManeuvers,
											    			    									labelWidth:90,
											    			    									width : 90,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
	    					        		    			    									listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															var tax = Ext.getCmp('taxCodeCombo22').getValue();
    										    															tax = +1 + +tax;
    										    															var subtotal = Ext.getCmp('conceptSubtotal22').getValue();
    										    															var total = subtotal*tax;
    										    															Ext.getCmp('conceptImport22').setValue(total);    										    															
    										    														}
    										    													}
    									    			    								},{
	    					        		    			    									xtype : 'combo',
	    					        		    			    									hideLabel:true,
	    					        		    			    									fieldLabel : SuppAppMsg.fiscalTitle24,
	    					        		    			    									id : 'taxCodeCombo22',
	    					        		    			    									itemId : 'taxCodeCombo22',
	    					        		    			    									name : 'taxCode22',
	    					        		    			    									store : getUDCStore('INVTAXCODE', '', '', ''),
	    					        		    			    									valueField : 'strValue1',
	    					        		    			    									displayField: 'udcKey',
	    					        		    			    									emptyText : SuppAppMsg.purchaseTitle19,
	    					        		    			    									typeAhead: true,
	    					        		    			    					                minChars: 1,
	    					        		    			    					                forceSelection: true,
	    					        		    			    					                triggerAction: 'all',
	    					        		    			    					                labelWidth:120,
	    					        		    			    					                width : 100,
	    					        		    			    									allowBlank : true,
	    					        		    			    									margin:'0 0 0 5',
	    					        		    			    									listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															var tax = Ext.getCmp('taxCodeCombo22').getValue();
    										    															tax = +1 + +tax;
    										    															var subtotal = Ext.getCmp('conceptSubtotal22').getValue();
    										    															var total = subtotal*tax;
    										    															Ext.getCmp('conceptImport22').setValue(total);    										    															
    										    														}
    										    													}
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
										    			    								},//Desconsolidación2
										    			    								items : [{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptImport23',
											    			    									itemId : 'conceptImport23',
											    			    									id : 'conceptImport23',				    			    									
											    			    									fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation,
											    			    									labelWidth:110,
											    			    									width : 200,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
    									    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptSubtotal23',
											    			    									itemId : 'conceptSubtotal23',
											    			    									id : 'conceptSubtotal23',
											    			    									hideLabel:true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitleDeconsolidation,
											    			    									labelWidth:90,
											    			    									width : 90,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
											    			                                        listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															var tax = Ext.getCmp('taxCodeCombo23').getValue();
    										    															tax = +1 + +tax;
    										    															var subtotal = Ext.getCmp('conceptSubtotal23').getValue();
    										    															var total = subtotal*tax;
    										    															Ext.getCmp('conceptImport23').setValue(total);    										    															
    										    														}
    										    													}
    									    			    								},{
	    					        		    			    									xtype : 'combo',
	    					        		    			    									hideLabel:true,
	    					        		    			    									fieldLabel : SuppAppMsg.fiscalTitle24,
	    					        		    			    									id : 'taxCodeCombo23',
	    					        		    			    									itemId : 'taxCodeCombo23',
	    					        		    			    									name : 'taxCode23',
	    					        		    			    									store : getUDCStore('INVTAXCODE', '', '', ''),
	    					        		    			    									valueField : 'strValue1',
	    					        		    			    									displayField: 'udcKey',
	    					        		    			    									emptyText : SuppAppMsg.purchaseTitle19,
	    					        		    			    									typeAhead: true,
	    					        		    			    					                minChars: 1,
	    					        		    			    					                forceSelection: true,
	    					        		    			    					                triggerAction: 'all',
	    					        		    			    					                labelWidth:120,
	    					        		    			    					                width : 100,
	    					        		    			    									allowBlank : true,
	    					        		    			    									margin:'0 0 0 5',
	    					        		    			    									listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															var tax = Ext.getCmp('taxCodeCombo23').getValue();
    										    															tax = +1 + +tax;
    										    															var subtotal = Ext.getCmp('conceptSubtotal23').getValue();
    										    															var total = subtotal*tax;
    										    															Ext.getCmp('conceptImport23').setValue(total);    										    															
    										    														}
    										    													}
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
										    			    								},//Otros1
										    			    								items : [{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptImport24',
											    			    									itemId : 'conceptImport24',
											    			    									id : 'conceptImport24',				    			    									
											    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1',
											    			    									labelWidth:110,
											    			    									width : 200,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
    									    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptSubtotal24',
											    			    									itemId : 'conceptSubtotal24',
											    			    									id : 'conceptSubtotal24',
											    			    									hideLabel:true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 1',
											    			    									labelWidth:90,
											    			    									width : 90,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
											    			                                        listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															var tax = Ext.getCmp('taxCodeCombo24').getValue();
    										    															tax = +1 + +tax;
    										    															var subtotal = Ext.getCmp('conceptSubtotal24').getValue();
    										    															var total = subtotal*tax;
    										    															Ext.getCmp('conceptImport24').setValue(total);    										    															
    										    														}
    										    													}
    									    			    								},{
	    					        		    			    									xtype : 'combo',
	    					        		    			    									hideLabel:true,
	    					        		    			    									fieldLabel : SuppAppMsg.fiscalTitle24,
	    					        		    			    									id : 'taxCodeCombo24',
	    					        		    			    									itemId : 'taxCodeCombo24',
	    					        		    			    									name : 'taxCode24',
	    					        		    			    									store : getUDCStore('INVTAXCODE', '', '', ''),
	    					        		    			    									valueField : 'strValue1',
	    					        		    			    									displayField: 'udcKey',
	    					        		    			    									emptyText : SuppAppMsg.purchaseTitle19,
	    					        		    			    									typeAhead: true,
	    					        		    			    					                minChars: 1,
	    					        		    			    					                forceSelection: true,
	    					        		    			    					                triggerAction: 'all',
	    					        		    			    					                labelWidth:120,
	    					        		    			    					                width : 100,
	    					        		    			    									allowBlank : true,
	    					        		    			    									margin:'0 0 0 5',
	    					        		    			    									listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															var tax = Ext.getCmp('taxCodeCombo24').getValue();
    										    															tax = +1 + +tax;
    										    															var subtotal = Ext.getCmp('conceptSubtotal24').getValue();
    										    															var total = subtotal*tax;
    										    															Ext.getCmp('conceptImport24').setValue(total);    										    															
    										    														}
    										    													}
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
										    			    								},//Otros2
										    			    								items : [{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptImport25',
											    			    									itemId : 'conceptImport25',
											    			    									id : 'conceptImport25',				    			    									
											    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2',
											    			    									labelWidth:110,
											    			    									width : 200,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
    									    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptSubtotal25',
											    			    									itemId : 'conceptSubtotal25',
											    			    									id : 'conceptSubtotal25',
											    			    									hideLabel:true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 2',
											    			    									labelWidth:90,
											    			    									width : 90,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
											    			                                        listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															var tax = Ext.getCmp('taxCodeCombo25').getValue();
    										    															tax = +1 + +tax;
    										    															var subtotal = Ext.getCmp('conceptSubtotal25').getValue();
    										    															var total = subtotal*tax;
    										    															Ext.getCmp('conceptImport25').setValue(total);    										    															
    										    														}
    										    													}
    									    			    								},{
	    					        		    			    									xtype : 'combo',
	    					        		    			    									hideLabel:true,
	    					        		    			    									fieldLabel : SuppAppMsg.fiscalTitle24,
	    					        		    			    									id : 'taxCodeCombo25',
	    					        		    			    									itemId : 'taxCodeCombo25',
	    					        		    			    									name : 'taxCode25',
	    					        		    			    									store : getUDCStore('INVTAXCODE', '', '', ''),
	    					        		    			    									valueField : 'strValue1',
	    					        		    			    									displayField: 'udcKey',
	    					        		    			    									emptyText : SuppAppMsg.purchaseTitle19,
	    					        		    			    									typeAhead: true,
	    					        		    			    					                minChars: 1,
	    					        		    			    					                forceSelection: true,
	    					        		    			    					                triggerAction: 'all',
	    					        		    			    					                labelWidth:120,
	    					        		    			    					                width : 100,
	    					        		    			    									allowBlank : true,
	    					        		    			    									margin:'0 0 0 5',
    										    								            		listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															var tax = Ext.getCmp('taxCodeCombo25').getValue();
    										    															tax = +1 + +tax;
    										    															var subtotal = Ext.getCmp('conceptSubtotal25').getValue();
    										    															var total = subtotal*tax;
    										    															Ext.getCmp('conceptImport25').setValue(total);    										    															
    										    														}
    										    													}
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
										    			    								},//Otros3
										    			    								items : [{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptImport26',
											    			    									itemId : 'conceptImport26',
											    			    									id : 'conceptImport26',
											    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3',
											    			    									labelWidth:110,
											    			    									width : 200,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
    										    					                                fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
    										    					                                readOnly: true
    									    			    								},{
											    			    									xtype: 'numericfield',
											    			    									name : 'conceptSubtotal26',
											    			    									itemId : 'conceptSubtotal26',
											    			    									id : 'conceptSubtotal26',
											    			    									hideLabel:true,
											    			    									fieldLabel : SuppAppMsg.fiscalTitleOther + ' 3',
											    			    									labelWidth:90,
											    			    									width : 90,
											    			    									value: 0,
											    			    									useThousandSeparator: true,
											    			                                        decimalPrecision: 2,
											    			                                        alwaysDisplayDecimals: true,
											    			                                        allowNegative: false,
											    			                                        currencySymbol:'$',
											    			                                        hideTrigger:true,
											    			                                        thousandSeparator: ',',
											    			                                        allowBlank : false,
											    			                                        margin:'0 0 0 5',
    										    								            		listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															var tax = Ext.getCmp('taxCodeCombo26').getValue();
    										    															tax = +1 + +tax;
    										    															var subtotal = Ext.getCmp('conceptSubtotal26').getValue();
    										    															var total = subtotal*tax;
    										    															Ext.getCmp('conceptImport26').setValue(total);    										    															
    										    														}
    										    													}
    									    			    								},{
	    					        		    			    									xtype : 'combo',
	    					        		    			    									hideLabel:true,
	    					        		    			    									fieldLabel : SuppAppMsg.fiscalTitle24,
	    					        		    			    									id : 'taxCodeCombo26',
	    					        		    			    									itemId : 'taxCodeCombo26',
	    					        		    			    									name : 'taxCode26',
	    					        		    			    									store : getUDCStore('INVTAXCODE', '', '', ''),
	    					        		    			    									valueField : 'strValue1',
	    					        		    			    									displayField: 'udcKey',
	    					        		    			    									emptyText : SuppAppMsg.purchaseTitle19,
	    					        		    			    									typeAhead: true,
	    					        		    			    					                minChars: 1,
	    					        		    			    					                forceSelection: true,
	    					        		    			    					                triggerAction: 'all',
	    					        		    			    					                labelWidth:120,
	    					        		    			    					                width : 100,
	    					        		    			    									allowBlank : true,
	    					        		    			    									margin:'0 0 0 5',
    										    								            		listeners:{
    										    														change: function(field, newValue, oldValue){
    										    															var tax = Ext.getCmp('taxCodeCombo26').getValue();
    										    															tax = +1 + +tax;
    										    															var subtotal = Ext.getCmp('conceptSubtotal26').getValue();
    										    															var total = subtotal*tax;
    										    															Ext.getCmp('conceptImport26').setValue(total);    										    															
    										    														}
    										    													}
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
    		    			    				    }],
    	    			
    	    			    						buttons : [{
    	    			    							text : SuppAppMsg.supplierLoad,
    	    			    							margin:'10 0 0 0',
    	    			    							handler : function() {
    	    			    								var form = this.up('form').getForm();
    	    			    								if (form.isValid()) {
    	    			    									form.submit({
    	    			    												url : 'uploadInvoiceWithoutOrder.action',
    	    			    												waitMsg : SuppAppMsg.supplierLoadFile,
    	    			    												success : function(fp, o) {
    	    			    													var res = Ext.decode(o.response.responseText);
    	    			    													me.winLoadInv.destroy();
    	    			    													if(me.receiptWindow){
    	    			    														me.receiptWindow.close();
    	    			    													}
    	    			    													Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.approvalInvRespUploaded});
    	    			    						
    	    			    													
    	    			    												},
    	    			    										        failure: function(fp, o) {
    	    			    										        	var res = o.response.responseText;
    	    			    										        	var result = Ext.decode(res);
    	    			    										        	var msgResp = result.message;

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
    	    			    										        	}else if(msgResp != "Error_9" && msgResp != "Error_10" && msgResp != "Error_11"){
    	    			    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  msgResp});
    	    			    										        	}
    	    			    										        }
    	    			    											});
    	    			    								}
    	    			    							}
    	    			    						}]
    	    			    					});

    	    			    	me.winLoadInv = new Ext.Window({
    	    			    		layout : 'fit',
    	    			    		title : SuppAppMsg.purchaseUploadInvoice,
    	    			    		width : 900,
    	    			    		height : me.isTransportCB==true?200:800,
    	    			    		modal : true,
    	    			    		closeAction : 'destroy',
    	    			    		resizable : false,
    	    			    		minimizable : false,
    	    			    		maximizable : false,
    	    			    		plain : true,
    	    			    		items : [ filePanel ]
    	    			
    	    			    	});
    	    			    	me.winLoadInv.show();
    	    			    	
    	    			    } else if(sup.data.country != null && sup.data.country != "") {
    	    			    	
    				    		me.winLoadInv = new Ext.Window({
    					    		layout : 'fit',
    					    		title : SuppAppMsg.purchaseUploadInvoiceForeing,
    					    		width : me.isTransportCB==true?515:1200,
    					    		height : 650,
    					    		modal : true,
    					    		closeAction : 'destroy',
    					    		resizable : false,
    					    		minimizable : false,
    					    		maximizable : false,
    					    		plain : true,
    					    		items : [ 
    					    				{
    					    				xtype:'foreignFiscalDocumentsForm'
    					    				} 
    					    			]
    					    	});
    				    		
    				    		//orderForm.findField('addressNumber').getValue(),
    				        	var foreignForm = me.getForeignFiscalDocumentsForm().getForm();
    				        	foreignForm.setValues({
    				        		addressNumber: supNumber,
    				        		name:  sup.data.razonSocial,
    				        		taxId:  sup.data.taxId,
    				        		address: sup.data.calleNumero + ", " + sup.data.colonia + ", " + sup.data.delegacionMncipio + ", C.P. " + sup.data.codigoPostal,
    				        		country:  sup.data.country,
    				        		foreignSubtotal:0,
    				        		attachmentFlag:''
    				        	});

    				    		setTimeout(function(){},2000); 
    				    		me.winLoadInv.show();
    			        		
    	    			    } else {
    	    			    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg:  SuppAppMsg.purchaseErrorNonSupplierWithoutOC});
    	    			    }
    	    		    },  
    			        failure: function(fp, o) {
    			        	box.hide();
    			        }
    	    		});
    		    },
    	        failure: function(fp, o) {
    	        	box.hide();
    	        }
    		});
    	}else{
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.purchaseOrdersMsg8 });
    	}

    },
    
    uploadForeignAdditional : function(button) {
    	var me = this;
        var form = this.getForeignFiscalDocumentsForm().getForm();
    	if (form.isValid()) {
    		var val = form.getFieldValues();
    		var filePanel = Ext.create(
					'Ext.form.Panel',
					{
						width : 900,
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
									value : val.foreignCompany
								},{
									xtype : 'textfield',
									name : 'currentUuid',
									hidden : true,
									value : val.currentUuid
								},{
									xtype : 'textfield',
									name : 'invoiceNumber',
									hidden : true,
									value : val.invoiceNumber
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
												url : 'uploadForeignAdditionalFD.action',
												waitMsg : SuppAppMsg.supplierLoadFile,
												success : function(fp, o) {
													var res = Ext.decode(o.response.responseText);
													Ext.getCmp('currentUuid').setValue(res.uuid);
													val.currentUuid = res.uuid;
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
																Ext.getCmp('fileListForeignHtmlFD').setValue(files);
																Ext.getCmp('attachmentFlagFD').setValue("ATTACH");																																
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
						me.winLoadInvFile.show();
    	}
        
    	
    	
    },
    
    sendForeignRecord : function(button) {
    	var me = this;
    	var form = this.getForeignFiscalDocumentsForm().getForm();
    	var attach = Ext.getCmp('attachmentFlagFD').getValue();
 
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
    		
			form.submit({
				url : 'supplier/orders/uploadForeignInvoiceWithoutOrder.action',
				waitMsg : SuppAppMsg.supplierProcessRequest,
				success : function(fp, o) {
					var res = Ext.decode(o.response.responseText);
					if(me.getForeignFiscalDocumentsForm().getForm()){
						me.getForeignFiscalDocumentsForm().getForm().reset();
					}
					me.winLoadInv.destroy();
					Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.approvalInvRespUploaded});					
				},
		        failure: function(fp, o) {
		        	var res = o.response.responseText;
		        	var result = Ext.decode(res);
		        	var msgResp = result.message;

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
		        	}else if(msgResp == "ForeignInvoiceError_9"){
		        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError9});
		        	}else if(msgResp == "ForeignInvoiceError_10"){
		        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError10});
		        	}else if(msgResp == "ForeignInvoiceError_11"){
		        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError11});
		        	}else if(msgResp == "ForeignInvoiceError_12"){
		        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.foreignInvoiceError12});
		        	}else{
		        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  msgResp});
		        	}		        	
		        }
			});
    	}
    },
    
    acceptSelInvFD : function(grid, record) {
    	var gridAccept = this.getAcceptInvGridFD();
    	var storeAccept = gridAccept.getStore();
    	
		storeAccept.each(function(rec) {
			if(rec){
		       if (rec.data.uuidFactura == record.data.uuidFactura) {
		    	   storeAccept.remove(rec)
		      }
			}
		});
		
		storeAccept.insert(0, record);
    },
    
    rejectSelInvFD : function(grid, record) {
    	var gridAccept = this.getAcceptInvGridFD();
    	var storeAccept = gridAccept.getStore();
		storeAccept.remove(record);
    },
    
    loadComplFileFD : function(grid, record) {
        var me = this;    	
    	var fiscalDocumentArray = [];
    	var gridAccept = this.getAcceptInvGridFD();
    	var storeAccept = gridAccept.getStore();
    	
    	var gridSelect = this.getSelInvGridFD();
    	var storeSelect = gridSelect.getStore();
    	
    	var supNumber = Ext.getCmp('supNumberFD').getValue() == ''?'':Ext.getCmp('supNumberFD').getValue();
		storeAccept.each(function(rec) {
			if(rec){
		       fiscalDocumentArray.push(rec.data.id);
			}
		});
		
		if(fiscalDocumentArray.length > 0){
			var filePanel = Ext.create(
					'Ext.form.Panel',
					{
						width : 900,
						items : [
								{
									xtype : 'textfield',
									name : 'addressBook',
									hidden : true,
									value : supNumber
			                    },{
									xtype : 'textfield',
									name : 'documents',
									hidden : true,
									value : fiscalDocumentArray
			                    },{
									xtype : 'filefield',
									name : 'file',
									fieldLabel : SuppAppMsg.purchaseFileXML +':',
									labelWidth : 80,
									msgTarget : 'side',
									allowBlank : false,
									margin:'30 0 70 20',
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
												url : 'uploadComplPagoFD.action',
												waitMsg : SuppAppMsg.supplierLoadFile,
												success : function(fp, o) {
													Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
													storeAccept.loadData([],false);
													storeSelect.load();
												},
										        failure: function(fp, o) {
										        	var res = o.response.responseText;
										        	var result = Ext.decode(res);
										        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  result.message});
										        }
											});
								}
							}
						} ]
					});

					this.winLoadInv = new Ext.Window({
						layout : 'fit',
						title : SuppAppMsg.purchaseOrdersTitle3,
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
		}else{
			Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.purchaseOrdersMsg6 });
		}
    },
    
    fdSearch: function(button) {
    	
    	var grid = this.getFreightApprovalGrid();
    	var store = grid.getStore();
    	
    	var ctaPresupuestalFA = Ext.getCmp('ctaPresupuestalFA').getValue();
    	var semanaPagoFA = Ext.getCmp('semanaPagoFA').getValue();
    	var invoiceStatus = Ext.getCmp('comboInvoiceStatusFA').getValue();
    	var batchIdParam= Ext.getCmp('batchIdParam').getValue();

    	store.removeAll();    	
    	store.proxy.extraParams = {
    			accountingAccount : ctaPresupuestalFA?ctaPresupuestalFA:"",
    			status : invoiceStatus?invoiceStatus:"",
    					batchIdParam:batchIdParam?batchIdParam:"",
    							semanaPago:semanaPagoFA?semanaPagoFA:""		
    	    			        }
    	store.loadPage(1);
    	grid.getView().refresh()
    	/*
    	gblMassiveLoadEx = getUDCStore('MASSIVEUP', '', '', '');
    	gblMassiveLoadEx.load();*/
    },
    
    uploadAdditional : function(grid, rowIndex, colIndex, record) {
    	
    	var record = grid.store.getAt(rowIndex);
        var me = this;
    	var filePanel = Ext.create(
    					'Ext.form.Panel',
    					{
    						width : 900,
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
    									xtype : 'textfield',
    									name : 'fdId',
    									hidden : true,
    									value : record.data.id
    								},
    								{
    			                        xtype: 'radiogroup',
    			                        labelWidth: 60,
    			                        fieldLabel: 'Tipo',
    			                        
    			                        //arrange Radio Buttons into 2 columns
    			                        columns: 1,
    			                        itemId: 'invType',
    			                        margin:'20 20 20 5',
    			                        items: [
    			                            
    			                            {
    			                                xtype: 'radiofield',
    			                                labelWidth: 70,
    			                                checked: true,
    			                                boxLabel: 'Complemento de pago xml',
    			                                name: 'tipoComprobante',
    			                                inputValue: 'ComplementoPago'
    			                            },{
    			                                xtype: 'radiofield',
    			                                labelWidth: 70,
    			                                boxLabel: 'Nota de crédito xml',
    			                                name: 'tipoComprobante',
    			                                inputValue: 'NotaCredito'
    			                            },
    			                            {
    			                                xtype: 'radiofield',
    			                                labelWidth: 70,
    			                                boxLabel: 'Otros (.pdf o .jpg)',
    			                                name: 'tipoComprobante',
    			                                inputValue: 'Otros'
    			                            }
    			                        ]
    			                    },
    								{
    									xtype : 'filefield',
    									name : 'file',
    									fieldLabel : 'Archivo(xml):',
    									labelWidth : 120,
    									msgTarget : 'side',
    									allowBlank : false,
    									margin:'0 0 70 0',
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
    												url : 'uploadInvoice.action',
    												waitMsg : SuppAppMsg.supplierLoadFile,
    												success : function(fp, o) {
    													var res = Ext.decode(o.response.responseText);
    													Ext.Ajax.request({
    														url : 'documents/listDocumentsByOrder.action',
    														method : 'GET',
    															params : {
    																start : 0,
    																limit : 20,
    																orderNumber : res.orderNumber,
    																orderType : res.orderType,
    																addressNumber : res.addressNumber
    															},
    															success : function(response,opts) {
    																response = Ext.decode(response.responseText);
    																var index = 0;
    																var files = "";
    																for (index = 0; index < response.data.length; index++) {
    										                            files = files + "> " + response.data[index].name + " - " + response.data[index].size + " bytes : " + response.data[index].fiscalType + "<br />";
    																} 
    																      
    																 Ext.getCmp('fileListHtml').setValue(files);
    															},
    															failure : function() {
    															}
    														});
    													   Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
    													
    													me.winLoadInv.close();
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
    		title : SuppAppMsg.fiscalTitle2 ,
    		width : 600,
    		height : 260,
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

    fdLoadCompl: function(button) {    	
    	var supNumber = Ext.getCmp('supNumberFD').getValue() == ''?'':Ext.getCmp('supNumberFD').getValue();

    	if(supNumber != ""){
        	new Ext.Window({
        		  width        : 1120,
        		  height       : 465,
        		  title        : 'Complementos de Pago',
        		  border       : false,
    	      		modal : true,
    	    		closeAction : 'destroy',
    	    		resizable : false,
    	    		minimizable : false,
    	    		maximizable : false,
    	    		plain : true,
        		  items : [
        			  {
        	    			xtype : 'complementoPagoPanelFD',
        	    			border : true,
        	    			height : 460
        	    		}
        			  ]
        		}).show();
    		
    		var gridSel = this.getSelInvGridFD();
        	var gridAccept = this.getAcceptInvGridFD();
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
    	
    }

});


