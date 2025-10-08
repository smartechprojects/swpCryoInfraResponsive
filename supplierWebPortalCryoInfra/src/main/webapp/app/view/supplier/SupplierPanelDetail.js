Ext.define('SupplierApp.view.supplier.SupplierPanelDetail' ,{
    extend: 'Ext.Panel',
    alias : 'widget.supplierPanelDetail',
    id:'supplierPanelDetail',
    border:false,
    frame:false,
 	scrollable: true,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'supplierForm',
           	 //height:490,
          
            }]
        });
        this.callParent(arguments);
    }
 
});