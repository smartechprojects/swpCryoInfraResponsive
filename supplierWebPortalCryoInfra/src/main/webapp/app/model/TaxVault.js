Ext.define('SupplierApp.model.TaxVault', {
    extend: 'Ext.data.Model',
    fields: [
				{name:  'id'},
				{name:  'uploadDate', type: 'date', dateFormat: 'c'},
				{name:  'nameFile'},
				{name:  'uuid'},
				{name:  'rfcEmisor'},
				{name:  'rfcReceptor'},
				{name:  'usuario'},
				{name:  'year'},
				{name:  'invoiceDate'},
				{name:  'amount'},
				{name:  'serie'},
				{name:  'status'},
				{name:  'folio'},
				{name:  'size'},
				{name:  'type'},
				{name:  'documentType'},
				{name:  'documentStatus'},
				{name:  'ip'},
				{name:  'origen'}, 
				{name:  'hostname'},
				{name:  'addressNumber'}
        ]
});