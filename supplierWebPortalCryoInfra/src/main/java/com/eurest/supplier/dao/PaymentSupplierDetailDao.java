package com.eurest.supplier.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.model.PaymentSupplier;
import com.eurest.supplier.model.PaymentSupplierDetail;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UserDocument;

@Repository("paymentSupplierDetailDao")
@Transactional
public class PaymentSupplierDetailDao {
	 private Logger log4j = Logger.getLogger(PaymentSupplierDetailDao.class);
	 
	@Autowired
	SessionFactory sessionFactory;
	
	public PaymentSupplierDetail getById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (PaymentSupplierDetail) session.get(PaymentSupplierDetail.class, id);
	}
			
	@Transactional
	public void save(PaymentSupplierDetail o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(o);
	}

	
	public boolean update(PaymentSupplierDetail o) {
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
	
	public void delete(PaymentSupplierDetail o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(o);
	}

	public List<PaymentSupplierDetail> getPaymentSupplierDetailList(String idPayment) {
	
			Session session = this.sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(PaymentSupplierDetail.class);

				criteria.add(Restrictions.eq("idPayment", Integer.parseInt(idPayment)));
			
			// Ejecutar la consulta y obtener la lista de resultados
			List<PaymentSupplierDetail> paymentSupplierList = criteria.list();
			return paymentSupplierList;
	}
	
	
}
