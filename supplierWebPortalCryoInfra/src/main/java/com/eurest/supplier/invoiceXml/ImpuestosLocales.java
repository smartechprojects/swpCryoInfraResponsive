package com.eurest.supplier.invoiceXml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ImpuestosLocales {
	
	private String totaldeRetenciones;
	private String totaldeTraslados;
	private RetencionesLocales retencionesLocales;
	private TrasladosLocales trasladosLocales;
	
	
	
	public String getTotaldeRetenciones() {
		return totaldeRetenciones;
	}
	
	@XmlAttribute(name = "TotaldeRetenciones")
	public void setTotaldeRetenciones(String totaldeRetenciones) {
		this.totaldeRetenciones = totaldeRetenciones;
	}
	public String getTotaldeTraslados() {
		return totaldeTraslados;
	}
	@XmlAttribute(name = "TotaldeTraslados")
	public void setTotaldeTraslados(String totaldeTraslados) {
		this.totaldeTraslados = totaldeTraslados;
	}
	@XmlElement(name = "RetencionesLocales", namespace = "http://www.sat.gob.mx/implocal")
	public RetencionesLocales getRetencionesLocales() {
		return retencionesLocales;
	}
	public void setRetencionesLocales(RetencionesLocales retencionesLocales) {
		this.retencionesLocales = retencionesLocales;
	}
	
	@XmlElement(name = "TrasladosLocales", namespace = "http://www.sat.gob.mx/implocal")
	public TrasladosLocales getTrasladosLocales() {
		return trasladosLocales;
	}
	public void setTrasladosLocales(TrasladosLocales trasladosLocales) {
		this.trasladosLocales = trasladosLocales;
	}
	



}