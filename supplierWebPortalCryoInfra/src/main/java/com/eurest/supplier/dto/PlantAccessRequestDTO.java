package com.eurest.supplier.dto;

import java.util.List;

public class PlantAccessRequestDTO {

    private long ClaveAccesoPlantaPortal;
    private long NumeroProveedor;
    private String RegistroPatronal;
    private String NombreSolicitante;
    private String RepresentanteProveedor;
    private String FechaVigenciaInicio;
    private String FechaVigenciaFinal;
    private String FechaAutorizacion;
    private String NombreAutorizador;
    private List<AtencionOrdenesRequestDTO> AtencionOrdenes;
    private List<AccesoEmpleadoRequestDTO> AccesoEmpleados;
    private String RazonSocialSubcontratada;
    private String RFCSubcontratada;
    
    
	public long getClaveAccesoPlantaPortal() {
		return ClaveAccesoPlantaPortal;
	}
	public void setClaveAccesoPlantaPortal(long claveAccesoPlantaPortal) {
		ClaveAccesoPlantaPortal = claveAccesoPlantaPortal;
	}
	public long getNumeroProveedor() {
		return NumeroProveedor;
	}
	public void setNumeroProveedor(long numeroProveedor) {
		NumeroProveedor = numeroProveedor;
	}
	public String getRegistroPatronal() {
		return RegistroPatronal;
	}
	public void setRegistroPatronal(String registroPatronal) {
		RegistroPatronal = registroPatronal;
	}
	public String getNombreSolicitante() {
		return NombreSolicitante;
	}
	public void setNombreSolicitante(String nombreSolicitante) {
		NombreSolicitante = nombreSolicitante;
	}
	public String getRepresentanteProveedor() {
		return RepresentanteProveedor;
	}
	public void setRepresentanteProveedor(String representanteProveedor) {
		RepresentanteProveedor = representanteProveedor;
	}
	
	public String getFechaVigenciaInicio() {
		return FechaVigenciaInicio;
	}
	public void setFechaVigenciaInicio(String fechaVigenciaInicio) {
		FechaVigenciaInicio = fechaVigenciaInicio;
	}
	public String getFechaVigenciaFinal() {
		return FechaVigenciaFinal;
	}
	public void setFechaVigenciaFinal(String fechaVigenciaFinal) {
		FechaVigenciaFinal = fechaVigenciaFinal;
	}
	public String getFechaAutorizacion() {
		return FechaAutorizacion;
	}
	public void setFechaAutorizacion(String fechaAutorizacion) {
		FechaAutorizacion = fechaAutorizacion;
	}
	public String getNombreAutorizador() {
		return NombreAutorizador;
	}
	public void setNombreAutorizador(String nombreAutorizador) {
		NombreAutorizador = nombreAutorizador;
	}
	public List<AtencionOrdenesRequestDTO> getAtencionOrdenes() {
		return AtencionOrdenes;
	}
	public void setAtencionOrdenes(List<AtencionOrdenesRequestDTO> atencionOrdenes) {
		AtencionOrdenes = atencionOrdenes;
	}
	public List<AccesoEmpleadoRequestDTO> getAccesoEmpleados() {
		return AccesoEmpleados;
	}
	public void setAccesoEmpleados(List<AccesoEmpleadoRequestDTO> accesoEmpleados) {
		AccesoEmpleados = accesoEmpleados;
	}
	public String getRazonSocialSubcontratada() {
		return RazonSocialSubcontratada;
	}
	public void setRazonSocialSubcontratada(String razonSocialSubcontratada) {
		RazonSocialSubcontratada = razonSocialSubcontratada;
	}
	public String getRFCSubcontratada() {
		return RFCSubcontratada;
	}
	public void setRFCSubcontratada(String rFCSubcontratada) {
		RFCSubcontratada = rFCSubcontratada;
	}
    
    
}
