

Ext.QuickTips.init();

var storeFile = Ext.create('Ext.data.Store', {
	model: 'SupplierApp.model.TaxVaultFile',
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


Ext.define('SupplierApp.view.taxVault.TaxVaultFileGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.taxVaultFileGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',  
	store:storeFile,
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
        this.columns = [{
						    text     :"Nombre del Documento",
						    width: "30%",
						    dataIndex: 'originName'
						},{
						    text     : 'Tipo de Documento',
						    width: '20%',
						    dataIndex: 'documentType'
						},{
				        	xtype: 'actioncolumn', 
				            width: '10%',
				            header: 'quitar',
				            align: 'center',
							name : 'fviewXMLFDA',
							hidden: false,
							itemId : 'fviewXMLFDA',
				            style: 'text-align:center;',
				            items: [
				            	{
				            	icon:'resources/images/doc.png',
			             	      text: SuppAppMsg.freightApprovalTitle9,
				                  handler: function(grid, rowIndex, colIndex) {
				                	  var record = grid.store.getAt(rowIndex);
										var href = "documents/openDocumentByUuid.action?orderNumber=0&type=xml&uuidFactura=" + record.data.uuidFactura;
										var newWindow = window.open(href, '_blank');
										setTimeout(function(){ newWindow.document.title = 'Factura XML'; }, 10);
				                  }}]
						
						}];
        
        this.tbar = [Ext.create(
				'Ext.form.Panel',
				{
					layout: {
	                type: 'hbox'
	            },
	            items : [{
			xtype : 'combo',
			fieldLabel : 'Tipo Documento',
			id : 'addRequestDocumentType',
			itemId : 'addRequestDocumentType',
			name : 'addRequestDocumentType',
			store : getUDCStore('FILEACCESPLANT', ('REQUEST'+(Ext.getCmp('addworkatheights').getValue()?",WORKATHEIGHTS":"")
					+(Ext.getCmp('addHeavyequipment').getValue()?",HEAVYEQUIPMENT":"")
					+(Ext.getCmp('addconfinedspaces').getValue()?",CONFINEDSPACES":"")
					+(Ext.getCmp('addcontelectricworks').getValue()?",CONTELECTRICWORKS":"")
					+(Ext.getCmp('addworkhots').getValue()?",WORKHOTS":"")
					+(Ext.getCmp('addchemicalsubstances').getValue()?",CHEMICALSUBSTANCES":"")), '', 'CONTRATIST'),
			valueField : 'strValue2',
			displayField: 'strValue1',
			emptyText : 'Seleccione',
			typeAhead: true,
            minChars: 1,
            forceSelection: true,
            triggerAction: 'all',
            labelWidth:100,
            width : 400,
			allowBlank : true,
			margin:'10 0 10 10'
	},{
		xtype : 'filefield',
		name : 'file',
		fieldLabel : 'Archivo:',
		labelWidth : 70,
		msgTarget : 'side',
		allowBlank : false,
		width:300,
		buttonText : 'Archivo',
		margin:'10 0 10 10'
//		listeners: {
//            'change': function(f, value){
//                  alert(f.size); // not filesize
//            }}
	}
//	,
//		{
//		 xtype: 'button',
//			itemId : 'faddTaxVaultRequestEmployBtn',
//			id : 'faddTaxVaultRequestEmployBtn',
//			text : 'Agregar',
//			action : 'addFileTaxVaultRequestBtnAct',
//			labelWidth : 70,
//			margin:'10 0 10 10'
//		}
	],
	buttons : [ {
		text : SuppAppMsg.supplierLoad,
		margin:'10 0 0 0',
		handler : function() {
			var form = this.up('form').getForm();
			if (form.isValid()) {
				form.submit({
							url : 'taxVault/uploadFileRequest.action',
							waitMsg : SuppAppMsg.supplierLoadFile,
							success : function(fp, o) {
								var res = Ext.decode(o.response.responseText);
//								
								storeFile.add(res.data);
							},       // If you don't pass success:true, it will always go here
					        failure: function(fp, o) {
					        	var res = Ext.decode(o.response.responseText);
					        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
					        }
						});
			}
		}
	} ]
	            })
	]
        
        ;
              
        this.callParent(arguments);
    }
	
});