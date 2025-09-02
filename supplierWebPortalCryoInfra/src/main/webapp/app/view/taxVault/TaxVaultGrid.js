Ext.define('SupplierApp.view.taxVault.TaxVaultGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.taxVaultGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',  
    store:'TaxVault',
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
    	
    	docType = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'name_es','name_en'],
    	    data : [
    	        {"id":"FACTURA", "name_es":"Factura","name_en":"Invoice"},
    	        {"id":"COMPLEMENTO", "name_es":"Complemento Pago","name_en":"Payment Plugin"},
    	        {"id":"NOTA_CREDITO", "name_es":"Nota Crédito","name_en":"Credit Note"},
    	        {"id":"NOTA_CARGO", "name_es":"Nota Cargo","name_en":"Charge Note"}    	        
    	    ]
    	});
    	
    	Ext.define('statusCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.fiscalTitle21,
    	    store: docType,
    	    editable:false,
    	    alias: 'widget.comboType',
    	    queryMode: 'local',
    	    //displayField: 'name',
    	    displayField: window.navigator.language.startsWith("es", 0)? 'name_es':'name_en',
    	    labelWidth:60,
    	    valueField: 'id',
    	    margin:'20 10 0 0',
    	    id:'comboType',
    	    width:200
    	});
    	
    	//var docType = null;
    	var invStatus = null;
    	
    	//if(role!=('ROLE_FISCAL_PRD')){
        this.columns = [
					{
						text : 'No.',
						width : 100,
						hidden:true,
						dataIndex : 'id' 
					},
					{
			            text     : 'UUID',
			            width: 250,
			            dataIndex: 'uuid'
			        },
		        	{
			            text     : SuppAppMsg.taxvaultIssuerRFC,
			            width: 150,
			            dataIndex: 'rfcEmisor'
			        },{
			            text     : SuppAppMsg.taxvaultreceiverRFC,
			            width: 150,
			            dataIndex: 'rfcReceptor'
			        },{
			            text     : SuppAppMsg.fiscalTitle21,
			            width: 200,
			            dataIndex: 'type'
			        },{
			            text     : window.navigator.language.startsWith("es", 0)? 'Fecha de Carga':'Upload date',
			            width: 200,
			            dataIndex: 'origen'
			        },{
			            text     : SuppAppMsg.taxvaulUser,
			            width: 100,
			            dataIndex: 'usuario' 
					},{
						text : SuppAppMsg.usersSupplier,
						width : 100,
						dataIndex : 'addressNumber'
			        },{
			            text     : SuppAppMsg.taxvaulStatus,
			            width: 150,
			            dataIndex: 'documentStatus'
			        }/*,{
			            text     : window.navigator.language.startsWith("es", 0)? 'Fecha de carga':'Upload date',
			            width: 150,
			            dataIndex: 'origen'
			        }*/, 	{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.taxvaulRequest,
			            align: 'center',
						name : 'taxVaultReportBatch',
						hidden: false,
						itemId : 'taxVaultReportBatch',
			            style: 'text-align:center;',
			            items: [
			            	{
			            		icon:'resources/images/archivo-pdf.png',
			            		getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			            			if(r.data.documentStatus != "COMPLETED" ){
				              			return "x-hide-display";
				              		}
				              	          if(!(role=='ROLE_ADMIN'||role=='ROLE_TAX'||role=='ROLE_TREASURY'||role=='ROLE_ACCOUNTING'||role=='ROLE_MANAGER'||role=='ROLE_BF_ADMIN'||r.data.usuario==userName)) {
				              	        	  
				              	              return "x-hide-display";
				              	          }
				              	      },
			             	      text: SuppAppMsg.freightApprovalReportBatch,
				                  handler: function(grid, rowIndex, colIndex) {
				                	 
				                	  var record = grid.store.getAt(rowIndex);
										var href = "taxVault/taxVaultPDF.action?id=" + record.data.id;
										var newWindow = window.open(href, '_blank');
										setTimeout(function(){ newWindow.document.title = 'Factura PDF'; }, 10);
			                  }}]
			        },{
			        	xtype: 'actioncolumn', 
			            width: 150,
			            header: SuppAppMsg.taxvaultUploandDocuments,
			            align: 'center',
						name : 'upFileExtTaxVault',
						itemId : 'upFileExtTaxVault',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/add.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		if(r.data.documentStatus != "COMPLETED" ){
			              			return "x-hide-display";
			              		}
			              		  
			              	          if(
              	        		  !(role=='ROLE_ADMIN'||role=='ROLE_BF_ADMIN'||r.data.usuario==userName)) {
              	        	  
              	              return "x-hide-display";
              	          }
			              	      },
			              	      text: SuppAppMsg.approvalApprove,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  
			                  }}]
			        },{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.taxvaultResendMail,
			            align: 'center',
						name : 'reenvioMailTaxDocument',
						itemId : 'reenvioMailTaxDocument',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/accept.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		if(r.data.documentStatus != "COMPLETED" ){
			              			return "x-hide-display";
			              		}
			              	          if(
              	        		  !(role=='ROLE_ADMIN'||role=='ROLE_TAX'||role=='ROLE_TREASURY'||role=='ROLE_ACCOUNTING'||role=='ROLE_MANAGER'||role=='ROLE_BF_ADMIN')) {
              	        	  
              	              return "x-hide-display";
              	          }
			              	      },
			              	      text: SuppAppMsg.approvalApprove,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        },{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.taxvaultDelete,
			            align: 'center',
						name : 'eliminarTaxVaultDocument',
						hidden: role=='ROLE_AUDIT_USR' ? true:false,
						itemId : 'eliminarTaxVaultDocument',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/close.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		if(r.data.documentStatus != "COMPLETED" ){
			              			return "x-hide-display";
			              		}
			              		   if(
              	        		  !(role=='ROLE_ADMIN'||role=='ROLE_BF_ADMIN')) {
                      	        	  
                      	              return "x-hide-display";
                      	          }
			              	      },
			              	      text: SuppAppMsg.approvalReject,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        }];
    	//}else{
    	//	this.columns = [];
    	//}
    	
    	this.dockedItems = [
    		{
                xtype: 'toolbar',
                height: 60,
               // maxHeight : 50,
                style: {
                    background: '#d3d3d3'
                  },
                dock: 'top',
                items: [
                	{
						xtype : 'combo',
						fieldLabel : SuppAppMsg.taxvaultreceiverRFC,
						id : 'companyBF',
						store : 'Company',
						valueField : 'company',
						displayField : 'company',
                        width:200,
                        labelWidth:70,
                        margin:'0 20 0 10',
                        editable:false,
                        hidden:role==('ROLE_SUPPLIER')
                	},{
            			xtype: 'textfield',
                        fieldLabel: SuppAppMsg.taxvaultIssuerRFC,
                        id: 'rfcEmisor',
                        itemId: 'rfcEmisor',
                        name:'rfcEmisor',
                        width:200,
                        labelWidth:70,
                        margin:'0 20 0 10',
                        hidden:role==('ROLE_FISCAL_PRD')
            		},{
            			xtype: 'textfield',
                        fieldLabel: 'UUID',
                        id: 'tvUUID',
                        itemId: 'tvUUID',
                        name:'tvUUID',
                        width:250,
                        labelWidth:50,
                        margin:'20 20 0 10',
                        //hidden:role==('ROLE_FISCAL_PRD')
            		},{
						xtype: 'comboType'
					},{
            			xtype: 'datefield',
                        fieldLabel: SuppAppMsg.purchaseOrderDesde,
                        id: 'tvFromDate',
                        itemId: 'tvFromDate',
                        name:'tvFromDate',
                        allowBlank:false,
                        //minValue: new Date(),
                        maxValue: new Date(), // Fecha máxima, hoy
                        value: Ext.Date.add(new Date(), Ext.Date.MONTH, -3), //Fecha inicial Desde 3 meses hacia atrás
                        width:160,
                        labelWidth:35,
                        margin:'0 20 0 10',
                        	listeners:{
            					change: function(field, newValue, oldValue){
            						Ext.getCmp("tvToDate").setMinValue(newValue);
            					}
            				},
            		},{
            			xtype: 'datefield',
                        fieldLabel: SuppAppMsg.purchaseOrderHasta,
                        id: 'tvToDate',
                        itemId: 'tvToDate',
                        name:'tvToDate',
                        //minValue: new Date(),
                        maxValue: new Date(), // Fecha máxima, hoy
                        value: new Date(), //Fecha inicial hoy
                        allowBlank:false,
                        width:160,
                        labelWidth:35,
                        margin:'0 40 0 10'
            		},{
                   		xtype:'button',
                        text: SuppAppMsg.suppliersSearch,
                        iconCls: 'icon-appgo',
                        action:'parSearch',
                        cls: 'buttonStyle',
                        margin:'0 20 0 10',
                        //hidden:role==('ROLE_FISCAL_PRD')
            		}
                ]
    		},
    		{
                xtype: 'toolbar',
                height: 25,
               // maxHeight : 50,
                style: {
                    background: '#d3d3d3'
                  },
                dock: 'top',
                items: [
                	{
            			iconCls : 'icon-add',
            			itemId : 'addTaxVaultRequest',
            			id : 'addTaxVaultRequest',
            			text : window.navigator.language.startsWith("es", 0)? 'Cargar documento':'Upload document', 
            			action : 'addNewTaxVaultRequest',
            			hidden:!(role==('ROLE_ADMIN')||role==('ROLE_FISCAL_USR')||role==('ROLE_SUPPLIER')||role==('ROLE_BF_ADMIN') ||role=='ROLE_TAX'||role=='ROLE_TREASURY'||role=='ROLE_ACCOUNTING'||role=='ROLE_MANAGER'||role=='ROLE_AUDIT_USR'),
            		}/*,{
            			iconCls : 'icon-add',
            			itemId : 'addTaxvaultNewcomplement',
            			id : 'addTaxvaultNewcomplement',
            			hidden:true,
            			text : SuppAppMsg.taxvaultNewcomplement,
            			action : 'addTaxvaultNewcomplement',
            			hidden:!(role==('ROLE_ADMIN')||role==('ROLE_FISCAL_USR')||role==('ROLE_SUPPLIER')||role==('ROLE_BF_ADMIN') ||role=='ROLE_TAX'||role=='ROLE_TREASURY'||role=='ROLE_ACCOUNTING'||role=='ROLE_MANAGER'||role=='ROLE_AUDIT_USR'),
            		}*/,{
            			iconCls : 'icon-add',
            			itemId : 'FiscalPeriodTaxVAult',
            			id : 'FiscalPeriodTaxVAult',
            			text :SuppAppMsg.taxvaultAddPeriod,
            			action : 'addFiscalPeriodTaxVAult',
            			hidden:!(role==('ROLE_ADMIN')||role==('ROLE_FISCAL_PRD')||role==('ROLE_BF_ADMIN')||role=='ROLE_TREASURY'),
            		},{
            			text: SuppAppMsg.taxvaultLeyenda01,
            			border: 0,
            			pressed : true,
            			text: '<div style="color: red">IMPORTANTE Si su factura es por OC u OS no validar por bóveda fiscal, realizar la carga directamente en Órdenes de Compra</div>'
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