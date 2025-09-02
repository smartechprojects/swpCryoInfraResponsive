package com.eurest.supplier.dto;

public class FiscalDocumentsDTO {
	//private int id;
	private int orderNumber;
	private String orderType;
	//private String folio;
	private int documentNumber;
	/*
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
*/
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	/*
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}	
	*/
	public int getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(int documentNumber) {
		this.documentNumber = documentNumber;
	}
	
}
