package com.eurest.supplier.model;

public class InvoiceFreightResponse {
	

	String serieBitacora;
	String numBitacora;
	String serieFactura;
	String folioFactura;
	String respuesta;
	int valido;
	
	public String getSerieBitacora() {
		return serieBitacora;
	}
	public void setSerieBitacora(String serieBitacora) {
		this.serieBitacora = serieBitacora;
	}
	
	public String getSerieFactura() {
		return serieFactura;
	}
	public void setSerieFactura(String serieFactura) {
		this.serieFactura = serieFactura;
	}
	
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public int getValido() {
		return valido;
	}
	public void setValido(int valido) {
		this.valido = valido;
	}
	public String getNumBitacora() {
		return numBitacora;
	}
	public void setNumBitacora(String numBitacora) {
		this.numBitacora = numBitacora;
	}
	public String getFolioFactura() {
		return folioFactura;
	}
	public void setFolioFactura(String folioFactura) {
		this.folioFactura = folioFactura;
	}
	@Override
	public String toString() {
		return "InvoiceFreightResponse [serieBitacora=" + serieBitacora + ", numBitacora=" + numBitacora
				+ ", serieFactura=" + serieFactura + ", folioFactura=" + folioFactura + ", respuesta=" + respuesta
				+ ", valido=" + valido + "]";
	}
	
	
	
	
}