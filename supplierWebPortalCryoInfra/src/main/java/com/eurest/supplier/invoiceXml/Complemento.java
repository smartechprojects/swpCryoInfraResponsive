package com.eurest.supplier.invoiceXml;

import javax.xml.bind.annotation.XmlElement;

public class Complemento {

	private TimbreFiscalDigital timbreFiscalDigital;
	private Pagos pagos;
	private ImpuestosLocales impuestosLocales;

	@XmlElement(name = "TimbreFiscalDigital", namespace="http://www.sat.gob.mx/TimbreFiscalDigital")
	public TimbreFiscalDigital getTimbreFiscalDigital() {
		return timbreFiscalDigital;
	}

	public void setTimbreFiscalDigital(TimbreFiscalDigital timbreFiscalDigital) {
		this.timbreFiscalDigital = timbreFiscalDigital;
	}
	
	@XmlElement(name = "Pagos", namespace="http://www.sat.gob.mx/Pagos")
	public Pagos getPago() {
		return pagos;
	}

	public void setPago(Pagos pagos) {
		this.pagos = pagos;
	}
	
	@XmlElement(name = "ImpuestosLocales", namespace = "http://www.sat.gob.mx/implocal")
	public ImpuestosLocales getImpuestosLocales() {
		return impuestosLocales;
	}

	public void setImpuestosLocales(ImpuestosLocales impuestosLocales) {
		this.impuestosLocales = impuestosLocales;
	}
	
	
}
