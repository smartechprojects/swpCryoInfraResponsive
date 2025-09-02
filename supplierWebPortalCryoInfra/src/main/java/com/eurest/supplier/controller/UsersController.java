package com.eurest.supplier.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eurest.supplier.model.PlantAccessRequest;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.service.UsersService;
import com.eurest.supplier.util.AppConstants;

import net.sf.json.JSONObject;


@Controller
public class UsersController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private UdcService udcService;
	
	Logger log4j = Logger.getLogger(UsersController.class);
	
	@RequestMapping(value ="/admin/users/view.action")
	public @ResponseBody Map<String, Object> view(@RequestParam int start,
												  @RequestParam int limit,
												  @RequestParam String query,
												  HttpServletRequest request){	
		List<Users> list=null;
		int total=0;
		try{
			if("".equals(query)){
				list = usersService.getUsersList(start, limit);
				total = usersService.getTotalRecords();
			}else{
				list = usersService.searchCriteria(query);
				total = list.size();
			}
			
			for(Users u : list) {
				String pass = u.getPassword().substring(6);
				byte [] barr = Base64.getDecoder().decode(pass); 
				u.setPassword(new String(barr));
			}
			
		    return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
		
	}

	@RequestMapping(value ="/admin/users/viewByRole.action")
	public @ResponseBody Map<String, Object> viewByRole(@RequestParam int start,
												  @RequestParam int limit,
												  @RequestParam String query){	
		List<Users> list=null;
		int total=0;
		try{
				if(!"".equals(query)){
					list = usersService.searchCriteriaByRole(query);
					total = list.size();
					return mapOK(list, total);
				}else{
					return mapOK(list, 0);
				}
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/admin/users/searchByRole.action")
	public @ResponseBody Map<String, Object> searchByRole(@RequestParam String role){	
		List<Users> list=null;
		int total=0;
		try{
				if(!"".equals(role)){
					list = usersService.searchCriteriaByRole(role);
					total = list.size();
					return mapOK(list, total);
				}else{
					return mapOK(list, 0);
				}
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/admin/users/searchByRoleExclude.action")
	public @ResponseBody Map<String, Object> searchByRoleExclude(@RequestParam String role){	
		List<Users> list=null;
		int total=0;
		try{
				if(!"".equals(role)){
					list = usersService.searchCriteriaByRoleExclude(role);
					total = list.size();
					return mapOK(list, total);
				}else{
					return mapOK(list, 0);
				}
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/admin/users/searchUser.action")
	public @ResponseBody Map<String, Object> searchUser(@RequestParam String user){	

		try{
			Users e = usersService.searchCriteriaUserName(user);				
		    return mapOK(e);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
		
	}
	
	@RequestMapping(value ="/admin/users/validateUser.action")
	public @ResponseBody Map<String, Object> validateUser(@RequestBody Users user){	

		try{
			String validationMessage = usersService.validateUser(user);
			
			if("".equals(validationMessage)) {
				return mapStrOk("OK");
			} else {
				return mapError(validationMessage);
			}
		    
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
		
	}
	
	@RequestMapping(value ="/admin/users/save.action", produces={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody public String save(@RequestBody Users obj,
									HttpServletResponse response){
		
		try{
			String validationMessage = usersService.validateUser(obj);
			if("".equals(validationMessage)) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				String usr = auth.getName();
				usersService.save(obj,new Date(),usr);
			}			
			return mapJsonStrOk(validationMessage);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapJsonStrOk(e.getMessage());
		}
	}

	@RequestMapping(value ="/admin/users/update.action", produces={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody public String update(@RequestBody Users obj,
			HttpServletResponse response){
		
		try{
			String validationMessage = usersService.validateUser(obj);
			if("".equals(validationMessage)) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				String usr = auth.getName();
				usersService.update(obj,new Date(),usr);
			}
			return mapJsonStrOk(validationMessage);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapJsonStrOk(e.getMessage());
		}
	}

	@RequestMapping(value ="/admin/users/delete.action")
	public @ResponseBody Map<String, Object>  delete(@RequestBody Users obj){

		try{ 
			usersService.delete(obj.getId());
			return mapOK(new Users());
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/admin/users/searchByRoleAprover.action")
	public @ResponseBody Map<String, Object> searchByRoleAprover(@RequestParam String role,@RequestParam String etapa){	
		List<Users> list=null;
		int total=0;
		try{
			etapa=etapa+"_APPROVER";
			 List<UDC> udcs = udcService.searchListBySystemAndKey(role, etapa);
			 List<String> usernames=new ArrayList<String>();
			 
			 for (UDC use : udcs) {
				usernames.add(use.getStrValue1());
			}
			 
			
				if(!"".equals(role)){
					list = usersService.getListUsersByUsername(usernames);
					total = list.size();
					return mapOK(list, total);
				}else{
					return mapOK(list, 0);
				}
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	


	public Map<String,Object> mapOK(List<Users> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String,Object> mapOK(Users obj){
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

	public String mapJsonStrOk(String msg){
		JSONObject json = new JSONObject();
		json.put("message", msg);
		json.put("success", true);
		return json.toString();
	} 

}
