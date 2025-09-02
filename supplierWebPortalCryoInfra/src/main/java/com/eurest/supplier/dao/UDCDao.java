package com.eurest.supplier.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.UDC;
import com.eurest.supplier.util.AppConstants;

@Repository("udcDao")
@Transactional
public class UDCDao{

	@Autowired
	SessionFactory sessionFactory;

	public UDC getUDCById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (UDC) session.get(UDC.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<UDC> getUDCList(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from UDC");
	    q.setFirstResult(start); // modify this to adjust paging
	    q.setMaxResults(limit);
		return (List<UDC>) q.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UDC> searchCriteria(String query){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.like("udcSystem", "%" + query + "%"))
				.add(Restrictions.like("udcKey", "%" + query + "%"))
				.add(Restrictions.like("systemRef", "%" + query + "%"))
				.add(Restrictions.like("keyRef", "%" + query + "%"))
				.add(Restrictions.like("strValue1", "%" + query + "%"))
				.add(Restrictions.like("strValue2", "%" + query + "%"))
				);
		criteria.addOrder(Order.asc("strValue1"));
		return (List<UDC>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public UDC searchBySystemAndKey(String udcSystem, String udcKey){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", udcSystem.trim()))
				.add(Restrictions.like("udcKey", udcKey.trim()))
				);
		criteria.addOrder(Order.asc("strValue1"));
	   List<UDC> list =  criteria.list();
	   if(list != null){
		   if(!list.isEmpty())
			   return list.get(0);
		   else 
			   return null;
	   }else{
		   return null;
	   }
	}
	
	@SuppressWarnings("unchecked")
	public UDC searchBySystemAndKeyRef(String udcSystem, String udcKey, String systemRef){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", udcSystem.trim()))
				.add(Restrictions.eq("udcKey", udcKey.trim()))
				.add(Restrictions.eq("keyRef", systemRef.trim()).ignoreCase())
				);
		criteria.addOrder(Order.asc("strValue1"));
	   List<UDC> list =  criteria.list();
	   if(list != null){
		   if(!list.isEmpty())
			   return list.get(0);
		   else 
			   return null;
	   }else{
		   return null;
	   }
	}

	@SuppressWarnings("unchecked")
	public UDC searchBySystemAndSystemRef(String udcSystem, String udcKey, String systemRef){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", udcSystem.trim()))
				.add(Restrictions.eq("udcKey", udcKey.trim()))
				.add(Restrictions.eq("systemRef", systemRef.trim()).ignoreCase())
				);
		criteria.addOrder(Order.asc("strValue1"));
	   List<UDC> list =  criteria.list();
	   if(list != null){
		   if(!list.isEmpty())
			   return list.get(0);
		   else 
			   return null;
	   }else{
		   return null;
	   }
	}
	
	public UDC searchBySystemAndSystemRefAndKeyRef(String udcSystem, String udcKey, String systemRef, String keyRef){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", udcSystem.trim()))
				.add(Restrictions.eq("udcKey", udcKey.trim()))
				.add(Restrictions.eq("systemRef", systemRef.trim()))
				.add(Restrictions.eq("keyRef", keyRef.trim()).ignoreCase())
				);
		criteria.addOrder(Order.asc("strValue1"));
	   List<UDC> list =  criteria.list();
	   if(list != null){
		   if(!list.isEmpty())
			   return list.get(0);
		   else 
			   return null;
	   }else{
		   return null;
	   }
	}
	
	@SuppressWarnings("unchecked")
	public UDC searchBySystemAndStrValue(String udcSystem, String udcKey, String strValue){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", udcSystem.trim()))
				.add(Restrictions.like("udcKey", udcKey.trim()))
				.add(Restrictions.like("strValue1", strValue.trim()))
				);
		criteria.addOrder(Order.asc("strValue1"));
	   List<UDC> list =  criteria.list();
	   if(list != null){
		   if(!list.isEmpty())
			   return list.get(0);
		   else 
			   return null;
	   }else{
		   return null;
	   }
	}
	
	@SuppressWarnings("unchecked")
	public UDC searchBySystemAndStrValue1(String udcSystem, String strValue){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", udcSystem.trim()))
			    .add(Restrictions.eq("strValue1", strValue.trim()))
				);
		criteria.addOrder(Order.asc("strValue1"));
	   List<UDC> list =  criteria.list();
	   if(list != null){
		   if(!list.isEmpty())
			   return list.get(0);
		   else 
			   return null;
	   }else{
		   return null;
	   }
	}
	
	@SuppressWarnings("unchecked")
	public List<UDC> searchBySystem(String udcSystem){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", udcSystem.trim()))
				);
		criteria.addOrder(Order.asc("strValue1"));
	   return  criteria.list();	   
	}

	@SuppressWarnings("unchecked")
	public List<UDC> advaceSearch(String udcSystem, String udcKey,String systemRef,String keyRef){
		
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		
		if(udcSystem.split(",").length==1) {
				criteria.add(Restrictions.like("udcSystem", "%" + udcSystem + "%"));
		}else {
			criteria.add(Restrictions.in("udcSystem", udcSystem.split(",")));
		}
		
		if(udcKey.split(",").length==1) {
			criteria.add(Restrictions.like("udcKey", "%" + udcKey + "%"));	
		}else {
		criteria.add(Restrictions.in("udcKey", udcKey.split(",")));
		}
		
		if(systemRef.split(",").length==1) {
			criteria.add(Restrictions.like("systemRef", "%" + systemRef + "%"));	
		}else {
		criteria.add(Restrictions.in("systemRef", systemRef.split(",")));
		}
		
		if(keyRef.split(",").length==1) {
			criteria.add(Restrictions.like("keyRef", "%" + keyRef + "%"));	
		}else {
		criteria.add(Restrictions.in("keyRef", keyRef.split(",")));
		}
		
		criteria.add(Restrictions.ne("strValue1", AppConstants.ROLE_SFT));
		criteria.addOrder(Order.asc("strValue1"));
		return (List<UDC>) criteria.list();
	}
	
	public void saveUDC(UDC udc) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(udc);
	}

	public void updateUDC(UDC udc) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(udc);
	}

	public void deleteUDC(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		UDC p = (UDC) session.load(UDC.class, new Integer(id));
		if(null != p){
			session.delete(p);
		}
	}
	
	public int getTotalRecords(){
		Session session = this.sessionFactory.getCurrentSession();
		Long count = (Long) session.createQuery("select count(*) from  UDC").uniqueResult();
		return count.intValue();
	}
	@SuppressWarnings("unchecked")
	public UDC search(UDC udc){
		
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		
		if(udc.getUdcSystem()!=null)
			criteria.add(Restrictions.like("udcSystem", "%" + udc.getUdcSystem() + "%"));
		if(udc.getUdcKey()!=null)
		criteria.add(Restrictions.like("udcKey", "%" + udc.getUdcKey() + "%"));
		if(udc.getSystemRef()!=null)
		criteria.add(Restrictions.like("systemRef", "%" + udc.getSystemRef() + "%"));
		if(udc.getKeyRef()!=null)
		criteria.add(Restrictions.like("keyRef", "%" + udc.getKeyRef() + "%"));
		if(udc.getStrValue1()!=null)
		criteria.add(Restrictions.like("strValue1", "%" + udc.getStrValue1() + "%"));
		if(udc.getStrValue2()!=null)
			criteria.add(Restrictions.like("strValue2", "%" + udc.getStrValue2() + "%"));
		if(udc.getIntValue()!=0)
			criteria.add(Restrictions.eq("intValue", udc.getIntValue()));
		if(udc.getNote()!=null)
			criteria.add(Restrictions.like("note", "%" + udc.getNote()+ "%"));
		if(udc.getDescription()!=null)
			criteria.add(Restrictions.like("description", "%" + udc.getDescription()+ "%"));
		if(udc.getCreatedBy()!=null)
			criteria.add(Restrictions.like("createdBy", "%" + udc.getCreatedBy()+ "%"));
		if(udc.getCreationDate()!=null)
			criteria.add(Restrictions.like("creationDate", "%" + udc.getCreationDate()+ "%"));
		if(udc.getUpdatedBy()!=null)
			criteria.add(Restrictions.like("updatedBy", "%" + udc.getUpdatedBy()+ "%"));
		if(udc.getUpdatedDate()!=null)
			criteria.add(Restrictions.like("updatedDate", "%" + udc.getUpdatedDate()+ "%"));
		
		
	   List<UDC> list =  criteria.list();
	   if(list != null){
		   if(!list.isEmpty())
			   return list.get(0);
		   else 
			   return null;
	   }else{
		   return null;
	   }
	}

	@SuppressWarnings("unchecked")
	public List<UDC> searchBySystemBoolean(String udcSystem, Boolean bandera) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", udcSystem.trim()))
				.add(Restrictions.eq("booleanValue", bandera))
				);
		criteria.addOrder(Order.desc("updatedDate"));
	   return  criteria.list();	   
	}

	public List<UDC> searchListBySystemAndKey(String udcSystem, String udcKey) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UDC.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("udcSystem", udcSystem.trim()))
				.add(Restrictions.like("udcKey", udcKey.trim()))
				);
		criteria.addOrder(Order.asc("strValue1"));
		
	   return criteria.list();
	 
	}

}
