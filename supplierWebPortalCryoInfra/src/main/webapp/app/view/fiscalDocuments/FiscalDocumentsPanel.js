Ext.define('SupplierApp.view.fiscalDocuments.FiscalDocumentsPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.fiscalDocumentsPanel',
    border:false,
    frame:false,
    layout: 'fit',
    scrollable: true,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'fiscalDocumentsGrid',
           	 flex:1,
            }]
        });
        this.callParent(arguments);
    }
 
});