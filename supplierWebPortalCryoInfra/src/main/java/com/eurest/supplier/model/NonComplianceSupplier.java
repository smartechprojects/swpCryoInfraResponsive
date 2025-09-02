package com.eurest.supplier.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="noncompliancesupplier")
public class NonComplianceSupplier {
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String lastUpdate;
	private String lineNumber;
	private String taxId;
	private String supplierName;
	private String status;
	private String refDate1;
	private String refDate2;
	private String refDate3;
	private String refDate4;
	private String refDate5;
	private String refDate6;
	private String refDate7;
	private String refDate8;
	private String refDate9;
	private String refDate10;
	private String refDate11;
	private String refDate12;
	private String refDate13;
	private String refDate14;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRefDate1() {
		return refDate1;
	}
	public void setRefDate1(String refDate1) {
		this.refDate1 = refDate1;
	}
	public String getRefDate2() {
		return refDate2;
	}
	public void setRefDate2(String refDate2) {
		this.refDate2 = refDate2;
	}
	public String getRefDate3() {
		return refDate3;
	}
	public void setRefDate3(String refDate3) {
		this.refDate3 = refDate3;
	}
	public String getRefDate4() {
		return refDate4;
	}
	public void setRefDate4(String refDate4) {
		this.refDate4 = refDate4;
	}
	public String getRefDate5() {
		return refDate5;
	}
	public void setRefDate5(String refDate5) {
		this.refDate5 = refDate5;
	}
	public String getRefDate6() {
		return refDate6;
	}
	public void setRefDate6(String refDate6) {
		this.refDate6 = refDate6;
	}
	public String getRefDate7() {
		return refDate7;
	}
	public void setRefDate7(String refDate7) {
		this.refDate7 = refDate7;
	}
	public String getRefDate8() {
		return refDate8;
	}
	public void setRefDate8(String refDate8) {
		this.refDate8 = refDate8;
	}
	public String getRefDate9() {
		return refDate9;
	}
	public void setRefDate9(String refDate9) {
		this.refDate9 = refDate9;
	}
	public String getRefDate10() {
		return refDate10;
	}
	public void setRefDate10(String refDate10) {
		this.refDate10 = refDate10;
	}
	public String getRefDate11() {
		return refDate11;
	}
	public void setRefDate11(String refDate11) {
		this.refDate11 = refDate11;
	}
	public String getRefDate12() {
		return refDate12;
	}
	public void setRefDate12(String refDate12) {
		this.refDate12 = refDate12;
	}
	public String getRefDate13() {
		return refDate13;
	}
	public void setRefDate13(String refDate13) {
		this.refDate13 = refDate13;
	}
	public String getRefDate14() {
		return refDate14;
	}
	public void setRefDate14(String refDate14) {
		this.refDate14 = refDate14;
	}

}
