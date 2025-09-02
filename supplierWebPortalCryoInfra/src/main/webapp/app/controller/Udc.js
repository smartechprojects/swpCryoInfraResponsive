Ext.define('SupplierApp.controller.Udc', {
    extend: 'Ext.app.Controller',
    stores: ['Udc'],
    models: ['Udc'],
    views: ['udc.UdcGrid','udc.UdcForm','udc.UdcPanel'],
    refs: [{
        	ref: 'udcForm',
        	selector: 'udcForm'
	    },
	    {
	        ref: 'udcGrid',
	        selector: 'udcGrid'
	    }],
 
    init: function() {
        this.control({
            'udcGrid': {
                selectionchange: this.gridSelectionChange
            },
            'udcForm button[action=loadUdc]': {
                click: this.loadGrid
            },
            'udcForm button[action=save]': {
                click: this.saveUdc
            },
            'udcForm button[action=new]': {
                click: this.resetUdcForm
            },
            'udcForm button[action=delete]': {
                click: this.deleteUdc
            },
            'udcForm button[action=update]': {
                click: this.updateUdc
            },
            '#searchUdc' : {
                "ontriggerclick": this.loadSearchList
            },
            '#winUdc button[action=UdcAdvanceSearchButton]':{
            	click: this.UdcAdvanceSearchButton
            }
        });
    },
    
    UdcAdvanceSearchButton:function(){    	
    	this.getUdcGrid().getStore().getProxy().extraParams={
    		udcSystem:Ext.getCmp('Sistema').getValue(),
    		udcKey:Ext.getCmp('Clave').getValue(),
    		strValue1:Ext.getCmp('Valor 1').getValue(),
    		strValue2:Ext.getCmp('Valor 2').getValue(),
    		query:''    		
    	};
    	this.getUdcGrid().getStore().load();
    },
    
    loadSearchList: function (event){
    	this.getUdcGrid().getStore().getProxy().extraParams={
    		query:event.getValue(),
    		udcSystem:'',
        	udcKey:'',
        	strValue1:'',
        	strValue2:''
    	};
    	this.getUdcGrid().getStore().load();
    },
    
    gridSelectionChange: function(model, records) {
        if (records[0]) {
        	var form = this.getUdcForm().getForm();
        	form.loadRecord(records[0]);
            this.enableUpdate();
        }
    },
    
    saveUdc: function (button) {
    	var form = this.getUdcForm().getForm();
    	if (form.isValid()) { 
        	record = Ext.create('SupplierApp.model.Udc');
            values = form.getFieldValues();
            updatedRecord = populateObj(record, values);
            
            if (values.id > 0){
            	Ext.Msg.alert(SuppAppMsg.usersSaveError, "Error");
    		} else{
    			this.getUdcStore().add(updatedRecord);
        	    this.getUdcStore().sync();
        	    this.getUdcStore().reload();
        	    form.reset();
    		}
    	}
    },
    
    resetUdcForm: function (button) {
    	var form = this.getUdcForm().getForm();
    	form.reset();
    	this.enableSave();
    },
    
    loadGrid: function (button) {
    	this.getUdcGrid().getStore().getProxy().extraParams={
    		query:'',
    		udcSystem:'',
        	udcKey:'',
        	strValue1:'',
        	strValue2:'',
    	},
 	   this.getUdcStore().reload();
     },
    
    updateUdc: function (button) {
    	var form = this.getUdcForm().getForm();
		if (form.isValid()) { 
			record = form.getRecord();
			values = form.getFieldValues();
			updatedRecord = populateObj(record, values);

			if (values.id > 0){
				record.set(updatedRecord);
				this.getUdcStore().sync();
				this.getUdcStore().reload();
				//form.reset();
			} else{
				Ext.Msg.alert(SuppAppMsg.udcMsgError1, "Error");
			}
		}
    },
    
    deleteUdc: function (button) {
    	var form = this.getUdcForm().getForm();
		record = form.getRecord();
		values = form.getFieldValues();
		if (values.id > 0){
			updatedRecord = populateObj(record, values);
			record.set(updatedRecord);
			this.getUdcStore().remove(record);
			this.getUdcStore().sync({callback: function() {},
                success: function() {
               	 Ext.getCmp('udcgrid').getStore().load();
            	     form.reset();
             },                            
             failure: function(batch, options) {
            	 Ext.Msg.alert('Error al guardar');
                }
            });
			this.getUdcStore().reload();
			form.reset();
		} else{
			Ext.Msg.alert(SuppAppMsg.udcMsgError2);
		}
    },

	enableUpdate: function(){
		Ext.getCmp('udcSave').setDisabled(true);
		Ext.getCmp('udcUpdate').setDisabled(false);
		Ext.getCmp('udcDelete').setDisabled(false);
		Ext.getCmp('udcSystem').setDisabled(true);
		Ext.getCmp('udcKey').setDisabled(true);
		
		
	},

	enableSave: function(){
		Ext.getCmp('udcSave').setDisabled(false);
		Ext.getCmp('udcUpdate').setDisabled(true);
		Ext.getCmp('udcDelete').setDisabled(true);
		Ext.getCmp('udcSystem').setDisabled(false);
		Ext.getCmp('udcKey').setDisabled(false);
	},
	
	initController: function(){
	}
});