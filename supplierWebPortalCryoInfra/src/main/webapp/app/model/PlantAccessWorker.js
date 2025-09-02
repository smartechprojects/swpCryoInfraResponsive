Ext.define('SupplierApp.model.PlantAccessWorker', {
    extend: 'Ext.data.Model',
    fields: [
			{name:  'id'},
			{name:  'tempId'},
			{name:  'employeeName'},
			{name:  'employeeLastName'},
			{name:  'employeeSecondLastName'},
			{name:  'cardNumber'},
			{name:  'membershipIMSS'},
			{name:  'datefolioIDcard'},
			{name:  'dateInduction'},
			{name:  'fechaRegistro'},
			{name:  'requestNumber'},
			{name:  'activities'},
			{name:  'listDocuments'},
			{name: 'allDocuments', type:'boolean'},
			{name: 'docsActivity1', type:'boolean'},
			{name: 'docsActivity2', type:'boolean'},
			{name: 'docsActivity3', type:'boolean'},
			{name: 'docsActivity4', type:'boolean'},
			{name: 'docsActivity5', type:'boolean'},
			{name: 'docsActivity6', type:'boolean'},
			{name: 'docsActivity7', type:'boolean'},
			{name:  'employeeOrdenes'},
			{name:  'employeePuesto'},
			{name:  'employeeCurp'},
			{name:  'employeeRfc'}
        ],
        proxy: {
            type: 'ajax',
            api: {
                create: 'plantAccess/savePlantAccessWorker.action',
                update: 'plantAccess/updatePlantAccessWorker.action',
                destroy: 'plantAccess/deletePlantAccessWorker.action'
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