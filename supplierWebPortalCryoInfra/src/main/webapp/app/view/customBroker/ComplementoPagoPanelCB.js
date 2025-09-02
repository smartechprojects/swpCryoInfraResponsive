Ext.define('SupplierApp.view.customBroker.ComplementoPagoPanelCB' ,{
    extend: 'Ext.Panel',
    alias : 'widget.complementoPagoPanelCB',
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
            	    	xtype: 'selInvGridCB',
                      	height:430,
                      	flex:.50
                    },{    
            	    	xtype: 'acceptInvGridCB',
                      	height:430,
                      	flex:.50
                    }
            	]
             }]
        });
        this.callParent(arguments);
    }
 
});