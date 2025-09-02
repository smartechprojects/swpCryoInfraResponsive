package com.eurest.supplier.invoiceXml4;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Pagos {

    private String version;
    private List<Pago> pago;
    private Totales totales;

	@XmlElement(name = "Pago", namespace="http://www.sat.gob.mx/Pagos20")
	public List<Pago> getPago() {
		return pago;
	}

	public void setPago(List<Pago> pago) {
		this.pago = pago;
	}

	public String getVersion() {
		return version;
	}
	
	@XmlAttribute(name="Version")
	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement(name = "Totales", namespace="http://www.sat.gob.mx/Pagos20")
	public Totales getTotales() {
		return totales;
	}

	public void setTotales(Totales totales) {
		this.totales = totales;
	}

	
}
