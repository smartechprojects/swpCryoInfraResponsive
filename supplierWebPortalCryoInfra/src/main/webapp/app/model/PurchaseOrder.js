Ext.define('SupplierApp.model.PurchaseOrder', {
    extend: 'Ext.data.Model',
    fields: [
			    {name:  'id'},
				{name:  'addressNumber'},
				{name:  'businessUnit'},
				{name:  'companyKey'},
				{name:  'creditNoteUploadDate'},
				{name:  'creditNotUuid'},
				{name:  'currecyCode'},
				{name:  'dateRequested'},
				{name:  'deliveryInst1'},
				{name:  'deliveryInst2'},
				{name:  'description'},
				{name:  'documentCompany'},
				{name:  'invoiceUploadDate', type: 'date', dateFormat: 'c'},
				{name:  'invoiceUuid'},
				{name:  'notes'},
				{name:  'orderAmount'},
				{name:  'foreignAmount'},
				{name:  'originalOrderAmount'},
				{name:  'orderCompany'},
				{name:  'orderNumber'},
				{name:  'shortCompanyName'},
				{name:  'longCompanyName'},
				{name:  'orderStauts'},
				{name:  'orderSuffix'},
				{name:  'orderType'},
				{name:  'invoiceNumber'},
				{name:  'originalOrderNumber'},
				{name:  'originalOrderType'},
				{name:  'paymentUploadDate', type: 'date', dateFormat: 'c'},
				{name:  'paymentUuid'},
				{name:  'promiseDelivery', type: 'date', dateFormat: 'c'},
				{name:  'relatedStatus'},
				{name:  'remark'},
				{name:  'shipTo'},
				{name:  'email'},
				{name:  'paymentType'},
				{name:  'invoiceAmount'},
				{name:  'supplierEmail'},
				{name:  'rejectNotes'},
				{name:  'headerNotes'},
				{name:  'status'},
				{name:  'transferDate', type: 'date', dateFormat: 'c'},
				{name:  'estimatedPaymentDate', type: 'date', dateFormat: 'c'},
				{name:  'transferStatus'},
				{name:  'relievedAmount'},
				{name:  'sentToWns'},
				{model: 'SupplierApp.model.PurchaseOrderDetail', name: 'purchaseOrderDetail', mapping:'purchaseOrderDetail',type: 'auto'}

        ],
        proxy: {
            type: 'ajax',
            api: {
               // read: 'supplier/orders/view.action',
                create: 'supplier/orders/save.action',
                update: 'supplier/orders/update.action',
                destroy: 'supplier/orders/delete.action'
            },
            reader: {
                type: 'json',
                rootProperty: 'data',
                successProperty: 'success'
            },
            writer: {
            	type: 'json',
                writeAllFields: true,
                encode: false
            }
        }
});