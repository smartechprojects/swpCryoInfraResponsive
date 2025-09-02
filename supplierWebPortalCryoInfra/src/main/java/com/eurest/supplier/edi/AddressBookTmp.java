package com.eurest.supplier.edi;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressBookTmp implements Serializable{ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String VOEDUS;
	

	private String VOEDTY;
	

	private int VOEDSQ;
	

	private String VOEDTN;
	private String VOEDCT;
	private int VOEDLN;
	private String VOEDTS;
	private String VOEDFT;
	public String getVOEDUS() {
		return VOEDUS;
	}
	public void setVOEDUS(String vOEDUS) {
		VOEDUS = vOEDUS;
	}
	public String getVOEDTY() {
		return VOEDTY;
	}
	public void setVOEDTY(String vOEDTY) {
		VOEDTY = vOEDTY;
	}
	public int getVOEDSQ() {
		return VOEDSQ;
	}
	public void setVOEDSQ(int vOEDSQ) {
		VOEDSQ = vOEDSQ;
	}
	public String getVOEDTN() {
		return VOEDTN;
	}
	public void setVOEDTN(String vOEDTN) {
		VOEDTN = vOEDTN;
	}
	public String getVOEDCT() {
		return VOEDCT;
	}
	public void setVOEDCT(String vOEDCT) {
		VOEDCT = vOEDCT;
	}
	public int getVOEDLN() {
		return VOEDLN;
	}
	public void setVOEDLN(int vOEDLN) {
		VOEDLN = vOEDLN;
	}
	public String getVOEDTS() {
		return VOEDTS;
	}
	public void setVOEDTS(String vOEDTS) {
		VOEDTS = vOEDTS;
	}
	public String getVOEDFT() {
		return VOEDFT;
	}
	public void setVOEDFT(String vOEDFT) {
		VOEDFT = vOEDFT;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	


}
