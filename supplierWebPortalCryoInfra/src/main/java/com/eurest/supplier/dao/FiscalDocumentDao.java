package com.eurest.supplier.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;

@Repository("fiscalDocumentDao")
@Transactional
public class FiscalDocumentDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public FiscalDocuments getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (FiscalDocuments) session.get(FiscalDocuments.class, id);
	}
			
	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getFiscalDocuments(String addressNumber,
			                                      String status,
			                                      int start,
			                                      int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("FiscalDocumentsConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.like("addressNumber", "%" + addressNumber + "%"))
				//.add(Restrictions.eq("status", status))
				);
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getFiscalDocuments(String addressNumber,
			                                      String status,
			                                      String uuid,
			                                      String documentType,
			                                      int start,
			                                      int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("FiscalDocumentsConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		if(!"".equals(uuid) || !"".equals(status) || !"".equals(documentType) || !"".equals(addressNumber)) {
			if(!"".equals(uuid)) criteria.add(Restrictions.eq("uuidFactura", uuid));			
			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
			if(!"".equals(documentType)) criteria.add(Restrictions.eq("type", documentType));
			if(!"".equals(addressNumber)) criteria.add(Restrictions.like("addressNumber", "" + addressNumber + ""));
			criteria.addOrder(Order.desc("invoiceUploadDate"));
			return criteria.list();
		} else {
			return null;
		}
		
		/*
		if("FACTURA".equals(documentType) || "FACT EXTRANJERO".equals(documentType)) {			
			if(!"".equals(uuid)) criteria.add(Restrictions.like("uuidFactura", "%" + uuid + "%"));			
			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
			if(!"".equals(documentType)) criteria.add(Restrictions.eq("type", documentType));
			if(!"".equals(addressNumber)) criteria.add(Restrictions.like("addressNumber", "%" + addressNumber + "%"));
			
			return criteria.list();
			//criteria.add(
					//Restrictions.conjunction()
					//.add(Restrictions.like("addressNumber", "%" + addressNumber + "%"))
					//.add(Restrictions.like("uuidFactura", "%" + uuid + "%"))
					////.add(Restrictions.eq("status", status))
					//);
		}else if("NOTACREDITO".equals(documentType)) {
			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
			if(!"".equals(addressNumber)) criteria.add(Restrictions.like("addressNumber", "%" + addressNumber + "%"));
			if(!"".equals(documentType)) criteria.add(Restrictions.eq("type", documentType));
			
			return criteria.list();			
		}		
		return null;
		*/
	}
	
	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getFiscalDocumentsView(int orderNumber,
														String addressNumber,
											            String status,
											            String uuid,
											            String documentType,
											            String userName,
											            int start,
											            int limit) {

		Session session = sessionFactory.getCurrentSession();
		List<FiscalDocuments> dtoList = null;
		SQLQuery query;
		String sql;
		String sqlFilter = "";
		
		try {
			if(!"".equals(uuid) || !"".equals(status) || !"".equals(documentType) || !"".equals(addressNumber) || orderNumber > 0) {
				
				if(orderNumber > 0) {
					sqlFilter = sqlFilter + " and orderNumber = :orderNumber ";
				}
				
				if(!"".equals(addressNumber)) {
					sqlFilter = sqlFilter + " and addressNumber like :addressNumber ";
				}

				if(!"".equals(status)) {
					sqlFilter = sqlFilter + " and status = :status ";
				}
				
				if(!"".equals(uuid)) {
					sqlFilter = sqlFilter + " and uuidFactura = :uuid ";
				}
				
				if(!"".equals(documentType)) {
					sqlFilter = sqlFilter + " and type = :type ";
				}

				sql = " SELECT " +
						" id, " +
						" accountNumber, " +
						" accountingAccount, " +
						" addressNumber, " +
						" advancePayment, " +
						" amount, " +
						" approvalStatus, " +
						" approvalStep, " +
						" centroCostos, " +
						" companyFD, " +
						" complPagoUuid, " +
						" conceptTotalAmount, " +
						" conceptoArticulo, " +
						" currencyCode, " +
						" currencyMode, " +
						" currentApprover, " +
						" DATE_FORMAT(dateAprov, '%Y-%m-%d %H:%i:%s') as dateAprov, " +
						" descuento, " +
						" documentNumber, " +
						" DATE_FORMAT(estimatedPaymentDate, '%Y-%m-%d %H:%i:%s') as estimatedPaymentDate, " +
						" folio, " +
						" folioNC, " +
						" folioPago, " +
						" glOffset, " +
						" impuestos, " +
						" invoiceDate, " +
						" DATE_FORMAT(invoiceUploadDate, '%Y-%m-%d') as invoiceUploadDate, " +
						" moneda, " +
						" nextApprover, " +
						" noteRejected, " +
						" orderCompany, " +
						" orderNumber, " +
						" orderType, " +
						" paymentAmount, " +
						" paymentDate, " +
						" paymentReference, " +
						" paymentStatus, " +
						" paymentTerms, " +
						" plant, " +
						" DATE_FORMAT(replicationDate, '%Y-%m-%d %H:%i:%s') as replicationDate, " +
						" replicationMessage, " +
						" replicationStatus, " +
						" responsibleUser, " +
						" rfcEmisor, " +
						" rfcReceptor, " +
						" semanaPago, " +
						" serie, " +
						" serieNC, " +
						" seriePago, " +
						" status, " +
						" subtotal, " +
						" supplierName, " +
						" taxCode, " +
						" tipoCambio, " +
						" toSend, " +
						" type, " +
						" uuidFactura, " +
						" uuidNotaCredito, " +
						" uuidPago, " +
						" case when currentApprover like '%" + userName + "%' then 1 else 0 end as approverOrder, " + //Campo para ordenar
						" case when status = 'PENDIENTE' then 1 " +
						" when status = 'APROBADO'		then 2 " +
						" when status = 'PAGADO'		then 3 " +
						" when status = 'COMPLEMENTO'		then 4 " +
						" when status = 'RECHAZADO'	then 5 " +
						" when status = 'CANCELADO'		then 6 else 7 end as statusOrder " + //Campo para ordenar
						" from fiscaldocuments " +
						" where orderType <> 'FLETE' " +
						  sqlFilter +
						" order by  statusOrder asc, invoiceUploadDate desc, id desc  ";
				
				query = session.createSQLQuery(sql);
				
				//Mapeo de campos del objeto PurchaseOrderGridDTO
				query.setResultTransformer(Transformers.aliasToBean(FiscalDocuments.class));
				query.addScalar("id", new IntegerType());
				query.addScalar("accountNumber", new StringType());
				query.addScalar("accountingAccount", new StringType());
				query.addScalar("addressNumber", new StringType());
				query.addScalar("advancePayment", new DoubleType());
				query.addScalar("amount", new DoubleType());
				query.addScalar("approvalStatus", new StringType());
				query.addScalar("approvalStep", new StringType());
				query.addScalar("centroCostos", new StringType());
				query.addScalar("companyFD", new StringType());
				query.addScalar("complPagoUuid", new StringType());
				query.addScalar("conceptTotalAmount", new DoubleType());
				query.addScalar("conceptoArticulo", new StringType());
				query.addScalar("currencyCode", new StringType());
				query.addScalar("currencyMode", new StringType());
				query.addScalar("currentApprover", new StringType());
				query.addScalar("dateAprov", new TimestampType());
				query.addScalar("documentNumber", new IntegerType());
				query.addScalar("descuento", new DoubleType());
				query.addScalar("estimatedPaymentDate", new TimestampType());
				query.addScalar("folio", new StringType());
				query.addScalar("folioNC", new StringType());
				query.addScalar("folioPago", new StringType());
				query.addScalar("glOffset", new StringType());
				query.addScalar("impuestos", new DoubleType());
				query.addScalar("invoiceDate", new StringType());
				query.addScalar("invoiceUploadDate", new TimestampType());
				query.addScalar("moneda", new StringType());
				query.addScalar("nextApprover", new StringType());
				query.addScalar("noteRejected", new StringType());
				query.addScalar("orderCompany", new StringType());
				query.addScalar("orderNumber", new IntegerType());
				query.addScalar("orderType", new StringType());
				query.addScalar("paymentAmount", new DoubleType());
				query.addScalar("paymentDate", new TimestampType());
				query.addScalar("paymentReference", new StringType());
				query.addScalar("paymentStatus", new StringType());
				query.addScalar("paymentTerms", new StringType());
				query.addScalar("plant", new StringType());
				query.addScalar("replicationDate", new TimestampType());
				query.addScalar("replicationMessage", new StringType());
				query.addScalar("replicationStatus", new StringType());
				query.addScalar("responsibleUser", new StringType());
				query.addScalar("rfcEmisor", new StringType());
				query.addScalar("rfcReceptor", new StringType());
				query.addScalar("semanaPago", new StringType());
				query.addScalar("serie", new StringType());
				query.addScalar("serieNC", new StringType());
				query.addScalar("seriePago", new StringType());
				query.addScalar("status", new StringType());
				query.addScalar("subtotal", new DoubleType());
				query.addScalar("supplierName", new StringType());
				query.addScalar("taxCode", new StringType());
				query.addScalar("tipoCambio", new DoubleType());
				query.addScalar("toSend", new IntegerType());
				query.addScalar("type", new StringType());
				query.addScalar("uuidFactura", new StringType());
				query.addScalar("uuidNotaCredito", new StringType());
				query.addScalar("uuidPago", new StringType());
				

				if(orderNumber > 0) {
					query.setParameter("orderNumber", orderNumber);
				}
				
				if(!"".equals(addressNumber)) {
					query.setParameter("addressNumber", "%" + addressNumber + "%");
				}

				if(!"".equals(status)) {
					query.setParameter("status", status);
				}
				
				if(!"".equals(uuid)) {
					query.setParameter("uuid", uuid);
				}
				
				if(!"".equals(documentType)) {
					query.setParameter("type", documentType);
				}
				
				query.setFirstResult(start);
				query.setMaxResults(limit);
				dtoList = query.list();	
				
				return dtoList;				
			} else {
				return null;
			}	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Long getFiscalDocumentsCount(int orderNumber,
										String addressNumber,
							            String status,
							            String uuid,
							            String documentType,
							            String userName) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query;
		String sql;
		String sqlFilter = "";
		
		try {
			if(!"".equals(uuid) || !"".equals(status) || !"".equals(documentType) || !"".equals(addressNumber) || orderNumber > 0) {
				
				if(orderNumber > 0) {
					sqlFilter = sqlFilter + " and orderNumber = :orderNumber ";
				}
				
				if(!"".equals(addressNumber)) {
					sqlFilter = sqlFilter + " and addressNumber like :addressNumber ";
				}

				if(!"".equals(status)) {
					sqlFilter = sqlFilter + " and status = :status ";
				}
				
				if(!"".equals(uuid)) {
					sqlFilter = sqlFilter + " and uuidFactura = :uuid ";
				}
				
				if(!"".equals(documentType)) {
					sqlFilter = sqlFilter + " and type = :type ";
				}

				sql = " select count(id) from (" +
						" select " +
						" id, " +
						" accountNumber, " +
						" accountingAccount, " +
						" addressNumber, " +
						" advancePayment, " +
						" amount, " +
						" approvalStatus, " +
						" approvalStep, " +
						" centroCostos, " +
						" companyFD, " +
						" complPagoUuid, " +
						" conceptTotalAmount, " +
						" conceptoArticulo, " +
						" currencyCode, " +
						" currencyMode, " +
						" currentApprover, " +
						" DATE_FORMAT(dateAprov, '%Y-%m-%d %H:%i:%s') as dateAprov, " +
						" descuento, " +
						" documentNumber, " +
						" DATE_FORMAT(estimatedPaymentDate, '%Y-%m-%d %H:%i:%s') as estimatedPaymentDate, " +
						" folio, " +
						" folioNC, " +
						" folioPago, " +
						" glOffset, " +
						" impuestos, " +
						" invoiceDate, " +
						" DATE_FORMAT(invoiceUploadDate, '%Y-%m-%d') as invoiceUploadDate, " +
						" moneda, " +
						" nextApprover, " +
						" noteRejected, " +
						" orderCompany, " +
						" orderNumber, " +
						" orderType, " +
						" paymentAmount, " +
						" paymentDate, " +
						" paymentReference, " +
						" paymentStatus, " +
						" paymentTerms, " +
						" plant, " +
						" DATE_FORMAT(replicationDate, '%Y-%m-%d %H:%i:%s') as replicationDate, " +
						" replicationMessage, " +
						" replicationStatus, " +
						" responsibleUser, " +
						" rfcEmisor, " +
						" rfcReceptor, " +
						" semanaPago, " +
						" serie, " +
						" serieNC, " +
						" seriePago, " +
						" status, " +
						" subtotal, " +
						" supplierName, " +
						" taxCode, " +
						" tipoCambio, " +
						" toSend, " +
						" type, " +
						" uuidFactura, " +
						" uuidNotaCredito, " +
						" uuidPago, " +
						" case when currentApprover like '%" + userName + "%' then 1 else 0 end as approverOrder, " + //Campo para ordenar
						" case when status = 'PENDIENTE'	then 6 " +
						" when status = 'APROBADO'		then 5 " +
						" when status = 'RECHAZADO'		then 4 " +
						" when status = 'PAGADO'		then 3 " +
						" when status = 'COMPLEMENTO'	then 2 " +
						" when status = 'CANCELADO'		then 1 else 0 end as statusOrder " + //Campo para ordenar
						" from fiscaldocuments " +
						" where orderType <> 'FLETE' " +
						  sqlFilter +
						" ) T ";
				
				//COUNT
				query = session.createSQLQuery(sql);

				if(orderNumber > 0) {
					query.setParameter("orderNumber", orderNumber);
				}
				
				if(!"".equals(addressNumber)) {
					query.setParameter("addressNumber","%" + addressNumber + "%");
				}

				if(!"".equals(status)) {
					query.setParameter("status", status);
				}
				
				if(!"".equals(uuid)) {
					query.setParameter("uuid", uuid);
				}
				
				if(!"".equals(documentType)) {
					query.setParameter("type", documentType);
				}
				
				BigInteger count = (BigInteger) query.uniqueResult();
				return count.longValue();	
			} else {
				return 0L;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getFiscalDocumentsView_Old(String addressNumber,
			                                      String status,
			                                      String uuid,
			                                      String documentType,
			                                      String userName,
			                                      int start,
			                                      int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("FiscalDocumentsConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		if(!"".equals(uuid) || !"".equals(status) || !"".equals(documentType) || !"".equals(addressNumber)) {
			if(!"".equals(uuid)) criteria.add(Restrictions.eq("uuidFactura", uuid));			
			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
			if(!"".equals(documentType)) criteria.add(Restrictions.eq("type", documentType));
			if(!"".equals(addressNumber)) criteria.add(Restrictions.like("addressNumber", "" + addressNumber + ""));
			criteria.add(Restrictions.ne("orderType", "FLETE"));
			criteria.addOrder(Order.desc("id"));
			return criteria.list();
		} else {
			return null;
		}
		
	}
	
	public Long getFiscalDocumentsCount_Old(String addressNumber,
	                                    String status,
	                                    String uuid,
	                                    String documentType,
	                                    String userName) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(FiscalDocuments.class)
	            .setProjection(Projections.rowCount());
	    
	    if (!"".equals(uuid) || !"".equals(status) || !"".equals(documentType) || !"".equals(addressNumber)) {
	        if (!"".equals(uuid)) criteria.add(Restrictions.eq("uuidFactura", uuid));            
	        if (!"".equals(status)) criteria.add(Restrictions.eq("status", status));
	        if (!"".equals(documentType)) criteria.add(Restrictions.eq("type", documentType));
	        if (!"".equals(addressNumber)) criteria.add(Restrictions.like("addressNumber", "" + addressNumber + ""));
	        criteria.add(Restrictions.ne("orderType", "FLETE"));			
	        return (Long) criteria.uniqueResult();
	    } else {
	        return 0L; // Si no se especifican criterios de filtro, el conteo es 0.
	    }
	}

	
	
	@SuppressWarnings("rawtypes")
	public int getTotalRecords(String addressNumber, String status, String uuid, String documentType, int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("FiscalDocumentsConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		if(!"".equals(uuid) || !"".equals(status) || !"".equals(documentType) || !"".equals(addressNumber)) {
			if(!"".equals(uuid)) criteria.add(Restrictions.eq("uuidFactura", uuid));			
			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
			if(!"".equals(documentType)) criteria.add(Restrictions.eq("type", documentType));
			if(!"".equals(addressNumber)) criteria.add(Restrictions.like("addressNumber", "" + addressNumber + ""));
			criteria.setProjection(Projections.rowCount());

            List result = criteria.list();
            if (!result.isEmpty()) {
                Long rowCount = (Long) result.get(0);
                return rowCount.intValue();
            } else {
            	return 0;
            }
		} else {
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getPendingPaymentInvoices(String addressNumber,
					                                       String folio) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("FiscalDocumentsConcept", FetchMode.SELECT);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.like("addressNumber", "%" + addressNumber + "%"))
				.add(Restrictions.eq("uuidPago", ""))
				.add(Restrictions.ne("folio", folio.trim()))
				);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getComplPendingInvoice(String addressBook) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class);
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("addressNumber",addressBook))
				.add(Restrictions.ne("paymentAmount",0d)));

		criteria.add(Restrictions.or(Restrictions.isNotNull("uuidFactura"), Restrictions.ne("uuidFactura", "")));
		criteria.add(Restrictions.eq("status", AppConstants.STATUS_PAID));
		criteria.addOrder(Order.asc("invoiceUploadDate"));
		return criteria.list();
	}
	
	public void saveDocument(FiscalDocuments o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	
	public void updateDocument(FiscalDocuments o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
	public void deleteFiscalDocument(FiscalDocuments o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}

	public FiscalDocuments getFiscalDocumentsByUuid(String uuid){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class);
		criteria.add(
				Restrictions.disjunction()
				.add(Restrictions.eq("uuidFactura", uuid))
				);
		@SuppressWarnings("unchecked")
		List<FiscalDocuments> list =  criteria.list();
		if(!list.isEmpty()) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getFiscalDocumentsByPO(String addressNumber, int order, String orderType, int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("FiscalDocumentsConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		if(!"".equals(addressNumber) || order != 0 || !"".equals(orderType)) {
			criteria.add(Restrictions.eq("addressNumber", addressNumber));
			criteria.add(Restrictions.eq("orderNumber", order));
			criteria.add(Restrictions.eq("type", orderType));
			criteria.addOrder(Order.desc("id"));
			return criteria.list();
		} else {
			return null;
		}

	}
	
	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getPendingReplicationInvoice(String replicationStatus, String documentType, boolean hasPO, int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setFetchMode("FiscalDocumentsConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);

		criteria.add(Restrictions.eq("status", AppConstants.FISCAL_DOC_APPROVED));
		criteria.add(Restrictions.ne("orderType", AppConstants.FREIGHT));
		
		
		
		
			if (!"".equals(replicationStatus))
				criteria.add(Restrictions.eq("replicationStatus", replicationStatus));
			if (!"".equals(documentType))
				criteria.add(Restrictions.eq("type", documentType));
			
			if (hasPO) {
				criteria.add(Restrictions.ne("orderNumber", 0));
			}else {
				criteria.add(Restrictions.eq("orderNumber", 0));
			}
			
							
			
			criteria.addOrder(Order.desc("id"));
			return criteria.list();

	}
	
	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getReplicationInvoiceForUpdate(String addressNumber, String replicationStatus,
			int order, String documentType, String status, /*String folio,*/ int start, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("FiscalDocumentsConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		if(!"".equals(replicationStatus) || !"".equals(status) || !"".equals(documentType) || !"".equals(addressNumber) 
				|| order != 0 /*|| !"".equals(folio)*/ ) {
			if(!"".equals(replicationStatus)) criteria.add(Restrictions.eq("replicationStatus", replicationStatus));			
			if(!"".equals(status)) criteria.add(Restrictions.eq("status", status));
			if(order != 0) criteria.add(Restrictions.eq("orderNumber", order));
			if(!"".equals(documentType)) criteria.add(Restrictions.eq("type", documentType));
			//if(!"".equals(folio)) criteria.add(Restrictions.eq("folio", folio));
			if(!"".equals(addressNumber)) criteria.add(Restrictions.like("addressNumber", "" + addressNumber + ""));
			criteria.addOrder(Order.desc("id"));
			return criteria.list();
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getFiscalDocumentsByUUID(
			                                      String uuid
			                                     ) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class);
		if(!"".equals(uuid)) criteria.add(Restrictions.like("uuidFactura", "%"+uuid+"%" ));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<FiscalDocumentsConcept> getFiscalDocumentsConceptByUUID(
			                                      String uuid
			                                     ) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocumentsConcept.class);
		if(!"".equals(uuid)) criteria.add(Restrictions.eq("uuidFactura", uuid ));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getListFletesSend(int start, int limit) {
		
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocuments.class);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		criteria.add(Restrictions.eq("toSend", 1 ));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<FiscalDocuments> getFiscalDocumentsFreights(Users us, String accountingAccount, String status,String batchIdParam,
			                                      String semanaPago, int start,
			                                      int limit, boolean b) {
		Session session = this.sessionFactory.getCurrentSession();
		
		DetachedCriteria subquery = DetachedCriteria.forClass(UserDocument.class);
		subquery.setProjection(Projections.property("addressBook"));
		subquery.add(Restrictions.like("documentType", "%PDFBATCH%")); 
		
		
		
		Criteria criteria = session.createCriteria(FiscalDocuments.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("FiscalDocumentsConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		 criteria.add(Restrictions.eq("orderType", AppConstants.FREIGHT));
		 if(!b) {
		 criteria.add(Subqueries.propertyIn("id",subquery));
		 }
		if(!"".equals(accountingAccount) || !"".equals(status)||!"".equals(batchIdParam)||!"".equals(semanaPago)) {
			if(!"".equals(accountingAccount)) criteria.add(Restrictions.eq("accountingAccount", accountingAccount));
			if(!"".equals(semanaPago)) criteria.add(Restrictions.like("semanaPago", "%"+semanaPago+"%"));
			if(!"".equals(status)) { 
				criteria.add(Restrictions.eq("status", status));
				if(status.equals("APROBADO")&&us.getRole().equals("ROLE_SUPPLIER")) {
					criteria.add(Restrictions.eq("addressNumber", us.getAddressNumber()));
				}
			}
			if(!"".equals(batchIdParam)) criteria.add(Restrictions.eq("id", Integer.parseInt(batchIdParam) ));
			criteria.equals(Restrictions.eq("toSend", 0));
			
			criteria.addOrder(Order.desc("id"));
			return criteria.list();
		} else {
			return null;
		}
		

	}
	
	@SuppressWarnings("unchecked")
	public Long getFiscalDocumentsFreightsCount(Users us, String accountingAccount, String status, String batchIdParam, String semanaPago, boolean b) {
	    Session session = this.sessionFactory.getCurrentSession();

	    DetachedCriteria subquery = DetachedCriteria.forClass(UserDocument.class);
	    subquery.setProjection(Projections.property("addressBook"));
	    subquery.add(Restrictions.like("documentType", "%PDFBATCH%"));

	    Criteria criteria = session.createCriteria(FiscalDocuments.class)
	            .setProjection(Projections.rowCount())
	            .add(Restrictions.eq("orderType", AppConstants.FREIGHT));
	    if(!b) {
	    criteria.add(Subqueries.propertyIn("id", subquery));
	    }
	    if (!"".equals(accountingAccount) || !"".equals(status) || !"".equals(batchIdParam) || !"".equals(semanaPago)) {
	        if (!"".equals(accountingAccount)) criteria.add(Restrictions.eq("accountingAccount", accountingAccount));
	        if (!"".equals(semanaPago)) criteria.add(Restrictions.like("semanaPago", "%" + semanaPago + "%"));
	        if (!"".equals(status)) {
	            criteria.add(Restrictions.eq("status", status));
	            if (status.equals("APROBADO") && us.getRole().equals("ROLE_SUPPLIER")) {
	                criteria.add(Restrictions.eq("addressNumber", us.getUserName()));
	            }
	        }
	        if (!"".equals(batchIdParam)) criteria.add(Restrictions.eq("id", Integer.parseInt(batchIdParam)));
	        criteria.equals(Restrictions.eq("toSend", 0));

	        return (Long) criteria.uniqueResult();
	    } else {
	        return null;
	    }
	}

	
	
	@SuppressWarnings("unchecked")
	public List<FiscalDocumentsConcept> getInvoicesByBatchID(String batchID,
			                                      int start,
			                                      int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(FiscalDocumentsConcept.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	            .setFetchMode("FiscalDocumentsConcept", FetchMode.SELECT);
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		
		if(!"".equals(batchID)) {
			if(!"".equals(batchID)) criteria.add(Restrictions.eq("batchID", batchID));			
			
			criteria.addOrder(Order.desc("id"));
			return criteria.list();
		} else {
			return null;
		}
		

	}
	
	public boolean existsAddressNumberForFlete(String addressNumber) {
		Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(FiscalDocumentsConcept.class)
	            .setProjection(Projections.distinct(Projections.property("addressNumber")))
	            .add(Restrictions.eq("addressNumber", addressNumber))
	            .add(Restrictions.eq("orderType", "FLETE"));

	    return criteria.uniqueResult() != null;
	}
	public List<FiscalDocumentsConcept> getFiscalDocumentsConceptsForFlete() {
		 Session session = this.sessionFactory.getCurrentSession();
		    Criteria criteria = session.createCriteria(FiscalDocumentsConcept.class);

		    // Obtener la fecha de hace 30 d�as
		    Calendar calendar = Calendar.getInstance();
		    calendar.add(Calendar.DAY_OF_MONTH, -20);
		    Date thirtyDaysAgo = calendar.getTime();

		    // Agregar restricciones
		    criteria.add(Restrictions.eq("orderType", "FLETE"));
		    criteria.add(Restrictions.ge("invoiceUploadDate", thirtyDaysAgo));

		    return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getBatchAndSemanaPago() {
	    Session session = sessionFactory.getCurrentSession();
	    ArrayList<String> res = new ArrayList<>();

	    try {
	        // Definir la consulta SQL
	        String sql = "SELECT DISTINCT fd.id AS NoBatch, fd.semanaPago AS semanaPago " + 
	                     "FROM fiscaldocuments fd " + 
	                     "WHERE fd.orderType = 'FLETE' " + 
	                     "AND fd.replicationDate >= '2024-01-01' " +
	                     "AND fd.id not in (select distinct addressBook from userdocument)" + 
	                     "ORDER BY fd.id DESC;";

	        // Crear la consulta
	        SQLQuery query = session.createSQLQuery(sql);
	        
	        // Obtener la lista de resultados
	        List<Object[]> resultList = query.list();
	        
	        // Procesar la lista de resultados
	        for (Object[] row : resultList) {
	            // Concatenar los valores en un formato adecuado y agregar a la lista
	            String concatenatedResult = String.valueOf(row[0]) + "," + String.valueOf(row[1]);
	            res.add(concatenatedResult);
	        }

	    } catch (Exception e) {
	        // Registrar el error en lugar de solo imprimir
	        // Logger logger = LoggerFactory.getLogger(YourClass.class);
	        // logger.error("Error en getBatchAndSemanaPago", e);
	        e.printStackTrace(); // Mantener para depuración
	    }

	    return res;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getBatchAndSemanaPago(List<String> batches) {
	    Session session = sessionFactory.getCurrentSession();
	    ArrayList<String> res = new ArrayList<>();

	    try {
	        // Definir la consulta SQL con parámetros
	        String sql = "SELECT DISTINCT fd.id AS NoBatch, fd.semanaPago AS semanaPago " + 
	                     "FROM fiscaldocuments fd " + 
	                     "WHERE fd.orderType = 'FLETE' " + 
	                     "AND fd.replicationDate >= '2024-01-01' " +
	                     "AND fd.id IN (:batchIds) " +  // Nueva condición
	                     "ORDER BY fd.id DESC";

	        // Crear la consulta
	        SQLQuery query = session.createSQLQuery(sql);
	        
	        // Establecer el parámetro para la lista de batches
	        query.setParameterList("batchIds", batches);
	        
	        // Obtener la lista de resultados
	        List<Object[]> resultList = query.list();
	        
	        // Procesar la lista de resultados
	        for (Object[] row : resultList) {
	            // Concatenar los valores en un formato adecuado y agregar a la lista
	            String concatenatedResult = String.valueOf(row[0]) + "," + String.valueOf(row[1]);
	            res.add(concatenatedResult);
	        }

	    } catch (Exception e) {
	        // Manejar el error adecuadamente
	        e.printStackTrace();
	    }

	    return res;
	}
	

}
