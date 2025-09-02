package com.eurest.supplier.invoiceXml4;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Retenciones {

	private List<Retencion> retencion;

	@XmlElement(name = "Retencion")
	public List<Retencion> getRetencion() {
		return retencion;
	}

	public void setRetencion(List<Retencion> retencion) {
		this.retencion = retencion;
	}

}
