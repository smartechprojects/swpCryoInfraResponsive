Ext.define('SupplierApp.view.approval.ApprovalPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.approvalPanel',
    border:false,
    frame:false,
    layout: 'fit',
    autoScroll : false,
	initComponent: function () {
        Ext.apply(this, {  
            items: [{
           	 xtype: 'approvalGrid'
            }]
        });
        this.callParent(arguments);
    }
 
});