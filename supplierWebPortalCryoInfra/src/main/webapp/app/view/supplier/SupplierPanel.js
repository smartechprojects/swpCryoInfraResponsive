Ext.define('SupplierApp.view.supplier.SupplierPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.supplierPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'supplierGrid',
           	 height:430
            }]
        });
        this.callParent(arguments);
    }
 
});