Ext.define('SupplierApp.view.supplier.SupplierGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.supplierGrid',
    loadMask: true,
	frame:false,
	border:false,
	//forceFit:true,
	//fullscreen: true,
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
    	var supController = SupplierApp.app.getController("SupplierApp.controller.Supplier");
    	
		this.store =  storeAndModelFactory('SupplierApp.model.SupplierDTO',
                'supplierModel',
                'supplier/searchSupplier.action', 
                false,
                {
			        supAddNbr : '',
	    			supAddName :''
                },
			    "", 
			    12);
 
        this.columns = [
           {
            text     : SuppAppMsg.suppliersNumber,
            flex: 0.5,
            dataIndex: 'addresNumber'
        },{
            text     : SuppAppMsg.suppliersNameSupplier,
            flex: 1,
            dataIndex: 'razonSocial'
        },{
        	xtype: 'actioncolumn', 
        	maxWidth: 90,
        	flex: 1,
            header: SuppAppMsg.suppliersDisable,
            align: 'center',
			name : 'disableSupplier',
			hidden:true,
			itemId : 'disableSupplier',
            style: 'text-align:center;',
            items: [
            	{
            	icon:'resources/images/cancel.jpg',
              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
              	          if(r.data.observaciones == "INHABILITADO") {
              	              return "x-hidden-display";
              	          }
              	      },
              	      text: 'RECHAZAR',
                  handler: function(grid, rowIndex, colIndex) {
                  	this.fireEvent('buttonclick', grid, rowIndex, colIndex);
                  }}]
        }/*,{
            text     : 'Categoría',
            width: 400,
            dataIndex: 'categoria'
        },{
            text     : 'Tipo de producto',
            width: 250,
            dataIndex: 'tipoProducto'
        }*/];
        
        
        
        this.dockedItems = [
            {
              xtype: 'toolbar',
              dock: 'top',
              padding: '0',
              items: [{
      			xtype: 'textfield',
                fieldLabel: SuppAppMsg.suppliersNumber,
                labelAlign:'top',
                id: 'supAddNbr',
                itemId: 'supAddNbr',
                name:'supAddNbr',
                value: role.includes('ROLE_SUPPLIER')?addressNumber:'',
                fieldStyle: role.includes('ROLE_SUPPLIER')?'border:none;background-color: #ddd; background-image: none;':'',
                readOnly: role.includes('ROLE_SUPPLIER')?true:false,
                width:150,
                listeners: {
                    specialkey: function(field, e) {
                        if (e.getKey() === e.ENTER) {
                            // Buscar el botón en el grid completo
                            var grid = field.up('grid');
                            if (grid) {
                                var button = grid.down('button[action="supAddNbrSrch"]');
                                if (button) {
                                    // Disparar el evento click del botón
                                    button.fireEvent('click', button);
                                }
                            }
                        }
                    }
                }
    		},{
    			xtype: 'textfield',
                labelAlign:'top',
                fieldLabel: SuppAppMsg.suppliersName,
                id: 'supAddName',
                itemId: 'supAddName',
                name:'supAddName',
                value: role.includes('ROLE_SUPPLIER')?addressNumber:'',
                fieldStyle: role.includes('ROLE_SUPPLIER')?'border:none;background-color: #ddd; background-image: none;':'',
                readOnly: role.includes('ROLE_SUPPLIER')?true:false,
                width:380,
                labelWidth:70,
                listeners: {
                    specialkey: function(field, e) {
                        if (e.getKey() === e.ENTER) {
                            // Buscar el botón en el grid completo
                            var grid = field.up('grid');
                            if (grid) {
                                var button = grid.down('button[action="supAddNbrSrch"]');
                                if (button) {
                                    // Disparar el evento click del botón
                                    button.fireEvent('click', button);
                                }
                            }
                        }
                    }
                }
    		}
              ]},
             {
                xtype: 'toolbar',
                dock: 'top',
                padding: '0',
                defaults: {
                    margin: '5 5 10 0' 
                },
                items: [{
               		xtype:'button',
                    hidden: role.includes('ROLE_SUPPLIER')?true:false,
                    text: SuppAppMsg.suppliersSearch,
                    iconCls: 'icon-doSearch',
                    action:'supAddNbrSrch',
                    id:'supAddNbrSrch',
                    itemId:'supAddNbrSrch',
	                cls: 'buttonStyle'
        		},{
               		xtype:'button',
                    hidden: role.includes('ROLE_ADMIN')?false:true,
                    text: SuppAppMsg.suppliersLoadFile,
                    iconCls: 'icon-excel',
                    action:'uploadSuppliersFile',
	                cls: 'buttonStyle'
        		},{
               		xtype:'button',
                    hidden: true,
                    text: 'Replicar proveedores',
                    action:'replicateSupplier',
                    iconCls: 'icon-doSearch',
	                cls: 'buttonStyle'
                  }
                	 
              ]},
		    getPagingContent()
      ]; 
        
      
      
        this.callParent(arguments);
        
        this.on('afterrender', function() {
            var view = this.getView();
            view.el.setStyle('height', 'auto');
        }, this);
    }
});