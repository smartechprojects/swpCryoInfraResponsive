package com.eurest.supplier.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name="customBrokerConcept")
public class CustomBrokerConcept {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String conceptName;
	private String conceptAccount;
	private String folio;
	private String serie;
	private String uuid;
	private String addressNumber; 
	private double amount; 
	private double subtotal;
	private double descuento;
	private double impuestos;
	private double tipoCambio;
	private String rfcEmisor; 
	private String rfcReceptor;
	private String invoiceDate;	
	private String currencyMode;
	private String currencyCode;	
	private String accountingAccount;	
	private String glOffset;
	private String taxCode;
	private String status;
	private boolean isDocumentType;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getConceptName() {
		return conceptName;
	}
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	public String getConceptAccount() {
		return conceptAccount;
	}
	public void setConceptAccount(String conceptAccount) {
		this.conceptAccount = conceptAccount;
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
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getAddressNumber() {
		return addressNumber;
	}
	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public double getDescuento() {
		return descuento;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}
	public double getImpuestos() {
		return impuestos;
	}
	public void setImpuestos(double impuestos) {
		this.impuestos = impuestos;
	}
	public double getTipoCambio() {
		return tipoCambio;
	}
	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
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
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getCurrencyMode() {
		return currencyMode;
	}
	public void setCurrencyMode(String currencyMode) {
		this.currencyMode = currencyMode;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getAccountingAccount() {
		return accountingAccount;
	}
	public void setAccountingAccount(String accountingAccount) {
		this.accountingAccount = accountingAccount;
	}
	public String getGlOffset() {
		return glOffset;
	}
	public void setGlOffset(String glOffset) {
		this.glOffset = glOffset;
	}
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isDocumentType() {
		return isDocumentType;
	}
	public void setDocumentType(boolean isDocumentType) {
		this.isDocumentType = isDocumentType;
	}
}
