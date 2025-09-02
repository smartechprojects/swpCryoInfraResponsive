package com.eurest.supplier.edi;

//F0101
public class AddressBookInteroperabilityDTO {
	
	private int referenceID;
	private String jdeReference;
	
	private String SZEDUS;
	private String SZEDBT;
	private String SZEDTN;
	private String SZEDLN;
	private String SZEDSP;
	private String SZTNAC; // Default to A
	private String SZMCU;
	private String SZALPH;
	private String SZAT1; //Verify UDC 01/ST for suppliers
	private String SZLNGP; // Verify UDC 01/LP for languages
	private String SZATP; // Default to Y
	private String SZATE; // Default to N
	private String SZEFTB; // Date: Default 1 day after record is created
	private String SZALP1; // Alternative Name
	private String SZADD1;
	private String SZADD2;
	private String SZADDZ;
	private String SZCTY1;
	private String SZCTR; // Ver UDC 00/CN - Countries
	private String SZADDS; // Ver UDC 00/S - States
	private String SZCOUN;
	private String SZAR1; // Default 52
	private String SZPHT1; // Ver UDC 01/PH
	private String SZAR2; // Default 52
	private String SZPH2;
	private String SZPHT2; // Ver UDC 01/PH
	private String SZDRIN;
	private String SZTAX;

	private String SZTORG;
	private String SZUSER;
	private String SZPID;
	private String SZJOBN;
	private String SZUPMJ;
	private int SZTDAY;
	
	private SupplierMasterDTO supplierMasterDTO;
	private WhoIsWhoDTO whoIsWhoDTO;
	private AddressBookPhonesDTO addressBookPhonesDTO;
	private AddessBookElectronicAddressDTO addessBookElectronicAddressDTO;
	private AddressBookAlternativeAddressDTO addressBookAlternativeAddressDTO;
	
	
	public int getReferenceID() {
		return referenceID;
	}
	public void setReferenceID(int referenceID) {
		this.referenceID = referenceID;
	}
	public String getJdeReference() {
		return jdeReference;
	}
	public void setJdeReference(String jdeReference) {
		this.jdeReference = jdeReference;
	}
	public String getSZEDUS() {
		return SZEDUS;
	}
	public void setSZEDUS(String sZEDUS) {
		SZEDUS = sZEDUS;
	}
	public String getSZEDBT() {
		return SZEDBT;
	}
	public void setSZEDBT(String sZEDBT) {
		SZEDBT = sZEDBT;
	}
	public String getSZEDTN() {
		return SZEDTN;
	}
	public void setSZEDTN(String sZEDTN) {
		SZEDTN = sZEDTN;
	}
	public String getSZEDLN() {
		return SZEDLN;
	}
	public void setSZEDLN(String sZEDLN) {
		SZEDLN = sZEDLN;
	}
	public String getSZEDSP() {
		return SZEDSP;
	}
	public void setSZEDSP(String sZEDSP) {
		SZEDSP = sZEDSP;
	}
	public String getSZTNAC() {
		return SZTNAC;
	}
	public void setSZTNAC(String sZTNAC) {
		SZTNAC = sZTNAC;
	}
	public String getSZMCU() {
		return SZMCU;
	}
	public void setSZMCU(String sZMCU) {
		SZMCU = sZMCU;
	}
	public String getSZALPH() {
		return SZALPH;
	}
	public void setSZALPH(String sZALPH) {
		SZALPH = sZALPH;
	}
	public String getSZAT1() {
		return SZAT1;
	}
	public void setSZAT1(String sZAT1) {
		SZAT1 = sZAT1;
	}
	public String getSZLNGP() {
		return SZLNGP;
	}
	public void setSZLNGP(String sZLNGP) {
		SZLNGP = sZLNGP;
	}
	public String getSZATP() {
		return SZATP;
	}
	public void setSZATP(String sZATP) {
		SZATP = sZATP;
	}
	public String getSZATE() {
		return SZATE;
	}
	public void setSZATE(String sZATE) {
		SZATE = sZATE;
	}
	public String getSZEFTB() {
		return SZEFTB;
	}
	public void setSZEFTB(String sZEFTB) {
		SZEFTB = sZEFTB;
	}
	public String getSZALP1() {
		return SZALP1;
	}
	public void setSZALP1(String sZALP1) {
		SZALP1 = sZALP1;
	}
	public String getSZADD1() {
		return SZADD1;
	}
	public void setSZADD1(String sZADD1) {
		SZADD1 = sZADD1;
	}
	public String getSZADD2() {
		return SZADD2;
	}
	public void setSZADD2(String sZADD2) {
		SZADD2 = sZADD2;
	}
	public String getSZADDZ() {
		return SZADDZ;
	}
	public void setSZADDZ(String sZADDZ) {
		SZADDZ = sZADDZ;
	}
	public String getSZCTY1() {
		return SZCTY1;
	}
	public void setSZCTY1(String sZCTY1) {
		SZCTY1 = sZCTY1;
	}
	public String getSZCTR() {
		return SZCTR;
	}
	public void setSZCTR(String sZCTR) {
		SZCTR = sZCTR;
	}
	public String getSZADDS() {
		return SZADDS;
	}
	public void setSZADDS(String sZADDS) {
		SZADDS = sZADDS;
	}
	public String getSZCOUN() {
		return SZCOUN;
	}
	public void setSZCOUN(String sZCOUN) {
		SZCOUN = sZCOUN;
	}
	public String getSZAR1() {
		return SZAR1;
	}
	public void setSZAR1(String sZAR1) {
		SZAR1 = sZAR1;
	}
	public String getSZPHT1() {
		return SZPHT1;
	}
	public void setSZPHT1(String sZPHT1) {
		SZPHT1 = sZPHT1;
	}
	public String getSZAR2() {
		return SZAR2;
	}
	public void setSZAR2(String sZAR2) {
		SZAR2 = sZAR2;
	}
	public String getSZPH2() {
		return SZPH2;
	}
	public void setSZPH2(String sZPH2) {
		SZPH2 = sZPH2;
	}
	public String getSZPHT2() {
		return SZPHT2;
	}
	public void setSZPHT2(String sZPHT2) {
		SZPHT2 = sZPHT2;
	}
	public SupplierMasterDTO getSupplierMasterDTO() {
		return supplierMasterDTO;
	}
	public void setSupplierMasterDTO(SupplierMasterDTO supplierMasterDTO) {
		this.supplierMasterDTO = supplierMasterDTO;
	}
	public WhoIsWhoDTO getWhoIsWhoDTO() {
		return whoIsWhoDTO;
	}
	public void setWhoIsWhoDTO(WhoIsWhoDTO whoIsWhoDTO) {
		this.whoIsWhoDTO = whoIsWhoDTO;
	}
	public AddressBookPhonesDTO getAddressBookPhonesDTO() {
		return addressBookPhonesDTO;
	}
	public void setAddressBookPhonesDTO(AddressBookPhonesDTO addressBookPhonesDTO) {
		this.addressBookPhonesDTO = addressBookPhonesDTO;
	}
	public AddessBookElectronicAddressDTO getAddessBookElectronicAddressDTO() {
		return addessBookElectronicAddressDTO;
	}
	public void setAddessBookElectronicAddressDTO(AddessBookElectronicAddressDTO addessBookElectronicAddressDTO) {
		this.addessBookElectronicAddressDTO = addessBookElectronicAddressDTO;
	}
	public AddressBookAlternativeAddressDTO getAddressBookAlternativeAddressDTO() {
		return addressBookAlternativeAddressDTO;
	}
	public void setAddressBookAlternativeAddressDTO(AddressBookAlternativeAddressDTO addressBookAlternativeAddressDTO) {
		this.addressBookAlternativeAddressDTO = addressBookAlternativeAddressDTO;
	}
	public String getSZTORG() {
		return SZTORG;
	}
	public void setSZTORG(String sZTORG) {
		SZTORG = sZTORG;
	}
	public String getSZUSER() {
		return SZUSER;
	}
	public void setSZUSER(String sZUSER) {
		SZUSER = sZUSER;
	}
	public String getSZPID() {
		return SZPID;
	}
	public void setSZPID(String sZPID) {
		SZPID = sZPID;
	}
	public String getSZJOBN() {
		return SZJOBN;
	}
	public void setSZJOBN(String sZJOBN) {
		SZJOBN = sZJOBN;
	}
	public String getSZUPMJ() {
		return SZUPMJ;
	}
	public void setSZUPMJ(String sZUPMJ) {
		SZUPMJ = sZUPMJ;
	}
	public int getSZTDAY() {
		return SZTDAY;
	}
	public void setSZTDAY(int sZTDAY) {
		SZTDAY = sZTDAY;
	}
	public String getSZDRIN() {
		return SZDRIN;
	}
	public void setSZDRIN(String sZDRIN) {
		SZDRIN = sZDRIN;
	}
	public String getSZTAX() {
		return SZTAX;
	}
	public void setSZTAX(String sZTAX) {
		SZTAX = sZTAX;
	}
	

}
