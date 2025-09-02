Ext.define('SupplierApp.view.purchaseOrder.ReceiptOrderForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.receiptOrderForm',
	border : false,
	frame : false,
	style: 'border: solid #ccc 1px',
	autoScroll : true,
	initComponent : function() {
		
    	var statusStore = Ext.create('Ext.data.SimpleStore', {
    	    fields: ['id', 'status'],
    	    data : [
    	        [0, 'Precios incorrectos'],
    	        [1, 'Mala calidad del producto'],
    	        [2, 'Producto fuera de temperatura'],
    	        [3, 'Producto no solicitado en la orden de compra'],
    	        [4, 'Cantidades incorrectas'],
    	        [5, 'Producto fuera de especificación'],
    	        [6, 'Línea de la Orden de Compra Cancelada'],
    	        [7, 'Otro (Registrelo en las notas adicionales)']
    	    ]
    	});
    	
		this.items = [{
					xtype: 'container',
					layout: 'vbox',
					defaults : {
						labelWidth : 120,
						xtype : 'numberfield',
						margin: '10 0 0 15',
						align: 'stretch',
						hideTrigger : true,
						width:200,
				        decimalPrecision : 4,
				        minValue         : 0,
				        maxValue         : 9999999.99,
				        decimalSeparator: '.',
				        allowNegative: false,
	                    thousandSeparator: ',',
	                    alwaysDisplayDecimals: true,
				        emptyText        : SuppAppMsg.purchaseTitle37
					},
			        items:[{
								xtype : 'displayfield',
								value : SuppAppMsg.purchaseTitle38,
								height:10,
								margin: '5 0 10 3',
								fieldStyle: 'font-weight:bold'
							},{
				        		fieldLabel : SuppAppMsg.purchaseTitle26,
								name : 'lineNumber',
								width : 100,
								labelWidth : 50,
								fieldStyle: 'border:none;background-color: #efefef; background-image: none;',
								readOnly:true
							},{
								xtype : 'textfield',
								name : 'itemNumber',
								hidden:true
							},{
								xtype : 'textfield',
								fieldLabel : SuppAppMsg.purchaseTitle39,
								labelWidth : 50,
								name : 'itemDescription',
								width : 450,
								fieldStyle: 'border:none;background-color: #efefef; background-image: none;',
								readOnly:true
							},{
								xtype:'container',
								layout : {
					                  type :'table',
					                  columns : 4,
					                  tableAttrs: {
					                     style: {
					                        width: '100%',
					                     }
					                  }               
					               },
					               defaults : {
										labelWidth : 120,
										xtype: 'numericfield',
										currencySymbol: '',
					                    useThousandSeparator: true,
					                    decimalSeparator: '.',
					                    thousandSeparator: ',',
					                    alwaysDisplayDecimals: true,
										margin: '3 0 0 0',
										align: 'stretch',
										hideTrigger : true,
										width:200,
										padding: '0 10 0 10',
										fieldStyle: 'border:none;background-color: #efefef; background-image: none;',
										readOnly:true
									},
					               width:850,
					               items : [
					            	    {
					            	    	xtype : 'displayfield',
											value : '',
											colspan:2
										},{
											xtype : 'displayfield',
											value : 'TOLERANCIAS',
											margin: '0 0 5 0',
											width:400,
											fieldStyle: 'font-weight:bold;border-bottom:1px solid #000;text-align:center;',
											colspan:2
										},{
											fieldLabel : SuppAppMsg.purchaseTitle40,
											name : 'quantity'
										},{
											fieldLabel : SuppAppMsg.purchaseTitle41,
											name : 'unitCost',
											
										},{
											fieldLabel : SuppAppMsg.purchaseTitle42,
											labelWidth : 75,
											padding: '0 10 0 40',
											name : 'tolerances_qtyUnits',
											fieldStyle: 'border:none;background-color: #efefef; background-image: none; font-weight:bold;'
										},{
											fieldLabel : '% ' + SuppAppMsg.purchaseTitle42,
											labelWidth : 75,
											name : 'tolerances_qtyPercentage',
											fieldStyle: 'border:none;background-color: #efefef; background-image: none; font-weight:bold;'
										},{
											fieldLabel : SuppAppMsg.purchaseTitle43,
											name : 'received'
										},{
											fieldLabel : SuppAppMsg.purchaseTitle44,
											name : 'extendedPrice'
										},{
											value : ' ',
											xtype : 'displayfield',
											padding: '0 10 0 40',
											fieldStyle: 'border:none;background-color: #fff; background-image: none;'
										},{
											value : ' ',
											xtype : 'displayfield',
											padding: '0 10 0 40',
											fieldStyle: 'border:none;background-color: #fff; background-image: none;'
										},{
											fieldLabel : SuppAppMsg.purchaseTitle45,
											name : 'rejected'
										},{
											fieldLabel : SuppAppMsg.purchaseTitle46,
											name : 'amuntReceived'
										},{
											value : ' ',
											xtype : 'displayfield',
											padding: '0 10 0 40',
											fieldStyle: 'border:none;background-color: #fff; background-image: none;'
										},{
											value : ' ',
											xtype : 'displayfield',
											padding: '0 10 0 40',
											fieldStyle: 'border:none;background-color: #fff; background-image: none;'
										},{
											fieldLabel : SuppAppMsg.purchaseTitle47,
											name : 'pending'
										},{
											fieldLabel : SuppAppMsg.purchaseTitle48,
											name : 'openAmount'
										},{
					            	    	xtype : 'displayfield',
											value : '',
											colspan:2
										}
					            	]
							},{
								xtype:'container',
								hidden:role!='ROLE_SUPPLIER' && role!='ROLE_SUPPLIER_OPEN'?false:true,
										defaults:{labelWidth : 120,
												xtype : 'numberfield',
												margin: '10 0 0 15',
												align: 'stretch',
												hideTrigger : true,
												width:200,
										        decimalPrecision : 4,
										        minValue         : 0,
										        maxValue         : 9999999.99,
										        decimalSeparator: '.',
										        allowNegative: false,
							                    thousandSeparator: ',',
							                    alwaysDisplayDecimals: true,
										        emptyText        : SuppAppMsg.purchaseTitle37
				        	},
								items:[
									{
										xtype : 'displayfield',
										value : SuppAppMsg.purchaseTitle49,
										height:10,
										margin: '35 0 10 3',
										width:400,
										fieldStyle: 'font-weight:bold'
									},{
										fieldLabel : SuppAppMsg.purchaseTitle50,
										name : 'toReceive',
										xtype            : 'numberfield',
								        allowBlank       : false
									},{
										fieldLabel : SuppAppMsg.purchaseTitle51,
										name : 'toReject'
									},{
										xtype: 'combobox',
								        fieldLabel : SuppAppMsg.purchaseTitle52,
								        name:'reason',
								        valueField:'status',
								        displayField:'status',
								        queryMode: 'local',
								        store: statusStore,
								        allowBlank:true,
								        hideTrigger : false,
								        width:450
									},{
										xtype : 'textfield',
										fieldLabel : SuppAppMsg.purchaseTitle33,
										width : 750,
										labelWidth : 120,
										name : 'notes'
									}
								]
								
							}
					]}
			];

		this.tbar = [ {
			iconCls : 'icon-save',
			itemId : 'insertReceipt',
			id : 'insertReceipt',
			text : SuppAppMsg.purchaseTitle53,
			action : 'insertReceipt',
			hidden:role!='ROLE_SUPPLIER' && role!='ROLE_SUPPLIER_OPEN'?false:true
		}];
		this.callParent(arguments);
	}

});