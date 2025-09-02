package com.eurest.supplier.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eurest.supplier.model.PaymentCalendar;
import com.eurest.supplier.service.PaymentCalendarService;
import com.eurest.supplier.util.FileUploadBean;

import net.sf.json.JSONObject;

@Controller
public class PaymentCalendarController {

	@Autowired
	private PaymentCalendarService paymentCalendarService;
	
	Logger log4j = Logger.getLogger(PaymentCalendarController.class);

	@RequestMapping(value = "/paymentCalendar/view.action")
	public @ResponseBody Map<String, Object> view(@RequestParam int start, @RequestParam int limit) {

		List<PaymentCalendar> list = null;
		int total = 0;

		try {
			list = paymentCalendarService.getPaymentCalendarList(start, limit);
			total = paymentCalendarService.getTotalRecords();
			return mapOK(list, total);
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage());
		}

	}

	@RequestMapping(value = "/paymentCalendar/uploadCalendar.action", method = RequestMethod.POST)
	@ResponseBody
	public String uploadSuppliers(FileUploadBean uploadItem, BindingResult result, HttpServletResponse response) {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		JSONObject json = new JSONObject();

		try {

			if (result.hasErrors()) {
				for (ObjectError error : result.getAllErrors()) {
					System.err.println("Error: " + error.getCode() + " - " + error.getDefaultMessage());
				}

				json.put("success", false);
				json.put("message", "Error al cargar el archivo");
			}

			if (uploadItem.getFile().getOriginalFilename().endsWith(".xlsx")
					|| uploadItem.getFile().getOriginalFilename().endsWith(".xls")) {
				
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				String usr = auth.getName();	

				int count = paymentCalendarService.processFile(uploadItem, usr, new Date());
				json.put("success", true);
				json.put("message","El archivo " + uploadItem.getFile().getOriginalFilename() + " ha sido cargado exitosamente.");
				json.put("fileName", uploadItem.getFile().getOriginalFilename());
				json.put("count", count);

			} else {
				json.put("success", false);
				json.put("message", "Error: Sólo se permiten archivos tipo .xlsx o .xls");
			}

			return json.toString();

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			json.put("success", false);
			json.put("message", e.getMessage());

		}
		return json.toString();

	}
	
	@RequestMapping(value = "/paymentCalendar/deleteCalendar.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteCalendar(String year, HttpServletResponse response) {

		try {
			int y = Integer.valueOf(year);
			int result = paymentCalendarService.deleteRecords(y);
			return mapOKStr(String.valueOf(result));
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return mapError(e.getMessage().toString());
		}


	}

	public Map<String, Object> mapOK(List<PaymentCalendar> list, int total) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String, Object> mapOK(PaymentCalendar udc) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", 1);
		modelMap.put("data", udc);
		modelMap.put("success", true);
		return modelMap;
	}

	public Map<String, Object> mapOKStr(String result) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", 1);
		modelMap.put("data", result);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String, Object> mapError(String msg) {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		return modelMap;
	}

}
