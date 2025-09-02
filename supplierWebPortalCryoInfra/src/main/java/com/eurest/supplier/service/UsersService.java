package com.eurest.supplier.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eurest.supplier.dao.SupplierDao;
import com.eurest.supplier.dao.UsersDao;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.StringUtils;

@Service("usersService")
public class UsersService {
	
	@Autowired
	UsersDao usersDao;
	
	@Autowired
	private JavaMailSender mailSenderObj;
	
	@Autowired
	private UdcService udcService;
	
	@Autowired
	SupplierService supplierService;
	
	@Autowired
	SupplierDao supplierDao;
	
	@Autowired
	StringUtils stringUtils;
	
	@Autowired
	DataAuditService dataAuditService;
	
	Logger log4j = Logger.getLogger(UsersService.class);
	
	public Users getusersById(int id){
		return usersDao.getUsersById(id);
	}
	
	public List<Users> getUsersList(int start, int limit) {
		return usersDao.getUsersList(start, limit);
	}
	
	public Users getUserByEmail(String email){
		return usersDao.getUserByEmail(email);
	}
	
	public Users getPurchaseRoleByEmail(String email){
		return usersDao.getPurchaseRoleByEmail(email);
	}
	
	public Users getByUserName(String userName){
		return usersDao.getByUserName(userName);
	}
	
	public List<Users> getByAddressNumber(String addressNumber){
		return usersDao.getByAddressNumber(addressNumber);
	}
	
	public List<Users> searchCriteria(String query){
		return usersDao.searchCriteria(query);
	}
	
	public Users searchCriteriaUserName(String query){
		return usersDao.searchCriteriaUserName(query);
	}
	
	public List<Users> searchCriteriaByRole(String query){
		return usersDao.searchCriteriaByRole(query);
	}
	
	public List<Users> searchCriteriaByRoleExclude(String query){
		return usersDao.searchCriteriaByRoleExclude(query);
	}
	
	public List<Users> getListUsersByUsername(List<String> usernames){
		return usersDao.getListUsersByUsername(usernames);
	}
	
	public String getMailFromUsersByAddressNumber(String addressNumber){
		String emailUsers = "";
		try {
			List<Users> supplierUsers = usersDao.getByAddressNumber(addressNumber);									
			if(supplierUsers != null && !supplierUsers.isEmpty()) {
				List<String> emails = new ArrayList<String>();
				for(Users u : supplierUsers){
					if(u.getEmail() != null && !u.getEmail().isEmpty() && !emails.contains(u.getEmail())) {
						emails.add(u.getEmail());
					}
				}
				emailUsers = String.join(", ", emails);
			}
		} catch (Exception e) {
			log4j.error("Exception" , e);
		}
		return emailUsers;
	}
	
	public void save(Users users, Date date, String user){		
		UDC udc = udcService.getUdcById(users.getUserRole().getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
 		String userAuth = auth.getName();
 		Date currentDate = new Date();
		if(udc != null) users.setRole(udc.getStrValue1());
		
		String pass = users.getPassword();
		pass = pass.replace("==a20$", "");
		if(!isValidBase64(pass))
		{
			String encodedPass = Base64.getEncoder().encodeToString(users.getPassword().trim().getBytes());
		    users.setPassword("==a20$" + encodedPass);
		}
		
		//Multiusuarios: Si es el usuario principal del proveedor, deshabilitar los demás usuarios como usuario principal
		if(users.isMainSupplierUser() && users.getAddressNumber() != null && !users.getAddressNumber().isEmpty()) {
	    	  List<Users> userList = usersDao.getByAddressNumber(users.getAddressNumber());
	    	  if(userList != null && !userList.isEmpty()) {
	    		  for(Users uSupplier : userList) {
	    			  uSupplier.setSubUser(true);	    			  
	  				  uSupplier.setMainSupplierUser(false);
	  				  usersDao.updateUsers(uSupplier);
	    		  }
	    	  }
	    	  //El usuario principal del proveedor deja de ser subusuario
	    	  users.setSubUser(false);
		}
		
		if(!"ROLE_SUPPLIER".equals(udc.getStrValue1())) {
			users.setAddressNumber(null);
			users.setSubUser(false);
			users.setMainSupplierUser(false);
		}
		
		usersDao.saveUsers(users);
		
		dataAuditService.saveDataAudit("SaveUser", null, currentDate, request.getRemoteAddr(),
     	userAuth, users.toString(), "save", "Save User Successful " + users.toString(),null, null, null, 
		null, AppConstants.STATUS_COMPLETE, AppConstants.USERS_MODULE);
	}
	
	public void saveUsersList(List<Users> users){
		usersDao.saveUsersList(users);
	}
	
	public void update(Users users, Date date, String user){
		UDC udc = udcService.getUdcById(users.getUserRole().getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
 		String userAuth = auth.getName();
 		Date currentDate = new Date();
 		
 		Users userOld = usersDao.getUsersById(users.getId());
		if(udc != null) users.setRole(udc.getStrValue1());
		
		String pass = users.getPassword();
		pass = pass.replace("==a20$", "");
		if(!isValidBase64(pass))
		{
			String encodedPass = Base64.getEncoder().encodeToString(users.getPassword().trim().getBytes());
		    users.setPassword("==a20$" + encodedPass);
		}
		
		if(users.isSupplier()) {
			Supplier o = supplierService.searchByAddressNumber(users.getAddressNumber());
			o.setEmailSupplier(users.getEmail());
			supplierDao.updateSupplier(o);
		}
		
		//Multiusuarios: Si es el usuario principal del proveedor, deshabilitar los demás usuarios como usuario principal
		if(users.isMainSupplierUser() && users.getAddressNumber() != null && !users.getAddressNumber().isEmpty()) {
	    	  List<Users> userList = usersDao.getByAddressNumber(users.getAddressNumber());
	    	  if(userList != null && !userList.isEmpty()) {
	    		  for(Users uSupplier : userList) {
	    			  uSupplier.setSubUser(true);
	  				  uSupplier.setMainSupplierUser(false);
	  				  usersDao.updateUsers(uSupplier);
	    		  }
	    	  }
	    	  //El usuario principal del proveedor deja de ser subusuario
	    	  users.setSubUser(false);
		}
		
		if(!"ROLE_SUPPLIER".equals(udc.getStrValue1())) {
			users.setAddressNumber(null);
			users.setSubUser(false);
			users.setMainSupplierUser(false);
		}
		
		usersDao.updateUsers(users);
		
		dataAuditService.saveDataAudit("UpdateUser", null, currentDate, request.getRemoteAddr(),
	    userAuth, users.toString(), "update", "User Old - " + userOld.toString() ,null, null, null, 
		null, AppConstants.STATUS_COMPLETE, AppConstants.USERS_MODULE);							
	}
	
	public void updateAgreement(Users users){
		users.setAgreementAccept(true);
		usersDao.updateUsers(users);
	}
	
	public void delete(int id){
		usersDao.deleteUsers(id);
	}
	
	public int getTotalRecords(){
		return usersDao.getTotalRecords();
	}
	
	public boolean isValidBase64( String string ) {
		try {
			@SuppressWarnings("unused")
			byte[] decodedBytes = Base64.getDecoder().decode(string);
			return true;
		}catch(Exception e) {
			//log4j.error("Exception" , e);
			log4j.error("Illegal base64 character.");
			return false;
		}
	}
	
	public String validateUser(Users user) {
		try {
			
			//Solo se valida cuando es un usuario nuevo
			if(user.getId() == 0) {				
				//Valida que el usuario no exista en Base de Datos
				Users u = usersDao.searchCriteriaUserName(user.getUserName());
				if(u != null) {
					return "Error_1";
				}	
			}
			
			//Validaciones al actualizar o crear un usuario nuevo de tipo proveedor
			UDC udc = udcService.getUdcById(user.getUserRole().getId());
			if(udc != null) {
				String role = udc.getStrValue1();
				if("ROLE_SUPPLIER".equals(role)) {
					
					//Valida que exista el proveedor en el portal y no esté pasando por un flujo de aprobación
					Supplier s = supplierDao.searchByAddressNumber(user.getAddressNumber());
					if(s != null) {
						if("PENDIENTE".equals(s.getApprovalStatus())) {
							return "Error_2";
						}						
					} else {
						return "Error_3";
					}
					
					//Valida que por lo menos exista un usuario marcado como principal para el proveedor
					if(!user.isMainSupplierUser()) {
						Users u = usersDao.getMainSupplierUser(user.getAddressNumber());
						if(u == null || (u != null && u.getUserName().equals(user.getUserName()))) {
							return "Error_4";
						}
					}
					
					//Valida que sea SubUsuario o Usuario Principal
					if(!user.isSubUser() && !user.isMainSupplierUser()) {
						return "Error_5";
					}
				}
			}

			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			return "Ocurrió un error inesperado al guardar el usuario.";
		}
		return "";
	}
	
	public String resetPassword(String username){

			Users u = usersDao.searchCriteriaUserName(username);
			if(u != null){
				String tempPass = getAlphaNumericString(8);;
				String encodePass = Base64.getEncoder().encodeToString(tempPass.trim().getBytes());
				u.setPassword("==a20$" + encodePass); 
				u.setLogged(false);
				usersDao.updateUsers(u);

				String emailRecipient = (u.getEmail());
				String credentials = "<br /><br />Usuario: " + u.getUserName() + "<br />Contraseña: " + tempPass + " <br /> url: " + AppConstants.EMAIL_PORTAL_LINK ;
				EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				emailAsyncSup.setProperties(AppConstants.PASS_RESET, stringUtils.prepareEmailContent(AppConstants.EMAIL_PASS_RESET_NOTIFICATION + credentials), emailRecipient);
				emailAsyncSup.setMailSender(mailSenderObj);
				Thread emailThreadSup = new Thread(emailAsyncSup);
				emailThreadSup.start();	

				return "";
			}else {
				return "La cuenta de usuario no existe";
			}	
		
	}
	
	private String getAlphaNumericString(int n) 
	{ 

		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
									+ "0123456789"
									+ "abcdefghijklmnopqrstuvxyz"; 

		StringBuilder sb = new StringBuilder(n); 

		for (int i = 0; i < n; i++) { 
			int index 
				= (int)(AlphaNumericString.length() 
						* Math.random()); 
			sb.append(AlphaNumericString 
						.charAt(index)); 
		} 
		return sb.toString(); 
	}
	
}
