package com.eurest.supplier.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.DataAuditDao;
import com.eurest.supplier.model.DataAudit;
import com.eurest.supplier.util.AppConstants;

@Service("dataAuditService")
public class DataAuditService {
	
	@Autowired
	DataAuditDao dataAuditDao;
	
	public DataAudit getDataAuditById(int id) {
		return dataAuditDao.getDataAuditById(id);
	}
	
	public DataAudit save(DataAudit o) {
		return dataAuditDao.save(o);
	}
	
	public void saveDataAudit(List<DataAudit> list) {
	    dataAuditDao.saveDataAudit(list);
	}
	
	public void saveDataAudit(String action, String addressNumber, Date currentDate, String ip, 
            String usr, String message, String method, String notes, String documentNumber,
            String orderNumber, String step, String uuid, String status, String module) {
		
		DataAudit dataAudit = new DataAudit();	 		
    	dataAudit.setAction(action);
    	dataAudit.setAddressNumber(addressNumber);
    	dataAudit.setCreationDate(currentDate);
    	dataAudit.setDocumentNumber(documentNumber);
    	dataAudit.setIp(ip);
    	dataAudit.setMessage(message);
    	dataAudit.setMethod(method);
    	dataAudit.setModule(module);
    	dataAudit.setNotes(notes);
    	dataAudit.setOrderNumber(orderNumber);
    	dataAudit.setStatus(status);
    	dataAudit.setStep(step);
    	dataAudit.setUser(usr);
    	dataAudit.setUuid(uuid);             	
    	save(dataAudit);
	}
	
	

}
