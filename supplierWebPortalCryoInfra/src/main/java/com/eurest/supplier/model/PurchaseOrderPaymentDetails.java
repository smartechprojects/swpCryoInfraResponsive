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
@Table(name = "purchaseorderpaymentdetails")
public class PurchaseOrderPaymentDetails {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private int paymentDocument; 
	private String orderCompany; // Order company
	private int orderNumber; // Document
	private String orderType; // Order Type
	private String addressNumber;  // Address Number
	
	private String paymentOrder;
	private String paymentOrderType;
	
	private int icu;
	private String bankReference;
	private String trading;
	private String transferDate;
	
	private String voucherNumber;
	private String voucherType;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPaymentDocument() {
		return paymentDocument;
	}
	public void setPaymentDocument(int paymentDocument) {
		this.paymentDocument = paymentDocument;
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
	public String getPaymentOrder() {
		return paymentOrder;
	}
	public void setPaymentOrder(String paymentOrder) {
		this.paymentOrder = paymentOrder;
	}
	public String getPaymentOrderType() {
		return paymentOrderType;
	}
	public void setPaymentOrderType(String paymentOrderType) {
		this.paymentOrderType = paymentOrderType;
	}
	public int getIcu() {
		return icu;
	}
	public void setIcu(int icu) {
		this.icu = icu;
	}
	public String getBankReference() {
		return bankReference;
	}
	public void setBankReference(String bankReference) {
		this.bankReference = bankReference;
	}
	public String getTrading() {
		return trading;
	}
	public void setTrading(String trading) {
		this.trading = trading;
	}
	public String getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	
	

}
