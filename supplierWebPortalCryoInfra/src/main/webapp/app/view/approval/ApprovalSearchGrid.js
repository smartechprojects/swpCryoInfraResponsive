Ext.define('SupplierApp.view.approval.ApprovalSearchGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.approvalSearchGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid', 
	scroll :  true,
	viewConfig: {
	    stripeRows: true,
	    style : { overflow: 'auto', overflowX: 'hidden' },
	    enableTextSelection: true,
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
	                
	                // VALIDACIONES PARA APLICAR AJUSTE DE ALTURA:
	                // 1. Pantalla grande
	                var screenWidth = Ext.Element.getViewportWidth();
	                var screenHeight = Ext.Element.getViewportHeight();
	                var isLargeScreen = screenWidth >= 1000;
	                
	                // 2. Verificar si los registros de la página son iguales al pageSize
	                var store = grid.getStore();
	                var currentRecords = store.getCount();
	               // var pageSize = store.pageSize || 2; // Usar 2 como default si no existe
	                var pageSize = 2;
	                //var isFullPage = currentRecords === pageSize;
	                var isFullPage = currentRecords >= pageSize;
	                
	                // Aplicar ajuste de altura solo si ambas condiciones se cumplen
	                if (isLargeScreen && isFullPage) {
	                    // Ajuste de altura de filas
	                    var containerHeight = grid.getHeight();
	                    
	                    // Calcular altura de los headers
	                    var headerHeight = 0;
	                    var headerContainer = grid.headerCt;
	                    if (headerContainer && headerContainer.getHeight()) {
	                        headerHeight = headerContainer.getHeight();
	                    }
	                    
	                    // Calcular altura de los docked items (toolbars)
	                    var dockedHeight = 0;
	                    if (grid.dockedItems) {
	                        grid.dockedItems.each(function(item) {
	                            if (item.isVisible() && item.getHeight) {
	                                dockedHeight += item.getHeight();
	                            }
	                        });
	                    }
	                    
	                    var availableHeight = containerHeight - headerHeight - dockedHeight - 10; // margen
	                    
	                    var rows = view.getNodes();
	                    var totalContentHeight = 0;
	                    var rowHeights = [];
	                    
	                    // Calcular altura necesaria para cada fila
	                    Ext.each(rows, function(row, index) {
	                        var rowHeight = 25; // mínima
	                        var cells = Ext.get(row).query('.x-grid-cell');
	                        
	                        Ext.each(cells, function(cell) {
	                            var cellEl = Ext.get(cell);
	                            cellEl.setStyle('height', 'auto');
	                            var contentHeight = cellEl.dom.scrollHeight;
	                            if (contentHeight > rowHeight) {
	                                rowHeight = contentHeight + 8; // padding
	                            }
	                        });
	                        
	                        rowHeights[index] = rowHeight;
	                        totalContentHeight += rowHeight;
	                    });
	                    
	                    // Distribuir espacio sobrante si hay
	                    if (totalContentHeight < availableHeight && rows.length > 0) {
	                        var extraHeight = (availableHeight - totalContentHeight) / rows.length;
	                        
	                        Ext.each(rows, function(row, index) {
	                            Ext.get(row).setHeight(rowHeights[index] + extraHeight);
	                        });
	                    } else {
	                        // Usar alturas calculadas por contenido
	                        Ext.each(rows, function(row, index) {
	                            Ext.get(row).setHeight(rowHeights[index]);
	                        });
	                    }
	                    
	                    grid.updateLayout();
	                }	                
	            }, 200);
	        }
	    }
	},
    initComponent: function() {
    	this.emptyText = SuppAppMsg.emptyMsg;
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
			    12);
		
		this.store.getProxy().actionMethods = { read: 'POST' };

		var gridStore = this.store;
		
        this.columns = [
        	{   
        	   text: SuppAppMsg.approvalTicket,
        	   minWidth: 120,
        	   flex: 1,
               dataIndex: 'ticketId'
           },{   
        	   text: SuppAppMsg.suppliersNumber ,
        	   minWidth: 120,
               flex: 1,
               dataIndex: 'addresNumber'
           },{
        	   text     : SuppAppMsg.suppliersNameSupplier,
        	   minWidth: 300,
        	   flex: 2,
        	   dataIndex: 'razonSocial'
	       },{
	            text     : SuppAppMsg.approvalLevel,
	            //maxWidth: 120,
	            flex: 1,
	            dataIndex: 'approvalStep'
	       },{
	            text     : SuppAppMsg.approvalCurrentApprover,
	            //width: 120,
	            flex: 1,
	            dataIndex: 'currentApprover'
	       },{
	            text     : SuppAppMsg.approvalNextApprover,
	            //width: 120,
	            flex: 1,
	            dataIndex: 'nextApprover'
	       },{
	            text     : 'Status',
	            //width: 90,
	            flex: 1,
	            dataIndex: 'approvalStatus'
	       },{
	            text     : SuppAppMsg.approvalRequestDate,
	            //width: 90,
	            flex: 1,
	            dataIndex: 'fechaSolicitud',
	            renderer : Ext.util.Format.dateRenderer("d-m-Y")
	       },{
	            text     : SuppAppMsg.purchaseTitle30,
	            //maxWidth: 300,
	            flex: 2,
	            dataIndex: 'approvalNotes'
	       },{
	        	xtype: 'actioncolumn',
	        	hidden:true,
	        	//hidden:role=='ROLE_ADMIN'?false:true,
	            width: 90,
	            flex: 1,
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
              defaults: {
                  labelAlign: 'top',
                  flex:.3
              },
              items: [{
      			xtype: 'textfield',
                fieldLabel: SuppAppMsg.suppliersName,
                id: 'supSearchName',
                itemId: 'supSearchName',
                name:'supSearchName',
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
                valueField: 'status'
    		},{
    			fieldLabel : SuppAppMsg.approvalLevel,
    			name : 'supSearchApprLevel',
    			id : 'supSearchApprLevel',
    			xtype: 'combobox',
                queryMode: 'local',
    			store : apprLevelStore,
                displayField: 'description',
                valueField: 'status',
    		},{
    			xtype: 'textfield',
                fieldLabel: SuppAppMsg.approvalCurrentApprover,
                id: 'supSearchApprover',
                itemId: 'supSearchApprover',
                name:'supSearchApprover'
    		},{
		        xtype: 'displayfield',
		        value: '',
		        flex:.4
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
	                }
                  }
              ]},
              
              getPagingContent()
      ];

      
        this.callParent(arguments);
    }
});