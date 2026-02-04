Ext.define('SupplierApp.view.receipt.ReceiptGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.receiptGrid',
    //forceFit: true,
    loadMask: true,
	frame:false,
	border:false,
	//scrollable: true,
	selModel: {
        type: 'checkboxmodel',
        checkOnly: true,
        mode: 'SIMPLE',
        showHeaderCheckbox: false,
        injectCheckbox: 0,
        listeners: {
            beforeselect: function (selModel, record) {
                if (record.get('uuid')) {
                    return false;
                }
            }
        }
    },
	store : {
		type:'receipt'
	},
	scroll :  true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' },
		enableTextSelection: true,
		stripeRows: true,
	    markDirty: false,
	    listeners: {
	        refresh: function (view) {
	            var grid = view.up('grid');
	            if (!grid) return;
	         // Usar la función centralizada
	            GridUtils.adjustGridLayout(grid, true);
	            
	            setTimeout(function() {
	                var records = view.getStore().getRange();
	                for (var i = 0; i < records.length; i++) {
	                    var record = records[i];
	                    var row = view.getNode(i);
	                    
	                    if (row) {
	                        var shouldHide = ((record.data.uuid != null && record.data.uuid != '') || 
	                                         window.role == 'ROLE_PURCHASE_READ' || 
	                                         window.role == 'ROLE_AUDIT_USR' || 
	                                         record.data.status == 'OC CANCELADA');
	                        
	                        // Buscar el elemento del checkbox dentro de la primera celda
	                        var firstCell = row.querySelector('.x-grid-cell-first');
	                        if (firstCell) {
	                            var checkboxElement = firstCell.querySelector('.x-grid-checkcolumn');
	                            
	                            if (checkboxElement) {
	                                if (shouldHide) {
	                                    // Ocultar solo el checkbox, no toda la celda
	                                    checkboxElement.style.cssText = 'display: none !important; visibility: hidden !important;';
	                                    // Mantener la celda visible pero sin contenido de checkbox
	                                    firstCell.style.cssText = 'display: table-cell !important; visibility: visible !important; width: 24px !important; min-width: 24px !important;';
	                                } else {
	                                    // Mostrar el checkbox
	                                    checkboxElement.style.cssText = 'display: inline !important; visibility: visible !important;';
	                                    firstCell.style.cssText = 'display: table-cell !important; visibility: visible !important; width: 24px !important; min-width: 24px !important;';
	                                }
	                            }
	                        }
	                    }
	                }
	            }, 10);
	            
	        },
	        resize: function(view) {
	            var grid = view.up('grid');
	            if (!grid) return;
	            // Usar la función centralizada
	            GridUtils.adjustGridLayout(grid, false);
	        }
	    }

	},	
    initComponent: function() {
    	 var catalogData; // Variable para almacenar los datos del catálogo

        // Realiza una única llamada AJAX para cargar los datos del catálogo de forma síncrona
        Ext.Ajax.request({
            url: 'admin/udc/searchSystemAndKey.action',
            method: 'GET',
            async: false,
            params: {
                query: '',
                udcSystem: 'PMTTRM',
                udcKey: '', // Si es necesario, proporciona un valor predeterminado aquí
                systemRef: '',
                keyRef: '',
                page: 0,
                start: 0,
                limit: 0
            },
            success: function (response) {
                catalogData = Ext.decode(response.responseText).data;
				var catalogObject = {};

				// Recorre el arreglo catalogData y construye el nuevo objeto JSON
				for (var i = 0; i < catalogData.length; i++) {
					var entry = catalogData[i];
					catalogObject[entry.udcKey] = entry;
				}
				catalogData=catalogObject;
				
			},
            failure: function () {
                // Manejo de errores si es necesario
            }
        });
 
        this.columns = [
        	{
                hidden:true,
                dataIndex: 'id'
            },{
                text     : 'Sts',
                dataIndex: 'paymentStatus',
                //width: 80
                flex :1 
            },{
                text     : 'Sec',
                dataIndex: 'receiptLine',
                //width: 60
                flex :1 
            },
           {
            text     : SuppAppMsg.receiptTitle1,
            dataIndex: 'documentNumber',
            //width: 90
            flex :1 
        },{
            text     : SuppAppMsg.receiptTitle2,
            //width: 90,
            flex :1,
            dataIndex: 'receiptDate',
            renderer : Ext.util.Format.dateRenderer("d-m-Y")
        },{
            text     : SuppAppMsg.receiptTitle3,
            dataIndex: 'amountReceived',
            renderer : Ext.util.Format.numberRenderer('0,000.00')
        },{
            text     : SuppAppMsg.receiptTitle4,
            dataIndex: 'foreignAmountReceived',
            renderer : Ext.util.Format.numberRenderer('0,000.00')
        },{
            text     : SuppAppMsg.purchaseOrderCurrency,
            dataIndex: 'currencyCode',
            //width: 60
            flex :1 
        },{
            text     : SuppAppMsg.purchaseTitle43,
            dataIndex: 'quantityReceived',
            renderer : Ext.util.Format.numberRenderer('0,000.00')
        },{
            text     : 'UOM',
            dataIndex: 'uom',
            //width: 50
            flex :1 
        },{
            text     : 'Remark',
            dataIndex: 'remark',
            //width: 90
            flex :1 
        },{
            text     : SuppAppMsg.receiptTitle8,
            dataIndex: 'receiptType',
           // width: 40
            flex :1 
        },{
            text     : SuppAppMsg.receiptTitle5,
            dataIndex: 'estPmtDate',
            //width: 110,
            flex :1,
			renderer: function(value, metaData, record, row, col, store, gridView){
				if(value) {
					return Ext.util.Format.date(new Date(value), 'd-m-Y');
				} else {
					return null;
				}
			}
        },{
            text     : SuppAppMsg.receiptTitle10,
            dataIndex: 'uploadDate',
            //width: 150,
            flex :1 ,
            renderer: function(value, metaData, record, row, col, store, gridView){
				if(value) {
					return Ext.util.Format.date(new Date(value), 'd-m-Y H:i:s');
				} else {
					return null;
				}
            }
        },{
            text     : SuppAppMsg.receiptTitle11,
            dataIndex: 'paymentTerms',
           // width: 150,
            flex :1 ,
            renderer: function(value, metaData, record, row, col, store, gridView){
            	value = value.trim();
            	var returnValue = value;
            
            	if(value == null || value == '' || value== undefined){
            		value = 'N30'
            	}
            		
            		returnValue=catalogData[value]==undefined||catalogData[value]==null?value:catalogData[value].strValue1;
            		
            		
//            		Ext.Ajax.request({
//					    url: 'admin/udc/searchSystemAndKey.action',
//					    method: 'GET',
//					    async: false,
//					    params: {
//					    	query : '',
//							udcSystem : 'PMTTRM',
//							udcKey : value,
//							systemRef : '',
//							keyRef : '',
//							page : 0,
//							start :0,
//							limit :0
//				        },
//
//				        success: function(fp, o) {
//					    	
//					    	var res = Ext.decode(fp.responseText);
//					    	if(res.data.length > 0){
//
//					    		returnValue =  res.data[0].strValue1;
//					    		
//					    	}else{
//					    		returnValue =  value;
//					    	}
//					    },
//					    failure: function() {
//					    	returnValue = value;
//					    }
//					}); 

                return returnValue;
            	
            	
            }
            
        },{
            text     : SuppAppMsg.receiptTitle9,
            dataIndex: 'paymentDate',
            // width: 80,
            flex :1 ,
			renderer: function(value, metaData, record, row, col, store, gridView){
				if(value) {
					return Ext.util.Format.date(new Date(value), 'd-m-Y');
				} else {
					return null;
				}
			}
        },{
            text     : SuppAppMsg.receiptTitle12,
            dataIndex: 'uploadComplDate',
            //width: 150,
            flex :1 ,
            renderer: function(value, metaData, record, row, col, store, gridView){
            	var returnValue = '';
            	if(value != null && value != '' && value!= undefined){
					    		
					    		returnValue =   Ext.util.Format.date(new Date(value), 'd-m-Y H:i:s');
					    		
            	}
            	return returnValue;
            }
            
        },{
            text     : 'UUID',
            dataIndex: 'uuid',
           // width: 290,
            flex :2
        }];
      
        this.callParent(arguments);
    }
});