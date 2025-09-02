Ext.define('SupplierApp.view.approval.ApprovalSearchPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.approvalSearchPanel',
    border:false,
    frame:false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'approvalSearchGrid',
           	 height:430
            }]
        });
        this.callParent(arguments);
    }
 
});