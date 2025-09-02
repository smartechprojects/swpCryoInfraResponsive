package com.eurest.supplier.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.CodigosSAT;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.util.AppConstants;


@Repository("documentsDao")
@Transactional
public class DocumentsDao{

	@Autowired
	SessionFactory sessionFactory;
	
	private Logger log4j = Logger.getLogger(DocumentsDao.class);
	
	
	public UserDocument getDocumentById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (UserDocument) session.get(UserDocument.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> getDocumentsList(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from UserDocument");
	    q.setFirstResult(start); // modify this to adjust paging
	    q.setMaxResults(limit);
		return (List<UserDocument>) q.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> listDocuments(String addressNumber){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.like("addressBook", "%" + addressNumber + "%"))
				);
		return (List<UserDocument>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> searchCriteria(String query){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.like("name", "%" + query + "%"))
				.add(Restrictions.like("fiscalType", "%" + query + "%"))
				.add(Restrictions.like("uuid", "%" + query + "%"))
				);
		return (List<UserDocument>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> searchByAddressNumber(String query){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressBook", query))
				.add(Restrictions.like("documentType", "load%"))
				);
		return (List<UserDocument>) criteria.list();
	}
	
	
	
	/**
	 * Realiza una búsqueda de documentos de usuario basada en ciertos criterios de búsqueda.
	 *
	 * @param orderNumber    El número de documento a buscar.
	 * @param orderType      El tipo de documento a buscar.
	 * @param addressNumber  El addressNumber relacionado con el documento.
	 * @param includeContent Un indicador que determina si se debe incluir el campo "content" en los resultados.
	 *                      Si es false, los resultados no contendrán el campo "content".
	 * @return Una lista de documentos de orden que coinciden con los criterios de búsqueda.
	 */
	@SuppressWarnings("unchecked")
	public List<UserDocument> searchCriteriaByOrderNumber(int orderNumber, 
	                                                      String orderType, 
	                                                      String addressNumber,
	                                                      boolean includeContent) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(UserDocument.class);
	    
	    criteria.add(
	        Restrictions.conjunction()
	            .add(Restrictions.eq("documentNumber", orderNumber))
	            .add(Restrictions.eq("documentType", orderType))
	            .add(Restrictions.eq("addressBook", addressNumber))
	    );
	    
	    if (!includeContent) {
	        // Si includeContent es false, excluye el campo "content"
	        criteria.setProjection(Projections.projectionList()
	            .add(Projections.property("id"))
	            .add(Projections.property("name"))
	            .add(Projections.property("description"))
	            .add(Projections.property("addressBook"))
	            .add(Projections.property("documentNumber"))
	            .add(Projections.property("documentType"))
	            .add(Projections.property("status"))
	            .add(Projections.property("size"))
	            .add(Projections.property("fiscalType"))
	            .add(Projections.property("type"))
	            .add(Projections.property("uuid"))
	            .add(Projections.property("folio"))
	            .add(Projections.property("serie"))
	            .add(Projections.property("fiscalRef"))
	            .add(Projections.property("uploadDate"))
	            .add(Projections.property("accept"))
	            .add(Projections.property("replicationStatus"))
	            .add(Projections.property("replicationMessage"))
	            .add(Projections.property("replicationDate"))
	        );
	        	    
		    List<Object[]> resultList = criteria.list();
		    List<UserDocument> userDocuments = new ArrayList<>();

		    for (Object[] result : resultList) {
		        UserDocument userDocument = new UserDocument();
		        userDocument.setId((int) result[0]);
		        userDocument.setName((String) result[1]);
		        userDocument.setDescription((String) result[2]);
		        userDocument.setAddressBook((String) result[3]);
		        userDocument.setDocumentNumber((int) result[4]);
		        userDocument.setDocumentType((String) result[5]);
		        userDocument.setStatus((boolean) result[6]);
		        userDocument.setSize((long) result[7]);
		        userDocument.setFiscalType((String) result[8]);
		        userDocument.setType((String) result[9]);
		        userDocument.setUuid((String) result[10]);
		        userDocument.setFolio((String) result[11]);
		        userDocument.setSerie((String) result[12]);
		        userDocument.setFiscalRef((int) result[13]);
		        userDocument.setUploadDate((Date) result[14]);
		        userDocument.setAccept((boolean) result[15]);
		        userDocument.setReplicationStatus((String) result[16]);
		        userDocument.setReplicationMessage((String) result[17]);
		        userDocument.setReplicationDate((Date) result[18]);

		        userDocuments.add(userDocument);
		    }
		    return userDocuments;
	        
	        
	    }
	    List<UserDocument> ar=(List<UserDocument>)criteria.list();
	   
	    return  ar;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> searchCriteriaByOrderNumber(int orderNumber, 
	                                                      String orderType, 
	                                                      String addressNumber,
	                                                      int fiscalDocument,
	                                                      boolean includeContent) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(UserDocument.class);
	    
	    criteria.add(
	        Restrictions.conjunction()
	            .add(Restrictions.eq("documentNumber", orderNumber))
	            .add(Restrictions.eq("documentType", orderType))
	            .add(Restrictions.eq("addressBook", addressNumber))
	            .add(Restrictions.eq("fiscalRef", fiscalDocument))
	    );
	    
	    if (!includeContent) {
	        // Si includeContent es false, excluye el campo "content"
	        criteria.setProjection(Projections.projectionList()
	            .add(Projections.property("id"))
	            .add(Projections.property("name"))
	            .add(Projections.property("description"))
	            .add(Projections.property("addressBook"))
	            .add(Projections.property("documentNumber"))
	            .add(Projections.property("documentType"))
	            .add(Projections.property("status"))
	            .add(Projections.property("size"))
	            .add(Projections.property("fiscalType"))
	            .add(Projections.property("type"))
	            .add(Projections.property("uuid"))
	            .add(Projections.property("folio"))
	            .add(Projections.property("serie"))
	            .add(Projections.property("fiscalRef"))
	            .add(Projections.property("uploadDate"))
	            .add(Projections.property("accept"))
	            .add(Projections.property("replicationStatus"))
	            .add(Projections.property("replicationMessage"))
	            .add(Projections.property("replicationDate"))
	        );
	        	    
		    List<Object[]> resultList = criteria.list();
		    List<UserDocument> userDocuments = new ArrayList<>();

		    for (Object[] result : resultList) {
		        UserDocument userDocument = new UserDocument();
		        userDocument.setId((int) result[0]);
		        userDocument.setName((String) result[1]);
		        userDocument.setDescription((String) result[2]);
		        userDocument.setAddressBook((String) result[3]);
		        userDocument.setDocumentNumber((int) result[4]);
		        userDocument.setDocumentType((String) result[5]);
		        userDocument.setStatus((boolean) result[6]);
		        userDocument.setSize((long) result[7]);
		        userDocument.setFiscalType((String) result[8]);
		        userDocument.setType((String) result[9]);
		        userDocument.setUuid((String) result[10]);
		        userDocument.setFolio((String) result[11]);
		        userDocument.setSerie((String) result[12]);
		        userDocument.setFiscalRef((int) result[13]);
		        userDocument.setUploadDate((Date) result[14]);
		        userDocument.setAccept((boolean) result[15]);
		        userDocument.setReplicationStatus((String) result[16]);
		        userDocument.setReplicationMessage((String) result[17]);
		        userDocument.setReplicationDate((Date) result[18]);

		        userDocuments.add(userDocument);
		    }
		    return userDocuments;
	        
	        
	    }
	    List<UserDocument> ar=(List<UserDocument>)criteria.list();
	   
	    return  ar;
	}

	@SuppressWarnings("unchecked")
	public List<UserDocument> searchCriteriaByOrderNumber(int orderNumber,
	                                                      boolean includeContent) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(UserDocument.class);
	    
	    criteria.add(
	        Restrictions.conjunction()
	            .add(Restrictions.eq("documentNumber", orderNumber))
	    );
	    
	    if (!includeContent) {
	        // Si includeContent es false, excluye el campo "content"
	        criteria.setProjection(Projections.projectionList()
	            .add(Projections.property("id"))
	            .add(Projections.property("name"))
	            .add(Projections.property("description"))
	            .add(Projections.property("addressBook"))
	            .add(Projections.property("documentNumber"))
	            .add(Projections.property("documentType"))
	            .add(Projections.property("status"))
	            .add(Projections.property("size"))
	            .add(Projections.property("fiscalType"))
	            .add(Projections.property("type"))
	            .add(Projections.property("uuid"))
	            .add(Projections.property("folio"))
	            .add(Projections.property("serie"))
	            .add(Projections.property("fiscalRef"))
	            .add(Projections.property("uploadDate"))
	            .add(Projections.property("accept"))
	            .add(Projections.property("replicationStatus"))
	            .add(Projections.property("replicationMessage"))
	            .add(Projections.property("replicationDate"))
	        );
	        	    
		    List<Object[]> resultList = criteria.list();
		    List<UserDocument> userDocuments = new ArrayList<>();

		    for (Object[] result : resultList) {
		        UserDocument userDocument = new UserDocument();
		        userDocument.setId((int) result[0]);
		        userDocument.setName((String) result[1]);
		        userDocument.setDescription((String) result[2]);
		        userDocument.setAddressBook((String) result[3]);
		        userDocument.setDocumentNumber((int) result[4]);
		        userDocument.setDocumentType((String) result[5]);
		        userDocument.setStatus((boolean) result[6]);
		        userDocument.setSize((long) result[7]);
		        userDocument.setFiscalType((String) result[8]);
		        userDocument.setType((String) result[9]);
		        userDocument.setUuid((String) result[10]);
		        userDocument.setFolio((String) result[11]);
		        userDocument.setSerie((String) result[12]);
		        userDocument.setFiscalRef((int) result[13]);
		        userDocument.setUploadDate((Date) result[14]);
		        userDocument.setAccept((boolean) result[15]);
		        userDocument.setReplicationStatus((String) result[16]);
		        userDocument.setReplicationMessage((String) result[17]);
		        userDocument.setReplicationDate((Date) result[18]);

		        userDocuments.add(userDocument);
		    }
		    return userDocuments;
	        
	        
	    }
	    List<UserDocument> ar=(List<UserDocument>)criteria.list();
	   
	    return  ar;
	}
	
	public List<UserDocument> searchCriteriaByOrderNumberWithUuid(int orderNumber, 
            											  String orderType, 
            											  String addressNumber,
            											  String uuid){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("documentNumber", orderNumber))
				.add(Restrictions.eq("documentType", orderType))
				.add(Restrictions.eq("addressBook", addressNumber))
				.add(Restrictions.eq("uuid", uuid))
				);
		criteria.addOrder(Order.desc("id"));
		return (List<UserDocument>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> searchCriteriaByUuidOnly(String uuid){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("uuid", uuid))
				);

		return (List<UserDocument>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public UserDocument searchCriteriaByOrderNumberFiscalType(int orderNumber, 
			                                              String orderType, 
			                                              String addressNumber,
			                                              String type){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("documentNumber", orderNumber))
				.add(Restrictions.eq("documentType", orderType))
				.add(Restrictions.eq("addressBook", addressNumber))
				.add(Restrictions.eq("fiscalType", type))
				);
		
		List<UserDocument> list = (List<UserDocument>) criteria.list();
		if(list != null) {
			if(list.size() > 0) {
				return list.get(0);
			}
		}else {
			return null;
		}
		
		return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> searchCriteriaByType( String orderType){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("documentType", orderType))
				);
		return (List<UserDocument>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> searchCriteriaByRefFiscal(String addressNumber,
			   String uuid){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressBook", addressNumber))
				.add(Restrictions.eq("uuid", uuid))
				);
		return (List<UserDocument>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<UserDocument> searchCriteriaByDescription(String addressNumber, String description, boolean includeContent){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressBook", addressNumber))
				.add(Restrictions.eq("description", description))
				);
		if (!includeContent) {
	        // Si includeContent es false, excluye el campo "content"
	        criteria.setProjection(Projections.projectionList()
	            .add(Projections.property("id"))
	            .add(Projections.property("name"))
	            .add(Projections.property("description"))
	            .add(Projections.property("addressBook"))
	            .add(Projections.property("documentNumber"))
	            .add(Projections.property("documentType"))
	            .add(Projections.property("status"))
	            .add(Projections.property("size"))
	            .add(Projections.property("fiscalType"))
	            .add(Projections.property("type"))
	            .add(Projections.property("uuid"))
	            .add(Projections.property("folio"))
	            .add(Projections.property("serie"))
	            .add(Projections.property("fiscalRef"))
	            .add(Projections.property("uploadDate"))
	            .add(Projections.property("accept"))
	            .add(Projections.property("replicationStatus"))
	            .add(Projections.property("replicationMessage"))
	            .add(Projections.property("replicationDate"))
	        );
	        	    
		    List<Object[]> resultList = criteria.list();
		    List<UserDocument> userDocuments = new ArrayList<>();

		    for (Object[] result : resultList) {
		        UserDocument userDocument = new UserDocument();
		        userDocument.setId((int) result[0]);
		        userDocument.setName((String) result[1]);
		        userDocument.setDescription((String) result[2]);
		        userDocument.setAddressBook((String) result[3]);
		        userDocument.setDocumentNumber((int) result[4]);
		        userDocument.setDocumentType((String) result[5]);
		        userDocument.setStatus((boolean) result[6]);
		        userDocument.setSize((long) result[7]);
		        userDocument.setFiscalType((String) result[8]);
		        userDocument.setType((String) result[9]);
		        userDocument.setUuid((String) result[10]);
		        userDocument.setFolio((String) result[11]);
		        userDocument.setSerie((String) result[12]);
		        userDocument.setFiscalRef((int) result[13]);
		        userDocument.setUploadDate((Date) result[14]);
		        userDocument.setAccept((boolean) result[15]);
		        userDocument.setReplicationStatus((String) result[16]);
		        userDocument.setReplicationMessage((String) result[17]);
		        userDocument.setReplicationDate((Date) result[18]);

		        userDocuments.add(userDocument);
		    }
		    return userDocuments;
	        
	        
	    }
		return (List<UserDocument>) criteria.list();
	}
	
	public UserDocument saveDocuments(UserDocument o) {
		if(o != null) {
			o.setName(com.eurest.supplier.util.StringUtils.takeOffSpecialChars(o.getName()));
		}
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	    return o;	
	}

	public void updateDocuments(UserDocument o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}

	public void updateDocumentList(List<UserDocument> list) {
		Session session = this.sessionFactory.getCurrentSession();
		for(UserDocument o : list) {
		    session.update(o);
		}
	}
	
	public void deleteDocumentList(List<UserDocument> list) {
		Session session = this.sessionFactory.getCurrentSession();
		for(UserDocument o : list) {
		    session.delete(o);
		}
	}
	
	public void deleteDocuments(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		UserDocument p = (UserDocument) session.load(UserDocument.class, new Integer(id));
		if(null != p){
			session.delete(p);
		}
	}
	
	public int getTotalRecords(){
		Session session = this.sessionFactory.getCurrentSession();
		Long count = (Long) session.createQuery("select count(*) from  UserDocument").uniqueResult();
		return count.intValue();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CodigosSAT> getCodesFromArray(String[] array){
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(CodigosSAT.class);
			criteria.add(Restrictions.in("codigoSAT", array));
			return (List<CodigosSAT>) criteria.list();
		}

	@SuppressWarnings("unchecked")
	public UserDocument searchCriteriaByUuidAndPayroll(String uuid){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("uuid", uuid))
				.add(Restrictions.eq("fiscalType", "REC_NOMINA"))
				);

		List<UserDocument> list = (List<UserDocument>) criteria.list();
		if(list != null) {
			if(list.size() > 0) {
				return list.get(0);
			}
		}else {
			return null;
		}
		return null;
	}
	
	public UserDocument getDocumentByUuid(String uuid) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.eq("uuid", uuid))
				);
		@SuppressWarnings("unchecked")
		List<UserDocument> list =  criteria.list();
		if(!list.isEmpty()) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	public UserDocument getInvoiceDocumentByUuid(String uuid) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("uuid", uuid))
				.add(Restrictions.eq("fiscalType", AppConstants.INVOICE_FIELD))
				.add(Restrictions.eq("type", "text/xml"))
				);
		@SuppressWarnings("unchecked")
		List<UserDocument> list =  criteria.list();
		if(!list.isEmpty()) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	public UserDocument getInvoiceDocumentByUuid(String uuid,String fiscalType) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("uuid", uuid))
				.add(Restrictions.eq("fiscalType", fiscalType))
				.add(Restrictions.eq("type", "text/xml"))
				);
		@SuppressWarnings("unchecked")
		List<UserDocument> list =  criteria.list();
		if(!list.isEmpty()) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> searchCriteriaByUuid(int orderNumber,
												String uuidFactura, String type){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("documentNumber", orderNumber))
				.add(Restrictions.eq("uuid", uuidFactura))
				.add(Restrictions.eq("type", type))
				);

		return (List<UserDocument>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public UserDocument searchCriteriaByAddressBookAndType( String type, String addressBook){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(Restrictions.eq("fiscalType", type));
		criteria.add(Restrictions.eq("addressBook", addressBook));

		List<UserDocument> list = (List<UserDocument>) criteria.list();
		if(list != null) {
			if(list.size() > 0) {
				return list.get(0);
			}
		}else {
			return null;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDocument> searchCriteriaByFiscalType(int orderNumber, String addressNumber, String type) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction().add(
				Restrictions.eq("documentNumber", orderNumber))
				.add(Restrictions.eq("addressBook", addressNumber)).
				 add(Restrictions.eq("fiscalType", type)));

		List<UserDocument> list = (List<UserDocument>) criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<UserDocument> getListPendingReplication(int start, int limit) {
		log4j.info("*********** STEP 3: getListPendingReplicationDao:start:" + start + ",limit:" + limit);
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(UserDocument.class);
	    criteria.add((Criterion)Restrictions.eq("replicationStatus", AppConstants.STATUS_PENDING_REPLICATION));
	    criteria.setFirstResult(start);
	    criteria.setMaxResults(limit);
	    List<UserDocument> list = criteria.list();
	    return list;
	}
	
	@SuppressWarnings("unchecked")
	public UserDocument searchCriteriaByUuidAdnInvoice(String uuid){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("uuid", uuid))
				.add(Restrictions.eq("fiscalType", "Factura"))
				);

		List<UserDocument> list = (List<UserDocument>) criteria.list();
		if(list != null) {
			if(list.size() > 0) {
				return list.get(0);
			}
		}else {
			return null;
		}
		return null;
	}
	
}
