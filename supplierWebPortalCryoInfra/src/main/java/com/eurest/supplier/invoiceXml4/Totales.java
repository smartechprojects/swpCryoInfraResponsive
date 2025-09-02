package com.eurest.supplier.invoiceXml4;

import javax.xml.bind.annotation.XmlAttribute;

public class Totales {
	
    
    private String totalRetencionesIVA;    
    private String totalRetencionesISR;    
    private String totalRetencionesIEPS;    
    private String totalTrasladosBaseIVA16;    
    private String totalTrasladosImpuestoIVA16;    
    private String totalTrasladosBaseIVA8;    
    private String totalTrasladosImpuestoIVA8;    
    private String totalTrasladosBaseIVA0;    
    private String totalTrasladosImpuestoIVA0;    
    private String totalTrasladosBaseIVAExento;    
    private String montoTotalPagos;
    
	public String getTotalRetencionesIVA() {
		return totalRetencionesIVA;
	}
	
	@XmlAttribute(name = "TotalRetencionesIVA")
	public void setTotalRetencionesIVA(String totalRetencionesIVA) {
		this.totalRetencionesIVA = totalRetencionesIVA;
	}
	
	public String getTotalRetencionesISR() {
		return totalRetencionesISR;
	}
	
	@XmlAttribute(name = "TotalRetencionesISR")
	public void setTotalRetencionesISR(String totalRetencionesISR) {
		this.totalRetencionesISR = totalRetencionesISR;
	}
	
	public String getTotalRetencionesIEPS() {
		return totalRetencionesIEPS;
	}
	
	@XmlAttribute(name = "TotalRetencionesIEPS")
	public void setTotalRetencionesIEPS(String totalRetencionesIEPS) {
		this.totalRetencionesIEPS = totalRetencionesIEPS;
	}
	
	public String getTotalTrasladosBaseIVA16() {
		return totalTrasladosBaseIVA16;
	}
	
	@XmlAttribute(name = "TotalTrasladosBaseIVA16")
	public void setTotalTrasladosBaseIVA16(String totalTrasladosBaseIVA16) {
		this.totalTrasladosBaseIVA16 = totalTrasladosBaseIVA16;
	}
	
	public String getTotalTrasladosImpuestoIVA16() {
		return totalTrasladosImpuestoIVA16;
	}
	
	@XmlAttribute(name = "TotalTrasladosImpuestoIVA16")
	public void setTotalTrasladosImpuestoIVA16(String totalTrasladosImpuestoIVA16) {
		this.totalTrasladosImpuestoIVA16 = totalTrasladosImpuestoIVA16;
	}
	
	public String getTotalTrasladosBaseIVA8() {
		return totalTrasladosBaseIVA8;
	}
	
	@XmlAttribute(name = "TotalTrasladosBaseIVA8")
	public void setTotalTrasladosBaseIVA8(String totalTrasladosBaseIVA8) {
		this.totalTrasladosBaseIVA8 = totalTrasladosBaseIVA8;
	}
	
	public String getTotalTrasladosImpuestoIVA8() {
		return totalTrasladosImpuestoIVA8;
	}
	
	@XmlAttribute(name = "TotalTrasladosImpuestoIVA8")
	public void setTotalTrasladosImpuestoIVA8(String totalTrasladosImpuestoIVA8) {
		this.totalTrasladosImpuestoIVA8 = totalTrasladosImpuestoIVA8;
	}
	
	public String getTotalTrasladosBaseIVA0() {
		return totalTrasladosBaseIVA0;
	}
	
	@XmlAttribute(name = "TotalTrasladosBaseIVA0")
	public void setTotalTrasladosBaseIVA0(String totalTrasladosBaseIVA0) {
		this.totalTrasladosBaseIVA0 = totalTrasladosBaseIVA0;
	}
	
	public String getTotalTrasladosImpuestoIVA0() {
		return totalTrasladosImpuestoIVA0;
	}
	
	@XmlAttribute(name = "TotalTrasladosImpuestoIVA0")
	public void setTotalTrasladosImpuestoIVA0(String totalTrasladosImpuestoIVA0) {
		this.totalTrasladosImpuestoIVA0 = totalTrasladosImpuestoIVA0;
	}
	
	public String getTotalTrasladosBaseIVAExento() {
		return totalTrasladosBaseIVAExento;
	}
	
	@XmlAttribute(name = "TotalTrasladosBaseIVAExento")
	public void setTotalTrasladosBaseIVAExento(String totalTrasladosBaseIVAExento) {
		this.totalTrasladosBaseIVAExento = totalTrasladosBaseIVAExento;
	}
	
	public String getMontoTotalPagos() {
		return montoTotalPagos;
	}
	
	@XmlAttribute(name = "MontoTotalPagos")
	public void setMontoTotalPagos(String montoTotalPagos) {
		this.montoTotalPagos = montoTotalPagos;
	}
    
    

}
