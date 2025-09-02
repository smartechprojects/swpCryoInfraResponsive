package com.eurest.supplier.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "nextnumber")
public class NextNumber implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String module;
	private String nextDate;
	private int nexInt;
	private String nextStr;
	private int nextAddNum;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getNextDate() {
		return nextDate;
	}
	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}
	public int getNexInt() {
		return nexInt;
	}
	public void setNexInt(int nexInt) {
		this.nexInt = nexInt;
	}
	public String getNextStr() {
		return nextStr;
	}
	public void setNextStr(String nextStr) {
		this.nextStr = nextStr;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getNextAddNum() {
		return nextAddNum;
	}
	public void setNextAddNum(int nextAddNum) {
		this.nextAddNum = nextAddNum;
	}
	
	
}
