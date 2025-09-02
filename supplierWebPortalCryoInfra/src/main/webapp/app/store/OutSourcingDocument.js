Ext.define('SupplierApp.store.OutSourcingDocument', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.OutSourcingDocument',
    alias:'store.outsourcingdocument',
    autoLoad: false,
    pageSize: 11,
    proxy: {
        type: 'ajax',
        api: {
        	read: 'supplier/searchOSDocuments.action'
        },
        extraParams:{
        	status:'',
        	supplierName:'',
        	documentType:'',
        	fromDate:'',
        	toDate:'',
        	roleType:''
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