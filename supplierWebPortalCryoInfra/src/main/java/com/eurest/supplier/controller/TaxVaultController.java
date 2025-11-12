package com.eurest.supplier.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.dto.ResponseGeneral;
import com.eurest.supplier.model.BatchJournal;
import com.eurest.supplier.model.DataAudit;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.PlantAccessRequest;
import com.eurest.supplier.model.PlantAccessWorker;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.TaxVaultDocument;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.service.ApprovalBatchFreightService;
import com.eurest.supplier.service.BatchJournalService;
import com.eurest.supplier.service.DataAuditService;
import com.eurest.supplier.service.DocumentsService;
import com.eurest.supplier.service.FileStoreService;
import com.eurest.supplier.service.FiscalDocumentConceptService;
import com.eurest.supplier.service.FiscalDocumentService;
import com.eurest.supplier.service.GetDownloadCFDIByUuidRunner;
import com.eurest.supplier.service.PlantAccessRequestService;
import com.eurest.supplier.service.PlantAccessWorkerService;
import com.eurest.supplier.service.SupplierService;
import com.eurest.supplier.service.TaxVaultDocumentService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.service.UsersService;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.BASE64DecodedMultipartFile;
import com.eurest.supplier.util.Cronato;
import com.eurest.supplier.util.DecodedMultipartFileCompatibility;
import com.eurest.supplier.util.FileUploadBean;
import com.eurest.supplier.util.LoggerJEdwars;
import com.eurest.supplier.util.MultiFileUploadBean;
import com.eurest.supplier.util.PDFUtils;
import com.eurest.supplier.util.StringUtils;
import com.google.gson.Gson;


@Controller
public class TaxVaultController {
	
	@Autowired
	private FiscalDocumentService fiscalDocumentService;
	
	@Autowired
	PlantAccessRequestService plantAccessRequestService;
	
	@Autowired
	TaxVaultDocumentService taxVaultDocumentService;
	
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
	UdcService udcService;
	
	@Autowired
	GetDownloadCFDIByUuidRunner getDownloadCFDIByUuidRunner;
	
	@Autowired
	DataAuditService dataAuditService;
	
	Logger log4j = Logger.getLogger(TaxVaultController.class);
	
	@RequestMapping(value ="/taxVault/view.action")
	public @ResponseBody Map<String, Object> view(@RequestParam int start,
			  									  @RequestParam int limit,
			  									  @RequestParam String rfcReceptor,
												  @RequestParam String rfcEmisor,
												  @RequestParam String tvUUID,
												  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date tvFromDate,
												  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date tvToDate,
												  @RequestParam String comboType,
												  HttpServletRequest request){	
		try{
			SimpleDateFormat sdfNew = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			final String OLD_FORMAT = "dd-MM-yyyy HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(OLD_FORMAT);
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Users user=usersService.getByUserName(auth.getName());
			List<TaxVaultDocument> list=null;
			int total=0;
			
			if(tvFromDate!=null) {
				try {
					Calendar cal = Calendar.getInstance();
					cal.setTime(tvFromDate);
					cal.add(Calendar.DATE, 1);
					tvFromDate = cal.getTime();
				}catch(Exception e) {
					log4j.error("Exception" , e);
					e.printStackTrace();
				}
			}
			
			if(tvFromDate != null) {
				try {
					String fromDate = sdfNew.format(tvFromDate);
					tvFromDate = sdfNew.parse(getFirstTimeDate(fromDate));
				}catch(Exception e) {
					log4j.error("Exception" , e);
					e.printStackTrace();
				}
			}
			
			if(tvToDate != null) {
				try {
					Calendar cal = Calendar.getInstance();
					cal.setTime(tvToDate);
					cal.add(Calendar.DATE, 1);
					tvToDate = cal.getTime();
				}catch(Exception e) {
					log4j.error("Exception" , e);
					e.printStackTrace();
				}
			}
			
			if(tvToDate != null) {
				try {
					String toDate = sdfNew.format(tvToDate);
					tvToDate = sdfNew.parse(getLastTimeDate(toDate));
				}catch(Exception e) {
					log4j.error("Exception" , e);
					e.printStackTrace();
				}
			}
			
			list = taxVaultDocumentService.getTaxVaultDocuments(rfcReceptor,rfcEmisor,tvUUID,tvFromDate,tvToDate,comboType, user,start, limit);
			total = taxVaultDocumentService.getTaxVaultDocumentsTotal(rfcReceptor,rfcEmisor,tvUUID,tvFromDate,tvToDate,comboType, user);
			
			for(TaxVaultDocument x : list) {
				x.setOrigen(sdf.format(x.getUploadDate()));
		}
			
		    return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/taxtVault/listInvoiceByBatchID.action")
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
	@RequestMapping(value ="/taxVault/deleteTaxVaultDocument.action")
	public @ResponseBody Map<String, Object> deleteTaxVaultDocument(@RequestBody TaxVaultDocument taxVaulDocument,@RequestParam String note){
		
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			String msg = "";
			taxVaultDocumentService.delete(taxVaulDocument);
			
			return mapStrOk(msg);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value ="/taxVault/reenvioMailTaxDocument.action")
	public @ResponseBody Map<String, Object> reenvioMailTaxDocument(@RequestBody TaxVaultDocument taxVaulDocument){
		
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			String msg = "";
			taxVaulDocument=taxVaultDocumentService.getById(taxVaulDocument.getId());
			taxVaulDocument.setStatus(true);
			taxVaultDocumentService.updateDocument(taxVaulDocument);  
			
			return mapStrOk(msg);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	
	
	
	@RequestMapping(value ="/taxVault/taxVaultPDF.action", method = RequestMethod.GET)
	public void openDocumentByUuid(HttpServletResponse response, 
			@RequestParam int id) throws IOException {
		
		PDFUtils pdfUtils = new PDFUtils();
		TaxVaultDocument fdoc = taxVaultDocumentService.getById(id);
		
		byte[] pdfBytes = pdfUtils.getTaxVaulDocumentPDF(fdoc);
	 if(pdfBytes.length>0) {
		
		String fileName = "Factura_"+fdoc.getUuid()+".pdf";
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
	
	@RequestMapping(value ="/taxtVault/openCoverBatchReportBatch.action", method = RequestMethod.GET)
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
	
	
	@RequestMapping(value ="/taxVault/uploadInvoiceTaxVault.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadFileRequest(  @ModelAttribute MultiFileUploadBean  form, BindingResult result, HttpServletResponse response) {	
		String xmlContent = null,
			   esp = "",engl="";
		
		ResponseGeneral responseGs=null;
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		String origen = null;
		try {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 //origen=request.getParameter("origen");
			
		Users usuario= usersService.getByUserName(auth.getName());		
		if(usuario==null) {
			ResponseGeneral responseG=new ResponseGeneral(true,"uploadFileRequestUSERNull","error");
			responseG.addMensaje("es","Se perdio la sesión, inicia sesión otra vez");
			responseG.addMensaje("en","Lost session, log in again");
			 return mapMsg(responseG);
		}
		int number=0;
		
		//DACG
		//getDownloadCFDIByUuidRunner.getDownloadCFDI(form.getUploadedFiles());
		//Thread.sleep(30000 * form.getUploadedFiles().size());
		//log4j.info("DACG");
		for (MultipartFile file : form.getUploadedFiles()) {
			
			number=number+1;
			String styleL=number%2==0?" style=\"background-color: lightgray\"":"";
		
		TaxVaultDocument invoiceToSave=new TaxVaultDocument();
		
		
		
		 
		 if(!file.getOriginalFilename().toUpperCase().endsWith("XML")) {
       	 
       	esp=esp+" <tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td>El documento ingresado no es un XML</td></tr>";
       	engl=engl+" <tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td>The entered document is not an XML</td></tr>";
       	continue;
        }
		
		 
		ResponseGeneral respu=documentsService.validateInvoiceVsSat(file.getBytes());
		
//		ResponseGeneral respu=documentsService.validateInvoiceVsSat(file);
		 
		 if(respu.isError()) {
        	 System.err.println(new Gson().toJson(respu));
        	 
        	esp=esp+" <tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td>"+respu.getMensaje().get("es")+"</td></tr>";
        	engl=engl+" <tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td>"+respu.getMensaje().get("en")+"</td></tr>";
        	continue;
//         	return  mapMsg(respu);
         }else {
        	 if(respu.getDocument()!=null) {
//        		 CommonsMultipartFile bade=new CommonsMultipartFile(new BASE64DecodedMultipartFile(file, respu.getDocument()));
//        		 file.setFile(bade);
        		 file=new DecodedMultipartFileCompatibility(file, respu.getDocument());
        	 }
         }
		 
		 
		
		 InvoiceDTO inv = documentsService.getInvoiceXml(file);
		 if(inv.getResponse().isError()) {
			 
			 esp=esp+"<tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td>"+inv.getResponse().getMensaje().get("es")+"</td></tr>";
	        	engl=engl+"<tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td> "+inv.getResponse().getMensaje().get("en")+"</td></tr>";
	        	continue;
//			 return mapMsg(inv.getResponse());
		 }
		 
		 if(!"I".equals(inv.getTipoComprobante())){
			 if(!"P".equals(inv.getTipoComprobante())) {
				 if(!"E".equals(inv.getTipoComprobante())) {
					 ResponseGeneral responseG=new ResponseGeneral(true,"uploadFileRequest","error");
						responseG.addMensaje("es","Tipo de documento no aceptado, solo es valido Facturas, Complementos de pago, Notas de credito o Notas de cargo");
						responseG.addMensaje("en","Type of document not accepted, it is only valid Invoices, Payment Supplements, Credit Notes or Charge Notes");
						
						esp=esp+"<tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td>"+responseG.getMensaje().get("es")+"</td></tr>";
				        	engl=engl+"<tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td>"+responseG.getMensaje().get("en")+"</td></tr>";
				        	continue;
//						return mapMsg(responseG);
				 }
			 }
		 }
		 
		 /*if(origen.equals("COMPLEMENTO")) {
			 if(!inv.getTipoComprobante().equals("P")){
				 if(inv.getTipoComprobante().equals("I")) {
					 ResponseGeneral responseG=new ResponseGeneral(true,"uploadFileRequest","error");
						responseG.addMensaje("es","Este documento es una factura, debera ingresar un complemento valido");
						responseG.addMensaje("en","This document is a invoice,you must enter a valid complement ");
						 return mapMsg(responseG);
				 }else {
					 ResponseGeneral responseG=new ResponseGeneral(true,"uploadFileRequest","error");
						responseG.addMensaje("es","Type of voucher not accepted");
						responseG.addMensaje("en","");
						 return mapMsg(responseG);
				 }
				 
			 }
		 }else {
		 if(!inv.getTipoComprobante().equals("I")) {
			 if(inv.getTipoComprobante().equals("P")) {
				 ResponseGeneral responseG=new ResponseGeneral(true,"uploadFileRequest","error");
					responseG.addMensaje("es","Este documento es un complemento, debera ingresar una factura valida");
					responseG.addMensaje("en","This document is a complement,you must enter a valid invoice ");
					 return mapMsg(responseG);
			 }else {
				 ResponseGeneral responseG=new ResponseGeneral(true,"uploadFileRequest","error");
					responseG.addMensaje("es","Type of voucher not accepted");
					responseG.addMensaje("en","");
					 return mapMsg(responseG);
			 }
		 }
		 }*/
		 
		 
		 ByteArrayInputStream stream = new  ByteArrayInputStream(file.getBytes());
			
			try {
				
				xmlContent = IOUtils.toString(stream, "UTF-8");
				xmlContent = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
				xmlContent = xmlContent.replace("?<?xml", "<?xml");
			} catch (IOException e) {
				log4j.error("Exception" , e);
				ResponseGeneral responseG=new ResponseGeneral(true,"uploadFileRequest",e);
				responseG.addMensaje("es",(origen.equals("COMPLEMENTO")?"El complemento  no es valido.":"La factura  no es valida."));
				responseG.addMensaje("en",(origen.equals("COMPLEMENTO")?"The complemen":"The invoice ")+ "is not valid.");
				e.printStackTrace();
				log4j.error(new Gson().toJson(responseG));
				log4j.error("Factura: "+xmlContent);
				
				 esp=esp+"<tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td>"+responseG.getMensaje().get("es")+"</td></tr>";
		        	engl=engl+"<"+styleL+"tr><td>"+file.getOriginalFilename()+"</td><td>"+responseG.getMensaje().get("en")+"</td></tr>";
				
				continue;
//				return mapMsg(responseG);
				
			}
			
		
		 ResponseGeneral res=documentsService.validateInvoiceTaxVault(inv, xmlContent);
		 
		 if(res.isError()) {
			 
			 esp=esp+"<tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td> "+res.getMensaje().get("es")+"</td></tr>";
	        	engl=engl+"<tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td> "+res.getMensaje().get("en")+"</td></tr>";
			 continue;
//			 return mapMsg(res);
		 }
		 
		 
		
		 
		 
		 List<UDC> lista = udcService.searchBySystemBoolean("FISCAL_PERIOD",true);
		 Cronato cron=new Cronato();
		 
		 String resp_es="";
		 String resp_en="";
		 
		 switch(inv.getTipoComprobante()) {
		  case "I":
			  origen = "FACTURA";
			  resp_es="Factura cargada con éxito.";
			  resp_en="Invoice loaded successfully.";
		    break;
		  case "P":
			  origen = "COMPLEMENTO";
			  resp_es="El complemento de pago se cargó correctamente.";
			  resp_en="Pay complement loaded successfully.";
		    break;
		  case "E":
			  origen = "NOTA_CREDITO";
			  resp_es="Nota de credito cargada con éxito.";
			  resp_en="Credit note loaded successfully.";
		  default:
			break;
		    // code block
		}
	
		 
		 invoiceToSave.setAmount(inv.getTotal());
		 invoiceToSave.setContent(file.getBytes());
		 invoiceToSave.setDocumentType(file.getContentType());
		 invoiceToSave.setType(origen);
		 invoiceToSave.setFolio(inv.getFolio());
		 invoiceToSave.setHostname(request.getServerName());
		 invoiceToSave.setInvoiceDate(inv.getFechaTimbrado());
		 invoiceToSave.setIp(request.getRemoteAddr());
		 invoiceToSave.setNameFile(file.getOriginalFilename());
		 invoiceToSave.setRfcEmisor(inv.getRfcEmisor());
		 invoiceToSave.setRfcReceptor(inv.getRfcReceptor());
		 invoiceToSave.setSerie(inv.getSerie());
		 invoiceToSave.setSize(file.getSize());
		 invoiceToSave.setStatus(true);
		 invoiceToSave.setUploadDate(new Date());
		 invoiceToSave.setUsuario(usuario.getUserName().toUpperCase());
		 invoiceToSave.setUuid(inv.getUuid());
		 invoiceToSave.setYear(inv.getFecha().substring(0, 4));
		 invoiceToSave.setEmisor(inv.getEmisor().getNombre());
		 invoiceToSave.setOrigen(origen);
		 
		 //Multiusuario
		 if("ROLE_SUPPLIER".equals(usuario.getRole())) {
			invoiceToSave.setAddressNumber(usuario.getAddressNumber().trim());
		 }
			
		String date= new SimpleDateFormat("dd/MM/yyyy").format(invoiceToSave.getUploadDate());
		invoiceToSave.setDocumentStatus("PENDIENT");
		for (UDC udc : lista) {
			if(invoiceToSave.getYear().equals(udc.getIntValue()+"")) {
				invoiceToSave.setDocumentStatus("COMPLETED");
				break;
			}
		}

		taxVaultDocumentService.saveDocument(invoiceToSave);
    	
		if(invoiceToSave.getDocumentStatus().equals("COMPLETED")) {
			taxVaultDocumentService.sedMailUnic(invoiceToSave);
		}
		
		 esp=esp+"<tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td> "+resp_es+"</td></tr>";
     	engl=engl+"<tr"+styleL+"><td>"+file.getOriginalFilename()+"</td><td>"+resp_en+"</td></tr>";
		
		try {
			DataAudit dataAudit = new DataAudit();
			dataAudit.setAction("UploadInvoiceTaxVault");
	    	dataAudit.setAddressNumber(usuario.getAddressNumber());
	    	dataAudit.setCreationDate(new Date());
	    	dataAudit.setDocumentNumber(null);
	    	dataAudit.setIp(request.getRemoteAddr());
	    	dataAudit.setMethod("uploadFileRequest");
	    	dataAudit.setModule(AppConstants.TAXVAULT_MODULE);    	
	    	dataAudit.setOrderNumber(null);
	    	dataAudit.setUuid(invoiceToSave.getUuid());
	    	dataAudit.setStep(null);
	    	dataAudit.setMessage("Upload Invoice TaxVault");
	    	dataAudit.setNotes(null);
	    	dataAudit.setStatus(invoiceToSave.getDocumentStatus());
	    	dataAudit.setUser(usuario.getUserName());
	    	dataAuditService.save(dataAudit);
		} catch (Exception e) {
			log4j.error("Exception" , e);
		}
		
//		return mapMsg(responseG);	
		}
		
		responseGs=new ResponseGeneral(false,"uploadFileRequest");
		responseGs.addMensaje("es","Proceso Completo </br><div style=\"overflow:auto; height:200px;\"><table style=\"background-color: white;table-layout: fixed;\">"+esp+"</table></div>");
		responseGs.addMensaje("en","Complete process </br> <div style=\"overflow:auto; height:200px;\"><table style=\"background-color: white;table-layout: fixed;\">"+engl+"</table></div>");
		return mapMsg(responseGs);	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log4j.error("Exception" , e);
			ResponseGeneral responseG=new ResponseGeneral(true,"uploadFileRequest",e);
			responseG.addMensaje("es",(origen.equals("COMPLEMENTO")?"El complemento no cargado":"Factura no cargada")+ " con éxito.");
			responseG.addMensaje("en",(origen.equals("COMPLEMENTO")?"Complement":"Invoice")+ " not loaded successfully.");
			e.printStackTrace();
			log4j.error(new Gson().toJson(responseG));
			log4j.error("Factura: "+xmlContent);
			
			return mapMsg(responseG);
		}
		
	}

	@RequestMapping(value ="/taxVault/upFileExtTaxVault.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> upFileExtTaxVault(FileUploadBean file, BindingResult result,  int fdId, HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		TaxVaultDocument invoiceToSave=new TaxVaultDocument();
		 
		 invoiceToSave.setContent(file.getFile().getBytes());
		 invoiceToSave.setDocumentType(file.getFile().getContentType());
		 invoiceToSave.setType("ANEXO");
		 invoiceToSave.setHostname(request.getServerName());
		 invoiceToSave.setIp(request.getRemoteAddr());
		 invoiceToSave.setNameFile(file.getFile().getOriginalFilename());
		 invoiceToSave.setSize(file.getFile().getSize());
		 invoiceToSave.setStatus(true);
		 invoiceToSave.setUploadDate(new Date());
		 invoiceToSave.setUsuario(auth.getName());
		 invoiceToSave.setUuid(fdId+"");///relacion con la factura con id de factura
		
		taxVaultDocumentService.saveDocument(invoiceToSave);
		invoiceToSave.setContent(null);
		
		return mapOK(invoiceToSave);
	

	}
	
	@RequestMapping(value ="/taxtVault/uploadWorker.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadWorker(
											String employeeName,
											String membershipIMSS,
											String datefolioIDcard,
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String uuid= (String) request.getSession().getAttribute("accessPlantUUID");
		uuid=uuid==null?UUID.randomUUID().toString():uuid;
		request.getSession().setAttribute("accessPlantUUID", uuid);
		
		PlantAccessWorker trabajador=new PlantAccessWorker();
		
		trabajador.setFechaRegistro(new Date());
		trabajador.setRequestNumber(uuid);
		trabajador.setDatefolioIDcard(datefolioIDcard);
		trabajador.setEmployeeName(employeeName);
		trabajador.setMembershipIMSS(membershipIMSS);
		
		plantAccessWorkerService.save(trabajador);
		
		return mapOK(trabajador);
	

	}
	
	@RequestMapping(value ="/taxtVault/uploadPlantAccesRequest.action", method = RequestMethod.POST)
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
											HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		 
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		PlantAccessRequest solicituddto= new PlantAccessRequest();
		
		solicituddto.setStatus(status);
		solicituddto.setAprovUser(aprovUser);
		solicituddto.setNameRequest(nameRequest);
		solicituddto.setOrdenNumber(ordenNumber);
		solicituddto.setContractorCompany(contractorCompany);
		solicituddto.setContractorRepresentative(contractorRepresentative);
		solicituddto.setDescriptionUbication(descriptionUbication);
		solicituddto.setHighRiskActivities(highRiskActivities);
		solicituddto.setFechaSolicitud(new Date());
		
		
		String uuid= (String) request.getSession().getAttribute("accessPlantUUID");
		uuid=uuid==null?UUID.randomUUID().toString():uuid;
		plantAccessRequestService.save(solicituddto);
		plantAccessWorkerService.updateWorkerRequest(uuid, solicituddto.getId()+"");
		fileStoreService.updateFileRequest(uuid, solicituddto.getId()+"");
		
		
		
		
		return mapOK(solicituddto);
	

	}

	@SuppressWarnings("unused")
	@RequestMapping(value ="/taxVault/updateAprov.action")
	public @ResponseBody Map<String, Object> update(@RequestBody PlantAccessRequest obj,
													@RequestParam String status,
													@RequestParam String note,
													@RequestParam String idReques
													){
		
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			String msg = "";
			if(obj.getId() != 0) {				
				obj=plantAccessRequestService.getById(obj.getId());
				obj.setStatus(status);
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
	
	
	@RequestMapping(value = "/taxVault/listDocumentsAnex.action")
	public @ResponseBody
	Map<String, Object> viewByOrder(@RequestParam int start,
									  @RequestParam int limit,
									  @RequestParam String idFact) {
		List<TaxVaultDocument> list = null;
		int total = 0;
		try {
			list = taxVaultDocumentService.getTaxVaultDocumentsByIdFact(idFact, start, limit);
			list.add(taxVaultDocumentService.getById(Integer.parseInt(idFact)));
			

			if(list != null){
				total = list.size();
				for(TaxVaultDocument ud : list){
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
	
	@RequestMapping(value = "/taxVault/listPeriodoFiscal.action")
	public @ResponseBody
	Map<String, Object> listPeriodoFiscal(@RequestParam int start,
									  @RequestParam int limit) {
		List<UDC> list = null;
		int total = 0;
		try {
			list = udcService.searchBySystem("FISCAL_PERIOD");
			
			if(list != null){
				total = list.size();
			}
			else
				total = 0;
			
			return mapOKUDC(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/taxVault/actDesActPeriodoFiscal.action")
	public @ResponseBody
	Map<String, Object> actDesActPeriodoFiscal(int idFact) {
		
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			UDC period=udcService.getUdcById(idFact);
			period.setBooleanValue(!period.isBooleanValue());
			udcService.update(period, new Date(), usr);
			taxVaultDocumentService.sendMailDemon();
			return mapOK(period);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/taxVault/addePeriodoFiscal.action")
	public @ResponseBody
	Map<String, Object> addePeriodoFiscal( String periodoYear) {
		
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();
			UDC period=new UDC();
			
			period.setUdcSystem("FISCAL_PERIOD");
			period.setUdcKey("PERIOD");
			period.setIntValue(Integer.parseInt(periodoYear));
			if(udcService.search(period)!=null) {
				return mapError("Este periodo ya ha sido registrado antes.");
			}
			
			period.setBooleanValue(true);
			period.setCreatedBy(usr);
			period.setBooleanValue(true);
			udcService.save(period, new Date(), usr);
			
			return mapOK(period);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
//	@RequestMapping(value = "/taxVault/addePeriodoFiscal.action")
//	public @ResponseBody
//	Map<String, Object> addePeriodoFiscal( String from, String to) {
//		
//		try {
//			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//			String usr = auth.getName();
//			UDC period=new UDC();
//			period.setBooleanValue(true);
//			period.setUdcSystem("FISCAL_PERIOD");
//			period.setUdcKey("PERIOD");
//			period.setCreatedBy(usr);
//			
//			if(new SimpleDateFormat("yyyy-MM-dd").parse(from).after(new SimpleDateFormat("yyyy-MM-dd").parse(to))) {
//				String temp=from;
//				from=to;
//				to=temp;
//			}
//			
//			from= new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(from));
//			to=new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(to));
//			
//			period.setStrValue1(from);
//			period.setStrValue2(to);
//			
//			period.setBooleanValue(true);
//			udcService.save(period, new Date(), usr);
//			
//			return mapOK(period);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return mapError(e.getMessage());
//		}
//	}
	@RequestMapping(value ="/taxVault/openDocument.action", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, 
    			             @RequestParam int id) throws IOException {
     
		TaxVaultDocument doc = taxVaultDocumentService.getById(id);
		String fileName = doc.getNameFile();
		String contentType = doc.getDocumentType();
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
	
	@RequestMapping(value ="/taxVault/deleteDocument.action", method = RequestMethod.GET)
    public void deleteDocument(HttpServletResponse response, 
    			             @RequestParam int idFact) throws IOException {
     
		TaxVaultDocument doc = taxVaultDocumentService.getById(idFact);
		taxVaultDocumentService.delete(doc);
    }
	
	public static String takeOffBOM(InputStream inputStream) throws IOException {
	    BOMInputStream bomInputStream = new BOMInputStream(inputStream);
	    return IOUtils.toString(bomInputStream, "UTF-8");
	}


	public Map<String,Object> mapOK(List<TaxVaultDocument> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	public Map<String,Object> mapOKUDC(List<UDC> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	public Map<String,Object> mapOK(TaxVaultDocument invoiceToSave){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("data", invoiceToSave);
		modelMap.put("success", true);
		return modelMap;
	}
	public Map<String,Object> mapOK(UDC invoiceToSave){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("data", invoiceToSave);
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
	public Map<String,Object> mapMsg(Map<String, String> msg,boolean error){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", !error);
		return modelMap;
	}
	
	public Map<String,Object> mapMsg(ResponseGeneral resp){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("info", resp);
		modelMap.put("success", !resp.isError());
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
	
	private String getFirstTimeDate(String date) {
		return date.substring(0, 10).concat(" 00:00:00");
	}
	
	private String getLastTimeDate(String date) {
		return date.substring(0, 10).concat(" 23:59:59");
	}

	}
