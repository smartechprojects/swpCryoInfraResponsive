package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.PlantAccessRequest;
import com.eurest.supplier.model.Supplier;

@Repository("plantAccessRequestDao")
@Transactional
public class PlantAccessRequestDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public PlantAccessRequest getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (PlantAccessRequest) session.get(PlantAccessRequest.class, id);
	}
	
	public PlantAccessRequest getPlantAccessRequests(String uuid) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(PlantAccessRequest.class);
	    criteria.add(
	        (Criterion)Restrictions.disjunction()
	        .add((Criterion)Restrictions.eq("rfc", uuid)));
	    List<PlantAccessRequest> list = criteria.list();
	    if (!list.isEmpty())
	      return list.get(0); 
	    return null;
	  }
			
	@SuppressWarnings("unchecked")
	public List<PlantAccessRequest> getPlantAccessRequest(String rfc,
			                                      String status,
			                                      String approver,
			                                      String addressNumberPA,
			                                      int start,
			                                      int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PlantAccessRequest.class);
//		criteria.setFirstResult(start);
//		criteria.setMaxResults(limit);
		
		if(!rfc.equals(""))
		criteria.add(Restrictions.like("nameRequest", "%" + rfc + "%"));
		
		if(!status.equals(""))
		criteria.add(Restrictions.like("status", "%"+status+"%"));
		
		if(!approver.equals(""))
			criteria.add(Restrictions.like("aprovUserDef", "%"+approver+"%"));
		
		if(!addressNumberPA.equals(""))
			criteria.add(Restrictions.like("addressNumberPA", "%"+addressNumberPA+"%"));
		
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}
	
	
	@SuppressWarnings("rawtypes")
	public int getTotalRecords(String rfc, String status,int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PlantAccessRequest.class);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
	
			if(!"".equals(rfc)) criteria.add(Restrictions.eq("rfc", rfc));			
			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
			criteria.setProjection(Projections.rowCount());

            List result = criteria.list();
            if (!result.isEmpty()) {
                Long rowCount = (Long) result.get(0);
                return rowCount.intValue();
            } else {
            	return 0;
            }
		
	}
	
	
	public void save(PlantAccessRequest o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	public void update(PlantAccessRequest o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void deleteFiscalDocument(PlantAccessRequest o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}


}
