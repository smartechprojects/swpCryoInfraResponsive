Ext.define('SupplierApp.view.customBroker.CustomBrokerPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.customBrokerPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'customBrokerGrid',
           	 height:430
            }]
        });
        this.callParent(arguments);
    }
 
});