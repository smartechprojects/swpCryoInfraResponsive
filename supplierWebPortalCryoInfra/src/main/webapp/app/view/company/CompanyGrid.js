Ext.define('SupplierApp.view.company.CompanyGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.companyGrid',
	id: 'idCompanyGrid',
	loadMask : true,
	frame : false,
	border : false,
	cls: 'extra-large-cell-grid', 
	store : {
		type:'company'
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
	initComponent : function() {
		this.emptyText = SuppAppMsg.emptyMsg;
		var companyController = SupplierApp.app.getController("SupplierApp.controller.Company");

		this.columns = [ {
			text : SuppAppMsg.paymentTitle1,
			//width : 120,
			flex: 1,
			dataIndex : 'company'
		},{
			text : SuppAppMsg.suppliersName,
			//width : 200,
			flex: 1,
			dataIndex : 'companyName'
		},{
			text : SuppAppMsg.usersEmail,
			//width : 120,
			flex: 1,
			dataIndex : 'notificationEmail'
		},{
			text : SuppAppMsg.companys2,
			//width : 100,
			flex: 1,
			dataIndex : 'active'
		}];

		
        this.dockedItems = [
            {
              xtype: 'toolbar',
              dock: 'top',
              items: [
            	  {
          			name : 'searchCompany',
          			itemId : 'searchCompany',
          			emptyText : SuppAppMsg.suppliersSearch,
          			xtype : 'trigger',
          			//width : 400,
          			flex: 1,
          			margin: '5 0 10 0',
          			triggerCls : 'x-form-search-trigger',
          			onTriggerClick : function(e) {
          				companyController.loadSearchList(e);
          			},
          			enableKeyEvents : true,
          			listeners : {
          				specialkey : function(field, e) {
          					if (e.ENTER === e.getKey()) {
          						field.onTriggerClick();
          					}
          				}
          			}
          		}

              ]}
      ];
		

		this.callParent(arguments);
	}
});