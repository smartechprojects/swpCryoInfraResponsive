package com.eurest.supplier.test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import com.eurest.supplier.model.Receipt;

public class TestDate {
	public static void main(String[] args)throws Exception {  
		
		ArrayList<Receipt> requestedReceiptList = new ArrayList<Receipt>();
		Receipt r = new Receipt();
		
		Date fecha = new Date();
		
		r.setReceiptDate(fecha);
		
		requestedReceiptList.add(r);
		
		Date minDateReceipts = requestedReceiptList.stream().map(u -> u.getReceiptDate()).min(Date::compareTo).get();
		
		System.out.println(minDateReceipts);
		
	    String sDate1="2019-06-24";  
	    Date date=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);  
	    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    int month = localDate.getMonthValue();
	    String monthName = "";
	    
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
	    
	    System.out.println("Mes " + monthName);
	}  

}
