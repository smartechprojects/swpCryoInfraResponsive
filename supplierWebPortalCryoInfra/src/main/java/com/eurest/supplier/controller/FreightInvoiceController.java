package com.eurest.supplier.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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

import com.eurest.supplier.dao.UserDocumentDao;
import com.eurest.supplier.model.ApprovalBatchFreight;
import com.eurest.supplier.model.BatchJournal;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.service.ApprovalBatchFreightService;
import com.eurest.supplier.service.BatchJournalService;
import com.eurest.supplier.service.DocumentsService;
import com.eurest.supplier.service.FiscalDocumentConceptService;
import com.eurest.supplier.service.FiscalDocumentService;
import com.eurest.supplier.service.JDERestService;
import com.eurest.supplier.service.SupplierService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.service.UsersService;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.LoggerJEdwars;
import com.eurest.supplier.util.PDFUtils;
import com.eurest.supplier.util.StringUtils;


@Controller
public class FreightInvoiceController {
	
	@Autowired
	private FiscalDocumentService fiscalDocumentService;
	
	@Autowired
	DocumentsService documentsService;	
	
	@Autowired
	StringUtils stringUtils;
	
	@Autowired
	private JDERestService jDERestService;
	
	@Autowired
	LoggerJEdwars loggerJEdwars;
	
	@Autowired
	SupplierService supplierService;
	
	@Autowired
	FiscalDocumentConceptService fiscalDocumentConceptService;
	
	@Autowired
	ApprovalBatchFreightService approvalBatchFreightService;
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	BatchJournalService batchJournalService;
	
	
	@Autowired
	UserDocumentDao userDocumentDao;
	
	@Autowired
	UdcService udcService;
	
	Logger log4j = Logger.getLogger(FreightInvoiceController.class);	
	
	@RequestMapping(value ="/freight/view.action")
	public @ResponseBody Map<String, Object> view(@RequestParam int start,
			  									  @RequestParam int limit,
												  @RequestParam String accountingAccount,
												  @RequestParam String status,
												  @RequestParam String batchIdParam,
												  @RequestParam String semanaPago, HttpServletRequest request){	
		try{
			List<FiscalDocuments> list=null;
			List<FiscalDocuments> listSend=new ArrayList<>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			Users us=usersService.getByUserName(usr);
			
			Long total=(long) 0;
			  UDC udcAlert = udcService.searchBySystemAndKey("BANDERA", "VERSINFLETESPDF");

              
			list = fiscalDocumentService.getFiscalDocumentsFreights(us,accountingAccount, status,batchIdParam,semanaPago, start, limit,udcAlert!=null&&udcAlert.isBooleanValue()); 
			total = fiscalDocumentService.getFiscalDocumentsFreightsCount(us,accountingAccount, status,batchIdParam,semanaPago,udcAlert!=null&&udcAlert.isBooleanValue()); 
			if(list!=null) {
//			total = list.size();		
			/*
			if(total >0) {
				for(FiscalDocuments fisdoc : list) {
					
					
					if(userDocumentDao.getPdfByBatch(fisdoc.getId()+"")==null) {
//						si el aun no se consigue el pdfbatch no se va a mostar en la lista de fletes Gama
						continue;
					}
					
					List<FiscalDocumentsConcept> fdc = fiscalDocumentService.getInvoicesByBatchID(String.valueOf(fisdoc.getId()), 0, 100);
					
					if(fdc != null && fdc.size()>0) {
						Set<FiscalDocumentsConcept> hSet = new HashSet<FiscalDocumentsConcept>(fdc);
				        hSet.addAll(fdc);	
				        fisdoc.setConcepts(hSet);	
				       listSend.add(fisdoc);
					}
					
				
				}
			}*/
		  return mapOK(list, list.size());
		    }
			   
			   return mapOK(new ArrayList<>(), 0);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/freight/listInvoiceByBatchID.action")
	public @ResponseBody
	Map<String, Object> listInvoiceByBatchID(@RequestParam int start,
									  @RequestParam int limit,
									  @RequestParam String batchID) {
		
		List<UserDocument> list = null;
		List<FiscalDocuments> fdList = null;
		List<FiscalDocumentsConcept> fdcList = null;
		List<UserDocument> newList = new ArrayList<UserDocument>();
		boolean isDocumentFounded = false;
		UserDocument doc = null;
		int total = 0;
		
		try {

			fdcList = fiscalDocumentService.getInvoicesByBatchID(batchID, start, limit);

			if(fdcList != null) {				
				
				total = fdcList.size();
			} else {
				total = 0;
			}


			//return mapOK(newList, total);
			return mapConceptOK(fdcList, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value ="/freight/update.action")
	public @ResponseBody Map<String, Object> update(@RequestBody FiscalDocuments obj,
													@RequestParam String status,
													@RequestParam String centroCostos,
													@RequestParam String conceptoArticulo,
													@RequestParam String company,
													@RequestParam String note,
													@RequestParam String documentType,
													@RequestParam String currentApprover,
													@RequestParam String nextApprover,
													@RequestParam String step,
													@RequestParam boolean cancelOrder){
		
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			String msg = "";
			if(obj.getId() != 0) {				
				//msg = fiscalDocumentService.updateDocument(obj, status, centroCostos, conceptoArticulo, company, note, documentType, currentApprover, nextApprover, step, cancelOrder);
				Map<String, Object> arguments = new HashMap<String, Object>();
				arguments.put("note",note);
				
				//msg = fiscalDocumentService.updateDocument(obj, arguments,!cancelOrder ,"APPROVALFREIGHT");
				

				msg = fiscalDocumentService.updateDocument(obj,status,note,documentType, nextApprover, step ,cancelOrder,usr);
				
			//	public String updateDocument(FiscalDocuments doc, String status, String note, String documentType, String nextApprover, String step, boolean cancelOrder) {
				
				if("".equals(msg)) {
					return mapStrOk("Surgió un error en la actualización.");	
				}
			}

			return mapStrOk(msg);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	
	@RequestMapping(value ="/freight/getComplFiscalDocsByStatus.action")
	public @ResponseBody Map<String, Object> getOrderReceiptsByStatus(
												  @RequestParam String addressBook){	
		List<FiscalDocuments> list = null;
		int total=0;
		try{
				list = fiscalDocumentService.getComplPendingInvoice(addressBook);
				total = list.size();
				return mapOK(list, total);
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	/*@RequestMapping(value = "/reSendJedwar.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String reSendJedwar(
								    		  HttpServletResponse response){
 
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();
    	

    	
        try{
        	List<LogDataJEdwars> logs=loggerJEdwars.getLogDataToSend();
        	for (LogDataJEdwars logDataJEdwars : logs) {
        		jDERestService.sendJournalEntriesReload(logDataJEdwars);
			}
        
        }catch(Exception e){
        	e.printStackTrace();
        	json.put("success", true);
            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
        }
        return json.toString();
	}*/
	
	@RequestMapping(value ="/freight/openCoverBatchReport.action", method = RequestMethod.GET)
	public void openDocumentByUuid(HttpServletResponse response, 
			@RequestParam String batchID) throws IOException {
		
		int batch = Integer.valueOf(batchID).intValue();
		
		FiscalDocuments fdoc = fiscalDocumentService.getById(batch);
		List<Supplier> supplierList = supplierService.searchByRfc(fdoc.getRfcEmisor(), "rfc");
		Supplier sup = null;
		List<FiscalDocumentsConcept> fdocConcepts = fiscalDocumentConceptService.searchByIdBatch(batchID);
		
		
		
		PDFUtils pdfUtils = new PDFUtils();
		
		if(supplierList != null && supplierList.size() > 0) {
			sup = supplierList.get(0);
		}
		
		List<ApprovalBatchFreight> approvalBatchList = approvalBatchFreightService.searchByIdBatch(batchID);
		
		StringBuilder approvalMsg = new StringBuilder();
		
		if(approvalBatchList == null || approvalBatchList.size() == 0) {
			approvalMsg.append("");
		}else {
			approvalMsg.append("El batch fue aprobado por: ");
			approvalMsg.append("\n");
			approvalMsg.append("\n");
		}
		
		
		for(ApprovalBatchFreight aprovBatch : approvalBatchList) {
			
			if(AppConstants.STATUS_ACCEPT.equals(aprovBatch.getAction())){
				
			Users user =usersService.getByUserName(aprovBatch.getApprover());
			if(user != null) {
				approvalMsg.append(user.getName());
				approvalMsg.append("\n");
				
			}
			
			}
			
		}
	
		
		byte[] pdfBytes = pdfUtils.getFilePDFFleightCover(fdoc,sup,fdocConcepts,approvalMsg.toString(),udcService);
	 if(pdfBytes.length>0) {
		
		String fileName = "Caratula";
		String contentType = "application/pdf";
		response.setHeader("Content-Type", contentType);
	    response.setHeader("Content-Length", String.valueOf(pdfBytes.length));
	    response.setHeader("Content-Disposition", "inline; filename=\"" + fileName +".pdf"+ "\"");
	    InputStream is = new ByteArrayInputStream(pdfBytes);
	    byte[] bytes = new byte[1024];
	    int bytesRead;
	    while ((bytesRead = is.read(bytes)) != -1) {
	        response.getOutputStream().write(bytes, 0, bytesRead);
	    }
	    is.close();
	 }
	}
	
	@RequestMapping(value ="/freight/openCoverBatchReportBatch.action", method = RequestMethod.GET)
	public void openDocumentBatchByUuid(HttpServletResponse response, 
			@RequestParam String batchID) throws IOException {
		
		int batch = Integer.valueOf(batchID).intValue();
		
//		FiscalDocuments fdoc = fiscalDocumentService.getById(batch);
//		List<Supplier> supplierList = supplierService.searchByRfc(fdoc.getRfcEmisor(), "rfc");
//		Supplier sup = null;
//		List<FiscalDocumentsConcept> fdocConcepts = fiscalDocumentConceptService.searchByIdBatch(batchID);
//		List<BatchJournal> batchList=batchJournalService.searchByIdBatch(batchID);
//		
//		
//		PDFUtils pdfUtils = new PDFUtils();
//		
//		if(supplierList != null && supplierList.size() > 0) {
//			sup = supplierList.get(0);
//		}
		
//		byte[] pdfBytes = pdfUtils.getFilePDFBatch(fdoc,sup,fdocConcepts,batchList);
		UserDocument pdf=userDocumentDao.getPdfByBatch(batch+"");
		
		byte[] pdfBytes =pdf.getContent();
		
	 if(pdfBytes.length>0) {
		
		String fileName = "PDF_BATCH";
		String contentType = "application/pdf";
		response.setHeader("Content-Type", contentType);
	    response.setHeader("Content-Length", String.valueOf(pdfBytes.length));
	    response.setHeader("Content-Disposition", "inline; filename=\"" + pdf.getName() +".pdf"+ "\"");
	    InputStream is = new ByteArrayInputStream(pdfBytes);
	    byte[] bytes = new byte[1024];
	    int bytesRead;
	    while ((bytesRead = is.read(bytes)) != -1) {
	        response.getOutputStream().write(bytes, 0, bytesRead);
	    }
	    is.close();
	 }
	}
	
	public static String takeOffBOM(InputStream inputStream) throws IOException {
	    BOMInputStream bomInputStream = new BOMInputStream(inputStream);
	    return IOUtils.toString(bomInputStream, "UTF-8");
	}

	public Map<String,Object> mapOK(List<FiscalDocuments> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String,Object> mapConceptOK(List<FiscalDocumentsConcept> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String,Object> mapOK(FiscalDocuments obj){
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
	
	public double currencyToDouble(String amount) {
		return Double.valueOf(amount.replace("$", "").replace(",", "").replace(" ", ""));
	}

	}
