package com.eurest.supplier.invoiceXml;

import javax.xml.bind.annotation.XmlAttribute;

public class CfdiRelacionado {
    private String UUID;

    public String getCfdiUUID() {
        return UUID;
    }
    
    @XmlAttribute(name="UUID")
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
