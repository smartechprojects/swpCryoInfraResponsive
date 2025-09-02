package com.eurest.supplier.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eurest.supplier.dao.CodigosSatDao;
import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.dto.ResponseGeneral;
import com.eurest.supplier.invoiceXml.DoctoRelacionado;
import com.eurest.supplier.invoiceXml.Pago;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.CustomBroker;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.service.BatchProcessService;
import com.eurest.supplier.service.CodigosSATService;
import com.eurest.supplier.service.DocumentsService;
import com.eurest.supplier.service.EmailService;
import com.eurest.supplier.service.FTPService;
import com.eurest.supplier.service.FiscalDocumentService;
import com.eurest.supplier.service.MassiveDownloadService;
import com.eurest.supplier.service.CustomBrokerService;
import com.eurest.supplier.service.DataAuditService;
import com.eurest.supplier.service.PurchaseOrderService;
import com.eurest.supplier.service.SupplierService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.BASE64DecodedMultipartFile;
import com.eurest.supplier.util.FileUploadBean;
import com.eurest.supplier.util.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.sf.json.JSONObject;
 
@Controller
public class FileUploadController {
 
   	@Autowired
	DocumentsService documentsService;
   	
   	@Autowired
	PurchaseOrderService purchaseOrderService;
   	
   	@Autowired
   	FiscalDocumentService fiscalDocumentService;

   	@Autowired
   	CustomBrokerService customBrokerService;
	   	
	@Autowired
	UdcService udcService;
	
	@Autowired
	SupplierService supplierService;
	
	@Autowired
	CodigosSATService codigosSATService;
	
	@Autowired
	private CodigosSatDao codigosSatDao;
	
	@Autowired
	Logger logger;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	FTPService ftpService;
	
    @Autowired
    MassiveDownloadService massiveDownloadService;
	
	@Autowired
	DataAuditService dataAuditService;
	
	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(FileUploadController.class);
	
	 @RequestMapping(value = "/uploadInvoiceFromReceipt.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	    public @ResponseBody String uploadInvoiceFromReceipt(FileUploadBean uploadItem, 
	    										  BindingResult result, 
	    										  String orderCompany, 
	    										  String addressBook, 
												  int documentNumber, 
												  String documentType,
												  String tipoComprobante,
												  String receiptIdList,
												  String plantCodePO,
												  String docsOtherUUID,
									    		  HttpServletResponse response){
	 
		 
		 
		 
	    	response.setContentType("text/html");
	        response.setCharacterEncoding("UTF-8");
	    	JSONObject json = new JSONObject();
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	 		Date currentDate = new Date();
			String usr = auth.getName();
			
	        try{

	        if (result.hasErrors()){
	            for(ObjectError error : result.getAllErrors()){
	                System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
	            }
	 
	            json.put("success", false);
	            json.put("message", "Error_1");
	        }
	        
	        InvoiceDTO inv = null;
	        String ct = uploadItem.getFile().getContentType();
	        
	        if(uploadItem.getFileTwo() != null) {
	            String ctPdf = uploadItem.getFileTwo().getContentType();
	            if(!"application/pdf".equals(ctPdf)) {
	            	json.put("success", false);
	            	json.put("message", "Error_2");
	            	return json.toString();
	            }
	        }
	        
	        if(uploadItem.getFileThree() != null && uploadItem.getFileThree().getSize() >0) {
	            String ctPdf = uploadItem.getFileThree().getContentType();
	            if(!"application/pdf".equals(ctPdf)) {
	            	json.put("success", false);
	            	json.put("message", "Error_2");
	            	return json.toString();
	            }
	        }
	        

	        if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)){
	            if("text/xml".equals(ct.trim())){
	           	 ResponseGeneral respu=documentsService.validateInvoiceVsSat(uploadItem);
	    		 if(respu.isError()) {
	            	 System.err.println(new Gson().toJson(respu));
	            	 json.put("success", false);
		            	json.put("message", "Intente ingresar la factura mañana.");
		            	return json.toString();
		            	
	             }else {
	            	 if(respu.getDocument()!=null) {
	            		 CommonsMultipartFile bade=new CommonsMultipartFile(new BASE64DecodedMultipartFile(uploadItem.getFile().getFileItem(), respu.getDocument()));
	            		 uploadItem.setFile(bade);
	            	 }
	             }
	            	
	                inv = documentsService.getInvoiceXml(uploadItem);
	                if(!inv.getResponse().isError()){
	                	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante) && !"I".equals(inv.getTipoComprobante())){
	                		json.put("success", false);
	                    	json.put("message", "Error_3");
	                    	return json.toString();
	                	}
	                	                	
	                    ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getFile().getBytes());
	                    String xmlContent = IOUtils.toString(stream, "UTF-8");
	                    String source = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
	        			String xmlString = source.replace("?<?xml", "<?xml");
	                    
	        			PurchaseOrder po = purchaseOrderService.searchbyOrderAndAddressBookAndCompany(documentNumber, addressBook, documentType, orderCompany);

	                	String res = documentsService.validateInvoiceFromOrder(inv,
	                														   addressBook, 
																			   documentNumber, 
																			   documentType,
																			   tipoComprobante,
																			   po,
																			   true,
																			   xmlString,
																			   receiptIdList,
																			   false,
																			   plantCodePO,
																			   usr);

	                	if("".equals(res) || res.contains("DOC:")){
	                				        			
	                    	UserDocument doc;
							String fileName;							
							try {
								
								/*
								//Valida si el XML contiene caracteres especiales para el set de caracteres actual
								byte[] xmlContentFinal = uploadItem.getFile().getBytes();								
								String contentString = new String(uploadItem.getFile().getBytes());
    		        	        String regex = "[^\\u0000-\\u00FF]";
    		        	        Pattern pattern = Pattern.compile(regex);
    		        	        Matcher matcher = pattern.matcher(contentString);
    		        	        
    		        	        if (matcher.find()) {
    		        	            log4j.info("El XML de la factura " + inv.getUuid() + " contiene caracteres especiales.");
    		        	            xmlContentFinal = takeOffSpecialChars(contentString).getBytes();
    		        	        }*/
								
								doc = new UserDocument(); 
								doc.setAddressBook(po.getAddressNumber());
								doc.setDocumentNumber(documentNumber);
								doc.setDocumentType(documentType);
								doc.setContent(uploadItem.getFile().getBytes());
								doc.setType(ct.trim());
								fileName = uploadItem.getFile().getOriginalFilename().replace(" ", "_"); 
								doc.setName(fileName);
								//doc.setName("FAC_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + "_" + getTimeSuffix() + ".xml");
								doc.setSize(uploadItem.getFile().getSize());
								doc.setStatus(true);
								doc.setAccept(true);
								doc.setFiscalType(tipoComprobante);
								doc.setType("text/xml");
								doc.setFolio(inv.getFolio() != null ? inv.getFolio() : inv.getUuid().substring(inv.getUuid().length() - 4));
								doc.setSerie(inv.getFolio() != null ? inv.getSerie() : "ZX");
								doc.setUuid(inv.getUuid());
								doc.setUploadDate(new Date());
								doc.setDescription("MainUUID_".concat(inv.getUuid()));
								doc.setFiscalRef(0);
								documentsService.save(doc, new Date(), "");
								
								dataAuditService.saveDataAudit("UploadInvoiceFromReceipt", addressBook, currentDate, request.getRemoteAddr(),
								usr, "Uploaded Invoice Successful", "uploadInvoiceFromReceipt", null,receiptIdList, po.getOrderNumber()+"", null, 
								inv.getUuid(), AppConstants.STATUS_COMPLETE, AppConstants.SALESORDER_MODULE);
							} catch (Exception e) {
								log4j.error("Exception" , e);
								e.printStackTrace();
							}
	                            	
	                    	
	                    	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)){
	                    		
	                    		try {
									doc = new UserDocument(); 
									doc.setAddressBook(po.getAddressNumber());
									doc.setDocumentNumber(documentNumber);
									doc.setDocumentType(documentType);
									doc.setContent(uploadItem.getFileTwo().getBytes());
									doc.setType(uploadItem.getFileTwo().getContentType().trim());
									fileName = uploadItem.getFileTwo().getOriginalFilename().replace(" ", "_");
									doc.setName(fileName);
									//doc.setName("FAC_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + "_" + getTimeSuffix() + ".pdf");
									doc.setSize(uploadItem.getFileTwo().getSize());
									doc.setStatus(true);
									doc.setAccept(true);
									doc.setFiscalType(tipoComprobante);
									doc.setType("application/pdf");
									doc.setFolio(inv.getFolio() != null ? inv.getFolio() : inv.getUuid().substring(inv.getUuid().length() - 4));
									doc.setSerie(inv.getFolio() != null ? inv.getSerie() : "ZX");
									doc.setUuid(inv.getUuid());
									doc.setDescription("MainUUID_".concat(inv.getUuid()));
									doc.setUploadDate(new Date());
									doc.setFiscalRef(0);
									documentsService.save(doc, new Date(), "");
								} catch (Exception e) {
									log4j.error("Exception" , e);
									e.printStackTrace();
								}
	                        	
	                        	if(uploadItem.getFileThree() !=null && uploadItem.getFileThree().getSize() >0) {
	                        		
	                       		try {
									doc = new UserDocument(); 
									doc.setAddressBook(po.getAddressNumber());
									doc.setDocumentNumber(documentNumber);
									doc.setDocumentType(documentType);
									doc.setContent(uploadItem.getFileThree().getBytes());
									doc.setType(uploadItem.getFileThree().getContentType().trim());
									fileName = uploadItem.getFileThree().getOriginalFilename().replace(" ", "_");
									doc.setName(fileName);
									//doc.setName("FAC_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + "_" + getTimeSuffix() + ".pdf");
									doc.setSize(uploadItem.getFileThree().getSize());
									doc.setStatus(true);
									doc.setAccept(true);
									doc.setFiscalType(AppConstants.EVIDENCE_FIELD);
									doc.setType("application/pdf");
									doc.setFolio(inv.getFolio() != null ? inv.getFolio() : inv.getUuid().substring(inv.getUuid().length() - 4));
									doc.setSerie(inv.getFolio() != null ? inv.getSerie() : "ZX");
									doc.setUuid(inv.getUuid());
									doc.setDescription("MainUUID_".concat(inv.getUuid()));
									doc.setUploadDate(new Date());
									doc.setFiscalRef(0);
									documentsService.save(doc, new Date(), "");
								} catch (Exception e) {
									log4j.error("Exception" , e);
									e.printStackTrace();
								}
	                        	
	                        	}
	                        	
	                        	//Actualizacion de Otros documentos
	                        	
	                        	//List<UserDocument> docsOthers = documentsService.searchCriteriaByFiscalType(documentNumber,po.getAddressNumber(),"Otros");
	                        	if(docsOtherUUID!=null && !"".equals(docsOtherUUID)) {
	                        		
	                        		
	                        		docsOtherUUID = docsOtherUUID.substring(0, docsOtherUUID.length()-1);
	                        		
	                        		String[] uuidsOthers = docsOtherUUID.split(",");
	                        		
	                        		for( String uuid : uuidsOthers ) {
	                        		List<UserDocument> docsOthers = documentsService.searchCriteriaByDescription(po.getAddressNumber(), "MainUUID_".concat(uuid),true);
		        					
		                        	for(UserDocument u : docsOthers) {
		                        		
		    							u.setDescription("MainUUID_".concat(inv.getUuid()));
			                        	u.setFolio(inv.getFolio() != null ? inv.getFolio() : inv.getUuid().substring(inv.getUuid().length() - 4));
			                        	u.setSerie(inv.getFolio() != null ? inv.getSerie() : "ZX");
		    							u.setUuid(inv.getUuid());
		    							documentsService.updateDocuments(u);
		                        		
		    						}
	                        		}
	                        		
	                        	}
	                        	
	                        	
	                        	
	    	                	
	                        	//Send pdf to remote server
	                        	
	                       /* 		CommonsMultipartFile cFile = uploadItem.getFileTwo();
		    	                	File convFile = new File(System.getProperty("java.io.tmpdir")+"/" + inv.getUuid() + ".pdf");
		    	                	cFile.transferTo(convFile);
		    	                
		    	                	documentsService.sendFileToRemote(convFile, inv.getUuid() + ".pdf");
								
	                        	
	                        	ftpService.setServices(null, purchaseOrderService, documentsService, udcService, logger);
	    	                	//Send xml to SFTP server
	    	                	ftpService.sendToSftpServer(xmlString, uploadItem.getFile().getOriginalFilename());
	    	                	//Send PDF to SFTP server
	    	                	ftpService.sendToSftpServer(convFile, inv.getUuid() + ".pdf");
	    	                	
	                        	po.setInvoiceUuid(inv.getUuid());
	                    		po.setInvoiceNumber(inv.getFolio() + "");
	                    		po.setPaymentType(inv.getMetodoPago());
	                    		po.setStatus(AppConstants.STATUS_OC_INVOICED);
	                    		po.setOrderStauts(AppConstants.STATUS_OC_INVOICED);*/
	                    	}
	                    	
	                    	
	                    	if(AppConstants.NC_FIELD.equals(tipoComprobante)){
	                    		po.setPaymentUuid(inv.getUuid());
	                    		po.setStatus(AppConstants.STATUS_LOADNC);
	                    	}
	                    	
	                    	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante)){
	                    		po.setCreditNotUuid(inv.getUuid());
	                    		po.setStatus(AppConstants.STATUS_LOADCP);
	                    	}
	                    	
	                    	
	                    	purchaseOrderService.updateOrders(po);
	                    	
	        	            json.put("success", true);
	        	            json.put("message", inv.getResponse().getMensaje().get("es"));
	        	            json.put("orderNumber", documentNumber);
	        	            json.put("orderType", documentType);
	        	            json.put("addressNumber", addressBook);
	        	            json.put("docNbr", res);
	        	            json.put("uuid", inv.getUuid());
	        	         	        	            
	                	}else{
	                    	json.put("success", false);
	                    	json.put("message", res);
	                	}

	                }else{
	                	json.put("success", false);
	                	json.put("message", "Error_4");
	                }
	                
	            }else{
	            	json.put("success", false);
	            	json.put("message", "Error_5");
	            }
	        }else{
	        	
	            if(uploadItem.getFile() != null) {
	                String ctPdf = uploadItem.getFile().getContentType();
	                if(!"application/pdf".equals(ctPdf)) {
	                	json.put("success", false);
	                	json.put("message", "Error_6");
	                	return json.toString();
	                }
	            }
	        	
	        	PurchaseOrder po = purchaseOrderService.searchbyOrder(documentNumber, documentType);
	        	UserDocument doc = new UserDocument(); 
	        	
	        	if(po != null) {
	        		
	        		String fileName = uploadItem.getFile().getOriginalFilename();
	        		fileName = fileName.replaceAll(" ", "_");
	        		
	            	doc.setAddressBook(po.getAddressNumber());
	            	doc.setDocumentNumber(documentNumber);
	            	doc.setDocumentType(documentType);
	            	doc.setContent(uploadItem.getFile().getBytes());
	            	doc.setType(ct.trim());
	            	doc.setName(fileName);
	            	doc.setSize(uploadItem.getFile().getSize());
	            	doc.setStatus(true);
	            	doc.setAccept(true);
	            	doc.setFiscalType(tipoComprobante);
	            	doc.setUuid("");
	            	doc.setUploadDate(new Date());
	            	doc.setFiscalRef(0);
	            	documentsService.save(doc, new Date(), "");
	            	
	                json.put("success", true);
	                json.put("message", "El archivo ha sido cargado de forma exitosa");
	                json.put("orderNumber", documentNumber);
	                json.put("orderType", documentType);
	                json.put("addressNumber", addressBook);
	        	}else {
	                json.put("success", false);
	                json.put("message", "Error_7");
	        	}

	            
	        }
	        return json.toString();
	        
	        }catch(Exception e){
	        	log4j.error("Exception" , e);
	        	e.printStackTrace();
	        	json.put("success", false);
	            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
	        }
	        return json.toString();

	    }
	
	 
	 
	 @RequestMapping(value = "/uploadCreditNoteFromReceipt.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	    public @ResponseBody String uploadCreditNoteFromReceipt(FileUploadBean uploadItem, 
	    										  BindingResult result, 
	    										  String addressBook, 
												  int documentNumber, 
												  String documentType,
												  String tipoComprobante,
												  String receiptIdList,
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
	            json.put("message", "Error_1");
	        }
	        
	        InvoiceDTO inv = null;
	        String ct = uploadItem.getFile().getContentType();
	        
	        if(uploadItem.getFileTwo() != null) {
	            String ctPdf = uploadItem.getFileTwo().getContentType();
	            if(!"application/pdf".equals(ctPdf)) {
	            	json.put("success", false);
	            	json.put("message", "Error_2");
	            	return json.toString();
	            }
	        }
	        
	        if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)){
	            if("text/xml".equals(ct.trim())){
	            	ResponseGeneral respu=documentsService.validateInvoiceVsSat(uploadItem);
		    		 if(respu.isError()) {
		            	 System.err.println(new Gson().toJson(respu));
		            	 json.put("success", false);
			            	json.put("message", respu.getMensaje());
			            	return json.toString();
			            	
		             }else {
		            	 if(respu.getDocument()!=null) {
		            		 CommonsMultipartFile bade=new CommonsMultipartFile(new BASE64DecodedMultipartFile(uploadItem.getFile().getFileItem(), respu.getDocument()));
		            		 uploadItem.setFile(bade);
		            	 }
		             }
	                inv = documentsService.getInvoiceXml(uploadItem);
	                if(!inv.getResponse().isError()){
	                	
	                	if(AppConstants.NC_FIELD.equals(tipoComprobante) && !"E".equals(inv.getTipoComprobante())){
	                		json.put("success", false);
	                    	json.put("message", "Error_8");
	                    	return json.toString();
	                	}
	                	
	                    ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getFile().getBytes());
	                    String xmlContent = IOUtils.toString(stream, "UTF-8");
	                    String source = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
	        			String xmlString = source.replace("?<?xml", "<?xml");
	                    
	        			PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(documentNumber, addressBook, documentType);
	        			
	                	String res = documentsService.validateCreditNoteFromOrder(inv,
	                														   addressBook, 
																			   documentNumber, 
																			   documentType,
																			   tipoComprobante,
																			   po,
																			   true,
																			   xmlString,
																			   receiptIdList);
	                	
	                	
	                	if("".equals(res) || res.contains("DOC:")){
	                		int fisdocid=Integer.parseInt(res.split(":")[1]);
	                    	UserDocument doc = new UserDocument(); 
	                    	doc.setAddressBook(po.getAddressNumber());
	                    	doc.setDocumentNumber(documentNumber);
	                    	doc.setDocumentType(documentType);
	                    	doc.setContent(uploadItem.getFile().getBytes());
	                    	doc.setType(ct.trim());
                        	String fileName = uploadItem.getFile().getOriginalFilename();
	                    	fileName = fileName.replace(" ", "_");
                        	doc.setName(fileName);
	                    	doc.setName("NC_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + "_" + getTimeSuffix() + ".xml");
	                    	doc.setSize(uploadItem.getFile().getSize());
	                    	doc.setStatus(true);
	                    	doc.setAccept(true);
	                    	doc.setFiscalType(tipoComprobante);
	                    	doc.setType("text/xml");
	                    	doc.setFolio(inv.getFolio());
	                    	doc.setSerie(inv.getSerie());
	                    	doc.setUuid(inv.getUuid());
	                    	doc.setDescription("MainUUID_".concat(inv.getCfdiRelacionado()));
	                    	doc.setUploadDate(new Date());
	                    	doc.setFiscalRef(fisdocid);
	                    	documentsService.save(doc, new Date(), "");
	                    	
	                    	doc = new UserDocument(); 
                        	doc.setAddressBook(po.getAddressNumber());
                        	doc.setDocumentNumber(documentNumber);
                        	doc.setDocumentType(documentType);
                        	doc.setContent(uploadItem.getFileTwo().getBytes());
                        	doc.setType(uploadItem.getFileTwo().getContentType().trim());
                        	fileName = uploadItem.getFileTwo().getOriginalFilename();
                        	fileName = fileName.replace(" ", "_");
                        	doc.setName(fileName);
                        	doc.setSize(uploadItem.getFileTwo().getSize());
                        	doc.setStatus(true);
                        	doc.setAccept(true);
                        	doc.setFiscalType(tipoComprobante);
                        	doc.setType("application/pdf");
                        	doc.setFolio(inv.getFolio() != null ? inv.getFolio() : inv.getUuid().substring(inv.getUuid().length() - 4));
                        	doc.setSerie(inv.getFolio() != null ? inv.getSerie() : "ZX");
                        	doc.setUuid(inv.getUuid());
                        	doc.setDescription("MainUUID_".concat(inv.getCfdiRelacionado()));
                        	doc.setUploadDate(new Date());
                        	doc.setFiscalRef(fisdocid);
                        	documentsService.save(doc, new Date(), "");
                        	
//                        	if(uploadItem.getFileThree() !=null && uploadItem.getFileThree().getSize() >0) {
//                        		
//                       		doc = new UserDocument(); 
//                        	doc.setAddressBook(po.getAddressNumber());
//                        	doc.setDocumentNumber(documentNumber);
//                        	doc.setDocumentType(documentType);
//                        	doc.setContent(uploadItem.getFileThree().getBytes());
//                        	doc.setType(uploadItem.getFileThree().getContentType().trim());
//                        	fileName = uploadItem.getFileThree().getOriginalFilename();
//                        	fileName = fileName.replace(" ", "_");
//                        	doc.setName(fileName);
//                        	//doc.setName("FAC_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + "_" + getTimeSuffix() + ".pdf");
//                        	doc.setSize(uploadItem.getFileThree().getSize());
//                        	doc.setStatus(true);
//                        	doc.setAccept(true);
//                        	doc.setFiscalType(AppConstants.EVIDENCE_FIELD);
//                        	doc.setType("application/pdf");
//                        	doc.setFolio(inv.getFolio() != null ? inv.getFolio() : inv.getUuid().substring(inv.getUuid().length() - 4));
//                        	doc.setSerie(inv.getFolio() != null ? inv.getSerie() : "ZX");
//                        	doc.setUuid(inv.getUuid());
//                        	doc.setDescription("NC_EV_".concat(inv.getUuid()));
//                        	doc.setUploadDate(new Date());
//                        	doc.setFiscalRef(0);
//                        	documentsService.save(doc, new Date(), "");
//                        	
//                        	}
	                    	
	                    	
	                    	
	                    /* preguntar el envio a servidor
	                    	try {
	                        	//Send pdf to remote server
	                        	CommonsMultipartFile cFile = uploadItem.getFileTwo();
	    	                	File convFile = new File(System.getProperty("java.io.tmpdir")+"/" + inv.getUuid() + ".pdf");
	    	                	cFile.transferTo(convFile);
	    	                	documentsService.sendFileToRemote(convFile, inv.getUuid() + ".pdf");
	    	                	ftpService.setServices(null, purchaseOrderService, documentsService, udcService, logger);
	    	                	//Send xml to SFTP server Nota de Crédito
	    	                	ftpService.sendToSftpServer(xmlString, uploadItem.getFile().getOriginalFilename());
	    	                	//Send PDF to SFTP server Nota de Crédito
	    	                	ftpService.sendToSftpServer(cFile.getInputStream(), uploadItem.getFile().getOriginalFilename());
	    	                	
	    	                	
							} catch (Exception e) {
								log4j.error("Exception" , e);
								e.printStackTrace();
							}
    	                	*/
	                    	
	                    	
	                    	if(AppConstants.NC_FIELD.equals(tipoComprobante)){
	                    		po.setInvoiceUuid(inv.getUuid());
	                    		po.setInvoiceNumber(inv.getFolio() + "");
	                    		po.setPaymentType(inv.getMetodoPago());
	                    		po.setStatus(AppConstants.STATUS_OC_INVOICED);
	                    		po.setOrderStauts(AppConstants.STATUS_OC_INVOICED);
	                    	}
	                    	
	                    	purchaseOrderService.updateOrders(po);
	                    	
	        	            json.put("success", true);
	        	            json.put("message", inv.getResponse().getMensaje().get("es"));
	        	            json.put("orderNumber", documentNumber);
	        	            json.put("orderType", documentType);
	        	            json.put("addressNumber", addressBook);
	        	            json.put("docNbr", res);
	        	            json.put("uuid", inv.getUuid());
	                	}else{
	                		json.put("success", false);
	                    	json.put("message", res);
	                	}

	                }else{
	                	json.put("success", false);
	                	json.put("message", "Error_4");
	                }
	                
	            }else{
	            	json.put("success", false);
	            	json.put("message", "Error_5");
	            }
	        }
	        return json.toString();
	        
	        }catch(Exception e){
	        	log4j.error("Exception" , e);
	        	e.printStackTrace();
	        	json.put("success", true);
	            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
	        }
	        return json.toString();

	}
	 
	 @RequestMapping(value = "/uploadReceiptInvoiceZip.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	    public @ResponseBody String uploadReceiptInvoiceZip(FileUploadBean uploadItem, 
	    										  BindingResult result, 
	    										  String addressBook, 
												  int documentNumber, 
												  String documentType,
												  String tipoComprobante,
												  String receiptIdList,
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
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String usr = auth.getName();
	        
	        if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)){
	            if("application/x-zip-compressed".equals(ct.trim()) ||
	            	"application/zip".equals(ct.trim())){
	            	String message = documentsService.validateInvZipFile(uploadItem, result, addressBook, documentNumber, documentType, tipoComprobante, receiptIdList, usr);
	            	
	            	if(StringUtils.isBlank(message)) {
	            		json.put("success", true);
	            	} else {
	            		json.put("success", false);	
	            		json.put("message", message);
	            	}
	            }else{
	            	json.put("success", false);
	            	json.put("message", "Para cargas de archivos fiscales de forma masiva, sólo se permiten archivos .zip");
	            }
	        }
	        return json.toString();
	        
	        }catch(Exception e){
	        	log4j.error("Exception" , e);
	        	e.printStackTrace();
	        	json.put("success", true);
	            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
	        }
	        return json.toString();

	 }
	 
	 @SuppressWarnings("unused")
	 @RequestMapping(value = "/uploadSATInvoiceZip.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	 public @ResponseBody String uploadSATInvoiceZip(FileUploadBean uploadItem, 
	    										  BindingResult result,
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
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String usr = auth.getName();
	        
            if("application/x-zip-compressed".equals(ct.trim()) ||
	            	"application/zip".equals(ct.trim())){
            		massiveDownloadService.processZipFile("CARGA MANUAL", uploadItem.getFile().getBytes());
            		json.put("success", true);
            }else{
            	json.put("success", false);
            	json.put("message", "Para cargas de archivos fiscales de forma masiva, sólo se permiten archivos .zip");
            }
            
	        return json.toString();
	        
	        }catch(Exception e){
	        	log4j.error("Exception" , e);
	        	e.printStackTrace();
	        	json.put("success", true);
	            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
	        }
	        return json.toString();

	 }
	
    @RequestMapping(value = "/upload.action", method = RequestMethod.POST)
    @ResponseBody public String create(FileUploadBean uploadItem, 
    								   BindingResult result, 
    								   String addressBook, 
    								   int documentNumber, 
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
        
        //String ct = uploadItem.getFile().getContentType();
        //if("application/pdf".equals(ct.trim()) || "image/jpg".equals(ct.trim())|| "image/jpeg".equals(ct.trim())){
            
        if(uploadItem.getFile().getSize() <= 10000000) {
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		String usr = auth.getName();
    		
    		List<UserDocument> list = null;
    		list =  this.documentsService.searchByAddressNumber(addressBook);
        	
    	    for (UserDocument d : list) {
    	      if(documentType.equals(d.getDocumentType())) {
    	    	  this.documentsService.delete(d.getId(), "admin");  
    	      }
    	    }
    		
            documentsService.save(uploadItem, new Date(), usr, documentNumber, documentType, addressBook);

            json.put("success", true);
            json.put("message", "El archivo " + uploadItem.getFile().getOriginalFilename() + " ha sido cargado exitosamente.");
            json.put("fileName", uploadItem.getFile().getOriginalFilename());
        }else {
        	json.put("success", false);
            //json.put("message", "error_mb");
        	json.put("message", "El documento cargado supera los 10 MB");
        }
        /*
        }else{
        	json.put("success", false);
        	json.put("message", "Error: Sólo se permiten archivos tipo .pdf o .jpg");
        }
        */

        return json.toString();
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();

        }
        return json.toString();

    }
    
    @RequestMapping(value = "/uploadInvoiceFromOrder.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String uploadInvoiceFromOrder(FileUploadBean uploadItem, 
    										  BindingResult result, 
    										  String addressBook, 
											  int documentNumber, 
											  String documentType,
											  String tipoComprobante,
								    		  HttpServletResponse response){
 
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();

    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String usr = auth.getName();
		
        try{

        if (result.hasErrors()){
            for(ObjectError error : result.getAllErrors()){
                System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
            }
 
            json.put("success", false);
            json.put("message", "Error al cargar el archivo");
        }
        
        InvoiceDTO inv = null;
        String ct = uploadItem.getFile().getContentType();
        
        if(uploadItem.getFileTwo() != null) {
            String ctPdf = uploadItem.getFileTwo().getContentType();
            if(!"application/pdf".equals(ctPdf)) {
            	json.put("success", false);
            	json.put("message", "El documento cargado de tipo .pdf no es válido");
            	return json.toString();
            }
        }

        
        if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)){
            if("text/xml".equals(ct.trim())){
            	ResponseGeneral respu=documentsService.validateInvoiceVsSat(uploadItem);
	    		 if(respu.isError()) {
	            	 System.err.println(new Gson().toJson(respu));
	            	 json.put("success", false);
		            	json.put("message", "Intente ingresar la factura mañana.");
		            	return json.toString();
		            	
	             }else {
	            	 if(respu.getDocument()!=null) {
	            		 CommonsMultipartFile bade=new CommonsMultipartFile(new BASE64DecodedMultipartFile(uploadItem.getFile().getFileItem(), respu.getDocument()));
	            		 uploadItem.setFile(bade);
	            	 }
	             }
                inv = documentsService.getInvoiceXml(uploadItem);
                if(!inv.getResponse().isError()){
                	
                	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante) && !"I".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no es de tipo FACTURA<br />(Tipo Comprobante = I" + ")");
                    	return json.toString();
                	}
                	
                	if(AppConstants.NC_FIELD.equals(tipoComprobante) && !"E".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no coresponde a una NOTA DE CREDITO<br />(Tipo Comprobante = E" + ")");
                    	return json.toString();
                	}
                	
                	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante) && !"P".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no corresponde a un COMPLEMENTO DE PAGO <br />(Tipo Comprobante = P" + ")");
                    	return json.toString();
                	}
                	
                    ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getFile().getBytes());
                    String xmlContent = IOUtils.toString(stream, "UTF-8");
                    String source = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
        			String xmlString = source.replace("?<?xml", "<?xml");
                    
        			PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(documentNumber, addressBook, documentType);
                	String res = documentsService.validateInvoiceFromOrder(inv,
                														   addressBook, 
																		   documentNumber, 
																		   documentType,
																		   tipoComprobante,
																		   po,
																		   true,
																		   xmlString,
																		   "",
																		   false,"",usr);
                	
                	
                	if("".equals(res) || res.contains("DOC:")){
                    	UserDocument doc = new UserDocument(); 
                    	doc.setAddressBook(po.getAddressNumber());
                    	doc.setDocumentNumber(documentNumber);
                    	doc.setDocumentType(documentType);
                    	doc.setContent(uploadItem.getFile().getBytes());
                    	doc.setType(ct.trim());
                    	//doc.setName(uploadItem.getFile().getOriginalFilename());
                    	doc.setName("FAC_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + ".xml");
                    	doc.setSize(uploadItem.getFile().getSize());
                    	doc.setStatus(true);
                    	doc.setAccept(true);
                    	doc.setFiscalType(tipoComprobante);
                    	doc.setType("text/xml");
                    	doc.setFolio(inv.getFolio());
                    	doc.setSerie(inv.getSerie());
                    	doc.setUuid(inv.getUuid());
                    	doc.setUploadDate(new Date());
                    	doc.setFiscalRef(0);
                    	documentsService.save(doc, new Date(), "");
                    	
                    	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)){
                    		
                    		doc = new UserDocument(); 
                        	doc.setAddressBook(po.getAddressNumber());
                        	doc.setDocumentNumber(documentNumber);
                        	doc.setDocumentType(documentType);
                        	doc.setContent(uploadItem.getFileTwo().getBytes());
                        	doc.setType(uploadItem.getFileTwo().getContentType().trim());
                        	//doc.setName(uploadItem.getFileTwo().getOriginalFilename());
                        	doc.setName("FAC_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + ".pdf");
                        	doc.setSize(uploadItem.getFileTwo().getSize());
                        	doc.setStatus(true);
                        	doc.setAccept(true);
                        	doc.setFiscalType(tipoComprobante);
                        	doc.setType("application/pdf");
                        	doc.setFolio(inv.getFolio());
                        	doc.setSerie(inv.getSerie());
                        	doc.setUuid(inv.getUuid());
                        	doc.setUploadDate(new Date());
                        	doc.setFiscalRef(0);
                        	documentsService.save(doc, new Date(), "");
                    		
                    		po.setInvoiceUuid(inv.getUuid());
                    		po.setInvoiceNumber(inv.getFolio() + "");
                    		po.setPaymentType(inv.getMetodoPago());
                    		po.setStatus(AppConstants.STATUS_OC_INVOICED);
                    		po.setOrderStauts(AppConstants.STATUS_OC_INVOICED);
                    	}
                    	
                    	
                    	if(AppConstants.NC_FIELD.equals(tipoComprobante)){
                    		po.setPaymentUuid(inv.getUuid());
                    		po.setStatus(AppConstants.STATUS_LOADNC);
                    	}
                    	
                    	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante)){
                    		po.setCreditNotUuid(inv.getUuid());
                    		po.setStatus(AppConstants.STATUS_LOADCP);
                    	}
                    	
                    	
                    	purchaseOrderService.updateOrders(po);
                    	
        	            json.put("success", true);
        	            json.put("message", inv.getResponse().getMensaje().get("es"));
        	            json.put("orderNumber", documentNumber);
        	            json.put("orderType", documentType);
        	            json.put("addressNumber", addressBook);
        	            json.put("docNbr", res);
        	            json.put("uuid", inv.getUuid());
                	}else{
                    	json.put("success", false);
                    	json.put("message", res);
                	}

                }else{
                	json.put("success", false);
                	json.put("message", "El archivo no es aceptado.  <br />NO ha pasado la fase de verificación y tampoco sera cargado a la solicitud.");
                }
                
            }else{
            	json.put("success", false);
            	json.put("message", "Para cargas de archivos fiscales, sólo se permiten archivos .xml");
            }
        }else{
        	
            if(uploadItem.getFile() != null) {
                String ctPdf = uploadItem.getFile().getContentType();
                if(!"application/pdf".equals(ctPdf)) {
                	json.put("success", false);
                	json.put("message", "Sólo se permiten archivos tipo PDF");
                	return json.toString();
                }
            }
        	
        	PurchaseOrder po = purchaseOrderService.searchbyOrder(documentNumber, documentType);
        	UserDocument doc = new UserDocument(); 
        	
        	if(po != null) {
        		
        		String fileName = uploadItem.getFile().getOriginalFilename();
        		fileName = fileName.replaceAll(" ", "_");
        		
            	doc.setAddressBook(po.getAddressNumber());
            	doc.setDocumentNumber(documentNumber);
            	doc.setDocumentType(documentType);
            	doc.setContent(uploadItem.getFile().getBytes());
            	doc.setType(ct.trim());
            	doc.setName(fileName);
            	doc.setSize(uploadItem.getFile().getSize());
            	doc.setStatus(true);
            	doc.setAccept(true);
            	doc.setFiscalType(tipoComprobante);
            	doc.setUuid("");
            	doc.setUploadDate(new Date());
            	doc.setFiscalRef(0);
            	documentsService.save(doc, new Date(), "");
            	
                json.put("success", true);
                json.put("message", "El archivo ha sido cargado de forma exitosa");
                json.put("orderNumber", documentNumber);
                json.put("orderType", documentType);
                json.put("addressNumber", addressBook);
        	}else {
                json.put("success", false);
                json.put("message", "El archivo no pertenece al proveedor de la sesión");
        	}

            
        }
        return json.toString();
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", true);
            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
        }
        return json.toString();

    }
    
	@SuppressWarnings("unused")
	@RequestMapping(value = "/uploadComplPago.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody public String uploadInvoiceFromOrder(FileUploadBean uploadItem, String[] orders,  String[] invoices, String addressBook, 
    										           BindingResult result){
 
    	JSONObject json = new JSONObject();
    	
    	String tipoComprobante = AppConstants.PAYMENT_FIELD;
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
 		String userAuth = auth.getName();
 		Date currentDate = new Date();

        try{

        if (result.hasErrors()){
            for(ObjectError error : result.getAllErrors()){
                System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
            }
 
            json.put("success", false);
            json.put("message", "Error al cargar el archivo");
        }
        
        Arrays.asList(invoices).replaceAll(String::toUpperCase);
        
        if(uploadItem.getFileTwo() != null) {
            String ctPdf = uploadItem.getFileTwo().getContentType();
            if(!"application/pdf".equals(ctPdf)) {
            	json.put("success", false);
            	json.put("message", "Error_2");
            	return json.toString();
            }
        }
        
        InvoiceDTO inv = null;
        String ct = uploadItem.getFile().getContentType();
        ResponseGeneral respu= documentsService.validateInvoiceVsSat(uploadItem);
         if(respu.isError()) {
        	 System.err.println(new Gson().toJson(respu));
        	 json.put("success", false);
         	json.put("message", respu.getMensaje().get("es"));
         	return json.toString();
         }else {
        	 if(respu.getDocument()!=null) {
        		 CommonsMultipartFile bade=new CommonsMultipartFile(new BASE64DecodedMultipartFile(uploadItem.getFile().getFileItem(), respu.getDocument()));
        		 uploadItem.setFile(bade);
        	 }
         }
        
        if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)){
            if("text/xml".equals(ct.trim())){
                inv = documentsService.getInvoiceXml(uploadItem);
                if(!inv.getResponse().isError()){
                	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante) && !"P".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no corresponde a un COMPLEMENTO DE PAGO <br />(Tipo Comprobante = P" + ")");
                    	return json.toString();
                	}
                	
                    ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getFile().getBytes());
                    String xmlContent = IOUtils.toString(stream, "UTF-8");
                    String source = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
        			String xmlString = source.replace("?<?xml", "<?xml");
                    
        			
        			
                    String res = "";
                    UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");
        			if(udcCfdi != null) {
        				if(!"".equals(udcCfdi.getStrValue1())) {
        					if("TRUE".equals(udcCfdi.getStrValue1())) {
        	            		res = documentsService.validaComprobanteSAT(inv);
        	            		if(!"".equals(res)) {
        	            			json.put("success", false);
        	                    	json.put("message", res);
        	                    	return json.toString();
        	            		}
        					}
        				}
        			}else {
                		res = documentsService.validaComprobanteSAT(inv);
                		if(!"".equals(res)) {
	            			json.put("success", false);
	                    	json.put("message", res);
	                    	return json.toString();
                		}
        			}
        			
        			String fechaFactura = inv.getFechaTimbrado();
        			fechaFactura = fechaFactura.replace("T", " ");
        			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        			Date invDate = null;
        			try {
        				invDate = sdf.parse(fechaFactura);
        			}catch(Exception e) {
        				log4j.error("Exception" , e);
        				e.printStackTrace();
        			}
        			
        			//Validación CFDI Versión 3.3
        			/*
        			if(AppConstants.CFDI_V3.equals(inv.getVersion())) {
        				UDC udcVersion = udcService.searchBySystemAndKey("VERSIONCFDI", "VERSION33");
        				if(udcVersion != null) {
        					try {
        						boolean isVersionValidationOn = udcVersion.isBooleanValue();
        						if(isVersionValidationOn) {
        							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        							String strLastDateAllowed = udcVersion.getStrValue1();
        							Date dateLastDateAllowed = formatter.parse(strLastDateAllowed);
        							if(invDate.compareTo(dateLastDateAllowed) > 0) {
    	    	            			json.put("success", false);
    	    	                    	json.put("message", "La versión del CFDI no es válida.");
    	    	                    	return json.toString();
        							}
        						}
        					} catch (Exception e) {
        						log4j.error("Exception" , e);
        						e.printStackTrace();
    	            			json.put("success", false);
    	                    	json.put("message", "Error al obtener la fecha de timbrado del comprobante");
    	                    	return json.toString();
        					}
        				}
        			}
                    */
        			
        			// START J.AVILA: Se desactiva la validación estricta de UUIDS de Facturas contenidas en el complemento
        			/*
                    List<String> invListCompl = new ArrayList<String>();
        			List<Pago> pagosXml = inv.getComplemento().getPago().getPago();
        			for(Pago p : pagosXml) {
            			List<DoctoRelacionado> drList = p.getDoctoRelacionado();
            			for(DoctoRelacionado dr : drList) {
            				invListCompl.add(dr.getIdDocumento());
            			}
        			}
        			
        			if(invListCompl.size() != invoices.length) {
        				json.put("success", false);
                    	json.put("message", "La cantidad de facturas seleccionadas es diferente a la cantidad de facturas contenidas en el Complemento de Pago");
                    	return json.toString();
        			}
        			
        			List<String> invListSelectedOriginal = Arrays.asList(invoices); 
        			List<String> invListSelected = new ArrayList<String>(); 
        			for(String q : invListSelectedOriginal) {
        				invListSelected.add(q.toUpperCase());
        			}
        			
        			for(String uuidCompl : invListCompl) {
        				 if(!invListSelected.contains(uuidCompl)) {
        					 json.put("success", false);
                         	json.put("message", "Existen facturas en su selección que no se encuentran dentro del Complemento de Pago");
                         	return json.toString(); 
        				 }
        			 }
        			 
        			 for(String uuidSel : invListSelected) {
        				 if(!invListCompl.contains(uuidSel)) {
        					 json.put("success", false);
                         	json.put("message", "Existen facturas en el Complemento de Pago que no se encuentran dentro de su selección de facturas");
                         	return json.toString(); 
        				 }
        			 }
        			 END JAVILA
        			 */
        			
            		Supplier s = supplierService.searchByAddressNumber(addressBook);
            		
            		String rfcEmisor = inv.getRfcEmisor();
            		if(rfcEmisor != null) {
            			if(!"".equals(rfcEmisor)) {
            				if(!s.getRfc().equals(rfcEmisor)) {
    	            			json.put("success", false);
    	                    	json.put("message", "El complemento de pago no pertenece al emisor " + s.getRfc());
    	                    	return json.toString();
            				}
            			}
            		}
            		
            		boolean receptorValido = false;
            		List<UDC> receptores = udcService.searchBySystem("RECEPTOR");
            		if(receptores != null) {
            			for(UDC udc : receptores) {
            				if(udc.getStrValue1().equals(inv.getRfcReceptor().trim())) {
            					receptorValido = true;
            					break;
            				}
            			}
            		}
            		
            		if(!receptorValido) {
            			json.put("success", false);
                    	json.put("message", "El receptor " + inv.getRfcReceptor() + " no es permitido para carga de complementos");
                    	return json.toString();
            		}
                	

            		List<String> uuidList = new ArrayList<String>();
            		List<Pago> pagos = inv.getComplemento().getPago().getPago();
            		for(Pago p : pagos) {
            			List<DoctoRelacionado> dList = p.getDoctoRelacionado();
        				String uuid = "";
        				if(dList != null) {
                			for(DoctoRelacionado d : dList) {
                				uuid = d.getIdDocumento().trim();
                				if(!Arrays.asList(invoices).contains(uuid.toUpperCase())) {
                					continue;
                				}
                        		List<Receipt> receiptList = purchaseOrderService.getReceiptsByUUID(uuid);
                				if(receiptList == null) {
                        			json.put("success", false);
                                	json.put("message", "Error: El uuid " + uuid + " contenido en el complemento, no tiene una factura relacionada. Verifique que su complemento de pago contenga las facturas que previamenta ha enviado a CRYOINFRA");
                                	return json.toString();
                				}else {
                					uuidList.add(uuid);
                					for(Receipt r : receiptList) {
                						String oCurr = "";
                						if("MXP".equals(r.getCurrencyCode())) {
                							oCurr = "MXN";
                						}else {
                							oCurr = r.getCurrencyCode();
                						}
                						
                						if(!d.getMonedaDR().equals(oCurr)) {
                    						json.put("success", false);
                	                    	json.put("message", "Error: La clave de moneda para el " + uuid + " son diferentes en el complemento y la factura.");
                	                    	return json.toString();
                    					}
                  					}
                				}
                			}	
        				}
            		}

            		int orderNumber = 0;
            		String orderType = "";
            		String addressNumber = "";
            		String documentNumber ="";
            		boolean saveDocument = false;
            		if("".equals(res)){
            			
            			for(String uid : uuidList) {
            				List<Receipt> rList = purchaseOrderService.getReceiptsByUUID(uid);
            				Date a=new Date();
            				for(Receipt r_ : rList) {
            					r_.setComplPagoUuid(inv.getUuid());
                    			r_.setStatus(AppConstants.STATUS_OC_PAYMENT_COMPL);
                    			r_.setUploadComplDate(a);
                    			r_ = purchaseOrderService.unblockSupCompl(r_);
                    			
                                purchaseOrderService.updateReceipt(r_);
                                orderNumber = r_.getOrderNumber();
                                orderType = r_.getOrderType();
                                addressNumber = r_.getAddressNumber();
                                PurchaseOrder po_ = purchaseOrderService.searchbyOrderAndAddressBook(orderNumber, addressNumber, orderType);
                                po_.setOrderStauts(AppConstants.STATUS_OC_PAYMENT_COMPL);
                                purchaseOrderService.updateOrders(po_);
                                saveDocument = true;
                                documentNumber = documentNumber + r_.getDocumentNumber() +",";
            				}
            			}
                		
            			if(saveDocument) {
        				UserDocument doc = new UserDocument(); 
                    	doc.setAddressBook(addressNumber);
                    	doc.setDocumentNumber(orderNumber);
                    	doc.setDocumentType(orderType);
                    	doc.setContent(uploadItem.getFile().getBytes());
                    	doc.setType(ct.trim());
                    	//doc.setName(uploadItem.getFile().getOriginalFilename());
                    	doc.setName("COMPL_" + inv.getUuid() + ".xml");
                    	doc.setSize(uploadItem.getFile().getSize());
                    	doc.setStatus(true);
                    	doc.setAccept(true);
                    	doc.setFiscalType(tipoComprobante);
                    	doc.setType("text/xml");
                    	//doc.setType(tipoComprobante);
                    	doc.setFolio(inv.getFolio());
                    	doc.setSerie(inv.getSerie());
                    	doc.setUuid(inv.getUuid());
                    	doc.setUploadDate(new Date());
                    	doc.setFiscalRef(0);
                    	documentsService.save(doc, new Date(), "");
                    	
                    	dataAuditService.saveDataAudit("UploadComplInvoice",addressNumber, currentDate, request.getRemoteAddr(),
            	    	userAuth, "COMPL_" + inv.getUuid() + ".xml", "uploadInvoiceFromOrder", 
            	    	"Uploaded Compl Payment Successful",documentNumber+"", orderNumber+"", null, 
            	    	inv.getUuid(), AppConstants.STATUS_COMPLETE, AppConstants.SALESORDER_MODULE);
                    	
                		doc = new UserDocument(); 
                    	doc.setAddressBook(addressNumber);
                    	doc.setDocumentNumber(orderNumber);
                    	doc.setDocumentType(orderType);
                    	doc.setContent(uploadItem.getFileTwo().getBytes());
                    	doc.setType(uploadItem.getFileTwo().getContentType().trim());
                    	doc.setName("COMPL_" + inv.getUuid() + ".pdf");
                    	doc.setSize(uploadItem.getFileTwo().getSize());
                    	doc.setStatus(true);
                    	doc.setAccept(true);
                    	doc.setFiscalType(tipoComprobante);
                    	doc.setType("application/pdf");
                    	doc.setFolio(inv.getFolio());
                    	doc.setSerie(inv.getSerie());
                    	doc.setUuid(inv.getUuid());
                    	doc.setUploadDate(new Date());
                    	doc.setFiscalRef(0);
                    	documentsService.save(doc, new Date(), "");
            			}else {
            				json.put("success", false);
                        	json.put("message", "El archivo no será cargado debido que la relación de UUID's del compelmento con los recibos seleccionados no es válida.");
                        	return json.toString();
            			}
                	} 

        	            json.put("success", true);
        	            json.put("message", inv.getResponse().getMensaje().get("es"));
        	            json.put("docNbr", res);
        	            json.put("uuid", inv.getUuid());
                    	
                	}else{
                    	json.put("success", false);
                    	json.put("message", "ERROR");
                	}

                }else{
                	json.put("success", false);
                	json.put("message", "El archivo no es aceptado.  <br />NO ha pasado la fase de verificacion y tampoco sera cargado a la solicitud.");
                }
                
            }else{
            	json.put("success", false);
            	json.put("message", "Para cargas de archivos fiscales, sólo se permiten de tipo .xml");
            }
        return json.toString();
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", false);
            json.put("message", "Ha ocurrido un error inesperado: " + e.getMessage());
        }
        return json.toString();

    }

    
	@SuppressWarnings("unused")
	@RequestMapping(value = "/uploadComplPagoMassive.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody public String uploadComplPagoMassive(FileUploadBean uploadItem, String[] orders,  String[] invoices, String addressBook, 
    										           BindingResult result){
 
    	
    	JsonArray jsonarray=new JsonArray();
    	
    	String tipoComprobante = AppConstants.PAYMENT_FIELD;
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
 		String userAuth = auth.getName();
 		Date currentDate = new Date();

        try{

        if (result.hasErrors()){
            for(ObjectError error : result.getAllErrors()){
                System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
            }
            JsonObject json = new JsonObject();
            json.addProperty("success", false);
            json.addProperty("message", "Error al cargar el archivo");
            jsonarray.add(json);
            return jsonarray.toString();
            
        }
        
        
        
        try (ByteArrayInputStream bis = new ByteArrayInputStream(uploadItem.getFile().getBytes());
                ZipInputStream zis = new ZipInputStream(bis)) {

               ZipEntry entry;
               while ((entry = zis.getNextEntry()) != null) {
            	   if (!entry.isDirectory() && !entry.getName().toLowerCase().endsWith(".xml")) {
            		   JsonObject json = new JsonObject();
                       json.addProperty("success", false);
                       json.addProperty("Archivo", entry.getName());
                       json.addProperty("message", "Archivo "+entry.getName().split("\\.")[1].toUpperCase());
                       jsonarray.add(json);
                       continue;
            	   }
            	   else if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".xml")) {
                       byte[] invoiceXMLBytes = documentsService.extraerBytesXml(zis);
                       System.out.println("XML extraído: " + new String(invoiceXMLBytes)); // Ejemplo: Imprimir el XML
                      byte[] pdfDocBytes= documentsService.extraerArchivoDelZip(uploadItem.getFile().getBytes(), entry.getName().replace(".xml", ".pdf"));
//                       if(pdfDocBytes==null) {
//                    	   JsonObject json = new JsonObject();
//                           json.addProperty("success", false);
//                           json.addProperty("Archivo", entry.getName());
//                           json.addProperty("message", "no se contiene el documento PDF a relacionar");
//                           jsonarray.add(json);
//                           continue;
//                       }
                       InvoiceDTO inv = null;
                       String ct = "text/xml";
                       
                       ResponseGeneral respu= documentsService.validateInvoiceVsSat(invoiceXMLBytes);
                        if(respu.isError()) {
                        	
                        	JsonObject json = new JsonObject();
                            json.addProperty("success", false);
                            json.addProperty("Archivo", entry.getName());
                            json.addProperty("message", respu.getMensaje().get("es"));
                            jsonarray.add(json);
                            continue;
                        }else {
                       	 if(respu.getDocument()!=null) {
                       		invoiceXMLBytes=respu.getDocument();
                       	 }
                        }
                       
                       if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)){
                           if("text/xml".equals(ct.trim())){
                               inv = documentsService.getInvoiceXml(invoiceXMLBytes);
                               if(!inv.getResponse().isError()){
                               	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante) && !"P".equals(inv.getTipoComprobante())){
                               		JsonObject json = new JsonObject();
                                    json.addProperty("success", false);
                                    json.addProperty("Archivo", entry.getName());
                                    json.addProperty("message", "El documento cargado no corresponde a un COMPLEMENTO DE PAGO <br />(Tipo Comprobante = P" + ")");
                                    jsonarray.add(json);
                                    continue;
                               		
                               	}
                               	
                                   ByteArrayInputStream stream = new  ByteArrayInputStream(invoiceXMLBytes);
                                   String xmlContent = IOUtils.toString(stream, "UTF-8");
                                   String source = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
                       			String xmlString = source.replace("?<?xml", "<?xml");
                                   
                       			
                       			
                                   String res = "";
                                   UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");
                       			if(udcCfdi != null) {
                       				if(!"".equals(udcCfdi.getStrValue1())) {
                       					if("TRUE".equals(udcCfdi.getStrValue1())) {
                       	            		res = documentsService.validaComprobanteSAT(inv);
                       	            		if(!"".equals(res)) {
                       	            			JsonObject json = new JsonObject();
                                                json.addProperty("success", false);
                                                json.addProperty("Archivo", entry.getName());
                                                json.addProperty("message", res);
                                                jsonarray.add(json);
                                                continue;
                       	            		}
                       					}
                       				}
                       			}else {
                               		res = documentsService.validaComprobanteSAT(inv);
                               		if(!"".equals(res)) {
                               			JsonObject json = new JsonObject();
                                        json.addProperty("success", false);
                                        json.addProperty("Archivo", entry.getName());
                                        json.addProperty("message", res);
                                        jsonarray.add(json);
                                        continue;
                               		}
                       			}
                       			
                       			String fechaFactura = inv.getFechaTimbrado();
                       			fechaFactura = fechaFactura.replace("T", " ");
                       			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                       			Date invDate = null;
                       			try {
                       				invDate = sdf.parse(fechaFactura);
                       			}catch(Exception e) {
                       				log4j.error("Exception" , e);
                       				e.printStackTrace();
                       				JsonObject json = new JsonObject();
                                    json.addProperty("success", false);
                                    json.addProperty("Archivo", entry.getName());
                                    json.addProperty("message", "Error en fecha timbrado: "+ e);
                                    jsonarray.add(json);
                                    continue;
                       			}
                       			
                       			
                       			
                           		Supplier s = supplierService.searchByAddressNumber(addressBook);
                           		
                           		String rfcEmisor = inv.getRfcEmisor();
                           		if(rfcEmisor != null) {
                           			if(!"".equals(rfcEmisor)) {
                           				if(!s.getRfc().equals(rfcEmisor)) {
                           					JsonObject json = new JsonObject();
                                            json.addProperty("success", false);
                                            json.addProperty("Archivo", entry.getName());
                                            json.addProperty("message", "El complemento de pago no pertenece al emisor " + s.getRfc());
                                            jsonarray.add(json);
                                            continue;
                           				}
                           			}
                           		}
                           		
                           		boolean receptorValido = false;
                           		List<UDC> receptores = udcService.searchBySystem("RECEPTOR");
                           		if(receptores != null) {
                           			for(UDC udc : receptores) {
                           				if(udc.getStrValue1().equals(inv.getRfcReceptor().trim())) {
                           					receptorValido = true;
                           					break;
                           				}
                           			}
                           		}
                           		
                           		if(!receptorValido) {
                           			JsonObject json = new JsonObject();
                                    json.addProperty("success", false);
                                    json.addProperty("Archivo", entry.getName());
                                    json.addProperty("message", "El receptor " + inv.getRfcReceptor() + " no es permitido para carga de complementos");
                                    jsonarray.add(json);
                                    continue;
                                    
                           		}
                               	

                           		List<String> uuidList = new ArrayList<String>();
                           		List<Pago> pagos = inv.getComplemento().getPago().getPago();
                           		for(Pago p : pagos) {
                           			List<DoctoRelacionado> dList = p.getDoctoRelacionado();
                       				String uuid = "";
                       				if(dList != null) {
                               			for(DoctoRelacionado d : dList) {
                               			  uuid = d.getIdDocumento().trim();
                                         final String finalUuid = uuid; 
//                               				aqui verificar los uud con la lista
                               				
                                         if (!Arrays.stream(invoices)
                                                 .map(String::toUpperCase)
                                                 .anyMatch(invoiceUuid -> invoiceUuid.equals(finalUuid.toUpperCase()))) {
                                          
                               					
                               					JsonObject json = new JsonObject();
                                                json.addProperty("success", false);
                                                json.addProperty("Archivo", entry.getName());
                                                json.addProperty("message", "El uuid " + uuid + " contenido en el complemento,no estaba seleccionado.");
                                                jsonarray.add(json);
                                                continue;
											}
                               				
                                       		List<Receipt> receiptList = purchaseOrderService.getReceiptsByUUID(uuid);
                               				if(receiptList == null) {
                               					JsonObject json = new JsonObject();
                                                json.addProperty("success", false);
                                                json.addProperty("Archivo", entry.getName());
                                                json.addProperty("message", "Error: El uuid " + uuid + " contenido en el complemento, no tiene una factura relacionada. Verifique que su complemento de pago contenga las facturas que previamenta ha enviado a CRYOINFRA");
                                                jsonarray.add(json);
                                                continue;
                               				}else {
                               					uuidList.add(uuid);
                               					for(Receipt r : receiptList) {
                               						String oCurr = "";
                               						if("MXP".equals(r.getCurrencyCode())) {
                               							oCurr = "MXN";
                               						}else {
                               							oCurr = r.getCurrencyCode();
                               						}
                               						
                               						if(!d.getMonedaDR().equals(oCurr)) {
                               							JsonObject json = new JsonObject();
                                                        json.addProperty("success", false);
                                                        json.addProperty("Archivo", entry.getName());
                                                        json.addProperty("message", "Error: La clave de moneda para el " + uuid + " son diferentes en el complemento y la factura.");
                                                        jsonarray.add(json);
                                                        continue;
                                   					}
                                 					}
                               				}
                               			}	
                       				}
                           		}

                           		int orderNumber = 0;
                           		String orderType = "";
                           		String addressNumber = "";
                           		String documentNumber ="";
                           		boolean saveDocument = false;
                           		if("".equals(res)){
                           			
                           			for(String uid : uuidList) {
                           				List<Receipt> rList = purchaseOrderService.getReceiptsByUUID(uid);
                           				Date a=new Date();
                           				for(Receipt r_ : rList) {
                           					r_.setComplPagoUuid(inv.getUuid());
                                   			r_.setStatus(AppConstants.STATUS_OC_PAYMENT_COMPL);
                                   			r_.setUploadComplDate(a);
                                   			r_ = purchaseOrderService.unblockSupCompl(r_);
                                   			
                                               purchaseOrderService.updateReceipt(r_);
                                               orderNumber = r_.getOrderNumber();
                                               orderType = r_.getOrderType();
                                               addressNumber = r_.getAddressNumber();
                                               PurchaseOrder po_ = purchaseOrderService.searchbyOrderAndAddressBook(orderNumber, addressNumber, orderType);
                                               po_.setOrderStauts(AppConstants.STATUS_OC_PAYMENT_COMPL);
                                               purchaseOrderService.updateOrders(po_);
                                               saveDocument = true;
                                               documentNumber = documentNumber + r_.getDocumentNumber() +",";
                           				}
                           			}
                               		
                           			if(saveDocument) {
                       				UserDocument doc = new UserDocument(); 
                                   	doc.setAddressBook(addressNumber);
                                   	doc.setDocumentNumber(orderNumber);
                                   	doc.setDocumentType(orderType);
                                   	doc.setContent(uploadItem.getFile().getBytes());
                                   	doc.setType(ct.trim());
                                   	//doc.setName(uploadItem.getFile().getOriginalFilename());
                                   	doc.setName("COMPL_" + inv.getUuid() + ".xml");
                                   	doc.setSize(uploadItem.getFile().getSize());
                                   	doc.setStatus(true);
                                   	doc.setAccept(true);
                                   	doc.setFiscalType(tipoComprobante);
                                   	doc.setType("text/xml");
                                   	//doc.setType(tipoComprobante);
                                   	doc.setFolio(inv.getFolio());
                                   	doc.setSerie(inv.getSerie());
                                   	doc.setUuid(inv.getUuid());
                                   	doc.setUploadDate(new Date());
                                   	doc.setFiscalRef(0);
                                   	documentsService.save(doc, new Date(), "");
                                   	
                                   	dataAuditService.saveDataAudit("UploadComplInvoice",addressNumber, currentDate, request.getRemoteAddr(),
                           	    	userAuth, "COMPL_" + inv.getUuid() + ".xml", "uploadInvoiceFromOrder", 
                           	    	"Uploaded Compl Payment Successful",documentNumber+"", orderNumber+"", null, 
                           	    	inv.getUuid(), AppConstants.STATUS_COMPLETE, AppConstants.SALESORDER_MODULE);
                                   	
//                               		doc = new UserDocument(); 
//                                   	doc.setAddressBook(addressNumber);
//                                   	doc.setDocumentNumber(orderNumber);
//                                   	doc.setDocumentType(orderType);
//                                   	doc.setContent(pdfDocBytes);
//                                   	doc.setName("COMPL_" + inv.getUuid() + ".pdf");
//                                   	doc.setSize(pdfDocBytes.length);
//                                   	doc.setStatus(true);
//                                   	doc.setAccept(true);
//                                   	doc.setFiscalType(tipoComprobante);
//                                   	doc.setType("application/pdf");
//                                   	doc.setFolio(inv.getFolio());
//                                   	doc.setSerie(inv.getSerie());
//                                   	doc.setUuid(inv.getUuid());
//                                   	doc.setUploadDate(new Date());
//                                   	doc.setFiscalRef(0);
//                                   	documentsService.save(doc, new Date(), "");
                           			}else {
                                       	JsonObject json = new JsonObject();
                                        json.addProperty("success", false);
                                        json.addProperty("Archivo", entry.getName());
                                        json.addProperty("message", "El archivo no será cargado debido que la relación de UUID's del compelmento con los recibos seleccionados no es válida.");
                                        jsonarray.add(json);
                                        continue;
                           			}
                               	} 
                           		JsonObject json = new JsonObject();
                                json.addProperty("success", false);
                                json.addProperty("Archivo", entry.getName()+"  "+inv.getUuid());
                                json.addProperty("message", inv.getResponse().getMensaje().get("es"));
                                jsonarray.add(json);
                                continue;
                       	            
                                   	
                               	}else{
                               		JsonObject json = new JsonObject();
                                    json.addProperty("success", false);
                                    json.addProperty("Archivo", entry.getName()+"  "+inv.getUuid());
                                    json.addProperty("message", inv.getResponse().getMensaje().get("es"));
                                    jsonarray.add(json);
                                    continue;
                           	            
                               	}

                               }else{
                          
                               	JsonObject json = new JsonObject();
                                json.addProperty("success", false);
                                json.addProperty("Archivo", entry.getName()+"  "+inv.getUuid());
                                json.addProperty("message", "El archivo no es aceptado.  <br />NO ha pasado la fase de verificacion y tampoco sera cargado a la solicitud.");
                                jsonarray.add(json);
                                continue;
                               }
                               
                           }else{
                      
                           	JsonObject json = new JsonObject();
                            json.addProperty("success", false);
                            json.addProperty("Archivo", entry.getName()+"  "+inv.getUuid());
                            json.addProperty("message", "Para cargas de archivos fiscales, sólo se permiten de tipo .xml");
                            jsonarray.add(json);
                            continue;
                           	
                           }
                       
                   }
                   zis.closeEntry();
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
        
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	JsonObject json = new JsonObject();
        	json.addProperty("success", false);
            json.addProperty("message", "Ha ocurrido un error inesperado: " + e.getMessage());
            return json.toString();
        }
        JsonObject json = new JsonObject();
    	json.addProperty("success", true);
        json.addProperty("message", "Procesado correctamente");
        json.add("detail", jsonarray);
        return json.toString();

    }

	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/uploadComplPagoFD.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody public String uploadComplPagoFD(FileUploadBean uploadItem, String[] documents, String addressBook, 
    										           BindingResult result){
 
    	JSONObject json = new JSONObject();    	
    	String tipoComprobante = AppConstants.PAYMENT_FIELD;

        try{

        if (result.hasErrors()){
            for(ObjectError error : result.getAllErrors()){
                System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
            }
 
            json.put("success", false);
            json.put("message", "Error al cargar el archivo");
        }
        
        InvoiceDTO inv = null;
        String ct = uploadItem.getFile().getContentType();
        if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)){
            if("text/xml".equals(ct.trim())){
            	ResponseGeneral respu=documentsService.validateInvoiceVsSat(uploadItem);
	    		 if(respu.isError()) {
	            	 System.err.println(new Gson().toJson(respu));
	            	 json.put("success", false);
		            	json.put("message", "Intente ingresar la factura mañana.");
		            	return json.toString();
		            	
	             }else {
	            	 if(respu.getDocument()!=null) {
	            		 CommonsMultipartFile bade=new CommonsMultipartFile(new BASE64DecodedMultipartFile(uploadItem.getFile().getFileItem(), respu.getDocument()));
	            		 uploadItem.setFile(bade);
	            	 }
	             }
                inv = documentsService.getInvoiceXml(uploadItem);
                if(!inv.getResponse().isError()){
                	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante) && !"P".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no corresponde a un COMPLEMENTO DE PAGO <br />(Tipo Comprobante = P" + ")");
                    	return json.toString();
                	}
                	
                    ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getFile().getBytes());
                    String xmlContent = IOUtils.toString(stream, "UTF-8");
                    String source = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
        			String xmlString = source.replace("?<?xml", "<?xml");
                    
                    String res = "";
        			/*
                    UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");
        			if(udcCfdi != null) {
        				if(!"".equals(udcCfdi.getStrValue1())) {
        					if("TRUE".equals(udcCfdi.getStrValue1())) {
        	            		res = documentsService.validaComprobanteSATPagos(xmlString);
        	            		if(!"".equals(res)) {
        	            			json.put("success", false);
        	                    	json.put("message", "El documento cargado no es aceptable ante el SAT. Verifique su archivo e intente nuevamente.");
        	                    	return json.toString();
        	            		}
        					}
        				}
        			}else {
                		res = documentsService.validaComprobanteSATPagos(xmlString);
                		if(!"".equals(res)) {
	            			json.put("success", false);
	                    	json.put("message", "El documento cargado no es aceptable ante el SAT. Verifique su archivo e intente nuevamente.");
	                    	return json.toString();
                		}
        			}
        			*/
        			
        			String fechaFactura = inv.getFechaTimbrado();
        			fechaFactura = fechaFactura.replace("T", " ");
        			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        			Date invDate = null;
        			try {
        				invDate = sdf.parse(fechaFactura);
        			}catch(Exception e) {
        				log4j.error("Exception" , e);
        				e.printStackTrace();
        			}
        			
        			//Validación CFDI Versión 3.3
        			/*
        			if(AppConstants.CFDI_V3.equals(inv.getVersion())) {
        				UDC udcVersion = udcService.searchBySystemAndKey("VERSIONCFDI", "VERSION33");
        				if(udcVersion != null) {
        					try {
        						boolean isVersionValidationOn = udcVersion.isBooleanValue();
        						if(isVersionValidationOn) {
        							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        							String strLastDateAllowed = udcVersion.getStrValue1();
        							Date dateLastDateAllowed = formatter.parse(strLastDateAllowed);
        							if(invDate.compareTo(dateLastDateAllowed) > 0) {
    	    	            			json.put("success", false);
    	    	                    	json.put("message", "La versión del CFDI no es válida.");
    	    	                    	return json.toString();
        							}
        						}
        					} catch (Exception e) {
        						log4j.error("Exception" , e);
        						e.printStackTrace();
    	            			json.put("success", false);
    	                    	json.put("message", "Error al obtener la fecha de timbrado del comprobante");
    	                    	return json.toString();
        					}
        				}
        			}
                    */
        			
            		Supplier s = supplierService.searchByAddressNumber(addressBook);
            		
            		String rfcEmisor = inv.getRfcEmisor();
            		if(rfcEmisor != null) {
            			if(!"".equals(rfcEmisor)) {
            				if(!s.getRfc().equals(rfcEmisor)) {
    	            			json.put("success", false);
    	                    	json.put("message", "El complemento de pago no pertenece al emisor " + s.getRfc());
    	                    	return json.toString();
            				}
            			}
            		}
            		
            		boolean receptorValido = false;
            		List<UDC> receptores = udcService.searchBySystem("RECEPTOR");
            		if(receptores != null) {
            			for(UDC udc : receptores) {
            				if(udc.getStrValue1().equals(inv.getRfcReceptor().trim())) {
            					receptorValido = true;
            					break;
            				}
            			}
            		}
            		
            		if(!receptorValido) {
            			json.put("success", false);
                    	json.put("message", "El receptor " + inv.getRfcReceptor() + " no es permitido para carga de complementos");
                    	return json.toString();
            		}
                	            		
            		List<Pago> pagos = inv.getComplemento().getPago().getPago();
            		for(Pago p : pagos) {
            			List<DoctoRelacionado> dList = p.getDoctoRelacionado();
        				String uuid = "";
        				if(dList != null) {
                			for(DoctoRelacionado d : dList) {
                				uuid = d.getIdDocumento().trim();
                				List<FiscalDocuments> fdList = fiscalDocumentService.getFiscalDocuments(s.getAddresNumber(), AppConstants.STATUS_PAID, uuid, "", 0, 20);
                				if(fdList != null && !fdList.isEmpty()) {
                					for(FiscalDocuments fd : fdList) {
                						String oCurr = "";
                						if("MXP".equals(fd.getCurrencyCode())) {
                							oCurr = "MXN";
                						}else {
                							oCurr = fd.getCurrencyCode();
                						}
                						
                						if(!d.getMonedaDR().equals(oCurr)) {
                    						json.put("success", false);
                	                    	json.put("message", "Error: La clave de moneda para el uuid " + uuid + " son diferentes en el complemento y la factura.");
                	                    	return json.toString();
                    					}
                  					}
                					
                					for(FiscalDocuments fd : fdList) {
                    					for(String i : documents) {
                    						if(fd.getId() == Integer.valueOf(i).intValue()) {
                    							fd.setComplPagoUuid(inv.getUuid());
                    							fd.setStatus(AppConstants.STATUS_COMPLEMENT);
                    							fiscalDocumentService.updateDocument(fd);
                    						}
                    					}	
                					}
                					
                    				UserDocument doc = new UserDocument(); 
                                	doc.setAddressBook(s.getAddresNumber());
                                	doc.setDocumentNumber(0);
                                	doc.setDocumentType(AppConstants.PAYMENT_FIELD);
                                	doc.setContent(uploadItem.getFile().getBytes());
                                	doc.setType(ct.trim());
                                	//doc.setName(uploadItem.getFile().getOriginalFilename());
                                	doc.setDescription("MainUUID_".concat(inv.getUuid()));
                                	doc.setName(inv.getUuid() + ".xml");
                                	doc.setSize(uploadItem.getFile().getSize());
                                	doc.setStatus(true);
                                	doc.setAccept(true);
                                	doc.setFiscalType(tipoComprobante);
                                	doc.setType("text/xml");
                                	//doc.setType(tipoComprobante);
                                	doc.setFolio(inv.getFolio());
                                	doc.setSerie(inv.getSerie());
                                	doc.setUuid(inv.getUuid());
                                	doc.setUploadDate(new Date());
                                	doc.setFiscalRef(0);
                                	documentsService.save(doc, new Date(), "");
                                	
                				}else {
                        			json.put("success", false);
                                	json.put("message", "Error: El uuid " + uuid + " contenido en el complemento, no tiene una factura relacionada. Verifique que su complemento de pago contenga las facturas que previamenta ha enviado a CRYOINFRA.");
                                	return json.toString();
                				}
                			}
        				}
            		}
            		
    	            json.put("success", true);
    	            json.put("message", inv.getResponse().getMensaje().get("es"));
    	            json.put("docNbr", res);
    	            json.put("uuid", inv.getUuid());
                    	
                	}else{
                    	json.put("success", false);
                    	json.put("message", "ERROR");
                	}

                }else{
                	json.put("success", false);
                	json.put("message", "El archivo no es aceptado.  <br />NO ha pasado la fase de verificacion y tampoco sera cargado a la solicitud.");
                }
                
            }else{
            	json.put("success", false);
            	json.put("message", "Para cargas de archivos fiscales, sólo se permiten de tipo .xml");
            }
        return json.toString();
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", true);
            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
        }
        return json.toString();

    }

	@SuppressWarnings("unused")
	@RequestMapping(value = "/uploadComplPagoCB.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody public String uploadComplPagoCB(FileUploadBean uploadItem, String[] documents, String addressBook, 
    										           BindingResult result){
 
    	JSONObject json = new JSONObject();    	
    	String tipoComprobante = AppConstants.PAYMENT_FIELD;

        try{

        if (result.hasErrors()){
            for(ObjectError error : result.getAllErrors()){
                System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
            }
 
            json.put("success", false);
            json.put("message", "Error al cargar el archivo");
        }
        
        InvoiceDTO inv = null;
        String ct = uploadItem.getFile().getContentType();
        if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)){
            if("text/xml".equals(ct.trim())){
            	ResponseGeneral respu=documentsService.validateInvoiceVsSat(uploadItem);
	    		 if(respu.isError()) {
	            	 System.err.println(new Gson().toJson(respu));
	            	 json.put("success", false);
		            	json.put("message", "Intente ingresar la factura mañana.");
		            	return json.toString();
		            	
	             }else {
	            	 if(respu.getDocument()!=null) {
	            		 CommonsMultipartFile bade=new CommonsMultipartFile(new BASE64DecodedMultipartFile(uploadItem.getFile().getFileItem(), respu.getDocument()));
	            		 uploadItem.setFile(bade);
	            	 }
	             }
                inv = documentsService.getInvoiceXml(uploadItem);
                if(!inv.getResponse().isError()){
                	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante) && !"P".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no corresponde a un COMPLEMENTO DE PAGO <br />(Tipo Comprobante = P" + ")");
                    	return json.toString();
                	}
                	
                    ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getFile().getBytes());
                    String xmlContent = IOUtils.toString(stream, "UTF-8");
                    String source = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
        			String xmlString = source.replace("?<?xml", "<?xml");
                    
                    String res = "";
        			/*
                    UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");
        			if(udcCfdi != null) {
        				if(!"".equals(udcCfdi.getStrValue1())) {
        					if("TRUE".equals(udcCfdi.getStrValue1())) {
        	            		res = documentsService.validaComprobanteSATPagos(xmlString);
        	            		if(!"".equals(res)) {
        	            			json.put("success", false);
        	                    	json.put("message", "El documento cargado no es aceptable ante el SAT. Verifique su archivo e intente nuevamente.");
        	                    	return json.toString();
        	            		}
        					}
        				}
        			}else {
                		res = documentsService.validaComprobanteSATPagos(xmlString);
                		if(!"".equals(res)) {
	            			json.put("success", false);
	                    	json.put("message", "El documento cargado no es aceptable ante el SAT. Verifique su archivo e intente nuevamente.");
	                    	return json.toString();
                		}
        			}
        			*/
        			
        			String fechaFactura = inv.getFechaTimbrado();
        			fechaFactura = fechaFactura.replace("T", " ");
        			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        			Date invDate = null;
        			try {
        				invDate = sdf.parse(fechaFactura);
        			}catch(Exception e) {
        				log4j.error("Exception" , e);
        				e.printStackTrace();
        			}
        			
        			//Validación CFDI Versión 3.3
        			/*
        			if(AppConstants.CFDI_V3.equals(inv.getVersion())) {
        				UDC udcVersion = udcService.searchBySystemAndKey("VERSIONCFDI", "VERSION33");
        				if(udcVersion != null) {
        					try {
        						boolean isVersionValidationOn = udcVersion.isBooleanValue();
        						if(isVersionValidationOn) {
        							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        							String strLastDateAllowed = udcVersion.getStrValue1();
        							Date dateLastDateAllowed = formatter.parse(strLastDateAllowed);
        							if(invDate.compareTo(dateLastDateAllowed) > 0) {
    	    	            			json.put("success", false);
    	    	                    	json.put("message", "La versión del CFDI no es válida.");
    	    	                    	return json.toString();
        							}
        						}
        					} catch (Exception e) {
        						log4j.error("Exception" , e);
        						e.printStackTrace();
    	            			json.put("success", false);
    	                    	json.put("message", "Error al obtener la fecha de timbrado del comprobante");
    	                    	return json.toString();
        					}
        				}
        			}
                    */
        			
            		Supplier s = supplierService.searchByAddressNumber(addressBook);
            		
            		String rfcEmisor = inv.getRfcEmisor();
            		if(rfcEmisor != null) {
            			if(!"".equals(rfcEmisor)) {
            				if(!s.getRfc().equals(rfcEmisor)) {
    	            			json.put("success", false);
    	                    	json.put("message", "El complemento de pago no pertenece al emisor " + s.getRfc());
    	                    	return json.toString();
            				}
            			}
            		}
            		
            		boolean receptorValido = false;
            		List<UDC> receptores = udcService.searchBySystem("RECEPTOR");
            		if(receptores != null) {
            			for(UDC udc : receptores) {
            				if(udc.getStrValue1().equals(inv.getRfcReceptor().trim())) {
            					receptorValido = true;
            					break;
            				}
            			}
            		}
            		
            		if(!receptorValido) {
            			json.put("success", false);
                    	json.put("message", "El receptor " + inv.getRfcReceptor() + " no es permitido para carga de complementos");
                    	return json.toString();
            		}
                	            		
            		List<Pago> pagos = inv.getComplemento().getPago().getPago();
            		for(Pago p : pagos) {
            			List<DoctoRelacionado> dList = p.getDoctoRelacionado();
        				String uuid = "";
        				if(dList != null) {
                			for(DoctoRelacionado d : dList) {
                				uuid = d.getIdDocumento().trim();
                				List<CustomBroker> fdList = customBrokerService.getCustomBroker(s.getAddresNumber(), AppConstants.STATUS_PAID, uuid, "", 0, 20);
                				if(fdList != null && !fdList.isEmpty()) {
                					for(CustomBroker fd : fdList) {
                						String oCurr = "";
                						if("MXP".equals(fd.getCurrencyCode())) {
                							oCurr = "MXN";
                						}else {
                							oCurr = fd.getCurrencyCode();
                						}
                						
                						if(!d.getMonedaDR().equals(oCurr)) {
                    						json.put("success", false);
                	                    	json.put("message", "Error: La clave de moneda para el uuid " + uuid + " son diferentes en el complemento y la factura.");
                	                    	return json.toString();
                    					}
                  					}
                					
                					for(CustomBroker fd : fdList) {
                    					for(String i : documents) {
                    						if(fd.getId() == Integer.valueOf(i).intValue()) {
                    							fd.setComplPagoUuid(inv.getUuid());
                    							fd.setStatus(AppConstants.STATUS_COMPLEMENT);
                    							customBrokerService.updateDocument(fd);
                    						}
                    					}	
                					}
                					
                    				UserDocument doc = new UserDocument(); 
                                	doc.setAddressBook(s.getAddresNumber());
                                	doc.setDocumentNumber(0);
                                	doc.setDocumentType(AppConstants.PAYMENT_FIELD);
                                	doc.setContent(uploadItem.getFile().getBytes());
                                	doc.setType(ct.trim());
                                	//doc.setName(uploadItem.getFile().getOriginalFilename());
                                	doc.setDescription("MainUUID_".concat(inv.getUuid()));
                                	doc.setName(inv.getUuid() + ".xml");
                                	doc.setSize(uploadItem.getFile().getSize());
                                	doc.setStatus(true);
                                	doc.setAccept(true);
                                	doc.setFiscalType(tipoComprobante);
                                	doc.setType("text/xml");
                                	//doc.setType(tipoComprobante);
                                	doc.setFolio(inv.getFolio());
                                	doc.setSerie(inv.getSerie());
                                	doc.setUuid(inv.getUuid());
                                	doc.setUploadDate(new Date());
                                	doc.setFiscalRef(0);
                                	documentsService.save(doc, new Date(), "");
                                	
                				}else {
                        			json.put("success", false);
                                	json.put("message", "Error: El uuid " + uuid + " contenido en el complemento, no tiene una factura relacionada. Verifique que su complemento de pago contenga las facturas que previamenta ha enviado a CRYOINFRA.");
                                	return json.toString();
                				}
                			}
        				}
            		}
            		
    	            json.put("success", true);
    	            json.put("message", inv.getResponse().getMensaje().get("es"));
    	            json.put("docNbr", res);
    	            json.put("uuid", inv.getUuid());
                    	
                	}else{
                    	json.put("success", false);
                    	json.put("message", "ERROR");
                	}

                }else{
                	json.put("success", false);
                	json.put("message", "El archivo no es aceptado.  <br />NO ha pasado la fase de verificacion y tampoco sera cargado a la solicitud.");
                }
                
            }else{
            	json.put("success", false);
            	json.put("message", "Para cargas de archivos fiscales, sólo se permiten de tipo .xml");
            }
        return json.toString();
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", true);
            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
        }
        return json.toString();

    }
    	
    @RequestMapping(value = "/uploadInvoiceFromOrderWithoutPayment.action", method = RequestMethod.POST)
    @ResponseBody public String uploadInvoiceFromOrderWithoutPayment(FileUploadBean uploadItem, 
    										  BindingResult result, 
    										  String addressBook, 
											  int documentNumber, 
											  String documentType,
											  String tipoComprobante,
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
        
        InvoiceDTO inv = null;
        String ct = uploadItem.getFile().getContentType();
        if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)){
            if("text/xml".equals(ct.trim())){
            	ResponseGeneral respu=documentsService.validateInvoiceVsSat(uploadItem);
	    		 if(respu.isError()) {
	            	 System.err.println(new Gson().toJson(respu));
	            	 json.put("success", false);
		            	json.put("message", "Intente ingresar la factura mañana.");
		            	return json.toString();
		            	
	             }else {
	            	 if(respu.getDocument()!=null) {
	            		 CommonsMultipartFile bade=new CommonsMultipartFile(new BASE64DecodedMultipartFile(uploadItem.getFile().getFileItem(), respu.getDocument()));
	            		 uploadItem.setFile(bade);
	            	 }
	             }
                inv = documentsService.getInvoiceXml(uploadItem);
                if(!inv.getResponse().isError()){
                	
                	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante) && !"I".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no es de tipo FACTURA<br />(Tipo Comprobante = I" + ")");
                    	return json.toString();
                	}
                	
                    ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getFile().getBytes());
                    String xmlContent = IOUtils.toString(stream, "UTF-8");
                	
        			PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(documentNumber, addressBook, documentType);
                	String res = documentsService.validateInvoiceFromOrderWithoutPayment(inv,
                														   addressBook, 
																		   documentNumber, 
																		   documentType,
																		   tipoComprobante,
																		   po,
																		   xmlContent);
                	if("".equals(res)){
                    	UserDocument doc = new UserDocument(); 
                    	doc.setAddressBook(addressBook);
                    	doc.setDocumentNumber(documentNumber);
                    	doc.setDocumentType(documentType);
                    	doc.setContent(uploadItem.getFile().getBytes());
                    	doc.setType(ct.trim());
                    	doc.setName(uploadItem.getFile().getOriginalFilename());
                    	doc.setSize(uploadItem.getFile().getSize());
                    	doc.setStatus(false);
                    	doc.setAccept(false);
                    	doc.setFiscalType(tipoComprobante);
                    	doc.setType(tipoComprobante);
                    	doc.setFolio(inv.getFolio());
                    	doc.setSerie(inv.getSerie());
                    	doc.setUuid(inv.getUuid());
                    	doc.setUploadDate(new Date());
                    	doc.setFiscalRef(0);
                    	documentsService.save(doc, new Date(), "");
                    	
                    	po.setStatus(AppConstants.STATUS_LOADINV_VALIDATE);
                    	purchaseOrderService.updateOrders(po);
                    	
        	            json.put("success", true);
        	            json.put("message", inv.getResponse().getMensaje().get("es"));
        	            json.put("orderNumber", documentNumber);
        	            json.put("orderType", documentType);
        	            json.put("addressNumber", addressBook);
        	            json.put("uuid", inv.getUuid());
        	            return json.toString();
                	}else{
                    	json.put("success", false);
                    	json.put("message", res);
                    	return json.toString();
                	}

                }else{
                	json.put("success", false);
                	json.put("message", "El archivo no es aceptado.  <br />NO ha pasado la fase de verificación y tampoco sera cargado a la solicitud.");
                	return json.toString();
                }
                
            }else{
            	json.put("success", false);
            	json.put("message", "Para cargas de archivos fiscales, sólo se permiten tipos .xml");
            }
        }
        return json.toString();
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", false);
            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
        }
        
        return json.toString();
    }
    
    
    @RequestMapping(value = "/processInvoiceFromOrderWithoutPayment.action", method = RequestMethod.POST)
    @ResponseBody public String processInvoiceFromOrderWithoutPayment(
    										  String addressBook, 
											  int documentNumber, 
											  String documentType){
 
    	JSONObject json = new JSONObject();
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String usr = auth.getName();
		
        try{

        UserDocument doc = null;
        InvoiceDTO inv = null;
        List<UserDocument> list = documentsService.searchCriteriaByOrderNumber(documentNumber, documentType, addressBook,true);
        if(list != null) {
        	if(!list.isEmpty()) {
        		for(UserDocument o : list) {
        			if(!o.isAccept()) {
        				doc = o;
        				break;
        			}
        		}
        	}
        }
        
        if(doc!= null) {
        	String xmlContent = new String(doc.getContent());
        	inv = documentsService.getInvoiceXmlFromString(xmlContent);
        	if(!inv.getResponse().isError()) {
                
    			PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(documentNumber, addressBook, documentType);
            	String res = documentsService.validateInvoiceFromOrder(inv,
            														   addressBook, 
																	   documentNumber, 
																	   documentType,
																	   AppConstants.INVOICE_FIELD,
																	   po, 
																	   true,
																	   xmlContent,
																	   "",
																	   false,"",usr);
            	if("".equals(res)){
                	doc.setStatus(true);
                	doc.setAccept(true);
                	documentsService.update(doc, new Date(), "");
                	
                	po.setInvoiceUuid(inv.getUuid());
                	po.setStatus(AppConstants.STATUS_LOADINV);
                	purchaseOrderService.updateOrders(po);
                	
                	json.put("success", true);
                	json.put("message", "La factura ha sido procesada de forma exitosa");
                	return json.toString();
    	            
            	}else {
                	json.put("success", false);
                	json.put("message", res);
                	return json.toString();
            	}
        		
        	}else {
	        	json.put("success", false);
	        	json.put("message", "No se ha podido recuperar la factura");
	        	return json.toString();
        	}
          }
        }catch(Exception e) {
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", false);
            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
            return json.toString();
        }
        
        json.put("success", false);
    	json.put("message", "Ha ocurrido un error inesperado mientras se procesaba la factura");
        return json.toString();
        
    }
    
    

    @RequestMapping(value = "/rejectInvoiceFromOrderWithoutPayment.action", method = RequestMethod.POST)
    @ResponseBody public String rejectInvoiceFromOrderWithoutPayment(
    										  String addressBook, 
											  int documentNumber, 
											  String documentType){
 
    	JSONObject json = new JSONObject();
        try{

        UserDocument doc = null;
        List<UserDocument> list = documentsService.searchCriteriaByOrderNumber(documentNumber, documentType, addressBook,true);
        if(list != null) {
        	if(!list.isEmpty()) {
        		for(UserDocument o : list) {
        			if(!o.isAccept()) {
        				doc = o;
        				break;
        			}
        		}
        	}
        }
        
        if(doc!= null) {
                	doc.setStatus(true);
                	doc.setAccept(true);
                	documentsService.delete(doc.getId(),"");
                	documentsService.rejectInvoice(addressBook, documentNumber, documentType);

                	json.put("success", true);
                	json.put("message", "La factura ha sido procesada de forma exitosa");
                	return json.toString();
            	}
        }catch(Exception e) {
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", false);
            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
            return json.toString();
        }
        
        json.put("success", false);
    	json.put("message", "Ha ocurrido un error inesperado");
    	return json.toString();
        
    }
    
    @RequestMapping(value = "/uploadInvoice.action", method = RequestMethod.POST)
    @ResponseBody public String uploadInvoice(FileUploadBean uploadItem, 
    										  BindingResult result, 
    										  String addressBook, 
											  String tipoComprobante,
											  int fdId,
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
        
        InvoiceDTO inv = null;
        String ct = uploadItem.getFile().getContentType();
        if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)){
            if("text/xml".equals(ct.trim())){
            	ResponseGeneral respu=documentsService.validateInvoiceVsSat(uploadItem);
	    		 if(respu.isError()) {
	            	 System.err.println(new Gson().toJson(respu));
	            	 json.put("success", false);
		            	json.put("message", "Intente ingresar la factura mañana.");
		            	return json.toString();
		            	
	             }else {
	            	 if(respu.getDocument()!=null) {
	            		 CommonsMultipartFile bade=new CommonsMultipartFile(new BASE64DecodedMultipartFile(uploadItem.getFile().getFileItem(), respu.getDocument()));
	            		 uploadItem.setFile(bade);
	            	 }
	             }
                inv = documentsService.getInvoiceXml(uploadItem);
                if(!inv.getResponse().isError()){
                	
                	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante) && !"I".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no es de tipo FACTURA<br />(Tipo Comprobante = I" + ")");
                    	return json.toString();
                	}
                	
                	if(AppConstants.NC_FIELD.equals(tipoComprobante) && !"E".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no coresponde a una NOTA DE CREDITO<br />(Tipo Comprobante = E" + ")");
                    	return json.toString();
                	}
                	
                	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante) && !"P".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no corresponde a un COMPLEMENTO DE PAGO <br />(Tipo Comprobante = P" + ")");
                    	return json.toString();
                	}
                	
        			//String res = documentsService.validateInvoice(inv,addressBook,tipoComprobante);
                	
                	String res="";
                	if("".equals(res)){
                    	UserDocument doc = new UserDocument(); 
                    	doc.setAddressBook(addressBook);
                    	doc.setDocumentNumber(0);
                    	doc.setDocumentType("");
                    	doc.setContent(uploadItem.getFile().getBytes());
                    	doc.setType(ct.trim());
                    	doc.setName(uploadItem.getFile().getOriginalFilename());
                    	doc.setSize(uploadItem.getFile().getSize());
                    	doc.setStatus(true);
                    	doc.setAccept(true);
                    	doc.setFiscalType(tipoComprobante);
                    	doc.setType(tipoComprobante);
                    	doc.setFolio(inv.getFolio());
                    	doc.setSerie(inv.getSerie());
                    	doc.setUuid(inv.getUuid());
                    	doc.setUploadDate(new Date());
                    	doc.setFiscalRef(fdId);
                    	documentsService.save(doc, new Date(), "");
                    	
                    	FiscalDocuments fd = null;
                    	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)){
                    		fd = new FiscalDocuments();
                    	}
                    	
                    	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante)){
                    		fd = fiscalDocumentService.getById(fdId);
                    	}
                    	
                    	if(AppConstants.NC_FIELD.equals(tipoComprobante)){
                    		fd = fiscalDocumentService.getById(fdId);
                    	}
                    	
                    	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)){
                        	fd.setFolio(inv.getFolio());
                        	fd.setSerie(inv.getSerie());
                    		//fd.setInvoiceUploadDate(new Date());
                    		fd.setUuidFactura(inv.getUuid());
                    		fd.setUuidPago("");
                    		fd.setUuidNotaCredito("");
                    		fd.setFolioPago("");
                    		fd.setSeriePago("");
                    		fd.setFolioNC("");
                    		fd.setSerieNC("");
                        	fd.setStatus(AppConstants.STATUS_LOADINV);
                        	fd.setAddressNumber(addressBook);
                        	fd.setAmount(inv.getTotal());
                        	fd.setSubtotal(inv.getSubTotal());
                        	fd.setDescuento(inv.getDescuento());
                        	fd.setImpuestos(inv.getTotalImpuestos());
                        	fd.setRfcEmisor(inv.getRfcEmisor());
                        	fd.setRfcReceptor(inv.getRfcReceptor());
                        	fd.setMoneda(inv.getMoneda());
                        	fd.setInvoiceDate(inv.getFechaTimbrado());
                        	
                        	fd.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
                    	}
                    	
                    	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante)){
                    		fd.setFolioPago(inv.getFolio());
                    		fd.setSeriePago(inv.getSerie());
                    		fd.setUuidPago(inv.getUuid());
                    		//fd.setPaymentUploadDate(new Date());
                        	fd.setStatus(AppConstants.STATUS_LOADCP);
                    	}
                    	
                    	if(AppConstants.NC_FIELD.equals(tipoComprobante)){
                    		fd.setUuidNotaCredito(inv.getUuid());
                    		//fd.setCreditNoteUploadDate(new Date());
                    		fd.setFolioNC(inv.getFolio());
                    		fd.setSerieNC(inv.getSerie());
                        	fd.setStatus(AppConstants.STATUS_LOADNC);
                    	}
                    	
                    	if(fdId == 0)
                    	    fiscalDocumentService.saveDocument(fd);
                    	else
                    		fiscalDocumentService.updateDocument(fd);
                    	
        	            json.put("success", true);
        	            json.put("message", inv.getResponse().getMensaje().get("es"));
        	            json.put("addressNumber", addressBook);
        	            json.put("uuid", inv.getUuid());
                	}else{
                    	json.put("success", false);
                    	json.put("message", res);
                	}

                }else{
                	json.put("success", false);
                	json.put("message", "El archivo no es aceptado.  <br />NO ha pasado la fase de verificación y tampoco sera cargado a la solicitud.");
                }
                
            }else{
            	json.put("success", false);
            	json.put("message", "Para cargas de archivos fiscales, sólo se permiten tipos .xml");
            }
        }else{
        	UserDocument doc = new UserDocument(); 
        	doc.setAddressBook(addressBook);
        	doc.setDocumentNumber(0);
        	doc.setDocumentType("");
        	doc.setContent(uploadItem.getFile().getBytes());
        	doc.setType(ct.trim());
        	doc.setName(uploadItem.getFile().getOriginalFilename());
        	doc.setSize(uploadItem.getFile().getSize());
        	doc.setStatus(true);
        	doc.setAccept(true);
        	doc.setFiscalType(tipoComprobante);
        	doc.setUuid("");
        	doc.setUploadDate(new Date());
        	doc.setFiscalRef(fdId);
        	documentsService.save(doc, new Date(), "");
        	
            json.put("success", true);
            json.put("message", "El archivo ha sido cargado de forma exitosa");
            json.put("addressNumber", addressBook);
            
        }
        return json.toString();
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", true);
            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
        }
        return json.toString();

    }

    @RequestMapping(value = "/uploadInvoiceCB.action", method = RequestMethod.POST)
    @ResponseBody public String uploadInvoiceCB(FileUploadBean uploadItem, 
    										  BindingResult result, 
    										  String addressBook, 
											  String tipoComprobante,
											  int fdId,
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
        
        InvoiceDTO inv = null;
        String ct = uploadItem.getFile().getContentType();
        if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)){
            if("text/xml".equals(ct.trim())){
            	ResponseGeneral respu=documentsService.validateInvoiceVsSat(uploadItem);
	    		 if(respu.isError()) {
	            	 System.err.println(new Gson().toJson(respu));
	            	 json.put("success", false);
		            	json.put("message", "Intente ingresar la factura mañana.");
		            	return json.toString();
		            	
	             }else {
	            	 if(respu.getDocument()!=null) {
	            		 CommonsMultipartFile bade=new CommonsMultipartFile(new BASE64DecodedMultipartFile(uploadItem.getFile().getFileItem(), respu.getDocument()));
	            		 uploadItem.setFile(bade);
	            	 }
	             }
                inv = documentsService.getInvoiceXml(uploadItem);
                if(!inv.getResponse().isError()){
                	
                	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante) && !"I".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no es de tipo FACTURA<br />(Tipo Comprobante = I" + ")");
                    	return json.toString();
                	}
                	
                	if(AppConstants.NC_FIELD.equals(tipoComprobante) && !"E".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no coresponde a una NOTA DE CREDITO<br />(Tipo Comprobante = E" + ")");
                    	return json.toString();
                	}
                	
                	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante) && !"P".equals(inv.getTipoComprobante())){
                		json.put("success", false);
                    	json.put("message", "El documento cargado no corresponde a un COMPLEMENTO DE PAGO <br />(Tipo Comprobante = P" + ")");
                    	return json.toString();
                	}
                	
        			//String res = documentsService.validateInvoice(inv,addressBook,tipoComprobante);
                	
                	String res="";
                	if("".equals(res)){
                    	UserDocument doc = new UserDocument(); 
                    	doc.setAddressBook(addressBook);
                    	doc.setDocumentNumber(0);
                    	doc.setDocumentType("");
                    	doc.setContent(uploadItem.getFile().getBytes());
                    	doc.setType(ct.trim());
                    	doc.setName(uploadItem.getFile().getOriginalFilename());
                    	doc.setSize(uploadItem.getFile().getSize());
                    	doc.setStatus(true);
                    	doc.setAccept(true);
                    	doc.setFiscalType(tipoComprobante);
                    	doc.setType(tipoComprobante);
                    	doc.setFolio(inv.getFolio());
                    	doc.setSerie(inv.getSerie());
                    	doc.setUuid(inv.getUuid());
                    	doc.setUploadDate(new Date());
                    	doc.setFiscalRef(fdId);
                    	documentsService.save(doc, new Date(), "");
                    	
                    	CustomBroker fd = null;
                    	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)){
                    		fd = new CustomBroker();
                    	}
                    	
                    	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante)){
                    		fd = customBrokerService.getById(fdId);
                    	}
                    	
                    	if(AppConstants.NC_FIELD.equals(tipoComprobante)){
                    		fd = customBrokerService.getById(fdId);
                    	}
                    	
                    	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)){
                        	fd.setFolio(inv.getFolio());
                        	fd.setSerie(inv.getSerie());
                    		//fd.setInvoiceUploadDate(new Date());
                    		fd.setUuidFactura(inv.getUuid());
                    		fd.setUuidPago("");
                    		fd.setUuidNotaCredito("");
                    		fd.setFolioPago("");
                    		fd.setSeriePago("");
                    		fd.setFolioNC("");
                    		fd.setSerieNC("");
                        	fd.setStatus(AppConstants.STATUS_LOADINV);
                        	fd.setAddressNumber(addressBook);
                        	fd.setAmount(inv.getTotal());
                        	fd.setSubtotal(inv.getSubTotal());
                        	fd.setDescuento(inv.getDescuento());
                        	fd.setImpuestos(inv.getTotalImpuestos());
                        	fd.setRfcEmisor(inv.getRfcEmisor());
                        	fd.setRfcReceptor(inv.getRfcReceptor());
                        	fd.setMoneda(inv.getMoneda());
                        	fd.setInvoiceDate(inv.getFechaTimbrado());
                    	}
                    	
                    	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante)){
                    		fd.setFolioPago(inv.getFolio());
                    		fd.setSeriePago(inv.getSerie());
                    		fd.setUuidPago(inv.getUuid());
                    		//fd.setPaymentUploadDate(new Date());
                        	fd.setStatus(AppConstants.STATUS_LOADCP);
                    	}
                    	
                    	if(AppConstants.NC_FIELD.equals(tipoComprobante)){
                    		fd.setUuidNotaCredito(inv.getUuid());
                    		//fd.setCreditNoteUploadDate(new Date());
                    		fd.setFolioNC(inv.getFolio());
                    		fd.setSerieNC(inv.getSerie());
                        	fd.setStatus(AppConstants.STATUS_LOADNC);
                    	}
                    	
                    	if(fdId == 0)
                    		customBrokerService.saveDocument(fd);
                    	else
                    		customBrokerService.updateDocument(fd);
                    	
        	            json.put("success", true);
        	            json.put("message", inv.getResponse().getMensaje().get("es"));
        	            json.put("addressNumber", addressBook);
        	            json.put("uuid", inv.getUuid());
                	}else{
                    	json.put("success", false);
                    	json.put("message", res);
                	}

                }else{
                	json.put("success", false);
                	json.put("message", "El archivo no es aceptado.  <br />NO ha pasado la fase de verificación y tampoco sera cargado a la solicitud.");
                }
                
            }else{
            	json.put("success", false);
            	json.put("message", "Para cargas de archivos fiscales, sólo se permiten tipos .xml");
            }
        }else{
        	UserDocument doc = new UserDocument(); 
        	doc.setAddressBook(addressBook);
        	doc.setDocumentNumber(0);
        	doc.setDocumentType("");
        	doc.setContent(uploadItem.getFile().getBytes());
        	doc.setType(ct.trim());
        	doc.setName(uploadItem.getFile().getOriginalFilename());
        	doc.setSize(uploadItem.getFile().getSize());
        	doc.setStatus(true);
        	doc.setAccept(true);
        	doc.setFiscalType(tipoComprobante);
        	doc.setUuid("");
        	doc.setUploadDate(new Date());
        	doc.setFiscalRef(fdId);
        	documentsService.save(doc, new Date(), "");
        	
            json.put("success", true);
            json.put("message", "El archivo ha sido cargado de forma exitosa");
            json.put("addressNumber", addressBook);
            
        }
        return json.toString();
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", true);
            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
        }
        return json.toString();

    }
        
    @RequestMapping(value = "/uploadExcelSuppliers.action", method = RequestMethod.POST)
    @ResponseBody public String uploadSuppliers(FileUploadBean uploadItem, 
    								   BindingResult result, HttpServletResponse response){
 
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

        if(uploadItem.getFile().getOriginalFilename().endsWith(".xlsx") || uploadItem.getFile().getOriginalFilename().endsWith(".xls") ){

        	org.json.JSONObject count = documentsService.processExcelFile(uploadItem);
        	//String respProcEF = count.getString("message");
        	
        	if(count.getBoolean("success")) {
        		json.put("success", true);
                json.put("message", "El archivo " + uploadItem.getFile().getOriginalFilename() + " ha sido cargado exitosamente.");
                json.put("fileName", uploadItem.getFile().getOriginalFilename());
                json.put("count", count.getInt("count"));
        	}else {
        		json.put("success", false);
        		json.put("error_data_template", true);
            	json.put("message_es", count.getString("message_es"));
            	json.put("message_en", count.getString("message_en"));
        	}
        	/*int count = documentsService.processExcelFile(uploadItem);
        	
        	if(count>0) {
        		json.put("success", true);
                json.put("message", "El archivo " + uploadItem.getFile().getOriginalFilename() + " ha sido cargado exitosamente.");
                json.put("fileName", uploadItem.getFile().getOriginalFilename());
                json.put("count", count);
        	}else {
        		json.put("success", false);
                json.put("message", "ERROR_DATA_NULL");
        	}*/
            
        }else{
        	json.put("success", false);
        	json.put("message", "Error: Sólo se permiten archivos tipo .xlsx o .xls");
        }

        return json.toString();
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", false);
        	json.put("message", e.getMessage());

        }
        return json.toString();

    }    
        
    @RequestMapping(value = "/uploadExcelCodigosSat.action", method = RequestMethod.POST)
    @ResponseBody public String uploadCodigosSat(FileUploadBean uploadItem, 
    								   BindingResult result, HttpServletResponse response){
 
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
 		String userAuth = auth.getName();
 		String ip = request.getRemoteAddr();
 		Date currentDate = new Date();

        try{

        if (result.hasErrors()){
            for(ObjectError error : result.getAllErrors()){
                System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
            }
 
            json.put("success", false);
            json.put("message", "Error al cargar el archivo");
        }

        if(uploadItem.getFile().getOriginalFilename().endsWith(".xlsx") || uploadItem.getFile().getOriginalFilename().endsWith(".xls") ){

        	Workbook workbook = null;
        	workbook = WorkbookFactory.create(uploadItem.getFile().getInputStream());
        	 //int count = codigosSATService.processExcelFile(uploadItem);
        	 BatchProcessService bps = new BatchProcessService();
        	 bps.setCodigoSatDao(codigosSatDao);
        	 bps.setLogger(logger);
        	 bps.setFile(workbook);
        	 Thread codSatThread = new Thread(bps);
        	 codSatThread.start();

            json.put("success", true);
            json.put("message", "El archivo " + uploadItem.getFile().getOriginalFilename() + " ha sido sometido al proceso batch.");
            json.put("fileName", uploadItem.getFile().getOriginalFilename());
            json.put("count", 1);
            
            dataAuditService.saveDataAudit("UploadExcelCodigosSat", null, currentDate, ip,
            userAuth, uploadItem.getFile().getOriginalFilename(), "save", "Upload Excel CodigosSat Successful" ,null, null, null, 
            null, AppConstants.STATUS_COMPLETE, AppConstants.SAT_MODULE);
            
        }else{
        	json.put("success", false);
        	json.put("message", "Error: Sólo se permiten archivos tipo .xlsx o .xls");
        }

        return json.toString();
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", false);
        	json.put("message", e.getMessage());

        }
        return json.toString();

    }


    @RequestMapping("/uploadResources.action")
    @ResponseBody public String  uploadResources(FileUploadBean file,
    							   BindingResult result, 
 								   String addressBook, 
 								   int documentNumber, 
 								   String documentType,
 								   String tipoComprobante,
 		                           HttpServletResponse response){

    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();

        String invCT = file.getFile().getContentType();
        String cnCT = file.getFileTwo().getContentType();

        if("text/xml".equals(invCT) && "text/xml".equals(cnCT) ){
            InvoiceDTO inv = documentsService.getInvoiceXml(file);
            InvoiceDTO cn = documentsService.getCreditNoteXml(file);
            
            if(!inv.getResponse().isError() && cn != null){
            	
            	if(!"I".equals(inv.getTipoComprobante())){
            		json.put("success", false);
                	json.put("message", "La factura cargada no es de tipo Tipo Comprobante = I");
                	return json.toString();
            	}
            	
            	if(!"E".equals(cn.getTipoComprobante())){
            		json.put("success", false);
            		json.put("message", "La nota de crédito cargada no es de tipo Tipo Comprobante = E");
                	return json.toString();
            	}
            	
            	PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(documentNumber, addressBook, documentType);
            	
            	if(po != null) {
                	String res = "";
                	String xmlInvContent = "";
                	String xmlCNContent = "";
                	
            		try {
            		ByteArrayInputStream stream = new  ByteArrayInputStream(file.getFile().getBytes());
        			xmlInvContent = IOUtils.toString(stream, "UTF-8");
        			xmlInvContent = takeOffBOM(IOUtils.toInputStream(xmlInvContent, "UTF-8"));
        			xmlInvContent = xmlInvContent.replace("?<?xml", "<?xml");
        			
            		stream = new  ByteArrayInputStream(file.getFileTwo().getBytes());
        			xmlCNContent = IOUtils.toString(stream, "UTF-8");
        			xmlCNContent = takeOffBOM(IOUtils.toInputStream(xmlCNContent, "UTF-8"));
        			xmlCNContent = xmlCNContent.replace("?<?xml", "<?xml");
        			
                	res = documentsService.validateInvoiceFromOrderWitCreditNote(inv, cn,
 						   addressBook, 
 						   documentNumber, 
 						   documentType,
 						   AppConstants.INVOICE_FIELD,
 						   po, false,
 						   xmlInvContent,
 						   xmlCNContent);
        			
            		}catch(Exception e) {
            			log4j.error("Exception" , e);
            			e.printStackTrace();
            		}
                	
                		if("".equals(res)) {
                			
                			if(!"".equals(xmlInvContent) && !"".equals(xmlCNContent)) {
                				
                    			res = documentsService.processInvoiceAndCreditNoteFromOrder(inv,
										   cn,
		                                   addressBook, 
		           						   documentNumber, 
		          						   documentType,
		          						   po);
                    			
                    			if("".equals(res)){
                                	UserDocument doc = new UserDocument(); 
                                	doc.setAddressBook(po.getAddressNumber());
                                	doc.setDocumentNumber(documentNumber);
                                	doc.setDocumentType(documentType);
                                	doc.setContent(file.getFile().getBytes());
                                	doc.setType(invCT.trim());
                                	doc.setName(file.getFile().getOriginalFilename());
                                	doc.setSize(file.getFile().getSize());
                                	doc.setStatus(true);
                                	doc.setAccept(true);
                                	doc.setFiscalType(inv.getTipoComprobante());
                                	doc.setType(inv.getTipoComprobante());
                                	doc.setFolio(inv.getFolio());
                                	doc.setSerie(inv.getSerie());
                                	doc.setUuid(inv.getUuid());
                                	doc.setUploadDate(new Date());
                                	doc.setFiscalRef(0);
                                	documentsService.save(doc, new Date(), "");
                                	
                                	doc = new UserDocument(); 
                                	doc.setAddressBook(po.getAddressNumber());
                                	doc.setDocumentNumber(documentNumber);
                                	doc.setDocumentType(documentType);
                                	doc.setContent(file.getFileTwo().getBytes());
                                	doc.setType(cn.getTipoComprobante());
                                	doc.setName(file.getFileTwo().getOriginalFilename());
                                	doc.setSize(file.getFileTwo().getSize());
                                	doc.setStatus(true);
                                	doc.setAccept(true);
                                	doc.setFiscalType(cn.getTipoComprobante());
                                	doc.setType(cn.getTipoComprobante());
                                	doc.setFolio(cn.getFolio());
                                	doc.setSerie(cn.getSerie());
                                	doc.setUuid(cn.getUuid());
                                	doc.setUploadDate(new Date());
                                	doc.setFiscalRef(0);
                                	documentsService.save(doc, new Date(), "");
                                	
                                	po.setInvoiceUuid(inv.getUuid());
                                	po.setCreditNotUuid(cn.getUuid());
                                	po.setStatus(AppConstants.STATUS_LOADINV);
                                	purchaseOrderService.updateOrders(po);
                                	
                                	json.put("success", false);
                            		json.put("message", "Los archivos han sido cargados exitosamente");
                                	return json.toString();
                    			}
                			}
                			

                            	
                		}else {
                			json.put("success", false);
                    		json.put("message", res);
                        	return json.toString();
                		}

	
            	}else {
            		json.put("success", false);
            		json.put("message", "La orden de compra no existe para ese proveedor");
            	}
            	

            	
            }else {
            	json.put("success", false);
                json.put("message", "Uno de los archivos no corresponde al formato de factura electrónica");
            }
        }else {
        	json.put("success", false);
            json.put("message", "Los archivos deben estar en formato .xml");
        }
        
        return json.toString();
    	
    }
    
    
    @RequestMapping(value = "/uploadForeignAdditional.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String uploadForeignAdditional(FileUploadBean uploadItem, 
    										  BindingResult result, 
    										  String addressBook, 
											  int documentNumber, 
											  String documentType,
								    		  HttpServletResponse response){
 
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
     	String userAuth = auth.getName();
     	Date currentDate = new Date();
    	
    	try {
            if (result.hasErrors()){
                for(ObjectError error : result.getAllErrors()){
                    System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
                }
     
                json.put("success", false);
                json.put("message", "Error_1");
            }
            
            String ct = uploadItem.getFile().getContentType();
        	PurchaseOrder po = purchaseOrderService.searchbyOrderAndAddressBook(documentNumber, addressBook, documentType);
        	UserDocument doc = new UserDocument(); 
        	
        	if(po != null) {
            	doc.setAddressBook(po.getAddressNumber());
            	doc.setDocumentNumber(documentNumber);
            	doc.setDocumentType(documentType);
            	doc.setContent(uploadItem.getFile().getBytes());
            	doc.setType(ct.trim());
            	doc.setName("FACT_FOR_" + uploadItem.getFile().getOriginalFilename());
            	doc.setSize(uploadItem.getFile().getSize());
            	doc.setStatus(true);
            	doc.setAccept(true);
            	doc.setFiscalType("Otros");
            	doc.setUuid("");
            	doc.setUploadDate(new Date());
            	doc.setFiscalRef(0);
            	documentsService.save(doc, new Date(), "");   
            	
            	dataAuditService.saveDataAudit("UploadForeign",po.getAddressNumber(), currentDate, request.getRemoteAddr(),
                userAuth, "FACT_FOR_" + uploadItem.getFile().getOriginalFilename(), "uploadForeignAdditional", 
                "Uploaded Foreign Invoice Successful",documentNumber+"", po.getOrderNumber()+"", null, 
                null, AppConstants.STATUS_COMPLETE, AppConstants.SALESORDER_MODULE);                    	
                            	
            	
                json.put("success", true);
                json.put("message", "Success");
                json.put("orderNumber", documentNumber);
                json.put("orderType", documentType);
                json.put("addressNumber", addressBook);
        	}else {
                json.put("success", false);
                json.put("message", "Error_7");
        	}
        	
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
            json.put("success", false);
            json.put("message", "Error_1");
		}
        return json.toString();

    }
    
    @RequestMapping(value = "/uploadForeignAdditionalFD.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String uploadForeignAdditionalFD(FileUploadBean uploadItem, 
    										  BindingResult result, 
    										  String addressBook,
    										  String company,
    										  String currentUuid,
    										  String invoiceNumber,
								    		  HttpServletResponse response){
 
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();

        if (result.hasErrors()){
            for(ObjectError error : result.getAllErrors()){
                System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
            } 
            json.put("success", false);
            json.put("message", "Error al cargar el archivo");
        } else {
            String uuid = "";
            if("".equals(currentUuid)) {
            	uuid = getUuidNoOC(addressBook, company);
            } else {
            	uuid = currentUuid;
            }
            
            String ct = uploadItem.getFile().getContentType();        
            UserDocument doc = new UserDocument(); 
        	doc.setAddressBook(addressBook);
        	doc.setDocumentNumber(0);
        	doc.setDocumentType("Honorarios");
        	doc.setContent(uploadItem.getFile().getBytes());
        	doc.setType(ct.trim());
        	doc.setName(uploadItem.getFile().getOriginalFilename());
        	doc.setSize(uploadItem.getFile().getSize());
        	doc.setStatus(true);
        	doc.setAccept(true);
        	doc.setFiscalType("Factura");
        	doc.setUuid(uuid);
        	doc.setFolio(invoiceNumber);
        	doc.setSerie("");
        	doc.setUploadDate(new Date());
        	doc.setFiscalRef(0);
        	doc.setDescription("MainUUID_".concat(uuid));
        	documentsService.save(doc, new Date(), "");            	
        	
            json.put("success", true);
            json.put("message", "El archivo ha sido cargado de forma exitosa");
            json.put("addressNumber", addressBook);
            json.put("uuid", uuid);        	
        }

        return json.toString();
    }
       
    @RequestMapping(value = "/uploadForeignAdditionalCB.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String uploadForeignAdditionalCB(FileUploadBean uploadItem, 
    										  BindingResult result, 
    										  String addressBook,
    										  String company,
    										  String currentUuid,
    										  String invoiceNumber,
								    		  HttpServletResponse response){
 
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();

        if (result.hasErrors()){
            for(ObjectError error : result.getAllErrors()){
                System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
            } 
            json.put("success", false);
            json.put("message", "Error al cargar el archivo");
        } else {
            String uuid = "";
            if("".equals(currentUuid)) {
            	uuid = getUuidNoOC(addressBook, company);
            } else {
            	uuid = currentUuid;
            }
            
            String ct = uploadItem.getFile().getContentType();        
            UserDocument doc = new UserDocument(); 
        	doc.setAddressBook(addressBook);
        	doc.setDocumentNumber(0);
        	doc.setDocumentType("Honorarios");
        	doc.setContent(uploadItem.getFile().getBytes());
        	doc.setType(ct.trim());
        	doc.setName(uploadItem.getFile().getOriginalFilename());
        	doc.setSize(uploadItem.getFile().getSize());
        	doc.setStatus(true);
        	doc.setAccept(true);
        	doc.setFiscalType("Factura");
        	doc.setUuid(uuid);
        	doc.setFolio(invoiceNumber);
        	doc.setSerie("");
        	doc.setUploadDate(new Date());
        	doc.setFiscalRef(0);
        	doc.setDescription("MainUUID_".concat(uuid));
        	documentsService.save(doc, new Date(), "");            	
        	
            json.put("success", true);
            json.put("message", "El archivo ha sido cargado de forma exitosa");
            json.put("addressNumber", addressBook);
            json.put("uuid", uuid);        	
        }

        return json.toString();
    }
    
    @RequestMapping(value = "/uploadOthersDocsReceipt.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String uploadOthersDocsReceipt(FileUploadBean uploadItem, 
    										  BindingResult result, 
    										  String addressBook,
    										  String company,
    										  String currentUuid,
    										  String invoiceNumber,
    										  String orderType,
								    		  HttpServletResponse response){
 
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();

        if (result.hasErrors()){
            for(ObjectError error : result.getAllErrors()){
                System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
            } 
            json.put("success", false);
            json.put("message", "Error al cargar el archivo");
        } else {
            String uuid = "";
            if("".equals(currentUuid)) {
            	uuid = getUuidNoOC(addressBook, company);
            } else {
            	uuid = currentUuid;
            }
            
            String ct = uploadItem.getFile().getContentType();        
            UserDocument doc = new UserDocument(); 
        	doc.setAddressBook(addressBook);
        	doc.setDocumentNumber(Integer.valueOf(invoiceNumber));
        	doc.setDocumentType(orderType);
        	doc.setContent(uploadItem.getFile().getBytes());
        	doc.setType(ct.trim());
        	doc.setName(uploadItem.getFile().getOriginalFilename());
        	doc.setSize(uploadItem.getFile().getSize());
        	doc.setStatus(true);
        	doc.setAccept(true);
        	doc.setFiscalType("Otros");
        	doc.setUuid(uuid);
        	doc.setFolio("");
        	doc.setSerie("");
        	doc.setUploadDate(new Date());
        	doc.setFiscalRef(0);
        	doc.setDescription("MainUUID_".concat(uuid));
        	documentsService.save(doc, new Date(), "");            	
        	
            json.put("success", true);
            json.put("message", "El archivo ha sido cargado de forma exitosa");
            json.put("addressNumber", addressBook);
            json.put("uuid", uuid);        	
        }

        return json.toString();
    }

    
    @RequestMapping(value = "/deleteuuidremoteSerever.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String deleteuuidremoteSerever( HttpServletResponse response){
 
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();
            json.put("success", false);
            json.put("message", "Error al cargar el archivo");
            
           List<UserDocument> docum= documentsService.getDocumentsList(0, 10000);
           ArrayList<String> lisDocument=new ArrayList<>();
           
           for (UserDocument string : docum) {
        	   
			lisDocument.add(string.getUuid());
			lisDocument.add(string.getName().split("\\.")[0]);

		}
           
           ftpService.EliminarByUUid(lisDocument);
           
           
           
            
            
            
            
       

        return json.toString();
    }

    
    
    
    public Map<String,Object> mapOK(String obj){
    	Map<String,Object> modelMap = new HashMap<String,Object>(2);
    	modelMap.put("message", obj);
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
	
	private String getTimeSuffix() {
		StringBuilder str = new StringBuilder();
		str.append(Calendar.getInstance().get(Calendar.YEAR));
		str.append("-");
		String month = org.apache.commons.lang.StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.MONTH ) + 1),2,"0");
		String day = org.apache.commons.lang.StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH )),2,"0");
		str.append(month);
		str.append(day);
		str.append("-");
		String hour = org.apache.commons.lang.StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY )),2,"0");
		String minute = org.apache.commons.lang.StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.MINUTE )),2,"0");
		str.append(hour);
		str.append(minute);
		return str.toString();
	}
	
	private String getUuidNoOC(String addressNumber, String company) {
		StringBuilder str = new StringBuilder();
		String supNbr = org.apache.commons.lang.StringUtils.leftPad(addressNumber,8,"0");
		str.append(supNbr);
		str.append("-");
		str.append(Calendar.getInstance().get(Calendar.YEAR));
		str.append("-");
		String month = org.apache.commons.lang.StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.MONTH ) + 1),2,"0");
		String day = org.apache.commons.lang.StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH )),2,"0");
		str.append(month);
		str.append(day);
		str.append("-");
		String hour = org.apache.commons.lang.StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY )),2,"0");
		String minute = org.apache.commons.lang.StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.MINUTE )),2,"0");
		str.append(hour);
		str.append(minute);
		str.append("-");
		String second = org.apache.commons.lang.StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.SECOND)),2,"0");
		str.append(second);
		str.append(org.apache.commons.lang.StringUtils.leftPad(company,6,"0"));
		str.append("NOOC");
		return str.toString();
	}
	
}
