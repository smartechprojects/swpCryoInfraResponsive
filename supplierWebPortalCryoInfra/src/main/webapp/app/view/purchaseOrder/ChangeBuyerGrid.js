	Ext.define('SupplierApp.view.purchaseOrder.ChangeBuyerGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.changeBuyerGrid',
    loadMask: true,
	border:false,
	selModel: {
        checkOnly: true,
        mode: 'SIMPLE'
    },
    forceFit: true,
    selType: 'checkboxmodel',
	cls: 'extra-large-cell-grid',  
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
    	
    	var poController = SupplierApp.app.getController("SupplierApp.controller.PurchaseOrder");
	 
		this.store =  storeAndModelFactory('SupplierApp.model.PurchaseOrder',
                'orderModel',
                'supplier/orders/searchOrdersByBuyer.action', 
                false,
                {
			     buyerEmail:''
                },
			    "", 
			    null);
		
		var buyerStore =  storeAndModelFactory('SupplierApp.model.Users',
                'usersModel',
                'admin/users/searchByRoleExclude.action', 
                false,
                {
			     role:'ROLE_SUPPLIER'
                },
			    "", 
			    500);
		
    	var fromBuyerCombo = new Ext.form.ComboBox({
    	    fieldLabel: SuppAppMsg.purchaseTitle1,
    	    store: buyerStore,
    	    displayField: 'email',
    	    labelWidth:100,
    	    valueField: 'id',
    	    margin:'20 20 0 10',
    	    id:'fromBuyerCombo',
    	    forceSelection: true,
    	    typeAhead: true,
    	    triggerAction : 'all',
            minChars: 2,
            emptyText : SuppAppMsg.purchaseTitle2,
    	    width:320
    	});
    	
    	var toBuyerCombo = new Ext.form.ComboBox({
    	    fieldLabel: SuppAppMsg.purchaseTitle3,
    	    store: buyerStore,
    	    displayField: 'email',
    	    labelWidth:100,
    	    valueField: 'id',
    	    margin:'20 20 0 10',
    	    id:'toBuyerCombo',
    	    forceSelection: true,
    	    typeAhead: true,
    	    triggerAction : 'all',
            minChars: 2,
            emptyText : SuppAppMsg.purchaseTitle4,
    	    width:320
    	});

 
        this.columns = [
        	{
              hidden:true,
              dataIndex: 'id'
            }, {
            text     : SuppAppMsg.purchaseOrderNumber,
            width: 120,
            dataIndex: 'orderNumber'
	        },{
	            text     : SuppAppMsg.purchaseTitle5,
	            width: 100,
	            dataIndex: 'orderType'
	        },{
	            text     : SuppAppMsg.purchaseOrderSupplier,
	            width: 280,
	            dataIndex: 'description'
	        },{
	            text     : 'Status',
	            width: 140,
	            dataIndex: 'orderStauts'
	        },{
	            text     : SuppAppMsg.purchaseTitle6 ,
	            width: 200,
	            dataIndex: 'email'
	        }];
        
        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: {
                    background: 'white'
                  },
                dock: 'top',
                items: [
                	    fromBuyerCombo,
                	    {
    		           		xtype:'button',
    			            text: SuppAppMsg.suppliersSearch,
    			            iconCls: 'icon-search',
    			            action:'fromBuyerSearch',
    			            cls: 'buttonStyle',
    			            margin:'10 20 10 10',
    						listeners: {
    	   	                    tap: function (button) {
    	   	                    	poController.fromBuyerSearch(button);
    	   	                    }
    	   	                }
    					},
                	    {
    		           		xtype:'displayfield',
    			            text: SuppAppMsg.purchaseTitle7,
    			            margin:'10 20 10 10'
    					},
                	    toBuyerCombo,{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseTitle8,
			            iconCls: 'icon-appgo',
			            action:'toBuyerExecute',
			            cls: 'buttonStyle',
			            margin:'10 20 10 10',
						listeners: {
	   	                    tap: function (button) {
	   	                    	poController.toBuyerExecute(button);
	   	                    }
	   	                }
					}
                ]
            }
        ];
        
      
        this.callParent(arguments);
    }
});