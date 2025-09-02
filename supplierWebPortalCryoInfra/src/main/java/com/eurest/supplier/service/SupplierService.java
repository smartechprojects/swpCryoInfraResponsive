package com.eurest.supplier.service;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eurest.supplier.dao.CodigosPostalesDao;
import com.eurest.supplier.dao.SupplierDao;
import com.eurest.supplier.dto.SupplierDTO;
import com.eurest.supplier.edi.SupplierJdeDTO;
import com.eurest.supplier.model.AccessTokenRegister;
import com.eurest.supplier.model.CodigosPostales;
import com.eurest.supplier.model.NextNumber;
import com.eurest.supplier.model.NonComplianceSupplier;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service("supplierService")
public class SupplierService {
  @Autowired
  JavaMailSender mailSenderObj;
  
  @Autowired
  SupplierDao supplierDao;
  
  @Autowired
  UdcService udcService;
  
  @Autowired
  UsersService usersService;
  
  @Autowired
  CodigosPostalesDao codigosPostalesDao;
  
  @Autowired
  DocumentsService documentsService;
  
  @Autowired
  EmailService emailService;
  
  @Autowired
  NextNumberService nextNumberService;
  
  @Autowired
  JDERestService jDERestService;
  
  @Autowired
  StringUtils stringUtils;
  
  @Autowired
  EDIService EDIService;
  
  @Autowired
  NonComplianceSupplierService nonComplianceSupplierService;
  
  @Autowired
  DataAuditService dataAuditService;
  
  @Autowired
  FiscalDocumentService fiscalDocumentService;
  
  private final String SECRET = "aink_45$11SecKey";
  
  private Logger log4j = Logger.getLogger(SupplierService.class);
  
  public Supplier getSupplierById(int id) {
    Supplier s = this.supplierDao.getSupplierById(id);
   /* if (!"APROBADO".equals(s.getApprovalStatus()) 
    	&& ( "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null || s.getAddresNumber() == ""))
    	s.addresNumber(""); */
    
    if(s != null) {
    	if("".equals(s.getAddresNumber()) ||  "0".equals(s.getAddresNumber()) || s.getAddresNumber() == null) {
    	}
    	else{
    		List<UserDocument> docs = documentsService.searchByAddressNumber(s.getAddresNumber());
        	if(docs != null) {
        		s.setFileList("");
        		String newFileList = "";
        		for(UserDocument doc : docs) {
        			if(!"Factura".equals(doc.getFiscalType())) {
        				newFileList = newFileList + "_FILE:" + doc.getId() + "_:_" + doc.getName() + "_:_" + doc.getSize();
        			}
        		}
        		s.setFileList(newFileList);
        	}
    	}
    }
    return s;
  }
  
  public List<SupplierDTO> getList(int start, int limit) {
	  log4j.info("*********** STEP 2: getList:start:" + start + ",limit:" + limit);
    return this.supplierDao.getList(start, limit);
  }
  
  public List<Supplier> getListPendingReplication(int start, int limit) {
	  log4j.info("*********** STEP 2: getListPendingReplication:start:" + start + ",limit:" + limit);
    return this.supplierDao.getListPendingReplication(start, limit);
  }
  
  
  
  public void sendAddressBookTest() {
	  try {
	    Supplier s =  this.supplierDao.getSupplierById(32);
	    Supplier jdeS = EDIService.registerNewAddressBook(s);
	    int i = 0;
	  }catch(Exception e) {
		  log4j.error("Exception" , e);
		  e.printStackTrace();
	  }
	    
  }
  
  public List<SupplierDTO> searchByCriteria(String query, int start, int limit) {
    return this.supplierDao.searchByCriteria(query, start, limit);
  }
  
  public Supplier searchByAddressNumber(String addressNumber) {
    return this.supplierDao.searchByAddressNumber(addressNumber);
  }
  
 /* public Supplier searchByAddressNumber(String addressNumber) {
	  Float f = Float.valueOf(addressNumber);
	  addressNumber = String.valueOf(f.intValue());
    return this.supplierDao.searchByAddressNumber(addressNumber);
  }*/
  
  public Supplier searchByApprover(String currentApprover) {
    return this.supplierDao.searchByApprover(currentApprover);
  }
  
  public long updateSupplier(Supplier o) {
    NonComplianceSupplier ncs = this.nonComplianceSupplierService.searchByTaxId(o.getRfc(), 0, 0);
    Date currentDate = new Date();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	String usr = auth.getName();
	Users user = null;
	if(usr != null) {
		user = usersService.searchCriteriaUserName(usr);
	}
	Supplier objForm = o;
    if (ncs != null && (
      ncs.getRefDate1().contains("Definitivo") || 
      ncs.getRefDate1().contains("Presunto") || 
      ncs.getRefDate1().contains("Desvirtuado") || 
      ncs.getRefDate2().contains("Definitivo") || 
      ncs.getRefDate2().contains("Presunto") || 
      ncs.getRefDate2().contains("Desvirtuado") || 
      ncs.getStatus().contains("Definitivo") || 
      ncs.getStatus().contains("Presunto") || 
      ncs.getStatus().contains("Desvirtuado"))) {
      o.setTicketId(Long.valueOf(-1L));
      return -1;
    } 
    
    if(o.getTicketId() != null) {
	    long currentTicket = o.getTicketId();
    	if (currentTicket == 0L) {
    	      long leftLimit = 1L;
    	      long rightLimit = 1000000000000L;
    	      long randomTicket = leftLimit + (long)(Math.random() * rightLimit);
    	      o.setTicketId(Long.valueOf(randomTicket));
    	    }
    }else {
    	long leftLimit = 1L;
	    long rightLimit = 1000000000000L;
	    long randomTicket = leftLimit + (long)(Math.random() * rightLimit);
	    o.setTicketId(Long.valueOf(randomTicket));
    }
     
    String docReference = "";
    //o.setBatchNumber("");
    if (o.getTaxId() != null && !"".equals(o.getTaxId())) {
    	docReference = o.getTaxId(); 
    }else {
    	docReference = o.getRfc();
    }
    
    //Multiusuarios: Obtiene usuarios del proveedor
    List<Users> userList = usersService.getByAddressNumber(o.getAddresNumber());
    
    //Define correos de proveedor y usuarios del proveedor
	  String emailSupplierAndUsers = "";	  
	  if(userList != null && !userList.isEmpty()) {
		  for(Users u : userList) {
			  if(u.getEmail() != null && !u.getEmail().trim().isEmpty()) {
				  emailSupplierAndUsers = emailSupplierAndUsers + "," + u.getEmail().trim();
			  }
		  }
	  }
	  
   if ("DRAFT".equals(o.getApprovalStatus())) {
   // if (o.getId() != 0 && "DRAFT".equals(o.getApprovalStatus())) {
      List<UserDocument> list = this.documentsService.searchByAddressNumber("NEW_" + docReference);
      StringBuilder fileList = new StringBuilder();
      for (UserDocument d : list) {
        fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
        //this.documentsService.update(d, new Date(), "admin");
      } 
  	  o.setEmailContactoPedidos(o.getEmailSupplier());
  	  o.setName(o.getRazonSocial());
      o.setFileList(fileList.toString());
      o.setFechaSolicitud(new Date());
      o.setSearchType("V");
	  o.setCreditMessage("");
	  o.setDataUpdateList("");
      o.setUpdateApprovalFlow("");
	  
	  if(o.getEstado() == "0" ||o.getEstado()== null) o.setEstado("");
	  

	  
      EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
      emailAsyncPur.setProperties("CryoInfra - Registro como borrador en Alta de Proveedor. Ticket " + o.getTicketId(), this.stringUtils.prepareEmailContent("Estimado proveedor <br><br>Hemos recibido una solicitud como borrador en nuestros sistemas. Su solicitud será procesada una vez que someta el formato de forma definitiva. <br> <br> Puede continuar actualizando sus datos utilizando el número de ticket que le enviamos a continuación " + o.getTicketId() + "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK), emailSupplierAndUsers);
      emailAsyncPur.setMailSender(this.mailSenderObj);
      Thread emailThreadPur = new Thread(emailAsyncPur);
      emailThreadPur.start();
      
    } /*else if (o.getId() != 0 && "NEW".equals(o.getApprovalStatus()) || 
    	o.getId() != 0 && "RENEW".equals(o.getApprovalStatus())) {*/
    else if (o.getId() != 0 && "NEW".equals(o.getApprovalStatus()) || 
        	o.getId() != 0 && "RENEW".equals(o.getApprovalStatus())  && 
        	(o.getDataUpdateList() == null || o.getDataUpdateList().equals(""))) {
      //String emailRecipient = o.getEmailSupplier();
      String reference = o.getEmailComprador().toLowerCase();
      UDC userTax = udcService.searchBySystemAndKey("TAXUSER", "01");
      UDC udc = this.udcService.searchBySystemAndKeyRef("APPROVER", "FIRST_APPROVER", userTax.getStrValue2().trim().toLowerCase());
      if (udc != null) {
        o.setCurrentApprover(udc.getStrValue1());
        o.setNextApprover(udc.getStrValue2());
        String fstApprover = udc.getKeyRef().trim();
        //Users fstApprover = usersService.getByUserName(o.getCurrentApprover());
        
        String emailContent = AppConstants.EMAIL_FIRST_APP_CONTENT;
  	  	emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
  	  	emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
  	  
        EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
        emailAsyncPur.setProperties(AppConstants.EMAIL_FIRST_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), fstApprover);
        emailAsyncPur.setMailSender(this.mailSenderObj);
        Thread emailThreadPur = new Thread(emailAsyncPur);
        emailThreadPur.start();
      } else {
        o.setCurrentApprover("approver1");
        o.setNextApprover("approver2");
      }
      
      //Users fstApprover = usersService.getByUserName(o.getCurrentApprover());
      
      StringBuilder fileList = new StringBuilder();
      
      if(o.getAddresNumber() == "" ||  "0".equals(o.getAddresNumber()) || o.getAddresNumber() == null) {
    	  List<UserDocument> list = this.documentsService.searchByAddressNumber("NEW_" + docReference);
	      for (UserDocument d : list) {
	        fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
	        //this.documentsService.update(d, new Date(), "admin");
	      } 
      }else {
    	  List<UserDocument> listOld = this.documentsService.searchByAddressNumber(o.getAddresNumber());
	        for (UserDocument d : listOld) {
	          fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
	          //this.documentsService.update(d, new Date(), "admin");
	        } 
      }
      
      o.setApprovalStep("FIRST");
      o.setApprovalStatus("PENDIENTE");
      o.setApprovalNotes("");
      o.setRejectNotes("");
      o.setWebSite("SEND");
      o.setEmailContactoPedidos(o.getEmailSupplier());
  	  o.setName(o.getRazonSocial());
      //o.setFileList(fileList.toString());
      o.setFechaSolicitud(new Date());
      o.setSearchType("V");
	  o.setCreditMessage("");
	  o.setDataUpdateList("");
      o.setUpdateApprovalFlow("");
	  
	  if(o.getEstado() == "0" ||o.getEstado()== null) o.setEstado("");
	  
      o.setFileList(fileList.toString());
     
      EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
      String emailContent = AppConstants.EMAIL_REQUEST_RECEIVED_CONTENT;
      emailContent = emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
      
      emailAsyncSup.setProperties(AppConstants.EMAIL_REQUEST_RECEIVED_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), emailSupplierAndUsers);
      emailAsyncSup.setMailSender(this.mailSenderObj);
      Thread emailThreadSup = new Thread(emailAsyncSup);
      emailThreadSup.start();

    /*
      emailContent = AppConstants.EMAIL_FIRST_APP_CONTENT;
	  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
	  emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
	  
      EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
      emailAsyncPur.setProperties(AppConstants.EMAIL_FIRST_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), fstApprover.getEmail());
      emailAsyncPur.setMailSender(this.mailSenderObj);
      Thread emailThreadPur = new Thread(emailAsyncPur);
      emailThreadPur.start();*/
    
    
    
    }else if (o.getId() != 0 && "PENDIENTE".equals(o.getApprovalStatus())) {
    	
    	StringBuilder fileList = new StringBuilder();
    	
    	if(o.getAddresNumber() == "" ||  "0".equals(o.getAddresNumber()) || o.getAddresNumber() == null) {
      	  List<UserDocument> list = this.documentsService.searchByAddressNumber("NEW_" + docReference);
  	      for (UserDocument d : list) {
  	        fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());	        
  	      } 
        }else {
      	  List<UserDocument> listOld = this.documentsService.searchByAddressNumber(o.getAddresNumber());
  	        for (UserDocument d : listOld) {
  	          fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
  	        } 
        }
      o.setFileList(fileList.toString());
      Supplier suppBD = supplierDao.getSupplierById(o.getId());
	   o = approvalUpdateSup(suppBD,o);
    }else if(o.getId() != 0 && "APROBADO".equals(o.getApprovalStatus())) {
    	if(o.getDataUpdateList() == null || o.getDataUpdateList().equals("")) {
    	Users u = usersService.getByUserName(o.getAddresNumber());
    	u.setEnabled(false);
    	u.setEmail(o.getEmailSupplier());
    	usersService.update(u, null, null);    	
    	//Multiusuarios: Actualiza usuarios del proveedor también
    	if(userList != null && !userList.isEmpty()) {
    		for(Users uSupplier : userList) {
				uSupplier.setEnabled(false);
		    	usersService.update(uSupplier, null, null);
    		}
    	}
    	String reference = o.getEmailComprador().toLowerCase();
    	UDC userTax = udcService.searchBySystemAndKey("TAXUSER", "01");
        UDC udc = this.udcService.searchBySystemAndKeyRef("APPROVER", "FIRST_APPROVER", userTax.getStrValue2().trim().toLowerCase());
        if (udc != null) {
          o.setCurrentApprover(udc.getStrValue1());
          o.setNextApprover(udc.getStrValue2());
          
          String fstApprover = udc.getKeyRef().trim();
          //Users fstApprover = usersService.getByUserName(o.getCurrentApprover());
          
          String emailContent = AppConstants.EMAIL_FIRST_APP_CONTENT;
    	  	emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
    	  	emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
    	  
          EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
          emailAsyncPur.setProperties(AppConstants.EMAIL_FIRST_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), fstApprover);
          emailAsyncPur.setMailSender(this.mailSenderObj);
          Thread emailThreadPur = new Thread(emailAsyncPur);
          emailThreadPur.start();
        } else {
          o.setCurrentApprover("approver1");
          o.setNextApprover("approver2");
        }
        o.setApprovalStep("FIRST");
        o.setApprovalStatus("PENDIENTE");
        o.setFechaSolicitud(new Date());
        o.setApprovalNotes("");
        o.setRejectNotes("");
        o.setEmailContactoPedidos(o.getEmailSupplier());
        o.setName(o.getRazonSocial());
    	}else {
    		//DACG
    		Supplier suppBD = supplierDao.getSupplierById(o.getId());
    		suppBD.setBatchNumber("");
    		suppBD.setTicketId(o.getTicketId());
    		o = approvalUpdateSup(suppBD,o);	
    	}
        
        List<UserDocument> listOld = this.documentsService.searchByAddressNumber(o.getAddresNumber());
        StringBuilder fileList = new StringBuilder();
        for (UserDocument d : listOld) {
          fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
          //this.documentsService.update(d, new Date(), "admin");
        } 
        o.setFileList(fileList.toString());
        
   Users fstApprover = usersService.getByUserName(o.getCurrentApprover());
        
        if(!o.getDataUpdateList().contains("checkEditImp")){
        	
        	 if(!"NA".equals(o.getUpdateApprovalFlow())){
        	String emailContent = AppConstants.EMAIL_FIRST_APP_CONTENT;
    	  	emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
    	  	emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
    	  
        	EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
            emailAsyncPur.setProperties(AppConstants.EMAIL_FIRST_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), fstApprover.getEmail());
            emailAsyncPur.setMailSender(this.mailSenderObj);
            Thread emailThreadPur = new Thread(emailAsyncPur);
            emailThreadPur.start();
        	 }
            
            
            EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
            String emailContentSupp = AppConstants.EMAIL_REQUEST_UPDATE_CONTENT;
            emailContentSupp = emailContentSupp = emailContentSupp.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
            
            emailAsyncSup.setProperties(AppConstants.EMAIL_REQUEST_UPDATE_SUBJECT, this.stringUtils.prepareEmailContent(emailContentSupp), emailSupplierAndUsers);
            emailAsyncSup.setMailSender(this.mailSenderObj);
            Thread emailThreadSup = new Thread(emailAsyncSup);
            emailThreadSup.start();
	     }
        
        /*Users fstApprover = usersService.getByUserName(o.getCurrentApprover());
        
        String emailContent = AppConstants.EMAIL_FIRST_APP_CONTENT;
  	    emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
  	    emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
  	  
        EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
        emailAsyncPur.setProperties(AppConstants.EMAIL_FIRST_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), fstApprover.getEmail());
        emailAsyncPur.setMailSender(this.mailSenderObj);
        Thread emailThreadPur = new Thread(emailAsyncPur);
        emailThreadPur.start();*/
        
        /*
        List<UserDocument> list = this.documentsService.searchByAddressNumber("NEW_" + docReference);
        for (UserDocument d : list) {
          if (!"".equals(o.getAddresNumber())) d.setAddressBook(o.getAddresNumber()); 
          fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getSize());
          //d.setAddressBook(o.getAddresNumber());
          this.documentsService.update(d, new Date(), "admin");
        } 
        o.setFileList(fileList.toString());*/
    } 
    this.supplierDao.updateSupplier(o);
    
    dataAuditService.saveDataAudit("UpdateSupplier", o.getAddresNumber(), currentDate, request.getRemoteAddr(),
    usr, "Supplier Updated Successful - Ticket: " + o.getTicketId(), "updateSupplier", o.getApprovalNotes(),null, null, null, 
    null, AppConstants.STATUS_COMPLETE, AppConstants.SUPPLIER_MODULE);
    
    return o.getTicketId();
  }
  
  public long updateSupplier_BKP(Supplier o) {
		
	    NonComplianceSupplier ncs = this.nonComplianceSupplierService.searchByTaxId(o.getRfc(), 0, 0);
	    Date currentDate = new Date();
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String usr = auth.getName();
		Supplier objForm = o;
	    if (ncs != null && (
	      ncs.getRefDate1().contains("Definitivo") || 
	      ncs.getRefDate1().contains("Presunto") || 
	      ncs.getRefDate1().contains("Desvirtuado") || 
	      ncs.getRefDate2().contains("Definitivo") || 
	      ncs.getRefDate2().contains("Presunto") || 
	      ncs.getRefDate2().contains("Desvirtuado") || 
	      ncs.getStatus().contains("Definitivo") || 
	      ncs.getStatus().contains("Presunto") || 
	      ncs.getStatus().contains("Desvirtuado"))) {
	      o.setTicketId(Long.valueOf(-1L));
	      return -1;
	    } 
	    
	    if(o.getTicketId() != null) {
		    long currentTicket = o.getTicketId();
	    	if (currentTicket == 0L) {
	    	      long leftLimit = 1L;
	    	      long rightLimit = 1000000000000L;
	    	      long randomTicket = leftLimit + (long)(Math.random() * rightLimit);
	    	      o.setTicketId(Long.valueOf(randomTicket));
	    	    }
	    }else {
	    	long leftLimit = 1L;
		    long rightLimit = 1000000000000L;
		    long randomTicket = leftLimit + (long)(Math.random() * rightLimit);
		    o.setTicketId(Long.valueOf(randomTicket));
	    }
	     
	    String docReference = "";
	    //o.setBatchNumber("");
	    if (o.getTaxId() != null && !"".equals(o.getTaxId())) {
	    	docReference = o.getTaxId(); 
	    }else {
	    	docReference = o.getRfc();
	    }
	    
	    if ("DRAFT".equals(o.getApprovalStatus())) {
	      List<UserDocument> list = this.documentsService.searchByAddressNumber("NEW_" + docReference);
	      StringBuilder fileList = new StringBuilder();
	      for (UserDocument d : list) {
	        fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
	        //this.documentsService.update(d, new Date(), "admin");
	      } 
	  	  o.setEmailContactoPedidos(o.getEmailSupplier());
	  	  o.setName(o.getRazonSocial());
	      o.setFileList(fileList.toString());
	      o.setFechaSolicitud(new Date());
	      o.setSearchType("V");
		  o.setCreditMessage("");
		  
		  if(o.getEstado() == "0" ||o.getEstado()== null) o.setEstado("");
	      EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
	      emailAsyncPur.setProperties("CryoInfra - Registro como borrador en Alta de Proveedor. Ticket " + o.getTicketId(), this.stringUtils.prepareEmailContent("Estimado proveedor <br><br>Hemos recibido una solicitud como borrador en nuestros sistemas. Su solicitud será procesada una vez que someta el formato de forma definitiva. <br> <br> Puede continuar actualizando sus datos utilizando el número de ticket que le enviamos a continuación " + o.getTicketId() + "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK), o.getEmailSupplier());
	      emailAsyncPur.setMailSender(this.mailSenderObj);
	      Thread emailThreadPur = new Thread(emailAsyncPur);
	      emailThreadPur.start();
	    } else if (o.getId() != 0 && "NEW".equals(o.getApprovalStatus()) || 
	    	o.getId() != 0 && "RENEW".equals(o.getApprovalStatus())) {
	      String emailRecipient = o.getEmailSupplier();
	      String reference = o.getEmailComprador().toLowerCase();
	      UDC userTax = udcService.searchBySystemAndKey("TAXUSER", "01");
	      UDC udc = this.udcService.searchBySystemAndKeyRef("APPROVER", "FIRST_APPROVER", userTax.getStrValue2().trim().toLowerCase());
	      if (udc != null) {
	        o.setCurrentApprover(udc.getStrValue1());
	        o.setNextApprover(udc.getStrValue2());
	        String fstApprover = udc.getKeyRef().trim();
	        //Users fstApprover = usersService.getByUserName(o.getCurrentApprover());
	        
	        String emailContent = AppConstants.EMAIL_FIRST_APP_CONTENT;
	  	  	emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
	  	  	emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
	  	  
	        EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
	        emailAsyncPur.setProperties(AppConstants.EMAIL_FIRST_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), fstApprover);
	        emailAsyncPur.setMailSender(this.mailSenderObj);
	        Thread emailThreadPur = new Thread(emailAsyncPur);
	        emailThreadPur.start();
	      } else {
	        o.setCurrentApprover("approver1");
	        o.setNextApprover("approver2");
	      }
	      
	      //Users fstApprover = usersService.getByUserName(o.getCurrentApprover());
	      
	      StringBuilder fileList = new StringBuilder();
	      
	      if(o.getAddresNumber() == "" ||  "0".equals(o.getAddresNumber()) || o.getAddresNumber() == null) {
	    	  List<UserDocument> list = this.documentsService.searchByAddressNumber("NEW_" + docReference);
		      for (UserDocument d : list) {
		        fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
		        //this.documentsService.update(d, new Date(), "admin");
		      } 
	      }else {
	    	  List<UserDocument> listOld = this.documentsService.searchByAddressNumber(o.getAddresNumber());
		        for (UserDocument d : listOld) {
		          fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
		          //this.documentsService.update(d, new Date(), "admin");
		        } 
	      }
	      
	      o.setApprovalStep("FIRST");
	      o.setApprovalStatus("PENDIENTE");
	      o.setApprovalNotes("");
	      o.setRejectNotes("");
	      o.setWebSite("SEND");
	      o.setEmailContactoPedidos(o.getEmailSupplier());
	  	  o.setName(o.getRazonSocial());
	      //o.setFileList(fileList.toString());
	      o.setFechaSolicitud(new Date());
	      o.setSearchType("V");
		  o.setCreditMessage("");
		  
		  if(o.getEstado() == "0" ||o.getEstado()== null) o.setEstado("");
		  
	      o.setFileList(fileList.toString());
	     
	      EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	      String emailContent = AppConstants.EMAIL_REQUEST_RECEIVED_CONTENT;
	      emailContent = emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
	      
	      emailAsyncSup.setProperties(AppConstants.EMAIL_REQUEST_RECEIVED_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), emailRecipient);
	      emailAsyncSup.setMailSender(this.mailSenderObj);
	      Thread emailThreadSup = new Thread(emailAsyncSup);
	      emailThreadSup.start();
	    /*
	      emailContent = AppConstants.EMAIL_FIRST_APP_CONTENT;
		  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
		  emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
		  
	      EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
	      emailAsyncPur.setProperties(AppConstants.EMAIL_FIRST_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), fstApprover.getEmail());
	      emailAsyncPur.setMailSender(this.mailSenderObj);
	      Thread emailThreadPur = new Thread(emailAsyncPur);
	      emailThreadPur.start();*/
	    
	    
	    
	    }else if (o.getId() != 0 && "PENDIENTE".equals(o.getApprovalStatus())) {
	    	
	    	StringBuilder fileList = new StringBuilder();
	    	
	    	if(o.getAddresNumber() == "" ||  "0".equals(o.getAddresNumber()) || o.getAddresNumber() == null) {
	      	  List<UserDocument> list = this.documentsService.searchByAddressNumber("NEW_" + docReference);
	  	      for (UserDocument d : list) {
	  	        fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());	        
	  	      } 
	        }else {
	      	  List<UserDocument> listOld = this.documentsService.searchByAddressNumber(o.getAddresNumber());
	  	        for (UserDocument d : listOld) {
	  	          fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
	  	        } 
	        }
	      o.setFileList(fileList.toString());
	    }else if(o.getId() != 0 && "APROBADO".equals(o.getApprovalStatus())) {
	    	Users u = usersService.getByUserName(o.getAddresNumber());
	    	u.setEnabled(false);
	    	u.setEmail(o.getEmailSupplier());
	    	usersService.update(u, null, null);
	          //Multiusuarios: Actualiza usuarios del proveedor también
	    	  List<Users> userList = usersService.getByAddressNumber(o.getAddresNumber());
	    	  if(userList != null && !userList.isEmpty()) {
	    		  for(Users uSupplier : userList) {
					  uSupplier.setEnabled(false);
					  usersService.update(uSupplier, null, null);
	    		  }
	    	  }
	    	String reference = o.getEmailComprador().toLowerCase();
	    	UDC userTax = udcService.searchBySystemAndKey("TAXUSER", "01");
	        UDC udc = this.udcService.searchBySystemAndKeyRef("APPROVER", "FIRST_APPROVER", userTax.getStrValue2().trim().toLowerCase());
	        if (udc != null) {
	          o.setCurrentApprover(udc.getStrValue1());
	          o.setNextApprover(udc.getStrValue2());
	          
	          String fstApprover = udc.getKeyRef().trim();
	          //Users fstApprover = usersService.getByUserName(o.getCurrentApprover());
	          
	          String emailContent = AppConstants.EMAIL_FIRST_APP_CONTENT;
	    	  	emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
	    	  	emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
	    	  
	          EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
	          emailAsyncPur.setProperties(AppConstants.EMAIL_FIRST_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), fstApprover);
	          emailAsyncPur.setMailSender(this.mailSenderObj);
	          Thread emailThreadPur = new Thread(emailAsyncPur);
	          emailThreadPur.start();
	        } else {
	          o.setCurrentApprover("approver1");
	          o.setNextApprover("approver2");
	        }
	        o.setApprovalStep("FIRST");
	        o.setApprovalStatus("PENDIENTE");
	        o.setFechaSolicitud(new Date());
	        o.setApprovalNotes("");
	        o.setRejectNotes("");
	        o.setEmailContactoPedidos(o.getEmailSupplier());
	        o.setName(o.getRazonSocial());
	        
	        List<UserDocument> listOld = this.documentsService.searchByAddressNumber(o.getAddresNumber());
	        StringBuilder fileList = new StringBuilder();
	        for (UserDocument d : listOld) {
	          fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
	          //this.documentsService.update(d, new Date(), "admin");
	        } 
	        o.setFileList(fileList.toString());
	        
	        /*Users fstApprover = usersService.getByUserName(o.getCurrentApprover());
	        
	        String emailContent = AppConstants.EMAIL_FIRST_APP_CONTENT;
	  	    emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
	  	    emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
	  	  
	        EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
	        emailAsyncPur.setProperties(AppConstants.EMAIL_FIRST_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), fstApprover.getEmail());
	        emailAsyncPur.setMailSender(this.mailSenderObj);
	        Thread emailThreadPur = new Thread(emailAsyncPur);
	        emailThreadPur.start();*/
	        
	        /*
	        List<UserDocument> list = this.documentsService.searchByAddressNumber("NEW_" + docReference);
	        for (UserDocument d : list) {
	          if (!"".equals(o.getAddresNumber())) d.setAddressBook(o.getAddresNumber()); 
	          fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getSize());
	          //d.setAddressBook(o.getAddresNumber());
	          this.documentsService.update(d, new Date(), "admin");
	        } 
	        o.setFileList(fileList.toString());*/
	    } 
	    this.supplierDao.updateSupplier(o);
	    
	    dataAuditService.saveDataAudit("UpdateSupplier", o.getAddresNumber(), currentDate, request.getRemoteAddr(),
	    usr, "Supplier Updated Successful - Ticket: " + o.getTicketId(), "updateSupplier", o.getApprovalNotes(),null, null, null, 
	    null, AppConstants.STATUS_COMPLETE, AppConstants.SUPPLIER_MODULE);
	    
	    return o.getTicketId();
	  }
  
  public long saveSupplier(Supplier o) {
    NonComplianceSupplier ncs = this.nonComplianceSupplierService.searchByTaxId(o.getRfc(), 0, 0);
    Date currentDate = new Date();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	String usr = auth.getName();
	Users user = null;
	if(usr != null) {
		user = usersService.searchCriteriaUserName(usr);
	}
    if (ncs != null && (
      ncs.getRefDate1().contains("Definitivo") || 
      ncs.getRefDate1().contains("Presunto") || 
      ncs.getRefDate1().contains("Desvirtuado") || 
      ncs.getRefDate2().contains("Definitivo") || 
      ncs.getRefDate2().contains("Presunto") || 
      ncs.getRefDate2().contains("Desvirtuado") || 
      ncs.getStatus().contains("Definitivo") || 
      ncs.getStatus().contains("Presunto") || 
      ncs.getStatus().contains("Desvirtuado"))) {
      o.setTicketId(Long.valueOf(-1L));
      return -1L;
    } 
    o.setFechaSolicitud(new Date());
    o.setCurrentApprover("");
    //o.setAddresNumber("");
    String docReference = "";
    if (o.getTaxId() != null && !"".equals(o.getTaxId())) {
    	docReference = o.getTaxId(); 
    }else {
    	docReference = o.getRfc();
    } 
    long currentTicket = o.getTicketId().longValue();
    if (currentTicket == 0L) {
      long leftLimit = 1L;
      long rightLimit = 1000000000000L;
      long randomTicket = leftLimit + (long)(Math.random() * rightLimit);
      o.setTicketId(Long.valueOf(randomTicket));
    } 
    
    if ("DRAFT".equals(o.getApprovalStatus())) {
      //o.setBatchNumber("");
      List<UserDocument> list = this.documentsService.searchByAddressNumber("NEW_" + docReference);
      StringBuilder fileList = new StringBuilder();
      for (UserDocument d : list) {
        fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
        //this.documentsService.update(d, new Date(), "admin");
      } 
      o.setEmailContactoPedidos(o.getEmailSupplier());
  	  o.setName(o.getRazonSocial());
      o.setFileList(fileList.toString());
      o.setFechaSolicitud(new Date());
      o.setSearchType("V");
	  o.setCreditMessage("");
	  
	  if(o.getEstado() == "0" ||o.getEstado()== null) o.setEstado("");
      EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
      emailAsyncPur.setProperties("CryoInfra - Registro como borrador en Alta de Proveedor. Ticket " + o.getTicketId(), this.stringUtils.prepareEmailContent("Estimado proveedor <br><br>Hemos recibido una solicitud como borrador en nuestros sistemas. Su solicitud será procesada una vez que someta el formato de forma definitiva. <br> <br> Puede continuar actualizando sus datos utilizando el número de ticket que le enviamos a continuación " + o.getTicketId() + "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK), o.getEmailSupplier());
      emailAsyncPur.setMailSender(this.mailSenderObj);
      Thread emailThreadPur = new Thread(emailAsyncPur);
      emailThreadPur.start();
      
    } else {
      String emailRecipient = o.getEmailSupplier();
      String reference = o.getEmailComprador().toLowerCase();
      UDC userTax = udcService.searchBySystemAndKey("TAXUSER", "01");
      UDC udc = this.udcService.searchBySystemAndKeyRef("APPROVER", "FIRST_APPROVER", userTax.getStrValue2().trim().toLowerCase());
      if (udc != null) {
        o.setCurrentApprover(udc.getStrValue1());
        o.setNextApprover(udc.getStrValue2());
        
        String fstApprover = udc.getKeyRef().trim();
        //Users fstApprover = usersService.getByUserName(o.getCurrentApprover());
        
        String emailContent = AppConstants.EMAIL_FIRST_APP_CONTENT;
  	  	emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
  	  	emailContent = emailContent.replace("_URL_", AppConstants.EMAIL_PORTAL_LINK);
  	  
        EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
        emailAsyncPur.setProperties(AppConstants.EMAIL_FIRST_APP_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), fstApprover);
        emailAsyncPur.setMailSender(this.mailSenderObj);
        Thread emailThreadPur = new Thread(emailAsyncPur);
        emailThreadPur.start();
      } else {
        o.setCurrentApprover("approver1");
        o.setNextApprover("approver2");
      }
      
      
      o.setApprovalStep("FIRST");
      o.setApprovalStatus("PENDIENTE");
      o.setWebSite("SEND");
      o.setApprovalNotes("");
      o.setRejectNotes("");
      o.setEmailContactoPedidos(o.getEmailSupplier());
  	  o.setName(o.getRazonSocial());
      o.setFechaSolicitud(new Date());
      o.setSearchType("V");
	  o.setCreditMessage("");
	  
      //int addressNumber = this.jDERestService.getAddressBookNextNumber();
      //o.setAddresNumber(String.valueOf(addressNumber));
     
      EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
      String emailContent = AppConstants.EMAIL_REQUEST_RECEIVED_CONTENT;
	  emailContent = emailContent.replace("_NUMTICKET_", String.valueOf(o.getTicketId()));
      
      emailAsyncSup.setProperties(AppConstants.EMAIL_REQUEST_RECEIVED_SUBJECT, this.stringUtils.prepareEmailContent(emailContent), emailRecipient);
      emailAsyncSup.setMailSender(this.mailSenderObj);
      Thread emailThreadSup = new Thread(emailAsyncSup);
      emailThreadSup.start();
      
      StringBuilder fileList = new StringBuilder();
      
      if(o.getAddresNumber() == "" ||  "0".equals(o.getAddresNumber()) || o.getAddresNumber() == null) {
    	  List<UserDocument> list = this.documentsService.searchByAddressNumber("NEW_" + docReference);
	      for (UserDocument d : list) {
	        fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
	        //this.documentsService.update(d, new Date(), "admin");
	      } 
      }else {
    	  List<UserDocument> listOld = this.documentsService.searchByAddressNumber(o.getAddresNumber());
	        for (UserDocument d : listOld) {
	          fileList.append("_FILE:" + d.getId() + "_:_" + d.getName() + "_:_" + d.getUploadDate());
	          //this.documentsService.update(d, new Date(), "admin");
	        } 
      }
      
      o.setFileList(fileList.toString());
    } 
    o.setDiasCredito(null);
    this.supplierDao.saveSupplier(o);
    
    dataAuditService.saveDataAudit("SaveSupplier", o.getAddresNumber(), currentDate, request.getRemoteAddr(),
    usr, "Saved Supplier Successful - Ticket: " + o.getTicketId(), "saveSupplier", null,null, null, null, 
    null, AppConstants.STATUS_COMPLETE, AppConstants.SUPPLIER_MODULE);
    
    return o.getTicketId().longValue();
  }
  
  
public String disableSupplier(String id) {
	  try {
		  Supplier s = supplierDao.getSupplierById(Integer.valueOf(id));
		  Users u = usersService.getByUserName(s.getAddresNumber());
		  
		  if(s != null) {
			  s.setObservaciones("INHABILITADO");
			  s.setCreditMessage("9");
	          s.setSearchType("ZZZ");
	          s.setHold("C9");
	          s.setTipoMovimiento("C");
			  supplierDao.updateSupplier(s);
			  EDIService.registerNewAddressBook(s);
		  }
		  if(u != null) {
			  u.setEnabled(false);
			  usersService.update(u, null, null);
		  }
		  
          //Multiusuarios: Actualiza usuarios del proveedor también
    	  List<Users> userList = usersService.getByAddressNumber(s.getAddresNumber());
    	  if(userList != null && !userList.isEmpty()) {
    		  for(Users uSupplier : userList) {
				  uSupplier.setEnabled(false);
				  usersService.update(uSupplier, null, null);
    		  }
    	  }
    	  
		  return "Succ Disable";
	  }catch(Exception e) {
		  log4j.error("Exception" , e);
		  return "Fail Disable";
	  }
  }

public String updateEmailSupplier(int id, String emailSupplier) {
	  try {
		  Supplier s = supplierDao.getSupplierById(id);
		  Users u = usersService.getByUserName(s.getAddresNumber());
		  
		  if(s != null) {
			  s.setEmailSupplier(emailSupplier);
			  supplierDao.updateSupplier(s);
		  }
		  if(u != null) {
			  u.setEmail(emailSupplier);
			  usersService.update(u, null, null);
		  }
		  return "Succ Update Email";
	  }catch(Exception e) {
		  log4j.error("Exception" , e);
		  e.printStackTrace();
		  return "Fail Update Email";
	  }
}
  
  public int getTotalRecords() {
    return this.supplierDao.getTotalRecords();
  }
  
  public List<CodigosPostales> getByCode(String code, int start, int limit) {
    return this.codigosPostalesDao.getByCode(code, start, limit);
  }
  
  public List<SupplierDTO> listSuppliers(String supAddNbr, String supAddName, int start, int limit) {
    return this.supplierDao.listSuppliers(supAddNbr, supAddName, start, limit);
  }
  
  public int listSuppliersTotalRecords(String supAddNbr, String supAddName) {
    return this.supplierDao.listSuppliersTotalRecords(supAddNbr, supAddName);
  }
  
  public void saveSuppliers(List<Supplier> list) {
    this.supplierDao.saveSuppliers(list);
  }
  
  public Users getUserByEmail(String email) {
    return this.usersService.getUserByEmail(email);
  }
  
  public Users getPurchaseRoleByEmail(String email) {
    return this.usersService.getPurchaseRoleByEmail(email);
  }
  
  public int getRfcRecords(String rfc) {
    return this.supplierDao.getRfcRecords(rfc);
  }
  
  public List<Supplier> searchByRfc(String rfc, String typeSearch) {
	if("rfc".equals(typeSearch)) {
		return this.supplierDao.searchByRfc(rfc);
	}else {
		return this.supplierDao.searchByTaxId(rfc);
	}
    
  }
  
  public Supplier searchByTicket(long ticketId) {
    return this.supplierDao.searchByTicket(ticketId);
  }
  
  public void sendOutSourcingEmail(Supplier s) {
	  
	  EmailServiceAsync emailAsyncPur = new EmailServiceAsync();
	  String subject = AppConstants.OUTSOURCING_APPROVAL_SUBJECT;
	  subject = subject.replace("_SUPPLIER_", s.getRazonSocial());
	  String msg = this.stringUtils.prepareEmailContent(AppConstants.OUTSOURCING_APPROVED_MESSAGE);
	  
      emailAsyncPur.setProperties(subject, msg, s.getEmailSupplier());
      emailAsyncPur.setMailSender(this.mailSenderObj);
      Thread emailThreadPur = new Thread(emailAsyncPur);
      emailThreadPur.start();
  }
  
  public void updateSupplierOutSourcing(Supplier o) {
	  this.supplierDao.updateSupplier(o);
  }

  public void updateSupplierCore(Supplier o) {
	  this.supplierDao.updateSupplier(o);
  }

  public List<Supplier> searchByOutSourcingStatus() {
	  return this.supplierDao.searchByOutSourcingStatus();
  }
  
  public List <SupplierJdeDTO> sendAddressBook(List <SupplierJdeDTO> o) {
	  final SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
		String resp = "";
		try {
			if (o != null) {
				ObjectMapper jsonMapper = new ObjectMapper();
				String jsonInString = jsonMapper.writeValueAsString(o);
				log4j.info(jsonInString);

				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				final String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/postAddressBook";
				Map<String, String> params = new HashMap<String, String>();
				HttpEntity<?> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
				
				ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
						String.class, params);
				HttpStatus statusCode = responseEntity.getStatusCode();

				if (statusCode.value() == 200) {
					String body = responseEntity.getBody();
					ObjectMapper mapper = new ObjectMapper();
					SupplierJdeDTO response = mapper.readValue(body, SupplierJdeDTO.class);
					log4j.info("Guardado:" + response.getAddresNumber());
					return (List<SupplierJdeDTO>) response;
				}else {
					return null;
				}
			}
			return null;
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}
  }
  
  public void saveAccessToken(AccessTokenRegister o, String userName, String company) {

		o.setCompany(company);
		o.setCreatedBy(userName);
		Date currentDate = new Date();
		o.setCreationDate(currentDate);
		o.setUpdatedDate(currentDate);

		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, 3); // El token vence en 3 d�as posterior a su creaci�n
		o.setExpirationDate(c.getTime());
		o.setCode(StringUtils.randomString(6));
		String tempPass = StringUtils.randomString(8);
		String encodePassCode = Base64.getEncoder().encodeToString(tempPass.trim().getBytes());
		encodePassCode = "==a20$" + encodePassCode;
		o.setPassword(encodePassCode);

		String token = getJWTToken(o.getCode(), c.getTime());
		o.setToken(token);

		this.supplierDao.saveAccessToken(o);
		
		String secureUrl = AppConstants.EMAIL_PORTAL_LINK + "/public/authRegister?access_token=" + token;
		
		EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
		emailAsyncSup.setProperties(AppConstants.NEWREGISTER_SUBJECT, stringUtils.prepareEmailContent(AppConstants.NEWREGISTER_MESSAGE + secureUrl), o.getEmail());
		emailAsyncSup.setMailSender(mailSenderObj);
		Thread emailThreadSup = new Thread(emailAsyncSup);
		emailThreadSup.start();

	}
	
	public void updateAccessToken(AccessTokenRegister obj, String userName) {

		AccessTokenRegister o = supplierDao.getAccessTokenRegisterById(obj.getId());

		Date currentDate = new Date();
		o.setUpdatedDate(currentDate);
		o.setCode(StringUtils.randomString(6));

		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, 3); // El token vence en 3 d�as posterior a su creaci�n
		o.setExpirationDate(c.getTime());

		String token = getJWTToken(o.getCode(), c.getTime());
		o.setToken(token);
		o.setEnabled(true);
		
		o.setEmail(obj.getEmail());
		
		o.setUpdatedBy(userName);

		this.supplierDao.updateAccessToken(o);
		
		String secureUrl = AppConstants.EMAIL_PORTAL_LINK + "/public/authRegister?access_token=" + token;
		System.out.println(secureUrl);
		EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
		emailAsyncSup.setProperties(AppConstants.NEWREGISTER_RENEW_SUBJECT, stringUtils.prepareEmailContent(AppConstants.NEWREGISTER_RENEW_MESSAGE + secureUrl), o.getEmail());
		emailAsyncSup.setMailSender(mailSenderObj);
		Thread emailThreadSup = new Thread(emailAsyncSup);
		emailThreadSup.start();

	}

	private String getJWTToken(String username, Date expirationDate) {
		String secretKey = "aink_45$11SecKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts.builder().setId("smartechIdJWT").setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

		return token;
	}
	
	public List<AccessTokenRegister> listAccessTokenRegister(String query, int start, int limit) {
		return this.supplierDao.listAccessTokenRegister(query, start, limit);
	}

	public int listAccessTokenRegisterCount(String query) {
		return this.supplierDao.listAccessTokenRegisterCount(query);
	}
	
	public String validateAcccessTokenRegister(String token, HttpServletRequest request, boolean disable) {
		try {
			Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
			if (claims.get("authorities") != null) {
				return setUpSpringAuthentication(claims, token, disable);
			} else {
				return "invalidToken";
			}
		} catch (ExpiredJwtException e) {
		    return "invalidToken";
		} catch(Exception e){
		    return "invalidToken";
		}

	}
	
	private String setUpSpringAuthentication(Claims claims, String token, boolean disable) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<String> authorities = (List) claims.get("authorities");

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		
		AccessTokenRegister atr = supplierDao.searchActiveAccessCode(auth.getPrincipal().toString());
		if(atr != null) {
			if(disable) {
				atr.setEnabled(false);
			}
			supplierDao.updateAccessToken(atr);
			return "redirect:/newRegisterAuth.action?access_token=" + token;
		}
		
		return "invalidToken";

	}
	
	

public boolean existsAddressNumberForFlete(String userName) {
	// TODO Auto-generated method stub
	return fiscalDocumentService.existsAddressNumberForFlete(userName);
}

public Supplier approvalUpdateSup(Supplier objBD, Supplier objForm) {

	  String[] splitReg = objForm.getDataUpdateList().split(",");
	  Map<String, String> mapAprov = new HashMap<>();
	  String flujoAprov = null;
	  
	  for(String reg : splitReg) {
		  if(reg.equals("checkEditImp")){	 
			  objBD.setTaxAreaCxC(objForm.getTaxAreaCxC());
			  objBD.setPmtTrmCxC(objForm.getPmtTrmCxC());
			  objBD.setPayInstCxC(objForm.getPayInstCxC());
			  objBD.setGlClass(objForm.getGlClass());
			  return objBD;
		  }
	  }
	  
	  //First Aprover
	  for(String reg : splitReg) {
		  
		  
		  
		  if(reg.equals("checkEditDataSupp")){
			  //mapAprov.put("FISCAL", "FISCAL");
			  mapAprov.put("IMPUESTOS", "IMPUESTOS");
			  mapAprov.put("CONTABILIDAD", "CONTABILIDAD");
			  objBD.setFisicaMoral(objForm.getFisicaMoral());
			  //objBD.setCurrencyCode(objForm.getCurrencyCode());
			//  objBD.setRollNumber2(objForm.getRollNumber2());
			  
			  objBD.setTipoIdentificacion(objForm.getTipoIdentificacion());
			  objBD.setNumeroIdentificacion(objForm.getNumeroIdentificacion());
			  objBD.setNombreRL(objForm.getNombreRL());
			  objBD.setApellidoPaternoRL(objForm.getApellidoPaternoRL());
			  objBD.setApellidoMaternoRL(objForm.getApellidoMaternoRL());
			  
			  //DIRECCION FISCAL
			  
			  if(objForm.getCountry().equals("MX")) {
				  objBD.setCalleNumero(objForm.getCalleNumero());
				  objBD.setDelegacionMnicipio(objForm.getDelegacionMnicipio());
				  objBD.setCodigoPostal(objForm.getCodigoPostal());
				  objBD.setColonia(objForm.getColonia());
				  objBD.setEstado(objForm.getEstado());
				  objBD.setTelefonoDF(objForm.getTelefonoDF());
				  objBD.setFaxDF(objForm.getFaxDF());
			  }else {
				  objBD.setCalleNumero(objForm.getCalleNumero());
				  objBD.setCodigoPostal(objForm.getCodigoPostal());
				  objBD.setColonia(objForm.getColonia());
				  objBD.setEstado(objForm.getEstado());
				  objBD.setTelefonoDF(objForm.getTelefonoDF());
				  objBD.setFaxDF(objForm.getFaxDF());
			  }
			  
		  }
		  
		  if(reg.equals("checkEditFiscalAddress")){
			  mapAprov.put("IMPUESTOS", "IMPUESTOS");
			  mapAprov.put("CONTABILIDAD", "CONTABILIDAD");		  
			  if(objForm.getCountry().equals("MX")) {
				  objBD.setCalleNumero(objForm.getCalleNumero());
				  objBD.setDelegacionMnicipio(objForm.getDelegacionMnicipio());
				  objBD.setCodigoPostal(objForm.getCodigoPostal());
				  objBD.setColonia(objForm.getColonia());
				  objBD.setEstado(objForm.getEstado());
				  objBD.setTelefonoDF(objForm.getTelefonoDF());
				  objBD.setFaxDF(objForm.getFaxDF());
			  }else {
				  objBD.setCalleNumero(objForm.getCalleNumero());
				  objBD.setCodigoPostal(objForm.getCodigoPostal());
				  objBD.setColonia(objForm.getColonia());
				  objBD.setEstado(objForm.getEstado());
				  objBD.setTelefonoDF(objForm.getTelefonoDF());
				  objBD.setFaxDF(objForm.getFaxDF());
			  }
			  
		  }
		  
		  if(reg.equals("checkEditContact")){
			  mapAprov.put("NA", "NA");
			 // mapAprov.put("GTE_COMPRAS", "GTE_COMPRAS");
			  
			  objBD.setNombreContactoCxC(objForm.getNombreContactoCxC());
			  objBD.setEmailComprador(objForm.getEmailComprador());
			  objBD.setTelefonoContactoCxC(objForm.getTelefonoContactoCxC());
			  objBD.setCargoCxC(objForm.getCargoCxC());
			  objBD.setEmailSupplier(objForm.getEmailSupplier());
			  objBD.setNombreCxP01(objForm.getNombreCxP01());
			  objBD.setEmailCxP01(objForm.getEmailCxP01());
			  objBD.setTelefonoCxP01(objForm.getTelefonoCxP01());
			  objBD.setNombreCxP02(objForm.getNombreCxP02());
			  objBD.setEmailCxP02(objForm.getEmailCxP02());
			  objBD.setTelefonoCxP02(objForm.getTelefonoCxP02());
			  objBD.setNombreCxP03(objForm.getNombreCxP03());
			  objBD.setEmailCxP03(objForm.getEmailCxP03());
			  objBD.setTelefonoCxP03(objForm.getTelefonoCxP03());
			  objBD.setNombreCxP04(objForm.getNombreCxP04());
			  objBD.setEmailCxP04(objForm.getEmailCxP04());
			  objBD.setTelefonoCxP04(objForm.getTelefonoCxP04());

		  }
		  
		 /* if(reg.equals("checkEditLegalRepr")){
			  mapAprov.put("COMPRAS", "COMPRAS");
			  mapAprov.put("GTE_COMPRAS", "GTE_COMPRAS");
			  mapAprov.put("IMPUESTOS", "IMPUESTOS");
			  
			  objBD.setTipoIdentificacion(objForm.getTipoIdentificacion());
			  objBD.setNumeroIdentificacion(objForm.getNumeroIdentificacion());
			  objBD.setNombreRL(objForm.getNombreRL());
			  objBD.setApellidoPaternoRL(objForm.getApellidoPaternoRL());
			  objBD.setApellidoMaternoRL(objForm.getApellidoMaternoRL());
			  
		  }*/
		  
		  if(reg.equals("checkEditDataBank")){
			  mapAprov.put("TESORERIA", "TESORERIA");
			  mapAprov.put("CONTABILIDAD", "CONTABILIDAD");
			  
			  			  
			 /* if(objForm.getCountry().equals("MX")) {
				  objBD.setBankTransitNumber(objForm.getBankTransitNumber());
				  objBD.setCustBankAcct(objForm.getCustBankAcct());
				  objBD.setControlDigit(objForm.getControlDigit());
				  objBD.setDescription(objForm.getDescription());
			  }else {
				  objBD.setSwiftCode(objForm.getSwiftCode());
				  objBD.setIbanCode(objForm.getIbanCode());
				  objBD.setCheckingOrSavingAccount(objForm.getCheckingOrSavingAccount());
				  objBD.setRollNumber(objForm.getRollNumber());
				  objBD.setBankAddressNumber(objForm.getBankAddressNumber());
				  objBD.setBankCountryCode(objForm.getBankCountryCode());
				  
			  }*/
			  
			  objBD.setBankTransitNumber(objForm.getBankTransitNumber());
			  objBD.setCustBankAcct(objForm.getCustBankAcct());
			  objBD.setControlDigit(objForm.getControlDigit());
			  objBD.setDescription(objForm.getDescription());
			  objBD.setSwiftCode(objForm.getSwiftCode());
			  objBD.setIbanCode(objForm.getIbanCode());
			  objBD.setCheckingOrSavingAccount(objForm.getCheckingOrSavingAccount());
			  objBD.setRollNumber(objForm.getRollNumber());
			  objBD.setBankAddressNumber(objForm.getBankAddressNumber());
			  objBD.setBankCountryCode(objForm.getBankCountryCode());
			  
			 /* objBD.setRecordTypeExtranjero(objForm.getRecordTypeExtranjero());
			  objBD.setBankTransitNumber2(objForm.getBankTransitNumber2());
			  objBD.setCustBankAcct2(objForm.getCustBankAcct2());
			  objBD.setControlDigit2(objForm.getControlDigit2());
			  objBD.setIbanCode2(objForm.getIbanCode2());
			  objBD.setDescription2(objForm.getDescription2());
			  objBD.setCheckingOrSavingAccount2(objForm.getCheckingOrSavingAccount2());  
			  objBD.setSwiftCode2(objForm.getSwiftCode2());
			  objBD.setRollNumber2(objForm.getRollNumber2());
			  objBD.setBankAddressNumber2(objForm.getBankAddressNumber2());
			  objBD.setBankCountryCode2(objForm.getBankCountryCode2());*/
			  
			  
		  }
	  }
		  
		  if(!mapAprov.isEmpty()) {
			  
			  if(mapAprov.containsKey("IMPUESTOS") && !mapAprov.containsKey("TESORERIA") && mapAprov.containsKey("CONTABILIDAD") && !mapAprov.containsKey("NA")){
				  flujoAprov = "IMP_CONT";
			  }
			  
			  if(mapAprov.containsKey("IMPUESTOS") && !mapAprov.containsKey("TESORERIA") && mapAprov.containsKey("CONTABILIDAD") && mapAprov.containsKey("NA")){
				  flujoAprov = "IMP_CONT";
			  }
			  
			  if(!mapAprov.containsKey("IMPUESTOS") && mapAprov.containsKey("TESORERIA") && mapAprov.containsKey("CONTABILIDAD") && !mapAprov.containsKey("NA")){
				  flujoAprov = "TES_CONT";
			  }
			  
			  if(!mapAprov.containsKey("IMPUESTOS") && mapAprov.containsKey("TESORERIA") && mapAprov.containsKey("CONTABILIDAD") && mapAprov.containsKey("NA")){
				  flujoAprov = "TES_CONT";
			  }
			  
			  if(mapAprov.containsKey("IMPUESTOS") && mapAprov.containsKey("TESORERIA") && mapAprov.containsKey("CONTABILIDAD") && !mapAprov.containsKey("NA")){
				  flujoAprov = "IMP_TES_CONT";
			  }
			  
			  if(mapAprov.containsKey("IMPUESTOS") && mapAprov.containsKey("TESORERIA") && mapAprov.containsKey("CONTABILIDAD") && mapAprov.containsKey("NA")){
				  flujoAprov = "IMP_TES_CONT";
			  }
			  
			  if(!mapAprov.containsKey("IMPUESTOS") && !mapAprov.containsKey("TESORERIA") && !mapAprov.containsKey("CONTABILIDAD") && mapAprov.containsKey("NA")){
				  flujoAprov = "NA";
			  }
			 
			  
			  
		  }
		   
		   if(flujoAprov != null) {
			  
				Users u = usersService.getByUserName(objBD.getAddresNumber());
				String reference = objForm.getEmailComprador().toLowerCase();
		    	u.setEnabled(false);
		    	u.setEmail(objBD.getEmailSupplier());
		    	usersService.update(u, null, null);
		    	
		    	  //Multiusuarios: Actualiza usuarios del proveedor también
		    	  List<Users> userList = usersService.getByAddressNumber(objBD.getAddresNumber());
		    	  if(userList != null && !userList.isEmpty()) {
		    		  for(Users uSupplier : userList) {
	    				  uSupplier.setEnabled(false);
	    				  usersService.update(uSupplier, null, null);
		    		  }
		    	  }
		    	  
		    	UDC udc = null;
		    	if(!"NA".equals(flujoAprov)){
		    		udc = this.udcService.searchBySystemAndSystemRef("APPROVERUPDSUP", "FIRST_APPROVER", flujoAprov);
		    		if (udc != null) {
			        	objBD.setCurrentApprover(udc.getStrValue1());
			        	objBD.setNextApprover(udc.getStrValue2());
			        	objBD.setApprovalStep("FIRST");
					    objBD.setApprovalStatus("PENDIENTE");
			        } else {
			        	objBD.setCurrentApprover("");
			        	objBD.setNextApprover("");
			        } 
		    	}else if("NA".equals(flujoAprov)){

		    		objBD.setCurrentApprover("FINAL");
		    			objBD.setNextApprover("FINAL");
		    			objBD.setApprovalStep("FINAL");
		    			objBD.setApprovalStatus("APROBADO");
		    			objBD.setFechaAprobacion(new Date ());
		    	        
		    			objBD.setTipoMovimiento("C");
		    			objBD.setCreditMessage("");
		    			objBD.setSearchType("V");
		    			objBD.setHold("");
		    	          
		    			objBD.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
		    			objBD.setReplicationMessage(null);
		    			objBD.setReplicationDate(null);
		    			
		    			//Se añade nuevo batch
				          NextNumber nn = this.nextNumberService.getNextNumber("ADDRESSBOOK");
				          String nextBatch = nn.getNextStr();
				          int batchNbr = Integer.valueOf(nextBatch).intValue();
				          objBD.setBatchNumber(String.valueOf(batchNbr));
				    	  batchNbr++;
				    	  nn.setNextStr(String.valueOf(batchNbr));
				    	  this.nextNumberService.updateNextNumber(nn);
		    	}

		        objBD.setApprovalNotes("");
		        objBD.setRejectNotes("");
		        objBD.setUpdateApprovalFlow(flujoAprov);
		        objBD.setDataUpdateList(objForm.getDataUpdateList());  
		  }
			  
	return objBD;
	  

}

public Supplier updateSupInApproval(Supplier objBD, Supplier objForm) {
	  String[] splitReg = objForm.getDataUpdateList().split(",");
	  for(String reg : splitReg) {
		  
		  if(reg.equals("checkEditImp")){	 
			  objBD.setTaxAreaCxC(objForm.getTaxAreaCxC());
			  objBD.setPmtTrmCxC(objForm.getPmtTrmCxC());
			  objBD.setPayInstCxC(objForm.getPayInstCxC());
			  objBD.setGlClass(objForm.getGlClass());	  
		  }
	  }
	return objBD;
	  
}
  
}
