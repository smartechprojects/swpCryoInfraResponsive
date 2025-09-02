Ext.define('SupplierApp.view.supplier.SupplierForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.supplierForm',
	border : false,
	id:'supplierFormId',
	frame : false,
	style: 'border: solid #ccc 1px',
	autoScroll : true,
    getInvalidFields: function() {
        var invalidFields = [];
        Ext.suspendLayouts();
        this.form.getFields().filterBy(function(field) {
            if (field.validate()) return;
            invalidFields.push(field);
        });
        Ext.resumeLayouts(true);
        return invalidFields;
    },
	initComponent : function() {
		
		
		var colStore = Ext.create('Ext.data.Store', {
		    fields: ['id_', 'name'],
		    data: [{
		            'id_': 'Sin datos',
		            'name': 'Sin datos'
		        }
		    ]

		});
		
		
		this.items = [{
						xtype : 'hidden',
						name : 'id'
					},{
						xtype : 'hidden',
						name : 'approvalStatus'
					},{
						xtype : 'hidden',
						name : 'approvalStep'
					},{
						xtype : 'hidden',
						name : 'steps',
						id:'steps',
						value:2
					},{
					xtype: 'container',
					margin:'15 0 0 10',
					layout : {
		                  type :'table',
		                  columns : 3,
		                  tableAttrs: {
		                     style: {
		                        width: '95%'
		                     }
		                  }               
		             },
		            autoHeight:true, 
		            autoWidth:true, 
					cls: 'valign_class',
					defaults : {
						labelWidth : 130,
						xtype : 'textfield',
						margin: '15 0 0 10',
						width : 320,
						align: 'stretch',
						//fieldStyle: 'padding-bottom:5px;font-size:18px;vertical-align:top;border:none;background:transparent;color:black;font-weight:bold',
						//readOnly:true
					},
			        items:[{
								name : 'ticketId',
								hidden: true,
								id:'ticketId',
								value:0,
								readOnly:true,
								colspan:3,
								fieldLabel:'Num. Ticket',
								margin: '0 0 20 10',
								fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
								 tip: 'Este campo está protegido',
								    listeners: {
								      render: function(c) {
								        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      }
								    }
							},{
						   fieldLabel : 'Fecha de solicitud',
						   translate   : {
						      English : 'Request Date',
						      Español : 'Fecha de solicitud'
						   },
						   name : 'fechaSolicitud',
						   width:250,
						   xtype:'datefield',
						   format: 'd-m-Y',
						   margin:'0 0 20 10',
						   value: new Date(),
						   readOnly:true,
						   fieldStyle: 'border:none;background-color: #ddd; background-image: none;'
						   },{
							fieldLabel : 'Num. Proveedor',
							translate   : {
							    English : 'Supplier Nbr',
							    Español : 'Num. Proveedor'
							},
							name : 'addresNumber',
							labelAlign:'right',
							margin:'0 0 20 0',
							colspan:2,
							readOnly:true,
							hidden:role=='ANONYMOUS'?true:false,
							fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
						    tip: 'Este campo está protegido',
						    listeners: {
						      render: function(c) {
						        Ext.create('Ext.tip.ToolTip', {
						          target: c.getEl(),
						          html: c.tip 
						        });
						      }
						    }
						    },{
							fieldLabel : 'Comprador asignado',
							translate   : {
							    English : 'Buyer email',
							    Español : 'Comprador asignado'
							   },
							name : 'compradorAsignado',
							width:400,
							readOnly:true,
							hidden:true,	
							fieldStyle: 'border:none;background-color: #ddd; background-image: none;',
							tip: 'Este campo está protegido',
						    listeners: {
						      render: function(c) {
						        Ext.create('Ext.tip.ToolTip', {
						          target: c.getEl(),
						          html: c.tip 
						        });
						      }
						    }
							},{
							xtype: 'combobox',
			                name: 'categoriaJDE',
			                fieldLabel: 'Categoría JDE',
			                translate   : {
							    English : 'JDE Category',
							    Español : 'Categoría JDE'
							   },
			                store : getAutoLoadUDCStore('JDECATEGORY', '', '', ''),
			                displayField: 'strValue1',
			                valueField: 'udcKey',
			                typeAhead: true,
			                typeAheadDelay: 100,
			                minChars: 1,
			                queryMode: 'local',
			                //forceSelection: true,
			                colspan:3,
			                hidden:role=='ANONYMOUS'?true:false,
					        fieldStyle:'border:2px solid green;'
							},{
							xtype: 'combobox',
							translate   : {
							    English : 'Supplier Type',
							    Español : 'Tipo de proveedor'
							   },
			                name: 'supplierType',
			                fieldLabel: 'Tipo de proveedor',
			                width : 400,
			                typeAhead: true,
			                typeAheadDelay: 100,
			                minChars: 1,
			                queryMode: 'local',
			                //forceSelection: true,
			                store : getAutoLoadUDCStore('SUPTYPE', '', '', ''),
			                displayField: 'strValue2',
			                valueField: 'udcKey',
			                colspan:3,
			                hidden:true
							},{
							fieldLabel : 'GL Class',
							translate   : {
							    English : 'GL Class',
							    Español : 'GL CLass'
							   },
							name : 'categorias',
							xtype: 'combobox',
			                typeAhead: true,
			                typeAheadDelay: 100,
			                minChars: 1,
			                queryMode: 'local',
			                //forceSelection: true,
							store : getAutoLoadUDCStore('GLCLASS', '', '', ''),
			                displayField: 'strValue1',
			                valueField: 'udcKey',
			                width : 350,
							colspan:3,
			                hidden:role=='ANONYMOUS'?true:false,
							        fieldStyle:'border:2px solid green;'
							},{
								fieldLabel : 'Tipo de Prod/Serv',
								translate   : {
								    English : 'Service/Prod Type',
								    Español : 'Tipo de Prod/Serv'
								   },
								name : 'tipoProductoServicio',
								xtype: 'combobox',
								store : getAutoLoadUDCStore('SERVTYPE', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                width : 350,
				                typeAhead: true,
				                typeAheadDelay: 100,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								colspan:3,
				                hidden:role=='ANONYMOUS'?true:false,
								        fieldStyle:'border:2px solid green;'
							},{
								fieldLabel : 'Método de pago',
								translate   : {
								    English : 'Payment Method',
								    Español : 'Método de pago'
								   },
								name : 'paymentMethod',
								xtype: 'combobox',
								store : getAutoLoadUDCStore('SUPPAYMENT', '', '', ''),
				                displayField: 'strValue1',
				                typeAhead: true,
				                typeAheadDelay: 100,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
				                valueField: 'udcKey',
				                width : 400,
								colspan:3,
				                hidden:role=='ANONYMOUS'?true:false,
								        fieldStyle:'border:2px solid green;'
							},{
								fieldLabel : 'Dias de crédito',
								translate   : {
								    English : 'Payment Terms',
								    Español : 'Días de crédito'
								   },
								name : 'diasCredito',
								xtype: 'combobox',
								store : getAutoLoadUDCStore('CREDITO', '', '', ''),
				                displayField: 'strValue1',
				                typeAhead: true,
				                typeAheadDelay: 100,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
				                valueField: 'udcKey',
				                width : 250,
								colspan:3,
				                hidden:role=='ANONYMOUS'?true:false,
								        fieldStyle:'border:2px solid green;'
							},{
								fieldLabel : 'Tipo de impuesto',
								translate   : {
								    English : 'Tax Rate',
								    Español : 'Tipo de impuesto'
								   },
								name : 'taxRate',
								xtype: 'combobox',
								store : getAutoLoadUDCStore('TAXRATE', '', '', ''),
				                displayField: 'strValue1',
				                typeAhead: true,
				                typeAheadDelay: 100,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
				                valueField: 'udcKey',
				                width : 350,
								colspan:3,
							    listeners: {
							    	select: function (comboBox, records, eOpts) {
							    		var tasaIva = records[0].data.strValue2;
							    		if(tasaIva){
							    			Ext.getCmp('tasaIva').setValue(tasaIva);
							    	    }
							    	}
							    },
				                hidden:role=='ANONYMOUS'?true:false,
								        fieldStyle:'border:2px solid green;'
							},{
								fieldLabel : '% de impuesto',
								translate   : {
								    English : 'Tax %',
								    Español : '% de impuesto'
								   },
								name : 'tasaIva',
								id : 'tasaIva',
								xtype:'textfield',
								readOnly:true,
								//xtype: 'combobox',
								//store : getAutoLoadUDCStore('TAXAMT', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                width : 200,
								colspan:3,
				                hidden:role=='ANONYMOUS'?true:false
							},{
								fieldLabel : 'Expl Code',
								translate   : {
								    English : 'Expl Code',
								    Español : 'Expl Code'
								   },
								name : 'explCode1',
								xtype: 'combobox',
								store : getAutoLoadUDCStore('EXPLCODE', '', '', ''),
				                displayField: 'strValue1',
				                typeAhead: true,
				                typeAheadDelay: 100,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
				                valueField: 'udcKey',
				                width : 320,
								colspan:3,
				                hidden:role=='ANONYMOUS'?true:false,
								fieldStyle:'border:2px solid green;'
							},{
							xtype : 'displayfield',
							value : 'FORMATO DE SOLICITUD',
							translate   : {
							    English : 'REQUEST FORM',
							    Español : 'FORMATO DE SOLICITUD'
							   },
							height:20,
							margin: '50 0 10 10',
							colspan:3,
							fieldStyle: 'font-weight:bold;font-size:20px;'
							},{
								xtype:'container',
				                colspan:3,
								layout: {
								    type: 'hbox',
								    pack: 'start',
								    align: 'stretch'
								},
								items: [{
									xtype:'textfield',
									labelWidth:130,
									fieldLabel : 'Num. de Ticket',
									translate   : {
									    English : 'Ticket Nbr.',
									    Español : 'Número de ticket'
									   },
									name : 'ticketForSearch',
									id : 'ticketForSearch',
									itemId : 'ticketForSearch',
									labelAlign:'left',
									margin:'0 5 0 0',
									width:250,
									colspan:2,
									tip: '# de ticket enviado por correo',
								    listeners: {
								      render: function(c) {
								        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      }
								    }
							  },{
									xtype: 'button',
									translate   : {
									    English : 'Search',
									    Español : 'Buscar'
									   },
									width:60,
									text : 'Buscar',
									action : 'searchTicket',
									id : 'searchTicket',
									maring:'0 0 0 0'
								}]
							},{
							fieldLabel : 'Email del Comprador',
							translate   : {
							    English : 'Buyer email',
							    Español : 'Email del comprador'
							   },
							name : 'emailComprador',
							colspan:3,
							width:320,
							allowBlank:false,
							vtype: 'email',
							hidden:role=='ANONYMOUS'?false:true
						    },{
						   fieldLabel : 'Razón social',
						   translate   : {
							    English : 'Name',
							    Español : 'Razón social'
							   },
						   name : 'razonSocial',
						   itemId : 'razonSocial',
						   margin:'10 0 0 10',
						   width : 680,
						   colspan:3,
						   listeners:{
								change: function(field, newValue, oldValue){
									field.setValue(newValue.toUpperCase());
								}
							},
							allowBlank:false,
							readOnly:role=='ANONYMOUS'?false:true
						   },{
								fieldLabel : 'Tipo de contribuyente',
								translate   : {
								    English : 'Supplier tax type',
								    Español : 'Tipo de contribuyente'
								   },
								name : 'fisicaMoral',
								xtype: 'combobox',
								typeAhead: true,
				                typeAheadDelay: 100,
				                allowBlank:false,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('CONTRIB', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                width : 250,
								colspan:3
							},{
						   fieldLabel : 'Nombre comercial',
						   name : 'name',
						   itemId : 'name',
						   margin:'20 0 0 10',
						   width : 900,
						   hidden:true,
						   colspan:3,
						   listeners:{
								change: function(field, newValue, oldValue){
									field.setValue(newValue.toUpperCase());
								}
							},
							readOnly:role=='ANONYMOUS'?false:true
						   },{
								xtype: 'combobox',
								translate   : {
								    English : 'Country',
								    Español : 'País'
								   },
				                name: 'country',
				                fieldLabel: 'País',
				                typeAhead: true,
				                typeAheadDelay: 100,
				                allowBlank:false,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
				                store : getAutoLoadUDCStore('COUNTRY', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                colspan:3,
				                width:400,
							    listeners: {
							    	select: function (comboBox, records, eOpts) {
							    		var countryCode = records[0].data.udcKey;
							    		if(countryCode != 'MX'){
							    			Ext.getCmp('supRfc').setValue('XEXX010101000');
							    			Ext.getCmp('taxId').setValue('');
							    			Ext.getCmp('cuentaClabe').setMaxLength(18);
							    			Ext.getCmp('cuentaClabe').setMinLength(8);
							    	        Ext.getCmp('cuentaClabe').isValid();
							    	        Ext.getCmp('documentContainerCal').show();
							    		}else{
							    			Ext.getCmp('supRfc').setValue('');
							    			Ext.getCmp('taxId').setValue('.');
							    			Ext.getCmp('cuentaClabe').setMaxLength(18);
							    			Ext.getCmp('cuentaClabe').setMinLength(18);
							    	        Ext.getCmp('cuentaClabe').isValid();
							    	        Ext.getCmp('documentContainerCal').hide();
							    		}
							    	}
							    }
							},{
								fieldLabel : 'RFC',
								translate   : {
								    English : 'RFC (local)',
								    Español : 'RFC'
								   },
								name : 'rfc',
								id:'supRfc',
								colspan:3,
							    vtype:'rfc',
							    maxLength : 13,
								allowBlank:false,
							    listeners:{
									change: function(field, newValue, oldValue){
										if(typeof newValue !== 'undefined' && typeof oldValue !== 'undefined'){
											if(newValue.toUpperCase() != oldValue.toUpperCase()){
												Ext.getCmp('hrefFileList').setValue('');
												Ext.getCmp('rfcDocument').setValue('');
												Ext.getCmp('domDocument').setValue('');
												Ext.getCmp('edoDocument').setValue('');
												Ext.getCmp('escDocument').setValue('');
												Ext.getCmp('notDocument').setValue('');
												Ext.getCmp('identDocument').setValue('');
												Ext.getCmp('certDocument').setValue('');
											}
										}
										field.setValue(newValue.toUpperCase());
									}
							    },
							    checkChangeEvents:['change'],
								readOnly:role=='ANONYMOUS'?false:true,
								enforceMaxLength : true
						    },{
								fieldLabel : 'TaxId',
								translate   : {
								    English : 'Tax Id',
								    Español : 'Tax Id'
								   },
								name : 'taxId',
								id:'taxId',
								allowBlank:true,
								colspan:3,
							    listeners:{
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toUpperCase());
									}
								},
								readOnly:role=='ANONYMOUS'?false:true
							 },{
								fieldLabel : 'Moneda',
								translate   : {
								    English : 'Currency',
								    Español : 'Moneda'
								   },
								name : 'currencyCode',
								typeAhead: true,
				                typeAheadDelay: 100,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								xtype: 'combobox',
								store : getAutoLoadUDCStore('CURRENCY', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                width : 350,
				                allowBlank:false,
								colspan:3
							},{
							xtype : 'displayfield',
							value : 'DATOS DE DOMICILIO:',
							translate   : {
							    English : 'ADDRESS INFORMATION:',
							    Español : 'DATOS DEL DOMICILIO:'
							   },
							height:20,
							margin: '50 0 0 10',
							colspan:3,
							fieldStyle: 'font-weight:bold'
							},{
							fieldLabel : 'Calle y número',
							translate   : {
							    English : 'Street and number',
							    Español : 'Calle y número'
							   },
							name : 'calleNumero',
							width:550,
							allowBlank:false,
							   listeners:{
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toUpperCase());
									}
								},
							fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						    }
							
							
							/*,{
							fieldLabel : 'Código Postal',
							name : 'codigoPostal',
							labelAlign:'right',
							width:250,
							colspan:2,
							allowBlank:false,
							   listeners:{
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toUpperCase());
									}
								}
						    }*/
							
							,{
								xtype:'container',
				                colspan:3,
								layout: {
								    type: 'hbox',
								    pack: 'start',
								    align: 'stretch'
								},
								items: [
									{
										xtype:'textfield',
										fieldLabel : 'Código Postal',
										translate   : {
										    English : 'Zip Code',
										    Español : 'Código Postal'
										   },
										name : 'codigoPostal',
										id : 'codigoPostal',
										labelAlign:'right',
										margin:'0 10 0 0',
										width:200,
										colspan:2,
										allowBlank:false,
										   listeners:{
												change: function(field, newValue, oldValue){
													field.setValue(newValue.toUpperCase());
												}
											},
											fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
									    },
				            			{
			            				  xtype: 'button',		
			            				  text: 'Obtener datos',	
			            				  translate   : {
											    English : 'Search',
											    Español : 'Obtener datos'
											   },
			            				  listeners: {		
			            				      click: function() {
			            				    	  var box = Ext.MessageBox.wait('Buscando datos. Espere unos segundos', 'Ejecución');
			            				    	  Ext.Ajax.cors = true;
				            						Ext.Ajax.useDefaultXhrHeader = false;
				            						var value = Ext.getCmp('codigoPostal').getValue();
				            						if(value){
				            							if(value != ''){
						            						var url = 'https://api-sepomex.hckdrk.mx/query/info_cp/' + value;
						            						
						            						Ext.Ajax.request({
						            							url: url,
						            							method: 'GET',
						            							success: function(response){
							            							var text = response.responseText;
							            							var jsonData = Ext.JSON.decode(response.responseText);
							            							colStore.removeAll();
							            							if(jsonData){
							            								if(jsonData.length > 0){
							            									for(var i = 0;i<jsonData.length;i++){
							            										resp = jsonData[i].response;
							            										colStore.add({
										            						        id_: resp.asentamiento,
										            						        name: resp.asentamiento
										            						    });
							            										Ext.getCmp('fldEstado').setValue(resp.estado);
							            										Ext.getCmp('fldMunicipio').setValue(resp.municipio);
							            									}
							            								}
							            							}
							            							box.hide();
							            							
						            							},
						            							failure: function (msg) {
						            								alert("Error" + msg);
						            								box.hide();
						            							}
						            						});
				            							}
				            						}
			            				      }		
			            				  }
				            			}
								]
							},{
								fieldLabel : 'Colonia',
								xtype: 'combobox',
								store:colStore,
								queryMode: 'local',
								displayField: 'name',
								valueField: 'id_',
								triggerAction: 'all',
					            forceSelection: false,
					            editable: true,
					            typeAhead: true,
								translate   : {
								    English : 'County',
								    Español : 'County'
								   },
								name : 'colonia',
								id:'fldColonia',
								labelAlign:'top',
								margin:'20 0 0 145',
								width:400,
								allowBlank:false,
									fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							    },{
							fieldLabel : 'Delegación o Municipio',
							translate   : {
							    English : 'City',
							    Español : 'Delegación o Municipio'
							   },
							name : 'delegacionMnicipio',
							id:'fldMunicipio',
							labelAlign:'top',
							margin:'20 0 0 0',
							width:300,
							allowBlank:false,
							   listeners:{
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toUpperCase());
									}
								},
								fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						    },{
							fieldLabel : 'Estado',
							translate   : {
							    English : 'State',
							    Español : 'Estado'
							   },
							name : 'estado',
							labelAlign:'top',
							id:'fldEstado',
							width:250,
							margin:'20 0 0 0',
							colspan:1,
							allowBlank:false,
							   listeners:{
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toUpperCase());
									}
								},
								fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						    },{
							fieldLabel : 'Banco',
							translate   : {
							    English : 'Bank Name',
							    Español : 'Banco'
							   },
							width:400,
							typeAhead: true,
			                typeAheadDelay: 100,
			                minChars: 1,
			                queryMode: 'local',
			                //forceSelection: true,
							margin:'30 0 0 10',
							xtype: 'combobox',
			                name: 'nombreBanco',
			                store : getAutoLoadUDCStore('SWITFCODE', '', '', ''),
			                displayField: 'strValue1',
			                valueField: 'udcKey',
							allowBlank:false,
							readOnly:role=='ANONYMOUS'?false:true
						    },{
							fieldLabel : 'Cuenta CLABE',
							translate   : {
							    English : 'Bank account',
							    Español : 'Cuenta CLABE'
							   },
							name : 'cuentaClabe',
							maskRe: /[0-9.]/,
							id:'cuentaClabe',
							xtype:'textfield',
							hideTrigger : true,
							labelAlign:'right',
							margin:'30 0 0 0',
							width:300,
							colspan:2,
							//minLength: 8,
						    maxLength: 18,
							allowBlank:false,
							readOnly:role=='ANONYMOUS'?false:true,
							setMaxLength: function (v) {
					            this.maxLength = v;
					        },
					        getMaxLength: function () {
					            return this.maxLength;
					        },
							setMinLength: function (v) {
					            this.minLength = v;
					        },
					        getMinLength: function () {
					            return this.minLength;
					        }
						    },{
					    	fieldLabel : 'Forma de pago',
					    	translate   : {
							    English : 'Payment Method',
							    Español : 'Forma de pago'
							   },
							width:400,
							margin:'20 0 0 10',
							typeAhead: true,
			                typeAheadDelay: 100,
			                minChars: 1,
			                queryMode: 'local',
			                //forceSelection: true,
							xtype: 'combobox',
			                name: 'formaPago',
			                store : getAutoLoadUDCStore('PINSTRUMENT', '', '', ''),
			                displayField: 'strValue1',
			                valueField: 'udcKey',
							colspan:3,
							allowBlank:false,
							readOnly:role=='ANONYMOUS'?false:true
						    },{
							xtype : 'displayfield',
							value : 'DATOS DE CONTACTO:',
							translate   : {
							    English : 'CONTACT INFORMATION:',
							    Español : 'DATOS DE CONTACTO'
							   },
							height:20,
							margin: '50 0 0 10',
							colspan:3,
							readOnly:role=='ANONYMOUS'?false:true,
							fieldStyle: 'font-weight:bold'
							},{
							fieldLabel : 'Contacto 01*',
							translate   : {
							    English : 'Contact 01*',
							    Español : 'Contacto 01*'
							   },
							name : 'nombreContactoPedidos',
							itemId : 'nombreContactoPedidos',
							width:450,
							allowBlank:false,
							colspan:4,
							listeners:{
								change: function(field, newValue, oldValue){
									field.setValue(newValue.toUpperCase());
								}
						    }
						    },{
							fieldLabel : 'email(s) 01*',
							colspan:2,
							labelAlign:'left',
							name : 'emailContactoPedidos',
							itemId : 'emailContactoPedidos',
							width:880,
							allowBlank:false
						    },{
							fieldLabel : 'Teléfono 01*',
							translate   : {
							    English : 'Phone 01*',
							    Español : 'Teléfono 01*'
							   },
							name : 'telefonoContactoPedidos',
							itemId : 'telefonoContactoPedidos',
							labelWidth:80,
							minLength: 9,
						    maxLength: 15,
							labelAlign:'right',
							width:250,
							colspan:1,
							allowBlank:false,
							colspan:4
						    },{
							fieldLabel : 'Contacto 02*',
							translate   : {
							    English : 'Contact 02*',
							    Español : 'Contacto 02*'
							   },
							name : 'nombreContactoVentas',
							width:450,
							listeners:{
								change: function(field, newValue, oldValue){
									field.setValue(newValue.toUpperCase());
								}
						    }
						    },{
							fieldLabel : 'email 02*',
							labelWidth:80,
							labelAlign:'right',
							name : 'emailContactoVentas',
							itemId : 'emailContactoVentas',
							width:320,
							vtype: 'email'
						    },{
							fieldLabel : 'Teléfono 02*',
							translate   : {
							    English : 'Phone 02*',
							    Español : 'Teléfonno 02*'
							   },
							name : 'telefonoContactoVentas',
							labelWidth:80,
							minLength: 9,
						    maxLength: 15,
							labelAlign:'right',
							width:250,
							colspan:1
						    },{
								fieldLabel : 'Contacto 03',
								translate   : {
								    English : 'Contact 03',
								    Español : 'Contacto 03'
								   },
								name : 'nombreContactoCxC',
								width:450,
								listeners:{
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toUpperCase());
									}
							    }
							    },{
								fieldLabel : 'email 03',
								labelWidth:80,
								labelAlign:'right',
								name : 'emailContactoCxC',
								width:320,
								vtype: 'email'
								},{
								fieldLabel : 'Teléfono 03',
								translate   : {
								    English : 'Phone 03',
								    Español : 'Teléfono 03'
								   },
								name : 'telefonoContactoCxC',
								labelAlign:'right',
								labelWidth:80,
								minLength: 9,
							    maxLength: 10,
								width:250,
								colspan:1
							    },{
									fieldLabel : 'Contacto 04',
									translate   : {
									    English : 'Contact 04',
									    Español : 'Contacto 04'
									   },
									name : 'nombreContactoCalidad',
									width:450,
									listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
								    }
								    },{
									fieldLabel : 'email 04',
									labelAlign:'right',
									labelWidth:80,
									name : 'emailContactoCalidad',
									width:320,
									vtype: 'email'
								    },{
									fieldLabel : 'Teléfono 04',
									translate   : {
									    English : 'Phone 04',
									    Español : 'Teléfono 04'
									   },
									labelAlign:'right',
									name : 'telefonoContactoCalidad',
									labelWidth:80,
									//minLength: 9,
								   // maxLength: 10,
									width:250,
									colspan:1
								    },{
								xtype : 'displayfield',
								value : 'DATOS DE CALIDAD:',
								height:20,
								hidden:true,
								margin: '50 0 0 10',
								colspan:3,
								//allowBlank:false,
								fieldStyle: 'font-weight:bold'
								},{
								fieldLabel : 'Nombre del puesto',
								name : 'puestoCalidad',
								labelWidth:130,
								width:350,
								hidden:true,
								colspan:3,
								//allowBlank:false,
								readOnly:role=='ANONYMOUS'?false:true
							    },{
								xtype : 'displayfield',
								value : 'DATOS DE DISTRIBUCIÓN:',
								height:20,
								hidden:true,
								margin: '50 0 10 10',
								colspan:3,
								fieldStyle: 'font-weight:bold'
								},{
							   fieldLabel : 'Dirección completa de la planta',
							   name : 'direccionPlanta',
							   id:'direccionPlanta',
							   labelWidth:200,
							   hidden:true,
							   margin:'10 0 0 10',
							   width : 950,
							   colspan:3,
							   //allowBlank:false,
							   tip: 'Sólo una de las dos opciones es obligatoria: Dirección de planta o Dirección de CEDIS',
							   listeners:{
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toUpperCase());
										Ext.getCmp('direccionCentroDistribucion').allowBlank = true;
							    		Ext.getCmp('direccionCentroDistribucion').validate();
									},
									render: function(c) {
								        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      }
								},
								readOnly:role=='ANONYMOUS'?false:true
							   },{
							   fieldLabel : 'Dirección completa del CEDIS',
							   name : 'direccionCentroDistribucion',
							   id:'direccionCentroDistribucion',
							   margin:'20 0 0 10',
							   labelWidth:200,
							   hidden:true,
							   allowBlank: true,
							   width : 950,
							   colspan:3,
							   //allowBlank:false,
							   tip: 'Sólo una de las dos opciones es obligatoria: Dirección de planta o Dirección de CEDIS',
							   listeners:{
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toUpperCase());
										Ext.getCmp('direccionPlanta').allowBlank = true;
							    		Ext.getCmp('direccionPlanta').validate();
									},
									render: function(c) {
								        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      }
								},
								readOnly:role=='ANONYMOUS'?false:true
							   },{
							    xtype: 'combobox',
				                name: 'riesgoCategoria',
				                fieldLabel: 'Riesgo',
				                width : 400,
				                margin:'40 0 0 10',
				                store: getRiesgoList(),
				                displayField: 'riesgoCategoria',
				                valueField: 'riesgoCategoria',
				                colspan:3,
				                hidden:true,
				                allowBlank:true
				                //hidden:role=='ROLE_ADMIN'?false:true
							    },{
						    	fieldLabel : 'Días crédito Actual',
								name : 'diasCreditoActual',
								margin:'30 0 0 10',
								width:300,
								hidden:true,
								//hidden:role=='ROLE_ADMIN'?false:true
							    },{
								fieldLabel : 'Días crédito Anterior',
								name : 'diasCreditoAnterior',
								labelAlign:'right',
								margin:'30 0 0 0',
								width:300,
								colspan:2,
								hidden:true,
								//hidden:role=='ROLE_ADMIN'?false:true
							    },
							    
							    
							    {
							    	xtype: 'container',
							    	id:'docLoadSection',
							    	colspan:3,
							    	defaults : {
										labelWidth : 550,
										xtype : 'textfield',
										margin: '15 0 0 0'
									},
							    	items:[{
										xtype : 'displayfield',
										value : 'DOCUMENTOS (Max 10MB por documento):',
										translate   : {
										    English : 'ATTACHMENTS (Max 10 MB per file):',
										    Español : 'DOCUMENTOS (Max 10MB por documento):'
										   },
										margin: '30 0 0 0',
										colspan:3,
										fieldStyle: 'font-weight:bold'
										},{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerRfc',
									colspan:3,
									width:800,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 90,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : 'Constancia de Situación Fiscal',
										translate   : {
										    English : 'Fiscal subscription evidence',
										    Español : 'Constanca de situación fiscal'
										   },
										name : 'rfcDocument',
										id:'rfcDocument',
										width:400,
										readOny:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadRfcDoc',
											id : 'loadRfcDoc',
											text : 'Cargar',
											action : 'loadRfcDoc'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerDom',
									colspan:3,
									width:800,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 90,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : 'Comprobante de domicilio',
										translate   : {
										    English : 'Address evidence',
										    Español : 'Comprobante de domicilio'
										   },
										name : 'domDocument',
										id : 'domDocument',
										width:400,
										readOny:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadDomDoc',
											id : 'loadDomDoc',
											text : 'Cargar',
											action : 'loadDomDoc'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerBank',
									colspan:3,
									width:800,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 90,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : 'Estado de cuenta',
										translate   : {
										    English : 'Bank subscription evidence',
										    Español : 'Estado de cuenta'
										   },
										name : 'edoDocument',
										id : 'edoDocument',
										width:400,
										readOny:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadEdoDoc',
											id : 'loadEdoDoc',
											text : 'Cargar',
											action : 'loadEdoDoc'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerPub',
									colspan:3,
									width:800,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 90,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : 'Escritura pública',
										translate   : {
										    English : 'Other file',
										    Español : 'Escritura pública'
										   },
										name : 'escDocument',
										id : 'escDocument',
										width:400,
										readOny:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadEscDoc',
											id : 'loadEscDoc',
											text : 'Cargar',
											action : 'loadEscDoc'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerNot',
									colspan:3,
									width:800,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 90,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : 'Poder notarial',
										translate   : {
										    English : 'Other file',
										    Español : 'poder notarial'
										   },
										name : 'notDocument',
										id : 'notDocument',
										width:400,
										readOny:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadNotDoc',
											id : 'loadNotDoc',
											text : 'Cargar',
											action : 'loadNotDoc'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerIden',
									colspan:3,
									width:800,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 90,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : 'Identificación oficial',
										translate   : {
										    English : 'Personal ID evidence',
										    Español : 'Identificación oficial'
										   },
										name : 'identDocument',
										id : 'identDocument',
										width:400,
										readOny:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadIdentDoc',
											id : 'loadIdentDoc',
											text : 'Cargar',
											action : 'loadIdentDoc'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerCal',
									colspan:3,
									width:800,
									hidden:true,
									defaults : {
										labelWidth : 90,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : 'Certificado de residencia',
										translate   : {
										    English : 'Residence certification',
										    Español : 'Certificado de residencia'
										   },
										name : 'certDocument',
										id : 'certDocument',
										width:400,
										readOny:true,
										margin: '12 10 0 0'
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadCertDoc',
											id : 'loadCertDoc',
											text : 'Cargar',
											action : 'loadCertDoc'
										}]
							    }]
							    },{
									xtype : 'displayfield',
									value : '',
									id:'hrefFileList',
									height:20,
									width:800,
									margin: '10 0 20 10',
									colspan:3

								},{
								xtype : 'displayfield',
								value : 'DOCUMENTACIÓN DE USO INTERNO:',
								height:20,
								margin: '50 20 0 10',
								hidden:true,
								colspan:3,
								fieldStyle: 'font-weight:bold'
							},{
								xtype : 'displayfield',
								value : '',
								id:'internalFileList',
								hidden:role=='ANONYMOUS'?true:false,
								height:20,
								width:800,
								margin: '10 0 0 10',
								colspan:3

							},{
									xtype: 'button',
									iconCls : 'icon-save',
									width:250,
									itemId : 'sendSupplierForm',
									id : 'sendSupplierForm',
									text : 'Enviar formato',
									action : 'sendSupplierForm',
									translate   : {
									    English : 'Submit Form',
									    Español : 'Enviar formato'
									   },
									colspan:3,
									fieldStyle: 'padding-left:350px;',
									hidden:role=='ANONYMOUS'?false:true
								},{
									xtype: 'button',
									iconCls : 'icon-save',
									width:250,
									itemId : 'updateSupplierForm',
									id : 'updateSupplierForm',
									text : 'Actualizar datos',
									translate   : {
									    English : 'Update record',
									    Español : 'Actualizar datos'
									   },
									action : 'updateSupplierForm',
									colspan:3,
									hidden:role=='ROLE_ADMIN'?false:true,
									fieldStyle: 'padding-left:350px;'
								},{
									xtype: 'button',
									iconCls : 'icon-save',
									width:250,
									itemId : 'updateSupplierFormDraft',
									id : 'updateSupplierFormDraft',
									text : 'Guardar datos como borrador',
									translate   : {
									    English : 'Save as Draft',
									    Español : 'Guardar datos como borrador'
									   },
									action : 'updateSupplierFormDraft',
									colspan:3,
									hidden:role=='ANONYMOUS'?false:true,
									fieldStyle: 'padding-left:350px;'
								},{
									xtype : 'displayfield',
									value : '',
									height:20,
									margin: '20 0 0 10',
									colspan:3

								},{
									xtype : 'hidden',
									name : 'fileList'
								},,{
									name : 'regionesTmp',
									hidden:true
							    },{
									name : 'categoriasTmp',
									hidden:true,
									colspan:3
								 },{
									name : 'currentApprover',
									hidden:true
								 },{
									name : 'nextApprover',
									hidden:true
								 }
								 


					]}
			];

		
	    this.dockedItems= [{
	        xtype   : 'toolbar',
	        padding : '2 0 10 10',
	        dock    : 'top',
	        ui      : 'footer',
	        items   : [{
	          text: 'English',
	          listeners : {
	            click: function(btn){
	              this.up('form').changeLabel(btn);
	            }
	          }
	        },{
	          text: 'Español',
	          listeners : {
	            click: function(btn){
	              this.up('form').changeLabel(btn);
	            }
	          }
	        }]
	      }];

		
		this.callParent(arguments);
	},
    changeLabel : function(btn) {
        var frm = btn.up('form'),
        lang = btn.getText();
        var tfs = frm.query('.textfield');
        Ext.each(tfs, function(tf){
        	if (typeof tf.translate !== "undefined") {
                tf.setFieldLabel(tf.translate[lang]);
        	}
        });
       
        var tfs = frm.query('.displayfield');
        Ext.each(tfs, function(tf){
        	if (typeof tf.translate !== "undefined") {
                tf.setValue(tf.translate[lang]);
        	}
        });
        
        var tfs = frm.query('.combobox');
        Ext.each(tfs, function(tf){
        	if (typeof tf.translate !== "undefined") {
                tf.setFieldLabel(tf.translate[lang]);
        	}
        });
        
        var tfs = frm.query('.button');
        Ext.each(tfs, function(tf){
        	if (typeof tf.translate !== "undefined") {
                tf.setText(tf.translate[lang]);
        	}
        });
      }

});