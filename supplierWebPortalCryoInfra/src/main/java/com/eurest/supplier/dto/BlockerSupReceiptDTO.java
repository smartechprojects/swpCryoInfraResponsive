package com.eurest.supplier.dto;

public class BlockerSupReceiptDTO {
	
	String addressNumber;
	String action;
	String usuario;
	String tipoBloqueo;
	int bloquear;
	int id;
	String accountId;
	String uuid;
	int documentNumber;
	
	
	public String getAddressNumber() {
		return addressNumber;
	}
	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getTipoBloqueo() {
		return tipoBloqueo;
	}
	public void setTipoBloqueo(String tipoBloqueo) {
		this.tipoBloqueo = tipoBloqueo;
	}
	public int getBloquear() {
		return bloquear;
	}
	public void setBloquear(int bloquear) {
		this.bloquear = bloquear;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(int documentNumber) {
		this.documentNumber = documentNumber;
	}
	@Override
	public String toString() {
		return "BlockerSupReceiptDTO [addressNumber=" + addressNumber + ", action=" + action + ", usuario=" + usuario
				+ ", tipoBloqueo=" + tipoBloqueo + ", bloquear=" + bloquear + ", id=" + id + ", accountId=" + accountId
				+ ", uuid=" + uuid + ", documentNumber=" + documentNumber + "]";
	}


}
