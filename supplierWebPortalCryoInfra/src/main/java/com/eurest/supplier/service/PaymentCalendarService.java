package com.eurest.supplier.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.PaymentCalendarDao;
import com.eurest.supplier.model.PaymentCalendar;
import com.eurest.supplier.util.FileUploadBean;

@Service("paymentCalendarService")
public class PaymentCalendarService {

	@Autowired
	private PaymentCalendarDao paymentCalendarDao;
	
	Logger log4j = Logger.getLogger(PaymentCalendarService.class);
	
	public List<PaymentCalendar> getPaymentCalendarByYear(int year,int start, int limit){
		return paymentCalendarDao.getPaymentCalendarByYear(year, start, limit);
	}
	
	public List<PaymentCalendar> getPaymentCalendarList(int start, int limit){
		return paymentCalendarDao.getPaymentCalendarList(start, limit);
	}
	
	public void saveMultiple(List<PaymentCalendar> list) {
		paymentCalendarDao.saveMultiple(list);
	}
	
	public int getTotalRecords(){
		return paymentCalendarDao.getTotalRecords();
	}
	
	public List<PaymentCalendar> getPaymentCalendarFromToday(Date dateFrom,int start, int limit, String addressNumber){
		return paymentCalendarDao.getPaymentCalendarFromToday(dateFrom, start, limit);		
	}
	
	public int deleteRecords(int year){
		return paymentCalendarDao.deleteRecords(year);
	}
	
	public int processFile(FileUploadBean file, String user, Date date) {
		
		Workbook workbook = null;
		Sheet sheet = null;
		List<PaymentCalendar> calendarList = new ArrayList<PaymentCalendar>();
		int count = 0;
		
		try {
			
			workbook = WorkbookFactory.create(file.getFile().getInputStream());
			sheet = workbook.getSheet("Calendar");
			Iterator<Row> rowIterator = sheet.iterator();
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if(row.getRowNum() > 0) {
					PaymentCalendar pc = new PaymentCalendar();
					pc.setId(0);
					pc.setUpdatedBy(user);
					pc.setUploadDate(date);
					
					Cell readCell = row.getCell(0);
					if(readCell != null) {
						if(readCell.getCellType() == Cell.CELL_TYPE_BLANK) {
							break;
						}
						switch (readCell.getCellType()) 
	                    {
	                        case Cell.CELL_TYPE_NUMERIC:
	                        	int val = (int) readCell.getNumericCellValue();
	                        	String cellVal = StringUtils.leftPad(String.valueOf(val), 5, "0");
	                        	pc.setCompany(cellVal);
	                            break;
	                        case Cell.CELL_TYPE_STRING:
	                        	pc.setCompany(readCell.getStringCellValue());
	                            break;
	                    }
					
						
						readCell = row.getCell(1);
						Date cellDate = null;						
						if(readCell != null) {
							switch (readCell.getCellType()) 
		                    {
		                        case Cell.CELL_TYPE_NUMERIC:
		                            cellDate = readCell.getDateCellValue(); 
		                            pc.setPaymentDate(cellDate);
		                            break;
		                        case Cell.CELL_TYPE_STRING:
		                        	cellDate = new SimpleDateFormat("dd/MM/yyyy").parse(readCell.getStringCellValue());  
		                            break;
		                    }
							
							if(cellDate != null) {
								Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
								cal.setTime(cellDate);
								int year = cal.get(Calendar.YEAR);
								int month = cal.get(Calendar.MONTH);
								int day = cal.get(Calendar.DAY_OF_MONTH);
								pc.setYear(year);
								pc.setMonth(month + 1);
								pc.setDay(day);
							}
							count = count + 1;
							calendarList.add(pc);
						}
					}
				}
			}
			
			if(calendarList.size() > 0) {
				paymentCalendarDao.saveMultiple(calendarList);
			}
			
			return count;
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return 0;
		}

	}
	
	
}
