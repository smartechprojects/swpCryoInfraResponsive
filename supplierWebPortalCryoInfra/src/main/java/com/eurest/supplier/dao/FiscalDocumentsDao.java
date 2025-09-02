package com.eurest.supplier.dao;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.dto.FiscalDocumentsDTO;
import com.eurest.supplier.model.FiscalDocuments;

@Repository("fiscaldocumentsDao")
@Transactional
public class FiscalDocumentsDao {
	@Autowired
	SessionFactory sessionFactory;
	
	//private Logger log4j = Logger.getLogger(FiscalDocumentsDao.class);
	
	public int getReceipt(int orderNumber, String orderType) {
		System.out.println("entra a getReceipt: orderNumber: " + orderNumber + "orderType: " + orderType);
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("select documentNumber from receipt r where r.orderNumber = :orderNumber and r.orderType = :orderType");
		query.setInteger("orderNumber", orderNumber);
		//query.setString("folio", folio);
		query.setString("orderType", orderType);		
		
		FiscalDocumentsDTO dto = new FiscalDocumentsDTO();		
		dto.setOrderNumber(orderNumber);
		dto.setOrderType(orderType);
		
		int documentNumber = ((Integer)query.uniqueResult()).intValue();
		System.out.println("documentNumber" + documentNumber);
		
		dto.setDocumentNumber(documentNumber);
		
		return documentNumber;
	}	
	
}
