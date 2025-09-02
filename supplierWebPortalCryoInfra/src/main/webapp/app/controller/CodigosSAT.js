Ext.define('SupplierApp.controller.CodigosSAT', {
    extend: 'Ext.app.Controller',
    stores: [],
    models: ['CodigosSAT'],
    views: ['supplier.CodigosSATPanel','supplier.CodigosSATGrid'],
    refs: [{
        	ref: 'codigosSATGrid',
        	selector: 'codigosSATGrid'
	    }],
 
    init: function() {
        this.control({
			'codigosSATGrid button[action=codigoSatSearchBtn]' : {
				click : this.codigoSatSearchBtn
			},
			'codigosSATGrid button[action=uploadCodigosSatFile]' : {
				click : this.uploadCodigosSatFile
			}
        });
    },
    
    codigoSatSearchBtn: function(button) {
    	var grid = this.getCodigosSATGrid();
    	var store = grid.getStore();
    	var str = Ext.getCmp('codigoSatSearch').getValue();
    	if(str){
    		if(str != ""){
    			store.proxy.extraParams = { 
		    			query : str
    			        }
    	    	store.load();
    		}
    	}
    },
    
    uploadCodigosSatFile: function(button) {
        var me = this; 
        var grid = this.getCodigosSATGrid();
    	var store = grid.getStore();
    	var filePanel = Ext.create(
    					'Ext.form.Panel',
    					{
    						width : 900,
    						items : [
    								{
    									xtype : 'filefield',
    									name : 'file',
    									fieldLabel : SuppAppMsg.supplierFileExcel,
    									labelWidth : 120,
    									msgTarget : 'side',
    									allowBlank : false,
    									margin:'20 0 70 0',
    									anchor : '90%',
    									buttonText : SuppAppMsg.suppliersSearch
    								} ],

    						buttons : [ {
    							text : SuppAppMsg.supplierLoad,
    							margin:'10 0 0 0',
    							handler : function() {
    								var form = this.up('form').getForm();
    								if (form.isValid()) {
    									form.submit({
    												url : 'uploadExcelCodigosSat.action',
    												waitMsg : SuppAppMsg.supplierProcessRequest,
    												success : function(fp, o) {
    													var res = Ext.decode(o.response.responseText);
    													if(res.count > 0){
    														Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg: SuppAppMsg.satMsg1 });
    													}else{
    														Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad +' -  ERROR', msg: SuppAppMsg.supplierFileSuppFile });
    													}
    													me.winLoadSupplier.close();
    													
    												},       // If you don't pass success:true, it will always go here
    										        failure: function(fp, o) {
    										        	var res = Ext.decode(o.response.responseText);
    										        	me.winLoadSupplier.close();
    										        	Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.supplierMsgValidationLoad, msg:  res.message});
    										        }
    											});
    								}
    							}
    						} ]
    					});

    	this.winLoadSupplier = new Ext.Window({
    		layout : 'fit',
    		title : SuppAppMsg.satTitle1 ,
    		width : 600,
    		height : 160,
    		modal : true,
    		closeAction : 'destroy',
    		resizable : false,
    		minimizable : false,
    		maximizable : false,
    		plain : true,
    		items : [ filePanel ]

    	});
    	this.winLoadSupplier.show();
  
    }
});