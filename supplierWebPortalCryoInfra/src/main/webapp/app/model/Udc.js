Ext.define('SupplierApp.model.Udc', {
    extend: 'Ext.data.Model',
    fields: [
	        {name: 'id'},
	        {name: 'udcSystem'},
	        {name: 'udcKey'},
	        {name: 'strValue1'},
	        {name: 'strValue2'},
	        {name: 'intValue',type:'int'},
	        {name: 'booleanValue',type:'boolean'},
	        {name: 'dateValue'},
	        {name: 'systemRef'},
	        {name: 'keyRef'},
	        {name: 'createdBy'},
	        {name: 'creationDate'}
        ]
});