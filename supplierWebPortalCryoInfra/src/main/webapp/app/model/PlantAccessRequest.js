Ext.define('SupplierApp.model.PlantAccessRequest', {
    extend: 'Ext.data.Model',
    fields: [
				//{name:  'id'},
    			{ name: 'id', type: 'int' },
				{ name: 'fechaSolicitud', type: 'int' },
				{ name: 'fechaSolicitudStr', type: 'string' },
				{name:  'addressNumberPA'},
				{name:  'razonSocial'},
				{name:  'fechaSolicitud', type: 'date', dateFormat: 'c'},
				{name:  'fechaInicio', type: 'date', dateFormat: 'c'},
				{name:  'fechaFin', type: 'date', dateFormat: 'c'},
				{name:  'rfc'},
				{name:  'status'},
				{name:  'nameRequest'},
				{name:  'ordenNumber'},
				{name:  'contractorCompany'},
				{name:  'contractorRepresentative'},
				{name:  'descriptionUbication'},
				{name:  'aprovUser'},
				{name:  'aprovUserDef'},
				{name:  'plantRequest'},
				{name:  'highRiskActivities'},
				{name:	'heavyEquipment', type:'boolean'},
				{name:  'userRequest'},
				{name:  'sinOrden'},
				{name:  'contactEmergency'},
				{name:  'employerRegistration'},
				{name: 'fechafirmGui', type: 'date', dateFormat: 'time'},
				{name:  'subcontractService', type:'boolean'},
				{name:  'subContractedCompany'},
				{name:  'subContractedCompanyRFC'}
        ],
        proxy: {
            type: 'ajax',
            api: {
                create: 'plantAccess/savePlantAccessRequest.action',
                update: 'plantAccess/updatePlantAccessRequest.action',
                destroy: 'plantAccess/deletePlantAccessRequest.action'
            },
            reader: {
                type: 'json',
                rootProperty: 'data',
                successProperty: 'success'
            },
            writer: {
            	type: 'json',
                writeAllFields: true,
                encode: true,
                encode: false
            }
        }
});