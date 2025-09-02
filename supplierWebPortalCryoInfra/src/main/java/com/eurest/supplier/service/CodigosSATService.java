package com.eurest.supplier.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.CodigosSatDao;
import com.eurest.supplier.model.CodigosSAT;
import com.eurest.supplier.util.FileUploadBean;

@Service("codigosSatService")
public class CodigosSATService {

	@Autowired
	private CodigosSatDao codigosSatDao;
	
	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(CodigosSATService.class);
	
	public List<CodigosSAT> getList(int start, int limit) {
		return codigosSatDao.getOrders(start, limit);
	}
	
	public List<CodigosSAT> findCodes(List<String> list) {
		return codigosSatDao.findCodes(list);
	}
	
	public void saveMultiple(List<CodigosSAT> list) {
		codigosSatDao.deleteRecords();
		codigosSatDao.saveMultiple(list);
	}
	
	public int processExcelFile(FileUploadBean uploadItem) {
		Workbook workbook = null;
		Sheet sheet = null;
		List<CodigosSAT> codes = new ArrayList<CodigosSAT>();
		int count = 0;
		try {
		    workbook = WorkbookFactory.create(uploadItem.getFile().getInputStream());
			sheet = workbook.getSheet("c_ClaveProdServ");
			Iterator<Row> rowIterator = sheet.iterator();
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if(row.getRowNum() > 0) {
					CodigosSAT code = new CodigosSAT();
					code.setId(0);
					code.setCodigoSAT(row.getCell(0).getStringCellValue());
					code.setDescripcion(row.getCell(1).getStringCellValue());
					codes.add(code);
					count = count + 1;
				}
			}
			saveMultiple(codes);
			return count;

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return 0;
		}
	
	}
	
	public int getTotalRecords() {
		return codigosSatDao.getTotalRecords();
	}
	
	public List<CodigosSAT> searchByCriteria(String query,
            int start,
            int limit) {
		return codigosSatDao.searchByCriteria(query, start, limit);
	}
	
	public long searchByCriteriaTotalRecords(String query) {
		return codigosSatDao.searchByCriteriaTotalRecords(query);
	}
	
	public List<CodigosSAT> searchByTipoCodigo(String tipoCodigo) {
		return codigosSatDao.searchByTipoCodigo(tipoCodigo);
	}
	
	public List<CodigosSAT> searchBySupplier(String supplier, int start, int limit, String company) {
		return codigosSatDao.searchBySupplier(supplier, start, limit, company);
	}
	
}
