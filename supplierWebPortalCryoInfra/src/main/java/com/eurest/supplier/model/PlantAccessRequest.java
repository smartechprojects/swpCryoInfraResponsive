package com.eurest.supplier.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@JsonAutoDetect
@Entity
@Table(name="plantAccessRequest")
public class PlantAccessRequest {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date fechaSolicitud;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date fechaAprobacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date fechaInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date fechaFin;
	
	private String rfc;
	private String status;
	private String nameRequest;
    private String ordenNumber;
	private String contractorCompany;
	private String contractorRepresentative;
	private String descriptionUbication;
	private String aprovUser;
	private String aprovUserDef;
	private String highRiskActivities;
	private String fechaSolicitudStr;
	private String addressNumberPA;
	private String plantRequest;
	private boolean heavyEquipment;
	private String userRequest;
	private String razonSocial;
	private boolean sinOrden;
	private String contactEmergency;
	private String employerRegistration;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date fechafirmGui;
	
    private String nombreAprobador;
    private boolean subcontractService;
    private String subContractedCompany;
    private String subContractedCompanyRFC;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNameRequest() {
		return nameRequest;
	}
	public void setNameRequest(String nameRequest) {
		this.nameRequest = nameRequest;
	}
	public String getOrdenNumber() {
		return ordenNumber;
	}
	public void setOrdenNumber(String ordenNumber) {
		this.ordenNumber = ordenNumber;
	}
	public String getContractorCompany() {
		return contractorCompany;
	}
	public void setContractorCompany(String contractorCompany) {
		this.contractorCompany = contractorCompany;
	}
	public String getContractorRepresentative() {
		return contractorRepresentative;
	}
	public void setContractorRepresentative(String contractorRepresentative) {
		this.contractorRepresentative = contractorRepresentative;
	}
	public String getDescriptionUbication() {
		return descriptionUbication;
	}
	public void setDescriptionUbication(String descriptionUbication) {
		this.descriptionUbication = descriptionUbication;
	}
	public String getAprovUser() {
		return aprovUser;
	}
	public void setAprovUser(String aprovUser) {
		this.aprovUser = aprovUser;
	}
	public String getHighRiskActivities() {
		return highRiskActivities;
	}
	public void setHighRiskActivities(String highRiskActivities) {
		this.highRiskActivities = highRiskActivities;
	}
	public String getFechaSolicitudStr() {
		return fechaSolicitudStr;
	}
	public void setFechaSolicitudStr(String fechaSolicitudStr) {
		this.fechaSolicitudStr = fechaSolicitudStr;
	}
	public String getAddressNumberPA() {
		return addressNumberPA;
	}
	public void setAddressNumberPA(String addressNumberPA) {
		this.addressNumberPA = addressNumberPA;
	}
	public String getPlantRequest() {
		return plantRequest;
	}
	public void setPlantRequest(String plantRequest) {
		this.plantRequest = plantRequest;
	}
	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}
	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public boolean isHeavyEquipment() {
		return heavyEquipment;
	}
	public void setHeavyEquipment(boolean heavyEquipment) {
		this.heavyEquipment = heavyEquipment;
	}
	public String getUserRequest() {
		return userRequest;
	}
	public void setUserRequest(String userRequest) {
		this.userRequest = userRequest;
	}
	public boolean isSinOrden() {
		return sinOrden;
	}
	public void setSinOrden(boolean sinOrden) {
		this.sinOrden = sinOrden;
	}
	public String getContactEmergency() {
		return contactEmergency;
	}
	public void setContactEmergency(String contactEmergency) {
		this.contactEmergency = contactEmergency;
	}
	
	public Date getFechafirmGui() {
		return fechafirmGui;
	}
	public void setFechafirmGui(Date fechafirmGui) {
		this.fechafirmGui = fechafirmGui;
	}
	public String getNombreAprobador() {
		return nombreAprobador;
	}
	public void setNombreAprobador(String nombreAprobador) {
		this.nombreAprobador = nombreAprobador;
	}
	public String getEmployerRegistration() {
		return employerRegistration;
	}
	public void setEmployerRegistration(String employerRegistration) {
		this.employerRegistration = employerRegistration;
	}
	public String getAprovUserDef() {
		return aprovUserDef;
	}
	public void setAprovUserDef(String aprovUserDef) {
		this.aprovUserDef = aprovUserDef;
	}
	public boolean isSubcontractService() {
		return subcontractService;
	}
	public void setSubcontractService(boolean subcontractService) {
		this.subcontractService = subcontractService;
	}
	public String getSubContractedCompany() {
		return subContractedCompany;
	}
	public void setSubContractedCompany(String subContractedCompany) {
		this.subContractedCompany = subContractedCompany;
	}
	public String getSubContractedCompanyRFC() {
		return subContractedCompanyRFC;
	}
	public void setSubContractedCompanyRFC(String subContractedCompanyRFC) {
		this.subContractedCompanyRFC = subContractedCompanyRFC;
	}
	
	
	
}