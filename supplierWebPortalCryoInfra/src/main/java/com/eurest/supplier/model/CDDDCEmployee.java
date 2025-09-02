package com.eurest.supplier.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@JsonAutoDetect
@Entity
@Table(name="CDDDCEmployee")
public class CDDDCEmployee {

	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String numSegSoci;
	private String nombre;
	private String rfcCurp;
	private String claveUbicacion;
	
	@ManyToOne()
    @JoinColumn(name = "cdddc_id")
    private CDDDC cddc;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumSegSoci() {
		return numSegSoci;
	}
	public void setNumSegSoci(String numSegSoci) {
		this.numSegSoci = numSegSoci;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getRfcCurp() {
		return rfcCurp;
	}
	public void setRfcCurp(String rfcCurp) {
		this.rfcCurp = rfcCurp;
	}
	public String getClaveUbicacion() {
		return claveUbicacion;
	}
	public void setClaveUbicacion(String claveUbicacion) {
		this.claveUbicacion = claveUbicacion;
	}
	public CDDDC getCddc() {
		return cddc;
	}
	public void setCddc(CDDDC cddc) {
		this.cddc = cddc;
	}
		
}
