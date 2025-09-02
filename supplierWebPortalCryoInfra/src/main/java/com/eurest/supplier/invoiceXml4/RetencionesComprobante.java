package com.eurest.supplier.invoiceXml4;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class RetencionesComprobante {

    private List<RetencionComprobante> retencion;

    @XmlElement(name = "Retencion")
    public List<RetencionComprobante> getRetencion() {
        return retencion;
    }

    public void setRetencion(List<RetencionComprobante> retencion) {
        this.retencion = retencion;
    }

}
