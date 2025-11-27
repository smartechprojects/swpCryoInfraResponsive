Ext.define('SupplierApp.view.udc.UdcPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.udcPanel',
    border:false,
    frame:false,
    layout: 'fit',
    autoScroll : false,
	initComponent: function () {
        Ext.apply(this, {
        	layout: {
                type: 'vbox',
                align: 'stretch'
            },   
            items: [{
            	xtype: 'udcForm',
            	flex:.25
            },{
           	 xtype: 'udcGrid',
           	 flex:1
            }]
        });
        this.callParent(arguments);
    }
 
});