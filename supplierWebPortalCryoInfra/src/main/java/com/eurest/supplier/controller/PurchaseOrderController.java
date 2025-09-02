package com.eurest.supplier.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.eurest.supplier.dao.PaymentSupplierDao;
import com.eurest.supplier.dto.ForeingInvoice;
import com.eurest.supplier.dto.InvoiceCodesDTO;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.LogData;
import com.eurest.supplier.model.PaymentCalendar;
import com.eurest.supplier.model.PaymentSupplier;
import com.eurest.supplier.model.PaymentSupplierDetail;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.PurchaseOrderDetail;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.service.DocumentsService;
import com.eurest.supplier.service.EmailService;
import com.eurest.supplier.service.FTPService;
import com.eurest.supplier.service.FiscalDocumentService;
import com.eurest.supplier.service.CustomBrokerService;
import com.eurest.supplier.service.PaymentCalendarService;
import com.eurest.supplier.service.PurchaseOrderService;
import com.eurest.supplier.service.SupplierService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.service.UsersService;
import com.eurest.supplier.service.XmlToPojoService;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.FileConceptUploadBean;
import com.eurest.supplier.util.Logger;
import com.eurest.supplier.util.StringUtils;

import net.sf.json.JSONObject;


@Controller
public class PurchaseOrderController {
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	PaymentCalendarService paymentCalendarService;
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	XmlToPojoService xmlToPojoService;
	
	@Autowired
	Logger logger;
	
	@Autowired
	DocumentsService documentsService;
	
	@Autowired
	JavaMailSender mailSenderObj;

	  @Autowired
	  EmailService emailService;
	  
	  @Autowired
	  UsersService userService;
	  
	  @Autowired
	  StringUtils stringUtils;
	
	  @Autowired
	  FiscalDocumentService fiscalDocumentService;

	  @Autowired
	  CustomBrokerService customBrokerService;
	  
	
	  
	  org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(PurchaseOrderController.class);
	  	
	@RequestMapping(value ="/supplier/orders/view.action")
	public @ResponseBody Map<String, Object> view(@RequestParam int start,
												  @RequestParam int limit,
												  HttpServletRequest request){	
		try{
			List<PurchaseOrder> list=null;
			int total=0;
			//list = purchaseOrderService.getOrders(start, limit);
			list = null;
			total = purchaseOrderService.getTotalRecords();				
		    return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
		
	}

	@RequestMapping(value ="/supplier/orders/searchOrders.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchOrders(
												  @RequestParam String poNumber, 
												  @RequestParam String supNumber,
												  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date poFromDate,
												  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date poToDate,
												  @RequestParam String poTypeCombo,
												  @RequestParam String status,
												  @RequestParam String userName,
												  @RequestParam int start,
												  @RequestParam int limit){	
		List<PurchaseOrder> list = null;
		int total=0;
		try{

			String usr = userName;
			String role = usersService.searchCriteriaUserName(usr).getRole();
			String email = "";
			String foreginSearch = "";
		
			if(org.apache.commons.lang.StringUtils.isBlank(poNumber)) {
				poNumber = "0";
			} else {
				poNumber = poNumber.replaceAll("[^0-9]", "");				
				if (!org.apache.commons.lang.StringUtils.isNumeric(poNumber)) {
					poNumber = "0";
				}
			}
			
			list = purchaseOrderService.searchbyOrderNumber(Integer.valueOf(poNumber), supNumber, 
															poFromDate, poToDate,poTypeCombo, status, start, limit, role,
															email,foreginSearch); 
			
			total = purchaseOrderService.getTotalRecords(supNumber, Integer.valueOf(poNumber), poFromDate, poToDate,poTypeCombo, status, role, email,foreginSearch);
			if(list != null && !list.isEmpty()) {
				for(PurchaseOrder po : list) {
					String notes = "";
					List<FiscalDocuments> fdList = fiscalDocumentService.getFiscalDocumentsByPO(po.getAddressNumber(), po.getOrderNumber(), "FACTURA");
					if(fdList != null && !fdList.isEmpty()) {
						for(FiscalDocuments fd : fdList) {
							if(fd.getNoteRejected() != null && !fd.getNoteRejected().isEmpty()) {
								String uuid = fd.getUuidFactura().length() > 36 ? fd.getUuidFactura().substring(0, 36):fd.getUuidFactura();
								notes += fd.getApprovalStatus() + ": " + uuid + "<br>";
								notes += fd.getNoteRejected() + "<br><br>";
							}
						}
					}
					po.setNotes(notes);
				}
			}
			return mapOK(list, total);
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/supplier/orders/searchEmailedOrders.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchEmailedOrders(
												  @RequestParam String poNumber, 
												  @RequestParam String supNumber,
												  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date poFromDate,
												  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date poToDate,
												  @RequestParam String status,
												  @RequestParam int start,
												  @RequestParam int limit){	
		List<PurchaseOrder> list = null;
		int total=0;
		try{
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String usr = auth.getName();	
			String role = usersService.searchCriteriaUserName(usr).getRole();
			String email = "";
			String foreginSearch = "";
			
			if(role.equals(AppConstants.ROLE_PURCHASE) || role.equals(AppConstants.ROLE_TAX)) {
				email = usersService.searchCriteriaUserName(usr).getEmail();
			}
			
			if(role.equals(AppConstants.ROLE_TAX)) {
				foreginSearch = "Y";
			}
			
			list = purchaseOrderService.searchbyOrderNumber(Integer.valueOf(poNumber), supNumber, 
															poFromDate, poToDate,null, status, start, limit, role, email,foreginSearch); 
			total = purchaseOrderService.getTotalRecords(supNumber, Integer.valueOf(poNumber), poFromDate, poToDate,null, status, role, email,foreginSearch);
			return mapOK(list, total);
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/supplier/orders/searchCriteria.action")
	public @ResponseBody Map<String, Object> searchByStatus(@RequestParam int start,
												  @RequestParam int limit,
												  @RequestParam String addressBook, 
												  @RequestParam String status, 
												  @RequestParam String orderStatus){	
		List<PurchaseOrder> list = null;
		int total=0;
		try{
				list = purchaseOrderService.searchCriteria(addressBook, status, orderStatus); 
				total = list.size();
				return mapOK(list, total);
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	} 
	
	
	@RequestMapping(value ="/supplier/orders/update.action")
	public @ResponseBody Map<String, Object> update(@RequestBody PurchaseOrder obj){
		
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			@SuppressWarnings("unused")
			String usr = auth.getName();
			
			if(obj.getId() != 0) {
				PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(obj.getOrderNumber(), obj.getAddressNumber(), obj.getOrderType());
				po.setPurchasOrderDetail(obj.getPurchaseOrderDetail());		
				purchaseOrderService.updateOrders(obj);
			}

			return mapStrOk("El registro se actualizó de forma correcta");
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/supplier/orders/save.action")
	public @ResponseBody Map<String, Object> save(@RequestBody PurchaseOrder obj){
		
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			@SuppressWarnings("unused")
			String usr = auth.getName();
			if(obj.getId() != 0) {
				PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(obj.getOrderNumber(), obj.getAddressNumber(), obj.getOrderType());
				po.setPurchasOrderDetail(obj.getPurchaseOrderDetail());		
				purchaseOrderService.updateOrders(obj);
			}
			return mapStrOk("El registro se actualizó de forma correcta");
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/supplier/receipt/receipt.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> receipt(@RequestBody PurchaseOrderDetail[] obj,
												     @RequestParam int orderId,						     
												     HttpServletRequest request){
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			@SuppressWarnings("unused")
			String usr = auth.getName();
			purchaseOrderService.saveReceipts(obj,  orderId);

			return mapStrOk("El recibo se actualizó de forma correcta");
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}

	@RequestMapping(value ="/supplier/orders/replicatePurchaseOrderBySelection.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> replicatePurchaseOrderBySelection(@RequestParam int orderNumber,
																	   @RequestParam String addressNumber,
																	   @RequestParam String fromDate,
																	   @RequestParam String toDate){	

		try{
			purchaseOrderService.getPurchaseOrderListBySelection(orderNumber, addressNumber, fromDate, toDate);
			return mapStrOk("OK");  
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/supplier/orders/confirmEmailOrders.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> confirmEmailOrders(@RequestBody PurchaseOrder[] selected,					     
												     HttpServletRequest request,
												     HttpServletResponse response){
		try{
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8"); 
			if(purchaseOrderService.confirmEmailOrders(selected)) {
				return mapStrOk("Las ordenes fueron procesadas exitosamente");
			}else {
				return mapStrOk("Ha ocurrido un error al intentar enviar las órdenes. Revise la configuración del servidor SMTP");
			}

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/supplier/orders/confirmOrderInvoice.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> confirmOrderInvoice(@RequestBody PurchaseOrder[] selected,					     
		     HttpServletRequest request,
		     HttpServletResponse response){
		try{
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8"); 
			if(purchaseOrderService.confirmOrderInvoices(selected)) {
				return mapStrOk("Las facturas fueron procesadas exitosamente");
			}else {
				return mapStrOk("Ha ocurrido un error al intentar enviar las órdenes. Revise la configuración del servidor SMTP");
			}

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/supplier/orders/rejectOrderInvoice.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> rejectOrderInvoice(@RequestBody PurchaseOrder[] selected,	
			                                                    @RequestParam boolean sendEmailToSupplier,
			                                                    @RequestParam String rejectType,
															     HttpServletRequest request,
															     HttpServletResponse response){
		try{
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8"); 
			
				
			if(purchaseOrderService.rejectOrderInvoices(selected, sendEmailToSupplier, rejectType)) {
				return mapStrOk("Las facturas fueron procesadas exitosamente");
			}else {
				return mapStrOk("Ha ocurrido un error al intentar enviar las órdenes. Revise la configuración del servidor SMTP");
			}

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/supplier/orders/importPayments.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> importPayments(HttpServletRequest request, HttpServletResponse response){
		try{
		    purchaseOrderService.paymentImportBulk();
			return mapStrOk("La carga se ha solicitado de forma exitosa. Espere unos minutos para que el prceso en batch termine.");
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/supplier/orders/getSupplierName.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getSupplierName(HttpServletRequest request, String addressNumber){
		try{
		    Supplier s = supplierService.searchByAddressNumber(addressNumber);
			return mapStrOk(s.getName());
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/supplier/orders/saveForeign.action", produces={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody public String  saveForeign(@RequestBody ForeingInvoice inv){
		
		JSONObject json = new JSONObject();
		try{
			String r = purchaseOrderService.createForeignInvoice(inv);
			if(!"".equals(r)) {
				json.put("success", false);
	            json.put("message", r);
	            return json.toString();
			}else {
				json.put("success", true);
	            json.put("message", "");
				return json.toString();
			}
		    
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			json.put("success", false);
            json.put("message", e.getMessage());
			return json.toString();

		}
	}
	
	@RequestMapping(value = "/supplier/orders/uploadForeignInvoiceWithoutOrder.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String saveForeignFD(FileConceptUploadBean uploadItem,
    										  ForeingInvoice inv,
    										  BindingResult result,
    										  String currentUuid,
    										  String foreignSubtotal,
    										  String foreignDebit,    										  
    										  String invoiceNumber,
    										  String foreignCompany,
    										  String advancePayment,
								    		  HttpServletResponse response){
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
		JSONObject json = new JSONObject();
		
		try{
			double dblForeignSubtotal = currencyToDouble(foreignSubtotal);
			double dblForeignDebit = currencyToDouble(foreignDebit);
			double dblAdvancePayment = currencyToDouble(advancePayment);

        	inv.setForeignSubtotal(dblForeignSubtotal);
        	inv.setForeignDebit(dblForeignDebit);
        	inv.setFolio(invoiceNumber);
        	inv.setUuid(currentUuid);
        	inv.setReceptCompany(foreignCompany);
        	
			String r = purchaseOrderService.createForeignInvoiceWithoutOrder(uploadItem, inv, dblAdvancePayment);
			
			if(!"".equals(r)) {
				json.put("success", false);
	            json.put("message", r);
	            return json.toString();
			}else {
				json.put("success", true);
	            json.put("message", "");
				return json.toString();
			}
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			json.put("success", false);
            json.put("message", e.getMessage());
			return json.toString();
		}
    }

	@RequestMapping(value = "/supplier/orders/uploadForeignInvoiceWithoutOrderCB.action", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String saveForeignCB(FileConceptUploadBean uploadItem,
    										  ForeingInvoice inv,
    										  BindingResult result,
    										  String currentUuid,
    										  String foreignSubtotal,
    										  String foreignDebit,    										  
    										  String invoiceNumber,
    										  String foreignCompany,
    										  String advancePayment,
								    		  HttpServletResponse response){
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
		JSONObject json = new JSONObject();
		
		try{
			double dblForeignSubtotal = currencyToDouble(foreignSubtotal);
			double dblForeignDebit = currencyToDouble(foreignDebit);
			double dblAdvancePayment = currencyToDouble(advancePayment);

        	inv.setForeignSubtotal(dblForeignSubtotal);
        	inv.setForeignDebit(dblForeignDebit);
        	inv.setFolio(invoiceNumber);
        	inv.setUuid(currentUuid);
        	inv.setReceptCompany(foreignCompany);
        	
			String r = purchaseOrderService.createForeignInvoiceWithoutOrderCB(uploadItem, inv, dblAdvancePayment);
			
			if(!"".equals(r)) {
				json.put("success", false);
	            json.put("message", r);
	            return json.toString();
			}else {
				json.put("success", true);
	            json.put("message", "");
				return json.toString();
			}
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			json.put("success", false);
            json.put("message", e.getMessage());
			return json.toString();
		}
    }
    	
	@RequestMapping(value ="/supplier/orders/getForeignInvoice.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getForeignInvoice(@RequestParam String addressBook, 
															   @RequestParam int orderNumber, 
															   @RequestParam String orderType){
		try{
			ForeingInvoice inv = purchaseOrderService.searchForeignInvoice(addressBook, orderNumber, orderType);
			return mapForeignOK(inv);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/supplier/orders/searchOrdersByBuyer.action")
	public @ResponseBody Map<String, Object> searchOrdersByBuyer(@RequestParam String buyerEmail){	
		List<PurchaseOrder> list = null;
		int total=0;
		try{
				list = purchaseOrderService.searchCriteriaByEmail(buyerEmail);
				total = list.size();
				return mapOK(list, total);
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	} 
	
	@RequestMapping(value ="/supplier/orders/reasignOrders.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> reasignOrders(@RequestBody PurchaseOrder[] selected,					     
														   HttpServletRequest request,
														   HttpServletResponse response){
		try{
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8"); 
			if(purchaseOrderService.reasignOrders(selected)) {
				return mapStrOk("Las ordenes fueron procesadas exitosamente");
			}else {
				return mapStrOk("Ha ocurrido un error al intentar procesar las órdenes. Revise la configuración del servidor SMTP");
			}

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
		

	@RequestMapping(value ="/supplier/orders/searchEstimatedPaymentDate.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchEstimatedPaymentDate(String date, String addressNumber, String pmtType){	

		try{
			
			Date baseDate =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date);  
			DateFormat retDateFmt =new SimpleDateFormat("yyyy-MM-dd");
			Supplier s = supplierService.searchByAddressNumber(addressNumber);
			Calendar cal = Calendar.getInstance();
			cal.setTime(baseDate);
			cal.add(Calendar.DATE, Integer.valueOf(s.getDiasCredito()));
			
			if(AppConstants.FACTURA_PUE.equals(pmtType)) {
				int pueOffSetDays = 0;
				UDC pueOffSet = udcService.searchBySystemAndKey(AppConstants.FACTURA_PUE, AppConstants.OFFSET_DAYS);
				if(pueOffSet != null) {
					if(pueOffSet.getStrValue1() != null) {
						pueOffSetDays = Integer.valueOf(pueOffSet.getStrValue1());
					}
				}
			
			cal.add(Calendar.DATE, pueOffSetDays);
			}
			
			Date specDate = cal.getTime();
			
			List<PaymentCalendar> pcFromToday = paymentCalendarService.getPaymentCalendarFromToday(new Date(), 0, 10, addressNumber);
			
			List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(specDate, 0, 5, addressNumber);
			if(pc != null) {
				String fmtDate = "";
				Date calcDate = pc.get(0).getPaymentDate();
				Date today = new Date();
				if(pcFromToday.size() > 0) {
					if(calcDate.before(today)) {
						fmtDate = retDateFmt.format(pcFromToday.get(0).getPaymentDate());
					}else {
						fmtDate = retDateFmt.format(pc.get(0).getPaymentDate());
					}
				}
				
				return mapStrOk(fmtDate);
			}
			
			
			return mapStrOk("");
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	} 
	
	@RequestMapping(value ="/orders/invoice/getInvoiceCodes.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getInvoiceCodes(@RequestParam String addressBook, 
															 @RequestParam int orderNumber, 
															 @RequestParam String orderType){	

		try{
			List<InvoiceCodesDTO> codes = purchaseOrderService.getInvoiceCodes(orderNumber, addressBook, orderType);
			return mapStrOkInvCodes(codes,codes.size());
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	} 
	
	
	@RequestMapping(value ="/orders/log/getLog.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> logList(@RequestParam String fromDate, 
			                                         @RequestParam String toDate,
			                                         @RequestParam String logType,
			                                         @RequestParam int start,
													 @RequestParam int limit){	

		try{
			Date fDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(fromDate); 
			Date tDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(toDate); 
			List<LogData> pc = purchaseOrderService.getLogDataBayDate(fDate, tDate, logType, 0, 100);
			if(pc != null) {
				return mapLogOK(pc, pc.size());
			}
			return mapStrOk("");
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	} 
	
	@RequestMapping(value ="/supplier/orders/loadFTPInvoices.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> loadFTPInvoices(){	

		try{
			
			UDC host = udcService.searchBySystemAndKey("FTP", "HOST");
			UDC user = udcService.searchBySystemAndKey("FTP", "USER");
			UDC pass = udcService.searchBySystemAndKey("FTP", "PASS");
			UDC port = udcService.searchBySystemAndKey("FTP", "PORT");
			
			FTPService ftpService = new FTPService();
			ftpService.setServices(xmlToPojoService, purchaseOrderService, documentsService, udcService, logger);
			ftpService.setFtpParams(host.getStrValue1(), user.getStrValue1(), pass.getStrValue1(), Integer.valueOf(port.getStrValue1()));
			Thread t = new Thread(ftpService);
			t.start();
			return mapStrOk("OK");
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	
	@RequestMapping(value ="/report/orders/searchOrdersReport.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchOrdersReport(
												  @RequestParam String poNumber, 
												  @RequestParam String supNumber,
												  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date poFromDate,
												  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date poToDate,
												  @RequestParam String status,
												  @RequestParam String userName,
												  @RequestParam int start,
												  @RequestParam int limit){	
		List<PurchaseOrder> list = null;
		int total=0;
		try{

			String usr = userName;
			String role = usersService.searchCriteriaUserName(usr).getRole();
			String email = "";
			
		
			list = purchaseOrderService.searchbyOrderNumber(Integer.valueOf(poNumber), supNumber, 
																poFromDate, poToDate,null, status, start, limit, role,
																email,""); 
				
			total = purchaseOrderService.getTotalRecords(supNumber, Integer.valueOf(poNumber), poFromDate, poToDate,null, status, role, email,"");
			return mapOK(list, total);
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	
	@RequestMapping(value ="/receipt/getOrderReceipts.action")
	public @ResponseBody Map<String, Object> searchByStatus(
												  @RequestParam String addressBook, 
												  @RequestParam int orderNumber, 
												  @RequestParam String orderType,
												  @RequestParam String orderCompany){	
		List<Receipt> list = new ArrayList<Receipt>();
		List<Receipt> listReceipt = new ArrayList<Receipt>();		
		List<Integer> receiptNumList = new ArrayList<Integer>();
		List<UserDocument> listDocument = new ArrayList<UserDocument>();
		int total=0;
		try{
				listReceipt = purchaseOrderService.getOrderReceipts(orderNumber, addressBook, orderType, orderCompany);
				listDocument = documentsService.searchCriteriaByOrderNumber(orderNumber, orderType, addressBook,false);
				if(listReceipt != null && !listReceipt.isEmpty()) {
					for(Receipt r : listReceipt) {
						if(receiptNumList.contains(r.getDocumentNumber())) {							
							for(Receipt receipt : list) {
								if(receipt.getDocumentNumber() == r.getDocumentNumber()) {
									receipt.setAmountReceived(receipt.getAmountReceived() + r.getAmountReceived());
									receipt.setForeignAmountReceived(receipt.getForeignAmountReceived() + r.getForeignAmountReceived());
									receipt.setQuantityReceived(receipt.getQuantityReceived() + r.getQuantityReceived());
									break;
								}
							}
						} else {
							//Asigna fecha de carga
							if(r.getUuid() != null && !r.getUuid().trim().isEmpty() && listDocument != null && !listDocument.isEmpty()) {
								for(UserDocument d : listDocument) {
									if(r.getUuid().equals(d.getUuid())) {
										r.setUploadDate(d.getUploadDate());
										break;
									}
								}
							}
							list.add(r);
							receiptNumList.add(r.getDocumentNumber());
						}
					}
				}
				total = list.size();
				return mapReceiptOK(list, total);
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	} 
	
	@RequestMapping(value ="/receipt/getComplReceiptsByStatus.action")
	public @ResponseBody Map<String, Object> getOrderReceiptsByStatus(
												  @RequestParam String addressBook){	
		List<Receipt> list = null;
		int total=0;
		try{
				list = purchaseOrderService.getComplPendingReceipts(addressBook);
				total = list.size();
				return mapReceiptOK(list, total);
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	} 
	
	@RequestMapping(value ="/receipt/getCreditNotes.action")
	public @ResponseBody Map<String, Object> getCreditNotes(
												  @RequestParam String orderCompany, 									  
												  @RequestParam String addressBook, 
												  @RequestParam int orderNumber, 
												  @RequestParam String orderType){	
		List<PurchaseOrderDetail> list = null;
		int total=0;
		try{
				list = purchaseOrderService.getCreditNotes(orderNumber, addressBook, orderType, orderCompany);
				total = list.size();
				return mapPurchaseOrderDetailOK(list, total);
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value ="/receipt/getPaymentSupplierConsulta.action")
	public @ResponseBody Map<String, Object> getPaymentSupplierConsulta(
			  @RequestParam int start,
			  @RequestParam int limit,
			  @RequestParam String addressNumber,
			  @RequestParam String tipoDoc,
			  @RequestParam String currencyCode,
			  @RequestParam String estatus,
			  @RequestParam String pfromDate,
			  @RequestParam String ptoDate,
			  @RequestParam(required = false) String sort, // Parámetro de ordenación
			  HttpServletRequest request){
		
		List<PaymentSupplier> listPaymeSup = new ArrayList<PaymentSupplier>();
		int total=0;
		try{
			Date fromDate=pfromDate!=null&&(!pfromDate.trim().equals(""))?new SimpleDateFormat("yyyy-MM-dd").parse(pfromDate):null;
			Date toDate=ptoDate!=null&&(!ptoDate.trim().equals(""))?new SimpleDateFormat("yyyy-MM-dd").parse(ptoDate):null;
			listPaymeSup = purchaseOrderService.getPaymentSupplierList(addressNumber, tipoDoc, currencyCode,fromDate,toDate,estatus,start,limit,sort);
				total = (int) purchaseOrderService.getPaymentSupplierCount(addressNumber, tipoDoc, currencyCode,estatus,fromDate,toDate);
				return mapPaymentSupplierOK(listPaymeSup, total);
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	} 
	
	
	@RequestMapping(value ="/receipt/getPaymentSupplierDetailConsulta.action")
	public @ResponseBody Map<String, Object> getPaymentSupplierDetailConsulta(
			  @RequestParam String idPayment, 
			  HttpServletRequest request){
		
		List<PaymentSupplierDetail> listPaymeSup = new ArrayList<PaymentSupplierDetail>();
		int total=0;
		try{
			
			listPaymeSup = purchaseOrderService.getPaymentSupplierDetailList(idPayment);
				return mapPaymentSupplierOK(listPaymeSup);
		        
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	} 
	
	
	
	private Map<String, Object> mapPaymentSupplierOK(List<PaymentSupplierDetail> list) {
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", list.size());
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String,Object> mapReceiptOK(List<Receipt> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	public Map<String,Object> mapPaymentSupplierOK(List<PaymentSupplier> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String,Object> mapOK(List<PurchaseOrder> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	
	
	public Map<String,Object> mapLogOK(List<LogData> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String,Object> mapStrOkInvCodes(List<InvoiceCodesDTO> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String,Object> mapOK(PurchaseOrder obj){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", 1);
		modelMap.put("data", obj);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String,Object> mapForeignOK(ForeingInvoice obj){
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

	public Map<String,Object> mapErrorResponse(String msg){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("msg", msg);
		modelMap.put("success", false);
		return modelMap;
	} 
	
	public Map<String,Object> mapStrOk(String msg){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", true);
		return modelMap;
	} 
	
	public Map<String,Object> mapResponse(String msg){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("msg", msg);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public double currencyToDouble(String amount) {
		return Double.valueOf(amount.replace("$", "").replace(",", "").replace(" ", ""));
	}

	public Map<String,Object> mapPurchaseOrderDetailOK(List<PurchaseOrderDetail> list, int total){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	}
