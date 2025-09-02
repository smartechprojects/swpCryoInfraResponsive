Ext.define('SupplierApp.view.purchaseOrder.PurchaseOrderDetailPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.purchaseOrderDetailPanel',
    border:false,
    frame:false,
    width:1400,
	initComponent: function () {
		 Ext.apply(this, {
	        	layout: {
	                type: 'vbox',
	                align: 'stretch'
	            },   
	            items: [{
	            	xtype: 'purchaseOrderForm',
	            	height:160

	            },{
	           	 xtype: 'purchaseOrderDetailGrid',
	           	 height:270,
	           	 border:true
	            }, {
					xtype : 'panel',
					html : SuppAppMsg.purchaseTitle23,
					height:70,
					autoScroll: true,
					border:false,
					id:'fileListHtml',
					margin: '2 0 0 15',
					fieldStyle: 'font-size:11px;color:#blue;padding-bottom:10px;'
					
				}]
	        });
        this.callParent(arguments);
    }
 
});