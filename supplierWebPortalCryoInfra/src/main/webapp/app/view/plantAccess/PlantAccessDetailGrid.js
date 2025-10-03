function sortByKey(array, key) {
    return array.sort(function(a, b) {
        var x = a[key]; var y = b[key];
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    });
};

function renderTip(val, meta, rec, rowIndex, colIndex, store) {
	var comments = "";
	var firstLineNotes ="";
	if(rec.data.purchaseOrderNotes){
		sortByKey(rec.data.purchaseOrderNotes, "instruction");
		for(var i = 0; i < rec.data.purchaseOrderNotes.length; i++){
			if(i == 0){
				firstLineNotes = rec.data.purchaseOrderNotes[i].lineText + "...";
			}
			comments = comments + " " + rec.data.purchaseOrderNotes[i].lineText;
			comments = comments.replace(/"/g, '');
		}

	}
	
	if(comments != ""){
	    meta.tdAttr = 'data-qtip="' + comments + '"';
	    return firstLineNotes;
	}else{
		return "";
	}

};
Ext.QuickTips.init();

var store = Ext.create('Ext.data.Store', {
	model: 'SupplierApp.model.PlantAccessDetail',
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


Ext.define('SupplierApp.view.plantAccess.PlantAccessDetailGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.plantAccessDetailGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',  
	store:store,
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
        this.columns = [{
						    text     :SuppAppMsg.plantAccess23,
						    width: "25%",
						    dataIndex: 'employeeName'
						},{
						    text     : SuppAppMsg.plantAccess24,
						    width: '15%',
						    dataIndex: 'membershipIMSS'
						},{
						    text     : SuppAppMsg.plantAccess25,
						    width: '20%',
						    dataIndex: 'datefolioIDcard'
						},{
						    text     : SuppAppMsg.plantAccess33,
						    width: '15%',
						    dataIndex: 'activities'
						}/*,{
							xtype: 'button',
							width:200,
							icon:'resources/images/doc.png',
							//hidden : role == 'ROLE_SUPPLIER'?true:false,
							text : 'Cargar Archivos de Solicitud',
							action : 'openLoadFileWorker',
							id : 'openLoadFileWorker',
							maring:'0 0 0 0',
							items: [
				            	{
				            	  icon:'resources/images/doc.png',
				                  handler: function(grid, rowIndex, colIndex) {
				                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
				                  }
				            	}]
						}*//*,{
				        	xtype: 'actioncolumn', 
				            width: 90,
				            header: 'Cargar Documentos',
				            align: 'center',
							name : 'openLoadFileWorker',
							itemId : 'openLoadFileWorker',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	  icon:'resources/images/doc.png',
				              	      text: SuppAppMsg.approvalApprove,
				                  handler: function(grid, rowIndex, colIndex) {
				                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
				                  }
				            	}]
				        }*/
						,{
				        	xtype: 'actioncolumn', 
				            width: '15%',
				            header: SuppAppMsg.taxvaultUploandDocuments,
				            align: 'center',
							name : 'viewXMLFDA',
							//hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
							hidden: false,
							itemId : 'upfileWork',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/doc.png',
			             	      text: SuppAppMsg.freightApprovalTitle9,
				                  handler: function(grid, rowIndex, colIndex) {
				                	  
				                	  Ext.getCmp('AgrTabNuevo').hide();
				                	  Ext.getCmp('cancelarAgrTab').show();
				                	  Ext.getCmp('bodyeaderFileWorker').show();
				                	  
				                	  this.fireEvent('buttonclick', grid, rowIndex, colIndex)
				                	  
				                	  var recordWorker = grid.store.getAt(rowIndex).data;
				                  	  uuidPlantAccessWorker = recordWorker.id;
				                  	Ext.getCmp('addemployeeId').setValue(recordWorker.id);
											
											var controller = _AppGlobSupplierApp.getController('PlantAccess');
											
											var grid = controller.getPlantAccessWorkerFileGrid();
											
											var formHeader =  controller.getPlantAccessForm();
											 var griDformulario=controller.getPlantAccessDetailGrid();
											 var upfile=controller.getPlantAccessWorkerFileGrid();
											
											 
											 
											 estatusRequestPlan= formHeader.down('#estatusPlantAccess').getValue();
											 
											   Ext.getCmp('containerlistaWorkers').hide()
						                      Ext.getCmp('containerUpfile').show();

											  if(estatusRequestPlan!='APROBADO'&&estatusRequestPlan!='PENDIENTE'){
												  	Ext.getCmp('formWorkAccessPlantTitle').show();
												  		Ext.getCmp('formWorkAccessPlantBody').show();
												  		Ext.getCmp('formWorkAccessPlantBody2').show();
												  		
   	    
											  }else{
												  Ext.getCmp('formWorkAccessPlantTitle').hide();
												  Ext.getCmp('formWorkAccessPlantBody').hide();
												  Ext.getCmp('formWorkAccessPlantBody2').hide();
												  Ext.getCmp('bodyeaderFileWorker').hide();
												  Ext.getCmp('titleUploadFileWorker').hide();
											  }
											 
											 
											 
											 griDformulario.hide();
											formHeader.hide();
						                      upfile.show();
						                      
						                    
						                      
									    	var store = grid.getStore();
									    	debugger
											Ext.Ajax.request({
											    url: 'plantAccess/searchFilesPlantAccess.action',
											    method: 'POST',
											    params: {
											    	uuid:uuidPlantAccessWorker
										        },
											    success: function(fp, o) {
											    	var res = Ext.decode(fp.responseText);
													var rec =Ext.create('SupplierApp.model.PlantAccessDetail',res.data);
													store.removeAll();
													store.add(rec.raw);
											    	grid.getView().refresh();
											    	
											    	/*if(checkPlantAccess){
											    		//grid.columns[3].setVisible(false);
											    		//Ext.getCmp('loadFileWorker').hide();
											    		
											    		Ext.getCmp('toolDoc1').hide();
											    		Ext.getCmp('toolDoc2').hide();
											    		Ext.getCmp('toolDoc3').hide();
											    		Ext.getCmp('toolDoc4').hide();
											    		Ext.getCmp('toolDoc5').hide();
											    		Ext.getCmp('toolDoc6').hide();
											    		Ext.getCmp('toolDoc7').hide();
											    		Ext.getCmp('toolDoc8').hide();
											    		Ext.getCmp('toolDoc9').hide();
											    		Ext.getCmp('toolDoc10').hide();
											    		Ext.getCmp('toolDoc11').hide();
											    		Ext.getCmp('toolDoc12').hide();
											    		//Ext.getCmp('toolDoc13').hide();
											    	}*/
											    	
											    	if(checkPlantAccess &&
											    		role != 'ROLE_SUPPLIER'&&
											    		statusPlantAccess == 'PENDIENTE'){
											    		grid.columns[3].setVisible(true);
											    		//Ext.getCmp('loadFile').hide();
												    }
											    	
											    	if(checkPlantAccess &&
											    		role != 'ROLE_SUPPLIER'&&
											    		statusPlantAccess == 'RECHAZADO'){
												    		Ext.getCmp('toolDoc1').hide();
												    		Ext.getCmp('toolDoc2').hide();
//												    		Ext.getCmp('toolDoc3').hide();
//												    		Ext.getCmp('toolDoc4').hide();
//												    		Ext.getCmp('toolDoc5').hide();
												    		Ext.getCmp('toolDoc6').hide();
												    		Ext.getCmp('toolDoc7').hide();
												    		Ext.getCmp('toolDoc8').hide();
												    		Ext.getCmp('toolDoc9').hide();
												    		Ext.getCmp('toolDoc10').hide();
												    		Ext.getCmp('toolDoc11').hide();
												    		Ext.getCmp('toolDoc12').hide();
												    		Ext.getCmp('addPlantAccessRequestEmployBtnAct').hide();
													    }
											    	
											    	if(checkPlantAccess &&
												    	(statusPlantAccess == 'PENDIENTE' 
												    	|| statusPlantAccess == 'APROBADO')){
											    		Ext.getCmp('toolDoc1').hide();
											    		Ext.getCmp('toolDoc2').hide();
//											    		Ext.getCmp('toolDoc3').hide();
//											    		Ext.getCmp('toolDoc4').hide();
//											    		Ext.getCmp('toolDoc5').hide();
											    		Ext.getCmp('toolDoc6').hide();
											    		Ext.getCmp('toolDoc7').hide();
											    		Ext.getCmp('toolDoc8').hide();
											    		Ext.getCmp('toolDoc9').hide();
											    		Ext.getCmp('toolDoc10').hide();
											    		Ext.getCmp('toolDoc11').hide();
											    		Ext.getCmp('toolDoc12').hide();
											    	}
											    	
											    	/*if(checkPlantAccess &&
											    		role == 'ROLE_SUPPLIER'&&
											    		statusPlantAccess == 'RECHAZADO'){
												    		Ext.getCmp('toolDoc1').show();
												    		Ext.getCmp('toolDoc2').show();
												    		Ext.getCmp('toolDoc3').show();
												    		Ext.getCmp('toolDoc4').show();
												    		Ext.getCmp('toolDoc5').show();
												    		/*Ext.getCmp('toolDoc6').show();
												    		Ext.getCmp('toolDoc7').show();
												    		Ext.getCmp('toolDoc8').show();
												    		Ext.getCmp('toolDoc9').show();
												    		Ext.getCmp('toolDoc10').show();
												    		Ext.getCmp('toolDoc11').show();
												    		Ext.getCmp('toolDoc12').show();
												    		Ext.getCmp('toolDoc13').show();
												    	}*/
											    	
											    },
											    failure: function() {
											    	Ext.MessageBox.show({
										                title: 'Error',
										                msg: SuppAppMsg.approvalUpdateError,
										                buttons: Ext.Msg.OK
										            });
											    }
											});
											
											var activities = recordWorker.activities;
											
											
											
											
											
											
											
											Ext.getCmp('addworkatheights').setValue(false);
											Ext.getCmp('addHeavyequipmentWorker').setValue(false);
											Ext.getCmp('addconfinedspaces').setValue(false);
											Ext.getCmp('addcontelectricworks').setValue(false);
											Ext.getCmp('addworkhots').setValue(false);
											Ext.getCmp('addchemicalsubstances').setValue(false);
											Ext.getCmp('activityFree').setValue(false);
											
											Ext.getCmp('addemployeename').setValue(recordWorker.employeeName.split(' ')[0]);
											Ext.getCmp('addemployeenappat').setValue(recordWorker.employeeName.split(' ')[1]);
											Ext.getCmp('addemployeenapmat').setValue(recordWorker.employeeName.split(' ')[2]);
											Ext.getCmp('addmembershipIMSS').setValue(recordWorker.membershipIMSS);
											Ext.getCmp('addDatefolioIDcard').setValue(recordWorker.datefolioIDcard.split('/')[0]);
											Ext.getCmp('addfolioIDcard').setValue(recordWorker.datefolioIDcard.split('/')[1]);
											
											
											Ext.getCmp('addemployeename').setReadOnly(true); 
											Ext.getCmp('addemployeenappat').setReadOnly(true); 
											Ext.getCmp('addemployeenapmat').setReadOnly(true); ;
											Ext.getCmp('addmembershipIMSS').setReadOnly(true); 
											Ext.getCmp('addDatefolioIDcard').setReadOnly(true); 
											Ext.getCmp('addfolioIDcard').setReadOnly(true); 
											
											
											Ext.getCmp('addPlantAccessRequestEmployBtnAct').hide();
											if(activities.includes('1')){
												Ext.getCmp('addworkatheights').setValue(true);
												Ext.getCmp('toolDoc6').show();
												Ext.getCmp('toolDoc7').show();
											}
											if(activities.includes('2')){
												Ext.getCmp('toolDoc8').show();
												Ext.getCmp('addHeavyequipmentWorker').setValue(true);
											}
											if(activities.includes('3')){
												Ext.getCmp('toolDoc6').show();
												Ext.getCmp('toolDoc9').show();
												Ext.getCmp('addconfinedspaces').setValue(true);
											}
											if(activities.includes('4')){
												Ext.getCmp('toolDoc10').show();
												Ext.getCmp('addcontelectricworks').setValue(true);
											}
											if(activities.includes('5')){
												Ext.getCmp('toolDoc11').show();
												Ext.getCmp('addworkhots').setValue(true);
											}
											if(activities.includes('6')){
												Ext.getCmp('toolDoc12').show();
												Ext.getCmp('addchemicalsubstances').setValue(true);
											}
											if(activities.includes('*')){
												Ext.getCmp('toolDoc13').show();
												Ext.getCmp('activityFree').setValue(true);
											}

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
												if(recordWorker.listDocuments.includes('WORKER_AE')) Ext.getCmp('toolDoc13').hide();
											}*/
											
											//Ext.getCmp('toolDoc12').show();
				                	  
				                  }}]
						
						},{
				        	xtype: 'actioncolumn', 
				            //width: 90,
				            width: '15%',
				            header: SuppAppMsg.taxvaultDelete,
				            align: 'center',
							name : 'deleteWorker',
//							hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
							itemId : 'deleteWorker',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/close.png',
				              	 /* getClass: function(v, metadata, r, rowIndex, colIndex, store) {
				              		if(r.data.documentStatus != "COMPLETED" ){
				              			return "x-hide-display";
				              		}
				              		   if(
	              	        		  !(role=='ROLE_BF_ADMIN')) {
	                      	        	  
	                      	              return "x-hide-display";
	                      	          }
				              	      },
				              	      text: SuppAppMsg.approvalReject,*/
				                  handler: function(grid, rowIndex, colIndex) {
				                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
				                  }}]	
						
						}
						/*,{
				        	xtype: 'actioncolumn', 
				            width: '10%',
				            header: 'Quitar',
				            align: 'center',
							name : 'quitEmploye',
							//hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
							hidden: false,
							itemId : 'quitEmploye',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/delete.png',
			             	      text: SuppAppMsg.freightApprovalTitle9,
				                  handler: function(grid, rowIndex, colIndex) {
				                 // this.fireEvent('buttonclick', grid, rowIndex, colIndex);
				                	  var record = grid.store.getAt(rowIndex);
				                	  //var href = "documents/openDocument.action?id=" + 46;
										var href = "documents/openDocumentByUuid.action?orderNumber=0&type=xml&uuidFactura=" + record.data.uuidFactura;
										var newWindow = window.open(href, '_blank');
										//var newWindow = window.open(newdata, "_blank");
										setTimeout(function(){ newWindow.document.title = 'Factura XML'; }, 10);
				                  }}]
						
						}*/];
        
        /// carga de usuario antes de agregar a la lista (borrado)
        this.tbar = [
        	'->', 
        	{
				xtype : 'displayfield',
				value : SuppAppMsg.plantAccess33+"<br>"
						+SuppAppMsg.plantAccess54+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+SuppAppMsg.plantAccess55+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+SuppAppMsg.plantAccess56+"<br>"
						+SuppAppMsg.plantAccess57+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+SuppAppMsg.plantAccess58+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+SuppAppMsg.plantAccess59+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+SuppAppMsg.plantAccess52,
				height:55,
				id:'listActivities',
				hidden:true,
				//margin:'5 10 0 300',
				//colspan:3,
				fieldStyle: 'font-weight:bold;font-size: 11px;'
				}
			];
              
        this.callParent(arguments);
    }
	
});