Ext.define('SupplierApp.model.Users', {
    extend: 'Ext.data.Model',
    fields: [
			{name: 'id'},
			{name: 'userName'},
			{name: 'name'},
			{name: 'password'},
			{name: 'email'},
			{name: 'role'},
			{name: 'notes'},
			{model: 'SupplierApp.model.Udc', name: 'userRole', mapping:'userRole', convert:modelNull},
			{model: 'SupplierApp.model.Udc', name: 'userType', mapping:'userType', convert:modelNull},
			{name: 'enabled', type:'boolean'},
			{name: 'openOrders', type:'boolean'},
			{name: 'logged', type:'boolean'},
			{name: 'agreementAccept', type:'boolean'},
			{name: 'exepAccesRule', type:'boolean'},
			{name: 'addressNumber'},
			{name: 'supplier', type:'boolean'},
			{name: 'subUser', type:'boolean'},
			{name: 'mainSupplierUser', type:'boolean'}
        ],
        proxy: {
            type: 'ajax',
            api: {
                read: 'admin/users/view.action',
                create: 'admin/users/save.action',
                update: 'admin/users/update.action',
                destroy: 'admin/users/delete.action'
            },
            reader: {
                type: 'json',
                root: 'data',
                successProperty: 'success'
            },
            writer: {
            	type: 'json',
                writeAllFields: true,
                encode: false
            }
        }
});