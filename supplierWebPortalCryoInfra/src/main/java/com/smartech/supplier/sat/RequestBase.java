package com.smartech.supplier.sat;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

abstract class RequestBase {

    private String xml;
    private final String url;
    private final String SOAPAction;

    static Logger log4j = Logger.getLogger(RequestBase.class);
    /**
     * Constructor of RequestBase class
     *
     * @param url
     * @param SOAPAction
     */
    protected RequestBase(String url, String SOAPAction) {
        this.xml = null;
        this.url = url;
        this.SOAPAction = SOAPAction;
    }

    protected void setXml(String xml) {
        this.xml = xml;
    }
    
    protected String getXml() {
        return this.xml;
    }

    /**
     * Get result of a previously obtained XML
     *
     * @param xmlResponse
     * @return
     */
    protected abstract String getResult(String xmlResponse);

    /**
     * Create digest SHA1 from a String and returning a Base64 String
     *
     * @param sourceData
     * @return
     * @throws NoSuchAlgorithmException
     */
    protected String createDigest(String sourceData) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(sourceData.getBytes());

        return Base64.getEncoder().encodeToString(digest.digest());
    }

    /**
     * Sign SHA1 with private key and a String and returning a Base64 String
     *
     * @param sourceData
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    protected String sign(String sourceData, PrivateKey privateKey) throws
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException {
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(privateKey);
        sig.update(sourceData.getBytes());

        return Base64.getEncoder().encodeToString(sig.sign());
    }

    /**
     * Create HttpURLConnection to send previously created XML
     *
     * @return
     * @throws IOException
     */
    public String send(String authorization) throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set timeout
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(20000);

        // Output true
        conn.setDoOutput(true);

        // Headers
        conn.setRequestProperty("Accept-Charset", "utf-8");
        conn.setRequestProperty("Content-type", "text/xml; charset=utf-8");
        conn.setRequestProperty("SOAPAction", SOAPAction);

        if (authorization != null) {
            conn.setRequestProperty("Authorization", authorization);
        }

        // Logging request
        log4j.info("Request URL: " + this.url);
        log4j.info("Authorization: " + authorization);
        log4j.info("SOAPAction: " + SOAPAction);
        log4j.info("Request XML:\n" + xml);

        // Write XML
        try (OutputStream outputStream = conn.getOutputStream()) {
            outputStream.write(xml.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }

        // Read response or error stream
        InputStream inputStream;
        try {
            inputStream = conn.getInputStream(); // Success case
        } catch (IOException e) {
            // Get the error stream if the connection failed
            inputStream = conn.getErrorStream();
            log4j.error("Exception getting InputStream: " + e.getMessage(), e);
        }

        // If inputStream is still null, return or throw
        if (inputStream == null) {
            throw new IOException("No response received from server.");
        }

        // Read the response
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
        }

        log4j.info("Response:\n" + response.toString());

        // Check for specific error messages
        String responseStr = response.toString();
        if (responseStr.contains("Certificado Revocado o Caduco") || 
            responseStr.contains("Certificado Inválido") || 
            responseStr.contains("Certificado Invalido")) {
            return responseStr;
        }

        return getResult(responseStr);
    }

    public String send_bkp(String authorization) throws IOException {
        URL url = new URL(this.url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set timeout as per needs
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(20000);

        // Set DoOutput to true if you want to use URLConnection for output.
        // Default is false
        conn.setDoOutput(true);

        // Set Headers
        conn.setRequestProperty("Accept-Charset", "utf-8");
        conn.setRequestProperty("Content-type", "text/xml; charset=utf-8");
        conn.setRequestProperty("SOAPAction", SOAPAction);

        if (authorization != null)
            conn.setRequestProperty("Authorization", authorization);

        // Write XML
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(xml.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
        log4j.info("this.url: " + this.url);
        log4j.info("authorization " + authorization);
        log4j.info("SOAPAction " + SOAPAction);
        InputStream inputStream = conn.getInputStream();

        // Read XML
        byte[] res = new byte[2048];
        int i;
        StringBuilder response = new StringBuilder();
        while ((i = inputStream.read(res)) != -1) {
            response.append(new String(res, 0, i));
        }
        inputStream.close();

        if (response.toString().contains("Certificado Revocado o Caduco") || 
        		response.toString().contains("Certificado Inválido") || 
        		response.toString().contains("Certificado Invalido")) {
        	return response.toString();
        }               
        return getResult(response.toString());
    }

    /**
     * Convert a String to XMl (Document Object)
     *
     * @param xmlString
     * @return
     */
    protected static Document convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
        	log4j.error("Exception" , e);
            e.printStackTrace();
        }

        return null;
    }
}
