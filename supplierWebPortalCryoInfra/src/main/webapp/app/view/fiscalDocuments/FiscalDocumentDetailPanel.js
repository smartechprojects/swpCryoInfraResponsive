Ext.define('SupplierApp.view.fiscalDocuments.FiscalDocumentDetailPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.fiscalDocumentDetailPanel',
    border:false,
    frame:false,
    width:1700,
	initComponent: function () {
		 Ext.apply(this, {
 	            items: [{
	            	xtype: 'fiscalDocumentForm',
	            	height:515
	            }/*,{
					xtype : 'displayfield',
					value : SuppAppMsg.purchaseTitle23,
					height:180,
					id:'fileListHtmlInvoice',
					autoScroll: true,
					margin: '0 0 0 15',
					fieldStyle: 'font-size:12px;color:#blue;padding-bottom:10px;'
				}*/]
	        });
        this.callParent(arguments);
    }
 
});