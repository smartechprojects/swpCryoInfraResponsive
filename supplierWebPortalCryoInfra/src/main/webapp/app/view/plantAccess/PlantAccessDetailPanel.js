Ext.define('SupplierApp.view.plantAccess.PlantAccessDetailPanel', {
  extend: 'Ext.Panel',
  alias: 'widget.plantAccessDetailPanel',
  border: false,
  frame: false,

  initComponent: function() {
    var form = Ext.widget('plantAccessForm');
    var grid = Ext.widget('plantAccessDetailGrid');
    var upfile = Ext.widget('plantAccessWorkerFileGrid');

    var gridContainer = Ext.create('Ext.container.Container', {
      layout: 'fit',
      flex: 2,
      id:"containerlistaWorkers",
      name:"containerlistaWorkers",
      items: [grid],
      hidden: false
    });

    var upfileContainer = Ext.create('Ext.container.Container', {
      layout: 'fit',
      id:"containerUpfile",
      name:"containerUpfile",
      flex: 1,
      items: [upfile],
      hidden: true
    });

    Ext.apply(this, {
      layout: {
        type: 'vbox',
        align: 'stretch'
      },
      scrollable: true,
      items: [
        form,
        gridContainer,
        upfileContainer
      ],
      buttons: [
        {
          text: 'Finalizar trabajador',
          id: 'cancelarAgrTab',
          hidden: true,
          handler: function() {
        	debugger;
          	if (Ext.getCmp('addemployeename').getValue().trim() != ''&&
            		Ext.getCmp('addemployeenappat').getValue().trim() != ''&&
            		Ext.getCmp('addemployeenapmat').getValue().trim() != ''&&
            		Ext.getCmp('addmembershipIMSS').getValue() != ''&& 
            		Ext.getCmp('addDatefolioIDcard').getValue() != ''&&
            		Ext.getCmp('addfolioIDcard').getValue() != ''){
          		
        		if(Ext.getCmp('addmembershipIMSS').getValue().length!=11){
        			Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: SuppAppMsg.plantAccess63});
        			return;
        		}
        		
            	var store = grid.getStore();
        		var worker = store.findRecord('datefolioIDcard',  Ext.getCmp('addDatefolioIDcard').getValue());
        		
        		if(worker ==null){
        			var activities= ""+((Ext.getCmp('addworkatheights').getValue()?"1,":"")
        					+(Ext.getCmp('addHeavyequipmentWorker').getValue()?"2,":"")
        					+(Ext.getCmp('addconfinedspaces').getValue()?"3,":"")
        					+(Ext.getCmp('addcontelectricworks').getValue()?"4,":"")
        					+(Ext.getCmp('addworkhots').getValue()?"5,":"")
        					+(Ext.getCmp('addchemicalsubstances').getValue()?"6,":"")
        					+(Ext.getCmp('activityFree').getValue()?"*":""));
            		
            		if(activities!=""){
        	          	  Ext.Ajax.request({
        		      		    url: 'plantAccess/verifyWorkerFiles.action',
        		      		    method: 'POST',
        		      		    params: {
        		      		    	idWorker:Ext.getCmp('addemployeeId').value,        		    	
        		      		    },
        		      		    success: function(fp, o) {
        		      		    	debugger;
        		      		    dta=JSON.parse(fp.responseText);
        		      		    
        		      		    if(!dta.success){
        		      		    	Ext.MessageBox.show({
        		      		            title: 'Error',
        		      		            msg: dta.message,
        		      		            buttons: Ext.Msg.OK
        		      		        });
        		      		    	return;
        		      		    }
        		      		    	
        		//      		    	 griDformulario.hide();
        		//      					formHeader.hide();
        		      					grid.show();
        		      	                
        		      	                Ext.getCmp('containerlistaWorkers').show()
        		      	            gridContainer.show();
        		      	            form.show();
        		      	            
        		      	            upfile.query('textfield').forEach(function(textfield) {
        		      	              textfield.setValue('');
        		      	            });
        		
        		      	            upfile.query('checkboxfield').forEach(function(checkbox) {
        		      	              checkbox.setValue(false);
        		      	            });
        		
        		      	            Ext.getCmp('cancelarAgrTab').hide();
        		      	            Ext.getCmp('datosRegister').show();
        		      	            Ext.getCmp('documentContainer').hide();
        		      	            var estatusRequestPlan = form.down('#estatusPlantAccess').getValue();
        		
        		      	            if (estatusRequestPlan !== 'APROBADO' && estatusRequestPlan !== 'PENDIENTE') {
        		      	              Ext.getCmp('AgrTabNuevo').show();
        		      	            }
        		
        		      	            upfileContainer.hide();
        		      	            gridContainer.setHeight(form.getHeight() - upfileContainer.getHeight());
        		      	            
        		      		    	
        		      		    	
        		      		    	
        		      		    },
        		      		    failure: function() {
        		      		    	Ext.MessageBox.show({
        		      		            title: 'Error',
        		      		            msg: SuppAppMsg.approvalUpdateError,
        		      		            buttons: Ext.Msg.OK
        		      		        });
        		      		    }
        		      		});
            		} else {    		
            			Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.plantAccess89 , msg:  SuppAppMsg.plantAccessTempMessage24 });
            			return;
            		}
        		} else {
        			Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.plantAccess89 , msg:  SuppAppMsg.plantAccessTempMessage25 });
        			return
        		}          		          		
          	} else {
          		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.plantAccess89 , msg:  SuppAppMsg.plantAccessTempMessage26 });
          		return;
          	}        	  
          }
        },
        {
          text: 'Agregar Trabajador',
          id: 'AgrTabNuevo',
          hidden: true,
          handler: function() {
        	debugger;
        	//Primero valida que ya se hayan cargado los archivos de la Solicitud
        	var uuid = uuidPlantAccess;    	
    		Ext.Ajax.request({
    		    url: 'plantAccess/searchFilesPlantAccess.action',
    		    method: 'POST',
    		    params: {
    		    	uuid:uuid
    	        },
    		    success: function(fp, o) {
    		    	debugger;
    		    	//Valida el total de documentos de la solicitud.
    		    	var totalDocs = 3;
			    	var res = Ext.decode(fp.responseText);
			    	var extraDocst = Ext.getCmp('addHeavyequipment').getValue();
			    	if(extraDocst) totalDocs=5;			    	
			    	if(totalDocs == res.total){
        	            form.hide();
        	            gridContainer.hide();
        	            Ext.getCmp('cancelarAgrTab').show();
        	            Ext.getCmp('AgrTabNuevo').hide();
        	            Ext.getCmp('bodyeaderFileWorker').hide();
        	            upfile.query('textfield').forEach(function(textfield) {
        	              textfield.setValue('');
        	              try {
        	                textfield.setReadOnly(false);
        	              } catch (e) {
        	                // TODO: handle exception
        	              }
        	            });

        	            upfile.query('checkboxfield').forEach(function(checkbox) {
        	              checkbox.setValue(false);
        	            });

        	            var store = upfile.getStore();
        	            store.removeAll();
        	            upfileContainer.show();
        	            gridContainer.setHeight(form.getHeight() - upfileContainer.getHeight());
			    	} else {
			    		Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: 'Error', msg: SuppAppMsg.plantAccessTempMessage21 });
			    	}
    		    },
    		    failure: function() {
    		    	Ext.MessageBox.show({
    	                title: 'Error',
    	                msg: SuppAppMsg.approvalUpdateError,
    	                buttons: Ext.Msg.OK
    	            });
    		    }
    		});    	
          }
        }
      ]
    });

    this.callParent(arguments);
  }
});
