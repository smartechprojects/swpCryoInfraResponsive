package com.eurest.supplier.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "foreigninvoicetable")
public class ForeignInvoiceTable implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
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
	
	private String uuid;
	private String taxId;
	
	private String invoiceNumber;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddressNumber() {
		return addressNumber;
	}
	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getExpeditionDate() {
		return expeditionDate;
	}
	public void setExpeditionDate(String expeditionDate) {
		this.expeditionDate = expeditionDate;
	}
	public String getReceptCompany() {
		return receptCompany;
	}
	public void setReceptCompany(String receptCompany) {
		this.receptCompany = receptCompany;
	}
	public String getForeignCurrency() {
		return foreignCurrency;
	}
	public void setForeignCurrency(String foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}
	public double getForeignSubtotal() {
		return foreignSubtotal;
	}
	public void setForeignSubtotal(double foreignSubtotal) {
		this.foreignSubtotal = foreignSubtotal;
	}
	public double getForeignTaxes() {
		return foreignTaxes;
	}
	public void setForeignTaxes(double foreignTaxes) {
		this.foreignTaxes = foreignTaxes;
	}
	public double getForeignRetention() {
		return foreignRetention;
	}
	public void setForeignRetention(double foreignRetention) {
		this.foreignRetention = foreignRetention;
	}
	public double getForeignDebit() {
		return foreignDebit;
	}
	public void setForeignDebit(double foreignDebit) {
		this.foreignDebit = foreignDebit;
	}
	public String getForeignDescription() {
		return foreignDescription;
	}
	public void setForeignDescription(String foreignDescription) {
		this.foreignDescription = foreignDescription;
	}
	public String getForeignNotes() {
		return foreignNotes;
	}
	public void setForeignNotes(String foreignNotes) {
		this.foreignNotes = foreignNotes;
	}
	public String getUsuarioImpuestos() {
		return usuarioImpuestos;
	}
	public void setUsuarioImpuestos(String usuarioImpuestos) {
		this.usuarioImpuestos = usuarioImpuestos;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
}
