Ext.define('SupplierApp.view.taxVault.TaxVaultPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.taxVaultPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'taxVaultGrid',
           	 height:430
            }]
        });
        this.callParent(arguments);
    }
 
});