package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.CDDDCEmployee;
import com.eurest.supplier.model.OutSourcingDocument;

@Repository("CDDDCEmployeeDao")
@Transactional
public class CDDDCEmployeeDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public void save(CDDDCEmployee o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	

	public void update(CDDDCEmployee o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}

	public void delete(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		CDDDCEmployee p = (CDDDCEmployee) session.load(CDDDCEmployee.class, new Integer(id));
		if(null != p){
			session.delete(p);
		}
	}
	
	public  List<CDDDCEmployee> getListNominal(String periodo){
		Session session = this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		
		List<CDDDCEmployee> res = (List<CDDDCEmployee>) session.createQuery(" from CDDDCEmployee "
				+ " where cdddc_id  in (select id from CDDDC where lower(periododeProceso) = '"+periodo.toLowerCase()+"')").list();
		return res;
		
	}
	
}