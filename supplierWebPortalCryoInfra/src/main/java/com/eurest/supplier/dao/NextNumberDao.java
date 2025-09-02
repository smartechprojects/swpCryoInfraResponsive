package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.NextNumber;

@Repository("nextNumberDao")
@Transactional
public class NextNumberDao{

	@Autowired
	SessionFactory sessionFactory;
		
	@SuppressWarnings("unchecked")
	public NextNumber getNextNumber(String module){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(NextNumber.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.eq("module", module))
				);
		List<NextNumber> list =  criteria.list();
		if(!list.isEmpty()) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	public void updateNextNumber(NextNumber o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
}
