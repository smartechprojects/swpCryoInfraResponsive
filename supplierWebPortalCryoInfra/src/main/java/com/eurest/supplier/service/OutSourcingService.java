package com.eurest.supplier.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eurest.supplier.dao.CDDDCDao;
import com.eurest.supplier.dao.CDDDCEmployeeDao;
import com.eurest.supplier.dao.DocumentsDao;
import com.eurest.supplier.dao.OutSourcingDao;
import com.eurest.supplier.dao.PurchaseOrderDao;
import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.invoiceXml.Concepto;
import com.eurest.supplier.model.CDDDC;
import com.eurest.supplier.model.CDDDCEmployee;
import com.eurest.supplier.model.CodigosSAT;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.Company;
import com.eurest.supplier.model.OutSourcingDocument;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.FileUploadBean;
import com.eurest.supplier.util.Logger;
import com.eurest.supplier.util.MultiFileUploadBean;
import com.eurest.supplier.util.PDFutils2;
import com.eurest.supplier.util.StringUtils;

@Service("outSourcingService")
public class OutSourcingService {
	
	@Autowired
	OutSourcingDao outSourcingDao;

	@Autowired
	SupplierService supplierService;
	
	 @Autowired
	 private JavaMailSender mailSenderObj;

	@Autowired
	EmailService emailService;

	@Autowired
	StringUtils stringUtils;
	
	@Autowired
	CodigosSATService codigosSATService; 
	
	@Autowired
	XmlToPojoService xmlToPojoService;
	
   	@Autowired
	PurchaseOrderService purchaseOrderService;

   	@Autowired
	DocumentsService documentsService;
   	
	@Autowired 
	FiscalDocumentService fiscalDocumentService;   	
   	
	@Autowired
	private DocumentsDao documentsDao;
	
	@Autowired
	PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	EDIService eDIService;
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	Logger logger;

	@Autowired
	CDDDCDao cdddcDao;	
	
	@Autowired
	CDDDCEmployeeDao cdddcEmployeeDao;
	
	@Autowired
	UsersService usersService;

	public final String OUTSOURCING_RECEIPT_SUBJECT = "CryoInfra Energía - Nueva documentación del proveedor _SUPPLIER_";
	public final String OUTSOURCING_RECEIPT_MESSAGE = "Estimado Proveedor <br /><br />Su documentación relacionada con los servicios especializados ha sido recibida y será revisada por nuestro equipo interno antes de ser aprobada. <br /><br />Por favor, esté atento a los mensajes en Portal  para recibir más información relacionada con la nueva reforma en materia laboral.<br /><br />Dear Provider <br /><br />Your documentation related to specialized services has been received and will be reviewed by our internal team before being approved. <br /><br />Please pay attention to the messages on the Portal to receive more information related to the new labor reform.<br /><br />";
	
	public final String OUTSOURCING_SEND_MESSAGE = "Estimado Colaborador <br /><br />El proveedor _SUPPLIER_ ha enviado la documentación _DOCTYPE_ relacionada con su condición de Servicios Especializados. Ésta documentación la puede consultar dentro del Portal de Proveedores en la pestaña \"Servicios Especializados\" <br /><br />Dear Collaborator <br /><br />The supplier _SUPPLIER_ has sent the _DOCTYPE_ documentation related to his condition of Specialized Services. This documentation can be consulted within the Provider Portal in the tab \\\"Specialized Services\\\" <br /><br />";
	
	public final String OUTSOURCING_SEND_MESSAGE_MONTH = "Estimado Colaborador <br /><br />El proveedor _SUPPLIER_ ha enviado la documentación _DOCTYPE_ relacionada con su condición de Servicios Especializados. Ésta documentación la puede consultar dentro del Portal de Proveedores en la pestaña \"Servicios Especializados\" <br /><br />";
	
	public final String OUTSOURCING_RECEIPT_SUBJECT_MONTH = "CryoInfra Energía - Nueva documentación mensual del proveedor _SUPPLIER_";
	
	public final String OUTSOURCING_ALERT_SUBJECT = "CryoInfra Energía - Deshabilitación del sistema: Documentación Mensual pendiente de carga";
	public final String OUTSOURCING_ALERT_MESSAGE = "Estimado Proveedor <br /><br />La documentación mensual relacionada con los servicios de Servicios Especializados no ha sido recibida aún. Recuerde que debe enviar los documentos a más tardar el día 17 de cada mes. Ingrese al portal para cargar la documentación faltante, de lo contrario no podrá enviar sus facturas o recibir nuevas órdenes de compra.<br /><br />Por favor, esté atento a los mensajes en Portal de Proveedores para recibir más información relacionada con la nueva reforma en materia laboral.<br /><br />Dear Provider <br /><br />The monthly documentation related to the services of Specialized Services has not yet been received. Remember that you must send the documents no later than the 17th of each month. Enter the portal to upload the missing documentation, otherwise you will not be able to send your invoices or receive new purchase orders.<br /><br />Please pay attention to the messages on the Supplier Portal to receive more information related to the new labor reform.<br /><br />";

	public final String OUTSOURCING_NOTIF_SUBJECT = "Portal de Proveedores-Notificación: Documentación Mensual pendiente de carga";
	public final String OUTSOURCING_NOTIF_MESSAGE = "Estimado Proveedor <br /><br />Recuerde cargar la documentación mensual relacionada con los servicios de Servicios Especializados antes de los días 15 de cada mes, de lo contrario su cuenta será deshabilitada hasta no cargar los documentos.<br /><br />Por favor, esté atento a los mensajes en Portal de Proveedores para recibir más información relacionada con la nueva reforma en materia laboral.<br /><br />Dear Provider <br /><br />Remember to upload the monthly documentation related to Specialized Services services before the 15th of each month, otherwise your account will be disabled until you upload the documents.<br /><br />Please, be attentive to the messages on the Supplier Portal to receive more information related to the new labor reform.<br /><br />";
	
	public static final String EMAIL_OSINV_ACCEPT_SUP = "CryoInfra Energía - Notificación de carga exitosa de la factura por servicios especializados en el portal de proveedores para la Orden de Compra No. ";
    public static final String EMAIL_OSINVOICE_ACCEPTED = "Estimado Proveedor: <br /><br />Su factura asociada con SERVICIOS ESPECIALIZADOS se ha recibido correctamente. Esta factura será revisada por el equipo interno junto con los recibos de pago debidamente timbrados. En caso de que no exista algún error con la documentación enviada, la factura será programada a pago a partir de la fecha de recepción de la factura.<br /><br />La orden de compra asociada a su factura es: <br><br>Dear Provider: <br /><br />Your invoice associated with SPECIALIZED SERVICES has been received correctly. This invoice will be reviewed by the internal team together with the duly stamped payment receipts. If there is no error with the documentation sent, the invoice will be scheduled for payment from the date of receipt of the invoice.<br /><br />The purchase order associated with your invoice is: <br ><br>";
    
	public static final String EMAIL_OSINV_ACCEPT_COLAB = "CryoInfra Energía - Notificación de revisión de factura por servicios especializados para la Orden de Compra No. ";
    public static final String EMAIL_OSINVOICE_MSG = "Estimado Colaborador: <br /><br />Una nueva factura asociada con SERVICIOS ESPECIALIZADOS del proveedor _SUPPLIERNAME_ ha sido cargada en el portal. Agradecemos su pronta intervención para la revisión y aprobación o rechazo de esta factura<br /><br />La orden de compra asociada a la factura es: <br /><br />Dear Collaborator: <br /><br />A new invoice associated with SPECIALIZED SERVICES from supplier _SUPPLIERNAME_ has been uploaded to the portal. We appreciate your prompt intervention to review and approve or reject this invoice<br /><br />The purchase order associated with the invoice is: <br /><br />";
    
	public static final String EMAIL_OSINV_ACCEPT = "CryoInfra Energía - Notificación Factura de Servicios Especializados ACEPTADA para la Orden de Compra No. ";
    public static final String EMAIL_OSINVOICE_ACCEPT= "Estimado Proveedor: <br /><br />Su factura asociada con SERVICIOS ESPECIALIZADOS se ha recibido ACEPTADA para pago. La factura será programada a pago a partir de la fecha de recepción de la factura.<br /><br />La orden de compra asociada a su factura es:<br /><br />Dear Provider: <br /><br />Your invoice associated with SPECIALIZED SERVICES has been received ACCEPTED for payment. The invoice will be scheduled for payment from the date of receipt of the invoice.<br /><br />The purchase order associated with your invoice is:<br /><br />";
    
	public static final String EMAIL_OSINV_REJECT = "CryoInfra Energía - Notificación Factura de Servicios Especializados RECHAZADA para la Orden de Compra No. ";
    public static final String EMAIL_OSINVOICE_REJECT = "Estimado Proveedor: <br /><br />Su factura asociada a SERVICIOS ESPECIALIZADOS se ha recibido RECHAZADA. A continuación encontrará las notas del rechazo.<br /> <br /> NOTAS: _NOTES_ <br /><br />Revise los errores y vuelva a subir su factura incluyendo los recibos de nómina debidamente timbrados. <br /> <br />La orden de compra asociada a su factura es:<br /><br />Dear Provider: <br /><br />Your invoice associated with SPECIALIZED SERVICES has been received REJECTED. Below you will find the rejection notes.<br /> <br /> NOTES: _NOTES_ <br /><br />Review the errors and re-upload your invoice including the duly stamped payslips. <br /> <br />The purchase order associated with your invoice is:<br /><br />";
    
	public static final String EMAIL_OSDOC_REJECT = "CryoInfra Energía -  Notificación de documento RECHAZADO";
    public static final String EMAIL_OSDOC_REJECT_MSG = "Estimado Proveedor: <br /><br />El documento _DOCNAME_ asociado con SERVICIOS ESPECIALIZADOS ha sido RECHAZADO. A continuación encontrará las notas del rechazo.<br /> <br /> NOTAS: _NOTES_ <br /><br />Revise los errores y vuelva a cargar el documento accediendo la pestaña \"Servicios Especializados\" y utilizando la columna \"Cargar reemplazo\" para enviar nuevamente el documento.<br /><br />Dear Provider: <br /><br />The document _DOCNAME_ associated with SPECIALIZED SERVICES has been REJECTED. Below you will find the rejection notes.<br /> <br /> NOTES: _NOTES_ <br /><br />Review errors and reload the document by accessing the \\\"Specialized Services\\\" tab and using the \\ column \"Upload replacement\\\" to send the document again.<br /><br />";
    
	public static final String EMAIL_OSDOC_ACCEPT = "CryoInfra Energía - Notificación de documento ACEPTADO para la Orden de Compra No. ";
    public static final String EMAIL_OSDOC_ACCEPT_MSG= "Estimado Proveedor: <br /><br />El documento _DOCNAME_ asociado con SERVICIOS ESPECIALIZADOS se ha recibido ACEPTADO. Si el documento es una factura, ésta será programada a pago a partir de la fecha de aprobación.<br /><br />Dear Supplier: <br /><br />The document _DOCNAME_ associated with SPECIALIZED SERVICES has been received ACCEPTED. If the document is an invoice, it will be scheduled for payment from the approval date.<br /><br />";
    
    public static final String FISCAL_DOC_PENDING = "PENDIENTE";
    public static final String FISCAL_DOC_REJECTED = "RECHAZADO";
    public static final String FISCAL_DOC_APPROVED = "APROBADO";
    public static final String LOG_APPROVAL = "APROBACIONES";
    public static final String LOG_DOCUMENTS = "CARGA_DOCUMENTOS";

    public final String OUTSOURCING_RECEIPT_SUBJECT_QUARTER = "PSmartREPSE: Nueva documentación cuatrimestral del proveedor _SUPPLIER_";
        
    private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(OutSourcingService.class);
    
    public synchronized String validateInvoiceFromOrder(InvoiceDTO inv, String addressBook, int documentNumber, String documentType, 
    		                               String tipoComprobante, String receiptList, String xmlContent, List<MultipartFile> uploadedFiles,
    		                               String orderCompany) {

    	try {
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		String usr = auth.getName();
			String rOs = validateInvoiceCodes(inv.getConcepto());
			if("".equals(rOs)) {
				return "La factura NO contiene claves por Conceptos por Servicios Especializados. Utilice el botón \"Cargar Factura\" para enviar facturas que no contengan servicios especializados";
			}
			
			PurchaseOrder po = purchaseOrderService.searchbyOrderAndAddressBookAndCompany(documentNumber, addressBook, documentType, orderCompany);
			
			String res = documentsService.validateInvoiceFromOrder(inv, addressBook, documentNumber, 
					                                                documentType, tipoComprobante, po, 
					                                                false, xmlContent, 
					                                                receiptList, true,"",usr);
			
			if("".equals(res)) {
				Supplier s = supplierService.searchByAddressNumber(addressBook);
				EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				emailAsyncSup.setProperties(EMAIL_OSINV_ACCEPT_SUP + po.getOrderNumber() + "-" + po.getOrderType(), 
				stringUtils.prepareEmailContent(EMAIL_OSINVOICE_ACCEPTED + po.getOrderNumber() + "-" + po.getOrderType() + "<br /> <br />" + AppConstants.ETHIC_CONTENT), s.getEmailSupplier());
				emailAsyncSup.setMailSender(mailSenderObj);
				Thread emailThreadSup = new Thread(emailAsyncSup);
				emailThreadSup.start();	
				
            	UserDocument doc = new UserDocument(); 
            	doc.setAddressBook(po.getAddressNumber());
            	doc.setDocumentNumber(documentNumber);
            	doc.setDocumentType(documentType);
            	doc.setContent(uploadedFiles.get(0).getBytes());
            	doc.setType(uploadedFiles.get(0).getContentType());
            	String fileName = uploadedFiles.get(0).getOriginalFilename();
            	fileName = fileName.replace(" ", "_");
            	doc.setName(fileName);
            	doc.setSize(uploadedFiles.get(0).getSize());
            	doc.setStatus(true);
            	doc.setAccept(true);
            	doc.setFiscalType(tipoComprobante);;
            	doc.setType("text/xml");
            	doc.setFolio(inv.getFolio());
            	doc.setSerie(inv.getSerie());
            	doc.setUuid(inv.getUuid());
            	doc.setUploadDate(new Date());
            	doc.setFiscalRef(0);
            	documentsService.save(doc, new Date(), "");
				
            	doc = new UserDocument(); 
            	doc.setAddressBook(po.getAddressNumber());
            	doc.setDocumentNumber(documentNumber);
            	doc.setDocumentType(documentType);
            	doc.setContent(uploadedFiles.get(1).getBytes());
            	doc.setType(uploadedFiles.get(1).getContentType());
            	fileName = uploadedFiles.get(1).getOriginalFilename();
            	fileName = fileName.replace(" ", "_");
            	doc.setName(fileName);
            	doc.setSize(uploadedFiles.get(1).getSize());
            	doc.setStatus(true);
            	doc.setAccept(true);
            	doc.setFiscalType(tipoComprobante);
            	doc.setType("application/pdf");
            	doc.setFolio(inv.getFolio());
            	doc.setSerie(inv.getSerie());
            	doc.setUuid(inv.getUuid());
            	doc.setUploadDate(new Date());
            	doc.setFiscalRef(0);
            	documentsService.save(doc, new Date(), "");
   		    	
            	ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(baos);
                
                ZipEntry entry = new ZipEntry(uploadedFiles.get(0).getOriginalFilename());
                entry.setSize(uploadedFiles.get(0).getSize());
                zos.putNextEntry(entry);
                zos.write(uploadedFiles.get(0).getBytes());

                entry = new ZipEntry(uploadedFiles.get(1).getOriginalFilename());
                entry.setSize(uploadedFiles.get(1).getSize());
                zos.putNextEntry(entry);
                zos.write(uploadedFiles.get(1).getBytes());
                
                entry = new ZipEntry(uploadedFiles.get(2).getOriginalFilename());
                entry.setSize(uploadedFiles.get(2).getSize());
                zos.putNextEntry(entry);
                zos.write(uploadedFiles.get(2).getBytes());
                zos.closeEntry();
                zos.close();
                
   		    	OutSourcingDocument docOs = new OutSourcingDocument();
   				docOs.setAddressBook(addressBook);
   				docOs.setSupplierName(s.getRazonSocial());
   				docOs.setDocumentType("REC_NOMINA");
   				docOs.setContent(baos.toByteArray());
   				docOs.setType(uploadedFiles.get(2).getContentType());
   				docOs.setName("CONSOLIDADO_" + uploadedFiles.get(2).getOriginalFilename());
   				docOs.setSize(baos.toByteArray().length);
   				docOs.setStatus(false);
   				docOs.setFolio(inv.getFolio());
   				docOs.setUuid(inv.getUuid());
   				docOs.setAttachId(inv.getUuid());
   				docOs.setUploadDate(new Date());
   				docOs.setFrequency("INV");
   				docOs.setCompany("");
   				docOs.setOrderNumber(po.getOrderNumber());
   				docOs.setOrderType(po.getOrderType());
   				docOs.setObsolete(false);
   				docOs.setDocStatus(FISCAL_DOC_PENDING);
   		    	outSourcingDao.saveDocument(docOs);

   		    	List<Receipt> receipts = purchaseOrderService.getReceiptsByUUID(inv.getUuid());
   		    	for(Receipt r : receipts) {
   		    		r.setPaymentStatus("PENDING");
   		    		purchaseOrderService.updateReceipt(r);
   		    	}
   		    	
   		    	String internalEmail = "";
   		    	UDC udc =  udcService.searchBySystemAndKey("OSAPPROVE", "ROLE_RH");
				if(udc != null) {
					internalEmail = udc.getStrValue1();
				}
				
				if(!"".equals(internalEmail)) {
	   		    	String content = EMAIL_OSINVOICE_MSG;
	   		    	content = content.replace("_SUPPLIERNAME_", s.getRazonSocial());
		   		 	emailAsyncSup = new EmailServiceAsync();
					emailAsyncSup.setProperties(EMAIL_OSINV_ACCEPT_COLAB + po.getOrderNumber() + "-" + po.getOrderType(), 
					stringUtils.prepareEmailContent(content + po.getOrderNumber() + "-" + po.getOrderType() + "<br /> <br />" + AppConstants.ETHIC_CONTENT), internalEmail);
					emailAsyncSup.setMailSender(mailSenderObj);
					emailThreadSup = new Thread(emailAsyncSup);
					emailThreadSup.start();	
				}

				logger.log(LOG_DOCUMENTS, "CARGA DE FACTURAS C/RECIBO DE NOMINA PARA EL PROVEEDOR: " + s.getRazonSocial());
				
   		    	return "";
				
			}else {
				return res;
			}
    	}catch(Exception e) {
    		log4j.error("Exception" , e);
    		e.printStackTrace();
    		return e.getMessage(); 
    	}

    }
    
    public synchronized String approveDocument(int id, String notes, String user) {

 		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendarNow = Calendar.getInstance();
        String now = simpleDateFormat.format(calendarNow.getTime());
        
 		OutSourcingDocument osDoc = outSourcingDao.getDocumentById(id);
 		osDoc.setNotes(osDoc.getNotes() + " / " + now + " - " + user + " Escribió: " + notes);
 		osDoc.setStatus(false);
 		osDoc.setDocStatus(FISCAL_DOC_APPROVED);
 		outSourcingDao.updateDocument(osDoc);
 		/*
 		if("REC_NOMINA".equals(osDoc.getDocumentType())) {
			FiscalDocuments fd = fiscalDocumentService.getFiscalDocumentsByUuid(osDoc.getUuid());
			if(fd != null) {
				fd.setStatus(AppConstants.FISCAL_DOC_APPROVED);
				fd.setApprovalStatus(AppConstants.FISCAL_DOC_APPROVED);
				fiscalDocumentService.updateFiscalDocuments(fd);
			}
 		}*/
 		
 		if("CEDU_DETERM_CUOTAS".equals(osDoc.getDocumentType())) {
			int res=savingODDDCFromPDF(osDoc.getContent(),osDoc.getId());
			if(res==0) {
				return "Error No se encontro CÉDULA DE DETERMINACIÓN DE CUOTAS para lista nominal en el archivo";
			}
			
 		}
 		
 		String docName = osDoc.getName();
 		Supplier s = supplierService.searchByAddressNumber(osDoc.getAddressBook());
 		
 		EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
 		String msg = EMAIL_OSDOC_ACCEPT_MSG;
 		msg = msg.replace("_DOCNAME_", osDoc.getName());
		emailAsyncSup.setProperties(EMAIL_OSDOC_ACCEPT, stringUtils.prepareEmailContent(msg), s.getEmailSupplier());
		emailAsyncSup.setMailSender(mailSenderObj);
		Thread emailThreadSup = new Thread(emailAsyncSup);
		emailThreadSup.start();

		logger.log(LOG_APPROVAL, "APROBACIÓN DE DOCUMENTOS PARA : " + s.getRazonSocial() +  " \"" + docName + "\" "  + now + " - " + user + " Escribió: " + notes);
		return "";
	}

    public synchronized String approveInvoice(String uuid, String notes, String user) {

 		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendarNow = Calendar.getInstance();
        String now = simpleDateFormat.format(calendarNow.getTime());
        
	    String internalEmail = "";
	    Users usr =  usersService.getByUserName(user);
		if(usr != null) {
			internalEmail = usr.getEmail();
		}
        
		List<UserDocument> docs = documentsDao.searchCriteriaByUuidOnly(uuid);
		if(docs != null) {
			InvoiceDTO inv = null;
			if(docs != null) {
				for(UserDocument u : docs) {
					Supplier s = supplierService.searchByAddressNumber(u.getAddressBook());
					if(AppConstants.INVOICE_FIELD.equals(u.getFiscalType()) && "text/xml".equals(u.getType())) {
						try {
						String xmlStr = new String(u.getContent(), StandardCharsets.UTF_8);
						inv = getInvoiceXmlFromString(xmlStr);
						List<Receipt> receipts = purchaseOrderDao.getOrderReceiptsByUuid(uuid);
						PurchaseOrder po = purchaseOrderDao.searchbyOrderAndAddressBookAndType(u.getDocumentNumber(),u.getAddressBook(), u.getDocumentType());
				        eDIService.createNewVoucher(po, inv, 0, s, receipts, AppConstants.NN_MODULE_VOUCHER);
				        
				        String emailRecipent = s.getEmailSupplier();
				        if(!"".equals(internalEmail)) {
				        	emailRecipent = emailRecipent + "," + internalEmail;
				        }
				        
						EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						emailAsyncSup.setProperties(EMAIL_OSINV_ACCEPT + po.getOrderNumber() + "-" + po.getOrderType(), 
						stringUtils.prepareEmailContent(EMAIL_OSINVOICE_ACCEPT + po.getOrderNumber() + "-" + po.getOrderType() + "<br /> <br />" + AppConstants.ETHIC_CONTENT), emailRecipent);
						emailAsyncSup.setMailSender(mailSenderObj);
						Thread emailThreadSup = new Thread(emailAsyncSup);
						emailThreadSup.start();	
				        
						log4j.info("Sent: " + inv.getUuid());
						}catch(Exception e){
							log4j.error("Exception" , e);
						}
					}
					
					if(AppConstants.INVOICE_FIELD.equals(u.getFiscalType()) && "application/pdf".equals(u.getType())) {
						try {
							//Send pdf to remote server
    	                	File convFile = new File(System.getProperty("java.io.tmpdir")+"/" + inv.getUuid() + ".pdf");
    	                	FileUtils.writeByteArrayToFile(convFile, u.getContent());
    	                	documentsService.sendFileToRemote(convFile, inv.getUuid() + ".pdf");
    	                	convFile.delete();
						}catch(Exception e){
							log4j.error("Exception" , e);
						}
					}
					
					logger.log(LOG_APPROVAL, "APROBACIÓN DE FACTURAS PARA : " + s.getRazonSocial() +  " \" UUID:" + u.getUuid() + "\" "  + now + " - " + user + " Escribió: " + notes);
				
				}
				
   		    	List<Receipt> receipts = purchaseOrderService.getReceiptsByUUID(uuid);
   		    	for(Receipt r : receipts) {
   		    		r.setPaymentStatus("");
   		    		purchaseOrderService.updateReceipt(r);
   		    	}
   		    	
   		    	List<OutSourcingDocument> docOs = outSourcingDao.searchByAttachID(uuid);
   		    	for(OutSourcingDocument d : docOs) {
   		    		d.setStatus(true);
   		  		    d.setDocStatus(FISCAL_DOC_APPROVED);
   		  		    d.setNotes(notes);
   		    		outSourcingDao.updateDocument(d);
   		    	}
			}
		}
			
    	return "";
    }
    
    
    public synchronized String rejectDocument(int id, String notes, String user) {
		
 		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendarNow = Calendar.getInstance();
        String now = simpleDateFormat.format(calendarNow.getTime());
        
 		OutSourcingDocument osDoc = outSourcingDao.getDocumentById(id);
 		osDoc.setNotes(osDoc.getNotes() + " / " + now + " - " + user + " Escribió: " + notes);
 		osDoc.setStatus(false);
 		osDoc.setDocStatus(FISCAL_DOC_REJECTED);
 		outSourcingDao.updateDocument(osDoc);
 		
 		String docName = osDoc.getName();
 		Supplier s = supplierService.searchByAddressNumber(osDoc.getAddressBook());
 		
 		EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
 		String msg = EMAIL_OSDOC_REJECT_MSG;
 		msg = msg.replace("_DOCNAME_", osDoc.getName());
 		msg = msg.replace("_NOTES_", osDoc.getNotes());
		emailAsyncSup.setProperties(EMAIL_OSDOC_REJECT, stringUtils.prepareEmailContent(msg), s.getEmailSupplier());
		emailAsyncSup.setMailSender(mailSenderObj);
		Thread emailThreadSup = new Thread(emailAsyncSup);
		emailThreadSup.start();
		
		logger.log(LOG_APPROVAL, "RECHAZO DE DOCUMENTOS PARA : " + s.getRazonSocial() +  " \"" + docName + "\" "  + now + " - " + user + " Escribió: " + notes);
		return "";
	}
    
    public synchronized String rejectInvoice(String uuid, String notes, String user) {
    	
 		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendarNow = Calendar.getInstance();
        String now = simpleDateFormat.format(calendarNow.getTime());
        
    	int documentNumber = 0;
    	String documentType = "";
    	String addressNumber = "";
    	List<UserDocument> docs = documentsDao.searchCriteriaByUuidOnly(uuid);
    	for(UserDocument d : docs) {
    		documentsDao.deleteDocuments(d.getId());
    		documentNumber = d.getDocumentNumber();
    		documentType = d.getDocumentType();
    		addressNumber = d.getAddressBook();
    	}
    	
    	List<OutSourcingDocument> docOs = outSourcingDao.searchByAttachID(uuid);
    	for(OutSourcingDocument d : docOs) {
    		outSourcingDao.deleteDocument(d.getId());
    	}

    	List<Receipt> receipts = purchaseOrderService.getReceiptsByUUID(uuid);
    	for(Receipt r : receipts) {
    		r.setUuid("");
    		r.setEstPmtDate(null);
    		r.setFolio("");
    		r.setSerie("");
    		r.setInvDate(null);
    		r.setStatus(AppConstants.STATUS_OC_RECEIVED);
	    	r.setPaymentStatus("");
    		purchaseOrderService.updateReceipt(r);
    	}
    	
    	PurchaseOrder po = purchaseOrderDao.searchbyOrderAndAddressBookAndType(documentNumber,addressNumber, documentType);
    	po.setCreditNotUuid("");
    	po.setOrderStauts(AppConstants.STATUS_OC_RECEIVED);
    	po.setInvoiceAmount(0);
    	po.setInvoiceNumber("");
    	po.setInvoiceUploadDate(null);
    	purchaseOrderDao.updateOrders(po);
    	
    	Supplier s = supplierService.searchByAddressNumber(addressNumber);
    	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
    	
    	String msg = EMAIL_OSINVOICE_REJECT;
    	msg = msg.replace("_NOTES_", notes);
    	
		emailAsyncSup.setProperties(EMAIL_OSINV_REJECT + documentNumber + "-" + documentType,
		stringUtils.prepareEmailContent(msg + documentNumber + "-" + documentType + "<br /> <br />" + AppConstants.ETHIC_CONTENT), s.getEmailSupplier());
		emailAsyncSup.setMailSender(mailSenderObj);
		Thread emailThreadSup = new Thread(emailAsyncSup);
		emailThreadSup.start();
		
		logger.log(LOG_APPROVAL, "RECHAZO DE FACTURA PARA : " + s.getRazonSocial() +  " \"" + " PO: " + po.getOrderNumber() + "-" + po.getOrderType() + "\" "  + now + " - " + user + " Escribió: " + notes);
    	
    	return "";
    }
	
	public OutSourcingDocument getDocumentById(int id) {
		return outSourcingDao.getDocumentById(id);
	}
		
	
	public synchronized String saveBaseLineDocument(MultiFileUploadBean multiFileUploadBean, String effectiveDate, String addressNumber) {

		List<MultipartFile> uploadedFiles = multiFileUploadBean.getUploadedFiles();
   		
   		try {
   			if(uploadedFiles.size() == 3) {
   				
   				Supplier s = supplierService.searchByAddressNumber(addressNumber);
   				if(s != null) {
   					
   					Date today = new Date();
   					Calendar calendar = Calendar.getInstance();
   					calendar.setTime(today);
   					 
   					calendar.add(Calendar.MONTH, -1);
   					 	
   					int monthLoad = (calendar.get(Calendar.MONTH) + 1 );
   					int yearLoad = calendar.get(Calendar.YEAR);
   		    	
	   		    	//Constancia de cumplimiento de obligaciones
	   		    	OutSourcingDocument doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setRfc(s.getRfc());
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("CONST_CUMP_OBLIG");
	   		    	doc.setContent(uploadedFiles.get(0).getBytes());
	   		    	doc.setType(uploadedFiles.get(0).getContentType());
	   		    	doc.setName(uploadedFiles.get(0).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(0).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("BL");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   				doc.setDocStatus(FISCAL_DOC_PENDING);
	   		    	doc.setHistory(false);
	   		    	doc.setMonthLoad(monthLoad);
	   		    	doc.setYearLoad(yearLoad);
	   		    	outSourcingDao.saveDocument(doc);
	   		    	
	   		        //Constancia de Situación Fiscal 
	   				doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setRfc(s.getRfc());
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("CONST_SIT_FIS");
	   		    	doc.setContent(uploadedFiles.get(1).getBytes());
	   		    	doc.setType(uploadedFiles.get(1).getContentType());
	   		    	doc.setName(uploadedFiles.get(1).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(1).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("BL");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   				doc.setDocStatus(FISCAL_DOC_PENDING);
	   		    	doc.setHistory(false);
	   		    	doc.setMonthLoad(monthLoad);
	   		    	doc.setYearLoad(yearLoad);
	   		    	outSourcingDao.saveDocument(doc);
	   		    	
	   		    	//Autorización vigente a que se refiere el artículo 15 de la Ley Federal del Trabajo
	   				doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setRfc(s.getRfc());
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("AUT_STPS");
	   		    	doc.setContent(uploadedFiles.get(2).getBytes());
	   		    	doc.setType(uploadedFiles.get(2).getContentType());
	   		    	doc.setName(uploadedFiles.get(2).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(2).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("BL");
	   		    	doc.setEffectiveDate(effectiveDate);
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   				doc.setDocStatus(FISCAL_DOC_PENDING);
	   		    	doc.setHistory(false);
	   		    	doc.setMonthLoad(monthLoad);
	   		    	doc.setYearLoad(yearLoad);
	   		    	outSourcingDao.saveDocument(doc);
	   		    	
	   		    	//Lista de trabajadores
	   				/*doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("LISTA_TRAB");
	   		    	doc.setContent(uploadedFiles.get(3).getBytes());
	   		    	doc.setType(uploadedFiles.get(3).getContentType());
	   		    	doc.setName(uploadedFiles.get(3).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(3).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("BL");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   				doc.setDocStatus(FISCAL_DOC_PENDING);
	   		    	outSourcingDao.saveDocument(doc);*/
	   		    	
	   		    	//Acta protocolizada con detalle de su objeto social
	   				/*doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("ACTA_PROTOCOL");
	   		    	doc.setContent(uploadedFiles.get(4).getBytes());
	   		    	doc.setType(uploadedFiles.get(4).getContentType());
	   		    	doc.setName(uploadedFiles.get(4).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(4).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("BL");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   				doc.setDocStatus(FISCAL_DOC_PENDING);
	   		    	outSourcingDao.saveDocument(doc);*/
	   		    	
	   		    	s.setOutSourcingAccept(true);
	   		    	s.setOutSourcingRecordDate(new Date());
	   		    	supplierService.updateSupplierCore(s);
	   		    	
	   		    	String subject = OUTSOURCING_RECEIPT_SUBJECT;
	   		    	subject = subject.replace("_SUPPLIER_", s.getRazonSocial());
	   		    	
	   		    	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(OUTSOURCING_RECEIPT_MESSAGE), s.getEmailSupplier());
	   				emailAsyncSup.setMailSender(mailSenderObj);
	   				Thread emailThreadSup = new Thread(emailAsyncSup);
	   				emailThreadSup.start();
	   				
	   		    	String internalEmail = "";
	   		    	UDC udc =  udcService.searchBySystemAndKey("OSAPPROVE", "ROLE_RH");
					if(udc != null) {
						internalEmail = udc.getStrValue1();
					}
					
					if(!"".equals(internalEmail)) {
						emailAsyncSup = new EmailServiceAsync();
		   		    	String content = OUTSOURCING_SEND_MESSAGE;
		   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
		   		    	content = content.replace("_DOCTYPE_", "BASE");
		   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), internalEmail);
		   				emailAsyncSup.setMailSender(mailSenderObj);
		   				emailThreadSup = new Thread(emailAsyncSup);
		   				emailThreadSup.start();
					}
	   				
	   		    	internalEmail = "";
	   		    	udc =  udcService.searchBySystemAndKey("OSAPPROVE", "ROLE_TAX");
					if(udc != null) {
						internalEmail = udc.getStrValue1();
					}
					
					if(!"".equals(internalEmail)) {
						emailAsyncSup = new EmailServiceAsync();
		   		    	String content = OUTSOURCING_SEND_MESSAGE;
		   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
		   		    	content = content.replace("_DOCTYPE_", "BASE");
		   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), internalEmail);
		   				emailAsyncSup.setMailSender(mailSenderObj);
		   				emailThreadSup = new Thread(emailAsyncSup);
		   				emailThreadSup.start();
					}
					
	   		    	internalEmail = "";
	   		    	udc =  udcService.searchBySystemAndKey("OSAPPROVE", "ROLE_LEGAL");
					if(udc != null) {
						internalEmail = udc.getStrValue1();
					}
					
					if(!"".equals(internalEmail)) {
						emailAsyncSup = new EmailServiceAsync();
		   		    	String content = OUTSOURCING_SEND_MESSAGE;
		   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
		   		    	content = content.replace("_DOCTYPE_", "BASE");
		   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), internalEmail);
		   				emailAsyncSup.setMailSender(mailSenderObj);
		   				emailThreadSup = new Thread(emailAsyncSup);
		   				emailThreadSup.start();
					}
					
					logger.log(LOG_DOCUMENTS, "CARGA DE DOCUMENTOS BASE DEL PROVEEDOR: " + s.getRazonSocial());
					
   				}
   			}
   	    	
   		}catch(Exception e) {
   			log4j.error("Exception" , e);
   			e.printStackTrace();
   			return e.getMessage();
   		}
   		
		return "";
		
	}
	
	public synchronized String saveMonthlyDocs(MultiFileUploadBean multiFileUploadBean, String addressNumber) {
		
		List<MultipartFile> uploadedFiles = multiFileUploadBean.getUploadedFiles();

		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.MONTH, -1);
		 	
		int monthLoad = (calendar.get(Calendar.MONTH) + 1 );
		int yearLoad = calendar.get(Calendar.YEAR);	
		
   		try {
   			if(uploadedFiles.size() == 5) {
   				
   				Supplier s = supplierService.searchByAddressNumber(addressNumber);
   				//if(s != null) {
   				if(s != null && !s.isOutSourcingMonthlyAccept()) {
   					
   					String [] data= new PDFutils2().getPdfText(uploadedFiles.get(4).getBytes(), 2, 0);
   					if(data==null) {
      					 return "El formato del archivo Cédula de determinación de cuotas es INVALIDO, favor de cargar el archivo en un formato PDF/texto";
   					}
   					String string="";
   					for (String string2 : data) {
   						string=string+" "+string2;
					}
   					
      				if(data==null || !(string.contains("SISTEMA ÚNICO DE AUTODETERMINACIÓN")&&string.contains("Período de Proceso")&&string.contains("CÉDULA DE DETERMINACIÓN DE CUOTAS"))) {
       					 return "El formato del archivo Cédula de determinación de cuotas es INVALIDO, favor de cargar el archivo en un formato PDF/texto";       					
       				}
   					
      				 
      				if(data[2].contains("EXTEMPORÁNEO")) {
      					 String meses[] = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};			 			 		 			 
      					 String aux = ""; 
      					 if (data[4].contains("Período de Proceso:")) {
      						 aux = ((data[4].replace("Período de Proceso:","")).replace("Calculo Extemporaneo al:", "")).replace("T. D.", "").trim();				 			    	
      					 } else {
      						if(data[5].contains("Período de Proceso:")) {
   	 		 				aux = (data[5].replace("Período de Proceso:","")).replace("Calculo Extemporaneo al:", ""); 		 			
   	 		 				aux = (aux.replace(aux.substring( aux.indexOf("T. D.", 0), aux.length()),"")).trim();
      						}
      					 }
      					 if (aux.contains("-")) {
      						String mes1 = aux.substring(0, aux.indexOf( "-", 0) ); 
    	 			    	int mespdf = 0;
    	 			    	for (int v = 0; v < meses.length; v++) {
    	 			    		if ( (meses[v]).equals(mes1.toUpperCase()) ) {
    	 			    			mespdf = v;
    	 			    		}
    	 			    	}
    	 			    	int monthLoad2 = (calendar.get(Calendar.MONTH));
   						if ( monthLoad2 == mespdf ) {
      						} else {
      							//Comentar la siguiente linea para no validar periodo -
      							//return "El archivo Cédula de determinación de cuotas no corresponde al periodo del mes solicitado, favor de cargar el archivo correcto";
      						}
      					 }					    						  	 			    	 	 			    				    	 	 			    	
      				 } else {
      					if(data[2].contains("OBRERO-PATRONALES, APORTACIONES Y AMORTIZACIONES")) {
      						if (data[3].contains("Bimestre de Proceso:")) {						
      							String aux = (data[3].replace("Bimestre de Proceso:","")).trim();
   	 		 				String replaceString = aux.replace(aux.substring( aux.indexOf("Fecha de Proceso:", 0)+17, aux.length()),"");
   		 		    		aux = (replaceString.replace("Fecha de Proceso:","")).trim();
     							  							
   							String meses[] = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};			 			 		 			 
   							String mes1 = aux.substring(0, aux.indexOf( "-", 0) ); 

   							int mespdf = 0;
   							for (int v = 0; v < meses.length; v++) {
   								if ( (meses[v]).equals(mes1.toUpperCase()) ) {
   									mespdf = v;
   								}
   							}
   							int monthLoad2 = (calendar.get(Calendar.MONTH));
   							if ( monthLoad2 == mespdf ) {
      							} else {
      							//Comentar la siguiente linea para no validar periodo -
      								//return "El archivo Cédula de determinación de cuotas no corresponde al periodo del mes solicitado, favor de cargar el archivo correcto";
      							}
      						}
      					} else {
      						if(data[1].contains("CÉDULA DE DETERMINACIÓN DE CUOTAS")) {
      		 			    	if (data[2].contains("Período de Proceso:")) {
      			 			    	String meses[] = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};			 			 		 			 
      			 			    	String aux = (data[2].replace("Período de Proceso:","")).replace("Fecha de Proceso:", "").trim();				 			    	
      			 			    	String mes1 = aux.substring(0, aux.indexOf( "-", 0) ); 
      			 			    	
      			 			    	int mespdf = 0;
      			 			    	for (int v = 0; v < meses.length; v++) {
      			 			    		if ( (meses[v]).equals(mes1.toUpperCase()) ) {
      			 			    			mespdf = v;
      			 			    		}
      			 			    	}
      			 			    	int monthLoad2 = (calendar.get(Calendar.MONTH));
      								if ( monthLoad2 == mespdf ) {
      								} else {
      								//Comentar la siguiente linea para no validar periodo -
      						         //return "El archivo Cédula de determinación de cuotas no corresponde al periodo del mes solicitado, favor de cargar el archivo correcto";
      								}						 			  	
      		 			    	}
      						}
      					}
      				}
   				      				
   					data= new PDFutils2().getPdfText(uploadedFiles.get(0).getBytes(), 6, 0);
   					if(data==null) {
      					 return "El formato del archivo Pagos Provisionales de ISR por salarios es INVALIDO, favor de cargar el archivo en un formato PDF/texto";
   					} 
   					
   					data= new PDFutils2().getPdfText(uploadedFiles.get(1).getBytes(), 6, 0);
   					if(data==null) {
      					 return "El formato del archivo Pago de las cuotas obrero-patronales al IMSS es INVALIDO, favor de cargar el archivo en un formato PDF/texto";
   					} 
   					
   					data= new PDFutils2().getPdfText(uploadedFiles.get(2).getBytes(), 6, 0);
   					if(data==null) {
      					 return "El formato del archivo Pago de las aportaciones al INFONAVIT es INVALIDO, favor de cargar el archivo en un formato PDF/texto";
   					} 
   					
   					data= new PDFutils2().getPdfText(uploadedFiles.get(3).getBytes(), 6, 0);
   					if(data==null) {
      					 return "El formato del archivo Evidencia del Pago Provisional de IVA es INVALIDO, favor de cargar el archivo en un formato PDF/texto";
   					}
   					
 	   				//Acuse de declaración informativa mensual del IMSS
   					OutSourcingDocument doc = new OutSourcingDocument();
   					/*doc.setAddressBook(addressNumber);
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("DECL_MENS_IMSS");
	   		    	doc.setContent(uploadedFiles.get(0).getBytes());
	   		    	doc.setType(uploadedFiles.get(0).getContentType());
	   		    	doc.setName(uploadedFiles.get(0).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(0).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("MONTH");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   				doc.setDocStatus(FISCAL_DOC_PENDING);
	   		    	outSourcingDao.saveDocument(doc);*/
	   		    	
	   		    	//Pagos Provisionales de ISR por salarios mensual
	   				doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setRfc(s.getRfc());
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("PROV_ISR_SAL");
	   		    	doc.setContent(uploadedFiles.get(0).getBytes());
	   		    	doc.setType(uploadedFiles.get(0).getContentType());
	   		    	doc.setName(uploadedFiles.get(0).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(0).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("MONTH");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   				doc.setDocStatus(FISCAL_DOC_PENDING);
	   		    	doc.setHistory(false);
	   		    	doc.setMonthLoad(monthLoad);
	   		    	doc.setYearLoad(yearLoad);
	   		    	outSourcingDao.saveDocument(doc);
	   		    	
	   		        //Pago de las cuotas obrero-patronales al Instituto Mexicano del Seguro Social
	   				doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setRfc(s.getRfc());
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("CUOT_OBR_PATR");
	   		    	doc.setContent(uploadedFiles.get(1).getBytes());
	   		    	doc.setType(uploadedFiles.get(1).getContentType());
	   		    	doc.setName(uploadedFiles.get(1).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(1).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("MONTH");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   				doc.setDocStatus(FISCAL_DOC_PENDING);
	   		    	doc.setHistory(false);
	   		    	doc.setMonthLoad(monthLoad);
	   		    	doc.setYearLoad(yearLoad);
	   		    	outSourcingDao.saveDocument(doc);
	   		    	
	   		    	//Pago de las aportaciones al Instituto del Fondo Nacional de la Vivienda para los Trabajadores
	   				doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setRfc(s.getRfc());
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("APOR_FON_NAL_VIV");
	   		    	doc.setContent(uploadedFiles.get(2).getBytes());
	   		    	doc.setType(uploadedFiles.get(2).getContentType());
	   		    	doc.setName(uploadedFiles.get(2).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(2).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("MONTH");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   				doc.setDocStatus(FISCAL_DOC_PENDING);
	   		    	doc.setHistory(false);
	   		    	doc.setMonthLoad(monthLoad);
	   		    	doc.setYearLoad(yearLoad);
	   		    	outSourcingDao.saveDocument(doc);
	   		    	
	   		    	//Pago de ISN mensual
	   				/*doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("PAGO_ISN");
	   		    	doc.setContent(uploadedFiles.get(4).getBytes());
	   		    	doc.setType(uploadedFiles.get(4).getContentType());
	   		    	doc.setName(uploadedFiles.get(4).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(4).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("MONTH");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   				doc.setDocStatus(FISCAL_DOC_PENDING);
	   		    	outSourcingDao.saveDocument(doc);*/
	   		    	
	   				//Pago Provisional de IVA
	   				doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setRfc(s.getRfc());
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("PAGO_PROV_IVA");
	   		    	doc.setContent(uploadedFiles.get(3).getBytes());
	   		    	doc.setType(uploadedFiles.get(3).getContentType());
	   		    	doc.setName(uploadedFiles.get(3).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(3).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("MONTH");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   				doc.setDocStatus(FISCAL_DOC_PENDING);
	   		    	doc.setHistory(false);
	   		    	doc.setMonthLoad(monthLoad);
	   		    	doc.setYearLoad(yearLoad);
	   		    	outSourcingDao.saveDocument(doc);

//	   		    	Cédula de determinación de cuotas :
	   				doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setRfc(s.getRfc());
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("CEDU_DETERM_CUOTAS");
	   		    	doc.setContent(uploadedFiles.get(4).getBytes());
	   		    	doc.setType(uploadedFiles.get(4).getContentType());
	   		    	doc.setName(uploadedFiles.get(4).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(4).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("MONTH");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   		    	doc.setDocStatus(AppConstants.FISCAL_DOC_PENDING);
	   		    	doc.setMonthLoad(monthLoad);
	   		    	doc.setYearLoad(yearLoad);
	   		    	outSourcingDao.saveDocument(doc);
	   		    	
	   		    	s.setOutSourcingMonthlyAccept(true);
	   		    	supplierService.updateSupplierCore(s);

	   		    	logger.log(AppConstants.LOG_DOCUMENTS, "DOCUMENTOS MENSUALES PARA : " + s.getRazonSocial()); //, company
	   		    	
	   		    	String subject = OUTSOURCING_RECEIPT_SUBJECT_MONTH;
	   		    	subject = subject.replace("_SUPPLIER_", s.getRazonSocial());
	   		    	
	   		    	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(OUTSOURCING_RECEIPT_MESSAGE), s.getEmailSupplier());
	   				emailAsyncSup.setMailSender(mailSenderObj);
	   				Thread emailThreadSup = new Thread(emailAsyncSup);
	   				emailThreadSup.start();
	   		    	
	   		    	String internalEmail = "";
	   		    	UDC udc =  udcService.searchBySystemAndKey("OSAPPROVE", "ROLE_RH");
					if(udc != null) {
						internalEmail = udc.getStrValue1();
					}
					
					if(!"".equals(internalEmail)) {
						emailAsyncSup = new EmailServiceAsync();
		   		    	String content = OUTSOURCING_SEND_MESSAGE;
		   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
		   		    	content = content.replace("_DOCTYPE_", "MENSUAL");
		   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), internalEmail);
		   				emailAsyncSup.setMailSender(mailSenderObj);
		   				emailThreadSup = new Thread(emailAsyncSup);
		   				emailThreadSup.start();
					}
	   				
	   		    	internalEmail = "";
	   		    	udc =  udcService.searchBySystemAndKey("OSAPPROVE", "ROLE_TAX");
					if(udc != null) {
						internalEmail = udc.getStrValue1();
					}
					
					if(!"".equals(internalEmail)) {
						emailAsyncSup = new EmailServiceAsync();
		   		    	String content = OUTSOURCING_SEND_MESSAGE;
		   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
		   		    	content = content.replace("_DOCTYPE_", "MENSUAL");
		   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), internalEmail);
		   				emailAsyncSup.setMailSender(mailSenderObj);
		   				emailThreadSup = new Thread(emailAsyncSup);
		   				emailThreadSup.start();
					}
						   		    	
					logger.log(LOG_DOCUMENTS, "CARGA DE DOCUMENTOS MENSUALES DEL PROVEEDOR: " + s.getRazonSocial());
   				}
   			}
   	    	
   		}catch(Exception e) {
   			log4j.error("Exception" , e);
   			e.printStackTrace();
   			return e.getMessage();
   		}
   		
		return "";
	}

	public synchronized String saveQuarterlyDocs(MultiFileUploadBean multiFileUploadBean, String addressNumber) {
		
		List<MultipartFile> uploadedFiles = multiFileUploadBean.getUploadedFiles();
   		
		//Company c = companyService.searchByCompany(company);
		
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.MONTH, -1);
		 	
		int monthLoad = (calendar.get(Calendar.MONTH) + 1 );
		int yearLoad = calendar.get(Calendar.YEAR);
		
   		try {
   			if(uploadedFiles.size() == 2) {
   				
   				//Supplier s = supplierService.searchByAddressNumberAndCompany(addressNumber, company);
   				Supplier s = supplierService.searchByAddressNumber(addressNumber);
   				
   				if(s != null && !s.isOutSourcingQuarterlyAccept()) {
   					
   					String [] data = new PDFutils2().getPdfText(uploadedFiles.get(0).getBytes(), 6, 0);
   					if(data==null) {
      					 return "El formato del archivo Informativa de Contratos de Servicios u Obras Especializados es INVALIDO, favor de cargar el archivo en un formato PDF/texto";
   					}
   					
   					data = new PDFutils2().getPdfText(uploadedFiles.get(1).getBytes(), 6, 0);
   					if(data==null) {
      					 return "El formato del archivo Sistema de Información de Subcontratación es INVALIDO, favor de cargar el archivo en un formato PDF/texto";
   					}   								
   					
	   				//Informativa de Contratos de Servicios u Obras Especializados
   					//OutSourcingDocument doc = null;
   					OutSourcingDocument doc = new OutSourcingDocument();
   					//if(uploadedFiles.get(0).getSize() > 0) {
		   		    	//doc = new OutSourcingDocument();
		   				doc.setAddressBook(addressNumber);
		   				doc.setRfc(s.getRfc());
		   				doc.setSupplierName(s.getRazonSocial());
		   		    	doc.setDocumentType("ICSOE"); 
		   		    	doc.setContent(uploadedFiles.get(0).getBytes());
		   		    	doc.setType(uploadedFiles.get(0).getContentType());
		   		    	doc.setName(uploadedFiles.get(0).getOriginalFilename());
		   		    	doc.setSize(uploadedFiles.get(0).getSize());
		   		    	doc.setStatus(true);
		   		    	doc.setFolio("");
		   		    	doc.setUuid("");
		   		    	doc.setUploadDate(new Date());
		   		    	doc.setFrequency("QUARTER");
		   		    	doc.setCompany("");
		   		    	doc.setObsolete(false);
		   				doc.setDocStatus(AppConstants.FISCAL_DOC_PENDING);
		   		    	doc.setMonthLoad(monthLoad);
		   		    	doc.setYearLoad(yearLoad);
		   		    	outSourcingDao.saveDocument(doc);
		   		    	
   					//}
   					
		   		    //Sistema de Información de Subcontratación
   					//if(uploadedFiles.get(1).getSize() > 0) {
		   				doc = new OutSourcingDocument();
		   				doc.setAddressBook(addressNumber);
		   				doc.setRfc(s.getRfc());
		   				doc.setSupplierName(s.getRazonSocial());
		   		    	doc.setDocumentType("SISUB");
		   		    	doc.setContent(uploadedFiles.get(1).getBytes());
		   		    	doc.setType(uploadedFiles.get(1).getContentType());
		   		    	doc.setName(uploadedFiles.get(1).getOriginalFilename());
		   		    	doc.setSize(uploadedFiles.get(1).getSize());
		   		    	doc.setStatus(true);
		   		    	doc.setFolio("");
		   		    	doc.setUuid("");
		   		    	doc.setUploadDate(new Date());
		   		    	doc.setFrequency("QUARTER");
		   		    	doc.setCompany("");
		   		    	doc.setObsolete(false);
		   				doc.setDocStatus(AppConstants.FISCAL_DOC_PENDING);
		   		    	doc.setMonthLoad(monthLoad);
		   		    	doc.setYearLoad(yearLoad);
		   		    	outSourcingDao.saveDocument(doc);
		   		    	
   					//}
   					
	   		 	    s.setOutSourcingQuarterlyAccept(true);
	   		    	supplierService.updateSupplierCore(s);
	   		    	   		    	
	   		    	logger.log(AppConstants.LOG_DOCUMENTS, "DOCUMENTOS CUATRIMESTRALES PARA : " + s.getRazonSocial()); //company
	   		    	
	   		    	String subject = OUTSOURCING_RECEIPT_SUBJECT_QUARTER;
	   		    	subject = subject.replace("_SUPPLIER_", s.getRazonSocial());
	   		    	
	   		    	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(OUTSOURCING_RECEIPT_MESSAGE), s.getEmailSupplier());
	   				emailAsyncSup.setMailSender(mailSenderObj);
	   				Thread emailThreadSup = new Thread(emailAsyncSup);
	   				emailThreadSup.start();
	   				
	   				/*
	   				emailAsyncSup = new EmailServiceAsync();
	   		    	String content = OUTSOURCING_SEND_MESSAGE;
	   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
	   		    	content = content.replace("_DOCTYPE_", "CUATRIMESTRAL");
	   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), c.getNotificationEmail());
	   				emailAsyncSup.setMailSender(mailSenderObj);
	   				emailThreadSup = new Thread(emailAsyncSup);
	   				emailThreadSup.start();*/
	   				
	   		    	String internalEmail = "";
	   		    	UDC udc =  udcService.searchBySystemAndKey("OSAPPROVE", "ROLE_RH");
					if(udc != null) {
						internalEmail = udc.getStrValue1();
					}
					
					if(!"".equals(internalEmail)) {
						emailAsyncSup = new EmailServiceAsync();
		   		    	String content = OUTSOURCING_SEND_MESSAGE;
		   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
		   		    	content = content.replace("_DOCTYPE_", "CUATRIMESTRAL");
		   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), internalEmail);
		   				emailAsyncSup.setMailSender(mailSenderObj);
		   				emailThreadSup = new Thread(emailAsyncSup);
		   				emailThreadSup.start();
					}
	   				
	   		    	internalEmail = "";
	   		    	udc =  udcService.searchBySystemAndKey("OSAPPROVE", "ROLE_TAX");
					if(udc != null) {
						internalEmail = udc.getStrValue1();
					}
					
					if(!"".equals(internalEmail)) {
						emailAsyncSup = new EmailServiceAsync();
		   		    	String content = OUTSOURCING_SEND_MESSAGE;
		   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
		   		    	content = content.replace("_DOCTYPE_", "CUATRIMESTRAL");
		   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), internalEmail);
		   				emailAsyncSup.setMailSender(mailSenderObj);
		   				emailThreadSup = new Thread(emailAsyncSup);
		   				emailThreadSup.start();
					}
						   		    	
					logger.log(LOG_DOCUMENTS, "CARGA DE DOCUMENTOS CUATRIMESTRALES DEL PROVEEDOR: " + s.getRazonSocial());	   				  				
   				}
   			}
   	    	
   		}catch(Exception e) {
   			log4j.error("Exception" , e);
   			e.printStackTrace();
   			return e.getMessage();
   		}
   		
		return "";
	}	

	 public String saveInvoiceZipDoc(MultipartFile file, String addressNumber, String uuid, String periodo, boolean status) { // String company, 
	  		
			//Company c = companyService.searchByCompany(company);
			
	   		try {
				//Supplier s = supplierService.searchByAddressNumberAndCompany(addressNumber, company);				
   				Supplier s = supplierService.searchByAddressNumber(addressNumber);				
				
				int mes=Integer.parseInt( periodo.split(",")[0]);
				int ano=Integer.parseInt( periodo.split(",")[1]);
				if(s != null) {
					
					FiscalDocuments fd = fiscalDocumentService.getFiscalDocumentsByUuid(uuid);
		
	   				//Recibos de Nómina
	   				OutSourcingDocument doc = new OutSourcingDocument();
	   				doc.setMonthLoad(mes);
	   				doc.setYearLoad(ano);
	   				doc.setAddressBook(addressNumber);
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("REC_NOMINA");
	   		    	doc.setContent(file.getBytes());
	   		    	doc.setType(file.getContentType());
	   		    	doc.setName(file.getOriginalFilename());
	   		    	doc.setSize(file.getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio(fd.getFolio());
	   		    	doc.setUuid(fd.getUuidFactura());
	   		    	doc.setOrderNumber(0);
	   		    	doc.setOrderType("");
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("INV");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   		    	doc.setDocStatus(AppConstants.FISCAL_DOC_APPROVED); // AppConstants.FISCAL_DOC_PENDING
	   		    	//doc.setCompany(company);
	   		    	outSourcingDao.saveDocument(doc);
	   		    	
	   		    	UserDocument usrDoc = new UserDocument();
	   		    	usrDoc.setAccept(true);
	   		    	usrDoc.setAddressBook(addressNumber);
	   		    	usrDoc.setContent(file.getBytes());
	   		    	usrDoc.setDescription("Recibos Timbrados");
	   		    	usrDoc.setDocumentNumber(0);
	   		    	usrDoc.setDocumentType("");
	   		    	usrDoc.setFiscalRef(0);
	   		    	usrDoc.setFiscalType("REC_NOMINA");
	   		    	usrDoc.setFolio(fd.getFolio());
	   		    	usrDoc.setSerie(fd.getSerie());
	   		    	usrDoc.setUuid(fd.getUuidFactura());
	   		    	usrDoc.setName(file.getOriginalFilename());
	   		    	usrDoc.setSize(file.getSize());
	   		    	usrDoc.setType(file.getContentType());
	   		    	usrDoc.setUploadDate(new Date());
	   		    	//usrDoc.setCompany(company);
					documentsDao.saveDocuments(usrDoc);
					
					fd.setApprovalStatus(AppConstants.FISCAL_DOC_APPROVED); // AppConstants.FISCAL_DOC_PENDING
					fd.setStatus(AppConstants.FISCAL_DOC_APPROVED); // AppConstants.FISCAL_DOC_PENDING
					//fiscalDocumentService.updateFiscalDocuments(fd);
					
					logger.log(AppConstants.LOG_DOCUMENTS, "RECIBOS DE NÓMINA PARA : " + s.getRazonSocial() + " Factura(UUID) " + usrDoc.getUuid()); //, company
					
					String subject = OUTSOURCING_RECEIPT_SUBJECT;
	   		    	subject = subject.replace("_SUPPLIER_", s.getRazonSocial());
	   		    	
	   		    	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(OUTSOURCING_RECEIPT_MESSAGE), s.getEmailSupplier());
	   				emailAsyncSup.setMailSender(mailSenderObj);
	   				Thread emailThreadSup = new Thread(emailAsyncSup);
	   				emailThreadSup.start();
	   				/*
	   				emailAsyncSup = new EmailServiceAsync();
	   		    	String content = OUTSOURCING_SEND_MESSAGE;
	   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
	   		    	content = content.replace("_DOCTYPE_", "DE RECIBOS TIMBRADOS");
	   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), c.getNotificationEmail());
	   				emailAsyncSup.setMailSender(mailSenderObj);
	   				emailThreadSup = new Thread(emailAsyncSup);
	   				emailThreadSup.start(); */
						   				
	   		    	String internalEmail = "";
	   		    	UDC udc =  udcService.searchBySystemAndKey("OSAPPROVE", "ROLE_RH");
					if(udc != null) {
						internalEmail = udc.getStrValue1();
					}
					
					if(!"".equals(internalEmail)) {
						emailAsyncSup = new EmailServiceAsync();
		   		    	String content = OUTSOURCING_SEND_MESSAGE;
		   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
		   		    	content = content.replace("_DOCTYPE_", "DE RECIBOS TIMBRADOS");
		   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), internalEmail);
		   				emailAsyncSup.setMailSender(mailSenderObj);
		   				emailThreadSup = new Thread(emailAsyncSup);
		   				emailThreadSup.start();
					}
	   				
	   		    	internalEmail = "";
	   		    	udc =  udcService.searchBySystemAndKey("OSAPPROVE", "ROLE_TAX");
					if(udc != null) {
						internalEmail = udc.getStrValue1();
					}
					
					if(!"".equals(internalEmail)) {
						emailAsyncSup = new EmailServiceAsync();
		   		    	String content = OUTSOURCING_SEND_MESSAGE;
		   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
		   		    	content = content.replace("_DOCTYPE_", "DE RECIBOS TIMBRADOS");
		   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), internalEmail);
		   				emailAsyncSup.setMailSender(mailSenderObj);
		   				emailThreadSup = new Thread(emailAsyncSup);
		   				emailThreadSup.start();
					}	   					   				
	   				
				}
	   		}catch(Exception e) {
	   			log4j.error("Exception" , e);
	   			e.printStackTrace();
	   			return e.getMessage();
	   		}
	   		
			return "";
		}
	 		
	public synchronized String saveReplacementFile(MultipartFile file, String addressNumber, int id) {
		
		try {
			Supplier s = supplierService.searchByAddressNumber(addressNumber);			
			if(s != null) {
				
				OutSourcingDocument doc = outSourcingDao.getDocumentById(id);
				String currentName = doc.getName();
				doc.setNotes(doc.getNotes() + " / Se ha reemplazado un nuevo archivo para el anterior denominado " + doc.getName());
				doc.setName(file.getOriginalFilename());
				doc.setSize(file.getSize());
				doc.setStatus(true);
				doc.setType(file.getContentType());
				doc.setUploadDate(new Date());
				doc.setDocStatus(FISCAL_DOC_PENDING);
				doc.setContent(file.getBytes());
				outSourcingDao.updateDocument(doc);

				String subject = OUTSOURCING_RECEIPT_SUBJECT;
   		    	subject = subject.replace("_SUPPLIER_", s.getRazonSocial());
   				
   				EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
   		    	String content = OUTSOURCING_SEND_MESSAGE;
   		    	content = content.replace("_SUPPLIER_", s.getRazonSocial());
   		    	content = content.replace("_DOCTYPE_", "DE REEMPLAZO POR RECHAZO");
   				emailAsyncSup.setProperties(subject, stringUtils.prepareEmailContent(content), s.getEmailSupplier());
   				emailAsyncSup.setMailSender(mailSenderObj);
   				Thread emailThreadSup = new Thread(emailAsyncSup);
   				emailThreadSup.start();
   				
				logger.log(LOG_DOCUMENTS, "REEMPLAZO DE ARCHIVO DEL PROVEEDOR : " + addressNumber + ". Original: " + currentName + " - Reemplazo: " + doc.getName());
			}
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return e.getMessage();
		}
		
		return "";
	}

    public synchronized String saveInvoiceDocs(MultiFileUploadBean multiFileUploadBean, String addressNumber, int orderNumber, String orderType) {
  		
		List<MultipartFile> uploadedFiles = multiFileUploadBean.getUploadedFiles();
   		
   		try {
   			if(uploadedFiles.size() == 1) {
   				
   				Supplier s = supplierService.searchByAddressNumber(addressNumber);
   				if(s != null) {
   					
	   				//Recibos de Nómina
	   				OutSourcingDocument doc = new OutSourcingDocument();
	   				doc.setAddressBook(addressNumber);
	   				doc.setSupplierName(s.getRazonSocial());
	   		    	doc.setDocumentType("REC_NOMINA");
	   		    	doc.setContent(uploadedFiles.get(0).getBytes());
	   		    	doc.setType(uploadedFiles.get(0).getContentType());
	   		    	doc.setName(uploadedFiles.get(0).getOriginalFilename());
	   		    	doc.setSize(uploadedFiles.get(0).getSize());
	   		    	doc.setStatus(true);
	   		    	doc.setFolio("");
	   		    	doc.setUuid("");
	   		    	doc.setOrderNumber(orderNumber);
	   		    	doc.setOrderType(orderType);
	   		    	doc.setUploadDate(new Date());
	   		    	doc.setFrequency("INV");
	   		    	doc.setCompany("");
	   		    	doc.setObsolete(false);
	   		    	outSourcingDao.saveDocument(doc);
	   		    	 
   				}
   			}
   	    	
   		}catch(Exception e) {
   			log4j.error("Exception" , e);
   			e.printStackTrace();
   			return e.getMessage();
   		}
   		
		return "";
	}



	//@Scheduled(cron="0 0 22 * * *") // Valida si no ha cargado documentos iniciales
	public void searchBaseLineDocs() {
		
		List<Supplier> supList = supplierService.searchByOutSourcingStatus();
		if(supList != null) {
			for(Supplier s : supList) {
				List<OutSourcingDocument> baseDocList = outSourcingDao.searchDocsByFrequency(s.getAddresNumber(), "BL");
				if(baseDocList == null || baseDocList.size() <=0) {
					s.setOutSourcingAccept(false);
					supplierService.updateSupplierCore(s);
				}
			}
		}	
	}
	
	//@Scheduled(cron="0 0/40 13 17-28 * *") // Valida si despues de los 17 días de cada mes tiene documentos pendientes
	public void searchMonthlyDocs() {
		
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int currentMonth = cal.get(Calendar.MONTH);
		
		List<Supplier> supList = supplierService.searchByOutSourcingStatus();
		if(supList != null) {
			for(Supplier s : supList) {
				if(s.isOutSourcingAccept()) {
					List<OutSourcingDocument> baseDocList = outSourcingDao.searchActiveDocsByFrequency(s.getAddresNumber(), "MONTH");
					if(baseDocList == null || baseDocList.size() <=0) {
						s.setOutSourcingMonthlyAccept(false);
						supplierService.updateSupplierCore(s);
						
						EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
		   				emailAsyncSup.setProperties(OUTSOURCING_ALERT_SUBJECT, stringUtils.prepareEmailContent(OUTSOURCING_ALERT_MESSAGE), s.getEmailSupplier());
		   				emailAsyncSup.setMailSender(mailSenderObj);
		   				Thread emailThreadSup = new Thread(emailAsyncSup);
		   				emailThreadSup.start();
						
					}else {
						boolean isObsolete = false;
						for(OutSourcingDocument doc : baseDocList) {					
							cal.setTime(doc.getUploadDate());
							int documentMonth = cal.get(Calendar.MONTH);
							if(currentMonth == 1 && documentMonth == 12) {
								isObsolete = true;
								doc.setObsolete(true);
								outSourcingDao.updateDocument(doc);
							}else {
								if(documentMonth < currentMonth) {
									isObsolete = true;
									doc.setObsolete(true);
									outSourcingDao.updateDocument(doc);
								}
							}
							
						}
						
						if(isObsolete) {
							s.setOutSourcingMonthlyAccept(false);
							supplierService.updateSupplierCore(s);
							
							EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			   				emailAsyncSup.setProperties(OUTSOURCING_ALERT_SUBJECT, stringUtils.prepareEmailContent(OUTSOURCING_ALERT_MESSAGE), s.getEmailSupplier());
			   				emailAsyncSup.setMailSender(mailSenderObj);
			   				Thread emailThreadSup = new Thread(emailAsyncSup);
			   				emailThreadSup.start();
						}
					}
				}
			}
		}	
	}
	
	//@Scheduled(cron="0 0/30 13 5-10 * *") // Notifica los primeros días del mes
	public void searchMonthlyDocsNotification() {
		
		List<Supplier> supList = supplierService.searchByOutSourcingStatus();
		if(supList != null) {
			for(Supplier s : supList) {
				if(s.isOutSourcingAccept() && !s.isOutSourcingMonthlyAccept()) {
					EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
	   				emailAsyncSup.setProperties(OUTSOURCING_NOTIF_SUBJECT, stringUtils.prepareEmailContent(OUTSOURCING_NOTIF_MESSAGE), s.getEmailSupplier());
	   				emailAsyncSup.setMailSender(mailSenderObj);
	   				Thread emailThreadSup = new Thread(emailAsyncSup);
	   				emailThreadSup.start();
				}
			}
		}		
	}
	
	public List<OutSourcingDocument> searchDocsByQuery(String supplierName, String status, String documentType, 
			                                           String supplierNumber, List<String> docTypeList, int start, int limit, int monthLoad, int yearLoad){
		
		 List<OutSourcingDocument> list = outSourcingDao.searchDocsByQuery(supplierName, status, documentType, supplierNumber, docTypeList, start, limit, monthLoad, yearLoad);
		 if(list != null) {
			 for(OutSourcingDocument doc : list) {
				 doc.setContent(null);
			 }
			 return list;
		}else { 
			return null;
		}
	}
	

	public int searchDocsByQueryCount(String supplierName, String status, String documentType, String supplierNumber, List<String> docTypeList, int monthLoad, int yearLoad){
		return outSourcingDao.searchDocsByQueryCount(supplierName, status, documentType, supplierNumber, docTypeList, monthLoad, yearLoad);
	}
	


	public List<OutSourcingDocument> searchCriteria(String query){
		return outSourcingDao.searchCriteria(query);
	}
	
	public List<OutSourcingDocument> searchByAttachID(String attachId){
		return outSourcingDao.searchByAttachID(attachId);
	}
	
	public OutSourcingDocument saveDocument(OutSourcingDocument o) {
		return outSourcingDao.saveDocument(o);
	}

	public void updateDocument(OutSourcingDocument o) {
		outSourcingDao.updateDocument(o);
	}

	public void updateDocumentList(List<OutSourcingDocument> list) {
		outSourcingDao.updateDocumentList(list);
	}
	
	public void deleteDocument(int id) {
		outSourcingDao.deleteDocument(id);
	}
	
	public int getTotalRecords(){
		return outSourcingDao.getTotalRecords();
	}
	
	public String validateInvoiceCodes(List<Concepto> cList) {

		String textResult = "";
		List<String> osValidList = new ArrayList<String>();
		List<CodigosSAT> osList = codigosSATService.searchByTipoCodigo("OUTSOURCING");
		if(osList != null) {
			if(osList.size() > 0) {
				for(CodigosSAT cs : osList) {
					osValidList.add(cs.getCodigoSAT());
				}
			}
		}
		
		if(osValidList.size() > 0) {
			if(cList != null) {
				for(Concepto concepto : cList) {
					if(osValidList.contains(concepto.getClaveProdServ())) {
						textResult = "La factura contiene claves de producto servicio para servicios especializados";
						break;
					}
				}
			}
		}
		
		return textResult;
		
	}
	
	public InvoiceDTO getInvoiceXmlFromString(String xmlContent){
		try{
			InvoiceDTO dto = null;
			if(xmlContent.contains(AppConstants.NAMESPACE_CFDI_V4)) {
				dto = xmlToPojoService.convertV4(xmlContent);
			} else {
				dto = xmlToPojoService.convert(xmlContent);
			}
			return dto;
		}catch(Exception e){
			log4j.error("Exception" , e);
			return null;
		}
	}
	
	 public String getPendingDocuments(String addressNumber) {
		 List<OutSourcingDocument> pendingDocuments = outSourcingDao.searchPendingDocuments(addressNumber);
		 StringBuilder strb = new StringBuilder();
		 if(pendingDocuments != null) {
			 if(pendingDocuments.size() > 0) {
				 for(OutSourcingDocument doc : pendingDocuments) {
					 strb.append(doc.getName().trim());
					 strb.append(", ");
				 }
			 }
		 }
		 return strb.toString();
	 }

		public int savingODDDCFromPDF(byte[] file, int id) {
			String [] pages = new PDFutils2().getPdfText(file, 5, 0);
		 	int guardados = 0;
		 	int cabecero = 0;
		    if(pages!=null){
		    	CDDDC cabezal = new CDDDC();
		    	for (int x=0; x < pages.length; x++){
		    		int nopag = Integer.parseInt(pages[x]);
					ArrayList<String> clave = new ArrayList<String>();
					ArrayList<String> nombre = new ArrayList<String>(); 
				 	ArrayList<String> curp = new ArrayList<String>();
				 	ArrayList<String> cUbicacion = new ArrayList<String>();
		 			String [] data2 = new PDFutils2().getPdfText(file, 2, nopag);
		 		 	String [] data3 = new PDFutils2().getPdfText(file, 3, nopag);
		 		 	String [] data4 = new PDFutils2().getPdfText(file, 4, nopag);
		 		 	if(data2!=null) {
		 		 		if(data2[2].contains("EXTEMPORÁNEO")) {	    				
			 		    		if (cabecero == 0) {
			 		    			cabezal.setFechaDeProceso( (data2[3].replace("Fecha de Proceso:","")).trim() );
			 		    			if(data2[4].contains("Período de Proceso:")) {
				 		    			cabezal.setPeriododeProceso( ((data2[4].replace("Período de Proceso:","")).replace("Calculo Extemporaneo al:", "")).replace("T. D.", "").trim() );
				 		    			cabezal.setRfc( ((((data2[8].replace("Area Geográfica:","")).replace("RFC:", "")).replace(" ","")).trim()).substring(14) );
				 		    			cabezal.setRegistroPatronal( ((data2[8].replace("Registro Patronal:","")).trim()).substring(0,14) );
				 		    			cabezal.setRazonSocial( (data2[12].replace("Nombre o Razón Social:","")).trim() );
			 		    			} else {
			 		    				if(data2[5].contains("Período de Proceso:")) {
					 		 				String aux = (data2[5].replace("Período de Proceso:","")).replace("Calculo Extemporaneo al:", ""); 		 			
					 		 				aux = (aux.replace(aux.substring( aux.indexOf("T. D.", 0), aux.length()),"")).trim();
					 		 				cabezal.setPeriododeProceso(aux);
					 		 				cabezal.setRegistroPatronal(((data2[9].replace("Registro Patronal:","")).trim()).substring(0,14));
					 		 				cabezal.setRfc(((((data2[9].replace("Area Geográfica:","")).replace("RFC:", "")).replace(" ","")).trim()).substring(14));
					 		 				cabezal.setRazonSocial((data2[13].replace("Nombre o Razón Social:","")).trim());
			 		    				}
			 		    			}		 		    					    			
			 		    			cabezal.setFechaProcess(new Date());
			 		    			cabezal.setIdOutsourcingDocument(id);
			 		    			cabezal.setId(0);
			 		    			cdddcDao.save(cabezal);	
			 		    			cabecero = 1;
			 		    		}	 		    			 		    		
		 		 		} else { 
		 		 			
		 		 			if(data2[2].contains("OBRERO-PATRONALES, APORTACIONES Y AMORTIZACIONES")) {
		 		 				if (cabecero == 0) {
			 		 				String aux = (data2[3].replace("Bimestre de Proceso:","")).trim();
			 		 				String replaceString = aux.replace(aux.substring( aux.indexOf("Fecha de Proceso:", 0)+17, aux.length()),"");
			 		 				cabezal.setFechaDeProceso( (aux.substring( aux.indexOf("Fecha de Proceso:", 0)+17, aux.length())).trim() );
			 		 				
			 		 				cabezal.setFechaProcess(new Date());
			 		    			cabezal.setIdOutsourcingDocument(id);
			 		    			
			 		    			cabezal.setPeriododeProceso( (replaceString.replace("Fecha de Proceso:","")).trim() );
			 		    			
			 		 				aux = (data2[4].replace("Registro Patronal:","")).trim();
			 		 				replaceString = aux.replace(aux.substring(aux.indexOf("RFC:", 0)+4, aux.length()), "");

			 		 				String rfc = aux.replace(aux.substring(aux.indexOf("Area", 0), aux.length()), "");
			 		    			cabezal.setRfc( (rfc.substring(rfc.indexOf("RFC:", 0)+4, rfc.length())).trim() );

			 		    			cabezal.setRegistroPatronal( (replaceString.replace("RFC:","")).trim() );

			 		    			replaceString = data2[5].replace(data2[5].substring(data2[5].indexOf("Delegación", 0), data2[5].length()), "");
			 		    			cabezal.setRazonSocial( (replaceString.replace("Nombre o Razón Social:","")).trim() );
		 		 					
			 		    			cdddcDao.save(cabezal);	
			 		    			cabecero = 1;
		 		 				}

		 		 			} else {
			 		    		if(data2[1].contains("CÉDULA DE DETERMINACIÓN DE CUOTAS")){
				 		    		if (cabecero == 0) {
				 		    			
					 			    	if ((data2[2].indexOf("Fecha", 0)+17) == (data2[2].length()-1)){
					 		    			cabezal.setPeriododeProceso( (data2[2].replace("Período de Proceso:","")).replace("Fecha de Proceso:", "").trim() );
					 		    			cabezal.setFechaDeProceso( (data2[3].replace("Fecha de Proceso:","")).trim() );
					 			    	} else {
					 			    		if ((data2[2].length()-1) > (data2[2].indexOf("Fecha", 0)+17)) {
					 			    			
					 			    			cabezal.setFechaDeProceso((data2[2].substring((data2[2].indexOf("Fecha", 0)+17), data2[2].length())).trim());
					 			    							 			    			
					 			    			if (data2[2].contains("Nombre o Razón Social:")) {			 			    			
						 			    			cabezal.setPeriododeProceso(data2[2].substring((data2[2].indexOf("Período", 0)+19),(data2[2].indexOf("Fecha", 0))).trim());
					 			    			} else {
					 			    				if (data2[3].contains("Período de Proceso:")) {					 			    			
							 			    			cabezal.setPeriododeProceso( (data2[3].replace("Período de Proceso:","")).trim() );
					 			    				} else {
					 			    					if (data2[2].contains("Período de Proceso:")) {
					 			    						cabezal.setPeriododeProceso((data2[2].substring((data2[2].indexOf("Período de Proceso:", 0)+19), (data2[2].indexOf("Fecha", 0)))).trim());
					 			    					}
					 			    				}
					 			    			}
					 			    						 			    			
					 			    		}
					 			    	}
				 		    						 		    						 		    					 		    			
						 			    
										if (data2[4].contains("Nombre o Razón Social:")) {
											if (data2[4].contains("Delegación IMSS:")) {
												cabezal.setRazonSocial((data2[4].substring((data2[4].indexOf("Nombre o Razón Social:", 0)+22), (data2[4].indexOf("Delegación IMSS:", 0)))).trim());
											} else {
												cabezal.setRazonSocial((data2[4].replace("Nombre o Razón Social:","")).trim());
											}
											if (data2[3].contains("Registro Patronal:")) {
							 			    	String aux1 = (data2[3].replace("Registro Patronal:","")).trim();	 		 			    				 			    	
							 			    	aux1 = aux1.replace(aux1.substring(aux1.indexOf("Area", 0), aux1.length()),"");			 			    							 			    	
						 		    			cabezal.setRfc((aux1.substring(aux1.indexOf("RFC:", 0)+4, aux1.length())).trim());
						 		    			cabezal.setRegistroPatronal((aux1.substring(0, aux1.indexOf("RFC:", 0))).trim());
											}
										} else {
					 		    			if (data2[5].contains("Nombre o Razón Social:")) {
						 			    		cabezal.setRazonSocial( (data2[5].replace("Nombre o Razón Social:","")).trim() );
						 			    	} else {
						 			    		if (data2[6].contains("Nombre o Razón Social:") && data2[6].length() > 24) {
								 			    	cabezal.setRazonSocial( (data2[6].replace("Nombre o Razón Social:","")).trim() );
						 			    		}
												if (data2[6].contains("Nombre o Razón Social:") && data2[6].length() <= 23) {
													cabezal.setRazonSocial( data2[8].trim() );
												}
						 			    	}
						 			    	if (data2[7].contains("Nombre o Razón Social:")) {
							 			    	cabezal.setRazonSocial( (data2[7].replace("Nombre o Razón Social:","")).trim() );
						 			    	}
					 		    			String aux1 = (data2[4].replace("Registro Patronal:","")).trim();	
					 		    			aux1 = aux1.replace(aux1.substring(aux1.indexOf("Area", 0), aux1.length()),"");
					 		    			cabezal.setRfc((aux1.substring(aux1.indexOf("RFC:", 0)+4, aux1.length())).trim());
					 		    			cabezal.setRegistroPatronal((aux1.substring(0, aux1.indexOf("RFC:", 0))).trim());			 		    					 		    			
										}				 		    				 		    				 		    			
				 		    			cabezal.setFechaProcess(new Date());
				 		    			cabezal.setIdOutsourcingDocument(id);
				 		    			cabezal.setId(0);
				 		    			cdddcDao.save(cabezal);	
				 		    			cabecero = 1;
				 		    		}
			 		    		}
			 		    			    		
		 		 				if(data2[2].contains("CÉDULA DE DETERMINACIÓN DE CUOTAS")) {				
			 		 				if (cabecero == 0) {
				 		 				cabezal.setFechaProcess(new Date());
				 		    			cabezal.setIdOutsourcingDocument(id);	 		 					 		    					    				 		    					 		    					 		    		 		 					 		 				 		 					
		 		 					if ((data2[3].length()-1) == (data2[3].indexOf("Fecha", 0)+17)) {
		 		 						cabezal.setPeriododeProceso( (data2[3].replace("Período de Proceso:","")).replace("Fecha de Proceso:", "").trim() ); 
					 			    	cabezal.setFechaDeProceso(data2[4].trim());
		 		 					}
		 		 					if (data2[5].contains("Registro Patronal:")) {
					 			    	String aux = (data2[5].replace("Registro Patronal:","")).trim();	
					 			    	cabezal.setRegistroPatronal( (aux.replace(aux.substring(aux.indexOf("RFC:", 0), aux.length()), "")).trim() );
					 			    	aux = aux.replace(aux.substring(aux.indexOf("Area", 0), aux.length()), "");
					 			    	cabezal.setRfc( (aux.substring(aux.indexOf("RFC:", 0)+4, aux.length())).trim() );
		 		 					}
		 		 					if (data2[8].contains("Nombre o Razón Social:")) {
		 		 						cabezal.setRazonSocial( (data2[8].replace("Nombre o Razón Social:","")).trim() );
		 		 					}
		 		 					
			 		    			cdddcDao.save(cabezal);	
			 		    			cabecero = 1;		 		 					
			 		 				} 		 						 		 						 		 						 		 						 		 					
		 		 				}
			 		    		 		 				 		 				 		 					 		 					 		    			 		    		 		    		
		 		 			}

		 		    	}		
		 		 	}
		 		 	
					if(data3!=null) {
					 	for (int i=0; i<data3.length; i++){	 	
					 		if (data3[i].length() >= 15) {
					 			if ( org.apache.commons.lang3.StringUtils.countMatches(data3[i].substring(0,15), "-") == 4){ 			 				
					 				clave.add(data3[i].substring(0,15));
					 				nombre.add(data3[i].substring(15).trim()); 			 				
					 			}	
					 		}

					 	}		
					}	
					
				 	if(data4!=null) {
				 		for (int i=0; i<data4.length; i++){	 				 			
				 			if ( data4[i].length() >= 19 ) {			 				
		 			 			if ( org.apache.commons.lang3.StringUtils.countMatches(data4[i].substring(0,12), "-") == 2){	
		 			 				if ( org.apache.commons.lang3.StringUtils.countMatches(data4[i].substring(0,15), " ") >= 1) {
		 			 					curp.add(data4[i].substring(0,12));
		 	 			 				cUbicacion.add(data4[i].substring(12).trim());			
		 			 				} else {
		 			 					curp.add(data4[i].substring(0,15));
		 			 					cUbicacion.add(data4[i].substring(15).trim());
		 			 				}	 			 				
	 	 			 			} else {
	 	 			 				if ( org.apache.commons.lang3.StringUtils.countMatches(data4[i].substring(0,18), " ") == 0){
	 	 			 					curp.add(data4[i].substring(0,18));
	 	 			 					cUbicacion.add(data4[i].substring(18).trim());
	 	 			 				}
	 	 			 			}			 				
				 			} else {
				 				if (data4[i].length() == 16) {
				 					if (org.apache.commons.lang3.StringUtils.countMatches(data4[i].substring(0,15), "-") == 2) {
				 						curp.add(data4[i].trim());
	 	 			 					cUbicacion.add("");
	 	 			 				}
				 				} else {
					 				if (data4[i].length() >= 12) {
			 			 				if ( org.apache.commons.lang3.StringUtils.countMatches(data4[i].substring(0,12), "-") >= 1){
			 			 					curp.add(data4[i].substring(0,12));
			 			 					cUbicacion.add(data4[i].substring(12).trim());
			 			 				}
					 				}
				 				}

				 			}	
				 		}		
				 	}
				 	
	 			    for (int z=0; z<clave.size(); z++) {
		 				CDDDCEmployee eploy = new CDDDCEmployee();
		 				eploy.setNumSegSoci( clave.get(z) );
		 				eploy.setRfcCurp( curp.get(z) );
		 				eploy.setClaveUbicacion( cUbicacion.get(z) );
		 				eploy.setNombre( nombre.get(z) );
		 				eploy.setCddc(cabezal);
						cdddcEmployeeDao.save(eploy);
						guardados = guardados + 1;
				    }
		    	}	
		    }  
		    return guardados;
		}	
	 
	 public void saveCompanyDocument(FileUploadBean uploadItem, String docType, String company) {
			
			
			UserDocument doc = documentsDao.searchCriteriaByAddressBookAndType(docType, company);
			if(doc != null) {
				documentsDao.deleteDocuments(doc.getId());
				doc = new UserDocument();
			}else {
				doc = new UserDocument();
			}
			
			String description ="";
			if("application/x-pkcs12".equals(uploadItem.getFile().getContentType())) {
				description ="Archivo de seguridad proveniente de FIEL";
			}
			
			doc.setName(uploadItem.getFile().getOriginalFilename());
			doc.setType(uploadItem.getFile().getContentType());
			doc.setAccept(true);
			doc.setDescription(description);
			doc.setFolio("");
			doc.setSerie("");
			doc.setFiscalRef(0);
			doc.setUuid("");
			doc.setUploadDate(new Date());
			doc.setDocumentNumber(0);
			doc.setDocumentType("");
			doc.setFiscalType(docType);
			doc.setAddressBook(company);
			doc.setSize(uploadItem.getFile().getSize());
			doc.setContent(uploadItem.getFile().getBytes());
			documentsDao.saveDocuments(doc);// TODO Auto-generated method stub
		}

	 	public List<CDDDCEmployee> getListTemp(String periodo){
	 		return cdddcEmployeeDao.getListNominal(periodo);
	 	}	 

	 	public ArrayList<String> getListnames(byte[] file) {
	 		ArrayList<String> listnamesNomina=new ArrayList<>();
	 		ByteArrayInputStream stream = new  ByteArrayInputStream(file);
	 		ZipInputStream zis = new ZipInputStream(stream);
	 		ZipEntry ze=null;
	 		try {
	 			while (( ze = zis.getNextEntry()) != null) { System.out.println("ze.getName(): " + ze.getName());
	 				String fileName = ze.getName().replace('á','a').replace('é','e').replace('í','i').replace('ó','o').replace('ú','u')+" "+FilenameUtils.getBaseName(ze.getName().replace('á','a').replace('é','e').replace('í','i').replace('ó','o').replace('ú','u'));
	 			if(ze.getName().replace('á','a').replace('é','e').replace('í','i').replace('ó','o').replace('ú','u').startsWith("xml/")||ze.getName().replace('á','a').replace('é','e').replace('í','i').replace('ó','o').replace('ú','u').toLowerCase().endsWith("xml")) {
	 				log4j.info(fileName);	System.out.println("fileName: " + fileName);
	 				List<String> xmlStrArray = new ArrayList<String>();
	 				InputStream in = zis;
	 				ByteArrayOutputStream out = new ByteArrayOutputStream();
	 				int c;
	 				while ((c = in.read()) != -1)
	 					out.write(c);
//	 				in.close();
	 				xmlStrArray.add(new String(out.toByteArray(), Charset.forName("utf-8")));
	 				String tempo="";
	 				for (String string : xmlStrArray) {
	 					tempo=tempo+string;
	 				}
	 				
	 				JSONObject json = XML.toJSONObject(tempo);
	 				try {
	 					String jsonString = json.getJSONObject("cfdi:Comprobante").getJSONObject("cfdi:Receptor").getString("Nombre");
	 					listnamesNomina.add(jsonString.trim());
	 				} catch (Exception e) {	System.out.println("Error grave en getListnames...");
	 					log4j.error("Exception" , e);
	 					// TODO: handle exception
	 				}
	 				
	 			}
	 			
	 			}
	 		} catch (IOException e) {
	 			log4j.error("Exception" , e);
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	 		return listnamesNomina;
	 	}
	 	
}
