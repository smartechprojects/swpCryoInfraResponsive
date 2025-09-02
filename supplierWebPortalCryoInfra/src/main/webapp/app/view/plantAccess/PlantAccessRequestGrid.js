Ext.define('SupplierApp.view.plantAccess.PlantAccessRequestGrid',	{
	extend : 'Ext.grid.Panel',
	alias : 'widget.plantAccessRequestGrid',
	border : false,
	frame : false,
	store: 'PlantAccessWorker',
	style : 'border: solid #ccc 1px',
	//autoScroll : true,
	initComponent : function() {
		
		this.columns = [{
			text     :SuppAppMsg.plantAccess23,
		    width: "29%",
		    dataIndex: 'employeeName',
			renderer: function(value, metaData, record, row, col, store, gridView){
				return record.data.employeeName + ' ' + record.data.employeeLastName + ' ' + record.data.employeeSecondLastName;
			}
		},{
		    text     : SuppAppMsg.plantAccess24,
		    width: '12%',
		    dataIndex: 'membershipIMSS'
		},{
		    text     : SuppAppMsg.plantAccess25,
		    width: '20%',
		    dataIndex: 'datefolioIDcard'
		},{
		    text     : SuppAppMsg.plantAccess33,
		    width: '8%',
		    dataIndex: 'activities'
		},{
        	xtype: 'actioncolumn', 
            width: '15%',
            header: SuppAppMsg.taxvaultUploandDocuments,
            align: 'center',
            dataIndex : 'paEditRequestWorker',
			name : 'paEditRequestWorker',
			id : 'paEditRequestWorker',
			itemId : 'paEditRequestWorker',
            style: 'text-align:center;',
            items: [{
            	icon:'resources/images/doc.png',
            	text: SuppAppMsg.freightApprovalTitle9,
                handler: function(grid, rowIndex, colIndex) {
                	this.fireEvent('buttonclick', grid, rowIndex, colIndex);
                }
            }]
		},{
        	xtype: 'actioncolumn',
            width: '15%',
            header: SuppAppMsg.taxvaultDelete,
            align: 'center',
            dataIndex : 'paDeleteWorker',
			name : 'paDeleteWorker',
			id : 'paDeleteWorker',
			itemId : 'paDeleteWorker',
            style: 'text-align:center;',
            items: [{
            	icon:'resources/images/close.png',
            	handler: function(grid, rowIndex, colIndex) {
            		this.fireEvent('buttonclick', grid, rowIndex, colIndex);
            	}
            }]
		}];
		
		this.viewConfig = {
	            getRowClass: function(record) {
	                return record.get('isDuplicate') ? 'duplicate-row' : '';
	            }
	        };
		
        this.tbar = [
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
				height:45,
				id:'listActivities',
				hidden:true,
				margin:'5 10 0 300',
				colspan:3,
				fieldStyle: 'font-weight:bold'
				}
			];
		
		this.callParent(arguments);
	}
});