Ext.define('SupplierApp.controller.Token', {
    extend: 'Ext.app.Controller',
    stores: ['AccessTokenRegister'],
    models: ['AccessTokenRegister'],
    views: ['token.TokenGrid','token.TokenForm','token.TokenPanel'],
    refs: [{
        	ref: 'tokenForm',
        	selector: 'tokenForm'
	    },
	    {
	        ref: 'tokenGrid',
	        selector: 'tokenGrid'
	    }],
 
    init: function() {
        this.control({
            'tokenGrid': {
                selectionchange: this.gridSelectionChange
            },
            'tokenForm button[action=save]': {
                click: this.saveToken
            },
            'tokenForm button[action=new]': {
                click: this.resetTokenForm
            },
            'tokenForm button[action=update]': {
                click: this.updateToken
            },
            '#searchAccessToken' : {
                "ontriggerclick": this.loadSearchList
            }
        });
    },
    
    loadSearchList: function (event){
    	this.getTokenGrid().getStore().getProxy().extraParams={
    		query:event.getValue()
    	};
    	this.getTokenGrid().getStore().load();
    },
    
    gridSelectionChange: function(model, records) {
        if (records[0]) {
        	var form = this.getTokenForm().getForm();
        	form.loadRecord(records[0]);
            this.enableUpdate();
        }
    },
    
    saveToken: function (button) {
    	var form = this.getTokenForm().getForm();
    	var grid = this.getTokenGrid();
    	if (form.isValid()) { 
        	record = Ext.create('SupplierApp.model.AccessTokenRegister');
            values = form.getFieldValues();
            updatedRecord = populateObj(record, values);
            if (values.id > 0){
            	Ext.Msg.alert(SuppAppMsg.usersSaveError, "Error");
    		} else{
    			record.set(updatedRecord);
    			record.save(
    					{
    					    callback: function (records, o, success) { 
    					    	if(success){
    				        	    grid.store.load();
    					    	}else{
    					    	}
    					    }
    			});
        	    form.reset();
        	    this.enableSave();
    		}
    	}
    },
    
    updateToken: function (button) {
    	var form = this.getTokenForm().getForm();
    	var grid = this.getTokenGrid();
    	if (form.isValid()) { 
        	record = Ext.create('SupplierApp.model.AccessTokenRegister');
            values = form.getFieldValues();
            updatedRecord = populateObj(record, values);
            if (values.id == 0){
            	Ext.Msg.alert(SuppAppMsg.usersSaveError, "Error");
    		} else{
    			record.set(updatedRecord);
    			record.save(
    					{
    					    callback: function (records, o, success) { 
    					    	if(success){
    				        	    grid.store.load();
    					    	}else{
    					    	}
    					    }
    			});
        	    form.reset();
        	    this.enableSave();
    		}
    	}
    },
    
    resetTokenForm: function (button) {
    	var form = this.getTokenForm().getForm();
    	form.reset();
    	this.enableSave();
    },
    
	enableUpdate: function(){
		var form = this.getTokenForm().getForm();
		Ext.getCmp('saveTokenRegisterId').setDisabled(true);
		Ext.getCmp('updateTokenRegisterId').setDisabled(false);
		form.findField('registerName').setReadOnly(true);

	},

	enableSave: function(){
		var form = this.getTokenForm().getForm();
		Ext.getCmp('saveTokenRegisterId').setDisabled(false);
		Ext.getCmp('updateTokenRegisterId').setDisabled(true);
		form.findField('registerName').setReadOnly(false);
	},
	
	initController: function(){
	}
});