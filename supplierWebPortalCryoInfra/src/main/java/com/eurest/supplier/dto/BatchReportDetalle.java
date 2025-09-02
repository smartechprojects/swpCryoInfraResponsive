package com.eurest.supplier.dto;

public class BatchReportDetalle {

	
	private String TP;
	private String numDoc;
	private String cia;
	private String fechaLM;
	private String rA;
	private String descripCuenta;
	private String cdMon;
	private String debito;
	private String credito;
	private String unidades;
	private String lMAux;
	private String noActivo;
	private String observacion;
	public String getTP() {
		return TP;
	}
	public void setTP(String tP) {
		TP = tP;
	}
	public String getNumDoc() {
		return numDoc==null?"":numDoc;
	}
	public void setNumDoc(String numDoc) {
		this.numDoc = numDoc;
	}
	public String getCia() {
		return cia==null?"":cia;
	}
	public void setCia(String cia) {
		this.cia = cia;
	}
	public String getFechaLM() {
		return fechaLM==null?"":fechaLM;
	}
	public void setFechaLM(String fechaLM) {
		this.fechaLM = fechaLM;
	}
	public String getrA() {
		return rA==null?"":rA;
	}
	public void setrA(String rA) {
		this.rA = rA;
	}
	public String getDescripCuenta() {
		return descripCuenta==null?"":descripCuenta;
	}
	public void setDescripCuenta(String descripCuenta) {
		this.descripCuenta = descripCuenta;
	}
	public String getCdMon() {
		return cdMon==null?"":cdMon;
	}
	public void setCdMon(String cdMon) {
		this.cdMon = cdMon;
	}
	public String getDebito() {
		return debito==null?"":debito;
	}
	public void setDebito(String debito) {
		this.debito = debito;
	}
	public String getCredito() {
		return credito==null?"":credito;
	}
	public void setCredito(String credito) {
		this.credito = credito;
	}
	public String getUnidades() {
		return unidades==null?"":unidades;
	}
	public void setUnidades(String unidades) {
		this.unidades = unidades;
	}
	public String getlMAux() {
		return lMAux==null?"":lMAux;
	}
	public void setlMAux(String lMAux) {
		this.lMAux = lMAux;
	}
	public String getNoActivo() {
		return noActivo==null?"":noActivo;
	}
	public void setNoActivo(String noActivo) {
		this.noActivo = noActivo;
	}
	public String getObservacion() {
		return observacion==null?"":observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	
	
	
	
}
