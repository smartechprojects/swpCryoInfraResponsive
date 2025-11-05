Ext.define('SupplierApp.view.customBroker.CustomBrokerGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.customBrokerGrid',
    loadMask: true,
	frame:false,
	border:false,
    forceFit: true,
	cls: 'extra-large-cell-grid',  
	store : {
		type:'custombroker'
	},
    dockedItems: [
    	getPagingContent()
    ],
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
    	
    	var docType = null;
    	var invStatus = null;
    	
    	docType = Ext.create('Ext.data.Store', {
        	    fields: ['id', 'name'],
        	    data : [
        	        {"id":"FACTURA", "name":"Factura"},
        	        {"id":"FACT EXTRANJERO", "name":"Factura Extranjeros"}//,{"id":"NOTACREDITO", "name":"Nota de Credito"}
        	    ]
        	});

    	invStatus = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'name'],
    	    data : [    	        
    	        {"id":"PENDIENTE", "name":"PENDIENTE"},
    	        {"id":"APROBADO", "name":"APROBADO"},
    	        {"id":"RECHAZADO", "name":"RECHAZADO"},
    	        {"id":"PAGADO", "name":"PAGADO"},
    	        {"id":"COMPLEMENTO", "name":"COMPLEMENTO"}
    	    ]
    	});
    	
    	Ext.define('docTypeCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.fiscalTitle21,
    	    store: docType,
    	    alias: 'widget.combodoctype',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    editable: false,
    	    displayField: 'name',
			flex:.2,
    	    labelWidth:90,
    	    valueField: 'id',
    	    id:'comboDocumentTypeCB'
    	});
    	
    	Ext.define('statusCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.fiscalTitle22,
    	    store: invStatus,
    	    alias: 'widget.combostatus',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    editable: false,
    	    displayField: 'name',
			width:150,
    	    labelWidth:40,
    	    valueField: 'id',
    	    margin:'20 20 0 10',
    	    id:'comboInvoiceStatusCB'
    	});
    	
        this.columns = [
			           {
			            text     : SuppAppMsg.suppliersNumber,
			            width: 90,
			            dataIndex: 'addressNumber'
			        },{
			            text     : SuppAppMsg.suppliersNameSupplier,
			            width: 230,
			            dataIndex: 'supplierName'
			        },{
			            text     : SuppAppMsg.fiscalTitle30,
			            width: 90,
			            dataIndex: 'rfcReceptor'
			        },{
			            text     : SuppAppMsg.fiscalTitle3,
			            width: 90,
			            dataIndex: 'folio'
			        },{
			            text     : SuppAppMsg.fiscalTitle4,
			            width: 80,
			            dataIndex: 'serie'
			        } ,{
			            text     : SuppAppMsg.fiscalTitle26,
			            width: 90,
			            dataIndex: 'invoiceUploadDate',
			            renderer : Ext.util.Format.dateRenderer("d-m-Y")
			        } ,{
			            text     : SuppAppMsg.fiscalTitle29,
			            width: 90,
			            dataIndex: 'paymentDate',
						renderer: function(value, metaData, record, row, col, store, gridView){
							if(value) {
								return Ext.util.Format.date(new Date(value), 'd-m-Y');
							} else {
								return null;
							}
						}
			        },{
			            text     : SuppAppMsg.fiscalTitle27,
			            width: 100,
			            dataIndex: 'amount',
						align: 'center',
			            renderer : Ext.util.Format.numberRenderer('0,0.00')
			        },{
			            text     : SuppAppMsg.fiscalTitle20,
			            width: 110,
						align: 'center',
			            dataIndex: 'conceptTotalAmount',
			            renderer : Ext.util.Format.numberRenderer('0,0.00')
			        },{
			            text     : SuppAppMsg.fiscalTitle28,
			            width: 100,
						align: 'center',
			            renderer : function(value, metadata, record, rowIndex, colIndex, store){
									return Ext.util.Format.number((record.data.amount + record.data.conceptTotalAmount),'0,0.00')
						}
			        },{
			            text     : SuppAppMsg.purchaseOrderCurrency,
			            width: 90,
			            dataIndex: 'moneda'
			        },{
			            text     : SuppAppMsg.fiscalTitle5,
			            width: 250,
			            dataIndex: 'uuidFactura'
			        },{
			            text     : SuppAppMsg.fiscalTitle22,
			            width: 110,
			            dataIndex: 'status'
			        },{
			            text     : SuppAppMsg.approvalLevel,
			            width: 120,
			            dataIndex: 'approvalStep'
			        },{
			            text     : SuppAppMsg.approvalCurrentApprover,
			            width: 200,
			            dataIndex: 'currentApprover'
			        },{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.approvalApprove,
			            align: 'center',
						name : 'approveInvoiceCB',
						hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
						itemId : 'approveInvoiceCB',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/accept.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              	          if(role == 'ROLE_SUPPLIER' || r.data.status != "PENDIENTE" || (r.data.status == "PENDIENTE" && r.data.currentApprover != userName)) {
			              	              return "x-hidden-display";
			              	          }
			              	      },
			              	      text: SuppAppMsg.approvalApprove,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        },{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.approvalReject,
			            align: 'center',
						name : 'rejectInvoiceCB',
						hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
						itemId : 'rejectInvoiceCB',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/close.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		if(role == 'ROLE_SUPPLIER' || r.data.status != "PENDIENTE" || (r.data.status == "PENDIENTE" && r.data.currentApprover != userName)) {
			              	              return "x-hidden-display";
			              	          }
			              	      },
			              	      text: SuppAppMsg.approvalReject,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        }];
        
        
        
        this.dockedItems = [
            {
              xtype: 'toolbar',
              dock: 'top',
              items: [{
      			iconCls : 'icon-save',
    			itemId : 'uploadNewFiscalDocCB',
    			id : 'uploadNewFiscalDocCB',
    			text : SuppAppMsg.fiscalTitle18,
    			hidden:true,
    			action : 'uploadNewFiscalDocCB'
    		}, '-', {
    			name : 'searchcustomBroker',
    			itemId : 'searchcustomBroker',
    			emptyText : SuppAppMsg.fiscalTitle19,
    			xtype : 'trigger',
    			width : 300,
    			margin: '5 0 10 0',
    			hidden:true,
    			triggerCls : 'x-form-search-trigger',
    			onTriggerClick : function(e) {
    				this.fireEvent("ontriggerclick", this, event);
    			},
    			enableKeyEvents : true,
    			listeners : {
    				specialkey : function(field, e) {
    					if (e.ENTER === e.getKey()) {
    						field.onTriggerClick();
    					}
    				}
    			}
    		},{ 
    			xtype: 'combodoctype'
    		}
              ]},
             {
                xtype: 'toolbar',
                dock: 'top',
                items: [
                	{
            			xtype: 'textfield',
                        fieldLabel: 'UUID',
                        id: 'cbUUID',
                        itemId: 'cbUUID',
                        name:'cbUUID',
                        width:300,
                        labelWidth:30,
                        margin:'20 20 0 10'
            		},{ 
            			xtype: 'combostatus'
            		},{
            			xtype: 'textfield',
                        fieldLabel: SuppAppMsg.suppliersNumber,
                        id: 'supNumberCB',
                        itemId: 'supNumberCB',
                        name:'supNumberCB',
                        value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
                        fieldStyle: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?'border:none;background-color: #ddd; background-image: none;':'',
                        readOnly: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?true:false,
                        width:200,
                        labelWidth:80,
                        margin:'20 20 0 10'
            		},{
                   		xtype:'button',
                        text: SuppAppMsg.suppliersSearch,
                        iconCls: 'icon-appgo',
                        action:'cbSearch',
                        cls: 'buttonStyle',
                        margin:'0 20 0 10'
            		},{
                   		xtype:'button',
                        text: SuppAppMsg.purchaseTitle58,
                        iconCls: 'icon-accept',
                        action:'poUploadInvoiceFileCB',
                        cls: 'buttonStyle',
                        margin:'0 20 0 10'
            		},{
            			xtype:'button',
            			text: SuppAppMsg.purchaseOrderCCPG,
            			iconCls: 'icon-accept',
            			action:'cbLoadCompl',
            			cls: 'buttonStyle',
            			margin:'0 20 0 10',
            			hidden:role == 'ROLE_WNS'?true:false
            		} 
              ]}
      ];

		
        this.callParent(arguments);
    }
});