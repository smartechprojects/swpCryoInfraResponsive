package com.eurest.supplier.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.PaymentCalendar;

@Repository("paymentCalendarDao")
@Transactional
public class PaymentCalendarDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<PaymentCalendar> getPaymentCalendarByYear(int year,int start, int limit){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PaymentCalendar.class);
		criteria.add(Restrictions.ge("year", year));
		return (List<PaymentCalendar>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentCalendar> getPaymentCalendarFromToday(Date dateFrom,int start, int limit){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PaymentCalendar.class);
		criteria.add(Restrictions.gt("paymentDate", dateFrom));
		criteria.addOrder(Order.asc("paymentDate"));
		return (List<PaymentCalendar>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentCalendar> getPaymentCalendarList(int start, int limit){
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from PaymentCalendar order by paymentDate");
	    q.setFirstResult(start); 
	    q.setMaxResults(limit);
		return (List<PaymentCalendar>) q.list();
	}
	
	public void updatePaymentCalendar(PaymentCalendar o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void saveMultiple(List<PaymentCalendar> list) {
		Session session = this.sessionFactory.getCurrentSession();
		for(PaymentCalendar o : list) {
		    session.saveOrUpdate(o);
		}
	}
	
	public int deleteRecords(int year){
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "delete from PaymentCalendar where year = :year"; 
		Query query = session.createQuery(hql); 
		query.setInteger("year", year);  
	    return query.executeUpdate();
	}

	public int getTotalRecords(){
		Session session = this.sessionFactory.getCurrentSession();
		Long count = (Long) session.createQuery("select count(*) from  PaymentCalendar").uniqueResult();
		return count.intValue();
	}

}
