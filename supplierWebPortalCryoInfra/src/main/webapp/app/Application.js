Ext.define('SupplierApp.Application', {
    extend: 'Ext.app.Application',
    name: 'SupplierApp',
    stores: [
    ],
    requires: [
        'SupplierApp.view.main.Main',
        'SupplierApp.widgets.SessionMonitor',
        'Ext.ux.form.NumericField',
        'SupplierApp.view.main.MainController'
    ],
    
    autoCreateViewport: false,
    launch: function () {
    	
    	_AppGlobSupplierApp = this;
    	
    	var langu = window.navigator.language;
		var lang = "";
		if(langu.startsWith("es", 0)){
			lang = "es";
		}else{
			lang = "en";
		}
		
		var me = this;
		 if (Ext.get('page-loader')) {
			    Ext.get('page-loader').hide();
		 }
		
		SupplierApp.widgets.SessionMonitor.start();
		
		Ext.Ajax.request({
		    url: 'getLocalization.action',
		    method: 'GET',
		    params: {
		    	lang : lang
	        },
		    success: function(fp, o) {
		    	var resp = Ext.decode(fp.responseText, true);
		    	SuppAppMsg = resp.data;	 
		    	var app = me.getApplication();
		    	app.setMainView('SupplierApp.view.main.Main');
		    }
		}); 
		
		
    },
    init: function() {
    	
    	// Aplicar override para campos de formulario
    	Ext.override(Ext.form.field.Text, {
            blankText: 'Este campo es requerido',
            requiredMessage: 'Este campo es requerido'
        });
    	
    	 Ext.override(Ext.form.field.Number, {
	        blankText: 'Este campo numérico es requerido',
	        requiredMessage: 'Este campo numérico es requerido',
	        nanMessage: 'Por favor ingrese un número válido'
	    });
	    
	    Ext.override(Ext.form.field.Date, {
	        blankText: 'La fecha es requerida',
	        requiredMessage: 'La fecha es requerida',
	        disabledDaysText: 'Día no disponible',
	        disabledDatesText: 'Fecha no disponible'
	    });
	    
	    Ext.override(Ext.form.field.ComboBox, {
	        blankText: 'Esta selección es requerida',
	        requiredMessage: 'Esta selección es requerida'
	    });
	    
	    Ext.override(Ext.form.field.TextArea, {
	        blankText: 'Este campo de texto es requerido',
	        requiredMessage: 'Este campo de texto es requerido'
	    });
	    
	    Ext.override(Ext.form.field.Checkbox, {
	        blankText: 'Esta opción es requerida',
	        requiredMessage: 'Esta opción es requerida'
	    });
	    
	    Ext.override(Ext.form.RadioGroup, {
	        blankText: 'Debe seleccionar una opción',
	        requiredMessage: 'Debe seleccionar una opción'
	    });
    	
    	Ext.apply(Ext.form.field.VTypes,
				{
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
    }
});
