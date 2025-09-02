package com.eurest.supplier.invoiceXml4;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(namespace = "http://www.sat.gob.mx/cfd/4", name = "Comprobante")
@XmlType(propOrder = {"version", "serie", "folio", "fecha", "sello", "certificado", "noCertificado", "subTotal", "moneda", "total",
		"tipoDeComprobante", "formaPago", "metodoPago", "condicionesDePago", "descuento", "tipoCambio", "lugarExpedicion", 
		"informacionGlobal", "cfdiRelacionados", "emisor", "receptor", "conceptos", "impuestos", "complementos", "addenda"})
public class Comprobante {

    private String version;
    private String serie;
    private String folio;
    private String fecha;
    private String sello;
    private String certificado;
    private String noCertificado;
    private String condicionesDePago;
    private String subTotal;
    private String descuento;
    private String moneda;
    private String tipoCambio;
    private String total;
    private String tipoDeComprobante;
    private String exportacion;
    private String formaPago;
    private String metodoPago;
    private String lugarExpedicion;
    private String confirmacion;    
    private InformacionGlobal informacionGlobal;
    private CfdiRelacionados cfdiRelacionados;
    private Emisor emisor;
    private Receptor receptor;
    private Conceptos conceptos;
    private Impuestos impuestos;
    private List<Complemento> complementos;
    private Addenda addenda;
    
    public String getVersion() {
    	return version;
    }

    @XmlAttribute(name = "Version")
    public void setVersion(String version) {
        this.version = version;
    }

    public String getSerie() {
        return serie;
    }

    @XmlAttribute(name = "Serie")
    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getFolio() {
        return folio;
    }

    @XmlAttribute(name = "Folio")
    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getFecha() {
        return fecha;
    }

    @XmlAttribute(name = "Fecha")
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getSello() {
        return sello;
    }

    @XmlAttribute(name = "Sello")
    public void setSello(String sello) {
        this.sello = sello;
    }

    public String getFormaPago() {
        return formaPago;
    }

    @XmlAttribute(name = "FormaPago")
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getNoCertificado() {
        return noCertificado;
    }

    @XmlAttribute(name = "NoCertificado")
    public void setNoCertificado(String noCertificado) {
        this.noCertificado = noCertificado;
    }

    public String getCertificado() {
        return certificado;
    }

    @XmlAttribute(name = "Certificado")
    public void setCertificado(String certificado) {
        this.certificado = certificado;
    }

    public String getCondicionesDePago() {
        return condicionesDePago;
    }

    @XmlAttribute(name = "CondicionesDePago")
    public void setCondicionesDePago(String condicionesDePago) {
        this.condicionesDePago = condicionesDePago;
    }

    public String getSubTotal() {
        return subTotal;
    }

    @XmlAttribute(name = "SubTotal")
    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getDescuento() {
        return descuento;
    }

    @XmlAttribute(name = "Descuento")
    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getMoneda() {
        return moneda;
    }

    @XmlAttribute(name = "Moneda")
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getTipoCambio() {
        return tipoCambio;
    }

    @XmlAttribute(name = "TipoCambio")
    public void setTipoCambio(String tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public String getTotal() {
        return total;
    }

    @XmlAttribute(name = "Total")
    public void setTotal(String total) {
        this.total = total;
    }

    public String getTipoDeComprobante() {
        return tipoDeComprobante;
    }

    @XmlAttribute(name = "TipoDeComprobante")
    public void setTipoDeComprobante(String tipoDeComprobante) {
        this.tipoDeComprobante = tipoDeComprobante;
    }

    public String getExportacion() {
        return exportacion;
    }

    @XmlAttribute(name = "Exportacion")
    public void setExportacion(String exportacion) {
        this.exportacion = exportacion;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    @XmlAttribute(name = "MetodoPago")
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getLugarExpedicion() {
        return lugarExpedicion;
    }

    @XmlAttribute(name = "LugarExpedicion")
    public void setLugarExpedicion(String lugarExpedicion) {
        this.lugarExpedicion = lugarExpedicion;
    }

    public String getConfirmacion() {
        return confirmacion;
    }

    @XmlAttribute(name = "Confirmacion")
    public void setConfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }

    @XmlElement(name = "InformacionGlobal")
    public InformacionGlobal getInformacionGlobal() {
        return informacionGlobal;
    }
        
    public void setInformacionGlobal(InformacionGlobal informacionGlobal) {
        this.informacionGlobal = informacionGlobal;
    }
    
    @XmlElement(name = "CfdiRelacionados")
    public CfdiRelacionados getCfdiRelacionados() {
        return this.cfdiRelacionados;
    }

    public void setCfdiRelacionados(CfdiRelacionados cfdiRelacionados) {
        this.cfdiRelacionados = cfdiRelacionados;
    }
    
    @XmlElement(name = "Emisor")
    public Emisor getEmisor() {
        return emisor;
    }

    public void setEmisor(Emisor emisor) {
        this.emisor = emisor;
    }

    @XmlElement(name = "Receptor")
    public Receptor getReceptor() {
        return receptor;
    }

    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }

    @XmlElement(name = "Conceptos")
    public Conceptos getConceptos() {
        return conceptos;
    }

    public void setConceptos(Conceptos conceptos) {
        this.conceptos = conceptos;
    }

    @XmlElement(name = "Impuestos")
    public Impuestos getImpuestos() {
        return impuestos;
    }
 
    public void setImpuestos(Impuestos impuestos) {
        this.impuestos = impuestos;
    }
    
    @XmlElement(name = "Complemento", namespace="http://www.sat.gob.mx/cfd/4")
    public List<Complemento> getComplementos() {
		return complementos;
	}

	public void setComplementos(List<Complemento> complementos) {
		this.complementos = complementos;
	}

    @XmlElement(name = "Addenda")
    public Addenda getAddenda() {
        return addenda;
    }

    public void setAddenda(Addenda addenda) {
        this.addenda = addenda;
    }

	@Override
	public String toString() {
		return "Comprobante [version=" + version + ", serie=" + serie + ", folio=" + folio + ", fecha=" + fecha
				+ ", sello=" + sello + ", certificado=" + certificado + ", noCertificado=" + noCertificado
				+ ", condicionesDePago=" + condicionesDePago + ", subTotal=" + subTotal + ", descuento=" + descuento
				+ ", moneda=" + moneda + ", tipoCambio=" + tipoCambio + ", total=" + total + ", tipoDeComprobante="
				+ tipoDeComprobante + ", exportacion=" + exportacion + ", formaPago=" + formaPago + ", metodoPago="
				+ metodoPago + ", lugarExpedicion=" + lugarExpedicion + ", confirmacion=" + confirmacion
				+ ", informacionGlobal=" + informacionGlobal + ", cfdiRelacionados=" + cfdiRelacionados + ", emisor="
				+ emisor + ", receptor=" + receptor + ", conceptos=" + conceptos + ", impuestos=" + impuestos
				+ ", complementos=" + complementos + ", addenda=" + addenda + "]";
	}

    
}
