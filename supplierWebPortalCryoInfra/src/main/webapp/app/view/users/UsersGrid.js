Ext.define('SupplierApp.view.users.UsersGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.usersGrid',
	id: 'idUsersGrid',
	//forceFit: true,
	loadMask : true,
	frame : false,
	store : {
		type:'usersstore'
	},
	dockedItems: [
    	getPagingContent()
     ],
	border : false,
	cls: 'extra-large-cell-grid', 
	//style :'border: solid #ccc 1px',
	store : 'Users',
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
		var userController = SupplierApp.app.getController("SupplierApp.controller.Users");

		this.columns = [ {
			text : SuppAppMsg.suppliersName,
			//width : 200,
			flex: 1,
			dataIndex : 'name'
		},{
			text : SuppAppMsg.usersUser,
			//width : 120,
			flex: 1,
			dataIndex : 'userName'
		}, {
			text : SuppAppMsg.usersRole,
			//width : 100,
			flex: 1,
			dataIndex : 'userRole.id',
			renderer:function (value,metaData,record,row,col,store,gridView){
           	 return record.data.userRole.strValue1;
			 }
		}, {
			text : SuppAppMsg.usersUserType,
			//width : 100,
			flex: 1,
			dataIndex : 'userType.id',
			renderer:function (value,metaData,record,row,col,store,gridView){
           	 return record.data.userType.strValue1;
			 }
		},{
			text : SuppAppMsg.usersSupplier,
			//width : 100,
			flex: 1,
			dataIndex : 'addressNumber'
		},{
			text : SuppAppMsg.usersMainUser,
			//width : 100,
			flex: 1,
			dataIndex : 'mainSupplierUser',
			renderer:function (value,metaData,record,row,col,store,gridView){
				if(value == true){
					return SuppAppMsg.usersBooleanYes;
				} else {
					return SuppAppMsg.usersBooleanNo;
				}	           	 
			},
			align: 'center'
		},{
			text : SuppAppMsg.usersActivo,
			//width : 50,
			flex: 1,
			dataIndex : 'enabled',
			renderer:function (value,metaData,record,row,col,store,gridView){
				if(value == true){
					return SuppAppMsg.usersBooleanYes;
				} else {
					return SuppAppMsg.usersBooleanNo;
				}	           	 
			},
			align: 'center'
		},{
			hidden:true,
			//width : 50,
			flex: 1,
			dataIndex : 'openOrders'
		},{
			hidden:true,
			//width : 50,
			flex: 1,
			dataIndex : 'logged'
		},{
			hidden:true,
			//width : 50,
			flex: 1,
			dataIndex : 'agreementAccept'
		}  ];


		this.tbar = [ {
			name : 'searchUsers',
			itemId : 'searchUsers',
			emptyText : SuppAppMsg.suppliersSearch,
			xtype : 'trigger',
			width:300,
			triggerCls : 'x-form-search-trigger',
			onTriggerClick : function() {
			    userController.loadSearchList(this, this.getValue());
			},
			enableKeyEvents : true,
			listeners : {
				specialkey : function(field, e) {
					if (e.ENTER === e.getKey()) {
						field.onTriggerClick();
					}
				}
			}
		} ];
		this.callParent(arguments);
	}
});