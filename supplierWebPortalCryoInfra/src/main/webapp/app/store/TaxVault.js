Ext.define('SupplierApp.store.TaxVault', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.TaxVault',
    alias:'store.taxvault',
    autoLoad: false,
    pageSize: 10000,
    proxy: {
        enablePaging: true,
        type: 'ajax',
        api: {
        	read: 'taxVault/view.action'
        },
        extraParams:{
        	rfcReceptor:'',
        	rfcEmisor:'',
        	tvUUID:'',
        	tvFromDate:'',
        	tvToDate:'',
        	comboType:''
        		
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