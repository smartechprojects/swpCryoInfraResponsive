Ext.define('SupplierApp.store.Udc', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.Udc',
    alias:'store.udcstore',
    autoLoad: false,
    remoteSort:true,
    pageSize: 12,
    proxy: {
        type: 'ajax',
        api: {
            read: 'admin/udc/view.action',
            create: 'admin/udc/save.action',
            update: 'admin/udc/update.action',
            destroy: 'admin/udc/delete.action'
        },
        extraParams:{
        	query:'',
        	udcSystem:'',
        	udcKey:'',
        	strValue1:'',
        	strValue2:''
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