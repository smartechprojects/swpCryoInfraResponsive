package com.eurest.supplier.invoiceXml4;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class RetencionesP {

	private List<RetencionP> retencionP;

	@XmlElement(name = "RetencionP")
	public List<RetencionP> getRetencionP() {
		return retencionP;
	}

	public void setRetencionP(List<RetencionP> retencionP) {
		this.retencionP = retencionP;
	}

}
