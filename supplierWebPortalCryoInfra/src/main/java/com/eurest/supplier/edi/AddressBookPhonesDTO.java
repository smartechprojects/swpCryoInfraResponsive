package com.eurest.supplier.edi;

//F0115
public class AddressBookPhonesDTO {
	
	private String PIEDUS;
	private String PIEDBT;
	private String PIEDTN;
	private double PIEDLN;
	private String PIEDTL;
	private String PITNAC; //Defaulvaluet to A
	private String PIEDSP;
	private String PIAN8;
	private String PIPH1;
	private int LBIDLN; // Default to addressbook but just only 5 digits
	
	public String getPIEDUS() {
		return PIEDUS;
	}
	public void setPIEDUS(String pIEDUS) {
		PIEDUS = pIEDUS;
	}
	public String getPIEDBT() {
		return PIEDBT;
	}
	public void setPIEDBT(String pIEDBT) {
		PIEDBT = pIEDBT;
	}
	public String getPIEDTN() {
		return PIEDTN;
	}
	public void setPIEDTN(String pIEDTN) {
		PIEDTN = pIEDTN;
	}
	public double getPIEDLN() {
		return PIEDLN;
	}
	public void setPIEDLN(double pIEDLN) {
		PIEDLN = pIEDLN;
	}
	public String getPIEDTL() {
		return PIEDTL;
	}
	public void setPIEDTL(String pIEDTL) {
		PIEDTL = pIEDTL;
	}
	public String getPITNAC() {
		return PITNAC;
	}
	public void setPITNAC(String pITNAC) {
		PITNAC = pITNAC;
	}
	public String getPIEDSP() {
		return PIEDSP;
	}
	public void setPIEDSP(String pIEDSP) {
		PIEDSP = pIEDSP;
	}
	public String getPIAN8() {
		return PIAN8;
	}
	public void setPIAN8(String pIAN8) {
		PIAN8 = pIAN8;
	}
	public String getPIPH1() {
		return PIPH1;
	}
	public void setPIPH1(String pIPH1) {
		PIPH1 = pIPH1;
	}
	public int getLBIDLN() {
		return LBIDLN;
	}
	public void setLBIDLN(int lBIDLN) {
		LBIDLN = lBIDLN;
	}
	
	

}
