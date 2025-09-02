package com.eurest.supplier.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@JsonAutoDetect
@Entity
@Table(name="exchangerate")
public class ExchangeRate {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String currencyCode;
	private String currencyCodeTo;
	private double equivalence; 
	private double exchangeRate;	
	private int year;
	private int month;
	private int day;
	private int jdeUpdatedTime;
	private String jdeUpdatedBy;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date effectiveDate;	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date jdeUpdatedDate;	
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date updatedDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyCodeTo() {
		return currencyCodeTo;
	}
	public void setCurrencyCodeTo(String currencyCodeTo) {
		this.currencyCodeTo = currencyCodeTo;
	}
	public double getEquivalence() {
		return equivalence;
	}
	public void setEquivalence(double equivalence) {
		this.equivalence = equivalence;
	}
	public double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getJdeUpdatedTime() {
		return jdeUpdatedTime;
	}
	public void setJdeUpdatedTime(int jdeUpdatedTime) {
		this.jdeUpdatedTime = jdeUpdatedTime;
	}
	public String getJdeUpdatedBy() {
		return jdeUpdatedBy;
	}
	public void setJdeUpdatedBy(String jdeUpdatedBy) {
		this.jdeUpdatedBy = jdeUpdatedBy;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getJdeUpdatedDate() {
		return jdeUpdatedDate;
	}
	public void setJdeUpdatedDate(Date jdeUpdatedDate) {
		this.jdeUpdatedDate = jdeUpdatedDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
