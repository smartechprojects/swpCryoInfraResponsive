package com.eurest.supplier.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eurest.supplier.model.Company;
import com.eurest.supplier.service.CompanyService;
import com.eurest.supplier.service.OutSourcingService;
import com.eurest.supplier.service.UsersService;
import com.eurest.supplier.util.FileUploadBean;

import net.sf.json.JSONObject;

@Controller
public class CompanyController {

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	OutSourcingService outSourcingService;
	
	@Autowired
	UsersService userService;
	
	Logger log4j = Logger.getLogger(CompanyController.class);

	@RequestMapping(value = "company/searchByQuery.action")
	public @ResponseBody Map<String, Object> searchUser(@RequestParam String query, @RequestParam int start,
			@RequestParam int limit) {

		try {
			List<Company> list = companyService.searchCriteria(query, start, limit);
			int total = companyService.searchCriteriaCount(query);
			return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}

	@RequestMapping(value = "/admin/company/searchActiveCompanies.action")
	public @ResponseBody Map<String, Object> searchActiveCompanies() {

		try {
			//List<Company> list = companyService.searchActiveCompanies(getUserCompany());
			List<Company> list = null;
			int total = list.size();
			return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/admin/company/getListCompany.action")
	public @ResponseBody Map<String, Object> getListCompany() {

		try {
			List<Company> list = companyService.getListCompany();
			int total = list.size();
			return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}

	@RequestMapping(value = "/admin/company/save.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> save(@RequestBody Company o) {

		try {

			Company c = companyService.saveCompany(o);
			if(c != null) {
				return mapOK(c);
			}else {
				return mapError("La compañía ya existe previamente");
			}

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}

	@RequestMapping(value = "/admin/company/update.action")
	public @ResponseBody Map<String, Object> update(@RequestBody Company o) {

		try {
			return mapOK(companyService.updateCompany(o));
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/uploadCompanyFile.action", method = RequestMethod.POST)
    @ResponseBody public String uploadCompanyFile(FileUploadBean uploadItem, 
			    								   BindingResult result, 
			    								   String company, 
			    								   String documentType, 
			    		                           HttpServletResponse response){
 
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();

        try{

	        if (result.hasErrors()){
	            for(ObjectError error : result.getAllErrors()){
	                System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
	            }
	 
	            json.put("success", false);
	            json.put("message", "Error al cargar el archivo");
	        }
	        
	        String ct = uploadItem.getFile().getContentType();
	        if("PKCS12".equals(documentType) && !"application/x-pkcs12".equals(ct)) {
	        	json.put("success", false);
	        	json.put("message", "Error: El archivo de firma encriptada no es del tipo correcto. Seleccione archivos .pfx provenientes de la firma electrónica");
	        	return json.toString();
	        }

	        if("LOGO".equals(documentType)) {
	            if(!"image/jpg".equals(ct.trim()) && !"image/jpeg".equals(ct.trim())){
	            	json.put("success", false);
	            	json.put("message", "Error: El archivo de imágen debe ser tipo JPG o JPEG");
	            	return json.toString();
	            }
	        }
	
			outSourcingService.saveCompanyDocument(uploadItem, documentType, company);
	        json.put("success", true);
	        json.put("message", "El archivo " + uploadItem.getFile().getOriginalFilename() + " ha sido cargado exitosamente.");
	        json.put("fileName", uploadItem.getFile().getOriginalFilename());
	        return json.toString();
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();

        }
        return json.toString();

    }

	public Map<String, Object> mapOK(List<Company> list, int total) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String, Object> mapOK(Company obj) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", 1);
		modelMap.put("data", obj);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String, Object> mapError(String msg) {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		return modelMap;
	}

	public Map<String, Object> mapStrOk(String msg) {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", true);
		return modelMap;
	}


}
