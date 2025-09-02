package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.CustomBroker;
import com.eurest.supplier.util.AppConstants;

@Repository("customBrokerDao")
@Transactional
public class CustomBrokerDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public CustomBroker getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (CustomBroker) session.get(CustomBroker.class, id);
	}
			
	@SuppressWarnings("unchecked")
	public List<CustomBroker> getCustomBroker(String addressNumber,
			                                      String status,
			                                      int start,
			                                      int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CustomBroker.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("CustomBrokerConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.like("addressNumber", "%" + addressNumber + "%"))
				//.add(Restrictions.eq("status", status))
				);
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	public List<CustomBroker> getCustomBroker(String addressNumber,
			                                      String status,
			                                      String uuid,
			                                      String documentType,
			                                      int start,
			                                      int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CustomBroker.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("CustomBrokerConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		if(!"".equals(uuid) || !"".equals(status) || !"".equals(documentType) || !"".equals(addressNumber)) {
			if(!"".equals(uuid)) criteria.add(Restrictions.eq("uuidFactura", uuid));			
			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
			if(!"".equals(documentType)) criteria.add(Restrictions.eq("type", documentType));
			if(!"".equals(addressNumber)) criteria.add(Restrictions.like("addressNumber", "" + addressNumber + ""));
			criteria.addOrder(Order.desc("id"));
			return criteria.list();
		} else {
			return null;
		}
		
		/*
		if("FACTURA".equals(documentType) || "FACT EXTRANJERO".equals(documentType)) {			
			if(!"".equals(uuid)) criteria.add(Restrictions.like("uuidFactura", "%" + uuid + "%"));			
			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
			if(!"".equals(documentType)) criteria.add(Restrictions.eq("type", documentType));
			if(!"".equals(addressNumber)) criteria.add(Restrictions.like("addressNumber", "%" + addressNumber + "%"));
			
			return criteria.list();
			//criteria.add(
					//Restrictions.conjunction()
					//.add(Restrictions.like("addressNumber", "%" + addressNumber + "%"))
					//.add(Restrictions.like("uuidFactura", "%" + uuid + "%"))
					////.add(Restrictions.eq("status", status))
					//);
		}else if("NOTACREDITO".equals(documentType)) {
			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
			if(!"".equals(addressNumber)) criteria.add(Restrictions.like("addressNumber", "%" + addressNumber + "%"));
			if(!"".equals(documentType)) criteria.add(Restrictions.eq("type", documentType));
			
			return criteria.list();			
		}		
		return null;
		*/
	}
	
	@SuppressWarnings("rawtypes")
	public int getTotalRecords(String addressNumber, String status, String uuid, String documentType, int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CustomBroker.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("CustomBrokerConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		if(!"".equals(uuid) || !"".equals(status) || !"".equals(documentType) || !"".equals(addressNumber)) {
			if(!"".equals(uuid)) criteria.add(Restrictions.eq("uuidFactura", uuid));			
			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
			if(!"".equals(documentType)) criteria.add(Restrictions.eq("type", documentType));
			if(!"".equals(addressNumber)) criteria.add(Restrictions.like("addressNumber", "" + addressNumber + ""));
			criteria.setProjection(Projections.rowCount());

            List result = criteria.list();
            if (!result.isEmpty()) {
                Long rowCount = (Long) result.get(0);
                return rowCount.intValue();
            } else {
            	return 0;
            }
		} else {
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CustomBroker> getPendingPaymentInvoices(String addressNumber,
					                                       String folio) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CustomBroker.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("CustomBrokerConcept", FetchMode.SELECT);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.like("addressNumber", "%" + addressNumber + "%"))
				.add(Restrictions.eq("uuidPago", ""))
				.add(Restrictions.ne("folio", folio.trim()))
				);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<CustomBroker> getComplPendingInvoice(String addressBook) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CustomBroker.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber",addressBook))
				.add(Restrictions.ne("paymentAmount",0d)));

		criteria.add(Restrictions.or(Restrictions.isNotNull("uuidFactura"), Restrictions.ne("uuidFactura", "")));
		criteria.add(Restrictions.eq("status", AppConstants.STATUS_PAID));
		criteria.addOrder(Order.asc("invoiceUploadDate"));
		return criteria.list();
	}
	
	public void saveDocument(CustomBroker o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	public void updateDocument(CustomBroker o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void deleteCustomBroker(CustomBroker o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}

	public CustomBroker getCustomBrokerByUuid(String uuid){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CustomBroker.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.eq("uuidFactura", uuid))
				);
		@SuppressWarnings("unchecked")
		List<CustomBroker> list =  criteria.list();
		if(!list.isEmpty()) {
			return list.get(0);
		}else {
			return null;
		}
	}
}
