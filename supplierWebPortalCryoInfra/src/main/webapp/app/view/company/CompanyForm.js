Ext.define('SupplierApp.view.company.CompanyForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.companyForm',
	border : false,
	frame : false,
	style: 'border: solid #ccc 1px',
	autoScroll : true,
	layout: {
        type: 'vbox',
        align: 'stretch'
    },
	initComponent : function() {
		this.items = [ {
			name:'id',
			xtype:'hidden'
			
		},{
			name:'attachId',
			xtype:'hidden'
			
		},
			  		{
							xtype:'container',
							layout:'hbox',
							//width : 500,
							margin:'10 0 0 10',
							width: '100%',
							defaults : {
								margin : '0 0 1 0',
								xtype : 'textfield',
								labelAlign: 'left',
								width: '65%',
							},
							items:[
								{
									xtype:'textfield',
									fieldLabel : SuppAppMsg.supplierForm6,
									name : 'company',
									itemId : 'idCompany',
									labelStyle: 'padding-top:4px;padding-bottom:0px;',
									//width : 250,
									allowBlank : false,
									listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
											validarInputIssuer(newValue,field);
										}
									}
								},{
									xtype:'displayfield',
									fieldCls: 'no-border-displayfield',
									name : 'resultadoRfc',
									itemId : 'resultadoRfc',
									margin:'0 0 0 10',
									//width : 200,
									readOnly:true,
									fieldStyle: 'border:none;background-image: none;font-weight:bold;',
								}
							]
						},{
							xtype:'container',
							layout:'vbox',
							//width : 500,
							margin:'10 0 0 10',
							width: '100%',
							defaults : {
								margin : '0 0 6 0',
								xtype : 'textfield',
								labelAlign: 'left',
								width: '64%',
							},
							items:[
								{
							xtype:'textfield',
							fieldLabel : SuppAppMsg.suppliersName,
							name : 'companyName',
							margin:'4 0 0 0',
							labelStyle: 'padding-top:0px;padding-bottom:0px;',
							//width : 550,
							allowBlank : false,
							listeners:{
								change: function(field, newValue, oldValue){
									field.setValue(newValue.toUpperCase());
								}
							}
						},{
							xtype:'textfield',
							fieldLabel : SuppAppMsg.companys3,
							name : 'notificationEmail',
							labelStyle: 'padding-top:0px;padding-bottom:0px;',
							margin:'10 0 10 0',
							//width : 550,
							allowBlank : false
						},{
							xtype : 'checkbox',
							fieldLabel : SuppAppMsg.companys2,
							padding:0,
							name : 'active',
							margin : '0 0 0 0',
							//width : 300,
							labelStyle: 'padding-top:0px;padding-bottom:0px;', 
						    boxLabelCls: 'compact-checkbox',    
						    //cls: 'compact-checkbox-field' ,
							checked: false,
							itemId: 'activeCheckbox', 
							inputValue: 'true',      // valor que se guarda cuando está marcado
							uncheckedValue: 'false'  // valor que se guarda cuando no está marcado							 
						}
						
						
				    	]
						},{
						    xtype: 'container',
						    layout: 'vbox',
						    margin: '0 0 0 10',
						    width: '100%',
						    defaults: {
						        margin: '0 0 10 0',
						        xtype: 'textfield',
						        labelAlign: 'left',
						        width: '64%',
						    },
						    items: [

						        {
						            xtype: 'displayfield',
						            value: SuppAppMsg.supplierFile + ' .PFX',
						            margin: '0 0 0 0',
						            colspan: 3,
						            id: 'pfxTitle',
						            fieldStyle: 'font-weight:bold'
						        },

						        // CONTENEDOR PARA EL TEXTFIELD Y EL BOTÓN
						        {
						            xtype: 'container',
						            layout: 'hbox',
						            width: '95%',  // mismo ancho que los defaults
						            items: [
						                {
						                    xtype: 'textfield',
						                    fieldLabel: SuppAppMsg.supplierFile + ' .pfx',
						                    name: 'taxFileRef',
						                    id: 'taxFileRef',
						                    readOnly: true,
						                    width: '69%',

						                },
						                {
						                    xtype: 'button',
						                    itemId: 'loadTaxFileRef',
						                    id: 'loadTaxFileRef',
						                    text: SuppAppMsg.supplierLoad,
						                    action: 'loadTaxFileRef',
						                    cls: 'buttonStyle',
						                    margin: '0 0 0 10',
						                    width: '15%',
						                }
						            ]
						        }			        
						    ]
						},{
							xtype:'container',
							layout:'vbox',
							//width : 500,
							margin:'4 0 0 10',
							width: '100%',
							defaults : {
								margin : '0 0 6 0',
								xtype : 'textfield',
								labelAlign: 'left',
								width: '64%',
							},
							items:[
								{
						            fieldLabel: 'Password .pfx',
						            name: 'secretPass',
						            inputType: 'password',
						            allowBlank: true
						        }
						
						
				    	]
						},{
						    xtype: 'container',
						    layout: 'vbox',
						    margin: '0 0 0 10',
						    width: '100%',
						    defaults: {
						        margin: '0 0 10 0',
						        xtype: 'textfield',
						        labelAlign: 'left',
						        width: '64%',
						    },
						    items: [

						        {
						            xtype: 'displayfield',
						            value: SuppAppMsg.companys4,
						            margin: '0 0 5 0',
						            colspan: 3,
						            id: 'cerTitle',
						            fieldStyle: 'font-weight:bold'
						        },

						        // CONTENEDOR PARA EL TEXTFIELD Y EL BOTÓN
						        {
						            xtype: 'container',
						            layout: 'hbox',
						            width: '95%',  // mismo ancho que los defaults
						            items: [
						                {
						                    xtype: 'textfield',
						                    fieldLabel: SuppAppMsg.supplierFile + ' .cer',
						                    name: 'cerFileRef',
						                    id: 'cerFileRef',
						                    readOnly: true,
						                    width: '69%',

						                },
						                {
						                    xtype: 'button',
						                    itemId: 'loadCerFileRef',
						                    id: 'loadCerFileRef',
						                    text: SuppAppMsg.companys5,
						                    action: 'loadCerFileRef',
						                    cls: 'buttonStyle',
						                    margin: '0 0 0 10',
						                    width: '15%',
						                }
						            ]
						        },{
						            xtype: 'container',
						            layout: 'hbox',
						            margin: '5 0 0 0',
						            width: '95%',  // mismo ancho que los defaults
						            items: [
						                {
						                    xtype: 'textfield',
						                    fieldLabel: SuppAppMsg.supplierFile + ' .key',
						                    name: 'keyFileRef',
						                    id: 'keyFileRef',
						                    readOnly: true,
						                    width: '69%',

						                },
						                {
						                    xtype: 'button',
						                    itemId: 'loadKeyFileRef',
						                    id: 'loadKeyFileRef',
						                    text: SuppAppMsg.companys5,
						                    action: 'loadKeyFileRef',
						                    cls: 'buttonStyle',
						                    margin: '0 0 0 10',
						                    width: '15%',
						                }
						            ]
						        },{
									xtype:'container',
									layout:'vbox',
									//width : 500,
									margin:'15 0 0 0',
									width: '100%',
									defaults : {
										margin : '0 0 6 0',
										xtype : 'textfield',
										labelAlign: 'left',
										width: '64%',
									},
									items:[
										{
								            fieldLabel: 'Password',
								            name: 'secretPassCer',
								            inputType: 'password',
								            allowBlank: true
								        }
								
								
						    	]
								},

						        
						    ]
						}


			];
			

		this.tbar = [ {
			iconCls : 'icon-save',
			companyId : 'saveCompany',
			id : 'saveCompany',
			text : SuppAppMsg.usersSave,
			action : 'saveCompany',
			cls: 'buttonStyle'
		}, '-', {
			iconCls : 'icon-accept',
			companyId : 'updateCompany',
			id : 'updateCompany',
			text : SuppAppMsg.usersUpdate,
			action : 'updateCompany',
			disabled : true,
			cls: 'buttonStyle'
		}, '-', {
			iconCls : 'icon-add',
			companyId : 'companyNew',
			text : SuppAppMsg.usersNew,
			action : 'companyNew',
			margin : '5 0 10 0',
			cls: 'buttonStyle'
		} ];
		
		this.callParent(arguments);
	}

});