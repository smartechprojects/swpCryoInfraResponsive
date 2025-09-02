Ext.define('SupplierApp.view.supplier.NonComplianceSupplierGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.nonComplianceSupplierGrid',
    forceFit: true,
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid', 
	scroll : false,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
    	
    	var ncsController = SupplierApp.app.getController("SupplierApp.controller.NonComplianceSupplier");
    	
		this.store =  storeAndModelFactory('SupplierApp.model.NonComplianceSupplier',
                'supplierModel',
                'noncompliancesupplier/searchByCriteria.action', 
                true,
                {
			        query : ''
                },
			    "", 
			    11);
 
        this.columns = [
           {
            text     : 'RFC',
            width: 120,
            dataIndex: 'taxId'
        },{
            text     : SuppAppMsg.suppliersNameSupplier,
            width: 420,
            dataIndex: 'supplierName'
        },{
            text     : SuppAppMsg.complianceSituation,
            width: 400,
            dataIndex: 'status'
        },{
            text     : SuppAppMsg.compliancepostSAT,
            width: 450,
            dataIndex: 'refDate2'
        }];
        
       
        this.dockedItems = [
	          {
	            xtype: 'toolbar',
	            dock: 'top',
	            items: [{
	    			xtype: 'textfield',
	                fieldLabel:SuppAppMsg.complianceSearchText,
	                id: 'nonComplianceSearch',
	                itemId: 'nonComplianceSearch',
	                name:'nonComplianceSearch',
	                width:400,
	                labelAlign:'top',
	                margin:'5 0 0 0'
	    		}
	            ]},
	           {
	              xtype: 'toolbar',
	              dock: 'top',
	              items: [{
	             		xtype:'button',
	                    text: SuppAppMsg.suppliersSearch,
	                    iconCls: 'icon-doSearch',
	                    action:'nonComplianceSearchBtn',
	                    margin:'0 0 10 0',
		                cls: 'buttonStyle',
		                listeners: {
		                    tap: function (button) {
		                    	ncsController.nonComplianceSearchBtn(button);
		                    }
		                }
	        		}
	            ]},
	            getPagingContent()
	    ];
      
        this.callParent(arguments);
    }
});