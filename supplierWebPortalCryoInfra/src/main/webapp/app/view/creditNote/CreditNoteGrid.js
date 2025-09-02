Ext.define('SupplierApp.view.creditNote.CreditNoteGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.creditNoteGrid',
    loadMask: true,
    forceFit: true,
	frame:false,
	border:false,
	selModel: {
        checkOnly: true,
        mode: 'SIMPLE',
        showHeaderCheckbox: false,
        injectCheckbox:'first',
        renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
			debugger
	            metaData.tdCls = Ext.baseCSSPrefix + 'grid-cell-special';
	            var html = '';
	            if ((record.data.uuid != null&&record.data.uuid != '')|| record.data.status == 'OC FACTURADA' || role=='ROLE_PURCHASE_READ' || role=='ROLE_AUDIT_USR' || record.data.status == 'OC CANCELADA') {
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
		type:'receipt'
	},
    scroll: 'horizontal',
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
 
        this.columns = [{
        					text: SuppAppMsg.fiscalTitle22,
						    width: 100,
						    dataIndex: 'status',
						    hidden:false
						},{
						    text     : SuppAppMsg.purchaseTitle26,
						    width: 50,
						    dataIndex: 'lineNumber'
						},{
						    text     : 'Código',
						    width: 70,
						    dataIndex: 'itemNumber',
						    hidden:true
						},{
						    text     : SuppAppMsg.paymentTitle1,
						    width: 70,
						    dataIndex: 'orderCompany'
						},{
						    text     : SuppAppMsg.purchaseTitle27,
						    width: 140,
						    dataIndex: 'glOffSet'
						},{
						    text     : 'Cantidad',
						    width: 70,
						    dataIndex: 'quantity',
						    renderer : Ext.util.Format.numberRenderer('0,0.00'),
						    hidden:true
						},{
						    text     : 'UOM',
						    width: 50,
						    dataIndex: 'uom',
						    hidden:true
						},{
						    text     : SuppAppMsg.purchaseTitle29,
						    width: 200,
						    dataIndex: 'itemDescription'
						},{
						    text     : 'Código SAT',
						    width: 80,
						    dataIndex: 'codigoSat',
						    hidden:true
						},{
						    text     : 'Precio unitario',
						    width: 100,
						    dataIndex: 'unitCost',
						    renderer : Ext.util.Format.numberRenderer('0,0.00'),
						    hidden:true
						},{
						    text     : SuppAppMsg.purchaseOrderÌmporteTotal,
						    width: 80,
						    dataIndex: 'extendedPrice',
						    renderer : Ext.util.Format.numberRenderer('0,0.00'),
						    hidden:true
						},{
							text     : 'Recibido',
						    width: 90,
						    dataIndex: 'received',
						    renderer : Ext.util.Format.numberRenderer('0,0.00'),
						    hidden:true
						},{
							text     : 'Rechazado',
						    width: 90,
						    dataIndex: 'rejected',
						    renderer : Ext.util.Format.numberRenderer('0,0.00'),
						    hidden:true
						},{
						    text     : 'Por recibir',
						    width: 100,
						    dataIndex: 'toReceive',
						    renderer : Ext.util.Format.numberRenderer('0,0.00'),
						    //hidden:role=='ROLE_SUPPLIER' || role== 'ROLE_SUPPLIER_OPEN'?true:false,
						    hidden:true
						},{
						    text     : 'Por rechazar',
						    width: 100,
						    dataIndex: 'toReject',
						    renderer : Ext.util.Format.numberRenderer('0,0.00'),
						    //hidden:role=='ROLE_SUPPLIER' || role== 'ROLE_SUPPLIER_OPEN'?true:false,
							hidden:true
						},{
						    text     : 'Pendiente',
						    width: 80,
						    hidden:false,
						    dataIndex: 'pending',
						    renderer : Ext.util.Format.numberRenderer('0,0.00'),
						    hidden:true
						},{
						    text     : SuppAppMsg.receiptTitle3,
						    width: 100,
						    dataIndex: 'amount',
						    align: 'right',
						    renderer : Ext.util.Format.numberRenderer('0,0.00')
						},{
						    text     : SuppAppMsg.receiptTitle4,
						    width: 100,
						    dataIndex: 'foreignAmount',
						    align: 'right',
						    renderer : Ext.util.Format.numberRenderer('0,0.00')
						}
						,{
						    text     : 'Importe pendiente',
						    width: 100,
						    dataIndex: 'openAmount',
						    renderer : Ext.util.Format.numberRenderer('0,0.00'),
						    hidden:true
						},{
						    width: 160,
						    text: 'Motivo de rechazo',
						    dataIndex: 'reason',
						    hidden:true
						},{
						    text     : 'uuid',
						    width: 180,
						    dataIndex: 'uuid',
						    hidden:false
						},{
						    text     : 'moneda',
						    width: 100,
						    dataIndex: 'currency',
						    hidden:false
						},{
						    text     : 'Fecha pago',
						    width: 100,
						    dataIndex: 'estimatedPaymentDate',
						    renderer : Ext.util.Format.dateRenderer("d-m-Y")
						},{
						    text     : SuppAppMsg.purchaseTitle30,
						    width: 550,
						    dataIndex: 'purchaseOrderNotes',
						    renderer: renderTip
						},{
						    dataIndex: 'taxCode',
						    hidden:true
						},{
						    dataIndex: 'taxable',
						    hidden:true
						}];
      
        this.callParent(arguments);
    }
});