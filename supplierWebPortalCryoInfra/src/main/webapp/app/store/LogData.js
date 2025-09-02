Ext.define('SupplierApp.store.LogData', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.LogData',
    alias:'store.logdata',
    autoLoad: false,
    proxy: {
        type: 'ajax',
        api: {
            read: 'orders/log/getLog.action'
        },
        extraParams:{
        	fromDate:'',
        	toDate:''
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