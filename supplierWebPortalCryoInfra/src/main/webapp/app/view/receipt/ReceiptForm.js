Ext.define('SupplierApp.view.receipt.ReceiptForm' ,{
	extend: 'Ext.form.Panel',
	alias : 'widget.receiptForm',
	border:true,
	scrollable : true,
	  initComponent: function() {		  
			this.items= [{
				xtype: 'container',
				layout: 'hbox',
				//margin: '15 15 0 5',
				width: '100%',
        		//style:'border-bottom: 1px dotted #fff;padding-bottom:10px',
				defaults: { 
					//labelWidth : 100,
					align: 'stretch',
					flex : 1,
					//fieldStyle: 'padding-bottom:5px;font-size:18px;vertical-align:top;border:none;background:transparent;color:black;font-weight:bold',
					xtype:'textfield',
					readOnly:true,
					//width:220
					margin: '0 10 0 0'
				},
				items       :[{
					xtype: 'hidden',
					name: 'id'

				},{
     				xtype : 'hidden',
     				name : 'createdBy',
     				id:'createdBy'
     			},{
     				xtype : 'hidden',
     				name : 'creationDate',
     				id:'creationDate',
     				format : 'd-M-Y',
     			},{
					name: 'orderCompany',
					hidden:true
				},{
					fieldLabel: SuppAppMsg.purchaseOrderNumber,
					name: 'orderNumber'
				},{
					fieldLabel: SuppAppMsg.purchaseTitle5,
					//labelWidth : 90,
					name: 'orderType',
					//width:170
					flex :1
				},{
					fieldLabel: SuppAppMsg.suppliersNumber,
					//labelWidth : 130,
					name: 'addressNumber',
					//width:280
					flex :1.2
				},{
					fieldLabel: SuppAppMsg.purchaseOrderÌmporteTotal,
					name: 'orderAmount',
					xtype: 'numericfield',
					currencySymbol: '$ ',
                    useThousandSeparator: true,
                    decimalSeparator: '.',
                    thousandSeparator: ',',
                    alwaysDisplayDecimals: true,
                    //width:320
                    flex :1.1
				},{
					name: 'supplierCountry',
					hidden:true
				},{
     				xtype : 'hidden',
     				name : 'optionType',
     				id:'optionType',
				}]
			}];
			
			this.tbar=[      
				 {
			    	  iconCls: 'icon-save',
			    	  id: 'uploadReceiptInvoice',
			    	  itemId: 'uploadReceiptInvoice',
			    	  text: SuppAppMsg.purchaseTitle36,
			    	  action: 'uploadReceiptInvoice',
			    	  hidden:true,
			    	  cls: 'buttonStyle'
			      },{
			    	  iconCls: 'icon-save',
			    	  id: 'showOutSourcingWindow',
			    	  itemId: 'showOutSourcingWindow',
			    	  text: SuppAppMsg.outsourcingButton,
			    	  action: 'showOutSourcingWindow',
			    	  hidden:true,
			    	  cls: 'buttonStyle'
			      },{
			    	  iconCls: 'icon-save',
			    	  id: 'uploadReceiptCreditNote',
			    	  itemId: 'uploadReceiptCreditNote',
			    	  text: SuppAppMsg.purchaseTitle57,
			    	  action: 'uploadReceiptCreditNote',
			    	  hidden:true,
			    	  cls: 'buttonStyle'
			      },{
			    	  iconCls: 'icon-save',
			    	  id: 'uploadReceiptInvoiceZip',
			    	  itemId: 'uploadReceiptInvoiceZip',
			    	  text: SuppAppMsg.supplierLoad + ' Zip',
			    	  action: 'uploadReceiptInvoiceZip',
			    	  hidden: true,
			    	  cls: 'buttonStyle'
			      }];
		  this.callParent(arguments);	    
	  }

});