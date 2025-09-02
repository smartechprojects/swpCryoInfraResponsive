package com.eurest.supplier.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="codigossat")
public class CodigosSAT {

	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(length = 20)
	private String codigoSAT;
	
	@Column(length = 20)
	private String tipoCodigo;
	
	@Column(length = 200)
	private String descripcion;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCodigoSAT() {
		return codigoSAT;
	}
	public void setCodigoSAT(String codigoSAT) {
		this.codigoSAT = codigoSAT;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getTipoCodigo() {
		return tipoCodigo;
	}
	public void setTipoCodigo(String tipoCodigo) {
		this.tipoCodigo = tipoCodigo;
	}
	
	
}
