package com.eurest.supplier.dao;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.LogDataAprovalAction;

@Repository("logDataAprovalActionDao")
@Transactional
public class LogDataAprovalActionDao{

	@Autowired
	SessionFactory sessionFactory;
	
	public void save(LogDataAprovalAction o) {
		Session session = this.sessionFactory.getCurrentSession();
		o.setFechaIngreso(new Date());
		session.save(o);
	}
	
}