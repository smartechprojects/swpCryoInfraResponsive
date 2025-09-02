Ext.define('SupplierApp.view.fiscalDocuments.AcceptInvGridFD' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.acceptInvGridFD',
    forceFit: true,
    loadMask: true,
	frame:true,
	border:false,
	title: SuppAppMsg.fileUploadError10,
	cls: 'extra-large-cell-grid',  
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
	 
		this.store =  storeAndModelFactory('SupplierApp.model.PurchaseOrder',
                'fiscalDocumentsModel',
                'fiscalDocuments/getComplFiscalDocsByStatus.action',
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
                      text: SuppAppMsg.acceptTitle2,
                      name : 'rejectSelInvFD',
         			  itemId : 'rejectSelInvFD',
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
                items: [{
		           		xtype:'button',
			            text: SuppAppMsg.acceptTitle3,
			            iconCls: 'icon-appgo',
			            action:'loadComplFileFD',
			            cls: 'buttonStyle',
			            margin:'10 20 10 10'
					}
                ]
            }
        ];
        
      
        this.callParent(arguments);
    }
});