Ext.define('SupplierApp.controller.Supplier', {
    extend: 'Ext.app.Controller',
    stores: ['Supplier'],
    models: ['SupplierDTO','Supplier'],
    views: ['supplier.SupplierForm','supplier.SupplierGrid','supplier.SupplierPanel',
            'supplier.SupplierPanelDetail'],
    refs: [{
        	ref: 'supplierForm',
        	selector: 'supplierForm'
	    },{
        	ref: 'supplierGrid',
        	selector: 'supplierGrid'
	    }],
  
    init: function() {
    	this.winLoadInv = null;
    	this.supDetailWindow = null;
    	this.winLoadSupplier = null;
        this.control({
			'supplierForm button[action=sendSupplierForm]' : {
				click : this.saveForm
			},
			'supplierForm button[action=updateSupplierForm]' : {
				click : this.updateForm
			},
			'supplierForm button[action=updateSupplierFormDraft]' : {
				click : this.updateFormDraft
			},
			'supplierForm button[action=updateEmailSupplierForm]' : {
				click : this.updateEmailForm
			},
			'supplierForm button[action=searchTicket]' : {
				click : this.searchTicket
			},
			'supplierForm button[action=loadRfcDoc]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=loadDomDoc]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=loadEdoDoc]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=loadEscDoc]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=loadNotDoc]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=loadIdentDoc]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=loadCertDoc]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=loadActaConst]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=loadRpcDocument]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=loadlegalExistence]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=loadForeingResidence]' : {
				click : this.loadRfcDoc
			},
            'supplierGrid': {
            	itemdblclick: this.gridSelectionChange
            },
			'supplierGrid button[action=supAddNbrSrch]' : {
				click : this.supAddNbrSrch
			},
			'supplierGrid button[action=uploadSuppliersFile]' : {
				click : this.uploadSuppliersFile
			},
			'supplierGrid button[action=replicateSupplier]' : {
				click : this.replicateSupplier
			},
			'#disableSupplier' : {
				"buttonclick" : this.disableSupplier
			},
			'supplierForm button[action=loadSTPS]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=loadIMSS]' : {
				click : this.loadRfcDoc
			},
			'supplierForm button[action=updateCatCodeSuppForm]' : {
				click : this.updateCatCodeSuppForm
			}
        });
    },
    loadInfoSupplier:function(){
    },
    getInvalidFields: function() {
        var invalidFields = [];
        Ext.suspendLayouts();
        this.getSupplierForm().getForm().getFields().filterBy(function(field) {
            if (field.validate()) return;
            invalidFields.push(field);
        });
        Ext.resumeLayouts(true);
        return invalidFields;
    },
    updateEmailForm : function(button) {
    	var form = this.getSupplierForm().getForm();
    		values = form.getFieldValues();
    		var id = values.id;
    		var me = this;
    		if (values.id = 0) {
    			Ext.Msg.alert(
    					SuppAppMsg.supplierUpdateFail,
    							"Error");
    		} else {
    			var box = Ext.MessageBox.wait(
    							SuppAppMsg.supplierProcessRequest,
    							SuppAppMsg.approvalExecution);
    			var emailSupplier = values.emailSupplier;
    			
    			Ext.Ajax.request({
				    url: 'supplier/updateEmailSupplier.action',
				    method: 'GET',
				    params: {
				    	idSupplier:id,
				    	emailSupplier:emailSupplier
			        },
				    success: function(fp, o) {
				    	var res = Ext.decode(fp.responseText); 
				    	box.hide();
				    	if(res.message == "Succ Update Email"){
				    		Ext.Msg.alert(SuppAppMsg.approvalResponse, 'Actualizacion de correo exitosa');
				    	}else if(res.message == "Fail Update Email"){
				    		Ext.Msg.alert(SuppAppMsg.approvalResponse, 'Surgio un error al actualizar el correo');
				    	}
			        	
				    },
				    failure: function() {
				    	box.hide();
				    	Ext.MessageBox.show({
			                title: 'Error',
			                msg: 'Surgio un error al actualizar el correo',
			                buttons: Ext.Msg.OK
			            });
				    }
				}); 
    	    	
    			
	            //this.supDetailWindow.close();
    			box.hide();
    			//form.reset();
    		}
    	
    },
    disableSupplier : function(grid, rowIndex, colIndex, record) {
    	var record = grid.store.getAt(rowIndex);
    	var id = record.data.id;
    	//var dto = Ext.create('SupplierApp.model.SupplierDTO',record.data);
    	
    	Ext.MessageBox.show({
            title:SuppAppMsg.supplierTitleDisable,
            msg: SuppAppMsg.supplierTitleMsg,
            buttons: Ext.MessageBox.YESNO,
            fn: function showResult(btn){
                if(btn == 'yes'){
                	var box = Ext.MessageBox.wait(
                			SuppAppMsg.supplierWaitSeconds,
							SuppAppMsg.approvalExecution);
					
					Ext.Ajax.request({
					    url: 'supplier/disableSupplier.action',
					    method: 'POST',
					    params: {
				        	idSupplier:id
				        },
					    success: function(fp, o) {
					    	var res = Ext.decode(fp.responseText);
					    	grid.store.load();
					    	box.hide();
					    	if(res.message == "Succ Disable"){
					    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.supplierRespDisableSucc);
					    	}else if(res.message == "Fail Disable"){
					    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.supplierRespDisableFail);
					    	}
				        	
					    },
					    failure: function() {
					    	box.hide();
					    	Ext.MessageBox.show({
				                title: 'Error',
				                msg: SuppAppMsg.supplierErrorDisable,
				                buttons: Ext.Msg.OK
				            });
					    }
					}); 
               	 
                }
            },
            icon: Ext.MessageBox.QUESTION
        });
    	
    },
    
     updateCatCodeSuppForm : function(button) {
    	var form = this.getSupplierForm().getForm();
    	var val = form.getFieldValues();
    	
    	/*if(val.addresNumber == '0'  || val.addresNumber == '' ){
    		Ext.Msg.alert(SuppAppMsg.supplierForm79, SuppAppMsg.supplierForm80);
    		return false;
    	}*/
    	
    	
    	Ext.MessageBox.show({
            title:SuppAppMsg.supplierForm77,
            msg:  SuppAppMsg.supplierForm78,
            buttons: Ext.MessageBox.YESNO,
            fn: function showResult(btn){
                if(btn == 'yes'){
                	var box = Ext.MessageBox.wait(
                			SuppAppMsg.supplierWaitSeconds,
							SuppAppMsg.approvalExecution);
					
					Ext.Ajax.request({
					    url: 'supplier/updateCatCodeSupp.action',
					    method: 'POST',
					    params: {
				        	idSupplier:val.id,
				        	approvalStep : val.approvalStep,
				        	catCode23: val.catCode23,
				        	catCode24: val.catCode24,
				        	catCode01: val.catCode01,
				        	catCode20: val.catCode20,
				        	glClass: val.glClass,
				        	paymentMethod: val.paymentMethod,
				        	requisitosFiscales: val.requisitosFiscales,
				        	pmtTrmCxC: val.pmtTrmCxC,
				        	explFiscal: val.explFiscal,
				        	catCode15: val.catCode15
				        },
					    success: function(fp, o) {
					    	var res = Ext.decode(fp.responseText);
					    	box.hide();
					    	if(res.message == "Succ Update"){
					    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.supplierRespUpdCatCodeSucc);
					    	}else {
					    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.supplierRespUpdCatCodeFail);
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
               	 
                }
            },
            icon: Ext.MessageBox.QUESTION
        });
    	
    },
    
    supAddNbrSrch: function(button) {
    	var grid = this.getSupplierGrid();
        var store = grid.getStore();
        
        var nbrField = grid.down('#supAddNbr');
        var nameField = grid.down('#supAddName');
        
        var supAddNbr = nbrField.getValue() ==''?'':nbrField.getValue();
    	var supAddName = nameField.getValue() == ''?'':nameField.getValue();

        store.proxy.extraParams = { 
            supAddNbr : supAddNbr?supAddNbr:'',
        	supAddName : supAddName?supAddName:''
        };
        
        store.load();
    	
    },
    
    uploadSuppliersFile: function(button) {
        var me = this; 
        var grid = this.getSupplierGrid();
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
    									xtype : 'filefield',
    									name : 'file',
    									fieldLabel : SuppAppMsg.supplierFileExcel,
    									//labelWidth : 120,
    									msgTarget : 'side',
    									allowBlank : false,
    									//margin:'20 0 70 0',
    									//anchor : '90%',
    									buttonText : SuppAppMsg.suppliersSearch,
    									buttonConfig: {
    										cls: 'buttonStyle'
    								    }
    								} ],

    						buttons : [ {
    							text : SuppAppMsg.supplierLoad,
    							cls: 'buttonStyle',
    							margin:'10 0 0 0',
    							handler : function() {
    								var form = this.up('form').getForm();
    								if (form.isValid()) {
    									form.submit({
    												url : 'uploadExcelSuppliers.action',
    												waitMsg : SuppAppMsg.supplierLoadFile,
    												success : function(fp, o) {
    													var res = Ext.decode(o.response.responseText);
    													if(res.count > 0){
    														Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.supplierFileSuppSucc + " " + res.count + " " +  SuppAppMsg.supplierFileSuppSucc});
    													}else{
    														Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierFileSuppFile });
    													}
    													me.winLoadSupplier.close();
    													
    												},       // If you don't pass success:true, it will always go here
    										        failure: function(fp, o) {
    										        	var res = Ext.decode(o.response.responseText);
    										        	me.winLoadSupplier.close();
    										        	if(res.error_data_template){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  window.navigator.language.startsWith("es", 0)? res.message_es:res.message_en});
    										        	}else
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierFileSuppFile});
    										        	/*var res = Ext.decode(o.response.responseText);
    										        	
    										        	if(res.message =='ERROR_DATA_NULL'){
    										        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.excelSupplier});
    										        	}else Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
    										        	
    										        	me.winLoadSupplier.close();*/
    										        }
    											});
    								}
    							}
    						} ]
    					});

    	this.winLoadSupplier = new Ext.Window({
    		layout : 'fit',
    		title : SuppAppMsg.supplierLoadNewSupplier,
    		//width : 600,
    		//height : 160,
    		width: Ext.Element.getViewportWidth() * 0.35,   //  40% de pantalla
            maxWidth: 520,                                //  ancho mínimo
            height: Ext.Element.getViewportHeight() * 0.45, //  40% de alto
            maxHeight: 150,
    		modal : true,
    		closeAction : 'destroy',
    		resizable : false,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		items : [ filePanel ]

    	});
    	this.winLoadSupplier.show();
  
    },
    
    saveForm : function(button) { 
    	this.getInvalidFields();
    	var form = this.getSupplierForm().getForm();
    	if (form.isValid()) {
    		var values = form.getFieldValues();
			var box = Ext.MessageBox.wait(
					SuppAppMsg.supplierProcessRequest,
					SuppAppMsg.approvalExecution);
    		/*
        	Ext.Ajax.request({
				url : 'public/emailValidation.action',
				method : 'GET',
					params : {
						emailComprador : values.emailComprador
					},
					success : function(response,opts) {
						response = Ext.decode(response.responseText);
						if(response.success == 'false' || !response.success){
							Ext.Msg.alert("Error",response.message);
							return false;
						}else{
							*/
							var rfcSupplierData = null;
			    			var typeSearchData = null;
			    			
			    			if(!values.rfc){
			    				rfcSupplierData =  values.taxId;
			    				typeSearchData = "taxId";
			        		}else{
			        			rfcSupplierData =  values.rfc;
			        			typeSearchData = "rfc";
			        		}
			    			
			    			Ext.Ajax.request({
								url : 'public/getCountRFC.action',
								method : 'GET',
									params : {
										rfcSupplier : rfcSupplierData,
										typeSearch : typeSearchData
									},
									success : function(response,opts) {
										var statusApp = values.approvalStatus
										response = Ext.decode(response.responseText);
										if((statusApp == "DRAFT" && response.message < 2) ||
											(statusApp == "RENEW" && response.message < 2) ||
											(statusApp != "DRAFT" && response.message < 1)){
											
											var record = Ext.create('SupplierApp.model.Supplier');
								    		updatedRecord = populateObj(record, values);

							    			updatedRecord.regiones = "";
							    			updatedRecord.name = updatedRecord.razonSocial;
							    			updatedRecord.approvalStatus = 'NEW';
							    			record.set(updatedRecord);
							    			record.save({
							    			    callback: function (records, operation, success) { 
							    			    	var ticketId = "";
							    			    	var response = operation.response;
							    			    	var res = Ext.decode(response.responseText);
							    			    	if(res.message == 'ERROR_COMPL'){
								    			    	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad , msg:  SuppAppMsg.supplierErrorCompl});
								    			    	return false;
							    			    	}else{
							    			    		var ticketId = res.message;
							    			    	}

									    			form.reset();
							    			    	box.hide();
							    			    	Ext.MessageBox.show({
							        	        	    title: SuppAppMsg.supplierMessage,
							        	        	    msg: SuppAppMsg.supplierSaveSucc  + ticketId + '<br><br>',
							        	        	    buttons: Ext.MessageBox.OK,
							        	        	    fn: function (btn) {
							        	        	        if (btn == 'ok') {
							        	        	        	location.href = "login.action";
							        	        	        }
							        	        	    }
							        	        	});
							    			    }
							    			});
										}else{
											Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error' , msg:  'El RFC o TaxId utilizado en esta solicitud ya se encuentra en nuestra base de datos y no puede continuar con el proceso de enviar formato. <br><br>' });
										}
										},
										failure : function() {
											 box.hide();
										}
									});
							/*
							var record = Ext.create('SupplierApp.model.Supplier');
				    		updatedRecord = populateObj(record, values);

			    			updatedRecord.regiones = "";
			    			updatedRecord.name = updatedRecord.razonSocial;
			    			updatedRecord.approvalStatus = 'NEW';
			    			record.set(updatedRecord);
			    			record.save({
			    			    callback: function (records, operation, success) { 
			    			    	var ticketId = "";
			    			    	var response = operation.response;
			    			    	var res = Ext.decode(response.responseText);
			    			    	if(res.message == 'ERROR_COMPL'){
				    			    	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad , msg:  SuppAppMsg.supplierErrorCompl});
				    			    	return false;
			    			    	}else{
			    			    		var ticketId = res.message;
			    			    	}

					    			form.reset();
			    			    	box.hide();
			    			    	Ext.MessageBox.show({
			        	        	    title: SuppAppMsg.supplierMessage,
			        	        	    msg: SuppAppMsg.supplierSaveSucc  + ticketId,
			        	        	    buttons: Ext.MessageBox.OK,
			        	        	    fn: function (btn) {
			        	        	        if (btn == 'ok') {
			        	        	        	location.href = "login.action";
			        	        	        }
			        	        	    }
			        	        	});
			    			    }
			    			});
			    		*/
						/*}
			    		
					 },
					failure : function(response,opts) {
						box.hide();
					}
				});*/
    	}else{
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad , msg:  SuppAppMsg.supplierSaveFail });
    	}
    },

    replicateSupplier: function(button) {
    	var box = Ext.MessageBox.wait(
    			SuppAppMsg.supplierProcessRequest,
				SuppAppMsg.approvalExecution);
		
    	Ext.Ajax.request({
			url : 'public/supplierReplication.action',
			method : 'GET',
				success : function(response,opts) {
					box.hide();
			    	Ext.Msg.alert( SuppAppMsg.supplierRepliSupp ,SuppAppMsg.supplierMessage);
				},
				failure : function(response,opts) {
					box.hide();
				}
			});
  
    },

    updateForm : function(button) {
    	this.getInvalidFields();
    	// Ext.getCmp('custBankAcct').maxLength=28;
    	var form = this.getSupplierForm().getForm();
    	var val = form.getFieldValues();
    	var validSections = true;
    	if(val.originApproval && role != 'ROLE_SUPPLIER'){
			validSections = false;
		
		}
    	if (form.isValid()) {
    		var record = Ext.create('SupplierApp.model.Supplier');
    		values = form.getFieldValues();
    		var id = values.id;
    		var regiones = values.regionesTmp;
    		updatedRecord = populateObj(record, values);
    		var me = this;
    		
    		if(!values.checkEditContact && !values.checkEditDataBank && !values.checkEditDataSupp
    				&& !values.checkEditFiscalAddress && !values.checkEditLegalRepr && validSections){
    			Ext.Msg.alert("Error",
    					SuppAppMsg.supplierUpdateWithOutData
    							);
    		}else{
    			
    			var regEdit = "";
    			
    			if(values.checkEditContact){
    				regEdit = regEdit + "checkEditContact" +",";
    			}
    			    			
    			if(values.checkEditDataSupp){
    				regEdit = regEdit + "checkEditDataSupp" +",";
    			}
    			
    			if(values.checkEditFiscalAddress){
    				regEdit = regEdit + "checkEditFiscalAddress" +",";                  		
    			}
    			
    			if(values.checkEditLegalRepr){
    				regEdit = regEdit + "checkEditLegalRepr" +",";
    			}
    			
    			if(values.checkEditDataBank){
    				regEdit = regEdit + "checkEditDataBank" +",";	
    			}
    			
    			if(!validSections && regEdit == "") {
    				regEdit = "checkEditImp" +",";
    			}

    			if(regEdit == ""){
    				Ext.Msg.alert("Error",
        					SuppAppMsg.supplierUpdateWithOutData
        							);
    				return false;
    			}
    			
    		if (values.id = 0) {
    			Ext.Msg.alert(
    					SuppAppMsg.supplierUpdateFail,
    							"Error");
    		} else {
    			
    			Ext.MessageBox.show({
                    title:SuppAppMsg.supplierForm73,
                    msg: SuppAppMsg.supplierForm74,
                    buttons: Ext.MessageBox.YESNO,
                    buttonText : {
            			yes : window.navigator.language.startsWith("es", 0)? "SÍ" : "YES",
            			no : "NO"
            		},
                    fn: function showResult(btn){
                        if(btn == 'yes'){
    			
    			var box = Ext.MessageBox.wait(
    							SuppAppMsg.supplierProcessRequest,
    							SuppAppMsg.approvalExecution);
    			
    	    	
    			updatedRecord.updateApprovalFlow = regEdit;
    			updatedRecord.dataUpdateList = regEdit;
    			updatedRecord.id = id;
    			rec = Ext.create('SupplierApp.model.Supplier', updatedRecord);
    			
    			rec.save({
    			    callback: function (records, operation, success) {
    			    	var ticketId = "";
    			    	var response = operation.response;
    			    	var res = Ext.decode(response.responseText);
    			    	if(res.message == 'ERROR_COMPL'){
	    			    	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierErrorCompl});
	    			    	return false;
    			    	}else{
    			    		var ticketId = res.message;
    			    	}
    			    	
    			    	if(role=='ROLE_SUPPLIER'){
    			    		Ext.MessageBox.show({
            	        	    title: SuppAppMsg.supplierDataUpdate,
            	        	    msg: SuppAppMsg.supplierInfoSendSucc + '<br><br>',
            	        	    buttons: Ext.MessageBox.OK,
            	        	    fn: function (btn) {
            	        	        if (btn == 'ok') {
            	        	        	location.href = "login.action";
            	        	        }
            	        	    }
            	        	});
    			    	}else{
    			    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierDataUpdate, msg: SuppAppMsg.supplierInfoSendSucc });
    			    	}
    			    	
    			    	//
    			    }
    			});
	            //this.supDetailWindow.close();
    			box.hide();
    			//form.reset();
                        }
                    },
                    icon: Ext.MessageBox.QUESTION
                });
    		}
    	  }
    	}else{
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad , msg:  SuppAppMsg.supplierSaveFail });
    	}
    },
    
    updateFormDraft : function(button) {
    	this.getInvalidFields();
    	var form = this.getSupplierForm().getForm();
    	if (form.isValid()) {
    		var record = Ext.create('SupplierApp.model.Supplier');
    		values = form.getFieldValues();
    		var id = values.id;
    		var regiones = values.regionesTmp;
    		updatedRecord = populateObj(record, values);
    		/*if(values.approvalStatus == "RENEW"){
    			updatedRecord.addresNumber = '';
    		}*/
    		updatedRecord.approvalStatus = "DRAFT";
    		var me = this;

    		if (values.id = 0) {
    			Ext.Msg.alert(
    					SuppAppMsg.supplierUpdateFail,
    							"Error");
    		} else {
    			
    			var box = Ext.MessageBox.wait(
    					SuppAppMsg.supplierProcessRequest,
    							SuppAppMsg.approvalExecution);
    			
    			var rfcSupplierData = null;
    			var typeSearchData = null;
    			if(!values.rfc){
    				rfcSupplierData =  values.taxId;
    				typeSearchData = "taxId";
        		}else{
        			rfcSupplierData =  values.rfc;
        			typeSearchData = "rfc";
        		}
    			
    			Ext.Ajax.request({
					url : 'public/getCountRFC.action',
					method : 'GET',
						params : {
							rfcSupplier : rfcSupplierData,
							typeSearch : typeSearchData
						},
						success : function(response,opts) {
							var statusApp = values.approvalStatus
							response = Ext.decode(response.responseText);
							if(response.message < 1 || values.approvalStatus == "DRAFT" || values.approvalStatus == "RENEW" ){
								
								updatedRecord.regiones = regiones;
				    			updatedRecord.id = id;
				    			rec = Ext.create('SupplierApp.model.Supplier', updatedRecord);
				    			
				    			rec.commit();
				    			
				    			rec.save({
				    			    callback: function (records, operation, success) {
				    			    	if(records.data != null){
					    			    	var ticketId = "";
					    			    	var response = operation.response;
					    			    	var res = Ext.decode(response.responseText);
					    			    	if(res.message == 'ERROR_COMPL'){
						    			    	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierErrorCompl});
						    			    	return false;
					    			    	}else{
					    			    		var ticketId = res.message;
					    			    	}
				    			    		//Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierDataUpdate, msg:  SuppAppMsg.supplierDataUpdateMsj + ticketId});
				        			    	form.reset();
				        			    	Ext.MessageBox.show({
					        	        	    title: SuppAppMsg.supplierDataUpdate,
					        	        	    msg: SuppAppMsg.supplierDataUpdateMsj + ticketId + '<br><br>',
					        	        	    buttons: Ext.MessageBox.OK,
					        	        	    fn: function (btn) {
					        	        	        if (btn == 'ok') {
					        	        	        	location.href = "login.action";
					        	        	        }
					        	        	    }
					        	        	});
				    			    	}
				    			    	
				    			    
				    			    }
				    			});
					            //this.supDetailWindow.close();
				    			box.hide();
				    			//form.reset();
				    			
							}else{
								Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error' , msg:  'El RFC o TaxId utilizado en esta solicitud ya se encuentra en nuestra base de datos y no puede continuar con el proceso de guardar como borrador. <br><br>' });
							}
							},
							failure : function() {
								 box.hide();
							}
						});
    			
    		}
    	}else{
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad , msg:  SuppAppMsg.supplierSaveFail });
    	}
    },
    
    loadRfcDoc : function(button) {
    	var supForm = this.getSupplierForm().getForm();
    	var values = supForm.getFieldValues();
    	var supField = "";
    	switch (button.action) {
    	  case 'loadRfcDoc':
    		    supField = 'rfcDocument';
    		    break;
    	  case 'loadDomDoc':
		        supField = 'domDocument';
              break;
    	  case 'loadEdoDoc':
		        supField = 'edoDocument';
              break;
    	  case 'loadEscDoc':
		        supField = 'escDocument';
              break;
    	  case 'loadNotDoc':
		        supField = 'notDocument';
              break;
    	  case 'loadIdentDoc':
		        supField = 'identDocument';
              break;
    	  case 'loadCertDoc':
		        supField = 'certDocument';
              break;
    	  case 'loadActaConst':
		        supField = 'actaConstitutiva';
		      break;
    	  case 'loadRpcDocument':
		        supField = 'rpcDocument';
		      break;
    	  case 'loadlegalExistence':
		        supField = 'legalExistence';
		      break;
    	  case 'loadForeingResidence':
		        supField = 'foreingResidence';
		      break;
    	  case 'loadSTPS':
		        supField = 'textSTPS';
		      break;
    	  case 'loadIMSS':
		        supField = 'textIMSS';
		      break;
    	  default:
            break;
    	};
    	
    	if(!values.rfc && !values.taxId){
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocNot });
    		return false;
    	}
    	var me = this;
    	var addressNumber = '';
    	if( values.addresNumber != '0'  && values.addresNumber != '' ){
        	addressNumber = values.addresNumber;
    	}else{
    		if(!values.rfc){
    			addressNumber = "NEW_" + values.taxId;
    		}else{
    			addressNumber = "NEW_" + values.rfc;
    		}
    		
    	}
    	
    	var filePanel = Ext.create(
    					'Ext.form.Panel',
    					{
    						width : 900,	
    						items : [{
    									xtype : 'textfield',
    									name : 'addressBook',
    									hidden : true,
    									margin:'20 0 0 10',
    									value : addressNumber 
    								},{
    									xtype : 'textfield',
    									name : 'documentNumber',
    									hidden : true,
    									value : 0
    								},{
    									xtype : 'textfield',
    									name : 'documentType',
    									hidden : true,
    									value : button.action
    								},
    								{
    									xtype : 'filefield',
    									name : 'file',
    									fieldLabel :SuppAppMsg.purchaseFile,
    									labelWidth : 120,
    									msgTarget : 'side',
    									allowBlank : false,
    									margin:'20 0 70 0',
    									anchor : '90%',
    									buttonText : SuppAppMsg.suppliersSearch,
    									regex: /(.)+((\.pdf)|(\.PDF)(\w)?)$/i,
    								    regexText: SuppAppMsg.supplierDocsFormat,
    									listeners:{
    							            change: 'fileInputChange',
    							            afterrender:function(cmp){
    							                cmp.fileInputEl.set({
    							                    accept:'.pdf'
    							                });
    							            },
    							            change:function(inputFile, value){
    							            	var sizeFile = inputFile.fileInputEl.dom.files[0].size;
    							            	if(sizeFile > 10000000){
    							            	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.fileUploadError1, msg:  SuppAppMsg.fileUploadError13 });
    							        		return false;
    							            	}
    							            }
    							        },
    									} ],

    						buttons : [ {
    							text : SuppAppMsg.supplierLoad,
    							margin:'10 0 0 0',
    							handler : function() {
    								var form = this.up('form').getForm();
    								if (form.isValid()) {
    									form.submit({
    												url : 'upload.action',
    												waitMsg : SuppAppMsg.supplierLoadFile,
    												success : function(fp, o) {
    													var res = Ext.decode(o.response.responseText);
    													Ext.MessageBox.alert({ maxWidth: 400, minWidth: 400, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.supplierLoadDocSucc });
    													supForm.findField(supField).setValue(res.fileName);

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
    		title : SuppAppMsg.supplierLoadDocTitle,
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

    gridSelectionChange: function(model, record) {
        if (record) {
        	//Ext.getCmp('domDocument').setReadOnly(true);
        	//Ext.getCmp('edoDocument').setReadOnly(true);
        	//Ext.getCmp('escDocument').setReadOnly(true);
        	//Ext.getCmp('notDocument').setReadOnly(true);
        	//Ext.getCmp('identDocument').setReadOnly(true);
        	//Ext.getCmp('certDocument').setReadOnly(true);
        	
        	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
        	var me = this;
        	this.supDetailWindow = new Ext.Window({
        		layout : 'fit',
        		title : SuppAppMsg.supplierDetailsSupplier,
        		//width : 1200,
        		//height : 550,
        		width: Ext.Element.getViewportWidth() * 0.80,   // 👈 40% de pantalla
                maxWidth: 1200,                                // 👈 ancho mínimo
                height: Ext.Element.getViewportHeight() * 0.80, // 👈 40% de alto
                maxHeight: 550,
        		modal : true,
        		scrollable  : true,
        		closeAction : 'destroy',
        		resizable : true,
        		minimizable : false,
        		maximizable : false,
        		plain : true,
        		items : [ {
        			xtype : 'supplierPanelDetail',
        			border : true,
        			height : 415
        		} ]

        	});
        	this.supDetailWindow.show();
        	var form = this.getSupplierForm().getForm();
        	
        	
        	Ext.Ajax.request({
				url : 'supplier/getById.action',
				method : 'GET',
					params : {
						start : 0,
						limit : 15,
						id:record.data.id
					},
					success : function(response,opts) {
						response = Ext.decode(response.responseText);
						rec = Ext.create('SupplierApp.model.Supplier');
						rec.set(response.data);
			        	form.loadRecord(rec);
			        	form.findField('codigoPostal').setRawValue(response.data.codigoPostal);
			        	form.findField('regionesTmp').setValue(response.data.regiones);
			        	form.findField('categoriasTmp').setValue(response.data.categorias);
			        	var fileList = rec.data.fileList;
			        	var hrefList ="";
			        	var r2 = [];
			        	var href ="";
			        	var fileHref="";

			        	var r1 = fileList.split("_FILE:");
			        	var inx = r1.length;
			        	for (var index = inx - 1; index >= 0; index--) {
			        		r2 = r1[index].split("_:_");
				        		if(r2[0]  != ""){
					        	href = "documents/openDocument.action?id=" + r2[0];
								
					        	var typeDoc = "";
					        	if(r2[1].includes("*1*")){
					        		r2[1]=r2[1].replace("*1*",'');
					        		typeDoc= SuppAppMsg.supplierForm36;
					        	}else if(r2[1].includes("*2*")){
					        		r2[1]=r2[1].replace("*2*",'');
					        		typeDoc= SuppAppMsg.supplierForm37;
					        	}else if(r2[1].includes("*3*")){
					        		r2[1]=r2[1].replace("*3*",'');
					        		typeDoc= SuppAppMsg.supplierForm38;
					        	}else if(r2[1].includes("*4*")){
					        		r2[1]=r2[1].replace("*4*",'');
					        		typeDoc= SuppAppMsg.supplierForm39;
					        	}else if(r2[1].includes("*5*")){
					        		r2[1]=r2[1].replace("*5*",'');
					        		typeDoc= SuppAppMsg.supplierForm40;
					        	}
					        	
								fileHref = typeDoc +"** <a href= '" + href + "' target='_blank'>" + r2[1]  + "</a> ||" + r2[2];
								hrefList = "<p>"  + hrefList + fileHref + "</p>";
			        		}
			        	} 
			        	Ext.getCmp('hrefFileList').setValue(hrefList);
			        	
			        	if(rec.data.approvalStatus == 'APROBADO'){
				        	var formPanelName = Ext.ComponentQuery.query('#supplierFormId field');
				        	for(var i= 0; i < formPanelName.length; i++) {
				        	   formPanelName[i].setReadOnly(true);
				        	   formPanelName[i].setFieldStyle('border: 1px solid #666;');
				        	}

				        	var documentContainer = Ext.ComponentQuery.query('#documentContainerRfc field');
				        	for(var i= 0; i < documentContainer.length; i++) {
				        		    documentContainer[i].setVisible(false);
					        }
				        	
				        	documentContainer = Ext.ComponentQuery.query('#documentContainerDom field');
				        	for(var i= 0; i < documentContainer.length; i++) {
				        		    documentContainer[i].setVisible(false);
					        }
				        	
				        	documentContainer = Ext.ComponentQuery.query('#documentContainerBank field');
				        	for(var i= 0; i < documentContainer.length; i++) {
				        		    documentContainer[i].setVisible(false);
					        }
				        	
				        	documentContainer = Ext.ComponentQuery.query('#documentContainerPub field');
				        	for(var i= 0; i < documentContainer.length; i++) {
				        		    documentContainer[i].setVisible(false);
					        }
				        	
				        	documentContainer = Ext.ComponentQuery.query('#documentContainerNot field');
				        	for(var i= 0; i < documentContainer.length; i++) {
				        		    documentContainer[i].setVisible(false);
					        }
				        	
				        	documentContainer = Ext.ComponentQuery.query('#documentContainerIden field');
				        	for(var i= 0; i < documentContainer.length; i++) {
				        		    documentContainer[i].setVisible(false);
					        }
				        	
				        	documentContainer = Ext.ComponentQuery.query('#documentContainerCal field');
				        	for(var i= 0; i < documentContainer.length; i++) {
				        		    documentContainer[i].setVisible(false);
					        }
				        	
				        	Ext.getCmp('loadRfcDoc').hide();
				        	Ext.getCmp('loadDomDoc').hide();
				        	Ext.getCmp('loadEdoDoc').hide();
				        	Ext.getCmp('documentContainerOutSourcing').hide();
			        	
			        	}
			        	
			        	if(role == 'ROLE_ADMIN'){
			        		
			        		form.findField('nombreContactoCxC').setReadOnly(true);
			        		form.findField('telefonoContactoCxC').setReadOnly(true);
			        		Ext.getCmp('emailSupplier').setReadOnly(false);
			        	}
			        	
			        	if(tabChgn == 'suppliers'){			        		
			        		Ext.getCmp('ticketForSearch').hide();
				        	Ext.getCmp('searchTicket').hide();
				        	Ext.getCmp('searchCP').hide();
				        	Ext.getCmp('fechaSolicitud').hide();
				        	Ext.getCmp('formatoSolicitud').hide();
				        	Ext.getCmp('documents').hide();
				        	Ext.getCmp('actaConstitutiva').hide();
				        	Ext.getCmp('loadActaConst').hide();
				        	Ext.getCmp('rpcDocument').hide();
				        	Ext.getCmp('loadRpcDocument').hide();
				        	Ext.getCmp('loadlegalExistence').hide();
				        	Ext.getCmp('legalExistence').hide();
				        	Ext.getCmp('foreingResidence').hide();
				        	Ext.getCmp('loadForeingResidence').hide();
				        	Ext.getCmp('camposobligatorios').hide();
				        	Ext.getCmp('updateSupplierForm').hide();

				        	Ext.getCmp('loadIdentDoc').hide();
				        	form.findField('emailComprador').setReadOnly(true);
				        	form.findField('nombreContactoCxC').setReadOnly(true);
				        	form.findField('telefonoContactoCxC').setReadOnly(true);
			        	}
			        	/*
			        	Ext.Ajax.request({
							url : 'documents/listDocumentsByOrder.action',
							method : 'GET',
								params : {
									start : 0,
									limit : 15,
									orderNumber:100,
									orderType:'GRAL',
									addressNumber:'GENERAL'
								},
								success : function(response,opts) {
									response = Ext.decode(response.responseText);
									var data = response.data;
									var hrefList = "";
									for (var i = 0; i < data.length; i++) {
										href = "documents/openDocument.action?id=" + data[i].id;
										fileHref = "*** <a href= '" + href + "' target='_blank'>" +  data[i].name + "</a>";
										hrefList = "<p>"  + hrefList + fileHref + "</p>";
					    			}
									Ext.getCmp('internalFileList').setValue(hrefList);
									

								},
								failure : function(response,opts) {
									box.hide();
								}
							});*/
			        	
						box.hide();
					},
					failure : function() {
						 box.hide();
					}
				});
        	
        }
    },
    
    searchTicket: function(button) {
    	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
       	var form = this.getSupplierForm().getForm();
       	var ticketId = form.findField('ticketForSearch').getValue();
   	    form.reset();
   	    form.findField('ticketForSearch').setValue(ticketId);
    	Ext.Ajax.request({
			url : 'public/getByTicketId.action',
			method : 'GET',
				params : {
					ticketId : ticketId
				},
				success : function(response,opts) {
					response = Ext.decode(response.responseText);
					if(response.data != null){
						rec = Ext.create('SupplierApp.model.Supplier');
						rec.set(response.data);
						/*if(rec.data.addresNumber != '' && rec.data.approvalStatus != 'RENEW'){
							box.hide();
							Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMessage, msg: 'Su información ya no se encuentra disponible como borrador. Si ha iniciado el proceso de activación, deberá esperar a que su cuenta sea creada y la notificación se envíe a su cuenta de correo.' });
							return;
						}else */if(rec.data.approvalStatus == 'PENDIENTE' || rec.data.approvalStatus == 'APROBADO'){
							box.hide();
							Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMessage, msg: 'Su información ya no se encuentra disponible como borrador. Si ha iniciado el proceso de activación, deberá esperar a que su cuenta sea creada y la notificación se envíe a su cuenta de correo.' });
							return;
						}
						
			        	form.loadRecord(rec);
			        	form.findField('codigoPostal').setRawValue(response.data.codigoPostal);
			        	form.findField('regionesTmp').setValue(response.data.regiones);
			        	form.findField('categoriasTmp').setValue(response.data.categorias);
			        	var fileList = rec.data.fileList;
			        	var hrefList ="";
			        	var r2 = [];
			        	var href ="";
			        	var fileHref="";
	
			        	if(fileList != ''){
			        		var r1 = fileList.split("_FILE:");
				        	var inx = r1.length;
				        	for (var index = inx - 1; index >= 0; index--) {
				        		r2 = r1[index].split("_:_");
					        		if(r2[0]  != ""){
						        	href = "documents/openDocument.action?id=" + r2[0];
						        	
						        	var typeDoc = "";
						        	if(r2[1].includes("*1*")){
						        		r2[1]=r2[1].replace("*1*",'');
						        		typeDoc= SuppAppMsg.supplierForm36;
						        	}else if(r2[1].includes("*2*")){
						        		r2[1]=r2[1].replace("*2*",'');
						        		typeDoc= SuppAppMsg.supplierForm37;
						        	}else if(r2[1].includes("*3*")){
						        		r2[1]=r2[1].replace("*3*",'');
						        		typeDoc= SuppAppMsg.supplierForm38;
						        	}else if(r2[1].includes("*4*")){
						        		r2[1]=r2[1].replace("*4*",'');
						        		typeDoc= SuppAppMsg.supplierForm39;
						        	}else if(r2[1].includes("*5*")){
						        		r2[1]=r2[1].replace("*5*",'');
						        		typeDoc= SuppAppMsg.supplierForm40;
						        	}
						        	
									fileHref = typeDoc +"** <a href= '" + href + "' target='_blank'>" + r2[1]  + "</a> ||" + r2[2];
									hrefList = "<p>"  + hrefList + fileHref + "</p>";
				        		}
				        	} 
				        	Ext.getCmp('hrefFileList').setValue(hrefList);
				        	
				        	//form.findField('rfcDocument').allowBlank = true;
			        		//form.findField('domDocument').allowBlank = true;
			        		//form.findField('edoDocument').allowBlank = true;
			        		//form.findField('identDocument').allowBlank = true;
			        		//form.findField('actaConstitutiva').allowBlank = true;
			        		form.findField('rpcDocument').allowBlank = true;
			        		form.findField('legalExistence').allowBlank = true;
			        		form.findField('foreingResidence').allowBlank = true;
			        	}
			        	
			        	
		        		form.findField('nombreContactoCxC').setReadOnly(false);
		        		form.findField('telefonoContactoCxC').setReadOnly(false);
		        		
		        		if(rec.data.approvalStatus == 'DRAFT'){
		        			form.findField('rfcDocument').allowBlank = true;
		        			form.findField('domDocument').allowBlank = true;
		        			form.findField('edoDocument').allowBlank = true;
		        			form.findField('identDocument').allowBlank = true;
		        			form.findField('actaConstitutiva').allowBlank = true;
		        		}
		        		
		        		 
		        		if(rec.data.country != 'MX'){
			    			//Ext.getCmp('supRfc').setReadOnly(true);
			    			//Ext.getCmp('documentContainerForeingResidence').show();
			    			//Ext.getCmp('documentContainerOutSourcing').show();
			    			Ext.getCmp('taxId').allowBlank=false;
		        			Ext.getCmp('taxId').show();
		        			Ext.getCmp('supRfc').allowBlank=true;
		        			Ext.getCmp('supRfc').hide();
		        			
		        			Ext.getCmp('searchCP').hide();
			    			
			    			Ext.getCmp('coloniaEXT').allowBlank=false;
			    			Ext.getCmp('coloniaEXT').show();
			    			Ext.getCmp('coloniaEXT').setValue(rec.data.colonia);
			    			
			    			Ext.getCmp('fldColonia').hide();
			    			Ext.getCmp('fldColonia').allowBlank=true;
			    			
			    			Ext.getCmp('fldMunicipio').hide();
							Ext.getCmp('fldMunicipio').allowBlank=true;
			    			
			    			Ext.getCmp('codigoPostal').allowBlank=true;
		        		}else{
			    			//Ext.getCmp('supRfc').setReadOnly(false);
			    			Ext.getCmp('documentContainerForeingResidence').hide();
			    			Ext.getCmp('documentContainerOutSourcing').hide();
			    			Ext.getCmp('supRfc').allowBlank=false;
		        			Ext.getCmp('supRfc').show();
		        			Ext.getCmp('taxId').allowBlank=true;
		        			Ext.getCmp('taxId').hide();
		        			
		        			Ext.getCmp('searchCP').show();
			    			
			    			Ext.getCmp('fldColonia').show();
			    			Ext.getCmp('fldColonia').allowBlank=false;
			    			
			    			Ext.getCmp('coloniaEXT').hide();
			    			Ext.getCmp('coloniaEXT').allowBlank=true;
			    			
			    			Ext.getCmp('fldMunicipio').show();
							Ext.getCmp('fldMunicipio').allowBlank=false;
			    			
			    			Ext.getCmp('codigoPostal').allowBlank=false;
		        		}
		        		
		        		if(rec.data.fisicaMoral != '1'){
		        			Ext.getCmp('REPRESENTE_LEGAL').show();
			    			Ext.getCmp('tipoIdentificacion').allowBlank=false;
			    			Ext.getCmp('tipoIdentificacion').show();
			    			Ext.getCmp('numeroIdentificacion').allowBlank=false;
			    			Ext.getCmp('numeroIdentificacion').show();
			    			Ext.getCmp('nombreRL').allowBlank=false;
			    			Ext.getCmp('nombreRL').show();
			    			Ext.getCmp('apellidoPaternoRL').allowBlank=false;
			    			Ext.getCmp('apellidoPaternoRL').show();
			    			Ext.getCmp('apellidoMaternoRL').show();
		        		}else{
		        			Ext.getCmp('REPRESENTE_LEGAL').hide();
			    			Ext.getCmp('tipoIdentificacion').allowBlank=true;
			    			Ext.getCmp('tipoIdentificacion').hide();
			    			Ext.getCmp('numeroIdentificacion').allowBlank=true;
			    			Ext.getCmp('numeroIdentificacion').hide();
			    			Ext.getCmp('nombreRL').allowBlank=true;
			    			Ext.getCmp('nombreRL').hide();
			    			Ext.getCmp('apellidoPaternoRL').allowBlank=true;
			    			Ext.getCmp('apellidoPaternoRL').hide();
			    			Ext.getCmp('apellidoMaternoRL').hide();
		        		}
		        		
		        		if(rec.data.webSite == 'SEND'){
		        			Ext.getCmp('supRfc').setReadOnly(true);
		        			Ext.getCmp('taxId').setReadOnly(true);
		        		}
		        		

			        	/*Ext.Ajax.request({
							url : 'documents/listDocumentsByOrder.action',
							method : 'GET',
								params : {
									start : 0,
									limit : 15,
									orderNumber:100,
									orderType:'GRAL',
			
									addressNumber:'GENERAL'
								},
								success : function(response,opts) {
									response = Ext.decode(response.responseText);
									var data = response.data;
									var hrefList = "";
									for (var i = 0; i < data.length; i++) {
										href = "documents/openDocument.action?id=" + data[i].id;
										fileHref = "*** <a href= '" + href + "' target='_blank'>" +  data[i].name + "</a>";
										hrefList = "<p>"  + hrefList + fileHref + "</p>";
					    			}
									Ext.getCmp('internalFileList').setValue(hrefList);
										
	
									},
									failure : function(response,opts) {
										box.hide();
									}
								});*/
					     }else{
					    	 form.reset();
					     }
							box.hide();
					},
					failure : function() {
						 box.hide();
					}
				});
 
    }

});