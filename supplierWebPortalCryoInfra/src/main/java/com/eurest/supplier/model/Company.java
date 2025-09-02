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

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonAutoDetect
@Entity
@Table(name = "company")
public class Company implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String company;
	private String companyName;
	private String taxFileRef;
	private String logoFileRef;
	private String notificationEmail;
	private String status;
	private String secretPass;
	private String attachId;
	private boolean active;
	
	private String sitePrefix;

	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "America/Mexico_City")
	private Date creationDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTaxFileRef() {
		return taxFileRef;
	}

	public void setTaxFileRef(String taxFileRef) {
		this.taxFileRef = taxFileRef;
	}

	public String getLogoFileRef() {
		return logoFileRef;
	}

	public void setLogoFileRef(String logoFileRef) {
		this.logoFileRef = logoFileRef;
	}

	public String getNotificationEmail() {
		return notificationEmail;
	}

	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getSitePrefix() {
		return sitePrefix;
	}

	public void setSitePrefix(String sitePrefix) {
		this.sitePrefix = sitePrefix;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getSecretPass() {
		return secretPass;
	}

	public void setSecretPass(String secretPass) {
		this.secretPass = secretPass;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

}
