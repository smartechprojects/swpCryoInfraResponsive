package com.eurest.supplier.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eurest.supplier.dao.PaymentSupplierDao;
import com.eurest.supplier.dao.PaymentSupplierDetailDao;
import com.eurest.supplier.dao.PurchaseOrderDao;
import com.eurest.supplier.dao.UDCDao;
import com.eurest.supplier.dto.ForeingInvoice;
import com.eurest.supplier.dto.InvoiceCodesDTO;
import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.dto.PurchaseOrderDTO;
import com.eurest.supplier.invoiceXml.Concepto;
import com.eurest.supplier.model.CodigosSAT;
import com.eurest.supplier.model.ExchangeRate;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.CustomBroker;
import com.eurest.supplier.model.ForeignInvoiceTable;
import com.eurest.supplier.model.LogData;
import com.eurest.supplier.model.PaymentCalendar;
import com.eurest.supplier.model.PaymentSupplier;
import com.eurest.supplier.model.PaymentSupplierDetail;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.PurchaseOrderDetail;
import com.eurest.supplier.model.PurchaseOrderPayment;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.ReceiptInvoice;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.Tolerances;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.FileConceptUploadBean;
import com.eurest.supplier.util.JdeJavaJulianDateTools;
import com.eurest.supplier.util.Logger;
import com.eurest.supplier.util.StringUtils;

@Service("purchaseOrderService")
public class PurchaseOrderService {

	@Autowired
	private PurchaseOrderDao purchaseOrderDao;

	@Autowired
	EDIService eDIService;

	@Autowired
	private JavaMailSender mailSenderObj;

	@Autowired
	EmailService emailService;

	@Autowired
	SupplierService supplierService;

	@Autowired
	TolerancesService tolerancesService;

	@Autowired
	DocumentsService documentsService;

	@Autowired
	JDERestService jDERestService;

	@Autowired
	StringUtils stringUtils;

	@Autowired
	UdcService udcService;
	
	@Autowired
	UDCDao udcDao;

	@Autowired
	Logger logger;
	
	@Autowired
	XmlToPojoService xmlToPojoService;
	
	@Autowired
	CodigosSATService codigosSATService;
	
	@Autowired
	PaymentCalendarService paymentCalendarService;
	
	@Autowired
	FiscalDocumentService fiscalDocumentService;

	@Autowired
	CustomBrokerService customBrokerService;
		
	@Autowired
	ExchangeRateService exchangeRateService;
	
	@Autowired
	DataAuditService dataAuditService;
	
	  @Autowired
	  PaymentSupplierDao paymentSupplierDao;
	  
	  @Autowired
	  PaymentSupplierDetailDao paymentSupplierDetailDao;
	
	static String TIMESTAMP_DATE_PATTERN = "yyyy-MM-dd";
	static String TIMESTAMP_DATE_PATTERN_NEW = "yyyy-MM-dd HH:mm:ss";
	static String DATE_PATTERN = "dd/MM/yyyy";	
	
	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(PurchaseOrderService.class);

	public List<PurchaseOrder> getOrders(int start, int limit) {
		return purchaseOrderDao.getOrders(start, limit);
	}

	public boolean confirmEmailOrders(PurchaseOrder[] selected) {

		try {
			
			String altEmail = "";
			List<UDC> udcList =  udcService.searchBySystem("ALTEMAIL");
			if(udcList != null) {
				altEmail = udcList.get(0).getStrValue1();
			}

			List<PurchaseOrder> objList = Arrays.asList(selected);
			Supplier s = null;
			for (PurchaseOrder o : objList) {
				
				s = supplierService.searchByAddressNumber(o.getAddressNumber());
				o.setOrderStauts(AppConstants.STATUS_OC_SENT);
				o.setSupplierEmail(s.getEmailSupplier());
				purchaseOrderDao.updateOrders(o);
				String emailRecipient = (s.getEmailContactoPedidos());
				if(AppConstants.DOCTYPE_PUB1.equals(o.getOrderType()) || AppConstants.DOCTYPE_PUB2.equals(o.getOrderType())) {
					if(!"".equals(altEmail)) {
						emailRecipient = emailRecipient + "," + altEmail;
					}
				}
				
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
						emailRecipient + "," + o.getEmail());
				emailAsyncSup.setMailSender(mailSenderObj);
				emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
				Thread emailThreadSup = new Thread(emailAsyncSup);
				emailThreadSup.start();
			}
			return true;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return false;
		}
	}

	public boolean reasignOrders(PurchaseOrder[] selected) {

		try {

			List<PurchaseOrder> objList = Arrays.asList(selected);
			List<Integer> idList = new ArrayList<Integer>();
			String email = "";

			for (PurchaseOrder o : objList) {
				idList.add(o.getId());
				email = o.getEmail();
				logger.log(AppConstants.LOG_REASIGN_TITLE, AppConstants.LOG_REASIGN_MSG.replace("ORDER_NUMBER", String.valueOf(o.getOrderNumber())).replace("NEW_ORDER_EMAIL", email));
			}

			if (idList.size() > 0) {
				return purchaseOrderDao.updateEmail(email, idList);
			}

			return false;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return false;
		}
	}

	public boolean confirmOrderInvoices(PurchaseOrder[] selected) {

		try {

			List<PurchaseOrderDTO> oDto = new ArrayList<PurchaseOrderDTO>();
			List<PurchaseOrder> objList = Arrays.asList(selected);
			for (PurchaseOrder o : objList) {

				o.setOrderStauts(AppConstants.STATUS_OC_PROCESSED);
				purchaseOrderDao.updateOrders(o);
				Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
				String emailRecipient = (s.getEmailSupplier());
				
				String emailContent = AppConstants.EMAIL_INVOICE_NOPURCHASE_ACCEPTED;
				emailContent = emailContent.replace("_DATA_", o.getOrderNumber() + "-" + o.getOrderType());

				EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				emailAsyncSup
						.setProperties(
								AppConstants.EMAIL_INV_APPROVED_SUP + o.getOrderNumber() + "-" + o.getOrderType(),
								stringUtils.prepareEmailContent(emailContent),
								emailRecipient + "," + o.getEmail());
				emailAsyncSup.setMailSender(mailSenderObj);
				emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
				Thread emailThreadSup = new Thread(emailAsyncSup);
				emailThreadSup.start();

				PurchaseOrderDTO td = new PurchaseOrderDTO();
				td.setPHAN8(o.getAddressNumber());
				td.setPHSFXO(o.getOrderCompany());
				td.setPHDOCO(String.valueOf(o.getOrderNumber()));
				td.setPHDCTO(o.getOrderType());
				td.setPHDESC(o.getInvoiceUuid());
				td.setPHORDERSTS(o.getInvoiceNumber());
				oDto.add(td);
				eDIService.createNewReceipt(o);
			}

			if (oDto.size() > 0) {
				jDERestService.sendOrderInvoiceConfirmation(oDto);
			}

			return true;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return false;
		}
	}

	public boolean rejectOrderInvoices(PurchaseOrder[] selected, boolean notifySupplier, String rejectType) {

		try {
			List<PurchaseOrder> objList = Arrays.asList(selected);
			for (PurchaseOrder o : objList) {

				    String uuid = "";
					if("S".equals(o.getSentToWns())) {
						o.setSentToWns("N");
					}else {
						o.setSentToWns(null);
					}
					
					if("WNS".equals(rejectType)) {
						o.setStatus(AppConstants.STATUS_OC_INVOICED);
						o.setOrderStauts(AppConstants.STATUS_OC_INVOICED);
						purchaseOrderDao.updateOrders(o);
					}else {
						uuid = o.getInvoiceUuid();
						o.setOrderStauts(AppConstants.STATUS_OC_SENT);
						o.setInvoiceUuid("");
						o.setInvoiceAmount(0);
						o.setOrderAmount(o.getOriginalOrderAmount());
						o.setStatus("");
						o.setInvoiceNumber("");
						purchaseOrderDao.updateOrders(o);

						List<UserDocument> docList = documentsService.searchCriteriaByOrderNumber(o.getOrderNumber(),
								o.getOrderType(), o.getAddressNumber(),true);
						if (docList != null) {
							if (!docList.isEmpty()) {
								for (UserDocument d : docList) {
									documentsService.delete(d.getId(),"INV_REJECT");
								}
							}
						}
					}
				if(notifySupplier) {
					Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
	
					// ****** ELIMINAR COMENTARIOS EN PRODUCCION
					String emailRecipient = (s.getEmailSupplier());
					
					String emailContent = AppConstants.EMAIL_INVOICE_REJECTED;
					emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(o.getOrderNumber()));
					emailContent = emailContent.replace("_ORDERTYPE_", o.getOrderType());
					emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);
					emailContent = emailContent.replace("_REASON_", o.getRejectNotes());
					emailContent = emailContent.replace("_EMAILSUPPORT_", o.getEmail());
	
					/// ************ DESHABILITAR PARA PRODUCCIÓN
					//String emailRecipient = "javila@smartech.com.mx";

					EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
					emailAsyncSup.setProperties(
							AppConstants.EMAIL_INV_REJECT_SUP + o.getOrderNumber() + "-" + o.getOrderType(),
							stringUtils.prepareEmailContent(emailContent),
							emailRecipient + "," + o.getEmail());
					emailAsyncSup.setMailSender(mailSenderObj);
					emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
					Thread emailThreadSup = new Thread(emailAsyncSup);
					emailThreadSup.start();
				}else {
					String emailRecipient = (o.getEmail());
					
					String emailContent = AppConstants.EMAIL_INVOICE_REJECTED;
					emailContent = emailContent.replace("_ORDERNUM_", String.valueOf(o.getOrderNumber()));
					emailContent = emailContent.replace("_ORDERTYPE_", o.getOrderType());
					emailContent = emailContent.replace("_PORTALLINK_", AppConstants.EMAIL_PORTAL_LINK);
					emailContent = emailContent.replace("_REASON_", o.getRejectNotes());
					emailContent = emailContent.replace("_EMAILSUPPORT_", o.getEmail());
					
					EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
					emailAsyncSup.setProperties(
							AppConstants.EMAIL_INV_REJECT_SUP + o.getOrderNumber() + "-" + o.getOrderType(),
							stringUtils.prepareEmailContent(emailContent),
							emailRecipient);
					emailAsyncSup.setMailSender(mailSenderObj);
					emailAsyncSup.setAdditionalReference(udcDao, o.getOrderType());
					Thread emailThreadSup = new Thread(emailAsyncSup);
					emailThreadSup.start();
				}

				logger.log(AppConstants.LOG_INVREJECTED_TITLE, AppConstants.LOG_INVREJECTED_MDG + o.getOrderNumber()
						+ " / UUID:" + uuid + " -> Texto del rechazo: " + o.getRejectNotes());
			}
			return true;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return false;
		}
	}

	public void paymentImportBulk() {
		try {
			jDERestService.getOrderPayments();
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();

		}
	}

	public List<PurchaseOrder> getOrderForPayment() {
		return purchaseOrderDao.getOrderForPayment();
	}

	public List<PurchaseOrder> searchbyOrderNumber(int orderNumber, String addressBook, Date poFromDate, Date poToDate,String poTypeCombo,
			String status, int start, int limit, String role, String email, String foreing) {

		Map<String, Tolerances> tolerances = tolerancesService.getTolerancesMap();
		List<PurchaseOrder> list = purchaseOrderDao.searchbyOrderNumberHQL(orderNumber, addressBook, poFromDate, poToDate,poTypeCombo,
				status, start, limit, role, email, foreing);

		Map<String, String> udcCompanyMap = new HashMap<String, String>();
		List<UDC> udcList = udcService.searchBySystem("CIACOR");
		if (udcList != null) {
			for (UDC o : udcList) {
				udcCompanyMap.put(o.getUdcKey(), o.getStrValue1());
			}
		}

		Map<String, String> udcCompanyMapLar = new HashMap<String, String>();
		List<UDC> udcListLar = udcService.searchBySystem("CIALAR");
		if (udcListLar != null) {
			for (UDC o : udcListLar) {
				udcCompanyMapLar.put(o.getUdcKey(), o.getStrValue1() + " / " + o.getStrValue2());
			}
		}

		if (!list.isEmpty()) {
			for (PurchaseOrder po : list) {
				Set<PurchaseOrderDetail> dtl = po.getPurchaseOrderDetail();
				String company = po.getOrderCompany();
				String obj = udcCompanyMap.get(company);
				if (obj != null) {
					po.setShortCompanyName(company + "-" + obj);
				}

				obj = udcCompanyMapLar.get(company);
				if (obj != null) {
					po.setLongCompanyName(company + "-" + obj);
				}

				if (!dtl.isEmpty()) {
					for (PurchaseOrderDetail d : dtl) {
						// String key = d.getOrderCompany().trim() + "_" + d.getItemNumber().trim();
						String key = d.getItemNumber().trim();
						Tolerances t = (Tolerances) tolerances.get(key);
						if (t != null) {
							d.setTolerances(t);
						}
					}
				}

			}

		}
		return list;
	}

	public int getTotalRecords(String addressBook, int orderNumber, Date poFromDate, Date poToDate,String poTypeCombo, String status,
			String role, String email, String foreign) {
		return purchaseOrderDao.searchbyOrderNumberHQLCount(orderNumber, addressBook, poFromDate, poToDate,poTypeCombo, status, role, email, foreign);
	}

	public List<PurchaseOrder> searchCriteria(String addressBook, String status, String orderStatus) {
		return purchaseOrderDao.searchCriteria(addressBook, status, orderStatus);
	}

	public PurchaseOrder getOrderByOrderAndAddresBook(int orderNumber, String addressBook, String orderType) {
		return purchaseOrderDao.searchbyOrderAndAddressBook(orderNumber, addressBook, orderType);
	}
	
	public PurchaseOrder searchbyOrderAdress(int orderNumber, String addressBook) {
		return purchaseOrderDao.searchbyOrderAdress(orderNumber, addressBook);
	}
	
	public List<ReceiptInvoice> getPaymentPendingReceipts() {
		return purchaseOrderDao.getPaymentPendingReceipts();
	}
		
	public List<Receipt> getPaymentPendingReceipts(int start, int limit) {
		return purchaseOrderDao.getPaymentPendingReceipts(start, limit);
	}
	
	public List<PurchaseOrder> getOpenOrderPO(int start, int limit) {
		return purchaseOrderDao.getOpenOrderPO(start, limit);
	}
	
	public List<Receipt> getOpenOrderReceipts(int start, int limit) {
		return purchaseOrderDao.getOpenOrderReceipts(start, limit);
	}

	public PurchaseOrder searchbyOrder(int orderNumber, String orderType) {
		return purchaseOrderDao.searchbyOrder(orderNumber, orderType);
	}

	public List<PurchaseOrder> searchCriteriaByEmail(String email) {
		return purchaseOrderDao.searchCriteriaByEmail(email.trim());
	}

	public PurchaseOrder getOrderById(int id) {
		return purchaseOrderDao.getOrderById(id);

	}

	public Receipt getReceiptById(int id) {
		return purchaseOrderDao.getReceiptById(id);
	}
	
	public ReceiptInvoice getReceiptInvoiceId(int id) {
		return purchaseOrderDao.getReceiptInvoiceById(id);
	}
	
	public List<PurchaseOrder> getPendingPaymentOrders(int orderNumber, String addressBook, String orderType) {

		
		//JAVILA: Se revisó el fromat de fechas que proucía un a excepción incorrecta
		List<PurchaseOrder> invalidList = new ArrayList<PurchaseOrder>();
		List<PurchaseOrder> list = purchaseOrderDao.getPendingPaymentOrders(orderNumber, addressBook, orderType);
		for (PurchaseOrder p : list) {
			String paymentDate = p.getRemark();
			if (paymentDate != null) {
				if (!"".equals(paymentDate)) {
					try {
						Date pDate = new SimpleDateFormat("yyyy-MM-dd").parse(paymentDate);
						Date currentDate = new Date();

						Calendar paymentCalendar = Calendar.getInstance();
						Calendar currentCalendar = Calendar.getInstance();
						paymentCalendar.setTime(pDate);
						currentCalendar.setTime(currentDate);

						int paymentMonth = paymentCalendar.get(Calendar.MONTH) + 1;
						int currentMonth = currentCalendar.get(Calendar.MONTH) + 1;

						if (paymentMonth < 12) {
							int paymentTargetMonth = paymentMonth + 1;
							int paymentTargetDay = 10;

							// Same Year
							if (paymentCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) {
								if (currentMonth == paymentTargetMonth) {
									int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
									if (currentDay > paymentTargetDay) {
										invalidList.add(p);
										continue;
									}
								}
								if (currentMonth > paymentTargetMonth) {
									invalidList.add(p);
									continue;
								}

							}
							
							// Previous Year
							if (paymentCalendar.get(Calendar.YEAR) < currentCalendar.get(Calendar.YEAR)) {
								if (currentMonth == paymentTargetMonth) {
									int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
									if (currentDay > paymentTargetDay) {
										invalidList.add(p);
										continue;
									}
								}
								
								if (currentMonth < paymentTargetMonth) {
									invalidList.add(p);
									continue;
								}

							}
							

						} else {
							// Next year
							int paymentTargetMonth = 1;
							int paymentTargetDay = 10;
							if (currentCalendar.get(Calendar.YEAR) > paymentCalendar.get(Calendar.YEAR)) {
								if (currentMonth == paymentTargetMonth) {
									int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
									if (currentDay > paymentTargetDay) {
										invalidList.add(p);
										continue;
									}
								}
							}
						}

					} catch (Exception e) {
						log4j.error("Exception" , e);
						continue;
					}
				}
			}
		}

		return invalidList;
	}

	public synchronized void saveReceipts(PurchaseOrderDetail[] objArr, int id) {

		List<PurchaseOrderDetail> obj = new ArrayList<PurchaseOrderDetail>(Arrays.asList(objArr));
		List<PurchaseOrderDetail> objExcept = new ArrayList<PurchaseOrderDetail>(Arrays.asList(objArr));

		PurchaseOrder order = purchaseOrderDao.getOrderById(id);
		Supplier s = supplierService.searchByAddressNumber(order.getAddressNumber());
		String emailRecipient = (s.getEmailContactoVentas() + "," + s.getEmailSupplier());

		order.setPurchasOrderDetail(null);
		Set<PurchaseOrderDetail> set = new HashSet<PurchaseOrderDetail>();
		Set<PurchaseOrderDetail> setExcept = new HashSet<PurchaseOrderDetail>();

		for (PurchaseOrderDetail dtl : obj) {
			if (dtl.getToReject() > 0) {
				dtl.setRejected(dtl.getRejected() + dtl.getToReject());
				dtl.setStatus(AppConstants.STATUS_REJECT);
			}

			if (dtl.getToReceive() > 0) {
				dtl.setReceived(dtl.getReceived() + dtl.getToReceive());
				if (dtl.getPending() == 0) {
					dtl.setStatus(AppConstants.STATUS_RECEIVED);
				} else {
					if (dtl.getToReceive() > 0) {
						dtl.setStatus(AppConstants.STATUS_PARTIAL);
					}
				}
			}
			set.add(dtl);
		}
		order.setPurchasOrderDetail(set);

		boolean partial = false;
		double amountReceived = 0;
		Set<PurchaseOrderDetail> dtlList = order.getPurchaseOrderDetail();
		for (PurchaseOrderDetail dtl : dtlList) {
			amountReceived = amountReceived + dtl.getAmuntReceived();
			if (AppConstants.STATUS_PARTIAL.equals(dtl.getStatus())) {
				partial = true;
			}
		}

		order.setOrderAmount(amountReceived);
		if (partial) {
			order.setOrderStauts(AppConstants.STATUS_PARTIAL);
		} else {
			order.setOrderStauts(AppConstants.STATUS_RECEIVED);
		}

		purchaseOrderDao.updateOrders(order);

		if ("Y".equals(s.getInvException())) { // Proceso excepcional de recibo
			for (PurchaseOrderDetail dtl : objExcept) {
				if (dtl.getToReject() > 0) {
					dtl.setReceived(dtl.getToReject());
					dtl.setAmuntReceived(dtl.getReceived() * dtl.getUnitCost());
					dtl.setToReceive(dtl.getToReject());
					dtl.setToReject(0);
				}
				setExcept.add(dtl);
			}
			order.setPurchasOrderDetail(setExcept); // Override only for JDE
		}

		eDIService.createNewReceipt(order);

		emailService.sendEmail(AppConstants.EMAIL_INVOICE_SUBJECT,
				AppConstants.EMAIL_RECEIPT_COMPLETE + order.getOrderNumber() + "-" + order.getOrderType(),
				emailRecipient);
	}

	public void updateOrders(PurchaseOrder o) {
		purchaseOrderDao.updateOrders(o);
	}
	
	public void updateReceipts(List<Receipt> o) {
		purchaseOrderDao.updateReceipts(o);
	}

	public synchronized List<PurchaseOrder> saveMultiple(List<PurchaseOrder> list) {
		log4j.info("*********** STEP 5: saveMultiple:listSize:" + list.size());
		return purchaseOrderDao.saveMultiple(list);
	}
	
	public List<Receipt> saveMultipleReceipt(List<Receipt> list) {
		for(Receipt r : list) {			
			if((r.getAmountReceived() < 0d || r.getForeignAmountReceived() < 0d) 
					&& AppConstants.JDE_RETENTION_CODE.equals(String.valueOf(r.getObjectAccount()).trim())) {
				
				r.setAmountReceived(Math.abs(r.getAmountReceived()));
				r.setForeignAmountReceived(Math.abs(r.getForeignAmountReceived()));
				r.setQuantityReceived(Math.abs(r.getQuantityReceived()));
				r.setReceiptType(AppConstants.RECEIPT_CODE_RETENTION);
			}
			r.setStatus(AppConstants.STATUS_OC_APPROVED);
		}
		
		return purchaseOrderDao.saveMultipleReceipt(list);
	}

	public synchronized void updateMultiple(List<PurchaseOrder> list) {
		purchaseOrderDao.updateMultiple(list);
	}

	public int getTotalRecords() {
		return purchaseOrderDao.getTotalRecords();
	}

	public void getPurchaseOrderListBySelection(int orderNumber, String addressNumber, String fromDate, String toDate) {
		
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		Date d;
		try {
			d = fmt.parse(fromDate);
			String fromDateJulian = JdeJavaJulianDateTools.Methods.getJulianDate(d);
			
			d = fmt.parse(toDate);
			String toDateJulian = JdeJavaJulianDateTools.Methods.getJulianDate(d);

			jDERestService.getPurchaseOrderListBySelection(orderNumber, addressNumber, fromDateJulian, toDateJulian, "", "");
		} catch (ParseException e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		 

	}

	public void getPurchaseOrderList() {
		jDERestService.getPurchaseOrderList();
	}
	
	public void saveMultiplePayments(List<PurchaseOrderPayment> list) {
		purchaseOrderDao.saveMultiplePayments(list);
	}

	public PurchaseOrder searchbyOrderUuid(String uuid) {
		return purchaseOrderDao.searchbyOrderUuid(uuid);
	}
	
	public List<PurchaseOrderPayment> getAll() {
		return purchaseOrderDao.getAll();
	}

	@SuppressWarnings("unused")
	public String createForeignInvoice(ForeingInvoice inv) {

		PurchaseOrder po = purchaseOrderDao.searchbyOrderAndAddressBook(inv.getOrderNumber(), inv.getAddressNumber(),inv.getOrderType());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	 	String userAuth = auth.getName();
	 	Date currentDateAudit = new Date();
		String documentNumber ="";
		if (po != null) {
			try {
				boolean isPDFAttached = false;
				List<UserDocument> listDocumentAtt = documentsService.searchCriteriaByOrderNumber(inv.getOrderNumber(),inv.getOrderType(), inv.getAddressNumber(),true);						
				if(listDocumentAtt != null && !listDocumentAtt.isEmpty()) {
					for(UserDocument document : listDocumentAtt) {
						if(org.apache.commons.lang.StringUtils.isBlank(document.getUuid())
								&& document.getContent() != null
								&& document.getName() != null 
								&& document.getName().toLowerCase().contains(".pdf") 
								) {
							isPDFAttached = true;
							break;
						}
					}
				}
				
				if(!isPDFAttached) {
					return "ForeignInvoiceError_1";
				}
				
				Supplier s = supplierService.searchByAddressNumber(inv.getAddressNumber());
				if(s == null) {
					return "ForeignInvoiceError_2";
				}
				String emailRecipient = (s.getEmailSupplier());
				
				String fechaFactura = inv.getExpeditionDate();
				fechaFactura = fechaFactura.replace("T", " ");
				SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
				Date invDate = null;
				try {
					invDate = sdf.parse(fechaFactura);
				}catch(Exception e) {
					log4j.error("Exception" , e);
					e.printStackTrace();
				}
				
				
				int currentYear = Calendar.getInstance().get(Calendar.YEAR);
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, currentYear);
				cal.set(Calendar.DAY_OF_YEAR, 1);    
				Date startYear = cal.getTime();
				try {
					if(invDate.compareTo(startYear) < 0) {
						return "ForeignInvoiceError_3";
					}
				}catch(Exception e) {
					log4j.error("Exception" , e);
					e.printStackTrace();
					return "ForeignInvoiceError_4";
				}

				
				String oCurr = po.getCurrecyCode();
				String invCurrency = inv.getForeignCurrency();
				if(!invCurrency.equals(oCurr)) {
					return "ForeignInvoiceError_5";
				}
				
				List<Receipt> requestedReceiptList = null;
				List<Receipt> receiptArray= purchaseOrderDao.getOrderReceipts(inv.getOrderNumber(), inv.getAddressNumber(),inv.getOrderType(), "");
				
				//JSC:Se sustituyen Ids por Numeros de Recibo para Facturas Nacionales y Foráneas
				String[] idList = inv.getReceiptIdList().split(",");
				//Receipt selReceipt = this.getReceiptById(Integer.valueOf(idList[0]).intValue());
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
					return "ForeignInvoiceError_6";
			    }
				
				
				String domesticForeignCurrency = po.getCurrecyCode().trim();
				boolean isDomestic = false;
				List<UDC> comUDCList =  udcService.searchBySystem("COMPANYDOMESTIC");
				if(comUDCList != null && !comUDCList.isEmpty()) {
					for(UDC company : comUDCList) {
						if(company.getStrValue1().trim().equals(po.getOrderCompany().trim()) && !"".equals(company.getStrValue2().trim())) {
							domesticForeignCurrency = company.getStrValue2().trim();
							isDomestic = true;
							break;
						}
					}
				}

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
				
				for(Receipt r :requestedReceiptList) {
					if(isDomestic && domesticForeignCurrency.equals(invCurrency)) {
						orderAmount = orderAmount + r.getAmountReceived();	
					} else {
						orderAmount = orderAmount + r.getForeignAmountReceived();
					}
				}

				String tipoValidacion ="";
				
				double totalImporteMayor = orderAmount;
				double totalImporteMenor = orderAmount;
				
				if(montoLimite != 0) {
					if(inv.getForeignSubtotal() >= montoLimite) {
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
				invoiceAmount = inv.getForeignSubtotal() - discount;
				
				String uuid = getUuid(po);
				if(totalImporteMayor < invoiceAmount || totalImporteMenor > invoiceAmount) {
					return "ForeignInvoiceError_7";
				}else {
					inv.setUuid(uuid);
					int diasCred = 30 ;
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
					
//					if(po.getPaymentTerms() != null && !"".equals(po.getPaymentTerms().trim())) {
//						for(UDC udcpmt : pmtTermsUdc) {
//							if(udcpmt.getUdcKey().equals(po.getPaymentTerms().trim()) && udcpmt.getStrValue2() != null && !"".equals(udcpmt.getStrValue2())) {
//								diasCred = Integer.parseInt(udcpmt.getStrValue2());
//								break;
//							}
//						}
//					} else {
//						diasCred = 30;
//					}
					
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
					
			        po.setInvoiceAmount(po.getInvoiceAmount() + inv.getForeignDebit());
			        po.setOrderStauts(AppConstants.STATUS_OC_INVOICED);
			        po.setInvoiceUploadDate(invDate);
			        po.setSentToWns(null);
			        po.setEstimatedPaymentDate(estimatedPaymentDate);			        
			        purchaseOrderDao.updateOrders(po);
			        
			        for(Receipt r :requestedReceiptList) {
			        	r.setInvDate(invDate);
			        	r.setFolio(inv.getInvoiceNumber());
			        	r.setSerie("");
			        	r.setUuid(uuid);
			        	r.setEstPmtDate(estimatedPaymentDate);
						r.setStatus(AppConstants.STATUS_OC_INVOICED);
						documentNumber = documentNumber + r.getDocumentNumber() + ",";
					}
					purchaseOrderDao.updateReceipts(requestedReceiptList);
				}
				
				JAXBContext contextObj = JAXBContext.newInstance(ForeingInvoice.class);
				Marshaller marshallerObj = contextObj.createMarshaller();
				marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				StringWriter sw = new StringWriter();
				marshallerObj.marshal(inv, sw);
				String xmlString = sw.toString();

				UserDocument doc = new UserDocument();
				doc.setAddressBook(inv.getAddressNumber());
				doc.setDocumentNumber(inv.getOrderNumber());
				doc.setDocumentType(inv.getOrderType());
				doc.setContent(xmlString.getBytes("UTF-8"));
				doc.setType("TEMP");
				doc.setName("FACT_FOR_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + ".xml");
				doc.setSize(xmlString.length());
				doc.setStatus(false);
				doc.setAccept(true);
				doc.setFiscalType("Factura");
				doc.setFolio("");
				doc.setSerie("");
				doc.setUuid(uuid);//A REVISAR
				doc.setUploadDate(new Date());
				doc.setFiscalRef(0);
				documentsService.save(doc, new Date(), "");
				
				dataAuditService.saveDataAudit("CreateForeignInvoice",po.getAddressNumber(), currentDateAudit, request.getRemoteAddr(),
				userAuth, "FACT_FOR_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + ".xml", "createForeignInvoice", 
				"Created Foreign Invoice Successful",documentNumber+"", po.getOrderNumber()+"", null, 
			    uuid, AppConstants.STATUS_COMPLETE, AppConstants.SALESORDER_MODULE);    


				ForeignInvoiceTable fit = new ForeignInvoiceTable();
				fit.setAddressNumber(inv.getAddressNumber());
				fit.setOrderNumber(inv.getOrderNumber());
				fit.setOrderType(inv.getOrderType());
				fit.setCountry(inv.getCountry());
				fit.setInvoiceNumber(inv.getInvoiceNumber());
				fit.setExpeditionDate(inv.getExpeditionDate());
				fit.setReceptCompany(inv.getReceptCompany());
				fit.setForeignCurrency(inv.getForeignCurrency());
				fit.setForeignSubtotal(inv.getForeignSubtotal());
				fit.setForeignTaxes(inv.getForeignTaxes());
				fit.setForeignRetention(inv.getForeignRetention());
				fit.setForeignDebit(inv.getForeignDebit());
				fit.setForeignDescription(inv.getForeignDescription());
				fit.setForeignNotes(inv.getForeignNotes());
				fit.setUsuarioImpuestos(inv.getUsuarioImpuestos());
				fit.setTaxId(inv.getTaxId());
				fit.setUuid(uuid);
				purchaseOrderDao.saveForeignInvoice(fit);
				
		
				UDC ccompany=udcService.searchBySystemAndKey("COMPANYCB",po.getCompanyKey());
				

				FiscalDocuments fd = new FiscalDocuments();
				
				fd.setFolio(inv.getFolio());
            	fd.setSerie(inv.getInvoiceNumber());
        		//fd.setInvoiceUploadDate(new Date());
        		fd.setUuidFactura(uuid);
        		fd.setUuidPago("");
        		fd.setUuidNotaCredito("");
        		fd.setFolioPago("");
        		fd.setSeriePago("");
        		fd.setFolioNC("");
        		fd.setSerieNC("");
            	fd.setStatus(AppConstants.STATUS_LOADINV);
            	fd.setAddressNumber(fit.getAddressNumber());
            	fd.setAmount(fit.getForeignSubtotal()+fit.getForeignTaxes()+inv.getForeignRetention());
            	fd.setSubtotal(fit.getForeignSubtotal());
            	fd.setDescuento(0);
            	fd.setImpuestos(fit.getForeignTaxes());
            	fd.setRfcEmisor(s.getRfc() == null || "".equals(s.getRfc()) ? s.getTaxId() : s.getRfc());
            	fd.setRfcReceptor(ccompany.getStrValue1());
            	fd.setMoneda(inv.getForeignCurrency());
            	fd.setInvoiceDate(inv.getExpeditionDate());
            	fd.setOrderNumber(inv.getOrderNumber());
            	fd.setOrderType(inv.getOrderType());
            	fd.setOrderCompany(po.getCompanyKey());
            	fd.setType(AppConstants.STATUS_FACT_FOREIGN);
            	fd.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
            	fd.setSupplierName(s.getName());
            	fd.setStatus(AppConstants.STATUS_INPROCESS);
            	fd.setApprovalStatus(AppConstants.STATUS_INPROCESS);
            	fd.setApprovalStep(AppConstants.FIRST_STEP);
            	fd.setPlant(inv.getPlantCode());
            	fd.setDocumentNumber(selReceipt.getDocumentNumber());
            	
            	String emailApprover = "";
        	/*	List<UDC> approverUDCList = udcDao.searchBySystem("APPROVERINV");
        		if(approverUDCList != null) {
        			for(UDC approver : approverUDCList) {
        				if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
        					fd.setCurrentApprover(approver.getStrValue1());
        					emailApprover = approver.getStrValue2();
        				}

        				if(AppConstants.INV_SECOND_APPROVER.equals(approver.getUdcKey())){
        					fd.setNextApprover(approver.getStrValue1());
        				}				
        			}
        		}
            	    fiscalDocumentService.saveDocument(fd);*/
            	if(inv.getPlantCode()!=null && !"".equals(inv.getPlantCode())) {
            	List<UDC> approverUDCList = udcDao.advaceSearch("APPROVERPONP", "", inv.getPlantCode(),"");
        		if(approverUDCList != null) {
        			for(UDC approver : approverUDCList) {
        				if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
        					fd.setCurrentApprover(approver.getStrValue1());
        					emailApprover = approver.getStrValue2();       					  
        				}

        				/*if(AppConstants.INV_SECOND_APPROVER.equals(approver.getUdcKey())){
        					fd.setNextApprover(approver.getStrValue1());
        				}		*/		
        			}
        		}
        		
        		
        		fd.setStatus(AppConstants.STATUS_INPROCESS);
        		fd.setApprovalStatus(AppConstants.STATUS_INPROCESS);
        		fd.setApprovalStep(AppConstants.FIRST_STEP);
        		
        		
        		
        		fiscalDocumentService.saveDocument(fd);
        		}
         

				try {
					String resp;
					if(isDomestic && domesticForeignCurrency.equals(invCurrency)) {//
						InvoiceDTO invDTO = new InvoiceDTO();
						invDTO.setTipoComprobante(AppConstants.RECEIPT_CODE_INVOICE);
						invDTO.setFechaTimbrado(inv.getExpeditionDate());
						invDTO.setSubTotal(inv.getForeignSubtotal());
						invDTO.setFolio(inv.getInvoiceNumber());
						invDTO.setSerie("");
						invDTO.setUuid(uuid);
						log4j.info("DACG:eDIService.createNewVoucher");
						//resp = "DOC:" + eDIService.createNewVoucher(po, invDTO, 0, s, requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);	
					} else {
						//resp = "DOC:" + eDIService.createNewForeignVoucher(po, inv, 0, s, requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
						log4j.info("DACG:eDIService.createNewForeignVoucher");
					}
					
					//Enviar primer archivo adjunto a la factura foranea
					try {
						boolean isFileSent = false;
						List<UserDocument> listDocument = documentsService.searchCriteriaByOrderNumber(inv.getOrderNumber(),inv.getOrderType(), inv.getAddressNumber(),true);						
						if(listDocument != null && !listDocument.isEmpty()) {
							for(UserDocument document : listDocument) {
								document.setUuid(uuid);
								documentsService.update(document, null, null);
								/*if(document.getName() != null && document.getName().toLowerCase().contains(".pdf") 
										&& document.getContent() != null && org.apache.commons.lang.StringUtils.isBlank(document.getUuid())) {*/
									if(document.getName() != null && document.getName().toLowerCase().contains(".pdf") 
											&& document.getContent() != null ) {	
				                	File file = new File(System.getProperty("java.io.tmpdir")+"/"+ fit.getUuid() + ".pdf");
				                	Path filePath = Paths.get(file.getAbsolutePath());
				                	Files.write(filePath, document.getContent());
				                	//document.setUuid(uuid);
				                	//documentsService.sendFileToRemote(file, fit.getUuid() + ".pdf");				                
				                	//documentsService.update(document, null, null);
				                	log4j.info("FACTURA FORANEA: Se envía archivo " + fit.getUuid() + "");
				                	file.delete();
				                	isFileSent = true;
				                	//break;
								}
							}
						}
						if(!isFileSent) {
							log4j.info("FACTURA FORANEA: No se envió ningún archivo para la factura " + fit.getUuid() + "");
						}
					} catch (Exception e) {
						log4j.error("FACTURA FORANEA: Error al enviar el archivo para el UUID " + fit.getUuid() + ".\n" + e.getMessage());
						log4j.error("Exception" , e);
						e.printStackTrace();
					}
					
					String emailContent = AppConstants.EMAIL_INVOICE_ACCEPTED;
					emailContent = emailContent.replace("_INVOICE_", po.getOrderNumber() + "-" + po.getOrderType());
					
					EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
					emailAsyncSup.setProperties(AppConstants.EMAIL_INV_ACCEPT_SUP + po.getOrderNumber() + "-" + po.getOrderType(), 
							stringUtils.prepareEmailContent(emailContent + "<br /> <br />" + AppConstants.ETHIC_CONTENT),
							emailApprover);
					emailAsyncSup.setMailSender(mailSenderObj);
					emailAsyncSup.setAdditionalReference(udcDao, po.getOrderType());
					Thread emailThreadSup = new Thread(emailAsyncSup);
					emailThreadSup.start();
					
					return "";
					
					
				} catch(Exception e) {
					log4j.error("Exception" , e);
					return "";
				}

			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return e.getMessage();
			}
		} else {
			return "ForeignInvoiceError_8";
		}
	}

	public String createForeignInvoiceWithoutOrder(FileConceptUploadBean uploadItem, ForeingInvoice inv, double dblAdvancePayment) {

		try {        	
			boolean isPDFAttached = false;
			List<UserDocument> listDocumentAtt = documentsService.searchCriteriaByRefFiscal(inv.getAddressNumber(), inv.getUuid());						
			if(listDocumentAtt != null && !listDocumentAtt.isEmpty()) {
				for(UserDocument document : listDocumentAtt) {
					if(document.getContent() != null && document.getName() != null && document.getName().toLowerCase().contains(".pdf")) {
						isPDFAttached = true;
						break;
					}
				}
			}
			
			if(!isPDFAttached) {
				return "ForeignInvoiceError_1";
			}
			
			//New Fiscal Document
        	FiscalDocuments fiscalDoc = new FiscalDocuments();
        	fiscalDoc.setAddressNumber(inv.getAddressNumber());
        	fiscalDoc.setCompanyFD(inv.getReceptCompany());
        	
			Supplier s = supplierService.searchByAddressNumber(inv.getAddressNumber());
			if(s != null) {
				fiscalDoc.setSupplierName(s.getName());			
			} else {
				return "ForeignInvoiceError_2";
			}
        	
			String company = inv.getReceptCompany();
			String fechaFactura = inv.getExpeditionDate();
			String fechaFacturaNueva = fechaFactura;
			fechaFactura = fechaFactura.replace("T", " ");
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
			SimpleDateFormat sdfNew = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN_NEW);
			Date invDate = null;
			try {
				invDate = sdf.parse(fechaFactura);
				fechaFacturaNueva = sdfNew.format(invDate);
				fechaFacturaNueva = fechaFacturaNueva.replace(" ", "T");
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}

			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, currentYear);
			cal.set(Calendar.DAY_OF_YEAR, 1);    
			Date startYear = cal.getTime();
			try {
				if(invDate.compareTo(startYear) < 0) {
					return "ForeignInvoiceError_3";
				}
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return "ForeignInvoiceError_4";
			}
        	
			String invCurrency = inv.getForeignCurrency().trim();
			UDC companyRfc = udcDao.searchBySystemAndKey("RFCCOMPANYCC", company);
			if(companyRfc != null) {
				fiscalDoc.setRfcReceptor(companyRfc.getStrValue1());
				fiscalDoc.setCentroCostos(org.apache.commons.lang.StringUtils.leftPad(companyRfc.getStrValue2(), 12, " "));				
			} else {
				return "ForeignInvoiceError_9";
			}
		
			String accountingAcc = "";
			List<UDC> accountingAccRfc = udcDao.searchBySystem("RFCACCOUNTINGACC");
			if(accountingAccRfc != null) {
				for(UDC udcAcc : accountingAccRfc) {
					if(udcAcc.getUdcKey().equals(company) && udcAcc.getStrValue1().equals(invCurrency)) {
						accountingAcc = udcAcc.getStrValue2().trim();
						break;
					}
				}
			}
			
			if(!"".equals(accountingAcc)) {
				fiscalDoc.setAccountingAccount(accountingAcc);
			} else {
				return "ForeignInvoiceError_10";
			}
			
	    	boolean isTransportCB = false;
	    	UDC udcTrans = udcService.searchBySystemAndKey("CUSTOMBROKER", fiscalDoc.getAddressNumber());    	
	    	if(udcTrans != null) {
				if(udcTrans.getStrValue1() != null && "Y".equals(udcTrans.getStrValue1().trim())) {
					isTransportCB = true;
				}
	    	}
	    	
	    	UDC accountingNumber = udcDao.searchBySystemAndKey("CPTACCNUMBER", "CONCEPT000");
	    	if(accountingNumber != null) {
				if(!isTransportCB) {
					fiscalDoc.setAccountNumber(fiscalDoc.getCentroCostos().concat(".").concat(accountingNumber.getStrValue1()));
				} else {
					fiscalDoc.setAccountNumber(fiscalDoc.getCentroCostos().concat(".").concat(accountingNumber.getStrValue2()));						
				}	
	    	} else {
	    		return "ForeignInvoiceError_11";
	    	}
			
	    	if(!AppConstants.DEFAULT_CURRENCY.equals(invCurrency)) {
	    		double exchangeRate;
	    		exchangeRate = this.getCurrentExchangeRate(new Date(), AppConstants.DEFAULT_CURRENCY_JDE, invCurrency);
				if(exchangeRate > 0D) {
					fiscalDoc.setTipoCambio(exchangeRate);					
				} else {
					return "ForeignInvoiceError_12";
				}	
	    	}
			
			int diasCredito = 0;
			UDC pmtTermsUDC = udcDao.searchBySystemAndKey("PMTTCUSTOM", "DEFAULT");
			if(pmtTermsUDC != null) {
				fiscalDoc.setPaymentTerms(pmtTermsUDC.getStrValue1());
				diasCredito = Integer.valueOf(pmtTermsUDC.getStrValue2());
			} else {
				fiscalDoc.setPaymentTerms("N30");
				diasCredito = 30;
			}
			
			String emailApprover = "";
			List<UDC> approverUDCList = udcDao.searchBySystem("APPROVERINV");
			if(approverUDCList != null) {
				for(UDC approver : approverUDCList) {
					if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
						fiscalDoc.setCurrentApprover(approver.getStrValue1());
						emailApprover = approver.getStrValue2();
					}

					if(AppConstants.INV_SECOND_APPROVER.equals(approver.getUdcKey())){
						fiscalDoc.setNextApprover(approver.getStrValue1());
					}				
				}
			}
	    	
			Date estimatedPaymentDate = null;
			Date currentDate = new Date();
			Calendar c = Calendar.getInstance();		
			c.setTime(currentDate);
			c.add(Calendar.DATE, diasCredito);
			List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(c.getTime(), 0, 500, s.getAddresNumber());
			if(pc != null) {
				if(pc.size() > 0) {
					estimatedPaymentDate = pc.get(0).getPaymentDate();
				}else {
					estimatedPaymentDate = c.getTime();
				}
			}else {
				estimatedPaymentDate = c.getTime();
			}
        	
			fiscalDoc.setEstimatedPaymentDate(estimatedPaymentDate);
			fiscalDoc.setInvoiceDate(fechaFacturaNueva);
			fiscalDoc.setCurrencyCode(invCurrency);
			fiscalDoc.setCurrencyMode(AppConstants.CURRENCY_MODE_FOREIGN);
			fiscalDoc.setGlOffset(AppConstants.GL_OFFSET_FOREIGN);
			fiscalDoc.setTaxCode(AppConstants.INVOICE_TAX0);
			fiscalDoc.setSerie("");
			fiscalDoc.setStatus(AppConstants.STATUS_INPROCESS);
			fiscalDoc.setApprovalStatus(AppConstants.STATUS_INPROCESS);
			fiscalDoc.setApprovalStep(AppConstants.FIRST_STEP);			
			fiscalDoc.setFolio(inv.getFolio());
			fiscalDoc.setSerie(inv.getSerie());
			fiscalDoc.setUuidFactura(inv.getUuid());
			fiscalDoc.setType(AppConstants.STATUS_FACT_FOREIGN);                	
			fiscalDoc.setRfcEmisor("");
			fiscalDoc.setSubtotal(inv.getForeignSubtotal());
			fiscalDoc.setAmount(inv.getForeignDebit());
			fiscalDoc.setMoneda(inv.getForeignCurrency());
			fiscalDoc.setDescuento(0);
			fiscalDoc.setImpuestos(0);			
        	fiscalDoc.setAdvancePayment(dblAdvancePayment);
        	fiscalDoc.setInvoiceUploadDate(new Date());
        	
        	//Create concept list
        	fiscalDocumentService.createConceptList(uploadItem, fiscalDoc, false);
        	
        	//Save Fiscal Document
        	fiscalDocumentService.saveDocument(fiscalDoc);
        	
        	//Save Concept Documents
        	documentsService.save(uploadItem, inv.getAddressNumber(), inv.getUuid());
			
			JAXBContext contextObj = JAXBContext.newInstance(ForeingInvoice.class);
			Marshaller marshallerObj = contextObj.createMarshaller();
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			StringWriter sw = new StringWriter();
			marshallerObj.marshal(inv, sw);
			String xmlString = sw.toString();

			UserDocument doc = new UserDocument();
			doc.setAddressBook(inv.getAddressNumber());
			doc.setDocumentNumber(0);
			doc.setDocumentType("Honorarios");
			doc.setContent(xmlString.getBytes("UTF-8"));
			doc.setType("TEMP");
			doc.setName(inv.getUuid() + ".xml");
			doc.setSize(xmlString.length());
			doc.setStatus(false);
			doc.setAccept(true);
			doc.setFiscalType("Factura");
			doc.setFolio(inv.getFolio());
			doc.setSerie("");
			doc.setUuid(inv.getUuid());
			doc.setUploadDate(new Date());
			doc.setFiscalRef(0);
			doc.setDescription("MainUUID_".concat(inv.getUuid()));
			documentsService.save(doc, new Date(), "");

			ForeignInvoiceTable fit = new ForeignInvoiceTable();
			fit.setAddressNumber(inv.getAddressNumber());
			fit.setOrderNumber(inv.getOrderNumber());
			fit.setOrderType(inv.getOrderType());
			fit.setCountry(inv.getCountry());
			fit.setInvoiceNumber(inv.getInvoiceNumber());
			fit.setExpeditionDate(fechaFacturaNueva);
			fit.setReceptCompany(inv.getReceptCompany());
			fit.setForeignCurrency(inv.getForeignCurrency());
			fit.setForeignSubtotal(inv.getForeignSubtotal());
			fit.setForeignTaxes(inv.getForeignTaxes());
			fit.setForeignRetention(inv.getForeignRetention());
			fit.setForeignDebit(inv.getForeignDebit());
			fit.setForeignDescription(inv.getForeignDescription());
			fit.setForeignNotes(inv.getForeignNotes());
			fit.setUsuarioImpuestos(inv.getUsuarioImpuestos());
			fit.setTaxId(inv.getTaxId());
			fit.setUuid(inv.getUuid());
			fit.setReceptCompany("");
			fit.setUsuarioImpuestos("");
			purchaseOrderDao.saveForeignInvoice(fit);
			
			try {
				String emailContent = AppConstants.EMAIL_INV_APPROVAL_MSG_1_NO_OC;
				  emailContent = emailContent.replace("_UUID_", inv.getUuid());
				  emailContent = emailContent.replace("_SUPPLIER_", s.getAddresNumber());
				
		    	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
		        emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_NO_OC, 
		        this.stringUtils.prepareEmailContent(emailContent +  "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK),
		        emailApprover);
		        emailAsyncSup.setMailSender(mailSenderObj);
		        Thread emailThreadSup = new Thread(emailAsyncSup);
		        emailThreadSup.start();
			} catch (Exception e) {			
				log4j.error("Exception" , e);
			}
			
			return "";
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String createForeignInvoiceWithoutOrderCB(FileConceptUploadBean uploadItem, ForeingInvoice inv, double dblAdvancePayment) {

		try {        	
			boolean isPDFAttached = false;
			List<UserDocument> listDocumentAtt = documentsService.searchCriteriaByRefFiscal(inv.getAddressNumber(), inv.getUuid());						
			if(listDocumentAtt != null && !listDocumentAtt.isEmpty()) {
				for(UserDocument document : listDocumentAtt) {
					if(document.getContent() != null && document.getName() != null && document.getName().toLowerCase().contains(".pdf")) {
						isPDFAttached = true;
						break;
					}
				}
			}
			
			if(!isPDFAttached) {
				return "ForeignInvoiceError_1";
			}
			
			//New Fiscal Document
        	CustomBroker fiscalDoc = new CustomBroker();
        	fiscalDoc.setAddressNumber(inv.getAddressNumber());
        	fiscalDoc.setCompanyFD(inv.getReceptCompany());
        	
			Supplier s = supplierService.searchByAddressNumber(inv.getAddressNumber());
			if(s != null) {
				fiscalDoc.setSupplierName(s.getName());			
			} else {
				return "ForeignInvoiceError_2";
			}
        	
			String company = inv.getReceptCompany();
			String fechaFactura = inv.getExpeditionDate();
			String fechaFacturaNueva = fechaFactura;
			fechaFactura = fechaFactura.replace("T", " ");
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
			SimpleDateFormat sdfNew = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN_NEW);
			Date invDate = null;
			try {
				invDate = sdf.parse(fechaFactura);
				fechaFacturaNueva = sdfNew.format(invDate);
				fechaFacturaNueva = fechaFacturaNueva.replace(" ", "T");
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}

			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, currentYear);
			cal.set(Calendar.DAY_OF_YEAR, 1);    
			Date startYear = cal.getTime();
			try {
				if(invDate.compareTo(startYear) < 0) {
					return "ForeignInvoiceError_3";
				}
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return "ForeignInvoiceError_4";
			}
        	
			String invCurrency = inv.getForeignCurrency().trim();
			UDC companyRfc = udcDao.searchBySystemAndKey("RFCCOMPANYCC", company);
			if(companyRfc != null) {
				fiscalDoc.setRfcReceptor(companyRfc.getStrValue1());
				fiscalDoc.setCentroCostos(org.apache.commons.lang.StringUtils.leftPad(companyRfc.getStrValue2(), 12, " "));				
			} else {
				return "ForeignInvoiceError_9";
			}
		
			String accountingAcc = "";
			List<UDC> accountingAccRfc = udcDao.searchBySystem("RFCACCOUNTINGACC");
			if(accountingAccRfc != null) {
				for(UDC udcAcc : accountingAccRfc) {
					if(udcAcc.getUdcKey().equals(company) && udcAcc.getStrValue1().equals(invCurrency)) {
						accountingAcc = udcAcc.getStrValue2().trim();
						break;
					}
				}
			}
			
			if(!"".equals(accountingAcc)) {
				fiscalDoc.setAccountingAccount(accountingAcc);
			} else {
				return "ForeignInvoiceError_10";
			}
			
	    	boolean isTransportCB = false;
	    	UDC udcTrans = udcService.searchBySystemAndKey("CUSTOMBROKER", fiscalDoc.getAddressNumber());    	
	    	if(udcTrans != null) {
				if(udcTrans.getStrValue1() != null && "Y".equals(udcTrans.getStrValue1().trim())) {
					isTransportCB = true;
				}
	    	}
	    	
	    	UDC accountingNumber = udcDao.searchBySystemAndKey("CPTACCNUMBER", "CONCEPT000");
	    	if(accountingNumber != null) {
				if(!isTransportCB) {
					fiscalDoc.setAccountNumber(fiscalDoc.getCentroCostos().concat(".").concat(accountingNumber.getStrValue1()));
				} else {
					fiscalDoc.setAccountNumber(fiscalDoc.getCentroCostos().concat(".").concat(accountingNumber.getStrValue2()));						
				}	
	    	} else {
	    		return "ForeignInvoiceError_11";
	    	}
			
	    	if(!AppConstants.DEFAULT_CURRENCY.equals(invCurrency)) {
	    		double exchangeRate;
	    		exchangeRate = this.getCurrentExchangeRate(new Date(), AppConstants.DEFAULT_CURRENCY_JDE, invCurrency);
				if(exchangeRate > 0D) {
					fiscalDoc.setTipoCambio(exchangeRate);					
				} else {
					return "ForeignInvoiceError_12";
				}	
	    	}
			
			int diasCredito = 0;
			UDC pmtTermsUDC = udcDao.searchBySystemAndKey("PMTTCUSTOM", "DEFAULT");
			if(pmtTermsUDC != null) {
				fiscalDoc.setPaymentTerms(pmtTermsUDC.getStrValue1());
				diasCredito = Integer.valueOf(pmtTermsUDC.getStrValue2());
			} else {
				fiscalDoc.setPaymentTerms("N30");
				diasCredito = 30;
			}
			
			String emailApprover = "";
			List<UDC> approverUDCList = udcDao.searchBySystem("APPROVERINV");
			if(approverUDCList != null) {
				for(UDC approver : approverUDCList) {
					if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
						fiscalDoc.setCurrentApprover(approver.getStrValue1());
						emailApprover = approver.getStrValue2();
					}

					if(AppConstants.INV_SECOND_APPROVER.equals(approver.getUdcKey())){
						fiscalDoc.setNextApprover(approver.getStrValue1());
					}				
				}
			}
	    	
			Date estimatedPaymentDate = null;
			Date currentDate = new Date();
			Calendar c = Calendar.getInstance();		
			c.setTime(currentDate);
			c.add(Calendar.DATE, diasCredito);
			List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(c.getTime(), 0, 500, s.getAddresNumber());
			if(pc != null) {
				if(pc.size() > 0) {
					estimatedPaymentDate = pc.get(0).getPaymentDate();
				}else {
					estimatedPaymentDate = c.getTime();
				}
			}else {
				estimatedPaymentDate = c.getTime();
			}
        	
			fiscalDoc.setEstimatedPaymentDate(estimatedPaymentDate);
			fiscalDoc.setInvoiceDate(fechaFacturaNueva);
			fiscalDoc.setCurrencyCode(invCurrency);
			fiscalDoc.setCurrencyMode(AppConstants.CURRENCY_MODE_FOREIGN);
			fiscalDoc.setGlOffset(AppConstants.GL_OFFSET_FOREIGN);
			fiscalDoc.setTaxCode(AppConstants.INVOICE_TAX0);
			fiscalDoc.setSerie("");
			fiscalDoc.setStatus(AppConstants.STATUS_INPROCESS);
			fiscalDoc.setApprovalStatus(AppConstants.STATUS_INPROCESS);
			fiscalDoc.setApprovalStep(AppConstants.FIRST_STEP);			
			fiscalDoc.setFolio(inv.getFolio());
			fiscalDoc.setSerie(inv.getSerie());
			fiscalDoc.setUuidFactura(inv.getUuid());
			fiscalDoc.setType(AppConstants.STATUS_FACT_FOREIGN);                	
			fiscalDoc.setRfcEmisor("");
			fiscalDoc.setSubtotal(inv.getForeignSubtotal());
			fiscalDoc.setAmount(inv.getForeignDebit());
			fiscalDoc.setMoneda(inv.getForeignCurrency());
			fiscalDoc.setDescuento(0);
			fiscalDoc.setImpuestos(0);			
        	fiscalDoc.setAdvancePayment(dblAdvancePayment);
        	fiscalDoc.setInvoiceUploadDate(new Date());
        	
        	//Create concept list
        	customBrokerService.createConceptList(uploadItem, fiscalDoc, false);
        	
        	//Save Fiscal Document
        	customBrokerService.saveDocument(fiscalDoc);
        	
        	//Save Concept Documents
        	documentsService.save(uploadItem, inv.getAddressNumber(), inv.getUuid());
			
			JAXBContext contextObj = JAXBContext.newInstance(ForeingInvoice.class);
			Marshaller marshallerObj = contextObj.createMarshaller();
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			StringWriter sw = new StringWriter();
			marshallerObj.marshal(inv, sw);
			String xmlString = sw.toString();

			UserDocument doc = new UserDocument();
			doc.setAddressBook(inv.getAddressNumber());
			doc.setDocumentNumber(0);
			doc.setDocumentType("Honorarios");
			doc.setContent(xmlString.getBytes("UTF-8"));
			doc.setType("TEMP");
			doc.setName(inv.getUuid() + ".xml");
			doc.setSize(xmlString.length());
			doc.setStatus(false);
			doc.setAccept(true);
			doc.setFiscalType("Factura");
			doc.setFolio(inv.getFolio());
			doc.setSerie("");
			doc.setUuid(inv.getUuid());
			doc.setUploadDate(new Date());
			doc.setFiscalRef(0);
			doc.setDescription("MainUUID_".concat(inv.getUuid()));
			documentsService.save(doc, new Date(), "");

			ForeignInvoiceTable fit = new ForeignInvoiceTable();
			fit.setAddressNumber(inv.getAddressNumber());
			fit.setOrderNumber(inv.getOrderNumber());
			fit.setOrderType(inv.getOrderType());
			fit.setCountry(inv.getCountry());
			fit.setInvoiceNumber(inv.getInvoiceNumber());
			fit.setExpeditionDate(fechaFacturaNueva);
			fit.setReceptCompany(inv.getReceptCompany());
			fit.setForeignCurrency(inv.getForeignCurrency());
			fit.setForeignSubtotal(inv.getForeignSubtotal());
			fit.setForeignTaxes(inv.getForeignTaxes());
			fit.setForeignRetention(inv.getForeignRetention());
			fit.setForeignDebit(inv.getForeignDebit());
			fit.setForeignDescription(inv.getForeignDescription());
			fit.setForeignNotes(inv.getForeignNotes());
			fit.setUsuarioImpuestos(inv.getUsuarioImpuestos());
			fit.setTaxId(inv.getTaxId());
			fit.setUuid(inv.getUuid());
			fit.setReceptCompany("");
			fit.setUsuarioImpuestos("");
			purchaseOrderDao.saveForeignInvoice(fit);
			
			try {
				String emailContent = AppConstants.EMAIL_INV_APPROVAL_MSG_1_NO_OC;
				  emailContent = emailContent.replace("_UUID_", inv.getUuid());
				  emailContent = emailContent.replace("_SUPPLIER_", s.getAddresNumber());
				
		    	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
		        emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_NO_OC, 
		        this.stringUtils.prepareEmailContent(emailContent +  "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK), emailApprover);
		        emailAsyncSup.setMailSender(mailSenderObj);
		        Thread emailThreadSup = new Thread(emailAsyncSup);
		        emailThreadSup.start();
			} catch (Exception e) {		
				log4j.error("Exception" , e);
			}
			
			return "";
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return e.getMessage();
		}
	}
		
	public String acceptForeignInvoice(ForeingInvoice inv) {

		PurchaseOrder po = purchaseOrderDao.searchbyOrderAndAddressBook(inv.getOrderNumber(), inv.getAddressNumber(),
				inv.getOrderType());
		if (po != null) {
			try {

				UserDocument current = null;
				List<UserDocument> list = documentsService.searchCriteriaByOrderNumber(inv.getOrderNumber(),
						inv.getOrderType(), inv.getAddressNumber(),true);
				for (UserDocument o : list) {
					if (o.getType().equals("TEMP")) {
						o.setType("FINAL");
						current = o;
						break;
					}
				}

				if (current != null) {
					
					if(inv.getUuid() == null) {
						inv.setUuid(getUuid(po));
					}
					
					JAXBContext contextObj = JAXBContext.newInstance(ForeingInvoice.class);
					Marshaller marshallerObj = contextObj.createMarshaller();
					marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

					StringWriter sw = new StringWriter();
					marshallerObj.marshal(inv, sw);
					String xmlString = sw.toString();
					current.setContent(xmlString.getBytes("UTF-8"));
					documentsService.update(current, new Date(), "");

					ForeignInvoiceTable fit = purchaseOrderDao.getForeignInvoice(inv);
					if (fit != null) {
						fit.setAddressNumber(inv.getAddressNumber());
						fit.setOrderNumber(inv.getOrderNumber());
						fit.setOrderType(inv.getOrderType());
						fit.setCountry(inv.getCountry());
						fit.setExpeditionDate(inv.getExpeditionDate());
						fit.setReceptCompany(inv.getReceptCompany());
						fit.setForeignCurrency(inv.getForeignCurrency());
						fit.setForeignSubtotal(inv.getForeignSubtotal());
						fit.setForeignTaxes(inv.getForeignTaxes());
						fit.setForeignRetention(inv.getForeignRetention());
						fit.setForeignDebit(inv.getForeignDebit());
						fit.setForeignDescription(inv.getForeignDescription());
						fit.setForeignNotes(inv.getForeignNotes());
						fit.setUsuarioImpuestos(inv.getUsuarioImpuestos());
						fit.setTaxId(inv.getTaxId());
						fit.setUuid(inv.getUuid());
						purchaseOrderDao.saveForeignInvoice(fit);
					}

				}

				po.setInvoiceAmount(inv.getForeignDebit());
				po.setStatus(AppConstants.STATUS_OC_INVOICED);
				po.setOrderStauts(AppConstants.STATUS_OC_INVOICED);
				purchaseOrderDao.updateOrders(po);

				String emailRecipient = "";
				UDC udc = udcService.getUdcById(Integer.valueOf(inv.getUsuarioImpuestos()));
				emailRecipient = udc.getStrValue1();
				
				String emailContent = AppConstants.EMAIL_FOREIGINVOICE_RECEIVED;
				emailContent = emailContent.replace("_DATA_", po.getOrderNumber() + "-" + po.getOrderType());

				EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				emailAsyncSup
						.setProperties(
								AppConstants.EMAIL_INV_ACCEPT_SUP + po.getOrderNumber() + "-" + po.getOrderType(),
								stringUtils.prepareEmailContent(emailContent),
								emailRecipient + "," + po.getEmail());
				emailAsyncSup.setMailSender(mailSenderObj);
				emailAsyncSup.setAdditionalReference(udcDao, po.getOrderType());
				Thread emailThreadSup = new Thread(emailAsyncSup);
				emailThreadSup.start();

				return "";

			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return e.getMessage();
			}
		} else {
			return "No existen órdenes de compra para la factura solicitada";
		}

	}

	public ForeingInvoice searchForeignInvoice(String addressBook, int orderNumber, String orderType) {

		UserDocument doc = documentsService.searchCriteriaByOrderNumberFiscalType(orderNumber, orderType, addressBook,
				"Factura");
		if (doc != null) {
			try {
				String docDetail = new String(doc.getContent());
				JAXBContext jaxbContext = JAXBContext.newInstance(ForeingInvoice.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

				StringReader reader = new StringReader(docDetail);
				ForeingInvoice inv = (ForeingInvoice) unmarshaller.unmarshal(reader);
				return inv;

			} catch (Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return null;
			}
		}

		return null;

	}

	public PurchaseOrder searchbyOrderAndAddressBook(int orderNumber, String addressNumber, String orderType) {
		return purchaseOrderDao.searchbyOrderAndAddressBook(orderNumber, addressNumber, orderType);
	}

	public List<LogData> getLogDataBayDate(Date startDate, Date endDate, String logType, int start, int limit) {
		return logger.getLogDataBayDate(startDate, endDate, logType, start, limit);
	}
	
	public List<InvoiceCodesDTO> getInvoiceCodes(int orderNumber, String addressNumber, String orderType){
		
		List<InvoiceCodesDTO> codes = new ArrayList<InvoiceCodesDTO>();
		try {
		String xmlContent = "";
		List<UserDocument> docList = documentsService.searchCriteriaByOrderNumber(orderNumber,orderType, addressNumber,true);
		if (docList != null) {
			if (!docList.isEmpty()) {
				for (UserDocument d : docList) {
					if(AppConstants.INVOICE_FIELD.equals(d.getFiscalType())){
						    InputStream is = new ByteArrayInputStream(d.getContent());
						    StringBuilder textBuilder = new StringBuilder();
						    try (Reader reader = new BufferedReader(new InputStreamReader
						    	      (is, Charset.forName(StandardCharsets.UTF_8.name())))) {
						    	        int c = 0;
						    	        while ((c = reader.read()) != -1) {
						    	            textBuilder.append((char) c);
						    	        }
						    	    }
					        xmlContent = textBuilder.toString();
					        if(!"".equals(xmlContent)) {
					        	InvoiceDTO inv =  null;
								if(xmlContent.contains(AppConstants.NAMESPACE_CFDI_V4)) {
									inv = xmlToPojoService.convertV4(xmlContent);
								} else {
									inv = xmlToPojoService.convert(xmlContent);
								}
					        	if(inv != null) {
					        		List<Concepto> conceptosFactura = inv.getConcepto();
					        		for(Concepto c : conceptosFactura) {
					        			InvoiceCodesDTO obj = new InvoiceCodesDTO();
					        			obj.setCode(c.getClaveProdServ());
					        			obj.setUom(c.getUnidad());
					        			obj.setDescription(c.getDescripcion());
					        			obj.setAmount(Double.valueOf(c.getImporte()));
					        			codes.add(obj);
					        		}
					        	}
					        }
					        break;
					}
					documentsService.delete(d.getId(),"");
				}
			}
		}
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		
		if(codes.size()>0) {
			List<String> codeList = new ArrayList<String>();
			for(InvoiceCodesDTO o : codes) {
				codeList.add(o.getCode());
			}
			List<CodigosSAT> codeListSAT = codigosSATService.findCodes(codeList);
			for(CodigosSAT o : codeListSAT) {
				for(InvoiceCodesDTO p : codes) {
					if(p.getCode().equals(o.getCodigoSAT())) {
						p.setDescriptionSAT(o.getDescripcion());
					}
				}
			}
		}
		
		return codes;
	}
	
	public List<Receipt> getPendingPaymentOrdersReceipt(int orderNumber, String addressBook, String orderType) {

		List<Receipt> invalidList = new ArrayList<Receipt>();
		List<Receipt> list = purchaseOrderDao.getPendingPaymentReceipts(orderNumber, addressBook, orderType);
		for (Receipt p : list) {
			if (p.getPaymentDate() != null) {
				try {
					Date currentDate = new Date();
					Calendar paymentCalendar = Calendar.getInstance();
					Calendar currentCalendar = Calendar.getInstance();
					Calendar paymentLimitCalendar = Calendar.getInstance();
					
					int paymentTargetDay = 3;
					
					paymentLimitCalendar.setTime(p.getPaymentDate());
					paymentCalendar.setTime(p.getPaymentDate());
					currentCalendar.setTime(currentDate);		
					currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
					currentCalendar.set(Calendar.MINUTE, 0);
					currentCalendar.set(Calendar.SECOND, 0);
					currentCalendar.set(Calendar.MILLISECOND, 0);
					
					paymentLimitCalendar.add(Calendar.DAY_OF_MONTH, paymentTargetDay);
					
				if(currentCalendar.compareTo(paymentLimitCalendar) > 0) {
					invalidList.add(p);
					continue;
				}

				} catch (Exception e) {
					log4j.error("Exception" , e);
					continue;
				}
				}
		}

		return invalidList;
	}
	
	
	private String getUuid(PurchaseOrder po) {
		StringBuilder str = new StringBuilder();
		String supNbr = org.apache.commons.lang.StringUtils.leftPad(po.getAddressNumber(),8,"0");
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
		str.append(org.apache.commons.lang.StringUtils.leftPad(po.getCompanyKey(),4,"0"));
		str.append(org.apache.commons.lang.StringUtils.leftPad(String.valueOf(po.getOrderNumber()),7,"0"));
		return str.toString();
	}
	
	public double getCurrentExchangeRate(Date invoiceDate, String currencyCode, String currencyTarget) {
    	double exchangeRate = 0D;
    	List<ExchangeRate> erToday = exchangeRateService.getExchangeRateFromToday(invoiceDate, currencyCode, currencyTarget, 0, 50);
		if(erToday != null && erToday.size() > 0) {
			exchangeRate = erToday.get(0).getExchangeRate();
		}
		
		if(exchangeRate == 0D) {
	    	List<ExchangeRate> erBefore = exchangeRateService.getExchangeRateBeforeToday(invoiceDate, currencyCode, currencyTarget, 0, 50);
	    	if(erBefore != null && erBefore.size() > 0) {
	    		exchangeRate = erBefore.get(0).getExchangeRate();
			}
		}

		if(exchangeRate == 0D) {
	    	List<ExchangeRate> erAfter = exchangeRateService.getExchangeRateBeforeToday(invoiceDate, currencyCode, currencyTarget, 0, 50);
	    	if(erAfter != null && erAfter.size() > 0) {
	    		exchangeRate = erAfter.get(0).getExchangeRate();
			}
		}
		
		return exchangeRate;
	}
	
	public List<Receipt> getOrderReceipts(int orderNumber,String addressBook, String orderType, String orderCompany) {
		return purchaseOrderDao.getOrderReceipts(orderNumber, addressBook, orderType, orderCompany);
	}
	
	public List<Receipt> getComplPendingReceipts(String addressBook) {
		Map<String, Receipt> rm = new HashMap<String, Receipt>();
		List<Receipt> returnList = new ArrayList<Receipt>();
		List<Receipt> receiptList = purchaseOrderDao.getComplPendingReceipts(addressBook);
		if(receiptList != null) {
			if(receiptList.size() > 0) {
				for(Receipt o : receiptList) {
					Receipt rec = rm.get(o.getUuid());
					if(rec != null) {
						continue;
					}else {
						rm.put(o.getUuid(), o);
					}
				}
			}
		}
		
		for (Map.Entry<String, Receipt> entry : rm.entrySet()) {
			returnList.add(entry.getValue());
		}

		return returnList;
	}
	
	public List<ReceiptInvoice> getComplPendingReceiptInvoice(String addressBook) {
		return purchaseOrderDao.getComplPendingReceiptInvoice(addressBook);
	}
	
	public List<Receipt> getNegativeOrderReceipts(int orderNumber,String addressBook, String orderType, String orderCompany) {
		return purchaseOrderDao.getNegativeOrderReceipts(orderNumber, addressBook, orderType, orderCompany);
	}
	public List<PurchaseOrderDetail> getCreditNotes(int orderNumber,String addressBook, String orderType, String orderCompany) {
		
		List<PurchaseOrderDetail> getCreditNotesList=purchaseOrderDao.getCreditNotes(orderNumber, addressBook, orderType, orderCompany);
		
		for (PurchaseOrderDetail purchaseOrderDetail : getCreditNotesList) {
			try {
				
				
				List<FiscalDocuments> doct=fiscalDocumentService.getFiscalDocumentsByPO(addressBook, orderNumber, "NOTACREDITO");
				

				
				if(doct.size()>0&&!doct.get(0).getStatus().equals("RECHAZADO")) {
					
//					agregar moneda al grid de op
//					fecha de vencimoento
//					fecha hora carga de factura
//					fecha de pago
//					uuid
//					estatus
					
					FiscalDocuments docs=doct.get(0);
					purchaseOrderDetail.setUuid(docs.getUuidFactura());
					purchaseOrderDetail.setCurrencyCode(docs.getCurrencyCode());
					purchaseOrderDetail.setStatus(docs.getStatus());
					List<Receipt> receiptArray = getReceiptsBycreditNoteUUID(docs.getUuidFactura());
	        		PurchaseOrder poOriginal=getOrderByOrderAndAddresBook(receiptArray.get(0).getOrderNumber(), receiptArray.get(0).getAddressNumber(), receiptArray.get(0).getOrderType());
					purchaseOrderDetail.setEstimatedPaymentDate(poOriginal.getEstimatedPaymentDate());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		return getCreditNotesList;
	}
	public List<PurchaseOrderDetail> getPurchaseOrderDetailByIds(String ids) {
		return purchaseOrderDao.getPurchaseOrderDetailByIds(ids);
	}
	public void updatePurchaseOrderDetail(PurchaseOrderDetail o) {
		 purchaseOrderDao.updatePurchaseOrderDetail(o);
		 return;
	}
	public List<Receipt> getSuplierInvoicedReceipts(String addressNumber, String uuid) {
		return purchaseOrderDao.getSuplierInvoicedReceipts(addressNumber, uuid);
	}
	
	public void updatePaymentReceipts(List<Receipt> list) {
		purchaseOrderDao.updatePaymentReceipts(list);
	}
	
	public List<Receipt> getReceiptsByUUID(String uuid) {
		return purchaseOrderDao.getReceiptsByUUID(uuid);
	}
	public List<Receipt> getReceiptsBycreditNoteUUID(String uuid) {
		return purchaseOrderDao.getReceiptsBycreditNoteUUID(uuid);
	}
	public void updateReceipt(Receipt o) {
		purchaseOrderDao.updateReceipt(o);
	}
	
	public void saveReceiptInvoice(ReceiptInvoice o) {
		purchaseOrderDao.saveReceiptInvoice(o);
	}
	
	public List<ReceiptInvoice> getReceiptsInvoiceByUUID(String uuid) {
		return purchaseOrderDao.getReceiptsInvoiceByUUID(uuid);
	}
	
	/*public String getPendingReceiptsComplPago(String addressNumber) {
		List<String> pendingList = new ArrayList<String>();
		List<Receipt> invalidList = new ArrayList<Receipt>();
		List<Receipt> list = purchaseOrderDao.getPendingReceiptsComplPago(addressNumber);
		for(Receipt r : list) {
			if(r.getPaymentDate() != null) {
				try {
					Date pDate = r.getPaymentDate();
					Date currentDate = new Date();

					Calendar paymentCalendar = Calendar.getInstance();
					Calendar currentCalendar = Calendar.getInstance();
					paymentCalendar.setTime(pDate);
					currentCalendar.setTime(currentDate);

					int paymentMonth = paymentCalendar.get(Calendar.MONTH) + 1;
					int currentMonth = currentCalendar.get(Calendar.MONTH) + 1;

					if (paymentMonth < 12) {
						int paymentTargetMonth = paymentMonth + 1;
						int paymentTargetDay = 10;

						// Same Year
						if (paymentCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) {
							if (currentMonth == paymentTargetMonth) {
								int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
								if (currentDay > paymentTargetDay) {
									invalidList.add(r);
									continue;
								}
							}
							if (currentMonth > paymentTargetMonth) {
								invalidList.add(r);
								continue;
							}

						}
						
						// Previous Year
						if (paymentCalendar.get(Calendar.YEAR) < currentCalendar.get(Calendar.YEAR)) {
							if (currentMonth == paymentTargetMonth) {
								int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
								if (currentDay > paymentTargetDay) {
									invalidList.add(r);
									continue;
								}
							}
							
							if (currentMonth < paymentTargetMonth) {
								invalidList.add(r);
								continue;
							}
						}

					} else {
						// Next year
						int paymentTargetMonth = 1;
						int paymentTargetDay = 10;
						if (currentCalendar.get(Calendar.YEAR) > paymentCalendar.get(Calendar.YEAR)) {
							if (currentMonth == paymentTargetMonth) {
								int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
								if (currentDay > paymentTargetDay) {
									invalidList.add(r);
									continue;
								}
							}
						}
					}

				} catch (Exception e) {
					continue;
				}
			}
			
		}

		if(invalidList != null) {
			if(invalidList.size()> 0) {
				for(Receipt r : invalidList) {
					if(!pendingList.contains(r.getUuid())) {
						pendingList.add(r.getUuid());
					}
				}
				return String.join(",", pendingList);
			}
		}	
		return "";
	}*/
	
	public String getPendingReceiptsComplPago(String addressNumber) {
		List<String> pendingList = new ArrayList<String>();
		List<Receipt> invalidList = new ArrayList<Receipt>();
		List<Receipt> list = purchaseOrderDao.getPendingReceiptsComplPago(addressNumber);
		for(Receipt r : list) {
			if(r.getPaymentDate() != null) {
				try {
					Date currentDate = new Date();
					Calendar paymentCalendar = Calendar.getInstance();
					Calendar currentCalendar = Calendar.getInstance();
					Calendar paymentLimitCalendar = Calendar.getInstance();
					
					int paymentTargetDay = 3;
					
					paymentLimitCalendar.setTime(r.getPaymentDate());
					paymentCalendar.setTime(r.getPaymentDate());
					currentCalendar.setTime(currentDate);		
					currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
					currentCalendar.set(Calendar.MINUTE, 0);
					currentCalendar.set(Calendar.SECOND, 0);
					currentCalendar.set(Calendar.MILLISECOND, 0);
					
					paymentLimitCalendar.add(Calendar.DAY_OF_MONTH, paymentTargetDay);
					
				if(currentCalendar.compareTo(paymentLimitCalendar) > 0) {
					invalidList.add(r);
					continue;
				}

				} catch (Exception e) {
					log4j.error("Exception" , e);
					continue;
				}
			}
			
		}

		if(invalidList != null) {
			if(invalidList.size()> 0) {
				for(Receipt r : invalidList) {
					if(!pendingList.contains(r.getUuid())) {
						pendingList.add(r.getUuid());
					}
				}
				return String.join(",", pendingList);
			}
		}	
		return "";
	}
	
	//@Scheduled(cron = "0 0/1 * * * ?") 
	//@Scheduled(cron = "0 30 1 * * ?")
	@Scheduled(cron = "0 0/30 * * * ?")
	public void getPendingReceiptsComplPagoList() {
		List<Receipt> list = purchaseOrderDao.getPendingReceiptsComplPagoList();
		for(Receipt r : list) {
			if(r.getPaymentDate() != null) {
				try {
					Date currentDate = new Date();
					Calendar paymentCalendar = Calendar.getInstance();
					Calendar currentCalendar = Calendar.getInstance();
					Calendar paymentLimitCalendar = Calendar.getInstance();
					
					int paymentTargetDay = 3;
					
					paymentLimitCalendar.setTime(r.getPaymentDate());
					paymentCalendar.setTime(r.getPaymentDate());
					currentCalendar.setTime(currentDate);		
					currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
					currentCalendar.set(Calendar.MINUTE, 0);
					currentCalendar.set(Calendar.SECOND, 0);
					currentCalendar.set(Calendar.MILLISECOND, 0);
					
					paymentLimitCalendar.add(Calendar.DAY_OF_MONTH, paymentTargetDay);
					
				if(currentCalendar.compareTo(paymentLimitCalendar) > 0 && (r.getReplicationComplPagoStatus() == null || "".contains(r.getReplicationComplPagoStatus()))) {
					r.setReplicationComplPagoStatus(AppConstants.STATUS_PENDING_REPLICATION);
					r.setActionReplication(AppConstants.BLOCK_ACTION);
					purchaseOrderDao.updateReceipt(r);
					continue;
				}

				} catch (Exception e) {
					log4j.error("Exception" , e);
					continue;
				}
			}
			
		}
	}
//	@Scheduled(cron = "0 0/30 * * * ?")
	public void getPendingReceiptsFactuList() {
		try {
			List<Receipt> list = purchaseOrderDao.getPendingReceiptsComplPagoList();
			List<Receipt> listFac = purchaseOrderDao.getPendingReceiptsFactuList();
			for (Receipt receipt : listFac) {
				Supplier are=supplierService.searchByAddressNumber(receipt.getAddressNumber());
				
				EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			      emailAsyncSup.setProperties("Archivos pendientes", this.stringUtils.prepareEmailContent("Estimado Proveedor:<br /> tiene recibos con pendiente de carga de factura"), are.getEmail());
			      emailAsyncSup.setMailSender(this.mailSenderObj);
			      Thread emailThreadSup = new Thread(emailAsyncSup);
			      emailThreadSup.start();
				
			}
			
for (Receipt receipt : list) {
	Supplier are=supplierService.searchByAddressNumber(receipt.getAddressNumber());
	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
    emailAsyncSup.setProperties("Archivos pendientes", this.stringUtils.prepareEmailContent("Estimado Proveedor:<br /> tiene recibos con pendiente de carga de complemento de pago"), are.getEmail());
    emailAsyncSup.setMailSender(this.mailSenderObj);
    Thread emailThreadSup = new Thread(emailAsyncSup);
    emailThreadSup.start();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	
	public Receipt unblockSupCompl(Receipt r) {
		try {
			Date currentDate = new Date();
			Calendar paymentCalendar = Calendar.getInstance();
			Calendar currentCalendar = Calendar.getInstance();
			Calendar paymentLimitCalendar = Calendar.getInstance();
			
			int paymentTargetDay = 3;
			
			paymentLimitCalendar.setTime(r.getPaymentDate());
			paymentCalendar.setTime(r.getPaymentDate());
			currentCalendar.setTime(currentDate);		
			currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currentCalendar.set(Calendar.MINUTE, 0);
			currentCalendar.set(Calendar.SECOND, 0);
			currentCalendar.set(Calendar.MILLISECOND, 0);
			
			paymentLimitCalendar.add(Calendar.DAY_OF_MONTH, paymentTargetDay);
			
		if(currentCalendar.compareTo(paymentLimitCalendar) > 0) {
			if(AppConstants.BLOCK_ACTION.contains(r.getActionReplication())){
				r.setActionReplication(AppConstants.UNBLOCK_ACTION);
				r.setReplicationComplPagoStatus(AppConstants.STATUS_PENDING_REPLICATION);
			}
		}

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		
		return r;
	}
	
	public PurchaseOrder searchbyOrderAndAddressBookAndCompany(int documentNumber, String addressBook,
			String documentType, String orderCompany) {
		return purchaseOrderDao.searchbyOrderAndAddressBookAndCompany(documentNumber, addressBook, documentType, orderCompany);
	}
	
	public ForeignInvoiceTable getForeignInvoiceFromOrder(PurchaseOrder o) {
		return purchaseOrderDao.getForeignInvoiceFromOrder(o);
	}

	public ForeignInvoiceTable getForeignInvoiceFromUuid(String uuid) {
		return purchaseOrderDao.getForeignInvoiceFromUuid(uuid);
	}
	
	public void saveForeignInvoice(ForeignInvoiceTable o) {
		purchaseOrderDao.saveForeignInvoice(o);
	}
	
	public void deleteForeignInvoice(ForeignInvoiceTable o) {
		purchaseOrderDao.deleteForeignInvoice(o);
	}
	
	public void deleteReceiptInvoice(ReceiptInvoice o) {
		purchaseOrderDao.deleteReceiptInvoice(o);
	}
	
	public List<Receipt> getBlockAndUnblocksSuppCompl(int start, int limit) {
		return purchaseOrderDao.getBlockAndUnblocksSuppCompl(start, limit);
	}

	public List<Receipt> getOrderReceiptsList(String addressNumber, String tipoDoc, String currencyCode, Date fromDate,
			Date toDate, int start, int limit) {
		return  purchaseOrderDao.getOrderReceiptsList(addressNumber, tipoDoc, currencyCode,fromDate,toDate,start,limit);
	}
	public long getTotalOrderReceiptsList(String addressNumber, String tipoDoc, String currencyCode, Date fromDate,
			Date toDate) {
		return  purchaseOrderDao.getTotalOrderReceiptsList(addressNumber, tipoDoc, currencyCode,fromDate,toDate);
	}

	public List<PaymentSupplier> getPaymentSupplierList(String addressNumber, String tipoDoc, String currencyCode,
			Date fromDate, Date toDate, String estatus, int start, int limit, String sort) {
		return paymentSupplierDao.getPaymentSupplierList(addressNumber, tipoDoc, currencyCode, fromDate, toDate,estatus, start,
				limit,sort);
	}
	
	public List<PaymentSupplierDetail> getPaymentSupplierDetailList(String idPayment) {
		return paymentSupplierDetailDao.getPaymentSupplierDetailList(idPayment);
	}

	public int getPaymentSupplierCount(String addressNumber, String tipoDoc, String currencyCode, String estatus, Date fromDate,
			Date toDate) {
		return paymentSupplierDao.getPaymentSupplierCount(addressNumber, tipoDoc, currencyCode, fromDate, toDate,estatus);
	}
}
