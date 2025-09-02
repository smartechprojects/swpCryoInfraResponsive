package com.eurest.supplier.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


@JsonAutoDetect
@Entity
@Table(name="fiscaldocuments")
public class FiscalDocuments {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String folio; 
	private String serie;
	private String uuidFactura;
	private String folioPago; 
	private String seriePago;
	private String uuidPago;
	private String folioNC; 
	private String serieNC;
	private String uuidNotaCredito;
	private String type;
	private String addressNumber;
	private String supplierName;
	private double amount; 
	private double subtotal;
	private double descuento;
	private double impuestos;
	private double tipoCambio;
	private String rfcEmisor; 
	private String rfcReceptor; 
	private String moneda;	
	private String centroCostos;
	private String conceptoArticulo;
	private String companyFD;	
	private String invoiceDate; 	
	private String status;
	private String noteRejected;	
	private String orderCompany; 
	private int orderNumber; 
	private String orderType;
	private String currencyMode;
	private String currencyCode;	
	private String paymentTerms;	
	private String accountingAccount;
	private String accountNumber;
	private String glOffset;
	private String taxCode;
	private String currentApprover;
	private String nextApprover;
	private String approvalStatus;
	private String approvalStep;
	private double advancePayment;
	private double conceptTotalAmount;
	private String paymentStatus;
	private Date paymentDate;
	private String paymentReference;
	private double paymentAmount;	
	private String complPagoUuid;
	private String semanaPago;
	private int documentNumber;
	private String responsibleUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date invoiceUploadDate;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date estimatedPaymentDate;
	
	@OneToMany(cascade=CascadeType.MERGE, fetch = FetchType.EAGER)
	Set<FiscalDocumentsConcept> concepts;
	
	 private String replicationStatus;
	  
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(iso = ISO.DATE_TIME)
	  private Date replicationDate;
	 
	 @DateTimeFormat(iso = ISO.DATE_TIME)
	  private Date dateAprov;
	    
	  private String replicationMessage;
	  private int toSend;
	  
	  private String plant;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getUuidFactura() {
		return uuidFactura;
	}
	public void setUuidFactura(String uuidFactura) {
		this.uuidFactura = uuidFactura;
	}
	public String getFolioPago() {
		return folioPago;
	}
	public void setFolioPago(String folioPago) {
		this.folioPago = folioPago;
	}
	public String getSeriePago() {
		return seriePago;
	}
	public void setSeriePago(String seriePago) {
		this.seriePago = seriePago;
	}
	public String getUuidPago() {
		return uuidPago;
	}
	public void setUuidPago(String uuidPago) {
		this.uuidPago = uuidPago;
	}
	public String getFolioNC() {
		return folioNC;
	}
	public void setFolioNC(String folioNC) {
		this.folioNC = folioNC;
	}
	public String getSerieNC() {
		return serieNC;
	}
	public void setSerieNC(String serieNC) {
		this.serieNC = serieNC;
	}
	public String getUuidNotaCredito() {
		return uuidNotaCredito;
	}
	public void setUuidNotaCredito(String uuidNotaCredito) {
		this.uuidNotaCredito = uuidNotaCredito;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAddressNumber() {
		return addressNumber;
	}
	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getCentroCostos() {
		return centroCostos;
	}
	public void setCentroCostos(String centroCostos) {
		this.centroCostos = centroCostos;
	}
	public String getConceptoArticulo() {
		return conceptoArticulo;
	}
	public void setConceptoArticulo(String conceptoArticulo) {
		this.conceptoArticulo = conceptoArticulo;
	}
	public String getCompanyFD() {
		return companyFD;
	}
	public void setCompanyFD(String companyFD) {
		this.companyFD = companyFD;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNoteRejected() {
		return noteRejected;
	}
	public void setNoteRejected(String noteRejected) {
		this.noteRejected = noteRejected;
	}
	public String getOrderCompany() {
		return orderCompany;
	}
	public void setOrderCompany(String orderCompany) {
		this.orderCompany = orderCompany;
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
	public String getPaymentTerms() {
		return paymentTerms;
	}
	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}
	public String getAccountingAccount() {
		return accountingAccount;
	}
	public void setAccountingAccount(String accountingAccount) {
		this.accountingAccount = accountingAccount;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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
	public String getCurrentApprover() {
		return currentApprover;
	}
	public void setCurrentApprover(String currentApprover) {
		this.currentApprover = currentApprover;
	}
	public String getNextApprover() {
		return nextApprover;
	}
	public void setNextApprover(String nextApprover) {
		this.nextApprover = nextApprover;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getApprovalStep() {
		return approvalStep;
	}
	public void setApprovalStep(String approvalStep) {
		this.approvalStep = approvalStep;
	}
	public double getAdvancePayment() {
		return advancePayment;
	}
	public void setAdvancePayment(double advancePayment) {
		this.advancePayment = advancePayment;
	}
	public double getConceptTotalAmount() {
		return conceptTotalAmount;
	}
	public void setConceptTotalAmount(double conceptTotalAmount) {
		this.conceptTotalAmount = conceptTotalAmount;
	}
	public Date getInvoiceUploadDate() {
		return invoiceUploadDate;
	}
	public void setInvoiceUploadDate(Date invoiceUploadDate) {
		this.invoiceUploadDate = invoiceUploadDate;
	}
	public Date getEstimatedPaymentDate() {
		return estimatedPaymentDate;
	}
	public void setEstimatedPaymentDate(Date estimatedPaymentDate) {
		this.estimatedPaymentDate = estimatedPaymentDate;
	}
	public Set<FiscalDocumentsConcept> getConcepts() {
		return concepts;
	}
	public void setConcepts(Set<FiscalDocumentsConcept> concepts) {
		this.concepts = concepts;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getPaymentReference() {
		return paymentReference;
	}
	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}
	public double getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getComplPagoUuid() {
		return complPagoUuid;
	}
	public void setComplPagoUuid(String complPagoUuid) {
		this.complPagoUuid = complPagoUuid;
	}
	public String getReplicationStatus() {
		return replicationStatus;
	}
	public void setReplicationStatus(String replicationStatus) {
		this.replicationStatus = replicationStatus;
	}
	public Date getReplicationDate() {
		return replicationDate;
	}
	public void setReplicationDate(Date replicationDate) {
		this.replicationDate = replicationDate;
	}
	public String getReplicationMessage() {
		return replicationMessage;
	}
	public void setReplicationMessage(String replicationMessage) {
		this.replicationMessage = replicationMessage;
	}
	public int getToSend() {
		return toSend;
	}
	public void setToSend(int toSend) {
		this.toSend = toSend;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getSemanaPago() {
		return semanaPago;
	}
	public void setSemanaPago(String semanaPago) {
		this.semanaPago = semanaPago;
	}
	public int getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(int documentNumber) {
		this.documentNumber = documentNumber;
	}
	public Date getDateAprov() {
		return dateAprov;
	}
	public void setDateAprov(Date dateAprov) {
		this.dateAprov = dateAprov;
	}
	public String getResponsibleUser() {
		return responsibleUser;
	}
	public void setResponsibleUser(String responsibleUser) {
		this.responsibleUser = responsibleUser;
	}
	
	
}
