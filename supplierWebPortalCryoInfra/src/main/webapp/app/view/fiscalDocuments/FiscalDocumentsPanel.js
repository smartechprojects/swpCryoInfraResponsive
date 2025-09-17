Ext.define('SupplierApp.view.fiscalDocuments.FiscalDocumentsPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.fiscalDocumentsPanel',
    border:false,
    frame:false,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'fiscalDocumentsGrid',
           	 //height:430
           	 flex:1,
            }]
        });
        this.callParent(arguments);
    }
 
});