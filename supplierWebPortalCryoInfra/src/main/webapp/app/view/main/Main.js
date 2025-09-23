Ext.define('SupplierApp.view.main.Main', {
    extend: 'Ext.tab.Panel',
    xtype: 'app-main',
    listeners: {
        afterrender: function(form) {
            hidePreloader();
        }
    },
    controller: 'main',
    requires: [
        'Ext.plugin.Viewport',
        'Ext.window.MessageBox',
        'SupplierApp.controller.Users',
        'SupplierApp.controller.Udc',
        'SupplierApp.controller.Supplier',
        'SupplierApp.controller.Company',
        'SupplierApp.controller.Approval',
        'SupplierApp.controller.PaymentsSuppliers',
        'SupplierApp.controller.TaxVault',
        'SupplierApp.controller.FreightApproval',
        'SupplierApp.controller.FiscalDocuments',
        'SupplierApp.controller.PurchaseOrder'/*,
        'SupplierApp.controller.NonComplianceSupplier',
        'SupplierApp.controller.OutSourcing',
        'SupplierApp.controller.CodigosSAT',
        'SupplierApp.controller.PurchaseOrder',
        'SupplierApp.controller.FiscalDocuments',
        'SupplierApp.controller.Invoices',
        'SupplierApp.controller.Approval'*/
        
    ],
    
    ui: 'navigation',
    header: {
        flex:   1,
        height: 80,
        style: {
            backgroundColor: '#FFFFFF',
            padding: '0px'
        },
        layout: {
            type:  'hbox',
            align: 'stretchmax'
        },
        title: {
        	text: '<table style="width:100%;"><tr><td style="width:33%;"><img src="resources/images/hand-click.png" style="width:65%;"></td><td style="width:33%;text-align:center;padding-top:12px;"><img src="resources/images/CryoInfra-logo-gris.png" style="width:20%;"></td><td style="text-align:right;width:33%;padding-right:30px;"><img src="resources/images/profile.png" style="width:45px;padding-right:30px;"></a><img src="resources/images/help-icon.png" style="width:45px;padding-right:30px;"></a><a href="logout" class="page-link"><img src="resources/images/logout-icon.png" style="width:15px;"></a></td></tr></table>',
            flex: 1,
        }
    },

    tabRotation: 0,
    tabPosition: 'left',

    tabBar: {
    	iconAlign: 'left',
        layout: {
            align: 'stretch',
            overflowHandler: 'none'
        },
        style: {
            'background-color': '#3D72A4',
            'text-align':'left'
        }
    },
    
    responsiveConfig: {
        tall: {
            headerPosition: 'left'
        },
        wide: {
            headerPosition: 'top'
        }
    },

    defaults: {
    	iconAlign: 'left',
        plugins: 'responsive',
        responsiveConfig: {
            wide: {
                iconAlign: 'left',
                textAlign: 'left'
            },
            tall: {
                iconAlign: 'top',
                textAlign: 'center',
                width: 120
            }
        },
        tabConfig: {
        	iconAlign: 'left',
        	style: {
                'background-color': '#3D72A4',
                'text-align':'left',
                'width':'210px'//,
               // 'min-height': '60px'
            },
            listeners: {
                click: function(tab) {
                	var cls = tab.iconCls;
                	if(cls === "fa-external-link"){
                		//window.open('https://www.telvista.com/es/politicas-de-privacidad', '_blank');
                	}
                }
            }
        }
    },

    items: [{
        title: SuppAppMsg.tabInicio,
        iconCls: 'fa-home',
        id:'tabHomeId',
        html:"<div style='display: flex;justify-content: center;vertical-align: middle;padding-top:100px;'><img src='resources/images/CryoInfra-logo-gris.png'></div>"
    },{
    	xtype : 'supplierPanel',
    	iconCls: 'fa-share-alt',
		title : '',
		id:'tabSuppliersId',
		hidden:role=='ROLE_ADMIN' || role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN_PURCHASE' || role=='ROLE_PURCHASE_IMPORT' || role=='ROLE_CXP' || role=='ROLE_CXP_IMPORT' || role=='ROLE_MANAGER' || role=='ROLE_TAX'?false:true
	},{
		xtype : 'purchaseOrderPanel',
		iconCls: 'fa-list-alt',
		itemId: 'purchaseOrderPanelTab',
		id:'tabPurchaseOrderPanelId',
		title : '',
		hidden:role=='ROLE_ADMIN' || role=='ROLE_MANAGER' || role=='ROLE_PURCHASE_READ' || role=='ROLE_SUPPLIER' || role=='ROLE_AUDIT_USR' ?false:true
	},{
    	xtype : 'fiscalDocumentsPanel',
    	iconCls: 'fa-file-text',
    	itemId: 'fiscalDocumentsPanelTab',
		title : '',
		id:'tabFiscalDocumentsPanelId',
		hidden: role=='ROLE_ADMIN' ||  role=='ROLE_PURCHASE' || role=='ROLE_PURCHASE_IMPORT' || (role=='ROLE_SUPPLIER' && supplierWithoutOC == 'TRUE') ?false:true
	},{
		xtype : 'freightApprovalPanel',
		iconCls: 'fa-truck',
		itemId: 'freightApprovalPanel',
		id:'tabFreightApprovalId',
		title : '',					
		hidden:role=='ROLE_ADMIN' || role=='ROLE_MANAGER' || role=='ROLE_PURCHASE_READ'|| Flete=='si'?false:true
	},
	{
		xtype : 'taxVaultPanel',
		id:'tabTaxVaultPanelId',
		itemId: 'taxVaultPanelTab',
		iconCls: 'fa-lock',
		title : '',	
		hidden:role=='ROLE_ADMIN' || role=='ROLE_TAX' || role=='ROLE_TREASURY' || role=='ROLE_ACCOUNTING'
			|| role=='ROLE_MANAGER' || role=='ROLE_FISCAL_USR' || role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE_READ'
			|| role=='ROLE_FISCAL_PRD'|| role=='ROLE_BF_ADMIN' || role=='ROLE_AUDIT_USR' ?false:true
	},
	{
    	xtype : 'approvalPanel',
    	iconCls: 'fa-check-circle',
		id:'tabApprovalPanelId',
		itemId:'approvalPanelTab',
		title : '',
		hidden:role=='ROLE_ADMIN' || role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN_PURCHASE' || role=='ROLE_PURCHASE_IMPORT' || role=='ROLE_CXP' ||role=='ROLE_CXP_IMPORT' || role=='ROLE_MANAGER'?false:true
	},{
    	xtype : 'approvalSearchPanel',
    	iconCls: 'fa-tasks',		
		id:'tabApprovalSearchPanelId',
		itemId:'approvalSearchPanelTab',
		title : '',
		hidden:role=='ROLE_ADMIN' || role=='ROLE_TAX' || role=='ROLE_TREASURY' || role=='ROLE_PURCHASE_READ' || role=='ROLE_ACCOUNTING' || role=='ROLE_MANAGER'?false:true
	}, {
		xtype : 'paymentsSuppliersPanel',
		iconCls: 'fa-money',	
		id:'tabPaymentsSuppliersPanelId',
		itemId: 'paymentsSuppliersPanelTab',
		title : ''
	}
	/*,{
    	xtype : 'outSourcingPanel',
    	iconCls: 'fa-object-group',
		title : '',
		id:'tabOutSourcingPanelId',
		hidden:role=='ROLE_ADMIN' || role=='ROLE_3RDPARTY' || role=='ROLE_RH' || role=='ROLE_TAX' || role=='ROLE_LEGAL' || (role=='ROLE_SUPPLIER' && osSupplier == 'TRUE') || role=='ROLE_REPSE' || role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN_PURCHASE' || role=='ROLE_PURCHASE_IMPORT' ?false:true
	},{
    	xtype : 'codigosSATPanel',
    	iconCls: 'fa-tags',
		title : '',
		id:'tabCodigosSATPanelId',
		hidden:role=='ROLE_ADMIN' || role=='ROLE_TAX' || role=='ROLE_3RDPARTY'?false:true
	},{
    	xtype : 'purchaseOrderPanel',
    	iconCls: 'fa-list-alt',
		title : '',
		id:'tabPurchaseOrderPanelId',
		hidden: role=='ROLE_ADMIN' ||  role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN_PURCHASE' || role=='ROLE_PURCHASE_IMPORT' || (role=='ROLE_SUPPLIER' && supplierWithOC == 'TRUE') || role=='ROLE_CXP_IMPORT'  ?false:true
	},{
    	xtype : 'fiscalDocumentsPanel',
    	iconCls: 'fa-file-text',
		title : '',
		id:'tabFiscalDocumentsPanelId',
		hidden: role=='ROLE_ADMIN' ||  role=='ROLE_PURCHASE' || role=='ROLE_PURCHASE_IMPORT' || (role=='ROLE_SUPPLIER' && supplierWithoutOC == 'TRUE') ?false:true
	},{

    	xtype : 'invoicesPanel',
    	iconCls: 'fa-share-alt',
		title : '',
		id:'tabInvoicesPanelId',
		hidden:role=='ROLE_ADMIN' || role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN_PURCHASE' || role=='ROLE_PURCHASE_IMPORT' || role=='ROLE_CXP' || role=='ROLE_CXP_IMPORT' || role=='ROLE_MANAGER' || role=='ROLE_TAX'?false:true
	},{
    	xtype : 'approvalSearchPanel',
    	iconCls: 'fa-random',
		title : '',
		id:'tabApprovalSearchPanelId',
		hidden:role=='ROLE_SUPPLIER' || role=='ROLE_RH' || role=='ROLE_TAX' || role=='ROLE_LEGAL' || role=='ROLE_REPSE' || role=='ROLE_3RDPARTY' || 'ROLE_WS' ?true:false
	},{
    	xtype : 'approvalPanel',
    	iconCls: 'fa-exchange',
		title : '',
		id:'tabApprovalPanelId',
		hidden:role=='ROLE_ADMIN' || role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN_PURCHASE' || role=='ROLE_PURCHASE_IMPORT' || role=='ROLE_CXP' ||role=='ROLE_CXP_IMPORT' || role=='ROLE_MANAGER'?false:true
	},{
    	xtype : 'nonComplianceSupplierPanel',
    	iconCls: 'fa-exclamation-circle',
		title : '',
		id:'tabnonComplianceSupplierPanelId',
		hidden:role=='ROLE_ADMIN' || role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN_PURCHASE' || role=='ROLE_PURCHASE_IMPORT' || role=='ROLE_TAX'?false:true
	},{
		xtype : 'logDataPanel',
		title: '',
		id:'tabLogDataPanelId',
        iconCls: 'fa-file-text',
        hidden:role=='ROLE_ADMIN' || role=='ROLE_IT'?false:true
    }*/,{
		xtype : 'udcPanel',
		id:'tabUdcId',
        iconCls: 'fa-cog',
		itemId:'udcPanelTab',
        hidden:true
    },{
		xtype : 'usersPanel',
        iconCls: 'fa-users',
		id:'tabUsersId',
		itemId:'usersPanelTab',
		hidden:true
	},{
		xtype : 'companyPanel',
		iconCls: 'fa-building',
		id:'tabCompanyId',
		itemId:'companyPanelTab',
		hidden:true
		
	}/*,{
		xtype : 'panel',
		id:'tabNoticePrivacyId',
		iconCls: 'fa-external-link',
		title : '',
		html: '',
		layout:{
	        type:'fit',
	        align:'stretch',
	        pack:'start'
        }
		 
	}*/,
    {
        title: 'Configuración',
        html: '',
        tabConfig: {
            iconCls: 'fa-cog',
            listeners: {
                click: function(tab, e) {
                    e.stopEvent(); 
                    var tab = this.up();
                    var tabs = tab.up();
                    var users = tabs.child('#usersPanelTab');
                    var udc = tabs.child('#udcPanelTab');
                    var company = tabs.child('#companyPanelTab');
                    var xy = this.getXY();
                    xy[1]=xy[1] + 50;
                    var menu = Ext.create('Ext.menu.Menu', {
                    	style: 'margin-left:1px;border: none;box-shadow: none;background: none;background-color: #3D72A4;font-family: Poppins-Regular, sans-serif !important;',
                    	border: false,
                    	bodyStyle: 'background-color: transparent;border: none;box-shadow: none;background: none;',
                        items: [
                            { 
                            	text: SuppAppMsg.tabUsers ,
                            	iconCls: 'x-fa fa-users',
                                listeners: {
                                    click: function(){
                                    	 tabs.setActiveTab(users);
                                    }
                                },
                            	style: 'margin-left:40px;background-color: #3D72A4;padding:2px;width:102px;',
                            },
                            { 
                            	text: SuppAppMsg.tabUDC,
                            	iconCls: 'x-fa fa-bars',
                            	style: 'margin-left:40px;background-color: #3D72A4;padding:2px;',
                                listeners: {
                                    click: function(){
                                    	 tabs.setActiveTab(udc);
                                    }
                                }
                            },
                            { 
                            	text: SuppAppMsg.companys,
                            	iconCls: 'x-fa fa-building',
                            	style: 'margin-left:40px;background-color: #3D72A4;padding:2px;',
                                listeners: {
                                    click: function(){
                                    	 tabs.setActiveTab(company);
                                    }
                                }
                            }
                        ]
                    });
                    menu.showAt(xy); // Show menu at mouse position
                }
            }
        }
    },
    {
        title: 'TEST',
        html: 'HOLA',
    }
	]
});
