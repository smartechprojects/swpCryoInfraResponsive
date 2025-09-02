package com.eurest.supplier.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class JdeJavaJulianDateTools {
	
	private static Logger log4j = Logger.getLogger(JdeJavaJulianDateTools.class);
	
	public static class Methods {
		/**
		 * JavaDateToJdeDate: translate from a Java Date to the JDEdwards Julian
		 * date format.
		 * 
		 * {talendTypes} int | Integer {Category} User Defined {param}
		 * Date("1/10/2008") input: The Java Date to be converted. {example}
		 * JavaDateToJdeDate(1/10/2008) # 108010.
		 */
		public static String getJulianDate(Date date) {
			StringBuilder sb = new StringBuilder();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return sb.append("1").append(Integer.toString(cal.get(Calendar.YEAR)).substring(2, 4))
					.append(String.format("%03d", cal.get(Calendar.DAY_OF_YEAR))).toString();
		}
		/**
		 * getJDETimeStamp: Get HHmmss JDE TIME STAMP
		 * date format.
		 * 
		 * {talendTypes} int | Integer {Category} User Defined {param}
		 */
		public static int getJDETimeStamp() {
	  
	  return Integer.parseInt(LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss")));
		}

		/**
		 * JdeDateToJavaDate: translate from the JDEdwards Julian date format to
		 * a Java Date.
		 * 
		 * {talendTypes} Date {Category} User Defined {param} int(108010) input:
		 * The JDE Julian date to be converted. {example}
		 * JdeDateToJavaDate(108010) # 1/10/2008.
		 */
		public static Date JulianDateToJavaDate(Integer julianDate) {
			return JulianDateToJavaDate(julianDate.toString());
		}

		/**
		 * JulianDateToJavaDate: translate from the JDEdwards Julian date format
		 * to a Java Date.
		 * 
		 * {talendTypes} Date {Category} User Defined {param} String("116001")
		 * input: The JDE Julian date to be converted. {example}
		 * JulianDateToJavaDate("116001") # 1/1/2016.
		 */
		public static Date JulianDateToJavaDate(String julianDate) {
			Date date = null;
			String j = julianDate.toString();
			String format=j.length()==6?"Myyddd":"yyddd";
			try {
				log4j.info(j);
				date = new SimpleDateFormat(format).parse(j);
			} catch (ParseException e) {
				log4j.error("Exception" , e);
			}
			return date;
		}
		
		
		/**
		 * JulianDateToJavaDate: translate from the JDEdwards Julian date format
		 * to a Java Date.
		 * 
		 * {talendTypes} Date {Category} User Defined {param} String("116001")
		 * input: The JDE Julian date to be converted. {example}
		 * JulianDateToJavaDate("116001") # 1/1/2016.
		 */
		
		
		
		public static Date JulianDateToJavaDate(String julianDate,String formato) {
			try {
				return JulianDateToJavaDate(Integer.parseInt(julianDate),formato);
			} catch (Exception e) {
				log4j.error(e);
				e.printStackTrace();
			}
			return null;
			
		}
		
		public static Date JulianDateToJavaDate(Integer julianDate,String formato) {
		    Date date = null;
		    String j = julianDate.toString();
		    try {
		        // Corregir el formato de la fecha juliana a "yyDDD"
		        String year = j.substring(1, 3);
		        String dayOfYear = j.substring(3);
		        String dateStr = year + dayOfYear;
		        date = new SimpleDateFormat("yyDDD").parse(dateStr);
		    } catch (ParseException e) {
		        log4j.error("Exception", e);
		    }
		    return date;
		}

		/**
		 * getJulianDate: translate from the JDEdwards Julian date format to a
		 * Java Date. {talendTypes} String {Category} User Defined {example}
		 * getJulianDate() # 1/1/2016.
		 */
		public static String getJulianDate() {
			return getJulianDate(0);
		}

		/**
		 * getJulianDate: translate from the JDEdwards Julian date format to a
		 * Java Date. {talendTypes} String {Category} User Defined {example}
		 * getJulianDate() # 1/1/2016.
		 */
		public static int getJulianDateInteger() {
			return Integer.parseInt(getJulianDate());
		}
		
		/**
		 * getJulianDate: translate from the JDEdwards Julian date format to a
		 * Java Date. {talendTypes} String {Category} User Defined {example}
		 * getJulianDate() # 1/1/2016.
		 */
		public static int getJulianDateInteger(Date date) {
			return Integer.parseInt(getJulianDate(date));
		}
		


		/**
		 * getJulianDate: translate from the JDEdwards Julian date format to a
		 * Java Date. {talendTypes} String {Category} User Defined {example}
		 * getJulianDate() # 1/1/2016.
		 */
		public static int getJulianDateInteger(int dayOffset) {
			return Integer.parseInt(getJulianDate(dayOffset));
		}

		/**
		 * getJulianDate: translate from the JDEdwards Julian date format to a
		 * Java Date. {talendTypes} String {Category} User Defined {param} int |
		 * Integer(1) input: Integer days forward or ahead. {example}
		 * JavaDateToJdeDate(1/10/2008) # 108010.
		 */
		public static String getJulianDate(Integer dayOffset) {
			if (dayOffset == null)
				dayOffset = 0;
			long offset = dayOffset;
			offset = offset * (1000 * 60 * 60 * 24);
			Date date = new Date(System.currentTimeMillis() + offset);
			StringBuilder sb = new StringBuilder();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return sb.append("1").append(Integer.toString(cal.get(Calendar.YEAR)).substring(2, 4))
					.append(String.format("%03d", cal.get(Calendar.DAY_OF_YEAR))).toString();
		}

		/**
		 * JulianFirstDayLastMonth: return int representing first day of last
		 * month in jde julian format {talendTypes} Date {Category} User Defined
		 */
		public static Integer JulianFirstDayLastMonth() {
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.add(Calendar.MONTH, -1);
			aCalendar.set(Calendar.DATE, 1);
			StringBuilder sb = new StringBuilder();
			Calendar cal = Calendar.getInstance();
			cal.setTime(aCalendar.getTime());
			return Integer.parseInt(sb.append("1").append(Integer.toString(cal.get(Calendar.YEAR)).substring(2, 4))
					.append(String.format("%03d", cal.get(Calendar.DAY_OF_YEAR))).toString());
		}

		/**
		 * JulianLastDayPreviousMonth: return int representing first day of last
		 * month in jde julian format {talendTypes} Date {Category} User Defined
		 */
		public static Integer JulianLastDayPreviousMonth() {
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.add(Calendar.MONTH, -1);
			aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			StringBuilder sb = new StringBuilder();
			Calendar cal = Calendar.getInstance();
			cal.setTime(aCalendar.getTime());
			return Integer.parseInt(sb.append("1").append(Integer.toString(cal.get(Calendar.YEAR)).substring(2, 4))
					.append(String.format("%03d", cal.get(Calendar.DAY_OF_YEAR))).toString());
		}

		/**
		 * DateLastDayPreviousMonth: return int representing first day of last
		 * month in jde julian format {talendTypes} Date {Category} User Defined
		 */
		public static String DateLastDayPreviousMonth() {
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.add(Calendar.MONTH, -1);
			aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			Calendar cal = Calendar.getInstance();
			cal.setTime(aCalendar.getTime());
			return (new SimpleDateFormat("MM/dd/yyyy").format(cal.getTime()));
		}

		/**
		 * DateFirstDayLastMonth: return int representing first day of last
		 * month in jde julian format {talendTypes} Date {Category} User Defined
		 */
		public static String DateFirstDayLastMonth() {
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.add(Calendar.MONTH, -1);
			aCalendar.set(Calendar.DATE, 1);
			Calendar cal = Calendar.getInstance();
			cal.setTime(aCalendar.getTime());
			return (new SimpleDateFormat("MM/dd/yyyy").format(cal.getTime()));
		}
		
		
		
		
		public static Boolean compareDates(int intDate) {
			//SimpleDateFormat df1 = new SimpleDateFormat("dd/M/yyyy HHmmss");
			//Set Update Day to be Date Last updated based on Julian Int Input
			Calendar update= Calendar.getInstance();
			update.setTime(JulianDateToJavaDate(intDate));
			update.set(Calendar.HOUR_OF_DAY, 0); 
			
			//Set Last Relevant Day to be 4 months in past
			Calendar lastRelevantDate = Calendar.getInstance();
			lastRelevantDate.set(Calendar.HOUR_OF_DAY, 0); 
			lastRelevantDate.add(Calendar.MONTH, -4);
			lastRelevantDate.add(Calendar.HOUR, -24);
			
			//If update date is greater than or equal to  the last relevant day true, else false
			if (update.compareTo(lastRelevantDate)>-1)return true;
		    else return false;
				
		}
		
	}

	public static void main(String[] args) {
		 System.out.println(JdeJavaJulianDateTools.Methods.JulianDateToJavaDate("94185")); 
	}
}
