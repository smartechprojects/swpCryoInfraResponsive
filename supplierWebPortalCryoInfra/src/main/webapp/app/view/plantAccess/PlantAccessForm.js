Ext.define('SupplierApp.view.plantAccess.PlantAccessForm',	{
	extend : 'Ext.form.Panel',
	alias : 'widget.plantAccessForm',
	border : false,
	frame : false,
	style : 'border: solid #ccc 1px',
	autoScroll : true,
	initComponent : function() {
	
		
		this.items = [];
		  var firstContainer = Ext.create('Ext.container.Container', {
	            layout: 'anchor',
	            id:'datosRegister',
	            margin: '10 0 0 0',
	            items: [
	            	
	            	{
	    				xtype : 'displayfield',
	    				value : SuppAppMsg.plantAccess6,
	    				height:20,
	    				//margin: '50 0 0 10',
	    				margin:'5 10 0 300',
	    				colspan:3,
	    				fieldStyle: 'font-weight:bold'
	    				},
	    				{
	    					xtype: 'textfield',
	    		            fieldLabel: 'RFC',
	    		            id: 'rfcPlantAccess',
	    		            itemId: 'rfcPlantAccess',
	    		            name:'rfcPlantAccess',
	    		            width:8,
	    		            labelWidth:100,
	    		            margin:'5 10 0 10',
	    		            hidden:true
	    				},{
	    					xtype: 'textfield',
	    		            fieldLabel: 'Estatus',
	    		            id: 'estatusPlantAccess',
	    		            itemId: 'estatusPlantAccess',
	    		            name:'estatusPlantAccess',
	    		            width:8,
	    		            labelWidth:100,
	    		            margin:'5 10 0 10',
	    		            hidden:true
	    				},
	    				{
	    					xtype: 'textfield',
	    		            fieldLabel: 'addressNumber',
	    		            id: 'addressNumberPA',
	    		            itemId: 'addressNumberPA',
	    		            name:'addressNumberPA',
	    		            width:300,
	    		            labelWidth:100,
	    		            margin:'5 10 0 10',
	    		            hidden:true
	    				},
	    				{
	    					xtype: 'textfield',
	    		            fieldLabel: SuppAppMsg.plantAccess2,
	    		            id: 'addnameRequest',
	    		            itemId: 'addnameRequest',
	    		            name:'addnameRequest',
	    		            width:800,
	    		            maxLength : 254,
	    		            labelWidth:150,
	    		            margin:'5 10 0 150',
	    		            //readOnly:true
	    		            allowBlank:false,
	    				},
	    				{
	    					xtype: 'textfield',
	    		            fieldLabel: SuppAppMsg.purchaseOrderNumber,
	    		            id: 'addnordenNumber',
	    		            itemId: 'addnordenNumber',
	    		            name:'addnordenNumber',
	    		            allowBlank:false,
	    		            maxLength : 254,
	    		            width:800,
	    		            labelWidth:150,
	    		            margin:'5 10 0 150',
	    				},
	    				{
	    					xtype: 'textfield',
	    		            fieldLabel: SuppAppMsg.plantAccess7,
	    		            id: 'addcontractorCompany',
	    		            itemId: 'addcontractorCompany',
	    		            name:'addcontractorCompany',
	    		            allowBlank:false,
	    		            width:800,
	    		            maxLength : 254,
	    		            labelWidth:150,
	    		            margin:'5 10 0 150',
	    				},
	    				{
	    					xtype: 'textfield',
	    		            fieldLabel: SuppAppMsg.plantAccess8,
	    		            id: 'addcontractorRepresentative',
	    		            itemId: 'addcontractorRepresentative',
	    		            name:'addcontractorRepresentative',
	    		            width:800,
	    		            maxLength : 254,
	    		            labelWidth:150,
	    		            margin:'5 10 0 150',
	    		            allowBlank:false,
	    				},
	    				{
	    					xtype: 'textarea',
	    		            fieldLabel: SuppAppMsg.plantAccess9,
	    		            id: 'adddescriptionUbication',
	    		            itemId: 'adddescriptionUbication',
	    		            name:'adddescriptionUbication',
	    		            maxLength : 254,
	    		            allowBlank:false,
	    		            //labelWidth:100,
	    		            width:800,
	    		            labelWidth:150,
	    		            margin:'5 10 0 150',
	    				},{

	    					fieldLabel : 'Planta',
	    					name : 'plantRequest',
	    					id : 'plantRequest',
	    					itemId : 'plantRequest',
	    					xtype: 'combobox',
	    					typeAhead: true,
	    	                typeAheadDelay: 100,
	    	                allowBlank:false,
	    	                minChars: 1,
	    	                queryMode: 'local',
	    	                //forceSelection: true,
	    					store : getAutoLoadUDCStore('PLANTCRYO', '', '', ''),
	    	                displayField: 'strValue1',
	    	                valueField: 'udcKey',
	    	                labelWidth:150,
	    	                width : 400,
	    	                typeAheadDelay: 100,
	    	                margin:'5 10 0 150',
	    	                //editable: false,
	    					//colspan:3,
	    					listeners: {
	    				    	select: function (comboBox, records, eOpts) {
	    				    		//var contrib = records[0].data.udcKey;
	    				    		//Ext.getCmp('addAproval').setValue(records[0].data.strValue2);
	    				    	}
	    				    }
	    				
	    					},
	    					{
	    						xtype: 'textfield',
	    						fieldLabel : SuppAppMsg.plantAccess3,
	    			            id: 'addAproval',
	    			            itemId: 'addAproval',
	    			            name:'addAproval',
	    			            width:400,
	    			            labelWidth:150,
	    			            readOnly:true,
	    			            margin:'5 10 0 150',
	    			            hidden:true
	    			            //hidden:true
	    					},{
	    		                xtype: 'button',
	    		                width: 150,
	    		                icon: 'resources/images/doc.png',
	    		                text: 'Cargar Documentación',
//	    		                action: 'uploadFileRequest',
	    		                id: 'uploadFileShow',
	    		                margin: '10 10 10 150',
	    		                handler: function () {
	    		                    secondContainer.show();
	    		                    firstContainer.hide();
	    		                },
	    		                action:'uploadPlantAccessRequestActHeader'	    		                
	    		            }
	            ]
	        });
		  
		  
		  
		  var secondContainer = Ext.create('Ext.container.Container', {
	            id: 'documentContainer',
	            name:'documentContainer',
	            layout: 'fit',
	            margin: '10 0 0 0',
	            hidden: true,
	            items: [
		        	{
						xtype : 'displayfield',
						value : SuppAppMsg.supplierForm35,
						id:'textDocsRequest',
						height:20,
						//margin: '50 0 0 10',
						margin:'5 10 0 300',
						colspan:3,
						fieldStyle: 'font-weight:bold'
					},
//					{ 
//						xtype: 'container',
//						layout: 'hbox',
//						id:'documentContainerDoc1',
//						colspan:3,
//						width:800,
//						margin:'10 0 10 150',
//						//hidden:role=='ANONYMOUS'?false:true,
//						defaults : {
//							labelWidth : 150,
//							xtype : 'textfield',
//							margin: '12 0 0 0'
//						},
//				        items:
//				        	[
//				        	{
////				        		FORMATO DE INGRESO DE PROVEEDORES:
//				        	xtype : 'textfield',
//							fieldLabel : SuppAppMsg.plantAccess10,
//							name : 'text_SI',
//							id:'text_SI',
//							width:600,
//							labelWidth:300,
//							readOnly:true,
//							margin: '12 10 0 0',
//							allowBlank:false,
//							//allowBlank:role=='ANONYMOUS'?false:true
//						    },{
//								xtype: 'button',
//								width:100,
//								itemId : 'requestDOC_1',
//								id : 'requestDOC_1',
//								action : 'requestDOC_1',
//								text : SuppAppMsg.supplierLoad,
//							}
//						    ]	
				    
//				     },
				     { 
							xtype: 'container',
							layout: 'hbox',
							id:'documentContainerDoc2',
							colspan:3,
							width:800,
							margin:'10 0 10 150',
							//hidden:role=='ANONYMOUS'?false:true,
							defaults : {
								labelWidth : 150,
								xtype : 'textfield',
								margin: '12 0 0 0'
							},
					        items:[{
					        	xtype : 'textfield',
								fieldLabel : SuppAppMsg.plantAccess11+'a',
								name : 'text_PSUA',
								id:'text_PSUA',
								width:600,
								labelWidth:300,
								readOnly:true,
								margin: '12 10 0 0',
//								allowBlank:false,
								//allowBlank:role=='ANONYMOUS'?false:true
							    },{
									xtype: 'button',
									width:100,
									itemId : 'requestDOC_2',
									id : 'requestDOC_2',
									action : 'requestDOC_2',
									text : SuppAppMsg.supplierLoad,
								}]	
					    
					    },{
							xtype: 'container',
							layout: 'hbox',
							id:'documentContainerSUA',
							colspan:3,
							width:800,
							margin:'10 0 10 150',
							//hidden:role=='ANONYMOUS'?false:true,
							defaults : {
								labelWidth : 150,
								xtype : 'textfield',
								margin: '12 0 0 0'
							},
					        items:[{
					        	xtype : 'textfield',
								fieldLabel : 'SUA',
								name : 'text_SUA',
								id:'text_SUA',
								width:600,
								labelWidth:300,
								readOnly:true,
								margin: '12 10 0 0',
//								allowBlank:false,
								//allowBlank:role=='ANONYMOUS'?false:true
							    },{
									xtype: 'button',
									width:100,
									itemId : 'requestDOC_3',
									id : 'requestDOC_3',
									action : 'requestDOC_3',
									text : SuppAppMsg.supplierLoad,
								}]
					    },{
							xtype: 'container',
							layout: 'hbox',
							id:'documentContainerDoc4',
							colspan:3,
							width:800,
							margin:'10 0 10 150',
							//hidden:role=='ANONYMOUS'?false:true,
							defaults : {
								labelWidth : 150,
								xtype : 'textfield',
								margin: '12 0 0 0'
							},
					        items:[{
					        	xtype : 'textfield',
								fieldLabel : SuppAppMsg.plantAccess12,
								name : 'text_FPCOPAA',
								id:'text_FPCOPAA',
								width:600,
								labelWidth:300,
								readOnly:true,
								margin: '12 10 0 0',
//								allowBlank:false,
								//allowBlank:role=='ANONYMOUS'?false:true
							    },{
									xtype: 'button',
									width:100,
									itemId : 'requestDOC_4',
									id : 'requestDOC_4',
									action : 'requestDOC_4',
									text : SuppAppMsg.supplierLoad,
								}]	
					    },
					   ,{
				xtype: 'checkboxfield',
	            fieldLabel: SuppAppMsg.plantAccess18,
	            id: 'addHeavyequipment',
	            itemId: 'addHeavyequipment',
	            name:'addHeavyequipment',
	            width:650,
	            labelWidth:650,
	            margin:'5 10 0 150',
	            listeners: {
	                click: {
	                    element: 'el', //bind to the underlying el property on the panel
	                    fn: function(){
	                    	var radio = Ext.getCmp('addHeavyequipment').checked;
	                    	if(radio){
	                    		Ext.getCmp('text_SRC').allowBlank=false;
//	                    		Ext.getCmp('text_CERTIFICADO').allowBlank=false;
	                    		Ext.getCmp('text_RM').allowBlank=false;
	                    		Ext.getCmp('documentContainerDoc10').show();
//	                    		Ext.getCmp('documentContainerDoc11').show();
	                    		Ext.getCmp('documentContainerDoc12').show();
	                    	}
	                    	if(!radio){
	                    		Ext.getCmp('text_SRC').allowBlank=true;
//	                    		Ext.getCmp('text_CERTIFICADO').allowBlank=true;
	                    		Ext.getCmp('text_RM').allowBlank=true;
	                    		Ext.getCmp('documentContainerDoc10').hide();
//	                    		Ext.getCmp('documentContainerDoc11').hide();
	                    		Ext.getCmp('documentContainerDoc12').hide();
	                    	}
	                    }
	                },
	            }
			},{
				xtype: 'container', 
				layout: 'hbox',
				id:'documentContainerDoc10',
				colspan:3,
				width:800,
				margin:'10 0 10 150',
				hidden:true,
				//hidden:role=='ANONYMOUS'?false:true,
				defaults : {
					labelWidth : 150,
					xtype : 'textfield',
					margin: '12 0 0 0'
				},
		        items:[{
		        	xtype : 'textfield',
		        	fieldLabel : SuppAppMsg.plantAccess19,
		        	name : 'text_SRC',
					id:'text_SRC',
					width:600,
					labelWidth:300,
					readOnly:true,
					margin: '12 10 0 0',
					//allowBlank:false,
					//allowBlank:role=='ANONYMOUS'?false:true
				    },{
						xtype: 'button',
						width:100,
						itemId : 'requestDOC_10',
						id : 'requestDOC_10',
						action : 'requestDOC_10',
						text : SuppAppMsg.supplierLoad,
					}]	
		    
		    },
		    {
				xtype: 'container',
				layout: 'hbox',
				id:'documentContainerDoc12',
				colspan:3,
				hidden:true,
				width:800,
				margin:'10 0 10 150',
				//hidden:role=='ANONYMOUS'?false:true,
				defaults : {
					labelWidth : 150,
					xtype : 'textfield',
					margin: '12 0 0 0'
				},
		        items:[{
		        	xtype : 'textfield',
		        	fieldLabel : SuppAppMsg.plantAccess21,
		        	name : 'text_RM',
					id:'text_RM',
					width:600,
					labelWidth:300,
					readOnly:true,
					margin: '12 10 0 0',
					//allowBlank:false,
					//allowBlank:role=='ANONYMOUS'?false:true
				    },{
						xtype: 'button',
						width:100,
						itemId : 'requestDOC_12',
						id : 'requestDOC_12',
						action : 'requestDOC_12',
						text : SuppAppMsg.supplierLoad,
					}]	
		    
		    },{
				xtype: 'container',
				layout: 'hbox',
				colspan:3,
				width:800,
				margin:'20 10 10 150',
		        items:[ {
	                xtype: 'button',
	                width: 150,
	                icon: 'resources/images/doc.png',
	                text: SuppAppMsg.plantAccess88,
//	                action: 'uploadFileRequest',
	                id: 'uploadDatahow',
	                margin: '0 0 0 0', 
	                handler: function () {
	                    secondContainer.hide();
	                    firstContainer.show();
	                }
	            } ]	
		    
		    }
	            ]
	        });
		  
		  this.items.push(firstContainer, secondContainer);

			this.tbar = [
					{
						iconCls : 'icon-save',
						itemId : 'uploadPlantAccessRequestBtn',
						id : 'uploadPlantAccessRequestBtn',
						text : SuppAppMsg.plantAccess4,
						action : 'uploadPlantAccessRequestAct',
						cls: 'buttonStyle',
					},{
						xtype: 'button',
						width:200,
						icon:'resources/images/doc.png',
						//hidden : role == 'ROLE_SUPPLIER'?true:false,
						text : SuppAppMsg.plantAccess5,
						action : 'uploadFileRequest',
						id : 'uploadFileRequest',
						maring:'0 0 0 0',
						cls: 'buttonStyle',
					}
					];
			this.callParent(arguments);
	}

});