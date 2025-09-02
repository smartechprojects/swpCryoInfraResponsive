var SuppAppMsg = {};

Ext.application({
    name: 'SupplierApp',
    controllers: ['Supplier'],
    autoCreateViewport: false,
	appFolder: 'app',
	launch: function () {
        Ext.QuickTips.init();
        app = this;

		var langu = window.navigator.language;
		var lang = "";
		if(langu.startsWith("es", 0)){
			lang = "es";
		}else{
			lang = "en";
		}
		
		var box = Ext.MessageBox.wait("Loading data...","");
		
		var me = this;
		Ext.Ajax.request({
		    url: 'getLocalization.action',
		    method: 'GET',
		    params: {
		    	lang : lang
	        },
		    success: function(fp, o) {
		    	var resp = Ext.decode(fp.responseText, true);
		    	SuppAppMsg = resp.data;
		    	
		    	ticketAccepted = document.getElementById("ticketAccepted").value;
		    	if(ticketAccepted != null && ticketAccepted != ''){
			    	Ext.Ajax.request({
					    url: 'public/getByTicketId.action',
					    method: 'GET',
					    params: {
					    	ticketId : ticketAccepted
				        },
					    success: function(fp, o) {
					    	var resp = Ext.decode(fp.responseText, true);
							if(!me.mainPanel){
								me.mainPanel = Ext.create('Ext.panel.Panel', {
							        autoWidth: true,
							        renderTo: 'content',
							        layout:'fit',
							        border:false,
							        frame:false,
							        items: [{
							        	xtype: 'supplierForm'
									}]
								});
							}

							if(resp.data != null){
								rec = Ext.create('SupplierApp.model.Supplier');
								rec.set(resp.data);
								/*if(rec.data.addresNumber != '' && rec.data.approvalStatus != 'RENEW'){
									box.hide();
									Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMessage, msg: 'Su información ya no se encuentra disponible como borrador. Si ha iniciado el proceso de activación, deberá esperar a que su cuenta sea creada y la notificación se envíe a su cuenta de correo.' });
									return;
								}else */if(rec.data.approvalStatus == 'PENDIENTE' || rec.data.approvalStatus == 'APROBADO'){
									box.hide();
									Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMessage, msg: 'Su información ya no se encuentra disponible como borrador. Si ha iniciado el proceso de activación, deberá esperar a que su cuenta sea creada y la notificación se envíe a su cuenta de correo.' });
									return;
								}
								
								var form = Ext.getCmp('supplierFormId').getForm();
					        	form.loadRecord(rec);
					        	form.findField('codigoPostal').setRawValue(resp.data.codigoPostal);
					        	form.findField('regionesTmp').setValue(resp.data.regiones);
					        	form.findField('categoriasTmp').setValue(resp.data.categorias);
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
							
							
							
					    }
					});
		    	}else{
		    		box.hide();
		    		if(!me.mainPanel){
						me.mainPanel = Ext.create('Ext.panel.Panel', {
					        autoWidth: true,
					        renderTo: 'content',
					        layout:'fit',
					        border:false,
					        frame:false,
					        items: [{
					        	xtype: 'supplierForm'
							}]
						});
					}
		    	}
		    	
				/*if(!me.mainPanel){
					me.mainPanel = Ext.create('Ext.panel.Panel', {
				        autoWidth: true,
				        height:500,
				        renderTo: 'content',
				        layout:'fit',
				        border:false,
				        frame:false,
				        items: [{
				        	xtype: 'supplierForm'
						}]
					});
				}*/
		    }
		});
		
		
	}
});	