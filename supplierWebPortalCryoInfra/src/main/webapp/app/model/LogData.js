Ext.define('SupplierApp.model.LogData', {
    extend: 'Ext.data.Model',
    fields: [
	        {name: 'id'},
	        {name: 'logType'},
	        {name: 'mesage'},
	        {name: 'date', type: 'date', dateFormat: 'c'}
        ]

});