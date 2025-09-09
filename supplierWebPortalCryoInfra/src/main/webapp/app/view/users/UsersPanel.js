Ext.define('SupplierApp.view.users.UsersPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.usersPanel',
    border:true,
    frame:false,
    title:'Usuarios',
    layout: 'fit', 
    scrollable: {
        x: true,
        y: true
    },
	initComponent: function () {
        Ext.apply(this, {
        	layout: {
                type: 'hbox',
                align: 'stretch'
            },   
            items: [{
            	xtype: 'usersForm',
            	flex:5

            },{
           	 xtype: 'usersGrid',
           	flex:5
            }]
        });
        this.callParent(arguments);
    }
 
});