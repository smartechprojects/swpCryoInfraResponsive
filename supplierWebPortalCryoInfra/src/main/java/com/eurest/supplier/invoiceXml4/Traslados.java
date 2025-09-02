package com.eurest.supplier.invoiceXml4;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Traslados {

    private List<Traslado> traslado;
    
    @XmlElement(name = "Traslado")
    public List<Traslado> getTraslado() {
        return traslado;
    }

    public void setTraslado(List<Traslado> traslado) {
        this.traslado = traslado;
    }

}
