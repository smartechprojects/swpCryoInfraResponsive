package com.eurest.supplier.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eurest.supplier.dto.SupplierDTO;
import com.eurest.supplier.service.ApprovalService;

@Controller
public class ApprovalController {
	
	@Autowired
	private ApprovalService approvalService;
	
	Logger log4j = Logger.getLogger(ApprovalController.class);
	
	@RequestMapping(value ="/approval/view.action")
	public @ResponseBody Map<String, Object> view(@RequestParam int start,
												  @RequestParam int limit,
												  @RequestParam String currentApprover,
												  @RequestParam String status,
			                                      @RequestParam String step,
			                                      @RequestParam String notes,
												  HttpServletRequest request){	
		List<SupplierDTO> list=null;
		int total=0;
		try{
				list = approvalService.getPendingApproval(currentApprover, start, limit);
				total = approvalService.getPendingApprovalTotal(currentApprover);

		    return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
		
	}
	
	@RequestMapping(value ="/approval/search.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> search(  @RequestParam String ticketId,
													@RequestParam String approvalStep,
													@RequestParam String approvalStatus,
													@RequestParam String fechaAprobacion,
													@RequestParam String currentApprover,
													@RequestParam String name,
													@RequestParam int start,
													@RequestParam int limit){	
		List<SupplierDTO> list=null;
		int total=0;
		try{
			    
			    Date requestDate = null;
			    if(!"".equals(fechaAprobacion)) {
			    	requestDate = new SimpleDateFormat("dd/MM/yyyy").parse(fechaAprobacion);  
			    }
				list = approvalService.searchApproval(ticketId, approvalStep, approvalStatus, requestDate, currentApprover, name, start, limit);
				total = approvalService.searchApprovalTotal(ticketId, approvalStep, approvalStatus, requestDate, currentApprover, name);

		    return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
		
	}

	@RequestMapping(value ="/approval/update.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> update(@RequestBody SupplierDTO obj,
													@RequestParam String currentApprover,
			                                        @RequestParam String status,
			                                        @RequestParam String step,
			                                        @RequestParam String notes){
		
		try{
			String msg = approvalService.updateSupplier(obj.getId(), status, step, notes);
			return mapStrOk(msg);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}

	@RequestMapping(value ="/approval/reasignApprover.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> update(@RequestParam int id,
			                                        @RequestParam String newApprover){
		
		try{
			String msg = approvalService.reasignApprover(id, newApprover);
			return mapStrOk(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/approval/reasignApproverFletes.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> reasignApproverFletes(@RequestParam int id,
			                                        @RequestParam String newApprover,
			                                        @RequestParam String etapa){
		
		try{
			String msg = approvalService.reasignApproverFletes(id, newApprover,etapa);
			return mapStrOk(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	public Map<String,Object> mapOK(List<SupplierDTO> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String,Object> mapOK(SupplierDTO obj){
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

	public Map<String,Object> mapStrOk(String msg){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", true);
		return modelMap;
	} 

	}
