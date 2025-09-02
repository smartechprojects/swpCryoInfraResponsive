Ext.define('SupplierApp.view.plantAccess.PlantAccessWorkerGrid',	{
	extend : 'Ext.grid.Panel',
	alias : 'widget.plantAccessWorkerGrid',
	border : false,
	frame : false,
	store: 'PlantAccessFile',
	style : 'border: solid #ccc 1px',
	//autoScroll : true,
	initComponent : function() {
		
		this.columns = [{
		    text     :SuppAppMsg.taxvaultDocument,
		    width: "30%",
		    dataIndex: 'originName'
		},{
		    text     : SuppAppMsg.fiscalTitle21,
		    width: '40%',
		    dataIndex: 'documentType',
            renderer: function(value, meta, record) {            	
            	var status = {
            			WORKER_CMA: 'WORKER_CMA',
            			WORKER_CI: 'WORKER_CI',
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
            width: '15%',
            header: SuppAppMsg.plantAccess34,
            align: 'center',
			name : 'openDocumentFileWorker',
			itemId : 'openDocumentFileWorker',
            style: 'text-align:center;',
            items: [{
            	iconCls:'icon-document',
            	text: SuppAppMsg.freightApprovalTitle9,
            	handler: function(grid, rowIndex, colIndex) {
            		var record = grid.store.getAt(rowIndex);
            		var href = "plantAccess/openDocumentPlantAccess.action?id=" + record.data.id;
            		window.open(href);
            	}
            }]
		},{
        	xtype: 'actioncolumn', 
        	width: '15%',
            header: 'Eliminar documento',
            align: 'center',
            hidden:false,
            dataIndex : 'paDeleteWorkerFile',
			name : 'paDeleteWorkerFile',
			id : 'paDeleteWorkerFile',
			itemId : 'paDeleteWorkerFile',
            style: 'text-align:center;',
            items: [{
            	icon:'resources/images/close.png',
            	handler: function(grid, rowIndex, colIndex) {
            		this.fireEvent('buttonclick', grid, rowIndex, colIndex);
            	}
            }]			
		}]
		
		this.callParent(arguments);
	}
});