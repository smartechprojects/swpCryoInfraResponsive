Ext.define('SupplierApp.view.outSourcing.OutSourcingGrid' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.outSourcingGrid',
    loadMask: true,
    forceFit: true,
	cls: 'extra-large-cell-grid', 
	store : {
		type:'outsourcingdocument'
	},
	viewConfig: {
		stripeRows: true,
		style : { overflow: 'auto', overflowX: 'hidden' },
		getRowClass: function(record, rowIndex, rowParams, store) {

			if(record.get('docStatus') == 'RECHAZADO'){
				return 'alertOrange';
			}
        }
	},
    initComponent: function() {
    	
    	var osController = SupplierApp.app.getController("SupplierApp.controller.OutSourcing");
    	
    	var statusStore = Ext.create('Ext.data.ArrayStore', {
    	    fields: ['id', 'value'],
    	    //data :[['APROBADO', 'Aprobado'],['PENDIENTE', 'Pendiente'],['RECHAZADO', 'Rechazado']]
    	    data :[['APROBADO', 'APROBADO'],['PENDIENTE', 'PENDIENTE'],['RECHAZADO', 'RECHAZADO']]
    	});

    	var documentType = Ext.create('Ext.data.ArrayStore', {
    	    fields: ['name', 'type'],
    	    data :[['PAGO_PROV_IVA', 'Pago Provisional de IVA'],['CONST_CUMP_OBLIG', 'Const de cumpl de obligaciones'],
    	    	   ['CONST_SIT_FIS', 'Constancia de Sit Fiscal '],['AUT_STPS', 'Autorización STPS'],
    	    	   ['CEDU_DETERM_CUOTAS', 'Cedula de Determinacion de Cuotas'],
    	    	   /*['LISTA_TRAB', 'Lista de trabajadores'],*/['DECL_MENS_IMSS', 'Decl inf del IMSS'],
    	    	   ['PROV_ISR_SAL', 'Pagos Provisionales de ISR'],['CUOT_OBR_PATR', 'Cuotas obrero-patronales al IMSS'],
    	    	   ['APOR_FON_NAL_VIV', 'Aportaciones INFONAVIT'],/*['PAGO_ISN', 'Pago de ISN mensual'],*/
    	    	   ['REC_NOMINA', 'Recibos de Nómina']]
    	});
    	
    	var monthLoad = Ext.create('Ext.data.ArrayStore', {
    	    fields: ['name', 'month'],
    	    data :[['&nbsp;', null],['ENERO', 1],['FEBRERO', 2],
    	    	   ['MARZO', 3],['ABRIL',4],
    	    	   ['MAYO', 5],['JUNIO', 6],
    	    	   ['JULIO', 7],['AGOSTO', 8],
    	    	   ['SEPTIEMBRE', 9],['OCTUBRE', 10],
    	    	   ['NOVIEMBRE', 11],['DICIEMBRE', 12]]
    	});
    	
        var yearsStore = Ext.create('Ext.data.ArrayStore', {
	          fields : ['years'],
	          //data : years
	         //data : [['&nbsp;'],[years]]
	          data : [['2023'],['2022']]
	    });    	
    	
        this.columns = [
       {
                hidden:true,
                dataIndex: 'id'
       },{
            text     : 'Fecha de Carga',
            width: 110,
            dataIndex: 'uploadDate',
            renderer: function(date){
                return Ext.Date.format((new Date(date)),'d-M-Y  H:i');
            }	
        },{
            text     : 'Proveedor',
            width: 180,
            dataIndex: 'supplierName'
        },{
    	        text     : 'RFC Proveedor',
    	        width: 130,
    	        dataIndex: 'rfc'
    	},{
                text     : 'Periodo',
                width: 120,
               // xtype:'templatecolumn', 
               // tpl:'{monthLoad} {yearLoad}',
                renderer: function(value,metaData,record,store,view){
                
                	var month = record.data.monthLoad;
                	var year = record.data.yearLoad;
                	if(month != 0){
                		return monthToString(month) + " " + year;
                	}else
                		{
                		return "";
                		}
                    
                }	
         },{        	        	
            text     : 'Archivo',
            width: 220,
            dataIndex: 'name'
         },{
             width: 115,
             renderer: function (value, meta, record) {
             	var hrefDoc = "supplier/openOSDocument.action?id=" + record.data.id;
             	
             	var hrefPayroll = "";
             	if(record.data.documentType == 'REC_NOMINA'){
             		hrefPayroll = '</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +  '<a href="' + 'fiscalDocuments/openInvoiceByUuid.action?uuid=' + record.data.uuid + '" target="_blank">'+ 'Ver factura' +'</a>';
             		return '<a href="' + hrefDoc + '" target="_blank">'+ 'Ver recibos' + hrefPayroll ;
             	}else{
             		return '<a href="' + hrefDoc + '" target="_blank">'+ 'Abrir documento';
             	}
             	
                 
             }
         },{
            text     : 'Tipo',
            width: 100,
            dataIndex: 'documentType'
        },{
            text     : 'UUID',
            width: 100,
            hidden:true,
            dataIndex: 'uuid'
        },{
            text     : 'Frecuencia',
            width: 100,
            dataIndex: 'frequency',
            renderer: function(value, meta, record) {
            	            	
            	switch (value) {
            	  case 'INV':
            	    return "Facturación";
            	    break;
            	  case 'MONTH':
              	    return "Mensual";
              	    break;
            	  case 'QUARTER':
              	    return "Cuatrimestral";
              	    break;
            	  case 'BL':
              	    return "Inicial";
              	    break;
              	  default:
              		break;
            	}

             }
        },{
            text     : 'Estado',
            width: 100,
            dataIndex: 'docStatus'
        },{
            xtype: 'actioncolumn', 
            width: 40,
            header: SuppAppMsg.purchaseTitle30,
            align: 'center',
			name : 'openOSNotes',
			itemId : 'openOSNotes',
            style: 'text-align:center;',
            items: [
            	{
            	icon:'resources/images/notepad.png',
          	     iconCls: 'increaseSize',
            	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
              		  if(r.data.notes == null || r.data.notes == '') {
        	              return "x-hidden-display";
        	          }else{
        	        	  return "increaseSize";
        	          }
              	  },
                  handler: function(grid, rowIndex, colIndex) {
                  	this.fireEvent('buttonclick', grid, rowIndex, colIndex);
             }}]
        },{
        	xtype: 'actioncolumn', 
            hidden:role.includes('ROLE_ADMIN') || role.includes('ROLE_RH') || role.includes('ROLE_TAX') || role.includes('ROLE_LEGAL') || role.includes('ROLE_3RDPARTY')?false:true,
            width: 90,
            header: SuppAppMsg.approvalReject,
            align: 'center',
			name : 'rejectOSDoc',
			itemId : 'rejectOSDoc',
            style: 'text-align:center;',
            items: [
            	{
            	  icon:'resources/images/close.png',
            	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
              		  if(r.data.docStatus != "PENDIENTE") {
        	              return "x-hidden-display";
        	          }else{
        	        	  return "increaseSize";
        	          }
              	  },
                  handler: function(grid, rowIndex, colIndex) {
                  	this.fireEvent('buttonclick', grid, rowIndex, colIndex);
             }}]
        },{
        	xtype: 'actioncolumn', 
        	hidden:role.includes('ROLE_ADMIN') || role.includes('ROLE_RH') || role.includes('ROLE_TAX') || role.includes('ROLE_LEGAL') || role.includes('ROLE_3RDPARTY')?false:true,
            width: 90,
            header: SuppAppMsg.approvalApprove,
            align: 'center',
			name : 'approveSODoc',
			itemId : 'approveSODoc',
            style: 'text-align:center;',
            items: [
            	{
            		icon:'resources/images/accept.png',
            	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
              		  if(r.data.docStatus != "PENDIENTE") {
        	              return "x-hidden-display";
        	          }else{
        	        	  return "increaseSize";
        	          }
              	   },
                  handler: function(grid, rowIndex, colIndex) {
                  	this.fireEvent('buttonclick', grid, rowIndex, colIndex);
             }}]
        },{
        	xtype: 'actioncolumn', 
            hidden:role.includes('ROLE_SUPPLIER')?false:true,
            width: 80,
            header: 'Cargar reemplazo',
            align: 'center',
			name : 'uploadNewSODoc',
			itemId : 'uploadNewSODoc',
            style: 'text-align:center;',
            items: [
            	{
            	  icon:'resources/images/file.png',
              	  getClass: function(v, metadata, r, rowIndex, colIndex, store) {
              		  
              		  if(r.data.documentType == 'REC_NOMINA'){
              			return "x-hidden-display";
              		  }

              		  if(r.data.docStatus != "RECHAZADO") {
        	              return "x-hidden-display";
        	          }else{
        	        	  return "increaseSize";
        	          }
              	   },
                  handler: function(grid, rowIndex, colIndex) {
                  	this.fireEvent('buttonclick', grid, rowIndex, colIndex);
             }}]
        }];
        
        this.dockedItems = [{
		    xtype: 'toolbar',
		    dock: 'top',
		    items: [
		    	/*{
		            xtype: 'datefield',
		            labelWidth: 30,
		            width:100,
		            fieldLabel: SuppAppMsg.purchaseOrderDesde,
		            id: 'fromDateOS',
		            margin:'10 0 0 10',
		            dateFormat: 'Y-M-d',
		            labelAlign:'top',
		            maxValue: new Date()
		        },{
		            xtype: 'datefield',
		            labelWidth: 30,
		            width:100,
		            margin:'10 0 0 30',
		            dateFormat: 'Y-M-d',
		            fieldLabel: SuppAppMsg.purchaseOrderHasta,
		            id: 'toDateOS',
		            labelAlign:'top',
		            maxValue: new Date()
		        },*/{
						xtype : 'combo',
							fieldLabel : SuppAppMsg.supplierForm058,
							id : 'periodMonth',
							name : 'periodMonth',
							store : monthLoad,
							valueField : 'month',
							displayField: 'name',
			                typeAhead: true,
			                minChars: 2,
			                triggerAction: 'all',
			                labelWidth:30,
			                width : 100,
			                labelAlign:'top',
			                fieldStyle: 'font-size:10px;',
			                margin:'0 20 0 25',
			                editable: false,
			                listeners: {
			                    select: function (comp, record, index) {
			                    	
			                        if (comp.getValue() === null) {
			                            comp.setRawValue('');
			                            comp.setValue(null);
			                        }
			                    }
			                }
					},{
						xtype : 'combo',
						fieldLabel : SuppAppMsg.supplierForm059,
						id : 'periodYear',
						name : 'periodYear',
						store : yearsStore,
						valueField : 'years',
						displayField: 'years',
		                typeAhead: true,
		                minChars: 2,
		                triggerAction: 'all',
		                labelWidth:30,
		                width : 100,
		                labelAlign:'top',
		                fieldStyle: 'font-size:10px;',
		                editable: false,
		                listeners: {
		                    select: function (comp, record, index) {
		                    	
		                        if (comp.getValue() === '&nbsp;') {
		                            comp.setRawValue('');
		                            comp.setValue(null);
		                        }
		                    }
		                }		               
					},{
						
			            xtype: 'combo',
			            fieldLabel: 'Tipo de archivo',
			            labelWidth : 40,
						width:200,
			            store: documentType,
			            queryMode: 'local',
			            displayField: 'type',
			            valueField: 'name',
			            margin:'0 0 0 30',
			            labelAlign:'top',
						id : 'documentTypeOS',
						fieldStyle: 'font-size:10px;'
			        },{					
		            xtype: 'combo',
		            fieldLabel: 'Estado',
		            labelWidth : 40,
					width:150,
					store:statusStore,
		            valueField: 'id',
		            displayField: 'value',
		            margin:'10 0 0 20',
		            labelAlign:'top',
					id : 'docStatusOS'
		        },{
					xtype: 'textfield',
		            fieldLabel: SuppAppMsg.suppliersNameRfc,
		            id: 'supNameOS',
		            width:200,
		            labelWidth:40,
		            labelAlign:'top',
		            margin:'10 0 0 20',
		            hidden:role.includes('ROLE_ADMIN') || role.includes('ROLE_TAX') || role.includes('ROLE_RH')|| role.includes('ROLE_LEGAL')|| role.includes('ROLE_REPSE') || role.includes('ROLE_3RDPARTY')?false:true,
            		listeners:{
						change: function(field, newValue, oldValue){
							field.setValue(newValue.toUpperCase());
						}
					}
				},{
					xtype: 'textfield',
		            id: 'supNumberOS',
		            width:200,
		            labelWidth:40,
		            labelAlign:'top',
		            margin:'0 20 0 5',
		            readOnly:true,
		            hidden:true,
		            value:role.includes('ROLE_SUPPLIER')?addressNumber:''
				},{
					xtype: 'button',
					margin:'10 0 0 30',
					text: SuppAppMsg.suppliersSearch,
					iconCls:'icon-search',
					itemId : 'searchDocsOS',
					id : 'searchDocsOS',
					action : 'searchDocsOS',
					cls: 'buttonStyle',
					listeners: {
   	                    tap: function (button) {
   	                    	osController.searchDocsOS(button);
   	                    }
   	                }
				},{
					xtype: 'button',
					margin:'10 0 0 30',
					text: SuppAppMsg.outsourcingDownloadFilesTitle,
					iconCls:'icon-zip',
					itemId : 'downloadDocsOS',
					id : 'downloadDocsOS',
					action : 'downloadDocsOS',
					cls: 'buttonStyle',
					listeners: {
   	                    tap: function (button) {
   	                    	osController.downloadDocsOS(button);
   	                    }
   	                }
				}]
		},
		getPagingContent()
		];
   	 

	 function monthToString(month){
 		var  months = ["","ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"];
 		 return months[month]; 
 	 };
 	 

     this.callParent(arguments);
 }
});