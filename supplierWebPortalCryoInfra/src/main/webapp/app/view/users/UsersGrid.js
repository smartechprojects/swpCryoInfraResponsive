Ext.define('SupplierApp.view.users.UsersGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.usersGrid',
	id: 'idUsersGrid',
	//forceFit: true,
	loadMask : true,
	frame : false,
	store : {
		type:'usersstore'
	},
	dockedItems: [
    	getPagingContent()
     ],
	border : false,
	cls: 'extra-large-cell-grid', 
	//style :'border: solid #ccc 1px',
	store : 'Users',
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
	initComponent : function() {
		this.emptyText = SuppAppMsg.emptyMsg;
		var userController = SupplierApp.app.getController("SupplierApp.controller.Users");

		this.columns = [ {
			text : SuppAppMsg.suppliersName,
			//width : 200,
			flex: 1,
			dataIndex : 'name'
		},{
			text : SuppAppMsg.usersUser,
			//width : 120,
			flex: 1,
			dataIndex : 'userName'
		}, {
			text : SuppAppMsg.usersRole,
			//width : 100,
			flex: 1,
			dataIndex : 'userRole.id',
			renderer:function (value,metaData,record,row,col,store,gridView){
           	 return record.data.userRole.strValue1;
			 }
		}, {
			text : SuppAppMsg.usersUserType,
			//width : 100,
			flex: 1,
			dataIndex : 'userType.id',
			renderer:function (value,metaData,record,row,col,store,gridView){
           	 return record.data.userType.strValue1;
			 }
		},{
			text : SuppAppMsg.usersSupplier,
			//width : 100,
			flex: 1,
			dataIndex : 'addressNumber'
		},{
			text : SuppAppMsg.usersMainUser,
			//width : 100,
			flex: 1,
			dataIndex : 'mainSupplierUser',
			renderer:function (value,metaData,record,row,col,store,gridView){
				if(value == true){
					return SuppAppMsg.usersBooleanYes;
				} else {
					return SuppAppMsg.usersBooleanNo;
				}	           	 
			},
			align: 'center'
		},{
			text : SuppAppMsg.usersActivo,
			//width : 50,
			flex: 1,
			dataIndex : 'enabled',
			renderer:function (value,metaData,record,row,col,store,gridView){
				if(value == true){
					return SuppAppMsg.usersBooleanYes;
				} else {
					return SuppAppMsg.usersBooleanNo;
				}	           	 
			},
			align: 'center'
		},{
			hidden:true,
			//width : 50,
			flex: 1,
			dataIndex : 'openOrders'
		},{
			hidden:true,
			//width : 50,
			flex: 1,
			dataIndex : 'logged'
		},{
			hidden:true,
			//width : 50,
			flex: 1,
			dataIndex : 'agreementAccept'
		}  ];


		this.tbar = {
			    padding: 0,
			    margin: '0 0 5 0',
			    items: [{
			        name : 'searchUsers',
			        itemId : 'searchUsers',
			        emptyText : SuppAppMsg.suppliersSearch,
			        xtype : 'trigger',
			        width: 300,
			        triggerCls : 'x-form-search-trigger',
			        onTriggerClick: function() {
			            userController.loadSearchList(this, this.getValue());
			        },
			        enableKeyEvents : true,
			        listeners : {
			            specialkey : function(field, e) {
			                if (e.ENTER === e.getKey()) field.onTriggerClick();
			            }
			        }
			    }]
			};

		this.callParent(arguments);
	}
});