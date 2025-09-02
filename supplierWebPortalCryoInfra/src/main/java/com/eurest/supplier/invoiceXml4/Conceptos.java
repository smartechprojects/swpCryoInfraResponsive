package com.eurest.supplier.invoiceXml4;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Conceptos {

	private List<Concepto> concepto;

	@XmlElement(name = "Concepto")
	public List<Concepto> getConcepto() {
		return concepto;
	}

	public void setConcepto(List<Concepto> concepto) {
		this.concepto = concepto;
	}

}
