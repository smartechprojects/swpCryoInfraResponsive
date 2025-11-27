Ext.define('SupplierApp.view.udc.UdcForm' ,{
	extend: 'Ext.form.Panel',
	alias : 'widget.udcForm',
	border:false,
	  initComponent: function() {
		  var me = this; 
		  var upperCaseUDC = ['APPROVER','TAXUSER','APPROVALFREIGHT','PLANTREQUEST','APPROVERPONP','APPROVERPOSP','APPROVERINV'];
		  
		  var udcController = SupplierApp.app.getController("SupplierApp.controller.Udc");

		  
			this.items= [{
				xtype: 'toolbar',
	            padding: '0',
				defaults: { 
					//margin: '0 10 0 0',
				},
				items :[{
			    	  iconCls: 'icon-save',
			    	  id: 'udcSave',
			    	  itemId: 'save',
			    	  text: SuppAppMsg.usersSave,
			    	  action: 'save',
			           cls: 'buttonStyle'
			      },
			      {
			    	  iconCls: 'icon-delete',
			    	  id: 'udcDelete',
			    	  itemId: 'delete',
			    	  text: 'Eliminar',
			    	  action: 'delete',
			    	  hidden: true,
			            cls: 'buttonStyle'
			      },
			      {
			    	  iconCls: 'icon-accept',
			    	  id: 'udcUpdate',
			    	  itemId: 'update',
			    	  text: SuppAppMsg.usersUpdate,
			    	  action: 'update',
			    	  disabled: true,
			            cls: 'buttonStyle'
			      },
			      {
			    	  iconCls: 'icon-add',
			    	  itemId: 'udcNew',
			    	  text: SuppAppMsg.usersNew,
			    	  action: 'new',
			            cls: 'buttonStyle'
			      },
			      {
                      name: 'searchUdc',
                      itemId: 'searchUdc',
                      emptyText: SuppAppMsg.suppliersSearch,
                      xtype: 'trigger',
                      triggerCls: 'x-form-search-trigger',
                      onTriggerClick: function() {
                    	    udcController.loadSearchList(this, this.getValue());
                    },
                      enableKeyEvents: true,
                      listeners: {
                    	  specialkey: function (field, e) {
                    		  if (e.ENTER === e.getKey()) {
                    			  field.onTriggerClick();
                    		  }
                    	  }
                      }    
                    
              }]
			},{
				xtype: 'container',
				layout: 'hbox',
				padding: '0',			
				defaults: { 
					labelWidth: 50, 
					align: 'stretch',
					labelAlign: 'top',
					margin: '0 10 0 0',
				},
				items       :[{
					xtype: 'hidden',
					name: 'id'

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
					xtype:'textfield',
					fieldLabel: 'System',
					name: 'udcSystem',
					id: 'udcSystem',
					maxWidth:300,
					flex: 1,
					margin: '0 10 0 0',
					disabled:false,
					allowBlank:false,
					fieldCls: 'outlineField',
					listeners:{
						change: function(field, newValue, oldValue){
							field.setValue(newValue.toUpperCase());
						}
					}
				},
				{
					xtype:'textfield',
					fieldLabel: SuppAppMsg.udcKey,
					name: 'udcKey',
					id: 'udcKey',
					maxWidth:300,
					flex: 1,
					disabled:false,
					allowBlank:false,
					margin: '0 10 0 0',
					fieldCls: 'outlineField',
					listeners:{
						change: function(field, newValue, oldValue){
							//field.setValue(newValue.toUpperCase());
						}
					}
				},{
			    	   xtype:'textfield',
			    	   fieldLabel: 'KeyRef',
			    	   name: 'keyRef',
			    	   maxWidth:250,
			    	   flex: 1,
			    	   margin: '0 10 0 0',
			           listeners:{
							change: function(field, newValue, oldValue){
								field.setValue(newValue.toLowerCase());
							}
						}
			       },{
			    	   xtype:'textfield',
			    	   fieldLabel: 'SysRef',
			    	   name: 'systemRef',
			    	   maxWidth:250,
			    	   flex: 1
			       }]
			},
			{
				xtype: 'container',
				layout: 'hbox',
				padding: '0',
				defaults: { 
					labelWidth: 50, 
					align: 'stretch',
					labelAlign: 'top',
					margin: '0 10 0 0',
				},
				items :[{
					xtype:'textfield',
					fieldLabel: 'strValue1',
					name: 'strValue1',
					maxWidth:300,
					margin: '0 10 0 0',
					flex: 1,
					/*listeners:{
							change: function(field, newValue, oldValue){
							var form = me.getForm();
							var rowSelected = form._record.data;
							for (const udc of upperCaseUDC) {
								if(rowSelected.udcSystem == udc){
									field.setValue(newValue.toUpperCase());
									break;
								}
							}	
						}
					}*/
					listeners:{
				        change: function(field, newValue, oldValue){
				            var formPanel = field.up('form');       // subir al panel form
				            if (!formPanel) return;

				            var form = formPanel.getForm();
				            if (!form) return;

				            // obtener udcSystem directamente del campo
				            var udcSystemField = form.findField('udcSystem');
				            var udcSystem = udcSystemField ? udcSystemField.getValue() : null;
				            if (!udcSystem) return;

				            // convertir a mayúsculas si pertenece a la lista
				            for (const udc of upperCaseUDC) {
				                if(udcSystem === udc){
				                    if (newValue) {
				                        // evitar recursión
				                        if (field._settingUpper) return;
				                        field._settingUpper = true;
				                        field.setValue(newValue.toUpperCase());
				                        field._settingUpper = false;
				                    }
				                    break;
				                }
				            }
				        }
				    }
				},{

					xtype:'textfield',
					fieldLabel: 'strValue2',
					name: 'strValue2',
					margin: '0 10 0 0',
					maxWidth:300,
					flex: 1,
					listeners:{
				        change: function(field, newValue, oldValue){
				            var formPanel = field.up('form');       // subir al panel form
				            if (!formPanel) return;

				            var form = formPanel.getForm();
				            if (!form) return;

				            // obtener udcSystem directamente del campo
				            var udcSystemField = form.findField('udcSystem');
				            var udcSystem = udcSystemField ? udcSystemField.getValue() : null;
				            if (!udcSystem) return;

				            // convertir a mayúsculas si pertenece a la lista
				            for (const udc of upperCaseUDC) {
				                if(udcSystem === udc){
				                    if (newValue) {
				                        // evitar recursión
				                        if (field._settingUpper) return;
				                        field._settingUpper = true;
				                        field.setValue(newValue.toUpperCase());
				                        field._settingUpper = false;
				                    }
				                    break;
				                }
				            }
				        }
				    }
				},
				{
					xtype: 'numberfield',
					fieldLabel: 'intValue',
					name: 'intValue',
					maxWidth:150,
					flex: 1,
					margin: '0 10 0 0',
					hideTrigger : true
				},
				{
					xtype: 'datefield',
					fieldLabel: 'dateValue',
					name: 'dateValue',
					id: 'dateValue',
					maxWidth: 150,
					flex: 1,
					dateFormat: 'd-m-Y',
					margin: '0 15 0 0',
					//dateFormat: 'Y-M-d',
				},
				{
					fieldLabel: 'boolValue',
			    	xtype: 'checkbox',
			    	name: 'booleanValue'			    	
			       }
				]
			}];
			
		  this.callParent(arguments);	    
	  }

});