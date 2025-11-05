Ext.define('SupplierApp.view.customBroker.CustomBrokerPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.customBrokerPanel',
    border:false,
    frame:false,
    layout: 'fit',
    autoScroll : false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'customBrokerGrid'
            }]
        });
        this.callParent(arguments);
    }
 
});