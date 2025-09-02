Ext.define('SupplierApp.store.Receipt', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.Receipt',
    alias:'store.receipt',
    autoLoad: false,
    remoteSort:true,
    pageSize: 12,
    proxy: {
        type: 'ajax',
        api: {
            read: 'receipt/getOrderReceipts.action'
        },
        extraParams:{
        	orderNumber:'',
        	orderType:'',
        	addressBook:'',
        	orderCompany:''
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