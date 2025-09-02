package com.eurest.supplier.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eurest.supplier.model.NonComplianceSupplier;
import com.eurest.supplier.service.NonComplianceSupplierService;

@Controller
public class NonComplianceSupplierController {
	
	@Autowired
	private NonComplianceSupplierService nonComplianceSupplierService;
	
	Logger log4j = Logger.getLogger(NonComplianceSupplierController.class);
	
	@RequestMapping(value ="/noncompliancesupplier/view.action")
	public @ResponseBody Map<String, Object> view(@RequestParam int start,
												  @RequestParam int limit,
												  HttpServletRequest request){	
		List<NonComplianceSupplier> list=null;
		int total=0;
		try{
				list = nonComplianceSupplierService.getList(start, limit);
				total = nonComplianceSupplierService.getTotalRecords();

		    return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/noncompliancesupplier/getById.action")
	public @ResponseBody Map<String, Object> getById(@RequestParam int start,
													  @RequestParam int limit,
													  @RequestParam int id,
													  HttpServletRequest request){	
		NonComplianceSupplier sup = null;
		try{
				sup = nonComplianceSupplierService.getNonComplianceSupplierById(id);
		    return mapOK(sup);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/noncompliancesupplier/searchByCriteria.action")
	public @ResponseBody Map<String, Object> getByName(@RequestParam int start,
													   @RequestParam int limit,
													   @RequestParam String query,
													   HttpServletRequest request){	
		List<NonComplianceSupplier> list=null;
		int total=0;
		try{
				list = nonComplianceSupplierService.searchByCriteria(query, start, limit);
				total =((Long)nonComplianceSupplierService.searchByCriteriaTotalRecords(query)).intValue();
		    return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	
	
	public Map<String,Object> mapOK(List<NonComplianceSupplier> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String,Object> mapOK(NonComplianceSupplier obj){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", 1);
		modelMap.put("data", obj);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String,Object> mapError(String msg){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		return modelMap;
	} 

	
	}
