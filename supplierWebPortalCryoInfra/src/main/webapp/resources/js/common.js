
 
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

//Namespace para funciones de grid
var GridUtils = {
 
 /**
  * Función de ajuste reutilizable para grids
  * @param {Ext.grid.Panel} grid - El grid a ajustar
  * @param {boolean} isRefresh - Indica si es un refresh o resize
  */
 adjustGridLayout: function(grid, isRefresh) {
     if (!grid || !grid.isXType('gridpanel')) return;
     
     Ext.defer(function() {
         // Para resize, validar zoom solo si no es refresh
         if (!isRefresh) {
             var currentGridWidth = grid.getWidth();
             
             // Guardar tamaño original solo la primera vez
             if (!grid.originalGridWidth) {
                 grid.originalGridWidth = currentGridWidth;
             }
             
             // Solo ejecutar si es zoom out (ancho actual mayor al original)
             if (currentGridWidth <= grid.originalGridWidth) {
                 return;
             }
         } else {
             // Para refresh, guardar tamaño original si no existe
             if (!grid.originalGridWidth) {
                 grid.originalGridWidth = grid.getWidth();
             }
         }
         
         // Autoajuste de columnas
         GridUtils.adjustColumns(grid);
         
         // Repartir espacio sobrante
         Ext.defer(function() {
             GridUtils.distributeExtraSpace(grid);
         }, 100);
         
         // Ajustar altura de filas si se cumplen condiciones
         GridUtils.adjustRowHeights(grid);
         
     }, 200);
 },
 
 /**
  * Ajustar columnas según contenido
  * @param {Ext.grid.Panel} grid - El grid a ajustar
  */
 adjustColumns: function(grid) {
     Ext.each(grid.columns, function(col) {
         if (col.autoSize) col.autoSize();
         else if (col.autoSizeColumn) col.autoSizeColumn();
         
         // Ajuste adicional según header
         var headerText = col.text || '';
         if (headerText && col.getEl()) {
             var headerEl = col.getEl().down('.x-column-header-text');
             if (headerEl) {
                 var textWidth = Ext.util.TextMetrics.measure(headerEl, headerText).width + 20;
                 if (textWidth > col.getWidth()) {
                     col.setWidth(textWidth);
                 }
             }
         }
     });
 },
 
 /**
  * Distribuir espacio sobrante entre columnas
  * @param {Ext.grid.Panel} grid - El grid a ajustar
  */
 distributeExtraSpace: function(grid) {
     var totalWidth = 0;
     var gridWidth = grid.getWidth();
     
     // Calcular ancho total de columnas visibles
     Ext.each(grid.columns, function(col) {
         if (!col.hidden) totalWidth += col.getWidth();
     });
     
     // Si sobra espacio, repartirlo
     if (totalWidth < gridWidth) {
         var diff = gridWidth - totalWidth - 10; // margen visual
         var visibles = Ext.Array.filter(grid.columns, function(col) {
             return !col.hidden;
         });
         var extra = diff / visibles.length;
         
         Ext.each(visibles, function(col) {
             col.setWidth(col.getWidth() + extra);
         });
         
         grid.updateLayout();
     }
 },
 
 /**
  * Ajustar altura de filas si se cumplen condiciones
  * @param {Ext.grid.Panel} grid - El grid a ajustar
  */
 adjustRowHeights: function(grid) {
     var view = grid.getView();
     
     // Validaciones para aplicar ajuste de altura
     // 1. Pantalla grande
     var screenWidth = Ext.Element.getViewportWidth();
     var isLargeScreen = screenWidth >= 1000;
     
     // 2. Verificar si los registros de la página son iguales al pageSize
     var store = grid.getStore();
     var currentRecords = store.getCount();
     var pageSize = store.pageSize || 1;
     var isFullPage = currentRecords >= pageSize;
     
     // Aplicar ajuste solo si ambas condiciones se cumplen
     if (isLargeScreen && isFullPage) {
         GridUtils.performRowHeightAdjustment(grid, view);
     }
 },
 
 /**
  * Realizar el ajuste de altura de filas
  * @param {Ext.grid.Panel} grid - El grid a ajustar
  * @param {Ext.grid.View} view - La vista del grid
  */
 performRowHeightAdjustment: function(grid, view) {
     // Calcular altura disponible
     var containerHeight = grid.getHeight();
     var headerHeight = 0;
     var headerContainer = grid.headerCt;
     
     if (headerContainer && headerContainer.getHeight()) {
         headerHeight = headerContainer.getHeight();
     }
     
     // Calcular altura de docked items
     var dockedHeight = 0;
     if (grid.dockedItems) {
         grid.dockedItems.each(function(item) {
             if (item.isVisible() && item.getHeight) {
                 dockedHeight += item.getHeight();
             }
         });
     }
     
     var availableHeight = containerHeight - headerHeight - dockedHeight - 10;
     var rows = view.getNodes();
     var realRowCount = rows.length;
     var targetRowCount = 12;
     
     // Caso 1: Solo una fila
     if (realRowCount === 1) {
         var uniformHeight = availableHeight / targetRowCount;
         Ext.get(rows[0]).setHeight(uniformHeight);
         
         Ext.defer(function() {
             Ext.get(rows[0]).setHeight(uniformHeight);
             grid.updateLayout();
         }, 50);
         return;
     }
     
     // Caso 2: Entre 2 y 11 filas
     if (realRowCount > 1 && realRowCount < targetRowCount) {
         var uniformHeight = availableHeight / targetRowCount;
         Ext.each(rows, function(row) {
             Ext.get(row).setHeight(uniformHeight);
         });
         grid.updateLayout();
         return;
     }
     
     // Caso 3: 12 o más filas
     var totalContentHeight = 0;
     var rowHeights = [];
     
     Ext.each(rows, function(row, index) {
         var rowHeight = 25;
         var cells = Ext.get(row).query('.x-grid-cell');
         
         Ext.each(cells, function(cell) {
             var cellEl = Ext.get(cell);
             cellEl.setStyle('height', 'auto');
             var contentHeight = cellEl.dom.scrollHeight;
             if (contentHeight > rowHeight) {
                 rowHeight = contentHeight + 8;
             }
         });
         
         rowHeights[index] = rowHeight;
         totalContentHeight += rowHeight;
     });
     
     if (totalContentHeight < availableHeight && rows.length > 0) {
         var extraHeight = (availableHeight - totalContentHeight) / rows.length;
         Ext.each(rows, function(row, index) {
             Ext.get(row).setHeight(rowHeights[index] + extraHeight);
         });
     } else {
         Ext.each(rows, function(row, index) {
             Ext.get(row).setHeight(rowHeights[index]);
         });
     }
     
     grid.updateLayout();
 },
 
 /**
  * Configurar listeners estándar para un grid
  * @param {Ext.grid.Panel} grid - El grid a configurar
  */
 setupGridListeners: function(grid) {
     var view = grid.getView();
     
     view.on('refresh', function(view) {
         GridUtils.adjustGridLayout(grid, true);
     });
     
     view.on('resize', function(view) {
         GridUtils.adjustGridLayout(grid, false);
     });
 },
 
 /**
  * Mixin para agregar las funciones de ajuste a un grid
  * @param {Ext.grid.Panel} gridClass - La clase del grid a extender
  */
 applyGridMixin: function(gridClass) {
     Ext.apply(gridClass.prototype, {
         adjustGridLayout: function(isRefresh) {
             GridUtils.adjustGridLayout(this, isRefresh);
         },
         
         adjustColumns: function() {
             GridUtils.adjustColumns(this);
         },
         
         distributeExtraSpace: function() {
             GridUtils.distributeExtraSpace(this);
         },
         
         adjustRowHeights: function() {
             GridUtils.adjustRowHeights(this);
         },
         
         performRowHeightAdjustment: function(view) {
             GridUtils.performRowHeightAdjustment(this, view);
         }
     });
 }
};

var recordWorker={};
