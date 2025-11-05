Ext.define('SupplierApp.view.taxVault.TaxVaultPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.taxVaultPanel',
    border:false,
    frame:false,
    layout: 'fit',
    autoScroll : false,
    responsiveConfig: {
        'width < 800': {   // pantallas chicas
            layout: 'vbox'
        },
        'width >= 800': {  // pantallas grandes
            layout: 'fit'
        }
    },
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'taxVaultGrid',
            }]
        });
        this.callParent(arguments);
    }
 
});