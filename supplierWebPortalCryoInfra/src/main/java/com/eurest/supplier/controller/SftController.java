package com.eurest.supplier.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eurest.supplier.dto.InvoiceFreightIn;
import com.eurest.supplier.dto.RequestComplemetPagDTO;
import com.eurest.supplier.edi.BatchJournalDTO;
import com.eurest.supplier.model.InvoiceFreightResponse;
import com.eurest.supplier.model.ReceipInvoiceFreight;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.service.EmailServiceAsync;
import com.eurest.supplier.service.SftService;
import com.eurest.supplier.service.TaxVaultDocumentService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.StringUtils;
import com.google.gson.Gson;

@Controller
public class SftController { 
	
	@Autowired
	SftService sftService;
	
	@Autowired
	TaxVaultDocumentService taxVaultDocumentService;
	
	@Autowired
	StringUtils stringUtils;
	
	@Autowired
	private JavaMailSender mailSenderObj;
	
	@Autowired
	private UdcService udcService;

	private Logger log4j = Logger.getLogger(SftController.class);
	
	@CrossOrigin
	@RequestMapping(value ="/sft/validateInvoiceFreight", method=RequestMethod.POST)
	@ResponseBody
//	@Transactional(timeout =120000)
	
	//public List<InvoiceFreightResponse>  validateInvoiceFreight(@RequestBody List<InvoiceFreightRequest> batchJournalList) {
	public List<InvoiceFreightResponse>  validateInvoiceFreight(@RequestBody ReceipInvoiceFreight recep) {
		try{
			UDC udcCfdi = udcService.searchBySystemAndKey("PATH", "LOGFLETE");	
			  LocalDateTime inicio = LocalDateTime.now(); // Fecha y hora de inicio
//		       String logsjson=udcCfdi.getStrValue1()+DateTimeFormatter.ofPattern("yyyy_MM_dd__HH_mm_ss").format(LocalDateTime.now())+".flete.json";
//		       
//		       File archivo = new File(logsjson);
//		        File directorio = archivo.getParentFile();
//
//		        if (!directorio.exists()) {
//		            if (directorio.mkdirs()) {
//		                System.out.println("Directorio creado: " + directorio.getAbsolutePath());
//		            } else {
//		                System.out.println("Error al crear el directorio."+ logsjson);
//		                
//		            }
//		        }
//			
//			
//			  try (FileWriter fileWriter = new FileWriter(logsjson)) {
//		            fileWriter.write(new Gson().toJson(recep));
//		            System.out.println("Archivo creado correctamente.");
//		        } catch (IOException e) {
//		            System.out.println("Ocurrió un error al crear el archivo: " + e.getMessage());
//		        }
//			
//			  Path directorioPath = Paths.get(archivo.getParentFile().getParent());
//			  double sizeInGB = (double)Files.walk(directorioPath)
//              .filter(Files::isRegularFile)
//              .mapToLong(path -> {
//                  try {
//                      return Files.size(path);
//                  } catch (IOException e) {
//                      return 0;
//                  }
//              })
//              .sum()/ (1024 * 1024 * 1024); 
//			  if (sizeInGB>3) {
//				
//
//				  try {
//					  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
//					  emailAsyncSup.setProperties("Alerta de espacio",
//					  this.stringUtils.prepareEmailContent("Este mensaje es de alerta de haber alcanzado el limite de 3gb de almacenamiento de json en ruta "+logsjson), "soportecryoinfra@smartech.com.mx");
//					  emailAsyncSup.setMailSender(mailSenderObj);
//					  Thread emailThreadSup = new Thread(emailAsyncSup);
//					  emailThreadSup.start();
//				  } catch (Exception e) {	
//					  
//					  log4j.error("Exception" , e);
//				  }
//			}
			  
			  
			
			List<InvoiceFreightResponse> response = sftService.validateInvoiceFreight(recep);
			 LocalDateTime fin = LocalDateTime.now(); // Fecha y hora de fin

		        Duration duracion = Duration.between(inicio, fin);
		        long dias = duracion.toDays();
		        long horas = duracion.toHours() % 24;
		        long minutos = duracion.toMinutes() % 60;

		        System.out.println("Días: " + dias);
		        System.out.println("Horas: " + horas);
		        System.out.println("Minutos: " + minutos);			
			return response;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		//	return e.toString();
		}
		return null;
	}
	
	
	@RequestMapping(value ="/middleware/getListFletes", method=RequestMethod.POST)
	@ResponseBody
//	@Transactional(timeout =120000)
	public List<BatchJournalDTO>  GetListFletes(@RequestParam int start,@RequestParam int limit) {
		try{
						List<BatchJournalDTO>  response = sftService.createBatchJournalList(start,limit);
			return response;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		//	return e.toString()b
		}
		return null;
	}

	@RequestMapping(value ="/middleware/updateListFletesForNewOrUpdate", method=RequestMethod.POST)
	@ResponseBody
//	@Transactional(timeout =120000)
	public ResponseEntity<Map<String, Object>>  updateListFletesForNewOrUpdate(@RequestBody List<BatchJournalDTO>  batchVoucherTransactionsDTO) {
		try{
			
			
			List<BatchJournalDTO>  response = sftService.updateFletesStatus(batchVoucherTransactionsDTO);
			
			
			 return ResponseEntity.ok().body(mapOk(response));
		} catch (Exception e) {
			log4j.error("Exception" , e);
			return ResponseEntity.ok().body(mapError(e.getMessage()));
		}
	
	}
	
	@CrossOrigin
	@RequestMapping(value ="/sft/getListCompP", method=RequestMethod.POST)
	@ResponseBody
//	@Transactional(timeout =120000)
	
	public ResponseEntity<Map<String, Object>>  getListCompP(@RequestBody RequestComplemetPagDTO compl) {
		try{
			log4j.info("CRYO INFRA :: WS-GetListCompP: Datos recibidos \n"+new Gson().toJson(compl));
		RequestComplemetPagDTO response = taxVaultDocumentService.getListComplPag(compl);
						
		 return ResponseEntity.ok().body( mapOk(response));
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return ResponseEntity.ok().body(mapError(e.getMessage()));
		//	return e.toString();
		}
	}
	
	
	@CrossOrigin
	@RequestMapping(value ="/sft/validateInvoiceUtil", method=RequestMethod.POST)
	@ResponseBody
//	@Transactional(timeout =120000)
	
	public InvoiceFreightIn  validateInvoiceFreightUtil(@RequestBody InvoiceFreightIn recep) {
		try{
			log4j.info("datos recibidos validateInvoiceFreightUtil \n"+new Gson().toJson(recep));
			InvoiceFreightIn response = sftService.validateInvoiceFreightIn(recep);
						
			return response;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		// 	return e.toString();
		}
		return null;
	}
	
	
	
	
	public Map<String,Object> mapError(String msg){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		return modelMap;
	} 

	public Map<String,Object> mapOk(Object list){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("data", list);
		modelMap.put("message", "OK");
		modelMap.put("success", true);
		return modelMap;
	} 
}
