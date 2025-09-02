Ext.define('SupplierApp.model.Tolerances', {
    extend: 'Ext.data.Model',
    
    fields: [
    	{name: 'id'},
    	{name: 'final'},
    	{name: 'type'},
    	{name: 'itemNumber'},
    	{name: 'company'},
    	{name: 'commodityClass'},
    	{name: 'commodityCode'},
    	{name: 'qtyPercentage'},
    	{name: 'qtyUnits'},
    	{name: 'unitCostPercentage'},
    	{name: 'unitCostAmount'},
    	{name: 'extendedAmtPercentage'},
    	{name: 'extendedAmtAmount'},
    	{name: 'currencyCode'}
        ]
});