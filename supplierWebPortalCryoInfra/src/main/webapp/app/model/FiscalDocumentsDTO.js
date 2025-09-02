Ext.define('SupplierApp.model.SupplierDTO', {
    extend: 'Ext.data.Model',
    fields: [
			{name: 'id'},
			{name: 'orderNumber'},
			{name: 'orderType'},
			{name: 'folio'},
			{name: 'documentNumber'}
        ]
});