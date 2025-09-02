Ext.define('SupplierApp.view.purchaseOrder.ComplementoPagoPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.complementoPagoPanel',
    border:false,
    frame:false,
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
                      	height:430,
                      	flex:.50
                    },{    
            	    	xtype: 'acceptInvGrid',
                      	height:430,
                      	flex:.50
                    }
            	]
             }]
        });
        this.callParent(arguments);
    }
 
});