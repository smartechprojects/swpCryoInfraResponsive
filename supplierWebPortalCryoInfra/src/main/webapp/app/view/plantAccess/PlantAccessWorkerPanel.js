Ext.define('SupplierApp.view.plantAccess.PlantAccessWorkerPanel', {
	extend: 'Ext.panel.Panel',
	alias : 'widget.plantAccessWorkerPanel',
	border : false,
	frame : false,
	style: 'border: solid #fff 1px',
	layout: {
        type: 'vbox', // Layout vertical
        align: 'stretch' // Que los items ocupen todo el ancho
    },
	initComponent: function() {
		
		this.items = [{
			id: 'paWorkerForm',
			itemId: 'paWorkerForm',
			xtype:'container',
			layout: 'fit',
			items: [{
				xtype: 'plantAccessWorkerForm',
			}]			
		},{
			id: 'paWorkerGrid',
			itemId: 'paWorkerGrid',
			xtype:'container',
			layout: 'fit',
			items: [{
				xtype: 'plantAccessWorkerGrid',
			}]
		}]
		
		this.callParent(arguments);
	}
});