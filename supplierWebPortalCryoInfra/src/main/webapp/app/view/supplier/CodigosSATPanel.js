Ext.define('SupplierApp.view.supplier.CodigosSATPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.codigosSATPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'codigosSATGrid',
           	 height:430
            }]
        });
        this.callParent(arguments);
    }
 
});