package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.FileStore;
import com.eurest.supplier.model.PlantAccessWorker;

@Repository("plantAccessWorkerDao")
@Transactional
public class PlantAccessWorkerDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public PlantAccessWorker getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (PlantAccessWorker) session.get(PlantAccessWorker.class, id);
	}
	
	public List<PlantAccessWorker> searchWorkersPlantAccessByIdRequest(String id) {
		Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(PlantAccessWorker.class);
	   /* criteria.add(
	        (Criterion)Restrictions.disjunction()
	        .add((Criterion)Restrictions.eq("uuid", uuid)));*/
	    criteria.add((Criterion)Restrictions.eq("requestNumber", id));
	    List<PlantAccessWorker> list = criteria.list();
	    /*if (!list.isEmpty())
	      return list.get(0); */
	    return list;
	}
			
//	@SuppressWarnings("unchecked")
//	public List<PlantAccessWorker> PlantAccessWorker(String rfc,
//			                                      String status,
//			                                      int start,
//			                                      int limit) {
//		Session session = this.sessionFactory.getCurrentSession();
//		Criteria criteria = session.createCriteria(PlantAccessRequest.class);
//		criteria.setFirstResult(start);
//		criteria.setMaxResults(limit);
//		criteria.add(
//				Restrictions.conjunction()
//				.add(Restrictions.like("rfc", "%" + rfc + "%"))
//				.add(Restrictions.like("status", "%"+status+"%"))
//				);
//		return criteria.list();
//	}
//	
	
//	@SuppressWarnings("rawtypes")
//	public int getTotalRecords(String rfc, String status,int start, int limit) {
//		Session session = this.sessionFactory.getCurrentSession();
//		Criteria criteria = session.createCriteria(PlantAccessRequest.class);
//		criteria.setFirstResult(start);
//		criteria.setMaxResults(limit);
//		
//	
//			if(!"".equals(rfc)) criteria.add(Restrictions.eq("rfc", rfc));			
//			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
//			criteria.setProjection(Projections.rowCount());
//
//            List result = criteria.list();
//            if (!result.isEmpty()) {
//                Long rowCount = (Long) result.get(0);
//                return rowCount.intValue();
//            } else {
//            	return 0;
//            }
//		
//	}
	
	
	public void save(PlantAccessWorker o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	public void update(PlantAccessWorker o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void delete(PlantAccessWorker o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}
	public int updateWorkerRequest(String uuid,String idref) {
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "update PlantAccessWorker set requestNumber = :newrequestNumber where requestNumber = :requestNumber"; 
		Query query = session.createQuery(hql); 
		query.setString("newrequestNumber", idref);
		query.setString("requestNumber", uuid);
	    return query.executeUpdate();
	}

}
