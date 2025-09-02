package com.smartech.supplier.sat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;

import com.eurest.supplier.invoiceXml.Comprobante;

public class Main {

    final static String urlAutentica = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/Autenticacion/Autenticacion.svc";
    final static String urlAutenticaAction = "http://DescargaMasivaTerceros.gob.mx/IAutenticacion/Autentica";

    final static String urlSolicitud = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/SolicitaDescargaService.svc";
    final static String urlSolicitudRecibidosAction = "http://DescargaMasivaTerceros.sat.gob.mx/ISolicitaDescargaService/SolicitaDescargaRecibidos";

    final static String urlVerificarSolicitud = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/VerificaSolicitudDescargaService.svc";
    final static String urlVerificarSolicitudAction = "http://DescargaMasivaTerceros.sat.gob.mx/IVerificaSolicitudDescargaService/VerificaSolicitudDescarga";

    final static String urlDescargarSolicitud = "https://cfdidescargamasiva.clouda.sat.gob.mx/DescargaMasivaService.svc";
	final static String urlDescargarSolicitudAction = "http://DescargaMasivaTerceros.sat.gob.mx/IDescargaMasivaTercerosService/Descargar";

    final static char[] pwdPFX = "asdfghj8".toCharArray(); // PFX's password
    final static String rfc = "SCG990720RF1";
    final static String dateStart = "2021-08-01"; // yyyy-MM-dd
    final static String dateEnd = "2021-08-20"; // yyyy-MM-dd

    static X509Certificate certificate = null;
    static PrivateKey privateKey = null;
    
    static boolean special = false;
    
    private static Logger log4j = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        
    	String filePath = "C:/temp/SCG990720RF1.pfx";
        File filePFX = new File(filePath);

        // Get certificate and private key from PFX file
        certificate = getCertificate(filePFX);
        privateKey = getPrivateKey(filePFX);

        processZipFileOnly();
        
        // Get Token
        String token = "WRAP access_token=\"" + decodeValue(getToken()) + "\"";
        
        // Get idRequest with token obtained
        String idRequest = getRequest(token);

        // Get idPackages with token and idRequest obtained
        String idPackages = getVerifyRequest(token, idRequest);

        // Get package in Base64 with token and idPackages obtained
        String packageString = getDownload(token, idPackages);
        
        
        if(packageString != null) {
        	decodeDownload(packageString);
        }
        
    }

    /**
     * Get a certificate through a pfx file
     *
     * @param file
     * @return
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     */
    public static X509Certificate getCertificate(File file)
            throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(file), pwdPFX);        
        
        String alias = ks.aliases().nextElement();
        return (X509Certificate) ks.getCertificate(alias);
        
    }

    /**
     * Get a private key through a pfx file
     *
     * @param file
     * @return
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     */
    public static PrivateKey getPrivateKey(File file)
            throws KeyStoreException,
            IOException,
            CertificateException,
            NoSuchAlgorithmException,
            UnrecoverableKeyException {
    	
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(file), pwdPFX);
        String alias = ks.aliases().nextElement();

        return (PrivateKey) ks.getKey(alias, pwdPFX);
    }

    /**
     * Get XML response through SAT's web service and extract token from it
     *
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws CertificateEncodingException
     */
    public static String getToken()
            throws IOException,
            NoSuchAlgorithmException,
            SignatureException,
            InvalidKeyException,
            CertificateEncodingException {
        Authentication authentication = new Authentication(urlAutentica, urlAutenticaAction);
        authentication.generate(certificate, privateKey);

        return authentication.send(null);
    }

    /**
     * Get XML response through SAT's web service and extract idRequest from it
     *
     * @param token
     * @return
     * @throws Exception 
     */
    public static String getRequest(String token)
            throws Exception {
        Request request = new Request(urlSolicitud, urlSolicitudRecibidosAction);
        request.setTypeRequest("CFDI");

        // Send empty in rfcEmisor if you want to get receiver packages
        // or send empty in rfcReceptor if you want to get sender packages
        request.generateReceived(certificate, privateKey, rfc, rfc, rfc, dateStart, dateEnd,special);

        return request.send(token);
    }

    /**
     * Get XML response through SAT's web service and extract idPackages from it
     *
     * @param token
     * @param idRequest
     * @return
     * @throws CertificateEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws IOException
     */
    public static String getVerifyRequest(String token, String idRequest)
            throws CertificateEncodingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException,
            IOException {
        VerifyRequest verifyRequest = new VerifyRequest(urlVerificarSolicitud, urlVerificarSolicitudAction);
        verifyRequest.generate(certificate, privateKey, idRequest, rfc);

        return verifyRequest.send(token);
    }

    /**
     * Get XML response through SAT's web service and extract Base64's package from it
     *
     * @param token
     * @param idPackage
     * @return
     * @throws IOException
     * @throws CertificateEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static String getDownload(String token, String idPackage)
            throws IOException,
            CertificateEncodingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException {
        Download download = new Download(urlDescargarSolicitud, urlDescargarSolicitudAction);
        download.generate(certificate, privateKey, rfc, idPackage);

        return download.send(token);
    }

    /**
     * Decodes a URL encoded string using `UTF-8`
     *
     * @param value
     * @return
     */
    public static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
        	log4j.error("Exception" , ex);
            throw new RuntimeException(ex.getCause());
        }
    }
    
    public static String decodeDownload(String packageString) {
    	
    	try {
    	
    	 byte[] byteArray = Base64.decodeBase64(packageString.getBytes());
    	 SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmmss");
    	 String dt = formatter.format(new Date());
    	 // SAVE ZIP FILE TO DISK:
	    	 File file = new File("C:/temp/download_save"+ dt + ".zip");
	    	 OutputStream outputStream = new FileOutputStream(file);
	    	 outputStream.write(byteArray);
	         outputStream.close();
    	
    	}catch(Exception e) {
    		log4j.error("Exception" , e);
    		e.printStackTrace();
    		return null; 
    	}
	
    	return "OK";
    }
    
    
    public static void processZipFileOnly() {
    	
    	try {
    	
    	 // READ ZIP AN PROCESS EACH FILE:
    	 ZipFile zipFile = new ZipFile("C:/temp/download_save.zip");
    	 Enumeration<? extends ZipEntry> entries = zipFile.entries();
    	 List<String> xmlStrArray = new ArrayList<String>();

    	    while(entries.hasMoreElements()){
    	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	        ZipEntry entry = entries.nextElement();
    	        InputStream in = zipFile.getInputStream(entry);
    	        int c;
    	        while ((c = in.read()) != -1)
    	            out.write(c);
    	        in.close();
    	        xmlStrArray.add(new String(out.toByteArray(), Charset.forName("utf-8")));
    	    }
    	    zipFile.close();
    	    
    	    if(xmlStrArray.size() > 0) {
    	    	for(String str : xmlStrArray) {
    	    		String strFixed = takeOffBOM(IOUtils.toInputStream(str,"UTF-8"));
    	    		strFixed = strFixed.replace("?<?xml", "<?xml");
    	    		
    	    		Comprobante c = new Comprobante();
    	    		JAXBContext jaxbContext = JAXBContext.newInstance(Comprobante.class);
    				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    				StringReader reader = new StringReader(strFixed);
    				c = (Comprobante) unmarshaller.unmarshal(reader);
    				
    				log4j.info(c.getTipoDeComprobante());
    				
    				if("I".equals(c.getTipoDeComprobante())) {
    					log4j.info("EMISOR:" + c.getEmisor().getRfc() + " / RECEPTOR: " + c.getReceptor().getRfc() + " / UUID: " + c.getComplemento().getTimbreFiscalDigital().getUUID());
    				}
    	    	}
    	    }
    	
    	
    	}catch(Exception e) {
    		log4j.error("Exception" , e);
    		e.printStackTrace();

    	}
    }
    
    public static String takeOffBOM(InputStream inputStream) throws IOException {
	    BOMInputStream bomInputStream = new BOMInputStream(inputStream);
	    return IOUtils.toString(bomInputStream, "UTF-8");
	}
}
