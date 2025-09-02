Ext.define('SupplierApp.store.PlantAccessFile', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.PlantAccessFile',
    alias:'store.plantaccessfile',
    autoLoad: false,
    pageSize: 10000,
    proxy: {
        enablePaging: true,
        type: 'ajax',
        api: {
        	read: 'plantAccess/searchWorkerFilesPlantAccessByIdWorker.action'
        },
        extraParams:{
			idRequest:'',
			idWorker:''
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