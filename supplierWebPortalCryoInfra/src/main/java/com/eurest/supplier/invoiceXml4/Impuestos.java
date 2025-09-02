package com.eurest.supplier.invoiceXml4;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Impuestos {

    private String totalImpuestosTrasladados;
	private String totalImpuestosRetenidos;
    private Traslados traslados;
    private Retenciones retenciones;

    public String getTotalImpuestosTrasladados() {
        return totalImpuestosTrasladados;
    }

    @XmlAttribute(name = "TotalImpuestosTrasladados")
    public void setTotalImpuestosTrasladados(String totalImpuestosTrasladados) {
        this.totalImpuestosTrasladados = totalImpuestosTrasladados;
    }
    
	public String getTotalImpuestosRetenidos() {
		return totalImpuestosRetenidos;
	}

	@XmlAttribute(name="TotalImpuestosRetenidos")
	public void setTotalImpuestosRetenidos(String totalImpuestosRetenidos) {
		this.totalImpuestosRetenidos = totalImpuestosRetenidos;
	}
	
    @XmlElement(name = "Traslados")
    public Traslados getTraslados() {
        return traslados;
    }

    public void setTraslados(Traslados traslados) {
        this.traslados = traslados;
    }
    @XmlElement(name = "Retenciones")
    public Retenciones getRetenciones() {
        return retenciones;
    }

    public void setRetenciones(Retenciones retenciones) {
        this.retenciones = retenciones;
    }

}
