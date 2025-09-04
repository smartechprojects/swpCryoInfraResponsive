Ext.define('SupplierApp.view.users.UsersForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.usersForm',
	border : false,
	frame : false,
	style: 'border: solid #ccc 1px',
	autoScroll : true,
	initComponent : function() {
		this.items = [ {
			xtype : 'container',
			layout : 'vbox',
			margin : '15 15 0 10',
			style : 'border-bottom: 1px dotted #fff;padding-bottom:10px',
			defaults : {
				labelWidth : 150,
				margin : '5 15 5 0',
				xtype : 'textfield',
				labelAlign: 'left'
			},
			items : [ {
				xtype : 'hidden',
				name : 'id'
			}, {
				fieldLabel : SuppAppMsg.usersUserId,
				name : 'userName',
				id : 'usersFormUserName',
				width : 300,
				allowBlank:false,
				maskRe: /[A-Za-z\d-]/,
				stripCharsRe: /[^A-Za-z\d-]/,
//			    maxLength : 12,
//				maxLengthText : SuppAppMsg.supplierForm68 + '{0}<br>',
				listeners:{
					change: function(field, newValue, oldValue){
						field.setValue(newValue.toUpperCase());
						}
				},
			}, {
				fieldLabel : SuppAppMsg.usersFullName,
				name : 'name',
				id : 'usersFormName',
				width : 600,
				allowBlank:false,
				maskRe: /[A-Za-z \d]/,
				stripCharsRe: /[^A-Za-z \d]/,
				listeners:{
					change: function(field, newValue, oldValue){
						field.setValue(newValue.toUpperCase());
						}
				},
			}, {
				fieldLabel : SuppAppMsg.usersEmail,
				name : 'email',
				id : 'usersFormEmail',
				vtype : 'email',
				width : 400,
				allowBlank : false,
	            listeners:{
					change: function(field, newValue, oldValue){
						field.setValue(newValue.toLowerCase());
					}
				}
			}, {
				fieldLabel : SuppAppMsg.usersPass,
				name : 'password',
				width : 300,
				enforceMaxLength :8,
				maxLength : 8,
				vtype:'secPass', 
				inputType: 'password',
				allowBlank : false,
				readonly:true
			}, {
				xtype : 'checkbox',
				fieldLabel : SuppAppMsg.usersSubUser,
				id : 'usersIsSubUser',
				name : 'subUser',
				width : 300,
				listeners: {
                    change: function (checkbox, newVal, oldVal) {
                    	
                    	if (newVal == true) {
                    		//Selecciona el ROL de Proveedor por default
                    		var comboBoxRole = Ext.getCmp('usersRoleCombo');
                            if (comboBoxRole) {
                                var storeRole = comboBoxRole.getStore();
                                var recordToSelectRole = storeRole.findRecord(comboBoxRole.displayField, 'ROLE_SUPPLIER');
                                if (recordToSelectRole) {
                                	comboBoxRole.select(recordToSelectRole);
                                	comboBoxRole.setValue(recordToSelectRole.data.id);
                                	comboBoxRole.setDisabled(true);
                                }                               
                            }
                            
                            //Selecciona el TIPO DE USUARIO Proveedor por default
                            var comboBoxType = Ext.getCmp('userTypeCombo');
                            if (comboBoxType) {
                                var storeType = comboBoxType.getStore();
                                var recordToSelectType = storeType.findRecord(comboBoxType.displayField, 'Proveedor');
                                if (recordToSelectType) {
                                	comboBoxType.select(recordToSelectType);
                                	comboBoxType.setValue(recordToSelectType.data.id);
                                	comboBoxType.setDisabled(true);                                    
                                }                               
                            }
                            
                            //Muestra controles de Proveedor
			    			Ext.getCmp('usersAddressNumber').show();
			    			Ext.getCmp('usersAddressNumber').allowBlank=false;
			    			Ext.getCmp('userMainSupplierUser').show();
                    		var uIsSupplier = Ext.getCmp('userIsSupplierUser').getValue();
                    		if(uIsSupplier == true){
                    			Ext.getCmp('usersAddressNumber').setReadOnly(true);
                    		} else {
                    			Ext.getCmp('usersAddressNumber').setReadOnly(false);
                    		}
                    		
                    	} else {
                    		Ext.getCmp('usersRoleCombo').setDisabled(false);
                    		Ext.getCmp('usersRoleCombo').setValue(null);
                    		Ext.getCmp('userTypeCombo').setDisabled(false);
                    		Ext.getCmp('userTypeCombo').setValue(null);
                    		Ext.getCmp('usersAddressNumber').setReadOnly(false);
			    			Ext.getCmp('usersAddressNumber').allowBlank=true;
			    			Ext.getCmp('usersAddressNumber').hide();
			    			Ext.getCmp('userMainSupplierUser').hide();
                    	}

                    }
				}
			},{
				xtype : 'combo',
				fieldLabel : SuppAppMsg.usersRoleAuth,
				id : 'usersRoleCombo',
				itemId : 'usersRoleCombo',
				name : 'userRole.id',
				store : getUDCStore('ROLES', '', '', ''),
				triggerAction : 'all',
				valueField : 'id',
				displayField : 'strValue1',
				emptyText : 'Selecciona...',
				width : 350,
				allowBlank : false,
			    listeners: {
			    	select: function (comboBox, records, eOpts) {
			    		   
			    		var role = records.data.strValue1;
			    		if(role == 'ROLE_SUPPLIER'){                            
			    			Ext.getCmp('usersAddressNumber').show();
			    			Ext.getCmp('usersAddressNumber').allowBlank=false;
			    			Ext.getCmp('userMainSupplierUser').show();
			    			
                            //Selecciona el TIPO DE USUARIO Proveedor por default
                            var comboBoxType = Ext.getCmp('userTypeCombo');
                            if (comboBoxType) {
                                var storeType = comboBoxType.getStore();
                                var recordToSelectType = storeType.findRecord(comboBoxType.displayField, 'Proveedor');
                                if (recordToSelectType) {
                                	comboBoxType.select(recordToSelectType);
                                	comboBoxType.setValue(recordToSelectType.data.id);
                                	comboBoxType.setDisabled(true);                                    
                                }                               
                            }
                            
			    		} else {                    		
			    			Ext.getCmp('usersAddressNumber').hide();
			    			Ext.getCmp('usersAddressNumber').allowBlank=true;
			    			Ext.getCmp('userMainSupplierUser').hide();
			    			
                    		Ext.getCmp('userTypeCombo').setDisabled(false);
                    		Ext.getCmp('userTypeCombo').setValue(null);
			    		}
			    	}
			    }
			}, {
				xtype : 'combo',
				fieldLabel : SuppAppMsg.usersUserType,
				id : 'userTypeCombo',
				itemId : 'userTypeCombo',
				name : 'userType.id',
				store : getUDCStore('USERTYPE', '', '', ''),
				triggerAction : 'all',
				valueField : 'id',
				displayField : 'strValue1',
				emptyText : 'Selecciona...',
				width : 399,
				allowBlank : false
			}, {
				xtype: 'numberfield',
				fieldLabel : SuppAppMsg.suppliersNumber,
				name : 'addressNumber',
				id : 'usersAddressNumber',
				width : 300,
				allowBlank : true,
				hidden: true,
				useThousandSeparator: false,
                hideTrigger:true,
			    allowNegative: false,    							    
			    allowDecimals: false,
                enableKeyEvents:true,
				minValue: 1,
				maxLength : 12,
				enforceMaxLength : true,									
                maskRe: /[0-9]/,
                listeners : {
                    paste: {
                        element: 'inputEl',
                        fn: function(event, inputEl) {
                            if(event.type == "paste"){
                            	event.preventDefault();
                            	return false;
                            }
                        }
                    }
               }
			}, {
				xtype : 'checkbox',
				fieldLabel : SuppAppMsg.usersMainSupplierUser,
				id : 'userMainSupplierUser',
				name : 'mainSupplierUser',
				width : 300,
				hidden: true
			},{
				xtype : 'checkbox',
				fieldLabel : SuppAppMsg.usersActivo,
				name : 'enabled',
				width : 300,
				checked: true
			},{
				xtype : 'checkbox',
				fieldLabel : SuppAppMsg.usersSystem,
				name : 'logged',
				width : 400,
				readOnly:true
			},{
				xtype : 'checkbox',
				fieldLabel : 'Ha aceptado el acuerdo',
				name : 'agreementAccept',
				width : 400,
				readOnly: true,
				hidden : true
			},{
				xtype : 'checkbox',
				id : 'userIsSupplierUser',
				name : 'supplier',
				width : 400,
				hidden: true
			}, {
				name : 'notes',
				width : 600,
				hidden : true
			},{
				xtype : 'checkbox',
				fieldLabel : SuppAppMsg.usersExepAcces,
				name : 'exepAccesRule',
				width : 400
			},{
				xtype: 'textareafield',
				readOnly:true,
				fieldStyle: 'border: none; background-image: none;',
				value : SuppAppMsg.usersSupplierMainUserMessage,
				id : 'userMainSupplierUserMsg',
				name : 'mainSupplierUserMsg',
				width : 700,
				maxRows: 5,
				hidden : true
			}

			]
		} ];

		this.tbar = [ {
			iconCls : 'icon-save',
			itemId : 'saveUsers',
			id : 'saveUsers',
			text : SuppAppMsg.usersSave,
			action : 'saveUsers'
		}, {
			iconCls : 'icon-delete',
			itemId : 'deleteUsers',
			id : 'deleteUsers',
			text : 'Eliminar',
			action : 'deleteUsers',
			disabled : true,
			hidden:true
		}, '-', {
			iconCls : 'icon-accept',
			itemId : 'updateUsers',
			id : 'updateUsers',
			text : SuppAppMsg.usersUpdate,
			action : 'updateUsers',
			disabled : true
		}, '-', {
			iconCls : 'icon-add',
			itemId : 'usersNew',
			text : SuppAppMsg.usersNew,
			action : 'usersNew',
			margin : '5 0 10 0'
		} ];
		this.callParent(arguments);
	}

});