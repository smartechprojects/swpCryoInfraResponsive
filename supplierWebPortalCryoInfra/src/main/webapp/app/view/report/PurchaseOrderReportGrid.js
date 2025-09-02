	Ext.define('SupplierApp.view.purchaseOrder.PurchaseOrderReportGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.purchaseOrderReportGrid',
    forceFit: true,
    loadMask: true,
	frame:false,
	border:false,
	selModel: {
        checkOnly: true,
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
	 
    	var status = null;
    	
    	if(role == 'ROLE_SUPPLIER'){
        	status = Ext.create('Ext.data.Store', {
        	    fields: ['id', 'name'],
        	    data : [
        	        {"id":"STATUS_OC_SENT", "name":"Orden Liberada"},
        	        {"id":"STATUS_OC_APPROVED", "name":"Orden Aprobada"},
        	        {"id":"STATUS_OC_INVOICED", "name":"Orden Facturada"},
        	        {"id":"STATUS_OC_PROCESSED", "name":"Factura Aceptada"},
        	        {"id":"STATUS_OC_PAID", "name":"Factura Pagada"},
        	        {"id":"STATUS_OC_PAYMENT_COMPL", "name":"Complemento Cargado"},
        	        {"id":"STATUS_OC_CANCEL", "name":"OC Cancelada"}
        	    ]
        	});
    	}else{
    		status = Ext.create('Ext.data.Store', {
        	    fields: ['id', 'name'],
        	    data : [
        	        {"id":"STATUS_OC_RECEIVED", "name":"Orden Recibida"},
        	        {"id":"STATUS_OC_SENT", "name":"Orden Liberada"},
        	        {"id":"STATUS_OC_APPROVED", "name":"Orden Aprobada"},
        	        {"id":"STATUS_OC_INVOICED", "name":"Orden Facturada"},
        	        {"id":"STATUS_OC_PROCESSED", "name":"Factura Aceptada"},
        	        {"id":"STATUS_OC_PAID", "name":"Factura Pagada"},
        	        {"id":"STATUS_OC_PAYMENT_COMPL", "name":"Complemento Cargado"},
        	        {"id":"STATUS_OC_CANCEL", "name":"OC Cancelada"}
        	    ]
        	});
    	}

    	
    	Ext.define('statusCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: 'Status',
    	    store: status,
    	    alias: 'widget.combostatus',
    	    queryMode: 'local',
    	    displayField: 'name',
    	    labelWidth:40,
    	    valueField: 'id',
    	    margin:'20 20 0 0',
    	    id:'combostatus',
    	    width:250
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
					status:'OC ENVIADA',
					userName:userName
                },
			    "", 
			    10);
 
        this.columns = [
           {
            text     : SuppAppMsg.purchaseOrderNumber,
            width: 80,
            dataIndex: 'orderNumber'
        },{
            text     : SuppAppMsg.purchaseOrderType,
            width: 40,
            dataIndex: 'orderType'
        },{
            text     : SuppAppMsg.purchaseOrderSupplier,
            width: 80,
            dataIndex: 'addressNumber'
        },{
        	text     : SuppAppMsg.suppliersName,
            width: 180,
            dataIndex: 'description'
        },{
            text     : SuppAppMsg.purchaseOrderFechaAprovacion,
            width: 120,
            dataIndex: 'promiseDelivery',
            renderer : Ext.util.Format.dateRenderer("d-m-Y")
        },{
            text     : 'Fecha estimada de recibo',
            width: 150,
            dataIndex: 'promiseDelivery',
            renderer : Ext.util.Format.dateRenderer("d-m-Y"),
            hidden:true
        },{
        	text     : 'Email Comprador',
            width: 180,
            dataIndex: 'email'
        },{
        	text     : 'Compañía',
            width: 160,
            dataIndex: 'shortCompanyName'
        },{
            text     : SuppAppMsg.purchaseOrderÌmporteTotal,
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
            text     : 'Status',
            width: 110,
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
            text     : 'Status de Factura',
            width: 100,
            dataIndex: 'status',
            hidden:true
        },{
            text     : SuppAppMsg.fiscalTitle3,
            width: 80,
            dataIndex: 'invoiceNumber'
        },{
            align: 'center',
            width: 140,
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
	            	        STATUS_OC_CANCEL: 'OC CANCELADA'
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
	              	  default:
	              		break;
	            	}
	
	            	if(role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN' || role == 'ROLE_MANAGER'){
		            	if(showButton){
			        		 Ext.defer(function(){
			                     new Ext.Button({
			             			 name : 'uploadPayment',
			            			 itemId : 'uploadPayment',
			            			 iconCls:'icon-add',
			                         text: 'Cargar archivo',
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
			            fieldLabel:SuppAppMsg.purchaseOrderNumber,
			            id: 'poNumber',
			            itemId: 'poNumber',
			            name:'poNumber',
			            width:200,
			            labelWidth:70,
			            margin:'0 20 0 10'
					},{
						xtype: 'textfield',
			            fieldLabel: SuppAppMsg.suppliersNumber,
			            id: 'supNumber',
			            itemId: 'supNumber',
			            name:'supNumber',
			            value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
			            fieldStyle: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?'border:none;background-color: #ddd; background-image: none;':'',
			            readOnly: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?true:false,
			            width:200,
			            labelWidth:90,
			            margin:'20 20 0 10'
					},{
						xtype: 'datefield',
			            fieldLabel:SuppAppMsg.purchaseOrderDesde,
			            id: 'poFromDate',
			            itemId: 'poFromDate',
			            name:'poFromDate',
			            width:160,
			            labelWidth:35,
			            margin:'0 20 0 10'
					},{
						xtype: 'datefield',
			            fieldLabel:SuppAppMsg.purchaseOrderHasta,
			            id: 'poToDate',
			            itemId: 'poToDate',
			            name:'poToDate',
			            width:160,
			            labelWidth:35,
			            margin:'0 40 0 10'
					},{ 
						xtype: 'combostatus'
					},{
		           		xtype:'button',
			            text: SuppAppMsg.suppliersSearch,
			            iconCls: 'icon-appgo',
			            action:'poSearch',
			            cls: 'buttonStyle',
			            margin:'0 20 0 10'
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
			            text: SuppAppMsg.receiptTitle6,
			            iconCls: 'icon-accept',
			            action:'poInvAccept',
			            cls: 'buttonStyle',
			            hidden:role == 'ROLE_SUPPLIER'?true:false,
			            margin:'2 20 5 10'
					},{
		           		xtype:'button',
			            text: SuppAppMsg.receiptTitle7,
			            iconCls: 'icon-delete',
			            action:'poInvReject',
			            hidden:role == 'ROLE_SUPPLIER'?true:false,
			            cls: 'buttonStyle',
			            margin:'2 20 5 10'
					},
					{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseOrderCCPG,
			            iconCls: 'icon-accept',
			            action:'poLoadCompl',
			            cls: 'buttonStyle',
			            margin:'2 20 5 10'
					},
					{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseOrderRO,
			            iconCls: 'icon-accept',
			            action:'poReasignPurchases',
			            hidden:role == 'ROLE_ADMIN' || role == 'ROLE_MANAGER' ?false:true,
			            cls: 'buttonStyle',
			            margin:'2 20 5 10'
					},
					{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseOrderCP,
			            iconCls: 'icon-accept',
			            action:'poPaymentCalendar',
			            hidden:role == 'ROLE_ADMIN' || role == 'ROLE_MANAGER' ?false:true,
			            cls: 'buttonStyle',
			            margin:'2 20 5 10'
					},
					{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseOrderFTP,
			            iconCls: 'icon-accept',
			            action:'poLoadFTPInv',
			            hidden:role == 'ROLE_ADMIN' || role == 'ROLE_MANAGER' ?false:true,
			            cls: 'buttonStyle',
			            margin:'2 20 5 10'
					},'->',
					{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseOrderIP,
			            iconCls: 'icon-accept',
			            action:'poLoadPayment',
			            hidden:role == 'ROLE_ADMIN'?false:true,
			            cls: 'buttonStyle',
			            margin:'2 20 5 10'
					},
					{
		           		xtype:'button',
			            text: SuppAppMsg.purchaseOrderIOC,
			            iconCls: 'icon-accept',
			            action:'poLoadPurchases',
			            hidden:role == 'ROLE_ADMIN'?false:true,
			            cls: 'buttonStyle',
			            margin:'2 20 5 10'
					}
                ]
            }
        ];
        

        
		this.bbar = Ext.create('Ext.PagingToolbar', {
			store: this.store,
			displayInfo : true,
			beforePageText : SuppAppMsg.page,
			afterPageText :SuppAppMsg.de + ' {0}',
			emptyMsg  : SuppAppMsg.emptyMsg ,
			displayMsg :SuppAppMsg.displayMsg + ' {0} - {1} '+ SuppAppMsg.de +' {2}'
		});
      
        this.callParent(arguments);
    }
});