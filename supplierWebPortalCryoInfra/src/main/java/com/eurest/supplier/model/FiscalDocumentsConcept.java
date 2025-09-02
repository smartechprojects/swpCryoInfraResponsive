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
@Table(name="fiscaldocumentsconcept")
public class FiscalDocumentsConcept {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String code;
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
	private double unitPrice;
	private double quantity;
	private String rfcEmisor; 
	private String uom;
	private String rfcReceptor;
	private String invoiceDate;	
	private String currencyMode;
	private String currencyCode;	
	private String accountingAccount;	
	private String glOffset;
	private String taxCode;
	private String status;
	private boolean isDocumentType;
	
	private String batchID;
	private String ctaPresupuestal;
	private String rfc;
	private String serieBitacora;
	private String numBitacora;
	private String serieFactura;
	private String folioFactura;
	
	private String uuidFactura;
	private String type;
	private String moneda;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date estimatedPaymentDate;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date invoiceUploadDate;
	
	private String orderType;
	
	private double cantidad;
	private double iva;
	private double retIva;
	private double precioUnitario;
	
	
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
	public String getBatchID() {
		return batchID;
	}
	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}
	public String getCtaPresupuestal() {
		return ctaPresupuestal;
	}
	public void setCtaPresupuestal(String ctaPresupuestal) {
		this.ctaPresupuestal = ctaPresupuestal;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getSerieBitacora() {
		return serieBitacora;
	}
	public void setSerieBitacora(String serieBitacora) {
		this.serieBitacora = serieBitacora;
	}
	public String getNumBitacora() {
		return numBitacora;
	}
	public void setNumBitacora(String numBitacora) {
		this.numBitacora = numBitacora;
	}
	public String getSerieFactura() {
		return serieFactura;
	}
	public void setSerieFactura(String serieFactura) {
		this.serieFactura = serieFactura;
	}
	public String getFolioFactura() {
		return folioFactura;
	}
	public void setFolioFactura(String folioFactura) {
		this.folioFactura = folioFactura;
	}
	public String getUuidFactura() {
		return uuidFactura;
	}
	public void setUuidFactura(String uuidFactura) {
		this.uuidFactura = uuidFactura;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public Date getEstimatedPaymentDate() {
		return estimatedPaymentDate;
	}
	public void setEstimatedPaymentDate(Date estimatedPaymentDate) {
		this.estimatedPaymentDate = estimatedPaymentDate;
	}
	public Date getInvoiceUploadDate() {
		return invoiceUploadDate;
	}
	public void setInvoiceUploadDate(Date invoiceUploadDate) {
		this.invoiceUploadDate = invoiceUploadDate;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public double getIva() {
		return iva;
	}
	public void setIva(double iva) {
		this.iva = iva;
	}
	public double getRetIva() {
		return retIva;
	}
	public void setRetIva(double retIva) {
		this.retIva = retIva;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public double getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}	
	
}
