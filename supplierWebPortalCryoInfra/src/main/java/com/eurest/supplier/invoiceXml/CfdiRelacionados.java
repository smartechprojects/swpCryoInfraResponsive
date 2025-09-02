package com.eurest.supplier.invoiceXml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class CfdiRelacionados {

    private String tipoRelacion;
    private CfdiRelacionado cfdiRelacionado;

    public String getTipoRelacion() {
        return tipoRelacion;
    }
    
    @XmlAttribute(name="tipoRelacion")
    public void setTipoRelacion(String tipoRelacion) {
        this.tipoRelacion = tipoRelacion;
    }
    
	@XmlElement(name = "CfdiRelacionado")
    public CfdiRelacionado getCfdiRelacionado() {
        return cfdiRelacionado;
    }
    
    public void setCfdiRelacionado(CfdiRelacionado cfdiRelacionado) {
        this.cfdiRelacionado = cfdiRelacionado;
    }
}
