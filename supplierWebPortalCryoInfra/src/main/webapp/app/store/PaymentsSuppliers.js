Ext.define('SupplierApp.store.PaymentsSuppliers', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.PaymentsSuppliers',
    alias:'store.paymentssuppliers',
    autoLoad: false,
    remoteSort:true,
    pageSize: 12,
    proxy: {
        type: 'ajax',
        api: {
            read: 'receipt/getPaymentSupplierConsulta.action'
        },
        extraParams:{
        	addressNumber:'',
        	tipoDoc:'',
        	currencyCode:'',
        	estatus:'',
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