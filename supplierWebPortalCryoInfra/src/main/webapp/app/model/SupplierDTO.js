Ext.define('SupplierApp.model.SupplierDTO', {
    extend: 'Ext.data.Model',
    fields: [
			{name: 'id'},
			{name: 'name'},
			{name: 'razonSocial'},
			{name: 'ticketId'},
			{name: 'email'},
			{name: 'addresNumber'},
			{name: 'categoria'},
			{name: 'tipoProducto'},
			{name: 'currentApprover'},
			{name: 'nextApprover'},
			{name: 'approvalStatus'},
			{name: 'logged'},
			{name: 'approvalStep'},
			{name: 'observaciones'},
			{name: 'rejectNotes'},
			{name: 'approvalNotes'},
			{name: 'fechaSolicitud', type: 'date', dateFormat: 'c'},
			{name: 'fechaAprobacion', type: 'date', dateFormat: 'c'}
        ]
});