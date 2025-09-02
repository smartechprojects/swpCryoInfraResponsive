package com.eurest.supplier.invoiceXml4;
import javax.xml.bind.annotation.XmlElement;

public class ImpuestosP {

    private TrasladosP trasladosP;
    private RetencionesP retencionesP;

    @XmlElement(name = "TrasladosP")
    public TrasladosP getTrasladosP() {
        return trasladosP;
    }

    public void setTrasladosP(TrasladosP trasladosP) {
        this.trasladosP = trasladosP;
    }
    @XmlElement(name = "RetencionesP")
    public RetencionesP getRetencionesP() {
        return retencionesP;
    }

    public void setRetencionesP(RetencionesP retencionesP) {
        this.retencionesP = retencionesP;
    }

}
