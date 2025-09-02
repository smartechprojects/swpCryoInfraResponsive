package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.Company;

@Repository("companyDao")
@Transactional
public class CompanyDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public Company getCompanyById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Company) session.get(Company.class, id);
	}
		
	public Company saveCompany(Company o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
		return o;
	}

	public Company updateCompany(Company o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(o);
		return o;
	}
	
	@SuppressWarnings("unchecked")
	public List<Company> searchCriteria(String query, int start, int limit){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.like("company", "%" + query + "%"))
				.add(Restrictions.like("companyName", "%" + query + "%"))
				);
		criteria.addOrder(Order.asc("company"));
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		return (List<Company>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Company> searchActiveCompanies(String company){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		
		if(!"".equals(company)) {
			criteria.add(Restrictions.eq("company", company));
		}
		
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("active", true))
				);
						
		return (List<Company>) criteria.list();
	}
	
		@SuppressWarnings("unchecked")
	public List<Company> getListCompany(){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("active", true))
				);
						
		return (List<Company>) criteria.list();
	}
	public int searchCriteriaCount(String query){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.like("company", "%" + query + "%"))
				.add(Restrictions.like("companyName", "%" + query + "%"))
				);
		
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count.intValue();
	}
	
	@SuppressWarnings("unchecked")
	public Company searchByCompany(String company){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		criteria.add(Restrictions.eq("company", company));
		List<Company> list = criteria.list();
		if(list != null){
			if(!list.isEmpty())
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}

}
