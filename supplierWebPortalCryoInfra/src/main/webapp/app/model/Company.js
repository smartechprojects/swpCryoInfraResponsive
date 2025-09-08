Ext.define('SupplierApp.model.Company', {
    extend: 'Ext.data.Model',
    fields: [
	    	{name: 'id'},
	    	{name: 'company'},
	    	{name: 'companyName'},
	    	{name: 'taxFileRef'},
	    	{name: 'secretPass'},
	    	{name: 'logoFileRef'},
	    	{name: 'notificationEmail'},
	    	{name: 'status'},
	    	{name: 'attachId'},
	    	{name: 'creationDate', type: 'date', dateFormat: 'c'},
			//{name: 'active', type:'boolean'}			
	    	{name: 'active', type: 'boolean', convert: function(v) { return v === true || v === 'true'; }}
        ],
        proxy: {
            type: 'ajax',
            api: {
                create: 'admin/company/save.action',
                update: 'admin/company/update.action'
            },
            reader: {
                type: 'json',
                rootProperty: 'data',
                successProperty: 'success'
            },
            writer: {
            	type: 'json',
                writeAllFields: true,
                encode: false
            }
        }
});