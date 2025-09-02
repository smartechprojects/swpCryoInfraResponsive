package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.ApprovalBatchFreight;


@Repository("ApprovalBatchFreightDao")
@Transactional
public class ApprovalBatchFreightDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public ApprovalBatchFreight getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (ApprovalBatchFreight) session.get(ApprovalBatchFreight.class, id);
	}
	public void save(ApprovalBatchFreight o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	public void update(ApprovalBatchFreight o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void deleteBatchJournal(ApprovalBatchFreight o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}

	
	public List<ApprovalBatchFreight> searchByIdBatch(String idBatch){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ApprovalBatchFreight.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("batchID", idBatch))
				);
		return (List<ApprovalBatchFreight>) criteria.list();
	}
	

	
}
