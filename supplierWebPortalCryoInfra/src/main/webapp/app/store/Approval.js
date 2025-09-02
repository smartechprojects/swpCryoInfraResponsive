Ext.define('SupplierApp.store.Approval', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.SupplierDTO',
    alias:'store.approvalstore',
    autoLoad: false,
    remoteSort:false,
    pageSize: 12,
    proxy: {
        type: 'ajax',
        api: {
            read: 'approval/view.action',
            update: 'approval/update.action'
        },
        extraParams:{
        	currentApprover:'',
            status:'',
            step:'',
            notes:''
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