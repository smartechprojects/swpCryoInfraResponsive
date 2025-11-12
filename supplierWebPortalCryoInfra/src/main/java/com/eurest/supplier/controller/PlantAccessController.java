package com.eurest.supplier.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eurest.supplier.dto.ForeingInvoice;
import com.eurest.supplier.model.ApprovalBatchFreight;
import com.eurest.supplier.model.BatchJournal;
import com.eurest.supplier.model.FileStore;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.PlantAccessRequest;
import com.eurest.supplier.model.PlantAccessWorker;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.PurchaseOrderDetail;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.Tolerances;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.service.ApprovalBatchFreightService;
import com.eurest.supplier.service.BatchJournalService;
import com.eurest.supplier.service.DocumentsService;
import com.eurest.supplier.service.EmailService;
import com.eurest.supplier.service.EmailServiceAsync;
import com.eurest.supplier.service.FileStoreService;
import com.eurest.supplier.service.FiscalDocumentConceptService;
import com.eurest.supplier.service.FiscalDocumentService;
import com.eurest.supplier.service.PlantAccessRequestService;
import com.eurest.supplier.service.PlantAccessWorkerService;
import com.eurest.supplier.service.PurchaseOrderService;
import com.eurest.supplier.service.SupplierService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.service.UsersService;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.FileUploadBean;
import com.eurest.supplier.util.LoggerJEdwars;
import com.eurest.supplier.util.PDFUtils;
import com.eurest.supplier.util.StringUtils;

import net.sf.json.JSONObject;


@Controller
public class PlantAccessController {
	
	@Autowired
	private FiscalDocumentService fiscalDocumentService;
	
	@Autowired
	PlantAccessRequestService plantAccessRequestService;
	
	@Autowired
	DocumentsService documentsService;	
	
	@Autowired
	StringUtils stringUtils;
	
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
	FileStoreService fileStoreService;
	
	@Autowired
	PlantAccessWorkerService plantAccessWorkerService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private JavaMailSender mailSenderObj;
	
	Logger log4j = Logger.getLogger(PlantAccessController.class);
	
	@RequestMapping(value ="/plantAccess/view.action")
	public @ResponseBody Map<String, Object> view(@RequestParam int start,
			  									  @RequestParam int limit,
												  @RequestParam String rFC,
												  @RequestParam String status,
												  @RequestParam String approver,
												  @RequestParam String addressNumberPA,
												  HttpServletRequest request){	
		try{
			final String OLD_FORMAT = "dd-MM-yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(OLD_FORMAT);
			
			List<PlantAccessRequest> list=null;
			int total=0;
			list = plantAccessRequestService.getPlantAccessRequests(rFC, status, approver, addressNumberPA, start, limit);
			total = plantAccessRequestService.getPlantAccessRequestsTotal(rFC, status, approver, addressNumberPA);
			/*for (PlantAccessRequest x : list) {

				x.setFechaSolicitudStr(sdf.format(x.getFechaSolicitud()));
			}*/
			
			list = list.parallelStream()
				    .map(x -> {
				        x.setFechaSolicitudStr(sdf.format(x.getFechaSolicitud()));
				        return x;
				    })
				    .collect(Collectors.toList());

		    return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
		
	}
	
	@RequestMapping(value ="/plantAccess/plantAccessPDF.action", method = RequestMethod.GET)
	public void plantAccessPDF(HttpServletResponse response, 
			@RequestParam String uuid) throws IOException {
		
		PDFUtils pdfUtils = new PDFUtils();
		PlantAccessRequest paRequest = plantAccessRequestService.getPlantAccessRequests(uuid);
		List<PlantAccessWorker> workers = plantAccessWorkerService.searchWorkersPlantAccessByIdRequest(String.valueOf(paRequest.getId()));
		
		Users u = usersService.getByUserName(paRequest.getAprovUser());
		if(u != null) paRequest.setAprovUser(u.getName());
		
		//TaxVaultDocument fdoc = taxVaultDocumentService.getById(id);
		
		byte[] pdfBytes = pdfUtils.getPlantAccessPDF(paRequest,workers);
	 if(pdfBytes.length>0) {
		
		String fileName = "R-AD-02-03-05_"+paRequest.getRfc()+".pdf";
		String contentType = "application/pdf";
		response.setHeader("Content-Type", contentType);
	    response.setHeader("Content-Length", String.valueOf(pdfBytes.length));
	    response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
	    InputStream is = new ByteArrayInputStream(pdfBytes);
	    byte[] bytes = new byte[1024];
	    int bytesRead;
	    while ((bytesRead = is.read(bytes)) != -1) {
	        response.getOutputStream().write(bytes, 0, bytesRead);
	    }
	    is.close();
	 }
	}
	
	@RequestMapping(value ="/plantAccess/updateListDocumentsWorker.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateListDocumentsWorker(
											String id,
											String documents,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		//PlantAccessRequest paRequest = null;
		//int total = 0;
	    try {
	      PlantAccessWorker worker = plantAccessWorkerService.getById(Integer.valueOf(id));
	      worker.setListDocuments(documents);
	      plantAccessWorkerService.update(worker);
	      
	      return mapStrOk("Succ");
	      //return mapOK(paRequest);
	    } catch (Exception e) {
	    	log4j.error("Exception" , e);
	      e.printStackTrace();
	      return mapError(e.getMessage());
	    } 
	

	}
	
	@RequestMapping(value ="/plantAccess/updateCheckBoxWorker.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateCheckBoxWorker(
											int idWorker,
											String idCheckbox,
											boolean selected,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		//PlantAccessRequest paRequest = null;
		//int total = 0;
	    try {
	    	
	      PlantAccessWorker worker = plantAccessWorkerService.getById(idWorker);
	      
	      List<FileStore> list = null;
		    try {
		    	
//		      list = fileStoreService.getFilesPlantAccess(idWorker,false);//false para no traer el content
		      String delete="";
		      
		      switch (idCheckbox) {
			case "1":
				delete="WORKER_CM1,WORKER_CD3TA";
				if(worker.getActivities().contains("3")) {
					delete="WORKER_CD3TA";
				}
				break;
			case "2":
				delete="WORKER_CD3G";
				break;
			case "3":
				delete="WORKER_CM1,WORKER_CD3TEC";
				if(worker.getActivities().contains("1")) {
					delete="WORKER_CD3TEC";
				}
				break;
			case "4":
				delete="WORKER_CD3TE1";
				break;
			case "5":
				delete="WORKER_CD3TC";
				break;
			case "6":
				delete="WORKER_HS";
				break;
			case "*":
				delete="WORKER_AE";
				break;
			}
		      if(!selected) {
		      list=fileStoreService.deleteFilesPlantAccess(idWorker, delete); 
		      }else {
		    	  list = fileStoreService.getFilesPlantAccess(idWorker,false);
		      }
		      
		      String documen="";
		      for (FileStore fileStore : list) {
				if(documen.length()!=0){
					documen=documen+",";
				}
				documen=documen+fileStore.getDocumentType();
			}
		      worker.setListDocuments(documen);
		      plantAccessWorkerService.update(worker);
		      
		      return mapOKFS(list, list.size());
		    } catch (Exception e) {
		    	log4j.error("Exception" , e);
		    	
		    	e.printStackTrace();
		      return mapError(e.getMessage());
		    } 
	      
	      
	      //return mapOK(paRequest);
	    } catch (Exception e) {
	    	log4j.error("Exception" , e);
	      e.printStackTrace();
	      return mapError(e.getMessage());
	    } 
	

	}
	
	@RequestMapping(value ="/plantAccess/searchPlantAccessRequest.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> searchPlantAccessRequest(
											String uuid,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		PlantAccessRequest paRequest = null;
	    int total = 0;
	    try {
	    	
	      paRequest = plantAccessRequestService.getPlantAccessRequests(uuid);
	      if(paRequest!=null) {
	    	  request.getSession().setAttribute("accessPlantUUID", uuid);
	      }else{
	    	  try {
	    		  request.getSession().removeAttribute("accessPlantUUID");
			} catch (Exception e) {
				// TODO: handle exception
			}
	      }
	      
	      return mapOK(paRequest);
	    } catch (Exception e) {
	      log4j.error("Exception" , e);
	      e.printStackTrace();
	      return mapError(e.getMessage());
	    } 
	

	}
	
	@RequestMapping(value ="/plantAccess/openDocumentPlantAccess.action", method = RequestMethod.GET)
    public void openDocumentPlantAccess(HttpServletResponse response, 
    			             @RequestParam int id) throws IOException {
     
		FileStore doc = fileStoreService.getById(id);
		String fileName = doc.getOriginName();
		String contentType = doc.getFileType();
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
	
	@RequestMapping(value ="/plantAccess/searchWorkersPlantAccessByIdRequest.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> searchWorkersPlantAccessByIdRequest(
											String uuid,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		List<PlantAccessWorker> list = null;
	    int total = 0;
	    try {
	    	
	      list = plantAccessWorkerService.searchWorkersPlantAccessByIdRequest(uuid);
	      
	      if(list!=null)
				total = list.size();		
	      
	      return mapOKW(list, total);
	    } catch (Exception e) {
	      log4j.error("Exception" , e);
	      e.printStackTrace();
	      return mapError(e.getMessage());
	    } 
	

	}
	
	@RequestMapping(value ="/plantAccess/searchWorkerFilesPlantAccessByIdWorker.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> searchWorkerFilesPlantAccessByIdWorker(
											String idRequest,
											String idWorker,
											HttpServletResponse response) {
		List<FileStore> list = null;
	    int total = 0;
	    try {
	      if(!org.apache.commons.lang.StringUtils.isBlank(idRequest)
	    		  && !org.apache.commons.lang.StringUtils.isBlank(idWorker)) {
	    	  
		      list = fileStoreService.getFilesPlantAccessWorker(idRequest, Integer.valueOf(idWorker).intValue(), false);//false para no traer el content	      
		      if(list!=null)
					total = list.size();
	      }
	      
	      return mapOKFS(list, total);
	    } catch (Exception e) {
	    	log4j.error("Exception" , e);	    	
	    	e.printStackTrace();
	      return mapError(e.getMessage());
	    }
	}
	@RequestMapping(value ="/plantAccess/deleteWorkerPlantAccessById.action")
	public @ResponseBody Map<String, Object> deleteWorkerPlantAccessById(@RequestParam String workerId){
		
		try{
			if(!org.apache.commons.lang.StringUtils.isBlank(workerId)) {
				PlantAccessWorker plantAccessWorker = plantAccessWorkerService.getById(Integer.valueOf(workerId));
				if(plantAccessWorker != null) {
					plantAccessWorkerService.deleteWorkerPlantAccess(plantAccessWorker);
				}
			}			
	        return mapStrOk("Success");
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value ="/plantAccess/deleteWorkerPlantAccess.action")
	public @ResponseBody Map<String, Object> deleteWorkerPlantAccess(@RequestBody PlantAccessWorker worker,@RequestParam String uuid){
		
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			String msg = "Succ";
			
			plantAccessWorkerService.deleteWorkerPlantAccess(worker);
			
			List<PlantAccessWorker> list = plantAccessWorkerService.searchWorkersPlantAccessByIdRequest(uuid);
			int total = 0;
		      
	        if(list!=null)
				total = list.size();		
	      
	        return mapOKW(list, total);
			
			//return mapStrOk(msg);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/plantAccess/deleteWorkerFilePlantAccessById.action")
	public @ResponseBody Map<String, Object> deleteWorkerFilePlantAccessById(@RequestParam String documentId){
		
		try{
			if(!org.apache.commons.lang.StringUtils.isBlank(documentId)) {
				FileStore file = fileStoreService.getById(Integer.valueOf(documentId).intValue());
				if(file != null) {
					if(file.getDocumentType().contains("WORKER_")) {
						//Borra archivo
						fileStoreService.deleteFilesPlantAccess(file);
						
						//Actualiza información del trabajador
						PlantAccessWorker plantAccessWorker = plantAccessWorkerService.getById(file.getNumRefer());
						if(plantAccessWorker != null && plantAccessWorker.getListDocuments() != null) {
							plantAccessWorkerService.updatePlantAccessWorkerDocuments(plantAccessWorker);						    
						    plantAccessWorkerService.update(plantAccessWorker);
						}
					}
				}
			}

			return mapStrOk("Success");
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value ="/plantAccess/deleteFilesPlantAccess.action")
	public @ResponseBody Map<String, Object> deleteFilesPlantAccess(@RequestBody FileStore file,@RequestParam String uuid){
		
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			String msg = "Succ";
			
			if(file.getDocumentType().contains("WORKER")) {
				PlantAccessWorker worker = plantAccessWorkerService.getById(file.getNumRefer());
			    worker.setListDocuments(worker.getListDocuments().replace(file.getDocumentType(), ""));
			    plantAccessWorkerService.update(worker);
			}
			
			fileStoreService.deleteFilesPlantAccess(file);
			List<FileStore> list = fileStoreService.getFilesPlantAccess(uuid,false);
			int total = 0;
		      
	        if(list!=null)
				total = list.size();		
	      
	        return mapOKFS(list, total);
			
			//return mapStrOk(msg);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	/*
	@RequestMapping(value ="/plantAccess/deleteFilesPlantAccess.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteFilesPlantAccess(
											String id,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		List<FileStore> list = null;
	    int total = 0;
	    try {
	    	
	      list = fileStoreService.getFilesPlantAccess(uuid);
	      
	      if(list!=null)
				total = list.size();		
	      
	      return mapOKFS(list, total);
	    } catch (Exception e) {
	      e.printStackTrace();
	      return mapError(e.getMessage());
	    } 
	

	}*/

	@RequestMapping(value ="/plantAccess/searchFilesPlantAccessNew.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> searchFilesPlantAccessNew(
											String requestId,
											HttpServletResponse response) {

		List<FileStore> list = null;
	    int total = 0;
	    try {
	      if(requestId != null && !requestId.isEmpty() && org.apache.commons.lang3.StringUtils.isNumeric(requestId)) {
	    	  
		      list = fileStoreService.getFilesPlantAccessRequest(Integer.parseInt(requestId),false);//false para no traer el content	      
		      if(list!=null) {
		    	  total = list.size();
		      }
					  
	      }
	      
	      return mapOKFS(list, total);
	    } catch (Exception e) {
	    	log4j.error("Exception" , e);	    	
	    	e.printStackTrace();
	      return mapError(e.getMessage());
	    } 
	

	}
	
	@RequestMapping(value ="/plantAccess/searchFilesPlantAccess.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getFilesPlantAccess(
											String uuid,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		
		try {
			Integer.parseInt(uuid);
			
		} catch (Exception e) {
			try {
				uuid= (String) request.getSession().getAttribute("accessPlantUUID");
			} catch (Exception e2) {
				uuid= ((Integer) request.getSession().getAttribute("accessPlantUUID"))+"";
			}
			
		}
		
		List<FileStore> list = null;
	    int total = 0;
	    try {
	    	
	      list = fileStoreService.getFilesPlantAccess(Integer.parseInt(uuid),false);//false para no traer el content
	      
	      if(list!=null)
				total = list.size();		
	      
	      return mapOKFS(list, total);
	    } catch (Exception e) {
	    	log4j.error("Exception" , e);
	    	
	    	e.printStackTrace();
	      return mapError(e.getMessage());
	    } 
	

	}
	/*
	@RequestMapping(value ="/plantAccess/searchFilesPlantAccess.action", method = RequestMethod.GET)
	@ResponseBody
	  public Map<String, Object> getFilesPlantAccess(@RequestParam String uuid) {
	    List<FileStore> list = null;
	    int total = 0;
	    try {
	    	
	      list = fileStoreService.getFilesPlantAccess(uuid);
	      
	      if(list!=null)
				total = list.size();		
	      
	      return mapOKFS(list, total);
	    } catch (Exception e) {
	      e.printStackTrace();
	      return mapError(e.getMessage());
	    } 
	  }
	*/
	@RequestMapping(value = "/plantAccess/listInvoiceByBatchID.action")
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
	@RequestMapping(value ="/plantAccess/update.action")
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
	
	
	
	
	@RequestMapping(value ="/plantAccess/openCoverBatchReport.action", method = RequestMethod.GET)
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
	    response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
	    InputStream is = new ByteArrayInputStream(pdfBytes);
	    byte[] bytes = new byte[1024];
	    int bytesRead;
	    while ((bytesRead = is.read(bytes)) != -1) {
	        response.getOutputStream().write(bytes, 0, bytesRead);
	    }
	    is.close();
	 }
	}
	
	@RequestMapping(value ="/plantAccess/openCoverBatchReportBatch.action", method = RequestMethod.GET)
	public void openDocumentBatchByUuid(HttpServletResponse response, 
			@RequestParam String batchID) throws IOException {
		
		int batch = Integer.valueOf(batchID).intValue();
		
		FiscalDocuments fdoc = fiscalDocumentService.getById(batch);
		List<Supplier> supplierList = supplierService.searchByRfc(fdoc.getRfcEmisor(), "rfc");
		Supplier sup = null;
		List<FiscalDocumentsConcept> fdocConcepts = fiscalDocumentConceptService.searchByIdBatch(batchID);
		List<BatchJournal> batchList=batchJournalService.searchByIdBatch(batchID);
		
		
		PDFUtils pdfUtils = new PDFUtils();
		
		if(supplierList != null && supplierList.size() > 0) {
			sup = supplierList.get(0);
		}
		
		byte[] pdfBytes = pdfUtils.getFilePDFBatch(fdoc,sup,fdocConcepts,batchList);
	 if(pdfBytes.length>0) {
		
		String fileName = "PDF_BATCH";
		String contentType = "application/pdf";
		response.setHeader("Content-Type", contentType);
	    response.setHeader("Content-Length", String.valueOf(pdfBytes.length));
	    response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
	    InputStream is = new ByteArrayInputStream(pdfBytes);
	    byte[] bytes = new byte[1024];
	    int bytesRead;
	    while ((bytesRead = is.read(bytes)) != -1) {
	        response.getOutputStream().write(bytes, 0, bytesRead);
	    }
	    is.close();
	 }
	}

	@RequestMapping(value ="/plantAccess/uploadFileRequestNew.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadFileRequestNew(FileUploadBean file, BindingResult result, String documentType, String idRequest, HttpServletResponse response) {
		
		FileStore fileToSave=new FileStore();		
		if(file.getFile().getSize() <= 10000000) {
			List<FileStore> list = fileStoreService.getFilesPlantAccess(idRequest,false);
			String docType = "REQUEST_"+documentType;
			for (FileStore d : list) {
	    	      if(docType.equals(d.getDocumentType())) {
	    	    	  this.fileStoreService.deleteFilesPlantAccess(d);  
	    	      }
	    	    }
			
			fileToSave.setDateUpload(new Date());
			fileToSave.setContent(file.getFile().getBytes());
			fileToSave.setFileType(file.getFile().getContentType());
			fileToSave.setOriginName(file.getFile().getOriginalFilename());
			fileToSave.setDocumentType("REQUEST_"+documentType);
			fileToSave.setStatus("PENDING");
			fileToSave.setNumRefer(Integer.valueOf(idRequest).intValue());
			fileToSave.setNamefile(idRequest);
			fileToSave.setUuid(idRequest);
			
			fileStoreService.save(fileToSave);
			fileToSave.setContent(null);
		} else {
			mapError("El documento cargado supera los 10 MB.");
		}
		return mapOK(fileToSave);
	}
	
	@RequestMapping(value ="/plantAccess/uploadFileWorkerNew.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadFileWorkerNew(FileUploadBean file, BindingResult result, String documentType,String idRequest, String idworker, HttpServletResponse response) {
		
		FileStore fileToSave=new FileStore();
		
		if(file.getFile().getSize() <= 10000000) {
			List<FileStore> list = fileStoreService.getFilesPlantAccess(idRequest,false);
			String docType = "WORKER_"+documentType;
			for (FileStore d : list) {
	    	      if(d.getNumRefer() == Integer.valueOf(idworker).intValue() 
	    	    		  && docType.equals(d.getDocumentType())) {
	    	    	  this.fileStoreService.deleteFilesPlantAccess(d);  
	    	      }
	    	    }
			
			fileToSave.setDateUpload(new Date());
			fileToSave.setContent(file.getFile().getBytes());
			fileToSave.setFileType(file.getFile().getContentType());
			fileToSave.setOriginName(file.getFile().getOriginalFilename());
			fileToSave.setDocumentType("WORKER_"+documentType);
			fileToSave.setStatus("PENDING");
			fileToSave.setNumRefer(Integer.valueOf(idworker).intValue());
			fileToSave.setNamefile(idworker);
			fileToSave.setUuid(idRequest);
			
			fileStoreService.save(fileToSave);
			fileToSave.setContent(new byte[] {});
		} else {
			mapError("El documento cargado supera los 10 MB.");
		}		
		return mapOK(fileToSave);
	

	}
	
	@RequestMapping(value ="/plantAccess/uploadFileRequest.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadFileRequest(FileUploadBean file, BindingResult result, String addRequestDocumentType, String uuidRequest, HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		int uuid= (Integer) request.getSession().getAttribute("accessPlantUUID");
		try {
			uuid=uuid==0?Integer.parseInt(uuidRequest):uuid;
			request.getSession().setAttribute("accessPlantUUID",uuid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
//		uuid=uuid==null?UUID.randomUUID().toString():uuid;
		request.getSession().setAttribute("accessPlantUUID", uuid);
		FileStore fileToSave=new FileStore();
		
		if(file.getFile().getSize() <= 10000000) {
			List<FileStore> list = fileStoreService.getFilesPlantAccess(uuid,false);
			String docType = "REQUEST_"+addRequestDocumentType;
			for (FileStore d : list) {
	    	      if(docType.equals(d.getDocumentType())) {
	    	    	  this.fileStoreService.deleteFilesPlantAccess(d);  
	    	      }
	    	    }
			
			fileToSave.setDateUpload(new Date());
			fileToSave.setContent(file.getFile().getBytes());
			fileToSave.setFileType(file.getFile().getContentType());
			fileToSave.setOriginName(file.getFile().getOriginalFilename());
			fileToSave.setDocumentType("REQUEST_"+addRequestDocumentType);
			fileToSave.setStatus("PENDING");
			fileToSave.setNumRefer(uuid);
			fileToSave.setNamefile(uuid+"");
			fileToSave.setUuid(uuid+"");
			
			fileStoreService.save(fileToSave);
			fileToSave.setContent(null);
		}
		
		
		
		return mapOK(fileToSave);
	

	}
	
	@RequestMapping(value ="/plantAccess/uploadFileWorker.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadFileWorker(FileUploadBean file, BindingResult result, String addRequestDocumentType,String uuidRequestWorker,@RequestParam int idworker, HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String uuid="";
				try {
					uuid=(String) request.getSession().getAttribute("accessPlantUUID");
				} catch (Exception e) {
					try {
						uuid=((Integer) request.getSession().getAttribute("accessPlantUUID"))+"";
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
				
				
//		uuid=uuid==null?UUID.randomUUID().toString():uuid;
		request.getSession().setAttribute("accessPlantUUID", uuid);
		FileStore fileToSave=new FileStore();
		
		if(file.getFile().getSize() <= 10000000) {
			List<FileStore> list = fileStoreService.getFilesPlantAccess(idworker,false);
			String docType = "WORKER_"+addRequestDocumentType;
			for (FileStore d : list) {
	    	      if(docType.equals(d.getDocumentType())) {
	    	    	  this.fileStoreService.deleteFilesPlantAccess(d);  
	    	      }
	    	    }
			
			fileToSave.setDateUpload(new Date());
			fileToSave.setContent(file.getFile().getBytes());
			fileToSave.setFileType(file.getFile().getContentType());
			fileToSave.setOriginName(file.getFile().getOriginalFilename());
			fileToSave.setDocumentType("WORKER_"+addRequestDocumentType);
			fileToSave.setStatus("PENDING");
			fileToSave.setNamefile("");
			fileToSave.setNumRefer(idworker);
			fileToSave.setUuid(idworker+"");
			
			fileStoreService.save(fileToSave);
			fileToSave.setContent(new byte[] {});
		}
		
		return mapOK(fileToSave);
	

	}
	
	@RequestMapping(value ="/plantAccess/uploadWorker.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadWorker(
											String employeeName,
											String membershipIMSS,
											String datefolioIDcard,
											String activities,
											String uuidPlantAccess,
											String id,
											String employeeOrdenes,
										    String employeePuesto,
										    String employeeCurp,
										    String employeeRfc,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		int  uuid=0;
		int idWorker=0;
		try {
			uuid= (Integer) request.getSession().getAttribute("accessPlantUUID");
		} catch (Exception e) {
			try {
				uuid=Integer.parseInt((String) request.getSession().getAttribute("accessPlantUUID"));
			} catch (Exception e2) {
				// TODO: handle exception
			}
			// TODO: handle exception
		}
		if (uuid==0) {
			uuid=Integer.parseInt(uuidPlantAccess);
		}
		
		try {
			
			idWorker= Integer.parseInt(id);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		request.getSession().setAttribute("accessPlantUUID", uuid);
		
		PlantAccessWorker trabajador=new PlantAccessWorker();
		trabajador=plantAccessWorkerService.getById(idWorker);
		
		if(trabajador==null) {
			trabajador=new PlantAccessWorker();
			trabajador.setFechaRegistro(new Date());
		trabajador.setRequestNumber(uuid+"");
		trabajador.setDatefolioIDcard(datefolioIDcard);
		trabajador.setEmployeeName(employeeName);
		trabajador.setMembershipIMSS(membershipIMSS);
		trabajador.setActivities(activities);
		trabajador.setEmployeeCurp(employeeCurp);
		trabajador.setEmployeeRfc(employeeRfc);
		trabajador.setEmployeePuesto(employeePuesto);
		trabajador.setEmployeeOrdenes(employeeOrdenes);
		
		plantAccessWorkerService.save(trabajador);
			
		}else {
			trabajador.setRequestNumber(uuid+"");
			trabajador.setDatefolioIDcard(datefolioIDcard);
			trabajador.setEmployeeName(employeeName);
			trabajador.setMembershipIMSS(membershipIMSS);
			trabajador.setActivities(activities);
			trabajador.setEmployeeCurp(employeeCurp);
			trabajador.setEmployeeRfc(employeeRfc);
			trabajador.setEmployeePuesto(employeePuesto);
			trabajador.setEmployeeOrdenes(employeeOrdenes);
			
			plantAccessWorkerService.update(trabajador);
		}
		
		
		
		
		return mapOK(trabajador);
	

	}

	@RequestMapping(value ="/plantAccess/savePlantAccessWorker.action", produces={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody public String  savePlantAccessWorker(@RequestBody PlantAccessWorker worker,
														HttpServletResponse response){
		JSONObject json = new JSONObject();
		PlantAccessWorker plantAccessWorker = null;
		SimpleDateFormat sdfOld = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfNew = new SimpleDateFormat("dd-MM-yyyy");
		
		try{
			//Genera lista de actividades
			String activities = "" +
			(worker.isDocsActivity1() ? "1," :"") +
			(worker.isDocsActivity2() ? "2," :"") +
			(worker.isDocsActivity3() ? "3," :"") +
			(worker.isDocsActivity4() ? "4," :"") +
			(worker.isDocsActivity5() ? "5," :"") +
			(worker.isDocsActivity6() ? "6," :"") +
			(worker.isDocsActivity7() ? "*" :"");
			
			//Obtiene la fecha de inducción
			String inductionDate = "";
			try {
				Date newDate = sdfOld.parse(worker.getDateInduction().replace("T", " "));
				inductionDate = sdfNew.format(newDate);
			} catch (Exception e) {
				inductionDate = sdfNew.format(new Date());
			}
			
			String dateFolioCardNumber = inductionDate.concat("/").concat(worker.getCardNumber());
			
			//Se recupera el Id del registro contenido en el campo RFC (ExtJs no permite enviar el Id directamente en el objeto al servicio).
			if(org.apache.commons.lang.StringUtils.isBlank(worker.getTempId())) {

				//Registro Nuevo
				worker.setFechaRegistro(new Date());
				worker.setActivities(activities);
				worker.setDatefolioIDcard(dateFolioCardNumber);
				
				/* ***¡¡NO ENVIAR MENSAJE DE VALIDACIÓN DESDE ESTE MÉTODO O MODIFICAR LA LÓGICA EN LA INTERFAZ GRÁFICA!!***
				String message = plantAccessWorkerService.validatePlantAccessWorker(worker, true);						
				if(!"".equals(message)) {
					json.put("success", true);
		            json.put("message", message);
		            json.put("data", worker); //Se envía información inicial
		            return json.toString();
				}*/
				
				plantAccessWorkerService.save(worker);
				
				//Se recupera el Id (El dato TempId que se maneja de manera temporal en el objeto, no persiste en la BD)
				worker.setTempId(String.valueOf(worker.getId()));
				plantAccessWorker = worker;
				
			} else {			
				
				//Actualización
				plantAccessWorker = plantAccessWorkerService.getById(Integer.valueOf(worker.getTempId()));
				if(plantAccessWorker != null) {
					//El dato TempId que se maneja de manera temporal en el objeto, no persiste en la BD)
					plantAccessWorker.setTempId(String.valueOf(worker.getTempId()));
					plantAccessWorker.setEmployeeName(worker.getEmployeeName());
					plantAccessWorker.setEmployeeLastName(worker.getEmployeeLastName());
					plantAccessWorker.setEmployeeSecondLastName(worker.getEmployeeSecondLastName());
					plantAccessWorker.setMembershipIMSS(worker.getMembershipIMSS());
					plantAccessWorker.setDateInduction(worker.getDateInduction());
					plantAccessWorker.setCardNumber(worker.getCardNumber());
					plantAccessWorker.setDocsActivity1(worker.isDocsActivity1());
					plantAccessWorker.setDocsActivity2(worker.isDocsActivity2());
					plantAccessWorker.setDocsActivity3(worker.isDocsActivity3());
					plantAccessWorker.setDocsActivity4(worker.isDocsActivity4());
					plantAccessWorker.setDocsActivity5(worker.isDocsActivity5());
					plantAccessWorker.setDocsActivity6(worker.isDocsActivity6());
					plantAccessWorker.setDocsActivity7(worker.isDocsActivity7());
					plantAccessWorker.setListDocuments(worker.getListDocuments());
					plantAccessWorker.setActivities(activities);
					plantAccessWorker.setDatefolioIDcard(dateFolioCardNumber);
					plantAccessWorker.setEmployeeCurp(worker.getEmployeeCurp());
					plantAccessWorker.setEmployeeRfc(worker.getEmployeeRfc());
					plantAccessWorker.setEmployeePuesto(worker.getEmployeePuesto());
					plantAccessWorker.setEmployeeOrdenes(worker.getEmployeeOrdenes());
//					plantAccessWorker.setActivities(worker.getActivities());
//					plantAccessWorker.setAllDocuments(worker.isAllDocuments());
//					plantAccessWorker.setFechaRegistro(worker.getFechaRegistro());										
//					plantAccessWorker.setRequestNumber(worker.getRequestNumber());
					
					/* ***¡¡NO ENVIAR MENSAJE DE VALIDACIÓN DESDE ESTE MÉTODO O MODIFICAR LA LÓGICA EN LA INTERFAZ GRÁFICA!!***
					String message = plantAccessWorkerService.validatePlantAccessWorker(plantAccessWorker, false);						
					if(!"".equals(message)) {
						json.put("success", true);
			            json.put("message", message);
			            json.put("data", worker); //Se envía información inicial
			            return json.toString();
					}*/
					
					plantAccessWorkerService.updatePlantAccessWorkerDocuments(plantAccessWorker);
					plantAccessWorkerService.update(plantAccessWorker);
										
				}
			}
			
			json.put("success", true);
            json.put("message", "");
            json.put("data", plantAccessWorker);
			return json.toString();
		    
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			json.put("success", true);
            json.put("message", e.getMessage());
            json.put("data", worker);
			return json.toString();

		}	
	}
	
	@RequestMapping(value ="/plantAccess/savePlantAccessRequest.action", produces={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody public String  savePlantAccessRequest(@RequestBody PlantAccessRequest request,
														HttpServletResponse response){
		JSONObject json = new JSONObject();
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 		HttpServletRequest requestHTTP = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String usr = auth.getName();
		PlantAccessRequest plantAccessRequest = null;
		try{
			//Se recupera el Id del registro contenido en el campo RFC (ExtJs no permite enviar el Id directamente en el objeto al servicio).
			if(org.apache.commons.lang.StringUtils.isBlank(request.getRfc())) {
				
				//Obtiene Razón Social
				if(!org.apache.commons.lang.StringUtils.isBlank(request.getAddressNumberPA())) {
					Supplier s = supplierService.searchByAddressNumber(request.getAddressNumberPA());
					if(s != null) {
						request.setRazonSocial(s.getRazonSocial());
					}
				}
				
				//Registro Nuevo				
				request.setStatus("GUARDADO");
				request.setHighRiskActivities("0");
				request.setFechaSolicitud(new Date());
				request.setUserRequest(usr);
				plantAccessRequestService.save(request);
				
				//Se recupera el Id
				request.setRfc(String.valueOf(request.getId()));
				plantAccessRequestService.updatet(request);
				plantAccessRequest = request;
				
			} else {
				
				//Actualización
				plantAccessRequest = plantAccessRequestService.getById(Integer.valueOf(request.getRfc()));
				if(plantAccessRequest != null) {
					String newStatus = request.getStatus();
					String oldStatus = plantAccessRequest.getStatus();
					boolean isSendRequest = "PENDIENTE".equals(newStatus) && !"PENDIENTE".equals(oldStatus) ? true: false;
					
					plantAccessRequest.setContractorCompany(request.getContractorCompany());
					plantAccessRequest.setContractorRepresentative(request.getContractorRepresentative());
					plantAccessRequest.setDescriptionUbication(request.getDescriptionUbication());
					plantAccessRequest.setHeavyEquipment(request.isHeavyEquipment());
					plantAccessRequest.setNameRequest(request.getNameRequest());
					plantAccessRequest.setOrdenNumber(request.getOrdenNumber());
					plantAccessRequest.setPlantRequest(request.getPlantRequest());
					plantAccessRequest.setStatus(request.getStatus());
					plantAccessRequest.setSinOrden(request.isSinOrden());
					plantAccessRequest.setContactEmergency(request.getContactEmergency());
					plantAccessRequest.setEmployerRegistration(request.getEmployerRegistration());
					plantAccessRequest.setFechafirmGui(request.getFechafirmGui());
					plantAccessRequest.setSubcontractService(request.isSubcontractService());
					plantAccessRequest.setSubContractedCompany(request.getSubContractedCompany());
					plantAccessRequest.setSubContractedCompanyRFC(request.getSubContractedCompanyRFC());
					
//					plantAccessRequest.setAddressNumberPA(request.getAddressNumberPA());
//					plantAccessRequest.setAprovUser(request.getAprovUser());
//					plantAccessRequest.setHighRiskActivities(request.getHighRiskActivities());
//					plantAccessRequest.setFechaAprobacion(request.getFechaAprobacion());
//					plantAccessRequest.setFechaFin(request.getFechaFin());
//					plantAccessRequest.setFechaInicio(request.getFechaInicio());
//					plantAccessRequest.setFechaSolicitudStr(request.getFechaSolicitudStr());
//					plantAccessRequest.setRazonSocial(request.getRazonSocial());					
					
					String heavyEquipment = "0" +
					(plantAccessRequest.isHeavyEquipment() ? ",HEAVYEQUIPMENT" : "");
					plantAccessRequest.setHighRiskActivities(heavyEquipment);
					
					//Elimina documentos si la opción HeavyEquipment es false
					if(!plantAccessRequest.isHeavyEquipment()) {
						List<FileStore> files = fileStoreService.getFilesPlantAccess(plantAccessRequest.getId(), false);
						if(files != null && !files.isEmpty()) {
							for(FileStore file : files) {
								if("REQUEST_SRC".equals(file.getDocumentType())
								|| "REQUEST_RM".equals(file.getDocumentType())) {
									fileStoreService.deleteFilesPlantAccess(file);									
								}
							}
						}
					}
					
					//Validaciones antes de enviar solicitud a flujo de aprobación
					if(isSendRequest) {
						String message = plantAccessRequestService.validatePlantAccessRequest(plantAccessRequest, true);						
						if(!"".equals(message)) {
							json.put("success", true);
				            json.put("message", message);
				            json.put("data", request); //Se envía información inicial
				            return json.toString();
						}
					}
					
					//Guardar Solicitud
					plantAccessRequestService.updatet(plantAccessRequest);					
				}
			}

			String r = "";
			if(!"".equals(r)) {
				json.put("success", true);
	            json.put("message", r);
	            json.put("data", plantAccessRequest);
	            return json.toString();
			}else {
				json.put("success", true);
	            json.put("message", "");
	            json.put("data", plantAccessRequest);
				return json.toString();
			}
		    
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			json.put("success", true);
            json.put("message", "Ocurrió un error al enviar la solicitud de Acceso a Planta.");
            json.put("data", request);
			return json.toString();

		}	
	}
	
	@RequestMapping(value ="/plantAccess/uploadPlantAccesRequest.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadPlantAccesRequest(
			String status,
			String nameRequest,
			String ordenNumber,
			String contractorCompany,
			String contractorRepresentative,
			String descriptionUbication,
			String aprovUser,
			String highRiskActivities,
			String rfc,
			String addressNumberPA,
			String plantRequest,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		PlantAccessRequest solicituddto= new PlantAccessRequest();
		int uuid=0;
		try {
			uuid= Integer.parseInt((String) request.getSession().getAttribute("accessPlantUUID"));
		} catch (Exception e) {
			uuid= (Integer) request.getSession().getAttribute("accessPlantUUID");
		}
		 
		
		
//		PlantAccessRequest solicitud = plantAccessRequestService.getPlantAccessRequests(rfc);
		PlantAccessRequest solicitud = plantAccessRequestService.getById(uuid);
		
		
		
		////verificar los archivos de cada proveedor
		
//		normal 2
//		1 2
//		2 1
//		3 2
//		4 1
//		5 1
//		6 1
//		* 1
		 int[] tabla = {2, 1, 2, 1, 1, 1}; 
		List<PlantAccessWorker> trabajadores=plantAccessWorkerService.searchWorkersPlantAccessByIdRequest(solicitud.getId()+"");
		
		for (PlantAccessWorker plantAccessWorker : trabajadores) {
			  String[] numerosArray = plantAccessWorker.getActivities().split(",");
		        
		       // Valores correspondientes en la tabla
		        
		        int suma = 2;
		        for (String numero : numerosArray) {
		            if (numero.equals("*")) {
		                suma += 1; // Sumar 1 para el asterisco '*'
		            } else {
		                int index = Integer.parseInt(numero) - 1; // Restar 1 para obtener el índice correcto en la tabla
		                if (index >= 0 && index < tabla.length) {
		                    suma += tabla[index];
		                }
		            }
		        }
		        
		        if(plantAccessWorker.getActivities().contains("1")&&plantAccessWorker.getActivities().contains("3")) {
		        	suma=suma-1;
		        }
		        if(suma!=((plantAccessWorker.getListDocuments()==null?"":plantAccessWorker.getListDocuments()).split(",").length-1)) {
		        	
		       	 List<FileStore> list = fileStoreService.getFilesPlantAccess(plantAccessWorker.getId(),false);
	        	 
	        	 String documen="";
			      for (FileStore fileStore : list) {
					if(documen.length()!=0){
						documen=documen+",";
					}
					documen=documen+fileStore.getDocumentType();
				}
			      plantAccessWorker.setListDocuments(documen);
			      plantAccessWorkerService.update(plantAccessWorker);
			      
	        	 if(suma!=list.size()) {
	        		 return mapError("El trabajador tiene documentos faltantes");
	        	 }
		        	
		        }
		        
		        
		}
		
		
		
		
		
		String currentApprover ="";
		String emailApprover = "";
		List<UDC> approverUDCList = udcService.advaceSearch("APPROVERPONP", "", plantRequest,"");
		if(approverUDCList != null) {
			for(UDC approver : approverUDCList) {
				if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
					currentApprover = currentApprover.concat(approver.getStrValue1()).concat(",");;
					emailApprover = emailApprover.concat(approver.getStrValue2().concat(","));
				}
		
			}
		}
		
		//PlantAccessRequest solicituddto= new PlantAccessRequest();
		
		
		if(solicitud != null) {
			solicitud.setStatus(status);
			solicitud.setAprovUser(currentApprover.toUpperCase());
			solicitud.setAprovUserDef(currentApprover.toUpperCase());
//			solicitud.setNameRequest(nameRequest);
//			solicitud.setOrdenNumber(ordenNumber);
//			solicitud.setContractorCompany(contractorCompany);
//			solicitud.setContractorRepresentative(contractorRepresentative);
//			solicitud.setDescriptionUbication(descriptionUbication);
//			solicitud.setHighRiskActivities(highRiskActivities);
//			solicitud.setPlantRequest(plantRequest);
			solicitud.setFechaSolicitud(new Date());
			
			String res = plantAccessRequestService.updatet(solicitud);
//			plantAccessWorkerService.updateWorkerRequest(rfc, solicitud.getId()+"");
			fileStoreService.updateFileRequest(uuid+"", solicitud.getId()+"");
		}else {
			
			
			solicituddto.setStatus(status);
			solicituddto.setAprovUser(currentApprover.toUpperCase());
			solicitud.setAprovUserDef(currentApprover.toUpperCase());

			solicituddto.setNameRequest(nameRequest);
			solicituddto.setOrdenNumber(ordenNumber);
			solicituddto.setContractorCompany(contractorCompany);
			solicituddto.setContractorRepresentative(contractorRepresentative);
			solicituddto.setDescriptionUbication(descriptionUbication);
			solicituddto.setHighRiskActivities(highRiskActivities);
			solicituddto.setFechaSolicitud(new Date());
			solicituddto.setRfc(rfc); 
			solicituddto.setAddressNumberPA(addressNumberPA);
			solicituddto.setPlantRequest(plantRequest);
			
			plantAccessRequestService.save(solicituddto);
			plantAccessWorkerService.updateWorkerRequest(rfc, solicituddto.getId()+"");
			fileStoreService.updateFileRequest(uuid+"", solicituddto.getId()+"");
		}
		
		//Users aprobador = usersService.getByUserName(aprovUser.toUpperCase());
		if(emailApprover != null) {
			EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
		      emailAsyncSup.setProperties("Solicitud de Acceso a Planta", this.stringUtils.prepareEmailContent("Estimado Aprobador:<br />La solicitud de " + nameRequest + "requiere de su aprobación.<br />Favor de revisar la información en el portal "), emailApprover);
		      emailAsyncSup.setMailSender(this.mailSenderObj);
		      Thread emailThreadSup = new Thread(emailAsyncSup);
		      emailThreadSup.start();
		}
		
		return mapOK(solicituddto);
	

	}

	@SuppressWarnings("unused")
	@RequestMapping(value ="/plantAccess/updateAprov.action")
	public @ResponseBody Map<String, Object> update(@RequestBody PlantAccessRequest obj,
													@RequestParam String status,
													@RequestParam String note,
													@RequestParam String idReques,
													@RequestParam String paFromDate,
													@RequestParam String paToDate
													){
		
		try{ 
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			String msg = "";
			if(obj.getId() != 0) {				
				obj=plantAccessRequestService.getById(obj.getId());
				Supplier supp = supplierService.searchByAddressNumber(obj.getAddressNumberPA());
				
				//Multiusuarios
				String emailUser = supp.getEmailSupplier();
				if(obj.getUserRequest() != null && !obj.getUserRequest().isEmpty()) {
					Users user = usersService.getByUserName(obj.getUserRequest());
					if(user != null) {
						emailUser = user.getEmail();
					}
				}
				
				if("APROBADO".equals(status)) {
					Users userAprob = usersService.getByUserName(usr);
					PDFUtils pdfUtils = new PDFUtils();
					PlantAccessRequest paRequest = plantAccessRequestService.getPlantAccessRequests(obj.getRfc());
					List<PlantAccessWorker> workers = plantAccessWorkerService.searchWorkersPlantAccessByIdRequest(String.valueOf(paRequest.getId()));
					paRequest.setFechaInicio(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(paFromDate));
					paRequest.setFechaFin(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(paToDate));
					paRequest.setFechaAprobacion(new Date());
					paRequest.setNombreAprobador(userAprob.getName());
					
					Users u = usersService.getByUserName(usr);
					if(u != null) paRequest.setAprovUser(u.getUserName());
					
					byte[] pdfBytes = pdfUtils.getPlantAccessPDF(paRequest,workers);
					try {
						//Envio al servicio de cryo
						 String res=  plantAccessRequestService.sendPlantAcceseRequestCryo(paRequest, workers,userAprob.getName());
						 if(res==null||!res.equals("ok")) {
							 return mapError("ocurrio un error al enlace del servicio, contacte al administrador, Error:(serv01) "+res);
						 }
						 	obj.setFechaInicio(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(paFromDate));
							obj.setFechaFin(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(paToDate));
							obj.setFechaAprobacion(new Date());
							obj.setNombreAprobador(userAprob.getName());
							obj.setAprovUser(u.getUserName());
						   
					} catch (Exception e) {
						e.printStackTrace();
					}
				   
					try {
						//Envio al servicio de cryo
						emailService.sendEmailWithAttach("Solicitud de Acceso a Planta", "Estimado proveedor: "+ supp.getRazonSocial() +" <br />Su solicitud de acceso a planta ha sido aprobado con éxito.<br>A continuación podrá descargar su formato de acceso."
								+ "<br><br><br><br>Dear supplier: "+ supp.getRazonSocial() +" <br />Your plant access request has been successfully approved.<br>Next you can download your access form.", emailUser, pdfBytes, "Acceso a Planta_"+obj.getNameRequest()+".pdf");
						   
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
				}
				if("RECHAZADO".equals(status)) {
					EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				      emailAsyncSup.setProperties("Solicitud de Acceso a Planta", this.stringUtils.prepareEmailContent("Estimado proveedor: <br /> Su solicitud de Acceso a Planta, desafortunadamente ha sido RECHAZADA.<br />El motivo es: <br />"+note+""
				      		+ "<br /><br /><br /><br />"
				      		+ "Dear supplier: <br /> Your request for Plant Access has unfortunately been REJECTED.<br />The reason is: <br />"+note), emailUser);
				      emailAsyncSup.setMailSender(this.mailSenderObj);
				      Thread emailThreadSup = new Thread(emailAsyncSup);
				      emailThreadSup.start();
				}
				obj.setStatus(status);
				obj.setFechaSolicitudStr(note);
				msg=plantAccessRequestService.updatet(obj);
				
				
				
				
				
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
	
	
	/**
	 * se creo este metodo para poder guardar la solicitud de accesso a planta
	 * y pudiera editarse despues, solo guardamos los primeros datos para poder obtener un id
	 * @param String status,
			String nameRequest,
			String ordenNumber,
			String contractorCompany,
			String contractorRepresentative,
			String descriptionUbication,
			String aprovUser,
			String highRiskActivities,
			String rfc,
			String addressNumberPA,
			String plantRequest,
											HttpServletResponse response
	 * @return          Json Map<String, Object>
	 * @author          Gamaliel Cruz
	 * @version         1.0
	 */
	
	
	@RequestMapping(value ="/plantAccess/uploadPlantAccesRequestHeader.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadPlantAccesRequestHeader(
			String status,
			String nameRequest,
			String ordenNumber,
			String contractorCompany,
			String contractorRepresentative,
			String descriptionUbication,
			String aprovUser,
			String highRiskActivities,
			String rfc,
			String addressNumberPA,
			String plantRequest,
			String idPlantRequest,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		PlantAccessRequest solicituddto= new PlantAccessRequest();
		
		int id=0;
		try {
			 id= (Integer) request.getSession().getAttribute("accessPlantUUID");
		} catch (Exception e) {
			try {
				id= Integer.valueOf(String.valueOf(request.getSession().getAttribute("accessPlantUUID"))).intValue();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		id=rfc.toUpperCase().equals("NUEVO")?0:(id==0?Integer.parseInt(rfc):id);
		
		
//		uuid=uuid==null?UUID.randomUUID().toString():uuid;
//		
		
		PlantAccessRequest solicitud = null;
		if (id!=0) {
			 solicitud = plantAccessRequestService.getById(id );
		}
		
//		String currentApprover ="";
//		String emailApprover = "";
//		List<UDC> approverUDCList = udcService.advaceSearch("APPROVERPONP", "", plantRequest,"");
//		if(approverUDCList != null) {
//			for(UDC approver : approverUDCList) {
//				if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
//					currentApprover = currentApprover.concat(approver.getStrValue1()).concat(",");;
//					emailApprover = emailApprover.concat(approver.getStrValue2().concat(","));
//				}
//		
//			}
//		}
		
		//PlantAccessRequest solicituddto= new PlantAccessRequest();
		
		
		if(solicitud != null) {
			solicituddto.setStatus("GUARDADO");
//			solicitud.setAprovUser(currentApprover.toUpperCase());
			solicitud.setNameRequest(nameRequest);
			solicitud.setOrdenNumber(ordenNumber);
			solicitud.setContractorCompany(contractorCompany);
			solicitud.setContractorRepresentative(contractorRepresentative);
			solicitud.setDescriptionUbication(descriptionUbication);
			solicitud.setHighRiskActivities(highRiskActivities);
			solicitud.setPlantRequest(plantRequest);
			solicitud.setFechaSolicitud(new Date());
			
			String res = plantAccessRequestService.updatet(solicitud);
			request.getSession().setAttribute("accessPlantUUID",solicituddto.getId());
//			plantAccessWorkerService.updateWorkerRequest(rfc, solicitud.getId()+"");
//			fileStoreService.updateFileRequest(uuid, solicitud.getId()+"");
		}else {
			
			
			solicituddto.setStatus("GUARDADO");
//			solicituddto.setAprovUser(currentApprover.toUpperCase());
			solicituddto.setNameRequest(nameRequest);
			solicituddto.setOrdenNumber(ordenNumber);
			solicituddto.setContractorCompany(contractorCompany);
			solicituddto.setContractorRepresentative(contractorRepresentative);
			solicituddto.setDescriptionUbication(descriptionUbication);
			solicituddto.setHighRiskActivities(highRiskActivities);
			solicituddto.setFechaSolicitud(new Date());
			solicituddto.setRfc(rfc); 
			solicituddto.setAddressNumberPA(addressNumberPA);
			solicituddto.setRazonSocial(supplierService.searchByAddressNumber(addressNumberPA).getRazonSocial());  ;
			solicituddto.setPlantRequest(plantRequest);
			
			plantAccessRequestService.save(solicituddto);
			solicituddto.setRfc(solicituddto.getId()+"");
			plantAccessRequestService.updatet(solicituddto);
			request.getSession().setAttribute("accessPlantUUID",solicituddto.getId());
//			plantAccessWorkerService.updateWorkerRequest(rfc, solicituddto.getId()+"");
//			fileStoreService.updateFileRequest(uuid, solicituddto.getId()+"");
		}
		
//		//Users aprobador = usersService.getByUserName(aprovUser.toUpperCase());
//		if(emailApprover != null) {
//			EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
//		      emailAsyncSup.setProperties("Solicitud de Acceso a Planta", this.stringUtils.prepareEmailContent("Estimado Aprobador:<br />La solicitud de " + nameRequest + "requiere de su aprobación.<br />Favor de revisar la información en el portal "), emailApprover);
//		      emailAsyncSup.setMailSender(this.mailSenderObj);
//		      Thread emailThreadSup = new Thread(emailAsyncSup);
//		      emailThreadSup.start();
//		}
		
		return mapOK(solicituddto);
	

	}
	
	@RequestMapping(value ="/plantAccess/verifyWorkerFiles.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> verifyWorkerFiles(
											int idWorker,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		//PlantAccessRequest paRequest = null;
		//int total = 0;
	    try {
	    	
//			normal 2
//			1 2
//			2 1
//			3 2
//			4 1
//			5 1
//			6 1
//			* 1
			 int[] tabla = {2, 1, 2, 1, 1, 1}; 
			PlantAccessWorker trabajador=plantAccessWorkerService.getById(idWorker);
			
		if (trabajador!=null) {
			  String[] numerosArray = trabajador.getActivities().split(",");
			        
			       // Valores correspondientes en la tabla
			        
			        int suma = 2;
			        for (String numero : numerosArray) {
			            if (numero.equals("*")) {
			                suma += 1; // Sumar 1 para el asterisco '*'
			            } else {
			                int index = Integer.parseInt(numero) - 1; // Restar 1 para obtener el índice correcto en la tabla
			                if (index >= 0 && index < tabla.length) {
			                    suma += tabla[index];
			                }
			            }
			        }
			        if(trabajador.getActivities().contains("1")&&trabajador.getActivities().contains("3")) {
			        	suma=suma-1;
			        }
			        
			        if(suma!=((trabajador.getListDocuments()==null?"":trabajador.getListDocuments()).split(",").length-1)) {
			        	 List<FileStore> list = fileStoreService.getFilesPlantAccess(idWorker,false);
			        	 
			        	 String documen="";
					      for (FileStore fileStore : list) {
							if(documen.length()!=0){
								documen=documen+",";
							}
							documen=documen+fileStore.getDocumentType();
						}
					      trabajador.setListDocuments(documen);
					      plantAccessWorkerService.update(trabajador);
					      
			        	 if(suma!=list.size()) {
			        		 return mapError("El trabajador tiene documentos faltantes");
			        	 }
			        	
			        }
			        
			        return mapStrOk("ok");
		}
		return mapError("No se pudo verifdicar el trabajador");
			        
			
	    } catch (Exception e) {
	    	log4j.error("Exception" , e);
	      e.printStackTrace();
	      return mapError(e.getMessage());
	    } 
	

	}
	
	@RequestMapping(value ="/plantAccess/verifyOrderInput.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> verifyOrderInput(
											boolean esSinOrden,
											String paOrdenNumberInput,
											String empresaPlantRequest,
											String description,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		
	    try {
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			String orderreturn="";
			if (esSinOrden) {
				/// buscar udc de empresa
				UDC udcORder= udcService.searchBySystemAndStrValue("PLANTACCES", "CONSECUTIVO", empresaPlantRequest);
				if (udcORder==null) {
					udcORder=new UDC();
					udcORder.setUdcSystem("PLANTACCES");
					udcORder.setUdcKey("CONSECUTIVO");
					udcORder.setStrValue2(empresaPlantRequest.toUpperCase().substring(0,3));
					udcORder.setStrValue1(empresaPlantRequest);
					udcORder.setIntValue(1);
					udcService.save(udcORder, new Date(), usr);
				}
				
				 orderreturn=udcORder.getStrValue2()+ String.format("%06d", udcORder.getIntValue());
				
				udcORder.setIntValue(udcORder.getIntValue()+1);
				udcService.update(udcORder, new Date(), usr);
				
				return mapStrOk(orderreturn);
				
			}else {
				
				int order=Integer.parseInt(paOrdenNumberInput);
				
				PurchaseOrder rat=purchaseOrderService.searchbyOrderAdress(order, usr);
				if(rat==null) {
					return mapError("Numero de orden invalida o no encontrada");
				}
				
				return mapStrOk(order+"");
				
				
			}
			   
			
	    } catch (Exception e) {
	    	log4j.error("Exception" , e);
	      e.printStackTrace();
	      return mapError(e.getMessage());
	    } 
	

	}
	
	
	@RequestMapping(value ="/plantAccess/validateOrderInput.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> validateOrderInput(
											String order,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		
	    try {
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
		
				
				int orders=Integer.parseInt(order);
				
				PurchaseOrder rat=purchaseOrderService.searchbyOrderAdress(orders, usr);
				
				 Set<String> validStatuses = new HashSet<>();
			        validStatuses.add("OC APROBADA");
			        validStatuses.add("OC FACTURADA");
			        validStatuses.add("OC COMPLEMENTO");
			        validStatuses.add("OC PAGADA");
			        validStatuses.add("OC RECIBIDA");
			        
			        if (rat == null || !validStatuses.contains(rat.getOrderStauts())) {
			            return mapError("Numero de orden invalida o no encontrada");
			        }
				Set<PurchaseOrderDetail> detalle=rat.getPurchaseOrderDetail();
				String description="";
				for (PurchaseOrderDetail d : detalle) {
					description=d.getItemDescription();
					break;
				}
				description=description==null||description.equals("")?"Sin descripcion":description;
				return mapOK(order+"",description);
			
	    } catch (Exception e) {
	    	log4j.error("Exception" , e);
	      e.printStackTrace();
	      return mapError(e.getMessage());
	    } 
	

	}
	
	public static String takeOffBOM(InputStream inputStream) throws IOException {
	    BOMInputStream bomInputStream = new BOMInputStream(inputStream);
	    return IOUtils.toString(bomInputStream, "UTF-8");
	}


	public Map<String,Object> mapOK(List<PlantAccessRequest> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String,Object> mapOK(String paOrdenNumberInput,String description){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("order", paOrdenNumberInput);
		modelMap.put("description", description);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String,Object> mapOKFS(List<FileStore> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String,Object> mapOKW(List<PlantAccessWorker> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String,Object> mapOK(FileStore data){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("data", data);
		modelMap.put("success", true);
		return modelMap;
	}
	public Map<String,Object> mapOK(PlantAccessWorker data){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("data", data);
		modelMap.put("success", true);
		return modelMap;
	}
		public Map<String,Object> mapOK(PlantAccessRequest data){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("data", data);
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
