package com.eurest.supplier.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.model.CDDDCEmployee;
import com.eurest.supplier.model.OutSourcingDocument;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.service.DocumentsService;
import com.eurest.supplier.service.EmailService;
import com.eurest.supplier.service.OutSourcingService;
import com.eurest.supplier.service.SupplierService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.service.XmlToPojoService;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.MultiFileUploadBean;
import com.eurest.supplier.util.PDFutils2;

import net.sf.json.JSONObject;


@Controller
public class OutSourcingController {
	
   	@Autowired
   	OutSourcingService outSourcingService;
   	
   	@Autowired
	DocumentsService documentsService;
   	
	@Autowired
	SupplierService supplierService;
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	XmlToPojoService xmlToPojoService;
	
	Logger log4j = Logger.getLogger(OutSourcingController.class);
	
    @RequestMapping(value = "/rejectOutSourcingDocument.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
   	public @ResponseBody String rejectOutSourcingDocument(@RequestParam int id, @RequestParam String notes, 
   														  @RequestParam String frequency, @RequestParam String uuid, 
   														  HttpServletResponse response){

     	
   		response.setContentType("text/html");
   		response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		
		if("INV".equals(frequency)) {
			outSourcingService.rejectInvoice(uuid, notes, user);
		}else {
			outSourcingService.rejectDocument(id, notes, user);
		}
		json.put("success", true);
    	json.put("message", "El documento ha sido rechazado y el porveedor ha sido notificado");
    	return json.toString();    	
    }
    
    @RequestMapping(value = "/approveOutSourcingDocument.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
   	public @ResponseBody String approveOutSourcingDocument(@RequestParam int id, @RequestParam String notes, 
												   		   @RequestParam String frequency, @RequestParam String uuid,
												   		   HttpServletResponse response){

     	
   		response.setContentType("text/html");
   		response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		
		if("INV".equals(frequency)) {
			outSourcingService.approveInvoice(uuid, notes, user);
		}else {
			outSourcingService.approveDocument(id, notes, user);
		}
		
		json.put("success", true);
    	json.put("message", "El documento ha sido aprobado y el porveedor ha sido notificado");
    	return json.toString();    	
    }

	@RequestMapping(value ="/downloadDocumentsZip.action", method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	 public @ResponseBody String downloadDocumentsZip(HttpServletResponse response,  @RequestParam String ids) throws IOException {

		List<String> filesIDs = Arrays.asList(ids.split(","));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(baos);

		long totalLenght = 0;

		int count = 1;

		for (String id : filesIDs) {
			OutSourcingDocument doc = outSourcingService.getDocumentById(Integer.valueOf(id));
			String fileName = doc.getName();
			// String contentType = doc.getType();
			fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "");
			ZipEntry entry = new ZipEntry(count + "_" + fileName);
			zipOut.putNextEntry(entry);
			InputStream is = new ByteArrayInputStream(doc.getContent());
			byte[] bytes = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(bytes)) != -1) {
				zipOut.write(bytes, 0, bytesRead);
			}
			is.close();
			zipOut.flush();

			count++;
			totalLenght = totalLenght + doc.getSize();

		}

		zipOut.close();

		byte[] bytesRead = baos.toByteArray();
		String encodedBase64 = new String(Base64.getEncoder().encodeToString(bytesRead));

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		return encodedBase64;
   
   
   }
    
	@RequestMapping(value = "/uploadOSReplacementFile.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String uploadOSReplacementFile(@ModelAttribute MultiFileUploadBean multiFileUploadBean, 
	   				    										  String addressNumber,
	   				    										  int id,
	   				    										  String documentType,
	   												    		  HttpServletResponse response){
	    	
   		response.setContentType("text/html");
   		response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();
   		
    	List<MultipartFile> uploadedFiles = multiFileUploadBean.getUploadedFiles();
    	if(uploadedFiles.size() == 1) {
        	if("CEDU_DETERM_CUOTAS".equals(documentType)) {
        		String[] data = null;
    			try {
    				data = new PDFutils2().getPdfText(uploadedFiles.get(0).getBytes(), 2, 0);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}		
    			if(data == null) {
    	    		json.put("success", false);
    	    		json.put("message", "El formato del archivo Cédula de determinación de cuotas es INVALIDO, favor de cargar el archivo en un formato PDF/texto");
    	    		return json.toString();
    			}		
    			String string="";
    			for (String string2 : data) {
    				string=string+" "+string2;
    			}			
    			if(data == null || !(string.contains("SISTEMA ÚNICO DE AUTODETERMINACIÓN") && string.contains("Período de Proceso") && string.contains("CÉDULA DE DETERMINACIÓN DE CUOTAS"))) {
    	    		json.put("success", false);
    	    		json.put("message", "El formato del archivo Cédula de determinación de cuotas es INVALIDO, favor de cargar el archivo en un formato PDF/texto");
    	    		return json.toString();
    			} else {
    				MultipartFile file = uploadedFiles.get(0);
    				outSourcingService.saveReplacementFile(file, addressNumber, id); // , company, getUserCompany()
    	    		json.put("success", true);
    	    		json.put("message", "");
    	    		return json.toString();	
    			}
        	} else {
            	MultipartFile file = uploadedFiles.get(0);
    	    	if("".equals(outSourcingService.saveReplacementFile(file, addressNumber, id))) {  // , company, getUserCompany()
    	    		json.put("success", true);
    	    		json.put("message", "");
    	    		return json.toString();	        	
    	    	} else {
    	    		json.put("success", false);
    	    		json.put("message", "Ha ocurrido un error inesperado");
    	    		return json.toString();
    	    	}
        	}	        
        } else {
       		json.put("success", false);
            json.put("message", "Ha ocurrido un error inesperado");
            return json.toString();
       	}  		
    }
	
    @RequestMapping(value = "/uploadMonthlyFiles.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
   	public String uploadMonthlyFilesFiles(Model model, @ModelAttribute MultiFileUploadBean multiFileUploadBean, 
   				    										  String addressNumber,
   												    		  HttpServletResponse response){
   		response.setContentType("text/html");
   		response.setCharacterEncoding("UTF-8");
   		String res = outSourcingService.saveMonthlyDocs(multiFileUploadBean, addressNumber);
   		
   		if("".equals(res)) {
   			model.addAttribute("message", "_SUCCESSMONTH");
   			return "login";
   		}else {
   			//model.addAttribute("message", "_ERROR");
   			model.addAttribute("message", Base64.getEncoder().encodeToString(res.getBytes()));
   			return "redirect:home.action";
   		}
   		//return "login";
    }

    @RequestMapping(value = "/uploadQuarterlyFiles.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
   	public String uploadQuarterlyFilesFiles(Model model, @ModelAttribute MultiFileUploadBean multiFileUploadBean, 
   				    										  String addressNumber,
   												    		  HttpServletResponse response){
   		response.setContentType("text/html");
   		response.setCharacterEncoding("UTF-8");
   		String res = outSourcingService.saveQuarterlyDocs(multiFileUploadBean, addressNumber);
   		   		
   		if("".equals(res)) {
   			model.addAttribute("message", "_SUCCESSQUARTER");
   			return "login";
   		}else {
   			//model.addAttribute("message", "_ERROR");
   			model.addAttribute("message", Base64.getEncoder().encodeToString(res.getBytes()));
   			return "redirect:home.action";
   		}
   		//return "login";
    }    
    
    @RequestMapping(value = "/uploadBaseLineFiles.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
   	public String uploadBaseLineFiles(Model model, 
   									  @ModelAttribute MultiFileUploadBean multiFileUploadBean, 
   				    				  String addressNumber,
   				    				  String effectiveDate,
   				    				  HttpServletResponse response){
    	
   		response.setContentType("text/html");
   		response.setCharacterEncoding("UTF-8");
   		
   		if("".equals(outSourcingService.saveBaseLineDocument(multiFileUploadBean, effectiveDate, addressNumber))){
   			model.addAttribute("message", "_SUCCESS");
   		}else {
   			model.addAttribute("message", "_ERROR");
   		}
   		return "login";
    }
	
	@RequestMapping(value = "/uploadOSInvoiceFiles2.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String uploadOSInvoiceFiles(@ModelAttribute MultiFileUploadBean multiFileUploadBean, 
	   				    										  String addressNumber,
	   				    										  String uuid,
	   				    										  String company,
	   				    										  String periodText,
	   				    										  String periodTextVale,
	   												    		  HttpServletResponse response){
	    	
   		response.setContentType("text/html");
   		response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();	
   		
    	List<MultipartFile> uploadedFiles = multiFileUploadBean.getUploadedFiles();
    	if(uploadedFiles.size() == 1) {
	        MultipartFile file = uploadedFiles.get(0);
	        String ct = file.getContentType();
	        if("application/x-zip-compressed".equals(ct.trim()) ||
	            	"application/zip".equals(ct.trim())){
	        	
	        	boolean banderaDeFechas=true;
	        	List<CDDDCEmployee> listnom=outSourcingService.getListTemp(periodText);
	        	String resp="";
	        	 for (CDDDCEmployee cdddcEmployee : listnom) {
						resp=resp+cdddcEmployee.getNombre()+",";
					}
			    List<String> stringListBD = resp.equals("")?new ArrayList<String>():new ArrayList<String>(Arrays.asList(resp.split(",")));
			    if (stringListBD.size()<1) {
			    	json.put("success", false);
		        	json.put("message", "No se ha cargado la CÉDULA DE DETERMINACIÓN DE CUOTAS el periodo "+periodText);
		        	return json.toString();
				}
			    			   
			    try {
					ArrayList<String> listNominaZip= outSourcingService.getListnames(file.getBytes());
					if(listNominaZip.size()<1) {
						json.put("success", false);
			        	json.put("message", "No hay registros correctos en el zip");
			        	return json.toString();
					}
					
					String listBad="";
					for (String string : listNominaZip) {
						boolean banderaListnom=true;							
						
						for (String string2 : stringListBD) {									
							
							String string22 = (string2.replace(" ", "")).trim();
							String string1  = (string.replace(" ", "").toUpperCase()).trim();
							string1  = string1.replace("Á", "A");
							string1  = string1.replace("É", "E");
							string1  = string1.replace("Í", "I");
							string1  = string1.replace("Ó", "O");
							string1  = string1.replace("Ú", "U");
							//string1 = string1.replaceAll("[^\\dA-Za-z ]", "");
														
							if (string22.contains(string1)) {
								
							banderaListnom=false;
							break;}
							else {
								banderaListnom=false;
								for (String string3 : string.trim().split(" ")) {
									if(!string2.contains(string3)) {
										banderaListnom=true;
										break;
									}
								}
								if(!banderaListnom) {
									break;
								}
								
							}
						}
						if(banderaListnom) {
							listBad=listBad+"<br>"+string;
						}
						
					}
					if (listBad.length()>0) {
						json.put("success", false);
			        	json.put("message", "Los siguientes empleados no se encontraron en la CÉDULA DE DETERMINACIÓN DE CUOTAS del periodo: <br>"+listBad);
			        	return json.toString();
					}
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			    
	        	
		   		if("".equals(outSourcingService.saveInvoiceZipDoc(file, addressNumber, uuid, periodTextVale,banderaDeFechas))) { //company,
		   			json.put("success", true);
		        	json.put("message", "");
		        	return json.toString();
		        	
		   		}else {
		   			json.put("success", false);
		        	json.put("message", "Ha ocurrido un error inesperado");
		        	return json.toString();
		   		}
		   		
	        
        }else {
        	json.put("success", false);
    	json.put("message", "No es un archivo zip valido");
    	return json.toString();
        }	        
	        
    	}else {
   			json.put("success", false);
        	json.put("message", "Ha ocurrido un error inesperado");
        	return json.toString();
   		}
    	
    	//return "";
    }
    
    @RequestMapping(value = "/uploadOSInvoiceFiles.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
   	public @ResponseBody String uploadOSInvoiceFiles(@ModelAttribute MultiFileUploadBean multiFileUploadBean, 
   				    										  String addressNumber,
   				    										  int orderNumber,
   				    										  String orderType,
   												    		  HttpServletResponse response){
    	
   		response.setContentType("text/html");
   		response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();
   		
   		if("".equals(outSourcingService.saveInvoiceDocs(multiFileUploadBean, addressNumber, orderNumber, orderType))) {
   			json.put("success", true);
        	json.put("message", "");
        	return json.toString();
   		}else {
   			json.put("success", true);
        	json.put("message", "Ha ocurrido un error inesperado");
        	return json.toString();
   		}
    }
    
    @RequestMapping(value = "/supplier/searchOSDocuments.action", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> searchOSDocuments(@RequestParam int start,
															   @RequestParam int limit,
															   @RequestParam String status,
															   @RequestParam String supplierName,
															   @RequestParam String supplierNumber,
															   @RequestParam String documentType,
															   //@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
															   //@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
															   @RequestParam String roleType,
															   @RequestParam int periodMonth,
															   @RequestParam int periodYear,
															   HttpServletRequest request){	
    	
    	try{
			List<OutSourcingDocument> list=null;
			int total=0;
			List<String> docTypeList = new ArrayList<String>();
			
			if(!"".equals(roleType)) {
				List<UDC> udcList = udcService.searchBySystem("OSDOCUMENT");
				if(udcList != null) {
					for(UDC udc : udcList) {
						if(roleType.equals(udc.getStrValue2())) {
							docTypeList.add(udc.getUdcKey());
						}
					}
				}				
			}
			
			int monthLoad = 0;
			int yearLoad = 0;		
			monthLoad = periodMonth;
			yearLoad = periodYear;
			
			list = outSourcingService.searchDocsByQuery(supplierName, status, documentType, supplierNumber, docTypeList, start, limit, monthLoad, yearLoad);
			if(list != null) {
				total = outSourcingService.searchDocsByQueryCount(supplierName, status, documentType, supplierNumber, docTypeList, monthLoad, yearLoad);
			}
			return mapOKList(list, total);
		    
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
   	
    }
    
    @RequestMapping(value ="/supplier/openOSDocument.action", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, 
    			             @RequestParam int id) throws IOException {
     
    	OutSourcingDocument doc = outSourcingService.getDocumentById(id);
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
    
    @RequestMapping(value = "/uploadOutSoucingSupplierFiles.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
   	public String uploadOutSoucingSupplierFiles(Model model, @ModelAttribute MultiFileUploadBean multiFileUploadBean, 
   				    										  String addressNumber,
   												    		  HttpServletResponse response){
   		response.setContentType("text/html");
   		response.setCharacterEncoding("UTF-8");
   		
   		List<MultipartFile> uploadedFiles = multiFileUploadBean.getUploadedFiles();
   		
   		try {
   			if(uploadedFiles.size() > 1) {
   				
   				Supplier s = supplierService.searchByAddressNumber(addressNumber);
   				
   				UserDocument doc = new UserDocument();
   				doc.setAddressBook(addressNumber);
   		    	doc.setDocumentNumber(0);
   		    	doc.setDocumentType("OUTSRC");
   		    	doc.setContent(uploadedFiles.get(0).getBytes());
   		    	doc.setType(uploadedFiles.get(0).getContentType());
   		    	doc.setName("STPS_OUTSRC_" + uploadedFiles.get(0).getOriginalFilename());
   		    	doc.setSize(uploadedFiles.get(0).getSize());
   		    	doc.setStatus(true);
   		    	doc.setAccept(true);
   		    	doc.setFiscalType("Otros");
   		    	doc.setFolio("");
   		    	doc.setSerie("");
   		    	doc.setUuid("");
   		    	doc.setUploadDate(new Date());
   		    	doc.setFiscalRef(0);
   		    	documentsService.save(doc, new Date(), "");
   		    	
   		    	doc = new UserDocument();
   				doc.setAddressBook(addressNumber);
   		    	doc.setDocumentNumber(0);
   		    	doc.setDocumentType("OUTSRC");
   		    	doc.setContent(uploadedFiles.get(1).getBytes());
   		    	doc.setType(uploadedFiles.get(1).getContentType());
   		    	doc.setName("IMSS_OUTSRC_" + uploadedFiles.get(1).getOriginalFilename());
   		    	doc.setSize(uploadedFiles.get(1).getSize());
   		    	doc.setStatus(true);
   		    	doc.setAccept(true);
   		    	doc.setFiscalType("Otros");
   		    	doc.setFolio("");
   		    	doc.setSerie("");
   		    	doc.setUuid("");
   		    	doc.setUploadDate(new Date());
   		    	doc.setFiscalRef(0);
   		    	documentsService.save(doc, new Date(), "");
   			
   		    	UDC udc = udcService.searchBySystemAndKey("OUTSOURCING", "EMAIL");
   		    	if(udc != null){
   		    		String outSrcApprovalEmail = udc.getStrValue1();
   		    		String msg = AppConstants.OUTSOURCING_APPROVAL_MESSAGE;
   		    		msg = msg.replace("_SUPPLIER_", s.getRazonSocial());
   		    		msg = msg.replace("_LINK_", AppConstants.EMAIL_PORTAL_LINK_PUBLIC + "/approveOutsourcingRequest?ab=" + s.getAddresNumber() + "&token=" + com.eurest.supplier.util.StringUtils.randomString(12));
   			    	emailService.sendEmailAttachMultiPart(AppConstants.OUTSOURCING_APPROVAL_SUBJECT.replace("_SUPPLIER_", s.getRazonSocial()), msg, outSrcApprovalEmail, multiFileUploadBean, 2);
   		    	}

   		    	model.addAttribute("message", "_SUCCESS");
   				model.addAttribute("userName", s.getRazonSocial());
   				model.addAttribute("name", s.getRazonSocial());
   			}
   	    	
   		}catch(Exception e) {
   			log4j.error("Exception" , e);
   			e.printStackTrace();
   			model.addAttribute("message", "_ERROR");
   		}
   		
   		return "outsourcingFiles";
    }
    
	@RequestMapping(value = "/uploadOutSourcingInvoiceDocuments.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String uploadOutSourcingInvoiceDocuments(@ModelAttribute MultiFileUploadBean multiFileUploadBean, 
    										  BindingResult result, 
    										  String orderCompany,
    										  String addressBook,
											  int documentNumber, 
											  String documentType,
											  String tipoComprobante,
											  String receiptIdList,
								    		  HttpServletResponse response){
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	JSONObject json = new JSONObject();
    	
    	try {
	    	List<MultipartFile> uploadedFiles = multiFileUploadBean.getUploadedFiles();

    		String ctXml = uploadedFiles.get(0).getContentType();
    		if(!"text/xml".equals(ctXml)) {
            	json.put("success", false);
            	json.put("message", "El archivo .xml no es válido");
            	return json.toString();
            }
	    	
    		String ctPdf = uploadedFiles.get(1).getContentType();
    		if(!"application/pdf".equals(ctPdf)) {
            	json.put("success", false);
            	json.put("message", "El archivo .pdf no es válido");
            	return json.toString();
            }
    		
    		String ctZip = uploadedFiles.get(2).getContentType();
    		if(!"application/x-zip-compressed".equals(ctZip) && !"application/zip".equals(ctZip)) {
            	json.put("success", false);
            	json.put("message", "El archivo .zip no es válido: " + ctZip);
            	return json.toString();
            }
    		
    		ByteArrayInputStream stream = new  ByteArrayInputStream(uploadedFiles.get(0).getBytes());
			String xmlContent = IOUtils.toString(stream, "UTF-8");
            String source = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
			String xmlString = source.replace("?<?xml", "<?xml");
			//InvoiceDTO inv =  xmlToPojoService.convert(xmlString);
			InvoiceDTO inv = null;
			
			if(xmlString.contains(AppConstants.NAMESPACE_CFDI_V4)) {
				inv = xmlToPojoService.convertV4(xmlString);
			} else {
				inv = xmlToPojoService.convert(xmlString);
			}
			
			String res = outSourcingService.validateInvoiceFromOrder(inv, addressBook, documentNumber, documentType, 
					                                                 tipoComprobante, receiptIdList, xmlString, 
					                                                 uploadedFiles, orderCompany);
			
			if(!"".equals(res)) {
		    	json.put("success", false);
	            json.put("message", res);
	            return json.toString();
			}
			

	    	json.put("success", true);
            json.put("message", "Success");
            
    	} catch (Exception e) {
    		log4j.error("Exception" , e);
			e.printStackTrace();
            json.put("success", false);
            json.put("message", "Error_1");
		}
        return json.toString();
    	
    }
    
	
	public Map<String, Object> mapOK(OutSourcingDocument obj) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", 1);
		modelMap.put("data", obj);
		modelMap.put("success", true);
		return modelMap;
	}

	
	public Map<String, Object> mapOKList(List<OutSourcingDocument> list, int total) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
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

