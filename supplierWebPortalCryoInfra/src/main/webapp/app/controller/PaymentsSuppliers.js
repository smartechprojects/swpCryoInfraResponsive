Ext.define('SupplierApp.controller.PaymentsSuppliers', {
    extend: 'Ext.app.Controller',
    stores: ['PaymentsSuppliers'],
    models: ['PaymentsSuppliers'],
    views: ['paymentsSuppliers.PaymentsSuppliersPanel','paymentsSuppliers.PaymentsSuppliersGrid'],
    refs: [
	    {
	        ref: 'paymentsSuppliersGrid',
	        selector: 'paymentsSuppliersGrid'
	    }],
 
    init: function() {
        this.control({
	            'paymentsSuppliersGrid': {
	            	itemdblclick: this.gridSelectionChange
	            },
				'paymentsSuppliersGrid button[action=paymentsSupplierSearch]' : {
					click : this.paymentsSupplierSearch
				}
        });
    },
    
    gridSelectionChange: function(model, record) {
        if (record) {
            var me = this;
            debugger
            var idPayment = record.data.idPayment;

            // Perform an AJAX call to fetch invoice data
            Ext.Ajax.request({
                url: 'receipt/getPaymentSupplierDetailConsulta.action', // Endpoint URL
                method: 'GET',
                params: {
                    idPayment: idPayment
                },
                success: function(response) {
                    var responseData = Ext.decode(response.responseText);

                    var invoiceStore = Ext.create('Ext.data.Store', {
                        fields: ['id', 'idPayment', 'typDocPas', 'docPasiveNumber', 'invoiceNumber', 'mountInvoice'],
                        data: responseData.data
                    });
                    
                    me.winDetail = Ext.create('Ext.window.Window', {
                        layout: 'fit',
                        title:   SuppAppMsg.paymentsSuppliersDetailoPay,
                        width: 600,
                        height: 400,
                        modal: true,
                        items: [
                            {
                                xtype: 'grid',
                                title: SuppAppMsg.paymentsSuppliersInvoices,
                                id: 'invoiceGrid',
                                store: invoiceStore,
                                columns: [
                                    {
                                        text: 'ID',
                                        dataIndex: 'id',
                                        width: 50,
                                        hidden:true
                                        
                                    },
                                    {
                                        text: 'ID de Pago',
                                        dataIndex: 'idPayment',
                                        width: 80,
                                        hidden:true
                                    },
                                    {
                                        text:SuppAppMsg.paymentsSuppliersTypePasive,
                                        dataIndex: 'typDocPas',
                                        width: 120
                                    },
                                    {
                                        text: SuppAppMsg.paymentsSuppliersNumDocPasive,
                                        dataIndex: 'docPasiveNumber',
                                        width: 100
                                    },
                                    {
                                        text: SuppAppMsg.paymentsSuppliersInvoiceNumber,
                                        dataIndex: 'invoiceNumber',
                                        flex: 1
                                    },
                                    {
                                        text: SuppAppMsg.paymentsSuppliersAmountInvoice,
                                        dataIndex: 'mountInvoice',
                                        width: 120,
                                        align: 'right', // Align to the right
                                        renderer: function(value) {
                                            return Ext.util.Format.currency(value, '$', 2); // Format as currency
                                        }
                                    }
                                ]
                            }
                        ]
                    });

                    me.winDetail.show();
                },
                failure: function(response) {
                    Ext.Msg.alert('Error', 'Failed to fetch invoice data.');
                }
            });
        }
    }


,
    paymentsSupplierSearch: function(button) {
    	debugger
    	var grid = this.getPaymentsSuppliersGrid();
    	var store = grid.getStore();
    	
    	var paymentsSuppliersAddressNumberGrid = Ext.getCmp('paymentsSuppliersAddressNumberGrid').getValue();
    	var paymentsSupplierstipoDocGrid = Ext.getCmp('paymentsSupplierstipoDocGrid').getValue();
    	var paymentsSuppliersCurrencyCodeGrid = Ext.getCmp('paymentsSuppliersCurrencyCodeGrid').getValue();
    	
    	
    	var paymentsSupplierspoFromDate = Ext.getCmp('paymentsSupplierspoFromDate').getValue()!=undefined&&Ext.getCmp('paymentsSupplierspoFromDate').getValue()!=""?Ext.Date.format(Ext.getCmp('paymentsSupplierspoFromDate').getValue(), 'Y-m-d'):"";
    	var paymentsSupplierspoToDate = Ext.getCmp('paymentsSupplierspoToDate').getValue()!=undefined&&Ext.getCmp('paymentsSupplierspoToDate').getValue()!=""? Ext.Date.format(Ext.getCmp('paymentsSupplierspoToDate').getValue(), 'Y-m-d'):"";
    	
    	
    	var paymentsSuppliersStatusPayGrid = Ext.getCmp('paymentsSuppliersStatusPayGrid').getValue();
    	store.removeAll();    	
    	store.proxy.extraParams = {
    			addressNumber : paymentsSuppliersAddressNumberGrid?paymentsSuppliersAddressNumberGrid:"",
    			tipoDoc : paymentsSupplierstipoDocGrid?paymentsSupplierstipoDocGrid:"",
    			currencyCode : paymentsSuppliersCurrencyCodeGrid?paymentsSuppliersCurrencyCodeGrid:"",
    			pfromDate : paymentsSupplierspoFromDate?paymentsSupplierspoFromDate:"",
    			ptoDate : paymentsSupplierspoToDate?paymentsSupplierspoToDate:""	,
    			estatus : paymentsSuppliersStatusPayGrid?paymentsSuppliersStatusPayGrid:""	
    	}
    	store.loadPage(1);
    	grid.getView().refresh()
    }
    
});


