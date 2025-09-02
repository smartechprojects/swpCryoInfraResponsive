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
@Table(name="userdocument")
public class UserDocument {

	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String name;
	private String description;
	private String addressBook;
	private int documentNumber;
	private String documentType;
	private boolean status;
	private long size;
	private String fiscalType;
	private String type;
	private String uuid;
	private String folio;
	private String serie;
	private int fiscalRef;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date uploadDate;
	
	private boolean accept;
	
	@Lob @Basic(fetch = FetchType.LAZY)
	private byte[] content;
	
	private String replicationStatus;
	
	private String replicationMessage;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date replicationDate;
	  
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAddressBook() {
		return addressBook;
	}

	public void setAddressBook(String addressBook) {
		this.addressBook = addressBook;
	}

	public int getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(int documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getFiscalType() {
		return fiscalType;
	}

	public void setFiscalType(String fiscalType) {
		this.fiscalType = fiscalType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public int getFiscalRef() {
		return fiscalRef;
	}

	public void setFiscalRef(int fiscalRef) {
		this.fiscalRef = fiscalRef;
	}

	public String getReplicationStatus() {
		return replicationStatus;
	}

	public void setReplicationStatus(String replicationStatus) {
		this.replicationStatus = replicationStatus;
	}

	public String getReplicationMessage() {
		return replicationMessage;
	}

	public void setReplicationMessage(String replicationMessage) {
		this.replicationMessage = replicationMessage;
	}

	public Date getReplicationDate() {
		return replicationDate;
	}

	public void setReplicationDate(Date replicationDate) {
		this.replicationDate = replicationDate;
	}
	
}
