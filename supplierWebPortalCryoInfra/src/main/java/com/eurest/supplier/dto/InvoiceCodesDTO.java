package com.eurest.supplier.dto;

public class InvoiceCodesDTO {
	
	private String uuid;
	private String code;
	private String uom;
	private String description;
	private String descriptionSAT;
	private double amount;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getDescriptionSAT() {
		return descriptionSAT;
	}
	public void setDescriptionSAT(String descriptionSAT) {
		this.descriptionSAT = descriptionSAT;
	}


}
