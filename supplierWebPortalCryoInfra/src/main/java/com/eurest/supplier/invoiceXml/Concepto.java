package com.eurest.supplier.invoiceXml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {
		"claveProdServ", "cantidad", "unidad", "claveUnidad", "noIdentificacion", "descripcion", "valorUnitario", "importe", 
		"descuento", "impuestos", "complementoConcepto", "cuentaPredial", "informacionAduanera", "parte" })
public class Concepto {

	private String claveProdServ;
	private String claveUnidad;
	private String noIdentificacion;
	private String cantidad;
	private String unidad;
	private String descripcion;
	private String valorUnitario;
	private String importe;
	private String descuento;

	private Impuestos impuestos;
	private ComplementoConcepto complementoConcepto;
	private CuentaPredial cuentaPredial;
	private InformacionAduanera informacionAduanera;
	private Parte parte;

	public String getClaveProdServ() {
		return claveProdServ;
	}

	@XmlAttribute(name = "ClaveProdServ")
	public void setClaveProdServ(String claveProdServ) {
		this.claveProdServ = claveProdServ;
	}

	public String getClaveUnidad() {
		return claveUnidad;
	}

	@XmlAttribute(name = "ClaveUnidad")
	public void setClaveUnidad(String claveUnidad) {
		this.claveUnidad = claveUnidad;
	}

	public String getNoIdentificacion() {
		return noIdentificacion;
	}

	@XmlAttribute(name = "NoIdentificacion")
	public void setNoIdentificacion(String noIdentificacion) {
		this.noIdentificacion = noIdentificacion;
	}

	public String getCantidad() {
		return cantidad;
	}

	@XmlAttribute(name = "Cantidad")
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getUnidad() {
		return unidad;
	}

	@XmlAttribute(name = "Unidad")
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	@XmlAttribute(name = "Descripcion")
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getValorUnitario() {
		return valorUnitario;
	}

	@XmlAttribute(name = "ValorUnitario")
	public void setValorUnitario(String valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public String getImporte() {
		return importe;
	}

	@XmlAttribute(name = "Importe")
	public void setImporte(String importe) {
		this.importe = importe;
	}

	public String getDescuento() {
		return descuento;
	}

	@XmlAttribute(name = "Descuento")
	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	@XmlElement(name = "Impuestos")
	public Impuestos getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(Impuestos impuestos) {
		this.impuestos = impuestos;
	}

	@XmlElement(name = "CuentaPredial")
	public CuentaPredial getCuentaPredial() {
		return cuentaPredial;
	}

	public void setCuentaPredial(CuentaPredial cuentaPredial) {
		this.cuentaPredial = cuentaPredial;
	}

	@XmlElement(name = "InformacionAduanera")
	public InformacionAduanera getInformacionAduanera() {
		return informacionAduanera;
	}

	public void setInformacionAduanera(InformacionAduanera informacionAduanera) {
		this.informacionAduanera = informacionAduanera;
	}

	@XmlElement(name = "Parte")
	public Parte getParte() {
		return parte;
	}

	public void setParte(Parte parte) {
		this.parte = parte;
	}

	@XmlElement(name = "ComplementoConcepto")
	public ComplementoConcepto getComplementoConcepto() {
		return complementoConcepto;
	}

	public void setComplementoConcepto(ComplementoConcepto complementoConcepto) {
		this.complementoConcepto = complementoConcepto;
	}

}
