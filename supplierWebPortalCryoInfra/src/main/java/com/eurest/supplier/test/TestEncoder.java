package com.eurest.supplier.test;

import java.util.Base64;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestEncoder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String password = "0eQCWTb3";
	    String encodePass = Base64.getEncoder().encodeToString(password.trim().getBytes());
		System.out.println("==a20$" + encodePass);

		encodePass ="U2FhdmkuMDA=";
		byte[] decodedBytes = Base64.getDecoder().decode(encodePass);
		String decodedPass = new String(decodedBytes);
		decodedPass = decodedPass.replace("==a20$", "");
		System.out.println("Decoded:" + decodedPass);
		
	}

}
