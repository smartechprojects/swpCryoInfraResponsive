Ext.define('SupplierApp.view.token.TokenPanel' ,{
    extend: 'Ext.panel.Panel',
    alias : 'widget.tokenPanel',
    border:false,
    frame:false,
    layout:'fit',
    autoScroll : false,
	initComponent: function () {
        Ext.apply(this, {
        	layout: {
                type: 'vbox',
                align: 'stretch'
            },  
            items: [{
            	xtype: 'tokenForm',
            	height: 100
            },{
           	 xtype: 'tokenGrid',
           	 flex: 1
            }]
        });
        this.callParent(arguments);
    }
});