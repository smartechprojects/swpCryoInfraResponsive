package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.UserDocument;

@Repository("FiscalDocumentConceptDao")
@Transactional
public class FiscalDocumentConceptDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public FiscalDocumentsConcept getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (FiscalDocumentsConcept) session.get(FiscalDocumentsConcept.class, id);
	}
	public void saveDocumentConcept(FiscalDocumentsConcept o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	public void updateDocumentConcept(FiscalDocumentsConcept o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void deleteFiscalDocumentConcept(FiscalDocumentsConcept o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}

	public FiscalDocumentsConcept getFiscalDocumentsConceptByUuid(String uuid){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocumentsConcept.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.eq("uuidFactura", uuid))
				);
		@SuppressWarnings("unchecked")
		List<FiscalDocumentsConcept> list =  criteria.list();
		if(!list.isEmpty()) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
		public List<FiscalDocumentsConcept> getFiscalDocumentsConceptListByUuid(String uuid){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocumentsConcept.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.eq("uuidFactura", uuid))
				);
		
		return criteria.list();
		
	}
	
	public List<FiscalDocumentsConcept> searchByIdBatch(String idBatch){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocumentsConcept.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("batchID", idBatch))
				);
		return (List<FiscalDocumentsConcept>) criteria.list();
	}
	

	
}
