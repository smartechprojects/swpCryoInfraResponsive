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

		
		this.tbar = {
			    padding: 0,
			    margin: '0 0 5 0',
			    items: [{
			        name : 'searchCompany',
			        itemId : 'searchCompany',
			        emptyText : SuppAppMsg.suppliersSearch,
			        xtype : 'trigger',
			        width: 300,
			        triggerCls : 'x-form-search-trigger',
			        onTriggerClick : function(e) {
          				companyController.loadSearchList(this, this.getValue());
          			},
          			enableKeyEvents : true,
          			listeners : {
			            specialkey : function(field, e) {
			                if (e.ENTER === e.getKey()) field.onTriggerClick();
			            }
			        }
			    }]
			};
		
        /*this.dockedItems = [
            {
              xtype: 'toolbar',
              dock: 'top',
              items: [
            	  {
          			name : 'searchCompany',
          			itemId : 'searchCompany',
          			emptyText : SuppAppMsg.suppliersSearch,
          			xtype : 'trigger',
          			maxWidth : 400,
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

              ]},
              getPagingContent()
      ];*/
		

		this.callParent(arguments);
	}
});