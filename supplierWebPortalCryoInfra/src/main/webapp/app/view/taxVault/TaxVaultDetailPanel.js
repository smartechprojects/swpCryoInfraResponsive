Ext.define('SupplierApp.view.taxVault.TaxVaultDetailPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.taxVaultDetailPanel',
    border:false,
    frame:false,
	initComponent: function () {
		 Ext.apply(this, {
	        	layout: {
	                type: 'hbox'
	            },   
	            items: [{
				            	xtype : 'taxVaultForm',
	            	width:'45%'

	            },{
	           	xtype : 'panel',
	        	html : SuppAppMsg.purchaseTitle23,
	        	 width:'55%',
	        	autoScroll: true,
	        	border:false,
	        	id:'fileListTaxVaultHtml',
	        	margin: '2 0 0 15',
	        	fieldStyle: 'font-size:11px;color:#blue;padding-bottom:10px;'
	            }]
	        });
        this.callParent(arguments);
    }
 
});