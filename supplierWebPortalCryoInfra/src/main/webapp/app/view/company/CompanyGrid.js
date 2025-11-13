Ext.define('SupplierApp.view.company.CompanyGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.companyGrid',
	id: 'idCompanyGrid',
	loadMask : true,
	frame : false,
	border : false,
	cls: 'extra-large-cell-grid', 
	store : {
		type:'company'
	},
    scroll : true,
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
	initComponent : function() {
		this.emptyText = SuppAppMsg.emptyMsg;
		var companyController = SupplierApp.app.getController("SupplierApp.controller.Company");

		this.columns = [ {
			text : SuppAppMsg.paymentTitle1,
			//width : 120,
			flex: 1,
			dataIndex : 'company'
		},{
			text : SuppAppMsg.suppliersName,
			//width : 200,
			flex: 1,
			dataIndex : 'companyName'
		},{
			text : SuppAppMsg.usersEmail,
			//width : 120,
			flex: 1,
			dataIndex : 'notificationEmail'
		},{
			text : SuppAppMsg.companys2,
			//width : 100,
			flex: 1,
			dataIndex : 'active'
		}];

		
        this.dockedItems = [
            {
              xtype: 'toolbar',
              dock: 'top',
              items: [
            	  {
          			name : 'searchCompany',
          			itemId : 'searchCompany',
          			emptyText : SuppAppMsg.suppliersSearch,
          			xtype : 'trigger',
          			//width : 400,
          			flex: 1,
          			margin: '5 0 10 0',
          			triggerCls : 'x-form-search-trigger',
          			onTriggerClick : function(e) {
          				companyController.loadSearchList(e);
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
              getPagingContent()
      ];
		

		this.callParent(arguments);
	}
});