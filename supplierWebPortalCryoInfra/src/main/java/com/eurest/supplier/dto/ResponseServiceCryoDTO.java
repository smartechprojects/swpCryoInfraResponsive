package com.eurest.supplier.dto;

public class ResponseServiceCryoDTO {
	
	private boolean ok;
	private accesTokenDTO accessToken;
	
	
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	public accesTokenDTO getAccesToken() {
		return accessToken;
	}
	public void setAccesToken(accesTokenDTO accesToken) {
		this.accessToken = accesToken;
	}
	
	

}
