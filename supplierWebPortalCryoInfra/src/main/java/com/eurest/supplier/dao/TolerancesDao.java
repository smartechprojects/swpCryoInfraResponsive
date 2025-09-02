package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.Tolerances;

@Repository("tolerancesDao")
@Transactional
public class TolerancesDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<Tolerances> getItemTolerances(List<Integer> items){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Tolerances.class);
		criteria.add(Restrictions.in("itemNumber", items));
		return (List<Tolerances>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Tolerances> getTolerances(){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Tolerances.class);
		return (List<Tolerances>) criteria.list();
	}
	
	public void updateTolerance(Tolerances o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void saveMultiple(List<Tolerances> list) {
		Session session = this.sessionFactory.getCurrentSession();
		for(Tolerances o : list) {
		    session.saveOrUpdate(o);
		}
	}
	
	public int deleteRecords(){
		Session session = this.sessionFactory.getCurrentSession();
	    String hql = String.format("delete from %s","Tolerances");
	    Query query = session.createQuery(hql);
	    return query.executeUpdate();
	}


}
