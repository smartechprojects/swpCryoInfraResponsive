package com.eurest.supplier.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.NonComplianceSupplier;

@Repository("nonComplianceSupplierDao")
@Transactional
public class NonComplianceSupplierDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	private Logger log4j = Logger.getLogger(NonComplianceSupplierDao.class);

	public NonComplianceSupplier getNonComplianceSupplierById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		NonComplianceSupplier s = (NonComplianceSupplier) session.get(NonComplianceSupplier.class, id);
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public List<NonComplianceSupplier> getList(int start,int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(NonComplianceSupplier.class);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		return criteria.list();
	}
			
	@SuppressWarnings("unchecked")
	public List<NonComplianceSupplier> searchByCriteria(String query,
			                                      int start,
			                                      int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(NonComplianceSupplier.class);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.like("taxId", "%" + query + "%" ))
				.add(Restrictions.like("supplierName", "%" + query + "%" )));

		return criteria.list(); 
	}
	
	@SuppressWarnings("unchecked")
	public NonComplianceSupplier searchByTaxId(String query,
			                                      int start,
			                                      int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(NonComplianceSupplier.class);
		criteria.add((Restrictions.eq("taxId", query)));

		List<NonComplianceSupplier> list = criteria.list();
		if(list != null) {
			if(list.size() > 0) {
				return list.get(0);
			}
		}else {
			return null;
		}
		return null;
	}
	
	public long searchByCriteriaTotalRecords(String query) {
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "SELECT COUNT(*) FROM NonComplianceSupplier WHERE taxId like :taxId OR supplierName like :supplierName";
		Query q = session.createQuery(sql);
		q.setParameter("taxId", "%" + query + "%");
		q.setParameter("supplierName", "%" + query + "%");
		return (Long) q.setMaxResults(1).uniqueResult();
	}
	
	public void saveSuppliers(List<NonComplianceSupplier> list) {
		Session session = this.sessionFactory.getCurrentSession();
		int i=0;
		session.setCacheMode(CacheMode.IGNORE);
		session.setFlushMode(FlushMode.COMMIT);
		for(NonComplianceSupplier o : list) {
		   session.save(o);
		   if( i % 50 == 0 ) { // Same as the JDBC batch size
		      //flush a batch of inserts and release memory:
		      session.flush();
		      session.clear();
		      log4j.info("registrados: " + i);
		   }
		   i++;
		}
	}
	
	public void deleteAll() {
		try {
			Connection c = this.sessionFactory.
			        getSessionFactoryOptions().getServiceRegistry().
			        getService(ConnectionProvider.class).getConnection();		
			Statement statement = c.createStatement();
			statement.executeUpdate("delete from  noncompliancesupplier;");
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}
	
	public int getTotalRecords(){
		Session session = this.sessionFactory.getCurrentSession();
		Long count = (Long) session.createQuery("select count(*) from NONCOMPLIANCESUPPLIER").uniqueResult();
		return count.intValue();
	}
	
}
