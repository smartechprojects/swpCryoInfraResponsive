Ext.define('SupplierApp.view.purchaseOrder.DeliverPurchaseOrderGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.deliverPurchaseOrderGrid',
    loadMask: true,
	border:false,
	forceFit: true,
	selModel: {
        checkOnly: true,
        injectCheckbox: 'first',
        mode: 'SIMPLE'
    },
    selType: 'checkboxmodel',
	cls: 'extra-large-cell-grid', 
	scroll : false,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
    	
    	var poController = SupplierApp.app.getController("SupplierApp.controller.PurchaseOrder");
    	
		this.store =  storeAndModelFactory('SupplierApp.model.PurchaseOrder',
                'orderModel',
                'supplier/orders/searchEmailedOrders.action', 
                false,
                {
					poNumber:'0',
					supNumber:'',
					poFromDate:'',
					poToDate:'',
					status:'OC RECIBIDA'
                },
			    "", 
			    10);
 
		 this.columns = [
	           {
	            text     : SuppAppMsg.purchaseOrderNumber,
	            width: 100,
	            dataIndex: 'orderNumber'
	        },{
	            text     : SuppAppMsg.purchaseTitle5,
	            width: 90,
	            dataIndex: 'orderType'
	        },{
	            text     : SuppAppMsg.suppliersNumber,
	            width: 100,
	            dataIndex: 'addressNumber'
	        },{
	        	text     : SuppAppMsg.suppliersName,
	            width: 180,
	            dataIndex: 'description'
	        },{
	            text     : SuppAppMsg.purchaseOrderFechaAprovacion,
	            width: 120,
	            dataIndex: 'dateRequested',
	            renderer : Ext.util.Format.dateRenderer("d-m-Y")
	        },{
	            text     : SuppAppMsg.purchaseTitle9,
	            width: 150,
	            dataIndex: 'promiseDelivery',
	            renderer : Ext.util.Format.dateRenderer("d-m-Y"),
	            hidden:true
	        },{
	        	text     : SuppAppMsg.purchaseTitle10 ,
	            width: 180,
	            dataIndex: 'email',
	            listeners:{
					change: function(field, newValue, oldValue){
						field.setValue(newValue.toLowerCase());
					}
				}
	        },{
	        	text     : SuppAppMsg.paymentTitle1,
	            width: 160,
	            dataIndex: 'orderCompany'
	        },{
	            text     : SuppAppMsg.purchaseOrderÌmporteTotal ,
	            width: 110,
	            dataIndex: 'orderAmount',
	            renderer : Ext.util.Format.numberRenderer('0,0.00')
	        },{
	            text     : SuppAppMsg.purchaseOrderCurrency,
	            width: 60,
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
	            text     : 'Status',
	            width: 120,
	            dataIndex: 'orderStauts',
	            hidden:true
	        },{
	            align: 'center',
	            text     : SuppAppMsg.purchaseTitle11,
	            width: 140,
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
	            	        STATUS_OC_CANCEL: 'OC CANCELADA'
	            	};
	            	
	            	switch (record.data.orderStauts) {
	            	  case status.STATUS_OC_RECEIVED:
	            	    return "ORDEN RECIBIDA";
	            	    break;
	            	  case status.STATUS_OC_APPROVED:
		            	    return "ORDEN APROBADA";
		            	    break;
	            	  case status.STATUS_OC_SENT:
	              	    return "ORDEN LIBERADA";
	              	    break;
	            	  case status.STATUS_OC_INVOICED:
	              	    return "ORDEN FACTURADA";
	              	    break;
	            	  case status.STATUS_OC_RECEIVED:
	              	    return "ORDEN RECIBIDA";
	              	    break;
	            	  case status.STATUS_OC_PROCESSED:
	              	    return "FACTURA ACEPTADA";
	              	    break;
	            	  case status.STATUS_OC_PAID:
	              	    return "FACTURA PAGADA";
	              	    break;
	            	  case status.STATUS_OC_PAYMENT_COMPL:
	              	    return "COMPLEMENTO CARGADO";
	              	    break;
	            	  case status.STATUS_OC_CANCEL:
	              	    return "ORDEN CANCELADA";
	              	    break;
	              	  default:
	              		break;
	            	}

	             }
	        },{
	            text     : SuppAppMsg.purchaseTitle12,
	            width: 100,
	            dataIndex: 'status',
	            hidden:true
	        }];
        
        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: {
                    background: 'white'
                  },
                dock: 'top',
                items: [
                	{
						xtype: 'textfield',
			            fieldLabel: SuppAppMsg.purchaseOrderNumber,
			            id: 'delPoNumber',
			            itemId: 'delPoNumber',
			            name:'delPoNumber',
			            width:200,
			            labelWidth:70,
			            margin:'0 20 0 10'
					},{
						xtype: 'textfield',
			            fieldLabel: SuppAppMsg.suppliersNumber,
			            id: 'delSupNumber',
			            itemId: 'delSupNumber',
			            name:'delSupNumber',
			            width:200,
			            labelWidth:90,
			            margin:'20 20 0 10'
					},{
						xtype: 'datefield',
			            fieldLabel:SuppAppMsg.purchaseOrderDesde,
			            id: 'delPoFromDate',
			            itemId: 'delPoFromDate',
			            name:'delPoFromDate',
			            width:160,
			            labelWidth:40,
			            margin:'0 20 0 10'
					},{
						xtype: 'datefield',
			            fieldLabel:SuppAppMsg.purchaseOrderHasta,
			            id: 'delPoToDate',
			            itemId: 'delPoToDate',
			            name:'delPoToDate',
			            width:160,
			            labelWidth:40,
			            margin:'0 40 0 10'
					},{
		           		xtype:'button',
			            text: SuppAppMsg.suppliersSearch,
			            iconCls: 'icon-doSearch',
			            action:'delPoSearch',
			            cls: 'buttonStyle',
			            margin:'2 30 0 10',
			        	listeners: {
			        		tap: function (button) {
			        			poController.delPoSearch(button);
			        		}
			        	}
					}
                ]
            },
            {
                xtype: 'toolbar',
                dock: 'top',
                style: {
                    background: 'white'
                  },
                items: [
                	{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseTitle13,
			            iconCls: 'icon-accept',
			            action:'delPoConfirm',
			            cls: 'buttonStyle',
			            margin:'2 30 5 10',
			        	listeners: {
			        		tap: function (button) {
			        			poController.delPoConfirm(button);
			        		}
			        	}
					}
                ]
            },
		    getPagingContent()
        ];
              
        this.callParent(arguments);
    }
});