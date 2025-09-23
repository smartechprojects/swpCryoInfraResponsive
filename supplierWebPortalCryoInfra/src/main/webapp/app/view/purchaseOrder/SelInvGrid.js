Ext.define('SupplierApp.view.purchaseOrder.SelInvGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.selInvGrid',
    loadMask: true,
    frame:true,
    border:false,
    scrollable: true,
    title:'Selección de órdenes',
    cls: 'extra-large-cell-grid',  
    viewConfig: {
        stripeRows: true,
        style : { overflow: 'auto', overflowX: 'hidden' }
    },
    initComponent: function() {
        this.selectedRecords = [];
        this.store =  storeAndModelFactory('SupplierApp.model.Receipt',
            'receiptModel',
            'receipt/getComplReceiptsByStatus.action', 
            false,
            {
                addressBook:''
            },
            "", 
            100);

        this.columns = [{
            xtype: 'checkcolumn',
            text: Ext.String.format('<input type="checkbox" id="{0}" class="select-all-checkbox"/>', Ext.id()),
            //width: 30,
            flex: 0.2,
            dataIndex: 'selected',
            sortable: false,
            menuDisabled: true,
            listeners: { 
                checkchange: function (column, rowIndex, checked) {
                    var record = this.store.getAt(rowIndex);
                    record.set('selected', checked);
                    if (checked) {
                        this.selectedRecords.push(record);
                    } else {
                        Ext.Array.remove(this.selectedRecords, record);
                    }
                    // Actualizar visualmente el estado del checkbox en la vista
                    this.getView().refresh();
                },
                scope: this
            }
        },
        {
            text     : SuppAppMsg.acceptTitle1,
            //width: 90,
            flex: 1,
            dataIndex: 'documentNumber'
        },{
            text     : 'UUID',
            //width: 260,
            flex: 2,
            dataIndex: 'uuid'
        },{
            text     : SuppAppMsg.purchaseTitle54,
            //width: 90,
            flex: 1,
            dataIndex: 'folio'
        },{         
            renderer: function(value, meta, record) {
               var id = Ext.id();
               Ext.defer(function() {
                  Ext.widget('button', {
                     renderTo: Ext.query("#"+id)[0],
                     text: SuppAppMsg.purchaseTitle55,
                     name : 'acceptSelInv',
                     itemId : 'acceptSelInv',
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
                items: [
                    {
                        xtype: 'textfield',
                        fieldLabel: SuppAppMsg.purchaseTitle56,
                        id: 'invNbr',
                        itemId: 'invNbr',
                        name: 'invNbr',
                        width: 200,
                        labelWidth: 90,
                        margin: '10 20 10 10',
                        hidden: true
                    },{
                        xtype: 'button',
                        text: SuppAppMsg.suppliersSearch,
                        iconCls: 'icon-appgo',
                        action: 'invNbrSearch',
                        cls: 'buttonStyle',
                        margin: '10 20 10 10',
                        hidden: true
                    }
                ]
            }
        ];

        this.callParent(arguments);

        // Manejar evento de clic en el checkbox "seleccionar todos"
        this.on('afterrender', function(grid) {
            grid.el.on('click', function(event, el) {
                var target = Ext.get(event.getTarget());
                if (target.hasCls('select-all-checkbox')) {
                	debugger
                    var isChecked = target.dom.checked;
                    var records = grid.store.data.items;
                    Ext.each(records, function(record) {
                        record.set('selected', isChecked);
                    });
                    // Actualizar visualmente el estado de los checkboxes en la vista
                    
                    if (isChecked) {
                        grid.selectedRecords = records;
                    } else {
                        grid.selectedRecords = [];
                    }
                    grid.getView().refresh();
                }
            });
        });
    }
});
