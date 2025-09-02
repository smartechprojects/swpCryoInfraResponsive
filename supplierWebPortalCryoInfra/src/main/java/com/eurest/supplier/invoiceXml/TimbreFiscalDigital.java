package com.eurest.supplier.invoiceXml;

import javax.xml.bind.annotation.XmlAttribute;

public class TimbreFiscalDigital {

	private String version;
	private String uUID;
	private String fechaTimbrado;
	private String rfcProvCertif;
	private String selloCFD;
	private String noCertificadoSAT;
	private String selloSAT;
	private String xsiSchemaLocationTimbre;
	private String xsiNameSpaceTimbre;

	public String getVersion() {
		return version;
	}

	@XmlAttribute(name = "Version")
	public void setVersion(String version) {
		this.version = version;
	}

	public String getUUID() {
		return uUID;
	}

	@XmlAttribute(name = "UUID")
	public void setUUID(String uUID) {
		this.uUID = uUID;
	}

	public String getFechaTimbrado() {
		return fechaTimbrado;
	}

	@XmlAttribute(name = "FechaTimbrado")
	public void setFechaTimbrado(String fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}

	public String getRfcProvCertif() {
		return rfcProvCertif;
	}

	@XmlAttribute(name = "RfcProvCertif")
	public void setRfcProvCertif(String rfcProvCertif) {
		this.rfcProvCertif = rfcProvCertif;
	}

	public String getSelloCFD() {
		return selloCFD;
	}

	@XmlAttribute(name = "SelloCFD")
	public void setSelloCFD(String selloCFD) {
		this.selloCFD = selloCFD;
	}

	public String getNoCertificadoSAT() {
		return noCertificadoSAT;
	}

	@XmlAttribute(name = "NoCertificadoSAT")
	public void setNoCertificadoSAT(String noCertificadoSAT) {
		this.noCertificadoSAT = noCertificadoSAT;
	}

	public String getSelloSAT() {
		return selloSAT;
	}

	@XmlAttribute(name = "SelloSAT")
	public void setSelloSAT(String selloSAT) {
		this.selloSAT = selloSAT;
	}

	public String getXsiSchemaLocationTimbre() {
		return xsiSchemaLocationTimbre;
	}

	@XmlAttribute(name = "xsiSchemaLocationTimbre")
	public void setXsiSchemaLocationTimbre(String xsiSchemaLocationTimbre) {
		this.xsiSchemaLocationTimbre = xsiSchemaLocationTimbre;
	}

	public String getXsiNameSpaceTimbre() {
		return xsiNameSpaceTimbre;
	}

	@XmlAttribute(name = "xsiNameSpaceTimbre")
	public void setXsiNameSpaceTimbre(String xsiNameSpaceTimbre) {
		this.xsiNameSpaceTimbre = xsiNameSpaceTimbre;
	}

}
