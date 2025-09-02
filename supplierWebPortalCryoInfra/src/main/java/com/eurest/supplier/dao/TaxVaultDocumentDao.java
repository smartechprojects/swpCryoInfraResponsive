package com.eurest.supplier.dao;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.TaxVaultDocument;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.StringUtils;

@Repository("taxVaultDocumentDao")
@Transactional
public class TaxVaultDocumentDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public TaxVaultDocument getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (TaxVaultDocument) session.get(TaxVaultDocument.class, id);
	}
			
	
	public void save(TaxVaultDocument o) {
		if(o != null) {			
			o.setNameFile(StringUtils.takeOffSpecialChars(o.getNameFile() ));						
		}
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	public void update(TaxVaultDocument o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void delete(TaxVaultDocument o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaxVaultDocument> getInvoices(String rfcReceptor, String rfcEmisor, String uuid, java.util.Date tvFromDate, java.util.Date tvToDate, String type, Users user, int start,int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(TaxVaultDocument.class);
		
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		if(!rfcReceptor.equals(""))
			criteria.add(Restrictions.like("rfcReceptor", "%" + rfcReceptor + "%"));
		
		if(!rfcEmisor.equals(""))
			criteria.add(Restrictions.like("rfcEmisor", "%" + rfcEmisor + "%"));
		
		if(!uuid.equals(""))
			criteria.add(Restrictions.like("uuid", "%" + uuid + "%"));
		
		if(tvFromDate != null)
			criteria.add(Restrictions.ge("uploadDate", tvFromDate));
		
		if(tvToDate != null)
			criteria.add(Restrictions.le("uploadDate", tvToDate));
		
		//user.getRole().equals("ROLE_FISCAL_USR")||user.getRole().equals("ROLE_TAX")||user.getRole().equals("ROLE_TREASURY")
		//||user.getRole().equals("ROLE_ACCOUNTING")||user.getRole().equals("ROLE_MANAGER")||user.getRole().equals("ROLE_AUDIT_USR")
		
		//Multiusuario
		if(user.getRole().equals("ROLE_SUPPLIER"))
			criteria.add(Restrictions.like("addressNumber", "%" + user.getAddressNumber() + "%")); 
		
		//criteria.add(Restrictions.like("type", "%FACTURA%"));
		if(!type.equals(""))
			criteria.add(Restrictions.like("type", "%" + type + "%"));
		else criteria.add(Restrictions.ne("type", "ANEXO"));

		return criteria.list();
	}
	
	public TaxVaultDocument getInvoiceByuuid(String uuid) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(TaxVaultDocument.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		criteria.add(Restrictions.or(Restrictions.like("type", "%FACTURA%"),
									Restrictions.like("type", "%NOTA_CREDITO%"),
									Restrictions.like("type", "%COMPLEMENTO%")));
		//criteria.add(Restrictions.like("type", "%FACTURA%"));
		List<TaxVaultDocument> list =  criteria.list();
		if(!list.isEmpty()){
			return list.get(0);
		}		
		return null;
	}


	public List<TaxVaultDocument> getTaxVaultDocumentsByIdFact(String idFact, int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(TaxVaultDocument.class);
		
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		criteria.add(Restrictions.like("uuid", "%" + idFact + "%"));
		
		criteria.add(Restrictions.like("type", "%ANEXO%"));
		return criteria.list();
	}
	
	public List<TaxVaultDocument> getlistToSend() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(TaxVaultDocument.class);
		
		criteria.add(Restrictions.or(Restrictions.like("type", "%FACTURA%"),
									Restrictions.like("type", "%NOTA_CREDITO%"),
									Restrictions.like("type", "%COMPLEMENTO%")));
				//criteria.add(Restrictions.like("type", "%FACTURA%"));
		criteria.add(Restrictions.eq("status", true));
		 
		return criteria.list();
	}
	
	
	public List<TaxVaultDocument> getListComplPag(String uuids,String dateFrom,String dateTo){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(TaxVaultDocument.class);
		
		criteria.add(Restrictions.like("origen", "%" + "COMPLEMENTO" + "%"));
	
		
				
		if(uuids!=null&&!uuids.equals("")) {
			criteria.add(Restrictions.in("uuid", uuids.split(",")));
		}else {
		if(!(dateFrom==null||dateTo==null||dateFrom.equals("")||dateTo.equals(""))) {
			 SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd"); 
			try {
				criteria.add(Restrictions.ge("uploadDate", ft.parse(dateFrom)));
				criteria.add(Restrictions.lt("uploadDate", ft.parse(dateTo)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}else {
			return new ArrayList<TaxVaultDocument>();
		}
		}
				
		
		return criteria.list();
		
	}
	
}
