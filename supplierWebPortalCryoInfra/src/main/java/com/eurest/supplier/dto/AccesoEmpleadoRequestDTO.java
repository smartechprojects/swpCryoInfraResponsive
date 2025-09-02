package com.eurest.supplier.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AccesoEmpleadoRequestDTO {
	
	 private String NumeroAfiliacionIMSS;
     private String NombreCompleto;
     private String Puesto;
     private String FechaInduccion;
     private String FolioCredencial;
     private String CURP;
     private String RFC;
     private List<OrdenRequestDTO> Ordenes;
     private List<Integer> Actividades;
	public String getNumeroAfiliacionIMSS() {
		return NumeroAfiliacionIMSS;
	}
	public void setNumeroAfiliacionIMSS(String numeroAfiliacionIMSS) {
		NumeroAfiliacionIMSS = numeroAfiliacionIMSS;
	}
	public String getNombreCompleto() {
		return NombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		NombreCompleto = nombreCompleto;
	}
	public String getPuesto() {
		return Puesto;
	}
	public void setPuesto(String puesto) {
		Puesto = puesto;
	}
	
	public String getFechaInduccion() {
		return FechaInduccion;
	}
	public void setFechaInduccion(String fechaInduccion) {
		FechaInduccion = fechaInduccion;
	}
	public String getFolioCredencial() {
		return FolioCredencial;
	}
	public void setFolioCredencial(String folioCredencial) {
		FolioCredencial = folioCredencial;
	}
	public String getCURP() {
		return CURP;
	}
	public void setCURP(String cURP) {
		CURP = cURP;
	}
	public String getRFC() {
		return RFC;
	}
	public void setRFC(String rFC) {
		RFC = rFC;
	}
	public List<OrdenRequestDTO> getOrdenes() {
		return Ordenes;
	}
	public void setOrdenes(List<OrdenRequestDTO> ordenes) {
		Ordenes = ordenes;
	}
	public List<Integer> getActividades() {
		return Actividades;
	}
	public void setActividades(List<Integer> actividades) {
		Actividades = actividades;
	}
     
     

}
