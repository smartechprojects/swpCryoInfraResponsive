package com.eurest.supplier.util;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.LogData;
import com.eurest.supplier.model.LogDataJEdwars;
import com.eurest.supplier.model.Supplier;

@Repository("LoggerJEdwars")
@Transactional
public class LoggerJEdwars {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public void putInitial(String url,String dataSend, String msg,String status) {
		Session session = this.sessionFactory.getCurrentSession();
		LogDataJEdwars obj = new LogDataJEdwars();
		
		
		obj.setFechaIngreso(new Date());
		obj.setFechaUltInt(new Date());
		obj.setIntentos(0);
		
		obj.setUrl(url);
		obj.setDataSend(dataSend);
		obj.setMesage(msg);
		obj.setStatus(status);
	    session.saveOrUpdate(obj);
	}
	
	public void putUpdate(LogDataJEdwars obj) {
		Session session = this.sessionFactory.getCurrentSession();
		obj.setIntentos(obj.getIntentos()+1);
		obj.setFechaUltInt(new Date());
	    session.saveOrUpdate(obj);
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<LogDataJEdwars> getLogDataToSend() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LogDataJEdwars.class);
		criteria.add(Restrictions.eq("status", AppConstants.LOGGER_JEDWARS_ERROR));
		
		return criteria.list();
	}
	

}
