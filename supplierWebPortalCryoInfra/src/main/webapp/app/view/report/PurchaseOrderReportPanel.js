Ext.define('SupplierApp.view.purchaseOrder.PurchaseOrderReportPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.purchaseOrderReportPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'purchaseOrderReportGrid',
           	 height:460
            }]
        });
        this.callParent(arguments);
    }
 
});