package com.eurest.supplier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.BatchJournalDao;
import com.eurest.supplier.model.BatchJournal;

@Service("BatchJournalService")
public class BatchJournalService {
	

	@Autowired
	BatchJournalDao batchJournalDao;
	
	
	public BatchJournal getById(int id) {
		return batchJournalDao.getById(id);
	}
	
	public void save(BatchJournal doc) {
		batchJournalDao.save(doc);
	}
	
	public void update(BatchJournal doc) {
		batchJournalDao.update(doc);
	}
	
		public List<BatchJournal> searchByIdBatch(String idBatch){
			
			return batchJournalDao.searchByIdBatch(idBatch);
		}
}
