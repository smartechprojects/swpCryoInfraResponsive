package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.BatchJournal;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.UserDocument;

@Repository("BatchJournalDao")
@Transactional
public class BatchJournalDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public BatchJournal getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (BatchJournal) session.get(BatchJournal.class, id);
	}
	public void save(BatchJournal o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	public void update(BatchJournal o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void deleteBatchJournal(BatchJournal o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}

	
	public List<BatchJournal> searchByIdBatch(String idBatch){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BatchJournal.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("batchID", idBatch))
				);
		return (List<BatchJournal>) criteria.list();
	}
	

	
}
