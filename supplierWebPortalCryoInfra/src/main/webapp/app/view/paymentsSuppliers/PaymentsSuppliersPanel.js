Ext.define('SupplierApp.view.paymentsSuppliers.PaymentsSuppliersPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.paymentsSuppliersPanel',
    border:false,
    frame:false,
    layout: 'fit', 
    scrollable: true, 
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'paymentsSuppliersGrid',
           	// height:430
           	 flex : 1
            }]
        });
        this.callParent(arguments);
    }
 
});