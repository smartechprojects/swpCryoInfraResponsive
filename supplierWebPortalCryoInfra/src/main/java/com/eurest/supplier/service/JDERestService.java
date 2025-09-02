package com.eurest.supplier.service;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.eurest.supplier.dao.UDCDao;
import com.eurest.supplier.dto.AddressBookLayot;
import com.eurest.supplier.dto.IntegerListDTO;
import com.eurest.supplier.dto.InvoiceRequestDTO;
import com.eurest.supplier.dto.PurchaseOrderDTO;
import com.eurest.supplier.dto.SupplierDTO;
import com.eurest.supplier.edi.BatchJournalDTO;
import com.eurest.supplier.edi.ReceivingAdviceHeaderDTO;
import com.eurest.supplier.edi.SupplierJdeDTO;
import com.eurest.supplier.edi.VoucherHeaderDTO;
import com.eurest.supplier.model.ExchangeRate;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.CustomBroker;
import com.eurest.supplier.model.LogDataJEdwars;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.PurchaseOrderDetail;
import com.eurest.supplier.model.PurchaseOrderPayment;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.Tolerances;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.JdeJavaJulianDateTools;
import com.eurest.supplier.util.LoggerJEdwars;
import com.eurest.supplier.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;

@Component
public class JDERestService {
  @Autowired
  PurchaseOrderService purchaseOrderService;
  
  @Autowired
  TolerancesService tolerancesService;
  
  @Autowired
  NextNumberService nextNumberService;
  
  @Autowired
  SupplierService supplierService;
  
  @Autowired
  UsersService usersService;
  
  @Autowired
  EmailService emailService;
  
  @Autowired
  UdcService udcService;
  
  @Autowired
  StringUtils stringUtils;
  
  @Autowired
  UDCDao udcDao;
  
  @Autowired
  DocumentsService documentsService;
  
  @Autowired
  FiscalDocumentService fiscalDocumentService;

  @Autowired
  CustomBrokerService customBrokerService;
    
  @Autowired
  ExchangeRateService exchangeRateService;
  
  @Autowired
  LoggerJEdwars loggerJEdwars;
  
  String orderDate;
  String lttr;
  String nxtr;
  
  @Autowired
  private JavaMailSender mailSenderObj;
  
  private Logger log4j = Logger.getLogger(JDERestService.class);

  	//@Scheduled(cron = "0 30 23 * * ?")
  	//@Scheduled(fixedDelay = 4200000, initialDelay = 30000)
	//@Scheduled(cron = "0 30 5 * * ?")
    //@Scheduled(cron = "0 0/3 * * * ?")
	public void getOrderPayments() {
		try {			
			for(int i=0; i<=20; i++) {
				int start = i*500;
				
				List<Receipt> poList = purchaseOrderService.getPaymentPendingReceipts(start, 500);
				if (poList != null) {
					if (poList.size() > 0) {
						IntegerListDTO idto = new IntegerListDTO();
						List<String> uuidList = new ArrayList<String>();
						for(Receipt po : poList) {
							uuidList.add(po.getUuid());
						}
						
						if(uuidList.size() > 0) {
							idto.setUuidList(uuidList);
							ObjectMapper jsonMapper = new ObjectMapper();
							String jsonInString = jsonMapper.writeValueAsString(idto);

							HttpHeaders httpHeaders = new HttpHeaders();
							httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
							httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			 				final String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/payments";
							Map<String, String> params = new HashMap<String, String>();
							HttpEntity<?> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);
							RestTemplate restTemplate = new RestTemplate();
							ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class, params);
							HttpStatus statusCode = responseEntity.getStatusCode();

							if (statusCode.value() == 200) {
								String body = responseEntity.getBody();
								if (body != null) {
									ObjectMapper mapper = new ObjectMapper();
									Receipt[] response = mapper.readValue(body, Receipt[].class);
									List<Receipt> objList = Arrays.asList(response);
									
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
			 	 								String emailRecipient = (s.getEmailSupplier());
			 	 	 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			 	 	 							String emailContent = AppConstants.EMAIL_PAYMENT_RECEIPT_NOTIF_CONTENT;
			 	 	 							emailContent = emailContent.replace("_UUID_", o.getUuid());
			 	 	 							emailContent = emailContent.replace("_GR_", String.valueOf(o.getDocumentNumber()));
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

										log4j.info("Guardado:" + response.length);
									}
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

	}

  	//@Scheduled(cron = "0 30 23 * * ?")
  	//@Scheduled(fixedDelay = 4200000, initialDelay = 30000)
	//@Scheduled(cron = "0 30 4 * * ?")
    //@Scheduled(cron = "0 0/3 * * * ?")
	public void getFiscalDocumentPayments() {
		try {			
			for(int i=0; i<=10; i++) {
				int start = i*500;
				
				List<FiscalDocuments> fdList = fiscalDocumentService.getFiscalDocuments("", AppConstants.STATUS_ACCEPT, "", "", start, 500);
				if (fdList != null) {
					if (fdList.size() > 0) {
						IntegerListDTO idto = new IntegerListDTO();
						List<String> uuidList = new ArrayList<String>();
						for(FiscalDocuments fd : fdList) {
							if(!uuidList.contains(fd.getUuidFactura())) {
								uuidList.add(fd.getUuidFactura());	
							}
						}
						
						if(uuidList.size() > 0) {
							idto.setUuidList(uuidList);
							ObjectMapper jsonMapper = new ObjectMapper();
							String jsonInString = jsonMapper.writeValueAsString(idto);

							HttpHeaders httpHeaders = new HttpHeaders();
							httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
							httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			 				final String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/payments";
							Map<String, String> params = new HashMap<String, String>();
							HttpEntity<?> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);
							RestTemplate restTemplate = new RestTemplate();
							ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class, params);
							HttpStatus statusCode = responseEntity.getStatusCode();

							if (statusCode.value() == 200) {
								String body = responseEntity.getBody();
								if (body != null) {
									ObjectMapper mapper = new ObjectMapper();
									Receipt[] response = mapper.readValue(body, Receipt[].class);
									List<Receipt> objList = Arrays.asList(response);
									
									if (!objList.isEmpty()) {
	 
										DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
	 	 	 					        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
	 	 	 					        
	 	 	 					        for(FiscalDocuments fd : fdList) {
											for (Receipt o : objList) {
												o.setUuid(o.getUuid().trim());
												if(fd.getUuidFactura().equals(o.getUuid())) {
													fd.setPaymentReference(o.getPaymentReference());
													fd.setPaymentAmount(o.getPaymentAmount());
													fd.setPaymentDate(o.getPaymentDate());
													fd.setPaymentStatus(AppConstants.STATUS_GR_PAID);
													fd.setStatus(AppConstants.STATUS_PAID);
													fiscalDocumentService.updateDocument(fd);
												}
											}
	 	 	 					        }
										//purchaseOrderService.updatePaymentReceipts(objList);										
										
										for (Receipt o : objList) {										
											Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
											if(s!=null) {
			 	 								String emailRecipient = (s.getEmailSupplier());
			 	 	 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			 	 	 							String emailContent = AppConstants.EMAIL_PAYMENT_NO_OC_NOTIF_CONTENT;
			 	 	 							emailContent = emailContent.replace("_UUID_", o.getUuid());
												String strDate = dateFormat.format(o.getPaymentDate());
												emailContent = emailContent.replace("_DATE_", strDate);

			 	 	 							String currency = format.format(o.getPaymentAmount());
			 	 	 							emailContent = emailContent.replace("_AMOUNT_", currency);
			 	 	 							emailContent = emailContent.replace("_PID_", o.getPaymentReference());

			 	 	 							emailAsyncSup.setProperties(
			 	 	 									AppConstants.EMAIL_PAYMENT_RECEIPT_NOTIF,
			 	 	 									stringUtils.prepareEmailContent(emailContent + AppConstants.EMAIL_PORTAL_LINK),emailRecipient);
			 	 	 							emailAsyncSup.setMailSender(mailSenderObj);
			 	 	 							//emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
			 	 	 							Thread emailThreadSup = new Thread(emailAsyncSup);
			 	 	 							emailThreadSup.start();
			 								}
											
										}

										log4j.info("Fiscal Docs Guardados:" + response.length);
									}
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

	}

  	//@Scheduled(cron = "0 30 23 * * ?")
  	//@Scheduled(fixedDelay = 4200000, initialDelay = 30000)
	//@Scheduled(cron = "0 30 4 * * ?")
    //@Scheduled(cron = "0 0/3 * * * ?")
	public void getCustomBrokerPayments() {
		try {			
			for(int i=0; i<=10; i++) {
				int start = i*500;
				
				List<CustomBroker> fdList = customBrokerService.getCustomBroker("", AppConstants.STATUS_ACCEPT, "", "", start, 500);
				if (fdList != null) {
					if (fdList.size() > 0) {
						IntegerListDTO idto = new IntegerListDTO();
						List<String> uuidList = new ArrayList<String>();
						for(CustomBroker fd : fdList) {
							if(!uuidList.contains(fd.getUuidFactura())) {
								uuidList.add(fd.getUuidFactura());	
							}
						}
						
						if(uuidList.size() > 0) {
							idto.setUuidList(uuidList);
							ObjectMapper jsonMapper = new ObjectMapper();
							String jsonInString = jsonMapper.writeValueAsString(idto);

							HttpHeaders httpHeaders = new HttpHeaders();
							httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
							httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			 				final String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/payments";
							Map<String, String> params = new HashMap<String, String>();
							HttpEntity<?> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);
							RestTemplate restTemplate = new RestTemplate();
							ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class, params);
							HttpStatus statusCode = responseEntity.getStatusCode();

							if (statusCode.value() == 200) {
								String body = responseEntity.getBody();
								if (body != null) {
									ObjectMapper mapper = new ObjectMapper();
									Receipt[] response = mapper.readValue(body, Receipt[].class);
									List<Receipt> objList = Arrays.asList(response);
									
									if (!objList.isEmpty()) {
	 
										DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
	 	 	 					        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
	 	 	 					        
	 	 	 					        for(CustomBroker fd : fdList) {
											for (Receipt o : objList) {
												o.setUuid(o.getUuid().trim());
												if(fd.getUuidFactura().equals(o.getUuid())) {
													fd.setPaymentReference(o.getPaymentReference());
													fd.setPaymentAmount(o.getPaymentAmount());
													fd.setPaymentDate(o.getPaymentDate());
													fd.setPaymentStatus(AppConstants.STATUS_GR_PAID);
													fd.setStatus(AppConstants.STATUS_PAID);
													customBrokerService.updateDocument(fd);
												}
											}
	 	 	 					        }
										//purchaseOrderService.updatePaymentReceipts(objList);										
										
										for (Receipt o : objList) {										
											Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
											if(s!=null) {
			 	 								String emailRecipient = (s.getEmailSupplier());
			 	 	 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			 	 	 							String emailContent = AppConstants.EMAIL_PAYMENT_NO_OC_NOTIF_CONTENT;
			 	 	 							emailContent = emailContent.replace("_UUID_", o.getUuid());
												String strDate = dateFormat.format(o.getPaymentDate());
												emailContent = emailContent.replace("_DATE_", strDate);

			 	 	 							String currency = format.format(o.getPaymentAmount());
			 	 	 							emailContent = emailContent.replace("_AMOUNT_", currency);
			 	 	 							emailContent = emailContent.replace("_PID_", o.getPaymentReference());

			 	 	 							emailAsyncSup.setProperties(
			 	 	 									AppConstants.EMAIL_PAYMENT_RECEIPT_NOTIF,
			 	 	 									stringUtils.prepareEmailContent(emailContent + AppConstants.EMAIL_PORTAL_LINK),emailRecipient);
			 	 	 							emailAsyncSup.setMailSender(mailSenderObj);
			 	 	 							//emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
			 	 	 							Thread emailThreadSup = new Thread(emailAsyncSup);
			 	 	 							emailThreadSup.start();
			 								}
											
										}

										log4j.info("Fiscal Docs Guardados:" + response.length);
									}
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

	}
		
	public void getPurchaseOrderListBySelection(int orderNumber, String addressNumber, String fromDate, String toDate, String lttr, String nxtr) {
		this.getPurchaseOrderListByFilter(orderNumber, addressNumber, fromDate, toDate);
	}

	private void getPurchaseOrderListByFilter(int orderNumber, String addressNumber, String fromDate, String toDate) {
 		try {
 			
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		    MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
			map.add("addressNumber", addressNumber);
			map.add("orderNumber", String.valueOf(orderNumber));
			map.add("fromDate", fromDate);
			map.add("toDate", toDate);
			
			String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/poListBySelection";
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class);
			HttpStatus statusCode = responseEntity.getStatusCode();

			if (statusCode.value() == 200) {
				String body = responseEntity.getBody();
				if (body != null) {
					ObjectMapper mapper = new ObjectMapper();
					PurchaseOrder[] response = mapper.readValue(body, PurchaseOrder[].class);
					List<PurchaseOrder> objList = Arrays.asList(response);
					if (!objList.isEmpty()) {
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
							o.setOrderStauts(AppConstants.STATUS_OC_RECEIVED);
							Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
							if(s!=null) {
								o.setLongCompanyName(s.getName());
							}
						}
						purchaseOrderService.saveMultiple(objList);
					}
				}
			}
			getPurchaseReceiptListBySelection(orderNumber, addressNumber, fromDate, toDate);
 		} catch (Exception e) {
 			log4j.error("Exception" , e);
 			e.printStackTrace();
 		}
	}
	
	private void getPurchaseReceiptListBySelection(int orderNumber, String addressNumber, String fromDate, String toDate) {
		log4j.info("Carga recibos: " + new Date());
 		try {

 				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
 			    MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
 				map.add("addressNumber", addressNumber);
 				map.add("orderNumber", String.valueOf(orderNumber));
 				map.add("fromDate", fromDate);
 				map.add("toDate", toDate);
 				String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/poReceiptsBySelection";
				
				HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);
 				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class);
				HttpStatus statusCode = responseEntity.getStatusCode();
 				if (statusCode.value() == 200) {
 					String body = responseEntity.getBody();
 					if (body != null) {
 						ObjectMapper mapper = new ObjectMapper();
 						Receipt[] response = mapper.readValue(body, Receipt[].class);
 						List<Receipt> objList = Arrays.asList(response);
 						if (!objList.isEmpty()) {
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
 							purchaseOrderService.updateMultiple(updateList); 							
 						}
 					}
 				}
 				log4j.info("Procesados los recibos (manual)");
 		} catch (Exception e) {
 			log4j.error("Exception" , e);
 			e.printStackTrace();
 		}
 	}
	
	//@Scheduled(cron = "0 20 * * * ?")	
	//@Scheduled(fixedDelay = 9920000, initialDelay = 15000)
	//@Scheduled(cron = "0 0/30 * * * ?")
	//@Scheduled(cron = "0 0 7,10,13,17,20 * * ?")
 	public void getPurchaseOrderList() {
 		log4j.info("Carga órdenes: " + new Date());
 		try {
 			
			  for(int i=0; i<=800; i++) {
			  int start = i*3;
			  int limit = 3;
			  List<SupplierDTO> supDtoList = supplierService.getList(start, limit);
 			  String supList = "";
 			  if (!supDtoList.isEmpty()) {
					List<String> sList = new ArrayList<String>();
					for (SupplierDTO sdto : supDtoList) {
						if(sdto.getAddresNumber() != null && !"".equals(sdto.getAddresNumber())){
							sList.add(sdto.getAddresNumber().trim());
						}
					}
					String idList = sList.toString();
					supList = idList.substring(1, idList.length() - 1).replace(", ", ",");
 			  }
 			
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
 			    MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
				map.add("supList", supList);
				
				String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/poList";
				
				if(this.orderDate != null && !"".equals(this.orderDate)) {
					map.add("orderDate", this.orderDate);
					map.add("lttr", lttr);
					map.add("nxtr", nxtr);
					url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/poListBySelection";
				}
				
				HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);
	
				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class);
				HttpStatus statusCode = responseEntity.getStatusCode();

 				if (statusCode.value() == 200) {
 					String body = responseEntity.getBody();
 					if (body != null) {
 						ObjectMapper mapper = new ObjectMapper();
 						PurchaseOrder[] response = mapper.readValue(body, PurchaseOrder[].class);
 						List<PurchaseOrder> objList = Arrays.asList(response);
 						if (!objList.isEmpty()) {
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
 								o.setOrderStauts(AppConstants.STATUS_OC_RECEIVED);
 								Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
 								if(s!=null) {
 									o.setLongCompanyName(s.getName());
 								}
 							}
 							
 							List<PurchaseOrder> returnedList = purchaseOrderService.saveMultiple(objList);
// 							for(PurchaseOrder o : returnedList) {
// 								Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
// 								if(s!=null) {	
// 	 								String emailRecipient = (s.getEmailSupplier());
//	 	 							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
//	 	 							emailAsyncSup.setProperties(
//	 	 									AppConstants.EMAIL_NEW_ORDER_NOTIF + o.getOrderNumber() + "-" + o.getOrderType(),
//	 	 									stringUtils.prepareEmailContent(AppConstants.EMAIL_PURCHASE_NEW + o.getOrderNumber() + "-"
//	 	 											+ o.getOrderType() + "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK + "<br /><br />"
//	 	 											+ "Cuenta: " + o.getAddressNumber() + "<br /> Razon Social: " + s.getRazonSocial()),
//	 	 									emailRecipient + "," + o.getEmail());
//	 	 							emailAsyncSup.setMailSender(mailSenderObj);
//	 	 							emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
//	 	 							Thread emailThreadSup = new Thread(emailAsyncSup);
//	 	 							emailThreadSup.start();
// 								}
// 							}

 							log4j.info("Procesado:" + response.length);
 						}
 					}
 				}
 				
 				getPurchaseReceiptList(supList);
			  }
			  log4j.info("Termina la carga................");
 		} catch (Exception e) {
 			log4j.error("Exception" , e);
 			e.printStackTrace();
 		}
 	}
  
  
 	public void getPurchaseReceiptList(String supList) {
 		log4j.info("Carga recibos: " + new Date());
 		try {
 				
 				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
 			    MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
				map.add("supList", supList);
				
 				String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/poReceipts";
				
				if(this.orderDate != null && !"".equals(this.orderDate)) {
					map.add("orderDate", this.orderDate);
					map.add("lttr", lttr);
					map.add("nxtr", nxtr);
					url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/poReceiptsBySelection";
				}
				
				HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);
 				
 				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class);
				HttpStatus statusCode = responseEntity.getStatusCode();
 				
 				if (statusCode.value() == 200) {
 					String body = responseEntity.getBody();
 					if (body != null) {
 						ObjectMapper mapper = new ObjectMapper();
 						Receipt[] response = mapper.readValue(body, Receipt[].class);
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
 	 								String emailRecipient = (s.getEmailSupplier());
 	 								
 	 								String emailContent = AppConstants.EMAIL_MIDDLEWARE_MSG_1;
 	 								emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(o.getOrderNumber()));
 	 								emailContent = emailContent.replace("_DOCNUM_", String.valueOf(o.getDocumentNumber()));
 	 								emailContent = emailContent.replace("_ORDERTYPE_", o.getOrderType());
 	 								emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);

 	 								
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
 							
 							purchaseOrderService.updateMultiple(updateList); 							
 							log4j.info("Procesados los recibos");
 						}
 					}
 				}
 		} catch (Exception e) {
 			log4j.error("Exception" , e);
 			e.printStackTrace();
 		}
 	}
  
 	//@Scheduled(cron = "0 0 22 * * ?")
	//@Scheduled(fixedDelay = 9920000, initialDelay = 15000)
 	public void getPurchaseOrderListHistory() {
 		log4j.info("Carga órdenes Histórico: " + new Date());
 	 		try {
 	 			
 				  for(int i=0; i<=800; i++) {
 				  int start = i*3;
 				  int limit = 3;
 				  List<SupplierDTO> supDtoList = supplierService.getList(start, limit);
 	 			  String supList = "";
 	 			  if (!supDtoList.isEmpty()) {
 						List<String> sList = new ArrayList<String>();
 						for (SupplierDTO sdto : supDtoList) {
 							if(sdto.getAddresNumber() != null && !"".equals(sdto.getAddresNumber())){
 								sList.add(sdto.getAddresNumber().trim());
 							}
 						}
 						String idList = sList.toString();
 						supList = idList.substring(1, idList.length() - 1).replace(", ", ",");
 	 			  }
 	 			
 					HttpHeaders httpHeaders = new HttpHeaders();
 					httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
 	 			    MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
 					map.add("supList", supList);
 					String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/poListHistory";
 					HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);
 					RestTemplate restTemplate = new RestTemplate();
 					ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class);
 					HttpStatus statusCode = responseEntity.getStatusCode();

 	 				if (statusCode.value() == 200) {
 	 					String body = responseEntity.getBody();
 	 					if (body != null) {
 	 						ObjectMapper mapper = new ObjectMapper();
 	 						PurchaseOrder[] response = mapper.readValue(body, PurchaseOrder[].class);
 	 						List<PurchaseOrder> objList = Arrays.asList(response);
 	 						if (!objList.isEmpty()) {
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
 	 								o.setOrderStauts(AppConstants.STATUS_OC_RECEIVED);
 	 								Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
 	 								if(s!=null) {
 	 									o.setLongCompanyName(s.getName());
 	 								}
 	 							}
 	 							purchaseOrderService.saveMultiple(objList);
 	 						}
 	 					}
 	 				}
 	 				getPurchaseReceiptListHistory(supList);
 				  }
 				 log4j.info("Termina la carga................");
 	 		} catch (Exception e) {
 	 			log4j.error("Exception" , e);
 	 			e.printStackTrace();
 	 		}
 	 	}
 	  
 	  
 	 	public void getPurchaseReceiptListHistory(String supList) {
 	 		log4j.info("Carga recibos: " + new Date());
 	 		try {
 	 				
 	 				HttpHeaders httpHeaders = new HttpHeaders();
 					httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
 	 			    MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
 					map.add("supList", supList);
 					
 	 				String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfraProd/poReceiptsHistory";
 					HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);
 	 				
 	 				RestTemplate restTemplate = new RestTemplate();
 					ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class);
 					HttpStatus statusCode = responseEntity.getStatusCode();
 	 				
 	 				if (statusCode.value() == 200) {
 	 					String body = responseEntity.getBody();
 	 					if (body != null) {
 	 						ObjectMapper mapper = new ObjectMapper();
 	 						Receipt[] response = mapper.readValue(body, Receipt[].class);
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
 	 	 								String emailRecipient = (s.getEmailSupplier());
 	 	 								
 	 	 								String emailContent = AppConstants.EMAIL_MIDDLEWARE_MSG_1;
 	 	 								emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(o.getOrderNumber()));
 	 	 								emailContent = emailContent.replace("_DOCNUM_", String.valueOf(o.getDocumentNumber()));
 	 	 								emailContent = emailContent.replace("_ORDERTYPE_", o.getOrderType());
 	 	 								emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);

 	 	 								
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
 	 							
 	 							purchaseOrderService.updateMultiple(updateList); 							
 	 							log4j.info("Procesados los recibos");
 	 						}
 	 					}
 	 				}
 	 		} catch (Exception e) {
 	 			log4j.error("Exception" , e);
 	 			e.printStackTrace();
 	 		}
 	 	}
 	 	
  public void getTolerances() {
	  log4j.info("Inicio carga tolerancias:");
    try {
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.set("Accept", "application/json");
      String url = "http://localhost:8081/supplierWebPortalRest/toList";
      Map<String, String> params = new HashMap<>();
      HttpEntity<?> httpEntity = new HttpEntity((MultiValueMap)httpHeaders);
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8081/supplierWebPortalRest/toList", HttpMethod.GET, httpEntity, String.class, 
          params);
      HttpStatus statusCode = responseEntity.getStatusCode();
      if (statusCode.value() == 200) {
        String body = (String)responseEntity.getBody();
        if (body != null) {
          ObjectMapper mapper = new ObjectMapper();
          Tolerances[] response = (Tolerances[])mapper.readValue(body, Tolerances[].class);
          List<Tolerances> objList = Arrays.asList(response);
          if (!objList.isEmpty()) {
            this.tolerancesService.deleteRecords();
            this.tolerancesService.saveMultiple(objList);
            log4j.info("Guardado:" + response.length);
          } 
        } 
      } 
    } catch (Exception e) {
    	log4j.error("Exception" , e);
      e.printStackTrace();
    } 
  }
  
  

	
  
  public boolean sendOrderInvoiceConfirmation(List<PurchaseOrderDTO> poList) {
    try {
      if (poList.size() > 0) {
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonInString = jsonMapper.writeValueAsString(poList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", "application/json");
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8081/supplierWebPortalRestCryoInfra/orderInvoiceConfirm";
        Map<String, String> params = new HashMap<>();
        HttpEntity<?> httpEntity = new HttpEntity(jsonInString, (MultiValueMap)httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8081/supplierWebPortalRestCryoInfra/orderInvoiceConfirm", HttpMethod.POST, httpEntity, 
            String.class, params);
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode.value() == 200) {
          String body = (String)responseEntity.getBody();
          if (body != null) {
            ObjectMapper mapper = new ObjectMapper();
            PurchaseOrderDTO[] response = (PurchaseOrderDTO[])mapper.readValue(body, PurchaseOrderDTO[].class);
            List<PurchaseOrderDTO> objList = Arrays.asList(response);
            if (!objList.isEmpty())
            	log4j.info("Guardado:" + response.length); 
            return true;
          } 
        } 
      } 
    } catch (Exception e) {
    	log4j.error("Exception" , e);
      e.printStackTrace();
      return false;
    } 
    return false;
  }
  
  public void sendReceivingAdvice(ReceivingAdviceHeaderDTO o) {
    SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
    try {
      if (o != null) {
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonInString = jsonMapper.writeValueAsString(o);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", "application/json");
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8081/supplierWebPortalRestCryoInfra/postReceivingAdvice";
        Map<String, String> params = new HashMap<>();
        HttpEntity<?> httpEntity = new HttpEntity(jsonInString, (MultiValueMap)httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8081/supplierWebPortalRestCryoInfra/postReceivingAdvice", HttpMethod.POST, httpEntity, 
            String.class, params);
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode.value() == 200) {
          String body = (String)responseEntity.getBody();
          ObjectMapper mapper = new ObjectMapper();
          ReceivingAdviceHeaderDTO response = (ReceivingAdviceHeaderDTO)mapper.readValue(body, ReceivingAdviceHeaderDTO.class);
          log4j.info("Guardado:" + response.getSYEDOC());
        } 
      } 
    } catch (Exception e) {
    	log4j.error("Exception" , e);	
      e.printStackTrace();
    } 
  }
  
  @SuppressWarnings("unused")
	public String sendVoucher(VoucherHeaderDTO o) {
		final SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
		String resp = "";

		String jsonInString="";
	    String url="";
		try {
			if (o != null) {
				ObjectMapper jsonMapper = new ObjectMapper();
				 jsonInString = jsonMapper.writeValueAsString(o);

				 log4j.info(jsonInString);

				
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/postVoucher";
				Map<String, String> params = new HashMap<String, String>();
				HttpEntity<?> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);
				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
						String.class, params);
				HttpStatus statusCode = responseEntity.getStatusCode();

				if (statusCode.value() == 200) {
					String body = responseEntity.getBody();
					ObjectMapper mapper = new ObjectMapper();
					VoucherHeaderDTO response = mapper.readValue(body, VoucherHeaderDTO.class);
					log4j.info("Guardado:" + response.getSYEDOC());
					resp = String.valueOf(response.getSYEDOC());
				}
				else {
					loggerJEdwars.putInitial(url, jsonInString, "Error estatus :"+statusCode.value()+" >>> "+responseEntity.getBody(), AppConstants.LOGGER_JEDWARS_ERROR);			

				}
			}
			return resp;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			loggerJEdwars.putInitial(url, jsonInString, "Error :"+e.getMessage()+">>>"+ StringUtils.getString(e), AppConstants.LOGGER_JEDWARS_ERROR);

			e.printStackTrace();
			return null;
		}

	}

  public String sendJournalEntries(BatchJournalDTO o) {
    String resp = "";

	String jsonInString="";
    String url="";
    try {
		if (o != null) {
			ObjectMapper jsonMapper = new ObjectMapper();
			 jsonInString = jsonMapper.writeValueAsString(o);
			 log4j.info(jsonInString);
			
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			 url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/postJournalEntries";
			Map<String, String> params = new HashMap<String, String>();
			HttpEntity<?> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					String.class, params);
			HttpStatus statusCode = responseEntity.getStatusCode();

			if (statusCode.value() == 200) {
				String body = responseEntity.getBody();
				ObjectMapper mapper = new ObjectMapper();
				BatchJournalDTO response = mapper.readValue(body, BatchJournalDTO.class);
				log4j.info("Guardado:" + response.getJournalEntries().get(0).getVNEDBT());
				resp = String.valueOf(response.getJournalEntries().get(0).getVNEDBT());
			}
			else {
				loggerJEdwars.putInitial(url, jsonInString, "Error estatus :"+statusCode.value()+" >>> "+responseEntity.getBody(), AppConstants.LOGGER_JEDWARS_ERROR);			

			}
		}
		return resp;
      } catch (Exception e) {
    	  log4j.error("Exception" , e);
			loggerJEdwars.putInitial(url, jsonInString, "Error :"+e.getMessage()+">>>"+ StringUtils.getString(e), AppConstants.LOGGER_JEDWARS_ERROR);

          e.printStackTrace();
          return null;
      }
  }
  
  public SupplierJdeDTO sendAddressBook(SupplierJdeDTO o) {
	  final SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
		String resp = "";
		try {
			if (o != null) {
				ObjectMapper jsonMapper = new ObjectMapper();
				String jsonInString = jsonMapper.writeValueAsString(o);
				log4j.info(jsonInString);

				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				final String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/postAddressBook";
				Map<String, String> params = new HashMap<String, String>();
				HttpEntity<?> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
				
				ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
						String.class, params);
				HttpStatus statusCode = responseEntity.getStatusCode();

				if (statusCode.value() == 200) {
					String body = responseEntity.getBody();
					ObjectMapper mapper = new ObjectMapper();
					SupplierJdeDTO response = mapper.readValue(body, SupplierJdeDTO.class);
					log4j.info("Guardado:" + response.getAddresNumber());
					return response;
				}else {
					return null;
				}
			}
			return null;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}
  }
  
  public void sendTestEmail() {
	log4j.info("Inicio prueba email:");
    List<PurchaseOrderPayment> all = this.purchaseOrderService.getAll();
    for (PurchaseOrderPayment o : all)
      this.emailService.sendEmailPagos("TEST", "PRUEBA", "javila@smartech.com.mx", o); 
  }
  
  public String sendHttpRequest(String url, String jsonInString, HttpMethod method) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Accept", "application/json");
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.setAcceptCharset(Arrays.asList(new Charset[] { Charset.forName("UTF-8") }));
    Map<String, String> params = new HashMap<>();
    HttpEntity<?> httpEntity = null;
    if ("".equals(jsonInString)) {
      httpEntity = new HttpEntity((MultiValueMap)httpHeaders);
    } else {
      httpEntity = new HttpEntity(jsonInString, (MultiValueMap)httpHeaders);
    } 
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, httpEntity, String.class, params);
    HttpStatus statusCode = responseEntity.getStatusCode();
    if (statusCode.value() == 200)
      return (String)responseEntity.getBody(); 
    return null;
  }
  
  public void getNewAddressBook() {
	  log4j.info("Inicio carga nuevos Proveedores:" + new Date());
    try {
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.set("Accept", "application/json");
      String url = "http://localhost:8081/supplierWebPortalRestCryoInfra/newAddressBook";
      Map<String, String> params = new HashMap<>();
      HttpEntity<?> httpEntity = new HttpEntity((MultiValueMap)httpHeaders);
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8081/supplierWebPortalRestCryoInfra/newAddressBook", HttpMethod.GET, httpEntity, String.class, 
          params);
      HttpStatus statusCode = responseEntity.getStatusCode();
      if (statusCode.value() == 200) {
        String body = (String)responseEntity.getBody();
        if (body != null) {
          ObjectMapper mapper = new ObjectMapper();
          AddressBookLayot[] response = (AddressBookLayot[])mapper.readValue(body, AddressBookLayot[].class);
          List<AddressBookLayot> objList = Arrays.asList(response);
          List<Supplier> saveList = new ArrayList<>();
          List<Users> users = new ArrayList<>();
          UDC userRole = this.udcService.searchBySystemAndKey("ROLES", "SUPPLIER");
          UDC userType = this.udcService.searchBySystemAndKey("USERTYPE", "SUPPLIER");
          List<String> updateList = new ArrayList<>();
          if (!objList.isEmpty()) {
            for (AddressBookLayot o : objList) {
              Supplier s = null;
              String supplierAddressNumber = String.valueOf(o.getABAN8());
              Supplier currentSup = this.supplierService.searchByAddressNumber(supplierAddressNumber);
              if (currentSup != null) {
                s = currentSup;
                s.setApprovalNotes("ACTUALIZACION");
              } else {
                s = new Supplier();
                s.setApprovalNotes("REPLICACION");
              } 
              s.setAddresNumber(String.valueOf(o.getABAN8()));
              s.setName(o.getABALPH().trim());
              s.setRazonSocial(o.getABALPH().trim());
              s.setCalleNumero(o.getALADD1().trim());
              s.setCodigoPostal(o.getALADDZ().trim());
              s.setColonia(o.getALADD2().trim());
              s.setDelegacionMnicipio(o.getALADD3().trim());
              s.setEstado(o.getALADD4().trim());
              s.setCountry(o.getALCTR().trim());
              s.setCategoriaJDE(o.getABAT1().trim());
              s.setCategorias(o.getA6APC().trim());
              s.setEmailComprador(o.getPPANBY().trim());
              s.setCurrencyCode(o.getA6CRRP().trim());
              s.setCuentaClabe(o.getAYRLN().trim());
              s.setDiasCredito(o.getA6TRAP().trim());
              String tps = o.getABAC12().trim();
              s.setEmailContactoCalidad(o.getWWMLN7().trim());
              s.setEmailContactoCxC(o.getWWMLN5().trim());
              s.setEmailContactoVentas(o.getWWMLN3().trim());
              s.setEmailContactoPedidos(o.getWWMLN1().trim());
              if ("PUB".equals(tps) || "ROY".equals(tps)) {
                if (o.getWWMLN5() != null && !"".equals(o.getWWMLN5().trim()))
                  s.setEmailContactoPedidos(String.valueOf(s.getEmailSupplier()) + "," + o.getWWMLN5().trim()); 
                if (o.getWWMLN3() != null && !"".equals(o.getWWMLN3().trim()))
                  s.setEmailContactoPedidos(String.valueOf(s.getEmailSupplier()) + "," + o.getWWMLN3().trim()); 
                if (o.getWWMLN7() != null && !"".equals(o.getWWMLN7().trim()))
                  s.setEmailContactoPedidos(String.valueOf(s.getEmailSupplier()) + "," + o.getWWMLN7().trim()); 
              } 
              s.setFormaPago(o.getA6PYIN().trim());
              s.setNombreBanco(o.getAYSWFT().trim());
              s.setNombreContactoCalidad(o.getWWMLN8().trim());
              s.setNombreContactoCxC(o.getWWMLN6());
              s.setNombreContactoPedidos(o.getWWMLN2().trim());
              s.setNombreContactoVentas(o.getWWMLN4().trim());
              s.setFisicaMoral(o.getABTAXC().trim());
              s.setRfc(o.getABTAX().trim());
              s.setTaxId(o.getABTX2().trim());
              s.setTaxRate(o.getA6TXA2().trim());
              s.setExplCode1(o.getA6EXR2().trim());
              s.setTasaIva(String.valueOf(o.getPPIVA()).trim());
              s.setTelefonoContactoCalidad(o.getWPPH4().trim());
              s.setTelefonoContactoCxC(o.getWPPH3());
              s.setTelefonoContactoPedidos(o.getWPPH1());
              s.setTelefonoContactoVentas(o.getWPPH2());
              s.setPaymentMethod(o.getPPPYMT().trim());
              s.setTipoProductoServicio(o.getABAC12().trim());
              s.setEmail(o.getWWMLN1().trim());
              s.setAcceptOpenOrder(false);
              s.setApprovalStatus("APROBADO");
              s.setApprovalStep("");
              s.setAutomaticEmail("");
              s.setCuentaBancaria("");
              s.setCurrentApprover("");
              s.setDiasCreditoActual("");
              s.setDiasCreditoAnterior("");
              s.setDireccionCentroDistribucion("");
              s.setDireccionPlanta("");
              s.setFechaAprobacion(new Date());
              s.setFechaSolicitud(new Date());
              s.setFileList("");
              s.setInvException("N");
              s.setNextApprover("");
              s.setObservaciones("");
              s.setPuestoCalidad("");
              s.setRegiones("");
              s.setRejectNotes("");
              s.setRiesgoCategoria("");
              s.setSteps(0);
              s.setTicketId(Long.valueOf(0L));
              s.setTipoMovimiento("A");
              s.setSupplierType("");
              s.setCompradorAsignado("");
              s.setDiasCreditoActual("0");
              s.setDiasCreditoAnterior("0");
              if (currentSup != null) {
                Users usr = this.usersService.getByUserName(supplierAddressNumber);
                if (usr != null) {
                  if (s.getCategoriaJDE().contains("X")) {
                    usr.setEnabled(false);
                  } else {
                    usr.setEnabled(true);
                  } 
                  usr.setEmail(s.getEmail());
                  usr.setAddressNumber(s.getAddresNumber());
                  usr.setSupplier(true);
                  usr.setMainSupplierUser(true);
                  users.add(usr);
                } 
              } else {
                Users usr = new Users();
                usr.setId(0);
                usr.setUserName(s.getAddresNumber());
                usr.setAddressNumber(s.getAddresNumber());
                usr.setSupplier(true);
                usr.setMainSupplierUser(true);
                usr.setEnabled(true);
                usr.setEmail(s.getEmail());
                usr.setName(s.getName());
                usr.setRole(userRole.getStrValue1());
                usr.setUserRole(userRole);
                usr.setUserType(userType);
                String tempPass = getAlphaNumericString(8);
                String encodePass = Base64.getEncoder().encodeToString(tempPass.trim().getBytes());
                encodePass = "==a20$" + encodePass;
                usr.setPassword(encodePass);
                users.add(usr);
              } 
              saveList.add(s);
              updateList.add(String.valueOf(o.getABAN8()));
            } 
            this.supplierService.saveSuppliers(saveList);
            for (Users s : users) {
              String emailRecipient = s.getEmail();
              if (s.getId() == 0) {
                String pass = s.getPassword();
                pass = pass.replace("==a20$", "");
                byte[] decodedBytes = Base64.getDecoder().decode(pass);
                String decodedPass = new String(decodedBytes);
                String credentials = "Usuario: " + s.getUserName() + "<br />Contrase" + decodedPass + "<br />&nbsp; url: " + "http://localhost:8081/supplierWebPortalCryoInfra/";
                EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
                emailAsyncSup.setProperties(AppConstants.EMAIL_INVOICE_SUBJECT, 
                this.stringUtils.prepareEmailContent(AppConstants.EMAIL_MASS_SUPPLIER_NOTIFICATION + credentials), emailRecipient);
                emailAsyncSup.setMailSender(this.mailSenderObj);
                Thread emailThreadSup = new Thread(emailAsyncSup);
                emailThreadSup.start();
              } 
            } 
            this.usersService.saveUsersList(users);
            if (updateList.size() > 0) {
              ObjectMapper jsonMapper = new ObjectMapper();
              String jsonInString = jsonMapper.writeValueAsString(updateList);
              String resp = sendHttpRequest("http://localhost:8081/supplierWebPortalRestCryoInfra/updateNewAddressBook", jsonInString, HttpMethod.POST);
              log4j.info("Actualizado:" + resp);
            } 
          } 
        } 
      } 
    } catch (Exception e) {
    	log4j.error("Exception" , e);
      e.printStackTrace();
    } 
  }
  
  public int getAddressBookNextNumber() {
	  log4j.info("Busca addressBook:");
    try {
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.set("Accept", "application/json");
      String url = "http://localhost:8081/supplierWebPortalRestCryoInfra/getAddressBookNextNumber";
      Map<String, String> params = new HashMap<>();
      HttpEntity<?> httpEntity = new HttpEntity((MultiValueMap)httpHeaders);
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8081/supplierWebPortalRestCryoInfra/getAddressBookNextNumber", HttpMethod.GET, httpEntity, String.class, 
          params);
      HttpStatus statusCode = responseEntity.getStatusCode();
      if (statusCode.value() == 200) {
        String body = (String)responseEntity.getBody();
        if (body != null) {
          ObjectMapper mapper = new ObjectMapper();
          Integer result = (Integer)mapper.readValue(body, Integer.class);
          if (result != null)
            return 1000; 
        } 
      } 
    } catch (Exception e) {
    	log4j.error("Exception" , e);
      e.printStackTrace();
    } 
    return 0;
  }
  
  
  private String getAlphaNumericString(int n) {
    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      int index = 
        (int)(AlphaNumericString.length() * 
        Math.random());
      sb.append(AlphaNumericString
          .charAt(index));
    } 
    return sb.toString();
  }
  
	//@Scheduled(cron = "0 30 23 * * ?")
	//@Scheduled(fixedDelay = 4200000, initialDelay = 10000)
	//@Scheduled(cron = "0 30 4 * * ?") ACTIVO
    //@Scheduled(cron = "0 0/3 * * * ?")
	public void getDisableOrderReceipts() {
		try {
			boolean isProcessOk = true;
			List<Receipt> allReceiptList = new ArrayList<Receipt>();
			List<Receipt> allJdeReceiptList = new ArrayList<Receipt>();
			log4j.info("DESHABILITA RECIBOS...");
			
			  for(int i=0; i<=10; i++) {
				  int start = i*500;
				  List<Receipt> receiptList = purchaseOrderService.getOpenOrderReceipts(start, 500);
				  
					if (receiptList != null) {
						if (!receiptList.isEmpty()) {
							allReceiptList.addAll(receiptList);
							
							List<String> coList = new ArrayList<String>();
							List<String> supList = new ArrayList<String>();
							List<String> poList = new ArrayList<String>();
							List<String> otList = new ArrayList<String>();
							List<String> rList = new ArrayList<String>();
							List<String> rtList = new ArrayList<String>();
							
							for(Receipt r : receiptList) {
								String company = "'"+ r.getOrderCompany().trim() + "'";
								String supplier = r.getAddressNumber().trim();
								String orderNumber = String.valueOf(r.getOrderNumber());
								String orderType = "'" + r.getOrderType().trim() + "'";
								String receiptNumber = String.valueOf(r.getDocumentNumber());
								String receiptType = "'" + r.getDocumentType() + "'";
								
								if(!coList.contains(company)) {
									coList.add(company);
								}

								if(!supList.contains(supplier)) {
									supList.add(supplier);
								}
								
								if(!poList.contains(orderNumber)) {
									poList.add(orderNumber);
								}
								
								if(!otList.contains(orderType)) {
									otList.add(orderType);
								}
								
								if(!rList.contains(receiptNumber)) {
									rList.add(receiptNumber);
								}
								
								if(!rtList.contains(receiptType)) {
									rtList.add(receiptType);
								}
							}
							
							String coStringList = String.join(",", coList);
							String supStringList = String.join(",", supList);
							String poStringList = String.join(",", poList);
							String otStringList = String.join(",", otList);
							String rStringList = String.join(",", rList);
							String rtStringList = String.join(",", rtList);

			 				HttpHeaders httpHeaders = new HttpHeaders();
							httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			 			    MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
			 			    map.add("coList", coStringList);
							map.add("supList", supStringList);
							map.add("poList", poStringList);
							map.add("otList", otStringList);
							map.add("rList", rStringList);
							map.add("rtList", rtStringList);
							
			 				String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/poEnableReceipts";						
							HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);			 				
			 				RestTemplate restTemplate = new RestTemplate();
							ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class);
							HttpStatus statusCode = responseEntity.getStatusCode();

			 				if (statusCode.value() == 200) {
			 					String body = responseEntity.getBody();
			 					if (body != null) {
			 						ObjectMapper mapper = new ObjectMapper();
			 						Receipt[] response = mapper.readValue(body, Receipt[].class);
			 						List<Receipt> objList = Arrays.asList(response);

			 						if (objList != null && !objList.isEmpty()) {			 							
			 							allJdeReceiptList.addAll(objList);
			 						}
			 					}
			 				} else {
			 					log4j.info("CONSULTA NO EXITOSA.");
			 					isProcessOk = false;
			 					break;
			 				}
						}
					}
			  }
			  
			  log4j.info("REGISTROS PORTAL:" + allReceiptList.size());
			  log4j.info("REGISTROS JDE:" + allJdeReceiptList.size());
			  
				if(isProcessOk) {
					List<Receipt> notFoundList = new ArrayList<Receipt>();					
					for(Receipt receipt : allReceiptList) {
						boolean isReceiptExists = false;
						for(Receipt receiptJDE : allJdeReceiptList) {
							if(receipt.getOrderCompany().trim().equals(receiptJDE.getOrderCompany().trim()) 
									&& receipt.getAddressNumber().trim().equals(receiptJDE.getAddressNumber().trim())
									&& receipt.getOrderNumber() == receiptJDE.getOrderNumber()
									&& receipt.getOrderType().trim().equals(receiptJDE.getOrderType().trim())
									&& receipt.getDocumentNumber() == receiptJDE.getDocumentNumber()
									&& receipt.getDocumentType().trim().equals(receiptJDE.getDocumentType().trim())) {
								isReceiptExists = true;
								break;
							}
						}
						
						if(!isReceiptExists) {
							receipt.setStatus(AppConstants.STATUS_OC_CANCEL);
							notFoundList.add(receipt);
						}
					}
					
					if(!notFoundList.isEmpty()) {
						purchaseOrderService.updateReceipts(notFoundList);		 							 							
						log4j.info("Recibos deshabilitados:" + notFoundList.size());
					}	
				}

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}
 	
	//@Scheduled(cron = "0 30 23 * * ?")
	//@Scheduled(fixedDelay = 4200000, initialDelay = 10000)
	//@Scheduled(cron = "0 30 7 * * ?") ACTIVO
    //@Scheduled(cron = "0 0/3 * * * ?")
	public void getUpdateExchangeRate() {
		try {
			log4j.info("ACTUALIZACIÓN DE TIPO DE CAMBIO...");
			String currencyCode = "";
			int startDate = 0;
			
			UDC erUDC = udcDao.searchBySystemAndKey("SCHEDULER", "EXCHANGERATE");			
			if(erUDC != null) {
				currencyCode = erUDC.getStrValue1();
				if(erUDC.getDateValue() != null) {	
					try {
						startDate = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(erUDC.getDateValue()));
					} catch (Exception e) {
						log4j.error("Exception" , e);
						e.printStackTrace();						
					}					
				}
				
				if(startDate == 0) {
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					c.add(Calendar.DATE, -1);
					startDate = Integer.valueOf(JdeJavaJulianDateTools.Methods.getJulianDate(c.getTime()));
				}
				
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			    MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
			    map.add("currencyCode", currencyCode);
				map.add("startDate", String.valueOf(startDate));
				
				String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/getExchangeRate";						
				HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);			 				
				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class);
				HttpStatus statusCode = responseEntity.getStatusCode();

				if (statusCode.value() == 200) {
					String body = responseEntity.getBody();
					if (body != null) {
						ObjectMapper mapper = new ObjectMapper();
						ExchangeRate[] response = mapper.readValue(body, ExchangeRate[].class);
						List<ExchangeRate> objList = Arrays.asList(response);

						if (objList != null && !objList.isEmpty()) {
							List<ExchangeRate> updatedRecords = exchangeRateService.saveMultipleExchangeRate(objList);
							if(updatedRecords != null) {
								log4j.info("Nuevos registros Tipo de Cambio:" + updatedRecords.size());
							}							
						}
					}
					
					erUDC.setDateValue(new Date());
					udcService.update(erUDC, new Date(), "system");
				} else {
					log4j.info("TIPO DE CAMBIO - CONSULTA NO EXITOSA.");
				}
			}
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}
	
    //@Scheduled(fixedDelay = 6000000, initialDelay = 10000)
	public void restoreInvoice() {
		List<InvoiceRequestDTO> list = new ArrayList<InvoiceRequestDTO>();
		InvoiceRequestDTO dto = null;
		String[] array = new String[]{};
//		String[] array = new String[]{
//		"84577,	OP,	2157619, 3036B345-A840-4DED-B33B-6BC2DF7E0D68"};		
//				"31926,	OP,	1987276, 73a93d9f-bf2d-453e-a019-ce237119298b",
//				"31926,	OP,	1987276, 73101C31-EB4A-463A-938D-BB1CF7F5CAA3",
//				"32173,	OP,	1987276, 8C681CEB-ABE9-4F92-81A3-B45F41A280FD",
//				"31681,	OP,	1987276, ba156a5f-7d15-4a02-add1-1b8d98da5a35"};		

		for(String record : array) {
			dto = new InvoiceRequestDTO();
			String[] items = record.split(",");
			dto.setDocumentNumber(Integer.valueOf(items[0].trim()).intValue());
			dto.setDocumentType(items[1].trim());
			dto.setAddressBook(items[2].trim());
			dto.setUuid(items[3].trim());
			list.add(dto);
		}

		documentsService.restoreInvoice(list);
	}
	
    //@Scheduled(fixedDelay = 6000000, initialDelay = 10000)
	public void restoreForeignInvoice() {
		List<InvoiceRequestDTO> list = new ArrayList<InvoiceRequestDTO>();
		InvoiceRequestDTO dto = null;
		String[] array = new String[]{};		
//		String[] array = new String[]{"8929,OP, 2193585,02193585-2020-1105-2152-0322200008929"};

		for(String record : array) {
			dto = new InvoiceRequestDTO();
			String[] items = record.split(",");
			dto.setDocumentNumber(Integer.valueOf(items[0].trim()).intValue());
			dto.setDocumentType(items[1].trim());
			dto.setAddressBook(items[2].trim());
			dto.setUuid(items[3].trim());
			list.add(dto);
		}

		documentsService.restoreForeignInvoice(list);
	}
	
//	@Scheduled(cron = "0 30 4 * * ?")
	public String reloadJde() {
		JSONObject json = new JSONObject();
		try{
        	List<LogDataJEdwars> logs=loggerJEdwars.getLogDataToSend();
        	for (LogDataJEdwars logDataJEdwars : logs) {
        		sendJournalEntriesReload(logDataJEdwars);
			}
        
        }catch(Exception e){
        	log4j.error("Exception" , e);
        	e.printStackTrace();
        	json.put("success", true);
            json.put("message", "Ha ocurrido un error inesperado: " +e.getMessage());
        }
		return json.toString();
	}
	
	 @SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	  public String sendJournalEntriesReload(LogDataJEdwars o) {
	      SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
	      String resp = "";
	      String jsonInString="";
	      String url="";
	      try {
	        if (o != null) {
	           jsonInString = o.getDataSend();
	          HttpHeaders httpHeaders = new HttpHeaders();
	          httpHeaders.set("Accept",MediaType.APPLICATION_JSON_VALUE);
	          httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	          url = o.getUrl();
	          Map<String, String> params = new HashMap<>();
	          HttpEntity<?> httpEntity = new HttpEntity(jsonInString, (MultiValueMap)httpHeaders);
	          RestTemplate restTemplate = new RestTemplate();
	          ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,  String.class, params);
	          HttpStatus statusCode = responseEntity.getStatusCode();
	          if (statusCode.value() == 200) {	          	
	            String body = (String)responseEntity.getBody();
	            ObjectMapper mapper = new ObjectMapper();
//	            BatchJournalDTO response = (BatchJournalDTO)mapper.readValue(body, BatchJournalDTO.class);
	            log4j.info("Guardado:");
	            o.setStatus(AppConstants.LOGGER_JEDWARS_SEND);
		    	  o.setMesage(body);
		      	loggerJEdwars.putUpdate(o);
	          } 
	        } 
	        return resp;
	      } catch (Exception e) {
//	      	logger.log(AppConstants.LOGGER_JEDWARS, "Error :"+e.getMessage()+">>>"+ StringUtils.getString(e));
	    	  log4j.error("Exception" , e);
	    	  o.setStatus(AppConstants.LOGGER_JEDWARS_ERROR);
	    	  o.setMesage( "Error :"+e.getMessage()+">>>"+ StringUtils.getString(e));
	      	loggerJEdwars.putUpdate(o);
	        e.printStackTrace();
	        return null;
	      } 
	    }
	 
	 
	 
	 @SuppressWarnings({ "unused", "unchecked" })
	public List<String> getListFactJde(String uuidList) {
		
			try {
				
					ObjectMapper jsonMapper = new ObjectMapper();
			        String jsonInString = jsonMapper.writeValueAsString(uuidList.split(","));

					HttpHeaders httpHeaders = new HttpHeaders();
					httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
					httpHeaders.setContentType(MediaType.APPLICATION_JSON);
					final String url = AppConstants.URL_HOST + "/supplierWebPortalRestSaavi/getInvoiceByList";
					Map<String, String> params = new HashMap<String, String>();
					HttpEntity<?> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);
					RestTemplate restTemplate = new RestTemplate();
					restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
					
					ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
							String.class, params);
					HttpStatus statusCode = responseEntity.getStatusCode();

					if (statusCode.value() == 200) {
						String body = responseEntity.getBody();
						ObjectMapper mapper = new ObjectMapper();
						List<String> response = mapper.readValue(body, List.class);
						
						return response;
					}else {
						return null;
					}
				
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return null;
			}
	  }
	 
	 
}
