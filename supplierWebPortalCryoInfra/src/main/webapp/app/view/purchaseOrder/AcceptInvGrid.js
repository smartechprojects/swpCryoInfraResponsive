Ext.define('SupplierApp.view.purchaseOrder.AcceptInvGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.acceptInvGrid',
    loadMask: true,
	frame:true,
	border:false,
	//forceFit: true,
	title:'Órdenes seleccionadas',
	cls: 'extra-large-cell-grid',  
	scrollable: true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
	 
		this.store =  storeAndModelFactory('SupplierApp.model.PurchaseOrder',
                'receiptModel',
                'receipt/getComplReceiptsByStatus.action',
                false,
                {
			        addressBook:''
                },
			    "", 
			    100);
 
        this.columns = [
           {
            text     : SuppAppMsg.acceptTitle1,
            //width: 90,
            flex: 1,
            dataIndex: 'documentNumber'
        },{
            text     : 'UUID',
            width: 250,
            flex: 2,
            dataIndex: 'uuid'
        },{
            renderer: function(value, meta, record) {
                var id = Ext.id();
                Ext.defer(function() {
                   Ext.widget('button', {
                      renderTo: Ext.query("#"+id)[0],
                      text: SuppAppMsg.acceptTitle2,
                      name : 'rejectSelInv',
         			  itemId : 'rejectSelInv',
                      scale: 'small',
                      handler: function(grid, rowIndex, colIndex) {
                       	this.fireEvent('buttonclick', grid, record);
                       }
                   });
                }, 50);
                return Ext.String.format('<div id="{0}"></div>', id);
             },
             hidden: role=='ROLE_AUDIT_USR' ? true : false
          }];
        
        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: {
                    background: 'white'
                  },
                dock: 'top',
                layout: {
                    type: 'hbox',
                    align: 'middle',
                    pack: 'start',
                    // permite que los items bajen de línea si no caben
                 //   overflowHandler: 'menu'
                },
                defaults: {
                    margin: '10 10 10 0'
                },
                items: [{
		           		xtype:'button',
			            text: SuppAppMsg.acceptTitle3,
			            iconCls: 'icon-appgo',
			            action:'loadComplFile',
			            cls: 'buttonStyle',
			            //margin:'10 20 10 10',
			            hidden: role=='ROLE_AUDIT_USR' ? true : false,
					},{
						xtype: 'container',
						layout: {
			                type: 'hbox',
			                align: 'middle',
			                pack: 'start'
			            },
			            defaults: {
			                margin: '10 10 10 0'
			            },
						items: [{
							xtype:'button',
							text: 'CargarZip',
							iconCls: 'icon-appgo',
							action: 'loadComplFileMassive',
							cls: 'buttonStyle',
							//margin: '10 20 10 10',
							hidden: role=='ROLE_AUDIT_USR' ? true : false
						},{
							xtype: 'label',
							html: '<b>Para esta Opción, se pueden cargar<br>varios Complementos que no<br>superen los 25MB Total</b>',
							//margin: '10 20 10 10'
							style: {
					            lineHeight: '20px', // <-- mejora alineación vertical
					            display: 'inline-block'
					        }
						}]
					}
                ]
            }
        ];
        
      
        this.callParent(arguments);
    }
});