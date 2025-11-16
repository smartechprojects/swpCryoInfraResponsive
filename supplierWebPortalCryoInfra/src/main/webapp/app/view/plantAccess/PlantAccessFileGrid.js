

Ext.QuickTips.init();

var storeFile = Ext.create('Ext.data.Store', {
	model: 'SupplierApp.model.PlantAccessFile',
	autoLoad: false,
	pageSize: 20,
	remoteSort: false,
	proxy: {
		type: 'memory',
		reader: {
			type: 'json',
			rootProperty: 'root'
		}, writer: {
			type: 'json',
			writeAllFields: true,
			encode: false
		}
	}
    
});


Ext.define('SupplierApp.view.plantAccess.PlantAccessFileGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.plantAccessFileGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',  
	store:storeFile,
	scroll : true,
	viewConfig: {
	    stripeRows: true,
	    style : { overflow: 'auto', overflowX: 'hidden' },
	    enableTextSelection: true,
	    markDirty: false,
	    listeners: {
	        refresh: function (view) {
	            var grid = view.up('grid');
	            if (!grid) return;

	            Ext.defer(function () {
	                // Autoajuste de columnas según contenido
	                Ext.each(grid.columns, function (col) {
	                    if (col.autoSize) col.autoSize();
	                    else if (col.autoSizeColumn) col.autoSizeColumn();

	                    // Ajuste adicional según header (por texto largo)
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

	                // Repartir espacio sobrante solo si sobra
	                Ext.defer(function () {
	                    var totalWidth = 0;
	                    var gridWidth = grid.getWidth();

	                    // Calcular ancho total de columnas visibles
	                    Ext.each(grid.columns, function (col) {
	                        if (!col.hidden) totalWidth += col.getWidth();
	                    });

	                    // Si sobra espacio, lo repartimos
	                    if (totalWidth < gridWidth) {
	                        var diff = gridWidth - totalWidth - 10; // margen visual
	                        var visibles = Ext.Array.filter(grid.columns, function (col) {
	                            return !col.hidden;
	                        });
	                        var extra = diff / visibles.length;

	                        Ext.each(visibles, function (col) {
	                            col.setWidth(col.getWidth() + extra);
	                        });

	                        grid.updateLayout();
	                    }
	                }, 100);	                                
	            }, 200);
	        }
	    }
	},
    initComponent: function() {
        this.columns = [{
						    text     :SuppAppMsg.taxvaultDocument,
						    //width: "30%",
						    flex:1,
						    dataIndex: 'originName'
						},{
						    text     : SuppAppMsg.fiscalTitle21,
						    //width: '40%',
						    flex:1,
						    dataIndex: 'documentType',
				            renderer: function(value, meta, record) {
				            	
				            	var status = {
				            			REQUEST_SI: 'REQUEST_SI',
				            			REQUEST_PSUA: 'REQUEST_PSUA',
				            			REQUEST_FPCOPAA: 'REQUEST_FPCOPAA',
				            			REQUEST_OCIMSS: 'REQUEST_OCIMSS',
				            			REQUEST_GC1: 'REQUEST_GC1',
				            			REQUEST_GCCOVID: 'REQUEST_GCCOVID',
				            			REQUEST_MCE: 'REQUEST_MCE',
				            			REQUEST_MPDF: 'REQUEST_MPDF',
				            			REQUEST_SRC: 'REQUEST_SRC',
				            			REQUEST_CERTIFICADO: 'REQUEST_CERTIFICADO',
				            			REQUEST_RM: 'REQUEST_RM',
				            			REQUEST_SUA: 'REQUEST_SUA',
				            	};
				            	    
				            	switch (record.data.documentType) {
				            	  case status.REQUEST_SI:
				            		return SuppAppMsg.plantAccess10.toUpperCase();
				            	    break;
				            	  case status.REQUEST_PSUA:
				            		return SuppAppMsg.plantAccess11.toUpperCase();
				              	    break;
				            	  case status.REQUEST_SUA:
				            		return 'SUA'.toUpperCase();
				              	    break;
				            	  case status.REQUEST_FPCOPAA:
				            		return SuppAppMsg.plantAccess12.toUpperCase();
				              	    break;
				            	  case status.REQUEST_OCIMSS:
				            		return SuppAppMsg.plantAccess13.toUpperCase();
				              	    break;
				            	  case status.REQUEST_GC1:
				            		return SuppAppMsg.plantAccess14.toUpperCase();
				              	    break;
				            	  case status.REQUEST_GCCOVID:
				            		return SuppAppMsg.plantAccess15.toUpperCase();
				              	    break;
				            	  case status.REQUEST_MCE:
				            		return SuppAppMsg.plantAccess16.toUpperCase();
				              	    break;
				            	  case status.REQUEST_MPDF:
				            		return SuppAppMsg.plantAccess17.toUpperCase();
				              	    break;
				            	  case status.REQUEST_SRC:
				            		return SuppAppMsg.plantAccess19.toUpperCase();
				                	break;
				            	  case status.REQUEST_CERTIFICADO:
				            		return SuppAppMsg.plantAccess20.toUpperCase();
				              	    break;
				            	  case status.REQUEST_RM:
				            		  return SuppAppMsg.plantAccess21.toUpperCase();
				              	    break;
				              	  default:
				              		break;
				            	}

				             }
						},{
				        	xtype: 'actioncolumn', 
				            //width: '10%',
				        	flex:1,
				            header: SuppAppMsg.plantAccess34,
				            align: 'center',
							name : 'openDocumentFile',
							//hidden: false,
							itemId : 'openDocumentFile',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	//icon:'resources/images/doc.png',
				            	  iconCls:'icon-document',
			             	      text: SuppAppMsg.freightApprovalTitle9,
				                  handler: function(grid, rowIndex, colIndex) {
				                	  var record = grid.store.getAt(rowIndex);
										var href = "plantAccess/openDocumentPlantAccess.action?id=" + record.data.id;
										window.open(href);
										//setTimeout(function(){ newWindow.document.title = record.data.originName; }, 10);
				                  }}]
						},{
				        	xtype: 'actioncolumn', 
				            //width: 90,
				        	flex:1,
				            header: 'Eliminar documento',
				            align: 'center',
							name : 'deleteFilePlantAccess',
							hidden:true,
//							hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
							itemId : 'deleteFilePlantAccess',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/close.png',
				              	 /* getClass: function(v, metadata, r, rowIndex, colIndex, store) {
				              		if(r.data.documentStatus != "COMPLETED" ){
				              			return "x-hidden-display";
				              		}
				              		   if(
	              	        		  !(role=='ROLE_BF_ADMIN')) {
	                      	        	  
	                      	              return "x-hidden-display";
	                      	          }
				              	      },
				              	      text: SuppAppMsg.approvalReject,*/
				                  handler: function(grid, rowIndex, colIndex) {
				                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
				                  }}]
				        }/*,{
				        	xtype: 'actioncolumn', 
				            width: '10%',
				            header: 'quitar',
				            align: 'center',
							name : 'fviewXMLFDA',
							hidden: false,
							itemId : 'fviewXMLFDA',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/doc.png',
			             	      text: SuppAppMsg.freightApprovalTitle9,
				                  handler: function(grid, rowIndex, colIndex) {
				                	  var record = grid.store.getAt(rowIndex);
										var href = "documents/openDocumentByUuid.action?orderNumber=0&type=xml&uuidFactura=" + record.data.uuidFactura;
										var newWindow = window.open(href, '_blank');
										setTimeout(function(){ newWindow.document.title = 'Factura XML'; }, 10);
				                  }}]
						
						}*/];
        
        this.tbar = [Ext.create(
				'Ext.form.Panel',
				{
					layout: {
	                type: 'hbox'
	            },
	            items : [{
	            	xtype: 'button',
					width:200,
					icon:'resources/images/doc.png',
					//hidden : role == 'ROLE_SUPPLIER'?true:false,
					text : 'Cargar Archivo',
					hidden:true,
					action : 'loadFile',
					id : 'loadFile',
					maring:'0 0 0 0'
	            },/*{
			xtype : 'combo',
			fieldLabel : 'Tipo Documento',
			id : 'addRequestDocumentType',
			itemId : 'addRequestDocumentType',
			name : 'addRequestDocumentType',
			store : getUDCStore('FILEACCESPLANT', ('REQUEST'+(Ext.getCmp('addworkatheights').getValue()?",WORKATHEIGHTS":"")
					+(Ext.getCmp('addHeavyequipment').getValue()?",HEAVYEQUIPMENT":"")
					+(Ext.getCmp('addconfinedspaces').getValue()?",CONFINEDSPACES":"")
					+(Ext.getCmp('addcontelectricworks').getValue()?",CONTELECTRICWORKS":"")
					+(Ext.getCmp('addworkhots').getValue()?",WORKHOTS":"")
					+(Ext.getCmp('addchemicalsubstances').getValue()?",CHEMICALSUBSTANCES":"")), '', 'CONTRATIST'),
			valueField : 'strValue2',
			displayField: 'strValue1',
			emptyText : 'Seleccione',
			typeAhead: true,
            minChars: 1,
            forceSelection: true,
            triggerAction: 'all',
            labelWidth:100,
            width : 400,
			allowBlank : true,
			margin:'10 0 10 10'
		},{
			xtype : 'filefield',
			name : 'file',
			fieldLabel : 'Archivo:',
			labelWidth : 70,
			msgTarget : 'side',
			allowBlank : false,
			width:300,
			buttonText : 'Archivo',
			margin:'10 0 10 10'
	//		listeners: {
	//            'change': function(f, value){
	//                  alert(f.size); // not filesize
	//            }}
		}
	//	,
	//		{
	//		 xtype: 'button',
	//			itemId : 'faddPlantAccessRequestEmployBtn',
	//			id : 'faddPlantAccessRequestEmployBtn',
	//			text : 'Agregar',
	//			action : 'addFilePlantAccessRequestBtnAct',
	//			labelWidth : 70,
	//			margin:'10 0 10 10'
	//		}
		*/],
			/*buttons : [ {
				text : SuppAppMsg.supplierLoad,
				margin:'10 0 0 0',
				handler : function() {
					var form = this.up('form').getForm();
					if (form.isValid()) {
						form.submit({
									url : 'plantAccess/uploadFileRequest.action',
									waitMsg : SuppAppMsg.supplierLoadFile,
									success : function(fp, o) {
										var res = Ext.decode(o.response.responseText);
		//								
										storeFile.add(res.data);
									},       // If you don't pass success:true, it will always go here
							        failure: function(fp, o) {
							        	var res = Ext.decode(o.response.responseText);
							        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
							        }
								});
					}
				 }
			}]*/
	            })
	      ];
              
        this.callParent(arguments);
    }
	
});