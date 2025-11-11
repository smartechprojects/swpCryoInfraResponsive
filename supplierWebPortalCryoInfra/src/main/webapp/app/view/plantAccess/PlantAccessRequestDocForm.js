Ext.define('SupplierApp.view.plantAccess.PlantAccessRequestDocForm',	{
	extend : 'Ext.form.Panel',
	alias : 'widget.plantAccessRequestDocForm',
	border : false,
	frame : false,
	style : 'border: solid #fff 1px',
	autoScroll : true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    defaults: {
        anchor: '100%'
    },
	initComponent : function() {
		
		this.items = [{
			xtype: 'container',
            layout: 'anchor',
            defaults: {
                margin: '5 10 5 10'
            },
            items: [{
				xtype : 'displayfield',
				value : SuppAppMsg.supplierForm35,
				id:'textDocsRequest',
				height:20,
				margin:'5 10 10 10',
			    //flex: 1,
				//colspan:3,
				fieldStyle: 'font-weight:bold'
            },{//Oculto
				xtype: 'container',
				layout: 'hbox',
				id:'documentContainerDoc1',
				//colspan:3,
				//width:800,
				hidden:true,
				defaults : {
					//labelWidth : 150,
					//xtype : 'textfield',
					margin: '0 5 0 0'
				},
		        items:[{//	FORMATO DE INGRESO DE PROVEEDORES:
		        	xtype : 'textfield',
					fieldLabel : SuppAppMsg.plantAccess10,
					name : 'text_SI',
					id:'text_SI',
					//width:600,
					labelWidth:300,
					readOnly:true,
					flex: 1
				},{
					xtype: 'button',
					width:100,
					itemId : 'requestDOC_1',
					id : 'requestDOC_1',
					action : 'requestDOC_1',
					text : SuppAppMsg.supplierLoad,
				}]
            },{
				xtype: 'container',
				layout: 'hbox',
				id:'documentContainerDoc2',
				//colspan:3,
				//width:800,
				defaults : {
					//labelWidth : 150,
					//xtype : 'textfield',
					margin: '0 5 0 0'
				},
		        items:[{
		        	xtype : 'textfield',
		        	fieldLabel : SuppAppMsg.plantAccess11,
		        	name : 'text_PSUA',
		        	id:'text_PSUA',
		        	//width:600,
		        	labelWidth:300,
		        	readOnly:true,
		        	allowBlank:false,
				    flex: 1
		        },{
		        	xtype: 'button',
		        	width:100,
		        	itemId : 'requestDOC_2',
		        	id : 'requestDOC_2',
		        	action : 'requestDOC_2',
		        	text : SuppAppMsg.supplierLoad,
		        	cls: 'buttonStyle',
		        }]
            },{
				xtype: 'container',
				layout: 'hbox',
				id:'documentContainerDoc3',
				//colspan:3,
				//width:800,
				//margin:'5 10 5 10',
			   // flex: 1,
				defaults : {
					//labelWidth : 150,
					//xtype : 'textfield',
					//margin: '5 10 5 10'
					margin: '0 5 0 0'
				},
		        items:[{
		        	xtype : 'textfield',
					fieldLabel : 'SUA',
					name : 'text_SUA',
					id:'text_SUA',
					//width:600,
					labelWidth:300,
					readOnly:true,
					allowBlank:false,
					//margin: '5 10 5 10',
				    flex: 1,
				},{
					xtype: 'button',
					width:100,
					itemId : 'requestDOC_3',
					id : 'requestDOC_3',
					action : 'requestDOC_3',
					text : SuppAppMsg.supplierLoad,
					cls: 'buttonStyle',
				}]
            },{
				xtype: 'container',
				layout: 'hbox',
				id:'documentContainerDoc4',
				//colspan:3,
				//width:800,
				//margin:'5 10 5 10',
			    //flex: 1,
				defaults : {
					//labelWidth : 150,
					//xtype : 'textfield',
					//margin: '5 10 5 10'
					margin: '0 5 0 0'
				},
		        items:[{
		        	xtype : 'textfield',
		        	fieldLabel : SuppAppMsg.plantAccess12,
		        	name : 'text_FPCOPAA',
		        	id:'text_FPCOPAA',
		        	//width:600,
		        	labelWidth:300,
		        	readOnly:true,
		        	allowBlank:false,
		        	//margin: '5 10 5 10',
				    flex: 1,
		        },{
		        	xtype: 'button',
		        	width:100,
		        	itemId : 'requestDOC_4',
		        	id : 'requestDOC_4',
		        	action : 'requestDOC_4',
		        	text : SuppAppMsg.supplierLoad,
		        	cls: 'buttonStyle',
		        }]
            },{
				xtype: 'checkboxfield',
	            fieldLabel: SuppAppMsg.plantAccess18,
	            id: 'heavyEquipmentRequestDoc',
	            itemId: 'heavyEquipmentRequestDoc',
	            name:'heavyEquipmentRequestDoc',
	            action:'heavyEquipmentRequestDoc',
	            //width:650,
	            labelWidth:650,
	            margin:'5 10 5 10',
			    flex: 1,
	            listeners: {
	                click: {
	                    element: 'el', //bind to the underlying el property on the panel
	                    fn: function(){
//	                    	var radio = Ext.getCmp('heavyEquipmentRequestDoc').checked;	                    	
//	                    	if(radio){
//	                    		Ext.getCmp('text_SRC').allowBlank=false;
//	                    		Ext.getCmp('text_RM').allowBlank=false;
//	                    		Ext.getCmp('documentContainerDoc10').show();
//	                    		Ext.getCmp('documentContainerDoc12').show();
//	                    	} else {
//	                    		Ext.getCmp('text_SRC').allowBlank=true;
//	                    		Ext.getCmp('text_RM').allowBlank=true;
//	                    		Ext.getCmp('documentContainerDoc10').hide();
//	                    		Ext.getCmp('documentContainerDoc12').hide();
//	                    	}
//	                    	
//	                    	//Setea el valor en el formulario de PlantAccessRequestForm
//	                    	//para que tome el valor desde ese formulario.
//	                    	Ext.getCmp('heavyEquipmentRequest').setValue(radio);
//	                    	
//	                    	//Ejecuta actualización de la solicitud (Botón en PlantAccessRequestForm)
//	                		var btnUpdate = Ext.getCmp('updatePlantAccessRequest');
//	                		btnUpdate.fireEvent('click', btnUpdate);
	                    }
	                },
	            }
            },{
				xtype: 'container', 
				layout: 'hbox',
				id:'documentContainerDoc10',
				//colspan:3,
				//width:800,
				//margin:'5 10 5 10',
			    //flex: 1,
				hidden:true,
				defaults : {
					//labelWidth : 150,
					//xtype : 'textfield',
					//margin: '5 10 5 10'
					margin: '0 5 0 0'
				},
		        items:[{
		        	xtype : 'textfield',
		        	fieldLabel : SuppAppMsg.plantAccess19,
		        	name : 'text_SRC',
					id:'text_SRC',
					//width:600,
					labelWidth:300,
					readOnly:true,
					margin: '5 10 5 10',
				    flex: 1,
				},{
					xtype: 'button',
					width:100,
					itemId : 'requestDOC_10',
					id : 'requestDOC_10',
					action : 'requestDOC_10',
					text : SuppAppMsg.supplierLoad,
					cls: 'buttonStyle',
				}]
            },{
				xtype: 'container',
				layout: 'hbox',
				id:'documentContainerDoc12',
				//colspan:3,				
				//width:800,
				//margin:'5 10 5 10',
			    //flex: 1,
				hidden:true,
				defaults : {
					//labelWidth : 150,
					//xtype : 'textfield',
					//margin: '5 10 5 10'
					margin: '0 5 0 0'
				},
		        items:[{
		        	xtype : 'textfield',
		        	fieldLabel : SuppAppMsg.plantAccess21,
		        	name : 'text_RM',
					id:'text_RM',
					//width:600,
					labelWidth:300,
					readOnly:true,
					//margin: '5 10 5 10',
				    flex: 1,
				},{
					xtype: 'button',
					width:100,
					itemId : 'requestDOC_12',
					id : 'requestDOC_12',
					action : 'requestDOC_12',
					text : SuppAppMsg.supplierLoad,
					cls: 'buttonStyle',
				}]
            },{
				xtype: 'container',
				layout: 'hbox',
				//colspan:3,
				//width:800,
				margin: '0 5 0 0',
			    //flex: 1,
		        items:[{
	                xtype: 'button',
	                text: SuppAppMsg.plantAccess88,
	                icon: 'resources/images/doc.png',	                
	                action: 'updatePlantAccessRequestDoc',
	                id: 'updatePlantAccessRequestDoc',
	                width: 120,
	                margin: '0 0 0 0',
	                cls: 'buttonStyle',
				    //flex: 1,
	            }]
            }]
		}]
		
		this.callParent(arguments);
	}
});