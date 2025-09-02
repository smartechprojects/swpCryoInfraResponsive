Ext.define('SupplierApp.model.PurchaseOrderNotes', {
    extend: 'Ext.data.Model',
    fields: [
			{name:  'id'},
			{name:  'orderCompany'},
			{name:  'orderNumber'},
			{name:  'orderType'},
			{name:  'lineNumber'},
			{name:  'lineNtc'},
			{name:  'instruction'},
			{name:  'lineText'}
        ]
});