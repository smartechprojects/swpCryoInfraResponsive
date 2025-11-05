Ext.define('SupplierApp.view.paymentsSuppliers.PaymentsSuppliersPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.paymentsSuppliersPanel',
    border:false,
    frame:false,
    layout: 'fit',
    autoScroll : false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'paymentsSuppliersGrid'
            }]
        });
        this.callParent(arguments);
    }
 
});