package com.eurest.supplier.invoiceXml4;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class TrasladosComprobante {

    private List<TrasladoComprobante> traslado;

    @XmlElement(name = "Traslado")
    public List<TrasladoComprobante> getTraslado() {
        return traslado;
    }

    public void setTraslado(List<TrasladoComprobante> traslado) {
        this.traslado = traslado;
    }

}
