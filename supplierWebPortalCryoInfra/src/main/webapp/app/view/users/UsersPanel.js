Ext.define('SupplierApp.view.users.UsersPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.usersPanel',
    border:false,
    frame:false,
    layout: 'fit',
    autoScroll : false,
	initComponent: function () {
        Ext.apply(this, {
        	layout: {
                type: 'hbox',
                align: 'stretch'
            },   
            items: [{
            	xtype: 'usersForm',
            	flex:.25,
            	border:true,
            	itemId: 'usersForm' 
            },{
           	 xtype: 'usersGrid',
           	flex:.75,
        	border:true,
        	itemId: 'usersGrid' 
            }]
        });
        this.callParent(arguments);
        
        // Ajustar layout responsivo
        this.on('afterrender', function() {
            var me = this;
            var updateResponsiveLayout = function() {
                var viewportWidth = Ext.Element.getViewportWidth();
                var form = me.down('#usersForm');
                var grid = me.down('#usersGrid');
                
                if (form && grid) {
                	
                    if (viewportWidth < 1200) {
                    	form.setFlex(0.5);
                        grid.setFlex(0.5);
                    } else if (viewportWidth < 1600) {
                        form.setFlex(0.25);
                        grid.setFlex(0.75);
                    } else if (viewportWidth < 700) {
                        form.setFlex(0.5);
                        grid.setFlex(0.5);
                    }else{
                    	form.setFlex(0.25);
                        grid.setFlex(0.75);
                    }
                    // CORRECCIÓN: Usar updateLayout() en lugar de doLayout()
                    me.updateLayout();
                }
            };
            
            updateResponsiveLayout();
            Ext.on('resize', updateResponsiveLayout, this);
        });
    }
 
});