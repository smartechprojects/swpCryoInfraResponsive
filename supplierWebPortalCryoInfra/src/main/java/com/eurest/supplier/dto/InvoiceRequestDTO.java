package com.eurest.supplier.dto;

public class InvoiceRequestDTO {
	
	private String addressBook; 
	private int documentNumber;
	private String documentType;
	private String uuid;
	
	public String getAddressBook() {
		return addressBook;
	}

	public void setAddressBook(String addressBook) {
		this.addressBook = addressBook;
	}

	public int getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(int documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "InvoiceRequestDTO [addressBook=" + addressBook + ", documentNumber=" + documentNumber
				+ ", documentType=" + documentType + ", uuid=" + uuid + "]";
	}
	
}
