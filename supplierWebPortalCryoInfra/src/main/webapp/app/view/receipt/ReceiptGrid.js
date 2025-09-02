Ext.define('SupplierApp.view.receipt.ReceiptGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.receiptGrid',
    forceFit: true,
    loadMask: true,
	frame:false,
	border:false,
	selModel: {
        checkOnly: true,
        mode: 'SIMPLE',
        showHeaderCheckbox: false,
        injectCheckbox:'first',
        renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
	            metaData.tdCls = Ext.baseCSSPrefix + 'grid-cell-special';
	            var html = '';
	            if ((record.data.uuid != null && record.data.uuid != '') || role=='ROLE_PURCHASE_READ' || role=='ROLE_AUDIT_USR' || record.data.status == 'OC CANCELADA') {
	                html = '';
	            } else {
	            	html = '<div class="' + Ext.baseCSSPrefix + 'grid-row-checker"> </div>';
	            }
	            return html;
        }
    },
    selType: 'checkboxmodel',
	cls: 'extra-large-cell-grid', 
	store : {
		type:'receiptstore'
	},
	scroll : false,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
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
				
				debugger;
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
                width: 80
            },{
                text     : 'Sec',
                dataIndex: 'receiptLine',
                width: 60
            },
           {
            text     : SuppAppMsg.receiptTitle1,
            dataIndex: 'documentNumber',
            width: 90
        },{
            text     : SuppAppMsg.receiptTitle2,
            width: 90,
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
            width: 60
        },{
            text     : SuppAppMsg.purchaseTitle43,
            dataIndex: 'quantityReceived',
            renderer : Ext.util.Format.numberRenderer('0,000.00')
        },{
            text     : 'UOM',
            dataIndex: 'uom',
            width: 50
        },{
            text     : 'Remark',
            dataIndex: 'remark',
            width: 90
        },{
            text     : SuppAppMsg.receiptTitle8,
            dataIndex: 'receiptType',
            width: 40
        },{
            text     : SuppAppMsg.receiptTitle5,
            dataIndex: 'estPmtDate',
            width: 110,
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
            width: 150,
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
            width: 150,
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
            width: 80,
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
            width: 150,
            renderer: function(value, metaData, record, row, col, store, gridView){
            	debugger
            	var returnValue = '';
            	if(value != null && value != '' && value!= undefined){
					    		
					    		returnValue =   Ext.util.Format.date(new Date(value), 'd-m-Y H:i:s');
					    		
            	}
            	return returnValue;
            }
            
        },{
            text     : 'UUID',
            dataIndex: 'uuid',
            width: 290,
        }];
      
        this.callParent(arguments);
    }
});