Ext.define('SupplierApp.controller.Approval', {
    extend: 'Ext.app.Controller',
    stores: ['Approval'],
    models: ['SupplierDTO','Supplier'],
    views: ['approval.ApprovalGrid','approval.ApprovalPanel','supplier.SupplierForm',
    		'approval.ApprovalSearchGrid','approval.ApprovalSearchPanel'],
    refs: [{
        	ref: 'approvalGrid',
        	selector: 'approvalGrid'
	    },{
        	ref: 'supplierForm',
        	selector: 'supplierForm'
	    },
	    {
        	ref: 'approvalSearchGrid',
        	selector: 'approvalSearchGrid'
	    }],
 
    init: function() {
    	this.supDetailWindow = null;
        this.control({
				'#approveSupplier' : {
					"buttonclick" : this.approveSupplier
				},
				'#rejectSupplier' : {
					"buttonclick" : this.rejectSupplier
				},
	            'approvalGrid': {
	            	itemdblclick: this.gridSelectionChange
	            },
				'approvalSearchGrid button[action=searchAppSupplier]' : {
					click : this.searchAppSupplier
				}
				});
    },
    
    approveSupplier : function(grid, rowIndex, colIndex, record) {
    	var record = grid.store.getAt(rowIndex);
debugger
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
					supp = response.data;
					debugger
				if(supp.updateApprovalFlow == null || supp.updateApprovalFlow == "") {
					if(supp.approvalStep == "THIRD"){
						if(supp.catCode01 ==""||
						   supp.catCode20 ==""||
						   supp.glClass ==""||
						   supp.paymentMethod ==""||
						   supp.requisitosFiscales ==""||
						   supp.pmtTrmCxC ==""||
						   supp.catCode15 ==""||
						   supp.catCode01 ==null||
						   supp.catCode20 ==null||
						   supp.glClass ==null||
						   supp.paymentMethod ==null||
						   supp.requisitosFiscales ==null||
						   supp.pmtTrmCxC ==null||
						   supp.catCode15 ==null){
							Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad , msg:  SuppAppMsg.supplierSaveFail });
							return false;
						}
					}else if(supp.approvalStep == "FIRST"){
						if(supp.catCode23 ==""||
						   supp.catCode24 ==""||
						   supp.catCode23 ==null||
						   supp.catCode24 ==null){
							Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad , msg:  SuppAppMsg.supplierSaveFail });
							return false;	
						}
					}
				}else{
					if( (supp.updateApprovalFlow == "TES" && supp.approvalStep == "FIRST") || 
							(supp.updateApprovalFlow.includes("TES") && supp.approvalStep == "SECOND")){
						
						if(supp.catCode23 ==""||
								   supp.catCode24 ==""||
								   supp.catCode23 ==null||
								   supp.catCode24 ==null||

								   supp.catCode01 ==""||
								   supp.catCode20 ==""||
								   supp.glClass ==""||
								   supp.paymentMethod ==""||
								   supp.requisitosFiscales ==""||
								   supp.pmtTrmCxC ==""||
								   supp.catCode15 ==""||
								   supp.catCode01 ==null||
								   supp.catCode20 ==null||
								   supp.glClass ==null||
								   supp.paymentMethod ==null||
								   supp.requisitosFiscales ==null||
								   supp.pmtTrmCxC ==null||
								   supp.catCode15 ==null					
						){
									Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad , msg:  SuppAppMsg.supplierSaveFail });
									return false;	
								}
					}
					
				}
					debugger
					var dto = Ext.create('SupplierApp.model.SupplierDTO',record.data);
			    	var currentApprover = addressNumber;
			    	var status ="APROBADO";
			    	var step = record.data.approvalStep;
			    	
			    	var dlgAccept = Ext.MessageBox.show({
						title : SuppAppMsg.approvalAceptSupp,
						msg : SuppAppMsg.approvalNoteAcept,
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
									var box = Ext.MessageBox.wait(
											SuppAppMsg.approvalUpdateData,
											SuppAppMsg.approvalExecution);
									var notes = text;
									Ext.Ajax.request({
									    url: 'approval/update.action',
									    method: 'POST',
									    params: {
								        	currentApprover:currentApprover,
								            status:status,
								            step:step,
								            notes:notes
								        },
									    jsonData: dto.data,
									    success: function(fp, o) {
									    	var res = Ext.decode(fp.responseText);
									    	grid.store.load();
									    	box.hide();
									    	if(res.message == "Success"){
									    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalRespAprobadoSucc);
									    	}else if(res.message == "Error JDE"){
									    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalRespErrorJDE);
									    	}else if(res.message == "Succ Update"){
									    		Ext.Msg.alert(SuppAppMsg.approvalResponse, 
									    				SuppAppMsg.approvalRespUpdateSupp + " " + o.jsonData.addresNumber);
									    	}else if(res.message == "Rejected"){
									    		Ext.Msg.alert(SuppAppMsg.approvalResponse, 
									    				SuppAppMsg.approvalRespRejected + " " + o.jsonData.ticketId);
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
			    	
			    	dlgAccept.textArea.inputEl.set({
					    maxLength: 255
					});
					
				},
				failure : function() {
					 box.hide();
				}
			});
    	
    	/*
    	var dto = Ext.create('SupplierApp.model.SupplierDTO',record.data);
    	var currentApprover = addressNumber;
    	var status ="APROBADO";
    	var step = record.data.approvalStep;
    	
    	var dlgAccept = Ext.MessageBox.show({
			title : SuppAppMsg.approvalAceptSupp,
			msg : SuppAppMsg.approvalNoteAcept,
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
						var box = Ext.MessageBox.wait(
								SuppAppMsg.approvalUpdateData,
								SuppAppMsg.approvalExecution);
						var notes = text;
						Ext.Ajax.request({
						    url: 'approval/update.action',
						    method: 'POST',
						    params: {
					        	currentApprover:currentApprover,
					            status:status,
					            step:step,
					            notes:notes
					        },
						    jsonData: dto.data,
						    success: function(fp, o) {
						    	var res = Ext.decode(fp.responseText);
						    	grid.store.load();
						    	box.hide();
						    	if(res.message == "Success"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalRespAprobadoSucc);
						    	}else if(res.message == "Error JDE"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalRespErrorJDE);
						    	}else if(res.message == "Succ Update"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, 
						    				SuppAppMsg.approvalRespUpdateSupp + " " + o.jsonData.addresNumber);
						    	}else if(res.message == "Rejected"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, 
						    				SuppAppMsg.approvalRespRejected + " " + o.jsonData.ticketId);
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
    	
    	dlgAccept.textArea.inputEl.set({
		    maxLength: 255
		});*/
    	
    },
    
    rejectSupplier : function(grid, rowIndex, colIndex, record) {
    	var record = grid.store.getAt(rowIndex);
    	var dto = Ext.create('SupplierApp.model.SupplierDTO',record.data);
    	var currentApprover = addressNumber;
    	var status ="RECHAZADO";
    	var step = record.data.approvalStep;
    	
    	var dlgRejected = Ext.MessageBox.show({
			title : SuppAppMsg.approvalAceptSupp,
			msg : SuppAppMsg.approvalNoteReject,
			buttons : Ext.MessageBox.YESNO,
			maxLength : 255,
			enforceMaxLength : true,
			multiline: true,
			width:500,
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
						    url: 'approval/update.action',
						    method: 'POST',
						    params: {
					        	currentApprover:currentApprover,
					            status:status,
					            step:step,
					            notes:notes
					        },
						    jsonData: dto.data,
						    success: function(fp, o) {
						    	var res = Ext.decode(fp.responseText);
						    	grid.store.load();
						    	box.hide();
						    	if(res.message == "Success"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalRespAprobadoSucc);
						    	}else if(res.message == "Error JDE"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalRespErrorJDE);
						    	}else if(res.message == "Succ Update"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, 
						    				SuppAppMsg.approvalRespUpdateSupp + " " + o.jsonData.addresNumber);
						    	}else if(res.message == "Rejected"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, 
						    				SuppAppMsg.approvalRespRejected + " " + o.jsonData.ticketId+'<br><br>');
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
    
    gridSelectionChange: function(model, record) {
        if (record) {
        	var box = Ext.MessageBox.wait(SuppAppMsg.approvalLoadRegistrer , SuppAppMsg.approvalExecution);
        	var me = this;
        	me.supDetailWindow = new Ext.Window({
        		layout : 'fit',
        		title : SuppAppMsg.approvalDetailsSupplier ,
        		width : 1200,
        		height : 550,
        		modal : true,
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
        	me.supDetailWindow.show();
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
			        	var approvarSupplier = response.data.approvalStep;
			        	var contrib = response.data.fisicaMoral;
			        	var countryData = response.data.country;
			        	var approvalStep = response.data.approvalStep;
			        	var approvalStatus = response.data.approvalStatus;
			        	
			        	form.findField('codigoPostal').setRawValue(response.data.codigoPostal);
			        	form.findField('regionesTmp').setValue(response.data.regiones);
			        	form.findField('categoriasTmp').setValue(response.data.categorias);
			        	
			        	var fileList = rec.data.fileList;
			        	var hrefList ="";
			        	var r2 = [];
			        	var href ="";
			        	var fileHref="";
			        	var r1 = fileList.split("_FILE:");
			        	//for (var index = 0; index < r1.length; index++) {
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
			        	Ext.getCmp('ticketForSearch').setValue(rec.data.ticketId);
			        	
			        	
			        	Ext.getCmp('docLoadSection').hide();
			        	Ext.getCmp('searchTicket').hide();
			        	Ext.getCmp('searchCP').hide();
				
				        form.findField('rfcDocument').allowBlank = true;
		        		form.findField('domDocument').allowBlank = true;
		        		form.findField('edoDocument').allowBlank = true;
		        		//form.findField('escDocument').allowBlank = true;
		        		//form.findField('notDocument').allowBlank = true;
		        		form.findField('identDocument').allowBlank = true;
		        		
		        		form.findField('ticketForSearch').setReadOnly(true);
		        		form.findField('razonSocial').setReadOnly(true);
		        		form.findField('country').setReadOnly(true);
		        		form.findField('currencyValidation').setReadOnly(true);
		        		form.findField('fisicaMoral').setReadOnly(true);
		        		form.findField('emailSupplier').setReadOnly(true);
		        		form.findField('calleNumero').setReadOnly(true);
		        		form.findField('delegacionMnicipio').setReadOnly(true);
		        		form.findField('codigoPostal').setReadOnly(true);
		        		form.findField('colonia').setReadOnly(true);
		        		form.findField('estado').setReadOnly(true);
		        		form.findField('telefonoDF').setReadOnly(true);
		        		form.findField('faxDF').setReadOnly(true);
		        		form.findField('emailComprador').setReadOnly(true);
		        		//form.findField('apellidoPaternoCxC').setReadOnly(true);
		        		//form.findField('apellidoMaternoCxC').setReadOnly(true);
		        		//form.findField('faxCxC').setReadOnly(true);
		        		form.findField('cargoCxC').setReadOnly(true);
		        		form.findField('nombreCxP01').setReadOnly(true);
		        		form.findField('emailCxP01').setReadOnly(true);
		        		form.findField('telefonoCxP01').setReadOnly(true);
		        		form.findField('nombreCxP02').setReadOnly(true);
		        		form.findField('emailCxP02').setReadOnly(true);
		        		form.findField('telefonoCxP02').setReadOnly(true);
		        		form.findField('nombreCxP03').setReadOnly(true);
		        		form.findField('emailCxP03').setReadOnly(true);
		        		form.findField('telefonoCxP03').setReadOnly(true);
		        		form.findField('nombreCxP04').setReadOnly(true);
		        		form.findField('emailCxP04').setReadOnly(true);
		        		form.findField('telefonoCxP04').setReadOnly(true);
		        		form.findField('tipoIdentificacion').setReadOnly(true);
		        		form.findField('numeroIdentificacion').setReadOnly(true);
		        		form.findField('nombreRL').setReadOnly(true);
		        		form.findField('apellidoPaternoRL').setReadOnly(true);
		        		form.findField('apellidoMaternoRL').setReadOnly(true);
		        		
		        		var msgUpdSect = Ext.getCmp('msgUpdSect').getValue();
		        		var dataUpdateList = Ext.getCmp('dataUpdateList').getValue();
		        		var updateApprovalFlow = Ext.getCmp('updateApprovalFlow').getValue();
		        		debugger
		        		if(dataUpdateList != null && dataUpdateList != ''){
		        			Ext.getCmp('updateSupplierForm').hide();
		        			Ext.getCmp('updateCatCodeSuppForm').hide();
		        			var sections = dataUpdateList.split(",");
		        			for (var index = 0; index < sections.length; index++) {
		        				var section = sections[index];
		        				msgUpdSect = msgUpdSect + '<br>'
		        				if(section != ""){
		        					
		        					if(section == 'checkEditDataSupp'){
		        						section = SuppAppMsg.supplierForm4;
		        					}else if(section == 'checkEditFiscalAddress'){
		        						section = SuppAppMsg.supplierForm9;
		        					}
		        					else if(section == 'checkEditContact'){
		        						section = SuppAppMsg.supplierForm86;
		        					}else if(section == 'checkEditLegalRepr'){
		        						section = SuppAppMsg.supplierForm29;
		        					}else if(section == 'checkEditDataBank'){
		        						section = SuppAppMsg.supplierForm32;
		        					}
		        					
		        					
		        					msgUpdSect = msgUpdSect + '-' + section;
		        					Ext.getCmp('msgUpdSect').show();
		        				}
		        				
		        			}
		        			Ext.getCmp('msgUpdSect').setValue(msgUpdSect);
	
		        		}
		        		Ext.getCmp('originApproval').setValue(true);
		        		
		        		if(contrib != '1'){
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
		        		
		        		Ext.getCmp('textCatCode').show();
		        		
		        		Ext.getCmp('explFiscal').show();
		        		debugger
		        		var dataUpdateList = Ext.getCmp('dataUpdateList').getValue();
		        		
		        	if(updateApprovalFlow == null || updateApprovalFlow =="") {
		        		
		        		if(approvarSupplier == 'FIRST'){
		        			Ext.getCmp('catCode23').allowBlank=false; 
			        		Ext.getCmp('catCode24').allowBlank=false; 
			        		Ext.getCmp('catCode23').show();
			        		Ext.getCmp('catCode24').show();
			        		Ext.getCmp('updateCatCodeSuppForm').show();
			        		//form.findField('industryClass').setReadOnly(false);
		        		}else if(approvarSupplier == 'THIRD'){
		        			Ext.getCmp('catCode01').allowBlank=false;
			        		Ext.getCmp('catCode20').allowBlank=false;
			        		Ext.getCmp('glClass').allowBlank=false;
			        		Ext.getCmp('paymentMethod').allowBlank=false;
			        		Ext.getCmp('requisitosFiscales').allowBlank=false;
			        		Ext.getCmp('pmtTrmCxC').allowBlank=false;
			        		Ext.getCmp('explFiscal').allowBlank=false;
			        		Ext.getCmp('catCode01').show();
			        		Ext.getCmp('catCode20').show();
			        		Ext.getCmp('glClass').show();
			        		Ext.getCmp('paymentMethod').show();
			        		Ext.getCmp('requisitosFiscales').show();
			        		Ext.getCmp('pmtTrmCxC').show();
			        		form.findField('explFiscal').setReadOnly(false);
			        		Ext.getCmp('catCode23').show();
			        		Ext.getCmp('catCode24').show();
			        		form.findField('catCode23').setReadOnly(true);
			        		form.findField('catCode24').setReadOnly(true);
			        		//form.findField('industryClass').setReadOnly(true);
			        		//form.findField('glClass').setReadOnly(true);
			        		form.findField('fisicaMoral').setReadOnly(false);
			        		Ext.getCmp('updateCatCodeSuppForm').show();
		        		}else{
		        				Ext.getCmp('catCode01').show();
				        		Ext.getCmp('catCode20').show();
				        		Ext.getCmp('glClass').show();
				        		Ext.getCmp('paymentMethod').show();
				        		Ext.getCmp('requisitosFiscales').show();
				        		Ext.getCmp('pmtTrmCxC').show();
				        		Ext.getCmp('catCode23').show();
				        		Ext.getCmp('catCode24').show();
				        		form.findField('catCode01').setReadOnly(true);
				        		form.findField('catCode20').setReadOnly(true);
				        		form.findField('glClass').setReadOnly(true);
				        		form.findField('paymentMethod').setReadOnly(true);
				        		form.findField('requisitosFiscales').setReadOnly(true);
				        		form.findField('pmtTrmCxC').setReadOnly(true);
				        		form.findField('catCode23').setReadOnly(true);
				        		form.findField('catCode24').setReadOnly(true);
				        		
				        		Ext.getCmp('updateSupplierForm').hide();
				        		Ext.getCmp('updateCatCodeSuppForm').hide();
				        		/*Ext.getCmp('tipoIdentificacion').allowBlank=true;
				    			Ext.getCmp('numeroIdentificacion').allowBlank=true;
				    			Ext.getCmp('nombreRL').allowBlank=true;
				    			Ext.getCmp('apellidoPaternoRL').allowBlank=true;*/
						}
		        	}else{
		        		if( (updateApprovalFlow == "TES" && approvarSupplier == "FIRST") || 
								(updateApprovalFlow.includes("TES") && approvarSupplier == "SECOND")){
		        			
		        			Ext.getCmp('catCode01').allowBlank=false;
			        		Ext.getCmp('catCode20').allowBlank=false;
			        		Ext.getCmp('glClass').allowBlank=false;
			        		Ext.getCmp('paymentMethod').allowBlank=false;
			        		Ext.getCmp('requisitosFiscales').allowBlank=false;
			        		Ext.getCmp('pmtTrmCxC').allowBlank=false;
			        		Ext.getCmp('explFiscal').allowBlank=false;
			        		Ext.getCmp('catCode01').show();
			        		Ext.getCmp('catCode20').show();
			        		Ext.getCmp('glClass').show();
			        		Ext.getCmp('paymentMethod').show();
			        		Ext.getCmp('requisitosFiscales').show();
			        		Ext.getCmp('pmtTrmCxC').show();
			        		form.findField('explFiscal').setReadOnly(false);
			        		form.findField('fisicaMoral').setReadOnly(false);
			        		
			        		Ext.getCmp('catCode23').allowBlank=false; 
			        		Ext.getCmp('catCode24').allowBlank=false; 
			        		Ext.getCmp('catCode23').show();
			        		Ext.getCmp('catCode24').show();
			        		Ext.getCmp('updateCatCodeSuppForm').show();
		        		}
		        	}
		        		
		        		
		        		
		        		if(countryData == 'MX'){
		        			Ext.getCmp('supRfc').allowBlank=false;
		        			Ext.getCmp('supRfc').show();
		        			Ext.getCmp('taxId').allowBlank=true;
		        			Ext.getCmp('taxId').hide();
		        			
		        			Ext.getCmp('fldColonia').show();
			    			Ext.getCmp('fldColonia').allowBlank=false;
			    			Ext.getCmp('coloniaEXT').hide();
			    			Ext.getCmp('coloniaEXT').allowBlank=true;
			    			
			    			Ext.getCmp('fldMunicipio').show();
							Ext.getCmp('fldMunicipio').allowBlank=false;
		        		}else{
		        			Ext.getCmp('taxId').allowBlank=false;
		        			Ext.getCmp('taxId').show();
		        			Ext.getCmp('supRfc').allowBlank=true;
		        			Ext.getCmp('supRfc').hide();
		        			
		        			Ext.getCmp('coloniaEXT').allowBlank=false;
			    			Ext.getCmp('coloniaEXT').show();
			    			Ext.getCmp('coloniaEXT').setValue(response.data.colonia);
			    			
			    			Ext.getCmp('fldColonia').hide();
			    			Ext.getCmp('fldColonia').allowBlank=true;
			    			
			    			Ext.getCmp('fldMunicipio').hide();
							Ext.getCmp('fldMunicipio').allowBlank=true;
		        		}
		        		
		        		
		        		form.findField('nombreContactoCxC').setReadOnly(true);
		        		form.findField('telefonoContactoCxC').setReadOnly(true);
		        		

						if ((role == 'ROLE_PURCHASE' && approvalStep == 'FIRST' && approvalStatus == 'PENDIENTE') ||
							(role == 'ROLE_ADMIN')) {

							form.findField('outSourcing').setReadOnly(false);

						}

		        		//form.findField('currencyCode').setValue(rec.data.currencyCode); 
		        		
		        		//Ext.getCmp('updateSupplierForm').show();
		        		
		        		if(role=='ROLE_ADMIN' ||role=='ROLE_CXP'||role == 'ROLE_PURCHASE'){
		        			
		        			//Ext.getCmp('updateSupplierForm').show();
		        		}else{
		        			Ext.getCmp('updateSupplierForm').hide();
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
			        	
						box.hide();
					},
					failure : function() {
						 box.hide();
					}
				});
        }
    },
    
    searchAppSupplier: function(button) {
    	var grid = this.getApprovalSearchGrid();
    	var store = grid.getStore();
    	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);

       	var ticketId = Ext.getCmp('supSearchTicket').getValue();
    	var approvalStep = Ext.getCmp('supSearchApprLevel').getValue();
    	var approvalStatus = Ext.getCmp('supSearchTicketSts').getValue();
    	var currentApprover = Ext.getCmp('supSearchApprover').getValue();
    	/*
    	var fechaAprobacion = Ext.getCmp('fechaAprobacion').getValue();
    	
    	*/
    	var name = Ext.getCmp('supSearchName').getValue();
    	store.loadData([], false);
    	grid.getView().refresh();
    	
    	Ext.Ajax.request({
			url : 'approval/search.action',
			method : 'POST',
				params : {
					ticketId:ticketId,
					approvalStep:approvalStep,
					approvalStatus:approvalStatus,
					fechaAprobacion:'',
					currentApprover:currentApprover,
					name:name
				},
				success : function(response,opts) {
					response = Ext.decode(response.responseText);
					if(response.data != null){
						for(var i = 0; i < response.data.length; i++){
				    		var r = Ext.create('SupplierApp.model.SupplierDTO',response.data[i]);
				    		store.insert(i, r);
						}
					}
						
					box.hide();
					},
					failure : function() {
						 box.hide();
					}
				});
 
    }
});