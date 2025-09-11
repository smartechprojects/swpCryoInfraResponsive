Ext.define('SupplierApp.view.taxVault.TaxVaultFiscalPeriodPanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.taxVaultFiscalPeriodPanel',
    border:false,
    frame:false,
    scrollable: true,
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
				    xtype : 'taxVaultFiscalPeriodForm',
	            	//width:550,
	            	//height:70
				    scrollable: true,
				    flex: 1 
	            },{
		           	xtype : 'panel',
		        	html : SuppAppMsg.taxvaultfiscalPeriods,
		        	//width:600,
		        	//height:160,
		        	flex: 1,
		        	scrollable: true,
		        	scroll: true,
		        	//border:false,
		        	id:'ListTaxVaulFiscalPeriodtHtml',
		        	margin: '2 0 0 15',
		        	fieldStyle: 'font-size:11px;color:#blue;padding-bottom:10px;'
	            }]
	        });
        this.callParent(arguments);
    }
 
});