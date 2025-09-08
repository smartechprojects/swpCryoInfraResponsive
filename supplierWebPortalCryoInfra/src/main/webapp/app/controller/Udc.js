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
    
    loadSearchList: function (field, newValue) {
        var store = this.getUdcGrid().getStore();
        store.getProxy().extraParams = {
            query: newValue,
            udcSystem: '',
            udcKey: '',
            strValue1: '',
            strValue2: ''
        };
        store.load();
    },
    
    gridSelectionChange: function(model, records) {
    	
        if (records[0]) {
        	var form = this.getUdcForm().getForm();
        	form.loadRecord(records[0]);
            this.enableUpdate();
        }
    },
    
    saveUdc: function (button) {
    	var me = this;
    	var form = this.getUdcForm().getForm();
    	if (form.isValid()) { 
    		
        	var record = Ext.create('SupplierApp.model.Udc');
            values = form.getFieldValues();
            updatedRecord = populateObj(record, values);
            if (values.id > 0){
            	Ext.Msg.alert(SuppAppMsg.usersSaveError, "Error");
    		} else{        		
    			me.getUdcStore().add(updatedRecord);
        	    me.getUdcStore().sync();
        	    me.getUdcStore().reload();
        	    Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.udcMsg1);
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
    
   /* updateUdc: function (button) {
    	debugger
    	var form = this.getUdcForm().getForm();
		if (form.isValid()) { 
			record = form.getRecord();
			values = form.getFieldValues();
			updatedRecord = populateObj(record, values);

			if (values.id > 0){
				//record.set(updatedRecord);
				if (this.getUdcStore().indexOf(record) === -1) {
				    this.getUdcStore().add(record);
				}
				record.set(values);
				//this.getUdcStore().sync();
				//this.getUdcStore().reload();
				//form.reset();
			} else{
				Ext.Msg.alert(SuppAppMsg.udcMsgError1, "Error");
			}
		}
    },*/
     
     updateUdc: function (button) {
    	    var me    = this;
    	    var form  = this.getUdcForm().getForm();
    	    var store = this.getUdcGrid().getStore(); 

    	    if (!form.isValid()) return;

    	    var values    = form.getValues();
    	    var updatedId = parseInt(values.id, 10);

    	    if (!updatedId || updatedId <= 0) {
    	        Ext.Msg.alert(SuppAppMsg.udcMsgError1, "Error: id inválido");
    	        return;
    	    }

    	    // obtener el registro actual del store del grid
    	    var record = store.findRecord('id', updatedId, 0, false, false, true) || form.getRecord();

    	    if (!record) {
    	        Ext.Msg.alert(SuppAppMsg.udcMsgError1, "No se encontró el registro en el store del grid.");
    	        return;
    	    }

    	    record.set(values);

    	    store.sync({
    	        success: function () {
    	            Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.udcMsg1);

    	            var searchField = me.getUdcForm().down('#searchUdc');
    	            var searchValue = searchField ? searchField.getValue() : '';

    	            store.getProxy().extraParams = {
    	                query: searchValue,
    	                udcSystem: '',
    	                udcKey: '',
    	                strValue1: '',
    	                strValue2: ''
    	            };

    	            store.load({
    	                callback: function (records, operation, success) {
    	                    var refreshed = store.findRecord('id', updatedId, 0, false, false, true);
    	                    if (refreshed) {
    	                        me.getUdcGrid().getSelectionModel().select(refreshed);

    	                        me.gridSelectionChange(
    	                            me.getUdcGrid().getSelectionModel(),
    	                            [refreshed]
    	                        );
    	                    }
    	                }
    	            });
    	        },
    	        failure: function (batch) {
    	            var errorMsg = "Error desconocido";
    	            if (batch.exceptions && batch.exceptions.length > 0) {
    	                var op = batch.exceptions[0];
    	                if (op.error && op.error.statusText) {
    	                    errorMsg = op.error.statusText;
    	                }
    	            }
    	            Ext.Msg.alert(SuppAppMsg.udcMsgError3, errorMsg);
    	        }
    	    });
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