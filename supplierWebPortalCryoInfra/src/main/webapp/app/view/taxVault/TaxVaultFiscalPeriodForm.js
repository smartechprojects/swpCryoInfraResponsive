Ext.define('SupplierApp.view.taxVault.TaxVaultFiscalPeriodForm',	{
	extend : 'Ext.form.Panel',
	alias : 'widget.taxVaultFiscalPeriodForm',
	border : false,
	frame : false,
	style : 'border: solid #ccc 1px',
	scrollable: true,
    bodyPadding: 10,
	initComponent : function() {
		var invYear=null;
		
		invYear = Ext.create('Ext.data.Store', {
            fields: ['optionName', 'value'],
            data: selectYeari()
        });
		
		Ext.define('yearCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.taxvaultFiscalPeriodsByYear,
    	    store: invYear,
    	    alias: 'widget.yearCombo',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    editable: false,
    	    displayField: 'optionName',
			//width:180,
    	    //labelWidth:100,
    	    valueField: 'value',
    	    //margin:'0 20 0 10',
    	    id: 'poFromDateTaxVault',
            itemId: 'poFromDateTaxVault',
            name:'poFromDateTaxVault',
           // flex: 0.1
    	});
	
		
		this.items = [
								{
									xtype : 'container',
									layout : 'vbox',
									defaults: {
						                anchor: '100%',
						                margin: '5 0'
						            },
									items : [
										,{
											xtype: 'yearCombo'
										}
//										,{
//											xtype: 'datefield',
//								            fieldLabel: SuppAppMsg.purchaseOrderHasta,
//								            id: 'poToDateTaxVault',
//								            itemId: 'poToDateTaxVault',
//								            name:'poToDateTaxVault',
//								            width:160,
//								            labelWidth:35,
//								            margin:'10 40 10 10'
//										}
										]
								} ];

						this.tbar = [
								{
									iconCls : 'icon-save',
									itemId : 'addeTaxVaultFiscPertBtn',
									id : 'addeTaxVaultFiscPerBtn',
									text : SuppAppMsg.taxvaultAddPeriod,
									action : 'addeTaxVaultFiscPerAct',
									cls: 'buttonStyle',
}
								];
						this.callParent(arguments);
					}

});

function selectYeari(){
	resp=[];
	for (var i =new Date().getFullYear() - 1; i <= new Date().getFullYear() +1; i++) {
		resp.push({
            value: i,
            optionName: i
        });
		}
		return resp;
	}
	
	