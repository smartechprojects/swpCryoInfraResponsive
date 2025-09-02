package com.eurest.supplier.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tomcat.jni.User;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.XMLUnit;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eurest.supplier.async.ProcessBatchInvoice;
import com.eurest.supplier.dao.DocumentsDao;
import com.eurest.supplier.dao.FiscalDocumentDao;
import com.eurest.supplier.dao.InvoiceSATDao;
import com.eurest.supplier.dao.PurchaseOrderDao;
import com.eurest.supplier.dao.SupplierDao;
import com.eurest.supplier.dao.TaxVaultDocumentDao;
import com.eurest.supplier.dao.UDCDao;
import com.eurest.supplier.dto.ForeingInvoice;
import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.dto.InvoiceRequestDTO;
import com.eurest.supplier.dto.ResponseGeneral;
import com.eurest.supplier.dto.ZipElementDTO;
import com.eurest.supplier.invoiceXml.Concepto;
import com.eurest.supplier.invoiceXml.DoctoRelacionado;
import com.eurest.supplier.invoiceXml.Impuestos;
import com.eurest.supplier.invoiceXml.Pago;
import com.eurest.supplier.invoiceXml.Retencion;
import com.eurest.supplier.invoiceXml.Retenciones;
import com.eurest.supplier.invoiceXml.Traslado;
import com.eurest.supplier.invoiceXml.Traslados;
import com.eurest.supplier.model.DataAudit;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.ForeignInvoiceTable;
import com.eurest.supplier.model.InvoiceSAT;
import com.eurest.supplier.model.NonComplianceSupplier;
import com.eurest.supplier.model.PaymentCalendar;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.PurchaseOrderDetail;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.TaxVaultDocument;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.BASE64DecodedMultipartFile;
import com.eurest.supplier.util.FileConceptUploadBean;
import com.eurest.supplier.util.FileUploadBean;
import com.eurest.supplier.util.Logger;
import com.eurest.supplier.util.NullValidator;
import com.eurest.supplier.util.PayloadProducer;
import com.eurest.supplier.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



@Service("documentsService")
public class DocumentsService {
	
	 @Autowired
	 private JavaMailSender mailSenderObj;
	
	@Autowired
	private DocumentsDao documentsDao;

	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	XmlToPojoService xmlToPojoService;
	
	@Autowired
	PurchaseOrderService purchaseOrderService;
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	SupplierService supplierService;
	
	@Autowired
	SupplierDao supplierDao;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	EDIService eDIService;
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	FiscalDocumentService fiscalDocumentService;
	
	@Autowired
	NonComplianceSupplierService nonComplianceSupplierService;
	
	@Autowired
	HTTPRequestService HTTPRequestService;
	
	@Autowired
	StringUtils stringUtils;
	
	@Autowired
	UDCDao udcDao;
	
	@Autowired
	PaymentCalendarService paymentCalendarService;
	
	@Autowired
	Logger logger;
	
	@Autowired
	OutSourcingService outSourcingService;
	
	@Autowired
	FiscalDocumentDao fiscalDocumentDao; 
	
	@Autowired
	TaxVaultDocumentDao taxVaultDocumentDao;
	
	@Autowired
	InvoiceSATDao invoiceSATDao;
	
	@Autowired
	FiscalDocumentConceptService fiscalDocumentConceptService;
	
	@Autowired
	MassiveDownloadService massiveDownloadService;
	
	@Autowired
	DataAuditService dataAuditService;
	
	static String TIMESTAMP_DATE_PATTERN = "yyyy-MM-dd";
	
	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(DocumentsService.class);
	
	public List<UserDocument> getDocumentsList(int start, int limit) {
		return documentsDao.getDocumentsList(start, limit);
	}
	
	public List<UserDocument> searchCriteria(String query){
		return documentsDao.searchCriteria(query);
	}
		
	public List<UserDocument> searchByAddressNumber(String query){
		return documentsDao.searchByAddressNumber(query);
	}
	
	public void update(UserDocument documents, Date date, String user){
		documentsDao.updateDocuments(documents);
	}
	
	public void delete(int id, String usr){
		
		UserDocument doc = documentsDao.getDocumentById(id);
		if(doc != null) {
			logger.log(AppConstants.LOG_DELETE_DOC, AppConstants.LOG_DELDOC_MSG.replace("ORDER_NUMBER", String.valueOf(doc.getDocumentNumber() + "-" + doc.getDocumentType())).replace("DOC_NAME", doc.getName()).replace("USER_NAME", usr));
			documentsDao.deleteDocuments(id);
		}
	}
	
	public int getTotalRecords(){
		return documentsDao.getTotalRecords();
	}

	public UserDocument getDocumentById(int id) {
		return documentsDao.getDocumentById(id);
	}
	
	public List<UserDocument> searchCriteriaByOrderNumber(int orderNumber, 
												          String orderType, 
												          String addressNumber,boolean includeContent){ 
			return documentsDao.searchCriteriaByOrderNumber(orderNumber, 
					                                        orderType, 
					                                        addressNumber, includeContent); 

	}
	
	public List<UserDocument> searchCriteriaByOrderNumber(int orderNumber, 
	          String orderType, 
	          String addressNumber,int fiscalDocument,boolean includeContent){ 
return documentsDao.searchCriteriaByOrderNumber(orderNumber, 
              orderType, 
              addressNumber,fiscalDocument, includeContent); 

}
	
	
	public List<UserDocument> searchCriteriaByOrderNumber(int orderNumber,boolean includeContent){ 
return documentsDao.searchCriteriaByOrderNumber(orderNumber, includeContent); 

}

	public List<UserDocument> searchCriteriaByType( String orderType){
		return documentsDao.searchCriteriaByType(orderType);
	}
	
	public List<UserDocument> searchCriteriaByRefFiscal(String addresNumber, String uuid){
		return documentsDao.searchCriteriaByRefFiscal(addresNumber, uuid);
	}

	public List<UserDocument> searchCriteriaByDescription(String addressNumber, String description, boolean includeContent){
		return documentsDao.searchCriteriaByDescription(addressNumber, description,includeContent);
	}
	
	public List<UserDocument> getListPendingReplication(int start, int limit){
		return documentsDao.getListPendingReplication(start, limit);
	}
	
	public void save(UserDocument obj, Date date, String usr) {
		documentsDao.saveDocuments(obj);		
	}

	public void save(FileUploadBean uploadItem, Date date, String usr,
			int docNumber, String docType, String addressNumber) {
		UserDocument doc = new UserDocument();
		if("loadRfcDoc".equals(docType)) {
			doc.setName("*1* " +uploadItem.getFile().getOriginalFilename());
		}else if("loadDomDoc".equals(docType)) {
			doc.setName("*2* " +uploadItem.getFile().getOriginalFilename());
		}else if("loadEdoDoc".equals(docType)) {
			doc.setName("*3* " +uploadItem.getFile().getOriginalFilename());
		}else if("loadIdentDoc".equals(docType)) {
			doc.setName("*4* " +uploadItem.getFile().getOriginalFilename());
		}else if("loadActaConst".equals(docType)) {
			doc.setName("*5* " +uploadItem.getFile().getOriginalFilename());
		}else {
			doc.setName(uploadItem.getFile().getOriginalFilename());
		}
		
		doc.setType(uploadItem.getFile().getContentType());
		doc.setAccept(true);
		doc.setDescription(addressNumber.replace("NEW_", ""));
		doc.setFolio("");
		doc.setSerie("");
		doc.setFiscalRef(0);
		doc.setUuid("");
		doc.setUploadDate(new Date());
		doc.setDocumentNumber(Integer.valueOf(docNumber));
		doc.setDocumentType(docType);
		doc.setAddressBook(addressNumber);
		doc.setSize(uploadItem.getFile().getSize());
		doc.setContent(uploadItem.getFile().getBytes());
		UserDocument d = documentsDao.saveDocuments(doc);// TODO Auto-generated method stub
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		DataAudit dataAudit = new DataAudit();
		dataAudit.setAction("UploadSupplierDocument");
		dataAudit.setAddressNumber(addressNumber);
		dataAudit.setCreationDate(new Date());
		dataAudit.setDocumentNumber(null);
		dataAudit.setIp(request.getRemoteAddr());
		dataAudit.setMethod("upload.action");
		dataAudit.setModule(AppConstants.SUPPLIER_MODULE);    	
		dataAudit.setOrderNumber(null);
		dataAudit.setUuid(null);
		dataAudit.setStep("");
		dataAudit.setMessage("Upload Supplier Document Successful - RFC: " + addressNumber + " - File Name: " + uploadItem.getFile().getOriginalFilename());
		dataAudit.setNotes("");
		dataAudit.setStatus("COMPLETE");
		dataAudit.setUser(usr);
		
		dataAuditService.save(dataAudit);
		
		if(!addressNumber.contains("NEW")) {
			Supplier s = supplierService.searchByAddressNumber(addressNumber);
			String fileList =  s.getFileList();
			fileList = fileList + "_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate();
			s.setFileList(fileList);
			supplierDao.updateSupplier(s);
		}
	}
	
	@SuppressWarnings("unused")
	public void save(FileConceptUploadBean uploadItems, String addressNumber, String uuid) {
		String docType = null;
		
		CommonsMultipartFile item = null;
		for(int i=1; i < 53; i++) {
			item = null;
			docType = "";

			switch (i) {
			case 1:
				item = uploadItems.getFileConcept1_1();
				docType = AppConstants.CONCEPT_001;
				break;
			case 2:
				item = uploadItems.getFileConcept1_2();
				docType = AppConstants.CONCEPT_001;
				break;
			case 3:
				item = uploadItems.getFileConcept2_1();
				docType = AppConstants.CONCEPT_002;
				break;
			case 4:
				item = uploadItems.getFileConcept2_2();
				docType = AppConstants.CONCEPT_002;
				break;
			case 5:
				item = uploadItems.getFileConcept3_1();
				docType = AppConstants.CONCEPT_003;
				break;
			case 6:
				item = uploadItems.getFileConcept3_2();
				docType = AppConstants.CONCEPT_003;
				break;
			case 7:
				item = uploadItems.getFileConcept4_1();
				docType = AppConstants.CONCEPT_004;
				break;
			case 8:
				item = uploadItems.getFileConcept4_2();
				docType = AppConstants.CONCEPT_004;
				break;
			case 9:				
				item = uploadItems.getFileConcept5_1();
				docType = AppConstants.CONCEPT_005;
				break;
			case 10:				
				item = uploadItems.getFileConcept5_2();
				docType = AppConstants.CONCEPT_005;
				break;
			case 11:
				item = uploadItems.getFileConcept6_1();
				docType = AppConstants.CONCEPT_006;
				break;
			case 12:
				item = uploadItems.getFileConcept6_2();
				docType = AppConstants.CONCEPT_006;
				break;
			case 13:
				item = uploadItems.getFileConcept7_1();
				docType = AppConstants.CONCEPT_007;
				break;
			case 14:
				item = uploadItems.getFileConcept7_2();
				docType = AppConstants.CONCEPT_007;
				break;
			case 15:
				item = uploadItems.getFileConcept8_1();
				docType = AppConstants.CONCEPT_008;
				break;
			case 16:
				item = uploadItems.getFileConcept8_2();
				docType = AppConstants.CONCEPT_008;
				break;
			case 17:
				item = uploadItems.getFileConcept9_1();
				docType = AppConstants.CONCEPT_009;
				break;
			case 18:
				item = uploadItems.getFileConcept9_2();
				docType = AppConstants.CONCEPT_009;
				break;
			case 19:
				item = uploadItems.getFileConcept10_1();
				docType = AppConstants.CONCEPT_010;
				break;
			case 20:
				item = uploadItems.getFileConcept10_2();
				docType = AppConstants.CONCEPT_010;
				break;
			case 21:
				item = uploadItems.getFileConcept11_1();
				docType = AppConstants.CONCEPT_011;
				break;
			case 22:				
				item = uploadItems.getFileConcept11_2();
				docType = AppConstants.CONCEPT_011;
				break;
			case 23:				
				item = uploadItems.getFileConcept12_1();
				docType = AppConstants.CONCEPT_012;
				break;
			case 24:
				item = uploadItems.getFileConcept12_2();
				docType = AppConstants.CONCEPT_012;
				break;
			case 25:
				item = uploadItems.getFileConcept13_1();
				docType = AppConstants.CONCEPT_013;
				break;
			case 26:
				item = uploadItems.getFileConcept13_2();
				docType = AppConstants.CONCEPT_013;
				break;
			case 27:
				item = uploadItems.getFileConcept14_1();
				docType = AppConstants.CONCEPT_014;
				break;
			case 28:
				item = uploadItems.getFileConcept14_2();
				docType = AppConstants.CONCEPT_014;
				break;
			case 29:				
				item = uploadItems.getFileConcept15_1();
				docType = AppConstants.CONCEPT_015;
				break;
			case 30:				
				item = uploadItems.getFileConcept15_2();
				docType = AppConstants.CONCEPT_015;
				break;
			case 31:
				item = uploadItems.getFileConcept16_1();
				docType = AppConstants.CONCEPT_016;
				break;
			case 32:
				item = uploadItems.getFileConcept16_2();
				docType = AppConstants.CONCEPT_016;
				break;
			case 33:
				item = uploadItems.getFileConcept17_1();
				docType = AppConstants.CONCEPT_017;
				break;
			case 34:
				item = uploadItems.getFileConcept17_2();
				docType = AppConstants.CONCEPT_017;
				break;
			case 35:
				item = uploadItems.getFileConcept18_1();
				docType = AppConstants.CONCEPT_018;
				break;
			case 36:
				item = uploadItems.getFileConcept18_2();
				docType = AppConstants.CONCEPT_018;
				break;
			case 37:
				item = uploadItems.getFileConcept19_1();
				docType = AppConstants.CONCEPT_019;
				break;
			case 38:
				item = uploadItems.getFileConcept19_2();
				docType = AppConstants.CONCEPT_019;
				break;
			case 39:
				item = uploadItems.getFileConcept20_1();
				docType = AppConstants.CONCEPT_020;
				break;
			case 40:
				item = uploadItems.getFileConcept20_2();
				docType = AppConstants.CONCEPT_020;
				break;
			case 41:
				item = uploadItems.getFileConcept21_1();
				docType = AppConstants.CONCEPT_021;
				break;
			case 42:				
				item = uploadItems.getFileConcept21_2();
				docType = AppConstants.CONCEPT_021;
				break;
			case 43:				
				item = uploadItems.getFileConcept22_1();
				docType = AppConstants.CONCEPT_022;
				break;
			case 44:
				item = uploadItems.getFileConcept22_2();
				docType = AppConstants.CONCEPT_022;
				break;
			case 45:
				item = uploadItems.getFileConcept23_1();
				docType = AppConstants.CONCEPT_023;
				break;
			case 46:
				item = uploadItems.getFileConcept23_2();
				docType = AppConstants.CONCEPT_023;
				break;
			case 47:
				item = uploadItems.getFileConcept24_1();
				docType = AppConstants.CONCEPT_024;
				break;
			case 48:
				item = uploadItems.getFileConcept24_2();
				docType = AppConstants.CONCEPT_024;
				break;
			case 49:				
				item = uploadItems.getFileConcept25_1();
				docType = AppConstants.CONCEPT_025;
				break;
			case 50:				
				item = uploadItems.getFileConcept25_2();
				docType = AppConstants.CONCEPT_025;
				break;
			case 51:
				item = uploadItems.getFileConcept26_1();
				docType = AppConstants.CONCEPT_026;
				break;
			case 52:
				item = uploadItems.getFileConcept26_2();
				docType = AppConstants.CONCEPT_026;
				break;
			default:
				break;
			}
						
			if(item != null && item.getSize() > 0) {
				
				String folio = "";
				String serie = "";
				String conceptUUID = "";
				ResponseGeneral resps = validateInvoiceVsSat(item.getBytes());
				if (resps.isError()) {
					log4j.info(resps.getMensaje().get("es") + "concept");
					return;/// devuelve solo el mensaje en español--en para ingles
				} else {
					if (resps.getDocument() != null) {
						CommonsMultipartFile bade = new CommonsMultipartFile(new BASE64DecodedMultipartFile(
								item.getFileItem(), resps.getDocument()));
						item=bade;
					}
				}
				if(item.getContentType() != null && "text/xml".equals(item.getContentType().trim())) {
					InvoiceDTO inv = null;
					inv = this.getInvoiceXmlFromBytes(item.getBytes());
					
/////////////////////validacion de XML CRUZG
	
					
					if(inv != null) {
						log4j.info("****** Folio:" + inv.getFolio());
						if(inv.getFolio() != null && !"null".equals(inv.getFolio()) && !"NULL".equals(inv.getFolio()) ) {
							folio = inv.getFolio();
						}
						
						log4j.info("****** Serie:" + inv.getSerie());
						if(inv.getSerie() != null && !"null".equals(inv.getSerie()) && !"NULL".equals(inv.getSerie()) ) {
							serie = inv.getSerie();
						}
						
						if(inv.getUuid() != null && !"null".equals(inv.getUuid()) && !"NULL".equals(inv.getUuid()) ) {
							conceptUUID = inv.getUuid();
						}
						
						//Si la factura no tiene folio, se asignan los últimos 4 caracteres del UUID
						if("".equals(folio) && inv.getUuid() != null && inv.getUuid().length() >= 4) {
							serie = "ZX";
							folio = inv.getUuid().substring(inv.getUuid().length() - 4);							
						}
						
						String vinv = "";
						vinv = serie + folio;
						
						//Si el vinv tiene mas de 25 caracteres, se asignan los últimos 12 caracteres del UUID
						if(vinv.length() > 25 && inv.getUuid() != null && inv.getUuid().length() >= 12) {
							vinv = inv.getUuid().substring(inv.getUuid().length() - 12).replaceAll("[^a-zA-Z0-9]", "");
						}
						//JSC: Si el vinv tiene mas de 25 caracteres, truncar a 25 (Validación por nueva versión de BD en JDE)
						if(vinv.length() > 25) {
							vinv = vinv.substring(0, 25);
						}
						log4j.info("****** Vinv:" + vinv);
						
					}
				}
				
				UserDocument doc = new UserDocument();
	    		doc.setAddressBook(addressNumber);
	    		doc.setDocumentNumber(Integer.valueOf(0));
	        	doc.setDocumentType(docType);
	        	doc.setContent(item.getBytes());
	        	doc.setType(item.getContentType());
	        	doc.setName(item.getOriginalFilename());
	        	doc.setSize(item.getSize());
	        	doc.setStatus(true);
	        	doc.setAccept(true);
	        	doc.setFiscalType("Otros");
	        	doc.setFolio(folio);
	        	doc.setSerie(serie);
	        	doc.setUuid(conceptUUID);
	        	doc.setUploadDate(new Date());
	        	doc.setFiscalRef(0);
	        	doc.setDescription("MainUUID_".concat(uuid));
				UserDocument d = documentsDao.saveDocuments(doc);
			}
		}
	}
	
	@SuppressWarnings("unused")
	public JSONObject processExcelFile(FileUploadBean uploadItem) {
		JSONObject json = new JSONObject();
		Workbook workbook = null;
		Sheet sheet = null;
		List<Supplier> suppliers = new ArrayList<Supplier>();
		List<Users> users = new ArrayList<Users>();
		List<DataAudit> dataAuditList = new ArrayList<DataAudit>();
		int count = 0;
		UDC userRole = udcService.searchBySystemAndKey("ROLES", "SUPPLIER");
		UDC userType = udcService.searchBySystemAndKey("USERTYPE", "SUPPLIER");
		//String encodePass =  AppConstants.START_PASS_ENCODED;
		Date currentDate = new Date();
	  	
	  	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String userCurrent = auth.getName();
		try {
		    workbook = WorkbookFactory.create(uploadItem.getFile().getInputStream());
			sheet = workbook.getSheet("WebPortal_Suppliers");
			Iterator<Row> rowIterator = sheet.iterator();
			int rowNumber = 1;
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				//log4j.info(count);
				
				if(row.getRowNum() > 0) {
					Supplier sup = new Supplier();
					Users usr = new Users();
					DataAudit dataAudit = new DataAudit();
					sup.setId(0);
					
					try {
						if(row.getCell(0)!= null) {
							int valueAN = row.getCell(0).getCellType();
							 
							if(valueAN == 0) {
								int addNum = (int) row.getCell(0).getNumericCellValue();
								addNum = (addNum*100)/100;
								sup.setAddresNumber(String.valueOf(addNum));
							}else {
								if("".equals(row.getCell(0).getStringCellValue())) {
									continue;
								}
								int addNum = Integer.valueOf(row.getCell(0).getStringCellValue());
								addNum = (addNum*100)/100;
								sup.setAddresNumber(String.valueOf(addNum));
							}
							
							Supplier o = supplierService.searchByAddressNumber(sup.getAddresNumber());
							
							if(o != null) {
								if(o.getAddresNumber().equals("0") || o.getAddresNumber().equals("")) {
									json.put("success", false);
									json.put("message_es", "Address Number 0 o campo vacio no es valido");
									json.put("message_en", "Address Number 0 or empty field is not valid");
									json.put("count", 0);
									return json;
								}else {
									json.put("success", false);
									json.put("message_es", "Address Number existente en el portal: "+ o.getAddresNumber());
									json.put("message_en", "Address Number existing in the portal: "+ o.getAddresNumber());
									json.put("count", 0);
									return json;
								}
							}
						}else {
							continue;
						}
						
						if(row.getCell(1)!= null) {
							sup.setName(row.getCell(1).getStringCellValue());
							if(row.getCell(1).getStringCellValue().length() > 40) {
								sup.setRazonSocial(row.getCell(1).getStringCellValue().substring(0, 40));
							} else {
								sup.setRazonSocial(row.getCell(1).getStringCellValue());
							}
						}
						if(row.getCell(10)!= null) {
							sup.setCountry(row.getCell(10).getStringCellValue());
							if(sup.getCountry().length()>2) {
								json.put("success", false);
								json.put("message_es", "PAIS excede el limite de 2 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "COUNTRY exceeds the limit of 2 positions.\n\n<br><br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						if(row.getCell(2)!= null) {
							int fisicaM = row.getCell(2).getCellType();
							if(fisicaM == 0) {
								if("MX".equals(sup.getCountry())) {
									sup.setRfc(String.valueOf(row.getCell(2).getNumericCellValue()));
								}else sup.setTaxId(String.valueOf(row.getCell(2).getNumericCellValue()));
								
							}else {
								if("MX".equals(sup.getCountry())) {
									sup.setRfc(row.getCell(2).getStringCellValue());
								}else sup.setTaxId(row.getCell(2).getStringCellValue());
								//sup.setRfc(row.getCell(2).getStringCellValue());
							}
							
							if("MX".equals(sup.getCountry())) {
								if(sup.getRfc().length()>20) {
									json.put("success", false);
									json.put("message_es", "RFC/TAX ID excede el limite de 20 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
									json.put("message_en", "RFC/TAX ID exceeds the limit of 20 positions.<br>Supplier:"+ sup.getAddresNumber());
									json.put("count", 0);
									return json;
								}
							}else {
								if(sup.getTaxId().length()>20) {
									json.put("success", false);
									json.put("message_es", "RFC/TAX ID excede el limite de 20 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
									json.put("message_en", "RFC/TAX ID exceeds the limit of 20 positions.<br>Supplier:"+ sup.getAddresNumber());
									json.put("count", 0);
									return json;
								}
							}
							
						}
						//sup.setRfc(row.getCell(2).getStringCellValue());
						if(row.getCell(3)!= null) {
							int fisicaM = row.getCell(3).getCellType();
							if(fisicaM == 0) {
								int cP  = (int) row.getCell(3).getNumericCellValue();
								cP = (cP*100)/100;
								sup.setFisicaMoral(String.valueOf(cP));
							}else {
								int addNum = Integer.valueOf(row.getCell(3).getStringCellValue());
								addNum = (addNum*100)/100;
								sup.setFisicaMoral(String.valueOf(addNum));
							}
							
							if(sup.getFisicaMoral().length()>1) {
								json.put("success", false);
								json.put("message_es", "TIPO_CONTRIBUYENTE excede el limite de 1 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "TAXPAYER_TYPE exceeds the limit of 1 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						
						if(row.getCell(4)!= null) {
							sup.setEmailSupplier(row.getCell(4).getStringCellValue());
							if(sup.getEmailSupplier().length()>254) {
								json.put("success", false);
								json.put("message_es", "EMAIL_SUPPLIER excede el limite de 254 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "EMAIL_SUPPLIER exceeds the limit of 254 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						if(row.getCell(5)!= null) {
							sup.setCalleNumero(row.getCell(5).getStringCellValue());
							if(sup.getCalleNumero().length()>40) {
								json.put("success", false);
								json.put("message_es", "CALLE Y NUMERO excede el limite de 40 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "STREET AND NUMBER exceeds the limit of 40 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						if(row.getCell(6)!= null) {
							sup.setColonia(row.getCell(6).getStringCellValue());
							if(sup.getColonia().length()>40) {
								json.put("success", false);
								json.put("message_es", "COLONIA excede el limite de 40 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "SUBURB exceeds the limit of 40 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						if(row.getCell(7)!= null) {
							int codP = row.getCell(7).getCellType();
							if(codP == 0) {
								int cP  = (int) row.getCell(7).getNumericCellValue();
								cP = (cP*100)/100;
								sup.setCodigoPostal(String.valueOf(cP));
							}else {
								int addNum = Integer.valueOf(row.getCell(7).getStringCellValue());
								addNum = (addNum*100)/100;
								sup.setCodigoPostal(String.valueOf(addNum));
							}
							
							if(sup.getCodigoPostal().length()>12) {
								json.put("success", false);
								json.put("message_es", "CODIGO_POSTAL excede el limite de 12 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "POSTAL CODE exceeds the limit of 12 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						
						if(row.getCell(8)!= null)
						sup.setDelegacionMnicipio(row.getCell(8).getStringCellValue());
						if(row.getCell(9)!= null) {
							sup.setEstado(row.getCell(9).getStringCellValue());
							if(sup.getEstado().length()>3) {
								json.put("success", false);
								json.put("message_es", "ESTADO excede el limite de 3 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "STATE exceeds the limit of 3 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						if(row.getCell(11)!= null) {
							int telDF = row.getCell(11).getCellType();
							if(telDF == 0) {
								sup.setTelefonoDF(String.valueOf(row.getCell(11).getNumericCellValue()));
							}else {
								sup.setTelefonoDF(row.getCell(11).getStringCellValue());
							}
							
							if(sup.getTelefonoDF().length()>20) {
								json.put("success", false);
								json.put("message_es", "TELEFONO_DIREC_FIS excede el limite de 20 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "PHONE_ADDRESS_FIS exceeds the limit of 20 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						
						/*if(row.getCell(12)!= null) {
							int faxDF = row.getCell(12).getCellType();
							if(faxDF == 0) {
								sup.setFaxDF(String.valueOf(row.getCell(12).getNumericCellValue()));
							}else {
								sup.setFaxDF(row.getCell(12).getStringCellValue());
							}
						}*/
							
						if(row.getCell(12)!= null)
						sup.setEmailComprador(row.getCell(12).getStringCellValue());
						if(row.getCell(13)!= null)
						sup.setNombreContactoCxC(row.getCell(13).getStringCellValue());
						if(row.getCell(14)!= null)
						sup.setApellidoPaternoCxC(row.getCell(14).getStringCellValue());
						if(row.getCell(15)!= null)
						sup.setApellidoMaternoCxC(row.getCell(15).getStringCellValue());
						if(row.getCell(16)!= null) {
							int telCxC = row.getCell(16).getCellType();
							if(telCxC == 0) {
								sup.setTelefonoContactoCxC(String.valueOf(row.getCell(16).getNumericCellValue()));
							}else {
								sup.setTelefonoContactoCxC(row.getCell(16).getStringCellValue());
							}
						}
						
						/*if(row.getCell(18)!= null) {
							int faxCxC = row.getCell(18).getCellType();
							if(faxCxC == 0) {
								sup.setFaxCxC(String.valueOf(row.getCell(18).getNumericCellValue()));
							}else {
								sup.setFaxCxC(row.getCell(18).getStringCellValue());
							}
						}*/
						if(row.getCell(17)!= null)
						sup.setCargoCxC(row.getCell(17).getStringCellValue());	
						if(row.getCell(18)!= null)
						sup.setNombreCxP01(row.getCell(18).getStringCellValue());
						if(row.getCell(19)!= null)
						sup.setEmailCxP01(row.getCell(19).getStringCellValue());
						if(row.getCell(20)!= null) {
							int texCxP1 = row.getCell(20).getCellType();
							if(texCxP1 == 0) {
								sup.setTelefonoCxP01(String.valueOf(row.getCell(20).getNumericCellValue()));
							}else {
								sup.setTelefonoCxP01(row.getCell(20).getStringCellValue());
							}
						}
						if(row.getCell(21)!= null)
						sup.setNombreCxP02(row.getCell(21).getStringCellValue());
						if(row.getCell(22)!= null)
						sup.setEmailCxP02(row.getCell(22).getStringCellValue());
						if(row.getCell(23)!= null) {
							int texCxP2 = row.getCell(23).getCellType();
							if(texCxP2 == 0) {
								sup.setTelefonoCxP02(String.valueOf(row.getCell(23).getNumericCellValue()));
							}else {
								sup.setTelefonoCxP02(row.getCell(23).getStringCellValue());
							}
						}
						/*if(row.getCell(26)!= null) {
							int cat15 = row.getCell(26).getCellType();
							if(cat15 == 0) {
								sup.setCatCode15(String.valueOf(row.getCell(26).getNumericCellValue()));
							}else {
								sup.setCatCode15(row.getCell(26).getStringCellValue());
							}
						}
						if(row.getCell(27)!= null) {
							int indCl = row.getCell(27).getCellType();
							if(indCl == 0) {
								int addNum = (int) row.getCell(27).getNumericCellValue();
								addNum = (addNum*100)/100;
								sup.setIndustryClass(String.valueOf(addNum));
							}else {
								if(!"".equals(row.getCell(27).getStringCellValue())) {
									int addNum = Integer.valueOf(row.getCell(27).getStringCellValue());
									addNum = (addNum*100)/100;
									sup.setIndustryClass(String.valueOf(addNum));
								}else sup.setIndustryClass(String.valueOf(row.getCell(27).getStringCellValue()));
								
							}
						}*/
						
						if(row.getCell(24)!= null) {
							int typeIdent = row.getCell(24).getCellType();
							if(typeIdent == 0) {
								sup.setTipoIdentificacion(String.valueOf(row.getCell(24).getNumericCellValue()));
							}else {
								sup.setTipoIdentificacion(row.getCell(24).getStringCellValue());
							}
						}
						if(row.getCell(25)!= null) {
							int numIdent = row.getCell(25).getCellType();
							if(numIdent == 0) {
								sup.setNumeroIdentificacion(String.valueOf(row.getCell(25).getNumericCellValue()));
							}else {
								sup.setNumeroIdentificacion(row.getCell(25).getStringCellValue());
							}
						}
						if(row.getCell(26)!= null)
						sup.setNombreRL(row.getCell(26).getStringCellValue());
						if(row.getCell(27)!= null)
						sup.setApellidoPaternoRL(row.getCell(27).getStringCellValue());
						if(row.getCell(28)!= null)
						sup.setApellidoMaternoRL(row.getCell(28).getStringCellValue());
						if(row.getCell(29)!= null) {
							sup.setCurrencyCode(row.getCell(29).getStringCellValue());
							if(sup.getCurrencyCode().length()>3) {
								json.put("success", false);
								json.put("message_es", "MONEDA excede el limite de 3 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "CURRENCY exceeds the limit of 3 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						if(row.getCell(30)!= null) {
							int valueAN = row.getCell(0).getCellType();
							 
							if(valueAN == 0) {
								int addNum = (int) row.getCell(0).getNumericCellValue();
								addNum = (addNum*100)/100;
								sup.setIdFiscal(String.valueOf(addNum));
							}else {
								sup.setIdFiscal(row.getCell(30).getStringCellValue());
							}
						}
						if(row.getCell(31)!= null) {
							sup.setSwiftCode(row.getCell(31).getStringCellValue());
							if(sup.getSwiftCode().length()>15) {
								json.put("success", false);
								json.put("message_es", "SWIFT_CODE excede el limite de 15 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "SWIFT_CODE exceeds the limit of 15 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						if(row.getCell(32)!= null) {
							int cheking = row.getCell(32).getCellType();
							if(cheking == 0) {
								sup.setIbanCode(String.valueOf(row.getCell(32).getNumericCellValue()));
							}else {
								sup.setIbanCode(row.getCell(32).getStringCellValue());
							}
							
							if(sup.getIbanCode().length()>34) {
								json.put("success", false);
								json.put("message_es", "IBAN_CODE excede el limite de 34 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "IBAN_CODE exceeds the limit of 34 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						//sup.setIbanCode(row.getCell(35).getStringCellValue());
						if(row.getCell(33)!= null) {
							int cheking = row.getCell(33).getCellType();
							if(cheking == 0) {
								sup.setBankTransitNumber(String.valueOf(row.getCell(33).getNumericCellValue()));
							}else {
								sup.setBankTransitNumber(row.getCell(33).getStringCellValue());
							}
							
							if(sup.getBankTransitNumber().length()>20) {
								json.put("success", false);
								json.put("message_es", "BANK_TRANSIT_NUMBER excede el limite de 20 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "BANK_TRANSIT_NUMBER exceeds the limit of 20 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						//if(row.getCell(37)!= null)
						//sup.setCustBankAcct(row.getCell(37).getStringCellValue());
						//sup.setCustBankAcct(String.valueOf(row.getCell(37).getNumericCellValue()));
							
							if(row.getCell(34)!= null) {
								int cheking = row.getCell(34).getCellType();
								if(cheking == 0) {
									sup.setCustBankAcct(String.valueOf(row.getCell(34).getNumericCellValue()));
								}else {
									sup.setCustBankAcct(row.getCell(34).getStringCellValue());
								}
								
								if(sup.getCustBankAcct().length()>20) {
									json.put("success", false);
									json.put("message_es", "CUST_BANK_ACCT excede el limite de 20 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
									json.put("message_en", "CUST_BANK_ACCT exceeds the limit of 20 positions.<br>Supplier:"+ sup.getAddresNumber());
									json.put("count", 0);
									return json;
								}
							}
						//if(row.getCell(38)!= null)
						//sup.setControlDigit(row.getCell(38).getStringCellValue());
							if(row.getCell(35)!= null) {
								int cheking = row.getCell(35).getCellType();
								if(cheking == 0) {
									sup.setControlDigit(String.valueOf(row.getCell(35).getNumericCellValue()));
								}else {
									sup.setControlDigit(row.getCell(35).getStringCellValue());
								}
								
								if(sup.getControlDigit().length()>2) {
									json.put("success", false);
									json.put("message_es", "CONTROL_DIGIT excede el limite de 2 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
									json.put("message_en", "CONTROL_DIGIT exceeds the limit of 2 positions.<br>Supplier:"+ sup.getAddresNumber());
									json.put("count", 0);
									return json;
								}
							}
						if(row.getCell(36)!= null) {
							sup.setDescription(row.getCell(36).getStringCellValue());
							if(sup.getDescription().length()>30) {
								json.put("success", false);
								json.put("message_es", "DESCRIPCION excede el limite de 30 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "DESCRIPCION exceeds the limit of 30 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						/*if(row.getCell(40)!= null) {
							int cheking = row.getCell(40).getCellType();
							if(cheking == 0) {
								sup.setCheckingOrSavingAccount(String.valueOf(row.getCell(40).getNumericCellValue()));
							}else {
								sup.setCheckingOrSavingAccount(row.getCell(40).getStringCellValue());
							}
						}*/
						if(row.getCell(37)!= null) {
							sup.setRollNumber(row.getCell(37).getStringCellValue());
							if(sup.getRollNumber().length()>18) {
								json.put("success", false);
								json.put("message_es", "ROLL_NUMBER excede el limite de 18 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "ROLL_NUMBER exceeds the limit of 18 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						if(row.getCell(38)!= null) {
							int cheking = row.getCell(38).getCellType();
							if(cheking == 0) {
								sup.setBankAddressNumber(String.valueOf(row.getCell(38).getNumericCellValue()));
							}else {
								sup.setBankAddressNumber(row.getCell(38).getStringCellValue());
							}
							
							if(sup.getBankAddressNumber().length()>8) {
								json.put("success", false);
								json.put("message_es", "BANK_ADDRESS_NUMBER excede el limite de 8 posiciones.<br>Proveedor: "+ sup.getAddresNumber());
								json.put("message_en", "BANK_ADDRESS_NUMBER exceeds the limit of 8 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						if(row.getCell(39)!= null) {
							sup.setBankCountryCode(row.getCell(39).getStringCellValue());
							if(sup.getBankCountryCode().length()>2) {
								json.put("success", false);
								json.put("message_es", "BANK_COUNTRY_CODE excede el limite de 2 posiciones.\nProveedor: "+ sup.getAddresNumber());
								json.put("message_en", "BANK_COUNTRY_CODE exceeds the limit of 2 positions.<br>Supplier:"+ sup.getAddresNumber());
								json.put("count", 0);
								return json;
							}
						}
						
						
						sup.setCurrentApprover("");
						sup.setNextApprover("");
						sup.setApprovalStatus(AppConstants.STATUS_ACCEPT);
						sup.setApprovalStep("");
						sup.setSteps(0);
						sup.setRejectNotes("");
						sup.setApprovalNotes("CARGA MASIVA");
						sup.setFechaSolicitud(null);
						sup.setFechaAprobacion(new Date());
						sup.setRegiones("");				
						sup.setTipoMovimiento("A");					
						sup.setRiesgoCategoria("");
						sup.setObservaciones("");
						sup.setDiasCreditoActual("0");
						sup.setDiasCreditoAnterior("0");
						sup.setAcceptOpenOrder(false);
						sup.setFileList("");
						sup.setSearchType("V");
						sup.setCreditMessage("");
						sup.setTicketId(0l);
						sup.setInvException("N");
						sup.setCatCode15("V");
						/*sup.setTaxAreaCxC("");
						sup.setTaxExpl2CxC("");
						sup.setPmtTrmCxC("N60");
						sup.setPayInstCxC("T");
						sup.setCurrCodeCxC("USD");
						if(sup.getCountry()!= "MX") {
							sup.setGlClass("200");
						}else {
							sup.setGlClass("100");
						}
						if(sup.getFisicaMoral()!="1") {
							sup.setCatCode27("M");
						}else {
							sup.setCatCode27("F");
						}
						if(sup.getCatCode15() == "0" || sup.getCatCode15() == null) sup.setCatCode15("");
						if(sup.getIndustryClass() == "0" || sup.getIndustryClass() == null) sup.setIndustryClass("");*/
						suppliers.add(sup);
						
						usr.setId(0);
						usr.setUserName(sup.getAddresNumber());
						usr.setAddressNumber(sup.getAddresNumber());
		                usr.setSupplier(true);
		                usr.setMainSupplierUser(true);
						usr.setEnabled(true);
						usr.setEmail(sup.getEmailSupplier());
						usr.setName(sup.getRazonSocial());
						usr.setRole(userRole.getStrValue1());
						usr.setUserRole(userRole);
						usr.setUserType(userType);
						
						String tempPass = "CRYOINFRA20";
						String encodePass = Base64.getEncoder().encodeToString(tempPass.trim().getBytes());
						encodePass = "==a20$" + encodePass; 
						
						usr.setPassword(encodePass); 
						users.add(usr);
						
						dataAudit.setAction("UploadSupplierByLayout");
				    	dataAudit.setAddressNumber(sup.getAddresNumber());
				    	dataAudit.setCreationDate(currentDate);
				    	dataAudit.setDocumentNumber(null);
				    	dataAudit.setIp(request.getRemoteAddr());
				    	dataAudit.setMethod("processExcelFile");
				    	dataAudit.setModule(AppConstants.SUPPLIER_MODULE);    	
				    	dataAudit.setOrderNumber(null);
				    	dataAudit.setUuid(null);
				    	dataAudit.setStep(null);
				    	dataAudit.setMessage("Upload Supplier Successful");
				    	dataAudit.setNotes(null);
				    	dataAudit.setStatus(AppConstants.STATUS_COMPLETE);
				    	dataAudit.setUser(userCurrent);
				    	
				    	dataAuditList.add(dataAudit);
						
						String credentials = "Usuario: " + sup.getAddresNumber() + "<br />Contraseña: " + tempPass.trim() + "<br />url: " + AppConstants.EMAIL_PORTAL_LINK ;
						EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						emailAsyncSup.setProperties(AppConstants.EMAIL_INVOICE_SUBJECT, stringUtils.prepareEmailContent(AppConstants.EMAIL_MASS_SUPPLIER_NOTIFICATION + credentials), sup.getEmailSupplier());
						emailAsyncSup.setMailSender(mailSenderObj);
						Thread emailThreadSup = new Thread(emailAsyncSup);
						//emailThreadSup.start();	

						count = count + 1;
					}catch(Exception e) {
						e.printStackTrace();
						
						json.put("success", false);
						json.put("message_es", "NÚMERO DE FILA: " + rowNumber + " - RUNTIME ERROR: " + e.getMessage());
						json.put("message_en", "ROW NUM: " + rowNumber + " - RUNTIME ERROR: " + e.getMessage());
						json.put("count", 0);
						return json;
						
					}
				}
			}
			
			if(!suppliers.isEmpty()) {
				supplierService.saveSuppliers(suppliers);
				usersService.saveUsersList(users);
				dataAuditService.saveDataAudit(dataAuditList);
				for(Users s : users) {
					String emailRecipient = (s.getEmail());

					String pass = s.getPassword();
					pass = pass.replace("==a20$", "");
					byte[] decodedBytes = Base64.getDecoder().decode(pass);
					String decodedPass = new String(decodedBytes);
					
					
					String credentials = "Usuario: " + s.getUserName() + "<br />Contraseña: " + decodedPass + "<br />url: " + AppConstants.EMAIL_PORTAL_LINK ;
					EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
					emailAsyncSup.setProperties(AppConstants.EMAIL_INVOICE_SUBJECT, 
					stringUtils.prepareEmailContent(AppConstants.EMAIL_MASS_SUPPLIER_NOTIFICATION + credentials), emailRecipient);
					emailAsyncSup.setMailSender(mailSenderObj);
					Thread emailThreadSup = new Thread(emailAsyncSup);
					emailThreadSup.start();	
				}
				json.put("success", true);
				json.put("count", count);
				json.put("message", "");
				return json;
			}
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}
		return null;
		
	}
	
	public InvoiceDTO getInvoiceXml(FileUploadBean uploadItem){
		try{
			ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getFile().getBytes());
			String xmlContent = IOUtils.toString(stream, "UTF-8");
			xmlContent = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
			xmlContent = xmlContent.replace("?<?xml", "<?xml");
			
			InvoiceDTO dto = null;
			if(!uploadItem.getFile().getContentType().equals("text/xml")) {
				InvoiceDTO inv=new InvoiceDTO();
				ResponseGeneral responseG=new ResponseGeneral(true, "ValidarArchivoXml");
				responseG.addMensaje("es", "La factura ingresada no es un xml");
				responseG.addMensaje("en", "The invoice entered is not an xml");
				inv.setResponse(responseG);
				return inv;
			}
			
			if(xmlContent.contains(AppConstants.NAMESPACE_CFDI_V4)) {
				dto = xmlToPojoService.convertV4(xmlContent);
			} else {
				dto = xmlToPojoService.convert(xmlContent);
			}
			return dto;
		}catch(Exception e){
			InvoiceDTO inv=new InvoiceDTO();
			ResponseGeneral responseG=new ResponseGeneral(true, "getInvoiceXml");
			responseG.addMensaje("es", "La factura tiene errores de validación. Factura no valida");
			responseG.addMensaje("en", "The invoice has validation errors. Invoice not valid");
			inv.setResponse(responseG);
			log4j.error("Exception" , e);
			e.printStackTrace();
			return inv;
		}
	}
	
	public InvoiceDTO getInvoiceXml(MultipartFile uploadItem){
		try{
			ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getBytes());
			String xmlContent = IOUtils.toString(stream, "UTF-8");
			xmlContent = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
			xmlContent = xmlContent.replace("?<?xml", "<?xml");
			
			InvoiceDTO dto = null;
			if(!uploadItem.getContentType().equals("text/xml")) {
				InvoiceDTO inv=new InvoiceDTO();
				ResponseGeneral responseG=new ResponseGeneral(true, "ValidarArchivoXml");
				responseG.addMensaje("es", "La factura ingresada no es un xml");
				responseG.addMensaje("en", "The invoice entered is not an xml");
				inv.setResponse(responseG);
				return inv;
			}
			
			if(xmlContent.contains(AppConstants.NAMESPACE_CFDI_V4)) {
				dto = xmlToPojoService.convertV4(xmlContent);
			} else {
				dto = xmlToPojoService.convert(xmlContent);
			}
			return dto;
		}catch(Exception e){
			InvoiceDTO inv=new InvoiceDTO();
			ResponseGeneral responseG=new ResponseGeneral(true, "getInvoiceXml");
			responseG.addMensaje("es", "La factura tiene errores de validación. Factura no valida");
			responseG.addMensaje("en", "The invoice has validation errors. Invoice not valid");
			inv.setResponse(responseG);
			log4j.error("Exception" , e);
			e.printStackTrace();
			return inv;
		}
	}
	
	public InvoiceDTO getCreditNoteXml(FileUploadBean uploadItem){
		try{
			ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getFileTwo().getBytes());
			String xmlContent = IOUtils.toString(stream, "UTF-8");
			xmlContent = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
			xmlContent = xmlContent.replace("?<?xml", "<?xml");
			InvoiceDTO dto = null;
			
			if(xmlContent.contains(AppConstants.NAMESPACE_CFDI_V4)) {
				dto = xmlToPojoService.convertV4(xmlContent);
			} else {
				dto = xmlToPojoService.convert(xmlContent);
			}
			return dto;
		}catch(Exception e){
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}
	}
	
	public InvoiceDTO getInvoiceXmlFromString(String xmlContent){
		try{
			InvoiceDTO dto = null;
			if(xmlContent.contains(AppConstants.NAMESPACE_CFDI_V4)) {
				dto = xmlToPojoService.convertV4(xmlContent);
			} else {
				dto = xmlToPojoService.convert(xmlContent);
			}
			return dto;
		}catch(Exception e){
			log4j.error("Exception" , e);
			return null;
		}
	}
	
	public InvoiceDTO getInvoiceXmlFromBytes(byte[] bytes){
		try{
			ByteArrayInputStream stream = new  ByteArrayInputStream(bytes);
			String xmlContent = IOUtils.toString(stream, "UTF-8");
			xmlContent = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
			xmlContent = xmlContent.replace("?<?xml", "<?xml");
			InvoiceDTO dto = null;
			
			if(xmlContent.contains(AppConstants.NAMESPACE_CFDI_V4)) {
				dto = xmlToPojoService.convertV4(xmlContent);
			} else {
				dto = xmlToPojoService.convert(xmlContent);
			}
			return dto;
		}catch(Exception e){
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}
	}
	
	public String validateInvZipFile(FileUploadBean uploadItem, 
									  BindingResult result, 
									  String addressBook, 
									  int documentNumber, 
									  String documentType,
									  String tipoComprobante,
									  String receiptIdList,
									  String usr) {

		DecimalFormat currencyFormat = new DecimalFormat("$#,###.###");
		Users user = usersService.getByUserName(usr);
		PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(documentNumber, addressBook, documentType);		
		Supplier s = supplierService.searchByAddressNumber(addressBook);
		if(s == null) {
			return "El proveedor no existe en la base de datos.";
		}
		
		List<Receipt> requestedReceiptList = null;
		List<Receipt> receiptArray = null;
		
		if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)) {
			receiptArray= purchaseOrderService.getOrderReceipts(documentNumber, addressBook, documentType, "");
		} else {
			receiptArray= purchaseOrderService.getNegativeOrderReceipts(documentNumber, addressBook, documentType,"");
		}
		
		if(receiptArray != null) {
			String[] idList = receiptIdList.split(",");
			requestedReceiptList = new ArrayList<Receipt>();
			for(Receipt r : receiptArray) {
				if(Arrays.asList(idList).contains(String.valueOf(r.getId()))) {
					requestedReceiptList.add(r);
				}
			}
			
			if(requestedReceiptList.isEmpty()) {
				return "No existen recibos por facturar.";
			}
		}else {
			return "No existen recibos por facturar.";
	    }

		//Valida que se corresponda el mismo recibo para todas las líneas seleccionadas.
		int originalReceipt = 0;
		for(Receipt r :requestedReceiptList) {
			if(originalReceipt == 0) {
				originalReceipt = r.getDocumentNumber();
			} else if (originalReceipt != r.getDocumentNumber()) {
				return "Las líneas seleccionadas deben corresponder al mismo número de recibo.";
			}
		}
		
		//Valida proveedores incumplidos
		if(s != null) {
			NonComplianceSupplier ncs = this.nonComplianceSupplierService.searchByTaxId(s.getRfc(), 0, 0);
		    if (ncs != null && (
		      ncs.getRefDate1().contains("Definitivo") || 
		      ncs.getRefDate1().contains("Presunto") || 
		      ncs.getRefDate1().contains("Desvirtuado") || 
		      ncs.getRefDate2().contains("Definitivo") || 
		      ncs.getRefDate2().contains("Presunto") || 
		      ncs.getRefDate2().contains("Desvirtuado") || 
		      ncs.getStatus().contains("Definitivo") || 
		      ncs.getStatus().contains("Presunto") || 
		      ncs.getStatus().contains("Desvirtuado"))) {
		    	
		    	String altEmail = "";
					List<UDC> udcList =  udcService.searchBySystem("TAXALTEMAIL");
					if(udcList != null) {
						String emailContent = AppConstants.EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER;
						emailContent = emailContent.replace("_ADDNUMBER_", s.getAddresNumber());
						emailContent = emailContent.replace("_RAZONSOCIAL_", s.getRazonSocial());

						
						altEmail = udcList.get(0).getStrValue1();
					    EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						emailAsyncSup.setProperties(
								AppConstants.EMAIL_NO_COMPLIANCE_INVOICE,
								emailContent,
								altEmail);
						emailAsyncSup.setMailSender(mailSenderObj);
						Thread emailThreadSup = new Thread(emailAsyncSup);
						emailThreadSup.start();
					}
		    	
					if(user.getEmail() != null && !"".equals(user.getEmail())) {
						 EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						 emailAsyncSup.setProperties(
									AppConstants.EMAIL_INVOICE_REJECTED + " " + po.getOrderNumber() ,
									AppConstants.EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER_NOTIF  + " PO:" + po.getOrderNumber() + "<br /> <br />" + AppConstants.ETHIC_CONTENT,
									user.getEmail());
						emailAsyncSup.setMailSender(mailSenderObj);
						Thread emailThreadSup = new Thread(emailAsyncSup);
						emailThreadSup.start();	
					}
					
					return "Los registros indican que cuenta con problemas fiscales y no se podrán cargar facturas en este momento.";
		    } 
		}

		String domesticCurrency = AppConstants.DEFAULT_CURRENCY;
		
		List<UDC> comUDCList =  udcService.searchBySystem("COMPANYDOMESTIC");
		if(comUDCList != null && !comUDCList.isEmpty()) {
			for(UDC company : comUDCList) {
				if(company.getStrValue1().trim().equals(po.getOrderCompany().trim()) && !"".equals(company.getStrValue2().trim())) {
					domesticCurrency = company.getStrValue2().trim();
					break;
				}
			}
		}
		
		List<Map<String, ZipElementDTO>> fileList = new ArrayList<Map<String,ZipElementDTO>>();
		List<Map<String, byte[]>> attachedList = new ArrayList<Map<String,byte[]>>();
		Map<String, ZipElementDTO> fileMap;
		Map<String, byte[]> attachedMap;
	    ZipElementDTO elementDTO;
	    InvoiceDTO invoiceDTO;
		
        try {        	
    		ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getFile().getBytes());
    		ZipInputStream zis = new ZipInputStream(stream);
    		ZipEntry ze;
    		
			while ((ze = zis.getNextEntry()) != null) {	    		
				String fileName = FilenameUtils.getBaseName(ze.getName());
				int fileSize = (int)ze.getSize();
				
			    if(FilenameUtils.getExtension(ze.getName()).equals(AppConstants.FILE_EXT_XML)) {
			    	fileMap = new HashMap<String, ZipElementDTO>();
		    		elementDTO = new ZipElementDTO();
		    		invoiceDTO = new InvoiceDTO();
					byte[] byteArrayFile = this.getByteArrayFromZipInputStream(fileName, AppConstants.FILE_EXT_XML, fileSize, zis);
					///////////////////// validacion de XML CRUZG
					ResponseGeneral resps = validateInvoiceVsSat(byteArrayFile);
					if (resps.isError()) {
						return resps.getMensaje().get("es") + "," + ze.getName();/// devuelve solo el mensaje en
																					/// español--en para ingles
					} else {
						if (resps.getDocument() != null) {
							CommonsMultipartFile bade = new CommonsMultipartFile(new BASE64DecodedMultipartFile(
									uploadItem.getFile().getFileItem(), resps.getDocument()));
							byteArrayFile=bade.getBytes();
						}
					}

			    	
					ByteArrayInputStream streamXML = new  ByteArrayInputStream(byteArrayFile);
					String xmlContent = IOUtils.toString(streamXML, "UTF-8");
					
					if(xmlContent.contains(AppConstants.NAMESPACE_CFDI_V4)) {
						invoiceDTO = xmlToPojoService.convertV4(xmlContent);
					} else {
						invoiceDTO = xmlToPojoService.convert(xmlContent);
					}
					

			    	elementDTO.setInvoiceDTO(invoiceDTO);
			    	elementDTO.setXmlFileName(ze.getName());
			    	elementDTO.setXmlFile(byteArrayFile);
				    fileMap.put(fileName, elementDTO);
				    fileList.add(fileMap);
			    }
			    
			    if(FilenameUtils.getExtension(ze.getName()).equals(AppConstants.FILE_EXT_PDF)) {
			        byte[] byteArrayFile = this.getByteArrayFromZipInputStream(fileName, AppConstants.FILE_EXT_PDF, fileSize, zis);
			        attachedMap = new HashMap<String, byte[]>();
			        attachedMap.put(fileName, byteArrayFile);
			        attachedList.add(attachedMap);
			    }
			}			
			zis.close();
			
			boolean isAssigned = false;
			if(fileList != null && !fileList.isEmpty() && attachedList != null && !attachedList.isEmpty()) {
				for(Map<String, ZipElementDTO> dto : fileList) {
					for(Map.Entry<String, ZipElementDTO> entryDTO : dto.entrySet()) {						
						isAssigned = false;
						
						for(Map<String, byte[]> attached : attachedList) {						
							for(Map.Entry<String, byte[]> entryAtt : attached.entrySet()) {									
								if(entryDTO.getKey().equals(entryAtt.getKey())) {										
									entryDTO.getValue().setAttachedFile(entryAtt.getValue());
									entryDTO.getValue().setAttachedFileName(entryAtt.getKey().concat(".").concat(AppConstants.FILE_EXT_PDF));									
									isAssigned = true;
									break;
								}
							}							
							if(isAssigned) {
								break;
							}
						}
						
					}
				}
			}
		} catch (IOException e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}

        //Validación Montos
		UDC porcentajeMaxUdc = udcService.searchBySystemAndKey("PORCENTAJE", "MAX");
		UDC porcentajeMinUdc = udcService.searchBySystemAndKey("PORCENTAJE", "MIN");		
		UDC montoLimiteUdc = udcService.searchBySystemAndKey("MONTO", "LIMITE");		
		UDC montoLimiteMaxUdc = udcService.searchBySystemAndKey("MONTO", "MAX");
		UDC montoLimiteMinUdc = udcService.searchBySystemAndKey("MONTO", "MIN");
		
		double porcentajeMax = Double.valueOf(porcentajeMaxUdc.getStrValue1()) / 100;
		double porcentajeMin = Double.valueOf(porcentajeMinUdc.getStrValue1()) / 100;	
		double montoLimite = Double.valueOf(montoLimiteUdc.getStrValue1());
		double montoLimiteMax = Double.valueOf(montoLimiteMaxUdc.getStrValue1());
		double montoLimiteMin = Double.valueOf(montoLimiteMinUdc.getStrValue1());
		double orderAmount = 0;
		double foreignOrderAmount = 0;
		double paymentAmount = 0;
		
		for(Receipt r :requestedReceiptList) {
			orderAmount = orderAmount + Math.abs(r.getAmountReceived());
			foreignOrderAmount = foreignOrderAmount + Math.abs(r.getForeignAmountReceived());
			paymentAmount = paymentAmount + Math.abs(r.getPaymentAmount());
		}
		
		double totalImporte = 0;
		double totalImporteMayor = 0;
		double totalImporteMenor = 0;
		double invoiceTotalAmount = 0;
		double invoiceAmount = 0;
		double discount = 0;
				
		//Obtiene moneda orden de compra
		String oCurr = "";
		if("MXP".equals(po.getCurrecyCode())) {
			oCurr = "MXN";
		}else {
			oCurr = po.getCurrecyCode();
		}
		
		String invCurrency = "";
		//Obtiene Importes Totales de las facturas
		if(fileList != null && !fileList.isEmpty()) {
			for(Map<String, ZipElementDTO> o : fileList) {
				for (Map.Entry<String,ZipElementDTO> entry : o.entrySet())  {
					
					double exchangeRate = 0;
					ZipElementDTO zElement = entry.getValue();
					invCurrency = zElement.getInvoiceDTO().getMoneda().trim();					
					
                	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante) && !"I".equals(zElement.getInvoiceDTO().getTipoComprobante())){
                		return "El documento cargado no es de tipo FACTURA.<br />(Tipo Comprobante = I) UUID: " + zElement.getInvoiceDTO().getUuid();
                	}
                	
                	if(AppConstants.NC_FIELD.equals(tipoComprobante) && !"E".equals(zElement.getInvoiceDTO().getTipoComprobante())){
                		return "El documento cargado no coresponde a una NOTA DE CREDITO.<br />(Tipo Comprobante = E) UUID: " + zElement.getInvoiceDTO().getUuid();
                	}                
					
					if(!invCurrency.equals(oCurr)) {
						return "La moneda de la factura es " + invCurrency + " sin embargo, el código de moneda de la orden de compra es " + oCurr + " UUID:" + zElement.getInvoiceDTO().getUuid();
					}
					
					if(!AppConstants.DEFAULT_CURRENCY.equals(invCurrency)) {
						exchangeRate = zElement.getInvoiceDTO().getTipoCambio();						
						if(exchangeRate == 0) {
							return "La moneda de la factura es " + invCurrency + " sin embargo, no existe definido un tipo de cambio. UUID: " + zElement.getInvoiceDTO().getUuid();
						}
					}
					
					if(zElement.getInvoiceDTO().getDescuento() != 0) {
						discount = zElement.getInvoiceDTO().getDescuento();
						discount = discount * 100;
						discount = Math.round(discount);
						discount = discount /100;
					}
					invoiceTotalAmount = invoiceTotalAmount + zElement.getInvoiceDTO().getSubTotal();
					invoiceTotalAmount = invoiceTotalAmount * 100;
					invoiceTotalAmount = Math.round(invoiceTotalAmount);
					invoiceTotalAmount = invoiceTotalAmount/100;
					
					invoiceAmount = invoiceAmount + zElement.getInvoiceDTO().getSubTotal() - discount;
					invoiceAmount = invoiceAmount * 100;
					invoiceAmount = Math.round(invoiceAmount);
					invoiceAmount = invoiceAmount/100;
				}
			}	
		}
		
		if(domesticCurrency.equals(invCurrency)) {
			totalImporte = Math.round(orderAmount*100.00)/100;
			totalImporteMayor = orderAmount;
			totalImporteMenor = orderAmount;
		} else {
			totalImporte = Math.round(foreignOrderAmount*100.00)/100;
			totalImporteMayor = foreignOrderAmount;
			totalImporteMenor = foreignOrderAmount;
		}
		
		// Validación con los importes del recibo:
		String tipoValidacion ="";
		if(montoLimite != 0) {
			if(invoiceTotalAmount >= montoLimite) {
				totalImporteMayor = totalImporteMayor + montoLimiteMax;
				totalImporteMenor = totalImporteMenor - montoLimiteMin;
				tipoValidacion = "Por Monto";
			}else {
				totalImporteMayor = totalImporteMayor + (totalImporteMayor * porcentajeMax);
				totalImporteMenor = totalImporteMenor - (totalImporteMenor * porcentajeMin);
				tipoValidacion = "Por porcentaje";
			}
		}
		
		totalImporteMayor = totalImporteMayor * 100;
		totalImporteMayor = Math.round(totalImporteMayor);
		totalImporteMayor = totalImporteMayor /100;	
		
		totalImporteMenor = totalImporteMenor * 100;
		totalImporteMenor = Math.round(totalImporteMenor);
		totalImporteMenor = totalImporteMenor /100;	

		if(totalImporteMayor < invoiceAmount || totalImporteMenor > invoiceAmount) {
			return "El total de las facturas " + currencyFormat.format(invoiceAmount) + " no coincide con el total de los recibos seleccionados " + currencyFormat.format(totalImporte) + ". Tipo de validación: " + tipoValidacion + ".";
		}
		
        // Envío Proceso Batch        
		ProcessBatchInvoice bp = new ProcessBatchInvoice();
		bp.setUdcService(udcService);
		bp.setPurchaseOrderService(purchaseOrderService);
		bp.seteDIService(eDIService);
		bp.sethTTPRequestService(HTTPRequestService);
		bp.setMailSenderObj(mailSenderObj);
		bp.setStringUtils(stringUtils);
		bp.setPaymentCalendarService(paymentCalendarService);
		bp.setRequestedReceiptList(requestedReceiptList);
		bp.setDocumentsService(this);		
		bp.setLogger(logger);
		bp.setUser(user);
		bp.setS(s);		
		bp.setPo(po);
		bp.setFileList(fileList);
		bp.setDocumentNumber(documentNumber);
		bp.setDocumentType(documentType);
		bp.setTipoComprobante(tipoComprobante);
		bp.setInvoiceAmount(invoiceAmount);
		Thread emailThreadSup = new Thread(bp);
		emailThreadSup.start();

		return "";
	}
	
	@SuppressWarnings({ "unused" })
	public String validateInvoiceFromOrder(InvoiceDTO inv,
								  String addressBook, 
								  int documentNumber, 
								  String documentType,
								  String tipoComprobante,
								  PurchaseOrder po,
								  boolean sendVoucher,
								  String xmlContent,
								  String receiptList,
								  boolean specializedServices,
								  String plantCodePO,
								  String usr){
		
		/////////////////////validacion de XML CRUZG
		ResponseGeneral resps=validateInvoiceVsSat(inv,xmlContent);	
		if (resps.isError()) {
			return resps.getMensaje().get("es");///devuelve solo el mensaje en español--en para ingles
		}else {
			if (resps.getDocument() != null) {
				InvoiceDTO invoiceUP =null;
				String InvoiceXML=null;
				ResponseGeneral resp=null;
				
				 ByteArrayInputStream stream = new  ByteArrayInputStream(resps.getDocument());
				try {
						InvoiceXML = IOUtils.toString(stream, "UTF-8");
						InvoiceXML = takeOffBOM(IOUtils.toInputStream(InvoiceXML, "UTF-8"));
						InvoiceXML = InvoiceXML.replace("?<?xml", "<?xml");
					} catch (IOException e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatXML",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp.getMensaje().get("es");
						
					}
				try {
					invoiceUP =getInvoiceXmlFromString(InvoiceXML);
					} catch (Exception e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatDTO",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp.getMensaje().get("es");
						
					}
				inv=invoiceUP;
				xmlContent=InvoiceXML;
			}
		}

		
		DecimalFormat currencyFormat = new DecimalFormat("$#,###.###");
		UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");
		
		if(udcCfdi != null) {
			if(!"".equals(udcCfdi.getStrValue1())) {
				if("TRUE".equals(udcCfdi.getStrValue1())) {
					String vcfdi = validaComprobanteSAT(inv);
					if(!"".equals(vcfdi)) {
						return "Error_9";
					}
					
					String vNull = validateInvNull(inv);
					if(!"".equals(vNull)) {
						return "Error_20_" + vNull + ".";
					}
				}
			}
		}else {
			String vcfdi = validaComprobanteSAT(inv);
			if(!"".equals(vcfdi)) {
				return "Error_9";
			}
			
			String vNull = validateInvNull(inv);
			if(!"".equals(vNull)) {
				return "Error_20_" + vNull + ".";
			}
		}
		
		String rfcEmisor = inv.getRfcEmisor();
		boolean allRules = true;
		List<UDC> supExclList =  udcService.searchBySystem("NOCHECKSUP");
		if(supExclList != null) {
			for(UDC udc : supExclList) {
				if(rfcEmisor.equals(udc.getStrValue1())){
					allRules = false;
					break;
				}
			}
		}
		
		Supplier s = supplierService.searchByAddressNumber(addressBook);
		if(s == null) {
			return "Error_10";
		}
		
		//Multiusuario
		String emailRecipient = "";
		Users currentUser = null;
		if(usr != null) {
			currentUser = usersService.searchCriteriaUserName(usr);
			emailRecipient = currentUser.getEmail();
		}
		
		int diasCred = 30;
		/*
		if(s.getDiasCredito() != null && !s.getDiasCredito().isEmpty()) {
			diasCred = Integer.valueOf(s.getDiasCredito());
		} 
		*/
		List<Receipt> requestedReceiptList = null;

		List<Receipt> receiptArray= purchaseOrderService.getOrderReceipts(documentNumber, addressBook, documentType, po.getOrderCompany());
		
		String fechaFactura = inv.getFechaTimbrado();
		fechaFactura = fechaFactura.replace("T", " ");
		SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
		Date invDate = null;
		try {
			invDate = sdf.parse(fechaFactura);
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}

		//Validación CFDI Versión 3.3
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
							return "Error_35";
						}
					}
				} catch (Exception e) {
					log4j.error("Exception" , e);
					e.printStackTrace();
				}
			}
		}
		
		String invCurrency = inv.getMoneda().trim();
		double exchangeRate = inv.getTipoCambio();
		
		//JSC:Se sustituyen Ids por Numeros de Recibo para Facturas Nacionales y Foráneas
		String[] idList = receiptList.split(",");
		//Receipt selReceipt = purchaseOrderService.getReceiptById(Integer.valueOf(idList[0]).intValue());
		List<String> rnList = Arrays.asList(idList);
		Receipt selReceipt = null;
		
		if(receiptArray != null) {
			//JSC: Se agrupan los Recibos en interfaz
			requestedReceiptList = new ArrayList<Receipt>();
			for(Receipt r : receiptArray) {
				if(rnList.contains(String.valueOf(r.getDocumentNumber()))) {
					if(selReceipt == null) {
						selReceipt = r;
					}
					requestedReceiptList.add(r);
				}
				/*
				if(r.getDocumentNumber() == selReceipt.getDocumentNumber()) {
					requestedReceiptList.add(r);
				}
				*/
				/*
				if(Arrays.asList(idList).contains(String.valueOf(r.getId()))) {
					requestedReceiptList.add(r);
				}*/
			}
		}else {
			return "Error_11";
	    }
				
		//Date minDateReceipts = requestedReceiptList.stream().map(u -> u.getReceiptDate()).min(Date::compareTo).get();

		String domesticCurrency = AppConstants.DEFAULT_CURRENCY;
		boolean isDomesticCurrency = false;
	/*	List<UDC> comUDCList =  udcService.searchBySystem("COMPANYDOMESTIC");
		if(comUDCList != null && !comUDCList.isEmpty()) {
			for(UDC company : comUDCList) {
				if(company.getStrValue1().trim().equals(po.getOrderCompany().trim()) && !"".equals(company.getStrValue2().trim())) {
					domesticCurrency = company.getStrValue2().trim();
					break;
				}
			}
		}*/
		
		if(domesticCurrency.contains(inv.moneda)) {
			isDomesticCurrency = true;
		}
		
		if(allRules) {
			List<Receipt> recUuidList = purchaseOrderService.getReceiptsByUUID(inv.getUuid());
			if(recUuidList != null) {
				if(recUuidList.size()>0)
					return "Error_8"; 
			}
			
			if(!AppConstants.DEFAULT_CURRENCY.equals(invCurrency)) {				
				if(exchangeRate == 0) {
					return "Error_21_" + invCurrency;
				}
			}
			
			String oCurr = "";
			if("MXP".equals(po.getCurrecyCode())) {
				oCurr = "MXN";
			}else {
				oCurr = po.getCurrecyCode();
			}
			if(!invCurrency.equals(oCurr)) {
				return "Error_22_" + invCurrency + "_:_" + oCurr;
			}
			String strStartSkipDateIssuance=null;
			String strFinalSkipDateIssuance = null;
			boolean skipDateIssuance  = false;
			List<UDC> skipValidationList =  udcService.searchBySystem("OMITIRVALIDACION");
			if(skipValidationList != null) {
				for(UDC udc : skipValidationList) {
					if("FECHAEMISION".equals(udc.getUdcKey()) && udc.isBooleanValue()){
						skipDateIssuance = udc.isBooleanValue();
						strStartSkipDateIssuance = udc.getStrValue1();
						strFinalSkipDateIssuance = udc.getStrValue2();
						break;
					}
				}
			}
			
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, currentYear);
			cal.set(Calendar.DAY_OF_YEAR, 1);  
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date startYear = cal.getTime();
			try {
				if(invDate.compareTo(startYear) < 0) {
					if(skipDateIssuance) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						Date dateStartSkipDateIssuance = formatter.parse(strStartSkipDateIssuance);
						Date dateFinalSkipDateIssuance = formatter.parse(strFinalSkipDateIssuance);
						if(invDate.compareTo(dateStartSkipDateIssuance) < 0 || invDate.compareTo(dateFinalSkipDateIssuance)>0) {
							return "Error_12";
						}
						
						
					}else {
						return "Error_12";
					}
				
				}
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return "Error_13";
			}
			
	
			/*//Validación FechaTimbrado VS FechaRecibos
			try {
			for(Receipt r :requestedReceiptList) {
				if(invDate.compareTo(r.getReceiptDate()) < 0) {
					return "Error_12";
				}
			}
			
			}catch(Exception e) {
			    log4j.error("Exception" , e);
				e.printStackTrace();
				return "Error_13";
			}*/
			
			if(s != null) {
				NonComplianceSupplier ncs = this.nonComplianceSupplierService.searchByTaxId(s.getRfc(), 0, 0);
			    if (ncs != null && (
			      ncs.getRefDate1().contains("Definitivo") || 
			      ncs.getRefDate1().contains("Presunto") || 
			      ncs.getRefDate1().contains("Desvirtuado") || 
			      ncs.getRefDate2().contains("Definitivo") || 
			      ncs.getRefDate2().contains("Presunto") || 
			      ncs.getRefDate2().contains("Desvirtuado") || 
			      ncs.getStatus().contains("Definitivo") || 
			      ncs.getStatus().contains("Presunto") || 
			      ncs.getStatus().contains("Desvirtuado"))) {
			    	
			    	String altEmail = "";
						List<UDC> udcList =  udcService.searchBySystem("TAXALTEMAIL");
						if(udcList != null) {
							String emailContent = AppConstants.EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER;
							emailContent = emailContent.replace("_ADDNUMBER_", s.getAddresNumber());
							emailContent = emailContent.replace("_RAZONSOCIAL_", s.getRazonSocial());

							
							altEmail = udcList.get(0).getStrValue1();
						    EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
							emailAsyncSup.setProperties(
									AppConstants.EMAIL_NO_COMPLIANCE_INVOICE,
									emailContent,
									altEmail);
							emailAsyncSup.setMailSender(mailSenderObj);
							Thread emailThreadSup = new Thread(emailAsyncSup);
							emailThreadSup.start();
						}
			    	
						 EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						 emailAsyncSup.setProperties(
									AppConstants.EMAIL_INVOICE_REJECTED + " " + po.getOrderNumber() ,
									AppConstants.EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER_NOTIF + inv.getUuid() + "<br /> <br />" + AppConstants.ETHIC_CONTENT,
									currentUser.getEmail());//Multiusuarios
							emailAsyncSup.setMailSender(mailSenderObj);
							Thread emailThreadSup = new Thread(emailAsyncSup);
							emailThreadSup.start();
						
						return "Error_14";
	
			    } 
			}else {
				return "Error_15";
			}

			boolean companyRfcIsValid = true;
			List<UDC> companyRfc = udcDao.searchBySystem("COMPANYRFC");
			if(companyRfc != null) {
				for(UDC udcrfc : companyRfc) {
					String cRfc = udcrfc.getStrValue1();
					String cRfcCompany = udcrfc.getStrValue2();
					if(cRfc.equals(inv.getRfcReceptor())) {
						if(!cRfcCompany.equals(po.getCompanyKey())) {
							companyRfcIsValid = false;
						}
					}
				}
			}
			
			if(!companyRfcIsValid) {
				return "Error_16";
			}
			
			String buyerEmail = po.getEmail();
			
			if(AppConstants.LOCAL_COUNTRY_CODE.equals(s.getCountry()) && AppConstants.REF_METODO_PAGO.equals(inv.getMetodoPago())) {
				String pendingList = purchaseOrderService.getPendingReceiptsComplPago(s.getAddresNumber());
				if(!"".equals(pendingList)){
					return "Error_23_" + pendingList;
				}
			}
	
			cal = Calendar.getInstance();
			invDate = null;
			Date orderDate = null;
			try {
				fechaFactura = inv.getFechaTimbrado();
				fechaFactura = fechaFactura.replace("T", " ");
				sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
				invDate = sdf.parse(fechaFactura);
				orderDate = po.getDateRequested();
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}
	
			int pueOffSetDays = 0;
			UDC pueOffSet = udcService.searchBySystemAndKey(AppConstants.FACTURA_PUE, AppConstants.OFFSET_DAYS);
			if(pueOffSet != null) {
				if(pueOffSet.getStrValue1() != null) {
					pueOffSetDays = Integer.valueOf(pueOffSet.getStrValue1());
				}
			}
			
			
			List<UDC> paymentMethodList = udcService.searchBySystem("METODOPAGO");
			boolean ispaymentMethodValid = false;
					for(UDC paymentMethod : paymentMethodList) {
						if(paymentMethod.getUdcKey().equals(inv.getMetodoPago().trim())) {
							ispaymentMethodValid = true;
							break; 
						}
					}
					
			/*if(!AppConstants.REF_METODO_PAGO.equals(inv.getMetodoPago()) || !AppConstants.REF_METODO_PAGO_PUE.equals(inv.getMetodoPago()) ){
				//return "El método de pago permitido es " + AppConstants.REF_METODO_PAGO + " y su CFDI contiene el valor " + inv.getMetodoPago() + ". Favor de emitir nuevamente el CFDI con el método de pago antes mencionado.";			
				return "Los métodos de pago permitidos son " + AppConstants.REF_METODO_PAGO + " y " + AppConstants.REF_METODO_PAGO_PUE + ". Su CFDI contiene el valor " + inv.getMetodoPago() + ". Favor de emitir nuevamente el CFDI con el método de pago antes mencionado.";
			}*/
					
			if(!ispaymentMethodValid){
				return "Error_24_" + inv.getMetodoPago();
			}
			
			List<UDC> paymentList = udcService.searchBySystem("FORMAPAGO");
			boolean ispaymentValid = false;
					for(UDC payment : paymentList) {
						if(payment.getUdcKey().equals(inv.getFormaPago().trim())) {
							ispaymentValid = true;
							break; 
						}
					}
			
			/*if(!AppConstants.REF_FORMA_PAGO.equals(inv.getFormaPago())){
				return  "La forma de pago permitida es " + AppConstants.REF_FORMA_PAGO + " y su CFDI contiene el valor " + inv.getFormaPago() + ". Favor de emitir nuevamente el CFDI con la forma de pago antes mencionada";		
			}*/
					
			if(!ispaymentValid){
				return  "Error_25_" + inv.getFormaPago() + ". Favor de emitir nuevamente el CFDI con una forma de pago permitida";		
					}
			
			String cfdiReceptor = inv.getReceptor().getUsoCFDI();
			String rfcReceptor = inv.getRfcReceptor();
			/*if(!AppConstants.USO_CFDI.equals(cfdiReceptor)){
				List<UDC> udcList =  udcService.searchBySystem("CFDIEXC");
				boolean usoCfdiExcept = false;
				if(udcList != null) {
					for(UDC udc : udcList) {
						if(udc.getStrValue1().equals(rfcEmisor)){
							usoCfdiExcept = true;
						}
					}
				}
				
				if(usoCfdiExcept) {
					return "El uso CFDI " + cfdiReceptor + " no es permitido para su razón social";
				}
				
			}*/
			
			List<UDC> cfdiUseList = udcService.searchBySystem("USOCFDI");
			boolean isCfdiUseValid = false;
					for(UDC cfdiUse : cfdiUseList) {
						if(cfdiUse.getUdcKey().equals(cfdiReceptor.trim())) {
							isCfdiUseValid = true;
							break; 
						}
					}
								
			if(!isCfdiUseValid){
				return  "Error_17";		
					}
			
			
			if(rfcEmisor != null) {
				if(!"".equals(rfcEmisor)) {
					if(!s.getRfc().equals(rfcEmisor)) {
						return "Error_26_" + s.getRfc();
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
				return "Error_27_" + inv.getRfcReceptor();
			}
			
			//Validación Impuestos
			String receiptTaxCode = "";
			for(Receipt r :requestedReceiptList) {
				if(r.getTaxCode() != null && !"".equals(r.getTaxCode().trim())) {
					receiptTaxCode = r.getTaxCode().trim();
					break;
				}
			}
			
			String tasaOCuota = "";
			List<UDC> udcTax = udcDao.searchBySystem("TAXCODE");
			List<String> validTaxCodeList = new ArrayList<String>();
			
			if(udcTax != null) {
				for(UDC ut :udcTax) {
					validTaxCodeList.add(ut.getUdcKey().trim());
				}
			}
						
			//Valida los impuestos contenidos en el CFDI
			if(!"".equals(receiptTaxCode)) {
				if(validTaxCodeList.contains(receiptTaxCode)) {					
					List<String> cfdiTransTaxList = this.getTranslatedTaxList(inv);
					List<String> cfdiRetTaxList = this.getRetainedTaxList(inv);
					List<String> udcTransTaxList = new ArrayList<String>();
					List<String> udcRetTaxList = new ArrayList<String>();
					String udcValueT = "";
					String udcValueR = "";
					String tasasRequeridas = "";
					String tasasTrasReq = "";
					String tasasRetReq = "";
					boolean cfdiIva0Retencion=false;
					boolean cfdiIva0Traslado=false;
					
					//Obtiene lista de impuestos Trasladados requeridos con base en la UDC
					for(UDC ut :udcTax) {
						if(ut.getUdcKey().equals(receiptTaxCode.trim()) && ut.getStrValue1() != null && !"".equals(ut.getStrValue1().trim()) && !"NA".equals(ut.getStrValue1().trim())) {
							udcValueT = ut.getStrValue1();
							udcTransTaxList = this.stringWithPipesToList(udcValueT);
							tasasTrasReq = udcValueT.replace("|",", ").trim();
							break;
						}
					}
					if("".equals(tasasTrasReq)) {
						tasasTrasReq = "N/A";
					}
					
					//Obtiene lista de impuestos Retenidos requeridos con base en la UDC
					for(UDC ut :udcTax) {
						if(ut.getUdcKey().equals(receiptTaxCode.trim()) && ut.getStrValue2() != null && !"".equals(ut.getStrValue2().trim()) && !"NA".equals(ut.getStrValue2().trim())) {
							udcValueR = ut.getStrValue2().trim();
							udcRetTaxList = this.stringWithPipesToList(udcValueR);
							tasasRetReq = udcValueR.replace("|",", ").trim();
							break;
						}
					}
					if("".equals(tasasRetReq)) {
						tasasRetReq = "N/A";
					}
					
					///verificar primero si tanto en udc como en los trastalos y retenciones  existe el registro "0.000000"
					
					
					
					//TRASLADADOS
					if(udcTransTaxList.contains("0.000000")) {
						
						if(!(cfdiTransTaxList.containsAll(udcTransTaxList)&&udcTransTaxList.containsAll(cfdiTransTaxList))) {
							return "Error_34_" + receiptTaxCode + "_:_" + tasasTrasReq + "_:_" + tasasRetReq + ".";
						}
						
					}else if (cfdiTransTaxList.contains("0.000000")) {
						
						cfdiTransTaxList.removeIf(elemento -> elemento.equals("0.000000"));
						if(!(cfdiTransTaxList.containsAll(udcTransTaxList)&&udcTransTaxList.containsAll(cfdiTransTaxList))) {
							return "Error_34_" + receiptTaxCode + "_:_" + tasasTrasReq + "_:_" + tasasRetReq + ".";
						}
					}else {
						if(!(cfdiTransTaxList.containsAll(udcTransTaxList)&&udcTransTaxList.containsAll(cfdiTransTaxList))) {
							return "Error_34_" + receiptTaxCode + "_:_" + tasasTrasReq + "_:_" + tasasRetReq + ".";
						}
						
					}
					
					
					
					//RETENCIONES
					if(udcRetTaxList.contains("0.000000")) {
						
						if(!(cfdiRetTaxList.containsAll(udcRetTaxList)&&udcRetTaxList.containsAll(cfdiRetTaxList))) {
							return "Error_34_" + receiptTaxCode + "_:_" + tasasTrasReq + "_:_" + tasasRetReq + ".";
						}
						
					}else if (cfdiRetTaxList.contains("0.000000")) {
						
						cfdiRetTaxList.removeIf(elemento -> elemento.equals("0.000000"));
						if(!(cfdiRetTaxList.containsAll(udcRetTaxList)&&udcRetTaxList.containsAll(cfdiRetTaxList))) {
							return "Error_34_" + receiptTaxCode + "_:_" + tasasTrasReq + "_:_" + tasasRetReq + ".";
						}
					}else {
						if(!(cfdiRetTaxList.containsAll(udcRetTaxList)&&udcRetTaxList.containsAll(cfdiRetTaxList))) {
							return "Error_34_" + receiptTaxCode + "_:_" + tasasTrasReq + "_:_" + tasasRetReq + ".";
						}
					}
					
										
					
					
//			inicio de intervencion		
//					
//					//TRASLADADOS
//					//Valida que el CFDI cuente con los impuestos Trasladado requeridos
//					if(!udcTransTaxList.isEmpty()) {
//						for(String transTaxValue : udcTransTaxList) {
//							//Verifica si el CFDI cuenta con el impuesto Trasladado
//							if(!cfdiTransTaxList.contains(transTaxValue)) {
//								/*
//								tasasRequeridas = udcValueT.replace("|",", ").trim();
//								if("".equals(tasasRequeridas)) {
//									tasasRequeridas = "N/A";
//								}
//								return "Error_28_" + receiptTaxCode + "_:_" + tasasRequeridas + ".";
//								*/
//								
//								
//								return "Error_34_" + receiptTaxCode + "_:_" + tasasTrasReq + "_:_" + tasasRetReq + ".";
//							}
//						}					
//					}
//					
//					//Valida que el CFDI no tenga impuestos Trasladados adicionales a los impuestos requeridos
//					if(!cfdiTransTaxList.isEmpty()) {					
//						for(String transTaxValue : cfdiTransTaxList) {
//							if(!udcTransTaxList.contains(transTaxValue)) {
//								/*
//								tasasRequeridas = udcValueT.replace("|",", ").trim();
//								if("".equals(tasasRequeridas)) {
//									tasasRequeridas = "N/A";
//								}
//								return "Error_29_" + receiptTaxCode + "_:_" + tasasRequeridas + "_:_" + transTaxValue + ".";
//								*/
//								return "Error_34_" + receiptTaxCode + "_:_" + tasasTrasReq + "_:_" + tasasRetReq + ".";
//							}
//						}
//					}
//					
//					//RETENIDOS					
//					//Valida que el CFDI cuente con los impuestos Retenidos requeridos
//					if(!udcRetTaxList.isEmpty()) {
//						for(String retTaxValue : udcRetTaxList) {
//							//Verifica si el CFDI cuenta con el impuesto Retenido
//							if(!cfdiRetTaxList.contains(retTaxValue)) {
//								/*
//								tasasRequeridas = udcValueR.replace("|",", ").trim();
//								if("".equals(tasasRequeridas)) {
//									tasasRequeridas = "N/A";
//								}
//								return "Error_30_" + receiptTaxCode + "_:_" + tasasRequeridas + ".";
//								*/
//								return "Error_34_" + receiptTaxCode + "_:_" + tasasTrasReq + "_:_" + tasasRetReq + ".";
//							}
//						}
//					}
//					
//					//Valida que el CFDI no tenga impuestos Retenidos adicionales a los impuestos requeridos
//					if(!cfdiRetTaxList.isEmpty()) {					
//						for(String retTaxValue : cfdiRetTaxList) {
//							if(!udcRetTaxList.contains(retTaxValue)) {
//								/*
//								tasasRequeridas = udcValueR.replace("|",", ").trim();
//								if("".equals(tasasRequeridas)) {
//									tasasRequeridas = "N/A";
//								}
//								return "Error_31_" + receiptTaxCode + "_:_" + tasasRequeridas + "_:_" + retTaxValue + ".";
//								*/
//								return "Error_34_" + receiptTaxCode + "_:_" + tasasTrasReq + "_:_" + tasasRetReq + ".";
//							}
//						}
//					}
//					
//					
//					fin de intervencion
					
				} else {
					return "Error_32_" + receiptTaxCode;
				}
			} else {
				return "Error_18";
			}
			
			//Validacion Cantidad de producto  DACG 11/05/22
	/*	int linea =0; 
	     float quantityReceipt = 0;
		for(Receipt r :requestedReceiptList) {
			quantityReceipt = quantityReceipt + r.getQuantityReceived();
		}
		float invQuantity = 0;
		for(int x =0 ; x < inv.concepto.size(); x ++) {
			invQuantity = invQuantity + Float.parseFloat(inv.concepto.get(x).getCantidad());  
		}
		
		if(invQuantity != quantityReceipt) {
			return "Error_19";
		}*/
			
		/*	try {
			for(int x =0 ; x < inv.concepto.size(); x ++) {
				//for(Receipt r :requestedReceiptList) {
				//for(int y =0 ; y < requestedReceiptList.size(); y ++) {
				//	if(r.getLineNumber() == (x+1)) {
					Receipt r = requestedReceiptList.get(x);
					
						float invQuantity=Float.parseFloat(inv.concepto.get(x).getCantidad());  
						
					//	if(invQuantity == r.getQuantityReceived()) {
						
						//	break;
						//}
						if(invQuantity != r.getQuantityReceived()) {
							return "Las cantidades de unidades en la factura no corresponden a las del recibo.";
						}
					//}
				//}
			}
			}catch(Exception ex) {
				log4j.error("Exception" , e);
				ex.printStackTrace();
				return "Verifique las lineas de cantidas coincida con las del recibo.";
			}*/
			
			//Validación de Montos de Impuestos 
			UDC impMontoLimiteMaxUdc = udcService.searchBySystemAndKey("MONTOIMPUESTO", "MAX");
			UDC impMontoLimiteMinUdc = udcService.searchBySystemAndKey("MONTOIMPUESTO", "MIN");
			double impMontoLimiteMax = Double.valueOf(impMontoLimiteMaxUdc.getStrValue1());
			double impMontoLimiteMin = Double.valueOf(impMontoLimiteMinUdc.getStrValue1());
			
			String taxAmountValidation = this.validateTaxAmount(inv, impMontoLimiteMax, impMontoLimiteMin);			
			if(!"".equals(taxAmountValidation)) {
				return taxAmountValidation;
			}
			
			// Validación de los importes del recibo:			
			/*if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)){
				Users u = usersService.searchCriteriaUserName(po.getAddressNumber());
				
				UDC porcentajeMaxUdc = udcService.searchBySystemAndKey("PORCENTAJE", "MAX");
				UDC porcentajeMinUdc = udcService.searchBySystemAndKey("PORCENTAJE", "MIN");				
				UDC montoLimiteUdc = udcService.searchBySystemAndKey("MONTO", "LIMITE");				
				UDC montoLimiteMaxUdc = udcService.searchBySystemAndKey("MONTO", "MAX");
				UDC montoLimiteMinUdc = udcService.searchBySystemAndKey("MONTO", "MIN");
				
				double porcentajeMax = Double.valueOf(porcentajeMaxUdc.getStrValue1()) / 100;
				double porcentajeMin = Double.valueOf(porcentajeMinUdc.getStrValue1()) / 100;
				
				double montoLimite = Double.valueOf(montoLimiteUdc.getStrValue1());
				double montoLimiteMax = Double.valueOf(montoLimiteMaxUdc.getStrValue1());
				double montoLimiteMin = Double.valueOf(montoLimiteMinUdc.getStrValue1());
				
				double orderAmount = 0;
				double foreignOrderAmount = 0;
				double retainedAmount = 0;
				double retainedForeignAmount = 0;
				double currentRetainedAmount = 0;
				double invoiceAmount = 0;
				
			
				
				
				for (Receipt r : requestedReceiptList) {
					if (AppConstants.RECEIPT_CODE_RETENTION.equals(r.getReceiptType())) {
						retainedAmount = retainedAmount + r.getAmountReceived();
						retainedForeignAmount = retainedForeignAmount + r.getForeignAmountReceived();
					} else {
						orderAmount = orderAmount + r.getAmountReceived();
						foreignOrderAmount = foreignOrderAmount + r.getForeignAmountReceived();
					}
				}
				String tipoValidacion = null;
				
				double totalImporteMayor = 0;
				double totalImporteMenor = 0;
				double invoiceTotalAmount = 0;
				double currentInvoiceAmount = 0;
				
				if(domesticCurrency.equals(invCurrency)) {
					currentInvoiceAmount = orderAmount;
					totalImporteMayor = orderAmount;
					totalImporteMenor = orderAmount;
				} else {
					currentInvoiceAmount = foreignOrderAmount;
					totalImporteMayor = foreignOrderAmount;
					totalImporteMenor = foreignOrderAmount;
				}
				
				invoiceTotalAmount = inv.getSubTotal();
				if(montoLimite != 0) {
					if(invoiceTotalAmount >= montoLimite) {
						totalImporteMayor = totalImporteMayor + montoLimiteMax;
						totalImporteMenor = totalImporteMenor - montoLimiteMin;
						tipoValidacion = "Por Monto";
					}else {
						totalImporteMayor = totalImporteMayor + (totalImporteMayor * porcentajeMax);
						totalImporteMenor = totalImporteMenor - (totalImporteMenor * porcentajeMin);
						tipoValidacion = "Por porcentaje";
					}
				}
				
				totalImporteMayor = totalImporteMayor * 100;
				totalImporteMayor = Math.round(totalImporteMayor);
				totalImporteMayor = totalImporteMayor /100;	
				
				totalImporteMenor = totalImporteMenor * 100;
				totalImporteMenor = Math.round(totalImporteMenor);
				totalImporteMenor = totalImporteMenor /100;
				
				double discount = 0;
				
				if(inv.getDescuento() != 0) {
					discount = inv.getDescuento();
					discount = discount * 100;
					discount = Math.round(discount);
					discount = discount /100;	
				}	
				invoiceAmount = inv.getSubTotal() - discount;
				invoiceAmount = invoiceAmount * 100;
				invoiceAmount = Math.round(invoiceAmount);
				invoiceAmount = invoiceAmount /100;
				
				if(totalImporteMayor < invoiceAmount || totalImporteMenor > invoiceAmount) {
					return "El total de la factura " + currencyFormat.format(invoiceAmount) + " no coincide con el total de los recibos seleccionados " + currencyFormat.format(currentInvoiceAmount) + ". Tipo de validación:" + tipoValidacion;
				}else {
					
					List<UDC> pmtTermsUdc = udcService.searchBySystem("PMTTERMS");
					String pmtTermsCode = "";
					for(Receipt r :requestedReceiptList) {
						if(r.getPaymentTerms() != null && !"".equals(r.getPaymentTerms())) {
							for(UDC udcpmt : pmtTermsUdc) {
								if(udcpmt.getStrValue1().equals(r.getPaymentTerms().trim())) {
									diasCred = Integer.parseInt(udcpmt.getStrValue2());
									break;
								}
							}
						}
						if(r.getPaymentTerms() == null || "".equals(r.getPaymentTerms())) {
							diasCred = 30;
							break;
						}
					}
					
					Date estimatedPaymentDate = null;
					Date currentDate = new Date();
					if(currentDate != null) {
						Calendar c = Calendar.getInstance();
						c.setTime(currentDate);
						c.add(Calendar.DATE, diasCred);
						List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(c.getTime(), 0, 500, po.getAddressNumber());
						if(pc != null) {
							if(pc.size() > 0) {
								estimatedPaymentDate = pc.get(0).getPaymentDate();
							}else {
								estimatedPaymentDate = c.getTime();
							}
						}else {
							estimatedPaymentDate = c.getTime();
						}
					}*/
			

			
			// Validación con los importes del recibo:
						if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)){										
							UDC porcentajeMaxUdc = udcService.searchBySystemAndKey("PORCENTAJE", "MAX");
							UDC porcentajeMinUdc = udcService.searchBySystemAndKey("PORCENTAJE", "MIN");				
							UDC montoLimiteUdc = udcService.searchBySystemAndKey("MONTO", "LIMITE");				
							UDC montoLimiteMaxUdc = udcService.searchBySystemAndKey("MONTO", "MAX");
							UDC montoLimiteMinUdc = udcService.searchBySystemAndKey("MONTO", "MIN");
							
							double porcentajeMax = Double.valueOf(porcentajeMaxUdc.getStrValue1()) / 100;
							double porcentajeMin = Double.valueOf(porcentajeMinUdc.getStrValue1()) / 100;
							
							double montoLimite = Double.valueOf(montoLimiteUdc.getStrValue1());
							double montoLimiteMax = Double.valueOf(montoLimiteMaxUdc.getStrValue1());
							double montoLimiteMin = Double.valueOf(montoLimiteMinUdc.getStrValue1());
							
							double orderAmount = 0;
							double invoiceAmount = 0;
							
							double discount = 0;
							if(inv.getDescuento() != 0) {
								discount = inv.getDescuento();
								discount = discount * 100;
								discount = Math.round(discount);
								discount = discount /100;	
							}
							
						/*	for(Receipt r :requestedReceiptList) {
								orderAmount = orderAmount + r.getAmountReceived();
							}*/
							
							for(Receipt r :requestedReceiptList) {					
								if(isDomesticCurrency) {
									orderAmount = orderAmount + r.getAmountReceived();
								} else {
									orderAmount = orderAmount + r.getForeignAmountReceived();
								}
							}

				
							String tipoValidacion ="";
							
							double totalImporteMayor = orderAmount;
							double totalImporteMenor = orderAmount;
							
							if(montoLimite != 0) {
								if(inv.getSubTotal() >= montoLimite) {
									totalImporteMayor = totalImporteMayor + montoLimiteMax;
									totalImporteMenor = totalImporteMenor - montoLimiteMin;
									tipoValidacion = "Por Monto";
								}else {
									totalImporteMayor = totalImporteMayor + (totalImporteMayor * porcentajeMax);
									totalImporteMenor = totalImporteMenor - (totalImporteMenor * porcentajeMin);
									tipoValidacion = "Por porcentaje";
								}
							}
							
							totalImporteMayor = totalImporteMayor * 100;
							totalImporteMayor = Math.round(totalImporteMayor);
							totalImporteMayor = totalImporteMayor /100;	
							
							totalImporteMenor = totalImporteMenor * 100;
							totalImporteMenor = Math.round(totalImporteMenor);
							totalImporteMenor = totalImporteMenor /100;	
							invoiceAmount = inv.getSubTotal() - discount;
							
							if(totalImporteMayor < invoiceAmount || totalImporteMenor > invoiceAmount) {
								return "Error_33_" + tipoValidacion;
							}else {
								List<UDC> pmtTermsUdc = udcService.searchBySystem("PMTTRM");
								String pmtTermsCode = "";
								//terminos por recibos
								for(Receipt r :requestedReceiptList) {
									if(r.getPaymentTerms() != null && !"".equals(r.getPaymentTerms())) {
										for(UDC udcpmt : pmtTermsUdc) {
											if(udcpmt.getUdcKey().equals(r.getPaymentTerms().trim()) && udcpmt.getStrValue2() != null && !"".equals(udcpmt.getStrValue2())) {
												diasCred = Integer.parseInt(udcpmt.getStrValue2());
												break;
											}
										}
									} else {
										diasCred = 30;
										break;
									}
								}
								//terminos por orden
//								if(po.getPaymentTerms() != null && !"".equals(po.getPaymentTerms().trim())) {
//									for(UDC udcpmt : pmtTermsUdc) {
//										if(udcpmt.getUdcKey().equals(po.getPaymentTerms().trim()) && udcpmt.getStrValue2() != null && !"".equals(udcpmt.getStrValue2())) {
//											diasCred = Integer.parseInt(udcpmt.getStrValue2());
//											break;
//										}
//									}
//								} else {
//									diasCred = 30;
//								}
								
								Date estimatedPaymentDate = null;
								Date currentDate = new Date();
								if(currentDate != null) {
									Calendar c = Calendar.getInstance();
									c.setTime(currentDate);
									c.add(Calendar.DATE, diasCred);
									List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(c.getTime(), 0, 500, po.getAddressNumber());
									if(pc != null) {
										if(pc.size() > 0) {
											estimatedPaymentDate = pc.get(0).getPaymentDate();
										}else {
											estimatedPaymentDate = c.getTime();
										}
									}else {
										estimatedPaymentDate = c.getTime();
									}
								}

					po.setInvoiceAmount(po.getInvoiceAmount() + inv.getTotal());
			        po.setOrderStauts(AppConstants.STATUS_OC_INVOICED);
			        po.setInvoiceUploadDate(invDate);
			        po.setSentToWns(null);
			        po.setEstimatedPaymentDate(estimatedPaymentDate);
			        purchaseOrderService.updateOrders(po);
			        
			        for(Receipt r :requestedReceiptList) {
			        	r.setInvDate(invDate);
			        	r.setFolio(inv.getFolio());
			        	r.setSerie(inv.getSerie());
			        	r.setUuid(inv.getUuid());
			        	r.setEstPmtDate(estimatedPaymentDate);
						r.setStatus(AppConstants.STATUS_OC_INVOICED);
						r.setMetodoPago(inv.getMetodoPago());
						r.setFormaPago(inv.getFormaPago());
						r.setUsoCFDI(inv.getReceptor().getUsoCFDI());
					}
					purchaseOrderService.updateReceipts(requestedReceiptList);
				}
	
			}
		
		
		}else {
			
				List<UDC> pmtTermsUdc = udcService.searchBySystem("PMTTRM");
				String pmtTermsCode = "";
				
				//terminos por recibos
				for(Receipt r :requestedReceiptList) {
					if(r.getPaymentTerms() != null && !"".equals(r.getPaymentTerms())) {
						for(UDC udcpmt : pmtTermsUdc) {
							if(udcpmt.getUdcKey().equals(r.getPaymentTerms().trim()) && udcpmt.getStrValue2() != null && !"".equals(udcpmt.getStrValue2())) {
								diasCred = Integer.parseInt(udcpmt.getStrValue2());
								break;
							}
						}
					} else {
						diasCred = 30;
						break;
					}
				}
				
				///terminos por orden de pago
//				if(po.getPaymentTerms() != null && !"".equals(po.getPaymentTerms().trim())) {
//					for(UDC udcpmt : pmtTermsUdc) {
//						if(udcpmt.getUdcKey().equals(po.getPaymentTerms().trim()) && udcpmt.getStrValue2() != null && !"".equals(udcpmt.getStrValue2())) {
//							diasCred = Integer.parseInt(udcpmt.getStrValue2());
//							break;
//						}
//					}
//				} else {
//					diasCred = 30;
//				}
				
				Date estimatedPaymentDate = null;
				Date currentDate = new Date();
				if(currentDate != null) {
					Calendar c = Calendar.getInstance();
					c.setTime(currentDate);
					c.add(Calendar.DATE, diasCred);
					List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(c.getTime(), 0, 500, po.getAddressNumber());
					if(pc != null) {
						if(pc.size() > 0) {
							estimatedPaymentDate = pc.get(0).getPaymentDate();
						}else {
							estimatedPaymentDate = c.getTime();
						}
					}else {
						estimatedPaymentDate = c.getTime();
					}
				}
								
				po.setInvoiceAmount(po.getInvoiceAmount() + inv.getTotal());
		        po.setOrderStauts(AppConstants.STATUS_OC_INVOICED);
		        po.setInvoiceUploadDate(invDate);
		        po.setSentToWns(null);
		        po.setEstimatedPaymentDate(estimatedPaymentDate);
		        purchaseOrderService.updateOrders(po);
		        
		        for(Receipt r :requestedReceiptList) {
		        	r.setInvDate(invDate);
		        	r.setFolio(inv.getFolio());
		        	r.setSerie(inv.getSerie());
		        	r.setUuid(inv.getUuid());
		        	r.setEstPmtDate(estimatedPaymentDate);
					r.setStatus(AppConstants.STATUS_OC_INVOICED);
					r.setMetodoPago(inv.getMetodoPago());
					r.setFormaPago(inv.getFormaPago());
					r.setUsoCFDI(inv.getReceptor().getUsoCFDI());
				}
				purchaseOrderService.updateReceipts(requestedReceiptList);
				sendVoucher = true;
			
		}
		//double dblAdvancePayment = currencyToDouble(advancePayment);
		if(plantCodePO!=null && !"".equals(plantCodePO)) {
		FiscalDocuments fis = new FiscalDocuments();
		fis.setAddressNumber(po.getAddressNumber());
		fis.setOrderNumber(po.getOrderNumber());
		fis.setOrderType(po.getOrderType());
		fis.setOrderCompany(po.getCompanyKey());
		fis.setDocumentNumber(selReceipt.getDocumentNumber());		
		fis.setAmount(inv.getTotal());
		fis.setSerie(inv.getFolio() != null ? inv.getSerie() : "ZX");
		fis.setFolio(inv.getFolio() != null ? inv.getFolio() : inv.getUuid().substring(inv.getUuid().length() - 4));
		fis.setImpuestos(inv.getImpuestos());
		fis.setInvoiceDate(inv.getFechaTimbrado());
		fis.setInvoiceUploadDate(new Date());
		fis.setMoneda(inv.getMoneda());
		fis.setRfcEmisor(inv.getRfcEmisor());
		fis.setRfcReceptor(inv.getRfcReceptor());
		fis.setSubtotal(inv.getSubTotal());
		fis.setDescuento(inv.getDescuento());
		fis.setType(AppConstants.STATUS_FACT);
		fis.setUuidFactura(inv.getUuid());
		fis.setConceptoArticulo("");
		//fis.setReplicationStatus("PENDING");
		fis.setTipoCambio(exchangeRate);
		//fis.setAdvancePayment(dblAdvancePayment);
		fis.setSupplierName(s.getName());
		fis.setResponsibleUser(currentUser.getUserName());//Multiusuarios
	
		String company = po.getOrderCompany();
		
		if("MX".equals(s.getCountry().trim())) {
			fis.setGlOffset(AppConstants.GL_OFFSET_DEFAULT);
		} else {
			fis.setGlOffset(AppConstants.GL_OFFSET_FOREIGN);
		}
		
		if("MXN".equals(invCurrency)) {			
			fis.setCurrencyCode("MXP");
			fis.setCurrencyMode(AppConstants.CURRENCY_MODE_DOMESTIC);
		} else {
			fis.setCurrencyCode(invCurrency);
			fis.setCurrencyMode(AppConstants.CURRENCY_MODE_FOREIGN);			
		}

		
/*		UDC companyCC = udcDao.searchBySystemAndKey("RFCCOMPANYCC", company);
		if(companyCC != null) {
			fis.setCompanyFD(company);
			fis.setCentroCostos(org.apache.commons.lang.StringUtils.leftPad(companyCC.getStrValue2(), 12, " "));
		} else {
			return "La compañía no tiene un centro de costos asignado en el portal de proveedores.";
		}
		
		String accountingAcc = "";
		List<UDC> accountingAccList = udcDao.searchBySystem("RFCACCOUNTINGACC");
		if(accountingAccList != null) {
			for(UDC udcAcc : accountingAccList) {
				if(udcAcc.getUdcKey().equals(company) && udcAcc.getStrValue1().equals(inv.getMoneda())) {
					accountingAcc = udcAcc.getStrValue2().trim();
					break;
				}
			}
		}
		
		if(!"".equals(accountingAcc)) {
			fis.setAccountingAccount(accountingAcc);
		} else {
			return "La compañía no tiene una cuenta contable asignada para la moneda " + inv.getMoneda() + " en el portal de proveedores.";
		}
		
    	boolean isTransportCB = false;
    	UDC udcTrans = udcService.searchBySystemAndKey("CUSTOMBROKER", addressBook);    	
    	if(udcTrans != null) {
			if(udcTrans.getStrValue1() != null && "Y".equals(udcTrans.getStrValue1().trim())) {
				isTransportCB = true;
			}
    	}
    	
    	UDC accountingNumber = udcDao.searchBySystemAndKey("CPTACCNUMBER", "CONCEPT000");
    	if(accountingNumber != null) {
			if(!isTransportCB) {
				fis.setAccountNumber(fis.getCentroCostos().concat(".").concat(accountingNumber.getStrValue1()));
			} else {
				fis.setAccountNumber(fis.getCentroCostos().concat(".").concat(accountingNumber.getStrValue2()));						
			}	
    	} else {
    		return "No está registrado ningún número de cuenta para agentes aduanales.";
    	}
    	
		if("MXN".equals(invCurrency)) {			
			fis.setCurrencyCode("MXP");
			fis.setCurrencyMode(AppConstants.CURRENCY_MODE_DOMESTIC);
		} else {
			fis.setCurrencyCode(invCurrency);
			fis.setCurrencyMode(AppConstants.CURRENCY_MODE_FOREIGN);			
		}
		
		

		List<UDC> comUDCList2 =  udcService.searchBySystem("COMPANYDOMESTIC");
		if(comUDCList2 != null && !comUDCList2.isEmpty()) {
			for(UDC companyCurrUDC : comUDCList2) {
				if(companyCurrUDC.getStrValue1().trim().equals(fis.getCompanyFD()) && !"".equals(companyCurrUDC.getStrValue2().trim())) {
					if(invCurrency.equals(companyCurrUDC.getStrValue2().trim())) {
						fis.setCurrencyMode(AppConstants.CURRENCY_MODE_DOMESTIC);
					} else {
						fis.setCurrencyMode(AppConstants.CURRENCY_MODE_FOREIGN);
					}
					break;
				}
			}
		}
		
		int diasCredito = 0;
		UDC pmtTermsUDC = udcDao.searchBySystemAndKey("PMTTCUSTOM", "DEFAULT");
		if(pmtTermsUDC != null) {
			fis.setPaymentTerms(pmtTermsUDC.getStrValue1());
			diasCredito = Integer.valueOf(pmtTermsUDC.getStrValue2());
		} else {
			fis.setPaymentTerms("N30");
			diasCredito = 30;
		}
		
		Date estimatedPaymentDate = null;
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();		
		c.setTime(currentDate);
		c.add(Calendar.DATE, diasCredito);
		List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(c.getTime(), 0, 500, addressBook);
		if(pc != null) {
			if(pc.size() > 0) {
				estimatedPaymentDate = pc.get(0).getPaymentDate();
			}else {
				estimatedPaymentDate = c.getTime();
			}
		}else {
			estimatedPaymentDate = c.getTime();
		}		
		fis.setEstimatedPaymentDate(estimatedPaymentDate);
		fis.setTaxCode(getInvoiceTaxCode(inv));
		*/
		
		String emailApprover = "";

		if(!AppConstants.SP2900.equals(plantCodePO) && !AppConstants.SP2802.equals(plantCodePO)) {
			
	   String currentApprover ="";
		List<UDC> approverUDCList = udcDao.advaceSearch("APPROVERPONP", "", plantCodePO,"");
		if(approverUDCList != null) {
			for(UDC approver : approverUDCList) {
				if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
					currentApprover = currentApprover.concat(approver.getStrValue1()).concat(",");;
					fis.setCurrentApprover(currentApprover);
					emailApprover = approver.getStrValue2();

					//fis.setCurrentApprover(approver.getStrValue1());
					//emailApprover = approver.getStrValue2();
					
					  try {
						  String emailContent = AppConstants.EMAIL_INV_APPROVAL_MSG_1_OC;
						  emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(fis.getOrderNumber()));
						  emailContent = emailContent.replace("_ADDNUMBER_", s.getAddresNumber());

						  
						  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						//  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_NO_OC, this.stringUtils.prepareEmailContent(AppConstants.EMAIL_INV_APPROVAL_MSG_1_NO_OC + o.getUuidFactura() + AppConstants.EMAIL_INV_APPROVAL_MSG_2_NO_OC + s.getAddresNumber() +  "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK), nextApproverEmail);
						  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_OC,
						  this.stringUtils.prepareEmailContent(emailContent + AppConstants.EMAIL_PORTAL_LINK), emailApprover);
						  emailAsyncSup.setMailSender(mailSenderObj);
						  Thread emailThreadSup = new Thread(emailAsyncSup);
						  emailThreadSup.start();
					  } catch (Exception e) {
						  log4j.error("Exception" , e);
					  }
					  
				}
		
			}
		}
		}else {
			List<UDC> approverUDCList = udcDao.advaceSearch("APPROVERPOSP", "", plantCodePO,"");
			String currentApprover ="";
			if(approverUDCList != null) {
				for(UDC approver : approverUDCList) {
					if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
						currentApprover = currentApprover.concat(approver.getStrValue1()).concat(",");;
						fis.setCurrentApprover(currentApprover);
						emailApprover = approver.getStrValue2();
						
						  try {
							  String emailContent = AppConstants.EMAIL_INV_APPROVAL_MSG_1_OC;
							  emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(fis.getOrderNumber()));
							  emailContent = emailContent.replace("_ADDNUMBER_", s.getAddresNumber());
							  
							  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
							//  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_NO_OC, this.stringUtils.prepareEmailContent(AppConstants.EMAIL_INV_APPROVAL_MSG_1_NO_OC + o.getUuidFactura() + AppConstants.EMAIL_INV_APPROVAL_MSG_2_NO_OC + s.getAddresNumber() +  "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK), nextApproverEmail);
							  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_OC, 
							  this.stringUtils.prepareEmailContent(emailContent + AppConstants.EMAIL_PORTAL_LINK), emailApprover);
							  emailAsyncSup.setMailSender(mailSenderObj);
							  Thread emailThreadSup = new Thread(emailAsyncSup);
							  emailThreadSup.start();
						  } catch (Exception e) {
							  log4j.error("Exception" , e);
						  }
						  
					}
					
					/*if(AppConstants.INV_SECOND_APPROVER.equals(approver.getUdcKey())){
						if(AppConstants.INV_SECOND_APPROVER.equals(approver.getUdcKey())){
							fis.setNextApprover(approver.getStrValue1());
						}				
						}	*/
			
				}
			}
			
				
			}
			
		
		
		
		
		fis.setStatus(AppConstants.STATUS_INPROCESS);
		fis.setApprovalStatus(AppConstants.STATUS_INPROCESS);
		fis.setApprovalStep(AppConstants.FIRST_STEP);
		fis.setPlant(plantCodePO);
		
		
		fiscalDocumentService.saveDocument(fis);
		}
		
		
		if(sendVoucher) {
			try {				
				for(Receipt r :requestedReceiptList) {
					if(AppConstants.RECEIPT_CODE_RETENTION.equals(r.getReceiptType())) {
						r.setAmountReceived(r.getAmountReceived() * -1);
						r.setForeignAmountReceived(r.getForeignAmountReceived() * -1);
						r.setQuantityReceived(Math.abs(r.getQuantityReceived()));
					}
				}
				
				if(domesticCurrency.equals(invCurrency)) {
			//		resp = "DOC:" + eDIService.createNewVoucher(po, inv, 0, s, requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
				} else {
					ForeingInvoice fi = new ForeingInvoice();
					fi.setSerie(inv.getFolio() != null ? inv.getSerie() : "ZX");
					fi.setFolio(inv.getFolio() != null ? inv.getFolio() : inv.getUuid().substring(inv.getUuid().length() - 4));
					fi.setUuid(inv.getUuid());
					fi.setExpeditionDate(inv.getFechaTimbrado());
			//		resp = "DOC:" + eDIService.createNewForeignVoucher(po, fi, 0, s, requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
				}
				
				String emailContent = AppConstants.EMAIL_INVOICE_ACCEPTED;
				emailContent = emailContent.replace("_INVOICE_", po.getOrderNumber() + "-" + po.getOrderType());
				
				EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				emailAsyncSup.setProperties(AppConstants.EMAIL_INV_ACCEPT_SUP + po.getOrderNumber() + "-" + po.getOrderType(), 
				stringUtils.prepareEmailContent(emailContent + "<br /> <br />" + AppConstants.ETHIC_CONTENT),
				emailRecipient);
				emailAsyncSup.setMailSender(mailSenderObj);
				emailAsyncSup.setAdditionalReference(udcDao, po.getOrderType());
				Thread emailThreadSup = new Thread(emailAsyncSup);
				emailThreadSup.start();
				
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}

	
	@SuppressWarnings({ "unused"})
	public String validateCreditNoteFromOrder(InvoiceDTO inv,String addressBook, 
								  int documentNumber, 
								  String documentType,
								  String tipoComprobante,
								  PurchaseOrder po,
								  boolean sendVoucher,
								  String xmlContent,
								  String receiptList){
/////////////////////validacion de XML CRUZG
ResponseGeneral resps=validateInvoiceVsSat(inv,xmlContent);	
if (resps.isError()) {
return resps.getMensaje().get("es")+",Credito Factura";///devuelve solo el mensaje en español--en para ingles
}else {
	if (resps.getDocument() != null) {
		InvoiceDTO invoiceUP =null;
		String InvoiceXML=null;
		ResponseGeneral resp=null;
		
		 ByteArrayInputStream stream = new  ByteArrayInputStream(resps.getDocument());
		try {
				InvoiceXML = IOUtils.toString(stream, "UTF-8");
				InvoiceXML = takeOffBOM(IOUtils.toInputStream(InvoiceXML, "UTF-8"));
				InvoiceXML = InvoiceXML.replace("?<?xml", "<?xml");
			} catch (IOException e) {
				
				resp=new ResponseGeneral(true,"validateInvoiceVsSatXML",e);
				resp.addMensaje("es", "La factura no es  XML valido.");
				resp.addMensaje("en", "The invoice is not XML valid.");
				log4j.error("Exception" , e);
				e.printStackTrace();
				return resp.getMensaje().get("es");
				
			}
		try {

				invoiceUP =getInvoiceXmlFromString(InvoiceXML);
			} catch (Exception e) {
				
				resp=new ResponseGeneral(true,"validateInvoiceVsSatDTO",e);
				resp.addMensaje("es", "La factura no es  XML valido.");
				resp.addMensaje("en", "The invoice is not XML valid.");
				log4j.error("Exception" , e);
				e.printStackTrace();
				return resp.getMensaje().get("es");
				
			}
		inv=invoiceUP;
		xmlContent=InvoiceXML;
	}
	
	
	
}



		String invCurrency = inv.getMoneda().trim();
		double exchangeRate = inv.getTipoCambio();
		
		UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");
		if(udcCfdi != null) {
			if(!"".equals(udcCfdi.getStrValue1())) {
				if("TRUE".equals(udcCfdi.getStrValue1())) {
					String vcfdi = validaComprobanteSAT(inv);
					if(!"".equals(vcfdi)) {
						return "Error de validación ante el SAT, favor de validar con su emisor fiscal.";
					}
				}
			}
		}else {
			String vcfdi = validaComprobanteSAT(inv);
			if(!"".equals(vcfdi)) {
				return "Error de validación ante el SAT, favor de validar con su emisor fiscal.";
			}
		}
		
		String rfcEmisor = inv.getRfcEmisor();
		boolean allRules = true;
		List<UDC> supExclList =  udcService.searchBySystem("NOCHECKSUP");
		if(supExclList != null) {
			for(UDC udc : supExclList) {
				if(rfcEmisor.equals(udc.getStrValue1())){
					allRules = false;
					break;
				}
			}
		}
		
		String resp = "";
		Supplier s = supplierService.searchByAddressNumber(addressBook);
		if(s == null) {
			return "El proveedor no existe en la base de datos";
		}
		String emailRecipient = (s.getEmailSupplier());
		List<Receipt> requestedReceiptList = null;
		List<PurchaseOrderDetail> requestedPurchaseDetailList = purchaseOrderService.getPurchaseOrderDetailByIds(receiptList);
		//List<Receipt> receiptArray= purchaseOrderService.getNegativeOrderReceipts(documentNumber, addressBook, documentType,"");
		List<Receipt> receiptArray= purchaseOrderService.getReceiptsByUUID(inv.getCfdiRelacionado());
		
		if(receiptArray==null||receiptArray.size()==0) {
			return "El CFDI relacionado a su Nota no se encuentra CARGADO";
		}
		
		for (Receipt receipt : receiptArray) {
			if(receipt.getStatus().equals(AppConstants.STATUS_OC_PAID)) {
				return "El CFDI relacionado a su Nota se encuentra PAGADO";
			}
		}
		
		PurchaseOrder poOriginal=purchaseOrderService.getOrderByOrderAndAddresBook(receiptArray.get(0).getOrderNumber(), addressBook, receiptArray.get(0).getOrderType());
		
		
		String fechaFactura = inv.getFechaTimbrado();
		fechaFactura = fechaFactura.replace("T", " ");
		SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
		Date invDate = null;
		try {
			invDate = sdf.parse(fechaFactura);
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		
		String domesticCurrency = AppConstants.DEFAULT_CURRENCY;
		List<UDC> comUDCList =  udcService.searchBySystem("COMPANYDOMESTIC");
		if(comUDCList != null && !comUDCList.isEmpty()) {
			for(UDC company : comUDCList) {
				if(company.getStrValue1().trim().equals(po.getOrderCompany().trim()) && !"".equals(company.getStrValue2().trim())) {
					domesticCurrency = company.getStrValue2().trim();
					break;
				}
			}
		}
		
		if(allRules) {		
		if(!AppConstants.DEFAULT_CURRENCY.equals(invCurrency)) {			
			if(exchangeRate == 0) {
				return "La moneda de la nota de crédito es " + invCurrency + " sin embargo, no existe definido un tipo de cambio.";
			}
		}
		
		String oCurr = "";
		if("MXP".equals(po.getCurrecyCode())) {
			oCurr = "MXN";
		}else {
			oCurr = po.getCurrecyCode();
		}
		if(!invCurrency.equals(oCurr)) {
			return "La moneda de la nota de crédito es " + invCurrency + " sin embargo, el código de moneda de la orden de compra es " + oCurr;
		}
		
		
		
		String strStartSkipDateIssuance=null;
		String strFinalSkipDateIssuance = null;
		boolean skipDateIssuance  = false;
		List<UDC> skipValidationList =  udcService.searchBySystem("OMITIRVALIDACION");
		if(skipValidationList != null) {
			for(UDC udc : skipValidationList) {
				if("FECHAEMISION".equals(udc.getUdcKey()) && udc.isBooleanValue()){
					skipDateIssuance = udc.isBooleanValue();
					strStartSkipDateIssuance = udc.getStrValue1();
					strFinalSkipDateIssuance = udc.getStrValue2();
					break;
				}
			}
		}
		
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, currentYear);
		cal.set(Calendar.DAY_OF_YEAR, 1);  
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date startYear = cal.getTime();
		try {
			if(invDate.compareTo(startYear) < 0) {
				if(skipDateIssuance) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date dateStartSkipDateIssuance = formatter.parse(strStartSkipDateIssuance);
					Date dateFinalSkipDateIssuance = formatter.parse(strFinalSkipDateIssuance);
					if(invDate.compareTo(dateStartSkipDateIssuance) < 0 || invDate.compareTo(dateFinalSkipDateIssuance)>0) {
						return "msgErrorPO4";
					}
				}else {
					return "msgErrorPO4";
				}
			}
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return "msgErrorPO5";
		}
		
		
		
		
		
		
	/*	
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, currentYear);
		cal.set(Calendar.DAY_OF_YEAR, 1);   
		
		Date startYear = cal.getTime();
		try {
			if(invDate.compareTo(startYear) < 0) {
				return "La fecha de emisión de la factura no puede ser anterior al primero de Enero del año actual";
			}
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return "Error al obtener la fecha de timbrado de la factura";
		}

*/
		if(receiptArray != null) {
			String[] idList = receiptList.split(",");
			
			requestedReceiptList = new ArrayList<Receipt>();
			for(Receipt r : receiptArray) {
					requestedReceiptList.add(r);	
			}
			
		
			
			if(requestedReceiptList.isEmpty()) {
				return "No existen recibos para cargar la nota de crédito";
			}			
		}else {
			return "No existen recibos para cargar la nota de crédito";
	    }
				
		String cfdiRel = inv.getCfdiRelacionado();
		if(cfdiRel != null) {
			List<Receipt> invReceipts = purchaseOrderService.getSuplierInvoicedReceipts(addressBook, cfdiRel);
			if(invReceipts == null) {
				return "No existe la factura relacionada al CFDI de la nota de crédito";
			}else {
				if(invReceipts.size() > 0) {
					double invTotalAmt = 0;
					double invForeignTotalAmt = 0;
					double credTotalAmt = 0;					
					credTotalAmt = inv.getSubTotal();
					
					for(Receipt r : invReceipts) {
						invTotalAmt = invTotalAmt + r.getAmountReceived();
						invForeignTotalAmt = invForeignTotalAmt + r.getForeignAmountReceived();
					}
					if(domesticCurrency.equals(invCurrency)) {
						if(invTotalAmt < credTotalAmt) {
							return "El total de la nota de crédito excede el total de las facturas correlacionadas";
						}
					} else {
						if(invForeignTotalAmt < credTotalAmt) {
							return "El total de la nota de crédito excede el total de las facturas correlacionadas";
						}
					}
				}else {
					return "No existe la factura relacionada al CFDI de la nota de crédito";
				}
			}
		}else {
			return "No existe CFDI relacionado en el documento";
		}
		// Valida subtotales
		
		if(s != null) {
			NonComplianceSupplier ncs = this.nonComplianceSupplierService.searchByTaxId(s.getRfc(), 0, 0);
		    if (ncs != null && (
		      ncs.getRefDate1().contains("Definitivo") || 
		      ncs.getRefDate1().contains("Presunto") || 
		      ncs.getRefDate1().contains("Desvirtuado") || 
		      ncs.getRefDate2().contains("Definitivo") || 
		      ncs.getRefDate2().contains("Presunto") || 
		      ncs.getRefDate2().contains("Desvirtuado") || 
		      ncs.getStatus().contains("Definitivo") || 
		      ncs.getStatus().contains("Presunto") || 
		      ncs.getStatus().contains("Desvirtuado"))) {
		    	
		    	String altEmail = "";
					List<UDC> udcList =  udcService.searchBySystem("TAXALTEMAIL");
					if(udcList != null) {
						String emailContent = AppConstants.EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER;
						emailContent = emailContent.replace("_ADDNUMBER_", s.getAddresNumber());
						emailContent = emailContent.replace("_RAZONSOCIAL_", s.getRazonSocial());

						
						altEmail = udcList.get(0).getStrValue1();
					    EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						emailAsyncSup.setProperties(
								AppConstants.EMAIL_NO_COMPLIANCE_INVOICE,
								emailContent,
								altEmail);
						emailAsyncSup.setMailSender(mailSenderObj);
						Thread emailThreadSup = new Thread(emailAsyncSup);
						emailThreadSup.start();
					}
		    	
					 EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
					 emailAsyncSup.setProperties(
								AppConstants.EMAIL_INVOICE_REJECTED + " " + po.getOrderNumber() ,
								AppConstants.EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER_NOTIF,
								altEmail);
						emailAsyncSup.setMailSender(mailSenderObj);
						Thread emailThreadSup = new Thread(emailAsyncSup);
						emailThreadSup.start();
					
					return "Los registros indican que cuenta con problemas fiscales y no se podrán cargar facturas en este momento.";

		    } 
		}else {
			return "El proveedor no existe en el catálogo de la aplicación";
		}
		
		
		String buyerEmail = po.getEmail();
		cal = Calendar.getInstance();
		invDate = null;
		Date orderDate = null;
		try {
			fechaFactura = inv.getFechaTimbrado();
			fechaFactura = fechaFactura.replace("T", " ");
			sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
			invDate = sdf.parse(fechaFactura);
			orderDate = poOriginal.getDateRequested();
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		
		UDC udcDate = udcService.searchBySystemAndKey(AppConstants.NO_VALIDATE_DATE, "SKIP");
//		no validar decha de nota de credito
//		if(udcDate != null) {
//			if(!udcDate.getStrValue1().equals(s.getTipoProductoServicio())){
//				if(invDate.before(orderDate)) {
//					return "Error: La fecha de la nota de crédito no puede ser anterior a la fecha de emisión de la orden.";
//				}
//			}
//		}

		
		if(rfcEmisor != null) {
			if(!"".equals(rfcEmisor)) {
				if(!s.getRfc().equals(rfcEmisor)) {
					return "La nota de crédito ingresada no pertenece al RFC del emisor del proveedor registrado como " + s.getRfc();
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
			return "El RFC receptor " + inv.getRfcReceptor() + " no es permitido para carga de documentos fiscales.";
		}
				
		// Validación los importes del recibo:		
		if(AppConstants.NC_FIELD.equals(tipoComprobante)){
			Users u = usersService.searchCriteriaUserName(po.getAddressNumber());
			
			UDC porcentajeMaxUdc = udcService.searchBySystemAndKey("PORCENTAJE", "MAX");
			UDC porcentajeMinUdc = udcService.searchBySystemAndKey("PORCENTAJE", "MIN");
			
			UDC montoLimiteUdc = udcService.searchBySystemAndKey("MONTO", "LIMITE");
			
			UDC montoLimiteMaxUdc = udcService.searchBySystemAndKey("MONTO", "MAX");
			UDC montoLimiteMinUdc = udcService.searchBySystemAndKey("MONTO", "MIN");
			
			double porcentajeMax = Double.valueOf(porcentajeMaxUdc.getStrValue1()) / 100;
			double porcentajeMin = Double.valueOf(porcentajeMinUdc.getStrValue1()) / 100;
			
			double montoLimite = Double.valueOf(montoLimiteUdc.getStrValue1());
			double montoLimiteMax = Double.valueOf(montoLimiteMaxUdc.getStrValue1());
			double montoLimiteMin = Double.valueOf(montoLimiteMinUdc.getStrValue1());
			
			double orderAmount = 0;
			double foreignOrderAmount = 0;
			double invoiceAmount = 0;			
			double discount = 0;

			for(PurchaseOrderDetail r :requestedPurchaseDetailList) {
				orderAmount = orderAmount + Math.abs(r.getAmount());
				foreignOrderAmount = foreignOrderAmount + Math.abs(r.getForeignAmount());
			}

			String tipoValidacion ="";			
			double totalImporteMayor = 0;
			double totalImporteMenor = 0;
			double invoiceTotalAmount = 0;
			
			if(domesticCurrency.equals(invCurrency)) {
				totalImporteMayor = orderAmount;
				totalImporteMenor = orderAmount;
			} else {
				totalImporteMayor = foreignOrderAmount;
				totalImporteMenor = foreignOrderAmount;
			}
			
			invoiceTotalAmount = inv.getSubTotal();
			if(montoLimite != 0) {
				if(invoiceTotalAmount >= montoLimite) {
					totalImporteMayor = totalImporteMayor + montoLimiteMax;
					totalImporteMenor = totalImporteMenor - montoLimiteMin;
					tipoValidacion = "Por Monto";
				}else {
					totalImporteMayor = totalImporteMayor + (totalImporteMayor * porcentajeMax);
					totalImporteMenor = totalImporteMenor - (totalImporteMenor * porcentajeMin);
					tipoValidacion = "Por porcentaje";
				}
			}
			
			totalImporteMayor = totalImporteMayor * 100;
			totalImporteMayor = Math.round(totalImporteMayor);
			totalImporteMayor = totalImporteMayor /100;	
			
			totalImporteMenor = totalImporteMenor * 100;
			totalImporteMenor = Math.round(totalImporteMenor);
			totalImporteMenor = totalImporteMenor /100;				
			
			if(inv.getDescuento() != 0) {
				discount = inv.getDescuento();
				discount = discount * 100;
				discount = Math.round(discount);
				discount = discount /100;	
			}
			invoiceAmount = inv.getSubTotal() - discount;
			invoiceAmount = invoiceAmount * 100;
			invoiceAmount = Math.round(invoiceAmount);
			invoiceAmount = invoiceAmount/100;
			
			if(totalImporteMayor < invoiceAmount || totalImporteMenor > invoiceAmount) {
				return "El total de la nota de crédito no coincide con el total de los recibos seleccionados. Tipo de validación:" + tipoValidacion;
				
			}else {				
				po.setInvoiceAmount(po.getInvoiceAmount() + inv.getTotal());
		        po.setOrderStauts(AppConstants.STATUS_OC_INVOICED);
		        po.setEstimatedPaymentDate(poOriginal.getEstimatedPaymentDate());
		        po.setInvoiceUploadDate(invDate);
		        po.setSentToWns(null);
		        
		        
		        
		        
		        purchaseOrderService.updateOrders(po);
		        
		        for(Receipt r :requestedReceiptList) {
//		        	r.setQuantityReceived(Math.abs(r.getQuantityReceived()));
//		        	r.setInvDate(invDate);
//		        	r.setFolio(inv.getFolio());
//		        	r.setSerie(inv.getSerie());
		        	r.setCreditNoteUuid(inv.getUuid());
					r.setStatus(AppConstants.STATUS_OC_INVOICED);
				}
				purchaseOrderService.updateReceipts(requestedReceiptList);
				
				
				for(PurchaseOrderDetail r :requestedPurchaseDetailList) {
					r.setStatus(AppConstants.STATUS_OC_INVOICED);
					purchaseOrderService.updatePurchaseOrderDetail(r);
				}
				
			}

		}
		}
		
		
		FiscalDocuments fis = new FiscalDocuments();
		fis.setAddressNumber(po.getAddressNumber());
		fis.setOrderNumber(po.getOrderNumber());
		fis.setOrderType(po.getOrderType());
		fis.setOrderCompany(po.getCompanyKey());
//		fis.setDocumentNumber(po.get);		
		fis.setAmount(inv.getTotal());
		fis.setSerie(inv.getFolio() != null ? inv.getSerie() : "ZX");
		fis.setFolio(inv.getFolio() != null ? inv.getFolio() : inv.getUuid().substring(inv.getUuid().length() - 4));
		fis.setImpuestos(inv.getImpuestos());
		fis.setInvoiceDate(inv.getFechaTimbrado());
		fis.setInvoiceUploadDate(new Date());
		fis.setMoneda(inv.getMoneda());
		fis.setRfcEmisor(inv.getRfcEmisor());
		fis.setRfcReceptor(inv.getRfcReceptor());
		fis.setSubtotal(inv.getSubTotal());
		fis.setDescuento(inv.getDescuento());
		fis.setType(AppConstants.NC_FIELD_UDC);
		fis.setUuidFactura(inv.getUuid());
		fis.setConceptoArticulo("");
		fis.setReplicationStatus("PENDING");
		fis.setTipoCambio(exchangeRate);
		//fis.setAdvancePayment(dblAdvancePayment);
		fis.setSupplierName(s.getName());	
	
		String company = po.getOrderCompany();
		
		if("MX".equals(s.getCountry().trim())) {
			fis.setGlOffset(AppConstants.GL_OFFSET_DEFAULT);
		} else {
			fis.setGlOffset(AppConstants.GL_OFFSET_FOREIGN);
		}
		
		if("MXN".equals(invCurrency)) {			
			fis.setCurrencyCode("MXP");
			fis.setCurrencyMode(AppConstants.CURRENCY_MODE_DOMESTIC);
		} else {
			fis.setCurrencyCode(invCurrency);
			fis.setCurrencyMode(AppConstants.CURRENCY_MODE_FOREIGN);			
		}
		
		FiscalDocuments fiscalOrigen=fiscalDocumentService.getFiscalDocumentsByUuid(inv.getCfdiRelacionado());
		String plantCodePO = fiscalOrigen.getPlant();
		
		
		String emailApprover = "";

		if(!AppConstants.SP2900.equals(plantCodePO) && !AppConstants.SP2802.equals(plantCodePO)) {
			
	   String currentApprover ="";
		List<UDC> approverUDCList = udcDao.advaceSearch("APPROVERPONP", "", plantCodePO,"");
		if(approverUDCList != null) {
			for(UDC approver : approverUDCList) {
				if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
					currentApprover = currentApprover.concat(approver.getStrValue1()).concat(",");;
					fis.setCurrentApprover(currentApprover);
					emailApprover = approver.getStrValue2();

					//fis.setCurrentApprover(approver.getStrValue1());
					//emailApprover = approver.getStrValue2();
					
					  try {
						  String emailContent = AppConstants.EMAIL_INV_APPROVAL_MSG_1_OP;
						  emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(fis.getOrderNumber()));
						  emailContent = emailContent.replace("_ADDNUMBER_", s.getAddresNumber());

						  
						  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						//  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_NO_OC, this.stringUtils.prepareEmailContent(AppConstants.EMAIL_INV_APPROVAL_MSG_1_NO_OC + o.getUuidFactura() + AppConstants.EMAIL_INV_APPROVAL_MSG_2_NO_OC + s.getAddresNumber() +  "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK), nextApproverEmail);
						  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_OP,
						  this.stringUtils.prepareEmailContent(emailContent + AppConstants.EMAIL_PORTAL_LINK), emailApprover);
						  emailAsyncSup.setMailSender(mailSenderObj);
						  Thread emailThreadSup = new Thread(emailAsyncSup);
						  emailThreadSup.start();
					  } catch (Exception e) {
						  log4j.error("Exception" , e);
					  }
					  
				}
		
			}
		}
		}else {
			List<UDC> approverUDCList = udcDao.advaceSearch("APPROVERPOSP", "", plantCodePO,"");
			String currentApprover ="";
			if(approverUDCList != null) {
				for(UDC approver : approverUDCList) {
					if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
						currentApprover = currentApprover.concat(approver.getStrValue1()).concat(",");;
						fis.setCurrentApprover(currentApprover);
						emailApprover = approver.getStrValue2();
						
						  try {
							  String emailContent = AppConstants.EMAIL_INV_APPROVAL_MSG_1_OP;
							  emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(fis.getOrderNumber()));
							  emailContent = emailContent.replace("_ADDNUMBER_", s.getAddresNumber());
							  
							  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
							//  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_NO_OC, this.stringUtils.prepareEmailContent(AppConstants.EMAIL_INV_APPROVAL_MSG_1_NO_OC + o.getUuidFactura() + AppConstants.EMAIL_INV_APPROVAL_MSG_2_NO_OC + s.getAddresNumber() +  "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK), nextApproverEmail);
							  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_OP, 
							  this.stringUtils.prepareEmailContent(emailContent + AppConstants.EMAIL_PORTAL_LINK), emailApprover);
							  emailAsyncSup.setMailSender(mailSenderObj);
							  Thread emailThreadSup = new Thread(emailAsyncSup);
							  emailThreadSup.start();
						  } catch (Exception e) {
							  log4j.error("Exception" , e);
						  }
						  
					}
			
				}
			}
			
				
			}
		
		
		
		
		/*String emailApprover="";
		   String currentApprover ="";
			List<UDC> approverUDCList = udcDao.searchBySystem("APROVERCREDNOTE");
			if(approverUDCList != null) {
				for(UDC approver : approverUDCList) {
					if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
						currentApprover = currentApprover.concat(approver.getStrValue1()).concat(",");;
						fis.setCurrentApprover(currentApprover);
						emailApprover = approver.getStrValue2();

						//fis.setCurrentApprover(approver.getStrValue1());
						//emailApprover = approver.getStrValue2();
						
						  try {
							  String emailContent = AppConstants.EMAIL_INV_APPROVAL_MSG_1_OC;
							  emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(fis.getOrderNumber()));
							  emailContent = emailContent.replace("_ADDNUMBER_", s.getAddresNumber());

							  
							  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
							//  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_NO_OC, this.stringUtils.prepareEmailContent(AppConstants.EMAIL_INV_APPROVAL_MSG_1_NO_OC + o.getUuidFactura() + AppConstants.EMAIL_INV_APPROVAL_MSG_2_NO_OC + s.getAddresNumber() +  "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK), nextApproverEmail);
							  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_OC,
							  this.stringUtils.prepareEmailContent(emailContent + AppConstants.EMAIL_PORTAL_LINK), emailApprover);
							  emailAsyncSup.setMailSender(mailSenderObj);
							  Thread emailThreadSup = new Thread(emailAsyncSup);
							  emailThreadSup.start();
						  } catch (Exception e) {
							  log4j.error("Exception" , e);
						  }
						  
					}
			
				}
			}*/
		
			fis.setStatus(AppConstants.STATUS_INPROCESS);
			fis.setApprovalStatus(AppConstants.STATUS_INPROCESS);
			fis.setApprovalStep(AppConstants.FIRST_STEP);
			fis.setPlant(plantCodePO);
			
			fiscalDocumentService.saveDocument(fis);
		
		/*if(sendVoucher) {
			try {				
				if(domesticCurrency.equals(invCurrency)) {
					resp = "DOC:" + eDIService.createNewVoucher(po, inv, 0, s, requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
				} else {
					ForeingInvoice fi = new ForeingInvoice();
					fi.setSerie(inv.getSerie());
					fi.setFolio(inv.getFolio());
					fi.setUuid(inv.getUuid());
					fi.setExpeditionDate(inv.getFechaTimbrado());
					resp = "DOC:" + eDIService.createNewForeignVoucher(po, fi, 0, s, requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
				}
				
				String emailContent = AppConstants.EMAIL_CN_ACCEPTED;
				emailContent = emailContent.replace("_DATA_", po.getOrderNumber() + "-" + po.getOrderType());
				
				EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				emailAsyncSup.setProperties(AppConstants.EMAIL_NC_ACCEPT_SUP + po.getOrderNumber() + "-" + po.getOrderType(), 
				stringUtils.prepareEmailContent(emailContent + "<br /> <br />" + AppConstants.ETHIC_CONTENT),
				emailRecipient);
				emailAsyncSup.setMailSender(mailSenderObj);
				emailAsyncSup.setAdditionalReference(udcDao, po.getOrderType());
				Thread emailThreadSup = new Thread(emailAsyncSup);
				emailThreadSup.start();				
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return "";
			}
		}*/
		return "DOC:"+fis.getId();
	}
	
	@SuppressWarnings("unused")
	public String validateInvoiceFromOrderWitCreditNote(InvoiceDTO inv, InvoiceDTO cn, 
								                     String addressBook, 
													 int documentNumber, 
													 String documentType,
													 String tipoComprobante,
													 PurchaseOrder po,
													 boolean sendVoucher,
													 String xmlInvContent,
													 String xmlNCContent){
/////////////////////validacion de XML CRUZG
		ResponseGeneral resps=validateInvoiceVsSat(inv,xmlInvContent);	
		if (resps.isError()) {
		return resps.getMensaje().get("es")+",Credito Factura";///devuelve solo el mensaje en español--en para ingles
		}else {
			if (resps.getDocument() != null) {
				InvoiceDTO invoiceUP =null;
				String InvoiceXML=null;
				ResponseGeneral resp=null;
				
				 ByteArrayInputStream stream = new  ByteArrayInputStream(resps.getDocument());
				try {
						InvoiceXML = IOUtils.toString(stream, "UTF-8");
						InvoiceXML = takeOffBOM(IOUtils.toInputStream(InvoiceXML, "UTF-8"));
						InvoiceXML = InvoiceXML.replace("?<?xml", "<?xml");
					} catch (IOException e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatXML",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp.getMensaje().get("es");
						
					}
				try {

						invoiceUP =getInvoiceXmlFromString(InvoiceXML);
					} catch (Exception e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatDTO",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp.getMensaje().get("es");
						
					}
				inv=invoiceUP;
				xmlInvContent=InvoiceXML;
			}
			
			
			
		}

		
		 resps=validateInvoiceVsSat(cn,xmlNCContent);	
		if (resps.isError()) {
		return resps.getMensaje().get("es")+",Credito Nota";///devuelve solo el mensaje en español--en para ingles
		}else {
			if (resps.getDocument() != null) {
				InvoiceDTO invoiceUP =null;
				String InvoiceXML=null;
				ResponseGeneral resp=null;
				
				 ByteArrayInputStream stream = new  ByteArrayInputStream(resps.getDocument());
				try {
						InvoiceXML = IOUtils.toString(stream, "UTF-8");
						InvoiceXML = takeOffBOM(IOUtils.toInputStream(InvoiceXML, "UTF-8"));
						InvoiceXML = InvoiceXML.replace("?<?xml", "<?xml");
					} catch (IOException e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatXML",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp.getMensaje().get("es");
						
					}
				try {

						invoiceUP =getInvoiceXmlFromString(InvoiceXML);
					} catch (Exception e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatDTO",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp.getMensaje().get("es");
						
					}
				cn=invoiceUP;
				xmlNCContent=InvoiceXML;
			}
			
			
			
		}

		
		
		
		Supplier s = supplierService.searchByAddressNumber(addressBook);
		String resp="";
		String emailRecipient = (s.getEmailSupplier());
		
		String vcfdi = validaComprobanteSAT(inv);
		if(!"".equals(vcfdi)) {
			return vcfdi;
		}
		

		String rfcEmisor = inv.getRfcEmisor();
		if(rfcEmisor != null) {
			if(!"".equals(rfcEmisor)) {
				if(!s.getRfc().equals(rfcEmisor)) {
					return "La factura no pertenece al emisor " + s.getRfc();
				}
			}
		}
		
		rfcEmisor = cn.getRfcEmisor();
		if(rfcEmisor != null) {
			if(!"".equals(rfcEmisor)) {
				if(!s.getRfc().equals(rfcEmisor)) {
					return "La nota de crédito no pertenece al emisor " + s.getRfc();
				}
			}
		}
		

		boolean receptorFacturaValido = false;
		List<UDC> receptores = udcService.searchBySystem("RECEPTOR");
		if(receptores != null) {
			for(UDC udc : receptores) {
				if(udc.getStrValue1().equals(inv.getRfcReceptor().trim())) {
					receptorFacturaValido = true;
					break;
				}
			}
		}
		
		boolean receptorNCValido = false;
		if(receptores != null) {
			for(UDC udc : receptores) {
				if(udc.getStrValue1().equals(cn.getRfcReceptor().trim())) {
					receptorNCValido = true;
					break;
				}
			}
		}
		
		
		
		if(!receptorNCValido || !receptorFacturaValido) {
			return "El receptor de uno de los documentos no es permitido. Verifique que ambos documentos tengan Emisor/Receptor correcto.";
		}
		

		if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)){
			
			UDC udc = udcService.searchBySystemAndKey(AppConstants.INVOICE_FIELD_UDC, "MF_PAGO");
			if(udc != null){
				if(!udc.getStrValue1().equals(inv.getMetodoPago())){
					return "El método de pago de la FACTURA debe ser " + udc.getStrValue1();	
				}
				if(!udc.getStrValue2().equals(inv.getFormaPago())){
					return "La forma de pago de la FACTURA debe ser " + udc.getStrValue2();	
				}
			}else{
				return "No existe método de pago para la factura";
			}
			
			List<PurchaseOrder> list = purchaseOrderService.getPendingPaymentOrders(documentNumber, 
                    addressBook, 
                    documentType);

			if (list != null && !list.isEmpty()) {
				String str = "";
				for (PurchaseOrder o : list) {
					str = str + o.getOrderNumber() + ",";
				}
				return "Los registros detectaron que tiene las siguientes órdenes de compra PAGADAS con COMPLEMENTOS DE PAGO Pendientes: <br /> "
						+ str;
			}
			
			Users u = usersService.searchCriteriaUserName(po.getAddressNumber());
			double orderAmount = po.getOrderAmount();
			double invoiceAmount = inv.getTotal();
			double cnAmount = cn.getTotal();
			double transAmount = invoiceAmount - cnAmount;
			
			if(orderAmount != transAmount) {
				return "El valor de la factura menos la nota de crédito no coinciden con el total recibido en la orde de compra.";
			}
			
			po.setInvoiceAmount(transAmount);
	        purchaseOrderService.updateOrders(po);
	        
			if(sendVoucher) {
				
				if(AppConstants.DEFAULT_CURRENCY.equals(inv.getMoneda())) {
					resp = "DOC:" + eDIService.createNewVoucher(po, inv, 0, s, null, AppConstants.NN_MODULE_VOUCHER);
				} else {
					ForeingInvoice fi = new ForeingInvoice();
					fi.setSerie(inv.getSerie());
					fi.setFolio(inv.getFolio());
					fi.setUuid(inv.getUuid());
					fi.setExpeditionDate(inv.getFechaTimbrado());
					resp = "DOC:" + eDIService.createNewForeignVoucher(po, fi, 0, s, null, AppConstants.NN_MODULE_VOUCHER);
				}
			}
		}

		if(sendVoucher) {
			String emailContent = AppConstants.EMAIL_INVOICE_ACCEPTED;
			emailContent = emailContent.replace("_INVOICE_", po.getOrderNumber() + "-" + po.getOrderType());
			
			emailService.sendEmail(AppConstants.EMAIL_INVOICE_SUBJECT, emailContent + "<br /> <br />" + AppConstants.ETHIC_CONTENT, emailRecipient);
		}
		return "";
	}

	@SuppressWarnings("unused")
	public String processInvoiceAndCreditNoteFromOrder(InvoiceDTO inv, InvoiceDTO cn,
									                    String addressBook, 
														int documentNumber, 
														String documentType,
														PurchaseOrder po){
		
		String resp = "";
		eDIService.createJournalEntries(inv, cn, addressBook, documentNumber, documentType, po);
		return "";
	}	
	
	
	
	
	public String validateInvoiceFromOrderWithoutPayment(InvoiceDTO inv, String addressBook, int documentNumber, String documentType,
			String tipoComprobante, PurchaseOrder po, String xmlContent) {

		ResponseGeneral resps=validateInvoiceVsSat(inv,xmlContent);	
		if (resps.isError()) {
		return resps.getMensaje().get("es");///devuelve solo el mensaje en español--en para ingles
		}else {
			if (resps.getDocument() != null) {
				InvoiceDTO invoiceUP =null;
				String InvoiceXML=null;
				ResponseGeneral resp=null;
				
				 ByteArrayInputStream stream = new  ByteArrayInputStream(resps.getDocument());
				try {
						InvoiceXML = IOUtils.toString(stream, "UTF-8");
						InvoiceXML = takeOffBOM(IOUtils.toInputStream(InvoiceXML, "UTF-8"));
						InvoiceXML = InvoiceXML.replace("?<?xml", "<?xml");
					} catch (IOException e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatXML",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp.getMensaje().get("es");
						
					}
				try {

						invoiceUP =getInvoiceXmlFromString(InvoiceXML);
					} catch (Exception e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatDTO",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp.getMensaje().get("es");
						
					}
				inv=invoiceUP;
				xmlContent=InvoiceXML;
			}
			
			
			
		}

		
		Supplier s = supplierService.searchByAddressNumber(addressBook);
		String emailRecipient = (s.getEmailSupplier());

		if (AppConstants.INVOICE_FIELD.equals(tipoComprobante)) {

			String rfcEmisor = inv.getRfcEmisor();
			if (rfcEmisor != null) {
				if (!"".equals(rfcEmisor)) {
					if (!s.getRfc().equals(rfcEmisor)) {
						return "La factura no pertenece al emisor " + s.getRfc();
					}
				}
			}
			
			String vcfdi = validaComprobanteSAT(inv);
			if(!"".equals(vcfdi)) {
				return vcfdi;
			}
		}

		emailService.sendEmail(AppConstants.EMAIL_INVOICE_SUBJECT,
				AppConstants.EMAIL_INVOICE_ACCEPTED_NOPAYMENT + po.getOrderNumber() + "-" + po.getOrderType() + "<br /> <br />" + AppConstants.ETHIC_CONTENT, emailRecipient);
		return "";
	}
	
	public void rejectInvoice(String addressBook, 
			  int documentNumber, 
			  String documentType) {
		
		PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(documentNumber, addressBook, documentType);
    	po.setInvoiceUuid("");
    	po.setStatus("");
    	purchaseOrderService.updateOrders(po);
    	Supplier s = supplierService.searchByAddressNumber(addressBook);
		String emailRecipient = (s.getEmailSupplier());
    	
    	emailService.sendEmail(AppConstants.EMAIL_INVOICE_SUBJECT, AppConstants.EMAIL_INVOICE_REJECTED + po.getOrderNumber() + "-" + po.getOrderType(), emailRecipient);

	}
	
	public void updateDocumentList(List<UserDocument> list) {
		documentsDao.updateDocumentList(list);
	}
	
	public void deleteDocumentList(List<UserDocument> list) {
		documentsDao.deleteDocumentList(list);
	}
	
	public String validaComprobanteSAT(InvoiceDTO inv) {	
		String payload = PayloadProducer.getCFDIPayload(inv);
		String response = HTTPRequestService.performSoapCall(payload);
		if(response != null) {
			JSONObject xmlJSONObj = XML.toJSONObject(response, true);
			JsonElement jelement = new JsonParser().parse(xmlJSONObj.toString());
			JsonObject jobject = jelement.getAsJsonObject();
			JsonElement soapEnvelope = jobject.get("s:Envelope").getAsJsonObject().get("s:Body").getAsJsonObject().get("ConsultaResponse").getAsJsonObject();
			JsonElement result = soapEnvelope.getAsJsonObject().get("ConsultaResult");
			
			String codigoEstatus = NullValidator.isNull(result.getAsJsonObject().get("a:CodigoEstatus").toString());
			String esCancelable = NullValidator.isNull(result.getAsJsonObject().get("a:EsCancelable").toString());
			String estado = NullValidator.isNull(result.getAsJsonObject().get("a:Estado").toString());
			String estatusCancelacion = NullValidator.isNull(result.getAsJsonObject().get("a:EstatusCancelacion").toString());

			codigoEstatus = codigoEstatus.replace("\"", "").trim();
			esCancelable = esCancelable.replace("\"", "").trim();
			estado = estado.replace("\"", "").trim();
			estatusCancelacion = estatusCancelacion.replace("\"", "").trim();
			
			if(!AppConstants.CFDI_SUCCESS_MSG.equals(codigoEstatus) || !AppConstants.CFDI_SUCCESS_MSG_ACTIVE.equals(estado)) {
				String errorMsg = "El documento no es aceptado ante el SAT. Se recibe el siguiente mensaje :<br>" +
			                       " - CodigoEstatus: " + codigoEstatus + "<br />" +
			                       " - EsCancelable: " + esCancelable + "<br />" +
			                       " - Estado: " + estado + "<br />" +
			                       " - EstatusCancelacion: " + estatusCancelacion + "<br />" ;
				return errorMsg;
			}
			
		}else {
			return "Error de validación ante el SAT, favor de validar con su emisor fiscal";	
		}
		
		return "";
		
	}
	
	public String validaComprobanteSATPagos(String xmlContent) {		
		return null;	
	}


	public UserDocument searchCriteriaByOrderNumberFiscalType(int orderNumber, 
			                                              String orderType, 
			                                              String addressNumber,
			                                              String type){
		
		return documentsDao.searchCriteriaByOrderNumberFiscalType(orderNumber, orderType, addressNumber, type);		
	}

	public String validaComplementoPago(String xmlString, String addressBook, InvoiceDTO inv) {
		 
/////////////////////////// VAlidacion contra repse cruzG////
ResponseGeneral resps=validateInvoiceVsSat(inv,xmlString);	
if (resps.isError()) {
return resps.getMensaje().get("es");///devuelve solo el mensaje en español--en para ingles
}else {
	if (resps.getDocument() != null) {
		InvoiceDTO invoiceUP =null;
		String InvoiceXML=null;
		ResponseGeneral resp=null;
		
		 ByteArrayInputStream stream = new  ByteArrayInputStream(resps.getDocument());
		try {
				InvoiceXML = IOUtils.toString(stream, "UTF-8");
				InvoiceXML = takeOffBOM(IOUtils.toInputStream(InvoiceXML, "UTF-8"));
				InvoiceXML = InvoiceXML.replace("?<?xml", "<?xml");
			} catch (IOException e) {
				
				resp=new ResponseGeneral(true,"validateInvoiceVsSatXML",e);
				resp.addMensaje("es", "La factura no es  XML valido.");
				resp.addMensaje("en", "The invoice is not XML valid.");
				log4j.error("Exception" , e);
				e.printStackTrace();
				return resp.getMensaje().get("es");
				
			}
		try {

				invoiceUP =getInvoiceXmlFromString(InvoiceXML);
			} catch (Exception e) {
				
				resp=new ResponseGeneral(true,"validateInvoiceVsSatDTO",e);
				resp.addMensaje("es", "La factura no es  XML valido.");
				resp.addMensaje("en", "The invoice is not XML valid.");
				log4j.error("Exception" , e);
				e.printStackTrace();
				return resp.getMensaje().get("es");
				
			}
		inv=invoiceUP;
		xmlString=InvoiceXML;
	}
	
	
	
}


		
		List<String> orders = new ArrayList<String>();
		String res = "";
			UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");
			if(udcCfdi != null) {
				if(!"".equals(udcCfdi.getStrValue1())) {
					if("TRUE".equals(udcCfdi.getStrValue1())) {
	            		res = validaComprobanteSATPagos(xmlString);
	            		if(!"".equals(res)) {
	                    	return "El documento cargado no es aceptable ante el SAT. Verifique su archivo e intente nuevamente.";
	            		}
					}
				}
			}else {
     		res = validaComprobanteSATPagos(xmlString);
     		if(!"".equals(res)) {
             	return "El documento cargado no es aceptable ante el SAT. Verifique su archivo e intente nuevamente.";
     		}
			}
			
 		Supplier s = supplierService.searchByAddressNumber(addressBook);
 		
 		String rfcEmisor = inv.getRfcEmisor();
 		if(rfcEmisor != null) {
 			if(!"".equals(rfcEmisor)) {
 				if(!s.getRfc().equals(rfcEmisor)) {
                  return "El complemento de pago no pertenece al emisor " + s.getRfc();
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
         	return "El receptor " + inv.getRfcReceptor() + " no es permitido para carga de complementos";
 		}
     	
 		List<Pago> pagos = inv.getComplemento().getPago().getPago();
 		for(Pago p : pagos) {
 			List<DoctoRelacionado> dList = p.getDoctoRelacionado();
     		double montoPago = Double.valueOf(p.getMonto());
				double totalPropinas = 0;
				double montoFactura = 0;
				String uuid = "";
 			for(DoctoRelacionado d : dList) {
 				uuid = d.getIdDocumento().trim();
 				PurchaseOrder po = purchaseOrderService.searchbyOrderUuid(uuid);
 				if(po == null) {
                 	return "Error: El uuid " + uuid + " contenido en el complemento, no tiene una factura relacionada. Verifique que su complemento de pago contenga las facturas que previamenta ha enviado a CRYOINFRA";
 				}else {
 	 				orders.add(String.valueOf(po.getId()));
 					if(!d.getMonedaDR().equals(po.getCurrecyCode())) {
	                    return "Error: La clave de moneda para el " + uuid + " son diferentes en el complemento y la factura.";
 					}
 					if(!d.getMetodoDePagoDR().equals(po.getPaymentType())) {
	                    return "Error: El metodo de pago para el " + uuid + " son diferentes en el complemento y la factura";
 					}
 					
 					String subStr = "";
 					for(PurchaseOrderDetail pd : po.getPurchaseOrderDetail()) {
 						subStr= pd.getItemDescription().trim();
 						if(subStr.contains(AppConstants.PROPINA_TEXT)) {
 							totalPropinas = totalPropinas + pd.getExtendedPrice();
 						}    						
 					}
 					
 					montoFactura = montoFactura + po.getInvoiceAmount() - po.getRelievedAmount();
					montoFactura = montoFactura * 100;
					montoFactura = (double) Math.round(montoFactura);
					montoFactura = montoFactura /100;
 					
 					if(d.getImpPagado() != null) {
 						if(!"".equals(d.getImpPagado())) {
 							Double impPagado = Double.valueOf(d.getImpPagado());
 							impPagado = impPagado * 100;
 							impPagado = (double) Math.round(impPagado);
 							impPagado = impPagado /100;
 							
 							double currentInvoiceAmount = po.getInvoiceAmount() - po.getRelievedAmount();
							currentInvoiceAmount = currentInvoiceAmount * 100;
							currentInvoiceAmount = (double) Math.round(currentInvoiceAmount);
							currentInvoiceAmount = currentInvoiceAmount /100;
 							
							if(impPagado != currentInvoiceAmount) {
     	                    	return "Error: El importe de la línea de pago para el " + uuid + " es diferente en el complemento y la factura. Importe Complemento=" + impPagado + " / Importe Factura=" + po.getInvoiceAmount();
         					}
 						}
 					}else {
	                    return "Error: El valor del importe pagado es incorrecto";
 					}
 				}
 			}

 			montoFactura = montoFactura + totalPropinas;
 			montoFactura = montoFactura * 100;
 			montoFactura = (double) Math.round(montoFactura);
 			montoFactura = montoFactura /100;
 			if(montoFactura != montoPago) {
             	return "Error: El importe total de pago del complemento es diferente al total de sus facturas considerando sus propinas. UUID: " + uuid + " /  Total Complemento:" + montoPago + " / Total Facturas:" + montoFactura;
 			}
 
 		}
 		
 		if("".equals(res) && orders.size() > 0){
    		
    		for(String i : orders) {
    			int id = Integer.valueOf(i);
    			PurchaseOrder po = purchaseOrderService.getOrderById(id);
    			
    			if(po.getPaymentUuid() == null) {
    				po.setPaymentUuid("");
    			}
    			
    			if("".equals(po.getPaymentUuid())) {
    				UserDocument doc = new UserDocument(); 
                	doc.setAddressBook(po.getAddressNumber());
                	doc.setDocumentNumber(po.getOrderNumber());
                	doc.setDocumentType(po.getOrderType());
                	doc.setContent(xmlString.getBytes());
                	doc.setName("COMPL_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + ".xml");
                	doc.setSize(xmlString.length());
                	doc.setStatus(true);
                	doc.setAccept(true);
                	doc.setFiscalType("ComplementoPago");
                	doc.setType("text/plain");
                	//doc.setType(tipoComprobante);
                	doc.setFolio(inv.getFolio());
                	doc.setSerie(inv.getSerie());
                	doc.setUuid(inv.getUuid());
                	doc.setUploadDate(new Date());
                	doc.setFiscalRef(0);
                	save(doc, new Date(), "");
                	
                	po.setPaymentUuid(inv.getUuid());
                	po.setStatus(AppConstants.STATUS_LOADCP);
                	po.setOrderStauts(AppConstants.STATUS_OC_PAYMENT_COMPL);

                	purchaseOrderService.updateOrders(po);
    			}else {
    				return "Ya se ha cargado el complemento de pago anteriormente";
    			}
    		} 
    	}
 		

		return "";
	}

	private byte[] getByteArrayFromZipInputStream(String fileName, String fileExt, int fileSize, ZipInputStream zis) {
		byte[] byteArrayFile;
		
		try {
	    	File tempFile = File.createTempFile(fileName, ".".concat(fileExt));
	        FileOutputStream out = new FileOutputStream(tempFile.getPath());
	        byte[] byteBuff = new byte[fileSize];
	        int bytesRead = 0;
	        
	        while ((bytesRead = zis.read(byteBuff)) != -1)
	        {
	            out.write(byteBuff, 0, bytesRead);
	        }
	        
	    	byteArrayFile = Files.readAllBytes(tempFile.toPath());	    	
	    	tempFile.delete();
	        out.close();
	        zis.closeEntry();
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			byteArrayFile = null;
		}
		
		return byteArrayFile;
	}

	@SuppressWarnings("unused")
	public void restoreInvoice(List<InvoiceRequestDTO> list) {
		
		try {
			for(InvoiceRequestDTO request : list) {
				log4j.info("\n" + request.toString());
				
				PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(request.getDocumentNumber(), request.getAddressBook(), request.getDocumentType());
				Supplier s = supplierService.searchByAddressNumber(request.getAddressBook());				
				List<UserDocument> docs = documentsDao.searchCriteria(request.getUuid());
				
				InvoiceDTO inv = new InvoiceDTO();
				for(UserDocument doc : docs) {					
					if(doc.getType().trim().equals("text/xml")) {
						ByteArrayInputStream stream = new  ByteArrayInputStream(doc.getContent());
						String xmlContent = IOUtils.toString(stream, "UTF-8");
						xmlContent = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
						xmlContent = xmlContent.replace("?<?xml", "<?xml");
						
						if(xmlContent.contains(AppConstants.NAMESPACE_CFDI_V4)) {
							inv = xmlToPojoService.convertV4(xmlContent);
						} else {
							inv = xmlToPojoService.convert(xmlContent);
						}
						
						log4j.info("Se crea InvoiceDTO..");
						break;
					}
				}
				
				List<Receipt> requestedReceiptList = null;
				List<Receipt> receiptArray= purchaseOrderService.getOrderReceipts(request.getDocumentNumber(), request.getAddressBook(), request.getDocumentType(),po.getOrderCompany());
				
				if(receiptArray != null) {
					//String[] idList = request.getReceiptIdList().split(",");
					requestedReceiptList = new ArrayList<Receipt>();
					for(Receipt r : receiptArray) {
						if(r.getUuid() != null && !r.getUuid().trim().isEmpty()) {
							if(request.getUuid().equals(r.getUuid().trim())) {
								requestedReceiptList.add(r);		
							}
						}
					}
				}
				log4j.info("Se obtienen " + requestedReceiptList.size() + " recibos.");
				
				if(requestedReceiptList != null && !requestedReceiptList.isEmpty()) {
					for(Receipt r :requestedReceiptList) {
			        	r.setFolio(inv.getFolio());
			        	r.setSerie(inv.getSerie());
			        	r.setUuid(inv.getUuid());
			        	
						if(AppConstants.RECEIPT_CODE_RETENTION.equals(r.getReceiptType())) {
							r.setAmountReceived(r.getAmountReceived() * -1);
							r.setForeignAmountReceived(r.getForeignAmountReceived() * -1);
							r.setQuantityReceived(Math.abs(r.getQuantityReceived()));
						}
					}
					
					String resp;
					if(AppConstants.DEFAULT_CURRENCY.equals(inv.getMoneda())) {
						resp = "DOC:" + eDIService.createNewVoucher(po, inv, 0, s, requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
					} else {
						ForeingInvoice fi = new ForeingInvoice();
						fi.setSerie(inv.getSerie());
						fi.setFolio(inv.getFolio());
						fi.setUuid(inv.getUuid());
						fi.setExpeditionDate(inv.getFechaTimbrado());
						resp = "DOC:" + eDIService.createNewForeignVoucher(po, fi, 0, s, requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
					}					
				}

				//Se envía el archivo PDF
				for(UserDocument doc : docs) {					
					if(doc.getType().trim().equals("application/pdf")) {
	                	File file = new File(System.getProperty("java.io.tmpdir")+"/"+ inv.getUuid() + ".pdf");
	                	Path filePath = Paths.get(file.getAbsolutePath());
	                	Files.write(filePath, doc.getContent());	                	
	                	this.sendFileToRemote(file, inv.getUuid() + ".pdf");	                	
		            	log4j.info("Se envía archivo " + inv.getUuid() + ".pdf");
		            	file.delete();
					}
				}
			}
			log4j.info("Termina proceso.....");
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unused")
	public void restoreForeignInvoice(List<InvoiceRequestDTO> list) {
		
		try {
			for(InvoiceRequestDTO request : list) {
				log4j.info("\n" + request.toString());
				
				PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(request.getDocumentNumber(), request.getAddressBook(), request.getDocumentType());
				Supplier s = supplierService.searchByAddressNumber(request.getAddressBook());
				
				ForeingInvoice fi = new ForeingInvoice();
				fi.setAddressNumber(request.getAddressBook());
				fi.setOrderNumber(request.getDocumentNumber());
				fi.setOrderType(request.getDocumentType());
				
				ForeignInvoiceTable fit = purchaseOrderDao.getForeignInvoice(fi);
				
				if(fit != null && fit.getUuid() != null && !fit.getUuid().trim().isEmpty() 
						&& fit.getUuid().trim().equals(request.getUuid())) {
					
					fi.setCountry(fit.getCountry());
					fi.setExpeditionDate(fit.getExpeditionDate());
					fi.setForeignCurrency(fit.getForeignCurrency());
					fi.setForeignDebit(fit.getForeignDebit());
					fi.setForeignDescription(fit.getForeignDescription());
					fi.setForeignNotes(fit.getForeignNotes());
					fi.setForeignRetention(fit.getForeignRetention());
					fi.setForeignSubtotal(fit.getForeignSubtotal());
					fi.setForeignTaxes(fit.getForeignTaxes());
					fi.setInvoiceNumber(fit.getInvoiceNumber());
					fi.setTaxId(fit.getTaxId());
					fi.setUsuarioImpuestos(fit.getUsuarioImpuestos());
					fi.setUuid(fit.getUuid());
				} else {
					log4j.info("No se encontró la factura foránea: " + request.getUuid() + ".");
					continue;
				}
				
				
				List<Receipt> requestedReceiptList = new ArrayList<Receipt>();
				List<Receipt> receiptArray= purchaseOrderService.getOrderReceipts(request.getDocumentNumber(), request.getAddressBook(), request.getDocumentType(),po.getOrderCompany());
				
				//Verificar que no esté registrado el uuid en recibos
				if(receiptArray != null) {					
					for(Receipt r : receiptArray) {
						if(r.getUuid() != null && !r.getUuid().trim().isEmpty()) {
//							if(request.getUuid().equals(r.getUuid().trim())) {
								requestedReceiptList.add(r);		
//							}
						}
					}
				}
				
				if(requestedReceiptList.isEmpty()) {
					log4j.info("No se encontraron recibos para la factura foránea: " + request.getUuid() + ".");
				} else {
					log4j.info("Se obtienen " + requestedReceiptList.size() + " recibos.");
					
					try {					
						String resp = "DOC:" + eDIService.createNewForeignVoucher(po, fi, 0, s, requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
						
						//Enviar primer archivo adjunto a la factura foranea
						try {
							List<UserDocument> listDocument = documentsDao.searchCriteria(request.getUuid());						
							if(listDocument != null && !listDocument.isEmpty()) {
								for(UserDocument document : listDocument) {
									if(document.getName() != null && document.getName().toLowerCase().contains(".pdf") 
											&& document.getContent() != null && org.apache.commons.lang.StringUtils.isBlank(document.getUuid())) {
										
					                	File file = new File(System.getProperty("java.io.tmpdir")+"/"+ fit.getUuid() + ".pdf");
					                	Path filePath = Paths.get(file.getAbsolutePath());
					                	Files.write(filePath, document.getContent());
					                	document.setUuid(fit.getUuid());
					                	this.sendFileToRemote(file, fit.getUuid() + ".pdf");				                
					                	this.update(document, null, null);
					                	log4j.info("FACTURA FORANEA: Se envía archivo " + fit.getUuid() + "");
					                	file.delete();
					                	break;
									}
								}
							}
						} catch (Exception e) {
							log4j.error("Exception" , e);
							e.printStackTrace();
						}
						
					}catch(Exception e) {
						log4j.error("Exception" , e);
						e.printStackTrace();
					}
				}

			}
			log4j.info("Termina proceso.....");
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		
	}
	
	public void sendFileToRemote(byte[] content, String fileName) {

		List<File> files = new ArrayList<File>();
		eDIService.sendFiles(files);
	}

	public void sendFileToRemote(File file, String fileName) {
		List<File> files = new ArrayList<File>();
		files.add(file);
		eDIService.sendFiles(files);
	}
	
	public String taxRoundingValidation(double importe, double tasaOCuota, double base) {
		
		if(base > 0 && importe > 0){
			double exp6 = (double) (Math.pow(10, -6)/2);
			double exp12 = (double) Math.pow(10, -12);
			double expDif = exp6 - exp12;
			
			double limInf = (base - exp6) * tasaOCuota;
			double limSup = (base + expDif) * tasaOCuota;
			
			String limInfStr = AppConstants.truncate(String.valueOf(limInf), 2);
			String limSupStr = AppConstants.round(limSup);
			
			double limInfDbl = Double.valueOf(limInfStr);
			double limSupDbl = Double.valueOf(limSupStr);
			
			if(importe >= limInfDbl) {
				if(importe <= limSupDbl) {
					return "";
				}else {
					return " Límite inferior " + limInfStr + " y superior "  + limSupStr;
				}
			}else {
				return " Límite inferior " + limInfStr + " y superior "  + limSupStr;
			}
		}
		
		return "";
		
	}
	
	private String validateInvNull(InvoiceDTO inv) {
		
		if(inv.getSello() == null) {
			return "Sello";
		}
		
		if(inv.getCertificado() == null) {
			return "Certificado";
		}
		
		if(inv.getLugarExpedicion() == null) {
			return "LugarExpedicion";
		}
		
		if(inv.getFecha() == null) {
			return "Fecha";
		}
		
		if(inv.getFechaTimbrado() == null) {
			return "FechaTimbrado";
		}
		
		if(inv.getSubTotal() == 0) {
			return "SubTotal";
		}
		
		if(inv.getMoneda() == null) {
			return "Moneda";
		}
		
		if(inv.getTotal() == 0) {
			return "Total";
		}
		
		if(inv.getRfcEmisor() == null) {
			return "RfcEmisor";
		}
		
		if(inv.getRfcReceptor() == null) {
			return "RfcReceptor";
		}
		
		if(inv.getReceptor() == null) {
			return "Receptor";
		}else {
			if(inv.getReceptor().getUsoCFDI() == null) {
				return "UsoCFDI";
			}
		}
		
		if(inv.getEmisor() == null) {
			return "Emisor";
		}else {
			if(inv.getEmisor().getRegimenFiscal() == null) {
				return "RegimenFiscal";
			}
		}
	
		if(inv.getConcepto() == null) {
			return "Concepto";
		}else {
			List<Concepto> conceptos = inv.getConcepto();
			if(conceptos != null) {
				for(Concepto concepto : conceptos) {
					
					if(concepto.getClaveProdServ() == null) {
						return "ClaveProdServ";
					}
					
					Impuestos impuestos = concepto.getImpuestos();
					if(impuestos != null) {
						Traslados traslados = impuestos.getTraslados();
						if(traslados != null) {
							List<Traslado> traslado = traslados.getTraslado();
							if(traslado != null) {
								for(Traslado t : traslado) {
									
									if(t.getTipoFactor() == null) {
										return "TipoFactor";
									} else {
										if("Tasa".equals(t.getTipoFactor())) {
											if(t.getTasaOCuota() == null) {
												return "TasaOCuota";
											}
										}									
									}
									
									//if(t.getBase() == null) {
									//	return "Base";
									//}
								}
							}
						}
					}					
					
					/*
					Retenciones retenciones = impuestos.getRetenciones();
					if(retenciones != null) {
						List<Retencion> retencion = retenciones.getRetencion();
						if(retencion != null) {
							for(Retencion t : retencion) {
								if(t.getTasaOCuota() == null) {
									return false;
								}
								
								if(t.getTipoFactor() == null) {
									//return false;
								}
								
								if(t.getBase() == null) {
									return false;
								}
							}
						}
					}
					*/
				}
			}
		}

		return "";
	}
	
	public UserDocument getDocumentByUuid(String uuid) {
		return documentsDao.getDocumentByUuid(uuid);
	}
	
	public UserDocument getInvoiceDocumentByUuid(String uuid) {
		return documentsDao.getInvoiceDocumentByUuid(uuid);
	}
	
	public UserDocument getInvoiceDocumentByUuid(String uuid,String fiscalType) {
		return documentsDao.getInvoiceDocumentByUuid(uuid,fiscalType);
	}
	
	public void updateDocuments(UserDocument o) {
		documentsDao.updateDocuments(o);
	}
	
	public InvoiceDTO getInvoiceXml(byte[] content){
		try{
			ByteArrayInputStream stream = new  ByteArrayInputStream(content);
			String xmlContent = IOUtils.toString(stream, "UTF-8");
			xmlContent = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
			xmlContent = xmlContent.replace("?<?xml", "<?xml");
			
			if(xmlContent.contains(AppConstants.NAMESPACE_CFDI_V4)) {
				return xmlToPojoService.convertV4(xmlContent);
			} else {
				return xmlToPojoService.convert(xmlContent);
			}
			
		}catch(Exception e){
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}
	}
	
	
	public String validateInvoiceFromSFT(InvoiceDTO inv, String addressBook, int documentNumber, String documentType,
			String tipoComprobante, boolean sendVoucher, String xmlContent, String receiptList,
			boolean specializedServices) {

		/////////////////////////// VAlidacion contra repse cruzG////
		ResponseGeneral response=null;
		log4j.info("inicio de validacion validateInvoiceVsSat: "+new Date().toLocaleString());
		ResponseGeneral resps=validateInvoiceVsSat(inv,xmlContent);
		log4j.info("fin de validacion validateInvoiceVsSat: "+new Date().toLocaleString());
		if (resps.isError()) {
			return resps.getMensaje().get("es");///devuelve solo el mensaje en español--en para ingles
		}else {
			if (resps.getDocument() != null) {
				InvoiceDTO invoiceUP =null;
				String InvoiceXML=null;
				ResponseGeneral resp=null;
				
				 ByteArrayInputStream stream = new  ByteArrayInputStream(resps.getDocument());
				try {
						InvoiceXML = IOUtils.toString(stream, "UTF-8");
						InvoiceXML = takeOffBOM(IOUtils.toInputStream(InvoiceXML, "UTF-8"));
						InvoiceXML = InvoiceXML.replace("?<?xml", "<?xml");
					} catch (IOException e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatXML",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp.getMensaje().get("es");
						
					}
				try {

						invoiceUP =getInvoiceXmlFromString(InvoiceXML);
					} catch (Exception e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatDTO",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp.getMensaje().get("es");
						
					}
				inv=invoiceUP;
				xmlContent=InvoiceXML;
			}
			
			
			
		}

		
		
		
		
		DecimalFormat currencyFormat = new DecimalFormat("$#,###.###");
		UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");
		log4j.info("inicio de busqueda previa cargada uuid: "+new Date().toLocaleString());
		List<FiscalDocumentsConcept> fisDocUuidList = fiscalDocumentConceptService.getListByUuid(inv.getUuid());
		log4j.info("fin de busqueda previa cargada uuid: "+new Date().toLocaleString());
		if(fisDocUuidList != null) {
			if(fisDocUuidList.size()>0)
//				for (FiscalDocumentsConcept fiscalDocumentsConcept : fisDocUuidList) {
//					fiscalDocumentsConcept.getBatchID();
//					FiscalDocuments fis=fiscalDocumentService.getById(Integer.parseInt(fiscalDocumentsConcept.getBatchID()) );
//					
//				}
				return "La factura que intenta ingresar ya se encuentra cargada previamente ";
		}
	
		/*
		if (udcCfdi != null) { 
			if (!"".equals(udcCfdi.getStrValue1())) {
				if ("TRUE".equals(udcCfdi.getStrValue1())) {
					log4j.info("inicio de validacion api sat: "+new Date().toLocaleString());
					String vcfdi = validaComprobanteSAT(inv);
					log4j.info("fin de validacion api sat: "+new Date().toLocaleString());
					if (!"".equals(vcfdi)) {
						return "Error de validación ante el SAT, favor de validar con su emisor fiscal.";
					}
					log4j.info("inicio de validacion nulos: "+new Date().toLocaleString());
					String vNull = validateInvNull(inv);
					log4j.info("fin de validacion nulos: "+new Date().toLocaleString());
					if (!"".equals(vNull)) {
						return "Error al validar el archivo XML, no se encontró el campo " + vNull + ".";
					}
				}
			}
		} else {
			log4j.info("inicio de validacion api sat: "+new Date().toLocaleString());
			String vcfdi = validaComprobanteSAT(inv);
			log4j.info("fin de validacion api sat: "+new Date().toLocaleString());
			if (!"".equals(vcfdi)) {
				return "Error de validación ante el SAT, favor de validar con su emisor fiscal.";
			}
			log4j.info("inicio de validacion nulos: "+new Date().toLocaleString());
			String vNull = validateInvNull(inv);
			log4j.info("fin de validacion nulos: "+new Date().toLocaleString());
			if (!"".equals(vNull)) {
				return "Error al validar el archivo XML, no se encontró el campo " + vNull + ".";
			}
		}*/

		String rfcEmisor = inv.getRfcEmisor();
		boolean allRules = true;
		List<UDC> supExclList = udcService.searchBySystem("NOCHECKSUP");
		if (supExclList != null) {
			for (UDC udc : supExclList) {
				if (rfcEmisor.equals(udc.getStrValue1())) {
					allRules = false;
					break;
				}
			}
		}

		// String resp = "";
		Supplier s = supplierService.searchByAddressNumber(addressBook);
		if (s == null) {
			return "El proveedor no existe en la base de datos.";
		}

		int diasCred = 30;
		/*
		 * if(s.getDiasCredito() != null && !s.getDiasCredito().isEmpty()) { diasCred =
		 * Integer.valueOf(s.getDiasCredito()); }
		 */

		String emailRecipient = (s.getEmailSupplier());
		List<Receipt> requestedReceiptList = null;

		/*List<Receipt> receiptArray = purchaseOrderService.getOrderReceipts(documentNumber, addressBook, documentType,
				po.getOrderCompany());*/
		List<Receipt> receiptArray = null;

				String fechaFactura = inv.getFechaTimbrado();
		fechaFactura = fechaFactura.replace("T", " ");
		SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
		Date invDate = null;
		try {
			invDate = sdf.parse(fechaFactura);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}

		String invCurrency = inv.getMoneda().trim();
		double exchangeRate = inv.getTipoCambio();

	
		String domesticCurrency = AppConstants.DEFAULT_CURRENCY;		
		/*List<UDC> comUDCList = udcService.searchBySystem("COMPANYDOMESTIC");
		if (comUDCList != null && !comUDCList.isEmpty()) {
			for (UDC company : comUDCList) {
				if (company.getStrValue1().trim().equals(po.getOrderCompany().trim())
						&& !"".equals(company.getStrValue2().trim())) {
					domesticCurrency = company.getStrValue2().trim();
					break;
				}
			}
		}*/

		if (allRules) {
			List<Receipt> recUuidList = purchaseOrderService.getReceiptsByUUID(inv.getUuid());
			if (recUuidList != null) {
				if (recUuidList.size() > 0)
					return "La factura que intenta ingresar ya se encuentra cargada previamente en otros recibos.";
			}

			if (!AppConstants.DEFAULT_CURRENCY.equals(invCurrency)) {
				if (exchangeRate == 0) {
					return "La moneda de la factura es " + invCurrency
							+ " sin embargo, no existe definido un tipo de cambio.";
				}
			}

			String oCurr = "";
			if ("MXP".equals(inv.getMoneda())) {
				oCurr = "MXN";
			} else {
				oCurr = inv.getMoneda();
			}
			if (!invCurrency.equals(oCurr)) {
				return "La moneda de la factura es " + invCurrency
						+ " sin embargo, el código de moneda de la orden de compra es " + oCurr;
			}

			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, currentYear);
			cal.set(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date startYear = cal.getTime();
			try {
				if (invDate.compareTo(startYear) < 0) {
					return "La fecha de emisión de la factura no puede ser anterior al primero de Enero del año en curso";
				}
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return "Error al obtener la fecha de timbrado de la factura";
			}

			if (s != null) {
			
				NonComplianceSupplier ncs = this.nonComplianceSupplierService.searchByTaxId(s.getRfc(), 0, 0);
				if (ncs != null && (ncs.getRefDate1().contains("Definitivo") || ncs.getRefDate1().contains("Presunto")
						|| ncs.getRefDate1().contains("Desvirtuado") || ncs.getRefDate2().contains("Definitivo")
						|| ncs.getRefDate2().contains("Presunto") || ncs.getRefDate2().contains("Desvirtuado")
						|| ncs.getStatus().contains("Definitivo") || ncs.getStatus().contains("Presunto")
						|| ncs.getStatus().contains("Desvirtuado"))) {

					String altEmail = "";
					List<UDC> udcList = udcService.searchBySystem("TAXALTEMAIL");
					if (udcList != null) {
						String emailContent = AppConstants.EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER;
						emailContent = emailContent.replace("_ADDNUMBER_", s.getAddresNumber());
						emailContent = emailContent.replace("_RAZONSOCIAL_", s.getRazonSocial());
						
						altEmail = udcList.get(0).getStrValue1();
						EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						emailAsyncSup.setProperties(AppConstants.EMAIL_NO_COMPLIANCE_INVOICE,
								emailContent,
								altEmail);
						emailAsyncSup.setMailSender(mailSenderObj);
						Thread emailThreadSup = new Thread(emailAsyncSup);
						emailThreadSup.start();
					}

					log4j.info("fin de validacion de lista negra "+new Date().toLocaleString());
					return "Los registros indican que cuenta con problemas fiscales y no se podrán cargar facturas en este momento.";

				}
			} else {
				return "El proveedor no existe en el catálogo de la aplicación";
			}

			boolean companyRfcIsValid = true;
			List<UDC> companyRfc = udcDao.searchBySystem("COMPANYRFC");
			if (companyRfc != null) {
				for (UDC udcrfc : companyRfc) {
					String cRfc = udcrfc.getStrValue1();
					String cRfcCompany = udcrfc.getStrValue2();
					if (cRfc.equals(inv.getRfcReceptor())) {
						
					}
				}
			}

			if (!companyRfcIsValid) {
				return "La factura no pertenece al receptor asociado a la orden de compra";
			}

		

//			if (AppConstants.LOCAL_COUNTRY_CODE.equals(s.getCountry())
//					&& AppConstants.REF_METODO_PAGO.equals(inv.getMetodoPago())) {
//				String pendingList = purchaseOrderService.getPendingReceiptsComplPago(s.getAddresNumber());
//				if (!"".equals(pendingList)) {
//					return "El sistema detectó que tiene las siguientes facturas (uuid) COMPLEMENTOS DE PAGO pendientes de carga: <br /> "
//							+ pendingList;
//				}
//			}

			cal = Calendar.getInstance();
			invDate = null;
			Date orderDate = null;
			try {
				fechaFactura = inv.getFechaTimbrado();
				fechaFactura = fechaFactura.replace("T", " ");
				sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
				invDate = sdf.parse(fechaFactura);
				orderDate = sdf.parse(inv.getFecha());
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}

			int pueOffSetDays = 0;
			UDC pueOffSet = udcService.searchBySystemAndKey(AppConstants.FACTURA_PUE, AppConstants.OFFSET_DAYS);
			if (pueOffSet != null) {
				if (pueOffSet.getStrValue1() != null) {
					pueOffSetDays = Integer.valueOf(pueOffSet.getStrValue1());
				}
			}

			if (!AppConstants.REF_METODO_PAGO.equals(inv.getMetodoPago())) {
				return "El método de pago permitido es " + AppConstants.REF_METODO_PAGO
						+ " y su CFDI contiene el valor " + inv.getMetodoPago()
						+ ". Favor de emitir nuevamente el CFDI con el método de pago antes mencionado.";
			}

			if (!AppConstants.REF_FORMA_PAGO.equals(inv.getFormaPago())) {
				return "La forma de pago permitida es " + AppConstants.REF_FORMA_PAGO + " y su CFDI contiene el valor "
						+ inv.getFormaPago()
						+ ". Favor de emitir nuevamente el CFDI con la forma de pago antes mencionada";
			}

			String cfdiReceptor = inv.getReceptor().getUsoCFDI();
			String rfcReceptor = inv.getRfcReceptor();
			if (!AppConstants.USO_CFDI.equals(cfdiReceptor)) {
				List<UDC> udcList = udcService.searchBySystem("CFDIEXC");
				boolean usoCfdiExcept = false;
				if (udcList != null) {
					for (UDC udc : udcList) {
						if (udc.getStrValue1().equals(rfcEmisor)) {
							usoCfdiExcept = true;
						}
					}
				}

				if (usoCfdiExcept) {
					return "El uso CFDI " + cfdiReceptor + " no es permitido para su razón social";
				}

			}

			if (rfcEmisor != null) {
				if (!"".equals(rfcEmisor)) {
					if (!s.getRfc().equals(rfcEmisor)) {
						return "La factura ingresada no pertenece al RFC del emisor del proveedor registrado como "
								+ s.getRfc();
					}
				}
			}

			boolean receptorValido = false;
			List<UDC> receptores = udcService.searchBySystem("RECEPTOR");
			if (receptores != null) {
				for (UDC udc : receptores) {
					if (udc.getStrValue1().equals(inv.getRfcReceptor().trim())) {
						receptorValido = true;
						break;
					}
				}
			}
			if (!receptorValido) {
				return "El RFC receptor " + inv.getRfcReceptor() + " no es permitido para carga de facturas.";
			}

			// Validación de los importes del recibo:
			if (AppConstants.INVOICE_FIELD.equals(tipoComprobante)) {
				Users u = usersService.searchCriteriaUserName(inv.getRfcEmisor());

				UDC porcentajeMaxUdc = udcService.searchBySystemAndKey("PORCENTAJE", "MAX");
				UDC porcentajeMinUdc = udcService.searchBySystemAndKey("PORCENTAJE", "MIN");
				UDC montoLimiteUdc = udcService.searchBySystemAndKey("MONTO", "LIMITE");
				UDC montoLimiteMaxUdc = udcService.searchBySystemAndKey("MONTO", "MAX");
				UDC montoLimiteMinUdc = udcService.searchBySystemAndKey("MONTO", "MIN");

				double porcentajeMax = Double.valueOf(porcentajeMaxUdc.getStrValue1()) / 100;
				double porcentajeMin = Double.valueOf(porcentajeMinUdc.getStrValue1()) / 100;

				double montoLimite = Double.valueOf(montoLimiteUdc.getStrValue1());
				double montoLimiteMax = Double.valueOf(montoLimiteMaxUdc.getStrValue1());
				double montoLimiteMin = Double.valueOf(montoLimiteMinUdc.getStrValue1());

				double orderAmount = 0;
				double foreignOrderAmount = 0;
				double retainedAmount = 0;
				double retainedForeignAmount = 0;
				double currentRetainedAmount = 0;
				double invoiceAmount = 0;

				boolean isRetainedValidationOn = true;
				List<UDC> taxCodeExcList = udcService.searchBySystem("NOCHECKRET");
				List<String> taxCodeList = new ArrayList<String>();
				if (taxCodeExcList != null && !taxCodeExcList.isEmpty()) {
					for (UDC taxCode : taxCodeExcList) {
						taxCodeList.add(taxCode.getStrValue1());
					}
				}

				// JAVILA
				String receiptTaxCode = "";
				for (Receipt r : requestedReceiptList) {
					if (r.getTaxCode() != null && !"".equals(r.getTaxCode().trim())) {
						receiptTaxCode = r.getTaxCode().trim();
						if (taxCodeList.contains(r.getTaxCode().trim().toUpperCase())) {
							isRetainedValidationOn = false;
							break;
						}
					}
				}


				// JAVILA: TEMPORAL END

				// JAVILA: Valida tasa de impuestos. Si el recibo tiene valor realiza la
				// validación
				String tasaOCuota = "";
				List<UDC> udcTax = udcDao.searchBySystem("F43121TXA1");
				List<String> validTaxCodeList = new ArrayList<String>();

				if (udcTax != null) {
					for (UDC ut : udcTax) {
						validTaxCodeList.add(ut.getUdcKey().trim());
					}
				}

				if (!"".equals(receiptTaxCode)) {
					// Valida código para retenciones
					if (validTaxCodeList.contains(receiptTaxCode.trim())) {
						boolean isRetentionOK = false;
						String taxCodeValue = "";
						for (UDC ut : udcTax) {
							if (ut.getUdcKey().equals(receiptTaxCode.trim()) && ut.getStrValue1() != null
									&& !"".equals(ut.getStrValue1()) && !"NA".equals(ut.getStrValue1())) {
								taxCodeValue = ut.getStrValue1();
								break;
							}
						}

						if ("".equals(taxCodeValue)) {
							isRetentionOK = true;
						} else {
							tasaOCuota = "NE";
							List<Concepto> conceptos = inv.getConcepto();
							if (conceptos != null) {
								for (Concepto concepto : conceptos) {
									Impuestos impuestos = concepto.getImpuestos();
									if (impuestos != null) {
										Retenciones retenciones = impuestos.getRetenciones();
										if (retenciones != null && !"".equals(taxCodeValue)) {
											List<Retencion> retencion = retenciones.getRetencion();
											if (retencion != null) {
												for (Retencion t : retencion) {
													if ("002".equals(t.getImpuesto())) {
														if (t.getTasaOCuota() != null) {
															tasaOCuota = t.getTasaOCuota().trim();
															if (!"".equals(taxCodeValue) && !"".equals(tasaOCuota)) {
																if (tasaOCuota.equals(taxCodeValue)) {
																	isRetentionOK = true;

																	// VALIDA RANGO DE IMPUESTOS
																	/*
																	 * if(!"".equals(tasaOCuota)) { double tasaOCuotaDbl
																	 * = Double.valueOf(tasaOCuota); double base = 0;
																	 * double importe = 0; if(t.getBase() != null &&
																	 * !"".equals(t.getBase())) { base =
																	 * Double.valueOf(t.getBase()); }
																	 * 
																	 * if(t.getImporte() != null &&
																	 * !"".equals(t.getImporte())) { importe =
																	 * Double.valueOf(t.getImporte()); }
																	 * 
																	 * String taxResult = taxRoundingValidation(importe,
																	 * tasaOCuotaDbl, base); if(!"".equals(taxResult)) {
																	 * return
																	 * "Los impuestos del CFDI no cumplen con las reglas dictaminadas por el SAT del "
																	 * + "concepto  " + concepto.getDescripcion() +
																	 * " El cual debe estar dentro de los límites " +
																	 * taxResult +
																	 * ". Favor de revisarlo con su PAC (Proveedor Autorizado de Certificado)"
																	 * ; } }
																	 */
																	break;
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}

						if (!isRetentionOK) {
							return "La tasa o cuota de retención de su CFDI es " + tasaOCuota
									+ " y la tasa del recibo es de " + taxCodeValue
									+ ". Favor de verificarlo con el comprador.";
						}
					}

					// Valida codigo para traslados
					if (validTaxCodeList.contains(receiptTaxCode.trim())) {
						boolean isTransOk = false;
						String taxCodeValue = "";
						for (UDC ut : udcTax) {
							if (ut.getUdcKey().equals(receiptTaxCode.trim()) && ut.getStrValue2() != null
									&& !"".equals(ut.getStrValue2()) && !"NA".equals(ut.getStrValue2())) {
								taxCodeValue = ut.getStrValue2();
								break;
							}
						}

						if ("".equals(taxCodeValue)) {
							isTransOk = true;
						} else {
							tasaOCuota = "NE";
							List<Concepto> conceptos = inv.getConcepto();
							if (conceptos != null) {
								for (Concepto concepto : conceptos) {
									Impuestos impuestos = concepto.getImpuestos();
									if (impuestos != null) {
										Traslados traslados = impuestos.getTraslados();
										if (traslados != null && !"".equals(taxCodeValue)) {
											List<Traslado> traslado = traslados.getTraslado();
											if (traslado != null) {
												for (Traslado t : traslado) {
													if ("002".equals(t.getImpuesto())) {
														if (t.getTasaOCuota() != null) {
															tasaOCuota = t.getTasaOCuota().trim();
															if (!"".equals(taxCodeValue) && !"".equals(tasaOCuota)) {
																if (tasaOCuota.equals(taxCodeValue)) {
																	isTransOk = true;

																	// VALIDA RANGO DE IMPUESTOS
																	/*
																	 * if(!"".equals(tasaOCuota)) { double tasaOCuotaDbl
																	 * = Double.valueOf(tasaOCuota); double base = 0;
																	 * double importe = 0; if(t.getBase() != null &&
																	 * !"".equals(t.getBase())) { base =
																	 * Double.valueOf(t.getBase()); }
																	 * 
																	 * if(t.getImporte() != null &&
																	 * !"".equals(t.getImporte())) { importe =
																	 * Double.valueOf(t.getImporte()); }
																	 * 
																	 * String taxResult = taxRoundingValidation(importe,
																	 * tasaOCuotaDbl, base); if(!"".equals(taxResult)) {
																	 * return
																	 * "Los impuestos del CFDI no cumplen con las reglas dictaminadas por el SAT del "
																	 * + "concepto  " + concepto.getDescripcion() +
																	 * " El cual debe estar dentro de los límites " +
																	 * taxResult +
																	 * ". Favor de revisarlo con su PAC (Proveedor Autorizado de Certificado)"
																	 * ; } }
																	 */
																	break;
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}

						if (!isTransOk) {
							return "La tasa o cuota de su CFDI es " + tasaOCuota + " y la tasa del recibo es de "
									+ taxCodeValue + ". Favor de verificarlo con el comprador.";
						}
					}

				} else {
					// Valida codigo para traslados con iva 0
					boolean isTransOk = false;
					boolean containTax = false;
					String taxCodeValue = "0.000000";
					String taxFactorType = "Exento";
					String tipoFactor = "";

					List<Concepto> conceptos = inv.getConcepto();
					if (conceptos != null) {
						for (Concepto concepto : conceptos) {
							Impuestos impuestos = concepto.getImpuestos();
							if (impuestos != null) {
								Traslados traslados = impuestos.getTraslados();
								if (traslados != null) {
									List<Traslado> traslado = traslados.getTraslado();
									if (traslado != null) {
										for (Traslado t : traslado) {
											containTax = true;
											if ("002".equals(t.getImpuesto())) {

												if (t.getTipoFactor() != null) {
													tipoFactor = t.getTipoFactor().trim();
													if (!"".equals(tipoFactor) && !"".equals(taxFactorType)) {
														if (tipoFactor.equals(taxFactorType)) {
															isTransOk = true;
															break;
														}
													}
												}

												if (t.getTasaOCuota() != null) {
													tasaOCuota = t.getTasaOCuota().trim();
													if (!"".equals(taxCodeValue) && !"".equals(tasaOCuota)) {
														if (tasaOCuota.equals(taxCodeValue)) {
															isTransOk = true;
															break;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}

					if (containTax && !isTransOk) {
						return "Los impuestos del CFDI no son válidos, el recibo viene con un código de impuesto vacío.<br>Favor de verificarlo con el comprador.";
					}
				}

				for (Receipt r : requestedReceiptList) {
					if (AppConstants.RECEIPT_CODE_RETENTION.equals(r.getReceiptType())) {
						retainedAmount = retainedAmount + r.getAmountReceived();
						retainedForeignAmount = retainedForeignAmount + r.getForeignAmountReceived();
					} else {
						orderAmount = orderAmount + r.getAmountReceived();
						foreignOrderAmount = foreignOrderAmount + r.getForeignAmountReceived();
					}
				}

				String tipoValidacion = "";

				if (isRetainedValidationOn) {
					double totalImporteMayorRetenido = 0;
					double totalImporteMenorRetenido = 0;
					double retainedTotalAmount = 0;

					if (domesticCurrency.equals(invCurrency)) {
						currentRetainedAmount = retainedAmount;
						totalImporteMayorRetenido = retainedAmount;
						totalImporteMenorRetenido = retainedAmount;
					} else {
						currentRetainedAmount = retainedForeignAmount;
						totalImporteMayorRetenido = retainedForeignAmount;
						totalImporteMenorRetenido = retainedForeignAmount;
					}

					retainedTotalAmount = inv.getTotalRetenidos();
					if (montoLimite != 0) {
						if (retainedTotalAmount >= montoLimite) {
							totalImporteMayorRetenido = totalImporteMayorRetenido + montoLimiteMax;
							totalImporteMenorRetenido = totalImporteMenorRetenido - montoLimiteMin;
							tipoValidacion = "Por Monto";
						} else {
							totalImporteMayorRetenido = totalImporteMayorRetenido
									+ (totalImporteMayorRetenido * porcentajeMax);
							totalImporteMenorRetenido = totalImporteMenorRetenido
									- (totalImporteMenorRetenido * porcentajeMin);
							tipoValidacion = "Por porcentaje";
						}
					}

					totalImporteMayorRetenido = totalImporteMayorRetenido * 100;
					totalImporteMayorRetenido = Math.round(totalImporteMayorRetenido);
					totalImporteMayorRetenido = totalImporteMayorRetenido / 100;

					totalImporteMenorRetenido = totalImporteMenorRetenido * 100;
					totalImporteMenorRetenido = Math.round(totalImporteMenorRetenido);
					totalImporteMenorRetenido = totalImporteMenorRetenido / 100;

					if (totalImporteMayorRetenido < retainedTotalAmount
							|| totalImporteMenorRetenido > retainedTotalAmount) {
						return "El total de los impuestos retenidos de su CFDI es "
								+ currencyFormat.format(retainedTotalAmount)
								+ " no coincide con el total de las retenciones del recibo seleccionado. Favor de verificarlo con el comprador.";
					}
				}

				double totalImporteMayor = 0;
				double totalImporteMenor = 0;
				double invoiceTotalAmount = 0;
				double currentInvoiceAmount = 0;

				if (domesticCurrency.equals(invCurrency)) {
					currentInvoiceAmount = orderAmount;
					totalImporteMayor = orderAmount;
					totalImporteMenor = orderAmount;
				} else {
					currentInvoiceAmount = foreignOrderAmount;
					totalImporteMayor = foreignOrderAmount;
					totalImporteMenor = foreignOrderAmount;
				}

				invoiceTotalAmount = inv.getSubTotal();
				if (montoLimite != 0) {
					if (invoiceTotalAmount >= montoLimite) {
						totalImporteMayor = totalImporteMayor + montoLimiteMax;
						totalImporteMenor = totalImporteMenor - montoLimiteMin;
						tipoValidacion = "Por Monto";
					} else {
						totalImporteMayor = totalImporteMayor + (totalImporteMayor * porcentajeMax);
						totalImporteMenor = totalImporteMenor - (totalImporteMenor * porcentajeMin);
						tipoValidacion = "Por porcentaje";
					}
				}

				totalImporteMayor = totalImporteMayor * 100;
				totalImporteMayor = Math.round(totalImporteMayor);
				totalImporteMayor = totalImporteMayor / 100;

				totalImporteMenor = totalImporteMenor * 100;
				totalImporteMenor = Math.round(totalImporteMenor);
				totalImporteMenor = totalImporteMenor / 100;

				double discount = 0;

				if (inv.getDescuento() != 0) {
					discount = inv.getDescuento();
					discount = discount * 100;
					discount = Math.round(discount);
					discount = discount / 100;
				}
				invoiceAmount = inv.getSubTotal() - discount;
				invoiceAmount = invoiceAmount * 100;
				invoiceAmount = Math.round(invoiceAmount);
				invoiceAmount = invoiceAmount / 100;

				if (totalImporteMayor < invoiceAmount || totalImporteMenor > invoiceAmount) {
					return "El total de la factura " + currencyFormat.format(invoiceAmount)
							+ " no coincide con el total de los recibos seleccionados "
							+ currencyFormat.format(currentInvoiceAmount) + ". Tipo de validación:" + tipoValidacion;
				} else {

					List<UDC> pmtTermsUdc = udcService.searchBySystem("PMTTERMS");
					String pmtTermsCode = "";
					
					//terminos por recibos
					for(Receipt r :requestedReceiptList) {
						if(r.getPaymentTerms() != null && !"".equals(r.getPaymentTerms())) {
							for(UDC udcpmt : pmtTermsUdc) {
								if(udcpmt.getUdcKey().equals(r.getPaymentTerms().trim()) && udcpmt.getStrValue2() != null && !"".equals(udcpmt.getStrValue2())) {
									diasCred = Integer.parseInt(udcpmt.getStrValue2());
									break;
								}
							}
						} else {
							diasCred = 30;
							break;
						}
					}
					
					
					
					///terminos por orden de pago
					
					
					
//					PurchaseOrder po = purchaseOrderService.searchbyOrderAndAddressBook(documentNumber, addressBook, documentType);
//					if (po != null && po.getPaymentTerms() != null && !"".equals(po.getPaymentTerms().trim())) {
//						for (UDC udcpmt : pmtTermsUdc) {
//							if (udcpmt.getStrValue1().equals(po.getPaymentTerms().trim())) {
//								diasCred = Integer.parseInt(udcpmt.getStrValue2());
//								break;
//							}
//						}
//					} else {
//						diasCred = 30;
//					}

					Date estimatedPaymentDate = null;
					Date currentDate = new Date();
					if (currentDate != null) {
						Calendar c = Calendar.getInstance();
						c.setTime(currentDate);
						c.add(Calendar.DATE, diasCred);
						List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(c.getTime(), 0,
								500, inv.getRfcEmisor());
						if (pc != null) {
							if (pc.size() > 0) {
								estimatedPaymentDate = pc.get(0).getPaymentDate();
							} else {
								estimatedPaymentDate = c.getTime();
							}
						} else {
							estimatedPaymentDate = c.getTime();
						}
					}

				

					for (Receipt r : requestedReceiptList) {
						r.setInvDate(invDate);
						r.setFolio(inv.getFolio());
						r.setSerie(inv.getSerie());
						r.setUuid(inv.getUuid());
						r.setEstPmtDate(estimatedPaymentDate);
						r.setStatus(AppConstants.STATUS_OC_INVOICED);
					}
					purchaseOrderService.updateReceipts(requestedReceiptList);
				}

			}

		} else {

			List<UDC> pmtTermsUdc = udcService.searchBySystem("PMTTERMS");
			String pmtTermsCode = "";			
			/*for (Receipt r : requestedReceiptList) {
				if (r.getPaymentTerms() != null && !"".equals(r.getPaymentTerms())) {
					for (UDC udcpmt : pmtTermsUdc) {
						if (udcpmt.getStrValue1().equals(r.getPaymentTerms().trim())) {
							diasCred = Integer.parseInt(udcpmt.getStrValue2());
							break;
						}
					}
				}
				if (r.getPaymentTerms() == null || "".equals(r.getPaymentTerms())) {
					diasCred = 30;
					break;
				}
			}*/
			Date estimatedPaymentDate = null;
			if (invDate != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(invDate);
				c.add(Calendar.DATE, diasCred);
				/*List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(c.getTime(), 0, 500,
						po.getAddressNumber());*/
				List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(c.getTime(), 0, 500,
						addressBook);
				if (pc != null) {
					if (pc.size() > 0) {
						estimatedPaymentDate = pc.get(0).getPaymentDate();
					} else {
						estimatedPaymentDate = c.getTime();
					}
				} else {
					estimatedPaymentDate = c.getTime();
				}
			}


		/*	for (Receipt r : requestedReceiptList) {
				r.setInvDate(invDate);
				r.setFolio(inv.getFolio());
				r.setSerie(inv.getSerie());
				r.setUuid(inv.getUuid());
				r.setEstPmtDate(estimatedPaymentDate);
				r.setStatus(AppConstants.STATUS_OC_INVOICED);
			}
			purchaseOrderService.updateReceipts(requestedReceiptList);
			sendVoucher = true;*/

		}

		if (sendVoucher) {
			try {
				for (Receipt r : requestedReceiptList) {
					if (AppConstants.RECEIPT_CODE_RETENTION.equals(r.getReceiptType())) {
						r.setAmountReceived(r.getAmountReceived() * -1);
						r.setForeignAmountReceived(r.getForeignAmountReceived() * -1);
						r.setQuantityReceived(Math.abs(r.getQuantityReceived()));
					}
				}

				if (domesticCurrency.equals(invCurrency)) {
					// resp = "DOC:" + eDIService.createNewVoucher(po, inv, 0, s,
					// requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
				} else {
					ForeingInvoice fi = new ForeingInvoice();
					fi.setSerie(inv.getSerie());
					fi.setFolio(inv.getFolio());
					fi.setUuid(inv.getUuid());
					fi.setExpeditionDate(inv.getFechaTimbrado());
					// resp = "DOC:" + eDIService.createNewForeignVoucher(po, fi, 0, s,
					// requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
				}

			
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}
	
	public String getInvoiceTaxCode(InvoiceDTO inv) {
		String taxCode = AppConstants.INVOICE_TAX0;
		String taxRateTranslated = "NA";
		String taxRateRetained = "NA";

		List<Concepto> conceptos = inv.getConcepto();
		if(conceptos != null) {
			for(Concepto concepto : conceptos) {
				Impuestos impuestos = concepto.getImpuestos();
				if(impuestos != null) {
					
					//Obtener impuesto trasladado
					Traslados traslados = impuestos.getTraslados();
					if(traslados != null) {
						List<Traslado> traslado = traslados.getTraslado();
						if(traslado != null) {
							for(Traslado t : traslado) {
								if(t.getTipoFactor() != null) {
									if("Tasa".equals(t.getTipoFactor())) {
										if(t.getTasaOCuota() != null 
												&& !"".equals(t.getTasaOCuota().trim()) 
												&& !AppConstants.INVOICE_TAX_RATE_TAX0.equals(t.getTasaOCuota().trim())) {
											taxRateTranslated = t.getTasaOCuota().trim();
											break;
										}
									}
								}
							}
						}
					}
					
					//Obtener impuesto retenido
					Retenciones retenciones = impuestos.getRetenciones();
					if(retenciones != null) {
						List<Retencion> retencion = retenciones.getRetencion();
						if(retencion != null) {
							for(Retencion t : retencion) {
								if("002".equals(t.getImpuesto())){
									if(t.getTasaOCuota()!= null
											&& !"".equals(t.getTasaOCuota().trim())
											&& !AppConstants.INVOICE_TAX_RATE_TAX0.equals(t.getTasaOCuota().trim())) {
										taxRateRetained = t.getTasaOCuota().trim(); 
										break;
									}
								}
							}
						}
					}
					
				}
			}
		}
		
		List<UDC> udcTaxUDCList = udcDao.searchBySystem("F43121TXA1");
		if(udcTaxUDCList != null) {
			for(UDC udc : udcTaxUDCList) {
				if(taxRateTranslated.equals(udc.getStrValue2().trim()) && taxRateRetained.equals(udc.getStrValue1().trim())) {
					taxCode = udc.getUdcKey().trim();
					break;
				}
			}	
		}
		
		return taxCode;
	}
	
	public List<UserDocument> searchCriteriaByUuid(int orderNumber, 
	          String uuidFactura, String type){
return documentsDao.searchCriteriaByUuid(orderNumber,  uuidFactura , type);
}	
	
public List<UserDocument> searchCriteriaByFiscalType(int orderNumber, String addressNumber, String type) {
	return documentsDao.searchCriteriaByFiscalType(orderNumber, addressNumber, type);	
	}
	
	public String validateTaxAmount(InvoiceDTO inv, double maxLimit, double minLimit) {
		String message = "";
		
		List<Concepto> conceptos = inv.getConcepto();
		if(conceptos != null) {
			for(Concepto concepto : conceptos) {
				Impuestos impuestos = concepto.getImpuestos();
				if(impuestos != null) {
					
					//VALIDA RANGO DE IMPUESTOS TRASLADADOS
					Traslados traslados = impuestos.getTraslados();
					if(traslados != null) {
						List<Traslado> traslado = traslados.getTraslado();
						if(traslado != null) {
							for(Traslado t : traslado) {
								if(t.getTasaOCuota() != null && t.getImporte() != null && t.getBase() != null
										&& !"".equals(t.getTasaOCuota()) && !"".equals(t.getImporte()) && !"".equals(t.getBase())) {
									
									double impImporte = Double.valueOf(t.getImporte()).doubleValue();
									double impImporteReal = Double.valueOf(t.getBase()).doubleValue() * Double.valueOf(t.getTasaOCuota()).doubleValue();
									double impTotalImporteMayor = impImporteReal;
									double impTotalImporteMenor = impImporteReal;																		

									impTotalImporteMayor = impTotalImporteMayor + maxLimit;
									impTotalImporteMenor = impTotalImporteMenor - minLimit;
									
									impTotalImporteMayor = impTotalImporteMayor * 100;
									impTotalImporteMayor = Math.round(impTotalImporteMayor);
									impTotalImporteMayor = impTotalImporteMayor /100;	
									
									impTotalImporteMenor = impTotalImporteMenor * 100;
									impTotalImporteMenor = Math.round(impTotalImporteMenor);
									impTotalImporteMenor = impTotalImporteMenor /100;
									
									impImporte = impImporte * 100;
									impImporte = Math.round(impImporte);
									impImporte = impImporte /100;

									if(impTotalImporteMayor < impImporte || impTotalImporteMenor > impImporte) {
										return "El importe de $" + t.getImporte() + " para la tasa de impuesto " + t.getTasaOCuota() + " del CFDI no se encuentra dentro del rango permitido.<br />" +
												"Favor de revisarlo con su PAC (Proveedor Autorizado de Certificado).";
									}
								}
							}
						}
					}
					
					//VALIDA RANGO DE IMPUESTOS RETENIDOS
					Retenciones retenciones = impuestos.getRetenciones();
					if(retenciones != null) {
						List<Retencion> retencion = retenciones.getRetencion();
						if(retencion != null) {
							for(Retencion t : retencion) {
								if(t.getTasaOCuota() != null && t.getImporte() != null && t.getBase() != null
										&& !"".equals(t.getTasaOCuota()) && !"".equals(t.getImporte()) && !"".equals(t.getBase())) {

									double impImporte = Double.valueOf(t.getImporte()).doubleValue();
									double impImporteReal = Double.valueOf(t.getBase()).doubleValue() * Double.valueOf(t.getTasaOCuota()).doubleValue();
									double impTotalImporteMayor = impImporteReal;
									double impTotalImporteMenor = impImporteReal;																		

									impTotalImporteMayor = impTotalImporteMayor + maxLimit;
									impTotalImporteMenor = impTotalImporteMenor - minLimit;
									
									impTotalImporteMayor = impTotalImporteMayor * 100;
									impTotalImporteMayor = Math.round(impTotalImporteMayor);
									impTotalImporteMayor = impTotalImporteMayor /100;	
									
									impTotalImporteMenor = impTotalImporteMenor * 100;
									impTotalImporteMenor = Math.round(impTotalImporteMenor);
									impTotalImporteMenor = impTotalImporteMenor /100;
									
									impImporte = impImporte * 100;
									impImporte = Math.round(impImporte);
									impImporte = impImporte /100;

									if(impTotalImporteMayor < impImporte || impTotalImporteMenor > impImporte) {
										return "El importe de $" + t.getImporte() + " para la tasa de impuesto " + t.getTasaOCuota() + " del CFDI no se encuentra dentro del rango permitido.<br />" +
												"Favor de revisarlo con su PAC (Proveedor Autorizado de Certificado).";
									}
								}
							}
						}
					}
				}
			}
		}
		
		return message;
	}
	
	public List<String> stringWithPipesToList(String currentString){
		//int stringLenght = 8;//No se toma en cuenta el último dígito de la tasa de impuestos
		List<String> list = new ArrayList<String>();		
		String[] splitArray = null;		
		
		try {
			if(!"".equals(currentString)) {
				if(currentString.contains("|")) {
					splitArray = currentString.split("\\|");
				} else {
					splitArray = new String[]{currentString};
				}			
			}
			list = Arrays.asList(splitArray);
			/*if(splitArray != null && splitArray.length > 0) {
				for(String string : splitArray) {
					if(string.trim().length() >= stringLenght) {
						list.add(string.trim().substring(0, stringLenght));
					} else {
						list.add(string.trim());
					}					
				}
			}*/
		} catch (Exception e) {	
			log4j.error("Exception" , e);
		}
		
		return list;
	}

	public List<String> getTranslatedTaxList(InvoiceDTO inv) {
		//int stringLenght = 8;//No se toma en cuenta el último dígito de la tasa de impuestos
		List<String> translatedTaxList = new ArrayList<String>();
		List<Concepto> conceptos = inv.getConcepto();
		if(conceptos != null) {
			for(Concepto concepto : conceptos) {
				Impuestos impuestos = concepto.getImpuestos();
				if(impuestos != null) {
					Traslados traslados = impuestos.getTraslados();
					if(traslados != null) {
						List<Traslado> traslado = traslados.getTraslado();
						if(traslado != null) {
							for(Traslado t : traslado) {
								if(t.getTasaOCuota() != null){
									String tasaOCuota = t.getTasaOCuota().trim(); //.substring(0, stringLenght);//No se toma en cuenta el último dígito
									if(!"".equals(tasaOCuota) && !translatedTaxList.contains(tasaOCuota)) {
										translatedTaxList.add(tasaOCuota);
									}
								}
							}
						}
					}
				}
			}
		}
		
		if(inv.getComplemento()!=null) {
			if(null!=inv.getComplemento().getImpuestosLocales()){
				if(null!=inv.getComplemento().getImpuestosLocales().getTrasladosLocales()) {
					translatedTaxList.add(inv.getComplemento().getImpuestosLocales().getTrasladosLocales().getTasadeTraslado());
				}
			}
			}
		return translatedTaxList;
	}	
	
	public List<String> getTaxFactorTypeList(InvoiceDTO inv) {
		List<String> taxFactorTypeList = new ArrayList<String>();
		List<Concepto> conceptos = inv.getConcepto();
		if(conceptos != null) {
			for(Concepto concepto : conceptos) {
				Impuestos impuestos = concepto.getImpuestos();
				if(impuestos != null) {
					Traslados traslados = impuestos.getTraslados();
					if(traslados != null) {
						List<Traslado> traslado = traslados.getTraslado();
						if(traslado != null) {
							for(Traslado t : traslado) {
								if(t.getTipoFactor() != null){
									String tipoFactor = t.getTipoFactor().trim();
									if(!"".equals(tipoFactor) && !taxFactorTypeList.contains(tipoFactor)) {
										if (tipoFactor.equals("Exento")) {
											taxFactorTypeList.add(tipoFactor);
										}
									}									
								}
							}
						}
					}
				}
			}
		}
		
		/*if(inv.getComplemento()!=null) {
			if(null!=inv.getComplemento().getImpuestosLocales()){
				if(null!=inv.getComplemento().getImpuestosLocales().getTrasladosLocales()) {
					taxFactorTypeList.add(inv.getComplemento().getImpuestosLocales().getTrasladosLocales().getTasadeTraslado());
				}
			}
			}*/
		return taxFactorTypeList;
	}
	
	public List<String> getRetainedTaxList(InvoiceDTO inv) {
		//int stringLenght = 8;//No se toma en cuenta el último dígito de la tasa de impuestos
		List<String> retainedTaxList = new ArrayList<String>();		
		List<Concepto> conceptos = inv.getConcepto();
		
		if(conceptos != null) {
			for(Concepto concepto : conceptos) {
				Impuestos impuestos = concepto.getImpuestos();
				if(impuestos != null) {
					Retenciones retenciones = impuestos.getRetenciones();
					if(retenciones != null) {
						List<Retencion> retencion = retenciones.getRetencion();
						if(retencion != null) {
							for(Retencion t : retencion) {
								if(t.getTasaOCuota()!= null) {
									String tasaOCuota = t.getTasaOCuota().trim(); //.substring(0, stringLenght);//No se toma en cuenta el último dígito
									if(!"".equals(tasaOCuota) && !retainedTaxList.contains(tasaOCuota)) {
										retainedTaxList.add(tasaOCuota);
									}
								}
							}
						}
					}
				}
			}
		}
		if(inv.getComplemento()!=null) {
			if(null!=inv.getComplemento().getImpuestosLocales()){
				if(null!=inv.getComplemento().getImpuestosLocales().getRetencionesLocales()) {
					retainedTaxList.add(inv.getComplemento().getImpuestosLocales().getRetencionesLocales().getTasadeRetencion());
				}
			}
			}
		
		return retainedTaxList;
	}

	
	
	
	public ResponseGeneral validateInvoiceVsSat(FileUploadBean file) {
		
		return validateInvoiceVsSat(file.getFile().getBytes());
	}
	
	public ResponseGeneral validateInvoiceVsSat(byte[] array) {
		InvoiceDTO invoiceUP =null;
		String InvoiceXML=null;
		ResponseGeneral resp=null;
		
		 ByteArrayInputStream stream = new  ByteArrayInputStream(array);
		try {
				InvoiceXML = IOUtils.toString(stream, "UTF-8");
				InvoiceXML = takeOffBOM(IOUtils.toInputStream(InvoiceXML, "UTF-8"));
				InvoiceXML = InvoiceXML.replace("?<?xml", "<?xml");
			} catch (IOException e) {
				
				resp=new ResponseGeneral(true,"validateInvoiceVsSatXML",e);
				resp.addMensaje("es", "El Documento no es  XML valido.");
				resp.addMensaje("en", "The Document is not XML valid.");
				log4j.error(resp.toString());
				log4j.error("Factura validateInvoiceVsSat(byte[] array): "+InvoiceXML);
				log4j.error("Exception" , e);
				e.printStackTrace();
				return resp;
				
			}
		try {

				invoiceUP =getInvoiceXmlFromString(InvoiceXML);
			} catch (Exception e) {
				
				resp=new ResponseGeneral(true,"validateInvoiceVsSatDTO",e);
				resp.addMensaje("es", "El documento no es  XML valido.");
				resp.addMensaje("en", "The document is not XML valid.");
				log4j.error(resp.toString());
				log4j.error("FACTURA"+InvoiceXML);
				log4j.error("Exception" , e);
				e.printStackTrace();
				return resp;
				
			}
		
		
		return validateInvoiceVsSat(invoiceUP, InvoiceXML);
	}
	
	public ResponseGeneral validateInvoiceVsSat(InvoiceDTO invoiceUP,String InvoiceXML) {
		ResponseGeneral resp=null;
		
		String InvoiceXMLSAT=null;
		InvoiceSAT invoiceSAT=null;
		InvoiceDTO invoiceSATDTO =null;
		ByteArrayInputStream stream = null;
		InvoiceXML=InvoiceXML.trim();
		
		try {
            InvoiceXML = takeOffBOM(IOUtils.toInputStream(InvoiceXML, "UTF-8"));
            InvoiceXML= InvoiceXML.replace("?<?xml", "<?xml");
    } catch (IOException e1) {
            // TODO Auto-generated catch block
    	    log4j.error("Exception" , e1);
            e1.printStackTrace();
    }
		
		
		try {

			invoiceUP =getInvoiceXmlFromString(InvoiceXML);
			
			boolean receptorValido = false;
			List<UDC> receptores = udcService.searchBySystem("RECEPTOR");
			if(receptores != null) {
				for(UDC udc : receptores) {
					if(udc.getStrValue1().equals(invoiceUP.getRfcReceptor().trim())) {
						receptorValido = true;
						break;
					}
				}
			}
			if(!receptorValido) {
				resp=new ResponseGeneral(true,"validateInvoiceVsSatRECPTOR");
				resp.addMensaje("es", "el RFC no se tiene registrado.");
				resp.addMensaje("en", "The RFC is not registered.");
				return resp;
			}
			
			
			
			
		} catch (Exception e) {
			
			resp=new ResponseGeneral(true,"validateInvoiceVsSatDTO",e);
			resp.addMensaje("es", "La factura no es  XML valido.");
			resp.addMensaje("en", "The invoice is not XML valid.");
			log4j.error("Exception" , e);
			e.printStackTrace();
			return resp;
			
		}
		
		
		
		
		
		
		UDC banderaValida=udcDao.searchBySystemAndKey("SAT", "BANDERA_VALIDACION");
		
		if(!banderaValida.isBooleanValue()) {
			resp=new ResponseGeneral(false,"validateInvoiceVsSatDTO");
			resp.addMensaje("es", "El documento es XML valido.");
			resp.addMensaje("en", "The document is XML valid.");
			return resp;
		}
		
		
		if(invoiceUP.getUuid()==null||invoiceUP.getUuid().equals("null")||invoiceUP.getUuid().equals("")) {
			resp=new ResponseGeneral(true,"getFromSATuuidnull");
			resp.addMensaje("es", "El documento no contiene un uuid valido: uuid= "+invoiceUP.getUuid());
			resp.addMensaje("en", "The document does not contain a valid uuid: uuid= "+invoiceUP.getUuid());
			log4j.error(new Gson().toJson(resp));
			log4j.error("Factura: "+InvoiceXML);
			return resp;
		}
		
			invoiceSAT=invoiceSATDao.getByUuid(invoiceUP.getUuid());
			
			
			/*if(invoiceSAT==null) {
				resp=new ResponseGeneral(true,"getFromSAT");
				resp.addMensaje("es", "Estimado proveedor: A partir de mañana puede ingresar su Documento.");
				resp.addMensaje("en", "Dear supplier: Starting tomorrow you can enter your Document.");
				log4j.error(new Gson().toJson(resp));
				log4j.error("Factura: "+InvoiceXML);	
				return resp;
			}*/
			
			
			//Descarga Factura por Folio - SAT  DACG
		if (invoiceSAT == null) {
			UDC descargaSATUUID = udcDao.searchBySystemAndKey("DESCARGASATUUID", "ENABLED");
			try {

				Date datecurrent = Calendar.getInstance().getTime();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				String strDatecurrent = dateFormat.format(datecurrent);
				SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

				Date fechaTimbrado = null;
				Date fechaActual = null;
				try {
					fechaTimbrado = sdformat.parse(invoiceUP.getFechaTimbrado());
					fechaActual = sdformat.parse(strDatecurrent);
				} catch (ParseException e) {
					log4j.error("ParseException", e);
					e.printStackTrace();
				}

				log4j.info("fechaTimbrado: " + sdformat.format(fechaTimbrado));
				log4j.info("fechaActual: " + sdformat.format(fechaActual));
				if (fechaTimbrado.compareTo(fechaActual) >= 0) {
					resp = new ResponseGeneral(true, "getFromSAT");
					resp.addMensaje("es", "Estimado proveedor: A partir de mañana puede ingresar su Documento.");
					resp.addMensaje("en", "Dear supplier: Starting tomorrow you can enter your Document.");
					log4j.error(new Gson().toJson(resp));
					log4j.error("Factura: " + InvoiceXML);
					return resp;
				}
				if ("TRUE".equals(descargaSATUUID.getStrValue1())) {

					if (!descargaSATUUID.isBooleanValue()) {

						descargaSATUUID.setBooleanValue(true);
						udcDao.updateUDC(descargaSATUUID);

						invoiceSAT = massiveDownloadService.getDownloadCFDIByUuid(invoiceUP.getRfcReceptor(),
								invoiceUP.getUuid());

						if (invoiceSAT == null) {
							log4j.info("Proceso DownloadCFDIByUuid No encontró factura en SAT - UUID: " + invoiceUP.getUuid()
							+ " Continua flujo con XML original");
					resp = new ResponseGeneral(false, "validateInvoiceVsSatDTO");
					resp.addMensaje("es", "El documento es XML valido.");
					resp.addMensaje("en", "The document is XML valid.");
					return resp;
						}
					} else {
						log4j.info("Proceso DownloadCFDIByUuid ocupado - UUID: " + invoiceUP.getUuid()
								+ " Continua flujo con XML original");
						resp = new ResponseGeneral(false, "validateInvoiceVsSatDTO");
						resp.addMensaje("es", "El documento es XML valido.");
						resp.addMensaje("en", "The document is XML valid.");
						return resp;
					}

				} else {
					log4j.info("UDC desactivada DownloadCFDIByUuid - UUID: " + invoiceUP.getUuid());
					resp = new ResponseGeneral(false, "validateInvoiceVsSatDTO");
					resp.addMensaje("es", "Estimado proveedor: A partir de mañana puede ingresar su Documento.");
					resp.addMensaje("en", "Dear supplier: Starting tomorrow you can enter your Document.");
					return resp;
				}

			} catch (Exception ex) {

				log4j.error("Exception", ex);

			} finally {
				descargaSATUUID.setBooleanValue(false);
				descargaSATUUID.setDateValue(new Date());
				udcDao.updateUDC(descargaSATUUID);
			}
		}
			
			  stream = new  ByteArrayInputStream(invoiceSAT.getContent());
				
				try {
                    
					InvoiceXMLSAT = IOUtils.toString(stream, "UTF-8");
					 InvoiceXMLSAT = takeOffBOM(IOUtils.toInputStream(InvoiceXMLSAT, "UTF-8"));
					 InvoiceXMLSAT= InvoiceXMLSAT.replace("?<?xml", "<?xml");
				} catch (IOException e) {
					
					resp=new ResponseGeneral(true,"getSatXMLBase",e);
					resp.addMensaje("es", "El Documento no es  XML valido.");
					resp.addMensaje("en", "The Document is not XML valid.");
					log4j.error("REvisar Factura PROVENIENTE de SAT: id: "+invoiceSAT.getId() +", uuid: ------ "+invoiceSAT.getUuid());
					log4j.error("Exception" , e);
					log4j.error(new Gson().toJson(resp));
					log4j.error("Factura: "+InvoiceXML);
					e.printStackTrace();
					return resp;
					
				}
			
				try {

					invoiceSATDTO =getInvoiceXml(invoiceSAT.getContent());
					
					if (invoiceSATDTO==null) {
						resp=new ResponseGeneral(true,"validateInvoiceVsSatDTONull");
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						return resp;
					}
				} catch (Exception e) {
					
					resp=new ResponseGeneral(true,"validateInvoiceVsSatDTOGetInvoice",e);
					resp.addMensaje("es", "La factura no es  XML valido.");
					resp.addMensaje("en", "The invoice is not XML valid.");
					log4j.error("Exception" , e);
					e.printStackTrace();
					log4j.error(new Gson().toJson(resp));
					log4j.error("Factura: "+InvoiceXML);
					return resp;
					
				}
				
				//////////////////////////igualacion para que sea igual el xml que subieron con la que encontro en sat para evitar leer el que subieron y sea igual
				InvoiceXML= InvoiceXMLSAT;
				resp=new ResponseGeneral(false,"validateInvoiceVsSatDTO");
				resp.setDocument(invoiceSAT.getContent());/// aqui seteo el documento del sat
				if(resp.getDocument()!=null) {
					resp.addMensaje("es",  "Documento Valido");
					resp.addMensaje("en", "Document Valid");
					
					return resp;
				}
				////////////////////////////////////////////////
				
			if(!InvoiceXML.equals(InvoiceXMLSAT)) {
				if(!InvoiceXML.split(">")[0].equals(InvoiceXMLSAT.split(">")[0])) {
					resp=new ResponseGeneral(true,"validateInvoiceVsSatDTOConverJson");
					resp.addMensaje("es", "La estructura del XML ha sido alterado. Ingrese una factura valida.");
					resp.addMensaje("en", "The structure of the XML has been altered. Enter a valid invoice.");
					
					return resp;
				}
				try {
					
						  XMLUnit.setIgnoreWhitespace(true);
					        XMLUnit.setIgnoreAttributeOrder(true); 
					        
					        DetailedDiff diff = new DetailedDiff(XMLUnit.compareXML(InvoiceXMLSAT, InvoiceXML));

					        List<?> allDifferences = diff.getAllDifferences();
					        
					        if (allDifferences.size()>0) {
					        	String etiquetas="";
					        	 for (Object object : allDifferences) {
										etiquetas=etiquetas+object.toString().split("@")[2]+"<br/>";
									}
					        	        log4j.info("Differences found: "+ diff.toString()+ allDifferences.size());
								        resp=new ResponseGeneral(true,"validateInvoiceVsSatDTOXMLAnalize");
										resp.addMensaje("es", "Su Factura no pudo ser cargada.<br/>Se encontraron alteraciones en la siguientes etiquetas de su archivo cargado:  <br/><br/>"+etiquetas+"<br/>Favor de ingresar una factura valida ");
										resp.addMensaje("en", "Your Invoice could not be uploaded.<br/>Alterations were found in the following tags of your uploaded file:  <br/><br/>"+etiquetas+" <br/>Please enter a valid invoice");
										return resp;   
								        
							}
					     
					
				} catch (Exception e) {
					resp=new ResponseGeneral(true,"validateInvoiceVsSatDTOConverJson",e);
					resp.addMensaje("es", "La factura no es  XML valido.");
					resp.addMensaje("en", "The invoice is not XML valid.");
					log4j.error("Exception" , e);
					e.printStackTrace();
					return resp;
				} 
				
				
				
			}
				
				
			resp=new ResponseGeneral(false,"validateInvoiceVsSatDTO");
			resp.addMensaje("es", "La factura es XML valido.");
			resp.addMensaje("en", "The invoice is XML valid.");
				
			
			
		
		return resp;
	}
	
	
	
	
	
	public ResponseGeneral validateInvoiceTaxVault(InvoiceDTO inv, String xmlContent) {
		ResponseGeneral response=null;
		ResponseGeneral resps=validateInvoiceVsSat(inv,xmlContent);	
		if (resps.isError()) {
			return resps;
		}else {
			if (resps.getDocument() != null) {
				InvoiceDTO invoiceUP =null;
				String InvoiceXML=null;
				ResponseGeneral resp=null;
				
				 ByteArrayInputStream stream = new  ByteArrayInputStream(resps.getDocument());
				try {
						InvoiceXML = IOUtils.toString(stream, "UTF-8");
						InvoiceXML = takeOffBOM(IOUtils.toInputStream(InvoiceXML, "UTF-8"));
						InvoiceXML = InvoiceXML.replace("?<?xml", "<?xml");
					} catch (IOException e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatXML",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp;
						
					}
				try {

						invoiceUP =getInvoiceXmlFromString(InvoiceXML);
						
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
							resp=new ResponseGeneral(false,"validateInvoiceVsSatRECPTOR");
							resp.addMensaje("es", "el RFC no se tiene registrado.");
							resp.addMensaje("en", "The RFC is not registered.");
							return resp;
						}
						
						
						
						
					} catch (Exception e) {
						
						resp=new ResponseGeneral(true,"validateInvoiceVsSatDTO",e);
						resp.addMensaje("es", "La factura no es  XML valido.");
						resp.addMensaje("en", "The invoice is not XML valid.");
						log4j.error("Exception" , e);
						e.printStackTrace();
						return resp;
						
					}
				inv=invoiceUP;
				xmlContent=InvoiceXML;
			}
			
			
			
		}

		
		DecimalFormat currencyFormat = new DecimalFormat("$#,###.###");
		UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");
		
		TaxVaultDocument taxVaultDocument = taxVaultDocumentDao.getInvoiceByuuid(inv.getUuid());
		if(taxVaultDocument != null) {
			
			String resp_es="";
			 String resp_en="";
			 
			 switch(inv.getTipoComprobante()) {
			  case "I":
				  resp_es="La factura";
				  resp_en="The invoice";
			    break;
			  case "P":
				  resp_es="El complemento de pago";
				  resp_en="The pay complement";
			    break;
			  case "E":
				  resp_es="La nota de credito";
				  resp_en="The credit note";
			  default:
				break;
			    // code block
			}
			
			response=new ResponseGeneral(true,"FacturaRepetida");
			response.addMensaje("es", resp_es+" que intenta ingresar ya se encuentra cargada previamente. ");
			response.addMensaje("en", resp_en+" you are trying to enter is already preloaded.");
				return response;
		}

		if (udcCfdi != null) {
			if (!"".equals(udcCfdi.getStrValue1())) {
				if ("TRUE".equals(udcCfdi.getStrValue1())) {
					String vcfdi = validaComprobanteSAT(inv);
					if (!"".equals(vcfdi)) {
						response=new ResponseGeneral(true,"validaComprobanteSAT",vcfdi);
						response.addMensaje("es", "Error de validación ante el SAT, favor de validar con su emisor fiscal. ");
						response.addMensaje("en", "Validation error before the SAT, please validate with your tax issuer.");
							return response;
					}

//					String vNull = validateInvNull(inv);
//					if (!"".equals(vNull)) {
//						response=new ResponseGeneral(true,"validateInvNull",vNull);
//						response.addMensaje("es",  "Error al validar el archivo XML, no se encontró el campo " + vNull + ".");
//						response.addMensaje("en", "Error validating XML file, " + vNull + " field not found.");
//							return response;
//					}
				}
			}
		} else {
			String vcfdi = validaComprobanteSAT(inv);
			if (!"".equals(vcfdi)) {
				response=new ResponseGeneral(true,"validaComprobanteSAT",vcfdi);
				response.addMensaje("es", "Error de validación ante el SAT, favor de validar con su emisor fiscal. ");
				response.addMensaje("en", "Validation error before the SAT, please validate with your tax issuer.");
					return response;
			}

//			String vNull = validateInvNull(inv);
//			if (!"".equals(vNull)) {
//				response=new ResponseGeneral(true,"validateInvNull",vNull);
//				response.addMensaje("es",  "Error al validar el archivo XML, no se encontró el campo " + vNull + ".");
//				response.addMensaje("en", "Error validating XML file, " + vNull + " field not found.");
//				return response;
//			}
		}
		
		response=new ResponseGeneral(false,"validateInvoiceTaxVaultInter");
		response.addMensaje("es",  "Factura Valida");
		response.addMensaje("en", "Invoice Valid");
		response.setDocument(resps.getDocument());
			return response;
	}
	public static String takeOffBOM(InputStream inputStream) throws IOException {
	    BOMInputStream bomInputStream = new BOMInputStream(inputStream);
	    return IOUtils.toString(bomInputStream, "UTF-8");
	}
	
	
	 public  byte[] extraerBytesXml(ZipInputStream zis) throws IOException {
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len;
	        while ((len = zis.read(buffer)) > 0) {
	            bos.write(buffer, 0, len);
	        }
	        return bos.toByteArray();
	    }

	 public  byte[] extraerArchivoDelZip(byte[] zipBytes, String nombreArchivo) {
	        try (ByteArrayInputStream bis = new ByteArrayInputStream(zipBytes);
	             ZipInputStream zis = new ZipInputStream(bis)) {

	            ZipEntry entry;
	            while ((entry = zis.getNextEntry()) != null) {
	                if (!entry.isDirectory() && entry.getName().equalsIgnoreCase(nombreArchivo)) {
	                    return extraerBytesXml(zis);
	                }
	                zis.closeEntry();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null; // Devolver null si el archivo no se encuentra
	    }
	 
	 public static void main(String[] args) {
			String fechaFactura = "2025-01-01T15:22:57";
			fechaFactura = fechaFactura.replace("T", " ");
			SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
			Date invDate = null;
			try {
				invDate = sdf.parse(fechaFactura);
			} catch (Exception e) {
//				log4j.error("Exception" , e);
				e.printStackTrace();
			}
			
			
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, currentYear);
			cal.set(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date startYear = cal.getTime();
			
				if (invDate.compareTo(startYear) < 0) {
					String  t= "La fecha de emisión de la factura no puede ser anterior al primero de Enero del año en curso";
				}
			
			
			
	}
}
