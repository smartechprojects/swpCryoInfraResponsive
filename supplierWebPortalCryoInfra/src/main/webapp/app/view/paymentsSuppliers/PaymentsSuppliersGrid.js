	Ext.define('SupplierApp.view.paymentsSuppliers.PaymentsSuppliersGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.paymentsSuppliersGrid',
    itemId: 'paymentsSuppliersGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',  
	store : {
		type:'paymentssuppliers'
	},
	scroll :  true,
	viewConfig: {
	    stripeRows: true,
	    style : { overflow: 'auto', overflowX: 'hidden' },
	    enableTextSelection: true,
	    markDirty: false,
	    listeners: {
	        refresh: function(view) {
	            var grid = view.up('grid');
	            if (!grid) return;
	            // Usar la función centralizada
	            GridUtils.adjustGridLayout(grid, true);
	        },
	        
	        resize: function(view) {
	            var grid = view.up('grid');
	            if (!grid) return;
	            // Usar la función centralizada
	            GridUtils.adjustGridLayout(grid, false);
	        }
	    }
	},
    initComponent: function() {
    	this.emptyText = SuppAppMsg.emptyMsg;
    	var apController = SupplierApp.app.getController("SupplierApp.controller.PaymentsSuppliers");
    	
    	var tipoDoc = null;
    	

    	tipoDoc = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'esp','ing'],
    	    data : [    	        
    	    	{"id":"PT", "esp":"PT","ing":"PT"},
    	        {"id":"PK", "esp":"PK","ing":"PK"},
    	        {"id":"PN", "esp":"PN","ing":"PN"}
    	    ]
    	});
    	
    	var currencyCode = null;
    	var dataMoney=[
     		{"id":"CAD", "esp":"DOLARES CANADIENSES","ing":"CANADIAN DOLLARS"},
    		{"id":"CHF", "esp":"FRANCO SUIZO","ing":"SWISS FRANC"},
    		{"id":"EUR", "esp":"EUROS","ing":"EUROS"},
    		{"id":"GBP", "esp":"LIBRA ESTERLINA","ing":"POUND STERLING"},
    		{"id":"JPY", "esp":"YEN","ing":"YEN"},
    		{"id":"MXP", "esp":"PESOS MEXICANOS","ing":"MEXICAN PESOS"},
    		{"id":"QTZ", "esp":"QUETZAL","ing":"QUETZAL"},
    		{"id":"USD", "esp":"DOLARES","ing":"DOLLARS"}
    		
    	];
    	
    	var dataTypePay=[
    		{"id":"PT", "esp":"Transferencia","ing":"Electronic Funds Transfer"},
	        {"id":"PK", "esp":"Cheque Aut.","ing":"Automated Check"},
	        {"id":"PN", "esp":"Cheque Manual","ing":"Manual Check"}
    	]
    	
    	

    	function getCurrencyNameById(id) {
    	    var languageCode = (navigator.language && (navigator.language.toLowerCase() === 'es' || navigator.language.toLowerCase().startsWith('es-')))
    	        ? 'esp' // Usar español si el idioma del navegador es español
    	        : 'ing'; // Usar inglés en caso contrario

    	    var currency = dataMoney.find(function(item) {
    	        return item.id === id;
    	    });

    	    return currency ? currency[languageCode] : '';
    	}
    	function getTypePayById(id) {
    	    var languageCode = (navigator.language && (navigator.language.toLowerCase() === 'es' || navigator.language.toLowerCase().startsWith('es-')))
    	        ? 'esp' // Usar español si el idioma del navegador es español
    	        : 'ing'; // Usar inglés en caso contrario

    	    var pay = dataTypePay.find(function(item) {
    	        return item.id === id;
    	    });

    	    return pay ? pay[languageCode] : '';
    	}
    	
    	currencyCode = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'esp','ing'],
    	    data :dataMoney
    	});
    	
    	var statusPay = null;
    	

		statusPay = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'esp','ing'],
    	    data : [    	        
    	    	{"id":"P", "esp":"PAGADO",'ing':'PAY'},
    	    	{"id":"C", "esp":"CANCELADO",'ing':'CANCEL'}
    	    ]
    	});
    	
    	
    	
    	Ext.define('tipoDoc', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.paymentsSuppliersTypeDocMatch,
    	    store: tipoDoc,
    	    alias: 'widget.tipoDoc',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    hidden:role!='ROLE_ADMIN',
    	    //editable: false,
    	    displayField: (navigator.language && (navigator.language.toLowerCase() === 'es' || navigator.language.toLowerCase().startsWith('es-')))
            ? 'esp'
            : 'ing',
    	    valueField: 'id',
    	    listeners: {
    	        afterrender: function() {
    	        	   this.setValue("");    
    	        }
    	    }
    	});
    	
    	Ext.define('currencyCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.paymentsSuppliersCurrencyCode,
    	    store: currencyCode,
    	    alias: 'widget.currencyCombo',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    //editable: false,
    	    displayField: (navigator.language && (navigator.language.toLowerCase() === 'es' || navigator.language.toLowerCase().startsWith('es-')))
            ? 'esp'
            : 'ing',
    	    valueField: 'id',
    	    listeners: {
    	        afterrender: function() {
    	        	   this.setValue("");    
    	        }
    	    }
    	});
    	
    	Ext.define('statusPayCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: 'Estatus',
    	    store: statusPay,
    	    alias: 'widget.statusPayCombo',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    //editable: false,
    	    displayField: (navigator.language && (navigator.language.toLowerCase() === 'es' || navigator.language.toLowerCase().startsWith('es-')))
            ? 'esp'
            : 'ing',
    	    valueField: 'id',
    	    listeners: {
    	        afterrender: function() {
    	        	   this.setValue("");    
    	        }
    	    }
    	});
    	
        this.columns = [
		        	{
			            text     : SuppAppMsg.suppliersNumber,
			            maxWidth: 120,
			            flex: 1,
			            dataIndex: 'addressBook',
			            sortable: true 
			        },{
			            text     : SuppAppMsg.plantAccess64,
			           // width: 180,
			            flex: 2,
			            dataIndex: 'suplierCompanyName', 
			            sortable: true
			        },
			        
			        {
			            text     : SuppAppMsg.paymentsSuppliersInvoiceNumber,
			            //width: 230,
			            flex: 2,
			            dataIndex: 'invoiceNumber',  renderer: function(value) {
			                // Verificar si el valor no es nulo ni vacío
			                if (value && value.trim() !== '') {
			                    // Dividir el valor utilizando comas como separador
			                    var splitValues = value.split(',');
			                    // Obtener el primer elemento del arreglo resultante
			                    var firstValue = splitValues[0].trim();
			                    // Devolver el valor renderizado
			                    if (splitValues.length > 1) {
			                        return `<div style="display: flex; justify-content: space-between; align-items: center;">
			                                    <span>${firstValue}</span>
			                                    <span style="color: #999;">▼</span>
			                                </div>`;
			                    } else {
			                        return firstValue;
			                    }
			                } else {
			                    // Si el valor es nulo o vacío, devolver un valor por defecto o vacío
			                    return '';
			                }
			            },
			            sortable: true
			        },{
			            text     : SuppAppMsg.paymentsSuppliersCompany,
			            //width: 130,
			            flex: 1,
			            dataIndex: 'company', 
			            sortable: true
			        },{
			            text     : SuppAppMsg.paymentsSuppliersTypeDocMatch,
			            //width: 150,
			            flex: 1,
			            dataIndex: 'docCotejo',
			              renderer: function(value, metaData, record) {
			                return getTypePayById(value);
			            },
			            sortable: true	
			        },{
			            text     : SuppAppMsg.paymentsSuppliersPayAmount, 
			            maxWidth: 130,
			            flex: 1,
			            dataIndex: 'paymentAmount', 
			            renderer : Ext.util.Format.numberRenderer('0,000.00'),
			            align: 'right',
			            sortable: true
			        },{
			            text: SuppAppMsg.paymentsSuppliersCurrency,
			            //width: 150,
			            flex: 1,
			            dataIndex: 'currencyCode', // Asumiendo que 'currencyCode' es el campo que contiene el ID de la moneda
			            renderer: function(value, metaData, record) {
			                return getCurrencyNameById(value);
			            },
			            sortable: true
			        },
			        {
			            text     : SuppAppMsg.paymentsSuppliersDateAmount,
			            //width: 130,
			            flex: 1,
			            dataIndex: 'paymentDate',
			            renderer: function(value, metaData, record, row, col, store, gridView){
			            	
			            	var returnValue = '';
			            	if(value != null && value != '' && value!= undefined){
								    		
								    		returnValue =   Ext.util.Format.date(new Date(value), 'd-m-Y');
								    		
			            	}
			            	return returnValue;
			            },
			            sortable: true
			        },{
			            text     : SuppAppMsg.taxvaulStatus,
			            //width: 130,
			            flex: 1,
			            dataIndex: 'statusPay',
			            renderer: function(value, metaData, record, row, col, store, gridView){
			            	if(value != '0'){ 
			            		
					    		 return '<span style="">'+SuppAppMsg.paymentsSuppliersCancel+'</span>';
			            	}else{
			            		return '<span style="">'+SuppAppMsg.paymentsSuppliersPay+'</span>';
			            	}
            	return '';
			            },
			            sortable: true
			        }];
        
       
        this.dockedItems = [
            {
              xtype: 'toolbar',
              dock: 'top',
              padding: '0',
              layout: {
                  type: 'hbox'
              },
              defaults: {
                  flex: .3,
                  labelAlign: 'top'
              },
              items: [
            	  {
    		          xtype: 'textfield',
    		          fieldLabel: SuppAppMsg.suppliersNumber,
    		          id: 'paymentsSuppliersAddressNumberGrid',
    		          itemId: 'paymentsSuppliersAddressNumberGrid',
    		          name: 'paymentsSuppliersAddressNumberGrid',
    		          value: role == 'ROLE_SUPPLIER' || role == 'ROLE_SUPPLIER_OPEN' ? addressNumber : '',
    		          fieldStyle: role == 'ROLE_SUPPLIER' || role == 'ROLE_SUPPLIER_OPEN' ? 'border:none;background-color: #ddd; background-image: none;' : '',
    		          readOnly: role == 'ROLE_SUPPLIER' || role == 'ROLE_SUPPLIER_OPEN' ? true : false,
	        		  listeners: {
	                      specialkey: function(field, e) {
	                          if (e.getKey() === e.ENTER) {
	                              // Buscar el botón en el grid completo
	                              var grid = field.up('grid');
	                              if (grid) {
	                                  var button = grid.down('button[action="paymentsSupplierSearch"]');
	                                  if (button) {
	                                      // Disparar el evento click del botón
	                                      button.fireEvent('click', button);
	                                  }
	                              }
	                          }
	                      }
	                  }
    		        },
    		        {
    		          xtype: 'tipoDoc',
    		          id: 'paymentsSupplierstipoDocGrid',
    		          itemId: 'paymentsSupplierstipoDocGrid',
    		          name: 'paymentsSupplierstipoDocGrid',
    		          listeners: {
	                      specialkey: function(field, e) {
	                          if (e.getKey() === e.ENTER) {
	                              // Buscar el botón en el grid completo
	                              var grid = field.up('grid');
	                              if (grid) {
	                                  var button = grid.down('button[action="paymentsSupplierSearch"]');
	                                  if (button) {
	                                      // Disparar el evento click del botón
	                                      button.fireEvent('click', button);
	                                  }
	                              }
	                          }
	                      }
	                  }
    		        },{
    		          xtype: 'currencyCombo',
    		          fieldLabel: SuppAppMsg.paymentsSuppliersCurrencyCode,
    		          id: 'paymentsSuppliersCurrencyCodeGrid',
    		          itemId: 'paymentsSuppliersCurrencyCodeGrid',
    		          name: 'paymentsSuppliersCurrencyCodeGrid',
    		          listeners: {
	                      specialkey: function(field, e) {
	                          if (e.getKey() === e.ENTER) {
	                              // Buscar el botón en el grid completo
	                              var grid = field.up('grid');
	                              if (grid) {
	                                  var button = grid.down('button[action="paymentsSupplierSearch"]');
	                                  if (button) {
	                                      // Disparar el evento click del botón
	                                      button.fireEvent('click', button);
	                                  }
	                              }
	                          }
	                      }
	                  }
    		        },
    		        {
    		          xtype: 'statusPayCombo',
    		          fieldLabel: 'Estatus',
    		          id: 'paymentsSuppliersStatusPayGrid',
    		          itemId: 'paymentsSuppliersStatusPayGrid',
    		          name: 'paymentsSuppliersStatusPayGrid',
    		          listeners: {
	                      specialkey: function(field, e) {
	                          if (e.getKey() === e.ENTER) {
	                              // Buscar el botón en el grid completo
	                              var grid = field.up('grid');
	                              if (grid) {
	                                  var button = grid.down('button[action="paymentsSupplierSearch"]');
	                                  if (button) {
	                                      // Disparar el evento click del botón
	                                      button.fireEvent('click', button);
	                                  }
	                              }
	                          }
	                      }
	                  }
    		        },
    		        {
    		          xtype: 'datefield',
    		          fieldLabel: SuppAppMsg.purchaseOrderDesde,
    		          id: 'paymentsSupplierspoFromDate',
    		          itemId: 'paymentsSupplierspoFromDate',
    		          name: 'paymentsSupplierspoFromDate',
    		          listeners: {
	                      specialkey: function(field, e) {
	                          if (e.getKey() === e.ENTER) {
	                              // Buscar el botón en el grid completo
	                              var grid = field.up('grid');
	                              if (grid) {
	                                  var button = grid.down('button[action="paymentsSupplierSearch"]');
	                                  if (button) {
	                                      // Disparar el evento click del botón
	                                      button.fireEvent('click', button);
	                                  }
	                              }
	                          }
	                      }
	                  }
    		        },
    		        {
    		          xtype: 'datefield',
    		          fieldLabel: SuppAppMsg.purchaseOrderHasta,
    		          id: 'paymentsSupplierspoToDate',
    		          itemId: 'paymentsSupplierspoToDate',
    		          name: 'paymentsSupplierspoToDate',
    		          listeners: {
	                      specialkey: function(field, e) {
	                          if (e.getKey() === e.ENTER) {
	                              // Buscar el botón en el grid completo
	                              var grid = field.up('grid');
	                              if (grid) {
	                                  var button = grid.down('button[action="paymentsSupplierSearch"]');
	                                  if (button) {
	                                      // Disparar el evento click del botón
	                                      button.fireEvent('click', button);
	                                  }
	                              }
	                          }
	                      }
	                  }
    		        },{
				        xtype: 'displayfield',
				        value: '',
				        flex:.3
				    }
              ]},
             {
                xtype: 'toolbar',
                dock: 'top',
                padding: '0',
                defaults: {
                    margin: '5 5 10 0' 
                },
                layout: {
                    type: 'hbox'
                },
                items: [
                	{
      		          xtype: 'button',
      		          text: SuppAppMsg.suppliersSearch,
      		          iconCls: 'icon-appgo',
      		          labelAlign: 'top',
      		          action: 'paymentsSupplierSearch',
      		          cls: 'buttonStyle',
      		          listeners: {
	                    tap: function (button) {
	                    	apController.paymentsSupplierSearch(button);
	                    }
	                },
      		        },      	 
              ]},
		    getPagingContent()
      ];
        
		
        this.callParent(arguments);
    }
});