package com.eurest.supplier.invoiceXml4;

import javax.xml.bind.annotation.XmlElement;

public class Parte {

    private String claveProdServ;
    private String claveUnidad;
    private String noIdentificacion;
    private String cantidad;
    private String unidad;
    private String descripcion;
    private String valorUnitario;
    private String importe;
    private InformacionAduanera informacionAduanera;

    public String getClaveProdServ() {
        return claveProdServ;
    }

    public void setClaveProdServ(String claveProdServ) {
        this.claveProdServ = claveProdServ;
    }

    public String getClaveUnidad() {
        return claveUnidad;
    }

    public void setClaveUnidad(String claveUnidad) {
        this.claveUnidad = claveUnidad;
    }

    public String getNoIdentificacion() {
        return noIdentificacion;
    }

    public void setNoIdentificacion(String noIdentificacion) {
        this.noIdentificacion = noIdentificacion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(String valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }
    @XmlElement(name = "InformacionAduanera")
    public InformacionAduanera getInformacionAduanera() {
        return informacionAduanera;
    }

    public void setInformacionAduanera(InformacionAduanera informacionAduanera) {
        this.informacionAduanera = informacionAduanera;
    }

}
