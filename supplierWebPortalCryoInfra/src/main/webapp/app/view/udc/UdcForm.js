Ext.define('SupplierApp.view.udc.UdcForm' ,{
	extend: 'Ext.form.Panel',
	alias : 'widget.udcForm',
	border:true,
	  initComponent: function() {
		  var me = this; 
		  var upperCaseUDC = ['APPROVER','TAXUSER','APPROVALFREIGHT','PLANTREQUEST','APPROVERPONP','APPROVERPOSP','APPROVERINV'];
		  
		  var udcController = SupplierApp.app.getController("SupplierApp.controller.Udc");

		  
			this.items= [{
				xtype: 'container',
				layout: 'hbox',
				margin: '15 15 0 10',
        		style:'border-bottom: 1px dotted #fff;padding-bottom:10px',
				defaults: { 
					labelWidth: 50, 
					align: 'stretch'
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
					width:300,
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
					labelWidth: 40,
					fieldLabel: SuppAppMsg.udcKey,
					name: 'udcKey',
					id: 'udcKey',
					width:200,
					padding:'0 0 0 35',
					disabled:false,
					allowBlank:false,
					fieldCls: 'outlineField',
					listeners:{
						change: function(field, newValue, oldValue){
							//field.setValue(newValue.toUpperCase());
						}
					}
				}]
			},
			{
				xtype: 'container',
				layout: 'hbox',
				margin: '0 15 0 10',
				width:'100%',
				defaults: { 
					labelWidth: 60, 
					align: 'stretch'
				},
				items :[{
					xtype:'textfield',
					fieldLabel: 'strValue1',
					name: 'strValue1',
					width:550,
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
					width:250,
					padding:'0 0 0 15',
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
				}
				]
			},
			{
				xtype: 'container',
				layout: 'hbox',
				margin: '10 15 0 0',
				defaults: { 
					labelWidth: 75, 
					align: 'stretch'
				},
				items:[,{
					xtype: 'numberfield',
					fieldLabel: 'intValue',
					name: 'intValue',
					width:150,
					padding:'0 0 0 10',
					hideTrigger : true
				},
				{
					xtype: 'datefield',
					fieldLabel: 'dateValue',
					name: 'dateValue',
					id: 'dateValue',
					width: 200,
					dateFormat: 'd-m-Y',
					//dateFormat: 'Y-M-d',
					
					padding:'0 0 0 25'
				},{
			    	   xtype: 'checkbox',
			    	   boxLabel:'boolValue',
			    	   name: 'booleanValue',
			    	   padding:'0 0 0 45'
			       },{
				    	   xtype:'textfield',
				    	   fieldLabel: 'KeyRef',
				    	   name: 'keyRef',
				    	   width:250,
				    	   padding:'0 0 0 25',
				            listeners:{
								change: function(field, newValue, oldValue){
									field.setValue(newValue.toLowerCase());
								}
							}
				       },{
				    	   xtype:'textfield',
				    	   fieldLabel: 'SysRef',
				    	   name: 'systemRef',
				    	   width:250,
				    	   padding:'0 0 0 30'
				       }]
			}];
			
			this.tbar=[      
			      {
			    	  iconCls: 'icon-save',
			    	  id: 'udcSave',
			    	  itemId: 'save',
			    	  text: SuppAppMsg.usersSave,
			    	  action: 'save'
			      },'-',
			      {
			    	  iconCls: 'icon-delete',
			    	  id: 'udcDelete',
			    	  itemId: 'delete',
			    	  text: 'Eliminar',
			    	  action: 'delete',
			    	  hidden: true
			      },'-',
			      {
			    	  iconCls: 'icon-accept',
			    	  id: 'udcUpdate',
			    	  itemId: 'update',
			    	  text: SuppAppMsg.usersUpdate,
			    	  action: 'update',
			    	  disabled: true
			      },'-',
			      {
			    	  iconCls: 'icon-add',
			    	  itemId: 'udcNew',
			    	  text: SuppAppMsg.usersNew,
			    	  action: 'new'
			      },'-',
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
              }];
		  this.callParent(arguments);	    
	  }

});