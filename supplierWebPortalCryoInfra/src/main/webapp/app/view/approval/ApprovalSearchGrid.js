Ext.define('SupplierApp.view.approval.ApprovalSearchGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.approvalSearchGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid', 
	scroll : false,
    dockedItems: [
    	getPagingContent()
    ],
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
    	
    	var apController = SupplierApp.app.getController("SupplierApp.controller.Approval");
    	
    	Ext.define('TStatus', {
    	    extend: 'Ext.data.Model',
    	    fields: [
    	        {type: 'string', name: 'status'},
    	        {type: 'string', name: 'description'}
    	    ]
    	});
    	
    	var ticketStatus = [
    		{"status":"","description":"ALL"},
    	    {"status":"PENDIENTE","description":"PENDIENTE"},
    	    {"status":"RECHAZADO","description":"RECHAZADO"},
    	    {"status":"RENEW","description":"ACTUALIZADO"},
    	    {"status":"DRAFT","description":"DRAFT"}

    	];
    	
    	var ticketSatusStore = Ext.create('Ext.data.Store', {
            autoDestroy: true,
            model: 'TStatus',
            data: ticketStatus
        });
    	
    	
    	Ext.define('AppLevel', {
    	    extend: 'Ext.data.Model',
    	    fields: [
    	        {type: 'string', name: 'status'},
    	        {type: 'string', name: 'description'}
    	    ]
    	});
    	
    	var appLelvel = [
    		{"status":"","description":"ALL"},
    	    {"status":"FIRST","description":"FIRST"},
    	    {"status":"SECOND","description":"SECOND"},
    	    {"status":"THIRD","description":"THIRD"}

    	];
    	
    	var apprLevelStore = Ext.create('Ext.data.Store', {
            autoDestroy: true,
            model: 'AppLevel',
            data: appLelvel
        });
    	
		this.store =  storeAndModelFactory('SupplierApp.model.SupplierDTO',
                'approvalSearchModel',
                'approval/search.action', 
                false,
                {
					  ticketId:'',
					  approvalStep:'',
					  approvalStatus:'',
					  fechaAprobacion:null,
					  currentApprover:'',
					  name:''
                },
			    "", 
			    100);

		var gridStore = this.store;
		
        this.columns = [
        	{   
        	   text: SuppAppMsg.approvalTicket,
               width: 100,
               dataIndex: 'ticketId'
           },{   
        	   text: SuppAppMsg.suppliersNumber ,
               width: 100,
               dataIndex: 'addresNumber'
           },{
        	   text     : SuppAppMsg.suppliersNameSupplier,
        	   width: 300,
        	   dataIndex: 'razonSocial'
	       },{
	            text     : SuppAppMsg.approvalLevel,
	            width: 120,
	            dataIndex: 'approvalStep'
	       },{
	            text     : SuppAppMsg.approvalCurrentApprover,
	            width: 120,
	            dataIndex: 'currentApprover'
	       },{
	            text     : SuppAppMsg.approvalNextApprover,
	            width: 120,
	            dataIndex: 'nextApprover'
	       },{
	            text     : 'Status',
	            width: 90,
	            dataIndex: 'approvalStatus'
	       },{
	            text     : SuppAppMsg.approvalRequestDate,
	            width: 90,
	            dataIndex: 'fechaSolicitud',
	            renderer : Ext.util.Format.dateRenderer("d-m-Y")
	       },{
	            text     : SuppAppMsg.purchaseTitle30,
	            width: 300,
	            dataIndex: 'approvalNotes'
	       },{
	        	xtype: 'actioncolumn',
	        	hidden:true,
	        	//hidden:role=='ROLE_ADMIN'?false:true,
	            width: 90,
	            header: SuppAppMsg.reasignRequest,
	            align: 'center',
				name : 'reasignRequest',
				itemId : 'reasignRequest',
	            style: 'text-align:center;',
	            items: [{
	                text: 'REASIGNAR',
	                iconCls:'user-group',
	                handler: function(grid, rowIndex, colIndex) {
	                	var record = grid.store.getAt(rowIndex);
	                	var me = this;
	                    var usrstore = getUsersByRoleExcludeStore('ROLE_SUPPLIER');
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
											    	if(Ext.getCmp('newApproverId').getValue() != null && Ext.getCmp('newApproverId').getValue() != ""){
											    		var box = Ext.MessageBox.wait(
								    							SuppAppMsg.supplierProcessRequest,
								    							SuppAppMsg.approvalExecution);
										    			Ext.Ajax.request({
														    url: 'approval/reasignApprover.action',
														    method: 'POST',
														    params: {
														    	id:record.data.id,
														    	newApprover: Ext.getCmp('newApproverId').getValue()
													        },
														    success: function(fp, o) {
														    	var res = Ext.decode(fp.responseText); 
														    	box.hide();
														    	if(res.message == "success"){
														    		gridStore.load();
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
	        }];
        
        this.dockedItems = [
            {
              xtype: 'toolbar',
              dock: 'top',
              items: [{
      			xtype: 'textfield',
                fieldLabel: SuppAppMsg.suppliersName,
                id: 'supSearchName',
                itemId: 'supSearchName',
                name:'supSearchName',
                width:200,
                labelWidth:70,
                labelAlign:'top',
                margin:'10 20 10 5',
                listeners:{
    				change: function(field, newValue, oldValue){
    					field.setValue(newValue.toUpperCase());
    				}
    			}
    		},{
    			xtype: 'textfield',
                fieldLabel: SuppAppMsg.approvalTicket,
                id: 'supSearchTicket',
                itemId: 'supSearchTicket',
                name:'supSearchTicket',
                width:120,
                labelWidth:70,
                labelAlign:'top',
                margin:'0 20 10 10',
                listeners:{
    				change: function(field, newValue, oldValue){
    					field.setValue(newValue.toUpperCase());
    				}
    			}
    		},{
    			fieldLabel : SuppAppMsg.purchaseOrderStatus,
    			name : 'supSearchTicketSts',
    			id : 'supSearchTicketSts',
    			xtype: 'combobox',
                queryMode: 'local',
    			store : ticketSatusStore,
                displayField: 'description',
                valueField: 'status',
                width : 150,
                labelAlign:'top'
    		},{
    			fieldLabel : SuppAppMsg.approvalLevel,
    			name : 'supSearchApprLevel',
    			id : 'supSearchApprLevel',
    			xtype: 'combobox',
                queryMode: 'local',
    			store : apprLevelStore,
                displayField: 'description',
                valueField: 'status',
                width : 150,
                labelAlign:'top',
                margin:'0 20 10 20',
    		},{
    			xtype: 'textfield',
                fieldLabel: SuppAppMsg.approvalCurrentApprover,
                id: 'supSearchApprover',
                itemId: 'supSearchApprover',
                name:'supSearchApprover',
                width:120,
                labelWidth:70,
                labelAlign:'top',
                margin:'0 20 10 10'
    		}
              ]},
             {
                xtype: 'toolbar',
                dock: 'top',
                items: [{
               		xtype:'button',
               		text: SuppAppMsg.suppliersSearch,
                    action:'searchAppSupplier',
                    iconCls: 'icon-doSearch',
                    cls: 'buttonStyle',
                    listeners: {
	                    tap: function (button) {
	                    	apController.searchAppSupplier(button);
	                    }
	                },
                    margin:'0 0 10 0'
                  }
              ]}
      ];

      
        this.callParent(arguments);
    }
});