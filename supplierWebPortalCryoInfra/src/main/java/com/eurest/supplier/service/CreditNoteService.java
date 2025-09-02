package com.eurest.supplier.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.dao.DocumentsDao;
import com.eurest.supplier.dao.PaymentSupplierDao;
import com.eurest.supplier.dao.PaymentSupplierDetailDao;
import com.eurest.supplier.dao.SupplierDao;
import com.eurest.supplier.dao.UDCDao;
import com.eurest.supplier.dao.UserDocumentDao;
import com.eurest.supplier.dto.BlockerSupReceiptDTO;
import com.eurest.supplier.dto.FileDTO;
import com.eurest.supplier.dto.ForeingInvoice;
import com.eurest.supplier.dto.IntegerListDTO;
import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.dto.SupplierDTO;
import com.eurest.supplier.edi.BatchJournalDTO;
import com.eurest.supplier.edi.BatchJournalEntryDTO;
import com.eurest.supplier.edi.BatchVoucherTransactionsDTO;
import com.eurest.supplier.edi.SupplierJdeDTO;
import com.eurest.supplier.edi.VoucherDetailDTO;
import com.eurest.supplier.edi.VoucherHeaderDTO;
import com.eurest.supplier.edi.VoucherSummaryDTO;
import com.eurest.supplier.model.ApprovalBatchFreight;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.ForeignInvoiceTable;
import com.eurest.supplier.model.NextNumber;
import com.eurest.supplier.model.PaymentSupplier;
import com.eurest.supplier.model.PaymentSupplierDetail;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.PurchaseOrderDetail;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.JdeJavaJulianDateTools;
import com.eurest.supplier.util.PDFUtils;
import com.eurest.supplier.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Service("creditNoteService")

public class CreditNoteService {
	
	  @Autowired
	  SupplierService supplierService;
	  
	  @Autowired
	  PurchaseOrderService purchaseOrderService;
	  
	  @Autowired
	  private JavaMailSender mailSenderObj;
	  
	  @Autowired
	  StringUtils stringUtils;
	  
	  @Autowired
	  UDCDao udcDao;
	 
	  @Autowired
	  EDIService EDIService;
	  
	  @Autowired
	  DocumentsService documentsService;
	  
	  @Autowired
	  UsersService userService;
	  
	  @Autowired
	  UdcService udcService;
	  
	  @Autowired
	  ApprovalService approvalService;
	  
	  @Autowired
	  FiscalDocumentService fiscalDocumentService;
	  
	  @Autowired
	  FiscalDocumentConceptService fiscalDocumentConceptService;
	  
	  @Autowired
	  XmlToPojoService xmlToPojoService;
	  
	  @Autowired
	  NextNumberService nextNumberService;
	  
	  @Autowired
	  UserDocumentDao userDocumentDao;
	  
	  @Autowired
	  DocumentsDao documentsDao;
	  
	  @Autowired
	  SupplierDao supplierDao;
	  
	  @Autowired
	  SftService sftService;
	  
	  @Autowired
	  ApprovalBatchFreightService approvalBatchFreightService;
	  
	  @Autowired
	  UsersService usersService;
	  
	   @Autowired
	  PaymentSupplierDao paymentSupplierDao;
	   
	   @Autowired
	   DataAuditService dataAuditService;
	   
	   @Autowired
		  PaymentSupplierDetailDao paymentSupplierDetailDao;
	  
	  
	  final long MAX_ATTACHED_SIZE = 20971520L; //20MB Aprox.
	  static String TIMESTAMP_DATE_PATTERN = "yyyy-MM-dd";	  
	  private Logger log4j = Logger.getLogger(CreditNoteService.class);
	
	public Map<String, Object> setPayents(List<Receipt> receipList) {
		int total =0 ;
		try{
			log4j.info("*********** STEP 3: setPayments:receipList:" + receipList);
		   
			if(receipList != null) {
			/*ObjectMapper mapper = new ObjectMapper();
			Receipt[] response = mapper.readValue(receipList, Receipt[].class);*/
			List<Receipt> objList = receipList;
			      if (!objList.isEmpty()) {
			      
			    	  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
					        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
					        
							for (Receipt o : objList) {
								o.setUuid(o.getUuid().trim());
							}
							
							purchaseOrderService.updatePaymentReceipts(objList);
	 					        
							for (Receipt o : objList) {										
								Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
								if(s!=null) {
									List<Receipt> receiptUUIDList = purchaseOrderService.getReceiptsByUUID(o.getUuid().trim());
									int receiptNumber = 0; 
									if(receiptUUIDList != null && receiptUUIDList.size()>0) {
										receiptNumber = receiptUUIDList.get(0).getDocumentNumber();
									}
 	 								String emailRecipient = (s.getEmailSupplier());
 	 	 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
 	 	 							String emailContent = AppConstants.EMAIL_PAYMENT_RECEIPT_NOTIF_CONTENT;
 	 	 							emailContent = emailContent.replace("_UUID_", o.getUuid());
 	 	 							emailContent = emailContent.replace("_GR_", String.valueOf(receiptNumber));
									String strDate = dateFormat.format(o.getPaymentDate()); 
									emailContent = emailContent.replace("_PO_", String.valueOf(o.getOrderNumber()));
									emailContent = emailContent.replace("_DATE_", strDate);

 	 	 							String currency = format.format(o.getPaymentAmount());
 	 	 							emailContent = emailContent.replace("_AMOUNT_", currency);
 	 	 							emailContent = emailContent.replace("_PID_", o.getPaymentReference());

 	 	 							emailAsyncSup.setProperties(
 	 	 									AppConstants.EMAIL_PAYMENT_RECEIPT_NOTIF,
 	 	 									stringUtils.prepareEmailContent(emailContent + AppConstants.EMAIL_PORTAL_LINK),emailRecipient);
 	 	 							emailAsyncSup.setMailSender(mailSenderObj);
 	 	 							emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
 	 	 							Thread emailThreadSup = new Thread(emailAsyncSup);
 	 	 							emailThreadSup.start();
 								}
								
							}
							total = receipList.size();
							log4j.info("Guardado:" + receipList.size());
						
			      }
			}
			
			
			return mapOK("Respuesta OK setPayments",total);
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - setPayments");
		}
		
	}
	
	public Map<String, Object> getPaymentPendingReceipts(int start, int limit) {
		try{
			log4j.info("*********** STEP 1: getPaymentPendingReceipts:start:" + start + " -limit:" + limit);
			String jsonInString = null;
			List<String> uuidList = null;
			int size = 0;
			List<Receipt> poList = purchaseOrderService.getPaymentPendingReceipts(start, limit);
			if (poList != null) {
				if (poList.size() > 0) {
					IntegerListDTO idto = new IntegerListDTO();
					uuidList = new ArrayList<String>();
					for(Receipt po : poList) {
						uuidList.add(po.getUuid());
					}
					if(uuidList.size() > 0) {
						idto.setUuidList(uuidList);
						ObjectMapper jsonMapper = new ObjectMapper();
						jsonInString = jsonMapper.writeValueAsString(idto);

					//return mapOK(jsonInString,uuidList.size() );
					}
					size = uuidList.size();
				}
				}
			return mapOK(jsonInString,size );
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getPaymentPendingReceipts");
			
		}
	}
	public Map<String, Object> getSupplierForPO(List<PurchaseOrder> supList) {
		try{
			log4j.info("*********** STEP 3: getSupplierForPO:supList:" + supList);
		    int total=0;
			if(supList != null) {
			/*ObjectMapper mapper = new ObjectMapper();
			PurchaseOrder[] response = mapper.readValue(supList, PurchaseOrder[].class);
			List<PurchaseOrder> objList = Arrays.asList(response);*/
			List<PurchaseOrder> objList = supList;
			      if (!objList.isEmpty()) {
			    	  total = objList.size();
			    	  log4j.info("REGISTROS:" + objList.size());
						
						for(PurchaseOrder o : objList) {
							if(o.getPurchaseOrderDetail() != null && !o.getPurchaseOrderDetail().isEmpty()) {
								for(PurchaseOrderDetail d : o.getPurchaseOrderDetail()) {
	 								if((d.getAmuntReceived() < 0d|| d.getForeignAmount() < 0d) 
	 										&& AppConstants.JDE_RETENTION_CODE.equals(String.valueOf(d.getObjectAccount()).trim())) {
	 									
	 									d.setAmount(d.getAmount()*-1);
	 									d.setAmuntReceived(d.getAmuntReceived()*-1);
	 									d.setForeignAmount(d.getForeignAmount()*-1);
	 									d.setPending(d.getPending()*-1);
	 									d.setQuantity(d.getQuantity()*-1);
	 									d.setReceived(d.getReceived()*-1); 		 									
	 									d.setReceiptType(AppConstants.RECEIPT_CODE_RETENTION);
	 								}
								}
							}
							o.setPortalRecordDate(new Date());
							o.setOrderStauts(AppConstants.STATUS_OC_RECEIVED);							
							Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
							if(s!=null) {
								o.setLongCompanyName(s.getRazonSocial());
							}
						}
						purchaseOrderService.saveMultiple(objList);
						List<PurchaseOrder> savedList = purchaseOrderService.saveMultiple(objList);
						//System.out.println("Procesado:" + response.length);
						
						for(PurchaseOrder o : savedList) {
							Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
								if(s!=null) {
									
									String emailContent = AppConstants.EMAIL_PURCHASE_NEW;
									emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(o.getOrderNumber()));
									emailContent = emailContent.replace("_ORDERTYPE_", o.getOrderType());
									emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);
									emailContent = emailContent.replace("_ADDNUMBER_", s.getAddresNumber());
									emailContent = emailContent.replace("_SOCIALREASON_", o.getDescription());
									
									EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
									emailAsyncSup.setProperties(
											AppConstants.EMAIL_NEW_ORDER_NOTIF + o.getOrderNumber() + "-" + o.getOrderType(),
											stringUtils.prepareEmailContent(emailContent),
											 o.getEmail());
									emailAsyncSup.setMailSender(mailSenderObj);
									emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
									Thread emailThreadSup = new Thread(emailAsyncSup);
									emailThreadSup.start();
								}
						}
						
					}
			
			      //getPurchaseReceiptList(supList);
			}
			return mapOK("Respuesta OK getSupplierForPO",total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			
			return mapError(e.getMessage() + " - getSupplierForPO");
		}
	}
	
	public Map<String, Object> setPoReceiptsHistory(List<Receipt> recpList){
		int total = 0;
		try{
			log4j.info("*********** STEP 4: setPoReceiptsHistory:recpList:" + recpList);

				if (recpList != null) {
					/*ObjectMapper mapper = new ObjectMapper();
					Receipt[] response = mapper.readValue(recpList, Receipt[].class);
					List<Receipt> objList = Arrays.asList(response);*/
					
					List<Receipt> objList = recpList;
					if (!objList.isEmpty()) {
						log4j.info("REGISTROS:" + objList.size());
						
						List<Receipt> insertedRows = purchaseOrderService.saveMultipleReceipt(objList);
						List<PurchaseOrder> updateList = new ArrayList<PurchaseOrder>();
						List<Integer> idList = new ArrayList<Integer>();
						
						for(Receipt o : insertedRows) {
							PurchaseOrder ao = purchaseOrderService.searchbyOrderAndAddressBook(o.getOrderNumber(), o.getAddressNumber(), o.getOrderType());			
							if(ao != null) {
								if(!idList.contains(ao.getId())) {
	 								ao.setOrderStauts(AppConstants.STATUS_OC_APPROVED);
	 								ao.setRelatedStatus(AppConstants.STATUS_UNCOMPLETE);
	 								ao.setStatus(AppConstants.STATUS_OC_APPROVED);
	 								updateList.add(ao);
	 								idList.add(ao.getId());
								}
							}
							
							Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
							if(s!=null) {
								String emailContent = AppConstants.EMAIL_MIDDLEWARE_MSG_1;
								emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(o.getOrderNumber()));
								emailContent = emailContent.replace("_DOCNUM_", String.valueOf(o.getDocumentNumber()));
								emailContent = emailContent.replace("_ORDERTYPE_", o.getOrderType());
								emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);
								
								String emailRecipient = (s.getEmailSupplier());
	 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	 							emailAsyncSup.setProperties(
	 									AppConstants.EMAIL_NEW_RECEIPT_NOTIF + o.getOrderNumber(),
	 									stringUtils.prepareEmailContent(emailContent),
	 									emailRecipient);
	 							emailAsyncSup.setMailSender(mailSenderObj);
	 							emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
	 							Thread emailThreadSup = new Thread(emailAsyncSup);
	 							emailThreadSup.start();
							}
						}
						total = updateList.size();
						purchaseOrderService.updateMultiple(updateList); 							
						log4j.info("Procesados los recibos");
					}
				}
			return mapOK("Respuesta OK getSupplierForPO",total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getSupplierForPO");
		}
	}
	
	public Map<String, Object> getPoReceiptsHistory(String recpList){
		int total = 0;
		try{
			log4j.info("*********** STEP 4: setPoReceiptsHistory:recpList:" + recpList);

				if (recpList != null) {
					ObjectMapper mapper = new ObjectMapper();
					Receipt[] response = mapper.readValue(recpList, Receipt[].class);
					List<Receipt> objList = Arrays.asList(response);
					if (!objList.isEmpty()) {
						log4j.info("REGISTROS:" + objList.size());
						
						List<Receipt> insertedRows = purchaseOrderService.saveMultipleReceipt(objList);
						List<PurchaseOrder> updateList = new ArrayList<PurchaseOrder>();
						List<Integer> idList = new ArrayList<Integer>();
						
						for(Receipt o : insertedRows) {
							PurchaseOrder ao = purchaseOrderService.searchbyOrderAndAddressBook(o.getOrderNumber(), o.getAddressNumber(), o.getOrderType());			
							if(ao != null) {
								if(!idList.contains(ao.getId())) {
	 								ao.setOrderStauts(AppConstants.STATUS_OC_APPROVED);
	 								ao.setRelatedStatus(AppConstants.STATUS_UNCOMPLETE);
	 								ao.setStatus(AppConstants.STATUS_OC_APPROVED);
	 								updateList.add(ao);
	 								idList.add(ao.getId());
								}
							}
							
							Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
							if(s!=null) {
								String emailContent = AppConstants.EMAIL_MIDDLEWARE_MSG_1;
								emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(o.getOrderNumber()));
								emailContent = emailContent.replace("_DOCNUM_", String.valueOf(o.getDocumentNumber()));
								emailContent = emailContent.replace("_ORDERTYPE_", o.getOrderType());
								emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);
								
								String emailRecipient = (s.getEmailSupplier());
	 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	 							emailAsyncSup.setProperties(
	 									AppConstants.EMAIL_NEW_RECEIPT_NOTIF + o.getOrderNumber(),
	 									stringUtils.prepareEmailContent(emailContent),
	 									emailRecipient);
	 							emailAsyncSup.setMailSender(mailSenderObj);
	 							emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
	 							Thread emailThreadSup = new Thread(emailAsyncSup);
	 							emailThreadSup.start();
							} 							
						}
						total = updateList.size();
						purchaseOrderService.updateMultiple(updateList); 							
						log4j.info("Procesados los recibos");
					}
				}
			return mapOK("Respuesta OK getSupplierForPO",total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getSupplierForPO");
		}
	}

	public Map<String, Object> setPurchaseReceiptList(List<Receipt> recpList){
		int total = 0;
		try{
	//		recpList = "[{\"id\":0,\"orderCompany\":\"00030\",\"orderNumber\":213,\"orderType\":\"OS\",\"addressNumber\":\"525175.0\",\"documentNumber\":4811,\"documentType\":\"OV\",\"receiptDate\":1619413200000,\"lineNumber\":1000,\"itemNumber\":null,\"amountReceived\":2900000.0,\"foreignAmountReceived\":0.0,\"amountOpen\":0.0,\"quantityReceived\":100.0,\"uom\":\"EA\",\"currencyCode\":\"MXP\",\"transactionOriginator\":null,\"matchType\":\"1\",\"status\":null,\"lineType\":\"J \",\"accountId\":\"01036193\",\"unitCost\":29000.0,\"exchangeRate\":0.0,\"foreignUnitCost\":0.0,\"uuid\":null,\"folio\":null,\"serie\":null,\"invDate\":null,\"receiptLine\":1,\"estPmtDate\":null,\"remark\":\"                              \",\"paymentStatus\":null,\"paymentDate\":null,\"paymentReference\":null,\"paymentAmount\":0.0,\"receiptType\":null,\"objectAccount\":\"7600  \",\"taxCode\":\"IVAFLE    \",\"paymentTerms\":\"NC \"},{\"id\":0,\"orderCompany\":\"00014\",\"orderNumber\":1687,\"orderType\":\"OS\",\"addressNumber\":\"525862.0\",\"documentNumber\":4641,\"documentType\":\"OV\",\"receiptDate\":1618462800000,\"lineNumber\":3000,\"itemNumber\":null,\"amountReceived\":692455.0,\"foreignAmountReceived\":0.0,\"amountOpen\":0.0,\"quantityReceived\":100.0,\"uom\":\"EA\",\"currencyCode\":\"MXP\",\"transactionOriginator\":null,\"matchType\":\"1\",\"status\":null,\"lineType\":\"J \",\"accountId\":\"00879425\",\"unitCost\":6924.55,\"exchangeRate\":0.0,\"foreignUnitCost\":0.0,\"uuid\":null,\"folio\":null,\"serie\":null,\"invDate\":null,\"receiptLine\":1,\"estPmtDate\":null,\"remark\":\"                              \",\"paymentStatus\":null,\"paymentDate\":null,\"paymentReference\":null,\"paymentAmount\":0.0,\"receiptType\":null,\"objectAccount\":\"7311  \",\"taxCode\":\"IVP15     \",\"paymentTerms\":\"   \"},{\"id\":0,\"orderCompany\":\"00014\",\"orderNumber\":1687,\"orderType\":\"OS\",\"addressNumber\":\"525862.0\",\"documentNumber\":4739,\"documentType\":\"OV\",\"receiptDate\":1621573200000,\"lineNumber\":3000,\"itemNumber\":null,\"amountReceived\":692455.0,\"foreignAmountReceived\":0.0,\"amountOpen\":0.0,\"quantityReceived\":100.0,\"uom\":\"EA\",\"currencyCode\":\"MXP\",\"transactionOriginator\":null,\"matchType\":\"1\",\"status\":null,\"lineType\":\"J \",\"accountId\":\"00879425\",\"unitCost\":6924.55,\"exchangeRate\":0.0,\"foreignUnitCost\":0.0,\"uuid\":null,\"folio\":null,\"serie\":null,\"invDate\":null,\"receiptLine\":2,\"estPmtDate\":null,\"remark\":\"                              \",\"paymentStatus\":null,\"paymentDate\":null,\"paymentReference\":null,\"paymentAmount\":0.0,\"receiptType\":null,\"objectAccount\":\"7311  \",\"taxCode\":\"IVP15     \",\"paymentTerms\":\"   \"},{\"id\":0,\"orderCompany\":\"00014\",\"orderNumber\":1687,\"orderType\":\"OS\",\"addressNumber\":\"525862.0\",\"documentNumber\":4811,\"documentType\":\"OV\",\"receiptDate\":1623733200000,\"lineNumber\":3000,\"itemNumber\":null,\"amountReceived\":692455.0,\"foreignAmountReceived\":0.0,\"amountOpen\":0.0,\"quantityReceived\":100.0,\"uom\":\"EA\",\"currencyCode\":\"MXP\",\"transactionOriginator\":null,\"matchType\":\"1\",\"status\":null,\"lineType\":\"J \",\"accountId\":\"00879425\",\"unitCost\":6924.55,\"exchangeRate\":0.0,\"foreignUnitCost\":0.0,\"uuid\":null,\"folio\":null,\"serie\":null,\"invDate\":null,\"receiptLine\":3,\"estPmtDate\":null,\"remark\":\"                              \",\"paymentStatus\":null,\"paymentDate\":null,\"paymentReference\":null,\"paymentAmount\":0.0,\"receiptType\":null,\"objectAccount\":\"7311  \",\"taxCode\":\"IVP15     \",\"paymentTerms\":\"   \"},{\"id\":0,\"orderCompany\":\"00014\",\"orderNumber\":1687,\"orderType\":\"OS\",\"addressNumber\":\"525862.0\",\"documentNumber\":4739,\"documentType\":\"OV\",\"receiptDate\":1621573200000,\"lineNumber\":4000,\"itemNumber\":null,\"amountReceived\":692455.0,\"foreignAmountReceived\":0.0,\"amountOpen\":0.0,\"quantityReceived\":100.0,\"uom\":\"EA\",\"currencyCode\":\"MXP\",\"transactionOriginator\":null,\"matchType\":\"1\",\"status\":null,\"lineType\":\"J \",\"accountId\":\"00879425\",\"unitCost\":6924.55,\"exchangeRate\":0.0,\"foreignUnitCost\":0.0,\"uuid\":null,\"folio\":null,\"serie\":null,\"invDate\":null,\"receiptLine\":1,\"estPmtDate\":null,\"remark\":\"                              \",\"paymentStatus\":null,\"paymentDate\":null,\"paymentReference\":null,\"paymentAmount\":0.0,\"receiptType\":null,\"objectAccount\":\"7311  \",\"taxCode\":\"IVP15     \",\"paymentTerms\":\"   \"},{\"id\":0,\"orderCompany\":\"00002\",\"orderNumber\":44360,\"orderType\":\"OC\",\"addressNumber\":\"600013.0\",\"documentNumber\":83665,\"documentType\":\"OV\",\"receiptDate\":1617170400000,\"lineNumber\":1000,\"itemNumber\":null,\"amountReceived\":2208506.0,\"foreignAmountReceived\":107196.0,\"amountOpen\":0.0,\"quantityReceived\":100.0,\"uom\":\"PC\",\"currencyCode\":\"USD\",\"transactionOriginator\":null,\"matchType\":\"1\",\"status\":null,\"lineType\":\"N \",\"accountId\":\"00819981\",\"unitCost\":22085.0559,\"exchangeRate\":20.6025,\"foreignUnitCost\":1.07196E7,\"uuid\":null,\"folio\":null,\"serie\":null,\"invDate\":null,\"receiptLine\":1,\"estPmtDate\":null,\"remark\":\"                              \",\"paymentStatus\":null,\"paymentDate\":null,\"paymentReference\":null,\"paymentAmount\":0.0,\"receiptType\":null,\"objectAccount\":\"7600  \",\"taxCode\":\"IVP15     \",\"paymentTerms\":\"N60\"}]";
			log4j.info("*********** STEP 4: setPurchaseReceiptList:recpList:" + recpList);

				if (recpList != null) {
					/*ObjectMapper mapper = new ObjectMapper();
					Receipt[] response = mapper.readValue(recpList, Receipt[].class);
					List<Receipt> objList = Arrays.asList(response);*/
					
					List<Receipt> objList = recpList;
					if (!objList.isEmpty()) {
						log4j.info("REGISTROS:" + objList.size());
						
						List<Receipt> insertedRows = purchaseOrderService.saveMultipleReceipt(objList);
						List<PurchaseOrder> updateList = new ArrayList<PurchaseOrder>();
						List<Integer> idList = new ArrayList<Integer>();
						
						for(Receipt o : insertedRows) {
							PurchaseOrder ao = purchaseOrderService.searchbyOrderAndAddressBook(o.getOrderNumber(), o.getAddressNumber(), o.getOrderType());			
							if(ao != null) {
								if(!idList.contains(ao.getId())) {
	 								ao.setOrderStauts(AppConstants.STATUS_OC_APPROVED);
	 								ao.setRelatedStatus(AppConstants.STATUS_UNCOMPLETE);
	 								ao.setStatus(AppConstants.STATUS_OC_APPROVED);
	 								updateList.add(ao);
	 								idList.add(ao.getId());
								}
							}
							
							Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
							if(s!=null) {
								String emailContent = AppConstants.EMAIL_MIDDLEWARE_MSG_1;
								emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(o.getOrderNumber()));
								emailContent = emailContent.replace("_DOCNUM_", String.valueOf(o.getDocumentNumber()));
								emailContent = emailContent.replace("_ORDERTYPE_", o.getOrderType());
								emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);
								
								String emailRecipient = (s.getEmailSupplier());
	 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	 							emailAsyncSup.setProperties(
	 									AppConstants.EMAIL_NEW_RECEIPT_NOTIF + o.getOrderNumber(),
	 									stringUtils.prepareEmailContent(emailContent),
	 									emailRecipient);
	 							emailAsyncSup.setMailSender(mailSenderObj);
	 							emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
	 							Thread emailThreadSup = new Thread(emailAsyncSup);
	 							emailThreadSup.start();
							} 							
						}
						total = updateList.size();
						purchaseOrderService.updateMultiple(updateList); 							
						log4j.info("Procesados los recibos");
					}
				}
			return mapOK("Respuesta OK getSupplierForPO",total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getSupplierForPO");
		}
	}

	
	public Map<String, Object> getPostAddressBook(int start, int limit){
		try{
			log4j.info("*********** STEP 1: getPostAddressBook:start:" + start + ",limit:" + limit);
			List<Supplier> supDtoList = supplierService.getListPendingReplication(start, limit);
			List<SupplierJdeDTO> supDtoListReturn = new ArrayList<SupplierJdeDTO>();
			ObjectMapper jsonMapper = new ObjectMapper();
			String jsonInString = null;
			if (!supDtoList.isEmpty()) {
				 
				for (Supplier sdto : supDtoList) {
					supDtoListReturn.add(EDIService.registerNewAddressBookMdw(sdto));
				}

				jsonMapper = new ObjectMapper();
				jsonInString = jsonMapper.writeValueAsString(supDtoListReturn);
				
			}
			
		    return mapOK(jsonInString,supDtoListReturn.size());
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getSuppliers");
		}
	}
	
	
	//public Map<String, Object> updatePostAddressBook(SupplierJdeDTO supplierList){
		public Map<String, Object> updatePostAddressBook(int id, String addressNumber){
		int total=100;
		 Gson gson = new Gson();
		 Date currentDate = new Date();
		//SupplierJdeDTO jdeS = gson.fromJson(supplierList, SupplierJdeDTO.class);
		//SupplierJdeDTO jdeS = supplierList;
		//Supplier s = supplierService.searchByAddressNumber(jdeS.getAddresNumber());
		
		Supplier s = supplierService.getSupplierById(id);
		String addressNumberOld = s.getAddresNumber();
		try{
		    
	          if(addressNumber==null) {
	              s.setAddresNumber(null);
	              s.setReplicationStatus(AppConstants.STATUS_ERROR_REPLICATION);
	              s.setReplicationMessage("Error JDE");
	              s.setReplicationDate(new Date());
	              
	          }else {
	        	  s.setAddresNumber(addressNumber);
	        	  s.setReplicationStatus(AppConstants.STATUS_SUCCESS_REPLICATION);
	              s.setReplicationMessage(null);
	              s.setReplicationDate(new Date());
	              supplierDao.updateSupplier(s);
	        	  List<UserDocument> list = null;
	        	 /* if(s.getTaxId() != null && !"".equals(s.getTaxId())) {
	        		list =  this.documentsService.searchByAddressNumber("NEW_" + s.getTaxId());
	        	  }else {
	        		list =  this.documentsService.searchByAddressNumber("NEW_" + s.getRfc());
	        	  }*/
	        	  
	        	  list = this.documentsService.searchByAddressNumber(addressNumberOld);
	              for (UserDocument d : list) {
	                d.setAddressBook(addressNumber);
	                d.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);//JSAAVEDRA: Se deja listo para el proceso que obtiene los archivos.
	                this.documentsService.update(d, new Date(), "admin");
	              } 	              
	              
	            if(s.getUpdateApprovalFlow() == null || s.getUpdateApprovalFlow().equals("")) {
	              log4j.info("***** ADDRESSNUMBER:" + s.getAddresNumber() + "ADRESSNUMBER OLD:" + addressNumberOld);
	        	  UDC role = udcService.searchBySystemAndKey("ROLES", "SUPPLIER");
	              UDC userType = udcService.searchBySystemAndKey("USERTYPE", "SUPPLIER");

	        	  String tempPass = "cryo20";
	        	  String encodePass = Base64.getEncoder().encodeToString(tempPass.trim().getBytes());
	        	  encodePass = "==a20$" + encodePass; 
	              Users u = new Users();
	              u.setEmail(s.getEmailSupplier());
	              u.setName(s.getRazonSocial());
	              u.setPassword(encodePass);
	              u.setUserName(s.getAddresNumber());
	              u.setUserRole(role);
	              u.setUserType(userType);
	              u.setEnabled(true);
	              userService.save(u, null, null);
	              approvalService.updateSupplier(s);
	              
				  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();				
				  String emailContent = AppConstants.EMAIL_ACCEPT_SUPPLIER_NOTIFICATION;
				  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
				  emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
				  emailContent = emailContent.replace("_USER_", s.getAddresNumber());
				  emailContent = emailContent.replace("_PASS_", tempPass.trim());

				  emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_ACCEPT, stringUtils.prepareEmailContent(emailContent), s.getEmailSupplier());
				  emailAsyncSup.setMailSender(mailSenderObj);
				  Thread emailThreadSup = new Thread(emailAsyncSup);
				  emailThreadSup.start();	
	          }else {
	        	  s.setUpdateApprovalFlow("");
	              s.setDataUpdateList("");
	              approvalService.updateSupplier(s);
	              
	              Users user = userService.getByUserName(s.getAddresNumber());
	              if(user != null) {
	            	  user.setEnabled(true);
	            	  userService.update(user, null, null);	            	  
	              }
	              
		    	  //Multiusuarios: Actualiza usuarios del proveedor también
		    	  List<Users> userList = userService.getByAddressNumber(s.getAddresNumber());
		    	  if(userList != null && !userList.isEmpty()) {
		    		  for(Users uSupplier : userList) {
	    				  uSupplier.setEnabled(true);
	    				  userService.update(uSupplier, null, null);
		    		  }
		    	  }
	              
	              dataAuditService.saveDataAudit("SupplierUpdateApproval",s.getAddresNumber(), currentDate, "",
	    		  "MDW", "Supplier Updated Successful - Ticket: " + s.getTicketId(), "updatePostAddressBook", 
	    		  "",null, null, "", 
	    		  null, AppConstants.STATUS_ACCEPT, AppConstants.SUPPLIER_MODULE);  
	    		       	
	    		  String credentials = "Usuario: " + s.getAddresNumber() + "<br />&nbsp; url: " + AppConstants.EMAIL_PORTAL_LINK ;
	    		  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	    		  emailAsyncSup.setProperties(AppConstants.EMAIL_INVOICE_SUBJECT, stringUtils.prepareEmailContent(AppConstants.EMAIL_MASS_SUPPLIER_CHANGE_NOTIFICATION + credentials), s.getEmailSupplier());
	    		  emailAsyncSup.setMailSender(mailSenderObj);
	    		  Thread emailThreadSup = new Thread(emailAsyncSup);
	    		  emailThreadSup.start();
	            		  
	          }
	              
				  
	          }
	          return mapOK("Success updatePostAddressBook",total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - updatePostAddressBook");
		}
	}
	
	public Map<String, Object> getSuppliers(int start, int limit ){
		try{
			log4j.info("*********** STEP 1: getSuppliers:start:" + start + ",limit:" + limit);
			List<SupplierDTO> supDtoList = supplierService.getList(start, limit);
			String supList = "";
			List<String> sList = new ArrayList<String>();
			if (!supDtoList.isEmpty()) {
				 
				for (SupplierDTO sdto : supDtoList) {
					if (sdto.getAddresNumber() != null && !"".equals(sdto.getAddresNumber())) {
						sList.add(sdto.getAddresNumber().trim());
					}
				}
				String idList = sList.toString();
				supList = idList.substring(1, idList.length() - 1).replace(", ", ",");
			}
			
		    return mapOK(supList,sList.size());
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getSuppliers");
		}
	}
	
	
	public Map<String, Object> setPoListHistory(List<PurchaseOrder> poList){
		try{
			log4j.info("*********** STEP 3: setPoListHistory:poList:" + poList);
		    int total=0;
			if(poList != null) {
			/*ObjectMapper mapper = new ObjectMapper();
			PurchaseOrder[] response = mapper.readValue(poList, PurchaseOrder[].class);
			List<PurchaseOrder> objList = Arrays.asList(response);*/
			List<PurchaseOrder> objList = poList;
			      if (!objList.isEmpty()) {
			    	  total = objList.size();
			    	  log4j.info("REGISTROS:" + objList.size());
						
						for(PurchaseOrder o : objList) {
								if(o.getPurchaseOrderDetail() != null && !o.getPurchaseOrderDetail().isEmpty()) {
									for(PurchaseOrderDetail d : o.getPurchaseOrderDetail()) {
		 								if((d.getAmuntReceived() < 0d|| d.getForeignAmount() < 0d) 
		 										&& AppConstants.JDE_RETENTION_CODE.equals(String.valueOf(d.getObjectAccount()).trim())) {
		 									
		 									d.setAmount(d.getAmount()*-1);
		 									d.setAmuntReceived(d.getAmuntReceived()*-1);
		 									d.setForeignAmount(d.getForeignAmount()*-1);
		 									d.setPending(d.getPending()*-1);
		 									d.setQuantity(d.getQuantity()*-1);
		 									d.setReceived(d.getReceived()*-1); 		 									
		 									d.setReceiptType(AppConstants.RECEIPT_CODE_RETENTION);
		 								}
									}
								}
								o.setPortalRecordDate(new Date());
								o.setOrderStauts(AppConstants.STATUS_OC_RECEIVED);
								Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
								if(s!=null) {
									o.setLongCompanyName(s.getRazonSocial());
								}
							}
							purchaseOrderService.saveMultiple(objList);
						//System.out.println("Procesado:" + response.length);
						
					}
			
			      //getPurchaseReceiptList(supList);
			}
			return mapOK("Respuesta OK setPoListHistory",total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			
			return mapError(e.getMessage() + " - setPoListHistory");
		}
	}
	
	public Map<String, Object> getPostJournalEntries(int start, int limit){
		try{
			log4j.info("*********** STEP 1: getPostJournalEntries:start:" + start + ",limit:" + limit);
			List<FiscalDocuments> fiscalDocList = fiscalDocumentService.getPendingReplicationInvoice("PENDING","FACTURA",false,start, limit);
			String supList = "";
			List<String> sList = new ArrayList<String>();
			List<BatchJournalDTO> batchJournalList = new ArrayList<BatchJournalDTO>();
			ObjectMapper jsonMapper = new ObjectMapper();
			String jsonInString = null;
			
			if (!fiscalDocList.isEmpty()) {
				 
				for (FiscalDocuments fisDoc : fiscalDocList) {
					if (fisDoc.getUuidFactura() != null && !"".equals(fisDoc.getUuidFactura())) {
						UserDocument userDocument = documentsService.getDocumentByUuid(fisDoc.getUuidFactura());
						if(userDocument != null) {
							
						InvoiceDTO inv = documentsService.getInvoiceXml(userDocument.getContent());
						Supplier s = supplierService.searchByAddressNumber(userDocument.getAddressBook());
							        		
						
		        		@SuppressWarnings("unused")
		        		BatchJournalDTO batchJournalDTO = createNewBatchJournal(fisDoc,inv,0,s,AppConstants.NN_MODULE_BATCHJOURNAL);
		        		
		        		batchJournalList.add(batchJournalDTO);
						
		        		
		        		
					}

					}
				}

				
			}
			//jsonMapper = new ObjectMapper();
			//jsonInString = jsonMapper.writeValueAsString(batchJournalList);
			
			Gson gson = new Gson();  
			jsonInString = gson.toJson(batchJournalList); 
			//System.out.println(jsonInString);
		    return mapOK(jsonInString,batchJournalList.size());
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getSuppliers");
		}
	}
	
	public Map<String, Object> updateJournalEntries(List<BatchJournalDTO> batchJournalDTO){
		try{
			int total =0;
			log4j.info("*********** STEP 2: updateJournalEntries:batchJournalList:" + batchJournalDTO);
			 String resp = "";
			 List<BatchJournalDTO> objList = null;
			if(batchJournalDTO != null ) {
				
				 /* ObjectMapper mapper = new ObjectMapper();
				  BatchJournalDTO[] response = (BatchJournalDTO[])mapper.readValue(batchJournalDTO, BatchJournalDTO[].class);
				  objList = new ArrayList<BatchJournalDTO>();
				  objList = Arrays.asList(response);*/
				  
				  objList = new ArrayList<BatchJournalDTO>();
				  objList = batchJournalDTO;
				  
				  for (int j = 0; j < objList.size(); j++) {
					  log4j.info("Guardado:" + objList.get(j).getJournalEntries().get(0).getVNEDBT());
							
		            }
				
			}
			return mapOK("Respuesta OK updateJournalEntries",objList.size());
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			
			return mapError(e.getMessage() + " - setPoListHistory");
		}
	}
	
	
	public Map<String, Object> getPostVoucher(int start, int limit){
		try{
			log4j.info("*********** STEP 1: getPostVoucher:start:" + start + ",limit:" + limit);
			//List<FiscalDocuments> fiscalDocList = fiscalDocumentService.getPendingReplicationInvoice("PENDING","FACTURA",true,start, limit);
			List<FiscalDocuments> fiscalDocList = fiscalDocumentService.getPendingReplicationInvoice("PENDING","FACTURA",true,start, limit);
			String supList = "";
			List<String> sList = new ArrayList<String>();
			List<VoucherHeaderDTO> voucherList = new ArrayList<VoucherHeaderDTO>();
			ObjectMapper jsonMapper = new ObjectMapper();
			String jsonInString = null;
			
			if (!fiscalDocList.isEmpty()) {
				 
				for (FiscalDocuments fisDoc : fiscalDocList) {
					if (fisDoc.getUuidFactura() != null && !"".equals(fisDoc.getUuidFactura())) {
						UserDocument userDocument = documentsService.getInvoiceDocumentByUuid(fisDoc.getUuidFactura());
						if(userDocument != null) {
							
						InvoiceDTO inv = documentsService.getInvoiceXml(userDocument.getContent());
						PurchaseOrder po = purchaseOrderService.searchbyOrderAndAddressBookAndCompany(fisDoc.getOrderNumber(), userDocument.getAddressBook(), userDocument.getDocumentType(), fisDoc.getOrderCompany());
						Supplier s = supplierService.searchByAddressNumber(userDocument.getAddressBook());
							        		
		        		//List<Receipt> receiptArray = purchaseOrderService.getOrderReceipts(fisDoc.getOrderNumber(), userDocument.getAddressBook(), userDocument.getDocumentType(), po.getOrderCompany());
		        		
		        		List<Receipt> receiptArray = purchaseOrderService.getReceiptsByUUID(fisDoc.getUuidFactura());
		      
		        		List<PurchaseOrderDetail> receiptArrayasd = null;
		        		@SuppressWarnings("unused")
						VoucherHeaderDTO voucher = createNewVoucherFotCreditNote(po,inv,0,s,receiptArrayasd,AppConstants.NN_MODULE_VOUCHER,fisDoc);
		        		
		        		voucherList.add(voucher);
						
		        		
					}

					}
				}

				
			}
		//	jsonMapper = new ObjectMapper();
			//jsonInString = jsonMapper.writeValueAsString(voucherList);
			
			Gson gson = new Gson();  
			jsonInString = gson.toJson(voucherList); 
		    return mapOK(jsonInString,voucherList.size());
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getSuppliers");
		}
	}
	
	
	public Map<String, Object> updatePostVoucher(List<VoucherHeaderDTO> batchJournalList){
		try{
	//		batchJournaList = "[{\"voucherDetailDTO\":[{\"szaap\":1868694.0,\"szlnty\":\"N \",\"szavch\":null,\"szedty\":null,\"szedoc\":80000254,\"szedst\":null,\"szeder\":null,\"szedsq\":0,\"szsfxo\":\"000\",\"szlnid\":1000.0,\"szan8\":525469,\"szedln\":1.0,\"szeddl\":0,\"szdoco\":35113,\"szkcoo\":\"00002\",\"szuopn\":100.0,\"szvinv\":\"\",\"szuprc\":1.7304E8,\"szekco\":\"00002\",\"szdcto\":\"OP\",\"szlitm\":null,\"szedct\":\"OP\",\"szag\":1868694.0,\"szmcu\":\"      MERIDA\",\"szaid\":\"00679926\",\"szaexp\":0.0,\"urab\":0,\"szacr\":0.0,\"szuom\":\"EA\",\"szcrr\":0.0,\"szcrcd\":\"MXP\",\"szfap\":0.0,\"szddj\":122079,\"szfrrc\":0.0,\"torg\":null,\"user\":null,\"pid\":null,\"jobn\":null,\"upmj\":null,\"tday\":0,\"crrm\":\"D\"}],\"voucherSummaryDTO\":{\"swuopn\":0.0,\"swaap\":1868694.0,\"swdcto\":\"OP\",\"swcrcd\":null,\"swfrrc\":0.0,\"swacr\":0.0,\"swag\":1868694.0,\"swdoco\":35113,\"swan8\":525469,\"swcrr\":0.0,\"swfap\":0.0,\"swkcoo\":\"00002\",\"swsfxo\":\"000\",\"swekco\":\"00002\",\"swedoc\":80000254,\"swvinv\":\"FM2265\",\"swkco\":\"00002\",\"crrm\":\"D\"},\"torg\":null,\"syedoc\":80000254,\"user\":null,\"sydcto\":\"OP\",\"sykcoo\":\"00002\",\"sysfxo\":\"000\",\"syedsq\":0,\"syshan\":700033,\"syekco\":\"00002\",\"pid\":null,\"syeder\":null,\"syedty\":null,\"syedst\":null,\"syedct\":\"PV\",\"syan8\":525469,\"symcu\":\"      MERIDA\",\"sydoco\":35113,\"sycrcd\":\"MXP\",\"jobn\":null,\"upmj\":null,\"syedln\":0.0,\"syvinv\":\"FM2265\",\"sydgj\":null,\"tday\":0,\"syvr01\":\"021D279F-75C0-45E4-AC96-9\",\"syurrf\":\"D0A6E2EE417\",\"sydivj\":\"122002\",\"crrm\":\"D\",\"sycrr\":0.0,\"syddu\":122079,\"syddj\":122079},{\"voucherDetailDTO\":[{\"szaap\":960000.0,\"szlnty\":\"N \",\"szavch\":null,\"szedty\":null,\"szedoc\":80000255,\"szedst\":null,\"szeder\":null,\"szedsq\":0,\"szsfxo\":\"000\",\"szlnid\":1000.0,\"szan8\":540962,\"szedln\":1.0,\"szeddl\":0,\"szdoco\":35109,\"szkcoo\":\"00002\",\"szuopn\":100.0,\"szvinv\":\"\",\"szuprc\":9.6E7,\"szekco\":\"00002\",\"szdcto\":\"OP\",\"szlitm\":null,\"szedct\":\"OP\",\"szag\":960000.0,\"szmcu\":\"      MERIDA\",\"szaid\":\"00679925\",\"szaexp\":0.0,\"urab\":0,\"szacr\":0.0,\"szuom\":\"EA\",\"szcrr\":0.0,\"szcrcd\":\"MXP\",\"szfap\":0.0,\"szddj\":122079,\"szfrrc\":0.0,\"torg\":null,\"user\":null,\"pid\":null,\"jobn\":null,\"upmj\":null,\"tday\":0,\"crrm\":\"D\"}],\"voucherSummaryDTO\":{\"swuopn\":0.0,\"swaap\":960000.0,\"swdcto\":\"OP\",\"swcrcd\":null,\"swfrrc\":0.0,\"swacr\":0.0,\"swag\":960000.0,\"swdoco\":35109,\"swan8\":540962,\"swcrr\":0.0,\"swfap\":0.0,\"swkcoo\":\"00002\",\"swsfxo\":\"000\",\"swekco\":\"00002\",\"swedoc\":80000255,\"swvinv\":\"FAC339\",\"swkco\":\"00002\",\"crrm\":\"D\"},\"torg\":null,\"syedoc\":80000255,\"user\":null,\"sydcto\":\"OP\",\"sykcoo\":\"00002\",\"sysfxo\":\"000\",\"syedsq\":0,\"syshan\":700033,\"syekco\":\"00002\",\"pid\":null,\"syeder\":null,\"syedty\":null,\"syedst\":null,\"syedct\":\"PV\",\"syan8\":540962,\"symcu\":\"      MERIDA\",\"sydoco\":35109,\"sycrcd\":\"MXP\",\"jobn\":null,\"upmj\":null,\"syedln\":0.0,\"syvinv\":\"FAC339\",\"sydgj\":null,\"tday\":0,\"syvr01\":\"21bd04b6-9154-4a91-b3e4-c\",\"syurrf\":\"88eff70dc15\",\"sydivj\":\"122007\",\"crrm\":\"D\",\"sycrr\":0.0,\"syddu\":122079,\"syddj\":122079}]";
			log4j.info("*********** STEP 2: updatePostVoucher:batchJournaList:" + batchJournalList);
			 String resp = "";
			 List<VoucherHeaderDTO> objList = null;
			if(batchJournalList != null) {
				/*JSONArray jsonArray = new JSONArray();
				jsonArray = new JSONArray(batchJournalList); 
				
				  ObjectMapper mapper = new ObjectMapper();
				  VoucherHeaderDTO[] response = (VoucherHeaderDTO[])mapper.readValue(batchJournalList, VoucherHeaderDTO[].class);
				  objList = new ArrayList<VoucherHeaderDTO>();
				  objList = Arrays.asList(response);*/
				
				 objList = new ArrayList<VoucherHeaderDTO>();
				  objList = batchJournalList;
		            
		            for (int j = 0; j < objList.size(); j++) {
		            	// System.out.println("Guardado:" + objList.get(j).getSYEDOC());
							//List<FiscalDocuments> fisList = fiscalDocumentService.getFiscalDocuments(String.valueOf(objList.get(j).getSYAN8()), "PENDIENTE", objList.get(j).getSYVR01(), "FACTURA",0 , 10);
							List<FiscalDocuments> fisList = fiscalDocumentService.getReplicationInvoiceForUpdate(String.valueOf(objList.get(j).getSYAN8()), 
									AppConstants.STATUS_PENDING_REPLICATION, objList.get(j).getSYDOCO(),
									AppConstants.INVOICE_FIELD_UDC,AppConstants.STATUS_ACCEPT,/*objList.get(j).getSYVINV(),*/ 0,10);
							if(fisList!= null && fisList.size() > 0) {
								
								FiscalDocuments fis = fisList.get(0);
								
								//fis.setStatus("APROBADO");
								fis.setReplicationDate(new Date());
								fis.setReplicationStatus(AppConstants.STATUS_SUCCESS_REPLICATION);
								fis.setReplicationMessage("");
								
								//fiscalDocumentService.saveDocument(fis);
								
								fiscalDocumentService.updateDocument(fis);								
								log4j.info("REGISTRO ACTUALIZADO => Folio:" + objList.get(j).getSYVR01() + " - AddressNumber:" + objList.get(j).getSYAN8());
								
								//JSAAVEDRA: Actualiza los documentos como Pendientes para replicarlos al middleware.
								List<UserDocument> documents = documentsDao.searchCriteriaByUuidOnly(fis.getUuidFactura());
								if(documents != null && !documents.isEmpty()) {
									for(UserDocument doc : documents) {
										doc.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
										this.documentsService.update(doc, null, null);
									}
								}
							}
		            }
			
			}
			return mapOK("Respuesta OK updateJournalEntries", objList.size());
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			
			return mapError(e.getMessage() + " - setPoListHistory");
		}
	}
	
	public Map<String,Object> mapOK(String list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String,Object> mapOK(String list, String total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
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
	
	  public VoucherHeaderDTO createNewVoucherFotCreditNote(PurchaseOrder o, InvoiceDTO inv, int nextNbr, Supplier s, List<PurchaseOrderDetail > purchaseDetail, String nextNumberType , FiscalDocuments fisDoc){		
			VoucherHeaderDTO recHdr = new VoucherHeaderDTO();
			NextNumber nn = null;
			int julianEPD = 0;
			
			if(nextNbr == 0) {
				nn = nextNumberService.getNextNumber(nextNumberType);
				nextNbr = nn.getNexInt();
				int nextNbrUpd = nextNbr + 1;
				nn.setNexInt(nextNbrUpd);
				nextNumberService.updateNextNumber(nn);	
			}
			
			if(AppConstants.NC_TC.equals(inv.getTipoComprobante())) {
				inv.setSubTotal(inv.getSubTotal() * -1);
			}
			
			if(o.getEstimatedPaymentDate() != null) {
				julianEPD =Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(o.getEstimatedPaymentDate()));
			}
			
			recHdr.setSYDCTV("P4");
			recHdr.setSYEDOC(nextNbr);
			recHdr.setSYEDBT(nextNbr+"");
			recHdr.setSYEKCO(o.getOrderCompany());
			recHdr.setSYEDCT("PD");
			recHdr.setSYEDLN(0);
			recHdr.setSYDOCO(o.getOrderNumber());
			recHdr.setSYDCTO(o.getOrderType());
			recHdr.setSYKCOO(o.getOrderCompany());
			recHdr.setSYSFXO(o.getOrderSuffix());
			recHdr.setSYAN8(Integer.valueOf(o.getAddressNumber()));
			recHdr.setSYSHAN(Integer.valueOf(o.getShipTo()));
			recHdr.setSYMCU(o.getBusinessUnit());
			recHdr.setCRRM(o.getCurrencyMode());
			recHdr.setSYCRCD(o.getCurrecyCode());
			recHdr.setSYCRR(o.getExchangeRate());		
			recHdr.setSYVR01(inv.getUuid().substring(0, 25));
			recHdr.setSYURRF(inv.getUuid().substring(inv.getUuid().length() - 11));
			recHdr.setSYDDU(julianEPD);
			recHdr.setSYDDJ(julianEPD);
			
			
			
			String folio = "";
			log4j.info("****** Folio:" + fisDoc.getFolio());
			if(fisDoc.getFolio() != null && !"null".equals(fisDoc.getFolio()) && !"NULL".equals(fisDoc.getFolio()) ) {
				folio = fisDoc.getFolio();
			}
			
			String serie = "";
			log4j.info("****** Serie:" + fisDoc.getSerie());
			if(fisDoc.getSerie() != null && !"null".equals(fisDoc.getSerie()) && !"NULL".equals(fisDoc.getSerie()) ) {
				serie = fisDoc.getSerie().replaceAll("[^a-zA-Z0-9 \\-]", "-").concat("-");
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
			
			recHdr.setSYVINV(vinv);
			
			String divj = inv.getFechaTimbrado();
			divj = divj.replace("T", " ");
	        String resultDate = null;
			 SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        Date date = null;
		        try {
		            date = fmt.parse(divj);
		            resultDate = JdeJavaJulianDateTools.Methods.getJulianDate(date);
		        } catch (Exception e) {
		        	log4j.error("Exception" , e);
		            e.printStackTrace();
		        }
		        
			recHdr.setSYDIVJ(resultDate);
			recHdr.setSYEDTY("1");
			recHdr.setSYEDSQ(0);
			recHdr.setSYEDSP("N");
			recHdr.setSYEDST("810");
			recHdr.setSYEDER("R");
			recHdr.setSYTORG("SUPWEB");
			recHdr.setSYUSER("SUPWEB");
			recHdr.setSYPID("JDECLASS");
			recHdr.setSYJOBN("BATCH001");
			 String resultDateNow = JdeJavaJulianDateTools.Methods.getJulianDate(new Date());
			recHdr.setSYDGJ(resultDateNow);
			recHdr.setSYUPMJ(resultDateNow);
			recHdr.setSYRMK("PORTAL CRYOINFRA");

			List<VoucherDetailDTO> dtlList = new ArrayList<VoucherDetailDTO>();
			int lineNumber = 1;
			double qtyOpen = 0;
			double totalAmount = 0;
			double totalForeignAmount = 0;
			double openAmount = 0;
			
			for(PurchaseOrderDetail d : purchaseDetail) {	
				if(d.getAmuntReceived() != 0) {
					
					VoucherDetailDTO dto = new VoucherDetailDTO();
					dto.setSZEDOC(nextNbr);
					dto.setSZEDBT(nextNbr+"");
					dto.setSZEDCT(o.getOrderType());
					dto.setSZEKCO(o.getOrderCompany());
					dto.setSZEDLN(lineNumber*1000);
					dto.setSZLNTY(d.getLineType());
					
					dto.setSZDOCO(o.getOrderNumber());
					dto.setSZDCTO(o.getOrderType());
					dto.setSZKCOO(o.getCompanyKey());
					dto.setSZSFXO(o.getOrderSuffix());
					//duda
					dto.setSZCRR(d.getExchangeRate()==0?o.getExchangeRate():d.getExchangeRate());
					
					dto.setSZFRRC(0);
					dto.setSZACR(d.getForeignAmount());
					dto.setSZFAP(d.getForeignAmount());
					dto.setSZPST("OP".equals(o.getOrderType()) ? "R" : "H");
					dto.setSZDCTV("PD");
					
					dto.setSZLNID(d.getLineNumber() * 1000);
					dto.setSZLITM(null);
					dto.setSZAN8(Integer.valueOf(o.getAddressNumber()));
					dto.setSZMCU(o.getBusinessUnit());
					
					dto.setSZVINV("");
					dto.setSZAID(d.getAccountId());
					dto.setCRRM(o.getCurrencyMode());
					dto.setSZCRCD(o.getCurrecyCode());
									
					dto.setSZUOM(d.getUom());
					dto.setSZUPRC(d.getUnitCost() * 10000);
					dto.setSZAEXP(0);
					dto.setURAB((int)d.getLineNumber());				
					dto.setSZUOPN(d.getQuantity()*100);
					
					qtyOpen = qtyOpen + dto.getSZUOPN();
					dto.setSZVR01(inv.getUuid().substring(0, 25));
					dto.setSZURRF(inv.getUuid().substring(inv.getUuid().length() - 11));
					
					if(!AppConstants.DEFAULT_CURRENCY_JDE.equals(o.getCurrecyCode())) {
						BigDecimal bd = new BigDecimal(d.getForeignAmount()*100).setScale(2, RoundingMode.HALF_EVEN);
						double foreignAmountReceived = bd.doubleValue();
						
						dto.setSZACR(foreignAmountReceived);
						dto.setSZFAP(foreignAmountReceived);
						totalForeignAmount = totalForeignAmount + foreignAmountReceived;
						
						if(d.getExchangeRate() == 0) {
							dto.setSZCRR(o.getExchangeRate());
						}else {
							dto.setSZCRR(d.getExchangeRate());
						}
					}
									
					BigDecimal bd = new BigDecimal(d.getAmuntReceived()*100).setScale(2, RoundingMode.HALF_EVEN);
					double detailAmount = bd.doubleValue();
					//float detailAmount = Math.round(d.getAmountReceived()*100); //Fix para cantidades grandes
					dto.setSZAG(detailAmount);
					dto.setSZAAP(detailAmount);
					totalAmount = totalAmount + detailAmount;
					
					openAmount = openAmount + dto.getSZAAP();
					dtlList.add(dto);
					lineNumber = lineNumber + 1;
					dto.setSZDDJ(julianEPD);
					
					dto.setSZEDTY("2");
					dto.setSZEDSQ(0);
					dto.setSZEDCT("PD");
					dto.setSZEDSP("N");
					dto.setSZEDST("810");
					dto.setSZEDER("R");
					dto.setSZEDDL(0);
					dto.setSZAVCH("N");
					dto.setSZTORG("SUPWEB");
					dto.setSZUSER("SUPWEB");
					dto.setSZPID("JDECLASS");
					dto.setSZJOBN("BATCH001");
					dto.setSZUPMJ(JdeJavaJulianDateTools.Methods.getJulianDate(new Date()));
					dto.setSZRMK("PORTAL CRYOINFRA");
					
				}
			}

			log4j.info("Total: " + totalAmount);
			recHdr.setVoucherDetailDTO(dtlList);
			VoucherSummaryDTO vs = new VoucherSummaryDTO();
			vs.setSWEKCO(o.getOrderCompany());
			vs.setSWEDOC(nextNbr);
			vs.setSWKCO(o.getOrderCompany());
			vs.setSWKCOO(o.getOrderCompany());
			vs.setSWDOCO(o.getOrderNumber());
			vs.setSWDCTO(o.getOrderType());
			vs.setSWSFXO(o.getOrderSuffix());
			vs.setSWAN8(Integer.valueOf(o.getAddressNumber()));
			vs.setSWVINV(vinv);
			vs.setSWUOPN(0);
			vs.setSWAG(totalAmount);
			vs.setSWAAP(totalAmount);
			vs.setCRRM(o.getCurrencyMode());
			vs.setSWEDCT("PD");
			vs.setSWDCTV("P4");
			if(!AppConstants.DEFAULT_CURRENCY_JDE.equals(o.getCurrecyCode())) {
				vs.setSWACR(totalForeignAmount);
				vs.setSWFAP(totalForeignAmount);
			}
			
			vs.setSWEDTY("2");
			vs.setSWEDST("810");
			vs.setSWEDER("R");
			vs.setSWEDDL("0");
			vs.setSWFEA("0");
			vs.setSWTORG("SUPWEB");
			vs.setSWUSER("SUPWEB");
			vs.setSWPID("JDECLASS");
			vs.setSWJOBN("BATCH001");
			vs.setSWUPMJ(JdeJavaJulianDateTools.Methods.getJulianDate(new Date()));
			
			recHdr.setVoucherSummaryDTO(vs);

			/*String resp = jDERestService.sendVoucher(recHdr);
			return resp;*/
			return recHdr;
		}
	  
	  public BatchJournalDTO createNewBatchJournal(FiscalDocuments o, InvoiceDTO inv, int nextNbr, Supplier s, String nextNumberType){
		    BatchJournalDTO batchJournalDTO = new BatchJournalDTO();		
			NextNumber nn = null;
			int julianDateInvoiceUpload = 0;
			int julianDateInvoice = 0;
			int julianDatePayment = 0;
			int julianDateToday = 0;
			int julianDateTime = 0;
			double lineNumber = 1D;
			String supplierName = "";
			String advancePaymentMessage = "";
			String invoiceNumber = "";

			if(nextNbr == 0) {
				nn = nextNumberService.getNextNumber(nextNumberType);
				nextNbr = nn.getNexInt();
				int nextNbrUpd = nextNbr + 1;
				nn.setNexInt(nextNbrUpd);
				nextNumberService.updateNextNumber(nn);	
			}

			if(AppConstants.NC_TC.equals(inv.getTipoComprobante())) {
				inv.setSubTotal(inv.getSubTotal() * -1);
			}
			
			if(s.getName().length() > 30) {
				supplierName = s.getName().substring(0, 30);
			} else {
				supplierName = s.getName();
			}
			
			if(inv.getFechaTimbrado() != null) {
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String divj = inv.getFechaTimbrado();		
			    Date date = null;
			    
		        try {
		        	divj = divj.replace("T", " ");
		            date = fmt.parse(divj);
		            julianDateInvoice = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(date));
		        } catch (Exception e) {
		        	log4j.error("Exception" , e);
		            e.printStackTrace();
		        }
			}

			if(o.getEstimatedPaymentDate() != null) {
		        try {
		            julianDatePayment = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(o.getEstimatedPaymentDate()));
		        } catch (Exception e) {
		        	log4j.error("Exception" , e);
		            e.printStackTrace();
		        }
			}
			
			if(o.getInvoiceUploadDate() != null) {
				try {
					julianDateInvoiceUpload = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(o.getInvoiceUploadDate()));
				} catch (Exception e) {
					log4j.error("Exception" , e);
					e.printStackTrace();
				}
			}
			
			julianDateToday = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(new Date())).intValue();
			julianDateTime = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJDETimeStamp()).intValue();
			
			String folio = "";
			log4j.info("****** Folio:" + inv.getFolio());
			if(inv.getFolio() != null && !"null".equals(inv.getFolio()) && !"NULL".equals(inv.getFolio()) ) {
				folio = inv.getFolio();
			}
			
			String serie = "";
			log4j.info("****** Serie:" + inv.getSerie());
			if(inv.getSerie() != null && !"null".equals(inv.getSerie()) && !"NULL".equals(inv.getSerie()) ) {
				serie = inv.getSerie().replaceAll("[^a-zA-Z0-9 \\-]", "-").concat("-");
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
			invoiceNumber = vinv;
			
			if(o.getAdvancePayment() > 0D) {
				advancePaymentMessage = "Anticipo: ".concat(String.format("%,.2f", o.getAdvancePayment())); 			
			}
			
			String accountingAcc = o.getAccountingAccount().split(".")[0];
			
			//Encabezado Batch Voucher Transactions (F0411Z1)
		    List<BatchVoucherTransactionsDTO> batchTransDTOList = new ArrayList<BatchVoucherTransactionsDTO>();	    	    
			BatchVoucherTransactionsDTO batchTransHdr = new BatchVoucherTransactionsDTO();
			batchTransHdr.setVLEDUS(AppConstants.USER_SMARTECH);
			batchTransHdr.setVLEDTN("1");
			batchTransHdr.setVLEDLN(lineNumber*1000D);
			batchTransHdr.setVLEDER("B");
			batchTransHdr.setVLEDSP("0");
			batchTransHdr.setVLEDTC("A");
			batchTransHdr.setVLEDTR("V");
			batchTransHdr.setVLEDBT(String.valueOf(nextNbr));
			batchTransHdr.setVLEDDH("1");
			batchTransHdr.setVLKCO(o.getCompanyFD());
			batchTransHdr.setVLSFXE("0");
			batchTransHdr.setVLAN8(Integer.valueOf(o.getAddressNumber()).intValue());		
			batchTransHdr.setVLPYE(Integer.valueOf(o.getAddressNumber()).intValue());		
			batchTransHdr.setVLDDJ(julianDatePayment);
			batchTransHdr.setVLDDNJ(julianDatePayment);
			batchTransHdr.setVLDIVJ(julianDateInvoice);
			batchTransHdr.setVLDSVJ(julianDateToday);
			batchTransHdr.setVLDGJ(julianDateInvoiceUpload);
			batchTransHdr.setVLCTRY(20);
			batchTransHdr.setVLCO(o.getCompanyFD());		
			batchTransHdr.setVLICUT("V");
			batchTransHdr.setVLBALJ("Y");
			batchTransHdr.setVLPST("H");
			batchTransHdr.setVLAG(inv.getTotal()*100D);
			batchTransHdr.setVLAAP(inv.getTotal()*100D);
			batchTransHdr.setVLATXA(inv.getSubTotal()*100D);
			batchTransHdr.setVLSTAM(o.getImpuestos()*100D);
			batchTransHdr.setVLTXA1(o.getTaxCode());//TAX CODE
			batchTransHdr.setVLEXR1("V");
			batchTransHdr.setVLCRRM(o.getCurrencyMode());
			batchTransHdr.setVLCRCD(o.getCurrencyCode());
			batchTransHdr.setVLGLC(o.getGlOffset());
			batchTransHdr.setVLGLBA(o.getAccountingAccount());//8 CHAR
			batchTransHdr.setVLAM("2");//1-Short ID, 2-Long ID
			batchTransHdr.setVLMCU(o.getCentroCostos());
			batchTransHdr.setVLPTC(o.getPaymentTerms());
			batchTransHdr.setVLVINV(invoiceNumber);		
			batchTransHdr.setVLVR01(inv.getUuid().substring(0, 25));
			batchTransHdr.setVLURRF(inv.getUuid().substring(inv.getUuid().length() - 11));
			batchTransHdr.setVLPYIN("T");
			batchTransHdr.setVLTORG(AppConstants.USER_SMARTECH);
			batchTransHdr.setVLUSER(AppConstants.USER_SMARTECH);
			batchTransHdr.setVLPID(AppConstants.PROGRAM_ID_ZP0411Z1);
			batchTransHdr.setVLUPMJ(julianDateToday);
			batchTransHdr.setVLUPMT(julianDateTime);
			batchTransHdr.setVLJOBN(AppConstants.WORK_STN_ID_COBOWB04);
			//batchTransHdr.setVLRMK(advancePaymentMessage);		
			batchTransHdr.setVLRMK("Portal CryoInfra");
			if(!"MXP".equals(o.getCurrencyCode())) {
				batchTransHdr.setVLAG(0D);
				batchTransHdr.setVLAAP(0D);
				batchTransHdr.setVLATXA(0D);
				batchTransHdr.setVLSTAM(0D);					
				batchTransHdr.setVLCRR(inv.getTipoCambio());
				batchTransHdr.setVLACR(inv.getTotal()*100D);
				batchTransHdr.setVLFAP(inv.getTotal()*100D);
				batchTransHdr.setVLCTXA(inv.getSubTotal()*100D);
				batchTransHdr.setVLCTAM(o.getImpuestos()*100D);					
			}		
			batchTransDTOList.add(batchTransHdr);

			//Encabezado Batch Journal Entry (F0911Z1)
			List<BatchJournalEntryDTO> batchJournalDTOList = new ArrayList<BatchJournalEntryDTO>();
			BatchJournalEntryDTO batchJournalHdr = new BatchJournalEntryDTO();		
			batchJournalHdr.setVNEDUS(AppConstants.USER_SMARTECH);
			batchJournalHdr.setVNEDTN("1");
			batchJournalHdr.setVNEDLN(lineNumber*1000D);
			batchJournalHdr.setVNEDER("B");
			batchJournalHdr.setVNEDSP("0");
			batchJournalHdr.setVNEDTC("A");
			batchJournalHdr.setVNEDTR("V");
			batchJournalHdr.setVNEDBT(String.valueOf(nextNbr));
			batchJournalHdr.setVNDGJ(julianDateInvoiceUpload);
			batchJournalHdr.setVNICUT("V");
			batchJournalHdr.setVNCO(o.getCompanyFD());
			batchJournalHdr.setVNANI(o.getAccountNumber());
			batchJournalHdr.setVNAM("2");
			batchJournalHdr.setVNAID("");
			batchJournalHdr.setVNMCU(o.getCentroCostos());
			batchJournalHdr.setVNLT("AA");
			batchJournalHdr.setVNCTRY(20);
			batchJournalHdr.setVNCRCD(o.getCurrencyCode());
			batchJournalHdr.setVNAA(inv.getSubTotal()*100D);
			batchJournalHdr.setVNEXA(supplierName);
			batchJournalHdr.setVNEXR(AppConstants.EXPLANATION_REMARK_FREIGHT);
			batchJournalHdr.setVNAN8(Integer.valueOf(s.getAddresNumber()));
			batchJournalHdr.setVNVINV(invoiceNumber);
			batchJournalHdr.setVNIVD(julianDateInvoice);
			batchJournalHdr.setVNDSVJ(julianDateToday);
			batchJournalHdr.setVNUSER(AppConstants.USER_SMARTECH);
			batchJournalHdr.setVNPID(AppConstants.PROGRAM_ID_ZP0411Z1);
			batchJournalHdr.setVNJOBN(AppConstants.WORK_STN_ID_COBOWB04);
			batchJournalHdr.setVNUPMJ(julianDateToday);
			batchJournalHdr.setVNUPMT(julianDateTime);
			batchJournalHdr.setVNCRRM(o.getCurrencyMode());
			if(!"MXP".equals(o.getCurrencyCode())) {
				batchJournalHdr.setVNACR(inv.getSubTotal()*100D);
			}
			batchJournalDTOList.add(batchJournalHdr);
			
			//Líneas de Conceptos
			if(o.getConceptTotalAmount() > 0D) {
				String invoiceNumberDtl;
				for(FiscalDocumentsConcept concept : o.getConcepts()) {
					invoiceNumberDtl = "";
					lineNumber = lineNumber + 1D;
					
					julianDateInvoice = 0;
					if(concept.getInvoiceDate() != null && !concept.getInvoiceDate().isEmpty()) {
						SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String divj = concept.getInvoiceDate();		
					    Date date = null;
					    
				        try {
				        	divj = divj.replace("T", " ");
				            date = fmt.parse(divj);
				            julianDateInvoice = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(date));
				        } catch (Exception e) {
				        	log4j.error("Exception" , e);
				            e.printStackTrace();
				        }
					}
					folio = "";				
					if(concept.getFolio() != null && !"null".equals(concept.getFolio()) && !"NULL".equals(concept.getFolio()) ) {
						folio = concept.getFolio();
					}
					
					serie = "";
					if(concept.getSerie() != null && !"null".equals(concept.getSerie()) && !"NULL".equals(concept.getSerie()) ) {
						serie = concept.getSerie();
					}
					
					invoiceNumberDtl = serie.concat(folio);
					if("".equals(invoiceNumberDtl) && !"".equals(invoiceNumber)) {
						invoiceNumberDtl = invoiceNumber.concat("-").concat(String.valueOf(Double.valueOf(lineNumber).intValue()));
					}
					log4j.info("****** Concept InvoiceNumber:" + invoiceNumberDtl);
					
					//Líneas de Batch Voucher Transactions (F0411Z1)
					BatchVoucherTransactionsDTO batchTransDtl = (BatchVoucherTransactionsDTO) SerializationUtils.clone(batchTransHdr);				
					batchTransDtl.setVLEDLN(lineNumber*1000D);
					batchTransDtl.setVLAG(concept.getAmount()*100D);
					batchTransDtl.setVLAAP(concept.getAmount()*100D);
					batchTransDtl.setVLATXA(concept.getSubtotal()*100D);
					batchTransDtl.setVLSTAM(concept.getImpuestos()*100D);
					batchTransDtl.setVLTXA1(concept.getTaxCode());
					batchTransDtl.setVLCRRM(concept.getCurrencyMode());
					batchTransDtl.setVLCRCD(concept.getCurrencyCode());
					batchTransDtl.setVLGLC(concept.getGlOffset());
					batchTransDtl.setVLGLBA(concept.getAccountingAccount());				
					batchTransDtl.setVLVR01(concept.getUuid().substring(0, 25));
					batchTransDtl.setVLURRF(concept.getUuid().substring(concept.getUuid().length() - 11));
					batchTransDtl.setVLDIVJ(julianDateInvoice);
					batchTransDtl.setVLVINV(invoiceNumberDtl);
					if(!"MXP".equals(concept.getCurrencyCode())) {
						batchTransDtl.setVLAG(0D);
						batchTransDtl.setVLAAP(0D);
						batchTransDtl.setVLATXA(0D);
						batchTransDtl.setVLSTAM(0D);
						batchTransDtl.setVLCRR(concept.getTipoCambio());
						batchTransDtl.setVLACR(concept.getAmount()*100D);
						batchTransDtl.setVLFAP(concept.getAmount()*100D);
						batchTransDtl.setVLCTXA(concept.getSubtotal()*100D);
						batchTransDtl.setVLCTAM(concept.getImpuestos()*100D);
					}
					batchTransDTOList.add(batchTransDtl);
					
					//Líneas de Batch Journal Entry (F0911Z1)
					BatchJournalEntryDTO batchJournalDtl = (BatchJournalEntryDTO) SerializationUtils.clone(batchJournalHdr);
					batchJournalDtl.setVNEDLN(lineNumber*1000D);
					batchJournalDtl.setVNANI(concept.getConceptAccount());				
					batchJournalDtl.setVNAA(concept.getSubtotal()*100D);
					batchJournalDtl.setVNCRCD(concept.getCurrencyCode());
					batchJournalDtl.setVNCRRM(concept.getCurrencyMode());
					batchJournalDtl.setVNIVD(julianDateInvoice);
					batchJournalDtl.setVNVINV(invoiceNumberDtl);
					if(!"MXP".equals(concept.getCurrencyCode())) {
						batchJournalDtl.setVNACR(concept.getAmount()*100D);
					}				
					batchJournalDTOList.add(batchJournalDtl);
				}
			}
			
			batchJournalDTO.setVoucherEntries(batchTransDTOList);
			batchJournalDTO.setJournalEntries(batchJournalDTOList);		
			//String resp = jDERestService.sendJournalEntries(batchJournalDTO);
			return batchJournalDTO;
		}
	  
	  public Map<String, Object> getPostVoucherForeign(int start, int limit){
			try{
				log4j.info("*********** STEP 1: getPostVoucherForeign:" + start + ",limit:" + limit);
				//List<FiscalDocuments> fiscalDocList = fiscalDocumentService.getPendingReplicationInvoice("PENDING","FACTURA",true,start, limit);
				List<FiscalDocuments> fiscalDocList = fiscalDocumentService.getPendingReplicationInvoice("PENDING",AppConstants.STATUS_FACT_FOREIGN,true,start, limit);
				String supList = "";
				List<String> sList = new ArrayList<String>();
				List<VoucherHeaderDTO> voucherList = new ArrayList<VoucherHeaderDTO>();
				ObjectMapper jsonMapper = new ObjectMapper();
				String jsonInString = null;
				
				if (!fiscalDocList.isEmpty()) {
					 
					for (FiscalDocuments fisDoc : fiscalDocList) {
						if (fisDoc.getUuidFactura() != null && !"".equals(fisDoc.getUuidFactura())) {
							UserDocument userDocument = documentsService.getDocumentByUuid(fisDoc.getUuidFactura());
							if(userDocument != null) {
								
							PurchaseOrder po = purchaseOrderService.searchbyOrderAndAddressBookAndCompany(fisDoc.getOrderNumber(), userDocument.getAddressBook(), userDocument.getDocumentType(), fisDoc.getOrderCompany());
							Supplier s = supplierService.searchByAddressNumber(userDocument.getAddressBook());
								        		
			        		List<Receipt> receiptArray = purchaseOrderService.getOrderReceipts(fisDoc.getOrderNumber(), userDocument.getAddressBook(), userDocument.getDocumentType(), po.getOrderCompany());
			       
			        		ForeignInvoiceTable foreignInvoiceTable = purchaseOrderService.getForeignInvoiceFromUuid(fisDoc.getUuidFactura());
			        		
			        		ForeingInvoice foreingInvoice = new ForeingInvoice();
			        		
			        		foreingInvoice.setAddressNumber(po.getAddressNumber());
			        		foreingInvoice.setCountry(s.getCountry());
			        		foreingInvoice.setExpeditionDate(foreignInvoiceTable.getExpeditionDate());
			        		foreingInvoice.setFolio(fisDoc.getFolio());
			        		foreingInvoice.setSerie(fisDoc.getSerie());
			        		foreingInvoice.setForeignCurrency(foreignInvoiceTable.getForeignCurrency());
			        		foreingInvoice.setForeignDebit(foreignInvoiceTable.getForeignDebit());
			        		foreingInvoice.setForeignDescription(foreignInvoiceTable.getForeignDescription());
			        		foreingInvoice.setForeignNotes(foreignInvoiceTable.getForeignNotes());
			        		foreingInvoice.setForeignRetention(foreignInvoiceTable.getForeignRetention());
			        		foreingInvoice.setForeignSubtotal(foreignInvoiceTable.getForeignSubtotal());
			        		foreingInvoice.setForeignTaxes(foreignInvoiceTable.getForeignTaxes());
			        		foreingInvoice.setInvoiceNumber(foreignInvoiceTable.getInvoiceNumber());
			        		foreingInvoice.setOrderNumber(po.getOrderNumber());
			        		foreingInvoice.setOrderType(po.getOrderType());
			        		foreingInvoice.setReceptCompany(fisDoc.getOrderCompany());
			        		foreingInvoice.setTaxId(s.getTaxId());
			        		foreingInvoice.setUuid(foreignInvoiceTable.getUuid());
			        		foreingInvoice.setUsuarioImpuestos(foreignInvoiceTable.getUsuarioImpuestos());
			        		
	
			        		
			        		
							//VoucherHeaderDTO voucher = createNewVoucher(po,inv,0,s,receiptArray,AppConstants.NN_MODULE_VOUCHER);
			        		
			        		//resp = "DOC:" + eDIService.createNewForeignVoucher(po, inv, 0, s, requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
			        		@SuppressWarnings("unused")
			        		VoucherHeaderDTO voucher =  EDIService.createNewForeignVoucherMdw(po, foreingInvoice, 0, s, receiptArray, AppConstants.NN_MODULE_VOUCHER,null);
			        		
			        		voucherList.add(voucher);
							
			        		
						}

						}
					}

					
				}
			//	jsonMapper = new ObjectMapper();
				//jsonInString = jsonMapper.writeValueAsString(voucherList);
				
				Gson gson = new Gson();  
				jsonInString = gson.toJson(voucherList); 
				log4j.info(jsonInString);
			    return mapOK(jsonInString,voucherList.size());
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - getSuppliers");
			}
		}
	  
		public Map<String, Object> updatePostVoucherForeign(List<VoucherHeaderDTO> batchJournalList){
			try{
		//		batchJournaList = "[{\"voucherDetailDTO\":[{\"szaap\":1868694.0,\"szlnty\":\"N \",\"szavch\":null,\"szedty\":null,\"szedoc\":80000254,\"szedst\":null,\"szeder\":null,\"szedsq\":0,\"szsfxo\":\"000\",\"szlnid\":1000.0,\"szan8\":525469,\"szedln\":1.0,\"szeddl\":0,\"szdoco\":35113,\"szkcoo\":\"00002\",\"szuopn\":100.0,\"szvinv\":\"\",\"szuprc\":1.7304E8,\"szekco\":\"00002\",\"szdcto\":\"OP\",\"szlitm\":null,\"szedct\":\"OP\",\"szag\":1868694.0,\"szmcu\":\"      MERIDA\",\"szaid\":\"00679926\",\"szaexp\":0.0,\"urab\":0,\"szacr\":0.0,\"szuom\":\"EA\",\"szcrr\":0.0,\"szcrcd\":\"MXP\",\"szfap\":0.0,\"szddj\":122079,\"szfrrc\":0.0,\"torg\":null,\"user\":null,\"pid\":null,\"jobn\":null,\"upmj\":null,\"tday\":0,\"crrm\":\"D\"}],\"voucherSummaryDTO\":{\"swuopn\":0.0,\"swaap\":1868694.0,\"swdcto\":\"OP\",\"swcrcd\":null,\"swfrrc\":0.0,\"swacr\":0.0,\"swag\":1868694.0,\"swdoco\":35113,\"swan8\":525469,\"swcrr\":0.0,\"swfap\":0.0,\"swkcoo\":\"00002\",\"swsfxo\":\"000\",\"swekco\":\"00002\",\"swedoc\":80000254,\"swvinv\":\"FM2265\",\"swkco\":\"00002\",\"crrm\":\"D\"},\"torg\":null,\"syedoc\":80000254,\"user\":null,\"sydcto\":\"OP\",\"sykcoo\":\"00002\",\"sysfxo\":\"000\",\"syedsq\":0,\"syshan\":700033,\"syekco\":\"00002\",\"pid\":null,\"syeder\":null,\"syedty\":null,\"syedst\":null,\"syedct\":\"PV\",\"syan8\":525469,\"symcu\":\"      MERIDA\",\"sydoco\":35113,\"sycrcd\":\"MXP\",\"jobn\":null,\"upmj\":null,\"syedln\":0.0,\"syvinv\":\"FM2265\",\"sydgj\":null,\"tday\":0,\"syvr01\":\"021D279F-75C0-45E4-AC96-9\",\"syurrf\":\"D0A6E2EE417\",\"sydivj\":\"122002\",\"crrm\":\"D\",\"sycrr\":0.0,\"syddu\":122079,\"syddj\":122079},{\"voucherDetailDTO\":[{\"szaap\":960000.0,\"szlnty\":\"N \",\"szavch\":null,\"szedty\":null,\"szedoc\":80000255,\"szedst\":null,\"szeder\":null,\"szedsq\":0,\"szsfxo\":\"000\",\"szlnid\":1000.0,\"szan8\":540962,\"szedln\":1.0,\"szeddl\":0,\"szdoco\":35109,\"szkcoo\":\"00002\",\"szuopn\":100.0,\"szvinv\":\"\",\"szuprc\":9.6E7,\"szekco\":\"00002\",\"szdcto\":\"OP\",\"szlitm\":null,\"szedct\":\"OP\",\"szag\":960000.0,\"szmcu\":\"      MERIDA\",\"szaid\":\"00679925\",\"szaexp\":0.0,\"urab\":0,\"szacr\":0.0,\"szuom\":\"EA\",\"szcrr\":0.0,\"szcrcd\":\"MXP\",\"szfap\":0.0,\"szddj\":122079,\"szfrrc\":0.0,\"torg\":null,\"user\":null,\"pid\":null,\"jobn\":null,\"upmj\":null,\"tday\":0,\"crrm\":\"D\"}],\"voucherSummaryDTO\":{\"swuopn\":0.0,\"swaap\":960000.0,\"swdcto\":\"OP\",\"swcrcd\":null,\"swfrrc\":0.0,\"swacr\":0.0,\"swag\":960000.0,\"swdoco\":35109,\"swan8\":540962,\"swcrr\":0.0,\"swfap\":0.0,\"swkcoo\":\"00002\",\"swsfxo\":\"000\",\"swekco\":\"00002\",\"swedoc\":80000255,\"swvinv\":\"FAC339\",\"swkco\":\"00002\",\"crrm\":\"D\"},\"torg\":null,\"syedoc\":80000255,\"user\":null,\"sydcto\":\"OP\",\"sykcoo\":\"00002\",\"sysfxo\":\"000\",\"syedsq\":0,\"syshan\":700033,\"syekco\":\"00002\",\"pid\":null,\"syeder\":null,\"syedty\":null,\"syedst\":null,\"syedct\":\"PV\",\"syan8\":540962,\"symcu\":\"      MERIDA\",\"sydoco\":35109,\"sycrcd\":\"MXP\",\"jobn\":null,\"upmj\":null,\"syedln\":0.0,\"syvinv\":\"FAC339\",\"sydgj\":null,\"tday\":0,\"syvr01\":\"21bd04b6-9154-4a91-b3e4-c\",\"syurrf\":\"88eff70dc15\",\"sydivj\":\"122007\",\"crrm\":\"D\",\"sycrr\":0.0,\"syddu\":122079,\"syddj\":122079}]";
				log4j.info("*********** STEP 2: updatePostVoucherForeign:batchJournaList:" + batchJournalList);
				 String resp = "";
				 List<VoucherHeaderDTO> objList = null;
				if(batchJournalList != null ) {
					objList = new ArrayList<VoucherHeaderDTO>();
					objList = batchJournalList;
			            for (int j = 0; j < objList.size(); j++) {
			            	// System.out.println("Guardado:" + objList.get(j).getSYEDOC());
								//List<FiscalDocuments> fisList = fiscalDocumentService.getFiscalDocuments(String.valueOf(objList.get(j).getSYAN8()), "PENDIENTE", objList.get(j).getSYVR01(), "FACTURA",0 , 10);
								List<FiscalDocuments> fisList = fiscalDocumentService.getReplicationInvoiceForUpdate(String.valueOf(objList.get(j).getSYAN8()), 
										AppConstants.STATUS_PENDING_REPLICATION, objList.get(j).getSYDOCO(),
										AppConstants.STATUS_FACT_FOREIGN,AppConstants.STATUS_ACCEPT,/*objList.get(j).getSYVINV(),*/ 0,10);
								if(fisList!= null && fisList.size() > 0) {
									
									FiscalDocuments fis = fisList.get(0);
									
									//fis.setStatus("APROBADO");
									fis.setReplicationDate(new Date());
									fis.setReplicationStatus(AppConstants.STATUS_SUCCESS_REPLICATION);
									fis.setReplicationMessage("");
									
									//fiscalDocumentService.saveDocument(fis);
									
									fiscalDocumentService.updateDocument(fis);
									log4j.info("REGISTRO ACTUALIZADO => Folio:" + objList.get(j).getSYVR01() + " - AddressNumber:" + objList.get(j).getSYAN8());
									
									//JSAAVEDRA: Actualiza los documentos como Pendientes para replicarlos al middleware.
									List<UserDocument> documents = documentsDao.searchCriteriaByUuidOnly(fis.getUuidFactura());
									if(documents != null && !documents.isEmpty()) {
										for(UserDocument doc : documents) {
											doc.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
											this.documentsService.update(doc, null, null);
										}
									}
								}
			            }
				
				}
				return mapOK("Respuesta OK updatePostVoucherForeign", objList.size());
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				
				return mapError(e.getMessage() + " - setPoListHistory");
			}
		}
		
		
	
		
		public Map<String, Object> savePdfBatch(Map<String, Object> data){
		UserDocument doc=new UserDocument();
			try {
				String name=(String) data.get("nombre");
				doc.setDocumentType("PDFBATCH");
				doc.setUploadDate(new Date());
				doc.setType("application/pdf");
				doc.setName(name);
				doc.setAddressBook(name.split("\\.")[0].split("_")[2]);
//				doc.setAddressBook(new PDFUtils().getPdfBatchNumber(input));
				doc.setFiscalType("BATCH");
				doc.setContent(Base64.getDecoder().decode((String) data.get("file")));
				doc.setAddressBook(new PDFUtils().getPdfBatchNumber(doc.getContent()));
				doc.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);//JSAAVEDRA: Estatus Pendiente para replicarlos al middleware.
				userDocumentDao.save(doc);
			} catch (Exception e) {
				log4j.error("Exception" , e);
				return mapError("getListPdfBatch: "+e.getMessage());				
			}
			
			try {
				//JSAAVEDRA: Guarda Carátula de Batch con estatus Pendiente para replicarlo al middleware.
				//El BatchNumber viene en el AddressBook en UserDocuments
				String batchId = doc.getAddressBook();
				this.saveBatchCoverDocument(batchId);
				
				//JSAAVEDRA: Actualiza las facturas de Conceptos a estatus Pendiente para replicarlos al middleware.
				List<FiscalDocumentsConcept> conceptList = fiscalDocumentConceptService.searchByIdBatch(batchId);
				if(conceptList != null && !conceptList.isEmpty()) {
					
					for(FiscalDocumentsConcept concept : conceptList) {						
						if(concept.getUuidFactura() != null && !concept.getUuidFactura().trim().isEmpty()) {
							
							List<UserDocument> documents = documentsDao.searchCriteriaByUuidOnly(concept.getUuidFactura());
							if(documents != null && !documents.isEmpty()) {
								
								for(UserDocument docDet : documents) {									
									if(!AppConstants.STATUS_SUCCESS_REPLICATION.equals(docDet.getReplicationStatus())) {
										docDet.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
										documentsService.update(docDet, null, null);
									}
								}
							}
							
						}						
					}
				}
				
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}
			
			return mapOK("ok", 0);
			
		}

		public Map<String, Object> getAddressBookDocs(){
			try{				
				long fileAttachedSize = 0;
				int fileAttachedCount = 0;
				List<FileDTO> files = new ArrayList<FileDTO>();				
				String[] docTypes = new String[]{"loadRfcDoc", "loadDomDoc", "loadEdoDoc", "loadIdentDoc", "loadActaConst"};
				
				List<UserDocument> userDocuments = userDocumentDao.getDocsByRepStatusAndDocType(AppConstants.STATUS_PENDING_REPLICATION, docTypes);
				if(userDocuments != null  && !userDocuments.isEmpty()) {
					
					Map<String, Integer> listNameSeq = new HashMap<String, Integer>();
					SimpleDateFormat sdf = new SimpleDateFormat("YYMMdd_HHmmss");
					String strDate = sdf.format(new Date());
					String currentAddressNumber = "";
					
					for (UserDocument document : userDocuments) {
						
						try {							
							FileDTO file = new FileDTO();
							currentAddressNumber = document.getAddressBook();
							String docType = "";							
									
							switch(document.getDocumentType()) {
							case "loadRfcDoc" : docType = "CSF";
								break;
							case "loadDomDoc" : docType = "OC";
								break;
							case "loadEdoDoc" : docType = "EC";
								break;
							case "loadIdentDoc" : docType = "FAC";
								break;
							case "loadActaConst" : docType = "DB";
								break;
							default : docType = "OTR";
								break;
							}
							
							//Valida que no rebase límite de MB por envío
							fileAttachedSize += document.getSize();
							fileAttachedCount += 1;
							if(fileAttachedSize > MAX_ATTACHED_SIZE && fileAttachedCount > 1) {
								break;//Solo enviará los archivos adjuntos hasta el momento
							}
							
							//Obtiene el nombre genérico del archivo
							String fileExt = FilenameUtils.getExtension(document.getName());
							String fileName = "PP_" + strDate + "_" + document.getAddressBook() + "_" + docType + "." + fileExt;
							
							//Se obtiene el número consecutivo correspondiente
							int nextNumber = 1;
							if(listNameSeq.containsKey(fileName)) {
								nextNumber = listNameSeq.get(fileName).intValue() + 1;
								listNameSeq.replace(fileName, nextNumber);
							} else {
								listNameSeq.put(fileName, 1);
							}
							
							//Obtiene el nombre definitivo con consecutivo
							fileName = "PP_" + strDate + "_" + document.getAddressBook() + "_" + docType + "_" + nextNumber + "." + fileExt;
							
							file.setId(document.getId());
    	                	file.setFile64(Base64.getEncoder().encodeToString(document.getContent()));
    	                	file.setFileName(fileName);
    	                	file.setFileExt(fileExt);
    	                	files.add(file);
						} catch (Exception e) {
							log4j.error("CRYO - FILE IMPORT: Error al obtener el documento del proveedor "  + currentAddressNumber);
							log4j.error("Exception" , e);
							e.printStackTrace();
						}
					}
				}
				
				String jsonInString = null;
				Gson gson = new Gson();  
				jsonInString = gson.toJson(files.toArray()); 
			    return mapOK(jsonInString, files.size());
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - getAddressBookDocs");
			}
		}
		
		public Map<String, Object> updateAddressBookDocs(List<FileDTO> fileDTOList){
			try {
				if(fileDTOList != null && !fileDTOList.isEmpty()) {
					for(FileDTO dto : fileDTOList) {
						UserDocument doc = userDocumentDao.getById(dto.getId());
						if(doc != null) {
							doc.setReplicationDate(new Date());
							if("SUCCESS".equals(dto.getStatus())) {								
								doc.setReplicationStatus(AppConstants.STATUS_SUCCESS_REPLICATION);
							} else {
								doc.setReplicationStatus(AppConstants.STATUS_ERROR_REPLICATION);								
							}
							userDocumentDao.update(doc);							
						}
					}
				}
				
				String jsonInString = null;
				Gson gson = new Gson();  
				jsonInString = gson.toJson(fileDTOList.toArray()); 
			    return mapOK(jsonInString, fileDTOList.size());
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - updateAddressBookDocs");
			}
		}
		
		public Map<String, Object> getVoucherDocs(){
			try{
				long fileAttachedSize = 0;
				int fileAttachedCount = 0;
				List<FileDTO> files = new ArrayList<FileDTO>();
				String[] fisTypes = new String[]{"Factura", "Evidencia", "Otros", "BATCHCOVER", "BATCH", "I"};
				
				List<UserDocument> userDocuments = userDocumentDao.getDocsByRepStatusAndFiscalType(AppConstants.STATUS_PENDING_REPLICATION, fisTypes);
				if(userDocuments != null  && !userDocuments.isEmpty()) {
					
					Map<String, Integer> listNameSeq = new HashMap<String, Integer>();
					SimpleDateFormat sdf = new SimpleDateFormat("YYMMdd_HHmmss");
					String strDate = sdf.format(new Date());
					String currentUUID = "";
					String currentAddressBook = "";
					
					for (UserDocument document : userDocuments) {
						try {
							
							FileDTO file = new FileDTO();
							String filePrefix = "PP";
							String fisType = "";
							String serie = "";
							String folio = "";
							String vinv = "";
							currentUUID = document.getUuid();
							currentAddressBook = document.getAddressBook();
							
							if(document.getFiscalType() != null && !"null".equals(document.getFiscalType().trim()) && !"NULL".equals(document.getFiscalType().trim())) {
								fisType = document.getFiscalType().toUpperCase().trim();
							}
							if(document.getSerie() != null && !"null".equals(document.getSerie().trim()) && !"NULL".equals(document.getSerie().trim())) {
								serie = document.getSerie().replaceAll("[^a-zA-Z0-9 \\-]", "-").concat("-");
							}
							if(document.getFolio() != null && !"null".equals(document.getFolio().trim()) && !"NULL".equals(document.getFolio().trim())) {
								folio = document.getFolio();
							}
							
							//Si la factura no tiene folio, se asignan los últimos 4 caracteres del UUID
							if("".equals(folio) && document.getUuid() != null && document.getUuid().length() >= 4) {
								serie = "ZX";
								folio = document.getUuid().substring(document.getUuid().length() - 4);							
							}

							vinv = serie + folio;
							
							//Si el vinv tiene mas de 25 caracteres, se asignan los últimos 12 caracteres del UUID
							if(vinv.length() > 25 && document.getUuid() != null && document.getUuid().length() >= 12) {
								vinv = document.getUuid().substring(document.getUuid().length() - 12).replaceAll("[^a-zA-Z0-9]", "");
							}
							
							
							//JSC: Si el vinv tiene mas de 25 caracteres, truncar a 25 (Validación por nueva versión de BD en JDE)
							if(vinv.length() > 25) {
								vinv = vinv.substring(0, 25);
							}

							//Validaciones Facturas Foráneas --------------------------------
							if("FACTURA".equals(fisType) && document.getName().startsWith("FACT_FOR_") && "TEMP".equals(document.getType())) {
								document.setReplicationMessage("Factura Foránea XML.");
								document.setReplicationDate(new Date());
								document.setReplicationStatus(AppConstants.STATUS_NOTSENT_REPLICATION);
								userDocumentDao.update(document);
								continue;//Para las facturas de Proveedores Extranjeros, no se envía el xml del Portal.								
							}
							
							if("OTROS".equals(fisType) && document.getName().startsWith("FACT_FOR_")) {
								fisType = "EVIDENCIA";
								FiscalDocuments fdoc = fiscalDocumentService.getFiscalDocumentsByUuid(document.getUuid());
								currentAddressBook = fdoc.getAddressNumber();
								vinv = fdoc.getSerie();
							}							
							
							//Validaciones Facturas Batch ---------------------------------------
							if("I".equals(fisType)) {
								fisType = "FACTURA";
								List<FiscalDocumentsConcept> concepts = fiscalDocumentService.getFiscalDocumentsConceptByUUID(document.getUuid());
								FiscalDocuments fdoc = fiscalDocumentService.getById(Integer.valueOf(concepts.get(0).getBatchID()).intValue());
								currentAddressBook = document.getAddressBook();
								vinv = fdoc.getSemanaPago().length() > 25 ? fdoc.getSemanaPago().substring(0, 25).toUpperCase()
										: fdoc.getSemanaPago().toUpperCase();
							}
							
							if("BATCH".equals(fisType)) {
								filePrefix = "Bat";
								//En este tipo de documento este campo contiene el Número de Batch
								FiscalDocuments fdoc = fiscalDocumentService.getById(Integer.valueOf(document.getAddressBook()).intValue());
								currentAddressBook = fdoc.getAddressNumber();
								vinv = fdoc.getSemanaPago().length() > 25 ? fdoc.getSemanaPago().substring(0, 25).toUpperCase()
										: fdoc.getSemanaPago().toUpperCase();								
							}

							if("BATCHCOVER".equals(fisType)) {
								filePrefix = "Car";
								//En este tipo de documento este campo contiene el Número de Batch
								FiscalDocuments fdoc = fiscalDocumentService.getById(Integer.valueOf(document.getAddressBook()).intValue());
								currentAddressBook = fdoc.getAddressNumber();
								vinv = fdoc.getSemanaPago().length() > 25 ? fdoc.getSemanaPago().substring(0, 25).toUpperCase()
										: fdoc.getSemanaPago().toUpperCase();
							}
							
							//Validaciones Facturas Sin VINV ---------------------------------------
							if("".equals(vinv)) {
								document.setReplicationMessage("Documento Sin VINV.");
								document.setReplicationDate(new Date());
								document.setReplicationStatus(AppConstants.STATUS_NOTSENT_REPLICATION);
								userDocumentDao.update(document);
								continue;//Si no tienen VINV no se envían las facturas, no tiene forma de relacionar en JDE								
							}
							
							switch(fisType) {
							case "FACTURA": fisType = "FAC";
								break;
							case "EVIDENCIA": fisType = "EV";
								break;
							case "OTROS": fisType = "OTR";
								break;
							case "BATCH": fisType = "BAT";
								break;
							case "BATCHCOVER": fisType = "CAR";
								break;
							default : fisType = "OTR";
								break;
							}
							
							//Valida que no rebase límite de MB por envío
							fileAttachedSize += document.getSize();
							fileAttachedCount += 1;
							if(fileAttachedSize > MAX_ATTACHED_SIZE && fileAttachedCount > 1) {
								break;//Solo enviará los archivos adjuntos hasta el momento
							}
							
							//Obtiene el nombre genérico del archivo
							String fileExt = FilenameUtils.getExtension(document.getName());
							String fileName =
									filePrefix + "_" +
									strDate + "_" +									
									currentAddressBook + "_" +
									fisType + "_" +
									vinv +
									"." + fileExt;
							
							//Se obtiene el número consecutivo correspondiente
							int nextNumber = 1;
							if(listNameSeq.containsKey(fileName)) {
								nextNumber = listNameSeq.get(fileName).intValue() + 1;
								listNameSeq.replace(fileName, nextNumber);
							} else {
								listNameSeq.put(fileName, 1);
							}
							
							//Obtiene el nombre definitivo con consecutivo
							fileName =
									filePrefix + "_" +
									strDate + "_" +
									currentAddressBook + "_" +
									fisType + 
									nextNumber + "_" +
									vinv +
									"." + fileExt;
							
							file.setId(document.getId());
    	                	file.setFile64(Base64.getEncoder().encodeToString(document.getContent()));
    	                	file.setFileName(fileName);
    	                	file.setFileExt(fileExt);
    	                	files.add(file);
						} catch (Exception e) {
							log4j.error("CRYO - FILE IMPORT: Error al obtener archivos de la factura "  + currentUUID + " Proveedor " + currentAddressBook);
							log4j.error("Exception" , e);
							e.printStackTrace();							
						}
					}							
				}
				
				String jsonInString = null;
				Gson gson = new Gson();  
				jsonInString = gson.toJson(files.toArray()); 
			    return mapOK(jsonInString, files.size());
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - getVoucherDocs");
			}
		}
		
		public Map<String, Object> updateVoucherDocs(List<FileDTO> fileDTOList){
			try {
				if(fileDTOList != null && !fileDTOList.isEmpty()) {
					for(FileDTO dto : fileDTOList) {
						
						UserDocument doc = userDocumentDao.getById(dto.getId());
						if(doc != null) {
							doc.setReplicationDate(new Date());
							
							if("SUCCESS".equals(dto.getStatus())) {								
								doc.setReplicationStatus(AppConstants.STATUS_SUCCESS_REPLICATION);
							} else {
								doc.setReplicationStatus(AppConstants.STATUS_ERROR_REPLICATION);								
							}
							
							userDocumentDao.update(doc);
						}
					}
				}
				
				String jsonInString = null;
				Gson gson = new Gson();  
				jsonInString = gson.toJson(fileDTOList.toArray()); 
			    return mapOK(jsonInString, fileDTOList.size());
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - updateVoucherDocs");
			}
		}
		
		public Map<String, Object> getEnabledPO(int start, int limit) {
			try{
				//JSAAVEDRA
				log4j.info("*********** STEP 1: getEnabledPO:start:" + start + " -limit:" + limit);
				String jsonInString = "";
				int size = 0;
				
				List<PurchaseOrder> poList = purchaseOrderService.getOpenOrderPO(start, limit);
				if (poList != null && poList.size() > 0) {
					ObjectMapper jsonMapper = new ObjectMapper();
					jsonInString = jsonMapper.writeValueAsString(poList.toArray());
					size = poList.size();
				}
				return mapOK(jsonInString, size);
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - getEnabledPO");				
			}
		}

		public Map<String, Object> setDisabledPO(List<PurchaseOrder> poList) {
			
			try{
				//JSAAVEDRA
				log4j.info("*********** STEP 2: setDisabledPO");
				String jsonInString = "";
				int size = 0;
					
				if (poList != null && poList.size() > 0) {
					for(PurchaseOrder po : poList) {
						
						PurchaseOrder portalPO = purchaseOrderService.searchbyOrderAndAddressBookAndCompany(po.getOrderNumber(), po.getAddressNumber(), po.getOrderType(), po.getCompanyKey());
						if(portalPO != null) {							
							//Actualiza Orden de Compra en la tabla PurchaseOrder
							portalPO.setOrderStauts(AppConstants.STATUS_OC_CANCEL);
							purchaseOrderService.updateOrders(portalPO);
							
							try {
								//Notificación Proveedor
								Supplier s = supplierService.searchByAddressNumber(portalPO.getAddressNumber());
								if(s!=null) {
									String emailContent = AppConstants.EMAIL_MIDDLEWARE_MSG_2;
									emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(portalPO.getOrderNumber()));
									emailContent = emailContent.replace("_ORDERTYPE_", portalPO.getOrderType());
									emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);
									
									String emailRecipient = (s.getEmailSupplier());
		 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
		 							emailAsyncSup.setProperties(
		 									AppConstants.EMAIL_PO_CANCELED_NOTIF,
		 									stringUtils.prepareEmailContent(emailContent),
		 									emailRecipient);
		 							emailAsyncSup.setMailSender(mailSenderObj);
		 							emailAsyncSup.setAdditionalReference(udcDao, portalPO.getOrderType());
		 							Thread emailThreadSup = new Thread(emailAsyncSup);
		 							emailThreadSup.start();
								}
								
								//Notificación Compras
								UDC emailUDC = udcService.searchBySystemAndKey("CANCEL", "EMAIL");
								if(emailUDC != null) {
									if(emailUDC.getStrValue1() != null && !"".equals(emailUDC.getStrValue1().trim())) {
										String emailContent = AppConstants.EMAIL_MIDDLEWARE_MSG_3;
										emailContent = emailContent.replace("_SUPNUM_", portalPO.getAddressNumber());
										emailContent = emailContent.replace("_SUPNAME_", portalPO.getLongCompanyName());
										emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(portalPO.getOrderNumber()));
										emailContent = emailContent.replace("_ORDERTYPE_", portalPO.getOrderType());
										emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);
										
										String emailRecipient = (emailUDC.getStrValue1().trim());
			 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			 							emailAsyncSup.setProperties(
			 									AppConstants.EMAIL_PO_CANCELED_NOTIF,
			 									stringUtils.prepareEmailContent(emailContent),
			 									emailRecipient);
			 							emailAsyncSup.setMailSender(mailSenderObj);
			 							emailAsyncSup.setAdditionalReference(udcDao, portalPO.getOrderType());
			 							Thread emailThreadSup = new Thread(emailAsyncSup);
			 							emailThreadSup.start();									
									}
								}
							} catch (Exception e) {
								log4j.error("Exception" , e);
							}					
						}
					}
					
					ObjectMapper jsonMapper = new ObjectMapper();
					jsonInString = jsonMapper.writeValueAsString(poList.toArray());
					size = poList.size();
				}
				return mapOK(jsonInString, size);
				
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - setDisabledPO");
				
			}
		}
		
		public Map<String, Object> getEnabledReceipts(int start, int limit) {
			try{
				//JSAAVEDRA
				log4j.info("*********** STEP 1: getEnabledReceipts:start:" + start + " -limit:" + limit);
				String jsonInString = "";
				int size = 0;
				
				List<Receipt> receiptList = purchaseOrderService.getOpenOrderReceipts(start, limit);
				if (receiptList != null && receiptList.size() > 0) {
					ObjectMapper jsonMapper = new ObjectMapper();
					jsonInString = jsonMapper.writeValueAsString(receiptList.toArray());
					size = receiptList.size();
				}
				return mapOK(jsonInString, size);
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - getEnabledReceipts");				
			}
		}

		public Map<String, Object> setDisabledReceipts(List<Receipt> receiptList) {
			
			try{
				//JSAAVEDRA
				log4j.info("*********** STEP 2: setDisabledReceipts");
				String jsonInString = "";
				int size = 0;
				UDC emailUDC = udcService.searchBySystemAndKey("CANCEL", "EMAIL");
				
				if (receiptList != null && receiptList.size() > 0) {					
					//Actualiza Fiscal Documents 
					for(Receipt r : receiptList) {
						if(r.getAddressNumber() != null && r.getUuid() != null && !r.getUuid().isEmpty()) {
							List<FiscalDocuments> fdList = fiscalDocumentService.getFiscalDocuments(r.getAddressNumber(), "", r.getUuid(), "", 0, 100);
							if(fdList != null && !fdList.isEmpty()) {
								for(FiscalDocuments f : fdList) {
									if(!AppConstants.STATUS_CANCEL.equals(f.getStatus())) {
										f.setStatus(AppConstants.STATUS_CANCEL);
										f.setApprovalStatus(AppConstants.STATUS_CANCEL);
										fiscalDocumentService.updateDocument(f);
										
										try {
											//Notificación Planta
											if(f.getUuidFactura() != null && !"".equals(f.getUuidFactura().trim())
													&& f.getPlant() != null && !"".equals(f.getPlant().trim())) {
												
												UDC approverUDC = udcService.searchBySystemAndSystemRef("APPROVERPONP", "FIRST_APPROVER", f.getPlant());
												if(approverUDC != null) {
													String emailContent = AppConstants.EMAIL_MIDDLEWARE_MSG_6;
													emailContent = emailContent.replace("_SUPNUM_", r.getAddressNumber());
													emailContent = emailContent.replace("_SUPNAME_", f.getSupplierName());
													emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(r.getOrderNumber()));
													emailContent = emailContent.replace("_ORDERTYPE_", r.getOrderType());
													emailContent = emailContent.replace("_DOCNUM_", String.valueOf(r.getDocumentNumber()));
													emailContent = emailContent.replace("_UUIDINV_", f.getUuidFactura());
													emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);
													
													String emailRecipient = (approverUDC.getStrValue2());
						 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						 							emailAsyncSup.setProperties(
						 									AppConstants.EMAIL_RECEIPT_CANCELED_NOTIF,
						 									stringUtils.prepareEmailContent(emailContent),
						 									emailRecipient);
						 							emailAsyncSup.setMailSender(mailSenderObj);
						 							emailAsyncSup.setAdditionalReference(udcDao, r.getOrderType());
						 							Thread emailThreadSup = new Thread(emailAsyncSup);
						 							emailThreadSup.start();
												}
											}
										} catch (Exception e) {	
											log4j.error("Exception" , e);
										}

									}
								}
							}
						}
					}
					
					//Actualiza Recibos
					purchaseOrderService.updateReceipts(receiptList);
										
					try {
						//Envia correo de cancelación.
						List<String> receiptSentList = new ArrayList<String>();
						for(Receipt r : receiptList) {
							if(r.getAddressNumber() != null && !r.getAddressNumber().trim().isEmpty()) {
								
								String receiptString = r.getAddressNumber().trim() + "_" + r.getOrderNumber() + "_" + r.getOrderType() + "_" + r.getDocumentNumber();
								if(!receiptSentList.contains(receiptString)) {
									receiptSentList.add(receiptString);
									
									Supplier s = supplierService.searchByAddressNumber(r.getAddressNumber());
									if(s!=null) {
										//Notificación Proveedor
										String emailContent = AppConstants.EMAIL_MIDDLEWARE_MSG_4;
										emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(r.getOrderNumber()));
										emailContent = emailContent.replace("_ORDERTYPE_", r.getOrderType());
										emailContent = emailContent.replace("_DOCNUM_", String.valueOf(r.getDocumentNumber()));
										emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);
										
										String emailRecipient = (s.getEmailSupplier());
			 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			 							emailAsyncSup.setProperties(
			 									AppConstants.EMAIL_RECEIPT_CANCELED_NOTIF,
			 									stringUtils.prepareEmailContent(emailContent),
			 									emailRecipient);
			 							emailAsyncSup.setMailSender(mailSenderObj);
			 							emailAsyncSup.setAdditionalReference(udcDao, r.getOrderType());
			 							Thread emailThreadSup = new Thread(emailAsyncSup);
			 							emailThreadSup.start();			 						
									}
									
									if(s != null) {
										//Notificación Compras										
										if(emailUDC != null) {
											if(emailUDC.getStrValue1() != null && !"".equals(emailUDC.getStrValue1().trim())) {
												String emailContent = AppConstants.EMAIL_MIDDLEWARE_MSG_5;
												emailContent = emailContent.replace("_SUPNUM_", r.getAddressNumber());
												emailContent = emailContent.replace("_SUPNAME_", s.getRazonSocial());
												emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(r.getOrderNumber()));
												emailContent = emailContent.replace("_ORDERTYPE_", r.getOrderType());
												emailContent = emailContent.replace("_DOCNUM_", String.valueOf(r.getDocumentNumber()));
												emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);
												
												String emailRecipient = (emailUDC.getStrValue1().trim());
					 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
					 							emailAsyncSup.setProperties(
					 									AppConstants.EMAIL_RECEIPT_CANCELED_NOTIF,
					 									stringUtils.prepareEmailContent(emailContent),
					 									emailRecipient);
					 							emailAsyncSup.setMailSender(mailSenderObj);
					 							emailAsyncSup.setAdditionalReference(udcDao, r.getOrderType());
					 							Thread emailThreadSup = new Thread(emailAsyncSup);
					 							emailThreadSup.start();									
											}
										}
									}
								}
							}
						}
					} catch (Exception e) {	
						log4j.error("Exception" , e);
					}
					
					ObjectMapper jsonMapper = new ObjectMapper();
					jsonInString = jsonMapper.writeValueAsString(receiptList.toArray());
					size = receiptList.size();
				}
				return mapOK(jsonInString, size);
				
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - setDisabledReceipts");
				
			}
		}
		
		public Map<String, Object> getBlockAndUnblocksSuppCompl(int start, int limit) {
			try{
				log4j.info("*********** STEP 1: getBlockAndUnblocksSuppCompl:start:" + start + " -limit:" + limit);
				String jsonInString = "";
				int size = 0;
				
				List<Receipt> receiptList = purchaseOrderService.getBlockAndUnblocksSuppCompl(start, limit);
				List<BlockerSupReceiptDTO> blockerSupReceiptDTOList = new ArrayList<BlockerSupReceiptDTO>();
				if (receiptList != null && receiptList.size() > 0) {
					for(Receipt r : receiptList) {
						BlockerSupReceiptDTO blockerSupReceiptDTO = new BlockerSupReceiptDTO();
						
						blockerSupReceiptDTO.setAddressNumber(r.getAddressNumber());
						blockerSupReceiptDTO.setAction(r.getActionReplication());
						blockerSupReceiptDTO.setTipoBloqueo(AppConstants.BLOCK_TYPE_CRP);
						blockerSupReceiptDTO.setUsuario(AppConstants.USER_PPROVEEDORES);
						blockerSupReceiptDTO.setBloquear(AppConstants.BLOCK_ACTION.equals(r.getActionReplication()) ? 1 : 0);
						blockerSupReceiptDTO.setAccountId(r.getAccountId());
						blockerSupReceiptDTO.setDocumentNumber(r.getDocumentNumber());
						blockerSupReceiptDTO.setUuid(r.getUuid());
						blockerSupReceiptDTO.setId(r.getId());
						blockerSupReceiptDTOList.add(blockerSupReceiptDTO);
					}
					ObjectMapper jsonMapper = new ObjectMapper();
					jsonInString = jsonMapper.writeValueAsString(blockerSupReceiptDTOList.toArray());
					size = blockerSupReceiptDTOList.size();
				}
				return mapOK(jsonInString, size);
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - getBlockAndUnblocksSuppCompl");				
			}
		}
		
		
		public Map<String, Object> updateBlockAndUnblocksSuppCompl(BlockerSupReceiptDTO bsr) {
			try{
				log4j.info("*********** STEP 2: updateBlockAndUnblocksSuppCompl:" + bsr.toString());
				String jsonInString = "";
				int size = 0;
				
				Receipt receipt = purchaseOrderService.getReceiptById(bsr.getId());
				
				if(AppConstants.UNBLOCK_ACTION.equals(bsr.getAction())) {
					receipt.setActionReplication("");
					receipt.setReplicationComplPagoMessage("EXECUTE " + AppConstants.UNBLOCK_ACTION);
				}
				
				if(AppConstants.BLOCK_ACTION.equals(bsr.getAction())) {
					receipt.setReplicationComplPagoMessage("EXECUTE " + AppConstants.BLOCK_ACTION);
				}
				receipt.setReplicationComplPagoStatus("");
				receipt.setReplicationComplPagoDate(new Date());
				purchaseOrderService.updateReceipt(receipt);
				
					ObjectMapper jsonMapper = new ObjectMapper();
					jsonInString = jsonMapper.writeValueAsString(receipt);
					size = 1;
				
				return mapOK(jsonInString, size);
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - updateBlockAndUnblocksSuppCompl");				
			}
		}
		
		public void saveBatchCoverDocument(String batchID) {
			byte[] pdfBytes = null;
			
			try {
				int batch = Integer.valueOf(batchID).intValue();
				
				PDFUtils pdfUtils = new PDFUtils();
				Supplier supplier = new Supplier();
				StringBuilder approvalMsg = new StringBuilder();
				
				FiscalDocuments fdoc = fiscalDocumentService.getById(batch);
				List<FiscalDocumentsConcept> fdocConcepts = fiscalDocumentConceptService.searchByIdBatch(batchID);
				List<ApprovalBatchFreight> approvalBatchList = approvalBatchFreightService.searchByIdBatch(batchID);
				
				if(fdocConcepts != null && fdocConcepts.size() > 0) {
					supplier = supplierService.searchByAddressNumber(fdocConcepts.get(0).getAddressNumber());
				}
				
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
				
				pdfBytes = pdfUtils.getFilePDFFleightCover(fdoc,supplier,fdocConcepts,approvalMsg.toString(),udcService);
				
				UserDocument doc = new UserDocument();
				doc.setDocumentType("PDFBATCHCOVER");
				doc.setUploadDate(new Date());
				doc.setType("application/pdf");
				doc.setName("BATCH_COVER_" + batchID + ".pdf");
				doc.setFiscalType("BATCHCOVER");
				doc.setContent(pdfBytes);
				doc.setAddressBook(batchID);
				doc.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
				userDocumentDao.save(doc);
				
			} catch (Exception e) {
				log4j.error("Error al crear la Carátula del Batch: " + batchID);
				log4j.error("Exception" , e);
				e.printStackTrace();
			}
		}
		
		
		public Map<String, Object> getIdsPaymentsSupplier(){
			try{
						List<String> sd = paymentSupplierDao.getAddressAndMaxIdList();
					
				Gson gson = new Gson();  
				String jsonInString = gson.toJson(sd); 
			    return mapOK(jsonInString,sd.size());
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return mapError(e.getMessage() + " - getIdsPaymentsSupplier");
			}
		}
		
		@Transactional
		public Map<String, Object> savePaymentsSupplier(List<HashMap<String, Object>> dataList) {
		    try {
		        for (HashMap<String, Object> data : dataList) {
		            log4j.info("Processing data: " + data);

		            PaymentSupplier ps = new PaymentSupplier();
		            ps.setAddressBook(((int) Double.parseDouble((String) data.get("RMPYE")))+"");
		            ps.setDocCotejo((String) data.get("RMDCTM"));
		            ps.setCurrencyCode((String) data.get("RMCRCD"));

		            // Log the value of "rmdmtj" before conversion
		            log4j.info("rmdmtj (before conversion): " + data.get("RMDMTJ"));

		            ps.setPaymentDate(JdeJavaJulianDateTools.Methods.JulianDateToJavaDate(Integer.parseInt((String) data.get("RMDMTJ"))));
		            BigDecimal bd = new BigDecimal((String) data.get("RMPAAP"));

		            // Log the calculated payment amount before setting
		            double paymentAmount = (bd.doubleValue() / 100) * -1;
		            log4j.info("Calculated paymentAmount: " + paymentAmount);

		            ps.setPaymentAmount(paymentAmount);
		            ps.setInvoiceNumber((String) data.get("RNRMK"));
		            ps.setCompany((String) data.get("RNCO"));

		            // Log the value of "rmpyid" before conversion
		            log4j.info("rmpyid (before conversion): " + data.get("RMPYID"));

		            ps.setIdPayment((int) Double.parseDouble((String) data.get("RMPYID")));

		            ps.setStatus((String) data.get("RPPST"));
		            ps.setUploadDate(new Date());
		            log4j.info("RMVDGJ (before conversion): " + data.get("RMVDGJ"));
		            ps.setStatusPay((String) data.get("RMVDGJ"));
		            
		            ps.setSuplierCompanyName(supplierDao.searchByAddressNumber(ps.getAddressBook()).getRazonSocial());
		            
		            
		       paymentSupplierDao.save(ps);
		            log4j.info("PaymentSupplier saved: " + ps.toString());

		        }

		        return mapOK("ok", 0);
		    } catch (Exception e) {
		        log4j.error("Exception", e);
		        return mapError("savePaymentsSupplier: " + e.getMessage());				
		    }
		}
		
		
		@Transactional
		public Map<String, Object> savePaymentsSupplierDetail(List<HashMap<String, Object>> dataList) {
		    try {
		        for (HashMap<String, Object> data : dataList) {
		            log4j.info("Processing data: " + data);

		            PaymentSupplierDetail ps = new PaymentSupplierDetail();
		            
		            ps.setIdPayment((int) Double.parseDouble((String) data.get("RMPYID"))); 
		            ps.setInvoiceNumber((String) data.get("RPVINV"));
		            
		            BigDecimal bd = new BigDecimal((String) data.get("RMPAAP"));
		            double paymentAmount = (bd.doubleValue() / 100) * -1;
		            log4j.info("Calculated paymentAmount: " + paymentAmount);

		            ps.setMountInvoice(paymentAmount);
		            ps.setDocPasiveNumber(((int) Double.parseDouble((String) data.get("RNDOC")))+"");
		            ps.setTypDocPas((String) data.get("RNDCT"));
		          
		            paymentSupplierDetailDao.save(ps);
		            log4j.info("PaymentSupplier saved: " + ps.toString());

		        }

		        return mapOK("ok", 0);
		    } catch (Exception e) {
		        log4j.error("Exception", e);
		        return mapError("savePaymentsSupplier: " + e.getMessage());				
		    }
		}
		


		
}
