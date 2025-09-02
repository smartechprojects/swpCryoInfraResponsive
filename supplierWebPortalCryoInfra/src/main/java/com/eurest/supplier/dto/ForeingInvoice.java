package com.eurest.supplier.dto;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement  
public class ForeingInvoice {
	
	private String addressNumber;
	private int orderNumber;
	private String orderType;
	
	private String country;
	private String expeditionDate;
	private String receptCompany;
	private String foreignCurrency;
	private double foreignSubtotal;
	private double foreignTaxes;
	private double foreignRetention;
	private double foreignDebit;
	private String foreignDescription;
	private String foreignNotes;
	private String usuarioImpuestos;
	private String invoiceNumber;
	
	private String uuid;
	private String taxId;
	private String Serie;
	private String Folio;

	private String receiptIdList;
	private String plantCode;
	
	@XmlAttribute 
	public String getAddressNumber() {
		return addressNumber;
	}
	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}
	
	@XmlAttribute 
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	@XmlAttribute 
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@XmlAttribute 
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	@XmlAttribute 
	public String getExpeditionDate() {
		return expeditionDate;
	}
	public void setExpeditionDate(String expeditionDate) {
		this.expeditionDate = expeditionDate;
	}
	
	@XmlAttribute 
	public String getReceptCompany() {
		return receptCompany;
	}
	public void setReceptCompany(String receptCompany) {
		this.receptCompany = receptCompany;
	}
	
	@XmlAttribute 
	public String getForeignCurrency() {
		return foreignCurrency;
	}
	public void setForeignCurrency(String foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}
	
	@XmlAttribute 
	public double getForeignSubtotal() {
		return foreignSubtotal;
	}
	public void setForeignSubtotal(double foreignSubtotal) {
		this.foreignSubtotal = foreignSubtotal;
	}
	
	@XmlAttribute 
	public double getForeignTaxes() {
		return foreignTaxes;
	}
	public void setForeignTaxes(double foreignTaxes) {
		this.foreignTaxes = foreignTaxes;
	}
	
	@XmlAttribute 
	public double getForeignRetention() {
		return foreignRetention;
	}
	public void setForeignRetention(double foreignRetention) {
		this.foreignRetention = foreignRetention;
	}
	
	@XmlAttribute 
	public double getForeignDebit() {
		return foreignDebit;
	}
	public void setForeignDebit(double foreignDebit) {
		this.foreignDebit = foreignDebit;
	}
	
	@XmlAttribute 
	public String getForeignDescription() {
		return foreignDescription;
	}
	public void setForeignDescription(String foreignDescription) {
		this.foreignDescription = foreignDescription;
	}
	
	@XmlAttribute 
	public String getForeignNotes() {
		return foreignNotes;
	}
	public void setForeignNotes(String foreignNotes) {
		this.foreignNotes = foreignNotes;
	}
	
	@XmlAttribute 
	public String getUsuarioImpuestos() {
		return usuarioImpuestos;
	}
	public void setUsuarioImpuestos(String usuarioImpuestos) {
		this.usuarioImpuestos = usuarioImpuestos;
	}
	
	@XmlAttribute 
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@XmlAttribute 
	public String getTaxId() {
		return taxId;
	}
		
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	
	@XmlAttribute
	public String getSerie() {
		return Serie;
	}
	
	public void setSerie(String serie) {
		Serie = serie;
	}
	
	@XmlAttribute
	public String getFolio() {
		return Folio;
	}
	
	public void setFolio(String folio) {
		Folio = folio;
	}
	
	@XmlAttribute 
	public String getReceiptIdList() {
		return receiptIdList;
	}
	
	public void setReceiptIdList(String receiptIdList) {
		this.receiptIdList = receiptIdList;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getPlantCode() {
		return plantCode;
	}
	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	
	

}
