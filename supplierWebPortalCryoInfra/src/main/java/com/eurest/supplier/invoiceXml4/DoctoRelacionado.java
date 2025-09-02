package com.eurest.supplier.invoiceXml4;
import javax.xml.bind.annotation.XmlAttribute;

public class DoctoRelacionado {

	private String idDocumento;
	private String serie;
	private String folio;
	private String monedaDR;
	private String equivalenciaDR;
	private String numParcialidad;
	private String impSaldoAnt;
	private String impPagado;
	private String impSaldoInsoluto;
	private String objetoImpDR;

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

	public String getEquivalenciaDR() {
		return equivalenciaDR;
	}

	@XmlAttribute(name = "EquivalenciaDR")
	public void setEquivalenciaDR(String equivalenciaDR) {
		this.equivalenciaDR = equivalenciaDR;
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

	public String getObjetoImpDR() {
		return objetoImpDR;
	}

	@XmlAttribute(name = "ObjetoImpDR")
	public void setObjetoImpDR(String objetoImpDR) {
		this.objetoImpDR = objetoImpDR;
	}

}
