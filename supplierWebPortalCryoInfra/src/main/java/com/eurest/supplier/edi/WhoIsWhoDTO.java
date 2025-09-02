package com.eurest.supplier.edi;

//F0111
public class WhoIsWhoDTO {

	private String CBEDUS;
	private String CBEDBT;
	private String CBEDTN;
	private double CBEDLN;
	private String CBEDSP;
	private String CBTNAC; //Default to A
	private String CBALPH;
	private int CBAN8;
	private int CBIDLN; // Default to addressbook but just only 5 digits
	private String CBMLNM; // Deafult name of AddressBook
	
	public String getCBEDUS() {
		return CBEDUS;
	}
	public void setCBEDUS(String cBEDUS) {
		CBEDUS = cBEDUS;
	}
	public String getCBEDBT() {
		return CBEDBT;
	}
	public void setCBEDBT(String cBEDBT) {
		CBEDBT = cBEDBT;
	}
	public String getCBEDTN() {
		return CBEDTN;
	}
	public void setCBEDTN(String cBEDTN) {
		CBEDTN = cBEDTN;
	}
	public double getCBEDLN() {
		return CBEDLN;
	}
	public void setCBEDLN(double cBEDLN) {
		CBEDLN = cBEDLN;
	}
	public String getCBEDSP() {
		return CBEDSP;
	}
	public void setCBEDSP(String cBEDSP) {
		CBEDSP = cBEDSP;
	}
	public String getCBTNAC() {
		return CBTNAC;
	}
	public void setCBTNAC(String cBTNAC) {
		CBTNAC = cBTNAC;
	}
	public String getCBALPH() {
		return CBALPH;
	}
	public void setCBALPH(String cBALPH) {
		CBALPH = cBALPH;
	}
	public int getCBAN8() {
		return CBAN8;
	}
	public void setCBAN8(int cBAN8) {
		CBAN8 = cBAN8;
	}
	public int getCBIDLN() {
		return CBIDLN;
	}
	public void setCBIDLN(int cBIDLN) {
		CBIDLN = cBIDLN;
	}
	public String getCBMLNM() {
		return CBMLNM;
	}
	public void setCBMLNM(String cBMLNM) {
		CBMLNM = cBMLNM;
	}
	
}
