Ext.define('SupplierApp.view.purchaseOrder.PurchaseOrderPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.purchaseOrderPanel',
    border:false,
    frame:false,
    layout: 'fit',
    scrollable: true,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'purchaseOrderGrid',
           	 //height:430
           	scrollable: true,
           	flex: 1
            }]
        });
        this.callParent(arguments);
    }
 
});