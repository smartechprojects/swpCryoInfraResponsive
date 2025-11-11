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
        enableTextSelection: true,
        markDirty: false,
        listeners: {
            refresh: function (view) {
                var grid = view.up('grid');
                var viewEl = view.getEl();
                if (!grid || !viewEl) return;

                Ext.defer(function () {
                    // Ajustar tamaño base de columnas por contenido
                    grid.columns.forEach(function (col) {
                        if (col.autoSize) col.autoSize();
                        else if (col.autoSizeColumn) col.autoSizeColumn();

                        var headerText = col.text || '';
                        var headerEl = col.el;
                        if (headerEl && headerText) {
                            var textWidth = Ext.util.TextMetrics.measure(headerEl, headerText).width + 20;
                            if (textWidth > col.getWidth()) {
                                col.setWidth(textWidth);
                            }
                        }
                    });

                    // Calcular espacio disponible visible del grid
                    var totalColumnWidth = 0;
                    grid.columns.forEach(function (col) {
                        if (!col.hidden) totalColumnWidth += col.getWidth();
                    });

                    // Obtener ancho real del contenedor visible (no del grid)
                    var gridViewWidth = viewEl.getWidth();
                    var scrollbarWidth = grid.getVerticalScrollerWidth ? grid.getVerticalScrollerWidth() : 0;
                    var availableWidth = gridViewWidth - scrollbarWidth;

                    // Si sobra espacio, redistribuir proporcionalmente
                    if (availableWidth - totalColumnWidth > 20) {
                        var extraWidth = availableWidth - totalColumnWidth;
                        var visibleCols = grid.columns.filter(function (c) { return !c.hidden; });
                        var addPerCol = extraWidth / visibleCols.length;

                        Ext.suspendLayouts();
                        visibleCols.forEach(function (col) {
                            col.setWidth(col.getWidth() + addPerCol);
                        });
                        Ext.resumeLayouts(true);
                    }
                }, 200);
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
            maxWidth: 140,
            dataIndex: 'intValue'
        },{
            text     : 'boolValue',
            flex: 0.5,
            maxWidth: 140,
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
//            width: 125,
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