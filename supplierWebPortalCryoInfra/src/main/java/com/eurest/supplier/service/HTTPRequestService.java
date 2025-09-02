package com.eurest.supplier.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.eurest.supplier.util.AppConstants;

@Service("HTTPRequestService")
public class HTTPRequestService {
	
	private static Logger log4j = Logger.getLogger(HTTPRequestService.class);


	public static String httpPost(String destUrl, String postData) throws Exception {
		URL url = new URL(destUrl);
		String response = "";
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (conn == null) {
			return null;
		}
		conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setAllowUserInteraction(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("User-Agent", "Java Client");

		OutputStream out = conn.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
		writer.write(postData);
		writer.close();
		out.close();

		int state = conn.getResponseCode();
        if (state == HttpURLConnection.HTTP_OK) {
    		InputStream in = conn.getInputStream();
    		InputStreamReader iReader = new InputStreamReader(in);
    		BufferedReader bReader = new BufferedReader(iReader);

    		String line;
    		log4j.info("==================Service response: ================ ");
    		while ((line = bReader.readLine()) != null) {
    			log4j.info(line);
    			response += line;
    		}
    		iReader.close();
    		bReader.close();
    		in.close();
    		conn.disconnect();
    		return response;
        }else {
        	return"ERROR";
        }
	}

    public String performSoapCall(String xmlContent) {
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(xmlContent, AppConstants.CFDI_VALIDATION_ACTION), AppConstants.CFDI_VALIDATION_URL);
            soapConnection.close();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapResponse.writeTo(out);
            return new String(out.toByteArray(),"UTF-8");
        } catch (Exception e) {
        	log4j.error("Exception" , e);
            e.printStackTrace();
            return "ERROR";
        }
    }
    
    private static SOAPMessage createSOAPRequest(String payload, String soapAction) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        createSoapEnvelope(payload, soapMessage);
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);
        soapMessage.saveChanges();
        return soapMessage;
    }
    
    private static void createSoapEnvelope(String payload, SOAPMessage soapMessage) throws SOAPException {
    	SOAPPart soapPart = soapMessage.getSOAPPart();
        String myNamespace = "tem";
        String myNamespaceURI = "http://tempuri.org/";
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("Consulta", myNamespace);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("expresionImpresa", myNamespace);
        soapBodyElem1.addTextNode(payload);
    }
}
