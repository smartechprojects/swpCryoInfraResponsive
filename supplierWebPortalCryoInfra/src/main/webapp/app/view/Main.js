Ext.define('SupplierApp.view.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.mainpanel',
    autoWidth: false,
    id:'mainpanel',
    layout: 'border',
    height:370,
    border:false,
    defaults: {
        collapsible: false,
 		labelAlign: 'top',
 		border:false,
 		hideCollapseTool: true,
    },
    initComponent: function () {

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
    	

        if(showItems){
        	
        	
        	
        	this.items = [
        	    {
                region: 'north',
                height: 90,
                border: false,
                html: "<table height='100%' width='100%'> " +
                		"<tr class='hdrTable'>" +
                		  "<td style='width:70%;'> " +
                		     "<table height='70%' width='100%'>" +               		     
                		      	"<tr>" +
                		      		"<td rowspan=3 style='height:80;width:340px;'>&nbsp;</td>" +
                		      		"<td rowspan=3 style='height:80;font-size:26px;border-left:1px solid #333;color:#000;padding-bottom:7px;'>" +
                		      			"&nbsp;" +
                		      		"</td>" +
                		      		"<td colspan=2 style='height:30;text-align:left;color:#000;font-size:17px;'>"  + displayName + "</td>" +
                		        "</tr>" +
                		        "<tr>" +
            		        		"<td style='height:30;text-align:left;'><span style='color:#666;font-size:15px;'>" +SuppAppMsg.headerAccount + ":" + userName + " <b>" + userDesc + "</b></span></td>" +
            		        		"<td style='height:30;text-align:center;'>" +
            		        			"<span style='font-size:14px;color:#000;border: 2px solid royalblue; border-radius: 5px; padding: 3px; margin: 3px;'>" +
            		        				SuppAppMsg.homePortalHelp1 + " <a href='"+link+"' target='_blank'>" + SuppAppMsg.homePortalHelp2 + "</a>" +
            		        			"</span>" +
            		        		"</td>" +
                		        "</tr>" +
                		        "<tr>" +
                		        	"<td colspan=2 style='height:20;text-align:left;'><span style='font-size:11px;color:#666;background-color:#E0DC1B;'>" + approveNotif + "</span></td>" +
                		        "</tr>" +
                		      "</table>" +
                		  "</td>" +
                          "<td width:15%; style='text-align:right;padding-left:150px;vertical-align:middle;font: 16px Tahoma, Verdana, sans-serif; color:#000;'>" + 
                          " <a href='j_spring_security_logout'  id='logoutLink' style='font-size: 14px;color: red;'>" + SuppAppMsg.approvalExit  + "</a><br /><br />" +
                          "</td>" +
                          "<td width:5%; style='text-align:right;padding-right:40px;vertical-align:middle;'>" +
                          "&nbsp;" +
                          "</td>" +
                          "</tr></table>"
            },{
        		collapsible: false,
    		    region:'center',
    		    id:'mainContent',
    		    height:500,
                border:false,
    		    items:[
    		           {
    		        	   xtype:'homeTabs'
    		           }],
    		    bodyStyle: 'padding-top:0px;background: #fff;text-align:center;'
    			}
            ];	
        }
    	

	    this.callParent(arguments);
    }
});