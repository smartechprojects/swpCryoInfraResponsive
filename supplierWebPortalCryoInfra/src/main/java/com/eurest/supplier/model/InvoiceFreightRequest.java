package com.eurest.supplier.model;

public class InvoiceFreightRequest {
	
	String xml;
	String pdf;
	String ctaPresupuestal;
	String rfc;
	String serieBitacora;
	String numBitacora;
	
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public String getPdf() {
		return pdf;
	}
	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
	public String getCtaPresupuestal() {
		return ctaPresupuestal;
	}
	public void setCtaPresupuestal(String ctaPresupuestal) {
		this.ctaPresupuestal = ctaPresupuestal;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getSerieBitacora() {
		return serieBitacora;
	}
	public void setSerieBitacora(String serieBitacora) {
		this.serieBitacora = serieBitacora;
	}
	
	public String getNumBitacora() {
		return numBitacora;
	}
	public void setNumBitacora(String numBitacora) {
		this.numBitacora = numBitacora;
	}

	@Override
	public String toString() {
		return "InvoiceFreightRequest [xml=" + xml + ", pdf=" + pdf + ", ctaPresupuestal=" + ctaPresupuestal + ", rfc="
				+ rfc + ", serieBitacora=" + serieBitacora + ", numBitacora=" + numBitacora + "]";
	}
	
	

}
