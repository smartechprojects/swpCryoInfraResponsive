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
@Table(name = "purchaseorderdetail")
public class PurchaseOrderDetail {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String orderCompany; // Order company
	private int orderNumber; // Document
	private String orderType; // Order Type
	
	private String itemNumber;
	private String altItemNumber;
	private String itemDescription;
	private String codigoSat;
	private float lineNumber; 
	private String lotNumber;  
	private String lineStatus;
	private double quantity;  
	private String uom;
	private double unitCost;
	private double extendedPrice;
	private String status;  
	private String location;
	private String lineType; 
	private double received; 
	private double pending; 
	
	private double rejected;
	private double amount;
	private double amuntReceived;
	private double openAmount;
	
	private double foreignAmount;
	private double exchangeRate;
	private String currencyCode;
	
	private String glOffSet;
	private String currency;
	private String reason; 
	private String notes; 
	private String accountId;
	private double toReceive; 
	private double toReject;
	private String receiptType;
	private String objectAccount;
	
	@Transient
	private Tolerances tolerances;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date receptionDate; // Units rejected
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date lotExpirationDate; // Units rejected
	
	private String receptionBy;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date transferDate; // Original promise delivery
	
	private String transferStatus;
	private String taxCode;
	private String taxable;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL) 
	@JoinTable( name="purchaseorderdetail_purchasordernotes")
	private Set<PurchaseOrderNotes> purchaseOrderNotes;
	
	private String numberLineReceipt;
	private double receivedQuantity;
	private double amuntReceivedCN;
	private String uuid;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date estimatedPaymentDate;
	


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

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getAltItemNumber() {
		return altItemNumber;
	}

	public void setAltItemNumber(String altItemNumber) {
		this.altItemNumber = altItemNumber;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getCodigoSat() {
		return codigoSat;
	}

	public void setCodigoSat(String codigoSat) {
		this.codigoSat = codigoSat;
	}

	public float getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(float lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
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

	public double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	public double getExtendedPrice() {
		return extendedPrice;
	}

	public void setExtendedPrice(double extendedPrice) {
		this.extendedPrice = extendedPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public double getReceived() {
		return received;
	}

	public void setReceived(double received) {
		this.received = received;
	}

	public double getPending() {
		return pending;
	}

	public void setOpen(double pending) {
		this.pending = pending;
	}

	public double getRejected() {
		return rejected;
	}

	public void setRejected(double rejected) {
		this.rejected = rejected;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmuntReceived() {
		return amuntReceived;
	}

	public void setAmuntReceived(double amuntReceived) {
		this.amuntReceived = amuntReceived;
	}

	public double getOpenAmount() {
		return openAmount;
	}

	public void setOpenAmount(double openAmount) {
		this.openAmount = openAmount;
	}

	public String getGlOffSet() {
		return glOffSet;
	}

	public void setGlOffSet(String glOffSet) {
		this.glOffSet = glOffSet;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Date getReceptionDate() {
		return receptionDate;
	}

	public void setReceptionDate(Date receptionDate) {
		this.receptionDate = receptionDate;
	}

	public Date getLotExpirationDate() {
		return lotExpirationDate;
	}

	public void setLotExpirationDate(Date lotExpirationDate) {
		this.lotExpirationDate = lotExpirationDate;
	}

	public String getReceptionBy() {
		return receptionBy;
	}

	public void setReceptionBy(String receptionBy) {
		this.receptionBy = receptionBy;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public double getToReceive() {
		return toReceive;
	}

	public void setToReceive(double toReceive) {
		this.toReceive = toReceive;
	}

	public double getToReject() {
		return toReject;
	}

	public void setToReject(double toReject) {
		this.toReject = toReject;
	}

	public Tolerances getTolerances() {
		return tolerances;
	}

	public void setTolerances(Tolerances tolerances) {
		this.tolerances = tolerances;
	}

	public String getLineStatus() {
		return lineStatus;
	}

	public void setLineStatus(String lineStatus) {
		this.lineStatus = lineStatus;
	}

	public Set<PurchaseOrderNotes> getPurchaseOrderNotes() {
		return purchaseOrderNotes;
	}

	public void setPurchaseOrderNotes(Set<PurchaseOrderNotes> purchaseOrderNotes) {
		this.purchaseOrderNotes = purchaseOrderNotes;
	}

	public void setPending(double pending) {
		this.pending = pending;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getTaxable() {
		return taxable;
	}

	public void setTaxable(String taxable) {
		this.taxable = taxable;
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

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
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

	public String getNumberLineReceipt() {
		return numberLineReceipt;
	}

	public void setNumberLineReceipt(String numberLineReceipt) {
		this.numberLineReceipt = numberLineReceipt;
	}

	public double getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(double receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}

	public double getAmuntReceivedCN() {
		return amuntReceivedCN;
	}

	public void setAmuntReceivedCN(double amuntReceivedCN) {
		this.amuntReceivedCN = amuntReceivedCN;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getEstimatedPaymentDate() {
		return estimatedPaymentDate;
	}

	public void setEstimatedPaymentDate(Date estimatedPaymentDate) {
		this.estimatedPaymentDate = estimatedPaymentDate;
	}
	
}
