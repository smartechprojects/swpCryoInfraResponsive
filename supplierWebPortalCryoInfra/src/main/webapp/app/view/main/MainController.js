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
        Ext.getCmp('tabSuppliersId').setTitle( SuppAppMsg.tabProveedores);
        Ext.getCmp('tabApprovalPanelId').setTitle( SuppAppMsg.tabApproval);
        Ext.getCmp('tabApprovalSearchPanelId').setTitle( SuppAppMsg.tabSearchApproval);
        Ext.getCmp('tabPaymentsSuppliersPanelId').setTitle( SuppAppMsg.paymentsSuppliers);
        Ext.getCmp('tabTaxVaultPanelId').setTitle( SuppAppMsg.tabtaxVault);
        Ext.getCmp('tabFreightApprovalId').setTitle( SuppAppMsg.tabFreightApproval);
        Ext.getCmp('tabFiscalDocumentsPanelId').setTitle( SuppAppMsg.tabFiscalDocuments);
        Ext.getCmp('tabPurchaseOrderPanelId').setTitle( SuppAppMsg.tabPurchaseOrder);
        
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
