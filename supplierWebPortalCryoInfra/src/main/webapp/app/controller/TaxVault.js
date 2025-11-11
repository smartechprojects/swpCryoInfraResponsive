Ext.define('SupplierApp.controller.TaxVault', {
    extend: 'Ext.app.Controller',
    stores: ['TaxVault'],
    models: ['TaxVault','TaxVaultDetail','TaxVaultFile'],
    views: ['taxVault.TaxVaultPanel','taxVault.TaxVaultGrid',
    	'taxVault.TaxVaultForm','taxVault.TaxVaultDetailPanel',
    	'taxVault.TaxVaultDetailGrid','taxVault.TaxVaultFileGrid','taxVault.TaxVaultWorkerFileGrid','taxVault.TaxVaultFiscalPeriodPanel',
    	'taxVault.TaxVaultFiscalPeriodForm'],
    refs: [
	    {
	        ref: 'taxVaultGrid',
	        selector: 'taxVaultGrid'
	    },   
	    {
	        ref: 'taxVaultDetailGrid',
	        selector: 'taxVaultDetailGrid'
	    },
	    {
	        ref: 'taxVaultForm',
	        selector: 'taxVaultForm'
	    },
	    {
	        ref: 'taxVaultFileGrid',
	        selector: 'taxVaultFileGrid'
	    },{
	        ref: 'taxVaultWorkerFileGrid',
	        selector: 'taxVaultWorkerFileGrid'
	    },{
	        ref: 'taxVaultFiscalPeriodPanel',
	        selector: 'taxVaultFiscalPeriodPanel'
	    },{
	        ref: 'taxVaultFiscalPeriodForm',
	        selector: 'taxVaultFiscalPeriodForm'
	    }],
 
    init: function() {
		this.winLoadInv = null;
		this.winLoadInvFile = null;
		this.winDetail = null;
        this.control({
	            'taxVaultGrid': {
	            	itemdblclick: this.gridSelectionChange
	            },
				'taxVaultGrid button[action=uploadNewFiscalDoc]' : {
					click : this.invLoad
				},
				'taxVaultDocumentsForm button[action=uploadForeignDocsFD]' : {
					click: this.uploadForeignAdditional
				},
				'taxVaultDocumentsForm button[action=sendForeignRecordFD]' : {
					click: this.sendForeignRecord
				},
				'#upFileExtTaxVault' : {
					"buttonclick" : this.upFileExtTaxVault
				},
				'#approveTaxVault' : {
					"buttonclick" : this.approveTaxVault
				},
				'#eliminarTaxVaultDocument' : {
					"buttonclick" : this.eliminarTaxVaultDocument
				},
				'#reenvioMailTaxDocument' : {
					"buttonclick" : this.reenvioMailTaxDocument
				},
				'#acceptSelInvFD' : {
					"buttonclick" : this.acceptSelInvFD
				},
				'#rejectSelInvFD' : {
					"buttonclick" : this.rejectSelInvFD
				},
        		'#upfileWork' : {
			     "buttonclick" : this.upfileWork
		        },
				'acceptInvGridFD button[action=loadComplFileFD]' : {
					click: this.loadComplFileFD
				},
				'taxVaultGrid button[action=parSearch]' : {
					click : this.parSearch
				},
				'taxVaultGrid button[action=addNewTaxVaultRequest]' : {
					click : this.addNewTaxVaultRequest
				},
				'taxVaultGrid button[action=addTaxvaultNewcomplement]' : {
					click : this.addTaxvaultNewcomplement
				},
				'taxVaultGrid button[action=poUploadCreditNoteFile]' : {
					click : this.poUploadCreditNoteFile
				},
				'taxVaultGrid button[action=fdLoadCompl]' : {
					click : this.fdLoadCompl
				},
				'taxVaultGrid button[action=addFiscalPeriodTaxVAult]' : {
					click : this.addFiscalPeriodTaxVAult
				},
				'taxVaultDetailGrid button[action=addTaxVaultRequestEmployBtnAct]' : {
					click : this.addTaxVaultRequestEmployBtnAct
				},'taxVaultFileGrid button[action=addFileTaxVaultRequestBtnAct]' : {
					click : this.addFileTaxVaultRequestBtnAct
				},'taxVaultForm button[action=uploadTaxVaultRequestAct]' : {
					click : this.uploadTaxVaultRequestAct
				},'taxVaultFiscalPeriodForm button[action=addeTaxVaultFiscPerAct]' : {
					click : this.addeTaxVaultFiscPerAct
				},
				'taxVaultDetailGrid' : {
					itemdblclick: this.openReceiptForm
				},
        });
    },
    
    gridSelectionChange: function(model, record) {
        if (record) {
        	var me = this;  
        	this.winDetail = new Ext.Window({
        		layout : 'fit',
        		title : SuppAppMsg.fiscalTitle1,
        		//width : 1180,
        		//height : 200,
        		width: Ext.Element.getViewportWidth() * 0.45,   // 👈 40% de pantalla
                minWidth: 680,                                // 👈 ancho mínimo
                height: Ext.Element.getViewportHeight() * 0.2, // 👈 40% de alto
                minHeight: 200,
        		modal : true,
        		closeAction : 'destroy',
        		resizable : false,
        		minimizable : false,
        		maximizable : false,
        		plain : true,
        		items : [ {
        			xtype : 'taxVaultDetailPanel',
        			border : true,
        			height : 415
        		}  ]

        	});
        	 Ext.getCmp('rfcEmisorDet').setValue(record.data.rfcEmisor);
        		 Ext.getCmp('rfcReceptorDet').setValue(record.data.rfcReceptor);
        			 Ext.getCmp('usuarioDet').setValue(record.data.usuario);
        	this.winDetail.show();  
        	reloaddocsTaxVault(record.data.id);
        
        }
    }, addFiscalPeriodTaxVAult: function() {
        
        	
        	var me = this;   	

        	this.winDetail = new Ext.Window({
        		layout : 'fit',
        		title : SuppAppMsg.taxvaultFiscalPeriods,
        		//width : 1180,
        		//height : 200,
        		modal : true,
        		scrollable: true,
        		closeAction : 'destroy',
        		resizable : false,
        		minimizable : false,
        		maximizable : false,
        		width: Ext.Element.getViewportWidth() * 0.4,   // 60% pantalla
        	    height: Ext.Element.getViewportHeight() * 0.3, // 50% pantalla
        	    minWidth: 680,                                // 👈 ancho mínimo
                minHeight: 250,
        		plain : true,
        		items : [ {
        			xtype : 'taxVaultFiscalPeriodPanel',
        			border : true,
        			//height : 415
        		}  ]

        	});
//        	 Ext.getCmp('rfcEmisorDet').setValue(record.data.rfcEmisor);
//        		 Ext.getCmp('rfcReceptorDet').setValue(record.data.rfcReceptor);
//        			 Ext.getCmp('usuarioDet').setValue(record.data.usuario);
        	
        	this.winDetail.show();  
        	reloadPeriodoFiscal();
        
        
    },
    
    approveTaxVault : function(grid, rowIndex, colIndex, record) {
    	
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var dto = Ext.create('SupplierApp.model.TaxVault',record.data);
    	var status ="APROBADO";
    	
    	var filePanel = Ext.create(
				'Ext.form.Panel',
				{
					width: 510,
					items : [{
								fieldLabel : SuppAppMsg.taxvaultNotes,
								xtype : 'textfield',
								name : 'notesTaxVault',
								id : 'notesTaxVault',
								width:500,
								colspan:3,
								margin:'10 0 0 10'
							}],

					buttons : [{
						text : 'Aprobar',
						margin:'0 5 0 0',
						handler : function() {
							var form = this.up('form').getForm();
							if (form.isValid()) {
								
								var note = Ext.getCmp('notesTaxVault').getValue();
								
								Ext.MessageBox.show({
									title : SuppAppMsg.approvalInvRespTittle,
									msg : SuppAppMsg.approvalInvRespMessage,
									buttons : Ext.MessageBox.YESNO,
									width:100,
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
												    url: 'taxVault/updateAprov.action',
												    method: 'POST',
												    params: {
												    	
												    	status:status,
												    	note:note,
												    	documentType: record.data.type,
												    	idReques:record.data.id
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
    		width : 550,
    		height : 100,
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
    
    upfileWork : function(grid, rowIndex, colIndex, record) {
    	 recordWorker = grid.store.getAt(rowIndex).data;
    
    	
    },
    
    eliminarTaxVaultDocument : function(grid, rowIndex, colIndex, record) { 	
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var dto = Ext.create('SupplierApp.model.TaxVault',record.data);

    	var dlgRejected = Ext.MessageBox.show({
    		title : SuppAppMsg.taxvaultDeleteInvoice,
			msg :SuppAppMsg.taxvaultReasonRemoval,
			buttons : Ext.MessageBox.YESNO,
			multiline: true,
			//width:500,
			width: Math.min(Ext.Element.getViewportWidth() * 0.3, 500), 
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
						var notes = text;
						Ext.Ajax.request({
						    url: 'taxVault/deleteTaxVaultDocument.action',
						    method: 'POST',
						    params: {
						    	note:notes
					        },
						    jsonData: dto.data,
						    success: function(fp, o) {
						    	
						    	var res = Ext.decode(fp.responseText);
						    	grid.store.load();
						    	box.hide();
						    	
						    },
						    failure: function() {
						    	box.hide();
						    	/*Ext.MessageBox.show({
					                title: 'Error',
					                msg: SuppAppMsg.approvalUpdateError,
					                buttons: Ext.Msg.OK
					            });*/
						    	Ext.Msg.show({
			                        title: 'Error',
			                        msg: SuppAppMsg.approvalUpdateError,
			                        buttons: Ext.Msg.OK,
			                        width: Math.min(Ext.Element.getViewportWidth() * 0.3, 350)
			                    });
						    	
						    	Ext.defer(function() {
						    	    var activeMsg = Ext.WindowManager.getActive();
						    	    if (activeMsg) {
						    	        var buttons = activeMsg.query('toolbar button');
						    	        if (buttons.length > 0) {
						    	            buttons[0].addCls('buttonStyle');
						    	        }
						    	    }
						    	}, 100);
						    }
						}); 
						
						
					}else{
            			//Ext.Msg.alert(SuppAppMsg.approvalAlert, SuppAppMsg.approvalMessages);
						Ext.Msg.show({
	                        title: SuppAppMsg.approvalAlert,
	                        msg: SuppAppMsg.approvalMessages,
	                        buttons: Ext.Msg.OK,
	                        width: Math.min(Ext.Element.getViewportWidth() * 0.3, 350)
	                    });
						
						Ext.defer(function() {
						    var activeMsg = Ext.WindowManager.getActive();
						    if (activeMsg) {
						        var buttons = activeMsg.query('toolbar button');
						        if (buttons.length > 0) {
						            buttons[0].addCls('buttonStyle');
						        }
						    }
						}, 100);
            		}

				}
			}
		});
    	
    	  // Aplicar el estilo a los botones YES y NO después de que se renderice el diálogo
        Ext.defer(function() {
            var activeMsg = Ext.WindowManager.getActive();
            if (activeMsg) {
                var buttons = activeMsg.query('toolbar button');
                if (buttons.length > 0) {
                    // Aplicar la clase a todos los botones (YES y NO)
                    buttons.forEach(function(button) {
                        button.addCls('buttonStyle');
                    });
                }
            }
        }, 10);
    },
    reenvioMailTaxDocument : function(grid, rowIndex, colIndex, record) {
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var dto = Ext.create('SupplierApp.model.TaxVault',record.data);
    	
    	Ext.Ajax.request({
		    url: 'taxVault/reenvioMailTaxDocument.action',
		    method: 'POST',
		    jsonData: dto.data,
		    success: function(fp, o) {
		    	
		    	var res = Ext.decode(fp.responseText);
		    	Ext.MessageBox.show({
	                title: SuppAppMsg.taxvaultSuccess,
	                msg: SuppAppMsg.taxvaultSuccessfulForwarding,
	                buttons: Ext.Msg.OK,
	                width: Math.min(Ext.Element.getViewportWidth() * 0.3, 350)
	            });
		    	
		    	Ext.defer(function() {
		    	    var activeMsg = Ext.WindowManager.getActive();
		    	    if (activeMsg) {
		    	        var buttons = activeMsg.query('toolbar button');
		    	        if (buttons.length > 0) {
		    	            buttons[0].addCls('buttonStyle');
		    	        }
		    	    }
		    	}, 100);
		    	
		    },
		    failure: function() {
		    	box.hide();
		    	Ext.MessageBox.show({
	                title: 'Error',
	                msg: SuppAppMsg.approvalUpdateError,
	                buttons: Ext.Msg.OK,
	                width: Math.min(Ext.Element.getViewportWidth() * 0.3, 350)
	            });
		    	
		    	Ext.defer(function() {
		    	    var activeMsg = Ext.WindowManager.getActive();
		    	    if (activeMsg) {
		    	        var buttons = activeMsg.query('toolbar button');
		    	        if (buttons.length > 0) {
		    	            buttons[0].addCls('buttonStyle');
		    	        }
		    	    }
		    	}, 100);
		    }
		}); 
    },
    
    upFileExtTaxVault : function(grid, rowIndex, colIndex, record) {
    	record=grid.store.getAt(rowIndex);
        var me = this;
    	var filePanel = Ext.create(
    					'Ext.form.Panel',
    					{
    						//width : 900,
    						bodyPadding: 10,
    				        layout: 'anchor',
    				        scrollable: true, 
    				        defaults: {
    				            anchor: '100%',
    				            margin: '10 0'
    				        },
    						items : [
    						        {
    									xtype : 'textfield',
    									name : 'fdId',
    									hidden : true,
    									value : record.data.id
    								},{
    									xtype : 'filefield',
    									name : 'file',
    									fieldLabel : SuppAppMsg.taxvaultFile+':',
    									//labelWidth : 120,
    									msgTarget : 'side',
    									allowBlank : false,
    									//margin:'15 0 70 0',
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
    												url : 'taxVault/upFileExtTaxVault.action',
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
    		title : SuppAppMsg.taxvaultAdditionalInformation,
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
    					    				xtype:'taxVaultDocumentsForm'
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
    
    parSearch: function(button) {
    	var grid = this.getTaxVaultGrid();
    	var store = grid.getStore();
    	
    	var rfcReceptor = Ext.getCmp('companyBF').getValue();
    	var rfcEmisor = Ext.getCmp('rfcEmisor').getValue();
    	var tvUUID = Ext.getCmp('tvUUID').getValue();
    	var tvFromDate = Ext.getCmp('tvFromDate').getValue();
    	var tvToDate = Ext.getCmp('tvToDate').getValue();
    	var comboType = Ext.getCmp('comboType').getValue();
    	
    	if(tvFromDate == null || tvToDate == null){
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: window.navigator.language.startsWith("es", 0)? 'Debe seleccionar fecha de inicio y final para consultar':'strValue2' });
    		return false;
    	}

    	store.removeAll();    	
    	store.proxy.extraParams = {
    			rfcReceptor : rfcReceptor?rfcReceptor:"",
    			rfcEmisor : rfcEmisor?rfcEmisor:"",
    			tvUUID : tvUUID?tvUUID:"",
    			tvFromDate : tvFromDate?tvFromDate:"",
    			tvToDate : tvToDate?tvToDate:"",
    			comboType : comboType?comboType:""	
    	}
    	store.loadPage(1);
    	grid.getView().refresh()
    },
    
    addNewTaxVaultRequest : function(button) {
//    	var record = grid.store.getAt(rowIndex);
    	var record={data:{
    		orderNumber:"1",
    		orderType:"2",
    		id:"3"
    		
    	}}
    	var grid = this.getTaxVaultGrid();
    	var store = grid.getStore();
        var me = this;
       
    	var filePanel = Ext.create(
    					'Ext.form.Panel',
    					{
    						bodyPadding: 10,
    				        layout: 'anchor',
    				        scrollable: true, 
    				        defaults: {
    				            anchor: '100%',
    				            margin: '10 0'
    				        },
    						items : [ {
    							xtype : 'filefield',
    							name : 'uploadedFiles',
    							fieldLabel : SuppAppMsg.taxvaultFile+':',
    							//labelWidth : 70,
    							msgTarget : 'side',
    							allowBlank : false,
    							//width:300,
    							buttonText : SuppAppMsg.taxvaultFile,
    							buttonConfig: {
									cls: 'buttonStyle'
							    },
    							//margin:'10 0 10 10',
    							listeners:{
    						        afterrender:function(cmp){
    						            cmp.fileInputEl.set({
    						                multiple:'multiple'
    						            });
    						        }
    						    }
//    							listeners: {
//    					            'change': function(f, value){
//    					                  alert(f.size); // not filesize
//    					            }}
    						}  ]
    							, 

    							buttons : [ {
    								text : SuppAppMsg.supplierLoad,
    								cls: 'buttonStyle',
    								margin:'10 0 0 0',
    								handler : function() {
    									var form = this.up('form').getForm();
    									if (form.isValid()) {
    										form.submit({
    													url : 'taxVault/uploadInvoiceTaxVault.action?',
    													waitMsg : SuppAppMsg.supplierLoadFile,
    													success : function(fp, o) {
    														var res = Ext.decode(o.response.responseText);
    														mensaj= res.info.mensaje[getLanguaje()];
    														grid.store.load();
    															Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: mensaj==undefined?res.info.code:mensaj});
    															winLoadInv.close()
    													},       // If you don't pass success:true, it will always go here
    											        failure: function(fp, o) {
    											        	var res = Ext.decode(o.response.responseText);
    											        	mensaj= res.info.mensaje[getLanguaje()];
    											        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:mensaj==undefined?res.info.code:mensaj });
    											        }
    												});
    									}
    								}
    							} ]
    					});


    	winLoadInv = new Ext.Window({
    		layout : 'fit',
    		title : window.navigator.language.startsWith("es", 0)? 'Cargar documento':'Upload document',
    		name:'CargaXML',
    		modal : true,
    		id: 'addnameRequestwindow',
	        itemId: 'addnameRequestwindow',
    		closeAction : 'destroy',
    		resizable : false,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		width: Ext.Element.getViewportWidth() * 0.35,   // 👈 40% de pantalla
            maxWidth: 320,                                // 👈 ancho mínimo
            height: Ext.Element.getViewportHeight() * 0.35, // 👈 40% de alto
            maxHeight: 150,
    		items : [ filePanel ]

    	});
//      Ext.getCmp('file').fileInputEl.set({multiple: true});   //  set fileuploadfield to multiple
    	winLoadInv.show();
    },
    
    addTaxvaultNewcomplement: function(button) {
    	
//    	var record = grid.store.getAt(rowIndex);
    	var record={data:{
    		orderNumber:"1",
    		orderType:"2",
    		id:"3"
    		
    	}}
    	var grid = this.getTaxVaultGrid();
    	var store = grid.getStore();
        var me = this;
    	var filePanel = Ext.create(
    					'Ext.form.Panel',
    					{
    						items : [ {
    							xtype : 'filefield',
    							name : 'uploadedFiles',
    							fieldLabel : SuppAppMsg.taxvaultFile+':',
    							labelWidth : 70,
    							msgTarget : 'side',
    							allowBlank : false,
    							width:300,
    							buttonText : SuppAppMsg.taxvaultFile,
    							margin:'10 0 10 10',
    								 multiple: true
//    							listeners: {
//    					            'change': function(f, value){
//    					                  alert(f.size); // not filesize
//    					            }}
    						}  ]
    							, 

    							buttons : [ {
    								text : SuppAppMsg.supplierLoad,
    								margin:'10 0 0 0',
    								handler : function() {
    									var form = this.up('form').getForm();
    									if (form.isValid()) {
    										form.submit({
    													url : 'taxVault/uploadInvoiceTaxVault.action?origen=COMPLEMENTO',
    													waitMsg : SuppAppMsg.supplierLoadFile,
    													success : function(fp, o) {
    														var res = Ext.decode(o.response.responseText);
    														mensaj= res.info.mensaje[getLanguaje()];
    														grid.store.load();
    															Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: mensaj==undefined?res.info.code:mensaj});
    															winLoadInv.close()
    													},       // If you don't pass success:true, it will always go here
    											        failure: function(fp, o) {
    											        	var res = Ext.decode(o.response.responseText);
    											        	mensaj= res.info.mensaje[getLanguaje()];
    											        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:mensaj==undefined?res.info.code:mensaj });
    											        }
    												});
    									}
    								}
    							} ]
    					});

    	winLoadInv = new Ext.Window({
    		layout : 'fit',
    		title : SuppAppMsg.taxvaultUploandComplement ,
    		name:'CargaXML',
    		modal : true,
    		 id: 'addnameRequestwindow',
	            itemId: 'addnameRequestwindow',
    		closeAction : 'destroy',
    		resizable : false,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		items : [ filePanel ]

    	});
    	winLoadInv.show();
    },
    addTaxVaultRequestEmployBtnAct:function(button) {
    
    	 row = Ext.create('SupplierApp.model.TaxVaultDetail', 
    			   {
    			      id:0,
    				employeeName:Ext.getCmp('addemployeename').getValue(),
    					membershipIMSS:Ext.getCmp('addmembershipIMSS').getValue(),
    				datefolioIDcard:Ext.getCmp('addDatefolioIDcard').getValue()
    			    });
    	 
    	 Ext.Ajax.request({
 		    url: 'taxVault/uploadWorker.action',
 		    method: 'POST',
 		    params:  {
  				employeeName:Ext.getCmp('addemployeename').getValue(),
  					membershipIMSS:Ext.getCmp('addmembershipIMSS').getValue(),
  				datefolioIDcard:Ext.getCmp('addDatefolioIDcard').getValue()
  			    },
 		    success: function(fp, o) {
 		    	var res = Ext.decode(fp.responseText);
 		    	var sup = Ext.create('SupplierApp.model.TaxVaultDetail',res.data);
 		    	
    	store.add(sup);
    }})
    	 
    	 
    	 
    			
    			
    			Ext.getCmp('addemployeename').setValue('');
    			Ext.getCmp('addmembershipIMSS').setValue('');
    			Ext.getCmp('addDatefolioIDcard').setValue('');
    	
    	
    }, 
    uploadTaxVaultRequestAct:function(button) {
    	//No se usa funcion
    	Ext.Ajax.request({
		    url: 'taxVault/uploadTaxVaultRequest.action',
		    method: 'POST',
		    params: {
			  status:'PENDIENTE',
			  nameRequest:Ext.getCmp('addnameRequest').getValue(),
		      ordenNumber:Ext.getCmp('addnordenNumber').getValue(),
			  contractorCompany:Ext.getCmp('addcontractorCompany').getValue(),
			  contractorRepresentative:Ext.getCmp('addcontractorRepresentative').getValue(),
			  descriptionUbication:Ext.getCmp('adddescriptionUbication').getValue(),
			  aprovUser:Ext.getCmp('addAproval').getValue(),
			  highRiskActivities:"0"+((Ext.getCmp('addworkatheights').getValue()?",WORKATHEIGHTS":"")
						+(Ext.getCmp('addHeavyequipment').getValue()?",HEAVYEQUIPMENT":"")
						+(Ext.getCmp('addconfinedspaces').getValue()?",CONFINEDSPACES":"")
						+(Ext.getCmp('addcontelectricworks').getValue()?",CONTELECTRICWORKS":"")
						+(Ext.getCmp('addworkhots').getValue()?",WORKHOTS":"")
						+(Ext.getCmp('addchemicalsubstances').getValue()?",CHEMICALSUBSTANCES":""))
		    					 
		    	
	        },
		    success: function(fp, o) {
		    	dote=Ext.getCmp('addnameRequestwindow');
		    	var res = Ext.decode(fp.responseText);
		    	var sup = Ext.create('SupplierApp.model.Supplier',res.data);
		    
		    	dote.hide();
        
   	
   	
   }})}, 
   addeTaxVaultFiscPerAct:function(button) {
    	Ext.Ajax.request({
		    url: 'taxVault/addePeriodoFiscal.action',
		    method: 'POST',
		    params: {periodoYear:Ext.getCmp('poFromDateTaxVault').getValue()},
		    success: function(fp, o) {
		    	response = Ext.decode(fp.responseText)
		    	if(response.success){
		    			reloadPeriodoFiscal();
		    
		    	dote.hide();
		    	}else{
		    		//Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: response.message });
		    		
		    		Ext.MessageBox.show({
		    		    title: SuppAppMsg.supplierMsgValidationLoad,
		    		    msg: response.message,
		    		    buttons: Ext.MessageBox.OK,
		    		   // icon: Ext.MessageBox.INFO,
		    		    width: Math.min(Ext.Element.getViewportWidth() * 0.8, 600) // 80% pantalla, máx 600px
		    		});
		    	}
		    
        
   	
   	
   }})}, 
    addFileTaxVaultRequestBtnAct:function(button) {
    	Ext.Ajax.request({
		    url: 'supplier/getByAddressNumber.action',
		    method: 'POST',
		    params: {		    	
		    	fileUp :Ext.getCmp('faddFileAccesRequest').getValue(),
		    	documentType:Ext.getCmp('addRequestDocumentType').getValue()
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
		    	
        
   	 row = Ext.create('SupplierApp.model.TaxVaultFile', 
   			   {
   			      id:0,
   			   documentType:Ext.getCmp('addRequestDocumentType').getValue(),
   					membershipIMSS:Ext.getCmp('file').getValue()
   			    });
   	storeFile.add(row);
   			
   			Ext.getCmp('addRequestDocumentType').setValue('');
   			Ext.getCmp('faddFileAccesRequest').setValue('');
   	
   	
   }})},

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


