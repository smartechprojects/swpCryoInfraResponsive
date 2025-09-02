Ext.define('SupplierApp.view.purchaseOrder.LogDataGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.logDataGrid',
    loadMask: true,
    forceFit: true,
	cls: 'extra-large-cell-grid', 
	store : {
		type:'logdata'
	},
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
    	
    	var poController = SupplierApp.app.getController("SupplierApp.controller.PurchaseOrder");
    	
    	var types = Ext.create('Ext.data.ArrayStore', {
    	    fields: ['name', 'type'],
    	    data :[['CARGA_DOCUMENTOS', 'CARGA_DOCUMENTOS'],['APROBACIONES', 'APROBACIONES']]
    	});
    	
        this.columns = [
           {
            text     : SuppAppMsg.logsFecha,
            width: 90,
            dataIndex: 'date',
            renderer: function(field) { 
            	var formated = Ext.util.Format.date(field, 'd/m/Y');
            	return formated;
            	}	
        },{
            text     : SuppAppMsg.purchaseOrderType,
            width: 150,
            dataIndex: 'logType'
        },{
            text     : SuppAppMsg.logsMensaje,
            width: 750,
            dataIndex: 'mesage'
        }];
        
   	 this.dockedItems = [{
		    xtype: 'toolbar',
		    dock: 'top',
		    items: [
		    	{
		            xtype: 'datefield',
		            anchor: '100%',
		            labelWidth: 30,
		            fieldLabel: SuppAppMsg.purchaseOrderDesde,
		            id: 'fromDateGrid',
		            margin:'10 0 0 10',
		            dateFormat: 'Y-M-d',
		            maxValue: new Date()
		        },{
		            xtype: 'datefield',
		            anchor: '100%',
		            labelWidth: 30,
		            margin:'10 0 0 30',
		            dateFormat: 'Y-M-d',
		            fieldLabel: SuppAppMsg.purchaseOrderHasta,
		            id: 'toDateGrid',
		            maxValue: new Date()
		        }, {
		            xtype: 'combo',
		            fieldLabel: SuppAppMsg.purchaseOrderType,
		            labelWidth : 40,
					width:250,
		            store: types,
		            queryMode: 'local',
		            displayField: 'type',
		            valueField: 'name',
		            margin:'10 0 0 30',
					id : 'logType'
		        },
			 {
		        xtype: 'button',
		        margin:'10 0 0 30',
		        text: SuppAppMsg.suppliersSearch,
		        iconCls:'icon-appgo',
		        itemId : 'refreshLog',
				id : 'refreshLog',
				action : 'refreshLog',
				listeners: {
					tap: function (button) {
						poController.refreshLog(button);
					}
				}
		    },
			 {
		        xtype: 'button',
		        margin:'10 0 0 50',
		        text: SuppAppMsg.logsExportExcel,
		        iconCls:'icon-appgo',
		        itemId : 'exportExcelLog',
				id : 'exportExcelLog',
				action : 'exportExcelLog',
				listeners: {
					tap: function (button) {
						poController.exportExcelLog(button);
					}
				}
		    }]
		},
	    getPagingContent()
	    ];
        
        this.callParent(arguments);
    }
});