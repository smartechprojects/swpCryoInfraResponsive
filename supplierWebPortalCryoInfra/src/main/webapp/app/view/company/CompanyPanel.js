Ext.define('SupplierApp.view.company.CompanyPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.companyPanel',
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
            	xtype: 'companyForm',
            	flex:1,
            	border:true,
            	itemId: 'companyForm'
            },{
           	 xtype: 'companyGrid',
           	flex:2,
           	border:true,
        	itemId: 'companyGrid' 
            }]
        });
        this.callParent(arguments);
     
        // Ajustar layout responsivo
        this.on('afterrender', function() {
            var me = this;
            var updateResponsiveLayout = function() {
                var viewportWidth = Ext.Element.getViewportWidth();
                var form = me.down('#companyForm');
                var grid = me.down('#companyGrid');
                
                if (form && grid) {
                	debugger
                	if (viewportWidth < 800) {
                	    form.setFlex(2.5);
                	    grid.setFlex(1);
                	} else if (viewportWidth < 1200) {
                	    form.setFlex(1.8);
                	    grid.setFlex(2);
                	} else if (viewportWidth >= 1200 && viewportWidth <= 1500) {
                	    form.setFlex(0.8);
                	    grid.setFlex(2);
                	} else { // viewportWidth > 1500
                	    form.setFlex(1);
                	    grid.setFlex(2);
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