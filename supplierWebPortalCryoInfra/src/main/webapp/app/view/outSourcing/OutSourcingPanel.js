Ext.define('SupplierApp.view.outSourcing.OutSourcingPanel', {
	extend : 'Ext.Panel',
	alias : 'widget.outSourcingPanel',
	border : false,
	frame : false,
	initComponent : function() {
		Ext.apply(this, {
			items : [{
						xtype : 'outSourcingGrid',
						flex:1,
						height : 430
			        }] 
		});
		this.callParent(arguments);
	}
});