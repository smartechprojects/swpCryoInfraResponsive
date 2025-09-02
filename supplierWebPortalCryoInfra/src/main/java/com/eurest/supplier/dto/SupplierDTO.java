package com.eurest.supplier.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

public class SupplierDTO {
	
	private int id;
	private String name;
	private String addresNumber;
	private String razonSocial;
	private long ticketId;
    private String email;
    private String categoria;
    private String tipoProducto;
    private String currentApprover;
    private String nextApprover;
    private String approvalStatus;
    private String approvalStep;
    private String observaciones;
	private String rejectNotes;
	private String approvalNotes;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date fechaAprobacion;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date fechaSolicitud;
	
    private boolean logged;

    
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCurrentApprover() {
		return currentApprover;
	}
	public void setCurrentApprover(String currentApprover) {
		this.currentApprover = currentApprover;
	}
	public String getNextApprover() {
		return nextApprover;
	}
	public void setNextApprover(String nextApprover) {
		this.nextApprover = nextApprover;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getApprovalStep() {
		return approvalStep;
	}
	public void setApprovalStep(String approvalStep) {
		this.approvalStep = approvalStep;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getTipoProducto() {
		return tipoProducto;
	}
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	public String getAddresNumber() {
		return addresNumber;
	}
	public void setAddresNumber(String addresNumber) {
		this.addresNumber = addresNumber;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public long getTicketId() {
		return ticketId;
	}
	public void setTicketId(long ticketId) {
		this.ticketId = ticketId;
	}
	public boolean isLogged() {
		return logged;
	}
	public void setLogged(boolean logged) {
		this.logged = logged;
	}
	public String getRejectNotes() {
		return rejectNotes;
	}
	public void setRejectNotes(String rejectNotes) {
		this.rejectNotes = rejectNotes;
	}
	public String getApprovalNotes() {
		return approvalNotes;
	}
	public void setApprovalNotes(String approvalNotes) {
		this.approvalNotes = approvalNotes;
	}
	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}
	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	
    
}
