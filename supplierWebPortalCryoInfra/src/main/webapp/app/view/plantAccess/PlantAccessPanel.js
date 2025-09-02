Ext.define('SupplierApp.view.plantAccess.PlantAccessPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.plantAccessPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {
            items: [{
             id: 'paPlantAccessGrid',
             itemId: 'paPlantAccessGrid',
           	 xtype: 'plantAccessGrid',
           	 height:430
            }]
        });
        this.callParent(arguments);
    }
 
});