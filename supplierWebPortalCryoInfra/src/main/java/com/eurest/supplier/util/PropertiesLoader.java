package com.eurest.supplier.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class PropertiesLoader {

	Map<String,String> propMap = new HashMap<String,String>();
	
	public Map<String, String> getPropMap(String lang) {
		
		
		Locale locale = new Locale(lang);
		ResourceBundle rb = ResourceBundle.getBundle("config.message", locale);  
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			propMap.put(key.replace("supp.", ""), value);
	      }
		
		return propMap;
	}
}
