package com.eurest.supplier.invoiceXml4;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ImpuestosComprobante {

	private String totalImpuestosRetenidos;
	private String totalImpuestosTrasladados;
	private RetencionesComprobante retenciones;
	private TrasladosComprobante traslados;

	@XmlElement(name = "Traslados")
	public TrasladosComprobante getTraslados() {
		return traslados;
	}

	public void setTraslados(TrasladosComprobante traslados) {
		this.traslados = traslados;
	}

	public String getTotalImpuestosRetenidos() {
		return totalImpuestosRetenidos;
	}

	@XmlAttribute(name="TotalImpuestosRetenidos")
	public void setTotalImpuestosRetenidos(String totalImpuestosRetenidos) {
		this.totalImpuestosRetenidos = totalImpuestosRetenidos;
	}

	public String getTotalImpuestosTrasladados() {
		return totalImpuestosTrasladados;
	}

	@XmlAttribute(name="TotalImpuestosTrasladados")
	public void setTotalImpuestosTrasladados(String totalImpuestosTrasladados) {
		this.totalImpuestosTrasladados = totalImpuestosTrasladados;
	}

	@XmlElement(name = "Retenciones")
	public RetencionesComprobante getRetenciones() {
		return retenciones;
	}

	public void setRetenciones(RetencionesComprobante retenciones) {
		this.retenciones = retenciones;
	}

}
