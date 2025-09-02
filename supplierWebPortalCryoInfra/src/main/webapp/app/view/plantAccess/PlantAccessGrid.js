	Ext.define('SupplierApp.view.plantAccess.PlantAccessGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.plantAccessGrid',
    itemId: 'paMainGrid',
    loadMask: true,
	frame:false,
	border:false,
	cls: 'extra-large-cell-grid',  
    store:'PlantAccess',
	scroll : true,
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' }
	},
    initComponent: function() {
    	
    	var docType = null;
    	var invStatus = null;
    	

    	invStatus = Ext.create('Ext.data.Store', {
    	    fields: ['id', 'name'],
    	    data : [    	        
    	    	{"id":"", "name":"SELECCIONAR"},
    	    	{"id":"PENDIENTE", "name":"PENDIENTE"},
    	        {"id":"APROBADO", "name":"APROBADO"},
    	        {"id":"RECHAZADO", "name":"RECHAZADO"},
    	        {"id":"GUARDADO", "name":"GUARDADO"},
    	       // {"id":"CANCELADO", "name":"CANCELADO"}
    	    ]
    	});
    	
    	
    	
    	Ext.define('statusCombo', {
    	    extend: 'Ext.form.ComboBox',
    	    fieldLabel: SuppAppMsg.fiscalTitle22,
    	    store: invStatus,
    	    alias: 'widget.combostatus',
    	    queryMode: 'local',
    	    allowBlank:false,
    	    editable: false,
    	    displayField: 'name',
			width:150,
    	    labelWidth:40,
    	    valueField: 'id',
    	    margin:'20 20 0 10',
    	    emptyText:'SELECCIONAR',
    	    id:'combostatus',
    	    listeners: {
    	        afterrender: function() {
    	        	   this.setValue("");    
    	        }
    	    }
    	});
    	
        this.columns = [
		        	{
			            text     : 'Id de Solicitud',
			            width: 100,
			            dataIndex: 'id',
			            hidden:true
			        },{
			            text     : SuppAppMsg.approvalRequestDate,
			            width: 150,
			            dataIndex: 'fechaSolicitudStr',
			            //renderer : Ext.util.Format.dateRenderer("d-m-Y"),
			        },{
			            text     : 'UUID',
			            width: 230,
			            dataIndex: 'rfc',
			            hidden:true
			        },{
			            text     : SuppAppMsg.plantAccess51,
			            width: 230,
			            dataIndex: 'addressNumberPA',
			        },{
			            text     : SuppAppMsg.plantAccess64,
			            width: 230,
			            dataIndex: 'razonSocial', 
			        },{
			            text     : SuppAppMsg.plantAccess2,
			            width: 230,
			            dataIndex: 'nameRequest',
			        },{
			            text     : SuppAppMsg.fiscalTitle22,
			            width: 230,
			            dataIndex: 'status'
			        },{
			            text     : 'Vigente',
			            width: 110,
			            hidden:true,
			            dataIndex: 'status'
			        },{
			            text     : SuppAppMsg.plantAccess3,
			            width: 110,
			            dataIndex: 'aprovUser'
			        }, 	{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.taxvaulRequest,
			            align: 'center',
						name : 'plantAccessReportBatch',
//						hidden: role == 'ROLE_SUPPLIER'?true:false,
						itemId : 'plantAccessReportBatch',
			            style: 'text-align:center;',
			            items: [
			            	{
			            		icon:'resources/images/archivo-pdf.png',
			            		getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			            				if(r.data.status != "APROBADO") {
				              	              return "x-hide-display";
				              	          }
				              	      },
			             	      text: SuppAppMsg.freightApprovalReportBatch,
				                  handler: function(grid, rowIndex, colIndex) {
				                	 
				                	  var record = grid.store.getAt(rowIndex);
										var href = "plantAccess/plantAccessPDF.action?uuid=" + record.data.rfc;
										window.open(href, '_blank');
										//setTimeout(function(){ newWindow.document.title = 'Plant Access PDF'; }, 10);
			                  }}]
			        },{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.approvalApprove,
			            align: 'center',
			            hidden: role=='ROLE_PURCHASE_READ' || role == 'ROLE_SUPPLIER'?true:false,
						name : 'approvePlantAccess',
						itemId : 'approvePlantAccess',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/accept.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              	          if(!(r.data.status == "PENDIENTE" && r.data.aprovUser.includes(userName) )) {
			              	              return "x-hide-display";
			              	          }
			              	      },
			              	      text: SuppAppMsg.approvalApprove,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        },{
			        	xtype: 'actioncolumn', 
			            width: 90,
			            header: SuppAppMsg.approvalReject,
			            align: 'center',
						name : 'rejectInvoiceFDA',
						//hidden: role=='ROLE_ADMIN' || role=='ROLE_PURCHASE'?false:true,
						hidden: role=='ROLE_PURCHASE_READ' || role == 'ROLE_SUPPLIER'?true:false,
						itemId : 'rejectInvoiceFDA',
			            style: 'text-align:center;',
			            items: [
			            	{
			            	icon:'resources/images/close.png',
			              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
			              		 if(!(r.data.status == "PENDIENTE" && r.data.aprovUser.includes(userName) )) {
		              	              return "x-hide-display";
		              	          }
			              	      },
			              	      text: SuppAppMsg.approvalReject,
			                  handler: function(grid, rowIndex, colIndex) {
			                  this.fireEvent('buttonclick', grid, rowIndex, colIndex);
			                  }}]
			        }];
        
        this.tbar = [
        	{
    			xtype: 'textfield',
                fieldLabel: 'Aprobador',
                id: 'approverPlantAccess',
                itemId: 'approverPlantAccess',
                name:'approverPlantAccess',
                //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
                value: role != 'ROLE_SUPPLIER' ?userName:'',
                readOnly: true,
                hidden:true,
                width:200,
                labelWidth:50,
                margin:'20 20 0 10'
    		},{
    			xtype: 'textfield',
                fieldLabel: SuppAppMsg.plantAccess51, 
                id: 'addressNumberGrid',
                itemId: 'addressNumberGrid',
                name:'addressNumberGrid',
                //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
                value: role.includes('ROLE_SUPPLIER') ?addressNumber:'',
                readOnly: role.includes('ROLE_SUPPLIER') ?true:false,
                hidden: role.includes('ROLE_SUPPLIER') ?true:false,		
                //hidden: role.includes('ROLE_SUPPLIER')?true:false,
                width:200,
                labelWidth:50,
                margin:'20 20 0 10'
    		},{
			xtype: 'textfield',
            fieldLabel: SuppAppMsg.supplierForm5.substring(0,SuppAppMsg.supplierForm5.length - 1), 
            id: 'RFC',
            itemId: 'RFC',
            name:'RFC',
            //value: role == 'ROLE_SUPPLIER' || role=='ROLE_SUPPLIER_OPEN'?addressNumber:'',
            //value: role == 'ROLE_SUPPLIER'?displayName:'',
            readOnly: role == 'ROLE_SUPPLIER' ?true:false,
            hidden: role == 'ROLE_SUPPLIER' ?true:false,
            //hidden: role == 'ROLE_SUPPLIER'?true:false,
            width:200,
            labelWidth:50,
            margin:'20 20 0 10'
		},{ 
			xtype: 'combostatus'
		},{
       		xtype:'button',
            text: SuppAppMsg.suppliersSearch,
            iconCls: 'icon-appgo',
            action:'parSearch',
            cls: 'buttonStyle',
            margin:'0 20 0 10'
		},{
			iconCls : 'icon-add',
			itemId : 'addPlantAccessRequest',
			id : 'addPlantAccessRequest',
			text : SuppAppMsg.plantAccess1,
			action : 'addNewPlantAccessRequest',
//			hidden: role == 'ROLE_SUPPLIER'?false:true,
		}
		];
        
		this.bbar = Ext.create('Ext.PagingToolbar', {
			store: this.store,
			displayInfo : true,
			beforePageText : SuppAppMsg.page,
			afterPageText :SuppAppMsg.de + ' {0}',
			emptyMsg  : SuppAppMsg.emptyMsg ,
			displayMsg :SuppAppMsg.displayMsg + ' {0} - {1} '+ SuppAppMsg.de +' {2}'
		});
		
        this.callParent(arguments);
    }
});