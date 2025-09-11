Ext.define('SupplierApp.view.taxVault.TaxVaultForm',	{
	extend : 'Ext.form.Panel',
	alias : 'widget.taxVaultForm',
	border : false,
	frame : false,
	style : 'border: solid #ccc 1px',
	scrollable : true,
	bodyPadding: 10,
	layout: { 
		type: 'vbox', 
		align: 'stretch' 
			},
	initComponent : function() {
	
		this.items = [
								{
									xtype : 'container',
									layout : 'vbox',
									defaults: {
							            anchor: '100%' // se ajusta al ancho disponible
							        },
									items : [
										{
											
											xtype: 'textfield',
								            fieldLabel: SuppAppMsg.taxvaultIssuerRFC,
								            id: 'rfcEmisorDet',
								            itemId: 'rfcEmisorDet',
								            name:'rfcEmisorDet',
								            //width:300,
								            //labelWidth:100,
								            readOnly: true,
								            //margin:'5 10 0 10'
								            margin:'5 0'
										},
										{
											xtype: 'textfield',
								            fieldLabel: SuppAppMsg.taxvaultreceiverRFC,
								            id: 'rfcReceptorDet',
								            itemId: 'rfcReceptorDet',
								            name:'rfcReceptorDet',
								            //width:300,
								            //labelWidth:100,
								            readOnly: true,
								            //margin:'5 10 0 10'
								            margin:'5 0'
										},
										{
											xtype: 'textfield',
								            fieldLabel: SuppAppMsg.taxvaulUser,
								            id: 'usuarioDet',
								            itemId: 'usuarioDet',
								            name:'usuarioDet',
								            //width:300,
								            //labelWidth:100,
								            readOnly: true,
								            //margin:'5 10 0 10'
								            margin:'5 0'
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