Ext.define('SupplierApp.view.paymentsSuppliers.PaymentsSuppliersPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.paymentsSuppliersPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'paymentsSuppliersGrid',
           	 height:430
            }]
        });
        this.callParent(arguments);
    }
 
});