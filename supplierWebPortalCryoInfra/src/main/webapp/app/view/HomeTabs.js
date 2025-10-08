Ext.require('Ext.chart.*');
Ext.require(['Ext.layout.container.Fit', 'Ext.window.MessageBox']);
Ext.define('SupplierApp.view.HomeTabs', {
	extend : 'Ext.tab.Panel',
	alias : 'widget.homeTabs',
	frame : false,
	id : 'HomeTabs',
	cls : 'tabpanel',
	activeTab: 0,
    plain: false,
	border : false,
	height:520,
	style : 'background-color:#fff;font-color:#000;margin-top:10px;',
	tabBar: {
        width: 220,
        height: 30,
        defaults: {height:30},

    },
    listeners: {
    	'afterrender': function(component, eOpts) { 
    	   /*
	       if(role=='ROLE_ADMIN' ||  role=='ROLE_PURCHASE'){
	    	   Ext.getCmp('mainPanelId').getComponent('fiscalDocumentsPanel').tab.show();
	    	   Ext.getCmp('mainPanelId').getComponent('customBrokerPanel').tab.show();
	       } else if(role == 'ROLE_SUPPLIER') {
	      		Ext.Ajax.request({
	    		    url: 'supplier/getCustomBroker.action',
	    		    method: 'POST',
	    		    params: {
	    		    	addressNumber : addressNumber
	    	        },
	    		    success: function(fp, o) {
	    		    	var res = Ext.decode(fp.responseText);
	    		    	var isCustomBroker = res.data;
	    		    	if(isCustomBroker == true){
	    		    		Ext.getCmp('mainPanelId').getComponent('fiscalDocumentsPanel').tab.show();
	    		    		Ext.getCmp('mainPanelId').getComponent('customBrokerPanel').tab.show();
	    		    	} else {
	    		    		Ext.getCmp('mainPanelId').getComponent('fiscalDocumentsPanel').tab.hide();
	    		    		Ext.getCmp('mainPanelId').getComponent('customBrokerPanel').tab.hide();
	    		    	}
	    		    },
	    	        failure: function(fp, o) {
	    	        	Ext.getCmp('mainPanelId').getComponent('fiscalDocumentsPanel').tab.hide();
	    	        	Ext.getCmp('mainPanelId').getComponent('customBrokerPanel').tab.hide();
	    	        }
	       		});
	       } else {
	    	   Ext.getCmp('mainPanelId').getComponent('fiscalDocumentsPanel').tab.hide();
	    	   Ext.getCmp('mainPanelId').getComponent('customBrokerPanel').tab.hide();
	       }
	       */
    	},
    
        'tabchange': function (tabPanel, tab) {	
            /*if(tab.id == 'udcTabPanel'){
            	SupplierApp.Current.getController('Udc');
            	var obj = Ext.ComponentQuery.query('udcPanel')
            	tab.add({ xtype: 'udcPanel'});
            	tab.doLayout();
            	tabPanel.getUpdater().refresh();
            }
            */
        	debugger
        	if(tab.id == 'supplierTab') tabChgn = 'suppliers';
        	if(tab.id == 'usersPanelTab'){//Carga valores de combos
            	var roleCombo = Ext.getCmp('usersRoleCombo');
        		roleCombo.store.load();
        		roleCombo.store.reload();
        		
            	var typeCombo = Ext.getCmp('userTypeCombo');
            	typeCombo.store.load();
            	typeCombo.store.reload();
        	}
        }
    },
	initComponent : function() {
		var me = this;
		var supplierProfilePanel = new Ext.FormPanel({
		    title: SuppAppMsg.tabInfoSupplier,
		    hidden:role=='ROLE_SUPPLIER'?false:true,
		    bodyStyle:'padding:5px 5px 0',
		    items: [{
		    	 xtype: 'supplierForm',
	           	 height:490,
	           	 id:'supplierFormId'
		    }]
		});
		
		if(supplierProfile != null){
			var form = Ext.getCmp('supplierFormId');
			form.loadRecord(supplierProfile);
			/*if(supplierProfile.data.country == 'MX'){
				Ext.getCmp('documentContainerForeingResidence').hide();
				
				Ext.getCmp('fldColonia').show();
				Ext.getCmp('fldColonia').allowBlank=false;
				
				Ext.getCmp('coloniaEXT').hide();
				Ext.getCmp('coloniaEXT').allowBlank=true;
				
				Ext.getCmp('fldMunicipio').show();
				Ext.getCmp('fldMunicipio').allowBlank=false;
				
				//Ext.getCmp('searchCP').hide();
				Ext.getCmp('codigoPostal').allowBlank=false;
			}else{
				Ext.getCmp('documentContainerForeingResidence').show();
				
				Ext.getCmp('coloniaEXT').allowBlank=false;
				Ext.getCmp('coloniaEXT').show();
				Ext.getCmp('coloniaEXT').setValue(supplierProfile.data.colonia);
				
				Ext.getCmp('fldColonia').hide();
				Ext.getCmp('fldColonia').allowBlank=true;
				
				Ext.getCmp('fldMunicipio').hide();
				Ext.getCmp('fldMunicipio').allowBlank=true;
				
				Ext.getCmp('searchCP').hide();
				Ext.getCmp('codigoPostal').allowBlank=true;
			}
			
			if(supplierProfile.data.fisicaMoral == '1'){
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
			}else{
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
			}*/
			
			var fileList = supplierProfile.data.fileList;
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
		}
		
		
		var store = Ext.create('Ext.data.JsonStore', {
		    fields: ['name', 'data'],
		    data: [
		        { 'name': 'Recibidas',   'data': 10 },
		        { 'name': 'Enviadas',   'data':  7 },
		        { 'name': 'Facturadas', 'data':  5 },
		        { 'name': 'Aprobadas',  'data':  2 },
		        { 'name': 'Pagadas',  'data': 27 },
		        { 'name': 'Con complemento',  'data': 43 }
		    ]
		});
		
		Ext.Ajax.request({
		    url: 'public/searchSystem.action',
		    method: 'POST',
		    params: {
				udcSystem : 'HOMEPAGE'
	        },
		    success: function(fp, o) {
		    	var res = Ext.decode(fp.responseText);
		    	
		    	document.getElementById('messageHome').innerHTML = res.data[0].strValue1 + ' ' + res.data[0].strValue2;
		    	//messageHome = res.data[0].strValue1; 
		    },
	        failure: function(fp, o) {
	        	document.getElementById('messageHome').innerHTML = welcomeMessage;
	        }
   		});
		
		var chart = Ext.create('Ext.chart.Chart', {
		    width: 300,
		    height: 100,
		    animate: true,
		    store: store,
		    margin:'20 20 20 20',
		    theme: 'Base:gradients',
		    series: [{
		        type: 'pie',
		        angleField: 'data',
		        showInLegend: true,
		        tips: {
		            trackMouse: true,
		            width: 140,
		            height: 28,
		            renderer: function(storeItem, item) {
		                var total = 0;
		                store.each(function(rec) {
		                    total += rec.get('data');
		                });
		                this.setTitle(storeItem.get('name') + ': ' + Math.round(storeItem.get('data') / total * 100) + '%');
		            }
		        },
		        highlight: {
		            segment: {
		                margin: 20
		            }
		        },
		        label: {
		            field: 'name',
		            display: 'rotate',
		            contrast: true,
		            font: '14px Arial'
		        },
		       
		    }]
		});

       var confNotif = '';
       if(role == 'ROLE_SUPPLIER'){
    	   confNotif = ' <div style="text-align: justify; text-justify: inter-word;border:0px;padding:20px;"> ' +
           '<h2 style="text-align:center;">'+ SuppAppMsg.tabNoticePrivacy + '</h2>'+
           '<h2 style="text-align:center;"><a href="http://www.saavienergia.com/aviso-de-privacidad" target="_blank""> ' + SuppAppMsg.tabNoticePrivacyMsg + '</a></h2>'+
           '<div align="center"><IMG SRC="resources/images/CryoInfra-logo-gris.png" width="350" height="175" style="margin-top:30px;"></div>' +
			' </div>';
       }else{
    	   confNotif = ' <div style="text-align: justify; text-justify: inter-word;border:0px;padding:20px;"> ' +
    		            '<h2 style="text-align:center;">'+ SuppAppMsg.tabNoticePrivacy + '</h2>'+
    		            '<h2 style="text-align:center;"><a href="http://www.saavienergia.com/aviso-de-privacidad" target="_blank""> '+ SuppAppMsg.tabNoticePrivacyMsg + '</a></h2>'+
    		            '<div align="center"><IMG SRC="resources/images/CryoInfra-logo-gris.png" width="350" height="175" style="margin-top:30px;"></div>' +
    		            /*
						'“EL TRABAJADOR” reconoce que con motivo de su relación de trabajo con la “EMPRESA”'+
						'y durante el desempeño de sus funciones tendrá acceso a información, materiales,'+
						'sistemas y documentos propiedad exclusiva de la “EMPRESA” y/o de sus clientes,'+
						'proveedores, filiales y/o subsidiarias, incluyendo acceso a cualquier información o'+
						'documentación contenida en la plataforma conocida como “Portal de Proveedores”, misma'+
						'que es considerada como confidencial o reservada (en lo sucesivo la “Información'+
						'Confidencial”).'+ 
						' <br /><br />'+
						' Por tanto, “EL TRABAJADOR” se obliga a (i) manejar la Información Confidencial como'+
						'estrictamente reservada; (ii) no divulgar ni proporcionar a persona alguna, incluyendo a'+
						'familiares, la Información Confidencial, sin la autorización expresa previa y por escrito por'+
						'parte de la “EMPRESA”; (iii) utilizar la Información Confidencial exclusivamente para el'+
						'desempeño de sus funciones y cumplimiento específico de las tareas asignadas y para'+
						'ningún otro propósito; (iv) Nunca permitir a ningún tercero, incluyendo familiares, el acceso'+
						'a la Información Confidencial; (v) a no utilizar dicha Información Confidencial en beneficio'+
						'propio o de terceros; (vi) no reproducir, grabar, copiar Información Confidencial, ni'+
						'removerla de las instalaciones de la “EMPRESA” o de las instalaciones en donde la'+
						'“EMPRESA” preste servicios, excepto cuando se requiera para el desempeño de sus'+
						'labores para la “EMPRESA”, en cuyo caso “EL TRABAJADOR” deberá, en todo momento,'+
						'tomar las medidas necesarias para preservar su confidencialidad y prevenir su divulgación.'+ 
						'<br /><br />'+
						'“EL TRABAJADOR” se obliga además a devolver la Información Confidencial en el'+
						'momento en que la “EMPRESA” así lo requiera, así como al término de su relación laboral,'+
						'sin retener copia alguna de dicha información. “EL TRABAJADOR” deberá reportar'+
						'inmediatamente a la “EMPRESA” la pérdida de Información Confidencial con la finalidad de'+
						'que se tomen las medidas de seguridad correspondientes.'+ 
						'<br /><br />'+
						'El incumplimiento y/o violación por parte de “EL TRABAJADOR” de cualquiera de estas'+
						'obligaciones será considerado como causal de rescisión de su contrato de trabajo sin'+
						'responsabilidad alguna para la “EMPRESA”, independientemente de la responsabilidad civil'+
						'y penal en la que pudiera incurrir “EL TRABAJADOR” de conformidad con la Ley de'+
						'Propiedad Industrial, el Código Penal Federal, o cualquier otra legislación aplicable.'+ 
						'<br /><br />'+
						'Ratifico la obligación de confidencialidad que me corresponde frente a la “EMPRESA”, y'+
						'acepto que las obligaciones que asumo conforme al Aviso de Confidencialidad anterior'+
						'continuarán en vigor indefinidamente al término de mi contrato laboral, aún y cuando la'+
						'relación de trabajo termine por cualquier motivo. Asimismo, asumo el compromiso de no'+
						'destinar para fines personales la información o documentos mencionados en este Aviso'+
						' <br /><br />' +*/
						' </div>';	
       }
       
       
		Ext.apply(this, {
			id : 'mainPanelId',
			renderTo: Ext.getBody(),
			items : [{
				xtype : 'panel',
				title : SuppAppMsg.tabInicio,
				//html: "<IMG SRC='resources/images/CryoInfra-logo-gris.png' width=\"700\" height=\"169\" style='margin-top:100px;'><br /><span style='font-size:16px;text-decoration: underline;' >" + welcomeMessage + "</span>",
				html:"<div style='display: grid; place-items: center;'>" +
						"<div style='width: 50%; font-size:14px; color: black; border: 2px solid royalblue; border-radius: 10px; padding: 10px; margin: 10px;'>" +
							"<div style='display: grid; place-items: center; padding: 10px; margin: 10px;'>" +
								"<img src='resources/images/CryoInfra-logo-gris.png' width=\"40%\" height=\"80%\" style='align: center;'>" +
							"</div>" +
							"<div style='display: grid; place-items: center; padding: 10px; margin: 40px;'>" +
								"<span style='text-align: justify; font-family: Arial, Helvetica, sans-serif; font-size:14px; line-height: 1.5;'>" +
									//"<p id='messageHome' style='padding-left: 10px;'> </p>" +
									"<p style='padding-left: 10px;'>" + SuppAppMsg.homeHeaderMessage + "</p>" +
								"</span>" +
							"</div>" +							
						"</div>" +
					"</div>",						
				bodyStyle:{"background-color":"#fff"}
			    },{
					xtype : 'tokenPanel',
					title : window.navigator.language.startsWith("es", 0)? 'Nuevos Registros':'New Records',
					border : true,
					hidden:role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true
				           
				},{
					xtype : 'supplierPanel',
					title : SuppAppMsg.tabProveedores,
					border : true,
					id: 'supplierTab',
					hidden:role=='ROLE_ADMIN' || role=='ROLE_TAX' || role=='ROLE_TREASURY' || role=='ROLE_PURCHASE_READ' || role=='ROLE_ACCOUNTING'
						|| role=='ROLE_MANAGER' || role=='ROLE_AUDIT_USR' ?false:true
				},{
					xtype : 'outSourcingPanel',
					title : 'Servicios Especializados',
					border : true,
					hidden:role=='ROLE_ADMIN' || role=='ROLE_3RDPARTY' || role=='ROLE_RH' || role=='ROLE_TAX' || role=='ROLE_LEGAL'|| (role=='ROLE_SUPPLIER' && osSupplier == 'TRUE') || role=='ROLE_REPSE' || role=='ROLE_PURCHASE' || role=='ROLE_PURCHASE_IMPORT' ?false:true          
				},{
					xtype : 'codigosSATPanel',
					title : 'Claves SAT',
					hidden: true
				},{
					xtype : 'purchaseOrderPanel',
					title : SuppAppMsg.tabPurchaseOrder,
					hidden:role=='ROLE_ADMIN' || role=='ROLE_MANAGER' || role=='ROLE_PURCHASE_READ' || role=='ROLE_SUPPLIER' || role=='ROLE_AUDIT_USR' ?false:true
				}, {
					xtype : 'customBrokerPanel',
					itemId: 'customBrokerPanel',
					title : SuppAppMsg.tabCustomsBrokers,					
					hidden: true
				}, {
					xtype : 'fiscalDocumentsPanel',
					itemId: 'fiscalDocumentsPanel',
					title : SuppAppMsg.tabFiscalDocuments,					
					hidden:role=='ROLE_ADMIN' || role=='ROLE_MANAGER' || role=='ROLE_PURCHASE_READ'?false:true
				}, {
					xtype : 'freightApprovalPanel',
					itemId: 'freightApprovalPanel',
					title : SuppAppMsg.tabFreightApproval,					
					hidden:role=='ROLE_ADMIN' || role=='ROLE_MANAGER' || role=='ROLE_PURCHASE_READ'|| Flete=='si'?false:true
				}, {
					xtype : 'plantAccessPanel',
					itemId: 'plantAccessPanel',
					title : SuppAppMsg.tabplantaccess,					
					hidden:role=='ROLE_ADMIN' || role=='ROLE_PURCHASE_READ' || role=='ROLE_MANAGER' || role=='ROLE_SUPPLIER'?false:true
				}, {
					xtype : 'taxVaultPanel',
					itemId: 'taxVaultPanel',
					title : SuppAppMsg.tabtaxVault,	
					hidden:role=='ROLE_ADMIN' || role=='ROLE_TAX' || role=='ROLE_TREASURY' || role=='ROLE_ACCOUNTING'
						|| role=='ROLE_MANAGER' || role=='ROLE_FISCAL_USR' || role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE_READ'
						|| role=='ROLE_FISCAL_PRD'|| role=='ROLE_BF_ADMIN' || role=='ROLE_AUDIT_USR' ?false:true
				}, {
					xtype : 'paymentsSuppliersPanel',
					itemId: 'paymentsSuppliersPanel',
					title : SuppAppMsg.paymentsSuppliers
				},{
					xtype : 'approvalSearchPanel',
					title : SuppAppMsg.tabSearchApproval,
					hidden:role=='ROLE_ADMIN' || role=='ROLE_TAX' || role=='ROLE_TREASURY' || role=='ROLE_PURCHASE_READ' || role=='ROLE_ACCOUNTING'
						|| role=='ROLE_MANAGER'?false:true
				},{
					xtype : 'approvalPanel',
					title : SuppAppMsg.tabApproval,
					border : true,
					hidden:role=='ROLE_ADMIN' || role=='ROLE_TAX' || role=='ROLE_TREASURY' || role=='ROLE_ACCOUNTING'?false:true
				},{
					xtype : 'deliverPurchaseOrderPanel',
					title : "Liberación de OC",
					border : true,
					hidden:true
				           
				},{
					xtype : 'nonComplianceSupplierPanel',
					title : SuppAppMsg.tabNonComplianceSupplier,
					hidden:true
				},{
					xtype : 'panel',
					title : "Informes",
					border : true,
					hidden:true
				},{
					xtype : 'logDataPanel',
					title : SuppAppMsg.tabLogs,
					border : true,
					hidden: true
				},{
					xtype : 'usersPanel',
					id: 'usersPanelTab',
					title : SuppAppMsg.tabUsers,
					border : true,
					hidden:role=='ROLE_ADMIN'?false:true
				}, {
					xtype : 'companyPanel',
					title : SuppAppMsg.companys,
					hidden:role=='ROLE_ADMIN'?false:true
					
				},{
					xtype : 'udcPanel',
					id:'udcTabPanel',
					title : SuppAppMsg.tabUDC,
					hidden:role=='ROLE_ADMIN'?false:true
				},
					supplierProfilePanel
				,
				{
					xtype : 'panel',
					title : SuppAppMsg.tabNoticePrivacy,
					margin:'50 100 100 50',
					html: confNotif,
					hidden:true
					 
				} ]
		});
		this.callParent(arguments);
	}
});
