package com.eurest.supplier.test;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.eurest.supplier.service.MiddlewareService;

public class RunWS {
	
	  static MiddlewareService middlewareService = new MiddlewareService();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, Object> response = middlewareService.getPostVoucher(0,3);
	}

}
