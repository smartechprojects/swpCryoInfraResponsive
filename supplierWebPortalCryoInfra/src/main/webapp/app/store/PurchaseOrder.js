Ext.define('SupplierApp.store.PurchaseOrder', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.PurchaseOrder',
    alias:'store.purchaseorder',
    autoLoad: false,
    remoteSort:true,
    pageSize: 12,
    proxy: {
        type: 'ajax',
        api: {
            //read: 'supplier/orders/view.action',
            create: 'supplier/orders/save.action',
            update: 'supplier/orders/update.action',
            destroy: 'supplier/orders/delete.action'
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