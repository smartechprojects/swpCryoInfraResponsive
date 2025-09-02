package com.eurest.supplier.dto;

import java.util.List;

public class IntegerListDTO {

	private List<String> orderList;
	private List<Integer> supList;
	private List<String> uuidList;
	
	public List<String> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<String> orderList) {
		this.orderList = orderList;
	}
	public List<Integer> getSupList() {
		return supList;
	}
	public void setSupList(List<Integer> supList) {
		this.supList = supList;
	}
	public List<String> getUuidList() {
		return uuidList;
	}
	public void setUuidList(List<String> uuidList) {
		this.uuidList = uuidList;
	}
	
}
