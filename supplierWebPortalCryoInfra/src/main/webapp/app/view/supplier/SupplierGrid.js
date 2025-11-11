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
		stripeRows: true,
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
                width:150
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
                labelWidth:70
    		}
              ]},
             {
                xtype: 'toolbar',
                dock: 'top',
                items: [{
               		xtype:'button',
                    hidden: role.includes('ROLE_SUPPLIER')?true:false,
                    text: SuppAppMsg.suppliersSearch,
                    iconCls: 'icon-doSearch',
                    action:'supAddNbrSrch',
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