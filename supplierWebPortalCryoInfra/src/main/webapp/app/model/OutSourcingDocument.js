Ext.define('SupplierApp.model.OutSourcingDocument', {
    extend: 'Ext.data.Model',
    fields: [
    	{name: 'id'},
    	{name: 'company'},
    	{name: 'name'},
    	{name: 'addressBook'},
    	{name: 'supplierName'},
		{name: 'status', type:'boolean'},
		{name: 'obsolete', type:'boolean'},
    	{name: 'size'},
    	{name: 'type'},
    	{name: 'documentType'},
    	{name: 'frequency'},
    	{name: 'attachId'},
    	{name: 'uuid'},
    	{name: 'effectiveDate'},
    	{name: 'orderNumber'},
    	{name: 'orderType'},
    	{name: 'folio'},
    	{name: 'uploadDate'},
    	{name: 'notes'},
    	{name: 'monthLoad'},
    	{name: 'yearLoad'},
    	{name: 'rfc'},
    	{name: 'docStatus'}
        ]
});