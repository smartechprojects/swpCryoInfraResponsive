package com.eurest.supplier.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eurest.supplier.dao.UDCDao;
import com.eurest.supplier.dto.BlockerSupReceiptDTO;
import com.eurest.supplier.dto.FileDTO;
import com.eurest.supplier.edi.BatchJournalDTO;
import com.eurest.supplier.edi.VoucherHeaderDTO;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.service.ApprovalService;
import com.eurest.supplier.service.DocumentsService;
import com.eurest.supplier.service.EDIService;
import com.eurest.supplier.service.FiscalDocumentService;
import com.eurest.supplier.service.MiddlewareService;
import com.eurest.supplier.service.PurchaseOrderService;
import com.eurest.supplier.service.SupplierService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.service.UsersService;
import com.eurest.supplier.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MiddlewareController {
	
	  @Autowired
	  SupplierService supplierService;
	  
	  @Autowired
	  PurchaseOrderService purchaseOrderService;
	  
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
	  MiddlewareService middlewareService;
	  
	  @Autowired
	  FiscalDocumentService fiscalDocumentService;
	  
	  Logger log4j = Logger.getLogger(MiddlewareController.class);
	//Vacio
	/*@RequestMapping(value ="/middleware/getInvoiceForPayment")
	public @ResponseBody Map<String, Object> getInvoiceForPayment(@RequestParam int start,
												     @RequestParam int limit,
												     @RequestParam String status,
												     @RequestParam String step,
												     HttpServletRequest request){	
		int total=100;
		try{
		    return mapOK("Respuesta OK getInvoiceForPayment",total);
		} catch (Exception e) {
			e.printStackTrace();
			return mapError(e.getMessage() + " - getInvoiceForPayment");
		}
	}*/
	
	//public ResponseEntity<Map<String, Object>> setPayments(@RequestParam("receipList") String receipList){
	@RequestMapping(value ="/middleware/setPayments", method=RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setPayments(@RequestBody List<Receipt> receipList){	
		try {
			Map<String, Object> response = middlewareService.setPayents(receipList);

			return ResponseEntity.ok().body(response);

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - setPayments"));
		}

	}
	
	@RequestMapping(value ="/middleware/getPaymentPendingReceipts")
	public @ResponseBody Map<String, Object> getPaymentPendingReceipts(@RequestParam int start,
												     @RequestParam int limit,
												     HttpServletRequest request){	
		
		try{
			Map<String, Object> response = middlewareService.getPaymentPendingReceipts(start, limit);
			return response;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getPaymentPendingReceipts");
			
		}
	}

	@RequestMapping(value ="/middleware/getSupplierForPO", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getSupplierForPO(@RequestBody List<PurchaseOrder> supList){
		
		try{
			Map<String, Object> response = middlewareService.getSupplierForPO(supList);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			//return e.toString();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - getSupplierForPO"));
		}
	}
	//Vacio
	
	//public ResponseEntity<Map<String, Object>> setPoListHistory(@RequestParam("poList") String poList){
	@RequestMapping(value ="/middleware/setPoListHistory",method = RequestMethod.POST)
	 public ResponseEntity<Map<String, Object>> setPoListHistory(@RequestBody List<PurchaseOrder> poList){
			try{
			Map<String, Object> response = middlewareService.setPoListHistory(poList);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - getSupplierForPO"));
		}
	}
	
	@RequestMapping(value ="/middleware/setPoReceiptsHistory" , method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> setPoReceiptsHistory(@RequestBody List<Receipt> recpList){	
		
		try{
			Map<String, Object> response = middlewareService.setPoReceiptsHistory(recpList);

			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - getSupplierForPO"));
		}
	}
	//REVISION
	@RequestMapping(value ="/middleware/getPostVoucher")
	public @ResponseBody Map<String, Object> getPostVoucher(@RequestParam int start,
												     @RequestParam int limit,
												     HttpServletRequest request){	
		try{
			Map<String, Object> response = middlewareService.getPostVoucher(start,limit);
		    return response;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getPostVoucher");
		}
	}
	//Vacio
	//public ResponseEntity<Map<String, Object>> updatePostVoucher(@RequestParam("batchJournalList") String batchJournalList) {
	@RequestMapping(value ="/middleware/updatePostVoucher", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> updatePostVoucher(@RequestBody List<VoucherHeaderDTO> batchJournalList) {	
		try{
			Map<String, Object> response = middlewareService.updatePostVoucher(batchJournalList);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - updateJournalEntries"));
		}
	}
	//Vacio
	@RequestMapping(value ="/middleware/getPostJournalEntries")
	public @ResponseBody Map<String, Object> getPostJournalEntries(@RequestParam int start,
												     @RequestParam int limit,
												     HttpServletRequest request){	
		try{
			Map<String, Object> response = middlewareService.getPostJournalEntries(start,limit);
		    return response;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getPostJournalEntries");
		}
	}
	//Vacio

	@RequestMapping(value ="/middleware/updateJournalEntries", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> updateJournalEntries(@RequestBody List<BatchJournalDTO> batchJournalDTO){
		try{
			Map<String, Object> response = middlewareService.updateJournalEntries(batchJournalDTO);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - updateJournalEntries"));
		}
	}
	
	@RequestMapping(value ="/middleware/getPostAddressBook")
	public @ResponseBody Map<String, Object> getPostAddressBook(@RequestParam int start,
												     @RequestParam int limit,
												     HttpServletRequest request){	
		try{
			Map<String, Object> response = middlewareService.getPostAddressBook(start,limit);
		    return response;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getSuppliers");
		}
	}
	
	
	/*@RequestMapping(value ="/middleware/updatePostVoucher", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> updatePostVoucher(@RequestBody List<VoucherHeaderDTO> batchJournalList) {	
		try{
			Map<String, Object> response = middlewareService.updatePostVoucher(batchJournalList);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - updateJournalEntries"));
		}
	}*/
	
	/*@RequestMapping(value ="/postReceivingAdvice", method = RequestMethod.POST,consumes="application/json")
	public ResponseEntity<ReceivingAdviceHeaderDTO> postReceivingAdvice(@RequestBody ReceivingAdviceHeaderDTO o) {
		ReceivingAdviceHeaderDTO obj = JDEService.insertReceivingAdvice(o);
	    return ResponseEntity.ok().body(obj);
	}*/
	
	@RequestMapping(value ="/middleware/updatePostAddressBook")
	//public ResponseEntity<Map<String, Object>> updatePostAddressBook(@RequestBody List<SupplierJdeDTO> supplierList){
		//public ResponseEntity<String> updatePostAddressBook(@RequestBody List<SupplierJdeDTO> supplierList){	
	
	public @ResponseBody Map<String, Object> updatePostAddressBook(@RequestParam int id,
												     @RequestParam String addressBook,
												     HttpServletRequest request){	
		try{
		    
			//Map<String, Object> response = middlewareService.updatePostAddressBook(supplierList.get(0));
			Map<String, Object> response = middlewareService.updatePostAddressBook(id,addressBook);
			return response;
		//	return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			//return mapError(e.getMessage() + " - updatePostAddressBook");
			//return ResponseEntity.ok().body(mapError(e.getMessage() + " - updatePostAddressBook"));
			//return ResponseEntity.ok(e.getMessage() + " - updatePostAddressBook");
			return mapError(e.getMessage() + " - updatePostAddressBook");
		}
	}
	
	@RequestMapping(value ="/middleware/getSuppliers")
	public @ResponseBody Map<String, Object> getSuppliers(@RequestParam int start,
												     @RequestParam int limit,
												     HttpServletRequest request){	
			try{
				Map<String, Object> response = middlewareService.getSuppliers(start , limit);
			
		    return response;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getSuppliers");
		}
	}
	
	
	@RequestMapping(value ="/middleware/setPurchaseReceiptList" , method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> setPurchaseReceiptList(@RequestBody List<Receipt> recpList){	
		
		try{
			Map<String, Object> response = middlewareService.setPurchaseReceiptList(recpList);

			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - setPurchaseReceiptList"));
		}
	}
	
	
	@RequestMapping(value ="/middleware/getListPdfBatch" , method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getListPdfBatch(){	
		
		try{
			Map<String, Object> response = middlewareService.getListPdfBatch();

			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - getListPdfBatch"));
		}
	}
	
	@RequestMapping(value ="/middleware/saveFilePdfBatch" , method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> saveFilePdfBatch(@RequestBody HashMap<String,Object> request){	
		
		try{
			
			Map<String, Object> response = middlewareService.savePdfBatch(request);

			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - saveFilePdfBatch"));
		}
	}
	
	@RequestMapping(value ="/middleware/getAddressBookDocs", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAddressBookDocs(HttpServletRequest request){	
		try{
			Map<String, Object> response = middlewareService.getAddressBookDocs();
			
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - getAddressBookDocs"));
		}
	}

	@RequestMapping(value ="/middleware/updateAddressBookDocs", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> updateAddressBookDocs(@RequestBody List<FileDTO> fileDTOList) {	
		try{
			Map<String, Object> response = middlewareService.updateAddressBookDocs(fileDTOList);
			
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - getAddressBookDocs"));
		}
	}
	
	@RequestMapping(value ="/middleware/getVoucherDocs", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getVoucherDocs(HttpServletRequest request) {	
		try{
			Map<String, Object> response = middlewareService.getVoucherDocs();
			
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - getVoucherDocs"));
		}
	}
	
	@RequestMapping(value ="/middleware/updateVoucherDocs", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> updateVoucherDocs(@RequestBody List<FileDTO> fileDTOList) {	
		try{
			Map<String, Object> response = middlewareService.updateVoucherDocs(fileDTOList);
			
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - updateVoucherDocs"));
		}
	}
	
	@RequestMapping(value ="/middleware/getPostVoucherForeign")
	public @ResponseBody Map<String, Object> getPostVoucherForeign(@RequestParam int start,
												     @RequestParam int limit,
												     HttpServletRequest request){	
		try{
			Map<String, Object> response = middlewareService.getPostVoucherForeign(start,limit);
		    return response;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage() + " - getPostVoucher");
		}
	}
	//Vacio
	@RequestMapping(value ="/middleware/updatePostVoucherForeign", method=RequestMethod.POST)
	//public ResponseEntity<Map<String, Object>> updatePostVoucherForeign(@RequestParam("batchJournalList") String batchJournalList) {	
	public ResponseEntity<Map<String, Object>> updatePostVoucherForeign(@RequestBody List<VoucherHeaderDTO> batchJournalList) {
		try{
			Map<String, Object> response = middlewareService.updatePostVoucherForeign(batchJournalList);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - updateJournalEntries"));
		}
	}

	@RequestMapping(value ="/middleware/getEnabledPO", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getEnabledPO(@RequestParam int start,
																@RequestParam int limit,
																HttpServletRequest request){
		
		try{
			Map<String, Object> response = middlewareService.getEnabledPO(start, limit);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - getEnabledPO"));
		}
	}
	
	@RequestMapping(value ="/middleware/setDisabledPO", method=RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setDisabledPO(@RequestBody List<PurchaseOrder> poList){
		
		try {			
			Map<String, Object> response = middlewareService.setDisabledPO(poList);
			return ResponseEntity.ok().body(response);

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - setDisabledPO"));
		}

	}
	
	@RequestMapping(value ="/middleware/getEnabledReceipts", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getEnabledReceipts(@RequestParam int start,
																@RequestParam int limit,
																HttpServletRequest request){
		
		try{
			Map<String, Object> response = middlewareService.getEnabledReceipts(start, limit);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - getEnabledReceipts"));
		}
	}
	
	@RequestMapping(value ="/middleware/setDisabledReceipts", method=RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setDisabledReceipts(@RequestBody List<Receipt> receipList){
		
		try {			
			Map<String, Object> response = middlewareService.setDisabledReceipts(receipList);
			return ResponseEntity.ok().body(response);

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - setDisabledReceipts"));
		}

	}
	
	@RequestMapping(value ="/middleware/getBlockAndUnblocksSuppCompl", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getBlockAndUnblocksSuppCompl(@RequestParam int start,
																@RequestParam int limit,
																HttpServletRequest request){
		
		try{
			Map<String, Object> response = middlewareService.getBlockAndUnblocksSuppCompl(start, limit);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - getBlockAndUnblocksSuppCompl"));
		}
	}
	
	
	@RequestMapping(value ="/middleware/updateBlockAndUnblocksSuppCompl", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> updateBlockAndUnblocksSuppCompl(@RequestBody BlockerSupReceiptDTO bsr ){
		
		try{
			Map<String, Object> response = middlewareService.updateBlockAndUnblocksSuppCompl(bsr);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - updateBlockAndUnblocksSuppCompl"));
		}
	}
	
	@RequestMapping(value ="/middleware/getIdsPaymentsSupplier" , method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getIdsPaymentsSupplier(){	
		
		try{
			Map<String, Object> response = middlewareService.getIdsPaymentsSupplier();

			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage() + " - getIdsPaymentsSupplier"));
		}
	}
	
	@RequestMapping(value = "/middleware/savePaymentsSupplier", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> savePaymentsSupplier(@RequestBody List<HashMap<String, Object>>  request) {	
	    try {
	        Map<String, Object> response = middlewareService.savePaymentsSupplier(request);
	        return ResponseEntity.ok().body(response);
	    } catch (Exception e) {
	        log4j.error("Exception", e);
	        e.printStackTrace();
	        return ResponseEntity.ok().body(mapError(e.getMessage() + " - savePaymentsSupplier"));
	    }
	}
	
	
	@RequestMapping(value = "/middleware/savePaymentsSupplierDetail", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> savePaymentsSupplierDetail(@RequestBody List<HashMap<String, Object>>  request) {	
	    try {
	        Map<String, Object> response = middlewareService.savePaymentsSupplierDetail(request);
	        return ResponseEntity.ok().body(response);
	    } catch (Exception e) {
	        log4j.error("Exception", e);
	        e.printStackTrace();
	        return ResponseEntity.ok().body(mapError(e.getMessage() + " - savePaymentsSupplier"));
	    }
	}
	

	@RequestMapping(value = "/middleware/sendNotificacionPendNotificacion", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> sendNotificacionPendNotificacion(@RequestBody List<Map<String, Object>> jsonList) {
	    try {
	        // Crear una instancia de ObjectMapper
	        ObjectMapper objectMapper = new ObjectMapper();

	        // Convertir la lista de mapas a una cadena JSON
	        String jsonString = objectMapper.writeValueAsString(jsonList);

	        // Registrar la ejecución del método y la entrada en formato JSON
	        log4j.info("Ejecutando sendNotificacionPendNotificacion con entrada: {}: "+ jsonString);

	        
	            middlewareService.sendNotificacionPendienteContabilizar(jsonString);
	    

	        return ResponseEntity.ok().body(mapOK("ok", 0));
	    } catch (Exception e) {
	        log4j.error("Exception en sendNotificacionPendNotificacion", e);
	        return ResponseEntity.ok().body(mapError(e.getMessage() + " - sendNotificacionPendNotificacion"));
	    }
	}
	
	@RequestMapping(value = "/middleware/sendNotificacionPendienteFletes", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> sendNotificacionPendienteFletes(@RequestBody List<String> jsonList) {
	    try {
	        // Crear una instancia de ObjectMapper
	        ObjectMapper objectMapper = new ObjectMapper();

	        // Convertir la lista de mapas a una cadena JSON
	        String jsonString = objectMapper.writeValueAsString(jsonList);

	        // Registrar la ejecución del método y la entrada en formato JSON
	        log4j.info("Ejecutando sendNotificacionPendienteFletes con entrada: {}: "+ jsonString);

	        
	            fiscalDocumentService.fletesPendientesPosteoFletesJDE(jsonList);
	    

	        return ResponseEntity.ok().body(mapOK("ok", 0));
	    } catch (Exception e) {
	        log4j.error("Exception en sendNotificacionPendNotificacion", e);
	        return ResponseEntity.ok().body(mapError(e.getMessage() + " - sendNotificacionPendNotificacion"));
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

}
