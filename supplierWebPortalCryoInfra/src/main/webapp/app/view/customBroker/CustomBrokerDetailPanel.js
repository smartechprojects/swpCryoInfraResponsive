Ext.define('SupplierApp.view.customBroker.CustomBrokerDetailPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.customBrokerDetailPanel',
    border:false,
    frame:false,
    width:1700,
	initComponent: function () {
		 Ext.apply(this, {
 	            items: [{
	            	xtype: 'customBrokerForm',
	            	height:515
	            }/*,{
					xtype : 'displayfield',
					value : SuppAppMsg.purchaseTitle23,
					height:180,
					id:'fileListHtmlInvoice',
					autoScroll: true,
					margin: '0 0 0 15',
					fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
				}*/]
	        });
        this.callParent(arguments);
    }
 
});