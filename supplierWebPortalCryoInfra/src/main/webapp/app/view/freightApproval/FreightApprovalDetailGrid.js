function sortByKey(array, key) {
    return array.sort(function(a, b) {
        var x = a[key]; var y = b[key];
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    });
};

function renderTip(val, meta, rec, rowIndex, colIndex, store) {
	var comments = "";
	var firstLineNotes ="";
	if(rec.data.purchaseOrderNotes){
		sortByKey(rec.data.purchaseOrderNotes, "instruction");
		for(var i = 0; i < rec.data.purchaseOrderNotes.length; i++){
			if(i == 0){
				firstLineNotes = rec.data.purchaseOrderNotes[i].lineText + "...";
			}
			comments = comments + " " + rec.data.purchaseOrderNotes[i].lineText;
			comments = comments.replace(/"/g, '');
		}

	}
	
	if(comments != ""){
	    meta.tdAttr = 'data-qtip="' + comments + '"';
	    return firstLineNotes;
	}else{
		return "";
	}

};
Ext.QuickTips.init();

var store = Ext.create('Ext.data.Store', {
	model: 'SupplierApp.model.FreightApprovalDetail',
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
	},
	sortOnLoad: true,
    sorters: [{
        property: 'lineNumber',
        direction: 'ASC'
    }]
    
});


Ext.define('SupplierApp.view.freightApproval.FreightApprovalDetailGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.freightApprovalDetailGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',  
	store:store,
	forceFit: true,
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
        this.columns = [{
						    text     : SuppAppMsg.freightApprovalTitle1,
						    width: 100,
						    dataIndex: 'folio'
						},{
						    text     : SuppAppMsg.freightApprovalTitle2,
						    width: 100,
						    dataIndex: 'amount',
						    renderer : Ext.util.Format.numberRenderer('0,0.00')
						},{
						    text     : SuppAppMsg.freightApprovalTitle3,
						    width: 140,
						    dataIndex: 'serieBitacora'
						},{
						    text     : SuppAppMsg.freightApprovalTitle4,
						    width: 140,
						    dataIndex: 'numBitacora'
						},{
						    text     : SuppAppMsg.freightApprovalTitle5,
						    width: 350,
						    dataIndex: 'conceptName'//,
						},{
				        	xtype: 'actioncolumn', 
				            width: 90,
				            header: SuppAppMsg.freightApprovalTitle8,
				            align: 'center',
							name : 'viewPDFFDA',
							//hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
							hidden: false,
							itemId : 'viewPDFFDA',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/archivo-pdf.png',
			             	      text: SuppAppMsg.freightApprovalTitle8,
				                  handler: function(grid, rowIndex, colIndex) {
				                	 
				                	  var record = grid.store.getAt(rowIndex);
				                	  //var href = "documents/openDocument.action?id=" + 46;
										var href = "documents/openDocumentByUuid.action?orderNumber=0&type=pdf&uuidFactura=" + record.data.uuidFactura;
										var newWindow = window.open(href, '_blank');
										//var newWindow = window.open(newdata, "_blank");
										setTimeout(function(){ newWindow.document.title = 'Factura PDF'; }, 10);
				                  //this.fireEvent('buttonclick', grid, rowIndex, colIndex);
				                  }}]
				        },{
				        	xtype: 'actioncolumn', 
				            width: 90,
				            header: SuppAppMsg.freightApprovalTitle9,
				            align: 'center',
							name : 'viewXMLFDA',
							//hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
							hidden: false,
							itemId : 'viewXMLFDA',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/doc.png',
			             	      text: SuppAppMsg.freightApprovalTitle9,
				                  handler: function(grid, rowIndex, colIndex) {
				                 // this.fireEvent('buttonclick', grid, rowIndex, colIndex);
				                	  var record = grid.store.getAt(rowIndex);
				                	  //var href = "documents/openDocument.action?id=" + 46;
										var href = "documents/openDocumentByUuid.action?orderNumber=0&type=xml&uuidFactura=" + record.data.uuidFactura;
										var newWindow = window.open(href, '_blank');
										//var newWindow = window.open(newdata, "_blank");
										setTimeout(function(){ newWindow.document.title = 'Factura XML'; }, 10);
				                  }}]
						
						}];
        
        this.tbar = [ {
			iconCls : 'icon-save',
			itemId : 'updateOrder',
			id : 'updateOrderFDC',
			text : SuppAppMsg.purchaseTitle31,
			action : 'updateOrder',
			hidden:true
		}];
              
        this.callParent(arguments);
    }
	
});