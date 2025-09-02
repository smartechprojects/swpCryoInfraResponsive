package com.eurest.supplier.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@JsonAutoDetect
@Entity
@Table(name="CDDDC")
public class CDDDC {

	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private int idOutsourcingDocument;
	private String periododeProceso;
	private String fechaDeProceso;
	private String registroPatronal;
	private String rfc;
	private String razonSocial;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date fechaProcess;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdOutsourcingDocument() {
		return idOutsourcingDocument;
	}

	public void setIdOutsourcingDocument(int idOutsourcingDocument) {
		this.idOutsourcingDocument = idOutsourcingDocument;
	}

	public String getPeriododeProceso() {
		return periododeProceso;
	}

	public void setPeriododeProceso(String periododeProceso) {
		this.periododeProceso = periododeProceso;
	}

	public String getFechaDeProceso() {
		return fechaDeProceso;
	}

	public void setFechaDeProceso(String fechaDeProceso) {
		this.fechaDeProceso = fechaDeProceso;
	}

	public String getRegistroPatronal() {
		return registroPatronal;
	}

	public void setRegistroPatronal(String registroPatronal) {
		this.registroPatronal = registroPatronal;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public Date getFechaProcess() {
		return fechaProcess;
	}

	public void setFechaProcess(Date fechaProcess) {
		this.fechaProcess = fechaProcess;
	}
	
}
