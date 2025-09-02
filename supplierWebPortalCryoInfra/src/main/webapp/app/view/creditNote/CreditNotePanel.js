Ext.define('SupplierApp.view.creditNote.CreditNotePanel' ,{
    extend: 'Ext.Panel',
    alias : 'widget.creditNotePanel',
    border:false,
    frame:false,
	initComponent: function () {
		debugger
        Ext.apply(this, {
        	layout: {
                type: 'vbox',
                align: 'stretch'
            },   
            items: [{
            	xtype: 'creditNoteForm',
            	height:80

            },{
           	 xtype: 'creditNoteGrid',
           	 height:380
            }]
        });
        this.callParent(arguments);
    }
});