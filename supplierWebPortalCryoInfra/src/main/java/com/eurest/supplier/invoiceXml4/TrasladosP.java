package com.eurest.supplier.invoiceXml4;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class TrasladosP {

    private List<TrasladoP> trasladoP;
    
    @XmlElement(name = "TrasladoP")
    public List<TrasladoP> getTrasladoP() {
        return trasladoP;
    }

    public void setTrasladoP(List<TrasladoP> trasladoP) {
        this.trasladoP = trasladoP;
    }

}
