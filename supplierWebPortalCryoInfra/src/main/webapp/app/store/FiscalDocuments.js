Ext.define('SupplierApp.store.FiscalDocuments', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.FiscalDocuments',
    alias:'store.fiscaldocuments',
    autoLoad: false,
    pageSize: 12,
    proxy: {
        enablePaging: true,
        type: 'ajax',
        api: {
        	read: 'fiscalDocuments/view.action'
        },
        extraParams:{
        	orderNumber:'',
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