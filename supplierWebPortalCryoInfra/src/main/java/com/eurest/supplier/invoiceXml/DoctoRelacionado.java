package com.eurest.supplier.invoiceXml;
import javax.xml.bind.annotation.XmlAttribute;

public class DoctoRelacionado {

	private String idDocumento;
	private String serie;
	private String folio;
	private String monedaDR;
	private String tipoCambioDR;
	private String metodoDePagoDR;
	private String numParcialidad;
	private String impSaldoAnt;
	private String impPagado;
	private String impSaldoInsoluto;

	public String getIdDocumento() {
		return idDocumento;
	}

	@XmlAttribute(name = "IdDocumento")
	public void setIdDocumento(String idDocumento) {
		this.idDocumento = idDocumento;
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

	public String getMonedaDR() {
		return monedaDR;
	}

	@XmlAttribute(name = "MonedaDR")
	public void setMonedaDR(String monedaDR) {
		this.monedaDR = monedaDR;
	}

	public String getTipoCambioDR() {
		return tipoCambioDR;
	}

	@XmlAttribute(name = "TipoCambioDR")
	public void setTipoCambioDR(String tipoCambioDR) {
		this.tipoCambioDR = tipoCambioDR;
	}

	public String getMetodoDePagoDR() {
		return metodoDePagoDR;
	}

	@XmlAttribute(name = "MetodoDePagoDR")
	public void setMetodoDePagoDR(String metodoDePagoDR) {
		this.metodoDePagoDR = metodoDePagoDR;
	}

	public String getNumParcialidad() {
		return numParcialidad;
	}

	@XmlAttribute(name = "NumParcialidad")
	public void setNumParcialidad(String numParcialidad) {
		this.numParcialidad = numParcialidad;
	}

	public String getImpSaldoAnt() {
		return impSaldoAnt;
	}

	@XmlAttribute(name = "ImpSaldoAnt")
	public void setImpSaldoAnt(String impSaldoAnt) {
		this.impSaldoAnt = impSaldoAnt;
	}

	public String getImpPagado() {
		return impPagado;
	}

	@XmlAttribute(name = "ImpPagado")
	public void setImpPagado(String impPagado) {
		this.impPagado = impPagado;
	}

	public String getImpSaldoInsoluto() {
		return impSaldoInsoluto;
	}

	@XmlAttribute(name = "ImpSaldoInsoluto")
	public void setImpSaldoInsoluto(String impSaldoInsoluto) {
		this.impSaldoInsoluto = impSaldoInsoluto;
	}

}
