package com.eurest.supplier.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TestString {

	public static void main(String[] args) {
		
		
    		Double  f = 554545.0;
    				
    				f.intValue();
    		
    		System.out.println(String.valueOf(f.intValue()));
		
	






		StringBuilder str = new StringBuilder();
		String supNbr = StringUtils.leftPad("879",8,"0");
		str.append(supNbr);
		str.append("-");
		str.append(Calendar.getInstance().get(Calendar.YEAR));
		str.append("-");
		String month = StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.MONTH ) + 1),2,"0");
		String day = StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH )),2,"0");
		str.append(month);
		str.append(day);
		str.append("-");
		String hour = StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY )),2,"0");
		String minute = StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.MINUTE )),2,"0");
		str.append(hour);
		str.append(minute);
		str.append("-");
		str.append(StringUtils.leftPad("87",4,"0"));
		str.append(StringUtils.leftPad("9870",8,"0"));
		System.out.println(str.toString());
		
	}

}
