package com.eurest.supplier.dto;

public class ZipElementDTO {
	
	private InvoiceDTO invoiceDTO;
	private String attachedFileName;
	private String xmlFileName;
	private byte[] attachedFile;
	private byte[] xmlFile;
	
	public InvoiceDTO getInvoiceDTO() {
		return invoiceDTO;
	}
	public void setInvoiceDTO(InvoiceDTO invoiceDTO) {
		this.invoiceDTO = invoiceDTO;
	}	
	public String getAttachedFileName() {
		return attachedFileName;
	}
	public void setAttachedFileName(String attachedFileName) {
		this.attachedFileName = attachedFileName;
	}
	public String getXmlFileName() {
		return xmlFileName;
	}
	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}
	public byte[] getAttachedFile() {
		return attachedFile;
	}
	public void setAttachedFile(byte[] attachedFile) {
		this.attachedFile = attachedFile;
	}
	public byte[] getXmlFile() {
		return xmlFile;
	}
	public void setXmlFile(byte[] xmlFile) {
		this.xmlFile = xmlFile;
	}
	
}
