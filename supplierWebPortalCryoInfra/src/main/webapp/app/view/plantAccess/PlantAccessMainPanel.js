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
 // En el initComponent o después de crear el panel
    listeners: {
        afterrender: function(panel) {
            // Forzar la eliminación del espacio del tabpanel
            var tabPanel = Ext.getCmp('plantAccessMainTabPanel');
            if (tabPanel) {
                var tabBar = tabPanel.getTabBar();
                if (tabBar) {
                    tabBar.hide();
                    tabBar.setHeight(0);
                    tabBar.getEl().setStyle({
                        'display': 'none',
                        'height': '0',
                        'min-height': '0'
                    });
                }
                
                // Aplicar estilos al tabpanel completo
                tabPanel.getEl().setStyle({
                    'padding': '0',
                    'margin': '0',
                    'margin-top': '-10px' // ← USAR MARGIN NEGATIVO AQUÍ
                });
                
                var body = tabPanel.body;
                if (body) {
                    body.setStyle({
                        'padding': '0',
                        'margin': '0',
                        'border': 'none'
                    });
                }
                
                // También aplicar al panel interno (Solicitud)
                var solicitudPanel = tabPanel.items.items[0]; // Primera pestaña
                if (solicitudPanel && solicitudPanel.getEl()) {
                    solicitudPanel.getEl().setStyle({
                        'margin-top': '-10px', // ← Y AQUÍ TAMBIÉN
                        'padding': '0'
                    });
                }
            }
        }
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
