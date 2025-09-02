package com.eurest.supplier.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.TaxVaultDocument;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.util.AppConstants;

@Repository("userDocumentDao")
@Transactional
public class UserDocumentDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public UserDocument getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (UserDocument) session.get(UserDocument.class, id);
	}
			
	
	public void save(UserDocument o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	public void update(UserDocument o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void delete(UserDocument o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getListPdfBatchNames() {
	    Session session = this.sessionFactory.getCurrentSession();
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DAY_OF_MONTH, -5);
	    Date fiveDaysAgo = calendar.getTime();
	    
	    Criteria criteria = session.createCriteria(UserDocument.class)
	                                .setProjection(Projections.property("name"))
	                                .add(Restrictions.eq("documentType", "PDFBATCH"))
	                                .add(Restrictions.gt("uploadDate", fiveDaysAgo));
;
	    return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public UserDocument getPdfByBatch(String idBatch) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(Restrictions.eq("addressBook", idBatch));
		criteria.add(Restrictions.like("documentType", "PDFBATCH"));
		List<UserDocument> list =  criteria.list();
		if(!list.isEmpty()){
			return list.get(0);
		}		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> getDocsByAddressBookAndDocType(String addressBook, String[] docTypes) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(Restrictions.eq("addressBook", addressBook));
		criteria.add(Restrictions.in("documentType", docTypes));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<UserDocument> getDocsByRepStatusAndDocType(String replicationStatus, String[] docTypes) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(Restrictions.eq("replicationStatus", replicationStatus));
		criteria.add(Restrictions.in("documentType", docTypes));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> getDocsByRepStatusAndFiscalType(String replicationStatus, String[] fisTypes) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(Restrictions.eq("replicationStatus", replicationStatus));
		criteria.add(Restrictions.in("fiscalType", fisTypes));
		return criteria.list();
	}
	
	public List<UserDocument> getListComplPag(String uuids,String dateFrom,String dateTo){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		
		criteria.add(Restrictions.eq("fiscalType", "ComplementoPago"));
		criteria.add(Restrictions.eq("type", "text/xml"));
		
				
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
			return new ArrayList<UserDocument>();
		}
		}
				
		
		return criteria.list();
		
	}
	
	public UserDocument getComplPag(String uuids){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		
		criteria.add(Restrictions.eq("fiscalType", "P"));
		criteria.add(Restrictions.eq("type", "text/xml"));
		criteria.add(Restrictions.eq("uuid", uuids));
		
				
		
		return (UserDocument) criteria.uniqueResult();
		
	}
}
