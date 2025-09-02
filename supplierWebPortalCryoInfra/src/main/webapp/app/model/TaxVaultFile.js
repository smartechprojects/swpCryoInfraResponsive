Ext.define('SupplierApp.model.TaxVaultFile', {
    extend: 'Ext.data.Model',
    fields: [
			{name:  'id'},
			{name:  'content'},
			{name:  'documentType'},
			{name:  'dateUpload' ,type: 'date', dateFormat: 'c'},
			{name:  'fileType'},
			{name:  'namefile'},
			{name:  'originName'},
			{name:  'status'},
			{name:  'numRefer'}
        ]
});