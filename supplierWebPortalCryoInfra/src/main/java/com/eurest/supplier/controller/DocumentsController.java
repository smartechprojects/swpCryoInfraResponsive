package com.eurest.supplier.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
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

import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.CustomBroker;
import com.eurest.supplier.model.CustomBrokerConcept;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.service.DocumentsService;
import com.eurest.supplier.service.CustomBrokerService;
import com.eurest.supplier.service.FiscalDocumentService;
import com.eurest.supplier.util.AppConstants;
 

@Controller
public class DocumentsController {
	
@Autowired
private DocumentsService documentsService;

@Autowired
private CustomBrokerService customBrokerService;

@Autowired
private FiscalDocumentService fiscalDocumentService;

Logger log4j = Logger.getLogger(DocumentsController.class);

@RequestMapping(value ="/documents/view.action")
public @ResponseBody Map<String, Object> view(@RequestParam int start,
											  @RequestParam int limit,
											  @RequestParam String query){	
	List<UserDocument> list=null;
	int total=0;
	try{
		if("".equals(query)){
			list = documentsService.getDocumentsList(start, limit);
			total = documentsService.getTotalRecords();
		}else{
			list = documentsService.searchCriteria(query);
			total = list.size();
		}					
	    return mapOK(list, total);
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return mapError(e.getMessage());
	}
	
}

	@RequestMapping(value = "/documents/listDocumentsByOrder.action")
	public @ResponseBody
	Map<String, Object> viewByOrder(@RequestParam int start,
									  @RequestParam int limit,
									  @RequestParam int orderNumber, 
									  @RequestParam String orderType, 
									  @RequestParam String addressNumber) {
		List<UserDocument> list = null;
		int total = 0;
		try {
			list = documentsService.searchCriteriaByOrderNumber(orderNumber, 
					                                            orderType, 
					                                            addressNumber,false);
			

			if(list != null){
				total = list.size();
				for(UserDocument ud : list){
					ud.setContent(null);
				}
			}
			else
				total = 0;
			return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}

	}
	
	@RequestMapping(value = "/documents/listDocumentsByFiscalRef.action")
	public @ResponseBody
	Map<String, Object> viewByFiscalRef(@RequestParam int start,
									  @RequestParam int limit,
									  @RequestParam String addresNumber,
									  @RequestParam String uuid) {
		List<UserDocument> list = null;
		int total = 0;
		try {
			list = documentsService.searchCriteriaByRefFiscal(addresNumber, uuid);
			if(list != null){
				total = list.size();
				for(UserDocument ud : list){
					ud.setContent(null);
				}
			}
			else
				total = 0;
			return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}

	}

	@RequestMapping(value = "/documents/listConceptDocumentsByFiscalRef.action")
	public @ResponseBody
	Map<String, Object> viewConceptDocumentsByFiscalRef(@RequestParam int start,
									  @RequestParam int limit,
									  @RequestParam String addresNumber,
									  @RequestParam String uuid) {
		
		List<UserDocument> list = null;
		List<FiscalDocuments> fdList = null;
		List<UserDocument> newList = new ArrayList<UserDocument>();
		boolean isDocumentFounded = false;
		UserDocument doc = null;
		int total = 0;
		
		try {
			
			//Agregar conceptos y sus documentos si es que fueron cargados
			fdList = fiscalDocumentService.getFiscalDocuments(addresNumber, "", uuid, "", start, limit);
			list = documentsService.searchCriteriaByDescription(addresNumber, "MainUUID_".concat(uuid),false);

			//list = documentsService.searchCriteriaByDescription(addresNumber, uuid);

			if(fdList != null) {				
				for(FiscalDocuments fd : fdList) {
					int orderNumber = fd.getOrderNumber();
					String orderType = fd.getOrderType();
					if(AppConstants.STATUS_FACT_FOREIGN.equals(fd.getType())) {
					List<UserDocument> listAllDocs  = documentsService.searchCriteriaByOrderNumber(orderNumber, orderType, addresNumber,false);
					listAllDocs.removeIf( s -> !"Otros".equals(s.getFiscalType()));
					list.addAll(listAllDocs);
					}
					
					if(AppConstants.NC_FIELD_UDC.equals(fd.getType())) {
						List<UserDocument> listAllDocs  = documentsService.searchCriteriaByOrderNumber(orderNumber, orderType, addresNumber,fd.getId(),false);
						listAllDocs.removeIf( s -> !"NotaCredito".equals(s.getFiscalType()));
						list.addAll(listAllDocs);
						}
					
					if(list != null){
						for(UserDocument ud : list){									
							if(ud.getFiscalType() != null && ("Factura".equals(ud.getFiscalType()) || "Evidencia".equals(ud.getFiscalType()) || "Otros".equals(ud.getFiscalType())|| "NotaCredito".equals(ud.getFiscalType())  )) {								
								doc = new UserDocument();
								doc.setId(ud.getId());
								doc.setName(ud.getName());
								doc.setContent(null);
								doc.setUuid(ud.getUuid());
								doc.setAddressBook(ud.getAddressBook());
								doc.setDocumentType(ud.getDocumentType());
								doc.setDescription(ud.getDescription());										
								doc.setSize(ud.getSize());
								doc.setAccept(ud.isAccept());										
								doc.setDocumentNumber(ud.getDocumentNumber());										
								doc.setFiscalRef(ud.getFiscalRef());
								doc.setFiscalType(ud.getFiscalType());
								doc.setFolio(ud.getFolio());
								doc.setSerie(ud.getSerie());										
								doc.setStatus(ud.isStatus());
								doc.setType(ud.getType());
								doc.setUploadDate(ud.getUploadDate());
								newList.add(doc);
							}
						}
					}
					
					if(fd.getConcepts() != null) {
						for(FiscalDocumentsConcept concept : fd.getConcepts()) {
							isDocumentFounded = false;
							
							if(list != null){
								for(UserDocument ud : list){									
									if(ud.getDocumentType() != null && concept.getConceptName() != null
										&& ud.getDocumentType().equals(concept.getConceptName())) {
										
										doc = new UserDocument();
										doc.setId(ud.getId());
										doc.setName(ud.getName());
										doc.setContent(null);
										doc.setUuid(ud.getUuid());
										doc.setAddressBook(ud.getAddressBook());
										doc.setDocumentType(ud.getDocumentType());
										doc.setDescription(String.valueOf(concept.getAmount()));//Monto Concepto										
										doc.setSize(ud.getSize());
										doc.setAccept(ud.isAccept());										
										doc.setDocumentNumber(ud.getDocumentNumber());										
										doc.setFiscalRef(ud.getFiscalRef());
										doc.setFiscalType(ud.getFiscalType());
										doc.setFolio(ud.getFolio());
										doc.setSerie(ud.getSerie());										
										doc.setStatus(ud.isStatus());
										doc.setType(ud.getType());
										doc.setUploadDate(ud.getUploadDate());
										newList.add(doc);
										
										isDocumentFounded = true;
									}
								}
							}
							
							if(!isDocumentFounded) {
								doc = new UserDocument();
								doc.setId(0);
								doc.setName("");
								doc.setContent(null);
								doc.setUuid(concept.getUuid());
								doc.setAddressBook(concept.getAddressNumber());
								doc.setDocumentType(concept.getConceptName());
								doc.setDescription(String.valueOf(concept.getAmount()));//Monto Concepto
								newList.add(doc);	
							}
						}
					}
				}
				
				total = newList.size();
			} else {
				total = 0;
			}


			/*
			if(list != null){
				total = list.size();
				for(UserDocument ud : list){
					ud.setContent(null);
					ud.setDescription("");
					if(fdList != null) {
						for(FiscalDocuments fd : fdList) {
							if(fd.getConcepts() != null) {
								for(FiscalDocumentsConcept concept : fd.getConcepts()) {
									if(ud.getDocumentType() != null && concept.getConceptName() != null
											&& ud.getDocumentType().equals(concept.getConceptName())) {										
										ud.setDescription(String.valueOf(concept.getAmount()));
										break;
									}
								}
							}
						}
					}
				}
			}
			else {
				total = 0;
			}
			*/
			return mapOK(newList, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}

	@RequestMapping(value = "/documents/listConceptDocumentsByFiscalRefCB.action")
	public @ResponseBody
	Map<String, Object> viewConceptDocumentsByFiscalRefCB(@RequestParam int start,
									  @RequestParam int limit,
									  @RequestParam String addresNumber,
									  @RequestParam String uuid) {
		
		List<UserDocument> list = null;
		List<CustomBroker> fdList = null;
		List<UserDocument> newList = new ArrayList<UserDocument>();
		boolean isDocumentFounded = false;
		UserDocument doc = null;
		int total = 0;
		
		try {
			
			//Agregar conceptos y sus documentos si es que fueron cargados
			fdList = customBrokerService.getCustomBroker(addresNumber, "", uuid, "", start, limit);
			list = documentsService.searchCriteriaByDescription(addresNumber, "MainUUID_".concat(uuid),true);

			if(fdList != null) {				
				for(CustomBroker fd : fdList) {
					if(list != null){
						for(UserDocument ud : list){									
							if(ud.getFiscalType() != null && "Factura".equals(ud.getFiscalType())) {								
								doc = new UserDocument();
								doc.setId(ud.getId());
								doc.setName(ud.getName());
								doc.setContent(null);
								doc.setUuid(ud.getUuid());
								doc.setAddressBook(ud.getAddressBook());
								doc.setDocumentType(ud.getDocumentType());
								doc.setDescription(ud.getDescription());										
								doc.setSize(ud.getSize());
								doc.setAccept(ud.isAccept());										
								doc.setDocumentNumber(ud.getDocumentNumber());										
								doc.setFiscalRef(ud.getFiscalRef());
								doc.setFiscalType(ud.getFiscalType());
								doc.setFolio(ud.getFolio());
								doc.setSerie(ud.getSerie());										
								doc.setStatus(ud.isStatus());
								doc.setType(ud.getType());
								doc.setUploadDate(ud.getUploadDate());
								newList.add(doc);
							}
						}
					}
					
					if(fd.getConcepts() != null) {
						for(CustomBrokerConcept concept : fd.getConcepts()) {
							isDocumentFounded = false;
							
							if(list != null){
								for(UserDocument ud : list){									
									if(ud.getDocumentType() != null && concept.getConceptName() != null
										&& ud.getDocumentType().equals(concept.getConceptName())) {
										
										doc = new UserDocument();
										doc.setId(ud.getId());
										doc.setName(ud.getName());
										doc.setContent(null);
										doc.setUuid(ud.getUuid());
										doc.setAddressBook(ud.getAddressBook());
										doc.setDocumentType(ud.getDocumentType());
										doc.setDescription(String.valueOf(concept.getAmount()));//Monto Concepto										
										doc.setSize(ud.getSize());
										doc.setAccept(ud.isAccept());										
										doc.setDocumentNumber(ud.getDocumentNumber());										
										doc.setFiscalRef(ud.getFiscalRef());
										doc.setFiscalType(ud.getFiscalType());
										doc.setFolio(ud.getFolio());
										doc.setSerie(ud.getSerie());										
										doc.setStatus(ud.isStatus());
										doc.setType(ud.getType());
										doc.setUploadDate(ud.getUploadDate());
										newList.add(doc);
										
										isDocumentFounded = true;
									}
								}
							}
							
							if(!isDocumentFounded) {
								doc = new UserDocument();
								doc.setId(0);
								doc.setName("");
								doc.setContent(null);
								doc.setUuid(concept.getUuid());
								doc.setAddressBook(concept.getAddressNumber());
								doc.setDocumentType(concept.getConceptName());
								doc.setDescription(String.valueOf(concept.getAmount()));//Monto Concepto
								newList.add(doc);	
							}
						}
					}
				}
				
				total = newList.size();
			} else {
				total = 0;
			}


			/*
			if(list != null){
				total = list.size();
				for(UserDocument ud : list){
					ud.setContent(null);
					ud.setDescription("");
					if(fdList != null) {
						for(CustomBroker fd : fdList) {
							if(fd.getConcepts() != null) {
								for(CustomBrokerConcept concept : fd.getConcepts()) {
									if(ud.getDocumentType() != null && concept.getConceptName() != null
											&& ud.getDocumentType().equals(concept.getConceptName())) {										
										ud.setDescription(String.valueOf(concept.getAmount()));
										break;
									}
								}
							}
						}
					}
				}
			}
			else {
				total = 0;
			}
			*/
			return mapOK(newList, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
		
	@RequestMapping(value ="/documents/openDocument.action", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, 
    			             @RequestParam int id) throws IOException {
     
		UserDocument doc = documentsService.getDocumentById(id);
		String fileName = doc.getName();
		String contentType = doc.getType();
		byte[] content = doc.getContent();
		
		if("text/xml".equals(contentType)) {
			ByteArrayInputStream stream = new  ByteArrayInputStream(doc.getContent());
			String xmlContent = IOUtils.toString(stream, "UTF-8");
			xmlContent = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
			xmlContent = xmlContent.replace("?<?xml", "<?xml");
			content = xmlContent.getBytes();
		}
		
		response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Length", String.valueOf(content.length));
        response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
        InputStream is = new ByteArrayInputStream(content);
        byte[] bytes = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(bytes)) != -1) {
            response.getOutputStream().write(bytes, 0, bytesRead);
        }
        is.close();
    }
	
	@SuppressWarnings("unused")
	@RequestMapping(value ="/documents/listDocumentByType.action", method = RequestMethod.GET)
    public Map<String, Object> listFileByType(@RequestParam int start,
			  								  @RequestParam int limit, 
			  								  @RequestParam String type) throws IOException {
     
		List<UserDocument> list = documentsService.searchCriteriaByType(type);
		int total = 0;
		try {
			if(list != null){
				total = list.size();
				for(UserDocument ud : list){
					ud.setContent(null);
				}
			}
			else
				total = 0;
			
			return null;
			//return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
    }

	@RequestMapping(value = "/documents/save.action", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> save(@RequestBody UserDocument obj) {

		try{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String usr = auth.getName();
		documentsService.save(obj,new Date(),usr);
		return mapOK(new UserDocument());
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return mapError(e.getMessage());
	}
}

@RequestMapping(value ="/documents/update.action")
public @ResponseBody Map<String, Object> update(@RequestBody UserDocument obj){
	
	try{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String usr = auth.getName();
		documentsService.update(obj,new Date(),usr);
		return mapOK(new UserDocument());
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return mapError(e.getMessage());
	}
}

@RequestMapping(value ="/documents/delete.action")
public @ResponseBody Map<String, Object>  delete(@RequestParam int id){

	try{ 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String usr = auth.getName();
		documentsService.delete(id, usr);
		return mapOK(new UserDocument());
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return mapError(e.getMessage());
	}
}

/*@RequestMapping(value = "/documents/getDocumentByUuid.action")
public @ResponseBody Map<String, Object> getDocumentByUuid(@RequestParam int orderNumber, @RequestParam String uuid) {
	
	List<UserDocument> list = null;
	int total = 0;
	try {
		
						
		list = documentsService.searchCriteriaByUuid(orderNumber, 
				uuid);
		
		total = list.size();
			
		return mapOK(list,total);
	} catch (Exception e) {
		e.printStackTrace();
		return mapError(e.getMessage());
	}
		
}*/

@RequestMapping(value ="/documents/openDocumentByUuid.action", method = RequestMethod.GET)
public void openDocumentByUuid(HttpServletResponse response, 
		@RequestParam int orderNumber, @RequestParam String uuidFactura, @RequestParam String type) throws IOException {
	
	String docType = "pdf".equals(type) ? "application/pdf" : "text/xml";
	
	List<UserDocument> list = documentsService.searchCriteriaByUuid(orderNumber, 
			uuidFactura,docType);
	
 if(list.size()>0) {
	UserDocument doc = documentsService.getDocumentById(list.get(0).getId());
	String fileName = doc.getName();
	String contentType = doc.getType();
	byte[] content = doc.getContent();
	
	if("text/xml".equals(contentType)) {
		ByteArrayInputStream stream = new  ByteArrayInputStream(doc.getContent());
		String xmlContent = IOUtils.toString(stream, "UTF-8");
		xmlContent = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
		xmlContent = xmlContent.replace("?<?xml", "<?xml");
		content = xmlContent.getBytes();
	}
	
	response.setHeader("Content-Type", contentType);
    response.setHeader("Content-Length", String.valueOf(content.length));
    response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
    InputStream is = new ByteArrayInputStream(content);
    byte[] bytes = new byte[1024];
    int bytesRead;
    while ((bytesRead = is.read(bytes)) != -1) {
        response.getOutputStream().write(bytes, 0, bytesRead);
    }
    is.close();
 }
}

		@RequestMapping(value ="/documents/getInvoiceByUuid.action", method = RequestMethod.GET)
		public @ResponseBody Map<String, Object> getInvoiceByUuid(HttpServletResponse response, 
				@RequestParam int orderNumber, @RequestParam String uuidFactura) throws IOException {
			
			String docType = "application/pdf";
			
			List<UserDocument> list = documentsService.searchCriteriaByUuid(orderNumber, 
					uuidFactura,docType);
			
		 if(list.size()>0) {
			UserDocument doc = documentsService.getDocumentById(list.get(0).getId());
			doc.setContent(null);
			return mapOK(doc);
		 }
		 
		 return mapOK(null);
		}

public Map<String,Object> mapOK(List<UserDocument> list, int total){
	Map<String,Object> modelMap = new HashMap<String,Object>(3);
	modelMap.put("total", total);
	modelMap.put("data", list);
	modelMap.put("success", true);
	return modelMap;
}

public Map<String,Object> mapOK(UserDocument obj){
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

public static String takeOffBOM(InputStream inputStream) throws IOException {
    BOMInputStream bomInputStream = new BOMInputStream(inputStream);
    return IOUtils.toString(bomInputStream, "UTF-8");
}

}
