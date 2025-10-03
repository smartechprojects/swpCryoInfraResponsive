Ext.define('SupplierApp.view.plantAccess.PlantAccessWorkerPanel', {
	extend: 'Ext.Panel',
	alias : 'widget.plantAccessWorkerPanel',
	border : false,
	frame : false,
	style: 'border: solid #fff 1px',
	autoScroll : true,
	initComponent: function() {
		
		this.items = [{
			id: 'paWorkerForm',
			itemId: 'paWorkerForm',
			xtype:'container',
			layout: 'fit',
			autoScroll : true,
			items: [{
				xtype: 'plantAccessWorkerForm',
			}]			
		},{
			id: 'paWorkerGrid',
			itemId: 'paWorkerGrid',
			xtype:'container',
			layout: 'fit',
			autoScroll : true,
			items: [{
				xtype: 'plantAccessWorkerGrid',
			}]
		}]
		
		this.callParent(arguments);
	}
});