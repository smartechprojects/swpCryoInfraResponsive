package com.eurest.supplier.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.eurest.supplier.dao.CodigosSatDao;
import com.eurest.supplier.model.CodigosSAT;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.Logger;

public class BatchProcessService implements Runnable{

	Workbook workbook = null;
	CodigosSatDao codigosSatDao = null;
	Logger logger = null;
	
	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(BatchProcessService.class);
	
	@Override
	public void run() {
		Sheet sheet = null;
		List<CodigosSAT> codes = new ArrayList<CodigosSAT>();
		int count = 0;
		try {
			sheet = workbook.getSheet("c_ClaveProdServ");
			Iterator<Row> rowIterator = sheet.iterator();
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if(row.getRowNum() > 0) {
					CodigosSAT code = new CodigosSAT();
					code.setId(0);
					code.setCodigoSAT(row.getCell(0).getStringCellValue());
					code.setDescripcion(row.getCell(1).getStringCellValue());
					code.setTipoCodigo(row.getCell(2).getStringCellValue());
					codes.add(code);
					count = count + 1;
				}
			}
			
			//codigosSatDao.deleteRecords();
			codigosSatDao.saveMultiple(codes);
			
			List<String> strList = new ArrayList<String>();
			for(CodigosSAT o : codes) {
				strList.add(o.getCodigoSAT());
			}
			//InitConstructBean.setCodigosSatList(strList);
			
			logger.log(AppConstants.LOG_BATCH_PROCESS, AppConstants.LOG_BATCH_PROCESS_CODSAT + count);

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		
	}
	
		
	public void setCodigoSatDao(CodigosSatDao codigosSatDao) {
		this.codigosSatDao = codigosSatDao;
	}
	
	public void setFile(Workbook workbook) {
		this.workbook = workbook;
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
