Ext.define('SupplierApp.view.purchaseOrder.LogDataPanel', {
	extend : 'Ext.Panel',
	alias : 'widget.logDataPanel',
	border : false,
	frame : false,
	initComponent : function() {
		Ext.apply(this, {
			items : [{
						xtype : 'logDataGrid',
						flex:1,
						height : 430
			        }] 
		});
		this.callParent(arguments);
	}
});