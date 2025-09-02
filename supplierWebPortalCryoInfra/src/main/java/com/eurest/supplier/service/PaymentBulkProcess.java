package com.eurest.supplier.service;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.PurchaseOrderPayment;
import com.eurest.supplier.model.PurchaseOrderPaymentDetails;
import com.eurest.supplier.util.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PaymentBulkProcess implements Runnable{
	
	List<PurchaseOrderPayment> paymentList;
	List<PurchaseOrder> poList;
	PurchaseOrderService purchaseOrderService;
	JavaMailSender mailSenderObj;
	VelocityEngine velocityEngine;
	
	String additionalRecipient = "";
	
	Logger log4j = Logger.getLogger(PaymentBulkProcess.class);
	

	@Override
	public void run() {

		Map<String, PurchaseOrder> poMap = new HashMap<String, PurchaseOrder>();
		List<PurchaseOrder> validPo = null;
		List<Integer> validDocm = new ArrayList<Integer>();
		List<PurchaseOrderPayment> validPaymentList = new ArrayList<PurchaseOrderPayment>();
		
		for(PurchaseOrder po : poList) {
			String key = po.getAddressNumber() + "_" + po.getOrderNumber() + "_" + po.getOrderType();
			poMap.put(key,  po);
		}

		validPo = new ArrayList<PurchaseOrder>();
		for (PurchaseOrderPayment o : paymentList) {
			String supplierEmail = "";
			String purchaseEmail = "";
			boolean validRecord = true;
			Set<PurchaseOrderPaymentDetails> dtl = o.getPurchaseOrderPaymentDetails();
			for(PurchaseOrderPaymentDetails d : dtl) {
				String paymentKey = d.getAddressNumber() + "_" + d.getOrderNumber() + "_" + d.getOrderType();
				PurchaseOrder p = poMap.get(paymentKey);
				if(p != null) {
					validRecord = true;
					supplierEmail = p.getSupplierEmail().trim();
					purchaseEmail = p.getEmail().trim();
				}else {
					validRecord = false;
					supplierEmail = "";
					continue;
				}
			}
			
			if(validRecord) {
				validDocm.add(o.getPaymentDocument());
				o.setSupplierEmail(supplierEmail + "_" + purchaseEmail);
				validPaymentList.add(o);
				for(PurchaseOrderPaymentDetails d : dtl) {
					String paymentKey = d.getAddressNumber() + "_" + d.getOrderNumber() + "_" + d.getOrderType();
					PurchaseOrder p = poMap.get(paymentKey);
					if(p!= null) {
						p.setRelatedStatus(AppConstants.STATUS_COMPLETE);
						p.setOrderStauts(AppConstants.STATUS_OC_PAID);
						p.setStatus(AppConstants.STATUS_OC_PAID);
						p.setRemark(o.getDate().trim());
						validPo.add(p);
					}
				}
			}
		}
		
		if(validDocm.size() > 0) {
			ObjectMapper jsonMapper = new ObjectMapper();
			String jsonInString;
			try {
				jsonInString = jsonMapper.writeValueAsString(validDocm);
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				final String url = AppConstants.URL_HOST + "/supplierWebPortalRestUniversal/updatePayments";
				Map<String, String> params = new HashMap<String, String>();
				HttpEntity<?> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);
				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class, params);
				HttpStatus statusCode = responseEntity.getStatusCode();
				
				if (statusCode.value() == 200) {
					for (PurchaseOrderPayment o : validPaymentList) {
						
						String emailRecipient ="";
						String purchaseRecipient ="";
						
						String[] emailList = o.getSupplierEmail().split("_");
						if(emailList.length == 2) {
							emailRecipient = emailList[0];
							purchaseRecipient = emailList[1];
						}

						Template template = velocityEngine.getTemplate("./templates/pagoemailtemplate.vm");
						 
						VelocityContext velocityContext = new VelocityContext();
						velocityContext.put("supplierEmail", emailRecipient);
						velocityContext.put("buyerEmail", purchaseRecipient);

						String amt = String.valueOf(o.getPaymentDocument());
						velocityContext.put("numOperacion", StringUtils.leftPad(amt, 8, "0") + "-" + StringUtils.leftPad(o.getDocumentCompany(), 5, "0"));
						velocityContext.put("bancoPagador", o.getBank());
						velocityContext.put("cuentaPagadora", o.getPaymentBankAccount());
						
						double amount = o.getPaymentAmount();
						DecimalFormat amtFormat = new DecimalFormat("###,###,###.00");
						velocityContext.put("montoTotal", "$" + amtFormat.format(amount));
						             
						velocityContext.put("moneda", o.getCurrency());
						velocityContext.put("fechaPago", o.getDate());
						
					     String monthName = "";
						 String sDate1=o.getDate(); 
						 try {
						    Date date=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);  
						    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						    int month = localDate.getMonthValue();
						    
						    month = month + 1;
						    if(month == 13) {
						    	month = 1;
						    }
						    
						    switch (month) {
						    case 1:
						      monthName = "Enero";
						      break;
						    case 2:
							 monthName = "Febrero";
						      break;
						    case 3:
								 monthName = "Marzo";
						      break;
						    case 4:
								 monthName = "Abril";
						      break;
						    case 5:
								 monthName = "Mayo";
						      break;
						    case 6:
								 monthName = "Junio";
						      break;
						    case 7:
								 monthName = "Julio";
						      break;
						    case 8:
								 monthName = "Agosto";
						      break;
						    case 9:
								 monthName = "Septiembre";
						      break;
						    case 10:
								 monthName = "Octubre";
						      break;
						    case 11:
								 monthName = "Noviembre";
						      break;
						    case 12:
								 monthName = "Diciembre";
						      break;
						  }
						    
						    monthName = "10 de " + monthName;
						 }catch(Exception e) {
							 log4j.error("Exception" , e);
							 monthName = ""; 
						 }
						
						velocityContext.put("fechaCargaComplemento", monthName);
						
						Set<PurchaseOrderPaymentDetails> dtl = o.getPurchaseOrderPaymentDetails();
						List<PurchaseOrder> oList = new ArrayList<PurchaseOrder>();
						for(PurchaseOrderPaymentDetails d : dtl) {
							String paymentKey = d.getAddressNumber() + "_" + d.getOrderNumber() + "_" + d.getOrderType();
							PurchaseOrder p = poMap.get(paymentKey);
							if(p != null) {
								p.setDeliveryInst1(d.getBankReference());
								p.setDeliveryInst2(d.getTrading());
								oList.add(p);
							}

						}
						velocityContext.put("orders", oList);

						StringWriter stringWriter = new StringWriter();
						template.merge(velocityContext, stringWriter);

						
						if(!"".equals(purchaseRecipient)) {
							emailRecipient = emailRecipient + "," + purchaseRecipient;
							if(!"".equals(additionalRecipient)) {
								emailRecipient = emailRecipient + "," + additionalRecipient;
							}
						}
						
						EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						emailAsyncSup.setProperties(AppConstants.EMAIL_INV_PAYMENT_SUP, stringWriter.toString(), emailRecipient);
						emailAsyncSup.setMailSender(mailSenderObj);
						Thread emailThreadSup = new Thread(emailAsyncSup);
						emailThreadSup.start();
					}
					purchaseOrderService.saveMultiplePayments(validPaymentList);
					purchaseOrderService.updateMultiple(validPo);
				}
				
			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}
		}
	}
	
	
	public void setObjectList(List<PurchaseOrderPayment> paymentList, 
			                  List<PurchaseOrder> poList, 
			                  PurchaseOrderService purchaseOrderService,
			                  JavaMailSender mailSenderObj,
			                  VelocityEngine velocityEngine) {
		this.paymentList = paymentList;
		this.poList = poList;
		this.purchaseOrderService = purchaseOrderService;
		this.mailSenderObj = mailSenderObj;
		this.velocityEngine = velocityEngine;
	}


	public void setAdditionalRecipient(String additionalRecipient) {
		this.additionalRecipient = additionalRecipient;
	}
	
	
	

}
