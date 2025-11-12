package com.eurest.supplier.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.dto.ForeingInvoice;
import com.eurest.supplier.model.ForeignInvoiceTable;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.PurchaseOrderDetail;
import com.eurest.supplier.model.PurchaseOrderPayment;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.ReceiptInvoice;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.service.DocumentsService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.util.AppConstants;

@Repository("purchaseOrderDao")
@Transactional
public class PurchaseOrderDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DocumentsService documentsService;
	
	@Autowired
	UdcService udcService;
	
	private Logger log4j = Logger.getLogger(PurchaseOrderDao.class);
	
	public PurchaseOrder getOrderById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (PurchaseOrder) session.get(PurchaseOrder.class, id);
	}

	public Receipt getReceiptById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (Receipt) session.get(Receipt.class, id);
	}
	
	public ReceiptInvoice getReceiptInvoiceById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (ReceiptInvoice) session.get(ReceiptInvoice.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseOrder> getOrders(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from PurchaseOrder");
	    q.setFirstResult(start); // modify this to adjust paging
	    q.setMaxResults(limit);
		return (List<PurchaseOrder>) q.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseOrder> getPendingPaymentOrders(int orderNumber, 
													   String addressBook, 
											           String orderType) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber", addressBook))
				.add(Restrictions.eq("paymentUuid", ""))
				.add(Restrictions.ne("invoiceUuid",""))
				.add(Restrictions.ne("paymentType","PUE"))
				.add(Restrictions.eq("relatedStatus","COMPLETE"))
				);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseOrder> getOrderForPayment() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.add(Restrictions.eq("relatedStatus", "UNCOMPLETE"));
		criteria.add(Restrictions.eq("orderStauts", AppConstants.STATUS_OC_PROCESSED));
		criteria.add(Restrictions.eq("status", AppConstants.STATUS_OC_INVOICED));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult(0); 
		criteria.setMaxResults(Integer.MAX_VALUE);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public PurchaseOrder searchbyOrderAndAddressBookAndType(int orderNumber, 
													 String addressBook, 
			                                         String orderType){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber", addressBook))
				.add(Restrictions.eq("orderType", orderType))
				.add(Restrictions.eq("orderNumber", orderNumber))
				);
		
		List<PurchaseOrder> list = criteria.list();
		if(list != null){
			if(!list.isEmpty())
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}
	
	@SuppressWarnings("unchecked")
	public PurchaseOrder searchbyOrderAndAddressBook(int orderNumber, 
													 String addressBook, 
			                                         String orderType){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber", addressBook))
				.add(Restrictions.eq("orderType", orderType))
				.add(Restrictions.eq("orderNumber", orderNumber))
				);
		
		List<PurchaseOrder> list = criteria.list();
		if(list != null){
			if(!list.isEmpty())
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}
	
	@SuppressWarnings("unchecked")
	public PurchaseOrder searchbyOrderAdress(int orderNumber, 
									   String addressBook){
		Session session = this.sessionFactory.getCurrentSession();
		UDC limiteFecha=udcService.searchBySystemAndKey("PLANTACCES", "ORDERLIMIT");
		
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber", addressBook))
				.add(Restrictions.eq("orderNumber", orderNumber))
				.add(Restrictions.gt("dateRequested", limiteFecha.getDateValue()))
				);
		
		List<PurchaseOrder> list = criteria.list();
		if(list != null){
			if(!list.isEmpty())
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public PurchaseOrder searchbyOrder(int orderNumber,String orderType){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("orderType", orderType))
				.add(Restrictions.eq("orderNumber", orderNumber))
				);
		
		List<PurchaseOrder> list = criteria.list();
		if(list != null){
			if(!list.isEmpty())
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<PurchaseOrder> searchbyOrderNumber(int orderNumber, String addressBook, 
										            Date poFromDate, Date poToDate, String status,
										            int start, int limit, String role, String email,
										            String foreign){
		
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.setProjection(Projections.distinct(Projections.property("orderNumber")));
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		boolean fields = false;
		
		try {
			if(orderNumber > 0) {
				criteria.add(Restrictions.eq("orderNumber", orderNumber));
				fields = true;
			}
			
			if(!"".equals(addressBook)) {
				criteria.add(Restrictions.eq("addressNumber", addressBook));
				fields = true;
			}
			
			if(poFromDate != null) {
				criteria.add(Restrictions.ge("dateRequested", poFromDate));
				fields = true;
			}
			
			if(poToDate != null) {
				criteria.add(Restrictions.le("dateRequested", poToDate));
				fields = true;
			}
			
			if(!"".equals(status)) {
				criteria.add(Restrictions.eq("orderStauts", status));
				fields = true;
			}
			
			if(!"".equals(email)) {
				criteria.add(Restrictions.eq("email", email));
				fields = true;
			}
					
						
			if(fields) {
				List uniqueSubList = criteria.list();
				if(uniqueSubList.size() > 0) {
					criteria.setProjection(null);
					criteria.add(Restrictions.in("orderNumber", uniqueSubList));
					criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
					criteria.setFirstResult(0); 
					criteria.setMaxResults(Integer.MAX_VALUE);
				}

				List<PurchaseOrder> list= criteria.list();
				return list;
			}
			else
				return new ArrayList<PurchaseOrder>();
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return new ArrayList<PurchaseOrder>();
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<PurchaseOrder> searchbyOrderNumberHQL(int orderNumber, String addressBook, 
										            Date poFromDate, Date poToDate,String poTypeCombo, String status,
										            int start, int limit, String role, String email,
										            String foreign){
		try {
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "select distinct o from PurchaseOrder o " +
			         " where 1=1 ";
		
		if(orderNumber > 0) {
			sql = sql + " and o.orderNumber = :orderNumber ";
		}
		
		if(!"".equals(addressBook)) {
			sql = sql + " and o.addressNumber = :addressNumber ";
		}
		
		if(poFromDate != null) {
			sql = sql + " and o.dateRequested >= :dateRequestedFrom ";
		}
		
		
		
		if(poToDate != null) {
			sql = sql + " and o.dateRequested <= :dateRequestedTo ";
		}
		
		if( poTypeCombo.equals("OP")) { // para notas de credito
			sql = sql + " and o.orderType = :poTypeCombo ";
		}else
		if( poTypeCombo.equals("OS")) { // para notas de credito
			sql = sql + " and o.orderType not in ('OP') ";
		} 
		
		
		if(!"".equals(status)) {
			sql = sql + " and o.orderStauts = :orderStauts ";
		}
		
		if(!"".equals(foreign)) {
			if(!"".equals(email)) {
				sql = sql + " and (o.email = :email or (o.invoiceNumber = 'OC EXTRANJERO' and o.orderStauts = 'OC ENVIADA')) ";
			}
		}else {
			if(!"".equals(email)) {
				sql = sql + " and o.email = :email ";
			}
		}
				
		sql = sql + " order by o.dateRequested DESC ";
		
		Query q = session.createQuery(sql);
		
		if(orderNumber > 0) {
			q.setParameter("orderNumber", orderNumber);
		}
		
		if(!"".equals(addressBook)) {
			q.setParameter("addressNumber", addressBook);
		}
		
		if(poFromDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(poFromDate); 
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			q.setParameter("dateRequestedFrom", calendar.getTime());
			//q.setParameter("dateRequestedFrom", poFromDate);
		}
		
		if(poToDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(poToDate); // Configuramos la fecha que se recibe
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			q.setParameter("dateRequestedTo", calendar.getTime());
			//q.setParameter("dateRequestedTo", poToDate);
		}
		
		
		
		if(!"".equals(poTypeCombo)&&poTypeCombo.equals("OP")) { // para notas de credito
			q.setParameter("poTypeCombo", poTypeCombo);
		}
		
		if(!"".equals(status)) {
			q.setParameter("orderStauts", status);
		}
		
		if(!"".equals(email)) {
			q.setParameter("email", email);
		}
				
		q.setFirstResult(start);
		q.setMaxResults(limit);
		return q.list();
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}
	}
	
	
	public int searchbyOrderNumberHQLCount(int orderNumber, String addressBook, 
										            Date poFromDate, Date poToDate,String poTypeCombo, String status,
										            String role, String email,
										            String foreign){
		try {
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "select count(distinct o.orderNumber) from PurchaseOrder o, Supplier s " +
			         " where 1=1 ";
		
		if(orderNumber > 0) {
			sql = sql + " and o.orderNumber = :orderNumber ";
		}
		
		if(!"".equals(addressBook)) {
			sql = sql + " and o.addressNumber = :addressNumber ";
		}
		
		if(poFromDate != null) {
			sql = sql + " and o.dateRequested >= :dateRequestedFrom ";
		}
		
		if(poToDate != null) {
			sql = sql + " and o.dateRequested <= :dateRequestedTo ";
		}
		
		if(poTypeCombo != null && poTypeCombo.equals("OP")) { // para notas de credito
			sql = sql + " and o.orderType = :poTypeCombo ";
		}
		
		if(!"".equals(status)) {
			sql = sql + " and o.orderStauts = :orderStauts ";
		}

		if(!"".equals(foreign)) {
			if(!"".equals(email)) {
				sql = sql + " and (o.email = :email or (o.invoiceNumber = 'OC EXTRANJERO' and orderStauts = 'OC ENVIADA')) ";
			}
		}else {
			if(!"".equals(email)) {
				sql = sql + " and o.email = :email ";
			}
		}
		
		Query q = session.createQuery(sql);
		
		if(orderNumber > 0) {
			q.setParameter("orderNumber", orderNumber);
		}
		
		if(!"".equals(addressBook)) {
			q.setParameter("addressNumber", addressBook);
		}
		
		if(poFromDate != null) {
			q.setParameter("dateRequestedFrom", poFromDate);
		}
		
		if(poToDate != null) {
			q.setParameter("dateRequestedTo", poToDate);
		}
		
		if(poTypeCombo != null && poTypeCombo.equals("OP")) { // para notas de credito
			q.setParameter("poTypeCombo", poTypeCombo);
		}
		
		if(!"".equals(status)) {
			q.setParameter("orderStauts", status);
		}
		
		if(!"".equals(email)) {
			q.setParameter("email", email);
		}
		
		Long count = (Long) q.uniqueResult();
		return count.intValue();

		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseOrder> searchCriteria(String addressBook, 
			                                  String status, 
			                                  String orderStatus){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.like("addressNumber", "%" + addressBook + "%"))
				.add(Restrictions.like("status", "%" + status + "%"))
				.add(Restrictions.like("orderStauts", "%" + orderStatus + "%"))
				);
		return (List<PurchaseOrder>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseOrder> searchCriteriaByEmail(String email){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.add(Restrictions.eq("email", email));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("orderNumber"));
		
		 List<PurchaseOrder> returnList = new ArrayList<PurchaseOrder>();
		 List<PurchaseOrder> list = criteria.list();
		 
		 if(list != null) {
			 if(list.size() > 0) {
				 for(PurchaseOrder p : list) {
					 PurchaseOrder o = new PurchaseOrder();
					 o.setId(p.getId());
					 o.setOrderNumber(p.getOrderNumber());
					 o.setOrderType(p.getOrderType());
					 o.setAddressNumber(p.getAddressNumber());
					 o.setOrderStauts(p.getOrderStauts());
					 o.setDescription(p.getDescription());
					 o.setEmail(p.getEmail());
					 returnList.add(o);
				 }
			 }
		 }
		return returnList;
	}
	
	public boolean updateEmail(String email, List<Integer> idList){

		try {
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "UPDATE PurchaseOrder SET email= :email where id in(:idList)";
		Query query = session.createQuery(sql);
		query.setParameter("email", email);
		query.setParameterList("idList", idList);
		query.executeUpdate();
		return true;
		
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public void updateOrders(PurchaseOrder o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void updateReceipt(Receipt o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public synchronized List<PurchaseOrder> saveMultiple(List<PurchaseOrder> list) {
		List<PurchaseOrder> newList = new ArrayList<PurchaseOrder>();
		List<PurchaseOrder> savedList = new ArrayList<PurchaseOrder>();
		try {
		Session session = this.sessionFactory.getCurrentSession();
		int i=0;
		log4j.info("*********** STEP 6: saveMultipleDao:listSize:" + list.size());
		session.setCacheMode(CacheMode.IGNORE);
		session.setFlushMode(FlushMode.COMMIT);
		log4j.info("*********** STEP 7: afterFlush:" + list.size());
		for(PurchaseOrder o : list) {
			PurchaseOrder ao = searchbyOrderAndAddressBookAndCompany(o.getOrderNumber(), o.getAddressNumber(), o.getOrderType(), o.getOrderCompany());
			log4j.info("*********** STEP 8: findOrder:" + ao);
			if(ao != null) {
				if(AppConstants.STATUS_OC_CANCEL.equals(o.getOrderStauts())){
					if(AppConstants.STATUS_OC_PAID.equals(ao.getOrderStauts()) ||
					   AppConstants.STATUS_OC_PAYMENT_COMPL.equals(ao.getOrderStauts())) {
					   continue;
					}else {
					   ao.setOrderStauts(AppConstants.STATUS_OC_CANCEL);
					   ao.setRelatedStatus(AppConstants.STATUS_COMPLETE);
					   ao.setStatus(AppConstants.STATUS_OC_CANCEL);
					   session.saveOrUpdate(ao);
					}
				}else {
					continue;
				}
			}else {
				newList.add(o);
			}
		}
			
			for(PurchaseOrder o : newList) {
				Set<PurchaseOrderDetail> pod = o.getPurchaseOrderDetail();
				log4j.info("*********** STEP 9: detail:" + pod);
				if(pod != null && !pod.isEmpty()) {
				   session.save(o);
				   savedList.add(o);
				   log4j.info("*********** STEP 10: counter:" + i);
				   if( i % 50 == 0 ) {
					      log4j.info("*********** STEP 11: saveflush:" + i);
					      session.flush();
					      session.clear();
					      log4j.info("OC registradas: " + i);
					   }
				   i++;
				}
			}
				
			return savedList;

		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return savedList;
		}
	}
	
	public void updateMultiple(List<PurchaseOrder> list) {
		Session session = this.sessionFactory.getCurrentSession();
		int i=0;
		session.setCacheMode(CacheMode.IGNORE);
		session.setFlushMode(FlushMode.COMMIT);
		for(PurchaseOrder o : list) {
		   session.saveOrUpdate(o);
		   if( i % 50 == 0 ) {
			      session.flush();
			      session.clear();
			   }
		   i++; 
		}
	}
	
	public List<Receipt> saveMultipleReceipt(List<Receipt> list) {
		Session session = this.sessionFactory.getCurrentSession();
		int i=0;
		List<Receipt> newList = new ArrayList<Receipt>();
		session.setCacheMode(CacheMode.IGNORE);
		session.setFlushMode(FlushMode.COMMIT);
		for(Receipt o : list) {
		   o.setLineNumber(o.getLineNumber()/1000);
		   Receipt r = searchReceiptByOrder(o.getOrderNumber(), o.getOrderType(), o.getDocumentNumber(), o.getDocumentType(), o.getLineNumber(), o.getAddressNumber());
		   if(r == null) {
			   o.setAmountReceived(o.getAmountReceived()/100);
			   o.setForeignAmountReceived(o.getForeignAmountReceived()/100);
			   newList.add(o);
		   }
		}
		
		for(Receipt o : newList) {
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
	public Receipt searchReceiptByOrder(int orderNumber, String orderType, int documentNumber, String documentType, int lineNumber, String addressNumber){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("orderType", orderType))
				.add(Restrictions.eq("orderNumber", orderNumber))
				.add(Restrictions.eq("documentNumber", documentNumber))
				.add(Restrictions.eq("documentType", documentType))
				.add(Restrictions.eq("addressNumber", addressNumber))
				.add(Restrictions.eq("lineNumber", lineNumber))
				);
		
		List<Receipt> list = criteria.list();
		if(list != null){
			if(!list.isEmpty())
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}
	
	public void saveMultiplePayments(List<PurchaseOrderPayment> list) {
		Session session = this.sessionFactory.getCurrentSession();
		int i=0;
		session.setCacheMode(CacheMode.IGNORE);
		session.setFlushMode(FlushMode.COMMIT);
		for(PurchaseOrderPayment o : list) {
		   session.saveOrUpdate(o);
		   if( i % 50 == 0 ) {
			      session.flush();
			      session.clear();
			   }
		   i++; 
		}
	      session.flush();
	      session.clear();
	}
	
	@SuppressWarnings("unchecked")
	public PurchaseOrderPayment searchPaymentCriteria(int paymentDocument){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrderPayment.class);
		criteria.add(Restrictions.eq("paymentDocument", paymentDocument));
		List<PurchaseOrderPayment> l =  criteria.list();
		if(l != null) {
			if(!l.isEmpty()) {
				return l.get(0);
			}	
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getPendingPaymentReceipts(int orderNumber, 
													   String addressBook, 
											           String orderType) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber", addressBook))
				.add(Restrictions.eq("status", AppConstants.STATUS_OC_PAID))
				.add(Restrictions.gt("paymentAmount", 0d))
				.add(Restrictions.or(Restrictions.isNull("complPagoUuid"), Restrictions.eq("complPagoUuid", "")))
				);
		return criteria.list();
	}
	
	
	public int getTotalRecords(){
		Session session = this.sessionFactory.getCurrentSession();
		Long count = (Long) session.createQuery("select count(*) from  PurchaseOrder").uniqueResult();
		return count.intValue();
	}

	
	public int getTotalRecords(String addressBook, int orderNumber,Date poFromDate, Date poToDate, String status, String role, String email){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		boolean fields = false;
		Long totalResult = 0l;
		
		try {
			if(orderNumber > 0) {
				criteria.add(Restrictions.eq("orderNumber", orderNumber));
				fields = true;
			}
			
			if(!"".equals(addressBook)) {
				criteria.add(Restrictions.eq("addressNumber", addressBook));
				fields = true;
			}
			
			if(poFromDate != null) {
				criteria.add(Restrictions.ge("dateRequested", poFromDate));
				fields = true;
			}
			
			if(poToDate != null) {
				criteria.add(Restrictions.le("dateRequested", poToDate));
				fields = true;
			}

			if(!"".equals(status)) {
				criteria.add(Restrictions.eq("orderStauts", status));
				fields = true;
			}
			
			if(!"".equals(email)) {
				criteria.add(Restrictions.eq("email", email));
				fields = true;
			}
			
			if(role.contains(AppConstants.ROLE_SUPPLIER)) {
				criteria.add(Restrictions.ne("orderStauts", AppConstants.STATUS_OC_RECEIVED));
				fields = true;
			}
			
			if(fields) {
				totalResult = (Long)criteria.setProjection(Projections.rowCount()).uniqueResult();
			}
			
			
			
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return totalResult.intValue();
		}
		return totalResult.intValue();
	}
	
	
	public List<PurchaseOrder> getOrdersFromJDE(String query){
		return new ArrayList<PurchaseOrder>();
	}
	
	@SuppressWarnings("unchecked")
	public PurchaseOrder searchbyOrderUuid(String uuid){
		
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.add((Restrictions.eq("invoiceUuid", uuid).ignoreCase()));
		
		List<PurchaseOrder> list = criteria.list();
		if(list != null){
			if(list.size() > 0)
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrderPayment> getAll() {
		Session session = this.sessionFactory.getCurrentSession();
		Query q = session.createQuery("from PurchaseOrderPayment");
		return (List<PurchaseOrderPayment>) q.list();
	}
	
	public void saveForeignInvoice(ForeignInvoiceTable o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(o);
	}
	
	public ForeignInvoiceTable getForeignInvoice(ForeingInvoice o) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ForeignInvoiceTable.class);	
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber", o.getAddressNumber()))
				.add(Restrictions.eq("orderNumber", o.getOrderNumber()))
				.add(Restrictions.eq("orderType", o.getOrderType()))
				);
		
		@SuppressWarnings("unchecked")
		List<ForeignInvoiceTable> list = criteria.list();
		if(list != null){
			if(!list.isEmpty())
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}
	
	public ForeignInvoiceTable getForeignInvoiceFromOrder(PurchaseOrder o) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ForeignInvoiceTable.class);	
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber", o.getAddressNumber()))
				.add(Restrictions.eq("orderNumber", o.getOrderNumber()))
				.add(Restrictions.eq("orderType", o.getOrderType()))
				);
		
		@SuppressWarnings("unchecked")
		List<ForeignInvoiceTable> list = criteria.list();
		if(list != null){
			if(!list.isEmpty())
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}
	
	public ForeignInvoiceTable getForeignInvoiceFromUuid(String uuid) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ForeignInvoiceTable.class);	
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("uuid", uuid))
				);
		
		@SuppressWarnings("unchecked")
		List<ForeignInvoiceTable> list = criteria.list();
		if(list != null){
			if(!list.isEmpty())
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getOrderReceipts(int orderNumber,String addressBook, String orderType, String orderCompany) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		
		if(!"".equals(orderCompany)) {
			criteria.add(Restrictions.eq("orderCompany",orderCompany));
		}
		
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("orderNumber", orderNumber))
				.add(Restrictions.eq("orderType", orderType))
				.add(Restrictions.eq("addressNumber",addressBook))
				.add(Restrictions.ge("amountReceived",0d))
				);
		
		criteria.addOrder(Order.asc("documentNumber"));
		criteria.addOrder(Order.asc("lineNumber"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrder> getOpenOrderPO(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("orderStauts", AppConstants.STATUS_OC_RECEIVED))
				);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);		
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getOpenOrderReceipts(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.ne("status", AppConstants.STATUS_OC_CANCEL))
				);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);		
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getBlockAndUnblocksSuppCompl(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("paymentStatus", "P"));
		criteria.add(Restrictions.eq("replicationComplPagoStatus", AppConstants.STATUS_PENDING_REPLICATION));
		criteria.add(Restrictions.or(Restrictions.eq("actionReplication", AppConstants.UNBLOCK_ACTION), Restrictions.eq("actionReplication", AppConstants.BLOCK_ACTION)));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);		
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getOrderReceiptsByUuid(String uuid) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("uuid", uuid))
				);
		
		criteria.addOrder(Order.asc("documentNumber"));
		criteria.addOrder(Order.asc("lineNumber"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getComplPendingReceipts(String addressBook) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber",addressBook))
				.add(Restrictions.ne("paymentAmount",0d)));

		criteria.add(Restrictions.or(Restrictions.isNotNull("uuid"), Restrictions.ne("uuid", "")));
		criteria.add(Restrictions.eq("status", AppConstants.STATUS_OC_PAID));
		criteria.addOrder(Order.asc("uuid"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ReceiptInvoice> getComplPendingReceiptInvoice(String addressBook) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ReceiptInvoice.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber",addressBook))
				.add(Restrictions.ne("paymentAmount",0d))
				.add(Restrictions.or(Restrictions.isNotNull("uuid"), Restrictions.ne("uuid", "")))
				.add(Restrictions.eq("paymentStatus", AppConstants.STATUS_OC_PAID)));
		criteria.addOrder(Order.asc("uuid"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getNegativeOrderReceipts(int orderNumber,String addressBook, String orderType, String orderCompany) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		
		if(!"".equals(orderCompany)) {
			criteria.add(Restrictions.eq("orderCompany",orderCompany));
		}
		
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("orderNumber", orderNumber))
				.add(Restrictions.eq("orderType", orderType))
				.add(Restrictions.eq("addressNumber",addressBook))
				.add(Restrictions.lt("amountReceived",0d))
				);
		
		criteria.addOrder(Order.asc("documentNumber"));
		criteria.addOrder(Order.asc("lineNumber"));
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	public List<PurchaseOrderDetail> getCreditNotes(int orderNumber,String addressBook, String orderType, String orderCompany) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrderDetail.class);
		
		if(!"".equals(orderCompany)) {
			criteria.add(Restrictions.eq("orderCompany",orderCompany));
		}
		
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("orderNumber", orderNumber))
				.add(Restrictions.eq("orderType", orderType))
//				.add(Restrictions.eq("addressNumber",addressBook))
//				.add(Restrictions.lt("amountReceived",0d))
				);
		
//		criteria.addOrder(Order.asc("documentNumber"));
		criteria.addOrder(Order.asc("lineNumber"));
		return criteria.list();
	}
	public void updateReceipts(List<Receipt> list) {
		Session session = this.sessionFactory.getCurrentSession();
		int i=0;
		session.setCacheMode(CacheMode.IGNORE);
		session.setFlushMode(FlushMode.COMMIT);
		for(Receipt o : list) {
		   session.saveOrUpdate(o);
		   if( i % 50 == 0 ) {
			      session.flush();
			      session.clear();
			   }
		   i++; 
		}
	      session.flush();
	      session.clear();
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getPaymentPendingReceipts(int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(Restrictions.isNotNull("uuid"));
		criteria.add(Restrictions.ne("uuid", ""));
		criteria.add(Restrictions.eq("paymentAmount", 0d));
		criteria.add(Restrictions.gt("amountReceived", 0d));
		criteria.setFirstResult(start); // modify this to adjust paging
		criteria.setMaxResults(limit);
		criteria.add(Restrictions.or(Restrictions.isNull("paymentStatus"), Restrictions.eq("paymentStatus", "")));
		List<Receipt> list = criteria.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<ReceiptInvoice> getPaymentPendingReceipts() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ReceiptInvoice.class);
		criteria.add(Restrictions.isNotNull("uuid"));
		criteria.add(Restrictions.ne("uuid", ""));
		criteria.add(Restrictions.eq("paymentStatus", AppConstants.STATUS_OC_PROCESSED));
		criteria.add(Restrictions.eq("paymentAmount", 0d));
		criteria.add(Restrictions.gt("amountReceived", 0d));
		List<ReceiptInvoice> list = criteria.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getSuplierInvoicedReceipts(String addressNumber, String uuid) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("addressNumber", addressNumber));
		criteria.add(Restrictions.eq("uuid", uuid).ignoreCase());
		List<Receipt> list = criteria.list();
		return list;
	}
	
	
	public void updatePaymentReceipts(List<Receipt> list) {
		Session session = this.sessionFactory.getCurrentSession();
		int i=0;
		session.setCacheMode(CacheMode.IGNORE);
		session.setFlushMode(FlushMode.COMMIT);
		
		for(Receipt o : list) {
			List<Receipt> recList = getSuplierInvoicedReceipts(o.getAddressNumber(), o.getUuid());
			if(recList != null) {
				for(Receipt r : recList) {
					if(!AppConstants.STATUS_GR_PAID.equals(r.getPaymentStatus())) {
						r.setPaymentReference(o.getPaymentReference());
						r.setPaymentAmount(o.getPaymentAmount());
						r.setPaymentDate(o.getPaymentDate());
						r.setPaymentStatus(AppConstants.STATUS_GR_PAID);
						r.setStatus(AppConstants.STATUS_OC_PAID);
						
						PurchaseOrder ao = searchbyOrderAndAddressBook(o.getOrderNumber(), o.getAddressNumber(), o.getOrderType());
						ao.setOrderStauts(AppConstants.STATUS_OC_PAID);
						ao.setRelatedStatus(AppConstants.STATUS_COMPLETE);
						
					    session.saveOrUpdate(r);
					    session.saveOrUpdate(ao);
					    if( i % 50 == 0 ) {
						      session.flush();
						      session.clear();
						   }
					    i++; 
					}
				}
			}
		}
	      session.flush();
	      session.clear();
	}
	
	public void saveReceiptInvoice(ReceiptInvoice o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(o);
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getPendingReceiptsComplPago(String addressNumber) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(Restrictions.isNotNull("uuid"));
		criteria.add(Restrictions.ne("uuid", ""));
		criteria.add(Restrictions.eq("paymentStatus", "P"));
		criteria.add(Restrictions.eq("addressNumber", addressNumber));
		criteria.add(Restrictions.or(Restrictions.isNull("complPagoUuid"), Restrictions.eq("complPagoUuid", "")));
		List<Receipt> list = criteria.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getPendingReceiptsComplPagoList() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("paymentStatus", "P"));
		criteria.add(Restrictions.eq("status", AppConstants.STATUS_OC_PAID));
		criteria.add(Restrictions.or(Restrictions.isNull("complPagoUuid"), Restrictions.eq("complPagoUuid", "")));
		List<Receipt> list = criteria.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getReceiptsByUUID(String uuid) {
	    if (uuid == null) {
	        // Manejo del caso en que uuid es nulo
	        return Collections.emptyList(); // O devuelve una lista vacía, según tu lógica
	    }

	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(Receipt.class);
	    criteria.add(Restrictions.eq("uuid", uuid).ignoreCase());
	    return criteria.list();
	}
	@SuppressWarnings("unchecked")
	public List<Receipt> getReceiptsBycreditNoteUUID(String uuid) {
	    if (uuid == null) {
	        // Manejo del caso en que uuid es nulo
	        return Collections.emptyList(); // O devuelve una lista vacía, según tu lógica
	    }

	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(Receipt.class);
	    criteria.add(Restrictions.eq("creditNoteUuid", uuid).ignoreCase());
	    return criteria.list();
	}
	@SuppressWarnings("unchecked")
	public List<ReceiptInvoice> getReceiptsInvoiceByUUID(String uuid) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ReceiptInvoice.class);
		criteria.add(Restrictions.eq("uuid",uuid).ignoreCase());
		return criteria.list();
	}
	
	public void deleteForeignInvoice(ForeignInvoiceTable o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}
	
	public void deleteReceiptInvoice(ReceiptInvoice o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}
	
	@SuppressWarnings("unchecked")
	public PurchaseOrder searchbyOrderAndAddressBookAndCompany(int orderNumber, 
													 String addressBook, 
			                                         String orderType,
			                                         String company){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseOrder.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber", addressBook))
				.add(Restrictions.eq("orderType", orderType))
				.add(Restrictions.eq("orderNumber", orderNumber))
				.add(Restrictions.eq("orderCompany", company))
				);
		
		List<PurchaseOrder> list = criteria.list();
		if(list != null){
			if(!list.isEmpty())
				return list.get(0);
			else
				return null;
		}else
		    return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Receipt> getPendingReceiptsFactuList() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("status", "OC ENVIADA"));
		List<Receipt> list = criteria.list();
		return list;
	}

	public List<Receipt> getOrderReceiptsList(String addressNumber, String tipoDoc, String currencyCode, Date fromDate,
	        Date toDate, int start, int limit) {
	    
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(Receipt.class);
	    
	    // Validar y agregar los criterios de búsqueda
	    if (addressNumber != null && !addressNumber.trim().isEmpty()) {
	        criteria.add(Restrictions.eq("addressNumber", addressNumber.trim()));
	    }
	    
	    if (tipoDoc != null && !tipoDoc.trim().isEmpty()) {
	        criteria.add(Restrictions.eq("documentType", tipoDoc.trim()));
	    }
	    
	    if (currencyCode != null && !currencyCode.trim().isEmpty()) {
	        criteria.add(Restrictions.eq("currencyCode", currencyCode.trim()));
	    }
	    
	    if (fromDate != null) {
	        criteria.add(Restrictions.ge("receiptDate", fromDate));
	    }
	    
	    if (toDate != null) {
	        criteria.add(Restrictions.le("receiptDate", toDate));
	    }
	    criteria.add(Restrictions.isNotNull("paymentAmount"));
	    criteria.add(Restrictions.gt("paymentAmount", 0.0));
	    // Agregar paginación
	    criteria.setFirstResult(start);
	    criteria.setMaxResults(limit);
	    
	    return criteria.list();
	}

	public long getTotalOrderReceiptsList(String addressNumber, String tipoDoc, String currencyCode, Date fromDate, Date toDate) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(Receipt.class);
	    
	    // Validar y agregar los criterios de búsqueda
	    if (addressNumber != null && !addressNumber.trim().isEmpty()) {
	        criteria.add(Restrictions.eq("addressNumber", addressNumber.trim()));
	    }
	    
	    if (tipoDoc != null && !tipoDoc.trim().isEmpty()) {
	        criteria.add(Restrictions.eq("documentType", tipoDoc.trim()));
	    }
	    
	    if (currencyCode != null && !currencyCode.trim().isEmpty()) {
	        criteria.add(Restrictions.eq("currencyCode", currencyCode.trim()));
	    }
	    
	    if (fromDate != null) {
	        criteria.add(Restrictions.ge("receiptDate", fromDate));
	    }
	    
	    if (toDate != null) {
	        criteria.add(Restrictions.le("receiptDate", toDate));
	    }
	    criteria.add(Restrictions.isNotNull("paymentAmount"));
	    criteria.add(Restrictions.gt("paymentAmount", 0.0));
	    // Obtener el total de resultados
	    criteria.setProjection(Projections.rowCount());
	    return (long) criteria.uniqueResult();
	}


	@SuppressWarnings("unchecked")
	public List<PurchaseOrderDetail> getPurchaseOrderDetailByIds(String ids) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(PurchaseOrderDetail.class);

	    // Convertir la cadena de IDs separados por comas a una lista de enteros
	    List<Integer> idList = Arrays.stream(ids.split(","))
	                                  .map(Integer::parseInt)
	                                  .collect(Collectors.toList());

	    // Usar Restrictions.in para filtrar por IDs
	    criteria.add(Restrictions.in("id", idList));

	    criteria.addOrder(Order.asc("lineNumber"));
	    return criteria.list();
	}
	
	
	public void updatePurchaseOrderDetail (PurchaseOrderDetail  o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
		return ;
	}
	


}
