Ext.define('SupplierApp.view.supplier.SupplierForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.supplierForm',
	border : false,
	id:'supplierFormId',
	frame : false,
	style: 'border: solid #ccc 1px',
	autoScroll : true,
	 scrollable: true, 
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
						name : 'approvalStatus',
						id: 'approvalStatus'
					},{
						xtype : 'hidden',
						name : 'approvalStep'
					},{
						xtype : 'hidden',
						name : 'steps',
						id:'steps',
						value:2
					},{
						xtype : 'hidden',
						name : 'ukuid'
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
						   fieldLabel : SuppAppMsg.supplierForm1,
						   name : 'fechaSolicitud',
						   id:'fechaSolicitud',
						   //width:250,
						   flex:1,
						   xtype:'datefield',
						   format: 'd-m-Y',
						   margin:'0 0 20 10',
						   value: new Date(),
						   readOnly:true,
						   hidden : role == 'ROLE_SUPPLIER'?true:false,
						   fieldStyle: 'border:none;background-color: #ddd; background-image: none;'
						   },{
							xtype : 'displayfield',
							value : SuppAppMsg.supplierForm2,
							height:20,
							id: 'formatoSolicitud',
							margin: '50 0 10 10',
							colspan:2,
							//hidden : role == 'ROLE_SUPPLIER'?true:false,
							hidden:true,
							fieldStyle: 'font-weight:bold;font-size:20px;'
							},{//DACG
								name : 'dataUpdateList',
								itemId : 'dataUpdateList',
								id : 'dataUpdateList',
								//maxLength : 40,
								margin:'10 0 0 10',
								//width : 500,
								flex:1,
								readOnly:true,
								hidden: true
								},{//DACG
								name : 'updateApprovalFlow',
								itemId : 'updateApprovalFlow',
								id : 'updateApprovalFlow',
								//maxLength : 40,
								margin:'10 0 0 10',
								//width : 500,
								flex:1,
								readOnly:true,
								hidden: true
								},{ 
								xtype : 'displayfield',
								value : SuppAppMsg.supplierForm75,
								height:20,
								//width : 1000,
								flex:1,
								id: 'msgUpdSect',
								margin: '0 0 0 0',
								colspan:4,
								hidden : true,
								fieldStyle: 'font-weight:bold;font-size:10px;'
					            },{ 
								xtype : 'displayfield',
								value : false,
								height:20,
								//width : 1000,
								flex:1,
								id: 'originApproval',
								name: 'originApproval',
								margin: '0 0 0 0',
								colspan:4,
								hidden : true,
								readOnly:true
							     },{
								xtype:'container',
				                colspan:3,
				                hidden : role == 'ROLE_SUPPLIER'?true:false,
								layout: {
								    type: 'hbox',
								    pack: 'start',
								    align: 'stretch'
								},
								items: [{
									xtype:'textfield',
									labelWidth:130,
									fieldLabel : SuppAppMsg.approvalTicket,
									name : 'ticketForSearch',
									id : 'ticketForSearch',
									itemId : 'ticketForSearch',
									labelAlign:'left',
									margin:'0 5 0 0',
									maxWidth:250,
									flex:1,
									colspan:2,
									tip: SuppAppMsg.supplierForm3,
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
									maxWidth:60,
									flex:1,
									hidden : role == 'ROLE_SUPPLIER'?true:false,
									text : SuppAppMsg.suppliersSearch,
									action : 'searchTicket',
									id : 'searchTicket',
									maring:'0 0 0 0',
									cls: 'buttonStyle'
								}]
							},{
								fieldLabel : SuppAppMsg.suppliersNumber,
								name : 'addresNumber',
								labelAlign:'center',
								margin:'10 0 0 10',
								flex:1,
								colspan:3,
								readOnly:true,
								allowBlank:true,
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
								xtype: 'combobox',
				                name: 'country',
				                id: 'country',
				                fieldLabel: SuppAppMsg.purchaseTitle15 + '*',
				                typeAhead: true,
				                typeAheadDelay: 100,
				                allowBlank:false,
				                margin:'10 0 0 10',
				                minChars: 1,
				                colspan:3,
				                queryMode: 'local',
				                //forceSelection: true,
				                store : getAutoLoadUDCStore('COUNTRY', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width:400,
				                flex:1,
				                readOnly:role =='ROLE_SUPPLIER' ?true:false,
				                editable: false,
							    listeners: {
							    	select: function (comboBox, records, eOpts) { 
							    		var webSite = Ext.getCmp('webSite').getValue();
							    		var approvalStatus = Ext.getCmp('approvalStatus').getValue();
							    		if(webSite != 'SEND' && approvalStatus == 'DRAFT'){
							    			Ext.getCmp('rfcDocument').allowBlank = false;
							    			Ext.getCmp('domDocument').allowBlank = false;
							    			Ext.getCmp('edoDocument').allowBlank = false;
							    			Ext.getCmp('identDocument').allowBlank = false;
							    			Ext.getCmp('actaConstitutiva').allowBlank = false;
						        		}
							    		
							    		var currencyCode = Ext.getCmp('currencyCode').getValue();
							    		var countryCode = records[0].data.udcKey;
							    		
							    		if(currencyCode == 'MXP' && countryCode =='US'){
							    			Ext.getCmp('idFiscal').allowBlank=false;
							    			Ext.getCmp('idFiscal').setValue('');
							    		}else{
							    			Ext.getCmp('idFiscal').allowBlank=true;
							    			Ext.getCmp('idFiscal').setValue('');
							    		}
							    		
							    		Ext.getCmp('explFiscal').setValue('V');
							    		
							    		if(countryCode != 'MX'){
							    			Ext.getCmp('taxId').setValue('');
							    			Ext.getCmp('taxId').allowBlank=false;
							    			Ext.getCmp('taxId').show();
							    			
							    			Ext.getCmp('supRfc').hide();
							    			Ext.getCmp('supRfc').setValue(null);
							    			Ext.getCmp('supRfc').allowBlank=true;
							    			//Ext.getCmp('glClass').setValue('200');
							    			//Ext.getCmp('documentContainerForeingResidence').show();
							    			
							    			Ext.getCmp('searchCP').hide();
							    			
							    			Ext.getCmp('coloniaEXT').setValue('');
							    			Ext.getCmp('coloniaEXT').allowBlank=false;
							    			Ext.getCmp('coloniaEXT').show();
							    			
							    			Ext.getCmp('fldColonia').hide();
							    			Ext.getCmp('fldColonia').setValue(null);
							    			Ext.getCmp('fldColonia').allowBlank=true;
							    			Ext.getCmp('fldMunicipio').hide();
											Ext.getCmp('fldMunicipio').allowBlank=true;
							    			
							    			Ext.getCmp('codigoPostal').allowBlank=true;
							    			
							    			Ext.getCmp('estado').getStore().load({
							            	    callback: function(records, operation, success) {
							            	    	Ext.getCmp('estado').getStore().clearFilter();
									    			Ext.getCmp('estado').getStore().filterBy(function(rec, id) {
										    		    if(rec.get('keyRef') === "ext") {
										    		        return true;
										    		    }
										    		    else {
										    		        return false;
										    		    }
										    		});
							            	    }
							            	});
							    		}else{
							    			Ext.getCmp('supRfc').show();
							    			Ext.getCmp('supRfc').setValue('');
							    			Ext.getCmp('supRfc').allowBlank=false;
							    			
							    			Ext.getCmp('taxId').hide();
							    			Ext.getCmp('taxId').setValue(null);
							    			Ext.getCmp('taxId').allowBlank=true;
							    			//Ext.getCmp('glClass').setValue('100');
							    			//Ext.getCmp('documentContainerForeingResidence').hide();
							    			
							    			Ext.getCmp('searchCP').show();
							    			
							    			Ext.getCmp('fldColonia').show();
							    			Ext.getCmp('fldColonia').setValue('');
							    			Ext.getCmp('fldColonia').allowBlank=false;
							    			
							    			Ext.getCmp('coloniaEXT').hide();
							    			Ext.getCmp('coloniaEXT').setValue(null);
							    			Ext.getCmp('coloniaEXT').allowBlank=true;
							    			Ext.getCmp('codigoPostal').allowBlank=false;
							    			
							    			Ext.getCmp('fldMunicipio').show();
											Ext.getCmp('fldMunicipio').allowBlank=false;
											
											Ext.getCmp('estado').getStore().load({
							            	    callback: function(records, operation, success) {
							            	    	Ext.getCmp('estado').getStore().clearFilter();
									    			Ext.getCmp('estado').getStore().filterBy(function(rec, id) {
										    		    if(rec.get('keyRef') === "mx") {
										    		        return true;
										    		    }
										    		    else {
										    		        return false;
										    		    }
										    		});
							            	    }
							            	});
							    		}
							    	},
							    	afterrender: function(){						    		
							    	    var user = role;
							    		var countryField = Ext.getCmp('country');
							    	    
							    	    if (countryField && countryField.getValue() != null) {
							    	        var check = countryField.getValue();
							    	        
							    	        if (check != null && user == 'ROLE_SUPPLIER'){
							    	            // Tu lógica aquí...
							    	        }
							    	    } else {
							    	        // Si no tiene valor, esperar un poco y reintentar
							    	        setTimeout(function(){

							    	            var check = Ext.getCmp('country').getValue();
							    	            if (check != null && user == 'ROLE_SUPPLIER'){
							    	            	var currencyCode = Ext.getCmp('currencyCode').getValue();
										    		
										    		if(currencyCode == 'MXP' && check =='US'){
										    			Ext.getCmp('idFiscal').allowBlank=false;
										    		}else{
										    			Ext.getCmp('idFiscal').allowBlank=true;
										    		}
										    												    		
			                            		  if(check != 'MX'){
					                      				Ext.getCmp('supRfc').setReadOnly(true);
					                      				//Ext.getCmp('documentContainerForeingResidence').show();
					                      				Ext.getCmp('taxId').allowBlank=false;
					                      				Ext.getCmp('taxId').show();
					                      				Ext.getCmp('taxId').setReadOnly(false);
					                      				Ext.getCmp('supRfc').allowBlank=true;
					                      				Ext.getCmp('supRfc').hide();
					                      				//Ext.getCmp('regFiscal').hide();
					                      				//Ext.getCmp('regFiscal').allowBlank=true;
					                      				
					                      				var colonia = Ext.getCmp('fldColonia').getValue(); 
					                      				Ext.getCmp('coloniaEXT').allowBlank=false;
					                      				Ext.getCmp('coloniaEXT').show();
					                      				Ext.getCmp('coloniaEXT').setValue(colonia);
					                      				
					                      				Ext.getCmp('fldColonia').hide();
					                      				Ext.getCmp('fldColonia').allowBlank=true;
					                      				
					                      				Ext.getCmp('fldMunicipio').hide();
					                      				Ext.getCmp('fldMunicipio').allowBlank=true;
					                      				
					                      				Ext.getCmp('searchCP').hide();
					                      				Ext.getCmp('codigoPostal').allowBlank=true;
					                      				
					                      				/*Ext.getCmp('contCheckForeingBank').hide();
					                      				Ext.getCmp('swiftCode').show();
					                          			Ext.getCmp('ibanCode').show();
					                          			Ext.getCmp('checkingOrSavingAccount').show();
					                          			Ext.getCmp('rollNumber').show();
					                          			Ext.getCmp('bankAddressNumber').show();
					                          			Ext.getCmp('bankCountryCode').show();
					                          			Ext.getCmp('bankTransitNumber').allowBlank=true;
					                          			Ext.getCmp('custBankAcct').allowBlank=true;
					                          			Ext.getCmp('bankTransitNumber').minLength=0;
					                          			Ext.getCmp('custBankAcct').minLength=0;
					                          			Ext.getCmp('bankTransitNumber').maxLength=20;
											    		Ext.getCmp('custBankAcct').maxLength=20;
					                          			Ext.getCmp('bankTransitNumber').setFieldLabel("Bank Transit Number");
					                          			Ext.getCmp('custBankAcct').setFieldLabel("Cust Bank Account Number");
					                          			
					                          			Ext.getCmp('documentContainerForeingResidence').show();*/
					                      				
					                      				Ext.getCmp('custBankAcct').allowBlank=true;
					                      				//Ext.getCmp('custBankAcct').minLength=0;
					                                	//Ext.getCmp('custBankAcct').maxLength=0;
					                      				
					                      			//ReadOnly Datos del Proveedor
					                          		    Ext.getCmp('fisicaMoral').setReadOnly(true);
					                      				Ext.getCmp('currencyCode').setReadOnly(true);
					                      				Ext.getCmp('taxId').setReadOnly(true);
					                      				
					                      			    //ReadOnly Dirección Fiscal 
					                      				Ext.getCmp('calleNumero').setReadOnly(true);
					                      				Ext.getCmp('codigoPostal').setReadOnly(true);
					                      				Ext.getCmp('coloniaEXT').setReadOnly(true);
					                      				Ext.getCmp('estado').setReadOnly(true);
					                      				Ext.getCmp('phoneDF').setReadOnly(true);
					                      				Ext.getCmp('faxDF').setReadOnly(true);
					                      				
					                      			}else{

					                      				/*Ext.getCmp('fisicaMoral').setReadOnly(true);
					                      				Ext.getCmp('currencyCode').setReadOnly(true);
					                      				Ext.getCmp('nombreContactoCxC').setReadOnly(true);
					                      				Ext.getCmp('payInstCxC').setReadOnly(true);
					                      				Ext.getCmp('description').setReadOnly(true);
					                      				Ext.getCmp('controlDigit').setReadOnly(true);
					                      				Ext.getCmp('bankTransitNumber').setReadOnly(true);
					                      				Ext.getCmp('custBankAcct').setReadOnly(true);*/
					                      				Ext.getCmp('currencyCode').setReadOnly(true);
					                      				Ext.getCmp('supRfc').setReadOnly(true);
					                      				//Ext.getCmp('documentContainerForeingResidence').hide();
					                      				Ext.getCmp('supRfc').allowBlank=false;
					                      				Ext.getCmp('supRfc').show();
					                      				Ext.getCmp('taxId').allowBlank=true;
					                      				Ext.getCmp('taxId').hide();
					                      				//Ext.getCmp('regFiscal').show();
					                      				//Ext.getCmp('regFiscal').allowBlank=false;
					                      				
					                      				Ext.getCmp('fldColonia').show();
					                      				Ext.getCmp('fldColonia').allowBlank=false;
					                      				
					                      				Ext.getCmp('coloniaEXT').hide();
					                      				Ext.getCmp('coloniaEXT').allowBlank=true;
					                      				
					                      				Ext.getCmp('fldMunicipio').show();
					                      				Ext.getCmp('fldMunicipio').allowBlank=false;
					                      				
					                      				//Ext.getCmp('searchCP').hide();
					                      				Ext.getCmp('codigoPostal').allowBlank=false;
					                      				
					                      				//Ext.getCmp('contCheckForeingBank').show();
					                      				
					                      				//Ext.getCmp('swiftCode').hide();
					                          			//Ext.getCmp('ibanCode').hide();
					                          			//Ext.getCmp('checkingOrSavingAccount').hide();
					                          			//Ext.getCmp('rollNumber').hide();
					                          			//Ext.getCmp('bankAddressNumber').hide();
					                          			//Ext.getCmp('bankCountryCode').hide();
					                          			//Ext.getCmp('bankTransitNumber').allowBlank=false;
					                          			Ext.getCmp('custBankAcct').allowBlank=false;
					                          			//Ext.getCmp('bankTransitNumber').minLength=18;
					                          			Ext.getCmp('custBankAcct').maxLength=18;
					                          			Ext.getCmp('bankTransitNumber').maxLength=18;
											    		Ext.getCmp('custBankAcct').maxLength=18;
					                          			Ext.getCmp('bankTransitNumber').setFieldLabel(SuppAppMsg.supplierForm52);
					                          			Ext.getCmp('custBankAcct').setFieldLabel(SuppAppMsg.supplierForm53);
					                      				
					                          			Ext.getCmp('documentContainerForeingResidence').hide();
					                      				
					                      				/*Ext.getCmp('contCheckForeingBank').show();
					                      				Ext.getCmp('swiftCode').hide();
					                          			Ext.getCmp('ibanCode').hide();
					                          			Ext.getCmp('checkingOrSavingAccount').hide();
					                          			Ext.getCmp('rollNumber').hide();
					                          			Ext.getCmp('bankAddressNumber').hide();
					                          			Ext.getCmp('bankCountryCode').hide();
					                          			Ext.getCmp('bankTransitNumber').allowBlank=false;
					                          			Ext.getCmp('custBankAcct').allowBlank=false;
					                          			Ext.getCmp('bankTransitNumber').minLength=18;
					                          			Ext.getCmp('custBankAcct').minLength=18;
					                          			Ext.getCmp('bankTransitNumber').maxLength=18;
											    		Ext.getCmp('custBankAcct').maxLength=18;
					                          			Ext.getCmp('bankTransitNumber').setFieldLabel(SuppAppMsg.supplierForm52);
					                          			Ext.getCmp('custBankAcct').setFieldLabel(SuppAppMsg.supplierForm53);
					                      				
					                          			Ext.getCmp('documentContainerForeingResidence').hide();*/
					                          			

					                          			
					                          			//ReadOnly
					                          					                          			
					                          			//ReadOnly Datos del Proveedor MX
					                          		    //Ext.getCmp('emailSupplier').setReadOnly(true);
					                      				Ext.getCmp('fisicaMoral').setReadOnly(true);
					                      				Ext.getCmp('currencyCode').setReadOnly(true);
					                      				//Ext.getCmp('regFiscal').setReadOnly(true);
					                      				
					                      				//ReadOnly Dirección Fiscal MX
					                      				Ext.getCmp('calleNumero').setReadOnly(true);
					                      				Ext.getCmp('fldMunicipio').setReadOnly(true);
					                      				Ext.getCmp('codigoPostal').setReadOnly(true);
					                      				
					                      				Ext.getCmp('fldColonia').setReadOnly(true);
					                      				Ext.getCmp('estado').setReadOnly(true);
					                      				Ext.getCmp('phoneDF').setReadOnly(true);
					                      				Ext.getCmp('faxDF').setReadOnly(true);
					                      				Ext.getCmp('searchCP').hide();
					                      			}  
			                            		  
			                            		  //ReadOnly Contactos Proveedor
			                      					Ext.getCmp('nombreContactoCxC').setReadOnly(true);
			                      					Ext.getCmp('cargoCxC').setReadOnly(true);
			                      					Ext.getCmp('emailSupplier').setReadOnly(true);
			                      					Ext.getCmp('nombreCxP01').setReadOnly(true);
				                      				Ext.getCmp('emailCxP01').setReadOnly(true);
				                      				Ext.getCmp('telefonoCxP01').setReadOnly(true);
				                      				Ext.getCmp('nombreCxP02').setReadOnly(true);
				                      				Ext.getCmp('emailCxP02').setReadOnly(true);
				                      				Ext.getCmp('telefonoCxP02').setReadOnly(true);
				                      				Ext.getCmp('nombreCxP03').setReadOnly(true);
				                      				Ext.getCmp('emailCxP03').setReadOnly(true);
				                      				Ext.getCmp('telefonoCxP03').setReadOnly(true);
				                      				Ext.getCmp('nombreCxP04').setReadOnly(true);
				                      				Ext.getCmp('emailCxP04').setReadOnly(true);
				                      				Ext.getCmp('telefonoCxP04').setReadOnly(true);
				                      				
				                      	  //ReadOnly Representante Legal
				                      				
				                      				Ext.getCmp('tipoIdentificacion').setReadOnly(true);
				                                	Ext.getCmp('numeroIdentificacion').setReadOnly(true);
				                                	Ext.getCmp('nombreRL').setReadOnly(true);
				                                	Ext.getCmp('apellidoPaternoRL').setReadOnly(true);
				                                	Ext.getCmp('apellidoMaternoRL').setReadOnly(true);	
				                                	
				                         //ReadOnly Datos Bancarios
				                                	
				                                	 Ext.getCmp('bankTransitNumber').setReadOnly(true);
				                                	 Ext.getCmp('custBankAcct').setReadOnly(true);
				                                	 Ext.getCmp('controlDigit').setReadOnly(true);
				                                	 Ext.getCmp('description').setReadOnly(true);
				                                	 //Ext.getCmp('checkForeingBank').setReadOnly(true);
				                                	 //Ext.getCmp('outSourcing').setReadOnly(true);
				                                	 Ext.getCmp('bankCountryCode').setReadOnly(true);
				                                	 Ext.getCmp('rollNumber').setReadOnly(true);
				                                	 Ext.getCmp('ibanCode').setReadOnly(true);
				                                	 Ext.getCmp('idFiscal').setReadOnly(true);
				                                	 Ext.getCmp('swiftCode').setReadOnly(true);
				                                	 Ext.getCmp('bankAddressNumber').setReadOnly(true);
				                      								                      				
				                        //ReadOnly Datos Bancarios Extranjeros
				                                	 
				                                	/* Ext.getCmp('swiftCode').setReadOnly(true);
				                                	 Ext.getCmp('ibanCode').setReadOnly(true);
				                                	 Ext.getCmp('checkingOrSavingAccount').setReadOnly(true);
				                                	 Ext.getCmp('rollNumber').setReadOnly(true);
				                                	 Ext.getCmp('bankAddressNumber').setReadOnly(true);
				                                	 Ext.getCmp('bankCountryCode').setReadOnly(true);	*/
				                        //ReadOnly Documentos
				                                	 
				                                	 Ext.getCmp('loadEdoDoc').setDisabled(true);
				                                	 Ext.getCmp('loadRfcDoc').setDisabled(true);
				                                	// Ext.getCmp('loadObligacionesFiscales').setDisabled(true);
				                                	 Ext.getCmp('loadIdentDoc').setDisabled(true);
				                                	// Ext.getCmp('loadRpcDocument').setDisabled(true);
				                                	 Ext.getCmp('loadDomDoc').setDisabled(true);	
				                                	 Ext.getCmp('loadActaConst').setDisabled(true);
				                                	 
				                                	// Ext.getCmp('emailSupplierNewSupp').hide();
													 //Ext.getCmp('emailSupplierNewSupp').allowBlank=true;
													 
													//Allow Blank
													 
													//MX
					                      			Ext.getCmp('fisicaMoral').allowBlank=true;
					                      			Ext.getCmp('currencyCode').allowBlank=true;
					                      			//Ext.getCmp('regFiscal').allowBlank=true;
					                      			Ext.getCmp('calleNumero').allowBlank=true;
					                      			Ext.getCmp('fldMunicipio').allowBlank=true;
					                      			Ext.getCmp('codigoPostal').allowBlank=true;
					                       			Ext.getCmp('fldColonia').allowBlank=true;
					                      			Ext.getCmp('estado').allowBlank=true;
					                      			Ext.getCmp('phoneDF').allowBlank=true;
					                      			Ext.getCmp('faxDF').allowBlank=true;
					                      				
					                      		//Ext
				                      				
				                      				Ext.getCmp('fisicaMoral').allowBlank=true;
					                      			Ext.getCmp('currencyCode').allowBlank=true;
					                      			Ext.getCmp('taxId').allowBlank=true;
					                   				Ext.getCmp('calleNumero').allowBlank=true;
					                   				Ext.getCmp('codigoPostal').allowBlank=true;
					                   				Ext.getCmp('coloniaEXT').allowBlank=true;
					                   				Ext.getCmp('estado').allowBlank=true;
					                   				Ext.getCmp('phoneDF').allowBlank=true;
					                   				Ext.getCmp('faxDF').allowBlank=true;
					                   				
					                   			//ReadOnly Contactos Proveedor
			                      					Ext.getCmp('nombreContactoCxC').allowBlank=true;
			                      					Ext.getCmp('emailComprador').allowBlank=true;
			                      					Ext.getCmp('telefonoCxC').allowBlank=true;
			                      					Ext.getCmp('emailSupplier').allowBlank=true;
			                      					
			                      					
			                      					
			                      					Ext.getCmp('cargoCxC').allowBlank=true;
			                      					Ext.getCmp('emailSupplier').allowBlank=true;
			                      					Ext.getCmp('nombreCxP01').allowBlank=true;
				                      				Ext.getCmp('emailCxP01').allowBlank=true;
				                      				Ext.getCmp('telefonoCxP01').allowBlank=true;
				                      				Ext.getCmp('nombreCxP02').allowBlank=true;
				                      				Ext.getCmp('emailCxP02').allowBlank=true;
				                      				Ext.getCmp('telefonoCxP02').allowBlank=true;
				                      				Ext.getCmp('nombreCxP03').allowBlank=true;
				                      				Ext.getCmp('emailCxP03').allowBlank=true;
				                      				Ext.getCmp('telefonoCxP03').allowBlank=true;
				                      				Ext.getCmp('nombreCxP04').allowBlank=true;
				                      				Ext.getCmp('emailCxP04').allowBlank=true;
				                      				Ext.getCmp('telefonoCxP04').allowBlank=true;
				                      			
				                      		//ReadOnly Representante Legal
				                      				
				                      				Ext.getCmp('tipoIdentificacion').allowBlank=true;
				                                	Ext.getCmp('numeroIdentificacion').allowBlank=true;
				                                	Ext.getCmp('nombreRL').allowBlank=true;
				                                	Ext.getCmp('apellidoPaternoRL').allowBlank=true;
				                                	Ext.getCmp('apellidoMaternoRL').allowBlank=true;	
				                                	
				                         //Datos Bancarios
				                                	
				                                	// Ext.getCmp('bankTransitNumber').allowBlank=true;
				                                	 Ext.getCmp('custBankAcct').allowBlank=true;
				                                	 Ext.getCmp('controlDigit').allowBlank=true;
				                                	 Ext.getCmp('description').allowBlank=true;
				                                	 
				                                	  Ext.getCmp('custBankAcct').maxLength=28;
				                                	 
				                                	// Ext.getCmp('checkForeingBank').allowBlank=true;
				                          //Bancarios Extranjeros
				                                	 
				                                	 Ext.getCmp('swiftCode').allowBlank=true;
				                                	 Ext.getCmp('ibanCode').allowBlank=true;
				                                	// Ext.getCmp('checkingOrSavingAccount').allowBlank=true;
				                                	 Ext.getCmp('rollNumber').allowBlank=true;
				                                	 Ext.getCmp('bankAddressNumber').allowBlank=true;
				                                	 Ext.getCmp('bankCountryCode').allowBlank=true;
				                                	 //Documentos
				                                	 
				                                	 Ext.getCmp('loadEdoDoc').allowBlank=true;
				                                	 Ext.getCmp('loadRfcDoc').allowBlank=true;
				                                	// Ext.getCmp('loadObligacionesFiscales').allowBlank=true;
				                                	 Ext.getCmp('loadIdentDoc').allowBlank=true;
				                                	 //Ext.getCmp('loadRpcDocument').allowBlank=true;
				                                	 Ext.getCmp('loadDomDoc').allowBlank=true;	
													// Ext.getCmp('emailSupplierNewSupp').allowBlank=true;  
													 Ext.getCmp('actaConstitutiva').allowBlank=true;
							    	            }else{
							    	            	 Ext.getCmp('checkEditDataSupp').hide();
					          				        	Ext.getCmp('lblEditDataSupp').hide();
					          				        	//Ext.getCmp('checkEditFiscalAddress').hide();
					          				        	//Ext.getCmp('lblEditFiscalAddress').hide();
					          				        	Ext.getCmp('checkEditContact').hide();
					          				        	Ext.getCmp('lblEditContact').hide();
					          				        	//Ext.getCmp('checkEditLegalRepr').hide();
					          				        	//Ext.getCmp('lblEditLegalRepr').hide();
					          				        	Ext.getCmp('checkEditDataBank').hide();
					          				        	Ext.getCmp('lblEditDataBank').hide();	
							    	            }
							    	        }, 200);
							    	    }
							    	
		                            	 
		                              }
							    }
							},{//Contenedor de Datos Proveedor
								xtype:'container',
								id:'contCheckEditDataSupp',
								layout:'hbox',
								colspan:3,
								margin: '50 0 0 0',
							    width:'100%',
							    items:[{
									xtype : 'displayfield',
									value : SuppAppMsg.supplierForm4,
									height:20,
									//margin: '50 0 0 10',
									colspan:3,
									fieldStyle: 'font-weight:bold',
								    fieldCls: 'no-border-displayfield'
									},{
									xtype : 'checkbox',
									name : 'checkEditDataSupp',
									id : 'checkEditDataSupp',
									margin: '0 0 0 90',
									//width : 20,
									flex:1,
									checked: false,
									//hidden:false,
									hidden: isMainSupplierUser ? false : true, //Solo el proveedor puede modificar su información
									listeners: {
			                              change: function (checkbox, newVal, oldVal) {
			                            	  var check = Ext.getCmp('country').getValue();
			                                  if (newVal == true) { 
			                                    Ext.getCmp('loadRfcDoc').setDisabled(false);
				                                Ext.getCmp('rfcDocument').allowBlank=false;
						                        Ext.getCmp('loadDomDoc').setDisabled(false);	
						                        Ext.getCmp('domDocument').allowBlank=false;
			                                	
						                      //  Ext.getCmp('emailSupplier').setReadOnly(false);
						                      //  Ext.getCmp('emailSupplier').allowBlank=false;
						                        
						                        //DIRECCION FISCAL
						                        Ext.getCmp('loadDomDoc').setDisabled(false);	
						                        Ext.getCmp('domDocument').allowBlank=false;
						                        
						                        //REP LEGAL
						                        
						                        var fisicaMoral = Ext.getCmp('fisicaMoral').getValue();	
						                        if(fisicaMoral == '1'){
						                        	
						                        
			                                	  Ext.getCmp('tipoIdentificacion').setReadOnly(true);
			                                	  Ext.getCmp('numeroIdentificacion').setReadOnly(true);
			                                	  Ext.getCmp('nombreRL').setReadOnly(true);
			                                	  Ext.getCmp('apellidoPaternoRL').setReadOnly(true);
			                                	  Ext.getCmp('apellidoMaternoRL').setReadOnly(true);	 
			                                	  
			                                	  Ext.getCmp('tipoIdentificacion').allowBlank=true;
			                                	  Ext.getCmp('numeroIdentificacion').allowBlank=true;
			                                	  Ext.getCmp('nombreRL').allowBlank=true;
			                                	  Ext.getCmp('apellidoPaternoRL').allowBlank=true;	
			                                  }else{

			                                	  Ext.getCmp('tipoIdentificacion').setReadOnly(false);
			                                	  Ext.getCmp('numeroIdentificacion').setReadOnly(false);
			                                	  Ext.getCmp('nombreRL').setReadOnly(false);
			                                	  Ext.getCmp('apellidoPaternoRL').setReadOnly(false);
			                                	  Ext.getCmp('apellidoMaternoRL').setReadOnly(false);	 
			                                	  
			                                	  Ext.getCmp('tipoIdentificacion').allowBlank=false;
			                                	  Ext.getCmp('numeroIdentificacion').allowBlank=false;
			                                	  Ext.getCmp('nombreRL').allowBlank=false;
			                                	  Ext.getCmp('apellidoPaternoRL').allowBlank=false;	
			                                  }
						                        
			                                	  	if(check == 'MX'){
				                      			Ext.getCmp('fisicaMoral').setReadOnly(false);
				                      			//Ext.getCmp('currencyCode').setReadOnly(false);
				                      			Ext.getCmp('fisicaMoral').allowBlank=false;
				                      			//Ext.getCmp('currencyCode').allowBlank=false;
				                      			
				                      			 //DIRECCION FISCAL
				                      			Ext.getCmp('calleNumero').setReadOnly(false);
					                      		Ext.getCmp('fldMunicipio').setReadOnly(false);
					                      		Ext.getCmp('codigoPostal').setReadOnly(false);
					                      		Ext.getCmp('fldColonia').setReadOnly(false);
					                      		Ext.getCmp('estado').setReadOnly(false);
					                      		Ext.getCmp('phoneDF').setReadOnly(false);
					                      		Ext.getCmp('faxDF').setReadOnly(false);
					                      		Ext.getCmp('searchCP').show();
					                      		
					                      		Ext.getCmp('calleNumero').allowBlank=false;
					                      		Ext.getCmp('fldMunicipio').allowBlank=false;
					                      		Ext.getCmp('codigoPostal').allowBlank=false;
					                      		Ext.getCmp('fldColonia').allowBlank=false;
					                      		Ext.getCmp('estado').allowBlank=false;
					                      		Ext.getCmp('phoneDF').allowBlank=false;
				                      			
			                                	  	}else{
			                                	
					                      		Ext.getCmp('fisicaMoral').setReadOnly(false);
					                      		//Ext.getCmp('currencyCode').setReadOnly(false);
					                      		//Ext.getCmp('regFiscal').setReadOnly(false);		
					                      		
					                      		Ext.getCmp('fisicaMoral').allowBlank=false;
				                      			//Ext.getCmp('currencyCode').allowBlank=false;
				                      		//	Ext.getCmp('regFiscal').allowBlank=false;
				                      			
					                      	//DIRECCION FISCAL
					                      		Ext.getCmp('calleNumero').setReadOnly(false);
					                      		Ext.getCmp('codigoPostal').setReadOnly(false);
					                      		Ext.getCmp('coloniaEXT').setReadOnly(false);
					                      		Ext.getCmp('estado').setReadOnly(false);
					                      		Ext.getCmp('phoneDF').setReadOnly(false);
					                      		Ext.getCmp('faxDF').setReadOnly(false);		
					                      		
					                      		Ext.getCmp('calleNumero').allowBlank=false;
					                      		Ext.getCmp('codigoPostal').allowBlank=false;
					                      		Ext.getCmp('coloniaEXT').allowBlank=false;
					                      		Ext.getCmp('estado').allowBlank=false;
					                      		Ext.getCmp('phoneDF').allowBlank=false;
			                                	  	}
			                                  }else{
			                                	  
			                                	 /* if(!Ext.getCmp('checkEditFiscalAddress').checked) {
			                                	Ext.getCmp('loadRfcDoc').setDisabled(true);
					                            Ext.getCmp('rfcDocument').allowBlank=true;
							                    Ext.getCmp('loadDomDoc').setDisabled(true);	
							                    Ext.getCmp('domDocument').allowBlank=true;
							                    
							                  //  Ext.getCmp('emailSupplier').setReadOnly(true);
						                       // Ext.getCmp('emailSupplier').allowBlank=true;
						                        
			                                	  }*/
			                                	  
			                                	    Ext.getCmp('loadRfcDoc').setDisabled(true);
					                                Ext.getCmp('rfcDocument').allowBlank=true;
							                        Ext.getCmp('loadDomDoc').setDisabled(true);	
							                        Ext.getCmp('domDocument').allowBlank=true;
							                        
							                      //REP LEGAL
							                        
							                        //Ext.getCmp('loadIdentDoc').setDisabled(false);
				                                	 // Ext.getCmp('identDocument').allowBlank=false;
					                                 // Ext.getCmp('loadRpcDocument').setDisabled(false);
					                                //  Ext.getCmp('rpcDocument').allowBlank=false;					                                  
				                                	  Ext.getCmp('tipoIdentificacion').setReadOnly(true);
				                                	  Ext.getCmp('numeroIdentificacion').setReadOnly(true);
				                                	  Ext.getCmp('nombreRL').setReadOnly(true);
				                                	  Ext.getCmp('apellidoPaternoRL').setReadOnly(true);
				                                	  Ext.getCmp('apellidoMaternoRL').setReadOnly(true);	 
				                                	  
				                                	  Ext.getCmp('tipoIdentificacion').allowBlank=true;
				                                	  Ext.getCmp('numeroIdentificacion').allowBlank=true;
				                                	  Ext.getCmp('nombreRL').allowBlank=true;
				                                	  Ext.getCmp('apellidoPaternoRL').allowBlank=true;	
				                                	  
			                                	  
			                                	  if(check == 'MX'){
			                                	Ext.getCmp('fisicaMoral').setReadOnly(true);
					                      		//Ext.getCmp('currencyCode').setReadOnly(true);
					                      
					                      		Ext.getCmp('fisicaMoral').allowBlank=true;
				                      			//Ext.getCmp('currencyCode').allowBlank=true;
					                      		
					                      	//DIRECCION FISCAL
							                    Ext.getCmp('calleNumero').setReadOnly(true);
							                    Ext.getCmp('fldMunicipio').setReadOnly(true);
							                    Ext.getCmp('codigoPostal').setReadOnly(true);
							                    Ext.getCmp('fldColonia').setReadOnly(true);
							                    Ext.getCmp('estado').setReadOnly(true);
							                    Ext.getCmp('phoneDF').setReadOnly(true);
							                    Ext.getCmp('faxDF').setReadOnly(true);
							                    Ext.getCmp('searchCP').hide();
							                    
							                    Ext.getCmp('calleNumero').allowBlank=true;
							                    Ext.getCmp('fldMunicipio').allowBlank=true;
							                    Ext.getCmp('codigoPostal').allowBlank=true;
							                    Ext.getCmp('fldColonia').allowBlank=true;
							                    Ext.getCmp('estado').allowBlank=true;
							                    Ext.getCmp('phoneDF').allowBlank=true;
							                    //Ext.getCmp('faxDF').allowBlank=true;
				                      		
			                                	  }else{
			                                	Ext.getCmp('fisicaMoral').setReadOnly(true);
							                  //  Ext.getCmp('currencyCode').setReadOnly(true);
							                
							                    Ext.getCmp('fisicaMoral').allowBlank=true;
				                      			//Ext.getCmp('currencyCode').allowBlank=true;
							                    
							                  //DIRECCION FISCAL
							                    Ext.getCmp('calleNumero').setReadOnly(true);
							                    Ext.getCmp('codigoPostal').setReadOnly(true);
							                    Ext.getCmp('coloniaEXT').setReadOnly(true);
							                    Ext.getCmp('estado').setReadOnly(true);
							                    Ext.getCmp('phoneDF').setReadOnly(true);
							                    Ext.getCmp('faxDF').setReadOnly(true);	
							                    
							                    Ext.getCmp('calleNumero').allowBlank=true;
							                    Ext.getCmp('codigoPostal').allowBlank=true;
							                    Ext.getCmp('coloniaEXT').allowBlank=true;
							                    Ext.getCmp('estado').allowBlank=true;
							                    Ext.getCmp('phoneDF').allowBlank=true;
							                    //Ext.getCmp('faxDF').allowBlank=true;
							                    
			                                	  }
			                                  }
			                                  
			                                  if(!Ext.getCmp('checkEditDataBank').checked) {
			                                	Ext.getCmp('custBankAcct').maxLength=28;
			                                  }
			                              }
			                          }
									},{
				                        xtype: 'displayfield',
				                        width:'100%',
				                        value: SuppAppMsg.supplierForm71,
				                        id: 'lblEditDataSupp',
				                       // height:20,
										margin: '0 0 0 0',
										colspan:3,
										hidden: isMainSupplierUser ? false : true //Solo el proveedor puede modificar su información
				                    }]
								},/*{
							xtype : 'displayfield',
							value : SuppAppMsg.supplierForm4,
							height:20,
							margin: '50 0 0 10',
							colspan:3,
							fieldStyle: 'font-weight:bold'
							},*/{
						   fieldLabel : SuppAppMsg.supplierForm5,
						   name : 'razonSocial', 
						   itemId : 'razonSocial',
						   //maskRe: /[A-Za-z &]/, 
						   //stripCharsRe: /[^A-Za-z &]/,
						   margin:'10 0 0 10',
						   //width : 500,
						   flex:1,
						   maxLength : 40,
						   ////enforceMaxLength : true,
						   maxLengthText : SuppAppMsg.supplierForm68 + '{0}<br>',
						   listeners:{
								change: function(field, newValue, oldValue){
									field.setValue(newValue.toUpperCase());
								}
							},
							allowBlank:false,
							readOnly:role=='ANONYMOUS'?false:true,
							/*regex: /[A-Za-z]/,
							regexText: " ",
							validator: function(v) {
							   if(/[A-Za-z &]/.test(v)){
								   return true;
							   }else return false;
							   //return /[A-Za-z &]/.test(v)?true:"Solo permitido espacios y &";
							}*/
						   },{
								fieldLabel : SuppAppMsg.supplierForm6,
								name : 'rfc',
								id:'supRfc',
								colspan:2,
								maskRe: /[A-Za-z\d]/,
								stripCharsRe: /[^A-Za-z\d]/,
							    vtype:'rfc',
							    maxLength : 13,
								allowBlank:false,
								maxLengthText : SuppAppMsg.supplierForm68 + '{0}<br>',
							    listeners:{
									change: function(field, newValue, oldValue){
										var webSite = Ext.getCmp('webSite').getValue();
							    		var approvalStatus = Ext.getCmp('approvalStatus').getValue();
							    		if(webSite != 'SEND' && approvalStatus == 'DRAFT'){
							    			Ext.getCmp('rfcDocument').allowBlank = false;
							    			Ext.getCmp('domDocument').allowBlank = false;
							    			Ext.getCmp('edoDocument').allowBlank = false;
							    			Ext.getCmp('identDocument').allowBlank = false;
							    			Ext.getCmp('actaConstitutiva').allowBlank = false;
							    			
							    			var fileList = rec.data.fileList;
								        	var hrefList ="";
								        	var r2 = [];
								        	var href ="";
								        	var fileHref="";
						
								        	if(fileList != ''){
								        		var r1 = fileList.split("_FILE:");
									        	var inx = r1.length;
									        	for (var index = inx - 1; index >= 0; index--) {
									        		r2 = r1[index].split("_:_");
										        		if(r2[0]  != ""){
											        	href = "documents/openDocument.action?id=" + r2[0];
											        	
											        	var typeDoc = "";
											        	if(r2[1].includes("*1*")){
											        		r2[1]=r2[1].replace("*1*",'');
											        		typeDoc= SuppAppMsg.supplierForm36;
											        	}else if(r2[1].includes("*2*")){
											        		r2[1]=r2[1].replace("*2*",'');
											        		typeDoc= SuppAppMsg.supplierForm37;
											        	}else if(r2[1].includes("*3*")){
											        		r2[1]=r2[1].replace("*3*",'');
											        		typeDoc= SuppAppMsg.supplierForm38;
											        	}else if(r2[1].includes("*4*")){
											        		r2[1]=r2[1].replace("*4*",'');
											        		typeDoc= SuppAppMsg.supplierForm39;
											        	}else if(r2[1].includes("*5*")){
											        		r2[1]=r2[1].replace("*5*",'');
											        		typeDoc= SuppAppMsg.supplierForm40;
											        	}
											        	
														fileHref = typeDoc +"** <a href= '" + href + "' target='_blank'>" + r2[1]  + "</a> ||" + r2[2];
														hrefList = "<p>"  + hrefList + fileHref + "</p>";
									        		}
									        	} 
									        	Ext.getCmp('hrefFileList').setValue(hrefList);
								        	}
						        		}
							    		
										if(typeof newValue !== 'undefined' && typeof oldValue !== 'undefined'){
											if(newValue.toUpperCase() != oldValue.toUpperCase()){
												Ext.getCmp('hrefFileList').setValue('');
												Ext.getCmp('rfcDocument').setValue('');
												Ext.getCmp('domDocument').setValue('');
												Ext.getCmp('edoDocument').setValue('');
												Ext.getCmp('identDocument').setValue('');
												Ext.getCmp('actaConstitutiva').setValue('');
												//Ext.getCmp('rpcDocument').setValue('');
												Ext.getCmp('legalExistence').setValue('');
												Ext.getCmp('foreingResidence').setValue('');
												//Ext.getCmp('identDocument').setValue('');
											}
										}
										field.setValue(newValue.toUpperCase());
									}
							    },
							    checkChangeEvents:['change'],
								readOnly:role=='ANONYMOUS'?false:true,
								//enforceMaxLength : true
						    },{
								fieldLabel : 'TaxId*',
								name : 'taxId',
								id:'taxId',
								allowBlank:true,
								maxLength : 20,
								maskRe: /[A-Za-z\d]/,
								stripCharsRe: /[^A-Za-z\d]/,
								maxLengthText : SuppAppMsg.supplierForm68 + '{0}<br>',
								colspan:3,
							    listeners:{
									change: function(field, newValue, oldValue){
										var webSite = Ext.getCmp('webSite').getValue();
							    		var approvalStatus = Ext.getCmp('approvalStatus').getValue();
							    		if(webSite != 'SEND' && approvalStatus == 'DRAFT'){
							    			Ext.getCmp('rfcDocument').allowBlank = false;
							    			Ext.getCmp('domDocument').allowBlank = false;
							    			Ext.getCmp('edoDocument').allowBlank = false;
							    			Ext.getCmp('identDocument').allowBlank = false;
							    			Ext.getCmp('actaConstitutiva').allowBlank = false;
						        		}
							    		
										if(typeof newValue !== 'undefined' && typeof oldValue !== 'undefined'){
											if(newValue.toUpperCase() != oldValue.toUpperCase()){
												Ext.getCmp('hrefFileList').setValue('');
												Ext.getCmp('rfcDocument').setValue('');
												Ext.getCmp('domDocument').setValue('');
												Ext.getCmp('edoDocument').setValue('');
												Ext.getCmp('identDocument').setValue('');
												Ext.getCmp('actaConstitutiva').setValue('');
												//Ext.getCmp('rpcDocument').setValue('');
												Ext.getCmp('legalExistence').setValue('');
												Ext.getCmp('foreingResidence').setValue('');
												//Ext.getCmp('identDocument').setValue('');
											}
										}
										field.setValue(newValue.toUpperCase());
									}
								},
								readOnly:role=='ANONYMOUS'?false:true,
								//enforceMaxLength : true
							 }/*,{
								fieldLabel : SuppAppMsg.supplierForm7,
								name : 'emailSupplierNewSupp',
								id : 'emailSupplierNewSupp',
								width:450,
								allowBlank:false,blankText: SuppAppMsg.supplierForm74,
								blankText: SuppAppMsg.supplierForm74,
								maxLength : 254,
								//enforceMaxLength : true,
								//vtype: 'email',
								//vtypeText : SuppAppMsg.supplierForm73,
					            listeners:{
					            	render: function(c) {
								        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      },
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toLowerCase());
									}
								}
						   }*//*,{
								fieldLabel : SuppAppMsg.supplierForm7,
								name : 'emailSupplierNewSupp',
								id : 'emailSupplierNewSupp',
								width:450,
								allowBlank:false,blankText: SuppAppMsg.supplierForm74,
								blankText: SuppAppMsg.supplierForm74,
								maxLength : 254,
								//enforceMaxLength : true,
								//vtype: 'email',
								//vtypeText : SuppAppMsg.supplierForm73,
					            listeners:{
					            	render: function(c) {
								        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      },
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toLowerCase());
									}
								}
						   }*/,{
								fieldLabel : SuppAppMsg.supplierForm8,
								name : 'fisicaMoral',
								id : 'fisicaMoral',
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
				                //width : 250,
				                flex:1,
				                editable: false,
								colspan:3,
								listeners: {
									afterrender: function(){
										var check = Ext.getCmp('fisicaMoral').getValue();
										
										if(check == '1'){
											Ext.getCmp('REPRESENTE_LEGAL').hide();
											Ext.getCmp('tipoIdentificacion').allowBlank=true;
											Ext.getCmp('tipoIdentificacion').hide();
											Ext.getCmp('numeroIdentificacion').allowBlank=true;
											Ext.getCmp('numeroIdentificacion').hide();
											Ext.getCmp('nombreRL').allowBlank=true;
											Ext.getCmp('nombreRL').hide();
											Ext.getCmp('apellidoPaternoRL').allowBlank=true;
											Ext.getCmp('apellidoPaternoRL').hide();
							    			Ext.getCmp('apellidoMaternoRL').hide();
							    			//Ext.getCmp('checkEditLegalRepr').hide();
							    			//Ext.getCmp('lblEditLegalRepr').hide();	
							    			
							    			//Rep Legal
							    			
							    			// Ext.getCmp('loadIdentDoc').setDisabled(true);
		                                	//  Ext.getCmp('identDocument').allowBlank=true;
			                                  //Ext.getCmp('loadRpcDocument').setDisabled(true);
			                                 // Ext.getCmp('rpcDocument').allowBlank=true;
		                                	  Ext.getCmp('tipoIdentificacion').setReadOnly(true);
		                                	  Ext.getCmp('numeroIdentificacion').setReadOnly(true);
		                                	  Ext.getCmp('nombreRL').setReadOnly(true);
		                                	  Ext.getCmp('apellidoPaternoRL').setReadOnly(true);
		                                	  Ext.getCmp('apellidoMaternoRL').setReadOnly(true);
		                                	  
		                                	  Ext.getCmp('tipoIdentificacion').allowBlank=true;
		                                	  Ext.getCmp('numeroIdentificacion').allowBlank=true;
		                                	  Ext.getCmp('nombreRL').allowBlank=true;
		                                	  Ext.getCmp('apellidoPaternoRL').allowBlank=true;
										}else{
											Ext.getCmp('REPRESENTE_LEGAL').show();
											Ext.getCmp('tipoIdentificacion').allowBlank=true;
											Ext.getCmp('tipoIdentificacion').show();
											Ext.getCmp('numeroIdentificacion').allowBlank=true;
											Ext.getCmp('numeroIdentificacion').show();
											Ext.getCmp('nombreRL').allowBlank=true;
											Ext.getCmp('nombreRL').show();
											Ext.getCmp('apellidoPaternoRL').allowBlank=true;
											Ext.getCmp('apellidoPaternoRL').show();
							    			Ext.getCmp('apellidoMaternoRL').show();
							    			//Ext.getCmp('checkEditLegalRepr').show();
							    			//Ext.getCmp('lblEditLegalRepr').show();
							    			//Rep Legal
							    			 //Ext.getCmp('loadIdentDoc').setDisabled(true);
		                                	 // Ext.getCmp('identDocument').allowBlank=true;
			                                 // Ext.getCmp('loadRpcDocument').setDisabled(false);
			                                  //Ext.getCmp('rpcDocument').allowBlank=false;					                                  
		                                	  Ext.getCmp('tipoIdentificacion').setReadOnly(false);
		                                	  Ext.getCmp('numeroIdentificacion').setReadOnly(false);
		                                	  Ext.getCmp('nombreRL').setReadOnly(false);
		                                	  Ext.getCmp('apellidoPaternoRL').setReadOnly(false);
		                                	  Ext.getCmp('apellidoMaternoRL').setReadOnly(false);	 
		                                	  
		                                	  Ext.getCmp('tipoIdentificacion').allowBlank=false;
		                                	  Ext.getCmp('numeroIdentificacion').allowBlank=false;
		                                	  Ext.getCmp('nombreRL').allowBlank=false;
		                                	  Ext.getCmp('apellidoPaternoRL').allowBlank=false;	
		                                	  
		                                	  Ext.getCmp('tipoIdentificacion').setReadOnly(true);
		                                	  Ext.getCmp('numeroIdentificacion').setReadOnly(true);
		                                	  Ext.getCmp('nombreRL').setReadOnly(true);
		                                	  Ext.getCmp('apellidoPaternoRL').setReadOnly(true);
		                                	  Ext.getCmp('apellidoMaternoRL').setReadOnly(true);
		                                	  
		                                	  Ext.getCmp('tipoIdentificacion').allowBlank=true;
		                                	  Ext.getCmp('numeroIdentificacion').allowBlank=true;
		                                	  Ext.getCmp('nombreRL').allowBlank=true;
		                                	  Ext.getCmp('apellidoPaternoRL').allowBlank=true;
		                                	  
										}
										
										/*if(check == null){
											Ext.getCmp('checkEditLegalRepr').hide();
		          				        	Ext.getCmp('lblEditLegalRepr').hide();	
										}*/
								    },
							    	select: function (comboBox, records, eOpts) {
							    		
							    		var contrib = records[0].data.udcKey;
							    		/*if(contrib != '1'){
							    			Ext.getCmp('REPRESENTE_LEGAL').show();
							    			Ext.getCmp('tipoIdentificacion').show();
							    			Ext.getCmp('numeroIdentificacion').show();
							    			Ext.getCmp('nombreRL').show();
							    			Ext.getCmp('apellidoPaternoRL').show();
							    			Ext.getCmp('apellidoMaternoRL').show();
							    			//Ext.getCmp('checkEditLegalRepr').hide();
							    			//Ext.getCmp('lblEditLegalRepr').hide();
							    			
							    			if(role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER'){
							    				Ext.getCmp('apellidoPaternoRL').allowBlank=false;
							    				Ext.getCmp('nombreRL').allowBlank=false;
							    				Ext.getCmp('numeroIdentificacion').allowBlank=false;
							    				Ext.getCmp('tipoIdentificacion').allowBlank=false;
							    			}
							    			//Ext.getCmp('catCode27').setValue('85');
							    		}else{
							    			Ext.getCmp('REPRESENTE_LEGAL').hide();
							    			Ext.getCmp('tipoIdentificacion').allowBlank=true;
							    			Ext.getCmp('tipoIdentificacion').hide();
							    			Ext.getCmp('numeroIdentificacion').allowBlank=true;
							    			Ext.getCmp('numeroIdentificacion').hide();
							    			Ext.getCmp('nombreRL').allowBlank=true;
							    			Ext.getCmp('nombreRL').hide();
							    			Ext.getCmp('apellidoPaternoRL').allowBlank=true;
							    			Ext.getCmp('apellidoPaternoRL').hide();
							    			Ext.getCmp('apellidoMaternoRL').hide();
							    			//Ext.getCmp('checkEditLegalRepr').show();	
							    			//Ext.getCmp('lblEditLegalRepr').show();	
							    			//Ext.getCmp('catCode27').setValue('03');
							    		}*/
							    		if(contrib == '1'){
							    			Ext.getCmp('REPRESENTE_LEGAL').hide();
											Ext.getCmp('tipoIdentificacion').allowBlank=true;
											Ext.getCmp('tipoIdentificacion').hide();
											Ext.getCmp('numeroIdentificacion').allowBlank=true;
											Ext.getCmp('numeroIdentificacion').hide();
											Ext.getCmp('nombreRL').allowBlank=true;
											Ext.getCmp('nombreRL').hide();
											Ext.getCmp('apellidoPaternoRL').allowBlank=true;
											Ext.getCmp('apellidoPaternoRL').hide();
							    			Ext.getCmp('apellidoMaternoRL').hide();
							    			Ext.getCmp('tipoIdentificacion').setReadOnly(true);
							    			Ext.getCmp('numeroIdentificacion').setReadOnly(true);
							    			Ext.getCmp('nombreRL').setReadOnly(true);
							    			Ext.getCmp('apellidoPaternoRL').setReadOnly(true);
							    			Ext.getCmp('apellidoMaternoRL').setReadOnly(true);
							    			//Ext.getCmp('checkEditLegalRepr').hide();
							    			//Ext.getCmp('lblEditLegalRepr').hide();		
										}else{
											Ext.getCmp('REPRESENTE_LEGAL').show();
											Ext.getCmp('tipoIdentificacion').allowBlank=false;
											Ext.getCmp('tipoIdentificacion').show();
											Ext.getCmp('numeroIdentificacion').allowBlank=false;
											Ext.getCmp('numeroIdentificacion').show();
											Ext.getCmp('nombreRL').allowBlank=false;
											Ext.getCmp('nombreRL').show();
											Ext.getCmp('apellidoPaternoRL').allowBlank=false;
											Ext.getCmp('apellidoPaternoRL').show();
							    			Ext.getCmp('apellidoMaternoRL').show();
							    			Ext.getCmp('tipoIdentificacion').setReadOnly(false);
							    			Ext.getCmp('numeroIdentificacion').setReadOnly(false);
							    			Ext.getCmp('nombreRL').setReadOnly(false);
							    			Ext.getCmp('apellidoPaternoRL').setReadOnly(false);
							    			Ext.getCmp('apellidoMaternoRL').setReadOnly(false);
							    			
							    			//Ext.getCmp('checkEditLegalRepr').show();	
							    			//Ext.getCmp('lblEditLegalRepr').show();							    			
										}
							    	}
							    }
							},{
								xtype : 'displayfield',
								value : SuppAppMsg.supplierForm9,
								height:20,
								margin: '50 0 0 10',
								colspan:3,
								fieldStyle: 'font-weight:bold',
								fieldCls: 'no-border-displayfield'
						   },/*{//Contenedor de Dirección Fiscal
								xtype:'container',
								id:'contCheckEditFiscalAddress',
								layout:'hbox',
								colspan:3,
								margin: '50 0 0 0',
							    width:'100%',
							    items:[{
									xtype : 'displayfield',
									value : SuppAppMsg.supplierForm9,
									height:20,
									//margin: '50 0 0 10',
									colspan:3,
									fieldStyle: 'font-weight:bold'
									},{
									xtype : 'checkbox',
									name : 'checkEditFiscalAddress',
									id : 'checkEditFiscalAddress',
									margin: '0 0 0 90',
									width : 20,
									checked: false,
									//hidden:false,
									hidden: isMainSupplierUser ? false : true, //Solo el proveedor puede modificar su información
									listeners: {
			                              change: function (checkbox, newVal, oldVal) {
			                            	  var check = Ext.getCmp('country').getValue();
			                                  if (newVal == true) {  
			                                   Ext.getCmp('loadRfcDoc').setDisabled(false);
			                                   Ext.getCmp('rfcDocument').allowBlank=false;
					                           //Ext.getCmp('loadObligacionesFiscales').setDisabled(false);
					                           //Ext.getCmp('textObligacionesFiscales').allowBlank=false;
					                           Ext.getCmp('loadDomDoc').setDisabled(false);	
					                           Ext.getCmp('domDocument').allowBlank=false;
			                                	  	if(check == 'MX'){
			                                	Ext.getCmp('calleNumero').setReadOnly(false);
					                      		Ext.getCmp('fldMunicipio').setReadOnly(false);
					                      		Ext.getCmp('codigoPostal').setReadOnly(false);
					                      		Ext.getCmp('fldColonia').setReadOnly(false);
					                      		Ext.getCmp('estado').setReadOnly(false);
					                      		Ext.getCmp('phoneDF').setReadOnly(false);
					                      		Ext.getCmp('faxDF').setReadOnly(false);
					                      		Ext.getCmp('searchCP').show();
					                      		
					                      		Ext.getCmp('calleNumero').allowBlank=false;
					                      		Ext.getCmp('fldMunicipio').allowBlank=false;
					                      		Ext.getCmp('codigoPostal').allowBlank=false;
					                      		Ext.getCmp('fldColonia').allowBlank=false;
					                      		Ext.getCmp('estado').allowBlank=false;
					                      		Ext.getCmp('phoneDF').allowBlank=false;
					                    
					                      		
			                                	  	}else{
			                                	Ext.getCmp('calleNumero').setReadOnly(false);
					                      		Ext.getCmp('codigoPostal').setReadOnly(false);
					                      		Ext.getCmp('coloniaEXT').setReadOnly(false);
					                      		Ext.getCmp('estado').setReadOnly(false);
					                      		Ext.getCmp('phoneDF').setReadOnly(false);
					                      		Ext.getCmp('faxDF').setReadOnly(false);		
					                      		
					                      		Ext.getCmp('calleNumero').allowBlank=false;
					                      		Ext.getCmp('codigoPostal').allowBlank=false;
					                      		Ext.getCmp('coloniaEXT').allowBlank=false;
					                      		Ext.getCmp('estado').allowBlank=false;
					                      		Ext.getCmp('phoneDF').allowBlank=false;
					                      		
			                                	  	}
			                                  }else{
			                                	  
			                                	  if(!Ext.getCmp('checkEditDataSupp').checked){
			                                	Ext.getCmp('loadRfcDoc').setDisabled(true);
			                                	Ext.getCmp('rfcDocument').allowBlank=true;
						                      //  Ext.getCmp('loadObligacionesFiscales').setDisabled(true);
						                       // Ext.getCmp('textObligacionesFiscales').allowBlank=true;
						                        Ext.getCmp('loadDomDoc').setDisabled(true);	
						                        Ext.getCmp('domDocument').allowBlank=true;
			                                	  }
			                                	  if(check == 'MX'){
			                                	Ext.getCmp('calleNumero').setReadOnly(true);
							                    Ext.getCmp('fldMunicipio').setReadOnly(true);
							                    Ext.getCmp('codigoPostal').setReadOnly(true);
							                    Ext.getCmp('fldColonia').setReadOnly(true);
							                    Ext.getCmp('estado').setReadOnly(true);
							                    Ext.getCmp('phoneDF').setReadOnly(true);
							                    Ext.getCmp('faxDF').setReadOnly(true);
							                    Ext.getCmp('searchCP').hide();
							                    
							                    Ext.getCmp('calleNumero').allowBlank=true;
							                    Ext.getCmp('fldMunicipio').allowBlank=true;
							                    Ext.getCmp('codigoPostal').allowBlank=true;
							                    Ext.getCmp('fldColonia').allowBlank=true;
							                    Ext.getCmp('estado').allowBlank=true;
							                    Ext.getCmp('phoneDF').allowBlank=true;
							                    //Ext.getCmp('faxDF').allowBlank=true;
							                    
			                                	  }else{
			                                    Ext.getCmp('calleNumero').setReadOnly(true);
							                    Ext.getCmp('codigoPostal').setReadOnly(true);
							                    Ext.getCmp('coloniaEXT').setReadOnly(true);
							                    Ext.getCmp('estado').setReadOnly(true);
							                    Ext.getCmp('phoneDF').setReadOnly(true);
							                    Ext.getCmp('faxDF').setReadOnly(true);	
							                    
							                    Ext.getCmp('calleNumero').allowBlank=true;
							                    Ext.getCmp('codigoPostal').allowBlank=true;
							                    Ext.getCmp('coloniaEXT').allowBlank=true;
							                    Ext.getCmp('estado').allowBlank=true;
							                    Ext.getCmp('phoneDF').allowBlank=true;
							                    //Ext.getCmp('faxDF').allowBlank=true;
			                                	  }
			                                  }
			                              }
			                          }
									},{
				                        xtype: 'displayfield',
				                        width:'100%',
				                        value: SuppAppMsg.supplierForm71,
				                        id: 'lblEditFiscalAddress',
				                       // height:20,
										margin: '0 0 0 0',
										colspan:3,
										hidden: isMainSupplierUser ? false : true //Solo el proveedor puede modificar su información
				                    }]
								},*/{
								fieldLabel : SuppAppMsg.supplierForm10,
								name : 'calleNumero',
								id : 'calleNumero',
								//width:550,
								flex:1,
								colspan:3,
								maxLength : 40,
								////enforceMaxLength : true,
								maxLengthText : SuppAppMsg.supplierForm68 + '{0}<br>',
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						   },{
									fieldLabel :SuppAppMsg.supplierForm11,
									name : 'delegacionMnicipio',
									id:'fldMunicipio',
									//width:300,
									flex:1,
									maxLength : 40,
									////enforceMaxLength : true,
									maxLengthText : SuppAppMsg.supplierForm68 + '{0}<br>',
									allowBlank:false,
									   listeners:{
											change: function(field, newValue, oldValue){
												field.setValue(newValue.toUpperCase());
											}
										},
									//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						   },{
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
										fieldLabel : SuppAppMsg.supplierForm12,
										name : 'codigoPostal',
										id : 'codigoPostal',
										labelAlign:'right',
										margin:'0 10 0 0',
										//width:200,
										flex:1,
										colspan:2,
										allowBlank:false,
										   listeners:{
												change: function(field, newValue, oldValue){
													field.setValue(newValue.toUpperCase());
												}
											},
										//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
									    },
				            			{
			            				  xtype: 'button',	
			            				  id : 'searchCP',
			            				  text: SuppAppMsg.supplierForm13,	
			            				  cls: 'buttonStyle',
			            				  listeners: {		
			            				      click: function() {
			            				    	  var box = Ext.MessageBox.wait( SuppAppMsg.supplierProcessRequest , SuppAppMsg.approvalExecution);
			            				    	  Ext.Ajax.cors = true;
				            						Ext.Ajax.useDefaultXhrHeader = false;
				            						var value = Ext.getCmp('codigoPostal').getValue();
				            						if(value){
				            							if(value != ''){
				            								var token = '1d9ba79b-14b0-4882-b4d1-cb27142e73fe';
				            								var url = 'https://smartechcloud-apps.com/coreapi/api/query_cp/' + value;
						            						Ext.Ajax.request({
						            							url: url,
						            							method: 'GET',
						            							cors: true,
						            							headers: {
						            							    'Access-Control-Allow-Origin': 'https://smartechcloud-apps.com/'
						            							  },
						            							success: function(response){
							            							var text = response.responseText;
							            							var jsonData = Ext.JSON.decode(response.responseText);
							            							colStore.removeAll();
							            							if(jsonData){
							            								if(jsonData.length > 0){
							            									for(var i = 0;i<jsonData.length;i++){
							            										resp = jsonData[i];
							            										var asendamiento = resp.asentamiento
							            										var coloniaUpper = 	asendamiento.toUpperCase();
							            										
							            										if(coloniaUpper.length>40){
							            											colStore.add({
											            						        id_: coloniaUpper.substring(0,40),
											            						        name: coloniaUpper.substring(0,40)
											            						    });
							            										}else{
							            											colStore.add({
											            						        id_: coloniaUpper,
											            						        name: coloniaUpper
											            						    });
							            										}
							            										
							            										//Ext.getCmp('fldEstado').setValue(resp.estado);
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
				            						}else{
			            								Ext.Msg.show({
			            			   					     title:SuppAppMsg.supplierForm14,
			            			   					     msg: SuppAppMsg.supplierForm15,
			            			   					     buttons: Ext.Msg.YES,
			            			   					     icon: Ext.Msg.WARNING
			            								});
			            							}
			            				      }		
			            				  }
				            			}
								]
							},{
								fieldLabel :SuppAppMsg.supplierForm16,
								name : 'coloniaEXT', 
								id:'coloniaEXT',
								//width:300,
								flex:1,
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								maxLength : 40,
								////enforceMaxLength : true,
								maxLengthText : SuppAppMsg.supplierForm68 + '{0}<br>',
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
											Ext.getCmp('fldColonia').setValue(newValue.toUpperCase());
										}
									},
							},{
								fieldLabel :SuppAppMsg.supplierForm16,
								xtype: 'combobox',
								store:colStore,
								queryMode: 'local',
								displayField: 'name',
								valueField: 'id_',
								triggerAction: 'all',
					            forceSelection: false,
					            editable: true,
					            typeAhead: true,
								name : 'colonia',
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								id:'fldColonia', 
								editable: false,
								//width:400,
								flex:1,
								allowBlank:false,
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							    },{
									fieldLabel : SuppAppMsg.supplierForm17,
									name : 'estado',
									id : 'estado',
									xtype: 'combobox',
									typeAhead: true,
									editable: false,
					                typeAheadDelay: 100,
					                allowBlank:false,
					                minChars: 1,
					                queryMode: 'local',
					                //forceSelection: true,
									store : getAutoLoadUDCStore('STATE', '', '', ''),
					                displayField: 'strValue1',
					                valueField: 'udcKey',
					                //width : 250,
					                flex:1,
									colspan:3,
									lastQuery: ''
							},{
								fieldLabel : SuppAppMsg.supplierForm18,
								name : 'telefonoDF',
								id:'phoneDF',
								margin:'10 0 0 10',
								//width:250,
								flex:1,
								maskRe: /[0-9]/,
								stripCharsRe: /[^0-9]/,
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : 'Fax',
								name : 'faxDF',
								id:'faxDF',
								margin:'10 0 0 10',
								//width:250,
								flex:1,
								colspan:3,
								hidden:true,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
                            },{
                                fieldLabel : '',
                                name : 'auxComp',
                                id : 'auxComp',
                                //width:450,
                                flex:1,
                                colspan:3,
                                fieldStyle: 'display:none;'
                           }/*,{
								xtype : 'displayfield',
								value : SuppAppMsg.supplierForm19,
								height:20,
								margin: '50 0 0 10',
								colspan:3,
								fieldStyle: 'font-weight:bold'
						   }*/ /*,{
								fieldLabel : SuppAppMsg.supplierForm20,
								name : 'emailComprador',
								width:550,
								colspan:3,
								//tip: SuppAppMsg.supplierForm50,
								regex: /\w@\w/,
							    regexText: SuppAppMsg.supplierForm50,
							    validator: function(v) {
							        return /\w@\w/.test(v)?true:"";
							    },
								allowBlank:false,
					            listeners:{render: function(c) {
							        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      },
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toLowerCase());
									}
								}
						   }*/,{//Contenedor de Contacto Compras
								xtype:'container',
								id:'contCheckEditContact',
								layout:'hbox',
								colspan:3,
								margin: '50 0 0 0',
							    width:'100%',
							    items:[{
									xtype : 'displayfield',
									value : SuppAppMsg.supplierForm19,
									height:20,
									//margin: '50 0 0 10',
									colspan:3,
									fieldStyle: 'font-weight:bold',
									fieldCls: 'no-border-displayfield'
									},{
									xtype : 'checkbox',
									name : 'checkEditContact',
									id : 'checkEditContact',
									margin: '0 0 0 90',
									//width : 20,
									flex:1,
									checked: false,
									//hidden:false,
									hidden: isMainSupplierUser ? false : true, //Solo el proveedor puede modificar su información
									listeners: {
			                              change: function (checkbox, newVal, oldVal) {
			                            	  var check = Ext.getCmp('country').getValue();
			                                  if (newVal == true) {
			                                		Ext.getCmp('nombreContactoCxC').setReadOnly(false);
			                                		Ext.getCmp('cargoCxC').setReadOnly(false);
			                      					Ext.getCmp('emailSupplier').setReadOnly(false);
			                      					Ext.getCmp('nombreCxP01').setReadOnly(false);
				                      				Ext.getCmp('emailCxP01').setReadOnly(false);
				                      				Ext.getCmp('telefonoCxP01').setReadOnly(false);
				                      				Ext.getCmp('nombreCxP02').setReadOnly(false);
				                      				Ext.getCmp('emailCxP02').setReadOnly(false);
				                      				Ext.getCmp('telefonoCxP02').setReadOnly(false);
				                      				Ext.getCmp('nombreCxP03').setReadOnly(false);
				                      				Ext.getCmp('emailCxP03').setReadOnly(false);
				                      				Ext.getCmp('telefonoCxP03').setReadOnly(false);
				                      				Ext.getCmp('nombreCxP04').setReadOnly(false);
				                      				Ext.getCmp('emailCxP04').setReadOnly(false);
				                      				Ext.getCmp('telefonoCxP04').setReadOnly(false);
				                      				
				                      				Ext.getCmp('nombreContactoCxC').allowBlank=false;
			                                		Ext.getCmp('emailSupplier').allowBlank=false;
			                                		Ext.getCmp('nombreCxP01').allowBlank=false;
				                      				Ext.getCmp('emailCxP01').allowBlank=false;
				                      				Ext.getCmp('telefonoCxP01').allowBlank=false;
				                      				Ext.getCmp('nombreCxP02').allowBlank=false;
				                      				Ext.getCmp('emailCxP02').allowBlank=false;
				                      				Ext.getCmp('telefonoCxP02').allowBlank=false;
			                                  }else{
			                                		Ext.getCmp('nombreContactoCxC').setReadOnly(true);
			                                		Ext.getCmp('cargoCxC').setReadOnly(true);
			                      					Ext.getCmp('emailSupplier').setReadOnly(true);
			                      					Ext.getCmp('nombreCxP01').setReadOnly(true);
				                      				Ext.getCmp('emailCxP01').setReadOnly(true);
				                      				Ext.getCmp('telefonoCxP01').setReadOnly(true);
				                      				Ext.getCmp('nombreCxP02').setReadOnly(true);
				                      				Ext.getCmp('emailCxP02').setReadOnly(true);
				                      				Ext.getCmp('telefonoCxP02').setReadOnly(true);
				                      				Ext.getCmp('nombreCxP03').setReadOnly(true);
				                      				Ext.getCmp('emailCxP03').setReadOnly(true);
				                      				Ext.getCmp('telefonoCxP03').setReadOnly(true);
				                      				Ext.getCmp('nombreCxP04').setReadOnly(true);
				                      				Ext.getCmp('emailCxP04').setReadOnly(true);
				                      				Ext.getCmp('telefonoCxP04').setReadOnly(true);
				                      				
				                      				Ext.getCmp('nombreContactoCxC').allowBlank=true;
			                                		Ext.getCmp('emailSupplier').allowBlank=true;
			                                		Ext.getCmp('nombreCxP01').allowBlank=true;
				                      				Ext.getCmp('emailCxP01').allowBlank=true;
				                      				Ext.getCmp('telefonoCxP01').allowBlank=true;
				                      				Ext.getCmp('nombreCxP02').allowBlank=true;
				                      				Ext.getCmp('emailCxP02').allowBlank=true;
				                      				Ext.getCmp('telefonoCxP02').allowBlank=true;
			                                  }
			                              }
			                          }
									},{
				                        xtype: 'displayfield',
				                        width:'100%',
				                        value: SuppAppMsg.supplierForm71,
				                        id: 'lblEditContact',
				                       // height:20,
										margin: '0 0 0 0',
										colspan:3,
										hidden: isMainSupplierUser ? false : true //Solo el proveedor puede modificar su información
				                    }]
								},{
								fieldLabel : SuppAppMsg.supplierForm21,
								name : 'nombreContactoCxC',
								id : 'nombreContactoCxC', 
								xtype: 'combobox',
								typeAhead: true,
				                typeAheadDelay: 100,
				                allowBlank:false,
				                minChars: 1,
				                queryMode: 'local',
				                readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
				                //forceSelection: true,
								store : getAutoLoadUDCStore('USERPURCHASE', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'strValue1',
				                //width : 500,
				                flex:1,
				                editable: false,
								colspan:3,
								listeners: {
							    	select: function (comboBox, records, eOpts) {
							    		var userPurchase = records[0].data;
							    		Ext.getCmp('emailComprador').setValue(userPurchase.strValue2);
							    		Ext.getCmp('telefonoCxC').setValue(userPurchase.keyRef);
							    	}
							    }
							}/*,{
								fieldLabel : SuppAppMsg.supplierForm21,
								name : 'nombreContactoCxC',
								id:'nombresCxC',
								margin:'10 0 0 10',
								width:400,
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm22,
								name : 'apellidoPaternoCxC',
								id:'apellidoPaternoCxC',
								margin:'10 0 0 10',
								width:300,
								colspan:2,
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm23,
								name : 'apellidoMaternoCxC',
								id:'apellidoMaternoCxC',
								margin:'10 0 0 10',
								width:300,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							}*/,{
								fieldLabel : SuppAppMsg.supplierForm20,
								name : 'emailComprador',
								id : 'emailComprador',
								//width:550,
								flex:1,
								colspan:3,
								readOnly:true,
								//tip: SuppAppMsg.supplierForm50,
								vtype: 'email',
								vtypeText : SuppAppMsg.supplierForm69,
								allowBlank:false,
					            listeners:{render: function(c) {
							        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      },
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toLowerCase());
									}
								}
						   },{
								fieldLabel : SuppAppMsg.supplierForm18,
								name : 'telefonoContactoCxC',
								id:'telefonoCxC',
								margin:'10 0 0 10',
								readOnly:true,
								maskRe: /[0-9]/,
								stripCharsRe: /[^0-9]/,
								//width:300,
								flex:1,
								//colspan:1,
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							}/*,{
								fieldLabel : 'Fax',
								name : 'faxCxC',
								id:'FaxCxC',
								margin:'10 0 0 10',
								width:300,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							}*/,{
								fieldLabel : SuppAppMsg.supplierForm24,
								name : 'cargoCxC',
								id:'cargoCxC',
								margin:'10 0 0 10',
								//width:300,
								flex:1,
								colspan:3,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								xtype : 'displayfield',
								value : SuppAppMsg.supplierForm72,
								height:20,
								margin: '50 0 0 10',
								colspan:3,
								width:'100%',
								fieldStyle: 'font-weight:bold',
								fieldCls: 'no-border-displayfield'
								},{
									fieldLabel : SuppAppMsg.supplierForm7,
									name : 'emailSupplier',
									id : 'emailSupplier',
									//width:450,
									flex:1,
									margin:'10 0 0 10',
									allowBlank:false,blankText: SuppAppMsg.supplierForm74,
									blankText: SuppAppMsg.supplierForm74,
									maxLength : 254,
									colspan:3,
									//enforceMaxLength : true,
									//vtype: 'email',
									//vtypeText : SuppAppMsg.supplierForm73,
						            listeners:{
						            	render: function(c) {
									        Ext.create('Ext.tip.ToolTip', {
									          target: c.getEl(),
									          html: c.tip 
									        });
									      },
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toLowerCase());
										}
									}
							   },{
							xtype : 'displayfield',
							value : SuppAppMsg.supplierForm25,
							height:20,
							//margin: '50 0 0 10',
							margin: '50 0 0 10',
							colspan:3,
							width:'100%',
							fieldStyle: 'font-weight:bold',
							fieldCls: 'no-border-displayfield'
							},{
								fieldLabel : SuppAppMsg.supplierForm26 + ' 01*',
								name : 'nombreCxP01',
								id:'nombreCxP01',
								margin:'10 0 0 10',
								//width:400,
								flex:1,
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.usersEmail + '*',
								name : 'emailCxP01',
								id:'emailCxP01',
								//tip: SuppAppMsg.supplierForm50,
								vtype: 'email',
								vtypeText : SuppAppMsg.supplierForm69,
								margin:'10 0 0 10',
								//width:300,
								flex:1,
								allowBlank:false,
								listeners:{
					            	render: function(c) {
								        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      },
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toLowerCase());
									}
								}/*,
								   listeners:{
								change: function(field, newValue, oldValue){
									field.setValue(newValue.toUpperCase());
								}
							},*/
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm18,
								name : 'telefonoCxP01',
								id:'telefonoCxP01',
								margin:'10 0 0 10',
								//width:300,
								flex:1,
								maskRe: /[0-9]/,
								stripCharsRe: /[^0-9]/,
								colspan:2,
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm26 + ' 02*',
								name : 'nombreCxP02',
								id:'nombreCxP02',
								margin:'10 0 0 10',
								//width:400,
								flex:1,
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.usersEmail + '*',
								name : 'emailCxP02',
								id:'emailCxP02',
								margin:'10 0 0 10',
								//tip: SuppAppMsg.supplierForm50,
								vtype: 'email',
								vtypeText : SuppAppMsg.supplierForm69,
								//width:300,
								flex:1,
								allowBlank:false,
								listeners:{
					            	render: function(c) {
								        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      },
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toLowerCase());
									}
								}/*,
								   listeners:{
								change: function(field, newValue, oldValue){
									field.setValue(newValue.toUpperCase());
								}
							},*/
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm18,
								name : 'telefonoCxP02',
								id:'telefonoCxP02',
								maskRe: /[0-9]/,
								stripCharsRe: /[^0-9]/,
								margin:'10 0 0 10',
								//width:300,
								flex:1,
								colspan:2,
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm26 + ' 03',
								name : 'nombreCxP03',
								id:'nombreCxP03',
								margin:'10 0 0 10',
								//width:400,
								flex:1,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.usersEmail ,
								name : 'emailCxP03',
								id:'emailCxP03',
								//tip: SuppAppMsg.supplierForm50,
								vtype: 'email',
								vtypeText : SuppAppMsg.supplierForm69,
								margin:'10 0 0 10',
								//width:300,
								flex:1,
								allowBlank:true,
								listeners:{
					            	render: function(c) {
								        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      },
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toLowerCase());
									}
								}/*,
								   listeners:{
								change: function(field, newValue, oldValue){
									field.setValue(newValue.toUpperCase());
								}
							},*/
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm48,
								name : 'telefonoCxP03',
								id:'telefonoCxP03',
								margin:'10 0 0 10',
								//width:300,
								flex:1,
								maskRe: /[0-9]/,
								stripCharsRe: /[^0-9]/,
								colspan:2,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm26 + ' 04',
								name : 'nombreCxP04',
								id:'nombreCxP04',
								margin:'10 0 0 10',
								//width:400,
								flex:1,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.usersEmail ,
								name : 'emailCxP04',
								id:'emailCxP04',
								margin:'10 0 0 10',
								//tip: SuppAppMsg.supplierForm50,
								vtype: 'email',
								vtypeText : SuppAppMsg.supplierForm69,
								//width:300,
								flex:1,
								allowBlank:true,
								listeners:{
					            	render: function(c) {
								        Ext.create('Ext.tip.ToolTip', {
								          target: c.getEl(),
								          html: c.tip 
								        });
								      },
									change: function(field, newValue, oldValue){
										field.setValue(newValue.toLowerCase());
									}
								}/*,
								   listeners:{
								change: function(field, newValue, oldValue){
									field.setValue(newValue.toUpperCase());
								}
							},*/
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm48,
								name : 'telefonoCxP04',
								id:'telefonoCxP04',
								margin:'10 0 0 10',
								//width:300,
								flex:1,
								colspan:2,
								maskRe: /[0-9]/,
								stripCharsRe: /[^0-9]/,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								xtype : 'displayfield',
								value : SuppAppMsg.supplierForm27,
								height:20,
								id:'textCatCode',
								margin: '50 0 0 10',
								hidden:true,
								colspan:3,
								flex:1,
								fieldStyle: 'font-weight:bold'
							},{
								fieldLabel : SuppAppMsg.supplierForm63,
								name : 'catCode15',
								xtype: 'combobox',
								typeAhead: true,
								id:'explFiscal',
				                typeAheadDelay: 100,
				                readOnly:true,
				                editable: false,
				                allowBlank:true, 
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('EXPLFISCALCODE', '', '', ''),
				                displayField: 'strValue1', 
				                valueField: 'udcKey',
				                hidden:true,
				                //width : 400,
				                flex:1,
								colspan:3
							}/*,{
								fieldLabel : 'Cat Code 27',
								name : 'catCode27',
								id : 'catCode27',
								xtype: 'combobox',
								typeAhead: true,
				                typeAheadDelay: 100,
				                allowBlank:true,
				                editable: false,
				                minChars: 1,
				                hidden:true,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('CATEGORYCODE27', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                width : 400,
								colspan:3
							}*/,{
								fieldLabel : 'Cat Code 01',
								name : 'catCode01',
								id : 'catCode01',
								xtype: 'combobox',
								typeAhead: true,
				                typeAheadDelay: 100,
				                hidden:true,
				                editable: false,
				                allowBlank:true,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('CATEGORYCODE01', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 400,
				                flex:1,
								colspan:3
							},{
								fieldLabel : 'Cat Code 20',
								name : 'catCode20',
								id : 'catCode20',
								xtype: 'combobox',
								typeAhead: true,
				                typeAheadDelay: 100,
				                allowBlank:true,
				                editable: false,
				                hidden:true,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('CATEGORYCODE20', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 400,
				                flex:1,
								colspan:3
							},{
								fieldLabel : 'Cat Code 23',
								name : 'catCode23',
								id : 'catCode23',
								xtype: 'combobox',
								typeAhead: true,
				                typeAheadDelay: 100,
				                editable: false,
				                allowBlank:true,
				                minChars: 1,
				                queryMode: 'local',
				                hidden:true,
				                //forceSelection: true,
								store : getAutoLoadUDCStore('CATEGORYCODE23', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 400,
				                flex:1,
								colspan:3
							},{
								fieldLabel : 'Cat Code 24',
								name : 'catCode24',
								id : 'catCode24',
								xtype: 'combobox',
								typeAhead: true,
				                typeAheadDelay: 100,
				                allowBlank:true,
				                hidden:true,
				                editable: false,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('CATEGORYCODE24', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 400,
				                flex:1,
								colspan:3
							},{
								fieldLabel : SuppAppMsg.supplierForm28,
								name : 'industryClass',
								id:'industryClass',
								xtype: 'combobox',
								typeAhead: true,
								hidden:true,
				                typeAheadDelay: 100,
				                allowBlank:true,
				                editable: false,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('INDUSTRYCLASS', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 400,
				                flex:1,
								colspan:3
							}/*,{
								fieldLabel : 'G/L Class',
								name : 'glClass',
								id:'glClass',
								margin:'10 0 0 10',
								width:300,
								colspan:3,
								allowBlank:true,
								hidden: true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							}*/,{
								fieldLabel : SuppAppMsg.supplierForm64,
								name : 'glClass',
								id:'glClass',
								xtype: 'combobox',
								typeAhead: true,
								hidden:true,
				                typeAheadDelay: 100,
				                allowBlank:true,
				                editable: false,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('GLCLASS', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 400,
				                flex:1,
								colspan:3
							},{
								fieldLabel : SuppAppMsg.supplierForm65,
								name : 'paymentMethod',
								id:'paymentMethod',
								xtype: 'combobox',
								typeAhead: true,
								hidden:true,
				                typeAheadDelay: 100,
				                allowBlank:true,
				                editable: false,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('PAYMETH', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 400,
				                flex:1,
								colspan:3
							},{
								fieldLabel : SuppAppMsg.supplierForm66,
								name : 'requisitosFiscales',
								id:'requisitosFiscales',
								xtype: 'combobox',
								typeAhead: true,
								hidden:true,
				                typeAheadDelay: 100,
				                allowBlank:true,
				                editable: false,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('REQUISITOSFISCALES', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 400,
				                flex:1,
								colspan:3
							},{
								fieldLabel : SuppAppMsg.supplierForm67,
								name : 'pmtTrmCxC',
								xtype: 'combobox',
								id:'pmtTrmCxC',
								typeAhead: true,
				                typeAheadDelay: 100,
				                editable: false,
				                allowBlank:true,
				                minChars: 1,
				                queryMode: 'local',
				                hidden : true,
				                //forceSelection: true,
								store : getAutoLoadUDCStore('PMTTRM', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 400,
				                flex:1,
								colspan:3
							},{
								xtype : 'displayfield',
								value : SuppAppMsg.supplierForm29,
								height:20,
								margin: '50 0 0 10',
								id: 'REPRESENTE_LEGAL',
								colspan:3,
								fieldStyle: 'font-weight:bold',
								fieldCls: 'no-border-displayfield'
								},/*{//Contenedor de Representante Legal
									xtype:'container',
									id:'contCheckEditLegalRepr',
									layout:'hbox',
									colspan:3,
									margin: '50 0 0 10',
								    width:'100%',
								    items:[{
										xtype : 'displayfield',
										value : SuppAppMsg.supplierForm29,
										height:20,
										id: 'REPRESENTE_LEGAL',
										//margin: '50 0 0 10',
										colspan:3,
										fieldStyle: 'font-weight:bold'
										},{
										xtype : 'checkbox',
										name : 'checkEditLegalRepr',
										id : 'checkEditLegalRepr',
										margin: '0 0 0 90',
										width : 20,
										checked: false,
										//hidden:false,
										hidden: isMainSupplierUser ? false : true, //Solo el proveedor puede modificar su información
										listeners: {
				                              change: function (checkbox, newVal, oldVal) {
				                            	  var check = Ext.getCmp('country').getValue();
				                                  if (newVal == true) {
				                                	  Ext.getCmp('loadIdentDoc').setDisabled(false);
				                                	  Ext.getCmp('identDocument').allowBlank=false;
					                                  Ext.getCmp('loadRpcDocument').setDisabled(false);
					                                  Ext.getCmp('rpcDocument').allowBlank=false;					                                  
				                                	  Ext.getCmp('tipoIdentificacion').setReadOnly(false);
				                                	  Ext.getCmp('numeroIdentificacion').setReadOnly(false);
				                                	  Ext.getCmp('nombreRL').setReadOnly(false);
				                                	  Ext.getCmp('apellidoPaternoRL').setReadOnly(false);
				                                	  Ext.getCmp('apellidoMaternoRL').setReadOnly(false);	 
				                                	  
				                                	  Ext.getCmp('tipoIdentificacion').allowBlank=false;
				                                	  Ext.getCmp('numeroIdentificacion').allowBlank=false;
				                                	  Ext.getCmp('nombreRL').allowBlank=false;
				                                	  Ext.getCmp('apellidoPaternoRL').allowBlank=false;				                                	  
				                                	  
				                                  }else{
				                                	  Ext.getCmp('loadIdentDoc').setDisabled(true);
				                                	  Ext.getCmp('identDocument').allowBlank=true;
					                                  Ext.getCmp('loadRpcDocument').setDisabled(true);
					                                  Ext.getCmp('rpcDocument').allowBlank=true;
				                                	  Ext.getCmp('tipoIdentificacion').setReadOnly(true);
				                                	  Ext.getCmp('numeroIdentificacion').setReadOnly(true);
				                                	  Ext.getCmp('nombreRL').setReadOnly(true);
				                                	  Ext.getCmp('apellidoPaternoRL').setReadOnly(true);
				                                	  Ext.getCmp('apellidoMaternoRL').setReadOnly(true);
				                                	  
				                                	  Ext.getCmp('tipoIdentificacion').allowBlank=true;
				                                	  Ext.getCmp('numeroIdentificacion').allowBlank=true;
				                                	  Ext.getCmp('nombreRL').allowBlank=true;
				                                	  Ext.getCmp('apellidoPaternoRL').allowBlank=true;
				                                  }
				                              }
				                          }
										},{
					                        xtype: 'displayfield',
					                        width:'100%',
					                        value: SuppAppMsg.supplierForm71,
					                        id: 'lblEditLegalRepr',
											margin: '0 0 0 0',
											colspan:3,
											hidden: isMainSupplierUser ? false : true //Solo el proveedor puede modificar su información
					                    }]
									},*/{
									fieldLabel : SuppAppMsg.supplierForm30,
									name : 'tipoIdentificacion',
									id : 'tipoIdentificacion',
									xtype: 'combobox',
									typeAhead: true,
					                typeAheadDelay: 100,
					                editable: false,
					                allowBlank:false,
					                minChars: 1,
					                queryMode: 'local',
					                //forceSelection: true,
									store : getAutoLoadUDCStore('IDENTIFICATIONTYPE', '', '', ''),
					                displayField: 'strValue1',
					                valueField: 'udcKey',
					                //width : 250,
					                flex:1,
									colspan:3
							},{
								fieldLabel : SuppAppMsg.supplierForm31,
								name : 'numeroIdentificacion',
								id : 'numeroIdentificacion',
								//width:550,
								flex:1,
								colspan:3,
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						   },{
								fieldLabel : SuppAppMsg.supplierForm21,
								name : 'nombreRL',
								id:'nombreRL',
								margin:'10 0 0 10',
								//width:400,
								flex:1,
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm22,
								name : 'apellidoPaternoRL',
								id:'apellidoPaternoRL',
								margin:'10 0 0 10',
								//width:300,
								flex:1,
								colspan:2,
								allowBlank:false,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm23,
								name : 'apellidoMaternoRL',
								id:'apellidoMaternoRL',
								margin:'10 0 0 10',
								//width:300,
								flex:1,
								colspan:3,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							/*},{
								xtype : 'displayfield',
								value : SuppAppMsg.supplierForm32,
								height:20,
								margin: '50 0 0 10',
								colspan:3,
								fieldStyle: 'font-weight:bold'*/
							},{//Contenedor de Datos Bancarios
								xtype:'container',
								id:'contCheckEditDataBank',
								layout:'hbox',
								colspan:3,
								margin: '50 0 0 0',
							    width:'100%',
							    items:[{
									xtype : 'displayfield',
									value : SuppAppMsg.supplierForm32,
									height:20,
									//margin: '50 0 0 10',
									colspan:3,
									fieldStyle: 'font-weight:bold',
									fieldCls: 'no-border-displayfield'
									},{
									xtype : 'checkbox',
									name : 'checkEditDataBank',
									id : 'checkEditDataBank',
									margin: '0 0 0 90',
									//width : 20,
									flex:1,
									checked: false,
									//hidden:false,
									hidden: isMainSupplierUser ? false : true, //Solo el proveedor puede modificar su información
									listeners: {
			                              change: function (checkbox, newVal, oldVal) {
			                            	  var check = Ext.getCmp('country').getValue();
			                                  if (newVal == true) {
			                                	  Ext.getCmp('currencyCode').setReadOnly(false);
			                                	  Ext.getCmp('loadEdoDoc').setDisabled(false);
			                                	  Ext.getCmp('edoDocument').allowBlank=false;
			                                	  //Nacional
			                                	  Ext.getCmp('bankTransitNumber').setReadOnly(false);
			                                	  Ext.getCmp('custBankAcct').setReadOnly(false);
			                                	  Ext.getCmp('controlDigit').setReadOnly(false);
			                                	  Ext.getCmp('description').setReadOnly(false);	  
			                                	  //Ext.getCmp('outSourcing').setReadOnly(false);
			                                	  Ext.getCmp('idFiscal').setReadOnly(false);
			                                	  
			                                	
			                                	  //Extranjero               	 
				                                	 Ext.getCmp('swiftCode').setReadOnly(false);
				                                	 Ext.getCmp('ibanCode').setReadOnly(false);
				                                	 //Ext.getCmp('checkingOrSavingAccount').setReadOnly(false);
				                                	 Ext.getCmp('rollNumber').setReadOnly(false);
				                                	 Ext.getCmp('bankAddressNumber').setReadOnly(false);
				                                	 Ext.getCmp('bankCountryCode').setReadOnly(false);		
				                                	 
				                                	// Ext.getCmp('bankTransitNumber').allowBlank=false;
				                                	 Ext.getCmp('custBankAcct').allowBlank=false;

			                                  }else{
			                                	  Ext.getCmp('idFiscal').setReadOnly(true);
			                                	  Ext.getCmp('currencyCode').setReadOnly(true);
			                                	  Ext.getCmp('loadEdoDoc').setDisabled(true);
			                                	  Ext.getCmp('edoDocument').allowBlank=true;
			                                	//Nacional
			                                	  Ext.getCmp('bankTransitNumber').setReadOnly(true);
			                                	  Ext.getCmp('custBankAcct').setReadOnly(true);
			                                	  Ext.getCmp('controlDigit').setReadOnly(true);
			                                	  Ext.getCmp('description').setReadOnly(true);
			                                	  //Ext.getCmp('outSourcing').setReadOnly(true);
			                                	  
			                                   //Extranjero               	 
				                                	 Ext.getCmp('swiftCode').setReadOnly(true);
				                                	 Ext.getCmp('ibanCode').setReadOnly(true);
				                                //	 Ext.getCmp('checkingOrSavingAccount').setReadOnly(true);
				                                	 Ext.getCmp('rollNumber').setReadOnly(true);
				                                	 Ext.getCmp('bankAddressNumber').setReadOnly(true);
				                                	 Ext.getCmp('bankCountryCode').setReadOnly(true);
				                                	 
				                                	// Ext.getCmp('bankTransitNumber').allowBlank=true;
				                                	 Ext.getCmp('custBankAcct').allowBlank=true;
			                                	 
			                                  }
			                                 			                                 
			                              }
			                          }
									},{
				                        xtype: 'displayfield',
				                        width:'100%',
				                        value: SuppAppMsg.supplierForm71,
				                        id: 'lblEditDataBank',
				                       // height:20,
										margin: '0 0 0 0',
										colspan:3,
										hidden: isMainSupplierUser ? false : true //Solo el proveedor puede modificar su información
				                    }]
								},{
								fieldLabel : SuppAppMsg.purchaseOrderCurrency,
								//name : 'currencyCode',
								id:'currencyValidation',
								typeAhead: true,
				                typeAheadDelay: 100,
				                minChars: 1, 
				                queryMode: 'local',
				                hidden:true,
				                //forceSelection: true,
								xtype: 'combobox',
								editable: false,
								store : getAutoLoadUDCStore('CURRENCYVALIDATION', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 350,
				                flex:1,
				                allowBlank:true,
				                colspan:3,
								listeners: {
							    	select: function (comboBox, records, eOpts) {
							    		//Ext.getCmp('checkingOrSavingAccount').setValue('0');
							    		var contrib = records[0].data.udcKey; 
							    		if(contrib == '1'){
							    			Ext.getCmp('custBankAcct').show();
							    			Ext.getCmp('controlDigit').show();
							    			Ext.getCmp('description').show();
							    			//Ext.getCmp('checkingOrSavingAccount').show();
							    			
							    			Ext.getCmp('bankTransitNumber').hide();
							    			Ext.getCmp('ibanCode').hide();
							    			Ext.getCmp('swiftCode').hide();
							    			Ext.getCmp('rollNumber').hide();
							    			Ext.getCmp('bankAddressNumber').hide();
							    			Ext.getCmp('bankCountryCode').hide();
							    			
							    			Ext.getCmp('controlDigit').setValue('MX');
							    			Ext.getCmp('custBankAcct').setFieldLabel(SuppAppMsg.supplierForm53);
							    		}else if(contrib == '2'){
							    			
							    			Ext.getCmp('controlDigit').show();
							    			Ext.getCmp('description').show();
							    			//Ext.getCmp('checkingOrSavingAccount').show();
							    			Ext.getCmp('bankTransitNumber').show();
							    			Ext.getCmp('ibanCode').show();
							    			Ext.getCmp('swiftCode').show();
							    			
							    			Ext.getCmp('rollNumber').hide();
							    			Ext.getCmp('bankAddressNumber').hide();
							    			Ext.getCmp('bankCountryCode').hide();
							    			Ext.getCmp('custBankAcct').hide();
							    			
							    			Ext.getCmp('controlDigit').setValue('US');
							    			Ext.getCmp('bankTransitNumber').setFieldLabel(SuppAppMsg.supplierForm54);
							    		}else if(contrib == '3'){
							    			
							    			Ext.getCmp('controlDigit').show();
							    			Ext.getCmp('description').show();
							    			//Ext.getCmp('checkingOrSavingAccount').show();
							    			Ext.getCmp('bankTransitNumber').show();
							    			Ext.getCmp('ibanCode').show();
							    			Ext.getCmp('swiftCode').show();
							    			Ext.getCmp('custBankAcct').show();
							    			
							    			Ext.getCmp('rollNumber').hide();
							    			Ext.getCmp('bankAddressNumber').hide();
							    			Ext.getCmp('bankCountryCode').hide();
							    			
							    			Ext.getCmp('controlDigit').setValue('MX');
							    			Ext.getCmp('custBankAcct').setFieldLabel(SuppAppMsg.supplierForm53);
							    			Ext.getCmp('bankTransitNumber').setFieldLabel(SuppAppMsg.supplierForm55);
							    			
							    		}
							    	
							    	}
							    }
				                //hidden:true
							},{
								fieldLabel : SuppAppMsg.purchaseOrderCurrency,
								name : 'currencyCode',
								id : 'currencyCode',
								xtype: 'combobox',
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								typeAhead: true,
				                typeAheadDelay: 100,
				                allowBlank:false,
				                editable: false,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('CURRENCY', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 450,
				                flex:1,
								colspan:3,
							    listeners: {
							    	select: function (comboBox, records, eOpts) {
							    		var country = Ext.getCmp('country').getValue();
							    		var currencyCode = records[0].data.udcKey;

							    		if(currencyCode == 'MXP' && country =='US'){
							    			Ext.getCmp('idFiscal').allowBlank=false;
							    			Ext.getCmp('idFiscal').setValue('');
							    		}else{
							    			Ext.getCmp('idFiscal').allowBlank=true;
							    			Ext.getCmp('idFiscal').setValue('');
							    		}
							    	}
							    }
							},{
								fieldLabel : SuppAppMsg.supplierForm62,
								name : 'idFiscal', 
								id : 'idFiscal', 
								//width:450,
								flex:1,
								maxLength : 20,
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								colspan:3,
								stripCharsRe: /[^A-Za-z0-9 ]/,
								maxLengthText : SuppAppMsg.supplierForm68 + '{0}<br>',
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						   },{
								fieldLabel : SuppAppMsg.supplierForm33,
								name : 'swiftCode', 
								id : 'swiftCode',  
								//width:450,
								flex:1,
								maxLength : 15,
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								colspan:3,
								stripCharsRe: /[^A-Za-z0-9]/,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						   },{
								fieldLabel : SuppAppMsg.supplierForm34,
								name : 'ibanCode',
								id:'ibanCode',
								margin:'10 0 0 10',
								//width:400,
								flex:1,
								maxLength : 34,
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
							},{
								fieldLabel : SuppAppMsg.supplierForm56,
								name : 'bankTransitNumber',
								id : 'bankTransitNumber',
								//width:450,
								flex:1,
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								colspan:3,
								maxLength : 18, 
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						   },{
							   fieldLabel : SuppAppMsg.supplierForm61,
								name : 'custBankAcct',
								id : 'custBankAcct',
								//width:450,
								flex:1,
								colspan:3,
								stripCharsRe: /[^0-9]/,
								maxLength : 18,
								allowBlank:true,
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						   },{
								fieldLabel : SuppAppMsg.supplierForm49,
								name : 'controlDigit',
								id : 'controlDigit',
								xtype: 'combobox',
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								typeAhead: true,
				                typeAheadDelay: 100,
				                allowBlank:true,
				                editable: false,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('CONTROLDIGIT', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 450,
				                flex:1,
								colspan:3
							},{
								fieldLabel : SuppAppMsg.supplierForm51,
								name : 'description',
								id : 'description',
								//width:450,
								flex:1,
								maskRe: /[A-Za-z &]/,
								stripCharsRe: /[^A-Za-z &]/,
								maxLength : 30,
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								colspan:3,
								maxLength : 30,
								maxLengthText : SuppAppMsg.supplierForm68 + '{0}<br>', 
								//////enforceMaxLength : true,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						   },{
								fieldLabel : SuppAppMsg.supplierForm57,
								name : 'checkingOrSavingAccount',
								id : 'checkingOrSavingAccount',
								xtype: 'combobox',
								typeAhead: true,
								//readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								readOnly:true,
								//hidden:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? true:false,
								hidden:true,
				                typeAheadDelay: 100,
				                allowBlank:true,
				                readOnly:true,
				                editable: false,
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
								store : getAutoLoadUDCStore('CHECKORSAVEACCOUNT', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width : 400,
				                flex:1,
								colspan:3
							},{
								fieldLabel : SuppAppMsg.supplierForm58,
								name : 'rollNumber',
								id : 'rollNumber',
								//width:450,
								flex:1,
								maxLength : 18,
								colspan:3,
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						   },{
								fieldLabel : SuppAppMsg.supplierForm59,
								name : 'bankAddressNumber',
								id : 'bankAddressNumber',
								//width:450,
								flex:1,
								readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
								colspan:3,
								maxLength : 8,
								////enforceMaxLength : true,
								maxLengthText : SuppAppMsg.supplierForm68 + '{0}<br>',
								//maskRe: /[0-9]/,
								stripCharsRe: /[^0-9]/,
								allowBlank:true,
								   listeners:{
										change: function(field, newValue, oldValue){
											field.setValue(newValue.toUpperCase());
										}
									},
								//fieldStyle:role=='ANONYMOUS'?'':'border:2px solid green;'
						   },{
								xtype: 'combobox',
				                name: 'bankCountryCode',
				                id: 'bankCountryCode',
				                fieldLabel : SuppAppMsg.supplierForm60,
				                typeAhead: true,
				                typeAheadDelay: 100,
				                readOnly:role == 'ANONYMOUS' ||role == 'ROLE_SUPPLIER' ? false:true,
				                allowBlank:true,
				                editable: false,
				                margin:'10 0 0 10',
				                minChars: 1,
				                queryMode: 'local',
				                //forceSelection: true,
				                store : getAutoLoadUDCStore('COUNTRY', '', '', ''),
				                displayField: 'strValue1',
				                valueField: 'udcKey',
				                //width:400,
				                flex:1,
				                colspan:3
							},{
								xtype : 'checkbox',
								fieldLabel : SuppAppMsg.outsourcingSupp,
								name : 'outSourcing',
								//width : 220,
								flex:1,
								labelWidth:220,
								colspan:3,
								checked: false,
								//readOnly:role=='ROLE_SUPPLIER'?true:false
								//readOnly: true,
								readOnly:role=='ANONYMOUS'?false:true
								//hidden: true
								//hidden: role == 'ANONYMOUS'  ? true : false
							},{
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
										value : SuppAppMsg.supplierForm35,
										margin: '30 0 0 0',
										colspan:3,
										id: 'documents',
										fieldStyle: 'font-weight:bold',
										fieldCls: 'no-border-displayfield'
										},{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerRfc',
									colspan:3,
									width:800,
									//flex:1,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 250,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : SuppAppMsg.supplierForm36,
										name : 'rfcDocument',
										id:'rfcDocument',
										wdth:100,
										//flex:1,
										readOnly:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadRfcDoc',
											id : 'loadRfcDoc',
											text : SuppAppMsg.supplierLoad,
											action : 'loadRfcDoc',
											margin: '12 10 0 0',
											cls: 'buttonStyle',
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerDom',
									colspan:3,
									width:800,
									//flex:1,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 250,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : SuppAppMsg.supplierForm37,
										name : 'domDocument',
										id : 'domDocument',
										//width:100,
										//flex:1,
										readOnly:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											//flex:1,
											itemId : 'loadDomDoc',
											id : 'loadDomDoc',
											text : SuppAppMsg.supplierLoad,
											action : 'loadDomDoc',
											margin: '12 10 0 0',
											cls: 'buttonStyle'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerBank',
									colspan:3,
									width:800,
									//flex:1,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 250,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : SuppAppMsg.supplierForm38,
										name : 'edoDocument',
										id : 'edoDocument',
										//width:400,
										//flex:1,
										readOnly:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											//flex:1,
											itemId : 'loadEdoDoc',
											id : 'loadEdoDoc',
											text : SuppAppMsg.supplierLoad,
											action : 'loadEdoDoc',
											margin: '12 10 0 0',
											cls: 'buttonStyle'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerIden',
									colspan:3,
									width:800,
									//flex:1,
									defaults : {
										labelWidth : 250,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : SuppAppMsg.supplierForm39, 
										name : 'identDocument',
										id : 'identDocument',
										//width:400,
										//flex:1,
										readOnly:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadIdentDoc',
											id : 'loadIdentDoc',
											text : SuppAppMsg.supplierLoad,
											action : 'loadIdentDoc',
											margin: '12 10 0 0',
											cls: 'buttonStyle'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerActaConstitutiva',
									colspan:3,
									width:800,
									//flex:1,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 250,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : SuppAppMsg.supplierForm40,
										name : 'actaConstitutiva',
										id : 'actaConstitutiva',
										//width:400,
										//flex:1,
										readOnly:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadActaConst',
											id : 'loadActaConst',
											text : SuppAppMsg.supplierLoad,
											action : 'loadActaConst',
											margin: '12 10 0 0',
											cls: 'buttonStyle'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerRcp',
									colspan:3,
									hidden:true,
									width:800,
									//flex:1,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 250,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : SuppAppMsg.supplierForm41,
										name : 'rpcDocument',
										id : 'rpcDocument',
										//width:400,
										readOnly:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?true:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadRpcDocument',
											id : 'loadRpcDocument',
											text : SuppAppMsg.supplierLoad,
											action : 'loadRpcDocument',
											margin: '12 10 0 0',
											cls: 'buttonStyle'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerLegalExistence',
									colspan:3,
									hidden:true,
									width:800,
									//flex:1,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 250,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : SuppAppMsg.supplierForm42,
										name : 'legalExistence',
										id : 'legalExistence',
										//width:400,
										//flex:1,
										readOnly:true,
										margin: '12 10 0 0',
										//allowBlank:role=='ANONYMOUS'?false:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadlegalExistence',
											id : 'loadlegalExistence',
											text : SuppAppMsg.supplierLoad,
											action : 'loadlegalExistence',
											margin: '12 10 0 0',
											cls: 'buttonStyle'
										}]
							    },{
									xtype: 'container',
									layout: 'hbox',
									id:'documentContainerForeingResidence',
									colspan:3,
									width:800,
									allowBlank:true,
									hidden:true,
									//hidden:role=='ANONYMOUS'?false:true,
									defaults : {
										labelWidth : 250,
										xtype : 'textfield',
										margin: '12 0 0 0'
									},
							        items:[{
							        	xtype : 'textfield',
										fieldLabel : SuppAppMsg.supplierForm43,
										name : 'foreingResidence',
										id : 'foreingResidence',
										//width:400,
										//flex:1,
										allowBlank:true,
										readOnly:true,
										margin: '12 10 0 0',
										allowBlank:role=='ANONYMOUS'?true:true
									    },{
											xtype: 'button',
											width:100,
											itemId : 'loadForeingResidence',
											id : 'loadForeingResidence',
											text : SuppAppMsg.supplierLoad,
											action : 'loadForeingResidence',
											allowBlank:true,
											margin: '12 10 0 0',
											cls: 'buttonStyle'
										}]
							    },{
							    	//OUTSOURCING
									xtype: 'container',
									layout: 'vbox',
									id:'documentContainerOutSourcing',
									colspan:3,
									width:800,
									//flex:1,
									hidden:true,
							        items:[
							        	{
							        		xtype:'container',
							        		layout:'hbox',
							        		defaults : {
												labelWidth : 250,
												xtype : 'textfield',
												margin: '0 0 0 0'
											},
							        		items:[
							        			{
										        	xtype : 'textfield',
													fieldLabel : SuppAppMsg.outsourcingSTPSLabel,
													name : 'textSTPS',
													id : 'textSTPS',
													//width:400,
													//flex:1,
													readOnly:true,
													allowBlank:true
												    },{
														xtype: 'button',
														width:100,
														itemId : 'loadSTPS',
														margin:'0 0 0 12',
														id : 'loadSTPS',
														text : SuppAppMsg.supplierLoad,
														action : 'loadSTPS',
														cls: 'buttonStyle'
													}
							        		]
							        	
							        },{
						        		xtype:'container',
						        		layout:'hbox',
						        		defaults : {
											labelWidth : 250,
											xtype : 'textfield',
											margin: '15 10 0 0'
										},
						        		items:[
						        			{
									        	xtype : 'textfield',
												fieldLabel : SuppAppMsg.outsourcingIMSSLabel,
												name : 'textIMSS',
												id : 'textIMSS',
												//width:400,
												//flex:1,
												readOnly:true,
												allowBlank:true
											    },{
													xtype: 'button',
													width:100,
													itemId : 'loadIMSS',
													id : 'loadIMSS',
													text : SuppAppMsg.supplierLoad,
													action : 'loadIMSS',
													cls: 'buttonStyle'
											}
						        			]
							        }]
							    }]
							    },{
									xtype : 'displayfield',
									value : SuppAppMsg.supplierForm44,
									margin: '30 0 0 0',
									colspan:3,
									id : 'camposobligatorios',
									fieldStyle: 'font-weight:bold',
									fieldCls: 'no-border-displayfield'
									},{
									xtype : 'displayfield',
									value : '',
									id:'hrefFileList',
									fieldCls: 'no-border-displayfield',
									height:20,
									//width:800,
									flex:1,
									margin: '10 0 20 10',
									colspan:3

								},{
								xtype : 'displayfield',
								value : 'DOCUMENTACIÓN DE USO INTERNO:',
								height:20,
								margin: '50 20 0 10',
								hidden:true,
								colspan:3,
								fieldStyle: 'font-weight:bold',
								fieldCls: 'no-border-displayfield'
							},{
								xtype : 'displayfield',
								value : '',
								id:'internalFileList',
								fieldCls: 'no-border-displayfield',
								hidden:role=='ANONYMOUS'?true:false,
								height:20,
								//width:800,
								flex:1,
								margin: '10 0 0 10',
								colspan:3

							},{
									xtype: 'button',
									iconCls : 'icon-save',
									width:250,
									itemId : 'sendSupplierForm',
									id : 'sendSupplierForm',
									text : SuppAppMsg.supplierForm45,
									action : 'sendSupplierForm',
									colspan:3,
									fieldStyle: 'padding-left:350px;',
									hidden:role=='ANONYMOUS'?false:true,
									cls: 'buttonStyle'
								},{
									xtype: 'button',
									iconCls : 'icon-save',
									width:250,
									itemId : 'updateSupplierForm',
									id : 'updateSupplierForm',
									text : SuppAppMsg.supplierForm46,
									action : 'updateSupplierForm',
									colspan:3,
									cls: 'buttonStyle',
									//hidden:role=='ANONYMOUS'
									//	||role == 'ROLE_SUPPLIER'?true:false,
									//hidden:true,
									hidden:	role=='ROLE_ADMIN'
											||role=='ROLE_CXP'
											||(role == 'ROLE_SUPPLIER' && isMainSupplierUser) //Solo el proveedor puede modificar su información
											||role == 'ROLE_TREASURY'
											||role == 'ROLE_ACCOUNTING'
											||role == 'ROLE_TAX'
											||role == 'ROLE_PURCHASE'?false:true,
									/*hidden : role=='ROLE_ADMIN'
											||role=='ROLE_CXP'
											||role == 'ROLE_PURCHASE'?false:true,*/
									fieldStyle: 'padding-left:350px;'
								},{								
									xtype: 'button',
									iconCls : 'icon-save',
									width:250,
									itemId : 'updateCatCodeSuppForm',
									id : 'updateCatCodeSuppForm',
									text : SuppAppMsg.supplierForm76,
									action : 'updateCatCodeSuppForm',
									colspan:3,
									hidden:	true,
									//hidden : role == 'ROLE_ADMIN'?false:true,
									fieldStyle: 'padding-left:350px;',
									cls: 'buttonStyle'
								},{
									xtype: 'button',
									iconCls : 'icon-save',
									width:250,
									itemId : 'updateEmailSupplierForm',
									id : 'updateEmailSupplierForm',
									text : 'Actualizar Email',
									action : 'updateEmailSupplierForm',
									colspan:3,
									/*hidden:	role=='ROLE_ADMIN'
											||role=='ROLE_CXP'
											||role == 'ROLE_SUPPLIER'
											||role == 'ROLE_PURCHASE'?false:true,*/
									hidden : role=='ROLE_ADMIN'?true:true,
									fieldStyle: 'padding-left:350px;',
									cls: 'buttonStyle'
								},{
									xtype: 'button',
									iconCls : 'icon-save',
									width:250,
									itemId : 'updateSupplierFormDraft',
									id : 'updateSupplierFormDraft',
									text : SuppAppMsg.supplierForm47,
									action : 'updateSupplierFormDraft',
									colspan:3,
									hidden:role=='ANONYMOUS'?false:true,
									fieldStyle: 'padding-left:350px;',
									cls: 'buttonStyle'
								}
								,{
									xtype : 'displayfield',
									value : '',
									height:20,
									margin: '20 0 0 10',
                                    fieldCls: 'no-border-displayfield',
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
								 },{
									name : 'webSite',
									id : 'webSite',
									hidden:true
								 },{
									name : 'batchNumber',
									id : 'batchNumber',
									hidden:true
								 }
								 


					]}
			];

		


		
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