Ext.define('SupplierApp.view.users.UsersPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.usersPanel',
    border:false,
    frame:false,
    layout: 'fit',
    autoScroll : false,
	initComponent: function () {
        Ext.apply(this, {
        	layout: {
                type: 'hbox',
                align: 'stretch'
            },   
            items: [{
            	xtype: 'usersForm',
            	flex:.4,
            	border:true

            },{
           	 xtype: 'usersGrid',
           	flex:.6,
        	border:true
            }]
        });
        this.callParent(arguments);
    }
 
});