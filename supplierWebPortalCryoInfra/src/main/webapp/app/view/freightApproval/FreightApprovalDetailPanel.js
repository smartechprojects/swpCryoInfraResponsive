Ext.define('SupplierApp.view.freightApproval.FreightApprovalDetailPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.freightApprovalDetailPanel',
    border:false,
    frame:false,
   // width:1400,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
	initComponent: function () {
		 Ext.apply(this, {
	        	layout: {
	                type: 'vbox',
	                align: 'stretch'
	            },   
	            items: [{
	            	xtype: 'freightApprovalForm',
	            	//height:100
	            	flex: 0.3 

	            },{
	           	 xtype: 'freightApprovalDetailGrid',
	          // 	 height:300,
	           	flex: 0.7,
	           	 border:true
	            }]
	        });
        this.callParent(arguments);
    }
 
});