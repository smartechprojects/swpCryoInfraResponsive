package com.eurest.supplier.invoiceXml4;

import javax.xml.bind.annotation.XmlElement;

public class Addenda {
	
	RequestForPayment requestForPayment;

	public RequestForPayment getRequestForPayment() {
		return requestForPayment;
	}

	@XmlElement(name = "requestForPayment")
	public void setRequestForPayment(RequestForPayment requestForPayment) {
		this.requestForPayment = requestForPayment;
	}
	
	

}
