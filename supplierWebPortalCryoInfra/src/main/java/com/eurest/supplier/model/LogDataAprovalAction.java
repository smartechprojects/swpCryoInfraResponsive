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
@Table(name="LogDataAprovalAction")
public class LogDataAprovalAction {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date approvalDate;
	private String approver;
	private int step;
	private String action;
    private String message;
	private String approvalMethod;
	private String statusMoment;
	private String ip;
	private String uuidFactura;
	private String rfcEmisor;
	private String rfcReceptor;
	private String addressNumber;
	private String module;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date fechaIngreso;
		
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

    public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

    public String getAction() {
		return action;
	}

    public void setAction(String action) {
		this.action = action;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getApprovalMethod() {
		return approvalMethod;
	}

	public void setApprovalMethod(String approvalMethod) {
		this.approvalMethod = approvalMethod;
	}

	public String getStatusMoment() {
		return statusMoment;
	}

	public void setStatusMoment(String statusMoment) {
		this.statusMoment = statusMoment;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getUuidFactura() {
		return uuidFactura;
	}

	public void setUuidFactura(String uuidFactura) {
		this.uuidFactura = uuidFactura;
	}


	public String getRfcEmisor() {
		return rfcEmisor;
	}


	public void setRfcEmisor(String rfcEmisor) {
		this.rfcEmisor = rfcEmisor;
	}


	public String getRfcReceptor() {
		return rfcReceptor;
	}

	public void setRfcReceptor(String rfcReceptor) {
		this.rfcReceptor = rfcReceptor;
	}


	public String getAddressNumber() {
		return addressNumber;
	}


	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

    public Date getFechaIngreso() {
		return fechaIngreso;
	}


	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}



	
}