package com.eurest.supplier.invoiceXml;

import javax.xml.bind.annotation.XmlAttribute;

public class Emisor {

    private String rfc;
    private String nombre;
    private String regimenFiscal;

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

    public String getRegimenFiscal() {
        return regimenFiscal;
    }

    @XmlAttribute(name="RegimenFiscal")
    public void setRegimenFiscal(String regimenFiscal) {
        this.regimenFiscal = regimenFiscal;
    }

}
