Ext.define('SupplierApp.view.plantAccess.PlantAccessWorkerForm',	{
	extend : 'Ext.form.Panel',
	alias : 'widget.plantAccessWorkerForm',
	border : false,
	frame : false,
	style : 'border: solid #ccc 1px',
	autoScroll : true,
	initComponent : function() {
		
		this.dockedItems = [{
			xtype: 'hidden',
            id: 'pawUpdateFunctionOn',
            itemId: 'pawUpdateFunctionOn',
			name: 'pawUpdateFunctionOn'
		},{
			xtype: 'hidden',
            id: 'pawTempId',
            itemId: 'pawTempId',
			name: 'tempId'
		},{
			xtype: 'hidden',
            id: 'pawRequestNumber',
            itemId: 'pawRequestNumber',
			name: 'requestNumber'
		},{
			xtype: 'hidden',
            id: 'pawActivities',
            itemId: 'pawActivities',
			name: 'activities'
		},{
			xtype: 'hidden',
            id: 'pawListDocuments',
            itemId: 'pawListDocuments',
			name: 'listDocuments'			
		},{
			xtype: 'checkboxfield',
            id: 'pawAllDocuments',
            itemId: 'pawAllDocuments',
			name: 'allDocuments',
			hidden: true
		},{
			xtype: 'toolbar',
            id:'formWorkAccessPlantTitle',
            style: {
            	background: '#CCCCCC'
            },
            dock: 'top',
            items: [{
				xtype : 'displayfield',
				value : SuppAppMsg.plantAccess22,
				height:20,
				margin:'5 10 0 300',
				colspan:3,
				fieldStyle: 'font-weight:bold'
			}]
		},{
            xtype: 'toolbar',
            id:'formWorkAccessPlantBody',
            style: {
            	background: 'white'
            },
            dock: 'top',
            items: [{
            	xtype: 'textfield',
                fieldLabel: SuppAppMsg.plantAccess65,
                id: 'pawEmployeeId',
                itemId: 'pawEmployeeId',
                name:'pawEmployeeId',
                hidden:true,
                //width:300,
                flex : 1,
                labelWidth:130,
                margin:'5 10 0 10'
            },{
    			xtype: 'textfield',
                fieldLabel: SuppAppMsg.plantAccess65,
                id: 'pawEmployeeName',
                itemId: 'pawEmployeeName',
                name:'employeeName',
                allowBlank:false,
                //width:300,
                flex : 1,
                labelWidth:130,
                margin:'5 10 0 10',
                autoComplete: 'off',
                enforceMaxLength: true,
                	maxLength : 100,
                    validator: function(value) {
                        if (value.length >100) {
                            return 'solo menos de 100 caracteres';
                        }
                        return true;
                    }
            },{
    			xtype: 'textfield',
                fieldLabel: SuppAppMsg.plantAccess66,
                id: 'pawEmployeeLastName',
                itemId: 'pawEmployeeLastName',
                name:'employeeLastName',
                allowBlank:false,
                //width:300,
                flex : 1,
                labelWidth:130,
                margin:'5 10 0 10',
                autoComplete: 'off',
                maxLength : 50,
                enforceMaxLength: true,
                validator: function(value) {
                    if (value.length >50) {
                        return 'solo menos de 50 caracteres';
                    }
                    return true;
                }
            },{
    			xtype: 'textfield',
                fieldLabel: SuppAppMsg.plantAccess67,
                id: 'pawEmployeeSecondLastName',
                itemId: 'pawEmployeeSecondLastName',
                name:'employeeSecondLastName',
                allowBlank:false,
                //width:300,
                flex : 1,
                labelWidth:130,
                margin:'5 10 0 10',
                autoComplete: 'off',
                maxLength : 50,
                enforceMaxLength: true,
                validator: function(value) {
                    if (value.length >51) {
                        return 'solo menos de 50 caracteres';
                    }
                    return true;
                }
            }]
		},{
            xtype: 'toolbar',
            id:'formWorkAccessPlantBody2',
            style: {
            	background: 'white'
            },
            dock: 'top',
            items: [{
            	xtype: 'textfield',
            	fieldLabel: SuppAppMsg.plantAccess24,
            	id: 'pawMembershipIMSS',
            	itemId: 'pawMembershipIMSS',
            	name: 'membershipIMSS',
            	allowBlank: false,
            	//width: 300,
            	flex : 1,
            	labelWidth: 130,
            	margin: '5 10 0 10',
            	maskRe: /[0-9]/, // Solo permite números mientras se escribe
  	            regex: /^\d{0,11}$/, // Permite solo hasta 10 dígitos
  	            regexText: 'Solo se permiten números',
            	validator: function(value) {
            		if (value.length !== 11) {
            			return 'El&nbsp;valor&nbsp;debe&nbsp;tener&nbsp;11&nbsp;caracteres<br>';
            		}
            		return true;
            	},
            	enforceMaxLength: true,
            	maxLength: 11,
            	autoComplete: 'off'
            },{
            	xtype: 'datefield',
            	fieldLabel: SuppAppMsg.plantAccess68,
            	id: 'pawDateInduction',
            	itemId: 'pawDateInduction',
            	name: 'dateInduction',
            	allowBlank: false,
            	//width: 300,
            	flex : 1,
            	labelWidth: 130,
            	margin: '5 10 0 10',
            	format: 'd-m-Y',
            	autoComplete: 'off',
                minValue: Ext.Date.add(new Date(), Ext.Date.YEAR, -1), // Fecha mínima, un año atras a partir de hoy
                maxValue: new Date(), // Fecha máxima, hoy
            	enableKeyEvents: true,
            	listeners: {
            		keydown: function (field, e) {
            			e.preventDefault();
            		}
            	}
            },{
            	xtype: 'datefield',
            	fieldLabel: SuppAppMsg.plantAccess68,
            	id: 'pawDatefolioIDcard',
            	itemId: 'pawDatefolioIDcard',
            	name: 'datefolioIDcard',
            	//allowBlank: false,
            	//width: 300,
            	flex : 1,
            	labelWidth: 130,
            	margin: '5 10 0 10',
            	format: 'd-m-Y',
            	autoComplete: 'off',
            	enableKeyEvents: true,
            	listeners: {
            		keydown: function (field, e) {
            			e.preventDefault();
            		}
            	},
            	hidden: true,
            	maxLength : 50,
            	enforceMaxLength: true,
                validator: function(value) {
                    if (value.length >50) {
                        return 'solo menos de 50 caracteres';
                    }
                    return true;
                }
            },{
            	xtype: 'textfield',
                fieldLabel: SuppAppMsg.plantAccess62,
                id: 'pawCardNumber',
                itemId: 'pawCardNumber',
                name:'cardNumber',
                allowBlank:false,
                maxLength: 50,
                //width:300,
                flex : 1,
                labelWidth:130,
                margin:'5 10 0 10',
                autoComplete: 'off',
                enforceMaxLength: true,
                validator: function(value) {
                    if (value.length >50) {
                        return 'solo menos de 50 caracteres';
                    }
                    return true;
                }
                
            }]
		},
		{
            xtype: 'toolbar',
            id:'formWorkAccessPlantBody3',
            style: {
            	background: 'white'
            },
            dock: 'top',
            items: [{
                xtype: 'textfield',
                fieldLabel: SuppAppMsg.plantAccess71,
                id: 'pawEmployeeCurp',
                itemId: 'pawEmployeeCurp',
                name: 'employeeCurp',
                allowBlank: false,
                //width: 300,
                flex : 1,
                labelWidth: 130,
                margin: '5 10 0 10',
                autoComplete: 'off',
                validator: function(value) {
                    // Expresión regular para validar el formato del CURP
                    var curpPattern = /^[A-Z]{4}\d{6}[H|M][A-Z]{5}[A-Z0-9]{2}$/;
                    				

                    if (value.length !== 18) {
                        return 'El valor debe tener 18 caracteres<br>';
                    }
                    if (!curpPattern.test(value)) {
                        return 'Formato de CURP incorrecto<br>';
                    }
                    return true;
                },
                enforceMaxLength: true,
                maxLength: 18,
            }
            ,{
                xtype: 'textfield',
                fieldLabel: SuppAppMsg.plantAccess72,
                id: 'pawEmployeeRfc',
                itemId: 'pawEmployeeRfc',
                name: 'employeeRfc',
                allowBlank: false,
                //width: 300,
                flex : 1,
                labelWidth: 130,
                margin: '5 10 0 10',
                autoComplete: 'off',
                validator: function(value) {
                    // Expresión regular para validar el formato del RFC
                    var rfcPattern = /^[A-ZÑ&]{3,4}\d{6}[A-Z0-9]{3}$/;
                    if (value.length !== 12 && value.length !== 13) {
                        return 'El valor debe tener 12 o 13 caracteres<br>';
                    }
                    if (!rfcPattern.test(value)) {
                        return 'Formato de RFC incorrecto<br>';
                    }
                    return true;
                },
                enforceMaxLength: true,
                maxLength: 13,
            },{
    			xtype: 'textfield',
                fieldLabel: SuppAppMsg.plantAccess73,
                id: 'pawEmployeePuesto',
                itemId: 'pawEmployeePuesto',
                name:'employeePuesto',
                allowBlank:false,
                //width:300,
                flex : 1,
                labelWidth:130,
                margin:'5 10 0 10',
                autoComplete: 'off',
                maxLength: 50,
                enforceMaxLength: true,
                validator: function(value) {
                    if (value.length >50) {
                        return 'solo menos de 50 caracteres';
                    }
                    return true
                }
            },{xtype: 'textfield',
            fieldLabel: "ordenesTrabajador",
            id: 'pawEmployeeOrdenes',
            itemId: 'pawEmployeeOrdenes',
            name:'employeeOrdenes',
            allowBlank:false,
            hidden:true,
            //width:300,
            flex : 1,
            labelWidth:130,
            margin:'5 10 0 10',
            autoComplete: 'off'}]
		},
		{
            xtype: 'gridpanel',
            title: 'Orden(es) a atender:',
            id: 'selectionGrid',
            name: 'selectionGrid',
            store: Ext.create('Ext.data.Store', {
                fields: ['order', 'description'],
                data: [] // Datos iniciales vacíos
            }),
            selModel: {
                selType: 'checkboxmodel',
                mode: 'MULTI',
                listeners: {
                    selectionchange: function(selModel, selectedRecords) {
                    	debugger
                    	
                    	  var concatenatedData = '';
                        var store = Ext.getCmp('selectionGrid').getStore(); // Obtener la tienda del grid
                        for (var i = 0; i < selectedRecords.length; i++) {
                            var record = selectedRecords[i];
                            var order = record.get('order');
                            var description = record.get('description');
                            concatenatedData += order + ',' + description;
                            if (i < selectedRecords.length - 1) {
                                concatenatedData += '|';
                            }
                        }
                        Ext.getCmp('pawEmployeeOrdenes').setValue(concatenatedData);
                       debugger
                        console.log("Cantidad de elementos seleccionados: " + selectedRecords.length);
                    }
                }
            },
            columns: [{
                text: 'Orden',
                dataIndex: 'order',
                flex: 1
            }, {
                text: 'Descripción',
                dataIndex: 'description',
                flex: 2
            }],
            height: 100,
            //width: 500,
            flex : 1,
            margin: '5 10 0 10'
        }
		,{
            xtype: 'toolbar',
            id:'textcheckworkers',
            style: {
            	background: '#CCCCCC'
            },
            dock: 'top',
            items: [{
            	xtype : 'displayfield',
				value : SuppAppMsg.plantAccess27,
				height:20,
				margin:'5 10 0 300',
				colspan:3,
				fieldStyle: 'font-weight:bold'
			}]
		},{
            xtype: 'toolbar',
            id:'checkworkers1',
            style: {
            	background: 'white'
            },
            dock: 'top',
            items: [{
            	xtype: 'checkboxfield',
            	fieldLabel: SuppAppMsg.plantAccess28,
            	id: 'pawDocsActivity1',
            	itemId: 'pawDocsActivity1',
            	action:'pawDocsActivity1',
            	name:'docsActivity1',
            	//width:400,
            	flex : 1,
            	labelWidth:400,
            	margin:'5 10 0 10'
            },{
            	xtype: 'checkboxfield',
            	fieldLabel: '2.-' + SuppAppMsg.plantAccess18,
            	id: 'pawDocsActivity2',
            	itemId: 'pawDocsActivity2',
            	action:'pawDocsActivity2',
            	name:'docsActivity2',
            	//width:500,
            	flex : 1,
            	labelWidth:500,
            	margin:'5 10 0 75'
            }]
		},{
			xtype: 'toolbar',
			id:'checkworkers2',
			style: {
				background: 'white'
			},
			dock: 'top',
			items: [{
				xtype: 'checkboxfield',
				fieldLabel: SuppAppMsg.plantAccess29,
				id: 'pawDocsActivity3',
				itemId: 'pawDocsActivity3',
				action:'pawDocsActivity3',
				name:'docsActivity3',
				//width:400,
				flex : 1,
				labelWidth:400,
				margin:'5 10 0 10'
			},{
				xtype: 'checkboxfield',
				fieldLabel: SuppAppMsg.plantAccess30,
				id: 'pawDocsActivity4',
				itemId: 'pawDocsActivity4',
				action:'pawDocsActivity4',
				name:'docsActivity4',
				//width:500,
				flex : 1,
				labelWidth:500,
				margin:'5 10 0 75'
			}]			
		},{
            xtype: 'toolbar',
            id:'checkworkers3',
            style: {
            	background: 'white'
            },
            dock: 'top',
            items: [{
            	xtype: 'checkboxfield',
            	fieldLabel: SuppAppMsg.plantAccess31,
            	id: 'pawDocsActivity5',
            	itemId: 'pawDocsActivity5',
            	action:'pawDocsActivity5',
            	name:'docsActivity5',
            	//width:400,
            	flex : 1,
            	labelWidth:400,
            	margin:'5 10 0 10'
            },{
            	xtype: 'checkboxfield',
            	fieldLabel: SuppAppMsg.plantAccess32,
            	id: 'pawDocsActivity6',
            	itemId: 'pawDocsActivity6',
            	action:'pawDocsActivity6',
            	name:'docsActivity6',
            	//width:500,
            	flex : 1,
            	labelWidth:500,
            	margin:'5 10 0 75'
            }]
		},{
			xtype: 'toolbar',
			id:'checkworkers4',
            style: {
            	background: 'white'
            },
            dock: 'top',
            items: [{
            	xtype: 'checkboxfield',
            	fieldLabel: SuppAppMsg.plantAccess52,
            	id: 'pawDocsActivity7',
            	itemId: 'pawDocsActivity7',
            	action:'pawDocsActivity7',
            	name:'docsActivity7',
            	//width:400,
            	flex : 1,
            	labelWidth:400,
            	margin:'5 10 0 10'
            },{
            	iconCls : 'icon-add',
            	itemId : 'updatePlantAccessWorkerAct',
            	id : 'updatePlantAccessWorkerAct',
            	text : 'Continuar',//SuppAppMsg.plantAccess26,
            	action : 'addPlantAccessRequestEmployBtnAct',
            	//width:520,
            	flex : 1,
            	labelWidth:400,
            	margin:'5 10 0 75',
            	hidden: true
            }]
		},{
			xtype: 'toolbar',
			id:'titleUploadFileWorker',
			name:'titleUploadFileWorker',
			style: {
				background: '#CCCCCC'
			},
			dock: 'top',
			items: [{
				xtype : 'displayfield',
				value : 'Archivos a cargar del trabajador',
				height:20,
				margin:'5 10 0 300',
				colspan:3,
				fieldStyle: 'font-weight:bold'
			}]
		},{
			//Carga de documentos para trabajador
		     xtype: 'panel',
		     layout: {
		         type: 'vbox',
		         //align: 'stretch'
		     },
		     id: 'bodyeaderFileWorker',
		     name:'bodyeaderFileWorker',
		     //width: 800,
		     flex : 1,
		     //height: 100,
		     margin: '0 0 0 10',
		     dock: 'top',
		     items: [{
		    	 xtype: 'toolbar',
		    	 id:'toolDoc1',
		    	 style: {
		    		 background: 'white'
		    	 },
		    	 dock: 'top',
		    	 items: [{
		    		 xtype: 'container',
		    		 layout: 'hbox',
		    		 id:'documentContainerWorkerDoc1',
		    		 colspan:3,
		    		 //width:1000,
		    		 flex : 1,
		    		 margin:'0 0 0 10',
		    		 defaults : {
		    			 labelWidth : 150,
		    			 xtype : 'textfield',
		    			 margin: '0 0 0 0'
		    		 },
		    		 items:[{
		    			 xtype : 'textfield',
		    			 fieldLabel : SuppAppMsg.plantAccess35,
		    			 name : 'text_CMA',
		    			 id:'text_CMA',
		    			 //width:600,
		    			 flex : 1,
		    			 labelWidth:300,
		    			 readOnly:true,
		    			 margin: '0 0 0 0',
		    		 },{
		    			 xtype: 'button',
		    			 //width:100,
		    			 flex : 1,
		    			 itemId : 'workerDOC_1',
		    			 id : 'workerDOC_1',
		    			 action : 'workerDOC_1',
		    			 text : SuppAppMsg.supplierLoad,
		    			 cls: 'buttonStyle',
		    		 }]
		    	 }]
		     },{
		    	 xtype: 'toolbar',
		    	 id:'toolDoc2',
		    	 style: {
		    		 background: 'white'
		    	 },
		    	 dock: 'top',
		    	 items: [{
		    		 xtype: 'container',
		    		 layout: 'hbox',
		    		 id:'documentContainerWorkerDoc2',
		    		 colspan:3,
		    		 //width:1000,
		    		 flex : 1,
		    		 margin:'0 0 0 10',
		    		 defaults : {
		    			 labelWidth : 150,
		    			 xtype : 'textfield',
		    			 margin: '0 0 0 0'
		    		 },
		    		 items:[{
		    			 xtype : 'textfield',
		    			 fieldLabel : SuppAppMsg.plantAccess36,
		    			 name : 'text_CI',
		    			 id:'text_CI',
		    			 //width:600,
		    			 flex : 1,
		    			 labelWidth:300,
		    			 readOnly:true,
		    			 margin: '0 0 0 0',
		    		 },{
		    			 xtype: 'button',
		    			 width:100,
		    			 itemId : 'workerDOC_2',
		    			 id : 'workerDOC_2',
		    			 action : 'workerDOC_2',
		    			 text : SuppAppMsg.supplierLoad,
		    			 cls: 'buttonStyle',
		    		 }]
		    	 }]
		     },{
		    	 xtype: 'toolbar',
		    	 id:'toolDoc6',
		    	 hidden:true,
		    	 style: {
		    		 background: 'white'
		    	 },
		    	 dock: 'top',
		    	 items: [{
		    		 xtype: 'container',
		    		 layout: 'hbox',
		    		 id:'documentContainerWorkerDoc6',
		    		 colspan:3,
		    		 //width:1000,
		    		 flex : 1,
		    		 margin:'0 0 0 10',
		    		 defaults : {
		    			 labelWidth : 150,
		    			 xtype : 'textfield',
		    			 margin: '0 0 0 0'
		    		 },
		    		 items:[{
		    			 xtype : 'textfield',
		    			 fieldLabel : SuppAppMsg.plantAccess40,
		    			 name : 'text_CM1',
		    			 id:'text_CM1',
		    			 width:600,
		    			 labelWidth:300,
		    			 readOnly:true,
		    			 margin: '0 0 0 0',
		    		 },{
		    			 xtype: 'button',
		    			 //width:100,
		    			 flex : 1,
		    			 itemId : 'workerDOC_6',
		    			 id : 'workerDOC_6',
		    			 action : 'workerDOC_6',
		    			 text : SuppAppMsg.supplierLoad,
		    			 cls: 'buttonStyle',
		    		 }]
		    	 }]
		     },{
		    	 xtype: 'toolbar',
		    	 id:'toolDoc7',
		    	 hidden:true,
		    	 style: {
		    		 background: 'white'
		    	 },
		    	 dock: 'top',
		    	 items: [{
		    		 xtype: 'container',
		    		 layout: 'hbox',
		    		 id:'documentContainerWorkerDoc7',
		    		 colspan:3,
		    		 //width:1000,
		    		 flex : 1,
		    		 margin:'0 0 0 10',
		    		 defaults : {
		    			 labelWidth : 150,
		    			 xtype : 'textfield',
		    			 margin: '0 0 0 0'
		    		 },
		    		 items:[{
		    			 xtype : 'textfield',
		    			 fieldLabel : SuppAppMsg.plantAccess41,
		    			 name : 'text_CD3TA',
		    			 id:'text_CD3TA',
		    			 //width:600,
		    			 flex : 1,
		    			 labelWidth:300,
		    			 readOnly:true,
		    			 margin: '0 0 0 0',
		    		 },{
		    			 xtype: 'button',
		    			 width:100,
		    			 itemId : 'workerDOC_7',
		    			 id : 'workerDOC_7',
		    			 action : 'workerDOC_7',
		    			 text : SuppAppMsg.supplierLoad,
		    			 cls: 'buttonStyle',
		    		 }]
		    	 }]
		     },{
		    	 xtype: 'toolbar',
		    	 id:'toolDoc8',
		    	 hidden:true,
		    	 style: {
		    		 background: 'white'
		    	 },
		    	 dock: 'top',
		    	 items: [{
		    		 xtype: 'container',
		    		 layout: 'hbox',
		    		 id:'documentContainerWorkerDoc8',
		    		 colspan:3,
		    		 //width:1000,
		    		 flex : 1,
		    		 margin:'0 0 0 10',
		    		 defaults : {
		    			 labelWidth : 150,
		    			 xtype : 'textfield',
		    			 margin: '0 0 0 0'
		    		 },
		    		 items:[{
		    			 xtype : 'textfield',
		    			 fieldLabel : SuppAppMsg.plantAccess42,
		    			 name : 'text_CD3G',
		    			 id:'text_CD3G',
		    			 //width:600,
		    			 flex : 1,
		    			 labelWidth:300,
		    			 readOnly:true,
		    			 margin: '0 0 0 0',
		    		 },{
		    			 xtype: 'button',
		    			 width:100,
		    			 itemId : 'workerDOC_8',
		    			 id : 'workerDOC_8',
		    			 action : 'workerDOC_8',
		    			 text : SuppAppMsg.supplierLoad,
		    			 cls: 'buttonStyle',
		    		 }]
		    	 }]
		     },{
		    	 xtype: 'toolbar',
		    	 id:'toolDoc9',
		    	 hidden:true,
		    	 style: {
		    		 background: 'white'
		    	 },
		    	 dock: 'top',
		    	 items: [{
		    		 xtype: 'container',
		    		 layout: 'hbox',
		    		 id:'documentContainerWorkerDoc9',
		    		 colspan:3,
		    		 //width:1000,
		    		 flex : 1,
		    		 margin:'0 0 0 10',
		    		 defaults : {
		    			 labelWidth : 150,
		    			 xtype : 'textfield',
		    			 margin: '0 0 0 0'
		    		 },
		    		 items:[{
		    			 xtype : 'textfield',
		    			 fieldLabel : SuppAppMsg.plantAccess43,
		    			 name : 'text_CD3TEC',
		    			 id:'text_CD3TEC',
		    			 //width:600,
		    			 flex : 1,
		    			 labelWidth:300,
		    			 readOnly:true,
		    			 margin: '0 0 0 0',
		    		 },{
		    			 xtype: 'button',
		    			 width:100,
		    			 itemId : 'workerDOC_9',
		    			 id : 'workerDOC_9',
		    			 action : 'workerDOC_9',
		    			 text : SuppAppMsg.supplierLoad,
		    			 cls: 'buttonStyle',
		    		 }]
		    	 }]
		     },{
		    	 xtype: 'toolbar',
		    	 id:'toolDoc10',
		    	 hidden:true,
                 style: {
                	 background: 'white'
                 },
                 dock: 'top',
                 items: [{
                	 xtype: 'container',
                	 layout: 'hbox',
                	 id:'documentContainerWorkerDoc10',
                	 colspan:3,
                	 //width:1000,
                	 flex : 1,
                	 margin:'0 0 0 10',
                	 defaults : {
                		 labelWidth : 150,
                		 xtype : 'textfield',
                		 margin: '0 0 0 0'
                	 },
                	 items:[{
                		 xtype : 'textfield',
                		 fieldLabel : SuppAppMsg.plantAccess44,
                		 name : 'text_CD3TE1',
                		 id:'text_CD3TE1',
                		 //width:600,
                		 flex : 1,
                		 labelWidth:300,
                		 readOnly:true,
                		 margin: '0 0 0 0',
                	 },{
                		 xtype: 'button',
                		 //width:100,
                		 flex : 1,
                		 itemId : 'workerDOC_10',
                		 id : 'workerDOC_10',
                		 action : 'workerDOC_10',
                		 text : SuppAppMsg.supplierLoad,
                		 cls: 'buttonStyle',
                	 }]
                 }]
		     },{
		    	 xtype: 'toolbar',
		    	 id:'toolDoc11',
		    	 hidden:true,
		    	 style: {
		    		 background: 'white'
		    	 },
		    	 dock: 'top',
		    	 items: [{
		    		 xtype: 'container',
		    		 layout: 'hbox',
		    		 id:'documentContainerWorkerDoc11',
		    		 colspan:3,
		    		 //width:1000,
		    		 flex : 1,
		    		 margin:'0 0 0 10',
		    		 defaults : {
		    			 labelWidth : 150,
		    			 xtype : 'textfield',
		    			 margin: '0 0 0 0'
		    		 },
		    		 items:[{
		    			 xtype : 'textfield',
		    			 fieldLabel : SuppAppMsg.plantAccess45,
		    			 name : 'text_CD3TC',
		    			 id:'text_CD3TC',
		    			 //width:600,
		    			 flex : 1,
		    			 labelWidth:300,
		    			 readOnly:true,
		    			 margin: '0 0 0 0',
		    		 },{
		    			 xtype: 'button',
		    			 width:100,
		    			 itemId : 'workerDOC_11',
		    			 id : 'workerDOC_11',
		    			 action : 'workerDOC_11',
		    			 text : SuppAppMsg.supplierLoad,
		    			 cls: 'buttonStyle',
		    		 }]
		    	 }]
		     },{
		    	 xtype: 'toolbar',
		    	 id:'toolDoc12',
		    	 hidden:true,
		    	 style: {
		    		 background: 'white'
		    	 },
		    	 dock: 'top',
		    	 items: [{
		    		 xtype: 'container',
		    		 layout: 'hbox',
		    		 id:'documentContainerWorkerDoc12',
		    		 colspan:3,
		    		 //width:1000,
		    		 flex : 1,
		    		 margin:'0 0 0 10',
		    		 defaults : {
		    			 labelWidth : 150,
		    			 xtype : 'textfield',
		    			 margin: '0 0 0 0'
		    		 },
		    		 items:[{
		    			 xtype : 'textfield',
		    			 fieldLabel : SuppAppMsg.plantAccess46,
		    			 name : 'text_HS',
		    			 id:'text_HS',
		    			 //width:600,
		    			 flex : 1,
		    			 labelWidth:300,
		    			 readOnly:true,
		    			 margin: '0 0 0 0',
		    		 },{
		    			 xtype: 'button',
		    			 width:100,
		    			 itemId : 'workerDOC_12',
		    			 id : 'workerDOC_12',
		    			 action : 'workerDOC_12',
		    			 text : SuppAppMsg.supplierLoad,
		    			 cls: 'buttonStyle',
		    		 }]
		    	 }]
		     },{
		    	 xtype: 'toolbar',
		    	 id:'toolDoc13',
		    	 hidden:true,
		    	 style: {
		    		 background: 'white'
		    	 },
		    	 dock: 'top',
		    	 items: [{
		    		 xtype: 'container',
		    		 layout: 'hbox',
		    		 id:'documentContainerWorkerDoc13',
		    		 colspan:3,
		    		 //width:1000,
		    		 flex : 1,
		    		 margin:'0 0 0 10',
		    		 defaults : {
		    			 labelWidth : 150,
		    			 xtype : 'textfield',
		    			 margin: '0 0 0 0'
		    		 },
		    		 items:[{
		    			 xtype : 'textfield',
		    			 fieldLabel : SuppAppMsg.plantAccess70,
		    			 name : 'text_AE',
		    			 id:'text_AE',
		    			 //width:600,
		    			 flex : 1,
		    			 labelWidth:300,
		    			 readOnly:true,
		    			 hidden: role == 'ROLE_PURCHASE_READ' ?true:false,		    			
		    			 margin: '0 0 0 0',
		    		 },{
		    			 xtype: 'button',
		    			 width:100,
		    			 itemId : 'workerDOC_13',
		    			 id : 'workerDOC_13',
		    			 action : 'workerDOC_13',
		    			 hidden: role == 'ROLE_PURCHASE_READ' ?true:false,
		    			 text : SuppAppMsg.supplierLoad,
		    			 cls: 'buttonStyle',
		    		 }]
		    	 }]
		     }],
		     //autoScroll: true
		}]
		
		this.callParent(arguments);
	}
});