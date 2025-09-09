Ext.define('SupplierApp.view.company.CompanyPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.companyPanel',
    border:false,
    frame:false,
    layout: 'fit', 
    scrollable: true,
	initComponent: function () {
        Ext.apply(this, {
        	layout: {
                type: 'hbox',
                align: 'stretch'
            },   
            items: [{
            	xtype: 'companyForm',
            	flex:5
            },{
           	 xtype: 'companyGrid',
           	flex:5
            }]
        });
        this.callParent(arguments);
    }
 
});