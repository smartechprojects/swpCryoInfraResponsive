package com.eurest.supplier.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name="tolerances")
public class Tolerances {
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private static final long serialVersionUID = 1L;
	private String type;
	private int itemNumber;
	private String company;
	private String commodityClass;
	private String commodityCode;
	private double qtyPercentage;
	private double qtyUnits;
	private double unitCostPercentage;
	private double unitCostAmount;
	private double extendedAmtPercentage;
	private double extendedAmtAmount;
	private String currencyCode;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCommodityClass() {
		return commodityClass;
	}

	public void setCommodityClass(String commodityClass) {
		this.commodityClass = commodityClass;
	}

	public String getCommodityCode() {
		return commodityCode;
	}

	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}

	public double getQtyPercentage() {
		return qtyPercentage;
	}

	public void setQtyPercentage(double qtyPercentage) {
		this.qtyPercentage = qtyPercentage;
	}

	public double getQtyUnits() {
		return qtyUnits;
	}

	public void setQtyUnits(double qtyUnits) {
		this.qtyUnits = qtyUnits;
	}

	public double getUnitCostPercentage() {
		return unitCostPercentage;
	}

	public void setUnitCostPercentage(double unitCostPercentage) {
		this.unitCostPercentage = unitCostPercentage;
	}

	public double getUnitCostAmount() {
		return unitCostAmount;
	}

	public void setUnitCostAmount(double unitCostAmount) {
		this.unitCostAmount = unitCostAmount;
	}

	public double getExtendedAmtPercentage() {
		return extendedAmtPercentage;
	}

	public void setExtendedAmtPercentage(double extendedAmtPercentage) {
		this.extendedAmtPercentage = extendedAmtPercentage;
	}

	public double getExtendedAmtAmount() {
		return extendedAmtAmount;
	}

	public void setExtendedAmtAmount(double extendedAmtAmount) {
		this.extendedAmtAmount = extendedAmtAmount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
