	Ext.define('SupplierApp.view.purchaseOrder.PurchaseOrderGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.purchaseOrderGrid',
    loadMask: true,
	frame:false,
	border:false,
	flex: 1,
	selModel: {
        checkOnly: true,
        mode: 'SIMPLE'
    },
    selType: 'checkboxmodel',
	cls: 'extra-large-cell-grid',  
	scroll :  true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' },
		enableTextSelection: true,
		stripeRows: true,
	    markDirty: false,
	    listeners: {
	        refresh: function (view) {
	            var grid = view.up('grid');
	            if (!grid) return;

	            Ext.defer(function () {
	                // Autoajuste de columnas según contenido
	                Ext.each(grid.columns, function (col) {
	                    if (col.autoSize) col.autoSize();
	                    else if (col.autoSizeColumn) col.autoSizeColumn();

	                    // Ajuste adicional según header (por texto largo)
	                    var headerText = col.text || '';
	                    if (headerText && col.getEl()) {
	                        var headerEl = col.getEl().down('.x-column-header-text');
	                        if (headerEl) {
	                            var textWidth = Ext.util.TextMetrics.measure(headerEl, headerText).width + 20;
	                            if (textWidth > col.getWidth()) {
	                                col.setWidth(textWidth);
	                            }
	                        }
	                    }
	                });

	                // Repartir espacio sobrante solo si sobra
	                Ext.defer(function () {
	                    var totalWidth = 0;
	                    var gridWidth = grid.getWidth();

	                    // Calcular ancho total de columnas visibles
	                    Ext.each(grid.columns, function (col) {
	                        if (!col.hidden) totalWidth += col.getWidth();
	                    });

	                    // Si sobra espacio, lo repartimos
	                    if (totalWidth < gridWidth) {
	                        var diff = gridWidth - totalWidth - 10; // margen visual
	                        var visibles = Ext.Array.filter(grid.columns, function (col) {
	                            return !col.hidden;
	                        });
	                        var extra = diff / visibles.length;

	                        Ext.each(visibles, function (col) {
	                            col.setWidth(col.getWidth() + extra);
	                        });

	                        grid.updateLayout();
	                    }
	                }, 100);
	            }, 200);
	        }
	    }

	},
    initComponent: function() {
    	this.emptyText = SuppAppMsg.emptyMsg;
    	var poController = SupplierApp.app.getController("SupplierApp.controller.PurchaseOrder");
	 
    	var status = null;
    	
    	if(role == 'ROLE_SUPPLIER'){
        	status = Ext.create('Ext.data.Store', {
        	    fields: ['id', 'name'],
        	    data : [
        	    	{"id":"STATUS_OC_RECEIVED", "name":SuppAppMsg.purchaseStatus6},
        	        {"id":"STATUS_OC_APPROVED", "name":SuppAppMsg.purchaseStatus7},
        	        {"id":"STATUS_OC_INVOICED", "name":SuppAppMsg.purchaseStatus2},
        	        {"id":"STATUS_OC_PAID", "name":SuppAppMsg.purchaseStatus3},
        	        {"id":"STATUS_OC_PAYMENT_COMPL", "name":SuppAppMsg.purchaseStatus4},
        	        {"id":"STATUS_OC_CANCEL", "name":SuppAppMsg.purchaseStatus5}        	        
        	    ]
        	});
    	}else{
    		status = Ext.create('Ext.data.Store', {
        	    fields: ['id', 'name'],
        	    data : [
        	        {"id":"STATUS_OC_RECEIVED", "name":SuppAppMsg.purchaseStatus6},
        	        {"id":"STATUS_OC_APPROVED", "name":SuppAppMsg.purchaseStatus7},
        	        {"id":"STATUS_OC_INVOICED", "name":SuppAppMsg.purchaseStatus2},
        	        {"id":"STATUS_OC_PAID", "name":SuppAppMsg.purchaseStatus3},
        	        {"id":"STATUS_OC_PAYMENT_COMPL", "name":SuppAppMsg.purchaseStatus4},
        	        {"id":"STATUS_OC_CANCEL", "name":SuppAppMsg.purchaseStatus5}
        	    ]
        	});
    	}

    	var tipoOCStore= Ext.create('Ext.data.Store', {
    	    fields: ['id', 'name'],
    	    data : [
    	        {"id":"OP", "name":SuppAppMsg.purchaseOrderTypePayOrder},
    	        {"id":"OS", "name":SuppAppMsg.purchaseOrderTypePurchaseOrder}
    	    ]
    	});
    	
    	Ext.define('typeCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.purchaseOrderTypeComboName,
    	    store: tipoOCStore,
    	    alias: 'widget.poTypeCombo',
    	    queryMode: 'local',
    	    displayField: 'name',
    	    //labelWidth:40,
    	    valueField: 'id',
    	    //margin:'20 20 0 0',
    	    id:'poTypeCombo',
    	    itemId:'poTypeCombo',
    	    //width:150,
    	    labelAlign: 'top',
    	    flex:.6
    	   /* listeners: {
    	        afterrender: function() {
    	           if(role == 'ROLE_WNS'){
    	        	   this.setValue("STATUS_OC_PROCESSED");    
    	           }
    	        }
    	    }*/
    	});
    	
    	Ext.define('statusCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.fiscalTitle22,
    	    store: status,
    	    alias: 'widget.poComboStatus',
    	    queryMode: 'local',
    	    displayField: 'name',
    	    //labelWidth:40,
    	    valueField: 'id',
    	   // margin:'20 20 0 0',
    	    id:'poComboStatus',
    	    itemId:'poComboStatus',
    	    //width:150,
    	    flex:.8,
    	    listeners: {
    	        afterrender: function() {
    	           if(role == 'ROLE_WNS'){
    	        	   this.setValue("STATUS_OC_PROCESSED");    
    	           }
    	        }
    	    },labelAlign: 'top'
    	});
    	
		this.store =  storeAndModelFactory('SupplierApp.model.PurchaseOrder',
                'orderModel',
                'supplier/orders/searchOrders.action', 
                false,
                {
					poNumber:'0',
					supNumber:'',
					poFromDate:'',
					poToDate:'',
					poTypeCombo:'',
					status:'OC ENVIADA',
					userName:userName
                },
			    "", 
			    10);
 
        this.columns = [
           {
        	text     : SuppAppMsg.paymentTitle1,
			//width: 70,
        	flex : 1,
			dataIndex: 'orderCompany'
		},{
            text     : SuppAppMsg.purchaseOrderNumber,
            //width: 80,
            flex : 1,
            dataIndex: 'orderNumber'
        },{
            text     : SuppAppMsg.purchaseOrderType,
            //width: 40,
            flex : 1,
            dataIndex: 'orderType',
            hidden:false
        },{
            text     : SuppAppMsg.purchaseOrderSupplier,
            flex : 1,
            dataIndex: 'addressNumber'
        },{
        	text     : SuppAppMsg.suppliersName,
            //width: 280,
        	flex : 2,
            dataIndex: 'longCompanyName'
        },{
            text     : SuppAppMsg.approvalRequestDate,
            //width: 120,
            flex : 1,
            dataIndex: 'dateRequested',
            renderer: function(value, metaData, record, row, col, store, gridView){
            	debugger
            	var returnValue = '';
            	if(value != null && value != '' && value!= undefined){
					    		
					    		returnValue =   Ext.util.Format.date(new Date(value), 'd-m-Y H:i:s');
					    		
            	}
            	return returnValue;
            }
        },{
            text     : SuppAppMsg.purchaseOrderFechaAprovacion,
            //width: 120,
            flex : 1,
            dataIndex: 'promiseDelivery',
            renderer : Ext.util.Format.dateRenderer("d-m-Y"),
            hidden:true
        },{
            text     : 'Fecha estimada de recibo',
            //width: 150,
            flex : 1,
            dataIndex: 'promiseDelivery',
            renderer : Ext.util.Format.dateRenderer("d-m-Y"),
            hidden:true
        },{
            text     : SuppAppMsg.purchaseOrderÌmporteTotal,
            //width: 110,
            flex : 1,
            dataIndex: 'orderAmount',
            renderer: function(value, meta, record) {
            	if(record.data.currecyCode == "MXP"){
            		return Ext.util.Format.number(record.data.orderAmount,'0,0.00');
            	}else{
            		return Ext.util.Format.number(record.data.foreignAmount,'0,0.00');
            	}
            }
        },{
            text     : SuppAppMsg.purchaseOrderCurrency,
            //width: 60,
            flex : 1,
            dataIndex: 'currecyCode'
        },{
            hidden:true,
            dataIndex: 'invoiceUuid'
        },{
            hidden:true,
            dataIndex: 'supplierEmail'
        },{
            hidden:true,
            dataIndex: 'paymentUuid'
        },{
            hidden:true,
            dataIndex: 'paymentType'
        },{
            hidden:true,
            dataIndex: 'invoiceNumber'
        },{
            text     : SuppAppMsg.fiscalTitle22,
            //width: 120,
            flex : 1,
            dataIndex: 'orderStauts',
            hidden:true
        },{
            align: 'left',
            text     : SuppAppMsg.fiscalTitle22,
            //width: 170,
            flex : 1.2,
            renderer: function(value, meta, record) {
            	var status = {
            	        STATUS_OC_RECEIVED: 'OC RECIBIDA',
            	        STATUS_OC_APPROVED: 'OC APROBADA',
            	        STATUS_OC_SENT: 'OC ENVIADA',
            	        STATUS_OC_CLOSED: 'OC CERRADA',
            	        STATUS_OC_INVOICED: 'OC FACTURADA',
            	        STATUS_OC_PROCESSED: 'OC PROCESADA',
            	        STATUS_OC_PAID: 'OC PAGADA',
            	        STATUS_OC_PAYMENT_COMPL: 'OC COMPLEMENTO',
            	        STATUS_OC_CANCEL: 'OC CANCELADA',
            	        STATUS_OC_OBSOLETE: 'OC OBSOLETA'
            	};
            	            	
            	switch (record.data.orderStauts) {
            	  case status.STATUS_OC_RECEIVED:
            	    //return "ORDEN LIBERADA";
            		return SuppAppMsg.purchaseStatus1.toUpperCase();
            	    break;
            	  case status.STATUS_OC_APPROVED:
              	    //return "ORDEN APROBADA";
            		return SuppAppMsg.purchaseStatus7.toUpperCase();
              	    break;
            	  case status.STATUS_OC_SENT:
            		//return "ORDEN LIBERADA";
              		return SuppAppMsg.purchaseStatus1.toUpperCase();
              	    break;
            	  case status.STATUS_OC_INVOICED:
            		  return SuppAppMsg.purchaseStatus8;
              	    break;
            	  case status.STATUS_OC_RECEIVED:
            		  return SuppAppMsg.purchaseStatus9;
              	    break;
            	  case status.STATUS_OC_PROCESSED:
            		  return SuppAppMsg.purchaseStatus10;
              	    break;
            	  case status.STATUS_OC_PAID:
            		  return SuppAppMsg.purchaseStatus11;
              	    break;
            	  case status.STATUS_OC_PAYMENT_COMPL:
            		  return SuppAppMsg.purchaseStatus12;
              	    break;
            	  case status.STATUS_OC_CANCEL:
            		  return SuppAppMsg.purchaseStatus13;
              	    break;
            	  case status.STATUS_OC_OBSOLETE:
            		  return SuppAppMsg.purchaseStatus14;
                	break;
              	  default:
              		break;
            	}

             }
        },{
            text     : 'Status de Factura',
            //width: 100,
            flex : 1,
            dataIndex: 'status',
            hidden:true
        },{
            text     : SuppAppMsg.purchaseOrderRecibosFacturas,
            align: 'center',
            //width: 120,
            flex : 1,
            renderer: function(value, meta, record) {
            	
            	 if (record.data.orderType !== 'OP') {
            	var id = Ext.id();
        		 Ext.defer(function(){
                     new Ext.Button({
             			 name : 'showReceipts',
            			 itemId : 'showReceipts',
            			 iconCls:'icon-document',
                         text: SuppAppMsg.purchaseOpen,
                         cls: 'buttonStyle',
                         handler: function(grid, rowIndex, colIndex) {
                         	this.fireEvent('buttonclick', grid, record);
                         }
                     }).render(document.body, id);
                 },50);
                 return Ext.String.format('<div id="{0}"></div>', id);
            	 }	else {
                     // Si orderType no es 'OP', no mostrar el componente
                     return '';
                 }
             }
        },
        
        {
            text: SuppAppMsg.purchaseOrderCreditNotes,
            align: 'center',
            //width: 120,
            flex : 1,
            hidden: false,
            renderer: function(value, meta, record) {
                // Verifica si orderType es igual a 'OP'
                if (record.data.orderType === 'OP') {
                    var id = Ext.id();
                    Ext.defer(function() {
                        new Ext.Button({
                            name: 'showCreditNotes',
                            itemId: 'showCreditNotes',
                            iconCls: 'icon-cancel',
                            text: SuppAppMsg.purchaseOpen,
                            cls: 'buttonStyle',
                            handler: function(grid, rowIndex, colIndex) {
                                this.fireEvent('buttonclick', grid, record);
                            }
                        }).render(document.body, id);
                    }, 50);
                    return Ext.String.format('<div id="{0}"></div>', id);
                } else {
                    // Si orderType no es 'OP', no mostrar el componente
                    return '';
                }
            }
        }

        
        /*{
            text     : SuppAppMsg.purchaseOrderCreditNotes,
            align: 'center',
            width: 120,
            hidden:false,
            renderer: function(value, meta, record) {
            	var id = Ext.id();
        		 Ext.defer(function(){
                     new Ext.Button({
             			 name : 'showCreditNotes',
            			 itemId : 'showCreditNotes',
            			 iconCls:'icon-cancel',
                         text: SuppAppMsg.purchaseOpen,
                         handler: function(grid, rowIndex, colIndex) {
                         	this.fireEvent('buttonclick', grid, record);
                         }
                     }).render(document.body, id);
                 },50);
                 return Ext.String.format('<div id="{0}"></div>', id);
		            	
             }
        }*/
        
        ,{
            xtype: 'actioncolumn', 
            //width: 50,
            flex : 0.5,
            header: SuppAppMsg.purchaseTitle30,
            align: 'center',
			name : 'openPONotes',
			itemId : 'openPONotes',
            style: 'text-align:center;',
            items: [
            	{
            	icon:'resources/images/notepad.png',
          	     iconCls: 'increaseSize',
            	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
            		  debugger
              		  if(r.data.notes == null || r.data.notes == '') {
        	              return "x-hidden-display";
        	          }else{
        	        	  return "increaseSize";
        	          }
              	  },
                  handler: function(grid, rowIndex, colIndex) {
                  	this.fireEvent('buttonclick', grid, rowIndex, colIndex);
             }}]
        },{
            align: 'center',
            //width: 140,
            flex : 1,
            renderer: function(value, meta, record) {
	            	var id = Ext.id();
	            	var status = {
	            	        STATUS_OC_RECEIVED: 'OC RECIBIDA',
	            	        STATUS_OC_APPROVED: 'OC APROBADA',
	            	        STATUS_OC_SENT: 'OC ENVIADA',
	            	        STATUS_OC_CLOSED: 'OC CERRADA',
	            	        STATUS_OC_INVOICED: 'OC FACTURADA',
	            	        STATUS_OC_PROCESSED: 'OC PROCESADA',
	            	        STATUS_OC_PAID: 'OC PAGADA',
	            	        STATUS_OC_PAYMENT_COMPL: 'OC COMPLEMENTO',
	            	        STATUS_OC_CANCEL: 'OC CANCELADA',
	            	        STATUS_OC_OBSOLETA: 'OC OBSOLETA'
	            	};
	            	
	            	var showButton = false;
	            	
	            	switch (record.data.orderStauts) {
	            	  case status.STATUS_OC_RECEIVED:
	            		showButton = false;
	            	    break;
	            	  case status.STATUS_OC_APPROVED:
		            	showButton = false;
		            	break;
	            	  case status.STATUS_OC_SENT:
	            		showButton = false;
	              	    break;
	            	  case status.STATUS_OC_INVOICED:
	            		showButton = true;
	              	    break;
	            	  case status.STATUS_OC_RECEIVED:
	            		showButton = true;
	              	    break;
	            	  case status.STATUS_OC_PROCESSED:
	            		  showButton = true;
	              	    break;
	            	  case status.STATUS_OC_PAID:
	            		  showButton = true;
	              	    break;
	            	  case status.STATUS_OC_PAYMENT_COMPL:
	            		  showButton = false;
	              	    break;
	            	  case status.STATUS_OC_CANCEL:
	            		  showButton = false;
	              	    break;
	            	  case status.STATUS_OC_OBSOLETA:
	            		  showButton = false;
	              	  break;
	              	  default:
	              		break;
	            	}
	            	
	            	showButton=(record.data.orderType!=="OP");
	
	            	if(role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN' || role == 'ROLE_MANAGER'){
		            	if(showButton){
			        		 Ext.defer(function(){
			                     new Ext.Button({
			             			 name : 'uploadPayment',
			            			 itemId : 'uploadPayment',
			            			 iconCls:'icon-add',
			            			 cls: 'buttonStyle',
			                         text: SuppAppMsg.suppliersLoadFile,
			                         handler: function(grid, rowIndex, colIndex) {
			                         	this.fireEvent('buttonclick', grid, record);
			                         }
			                     }).render(document.body, id);
			                 },50);
			
			                 return Ext.String.format('<div id="{0}"></div>', id);
		            	}
	            	}
             }
        }];
        
        if(role == 'ROLE_PURCHASE_READ'){

            this.dockedItems = [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    style: {
                        background: 'white'
                      },
                      layout: {
                          type: 'hbox',
                          align: 'middle',
                          pack: 'start'
                      },
                      defaults: {
                          //margin: '5 10 5 0',
                          labelAlign: 'top',
                      },
                    items: [
                    	{
    						xtype: 'textfield',
    			            fieldLabel: SuppAppMsg.purchaseOrderNumber,
    			            id: 'poNumber',
    			            itemId: 'poNumber',
    			            name:'poNumber',
    			            flex:1
    					},
    					{
    						xtype: 'textfield',
    			            fieldLabel: SuppAppMsg.suppliersNumber,
    			            id: 'supNumber',
    			            itemId: 'supNumber',
    			            name:'supNumber',
    			            flex:1
    					},{
    						 xtype: 'datefield',
 						    fieldLabel: SuppAppMsg.purchaseOrderDesde,
 						    id: 'poFromDate',
 						    itemId: 'poFromDate',
 						    name: 'poFromDate',
 						    flex:1
    					},{
    						xtype: 'datefield',
    			            fieldLabel: SuppAppMsg.purchaseOrderHasta,
    			            id: 'poToDate',
    			            itemId: 'poToDate',
    			            name:'poToDate',
    			            flex:1
    					},{ 
    						xtype: 'poComboStatus'
    					},{
    						xtype:'poTypeCombo'
    					}
                    ]
                },
                {
                	xtype: 'toolbar',
                    dock: 'top',
                    layout: {
                        type: 'hbox',
                        align: 'middle',
                        pack: 'start'
                    },
                    defaults: {
                        margin: '2 20 5 10' 
                    },
                    items: [
                    	{
    		           		xtype:'button',
    			            text: SuppAppMsg.suppliersSearch,
    			            iconCls: 'icon-appgo',
    			            action:'poSearch',
    			            cls: 'buttonStyle',
    			            margin:'0 20 0 10'
    					},{
    		           		xtype:'button',
    			            text: 'Aprobar facturas seleccionadas',
    			            iconCls: 'icon-accept',
    			            action:'poInvAccept',
    			            cls: 'buttonStyle',
    			            //hidden:role == 'ROLE_SUPPLIER' || role == 'ROLE_WNS'?true:false,
    			            hidden : true,
    			            //margin:'2 20 5 10'
    					},{
    		           		xtype:'button',
    			            text: 'Rechazar facturas seleccionadas',
    			            iconCls: 'icon-delete',
    			            action:'poInvReject',
    			            //hidden:role == 'ROLE_SUPPLIER'?true:false,
    			            hidden : true,
    			            cls: 'buttonStyle',
    			            //margin:'2 20 5 10'
    					},
    					{
    		           		xtype:'button',
    			            text: SuppAppMsg.purchaseOrderCCPG,
    			            iconCls: 'icon-accept',
    			            action:'poLoadCompl',
    			            cls: 'buttonStyle',
    			            //margin:'2 20 5 10',
    			            hidden:role=='ROLE_PURCHASE_READ' || role == 'ROLE_WNS'?true:false
    					},
    					{
    		           		xtype:'button',
    			            text: SuppAppMsg.purchaseOrderRO,
    			            iconCls: 'icon-accept',
    			            action:'poReasignPurchases',
    			            hidden:true,
    			            //hidden:role == 'ROLE_ADMIN' || role == 'ROLE_MANAGER' ?false:true,
    			            cls: 'buttonStyle',
    			            //margin:'2 20 5 10'
    					},
    					{
    		           		xtype:'button',
    			            text: SuppAppMsg.purchaseOrderCP,
    			            iconCls: 'icon-accept',
    			            action:'poPaymentCalendar',
    			            hidden:role == 'ROLE_ADMIN' || role == 'ROLE_MANAGER' ?false:true,
    			            cls: 'buttonStyle',
    			            //margin:'2 20 5 10'
    					},
    					{
    		           		xtype:'button',
    			            text: SuppAppMsg.purchaseOrderFTP,
    			            iconCls: 'icon-accept',
    			            action:'poLoadFTPInv',
    			            hidden:true,
    			            //hidden:role == 'ROLE_ADMIN' || role == 'ROLE_MANAGER' ?false:true,
    			            cls: 'buttonStyle',
    			            //margin:'2 20 5 10'
    					},'->'
    					,
    					{
    		           		xtype:'button',
    			            text: SuppAppMsg.purchaseOrderIP,
    			            iconCls: 'icon-accept',
    			            action:'poLoadPayment',
    			            hidden:true,
    			            //hidden:role == 'ROLE_ADMIN'?false:true,
    			            cls: 'buttonStyle',
    			            //margin:'2 20 5 10'
    					},{
    						xtype: 'displayfield',
    			            value: SuppAppMsg.replicacion,
    			            //width : 100,
    			            flex : 1,
    			            hidden:true,
    			            //hidden:role == 'ROLE_ADMIN'?false:true,
    		            	},{
    						xtype: 'datefield',
    			            fieldLabel: SuppAppMsg.purchaseOrderDesde,
    			            id: 'fromDate',
    			            maxValue: new Date(),
    			            format: 'd/m/Y',
    			            //width:140,
    			            flex : 1,
    			            //labelWidth:30,
    			            hidden:true,
    			            //hidden:role == 'ROLE_ADMIN'?false:true,
    					},{
    						xtype: 'datefield',
    			            fieldLabel: SuppAppMsg.purchaseOrderHasta,
    			            //margin:'2 10 5 15',
    			            id: 'toDate',
    			            //width:140,
    			            flex : 1,
    			            maxValue: new Date(),
    			            //labelWidth:30,
    			            hidden:true,
    			            //hidden:role == 'ROLE_ADMIN'?false:true,
    					},{
    						xtype: 'textfield',
    			            fieldLabel: SuppAppMsg.purchaseOrderSupplier,
    			            id: 'addressNumber',
    			            //width:120,
    			            flex : 1,
    			            //labelWidth:50,
    			            //margin:'2 10 5 15',
    			            hidden:true,
    			            //hidden:role == 'ROLE_ADMIN'?false:true,
    					},{
    						xtype: 'numberfield',
    			            fieldLabel: 'OC',
    			            id: 'orderNumber',
    			            hideTrigger:'true', 
    			            //width:100,
    			            flex : 1,
    			            //labelWidth:20,
    			            //margin:'2 10 5 15',
    			            hidden:true,
    			            //hidden:role == 'ROLE_ADMIN'?false:true,
    					},{
    		           		xtype:'button',
    			            text: 'Importar',
    			            iconCls: 'icon-accept',
    			            action:'poLoadPurchases',
    			            hidden:true,
    			            //hidden:role == 'ROLE_ADMIN'?false:true,
    			            cls: 'buttonStyle',
    			            //margin:'2 10 5 10'
    					}
                    ]
                },
    		    getPagingContent()
            ];
                    	
        } else {
        
        this.dockedItems = [
            {
            	xtype: 'toolbar',
                dock: 'top',
                layout: {
                    type: 'hbox',
                    align: 'middle',
                    pack: 'start'
                },
                defaults: {
                    labelAlign: 'top'
                },
                items: [
                	{
						xtype: 'textfield',
			            fieldLabel: SuppAppMsg.purchaseOrderNumber,
			            id: 'poNumber',
			            itemId: 'poNumber',
			            name:'poNumber',
			            flex : .5,
					},
					{
						xtype: 'textfield',
			            fieldLabel: SuppAppMsg.suppliersNumber,
			            id: 'supNumber',
			            itemId: 'supNumber',
			            name:'supNumber',
			            value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
			            fieldStyle: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?'border:none;background-color: #ddd; background-image: none;':'',
			            readOnly: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?true:false,
			            //width:150,
			            //labelWidth:90,
			            //margin:'20 20 0 10',
			           	flex : .5
			           //labelAlign: 'top'
					},{
						 xtype: 'datefield',
						    fieldLabel: SuppAppMsg.purchaseOrderDesde,
						    id: 'poFromDate',
						    itemId: 'poFromDate',
						    name: 'poFromDate',
						    //width: 100,
						    flex : .5
						    //labelWidth: 35,
						    //margin: '0 20 0 10',
						    //labelAlign: 'top' // Set label position to top
					},{
						xtype: 'datefield',
			            fieldLabel: SuppAppMsg.purchaseOrderHasta,
			            id: 'poToDate',
			            itemId: 'poToDate',
			            name:'poToDate',
			            //width:100,
			            flex : .5
			            //labelWidth:35,
			            //margin:'0 40 0 10',
			            //labelAlign: 'top'
					},{ 
						xtype: 'poComboStatus'
					},{
						xtype:'poTypeCombo'
					},{
				        xtype: 'displayfield',
				        value: '',
				        flex:.4
				    }
                ]
            },
            {
            	xtype: 'toolbar',
                dock: 'top',
                layout: {
                    type: 'hbox',
                    align: 'middle',
                    pack: 'start'
                },
                defaults: {
                    //margin: '0 20 5 0' 
                },
                items: [
                	{
		           		xtype:'button',
			            text: SuppAppMsg.suppliersSearch,
			            iconCls: 'icon-appgo',
			            action:'poSearch',
			            cls: 'buttonStyle',
			           // margin:'0 20 0 10'
					},{
		           		xtype:'button',
			            text: 'Aprobar facturas seleccionadas',
			            iconCls: 'icon-accept',
			            action:'poInvAccept',
			            cls: 'buttonStyle',
			            //hidden:role == 'ROLE_SUPPLIER' || role == 'ROLE_WNS'?true:false,
			            hidden : true,
			            //margin:'2 20 5 10'
					},{
		           		xtype:'button',
			            text: 'Rechazar facturas seleccionadas',
			            iconCls: 'icon-delete',
			            action:'poInvReject',
			            //hidden:role == 'ROLE_SUPPLIER'?true:false,
			            hidden : true,
			            cls: 'buttonStyle',
			            //margin:'2 20 5 10'
					},
					{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseOrderCCPG,
			            iconCls: 'icon-accept',
			            action:'poLoadCompl',
			            cls: 'buttonStyle',
			            //margin:'2 20 5 10',
			            hidden:role=='ROLE_PURCHASE_READ' || role == 'ROLE_WNS'?true:false
					},
					{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseOrderRO,
			            iconCls: 'icon-accept',
			            action:'poReasignPurchases',
			            hidden:true,
			            //hidden:role == 'ROLE_ADMIN' || role == 'ROLE_MANAGER' ?false:true,
			            cls: 'buttonStyle',
			            //margin:'2 20 5 10'
					},
					{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseOrderCP,
			            iconCls: 'icon-accept',
			            action:'poPaymentCalendar',
			            hidden:role == 'ROLE_ADMIN' || role == 'ROLE_MANAGER' ?false:true,
			            cls: 'buttonStyle',
			            //margin:'2 20 5 10'
					},
					{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseOrderFTP,
			            iconCls: 'icon-accept',
			            action:'poLoadFTPInv',
			            hidden:true,
			            //hidden:role == 'ROLE_ADMIN' || role == 'ROLE_MANAGER' ?false:true,
			            cls: 'buttonStyle',
			            //margin:'2 20 5 10'
					}
					,
					{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseOrderIP,
			            iconCls: 'icon-accept',
			            action:'poLoadPayment',
			            hidden:true,
			            //hidden:role == 'ROLE_ADMIN'?false:true,
			            cls: 'buttonStyle',
			            //margin:'2 20 5 10'
					},{
						xtype: 'displayfield',
			            value: SuppAppMsg.replicacion,
			            //width : 100,
			            flex:1,
			            hidden:true,
			            //hidden:role == 'ROLE_ADMIN'?false:true,
		            	},{
						xtype: 'datefield',
			            fieldLabel: SuppAppMsg.purchaseOrderDesde,
			            id: 'fromDate',
			            maxValue: new Date(),
			            format: 'd/m/Y',
			            //width:140,
			            //labelWidth:30,
			            flex:1,
			            hidden:true,
			            //hidden:role == 'ROLE_ADMIN'?false:true,
					},{
						xtype: 'datefield',
			            fieldLabel: SuppAppMsg.purchaseOrderHasta,
			            //margin:'2 10 5 15',
			            id: 'toDate',
			            //width:140,			            
			            maxValue: new Date(),
			            //labelWidth:30,
			            hidden:true,
			            flex:1			            
			            //hidden:role == 'ROLE_ADMIN'?false:true,
					},{
						xtype: 'textfield',
			            fieldLabel: SuppAppMsg.purchaseOrderSupplier,
			            id: 'addressNumber',
			            //width:120,
			            //labelWidth:50,
			            //margin:'2 10 5 15',
			            hidden:true,
			            flex:1
			            //hidden:role == 'ROLE_ADMIN'?false:true,
					},{
						xtype: 'numberfield',
			            fieldLabel: 'OC',
			            id: 'orderNumber',
			            hideTrigger:'true', 
			            //width:100,
			            //labelWidth:20,
			            //margin:'2 10 5 15',
			            hidden:true,
			            flex:1,
			            //hidden:role == 'ROLE_ADMIN'?false:true,
					},{
		           		xtype:'button',
			            text: 'Importar',
			            iconCls: 'icon-accept',
			            action:'poLoadPurchases',
			            hidden:true,
			            //hidden:role == 'ROLE_ADMIN'?false:true,
			            flex:1,
			            cls: 'buttonStyle',
			            //margin:'2 10 5 10'
					},{
		           		xtype:'button',
			            text: 'Facturas SAT',
			            iconCls: 'icon-accept',
			            action:'uploadSATInvoiceZip',
			            hidden:true,
			            //hidden:role == 'ROLE_ADMIN'?false:true,
			            flex:1,
			            cls: 'buttonStyle',
			            //margin:'2 10 5 10'
					}
                ]
            },
		    getPagingContent()
        ];
        
    }


      
        this.callParent(arguments);
        
        this.getStore().on('load', function(store, records) {
            this.calculateHeight();
        }, this);
        
        this.on('resize', function() {
            this.calculateHeight();
        }, this);
    },
    
    calculateHeight: function() {
        var me = this;
        Ext.defer(function() {
            /*
        	var rowHeight = 40; // Altura aproximada por fila
            var headerHeight = 40; // Altura del header
            var toolbarHeight = me.dockedItems.length > 0 ? me.dockedItems[0].getHeight() : 0;
            var pagingHeight = 40; // Altura de la barra de paginación
            
            var recordCount = me.getStore().getCount();
            var calculatedHeight = (recordCount * rowHeight) + headerHeight + toolbarHeight + pagingHeight;
            
            // Establecer altura mínima y máxima
            var minHeight = 400;
            var maxHeight = Ext.getBody().getViewSize().height - 100;
            
            var finalHeight = Math.max(minHeight, Math.min(calculatedHeight, maxHeight));
            
            me.setHeight(finalHeight);
            */
        }, 100);
    }
});