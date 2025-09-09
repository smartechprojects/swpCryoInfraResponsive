Ext.define('SupplierApp.view.udc.UdcPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.udcPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {
        	layout: {
                type: 'vbox',
                align: 'stretch'
            },   
            items: [{
            	xtype: 'udcForm',
            	flex:.25,
            	minHeight: 150
            },{
           	 xtype: 'udcGrid',
           	 flex:.75,
           	minHeight: 250
            }]
        });
        this.callParent(arguments);
    }
 
});