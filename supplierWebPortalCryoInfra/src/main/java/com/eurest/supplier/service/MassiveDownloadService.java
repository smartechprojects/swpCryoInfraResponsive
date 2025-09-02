package com.eurest.supplier.service;

import java.io.ByteArrayInputStream;
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
import java.nio.file.Files;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.DocumentsDao;
import com.eurest.supplier.dao.InvoiceSATDao;
import com.eurest.supplier.invoiceXml.Complemento;
import com.eurest.supplier.invoiceXml.Comprobante;
import com.eurest.supplier.invoiceXml.Concepto;
import com.eurest.supplier.invoiceXml.Conceptos;
import com.eurest.supplier.invoiceXml.ImpuestosComprobante;
import com.eurest.supplier.invoiceXml.Pagos;
import com.eurest.supplier.invoiceXml.TimbreFiscalDigital;
import com.eurest.supplier.model.CodigosSAT;
import com.eurest.supplier.model.Company;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.InvoiceSAT;
import com.eurest.supplier.model.InvoiceSATRequest;
import com.eurest.supplier.model.NonComplianceSupplier;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.DownloadSATUtils;
import com.eurest.supplier.util.Logger;
import com.eurest.supplier.util.NullValidator;
import com.eurest.supplier.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.smartech.supplier.sat.Authentication;
import com.smartech.supplier.sat.Download;
import com.smartech.supplier.sat.Request;
import com.smartech.supplier.sat.VerifyRequest;

@Service("massiveDownloadService")
public class MassiveDownloadService {

	final static String urlAutentica = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/Autenticacion/Autenticacion.svc";
	final static String urlAutenticaAction = "http://DescargaMasivaTerceros.gob.mx/IAutenticacion/Autentica";

	final static String urlSolicitud = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/SolicitaDescargaService.svc";
    final static String urlSolicitudRecibidosAction = "http://DescargaMasivaTerceros.sat.gob.mx/ISolicitaDescargaService/SolicitaDescargaRecibidos";

	final static String urlVerificarSolicitud = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/VerificaSolicitudDescargaService.svc";
	final static String urlVerificarSolicitudAction = "http://DescargaMasivaTerceros.sat.gob.mx/IVerificaSolicitudDescargaService/VerificaSolicitudDescarga";

	final static String urlDescargarSolicitud = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/DescargaMasivaTercerosService.svc";
	final static String urlDescargarSolicitudAction = "http://DescargaMasivaTerceros.sat.gob.mx/IDescargaMasivaTercerosService/Descargar";

	static char[] pwdPFX = null;
	static String rfc = "";
	static String dateStart = "";
	static String dateEnd = "";
	
    static boolean special = false;

	static X509Certificate certificate = null;
	static PrivateKey privateKey = null;
	
	@Autowired
	DocumentsDao documentsDao;
	
	@Autowired
	InvoiceSATDao invoiceSATDao;
	
	@Autowired 
	FiscalDocumentService fiscalDocumentService;
	
	@Autowired 
	SupplierService supplierService;
	
	@Autowired
	NonComplianceSupplierService nonComplianceSupplierService;
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	JavaMailSender mailSenderObj;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	CodigosSATService codigosSATService;
	
	@Autowired
	Logger logger;
	
	@Autowired
	StringUtils stringUtils;
	
	@Autowired
	CompanyService companyService;
	
	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(MassiveDownloadService.class);
	static WebDriver driver;
	
	@Scheduled(cron = "0 0 21 * * ?")
 	//@Scheduled(fixedDelay = 86400000, initialDelay = 5000)
	public void run() throws Exception {

		boolean downloadEnabled = false;
		UDC enableSATDowwnload = udcService.searchBySystemAndKey("DESCARGASAT", "ENABLED");
		if(enableSATDowwnload != null) {
			if("TRUE".equals(enableSATDowwnload.getStrValue1())){
				downloadEnabled = true;
			}
		}
		
		if(downloadEnabled) {
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();  
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			cal.add(Calendar.DATE, -1);
			dateEnd = dateFormat.format(cal.getTime());
			cal.add(Calendar.DATE, -7);
			dateStart = dateFormat.format(cal.getTime());
			
			/*
			 * CRY800801222
			 *  Hay Exception
			dateStart = "2022-05-11";
			dateEnd = "2022-05-21";
			Fecha:2022-05-18
			UUID=6e6ce10e-2c75-43d2-a8bd-2f4225eb4ff7
			ULTIMINA EJECUCION
			dateStart = "2022-06-11";
			dateEnd = "2022-06-17";
			*/
	
			/*dateStart = "2021-12-18";
			dateEnd = "2021-12-31";*/
			
			List<Company> companies = companyService.searchActiveCompanies("");
			if(companies != null) {
				if(companies.size() > 0) {
					for(Company c : companies) {
						rfc = c.getCompany();
						pwdPFX = c.getSecretPass().toCharArray();
						
						UserDocument doc = documentsDao.searchCriteriaByAddressBookAndType("PKCS12", rfc);
						if(doc != null) {
							InvoiceSATRequest request = null;
							try {
								
								File tempFile = File.createTempFile(rfc, ".pfx", null);
								FileOutputStream fos = new FileOutputStream(tempFile);
								fos.write(doc.getContent());
								fos.close();
								log4j.info(new Date() + " INICIO DE PROCESO COMPAÑÍA: " + rfc + ". Inicio: " + dateStart + "Fin: " + dateEnd);
								logger.log(AppConstants.LOG_MASSUPLOAD, "INICIO DE PROCESO COMPAÑÍA: " + rfc + ". Inicio: " + dateStart + "Fin: " + dateEnd, rfc);
		
								// Get certificate and private key from PFX file
								certificate = getCertificate(tempFile);
								privateKey = getPrivateKey(tempFile);
		
								// Get Token
								String token = "WRAP access_token=\"" + decodeValue(getToken()) + "\"";
		
								// Get idRequest with token obtained
								String idRequest = getRequest(token);
								log4j.info(idRequest);
								
								if("Certificado Caduco".equals(idRequest)) {
									log4j.info( new Date() + " FIN: " + rfc);
									continue;
								}
		
								TimeUnit.SECONDS.sleep(90); // Wait 30 seconds for approval
								
								// Get idPackages with token and idRequest obtained
								String idPackages = getVerifyRequest(token, idRequest);
								
								
							
								request = new InvoiceSATRequest();
								request.setCompany(rfc);
								request.setCreatedDate(new Date());
								request.setDateStart(dateStart);
								request.setDateEnd(dateEnd);
								request.setIdRequest(idRequest);
								request.setLastVerifyDate(new Date());
								request.setStatus("PENDING");
								
								invoiceSATDao.saveDocuments(request);
								
								//DACG
								if (idPackages == null) {
									for (int x = 0; x < 10; x++) {
										TimeUnit.SECONDS.sleep(20);
										idPackages = getVerifyRequest(token, idRequest);
										if (idPackages == null) {
											
											continue;
										} else {	
											if("EXPIRED".equals(idPackages)) {
												request.setStatus("EXPIRED");
												idPackages=null;
											}else {
												request.setStatus("COMPLETE");
											}
											
											break;
										}
									}
									
									log4j.info(idRequest);
									logger.log(AppConstants.LOG_MASSUPLOAD, "TIMEOUT DE PETICION: " + idRequest + ". Inicio: " + dateStart + "Fin: " + dateEnd, rfc);
								}else {
									//request.setStatus("COMPLETE");
									if("EXPIRED".equals(idPackages)) {
										request.setStatus("EXPIRED");
										idPackages=null;
									}else {
										request.setStatus("COMPLETE");
									}
								}
								
								request.setLastVerifyDate(new Date());
								invoiceSATDao.updateDocuments(request);
								
		                        //DACG
								// Get package in Base64 with token and idPackages obtained								
								String packageString = null;
								if (idPackages != null) {
									if (idPackages.contains("Certificado Revocado o Caduco")) {
										log4j.info(rfc + " - Certificado Revocado o Caduco");
										request.setStatus("EXPIRED");
										request.setLastVerifyDate(new Date());
										invoiceSATDao.updateDocuments(request);
									} else {
										log4j.info("packageString:" + rfc);
										packageString = getDownload(token, idPackages);

										if (packageString != null && !"".equals(packageString)) {
											log4j.info("PROCESANDO:" + rfc);
											decodeDownload(packageString);
										}
									}
								}
								
								
								
								/*File file = new File("C:\\Users\\dcabr\\OneDrive\\Escritorio\\download_saveAME0909284I0_20220531032056.zip");
								
								 byte[] fileContent = Files.readAllBytes(file.toPath());
							        String baseFile =  java.util.Base64.getEncoder().encodeToString(fileContent);
							        decodeDownload(baseFile);*/
								
								tempFile.delete();
								
							}catch(Exception e) {
								log4j.error("Exception" , e);
								if(request != null) {
								request.setStatus("PENDING");
								request.setLastVerifyDate(new Date());
								invoiceSATDao.updateDocuments(request);
								}
								e.printStackTrace();
								continue;
							}
						}
						
						log4j.info(new Date() + " FIN: " + rfc);
					}
				}
			}
		}
	}
	
	//@Scheduled(cron = "0 0 21 * * ?")
 	//@Scheduled(fixedDelay = 86400000, initialDelay = 5000)
	@Scheduled(cron = "0 0 9,16 * * ?")
	public void run2() throws Exception {

		boolean downloadEnabled = false;
		UDC enableSATDowwnload = udcService.searchBySystemAndKey("DESCARGASAT", "ENABLED");
		if(enableSATDowwnload != null) {
			if("TRUE".equals(enableSATDowwnload.getStrValue1())){
				downloadEnabled = true;
			}
		}
		
		if(downloadEnabled) {
			Calendar cal = Calendar.getInstance();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateEnd = dateFormat.format(cal.getTime());
			cal.add(Calendar.DATE, -1);
			dateStart = dateFormat.format(cal.getTime());
			int currenthour = cal.get(Calendar.HOUR_OF_DAY);
			special = true;
			
			String currenthourStr = String.valueOf(currenthour);
			
			if(currenthourStr.length()<2) {
				currenthourStr = "0"+currenthourStr;
			}
			
			dateStart = dateStart + "T00:00:00";
			dateEnd = dateEnd + "T" + currenthourStr + ":00:00";
			
			
			List<Company> companies = companyService.searchActiveCompanies("");
			if(companies != null) {
				if(companies.size() > 0) {
					for(Company c : companies) {
						rfc = c.getCompany();
						pwdPFX = c.getSecretPass().toCharArray();
						
						UserDocument doc = documentsDao.searchCriteriaByAddressBookAndType("PKCS12", rfc);
						if(doc != null) {
							InvoiceSATRequest request = null;
							try {
								
								File tempFile = File.createTempFile(rfc, ".pfx", null);
								FileOutputStream fos = new FileOutputStream(tempFile);
								fos.write(doc.getContent());
								fos.close();
								log4j.info(new Date() + " INICIO DE PROCESO COMPAÑÍA: " + rfc + ". Inicio: " + dateStart + "Fin: " + dateEnd);
								logger.log(AppConstants.LOG_MASSUPLOAD, "INICIO DE PROCESO COMPAÑÍA: " + rfc + ". Inicio: " + dateStart + "Fin: " + dateEnd, rfc);
		
								// Get certificate and private key from PFX file
								certificate = getCertificate(tempFile);
								privateKey = getPrivateKey(tempFile);
		
								// Get Token
								String token = "WRAP access_token=\"" + decodeValue(getToken()) + "\"";
		
								// Get idRequest with token obtained
								String idRequest = getRequest(token);
								log4j.info(idRequest);
		
								TimeUnit.SECONDS.sleep(90); // Wait 30 seconds for approval
								
								// Get idPackages with token and idRequest obtained
								String idPackages = getVerifyRequest(token, idRequest);
							
								request = new InvoiceSATRequest();
								request.setCompany(rfc);
								request.setCreatedDate(new Date());
								request.setDateStart(dateStart);
								request.setDateEnd(dateEnd);
								request.setIdRequest(idRequest);
								request.setLastVerifyDate(new Date());
								request.setStatus("PENDING");
								
								invoiceSATDao.saveDocuments(request);
								
								//DACG
								if (idPackages == null) {
									for (int x = 0; x < 10; x++) {
										TimeUnit.SECONDS.sleep(20);
										idPackages = getVerifyRequest(token, idRequest);
										if (idPackages == null) {
											
											continue;
										} else {	
											if("EXPIRED".equals(idPackages)) {
												request.setStatus("EXPIRED");
												idPackages=null;
											}else {
												request.setStatus("COMPLETE");
											}
											
											break;
										}
									}
									
									log4j.info(idRequest);
									logger.log(AppConstants.LOG_MASSUPLOAD, "TIMEOUT DE PETICION: " + idRequest + ". Inicio: " + dateStart + "Fin: " + dateEnd, rfc);
								}else {
									//request.setStatus("COMPLETE");
									if("EXPIRED".equals(idPackages)) {
										request.setStatus("EXPIRED");
										idPackages=null;
									}else {
										request.setStatus("COMPLETE");
									}
								}
								
								request.setLastVerifyDate(new Date());
								invoiceSATDao.updateDocuments(request);
								
		                        //DACG
								// Get package in Base64 with token and idPackages obtained
								String packageString = null;
								if (idPackages != null) {
									if (idPackages.contains("Certificado Revocado o Caduco")) {
										log4j.info(rfc + " - Certificado Revocado o Caduco");
										request.setStatus("EXPIRED");
										request.setLastVerifyDate(new Date());
										invoiceSATDao.updateDocuments(request);
									} else {
										log4j.info("packageString:" + rfc);
										packageString = getDownload(token, idPackages);

										if (packageString != null && !"".equals(packageString)) {
											log4j.info("PROCESANDO:" + rfc);
											decodeDownload(packageString);
										}
									}
								}
								
								/*File file = new File("C:\\Users\\dcabr\\OneDrive\\Escritorio\\download_saveAME0909284I0_20220531032056.zip");
								
								 byte[] fileContent = Files.readAllBytes(file.toPath());
							        String baseFile =  java.util.Base64.getEncoder().encodeToString(fileContent);
							        decodeDownload(baseFile);*/
								
								tempFile.delete();
								
							}catch(Exception e) {
								log4j.error("Exception" , e);
								if(request != null) {
								request.setStatus("PENDING");
								request.setLastVerifyDate(new Date());
								invoiceSATDao.updateDocuments(request);
								}
								e.printStackTrace();
								continue;
							}
						}
						
						log4j.info(new Date() + " FIN: " + rfc);
					}
				}
			}
		}
	}
	
	//@Scheduled(fixedDelay = 8640000, initialDelay = 15000)
	@Scheduled(cron = "0 0/60 11,13,19,21 * * ?")
	public void runVerify() throws Exception {

		boolean downloadEnabled = false;
		UDC enableSATDowwnload = udcService.searchBySystemAndKey("DESCARGASAT", "ENABLED");
		if(enableSATDowwnload != null) {
			if("TRUE".equals(enableSATDowwnload.getStrValue1())){
				downloadEnabled = true;
			}
		}
		
		if(downloadEnabled) {
			
		List<InvoiceSATRequest> requestList = invoiceSATDao.getRequestByStatus("PENDING");			
			if(requestList != null) {
				if(requestList.size() > 0) {
					for(InvoiceSATRequest request : requestList) {
						List<Company> companies = companyService.searchCriteria(request.getCompany(),0,1);
						if(companies != null) {
							if(companies.size() > 0) {
								for(Company c : companies) {
									rfc = c.getCompany();
									pwdPFX = c.getSecretPass().toCharArray();
									UserDocument doc = documentsDao.searchCriteriaByAddressBookAndType("PKCS12", rfc);
									if(doc != null) {
										try {
											
											File tempFile = File.createTempFile(rfc, ".pfx", null);
											FileOutputStream fos = new FileOutputStream(tempFile);
											fos.write(doc.getContent());
											fos.close();
											log4j.info(new Date() + " VERIFICACION: INICIO DE PROCESO COMPAÑÍA: " + rfc + ". Inicio: " + dateStart + "Fin: " + dateEnd + "idRequest: " + request.getIdRequest());
											logger.log(AppConstants.LOG_MASSUPLOAD, "INICIO DE PROCESO COMPAÑÍA: " + rfc + ". Inicio: " + request.getDateStart() + "Fin: " + request.getDateEnd(), rfc);
					
											// Get certificate and private key from PFX file
											certificate = getCertificate(tempFile);
											privateKey = getPrivateKey(tempFile);
					
											String idRequest = request.getIdRequest();
											// Get Token
											String token = "WRAP access_token=\"" + decodeValue(getToken()) + "\"";

											String idPackages = getVerifyRequest(token, idRequest);
											TimeUnit.SECONDS.sleep(90);
											
											//DACG
											if (idPackages == null) {
												for (int x = 0; x < 10; x++) {
													TimeUnit.SECONDS.sleep(90);
													token = "WRAP access_token=\"" + decodeValue(getToken()) + "\"";
													idPackages = getVerifyRequest(token, idRequest);
													if (idPackages == null) {
														continue;
													} else {			
														if("EXPIRED".equals(idPackages)) {
															request.setStatus("EXPIRED");
															idPackages=null;
														}else {
															request.setStatus("COMPLETE");
														}
														
														break;
													}
												}
												log4j.info(idRequest);
												logger.log(AppConstants.LOG_MASSUPLOAD, "TIMEOUT DE PETICION: " + idRequest + ". Inicio: " + dateStart + "Fin: " + dateEnd, rfc);
											}else {
												if("EXPIRED".equals(idPackages)) {
													request.setStatus("EXPIRED");
													idPackages=null;
												}else {
													request.setStatus("COMPLETE");
												}
												
											
											}
											
											
											
											request.setLastVerifyDate(new Date());
											invoiceSATDao.updateDocuments(request);
											
					                        //DACG
											// Get package in Base64 with token and idPackages obtained
											String packageString = null;
											if (idPackages != null) {
												if (idPackages.contains("Certificado Revocado o Caduco")) {
													log4j.info(rfc + " - Certificado Revocado o Caduco");
													request.setStatus("EXPIRED");
													request.setLastVerifyDate(new Date());
													invoiceSATDao.updateDocuments(request);
												} else {
													log4j.info("packageString:" + rfc);
													packageString = getDownload(token, idPackages);

													if (packageString != null && !"".equals(packageString)) {
														log4j.info("PROCESANDO:" + rfc);
														decodeDownload(packageString);
													}
												}
											}
											
											
											tempFile.delete();
											
										}catch(Exception e) {
											log4j.error("Exception" , e);
											if(request !=null) {
											request.setStatus("PENDING");
											request.setLastVerifyDate(new Date());
											invoiceSATDao.updateDocuments(request);
											}
											e.printStackTrace();
											continue;
										}
									}
									log4j.info( new Date() + " FIN: " + rfc);
								}
								
							}
							
						}
					}
					
					
					
				}
				
			}

		}
	}

	
	// @Scheduled(cron = "0 0 21 * * SAT")
	//@Scheduled(fixedDelay = 8640000, initialDelay = 15000)
	public void runTest() throws Exception {
			//processZipFile(rfc);
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
	public X509Certificate getCertificate(File file)
			throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(file), pwdPFX);

		String alias = ks.aliases().nextElement();
		return (X509Certificate) ks.getCertificate(alias);

	}
	
	public X509Certificate getCertificateFromInputStream(InputStream fis)
			throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(fis, pwdPFX);
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
	public PrivateKey getPrivateKey(File file) throws KeyStoreException, IOException, CertificateException,
			NoSuchAlgorithmException, UnrecoverableKeyException {

		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(file), pwdPFX);
		String alias = ks.aliases().nextElement();
		return (PrivateKey) ks.getKey(alias, pwdPFX);
	}
	
	public PrivateKey getPrivateKeyFromInputStream(InputStream fis) throws KeyStoreException, IOException, CertificateException,
	NoSuchAlgorithmException, UnrecoverableKeyException {

		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(fis, pwdPFX);
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
	public String getToken() throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException,
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
	public String getRequest(String token) throws Exception {
		Request request = new Request(urlSolicitud, urlSolicitudRecibidosAction);
		request.setTypeRequest("CFDI");

		// Send empty in rfcEmisor if you want to get receiver packages
		// or send empty in rfcReceptor if you want to get sender packages
		request.generateReceived(certificate, privateKey, "", rfc, rfc, dateStart, dateEnd,special);

		String resultado = request.send(token);
		log4j.info("resultado: " + resultado);
        if (resultado.toString().contains("Certificado Revocado o Caduco")) {
    		//System.out.println("Certificado Revocado o Caduco.. ");	
	    	String altEmail = "";
			List<UDC> udcList =  udcService.searchBySystem("CERTIFICATE");
			if(udcList != null) {
				altEmail = udcList.get(0).getStrValue1();
				EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				emailAsyncSup.setProperties(
				AppConstants.EMAIL_REVOKED_OR_EXPIRED_CERTIFICATE,
				AppConstants.EMAIL_REVOKED_OR_EXPIRED_CERTIFICATE_CONTENT + " - RFC: " + rfc + "<br />",
				altEmail);
				emailAsyncSup.setMailSender(mailSenderObj);
				Thread emailThreadSup = new Thread(emailAsyncSup);
				emailThreadSup.start();
				return "Certificado Caduco";
			}    		
        } else if (resultado.toString().contains("Certificado Inválido") 
        		|| resultado.toString().contains("Certificado Invalido")) {
	    	String altEmail = "";
			List<UDC> udcList =  udcService.searchBySystem("CERTIFICATE");
			if(udcList != null) {
				altEmail = udcList.get(0).getStrValue1();
				EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				emailAsyncSup.setProperties(
				AppConstants.EMAIL_INVALID_CERTIFICATE,
				AppConstants.EMAIL_INVALID_CERTIFICATE_CONTENT + " - RFC: " + rfc + "<br />",
				altEmail);
				emailAsyncSup.setMailSender(mailSenderObj);
				Thread emailThreadSup = new Thread(emailAsyncSup);
				emailThreadSup.start();
				return "Certificado Caduco";
			}
        }		
		//return request.send(token);
        return resultado;
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
	public String getVerifyRequest(String token, String idRequest) throws CertificateEncodingException,
			NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
		VerifyRequest verifyRequest = new VerifyRequest(urlVerificarSolicitud, urlVerificarSolicitudAction);
		verifyRequest.generate(certificate, privateKey, idRequest, rfc);

		return verifyRequest.send(token);
	}

	/**
	 * Get XML response through SAT's web service and extract Base64's package from
	 * it
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
	public String getDownload(String token, String idPackage) throws IOException, CertificateEncodingException,
			NoSuchAlgorithmException, InvalidKeyException, SignatureException {
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
	public String decodeValue(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			log4j.error("Exception" , ex);
			throw new RuntimeException(ex.getCause());
		}
	}

	public String decodeDownload(String packageString) {

		try {

			byte[] byteArray = Base64.decodeBase64(packageString.getBytes());

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String dt = formatter.format(new Date());
			
			InvoiceSAT invoiceSAT = new InvoiceSAT();
			invoiceSAT.setAccept(true);
			invoiceSAT.setContent(byteArray);
			invoiceSAT.setDescription("Massive Download");
		    invoiceSAT.setFiscalType("DOWNLOAD");
			invoiceSAT.setFolio("");
			invoiceSAT.setName(rfc + "_" + dt + ".zip");
			invoiceSAT.setSize(byteArray.length);
			invoiceSAT.setType("application/zip");
			invoiceSAT.setUploadDate(new Date());
			invoiceSAT.setRfcEmisor("");
			invoiceSAT.setRfcReceptor(rfc);
			
			invoiceSAT.setMoneda("");
			invoiceSAT.setSubtotal("");
			invoiceSAT.setTotal("");
			invoiceSAT.setImpuestos("");
			invoiceSAT.setTipoCambio("");
			invoiceSAT.setDescuento("");
			invoiceSAT.setInvoiceDate("");
			
			invoiceSATDao.saveDocuments(invoiceSAT);

			// SAVE ZIP FILE TO DISK:
			File file = new File("C:/temp/download_save" + rfc + "_" + dt + ".zip");
			OutputStream outputStream = new FileOutputStream(file);
			outputStream.write(byteArray);
			outputStream.close();
			
			processZipFile(rfc, byteArray);
			
			logger.log(AppConstants.LOG_MASSUPLOAD, "DESCARGA MASIVA DEL ZIP : " + rfc + "_" + dt + ".zip", rfc);

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}

		return "OK";
	}

	//@Scheduled(fixedDelay = 8640000, initialDelay = 15000)
	public void processZipFileOnly() {

		try {

			// READ ZIP AN PROCESS EACH FILE:
			ZipFile zipFile = new ZipFile("C:/temp/download_save20220303220902.zip");
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			List<String> xmlStrArray = new ArrayList<String>();

			while (entries.hasMoreElements()) {
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

			if (xmlStrArray.size() > 0) {
				for (String str : xmlStrArray) {
					String strFixed = takeOffBOM(IOUtils.toInputStream(str, "UTF-8"));
					strFixed = strFixed.replace("?<?xml", "<?xml");
					Comprobante c = new Comprobante();
					c = this.getComprobante(strFixed);
					if ("I".equals(c.getTipoDeComprobante()) || "P".equals(c.getTipoDeComprobante())) {
						log4j.info("RECEPTOR:" + c.getReceptor().getRfc() + " / " + c.getReceptor().getNombre());
						log4j.info("EMISOR:" + c.getEmisor().getRfc() + " / " + c.getEmisor().getNombre());
					//	processZipFileOnlyRegister(str, c.getReceptor().getRfc());
					}
				}
			}

		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();

		}
	}

//	public boolean processZipFileOnlyRegister(String str, String company) {
//
//		try {
//			
//						boolean validateCodeEnabled = false;
//						
//						UDC enableValidateCode = udcService.searchBySystemAndKey("VALIDACODIGOSAT", "ENABLED");
//						if(enableValidateCode != null) {
//							if("TRUE".equals(enableValidateCode.getStrValue1())){
//								validateCodeEnabled = true;
//							}
//						}
//						
//						String strFixed = takeOffBOM(IOUtils.toInputStream(str, "UTF-8"));
//						strFixed = strFixed.replace("?<?xml", "<?xml");
//
//						Comprobante c = new Comprobante();
//						c = this.getComprobante(strFixed);
//						if ("I".equals(c.getTipoDeComprobante())) {
//							
//							Supplier s = supplierService.searchByAddressNumberAndCompany(c.getEmisor().getRfc(), company); 
//							
//							//Valida proveedores incumplidos
//							boolean validSupplier = true;
//							if(s != null) {
//								NonComplianceSupplier ncs = this.nonComplianceSupplierService.searchByTaxId(s.getRfc(), 0, 0);
//							    if (ncs != null && (
//							      ncs.getRefDate1().contains("Definitivo") || 
//							      ncs.getRefDate1().contains("Presunto") || 
//							      ncs.getRefDate1().contains("Desvirtuado") || 
//							      ncs.getRefDate2().contains("Definitivo") || 
//							      ncs.getRefDate2().contains("Presunto") || 
//							      ncs.getRefDate2().contains("Desvirtuado") || 
//							      ncs.getStatus().contains("Definitivo") || 
//							      ncs.getStatus().contains("Presunto") || 
//							      ncs.getStatus().contains("Desvirtuado"))) {
//							    	
//							    	validSupplier = false;
//							    	String altEmail = "";
//										List<UDC> udcList =  udcService.searchBySystem("TAXALTEMAIL");
//										if(udcList != null) {
//											altEmail = udcList.get(0).getStrValue1();
//										    EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
//											emailAsyncSup.setProperties(
//													AppConstants.EMAIL_NO_COMPLIANCE_INVOICE,
//													AppConstants.EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER + " RFC: " + s.getAddresNumber() + "<br /> Nombre: " + s.getRazonSocial() + "<br />",
//													altEmail);
//											emailAsyncSup.setMailSender(mailSenderObj);
//											Thread emailThreadSup = new Thread(emailAsyncSup);
//											emailThreadSup.start();
//										}
//							    	
//										if(s.getEmailSupplier() != null && !"".equals(s.getEmailSupplier())) {
//											 EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
//											 emailAsyncSup.setProperties(
//														AppConstants.EMAIL_INVOICE_REJECTED,
//														AppConstants.EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER_NOTIF  + c.getComplemento().getTimbreFiscalDigital().getUUID() + "<br /> <br />",
//														s.getEmailSupplier());
//											emailAsyncSup.setMailSender(mailSenderObj);
//											Thread emailThreadSup = new Thread(emailAsyncSup);
//											emailThreadSup.start();	
//										}
//							    } 
//							}else {
//								validSupplier = false;
//							}
//							
//							if(!validSupplier) {
//								return false;
//							}
//							
//							boolean validCSAT = false;
//							if (validateCodeEnabled) {
//								List<CodigosSAT> cSatList = codigosSATService.searchBySupplier(c.getEmisor().getRfc(), 0, 100, rfc);
//								if(cSatList != null) {
//									List<String> codeList = new ArrayList<String>();
//									for(CodigosSAT cs : cSatList) {
//										codeList.add(cs.getCodigoSAT());
//									}
//									Conceptos cListBase = c.getConceptos();
//									List<Concepto> cList = cListBase.getConcepto();
//									for (Concepto concepto : cList) {
//										if(codeList.contains(concepto.getClaveProdServ())) {
//											validCSAT = true;
//											break;
//										}
//									}
//								}else {
//									validCSAT = false;
//								}
//							}else {
//								validCSAT = true;
//							}
//							
//							if(!validCSAT) {
//								return false;
//							}
//							
//							FiscalDocuments fd = new FiscalDocuments();
//							Set<FiscalDocumentsConcept> detail = new HashSet<FiscalDocumentsConcept>();
//							
//							fd.setAddressNumber(c.getEmisor().getRfc());
//							fd.setAmount(StringUtils.roundDouble(Double.valueOf(c.getTotal()),2));
//							fd.setApprovalStatus(AppConstants.FISCAL_DOC_NEW);
//							fd.setApprovalStep("0");
//							fd.setCurrencyCode(c.getMoneda());
//							fd.setCurrencyMode("D");
//							fd.setCurrentApprover("");
//							fd.setFolio(c.getFolio());
//							fd.setSubtotal(StringUtils.roundDouble(Double.valueOf(c.getSubTotal()),2));
//							fd.setImpuestos(StringUtils.roundDouble(fd.getAmount() - fd.getSubtotal(),2));
//							fd.setInvoiceDate(c.getComplemento().getTimbreFiscalDigital().getFechaTimbrado());
//							fd.setMoneda(c.getMoneda());
//							fd.setRfcEmisor(c.getEmisor().getRfc());
//							fd.setRfcReceptor(c.getReceptor().getRfc());
//							fd.setStatus(AppConstants.FISCAL_DOC_NEW);
//							fd.setCompany(company);
//							if(s != null) {
//								fd.setSupplierName(s.getRazonSocial());
//							}
//							fd.setType(AppConstants.INVOICE_FIELD_UDC);
//							fd.setUuidFactura(c.getComplemento().getTimbreFiscalDigital().getUUID());
//							
//							Conceptos conceptos = c.getConceptos();
//							if(conceptos != null) {
//								List<Concepto> cList = conceptos.getConcepto();
//								for(Concepto o : cList) {
//									FiscalDocumentsConcept fdc = new FiscalDocumentsConcept();
//									fdc.setAddressNumber(fd.getAddressNumber());
//									fdc.setAmount(StringUtils.roundDouble(Double.valueOf(o.getImporte()),2));
//									fdc.setConceptName(o.getDescripcion());
//									fdc.setCode(o.getClaveProdServ());
//									fdc.setQuantity(StringUtils.roundDouble(Double.valueOf(o.getCantidad()),2));
//									fdc.setUnitPrice(StringUtils.roundDouble(Double.valueOf(o.getValorUnitario()),2));
//									fdc.setUom(o.getClaveUnidad());
//									fdc.setFolio(fd.getFolio());
//									fdc.setSerie(fd.getSerie());
//									fdc.setUuid(fd.getUuidFactura());
//									detail.add(fdc);
//								}
//							}
//							
//							fd.setConcepts(detail);
//							fiscalDocumentService.saveFiscalDocuments(fd);
//							
//							List<UserDocument> xmlDocs = documentsDao.searchCriteriaByUuidOnly(fd.getUuidFactura());
//							if(xmlDocs == null || xmlDocs.size() == 0) {
//								UserDocument doc = new UserDocument();
//								doc.setAccept(true);
//								doc.setAddressBook(fd.getRfcEmisor());
//								doc.setContent(str.getBytes());
//								doc.setDescription("Factura Emisor");
//								doc.setDocumentNumber(0);
//								doc.setDocumentType("");
//								doc.setFiscalRef(0);
//								doc.setFiscalType("Factura");
//								doc.setFolio(fd.getFolio());
//								doc.setSerie(fd.getSerie());
//								doc.setUuid(fd.getUuidFactura());
//								doc.setName(fd.getUuidFactura() + ".xml");
//								doc.setSize(str.getBytes().length);
//								doc.setType("text/xml");
//								doc.setUploadDate(new Date());
//								doc.setCompany(company);
//								documentsDao.saveDocuments(doc);
//								
//								/*
//								String msg = AppConstants.OUTSRC_MASSUPLOAD_MSG;
//								msg = msg.replace("_UUID", doc.getUuid());
//								EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
//				   				emailAsyncSup.setProperties(AppConstants.OUTSRC_MASSUPLOAD_SUBJECT, stringUtils.prepareEmailContent(msg), s.getEmailSupplier());
//				   				emailAsyncSup.setMailSender(mailSenderObj);
//				   				Thread emailThreadSup = new Thread(emailAsyncSup);
//				   				emailThreadSup.start();
//				   				*/
//								
//								logger.log(AppConstants.LOG_MASSUPLOAD, "FACTURA : " + fd.getUuidFactura() + ".xml del proveedor " + fd.getRfcEmisor(), rfc);
//							}
//						}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return true;
//	}
	
	@SuppressWarnings("unused")
	public void processZipFile(String rfc, byte[] zipContent) {

		try {
			List<InvoiceSAT> invoiceList = new ArrayList<InvoiceSAT>();
			boolean validateCodeEnabled = false;
			UDC enableValidateCode = udcService.searchBySystemAndKey("VALIDACODIGOSAT", "ENABLED");
			if(enableValidateCode != null) {
				if("TRUE".equals(enableValidateCode.getStrValue1())){
					validateCodeEnabled = true;
				}
			}
			
			List<String> xmlStrArray = new ArrayList<String>();
			ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(zipContent));
			ZipEntry entry = null;
			while ((entry = zipStream.getNextEntry()) != null) {
			    ByteArrayOutputStream out = new ByteArrayOutputStream();

			    byte[] byteBuff = new byte[4096];
			    int bytesRead = 0;
			    while ((bytesRead = zipStream.read(byteBuff)) != -1)
			    {
			        out.write(byteBuff, 0, bytesRead);
			    }
			    out.close();					    
			    //xmlStrArray.add(new String(out.toByteArray(), Charset.forName("utf-8")));
			  
			//}
			
			//DACG
			
			
				String str = new String(out.toByteArray(), Charset.forName("utf-8"));
			//if (xmlStrArray.size() > 0) {
				//for (String str : xmlStrArray) {
				String strFixed = takeOffBOM(IOUtils.toInputStream(str, "UTF-8"));
					strFixed = strFixed.replace("?<?xml", "<?xml");

					Comprobante c = new Comprobante();
					c = this.getComprobante(strFixed);
					if(c != null) {
					if ("I".equals(c.getTipoDeComprobante()) || "P".equals(c.getTipoDeComprobante()) || "E".equals(c.getTipoDeComprobante())) {
											
						List<InvoiceSAT> xmlDocs = invoiceSATDao.searchCriteriaByUuidOnly(c.getComplemento().getTimbreFiscalDigital().getUUID());
						if(xmlDocs == null || xmlDocs.size() == 0) {
							InvoiceSAT doc = new InvoiceSAT();
							doc.setAccept(true);
							
							double total;
							double subtotal;
							double impuestos;
							double descuentos;
							
							if(c.getTotal() == null || c.getTotal()  == ""){
								total = 0;
							}else {
								total = Double.valueOf(c.getTotal());;
							}
							
							if(c.getSubTotal() == null || c.getSubTotal()  == ""){
								subtotal = 0;
							}else {
								subtotal = Double.valueOf(c.getSubTotal());;
							}
							
							if(c.getDescuento() == null || c.getDescuento()  == ""){
								descuentos = 0;
							}else {
								descuentos = Double.valueOf(c.getDescuento());;
							}
							
							impuestos = total - (subtotal+descuentos);
							
							doc.setContent(str.getBytes());
							doc.setDescription("Factura Emisor");
							doc.setFiscalType(AppConstants.INVOICE_SAT);
							doc.setFolio(c.getFolio());
							doc.setSerie(c.getSerie());
							doc.setRfcEmisor(c.getEmisor().getRfc());
							doc.setRfcReceptor(c.getReceptor().getRfc());
							doc.setUuid(c.getComplemento().getTimbreFiscalDigital().getUUID());
							doc.setName(c.getComplemento().getTimbreFiscalDigital().getUUID() + ".xml");
							doc.setSize(str.getBytes().length);
							doc.setType("text/xml");
							doc.setUploadDate(new Date());
							doc.setMoneda(NullValidator.isNull(c.getMoneda()));
							doc.setSubtotal(NullValidator.isNull(c.getSubTotal()));
							doc.setTotal(NullValidator.isNull(c.getTotal()));
							doc.setImpuestos(String.valueOf(impuestos));
							doc.setTipoCambio(NullValidator.isNull(c.getTipoCambio()));
							doc.setDescuento(NullValidator.isNull(c.getDescuento()));
							doc.setInvoiceDate(NullValidator.isNull(c.getComplemento().getTimbreFiscalDigital().getFechaTimbrado()));
							//invoiceSATDao.saveDocuments(doc);
							
							/*
							String msg = AppConstants.OUTSRC_MASSUPLOAD_MSG;
							msg = msg.replace("_UUID", doc.getUuid());
							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			   				emailAsyncSup.setProperties(AppConstants.OUTSRC_MASSUPLOAD_SUBJECT, stringUtils.prepareEmailContent(msg), s.getEmailSupplier());
			   				emailAsyncSup.setMailSender(mailSenderObj);
			   				Thread emailThreadSup = new Thread(emailAsyncSup);
			   				emailThreadSup.start();
			   				*/
							invoiceList.add(doc);
							logger.log(AppConstants.LOG_MASSUPLOAD, "FACTURA : " + c.getComplemento().getTimbreFiscalDigital().getUUID() + ".xml del proveedor " + c.getEmisor().getRfc(), rfc);
							
							if ("I".equals(c.getTipoDeComprobante())) {
								Supplier s = supplierService.searchByAddressNumber(c.getEmisor().getRfc());
								FiscalDocuments fd = new FiscalDocuments();
								Set<FiscalDocumentsConcept> detail = new HashSet<FiscalDocumentsConcept>();
								
								fd.setAddressNumber(c.getEmisor().getRfc());
								fd.setAmount(StringUtils.roundDouble(Double.valueOf(c.getTotal()),2));
								fd.setApprovalStatus(AppConstants.FISCAL_DOC_NEW);
								fd.setApprovalStep("0");
								fd.setCurrencyCode(c.getMoneda());
								fd.setCurrencyMode("D");
								fd.setCurrentApprover("");
								fd.setFolio(c.getFolio());
								fd.setSubtotal(StringUtils.roundDouble(Double.valueOf(c.getSubTotal()),2));
								fd.setImpuestos(StringUtils.roundDouble(fd.getAmount() - fd.getSubtotal(),2));
								fd.setInvoiceDate(c.getComplemento().getTimbreFiscalDigital().getFechaTimbrado());
								fd.setMoneda(c.getMoneda());
								fd.setRfcEmisor(c.getEmisor().getRfc());
								fd.setRfcReceptor(c.getReceptor().getRfc());
								fd.setStatus(AppConstants.FISCAL_DOC_NEW);
								//fd.setCompany(rfc);
								if(s != null) {
									fd.setSupplierName(s.getRazonSocial());
								}
								fd.setType(AppConstants.INVOICE_FIELD_UDC);
								fd.setUuidFactura(c.getComplemento().getTimbreFiscalDigital().getUUID());
								
								Conceptos conceptos = c.getConceptos();
								if(conceptos != null) {
									List<Concepto> cList = conceptos.getConcepto();
									for(Concepto o : cList) {
										FiscalDocumentsConcept fdc = new FiscalDocumentsConcept();
										fdc.setAddressNumber(fd.getAddressNumber());
										fdc.setAmount(StringUtils.roundDouble(Double.valueOf(o.getImporte()),2));
										fdc.setConceptName(o.getDescripcion());
										fdc.setCode(o.getClaveProdServ());
										fdc.setQuantity(StringUtils.roundDouble(Double.valueOf(o.getCantidad()),2));
										fdc.setUnitPrice(StringUtils.roundDouble(Double.valueOf(o.getValorUnitario()),2));
										fdc.setUom(o.getClaveUnidad());
										fdc.setFolio(fd.getFolio());
										fdc.setSerie(fd.getSerie());
										fdc.setUuid(fd.getUuidFactura());
										detail.add(fdc);
									}
								}
								
								fd.setConcepts(detail);
								//fiscalDocumentService.saveFiscalDocuments(fd);
							}
							
						}
					}
					
					
					
					
					}
					  zipStream.closeEntry();
				}
			log4j.info("Guardado Multiple: " + invoiceList.size());
				invoiceSATDao.saveMultiple(invoiceList);
		//	}
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}

	public String takeOffBOM(InputStream inputStream) throws IOException {
		BOMInputStream bomInputStream = new BOMInputStream(inputStream);
		return IOUtils.toString(bomInputStream, "UTF-8");
	}
	
	public Comprobante objectCastV4(com.eurest.supplier.invoiceXml4.Comprobante c4) {
		
		Comprobante c = null;		
		try {
			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.setSerializationInclusion(Include.NON_NULL);
			String jsonInString = jsonMapper.writeValueAsString(c4);
			Gson gson = new Gson();
			c =  gson.fromJson(jsonInString, Comprobante.class);
			//c.getComplemento().getTimbreFiscalDigital().setUUID(c4.getComplemento().getTimbreFiscalDigital().getUUID());
			
			if(c4.getComplementos() != null && !c4.getComplementos().isEmpty()) {
				c.setComplemento(new Complemento());				
				for(com.eurest.supplier.invoiceXml4.Complemento complemento : c4.getComplementos()) {					
					//Timbre Fiscal Digital
					if(complemento.getTimbreFiscalDigital() != null) {
						TimbreFiscalDigital timbreFiscal = (TimbreFiscalDigital)objectCastGson(complemento.getTimbreFiscalDigital(), new TimbreFiscalDigital());
						c.getComplemento().setTimbreFiscalDigital(timbreFiscal);
						c.getComplemento().getTimbreFiscalDigital().setUUID(complemento.getTimbreFiscalDigital().getUUID());//No lo obtiene al convertirlo
					}
				}
			}
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		return c;
	}
	
	public Comprobante getComprobante(String str) {
		try {
			String strFixed = takeOffBOM(IOUtils.toInputStream(str, "UTF-8"));
			strFixed = strFixed.replace("?<?xml", "<?xml");
			Comprobante c = new Comprobante();
			
			if(strFixed.contains(AppConstants.NAMESPACE_CFDI_V4)) {
				com.eurest.supplier.invoiceXml4.Comprobante c4 = new com.eurest.supplier.invoiceXml4.Comprobante();
				JAXBContext jaxbContext = JAXBContext.newInstance(com.eurest.supplier.invoiceXml4.Comprobante.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				StringReader reader = new StringReader(strFixed);
				c4 = (com.eurest.supplier.invoiceXml4.Comprobante) unmarshaller.unmarshal(reader);			
				c = objectCastV4(c4);
			} else {
				JAXBContext jaxbContext = JAXBContext.newInstance(Comprobante.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				StringReader reader = new StringReader(strFixed);
				c = (Comprobante) unmarshaller.unmarshal(reader);
			}
			return c;
		}catch(Exception e) {
			log4j.error(str);
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T objectCastGson(T originalObject, T targetClass) {
		
		Object o = null;		
		try {
			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.setSerializationInclusion(Include.NON_NULL);
			String jsonInString = jsonMapper.writeValueAsString(originalObject);
			Gson gson = new Gson();
			o =  gson.fromJson(jsonInString, targetClass.getClass());
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			o = null;
		}
		return (T)o;
	}
	
	public InvoiceSAT getDownloadCFDIByUuid(String rfc, String folioFiscal) {
		int chromeDriverProcessID = 0;
		int chromeProcesID = 0;
		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString();
		String downloadDirectory = "D:\\DownloadCFDIByUuid";
		String chromeDriverDirectory = "D:\\DownloadCFDIByUuid\\chromedriver.exe";
		byte[]  content = null;
		InvoiceSAT invoiceSAT=null;
		String statusComp = null;
 	    String statusCancel = null;
				
		log4j.info("getDownloadCFDIByUuid: RFC: " + rfc + " - UUID: " + folioFiscal);
		try {
			log4j.info("Inicio: " + new Date());
			UserDocument docCert = documentsDao.searchCriteriaByAddressBookAndType("CERT", rfc);
			UserDocument doceKey = documentsDao.searchCriteriaByAddressBookAndType("KEY", rfc);
			Company company = companyService.searchByCompany(rfc);
			
			if(docCert == null || doceKey ==null || company ==null) {
				log4j.error("Error el obtener información de la compañia " + rfc);
				 return invoiceSAT;
			}
			
			File tempFileCert = File.createTempFile(rfc, ".cer", null);
			FileOutputStream fosCert = new FileOutputStream(tempFileCert);
			fosCert.write(docCert.getContent());
			fosCert.close();

			File tempFileKey = File.createTempFile(rfc, ".key", null);
			FileOutputStream fosKey = new FileOutputStream(tempFileKey);
			fosKey.write(doceKey.getContent());
			fosKey.close();

			System.setProperty("webdriver.chrome.driver", chromeDriverDirectory);

			Map<String, Object> prefs = new HashMap<String, Object>();

			prefs.put("download.prompt_for_download", false);
			prefs.put("download.extensions_to_open", "application/xml");
			prefs.put("safebrowsing.enabled", true);
			// indicamos que la notificaciones se bloqueen
			prefs.put("profile.default_content_setting.values.notifications", 2);
			// indicamos que el navegador llame al directorio que hemos indicado
			prefs.put("download.default_directory", downloadDirectory);

			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", prefs);
			options.addArguments("disable-infobars");
			options.addArguments("--app=https://portalcfdi.facturaelectronica.sat.gob.mx/");
			options.addArguments("--window-size=900,900");
			options.addArguments("--window-position=-2000,0");
			options.addArguments("--safebrowsing-disable-download-protection");
			options.addArguments("safebrowsing-disable-extension-blacklist");
			// options.addArguments("--user-data-dir=D:\\temp\\ChromeProfiles\\" +
			// uuidAsString); // Custom directory path for first profile
			options.setAcceptInsecureCerts(true);

			ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
            int port = chromeDriverService.getUrl().getPort();
            driver = new ChromeDriver(chromeDriverService, options);
            
            System.out.println("starting chromedriver on port " + port);
            chromeDriverProcessID = DownloadSATUtils.GetChromeDriverProcessID(port);
            System.out.println("detected chromedriver process id " + chromeDriverProcessID);
            chromeProcesID = DownloadSATUtils.GetChromeProcesID(chromeDriverProcessID);
            System.out.println("detected chrome process id " + chromeProcesID);
   
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                     
            String elementsSAT = driver.getPageSource();
            WebDriverWait wait = new WebDriverWait(driver, 10);

           driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
           WebElement elementHButtonFiel = driver.findElement(By.className("pull-right"));
            
            //WebElement elementHButtonFiel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pull-right")));
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            WebElement buttonFiel = elementHButtonFiel.findElement(By.id("buttonFiel"));
            buttonFiel.click();
            
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            List<WebElement> elementInputs = driver.findElements(By.className("input-group"));
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            WebElement elementInputCer = elementInputs.get(0).findElement(By.id("fileCertificate"));
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            elementInputCer.sendKeys(tempFileCert.getAbsolutePath());
            
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            WebElement elementInputKey = elementInputs.get(1).findElement(By.id("filePrivateKey"));
            elementInputKey.sendKeys(tempFileKey.getAbsolutePath());
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            WebElement elementPrivateKeyPassword = driver.findElement(By.id("privateKeyPassword"));
            elementPrivateKeyPassword.sendKeys(company.getSecretPass());
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            WebElement elementButtonFiel = driver.findElement(By.className("pull-right"));
            WebElement buttonSubmitFiel = elementButtonFiel.findElement(By.id("submit"));
            
            buttonSubmitFiel.click();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            List<WebElement> elementRadio = driver.findElements(By.className("radio"));
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            elementRadio.get(1).findElement(By.xpath("//a[@href='ConsultaReceptor.aspx']")).click();
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            
            WebElement elementInputFolio= driver.findElement(By.id("ctl00_MainContent_PnlConsulta"));
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            WebElement inputFolio = elementInputFolio.findElement(By.id("ctl00_MainContent_TxtUUID"));
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            inputFolio.sendKeys(Keys.HOME + folioFiscal);
            //inputFolio.sendKeys("54716ADA053A439DB0CA7A213D9B563B");
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            WebElement elementButtonBusqueda = driver.findElement(By.className("clearfix"));
            WebElement buttonSubmitBusqueda= elementButtonBusqueda.findElement(By.id("ctl00_MainContent_BtnBusqueda"));
            buttonSubmitBusqueda.click();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

            wait = new WebDriverWait(driver, 10);
            WebElement elementTablesResult = wait.until(ExpectedConditions.elementToBeClickable(By.id("ctl00_MainContent_tblResult")));

          List<WebElement> elementResultCFDITD = elementTablesResult.findElements((By.tagName("td")));
           
           if (elementResultCFDITD.size() == 0)
           {
        	   log4j.info("No se encontró el folio en SAT " + folioFiscal);
        	   return invoiceSAT;
           }else {
        	   int x= 0;
        	   
        	   statusComp = elementResultCFDITD.get(19).getText();
        	   statusCancel = elementResultCFDITD.get(18).getText();
        	   log4j.info(folioFiscal + " - Estado del Comprobante: " + statusComp);
        	   log4j.info(folioFiscal + " - Estatus de Cancelación: " + statusCancel);
        	   for(WebElement element : elementResultCFDITD) {
        		/*   System.out.println(x);
        		   System.out.println("element:" + element);
        		   System.out.println("element.getText():" + element.getText());
        		   System.out.println("element.getTagName():" + element.getTagName());
        		   System.out.println("element.toString():" + element.toString());
        		   System.out.println("---------------------");
        		   x = x+1;*/
        		   driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        		 WebElement btnDescarga =  element.findElement(By.id("BtnDescarga"));
        		   if(btnDescarga != null) {
        			 /*  System.out.println("btnDescarga");
        			   System.out.println("element:" + element);
            		   System.out.println("element.getText():" + element.getText());
            		   System.out.println("element.getTagName():" + element.getTagName());
            		   System.out.println("element.toString():" + element.toString());*/
        			   
                	   
        			   if("Vigente".equals(statusComp)) {
            		   btnDescarga.click();
        			   }
            		   
            		  // driver.switchTo().activeElement();
            		   driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);		   
            		   ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
            		 //  driver.switchTo().window(tabs2.get(1));
            		  //driver.navigate().refresh();
            		   try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						log4j.error("InterruptedException" , e);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		    break;
        		   }
        	   }
        	  
        	   
           }
            
           WebElement elementUserCredencials = driver.findElement(By.className("user-credencials"));
           WebElement cerrarSesion= elementUserCredencials.findElement(By.id("anchorClose"));
           JavascriptExecutor executor = (JavascriptExecutor)driver;
           executor.executeScript("arguments[0].click();", cerrarSesion);
           //SessionId s = ((RemoteWebDriver) driver).getSessionId();
           //System.out.println("Session Id is: " + s);
           //browser close
           
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.close();
            driver.quit();
            tempFileCert.delete();
            tempFileKey.delete();
            
            if("Vigente".equals(statusComp)) {
            content =  searchCFDIPath(downloadDirectory, folioFiscal);
            
            if(content == null) {
            	return invoiceSAT;
            }
            
            invoiceSAT = saveCFDIDownload(content);
            }
            

		}catch(Exception ex) {
			log4j.error("Exception" , ex);
			 ex.printStackTrace();
		}finally {
			log4j.info("Fin: " + new Date());
            try {
            	
				Runtime.getRuntime().exec("taskkill /F /PID " + chromeProcesID);
				Runtime.getRuntime().exec("taskkill /F /PID " + chromeDriverProcessID);
				
			} catch (Exception e) {
				log4j.error("Exception" , e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return invoiceSAT;
	}
	
	public byte[] searchCFDIPath(String path, String folioFiscal) {
		
		File cfdi = new File(path+"\\"+folioFiscal+".xml");
	    byte[] content = null;
		if(cfdi.exists()) {
			  try {
				  content = Files.readAllBytes(cfdi.toPath());
			} catch (IOException e) {
				log4j.error("IOException",e);
				e.printStackTrace();
			}

		}
		
		return content;
	    
	}
	
	public InvoiceSAT saveCFDIDownload(byte[] content) {

		String str = new String(content, Charset.forName("utf-8"));
		String strFixed = null;
		InvoiceSAT doc = null;
		try {
			strFixed = takeOffBOM(IOUtils.toInputStream(str, "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			strFixed = strFixed.replace("?<?xml", "<?xml");

			Comprobante c = new Comprobante();
			c = this.getComprobante(strFixed);
			
			if(c != null) {
				if ("I".equals(c.getTipoDeComprobante()) || "P".equals(c.getTipoDeComprobante()) || "E".equals(c.getTipoDeComprobante())) {
										
					List<InvoiceSAT> xmlDocs = invoiceSATDao.searchCriteriaByUuidOnly(c.getComplemento().getTimbreFiscalDigital().getUUID());
					if(xmlDocs == null || xmlDocs.size() == 0) {
						doc = new InvoiceSAT();
						doc.setAccept(true);
						
						double total;
						double subtotal;
						double impuestos;
						double descuentos;
						
						if(c.getTotal() == null || c.getTotal()  == ""){
							total = 0;
						}else {
							total = Double.valueOf(c.getTotal());;
						}
						
						if(c.getSubTotal() == null || c.getSubTotal()  == ""){
							subtotal = 0;
						}else {
							subtotal = Double.valueOf(c.getSubTotal());;
						}
						
						if(c.getDescuento() == null || c.getDescuento()  == ""){
							descuentos = 0;
						}else {
							descuentos = Double.valueOf(c.getDescuento());;
						}
						
						impuestos = total - (subtotal+descuentos);
						
						doc.setContent(str.getBytes());
						doc.setDescription("Factura Emisor");
						doc.setFiscalType(AppConstants.INVOICE_SAT);
						doc.setFolio(c.getFolio());
						doc.setSerie(c.getSerie());
						doc.setRfcEmisor(c.getEmisor().getRfc());
						doc.setRfcReceptor(c.getReceptor().getRfc());
						doc.setUuid(c.getComplemento().getTimbreFiscalDigital().getUUID());
						doc.setName(c.getComplemento().getTimbreFiscalDigital().getUUID() + ".xml");
						doc.setSize(str.getBytes().length);
						doc.setType("text/xml");
						doc.setUploadDate(new Date());
						doc.setMoneda(NullValidator.isNull(c.getMoneda()));
						doc.setSubtotal(NullValidator.isNull(c.getSubTotal()));
						doc.setTotal(NullValidator.isNull(c.getTotal()));
						doc.setImpuestos(String.valueOf(impuestos));
						doc.setTipoCambio(NullValidator.isNull(c.getTipoCambio()));
						doc.setDescuento(NullValidator.isNull(c.getDescuento()));
						doc.setInvoiceDate(NullValidator.isNull(c.getComplemento().getTimbreFiscalDigital().getFechaTimbrado()));
						invoiceSATDao.saveDocuments(doc);

					//	invoiceList.add(doc);
						log4j.info("Se guarda factura con UUID " + c.getComplemento().getTimbreFiscalDigital().getUUID());
					}
				}
		}
			return doc;
			
			
	}
	
	
}
