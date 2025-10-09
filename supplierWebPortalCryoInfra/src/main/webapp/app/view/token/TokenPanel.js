Ext.define('SupplierApp.view.token.TokenPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.tokenPanel',
    border:false,
    frame:false,
    autoScroll : true,
	initComponent: function () {
        Ext.apply(this, {
        	/*layout: {
                type: 'hbox'
            }, */  
            items: [{
            	xtype: 'tokenForm',
            	height:150
            	//flex:.4

            },{
           	 xtype: 'tokenGrid',
           	 //flex:.6,
           	height:300,
           //	autoScroll : true
            }]
        });
        this.callParent(arguments);
    }
});