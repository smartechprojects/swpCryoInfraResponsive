package com.eurest.supplier.async;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import com.eurest.supplier.dto.ForeingInvoice;
import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.dto.ZipElementDTO;
import com.eurest.supplier.model.PaymentCalendar;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.ReceiptInvoice;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.service.DocumentsService;
import com.eurest.supplier.service.EDIService;
import com.eurest.supplier.service.EmailServiceAsync;
import com.eurest.supplier.service.FTPService;
import com.eurest.supplier.service.HTTPRequestService;
import com.eurest.supplier.service.PaymentCalendarService;
import com.eurest.supplier.service.PurchaseOrderService;
import com.eurest.supplier.service.UdcService;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.Logger;
import com.eurest.supplier.util.NullValidator;
import com.eurest.supplier.util.PayloadProducer;
import com.eurest.supplier.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ProcessBatchInvoice implements Runnable{

	static String TIMESTAMP_DATE_PATTERN = "yyyy-MM-dd";
	private UdcService udcService;
	private Supplier s;
	private PurchaseOrder po;
	List<Map<String, ZipElementDTO>> fileList;
	private EDIService eDIService;
	private HTTPRequestService hTTPRequestService;
	private JavaMailSender mailSenderObj;
	private StringUtils stringUtils;
	private PurchaseOrderService purchaseOrderService;
	private PaymentCalendarService paymentCalendarService;
	private DocumentsService documentsService;
	private Logger logger;
	private int documentNumber;	
	private String documentType;
	private String tipoComprobante;
	private List<Receipt> requestedReceiptList;
	private double invoiceAmount;
	private Users user;
	private FTPService ftpService;
	
    private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(ProcessBatchInvoice.class);
	
	@Override
	public void run() {
		
		String validationMessage = "";
		String errorMessage = "";
		long startTime = System.nanoTime();		
		boolean isValidateOnSAT = false;
		
		UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");		
		if(udcCfdi != null) {
			if(!"".equals(udcCfdi.getStrValue1())) {
				if("TRUE".equals(udcCfdi.getStrValue1())) {
					isValidateOnSAT = true;
				}
			}
		}else {
			isValidateOnSAT = true;
		}
		
		String domesticCurrency = AppConstants.DEFAULT_CURRENCY;
		List<UDC> comUDCList =  udcService.searchBySystem("COMPANYDOMESTIC");
		if(comUDCList != null && !comUDCList.isEmpty()) {
			for(UDC company : comUDCList) {
				if(company.getStrValue1().trim().equals(po.getOrderCompany().trim()) && !"".equals(company.getStrValue2().trim())) {
					domesticCurrency = company.getStrValue2().trim();
					break;
				}
			}
		}
		
		//Obtiene días de crédito del proveedor
		int diasCred = 60;
		if(s.getDiasCredito() != null) {
			diasCred = Integer.valueOf(s.getDiasCredito());
		}
		
		//Obtiene Email del proveedor
		String emailRecipient = s.getEmailSupplier();
		
		//Obtiene fecha del primer día del año
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, currentYear);
		cal.set(Calendar.DAY_OF_YEAR, 1);  
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date startYear = cal.getTime();
		
		//Obtiene fecha de pago estimada
		Date estimatedPaymentDate = null;
		Date currentDate = new Date();
		if(currentDate != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, diasCred);
			List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(c.getTime(), 0, 500, po.getAddressNumber());
			if(pc != null) {
				if(pc.size() > 0) {
					estimatedPaymentDate = pc.get(0).getPaymentDate();
				}
			}else {
				estimatedPaymentDate = c.getTime();
			}
		}
		
		List<UDC> supExclList =  udcService.searchBySystem("NOCHECKSUP");
		UDC udcDate = udcService.searchBySystemAndKey(AppConstants.NO_VALIDATE_DATE, "SKIP");
		List<UDC> udcUsoCFDIExceptionList =  udcService.searchBySystem("CFDIEXC");
		List<UDC> receptores = udcService.searchBySystem("RECEPTOR");
		String currentInvoiceUUID = "";
		
		//Valida todas las facturas
		for(Map<String, ZipElementDTO> o : fileList) {
			if(!"".equals(validationMessage)) {
				break;
			} else {
				for (Map.Entry<String,ZipElementDTO> entry : o.entrySet())  {					
					ZipElementDTO zElement = entry.getValue();
					currentInvoiceUUID = zElement.getInvoiceDTO().getUuid();
					log4j.info("Procesando factura (UUID): " + currentInvoiceUUID);
					
					//Validación del comprobante con el servicio del SAT
					if(isValidateOnSAT) {
						String vcfdi = validaComprobanteSAT(zElement.getInvoiceDTO());
						if(!"".equals(vcfdi)) {
							validationMessage = "Error de validación ante el SAT, favor de validar con su emisor fiscal.";
							break;
						}
					}
					
					//Obtiene fecha de la factura
					String fechaFactura = zElement.getInvoiceDTO().getFechaTimbrado();
					fechaFactura = fechaFactura.replace("T", " ");
					SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
					Date invDate = null;
					try {
						invDate = sdf.parse(fechaFactura);
					}catch(Exception e) {
						log4j.error("Exception" , e);
						e.printStackTrace();
					}
					
					//Verifica si se validarán todas las reglas
					String rfcEmisor = zElement.getInvoiceDTO().getRfcEmisor();
					boolean allRules = true;					
					if(supExclList != null) {
						for(UDC udc : supExclList) {
							if(rfcEmisor.equals(udc.getStrValue1())){
								allRules = false;
								break;
							}
						}
					}
					
					if(allRules) {					
						//Valida fecha de emisión
						try {
							if(invDate.compareTo(startYear) < 0) {
								validationMessage = "La fecha de emisión de la factura no puede ser anterior al primero de Enero del año actual.";
								break;
							}
						}catch(Exception e) {
							log4j.error("Exception" , e);
							e.printStackTrace();
							validationMessage = "Error al obtener la fecha de timbrado de la factura.";
							break;
						}

						//Obtiene fecha de timbrado
						cal = Calendar.getInstance();
						invDate = null;
						Date orderDate = null;
						try {
							fechaFactura = zElement.getInvoiceDTO().getFechaTimbrado();
							fechaFactura = fechaFactura.replace("T", " ");
							sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
							invDate = sdf.parse(fechaFactura);
							orderDate = po.getDateRequested();
						} catch (Exception e) {
							log4j.error("Exception" , e);
							e.printStackTrace();
						}
						
						//Valida fecha de timbrado
						if(udcDate != null) {
							if(!udcDate.getStrValue1().equals(s.getTipoProductoServicio())){
								if(invDate.before(orderDate)) {
									validationMessage = "Error: La fecha de timbrado de la factura no puede ser anterior a la fecha de emisión de la orden.";
									break;
								}
							}
						}
						
						//Valida el método de pago permitido
						if(!AppConstants.REF_METODO_PAGO.equals(zElement.getInvoiceDTO().getMetodoPago())){
							validationMessage = "El método de pago permitido es " + AppConstants.REF_METODO_PAGO + ".";
							break;
						}
						
						//Valida la forma de pago permitida
						if(!AppConstants.REF_FORMA_PAGO.equals(zElement.getInvoiceDTO().getFormaPago())){
							validationMessage = "La forma de pago permitida es " + AppConstants.REF_FORMA_PAGO + ".";
							break;
						}
						
						//Valida el Uso CFDI permitido
						String cfdiReceptor = zElement.getInvoiceDTO().getReceptor().getUsoCFDI();
						if(!AppConstants.USO_CFDI.equals(cfdiReceptor)){							
							boolean validUsoCfdi = false;
							if(udcUsoCFDIExceptionList != null) {
								for(UDC udc : udcUsoCFDIExceptionList) {
									if(udc.getStrValue1().equals(rfcEmisor)){
										validUsoCfdi = true;
									}
								}
							}
							
							if(validUsoCfdi) {
								validationMessage = "El uso CFDI " + cfdiReceptor + " no es permitido para su razón social.";
								break;
							}							
						}
					
						//Valida que el emisor sea el mismo del proveedor
						if(rfcEmisor != null) {
							if(!"".equals(rfcEmisor)) {
								if(!s.getRfc().equals(rfcEmisor)) {
									validationMessage = "La factura ingresada no pertenece al RFC del emisor del proveedor registrado como " + s.getRfc() + ".";
									break;
								}
							}
						}
						
						//Valida que el receptor sea válido
						boolean receptorValido = false;
						if(receptores != null) {
							for(UDC udc : receptores) {
								if(udc.getStrValue1().equals(zElement.getInvoiceDTO().getRfcReceptor().trim())) {
									receptorValido = true;
									break;
								}
							}
						}
						if(!receptorValido) {
							validationMessage = "El RFC receptor " + zElement.getInvoiceDTO().getRfcReceptor() + " no es permitido para carga de facturas.";
							break;
						}
					}
				}
			}
		}

		//Valores iniciales para asignar recibos
		Receipt[] receiptArray = new Receipt[requestedReceiptList.size()];
        requestedReceiptList.toArray(receiptArray);
        double pendingReceiptAmount = 0;
        double pendingInvoiceAmount = 0;
        double currentReceiptAmount = 0;
        double currentInvoiceAmount = 0;
        boolean isNewReceipt = true;
        int countReceives = 0;
        int countInvoices = 0;
        int quantityAvailable = 0;

        //Calcula montos y envía facturas a JDE
		if("".equals(validationMessage)) {
			for(Map<String, ZipElementDTO> o : fileList) {
				if(!"".equals(errorMessage)) {
					break;
				} else {
					for (Map.Entry<String,ZipElementDTO> entry : o.entrySet())  {
						ZipElementDTO zElement = entry.getValue();
						countInvoices += 1;
						
				        //Asigna recibos correspondientes por factura
				        List<Receipt> currentReceiptList = new ArrayList<Receipt>();
				        boolean clearedInvoice = false;
				        log4j.info("\n\n*** UUID: " + zElement.getInvoiceDTO().getUuid() + " Subtotal Factura: " + zElement.getInvoiceDTO().getSubTotal());
				        
				        do {			        	
				        	Receipt newReceipt = (Receipt) SerializationUtils.clone(receiptArray[countReceives]);
				        	
				        	if(isNewReceipt) {
				        		quantityAvailable = Double.valueOf(Math.abs(newReceipt.getQuantityReceived())).intValue();
				        		isNewReceipt = false;
				        		log4j.info(">>>>>Cantidad Inicial Recibo<<<<<: " + quantityAvailable);
				        	}				        	
				        	
				        	if(pendingInvoiceAmount == 0) {
				        		currentInvoiceAmount = zElement.getInvoiceDTO().getSubTotal();
				        	} else {
				        		currentInvoiceAmount = Math.round(pendingInvoiceAmount*100.00)/100.00;
				        	}

				        	if(pendingReceiptAmount == 0) {
				        		currentReceiptAmount = Math.abs(newReceipt.getAmountReceived());
				        	} else {
				        		currentReceiptAmount = Math.round(pendingReceiptAmount*100.00)/100.00;
				        	}			        	
				        	
				        	double currentQuantity = 0;
				        	if(currentReceiptAmount >= currentInvoiceAmount) {
				        		currentQuantity = getCurrentQuantity(currentInvoiceAmount, Math.abs(newReceipt.getUnitCost()));
				        		newReceipt.setAmountReceived((double) currentInvoiceAmount);
				        		newReceipt.setForeignAmountReceived((double) currentInvoiceAmount);
				        		newReceipt.setQuantityReceived((double) currentQuantity);
				        		pendingInvoiceAmount = 0;
				        		pendingReceiptAmount = currentReceiptAmount - currentInvoiceAmount;
				        		clearedInvoice = true;
				        		
				        	} else {
				        		//currentQuantity = getCurrentQuantity(currentReceiptAmount, newReceipt.getUnitCost());
				        		currentQuantity = quantityAvailable;
				        		newReceipt.setAmountReceived((double) currentReceiptAmount);
				        		newReceipt.setForeignAmountReceived((double) currentReceiptAmount);
				        		newReceipt.setQuantityReceived((double) currentQuantity);
				        		pendingInvoiceAmount = currentInvoiceAmount - currentReceiptAmount; 
				        		pendingReceiptAmount = 0;
				        		
					        	if(countReceives == requestedReceiptList.size()-1 && countInvoices == fileList.size()) {
					        		clearedInvoice = true;
					        	}
					        	
					        	if(countReceives < requestedReceiptList.size()-1) {
					        		countReceives += 1;
					        		isNewReceipt = true;
					        	}
				        	}
				        	
				        	int currentQuantityInt = Double.valueOf(currentQuantity).intValue();
				        	if(quantityAvailable >= currentQuantityInt) {
				        		quantityAvailable = quantityAvailable - currentQuantityInt;	
				        	} else {
				        		if(quantityAvailable > 0) {
				        			newReceipt.setQuantityReceived((double) quantityAvailable);
				        			quantityAvailable = 0;
				        		}				        		
				        	}

				        	log4j.info("***** Linea de Recibo: " + newReceipt.getReceiptLine() + 
				        						" \nNuevo Monto Recibido: " + newReceipt.getAmountReceived() +
				        						" \nNuevo Monto Recibido 100: " + Math.round(newReceipt.getAmountReceived()*100) +
				        						" \nCantidad Recibida Calculada: " + newReceipt.getQuantityReceived() +
				        						" \nCantidad Disponible Recibo: " + quantityAvailable +
				        						" \nPendiente Recibo: " + pendingReceiptAmount +
				        						" \nPendiente Factura: " + pendingInvoiceAmount				        						
				        						);
				        	currentReceiptList.add(newReceipt);
				        	
				        } while (!clearedInvoice);
				        
				        //Modificar el signo de las cantidades si se trata de una Nota de Credito 
			        	if(AppConstants.NC_FIELD.equals(tipoComprobante)) {
					        for(Receipt r : currentReceiptList) {
				        		r.setAmountReceived(r.getAmountReceived()*-1);
				        		r.setForeignAmountReceived(r.getForeignAmountReceived()*-1);
				        		r.setQuantityReceived(Math.abs(r.getQuantityReceived())*-1);
				        		log4j.info("NC - JDE: AmountReceived " + r.getAmountReceived() + " ForeignAmountReceived " + r.getForeignAmountReceived() + " QuantityReceived " + r.getQuantityReceived());
					        }	
			        	}
				        		        	
				        //Registra en JDE
						String resp = "";
						if(domesticCurrency.equals(zElement.getInvoiceDTO().getMoneda())) {
							resp = eDIService.createNewVoucher(po, zElement.getInvoiceDTO(), 0, s, currentReceiptList, AppConstants.NN_MODULE_INVBATCH);
						} else {
							ForeingInvoice fi = new ForeingInvoice();
							fi.setSerie(zElement.getInvoiceDTO().getSerie());
							fi.setFolio(zElement.getInvoiceDTO().getFolio());
							fi.setUuid(zElement.getInvoiceDTO().getUuid());
							fi.setExpeditionDate(zElement.getInvoiceDTO().getFechaTimbrado());
							resp = eDIService.createNewForeignVoucher(po, fi, 0, s, currentReceiptList, AppConstants.NN_MODULE_INVBATCH);							
						}
						
						if(org.apache.commons.lang.StringUtils.isBlank(resp)) {
							errorMessage = "No fue posible enviar la información a JDE.";
							break;
						} else {
		                	UserDocument doc = new UserDocument();
		                	String fileName = zElement.getXmlFileName().replace(" ", "_");	                	
		                	doc.setType("text/xml");
		                	doc.setAddressBook(po.getAddressNumber());
		                	doc.setDocumentNumber(documentNumber);
		                	doc.setDocumentType(documentType);
		                	doc.setFiscalType(tipoComprobante);
		                	doc.setName(fileName);
		                	doc.setContent(zElement.getXmlFile());
		                	doc.setSize(zElement.getXmlFile().length);
		                	doc.setFolio(zElement.getInvoiceDTO().getFolio());
		                	doc.setSerie(zElement.getInvoiceDTO().getSerie());
		                	doc.setUuid(zElement.getInvoiceDTO().getUuid());
		                	doc.setUploadDate(new Date());
		                	doc.setFiscalRef(0);
		                	doc.setStatus(true);
		                	doc.setAccept(true);
		                	documentsService.save(doc, new Date(), "");
		                	
		            		doc = new UserDocument(); 
		            		fileName = zElement.getAttachedFileName().replace(" ", "_");	            		
		                	doc.setType("application/pdf");	                	
		                	doc.setAddressBook(po.getAddressNumber());
		                	doc.setDocumentNumber(documentNumber);
		                	doc.setDocumentType(documentType);
		                	doc.setFiscalType(tipoComprobante);
		                	doc.setName(fileName);
		                	doc.setContent(zElement.getAttachedFile());	                	
		                	doc.setSize(zElement.getAttachedFile().length);	                	
		                	doc.setFolio(zElement.getInvoiceDTO().getFolio());
		                	doc.setSerie(zElement.getInvoiceDTO().getSerie());
		                	doc.setUuid(zElement.getInvoiceDTO().getUuid());
		                	doc.setUploadDate(new Date());
		                	doc.setFiscalRef(0);
		                	doc.setStatus(true);
		                	doc.setAccept(true);
		                	documentsService.save(doc, new Date(), "");
							
							//Envía archivo a JDE
							try {
			                	File file = new File(System.getProperty("java.io.tmpdir")+"/"+ zElement.getInvoiceDTO().getUuid() + ".pdf");
			                	Path filePath = Paths.get(file.getAbsolutePath());
			                	Files.write(filePath, zElement.getAttachedFile());	                	
			                	documentsService.sendFileToRemote(file, zElement.getInvoiceDTO().getUuid() + ".pdf");
			                	
			                	ftpService.setServices(null, purchaseOrderService, documentsService, udcService, logger);
			                	//Send xml to SFTP server
	    	                	ftpService.sendToSftpServer(zElement.getXmlFile(), zElement.getXmlFileName().replace(" ", "_"));
			                	//Send PDF to SFTP server
	    	                	ftpService.sendToSftpServer(new FileInputStream(file.getAbsolutePath()), zElement.getInvoiceDTO().getUuid() + ".pdf");
			                	file.delete();
							} catch (Exception e) {
								log4j.error("Exception" , e);
								e.printStackTrace();
							}
							
							//Obtiene fecha de la factura
							String fechaFactura = zElement.getInvoiceDTO().getFechaTimbrado();
							fechaFactura = fechaFactura.replace("T", " ");
							SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
							Date invDate = null;
							
							try {
								invDate = sdf.parse(fechaFactura);
							}catch(Exception e) {
								log4j.error("Exception" , e);
								e.printStackTrace();
							}
							
							//Guarda el registro de las facturas cargadas al recibo
							for(Receipt r :requestedReceiptList) {
								ReceiptInvoice ri = new ReceiptInvoice();
								ri.setAmountReceived(r.getAmountReceived());
								ri.setCurrencyCode(r.getCurrencyCode());
								ri.setAddressNumber(s.getAddresNumber());
								ri.setDocumentNumber(documentNumber);
								ri.setDocumentType(documentType);
								ri.setEstPmtDate(estimatedPaymentDate);
								ri.setFolio(zElement.getInvoiceDTO().getFolio());					
								ri.setForeignAmountReceived(r.getForeignAmountReceived());
								ri.setInvDate(invDate);
								ri.setPaymentAmount(r.getPaymentAmount());
								ri.setPaymentDate(r.getPaymentDate());
								ri.setPaymentReference(r.getPaymentReference());
								ri.setPaymentStatus(AppConstants.STATUS_OC_INVOICED);
								ri.setSerie(zElement.getInvoiceDTO().getSerie());
								ri.setUuid(zElement.getInvoiceDTO().getUuid());
								ri.setLineNumber(r.getLineNumber());
								ri.setUploadDate(new Date());
								purchaseOrderService.saveReceiptInvoice(ri);
								logger.log(AppConstants.LOG_BATCH_PROCESS_MASS_LOAD, AppConstants.LOG_BATCH_PROCESS_MASS_LOAD_MSG
										.replace("INVOICE_NUMBER", zElement.getInvoiceDTO().getUuid())
										.replace("DOCUMENT_NUMBER", String.valueOf(po.getOrderNumber()))
										.replace("DOCUMENT_TYPE", po.getOrderType())
										.replace("ADDRESS_NUMBER", s.getAddresNumber()));
							}
							
							//Actualiza estatus de la orden de compra 
					        po.setInvoiceAmount(po.getInvoiceAmount() + invoiceAmount);
					        po.setOrderStauts(AppConstants.STATUS_OC_INVOICED);
					        po.setInvoiceUploadDate(new Date());
					        po.setSentToWns(null);
					        purchaseOrderService.updateOrders(po);
					        
							//Actualiza estatus de los Recibos
					        for(Receipt r :requestedReceiptList) {
					        	r.setInvDate(new Date());
					        	r.setUuid(AppConstants.RECEIPT_MASSIVE_UPLOAD);
					        	r.setEstPmtDate(estimatedPaymentDate);
								r.setStatus(AppConstants.STATUS_OC_INVOICED);
							}
							purchaseOrderService.updateReceipts(requestedReceiptList);
						}
					}	
				}
			}
		}

		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;
		log4j.info("Execution time in milliseconds : " + timeElapsed / 1000000);
		
		//Obtiene correos de envío
		if(user.getEmail() != null && !user.getEmail().isEmpty()) {
			emailRecipient = user.getEmail();
		}

		UDC udcEmail = udcService.searchBySystemAndKey("MASSIVEUPEMAIL", "ADDITIONAL");
		if(udcEmail != null && !org.apache.commons.lang.StringUtils.isBlank(udcEmail.getStrValue1())) {
			emailRecipient = emailRecipient.concat(",").concat(udcEmail.getStrValue1());
		}
		
		if("".equals(validationMessage) && "".equals(errorMessage)) {
			logger.log(AppConstants.LOG_BATCH_PROCESS_MASS_LOAD, AppConstants.LOG_BATCH_PROCESS_MASS_LOAD_MSG
					.replace("INVOICE_NUMBER", currentInvoiceUUID)
					.replace("DOCUMENT_NUMBER", String.valueOf(po.getOrderNumber()))
					.replace("DOCUMENT_TYPE", po.getOrderType())
					.replace("ADDRESS_NUMBER", s.getAddresNumber()));
			
			//CASO DE EXITO
			String subjectMessage;
			String contentMessage;			
			if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)) {
				subjectMessage = AppConstants.EMAIL_INV_BATCH_ACCEPT_SUP;
				contentMessage = AppConstants.EMAIL_INVBATCH_ACCEPTED;
			} else {
				subjectMessage = AppConstants.EMAIL_NC_BATCH_ACCEPT_SUP ;
				contentMessage = AppConstants.EMAIL_CNBATCH_ACCEPTED;
			}
			
			String emailContent = contentMessage;
			emailContent = emailContent.replace("_DATA_", po.getOrderNumber() + "-" + po.getOrderType());
			
			EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			emailAsyncSup.setProperties(subjectMessage, stringUtils.prepareEmailContent(emailContent + "<br /> <br />" + AppConstants.ETHIC_CONTENT), emailRecipient);
			emailAsyncSup.setMailSender(mailSenderObj);
			emailAsyncSup.setAdditionalReference(null,null);
			Thread emailThreadSup = new Thread(emailAsyncSup);
			emailThreadSup.start();
			
		} else {
			logger.log(AppConstants.LOG_BATCH_PROCESS_MASS_LOAD, AppConstants.LOG_BATCH_PROCESS_MASS_LOAD_ERR
					.replace("INVOICE_NUMBER", currentInvoiceUUID)
					.replace("DOCUMENT_NUMBER", String.valueOf(po.getOrderNumber()))
					.replace("DOCUMENT_TYPE", po.getOrderType())
					.replace("ADDRESS_NUMBER", s.getAddresNumber())
					.concat(" (" + validationMessage + errorMessage + ")"));
						
			//CASO DE ERROR
			String subjectMessage;
			String contentMessage;			
			if(AppConstants.INVOICE_FIELD.equals(tipoComprobante)) {				
				subjectMessage = AppConstants.EMAIL_INV_BATCH_REJECT_SUP + po.getOrderNumber() + "-" + po.getOrderType() + "<br />";
				contentMessage = AppConstants.EMAIL_INVBATCH_REJECTED ;
			} else {
				subjectMessage = AppConstants.EMAIL_NC_BATCH_REJECT_SUP + po.getOrderNumber() + "-" + po.getOrderType() + "<br />";
				contentMessage = AppConstants.EMAIL_CNBATCH_REJECTED ;
			}
			
			if(!"".equals(validationMessage)) {
				contentMessage = contentMessage + "<br />Comprobante <br />Proof" + currentInvoiceUUID + ": " + validationMessage;
			} else {
				contentMessage = contentMessage + "<br />" + errorMessage;
			}
			
			String emailContent = contentMessage;
			emailContent = emailContent.replace("_DATA_", po.getOrderNumber() + "-" + po.getOrderType());
			
			EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			emailAsyncSup.setProperties(subjectMessage, stringUtils.prepareEmailContent(emailContent), emailRecipient);
			emailAsyncSup.setMailSender(mailSenderObj);
			emailAsyncSup.setAdditionalReference(null,null);
			Thread emailThreadSup = new Thread(emailAsyncSup);
			emailThreadSup.start();
		}
	}
	
	private double getCurrentQuantity(double totalAmount, double unitCost) {		
		double currentQuantity = 0;
		
		try {
			currentQuantity = totalAmount/unitCost;
			
			if(currentQuantity < 1) {
				currentQuantity = 1;				
			} else {
				//Se realiza esta validacion, ya que al multiplicar un número flotante por 100, pierde decimales P.Ejemplo 6.5*100 = 6.44
				double fTotalAmount = (double) totalAmount;				
				int iTotalAmount = Double.valueOf(Math.round(fTotalAmount*100)).intValue();			
				String iString = String.valueOf(iTotalAmount);
				
				if(iString != null && iString.length() > 1 && Integer.valueOf(iString).intValue() > 9) {
					int initPos = iString.length() - 2;
					String tens = iString.substring(initPos);
					
					if(Integer.valueOf(tens).intValue() > 49) {
						currentQuantity = Math.ceil(currentQuantity);
					} else {
						if(Integer.valueOf(tens).intValue() > 9) {
							currentQuantity = Math.floor(currentQuantity);
						} else {
							currentQuantity = Math.round(currentQuantity);							
						}
						
					}
				} else {
					currentQuantity = Math.round(currentQuantity);
				}
			}
		} catch (Exception e) {
            log4j.error("Exception" , e);
			e.printStackTrace();
		}
		return currentQuantity;
	}
	
	private String validaComprobanteSAT(InvoiceDTO inv) {
		
		String payload = PayloadProducer.getCFDIPayload(inv);
		String response = hTTPRequestService.performSoapCall(payload);
		if(response != null) {
			JSONObject xmlJSONObj = XML.toJSONObject(response, true);
			JsonElement jelement = new JsonParser().parse(xmlJSONObj.toString());
			JsonObject jobject = jelement.getAsJsonObject();
			JsonElement soapEnvelope = jobject.get("s:Envelope").getAsJsonObject().get("s:Body").getAsJsonObject().get("ConsultaResponse").getAsJsonObject();
			JsonElement result = soapEnvelope.getAsJsonObject().get("ConsultaResult");
			
			String codigoEstatus = NullValidator.isNull(result.getAsJsonObject().get("a:CodigoEstatus").toString());
			String esCancelable = NullValidator.isNull(result.getAsJsonObject().get("a:EsCancelable").toString());
			String estado = NullValidator.isNull(result.getAsJsonObject().get("a:Estado").toString());
			String estatusCancelacion = NullValidator.isNull(result.getAsJsonObject().get("a:EstatusCancelacion").toString());

			codigoEstatus = codigoEstatus.replace("\"", "").trim();
			esCancelable = esCancelable.replace("\"", "").trim();
			estado = estado.replace("\"", "").trim();
			estatusCancelacion = estatusCancelacion.replace("\"", "").trim();
			
			if(!AppConstants.CFDI_SUCCESS_MSG.equals(codigoEstatus) || !AppConstants.CFDI_SUCCESS_MSG_ACTIVE.equals(estado)) {
				String errorMsg = "El documento no es aceptado ante el SAT. Se recibe el siguiente mensaje :<br>" +
			                       " - CodigoEstatus: " + codigoEstatus + "<br />" +
			                       " - EsCancelable: " + esCancelable + "<br />" +
			                       " - Estado: " + estado + "<br />" +
			                       " - EstatusCancelacion: " + estatusCancelacion + "<br />" ;
				return errorMsg;
			}
			
		}else {
			return "Error de validación ante el SAT, favor de validar con su emisor fiscal";	
		}
		
		return "";
		
	}
	
	public double roundDoubleToDoubleOld(double number, int decimalPosition) {
		return BigDecimal.valueOf(number).setScale(decimalPosition, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
	public void setUdcService(UdcService udcService) {
		this.udcService = udcService;
	}

	public void setS(Supplier s) {
		this.s = s;
	}

	public void setPo(PurchaseOrder po) {
		this.po = po;
	}

	public void setFileList(List<Map<String, ZipElementDTO>> fileList) {
		this.fileList = fileList;
	}

	public void seteDIService(EDIService eDIService) {
		this.eDIService = eDIService;
	}

	public void sethTTPRequestService(HTTPRequestService hTTPRequestService) {
		this.hTTPRequestService = hTTPRequestService;
	}
	
	public void setMailSenderObj(JavaMailSender mailSenderObj) {
		this.mailSenderObj = mailSenderObj;
	}

	public void setStringUtils(StringUtils stringUtils) {
		this.stringUtils = stringUtils;
	}

	public void setPurchaseOrderService(PurchaseOrderService purchaseOrderService) {
		this.purchaseOrderService = purchaseOrderService;
	}

	public void setPaymentCalendarService(PaymentCalendarService paymentCalendarService) {
		this.paymentCalendarService = paymentCalendarService;
	}
	
	public void setDocumentsService(DocumentsService documentsService) {
		this.documentsService = documentsService;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setDocumentNumber(int documentNumber) {
		this.documentNumber = documentNumber;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public void setRequestedReceiptList(List<Receipt> requestedReceiptList) {
		this.requestedReceiptList = requestedReceiptList;
	}

	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public void setFtpService(FTPService ftpService) {
		this.ftpService = ftpService;
	}	
	
	
}
