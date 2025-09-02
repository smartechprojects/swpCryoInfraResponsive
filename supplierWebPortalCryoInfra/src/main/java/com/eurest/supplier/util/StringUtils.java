package com.eurest.supplier.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.SecureRandom;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StringUtils {
	
	@Autowired
	VelocityEngine velocityEngine;
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();
	
	public String prepareEmailContent(String message) {
		Template template = velocityEngine.getTemplate("./templates/generalTemplate.vm");
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("emailContent", message);
		StringWriter stringWriter = new StringWriter();
		template.merge(velocityContext, stringWriter);
		return stringWriter.toString();
	}

	public static String randomString(int len){
	   StringBuilder sb = new StringBuilder(len);
	   for(int i = 0; i < len; i++)
	      sb.append(AB.charAt(rnd.nextInt(AB.length())));
	   return sb.toString();
	}
	
	public  static String getString(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	
	public static double roundDouble(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, BigDecimal.ROUND_HALF_EVEN);
	    return bd.doubleValue();
	}

	public static String takeOffSpecialChars(String str){
		//Evita error de codificación: Conversion from collation utf8mb4_0900_ai_ci into latin1_swedish_ci impossible for parameter
		try {
		    if(str != null) {
		    	return str.replaceAll("[^\u0000-\u00FF]", "");
		    } else {
		    	return null;
		    }
		} catch (Exception e) {
			e.printStackTrace();
			return str;
		}
	}
}
