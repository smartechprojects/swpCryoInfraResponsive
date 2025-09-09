Ext.define('SupplierApp.controller.Company', {
    extend: 'Ext.app.Controller',
    stores: ['Company'],
    models: ['Company'],
    views: ['company.CompanyPanel','company.CompanyForm','company.CompanyGrid'],
    refs: [{
        	ref: 'companyForm',
        	selector: 'companyForm'
	    },
	    {
	        ref: 'companyGrid',
	        selector: 'companyGrid'
	    }],
 
    init: function() {
    	
    	this.winLoadCompany = null;
    	
        this.control({
            'companyGrid': {
                selectionchange: this.gridSelectionChange
            },
            'companyForm button[action=saveCompany]': {
                click: this.saveCompany
            },
            'companyForm button[action=companyNew]': {
                click: this.resetCompanyForm
            },
            'companyForm button[action=updateCompany]': {
                click: this.updateCompany
            },
            '#searchCompany' : {
                "ontriggerclick": this.loadSearchList
            },
			'companyForm button[action=loadTaxFileRef]' : {
				click : this.loadDoc
			},
			'companyForm button[action=loadLogoFileRef]' : {
				click : this.loadDoc
			},
			'companyForm button[action=loadCerFileRef]' : {
				click : this.loadDoc
			},
			'companyForm button[action=loadKeyFileRef]' : {
				click : this.loadDoc
			}
        });
    },

    loadDoc : function(button) {
    	var cForm = this.getCompanyForm().getForm();
    	var me = this;
		if (cForm.isValid()) {
    	
    	var values = cForm.getFieldValues();
    	var supField = "";
    	var docType = "";
    	var labelText = "";
    	
    	switch (button.action) {
    	  case 'loadTaxFileRef':
    		    supField = 'taxFileRef';
    		    docType = "PKCS12";
    		    labelText = "Archivo .pfx";
    		    break;
    	  case 'loadLogoFileRef':
		        supField = 'logoFileRef';
		        docType = "LOGO";
		        labelText = "Imágen (jpg o jpeg)";
              break;
    	  case 'loadCerFileRef':
  		    supField = 'cerFileRef';
  		    docType = "CERT";
  		    labelText = "Archivo .cer";
  		    break;
    	  case 'loadKeyFileRef':
  		    supField = 'keyFileRef';
  		    docType = "KEY";
  		    labelText = "Archivo .key";
  		    break;
              
    	  default:
            break;
    	};

    	var winAlertWidth = Ext.Element.getViewportWidth();
    	var boxAlertWidth = Math.min(Math.max(winWidth * 0.5, 300), 500); // 50% de la pantalla, mínimo 300, máximo 500

    	var filePanel = Ext.create(
    					'Ext.form.Panel',
    					{
    						//width : 900,	
    						layout: {
    			                type: 'vbox',
    			                align: 'stretch'
    			            },
    			            bodyPadding: 10,
    			            defaults: {
    			                anchor: '100%',
    			                margin: '5 0'
    			            },
    						items : [{
    									xtype : 'textfield',
    									name : 'company',
    									hidden : true,
    									margin:'20 0 0 10',
    									value : values.company 
    								},{
    									xtype : 'textfield',
    									name : 'documentType',
    									hidden : true,
    									value : docType
    								},
    								{
    									xtype : 'filefield',
    									name : 'file',
    									fieldLabel :labelText,
    									labelWidth : 120,
    									msgTarget : 'side',
    									allowBlank : false,
    									margin:'20 0 70 0',
    									//anchor : '90%',
    									buttonText : SuppAppMsg.suppliersSearch
    								} ],

    						buttons : [ {
    							text : SuppAppMsg.supplierLoad,
    							margin:'10 0 0 0',
    							handler : function() {
    								var form = this.up('form').getForm();
    								if (form.isValid()) {
    									form.submit({
    												url : 'uploadCompanyFile.action',
    												waitMsg : SuppAppMsg.supplierLoadFile,
    												success : function(fp, o) {
    													var res = Ext.decode(o.response.responseText);
    													Ext.MessageBox.alert({ maxWidth: 400, minWidth: 400, width: boxWidth, title: SuppAppMsg.supplierMsgValidationLoad, msg: 'El archivo ha sido validado de forma exitosa' });
    													cForm.findField(supField).setValue(res.fileName);
    													me.getCompanyGrid().getStore().load();
    													me.winLoadCompany.close();
    												},       // If you don't pass success:true, it will always go here
    										        failure: function(fp, o) {
    										        	var res = Ext.decode(o.response.responseText);
    										        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, width: boxWidth, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
    										        }
    											});
    								}
    							}
    						} ]
    					});

    	var winWidth = Ext.Element.getViewportWidth();
    	var boxWidth = Math.min(Math.max(winWidth * 0.5, 400), 600); 
    	var winHeight = Ext.Element.getViewportHeight();
    	var boxHeight = Math.min(Math.max(winHeight * 0.3, 150), 300); // altura entre 150 y 300
    	 
    	this.winLoadCompany = new Ext.Window({
    		layout : 'fit',
    		title : 'Carga de archivos',
    		//width : 600,
    		//height : 160,
    		modal : true,
    		layout: 'fit',
    		width : boxWidth,
    		height: boxHeight,
            height: 180,
    		minHeight: 150,
            scrollable: true,
    		closeAction : 'destroy',
    		resizable : false,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		items : [ filePanel ]

    	});
    	
    	this.winLoadCompany.show();
    	
		}
    },
        
    gridSelectionChange: function(model, records) {   	
        if (records[0]) {
        	var form = this.getCompanyForm().getForm();
        	var box = Ext.MessageBox.wait(SuppAppMsg.approvalLoadRegistrer, SuppAppMsg.approvalExecution);
        	form.loadRecord(records[0]);
            this.enableUpdate();
            box.hide();
            
            form.findField('secretPass').hide();
            form.findField('secretPassCer').hide();
            form.findField('keyFileRef').hide();
            form.findField('cerFileRef').hide();
            //form.findField('taxFileRef').hide();
            //Ext.getCmp('loadTaxFileRef').hide();
            Ext.getCmp('loadCerFileRef').hide();
            Ext.getCmp('loadKeyFileRef').hide();
            Ext.getCmp('pfxTitle').setValue('ARCHIVO .PFX: ' + records[0].data.attachId + '.pfx');
            Ext.getCmp('cerTitle').setValue('');

        }
    },
    
    loadSearchList: function (event){
    	this.getCompanyGrid().getStore().getProxy().extraParams={
    		query:event.getValue()
    	};
    	this.getCompanyGrid().getStore().load();
    },

    saveCompany: function (button) {  	
    	var form = this.getCompanyForm().getForm();
    	var grid = this.getCompanyGrid();
    	
    	if (form.isValid()) { 
        	var record = Ext.create('SupplierApp.model.Company');
        	values = form.getFieldValues();
        	
        	if(values.secretPass == ''){
        		values.secretPass = values.secretPassCer;
        	}
        	
        	values.attachId = uuidv4();
        	
        	updatedRecord = populateObj(record, values);
 
            if (values.id > 0){
            	Ext.Msg.alert( SuppAppMsg.companySaveError, "Error");
    		} else{
    	    	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    	    	record.set(updatedRecord);
    	    	record.save({callback: function (records, o, success) { 
			    	if(success){
			    		
				    	//var res = Ext.decode(o.response.responseText);
		        	    grid.store.load();
		        	    form.reset();
				    	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.requisition_result, msg:  "La compañía ha sido creada exitosamente"});
			    	}else{
			    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.requisition_result, msg:  "Ha ocurrido un error al procesar su solicitud"});
			    	}
			    }
    	    });
        	    
    		}
	    	box.hide();
    	}
    },
    
    resetCompanyForm: function (button) {
    	var form = this.getCompanyForm().getForm();
    	form.reset();
    	this.enableSave();
    	
        form.findField('secretPass').show();
        form.findField('secretPassCer').show();
        form.findField('keyFileRef').show();
        form.findField('cerFileRef').show();
        form.findField('taxFileRef').show();
        Ext.getCmp('loadTaxFileRef').show();
        Ext.getCmp('loadCerFileRef').show();
        Ext.getCmp('loadKeyFileRef').show();
        Ext.getCmp('pfxTitle').setValue('ARCHIVO .PFX');
        Ext.getCmp('cerTitle').setValue('ARCHIVOS DE CERTIFICADO PARA GENERAR .PFX');
        
    },
        
    updateCompany: function (button) {
    	var form = this.getCompanyForm().getForm();
    	var grid = this.getCompanyGrid();
    	if (form.isValid()) { 
        	var record = form.getRecord();
        	values = form.getFieldValues();
        	updatedRecord = populateObj(record, values);

            if (values.id > 0){
    	    	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    	    	record.set(updatedRecord);
				record.save();
        	    grid.store.load();
        	    form.reset();
        	    this.enableSave();
        	    box.hide();
    		} else{
    			Ext.Msg.alert(SuppAppMsg.supplierUpdateFail, "Error");
    		}
    	}
    },
    
	enableUpdate: function(){
		Ext.getCmp('saveCompany').setDisabled(true);
		Ext.getCmp('updateCompany').setDisabled(false);
		//Ext.getCmp('idCompany').setReadOnly(true);
		var form = this.getCompanyForm(); // tu ref al formulario
		var field = form.down('#idCompany');
		if (field) {
		    field.setReadOnly(true);
		}
	},

	enableSave: function(){
		Ext.getCmp('saveCompany').setDisabled(false);
		Ext.getCmp('updateCompany').setDisabled(true);
		//Ext.getCmp('idCompany').setReadOnly(false);
		var form = this.getCompanyForm(); // tu ref al formulario
		var field = form.down('#idCompany');
		if (field) {
		    field.setReadOnly(false);
		}
	},
	
	initController: function(){
	}
});