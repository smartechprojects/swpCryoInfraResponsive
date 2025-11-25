Ext.define('SupplierApp.view.fiscalDocuments.FiscalDocumentsGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.fiscalDocumentsGrid',
    loadMask: true,
	frame:false,
	border:false,
	store : {
		type:'fiscaldocuments'
	},
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
	                var pageSize = 1;
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

		                 // Número real de filas visibles
		                 var realRowCount = rows.length;

		                 // Número deseado (12 máximo)
		                 var targetRowCount = 12;

		                 // Calcular altura disponible total
		                 var rowContainerHeight = availableHeight;
		                 
		                 //Cuando el registro es una fila 
		                 if (realRowCount === 1) {

		                     var uniformHeight = rowContainerHeight / targetRowCount;

		                     Ext.get(rows[0]).setHeight(uniformHeight);

		                     Ext.defer(function () {
		                         Ext.get(rows[0]).setHeight(uniformHeight);
		                         grid.updateLayout();
		                     }, 50);

		                     return;
		                 }

		                 // Para 2..11 filas
		                 if (realRowCount > 1 && realRowCount < targetRowCount) {

		                     var uniformHeight = rowContainerHeight / targetRowCount;

		                     Ext.each(rows, function(row) {
		                         Ext.get(row).setHeight(uniformHeight);
		                     });

		                     grid.updateLayout();
		                     return;
		                 }

		                 // Lógica normal para 12 o más filas
		                 var totalContentHeight = 0;
		                 var rowHeights = [];

		                 Ext.each(rows, function(row, index) {
		                     var rowHeight = 25; 
		                     var cells = Ext.get(row).query('.x-grid-cell');
		                     
		                     Ext.each(cells, function(cell) {
		                         var cellEl = Ext.get(cell);
		                         cellEl.setStyle('height', 'auto');
		                         var contentHeight = cellEl.dom.scrollHeight;
		                         if (contentHeight > rowHeight) {
		                             rowHeight = contentHeight + 8; 
		                         }
		                     });

		                     rowHeights[index] = rowHeight;
		                     totalContentHeight += rowHeight;
		                 });

		                 if (totalContentHeight < availableHeight && rows.length > 0) {
		                     var extraHeight = (availableHeight - totalContentHeight) / rows.length;
		                     
		                     Ext.each(rows, function(row, index) {
		                         Ext.get(row).setHeight(rowHeights[index] + extraHeight);
		                     });
		                 } else {
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
    	var fdController = SupplierApp.app.getController("SupplierApp.controller.FiscalDocuments");
    	
    	var docType = null;
    	var invStatus = null;
    	var approverPOSP = getUDCStore('APPROVERPOSP', '', '', '');
    	approverPOSP.load()
    	
    	docType = Ext.create('Ext.data.Store', {
        	    fields: ['id', 'name'],
        	    data : [
        	        {"id":"FACTURA", "name":"Factura"},
        	        {"id":"FACT EXTRANJERO", "name":"Factura Extranjeros"}
        	        ,{"id":"NOTACREDITO", "name":"Nota de Credito"}
        	    ]
        	});

    	invStatus = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'name'],
    	    data : [    	        
    	        {"id":"PENDIENTE", "name":"PENDIENTE"},
    	        {"id":"APROBADO", "name":"APROBADO"},
    	        {"id":"RECHAZADO", "name":"RECHAZADO"},
    	        {"id":"PAGADO", "name":"PAGADO"},
    	        {"id":"CANCELADO", "name":"CANCELADO"}
    	    ]
    	});
    	
    	Ext.define('docTypeCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.fiscalTitle21,
    	    store: docType,
    	    alias: 'widget.combodoctype',
    	    queryMode: 'local',
    	    //allowBlank:false,
    	    //editable: false,
    	    displayField: 'name',
			flex:.8,
    	    valueField: 'id',
    	    //margin:'20 20 0 10',
    	    id:'comboDocumentType',    	    
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
    	    //allowBlank:false,
    	    //editable: false,
    	    displayField: 'name',
			//width:150,
    	    flex: .4,
    	    labelWidth:40,
    	    valueField: 'id',
    	    //margin:'20 20 0 10',
    	    id:'comboInvoiceStatus',
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
			            text     : SuppAppMsg.suppliersNumber,
			            //width: 90,
			            flex: 1,
			            dataIndex: 'addressNumber'
			        },{
			            text     : SuppAppMsg.suppliersNameSupplier,
			            //width: 230,
			            flex: 2,
			            dataIndex: 'supplierName'
			        },{
			            text     : SuppAppMsg.fiscalTitle30,
			            minWidth: 150,
			            flex: 1,
			            dataIndex: 'rfcReceptor'
			        },{
			            text     : SuppAppMsg.fiscalTitle3,
			            minWidth: 90,
			            flex: 1,
			            dataIndex: 'folio'
			        },{
			            text     : SuppAppMsg.fiscalTitle32,
			            //width: 90,
			            flex: 1,
			            dataIndex: 'orderNumber'
			        },{
			            text     : SuppAppMsg.fiscalTitle33,
			            //width: 90,
			            flex: 1,
			            dataIndex: 'orderType'
			        },{
			            text     : SuppAppMsg.fiscalTitle4,
			            minWidth: 100,
			            flex: 1,
			            dataIndex: 'serie',
			            renderer : function(value, metadata, record, rowIndex, colIndex, store){
			            	return value == null || value == 'null'? "" : value;
				}
			        } ,{
			            text     : SuppAppMsg.fiscalTitle26,
			            minWidth: 120,
			            flex: 1,
			            dataIndex: 'invoiceUploadDate',
			            renderer: function(value) {
			                console.log("valor: "+value); // Imprimir el valor en la consola
			                if (value) {
			                  var date = new Date(value); // Convertir a objeto Date
			                  return Ext.util.Format.date(date, 'd-m-Y'); // Formatear la fecha y hora
			                } else {
			                  return '';
			                }
			              }
			        },{
			        	  text: 'Fecha/hora Carga Compl.',
			        	  //width: 120,
			        	  flex: 1,
			        	  dataIndex: 'fechComple',
			        	  renderer: function(value, metaData, record, row, col, store, gridView) {
			        		    var invoiceUploadDate = record.get('fechComple');
			        		    if (!invoiceUploadDate && record.data.complPagoUuid !== null) {
			        		        Ext.Ajax.request({
			        		            url: 'fiscalDocuments/getComplemento.action',
			        		            params: {
			        		                uuid: record.data.complPagoUuid
			        		            },
			        		            success: function(response) {
			        		                var data = Ext.decode(response.responseText).data;
			        		                if (data && data.uploadDate) {
			        		                    var date = new Date(data.uploadDate);
			        		                    var formattedDate = Ext.util.Format.date(date, 'd-m-Y H:i:s');
			        		                    metaData.tdAttr = 'data-qtip="' + formattedDate + '"';
			        		                    record.set('fechComple', formattedDate); // Utilizar el nombre correcto del campo
			        		                    gridView.refresh(); // Actualizar la visualización del grid
			        		                }
			        		            },
			        		            failure: function() {
			        		                // Manejar el fallo de la solicitud AJAX
			        		            }
			        		        });
			        		    }
			        		    return invoiceUploadDate || null;
			        		},
			        	hidden: true


			        },{
			            text     : SuppAppMsg.fiscalTitle29,
			            //width: 90,
			            flex: 1,
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
			            //width: 100,
			            flex: 1,
			            dataIndex: 'amount',
						align: 'center',
			            renderer : Ext.util.Format.numberRenderer('0,0.00')
			        },{
			            text     : SuppAppMsg.fiscalTitle20,
			            //width: 110,
			            flex: 1,
						align: 'center',
			            dataIndex: 'conceptTotalAmount',
			            renderer : Ext.util.Format.numberRenderer('0,0.00')
			        },{
			            text     : SuppAppMsg.fiscalTitle28,
			            //width: 100,
			            flex: 1,
						align: 'center',
			            renderer : function(value, metadata, record, rowIndex, colIndex, store){
									return Ext.util.Format.number((record.data.amount + record.data.conceptTotalAmount),'0,0.00')
						}
			        },{
			            text     : SuppAppMsg.purchaseOrderCurrency,
			            minWidth: 90,
			            flex: 1,
			            dataIndex: 'moneda'
			        },{
			            text     : SuppAppMsg.fiscalTitle5,
			            //width: 250,
			            flex: 2,
			            dataIndex: 'uuidFactura'
			        },/*{
			            text     : 'Uuid nota de credito',
			            width: 250,
			            dataIndex: 'uuidNotaCredito'
			        },{
			            text     : 'Status',
			            width: 110,
			            dataIndex: 'approvalStatus'
			        },*/{
			            text     : SuppAppMsg.fiscalTitle22,
			            //width: 110,
			            flex: 1,
			            dataIndex: 'status'
			        },{
			            text     : SuppAppMsg.approvalLevel,
			            //width: 120,
			            flex: 1,
			            dataIndex: 'approvalStep'
			        },{
			            text     : "Fecha Aprobación",
			            minWidth: 130,
			            flex: 1,
			            dataIndex: 'dateAprov',
			            renderer :  function(value, metaData, record, row, col, store, gridView){
							
			            	if(value) {
								return Ext.util.Format.date(new Date(value), 'd-m-Y H:i:s');
							} else {
								return null;
							}
						}
			        },{
			            text     : SuppAppMsg.approvalCurrentApprover,
			            minWidth: 130,
			            flex: 2,
			            dataIndex: 'currentApprover'
			        },{
			        	xtype: 'actioncolumn', 
			        	minWidth: 90,
			        	flex: 1,
			            header: SuppAppMsg.approvalApprove,
			            align: 'center',
						name : 'approveInvoiceFD',
						hidden: role=='ROLE_ADMIN' || role=='ROLE_MANAGER'?false:true,
						itemId : 'approveInvoiceFD',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/accept.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		// if(r.data.plant == 'SP2900' || r.data.plant == 'SP2802'){

			              			 var contains = false;
			              			 var approvers = r.data.currentApprover.split(",")
			              			 for(var i=0; i < approvers.length; i++){
			              				 if(approvers[i] == userName ){
			              					contains = true;
			              					break;
			              				 }
			              			 }
			              			 if(!contains ||  r.data.status == "CANCELADO" || r.data.approvalStatus == "APROBADO"){
			              			return "x-hidden-display";
			              			 }
			              		 /*}else{
			              	          if(role == 'ROLE_SUPPLIER' || r.data.status != "PENDIENTE" || (r.data.status == "PENDIENTE" && r.data.currentApprover != userName)) {
			              	              return "x-hidden-display";
			              	          }
			              		 }*/
			              	      },
			              	      text: SuppAppMsg.approvalApprove,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        },{
			        	xtype: 'actioncolumn', 
			        	minWidth: 90,
			        	flex: 1,
			            header: SuppAppMsg.approvalReject,
			            align: 'center',
						name : 'rejectInvoiceFD',
						hidden: role=='ROLE_ADMIN' || role=='ROLE_MANAGER'?false:true,
						itemId : 'rejectInvoiceFD',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/close.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		 //if(r.data.plant == 'SP2900' || r.data.plant == 'SP2802'){
				              			
				              			 var contains = false;
				              			 var approvers = r.data.currentApprover.split(",")
				              			 for(var i=0; i < approvers.length; i++){
				              				 if(approvers[i] == userName ){
				              					contains = true;
				              					break;
				              				 }
				              			 }
				              			 if(!contains || r.data.status == "CANCELADO" || r.data.approvalStatus == "APROBADO"){
				              			return "x-hidden-display";
				              			 }
				              		 /*}else{
			              		if(role == 'ROLE_SUPPLIER' || r.data.status != "PENDIENTE" || (r.data.status == "PENDIENTE" && r.data.currentApprover != userName)) {
			              	              return "x-hidden-display";
			              	          }
				              		 }*/
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
              layout: {
                  type: 'hbox',
                  align: 'middle',
                  pack: 'start'
              },
              defaults: {
                  //margin: '5 10 5 0'
            	  labelAlign: 'top'
              },
              items: [
            	  {
          			iconCls : 'icon-save',
          			itemId : 'uploadNewFiscalDoc',
          			id : 'uploadNewFiscalDoc',
          			text : SuppAppMsg.fiscalTitle18,
          			hidden:true,
          			action : 'uploadNewFiscalDoc'
          		}, {
          			name : 'searchFiscalDocuments',
          			itemId : 'searchFiscalDocuments',
          			emptyText : SuppAppMsg.fiscalTitle19,
          			xtype : 'trigger',
          			//width : 300,
          			flex: 1,
          			//margin: '5 0 10 0',
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
          		},{
          			xtype: 'textfield',
                      fieldLabel: 'UUID',
                      id: 'fdUUID',
                      itemId: 'fdUUID',
                      name:'fdUUID',
                      //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
                      //fieldStyle: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?'border:none;background-color: #ddd; background-image: none;':'',
                      //readOnly: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?true:false,
                      //width:300,
                      flex: 1,
                      labelWidth:30
          		},{ 
          			xtype: 'combostatus'
          		},{
          			xtype: 'textfield',
                      fieldLabel: SuppAppMsg.purchaseOrderNumber,
                      id: 'poNumberFD',
                      itemId: 'poNumberFD',
                      name:'poNumberFD',
                      //width:170,
                      flex: .3,
                      labelWidth:70
          		},{
          			xtype: 'textfield',
                      fieldLabel: SuppAppMsg.suppliersNumber,
                      id: 'supNumberFD',
                      itemId: 'supNumberFD',
                      name:'supNumberFD',
                      value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
                      fieldStyle: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?'border:none;background-color: #ddd; background-image: none;':'',
                      readOnly: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?true:false,
                     // width:190,
                    flex: .4,
                      labelWidth:90
          		},{
					xtype: 'displayfield',
		            value: '',
		            flex:.3,

	            	}
              ]},
             {
                xtype: 'toolbar',
                dock: 'top',
                items: [
                	
                	{
                   		xtype:'button',
                        text: SuppAppMsg.suppliersSearch,
                        iconCls: 'icon-appgo',
                        action:'fdSearch',
                        cls: 'buttonStyle',
                        listeners: {
    	                    tap: function (button) {
    	                    	fdController.fdSearch(button);
    	                    }
    	                }
            		}
              ]},
              getPagingContent()
      ];

        this.callParent(arguments);
    }
});