Ext.define('SupplierApp.store.Company', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.Company',
    alias:'store.company',
    autoLoad: false,
    remoteSort:true,
    pageSize: 12,
    proxy: {
        type: 'ajax',
        api: {
            read: 'company/searchByQuery.action'
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