package com.eurest.supplier.edi;

//F01151
public class AddessBookElectronicAddressDTO {
	
	private String EBEDUS;
	private String EBEDBT;
	private String EBEDTN;
	private double EBEDLN;
	private String EBEDTL;
	private String EBEDSP;
	private String EBTNAC; //Defaulvaluet to A
	private String EBAN8;
	private String EBIDLN; // Default to addressbook but just only 5 digits
	private String EBEMAL;
	
	public String getEBEDUS() {
		return EBEDUS;
	}
	public void setEBEDUS(String eBEDUS) {
		EBEDUS = eBEDUS;
	}
	public String getEBEDBT() {
		return EBEDBT;
	}
	public void setEBEDBT(String eBEDBT) {
		EBEDBT = eBEDBT;
	}
	public String getEBEDTN() {
		return EBEDTN;
	}
	public void setEBEDTN(String eBEDTN) {
		EBEDTN = eBEDTN;
	}
	public double getEBEDLN() {
		return EBEDLN;
	}
	public void setEBEDLN(double eBEDLN) {
		EBEDLN = eBEDLN;
	}
	public String getEBEDTL() {
		return EBEDTL;
	}
	public void setEBEDTL(String eBEDTL) {
		EBEDTL = eBEDTL;
	}
	public String getEBEDSP() {
		return EBEDSP;
	}
	public void setEBEDSP(String eBEDSP) {
		EBEDSP = eBEDSP;
	}
	public String getEBTNAC() {
		return EBTNAC;
	}
	public void setEBTNAC(String eBTNAC) {
		EBTNAC = eBTNAC;
	}
	public String getEBAN8() {
		return EBAN8;
	}
	public void setEBAN8(String eBAN8) {
		EBAN8 = eBAN8;
	}
	public String getEBIDLN() {
		return EBIDLN;
	}
	public void setEBIDLN(String eBIDLN) {
		EBIDLN = eBIDLN;
	}
	public String getEBEMAL() {
		return EBEMAL;
	}
	public void setEBEMAL(String eBEMAL) {
		EBEMAL = eBEMAL;
	}
	
}
