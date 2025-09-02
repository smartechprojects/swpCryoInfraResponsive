	Ext.define('SupplierApp.view.customBroker.SelInvGridCB' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.selInvGridCB',
    loadMask: true,
	frame:true,
	border:false,
	title:SuppAppMsg.fileUploadError9,
	cls: 'extra-large-cell-grid',  
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
	 
		this.store =  storeAndModelFactory('SupplierApp.model.customBroker',
                'customBrokerModel',
                'customBroker/getComplFiscalDocsByStatus.action', 
                false,
                {
			        addressBook:''
                },
			    "", 
			    100);
 
        this.columns = [
          {
            text     : 'UUID',
            width: 260,
            dataIndex: 'uuidFactura'
        },{
            text     : SuppAppMsg.fiscalTitle4,
            width: 90,
            dataIndex: 'serie'
        },{
            text     : SuppAppMsg.fiscalTitle3,
            width: 90,
            dataIndex: 'folio'
        },{
            renderer: function(value, meta, record) {
               var id = Ext.id();
               Ext.defer(function() {
                  Ext.widget('button', {
                     renderTo: Ext.query("#"+id)[0],
                     text: SuppAppMsg.purchaseTitle55,
                     name : 'acceptSelInvCB',
        			 itemId : 'acceptSelInvCB',
                     scale: 'small',
                     handler: function(grid, rowIndex, colIndex) {
                      	this.fireEvent('buttonclick', grid, record);
                      }
                  });
               }, 50);
               return Ext.String.format('<div id="{0}"></div>', id);
            }
         }];
        
        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: {
                    background: 'white'
                  },
                dock: 'top',
                items: [
                	{
						xtype: 'textfield',
			            fieldLabel:SuppAppMsg.purchaseTitle56,
			            id: 'invNbrCB',
			            itemId: 'invNbrCB',
			            name:'invNbrCB',
			            width:200,
			            labelWidth:90,
			            margin:'10 20 10 10',
			            hidden:true
					},{
		           		xtype:'button',
			            text: SuppAppMsg.suppliersSearch,
			            iconCls: 'icon-appgo',
			            action:'invNbrSearchCB',
			            cls: 'buttonStyle',
			            margin:'10 20 10 10',
			            hidden:true
					}
                ]
            }
        ];
        
      
        this.callParent(arguments);
    }
});