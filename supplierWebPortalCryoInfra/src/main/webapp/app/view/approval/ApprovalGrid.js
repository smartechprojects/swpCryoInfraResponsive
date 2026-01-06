Ext.define('SupplierApp.view.approval.ApprovalGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.approvalGrid',
    id: 'idApprovalGrid',
    loadMask: true,
	frame:false,
	border:false,
	title: SuppAppMsg.approvalTitle,
	cls: 'extra-large-cell-grid', 
	scroll :  true,
    dockedItems: [
    	getPagingContent()
    ],
    viewConfig: {
	    stripeRows: true,
	    style : { overflow: 'auto', overflowX: 'hidden' },
	    enableTextSelection: true,
	    markDirty: false,
	    listeners: {
	        refresh: function(view) {
	            var grid = view.up('grid');
	            if (!grid) return;
	            // Usar la función centralizada
	            GridUtils.adjustGridLayout(grid, true);
	        },
	        
	        resize: function(view) {
	            var grid = view.up('grid');
	            if (!grid) return;
	            // Usar la función centralizada
	            GridUtils.adjustGridLayout(grid, false);
	        }
	    }
	},
    initComponent: function() {
    	this.emptyText = SuppAppMsg.emptyMsg;
    	var approvalController = SupplierApp.app.getController("SupplierApp.controller.Approval");
    	
		this.store =  storeAndModelFactory('SupplierApp.model.SupplierDTO',
                'approvalModel',
                'approval/view.action', 
                true,
                {
                	currentApprover:userName,
                    status:'',
                    step:'',
                    notes:''
                },
			    "", 
			    12);
		
		this.store.getProxy().actionMethods = { read: 'POST' };
 
        this.columns = [
           {
            text     : SuppAppMsg.suppliersNameSupplier,
            //width: 400,
            flex: 2,
            dataIndex: 'razonSocial'
        },{
            text     : SuppAppMsg.approvalTicket,
            //width: 275,
            flex: 1,
            dataIndex: 'ticketId'
        },{
            text     : SuppAppMsg.approvalNextApprover,
            //width: 200,
            flex: 1,
            dataIndex: 'nextApprover'
        },{
            text     : 'Stauts',
            //width: 90,
            flex: 0.7,
            dataIndex: 'approvalStatus'
        },{
            text     : SuppAppMsg.approvalLevel,
            //width: 120,
            flex: 0.8,
            dataIndex: 'approvalStep'
        },{
        	xtype: 'actioncolumn', 
            //width: 90,
        	flex: 0.8,
            header: SuppAppMsg.approvalApprove,
            align: 'center',
			name : 'approveSupplier',
			itemId : 'approveSupplier',
            style: 'text-align:center;',
            items: [{
                text: SuppAppMsg.approvalApprove,
                iconCls:'icon-accept',
                handler: function(grid, rowIndex, colIndex) {
                	//this.fireEvent('buttonclick', grid, rowIndex, colIndex);
                	 // var controller = grid.up('panel').getController();
                     // controller.approveSupplier(grid, rowIndex, colIndex);
                      approvalController.approveSupplier(grid, rowIndex, colIndex)
                }
            }]
        },{
        	xtype: 'actioncolumn', 
            //width: 90,
        	flex: 0.8,
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