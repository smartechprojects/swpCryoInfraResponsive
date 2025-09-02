Ext.define('SupplierApp.view.company.CompanyForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.companyForm',
	border : false,
	frame : false,
	style: 'border: solid #ccc 1px',
	autoScroll : true,
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
			    	layout:'vbox',
			    	defaults:{labelWidth : 120, width : 350, labelAlign: 'left', xtype:'textfield', margin:'10 0 0 10'},
			    	items:[
			    		{
							xtype:'container',
							layout:'hbox',
							width : 500,
							margin:'10 0 0 10',
							items:[
								{
									xtype:'textfield',
									fieldLabel : SuppAppMsg.supplierForm6,
									name : 'company',
									id : 'idCompany',
									labelWidth : 120,
									width : 250,
									allowBlank : false,
									listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
											validarInputIssuer(newValue);
										}
									}
								},{
									xtype:'textfield',
									id : 'resultadoRfc',
									margin:'0 0 0 15',
									width : 200,
									readOnly:true,
									fieldStyle: 'border:none;background-image: none;font-weight:bold;',
								}
							]
						},{
							xtype:'textfield',
							fieldLabel : SuppAppMsg.suppliersName,
							name : 'companyName',
							width : 550,
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
							width : 550,
							allowBlank : false
						},{
							xtype : 'checkbox',
							fieldLabel : SuppAppMsg.companys2,
							name : 'active',
							width : 300,
							checked: false
						},/*{
							xtype:'container',
							layout:'hbox',
							width : 500,
							margin:'15 0 0 10', 
							defaults : {
								labelWidth : 80,
								xtype : 'textfield',
							},
							items:[
									{
							        	xtype : 'textfield',
										fieldLabel : 'Logo',
										name : 'logoFileRef',
										id:'logoFileRef',
										width:400,
										readOnly:true
								    },{
										xtype: 'button',
										width:90,
										height:22,
										itemId : 'loadLogoFileRef',
										id : 'loadLogoFileRef',
										text : 'Cargar',
										action : 'loadLogoFileRef',
										cls: 'buttonStyle',
										margin: '0 0 0 10'
									}]
						},*/{
							xtype : 'displayfield',
							value : SuppAppMsg.supplierFile+' .PFX',
							height:20,
							width : 500,
							margin: '15 0 0 10',
							colspan:3,
							id:'pfxTitle',
							fieldStyle: 'font-weight:bold'
						},{
							xtype:'container',
							layout:'hbox',
							width : 500,
							defaults : {
								labelWidth : 80,
								xtype : 'textfield'
							},
							items:[
									{
							        	xtype : 'textfield',
										fieldLabel : SuppAppMsg.supplierFile+' .pfx',
										name : 'taxFileRef',
										id:'taxFileRef',
										width:400,
										readOnly:true
								    },{
										xtype: 'button',
										width:90,
										height:22,
										itemId : 'loadTaxFileRef',
										id : 'loadTaxFileRef',
										text : SuppAppMsg.supplierLoad,
										action : 'loadTaxFileRef',
										cls: 'buttonStyle',
										margin: '0 0 0 10'
									}]
						}, {
							fieldLabel : 'Password .pfx',
							name : 'secretPass',
							width : 300,
							labelWidth : 80,
							inputType: 'password',
							allowBlank : true
						},{
							xtype : 'displayfield',
							value : SuppAppMsg.companys4,
							height:20,
							id:'cerTitle',
							margin: '15 0 0 10',
							colspan:3,
							fieldStyle: 'font-weight:bold'
						},{
							xtype:'container',
							layout:'hbox',
							width : 500,
							defaults : {
								labelWidth : 80,
								xtype : 'textfield'
							},
							items:[
									{
							        	xtype : 'textfield',
										fieldLabel : SuppAppMsg.supplierFile+' .cer',
										name : 'cerFileRef',
										id:'cerFileRef',
										width:400,
										readOnly:true
								    },{
										xtype: 'button',
										width:90,
										height:22,
										itemId : 'loadCerFileRef',
										id : 'loadCerFileRef',
										text : SuppAppMsg.companys5,
										action : 'loadCerFileRef',
										cls: 'buttonStyle',
										margin: '0 0 0 10'
									}]
						},{
							xtype:'container',
							layout:'hbox',
							width : 500,
							defaults : {
								labelWidth : 80,
								xtype : 'textfield'
							},
							items:[
									{
							        	xtype : 'textfield',
										fieldLabel : SuppAppMsg.supplierFile+' .key',
										name : 'keyFileRef',
										id:'keyFileRef',
										width:400,
										readOnly:true
								    },{
										xtype: 'button',
										width:90,
										height:22,
										itemId : 'loadKeyFileRef',
										id : 'loadKeyFileRef',
										text : SuppAppMsg.companys5,
										action : 'loadKeyFileRef',
										cls: 'buttonStyle',
										margin: '0 0 0 10'
									}]
						}, {
							fieldLabel : 'Password',
							name : 'secretPassCer',
							width : 300,
							labelWidth : 80,
							inputType: 'password',
							allowBlank : true
						}
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