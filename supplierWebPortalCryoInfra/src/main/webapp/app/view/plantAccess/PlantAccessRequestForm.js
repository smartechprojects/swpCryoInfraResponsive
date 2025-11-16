Ext.define('SupplierApp.view.plantAccess.PlantAccessRequestForm',	{
	extend : 'Ext.form.Panel',
	alias : 'widget.plantAccessRequestForm',
	border : false,
	frame : false,
	style : 'border: solid #fff 1px',
	autoScroll : true,
	layout: {
        type: 'vbox',
        align: 'stretch'
    },
    defaults: {
        margin: '5 10 5 10'
    },
	initComponent : function() {
		
		
		  Ext.define('UbicationModel', {
	            extend: 'Ext.data.Model',
	            fields: [
	                {name: 'order', type: 'string'},
	                {name: 'description', type: 'string'}
	            ]
	        });
		  
		 var ubicationStore = Ext.create('Ext.data.Store', {
	            model: 'UbicationModel',
	            data: [] // Inicialmente vacío, se puede llenar con datos si es necesario
	        });
		 
		 
		this.items = [{
			xtype: 'container',
            layout: 'anchor',
            defaults: {
                labelWidth: 150,
                margin: '5 10 5 10',
                labelAlign: 'top'
            },
            items: [{
            	xtype : 'displayfield',
				value : SuppAppMsg.plantAccess6,
				height:20,
				//margin:'5 10 0 300',
				margin: '5 10 5 0',
				colspan:3,
				fieldStyle: 'font-weight:bold'
            },{
				xtype: 'textfield',
	            fieldLabel: 'RFC',
	            name:'rfc',
	            id: 'paRequestRfc',
	            itemId: 'paRequestRfc',
	            //width:8,
	            //labelWidth:100,
	            //margin:'5 10 0 10',
	            hidden:true
            },{
				xtype: 'textfield',
	            fieldLabel: 'Razón Social',
	            name:'razonSocial',
	            //width:8,
	            //labelWidth:100,
	            //margin:'5 10 0 10',
	            hidden:true
            },{
				xtype: 'textfield',
	            fieldLabel: 'Estatus',
	            name:'status',
	            id: 'paRequestStatus',
	            itemId: 'paRequestStatus',
	            //width:8,
	            //labelWidth:100,
	            //margin:'5 10 0 10',
	            hidden:true
            },{
				xtype: 'textfield',
	            fieldLabel: 'Numero Proveedor',
	            name:'addressNumberPA',
	            id: 'paRequestAddressNumber',
	            itemId: 'paRequestAddressNumber',
	            //width:300,
	            //labelWidth:100,
	            //margin:'5 10 0 10',
	            hidden:true
            },/*{
				xtype: 'textfield',
	            fieldLabel: SuppAppMsg.plantAccess2,
	            id: 'paNameRequest',
	            itemId: 'paNameRequest',
	            name:'nameRequest',
	            width:360,
	            //labelWidth:150,
	            //margin:'5 10 0 150',
	            allowBlank:false,
	            maxLength : 100,
	            labelAlign: 'top',
	            enforceMaxLength: true,
                validator: function(value) {
                    if (value.length < 1) {
                        return 'Debe tener al menos 1 carácter';
                    }
                    if (value.length > 100) {
                        return 'Solo se permiten hasta 100 caracteres';
                    }
                    return true;
                }
            }, */ {
                xtype: 'container',
                layout: 'hbox',
                //margin: '5 10 0 150',
                margin: '0 5 5 0',
                defaults: {
                    labelAlign: 'top',
                    margin: '0 15 0 0' 
                },
                items: [
                	{
        				xtype: 'textfield',
        	            fieldLabel: SuppAppMsg.plantAccess2,
        	            id: 'paNameRequest',
        	            itemId: 'paNameRequest',
        	            name:'nameRequest',
        	            width:360,
        	            //labelWidth:150,
        	            //margin:'5 10 0 150',
        	            allowBlank:false,
        	            maxLength : 100,
        	            labelAlign: 'top',
        	            enforceMaxLength: true,
                        validator: function(value) {
                            if (value.length < 1) {
                                return 'Debe tener al menos 1 carácter';
                            }
                            if (value.length > 100) {
                                return 'Solo se permiten hasta 100 caracteres';
                            }
                            return true;
                        }
                    },{
				xtype: 'textfield',
	            fieldLabel: SuppAppMsg.plantAccess7,
	            id: 'paContractorCompany',
	            itemId: 'paContractorCompany',
	            name:'contractorCompany',	            
	            width:250,	            
	            //labelWidth:150,
	            //margin:'0 0 10 0',
	            //allowBlank:false,
	            flex: 2,
	            readOnly:true,
	            maxLength : 254
                },
                {
                    xtype: 'checkbox',
                    boxLabel: SuppAppMsg.plantAccess100,
                    id:'paisSubcontractService',
                    itemId:'paisSubcontractService',
                    name: 'subcontractService',
                    inputValue: 'true', // Valor enviado si el checkbox está marcado
                    uncheckedValue: 'false', // Valor enviado si el checkbox no está marcado
                    //margin: '0 0 0 10', // Ajusta el margen para separar del elemento anterior
                    margin: '28 0 0 0',
                    flex: 1,
                    	 listeners: {
                    	        change: function(checkbox, newValue, oldValue) {
                    	          
                    	        	 var form = checkbox.up('form');
                                     var subContractedCompany = form.down('textfield[name=subContractedCompany]');
                                     var subContractedCompanyRFC = form.down('textfield[name=subContractedCompanyRFC]');
                                     
                                     if (newValue) {
                                         // Habilitar campos y hacerlos requeridos
                                         subContractedCompany.setDisabled(false);
                                         subContractedCompany.allowBlank = false; // Campo requerido
                                         subContractedCompany.validate(); // Forzar validación
                                         
                                         subContractedCompanyRFC.setDisabled(false);
                                         subContractedCompanyRFC.allowBlank = false; // Campo requerido
                                         subContractedCompanyRFC.validate(); // Forzar validación
                                     } else {
                                         // Deshabilitar campos y hacerlos no requeridos
                                         subContractedCompany.setDisabled(true);
                                         subContractedCompany.allowBlank = true; // Campo no requerido
                                         subContractedCompany.setValue('');
                                         
                                         subContractedCompanyRFC.setDisabled(true);
                                         subContractedCompanyRFC.allowBlank = true; // Campo no requerido
                                         subContractedCompanyRFC.setValue('');
                                     }
                    	        	
                    	        	
                    	        },
                    	        
                    	        afterrender: function(comp) {
                    	        	  new Ext.tip.ToolTip({
                    	                  target: comp.getEl(),
                    	                  html: '<div style="white-space: normal; width: 250px;">' + 
                    	                        'Seleccionar solo en caso de subcontratar servicios' +
                    	                        '</div>',
                    	                  trackMouse: true,
                    	                  dismissDelay: 15000,
                    	                  anchor: 'top',
                    	                  showDelay: 100,
                    	                  autoHide: false,
                    	                  closable: false,
                    	                  style: {
                    	                      'font-size': '12px',
                    	                      'line-height': '1.4',
                    	                      'padding': '8px'
                    	                  }
                    	              });
                    	        }
                    	        
                    	    }	
                }
	            ]
            },
            
            {
                xtype: 'container',
                layout: 'hbox',
                //margin: '5 10 0 150',
                margin: '0 5 5 0',
                defaults: {
                    labelAlign: 'top',
                    margin: '0 15 0 0' 
                },
                items: [
                    {
                        xtype: 'textfield',
                        fieldLabel: SuppAppMsg.plantAccess101,
                        id: 'paSubContractedCompany',
                        itemId: 'paSubContractedCompany',
                        name: 'subContractedCompany',
                        flex: 1,
                        maxWidth: 360,
                        labelWidth: 150,
                        //margin: '0 0 10 0',
                        allowBlank: true,
                        disabled: true,
                        maxLength: 51,
                        enforceMaxLength: true,
                        maskRe: /[a-zA-ZÁÉÍÓÚÜÑáéíóúüñ0-9\s.,_-]/,
                        regex: /^(?!\s)[a-zA-ZÁÉÍÓÚÜÑáéíóúüñ0-9\s.,_-]{1,51}(?<!\s)$/,
                        minLength: 1,
                        minLengthText: 'Debe ingresar al menos 1 carácter',
                        regexText: 'Solo letras o numeros (1-51 caracteres)',
                        listeners: {
                            keypress: function(field, e) {
                                if (field.getValue().length >= 51 && e.getCharCode() !== e.BACKSPACE) {
                                    e.stopEvent();
                                }
                            },
                            blur: function(field) {
                                var val = field.getValue().trim();
                                field.setValue(val);
                                
                                // Solo muestra error si hay valor y no es válido
                                if (val && !field.isValid()) {
                                    Ext.Msg.alert('Error', 
                                        val.length > 51 ? 'Máximo 51 caracteres' : field.regexText);
                                    // Elimina el focus() para evitar el bucle
                                }
                            },
                            change: function(field, newVal) {
                                if (newVal.length > 51) {
                                    field.setValue(newVal.substring(0, 51));
                                }
                            }
                        }
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: SuppAppMsg.plantAccess102,
                        id: 'paSubContractedCompanyRFC',
                        itemId: 'paSubContractedCompanyRFC',
                        name: 'subContractedCompanyRFC',
                        flex:1,
                        maxWidth: 250,
                        labelWidth: 170,
                        margin: '0 0 0 10',
                        allowBlank: true,
                        disabled: true,
                        maxLength: 13,
                        minLength: 12,
                        enforceMaxLength: true,
                        maskRe: /[a-zA-Z0-9]/,
                        regex: /^[a-zA-Z0-9]{12,13}$/,
                        regexText: 'Debe contener hasta 13 caracteres alfanuméricos',
                        listeners: {
                            keypress: function(field, e) {
                                if (field.getValue().length >= 13 && e.getCharCode() !== e.BACKSPACE) {
                                    e.stopEvent();
                                }
                            },
                            blur: function(field) {
                                var val = field.getValue();
                                // Solo muestra error si hay valor y no es válido
                                if (val && !field.isValid()) {
                                    Ext.Msg.alert('Error', field.regexText);
                                    // Elimina el focus() para evitar el bucle
                                }
                            },
                            change: function(field, newVal) {
                                if (newVal.length > 13) {
                                    field.setValue(newVal.substring(0, 13));
                                }
                            }
                        }
                    }
                ]
            }
            ,
            {
                xtype: 'container',
                layout: 'hbox',
                margin: '0 5 5 0',
                defaults: {
                	labelAlign: 'top',
                    margin: '0 15 0 0'
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel: SuppAppMsg.plantAccess8,
                    id: 'paContractorRepresentative',
                    itemId: 'paContractorRepresentative',
                    name: 'contractorRepresentative',
                    maxWidth: 360,
                    width: '100%',
                    labelWidth: 150,
                   // margin: '5 10 0 150',
                    allowBlank: false,
                    maxLength: 100,
                    enforceMaxLength: true,
                    validator: function(value) {
                        if (value.length < 1) {
                            return 'Debe tener al menos 1 carácter';
                        }
                        if (value.length > 100) {
                            return 'Solo se permiten hasta 100 caracteres';
                        }
                        return true;
                    }
                },{
    				xtype: 'textfield',
    	            fieldLabel: SuppAppMsg.plantAccess74,
    	            id: 'paContacEmergency',
    	            itemId: 'paContacEmergency',
    	            name:'contactEmergency',
    	            maxWidth:200,
    	            flex: 1,
    	            labelWidth:150,
    	            margin:'0 0 10 0',
    	            allowBlank:false,
    	            maxLength : 254,
    	            maskRe: /[0-9]/, // Solo permite números mientras se escribe
    	            regex: /^\d{0,10}$/, // Permite solo hasta 10 dígitos
    	            regexText: 'Solo se permiten números y hasta 10 dígitos',
    	            validator: function(value) {
    	                if (value.length !== 10) {
    	                    return 'El valor debe tener exactamente 10 números';
    	                }
    	                return true;
    	            },
                	enforceMaxLength: true,
                	maxLength: 10,
                },{
                    xtype: 'datefield',
                    fieldLabel: SuppAppMsg.plantAccess75,
                    id: 'pafechaFirmaGuia',
                    itemId: 'pafechaFirmaGuia',
                    name: 'fechafirmGui',
                    maxWidth: 250,
                    flex: 1,
                    labelWidth: 100,
                    margin: '0 0 0 10',
                    allowBlank: false,
                    maxLength: 254,
                    format: 'd/m/Y' // O el formato de fecha que prefieras
                }]
            },
            {
                xtype: 'container',
                layout: 'hbox',
               // margin: '5 10 0 150',
                id: 'containerOrden',
	            itemId: 'containerOrden',
				name : 'containerOrden',
				 margin: '5 0 5 0',
				 defaults: {
					 labelAlign: 'top',
		             margin: '0 15 0 0'
		            },
                items: [{
                    xtype: 'checkbox',
                    boxLabel: SuppAppMsg.plantAccess76,
                    id:'sinOrden',
                    itemId:'sinOrden',
                    name: 'sinOrden',
                    inputValue: 'true', // Valor enviado si el checkbox está marcado
                    uncheckedValue: 'false', // Valor enviado si el checkbox no está marcado
                   // margin: '5 10 0 0', // Ajusta el margen para separar del elemento anterior
                    margin: '25 10 10 0',
                    	 listeners: {
                    	        change: function(checkbox, newValue, oldValue) {
                    	            var ordenNumberField = checkbox.up('form').down('textfield[name=paOrdenNumberInput]'); // Obtener el campo de texto
                    	            var empresaPlantField = checkbox.up('form').down('textfield[name=empresaPlantRequest]'); // Obtener el campo de texto
                    	            // Desactivar el campo de texto si el checkbox se marca
                    	            
                    	            
//                    	            var ordenNumberField = Ext.getCmp('ordenNumberFieldId');
//                                    var empresaPlantField = Ext.getCmp('empresaPlantFieldId');
                    	            var butonadd = Ext.getCmp('butonAddOrder');
                                    if (newValue) {
                                        ordenNumberField.setVisible(false); // Ocultar el campo
                                        empresaPlantField.setVisible(true); // Mostrar el campo
//                                        butonadd.setVisible(true);
                                    } else {
                                        ordenNumberField.setVisible(true); // Mostrar el campo
                                        empresaPlantField.setVisible(false); // Ocultar el campo
//                                        butonadd.setVisible(false);

                                    }

                    	            // Limpiar la tabla siempre que cambie el estado del checkbox
                    	            //var grid = checkbox.up('form').down('gridpanel'); // Obtener la referencia al grid
                    	            //var store = grid.getStore();
                    	            //store.removeAll();
                    	        }
                    	    }	
                }, 
                {				
    				xtype: 'combobox',
    				fieldLabel : SuppAppMsg.plantAccess78,
    	            id: 'empresaPlantRequest',
    	            itemId: 'empresaPlantRequest',
    				name : 'empresaPlantRequest',
    				typeAhead: true,
                    typeAheadDelay: 100,
                    minChars: 1,
                    queryMode: 'local',
    				store : getCompanyStore(),
                    displayField: 'companyName',
                    valueField: 'company',
                    labelWidth:50,
                    maxWidth : 300,
                    flex: 1,
                    editable: false,
                    hidden:true,
    				listeners: {
    			    	select: function (comboBox, records, eOpts) {
    			    		//var contrib = records[0].data.udcKey;
    			    		//Ext.getCmp('addAproval').setValue(records[0].data.strValue2);
    			    	}
    			    }
                }
                ,{
                    xtype: 'textfield',
                    fieldLabel: SuppAppMsg.plantAccess69,
                    id: 'paOrdenNumber',
                    itemId: 'paOrdenNumber',
                    name: 'ordenNumber',
                    maxWidth: 150,
                    flex: 1,
                    labelWidth: 100,
                    allowBlank: false,
                    hidden:true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: SuppAppMsg.plantAccess69,
                    id: 'paOrdenNumberInput',
                    itemId: 'paOrdenNumberInput',
                    name: 'paOrdenNumberInput',
                    maxWidth: 250,
                    flex: 1,
                    labelWidth: 100,
                    allowBlank: true,
                    maxLength: 254,
                    triggerCls: 'x-form-search-trigger',
                    maxLength: 10,  // Limita a 10 caracteres
                    enforceMaxLength: true,
                    formBind: false,
                    validator: function(value) {
                       debugger
                        if (value.length > 10) {
                            return 'Solo se permiten hasta 10 caracteres';
                        }
                        return true;
                    },
                    onTriggerClick: function() {
                        var value = this.getValue();

                        if (value) {
                            Ext.Ajax.request({
                                url: 'plantAccess/validateOrderInput.action', 
                                method: 'POST',
                                params: {
                                	order: value
                                },
                                success: function(response) {
                                	debugger
                                    var data = Ext.decode(response.responseText);
                                    // Asumiendo que la respuesta contiene campos llamados 'ordenNumber' y 'empresaPlant'
                                    var ordenNumberField = Ext.getCmp('paOrdenNumberInput');
                                    var descriptionfield = Ext.getCmp('description');
                                    var butonadd = Ext.getCmp('butonAddOrder');
                                    if (data.success) {
                                        ordenNumberField.setValue(data.order);
                                        descriptionfield.setValue(data.description);
//                                        ordenNumberField.setReadOnly(true);
//                                        butonadd.setVisible(true);
                                    } else {
                                    	ordenNumberField.setValue("");
                                        descriptionfield.setValue("");
//                                        ordenNumberField.setReadOnly(false);
//                                        butonadd.setVisible(false);
                                        
                                        var msg = Ext.Msg.show({
                                            title: 'Error',
                                            msg: SuppAppMsg.plantAccess79,
                                            buttons: Ext.Msg.OK,
                                            fn: function() {
                                                // Callback cuando se cierra el mensaje
                                            }
                                        });
                                        
                                        function applyButtonStyle(attempts) {
                                            attempts = attempts || 0;
                                            if (attempts > 10) return; // Timeout después de 10 intentos
                                            
                                            var dialog = msg.dialog || msg;
                                            var footer = dialog.down('toolbar[dock="bottom"]');
                                            
                                            if (footer && footer.items && footer.items.length > 0) {
                                                var okButton = footer.items.getAt(0);
                                                if (okButton && okButton.el) {
                                                    okButton.addCls('buttonStyle');
                                                    console.log('Estilo aplicado al botón OK');
                                                } else {
                                                    Ext.defer(applyButtonStyle, 50, this, [attempts + 1]);
                                                }
                                            } else {
                                                Ext.defer(applyButtonStyle, 50, this, [attempts + 1]);
                                            }
                                        }
                                        
                                        Ext.defer(applyButtonStyle, 10);
                                    }
                                },
                                failure: function(response) {
                                    var msg = Ext.Msg.show({
                                        title: 'Error',
                                        msg: SuppAppMsg.plantAccess80,
                                        buttons: Ext.Msg.OK,
                                        fn: function() {
                                            // Callback cuando se cierra el mensaje
                                        }
                                    });
                                    
                                    function applyButtonStyle(attempts) {
                                        attempts = attempts || 0;
                                        if (attempts > 10) return; // Timeout después de 10 intentos
                                        
                                        var dialog = msg.dialog || msg;
                                        var footer = dialog.down('toolbar[dock="bottom"]');
                                        
                                        if (footer && footer.items && footer.items.length > 0) {
                                            var okButton = footer.items.getAt(0);
                                            if (okButton && okButton.el) {
                                                okButton.addCls('buttonStyle');
                                                console.log('Estilo aplicado al botón OK');
                                            } else {
                                                Ext.defer(applyButtonStyle, 50, this, [attempts + 1]);
                                            }
                                        } else {
                                            Ext.defer(applyButtonStyle, 50, this, [attempts + 1]);
                                        }
                                    }
                                    
                                    Ext.defer(applyButtonStyle, 10);
                                }
                            });
                        } else {
                        	
                        	  var msg = Ext.Msg.show({
                                  title: 'Error',
                                  msg: SuppAppMsg.plantAccess81,
                                  buttons: Ext.Msg.OK,
                                  fn: function() {
                                      // Callback cuando se cierra el mensaje
                                  }
                              });
                              
                        	 // Función recursiva para aplicar el estilo
                            function applyButtonStyle(attempts) {
                                attempts = attempts || 0;
                                if (attempts > 10) return; // Timeout después de 10 intentos
                                
                                var dialog = msg.dialog || msg;
                                var footer = dialog.down('toolbar[dock="bottom"]');
                                
                                if (footer && footer.items && footer.items.length > 0) {
                                    var okButton = footer.items.getAt(0);
                                    if (okButton && okButton.el) {
                                        okButton.addCls('buttonStyle');
                                        console.log('Estilo aplicado al botón OK');
                                    } else {
                                        Ext.defer(applyButtonStyle, 50, this, [attempts + 1]);
                                    }
                                } else {
                                    Ext.defer(applyButtonStyle, 50, this, [attempts + 1]);
                                }
                            }
                            
                            Ext.defer(applyButtonStyle, 10);
                        }
                    },
                 // Evento cuando pierde el foco
                    listeners: {
                        blur: function(field) {
                            field.onTriggerClick(); // Llama a la función cuando el campo pierde el foco
                        }
                    }
                }
                , {
                    xtype: 'textfield',
                    fieldLabel: SuppAppMsg.plantAccess77,
                    name: 'description',
                    id: 'description',
                    itemId: 'description',
                    maxWidth: 450,
                    flex: 1,
                    labelWidth: 80,
                    maxLength: 254,
                    maxLength: 100,  // Limita a 10 caracteres
                    enforceMaxLength: true,
                    formBind: false,
                    validator: function(value) {
                       
                        if (value.length > 100) {
                            return 'Solo se permiten hasta 100 caracteres';
                        }
                        return true;
                    },
                }, {
                    xtype: 'button',
                    iconCls : 'icon-add',
                    margin: '25 10 10 0',
                    name: 'butonAddOrder',
                    id: 'butonAddOrder',
                    itemId: 'butonAddOrder',
                    width: 40,
                    cls: 'buttonStyle',
                    handler: function() {
//                    	var butonadd = Ext.getCmp('butonAddOrder');
//                    	butonadd.setVisible(false)
                    	debugger
                    	 var form = this.up('form');
                         var grid = form.down('gridpanel'); // Obtener la referencia al grid
                         var store = grid.getStore(); // Obtener el store del grid
                         
                         if (store.getCount() >= 4) {
                             Ext.Msg.alert('Error', 'llego al maximo de ordenes');
                             return;
                         }

                         var sinOrdenCheckbox = form.down('checkbox[name=sinOrden]'); // Obtener el checkbox
                         var esSinOrden = sinOrdenCheckbox.getValue(); // Verificar si el checkbox está marcado

                         var paOrdenNumberInput = form.down('textfield[name=paOrdenNumberInput]').getValue();
                         var parempresaPlantRequest = form.down('textfield[name=empresaPlantRequest]').getValue();
                         // Obtener el valor del campo de entrada "Descripción"
                         var description = form.down('textfield[name=description]').getValue();
                         debugger
                         if(esSinOrden){
                        	 if(parempresaPlantRequest==null||parempresaPlantRequest==''||description==null||description.trim()==''){
                        		  Ext.Msg.alert('Error', SuppAppMsg.plantAccess82);
//                        		  butonadd.setVisible(true)
                        		 return;
                        	 }
                        	 
                        	 
                         }else{
                        	 if(paOrdenNumberInput==null||paOrdenNumberInput==''||description==null||description.trim()==''){
                       		  Ext.Msg.alert('Error', SuppAppMsg.plantAccess82);
                       		 return;
                       	 }
                        	 
                         }
                         
                         
                         

                         // Realizar la validación en el servidor mediante AJAX
                         Ext.Ajax.request({
                             url: 'plantAccess/verifyOrderInput.action', // Reemplaza con la URL correcta para la validación
                             method: 'POST',
                             params: {
                                 esSinOrden: esSinOrden,
                                 paOrdenNumberInput: paOrdenNumberInput,
                                 empresaPlantRequest:parempresaPlantRequest,
                                 description:description
                             },
                             success: function(response) {
                            	 debugger
                                 var result = Ext.decode(response.responseText);
                                 if (result.success) {
                                	 debugger
                                     var order = result.message;
                                	 var ordenNumberField = Ext.getCmp('paOrdenNumberInput');
                                	 
                                	 var esNumerico = /^\d+$/.test(order); // Verifica si 'order' contiene solo números

                                	 var existeRegistro = store.findRecord('order', order, 0, false, true, true);

                                	 if (esNumerico && existeRegistro) {
                                	     Ext.Msg.alert('Error', SuppAppMsg.plantAccess99);
                                    	 ordenNumberField.setReadOnly(false);
                                	     return; // Detener ejecución si 'order' es numérico y existe en el store
                                	 }
                                	 

                                     // Verificar si la combinación de "order" y "descripcion" ya existe en el store
                                     var existeRegistro = store.findRecord('order', order, 0, false, true, true);
                                     if (existeRegistro && existeRegistro.get('description') === description) {
                                    	 ordenNumberField.setReadOnly(false);
                                         Ext.Msg.alert('Error', SuppAppMsg.plantAccess83);
                                         return; // Detener la ejecución si el registro ya existe
                                     }

                                     // Limpiar los campos de "order" y "description"
                                     form.down('textfield[name=paOrdenNumberInput]').setValue('');
                                     form.down('textfield[name=description]').setValue('');
                                	 ordenNumberField.setReadOnly(false);
                                	 var sinOrdenCheckbox = form.down('checkbox[name=sinOrden]');
                                	 if (sinOrdenCheckbox && sinOrdenCheckbox.checked) {
                                	     sinOrdenCheckbox.setValue(false); 
                                	     sinOrdenCheckbox.fireEvent('change', sinOrdenCheckbox, false, true); 
                                	 }
                                     // Agregar los datos al store
                                     store.add({
                                         order: order, // Asegúrate de asignar al campo correcto del store
                                         description: description
                                     });

                                     // Concatenar los datos
                                     var concatenatedData = '';
                                     store.each(function(record, index) {
                                         var order = record.get('order');
                                         var description = record.get('description');
                                         concatenatedData += order + ',' + description;
                                         if (index < store.getCount() - 1) {
                                             concatenatedData += '|';
                                         }
                                     });

                                     // Actualizar el campo concatenado
                                     form.down('textfield[name=ordenNumber]').setValue(concatenatedData);
                                 } else {
                                     // Mostrar mensaje de error si la validación falla
                                     Ext.Msg.alert('Error', result.message).setWidth(300);
                                 }
                             },
                             failure: function(response) {
                                 Ext.Msg.alert('Error', SuppAppMsg.plantAccess84);
                             }
                         });
                         
                    }
                }]
            },{
                xtype: 'container',
                layout: 'hbox',
                margin: '0 5 5 0',
                defaults: {
                    labelAlign: 'top',
                    margin: '0 15 0 0'
                },
                items: [{
                xtype: 'gridpanel',
                id: 'ordersPlantaccesGridPanel',
                title: SuppAppMsg.plantAccess9,
                store: ubicationStore,
                columns: [{
                    header: SuppAppMsg.plantAccess85,
                    dataIndex: 'order',
                    maxWidth: 200,
                    flex: 1,
                    editor: {
                        xtype: 'numberfield',
                        allowBlank: false
                    }
                }, {
                    header: SuppAppMsg.plantAccess77,
                    dataIndex: 'description',
                    flex: 1,
                    maxWidth:600,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                }, {
                    xtype: 'actioncolumn',
                    //width: 50,
                    flex: 1,
                    itemId: 'deleteActionColumn',
                    items: [{
                        icon: 'resources/images/delete.png',
                        tooltip: 'Eliminar',
                        handler: function(grid, rowIndex, colIndex) {
                        	debugger;
                            var store = grid.getStore();
                            store.removeAt(rowIndex);
                            
                            // Concatenar los datos
                            var concatenatedData = '';
                            store.each(function(record, index) {
                                var order = record.get('order');
                                var description = record.get('description');
                                concatenatedData += order + ',' + description;
                                if (index < store.getCount() - 1) {
                                    concatenatedData += '|';
                                }
                            });
                            var form = this.up('form');
                            // Actualizar el campo concatenado
                            form.down('textfield[name=ordenNumber]').setValue(concatenatedData);
                            
                        }
                    }]
                }],
                selType: 'rowmodel',
                //height: 1000,
                autoHeight: true,
                //width: 800,
                //margin: '5 5 0 150'
                flex: 1,
                //margin: '5 10 0 10',
                //minWidth: 800
                autoWidth: true
                }]
            },
            {
                xtype: 'container',
                layout: 'hbox',
                margin: '0 5 5 0',
                //margin: '5 10 5 10',
                defaults:{
                	labelAlign: 'top',
                	margin: '0 15 0 0'
                },
                items: [
                	{				
				xtype: 'combobox',
				fieldLabel : SuppAppMsg.plantAccess86,
	            id: 'paPlantRequest',
	            itemId: 'paPlantRequest',
				name : 'plantRequest',
				typeAhead: true,
                typeAheadDelay: 100,
                allowBlank:false,
                minChars: 1,
                queryMode: 'local',
                //forceSelection: true,
				store : getAutoLoadUDCStore('PLANTCRYO', '', '', ''),
                displayField: 'strValue1',
                valueField: 'udcKey',
                labelWidth:100,
                maxWidth : 450,
                typeAheadDelay: 100,
               // margin:'5 10 0 150',
                editable: false,
				listeners: {
			    	select: function (comboBox, records, eOpts) {
			    		//var contrib = records[0].data.udcKey;
			    		//Ext.getCmp('addAproval').setValue(records[0].data.strValue2); 
			    	}
			    }
            },{
				xtype: 'textfield',
	            fieldLabel: SuppAppMsg.plantAccess98,
	            id: 'paEmployerRegistration',
	            itemId: 'paEmployerRegistration',
	            name:'employerRegistration',	            
	            maxWidth:200,	            
	            labelWidth:130,
	           // margin:'5 10 0 100',
	            allowBlank:false,
//	            readOnly:true,
	            maxLength : 254,
//	            maskRe: /[0-9]/, // Solo permite números mientras se escribe
	            regex: /^[a-zA-Z0-9]{0,11}$/, // Permite solo hasta 10 dígitos
	            regexText: 'Solo se permiten alfanumericos y  11 caracteres',
	            validator: function(value) {
	                if (value.length !== 11) {
	                    return 'Llenar con 11 caracteres';
	                }
	                return true;
	            },
            	enforceMaxLength: true,
            	maxLength: 11,
            },
            {
                xtype: 'button',
                text: SuppAppMsg.plantAccess87,
                icon: 'resources/images/doc.png',
                action:'updatePlantAccessRequest',
                id: 'updatePlantAccessRequest',
                cls: 'buttonStyle',
                maxWidth: 180,
                flex: 1, 
                //margin: '10 10 10 150',
                margin: '25 10 0 10',
                handler: function () {
//                    secondContainer.show();
//                    firstContainer.hide();
                }
            }]
            },{
				xtype: 'textfield',
				fieldLabel : SuppAppMsg.plantAccess3,
	            name:'aprovUser',
	            width:400,
	            labelWidth:150,	            
	           // margin:'5 10 0 150',
	            readOnly:true,
	            hidden:true
            },{
				xtype: 'hidden',
	            id: 'heavyEquipmentRequest',
	            itemId: 'heavyEquipmentRequest',
	            name:'heavyEquipment',
            }]
		}]
		
		this.callParent(arguments);
	}
});