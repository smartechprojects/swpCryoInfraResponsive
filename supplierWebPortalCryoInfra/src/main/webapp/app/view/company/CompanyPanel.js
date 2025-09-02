Ext.define('SupplierApp.view.company.CompanyPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.companyPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {
        	layout: {
                type: 'hbox',
                align: 'stretch'
            },   
            items: [{
            	xtype: 'companyForm',
            	flex:4,
            	height:400

            },{
           	 xtype: 'companyGrid',
           	flex:6
            }]
        });
        this.callParent(arguments);
    }
 
});