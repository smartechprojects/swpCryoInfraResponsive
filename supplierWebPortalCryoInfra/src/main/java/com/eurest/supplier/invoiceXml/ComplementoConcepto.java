package com.eurest.supplier.invoiceXml;

import javax.xml.bind.annotation.XmlElement;

public class ComplementoConcepto {

	private InstEducativas instEducativas;

	@XmlElement(name = "instEducativas", namespace="http://www.sat.gob.mx/iedu")
	public InstEducativas getInstEducativas() {
		return instEducativas;
	}

	public void setInstEducativas(InstEducativas instEducativas) {
		this.instEducativas = instEducativas;
	}

}
