package com.eurest.supplier.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.eurest.supplier.dao.NonComplianceSupplierDao;
import com.eurest.supplier.model.NonComplianceSupplier;

public class InsertBatchRecords implements Runnable {
	
	List<NonComplianceSupplier> list;
	NonComplianceSupplierDao nonComplianceSupplierDao;
	
	private Logger log4j = Logger.getLogger(InsertBatchRecords.class);
	
	public InsertBatchRecords(List<NonComplianceSupplier> list, NonComplianceSupplierDao nonComplianceSupplierDao) {
        this.list = list;
        this.nonComplianceSupplierDao = nonComplianceSupplierDao;
    }
	
	public void run() {
		if(list.size() > 0) {
			try {
				log4j.info("Deleting Non Compliance....");
				nonComplianceSupplierDao.deleteAll();
				log4j.info("Inserting Non Compliance....");
				nonComplianceSupplierDao.saveSuppliers(list);
				log4j.info("Inserted Non Compliance....");
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}
		}
    }

}
