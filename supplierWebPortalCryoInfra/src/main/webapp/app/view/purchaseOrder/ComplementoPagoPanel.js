Ext.define('SupplierApp.view.purchaseOrder.ComplementoPagoPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.complementoPagoPanel',
    border:false,
    frame:false,
    layout: 'fit',
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
            	layout: {
            	    type: 'hbox',
            	    pack: 'start',
            	    align: 'stretch'
            	},
            	items: [
            	    {    
            	    	xtype: 'selInvGrid',
                      	//height:430,
                      	flex:1
                    },{    
            	    	xtype: 'acceptInvGrid',
                      	//height:430,
                      	flex:1
                    }
            	]
             }]
        });
        this.callParent(arguments);
    }
 
});