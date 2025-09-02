package com.eurest.supplier.dto;

import java.util.ArrayList;


public class RequestComplemetPagDTO {
	String fechaInicio;
	String fechaFin;
	String uuids;
	ArrayList<String> xml;
	String accion;
	
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	
	public String getUuids() {
		return uuids;
	}
	public void setUuids(String uuids) {
		this.uuids = uuids;
	}
	public ArrayList<String> getXml() {
		return xml;
	}
	public void setXml(ArrayList<String> xml) {
		this.xml = xml;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	

}
