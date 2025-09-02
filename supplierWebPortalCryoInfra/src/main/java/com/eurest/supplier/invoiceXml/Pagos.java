package com.eurest.supplier.invoiceXml;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Pagos {

    private String version;
    private List<Pago> pago;

	@XmlElement(name = "Pago", namespace="http://www.sat.gob.mx/Pagos")
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

}
