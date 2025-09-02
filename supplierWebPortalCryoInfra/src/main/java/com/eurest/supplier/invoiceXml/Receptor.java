package com.eurest.supplier.invoiceXml;

import javax.xml.bind.annotation.XmlAttribute;

public class Receptor {

    private String rfc;
    private String nombre;
    private String usoCFDI;

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

}
