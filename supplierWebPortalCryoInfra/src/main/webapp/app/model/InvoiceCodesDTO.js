Ext.define('SupplierApp.model.InvoiceCodesDTO', {
    extend: 'Ext.data.Model',
    fields: [
			{name: 'uuid'},
			{name: 'code'},
			{name: 'uom'},
			{name: 'description'},
			{name: 'descriptionSAT'},
			{name: 'amount'}
        ]
});