Ext.define('SupplierApp.view.receipt.ReceiptPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.receiptPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {
        	layout: {
                type: 'vbox',
                align: 'stretch'
            },   
            items: [{
            	xtype: 'receiptForm',
            	height:80

            },{
           	 xtype: 'receiptGrid',
           	 height:380
            }]
        });
        this.callParent(arguments);
    }
});