Ext.define('SupplierApp.view.freightApproval.FreightApprovalPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.freightApprovalPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'freightApprovalGrid',
           	 height:430
            }]
        });
        this.callParent(arguments);
    }
 
});