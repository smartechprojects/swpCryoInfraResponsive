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

import com.eurest.supplier.model.CodigosSAT;
import com.eurest.supplier.service.CodigosSATService;

@Controller
public class CodigosSatController {
	
	@Autowired
	private CodigosSATService codigosSatService;
	
	Logger log4j = Logger.getLogger(CodigosSatController.class);
	
	@RequestMapping(value ="/codsat/view.action")
	public @ResponseBody Map<String, Object> view(@RequestParam int start,
												  @RequestParam int limit,
												  HttpServletRequest request){	
		List<CodigosSAT> list=null;
		int total=0;
		try{
				list = codigosSatService.getList(start, limit);
				total = codigosSatService.getTotalRecords();

		    return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
		
	@RequestMapping(value ="/codsat/searchByCriteria.action")
	public @ResponseBody Map<String, Object> getByCode(@RequestParam int start,
													   @RequestParam int limit,
													   @RequestParam String query,
													   HttpServletRequest request){	
		List<CodigosSAT> list=null;
		int total=0;
		try{
				list = codigosSatService.searchByCriteria(query, start, limit);
				total =((Long)codigosSatService.searchByCriteriaTotalRecords(query)).intValue();
		    return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	public Map<String,Object> mapOK(List<CodigosSAT> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String,Object> mapOK(CodigosSAT obj){
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
