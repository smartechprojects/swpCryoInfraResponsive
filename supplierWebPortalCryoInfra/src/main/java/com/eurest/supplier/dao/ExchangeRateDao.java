package com.eurest.supplier.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.ExchangeRate;

@Repository("exchangeRateDao")
@Transactional
public class ExchangeRateDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public List<ExchangeRate> getExchangeRateFromToday(Date dateFrom, String currencyCode, String currencyCodeTo, int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ExchangeRate.class);
		criteria.add(Restrictions.eq("currencyCode", currencyCode));
		criteria.add(Restrictions.eq("currencyCodeTo", currencyCodeTo));
		criteria.add(Restrictions.eq("effectiveDate", dateFrom));
		criteria.addOrder(Order.desc("effectiveDate"));
		return (List<ExchangeRate>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<ExchangeRate> getExchangeRateBeforeToday(Date dateFrom, String currencyCode, String currencyCodeTo, int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ExchangeRate.class);
		criteria.add(Restrictions.eq("currencyCode", currencyCode));
		criteria.add(Restrictions.eq("currencyCodeTo", currencyCodeTo));
		criteria.add(Restrictions.lt("effectiveDate", dateFrom));
		criteria.addOrder(Order.desc("effectiveDate"));
		return (List<ExchangeRate>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<ExchangeRate> getExchangeRateAfterToday(Date dateFrom, String currencyCode, String currencyCodeTo, int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ExchangeRate.class);
		criteria.add(Restrictions.eq("currencyCode", currencyCode));
		criteria.add(Restrictions.eq("currencyCodeTo", currencyCodeTo));
		criteria.add(Restrictions.lt("effectiveDate", dateFrom));
		criteria.addOrder(Order.asc("effectiveDate"));
		return (List<ExchangeRate>) criteria.list();
	}

	public List<ExchangeRate> saveMultipleExchangeRate(List<ExchangeRate> objList) {
		Session session = this.sessionFactory.getCurrentSession();
		int i=0;
		List<ExchangeRate> newList = new ArrayList<ExchangeRate>();
		session.setCacheMode(CacheMode.IGNORE);
		session.setFlushMode(FlushMode.COMMIT);
		for(ExchangeRate o : objList) {
			ExchangeRate er = searchExchangeRateByDate(o);
		   if(er == null) {
			   o.setUpdatedDate(new Date());
			   newList.add(o);
		   }
		}
		
		for(ExchangeRate o : newList) {
		   session.saveOrUpdate(o);
		   if( i % 50 == 0 ) {
			      session.flush();
			      session.clear();
			   }
		   i++; 
	    }
		return newList;
	}

	@SuppressWarnings("unchecked")
	private ExchangeRate searchExchangeRateByDate(ExchangeRate o) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ExchangeRate.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("currencyCode", o.getCurrencyCode()))
				.add(Restrictions.eq("currencyCodeTo", o.getCurrencyCodeTo()))
				.add(Restrictions.eq("day", o.getDay()))
				.add(Restrictions.eq("month", o.getMonth()))
				.add(Restrictions.eq("year", o.getYear()))
				);
		
		List<ExchangeRate> list = criteria.list();
		if(list != null){
			if(!list.isEmpty())
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}
}
