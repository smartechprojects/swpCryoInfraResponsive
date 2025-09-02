Ext.define('SupplierApp.view.fiscalDocuments.ComplementoPagoPanelFD' ,{
    extend: 'Ext.Panel',
    alias : 'widget.complementoPagoPanelFD',
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
            	    	xtype: 'selInvGridFD',
                      	height:430,
                      	flex:.50
                    },{    
            	    	xtype: 'acceptInvGridFD',
                      	height:430,
                      	flex:.50
                    }
            	]
             }]
        });
        this.callParent(arguments);
    }
 
});