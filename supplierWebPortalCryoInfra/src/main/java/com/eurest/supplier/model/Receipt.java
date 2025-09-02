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
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@JsonAutoDetect
@Entity
@Table(name = "receipt")
public class Receipt implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String orderCompany; // Order company
	private int orderNumber; // Document
	private String orderType; // Order Type
	private String addressNumber;
	private int documentNumber;
	private String documentType;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date receiptDate;
	
	private int lineNumber;
	private String itemNumber;
	private double amountReceived;
	private double foreignAmountReceived;
	private double amountOpen;
	private double quantityReceived;
	private String uom;
	private String currencyCode;
	private String transactionOriginator;
	private String matchType;
	private String status;
	private String lineType;
	private String accountId;
	private double unitCost;
	private double exchangeRate;
	private double foreignUnitCost;
	
	private String uuid;
	private String folio;
	private String serie;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date invDate;
	
	private int receiptLine;	
	private Date estPmtDate;	
	private String remark;	
	private String paymentStatus;
	private Date paymentDate;
	private String paymentReference;
	private double paymentAmount;	
	private String complPagoUuid;
	private String creditNoteUuid;
	private String receiptType;
	private String objectAccount;
	private String taxCode;
	private String paymentTerms;
	
	private String usoCFDI;
	private String metodoPago;
	private String formaPago;
	
    private String replicationComplPagoStatus;
    private String actionReplication;
	  
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date replicationComplPagoDate;
	private String replicationComplPagoMessage;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date uploadComplDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date uploadDate;
	@Transient
	private String paymentType;
	
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
	public String getAddressNumber() {
		return addressNumber;
	}
	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
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
	public Date getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public double getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(double amountReceived) {
		this.amountReceived = amountReceived;
	}
	public double getAmountOpen() {
		return amountOpen;
	}
	public void setAmountOpen(double amountOpen) {
		this.amountOpen = amountOpen;
	}
	public double getQuantityReceived() {
		return quantityReceived;
	}
	public void setQuantityReceived(double quantityReceived) {
		this.quantityReceived = quantityReceived;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getTransactionOriginator() {
		return transactionOriginator;
	}
	public void setTransactionOriginator(String transactionOriginator) {
		this.transactionOriginator = transactionOriginator;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getMatchType() {
		return matchType;
	}
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Date getInvDate() {
		return invDate;
	}
	public void setInvDate(Date invDate) {
		this.invDate = invDate;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public double getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}
	public int getReceiptLine() {
		return receiptLine;
	}
	public void setReceiptLine(int receiptLine) {
		this.receiptLine = receiptLine;
	}
	public Date getEstPmtDate() {
		return estPmtDate;
	}
	public void setEstPmtDate(Date estPmtDate) {
		this.estPmtDate = estPmtDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public double getForeignAmountReceived() {
		return foreignAmountReceived;
	}
	public void setForeignAmountReceived(double foreignAmountReceived) {
		this.foreignAmountReceived = foreignAmountReceived;
	}
	public double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public double getForeignUnitCost() {
		return foreignUnitCost;
	}
	public void setForeignUnitCost(double foreignUnitCost) {
		this.foreignUnitCost = foreignUnitCost;
	}
	public String getComplPagoUuid() {
		return complPagoUuid;
	}
	public void setComplPagoUuid(String complPagoUuid) {
		this.complPagoUuid = complPagoUuid;
	}
	public String getCreditNoteUuid() {
		return creditNoteUuid;
	}
	public void setCreditNoteUuid(String creditNoteUuid) {
		this.creditNoteUuid = creditNoteUuid;
	}
	public String getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}
	public String getObjectAccount() {
		return objectAccount;
	}
	public void setObjectAccount(String objectAccount) {
		this.objectAccount = objectAccount;
	}
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public String getPaymentTerms() {
		return paymentTerms;
	}
	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}
	
	public String getUsoCFDI() {
		return usoCFDI;
	}
	public void setUsoCFDI(String usoCFDI) {
		this.usoCFDI = usoCFDI;
	}
	public String getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public String getReplicationComplPagoStatus() {
		return replicationComplPagoStatus;
	}
	public void setReplicationComplPagoStatus(String replicationComplPagoStatus) {
		this.replicationComplPagoStatus = replicationComplPagoStatus;
	}
	public String getActionReplication() {
		return actionReplication;
	}
	public void setActionReplication(String actionReplication) {
		this.actionReplication = actionReplication;
	}
	public Date getReplicationComplPagoDate() {
		return replicationComplPagoDate;
	}
	public void setReplicationComplPagoDate(Date replicationComplPagoDate) {
		this.replicationComplPagoDate = replicationComplPagoDate;
	}
	public String getReplicationComplPagoMessage() {
		return replicationComplPagoMessage;
	}
	public void setReplicationComplPagoMessage(String replicationComplPagoMessage) {
		this.replicationComplPagoMessage = replicationComplPagoMessage;
	}
	public Date getUploadComplDate() {
		return uploadComplDate;
	}
	public void setUploadComplDate(Date uploadComplDate) {
		this.uploadComplDate = uploadComplDate;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
}
