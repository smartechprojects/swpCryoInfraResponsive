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
@Table(name="paymentsupplierDetail")
public class PaymentSupplierDetail {

	
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private int idPayment;//	id de pago en jde
	private String typDocPas;//	rncdt ? tipo de documento pasivo
	private String docPasiveNumber;//rndoc= num doc pasivo
	private String invoiceNumber;		 //rpvinv=num factura
	private Double mountInvoice;//rnpaap=importe de factura
	
	
	
	@Override
	public String toString() {
		return "PaymentSupplierDetail [id=" + id + ", typDocPas=" + typDocPas + ", docPasiveNumber=" + docPasiveNumber
				+ ", invoiceNumber=" + invoiceNumber + ", mountInvoice=" + mountInvoice + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypDocPas() {
		return typDocPas;
	}
	public void setTypDocPas(String typDocPas) {
		this.typDocPas = typDocPas;
	}
	public String getDocPasiveNumber() {
		return docPasiveNumber;
	}
	public void setDocPasiveNumber(String docPasiveNumber) {
		this.docPasiveNumber = docPasiveNumber;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public Double getMountInvoice() {
		return mountInvoice;
	}
	public void setMountInvoice(Double mountInvoice) {
		this.mountInvoice = mountInvoice;
	}
	public int getIdPayment() {
		return idPayment;
	}
	public void setIdPayment(int idPayment) {
		this.idPayment = idPayment;
	}
	
	
	
	
	
	
	}
