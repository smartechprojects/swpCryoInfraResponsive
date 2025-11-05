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
            	flex:.3
            },{
           	 xtype: 'udcGrid',
           	 flex:.7
            }]
        });
        this.callParent(arguments);
    }
 
});