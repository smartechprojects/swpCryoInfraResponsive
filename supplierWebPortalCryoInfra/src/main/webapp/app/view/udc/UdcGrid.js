Ext.define('SupplierApp.view.udc.UdcGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.udcGrid',
    loadMask: true,
	frame:false,
	border:false,
	id:'udcgrid',
	cls: 'extra-large-cell-grid', 
	store : {
		type:'udcstore'
	},
	dockedItems: [
    	getPagingContent()
    ],
    scroll : true,
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
        this.columns = [
           {
            text     : SuppAppMsg.udcSystem,
            flex: 1,
//            width: 100,
            dataIndex: 'udcSystem'
        },{
            text     : SuppAppMsg.udcKey,
            flex: 1,
//            width: 100,
            dataIndex: 'udcKey'
        },{
            text     : 'strValue1',
            flex: 1.5,
//            width: 165,
            dataIndex: 'strValue1'
        },{
            text     : 'strValue2',
            flex: 1.5,
//            width: 150,
            dataIndex: 'strValue2'
        },{
            text     : 'intValue',
            flex: 0.5,
            minWidth: 100,
            dataIndex: 'intValue'
        },{
            text     : 'boolValue',
            flex: 0.5,
            minWidth: 100,
            dataIndex: 'booleanValue'
        },{
            text     : SuppAppMsg.udcDate,
            flex: 1,
//            width    : 100,
            dataIndex: 'dateValue',
    		renderer: function(value, metaData, record, row, col, store, gridView){
    			if(value) {
					return Ext.util.Format.date(new Date(value), 'd-m-Y');
				} else {
					return null;
				}
			}
           
        },{  
            text: 'SystemRef',
            flex: 1.25,
            minWidth: 120,
            dataIndex: 'systemRef'
        },{
            text     : 'KeyRef',
            flex: 1.10,
//            width: 110,
            dataIndex: 'keyRef'
        }];
        
      
        this.callParent(arguments);
    }
});