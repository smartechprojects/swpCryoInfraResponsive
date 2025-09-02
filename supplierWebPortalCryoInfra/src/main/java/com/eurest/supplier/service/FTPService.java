package com.eurest.supplier.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.Logger;
import com.eurest.supplier.util.SFTPClient;

@Service("ftpService")
public class FTPService implements Runnable{
	@Autowired
	XmlToPojoService xmlToPojoService;
	@Autowired
	PurchaseOrderService purchaseOrderService;
	@Autowired
	DocumentsService documentsService;
	@Autowired
	UdcService udcService;
	Logger logger;
	
	String supplierNumber;
	
	String host = ""; 
	String user = "";
	String pass = ""; 
	int port = 0;
	
	private static org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(FTPService.class);

	//@Scheduled(cron="0 0 22 * * MON-FRI")
	public void execute() {
	
		FTPClient ftpClient = new FTPClient();

		try {

			ftpClient.connect(this.host, this.port);
			showServerReply(ftpClient);

			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				log4j.error("Connect failed");
			}

			boolean success = ftpClient.login(this.user, this.pass);
			showServerReply(ftpClient);

			if (!success) {
				log4j.error("Could not login to the server");
			}

			// Changes working directory
			success = ftpClient.changeWorkingDirectory(AppConstants.FTP_FILEPATH);

			FTPFile[] files = ftpClient.listFiles();
			List<String> invList = new ArrayList<String>();
			List<String> cnList = new ArrayList<String>();
			List<String> pmtList = new ArrayList<String>();

			UDC invFileNameUdc = udcService.searchBySystemAndKey("FTP", "INVPREFIX");
			UDC cnFileNameUdc = udcService.searchBySystemAndKey("FTP", "CNPREFIX");
			UDC pmtFileNameUdc = udcService.searchBySystemAndKey("FTP", "PMTPREFIX");
			UDC supplierUdc = udcService.searchBySystemAndKey("FTP", "SUPPLIER");

			String invFilePrefix = invFileNameUdc.getStrValue1();
			String cnFilePrefix = cnFileNameUdc.getStrValue1();
			String pmtFilePrefix = pmtFileNameUdc.getStrValue1();
			supplierNumber = supplierUdc.getStrValue1();
			
			for (FTPFile file : files) {
				String fileName = file.getName();
				if (!file.isDirectory()) {
					if(containsIgnoreCase(fileName,invFilePrefix)) {
						if(containsIgnoreCase(fileName, ".xml")) invList.add(fileName);
					}else if(containsIgnoreCase(fileName,cnFilePrefix)) {
						if(containsIgnoreCase(fileName, ".xml")) cnList.add(fileName);
					}else if(containsIgnoreCase(fileName,pmtFilePrefix)) {
						if(containsIgnoreCase(fileName, ".xml")) pmtList.add(fileName);
					}else {
						String existingFilepath =  fileName;
						String newFilepath = AppConstants.FTP_FILEPATH_OTHERS + fileName;
						success = ftpClient.rename(existingFilepath,newFilepath);
						if (success) {
							log4j.info("Transferido");
						}
					}

				}
			}

			if(invList.size() > 0) {
				processInvoices(invList, ftpClient);
			}

			if(cnList.size() > 0) {
				processCreditNotes(cnList, ftpClient);
			}

			if(pmtList.size() > 0) {
				processPayments(pmtList, ftpClient);
			}

			ftpClient.logout();
			ftpClient.disconnect();

		} catch (IOException ex) {
			log4j.error("Oops! Something wrong happened");
			log4j.error("Exception" , ex);
			ex.printStackTrace();
		}
	}

	private void processInvoices(List<String> invList, FTPClient ftpClient) {

		for(String fileName : invList) {
			boolean success = false;

			try {
		    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				String usr = auth.getName();
				
				String fileContent = getXmlContentString(ftpClient, fileName);
				if(fileContent != null && containsIgnoreCase(fileName,".xml")) {
					InvoiceDTO dto = getInvoiceXml(fileContent);
					if(dto != null){
						if(!"".equals(dto.getFolio())){
							int orderNumber = Integer.valueOf("30" + dto.getFolio());
							PurchaseOrder po = purchaseOrderService.getOrderByOrderAndAddresBook(orderNumber, 
									supplierNumber, 
									AppConstants.DOCTYPE_PROD);
							if(po != null) {
								String res = documentsService.validateInvoiceFromOrder(dto,supplierNumber, 
										orderNumber, 
										AppConstants.DOCTYPE_PROD,
										"Factura",
										po,
										false,
										fileContent,
										"",
										false,
										"",
										usr);

								log4j.info("*********** " + res);
								if("".equals(res) || res.contains("DOC:")){

									String invPdfName = "";
									
									if(fileName.contains(".xml")) {
										invPdfName = fileName.replace(".xml", ".pdf");
									}else if(fileName.contains(".XML")) {
										invPdfName = fileName.replace(".XML", ".PDF");
									}else if(fileName.contains(".Xml")) {
										invPdfName = fileName.replace(".Xml", ".Pdf");
									}
									
									/*
									UserDocument doc = new UserDocument(); 
									doc.setAddressBook(po.getAddressNumber());
									doc.setDocumentNumber(orderNumber);
									doc.setDocumentType(AppConstants.DOCTYPE_PROD);
									doc.setContent(fileContent.getBytes());
									doc.setName("FAC_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + ".xml");
									doc.setSize(fileContent.length());
									doc.setStatus(true);
									doc.setAccept(true);
									doc.setFiscalType("Factura");
									doc.setType("text/plain");
									doc.setFolio(dto.getFolio());
									doc.setSerie(dto.getSerie());
									doc.setUuid(dto.getUuid());
									doc.setUploadDate(new Date());
									doc.setFiscalRef(0);
									documentsService.save(doc, new Date(), "");

									byte[] pdfFile = getPdfContentString(ftpClient, invPdfName);
									if(pdfFile != null) {
										doc = new UserDocument(); 
										doc.setAddressBook(po.getAddressNumber());
										doc.setDocumentNumber(orderNumber);
										doc.setDocumentType(AppConstants.DOCTYPE_PROD);
										doc.setContent(pdfFile);
										doc.setName("FAC_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + ".pdf");
										doc.setSize(pdfFile.length);
										doc.setStatus(true);
										doc.setAccept(true);
										doc.setFiscalType("Factura");
										doc.setType("application/pdf");
										doc.setFolio(dto.getFolio());
										doc.setSerie(dto.getSerie());
										doc.setUuid(dto.getUuid());
										doc.setUploadDate(new Date());
										doc.setFiscalRef(0);
										documentsService.save(doc, new Date(), "");
									}

									po.setInvoiceUuid(dto.getUuid());
									po.setInvoiceNumber(dto.getFolio() + "");
									po.setPaymentType(dto.getMetodoPago());
									po.setStatus(AppConstants.STATUS_OC_INVOICED);
									po.setOrderStauts(AppConstants.STATUS_OC_INVOICED);
									purchaseOrderService.updateOrders(po);

									String existingFilepath =  fileName;
									String newFilepath = AppConstants.FTP_FILEPATH_COMPLETE + fileName;
									success = ftpClient.rename(existingFilepath,newFilepath);
									if (success) {
										System.out.println("Transferido");
									}

									existingFilepath =  invPdfName;
									newFilepath = AppConstants.FTP_FILEPATH_COMPLETE + invPdfName;
									success = ftpClient.rename(existingFilepath,newFilepath);
									if (success) {
										System.out.println("Transferido");
									}

                                      */
								}else {
									logger.log(AppConstants.LOG_FTP_PROCESS, "ERROR:" + fileName + ":" + res);
									String existingFilepath =  fileName;
									String invPdfName = fileName.replace("xml", "pdf");
									String newFilepath = AppConstants.FTP_FILEPATH_ERROR + fileName;
									success = ftpClient.rename(existingFilepath,newFilepath);
									if (success) {
										log4j.info("Transferido a error XML");
										existingFilepath =  invPdfName;
										newFilepath = AppConstants.FTP_FILEPATH_ERROR + invPdfName;
										success = ftpClient.rename(existingFilepath,newFilepath);
										if (success) {
											log4j.info("Transferido a error pdf");
											logger.log(AppConstants.LOG_FTP_PROCESS, "ERROR:" + invPdfName + ":" + res);
										}
										
									}
								}
							}else {
								logger.log(AppConstants.LOG_FTP_PROCESS, "WARNING:" + fileName + ":" + "No tiene asociada una orden de compra");
							}
						}

					}

				}
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();	
			}
		}

	}

	private void processCreditNotes(List<String> invList, FTPClient ftpClient) {

		for(String fileName : invList) {
			boolean success = false;

			try {
				String fileContent = getXmlContentString(ftpClient, fileName);
				if(fileContent != null) {
					InvoiceDTO dto = getInvoiceXml(fileContent);
					if(dto != null && containsIgnoreCase(fileName,".xml")) {
						String uuid = dto.cfdiRelacionado;
						if(uuid != null && !"".equals(uuid)) {
							PurchaseOrder po = purchaseOrderService.searchbyOrderUuid(uuid);
							if(po != null) {
								double total = dto.getTotal();
								po.setRelievedAmount(total);
								purchaseOrderService.updateOrders(po);

								String invPdfName ="";
								
								if(fileName.contains(".xml")) {
									invPdfName = fileName.replace(".xml", ".pdf");
								}else if(fileName.contains(".XML")) {
									invPdfName = fileName.replace(".XML", ".PDF");
								}else if(fileName.contains(".Xml")) {
									invPdfName = fileName.replace(".Xml", ".Pdf");
								}
								
								/*

								UserDocument doc = new UserDocument(); 
								doc.setAddressBook(po.getAddressNumber());
								doc.setDocumentNumber(po.getOrderNumber());
								doc.setDocumentType(po.getOrderType());
								doc.setContent(fileContent.getBytes());
								doc.setName("NCR_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + ".xml");
								doc.setSize(fileContent.length());
								doc.setStatus(true);
								doc.setAccept(true);
								doc.setFiscalType("Factura");
								doc.setType("text/plain");
								doc.setFolio(dto.getFolio());
								doc.setSerie(dto.getSerie());
								doc.setUuid(dto.getUuid());
								doc.setUploadDate(new Date());
								doc.setFiscalRef(0);
								documentsService.save(doc, new Date(), "");

								byte[] pdfFile = getPdfContentString(ftpClient, invPdfName);
								if(pdfFile != null) {
									doc = new UserDocument(); 
									doc.setAddressBook(po.getAddressNumber());
									doc.setDocumentNumber(po.getOrderNumber());
									doc.setDocumentType(po.getOrderType());
									doc.setContent(pdfFile);
									doc.setName("NCR_OC_" + po.getOrderNumber() + "_" + po.getOrderType() + ".pdf");
									doc.setSize(pdfFile.length);
									doc.setStatus(true);
									doc.setAccept(true);
									doc.setFiscalType("Factura");
									doc.setType("application/pdf");
									doc.setFolio(dto.getFolio());
									doc.setSerie(dto.getSerie());
									doc.setUuid(dto.getUuid());
									doc.setUploadDate(new Date());
									doc.setFiscalRef(0);
									documentsService.save(doc, new Date(), "");
								}

								String existingFilepath =  fileName;
								String newFilepath = AppConstants.FTP_FILEPATH_COMPLETE + fileName;
								success = ftpClient.rename(existingFilepath,newFilepath);
								if (success) {
									System.out.println("Transferido");
								}

								existingFilepath =  invPdfName;
								newFilepath = AppConstants.FTP_FILEPATH_COMPLETE + invPdfName;
								success = ftpClient.rename(existingFilepath,newFilepath);
								if (success) {
									System.out.println("Transferido");
								}
								
								*/

							}else {
								logger.log(AppConstants.LOG_FTP_PROCESS, "WARNING:" + fileName + ":" + "No tiene asociada una factura asociada");	
							}
						}
					}
				}
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();	
			}
		}

	}


	private void processPayments(List<String> invList, FTPClient ftpClient) {

		for(String fileName : invList) {
			boolean success = false;

			try {
				String fileContent = getXmlContentString(ftpClient, fileName);
				InvoiceDTO dto = getInvoiceXml(fileContent);

				if(dto != null){
					if(!"".equals(dto.getFolio())){
						String res = documentsService.validaComplementoPago(fileContent, supplierNumber, dto);
						if("".equals(res) ){

							/*
							String existingFilepath =  fileName;
							String newFilepath = AppConstants.FTP_FILEPATH_COMPLETE + fileName;
							success = ftpClient.rename(existingFilepath,newFilepath);
							if (success) {
								System.out.println("Transferido");
							}
						}else {
							logger.log(AppConstants.LOG_FTP_PROCESS, "ERROR:" + fileName + ":" + res);
							String existingFilepath =  fileName;
							String newFilepath = AppConstants.FTP_FILEPATH_ERROR + fileName;
							success = ftpClient.rename(existingFilepath,newFilepath);
							if (success) {
								System.out.println("Transferido");
							}
							*/
						}
						
					}

				}
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();	
			}
		}

	}

	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				log4j.info("SERVER: " + aReply);
			}
		}
	}

	public boolean containsIgnoreCase(String str, String subString) {
		return str.toLowerCase().contains(subString.toLowerCase());
	}

	public InvoiceDTO getInvoiceXml(String uploadItem){
		try{
			ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getBytes());
			String xmlContent = IOUtils.toString(stream, "UTF-8");
			xmlContent = takeOffBOM(IOUtils.toInputStream(xmlContent, "UTF-8"));
			xmlContent = xmlContent.replace("?<?xml", "<?xml");
			InvoiceDTO dto = null;
			
			if(xmlContent.contains(AppConstants.NAMESPACE_CFDI_V4)) {
				dto = xmlToPojoService.convertV4(xmlContent);
			} else {
				dto = xmlToPojoService.convert(xmlContent);
			}
			return dto;
		}catch(Exception e){
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}
	} 

	private String getXmlContentString(FTPClient ftpClient, String fileName) {
		try {

			InputStream is = ftpClient.retrieveFileStream(fileName);

			if(is == null) {
				return null;
			}

			Scanner sc = new Scanner(is);
			StringBuffer sb = new StringBuffer();
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine());
			}
			boolean success = ftpClient.completePendingCommand();
			if (success) {
				log4j.info("File has been downloaded successfully.");
			}
			sc.close();
			is.close();
			return sb.toString();
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}
	}

	private byte[] getPdfContentString(FTPClient ftpClient, String fileName) {
		try {

			InputStream fis = ftpClient.retrieveFileStream(fileName);

			if(fis == null) {
				return null;
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];

			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum); 
				log4j.info("read " + readNum + " bytes,");
			}
			byte[] bytes = bos.toByteArray();

			boolean success = ftpClient.completePendingCommand();
			if (success) {
				log4j.info("File has been downloaded successfully.");
			}
			bos.close();
			fis.close();
			return bytes;
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void run() {
		execute();
	}

	public void setServices(XmlToPojoService xmlToPojoService,
			PurchaseOrderService purchaseOrderService,
			DocumentsService documentsService,
			UdcService udcService,
			Logger logger) {

		this.xmlToPojoService = xmlToPojoService;
		this.purchaseOrderService = purchaseOrderService;
		this.documentsService = documentsService;
		this.udcService = udcService;
		this.logger = logger;
	}
	
	public void setFtpParams(String host, String user, String pass, int port) {
		this.host = host;
		this.user = user;
		this.pass = pass;
		this.port = port;
	}
	
	public void sendToSftpServer(String xmlString, String targetFileName) {
		sendToSftpServer(xmlString.getBytes(), targetFileName);
	}
	
	public void sendToSftpServer(byte[] document, String targetFileName) {
		
		if(true) {
			return;
		}
		SFTPClient sftpclient=new SFTPClient(udcService.searchBySystemAndKey("SFTP", "REMOTE_HOST").getStrValue1(),
											 udcService.searchBySystemAndKey("SFTP", "USERNAME").getStrValue1(),
											 udcService.searchBySystemAndKey("SFTP", "STRINGKEY").getNote());
		
		sftpclient.setKnownHosts(udcService.searchBySystemAndKey("SFTP", "KNOWNHOSTS").getNote());
		sftpclient.conectar();
		sftpclient.enviar(document,
						  udcService.searchBySystemAndKey("SFTP", "PATH_REMOTE").getStrValue1(),
						  targetFileName);
		
		
	}
		public void EliminarByUUid(ArrayList<String> uuids) {
		
			
		SFTPClient sftpclient=new SFTPClient(udcService.searchBySystemAndKey("SFTP", "REMOTE_HOST").getStrValue1(),
											 udcService.searchBySystemAndKey("SFTP", "USERNAME").getStrValue1(),
											 udcService.searchBySystemAndKey("SFTP", "STRINGKEY").getNote());
		sftpclient.setLogger(logger);
		sftpclient.setKnownHosts(udcService.searchBySystemAndKey("SFTP", "KNOWNHOSTS").getNote());
	sftpclient.conectar();
		
		sftpclient.EliminarByUUid(uuids,
						  udcService.searchBySystemAndKey("SFTP", "PATH_REMOTE").getStrValue1()
						  );
		
	}

		public void sendToSftpServer(File convFile, String targetFileName) {
		
			try {
				sendToSftpServer(new FileInputStream(convFile), targetFileName);
			} catch (FileNotFoundException e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
			}
		}
		public static String takeOffBOM(InputStream inputStream) throws IOException {
		    BOMInputStream bomInputStream = new BOMInputStream(inputStream);
		    return IOUtils.toString(bomInputStream, "UTF-8");
		}
		
		
		
		public void sendToSftpServer(InputStream document, String targetFileName) {
			
			if(true) {
				return;
			}
		SFTPClient sftpclient=new SFTPClient(udcService.searchBySystemAndKey("SFTP", "REMOTE_HOST").getStrValue1(),
											 udcService.searchBySystemAndKey("SFTP", "USERNAME").getStrValue1(),
											 udcService.searchBySystemAndKey("SFTP", "STRINGKEY").getNote());
		sftpclient.setLogger(logger);
		sftpclient.setKnownHosts(udcService.searchBySystemAndKey("SFTP", "KNOWNHOSTS").getNote());
	sftpclient.conectar();
		
		sftpclient.enviar(document,
						  udcService.searchBySystemAndKey("SFTP", "PATH_REMOTE").getStrValue1(),
						  targetFileName);
		
	}
}

