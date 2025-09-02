Ext.define('SupplierApp.view.freightApproval.FreightApprovalGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.freightApprovalGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',  
	store : {
		type:'freightapproval'
	},
	forceFit: true,
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
    	var banderaAprobacion = false;
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
    	        {"id":"APROBADO", "name":"APROBADO"}
    	    ]
    	});
    	
    	ctaPPagar = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'name'],
    	    data : [    	        
    	        {"id":"DP", "name":"DP"},
    	        {"id":"DN", "name":"DN"}
    	    ]
    	});
    	
    	Ext.define('ctaPPagarCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.freightApprovalBillToPay,
    	    store: ctaPPagar,
    	    alias: 'widget.ctaPPagar',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    editable: false,
    	    displayField: 'name',
			width:200,
    	    labelWidth:120,
    	    valueField: 'id',
    	    margin:'20 20 0 10',
    	    id:'comboCtaPPagarFA',
    	   /* listeners: {
    	        afterrender: function() {
    	           if(role == 'ROLE_WNS'){
    	        	   this.setValue("STATUS_OC_PROCESSED");    
    	           }
    	        }
    	    }*/
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
			width:230,
    	    labelWidth:90,
    	    valueField: 'id',
    	    margin:'20 20 0 10',
    	    id:'comboDocumentTypeFA',    	    
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
    	    id:'comboInvoiceStatusFA',
    	   /* listeners: {
    	        afterrender: function() {
    	           if(role == 'ROLE_WNS'){
    	        	   this.setValue("STATUS_OC_PROCESSED");    
    	           }
    	        }
    	    }*/
    	});
    	
        this.columns = [
        	
		        	{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.freightApprovalCover,
			            align: 'center',
						name : 'freightApprovalCover',
						hidden: false,
						itemId : 'freightApprovalCover',
			            style: 'text-align:center;',
			           /* items: [
			            	{
			            	icon:'resources/images/archivo-pdf.png',
			              	      text: SuppAppMsg.freightApprovalCover,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]*/
			            items: [
			            	{
			            	icon:'resources/images/archivo-pdf.png',
		             	      text: SuppAppMsg.freightApprovalTitle8,
			                  handler: function(grid, rowIndex, colIndex) {
			                	 
			                	  var record = grid.store.getAt(rowIndex);
			                	
			                	  //var href = "documents/openDocument.action?id=" + 46;
									var href = "freight/openCoverBatchReport.action?batchID=" + record.data.id;
									var newWindow = window.open(href, '_blank');
									//var newWindow = window.open(newdata, "_blank");
									//setTimeout(function(){ newWindow.document.title = 'Caratula Batch'; }, 10);
									setTimeout(function(){ newWindow.document.title = 'Caratula PDF'; }, 10);
			                  //this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        }, 	{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.freightApprovalReportBatch,
			            align: 'center',
						name : 'freightApprovalReportBatch',
						hidden:role == 'ROLE_SUPPLIER',
						itemId : 'freightApprovalReportBatch',
			            style: 'text-align:center;',
			           /* items: [
			            	{
			            	icon:'resources/images/archivo-pdf.png',
			              	      text: SuppAppMsg.freightApprovalCover,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]*/
			            items: [
			            	{
			            		icon:'resources/images/archivo-pdf.png',
			             	      text: SuppAppMsg.freightApprovalReportBatch,
				                  handler: function(grid, rowIndex, colIndex) {
				                	 
				                	  var record = grid.store.getAt(rowIndex);
				                	
				                	  //var href = "documents/openDocument.action?id=" + 46;
										var href = "freight/openCoverBatchReportBatch.action?batchID=" + record.data.id;
										var newWindow = window.open(href, '_blank');
										//var newWindow = window.open(newdata, "_blank");
										//setTimeout(function(){ newWindow.document.title = 'Caratula Batch'; }, 10);
										setTimeout(function(){ newWindow.document.title = 'Batch PDF'; }, 10);
				                  //this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        },{
			            text     : SuppAppMsg.freightApprovalKey,
			            width: 100,
			            dataIndex: 'id',
			            hidden:role == 'ROLE_SUPPLIER'
			        },{
			            text     : SuppAppMsg.freightApprovalAmount,
			            width: 90,
			            dataIndex: 'amount',
			            renderer: function(value) {
			            	// formateo de dos ceimales	
			            	var formattedValue = Ext.util.Format.number(value, '0,000.00');
			                return formattedValue;
			            },
			            align: 'right'
			        },{
			            text     : SuppAppMsg.freightApprovalBillToPay,
			            width: 100,
			            dataIndex: 'serie'
			        },{
			            text     : SuppAppMsg.freightApprovalBudgetAccount,
			            width: 150,
			            dataIndex: 'accountingAccount',
			            hidden:role == 'ROLE_SUPPLIER'	
			        },{
			            text     : SuppAppMsg.fiscalTitle22,
			            width: 110,
			            dataIndex: 'status'
			        },{
			            text     : SuppAppMsg.approvalLevel,
			            width: 80,
			            dataIndex: 'approvalStep'
			        },
			        {
			            text     : SuppAppMsg.freightApprovalTitle10,
			            width: 230,
			            dataIndex: 'semanaPago'
			        },
			        {
			            text     : SuppAppMsg.approvalCurrentApprover,
			            width: 150,
			            dataIndex: 'currentApprover'
			        },{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.approvalApprove,
			            align: 'center',
						name : 'approveInvoiceFDA',
						hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'|| role=='ROLE_MANAGER'?false:true,
						itemId : 'approveInvoiceFDA',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/accept.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		  debugger
			              	          if(role == 'ROLE_SUPPLIER' || r.data.status != "PENDIENTE" || (r.data.status == "PENDIENTE" && !r.data.currentApprover.includes(userName))) {
			              	              return "x-hide-display";
			              	          }else if (r.data.currentApprover.includes(userName)){
			              	        	banderaAprobacion=true;
			              	          }
			              	      },
			              	      text: SuppAppMsg.approvalApprove,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        },{
			        	xtype: 'actioncolumn',
			        	//hidden:role=='ROLE_ADMIN'?false:true,
			            width: 90,
			            header: SuppAppMsg.reasignRequest,
			            align: 'center',
						name : 'reasignRequest',
						itemId : 'reasignRequest',
			            style: 'text-align:center;',
			            hidden:true,
			            items: [{
			                text: 'REASIGNAR',
			                iconCls:'user-group',
			                handler: function(grid, rowIndex, colIndex) {
			                	debugger
			                	var record = grid.store.getAt(rowIndex);
			                	var me = this;
			                    var usrstore = searchByRoleAprover('APPROVALFREIGHT',record.data.approvalStep);
			                	var formPanel =  {
			                		        xtype       : 'form',
			                		        height      : 125,
			                		        items       : [
			                		        	{
													fieldLabel : SuppAppMsg.newApprover,
													xtype: 'combobox',
													id:'newApproverId',
													store : usrstore,
									                displayField: 'name',
									                valueField: 'userName',
									                labelWidth:100,
									                width : 350,
									                margin: '10 10 0 10'
												},{
													xtype: 'button',
													width:100,
													text : SuppAppMsg.reasignRequest,
													margin: '12 10 0 10',
													cls: 'buttonStyle',
													iconCls : 'icon-appgo',
													listeners: {
													    click: function() {
													    	debugger
													    	if(Ext.getCmp('newApproverId').getValue() != null && Ext.getCmp('newApproverId').getValue() != ""){
													    		var box = Ext.MessageBox.wait(
										    							SuppAppMsg.supplierProcessRequest,
										    							SuppAppMsg.approvalExecution);
												    			Ext.Ajax.request({
																    url: 'approval/reasignApproverFletes.action',
																    method: 'POST',
																    params: {
																    	id:record.data.id,
																    	newApprover: Ext.getCmp('newApproverId').getValue(),
																    	etapa:record.data.approvalStep
															        },
																    success: function(fp, o) {
																    	var res = Ext.decode(fp.responseText); 
																    	box.hide();
																    	if(res.message == "success"){
																    		grid.store.load();
																    		me.reasignWindow.close();
																    		Ext.Msg.alert(SuppAppMsg.approvalResponse, SuppAppMsg.newApproverSuccess);
																    	}
															        	
																    },
																    failure: function() {
																    	box.hide();
																    	Ext.MessageBox.show({
															                title: 'Error',
															                msg: 'Surgio un error al actualizar',
															                buttons: Ext.Msg.OK
															            });
																    }
																});
													    	}
													    	
													    }
												    }
												}
			                		        ]
			                		    };
			                	
			                	
			                	me.reasignWindow = new Ext.Window({
			                		layout : 'fit',
			                		title : SuppAppMsg.reasignapproverRequest + record.data.ticketId ,
			                		width : 400,
			                		height : 120,
			                		modal : true,
			                		closeAction : 'destroy',
			                		resizable : true,
			                		minimizable : false,
			                		maximizable : false,
			                		autoScroll: true,
			                		plain : true,
			                		items : [formPanel]
			                	});
			                	
			                	me.reasignWindow.show();
			                }
			            }]
			        }
			        
			        /*,{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.approvalReject,
			            align: 'center',
						name : 'rejectInvoiceFDA',
						hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
						itemId : 'rejectInvoiceFDA',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/close.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		if(role == 'ROLE_SUPPLIER' || r.data.status != "PENDIENTE" || (r.data.status == "PENDIENTE" && r.data.currentApprover != userName)) {
			              	              return "x-hide-display";
			              	          }
			              	      },
			              	      text: SuppAppMsg.approvalReject,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        }*/];
        
        
        
        
        this.dockedItems = [
            {
              xtype: 'toolbar',
              dock: 'top',
              items: [
            	  {
          			iconCls : 'icon-save',
          			itemId : 'uploadNewFiscalDocFA',
          			id : 'uploadNewFiscalDocFA',
          			text : SuppAppMsg.fiscalTitle18,
          			hidden:true,
          			action : 'uploadNewFiscalDoc'
          		}, '-', {
          			name : 'searchFiscalDocumentsFA',
          			itemId : 'searchFiscalDocumentsFA',
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
          		}
              ]},
             {
                xtype: 'toolbar',
                dock: 'top',
                items: [
                	{
            			xtype: 'textfield',
                        fieldLabel: SuppAppMsg.freightApprovalTitle10,
                        id: 'semanaPagoFA',
                        itemId: 'semanaPagoFA',
                        name:'semanaPagoFA',
                        width:300,
                        labelWidth:120,
                        margin:'20 20 0 10'
            		},{
            			xtype: 'textfield',
                        fieldLabel: SuppAppMsg.freightApprovalBudgetAccount,
                        id: 'ctaPresupuestalFA',
                        itemId: 'ctaPresupuestalFA',
                        name:'ctaPresupuestalFA',
                        //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
                        //fieldStyle: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?'border:none;background-color: #ddd; background-image: none;':'',
                        //readOnly: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?true:false,
                        hidden:role == 'ROLE_SUPPLIER',
                        width:300,
                        labelWidth:120,
                        margin:'20 20 0 10'
            		},{
            			xtype: 'textfield',
                        fieldLabel: SuppAppMsg.freightApprovalTitle6,
                        id: 'batchIdParam',
                        itemId: 'batchIdParam',
                        name:'batchIdParam',
                        hidden:role == 'ROLE_SUPPLIER',
                        //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
                        //fieldStyle: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?'border:none;background-color: #ddd; background-image: none;':'',
                        //readOnly: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?true:false,
                        width:300,
                        labelWidth:120,
                        margin:'20 20 0 10'
            		},{ 
            			xtype: 'combostatus',
            			value: role == 'ROLE_SUPPLIER'?'APROBADO':'',
            			readOnly:  role == 'ROLE_SUPPLIER'		
            		
            		},{
                   		xtype:'button',
                        text: SuppAppMsg.suppliersSearch,
                        iconCls: 'icon-appgo',
                        action:'fdSearch',
                        cls: 'buttonStyle',
                        margin:'0 20 0 10'
            		}
                	 
              ]}
      ];

 
        this.callParent(arguments);
        if(banderaAprobacion){
        	 Ext.getCmp('approveInvoiceFDA').hidden=false;
        }
    }
});