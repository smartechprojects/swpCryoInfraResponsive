Ext.define('SupplierApp.view.plantAccess.PlantAccessRequestPanel', {
	extend: 'Ext.Panel',
	alias : 'widget.plantAccessRequestPanel',
	border : false,
	frame : false,
	style: 'border: solid #fff 1px',
	initComponent: function() {
		this.items = [{
			xtype:'container',
			layout: 'anchor',
			border:false,
			items: [{
				xtype: 'tabpanel',
				id: 'plantAccessRequestTabPanel',
				itemId: 'plantAccessRequestTabPanel',
				items:[{
					id: 'paRequestForm',
					itemId: 'paRequestForm',
					title: 'Datos Solicitante',//------------------ Pestaña Datos Solicitante -----------------------
					items:[{
						xtype: 'plantAccessRequestForm'
					}]
				},{
					id: 'paRequestDocForm',
					itemId: 'paRequestDocForm',
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
			layout: 'anchor',
			items: [{
				xtype:'plantAccessRequestGrid'
			}]
		}];

		this.callParent(arguments);
	}
});