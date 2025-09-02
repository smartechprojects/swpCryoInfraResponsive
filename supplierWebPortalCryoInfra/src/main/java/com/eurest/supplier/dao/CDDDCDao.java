package com.eurest.supplier.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.CDDDC;

@Repository("CDDDCDao")
@Transactional
public class CDDDCDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public void save(CDDDC o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	public void update(CDDDC o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}

	public void delete(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		CDDDC p = (CDDDC) session.load(CDDDC.class, new Integer(id));
		if(null != p){
			session.delete(p);
		}
	}
	
}
