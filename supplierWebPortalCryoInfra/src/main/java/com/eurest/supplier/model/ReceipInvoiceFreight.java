package com.eurest.supplier.model;

import java.util.List;

public class ReceipInvoiceFreight {
	
	String semanaPago;
	List<InvoiceFreightRequest> bitacoras;
	
	
	public String getSemanaPago() {
		return semanaPago;
	}
	public void setSemanaPago(String semanaPago) {
		this.semanaPago = semanaPago;
	}
	public List<InvoiceFreightRequest> getBitacoras() {
		return bitacoras;
	}
	public void setBitacoras(List<InvoiceFreightRequest> bitacoras) {
		this.bitacoras = bitacoras;
	}
	
}
