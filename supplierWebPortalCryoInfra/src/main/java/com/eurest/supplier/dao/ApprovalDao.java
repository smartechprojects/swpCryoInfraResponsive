package com.eurest.supplier.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eurest.supplier.dto.SupplierDTO;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.util.AppConstants;

@Repository("approvalDao")
@Transactional
public class ApprovalDao {
	
	@Autowired
	SessionFactory sessionFactory;

	public Supplier getSupplierById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Supplier sup = (Supplier) session.get(Supplier.class, id);
		return sup;
	}
			
	@SuppressWarnings("unchecked")
	public List<SupplierDTO> getPendingApproval(String currentApprover,
			                                      int start,
			                                      int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Supplier.class);
		criteria.add(Restrictions.eq("approvalStatus", AppConstants.STATUS_INPROCESS));
		criteria.add(
				Restrictions.conjunction()
				.add(Restrictions.eq("currentApprover", currentApprover ))
				);
	    
		List<SupplierDTO> supDTOList = new ArrayList<SupplierDTO>();
		List<Supplier> list = criteria.list();
	    if(list != null){
	    	for(Supplier sup : list){
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
				supDTO.setTicketId(sup.getTicketId());
				supDTOList.add(supDTO);
	    	}
	    }
	    return supDTOList;
	}
	
	@SuppressWarnings("unchecked")
	public List<SupplierDTO> searchApproval(String ticketId,
											String approvalStep,
											String approvalStatus,
											Date fechaAprobacion,
											String currentApprover,
											String name,
			                                int start,
			                                int limit) {
		
		
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Supplier.class);
		boolean fields = false;
		
		if(!"".equals(ticketId)) {
			Long val = Long.valueOf(ticketId);
			criteria.add((Criterion)Restrictions.eq("ticketId",val.longValue()));
			fields = true;
		}

		if(!"".equals(approvalStep)) {
			criteria.add((Criterion)Restrictions.like("approvalStep", "%" + approvalStep + "%"));
			fields = true;
		}
		
		if(!"".equals(approvalStatus)) {
			criteria.add((Criterion)Restrictions.like("approvalStatus", "%" + approvalStatus + "%"));
			fields = true;
		}else {
			criteria.add(Restrictions.ne("approvalStatus", AppConstants.STATUS_ACCEPT));
			fields = true;
		}
		
		if(!"".equals(currentApprover)) {
			criteria.add((Criterion)Restrictions.like("currentApprover", "%" + currentApprover + "%"));
			fields = true;
		}
		
		if(!"".equals(name)) {
			criteria.add((Criterion)Restrictions.like("razonSocial", "%" + name + "%"));
			fields = true;
		}
		
		if(fechaAprobacion != null) {
			criteria.add(Restrictions.ge("fechaSolicitud", fechaAprobacion));
			fields = true;
		}
		
		if(fechaAprobacion != null) {
			criteria.add(Restrictions.le("fechaSolicitud", fechaAprobacion));
			fields = true;
		}
    
		List<SupplierDTO> supDTOList = new ArrayList<SupplierDTO>();
		if(fields) {
			List<Supplier> list = criteria.list();
		    if(list != null){
		    	for(Supplier sup : list){
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
					supDTO.setTicketId(sup.getTicketId());
					supDTO.setFechaAprobacion(sup.getFechaAprobacion());
					supDTO.setFechaSolicitud(sup.getFechaSolicitud());
					supDTO.setRejectNotes(sup.getRejectNotes());
					supDTO.setApprovalNotes(sup.getApprovalNotes());
					supDTOList.add(supDTO);
		    	}
		    }
		}
	    return supDTOList;
	}
	
	public void updateSupplier(Supplier o) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(o);
	}
	
}
