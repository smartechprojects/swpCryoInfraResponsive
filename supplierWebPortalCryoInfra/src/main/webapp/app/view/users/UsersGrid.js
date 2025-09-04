Ext.define('SupplierApp.view.users.UsersGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.usersGrid',
	id: 'idUsersGrid',
	forceFit: true,
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
	viewConfig : {
		stripeRows : true,
		style : {
			overflow : 'auto',
			overflowX : 'hidden'
		}
	},
	initComponent : function() {
		
		var userController = SupplierApp.app.getController("SupplierApp.controller.Users");

		this.columns = [ {
			text : SuppAppMsg.suppliersName,
			width : 200,
			dataIndex : 'name'
		},{
			text : SuppAppMsg.usersUser,
			width : 120,
			dataIndex : 'userName'
		}, {
			text : SuppAppMsg.usersRole,
			width : 100,
			dataIndex : 'userRole.id',
			renderer:function (value,metaData,record,row,col,store,gridView){
           	 return record.data.userRole.strValue1;
			 }
		}, {
			text : SuppAppMsg.usersUserType,
			width : 100,
			dataIndex : 'userType.id',
			renderer:function (value,metaData,record,row,col,store,gridView){
           	 return record.data.userType.strValue1;
			 }
		},{
			text : SuppAppMsg.usersSupplier,
			width : 100,
			dataIndex : 'addressNumber'
		},{
			text : SuppAppMsg.usersMainUser,
			width : 100,
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
			width : 50,
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
			width : 50,
			dataIndex : 'openOrders'
		},{
			hidden:true,
			width : 50,
			dataIndex : 'logged'
		},{
			hidden:true,
			width : 50,
			dataIndex : 'agreementAccept'
		}  ];


		this.tbar = [ {
			name : 'searchUsers',
			itemId : 'searchUsers',
			emptyText : SuppAppMsg.suppliersSearch,
			xtype : 'trigger',
			width : 400,
			margin: '5 0 10 0',
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