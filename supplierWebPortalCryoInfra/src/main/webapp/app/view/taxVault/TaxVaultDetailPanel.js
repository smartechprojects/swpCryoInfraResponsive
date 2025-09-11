Ext.define('SupplierApp.view.taxVault.TaxVaultDetailPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.taxVaultDetailPanel',
    border:false,
    frame:false,
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    defaults: {
        margin: 5
    },
	initComponent: function () {
		 Ext.apply(this, {
	        	layout: {
	                type: 'hbox'
	            },   
	            items: [{
				            	xtype : 'taxVaultForm',
	            	//width:'45%'
				            	flex: 1,
					        	scrollable: true,
					        	minWidth: 250,  

	            },{
	           	xtype : 'panel',
	        	html : SuppAppMsg.purchaseTitle23,
	        	// width:'55%',
	        	flex: 2,
	        	maxWidth: 410, 
	        	scrollable: true,
	        	border:false,
	        	id:'fileListTaxVaultHtml',
	        	//margin: '2 0 0 15',
	        	fieldStyle: 'font-size:11px;color:#blue;padding-bottom:10px;'
	            }]
	        });
        this.callParent(arguments);
    }
 
});