Ext.define('SupplierApp.controller.FiscalDocuments', {
    extend: 'Ext.app.Controller',
    stores: ['FiscalDocuments'],
    models: ['FiscalDocuments'],
    views: ['fiscalDocuments.FiscalDocumentsPanel','fiscalDocuments.FiscalDocumentsGrid','fiscalDocuments.FiscalDocumentForm',
			'fiscalDocuments.FiscalDocumentDetailPanel', 'fiscalDocuments.ForeignFiscalDocumentsForm','fiscalDocuments.ComplementoPagoPanelFD',
			'fiscalDocuments.AcceptInvGridFD','fiscalDocuments.SelInvGridFD'],
    refs: [
	    {
	        ref: 'fiscalDocumentsGrid',
	        selector: 'fiscalDocumentsGrid'
	    },
	    {
	        ref: 'fiscalDocumentForm',
	        selector: 'fiscalDocumentForm'
	    },
	    {
	    	ref: 'foreignFiscalDocumentsForm',
	    	selector: 'foreignFiscalDocumentsForm'
	    },
	    {
	    	ref: 'complementoPagoPanelFD',
	    	selector: 'complementoPagoPanelFD'
	    },
	    {
	    	ref: 'acceptInvGridFD',
	    	selector: 'acceptInvGridFD'
	    },
	    {
	    	ref: 'selInvGridFD',
	    	selector: 'selInvGridFD'
	    }],
 
    init: function() {
		this.winLoadInv = null;
		this.winLoadInvFile = null;
		this.winDetail = null;
        this.control({
	            'fiscalDocumentsGrid': {
	            	itemdblclick: this.gridSelectionChange
	            },
				'fiscalDocumentForm button[action=uploadZipFile]' : {
					click : this.uploadZipFile
				},	            
				'fiscalDocumentsGrid button[action=uploadNewFiscalDoc]' : {
					click : this.invLoad
				},
				'foreignFiscalDocumentsForm button[action=uploadForeignDocsFD]' : {
					click: this.uploadForeignAdditional
				},
				'foreignFiscalDocumentsForm button[action=sendForeignRecordFD]' : {
					click: this.sendForeignRecord
				},
				'#uploadAdditional' : {
					"buttonclick" : this.uploadAdditional
				},
				'#approveInvoiceFD' : {
					"buttonclick" : this.approveInvoiceFD
				},
				'#rejectInvoiceFD' : {
					"buttonclick" : this.rejectInvoiceFD
				},
				'#acceptSelInvFD' : {
					"buttonclick" : this.acceptSelInvFD
				},
				'#rejectSelInvFD' : {
					"buttonclick" : this.rejectSelInvFD
				},
				'acceptInvGridFD button[action=loadComplFileFD]' : {
					click: this.loadComplFileFD
				},
				'fiscalDocumentsGrid button[action=fdSearch]' : {
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
        });
    },

    uploadZipFile : function(grid, rowIndex, colIndex, record) {
        var me = this;
        var form = this.getFiscalDocumentForm().getForm();
        var grid = this.getFiscalDocumentsGrid();
        var store = grid.getStore();
        var values = form.getFieldValues();
    	var filePanel = Ext.create(
    					'Ext.form.Panel',
    					{
    						width : 900,
    						items : [
    						         {
    									xtype : 'textfield',
    									name : 'addressNumber',
    									hidden : true,
    									value : values.addressNumber
    								},
    								{
    									xtype : 'textfield',
    									name : 'uuid',
    									hidden : true,
    									value : values.uuidFactura
    								},{
    									xtype : 'textfield',
    									name : 'company',
    									hidden : true,
    									value : values.rfcReceptor
    								},{
    									
    									xtype : 'filefield',
    									name : 'uploadedFiles[0]',
    									id: 'file',
    									fieldLabel : 'Archivo(.zip):',
    									labelWidth : 120,
    									msgTarget : 'side',
    									allowBlank : false,
    									margin:'15 0 70 0',
    									anchor : '90%',
    									buttonText : SuppAppMsg.suppliersSearch,
    									  listeners: {  
    					                      change: function (value) {  
    					                           var file =   Ext.getCmp('file').getEl().down('input[type=file]').dom.files[0]
    						                       if (file.size > 20971520) {
    					                    	   Ext.getCmp('file').setRawValue("");
    					                    	   Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:   'El archivo supera los 20 MB'});
    					                    		                    	   
    					                       }
    					                      
    					                      }  
    					                    }, 
    								},{ xtype: 'label',
								        text: 'Periodo',
								        margin: '0 0 0 10'},
									{
								        xtype: 'comboMes',
										name : 'mes',
										id:"mes",
									},
									{
										xtype : 'numberfield',
										anchor : '45%',
										name : 'anio',
										id:"anio",
										fieldLabel : '&nbsp;Año',
										value : new Date()
												.getFullYear(),
										maxValue : new Date()
												.getFullYear() + 1,
										minValue : new Date()
												.getFullYear() - 1
									} ,{ xtype: 'textfield',
								        id:"periodTextVale",
								        name : 'periodTextVale',
								        hidden : true
								        }
									,{ xtype: 'textfield',
								        id:"periodText",
								        name : 'periodText',
								        hidden : true
								        } ],

    						buttons : [ {
    							text : SuppAppMsg.supplierLoad,
    							margin:'10 0 0 0',
    							handler : function() {
    								
    								 Ext.getCmp('periodTextVale').setValue(Ext.getCmp('mes').getValue()+","+Ext.getCmp('anio').getValue());
                                     Ext.getCmp('periodText').setValue(peridos(Ext.getCmp('mes').getValue())+Ext.getCmp('anio').getValue());
    								var form = this.up('form').getForm();
    								if (form.isValid()) {
    									form.submit({
    												url : 'uploadOSInvoiceFiles2.action',
    												waitMsg : SuppAppMsg.supplierLoadFile,
    												success : function(fp, o) {
    													var res = Ext.decode(o.response.responseText);
    													Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  SuppAppMsg.supplierLoadDocSucc});
    													me.winLoadInv.close();
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
    		title : 'Archivos de nómina timbrados',
    		width : 600,
    		height : 300,
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
        	
        	var uuid = null;
        	
        	if(record.data.type == 'FACTURA' || record.data.type == 'FACT EXTRANJERO'){
        		uuid = record.data.uuidFactura;
        	}else if(record.data.type == 'NOTACREDITO'){
        		uuid = record.data.uuidNotaCredito;
        	}
        	if(record.data.serie=='null')record.data.serie='';
        	
        	var box = Ext.MessageBox.wait(SuppAppMsg.approvalLoadRegistrer, SuppAppMsg.approvalExecution);

        	this.winDetail = new Ext.Window({
        		layout : 'fit',
        		title : SuppAppMsg.fiscalTitle1,
        		//width : 550,
        		//height : 550,
        		maxWidth: 500,
        	    maxHeight: 750,
        	    width: Ext.Element.getViewportWidth() * 0.35,  // 80% del ancho
        	    height: Ext.Element.getViewportHeight() * 0.6,
        		modal : true,
        		closeAction : 'destroy',
        		resizable : false,
        		minimizable : false,
        		maximizable : false,
        		plain : true,
        		items : [ {
        			xtype : 'fiscalDocumentDetailPanel',
        			border : true,
        			//height : 415
        		}  ]

        	});
        	
        	this.winDetail.show();        	
        	var form = this.getFiscalDocumentForm().getForm();
        	form.loadRecord(record);

        	//Ext.getCmp('fileListHtmlInvoice').setValue("");
        	//fiscalDocumentDetailPanel.getView().refresh();
        	//var g = this.getFiscalDocumentForm().getForm();
	    	//var g = this.getView();	
        	//form.getView().refresh();
        	
        	//Ext.ComponentQuery.query('fiscalDocumentDetailPanel')[0].show();
        	//Ext.ComponentQuery.query('fiscalDocumentDetailPanel')[0].doLayout();
        	
        	if(record.data.status == 'APROBADO' || record.data.status == 'PENDIENTE'){
        		Ext.getCmp('uploadZipFile').hide();
        	}
        	
        	if(role !='ROLE_SUPPLIER') {
        		Ext.getCmp('uploadZipFile').hide();
        	}        	
        	
        	Ext.Ajax.request({
				url : 'documents/listConceptDocumentsByFiscalRef.action',
				method : 'GET',
					params : {
						start : 0,
						limit : 20,
						addresNumber: record.data.addressNumber,
						uuid: record.data.type=="NOTACREDITO"?record.data.uuidFactura:uuid
					},
					success : function(response,opts) {
						response = Ext.decode(response.responseText);
						var index = 0;
						var files = "";
						//var filesConcept1 = "";
						for (index = 0; index < response.data.length; index++) {
							var href = "documents/openDocument.action?id=" + response.data[index].id;
							var fileHref = "";
							
							if(response.data[index].id !== 0){
								fileHref = "<a href= '" + href + "' target='_blank'>" +  response.data[index].name + "</a>";
	                            files = files + "> " + fileHref + " - " + response.data[index].size + " bytes : " + response.data[index].fiscalType + "<br />";
							}						
						
                            if(response.data[index].fiscalType == "Factura" || response.data[index].fiscalType == "Evidencia" || response.data[index].fiscalType == "Otros"|| response.data[index].fiscalType == "NotaCredito"){
                            	if(index ==0 ){
                            		Ext.getCmp('fileListHtmlMainInvoice').setValue("");
                            	}
                            	Ext.getCmp('fileListHtmlMainInvoice').setValue(Ext.getCmp('fileListHtmlMainInvoice').getValue() + fileHref + "<br />");
                            }
                            if(response.data[index].documentType == "CNT"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept001').setValue(Ext.getCmp('fileListHtmlInvoiceConcept001').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept001').show();									
								}
                            	Ext.getCmp('conceptAmount001').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount001').show();
                            }
                            if(response.data[index].documentType == "Validation"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept002').setValue(Ext.getCmp('fileListHtmlInvoiceConcept002').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept002').show();
								}
                            	Ext.getCmp('conceptAmount002').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount002').show();
                            }
                            if(response.data[index].documentType == "Maneuvers"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept003').setValue(Ext.getCmp('fileListHtmlInvoiceConcept003').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept003').show();
								}
                            	Ext.getCmp('conceptAmount003').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount003').show();
                            }
                            if(response.data[index].documentType == "Deconsolidation"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept004').setValue(Ext.getCmp('fileListHtmlInvoiceConcept004').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept004').show();
								}
                            	Ext.getCmp('conceptAmount004').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount004').show();
                            }
                            if(response.data[index].documentType == "RedManeuvers"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept005').setValue(Ext.getCmp('fileListHtmlInvoiceConcept005').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept005').show();
								}
                            	Ext.getCmp('conceptAmount005').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount005').show();
                            }
                            if(response.data[index].documentType == "Fumigation"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept006').setValue(Ext.getCmp('fileListHtmlInvoiceConcept006').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept006').show();
								}
                            	Ext.getCmp('conceptAmount006').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount006').show();
                            }
                            if(response.data[index].documentType == "Docking"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept007').setValue(Ext.getCmp('fileListHtmlInvoiceConcept007').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept007').show();
								}
                            	Ext.getCmp('conceptAmount007').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount007').show();
                            }
                            if(response.data[index].documentType == "Storage"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept008').setValue(Ext.getCmp('fileListHtmlInvoiceConcept008').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept008').show();
								}
                            	Ext.getCmp('conceptAmount008').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount008').show();
                            }
                            if(response.data[index].documentType == "Delays"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept009').setValue(Ext.getCmp('fileListHtmlInvoiceConcept009').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept009').show();
								}
                            	Ext.getCmp('conceptAmount009').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount009').show();
                            }
                            if(response.data[index].documentType == "Dragging"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept010').setValue(Ext.getCmp('fileListHtmlInvoiceConcept010').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept010').show();
								}
                            	Ext.getCmp('conceptAmount010').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount010').show();
                            }
                            if(response.data[index].documentType == "Permissions"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept011').setValue(Ext.getCmp('fileListHtmlInvoiceConcept011').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept011').show();
								}
                            	Ext.getCmp('conceptAmount011').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount011').show();
                            }
                            if(response.data[index].documentType == "Duties"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept012').setValue(Ext.getCmp('fileListHtmlInvoiceConcept012').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept012').show();
								}
                            	Ext.getCmp('conceptAmount012').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount012').show();
                            }
                            if(response.data[index].documentType == "Other1"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept013').setValue(Ext.getCmp('fileListHtmlInvoiceConcept013').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept013').show();
								}
                            	Ext.getCmp('conceptAmount013').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount013').show();
                            }
                            if(response.data[index].documentType == "Other2"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept014').setValue(Ext.getCmp('fileListHtmlInvoiceConcept014').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept014').show();
								}
                            	Ext.getCmp('conceptAmount014').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount014').show();
                            }
                            if(response.data[index].documentType == "Other3"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept015').setValue(Ext.getCmp('fileListHtmlInvoiceConcept015').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept015').show();
								}
                            	Ext.getCmp('conceptAmount015').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount015').show();
                            }
                            if(response.data[index].documentType == "NoPECEAccount"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept016').setValue(Ext.getCmp('fileListHtmlInvoiceConcept016').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept016').show();
								}
                            	Ext.getCmp('conceptAmount016').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount016').show();
                            }
                            if(response.data[index].documentType == "DTA"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept017').setValue(Ext.getCmp('fileListHtmlInvoiceConcept017').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept017').show();
								}
                            	Ext.getCmp('conceptAmount017').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount017').show();
                            }
                            if(response.data[index].documentType == "IVA"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept018').setValue(Ext.getCmp('fileListHtmlInvoiceConcept018').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept018').show();
								}
                            	Ext.getCmp('conceptAmount018').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount018').show();
                            }
                            if(response.data[index].documentType == "IGI"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept019').setValue(Ext.getCmp('fileListHtmlInvoiceConcept019').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept019').show();
								}
                            	Ext.getCmp('conceptAmount019').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount019').show();
                            }
                            if(response.data[index].documentType == "PRV"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept020').setValue(Ext.getCmp('fileListHtmlInvoiceConcept020').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept020').show();
								}
                            	Ext.getCmp('conceptAmount020').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount020').show();
                            }
                            if(response.data[index].documentType == "IVAPRV"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept021').setValue(Ext.getCmp('fileListHtmlInvoiceConcept021').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept021').show();
								}
                            	Ext.getCmp('conceptAmount021').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount021').show();
                            }
                            if(response.data[index].documentType == "ManeuversNoF"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept022').setValue(Ext.getCmp('fileListHtmlInvoiceConcept022').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept022').show();
								}
                            	Ext.getCmp('conceptAmount022').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount022').show();
                            }
                            if(response.data[index].documentType == "DeconsolidationNoF"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept023').setValue(Ext.getCmp('fileListHtmlInvoiceConcept023').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept023').show();
								}
                            	Ext.getCmp('conceptAmount023').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount023').show();
                            }
                            if(response.data[index].documentType == "Other1NoF"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept024').setValue(Ext.getCmp('fileListHtmlInvoiceConcept024').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept024').show();
								}
                            	Ext.getCmp('conceptAmount024').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount024').show();
                            }
                            if(response.data[index].documentType == "Other2NoF"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept025').setValue(Ext.getCmp('fileListHtmlInvoiceConcept025').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept025').show();
								}
                            	Ext.getCmp('conceptAmount025').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount025').show();
                            }
                            if(response.data[index].documentType == "Other3NoF"){
								if(response.data[index].id !== 0){
	                            	Ext.getCmp('fileListHtmlInvoiceConcept026').setValue(Ext.getCmp('fileListHtmlInvoiceConcept026').getValue() + fileHref + "<br />");
	                            	Ext.getCmp('fileListHtmlInvoiceConcept026').show();
								}
                            	Ext.getCmp('conceptAmount026').setValue(response.data[index].description);
                            	Ext.getCmp('conceptAmount026').show();
                            }
						}					
						 //Ext.getCmp('fileListHtmlInvoice').setValue(files);
					},
					failure : function() {
					}
				});        	
            box.hide();
            //Ext.ComponentQuery.query('fiscalDocumentDetailPanel')[0].doLayout();
            Ext.ComponentQuery.query('fiscalDocumentDetailPanel')[0].updateLayout();
        }
    },
    
    approveInvoiceFD : function(grid, rowIndex, colIndex, record) {
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var orderNumber = record.data.orderNumber;
    	var dto = Ext.create('SupplierApp.model.FiscalDocuments',record.data);
    	var status ="APROBADO";
    	var currentApprover = addressNumber;
    	var nextApprover = record.data.nextApprover;
    	var step = record.data.approvalStep;
    	
    	var filePanel = Ext.create(
				'Ext.form.Panel',
				{
				//	width: 510,
					layout: 'anchor',
			        bodyPadding: 10,
			        defaults: {
			            anchor: '100%',
			            labelWidth: 150,
			            margin: '5 0 0 0'
			        },
					items : [{
								fieldLabel : 'Centro de Costos',
								xtype : 'textfield',
								name : 'centroCostos',
								id:'centroCostosFD',
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
								id:'conceptoArticuloFD',
								//width:300,
								//colspan:3,
								//margin:'5 0 0 5',
								//allowBlank:orderNumber==0?false:true,
								//hidden:orderNumber==0?false:true,
								hidden:true								
							},{
								fieldLabel : 'Compañia',
								xtype : 'textfield',
								name : 'companyFD',
								id:'companyFD',
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
								id : 'notesFD',
								//width:500,
								//colspan:3,
								//margin:'10 0 0 10'
							}],

					buttons : [{
						text : SuppAppMsg.approvalApprove,
						cls: 'buttonStyle',
						handler : function() {
							var form = this.up('form').getForm();
							if (form.isValid()) {
								var centroCostos = Ext.getCmp('centroCostosFD').getValue();
								var concepto = Ext.getCmp('conceptoArticuloFD').getValue();
								var company = Ext.getCmp('companyFD').getValue();
								var note = Ext.getCmp('notesFD').getValue();
								
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
												    url: 'fiscalDocuments/update.action',
												    method: 'POST',
												    timeout: 120000,
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
    		//width : 550,
    		//height : 100,
    		width : '30%',   // 
    	    height : 150,  
    		modal : true,
    		closeAction : 'destroy',
    		resizable : false,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		items : [ filePanel ],
    		 responsiveConfig: {
    	            'width < 768': {
    	                width: '95%',
    	                height: 250
    	            },
    	            'width >= 768': {
    	                width: '60%',
    	                height: 200
    	            }
    	        }

    	});
    	me.winLoadInv.show(); 
    	
    	
    },
    
    rejectInvoiceFD : function(grid, rowIndex, colIndex, record) {
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var dto = Ext.create('SupplierApp.model.FiscalDocuments',record.data);
    	var status ="RECHAZADO";
    	var currentApprover = addressNumber;
    	var nextApprover = record.data.nextApprover;
    	var step = record.data.approvalStep;
    	
    	var winWidth = Ext.Element.getViewportWidth();
    	var boxWidth = Math.min(Math.max(winWidth * 0.5, 400), 500);
    	
    	var dlgRejected = Ext.MessageBox.show({
    		title : SuppAppMsg.rejectDoc,
			msg : SuppAppMsg.approvalNoteReject,
			buttons : Ext.MessageBox.YESNO,
			multiline: true,
			//width:500,
			width: boxWidth, 
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
						    url: 'fiscalDocuments/update.action',
						    method: 'POST',
						    timeout: 120000,
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
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.approvalInvRespRejected);
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
    	
    	dlgRejected.on('afterlayout', function() {
    	    var buttons = dlgRejected.query('button');
    	    
    	    buttons.forEach(function(btn) {
    	        if (btn.text === SuppAppMsg.approvalAcept || btn.text === 'OK') {
    	            btn.addCls('buttonStyle');
    	        } else if (btn.text === SuppAppMsg.approvalExit || btn.text === 'Cancel') {
    	            btn.addCls('buttonStyle');
    	        }
    	    });
    	});
    	
    	dlgRejected.textArea.inputEl.set({
		    maxLength: 255
		});
    	dlgRejected.textArea.setWidth(boxWidth - 40);
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
    	var grid = this.getFiscalDocumentsGrid();
    	var store = grid.getStore();
    	
    	var poNumber = Ext.getCmp('poNumberFD').getValue() ==''?'0':Ext.getCmp('poNumberFD').getValue();
    	var supNumber = Ext.getCmp('supNumberFD').getValue() == ''?'':Ext.getCmp('supNumberFD').getValue();
    	var fdUUID = Ext.getCmp('fdUUID').getValue() == ''?'':Ext.getCmp('fdUUID').getValue();
    	var documentType = Ext.getCmp('comboDocumentType').getValue() == ''?'':Ext.getCmp('comboDocumentType').getValue();
    	var invoiceStatus = Ext.getCmp('comboInvoiceStatus').getValue() == ''?'':Ext.getCmp('comboInvoiceStatus').getValue();
    	/*var poFromDate = Ext.getCmp('poFromDate').getValue();
    	var poToDate = Ext.getCmp('poToDate').getValue();
    	var poStatus = Ext.getCmp('combostatus').getValue();*/

    	/*var comboStatus = status[poStatus];
    	
    	grid.headerCt.items.getAt(0).hide();
    	
    	if(comboStatus == status.STATUS_OC_INVOICED){
        	grid.headerCt.items.getAt(0).show();
    	}
    	
    	if((comboStatus == status.STATUS_OC_SENT && role=='ROLE_TAX') || (comboStatus == status.STATUS_OC_SENT && role=='ROLE_ADMIN')){
        	grid.headerCt.items.getAt(0).show();
    	}

    	if(comboStatus == status.STATUS_OC_PROCESSED && role == 'ROLE_WNS'){
        	grid.headerCt.items.getAt(0).show();
    	}
    	
    	if(comboStatus){
    		
    	}else{
    		comboStatus='';
    	}
    	
    	*/

    	store.removeAll();    	
    	store.proxy.extraParams = {
    							orderNumber : poNumber?poNumber:"",
				    			addressNumber : supNumber?supNumber:"",
				    	    	status: invoiceStatus?invoiceStatus:"",
				    	    	uuid: fdUUID?fdUUID:"",
				    	    	documentType: documentType?documentType:""
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


