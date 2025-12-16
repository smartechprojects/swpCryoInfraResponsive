Ext.define('SupplierApp.view.main.Main', {
    extend: 'Ext.tab.Panel',
    xtype: 'app-main',
    bodyPadding: 10,
    tabPosition: 'left',
    tabRotation: 0,
    tabBar: {
        style: {
            'background-color': '#00306E'
           // 'overflow-y': 'auto',  // ✅ por compatibilidad extra
           // 'max-height': '100vh',  // ✅ limita el alto al viewport visible
        },
        items: [
            {
                xtype: 'button',
                iconCls: 'fa fa-compress',
                margin: '28 0 0 23',
                height:30,
                iconAlign: 'left',
                textAlign : 'left',
                width: 50,
                style: {
                    border: 'none',
                    boxShadow: 'none',
                    background: '#00306E'
                },
                
	            handler: function() {
	            	var panel = this.up('tabpanel');
	                var tWidth = panel.tabBar.getWidth();
	                if (tWidth >= 100) {
	                panel.tabBar.width = 46;
	 	            panel.updateLayout();
	                } else {
	                    panel.tabBar.width = 220;
	                    panel.updateLayout();
	                }
	
	            }
            }
        ]
        
    },

    listeners: {
        afterrender: function(form) {
            hidePreloader();
        },
        tabchange: function (tabPanel, tab) {
        	
        	if(tab.id == 'tabSuppliersId') tabChgn = 'suppliers';
        	
        	if(tab.id == 'tabUsersId'){//Carga valores de combos
            	var roleCombo = Ext.getCmp('usersRoleCombo');
        		roleCombo.store.load();
        		roleCombo.store.reload();
        		
            	var typeCombo = Ext.getCmp('userTypeCombo');
            	typeCombo.store.load();
            	typeCombo.store.reload();
        	}
        	
        	if(tab.id == 'tabHelpId'){
        		
        		if ("".match(numeroUsuario)){
            		link = "https://servicios.cryoinfra.com.mx/scip/default.aspx?nombreUsuario="+displayName+"&numeroUsuario="+userName+"&correo="+userEmail+"&telefono="+telefono;
                	console.log("link: " + link);
            	} else {
                	link = "https://servicios.cryoinfra.com.mx/scip/default.aspx?nombreUsuario="+displayName+"&numeroUsuario="+numeroUsuario+"&correo="+userEmail+"&telefono="+telefono;
                	console.log("link: " + link);
            	}
        		 window.open(link, '_blank');
        		 tabPanel.setActiveTab(0);
        	}
        	
        	
        	
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
        'SupplierApp.controller.PurchaseOrder',
        'SupplierApp.controller.PlantAccess',
         'SupplierApp.controller.Token',/*,
        'SupplierApp.controller.NonComplianceSupplier',
        'SupplierApp.controller.OutSourcing',
        'SupplierApp.controller.CodigosSAT',
        'SupplierApp.controller.PurchaseOrder',
        'SupplierApp.controller.FiscalDocuments',
        'SupplierApp.controller.Invoices',
        'SupplierApp.controller.Approval'*/
        
    ],
    
    ui: 'navigation',
    scrollable: false, 
    header: {
        margin: '0 0 0 0',
        height:80,
        style: { backgroundColor: '#FFFFFF', padding: '0px' },
        //title: '<img src="resources/images/CryoInfra-logo-gris.png" style="max-width: 60%; display: block; height: auto;">',
        title: '<img src="resources/images/CryoInfra-logo.png" style="height:70px; width:auto; object-fit:contain; display:block;">',
        tabConfig: {
            width: 220,
            textAlign: 'left'   // ayuda pero no es suficiente sin el CSS
        },

        layout: { type: 'hbox', align: 'left', pack: 'start' },
        items: [
        	 {
                xtype: 'image',
                src: (navigator.language || navigator.userLanguage).startsWith('es') 
                ? 'resources/images/hdr-logo.png' 
                : 'resources/images/hdr-logo-en.png',
                margin: '25 0 0 0',
                height: 25,
                width: 230,
                cls: 'header-user-info-container',
                columnWidth: 1
            },{
                xtype: 'tbspacer', 
                flex: 1,
                columnWidth: 1
            },{
                xtype: 'container',
                layout: 'vbox',
                margin: '25 100 10 0',
                defaults: { xtype: 'label', style: 'color:#3F484D;' },
                items: [
                    { itemId: 'displayNameLabel', html: '', margin: '0 0 5 0',style: 'font-weight:bold;font-size:1.1em;color:#000;' },
                    { itemId: 'userInfoLabel', html: '', flex: 1 ,style: 'font-weight:bold;font-size:1.1em;color:#000;'},
                    { itemId: 'envLabel', html: '', flex: 1 ,style: 'font-size:1em;color:red;'}
                ],
                columnWidth: 1
            },
            {
                xtype: 'image',
                src: 'resources/images/logout-icon-red.png',
                style: 'width:25px;height:25px;',
                margin: '30 55 0 0',
                cls: 'image-grow',
                columnWidth: 1,
                listeners: {
                    render: function(img) {
                        // Cambia el cursor al pasar sobre la imagen
                        //img.getEl().setStyle('cursor', 'pointer');

                        img.getEl().on('click', function() {
                            window.location.href = 'j_spring_security_logout';
                        });
                    }
                }
            }



        ],
        listeners: {
            afterrender: function(header) {
            	var showItems = false;
            	var userDesc = "";

            	if(role == 'ROLE_SUPPLIER'){
                	if(isMainSupplierUser){
                		userDesc = SuppAppMsg.userDescMainSupplierUser;
                	} else {
                		userDesc = SuppAppMsg.userDescSubuserSupplierUser;
                	}
            	}
            	
            	if(multipleRfc != "empty"){
            		multipleRfc = multipleRfc.replace(/&#034;/g,'"');
            		var rfcList = Ext.JSON.decode(multipleRfc);
            		
            		var msg = 'Nuestro sistema ha identificado que tiene mútiples cuentas asociadas con el mismo RFC. <br /><br /> A continuación se muestra la lista de las cuentas disponibles. <br /><br />Debe seleccionar una de ellas para continuar.<br /><br /><select id="supplierRfcId" style="width:550px;height:20px;">';
            		for (var i = 0; i < rfcList.length; i++) {
            			msg = msg + '<option value="' + rfcList[i].addresNumber +'">' + rfcList[i].addresNumber + ' - ' + rfcList[i].rfc + ' - ' + rfcList[i].razonSocial + '</option>';
            		}
            		msg = msg + '</select><br />';
                	
                	Ext.MessageBox.show({
                	    title: 'Múltiples cuentas asociadas',
                	    msg: msg,
                	    buttons: Ext.MessageBox.OKCANCEL,
                	    fn: function (btn) {
                	        if (btn == 'ok') {
                	            addresNumber = Ext.get('supplierRfcId').getValue();
                	        	Ext.MessageBox.show({
                	        	    title: 'Aviso',
                	        	    msg: 'Su sesión se reiniciará con la cuenta: ' + addresNumber + '.',
                	        	    buttons: Ext.MessageBox.OKCANCEL,
                	        	    fn: function (btn) {
                	        	        if (btn == 'ok') {
                	        	        	var box = Ext.MessageBox.wait('Redireccionando a la nueva cuenta. Espere unos segundos', 'Redirección de cuentas');
                	        	            location.href = "homeUnique.action?userName=" + addresNumber;
                	        	        }else{
                	        	        	location.href = "j_spring_security_logout";
                	        	        }
                	        	    }
                	        	});
                	        }else{
                	        	location.href = "j_spring_security_logout";
                	        }
                	    }
                	});
                	
            	}else{
            		showItems = true;
            	}
                                
                header.down('#displayNameLabel').setHtml(displayName);
                header.down('#userInfoLabel').setHtml(SuppAppMsg.headerAccount + ': ' + userName + ' <b>' + userDesc + '</b>');
                header.down('#envLabel').setHtml('Ambiente TEST');
            }
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
                'background-color': '#00306E',
                'text-align':'left',
                'width':'220px'//,
               // 'min-height': '60px'
            }
        }
    },
    
    
 
    items: [{
    	xtype : 'panel',
        title: '',
        iconCls: 'fa fa-home',
        id:'tabHomeId',
        scrollable: true,
        //html:"<div style='display: flex;justify-content: center;vertical-align: middle;padding-top:100px;'><img src='resources/images/CryoInfra-logo-gris.png' style='max-width: 80%; height: auto;' alt='Logo CryoInfra'> </div>"
        listeners: {
            afterrender: function(panel) {
            
                 function updateHtml(width){
                     if(width >= 768){ // Pantallas grandes
			           var homeContent = "<table class='center-table'>" +
			                    	"   <tr style='height:45%;'>" +
			                    	"        <td style=' vertical-align: bottom;'><img src='resources/images/CryoInfra-logo-gris.png' style='height:35%;width:40%;'/></td>" +
			                    	"    </tr>" +
			                    	"   <tr>" +
			                    	"        <td style='vertical-align: top;padding:30px;line-height: 1.5;font-size:1.2em;'>" + SuppAppMsg.homeHeaderMessage + "</td>" +
			                    	"    </tr>" +
			                    	"</table>";
                     
                     panel.update(homeContent);
                      
                     } else { // Pantallas pequeñas
                    	 var homeContent = "<div style='display: grid; place-items: center;'>" +
                         "<div style='width: 50%; font-size:14px; color: black;padding: 10px; margin: 10px;'>" +
                         "<div style='display: grid; place-items: center; padding: 10px; margin: 10px;'>" +
                             "<img src='resources/images/CryoInfra-logo-gris.png' width='40%' height='80%' style='align: center;'>" +
                         "</div>" +
                         "<div style='display: grid; place-items: center; padding: 10px; margin: 40px;'>" +
                             "<span style='text-align: justify; font-family: Arial, Helvetica, sans-serif; font-size:14px; line-height: 1.5;'>" +
                                 "<p style='padding-left: 10px;'>" + SuppAppMsg.homeHeaderMessage + "</p>" +
                             "</span>" +
                         "</div>" +                            
                         "</div>" +
                         "</div>";
                     
                     panel.update(homeContent);
                     }
            	
                 }
                 
              // Inicial
                 updateHtml(panel.getWidth());

                 // Escucha cambios de tamaño
                 panel.on('resize', function(h, width){
                	 updateHtml(width);
                 });
            }
        }
    },{
        xtype: 'panel',
   		hidden:true,
		layout:'fit',
		itemId:'tabTokenTab',
		iconCls: 'fa fa-user-plus', 
        items:[
        	{
            	xtype : 'tokenPanel',
        		id:'tabTokenId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },{
        xtype: 'panel',
		layout:'fit',
		itemId:'tabSuppliersTab',
    	iconCls: 'fa fa-share-alt',
    	hidden:true,
        items:[
        	{
        		xtype : 'supplierPanel',
        		id:'tabSuppliersId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },{
    	xtype : 'supplierPanel',
    	id:'tabSuppliersMenuId',
		title : '',
		tabConfig: {
            tooltip: 'This is the quick hint for Tab 1' // The tooltip
        },
		style: 'background-color: #00306E;',
		hidden:role=='ROLE_ADMIN' || role == 'ROLE_PURCHASE' || role == 'ROLE_ADMIN_PURCHASE' || role=='ROLE_PURCHASE_IMPORT' || role=='ROLE_CXP' || role=='ROLE_CXP_IMPORT' || role=='ROLE_MANAGER' || role=='ROLE_TAX'?false:true,
		tabConfig: {
            iconCls: 'fa fa-solid fa-bars',
            listeners: {
                click: function(tab, e) {
                    e.stopEvent(); 
                    var tab = this.up();
                    var tabs = tab.up();
                    var token = tabs.child('#tabTokenTab');
                    var suppliers = tabs.child('#tabSuppliersTab');
                    var approvals = tabs.child('#approvalPanelTab');
                    var approvalSearch = tabs.child('#approvalSearchPanelTab');
                    var supplierPayment = tabs.child('#paymentsSuppliersPanelTab');
                    
                    
                    var xy = this.getXY();
                    xy[1]=xy[1] + 50;
                    var menu = Ext.create('Ext.menu.Menu', {
                    	style: 'width:900px;margin-left:1px;border: none;box-shadow: none;background: none;background-color: #00306E;font-family: Poppins-Regular, sans-serif !important;',
                    	border: false,
                    	bodyStyle: 'background-color: transparent;border: none;box-shadow: none;background: none;',
                        items: [
                        	{ 
                            	text: SuppAppMsg.tabProveedores ,
                            	iconCls: 'x-fa fa-share-alt',
                                listeners: {
                                    click: function(){
                                    	 tabs.setActiveTab(suppliers);
                                    }
                                },
                            	style: 'margin-left:40px;background-color: #00306E;padding:2px;width:182px;',
                            },
                        	{ 
                            	text: SuppAppMsg.tabToken ,
                            	iconCls: 'x-fa fa-user-plus',
                                listeners: {
                                    click: function(){
                                    	 tabs.setActiveTab(token);
                                    }
                                },
                            	style: 'margin-left:40px;background-color: #00306E;padding:2px;width:102px;',
                            },
                            { 
                            	text: SuppAppMsg.tabApproval ,
                            	iconCls: 'x-fa fa-check-circle',
                                listeners: {
                                    click: function(){
                                    	 tabs.setActiveTab(approvals);
                                    }
                                },
                            	style: 'margin-left:40px;background-color: #00306E;padding:2px;width:102px;',
                            },
                            { 
                            	text: SuppAppMsg.tabSearchApproval ,
                            	iconCls: 'x-fa fa-tasks',
                                listeners: {
                                    click: function(){
                                    	 tabs.setActiveTab(approvalSearch);
                                    }
                                },
                            	style: 'margin-left:40px;background-color: #00306E;padding:2px;width:178px;',
                            },
                            { 
                            	text: SuppAppMsg.paymentsSuppliers ,
                            	iconCls: 'x-fa fa-money',
                                listeners: {
                                    click: function(){
                                    	 tabs.setActiveTab(supplierPayment);
                                    }
                                },
                            	style: 'margin-left:40px;background-color: #00306E;padding:2px;width:102px;',
                            }
                        ]
                    });
                    menu.showAt(xy); // Show menu at mouse position
                }
            }
		}
	},{
        xtype: 'panel',
		layout:'fit',
		itemId: 'purchaseOrderPanelTab',
		iconCls: 'fa fa-list-alt',
		id:'tabPurchaseOrderPanelMenu',
		title:'',
		hidden:role=='ROLE_ADMIN' || role=='ROLE_MANAGER' || role=='ROLE_PURCHASE_READ' || role=='ROLE_SUPPLIER' || role=='ROLE_AUDIT_USR' ?false:true,
        items:[
        	{
        		xtype : 'purchaseOrderPanel',
        		id:'tabPurchaseOrderPanelId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },{
        xtype: 'panel',
		layout:'fit',
		itemId: 'fiscalDocumentsPanelTab',
		iconCls: 'fa fa-file-text',
		id:'tabFiscalDocumentsPanelMenu',
		title:'',
		hidden:role=='ROLE_ADMIN' || role=='ROLE_MANAGER' || role=='ROLE_PURCHASE_READ'?false:true,
        items:[
        	{
        		xtype : 'fiscalDocumentsPanel',
        		id:'tabFiscalDocumentsPanelId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },{
        xtype: 'panel',
		layout:'fit',
		itemId: 'freightApprovalPanel',
		id:'tabFreightApprovalPanelMenu',
		iconCls: 'fa fa-truck',
		title:'',
		hidden:role=='ROLE_ADMIN' || role=='ROLE_MANAGER' || role=='ROLE_PURCHASE_READ'|| Flete=='si'?false:true,
        items:[
        	{
        		xtype : 'freightApprovalPanel',
        		id:'tabFreightApprovalId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },{
        xtype: 'panel',
		layout:'fit',
		itemId: 'plantAccessPanelTab',
		id:'plantAccessPanelMenu',
		iconCls: 'fa fa-industry',	
		title:'',
		hidden:role=='ROLE_ADMIN' || role=='ROLE_PURCHASE_READ' || role=='ROLE_MANAGER' || role=='ROLE_SUPPLIER'?false:true,
        items:[
        	{
        		xtype : 'plantAccessPanel',
        		id:'tabPlantAccessPanelId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },{
        xtype: 'panel',
		layout:'fit',
		itemId: 'taxVaultPanelTab',
		id:'taxVaultPanelMenu',
		iconCls: 'fa fa-lock',	
		title:'',
		hidden:role=='ROLE_ADMIN' || role=='ROLE_TAX' || role=='ROLE_TREASURY' || role=='ROLE_ACCOUNTING'
			|| role=='ROLE_MANAGER' || role=='ROLE_FISCAL_USR' || role=='ROLE_SUPPLIER' || role=='ROLE_PURCHASE_READ'
			|| role=='ROLE_FISCAL_PRD'|| role=='ROLE_BF_ADMIN' || role=='ROLE_AUDIT_USR' ?false:true,
        items:[
        	{
        		xtype : 'taxVaultPanel',
        		id:'tabTaxVaultPanelId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },
	{
        xtype: 'panel',
		layout:'fit',
		itemId:'approvalPanelTab',
		iconCls: 'fa-check-circle',
    	hidden:true,
        items:[
        	{
        		xtype : 'approvalPanel',
        		id:'tabApprovalPanelId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },
	{
        xtype: 'panel',
		layout:'fit',
		itemId:'approvalSearchPanelTab',
		iconCls: 'fa-tasks',
    	hidden:true,
        items:[
        	{
        		xtype : 'approvalSearchPanel',
        		id:'tabApprovalSearchPanelId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },
    {
        xtype: 'panel',
		layout:'fit',
		itemId: 'paymentsSuppliersPanelTab',
		iconCls: 'fa-money',
    	hidden:true,
        items:[
        	{
        		xtype : 'paymentsSuppliersPanel',
        		id:'tabPaymentsSuppliersPanelId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },
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
    }*/
	{
        xtype: 'panel',
		layout:'fit',
		itemId:'udcPanelTab',
		id:'udcPanelMenu',
		iconCls: 'fa-cog',
    	hidden:true,
        items:[
        	{
        		xtype : 'udcPanel',
        		id:'tabUdcId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },{
        xtype: 'panel',
		layout:'fit',
		itemId:'usersPanelTab',
		iconCls: 'fa-users',
    	hidden:true,
        items:[
        	{
        		xtype : 'usersPanel',
        		id:'tabUsersId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },{
        xtype: 'panel',
		layout:'fit',
		itemId:'companyPanelTab',
		iconCls: 'fa-building',
    	hidden:true,
        items:[
        	{
        		xtype : 'companyPanel',
        		id:'tabCompanyId',
                title: '',
                titleAlign: 'center'
        	}
        ]
    },{
        xtype: 'form',
        title: SuppAppMsg.tabInfoSupplier,
        iconCls: 'fa-user',
        id: 'tabSupplierProfileId',
        hidden: role == 'ROLE_SUPPLIER' ? false : true,
        bodyStyle: 'padding:5px 5px 0',
        scrollable: true,
        items: [{
            xtype: 'supplierForm',
            //height: 490,
            autoScroll: true,
            id: 'supplierFormId'
        }],
        listeners: {
            afterrender: function(panel) {
                // Cargar datos del perfil del proveedor si existe
                if(supplierProfile != null){
                    var form = Ext.getCmp('supplierFormId');
                    form.loadRecord(supplierProfile);
                    
                    var fileList = supplierProfile.data.fileList;
                    var hrefList = "";
                    var r2 = [];
                    var href = "";
                    var fileHref = "";

                    var r1 = fileList.split("_FILE:");
                    var inx = r1.length;
                    for (var index = inx - 1; index >= 0; index--) {
                        r2 = r1[index].split("_:_");
                        if(r2[0] != ""){
                            href = "documents/openDocument.action?id=" + r2[0];
                            
                            var typeDoc = "";
                            if(r2[1].includes("*1*")){
                                r2[1] = r2[1].replace("*1*", '');
                                typeDoc = SuppAppMsg.supplierForm36;
                            } else if(r2[1].includes("*2*")){
                                r2[1] = r2[1].replace("*2*", '');
                                typeDoc = SuppAppMsg.supplierForm37;
                            } else if(r2[1].includes("*3*")){
                                r2[1] = r2[1].replace("*3*", '');
                                typeDoc = SuppAppMsg.supplierForm38;
                            } else if(r2[1].includes("*4*")){
                                r2[1] = r2[1].replace("*4*", '');
                                typeDoc = SuppAppMsg.supplierForm39;
                            } else if(r2[1].includes("*5*")){
                                r2[1] = r2[1].replace("*5*", '');
                                typeDoc = SuppAppMsg.supplierForm40;
                            }
                            
                            fileHref = typeDoc + "** <a href='" + href + "' target='_blank'>" + r2[1] + "</a> ||" + r2[2];
                            hrefList = "<p>" + hrefList + fileHref + "</p>";
                        }
                    } 
                    Ext.getCmp('hrefFileList').setValue(hrefList);
                }
            }
        }
    },
    {
        title: 'Configuración',
        html: '',
        id:'tabConfigId',
        hidden: role !== 'ROLE_ADMIN',
        tabConfig: {
            iconCls: 'fa  fa-solid fa-bars',
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
                    	style: 'margin-left:1px;border: none;box-shadow: none;background: none;background-color: #00306E;font-family: Poppins-Regular, sans-serif !important;',
                    	border: false,
                    	bodyStyle: 'background-color: transparent;border: none;box-shadow: none;background: none;',
                        items: [
                            { 
                            	text: SuppAppMsg.tabUsers ,
                            	iconCls: 'x-fa fa-users',
                            	hidden:role=='ROLE_ADMIN'?false:true,
                                listeners: {
                                    click: function(){
                                    	 tabs.setActiveTab(users);
                                    }
                                },
                            	style: 'margin-left:40px;background-color: #00306E;padding:2px;width:102px;',
                            },
                            { 
                            	text: SuppAppMsg.tabUDC,
                            	iconCls: 'x-fa fa-cog',
                            	hidden:role=='ROLE_ADMIN'?false:true,
                            	style: 'margin-left:40px;background-color: #00306E;padding:2px;',
                                listeners: {
                                    click: function(){
                                    	 tabs.setActiveTab(udc);
                                    }
                                }
                            },
                            { 
                            	text: SuppAppMsg.companys,
                            	iconCls: 'x-fa fa-building',
                            	hidden:role=='ROLE_ADMIN'?false:true,
                            	style: 'margin-left:40px;background-color: #00306E;padding:2px;',
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
    },{
    title: 'Ayuda',
	xtype : 'panel',
	id:'tabHelpId',
	iconCls: 'fa fa-question',
    }
	]
});
