
 
Ext.Ajax.on('requestcomplete', function (conn, response, options) {
	try{
        eval( "(" + response.responseText + ')' );
    }catch(e){
    	var loginIdent = "loginSupplier";
    	var str =response.responseText.trim()
        if(str.includes(loginIdent)){
        	Ext.Msg.show({
        		   title:'Aviso',
        		   msg: 'Su sesión ha expirado. Será dirigido a la pagina de inicio para que inicie una nueva sesión.',
        		   buttons: Ext.Msg.YES,
        		   buttonText: {
                       yes: 'Enterado',
                   },
        		   fn: function (buttonValue, inputText, showConfig) {
        			   location.href = "j_spring_security_logout";
        		   },
        		   animEl: 'elId'
        		});
        	
        }
    }
   
});

Ext.apply(Ext.tip.QuickTipManager.getQuickTip(), {
	dismissDelay : 15000 
});
Ext.Loader.setConfig({
	enabled : true
});


Ext.apply(Ext.form.VTypes,{
	secPass : function ValidaRfc(rfcStr) {
		var strCorrecta;
		strCorrecta = rfcStr;

		var valid = '(?=.*[!@#&$|_=.%])[0-9a-zA-Z!@#&$|_=.%0-9]{8}$';
		var validRfc = new RegExp(valid);
		var matchArray = strCorrecta.match(validRfc);
		if (matchArray == null) {
			return false;
		} else {
			return true;
		}
	},
	secPassText : 'Especificación: Contraseña entre 8 y 20 caracteres. Alfanumérica. Debe manejar 1 o 2 caracteres especiales.',
	secPassMask : /[A-Za-z0-9,Ñ,ñ,&,!,@,#,$,|,%,:,_,=,.]/,
	rfc : function ValidaRfc(rfcStr) {
		var strCorrecta;
		strCorrecta = rfcStr;

		var valid = '[A-Z,Ñ,&]{3,4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z,0-9][A-Z,0-9][0-9,A-Z]';
		var validRfc = new RegExp(valid);
		var matchArray = strCorrecta.match(validRfc);
		if (matchArray == null) {
			return false;
		} else {
			return true;
		}
	},
	rfcText : 'RFC No valido',
	rfcMask : /[A-Za-z0-9,Ñ,ñ,&,-]/,
	celphone : function(value, field) {
		return value.replace(/[ \-\(\)]/g, '').length == 10;
	},
	daterange : function(val, field) {
		var date = field.parseDate(val);

		if (!date) {
			return false;
		}
		if (field.startDateField
				&& (!this.dateRangeMax || (date.getTime() != this.dateRangeMax
						.getTime()))) {
			var start = field.up('form').down(
					'#' + field.startDateField);
			start.setMaxValue(date);
			start.validate();
			this.dateRangeMax = date;
		} else if (field.endDateField
				&& (!this.dateRangeMin || (date.getTime() != this.dateRangeMin
						.getTime()))) {
			var end = field.up('form').down(
					'#' + field.endDateField);
			end.setMinValue(date);
			end.validate();
			this.dateRangeMin = date;
		}
		return true;
	},
	daterangeText : 'Start date must be less than end date'
});

var udcEnabledFilter = new Ext.util.Filter({
	filterFn : function(rec) {
		return rec.get('booleanValue');
	}
});


var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
		+ "wxyz0123456789+/" + "=";

function storeFactory(name, url, fields, autoLoad, extraParams, groupField,
		pageSize) {
	return new Ext.data.Store({
		autoLoad : autoLoad,
		model : modelFactory(name, fields),
		groupField : groupField ? groupField : "",
		pageSize : pageSize ? pageSize : "",
		proxy : {
			type : 'ajax',
			url : url,
			extraParams : extraParams,
			reader : {
				type : 'json',
				rootProperty : 'data'
			},
			writer : {
				type : 'json',
				writeAllFields : true,
				encode : false
			}
		}
	});
};

function storeAndModelFactory(model, name, url, autoLoad, extraParams, groupField, pageSize) {
	return new Ext.data.Store({
		autoLoad : autoLoad,
		enablePaging: true,
		model : model,
		groupField : groupField ? groupField : "",
		pageSize : pageSize ? pageSize : "",
		proxy : {
			type : 'ajax',
			url : url,
			enablePaging: true,
			extraParams : extraParams,
			reader : {
				type : 'json',
				rootProperty : 'data'
			},
			writer : {
				type : 'json',
				writeAllFields : true,
				encode : false
			}
		}
	});
};

function modelNull(v, j) {
	if (v != null) {
		if (v.id != null && v.id != "") {
			return v;
			// {id: v.id,
			// strValue1:v.strValue1};
		} else {
			return v.id;
		}
	} else {
		return "";
	}

}
function modelFactory(name, fields) {
	return Ext.define(name, {
		extend : 'Ext.data.Model',
		fields : fields
	});
};

function nullModel(v, j) {
	return (v != null ? v.id : "");
};

function getUDCStore(udcSystem, udcKey, systemRef, keyRef) {
	return new Ext.data.Store({
		fields : [ 'id', 'udcKey', 'strValue1', 'systemRef', 'keyRef', 'strValue2', 'booleanValue' ],
		autoLoad : false,
		proxy : {
			type : 'ajax',
			url : 'admin/udc/searchSystemAndKey.action',
			extraParams : {
				query:'',
				udcSystem : udcSystem,
				udcKey : udcKey,
				systemRef : systemRef,
				keyRef : keyRef
			},
			reader : {
				rootProperty : 'data',
				totalProperty : 'total',
				type : 'json'
			}
		}
	});
};

function getAutoLoadUDCStore(udcSystem, udcKey, systemRef, keyRef) {
	return new Ext.data.Store({
		fields : [ 'id', 'udcKey', 'strValue1', 'systemRef', 'keyRef', 'strValue2', 'booleanValue' ],
		autoLoad : true,
		proxy : {
			type : 'ajax',
			url : 'public/searchSystemAndKey.action',
			extraParams : {
				query:'',
				udcSystem : udcSystem,
				udcKey : udcKey,
				systemRef : systemRef,
				keyRef : keyRef
			},
			reader : {
				rootProperty : 'data',
				totalProperty : 'total',
				type : 'json'
			}
		}
	});
};

function getAutoLoadUDCStoreWithFilterStrValue2(udcSystem, filterBy) {
	return new Ext.data.Store({
		fields : [ 'id', 'udcKey', 'strValue1', 'systemRef', 'keyRef', 'strValue2', 'booleanValue' ],
		autoLoad : true,
		proxy : {
			type : 'ajax',
			url : 'public/searchSystemAndKey.action',
			extraParams : {
				query:'',
				udcSystem : udcSystem,
				udcKey : '',
				systemRef : '',
				keyRef : ''
			},
			reader : {
				rootProperty : 'data',
				totalProperty : 'total',
				type : 'json'
			}
		},
		filters: [
	         function(item) {
		         if(role === 'ROLE_ADMIN' || role === 'ROLE_SUPPLIER' || role === 'ROLE_REPSE' || role=='ROLE_PURCHASE'){
		        	 return true;
		         }else{
			         return item.data.strValue2 === filterBy; //true
		         }
	         }
	    ]
	});	
};

function getAutoLoadUDCStoreByRoleInStrValue2(udcSystem, strValue2) {
	var addedRecords=[];
	return new Ext.data.Store({
		fields : [ 'id', 'udcKey', 'strValue1', 'strValue2', 'systemRef', 'keyRef', 'booleanValue' ],
		autoLoad : true,
		proxy : {
			type : 'ajax',
			url : 'public/searchSystemByRoleInStrValue2.action',
			extraParams : {
				query:'',
				udcSystem : udcSystem,
				udcKey : '',
				strValue1 : '',
				strValue2 : strValue2,
				systemRef : '',
				keyRef : ''
			},
			reader : {
				rootProperty : 'data',
				totalProperty : 'total',
				type : 'json'
			}
		}
	});
	
};

function getCPStore() {
	return new Ext.data.Store({
		fields : [ 'id', 'codigo', 'colonia', 'tipoColonia', 'municipio', 'estado' ],
		autoLoad : false,
		pageSize : 100,
		proxy : {
			type : 'ajax',
			url : 'codigoPostal/view.action',
			extraParams : {
				query : "",
			},
			reader : {
				rootProperty : 'data',
				totalProperty : 'total',
				type : 'json'
			}
		}
	});
};

function populateObj(record, values) {
	var obj = {}, name;
	var fields = record.fields;
	Ext.Array.each(fields, function(field) {
		name = field.name;
		if (field.model) {
			var nestedValues = {};
			var hasValues = false;
			for ( var v in values) {
				if (v.indexOf('.') > 0) {
					var parent = v.substr(0, v.indexOf('.'));
					if (parent == field.name) {
						var key = v.substr(v.indexOf('.') + 1);
						nestedValues[key] = values[v];
						hasValues = true;
					}
				}
			}
			if (hasValues) {
				obj[name] = populateObj(Ext.create(field.model), nestedValues);
			}
		} else if (name in values) {
			obj[name] = values[name];
		}
	});
	return obj;
};

Ext.toggle = function(msgCt, delay) {

	function createBox(t, s) {
		return '<div class="msg"><h3>' + t + '</h3><p>' + s + '</p></div>';
	}

	return {
		msg : function(title, format) {
			if (!msgCt) {
				msgCt = Ext.DomHelper.insertFirst(document.body, {
					id : 'msg-div'
				}, true);
			}
			var s = Ext.String.format.apply(String, Array.prototype.slice.call(
					arguments, 1));
			var m = Ext.DomHelper.append(msgCt, createBox(title, s), true);
			m.hide();
			m.slideIn('t').ghost("t", {
				delay : 2500,
				remove : true
			});
		},

		init : function() {
		}
	};
}();

function openChangePasswordWindow() {
	win = new Ext.create(
			'Ext.Window',
			{
				title : GdsLims.app.messages.lblChangePassword,
				height : 150,
				id : "testWindow",
				width : 400,
				layout : 'fit',
				applyTo : 'logout',
				resizable : false,
				draggable : false,
				modal : true,
				plain : true,
				border : false,
				items : [ {
					xtype : 'panel',
					region : 'center',
					id : 'chPswPanel',
					margins : '0 0 0 0',
					items : [ {
						xtype : 'fieldset',
						margin : '5 0 0 10',
						border : false,
						defaultType : 'textfield',
						items : [
								{
									xtype : 'hidden',
									name : 'userId',
									id : 'idUser',
									value : usrPrf.id
								},
								{
									fieldLabel : GdsLims.app.messages.lblCurrentPassword,
									labelWidth : 120,
									name : 'currentPassword',
									id : 'currentPassword',
									inputType : 'password',
									allowBlank : false
								},
								{
									fieldLabel : GdsLims.app.messages.lblNewPassword,
									labelWidth : 120,
									name : 'newPassword',
									id : 'newPassword',
									inputType : 'password',
									allowBlank : false,
									margin : '10 0 0 0'
								} ]
					} ]
				} ],

				buttons : [ {
					text : GdsLims.app.messages.btnSubmit,
					formBind : true,
					iconCls : 'icon-accept',
					handler : function() {

						var identUser = Ext.getCmp('idUser').getValue();
						var oldPass = Ext.getCmp('currentPassword');
						var newPass = Ext.getCmp('newPassword');

						if (oldPass.getValue() != ""
								&& newPass.getValue() != "") {
							if (oldPass.getValue() != decode64(usrPrf.password)) {
								Ext.toggle
										.msg(
												"<span style='color:red;'>ERROR!</span>",
												'La contraseña actual no conicide. Corrija el valor de su contraseña actual y vuelva a intentarlo.');
								return false;
							}
							if (oldPass.getValue() == newPass.getValue()) {
								Ext.toggle
										.msg(
												"<span style='color:red;'>ERROR!</span>",
												'La nueva contraseña no debe ser igual a la contraseña actual.');
								return false;
							}

							Ext.Ajax
									.request({
										url : 'admin/users/changePassword.action',
										method : 'POST',
										waitMsg : 'Enviando la solicitud...',
										params : {
											userId : identUser,
											currentPassword : oldPass
													.getValue(),
											newPassword : newPass.getValue()
										},
										success : function(response) {
											win.close();
											Ext.toggle
													.msg("Operación realizada",
															'La contraseña ha sido modificada exitosamente.');
										},
										failure : function() {
											Ext.Msg
													.alert('Advertencia!',
															'El servidor a notificado un error no se ha cambiado la contraseña ');
											oldPass.setValue('');
											newPass.setValue('');
										}
									});
						} else {
							Ext.Msg.alert('Error',
									'Los campos no pueden estar vacios');
						}
					}
				} ]
			});
	win.show();
};


// Moficación para llenar combobox y no esten en vacio
// J.camacho
function setComboDefaultValue(comboBox, store) {
	store.on('load', function() {
		this.data.each(function() {
			if (this.data['booleanValue'])
				comboBox.setValue(this.data['id']);
		});
	});
	store.load();
};

function getCategoryJDEList(){
	 var store = Ext.create('Ext.data.Store', {
       fields: ['categoriaJDE'],
       data: [{ 'categoriaJDE': 'NON FOOD'},
              { 'categoriaJDE': 'FOOD'}
              ]
   });
	 return store;
};

function getBancoList(){
	 var store = Ext.create('Ext.data.Store', {
      fields: ['nombreBanco'],
      data: [{ 'nombreBanco': 'ABC Capital '},
             { 'nombreBanco': 'American Express Bank (México)'},
             { 'nombreBanco': 'Banca Afirme'},
             { 'nombreBanco': 'banca Mifel'},
             { 'nombreBanco': 'Banco Actinver'},
             { 'nombreBanco': 'Banco Ahorro Famsa'},
             { 'nombreBanco': 'Banco Autofin México'},
             { 'nombreBanco': 'Banco Azteca'},
             { 'nombreBanco': 'Banco Bancrea'},
             { 'nombreBanco': 'Banco Base'},
             { 'nombreBanco': 'Banco Compartamos'},
             { 'nombreBanco': 'Banco Credit Suisse (México)'},
             { 'nombreBanco': 'Banco del Bajio'},
             { 'nombreBanco': 'Banco Forjadores'},
             { 'nombreBanco': 'Banco Inbursa'},
             { 'nombreBanco': 'Banco Inmobiliario Mexicano'},
             { 'nombreBanco': 'Banco Interacciones'},
             { 'nombreBanco': 'Banco Invex'},
             { 'nombreBanco': 'Banco JP Morgan'},
             { 'nombreBanco': 'Banco Mercantil del Norte (Banorte)'},
             { 'nombreBanco': 'Banco Monex'},
             { 'nombreBanco': 'Banco Multiva'},
             { 'nombreBanco': 'Banco Nacional de México (Banamex)'},
             { 'nombreBanco': 'Banco Pagatodo'},
             { 'nombreBanco': 'Banco Regional de Monterrey'},
             { 'nombreBanco': 'Banco Santander (México)'},
             { 'nombreBanco': 'Banco Ve Por Mas'},
             { 'nombreBanco': 'Banco Wal-Mart de México'},
             { 'nombreBanco': 'Bancoppel'},
             { 'nombreBanco': 'Bank of America México'},
             { 'nombreBanco': 'Bank of Tokyo-Mitsubishi UFJ (México)'},
             { 'nombreBanco': 'Bankaool'},
             { 'nombreBanco': 'Bansi'},
             { 'nombreBanco': 'Barclays Bank México'},
             { 'nombreBanco': 'BBVA Bancomer'},
             { 'nombreBanco': 'CiBanco'},
             { 'nombreBanco': 'ConsuBanco'},
             { 'nombreBanco': 'Deutsche Bank México'},
             { 'nombreBanco': 'Fundación Dondé Banco'},
             { 'nombreBanco': 'HSBC México'},
             { 'nombreBanco': 'Intercam Banco'},
             { 'nombreBanco': 'Investa Bank'},
             { 'nombreBanco': 'Scotiabank Inverlat'},
             { 'nombreBanco': 'UBS Bank México'},
             { 'nombreBanco': 'Volkswagen Bank'}
             ]
  });
	 return store;
};

function getFormaPagoList(){
	 var store = Ext.create('Ext.data.Store', {
     fields: ['formaPago'],
     data: [{ 'formaPago': 'Efectivo'},
            { 'formaPago': 'Cheque nominativo'},
            { 'formaPago': 'Transferencia electrónica de fondos'},
            { 'formaPago': 'Tarjeta de crédito'},
            { 'formaPago': 'Monedero electrónico'},
            { 'formaPago': 'Dinero electrónico'},
            { 'formaPago': 'Vales de despensa'},
            { 'formaPago': 'Tarjeta de débito'},
            { 'formaPago': 'Tarjeta de servicio'},
            { 'formaPago': 'Otros'}
            ]
 });
	 return store;
};

function getTipoProductoList(){
	 var store = Ext.create('Ext.data.Store', {
    fields: ['tipoProductoServicio'],
    data: [{ 'tipoProductoServicio': 'Tipo001'},
           { 'tipoProductoServicio': 'Tipo002'},
           { 'tipoProductoServicio': 'Tipo003'},
           { 'tipoProductoServicio': 'Tipo004'},
           { 'tipoProductoServicio': 'Tipo005'},
           { 'tipoProductoServicio': 'Tipo006'},
           { 'tipoProductoServicio': 'Tipo007'},
           { 'tipoProductoServicio': 'Tipo008'},
           { 'tipoProductoServicio': 'Tipo009'},
           { 'tipoProductoServicio': 'Tipo0010'}
           ]
	 });
	 return store;
};

function getRiesgoList(){
	 var store = Ext.create('Ext.data.Store', {
   fields: ['riesgoCategoria'],
   data: [{ 'riesgoCategoria': 'BAJO'},
          { 'riesgoCategoria': 'MEDIO'},
          { 'riesgoCategoria': 'ALTO'}
          ]
	 });
	 return store;
};

function getTasaIvaList(){
	 var store = Ext.create('Ext.data.Store', {
  fields: ['tasaIva'],
  data: [{ 'tasaIva': 'SI'},
         { 'tasaIva': 'N-A'}
         ]
	 });
	 return store;
};


function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) 
        month = '0' + month;
    if (day.length < 2) 
        day = '0' + day;

    return [year, month, day].join('-');
}


function deleteDocument (docId) {
	Ext.MessageBox.show({
		title : 'Eliminación de documentos',
		msg : 'Desea eliminar el documento?',
		buttons : Ext.MessageBox.YESNO,
		width:500,
		buttonText : {
			yes : "Aceptar",
			no : "Salir"
		},
		fn : function(btn, text) {
			if (btn === 'yes') {
				var box = Ext.MessageBox.wait('Procesando. Espere unos segundos', 'Ejecución');
		    	Ext.Ajax.request({
					url : 'documents/delete.action',
					method : 'GET',
						params : {
							id:docId
						},
						success : function(response,opts) {
							var resp = Ext.decode(response.responseText);
							store.load();
							box.hide();
						},
						failure : function() {
							box.hide();
						}
					});
				
				
			}
		}
	});
	
}

function reloaddocsTaxVault(id){	
	Ext.Ajax.request({
		url : 'taxVault/listDocumentsAnex.action',
		method : 'GET',
			params : {
				start : 0,
				limit : 15,
				idFact : id
			},
			success : function(response,opts) {

				response = Ext.decode(response.responseText);
				var index = 0;
				var files = "<table style='font-size: 11px;width: 100%;height: 100%;text-align: left;'><tr><th style='background-color: whitesmoke;'>"+SuppAppMsg.taxvaultDocument+"</th><th style='text-align: left;background-color: whitesmoke;'>"+SuppAppMsg.taxvaultSize+"</th><th style='text-align: left;background-color: whitesmoke;'>"+SuppAppMsg.taxvaultType+"</th><th style='text-align: left;background-color: whitesmoke;'>"+SuppAppMsg.taxvaultAction+"</th></tr>";
				for (index = 0; index < response.data.length; index++) {
								var href = "taxVault/openDocument.action?id=" + response.data[index].id;
								var fileHref = "<td style='background-color: whitesmoke;'><a href= '" + href + "' target='_blank'>" +  response.data[index].nameFile + "</a></td>";
	                            files = files + "<tr>" + fileHref + "<td style='background-color: whitesmoke;'>" + response.data[index].size + " bytes</td><td style='background-color: whitesmoke;'>" + response.data[index].documentType  +  "</td>" + (response.data[index].nameFile.toLowerCase().endsWith(".xml")&&response.data[index].rfcEmisor!=null&&response.data[index].rfcEmisor!=undefined&&response.data[index].rfcEmisor!=''?"<td style='background-color: whitesmoke;'></td>":("<td style='background-color: whitesmoke;'><A HREF='javascript:deleteDocsTaxVault(" + response.data[index].id + ",+"+id+")'>"+SuppAppMsg.taxvaultDelete+"</A></td>")) + "</tr>";

				} 
				
				 Ext.getCmp('fileListTaxVaultHtml').update(files+"</table>");

			},
			failure : function() {
			}
		});
	
}

function reloadPeriodoFiscal(){
	Ext.Ajax.request({
		url : 'taxVault/listPeriodoFiscal.action',
		method : 'GET',
			params : {
				start : 0,
				limit : 100,
			},
			success : function(response,opts) {

				response = Ext.decode(response.responseText);
				var index = 0;
				var files = "<table style='display:block;'><tbody style='display: block; overflow-y: auto;'><tr><th>"+SuppAppMsg.taxvaultFiscalPeriods+"</th>    <th>"+SuppAppMsg.taxvaulStatus+"</th>  </tr>";
				for (index = 0; index < response.data.length; index++) {
					var fileHref = "<a target='_blank'> " +  response.data[index].intValue+ "</a>";
					var row=" <tr><td>"+fileHref+"</td><td>" +
					"<A HREF='javascript:actDesActPeriodoFiscal(" + response.data[index].id + ")'>"+ (response.data[index].booleanValue?SuppAppMsg.taxvaultEnable:SuppAppMsg.taxvaultDisable)   +"</A>"		
					"</td>";
								
							files=files+row;	
								
//	                            files = files + "> " + fileHref + " - " + "&nbsp;&nbsp;&nbsp;" + "<A HREF='javascript:actDesActPeriodoFiscal(" + response.data[index].id + ")'>"+ (response.data[index].booleanValue?SuppAppMsg.taxvaultEnable:SuppAppMsg.taxvaultDisable)   +"</A>" + "<br />";

				} 
				files=files+"</tbody></table>";
				
				 Ext.getCmp('ListTaxVaulFiscalPeriodtHtml').update(files);

			},
			failure : function() {
			}
		});
	
} 

function actDesActPeriodoFiscal(id){
	Ext.Ajax.request({
		url : 'taxVault/actDesActPeriodoFiscal.action',
		method : 'GET',
			params : {
				idFact : id
			},
			success : function(response,opts) {

				reloadPeriodoFiscal();
			},
			failure : function() {
			}
		});
	
}

function getLanguaje(){
return 	(navigator.language || navigator.userLanguage).split("-")[0];
	
}

function deleteDocsTaxVault(id,idPadre){
	Ext.Ajax.request({
		url : 'taxVault/deleteDocument.action',
		method : 'GET',
			params : {
				idFact : id
			},
			success : function(response,opts) {

				reloaddocsTaxVault(idPadre);
			},
			failure : function() {
			}
		});
	
}

function getUsersByRoleExcludeStore(role) {
	return new Ext.data.Store({
		fields : [ 'id', 'userName', 'name', 'email' ],
		autoLoad : true,
		pageSize : 100,
		proxy : {
			type : 'ajax',
			url : 'admin/users/searchByRoleExclude.action',
			extraParams : {
				query : '',
				role:role
			},
			reader : {
				rootProperty : 'data',
				totalProperty : 'total',
				type : 'json'
			}
		}
	});
};

Date.prototype.addHours = function(h) {
	  this.setTime(this.getTime() + (h*60*60*1000));
	  return this;
}

function wait(ms){
	   var start = new Date().getTime();
	   var end = start;
	   while(end < start + ms) {
	     end = new Date().getTime();
	  }
}

function getPagingContent(){
	return  {
        xtype: 'pagingtoolbar',
        dock: 'bottom',
        displayInfo: true,
        displayMsg : 'Mostrando {0} - {1} de {2}',
        emptyMsg:'Sin datos para mostrar',
        beforePageText:'Página',
        afterPageText: 'de {0}',
        firstText : 'Primer página',
        prevText : 'Anterior',
        nextText : 'Siguiente',
        lastText : 'última',
    }
}

function uuidv4() {
	  return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
	    (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
	  );
};

function validarInputIssuer(rfc,field) {
	//resultado = document.getElementById("resultadoRfc");
	
	 var resultado = null;
	
    if (field) {
        var form = field.up('form'); // form contenedor
        if (form) {
            resultado = form.down('[name=resultadoRfc]'); // displayfield
        }
    }

	if (resultado != null) {
		var valido = '';

		var rfcCorrecto = rfcValido(rfc); // ⬅️ Acá se comprueba

		if (rfcCorrecto) {
			valido = "Válido";
			//resultado.classList.add("ok");
			resultado.addCls("ok"); 
		} else {
			valido = "No válido"
			//resultado.classList.remove("ok");
			resultado.removeCls("ok");
		}

		//resultado.innerText = "Formato RFC: " + valido;
		
		 resultado.setValue("Formato RFC: " + valido);
	}
}

//Función para validar un RFC
//Devuelve el RFC sin espacios ni guiones si es correcto
//Devuelve false si es inválido
//(debe estar en mayúsculas, guiones y espacios intermedios opcionales)
function rfcValido(rfc, aceptarGenerico) {
	
	aceptarGenerico = true;
	const re = /^([A-ZÑ&]{3,4}) ?(?:- ?)?(\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])) ?(?:- ?)?([A-Z\d]{2})([A\d])$/;
	var validado = rfc.match(re);

	if (!validado) //Coincide con el formato general del regex?
		return false;

	//Separar el dígito verificador del resto del RFC
	const digitoVerificador = validado.pop(), rfcSinDigito = validado.slice(1)
			.join(''), len = rfcSinDigito.length,

	//Obtener el digito esperado
	diccionario = "0123456789ABCDEFGHIJKLMN&OPQRSTUVWXYZ Ñ", indice = len + 1;
	var suma, digitoEsperado;

	if (len == 12)
		suma = 0
	else
		suma = 481; //Ajuste para persona moral

	for (var i = 0; i < len; i++)
		suma += diccionario.indexOf(rfcSinDigito.charAt(i)) * (indice - i);
	digitoEsperado = 11 - suma % 11;
	if (digitoEsperado == 11)
		digitoEsperado = 0;
	else if (digitoEsperado == 10)
		digitoEsperado = "A";

	//El dígito verificador coincide con el esperado?
	// o es un RFC Genérico (ventas a público general)?
	if ((digitoVerificador != digitoEsperado)
			&& (!aceptarGenerico || rfcSinDigito + digitoVerificador != "XAXX010101000"))
		return false;
	else if (!aceptarGenerico
			&& rfcSinDigito + digitoVerificador == "XEXX010101000")
		return false;
	return rfcSinDigito + digitoVerificador;
}

function getUsersByRoleExcludeStore(role) {
	return new Ext.data.Store({
		fields : [ 'id', 'userName', 'name', 'email' ],
		autoLoad : true,
		pageSize : 100,
		proxy : {
			type : 'ajax',
			url : 'admin/users/searchByRoleExclude.action',
			extraParams : {
				query : '',
				role:role
			},
			reader : {
				root : 'data',
				totalProperty : 'total',
				type : 'json'
			}
		}
	});
};

function searchByRoleAprover(role,etapa) {
	return new Ext.data.Store({
		fields : [ 'id', 'userName', 'name', 'email' ],
		autoLoad : true,
		pageSize : 100,
		proxy : {
			type : 'ajax',
			url : 'admin/users/searchByRoleAprover.action',
			extraParams : {
				query : '',
				role:role,
				etapa:etapa
			},
			reader : {
				root : 'data',
				totalProperty : 'total',
				type : 'json'
			}
		}
	});
};

function hidePreloader() {
    var loading = Ext.get('loading');
    loading.fadeOut({ duration: 0.2, remove: true });
 }

function getCompanyStore() {
	
    return new Ext.data.Store({
        fields: ['company', 'companyName' ],
        autoLoad: true,
        proxy: {
            type: 'ajax',
            url: 'admin/company/getListCompany.action',
            reader: {
            	rootProperty: 'data', // Cambiado de 'root' a 'rootProperty' para Ext JS 6 y posteriores
                totalProperty: 'total',
                type: 'json'
            }
        }
    });
}

/*Funciones para ajuste de Grids en diferentes modulos*/

var GridUtils = {

	    /**
	     * Función principal de ajuste de grid
	     * @param {Ext.grid.Panel} grid - El grid a ajustar
	     * @param {boolean} isRefresh - Indica si es un refresh o resize
	     */
	    adjustGridLayout: function(grid, isRefresh) {
	        if (!grid || !grid.isXType('gridpanel')) return;
	        
	        // console.log('=== adjustGridLayout ===');
	        // console.log('Grid:', grid.id || grid.itemId);
	        
	        // Usamos Ext.defer para asegurar que el grid esté completamente renderizado
	        Ext.defer(function() {
	            // Paso 1: Autoajustar columnas según su contenido
	            // console.log('🔧 Autoajustando columnas...');
	            GridUtils.adjustColumns(grid);
	            
	            // Paso 2: Ajustar alturas de filas para mostrar siempre 12 filas
	            // Diferimos este paso para que primero se complete el ajuste de columnas
	            Ext.defer(function() {
	                // console.log('📏 Ajustando alturas de filas (12 filas fijo)...');
	                GridUtils.adjustRowHeightsFixed12(grid);
	            }, 100);
	            
	        }, 200);
	    },

	    /**
	     * Ajustar columnas según contenido - VERSIÓN 100% GARANTIZADO
	     * Esta función es el corazón del sistema: autoajusta columnas y luego expande
	     * para ocupar el 100% del ancho disponible cuando hay espacio sobrante
	     * @param {Ext.grid.Panel} grid - El grid a ajustar
	     */
	    adjustColumns: function(grid) {
	        // console.log('=== adjustColumns (100% GARANTIZADO) ===');
	        
	        var gridWidth = grid.getWidth();
	        var visibleCount = 0;
	        
	        // 1. CONTAR COLUMNAS VISIBLES
	        // Iteramos sobre todas las columnas para contar cuántas están visibles
	        Ext.each(grid.columns, function(col) {
	            if (!col.hidden) visibleCount++;
	        });
	        
	        // console.log('Grid ancho:', gridWidth, 'px, Columnas visibles:', visibleCount);
	        
	        // 2. AUTO-AJUSTAR CADA COLUMNA SEGÚN SU CONTENIDO
	        var columnData = [];
	        var totalCurrentWidth = 0;
	        
	        Ext.each(grid.columns, function(col, index) {
	            if (col.hidden) return;
	            
	            var headerText = col.text || '';
	            var currentWidth = col.getWidth() || 0;
	            
	            // AUTO-AJUSTE: Usar la funcionalidad nativa de ExtJS si está disponible
	            if (col.autoSize || col.autoSizeColumn) {
	                try {
	                    // console.log('Columna', index, '("' + headerText + '"): autoajustando...');
	                    if (col.autoSize) col.autoSize();
	                    else if (col.autoSizeColumn) col.autoSizeColumn();
	                    currentWidth = col.getWidth();
	                } catch(e) {
	                    // console.log('Autoajuste falló:', e.message);
	                }
	            }
	            
	            // 3. CALCULAR ANCHO MÍNIMO BASADO EN EL HEADER
	            // El ancho mínimo debe ser suficiente para mostrar el texto del header
	            var minWidth = 80; // Mínimo absoluto para cualquier columna
	            
	            if (headerText) {
	                try {
	                    // Crear elemento temporal para medir el ancho real del texto
	                    var measureEl = Ext.getBody().createChild({
	                        tag: 'div',
	                        style: 'position:absolute;left:-1000px;top:-1000px;font:12px tahoma,arial,helvetica,sans-serif;white-space:nowrap;padding:0 8px;',
	                        html: headerText
	                    });
	                    minWidth = measureEl.getWidth() + 25; // +25px para padding y bordes
	                    measureEl.remove();
	                } catch(e) {
	                    // Fallback: estimar ancho basado en longitud del texto
	                    minWidth = headerText.length * 9 + 25;
	                }
	            }
	            
	            minWidth = Math.max(80, minWidth); // Asegurar mínimo de 80px
	            
	            // 4. APLICAR MÍNIMO SI ES NECESARIO
	            if (currentWidth < minWidth) {
	                currentWidth = minWidth;
	                col.setWidth(currentWidth);
	            }
	            
	            // 5. ALMACENAR DATOS DE LA COLUMNA PARA PROCESAMIENTO POSTERIOR
	            columnData.push({
	                col: col,               // Referencia al objeto columna
	                index: index,           // Índice de la columna
	                headerText: headerText, // Texto del header
	                currentWidth: currentWidth, // Ancho actual después de autoajuste
	                minWidth: minWidth,     // Ancho mínimo calculado
	                headerLength: headerText.length // Longitud del texto para cálculos
	            });
	            
	            totalCurrentWidth += currentWidth;
	        });
	        
	        // console.log('Ancho total después de autoajuste:', totalCurrentWidth, 'px');
	        
	        // 6. CALCULAR ESPACIO SOBRANTE
	        var spaceLeft = gridWidth - totalCurrentWidth;
	        // console.log('Espacio sobrante:', spaceLeft, 'px');
	        
	        // 7. DETECCIÓN INTELIGENTE: ¿CUÁNTO ESPACIO SOBRA?
	        // Calculamos porcentaje para decidir la estrategia de expansión
	        var percentageLeft = (spaceLeft / gridWidth) * 100;
	        // console.log('Porcentaje de espacio sobrante:', percentageLeft.toFixed(1) + '%');
	        
	        // 8. ESTRATEGIA INTELIGENTE SEGÚN EL ESPACIO SOBRANTE
	        if (spaceLeft > 0 && visibleCount > 0) {
	            // CASO A: MUCHO espacio sobrante (>20%) - EXPANSIÓN AGRESIVA
	            // Ocurre cuando el grid está casi vacío o las columnas son muy estrechas
	            if (percentageLeft > 20) {
	                // console.log('🚀 MUCHO espacio sobrante (' + percentageLeft.toFixed(1) + '%), aplicando expansión agresiva...');
	                GridUtils._applyAggressiveExpansion(grid, columnData, gridWidth, spaceLeft, visibleCount);
	            }
	            // CASO B: Espacio moderado (5-20%) - EXPANSIÓN PROPORCIONAL MEJORADA
	            // Ocurre cuando hay espacio significativo pero no excesivo
	            else if (percentageLeft > 5) {
	                // console.log('📏 Espacio moderado (' + percentageLeft.toFixed(1) + '%), aplicando expansión mejorada...');
	                GridUtils._applyImprovedExpansion(grid, columnData, gridWidth, spaceLeft, visibleCount);
	            }
	            // CASO C: Poco espacio (<5%) - DISTRIBUCIÓN SIMPLE
	            // Pequeños ajustes finales para llegar al 100%
	            else {
	                // console.log('📐 Poco espacio (' + percentageLeft.toFixed(1) + '%), distribuyendo equitativamente...');
	                GridUtils._applySimpleDistribution(grid, columnData, spaceLeft, visibleCount);
	            }
	            
	        } else if (spaceLeft < 0) {
	            // console.log('✅ Columnas más anchas que el grid - Scrollbar necesario');
	            // Si el contenido es más ancho que el grid, mantener scrollbar
	            // Asegurar al menos los anchos mínimos
	            Ext.each(columnData, function(item) {
	                if (item.currentWidth < item.minWidth) {
	                    item.col.setWidth(item.minWidth);
	                }
	            });
	        } else {
	            // console.log('⚖️ Ancho perfecto');
	            // Ya está al 100%, no hacer nada
	        }
	        
	        // 9. VERIFICACIÓN FINAL Y REPORTE
	        var finalTotal = 0;
	        Ext.each(grid.columns, function(col) {
	            if (!col.hidden) finalTotal += col.getWidth();
	        });
	        
	        var finalSpaceLeft = gridWidth - finalTotal;
	        // console.log('📊 RESULTADO FINAL:');
	        // console.log('   Ancho grid:', gridWidth, 'px');
	        // console.log('   Ancho columnas:', finalTotal, 'px');
	        // console.log('   Diferencia:', finalSpaceLeft, 'px');
	        // console.log('   % ocupación:', ((finalTotal / gridWidth) * 100).toFixed(1) + '%');
	        
	        // Forzar actualización del layout para aplicar cambios
	        grid.updateLayout();
	        // console.log('✅ adjustColumns completado');
	    },

	    /**
	     * EXPANSIÓN AGRESIVA: Para cuando hay MUCHO espacio sobrante (>20%)
	     * Esta estrategia calcula un "ancho ideal" por columna y expande hacia ese objetivo
	     * @param {Ext.grid.Panel} grid - El grid
	     * @param {Array} columnData - Datos de las columnas
	     * @param {number} gridWidth - Ancho total del grid
	     * @param {number} spaceLeft - Espacio sobrante
	     * @param {number} visibleCount - Número de columnas visibles
	     */
	    _applyAggressiveExpansion: function(grid, columnData, gridWidth, spaceLeft, visibleCount) {
	        // console.log('💪 EXPANSIÓN AGRESIVA ACTIVADA');
	        
	        // 1. CALCULAR ANCHO OBJETIVO POR COLUMNA
	        // Distribución equitativa ideal basada en el ancho total del grid
	        var targetWidthPerColumn = Math.floor(gridWidth / visibleCount);
	        // console.log('Ancho objetivo por columna:', targetWidthPerColumn, 'px');
	        
	        // 2. CALCULAR CUÁNTO NECESITA EXPANDIRSE CADA COLUMNA
	        var totalNeededExpansion = 0;
	        Ext.each(columnData, function(item) {
	            var needed = Math.max(0, targetWidthPerColumn - item.currentWidth);
	            item.neededExpansion = needed; // Cuánto necesita crecer esta columna
	            totalNeededExpansion += needed; // Total necesario para todas
	        });
	        
	        // 3. VERIFICAR SI HAY SUFICIENTE ESPACIO PARA LA EXPANSIÓN IDEAL
	        if (spaceLeft >= totalNeededExpansion) {
	            // console.log('✅ Espacio suficiente para expansión ideal');
	            // Hay espacio suficiente: cada columna alcanza su ancho objetivo
	            Ext.each(columnData, function(item) {
	                var newWidth = item.currentWidth + item.neededExpansion;
	                // Asegurar que no sea menor que el mínimo
	                newWidth = Math.max(newWidth, item.minWidth);
	                item.col.setWidth(newWidth);
	                // console.log('  Columna', item.index, '("' + item.headerText + '"):', 
	                //           item.currentWidth, '->', newWidth, 'px (+' + item.neededExpansion + 'px)');
	            });
	        } else {
	            // 4. ESPACIO INSUFICIENTE: EXPANDIR PROPORCIONALMENTE
	            // console.log('⚠️ Espacio insuficiente para expansión ideal, distribuyendo proporcionalmente...');
	            var expansionRatio = spaceLeft / totalNeededExpansion;
	            
	            Ext.each(columnData, function(item) {
	                var expansion = Math.floor(item.neededExpansion * expansionRatio);
	                var newWidth = item.currentWidth + expansion;
	                newWidth = Math.max(newWidth, item.minWidth);
	                item.col.setWidth(newWidth);
	                // console.log('  Columna', item.index, '("' + item.headerText + '"):', 
	                //           item.currentWidth, '->', newWidth, 'px (+' + expansion + 'px)');
	            });
	            
	            // 5. DISTRIBUIR CUALQUIER ESPACIO RESTANTE
	            var currentTotal = 0;
	            Ext.each(columnData, function(item) {
	                currentTotal += item.col.getWidth();
	            });
	            
	            var remainingSpace = gridWidth - currentTotal;
	            if (remainingSpace > 0) {
	                // console.log('📐 Distribuyendo espacio restante:', remainingSpace, 'px');
	                var extraPerColumn = Math.floor(remainingSpace / visibleCount);
	                var remainder = remainingSpace % visibleCount;
	                
	                // Distribuir el espacio restante pixel por pixel
	                Ext.each(columnData, function(item, idx) {
	                    var extra = extraPerColumn;
	                    if (idx < remainder) extra += 1; // Los primeros se llevan el resto
	                    var finalWidth = item.col.getWidth() + extra;
	                    item.col.setWidth(finalWidth);
	                });
	            }
	        }
	    },

	    /**
	     * EXPANSIÓN MEJORADA: Para espacio moderado (5-20%)
	     * Usa un factor de expansión dinámico basado en el espacio disponible
	     * @param {Ext.grid.Panel} grid - El grid
	     * @param {Array} columnData - Datos de las columnas
	     * @param {number} gridWidth - Ancho total del grid
	     * @param {number} spaceLeft - Espacio sobrante
	     * @param {number} visibleCount - Número de columnas visibles
	     */
	    _applyImprovedExpansion: function(grid, columnData, gridWidth, spaceLeft, visibleCount) {
	        // console.log('📈 EXPANSIÓN MEJORADA ACTIVADA');
	        
	        // 1. CALCULAR FACTOR DE EXPANSIÓN DINÁMICO
	        // Base según número de columnas (menos columnas = más expansión por columna)
	        var baseExpansion = 1.0;
	        
	        if (visibleCount <= 3) {
	            baseExpansion = 3.0; // Máxima expansión para muy pocas columnas
	        } else if (visibleCount <= 6) {
	            baseExpansion = 2.0; // Expansión media
	        } else if (visibleCount <= 10) {
	            baseExpansion = 1.5; // Expansión moderada
	        } else {
	            baseExpansion = 1.2; // Mínima expansión para muchas columnas
	        }
	        
	        // Ajustar factor según porcentaje de espacio sobrante
	        // Más espacio sobrante = factor de expansión más alto
	        var percentageLeft = (spaceLeft / gridWidth) * 100;
	        var dynamicFactor = baseExpansion * (1 + (percentageLeft / 100));
	        
	        // console.log('Factor de expansión dinámico:', dynamicFactor.toFixed(2), 
	        //            '(base:', baseExpansion, ', espacio:', percentageLeft.toFixed(1) + '%)');
	        
	        // 2. APLICAR EXPANSIÓN PROPORCIONAL CON LÍMITES INTELIGENTES
	        Ext.each(columnData, function(item) {
	            var newWidth = Math.floor(item.currentWidth * dynamicFactor);
	            
	            // LÍMITES INTELIGENTES BASADOS EN TIPO DE CONTENIDO
	            var maxWidth = 0;
	            
	            // Headers largos pueden expandirse más (contienen más información)
	            if (item.headerLength > 20) {
	                maxWidth = 600; // Headers muy largos
	            } else if (item.headerLength > 10) {
	                maxWidth = 400; // Headers largos
	            } else {
	                maxWidth = 300; // Headers cortos
	            }
	            
	            // AJUSTAR LÍMITES POR NÚMERO DE COLUMNAS
	            // Menos columnas = cada columna puede ser más ancha
	            if (visibleCount <= 3) {
	                maxWidth = Math.max(maxWidth, 800);
	            } else if (visibleCount <= 6) {
	                maxWidth = Math.max(maxWidth, 500);
	            }
	            
	            // Para expansión mejorada, aumentar límites dinámicamente
	            // No permitir que ninguna columna ocupe más del 30% del grid
	            maxWidth = Math.min(maxWidth, gridWidth * 0.3);
	            
	            // Aplicar límites
	            newWidth = Math.min(newWidth, maxWidth);
	            newWidth = Math.max(newWidth, item.minWidth); // No menos que el mínimo
	            
	            item.col.setWidth(newWidth);
	            // console.log('  Columna', item.index, '("' + item.headerText + '"):', 
	            //           item.currentWidth, '->', newWidth, 'px');
	        });
	        
	        // 3. DISTRIBUIR ESPACIO RESTANTE SI ES POCO (<100px)
	        var currentTotal = 0;
	        Ext.each(columnData, function(item) {
	            currentTotal += item.col.getWidth();
	        });
	        
	        var remainingSpace = gridWidth - currentTotal;
	        if (remainingSpace > 0 && remainingSpace < 100) {
	            // console.log('📐 Ajuste fino final:', remainingSpace, 'px');
	            GridUtils._applySimpleDistribution(grid, columnData, remainingSpace, visibleCount);
	        }
	    },

	    /**
	     * DISTRIBUCIÓN SIMPLE: Para poco espacio (<5%)
	     * Distribución equitativa pixel por pixel
	     * @param {Ext.grid.Panel} grid - El grid
	     * @param {Array} columnData - Datos de las columnas
	     * @param {number} spaceLeft - Espacio sobrante
	     * @param {number} visibleCount - Número de columnas visibles
	     */
	    _applySimpleDistribution: function(grid, columnData, spaceLeft, visibleCount) {
	        // console.log('📐 DISTRIBUCIÓN SIMPLE');
	        
	        // Calcular cuántos pixels extra para cada columna
	        var extraPerColumn = Math.floor(spaceLeft / visibleCount);
	        var remainder = spaceLeft % visibleCount; // Pixels sobrantes después de división
	        
	        // Distribuir: cada columna recibe extraPerColumn, y las primeras reciben 1px extra si hay remainder
	        Ext.each(columnData, function(item, idx) {
	            var extra = extraPerColumn;
	            if (idx < remainder) extra += 1; // Distribuir pixels sobrantes uno por uno
	            
	            var newWidth = item.col.getWidth() + extra;
	            item.col.setWidth(newWidth);
	            // console.log('  Columna', item.index, '("' + item.headerText + '"): +' + extra + 'px');
	        });
	    },

	    /**
	     * Distribuir espacio sobrante entre columnas (función legacy)
	     * Mantenida por compatibilidad, pero la lógica principal está en adjustColumns
	     * @param {Ext.grid.Panel} grid - El grid a ajustar
	     */
	    distributeExtraSpace: function(grid) {
	        // console.log('=== distributeExtraSpace ===');
	        
	        var totalWidth = 0;
	        var gridWidth = grid.getWidth();
	        var visibleCount = 0;
	        
	        // Calcular ancho total actual
	        Ext.each(grid.columns, function(col) {
	            if (!col.hidden) {
	                totalWidth += col.getWidth();
	                visibleCount++;
	            }
	        });
	        
	        var spaceLeft = gridWidth - totalWidth;
	        // console.log('Ancho total:', totalWidth, 'px, Espacio sobrante:', spaceLeft, 'px');
	        
	        // Solo hacer ajustes menores (<200px) para no interferir con adjustColumns
	        if (spaceLeft > 0 && spaceLeft < 200 && visibleCount > 0) {
	            // console.log('📏 Ajuste fino final...');
	            
	            var extraPerColumn = Math.floor(spaceLeft / visibleCount);
	            
	            Ext.each(grid.columns, function(col) {
	                if (!col.hidden) {
	                    var newWidth = col.getWidth() + extraPerColumn;
	                    col.setWidth(newWidth);
	                }
	            });
	        }
	        
	        grid.updateLayout();
	        // console.log('✅ distributeExtraSpace completado');
	    },

	    /**
	     * Ajustar alturas de filas para SIEMPRE usar 12 filas como referencia
	     * Esto garantiza que el scroll se comporte consistentemente
	     * @param {Ext.grid.Panel} grid - El grid a ajustar
	     */
	    adjustRowHeightsFixed12: function(grid) {
	        // console.log('=== adjustRowHeightsFixed12 (12 FILAS SIEMPRE) ===');
	        
	        var view = grid.getView();
	        if (!view) {
	            // console.log('❌ Vista no disponible');
	            return;
	        }
	        
	        // 1. CALCULAR ALTURA DISPONIBLE DEL GRID
	        var gridHeight = grid.getHeight();
	        // console.log('Altura total del grid:', gridHeight, 'px');
	        
	        // 2. CALCULAR ALTURA DE ELEMENTOS ESTRUCTURALES
	        var headerHeight = 0;
	        var headerContainer = grid.headerCt;
	        if (headerContainer && headerContainer.getHeight()) {
	            headerHeight = headerContainer.getHeight();
	        }
	        
	        var dockedHeight = 0;
	        if (grid.dockedItems) {
	            grid.dockedItems.each(function(item) {
	                if (item.isVisible() && item.getHeight) {
	                    dockedHeight += item.getHeight(); // Toolbars, paginación, etc.
	                }
	            });
	        }
	        
	        // 3. CALCULAR ALTURA DISPONIBLE PARA LAS FILAS
	        var availableHeight = gridHeight - headerHeight - dockedHeight - 2; // -2 para bordes
	        // console.log('Altura disponible para filas:', availableHeight, 'px');
	        // console.log('  - Altura grid:', gridHeight);
	        // console.log('  - Altura header:', headerHeight);
	        // console.log('  - Altura docked:', dockedHeight);
	        
	        // 4. OBTENER FILAS VISIBLES ACTUALES
	        var rows = view.getNodes();
	        var actualRowCount = rows.length;
	        // console.log('Filas visibles actuales:', actualRowCount);
	        
	        // 5. OBTENER PAGESIZE DEL STORE (POR DEFECTO 12)
	        var store = grid.getStore();
	        var pageSize = 12; // Valor por defecto
	        
	        if (store && store.pageSize) {
	            pageSize = store.pageSize;
	            // console.log('PageSize obtenido del store:', pageSize);
	        } else {
	            // console.log('Usando pageSize por defecto:', pageSize);
	        }
	        
	        // 6. SIEMPRE USAR 12 (O EL PAGESIZE) COMO REFERENCIA
	        var targetRowCount = pageSize;
	        // console.log('Filas objetivo (target):', targetRowCount);
	        
	        // 7. CALCULAR ALTURA POR FILA
	        var rowHeight = Math.floor(availableHeight / targetRowCount);
	        // console.log('Altura calculada por fila (basada en ' + targetRowCount + ' filas):', rowHeight, 'px');
	        
	        // 8. APLICAR ALTURA UNIFORME A TODAS LAS FILAS VISIBLES
	        if (actualRowCount > 0) {
	            Ext.each(rows, function(row, index) {
	                Ext.get(row).setHeight(rowHeight);
	                // console.log('  Fila visible ' + (index + 1) + ' de ' + actualRowCount + ': altura = ' + rowHeight + 'px');
	            });
	        }
	        
	        // 9. SI HAY MENOS DE 12 FILAS, AJUSTAR ESPACIO VACÍO
	        // Esto mantiene el scroll consistente incluso con pocos registros
	        if (actualRowCount < targetRowCount) {
	            var emptySpaceHeight = rowHeight * (targetRowCount - actualRowCount);
	            // console.log('Espacio vacío equivalente a ' + (targetRowCount - actualRowCount) + ' filas: ' + emptySpaceHeight + 'px');
	            
	            if (rows.length > 0) {
	                var lastRow = rows[rows.length - 1];
	                var lastRowEl = Ext.get(lastRow);
	                // Añadir margen inferior para simular las filas faltantes
	                lastRowEl.setStyle('margin-bottom', emptySpaceHeight + 'px');
	                // console.log('Añadido margen inferior a última fila:', emptySpaceHeight, 'px');
	            }
	        }
	        
	        // 10. FORZAR ACTUALIZACIÓN DEL LAYOUT
	        grid.updateLayout();
	        
	        // console.log('✅ Alturas de filas ajustadas exitosamente');
	        // console.log('   Cada fila tiene:', rowHeight, 'px de altura');
	        // console.log('   Basado en:', targetRowCount, 'filas (pageSize)');
	        // console.log('   Filas visibles:', actualRowCount);
	        // console.log('   Espacio total imitado:', (rowHeight * targetRowCount), 'px de', availableHeight, 'px disponibles');
	    },

	    /**
	     * Versión alternativa: Mantener scroll consistente con filas fantasma
	     * En lugar de margen, ajusta el contenedor para mantener altura constante
	     * @param {Ext.grid.Panel} grid - El grid a ajustar
	     */
	    adjustRowHeightsWithGhostRows: function(grid) {
	        // console.log('=== adjustRowHeightsWithGhostRows (CON FILAS FANTASMA) ===');
	        
	        var view = grid.getView();
	        if (!view) return;
	        
	        // Calcular altura disponible (misma lógica que adjustRowHeightsFixed12)
	        var gridHeight = grid.getHeight();
	        var headerHeight = grid.headerCt ? grid.headerCt.getHeight() : 0;
	        var dockedHeight = 0;
	        
	        if (grid.dockedItems) {
	            grid.dockedItems.each(function(item) {
	                if (item.isVisible() && item.getHeight) {
	                    dockedHeight += item.getHeight();
	                }
	            });
	        }
	        
	        var availableHeight = gridHeight - headerHeight - dockedHeight - 2;
	        
	        // Obtener pageSize (por defecto 12)
	        var store = grid.getStore();
	        var pageSize = (store && store.pageSize) ? store.pageSize : 12;
	        
	        // Obtener filas actuales
	        var rows = view.getNodes();
	        var actualRowCount = rows.length;
	        
	        // Calcular altura por fila
	        var rowHeight = Math.floor(availableHeight / pageSize);
	        
	        // Ajustar filas visibles
	        Ext.each(rows, function(row) {
	            Ext.get(row).setHeight(rowHeight);
	        });
	        
	        // ESTRATEGIA ALTERNATIVA: Ajustar min-height del contenedor
	        // En lugar de margen, forzar altura mínima del viewport
	        if (actualRowCount < pageSize) {
	            var viewBody = view.getEl().down('.x-grid-view');
	            if (viewBody) {
	                var minHeight = rowHeight * pageSize;
	                viewBody.setStyle('min-height', minHeight + 'px');
	                // console.log('Ajustado min-height del viewBody a:', minHeight, 'px');
	            }
	        }
	        
	        grid.updateLayout();
	        // console.log('✅ Ajuste con filas fantasma completado');
	    },

	    /**
	     * Función de ajuste de altura de filas (legacy - se mantiene por compatibilidad)
	     * @param {Ext.grid.Panel} grid - El grid a ajustar
	     */
	    adjustRowHeights: function(grid) {
	        // console.log('=== adjustRowHeights (LEGACY - usa Fixed12) ===');
	        GridUtils.adjustRowHeightsFixed12(grid);
	    },

	    /**
	     * Configurar listeners estándar para un grid
	     * Conecta eventos automáticos para que el grid se ajuste dinámicamente
	     * @param {Ext.grid.Panel} grid - El grid a configurar
	     */
	    setupGridListeners: function(grid) {
	        var view = grid.getView();
	        
	        // Evento cuando se refresca la vista (nuevos datos)
	        view.on('refresh', function(view) {
	            // console.log('📈 Evento REFRESH en grid:', grid.id || grid.itemId);
	            GridUtils.adjustGridLayout(grid, true);
	        });
	        
	        // Evento cuando cambia el tamaño del grid
	        view.on('resize', function(view) {
	            // console.log('📐 Evento RESIZE en grid:', grid.id || grid.itemId);
	            GridUtils.adjustGridLayout(grid, false);
	        });
	        
	        // También escuchar carga del store (datos cargados desde servidor)
	        var store = grid.getStore();
	        if (store) {
	            store.on('load', function() {
	                // console.log('📊 Store cargado, ajustando grid:', grid.id || grid.itemId);
	                Ext.defer(function() {
	                    GridUtils.adjustGridLayout(grid, true);
	                }, 100); // Pequeño delay para asegurar renderizado
	            });
	        }
	    },

	    /**
	     * NUEVA: Función para forzar 100% de ocupación inmediatamente
	     * Distribución equitativa simple, ignorando contenido
	     * Útil para debugging o casos especiales
	     * @param {Ext.grid.Panel} grid - El grid a ajustar
	     */
	    force100PercentWidth: function(grid) {
	        // console.log('💪 FORZANDO 100% DE OCUPACIÓN');
	        
	        var gridWidth = grid.getWidth();
	        var visibleCount = 0;
	        var columns = [];
	        
	        // Obtener columnas visibles
	        Ext.each(grid.columns, function(col, index) {
	            if (!col.hidden) {
	                visibleCount++;
	                columns.push({
	                    col: col,
	                    index: index,
	                    header: col.text || '',
	                    currentWidth: col.getWidth()
	                });
	            }
	        });
	        
	        if (visibleCount === 0) return;
	        
	        // DISTRIBUCIÓN EQUITATIVA SIMPLE
	        var widthPerColumn = Math.floor(gridWidth / visibleCount);
	        var remainder = gridWidth % visibleCount;
	        
	        // console.log('Ancho por columna:', widthPerColumn, 'px, Resto:', remainder);
	        
	        columns.forEach(function(item, index) {
	            var finalWidth = widthPerColumn;
	            if (index < remainder) finalWidth += 1; // Distribuir pixels sobrantes
	            
	            // Asegurar un mínimo razonable (80px)
	            finalWidth = Math.max(80, finalWidth);
	            
	            item.col.setWidth(finalWidth);
	            // console.log('  Columna', index, '("' + item.header + '"):', 
	            //           item.currentWidth, '->', finalWidth, 'px');
	        });
	        
	        grid.updateLayout();
	        // console.log('✅ 100% de ancho forzado exitosamente');
	    }
	};

	//==============================================
	// FUNCIONES DE DEBUG GLOBALES
	// Estas funciones están disponibles en la consola del navegador
	//==============================================
	var recordWorker = {}; // Variable global para otros usos

	/**
	 * Función para debug manual desde consola
	 * Muestra información detallada de todos los grids en la página
	 */
	function debugGridUtils() {
	    // console.log('=== DEBUG GridUtils ===');
	    // console.log('Versión: 2.5 (100% garantizado)');
	    // console.log('Funciones disponibles:', Object.keys(GridUtils));
	    
	    var grids = Ext.ComponentQuery.query('grid');
	    // console.log('Grids encontrados:', grids.length);
	    
	    grids.forEach(function(grid, index) {
	        // console.log('--- Grid ' + (index + 1) + ' ---');
	        // console.log('ID:', grid.id || grid.itemId);
	        // console.log('XTYPE:', grid.xtype);
	        // console.log('Ancho:', grid.getWidth(), 'px');
	        // console.log('Alto:', grid.getHeight(), 'px');
	        
	        var visibleCols = 0;
	        var totalColWidth = 0;
	        Ext.each(grid.columns, function(col) {
	            if (!col.hidden) {
	                visibleCols++;
	                totalColWidth += col.getWidth();
	            }
	        });
	        
	        // console.log('Columnas visibles:', visibleCols);
	        // console.log('Ancho total columnas:', totalColWidth, 'px');
	        // console.log('Espacio sobrante:', grid.getWidth() - totalColWidth, 'px');
	        // console.log('% de ocupación:', ((totalColWidth / grid.getWidth()) * 100).toFixed(1) + '%');
	        
	        var store = grid.getStore();
	        // console.log('Store pageSize:', store ? store.pageSize : 'N/A');
	    });
	}

	//==============================================
	// INICIALIZACIÓN AL CARGAR LA PÁGINA
	//==============================================
	Ext.onReady(function() {
	    // Hacer las funciones globales para acceso desde consola
	    window.debugGrids = debugGridUtils;
	    
	    /**
	     * Ajustar todos los grids de la página
	     * Útil después de cambios dinámicos o para debugging
	     */
	    window.adjustAllGrids = function() {
	        var grids = Ext.ComponentQuery.query('grid');
	        // console.log('🔧 Ajustando todos los grids (' + grids.length + ' encontrados)');
	        grids.forEach(function(grid, index) {
	            // console.log('  [' + (index + 1) + '] Grid:', grid.id || grid.itemId);
	            GridUtils.adjustGridLayout(grid, true);
	        });
	    };
	    
	    /**
	     * Ajustar un grid específico por ID
	     * @param {string} gridId - ID del grid a ajustar
	     */
	    window.adjustGridById = function(gridId) {
	        var grid = Ext.getCmp(gridId);
	        if (grid) {
	            // console.log('🔧 Ajustando grid específico:', gridId);
	            GridUtils.adjustGridLayout(grid, true);
	        } else {
	            // console.log('❌ Grid no encontrado:', gridId);
	        }
	    };
	    
	    /**
	     * Ajustar alturas de filas usando la estrategia de 12 filas fijas
	     * @param {string} gridId - ID del grid
	     */
	    window.adjustGridHeightsFixed12 = function(gridId) {
	        var grid = Ext.getCmp(gridId);
	        if (grid) {
	            // console.log('📏 Ajustando alturas (12 filas fijo) en grid:', gridId);
	            GridUtils.adjustRowHeightsFixed12(grid);
	        }
	    };
	    
	    /**
	     * Ajustar alturas usando la estrategia de filas fantasma
	     * @param {string} gridId - ID del grid
	     */
	    window.adjustGridHeightsWithGhost = function(gridId) {
	        var grid = Ext.getCmp(gridId);
	        if (grid) {
	            // console.log('👻 Ajustando con filas fantasma en grid:', gridId);
	            GridUtils.adjustRowHeightsWithGhostRows(grid);
	        }
	    };
	    
	    /**
	     * Forzar 100% de ocupación del ancho (distribución equitativa)
	     * @param {string} gridId - ID del grid
	     */
	    window.force100Percent = function(gridId) {
	        var grid = Ext.getCmp(gridId);
	        if (grid) {
	            // console.log('💪 Forzando 100% de ocupación en grid:', gridId);
	            GridUtils.force100PercentWidth(grid);
	        }
	    };
	    
	    // Mensaje inicial en consola
	    // console.log('✅ GridUtils v2.5 cargado (100% garantizado)');
	    // console.log('Comandos disponibles desde consola:');
	    // console.log('  - debugGrids(): muestra info de todos los grids');
	    // console.log('  - adjustAllGrids(): ajusta todos los grids');
	    // console.log('  - adjustGridById("id"): ajusta grid específico');
	    // console.log('  - force100Percent("id"): fuerza 100% de ocupación');
	    
	    // Aplicar ajuste automático a todos los grids existentes al cargar la página
	    Ext.defer(function() {
	        var grids = Ext.ComponentQuery.query('grid');
	        // console.log('🔧 Aplicando ajuste automático a', grids.length, 'grids existentes');
	        
	        grids.forEach(function(grid) {
	            // Aplicar ajuste inicial con delay para asegurar renderizado completo
	            Ext.defer(function() {
	                GridUtils.adjustGridLayout(grid, true);
	            }, 300);
	        });
	    }, 500);
	});

	var recordWorker = {}; // Variable global