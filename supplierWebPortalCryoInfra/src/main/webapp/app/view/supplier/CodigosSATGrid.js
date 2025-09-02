Ext.define('SupplierApp.view.supplier.CodigosSATGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.codigosSATGrid',
    forceFit: true,
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid', 
	scroll : false,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
    	
    	var csatController = SupplierApp.app.getController("SupplierApp.controller.CodigosSAT");
    	
		this.store =  storeAndModelFactory('SupplierApp.model.CodigosSAT',
                'codigosSATModel',
                'codsat/searchByCriteria.action', 
                true,
                {
			        query : ''
                },
			    "", 
			    11);
 
        this.columns = [
           {
            text     : SuppAppMsg.purchaseSATKey,
            width: 200,
            dataIndex: 'codigoSAT'
        },{
            text     : 'Tipo',
            width: 200,
            dataIndex: 'tipoCodigo'
        },{
            text     : SuppAppMsg.purchaseTitle29,
            width: 420,
            dataIndex: 'descripcion'
        }];
        
       
        this.dockedItems = [
	          {
	            xtype: 'toolbar',
	            dock: 'top',
	            items: [
	            	{
	        			xtype: 'textfield',
	                    fieldLabel:SuppAppMsg.complianceSearchText,
	                    id: 'codigoSatSearch',
	                    itemId: 'codigoSatSearch',
	                    name:'codigoSatSearch',
	                    width:400,
		                labelAlign:'top',
		                margin:'5 0 0 0'
	        		}
	            ]},
	           {
	              xtype: 'toolbar',
	              dock: 'top',
	              items: [
	            	  {
	                 	  xtype:'button',
	                      text: SuppAppMsg.suppliersSearch,
	                      iconCls: 'icon-doSearch',
	                      action:'codigoSatSearchBtn',
	                      margin:'0 0 10 0',
	                      cls: 'buttonStyle'
	          		},{
	                 	  xtype:'button',
	                      hidden: role=='ROLE_ADMIN'?false:true,
	                      text: SuppAppMsg.suppliersLoadFile,
	                      iconCls: 'icon-excel',
	                      action:'uploadCodigosSatFile',
	                      margin:'0 0 10 10',
	                      cls: 'buttonStyle',
	                      listeners: {
			                    tap: function (button) {
			                    	csatController.codigoSatSearchBtn(button);
			                    }
			                }
	          		}
	            ]},
	            getPagingContent()
	    ];
      
        this.callParent(arguments);
    }
});