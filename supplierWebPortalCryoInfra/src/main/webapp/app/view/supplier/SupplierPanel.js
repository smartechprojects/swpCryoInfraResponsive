Ext.define('SupplierApp.view.supplier.SupplierPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.supplierPanel',
    border:false,
    frame:false,
    autoScroll : true,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'supplierGrid',
             layout: 'fit'
            }]
        });
        this.callParent(arguments);
    }
 
});