Ext.define('SupplierApp.controller.NonComplianceSupplier', {
    extend: 'Ext.app.Controller',
    stores: [],
    models: ['NonComplianceSupplier'],
    views: ['supplier.NonComplianceSupplierPanel','supplier.NonComplianceSupplierGrid'],
    refs: [{
        	ref: 'nonComplianceSupplierGrid',
        	selector: 'nonComplianceSupplierGrid'
	    }],
 
    init: function() {
        this.control({
			'nonComplianceSupplierGrid button[action=nonComplianceSearchBtn]' : {
				click : this.nonComplianceSearchBtn
			}
        });
    },
    
    nonComplianceSearchBtn: function(button) {
    	var grid = this.getNonComplianceSupplierGrid();
    	var store = grid.getStore();
    	var str = Ext.getCmp('nonComplianceSearch').getValue();
    	if(str){
    		if(str != ""){
    			store.proxy.extraParams = { 
		    			query : str
    			        }
    	    	store.load();
    		}
    	}
    	

  
    }
});