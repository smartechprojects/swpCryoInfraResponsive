Ext.define('SupplierApp.view.supplier.SupplierGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.supplierGrid',
    forceFit: true,
    loadMask: true,
	frame:false,
	border:true,
	cls: 'extra-large-cell-grid', 
	scroll : false,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
    	
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
			    11);
 
        this.columns = [
           {
            text     : SuppAppMsg.suppliersNumber,
            width: 120,
            dataIndex: 'addresNumber'
        },{
            text     : SuppAppMsg.suppliersNameSupplier,
            width: 420,
            dataIndex: 'razonSocial'
        },{
        	xtype: 'actioncolumn', 
            width: 90,
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
              	              return "x-hide-display";
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
                id: 'supAddNbr',
                itemId: 'supAddNbr',
                name:'supAddNbr',
                value: role.includes('ROLE_SUPPLIER')?addressNumber:'',
                fieldStyle: role.includes('ROLE_SUPPLIER')?'border:none;background-color: #ddd; background-image: none;':'',
                readOnly: role.includes('ROLE_SUPPLIER')?true:false,
                width:200,
                labelWidth:90,
                margin:'10 20 0 20'
    		},{
    			xtype: 'textfield',
                fieldLabel: SuppAppMsg.suppliersName,
                id: 'supAddName',
                itemId: 'supAddName',
                name:'supAddName',
                value: role.includes('ROLE_SUPPLIER')?addressNumber:'',
                fieldStyle: role.includes('ROLE_SUPPLIER')?'border:none;background-color: #ddd; background-image: none;':'',
                readOnly: role.includes('ROLE_SUPPLIER')?true:false,
                width:500,
                labelWidth:70,
                margin:'10 20 0 0'
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
                    margin:'0 20 10 20'
        		},{
               		xtype:'button',
                    hidden: role.includes('ROLE_ADMIN')?false:true,
                    text: SuppAppMsg.suppliersLoadFile,
                    iconCls: 'icon-excel',
                    action:'uploadSuppliersFile',
                    margin:'0 20 10 20'
        		},{
               		xtype:'button',
                    hidden: true,
                    text: 'Replicar proveedores',
                    action:'replicateSupplier',
                    iconCls: 'icon-doSearch',
                    margin:'0 20 10 20'
                  }
                	 
              ]},
		    getPagingContent()
      ]; 
        
      
      
        this.callParent(arguments);
    }
});