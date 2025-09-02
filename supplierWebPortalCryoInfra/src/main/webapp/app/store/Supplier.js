Ext.define('SupplierApp.store.Supplier', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.Supplier',
    alias:'store.supplier',
    autoLoad: false,
    remoteSort:false,
    pageSize: 12,
    proxy: {
        type: 'ajax',
        api: {
            read: 'supplier/view.action',
            create: 'supplier/save.action',
            update: 'supplier/update.action'
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