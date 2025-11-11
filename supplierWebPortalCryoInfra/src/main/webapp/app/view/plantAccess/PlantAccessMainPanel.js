Ext.define('SupplierApp.view.plantAccess.PlantAccessMainPanel', {
	extend: 'Ext.Panel',
	alias : 'widget.plantAccessMainPanel',
	id: 'paMainPanel',
	itemId: 'paMainPanel',
	border : false,
	frame : false,
	autoScroll : true,
	layout: {
        type: 'vbox',
        align: 'stretch'
    },
	initComponent: function() {
		this.items = [{
        	xtype: 'tabpanel',
			id: 'plantAccessMainTabPanel',
			itemId: 'plantAccessMainTabPanel',
        	layout: 'fit',
        	flex: 1, 
    		plain: true,
    		border:false,
    		cls: 'hide-tabs hide-border',
    		items:[{
    			title: 'Solicitud',//------------------ Pestaña Solicitud -----------------------    			
    			autoScroll : true,
    			layout: 'fit',
    			items:[{
    				xtype: 'plantAccessRequestPanel'
    			}]
    		},{
    			title: 'Trabajador',//------------------ Pestaña Trabajador ---------------------
    			autoScroll : true,
    			layout: 'fit',
    			items:[{
    				xtype: 'plantAccessWorkerPanel'
    			}]            			
    		}]
		}]
		
		this.buttons = [{
	        text: 'Agregar Trabajador',
			action : 'plantAccessAddWorker',				
			itemId : 'plantAccessAddWorker',
			id : 'plantAccessAddWorker',
			cls: 'buttonStyle',
	        margin: '0 5 5 0'
		},{
	        text: 'Finalizar Trabajador',
			action : 'plantAccessFinishWorker',				
			itemId : 'plantAccessFinishWorker',
			id : 'plantAccessFinishWorker',
	        margin: '0 5 5 0',
	        cls: 'buttonStyle',
	        hidden: true
		},{
	        text: 'Ver Solicitud',
			action : 'plantAccessShowRequest',				
			itemId : 'plantAccessShowRequest',
			id : 'plantAccessShowRequest',
	        margin: '0 5 5 0',
	        cls: 'buttonStyle',
	        hidden: true
		}];
		
		this.tbar = [
			{
				iconCls : 'icon-save',
				text : SuppAppMsg.plantAccess4,
				action : 'savePlantAccessRequest',				
				itemId : 'savePlantAccessRequest',
				id : 'savePlantAccessRequest',
				cls: 'buttonStyle'
			},{
				iconCls:'icon-document',
				text: SuppAppMsg.plantAccess5,
				action : 'showPlantAccessRequestFiles',
				itemId: 'showPlantAccessRequestFiles',
				id: 'showPlantAccessRequestFiles',
			    cls: 'buttonStyle'
			}
		];
		
		this.callParent(arguments);
	}
});
