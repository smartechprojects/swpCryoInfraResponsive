Ext.define('SupplierApp.view.supplier.SupplierPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.supplierPanel',
    border:false,
    frame:false,
    layout: 'fit',
    autoScroll : false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'supplierGrid'
           }]
        });
        this.callParent(arguments);
    }
 
});