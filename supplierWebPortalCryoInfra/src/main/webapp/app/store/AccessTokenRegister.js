Ext.define('SupplierApp.store.AccessTokenRegister', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.AccessTokenRegister',
    alias:'store.accesstokenregister',
    autoLoad: false,
    remoteSort:true,
    pageSize: 12,
    proxy: {
        type: 'ajax',
        api: {
            read: 'supplier/token/listAccessTokenRegister.action'
        },
        extraParams:{
        	query:''
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