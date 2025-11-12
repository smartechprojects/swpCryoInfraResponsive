package com.eurest.supplier.service;

import com.eurest.supplier.dao.ApprovalDao;
import com.eurest.supplier.dto.SupplierDTO;
import com.eurest.supplier.model.DataAudit;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.NextNumber;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.StringUtils;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service("approvalService")
public class ApprovalService {
  @Autowired
  ApprovalDao approvalDao;
  
  @Autowired
  UdcService udcService;
  
  @Autowired
  EDIService EDIService;
  
  @Autowired
  EmailService emailService;
  
  @Autowired
  UsersService userService;
  
  @Autowired
  NextNumberService nextNumberService;
  
  @Autowired
  DocumentsService documentsService;
  
  @Autowired
  private JavaMailSender mailSenderObj;
  
  @Autowired
  StringUtils stringUtils;
  
  @Autowired
  DataAuditService dataAuditService;
  
  @Autowired
  FiscalDocumentService fiscalDocumentService;
  
  public List<SupplierDTO> getPendingApproval(String currentApprover, int start, int limit) {
    return this.approvalDao.getPendingApproval(currentApprover, start, limit);
  }
  
  public int getPendingApprovalTotal(String currentApprover) {
	    return this.approvalDao.getPendingApprovalTotal(currentApprover);
  }
  
  public String updateSupplier(int id, String status, String step, String notes) {
	    Supplier s = this.approvalDao.getSupplierById(id);
	    String emailRecipient = s.getEmailSupplier();
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		Date currentDate = new Date();
		String usr = auth.getName();  
		if(s.getUpdateApprovalFlow() == null || s.getUpdateApprovalFlow().equals("")) {
	    if ("APROBADO".equals(status) && "FIRST".equals(step)) {
	      String nextApprover = s.getNextApprover().trim();
	      UDC udc = this.udcService.searchBySystemAndStrValue("APPROVER", "SECOND_APPROVER", nextApprover);
	      String nextApproverEmail = "";
	      if (udc != null) {
	        s.setCurrentApprover(udc.getStrValue1());
	        s.setNextApprover(udc.getStrValue2());
	        nextApproverEmail = udc.getKeyRef().trim();
	      } else {
	        s.setCurrentApprover("EARANDA");
	        s.setNextApprover("PREYNOSO");
	      } 
	      s.setApprovalStep("SECOND");
	      s.setApprovalStatus("PENDIENTE");
	      s.setApprovalNotes(notes);
	      this.approvalDao.updateSupplier(s);
	      
	      dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	      usr, "Supplier Approval Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	      notes,null, null, AppConstants.STATUS_APPROVALFINALSTEP, 
	      null, AppConstants.STATUS_ACCEPT, AppConstants.SUPPLIER_MODULE);
	              
	      
		  String emailContent = AppConstants.EMAIL_SECOND_APP_CONTENT;
		  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
		  emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);

	      EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	      emailAsyncSup.setProperties(AppConstants.EMAIL_SECOND_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), nextApproverEmail);
	      emailAsyncSup.setMailSender(this.mailSenderObj);
	      Thread emailThreadSup = new Thread(emailAsyncSup);
	      emailThreadSup.start();
	     
	      return "Success";
	    } 
	    if ("APROBADO".equals(status) && "SECOND".equals(step)) {
	        String nextApprover = s.getNextApprover().trim();
	        UDC udc = this.udcService.searchBySystemAndStrValue("APPROVER", "THIRD_APPROVER", nextApprover);
	        String nextApproverEmail = "";
	        if (udc != null) {
	          s.setCurrentApprover(udc.getStrValue1());
	          s.setNextApprover(udc.getStrValue2());
	          nextApproverEmail = udc.getKeyRef().trim();
	        } else {
	          s.setCurrentApprover("PREYNOSO");
	          s.setNextApprover("FINAL");
	        } 
	        s.setApprovalStep("THIRD");
	        s.setApprovalStatus("PENDIENTE");
	        s.setApprovalNotes(notes);
	        this.approvalDao.updateSupplier(s);
	        
	        
	        dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	        usr, "Supplier Approval Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	        notes,null, null, AppConstants.STATUS_APPROVALSECONDSTEP, 
	        null, AppConstants.STATUS_INPROCESS, AppConstants.SUPPLIER_MODULE);
	      	
	        
	  	  String emailContent = AppConstants.EMAIL_SECOND_APP_CONTENT;
	  	  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
	  	  emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
	  	  

	        EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	        emailAsyncSup.setProperties(AppConstants.EMAIL_THIRD_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), nextApproverEmail);
	        emailAsyncSup.setMailSender(this.mailSenderObj);
	        Thread emailThreadSup = new Thread(emailAsyncSup);
	        emailThreadSup.start();
	       
	        return "Success";
	      } 
	    if ("APROBADO".equals(status) && "THIRD".equals(step)) {
	      s.setCurrentApprover("FINAL");
	      s.setNextApprover("FINAL");
	      s.setApprovalStep("THIRD");
	      s.setApprovalStatus("APROBADO");
	      s.setApprovalNotes(notes);
	      s.setFechaAprobacion(new Date());
	      
	      if(s.getAddresNumber() == "" ||  "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null) {
	          s.setTipoMovimiento("A");
	          s.setCreditMessage("");
	          s.setSearchType("V");
	          s.setHold("");
	          
	          Supplier jdeS = EDIService.registerNewAddressBook(s);
	          if(jdeS==null) {
	              s.setAddresNumber(null);
	              s.setReplicationStatus(AppConstants.STATUS_ERROR_REPLICATION);
	              s.setReplicationMessage("Error JDE");
	              s.setReplicationDate(null);
	              return "Error JDE";
	          }else {
	        	  s.setAddresNumber(jdeS.getAddresNumber());
	        	  s.setBatchNumber(jdeS.getBatchNumber());
	        	  s.setUkuid(jdeS.getUkuid());
	        	  //s.setReplicationStatus(AppConstants.STATUS_SUCCESS_REPLICATION);
	        	  s.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
	              s.setReplicationMessage(null);
	              s.setReplicationDate(null);
	        	  
	        	  List<UserDocument> list = null;
	        	  if(s.getTaxId() != null && !"".equals(s.getTaxId())) {
	        		list =  this.documentsService.searchByAddressNumber("NEW_" + s.getTaxId());
	        	  }else {
	        		list =  this.documentsService.searchByAddressNumber("NEW_" + s.getRfc());
	        	  }
	              for (UserDocument d : list) {
	                d.setAddressBook(jdeS.getAddresNumber());
	                this.documentsService.update(d, new Date(), "admin");
	              } 
	              
	              this.approvalDao.updateSupplier(s);
	              
	              
	              dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	              usr, "Supplier Approval Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	              notes,null, null, AppConstants.STATUS_APPROVALFINALSTEP, 
	              null, AppConstants.STATUS_ACCEPT, AppConstants.SUPPLIER_MODULE);
	            	
	              
	        	  /*System.out.println("***** ADDRESSNUMBER:" + s.getAddresNumber());
	        	  UDC role = udcService.searchBySystemAndKey("ROLES", "SUPPLIER");
	              UDC userType = udcService.searchBySystemAndKey("USERTYPE", "SUPPLIER");

	        	  String tempPass = "cryo22";
	        	  String encodePass = Base64.getEncoder().encodeToString(tempPass.trim().getBytes());
	        	  encodePass = "==a20$" + encodePass; 
	              Users u = new Users();
	              u.setEmail(s.getEmailSupplier());
	              u.setName(s.getRazonSocial());
	              u.setPassword(encodePass);
	              u.setUserName(s.getAddresNumber());
	              u.setUserRole(role);
	              u.setUserType(userType);
	              u.setEnabled(true);
	              userService.save(u, null, null);
	              
	              
				  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				
				  String emailContent = AppConstants.EMAIL_ACCEPT_SUPPLIER_NOTIFICATION;
				  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
				  emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
				  emailContent = emailContent.replace("_USER_", s.getAddresNumber());
				  emailContent = emailContent.replace("_PASS_", tempPass.trim());

				  emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_ACCEPT, stringUtils.prepareEmailContent(emailContent), s.getEmailSupplier());
				  emailAsyncSup.setMailSender(mailSenderObj);
				  Thread emailThreadSup = new Thread(emailAsyncSup);
				  emailThreadSup.start();	*/
	              
	              return "Success";
	          }
	  
	      }else {
	    	  Users u = userService.getByUserName(s.getAddresNumber());
	    	  u.setEnabled(true);
	    	  userService.update(u, null, null);
	    	  //Multiusuarios: Actualiza usuarios del proveedor también
	    	  List<Users> userList = userService.getByAddressNumber(s.getAddresNumber());
	    	  if(userList != null && !userList.isEmpty()) {
	    		  for(Users uSupplier : userList) {
    				  uSupplier.setEnabled(true);
    				  userService.update(uSupplier, null, null);
	    		  }
	    	  }
	    	  s.setTipoMovimiento("C");
	    	  s.setCreditMessage("");
	          s.setSearchType("V");
	          s.setHold("");
	          s.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
	          s.setReplicationMessage(null);
	          s.setReplicationDate(null);
	          
	          //Se añade nuevo batch
	          NextNumber nn = this.nextNumberService.getNextNumber("ADDRESSBOOK");
	          String nextBatch = nn.getNextStr();
	          int batchNbr = Integer.valueOf(nextBatch).intValue();
	    	  s.setBatchNumber(String.valueOf(batchNbr));
	    	  batchNbr++;
	    	  nn.setNextStr(String.valueOf(batchNbr));
	    	  this.nextNumberService.updateNextNumber(nn);
	    	  
	    	  this.approvalDao.updateSupplier(s);
	    	  
	    	  dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	    	  usr, "Supplier Reject Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	    	  notes,null, null, AppConstants.STATUS_APPROVALFIRSTSTEP, 
	    	  null, AppConstants.STATUS_REJECT, AppConstants.SUPPLIER_MODULE);       
	    	  
	    	  EDIService.registerNewAddressBook(s);
	    	  
			  String credentials = "Usuario: " + s.getAddresNumber() + "<br />&nbsp; url: " + AppConstants.EMAIL_PORTAL_LINK ;
			  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			  emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_ACCEPT, stringUtils.prepareEmailContent(AppConstants.EMAIL_MASS_SUPPLIER_CHANGE_NOTIFICATION + credentials), s.getEmailSupplier());
			  emailAsyncSup.setMailSender(mailSenderObj);
			  Thread emailThreadSup = new Thread(emailAsyncSup);
			  emailThreadSup.start();
	    	  
	    	  return "Succ Update";
	      }
	      
	    } 
	    

	    if ("RECHAZADO".equals(status) && "FIRST".equals(step)) {
	    	List<UserDocument> list = null;
	    	if(s.getAddresNumber() == "" ||  "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null) {
	    		if(s.getTaxId() != null && !"".equals(s.getTaxId())) {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getTaxId());
	    		  }else {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getRfc());
	    		  }
	    	}else {
	    		list =  this.documentsService.searchByAddressNumber(s.getAddresNumber());
	    	}
	    	
		    for (UserDocument d : list) {
		    	switch(d.getDocumentType()) {
			    	case "loadRfcDoc" : this.documentsService.delete(d.getId(), "admin");
					break;
					case "loadDomDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadEdoDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadIdentDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadActaConst" : this.documentsService.delete(d.getId(), "admin");
						break;
					default : 
						break;
		    	}
		     } 
	      
		  s.setFileList("");
	      s.setCurrentApprover("REJECT");
	      s.setNextApprover("REJECT");
	      s.setApprovalStep("FIRST");
	      s.setApprovalStatus("RENEW");
	      s.setRejectNotes(notes);
	      this.approvalDao.updateSupplier(s);
	      
	      String emailContent = AppConstants.EMAIL_REJECT_SUPPLIER_NOTIFICATION;
			emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
			emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
			emailContent = emailContent.replace("_REASON_", notes);
	      
	      EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	      emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), emailRecipient);
	      emailAsyncSup.setMailSender(this.mailSenderObj);
	      Thread emailThreadSup = new Thread(emailAsyncSup);
	      emailThreadSup.start();
	      
	      EmailServiceAsync emailAsyncSup2 = new EmailServiceAsync();
	      emailAsyncSup2.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), s.getEmailComprador());
	      emailAsyncSup2.setMailSender(this.mailSenderObj);
	      Thread emailThreadSup2 = new Thread(emailAsyncSup2);
	      emailThreadSup2.start();
	     
	      return "Rejected";
	    } 
	    if ("RECHAZADO".equals(status) && "SECOND".equals(step)) {
	    	List<UserDocument> list = null;
	    	if(s.getAddresNumber() == "" ||  "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null) {
	    		if(s.getTaxId() != null && !"".equals(s.getTaxId())) {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getTaxId());
	    		  }else {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getRfc());
	    		  }
	    	}else {
	    		list =  this.documentsService.searchByAddressNumber(s.getAddresNumber());
	    	}
	    	
	    	for (UserDocument d : list) {
		    	switch(d.getDocumentType()) {
			    	case "loadRfcDoc" : this.documentsService.delete(d.getId(), "admin");
					break;
					case "loadDomDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadEdoDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadIdentDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadActaConst" : this.documentsService.delete(d.getId(), "admin");
						break;
					default : 
						break;
		    	}
		     } 
	    	
		  s.setFileList("");
	      s.setCurrentApprover("");
	      s.setNextApprover("");
	      s.setApprovalStep("SECOND");
	      s.setApprovalStatus("RENEW");
	      s.setRejectNotes(notes);
	      this.approvalDao.updateSupplier(s);

	      String emailContent = AppConstants.EMAIL_REJECT_SUPPLIER_NOTIFICATION;
			emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
			emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
			emailContent = emailContent.replace("_REASON_", notes);
	      
	      EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	      emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), emailRecipient);
	      emailAsyncSup.setMailSender(this.mailSenderObj);
	      Thread emailThreadSup = new Thread(emailAsyncSup);
	      emailThreadSup.start();
	      
	      EmailServiceAsync emailAsyncSup2 = new EmailServiceAsync();
	      emailAsyncSup2.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), s.getEmailComprador());
	      emailAsyncSup2.setMailSender(this.mailSenderObj);
	      Thread emailThreadSup2 = new Thread(emailAsyncSup2);
	      emailThreadSup2.start();
	      
	      return "Rejected";
	    } 
	    if ("RECHAZADO".equals(status) && "THIRD".equals(step)) {
	    	
	    	List<UserDocument> list = null;
	    	if(s.getAddresNumber() == "" ||  "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null) {
	    		if(s.getTaxId() != null && !"".equals(s.getTaxId())) {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getTaxId());
	    		  }else {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getRfc());
	    		  }
	    	}else {
	    		list =  this.documentsService.searchByAddressNumber(s.getAddresNumber());
	    	}
	    	
	    	for (UserDocument d : list) {
		    	switch(d.getDocumentType()) {
			    	case "loadRfcDoc" : this.documentsService.delete(d.getId(), "admin");
					break;
					case "loadDomDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadEdoDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadIdentDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadActaConst" : this.documentsService.delete(d.getId(), "admin");
						break;
					default : 
						break;
		    	}
		     } 
		    
		    s.setFileList("");
	        s.setCurrentApprover("");
	        s.setNextApprover("");
	        s.setApprovalStep("THIRD");
	        s.setApprovalStatus("RENEW");
	        s.setRejectNotes(notes);
	        this.approvalDao.updateSupplier(s);

	        String emailContent = AppConstants.EMAIL_REJECT_SUPPLIER_NOTIFICATION;
	  		emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
	  		emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
	  		emailContent = emailContent.replace("_REASON_", notes);
	        
	        EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	        emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), emailRecipient);
	        emailAsyncSup.setMailSender(this.mailSenderObj);
	        Thread emailThreadSup = new Thread(emailAsyncSup);
	        emailThreadSup.start();
	        
	        EmailServiceAsync emailAsyncSup2 = new EmailServiceAsync();
	        emailAsyncSup2.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), s.getEmailComprador());
	        emailAsyncSup2.setMailSender(this.mailSenderObj);
	        Thread emailThreadSup2 = new Thread(emailAsyncSup2);
	        emailThreadSup2.start();
	        
	        return "Rejected";
	      } 
	    return "";
		}else {
			 if ("APROBADO".equals(status) && "FIRST".equals(step) && s.getNextApprover().equals("FINAL")) {
	      	        
	    	        String note = s.getApprovalNotes();
	    	        s.setApprovalNotes(note + " " + s.getCurrentApprover() + ": " + notes + " <br>");
	    	        s.setCurrentApprover("FINAL");
	    	        s.setNextApprover("FINAL");
	    	        s.setApprovalStep(step);
	    	        s.setApprovalStatus("APROBADO");
	    	        s.setFechaAprobacion(new Date ());
	    	        
                    s.setTipoMovimiento("C");
	    	      	s.setCreditMessage("");
	    	        s.setSearchType("V");
	    	        s.setHold("");
	    	          
	    	        s.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
		            s.setReplicationMessage(null);
		            s.setReplicationDate(null);
		            
		          //Se añade nuevo batch
			          NextNumber nn = this.nextNumberService.getNextNumber("ADDRESSBOOK");
			          String nextBatch = nn.getNextStr();
			          int batchNbr = Integer.valueOf(nextBatch).intValue();
			    	  s.setBatchNumber(String.valueOf(batchNbr));
			    	  batchNbr++;
			    	  nn.setNextStr(String.valueOf(batchNbr));
			    	  this.nextNumberService.updateNextNumber(nn);
			    	  
			    	  this.approvalDao.updateSupplier(s);

	    	        
	    	        
	    	        dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	    	    	usr, "Supplier Update Approval Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	    	    	notes,null, null, AppConstants.STATUS_APPROVALFINALSTEP, 
	    	    	null, AppConstants.STATUS_ACCEPT, AppConstants.SUPPLIER_MODULE);
	    	        
	    	        return "Success";
	    	      }
			 if ("APROBADO".equals(status) && "FIRST".equals(step) && !s.getNextApprover().equals("FINAL")) {

	    	        UDC udc = this.udcService.searchBySystemAndSystemRef("APPROVERUPDSUP", "SECOND_APPROVER", s.getUpdateApprovalFlow());
	    	        String nextApproverEmail = "";
	    	        String note = s.getApprovalNotes();
	    	        if(s.getApprovalNotes() == null) {
	    	        	s.setApprovalNotes(s.getCurrentApprover() + ": " + notes + " <br>");
	    	        }else {
	    	            s.setApprovalNotes(note + " " + s.getCurrentApprover() + ": " + notes + " <br>");	
	    	        }
	    	       // s.setLastApprover(s.getCurrentApprover());
	    	        if (udc != null) {
	    	          s.setCurrentApprover(udc.getStrValue1());
	    	          s.setNextApprover(udc.getStrValue2());
	    	          nextApproverEmail = udc.getKeyRef().trim();
	    	        } else {
	    	          s.setCurrentApprover("MarcoJulio.Reyes");
	    	          s.setNextApprover("Miguel.Gutierrez");
	    	        } 
	    	        s.setApprovalStep("SECOND");
	    	        s.setApprovalStatus("PENDIENTE");
	    	        
	    	        this.approvalDao.updateSupplier(s);
	    	        
	    	        dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	    	      	usr, "Supplier Update Approval Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	    	      	notes,null, null, AppConstants.FIRST_STEP, 
	    	      	null, AppConstants.STATUS_ACCEPT, AppConstants.SUPPLIER_MODULE);
	    	      	              
	    	      	      
	    	      	String emailContent = AppConstants.EMAIL_SECOND_APP_CONTENT;
	    	      	emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
	    	      	emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);

	    	      	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	    	      	emailAsyncSup.setProperties(AppConstants.EMAIL_SECOND_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), nextApproverEmail);
	    	      	emailAsyncSup.setMailSender(this.mailSenderObj);
	    	      	Thread emailThreadSup = new Thread(emailAsyncSup);
	    	      	emailThreadSup.start();
	    	      	
	    	        return "Success";
	 	      }
			 
			 if ("APROBADO".equals(status) && "SECOND".equals(step) && !s.getNextApprover().equals("FINAL")) {

	    	        UDC udc = this.udcService.searchBySystemAndSystemRef("APPROVERUPDSUP", "SECOND_APPROVER", s.getUpdateApprovalFlow());
	    	        String nextApproverEmail = "";
	    	        String note = s.getApprovalNotes();
	    	        if(s.getApprovalNotes() == null) {
	    	        	s.setApprovalNotes(s.getCurrentApprover() + ": " + notes + " <br>");
	    	        }else {
	    	            s.setApprovalNotes(note + " " + s.getCurrentApprover() + ": " + notes + " <br>");	
	    	        }
	    	       // s.setLastApprover(s.getCurrentApprover());
	    	        if (udc != null) {
	    	          s.setCurrentApprover(udc.getStrValue1());
	    	          s.setNextApprover(udc.getStrValue2());
	    	          nextApproverEmail = udc.getKeyRef().trim();
	    	        } else {
	    	          s.setCurrentApprover("MarcoJulio.Reyes");
	    	          s.setNextApprover("Miguel.Gutierrez");
	    	        } 
	    	        s.setApprovalStep("THIRD");
	    	        s.setApprovalStatus("PENDIENTE");
	    	        
	    	        this.approvalDao.updateSupplier(s);
	    	        
	    	        dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	    	      	usr, "Supplier Update Approval Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	    	      	notes,null, null, AppConstants.SECOND_STEP, 
	    	      	null, AppConstants.STATUS_ACCEPT, AppConstants.SUPPLIER_MODULE);
	    	      	              
	    	      	      
	    	      	String emailContent = AppConstants.EMAIL_SECOND_APP_CONTENT;
	    	      	emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
	    	      	emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);

	    	      	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	    	      	emailAsyncSup.setProperties(AppConstants.EMAIL_THIRD_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), nextApproverEmail);
	    	      	emailAsyncSup.setMailSender(this.mailSenderObj);
	    	      	Thread emailThreadSup = new Thread(emailAsyncSup);
	    	      	emailThreadSup.start();
	    	      	
	    	        return "Success";
	 	      }
			 
			 if ("APROBADO".equals(status) && "SECOND".equals(step) && s.getNextApprover().equals("FINAL")) {
				 
				 String note = s.getApprovalNotes();
	    	        s.setApprovalNotes(note + " " + s.getCurrentApprover() + ": " + notes + " <br>");
	    	        s.setCurrentApprover("FINAL");
	    	        s.setNextApprover("FINAL");
	    	        s.setApprovalStep(step);
	    	        s.setApprovalStatus("APROBADO");
	    	        s.setFechaAprobacion(new Date ());
	    	        
                 s.setTipoMovimiento("C");
	    	      	s.setCreditMessage("");
	    	        s.setSearchType("V");
	    	        s.setHold("");
	    	          
	    	        s.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
		            s.setReplicationMessage(null);
		            s.setReplicationDate(null);
		            
		          //Se añade nuevo batch
			          NextNumber nn = this.nextNumberService.getNextNumber("ADDRESSBOOK");
			          String nextBatch = nn.getNextStr();
			          int batchNbr = Integer.valueOf(nextBatch).intValue();
			    	  s.setBatchNumber(String.valueOf(batchNbr));
			    	  batchNbr++;
			    	  nn.setNextStr(String.valueOf(batchNbr));
			    	  this.nextNumberService.updateNextNumber(nn);
			    	  
			    	  this.approvalDao.updateSupplier(s);

	    	        
	    	        
	    	        dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	    	    	usr, "Supplier Update Approval Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	    	    	notes,null, null, AppConstants.STATUS_APPROVALFINALSTEP, 
	    	    	null, AppConstants.STATUS_ACCEPT, AppConstants.SUPPLIER_MODULE);
	    	        
	    	        return "Success";
	 	      }
			 	if ("APROBADO".equals(status) && "THIRD".equals(step) && s.getNextApprover().equals("FINAL")) {
				 
				 String note = s.getApprovalNotes();
	    	        s.setApprovalNotes(note + " " + s.getCurrentApprover() + ": " + notes + " <br>");
	    	        s.setCurrentApprover("FINAL");
	    	        s.setNextApprover("FINAL");
	    	        s.setApprovalStep(step);
	    	        s.setApprovalStatus("APROBADO");
	    	        s.setFechaAprobacion(new Date ());
	    	        
                 s.setTipoMovimiento("C");
	    	      	s.setCreditMessage("");
	    	        s.setSearchType("V");
	    	        s.setHold("");
	    	          
	    	        s.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
		            s.setReplicationMessage(null);
		            s.setReplicationDate(null);
		            
		          //Se añade nuevo batch
			          NextNumber nn = this.nextNumberService.getNextNumber("ADDRESSBOOK");
			          String nextBatch = nn.getNextStr();
			          int batchNbr = Integer.valueOf(nextBatch).intValue();
			    	  s.setBatchNumber(String.valueOf(batchNbr));
			    	  batchNbr++;
			    	  nn.setNextStr(String.valueOf(batchNbr));
			    	  this.nextNumberService.updateNextNumber(nn);
			    	  
			    	  this.approvalDao.updateSupplier(s);

	    	        
	    	        
	    	        dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	    	    	usr, "Supplier Update Approval Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	    	    	notes,null, null, AppConstants.STATUS_APPROVALFINALSTEP, 
	    	    	null, AppConstants.STATUS_ACCEPT, AppConstants.SUPPLIER_MODULE);
	    	        
	    	        return "Success";
	 	      }
			 if ("RECHAZADO".equals(status) && ("FIRST".equals(step) || "SECOND".equals(step) || "THIRD".equals(step))) {
			    	List<UserDocument> list = null;
			    		list =  this.documentsService.searchByAddressNumber(s.getAddresNumber());
			    	
				    for (UserDocument d : list) {
				    	switch(d.getDocumentType()) {
					    	case "loadRfcDoc" : this.documentsService.delete(d.getId(), "admin");
							break;
							case "loadDomDoc" : this.documentsService.delete(d.getId(), "admin");
								break;
							case "loadEdoDoc" : this.documentsService.delete(d.getId(), "admin");
								break;
							case "loadIdentDoc" : this.documentsService.delete(d.getId(), "admin");
								break;
							case "loadActaConst" : this.documentsService.delete(d.getId(), "admin");
								break;
							default : 
								break;
				    	}
				     } 
			      
				  s.setFileList("");
			      s.setCurrentApprover("REJECT");
			      s.setNextApprover("REJECT");
			      s.setApprovalStep("FIRST");
			      s.setApprovalStatus("RENEW");
			      s.setRejectNotes(notes);
			      s.setDataUpdateList("");
			      s.setUpdateApprovalFlow("");
			      this.approvalDao.updateSupplier(s);
			      
			      String emailContent = AppConstants.EMAIL_REJECT_SUPPLIER_NOTIFICATION;
					emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
					emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
					emailContent = emailContent.replace("_REASON_", notes);
			      
			      EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			      emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), emailRecipient);
			      emailAsyncSup.setMailSender(this.mailSenderObj);
			      Thread emailThreadSup = new Thread(emailAsyncSup);
			      emailThreadSup.start();
			      
			      EmailServiceAsync emailAsyncSup2 = new EmailServiceAsync();
			      emailAsyncSup2.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), s.getEmailComprador());
			      emailAsyncSup2.setMailSender(this.mailSenderObj);
			      Thread emailThreadSup2 = new Thread(emailAsyncSup2);
			      emailThreadSup2.start();
			     
			      return "Rejected";
			    }
			
		}
		return usr;
	  }
  
  public String updateSupplier_Bkp(int id, String status, String step, String notes) {
	    Supplier s = this.approvalDao.getSupplierById(id);
	    String emailRecipient = s.getEmailSupplier();
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		Date currentDate = new Date();
		String usr = auth.getName();  
	    if ("APROBADO".equals(status) && "FIRST".equals(step)) {
	      String nextApprover = s.getNextApprover().trim();
	      UDC udc = this.udcService.searchBySystemAndStrValue("APPROVER", "SECOND_APPROVER", nextApprover);
	      String nextApproverEmail = "";
	      if (udc != null) {
	        s.setCurrentApprover(udc.getStrValue1());
	        s.setNextApprover(udc.getStrValue2());
	        nextApproverEmail = udc.getKeyRef().trim();
	      } else {
	        s.setCurrentApprover("EARANDA");
	        s.setNextApprover("PREYNOSO");
	      } 
	      s.setApprovalStep("SECOND");
	      s.setApprovalStatus("PENDIENTE");
	      s.setApprovalNotes(notes);
	      this.approvalDao.updateSupplier(s);
	      
	      dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	      usr, "Supplier Approval Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	      notes,null, null, AppConstants.STATUS_APPROVALFINALSTEP, 
	      null, AppConstants.STATUS_ACCEPT, AppConstants.SUPPLIER_MODULE);
	              
	      
		  String emailContent = AppConstants.EMAIL_SECOND_APP_CONTENT;
		  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
		  emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);

	      EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	      emailAsyncSup.setProperties(AppConstants.EMAIL_SECOND_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), nextApproverEmail);
	      emailAsyncSup.setMailSender(this.mailSenderObj);
	      Thread emailThreadSup = new Thread(emailAsyncSup);
	      emailThreadSup.start();
	     
	      return "Success";
	    } 
	    if ("APROBADO".equals(status) && "SECOND".equals(step)) {
	        String nextApprover = s.getNextApprover().trim();
	        UDC udc = this.udcService.searchBySystemAndStrValue("APPROVER", "THIRD_APPROVER", nextApprover);
	        String nextApproverEmail = "";
	        if (udc != null) {
	          s.setCurrentApprover(udc.getStrValue1());
	          s.setNextApprover(udc.getStrValue2());
	          nextApproverEmail = udc.getKeyRef().trim();
	        } else {
	          s.setCurrentApprover("PREYNOSO");
	          s.setNextApprover("FINAL");
	        } 
	        s.setApprovalStep("THIRD");
	        s.setApprovalStatus("PENDIENTE");
	        s.setApprovalNotes(notes);
	        this.approvalDao.updateSupplier(s);
	        
	        
	        dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	        usr, "Supplier Approval Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	        notes,null, null, AppConstants.STATUS_APPROVALSECONDSTEP, 
	        null, AppConstants.STATUS_INPROCESS, AppConstants.SUPPLIER_MODULE);
	      	
	        
	  	  String emailContent = AppConstants.EMAIL_SECOND_APP_CONTENT;
	  	  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
	  	  emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
	  	  

	        EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	        emailAsyncSup.setProperties(AppConstants.EMAIL_THIRD_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), nextApproverEmail);
	        emailAsyncSup.setMailSender(this.mailSenderObj);
	        Thread emailThreadSup = new Thread(emailAsyncSup);
	        emailThreadSup.start();
	       
	        return "Success";
	      } 
	    if ("APROBADO".equals(status) && "THIRD".equals(step)) {
	      s.setCurrentApprover("FINAL");
	      s.setNextApprover("FINAL");
	      s.setApprovalStep("THIRD");
	      s.setApprovalStatus("APROBADO");
	      s.setApprovalNotes(notes);
	      s.setFechaAprobacion(new Date());
	      
	      if(s.getAddresNumber() == "" ||  "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null) {
	          s.setTipoMovimiento("A");
	          s.setCreditMessage("");
	          s.setSearchType("V");
	          s.setHold("");
	          
	          Supplier jdeS = EDIService.registerNewAddressBook(s);
	          if(jdeS==null) {
	              s.setAddresNumber(null);
	              s.setReplicationStatus(AppConstants.STATUS_ERROR_REPLICATION);
	              s.setReplicationMessage("Error JDE");
	              s.setReplicationDate(null);
	              return "Error JDE";
	          }else {
	        	  s.setAddresNumber(jdeS.getAddresNumber());
	        	  s.setBatchNumber(jdeS.getBatchNumber());
	        	  s.setUkuid(jdeS.getUkuid());
	        	  //s.setReplicationStatus(AppConstants.STATUS_SUCCESS_REPLICATION);
	        	  s.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
	              s.setReplicationMessage(null);
	              s.setReplicationDate(null);
	        	  
	        	  List<UserDocument> list = null;
	        	  if(s.getTaxId() != null && !"".equals(s.getTaxId())) {
	        		list =  this.documentsService.searchByAddressNumber("NEW_" + s.getTaxId());
	        	  }else {
	        		list =  this.documentsService.searchByAddressNumber("NEW_" + s.getRfc());
	        	  }
	              for (UserDocument d : list) {
	                d.setAddressBook(jdeS.getAddresNumber());
	                this.documentsService.update(d, new Date(), "admin");
	              } 
	              
	              this.approvalDao.updateSupplier(s);
	              
	              
	              dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	              usr, "Supplier Approval Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	              notes,null, null, AppConstants.STATUS_APPROVALFINALSTEP, 
	              null, AppConstants.STATUS_ACCEPT, AppConstants.SUPPLIER_MODULE);
	            	
	              
	        	  /*System.out.println("***** ADDRESSNUMBER:" + s.getAddresNumber());
	        	  UDC role = udcService.searchBySystemAndKey("ROLES", "SUPPLIER");
	              UDC userType = udcService.searchBySystemAndKey("USERTYPE", "SUPPLIER");

	        	  String tempPass = "cryo22";
	        	  String encodePass = Base64.getEncoder().encodeToString(tempPass.trim().getBytes());
	        	  encodePass = "==a20$" + encodePass; 
	              Users u = new Users();
	              u.setEmail(s.getEmailSupplier());
	              u.setName(s.getRazonSocial());
	              u.setPassword(encodePass);
	              u.setUserName(s.getAddresNumber());
	              u.setUserRole(role);
	              u.setUserType(userType);
	              u.setEnabled(true);
	              userService.save(u, null, null);
	              
	              
				  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				
				  String emailContent = AppConstants.EMAIL_ACCEPT_SUPPLIER_NOTIFICATION;
				  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
				  emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
				  emailContent = emailContent.replace("_USER_", s.getAddresNumber());
				  emailContent = emailContent.replace("_PASS_", tempPass.trim());

				  emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_ACCEPT, stringUtils.prepareEmailContent(emailContent), s.getEmailSupplier());
				  emailAsyncSup.setMailSender(mailSenderObj);
				  Thread emailThreadSup = new Thread(emailAsyncSup);
				  emailThreadSup.start();	*/
	              
	              return "Success";
	          }
	  
	      }else {
	    	  Users u = userService.getByUserName(s.getAddresNumber());
	    	  u.setEnabled(true);
	    	  userService.update(u, null, null);
	    	  
	    	  //Multiusuarios: Actualiza usuarios del proveedor también
	    	  List<Users> userList = userService.getByAddressNumber(s.getAddresNumber());
	    	  if(userList != null && !userList.isEmpty()) {
	    		  for(Users uSupplier : userList) {
    				  uSupplier.setEnabled(true);
    				  userService.update(uSupplier, null, null);
	    		  }
	    	  }
	      	
	    	  s.setTipoMovimiento("C");
	    	  s.setCreditMessage("");
	          s.setSearchType("V");
	          s.setHold("");
	          s.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
	          s.setReplicationMessage(null);
	          s.setReplicationDate(null);
	          
	          //Se añade nuevo batch
	          NextNumber nn = this.nextNumberService.getNextNumber("ADDRESSBOOK");
	          String nextBatch = nn.getNextStr();
	          int batchNbr = Integer.valueOf(nextBatch).intValue();
	    	  s.setBatchNumber(String.valueOf(batchNbr));
	    	  batchNbr++;
	    	  nn.setNextStr(String.valueOf(batchNbr));
	    	  this.nextNumberService.updateNextNumber(nn);
	    	  
	    	  this.approvalDao.updateSupplier(s);
	    	  
	    	  dataAuditService.saveDataAudit("SupplierApproval",s.getAddresNumber(), currentDate, request.getRemoteAddr(),
	    	  usr, "Supplier Reject Successful - Ticket: " + s.getTicketId(), "updateSupplier", 
	    	  notes,null, null, AppConstants.STATUS_APPROVALFIRSTSTEP, 
	    	  null, AppConstants.STATUS_REJECT, AppConstants.SUPPLIER_MODULE);       
	    	  
	    	  EDIService.registerNewAddressBook(s);
	    	  
			  String credentials = "Usuario: " + s.getAddresNumber() + "<br />&nbsp; url: " + AppConstants.EMAIL_PORTAL_LINK ;
			  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			  emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_ACCEPT, stringUtils.prepareEmailContent(AppConstants.EMAIL_MASS_SUPPLIER_CHANGE_NOTIFICATION + credentials), s.getEmailSupplier());
			  emailAsyncSup.setMailSender(mailSenderObj);
			  Thread emailThreadSup = new Thread(emailAsyncSup);
			  emailThreadSup.start();
	    	  
	    	  return "Succ Update";
	      }
	      
	    } 
	    

	    if ("RECHAZADO".equals(status) && "FIRST".equals(step)) {
	    	List<UserDocument> list = null;
	    	if(s.getAddresNumber() == "" ||  "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null) {
	    		if(s.getTaxId() != null && !"".equals(s.getTaxId())) {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getTaxId());
	    		  }else {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getRfc());
	    		  }
	    	}else {
	    		list =  this.documentsService.searchByAddressNumber(s.getAddresNumber());
	    	}
	    	
		    for (UserDocument d : list) {
		    	switch(d.getDocumentType()) {
			    	case "loadRfcDoc" : this.documentsService.delete(d.getId(), "admin");
					break;
					case "loadDomDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadEdoDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadIdentDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadActaConst" : this.documentsService.delete(d.getId(), "admin");
						break;
					default : 
						break;
		    	}
		     } 
	      
		  s.setFileList("");
	      s.setCurrentApprover("REJECT");
	      s.setNextApprover("REJECT");
	      s.setApprovalStep("FIRST");
	      s.setApprovalStatus("RENEW");
	      s.setRejectNotes(notes);
	      this.approvalDao.updateSupplier(s);
	      
	      String emailContent = AppConstants.EMAIL_REJECT_SUPPLIER_NOTIFICATION;
			emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
			emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
			emailContent = emailContent.replace("_REASON_", notes);
	      
	      EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	      emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), emailRecipient);
	      emailAsyncSup.setMailSender(this.mailSenderObj);
	      Thread emailThreadSup = new Thread(emailAsyncSup);
	      emailThreadSup.start();
	      
	      EmailServiceAsync emailAsyncSup2 = new EmailServiceAsync();
	      emailAsyncSup2.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), s.getEmailComprador());
	      emailAsyncSup2.setMailSender(this.mailSenderObj);
	      Thread emailThreadSup2 = new Thread(emailAsyncSup2);
	      emailThreadSup2.start();
	     
	      return "Rejected";
	    } 
	    if ("RECHAZADO".equals(status) && "SECOND".equals(step)) {
	    	List<UserDocument> list = null;
	    	if(s.getAddresNumber() == "" ||  "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null) {
	    		if(s.getTaxId() != null && !"".equals(s.getTaxId())) {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getTaxId());
	    		  }else {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getRfc());
	    		  }
	    	}else {
	    		list =  this.documentsService.searchByAddressNumber(s.getAddresNumber());
	    	}
	    	
	    	for (UserDocument d : list) {
		    	switch(d.getDocumentType()) {
			    	case "loadRfcDoc" : this.documentsService.delete(d.getId(), "admin");
					break;
					case "loadDomDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadEdoDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadIdentDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadActaConst" : this.documentsService.delete(d.getId(), "admin");
						break;
					default : 
						break;
		    	}
		     } 
	    	
		  s.setFileList("");
	      s.setCurrentApprover("");
	      s.setNextApprover("");
	      s.setApprovalStep("SECOND");
	      s.setApprovalStatus("RENEW");
	      s.setRejectNotes(notes);
	      this.approvalDao.updateSupplier(s);

	      String emailContent = AppConstants.EMAIL_REJECT_SUPPLIER_NOTIFICATION;
			emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
			emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
			emailContent = emailContent.replace("_REASON_", notes);
	      
	      EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	      emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), emailRecipient);
	      emailAsyncSup.setMailSender(this.mailSenderObj);
	      Thread emailThreadSup = new Thread(emailAsyncSup);
	      emailThreadSup.start();
	      
	      EmailServiceAsync emailAsyncSup2 = new EmailServiceAsync();
	      emailAsyncSup2.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), s.getEmailComprador());
	      emailAsyncSup2.setMailSender(this.mailSenderObj);
	      Thread emailThreadSup2 = new Thread(emailAsyncSup2);
	      emailThreadSup2.start();
	      
	      return "Rejected";
	    } 
	    if ("RECHAZADO".equals(status) && "THIRD".equals(step)) {
	    	
	    	List<UserDocument> list = null;
	    	if(s.getAddresNumber() == "" ||  "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null) {
	    		if(s.getTaxId() != null && !"".equals(s.getTaxId())) {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getTaxId());
	    		  }else {
	    			list =  this.documentsService.searchByAddressNumber("NEW_" + s.getRfc());
	    		  }
	    	}else {
	    		list =  this.documentsService.searchByAddressNumber(s.getAddresNumber());
	    	}
	    	
	    	for (UserDocument d : list) {
		    	switch(d.getDocumentType()) {
			    	case "loadRfcDoc" : this.documentsService.delete(d.getId(), "admin");
					break;
					case "loadDomDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadEdoDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadIdentDoc" : this.documentsService.delete(d.getId(), "admin");
						break;
					case "loadActaConst" : this.documentsService.delete(d.getId(), "admin");
						break;
					default : 
						break;
		    	}
		     } 
		    
		    s.setFileList("");
	        s.setCurrentApprover("");
	        s.setNextApprover("");
	        s.setApprovalStep("THIRD");
	        s.setApprovalStatus("RENEW");
	        s.setRejectNotes(notes);
	        this.approvalDao.updateSupplier(s);

	        String emailContent = AppConstants.EMAIL_REJECT_SUPPLIER_NOTIFICATION;
	  		emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
	  		emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
	  		emailContent = emailContent.replace("_REASON_", notes);
	        
	        EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	        emailAsyncSup.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), emailRecipient);
	        emailAsyncSup.setMailSender(this.mailSenderObj);
	        Thread emailThreadSup = new Thread(emailAsyncSup);
	        emailThreadSup.start();
	        
	        EmailServiceAsync emailAsyncSup2 = new EmailServiceAsync();
	        emailAsyncSup2.setProperties(AppConstants.EMAIL_NEW_SUPPLIER_REJECT, this.stringUtils.prepareEmailContent(emailContent), s.getEmailComprador());
	        emailAsyncSup2.setMailSender(this.mailSenderObj);
	        Thread emailThreadSup2 = new Thread(emailAsyncSup2);
	        emailThreadSup2.start();
	        
	        return "Rejected";
	      } 
	    return "";
	  }
  
	public List<SupplierDTO> searchApproval(String ticketId,
			String approvalStep,
			String approvalStatus,
			Date fechaAprobacion,
			String currentApprover,
			String name,
            int start,
            int limit) {
		return approvalDao.searchApproval(ticketId, approvalStep, approvalStatus, fechaAprobacion, currentApprover, name, start, limit);
	}
	
	public int searchApprovalTotal(String ticketId,
			String approvalStep,
			String approvalStatus,
			Date fechaAprobacion,
			String currentApprover,
			String name) {
		return approvalDao.searchApprovalTotal(ticketId, approvalStep, approvalStatus, fechaAprobacion, currentApprover, name);
	}
	
	  public void updateSupplier(Supplier supplier) {
		    approvalDao.updateSupplier(supplier);
		    
		  }

		
	  public String reasignApprover(int id, String newApprover) {		  
	      Users u = userService.getByUserName(newApprover);
	      String pastApprover = "";
	      
	      DataAudit dataAudit = new DataAudit();
	      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	      HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	      String usr = auth.getName();
	      
	      if(u != null) {
	    	  
		      Supplier s = this.approvalDao.getSupplierById(id);
		      pastApprover = s.getCurrentApprover();
		      s.setCurrentApprover(newApprover);
		      approvalDao.updateSupplier(s);
		      
		      String emailContent = AppConstants.EMAIL_REASIGN_CONTENT;
	    	  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(s.getTicketId()));
	    	  emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);

	          EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	          emailAsyncSup.setProperties(AppConstants.EMAIL_REASIGN_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), u.getEmail());
	          emailAsyncSup.setMailSender(this.mailSenderObj);
	          Thread emailThreadSup = new Thread(emailAsyncSup);
	          emailThreadSup.start();
		  
	            dataAudit.setAction("SupplierReasignApprover");
	    		dataAudit.setAddressNumber(s.getAddresNumber());
	    		dataAudit.setCreationDate(new Date());
	    		dataAudit.setDocumentNumber(null);
	    		dataAudit.setIp(request.getRemoteAddr());
	    		dataAudit.setMethod("reasignApprover");
	    		dataAudit.setModule(AppConstants.APPROVALSEARCH_MODULE);    	
	    		dataAudit.setOrderNumber(null);
	    		dataAudit.setUuid(null);
	    		dataAudit.setStep(s.getApprovalStep());
	    		dataAudit.setMessage("Supplier Reasign Approver Successful - Ticket: " + s.getTicketId());
	    		dataAudit.setNotes("Past Approver: " + pastApprover + " - New Approver: " + newApprover);
	    		dataAudit.setStatus(s.getApprovalStatus());
	    		dataAudit.setUser(usr);
	    		
	    		dataAuditService.save(dataAudit);
			
	      }
	    
		    return "success";
	  }
	  
	  
	  public String reasignApproverFletes(int id, String newApprover,String etapa) {		  
	      Users u = userService.getByUserName(newApprover);
	     UDC udcaproval= udcService.searchBySystemAndStrValue("APPROVALFREIGHT", newApprover,etapa+"_APPROVER");
	      String pastApprover = "";
	      
	      DataAudit dataAudit = new DataAudit();
	      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	      HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	      String usr = auth.getName();
	      
	      if(u != null) {
	    	  
	    	  
	    	  FiscalDocuments flete=fiscalDocumentService.getById(id);
	    	  pastApprover=flete.getCurrentApprover();
	    	  flete.setCurrentApprover(newApprover);
	    	  
	    	  fiscalDocumentService.updateDocument(flete);
	    	  
		      
		      String emailContent = AppConstants.EMAIL_REASIGN_CONTENT_FLETE;
	    	  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(id));
	    	  emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);

	          EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	          emailAsyncSup.setProperties(AppConstants.EMAIL_REASIGN_SUBJECT_FLETES, this.stringUtils.prepareEmailContent(emailContent), u.getEmail());
	          emailAsyncSup.setMailSender(this.mailSenderObj);
	          Thread emailThreadSup = new Thread(emailAsyncSup);
	          emailThreadSup.start();
		  
	            dataAudit.setAction("reasignApproverFletes");
	    		dataAudit.setAddressNumber(null);
	    		dataAudit.setCreationDate(new Date());
	    		dataAudit.setDocumentNumber(null);
	    		dataAudit.setIp(request.getRemoteAddr());
	    		dataAudit.setMethod("reasignApproverFletes");
	    		dataAudit.setModule("APPROVALFLETE");    	
	    		dataAudit.setOrderNumber(null);
	    		dataAudit.setUuid(null);
	    		dataAudit.setStep(etapa);
	    		dataAudit.setMessage("Flete Reasign Approver Successful - Batch: " + id);
	    		dataAudit.setNotes("Past Approver: " + pastApprover + " - New Approver: " + newApprover);
	    		dataAudit.setStatus(flete.getApprovalStatus());
	    		dataAudit.setUser(usr);
	    		
	    		dataAuditService.save(dataAudit);
			
	      }
	    
		    return "success";
	  }
	  

}
