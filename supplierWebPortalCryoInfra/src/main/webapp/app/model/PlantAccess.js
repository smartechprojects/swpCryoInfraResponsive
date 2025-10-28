Ext.define('SupplierApp.model.PlantAccess', {
    extend: 'Ext.data.Model',
    fields: [
				{name:  'id'},
				/*{ name: 'fechaSolicitud', type: 'int' },
		        { name: 'fechaSolicitudStr', type: 'string', convert: function (value, record) {
		            return Ext.util.Format.date(new Date(record.data.fechaSolicitud), 'd-m-Y H:i:s');
		        }},*/
		        { name: 'fechaSolicitudStr', type: 'string'},
				{name:  'addressNumberPA'},
				{name:  'razonSocial'},
				{name:  'fechaSolicitud', type: 'date', dateFormat: 'c'},
				{name:  'fechaInicio', type: 'date', dateFormat: 'c'},
				{name:  'fechaFin', type: 'date', dateFormat: 'c'},
				{name:  'rfc'},
				{name:  'status'},
				{name:  'nameRequest'},
				{name:  'ordenNumber'},
				{name:  'sinOrden'},
				{name:  'contractorCompany'},
				{name:  'contractorRepresentative'},
				{name:  'descriptionUbication'},
				{name:  'employeeOrdenes'},
				{name:  'employeePuesto'},
				{name:  'employeeCurp'},
				{name:  'employeeRfc'},
				{name:  'aprovUser'},
				{name:  'aprovUserDef'},
				{name:  'plantRequest'},
				{name:  'highRiskActivities'},
				{name:  'contactEmergency'},
				{name:  'employerRegistration'},
				{name:  'fechafirmGui', type: 'date', dateFormat: 'c'},
				{name:	'heavyEquipment', type:'boolean'},
				{name:  'subcontractService', type:'boolean'},
				{name:  'subContractedCompany'},
				{name:  'subContractedCompanyRFC'}
        ]
});
