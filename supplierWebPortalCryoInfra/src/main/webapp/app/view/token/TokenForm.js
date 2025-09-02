Ext.define('SupplierApp.view.token.TokenForm' ,{
	extend: 'Ext.form.Panel',
	alias : 'widget.tokenForm',
	border:false,
	  initComponent: function() {		  
			this.items= [{
				xtype : 'container',
				layout : 'vbox',
				style : 'border-bottom: 1px dotted #fff;background-color: #D5D8DC;padding-bottom:10px',
				defaults : {
					labelWidth : 150,
					margin : '5 5 5 5',
					xtype : 'textfield',
					width:250
				},
				items : [ {
					xtype: 'hidden',
					name: 'id',
					hidden:true
				},{
     				xtype : 'hidden',
     				name : 'createdBy',
     				id:'createdBy'
     			},{
     				xtype : 'hidden',
     				name : 'creationDate',
     				id:'creationDate',
     				format : 'd-M-Y',
     			},{
     				xtype : 'hidden',
     				name : 'expirationDate',
     				id:'creationDate',
     				format : 'd-M-Y',
     			},{
     				name : 'assigned',
     				xtype : 'checkbox',
     				hidden:true
     			},{
					name: 'company',
					hidden:true
				},{
					fieldLabel: window.navigator.language.startsWith("es", 0)? 'Nombre del Proveedor':'Name Supplier',
					name: 'registerName',
					width : 500,listeners:{
						change: function(field, newValue, oldValue){
							field.setValue(newValue.toUpperCase());
						},
						afterrender: function(field) {
					        field.focus(true);
					    }
				    },
				    allowBlank:false
				},{
					fieldLabel: window.navigator.language.startsWith("es", 0)? 'Correo electronico':'Email',
					vtype : 'email',
					name: 'email',
					width : 500,
					allowBlank:false
				},{
					xtype : 'checkbox',
					fieldLabel : window.navigator.language.startsWith("es", 0)? 'Registro activo?':'Active register?',
					name : 'enabled',
					width : 300,
					checked: true
				}]
			}];
			
			this.tbar=[      
				 {
			    	  iconCls: 'icon-save',
			    	  id: 'saveTokenRegisterId',
			    	  text: window.navigator.language.startsWith("es", 0)? 'Enviar registro':'Send register',
			    	  action: 'save',
			    	  cls: 'buttonStyle'
			      }, '-',{
			    	  iconCls: 'icon-accept',
			    	  id: 'updateTokenRegisterId',
			    	  text: window.navigator.language.startsWith("es", 0)? 'Renovar registro':'Renew register',
			    	  action: 'update',
			    	  cls: 'buttonStyle',
			    	  disabled : true
			      }, '-',{
			    	  iconCls: 'icon-add',
			    	  id: 'newTokenRegisterId',
			    	  text: window.navigator.language.startsWith("es", 0)? 'Nuevo registro':'New register',
			    	  action: 'new',
			    	  cls: 'buttonStyle',
			    	  margin : '5 0 10 0'
			      }];
		  this.callParent(arguments);	    
	  }

});