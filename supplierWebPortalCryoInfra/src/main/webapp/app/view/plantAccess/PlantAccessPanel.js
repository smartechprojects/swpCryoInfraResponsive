Ext.define('SupplierApp.view.plantAccess.PlantAccessPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.plantAccessPanel',
    border:false,
    frame:false,
    scrollable: true,
	initComponent: function () {
        Ext.apply(this, {
            items: [{
             id: 'paPlantAccessGrid',
             itemId: 'paPlantAccessGrid',
           	 xtype: 'plantAccessGrid',
           	 //height:430
           	flex: 0,
        	width: '100%',
        	scrollable: true
            }]
        });
        this.callParent(arguments);
    }
 
});