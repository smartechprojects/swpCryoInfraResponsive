package com.eurest.supplier.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@JsonAutoDetect
@Entity
@Table(name="TaxVaultDocument")

public class TaxVaultDocument {
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String nameFile;
	private String uuid;
	private String rfcEmisor;
	private String rfcReceptor;
	private String usuario;
	private String addressNumber;
	private String year;
	private String invoiceDate;
	private double amount;
	private String serie;
	private String folio;
	private long size;
	private String type;
	private String documentType;
	private boolean status;
	private String documentStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date uploadDate;
	
	@Lob @Basic(fetch = FetchType.LAZY)
	private byte[] content;
	
	private String ip;
	private String hostname;
	private String emisor;
	private String origen;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNameFile() {
		return nullstBlank(nameFile);
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRfcEmisor() {
		return nullstBlank(rfcEmisor);
	}

	public void setRfcEmisor(String rfcEmisor) {
		this.rfcEmisor = rfcEmisor;
	}

	public String getRfcReceptor() {
		return nullstBlank(rfcReceptor);
	}

	public void setRfcReceptor(String rfcReceptor) {
		this.rfcReceptor = rfcReceptor;
	}



	public String getUsuario() {
		return nullstBlank(usuario);
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getAddressNumber() {
		return addressNumber;
	}

	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public String getYear() {
		return nullstBlank(year);
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getSerie() {
		return nullstBlank(serie);
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getFolio() {
		return nullstBlank(folio);
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}


	public String getDocumentType() {
		return nullstBlank(documentType);
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getDocumentStatus() {
		return nullstBlank(documentStatus);
	}

	public void setDocumentStatus(String documentStatus) {
		this.documentStatus = documentStatus;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getIp() {
		return nullstBlank(ip);
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHostname() {
		return nullstBlank(hostname);
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getType() {
		return nullstBlank(type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmisor() {
		return nullstBlank(emisor);
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	
public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

String nullstBlank(String dat) {
	
	if(dat ==null || dat.toLowerCase().equals("null")) {
		return "";
	}
	return dat;
}
	
	
}
