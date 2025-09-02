Ext.define('SupplierApp.view.taxVault.TaxVaultForm',	{
	extend : 'Ext.form.Panel',
	alias : 'widget.taxVaultForm',
	border : false,
	frame : false,
	style : 'border: solid #ccc 1px',
	autoScroll : true,
	initComponent : function() {
	
		this.items = [
								{
									xtype : 'container',
									layout : 'vbox',
									items : [
										{
											
											xtype: 'textfield',
								            fieldLabel: SuppAppMsg.taxvaultIssuerRFC,
								            id: 'rfcEmisorDet',
								            itemId: 'rfcEmisorDet',
								            name:'rfcEmisorDet',
								            width:300,
								            labelWidth:100,
								            readOnly: true,
								            margin:'5 10 0 10'
										},
										{
											xtype: 'textfield',
								            fieldLabel: SuppAppMsg.taxvaultreceiverRFC,
								            id: 'rfcReceptorDet',
								            itemId: 'rfcReceptorDet',
								            name:'rfcReceptorDet',
								            width:300,
								            labelWidth:100,
								            readOnly: true,
								            margin:'5 10 0 10'
										},
										{
											xtype: 'textfield',
								            fieldLabel: SuppAppMsg.taxvaulUser,
								            id: 'usuarioDet',
								            itemId: 'usuarioDet',
								            name:'usuarioDet',
								            width:300,
								            labelWidth:100,
								            readOnly: true,
								            margin:'5 10 0 10'
										}
										]
								} ];

//						this.tbar = [
//								{
//									iconCls : 'icon-save',
//									itemId : 'uploadTaxVaultRequestBtn',
//									id : 'uploadTaxVaultRequestBtn',
//									text : 'Enviar solicitud',
//									action : 'uploadTaxVaultRequestAct',
//}
//								];
						this.callParent(arguments);
					}

});