package com.eurest.supplier.model;

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

import org.codehaus.jackson.annotate.JsonAutoDetect;


@JsonAutoDetect
@Entity
@Table(name = "purchaseorderpayment")
public class PurchaseOrderPayment {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private int paymentDocument; 
	private String documentCompany;
	private double paymentAmount;
	private String currency; 
	private String paymentBankAccount;
	private String bank;  
	private String date;
	private String status;
	private String addressNumber;
	private String supplierEmail;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL) 
	@JoinTable( name="PURCHASEORDERPAYMENT_PURCHASEORDERPAYMENTDETAILS", schema = "supplierdbuniversal")
	private Set<PurchaseOrderPaymentDetails> purchaseOrderPaymentDetails;

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

	public String getDocumentCompany() {
		return documentCompany;
	}

	public void setDocumentCompany(String documentCompany) {
		this.documentCompany = documentCompany;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPaymentBankAccount() {
		return paymentBankAccount;
	}

	public void setPaymentBankAccount(String paymentBankAccount) {
		this.paymentBankAccount = paymentBankAccount;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<PurchaseOrderPaymentDetails> getPurchaseOrderPaymentDetails() {
		return purchaseOrderPaymentDetails;
	}

	public void setPurchaseOrderPaymentDetails(Set<PurchaseOrderPaymentDetails> purchaseOrderPaymentDetails) {
		this.purchaseOrderPaymentDetails = purchaseOrderPaymentDetails;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAddressNumber() {
		return addressNumber;
	}

	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}
	
	

}
