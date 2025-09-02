Ext.define('SupplierApp.view.approval.ApprovalGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.approvalGrid',
    forceFit: true,
    loadMask: true,
	frame:false,
	border:false,
	title: SuppAppMsg.approvalTitle,
	cls: 'extra-large-cell-grid', 
	scroll : false,
    dockedItems: [
    	getPagingContent()
    ],
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
    	
		this.store =  storeAndModelFactory('SupplierApp.model.SupplierDTO',
                'approvalModel',
                'approval/view.action', 
                true,
                {
                	currentApprover:addressNumber,
                    status:'',
                    step:'',
                    notes:''
                },
			    "", 
			    100);
 
        this.columns = [
           {
            text     : SuppAppMsg.suppliersNameSupplier,
            width: 400,
            dataIndex: 'razonSocial'
        },{
            text     : SuppAppMsg.approvalTicket,
            width: 275,
            dataIndex: 'ticketId'
        },{
            text     : SuppAppMsg.approvalNextApprover,
            width: 200,
            dataIndex: 'nextApprover'
        },{
            text     : 'Stauts',
            width: 90,
            dataIndex: 'approvalStatus'
        },{
            text     : SuppAppMsg.approvalLevel,
            width: 120,
            dataIndex: 'approvalStep'
        },{
        	xtype: 'actioncolumn', 
            width: 90,
            header: SuppAppMsg.approvalApprove,
            align: 'center',
			name : 'approveSupplier',
			itemId : 'approveSupplier',
            style: 'text-align:center;',
            items: [{
                text: SuppAppMsg.approvalApprove,
                iconCls:'icon-accept',
                handler: function(grid, rowIndex, colIndex) {
                	this.fireEvent('buttonclick', grid, rowIndex, colIndex);
                }
            }]
        },{
        	xtype: 'actioncolumn', 
            width: 90,
            header: SuppAppMsg.approvalReject,
            align: 'center',
			name : 'rejectSupplier',
			itemId : 'rejectSupplier',
            style: 'text-align:center;',
            items: [{
                text: 'RECHAZAR',
                iconCls:'icon-delete',
                handler: function(grid, rowIndex, colIndex) {
                	this.fireEvent('buttonclick', grid, rowIndex, colIndex);
                }
            }]
        }];

      
        this.callParent(arguments);
    }
});