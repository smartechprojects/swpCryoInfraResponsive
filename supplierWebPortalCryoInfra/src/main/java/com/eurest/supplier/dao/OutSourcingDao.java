package com.eurest.supplier.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.OutSourcingDocument;
import com.eurest.supplier.util.AppConstants;


@Repository("outSourcingDao")
@Transactional
public class OutSourcingDao{

	@Autowired
	SessionFactory sessionFactory;
	
	
	public OutSourcingDocument getDocumentById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (OutSourcingDocument) session.get(OutSourcingDocument.class, id);
	}
			
	@SuppressWarnings("unchecked")
	public List<OutSourcingDocument> searchCriteria(String query){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(OutSourcingDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressBook", "%" + query + "%"))
				.add(Restrictions.eq("documentType", "%" + query + "%"))
				);
		return (List<OutSourcingDocument>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<OutSourcingDocument> searchPendingDocuments(String addressNumber){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(OutSourcingDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressBook", addressNumber))
				.add(Restrictions.ne("docStatus", AppConstants.STATUS_ACCEPT))
				);
		return (List<OutSourcingDocument>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<OutSourcingDocument> searchByAttachID(String attachId){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(OutSourcingDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("attachId", attachId))
				);
		
		return (List<OutSourcingDocument>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<OutSourcingDocument> searchDocsByFrequency(String addressBook, String frequency){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(OutSourcingDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressBook", addressBook))
				.add(Restrictions.eq("frequency", frequency))
				);
		criteria.addOrder(Order.desc("uploadDate"));
		return (List<OutSourcingDocument>) criteria.list();
	}

	
	@SuppressWarnings("unchecked")
	public List<OutSourcingDocument> searchActiveDocsByFrequency(String addressBook, String frequency){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(OutSourcingDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressBook", addressBook))
				.add(Restrictions.eq("frequency", frequency))
				.add(Restrictions.eq("obsolete", false))
				);
		criteria.addOrder(Order.desc("uploadDate"));
		return (List<OutSourcingDocument>) criteria.list();
	}

	
	@SuppressWarnings("unchecked")
	public List<OutSourcingDocument> searchDocsByQuery(String supplierName, String status, String documentType, 
			                                           String supplierNumber, List<String> docTypeList, int start, int limit, int monthLoad, int yearLoad){
		
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(OutSourcingDocument.class);
		//boolean isFieldSelected = false;
		/*
		if(fromDate != null) {
			criteria.add(Restrictions.ge("uploadDate", fromDate));
			isFieldSelected = true;
		}
		
		if(toDate != null) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(toDate);
		    calendar.add(Calendar.HOUR_OF_DAY, 23);
		    toDate = calendar.getTime();
			criteria.add(Restrictions.le("uploadDate", toDate));
			isFieldSelected = true;
		}*/
		
		if(documentType != null && !"".equals(documentType)) {
			criteria.add(Restrictions.eq("documentType", documentType));
			//isFieldSelected = true;
		}
		
		if(!"".equals(supplierName)) {
			criteria.add(Restrictions.disjunction()
		      .add( Restrictions.like("supplierName", "%" + supplierName + "%") )
		      .add( Restrictions.like("rfc", "%" + supplierName + "%") ));
		}		
		
		if(!"".equals(supplierNumber)) {
			criteria.add(Restrictions.like("addressBook", "%" + supplierNumber + "%"));
			//isFieldSelected = true;
		}
		
		if(!"".equals(status)) {
			criteria.add(Restrictions.eq("docStatus", status));
			//isFieldSelected = true;
		}
		
		if(docTypeList != null) {
			if(docTypeList.size() > 0) {
				criteria.add(Restrictions.in("documentType", docTypeList));
			}
		}

		if(monthLoad != 0) {
			criteria.add(Restrictions.eq("monthLoad", monthLoad));
			
		}
		
		if(yearLoad != 0) {			
			criteria.add(Restrictions.eq("yearLoad", yearLoad));
		}
		
		criteria.setFirstResult(start); 
		criteria.setMaxResults(limit);
		
		criteria.addOrder(Order.desc("uploadDate"));
		
		/*if(isFieldSelected) {
			return (List<OutSourcingDocument>) criteria.list();
		}else {
			return null;
		}*/
		return (List<OutSourcingDocument>) criteria.list();
	}
	
	public int searchDocsByQueryCount(String supplierName, String status, String documentType, String supplierNumber, List<String> docTypeList, int monthLoad, int yearLoad){
		
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(OutSourcingDocument.class);
		/*
		if(fromDate != null) {
			criteria.add(Restrictions.ge("uploadDate", fromDate));
		}
		
		if(toDate != null) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(toDate);
		    calendar.add(Calendar.HOUR_OF_DAY, 23);
		    toDate = calendar.getTime();
			criteria.add(Restrictions.le("uploadDate", toDate));
		}*/
		
		if(documentType != null && !"".equals(documentType)) {
			criteria.add(Restrictions.eq("documentType", documentType));
		}
		
		if(!"".equals(supplierName)) {
			criteria.add(Restrictions.like("supplierName", "%" + supplierName + "%"));
		}
		
		if(!"".equals(supplierNumber)) {
			criteria.add(Restrictions.like("addressBook", "%" + supplierNumber + "%"));
		}
		
		if(!"".equals(status)) {
			criteria.add(Restrictions.eq("docStatus", status));
		}
				
		if(docTypeList != null) {
			if(docTypeList.size() > 0) {
				criteria.add(Restrictions.in("documentType", docTypeList));
			}
		}
		
		if(monthLoad != 0 ) {
			criteria.add(Restrictions.eq("monthLoad", monthLoad));
		}
		
		if(yearLoad != 0) {
			criteria.add(Restrictions.eq("yearLoad", yearLoad));
		}
		
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count.intValue();
		
	}
	
	public OutSourcingDocument saveDocument(OutSourcingDocument o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	    return o;	
	}

	public void updateDocument(OutSourcingDocument o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}

	public void updateDocumentList(List<OutSourcingDocument> list) {
		Session session = this.sessionFactory.getCurrentSession();
		for(OutSourcingDocument o : list) {
		    session.update(o);
		}
	}
	
	public void deleteDocument(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		OutSourcingDocument p = (OutSourcingDocument) session.load(OutSourcingDocument.class, new Integer(id));
		if(null != p){
			session.delete(p);
		}
	}
	
	public int getTotalRecords(){
		Session session = this.sessionFactory.getCurrentSession();
		Long count = (Long) session.createQuery("select count(*) from  OutSourcingDocument").uniqueResult();
		return count.intValue();
		
	}
	

}
