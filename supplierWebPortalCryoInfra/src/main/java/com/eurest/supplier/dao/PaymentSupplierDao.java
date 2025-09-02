package com.eurest.supplier.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.PaymentSupplier;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UserDocument;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository("paymentSupplierDao")
@Transactional
public class PaymentSupplierDao {
	 private Logger log4j = Logger.getLogger(PaymentSupplierDao.class);
	 
	@Autowired
	SessionFactory sessionFactory;
	
	public PaymentSupplier getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (PaymentSupplier) session.get(PaymentSupplier.class, id);
	}
			
	@Transactional
	public void save(PaymentSupplier o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}
	public void saveCom(PaymentSupplier o) {
	    Session session = this.sessionFactory.getCurrentSession();
	    try {
	        session.persist(o);
	        // Realiza un commit explícito después de cada inserción
	        session.flush();
	        session.clear();
	    } catch (Exception e) {
	        // Registra la excepción en el log de errores
	        log4j.error("Error al guardar PaymentSupplier: " + e.getMessage(), e);
	        throw e; // Re-lanza la excepción para notificar al servicio llamador
	    }
	}

	
	public boolean update(PaymentSupplier o) {
	    Session session = this.sessionFactory.getCurrentSession();

	    try {
	        session.update(o);
	        return true; // Indicar que la actualización se realizó correctamente
	    } catch (Exception e) {
	        // Manejar la excepción (puedes loggearla o hacer algo con ella)
	        log4j.error("Error while updating PaymentSupplier: " + e.getMessage());
	        return false; // Indicar que la actualización falló
	    }
	}
	
	public void delete(PaymentSupplier o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAddressAndMaxIdList() {
	    List<String> resultList = new ArrayList<>();
	    Session session = this.sessionFactory.getCurrentSession();

	    // Obtener la lista de addresNumber de Supplier
	    Criteria supplierCriteria = session.createCriteria(Supplier.class);
	    supplierCriteria.add(Restrictions.isNotNull("addresNumber"));
	    supplierCriteria.add(Restrictions.not(Restrictions.eq("addresNumber", "")));
	    supplierCriteria.setProjection(Projections.property("addresNumber"));
	    List<String> addressList = supplierCriteria.list();

	    for (String address : addressList) {
	        // Obtener el id máximo de PaymentSupplier para el addresNumber actual
	        Criteria maxIdCriteria = session.createCriteria(PaymentSupplier.class);
	        maxIdCriteria.add(Restrictions.eq("addressBook", address));
	        maxIdCriteria.setProjection(Projections.max("idPayment"));
	        Integer maxId = (Integer) maxIdCriteria.uniqueResult();

	        // Construir la cadena con addresNumber e id máximo y agregarla a la lista resultado
	        String addressAndMaxId = address + "," + (maxId==null?0:maxId);
	        resultList.add(addressAndMaxId);
	    }

	    return resultList;
	}





	
	
	@SuppressWarnings("unchecked")
	public UserDocument getPdfByBatch(String idBatch) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDocument.class);
		criteria.add(Restrictions.eq("addressBook", idBatch));
		criteria.add(Restrictions.like("documentType", "%PDFBATCH%"));
		List<UserDocument> list =  criteria.list();
		if(!list.isEmpty()){
			return list.get(0);
		}		
		return null;
	}


	public List<PaymentSupplier> getPaymentSupplierList(String addressNumber, String tipoDoc, String currencyCode,
			Date fromDate, Date toDate, String estatus, int start, int limit, String sort) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PaymentSupplier.class);

		// Verificar si los parámetros son nulos o no y formar criterios de búsqueda
		// correspondientes
		if (addressNumber != null && !addressNumber.isEmpty()) {
			criteria.add(Restrictions.eq("addressBook", addressNumber));
		}

		if (tipoDoc != null && !tipoDoc.isEmpty()) {
			criteria.add(Restrictions.eq("docCotejo", tipoDoc));
		}

		if (currencyCode != null && !currencyCode.isEmpty()) {
			criteria.add(Restrictions.eq("currencyCode", currencyCode));
		}

		if (fromDate != null) {
			criteria.add(Restrictions.ge("paymentDate", fromDate));
		}

		if (toDate != null) {
			criteria.add(Restrictions.le("paymentDate", toDate));
		}
		if (estatus != null && !estatus.isEmpty()) {
		    if (estatus.equals("C")) {
		        criteria.add(Restrictions.gt("statusPay", "0"));
		    } else if (estatus.equals("P")) {
		        criteria.add(Restrictions.eq("statusPay", "0"));
		    }
		}
		
		if (sort != null && !sort.isEmpty()) {
	        try {
	            // Parsear la opción de ordenación en formato JSON
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode sortJson = objectMapper.readTree(sort);
	            sortJson=sortJson.get(0);
	            
	            if (sortJson.has("property") && sortJson.has("direction")) {
	                String orderByProperty = sortJson.get("property").asText();
	                String orderByDirection = sortJson.get("direction").asText();

	                // Aplicar la ordenación correspondiente a la consulta Criteria
	                if ("ASC".equalsIgnoreCase(orderByDirection)) {
	                    criteria.addOrder(Order.asc(orderByProperty));
	                } else if ("DESC".equalsIgnoreCase(orderByDirection)) {
	                    criteria.addOrder(Order.desc(orderByProperty));
	                }
	            }
	        } catch (Exception e) {
	            // Manejar errores de análisis JSON si es necesario
	            e.printStackTrace();
	        }
	    }

		
		

		// Paginación
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);

		// Ejecutar la consulta y obtener la lista de resultados
		List<PaymentSupplier> paymentSupplierList = criteria.list();
		return paymentSupplierList;
	}
	
	public int getPaymentSupplierCount(String addressNumber, String tipoDoc, String currencyCode, Date fromDate,
			Date toDate, String estatus) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PaymentSupplier.class);

		// Verificar si los parámetros son nulos o no y formar criterios de búsqueda
		// correspondientes
		if (addressNumber != null && !addressNumber.isEmpty()) {
			criteria.add(Restrictions.eq("addressBook", addressNumber));
		}

		if (tipoDoc != null && !tipoDoc.isEmpty()) {
			criteria.add(Restrictions.eq("docCotejo", tipoDoc));
		}

		if (currencyCode != null && !currencyCode.isEmpty()) {
			criteria.add(Restrictions.eq("currencyCode", currencyCode));
		}

		if (fromDate != null) {
			criteria.add(Restrictions.ge("paymentDate", fromDate));
		}

		if (toDate != null) {
			criteria.add(Restrictions.le("paymentDate", toDate));
		}
		if (estatus != null && !estatus.isEmpty()) {
		    if (estatus.equals("P")) {
		        criteria.add(Restrictions.gt("statusPay", "0"));
		    } else if (estatus.equals("C")) {
		        criteria.add(Restrictions.eq("statusPay", "0"));
		    }else if (estatus.equals("A")) {
		    	 criteria.add(Restrictions.eq("paymentAmount", "0"));
			}
		}


		// Obtener el conteo total de resultados que coinciden con los criterios de
		// búsqueda
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}
//	@SuppressWarnings("unchecked")
//	public UserDocument getPdfByBatch(String idBatch) {
//		Session session = this.sessionFactory.getCurrentSession();
//		Criteria criteria = session.createCriteria(UserDocument.class);
//		criteria.add(Restrictions.eq("addressBook", idBatch));
//		criteria.add(Restrictions.like("documentType", "%PDFBATCH%"));
//		List<UserDocument> list =  criteria.list();
//		if(!list.isEmpty()){
//			return list.get(0);
//		}		
//		return null;
//	}
	

}
