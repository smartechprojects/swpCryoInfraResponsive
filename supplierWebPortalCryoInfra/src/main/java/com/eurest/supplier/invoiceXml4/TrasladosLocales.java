package com.eurest.supplier.invoiceXml4;

import javax.xml.bind.annotation.XmlAttribute;

public class TrasladosLocales {

	private String impLocTraslado;
	private String tasadeTraslado;
	private String importe;
	
	
	
	public String getImpLocTraslado() {
		return impLocTraslado;
	}
	
	@XmlAttribute(name = "ImpLocTraslado")
	public void setImpLocTraslado(String impLocTraslado) {
		this.impLocTraslado = impLocTraslado;
	}
	public String getTasadeTraslado() {
		return tasadeTraslado;
	}
	
	@XmlAttribute(name = "TasadeTraslado")
	public void setTasadeTraslado(String tasadeTraslado) {
		this.tasadeTraslado = String.format("%.6f", Double.parseDouble(tasadeTraslado));
	}
	public String getImporte() {
		return importe;
	}
	
	@XmlAttribute(name = "Importe")
	public void setImporte(String importe) {
		this.importe = importe;
	}
	
}
