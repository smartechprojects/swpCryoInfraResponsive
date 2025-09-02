package com.eurest.supplier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.ApprovalBatchFreightDao;
import com.eurest.supplier.dao.BatchJournalDao;
import com.eurest.supplier.model.ApprovalBatchFreight;
import com.eurest.supplier.model.BatchJournal;

@Service("ApprovalBatchFreightService")
public class ApprovalBatchFreightService {
	

	@Autowired
	ApprovalBatchFreightDao approvalBatchFreightDao;
	
	
	public ApprovalBatchFreight getById(int id) {
		return approvalBatchFreightDao.getById(id);
	}
	
	public void save(ApprovalBatchFreight doc) {
		approvalBatchFreightDao.save(doc);
	}
	
	public void update(ApprovalBatchFreight doc) {
		approvalBatchFreightDao.update(doc);
	}
	
		public List<ApprovalBatchFreight> searchByIdBatch(String idBatch){
			
			return approvalBatchFreightDao.searchByIdBatch(idBatch);
		}
}
