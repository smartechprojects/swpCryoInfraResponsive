Ext.define('SupplierApp.view.purchaseOrder.PurchaseOrderPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.purchaseOrderPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'purchaseOrderGrid',
           	 height:430
            }]
        });
        this.callParent(arguments);
    }
 
});