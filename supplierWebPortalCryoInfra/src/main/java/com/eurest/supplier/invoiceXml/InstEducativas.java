package com.eurest.supplier.invoiceXml;

import javax.xml.bind.annotation.XmlAttribute;

public class InstEducativas {

	private String version;
	private String rfcPago;
	private String autRVOE;
	private String nivelEducativo;
	private String CURP;
	private String nombreAlumno;

	public String getVersion() {
		return version;
	}

	@XmlAttribute(name = "version")
	public void setVersion(String version) {
		this.version = version;
	}

	public String getRfcPago() {
		return rfcPago;
	}

	@XmlAttribute(name = "rfcPago")
	public void setRfcPago(String rfcPago) {
		this.rfcPago = rfcPago;
	}

	public String getAutRVOE() {
		return autRVOE;
	}

	@XmlAttribute(name = "autRVOE")
	public void setAutRVOE(String autRVOE) {
		this.autRVOE = autRVOE;
	}

	public String getNivelEducativo() {
		return nivelEducativo;
	}

	@XmlAttribute(name = "nivelEducativo")
	public void setNivelEducativo(String nivelEducativo) {
		this.nivelEducativo = nivelEducativo;
	}

	public String getCURP() {
		return CURP;
	}

	@XmlAttribute(name = "CURP")
	public void setCURP(String CURP) {
		this.CURP = CURP;
	}

	public String getNombreAlumno() {
		return nombreAlumno;
	}

	@XmlAttribute(name = "nombreAlumno")
	public void setNombreAlumno(String nombreAlumno) {
		this.nombreAlumno = nombreAlumno;
	}
}
