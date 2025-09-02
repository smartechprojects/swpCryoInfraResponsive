package com.eurest.supplier.invoiceXml4;

import javax.xml.bind.annotation.XmlAttribute;

public class TrasladoP {

	private String baseP;
	private String impuestoP;
	private String tipoFactorP;
	private String tasaOCuotaP;
	private String importeP;

	public String getBaseP() {
		return baseP;
	}

	@XmlAttribute(name = "BaseP")
	public void setBaseP(String baseP) {
		this.baseP = baseP;
	}

	public String getImpuestoP() {
		return impuestoP;
	}

	@XmlAttribute(name = "ImpuestoP")
	public void setImpuestoP(String impuestoP) {
		this.impuestoP = impuestoP;
	}

	public String getTipoFactorP() {
		return tipoFactorP;
	}

	@XmlAttribute(name = "TipoFactorP")
	public void setTipoFactorP(String tipoFactorP) {
		this.tipoFactorP = tipoFactorP;
	}

	public String getTasaOCuotaP() {
		return tasaOCuotaP;
	}

	@XmlAttribute(name = "TasaOCuotaP")
	public void setTasaOCuotaP(String tasaOCuotaP) {
		this.tasaOCuotaP = tasaOCuotaP;
	}

	public String getImporteP() {
		return importeP;
	}

	@XmlAttribute(name = "ImporteP")
	public void setImporteP(String importeP) {
		this.importeP = importeP;
	}

}
