Ext.define('SupplierApp.controller.PlantAccess', {
    extend: 'Ext.app.Controller',
    stores: ['PlantAccess', 'PlantAccessWorker', 'PlantAccessFile'],
    models: ['PlantAccess','PlantAccessDetail','PlantAccessFile'],
    views: ['plantAccess.PlantAccessPanel','plantAccess.PlantAccessGrid',
    	'plantAccess.PlantAccessForm','plantAccess.PlantAccessDetailPanel',
    	'plantAccess.PlantAccessDetailGrid','plantAccess.PlantAccessFileGrid',
    	'plantAccess.PlantAccessWorkerFileGrid','plantAccess.PlantAccessMainPanel',
    	'plantAccess.PlantAccessRequestPanel','plantAccess.PlantAccessWorkerPanel',
    	'plantAccess.PlantAccessRequestForm','plantAccess.PlantAccessRequestDocForm',
    	'plantAccess.PlantAccessRequestGrid','plantAccess.PlantAccessWorkerGrid',
    	'plantAccess.PlantAccessWorkerForm'],
    refs: [
	    {
	        ref: 'plantAccessMainPanel',
	        selector: 'plantAccessMainPanel'
	    },
	    {
	        ref: 'plantAccessRequestPanel',
	        selector: 'plantAccessRequestPanel'
	    },
	    {
	        ref: 'plantAccessRequestForm',
	        selector: 'plantAccessRequestForm'
	    },
	    {
	        ref: 'plantAccessRequestDocForm',
	        selector: 'plantAccessRequestDocForm'
	    },
	    {
	        ref: 'plantAccessRequestGrid',
	        selector: 'plantAccessRequestGrid'
	    },
	    {
	        ref: 'plantAccessWorkerPanel',
	        selector: 'plantAccessWorkerPanel'
	    },
	    {
	        ref: 'plantAccessWorkerForm',
	        selector: 'plantAccessWorkerForm'
	    },
	    {
	        ref: 'plantAccessWorkerGrid',
	        selector: 'plantAccessWorkerGrid'
	    },
	    {
	        ref: 'plantAccessGrid',
	        selector: 'plantAccessGrid'
	    },   
	    {
	        ref: 'plantAccessDetailGrid',
	        selector: 'plantAccessDetailGrid'
	    },
	    {
	        ref: 'plantAccessForm',
	        selector: 'plantAccessForm'
	    },
	    {
	        ref: 'plantAccessFileGrid',
	        selector: 'plantAccessFileGrid'
	    },{
	        ref: 'plantAccessWorkerFileGrid',
	        selector: 'plantAccessWorkerFileGrid'
	    }],
 
    init: function() {
    	this.viewAccessPlant = null;
		this.winLoadInv = null;
		this.winLoadInvFile = null;
		this.winDetail = null;
        this.control({
	    		//NUEVO
	            'plantAccessMainPanel[itemId=paMainPanel]': {
	            	close: this.onCloseMainPanel
	            },
	            //NUEVO
	            'plantAccessRequestGrid': {
	            	afterrender: this.onRequestGridAfterRender
	            },
        		//NUEVO
	            'plantAccessGrid': {
	            	itemdblclick: this.gridSelectionChange
	            },
				'plantAccessGrid button[action=uploadNewFiscalDoc]' : {
					click : this.invLoad
				},
				'plantAccessDocumentsForm button[action=uploadForeignDocsFD]' : {
					click: this.uploadForeignAdditional
				},
				'plantAccessDocumentsForm button[action=sendForeignRecordFD]' : {
					click: this.sendForeignRecord
				},
				'#uploadAdditional' : {
					"buttonclick" : this.uploadAdditional
				},
				'#approvePlantAccess' : {
					"buttonclick" : this.approvePlantAccess
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
        		'#upfileWork' : {
			     "buttonclick" : this.upfileWork
		        },
				'acceptInvGridFD button[action=loadComplFileFD]' : {
					click: this.loadComplFileFD
				},
				'plantAccessGrid button[action=parSearch]' : {
					click : this.parSearch
				},
				//NUEVO
				'plantAccessGrid button[action=addNewPlantAccessRequest]' : {
					click : this.addNewPlantAccessRequest
				},
				'plantAccessGrid button[action=poUploadCreditNoteFile]' : {
					click : this.poUploadCreditNoteFile
				},
				'plantAccessGrid button[action=fdLoadCompl]' : {
					click : this.fdLoadCompl
				},
				'plantAccessWorkerFileGrid button[action=addPlantAccessRequestEmployBtnAct]' : {
					click : this.addPlantAccessRequestEmployBtnAct
				},'plantAccessFileGrid button[action=addFilePlantAccessRequestBtnAct]' : {
					click : this.addFilePlantAccessRequestBtnAct
				},'plantAccessForm button[action=uploadPlantAccessRequestAct]' : {
					click : this.uploadPlantAccessRequestAct
				},'plantAccessForm button[action=uploadPlantAccessRequestActHeader]' : {
					click : this.uploadPlantAccessRequestActHeader
				},
				'plantAccessDetailGrid' : {
					itemdblclick: this.openReceiptForm
				},
				'plantAccessFileGrid button[action=loadFile]' : {
					click : this.loadFile
				},
				'plantAccessWorkerFileGrid button[action=loadFileWorker]' : {
					click : this.loadFileWorker
				},
				'#openLoadFileWorker' : {
				     "buttonclick" : this.openLoadFileWorker
			    },
			    '#deleteFilePlantAccess': {
				     "buttonclick" : this.deleteFilePlantAccess
			    },
			    '#deleteFileWorkerPlantAccess': {
				     "buttonclick" : this.deleteFileWorkerPlantAccess
			    },
			    '#deleteWorker': {
				     "buttonclick" : this.deleteWorker
			    },
			    'plantAccessForm button[action=uploadFileRequest]' : {
					click : this.uploadFileRequest
				},
				//Acciones del Panel Principal NUEVO
			    'plantAccessMainPanel button[action=savePlantAccessRequest]' : {
					click : this.savePlantAccessRequest
				},
				//Acciones del Panel Principal NUEVO
			    'plantAccessMainPanel button[action=showPlantAccessRequestFiles]' : {
					click : this.showRequestFile
				},
				
			    'plantAccessMainPanel button[action=plantAccessAddWorker]' : {
					click : this.plantAccessAddWorker
				},
			    'plantAccessMainPanel button[action=plantAccessFinishWorker]' : {
					click : this.plantAccessFinishWorker
				},
			    'plantAccessMainPanel button[action=plantAccessShowRequest]' : {
					click : this.plantAccessShowRequest
				},
			    //Llamados de documentos
				'plantAccessForm button[action=requestDOC_1]' : {
					click : this.loadFile
				},
				'plantAccessForm button[action=requestDOC_2]' : {
					click : this.loadFile
				},
			    'plantAccessForm button[action=requestDOC_3]' : {
					click : this.loadFile
				},
				'plantAccessForm button[action=requestDOC_4]' : {
					click : this.loadFile
				},
				'plantAccessForm button[action=requestDOC_5]' : {
					click : this.loadFile
				},
				'plantAccessForm button[action=requestDOC_6]' : {
					click : this.loadFile
				},
			    'plantAccessForm button[action=requestDOC_7]' : {
					click : this.loadFile
				},
				'plantAccessForm button[action=requestDOC_8]' : {
					click : this.loadFile
				},
				'plantAccessForm button[action=requestDOC_9]' : {
					click : this.loadFile
				},
				'plantAccessForm button[action=requestDOC_10]' : {
					click : this.loadFile
				},
			    'plantAccessForm button[action=requestDOC_11]' : {
					click : this.loadFile
				},
				'plantAccessForm button[action=requestDOC_12]' : {
					click : this.loadFile
				},
			    //Llamados de documentos NUEVO
				'plantAccessRequestDocForm button[action=requestDOC_1]' : {
					click : this.loadFileNew
				},
				'plantAccessRequestDocForm button[action=requestDOC_2]' : {
					click : this.loadFileNew
				},
			    'plantAccessRequestDocForm button[action=requestDOC_3]' : {
					click : this.loadFileNew
				},
				'plantAccessRequestDocForm button[action=requestDOC_4]' : {
					click : this.loadFileNew
				},
				'plantAccessRequestDocForm button[action=requestDOC_5]' : {
					click : this.loadFileNew
				},
				'plantAccessRequestDocForm button[action=requestDOC_6]' : {
					click : this.loadFileNew
				},
			    'plantAccessRequestDocForm button[action=requestDOC_7]' : {
					click : this.loadFileNew
				},
				'plantAccessRequestDocForm button[action=requestDOC_8]' : {
					click : this.loadFileNew
				},
				'plantAccessRequestDocForm button[action=requestDOC_9]' : {
					click : this.loadFileNew
				},
				'plantAccessRequestDocForm button[action=requestDOC_10]' : {
					click : this.loadFileNew
				},
			    'plantAccessRequestDocForm button[action=requestDOC_11]' : {
					click : this.loadFileNew
				},
				'plantAccessRequestDocForm button[action=requestDOC_12]' : {
					click : this.loadFileNew
				},
				//Documentos trabajadores
				'plantAccessWorkerFileGrid button[action=workerDOC_1]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_2]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_3]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_4]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_5]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_6]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_7]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_8]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_9]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_10]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_11]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_12]' : {
					click : this.loadFileWorker
				},
				'plantAccessWorkerFileGrid button[action=workerDOC_13]' : {
					click : this.loadFileWorker
				},
				//Documentos trabajadores NUEVO
				'plantAccessWorkerForm button[action=workerDOC_1]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_2]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_3]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_4]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_5]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_6]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_7]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_8]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_9]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_10]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_11]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_12]' : {
					click : this.loadFileWorkerNew
				},
				'plantAccessWorkerForm button[action=workerDOC_13]' : {
					click : this.loadFileWorkerNew
				},
				//Guarda solicitud NUEVO
				'plantAccessRequestForm button[action=updatePlantAccessRequest]' : {
					click : this.updatePlantAccessRequestBtn
				},
				//Guarda archivos de solicitud NUEVO
				'plantAccessRequestDocForm button[action=updatePlantAccessRequestDoc]' : {
					click : this.updatePlantAccessRequestDocBtn
				},
				//Guarda solicitud NUEVO
				'plantAccessRequestDocForm checkbox[action=heavyEquipmentRequestDoc]' : {
					change : this.updatePlantAccessRequestDocsCheck
				},
				//Validaciones Trabajador NUEVO
				'plantAccessWorkerForm checkbox[action=pawDocsActivity1]' : {
					change : this.updatePlantAccessWorkerDocsCheck
				},
				'plantAccessWorkerForm checkbox[action=pawDocsActivity2]' : {
					change : this.updatePlantAccessWorkerDocsCheck
				},
				'plantAccessWorkerForm checkbox[action=pawDocsActivity3]' : {
					change : this.updatePlantAccessWorkerDocsCheck
				},
				'plantAccessWorkerForm checkbox[action=pawDocsActivity4]' : {
					change : this.updatePlantAccessWorkerDocsCheck
				},
				'plantAccessWorkerForm checkbox[action=pawDocsActivity5]' : {
					change : this.updatePlantAccessWorkerDocsCheck
				},
				'plantAccessWorkerForm checkbox[action=pawDocsActivity6]' : {
					change : this.updatePlantAccessWorkerDocsCheck
				},
				'plantAccessWorkerForm checkbox[action=pawDocsActivity7]' : {
					change : this.updatePlantAccessWorkerDocsCheck
				},
				//Editar Trabajador NUEVO
				'#paEditRequestWorker' : {
					"buttonclick" : this.plantAccessEditWorker
				},
				//Eliminar Trabajador NUEVO
				'#paDeleteWorker' : {
					"buttonclick" : this.deleteWorker
				},
				//Eliminar Archivo de Trabajador NUEVO
			    '#paDeleteWorkerFile': {
				     "buttonclick" : this.deleteFileWorkerPlantAccess
			    },
        });
    },
    
    onCloseMainPanel: function(window) {
  	  debugger;
  	 var me = this;
  	var status = Ext.getCmp('paRequestStatus').getValue();

  	if (!['APROBADO', 'PENDIENTE',''].includes(status)) {
  	    Ext.getCmp('paRequestStatus').setValue('GUARDADODSALIDA');
  	
     // 1. Obtenemos el botón de guardar del panel principal
  	Ext.getCmp('paRequestStatus').setValue('GUARDADODSALIDA');
//  	var status= window.down('#paRequestStatus');
//  	status.setValue('GUARDADO');
     var saveButton = window.down('#savePlantAccessRequest');
     saveButton.fireEvent('click', saveButton);
     if (Ext.getCmp('paWorkerForm').isVisible()) {
    	 debugger;
    	 try {
			 var saveButton = window.down('#plantAccessFinishWorker');
         saveButton.fireEvent('click', saveButton);
		} catch (e) {
			  if (e instanceof TypeError && e.message.includes("Cannot read properties of undefined") && e.message.includes("setValue")) {
			        console.warn('El componente paRequestStatus no existe. Error controlado.');
			        // Aquí puedes decidir no hacer nada o registrar el evento
			    } else {
			        throw e; // Re-lanzamos otros errores que no son el que nos interesa
			    }
		}
    	
    	}
  	}
     
    	var grid = Ext.getCmp('paPlantAccessGrid');
    	var store = grid.getStore();
    	store.reload();
		grid.getView().refresh();
		
		
    },
    
    onRequestGridAfterRender: function(grid) {
    	/*
    	var me = this;
    	//Estatus de la solicitud
    	var statusPlantAccess = Ext.getCmp('paRequestStatus').getValue();
    	
        // Obtener el índice (index) de la columna que deseas modificar
        var columnIndexDelete = grid.headerCt.items.findIndex('dataIndex', 'paDeleteWorker');
        var columnIndexEdit = grid.headerCt.items.findIndex('dataIndex', 'paEditRequestWorker');

        // Cambios en columnas al renderizar el grid
        if(['PENDIENTE','RECHAZADO','APROBADO'].includes(statusPlantAccess)){
        	grid.columns[columnIndexDelete].setVisible(false);
        	grid.columns[columnIndexEdit].setText(SuppAppMsg.plantAccess50);
        }
        
        if(['RECHAZADO'].includes(statusPlantAccess) && role == 'ROLE_SUPPLIER'){
        	grid.columns[columnIndexDelete].setVisible(true);
        	grid.columns[columnIndexEdit].setText(SuppAppMsg.taxvaultUploandDocuments);
        }*/
    },
    
    deleteWorker : function(grid, rowIndex, colIndex) {
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var workerId = record.data.id;

    	Ext.MessageBox.show({
            title: SuppAppMsg.plantAccess90,
            msg: SuppAppMsg.plantAccessTempMessage5,
    	    buttons : Ext.MessageBox.YESNO,
    	    icon: Ext.MessageBox.QUESTION,
    	    width: 380,
    	    heigh: 200,
			buttonText : {
				yes : SuppAppMsg.plantAccess91,
				no : "No"
			},
            fn: function showResult(btn){
                if(btn == 'yes'){
                	Ext.Ajax.request({
            		    url: 'plantAccess/deleteWorkerPlantAccessById.action',
            		    method: 'POST',
            		    params: {
            		    	workerId:workerId
            	        },
            		    success: function(fp, o) {            		    
            		    	me.refreshRequestWorkersGrid(false);
            		    },
            		    failure: function() {
            		    	Ext.MessageBox.show({
            	                title: 'Error',
            	                msg: SuppAppMsg.approvalUpdateError,
            	                buttons: Ext.Msg.OK
            	            });
            		    }
            		}); 
                }
            }
        });
    	
    },
    
    deleteWorkerOld : function(grid, rowIndex, colIndex, record) {
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var dto = Ext.create('SupplierApp.model.PlantAccessDetail',record.data);
    	var grid = this.getPlantAccessDetailGrid();
    	var store = grid.getStore();
    	
    	Ext.MessageBox.show({
            title:SuppAppMsg.plantAccess90,
            msg: 'Esta seguro de eliminar el trabajador, se eliminara de forma definitiva<br><br>',
            buttons: Ext.MessageBox.YESNO,
            fn: function showResult(btn){
                if(btn == 'yes'){
                	Ext.Ajax.request({
            		    url: 'plantAccess/deleteWorkerPlantAccess.action',
            		    method: 'POST',
            		    params: {
            		    	uuid:dto.data.requestNumber
            	        },
            		    jsonData: dto.data,
            		    success: function(fp, o) {
            		    
            		    	var res = Ext.decode(fp.responseText);
            		    	//var res1 = Ext.decode(o.response.responseText);
            				var rec =Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
            				store.removeAll();
            		    	store.add(rec.raw);
            		    	grid.getView().refresh(); 
            		    	
            		    	var workers=store.data.items;
//            		    	for (var i = 0; i < workers.length; i++) {
//            		    		var listDocuments = workers[i].data.listDocuments;
//            		    		
//            		    		if(listDocuments.includes('WORKER_CI') //&&
////            				    		listDocuments.includes('WORKER_DSCI') &&
////            				    		listDocuments.includes('WORKER_CCOVID') &&
////            				    		listDocuments.includes('WORKER_CMA') &&
////            				    		listDocuments.includes('WORKER_IDEN')
//            				    		) workers[i].data.allDocuments = true;
//            				    	
//            				    	var activities = workers[i].data.activities;
//            						if(activities.includes('1')){
//            							if(workers[i].data.listDocuments.includes('WORKER_CM1') &&
//            								workers[i].data.listDocuments.includes('WORKER_CD3TA')) workers[i].data.docsActivity1 = true;
//            						}else workers[i].data.docsActivity1 = true;
//            						
//            						if(activities.includes('2')){
//            							if(workers[i].data.listDocuments.includes('WORKER_CD3G')) workers[i].data.docsActivity2 = true;
//            						}else workers[i].data.docsActivity2 = true;
//            						
//            						if(activities.includes('3')){
//            							if(workers[i].data.listDocuments.includes('WORKER_CM1') &&
//            								workers[i].data.listDocuments.includes('WORKER_CD3TEC')) workers[i].data.docsActivity3 = true;
//            						}else workers[i].data.docsActivity3 = true;
//            						
//            						if(activities.includes('4')){
//            							if(workers[i].data.listDocuments.includes('WORKER_CD3TE1')) workers[i].data.docsActivity4 = true;
//            						}else workers[i].data.docsActivity4 = true;
//            						
//            						if(activities.includes('5')){
//            							if(workers[i].data.listDocuments.includes('WORKER_CD3TC')) workers[i].data.docsActivity5 = true;
//            						}else workers[i].data.docsActivity5 = true;
//            						
//            						if(activities.includes('6')){
//            							if(workers[i].data.listDocuments.includes('WORKER_HS')) workers[i].data.docsActivity6 = true;
//            						}else workers[i].data.docsActivity6 = true;
//            						
//            						if(activities.includes('*')){
//            							if(workers[i].data.listDocuments.includes('WORKER_AE')) workers[i].data.docsActivity7 = true;
//            						}else workers[i].data.docsActivity7 = true;
//            		    		
//            				}
            		    	/*
            				Ext.Ajax.request({
            				    url: 'plantAccess/searchWorkersPlantAccessByIdRequest.action',
            				    method: 'POST',
            				    params: {
            			        	//uuid:uuidPlantAccessWorker
            				    	uuid:res.data.id
            			        },
            				    success: function(fp, o) {
            				    	var res = Ext.decode(fp.responseText);
            				    	//var res1 = Ext.decode(o.response.responseText);
            						var rec =Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
            						store.removeAll();
            				    	store.add(rec.raw);
            				    	grid.getView().refresh();
            				    	
            				    },
            				    failure: function() {
            				    	Ext.MessageBox.show({
            			                title: 'Error',
            			                msg: SuppAppMsg.approvalUpdateError,
            			                buttons: Ext.Msg.OK
            			            });
            				    }
            				});
            		    	 */
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
    
    deleteFileWorkerPlantAccess : function(grid, rowIndex, colIndex) {
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var documentId = record.data.id;
		
    	Ext.MessageBox.show({
            title:SuppAppMsg.plantAccess90,
            msg: SuppAppMsg.plantAccessTempMessage6,
    	    buttons : Ext.MessageBox.YESNO,
    	    icon: Ext.MessageBox.QUESTION,
    	    width: 380,
    	    heigh: 200,
			buttonText : {
				yes : SuppAppMsg.plantAccess91,
				no : "No"
			},
            fn: function showResult(btn){
                if(btn == 'yes'){
                	Ext.Ajax.request({
            		    url: 'plantAccess/deleteWorkerFilePlantAccessById.action',
            		    method: 'POST',
            		    params: {
            		    	documentId:documentId
            	        },
            		    success: function(fp, o) {
            		    	//Actualiza campo de texto
            		    	var fieldName = record.data.documentType.replace('WORKER_','text_');
            		    	Ext.getCmp(fieldName).setValue('');
            		    	
            		    	//Seteo manual, no acoplado en esta actualización
            		    	Ext.getCmp('pawAllDocuments').setValue(false);
            		    	
            		    	//Actualiza grid de archivos
            		    	me.refreshWorkerFileGrid();
            		    },
            		    failure: function() {
            		    	Ext.MessageBox.show({
            	                title: 'Error',
            	                msg: SuppAppMsg.approvalUpdateError,
            	                buttons: Ext.Msg.OK
            	            });
            		    }
            		}); 
                }
            }
        });
    },
    
    deleteFileWorkerPlantAccessOld : function(grid, rowIndex, colIndex, record) {
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var dto = Ext.create('SupplierApp.model.PlantAccessFile',record.data);
    	var grid = this.getPlantAccessWorkerFileGrid();
    	var store = grid.getStore();
    	
    	Ext.MessageBox.show({
            title:'Eliminar',
            msg: 'Esta seguro de eliminar el documento, se eliminara de forma definitiva<br><br>',
            buttons: Ext.MessageBox.YESNO,
            fn: function showResult(btn){
                if(btn == 'yes'){
                	Ext.Ajax.request({
            		    url: 'plantAccess/deleteFilesPlantAccess.action',
            		    method: 'POST',
            		    params: {
            		    	uuid:dto.data.uuid
            	        },
            		    jsonData: dto.data,
            		    success: function(fp, o) {
            		    
            		    	var res = Ext.decode(fp.responseText);
            		    	//var res1 = Ext.decode(o.response.responseText);
            				var rec =Ext.create('SupplierApp.model.PlantAccessFile',res.data);
            				store.removeAll();
            		    	store.add(rec.raw);
            		    	grid.getView().refresh();
            		    	
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
    
    deleteFilePlantAccess : function(grid, rowIndex, colIndex, record) {
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var dto = Ext.create('SupplierApp.model.PlantAccessFile',record.data);
    	var grid = this.getPlantAccessFileGrid();
    	var store = grid.getStore();
    	
    	Ext.MessageBox.show({
            title: SuppAppMsg.plantAccess90,
            msg: SuppAppMsg.plantAccessTempMessage6,
            buttons: Ext.MessageBox.YESNO,
            fn: function showResult(btn){
                if(btn == 'yes'){
                	Ext.Ajax.request({
            		    url: 'plantAccess/deleteFilesPlantAccess.action',
            		    method: 'POST',
            		    params: {
            		    	uuid:dto.data.uuid
            	        },
            		    jsonData: dto.data,
            		    success: function(fp, o) {
            		    	
            		    	var res = Ext.decode(fp.responseText);
            		    	//var res1 = Ext.decode(o.response.responseText);
            				var rec =Ext.create('SupplierApp.model.PlantAccessFile',res.data);
            				store.removeAll();
            		    	store.add(rec.raw);
            		    	grid.getView().refresh();
            		    	
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
    openLoadFileWorker : function(grid, rowIndex, colIndex) {
    	//No se utiliza
    	//var me = this;
    	//var record = grid.store.getAt(rowIndex);
    	var recordWorker = grid.store.getAt(rowIndex).data;
    	uuidPlantAccessWorker = recordWorker.datefolioIDcard;
    	
    	var win = new Ext.Window(
				{
					title : SuppAppMsg.plantAccess95,
					layout : 'fit',
					autoScroll : true,
					width : 850,
					height : 440,
					modal : true,
					closeAction : 'destroy',
					resizable : false,
					minimizable : false,
					maximizable : false,
					plain : true,
					bodyStyle : "padding:10 10 0 10px;background:#fff;",
					items : [ {
	        			xtype : 'plantAccessWorkerFileGrid',
	        			border : true,
	        			height : 415
	        		}  ]
				});

		win.show();
		
		var me = this;
    	var grid = this.getPlantAccessWorkerFileGrid();
    	var store = grid.getStore();
    	
    	Ext.Ajax.request({
		    url: 'plantAccess/searchFilesPlantAccess.action',
		    method: 'POST',
		    params: {
	        	//uuid:uuidPlantAccessWorker
		    	uuid:'test 1'
	        },
		    success: function(fp, o) {
		    	var res = Ext.decode(fp.responseText);
		    	//var res1 = Ext.decode(o.response.responseText);
				var rec =Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
		    	store.add(rec.raw);
		    	grid.getView().refresh();
		    	
		    },
		    failure: function() {
		    	Ext.MessageBox.show({
	                title: 'Error',
	                msg: SuppAppMsg.approvalUpdateError,
	                buttons: Ext.Msg.OK
	            });
		    }
		});
    	/*
    	var filePanel = Ext.create(
				'Ext.form.Panel',
				{
					width: 510,
					items : [{
								fieldLabel : SuppAppMsg.purchaseTitle30,
								xtype : 'textfield',
								name : 'notesPlantAcces',
								id : 'notesPlantAcces',
								width:500,
								colspan:3,
								margin:'10 0 0 10'
							}],

					buttons : [{
						text : SuppAppMsg.approvalApprove,
						margin:'0 5 0 0',
						handler : function() {
							var form = this.up('form').getForm();
							if (form.isValid()) {
								
								var note = Ext.getCmp('notesPlantAcces').getValue();
								
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
												    url: 'plantAccess/updateAprov.action',
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
    	me.winLoadInv.show(); */
    	
    	
    },
    
    loadFileWorkerNew:function(button){
    	debugger;
    	var me = this;
    	var pawForm = this.getPlantAccessWorkerForm().getForm();
    	var values = pawForm.getFieldValues();
    	var requestRfc = Ext.getCmp('paRequestRfc').getValue();
    	var workerId = Ext.getCmp('pawTempId').getValue();
    	var isWorkerFileBtn = false;
    	var isShowUpWindow = true;

    	var paField = "";
    	switch (button.action) {
    	case 'workerDOC_1':
    		paField = 'text_CMA';
    		isWorkerFileBtn = true;
    		break;
  	  	case 'workerDOC_2':
  	  		paField = 'text_CI';
  	  		isWorkerFileBtn = true;
  	  		break;
  	  	case 'workerDOC_3':
  	  		paField = 'text_DSCI';
  	  		break;
  	  	case 'workerDOC_4':
  	  		paField = 'text_IDEN';
  	  		break;
  	  	case 'workerDOC_5':
  	  		paField = 'text_CCOVID';
  	  		break;
  	  	case 'workerDOC_6':
  	  		paField = 'text_CM1';
  	  		isWorkerFileBtn = true;
  	  		break;
  	  	case 'workerDOC_7':
  	  		paField = 'text_CD3TA';
  	  		isWorkerFileBtn = true;
  	  		break;
  	  	case 'workerDOC_8':
  		  	paField = 'text_CD3G';
  		  	isWorkerFileBtn = true;
  		    break;
  	  	case 'workerDOC_9':
  		  	paField = 'text_CD3TEC';
  		  	isWorkerFileBtn = true;
  		    break;
  	  	case 'workerDOC_10':
  		  	paField = 'text_CD3TE1';
  		  	isWorkerFileBtn = true;
  		  	break;
  	  	case 'workerDOC_11':
  		  	paField = 'text_CD3TC';
  		  	isWorkerFileBtn = true;
  		    break;
  	  	case 'workerDOC_12':
  		  	paField = 'text_HS';
  		  	isWorkerFileBtn = true;
  		    break;
  	  	case 'workerDOC_13':
  	  		paField = 'text_AE';
  	  		isWorkerFileBtn = true;
  	  		break;
  	  	default:
            break;
    	};

    	if(isWorkerFileBtn){
    		var formWorker = this.getPlantAccessWorkerForm().getForm();
    		if(formWorker.isValid()){
    			if(!Ext.getCmp('pawDocsActivity1').checked
    			&& !Ext.getCmp('pawDocsActivity2').checked
    			&& !Ext.getCmp('pawDocsActivity3').checked
    			&& !Ext.getCmp('pawDocsActivity4').checked
    			&& !Ext.getCmp('pawDocsActivity5').checked
    			&& !Ext.getCmp('pawDocsActivity6').checked
    			&& !Ext.getCmp('pawDocsActivity7').checked){    				
    	    		Ext.MessageBox.show({
    	    			width: 400,
    	        	    title: SuppAppMsg.plantAccess89,	        	    
    	        	    msg: SuppAppMsg.plantAccessTempMessage7	        	    
    	        	});
        			isShowUpWindow = false;
    			}    			
    		} else {
	    		Ext.MessageBox.show({
	    			width: 400,
	        	    title: SuppAppMsg.plantAccess89,	        	    
	        	    msg: SuppAppMsg.plantAccessTempMessage8	        	    
	        	});
    			isShowUpWindow = false;
    		}
    	}
    	
    	if(isShowUpWindow){
        	var filePanel = Ext.create(
    				'Ext.form.Panel',
    				{
    					width : 900,	
    					items : [
    						{
    						xtype : 'textfield',
    						name : 'documentType',
    						hidden : true,
    						value : paField.replace('text_','')
    						},{
    						xtype : 'filefield',
    						name : 'file',
    						fieldLabel :SuppAppMsg.purchaseFile,
    						labelWidth : 70,
    						msgTarget : 'side',
    						allowBlank : false,
    						width:300,
    						buttonText : SuppAppMsg.suppliersSearch,
    						margin:'10 0 10 10',
    				        fileType: ['pdf'], // Filtrar por extensión PDF
    				        listeners: {
    				            change: function (field, value) {
    				                var ext = value.split('.').pop().toLowerCase();
    				                if (ext !== 'pdf') {
    				                    Ext.Msg.alert(SuppAppMsg.plantAccess89, SuppAppMsg.plantAccess92);
    				                    field.reset();
    				                }
    				            }
    				        }
    					},{
    		    			xtype : 'textfield',
    		    			name : 'idRequest',
    		    			fieldLabel : 'idRequest',
    		    			labelWidth : 70,
    		    			allowBlank : false,
    		    			width:300,
    		    			hidden:true,
    		    			margin:'10 0 10 10',
    		    			value: requestRfc
    					},{
    		    			xtype : 'textfield',
    		    			name : 'idworker',
    		    			fieldLabel : 'idworker',
    		    			labelWidth : 70,
    		    			allowBlank : false,
    		    			width:300,
    		    			hidden:true,
    		    			margin:'10 0 10 10',
    		    			value: workerId
    		    		}],
    					buttons : [ {
    						text : SuppAppMsg.supplierLoad,
    						margin:'10 0 0 0',
    						handler : function() {
    							var form = this.up('form').getForm();
    							if (form.isValid()) {
    								form.submit({
    									url : 'plantAccess/uploadFileWorkerNew.action',
    									waitMsg : SuppAppMsg.supplierLoadFile,
    									success : function(fp, o) {
    										var res = Ext.decode(o.response.responseText);
    										
    										if(res.data.originName != null){
    											Ext.MessageBox.alert({ maxWidth: 400, minWidth: 400, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.supplierLoadDocSucc });
    											pawForm.findField(paField).setValue(res.data.originName);
    									    	me.winLoadInv.close();
    										}else {
    											Ext.MessageBox.alert({ maxWidth: 400, minWidth: 400, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.plantAccessTempMessage9 });
    										}    								    	
    										
    										//Guarda o Actualiza infomación del trabajador (Verifica documentos completos)
    										me.updatePlantAccessWorker(true, false, false);
    										
    								    	//Actualiza grid de archivos de trabajador
    								    	//me.refreshWorkerFileGrid(); //El método updatePlantAccessWorker también contiene funcionalidad
    									},
    							        failure: function(fp, o) {
    							        	var res = Ext.decode(o.response.responseText);
    							        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
    							        }
    								});
    							}
    						 }
    					}]
    		});

    		this.winLoadInv = new Ext.Window({
    			layout : 'fit',
    			title : SuppAppMsg.plantAccess93,
    			//width : 600,
    			//height : 160,
    			width: Ext.Element.getViewportWidth() * 0.45,   
                maxWidth: 450,                               
                height: Ext.Element.getViewportHeight() * 0.35, 
                maxHeight: 160,
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
    
    loadFileWorker:function(button){
    	var me = this; 
    	var grid = this.getPlantAccessWorkerFileGrid();
    	var store = grid.getStore();
    	debugger
    	Ext.getCmp('uploadFileRequest').show();
    	var paField = "";
    	switch (button.action) {
    	  case 'workerDOC_1':
    		  	paField = 'text_CMA';
    		    break;
    	  case 'workerDOC_2':
    		  	paField = 'text_CI';
    		    break;
      	  case 'workerDOC_3':
      		  	paField = 'text_DSCI';
      		    break;
      	  case 'workerDOC_4':
    		  	paField = 'text_IDEN';
    		    break;
      	  case 'workerDOC_5':
    		  	paField = 'text_CCOVID';
    		    break;
    	  case 'workerDOC_6':
    		  	paField = 'text_CM1';
    		    break;
    	  case 'workerDOC_7':
    		  	paField = 'text_CD3TA';
    		    break;
    	  case 'workerDOC_8':
    		  	paField = 'text_CD3G';
    		    break;
    	  case 'workerDOC_9':
    		  	paField = 'text_CD3TEC';
    		    break;
    	  case 'workerDOC_10':
    		  	paField = 'text_CD3TE1';
    		    break;
    	  case 'workerDOC_11':
    		  	paField = 'text_CD3TC';
    		    break;
    	  case 'workerDOC_12':
    		  	paField = 'text_HS';
    		    break;
    	  case 'workerDOC_13':
	  		  	paField = 'text_AE';
	  		    break;
      	  default:
              break;
      	};
    	
    	var filePanel = Ext.create(
				'Ext.form.Panel',
				{
					width : 900,	
					items : [/*{
						xtype : 'combo',
						fieldLabel : 'Tipo Documento',
						id : 'addRequestDocumentType',
						itemId : 'addRequestDocumentType',
						name : 'addRequestDocumentType',
						store : getUDCStore('FILEACCESPLANT', ('REQUEST'+(Ext.getCmp('addworkatheights').getValue()?",WORKATHEIGHTS":"")
								+(Ext.getCmp('addHeavyequipment').getValue()?",HEAVYEQUIPMENT":"")
								+(Ext.getCmp('addconfinedspaces').getValue()?",CONFINEDSPACES":"")
								+(Ext.getCmp('addcontelectricworks').getValue()?",CONTELECTRICWORKS":"")
								+(Ext.getCmp('addworkhots').getValue()?",WORKHOTS":"")
								+(Ext.getCmp('addchemicalsubstances').getValue()?",CHEMICALSUBSTANCES":"")), '', 'WORKER'),
						valueField : 'strValue2',
						displayField: 'strValue1',
						emptyText : 'Seleccione',
						typeAhead: true,
			            minChars: 1,
			            forceSelection: true,
			            triggerAction: 'all',
			            labelWidth:100,
			            width : 400,
						allowBlank : true,
						margin:'10 0 10 10'
				}*/
					{
					xtype : 'textfield',
					name : 'documentType',
					id : 'addRequestDocumentType',
					itemId : 'addRequestDocumentType',
					name : 'addRequestDocumentType',
					hidden : true,
					value : paField.replace('text_','')
					},{
					xtype : 'filefield',
					name : 'file',
					fieldLabel : 'Archivo:',
					labelWidth : 70,
					msgTarget : 'side',
					allowBlank : false,
					width:300,
					buttonText : 'Archivo',
					margin:'10 0 10 10'
//					listeners: {
//			            'change': function(f, value){
//			                  alert(f.size); // not filesize
//			            }}
				},{
		    			xtype : 'textfield',
		    			name : 'uuidRequestWorker',
		    			fieldLabel : 'uuidRequestWorker:',
		    			labelWidth : 70,
		    			//msgTarget : 'side',
		    			hidden:true,
		    			allowBlank : false,
		    			width:300,
		    			id:'uuidRequestWorker',
		    			itemId:'uuidRequestWorker',
		    			margin:'10 0 10 10'
		    		}],

					/*buttons : [ {
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
					} ]*/
						buttons : [ {
							text : SuppAppMsg.supplierLoad,
							margin:'10 0 0 0',
							handler : function() {
								var form = this.up('form').getForm();
								if (form.isValid()) {
									debugger
									form.submit({
												//url : 'plantAccess/uploadFileRequest.action',
												url : 'plantAccess/uploadFileWorker.action?idworker='+(Ext.getCmp('addemployeeId').getValue()),
												waitMsg : SuppAppMsg.supplierLoadFile,
												success : function(fp, o) {
													
													var res = Ext.decode(o.response.responseText); 
													
													if(res.data.originName != null){
														var rec =Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
														Ext.getCmp(paField).setValue(res.data.originName);
														var file = store.findRecord('documentType',  'WORKER_' + paField.replace('text_',''));
														if(file != null)store.removeAt(store.find('documentType', 'WORKER_' + paField.replace('text_','')))
												    	store.add(rec.raw);
												    	grid.getView().refresh();
												    	me.winLoadInv.close();
												    	recordWorker.id=Ext.getCmp('addemployeeId').getValue();
												    	recordWorker.listDocuments = recordWorker.listDocuments + ',WORKER_' + paField.replace('text_','')
//												    	if(recordWorker.listDocuments.includes('WORKER_CI') //&&
////												    		recordWorker.listDocuments.includes('WORKER_DSCI') &&
////												    		recordWorker.listDocuments.includes('WORKER_CCOVID') &&
//												    		//recordWorker.listDocuments.includes('WORKER_CMA') &&
////												    		recordWorker.listDocuments.includes('WORKER_IDEN')
//												    		) recordWorker.allDocuments = true;
												    	
												    	
												    	recordWorker.activities= ""+((Ext.getCmp('addworkatheights').getValue()?"1,":"")
										    					+(Ext.getCmp('addHeavyequipmentWorker').getValue()?"2,":"")
										    					+(Ext.getCmp('addconfinedspaces').getValue()?"3,":"")
										    					+(Ext.getCmp('addcontelectricworks').getValue()?"4,":"")
										    					+(Ext.getCmp('addworkhots').getValue()?"5,":"")
										    					+(Ext.getCmp('addchemicalsubstances').getValue()?"6,":"")
										    					+(Ext.getCmp('activityFree').getValue()?"*":""));
												    	
												    	var activities = recordWorker.activities;
//														if(activities.includes('1')){
//															if(recordWorker.listDocuments.includes('WORKER_CM1') &&
//													    		recordWorker.listDocuments.includes('WORKER_CD3TA')) recordWorker.docsActivity1 = true;
//														}else recordWorker.docsActivity1 = true;
//														
//														if(activities.includes('2')){
//															if(recordWorker.listDocuments.includes('WORKER_CD3G')) recordWorker.docsActivity2 = true;
//														}else recordWorker.docsActivity2 = true;
//														
//														if(activities.includes('3')){
//															if(recordWorker.listDocuments.includes('WORKER_CM1') &&
//														    	recordWorker.listDocuments.includes('WORKER_CD3TEC')) recordWorker.docsActivity3 = true;
//														}else recordWorker.docsActivity3 = true;
//														
//														if(activities.includes('4')){
//															if(recordWorker.listDocuments.includes('WORKER_CD3TE1')) recordWorker.docsActivity4 = true;
//														}else recordWorker.docsActivity4 = true;
//														
//														if(activities.includes('5')){
//															if(recordWorker.listDocuments.includes('WORKER_CD3TC')) recordWorker.docsActivity5 = true;
//														}else recordWorker.docsActivity5 = true;
//														
//														if(activities.includes('6')){
//															if(recordWorker.listDocuments.includes('WORKER_HS')) recordWorker.docsActivity6 = true;
//														}else recordWorker.docsActivity6 = true;
//														
//														if(activities.includes('*')){
//															if(recordWorker.listDocuments.includes('WORKER_AE')) recordWorker.docsActivity7 = true;
//														}else recordWorker.docsActivity7 = true;
														
														//Actualizar lista de actividades
														Ext.Ajax.request({
														    url: 'plantAccess/updateListDocumentsWorker.action',
														    method: 'POST',
														    params: {
													        	//uuid:uuidPlantAccessWorker
														    	id:recordWorker.id,
														    	documents:recordWorker.listDocuments
													        },
														    success: function(fp, o) {
														    	var res = Ext.decode(fp.responseText);
														    	
														    },
														    failure: function() {
														    	Ext.MessageBox.show({
													                title: 'Error',
													                msg: SuppAppMsg.approvalUpdateError,
													                buttons: Ext.Msg.OK
													            });
														    }
														}); 
													}else Ext.MessageBox.alert({ maxWidth: 400, minWidth: 400, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.plantAccess94 });
													
											    	
												},       // If you don't pass success:true, it will always go here
										        failure: function(fp, o) {
										        	var res = Ext.decode(o.response.responseText);
										        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
										        }
											});
								}
							 }
						}]
				});

		this.winLoadInv = new Ext.Window({
			layout : 'fit',
			title : SuppAppMsg.plantAccess93,
			//width : 600,
			//height : 160,
			width: Ext.Element.getViewportWidth() * 0.45,   
            maxWidth: 450,                               
            height: Ext.Element.getViewportHeight() * 0.35, 
            maxHeight: 160,
			modal : true,
			closeAction : 'destroy',
			resizable : false,
			minimizable : false,
			maximizable : false,
			plain : true,
			items : [ filePanel ]
		
		});
		this.winLoadInv.show();
		
		var uuid = uuidPlantAccessWorker + '_' + uuidPlantAccess;
    	Ext.getCmp('uuidRequestWorker').setValue(uuid);
    },
    
    loadFileNew:function(button){
    	debugger;
    	var me = this;
    	var paForm = this.getPlantAccessRequestDocForm().getForm();
    	var values = paForm.getFieldValues();
    	var requestRfc = Ext.getCmp('paRequestRfc').getValue();

    	var paField = "";
    	switch (button.action) {
    	  case 'requestDOC_1':
    		  	paField = 'text_SI';
    		    break;
    	  case 'requestDOC_2':
    		  	paField = 'text_PSUA';
    		    break;
      	  case 'requestDOC_3':
      		  	paField = 'text_SUA';
      		    break;
      	  case 'requestDOC_4':
    		  	paField = 'text_FPCOPAA';
    		    break;
      	  case 'requestDOC_5':
    		  	paField = 'text_OCIMSS';
    		    break;
    	  case 'requestDOC_6':
    		  	paField = 'text_GC1';
    		    break;
    	  case 'requestDOC_7':
    		  	paField = 'text_GCCOVID';
    		    break;
    	  case 'requestDOC_8':
    		  	paField = 'text_MCE';
    		    break;
    	  case 'requestDOC_9':
    		  	paField = 'text_MPDF';
    		    break;
    	  case 'requestDOC_10':
    		  	paField = 'text_SRC';
    		    break;
    	  case 'requestDOC_11':
    		  	paField = 'text_CERTIFICADO';
    		    break;
    	  case 'requestDOC_12':
    		  	paField = 'text_RM';
    		    break;
      	  default:
              break;
      	};
      	
    	var filePanel = Ext.create(
				'Ext.form.Panel',
				{
					width : 900,	
					items : [
						{
						xtype : 'textfield',
						name : 'documentType',
						hidden : true,
						value : paField.replace('text_','')
						},{
						xtype : 'filefield',
						name : 'file',
						fieldLabel :SuppAppMsg.purchaseFile,
						labelWidth : 70,
						msgTarget : 'side',
						allowBlank : false,
						width:300,
						buttonText : SuppAppMsg.suppliersSearch,
						margin:'10 0 10 10',
				        fileType: ['pdf'], // Filtrar por extensión PDF
				        listeners: {
				            change: function (field, value) {
				                var ext = value.split('.').pop().toLowerCase();
				                if (ext !== 'pdf') {
				                    Ext.Msg.alert(SuppAppMsg.plantAccess89, SuppAppMsg.plantAccess92);
				                    field.reset();
				                }
				            }
				        }
					},{
		    			xtype : 'textfield',
		    			name : 'idRequest',
		    			fieldLabel : 'idRequest',
		    			labelWidth : 70,
		    			allowBlank : false,
		    			width:300,
		    			hidden:true,
		    			margin:'10 0 10 10',
		    			value: requestRfc
		    		}],
					buttons : [ {
						text : SuppAppMsg.supplierLoad,
						margin:'10 0 0 0',
						handler : function() {
							var form = this.up('form').getForm();
							if (form.isValid()) {
								form.submit({
									url : 'plantAccess/uploadFileRequestNew.action',
									waitMsg : SuppAppMsg.supplierLoadFile,
									success : function(fp, o) {
										var res = Ext.decode(o.response.responseText);
										
										if(res.data.originName != null){
											Ext.MessageBox.alert({ maxWidth: 400, minWidth: 400, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.supplierLoadDocSucc });
											paForm.findField(paField).setValue(res.data.originName);
									    	me.winLoadInv.close(); 
										}else {
											Ext.MessageBox.alert({ maxWidth: 400, minWidth: 400, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.plantAccessTempMessage9 });
										}
										
										//Valida documentos de la solicitud cargados
								    	var formDoc = me.getPlantAccessRequestDocForm().getForm();
								    	if (formDoc.isValid()) {
								    		//Habilita control Agregar Trabajador
								        	me.showAddNewWorkerBtnByStatus();								    		
								    	}
								    	
									},
							        failure: function(fp, o) {
							        	var res = Ext.decode(o.response.responseText);
							        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
							        }
								});
							}
						 }
					}]
		});

		this.winLoadInv = new Ext.Window({
			layout : 'fit',
			title : SuppAppMsg.plantAccess93,
			//width : 600,
			//height : 160,
			width: Ext.Element.getViewportWidth() * 0.45,   
            maxWidth: 450,                               
            height: Ext.Element.getViewportHeight() * 0.35, 
            maxHeight: 160,
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
    
    loadFile:function(button){ 
	var me = this;
	var paForm = this.getPlantAccessForm().getForm();
	var values = paForm.getFieldValues();
	debugger
	Ext.getCmp('uploadFileRequest').show();
	//var grid = this.getPlantAccessFileGrid();
	//var store = grid.getStore();
	
	var paField = "";
	switch (button.action) {
	  case 'requestDOC_1':
		  	paField = 'text_SI';
		    break;
	  case 'requestDOC_2':
		  	paField = 'text_PSUA';
		    break;
  	  case 'requestDOC_3':
  		  	paField = 'text_SUA';
  		    break;
  	  case 'requestDOC_4':
		  	paField = 'text_FPCOPAA';
		    break;
  	  case 'requestDOC_5':
		  	paField = 'text_OCIMSS';
		    break;
	  case 'requestDOC_6':
		  	paField = 'text_GC1';
		    break;
	  case 'requestDOC_7':
		  	paField = 'text_GCCOVID';
		    break;
	  case 'requestDOC_8':
		  	paField = 'text_MCE';
		    break;
	  case 'requestDOC_9':
		  	paField = 'text_MPDF';
		    break;
	  case 'requestDOC_10':
		  	paField = 'text_SRC';
		    break;
	  case 'requestDOC_11':
		  	paField = 'text_CERTIFICADO';
		    break;
	  case 'requestDOC_12':
		  	paField = 'text_RM';
		    break;
  	  default:
          break;
  	};
    	 
    	//var form = this.getPlantAccessForm().getForm();
       	//var uuid = form.findField('rfcPlantAccesss').getValue();
    	//var uuid = Ext.getCmp('rfcPlantAccesss').getValue();
    	var filePanel = Ext.create(
				'Ext.form.Panel',
				{
					width : 900,	
					items : [/*{
						xtype : 'combo',
						fieldLabel : 'Tipo Documento',
						id : 'addRequestDocumentType',
						itemId : 'addRequestDocumentType',
						name : 'addRequestDocumentType',
						store : getUDCStore('FILEACCESPLANT', ('REQUEST'+(Ext.getCmp('addworkatheights').getValue()?",WORKATHEIGHTS":"")
								+(Ext.getCmp('addHeavyequipment').getValue()?",HEAVYEQUIPMENT":"")
								+(Ext.getCmp('addconfinedspaces').getValue()?",CONFINEDSPACES":"")
								+(Ext.getCmp('addcontelectricworks').getValue()?",CONTELECTRICWORKS":"")
								+(Ext.getCmp('addworkhots').getValue()?",WORKHOTS":"")
								+(Ext.getCmp('addchemicalsubstances').getValue()?",CHEMICALSUBSTANCES":"")), '', 'CONTRATIST'),
						valueField : 'strValue2',
						displayField: 'strValue1',
						emptyText : 'Seleccione',
						typeAhead: true,
			            minChars: 1,
			            forceSelection: true,
			            triggerAction: 'all',
			            labelWidth:100,
			            width : 400,
						allowBlank : true,
						margin:'10 0 10 10'
					},*/
						{
						xtype : 'textfield',
						name : 'documentType',
						id : 'addRequestDocumentType',
						itemId : 'addRequestDocumentType',
						name : 'addRequestDocumentType',
						hidden : true,
						value : paField.replace('text_','')
						},{
						xtype : 'filefield',
						name : 'file',
						fieldLabel :SuppAppMsg.purchaseFile,
						labelWidth : 70,
						msgTarget : 'side',
						allowBlank : false,
						width:300,
						buttonText : SuppAppMsg.suppliersSearch,
						margin:'10 0 10 10'
				//		listeners: {
				//            'change': function(f, value){
				//                  alert(f.size); // not filesize
				//            }}
					},{
		    			xtype : 'textfield',
		    			name : 'uuidRequest',
		    			fieldLabel : 'uuidRequest:',
		    			labelWidth : 70,
		    			//msgTarget : 'side',
		    			allowBlank : false,
		    			width:300,
		    			hidden:true,
		    			id:'uuidRequest',
		    			itemId:'uuidRequest',
		    			//buttonText : 'Archivo',
		    			margin:'10 0 10 10'
		    	//		listeners: {
		    	//            'change': function(f, value){
		    	//                  alert(f.size); // not filesize
		    	//            }}
		    		}],

					/*buttons : [ {
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
					} ]*/
						buttons : [ {
							text : SuppAppMsg.supplierLoad,
							margin:'10 0 0 0',
							handler : function() {
								var form = this.up('form').getForm();
								if (form.isValid()) {
									form.submit({
												url : 'plantAccess/uploadFileRequest.action',
												waitMsg : SuppAppMsg.supplierLoadFile,
												success : function(fp, o) {
													
													/*Ext.Ajax.request({
													    url: 'searchFilesPlantAccess.action',
													    method: 'GET',
													    params: {
												        	uuid:uuid
												        },
													    success: function(fp, o) {
													    	
													    	var res = Ext.decode(fp.responseText);
													    	
													    },
													    failure: function() {
													    	box.hide();
													    	Ext.MessageBox.show({
												                title: 'Error',
												                msg: SuppAppMsg.approvalUpdateError,
												                buttons: Ext.Msg.OK
												            });
													    }
													}); */
													
													/*var res = Ext.decode(o.response.responseText);
													var rec =Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
													store.add(rec.raw);
											    	grid.getView().refresh();*/
													var res = Ext.decode(o.response.responseText);
													
													if(res.data.originName != null){
														Ext.MessageBox.alert({ maxWidth: 400, minWidth: 400, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.supplierLoadDocSucc });
														paForm.findField(paField).setValue(res.data.originName);
												    	me.winLoadInv.close(); 
													}else Ext.MessageBox.alert({ maxWidth: 400, minWidth: 400, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.plantAccess94 });
													
											    	
												},       // If you don't pass success:true, it will always go here
										        failure: function(fp, o) {
										        	var res = Ext.decode(o.response.responseText);
										        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
										        }
											});
								}
							 }
						}]
				});

		this.winLoadInv = new Ext.Window({
			layout : 'fit',
			title : SuppAppMsg.plantAccess93,
			//width : 600,
			//height : 160,
			width: Ext.Element.getViewportWidth() * 0.45,   
            maxWidth: 450,                               
            height: Ext.Element.getViewportHeight() * 0.35, 
            maxHeight: 160,
			modal : true,
			closeAction : 'destroy',
			resizable : false,
			minimizable : false,
			maximizable : false,
			plain : true,
			items : [ filePanel ]
		
		});
		this.winLoadInv.show();
		
		var uuid = uuidPlantAccess;
    	Ext.getCmp('uuidRequest').setValue(uuid);
    },
	
    showRequestFile: function(button) {
    	debugger
       	var win = new Ext.Window(
				{
					title : SuppAppMsg.taxvaultUploandDocuments,
					layout : 'fit',
					autoScroll : true,
					//width : 850,
					//height : 440,
					width: Ext.Element.getViewportWidth() * 0.65,   // 👈 40% de pantalla
	                maxWidth: 850,                                // 👈 ancho mínimo
	                height: Ext.Element.getViewportHeight() * 0.50, // 👈 40% de alto
	                maxHeight: 440,
					modal : true,
					closeAction : 'destroy',
					resizable : false,
					minimizable : false,
					maximizable : false,
					scrollable: true,
					plain : true,
					bodyStyle : "padding:10 10 0 10px;background:#fff;",
					items : [ {
	        			xtype : 'plantAccessFileGrid',
	        			border : true,
	        			//height : 415
	        		}  ]
				});

		win.show();
		
		var grid = this.getPlantAccessFileGrid();
    	var store = grid.getStore();
    	var paRequestId = Ext.getCmp('paRequestRfc').getValue();
	
		Ext.Ajax.request({
		    url: 'plantAccess/searchFilesPlantAccessNew.action',
		    method: 'POST',
		    params: {
		    	requestId:paRequestId
	        },
		    success: function(fp, o) {
		    	debugger
		    	var res = Ext.decode(fp.responseText);
				var rec =Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
				store.removeAll();
		    	store.add(res.data);
		    	grid.getView().refresh();
		    	
		    },
		    failure: function() {
		    	Ext.MessageBox.show({
	                title: 'Error',
	                msg: SuppAppMsg.approvalUpdateError,
	                buttons: Ext.Msg.OK
	            });
		    }
		});
		
    },
    
    uploadFileRequest: function(button) {
    	//Pendiente de uso
    	//var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
       	//var form = this.getPlantAccessForm().getForm();
       	/*
       	Ext.Ajax.request({
		    url: 'plantAccess/searchFilesPlantAccess.action',
		    method: 'POST',
		    params: {
	        	uuid:'9b2b8e72-5427-428b-bc48-34abd7eb8a70'
	        },
		    success: function(fp, o) {
		    	
		    	var res = Ext.decode(fp.responseText);
		    	
		    },
		    failure: function() {
		    	Ext.MessageBox.show({
	                title: 'Error',
	                msg: SuppAppMsg.approvalUpdateError,
	                buttons: Ext.Msg.OK
	            });
		    }
		}); */
      
       	var win = new Ext.Window(
				{
					title : SuppAppMsg.taxvaultUploandDocuments,
					layout : 'fit',
					autoScroll : true,
					width : 850,
					height : 440,
					modal : true,
					closeAction : 'destroy',
					resizable : false,
					minimizable : false,
					maximizable : false,
					plain : true,
					bodyStyle : "padding:10 10 0 10px;background:#fff;",
					items : [ {
	        			xtype : 'plantAccessFileGrid',
	        			border : true,
	        			height : 415
	        		}  ]
				});

		win.show();
		
		var grid = this.getPlantAccessFileGrid();
    	var store = grid.getStore();
    	var uuid = uuidPlantAccess;
	
		Ext.Ajax.request({
		    url: 'plantAccess/searchFilesPlantAccess.action',
		    method: 'POST',
		    params: {
		    	uuid:uuid
	        },
		    success: function(fp, o) {
		    	
		    	var res = Ext.decode(fp.responseText);
				var rec =Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
				store.removeAll();
		    	store.add(rec.raw);
		    	grid.getView().refresh();
		    	
		    	if(checkPlantAccess &&
		    		role != 'ROLE_SUPPLIER'&&
		    		statusPlantAccess == 'PENDIENTE'){
		    		grid.columns[3].setVisible(true);
		    		//Ext.getCmp('loadFile').hide();
		    	}
		    },
		    failure: function() {
		    	Ext.MessageBox.show({
	                title: 'Error',
	                msg: SuppAppMsg.approvalUpdateError,
	                buttons: Ext.Msg.OK
	            });
		    }
		});
		
    },
    
    gridSelectionChange: function(model, record) { 
    	debugger;
    	var me = this;
		me.viewAccessPlant = new Ext.Window({
			layout : 'fit',
			title : SuppAppMsg.plantAccess47 ,
			//width : 1050,
			//height : 600,
			width: Ext.Element.getViewportWidth() * 0.90,   // 👈 40% de pantalla
            maxWidth: 1050,                                // 👈 ancho mínimo
            height: Ext.Element.getViewportHeight() * 0.90, // 👈 40% de alto
            maxHeight: 600,
			modal : true,
			closeAction : 'destroy',
			resizable : true,
			minimizable : false,
			maximizable : false,
			plain : true,
			items : [ {
				xtype : 'plantAccessMainPanel',
    			border : true,
			} ],
		    listeners: {
		        beforeclose: this.onCloseMainPanel
		    }
		
		});
		me.viewAccessPlant.show();
		
		//Carga información en el formulario PlantAccessRequestForm
    	var form = this.getPlantAccessRequestForm().getForm();
		form.loadRecord(record);
		
		 var rawDate = record.data.fechafirmGui; // Suponiendo que el campo en record.raw se llama 'fechafirmGui'
		    if (rawDate) {
		        var dateField = form.findField('fechafirmGui');
		        var date = new Date(rawDate); // Convertir timestamp a objeto Date
		        dateField.setValue(date); // Establecer el valor en el campo de fecha
		    }
		var values = form.getFieldValues();
		
		//// cargar las ordenes en el grid a partir de la cadena de guardado
		 var grid = this.getPlantAccessRequestForm().down('gridpanel'); // Obtener la referencia al grid
		    var store = grid.getStore(); // Obtener el store del grid

		    // Limpiar el store actual
		    store.removeAll();

		    // Separar la cadena en registros individuales
		    var recordsArray =  record.data.ordenNumber.split('|');

		    // Iterar sobre cada registro y agregarlo al store
		    recordsArray.forEach(function(recordString) {
		        var fields = recordString.split(',');
		        if (fields.length > 1) {
		            store.add({
		                order: fields[0],
		                description: fields.slice(1).join(",")
		            });
		        }
		    });
		////////////////////
		
    	//Se actualizan campos del formulario PlantAccessWorkerForm
    	Ext.getCmp('pawUpdateFunctionOn').setValue("ON");
    	
		//Se actualizan campos del formulario PlantAccessRequestDocForm
		Ext.getCmp('heavyEquipmentRequestDoc').setValue(record.data.heavyEquipment);
    	if(record.data.heavyEquipment){
    		Ext.getCmp('text_SRC').allowBlank=false;
    		Ext.getCmp('text_RM').allowBlank=false;
    		Ext.getCmp('documentContainerDoc10').show();
    		Ext.getCmp('documentContainerDoc12').show();
    	} else {
    		Ext.getCmp('text_SRC').allowBlank=true;
    		Ext.getCmp('text_RM').allowBlank=true;
    		Ext.getCmp('documentContainerDoc10').hide();
    		Ext.getCmp('documentContainerDoc12').hide();
    	}    	
    	
    	//Habilita o deshabilita control Enviar Solicitud
    	this.showRequestViewAvailableByStatus();
    	debugger
    	//Deshabilita Agregar Trabajador
    	Ext.getCmp('plantAccessAddWorker').setVisible(false);
    	
    	var paRequestId = record.data.rfc;
		Ext.Ajax.request({
		    url: 'plantAccess/searchFilesPlantAccessNew.action',
		    method: 'POST',
		    params: {
		    	requestId:paRequestId
	        },
		    success: function(fp, o) {
		    	debugger
		    	var res = Ext.decode(fp.responseText);
				var rec =Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
				var fileArray = res.data;
		    	
				//Carga el nombre de los archivos de la solicitud
				var formDoc = me.getPlantAccessRequestDocForm().getForm();
				if(fileArray){
					fileArray.forEach(function(element) {
						var field = formDoc.findField('text_'+element.documentType.replace('REQUEST_',''));
						if(field){
							field.setValue(element.originName);
						}
					});
				}
		    	debugger
				//Valida documentos de la solicitud cargados y habilita control Agregar Trabajador
		    	if (formDoc.isValid()) {
		    		//Habilita control Agregar Trabajador
		    		me.showAddNewWorkerBtnByStatus();
		    	}
		    	
				//Se actualiza el grid de PlantAccessRequestGrid
				Ext.getCmp('listActivities').show();
				me.refreshRequestWorkersGrid(false);
		    	
		    },
		    failure: function() {
		    }
		});

    },
    
    gridSelectionChangeOld: function(model, record) { 
    	debugger;
    	checkPlantAccess = true;
    	statusPlantAccess = record.data.status;
    	uuidPlantAccess = record.data.rfc;
    	
    	var me = this;
		me.viewAccessPlant = new Ext.Window({
			layout : 'fit',
			//title : SuppAppMsg.approvalDetailsSupplier ,
			title : SuppAppMsg.plantAccess47 ,
			width : 1050,
			height : 600,
			modal : true,
			closeAction : 'destroy',
			resizable : true,
			minimizable : false,
			maximizable : false,
			plain : true,
			items : [ {
				xtype : 'plantAccessDetailPanel',
    			border : true,
			} ]
		
		});
		me.viewAccessPlant.show();
		//Ext.getCmp('rfcPlantAccess').setValue(uuid);
		//Ext.getCmp('addnameRequest').setValue(displayName);
		//uuidPlantAccess = uuid;
    	
    	var form = this.getPlantAccessForm().getForm();
    	//Workers
    	var grid = this.getPlantAccessDetailGrid(); 
    	var store = grid.getStore();
    	Ext.getCmp('uploadFileRequest').show();
    	Ext.Ajax.request({
		    url: 'plantAccess/searchPlantAccessRequest.action',
		    method: 'POST',
		    params: {
		    	uuid:record.data.rfc,
	        },
		    success: function(fp, o) {
		    	var res = Ext.decode(fp.responseText);
				
		    	if(statusPlantAccess =='APROBADO' ||
		    		statusPlantAccess == 'PENDIENTE' ||
		    		role != 'ROLE_SUPPLIER'){
		    		Ext.getCmp('listActivities').show();
		    		Ext.getCmp('uploadPlantAccessRequestBtn').hide();
			    	Ext.getCmp('uploadFileRequest').setText(SuppAppMsg.plantAccess48);
			    	Ext.getCmp('addemployeename').hide();
			    	Ext.getCmp('addmembershipIMSS').hide();
			    	Ext.getCmp('addDatefolioIDcard').hide();
			    	Ext.getCmp('addPlantAccessRequestEmployBtnAct').hide();
			    	//Ext.getCmp('containerAllDocs').hide();
			    	Ext.getCmp('textcheckworkers').hide();
			    	Ext.getCmp('checkworkers1').hide();
			    	Ext.getCmp('checkworkers2').hide();
			    	Ext.getCmp('checkworkers3').hide();
			    	Ext.getCmp('checkworkers4').hide();
			    	
			    	Ext.getCmp('textDocsRequest').hide();
			    	//Ext.getCmp('documentContainerDoc1').hide();
			    	Ext.getCmp('documentContainerDoc2').hide();
			    	Ext.getCmp('documentContainerSUA').hide();
			    	Ext.getCmp('documentContainerDoc4').hide();
//			    	Ext.getCmp('documentContainerDoc5').hide();
//			    	Ext.getCmp('documentContainerDoc6').hide();
//			    	Ext.getCmp('documentContainerDoc7').hide();
//			    	Ext.getCmp('documentContainerDoc8').hide();
//			    	Ext.getCmp('documentContainerDoc9').hide();
			    	
			    	Ext.getCmp('addcontractorCompany').setReadOnly(true);
			    	Ext.getCmp('addcontractorRepresentative').setReadOnly(true);
			    	Ext.getCmp('addnordenNumber').setReadOnly(true);
			    	Ext.getCmp('addnameRequest').setReadOnly(true);
			    	Ext.getCmp('adddescriptionUbication').setReadOnly(true);
			    	Ext.getCmp('plantRequest').setReadOnly(true);
			    	//Ext.getCmp('addAproval').setReadOnly(true);
			    	Ext.getCmp('addHeavyequipment').setReadOnly(true);
			    	
//			    	ocultar cargar documentos y agregar trabajador
			    	Ext.getCmp('uploadFileShow').hide();
			    	Ext.getCmp('AgrTabNuevo').hide();
			    	
			    	
		    	}else{
//		    		Ext.getCmp('text_SI').allowBlank = true;
//		    		Ext.getCmp('text_PSUA').allowBlank = true;
//		    		Ext.getCmp('text_SUA').allowBlank = true;
//		    		Ext.getCmp('text_FPCOPAA').allowBlank = true;
//		    		Ext.getCmp('text_OCIMSS').allowBlank = true;
//		    		Ext.getCmp('text_GC1').allowBlank = true;
//		    		Ext.getCmp('text_GCCOVID').allowBlank = true;
//		    		Ext.getCmp('text_MCE').allowBlank = true;
//		    		Ext.getCmp('text_MPDF').allowBlank = true;
		    		Ext.getCmp('uploadFileShow').show();
		    		Ext.getCmp('AgrTabNuevo').show();
		    		if(res.data.highRiskActivities.includes('HEAVYEQUIPMENT')){
		    			Ext.getCmp('documentContainerDoc10').show();
                		Ext.getCmp('documentContainerDoc11').show();
                		Ext.getCmp('documentContainerDoc12').show();
		    		}
		    	}
				
				form.findField('estatusPlantAccess').setValue(res.data.status);
				form.findField('rfcPlantAccess').setValue(res.data.rfc);
				form.findField('addnordenNumber').setValue(res.data.ordenNumber);
				form.findField('addnameRequest').setValue(res.data.nameRequest);
				form.findField('addcontractorCompany').setValue(res.data.contractorCompany);
				form.findField('addcontractorRepresentative').setValue(res.data.contractorRepresentative);
				form.findField('adddescriptionUbication').setValue(res.data.descriptionUbication);
				form.findField('addAproval').setValue(res.data.aprovUser); 
				form.findField('addressNumberPA').setValue(res.data.addressNumberPA);
				form.findField('plantRequest').setValue(res.data.plantRequest);
				
				
				
				
				if(res.data.highRiskActivities.includes('HEAVYEQUIPMENT'))
					form.findField('addHeavyequipment').setValue(true); 
				/*
				if(res.data.highRiskActivities.includes('WORKATHEIGHTS'))
					form.findField('addworkatheights').setValue(true); 
				if(res.data.highRiskActivities.includes('CONFINEDSPACES'))
					form.findField('addconfinedspaces').setValue(true); 
				if(res.data.highRiskActivities.includes('CONTELECTRICWORKS'))
					form.findField('addcontelectricworks').setValue(true); 
				if(res.data.highRiskActivities.includes('WORKHOTS'))
					form.findField('addworkhots').setValue(true); 
				if(res.data.highRiskActivities.includes('CHEMICALSUBSTANCES'))
					form.findField('addchemicalsubstances').setValue(true); */
				
				uuidPlantAccess = res.data.rfc;
		    	
		    	Ext.Ajax.request({
				    url: 'plantAccess/searchWorkersPlantAccessByIdRequest.action',
				    method: 'POST',
				    params: {
			        	//uuid:uuidPlantAccessWorker
				    	uuid:res.data.id
			        },
				    success: function(fp, o) {
				    	var res = Ext.decode(fp.responseText);
				    	//var res1 = Ext.decode(o.response.responseText);
						var rec =Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
						store.removeAll();
				    	store.add(rec.raw);
				    	grid.getView().refresh();
				    	
				    	var workers=store.data.items;
//				    	for (var i = 0; i < workers.length; i++) {
//				    		var listDocuments = workers[i].data.listDocuments;
//				    		debugger;
//				    		if(listDocuments != null){
//				    			if(listDocuments.includes('WORKER_CI')// &&
////						    		listDocuments.includes('WORKER_DSCI') &&
////						    		listDocuments.includes('WORKER_CCOVID') &&
//						    		// listDocuments.includes('WORKER_CMA')&&
////						    		listDocuments.includes('WORKER_IDEN')
//						    		) workers[i].data.allDocuments = true;
//							    	
//						    	var activities = workers[i].data.activities;
//								if(activities.includes('1')){
//									if(workers[i].data.listDocuments.includes('WORKER_CM1') &&
//										workers[i].data.listDocuments.includes('WORKER_CD3TA')) workers[i].data.docsActivity1 = true;
//								}else workers[i].data.docsActivity1 = true;
//								
//								if(activities.includes('2')){
//									if(workers[i].data.listDocuments.includes('WORKER_CD3G')) workers[i].data.docsActivity2 = true;
//								}else workers[i].data.docsActivity2 = true;
//								
//								if(activities.includes('3')){
//									if(workers[i].data.listDocuments.includes('WORKER_CM1') &&
//										workers[i].data.listDocuments.includes('WORKER_CD3TEC')) workers[i].data.docsActivity3 = true;
//								}else workers[i].data.docsActivity3 = true;
//								
//								if(activities.includes('4')){
//									if(workers[i].data.listDocuments.includes('WORKER_CD3TE1')) workers[i].data.docsActivity4 = true;
//								}else workers[i].data.docsActivity4 = true;
//								
//								if(activities.includes('5')){
//									if(workers[i].data.listDocuments.includes('WORKER_CD3TC')) workers[i].data.docsActivity5 = true;
//								}else workers[i].data.docsActivity5 = true;
//								
//								if(activities.includes('6')){
//									if(workers[i].data.listDocuments.includes('WORKER_HS')) workers[i].data.docsActivity6 = true;
//								}else workers[i].data.docsActivity6 = true;
//								
//								if(activities.includes('*')){
//									if(workers[i].data.listDocuments.includes('WORKER_AE')) workers[i].data.docsActivity7 = true;
//								}else workers[i].data.docsActivity7 = true;
//				    		}
//						}
				    	
				    	if(checkPlantAccess){ 
				    		grid.columns[5].setVisible(true);
				    		grid.columns[4].setText(SuppAppMsg.plantAccess50);
				    	}
				    	
				    	if(checkPlantAccess && 
			    			role == 'ROLE_SUPPLIER'&&
				    		statusPlantAccess == 'RECHAZADO'){ 
				    		grid.columns[5].setVisible(true);
				    		grid.columns[4].setText(SuppAppMsg.taxvaultUploandDocuments);
					    	}
				    	
				    	if( !(['GUARDADO','RECHAZADO'].includes(statusPlantAccess))){
				    		grid.columns[5].setVisible(false);
				    	}
				    	
				    },
				    failure: function() {
				    	Ext.MessageBox.show({
			                title: 'Error',
			                msg: SuppAppMsg.approvalUpdateError,
			                buttons: Ext.Msg.OK
			            });
				    }
				});
				
		    },
		    failure: function() {
		    	Ext.MessageBox.show({
	                title: 'Error',
	                msg: SuppAppMsg.approvalUpdateError,
	                buttons: Ext.Msg.OK
	            });
		    }
		});
    },
    
    approvePlantAccess : function(grid, rowIndex, colIndex, record) {
    	
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var dto = Ext.create('SupplierApp.model.PlantAccess',record.data);
    	var status ="APROBADO";
    	
    	var filePanel = Ext.create(
				'Ext.form.Panel',
				{
					//width : 900,
					bodyPadding: 10,
				    layout: {
				        type: 'vbox',
				        align: 'stretch'
				    },
				    defaults: {
				        anchor: '100%',   // ocupa todo el ancho disponible
				        labelWidth: 120,
				        margin: '5 10'
				    },
					items : [{
								xtype : 'displayfield',
								value : SuppAppMsg.approvalNoteAcept,
								//margin:'0 20 0 10',
							},{
					        	//xtype : 'textfield',
								xtype: 'textareafield',
								fieldLabel : '',
								name : 'noteApprovPA',
								id:'noteApprovPA',
								multiline: true,
								//width:400,
								height : 70,
								maxLength : 250,
								enforceMaxLength : true,
								//margin:'0 20 0 10',
								allowBlank:false
							},{
								xtype : 'displayfield',
								value : SuppAppMsg.plantAccess61,
								//margin:'0 20 0 10',
							},{
						        xtype: 'fieldcontainer',
						        layout: 'hbox',
						        items: [{
								xtype: 'datefield',
					            fieldLabel: SuppAppMsg.purchaseOrderDesde,
					            id: 'paFromDate',
					            itemId: 'paFromDate',
					            name:'paFromDate',
					            allowBlank:false,
					            minValue: new Date(),
					            width:160,
					            labelWidth:35,
					            margin:'0 20 0 10',
					            	listeners:{
										change: function(field, newValue, oldValue){
											Ext.getCmp("paToDate").setMinValue(newValue);
										}
									},
							},{
								xtype: 'datefield',
					            fieldLabel: SuppAppMsg.purchaseOrderHasta,
					            id: 'paToDate',
					            itemId: 'paToDate',
					            name:'paToDate',
					            minValue: new Date(),
					            allowBlank:false,
					            width:160,
					            labelWidth:35,
					            margin:'0 0 0 10'
							}]
						    }],

					buttons : [ {
						text : SuppAppMsg.supplierLoad,
						margin:'10 0 0 0',
						handler : function() {
							var form = this.up('form').getForm();
							if (form.isValid()) {
								var box = Ext.MessageBox.wait(
										SuppAppMsg.approvalUpdateData,
										SuppAppMsg.approvalExecution);

								Ext.Ajax.request({
								    url: 'plantAccess/updateAprov.action',
								    method: 'POST',
								    params: {
								    	status:status,
								    	note:Ext.getCmp("noteApprovPA").getValue(),
								    	idReques:record.data.id,
								    	paFromDate:Ext.getCmp("paFromDate").getValue(),
								    	paToDate:Ext.getCmp("paToDate").getValue(),
							        },
								    jsonData: dto.data,
								    success: function(fp, o) {
								    	debugger
								    	var res = Ext.decode(fp.responseText);
								    	grid.store.load();
								    	box.hide();
								    	me.winLoadInvFile.destroy();
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
								    	else if(!res.success){
								    		//Ext.Msg.alert(SuppAppMsg.approvalResponse, res.message);
								    		var messageLength = res.message.length;
								    		var msgWidth = Math.min(Math.max(messageLength * 6, 200), 600);  // Ajusta 6 según el tamaño de fuente

								    		Ext.Msg.show({
								    		    title: SuppAppMsg.approvalResponse,
								    		    msg: res.message,
								    		    buttons: Ext.Msg.OK,
								    		    icon: Ext.Msg.INFO,
								    		    width: msgWidth,
								    		    autoScroll: true
								    		});
								    		
								    		
								    		
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
		            			Ext.Msg.alert(SuppAppMsg.approvalAlert, SuppAppMsg.plantAccess60 );
		            		}
						}
					} ]
				});

					me.winLoadInvFile = new Ext.Window({
						layout : 'fit',
						title : SuppAppMsg.approvalAceptSupp,
						//width : 500,
						//height : 235,
						width: Ext.Element.getViewportWidth() * 0.55,   // 👈 40% de pantalla
		                maxWidth: 500,                                // 👈 ancho mínimo
		                height: Ext.Element.getViewportHeight() * 0.85, // 👈 40% de alto
		                maxHeight: 300,
						modal : true,
						closeAction : 'destroy',
						resizable : false,
						minimizable : false,
						maximizable : false,
						plain : true,
						items : [ filePanel ]
				
					});
					me.winLoadInvFile.show();
    	
    	/*var dlgAccept = Ext.MessageBox.show({
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
						    url: 'plantAccess/updateAprov.action',
						    method: 'POST',
						    params: {
						    	status:status,
						    	note:notes,
						    	idReques:record.data.id
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
    
    upfileWork : function(grid, rowIndex, colIndex, record) {
    	 recordWorker = grid.store.getAt(rowIndex).data;
    
    	
    },
    
    rejectInvoiceFDA : function(grid, rowIndex, colIndex, record) {
    	
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var dto = Ext.create('SupplierApp.model.PlantAccess',record.data);
    	var status ="RECHAZADO";
    	
    	var winWidth = Ext.Element.getViewportWidth();
    	var boxWidth = Math.min(Math.max(winWidth * 0.35, 400), 500); // ancho mínimo 400, máximo 600, 50% pantalla
    	
    	var dlgRejected = Ext.MessageBox.show({
    		title : SuppAppMsg.rejectDoc,
			msg : SuppAppMsg.approvalNoteReject,
			buttons : Ext.MessageBox.YESNO,
			multiline: true,
			//width:500,
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
							url: 'plantAccess/updateAprov.action',
						    method: 'POST',
						    params: {
						    	status:status,
						    	note:notes,
						    	idReques:record.data.id,
						    	paFromDate:new Date(),
						    	paToDate:new Date(),
					        },
						    jsonData: dto.data,
						    success: function(fp, o) {
						    	debugger;
						    	var res = Ext.decode(fp.responseText);
						    	grid.store.load();
						    	box.hide();
						    	if(res.message == "Success"){
						    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.plantAccess53);
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
    		title : SuppAppMsg.plantAccess96,
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
    					    				xtype:'plantAccessDocumentsForm'
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
    	
    	var grid = this.getPlantAccessGrid();
    	var store = grid.getStore();
    	
    	var addressNumberPA = Ext.getCmp('addressNumberGrid').getValue();
    	var rfc = Ext.getCmp('RFC').getValue();
    	var Status = Ext.getCmp('combostatus').getValue();
    	var approver = Ext.getCmp('approverPlantAccess').getValue();

    	store.removeAll();    	
    	store.proxy.extraParams = {
    			rFC : rfc?rfc:"",
    			status : Status?Status:"",
    			approver:approver?approver:"",
    			addressNumberPA:addressNumberPA?addressNumberPA:""
    	    			        }
    	store.loadPage(1);
    	grid.getView().refresh()
    },

    addNewPlantAccessRequest : function(button) {        
    	var me = this;
		me.viewAccessPlant = new Ext.Window({
			layout : 'fit',
			title : SuppAppMsg.plantAccess47 ,
			//width : 1050,
			//height : 600,
			width: Ext.Element.getViewportWidth() * 0.90,   // 👈 40% de pantalla
            maxWidth: 1050,                                // 👈 ancho mínimo
            height: Ext.Element.getViewportHeight() * 0.90, // 👈 40% de alto
            maxHeight: 600,
			modal : true,
			closeAction : 'destroy',
			resizable : true,
			minimizable : false,
			maximizable : false,
			scrollable : true,
			plain : true,
			items : [ {
				xtype : 'plantAccessMainPanel',
    			border : true,
			} ],
		    listeners: {
		        beforeclose: this.onCloseMainPanel
		    }
		
		});
		debugger
		me.viewAccessPlant.show();		
		Ext.getCmp('paContractorCompany').setValue(supplierProfile.data.razonSocial);
		Ext.getCmp('paRequestAddressNumber').setValue(addressNumber);
		Ext.getCmp('plantAccessAddWorker').setVisible(false);
		Ext.getCmp('paRequestRfc').setValue('');//Nueva Solicitud
		Ext.getCmp('pawRequestNumber').setValue('');//Nueva Solicitud
		Ext.getCmp('pawTempId').setValue('');//Nuevo Trabajador
		
		//Limpia grid de trabajadores PlantAccessRequestGrid
    	var gWorkers = me.getPlantAccessRequestGrid();
    	gWorkers.store.loadData([], false);
    	gWorkers.getView().refresh();

		//Limpia grid de archivos de trabajadores PlantAccessWorkerGrid
    	var gFiles = me.getPlantAccessWorkerGrid();
    	gFiles.store.loadData([], false);
    	gFiles.getView().refresh();
    	
    	//Se actualizan campos del formulario PlantAccessWorkerForm
    	Ext.getCmp('pawUpdateFunctionOn').setValue('ON');
    },
    
    
    addNewPlantAccessRequestOld : function(button) {
  
    	var dt = new Date().getTime();
        var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = (dt + Math.random()*16)%16 | 0;
            dt = Math.floor(dt/16);
            return (c=='x' ? r :(r&0x3|0x8)).toString(16);
        });
        
    	var me = this;
		me.viewAccessPlant = new Ext.Window({
			layout : 'fit',
			//title : SuppAppMsg.approvalDetailsSupplier ,
			title : SuppAppMsg.plantAccess47 ,
			width : 1050,
			height : 600,
			modal : true,
			closeAction : 'destroy',
			resizable : true,
			minimizable : false,
			maximizable : false,
			plain : true,
			items : [ {
				xtype : 'plantAccessDetailPanel',
    			border : true,
			} ]
		
		});
		me.viewAccessPlant.show();
		Ext.getCmp('rfcPlantAccess').setValue("Nuevo");
		Ext.getCmp('uploadFileRequest').hide();
		//Ext.getCmp('addnameRequest').setValue(displayName);
		Ext.getCmp('addressNumberPA').setValue(userName);
		uuidPlantAccess = uuid;
		
		var grid = this.getPlantAccessDetailGrid();
    	var store = grid.getStore();
    	store.removeAll();
    	grid.getView().refresh();
    	
    	checkPlantAccess = false;
    },
    
    addPlantAccessRequestEmployBtnAct:function(button) {
    	Ext.getCmp('addPlantAccessRequestEmployBtnAct').hide();
    	if (Ext.getCmp('addemployeename').getValue().trim() != ''&&
    		Ext.getCmp('addemployeenappat').getValue().trim() != ''&&
    		Ext.getCmp('addemployeenapmat').getValue().trim() != ''&&
    		Ext.getCmp('addmembershipIMSS').getValue() != ''&& 
    		Ext.getCmp('addDatefolioIDcard').getValue() != ''&&
    		Ext.getCmp('addfolioIDcard').getValue() != ''){ 
    		if(Ext.getCmp('addmembershipIMSS').getValue().length!=11){
    			Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: SuppAppMsg.plantAccess63});
    			return;
    		}
    		
    		
    		var grid = this.getPlantAccessDetailGrid();
        	var store = grid.getStore();
    		var worker = store.findRecord('datefolioIDcard',  Ext.getCmp('addDatefolioIDcard').getValue());
    		
    		if(worker ==null){
    			var activities= ""+((Ext.getCmp('addworkatheights').getValue()?"1,":"")
    					+(Ext.getCmp('addHeavyequipmentWorker').getValue()?"2,":"")
    					+(Ext.getCmp('addconfinedspaces').getValue()?"3,":"")
    					+(Ext.getCmp('addcontelectricworks').getValue()?"4,":"")
    					+(Ext.getCmp('addworkhots').getValue()?"5,":"")
    					+(Ext.getCmp('addchemicalsubstances').getValue()?"6,":"")
    					+(Ext.getCmp('activityFree').getValue()?"*":""));
        		
        		if(activities!=""){
        			
                	 row = Ext.create('SupplierApp.model.PlantAccessDetail', 
                			   {
                		 
                		 
                		 
                			    id:0,
                				employeeName:Ext.getCmp('addemployeename').getValue().trim()+' '+Ext.getCmp('addemployeenappat').getValue().trim()+' '+Ext.getCmp('addemployeenapmat').getValue().trim(),
                				membershipIMSS:Ext.getCmp('addmembershipIMSS').getValue(),
                				datefolioIDcard: Ext.Date.format(Ext.getCmp('addDatefolioIDcard').getValue(), 'd-m-Y')+'/'+Ext.getCmp('addfolioIDcard').getValue(),
                				activities:activities
                			    });
                	 debugger;
                	 Ext.Ajax.request({
             		    url: 'plantAccess/uploadWorker.action',
             		    method: 'POST',
             		    params:  {
              				employeeName:Ext.getCmp('addemployeename').getValue().trim()+' '+Ext.getCmp('addemployeenappat').getValue().trim()+' '+Ext.getCmp('addemployeenapmat').getValue().trim(),
              				membershipIMSS:Ext.getCmp('addmembershipIMSS').getValue().trim(),
              				datefolioIDcard:Ext.Date.format(Ext.getCmp('addDatefolioIDcard').getValue(), 'd-m-Y')+'/'+Ext.getCmp('addfolioIDcard').getValue(),
              				activities:activities,
              				uuidPlantAccess:uuidPlantAccess,
              				id:Ext.getCmp('addemployeeId').getValue()
              			    },
             		    success: function(fp, o) {
             		    	debugger;
             		    	var res = Ext.decode(fp.responseText);
             		    	Ext.getCmp('addemployeeId').setValue(res.data.id);
             		    	var sup = Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
             		    	Ext.getCmp('bodyeaderFileWorker').show();
             		    	store.add(sup);
             		    	grid.getView().refresh()
        	        	
             		 }})
                	 
//             		Ext.getCmp('addemployeename').setValue('');
//                	 Ext.getCmp('addemployeenappat').setValue('');
//                	Ext.getCmp('addfolioIDcard').setValue('');
//                	 Ext.getCmp('addemployeenapmat').setValue('');
//         			Ext.getCmp('addmembershipIMSS').setValue('');
//         			Ext.getCmp('addDatefolioIDcard').setValue('');
//         			Ext.getCmp('addworkatheights').reset();
//         			Ext.getCmp('addHeavyequipmentWorker').reset();
//         			Ext.getCmp('addconfinedspaces').reset();
//         			Ext.getCmp('addcontelectricworks').reset();
//         			Ext.getCmp('addworkhots').reset(); 
//         			Ext.getCmp('addchemicalsubstances').reset();
//         			Ext.getCmp('activityFree').reset();

        		}else Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.plantAccess89 , msg:  SuppAppMsg.plantAccessTempMessage10 });
    		}else Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.plantAccess89 , msg:  SuppAppMsg.plantAccessTempMessage11 });    		
    	}else Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.plantAccess89 , msg:  SuppAppMsg.plantAccessTempMessage12 });
    	
    },
    
    plantAccessEditWorker: function(grid, rowIndex, colIndex) {
    	debugger;
    	var me = this;
    	this.changeToPlantAccessWorkerView();
    	
		//Actualiza información del formulario PlantAccessWorkerForm
    	var formWorker = this.getPlantAccessWorkerForm().getForm();
    	var record = grid.store.getAt(rowIndex);
    	
    	//Deshabilita la actualización del trabajador temporalmente
    	Ext.getCmp('pawUpdateFunctionOn').setValue("OFF");

    	//Actualiza información
    	formWorker.loadRecord(record);
    	
    	//Setea valores no acoplados
    	Ext.getCmp('pawTempId').setValue(record.data.id);
    	Ext.getCmp('pawDateInduction').setValue(new Date(record.data.dateInduction));

    	if(record.data.docsActivity1){
    		Ext.getCmp('toolDoc6').show();
    		Ext.getCmp('toolDoc7').show();
    	}
    	if(record.data.docsActivity2){
    		Ext.getCmp('toolDoc8').show();
    	}
    	if(record.data.docsActivity3){
    		Ext.getCmp('toolDoc6').show();
    		Ext.getCmp('toolDoc9').show();
    	}
    	if(record.data.docsActivity4){
    		Ext.getCmp('toolDoc10').show();
    	}
    	if(record.data.docsActivity5){
    		Ext.getCmp('toolDoc11').show();
    	}
    	if(record.data.docsActivity6){
    		Ext.getCmp('toolDoc12').show();
    	}
    	if(record.data.docsActivity7){
    		Ext.getCmp('toolDoc13').show();
    	}
		
    	//Habilita la actualización del trabajador nuevamente
    	Ext.getCmp('pawUpdateFunctionOn').setValue("ON");

    	
    	
    	// lenar el calatolo de ordenes ingresadas
    	var ordenNumberValue = Ext.getCmp('paOrdenNumber').value;  
    	
    	
    	
    	
    	 var grid = Ext.getCmp('selectionGrid'); // Obtener la referencia al grid
		    var store = grid.getStore(); // Obtener el store del grid
		    var selectedrecordsArray=Ext.getCmp('pawEmployeeOrdenes').getValue().split('|');;
		    // Limpiar el store actual
		    store.removeAll();

		    // Separar la cadena en registros individuales
		    var recordsArray =  ordenNumberValue.split('|');
debugger
		    
		    // Iterar sobre cada registro y agregarlo al store
		    recordsArray.forEach(function(recordString) {
		        var fields = recordString.split(',');
		        if (fields.length > 1) {
		            store.add({
		                order: fields[0],
		                description: fields.slice(1).join(",")
		            });
		        }
		    });
		    
		    var selectionModel = Ext.getCmp('selectionGrid').getSelectionModel();
		    var recordsToSelect = [];

		    store.each(function(record) {
		        var order = record.get('order');
		        var description = record.get('description');
		        var recordString = order + ',' + description;
		        if (selectedrecordsArray.includes(recordString)) {
		            recordsToSelect.push(record);
		        }
		    });

		    // Seleccionar los registros en el grid
		    selectionModel.select(recordsToSelect);
		    
		    ////// termina seleccion de ordenes por trabajador
    	
    	
    	//Actualiza controles de archivos
    	var paRequestId = Ext.getCmp('paRequestRfc').getValue();
    	var paWorkerId = Ext.getCmp('pawTempId').getValue();    	
    	if(paRequestId){
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution); 
        	Ext.Ajax.request({
    		    url: 'plantAccess/searchWorkerFilesPlantAccessByIdWorker.action',
    		    method: 'POST',
    		    params: {
    		    	idRequest: paRequestId,
    		    	idWorker: paWorkerId
    	        },
    		    success: function(fp, o) {
    		    	debugger;
    		    	var res = Ext.decode(fp.responseText);
    				var rec =Ext.create('SupplierApp.model.PlantAccessFile',res.data);
    				var fileArray = res.data;
    		    	
    				//Carga el nombre de los archivos de la solicitud
    				fileArray.forEach(function(element) {
    					debugger;
    					var field = formWorker.findField('text_'+element.documentType.replace('WORKER_',''));
    					if(field){
    						field.setValue(element.originName);
    					}
    				});
    				
    				//Actualiza grid de archivos de trabajador
    		    	me.refreshWorkerFileGrid();
    		    	box.hide();
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
    
    plantAccessAddWorker: function(button) {
    	debugger
    	this.changeToPlantAccessWorkerView();
    	
    	//Nuevo Trabajador
    	Ext.getCmp('pawTempId').setValue('');
    	
		//Limpia grid de archivos de trabajadores PlantAccessWorkerGrid
    	var gFiles = this.getPlantAccessWorkerGrid();
    	gFiles.store.loadData([], false);
    	gFiles.getView().refresh();
    	
    	// lenar el calatolo de ordenes ingresadas
    	var ordenNumberValue = Ext.getCmp('paOrdenNumber').value;  
    	
    	
    	
    	
    	 var grid = Ext.getCmp('selectionGrid'); // Obtener la referencia al grid
		    var store = grid.getStore(); // Obtener el store del grid

		    // Limpiar el store actual
		    store.removeAll();

		    // Separar la cadena en registros individuales
		    var recordsArray =  ordenNumberValue.split('|');

		    // Iterar sobre cada registro y agregarlo al store
		    recordsArray.forEach(function(recordString) {
		        var fields = recordString.split(',');
		       if (fields.length > 1) {
		            store.add({
		                order: fields[0],
		                description: fields.slice(1).join(",")
		            });
		        }
		    });
    	
    	
    	
    	
    	
    	
    },

    plantAccessFinishWorker: function(button) {
    	debugger;
    	var me = this;
    	var isValidInfo = true;
    	var validationMessage = '';
		var formWorker = this.getPlantAccessWorkerForm().getForm();
		var isAllDocsOk = Ext.getCmp('pawAllDocuments').checked;
		
		//Valida datos del trabajador
		if(!formWorker.isValid()) {
			isValidInfo = false;
			validationMessage = SuppAppMsg.plantAccessTempMessage13;
			
		} else if(Ext.getCmp('pawDocsActivity1').checked == false
				&& Ext.getCmp('pawDocsActivity2').checked == false
				&& Ext.getCmp('pawDocsActivity3').checked == false
				&& Ext.getCmp('pawDocsActivity4').checked == false
				&& Ext.getCmp('pawDocsActivity5').checked == false
				&& Ext.getCmp('pawDocsActivity6').checked == false
				&& Ext.getCmp('pawDocsActivity7').checked == false) {
			isValidInfo = false;
			validationMessage = SuppAppMsg.plantAccessTempMessage14;
			
		} else if(isAllDocsOk == false) {
			isValidInfo = false;
			validationMessage = SuppAppMsg.plantAccessTempMessage15;
		}
		
		if(isValidInfo){
			//OK
			this.updatePlantAccessWorker(false, true, true);
    		//this.resetPlantAccessWorkerForm();	    	
	    	//this.refreshRequestWorkersGrid(true);
		} else {
    		Ext.MessageBox.show({
        	    title: SuppAppMsg.supplierMessage,
        	    msg: validationMessage,
        	    buttons : Ext.MessageBox.YESNO,
        	    icon: Ext.MessageBox.QUESTION,
        	    width: 450,
        	    heigh: 200,
    			buttonText : {
    				yes : "Sí",
    				no : "No"
    			},
    			fn : function(btn, text) {
    				if (btn === 'yes') {
    					//me.updatePlantAccessWorker(false, true, false);
    					me.resetPlantAccessWorkerForm();
    					me.refreshRequestWorkersGrid(false);
    				}
    			}
        	});
		}
	},
	
    plantAccessShowRequest: function(button) {
		this.resetPlantAccessWorkerForm();
		this.refreshRequestWorkersGrid(false);
    },
	
	resetPlantAccessWorkerForm: function() {
		//Actualiza información del formulario PlantAccessWorkerForm
    	Ext.getCmp('pawUpdateFunctionOn').setValue("OFF");

    	Ext.getCmp('pawTempId').setValue('');    	
    	Ext.getCmp('pawEmployeeName').setValue('');
    	Ext.getCmp('pawEmployeeLastName').setValue('');
    	Ext.getCmp('pawEmployeeSecondLastName').setValue('');
    	Ext.getCmp('pawMembershipIMSS').setValue('');
    	Ext.getCmp('pawDateInduction').setValue('');
    	Ext.getCmp('pawCardNumber').setValue('');
    	Ext.getCmp('pawEmployeeCurp').setValue('');
    	Ext.getCmp('pawEmployeeRfc').setValue('');
    	Ext.getCmp('pawEmployeePuesto').setValue('');

    	Ext.getCmp('pawDateInduction').setValue(null);
    	Ext.getCmp('pawDocsActivity1').setValue(false);
    	Ext.getCmp('pawDocsActivity2').setValue(false);
    	Ext.getCmp('pawDocsActivity3').setValue(false);
    	Ext.getCmp('pawDocsActivity4').setValue(false);
    	Ext.getCmp('pawDocsActivity5').setValue(false);
    	Ext.getCmp('pawDocsActivity6').setValue(false);
    	Ext.getCmp('pawDocsActivity7').setValue(false);
    	
    	Ext.getCmp('text_CMA').setValue('');
    	Ext.getCmp('text_CI').setValue('');
    	Ext.getCmp('text_CM1').setValue('');
    	Ext.getCmp('text_CD3TA').setValue('');
    	Ext.getCmp('text_CD3G').setValue('');
    	Ext.getCmp('text_CD3TEC').setValue('');
    	Ext.getCmp('text_CD3TE1').setValue('');
    	Ext.getCmp('text_CD3TC').setValue('');
    	Ext.getCmp('text_HS').setValue('');
    	Ext.getCmp('text_AE').setValue('');
    	
		Ext.getCmp('toolDoc6').hide();
		Ext.getCmp('toolDoc7').hide();
		Ext.getCmp('toolDoc8').hide();
		Ext.getCmp('toolDoc9').hide();
		Ext.getCmp('toolDoc10').hide();
		Ext.getCmp('toolDoc11').hide();
		Ext.getCmp('toolDoc12').hide();
		Ext.getCmp('toolDoc13').hide();
		
		//Limpia grid de archivos de trabajadores PlantAccessWorkerGrid
    	var gFiles = this.getPlantAccessWorkerGrid();
    	gFiles.store.loadData([], false);
    	gFiles.getView().refresh();
    	
		//Habilita la actualización del trabajador nuevamente
    	Ext.getCmp('pawUpdateFunctionOn').setValue("ON");
    	this.changeToPlantAccessRequestView();
    	
    	/*
		//SE RESETEA EL FORMULARIO A SUS VALORES INICIALES
		var formWorker = this.getPlantAccessWorkerForm().getForm();
		
		//DESHABILITA: Eventos y validaciones temporalmente		
    	formWorker.getFields().each(function(field) {
    		if(field.allowBlank !== undefined){
	    	    field.suspendEvents();
	    	    field.allowBlank = false;
    		}
    	});
    	
    	//RESET
    	formWorker.reset();
    	
    	//HABILITA: Eventos y validaciones
    	formWorker.getFields().each(function(field) {
    		if(field.allowBlank !== undefined){
	    	    field.allowBlank = true;
	    	    field.resumeEvents();
    		}
    	});
    	*/
	},
	
	changeToPlantAccessRequestView: function() {
		debugger
		//Formulario de datos y actividades del trabajador
		Ext.getCmp('paWorkerForm').hide();
    	
		//Opciones barra de tareas (Arriba)
    	Ext.getCmp('savePlantAccessRequest').show();
    	Ext.getCmp('showPlantAccessRequestFiles').show();
    	debugger
    	//Opciones barra de tareas (Abajo)
    	Ext.getCmp('plantAccessAddWorker').show();
    	Ext.getCmp('plantAccessFinishWorker').hide();
    	Ext.getCmp('plantAccessShowRequest').hide();
    	
    	//Pestaña activa
    	Ext.getCmp('plantAccessMainTabPanel').setActiveTab(0);
    	
    	//Habilita control Enviar Solicitud
    	this.showRequestViewAvailableByStatus();
        
    	//Habilita control Agregar Trabajador
    	this.showAddNewWorkerBtnByStatus();
	},
	
	changeToPlantAccessWorkerView: function() {
		//Formulario de datos y actividades del trabajador
		Ext.getCmp('paWorkerForm').show();
		
		//Opciones barra de tareas (Arriba)
    	Ext.getCmp('savePlantAccessRequest').hide();
    	Ext.getCmp('showPlantAccessRequestFiles').hide();
    	debugger
    	//Opciones barra de tareas (Abajo)
    	Ext.getCmp('plantAccessAddWorker').hide();
    	Ext.getCmp('plantAccessFinishWorker').show();
    	Ext.getCmp('plantAccessShowRequest').hide();
    	
    	//Pestaña
    	Ext.getCmp('plantAccessMainTabPanel').setActiveTab(1);
    	
    	//Definición de controles por estatus
    	var statusPlantAccess = Ext.getCmp('paRequestStatus').getValue();    	
    	
        if(['PENDIENTE','RECHAZADO','APROBADO'].includes(statusPlantAccess)){
        	Ext.getCmp('paWorkerForm').hide();
        	Ext.getCmp('plantAccessFinishWorker').hide();
        	Ext.getCmp('plantAccessShowRequest').show();
        }    		        
        if(['RECHAZADO'].includes(statusPlantAccess) && role == 'ROLE_SUPPLIER'){
        	Ext.getCmp('paWorkerForm').show();
        	Ext.getCmp('plantAccessFinishWorker').show();
        	Ext.getCmp('plantAccessShowRequest').hide();
        }    	
	},
	
    updatePlantAccessRequestBtn: function(button) {
    	
    	
    	
    	
        	var form = this.getPlantAccessRequestForm().getForm();

        	debugger
        	// Definir los campos a omitir en la validación
        	var omitFields = ['paOrdenNumberInput', 'description']; 

        	// Obtener lista de campos no válidos, excluyendo los que están en omitFields
        	var invalidFields = [];
        	form.getFields().each(function(field) {
        	    if (!field.isValid() && !omitFields.includes(field.getId())) {
        	        invalidFields.push(field.getFieldLabel() || field.getName());
        	    }
        	});

        	// Si hay campos inválidos, mostrar mensaje de error
        	if (invalidFields.length > 0) {
        	    Ext.MessageBox.alert({ 
        	        maxWidth: 700, 
        	        minWidth: 650, 
        	        title: SuppAppMsg.plantAccess89, 
        	        msg: SuppAppMsg.plantAccessTempMessage16 
        	    });
        	} else {
        	    this.updatePlantAccessRequest();
        	    Ext.getCmp('plantAccessRequestTabPanel').setActiveTab(1);
        	}

    },
    
    updatePlantAccessRequestDocBtn: function(button) {
    	var form = this.getPlantAccessRequestDocForm().getForm();
    	if (form.isValid()) {
    		Ext.getCmp('plantAccessRequestTabPanel').setActiveTab(0);
    	} else {
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.plantAccess89, msg: SuppAppMsg.plantAccessTempMessage17 })
    	}
    },
    
    updatePlantAccessRequestDocsCheck: function(checkbox, newValue, oldValue, eOpts) {
    	var me = this;
    	//Muestra controles para adicionar los archivos de Equipo Pesado
    	var radio = Ext.getCmp('heavyEquipmentRequestDoc').checked;
    	if(radio){
    		Ext.getCmp('text_SRC').allowBlank=false;
    		Ext.getCmp('text_RM').allowBlank=false;
    		Ext.getCmp('documentContainerDoc10').show();
    		Ext.getCmp('documentContainerDoc12').show();
    		
        	//Setea el valor en el formulario de PlantAccessRequestForm para que tome el valor desde ese formulario al guardar la solicitud.
        	Ext.getCmp('heavyEquipmentRequest').setValue(radio);
        	//Actualiza la solicitud
        	this.updatePlantAccessRequest();
    	} else {
    		if(!Ext.getCmp('text_SRC').isValid() && !Ext.getCmp('text_RM').isValid()){
    			//Aún no se han cargado documentos
        		Ext.getCmp('text_SRC').allowBlank=true;
        		Ext.getCmp('text_RM').allowBlank=true;
        		Ext.getCmp('documentContainerDoc10').hide();
        		Ext.getCmp('documentContainerDoc12').hide();
        		
            	//Setea el valor en el formulario de PlantAccessRequestForm para que tome el valor desde ese formulario al guardar la solicitud.
            	Ext.getCmp('heavyEquipmentRequest').setValue(radio);
            	//Actualiza la solicitud
            	this.updatePlantAccessRequest();
    		} else {
		    	Ext.MessageBox.show({
		            title: SuppAppMsg.plantAccess90,
		            msg: SuppAppMsg.plantAccessTempMessage18,
		    	    buttons : Ext.MessageBox.YESNO,
		    	    icon: Ext.MessageBox.QUESTION,
		    	    width: 650,
		    	    heigh: 200,
					buttonText : {
						yes : SuppAppMsg.plantAccess91,
						no : "No"
					},
		            fn: function showResult(btn){
		                if(btn == 'yes'){
            		    	Ext.getCmp('text_SRC').setValue('');
            		    	Ext.getCmp('text_RM').setValue('');
                    		Ext.getCmp('text_SRC').allowBlank=true;
                    		Ext.getCmp('text_RM').allowBlank=true;
                    		Ext.getCmp('documentContainerDoc10').hide();
                    		Ext.getCmp('documentContainerDoc12').hide();
                    		
                        	//Setea el valor en el formulario de PlantAccessRequestForm para que tome el valor desde ese formulario al guardar la solicitud.
                        	Ext.getCmp('heavyEquipmentRequest').setValue(radio);                        	
                        	//Actualiza la solicitud
                        	me.updatePlantAccessRequest();
		                } else {
                        	//Setea el valor en el formulario de PlantAccessRequestForm para que tome el valor desde ese formulario al guardar la solicitud.
		                	checkbox.setValue(oldValue);		                	
                        	//Actualiza la solicitud
                        	me.updatePlantAccessRequest();   
		                }
		            }
		        });
    		}
    	}
    },
    
    updatePlantAccessWorkerDocsCheck: function(checkbox, newValue, oldValue, eOpts) {
    	debugger;
    	
    	//recargar la seleccion de ordenes 
    	var grid = Ext.getCmp('selectionGrid');
    	var store = grid.getStore();
    	var selectionModel = grid.getSelectionModel();

    	// Obtener los registros seleccionados
    	var selectedRecords = selectionModel.getSelection();

    	// Iterar sobre los registros seleccionados y realizar la acción deseada
    	selectedRecords.forEach(function(record) {
    	    var order = record.get('order');
    	    var description = record.get('description');
    	    console.log('Orden:', order, 'Descripción:', description);
    	});

    	// Si necesitas concatenar los datos seleccionados como en tu ejemplo anterior
    	var concatenatedData = selectedRecords.map(function(record) {
    	    return record.get('order') + ',' + record.get('description');
    	}).join('|');

    	Ext.getCmp('pawEmployeeOrdenes').setValue(concatenatedData);
    	
    	var formWorker = this.getPlantAccessWorkerForm().getForm();
    	var updateFunctionOn = Ext.getCmp('pawUpdateFunctionOn').getValue();
    	
    	if(updateFunctionOn == 'ON'){//No se ejecuta cuando se resetea el formulario
    		if (newValue) {//Habilitar Casilla
    			//Valida datos del trabajador    						    		
    	    	if (formWorker.isValid()) {
    	    		//Guardar información
    				switch(checkbox.name){
    				case 'docsActivity1':
    					Ext.getCmp('toolDoc6').show();
    					Ext.getCmp('toolDoc7').show();
    					break;
    				case 'docsActivity2':
    					Ext.getCmp('toolDoc8').show();
    					break;
    				case 'docsActivity3':
    	               	Ext.getCmp('toolDoc6').show();
    					Ext.getCmp('toolDoc9').show();
    					break;
    				case 'docsActivity4':
    					Ext.getCmp('toolDoc10').show();
    					break;
    				case 'docsActivity5':
    					Ext.getCmp('toolDoc11').show();
    					break;
    				case 'docsActivity6':
    					Ext.getCmp('toolDoc12').show();
    					break;
    				case 'docsActivity7':
    					Ext.getCmp('toolDoc13').show();
    					break;
    				default: break;
    				}
    				//Guarda o Actualiza infomación del trabajador
    				this.updatePlantAccessWorker(true, false, false);
    	    	} else {
    	    		//Eliminar información
    	    		Ext.MessageBox.show({
    	    			width: 400,
    	        	    title: SuppAppMsg.plantAccess89,	        	    
    	        	    msg: SuppAppMsg.plantAccessTempMessage19	        	    
    	        	});    	    		
    	    		checkbox.setValue(oldValue);
    	    	}
    		} else {//Deshabilitar casilla
    			switch(checkbox.name){
    			case 'docsActivity1':
    				var isChecked3 = Ext.getCmp('pawDocsActivity3').checked;
    				if(!isChecked3){//El documento se comparte con la actividad 3
    					Ext.getCmp('text_CM1').setValue('');
    					Ext.getCmp('toolDoc6').hide();
    				}
        	    	Ext.getCmp('text_CD3TA').setValue('');    				
    				Ext.getCmp('toolDoc7').hide();
    				break;
    			case 'docsActivity2':
    				Ext.getCmp('text_CD3G').setValue('');
    				Ext.getCmp('toolDoc8').hide();
    				break;
    			case 'docsActivity3':
    				var isChecked1 = Ext.getCmp('pawDocsActivity1').checked;
    				if(!isChecked1){//El documento se comparte con la actividad 1
    					Ext.getCmp('text_CM1').setValue('');
    					Ext.getCmp('toolDoc6').hide();
    				}
    				Ext.getCmp('text_CD3TEC').setValue('');
    				Ext.getCmp('toolDoc9').hide();
    				break;
    			case 'docsActivity4':
    				Ext.getCmp('text_CD3TE1').setValue('');
    				Ext.getCmp('toolDoc10').hide();
    				break;
    			case 'docsActivity5':
    				Ext.getCmp('text_CD3TC').setValue('');
    				Ext.getCmp('toolDoc11').hide();
    				break;
    			case 'docsActivity6':
    				Ext.getCmp('text_HS').setValue('');
    				Ext.getCmp('toolDoc12').hide();
    				break;
    			case 'docsActivity7':
    				Ext.getCmp('text_AE').setValue('');
    				Ext.getCmp('toolDoc13').hide();
    				break;
    			default: break;
    			}
    			
    			if (formWorker.isValid()) {
        			//Guarda o Actualiza infomación del trabajador
        			this.updatePlantAccessWorker(true, false, false);
    			}
    		}	
    	}
    },
    
    updatePlantAccessRequest: function() {
    	debugger;
    	//var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
    	var me = this;
    	var form = this.getPlantAccessRequestForm().getForm();
		var values = form.getFieldValues();
		var record = Ext.create('SupplierApp.model.PlantAccessRequest');	
		//// generar resgistro multiple para ordenes
		
		
		 var grid = this.getPlantAccessRequestForm().down('gridpanel');
		    var store = grid.getStore();
		    
		    // Generar la cadena con los valores de order y description
		    var concatenatedData = '';
		    store.each(function(record, index) {
		        var order = record.get('order');
		        var description = record.get('description');
		        concatenatedData += order + ',' + description;
		        if (index < store.getCount() - 1) {
		            concatenatedData += '|';
		        }
		    });
		    
		    // Agregar la cadena generada a los valores del formulario
		    values.ordenNumber = concatenatedData;
		
		
		
		debugger
		
		 // SOLUCIÓN: Limpiar el ID del record antes de asignar valores
	    record.set('id', null); // Forzar ID a null para nuevo registro
	    
	    // Asignar valores excluyendo el ID si es temporal
	    if (values.id && typeof values.id === 'string' && values.id.indexOf('SupplierApp.model') !== -1) {
	        delete values.id;
	    }
		
		//var updatedRecord = populateObj(record, values);
		 record.set(values); 
		//record.set(updatedRecord);
		record.save({
			callback: function (records, o, success, msg) {
				debugger
				if(success == true){
		    		var r1 = Ext.decode(o._response.responseText);
			    	var res = Ext.decode(r1);

			    	if(res.message != ''){
			    		box.hide();
			    		Ext.MessageBox.show({
	    	        	    title: SuppAppMsg.plantAccess89,
	    	        	    msg: res.message
	    	        	});
			    		return false;
			    	} else {
			    		debugger
			    		//Actualiza información del formulario PlantAccessRequestForm
			    		var recordNew = Ext.create('SupplierApp.model.PlantAccessRequest', res.data);
			    		form.loadRecord(recordNew);
			    		 debugger
			    		//var rawDate = res.data.fechafirmGui.time; // Suponiendo que el campo en record.raw se llama 'fechafirmGui'
			    		 var rawDate = res.data.fechafirmGui && res.data.fechafirmGui.time;
			 		    if (rawDate) {
			 		        var dateField = form.findField('fechafirmGui');
			 		        var date = new Date(rawDate); // Convertir timestamp a objeto Date
			 		        dateField.setValue(date); // Establecer el valor en el campo de fecha
			 		    }
			    						    		
			    		//Se actualiza campos del formulario PlantAccessRequestDocForm			    		
			    		Ext.getCmp('heavyEquipmentRequestDoc').setValue(recordNew.data.heavyEquipment);
			    		
			    		//Valida documentos de la solicitud cargados y habilita control Agregar Trabajador
			    		var formDoc = me.getPlantAccessRequestDocForm().getForm();		
			    		debugger
				    	if (formDoc.isValid()) {
				    		//Habilita control Agregar Trabajador
				    		me.showAddNewWorkerBtnByStatus();
				    	} else {
				    		Ext.getCmp('plantAccessAddWorker').setVisible(false);
				    	}
			    		
			    		//box.hide();
			    		return true;				    		
			    	}
				}
			}
		});
    },
    
    updatePlantAccessWorker: function(isUpdateForm, isResetForm, isShowMessage) {
    	debugger;
    	
//recargar la seleccion de ordenes 
    	var grid = Ext.getCmp('selectionGrid');
    	var store = grid.getStore();
    	var selectionModel = grid.getSelectionModel();

    	// Obtener los registros seleccionados
    	var selectedRecords = selectionModel.getSelection();

    	// Iterar sobre los registros seleccionados y realizar la acción deseada
    	selectedRecords.forEach(function(record) {
    	    var order = record.get('order');
    	    var description = record.get('description');
    	    console.log('Orden:', order, 'Descripción:', description);
    	});

    	// Si necesitas concatenar los datos seleccionados como en tu ejemplo anterior
    	var concatenatedData = selectedRecords.map(function(record) {
    	    return record.get('order') + ',' + record.get('description');
    	}).join('|');

    	Ext.getCmp('pawEmployeeOrdenes').setValue(concatenatedData);
    	
    	
    	
    	var paRequestId = Ext.getCmp('paRequestRfc').getValue();
    	Ext.getCmp('pawRequestNumber').setValue(paRequestId);
    	
    	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);    	
    	var me = this;
    	var form = this.getPlantAccessWorkerForm().getForm();
		var values = form.getFieldValues();
		var record = Ext.create('SupplierApp.model.PlantAccessWorker');		
		//var updatedRecord = populateObj(record, values);
		
		// SOLUCIÓN: Limpiar el ID del record antes de asignar valores
	    record.set('id', null); // Forzar ID a null para nuevo registro
	    
	    // Asignar valores excluyendo el ID si es temporal
	    if (values.id && typeof values.id === 'string' && values.id.indexOf('SupplierApp.model') !== -1) {
	        delete values.id;
	    }
		
		//var updatedRecord = populateObj(record, values);
		 record.set(values); 
		//record.set(updatedRecord);
		
		//record.set(updatedRecord);
		record.save({
			callback: function (records, o, success, msg) {
				debugger;
				if(success == true){
		    		var r1 = Ext.decode(o._response.responseText);
			    	var res = Ext.decode(r1);

			    	if(res.message != ''){ 
			    		box.hide();
			    		Ext.MessageBox.alert({
	    	        	    title: SuppAppMsg.plantAccess89,
	    	        	    msg: res.message
	    	        	});
			    		return true;
			    	} else {
			    		box.hide();
			    		
			    		if(isUpdateForm){
    			    		//Actualiza información del formulario PlantAccessWorkerForm
    			    		var recordNew = Ext.create('SupplierApp.model.PlantAccessWorker', res.data); 
    			    		form.loadRecord(recordNew);
    			    		Ext.getCmp('pawDateInduction').setValue(new Date(recordNew.data.dateInduction));
    			    		
    			    		//Actualiza grid de archivos de trabajador
    			    		me.refreshWorkerFileGrid();
			    		}
			    		
			    		if(isResetForm){
			    			//Resetea Formulario (Para cambio de vista)
			    			me.resetPlantAccessWorkerForm();
			    			//Refresca grid de trabajadores (Vista de solicitud)
			    			me.refreshRequestWorkersGrid(isShowMessage);
			    		}
			    		
			    		return true;
			    	}
				} else {
					debugger
		    		var r1 = Ext.decode(o.response.responseText);
			    	var res = Ext.decode(r1);

			    	if(res.message != ''){
			    		box.hide();
			    		Ext.MessageBox.show({
	    	        	    title: SuppAppMsg.plantAccess89,
	    	        	    msg: res.message
	    	        	});
			    		return false;
			    	}
					box.hide();
				}
			}
		});
    },
    
    savePlantAccessRequest: function() {
    	debugger;
    	var form = this.getPlantAccessRequestForm().getForm();
    	var formDoc = this.getPlantAccessRequestDocForm().getForm();
    	
    	
    	
    	var me = this;
        var grid = me.getPlantAccessRequestGrid();
        var store = grid.getStore();

        // Objeto para almacenar la frecuencia de los valores de membershipIMSS
        var membershipIMSSCounts = {};

        // Contar las ocurrencias de cada membershipIMSS
        store.each(function(record) {
            var membershipIMSS = record.get('membershipIMSS');
            if (membershipIMSSCounts[membershipIMSS]) {
                membershipIMSSCounts[membershipIMSS]++;
            } else {
                membershipIMSSCounts[membershipIMSS] = 1;
            }
        });

        // Marcar registros duplicados
        store.each(function(record) {
            var membershipIMSS = record.get('membershipIMSS');
            if (membershipIMSSCounts[membershipIMSS] > 1) {
                record.set('isDuplicate', true);
            } else {
                record.set('isDuplicate', false);
            }
        });

        // Refrescar la vista y aplicar el estilo a las filas duplicadas
        grid.getView().refresh(); // Refrescar la vista del grid antes de aplicar el estilo

        // Obtener los nodos (filas) del grid y aplicar el estilo
        var rows = grid.getView().getNodes();  // Obtiene todas las filas del grid
        Ext.each(rows, function(row, rowIndex) {
            var record = store.getAt(rowIndex);  // Obtener el record correspondiente a la fila
            if (record.get('isDuplicate')) {
                // Aplicar el color de fondo para los duplicados
                Ext.get(row).setStyle('background-color', '#ffcccc');
            } else {
                // Limpiar el fondo si no es duplicado
                Ext.get(row).setStyle('background-color', '');
            }
        });

        // Mostrar un mensaje con los valores duplicados
        var duplicatedMemberships = Object.keys(membershipIMSSCounts).filter(function(key) {
            return membershipIMSSCounts[key] > 1;  
        });
        grid.getView().refresh(); 
        if (duplicatedMemberships.length > 0) {
//            Ext.Msg.alert('Valores Duplicados', 'Los siguientes valores de membershipIMSS se repiten: ' + duplicatedMemberships.join(', '));
            
            Ext.create('Ext.window.Window', {
                title: 'NSS Duplicados',
                height: 400,  // Define la altura de la ventana
                width: 600,   // Define el ancho de la ventana
                layout: 'fit',
                items: [{
                    xtype: 'textareafield',
                    value: 'Los siguientes valores de '+SuppAppMsg.plantAccess24+' se repiten: \n' + duplicatedMemberships.join(', '),
                    readOnly: true,
                    height: '100%',  // Se adapta automáticamente a la ventana
                    width: '100%',
                    grow: true,  // Habilita el crecimiento dinámico si es necesario
                    growMax: 500,  // Tamaño máximo que puede crecer
                    growMin: 100   // Tamaño mínimo
                }],
                buttons: [{
                    text: 'Cerrar',
                    handler: function() {
                        this.up('window').close();
                    }
                }]
            }).show();
            
            return;
        }

    	
    	
    	
    	
    	if (form.isValid()) {        	
        	if (formDoc.isValid()) {
            	var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution);
            	var me = this;    	
            	var oldStatus = Ext.getCmp('paRequestStatus').getValue();
            	if(oldStatus=='GUARDADODSALIDA'){
            		Ext.getCmp('paRequestStatus').setValue('GUARDADO');
            	}else{
            		Ext.getCmp('paRequestStatus').setValue('PENDIENTE');

            	}
            	
            	var form = this.getPlantAccessRequestForm().getForm();
        		var values = form.getFieldValues();
        		var record = Ext.create('SupplierApp.model.PlantAccessRequest');		
        		//var updatedRecord = populateObj(record, values);
        		//record.set(updatedRecord);
        		
        		 // SOLUCIÓN: Limpiar el ID del record antes de asignar valores
        	    record.set('id', null); // Forzar ID a null para nuevo registro
        	    
        	    // Asignar valores excluyendo el ID si es temporal
        	    if (values.id && typeof values.id === 'string' && values.id.indexOf('SupplierApp.model') !== -1) {
        	        delete values.id;
        	    }
        		
        		 record.set(values); 
        		record.save({
        			callback: function (records, o, success, msg) {
        				if(success == true){
        		    		var r1 = Ext.decode(o._response.responseText);
        			    	var res = Ext.decode(r1);
        			    	if(res.message != ''){
        			    		try{
        			    			Ext.getCmp('paRequestStatus').setValue(oldStatus);
        			    		}catch(e){
        			    			
        			    		}
        			    		
        			    		box.hide();
        			    		Ext.MessageBox.alert({
        			    			maxWidth: 700,
        			    			minWidth: 650,
        			    			title: SuppAppMsg.plantAccess89,
        			    			msg: res.message
        			    		});
        			    		return false;
        			    	} else {
        			    		//Cierra ventana
        			    		me.viewAccessPlant.destroy();
        			    		var grid = me.getPlantAccessGrid();
        			        	var store = grid.getStore();
        			        	store.reload();
        			    		grid.getView().refresh();        			    		
        			    		box.hide();
        			    		if(records.data.status!='GUARDADO'){
        			    			Ext.MessageBox.show({
        	    	        	    title: SuppAppMsg.plantAccess89,
        	    	        	    msg: SuppAppMsg.plantAccess97
        	    	        	});
        			    		}
        			    		
        			    		return true;				    		
        			    	}
        				}
        			}
        		});
        	} else {
        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.plantAccess89, msg: SuppAppMsg.plantAccessTempMessage17 })
        	}
    	} else {
    		debugger
    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.plantAccess89, msg: SuppAppMsg.plantAccessTempMessage16 })
    	}
    },
    
    refreshRequestWorkersGrid: function(isShowMessage) {
    	debugger;
    	var me = this;
    	var paRequestId = Ext.getCmp('paRequestRfc').getValue();
    	
    	if(paRequestId){
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution); 
        	Ext.Ajax.request({
    		    url: 'plantAccess/searchWorkersPlantAccessByIdRequest.action',
    		    method: 'POST',
    		    params: {
    		    	uuid:paRequestId
    	        },
    		    success: function(fp, o) {
    		    	var res = Ext.decode(fp.responseText);
    		    	var grid = me.getPlantAccessRequestGrid();    		    	
    		    	grid.store.loadData([], false);
    		    	grid.getView().refresh();
    		    	
    		    	if(res.data && res.data.length > 0){
        		    	for(var i = 0; i < res.data.length; i++){
        		    		var r = Ext.create('SupplierApp.model.PlantAccessWorker', res.data[i]);
        		    		grid.store.insert(i, r);
        		    	}
    		    	}
    		    	//Obtiene el estatus de la solicitud
    		    	var statusPlantAccess = Ext.getCmp('paRequestStatus').getValue();
    		    	
    		        // Obtener el índice (index) de la columna que deseas modificar
    		    	var columnIndexEdit = grid.headerCt.items.findIndex('dataIndex', 'paEditRequestWorker');
    		        var columnIndexDelete = grid.headerCt.items.findIndex('dataIndex', 'paDeleteWorker');    		        
		        	grid.columns[columnIndexEdit].setText(SuppAppMsg.taxvaultUploandDocuments);
		        	grid.columns[columnIndexDelete].setVisible(true);
		        	
    		        // Cambios en columnas al renderizar el grid
    		        if(['PENDIENTE','RECHAZADO','APROBADO'].includes(statusPlantAccess)){
    		        	grid.columns[columnIndexEdit].setText(SuppAppMsg.plantAccess50);
    		        	grid.columns[columnIndexDelete].setVisible(false);
    		        }    		        
    		        if(['RECHAZADO'].includes(statusPlantAccess) && role == 'ROLE_SUPPLIER'){
    		        	grid.columns[columnIndexEdit].setText(SuppAppMsg.taxvaultUploandDocuments);
    		        	grid.columns[columnIndexDelete].setVisible(true);
    		        }
    		        
    		    	box.hide();
    		    	if(isShowMessage){
    		    		Ext.MessageBox.show({
    		    			width: 400,
    		        	    title: SuppAppMsg.plantAccess89,	        	    
    		        	    msg: SuppAppMsg.plantAccessTempMessage20	        	    
    		        	});
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

    refreshWorkerFileGrid: function() {
    	debugger;
    	var me = this;
    	var paRequestId = Ext.getCmp('paRequestRfc').getValue();
    	var paWorkerId = Ext.getCmp('pawTempId').getValue();
    	
    	if(paRequestId){
    		var box = Ext.MessageBox.wait(SuppAppMsg.supplierProcessRequest, SuppAppMsg.approvalExecution); 
        	Ext.Ajax.request({
    		    url: 'plantAccess/searchWorkerFilesPlantAccessByIdWorker.action',
    		    method: 'POST',
    		    params: {
    		    	idRequest: paRequestId,
    		    	idWorker: paWorkerId
    	        },
    		    success: function(fp, o) {
    		    	debugger
    		    	var res = Ext.decode(fp.responseText);
    		    	var grid = me.getPlantAccessWorkerGrid();
    		    	grid.store.loadData([], false);
    		    	grid.getView().refresh();
    		    	if(res.data && res.data.length > 0){
        		    	for(var i = 0; i < res.data.length; i++){
        		    		var r = Ext.create('SupplierApp.model.PlantAccessFile', res.data[i]);
        		    		grid.store.insert(i, r);
        		    	}
    		    	}
    		    	
    		    	//Obtiene el estatus de la solicitud
    		    	var statusPlantAccess = Ext.getCmp('paRequestStatus').getValue();
    		    	
    		        // Obtener el índice (index) de la columna que deseas modificar
    		        var columnIndexDeleteFile = grid.headerCt.items.findIndex('dataIndex', 'paDeleteWorkerFile');
		        	grid.columns[columnIndexDeleteFile].setVisible(true);
		        	
    		        // Cambios en columnas al renderizar el grid
    		        if(['PENDIENTE','RECHAZADO','APROBADO'].includes(statusPlantAccess)){
    		        	grid.columns[columnIndexDeleteFile].setVisible(false);   		        	
    		        }    		        
    		        if(['RECHAZADO'].includes(statusPlantAccess) && role == 'ROLE_SUPPLIER'){
    		        	grid.columns[columnIndexDeleteFile].setVisible(true);
    		        }
    		        
    		    	box.hide();
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
    
    showRequestViewAvailableByStatus: function(){
    	debugger
    	//Definición de controles por estatus
    	var statusPlantAccess = Ext.getCmp('paRequestStatus').getValue();    	
    	Ext.getCmp('savePlantAccessRequest').show();
    	Ext.getCmp('updatePlantAccessRequest').show();
    	Ext.getCmp('paNameRequest').setReadOnly(false);
    	Ext.getCmp('paOrdenNumber').setReadOnly(false);
    	Ext.getCmp('paContractorCompany').setReadOnly(true);
    	Ext.getCmp('paContractorRepresentative').setReadOnly(false);
//    	Ext.getCmp('paDescriptionUbication').setReadOnly(false);
    	Ext.getCmp('paPlantRequest').setReadOnly(false);
    	Ext.getCmp('paContacEmergency').setReadOnly(false);
    	Ext.getCmp('pafechaFirmaGuia').setReadOnly(false);
    	
    	
           		        
        if(['RECHAZADO','GUARDADO'].includes(statusPlantAccess) && role == 'ROLE_SUPPLIER'){
        	Ext.getCmp('savePlantAccessRequest').show();
        	Ext.getCmp('updatePlantAccessRequest').show();
        	Ext.getCmp('paNameRequest').setReadOnly(false);
        	Ext.getCmp('paOrdenNumber').setReadOnly(false);
        	Ext.getCmp('paContractorCompany').setReadOnly(true);
        	Ext.getCmp('paContractorRepresentative').setReadOnly(false);
//        	Ext.getCmp('paDescriptionUbication').setReadOnly(false);
        	Ext.getCmp('paPlantRequest').setReadOnly(false);
        	Ext.getCmp('paContacEmergency').setReadOnly(false);
        	Ext.getCmp('pafechaFirmaGuia').setReadOnly(false);
        }else
        
        if(['PENDIENTE','RECHAZADO','APROBADO','GUARDADO'].includes(statusPlantAccess)){
        	Ext.getCmp('containerOrden').hide();
        	Ext.getCmp('savePlantAccessRequest').hide();
        	Ext.getCmp('updatePlantAccessRequest').hide();
        	Ext.getCmp('paNameRequest').setReadOnly(true);
        	Ext.getCmp('paOrdenNumber').setReadOnly(true);
        	Ext.getCmp('paContractorCompany').setReadOnly(true);
        	Ext.getCmp('paContractorRepresentative').setReadOnly(true);
        	Ext.getCmp('paContacEmergency').setReadOnly(true);
        	Ext.getCmp('pafechaFirmaGuia').setReadOnly(true);
//        	Ext.getCmp('paDescriptionUbication').setReadOnly(true);
        	Ext.getCmp('paPlantRequest').setReadOnly(true);
        	 var grid = Ext.getCmp('ordersPlantaccesGridPanel'); // Obtenemos el grid por su id
             var deleteColumn = grid.down('#deleteActionColumn');
             deleteColumn.setVisible(false); // Ocultamos la columna
        	
        	
        }
    },
    
    showAddNewWorkerBtnByStatus: function(){
    	//Definición de controles por estatus
    	debugger
    	var statusPlantAccess = Ext.getCmp('paRequestStatus').getValue();
    	Ext.getCmp('plantAccessAddWorker').setVisible(true);
    	 if(['RECHAZADO','GUARDADO'].includes(statusPlantAccess) && role == 'ROLE_SUPPLIER'){
        	Ext.getCmp('plantAccessAddWorker').setVisible(true);
        }else
        if(['PENDIENTE','RECHAZADO','APROBADO','GUARDADO'].includes(statusPlantAccess)){
        	Ext.getCmp('plantAccessAddWorker').setVisible(false);
        }    		        
       
    },
    
    uploadPlantAccessRequestAct:function(button) {  
    	Ext.getCmp('uploadPlantAccessRequestBtn').disable();
    	var documentWorkers =false;
    	
    	var controller = _AppGlobSupplierApp.getController('PlantAccess');
		var grid = controller.getPlantAccessDetailGrid();
    	var store = grid.getStore();
    	var workers = store.data.items;
    
    	
debugger;
//    	for (var i = 0; i < workers.length; i++) {
//    		if(!workers[i].data.allDocuments ||
//    			!workers[i].data.docsActivity1 ||
//    			!workers[i].data.docsActivity2 ||
//    			!workers[i].data.docsActivity3 ||
//    			!workers[i].data.docsActivity4 ||
//    			!workers[i].data.docsActivity5 ||
//    			!workers[i].data.docsActivity6 ||
//    			!workers[i].data.docsActivity7 ){
//    			documentWorkers = false;
//    			Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: 'Alguno de los trabajadores no cuenta con todos los documentos' });
//    			return;
//    		}else documentWorkers = true;
//    	}
documentWorkers = true
    	/*for (var i = 0; i < workers.length; i++) {
    		var uuidWorker = workers[i].data.datefolioIDcard;
    		Ext.Ajax.request({
			    url: 'plantAccess/searchFilesPlantAccess.action',
			    method: 'POST',
			    params: {
		        	//uuid:uuidPlantAccessWorker
			    	uuid:uuidWorker + '_' + uuidPlantAccess
		        },
			    success: function(fp, o) {
			    	
			    	var res = Ext.decode(fp.responseText);
					var rec =Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
					var docs = rec.raw;
			    },
			    failure: function() {
			    	Ext.MessageBox.show({
		                title: 'Error',
		                msg: SuppAppMsg.approvalUpdateError,
		                buttons: Ext.Msg.OK
		            });
			    }
			});
		}*/
    	 debugger;
    	var form = this.getPlantAccessForm().getForm();
    	if (form.isValid()) {
    		if(documentWorkers){
    		
    	    	var uuid = uuidPlantAccess;
    		
    			Ext.Ajax.request({
    			    url: 'plantAccess/searchFilesPlantAccess.action',
    			    method: 'POST',
    			    params: {
    			    	uuid:uuid
    		        },
    			    success: function(fp, o) {
    			    	var totalDocs = 3;
    			    	var res = Ext.decode(fp.responseText);
    			    	var extraDocst = Ext.getCmp('addHeavyequipment').getValue();
    			    	if(extraDocst) totalDocs=5;
    			    	debugger;
    			    	if(totalDocs == res.total){
    			    		if(workers.length==0){
    		    	    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: 'Se debe agregar almenos un trabajador' });
    		    	    		Ext.getCmp('uploadPlantAccessRequestBtn').enable();
    		    	    		return;
    		    	    	}
    			    		
    			    		Ext.Ajax.request({
    		        		    url: 'plantAccess/uploadPlantAccesRequest.action',
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
    		        						+(Ext.getCmp('addchemicalsubstances').getValue()?",CHEMICALSUBSTANCES":"")),
    		        			  rfc:Ext.getCmp('rfcPlantAccess').getValue(),
    		        			  addressNumberPA:Ext.getCmp('addressNumberPA').getValue(),
    		        			  plantRequest:Ext.getCmp('plantRequest').getValue(),
    		        		    					 
    		        		    	
    		        	        },
    		        		    success: function(fp, o) {
    		        		    	debugger;
    		        		    	res=JSON.parse(fp.responseText);
    		        		    	
    		        		    	if(!res.success){
    		        		    		Ext.getCmp('uploadPlantAccessRequestBtn').enable();
    		        		    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: res.message });
    		        		    		return;
    		        		    	}
    		        		    	
    		        		    	Ext.MessageBox.show({
    		        	        	    title: SuppAppMsg.plantAccess47,
    		        	        	    msg: SuppAppMsg.plantAccess49,
    		        	        	    buttons: Ext.MessageBox.OK,
    		        	        	    fn: function (btn) {
    		        	        	        if (btn == 'ok') {
    		        	        	        	location.href = "home.action";
    		        	        	        }
    		        	        	    }
    		        	        	});
    		           	
    		           	
    		        		 }})
    			    	}else{ 
    			    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: SuppAppMsg.plantAccessTempMessage21 });
    			    		Ext.getCmp('uploadPlantAccessRequestBtn').enable();
    			    	}
    			    	
    			    },
    			    failure: function() {
    			    	Ext.MessageBox.show({
    		                title: 'Error',
    		                msg: SuppAppMsg.approvalUpdateError,
    		                buttons: Ext.Msg.OK
    		            });
    			    	Ext.getCmp('uploadPlantAccessRequestBtn').enable();
    			    }
    			    
    			});
    			
        		/*Ext.Ajax.request({
        		    url: 'plantAccess/uploadPlantAccesRequest.action',
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
        						+(Ext.getCmp('addchemicalsubstances').getValue()?",CHEMICALSUBSTANCES":"")),
        			  rfc:Ext.getCmp('rfcPlantAccess').getValue(),
        			  addressNumberPA:Ext.getCmp('addressNumberPA').getValue(),
        			  plantRequest:Ext.getCmp('plantRequest').getValue(),
        		    					 
        		    	
        	        },
        		    success: function(fp, o) {
        		    	Ext.MessageBox.show({
        	        	    title: SuppAppMsg.plantAccess47,
        	        	    msg: SuppAppMsg.plantAccess49,
        	        	    buttons: Ext.MessageBox.OK,
        	        	    fn: function (btn) {
        	        	        if (btn == 'ok') {
        	        	        	location.href = "home.action";
        	        	        }
        	        	    }
        	        	});
           	
           	
        		 }})*/
    		}else{
    			Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: SuppAppMsg.plantAccessTempMessage22 })
    			Ext.getCmp('uploadPlantAccessRequestBtn').enable();
    		}
    	}else {Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: SuppAppMsg.plantAccessTempMessage23 })
    		Ext.getCmp('uploadPlantAccessRequestBtn').enable();};
    	
   },  
   
   uploadPlantAccessRequestActHeader:function(button) {  
	  
	var form = this.getPlantAccessForm().getForm();
	if (form.isValid()) {
		
		
		Ext.Ajax.request({
		    url: 'plantAccess/uploadPlantAccesRequestHeader.action',
		    method: 'POST',
		    params: {
			  status:'GUARDADO',
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
						+(Ext.getCmp('addchemicalsubstances').getValue()?",CHEMICALSUBSTANCES":"")),
			  rfc:Ext.getCmp('rfcPlantAccess').getValue(),
			  addressNumberPA:Ext.getCmp('addressNumberPA').getValue(),
			  plantRequest:Ext.getCmp('plantRequest').getValue(),
		    					 
		    	
	        },
		    success: function(fp, o) {
		    	debugger
		    	Ext.getCmp('AgrTabNuevo').show();
		    	Ext.getCmp('rfcPlantAccess').setValue(JSON.parse(fp.responseText).data.rfc);
//		    	Ext.MessageBox.show({
//	        	    title: SuppAppMsg.plantAccess47,
//	        	    msg: SuppAppMsg.plantAccess49,
//	        	    buttons: Ext.MessageBox.OK,
//	        	    fn: function (btn) {
//	        	        if (btn == 'ok') {
//	        	        	
//	        	        }
//	        	    }
//	        	});
   	
   	
		 }})
	
		
	}else {
		
		
		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: SuppAppMsg.plantAccessTempMessage23 });
		return false;
	
	}
	
}, 
 
    
   addFilePlantAccessRequestBtnAct:function(button) {
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
		    	
        
   	 row = Ext.create('SupplierApp.model.PlantAccessFile', 
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


