package com.eurest.supplier.invoiceXml4;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Pago {

	private String fechaPago;
    private String formaDePagoP;
    private String monedaP;
    private String tipoCambioP;
    private String monto;
    private String numOperacion;
    private String rfcEmisorCtaOrd;
    private String nomBancoOrdExt;
    private String ctaOrdenante;
    private String rfcEmisorCtaBen;
    private String ctaBeneficiario;
    private String tipoCadPago;
    private String certPago;
    private String cadPago;
    private String selloPago;
    private List<DoctoRelacionado> doctoRelacionado;
    private List<ImpuestosP> impuestosP;
    
    @XmlElement(name = "DoctoRelacionado", namespace="http://www.sat.gob.mx/Pagos20")
	public List<DoctoRelacionado> getDoctoRelacionado() {
		return doctoRelacionado;
	}

	public void setDoctoRelacionado(List<DoctoRelacionado> doctoRelacionado) {
		this.doctoRelacionado = doctoRelacionado;
	}

	@XmlElement(name = "ImpuestosP")
	public List<ImpuestosP> getImpuestosP() {
		return impuestosP;
	}

	public void setImpuestosP(List<ImpuestosP> impuestosP) {
		this.impuestosP = impuestosP;
	}

	public String getFechaPago() {
		return fechaPago;
	}
	
	@XmlAttribute(name="FechaPago")
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}

	public String getFormaDePagoP() {
		return formaDePagoP;
	}

	@XmlAttribute(name="FormaDePagoP")
	public void setFormaDePagoP(String formaDePagoP) {
		this.formaDePagoP = formaDePagoP;
	}

	public String getMonedaP() {
		return monedaP;
	}

	@XmlAttribute(name="MonedaP")
	public void setMonedaP(String monedaP) {
		this.monedaP = monedaP;
	}

	public String getTipoCambioP() {
		return tipoCambioP;
	}

	@XmlAttribute(name="TipoCambioP")
	public void setTipoCambioP(String tipoCambioP) {
		this.tipoCambioP = tipoCambioP;
	}

	public String getMonto() {
		return monto;
	}
	
	@XmlAttribute(name="Monto")
	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getNumOperacion() {
		return numOperacion;
	}

	@XmlAttribute(name="NumOperacion")
	public void setNumOperacion(String numOperacion) {
		this.numOperacion = numOperacion;
	}

	public String getRfcEmisorCtaOrd() {
		return rfcEmisorCtaOrd;
	}

	@XmlAttribute(name="RfcEmisorCtaOrd")
	public void setRfcEmisorCtaOrd(String rfcEmisorCtaOrd) {
		this.rfcEmisorCtaOrd = rfcEmisorCtaOrd;
	}

	public String getNomBancoOrdExt() {
		return nomBancoOrdExt;
	}

	@XmlAttribute(name="NomBancoOrdExt")
	public void setNomBancoOrdExt(String nomBancoOrdExt) {
		this.nomBancoOrdExt = nomBancoOrdExt;
	}

	public String getCtaOrdenante() {
		return ctaOrdenante;
	}

	@XmlAttribute(name="CtaOrdenante")
	public void setCtaOrdenante(String ctaOrdenante) {
		this.ctaOrdenante = ctaOrdenante;
	}

	public String getRfcEmisorCtaBen() {
		return rfcEmisorCtaBen;
	}

	@XmlAttribute(name="RfcEmisorCtaBen")
	public void setRfcEmisorCtaBen(String rfcEmisorCtaBen) {
		this.rfcEmisorCtaBen = rfcEmisorCtaBen;
	}

	public String getCtaBeneficiario() {
		return ctaBeneficiario;
	}

	@XmlAttribute(name="CtaBeneficiario")
	public void setCtaBeneficiario(String ctaBeneficiario) {
		this.ctaBeneficiario = ctaBeneficiario;
	}

	public String getTipoCadPago() {
		return tipoCadPago;
	}

	@XmlAttribute(name="TipoCadPago")
	public void setTipoCadPago(String tipoCadPago) {
		this.tipoCadPago = tipoCadPago;
	}

	public String getCertPago() {
		return certPago;
	}

	@XmlAttribute(name="CertPago")
	public void setCertPago(String certPago) {
		this.certPago = certPago;
	}

	public String getCadPago() {
		return cadPago;
	}

	@XmlAttribute(name="CadPago")
	public void setCadPago(String cadPago) {
		this.cadPago = cadPago;
	}

	public String getSelloPago() {
		return selloPago;
	}

	@XmlAttribute(name="SelloPago")
	public void setSelloPago(String selloPago) {
		this.selloPago = selloPago;
	}

}
