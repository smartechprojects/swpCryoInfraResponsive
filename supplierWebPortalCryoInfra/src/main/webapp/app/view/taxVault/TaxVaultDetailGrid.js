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
	model: 'SupplierApp.model.TaxVaultDetail',
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
	}
    
});






Ext.define('SupplierApp.view.taxVault.TaxVaultDetailGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.taxVaultDetailGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',  
	store:store,
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
        this.columns = [{
						    text     :"Nombre del trabajador",
						    width: "30%",
						    dataIndex: 'employeeName'
						},{
						    text     : 'No. de afiliacón al IMSS',
						    width: '20%',
						    dataIndex: 'membershipIMSS'
						},{
						    text     : 'Fecha de Induccion/Folio de Credencial',
						    width: '25%',
						    dataIndex: 'datefolioIDcard'
						},{
				        	xtype: 'actioncolumn', 
				            width: '15%',
				            header: 'Cargar Documentos',
				            align: 'center',
							name : 'viewXMLFDA',
							//hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
							hidden: false,
							itemId : 'upfileWork',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/doc.png',
			             	      text: SuppAppMsg.freightApprovalTitle9,
				                  handler: function(grid, rowIndex, colIndex) {
				                	  this.fireEvent('buttonclick', grid, rowIndex, colIndex)
											var win = new Ext.Window(
													{
														title : 'Cargar Archivos de empleado ',
														layout : 'fit',
														autoScroll : true,
														width : 850,
														height : 440,
														modal : true,
														closeAction : 'destroy',
														resizable : false,
														minimizable : false,
														maximizable : false,
														plain : true,
														bodyStyle : "padding:10 10 0 10px;background:#fff;",
														items : [ {
										        			xtype : 'taxVaultWorkerFileGrid',
										        			border : true,
										        			height : 415
										        		}  ]
													});

											win.show();
										
				                	  
				                	  
				                	  
				                	  /*
				                	  
				                 // this.fireEvent('buttonclick', grid, rowIndex, colIndex);
				                	  var record = grid.store.getAt(rowIndex);
				                	  //var href = "documents/openDocument.action?id=" + 46;
										var href = "documents/openDocumentByUuid.action?orderNumber=0&type=xml&uuidFactura=" + record.data.uuidFactura;
										var newWindow = window.open(href, '_blank');
										//var newWindow = window.open(newdata, "_blank");
										setTimeout(function(){ newWindow.document.title = 'Factura XML'; }, 10);*/
				                  }}]
						
						},{
				        	xtype: 'actioncolumn', 
				            width: '10%',
				            header: 'Quitar',
				            align: 'center',
							name : 'quitEmploye',
							//hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
							hidden: false,
							itemId : 'quitEmploye',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/delete.png',
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
        
        this.tbar = [{
			xtype: 'textfield',
            fieldLabel: 'Nombre',
            id: 'addemployeename',
            itemId: 'addemployeename',
            name:'addemployeename',
            //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
            //fieldStyle: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?'border:none;background-color: #ddd; background-image: none;':'',
            //readOnly: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?true:false,
            width:200,
            labelWidth:60,
            margin:'5 10 0 10'
		},{
			xtype: 'textfield',
            fieldLabel: 'No. afiliacón al IMSS',
            id: 'addmembershipIMSS',
            itemId: 'addmembershipIMSS',
            name:'addmembershipIMSS',
            width:200,
            labelWidth:60,
            margin:'5 10 0 10'
		},
		{
			xtype: 'textfield',
            fieldLabel: 'Fecha de Induccion/Folio de Credencial',
            id: 'addDatefolioIDcard',
            itemId: 'addDatefolioIDcard',
            name:'addDatefolioIDcard',
            width:200,
            labelWidth:70,
            margin:'5 10 0 10'
		},{
			iconCls : 'icon-add',
			itemId : 'addTaxVaultRequestEmployBtn',
			id : 'addTaxVaultRequestEmployBtn',
			text : 'Agregar',
			action : 'addTaxVaultRequestEmployBtnAct',
		}];
              
        this.callParent(arguments);
    }
	
});