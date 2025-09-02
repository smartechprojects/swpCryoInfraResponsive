package com.eurest.supplier.service;

import java.io.File;
import java.util.Date;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.eurest.supplier.dao.UDCDao;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.ApplicationContextProvider;

public class EmailServiceAsync implements Runnable{
	
	private JavaMailSender mailSenderObj;

	private String recipient = "";
	private String subject = "";
	private String message = "";
	private String CCrecipient = "";
	private UDCDao udcDao = null;
	private String orderType;
	
	private Resource image = null;
	private Resource imageBlank = null;
	
	ApplicationContext context = null;
	
	private Logger log4j = Logger.getLogger(EmailServiceAsync.class);
	
	public void setProperties(String subject, String message, String recipient, String CCrecipient) {
		this.recipient = recipient;
		this.message = message;
		this.subject = subject;
		this.CCrecipient=CCrecipient;
	}
	public void setProperties(String subject, String message, String recipient) {
		this.recipient = recipient;
		this.message = message;
		this.subject = subject;
	}

	public void setMailSender( JavaMailSender mailSenderObj) {
		this.mailSenderObj = mailSenderObj;
		this.context = ApplicationContextProvider.getApplicationContext();
	}
	
	public void setAdditionalReference(UDCDao udcDao, String orderType) {
		this.udcDao = udcDao;
		this.orderType = orderType;
	}
	
	private synchronized void sendEmail(){
		this.image =  context.getResource("classpath:com/eurest/supplier/util/cryoInfra-email-logo.jpg");
		this.imageBlank =  context.getResource("classpath:com/eurest/supplier/util/blank.jpg");
		try {
			
			if(orderType != null) {
				UDC udc = udcDao.searchBySystemAndKey("EMAILHDR", orderType);
				if(udc != null) {
					subject = subject.replace("Estimado Proveedor", udc.getStrValue1());
					message = message.replace("Estimado Proveedor", udc.getStrValue1());
					message = message.replace("Portal de Proveedores", udc.getStrValue2());
				}
			}
			
			InternetAddress[] parseRecipients = InternetAddress.parse(recipient , true);
			InternetAddress[] CCparseRecipients = InternetAddress.parse(CCrecipient , true);
			mailSenderObj.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");				
					mimeMsgHelperObj.setTo(parseRecipients);
					mimeMsgHelperObj.setCc(CCparseRecipients);
					mimeMsgHelperObj.setFrom(AppConstants.EMAIL_FROM);				
					mimeMsgHelperObj.setSubject(subject);
					mimeMsgHelperObj.setText(message, true);
					
					if(image != null) {
						mimeMsgHelperObj.addInline( "imageContent",new File( image.getURI() ));
					}
					
					if(imageBlank != null) {
						mimeMsgHelperObj.addInline( "imageContent",new File( imageBlank.getURI() ));
					}
				}
			});
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}

    @Override
    public void run() {
    	log4j.info("CRYO INFRA - Send email: " + recipient + " " + String.valueOf(new Date()));
    	sendEmail();
    }

}
