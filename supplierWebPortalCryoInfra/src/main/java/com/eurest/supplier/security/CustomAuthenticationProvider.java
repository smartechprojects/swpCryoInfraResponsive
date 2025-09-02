package com.eurest.supplier.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.service.SupplierService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.service.UsersService;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.Cronato;

public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	
	@Autowired
	private UsersService usersService;

	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	UdcService udcService;
	
    @Override
    public Authentication authenticate(Authentication authentication)  throws AuthenticationException{
  
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        UDC udcReglas = udcService.searchBySystemAndKey("RULES", "ACCESS");
        Users u = usersService.searchCriteriaUserName(name);
        
        if(u != null) {
         /*   if(!(u.isExepAccesRule()||Arrays.asList(udcReglas.getStrValue2().toUpperCase().split(",")).contains(u.getRole().toUpperCase())) ) {
            	if(udcReglas!=null&&new Cronato().validateAcces(udcReglas.isBooleanValue(),udcReglas.getStrValue1())) {
            	throw new DisabledException(AppConstants.MSG_PORTAL_INACTIVE);
            	}
            }*/
            
        	if(u.getRole().startsWith(AppConstants.ROLE_PURCHASE_VALID)) {//Roles relacionados a proveedores
        		if(u.getAddressNumber() != null && !u.getAddressNumber().isEmpty()){
                	Supplier s = supplierService.searchByAddressNumber(u.getAddressNumber());            	
                	if(s != null) {
                		if(!AppConstants.STATUS_ACCEPT.equals(s.getApprovalStatus())) {
                			throw new DisabledException("Existe una autorización pendiente con su número de proveedor " + s.getAddresNumber() + ", favor de esperar a que sea autorizada. Gracias");
                		}
                	} else {
                		throw new DisabledException("El proveedor asignado no está registrado en el portal.");
                	}
        		} else {
        			throw new DisabledException("El usuario no tiene un proveedor asignado.");
        		}        		
        	}
        	
        	if(u.isEnabled()) {
        		    String encodePass = Base64.getEncoder().encodeToString(password.trim().getBytes());
        		    String pass = u.getPassword();
        		    pass = pass.replace("==a20$", "");
        		    
        		    if(encodePass.equals(pass)) {
        		    	String userRole = u.getRole();
                        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(new SimpleGrantedAuthority(userRole));
                        return new UsernamePasswordAuthenticationToken(name, password, authorities);
        		    }else {
        		            throw new BadCredentialsException("La contraseña es incorrecta");
        		    }
        	}else {
        		throw new DisabledException("El usuario se encuentra deshabilitado");
        	}

        }else {
        	throw new AuthenticationCredentialsNotFoundException("Las credenciales ingresadas son inválidas");
        }
        	
        



    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
          UsernamePasswordAuthenticationToken.class);
    }

}
