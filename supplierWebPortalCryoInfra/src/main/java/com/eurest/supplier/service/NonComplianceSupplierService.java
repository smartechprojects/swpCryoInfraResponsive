package com.eurest.supplier.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.NonComplianceSupplierDao;
import com.eurest.supplier.model.NonComplianceSupplier;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.InsertBatchRecords;

@Service("nonComplianceSupplierService")
public class NonComplianceSupplierService {
	
	@Autowired
	NonComplianceSupplierDao nonComplianceSupplierDao;
	
	private Logger log4j = Logger.getLogger(NonComplianceSupplierService.class);
	
	public NonComplianceSupplier getNonComplianceSupplierById(int id) {
		return nonComplianceSupplierDao.getNonComplianceSupplierById(id);
	}
	
	public List<NonComplianceSupplier> getList(int start,int limit) {
		return nonComplianceSupplierDao.getList(start, limit);
	}
			
	public List<NonComplianceSupplier> searchByCriteria(String query,
			                                      int start,
			                                      int limit) {
		return nonComplianceSupplierDao.searchByCriteria(query, start, limit);
	}
	
	public NonComplianceSupplier searchByTaxId(String query,
            int start,
            int limit) {
		return nonComplianceSupplierDao.searchByTaxId(query, start, limit);
	}
	
	public long searchByCriteriaTotalRecords(String query) {
		return nonComplianceSupplierDao.searchByCriteriaTotalRecords(query);
	}
	
	public void saveSuppliers(List<NonComplianceSupplier> list) {
		nonComplianceSupplierDao.saveSuppliers(list);
	}
	
	public void deleteAll() {
		nonComplianceSupplierDao.deleteAll();
	}
	
	public int getTotalRecords(){
		return nonComplianceSupplierDao.getTotalRecords();
	}
	@Scheduled(cron = "0 0 21 * * SAT")
	//@Scheduled(fixedDelay = 8640000, initialDelay = 60000)
	public void getNonComplianceSuppliers() {
		try {
			URL urlCSV = new URL(AppConstants.SAT_NONCOMPLANCE_URL);
			URLConnection urlConn = urlCSV.openConnection();
			InputStreamReader inputCSV = new InputStreamReader(((URLConnection) urlConn).getInputStream());
			BufferedReader br = new BufferedReader(inputCSV);
			String line;
			
			List<NonComplianceSupplier> list = new ArrayList<NonComplianceSupplier>();
			String updDate = "";
			while ((line = br.readLine()) != null) {
					String[] values = line.split(",", -1);
					if(values.length >= 18) {  // El reporte contiene solamente 18 columnas
						NonComplianceSupplier o = new NonComplianceSupplier();
						o.setLastUpdate(updDate);
						o.setLineNumber(values[0]);
						o.setTaxId(values[1]);
						o.setSupplierName(values[2]);
						o.setStatus(values[3]);
						o.setRefDate1(values[4]);
						o.setRefDate2(values[5]);
						o.setRefDate3(values[6]);
						o.setRefDate4(values[7]);
						o.setRefDate5(values[8]);
						o.setRefDate6(values[9]);
						o.setRefDate7(values[10]);
						o.setRefDate8(values[11]);
						o.setRefDate9(values[12]);
						o.setRefDate10(values[13]);
						o.setRefDate11(values[14]);
						o.setRefDate12(values[15]);
						o.setRefDate13(values[16]);
						o.setRefDate14(values[17]);
						list.add(o);
					}else {
						log4j.info(values.length);
						if("".equals(updDate)) {
							updDate = values[0];
						}
						
					}
				
            }
			br.close();
			
			if(list.size() > 0) {
			    InsertBatchRecords b = new InsertBatchRecords(list, nonComplianceSupplierDao);
		        Thread t = new Thread(b);
		        t.start();
			}
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}

}
