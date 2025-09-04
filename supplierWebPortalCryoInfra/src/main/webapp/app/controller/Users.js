Ext.define('SupplierApp.controller.Users', {
    extend: 'Ext.app.Controller',
    stores: ['Users'],
    models: ['Users'],
    views: ['users.UsersPanel','users.UsersForm','users.UsersGrid'],
    refs: [{
        	ref: 'usersForm',
        	selector: 'usersForm'
	    },
	    {
	        ref: 'usersGrid',
	        selector: 'usersGrid'
	    }],
 
    init: function() {
        this.control({
            'usersGrid': {
                selectionchange: this.gridSelectionChange
            },
            'usersForm button[action=saveUsers]': {
                click: this.saveUsers
            },
            'usersForm button[action=usersNew]': {
                click: this.resetUsersForm
            },
            'usersForm button[action=deleteUsers]': {
                click: this.deleteUsers
            },
            'usersForm button[action=updateUsers]': {
                click: this.updateUsers
            },
            '#searchUsers' : {
                "ontriggerclick": this.loadSearchList
            }
        });
    },

    gridSelectionChange: function(model, records) {
        if (records[0]) {
        	var form = this.getUsersForm().getForm();
        	var box = Ext.MessageBox.wait(SuppAppMsg.approvalLoadRegistrer, SuppAppMsg.approvalExecution);
        	form.loadRecord(records[0]);

        	var roleCombo = form.findField('userRole.id');
			roleCombo.store.load({
                callback: function (r, options, success) {
                		roleCombo.setValue(records[0].data.userRole.id);
                 }
            });		
			
        	var typeCombo = form.findField('userType.id');
        	typeCombo.store.load({
                callback: function (r, options, success) {
                	typeCombo.setValue(records[0].data.userType.id);
                 }
            });
        	
        	Ext.getCmp('usersFormUserName').setReadOnly(true);
        	Ext.getCmp('usersFormName').setReadOnly(true);

        	var uUserName = records[0].data.userName;
        	var uAddressNumber = records[0].data.addressNumber;
        	var uRole = records[0].data.role;
        	var uIsSupplier = records[0].data.supplier;
        	var uIsSubUser = records[0].data.subUser;
        	var uIsMainSupplierUser = records[0].data.mainSupplierUser;
        	
        	if(uIsSupplier == true){
        		Ext.getCmp('usersIsSubUser').setDisabled(true);
        	} else {
        		Ext.getCmp('usersIsSubUser').setDisabled(false);
        	}
        	
        	if(uRole == 'ROLE_SUPPLIER'){
        		Ext.getCmp('usersAddressNumber').show();
        		if(uIsSupplier == true){
        			Ext.getCmp('usersAddressNumber').setReadOnly(true);
        		} else {
        			Ext.getCmp('usersAddressNumber').setReadOnly(false);
        		}

        		Ext.getCmp('userMainSupplierUser').show();        		
            	if(uIsMainSupplierUser == true){
            		this.enableSubUserSupplierUser();
            		this.disableMainSupplierUser();
            	} else {
            		this.enableMainSupplierUser();
            		this.disableSubUserSupplierUser();
            	}
            	
            	Ext.getCmp('userTypeCombo').setDisabled(true);
        	} else {
        		this.enableSubUserSupplierUser();
        		this.enableMainSupplierUser();
        		Ext.getCmp('usersAddressNumber').hide();
        		Ext.getCmp('userMainSupplierUser').hide();
        		Ext.getCmp('userMainSupplierUserMsg').hide();
        		Ext.getCmp('userTypeCombo').setDisabled(false);
        	}
        	//Ext.getCmp('usersFormEmail').setReadOnly(true);
        	
            this.enableUpdate();
            box.hide();
        }
    },
        
    loadSearchList: function (field, newValue) {
        var store = this.getUsersGrid().getStore();
        store.getProxy().extraParams = {
            query: newValue
        };
        store.load();
    },
    
    saveUsers: function (button) {
    	var me = this;
    	var form = this.getUsersForm().getForm();
    	if (form.isValid()) {
    		//¡Estos campos se deben habilitar para poder enviar el valor correspondiente al controlador de Java (Condición ExtJS)!
    		Ext.getCmp('usersRoleCombo').setDisabled(false);
    		Ext.getCmp('userTypeCombo').setDisabled(false);
    		//this.enableMainSupplierUser();
	    	
        	var record = Ext.create('SupplierApp.model.Users');
        	values = form.getFieldValues();
        	updatedRecord = populateObj(record, values);
        	
            if (values.id > 0){
            	Ext.Msg.alert( SuppAppMsg.usersSaveError, "Error");
    		} else{
    	    	var box = Ext.MessageBox.wait(SuppAppMsg.usersSaveDataMsj, SuppAppMsg.approvalExecution);
        		record.set(updatedRecord);
        		record.save({
        			callback: function (records, o, success, msg) {

        				if(success == true){
        		    		var r1 = Ext.decode(o._response.responseText);
        			    	var res = Ext.decode(r1);
        			    	var msgResp = res.message;
        			    	if(msgResp == ''){
        			    		var grid = me.getUsersGrid();
        			        	var store = grid.getStore();
        			        	store.reload();
        			    		grid.getView().refresh();
        			    		form.reset();
        			    		box.hide();
        			    		Ext.toggle.msg('Resultado', 'Su operación ha sido completada.');
        			    		return true;
        			    	} else {
        			    		box.hide();
        			    		
					        	if(msgResp == "Error_1"){
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  SuppAppMsg.usersUserExistsError});
					        	}else if(msgResp == "Error_2"){
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  SuppAppMsg.usersSupplierPendingError});					        		
					        	}else if(msgResp == "Error_3"){
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  SuppAppMsg.usersSupplierExistsError});
					        	}else if(msgResp == "Error_4"){
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  SuppAppMsg.usersSupplierMainUserError});
					        	}else if(msgResp == "Error_5"){
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  SuppAppMsg.usersSupplierSubUserError});
					        	}else{
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  msgResp});
					        	}
					        	
					        	//Se vuelve a deshabilitar estos campos según las condiciones
						    	var isSubUser = Ext.getCmp('usersIsSubUser').getValue();
						    	if(isSubUser == true){
						    		Ext.getCmp('usersRoleCombo').setDisabled(true);
						    		Ext.getCmp('userTypeCombo').setDisabled(true);
						    	}
						    	
						    	var uRole = Ext.getCmp('usersRoleCombo').rawValue;
						    	if(uRole == "ROLE_SUPPLIER"){
						    		Ext.getCmp('userTypeCombo').setDisabled(true);
						    	}
						    	
						    	//var isMainSupplierUser = Ext.getCmp('userMainSupplierUser').getValue();
						    	//if(isMainSupplierUser == true){
						    	//	this.disableMainSupplierUser();
						    	//}
						    	
        			    		return false;
        			    	}
        				}
        			}
        		});
    		}
    	}
    },

    updateUsers: function (button) {
    	
    	var me = this;
    	var form = this.getUsersForm().getForm();
    	var grid = this.getUsersGrid();
    	if (form.isValid()) {
    		//¡Estos campos se deben habilitar para poder enviar el valor correspondiente al controlador de Java (Condición ExtJS)!
    		Ext.getCmp('usersRoleCombo').setDisabled(false);
    		Ext.getCmp('userTypeCombo').setDisabled(false);
    		this.enableMainSupplierUser();
    		this.enableSubUserSupplierUser();
    		
        	var record = form.getRecord();
        	values = form.getFieldValues();
        	updatedRecord = populateObj(record, values);
        	
            if (values.id > 0){
    	    	var box = Ext.MessageBox.wait(SuppAppMsg.usersSaveDataMsj, SuppAppMsg.approvalExecution);
        		record.set(updatedRecord);
        		record.save({
        			callback: function (records, o, success, msg) {
        				if(success == true){
        		    		var r1 = Ext.decode(o._response.responseText);
        			    	var res = Ext.decode(r1);
        			    	var msgResp = res.message;
        			    	if(msgResp == ''){
        			    		var grid = me.getUsersGrid();
        			        	var store = grid.getStore();
        			        	store.reload();
        			    		grid.getView().refresh();
        			    		form.reset();
        			    		me.enableSave();
        			    		box.hide();
        			    		Ext.toggle.msg('Resultado', 'Su operación ha sido completada.');
        			    		return true;
        			    	} else {
        			    		box.hide();
        			    		
					        	if(msgResp == "Error_1"){
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  SuppAppMsg.usersUserExistsError});
					        	}else if(msgResp == "Error_2"){
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  SuppAppMsg.usersSupplierPendingError});					        		
					        	}else if(msgResp == "Error_3"){
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  SuppAppMsg.usersSupplierExistsError});
					        	}else if(msgResp == "Error_4"){
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  SuppAppMsg.usersSupplierMainUserError});
					        	}else if(msgResp == "Error_5"){
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  SuppAppMsg.usersSupplierSubUserError});
					        	}else{
					        		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.tabUsers, msg:  msgResp});
					        	}
					        	
					        	//Se vuelve a deshabilitar estos campos según las condiciones
						    	var isSubUser = Ext.getCmp('usersIsSubUser').getValue();
						    	if(isSubUser == true){
						    		Ext.getCmp('usersRoleCombo').setDisabled(true);
						    		Ext.getCmp('userTypeCombo').setDisabled(true);
						    		this.disableSubUserSupplierUser();
						    	}
						    	
						    	var uRole = Ext.getCmp('usersRoleCombo').rawValue;
						    	if(uRole == "ROLE_SUPPLIER"){
						    		Ext.getCmp('userTypeCombo').setDisabled(true);
						    	}
						    	
						    	var isMainSupplierUser = Ext.getCmp('userMainSupplierUser').getValue();
						    	if(isMainSupplierUser == true){
						    		this.disableMainSupplierUser();
						    	}
						    	
        			    		return false;
        			    	}
        				}
        			}
        		});
    		} else{
    			Ext.Msg.alert(SuppAppMsg.supplierUpdateFail, "Error");
    		}
    	}
    },
    
    deleteUsers: function (button) {
    	var form = this.getUsersForm().getForm();
    	var grid = this.getUsersGrid();
		record = form.getRecord();
		values = form.getFieldValues();
		if (values.id > 0){
			updatedRecord = populateObj(record, values);
			record.set(updatedRecord);
			this.getUsersStore().remove(record);
			this.getUsersStore().sync({callback: function() {},
                success: function() {
                	 grid.store.load();
		        	 grid.store.reload();
		        	 grid.getView().refresh();
            	     form.reset();
             },                            
             failure: function(batch, options) {
            	 Ext.Msg.alert(SuppAppMsg.usersSaveErrorTitle);
                }
            });
			this.getUsersStore().reload();
			form.reset();
		} else{
			Ext.Msg.alert(SuppAppMsg.usersDeleteError);
		}
    },
    
    resetUsersForm: function (button) {
    	var form = this.getUsersForm().getForm();
    	form.reset();
    	Ext.getCmp('usersFormUserName').setReadOnly(false);
    	Ext.getCmp('usersFormName').setReadOnly(false);
    	Ext.getCmp('usersAddressNumber').setDisabled(false);
		Ext.getCmp('usersAddressNumber').setReadOnly(false);
		Ext.getCmp('usersAddressNumber').hide();
		Ext.getCmp('userMainSupplierUser').hide();
		Ext.getCmp('userMainSupplierUserMsg').hide();
		Ext.getCmp('usersRoleCombo').setDisabled(false);
		Ext.getCmp('usersRoleCombo').setValue(null);
		Ext.getCmp('userTypeCombo').setDisabled(false);
		Ext.getCmp('userTypeCombo').setValue(null);
		Ext.getCmp('usersIsSubUser').setDisabled(false);
		Ext.getCmp('userMainSupplierUser').setDisabled(false);
    	//Ext.getCmp('usersFormEmail').setReadOnly(false);
    	this.enableSave();
    },
    
    enableUpdate: function(){
		Ext.getCmp('saveUsers').setDisabled(true);
		Ext.getCmp('updateUsers').setDisabled(false);
		Ext.getCmp('deleteUsers').setDisabled(false);	
	},

	enableSave: function(){
    	var roleCombo = Ext.getCmp('usersRoleCombo');
		roleCombo.store.load();
		roleCombo.store.reload();
		
    	var typeCombo = Ext.getCmp('userTypeCombo');
    	typeCombo.store.load();
    	typeCombo.store.reload();
    	
		Ext.getCmp('saveUsers').setDisabled(false);
		Ext.getCmp('updateUsers').setDisabled(true);
		Ext.getCmp('deleteUsers').setDisabled(true);
    	Ext.getCmp('usersFormUserName').setReadOnly(false);
    	Ext.getCmp('usersFormName').setReadOnly(false);
		Ext.getCmp('usersAddressNumber').setDisabled(false);
		Ext.getCmp('usersAddressNumber').setReadOnly(false);
		Ext.getCmp('usersAddressNumber').hide();
		Ext.getCmp('userMainSupplierUser').hide();
		Ext.getCmp('userMainSupplierUserMsg').hide();
		Ext.getCmp('usersRoleCombo').setDisabled(false);
		Ext.getCmp('usersRoleCombo').setValue(null);
		Ext.getCmp('userTypeCombo').setDisabled(false);
		Ext.getCmp('userTypeCombo').setValue(null);
		Ext.getCmp('usersIsSubUser').setDisabled(false);
		Ext.getCmp('userMainSupplierUser').setDisabled(false);
	},
	
	disableMainSupplierUser: function(){
		Ext.getCmp('usersIsSubUser').setDisabled(true);
		Ext.getCmp('usersRoleCombo').setDisabled(true);
		Ext.getCmp('userTypeCombo').setDisabled(true);
		Ext.getCmp('usersAddressNumber').setDisabled(true);
		Ext.getCmp('userMainSupplierUser').setDisabled(true);
		Ext.getCmp('userMainSupplierUserMsg').show();
	},
	
	
	enableMainSupplierUser: function(){
		Ext.getCmp('usersIsSubUser').setDisabled(false);
		Ext.getCmp('usersRoleCombo').setDisabled(false);
		Ext.getCmp('userTypeCombo').setDisabled(false);
		Ext.getCmp('usersAddressNumber').setDisabled(false);
		Ext.getCmp('userMainSupplierUser').setDisabled(false);
		Ext.getCmp('userMainSupplierUserMsg').hide();
	},

	disableSubUserSupplierUser: function(){
		Ext.getCmp('usersIsSubUser').setDisabled(true);
		Ext.getCmp('usersRoleCombo').setDisabled(true);
		Ext.getCmp('userTypeCombo').setDisabled(true);
		Ext.getCmp('usersAddressNumber').setDisabled(true);
	},
	
	
	enableSubUserSupplierUser: function(){
		Ext.getCmp('usersIsSubUser').setDisabled(false);
		Ext.getCmp('usersRoleCombo').setDisabled(false);
		Ext.getCmp('userTypeCombo').setDisabled(false);
		Ext.getCmp('usersAddressNumber').setDisabled(false);
	},
	
	initController: function(){
	}
});