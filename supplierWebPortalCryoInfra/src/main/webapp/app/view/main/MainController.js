/**
 * This class is the controller for the main view for the application. It is specified as
 * the "controller" of the Main view class.
 *
 * TODO - Replace this content of this view to suite the needs of your application.
 */
Ext.define('SupplierApp.view.main.MainController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.main',
    
    init: function() {
        var me = this;
        Ext.getCmp('tabHomeId').setTitle(SuppAppMsg.tabInicio);
        Ext.getCmp('tabSuppliersMenuId').setTitle( SuppAppMsg.tabProveedoresMenu);
        Ext.getCmp('taxVaultPanelMenu').setTitle( SuppAppMsg.tabtaxVault);
        Ext.getCmp('tabFreightApprovalPanelMenu').setTitle( SuppAppMsg.tabFreightApproval);
        Ext.getCmp('tabFiscalDocumentsPanelMenu').setTitle( SuppAppMsg.tabFiscalDocuments);
        Ext.getCmp('tabPurchaseOrderPanelMenu').setTitle( SuppAppMsg.tabPurchaseOrder);
        Ext.getCmp('plantAccessPanelMenu').setTitle( SuppAppMsg.tabplantaccess);
        Ext.getCmp('tabSupplierProfileId').setTitle( SuppAppMsg.tabInfoSupplier);
        
        Ext.getCmp('tabTokenId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;"">' + SuppAppMsg.tabToken + '</span>');
        Ext.getCmp('tabSuppliersId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.tabProveedores + '</span>');
        Ext.getCmp('tabApprovalPanelId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.tabApproval + '</span>');
        Ext.getCmp('tabApprovalSearchPanelId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.tabSearchApproval + '</span>');
        Ext.getCmp('tabPaymentsSuppliersPanelId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.paymentsSuppliers + '</span>');
        Ext.getCmp('tabPurchaseOrderPanelId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.tabPurchaseOrder + '</span>');
        Ext.getCmp('tabFiscalDocumentsPanelId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.tabFiscalDocuments + '</span>');
        Ext.getCmp('tabFreightApprovalId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.tabFreightApproval + '</span>');
        Ext.getCmp('tabPlantAccessPanelId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.tabplantaccess + '</span>');
        Ext.getCmp('tabTaxVaultPanelId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.tabtaxVault + '</span>');
        
        Ext.getCmp('tabUdcId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.tabUDC + '</span>');
        Ext.getCmp('tabUsersId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.tabUsers + '</span>');
        Ext.getCmp('tabCompanyId').setTitle('<span style="font-size:1.2em;color:#636D73;font-weight:bold;line-height:1.4;">' + SuppAppMsg.companys + '</span>');
        
        
         /*
        Ext.getCmp('tabnonComplianceSupplierPanelId').setTitle( SuppAppMsg.tabNonComplianceSupplier);
        Ext.getCmp('tabOutSourcingPanelId').setTitle( SuppAppMsg.outSourcingTitle);
        Ext.getCmp('tabCodigosSATPanelId').setTitle( SuppAppMsg.clavesSatTitle);
        Ext.getCmp('tabPurchaseOrderPanelId').setTitle( SuppAppMsg.tabPurchaseOrder);
        Ext.getCmp('tabFiscalDocumentsPanelId').setTitle( SuppAppMsg.tabFiscalDocuments);
        Ext.getCmp('tabInvoicesPanelId').setTitle( SuppAppMsg.tabInvoices);
        Ext.getCmp('tabApprovalSearchPanelId').setTitle( SuppAppMsg.tabSearchApproval);
        Ext.getCmp('tabApprovalPanelId').setTitle( SuppAppMsg.tabApproval);
        Ext.getCmp('tabLogDataPanelId').setTitle( SuppAppMsg.tabLogs);
        Ext.getCmp('tabNoticePrivacyId').setTitle( SuppAppMsg.tabNoticePrivacy);*/
        
       
        var privacyContent = ' <div style="text-align: justify; text-justify: inter-word;border:0px;padding:20px;"> ' +
        '<h2 style="text-align:center;">'+ SuppAppMsg.tabNoticePrivacy + '</h2>'+
        '<h2 style="text-align:center;"><a href="resources/Mexico Data Privacy Notice - Merged (Invenesa)-Effective0224 1.pdf" target="_blank""> ' + SuppAppMsg.tabNoticePrivacyMsg + '</a></h2>'+
        '<div align="center"><IMG SRC="resources/images/hdr-logo.png" style="width:50%;"></div>' +
			' </div>';
        
        //Ext.getCmp('tabNoticePrivacyId').update(privacyContent);
        
    },

});
