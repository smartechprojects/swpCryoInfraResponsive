Ext.define('SupplierApp.store.PlantAccessWorker', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.PlantAccessWorker',
    alias:'store.plantaccessworker',
    autoLoad: false,
    pageSize: 10000,
    proxy: {
        enablePaging: true,
        type: 'ajax',
        api: {
        	read: 'plantAccess/searchWorkersPlantAccessByIdRequest.action'
        },
        extraParams:{
        	uuid:''
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