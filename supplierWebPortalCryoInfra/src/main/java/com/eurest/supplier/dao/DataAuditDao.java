package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.DataAudit;

@Repository("dataAuditDao")
@Transactional
public class DataAuditDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public DataAudit getDataAuditById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		DataAudit data = (DataAudit) session.get(DataAudit.class, id);
		return data;
	}
	
	public DataAudit save(DataAudit o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	    return o;	
	}
	
	public void update(DataAudit o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void saveDataAudit(List<DataAudit> list) {
	    Session session = this.sessionFactory.getCurrentSession();
	    int i = 0;
	    session.setCacheMode(CacheMode.IGNORE);
	    session.setFlushMode(FlushMode.COMMIT);
	    for (DataAudit o : list) {
	      session.saveOrUpdate(o);
	      if (i % 50 == 0) {
	        session.flush();
	        session.clear();
	      } 
	      i++;
	    } 
	  }
	
	

}
