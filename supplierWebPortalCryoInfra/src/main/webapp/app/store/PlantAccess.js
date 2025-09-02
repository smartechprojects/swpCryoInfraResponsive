Ext.define('SupplierApp.store.PlantAccess', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.PlantAccess',
    alias:'store.plantaccess',
    autoLoad: false,
    pageSize: 10000,
    proxy: {
        enablePaging: true,
        type: 'ajax',
        api: {
        	read: 'plantAccess/view.action'
        },
        extraParams:{
        	accountNumber:'',
        	status:'',
        	approver:''	
        },
        reader: {
            type: 'json',
            rootProperty: 'data',
            successProperty: 'success'
        },
        writer: {
        	type: 'json',
            writeAllFields: true,
            encode: false
        }
    }
});