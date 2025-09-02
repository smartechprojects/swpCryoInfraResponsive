package com.eurest.supplier.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eurest.supplier.invoiceXml.Complemento;
import com.eurest.supplier.invoiceXml.Concepto;
import com.eurest.supplier.invoiceXml.Emisor;
import com.eurest.supplier.invoiceXml.Receptor;

public class InvoiceDTO {
	
	private String fecha;
	private String fechaTimbrado;
	public String folio;
	public String serie;
	public String uuid;
	public String cfdiRelacionado;
	public double subTotal;
	public String moneda;
	public double total;
	public String formaPago;
	public String metodoPago;
	public String condicionesDePago;
	public double descuento;
	public double tipoCambio;
	public double impuestos;
	public String rfcEmisor;
	public String rfcReceptor;
	public double totalImpuestos;
	public double totalRetenidos;
	public String tipoComprobante;
	public String domicilioFiscalReceptor;
	public String regimenFiscalReceptor;
	public String version;
	public List<Concepto> concepto;
	public String message;
	private Complemento complemento;
	private Emisor emisor;
	private Receptor receptor;
	private String sello;
	private String certificado;
	private String lugarExpedicion;
	private ResponseGeneral response;
	
	
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getCfdiRelacionado() {
		return cfdiRelacionado;
	}
	public void setCfdiRelacionado(String cfdiRelacionado) {
		this.cfdiRelacionado = cfdiRelacionado;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public String getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	public String getCondicionesDePago() {
		return condicionesDePago;
	}
	public void setCondicionesDePago(String condicionesDePago) {
		this.condicionesDePago = condicionesDePago;
	}
	public double getDescuento() {
		return descuento;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}
	public double getTipoCambio() {
		return tipoCambio;
	}
	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	public String getRfcEmisor() {
		return rfcEmisor;
	}
	public void setRfcEmisor(String rfcEmisor) {
		this.rfcEmisor = rfcEmisor;
	}
	public String getRfcReceptor() {
		return rfcReceptor;
	}
	public void setRfcReceptor(String rfcReceptor) {
		this.rfcReceptor = rfcReceptor;
	}
	public String getDomicilioFiscalReceptor() {
		return domicilioFiscalReceptor;
	}
	public void setDomicilioFiscalReceptor(String domicilioFiscalReceptor) {
		this.domicilioFiscalReceptor = domicilioFiscalReceptor;
	}
	public String getRegimenFiscalReceptor() {
		return regimenFiscalReceptor;
	}
	public void setRegimenFiscalReceptor(String regimenFiscalReceptor) {
		this.regimenFiscalReceptor = regimenFiscalReceptor;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public List<Concepto> getConcepto() {
		return concepto;
	}
	public void setConcepto(List<Concepto> concepto) {
		this.concepto = concepto;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFechaTimbrado() {
		return fechaTimbrado;
	}
	public void setFechaTimbrado(String fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}
	public double getImpuestos() {
		return impuestos;
	}
	public void setImpuestos(double impuestos) {
		this.impuestos = impuestos;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	public Complemento getComplemento() {
		return complemento;
	}
	public void setComplemento(Complemento complemento) {
		this.complemento = complemento;
	}
	public double getTotalImpuestos() {
		return totalImpuestos;
	}
	public void setTotalImpuestos(double totalImpuestos) {
		this.totalImpuestos = totalImpuestos;
	}	
	public double getTotalRetenidos() {
		return totalRetenidos;
	}
	public void setTotalRetenidos(double totalRetenidos) {
		this.totalRetenidos = totalRetenidos;
	}
	public Emisor getEmisor() {
		return emisor;
	}
	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}
	public Receptor getReceptor() {
		return receptor;
	}
	public void setReceptor(Receptor receptor) {
		this.receptor = receptor;
	}
	public String getSello() {
		return sello;
	}
	public void setSello(String sello) {
		this.sello = sello;
	}
	public String getCertificado() {
		return certificado;
	}
	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}
	public String getLugarExpedicion() {
		return lugarExpedicion;
	}
	public void setLugarExpedicion(String lugarExpedicion) {
		this.lugarExpedicion = lugarExpedicion;
	}
	public ResponseGeneral getResponse() {
		return response;
	}
	public void setResponse(ResponseGeneral response) {
		this.response = response;
	}

	

}
