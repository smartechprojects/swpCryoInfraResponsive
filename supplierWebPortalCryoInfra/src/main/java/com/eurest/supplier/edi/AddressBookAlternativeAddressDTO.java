package com.eurest.supplier.edi;

public class AddressBookAlternativeAddressDTO {
	
	private String LBEDUS;
	private String LBEDBT;
	private String LBEDTN;
	private double LBEDLN;
	private String LBEDSP; //Default as blank
	private String LBTNAC; // Default as A
	private int LBAN8;
	private String LBATYPE; // Ver UDC 01/AT 
	private int LBIDLN; // Default to addressbook but just only 5 digits
	private String LBADD1;
	
	public String getLBEDUS() {
		return LBEDUS;
	}
	public void setLBEDUS(String lBEDUS) {
		LBEDUS = lBEDUS;
	}
	public String getLBEDBT() {
		return LBEDBT;
	}
	public void setLBEDBT(String lBEDBT) {
		LBEDBT = lBEDBT;
	}
	public String getLBEDTN() {
		return LBEDTN;
	}
	public void setLBEDTN(String lBEDTN) {
		LBEDTN = lBEDTN;
	}
	public double getLBEDLN() {
		return LBEDLN;
	}
	public void setLBEDLN(double lBEDLN) {
		LBEDLN = lBEDLN;
	}
	public String getLBEDSP() {
		return LBEDSP;
	}
	public void setLBEDSP(String lBEDSP) {
		LBEDSP = lBEDSP;
	}
	public String getLBTNAC() {
		return LBTNAC;
	}
	public void setLBTNAC(String lBTNAC) {
		LBTNAC = lBTNAC;
	}
	public int getLBAN8() {
		return LBAN8;
	}
	public void setLBAN8(int lBAN8) {
		LBAN8 = lBAN8;
	}
	public String getLBATYPE() {
		return LBATYPE;
	}
	public void setLBATYPE(String lBATYPE) {
		LBATYPE = lBATYPE;
	}
	public int getLBIDLN() {
		return LBIDLN;
	}
	public void setLBIDLN(int lBIDLN) {
		LBIDLN = lBIDLN;
	}
	public String getLBADD1() {
		return LBADD1;
	}
	public void setLBADD1(String lBADD1) {
		LBADD1 = lBADD1;
	}
	
}
