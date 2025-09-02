Ext.define('SupplierApp.model.PaymentCalendar', {
    extend: 'Ext.data.Model',
    fields: [
	        {name: 'id'},
	        {name: 'company'},
	        {name: 'year'},
	        {name: 'month'},
	        {name: 'updatedBy'},
	        {name: 'paymentDate', type: 'date', dateFormat: 'c'},
	        {name: 'uploadDate', type: 'date', dateFormat: 'c'}
        ]
});