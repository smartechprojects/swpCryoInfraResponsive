package com.eurest.supplier.invoiceXml4;

import javax.xml.bind.annotation.XmlAttribute;

public class Traslado {

	private String base;
	private String impuesto;
	private String tipoFactor;
	private String tasaOCuota;
	private String importe;

	public String getBase() {
		return base;
	}

	@XmlAttribute(name = "Base")
	public void setBase(String base) {
		this.base = base;
	}

	public String getImpuesto() {
		return impuesto;
	}

	@XmlAttribute(name = "Impuesto")
	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}

	public String getTipoFactor() {
		return tipoFactor;
	}

	@XmlAttribute(name = "TipoFactor")
	public void setTipoFactor(String tipoFactor) {
		this.tipoFactor = tipoFactor;
	}

	public String getTasaOCuota() {
		return tasaOCuota;
	}

	@XmlAttribute(name = "TasaOCuota")
	public void setTasaOCuota(String tasaOCuota) {
		this.tasaOCuota = tasaOCuota;
	}

	public String getImporte() {
		return importe;
	}

	@XmlAttribute(name = "Importe")
	public void setImporte(String importe) {
		this.importe = importe;
	}

}
