package com.eurest.supplier.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.InvoiceSAT;
import com.eurest.supplier.model.InvoiceSATRequest;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.PurchaseOrderDetail;
import com.eurest.supplier.util.AppConstants;


@Repository("invoiceSATDao")
@Transactional
public class InvoiceSATDao{

	@Autowired
	SessionFactory sessionFactory;
	
	private Logger log4j = Logger.getLogger(InvoiceSATDao.class);
	
	
	public InvoiceSAT saveDocuments(InvoiceSAT o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	    return o;	
	}

	public void updateDocuments(InvoiceSAT o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}

	public void updateDocumentList(List<InvoiceSAT> list) {
		Session session = this.sessionFactory.getCurrentSession();
		for(InvoiceSAT o : list) {
		    session.update(o);
		}
	}
	
	public void deleteDocumentList(List<InvoiceSAT> list) {
		Session session = this.sessionFactory.getCurrentSession();
		for(InvoiceSAT o : list) {
		    session.delete(o);
		}
	}
	
	public void deleteDocuments(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		InvoiceSAT p = (InvoiceSAT) session.load(InvoiceSAT.class, new Integer(id));
		if(null != p){
			session.delete(p);
		}
	}
	
	public int getTotalRecords(){
		Session session = this.sessionFactory.getCurrentSession();
		Long count = (Long) session.createQuery("select count(*) from  InvoiceSAT").uniqueResult();
		return count.intValue();
		
	}
	

	
	@SuppressWarnings("unchecked")
	public List<InvoiceSAT> searchCriteriaByUuidOnly(String uuid){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(InvoiceSAT.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("uuid", uuid))
				);

		return (List<InvoiceSAT>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public InvoiceSAT getByUuid(String uuid){
		List<InvoiceSAT>list=searchCriteriaByUuidOnly(uuid);
		if(list.size()==0) {
			return null;
		}
		return list.get(0);
	}
	
	
	public InvoiceSATRequest saveDocuments(InvoiceSATRequest o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	    return o;	
	}

	public void updateDocuments(InvoiceSATRequest o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public List<InvoiceSATRequest> getRequestByStatus(String status){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(InvoiceSATRequest.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("status", status))
				);

		return (List<InvoiceSATRequest>) criteria.list();
	}
	
	public synchronized List<InvoiceSAT> saveMultiple(List<InvoiceSAT> list) {
		try {
		Session session = this.sessionFactory.getCurrentSession();
		session.setCacheMode(CacheMode.IGNORE);
		session.setFlushMode(FlushMode.COMMIT);
		log4j.info("*********** STEP 7: afterFlush:" + list.size());
		int i=0;
			for(InvoiceSAT o : list) {

				   session.save(o);
				  
				   if( i % 50 == 0 ) {
					   
					      session.flush();
					      session.clear();
					   }
				   i++;
				
			}
			
			session.flush();
		      session.clear();
				
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			
		}
		return list;
	}



}
