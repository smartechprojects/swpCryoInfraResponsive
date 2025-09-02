package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.CodigosPostales;

@Repository("codigosPostalesDao")
@Transactional
public class CodigosPostalesDao {
	
	@Autowired
	SessionFactory sessionFactory;
	

	@SuppressWarnings("unchecked")
	public List<CodigosPostales> getByCode(String code,int start,int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CodigosPostales.class);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.like("codigo", "%" + code + "%" ))
				);
		return criteria.list();
	}
			
	
}
