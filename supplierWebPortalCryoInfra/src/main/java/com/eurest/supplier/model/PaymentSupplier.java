package com.eurest.supplier.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@JsonAutoDetect
@Entity
@Table(name="paymentsupplier")
public class PaymentSupplier {

	
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String addressBook;//RMPYE-numprov
	private String docCotejo;//RMDCTM-tipDocCotejo
	private String currencyCode;//RMCRCD, -- codigo de moneda
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date paymentDate; //--rmdmtj fecha del pago
	
	private double paymentAmount; //rmpaap, -- importe de pago
	
	@Column(length = 1024)
	private String invoiceNumber; //RNRMK,  -- numero de factura (concatenado)
	
	private String company;//RNCO,   -- compañia
	
	private int idPayment;//rmpyid, -- PaymentReference
	
	private String status;// rppst --estatus
	
	private String statusPay; ///RMVDGJ fecha de cancelado
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date uploadDate;

	private String suplierCompanyName;
	
	@Override
	public String toString() {
		return "PaymentSupplier [id=" + id + ", addressBook=" + addressBook + ", docCotejo=" + docCotejo
				+ ", currencyCode=" + currencyCode + ", paymentDate=" + paymentDate + ", paymentAmount=" + paymentAmount
				+ ", invoiceNumber=" + invoiceNumber + ", company=" + company + ", idPayment=" + idPayment + ", status="
				+ status + ", statusPay=" + statusPay + ", uploadDate=" + uploadDate + "]";
	}

	public int getId() {
		return id;
	}

	public String getSuplierCompanyName() {
		return suplierCompanyName;
	}

	public void setSuplierCompanyName(String suplierCompanyName) {
		this.suplierCompanyName = suplierCompanyName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddressBook() {
		return addressBook;
	}

	public void setAddressBook(String addressBook) {
		this.addressBook = addressBook;
	}

	public String getDocCotejo() {
		return docCotejo;
	}

	public void setDocCotejo(String docCotejo) {
		this.docCotejo = docCotejo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getIdPayment() {
		return idPayment;
	}

	public void setIdPayment(int idPayment) {
		this.idPayment = idPayment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getStatusPay() {
		return statusPay;
	}

	public void setStatusPay(String statusPay) {
		this.statusPay = statusPay;
	}
	
	}
