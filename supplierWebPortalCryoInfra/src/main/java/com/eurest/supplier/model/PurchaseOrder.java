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
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


@JsonAutoDetect
@Entity
@Table(name = "purchaseorder")
public class PurchaseOrder {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String orderCompany; // Order company
	private int orderNumber; // Document
	private String orderType; // Order Type
	private String orderSuffix; // Order Suffix
	private String businessUnit;  // Business Unit
	private String documentCompany; // Document Company (original order)
	private String originalOrderNumber; // Original order number
	private String originalOrderType; // Original Order type
	private String companyKey; // Company key - related order
	private String addressNumber;  // Address Number
	private String shipTo; // Ship to
	
	private String shortCompanyName = "";
	private String longCompanyName ="";
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date dateRequested; // Date requested
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date promiseDelivery; // Original promise delivery
	
	private String deliveryInst1; // Delivery instructions 1
	private String deliveryInst2; // Delivery instructions 2
	private String remark; //  Remark
	private String description; // Description
	private String status; // Status
	private String currecyCode; // Currency code
	private double orderAmount; // Amount - order gross
	private double foreignAmount;
	private double exchangeRate;
	
	private double originalOrderAmount; // Amount - order gross
	private String relatedStatus; // Application related status
	private String notes;
	private String orderStauts;
	private double invoiceAmount;
	
	private String sentToWns;
	
	private String invoiceNumber;
	private String invoiceUuid;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date invoiceUploadDate; // Date requested
	
	private String paymentUuid;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date paymentUploadDate; // Date requested
	
	private String creditNotUuid;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date creditNoteUploadDate; // Date requested
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date transferDate; // Original promise delivery
	private String transferStatus;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL) 
	@JoinTable( name="purchaseorder_purchasorderdetail")
	private Set<PurchaseOrderDetail> purchaseOrderDetail;
	
	private String email;
	private String paymentType;
	private String supplierEmail;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date estimatedPaymentDate;

	
    @Column(length = 8000)
	private String headerNotes = "";
    
    private String fileName ="";
    
    @Column(length = 1000)
    private String rejectNotes ="";
    
	@Transient
	private byte[] fileContent;
	
	private double relievedAmount;
	
	private String currencyMode;
	
	private String paymentTerms;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date portalRecordDate;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getOrderSuffix() {
		return orderSuffix;
	}

	public void setOrderSuffix(String orderSuffix) {
		this.orderSuffix = orderSuffix;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getDocumentCompany() {
		return documentCompany;
	}

	public void setDocumentCompany(String documentCompany) {
		this.documentCompany = documentCompany;
	}

	public String getOriginalOrderNumber() {
		return originalOrderNumber;
	}

	public void setOriginalOrderNumber(String originalOrderNumber) {
		this.originalOrderNumber = originalOrderNumber;
	}

	public String getOriginalOrderType() {
		return originalOrderType;
	}

	public void setOriginalOrderType(String originalOrderType) {
		this.originalOrderType = originalOrderType;
	}

	public String getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}

	public String getAddressNumber() {
		return addressNumber;
	}

	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	public Date getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(Date dateRequested) {
		this.dateRequested = dateRequested;
	}

	public Date getPromiseDelivery() {
		return promiseDelivery;
	}

	public void setPromiseDelivery(Date promiseDelivery) {
		this.promiseDelivery = promiseDelivery;
	}

	public String getDeliveryInst1() {
		return deliveryInst1;
	}

	public void setDeliveryInst1(String deliveryInst1) {
		this.deliveryInst1 = deliveryInst1;
	}

	public String getDeliveryInst2() {
		return deliveryInst2;
	}

	public void setDeliveryInst2(String deliveryInst2) {
		this.deliveryInst2 = deliveryInst2;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrecyCode() {
		return currecyCode;
	}

	public void setCurrecyCode(String currecyCode) {
		this.currecyCode = currecyCode;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getRelatedStatus() {
		return relatedStatus;
	}

	public void setRelatedStatus(String relatedStatus) {
		this.relatedStatus = relatedStatus;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getOrderStauts() {
		return orderStauts;
	}

	public void setOrderStauts(String orderStauts) {
		this.orderStauts = orderStauts;
	}

	public String getInvoiceUuid() {
		return invoiceUuid;
	}

	public void setInvoiceUuid(String invoiceUuid) {
		this.invoiceUuid = invoiceUuid;
	}

	public Date getInvoiceUploadDate() {
		return invoiceUploadDate;
	}

	public void setInvoiceUploadDate(Date invoiceUploadDate) {
		this.invoiceUploadDate = invoiceUploadDate;
	}

	public String getPaymentUuid() {
		return paymentUuid;
	}

	public void setPaymentUuid(String paymentUuid) {
		this.paymentUuid = paymentUuid;
	}

	public Date getPaymentUploadDate() {
		return paymentUploadDate;
	}

	public void setPaymentUploadDate(Date paymentUploadDate) {
		this.paymentUploadDate = paymentUploadDate;
	}

	public String getCreditNotUuid() {
		return creditNotUuid;
	}

	public void setCreditNotUuid(String creditNotUuid) {
		this.creditNotUuid = creditNotUuid;
	}

	public Date getCreditNoteUploadDate() {
		return creditNoteUploadDate;
	}

	public void setCreditNoteUploadDate(Date creditNoteUploadDate) {
		this.creditNoteUploadDate = creditNoteUploadDate;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public Set<PurchaseOrderDetail> getPurchaseOrderDetail() {
		return purchaseOrderDetail;
	}

	public void setPurchasOrderDetail(Set<PurchaseOrderDetail> purchaseOrderDetail) {
		this.purchaseOrderDetail = purchaseOrderDetail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public void setPurchaseOrderDetail(Set<PurchaseOrderDetail> purchaseOrderDetail) {
		this.purchaseOrderDetail = purchaseOrderDetail;
	}

	public double getOriginalOrderAmount() {
		return originalOrderAmount;
	}

	public void setOriginalOrderAmount(double originalOrderAmount) {
		this.originalOrderAmount = originalOrderAmount;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}

	public String getShortCompanyName() {
		return shortCompanyName;
	}

	public void setShortCompanyName(String shortCompanyName) {
		this.shortCompanyName = shortCompanyName;
	}

	public String getLongCompanyName() {
		return longCompanyName;
	}

	public void setLongCompanyName(String longCompanyName) {
		this.longCompanyName = longCompanyName;
	}

	public double getForeignAmount() {
		return foreignAmount;
	}

	public void setForeignAmount(double foreignAmount) {
		this.foreignAmount = foreignAmount;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	
	public String getHeaderNotes() {
		return headerNotes;
	}

	public void setHeaderNotes(String headerNotes) {
		this.headerNotes = headerNotes;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRejectNotes() {
		return rejectNotes;
	}

	public void setRejectNotes(String rejectNotes) {
		this.rejectNotes = rejectNotes;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public Date getEstimatedPaymentDate() {
		return estimatedPaymentDate;
	}

	public void setEstimatedPaymentDate(Date estimatedPaymentDate) {
		this.estimatedPaymentDate = estimatedPaymentDate;
	}

	public double getRelievedAmount() {
		return relievedAmount;
	}

	public void setRelievedAmount(double relievedAmount) {
		this.relievedAmount = relievedAmount;
	}

	public String getSentToWns() {
		return sentToWns;
	}

	public void setSentToWns(String sentToWns) {
		this.sentToWns = sentToWns;
	}

	public String getCurrencyMode() {
		return currencyMode;
	}

	public void setCurrencyMode(String currencyMode) {
		this.currencyMode = currencyMode;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public Date getPortalRecordDate() {
		return portalRecordDate;
	}

	public void setPortalRecordDate(Date portalRecordDate) {
		this.portalRecordDate = portalRecordDate;
	}
	
}
