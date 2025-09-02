package com.eurest.supplier.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.CodigosSAT;

@Repository("codigosSatDao")
@Transactional
public class CodigosSatDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DataSource dataSource;
	
	private Logger log4j = Logger.getLogger(CodigosSatDao.class);
	
	@SuppressWarnings("unchecked")
	public List<CodigosSAT> getOrders(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from CodigosSAT");
	    q.setFirstResult(start); // modify this to adjust paging
	    q.setMaxResults(limit);
		return (List<CodigosSAT>) q.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getCodeList() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CodigosSAT.class).setProjection(Projections.property("codigoSAT"));
		List<String> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CodigosSAT> findCodes(List<String> list) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CodigosSAT.class);
		criteria.add(Restrictions.in("codigoSAT", list));
		return criteria.list();
	}
	
	public void saveMultiple(List<CodigosSAT> list) {
		/*
		Session session = this.sessionFactory.getCurrentSession();
		int i=0;
		for(CodigosSAT o : list) {
		   session.saveOrUpdate(o);
		   if( i % 100 == 0 ) {
			      session.flush();
			      session.clear();
			      log4j.info("Registros SAT guardados:" + i);
			   }
		   i++; 
		}
		*/
		try {
		final int batchSize = 200;
		Connection db_connection = dataSource.getConnection();
		db_connection.setAutoCommit(false);
		int i=0;
		PreparedStatement prepStmt = db_connection.prepareStatement("INSERT INTO codigossat (codigoSAT, tipoCodigo, descripcion) VALUES (?, ?, ?)");
		for(CodigosSAT o : list) {
		    prepStmt.setString(1,o.getCodigoSAT()); 
		    prepStmt.setString(2,o.getTipoCodigo());
		    prepStmt.setString(3,o.getDescripcion());
		    prepStmt.addBatch();
		      if(++i % batchSize == 0) {
		    	log4j.info("Inserción de " + i);
		    	prepStmt.executeBatch();
		    	db_connection.commit();
			  }
		}
		prepStmt.executeBatch();
		db_connection.commit();
		prepStmt.close();
		log4j.info("Inserción completa");
		
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}
		
	public int deleteRecords(){
		Session session = this.sessionFactory.getCurrentSession();
	    String hql = String.format("delete from %s","CodigosSAT");
	    Query query = session.createQuery(hql);
	    return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<CodigosSAT> searchByCriteria(String query,
					                                      int start,
					                                      int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CodigosSAT.class);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.like("codigoSAT", "%" + query + "%" )));

		return criteria.list(); 
	}
	
	@SuppressWarnings("unchecked")
	public List<CodigosSAT> searchByTipoCodigo(String tipoCodigo) {
	Session session = this.sessionFactory.getCurrentSession();
	Criteria criteria = session.createCriteria(CodigosSAT.class);
	criteria.add(
	Restrictions.disjunction()
	.add(Restrictions.eq("tipoCodigo", tipoCodigo )));
	
	return criteria.list(); 
	}
	
	public int getTotalRecords(){
		Session session = this.sessionFactory.getCurrentSession();
		Long count = (Long) session.createQuery("select count(*) from  CodigosSAT").uniqueResult();
		return count.intValue();
	}
	
	public long searchByCriteriaTotalRecords(String query) {
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "SELECT COUNT(*) FROM CodigosSAT WHERE codigoSAT like :codigoSAT ";
		Query q = session.createQuery(sql);
		q.setParameter("codigoSAT", "%" + query + "%");
		return (Long) q.setMaxResults(1).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<CodigosSAT> searchBySupplier(String supplier,
					                         int start,
					                         int limit, 
					                         String company) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CodigosSAT.class);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		if(!"".equals(company)) {
			criteria.add(Restrictions.eq("company", company));
		}
		
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.like("addressNumber", supplier )));

		return criteria.list(); 
	}

}
