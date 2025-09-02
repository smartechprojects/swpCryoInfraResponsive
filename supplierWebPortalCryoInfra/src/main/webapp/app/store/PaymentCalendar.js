Ext.define('SupplierApp.store.PaymentCalendar', {
    extend: 'Ext.data.Store',
    model: 'SupplierApp.model.PaymentCalendar',
    alias:'store.paymentcalendar',
    autoLoad: true,
    pageSize: 12,
    proxy: {
        type: 'ajax',
        api: {
            read: 'paymentCalendar/view.action'
        },
        reader: {
            type: 'json',
            rootProperty: 'data',
            successProperty: 'success'
        }
    }
});