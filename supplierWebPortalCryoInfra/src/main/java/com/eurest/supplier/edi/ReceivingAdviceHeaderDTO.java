package com.eurest.supplier.edi;

import java.util.List;

public class ReceivingAdviceHeaderDTO {
	
	private String SYEDTY; // Default 1
	private int SYEDSQ; // Default 1
	
	//Generic EDI transaction
	private String SYEDST; // UDC (40 ST), default = 861
	private String SYEDER; // Ver UDC (H00 ED), default = R
	private int SYEDDL;
	private String SYTPUR; //Ver UDC 47/PU
	private String SYRATY; //Default = 1
	
	//JDE Purchase Order
	private int SYEDOC;
	private String SYEDCT;
	private String SYEKCO;
	
	private int SYDOCO;
	private String SYDCTO;
	private String SYKCOO;
	private String SYSFXO;

	private int SYAN8;
	private int SYSHAN;
	private String SYMCU;
	
	private String TORG;
	private String USER;
	private String PID;
	private String JOBN;
	private String UPMJ;
	private int TDAY;
	
	private List<ReceivingAdviceDetailDTO> receivingAdviceDetailDTO;

	public String getSYEDTY() {
		return SYEDTY;
	}

	public void setSYEDTY(String sYEDTY) {
		SYEDTY = sYEDTY;
	}

	public int getSYEDSQ() {
		return SYEDSQ;
	}

	public void setSYEDSQ(int sYEDSQ) {
		SYEDSQ = sYEDSQ;
	}

	public String getSYEDST() {
		return SYEDST;
	}

	public void setSYEDST(String sYEDST) {
		SYEDST = sYEDST;
	}

	public String getSYEDER() {
		return SYEDER;
	}

	public void setSYEDER(String sYEDER) {
		SYEDER = sYEDER;
	}

	public int getSYEDDL() {
		return SYEDDL;
	}

	public void setSYEDDL(int sYEDDL) {
		SYEDDL = sYEDDL;
	}

	public String getSYTPUR() {
		return SYTPUR;
	}

	public void setSYTPUR(String sYTPUR) {
		SYTPUR = sYTPUR;
	}

	public String getSYRATY() {
		return SYRATY;
	}

	public void setSYRATY(String sYRATY) {
		SYRATY = sYRATY;
	}

	public int getSYEDOC() {
		return SYEDOC;
	}

	public void setSYEDOC(int sYEDOC) {
		SYEDOC = sYEDOC;
	}

	public String getSYEDCT() {
		return SYEDCT;
	}

	public void setSYEDCT(String sYEDCT) {
		SYEDCT = sYEDCT;
	}

	public String getSYEKCO() {
		return SYEKCO;
	}

	public void setSYEKCO(String sYEKCO) {
		SYEKCO = sYEKCO;
	}

	public int getSYDOCO() {
		return SYDOCO;
	}

	public void setSYDOCO(int sYDOCO) {
		SYDOCO = sYDOCO;
	}

	public String getSYDCTO() {
		return SYDCTO;
	}

	public void setSYDCTO(String sYDCTO) {
		SYDCTO = sYDCTO;
	}

	public String getSYKCOO() {
		return SYKCOO;
	}

	public void setSYKCOO(String sYKCOO) {
		SYKCOO = sYKCOO;
	}

	public String getSYSFXO() {
		return SYSFXO;
	}

	public void setSYSFXO(String sYSFXO) {
		SYSFXO = sYSFXO;
	}

	public List<ReceivingAdviceDetailDTO> getReceivingAdviceDetailDTO() {
		return receivingAdviceDetailDTO;
	}

	public void setReceivingAdviceDetailDTO(List<ReceivingAdviceDetailDTO> receivingAdviceDetailDTO) {
		this.receivingAdviceDetailDTO = receivingAdviceDetailDTO;
	}

	public int getSYAN8() {
		return SYAN8;
	}

	public void setSYAN8(int sYAN8) {
		SYAN8 = sYAN8;
	}

	public int getSYSHAN() {
		return SYSHAN;
	}

	public void setSYSHAN(int sYSHAN) {
		SYSHAN = sYSHAN;
	}

	public String getSYMCU() {
		return SYMCU;
	}

	public void setSYMCU(String sYMCU) {
		SYMCU = sYMCU;
	}

	public String getTORG() {
		return TORG;
	}

	public void setTORG(String tORG) {
		TORG = tORG;
	}

	public String getUSER() {
		return USER;
	}

	public void setUSER(String uSER) {
		USER = uSER;
	}

	public String getPID() {
		return PID;
	}

	public void setPID(String pID) {
		PID = pID;
	}

	public String getJOBN() {
		return JOBN;
	}

	public void setJOBN(String jOBN) {
		JOBN = jOBN;
	}

	public String getUPMJ() {
		return UPMJ;
	}

	public void setUPMJ(String uPMJ) {
		UPMJ = uPMJ;
	}

	public int getTDAY() {
		return TDAY;
	}

	public void setTDAY(int tDAY) {
		TDAY = tDAY;
	}

}
