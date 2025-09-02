package com.eurest.supplier.invoiceXml4;

import javax.xml.bind.annotation.XmlAttribute;

public class InformacionGlobal {
   
    private String periodicidad;    
    private String meses;
    private String año;

    public String getPeriodicidad() {
        return periodicidad;
    }

    @XmlAttribute(name = "periodicidad", required = true)
    public void setPeriodicidad(String value) {
        this.periodicidad = value;
    }

    public String getMeses() {
        return meses;
    }

    @XmlAttribute(name = "meses", required = true)
    public void setMeses(String value) {
        this.meses = value;
    }

    public String getAño() {
        return año;
    }

    @XmlAttribute(name = "a\u00f1o", required = true)
    public void setAño(String año) {
        this.año = año;
    }

}
