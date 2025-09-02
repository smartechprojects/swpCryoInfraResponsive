package com.eurest.supplier.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eurest.supplier.model.UDC;
import com.eurest.supplier.service.UdcService;


@Controller
public class UdcController {
	
@Autowired
private UdcService udcService;

Logger log4j = Logger.getLogger(UdcController.class);

@RequestMapping(value ="/admin/udc/view.action")
public @ResponseBody Map<String, Object> view(@RequestParam int start,
											  @RequestParam int limit,
											  @RequestParam String query,
											  @RequestParam String udcSystem,
											  @RequestParam String udcKey,
											  @RequestParam String strValue1,
										   	  @RequestParam String strValue2){	
	List<UDC> udcs=null;
	int total=0;
	
	try{
		if ("".equals(udcSystem)&&"".equals(udcKey)&&"".equals(strValue1)&&"".equals(strValue2)){					
			if("".equals(query)){
				udcs = udcService.getUDCList(start, limit);
				total = udcService.getTotalRecords();			
			}else{
				 udcs= udcService.searchCriteria(query);
				 total = udcs.size();						
			}
		}else{
			udcs=udcService.advaceSearch(udcSystem, udcKey, strValue1, strValue2);
			total = udcs.size();	
		}
		return mapOK(udcs, total);
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return mapError(e.getMessage());
	}
	
}

@RequestMapping(value ="/admin/udc/searchSystemAndKey.action", method = RequestMethod.GET)
public @ResponseBody Map<String, Object> searchSystemAndKey(
															@RequestParam String query,
															@RequestParam String udcSystem,
															@RequestParam String udcKey,
															@RequestParam String systemRef,
															@RequestParam String keyRef,
															@RequestParam int page, 
															@RequestParam int start, 
															@RequestParam int limit){
	
	try{
		List<UDC> udcs= udcService.advaceSearch(udcSystem, udcKey, systemRef, keyRef); 
		return mapOK(udcs, udcs.size());
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return mapError(e.getMessage());
	}
}

@RequestMapping(value ="/public/searchSystemAndKey.action", method = RequestMethod.GET)
public @ResponseBody Map<String, Object> publicSearchSystemAndKey(
															@RequestParam String query,
															@RequestParam String udcSystem,
															@RequestParam String udcKey,
															@RequestParam String systemRef,
															@RequestParam String keyRef,
															@RequestParam int page, 
															@RequestParam int start, 
															@RequestParam int limit){
	
	try{
		List<UDC> udcs= udcService.advaceSearch(udcSystem, udcKey, systemRef, keyRef); 
		return mapOK(udcs, udcs.size());
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return mapError(e.getMessage());
	}
}

@RequestMapping(value ="/public/searchSystem.action", method = RequestMethod.POST)
public @ResponseBody Map<String, Object> publicSearchSystem(
															@RequestParam String udcSystem){
	
	try{
		List<UDC> udcs= udcService.advaceSearch(udcSystem, "", "", ""); 
		return mapOK(udcs, udcs.size());
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return mapError(e.getMessage());
	}
}

@RequestMapping(value ="/admin/udc/save.action", method = RequestMethod.POST)
public @ResponseBody Map<String, Object> save(@RequestBody UDC udc){
	
	try{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String usr = auth.getName();
		udcService.save(udc,new Date(),usr);
		return mapOK(new UDC());
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return mapError(e.getMessage());
	}
}

@RequestMapping(value ="/admin/udc/update.action")
public @ResponseBody Map<String, Object> update(@RequestBody UDC udc){
	
	try{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String usr = auth.getName();
		udcService.update(udc,new Date(),usr);
		return mapOK(new UDC());
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return mapError(e.getMessage());
	}
}

@RequestMapping(value ="/admin/udc/delete.action")
public @ResponseBody Map<String, Object>  delete(@RequestBody UDC udc){

	try{ 
		udcService.delete(udc.getId());
		return mapOK(new UDC());
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return mapError(e.getMessage());
	}
}


public Map<String,Object> mapOK(List<UDC> udcs, int total){
	Map<String,Object> modelMap = new HashMap<String,Object>(3);
	modelMap.put("total", total);
	modelMap.put("data", udcs);
	modelMap.put("success", true);
	return modelMap;
}

public Map<String,Object> mapOK(UDC udc){
	Map<String,Object> modelMap = new HashMap<String,Object>(3);
	modelMap.put("total", 1);
	modelMap.put("data", udc);
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
