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
	originalGridWidth: null,
	viewConfig: {
	    stripeRows: true,
	    style : { overflow: 'auto', overflowX: 'hidden' },
	    enableTextSelection: true,
	    markDirty: false,
	    listeners: {
	        refresh: function(view) {
	            var grid = view.up('grid');
	            if (!grid) return;
	            // Usar la función centralizada
	            GridUtils.adjustGridLayout(grid, true);
	        },
	        
	        resize: function(view) {
	            var grid = view.up('grid');
	            if (!grid) return;
	            // Usar la función centralizada
	            GridUtils.adjustGridLayout(grid, false);
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