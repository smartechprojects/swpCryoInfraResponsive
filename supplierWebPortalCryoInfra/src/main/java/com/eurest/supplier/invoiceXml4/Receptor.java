package com.eurest.supplier.invoiceXml4;

import javax.xml.bind.annotation.XmlAttribute;

public class Receptor {

    private String rfc;
    private String nombre;
    private String usoCFDI;
    private String domicilioFiscalReceptor;
    private String regimenFiscalReceptor;

    public String getRfc() {
        return rfc;
    }

    @XmlAttribute(name="Rfc")
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNombre() {
        return nombre;
    }

    @XmlAttribute(name="Nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsoCFDI() {
        return usoCFDI;
    }

    @XmlAttribute(name="UsoCFDI")
    public void setUsoCFDI(String usoCFDI) {
        this.usoCFDI = usoCFDI;
    }

	public String getDomicilioFiscalReceptor() {
		return domicilioFiscalReceptor;
	}

	@XmlAttribute(name="DomicilioFiscalReceptor")
	public void setDomicilioFiscalReceptor(String domicilioFiscalReceptor) {
		this.domicilioFiscalReceptor = domicilioFiscalReceptor;
	}

	public String getRegimenFiscalReceptor() {
		return regimenFiscalReceptor;
	}

	@XmlAttribute(name="RegimenFiscalReceptor")
	public void setRegimenFiscalReceptor(String regimenFiscalReceptor) {
		this.regimenFiscalReceptor = regimenFiscalReceptor;
	}

}
