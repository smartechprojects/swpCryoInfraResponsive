Ext.define('SupplierApp.view.paymentCalendar.PaymentCalendarGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.paymentCalendarGrid',
    loadMask: true,
	frame:false,
	border:false,
	store : {
		type:'paymentcalendar'
	},
	//forceFit: true,
	dockedItems: [
    	getPagingContent()
    ],
	cls: 'extra-large-cell-grid', 
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {

        this.columns = [
           {
            text     : 'id',
            dataIndex: 'id',
            hidden:true
        },{
            text     : SuppAppMsg.paymentTitle1,
            //width: 120,
            flex:1,
            dataIndex: 'company'
        },{
            text     : SuppAppMsg.paymentTitle2,
            //width: 220,
            flex:1,
            dataIndex: 'paymentDate',
            renderer : Ext.util.Format.dateRenderer("d-m-Y")
        }];
      
        this.callParent(arguments);
    }
});