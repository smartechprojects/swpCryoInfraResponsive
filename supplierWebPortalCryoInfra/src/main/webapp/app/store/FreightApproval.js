Ext.define('SupplierApp.store.FreightApproval', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.FreightApproval',
    alias:'store.freightapproval',
    autoLoad: false,
    pageSize: 12,
    proxy: {
        enablePaging: true,
        type: 'ajax',
        api: {
        	read: 'freight/view.action'
        },
        extraParams:{
        	accountNumber:'',
        	status:'',
        	batchIdParam:''	
        		
        },
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty: 'total', 
            successProperty: 'success'
        },
        writer: {
        	type: 'json',
            writeAllFields: true,
            encode: false
        }
    }
});