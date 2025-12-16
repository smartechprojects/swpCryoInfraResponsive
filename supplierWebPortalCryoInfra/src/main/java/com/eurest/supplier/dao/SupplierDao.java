package com.eurest.supplier.dao;

import com.eurest.supplier.dto.SupplierDTO;
import com.eurest.supplier.model.AccessTokenRegister;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.util.AppConstants;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("supplierDao")
@Transactional
public class SupplierDao {
  @Autowired
  SessionFactory sessionFactory;
  
  private Logger log4j = Logger.getLogger(SupplierDao.class);
  
  public Supplier getSupplierById(int id) {
    Session session = this.sessionFactory.getCurrentSession();
    Supplier sup = (Supplier)session.get(Supplier.class, Integer.valueOf(id));
    return sup;
  }
  
  public List<SupplierDTO> getList(int start, int limit) {
	log4j.info("*********** STEP 3: getList:start:" + start + ",limit:" + limit);
    Session session = this.sessionFactory.getCurrentSession();
    Criteria criteria = session.createCriteria(Supplier.class);
    criteria.setFirstResult(start);
    criteria.setMaxResults(limit);
    criteria.add((Criterion)Restrictions.ne("approvalStatus", "PENDIENTE"));
    List<SupplierDTO> supDTOList = new ArrayList<>();
    List<Supplier> list = criteria.list();
    if (list != null)
      for (Supplier sup : list) {
        SupplierDTO supDTO = new SupplierDTO();
        supDTO.setId(sup.getId());
        supDTO.setName(sup.getName());
        supDTO.setEmail(sup.getEmail());
        supDTO.setAddresNumber(sup.getAddresNumber());
        supDTO.setCurrentApprover(sup.getCurrentApprover());
        supDTO.setNextApprover(sup.getNextApprover());
        supDTO.setApprovalStatus(sup.getApprovalStatus());
        supDTO.setApprovalStep(sup.getApprovalStep());
        supDTO.setCategoria(sup.getCategorias());
        supDTO.setTipoProducto(sup.getTipoProductoServicio());
        supDTO.setRazonSocial(sup.getRazonSocial());
        supDTO.setTicketId(sup.getTicketId().longValue());
        supDTOList.add(supDTO);
      }  
    return supDTOList;
  }
  
  public List<Supplier> getListPendingReplication(int start, int limit) {
	log4j.info("*********** STEP 3: getListPendingReplicationDao:start:" + start + ",limit:" + limit);
    Session session = this.sessionFactory.getCurrentSession();
    Criteria criteria = session.createCriteria(Supplier.class);
    criteria.setFirstResult(start);
    criteria.setMaxResults(limit);
    criteria.add((Criterion)Restrictions.eq("approvalStatus", AppConstants.STATUS_ACCEPT));
    criteria.add((Criterion)Restrictions.eq("replicationStatus", AppConstants.STATUS_PENDING_REPLICATION));

    List<Supplier> list = criteria.list();
    return list;
  }
  
  
  public List<Supplier> correctionEmail(){
	  Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(Supplier.class);
	    //criteria.setFirstResult(start);
	    //criteria.setMaxResults(limit);
	    //criteria.add((Criterion)Restrictions.ne("approvalStatus", "PENDIENTE"));
	    List<SupplierDTO> supDTOList = new ArrayList<>();
	    List<Supplier> list = criteria.list();
	    if (list != null)
	      for (Supplier sup : list) {
	        SupplierDTO supDTO = new SupplierDTO();
	        supDTO.setId(sup.getId());
	        supDTO.setName(sup.getName());
	        supDTO.setEmail(sup.getEmail());
	        supDTO.setAddresNumber(sup.getAddresNumber());
	        supDTO.setCurrentApprover(sup.getCurrentApprover());
	        supDTO.setNextApprover(sup.getNextApprover());
	        supDTO.setApprovalStatus(sup.getApprovalStatus());
	        supDTO.setApprovalStep(sup.getApprovalStep());
	        supDTO.setCategoria(sup.getCategorias());
	        supDTO.setTipoProducto(sup.getTipoProductoServicio());
	        supDTO.setRazonSocial(sup.getRazonSocial());
	        supDTO.setTicketId(sup.getTicketId().longValue());
	        supDTOList.add(supDTO);
	      }  
	    return list;
  }
  
  public List<SupplierDTO> searchByCriteria(String query, int start, int limit) {
    Session session = this.sessionFactory.getCurrentSession();
    Criteria criteria = session.createCriteria(Supplier.class);
    criteria.setFirstResult(start);
    criteria.setMaxResults(limit);
    criteria.add((Criterion)Restrictions.ne("approvalStatus", "PENDIENTE"));
    criteria.add(
        (Criterion)Restrictions.disjunction()
        .add((Criterion)Restrictions.like("name", "%" + query + "%"))
        .add((Criterion)Restrictions.like("addresNumber", "%" + query + "%")));
    List<SupplierDTO> supDTOList = new ArrayList<>();
    List<Supplier> list = criteria.list();
    if (list != null)
      for (Supplier sup : list) {
        SupplierDTO supDTO = new SupplierDTO();
        supDTO.setId(sup.getId());
        supDTO.setName(sup.getName());
        supDTO.setEmail(sup.getEmail());
        supDTO.setAddresNumber(sup.getAddresNumber());
        supDTO.setCurrentApprover(sup.getCurrentApprover());
        supDTO.setNextApprover(sup.getNextApprover());
        supDTO.setApprovalStatus(sup.getApprovalStatus());
        supDTO.setApprovalStep(sup.getApprovalStep());
        supDTO.setCategoria(sup.getCategorias());
        supDTO.setTipoProducto(sup.getTipoProductoServicio());
        supDTO.setRazonSocial(sup.getRazonSocial());
        supDTO.setTicketId(sup.getTicketId().longValue());
        supDTOList.add(supDTO);
      }  
    return supDTOList;
  }
  
  public List<SupplierDTO> listSuppliers(String supAddNbr, String supAddName, int start, int limit) {
    Session session = this.sessionFactory.getCurrentSession();
    Criteria criteria = session.createCriteria(Supplier.class);
    criteria.add((Criterion)Restrictions.ne("approvalStatus", "PENDIENTE"));
    criteria.setFirstResult(start);
    criteria.setMaxResults(limit);
    boolean fields = true;
    try {
      if (!"".equals(supAddNbr)) {
        criteria.add((Criterion)Restrictions.eq("addresNumber", supAddNbr));
        fields = true;
      } 
      if (!"".equals(supAddName)) {
        criteria.add((Criterion)Restrictions.like("razonSocial", String.valueOf('%') + supAddName.toUpperCase() + '%'));
        fields = true;
      }
      
      if (fields) {
        List<SupplierDTO> supDTOList = new ArrayList<>();
        List<Supplier> list = criteria.list();
        if (list != null)
          for (Supplier sup : list) {
            SupplierDTO supDTO = new SupplierDTO();
            supDTO.setId(sup.getId());
            supDTO.setName(sup.getName());
            supDTO.setEmail(sup.getEmail());
            supDTO.setAddresNumber(sup.getAddresNumber());
            supDTO.setCurrentApprover(sup.getCurrentApprover());
            supDTO.setNextApprover(sup.getNextApprover());
            supDTO.setApprovalStatus(sup.getApprovalStatus());
            supDTO.setApprovalStep(sup.getApprovalStep());
            supDTO.setCategoria(sup.getCategorias());
            supDTO.setTipoProducto(sup.getTipoProductoServicio());
            supDTO.setRazonSocial(sup.getRazonSocial());
            supDTO.setTicketId(sup.getTicketId().longValue());
            supDTO.setObservaciones(sup.getObservaciones());
            supDTOList.add(supDTO);
          }  
        return supDTOList;
      } 
      return new ArrayList<>();
    } catch (Exception e) {
      log4j.error("Exception" , e);
      e.printStackTrace();
      return new ArrayList<>();
    } 
  }
  
  public int listSuppliersTotalRecords(String supAddNbr, String supAddName) {
    Session session = this.sessionFactory.getCurrentSession();
    Criteria criteria = session.createCriteria(Supplier.class);
    criteria.add((Criterion)Restrictions.ne("approvalStatus", "PENDIENTE"));
    //criteria.add((Criterion)Restrictions.ne("observaciones", "INHABILITADO"));
    boolean fields = true;
    Long totalResult = Long.valueOf(0L);
    try {
      if (!"".equals(supAddNbr)) {
        criteria.add((Criterion)Restrictions.eq("addresNumber", supAddNbr));
        fields = true;
      } 
      if (!"".equals(supAddName)) {
        criteria.add((Criterion)Restrictions.like("razonSocial", String.valueOf('%') + supAddName.toUpperCase() + '%'));
        fields = true;
      } 
      if (fields)
        totalResult = (Long)criteria.setProjection(Projections.rowCount()).uniqueResult(); 
    } catch (Exception e) {
      log4j.error("Exception" , e);
      e.printStackTrace();
      return totalResult.intValue();
    } 
    return totalResult.intValue();
  }
  
  public Supplier searchByAddressNumber(String addressNumber) {
    Session session = this.sessionFactory.getCurrentSession();
    Criteria criteria = session.createCriteria(Supplier.class);
    criteria.add(
        (Criterion)Restrictions.disjunction()
        .add((Criterion)Restrictions.eq("addresNumber", addressNumber)));
    List<Supplier> list = criteria.list();
    if (!list.isEmpty())
      return list.get(0); 
    return null;
  }
  
  public Supplier searchByApprover(String currentApprover) {
    Session session = this.sessionFactory.getCurrentSession();
    Criteria criteria = session.createCriteria(Supplier.class);
    criteria.add(
        (Criterion)Restrictions.disjunction()
        .add((Criterion)Restrictions.eq("currentApprover", currentApprover)));
    List<Supplier> list = criteria.list();
    if (!list.isEmpty())
      return list.get(0); 
    return null;
  }
  
  public void updateSupplier(Supplier o) {
    Session session = this.sessionFactory.getCurrentSession();
    session.saveOrUpdate(o);
  }
  
  public void saveSupplier(Supplier o) {
    Session session = this.sessionFactory.getCurrentSession();
    session.saveOrUpdate(o);
  }
  
  public void saveSuppliers(List<Supplier> list) {
    Session session = this.sessionFactory.getCurrentSession();
    int i = 0;
    session.setCacheMode(CacheMode.IGNORE);
    session.setFlushMode(FlushMode.COMMIT);
    for (Supplier o : list) {
      session.saveOrUpdate(o);
      if (i % 50 == 0) {
        session.flush();
        session.clear();
      } 
      i++;
    } 
  }
  
  public int getTotalRecords() {
    Session session = this.sessionFactory.getCurrentSession();
    Long count = (Long)session.createQuery("select count(*) from  Supplier").uniqueResult();
    return count.intValue();
  }
  
  public int getRfcRecords(String rfc) {
    Session session = this.sessionFactory.getCurrentSession();
    Query query = session.createQuery("select count(*) from  Supplier s where s.rfc = :rfc");
    query.setString("rfc", rfc);
    int count = ((Integer)query.uniqueResult()).intValue();
    return count;
  }
  
  public List<Supplier> searchByRfc(String rfc) {
    Session session = this.sessionFactory.getCurrentSession();
    Criteria criteria = session.createCriteria(Supplier.class);
    criteria.add(
        (Criterion)Restrictions.disjunction()
        .add((Criterion)Restrictions.eq("rfc", rfc)));
    return criteria.list();
  }
  
  public List<Supplier> searchByTaxId(String taxId) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(Supplier.class);
	    criteria.add(
	        (Criterion)Restrictions.disjunction()
	        .add((Criterion)Restrictions.eq("taxId", taxId)));
	    return criteria.list();
	  }
  
  public Supplier searchByTicket(long ticketId) {
    Session session = this.sessionFactory.getCurrentSession();
    Criteria criteria = session.createCriteria(Supplier.class);
    criteria.add((Criterion)Restrictions.eq("ticketId", Long.valueOf(ticketId)));
    List<Supplier> list = criteria.list();
    if (list != null && 
      list.size() > 0)
      return list.get(0); 
    return null;
  }

  public List<Supplier> searchByOutSourcingStatus() {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(Supplier.class);
	    criteria.add((Criterion)Restrictions.eq("outSourcing", true));
	    return criteria.list();
	  }
  
  public List<AccessTokenRegister> listAccessTokenRegister(String query, int start, int limit) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(AccessTokenRegister.class);
	    criteria.setFirstResult(start);
	    criteria.setMaxResults(limit);
	    criteria.add((Criterion)Restrictions.like("registerName",  "%" + query + "%"));
	    return criteria.list(); 
}

public int listAccessTokenRegisterCount(String query) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(AccessTokenRegister.class);
	    criteria.add((Criterion)Restrictions.like("registerName", "%" + query + "%"));
	    criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count.intValue(); 
}

public AccessTokenRegister getAccessTokenRegisterById(int id) {
	    Session session = this.sessionFactory.getCurrentSession();
	    return (AccessTokenRegister)session.get(AccessTokenRegister.class, Integer.valueOf(id));
}


public AccessTokenRegister searchByAccessCode(String code, String pass) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(AccessTokenRegister.class);
	    criteria.add((Criterion)Restrictions.eq("code", code));
	    criteria.add((Criterion)Restrictions.eq("password", pass));
	    List<AccessTokenRegister> list = criteria.list();
	    if (list != null && 
	      list.size() > 0)
	      return list.get(0); 
	    return null;
}

public AccessTokenRegister searchByToken(String access_token) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(AccessTokenRegister.class);
	    criteria.add((Criterion)Restrictions.eq("token", access_token));
	    List<AccessTokenRegister> list = criteria.list();
	    if (list != null && 
	      list.size() > 0)
	      return list.get(0); 
	    return null;
}

public AccessTokenRegister searchActiveAccessCode(String code) {
	    Session session = this.sessionFactory.getCurrentSession();
	    Criteria criteria = session.createCriteria(AccessTokenRegister.class);
	    
	    criteria.add((Criterion)Restrictions.eq("code", code));
	    criteria.add((Criterion)Restrictions.eq("enabled", true));
	    
	    List<AccessTokenRegister> list = criteria.list();
	    if (list != null && 
	      list.size() > 0)
	      return list.get(0); 
	    return null;
 }

public void saveAccessToken(AccessTokenRegister o) {
	  Session session = this.sessionFactory.getCurrentSession();
	  session.save(o);
}

public void updateAccessToken(AccessTokenRegister o) {
	  Session session = this.sessionFactory.getCurrentSession();
	  session.saveOrUpdate(o);
}
}
