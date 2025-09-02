Ext.define('SupplierApp.view.freightApproval.FreightApprovalDetailPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.freightApprovalDetailPanel',
    border:false,
    frame:false,
    width:1400,
	initComponent: function () {
		 Ext.apply(this, {
	        	layout: {
	                type: 'vbox',
	                align: 'stretch'
	            },   
	            items: [{
	            	xtype: 'freightApprovalForm',
	            	height:100

	            },{
	           	 xtype: 'freightApprovalDetailGrid',
	           	 height:300,
	           	 border:true
	            }]
	        });
        this.callParent(arguments);
    }
 
});