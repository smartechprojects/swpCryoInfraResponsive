package com.eurest.supplier.model;



import java.io.Serializable;
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
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.eurest.supplier.util.JsonDateSerializer;

@JsonAutoDetect
@Entity
@Table(name = "udc")
public class UDC implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "UDCSYSTEM", nullable = false, length = 20)
	private String udcSystem;
	@Column(name = "UDCKEY", nullable = false, length = 20)
	private String udcKey;
	@Column(name = "STRVALUE1", nullable = true, length = 100)
	private String strValue1;
	@Column(name = "STRVALUE2", nullable = true, length = 100)
	private String strValue2;
	@Column(name = "INTVALUE", nullable = true, length = 20)
	private int intValue;
	@Column(name = "NOTE", nullable = true, length = 250)
	private String note;
	@Column(name = "BOOLEANVALUE", nullable = true, columnDefinition = "TINYINT", length = 1)
	private boolean booleanValue;
	@Column(name = "DESCRIPTION", nullable = true, length = 250)
	private String description;

	@Column(name = "DATEVALUE", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date dateValue;
	@Column(name = "SYSTEMREF", nullable = true, length = 50)
	private String systemRef;
	@Column(name = "KEYREF", nullable = true, length = 100)
	private String keyRef;
	@Column(name = "CREATEDBY", nullable = true, length = 20)
	private String createdBy;
	@Column(name = "CREATIONDATE", nullable = true)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date creationDate;
	@Column(name = "UPDATEDBY", nullable = true, length = 20)
	private String updatedBy;
	@Column(name = "UPDATEDDATE", nullable = true)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date updatedDate;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUdcSystem() {
		return udcSystem;
	}

	public void setUdcSystem(String udcSystem) {
		this.udcSystem = udcSystem;
	}

	public String getUdcKey() {
		return udcKey;
	}

	public void setUdcKey(String udcKey) {
		this.udcKey = udcKey;
	}

	public String getStrValue1() {
		return strValue1;
	}

	public void setStrValue1(String strValue1) {
		this.strValue1 = strValue1;
	}

	public String getStrValue2() {
		return strValue2;
	}

	public void setStrValue2(String strValue2) {
		this.strValue2 = strValue2;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public String getSystemRef() {
		return systemRef;
	}

	public void setSystemRef(String systemRef) {
		this.systemRef = systemRef;
	}

	public String getKeyRef() {
		return keyRef;
	}

	public void setKeyRef(String keyRef) {
		this.keyRef = keyRef;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "UDC [id=" + id + ", udcSystem=" + udcSystem + ", udcKey=" + udcKey + ", strValue1=" + strValue1
				+ ", strValue2=" + strValue2 + ", intValue=" + intValue + ", note=" + note + ", booleanValue="
				+ booleanValue + ", description=" + description + ", dateValue=" + dateValue + ", systemRef="
				+ systemRef + ", keyRef=" + keyRef + ", createdBy=" + createdBy + ", creationDate=" + creationDate
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + "]";
	}
	
	
}
