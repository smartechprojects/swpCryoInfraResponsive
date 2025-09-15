Ext.define('SupplierApp.view.freightApproval.FreightApprovalPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.freightApprovalPanel',
    border:false,
    frame:false,
    layout: 'fit',
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'freightApprovalGrid',
           	// height:430
           	flex: 1
            }]
        });
        this.callParent(arguments);
    }
 
});