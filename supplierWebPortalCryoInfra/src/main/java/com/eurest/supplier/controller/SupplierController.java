package com.eurest.supplier.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eurest.supplier.dao.SupplierDao;
import com.eurest.supplier.dto.SupplierDTO;
import com.eurest.supplier.model.AccessTokenRegister;
import com.eurest.supplier.model.CodigosPostales;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.service.JDERestService;
import com.eurest.supplier.service.SupplierService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.service.UsersService;
import com.eurest.supplier.util.AppConstants;

@Controller
public class SupplierController {
  @Autowired
  private SupplierService supplierService;
  
  @Autowired
  JDERestService jDERestService;
  
  @Autowired
  SupplierDao supplierDao;
  
  @Autowired
  UsersService userService;
  
  @Autowired
  UdcService udcService;
  
  private Logger log4j = Logger.getLogger(SupplierController.class);
  
  @RequestMapping({"/supplier/view.action"})
  @ResponseBody
  public Map<String, Object> view(@RequestParam int start, @RequestParam int limit, HttpServletRequest request) {
    List<SupplierDTO> list = null;
    int total = 0;
    try {
      list = this.supplierService.getList(start, limit);
      total = list.size();
      return mapOK(list, total);
    } catch (Exception e) {
      log4j.error("Exception" , e);
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  @RequestMapping({"/supplier/getById.action"})
  @ResponseBody
  public Map<String, Object> getById(@RequestParam int start, @RequestParam int limit, @RequestParam int id, HttpServletRequest request) {
    Supplier sup = null;
    try {
      sup = this.supplierService.getSupplierById(id);
      return mapOK(sup);
    } catch (Exception e) {
      log4j.error("Exception" , e);
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  @RequestMapping({"/supplier/getByAddressNumber.action"})
  @ResponseBody
  public Map<String, Object> getByAddressNumber(@RequestParam String addressNumber) {
    Supplier sup = null;
    try {
      sup = this.supplierService.searchByAddressNumber(addressNumber);
      return mapOK(sup);
    } catch (Exception e) {
      log4j.error("Exception" , e);
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  /*
  @RequestMapping({"/supplier/correctionEmail.action"})
  @ResponseBody
  public void correctionEmail() {
    List<Supplier> sup = null;
    try {
      sup = supplierDao.correctionEmail();
      for(Supplier o : sup) {
    	  Users u = userService.getByUserName(o.getAddresNumber());
    	  u.setEmail(o.getEmailSupplier());
    	  userService.update(u, null, null);
      }
      
      log.info("Actualizacion Email usuarios terminada");
      
      //return mapOK(sup);
    } catch (Exception e) {
      e.printStackTrace();
      //return mapError(e.getMessage());
    } 
  }
  */
  @RequestMapping({"/public/getCountRFC.action"})
  @ResponseBody
  public Map<String, Object> getCountRFC(@RequestParam String rfcSupplier, @RequestParam String typeSearch ) {
	  List<Supplier> sups = null;
	  String msg = "";
    try {
    	sups  = this.supplierService.searchByRfc(rfcSupplier,typeSearch);
    	if(sups.size()==2) {
    		if(!sups.get(0).getCurrencyCode().equals(sups.get(1).getCurrencyCode())) {
    			msg="1";
    		}else msg= Integer.toString(sups.size());
    		
    	}else {
        	msg = Integer.toString(sups.size());
    	}

      return mapStrOk(msg);
    } catch (Exception e) {
      log4j.error("Exception" , e);	
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  @RequestMapping(value = {"/supplier/updateCatCodeSupp.action"}, method = {RequestMethod.POST})
  public @ResponseBody Map<String, Object> updateCatCodeSupp(@RequestParam int idSupplier, 
		  @RequestParam String approvalStep, @RequestParam String catCode23, @RequestParam String catCode24, 
		  @RequestParam String catCode01, @RequestParam String catCode20, @RequestParam String glClass,
		  @RequestParam String paymentMethod, @RequestParam String requisitosFiscales,@RequestParam String pmtTrmCxC,
		  @RequestParam String explFiscal, @RequestParam String catCode15
		  ) {
   try {
     Supplier supp = this.supplierService.getSupplierById(idSupplier);
    if(supp.getDataUpdateList() == null || "".equals(supp.getDataUpdateList())) {
     if("FIRST".equals(approvalStep)) {
    	 supp.setCatCode23(catCode23);
    	 supp.setCatCode24(catCode24);
     }
     
     if("THIRD".equals(approvalStep)) {
    	 supp.setCatCode15(catCode15);
    	 supp.setCatCode01(catCode01);
    	 supp.setCatCode20(catCode20);
    	 supp.setGlClass(glClass);
    	 supp.setPaymentMethod(paymentMethod);
    	 supp.setRequisitosFiscales(requisitosFiscales);
    	 supp.setPmtTrmCxC(pmtTrmCxC);
    	 
      }
    }else {
    	if("TES".equals(supp.getUpdateApprovalFlow()) && "FIRST".equals(approvalStep) ||
    	   supp.getUpdateApprovalFlow().contains("TES")	&& "SECOND".equals(approvalStep)) {
    		
    		 supp.setCatCode23(catCode23);
       	     supp.setCatCode24(catCode24);
    		 supp.setCatCode15(catCode15);
        	 supp.setCatCode01(catCode01);
        	 supp.setCatCode20(catCode20);
        	 supp.setGlClass(glClass);
        	 supp.setPaymentMethod(paymentMethod);
        	 supp.setRequisitosFiscales(requisitosFiscales);
        	 supp.setPmtTrmCxC(pmtTrmCxC);
    	}

    }

    supplierDao.updateSupplier(supp);
     return mapStrOk("Succ Update");
   } catch (Exception e) {
     log4j.error("Exception" , e);
     e.printStackTrace();
     return mapError(e.getMessage());
   } 
 }  
  
  @RequestMapping({"/supplier/updateEmailSupplier.action"})
  @ResponseBody
  public Map<String, Object> updateEmailSupplier(@RequestParam int idSupplier, @RequestParam String emailSupplier) {
    try {
      String sup = this.supplierService.updateEmailSupplier(idSupplier, emailSupplier);
      return mapStrOk(sup);
    } catch (Exception e) {
      log4j.error("Exception" , e);
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  @RequestMapping({"/public/getByTicketId.action"})
  @ResponseBody
  public Map<String, Object> getById(@RequestParam long ticketId) {
    Supplier sup = null;
    try {
      sup = this.supplierService.searchByTicket(ticketId);
      return mapOK(sup);
    } catch (Exception e) {
      log4j.error("Exception" , e);	
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  @RequestMapping(value ="/supplier/disableSupplier.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> disableSupplier(
													@RequestParam String idSupplier){
		try{
			String msg = supplierService.disableSupplier(idSupplier);
			return mapStrOk(msg);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
  
  @RequestMapping({"/supplier/searchByCriteria.action"})
  @ResponseBody
  public Map<String, Object> getByName(@RequestParam int start, @RequestParam int limit, @RequestParam String query, HttpServletRequest request) {
    List<SupplierDTO> list = null;
    int total = 0;
    try {
      list = this.supplierService.searchByCriteria(query, start, limit);
      total = list.size();
      return mapOK(list, total);
    } catch (Exception e) {
      log4j.error("Exception" , e);	
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  @RequestMapping({"/supplier/searchSupplier.action"})
  @ResponseBody
  public Map<String, Object> getSuppliers(@RequestParam int start, @RequestParam int limit, @RequestParam String supAddNbr, @RequestParam String supAddName) {
    List<SupplierDTO> list = null;
    int total = 0;
    try {
      list = this.supplierService.listSuppliers(supAddNbr, supAddName, start, limit);
      total = this.supplierService.listSuppliersTotalRecords(supAddNbr, supAddName);
      return mapOK(list, total);
    } catch (Exception e) {
      log4j.error("Exception" , e);
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  @RequestMapping(value = {"/newRegister/save.action"}, method = {RequestMethod.POST})
  @ResponseBody
  public Map<String, Object> save(@RequestBody Supplier obj) {
    try {
      long ticket = this.supplierService.saveSupplier(obj);
      return mapStrOk(String.valueOf(ticket));
    } catch (Exception e) {
    	log4j.error("Exception" , e);	
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  @RequestMapping(value = {"/public/update.action"}, method = {RequestMethod.POST})
  @ResponseBody
  public Map<String, Object> update(@RequestBody Supplier obj) {
    try {
      long ticketId = this.supplierService.updateSupplier(obj);
      if (ticketId == -1L) return mapStrOk(String.valueOf("ERROR_COMPL")); 
      return mapStrOk(String.valueOf(ticketId));
    } catch (Exception e) {
      log4j.error("Exception" , e);
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  @RequestMapping(value = {"/codigoPostal/view.action"}, method = {RequestMethod.GET})
  @ResponseBody
  public Map<String, Object> update(@RequestParam int start, @RequestParam int limit, @RequestParam String query) {
    try {
      List<CodigosPostales> list = this.supplierService.getByCode(query, start, limit);
      if (list != null)
        return mapOKCP(list, list.size()); 
      return mapOKCP(new ArrayList<>(), 0);
    } catch (Exception e) {
      log4j.error("Exception" , e);	
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  @RequestMapping({"/public/emailValidation.action"})
  @ResponseBody
  public Map<String, Object> emailValidation(@RequestParam String emailComprador, HttpServletRequest request) {
    try {
      Users u = this.supplierService.getPurchaseRoleByEmail(emailComprador.toLowerCase());
      if (u != null)
        return mapOK(u); 
      return mapError("El correo electrdel comprador no existe en la base de datos de CRYOINFRA. Revise nuevamente los datos capturados.");
    } catch (Exception e) {
      log4j.error("Exception" , e);
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  @RequestMapping(value = {"/public/supplierReplication.action"}, method = {RequestMethod.GET})
  @ResponseBody
  public Map<String, Object> supplierReplication(HttpServletRequest request) {
    this.jDERestService.getNewAddressBook();
    return mapStrOk("OK");
  }
  
  @RequestMapping({"/supplier/getCustomBroker.action"})
  @ResponseBody
  public Map<String, Object> getCustomBroker(@RequestParam String addressNumber) {
    List<UDC> udcList = null;
    boolean isCustomBroker = false;
    try {
    	udcList = udcService.searchBySystem("CUSTOMBROKER");
    	
    	if(udcList != null && !udcList.isEmpty()) {
    		for(UDC udc : udcList) {
    			if(udc.getUdcKey().trim().equals(addressNumber.trim())) {
    				isCustomBroker = true;
    				break;
    			}
    		}
    	}
    	
      return mapBooleanOk(isCustomBroker);
    } catch (Exception e) {
      log4j.error("Exception" , e);	
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }

  @RequestMapping({"/supplier/getTransportCustomBroker.action"})
  @ResponseBody
  public Map<String, Object> getTransportCustomBroker(@RequestParam String addressNumber) {
    List<UDC> udcList = null;
    boolean isTransportCustomBroker = false;
    try {
    	udcList = udcService.searchBySystem("CUSTOMBROKER");
    	
    	if(udcList != null && !udcList.isEmpty()) {
    		for(UDC udc : udcList) {
    			if(udc.getUdcKey().trim().equals(addressNumber.trim()) && udc.getStrValue1() != null && "Y".equals(udc.getStrValue1().trim())) {
    				isTransportCustomBroker = true;
    				break;
    			}
    		}
    	}
    	
      return mapBooleanOk(isTransportCustomBroker);
    } catch (Exception e) {
      log4j.error("Exception" , e);
      e.printStackTrace();
      return mapError(e.getMessage());
    } 
  }
  
  //OUTSOURCING
  @RequestMapping(value ="/public/approveOutsourcingRequest", method = RequestMethod.GET)
  public String approveOutsourcingRequest(Model model, @RequestParam String ab, @RequestParam String token) {

      Supplier s = supplierService.searchByAddressNumber(ab);
      if(s != null) {
    	  s.setOutSourcingAccept(true);
    	  s.setOutSourcingRecordDate(new Date());
    	  supplierService.updateSupplierOutSourcing(s);
    	  supplierService.sendOutSourcingEmail(s);
    	  
          model.addAttribute("supplierName", s.getRazonSocial());
  		  model.addAttribute("message", "_APPROVED");
      }
		  return "outsourcingFiles";
  }
  
  @RequestMapping({ "/supplier/token/listAccessTokenRegister.action" })
	@ResponseBody
	public Map<String, Object> listAccessTokenRegister(@RequestParam int start, @RequestParam int limit,
			@RequestParam String query) {
		List<AccessTokenRegister> list = null;
		int total = 0;
		try {
			list = supplierService.listAccessTokenRegister(query, start, limit);
			total = supplierService.listAccessTokenRegisterCount(query);
			return mapOKAccessTokenRegister(list, total);
		} catch (Exception e) {
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
  
  @RequestMapping({ "/supplier/token/saveAccessTokenRegister.action" })
	@ResponseBody
	public Map<String, Object> listAccessTokenRegister(@RequestBody AccessTokenRegister o) {

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			supplierService.saveAccessToken(o, usr, "");
			return mapStrOk("OK");
		} catch (Exception e) {
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping({ "/supplier/token/updateAccessTokenRegister.action" })
	@ResponseBody
	public Map<String, Object> updateAccessTokenRegister(@RequestBody AccessTokenRegister o) {

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			supplierService.updateAccessToken(o, usr);
			return mapStrOk("OK");
		} catch (Exception e) {
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/public/authRegister", method = RequestMethod.GET)
	public String authRegister(@RequestParam String access_token, HttpServletRequest request, Model model) {
		try {
			return supplierService.validateAcccessTokenRegister(access_token, request, false);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	@RequestMapping(value="/newRegisterAuth.action",method = RequestMethod.GET)
	public String newRegisterAuth(Model model, @RequestParam String access_token, HttpServletRequest request){
		
		String validateToken = supplierService.validateAcccessTokenRegister(access_token, request, true);
		if(!"invalidToken".equals(validateToken)) {
			//model.addAttribute("access_token", access_token);
			return "newRegister";
		}else {
			return validateToken;
		}
	}
	
	@RequestMapping(value="/openTicketRequest.action",method = RequestMethod.POST)
	public String newRegisterWithTicket(Model model, @RequestParam String rfc, @RequestParam long ticket, 
			HttpServletRequest request){
		
		Supplier sup = this.supplierService.searchByTicket(ticket);
		if(sup != null) {
			
			if("MX".equals(sup.getCountry())) {
				if(!sup.getRfc().equals(rfc)) {
					model.addAttribute("errorRequest", "ERROR: El RFC no est� registrado");
					return "requestTicketPage";
				}
			}else {
				if(!sup.getTaxId().equals(rfc)) {
					model.addAttribute("errorRequest", "ERROR: TaxId is not registered");
					return "requestTicketPage";
				}
			}
			/*if(sup.getRfc() != null || !"".equals(sup.getRfc())){
				if(!sup.getRfc().equals(rfc)) {
					model.addAttribute("errorRequest", "ERROR: El RFC no est� registrado");
					return "requestTicketPage";
				}
			}else {
				if(sup.getTaxId() != null || !"".equals(sup.getTaxId())){
					if(!sup.getTaxId().equals(rfc)) {
						model.addAttribute("errorRequest", "ERROR: TaxId is not registered");
						return "requestTicketPage";
					}
				}
			}*/
			
			if(AppConstants.STATUS_INPROCESS.equals(sup.getApprovalStatus())) {
				model.addAttribute("errorRequest", "ERROR: Su solicitud ya no se encuentra disponible en formato borrador");
				return "requestTicketPage";
			}
			
			if(AppConstants.STATUS_ACCEPT.equals(sup.getApprovalStatus())) {
				model.addAttribute("errorRequest", "ERROR: Su solicitud ya no se encuentra disponible en formato borrador");
				return "requestTicketPage";
			}
			
			model.addAttribute("ticketAccepted", ticket);
			return "newRegister";
		}else {
			model.addAttribute("errorRequest", "ERROR: No record found");
			return "requestTicketPage";
		}

	}
	
	@RequestMapping(value="/requestTicketPage.action",method = RequestMethod.GET)
	public String newRegisterWithTicket(Model model, HttpServletRequest request){
		return "requestTicketPage";
	}
  
  public Map<String, Object> mapOK(List<SupplierDTO> list, int total) {
    Map<String, Object> modelMap = new HashMap<>(3);
    modelMap.put("total", Integer.valueOf(total));
    modelMap.put("data", list);
    modelMap.put("success", Boolean.valueOf(true));
    return modelMap;
  }
  
  public Map<String, Object> mapOK(SupplierDTO obj) {
    Map<String, Object> modelMap = new HashMap<>(3);
    modelMap.put("total", Integer.valueOf(1));
    modelMap.put("data", obj);
    modelMap.put("success", Boolean.valueOf(true));
    return modelMap;
  }
  
  public Map<String, Object> mapOK(Users obj) {
    Map<String, Object> modelMap = new HashMap<>(3);
    modelMap.put("total", Integer.valueOf(1));
    modelMap.put("data", obj);
    modelMap.put("success", Boolean.valueOf(true));
    return modelMap;
  }
  
  public Map<String, Object> mapOK(Supplier obj) {
    Map<String, Object> modelMap = new HashMap<>(3);
    modelMap.put("total", Integer.valueOf(1));
    modelMap.put("data", obj);
    modelMap.put("success", Boolean.valueOf(true));
    return modelMap;
  }
  
  public Map<String, Object> mapError(String msg) {
    Map<String, Object> modelMap = new HashMap<>(2);
    modelMap.put("message", msg);
    modelMap.put("success", Boolean.valueOf(false));
    return modelMap;
  }
  
  public Map<String, Object> mapStrOk(String msg) {
    Map<String, Object> modelMap = new HashMap<>(2);
    modelMap.put("message", msg);
    modelMap.put("success", Boolean.valueOf(true));
    return modelMap;
  }
  
  public Map<String, Object> mapOKCP(List<CodigosPostales> list, int total) {
    Map<String, Object> modelMap = new HashMap<>(3);
    modelMap.put("total", Integer.valueOf(total));
    modelMap.put("data", list);
    modelMap.put("success", Boolean.valueOf(true));
    return modelMap;
  }
  
  public Map<String, Object> mapBooleanOk(boolean obj) {
	  Map<String, Object> modelMap = new HashMap<>(2);
	  modelMap.put("data", Boolean.valueOf(obj));
	  modelMap.put("success", Boolean.valueOf(true));
	  return modelMap;	   
  }
  
  public Map<String, Object> mapOKAccessTokenRegister(List<AccessTokenRegister> list, int total) {
		Map<String, Object> modelMap = new HashMap<>(3);
		modelMap.put("total", Integer.valueOf(total));
		modelMap.put("data", list);
		modelMap.put("success", Boolean.valueOf(true));
		return modelMap;
	}
  
}
