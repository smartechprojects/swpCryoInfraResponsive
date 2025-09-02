Ext.define('SupplierApp.model.PlantAccessDetail', {
    extend: 'Ext.data.Model',
    fields: [
			{name:  'id'},
			{name:  'employeeName'},
			{name:  'membershipIMSS'},
			{name:  'datefolioIDcard'},
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
			{name: 'docsActivity7', type:'boolean'}	
        ]
});