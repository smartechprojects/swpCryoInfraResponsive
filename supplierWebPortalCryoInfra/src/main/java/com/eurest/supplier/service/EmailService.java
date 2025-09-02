package com.eurest.supplier.service;

import java.io.StringWriter;
import java.util.Date;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.eurest.supplier.model.PurchaseOrderPayment;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.FileUploadBean;
import com.eurest.supplier.util.MultiFileUploadBean;

@Service("emailService")
public class EmailService {
	
	 @Autowired
	 private JavaMailSender mailSenderObj;
	 
	 @Autowired
	 private VelocityEngine velocityEngine;

	private String emailSubject = "";
	private String emailMessage = "";

	Logger log4j = Logger.getLogger(EmailService.class);

	public synchronized void sendEmail(String subject, String message, String recipient){
		this.emailSubject = subject;
		this.emailMessage = message;

		try {
			InternetAddress[] parseRecipients = InternetAddress.parse(recipient , true);
			mailSenderObj.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");				
					mimeMsgHelperObj.setTo(parseRecipients);
					mimeMsgHelperObj.setFrom(AppConstants.EMAIL_FROM);				
					mimeMsgHelperObj.setText(emailMessage);
					mimeMsgHelperObj.setSubject(emailSubject);
				}
			});
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}
	
	public synchronized void sendEmailPagos(String subject, String message, String recipient, PurchaseOrderPayment o){
		this.emailSubject = subject;
		this.emailMessage = message;

		try {
			InternetAddress[] parseRecipients = InternetAddress.parse(recipient , true);
			mailSenderObj.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");				
					mimeMsgHelperObj.setTo(parseRecipients);
					mimeMsgHelperObj.setFrom(AppConstants.EMAIL_FROM);				
					mimeMsgHelperObj.setSubject(emailSubject);
					
					Template template = velocityEngine.getTemplate("./templates/pagoemailtemplate.vm");
					 
					VelocityContext velocityContext = new VelocityContext();
					
					
					
					velocityContext.put("supplierEmail", "Yashwant");
					velocityContext.put("buyerEmail", "Yashwant");

					velocityContext.put("numOperacion", "Yashwant");
					velocityContext.put("bancoPagador", "Yashwant");
					velocityContext.put("cuentaPagadora", "Yashwant");
					velocityContext.put("montoTotal", "Yashwant");
					             
					velocityContext.put("moneda", "Yashwant");
					velocityContext.put("fechaPago", "Yashwant");
					velocityContext.put("fechaCargaComplemento", "Yashwant");
			 
					StringWriter stringWriter = new StringWriter();
			 
					template.merge(velocityContext, stringWriter);
					mimeMsgHelperObj.setText(stringWriter.toString());
				}
			});
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}
	
	public void sendEmailWithAttach(String subject, String message, String recipient, FileUploadBean files){
		this.emailSubject = subject;
		this.emailMessage = message;
		
		try {
			InternetAddress[] parseRecipients = InternetAddress.parse(recipient , true);
			mailSenderObj.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");				
					mimeMsgHelperObj.setTo(parseRecipients);
					mimeMsgHelperObj.setFrom(AppConstants.EMAIL_FROM);				
					mimeMsgHelperObj.setText(emailMessage);
					mimeMsgHelperObj.setSubject(emailSubject);
					
					mimeMsgHelperObj.addAttachment(files.getFile().getOriginalFilename(), new ByteArrayResource(files.getFile().getBytes()),"text/plain");
					mimeMsgHelperObj.addAttachment(files.getFileTwo().getOriginalFilename(), new ByteArrayResource(files.getFileTwo().getBytes()),"application/pdf");
					
				}
			});
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}
	
	
	public void sendEmailWithAttach(String subject, String message, String recipient, byte[] file,String filename){
		this.emailSubject = subject;
		this.emailMessage = message;
		
		try {
			InternetAddress[] parseRecipients = InternetAddress.parse(recipient , true);
			mailSenderObj.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");				
					mimeMsgHelperObj.setTo(parseRecipients);
					mimeMsgHelperObj.setFrom(AppConstants.EMAIL_FROM);				
					mimeMsgHelperObj.setText(emailMessage,true);
					mimeMsgHelperObj.setSubject(emailSubject);
					
					mimeMsgHelperObj.addAttachment(filename, new ByteArrayResource(file),"application/pdf");
					
				}
			});
			log4j.info("CRYO INFRA - Send email (Adjunto): " + recipient + " " + String.valueOf(new Date()));
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}
	
	public void sendEmailAttachMultiPart(String subject, String message, String recipient, MultiFileUploadBean files, int filesListSize){
		this.emailSubject = subject;
		this.emailMessage = message;
		
		try {
			InternetAddress[] parseRecipients = InternetAddress.parse(recipient , true);
			mailSenderObj.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");				
					mimeMsgHelperObj.setTo(parseRecipients);
					mimeMsgHelperObj.setFrom(AppConstants.EMAIL_FROM);				
					mimeMsgHelperObj.setText(emailMessage, true);
					mimeMsgHelperObj.setSubject(emailSubject);
					
					for(int i = 0 ; i < filesListSize; i++) {
						mimeMsgHelperObj.addAttachment(files.getUploadedFiles().get(i).getOriginalFilename(), new ByteArrayResource(files.getUploadedFiles().get(i).getBytes()),files.getUploadedFiles().get(i).getContentType());
					}
				}
			});
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}

}
