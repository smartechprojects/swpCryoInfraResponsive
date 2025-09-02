package com.eurest.supplier.util;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.LogData;

@Repository("Logger")
@Transactional
public class Logger {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public void log(String logType, String msg) {
		Session session = this.sessionFactory.getCurrentSession();
		LogData obj = new LogData();
		obj.setDate(new Date());
		obj.setLogType(logType);
		obj.setMesage(msg);
	    session.saveOrUpdate(obj);
	}
	
	@SuppressWarnings("unchecked")
	public List<LogData> getLogDataBayDate(Date startDate, Date endDate, String logType, int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("FROM LogData AS c WHERE c.date BETWEEN :stDate AND :edDate AND logType LIKE :logType order by c.date DESC ")
		.setParameter("stDate", startDate)
		.setParameter("edDate", endDate)
		.setParameter("logType", "%" + logType + "%");
	    q.setFirstResult(start);
	    q.setMaxResults(limit);
		return (List<LogData>) q.list();
	}

		public void log(String logType, String msg, String company) {
		Session session = this.sessionFactory.getCurrentSession();
		LogData obj = new LogData();
		obj.setDate(new Date());
		obj.setLogType(logType);
		obj.setMesage(msg);
		obj.setCompany(company);
	    session.saveOrUpdate(obj);
	}
	

}
