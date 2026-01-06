Ext.define('SupplierApp.view.token.TokenGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.tokenGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',
    store : {
		type:'accesstokenregister'
	},
	scroll :  true,
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
    	
    	var tokenController = SupplierApp.app.getController("SupplierApp.controller.Token");
 
        this.columns = [
        	{
                hidden:true,
                dataIndex: 'id'
            },{
                text     : window.navigator.language.startsWith("es", 0)? 'Proveedor':'Supplier',
                dataIndex: 'registerName',
                //width: 100
                //flex : 1,
                minWidth: 150
            },{
                text     : 'Email',
                dataIndex: 'email',
                //width: 85
                flex : 1,
                minWidth: 150
            },{
                text     : window.navigator.language.startsWith("es", 0)? 'Creado por':'Created by',
                dataIndex: 'createdBy',
                //width: 45
                flex : 1,
                minWidth: 60
            },{
                text     : window.navigator.language.startsWith("es", 0)? 'Actualizado por':'Updated by',
                dataIndex: 'updatedBy',
                //width: 45
                //flex : 1
                minWidth: 80
            },{
                text     : window.navigator.language.startsWith("es", 0)? 'Activo':'Enabled',
                dataIndex: 'enabled',
                //width: 30
                flex : 1
            },{
                text     : window.navigator.language.startsWith("es", 0)? 'Asignado':'Assigned',
                dataIndex: 'assigned',
                //width: 30
                flex : 1
            },{
                text     : window.navigator.language.startsWith("es", 0)? 'Fecha de Registro':'Creation Date',
                dataIndex: 'creationDate',
                //width: 60,
                flex : 1,
                renderer : Ext.util.Format.dateRenderer("d-m-Y")
            },{
                text     : window.navigator.language.startsWith("es", 0)? 'Fecha Actualización':'Update Date',
                dataIndex: 'updatedDate',
                //width: 60,
                flex : 1,
                renderer : Ext.util.Format.dateRenderer("d-m-Y")
            },{
                text     : window.navigator.language.startsWith("es", 0)? 'Fecha Expiración':'Expiration Date',
                dataIndex: 'expirationDate',
                //width: 60,
                flex : 1,
                renderer : Ext.util.Format.dateRenderer("d-m-Y")
            }];
      
        
        this.dockedItems = [
		    getPagingContent()
      ];
	  
		
        this.callParent(arguments);
    }
});