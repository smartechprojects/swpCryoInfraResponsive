Ext.define('SupplierApp.view.company.CompanyGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.companyGrid',
	id: 'idCompanyGrid',
	loadMask : true,
	frame : false,
	border : false,
    forceFit: true,
	cls: 'extra-large-cell-grid', 
	store : {
		type:'company'
	},
    dockedItems: [
    	getPagingContent()
    ],
	scroll : true,
	viewConfig : {
		stripeRows : true,
		style : {
			overflow : 'auto',
			overflowX : 'hidden'
		}
	},
	initComponent : function() {
		
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