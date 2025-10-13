Ext.define('SupplierApp.view.main.Main', {
    extend: 'Ext.tab.Panel',
    xtype: 'app-main',
    tabPosition: 'left', // o 'right', según tu diseño
    tabRotation: 0,
    tabBar: {
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        listeners: {
            afterrender: function(tb) {
                tb.getEl().setStyle({
                    'overflow-y': 'auto',
                    'max-height': 'calc(100vh - 120px)' // ajusta según el alto de tu header
                });
            }
        },
        scrollable: 'y', // 🔹 Activa el scroll vertical en la barra de tabs
        autoScroll: true
    },

    defaults: {
        bodyPadding: 10,
        scrollable: true
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
    scrollable: true, 
    header: {
        height: 120,
        style: { backgroundColor: '#FFFFFF', padding: '0px' },
        title: '<img src="resources/images/CryoInfra-logo-gris.png" style="width:75%;height:80px;">',
        layout: { type: 'hbox', align: 'middle', pack: 'space-between' },
        plugins: 'responsive',
        responsiveConfig: {
            wide: { layout: { type: 'hbox', align: 'middle', pack: 'space-between' }, height: 120 },
            tall: { layout: { type: 'vbox', align: 'stretch', pack: 'start' }, height: 180 }
        },
        items: [
            {
                xtype: 'container',
                flex: 1.5,
                layout: 'vbox',
                defaults: { xtype: 'label', style: 'color:#666; font-size:15px;' },
                items: [
                    { itemId: 'displayNameLabel', html: '', margin: '0 0 5 0' },
                    { itemId: 'userInfoLabel', html: '', flex: 1 }
                ]
            },
            {
                xtype: 'component',
                itemId: 'helpLink', // ⚡ aquí directamente
                html: '',
                flex: 1,
                plugins: 'responsive',
                responsiveConfig: {
                    wide: { hidden: false },
                    tall: { hidden: true } // ahora sí se oculta en pantallas pequeñas
                }
            },
            {
                xtype: 'image',
                src: 'resources/images/logout-icon.png',
                width: 15,
                height: 15,
                margin: '0 35 0 0',
                plugins: 'responsive',
                responsiveConfig: {
                    wide: { hidden: false },
                    tall: { hidden: true }
                },
                listeners: {
                    render: function(img) {
                        // Cambia el cursor al pasar sobre la imagen
                        img.getEl().setStyle('cursor', 'pointer');

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
            	var link = "";
            	var userDesc = "";
            	/*console.log("nombreUsuario: " + displayName);
            	console.log("numeroUsuario: " + numeroUsuario);
            	console.log("correo: " + correo);
            	console.log("telefono: " + telefono);
            	
            	console.log("userName: " + userName);
            	console.log("userEmail: " + userEmail);*/
            	
            	if ("".match(numeroUsuario)){
            		link = "https://servicios.cryoinfra.com.mx/scip/default.aspx?nombreUsuario="+displayName+"&numeroUsuario="+userName+"&correo="+userEmail+"&telefono="+telefono;
                	//console.log("link: " + link);
            	} else {
                	link = "https://servicios.cryoinfra.com.mx/scip/default.aspx?nombreUsuario="+displayName+"&numeroUsuario="+numeroUsuario+"&correo="+userEmail+"&telefono="+telefono;
                	//console.log("link: " + link);
            	}

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
                /*header.down('#helpLink').setHtml(
                    "<span style='font-size:14px; color:#000; '>" +
                    SuppAppMsg.homePortalHelp1 + " <a href='" + link + "' target='_blank'>" + SuppAppMsg.homePortalHelp2 + "</a>" +
                    "</span>"
                );*/
                
                var helpCmp = header.down('#helpLink');

                function updateHelpHtml(width){
                    if(width >= 768){ // Pantallas grandes
                        helpCmp.setHtml(
                            "<span style='font-size:14px; color:#000; border:2px solid royalblue; border-radius:5px; padding:3px; margin:3px;'>" +
                            SuppAppMsg.homePortalHelp1 + " <a href='" + link + "' target='_blank'>" + SuppAppMsg.homePortalHelp2 + "</a>" +
                            "</span>"
                        );
                    } else { // Pantallas pequeñas
                        helpCmp.setHtml(
                            "<span style='font-size:14px; color:#000;'>" +
                            SuppAppMsg.homePortalHelp1 + " <a href='" + link + "' target='_blank'>" + SuppAppMsg.homePortalHelp2 + "</a>" +
                            "</span>"
                        );
                    }
                }

                // Inicial
                updateHelpHtml(header.getWidth());

                // Escucha cambios de tamaño
                header.on('resize', function(h, width){
                    updateHelpHtml(width);
                });
            }
        }
    }




,

 //   tabRotation: 0,
  //  tabPosition: 'left',

    tabBar: {
    	iconAlign: 'left',
    	scrollable: 'y',
        layout: {
            align: 'stretch',
           // overflowHandler: 'none'
        },
        style: {
            'background-color': '#3D72A4',
            'text-align':'left',
            'overflow-y': 'auto',  // ✅ por compatibilidad extra
            'max-height': '100vh',  // ✅ limita el alto al viewport visible
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
    	xtype : 'panel',
        title: '',
        iconCls: 'fa-home',
        id:'tabHomeId',
        scrollable: true,
        //html:"<div style='display: flex;justify-content: center;vertical-align: middle;padding-top:100px;'><img src='resources/images/CryoInfra-logo-gris.png' style='max-width: 80%; height: auto;' alt='Logo CryoInfra'> </div>"
        listeners: {
            afterrender: function(panel) {
            
                 function updateHtml(width){
                     if(width >= 768){ // Pantallas grandes
                    	 var homeContent = "<div style='display: grid; place-items: center;'>" +
                         "<div style='width: 50%; font-size:14px; color: black; border: 2px solid royalblue; border-radius: 10px; padding: 10px; margin: 10px;'>" +
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
		xtype : 'tokenPanel',
		title : '',
		border : true,
		iconCls: 'fa-user-plus',
		id:'tabTokenId',
		hidden:role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true
	           
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
		hidden:role=='ROLE_ADMIN' || role=='ROLE_MANAGER' || role=='ROLE_PURCHASE_READ'?false:true
	},{
		xtype : 'freightApprovalPanel',
		iconCls: 'fa-truck',
		itemId: 'freightApprovalPanel',
		id:'tabFreightApprovalId',
		title : '',					
		hidden:role=='ROLE_ADMIN' || role=='ROLE_MANAGER' || role=='ROLE_PURCHASE_READ'|| Flete=='si'?false:true
	},{
		xtype : 'plantAccessPanel',
		itemId: 'plantAccessPanelTab',
		id:'tabPlantAccessPanelId',
		title : '',	
		iconCls: 'fa-industry',		
		hidden:role=='ROLE_ADMIN' || role=='ROLE_PURCHASE_READ' || role=='ROLE_MANAGER' || role=='ROLE_SUPPLIER'?false:true
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
	]
});
