package com.eurest.supplier.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "purchaseordernotes")
public class PurchaseOrderNotes {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String orderCompany; // Order company
	private int orderNumber; // Document
	private String orderType; // Order Type
	
	private int lineNumber; 
	private String lineNtc;
	private int instruction;
	private String lineText;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getOrderCompany() {
		return orderCompany;
	}
	public void setOrderCompany(String orderCompany) {
		this.orderCompany = orderCompany;
	}
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
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getLineNtc() {
		return lineNtc;
	}
	public void setLineNtc(String lineNtc) {
		this.lineNtc = lineNtc;
	}
	public int getInstruction() {
		return instruction;
	}
	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
	public String getLineText() {
		return lineText;
	}
	public void setLineText(String lineText) {
		this.lineText = lineText;
	}

}
