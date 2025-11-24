Ext.define('SupplierApp.view.token.TokenForm' ,{
	extend: 'Ext.form.Panel',
	alias : 'widget.tokenForm',
	border:false,
	  initComponent: function() {		
		  var tokenController = SupplierApp.app.getController("SupplierApp.controller.Token");
		  
			this.items= [{
				xtype : 'container',
				layout : 'hbox',
				defaults : {
					labelAlign: 'top',
					margin : '5 5 0 5',
					xtype : 'textfield',
					//width:250
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
					width:350,
					listeners:{
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
					width:250,
					allowBlank:false
				},{
					xtype : 'checkbox',
					fieldLabel : window.navigator.language.startsWith("es", 0)? 'Registro activo?':'Active register?',
					name : 'enabled',
					//maxWidth : 300,
					flex:1,
					checked: true
				}
				
				
				
				]
			}];

			
			this.dockedItems = [{
		        xtype: 'toolbar',
		        dock: 'bottom', // Specify the dock position
		        defaults: {
                    margin: '0 20 0 0' 
                },
		        items: [
		        	{
		                iconCls: 'icon-save',
		                id: 'saveTokenRegisterId',
		                text: window.navigator.language.startsWith("es", 0) ? 'Enviar registro' : 'Send register',
		                action: 'save',
		                cls: 'buttonStyle'
		            }, {
		                iconCls: 'icon-accept',
		                id: 'updateTokenRegisterId',
		                text: window.navigator.language.startsWith("es", 0) ? 'Renovar registro' : 'Renew register',
		                action: 'update',
		                cls: 'buttonStyle',
		                disabled: true
		            }, {
		                iconCls: 'icon-add',
		                id: 'newTokenRegisterId',
		                text: window.navigator.language.startsWith("es", 0) ? 'Nuevo registro' : 'New register',
		                action: 'new',
		                cls: 'buttonStyle'
		            },{
		      			name : 'searchAccessToken',
		    			itemId : 'searchAccessToken',
		    			emptyText : SuppAppMsg.suppliersSearch,
		    			xtype : 'trigger',
		    			maxWidth : 300,
		    			flex : 1,
		    			triggerCls : 'x-form-search-trigger',
		    			onTriggerClick: function() {
		    				tokenController.loadSearchList(this, this.getValue());
		            },
		    			enableKeyEvents : true,
		    			listeners : {
		    				specialkey : function(field, e) {
		    					if (e.ENTER === e.getKey()) {
		    						field.onTriggerClick();
		    					}
		    				}
		    			}
		    		}
		        ]
		    }],
		    

		  this.callParent(arguments);	    
	  }

});