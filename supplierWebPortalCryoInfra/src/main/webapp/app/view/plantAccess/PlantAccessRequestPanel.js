Ext.define('SupplierApp.view.plantAccess.PlantAccessRequestPanel', {
	extend: 'Ext.Panel',
	alias : 'widget.plantAccessRequestPanel',
	border : false,
	frame : false,
	style: 'border: solid #fff 1px',
	layout: {
        type: 'vbox',
        align: 'stretch'
    },
	initComponent: function() {
		this.items = [{
			xtype:'container',
			flex: 2.2,
			layout: 'fit',
			border:false,
			items: [{
				xtype: 'tabpanel',
				id: 'plantAccessRequestTabPanel',
				itemId: 'plantAccessRequestTabPanel',
				items:[{
					id: 'paRequestForm',
					itemId: 'paRequestForm',
					layout: 'fit',
					title: 'Datos Solicitante',//------------------ Pestaña Datos Solicitante -----------------------
					items:[{
						xtype: 'plantAccessRequestForm'
					}]
				},{
					id: 'paRequestDocForm',
					itemId: 'paRequestDocForm',
					layout: 'fit',
					title: 'Documentos Solicitante',//------------------ Pestaña Documentos Solicitante -----------------------
					items:[{
						xtype: 'plantAccessRequestDocForm'
					}]
				}]
			}]
		},{
			id: 'paRequestGrid',
			itemId: 'paRequestGrid',
			xtype:'container',
			flex: 1,
			layout: 'fit',
			items: [{
				xtype:'plantAccessRequestGrid'
			}]
		}];

		this.callParent(arguments);
	}
});