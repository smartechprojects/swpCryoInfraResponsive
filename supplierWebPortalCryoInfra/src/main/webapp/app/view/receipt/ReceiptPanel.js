Ext.define('SupplierApp.view.receipt.ReceiptPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.receiptPanel',
    border:false,
    frame:false,
    scrollable: true,
	initComponent: function () {
        Ext.apply(this, {
        	layout: {
                type: 'vbox',
                align: 'stretch'
            },   
            items: [{
            	xtype: 'receiptForm',
            	height:80,
            	flex: 0,
            	width: '100%',
            	scrollable: true
            },{
           	 xtype: 'receiptGrid',
           	 //height:380
           	lex: 0,
        	width: '100%',
        	scrollable: true
            }]
        });
        this.callParent(arguments);
    }
});