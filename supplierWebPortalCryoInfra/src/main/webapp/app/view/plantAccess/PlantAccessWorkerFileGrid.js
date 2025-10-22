

Ext.QuickTips.init();

var storeFileWorker = Ext.create('Ext.data.Store', {
	model: 'SupplierApp.model.PlantAccessFile',
	autoLoad: false,
	pageSize: 20,
	remoteSort: false,
	proxy: {
		type: 'memory',
		reader: {
			type: 'json',
			rootProperty: 'root'
		}, writer: {
			type: 'json',
			writeAllFields: true,
			encode: false
		}
	}
    
});



var guardarWorkerAlter=function(){

	var button = Ext.getCmp('bodyeaderFileWorker'); // Obtén una referencia al botón por su ID o itemId

	if (button.isVisible()) {
		var button = Ext.getCmp('addPlantAccessRequestEmployBtnAct');
		button.fireEvent('click', button);
	}
}

var eliminarFileWork=function(idWorker,idCheckbox){
debugger
try{Ext.Ajax.request({
    url: 'plantAccess/updateCheckBoxWorker.action',
    method: 'POST',
    params: {
    	idWorker:idWorker.value,
    	idCheckbox:idCheckbox,
    	selected:false
    },
    success: function(fp, o) {
    	debugger
    	 var jsonData = Ext.JSON.decode(fp.responseText).data; // Convertir fp en JSON
    	  

    	  var controller = _AppGlobSupplierApp.getController('PlantAccess');
			
			var grid = controller.getPlantAccessWorkerFileGrid();
    	  
			var store = grid.getStore();
			
			
			Ext.Ajax.request({
			    url: 'plantAccess/searchFilesPlantAccess.action',
			    method: 'POST',
			    params: {
			    	uuid:idWorker.value
		        },
			    success: function(fp, o) {
			    	var res = Ext.decode(fp.responseText);
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
			

    	  guardarWorkerAlter();
    },
    failure: function() {
//    	Ext.MessageBox.show({
//            title: 'Error',
//            msg: SuppAppMsg.approvalUpdateError,
//            buttons: Ext.Msg.OK
//        });
    }
}); 
}catch(e){}
	
}



Ext.define('SupplierApp.view.plantAccess.PlantAccessWorkerFileGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.plantAccessWorkerFileGrid',
    loadMask: true,
	frame:false,
	id:'grifileworkerall',
	name:'grifileworkerall',
	border:false,
	cls: 'extra-large-cell-grid',  
	store:storeFileWorker,
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
   
        this.columns = [{
						    text     :SuppAppMsg.taxvaultDocument,
						    width: "30%",
						    dataIndex: 'originName'
						},{
						    text     : SuppAppMsg.fiscalTitle21,
						    width: '40%',
						    dataIndex: 'documentType',
				            renderer: function(value, meta, record) {
				            	
				            	/*//Ocultar documentos ya cargados
								if(recordWorker.listDocuments != null){
									if(recordWorker.listDocuments.includes('WORKER_CMA')) Ext.getCmp('toolDoc1').hide();
									if(recordWorker.listDocuments.includes('WORKER_CI')) Ext.getCmp('toolDoc2').hide();
									if(recordWorker.listDocuments.includes('WORKER_DSCI')) Ext.getCmp('toolDoc3').hide();
									if(recordWorker.listDocuments.includes('WORKER_IDEN')) Ext.getCmp('toolDoc4').hide();
									if(recordWorker.listDocuments.includes('WORKER_CCOVID')) Ext.getCmp('toolDoc5').hide();
									if(recordWorker.listDocuments.includes('WORKER_CM')) Ext.getCmp('toolDoc6').hide();
									if(recordWorker.listDocuments.includes('WORKER_CD3TA')) Ext.getCmp('toolDoc7').hide();
									if(recordWorker.listDocuments.includes('WORKER_CD3G')) Ext.getCmp('toolDoc8').hide();
									if(recordWorker.listDocuments.includes('WORKER_CD3TEC')) Ext.getCmp('toolDoc9').hide();
									if(recordWorker.listDocuments.includes('WORKER_CD3TE')) Ext.getCmp('toolDoc10').hide();
									if(recordWorker.listDocuments.includes('WORKER_CD3TC')) Ext.getCmp('toolDoc11').hide();
									if(recordWorker.listDocuments.includes('WORKER_HS')) Ext.getCmp('toolDoc12').hide();
									*/
				            	
				            	var status = {
				            			WORKER_CMA: 'WORKER_CMA',
				            			WORKER_CI: 'WORKER_CI',
//				            			WORKER_DSCI: 'WORKER_DSCI',
//				            			WORKER_IDEN: 'WORKER_IDEN',
//				            			WORKER_CCOVID: 'WORKER_CCOVID',
				            			WORKER_CM1: 'WORKER_CM1',
				            			WORKER_CD3TA: 'WORKER_CD3TA',
				            			WORKER_CD3G: 'WORKER_CD3G',
				            			WORKER_CD3TEC: 'WORKER_CD3TEC',
				            			WORKER_CD3TE1: 'WORKER_CD3TE1',
				            			WORKER_CD3TC: 'WORKER_CD3TC',
				            			WORKER_HS: 'WORKER_HS',
				            	};
				            	            	
				            	switch (record.data.documentType) {
				            	  case status.WORKER_CMA:
				            		return SuppAppMsg.plantAccess35.toUpperCase();
				            	    break;
				            	  case status.WORKER_CI:
				            		return SuppAppMsg.plantAccess36.toUpperCase();
				              	    break;
//				            	  case status.WORKER_DSCI:
//				            		return SuppAppMsg.plantAccess37.toUpperCase();
//				              	    break;
//				            	  case status.WORKER_IDEN:
//				            		  return SuppAppMsg.plantAccess38.toUpperCase();
//				              	    break;
//				            	  case status.WORKER_CCOVID:
//				            		  return SuppAppMsg.plantAccess39.toUpperCase();
//				              	    break;
				            	  case status.WORKER_CM1:
				            		  return SuppAppMsg.plantAccess40.toUpperCase();
				              	    break;
				            	  case status.WORKER_CD3TA:
				            		  return SuppAppMsg.plantAccess41.toUpperCase();
				              	    break;
				            	  case status.WORKER_CD3G:
				            		  return SuppAppMsg.plantAccess42.toUpperCase();
				              	    break;
				            	  case status.WORKER_CD3TEC:
				            		  return SuppAppMsg.plantAccess43.toUpperCase();
				              	    break;
				            	  case status.WORKER_CD3TE1:
				            		  return SuppAppMsg.plantAccess44.toUpperCase();
				                	break;
				            	  case status.WORKER_CD3TC:
				            		  return SuppAppMsg.plantAccess45.toUpperCase();
				              	    break;
				            	  case status.WORKER_HS:
				            		  return SuppAppMsg.plantAccess46.toUpperCase();
				              	    break;
				              	  default:
				              		  return SuppAppMsg.plantAccess70.toUpperCase();
				              		break;
				            	}

				             }
						},{
				        	xtype: 'actioncolumn', 
				            width: '10%',
				            header: SuppAppMsg.plantAccess34,
				            align: 'center',
							name : 'openDocumentFileWorker',
							//hidden: false,
							itemId : 'openDocumentFileWorker',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	//icon:'resources/images/doc.png',
				            	  iconCls:'icon-document',
			             	      text: SuppAppMsg.freightApprovalTitle9,
				                  handler: function(grid, rowIndex, colIndex) {
				                	  
				                	  var record = grid.store.getAt(rowIndex);
										var href = "plantAccess/openDocumentPlantAccess.action?id=" + record.data.id;
										window.open(href);
										//setTimeout(function(){ newWindow.document.title = record.data.originName; }, 10);
				                  }}]
						
						},{
				        	xtype: 'actioncolumn', 
				            width: 90,
				            header: 'Eliminar documento',
				            align: 'center',
				            hidden:true,
							name : 'deleteFileWorkerPlantAccess',
//							hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
							itemId : 'deleteFileWorkerPlantAccess',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/close.png',
				              	 /* getClass: function(v, metadata, r, rowIndex, colIndex, store) {
				              		if(r.data.documentStatus != "COMPLETED" ){
				              			return "x-hidden-display";
				              		}
				              		   if(
	              	        		  !(role=='ROLE_BF_ADMIN')) {
	                      	        	  
	                      	              return "x-hidden-display";
	                      	          }
				              	      },
				              	      text: SuppAppMsg.approvalReject,*/
				                  handler: function(grid, rowIndex, colIndex) {
				                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
				                  }}]	
						}/*,{
				        	xtype: 'actioncolumn', 
				            width: '10%',
				            header: 'quitar',
				            align: 'center',
							name : 'fviewXMLFDA',
							hidden: false,
							itemId : 'fviewXMLFDA',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/doc.png',
			             	      text: SuppAppMsg.freightApprovalTitle9,
				                  handler: function(grid, rowIndex, colIndex) {
				                	  var record = grid.store.getAt(rowIndex);
										var href = "documents/openDocumentByUuid.action?orderNumber=0&type=xml&uuidFactura=" + record.data.uuidFactura;
										var newWindow = window.open(href, '_blank');
										setTimeout(function(){ newWindow.document.title = 'Factura XML'; }, 10);
				                  }}]
						
						}*/];
        
        this.dockedItems = [
        	 {
                 xtype: 'toolbar',
                 id:'formWorkAccessPlantTitle',
                 style: {
                     background: '#CCCCCC'
                   },
                 dock: 'top',
                 items: [{
 					xtype : 'displayfield',
 					value : SuppAppMsg.plantAccess22,
 					height:20,
 					margin:'5 10 0 300',
 					colspan:3,
 					fieldStyle: 'font-weight:bold'
 					}
                 	]
             },
             {
                 xtype: 'toolbar',
                 id:'formWorkAccessPlantBody',
                 style: {
                     background: 'white'
                   },
                 dock: 'top',
                 items: [
                	 {
              			xtype: 'textfield',
                          fieldLabel: SuppAppMsg.supplierForm21,
                          id: 'addemployeeId',
                          itemId: 'addemployeeId',
                          name:'addemployeeId',
                          hidden:true,
                          width:300,
                          labelWidth:130,
                          margin:'5 10 0 10'
              		},
                	 {
         			xtype: 'textfield',
                     fieldLabel: SuppAppMsg.supplierForm21,
                     id: 'addemployeename',
                     itemId: 'addemployeename',
                     name:'addemployeename',
                     allowBlank:false,
                     width:300,
                     labelWidth:130,
                     margin:'5 10 0 10',
                     autoComplete: 'off'
         		},
         		{
         			xtype: 'textfield',
                     fieldLabel: SuppAppMsg.supplierForm22,
                     id: 'addemployeenappat',
                     itemId: 'addemployeenappat',
                     name:'addemployeenappat',
                     allowBlank:false,
                    width:300,
                     labelWidth:130,
                     margin:'5 10 0 10',
                     autoComplete: 'off'
         		},
         		{
         			xtype: 'textfield',
                     fieldLabel: SuppAppMsg.supplierForm23,
                     id: 'addemployeenapmat',
                     itemId: 'addemployeenapmat',
                     name:'addemployeenapmat',
                     allowBlank:false,
                     width:300,
                     labelWidth:130,
                     margin:'5 10 0 10',
                     autoComplete: 'off'
         		}
         		
                 	]
             },
             
             {
                 xtype: 'toolbar',
                 id:'formWorkAccessPlantBody2',
                 style: {
                     background: 'white'
                   },
                 dock: 'top',
                 items: [{
                 	  xtype: 'textfield',
                 	  fieldLabel: SuppAppMsg.plantAccess24,
                 	  id: 'addmembershipIMSS',
                 	  itemId: 'addmembershipIMSS',
                 	  name: 'addmembershipIMSS',
                 	  allowBlank: false,
                 	  width: 300,
                 	  labelWidth: 130,
                 	  margin: '5 10 0 10',
                 	  validator: function(value) {
                 	    if (value.length !== 11) {
                 	      return 'El valor debe tener 11 caracteres';
                 	    }
                 	    return true;
                 	  },
                 	 enforceMaxLength: true,
                 	maxLength: 11,
                 	autoComplete: 'off'
                 	}
 ,
         		{
         			  xtype: 'datefield',
         			  fieldLabel: SuppAppMsg.plantAccess25,
         			  id: 'addDatefolioIDcard',
         			  itemId: 'addDatefolioIDcard',
         			  name: 'addDatefolioIDcard',
         			  allowBlank: false,
         			  width: 300,
         			  labelWidth: 130,
         			  margin: '5 10 0 10',
         			  format: 'd-m-Y',
         			 autoComplete: 'off',
         			 enableKeyEvents: true,
         			  listeners: {
         			    keydown: function (field, e) {
         			      e.preventDefault();
         			    }
         			  }
         			}
         		 ,{
         			xtype: 'textfield',
                     fieldLabel: SuppAppMsg.plantAccess62,
                     id: 'addfolioIDcard',
                     itemId: 'addfolioIDcard',
                     name:'addfolioIDcard',
                     allowBlank:false,
                     maxLength: 60,
                     width:300,
                     labelWidth:130,
                     margin:'5 10 0 10',
                     autoComplete: 'off'
         		}
                 	]
             },
             
             {
                 xtype: 'toolbar',
                 id:'textcheckworkers',
                 style: {
                     background: '#CCCCCC'
                   },
                 dock: 'top',
                 items: [,{
 					xtype : 'displayfield',
 					value : SuppAppMsg.plantAccess27,
 					height:20,
 					//margin: '50 0 0 10',
 					margin:'5 10 0 300',
 					colspan:3,
 					fieldStyle: 'font-weight:bold'
 					}
                 	]
             }
             ,{
                 xtype: 'toolbar',
                 id:'checkworkers1',
                 style: {
                     background: 'white'
                   },
                 dock: 'top',
                 items: [
 				{
 					xtype: 'checkboxfield',
 		            fieldLabel: SuppAppMsg.plantAccess28,
 		            id: 'addworkatheights',
 		            itemId: 'addworkatheights',
 		            name:'addworkatheights',
 		            width:400,
 		            labelWidth:400,
 		            margin:'5 10 0 10',
 		           listeners: {
 		              change: function(checkbox, newValue, oldValue) {
 		            	  Ext.getCmp('addPlantAccessRequestEmployBtnAct').show();
 		                  // Lógica que se ejecuta cuando se produce el cambio
 		                  if (newValue) {
 		                	 Ext.getCmp('toolDoc6').show();
								Ext.getCmp('toolDoc7').show();
								guardarWorkerAlter();
 		                  } else {
 		                	 Ext.getCmp('toolDoc6').hide();
								Ext.getCmp('toolDoc7').hide();
								eliminarFileWork(Ext.getCmp('addemployeeId'),1,false);
								
 		                  }
 		              }
 		          }
 				},
 				{
 					xtype: 'checkboxfield',
 		            fieldLabel: '2.-' + SuppAppMsg.plantAccess18,
 		            id: 'addHeavyequipmentWorker',
 		            itemId: 'addHeavyequipmentWorker',
 		            name:'addHeavyequipmentWorker',
 		            width:500,
 		            labelWidth:500,
 		            margin:'5 10 0 75',
 		           listeners: {
  		              change: function(checkbox, newValue, oldValue) {
  		            	  Ext.getCmp('addPlantAccessRequestEmployBtnAct').show();
  		                  // Lógica que se ejecuta cuando se produce el cambio
  		                  if (newValue) {
  		                	Ext.getCmp('toolDoc8').show();
  		                	guardarWorkerAlter();
  		                  } else {
  		                	Ext.getCmp('toolDoc8').hide();
  		                	eliminarFileWork(Ext.getCmp('addemployeeId'),2,false);
  		                  }
  		              }
  		          }
 				},
 				/*{
 					xtype: 'checkboxfield',
 		            fieldLabel: 'Espacios Confinados. Se requiere certificado médico y DC3',
 		            id: 'addconfinedspaces',
 		            itemId: 'addconfinedspaces',
 		            colspan:3,
 		            name:'addconfinedspaces',
 		            width:650,
 		            labelWidth:650,
 		            margin:'5 10 0 150',
 				},
 				{
 					xtype: 'checkboxfield',
 		            fieldLabel: 'Trabajos eléctricos. Se requiere DC3',
 		            id: 'addcontelectricworks',
 		            itemId: 'addcontelectricworks',
 		            name:'addcontelectricworks',
 		            width:650,
 		            colspan:3,
 		            labelWidth:650,
 		            margin:'5 10 0 150',
 				},
 				{
 					xtype: 'checkboxfield',
 		            fieldLabel: 'Trabajos calientes. Se requiere DC3 e inspección de equipos',
 		            id: 'addworkhots',
 		            itemId: 'addworkhots',
 		            name:'addworkhots',
 		            width:650,
 		            labelWidth:650,
 		            colspan:3,
 		            margin:'5 10 0 150',
 				},
 				{
 					xtype: 'checkboxfield',
 		            fieldLabel: 'Ingreso de sustancias químicas. Hoja de seguridad, recipientes identificados e inflamables con recientes con arresta flama',
 		            id: 'addchemicalsubstances',
 		            itemId: 'addchemicalsubstances',
 		            name:'addchemicalsubstances',
 		            width:650,
 		            labelWidth:650,
 		            colspan:3,
 		            margin:'5 10 0 150',
 				}*/
                 	]
             },{
                 xtype: 'toolbar',
                 id:'checkworkers2',
                 style: {
                     background: 'white'
                   },
                 dock: 'top',
                 items: [
 				{
 					xtype: 'checkboxfield',
 		            fieldLabel: SuppAppMsg.plantAccess29,
 		            id: 'addconfinedspaces',
 		            itemId: 'addconfinedspaces',
 		            name:'addconfinedspaces',
 		            width:400,
 		            labelWidth:400,
 		            margin:'5 10 0 10',
 		           listeners: {
   		              change: function(checkbox, newValue, oldValue) {
   		            	 Ext.getCmp('addPlantAccessRequestEmployBtnAct').show();
   		                  // Lógica que se ejecuta cuando se produce el cambio
   		                  if (newValue) {
   		                	Ext.getCmp('toolDoc6').show();
							Ext.getCmp('toolDoc9').show();
							guardarWorkerAlter();
   		                  } else {
   		                	Ext.getCmp('toolDoc6').hide();
							Ext.getCmp('toolDoc9').hide();
							eliminarFileWork(Ext.getCmp('addemployeeId'),3,false);
   		                  }
   		              }
   		          }
 				},
 				{
 					xtype: 'checkboxfield',
 		            fieldLabel: SuppAppMsg.plantAccess30,
 		            id: 'addcontelectricworks',
 		            itemId: 'addcontelectricworks',
 		            name:'addcontelectricworks',
 		            width:500,
 		            labelWidth:500,
 		            margin:'5 10 0 75',
 		           listeners: {
    		              change: function(checkbox, newValue, oldValue) {
    		            	  Ext.getCmp('addPlantAccessRequestEmployBtnAct').show();
    		                  // Lógica que se ejecuta cuando se produce el cambio
    		                  if (newValue) {
    		                	  Ext.getCmp('toolDoc10').show();
    		                	  guardarWorkerAlter();
    		                  } else {
    		                	  Ext.getCmp('toolDoc10').hide();
    		                	  eliminarFileWork(Ext.getCmp('addemployeeId'),4,false);
    		                  }
    		              }
    		          }
 				},
 				/*{
 					xtype: 'checkboxfield',
 		            fieldLabel: 'Trabajos calientes. Se requiere DC3 e inspección de equipos',
 		            id: 'addworkhots',
 		            itemId: 'addworkhots',
 		            name:'addworkhots',
 		            width:650,
 		            labelWidth:650,
 		            colspan:3,
 		            margin:'5 10 0 150',
 				},
 				{
 					xtype: 'checkboxfield',
 		            fieldLabel: 'Ingreso de sustancias químicas. Hoja de seguridad, recipientes identificados e inflamables con recientes con arresta flama',
 		            id: 'addchemicalsubstances',
 		            itemId: 'addchemicalsubstances',
 		            name:'addchemicalsubstances',
 		            width:650,
 		            labelWidth:650,
 		            colspan:3,
 		            margin:'5 10 0 150',
 				}*/
                 	]
             },{
                 xtype: 'toolbar',
                 id:'checkworkers3',
                 style: {
                     background: 'white'
                   },
                 dock: 'top',
                 items: [
 				{
 					xtype: 'checkboxfield',
 		            fieldLabel: SuppAppMsg.plantAccess31,
 		            id: 'addworkhots',
 		            itemId: 'addworkhots',
 		            name:'addworkhots',
 		            width:400,
 		            labelWidth:400,
 		            margin:'5 10 0 10',
 		           listeners: {
 		              change: function(checkbox, newValue, oldValue) {
 		            	 Ext.getCmp('addPlantAccessRequestEmployBtnAct').show();
 		                  // Lógica que se ejecuta cuando se produce el cambio
 		                  if (newValue) {
 		                	 Ext.getCmp('toolDoc11').show();
 		                	guardarWorkerAlter();
 		                  } else {
 		                	 Ext.getCmp('toolDoc11').hide();
 		                	eliminarFileWork(Ext.getCmp('addemployeeId'),5,false);
 		                  }
 		              }
 		          }
 				},
 				{
 					xtype: 'checkboxfield',
 		            fieldLabel: SuppAppMsg.plantAccess32,
 		            id: 'addchemicalsubstances',
 		            itemId: 'addchemicalsubstances',
 		            name:'addchemicalsubstances',
 		            width:500,
 		            labelWidth:500,
 		            margin:'5 10 0 75',
 		           listeners: {
  		              change: function(checkbox, newValue, oldValue) {
  		            	 Ext.getCmp('addPlantAccessRequestEmployBtnAct').show();
  		                  // Lógica que se ejecuta cuando se produce el cambio
  		                  if (newValue) {
  		                	Ext.getCmp('toolDoc12').show();
  		                	guardarWorkerAlter();
  		                  } else {
  		                	Ext.getCmp('toolDoc12').hide();
  		                	eliminarFileWork(Ext.getCmp('addemployeeId'),6,false);
  		                  }
  		              }
  		          }
 				}
                 	]
             },{
                 xtype: 'toolbar',
                 id:'checkworkers4',
                 style: {
                     background: 'white'
                   },
                 dock: 'top',
                 items: [
 				{
 					xtype: 'checkboxfield',
 		            fieldLabel: SuppAppMsg.plantAccess52,
 		            id: 'activityFree',
 		            itemId: 'activityFree',
 		            name:'activityFree',
 		            width:400,
 		            labelWidth:400,
 		            margin:'5 10 0 10',
 		             listeners: {
  		              change: function(checkbox, newValue, oldValue) {
  		            	 Ext.getCmp('addPlantAccessRequestEmployBtnAct').show();
  		                  // Lógica que se ejecuta cuando se produce el cambio
  		                  if (newValue) {
  		                	Ext.getCmp('toolDoc13').show();
  		                	guardarWorkerAlter();
  		                  } else {
  		                	Ext.getCmp('toolDoc13').hide();
  		                	eliminarFileWork(Ext.getCmp('addemployeeId'),'*',false);
  		                  }
  		              }
  		          }
 				},{
         			iconCls : 'icon-add',
         			itemId : 'addPlantAccessRequestEmployBtnAct',
         			id : 'addPlantAccessRequestEmployBtnAct',
         			text : 'Continuar',//SuppAppMsg.plantAccess26,
         			action : 'addPlantAccessRequestEmployBtnAct',
         			width:520,
 		            labelWidth:400,
 		            margin:'5 10 0 75'
         		}]
             },
        	
             {
                 xtype: 'toolbar',
                 id:'titleUploadFileWorker',
                 name:'titleUploadFileWorker',
                 style: {
                     background: '#CCCCCC'
                   },
                 dock: 'top',
                 items: [{
 					xtype : 'displayfield',
 					value : 'Archivos a cargar del trabajador',
 					height:20,
 					margin:'5 10 0 300',
 					colspan:3,
 					fieldStyle: 'font-weight:bold'
 					}
                 	]
             },
        	
        	
//        	carga de documentos para trabajador

 			{
			     xtype: 'panel',
			     layout: {
			         type: 'vbox',
			         align: 'stretch'
			     },
			     id: 'bodyeaderFileWorker',
			     name:'bodyeaderFileWorker',
			     width: 800,
			     height: 100,
			     margin: '0 0 0 10',
			     dock: 'top',
			     items: [        
			    	 	{
			                 xtype: 'toolbar',
			                 id:'toolDoc1',
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc1',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	fieldLabel : SuppAppMsg.plantAccess35,
			     			        	name : 'text_CMA',
			     						id:'text_CMA',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_1',
			     							id : 'workerDOC_1',
			     							action : 'workerDOC_1',
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             },
			             {
			                 xtype: 'toolbar',
			                 id:'toolDoc2',
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	,{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc2',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	
			     			        	
			     			        	fieldLabel : SuppAppMsg.plantAccess36,
			     			        	name : 'text_CI',
			     						id:'text_CI',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_2',
			     							id : 'workerDOC_2',
			     							action : 'workerDOC_2',
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             },
			            /* {
			                 xtype: 'toolbar',
			                 id:'toolDoc3',
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	,{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc3',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	fieldLabel : SuppAppMsg.plantAccess37,
			     			        	name : 'text_DSCI',
			     						id:'text_DSCI',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_3',
			     							id : 'workerDOC_3',
			     							action : 'workerDOC_3',
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             }*/
			             ,
			             
			             /*{
			                 xtype: 'toolbar',
			                 id:'toolDoc4',
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	,{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc4',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	fieldLabel : SuppAppMsg.plantAccess38,
			     			        	name : 'text_IDEN',
			     						id:'text_IDEN',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_4',
			     							id : 'workerDOC_4',
			     							action : 'workerDOC_4',
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             },*/
			//             {
			//                 xtype: 'toolbar',
			//                 id:'toolDoc5',
			//                 style: {
			//                     background: 'white'
			//                   },
			//                 dock: 'top',
			//                 items: [
			//                 	,{
			//     					xtype: 'container',
			//     					layout: 'hbox',
			//     					id:'documentContainerWorkerDoc5',
			//     					colspan:3,
			//     					width:800,
			//     					margin:'0 0 0 10',
			//     					//hidden:role=='ANONYMOUS'?false:true,
			//     					defaults : {
			//     						labelWidth : 150,
			//     						xtype : 'textfield',
			//     						margin: '0 0 0 0'
			//     					},
			//     			        items:[{
			//     			        	xtype : 'textfield',
			//     			        	fieldLabel : SuppAppMsg.plantAccess39,
			//     			        	name : 'text_CCOVID',
			//     						id:'text_CCOVID',
			//     						width:600,
			//     						labelWidth:300,
			//     						readOnly:true,
			//     						margin: '0 0 0 0',
			//     						//allowBlank:false,
			//     						//allowBlank:role=='ANONYMOUS'?false:true
			//     					    },{
			//     							xtype: 'button',
			//     							width:100,
			//     							itemId : 'workerDOC_5',
			//     							id : 'workerDOC_5',
			//     							action : 'workerDOC_5',
			//     							text : SuppAppMsg.supplierLoad,
			//     						}]	
			//     			    
			//     			    }
			//                 	]
			//             },
			             //Documentos adicionales con check
			
			             {
			                 xtype: 'toolbar',
			                 id:'toolDoc6',
			                 hidden:true,
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	,{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc6',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	fieldLabel : SuppAppMsg.plantAccess40,
			     			        	name : 'text_CM1',
			     						id:'text_CM1',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_6',
			     							id : 'workerDOC_6',
			     							action : 'workerDOC_6',
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             },{
			                 xtype: 'toolbar',
			                 id:'toolDoc7',
			                 hidden:true,
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	,{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc7',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	fieldLabel : SuppAppMsg.plantAccess41,
			     			        	name : 'text_CD3TA',
			     						id:'text_CD3TA',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_7',
			     							id : 'workerDOC_7',
			     							action : 'workerDOC_7',
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             },{
			                 xtype: 'toolbar',
			                 id:'toolDoc8',
			                 hidden:true,
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	,{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc8',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	fieldLabel : SuppAppMsg.plantAccess42,
			     			        	name : 'text_CD3G',
			     						id:'text_CD3G',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_8',
			     							id : 'workerDOC_8',
			     							action : 'workerDOC_8',
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             },{
			                 xtype: 'toolbar',
			                 id:'toolDoc9',
			                 hidden:true,
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	,{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc9',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	fieldLabel : SuppAppMsg.plantAccess43,
			     			        	name : 'text_CD3TEC',
			     						id:'text_CD3TEC',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_9',
			     							id : 'workerDOC_9',
			     							action : 'workerDOC_9',
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             },{
			                 xtype: 'toolbar',
			                 id:'toolDoc10',
			                 hidden:true,
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	,{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc10',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	fieldLabel : SuppAppMsg.plantAccess44,
			     			        	name : 'text_CD3TE1',
			     						id:'text_CD3TE1',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_10',
			     							id : 'workerDOC_10',
			     							action : 'workerDOC_10',
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             },{
			                 xtype: 'toolbar',
			                 id:'toolDoc11',
			                 hidden:true,
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	,{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc11',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	fieldLabel : SuppAppMsg.plantAccess45,
			     			        	name : 'text_CD3TC',
			     						id:'text_CD3TC',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_11',
			     							id : 'workerDOC_11',
			     							action : 'workerDOC_11',
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             },{
			                 xtype: 'toolbar',
			                 id:'toolDoc12',
			                 hidden:true,
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	,{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc12',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	fieldLabel : SuppAppMsg.plantAccess46,
			     			        	name : 'text_HS',
			     						id:'text_HS',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_12',
			     							id : 'workerDOC_12',
			     							action : 'workerDOC_12',
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             },{
			                 xtype: 'toolbar',
			                 id:'toolDoc13',
			                 hidden:true,
			                 style: {
			                     background: 'white'
			                   },
			                 dock: 'top',
			                 items: [
			                 	,{
			     					xtype: 'container',
			     					layout: 'hbox',
			     					id:'documentContainerWorkerDoc13',
			     					colspan:3,
			     					width:800,
			     					margin:'0 0 0 10',
			     					//hidden:role=='ANONYMOUS'?false:true,
			     					defaults : {
			     						labelWidth : 150,
			     						xtype : 'textfield',
			     						margin: '0 0 0 0'
			     					},
			     			        items:[{
			     			        	xtype : 'textfield',
			     			        	fieldLabel : SuppAppMsg.plantAccess70,
			     			        	name : 'text_AE',
			     						id:'text_AE',
			     						width:600,
			     						labelWidth:300,
			     						readOnly:true,
			     						hidden: role == 'ROLE_PURCHASE_READ' ?true:false,
			     						margin: '0 0 0 0',
			     						//allowBlank:false,
			     						//allowBlank:role=='ANONYMOUS'?false:true
			     					    },{
			     							xtype: 'button',
			     							width:100,
			     							itemId : 'workerDOC_13',
			     							id : 'workerDOC_13',
			     							action : 'workerDOC_13',
			     							hidden: role == 'ROLE_PURCHASE_READ' ?true:false,
			     							text : SuppAppMsg.supplierLoad,
			     						}]	
			     			    
			     			    }
			                 	]
			             }
			 			
			 			
			
			         
			     ],
			     autoScroll: true
			 }

            ]
        
        this.tbar = [Ext.create(
				'Ext.form.Panel',
				{
					layout: {
	                type: 'hbox'
	            },
	            items : [{
	            	xtype: 'button',
					width:200,
					icon:'resources/images/doc.png',
					//hidden : role == 'ROLE_SUPPLIER'?true:false,
					text : 'Cargar Archivo',
					action : 'loadFileWorker',
					hidden:true,
					id : 'loadFileWorker',
					maring:'0 0 0 0'
	            }/*,{
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
//		listeners: {
//            'change': function(f, value){
//                  alert(f.size); // not filesize
//            }}
	}*/
//	,
//		{
//		 xtype: 'button',
//			itemId : 'faddPlantAccessRequestEmployBtn',
//			id : 'faddPlantAccessRequestEmployBtn',
//			text : 'Agregar',
//			action : 'addFilePlantAccessRequestBtnAct',
//			labelWidth : 70,
//			margin:'10 0 10 10'
//		}
	],
	/*buttons : [ {
		text : SuppAppMsg.supplierLoad,
		margin:'10 0 0 0',
		handler : function() {
			var form = this.up('form').getForm();
			if (form.isValid()) {
				form.submit({
							url : 'plantAccess/uploadFileWorker.action?idworker='+recordWorker.id,
							waitMsg : SuppAppMsg.supplierLoadFile,
							success : function(fp, o) {
								var res = Ext.decode(o.response.responseText);
//								
								storeFileWorker.add(res.data);
							},       // If you don't pass success:true, it will always go here
					        failure: function(fp, o) {
					        	var res = Ext.decode(o.response.responseText);
					        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
					        }
						});
			}
		}
	} ]*/
	            })
	]
        
        ;
              
        this.callParent(arguments);
        
        storeFileWorker.on('load', function() {
			
        	storeFileWorker.filter({
		        filterFn: function(rec) {
		        	if(rec.get('id') == recordWorker.id){
		        		return true
		        	}else{
		        		return false;
		        	}
		        }
		    });
		    
		});
        storeFileWorker.load();
        
    }
	
});