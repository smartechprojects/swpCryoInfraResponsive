Ext.define('SupplierApp.view.token.TokenPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.tokenPanel',
    border:false,
    frame:false,
    scroll : false,
	initComponent: function () {
        Ext.apply(this, {
        	/*layout: {
                type: 'hbox'
            }, */  
            items: [{
            	xtype: 'tokenForm',
            	height:130
            	//flex:.4

            },{
           	 xtype: 'tokenGrid',
           	 //flex:.6,
           	height:300
            }]
        });
        this.callParent(arguments);
    }
});