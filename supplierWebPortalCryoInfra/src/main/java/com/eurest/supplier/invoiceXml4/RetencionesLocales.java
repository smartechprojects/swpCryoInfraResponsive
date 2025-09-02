package com.eurest.supplier.invoiceXml4;

import javax.xml.bind.annotation.XmlAttribute;

public class RetencionesLocales {
	
	private String impLocRetenido;
	private String tasadeRetencion;
	private String importe;
	
	
	
	public String getImpLocRetenido() {
		return impLocRetenido;
	}
	@XmlAttribute(name = "ImpLocRetenido")
	public void setImpLocRetenido(String impLocRetenido) {
		this.impLocRetenido = impLocRetenido;
	}
	public String getTasadeRetencion() {
		return tasadeRetencion;
	}
	@XmlAttribute(name = "TasadeRetencion")
	public void setTasadeRetencion(String tasadeRetencion) {
		this.tasadeRetencion = String.format("%.6f", Double.parseDouble(tasadeRetencion));
	}
	public String getImporte() {
		return importe;
	}
	@XmlAttribute(name = "Importe")
	public void setImporte(String importe) {
		this.importe = importe;
	}
	

	
}
