Ext.define('SupplierApp.store.CustomBroker', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.CustomBroker',
    alias:'store.custombroker',
    autoLoad: false,
    pageSize: 10000,
    proxy: {
        enablePaging: true,
        type: 'ajax',
        api: {
        	read: 'customBroker/view.action'
        },
        extraParams:{
        	addressNumber:'',
        	status:'',
        	uuid:'',
        	documentType:'',
        	invoiceStatus:''
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