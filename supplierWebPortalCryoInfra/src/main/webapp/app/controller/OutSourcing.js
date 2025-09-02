Ext.define('SupplierApp.controller.OutSourcing', {
    extend: 'Ext.app.Controller',
    stores: ['OutSourcingDocument'],
    models: ['OutSourcingDocument'],
    views: ['outSourcing.OutSourcingGrid','outSourcing.OutSourcingPanel'],
    refs: [{
        	ref: 'outSourcingGrid',
        	selector: 'outSourcingGrid'
	    }],
 
    init: function() {
   	
    	this.winLoadInv=null;
    	
        this.control({
			'outSourcingGrid button[action=searchDocsOS]' : {
				click : this.searchDocsOS
			},
			'#rejectOSDoc' : {
				"buttonclick" : this.rejectOSDoc
			},
			'#approveSODoc' : {
				"buttonclick" : this.approveSODoc
			},
			'#openOSNotes' : {
				"buttonclick" : this.openOSNotes
			},
			'#uploadNewSODoc' : {
				"buttonclick" : this.uploadNewSODoc
			},
			'outSourcingGrid button[action=downloadDocsOS]' : {
				click : this.downloadDocsOS
			}
        });
    },

    uploadNewSODoc : function(grid, rowIndex, colIndex, store) {
        var me = this;
    	var record = grid.store.getAt(rowIndex);
    	var store = grid.getStore();
    	var filePanel = Ext.create(
    					'Ext.form.Panel',
    					{
    						width : 900,
    						items : [
									{
									xtype : 'textfield',
									name : 'documentType',
									hidden : true,
									value : record.data.documentType
									},{
    									xtype : 'textfield',
    									name : 'id',
    									hidden : true,
    									value : record.data.id
    								},{
    									xtype : 'textfield',
    									name : 'addressNumber',
    									hidden : true,
    									value : record.data.addressBook
    								},{
    									xtype : 'filefield',
    									name : 'uploadedFiles[0]',
    									fieldLabel : 'Archivo de reemplazo:',
    									labelWidth : 130,
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
    												url : 'uploadOSReplacementFile.action',
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
    		title : 'Cargar archivo de reemplazo para el documento: ' + record.data.name,
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
    
    openOSNotes : function(grid, rowIndex, colIndex, record) {
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	
     	var notesWindow = new Ext.Window({
    		layout : 'fit',
    		title : 'Notas del documento: ' + record.data.name,
    		width : 600,
    		height : 260,
    		modal : true,
    		closeAction : 'destroy',
    		resizable : false,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		html: record.data.notes
    	});
     	
     	notesWindow.show();
    },

    downloadDocsOS : function(grid, rowIndex, colIndex, record) {
    	
    	var grid = this.getOutSourcingGrid();
    	var store = grid.getStore();
    	var itemArray = [];
    	var rowArray = [];

    	store.each(function(rec) {
    		if(rec){
		      var recData = [rec.data.id];
		       rowArray.push(recData);
			}
		});
    	
    	data = rowArray;

    	data = rowArray;
    	if(data.length > 0) {
        	var idsExport = '';
        	data.forEach(function(infoArray, index) {
        		
        	  dataString = infoArray.join(',');
        	  idsExport += index < data.length ? dataString + ',' : dataString;
        	});
        	debugger
        	console.log(idsExport);
			Ext.Ajax.request({
				 url: 'downloadDocumentsZip.action',
				    method: 'POST',
				    params: {
				    	ids:idsExport
				    	
		        },
			    success: function(fp, o) {
			    	var res = fp.responseText;
			    	
				    var link = document.createElement('a');
				    link.innerHTML = 'Documentos REPSE';
				    link.download = 'Documentos REPSE.zip';
				    link.href = 'data:application/zip;base64,' + res;
                    link.style.display = 'none';
				    document.body.appendChild(link);
				    
				    link.click();
			
			    },
			    failure: function(fp, o) {
			    	Ext.MessageBox.show({
		                title: 'Error',
		                msg: SuppAppMsg.outsourcingDownloadFilesError,
		                buttons: Ext.Msg.OK
		            });
			    }
			}); 
        	
    	}  	

    },
    
    approveSODoc : function(grid, rowIndex, colIndex, record) {
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	
    	var dlgApproved = Ext.MessageBox.show({
    		title : 'Aprobación de documentos',
			msg : 'Si desea, registre algún comentario asociado a la aprobación para efectos de seguimiento',
			buttons : Ext.MessageBox.YESNO,
			multiline: true,
			width:500,
			buttonText : {
				yes : SuppAppMsg.approvalAcept,
				no : SuppAppMsg.approvalExit
			},
			fn : function(btn, text) {
				if (btn === 'yes') {
						var box = Ext.MessageBox.wait(
								SuppAppMsg.approvalUpdateData,
								SuppAppMsg.approvalExecution);
						var notes = text;
						Ext.Ajax.request({
						    url: 'approveOutSourcingDocument.action',
						    method: 'POST',
						    params: {
						    	id:record.data.id,
						    	notes:notes,
						    	frequency:record.data.frequency,
						    	uuid:record.data.uuid
					        },
						    success: function(fp, o) {
						    	var res = Ext.decode(fp.responseText);
						    	grid.store.load();
						    	box.hide();
						    	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.approvalResponse, msg:  'El documento ha sido APROBADO y el proveedor ha sido notificado.'});
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
			}
		});
    },
    
    rejectOSDoc : function(grid, rowIndex, colIndex, record) {
    	var me = this;
    	var record = grid.store.getAt(rowIndex);
    	
    	var dlgRejected = Ext.MessageBox.show({
    		title : SuppAppMsg.rejectDoc,
			msg : SuppAppMsg.msgRejectDoc,
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
						    url: 'rejectOutSourcingDocument.action',
						    method: 'POST',
						    params: {
						    	id:record.data.id,
						    	notes:notes,
						    	frequency:record.data.frequency,
						    	uuid:record.data.uuid
					        },
						    success: function(fp, o) {
						    	var res = Ext.decode(fp.responseText);
						    	grid.store.load();
						    	box.hide();
						    	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.approvalResponse, msg:  'El documento ha sido RECHAZADO y el proveedor ha sido notificado.'});
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
						Ext.Msg.alert(SuppAppMsg.approvalAlert, 'No registró comentarios, por lo que no se procesará el rechazo');
            		}

				}
			}
		});
    },
   
    searchDocsOS: function (button){

    	var grid = this.getOutSourcingGrid();
    	var status = Ext.getCmp('docStatusOS').getValue();
    	var supplierName = Ext.getCmp('supNameOS').getValue();
    	var supplierNumber = Ext.getCmp('supNumberOS').getValue();
    	var documentType = Ext.getCmp('documentTypeOS').getValue();
    	//var fromDate = Ext.getCmp('fromDateOS').getValue();
    	//var toDate = Ext.getCmp('toDateOS').getValue();
    	var periodMonth = Ext.getCmp('periodMonth').getValue() === null ? 0 : Ext.getCmp('periodMonth').getValue();
    	var periodYear = Ext.getCmp('periodYear').getValue() === null ? 0 : Ext.getCmp('periodYear').getValue();
    	
    	roleType = role;
    	if(role == 'ROLE_ADMIN' || role == 'ROLE_SUPPLIER'){
    		roleType = '';
    	}

    	grid.getStore().getProxy().extraParams={
        	status:status,
        	supplierName:supplierName,
        	supplierNumber:supplierNumber,
        	documentType:documentType,
        	//fromDate:fromDate,
        	//toDate:toDate,
        	roleType:roleType,
        	periodMonth:periodMonth,
        	periodYear:periodYear
    	};
    	
    	grid.getStore().load();
    },
    
   
	initController: function(){
	}
});