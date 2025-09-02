package com.eurest.supplier.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eurest.supplier.dao.DocumentsDao;
import com.eurest.supplier.dao.CustomBrokerDao;
import com.eurest.supplier.dao.PurchaseOrderDao;
import com.eurest.supplier.dao.UDCDao;
import com.eurest.supplier.dto.ForeingInvoice;
import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.invoiceXml.Concepto;
import com.eurest.supplier.invoiceXml.Impuestos;
import com.eurest.supplier.invoiceXml.Retencion;
import com.eurest.supplier.invoiceXml.Retenciones;
import com.eurest.supplier.invoiceXml.Traslado;
import com.eurest.supplier.invoiceXml.Traslados;
import com.eurest.supplier.model.CustomBroker;
import com.eurest.supplier.model.CustomBrokerConcept;
import com.eurest.supplier.model.ForeignInvoiceTable;
import com.eurest.supplier.model.NonComplianceSupplier;
import com.eurest.supplier.model.PaymentCalendar;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.FileConceptUploadBean;
import com.eurest.supplier.util.NullValidator;
import com.eurest.supplier.util.PayloadProducer;
import com.eurest.supplier.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service("customBrokerService")
public class CustomBrokerService {
	
	@Autowired
	CustomBrokerDao customBrokerDao;
	
	@Autowired
	PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	HTTPRequestService HTTPRequestService;
	
	@Autowired
	private JavaMailSender mailSenderObj;
	
	@Autowired
	StringUtils stringUtils;
	
	@Autowired
	private SupplierService supplierService;

	@Autowired
	DocumentsService documentsService;
	
	@Autowired
	PurchaseOrderService purchaseOrderService;

	@Autowired
	private DocumentsDao documentsDao;
	
	@Autowired
	XmlToPojoService xmlToPojoService;
	
	@Autowired
	EDIService eDIService;
	
	@Autowired
	UDCDao udcDao;
	
	@Autowired
	PaymentCalendarService paymentCalendarService;
	
	@Autowired
	ExchangeRateService exchangeRateService;
	
	private Logger log4j = Logger.getLogger(CustomBrokerService.class);
	
	static String TIMESTAMP_DATE_PATTERN = "yyyy-MM-dd";
	static String TIMESTAMP_DATE_PATTERN_NEW = "yyyy-MM-dd HH:mm:ss";
	static String DATE_PATTERN = "dd/MM/yyyy";
	
	public CustomBroker getById(int id) {
		return customBrokerDao.getById(id);
	}
	
	public List<CustomBroker> getCustomBroker(String addressNumber, String status, String uuid, String documentType, int start, int limit) {
		return customBrokerDao.getCustomBroker(addressNumber, status, uuid, documentType,  start, limit);		
	}
	
	
	@SuppressWarnings({ "unused"})
	public String validateInvoiceWithoutOrder(FileConceptUploadBean uploadConcept,
											  InvoiceDTO inv,
											  String addressBook,
											  String company,
											  String tipoComprobante,
											  double dblAdvancePayment){
		
		CustomBroker fiscalDoc = new CustomBroker();		
		DecimalFormat currencyFormat = new DecimalFormat("$#,###.###");
		UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");
		if(udcCfdi != null) {
			if(!"".equals(udcCfdi.getStrValue1())) {
				if("TRUE".equals(udcCfdi.getStrValue1())) {
					String vcfdi = validaComprobanteSAT(inv);
					if(!"".equals(vcfdi)) {
						return "Error de validación ante el SAT, favor de validar con su emisor fiscal.";
					}
					
					String vNull = validateInvNull(inv);
					if(!"".equals(vNull)) {
						return "Error al validar el archivo XML, no se encontró el campo " + vNull + ".";
					}
				}
			}
		}else {
			String vcfdi = validaComprobanteSAT(inv);
			if(!"".equals(vcfdi)) {
				return "Error de validación ante el SAT, favor de validar con su emisor fiscal.";
			}
			
			String vNull = validateInvNull(inv);
			if(!"".equals(vNull)) {
				return "Error al validar el archivo XML, no se encontró el campo " + vNull + ".";
			}
		}
		
		String resp = "";
		Supplier s = supplierService.searchByAddressNumber(addressBook);
		if(s != null) {
			fiscalDoc.setSupplierName(s.getName());			
		} else {
			return "El proveedor no está registrado en el portal.";
		}
		
		if("MX".equals(s.getCountry().trim())) {
			fiscalDoc.setGlOffset(AppConstants.GL_OFFSET_DEFAULT);
		} else {
			fiscalDoc.setGlOffset(AppConstants.GL_OFFSET_FOREIGN);
		}
		
		String emailRecipient = (s.getEmailSupplier());
		String fechaFactura = inv.getFechaTimbrado();
		fechaFactura = fechaFactura.replace("T", " ");
		SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
		Date invDate = null;
		
		try {
			invDate = sdf.parse(fechaFactura);
		}catch(Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}

		String rfcEmisor = inv.getRfcEmisor();
		String invCurrency = inv.getMoneda().trim();
		double exchangeRate = inv.getTipoCambio();
		String domesticCurrency = AppConstants.DEFAULT_CURRENCY;
		
		UDC companyCC = udcDao.searchBySystemAndKey("RFCCOMPANYCC", company);
		if(companyCC != null) {
			fiscalDoc.setCompanyFD(company);
			fiscalDoc.setCentroCostos(org.apache.commons.lang.StringUtils.leftPad(companyCC.getStrValue2(), 12, " "));
		} else {
			return "La compañía no tiene un centro de costos asignado en el portal de proveedores.";
		}
	
		String accountingAcc = "";
		List<UDC> accountingAccList = udcDao.searchBySystem("RFCACCOUNTINGACC");
		if(accountingAccList != null) {
			for(UDC udcAcc : accountingAccList) {
				if(udcAcc.getUdcKey().equals(company) && udcAcc.getStrValue1().equals(inv.getMoneda())) {
					accountingAcc = udcAcc.getStrValue2().trim();
					break;
				}
			}
		}
		
		if(!"".equals(accountingAcc)) {
			fiscalDoc.setAccountingAccount(accountingAcc);
		} else {
			return "La compañía no tiene una cuenta contable asignada para la moneda " + inv.getMoneda() + " en el portal de proveedores.";
		}
		
    	boolean isTransportCB = false;
    	UDC udcTrans = udcService.searchBySystemAndKey("CUSTOMBROKER", addressBook);    	
    	if(udcTrans != null) {
			if(udcTrans.getStrValue1() != null && "Y".equals(udcTrans.getStrValue1().trim())) {
				isTransportCB = true;
			}
    	}
    	
    	UDC accountingNumber = udcDao.searchBySystemAndKey("CPTACCNUMBER", "CONCEPT000");
    	if(accountingNumber != null) {
			if(!isTransportCB) {
				fiscalDoc.setAccountNumber(fiscalDoc.getCentroCostos().concat(".").concat(accountingNumber.getStrValue1()));
			} else {
				fiscalDoc.setAccountNumber(fiscalDoc.getCentroCostos().concat(".").concat(accountingNumber.getStrValue2()));						
			}	
    	} else {
    		return "No está registrado ningún número de cuenta para agentes aduanales.";
    	}
    	
		if("MXN".equals(invCurrency)) {			
			fiscalDoc.setCurrencyCode("MXP");
			fiscalDoc.setCurrencyMode(AppConstants.CURRENCY_MODE_DOMESTIC);
		} else {
			fiscalDoc.setCurrencyCode(invCurrency);
			fiscalDoc.setCurrencyMode(AppConstants.CURRENCY_MODE_FOREIGN);			
		}

		List<UDC> comUDCList =  udcService.searchBySystem("COMPANYDOMESTIC");
		if(comUDCList != null && !comUDCList.isEmpty()) {
			for(UDC companyCurrUDC : comUDCList) {
				if(companyCurrUDC.getStrValue1().trim().equals(fiscalDoc.getCompanyFD()) && !"".equals(companyCurrUDC.getStrValue2().trim())) {
					if(invCurrency.equals(companyCurrUDC.getStrValue2().trim())) {
						fiscalDoc.setCurrencyMode(AppConstants.CURRENCY_MODE_DOMESTIC);
					} else {
						fiscalDoc.setCurrencyMode(AppConstants.CURRENCY_MODE_FOREIGN);
					}
					break;
				}
			}
		}
		
		List<UDC> supDomUDCList =  udcService.searchBySystem("SUPPLIERDOMESTIC");
		if(supDomUDCList != null && !supDomUDCList.isEmpty()) {
			for(UDC supplier : supDomUDCList) {
				if(supplier.getStrValue1().trim().equals(addressBook) && !"".equals(supplier.getStrValue2().trim())) {
					if(invCurrency.equals(supplier.getStrValue2().trim())) {
						fiscalDoc.setCurrencyMode(AppConstants.CURRENCY_MODE_DOMESTIC);
					} else {
						fiscalDoc.setCurrencyMode(AppConstants.CURRENCY_MODE_FOREIGN);
					}
					break;
				}
			}
		}
		
		int diasCredito = 0;
		UDC pmtTermsUDC = udcDao.searchBySystemAndKey("PMTTCUSTOM", "DEFAULT");
		if(pmtTermsUDC != null) {
			fiscalDoc.setPaymentTerms(pmtTermsUDC.getStrValue1());
			diasCredito = Integer.valueOf(pmtTermsUDC.getStrValue2());
		} else {
			fiscalDoc.setPaymentTerms("N30");
			diasCredito = 30;
		}

		Date estimatedPaymentDate = null;
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();		
		c.setTime(currentDate);
		c.add(Calendar.DATE, diasCredito);
		List<PaymentCalendar> pc = paymentCalendarService.getPaymentCalendarFromToday(c.getTime(), 0, 500, addressBook);
		if(pc != null) {
			if(pc.size() > 0) {
				estimatedPaymentDate = pc.get(0).getPaymentDate();
			}else {
				estimatedPaymentDate = c.getTime();
			}
		}else {
			estimatedPaymentDate = c.getTime();
		}		
		fiscalDoc.setEstimatedPaymentDate(estimatedPaymentDate);

		String emailApprover = "";
		List<UDC> approverUDCList = udcDao.searchBySystem("APPROVERINV");
		if(approverUDCList != null) {
			for(UDC approver : approverUDCList) {
				if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
					fiscalDoc.setCurrentApprover(approver.getStrValue1());
					emailApprover = approver.getStrValue2();
				}

				if(AppConstants.INV_SECOND_APPROVER.equals(approver.getUdcKey())){
					fiscalDoc.setNextApprover(approver.getStrValue1());
				}				
			}
		}
		fiscalDoc.setStatus(AppConstants.STATUS_INPROCESS);
		fiscalDoc.setApprovalStatus(AppConstants.STATUS_INPROCESS);
		fiscalDoc.setApprovalStep(AppConstants.FIRST_STEP);
		fiscalDoc.setTaxCode(getInvoiceTaxCode(inv));

		List<Receipt> recUuidList = purchaseOrderService.getReceiptsByUUID(inv.getUuid());
		if(recUuidList != null) {
			if(recUuidList.size()>0)
				return "La factura que intenta ingresar ya se fue cargada previamente en una orden de compra.";
		}
		
		List<CustomBroker> fdUuidList = customBrokerDao.getCustomBroker("", "", inv.getUuid(), "FACTURA", 0, 1);
		if(fdUuidList != null) {
			if(fdUuidList.size()>0)
				return "La factura que intenta ingresar ya fue cargada previamente.";
		}
		
		boolean isCompanyOK = false;
    	UDC udcCompany = udcService.searchBySystemAndKey("COMPANYCB", company);    	
    	if(udcCompany != null) {
			if(udcCompany.getStrValue1().equals(inv.getRfcReceptor().trim())) {
				isCompanyOK = true;
			}
    	}
    	if(!isCompanyOK) {
    		return "El RFC de la compañia no corresponde con el RFC del receptor de la factura " + inv.getRfcReceptor().trim() + ".";
    	}
    	
		boolean allRules = true;
		List<UDC> supExclList =  udcService.searchBySystem("NOCHECKSUP");
		if(supExclList != null) {
			for(UDC udc : supExclList) {
				if(rfcEmisor.equals(udc.getStrValue1())){
					allRules = false;
					break;
				}
			}
		}

		//VALIDACIONES DEL XML
		if(allRules) {
			
			if(!AppConstants.DEFAULT_CURRENCY.equals(invCurrency)) {
				if(exchangeRate == 0) {
					return "La moneda de la factura es " + invCurrency + " sin embargo, no existe definido un tipo de cambio.";
				}
			}
			
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, currentYear);
			cal.set(Calendar.DAY_OF_YEAR, 1);    
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date startYear = cal.getTime();
			try {
				if(invDate.compareTo(startYear) < 0) {
					return "La fecha de emisión de la factura no puede ser anterior al primero de Enero del año en curso";
				}
			}catch(Exception e) {
				log4j.error("Exception" , e);
				e.printStackTrace();
				return "Error al obtener la fecha de timbrado de la factura";
			}
			
			if(s != null) {
				NonComplianceSupplier ncs = documentsService.nonComplianceSupplierService.searchByTaxId(s.getRfc(), 0, 0);
			    if (ncs != null && (
			      ncs.getRefDate1().contains("Definitivo") || 
			      ncs.getRefDate1().contains("Presunto") || 
			      ncs.getRefDate1().contains("Desvirtuado") || 
			      ncs.getRefDate2().contains("Definitivo") || 
			      ncs.getRefDate2().contains("Presunto") || 
			      ncs.getRefDate2().contains("Desvirtuado") || 
			      ncs.getStatus().contains("Definitivo") || 
			      ncs.getStatus().contains("Presunto") || 
			      ncs.getStatus().contains("Desvirtuado"))) {
			    	
			    	String altEmail = "";
						List<UDC> udcList =  udcService.searchBySystem("TAXALTEMAIL");
						if(udcList != null) {
							String emailContent = AppConstants.EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER;
							emailContent = emailContent.replace("_ADDNUMBER_", s.getAddresNumber());
							emailContent = emailContent.replace("_RAZONSOCIAL_", s.getRazonSocial());

							
							altEmail = udcList.get(0).getStrValue1();
						    EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
							emailAsyncSup.setProperties(
									AppConstants.EMAIL_NO_COMPLIANCE_INVOICE,
									emailContent,
									altEmail);
							emailAsyncSup.setMailSender(mailSenderObj);
							Thread emailThreadSup = new Thread(emailAsyncSup);
							emailThreadSup.start();
						}
			    	
						 EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						 emailAsyncSup.setProperties(
									AppConstants.EMAIL_INVOICE_REJECTED_WITHOUT_OC,
									AppConstants.EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER_NOTIF + inv.getUuid() + "<br /> <br />" + AppConstants.ETHIC_CONTENT,
									s.getEmailSupplier());
							emailAsyncSup.setMailSender(mailSenderObj);
							Thread emailThreadSup = new Thread(emailAsyncSup);
							emailThreadSup.start();
						
						return "Los registros indican que cuenta con problemas fiscales y no se podrán cargar facturas en este momento.";
	
			    } 
			}else {
				return "El proveedor no existe en el catálogo de la aplicación";
			}
			
			/*
			cal = Calendar.getInstance();
			invDate = null;
			
			try {
				fechaFactura = inv.getFechaTimbrado();
				fechaFactura = fechaFactura.replace("T", " ");
				sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
				invDate = sdf.parse(fechaFactura);
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
			
			if(!AppConstants.REF_METODO_PAGO.equals(inv.getMetodoPago())){
				return "El método de pago permitido es " + AppConstants.REF_METODO_PAGO + " y su CFDI contiene el valor " + inv.getMetodoPago() + ". Favor de emitir nuevamente el CFDI con el método de pago antes mencionado.";			
			}
			
			if(!AppConstants.REF_FORMA_PAGO.equals(inv.getFormaPago())){
				return  "La forma de pago permitida es " + AppConstants.REF_FORMA_PAGO + " y su CFDI contiene el valor " + inv.getFormaPago() + ". Favor de emitir nuevamente el CFDI con la forma de pago antes mencionada";		
			}
			
			String cfdiReceptor = inv.getReceptor().getUsoCFDI();
			String rfcReceptor = inv.getRfcReceptor();
			if(!AppConstants.USO_CFDI.equals(cfdiReceptor)){
				List<UDC> udcList =  udcService.searchBySystem("CFDIEXC");
				boolean usoCfdiExcept = false;
				if(udcList != null) {
					for(UDC udc : udcList) {
						if(udc.getStrValue1().equals(rfcEmisor)){
							usoCfdiExcept = true;
						}
					}
				}
				
				if(usoCfdiExcept) {
					return "El uso CFDI " + cfdiReceptor + " no es permitido para su razón social";
				}
				
			}
	
			if(rfcEmisor != null) {
				if(!"".equals(rfcEmisor)) {
					if(!s.getRfc().equals(rfcEmisor)) {
						return "La factura ingresada no pertenece al RFC del emisor del proveedor registrado como " + s.getRfc();
					}
				}
			}
			boolean receptorValido = false;
			List<UDC> receptores = udcService.searchBySystem("RECEPTOR");
			if(receptores != null) {
				for(UDC udc : receptores) {
					if(udc.getStrValue1().equals(inv.getRfcReceptor().trim())) {
						receptorValido = true;
						break;
					}
				}
			}
			if(!receptorValido) {
				return "El RFC receptor " + inv.getRfcReceptor() + " no es permitido para carga de facturas.";
			}			
		}
		
		//Calcula Total de Impuestos
		double taxAmount = inv.getImpuestos();
		if(inv.getTotalRetenidos() > 0D) {
			taxAmount = taxAmount - inv.getTotalRetenidos();
		}
				
		fiscalDoc.setFolio(inv.getFolio());
		fiscalDoc.setSerie(inv.getSerie());
		fiscalDoc.setUuidFactura(inv.getUuid());
		fiscalDoc.setType(AppConstants.INVOICE_FIELD_UDC);
		fiscalDoc.setAddressNumber(addressBook);
		fiscalDoc.setRfcEmisor(inv.getRfcEmisor());
		fiscalDoc.setSubtotal(inv.getSubTotal());
		fiscalDoc.setAmount(inv.getTotal());
		fiscalDoc.setMoneda(inv.getMoneda());
		fiscalDoc.setTipoCambio(exchangeRate);
		fiscalDoc.setInvoiceDate(inv.getFecha());
		fiscalDoc.setDescuento(inv.getDescuento());
		fiscalDoc.setImpuestos(taxAmount);
		fiscalDoc.setRfcReceptor(inv.getRfcReceptor());
		fiscalDoc.setAdvancePayment(dblAdvancePayment);
		fiscalDoc.setInvoiceUploadDate(new Date());
    	
    	//Create concept list
    	this.createConceptList(uploadConcept, fiscalDoc, true);
    	///gama validacion MX0 a TAX1
    	fiscalDoc.setTaxCode(fiscalDoc.getTaxCode().equals(AppConstants.INVOICE_TAX0)?"MXEX":fiscalDoc.getTaxCode());
    	
    	for (CustomBrokerConcept fc : fiscalDoc.getConcepts()) {
    		fc.setTaxCode(fc.getTaxCode().equals(AppConstants.INVOICE_TAX0)?"MXEX":fc.getTaxCode());
		}
    	
    	
    	
    	//Save Fiscal Document
    	this.saveDocument(fiscalDoc);
    	
    	//Save Concepts
    	documentsService.save(uploadConcept, addressBook, inv.getUuid());
    	
    	try {
    		String emailContent = AppConstants.EMAIL_INV_APPROVAL_MSG_1_NO_OC;
    		emailContent = emailContent.replace("_UUID_", inv.getUuid());
    		emailContent = emailContent.replace("_SUPPLIER_", s.getAddresNumber());
    		
        	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
            emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_NO_OC, 
            this.stringUtils.prepareEmailContent(emailContent),
            emailApprover);
            emailAsyncSup.setMailSender(mailSenderObj);
            Thread emailThreadSup = new Thread(emailAsyncSup);
            emailThreadSup.start();
		} catch (Exception e) {
			log4j.error("Exception" , e);
		}
        
		return "";
	}

	public String validateConceptInvoiceWithoutOrder(FileConceptUploadBean uploadItems, InvoiceDTO mainInvoice, String addressNumber, String tipoComprobante, String company) {
		String additionalMessage = "";
		CommonsMultipartFile itemXML = null;
		CommonsMultipartFile itemPDF = null;
		String conceptName;
		double conceptTotal;
		double conceptSubtotal;
		String conceptTaxCode;

		String mainInvCurrency = mainInvoice.getMoneda().trim();
		Supplier s = supplierService.searchByAddressNumber(addressNumber);
		if(s == null) {
			return "El proveedor no existe en la base de datos.";
		}
		
		UDC udcCfdi = udcService.searchBySystemAndKey("VALIDATE", "CFDI");
		List<UDC> supExclList =  udcService.searchBySystem("NOCHECKSUP");
		
		//Valida unicamente los conceptos que pueden tener factura fiscal
		for(int conceptNumber=1; conceptNumber < 27; conceptNumber++) {			
			itemXML = null;
			itemPDF = null;
			conceptName = "";
			conceptTaxCode = "";
			additionalMessage = "";
			conceptTotal = 0D;
			conceptSubtotal = 0D;
			
			switch (conceptNumber) {
			case 1:
				itemXML = uploadItems.getFileConcept1_1();
				itemPDF = uploadItems.getFileConcept1_2();
				conceptName = AppConstants.CONCEPT_DESC_001;
				break;
			case 2:
				itemXML = uploadItems.getFileConcept2_1();
				itemPDF = uploadItems.getFileConcept2_2();
				conceptName = AppConstants.CONCEPT_DESC_002;
				break;
			case 3:
				itemXML = uploadItems.getFileConcept3_1();
				itemPDF = uploadItems.getFileConcept3_2();
				conceptName = AppConstants.CONCEPT_DESC_003;
				break;
			case 4:
				itemXML = uploadItems.getFileConcept4_1();
				itemPDF = uploadItems.getFileConcept4_2();
				conceptName = AppConstants.CONCEPT_DESC_004;
				break;
			case 5:				
				itemXML = uploadItems.getFileConcept5_1();
				itemPDF = uploadItems.getFileConcept5_2();
				conceptName = AppConstants.CONCEPT_DESC_005;
				break;
			case 6:
				itemXML = uploadItems.getFileConcept6_1();
				itemPDF = uploadItems.getFileConcept6_2();
				conceptName = AppConstants.CONCEPT_DESC_006;
				break;
			case 7:
				itemXML = uploadItems.getFileConcept7_1();
				itemPDF = uploadItems.getFileConcept7_2();
				conceptName = AppConstants.CONCEPT_DESC_007;
				break;
			case 8:
				itemXML = uploadItems.getFileConcept8_1();
				itemPDF = uploadItems.getFileConcept8_2();
				conceptName = AppConstants.CONCEPT_DESC_008;
				break;
			case 9:
				itemXML = uploadItems.getFileConcept9_1();
				itemPDF = uploadItems.getFileConcept9_2();
				conceptName = AppConstants.CONCEPT_DESC_009;
				break;
			case 10:
				itemXML = uploadItems.getFileConcept10_1();
				itemPDF = uploadItems.getFileConcept10_2();
				conceptName = AppConstants.CONCEPT_DESC_010;
				break;
			case 11:
				itemXML = uploadItems.getFileConcept11_1();
				itemPDF = uploadItems.getFileConcept11_2();
				conceptName = AppConstants.CONCEPT_DESC_011;
				break;
			case 12:				
				itemXML = uploadItems.getFileConcept12_1();
				itemPDF = uploadItems.getFileConcept12_2();
				conceptName = AppConstants.CONCEPT_DESC_012;
				break;
			case 13:
				itemXML = uploadItems.getFileConcept13_1();
				itemPDF = uploadItems.getFileConcept13_2();
				conceptName = AppConstants.CONCEPT_DESC_013;
				break;
			case 14:
				itemXML = uploadItems.getFileConcept14_1();
				itemPDF = uploadItems.getFileConcept14_2();
				conceptName = AppConstants.CONCEPT_DESC_014;
				break;
			case 15:
				itemXML = uploadItems.getFileConcept15_1();
				itemPDF = uploadItems.getFileConcept15_2();
				conceptName = AppConstants.CONCEPT_DESC_015;
				break;
			case 16://Conceptos con impuesto por default
				conceptTotal = this.currencyToDouble(uploadItems.getConceptImport16());
				conceptSubtotal = this.currencyToDouble(uploadItems.getConceptSubtotal16());
				conceptTaxCode = this.validateTaxCode(uploadItems.getTaxCode16());
				conceptName = AppConstants.CONCEPT_DESC_016;
				break;
			case 17:
				conceptTotal = this.currencyToDouble(uploadItems.getConceptImport17());
				conceptSubtotal = this.currencyToDouble(uploadItems.getConceptSubtotal17());
				conceptTaxCode = this.validateTaxCode(uploadItems.getTaxCode17());
				conceptName = AppConstants.CONCEPT_DESC_017;
				break;
			case 18:
				conceptTotal = this.currencyToDouble(uploadItems.getConceptImport18());
				conceptSubtotal = this.currencyToDouble(uploadItems.getConceptSubtotal18());
				conceptTaxCode = this.validateTaxCode(uploadItems.getTaxCode18());
				conceptName = AppConstants.CONCEPT_DESC_018;
				break;
			case 19:
				conceptTotal = this.currencyToDouble(uploadItems.getConceptImport19());
				conceptSubtotal = this.currencyToDouble(uploadItems.getConceptSubtotal19());
				conceptTaxCode = this.validateTaxCode(uploadItems.getTaxCode19());
				conceptName = AppConstants.CONCEPT_DESC_019;
				break;
			case 20:
				conceptTotal = this.currencyToDouble(uploadItems.getConceptImport20());
				conceptSubtotal = this.currencyToDouble(uploadItems.getConceptSubtotal20());
				conceptTaxCode = this.validateTaxCode(uploadItems.getTaxCode20());
				conceptName = AppConstants.CONCEPT_DESC_020;
				break;
			case 21:
				conceptTotal = this.currencyToDouble(uploadItems.getConceptImport21());
				conceptSubtotal = this.currencyToDouble(uploadItems.getConceptSubtotal21());
				conceptTaxCode = this.validateTaxCode(uploadItems.getTaxCode21());
				conceptName = AppConstants.CONCEPT_DESC_021;
				break;
			case 22://Conceptos con impuesto elegido por usuario
				conceptTotal = this.currencyToDouble(uploadItems.getConceptImport22());
				conceptSubtotal = this.currencyToDouble(uploadItems.getConceptSubtotal22());
				conceptTaxCode = this.getTaxCodeFromValue(uploadItems.getTaxCode22());
				conceptName = AppConstants.CONCEPT_DESC_022;
				break;
			case 23:
				conceptTotal = this.currencyToDouble(uploadItems.getConceptImport23());
				conceptSubtotal = this.currencyToDouble(uploadItems.getConceptSubtotal23());
				conceptTaxCode = this.getTaxCodeFromValue(uploadItems.getTaxCode23());
				conceptName = AppConstants.CONCEPT_DESC_023;
				break;
			case 24:
				conceptTotal = this.currencyToDouble(uploadItems.getConceptImport24());
				conceptSubtotal = this.currencyToDouble(uploadItems.getConceptSubtotal24());
				conceptTaxCode = this.getTaxCodeFromValue(uploadItems.getTaxCode24());
				conceptName = AppConstants.CONCEPT_DESC_024;
				break;
			case 25:
				conceptTotal = this.currencyToDouble(uploadItems.getConceptImport25());
				conceptSubtotal = this.currencyToDouble(uploadItems.getConceptSubtotal25());
				conceptTaxCode = this.getTaxCodeFromValue(uploadItems.getTaxCode25());
				conceptName = AppConstants.CONCEPT_DESC_025;
				break;
			case 26:
				conceptTotal = this.currencyToDouble(uploadItems.getConceptImport26());
				conceptSubtotal = this.currencyToDouble(uploadItems.getConceptSubtotal26());
				conceptTaxCode = this.getTaxCodeFromValue(uploadItems.getTaxCode26());
				conceptName = AppConstants.CONCEPT_DESC_026;
				break;
			default:
				break;
			}
			
			additionalMessage = "<br />Factura para el concepto de " + conceptName + ".";			
			if(conceptNumber > 15 && conceptTotal > 0D && "".equals(conceptTaxCode)) {
				return "El código del impuesto no es válido." + additionalMessage;
			}

			if(conceptNumber > 15 && conceptTotal > 0D && conceptSubtotal == 0D) {
				return "El monto del Subtotal no es válido." + additionalMessage;
			}

			if(conceptNumber > 15 && conceptTotal == 0D && conceptSubtotal > 0D) {
				return "El monto del Total no es válido." + additionalMessage;
			}
			
			if(itemXML != null && itemXML.getSize() > 0 && itemPDF != null && itemPDF.getSize() > 0) {
		        InvoiceDTO inv = null;		        
		        String ctXML = itemXML.getContentType().trim();
		        String ctPDF = itemPDF.getContentType().trim();
		        
	            if(!"application/pdf".equals(ctPDF)) {
	            	return "El documento cargado de tipo .pdf no es válido." + additionalMessage;
	            }
	            
	            if(!"text/xml".equals(ctXML)) {
	            	return "El documento cargado de tipo .xml no es válido." + additionalMessage;
	            }
	            
	            if(!AppConstants.OTHER_FIELD.equals(tipoComprobante)) {
	                inv = documentsService.getInvoiceXmlFromBytes(itemXML.getBytes());
	                if(inv != null) {
		            	if(AppConstants.INVOICE_FIELD.equals(tipoComprobante) && !"I".equals(inv.getTipoComprobante())){
		                	return "El documento cargado no es de tipo FACTURA (Tipo Comprobante = I)." + additionalMessage;
		            	}
		            	
		            	if(AppConstants.NC_FIELD.equals(tipoComprobante) && !"E".equals(inv.getTipoComprobante())){
		                	return "El documento cargado no coresponde a una NOTA DE CREDITO (Tipo Comprobante = E)." + additionalMessage;
		            	}
		            	
		            	if(AppConstants.PAYMENT_FIELD.equals(tipoComprobante) && !"P".equals(inv.getTipoComprobante())){
		                	return "El documento cargado no corresponde a un COMPLEMENTO DE PAGO (Tipo Comprobante = P)." + additionalMessage;
		            	}
		            	
						if(udcCfdi != null) {
							if(!"".equals(udcCfdi.getStrValue1())) {
								if("TRUE".equals(udcCfdi.getStrValue1())) {
									String vcfdi = validaComprobanteSAT(inv);
									if(!"".equals(vcfdi)) {
										return "Error de validación ante el SAT, favor de validar con su emisor fiscal." + additionalMessage;
									}
									
									String vNull = validateInvNull(inv);
									if(!"".equals(vNull)) {
										return "Error al validar el archivo XML, no se encontró el campo " + vNull + "." + additionalMessage;
									}
								}
							}
						}else {
							String vcfdi = validaComprobanteSAT(inv);
							if(!"".equals(vcfdi)) {
								return "Error de validación ante el SAT, favor de validar con su emisor fiscal." + additionalMessage;
							}
							
							String vNull = validateInvNull(inv);
							if(!"".equals(vNull)) {
								return "Error al validar el archivo XML, no se encontró el campo " + vNull + "." + additionalMessage;
							}
						}
						/*
						String fechaFactura = inv.getFechaTimbrado();
						fechaFactura = fechaFactura.replace("T", " ");
						SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DATE_PATTERN);
						Date invDate = null;
						
						try {
							invDate = sdf.parse(fechaFactura);
						}catch(Exception e) {
							e.printStackTrace();
						}
						*/
						
						String rfcEmisor = inv.getRfcEmisor();
						String invCurrency = inv.getMoneda().trim();		
						double exchangeRate = inv.getTipoCambio();

						
						boolean allRules = true;						
						if(supExclList != null) {
							for(UDC udc : supExclList) {
								if(rfcEmisor.equals(udc.getStrValue1())){
									allRules = false;
									break;
								}
							}
						}
						
						boolean isCompanyOK = false;
				    	UDC udcCompany = udcService.searchBySystemAndKey("COMPANYCB", company);    	
				    	if(udcCompany != null) {
							if(udcCompany.getStrValue1().equals(inv.getRfcReceptor().trim())) {
								isCompanyOK = true;
							}
				    	}
				    	if(!isCompanyOK) {
				    		return "El RFC de la compañia no corresponde con el RFC del receptor de la factura " + inv.getRfcReceptor().trim() + "." + additionalMessage;
				    	}
				    	
						String accountingAcc = "";
						List<UDC> accountingAccList = udcDao.searchBySystem("RFCACCOUNTINGACC");
						if(accountingAccList != null) {
							for(UDC udcAcc : accountingAccList) {
								if(udcAcc.getUdcKey().equals(company) && udcAcc.getStrValue1().equals(inv.getMoneda())) {
									accountingAcc = udcAcc.getStrValue2().trim();
									break;
								}
							}
						}
						
						if("".equals(accountingAcc)) {
							return "La compañía no tiene una cuenta contable asignada para la moneda " + inv.getMoneda() + " en el portal de proveedores." + additionalMessage;
						}
						
						if(!mainInvCurrency.equals(invCurrency)) {
							return "La moneda de la factura " + invCurrency + " es diferente a la moneda de la factura principal " + mainInvCurrency + "." + additionalMessage;
						}
						
						//VALIDACIONES DEL XML
						if(allRules) {

							List<Receipt> recUuidList = purchaseOrderService.getReceiptsByUUID(inv.getUuid());
							if(recUuidList != null) {
								if(recUuidList.size()>0)
									return "La factura que intenta ingresar ya se fue cargada previamente en una orden de compra." + additionalMessage;
							}
							
							List<CustomBroker> fdUuidList = customBrokerDao.getCustomBroker("", "", inv.getUuid(), "FACTURA", 0, 1);
							if(fdUuidList != null) {
								if(fdUuidList.size()>0)
									return "La factura que intenta ingresar ya fue cargada previamente." + additionalMessage;
							}
							
							if(!AppConstants.DEFAULT_CURRENCY.equals(invCurrency)) {
								if(exchangeRate == 0) {
									return "La moneda de la factura es " + invCurrency + " sin embargo, no existe definido un tipo de cambio." + additionalMessage;
								}
							}
							
							/*
							int currentYear = Calendar.getInstance().get(Calendar.YEAR);
							Calendar cal = Calendar.getInstance();
							cal.set(Calendar.YEAR, currentYear);
							cal.set(Calendar.DAY_OF_YEAR, 1);    
							Date startYear = cal.getTime();
							try {
								if(invDate.compareTo(startYear) < 0) {
									return "La fecha de emisión de la factura no puede ser anterior al primero de Enero del año en curso";
								}
							}catch(Exception e) {
								e.printStackTrace();
								return "Error al obtener la fecha de timbrado de la factura";
							}							
							*/
							if(!AppConstants.REF_METODO_PAGO_PUE.equals(inv.getMetodoPago())){
								return "El método de pago permitido es " + AppConstants.REF_METODO_PAGO_PUE + " y su CFDI contiene el valor " + inv.getMetodoPago() + ". Favor de emitir nuevamente el CFDI con el método de pago antes mencionado." + additionalMessage;			
							}
							
							/*
							if(!AppConstants.REF_FORMA_PAGO.equals(inv.getFormaPago())){
								return  "La forma de pago permitida es " + AppConstants.REF_FORMA_PAGO + " y su CFDI contiene el valor " + inv.getFormaPago() + ". Favor de emitir nuevamente el CFDI con la forma de pago antes mencionada." + additionalMessage;		
							}
							*/
							
							String cfdiReceptor = inv.getReceptor().getUsoCFDI();
							String rfcReceptor = inv.getRfcReceptor();
							
							if(!AppConstants.USO_CFDI.equals(cfdiReceptor)){
								List<UDC> udcList =  udcService.searchBySystem("CFDIEXC");
								boolean usoCfdiExcept = false;
								if(udcList != null) {
									for(UDC udc : udcList) {
										if(udc.getStrValue1().equals(rfcEmisor)){
											usoCfdiExcept = true;
										}
									}
								}
								
								if(usoCfdiExcept) {
									return "El Uso CFDI " + cfdiReceptor + " no es válido para su razón social." + additionalMessage;
								}
							}
					
							/*
							if(rfcEmisor != null) {
								if(!"".equals(rfcEmisor)) {
									if(!s.getRfc().equals(rfcEmisor)) {
										return "La factura ingresada no pertenece al RFC del emisor del proveedor registrado como " + s.getRfc() + "." + additionalMessage;
									}
								}
							}
							*/
							
							boolean receptorValido = false;
							List<UDC> receptores = udcService.searchBySystem("RECEPTOR");
							if(receptores != null) {
								for(UDC udc : receptores) {
									if(udc.getStrValue1().equals(rfcReceptor.trim())) {
										receptorValido = true;
										break;
									}
								}
							}
							if(!receptorValido) {
								return "El RFC receptor " + rfcReceptor + " no es permitido para carga de facturas." + additionalMessage;
							}			
						}
						
	                } else {
	                	return "La estructura del archivo XML no es válida." + additionalMessage;
	                }
	            }
			}
			
		}
		return "";
	}
	
	public void createConceptList(FileConceptUploadBean uploadItem, CustomBroker fiscalDoc, boolean isNationalSupplier){
		
    	try {    		
    		double totalImport = 0;
    		List<UDC> accountingRfc = udcDao.searchBySystem("CPTACCNUMBER");
    		if(accountingRfc != null) {
    	    	Set<CustomBrokerConcept> fdConceptList = new HashSet<CustomBrokerConcept>();
    	    	CustomBrokerConcept fdConcept;
    	    	CommonsMultipartFile itemXML;
    			double currentTotal;
    			double currentSubtotal;
    			double currentDiscount;
    			double currentTaxAmount;
    			double currentExchangeRate;
    			String currentAccountingAcc;
    			String currentTaxCode;
    			String currentAccount;
    			String currentConceptName;
    			String currentUuid;
    			String currentCurrencyMode;
    			String currentCurrencyCode;
        		String currentFolio;
        		String currentSerie;
        		String currentInvoiceDate;
        		String currentRfcEmisor;
        		String currentRfcReceptor;        		
    			InvoiceDTO inv;
    			boolean isDocumentType;
    			
    			for(UDC udc : accountingRfc) {
    				isDocumentType = false;
    				inv = null;
    				itemXML = null;
    				currentUuid = "";
    				currentAccountingAcc = "";
    				currentConceptName = "";
    				currentCurrencyMode = "";
    				currentCurrencyCode = "";
    				currentFolio = "";
    				currentSerie = "";
    				currentInvoiceDate = "";
    				currentRfcEmisor = "";
    				currentRfcReceptor = "";
    				currentTotal = 0;
    				currentSubtotal = 0;
    				currentDiscount = 0;
    				currentTaxAmount = 0;
    				currentExchangeRate = 0;
    				currentTaxCode = AppConstants.INVOICE_TAX0;
    				currentAccount = fiscalDoc.getCentroCostos().concat(".").concat(udc.getStrValue1());
    				
    				switch (udc.getUdcKey()) {
    				
    					//Conceptos Con Comprobante Fiscal
    				case "CONCEPT001":
    					currentConceptName = AppConstants.CONCEPT_001;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport1());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept1_1();
    					break;
    				case "CONCEPT002":
    					currentConceptName = AppConstants.CONCEPT_002;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport2());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept2_1();
    					break;
    				case "CONCEPT003":
    					currentConceptName = AppConstants.CONCEPT_003;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport3());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept3_1();
    					break;
    				case "CONCEPT004":
    					currentConceptName = AppConstants.CONCEPT_004;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport4());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept4_1();
    					break;
    				case "CONCEPT005":
    					currentConceptName = AppConstants.CONCEPT_005;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport5());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept5_1();
    					break;
    				case "CONCEPT006":
    					currentConceptName = AppConstants.CONCEPT_006;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport6());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept6_1();
    					break;
    				case "CONCEPT007":
    					currentConceptName = AppConstants.CONCEPT_007;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport7());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept7_1();
    					break;
    				case "CONCEPT008":
    					currentConceptName = AppConstants.CONCEPT_008;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport8());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept8_1();
    					break;
    				case "CONCEPT009":
    					currentConceptName = AppConstants.CONCEPT_009;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport9());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept9_1();
    					break;
    				case "CONCEPT010":
    					currentConceptName = AppConstants.CONCEPT_010;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport10());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept10_1();
    					break;
    				case "CONCEPT011":
    					currentConceptName = AppConstants.CONCEPT_011;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport11());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept11_1();
    					break;
    				case "CONCEPT012":
    					currentConceptName = AppConstants.CONCEPT_012;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport12());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept12_1();
    					break;
    				case "CONCEPT013":
    					currentConceptName = AppConstants.CONCEPT_013;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport13());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept13_1();
    					break;
    				case "CONCEPT014":
    					currentConceptName = AppConstants.CONCEPT_014;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport14());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept14_1();
    					break;
    				case "CONCEPT015":
    					currentConceptName = AppConstants.CONCEPT_015;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport15());
    					currentSubtotal = currentTotal;
    					itemXML = uploadItem.getFileConcept15_1();
    					break;
    					//Conceptos Sin Comprobante Fiscal
    				case "CONCEPT016":
    					currentConceptName = AppConstants.CONCEPT_016;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport16());
    					currentSubtotal = currentTotal;
    					if(isNationalSupplier && currentTotal > 0D) {
        					currentSubtotal = currencyToDouble(uploadItem.getConceptSubtotal16());
        					currentTaxCode = validateTaxCode(uploadItem.getTaxCode16());
        					currentTaxAmount = currentTotal - currentSubtotal;
    					}
    					break;
    				case "CONCEPT017":
    					currentConceptName = AppConstants.CONCEPT_017;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport17());
    					currentSubtotal = currentTotal;
    					if(isNationalSupplier && currentTotal > 0D) {
        					currentSubtotal = currencyToDouble(uploadItem.getConceptSubtotal17());
        					currentTaxCode = validateTaxCode(uploadItem.getTaxCode17());
        					currentTaxAmount = currentTotal - currentSubtotal;
    					}
    					break;
    				case "CONCEPT018":
    					currentConceptName = AppConstants.CONCEPT_018;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport18());
    					currentSubtotal = currentTotal;
    					if(isNationalSupplier && currentTotal > 0D) {
        					currentSubtotal = currencyToDouble(uploadItem.getConceptSubtotal18());
        					currentTaxCode = validateTaxCode(uploadItem.getTaxCode18());
        					currentTaxAmount = currentTotal - currentSubtotal;
    					}
    					break;
    				case "CONCEPT019":
    					currentConceptName = AppConstants.CONCEPT_019;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport19());
    					currentSubtotal = currentTotal;
    					if(isNationalSupplier && currentTotal > 0D) {
        					currentSubtotal = currencyToDouble(uploadItem.getConceptSubtotal19());
        					currentTaxCode = validateTaxCode(uploadItem.getTaxCode19());
        					currentTaxAmount = currentTotal - currentSubtotal;
    					}
    					break;
    				case "CONCEPT020":
    					currentConceptName = AppConstants.CONCEPT_020;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport20());
    					currentSubtotal = currentTotal;
    					if(isNationalSupplier && currentTotal > 0D) {
        					currentSubtotal = currencyToDouble(uploadItem.getConceptSubtotal20());
        					currentTaxCode = validateTaxCode(uploadItem.getTaxCode20());
        					currentTaxAmount = currentTotal - currentSubtotal;
    					}
    					break;
    				case "CONCEPT021":
    					currentConceptName = AppConstants.CONCEPT_021;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport21());
    					currentSubtotal = currentTotal;
    					if(isNationalSupplier && currentTotal > 0D) {
        					currentSubtotal = currencyToDouble(uploadItem.getConceptSubtotal21());
        					currentTaxCode = validateTaxCode(uploadItem.getTaxCode21());
        					currentTaxAmount = currentTotal - currentSubtotal;
    					}
    					break;
    				case "CONCEPT022":
    					currentConceptName = AppConstants.CONCEPT_022;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport22());
    					currentSubtotal = currentTotal;
    					if(isNationalSupplier && currentTotal > 0D) {
        					currentSubtotal = currencyToDouble(uploadItem.getConceptSubtotal22());
        					currentTaxCode = getTaxCodeFromValue(uploadItem.getTaxCode22());
        					currentTaxAmount = currentTotal - currentSubtotal;
    					}
    					break;
    				case "CONCEPT023":
    					currentConceptName = AppConstants.CONCEPT_023;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport23());
    					currentSubtotal = currentTotal;
    					if(isNationalSupplier && currentTotal > 0D) {
        					currentSubtotal = currencyToDouble(uploadItem.getConceptSubtotal23());
        					currentTaxCode = getTaxCodeFromValue(uploadItem.getTaxCode23());
        					currentTaxAmount = currentTotal - currentSubtotal;
    					}
    					break;
    				case "CONCEPT024":
    					currentConceptName = AppConstants.CONCEPT_024;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport24());
    					currentSubtotal = currentTotal;
    					if(isNationalSupplier && currentTotal > 0D) {
        					currentSubtotal = currencyToDouble(uploadItem.getConceptSubtotal24());
        					currentTaxCode = getTaxCodeFromValue(uploadItem.getTaxCode24());
        					currentTaxAmount = currentTotal - currentSubtotal;
    					}
    					break;
    				case "CONCEPT025":
    					currentConceptName = AppConstants.CONCEPT_025;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport25());
    					currentSubtotal = currentTotal;
    					if(isNationalSupplier && currentTotal > 0D) {
        					currentSubtotal = currencyToDouble(uploadItem.getConceptSubtotal25());
        					currentTaxCode = getTaxCodeFromValue(uploadItem.getTaxCode25());
        					currentTaxAmount = currentTotal - currentSubtotal;
    					}
    					break;
    				case "CONCEPT026":
    					currentConceptName = AppConstants.CONCEPT_026;
    					currentTotal = currencyToDouble(uploadItem.getConceptImport26());
    					currentSubtotal = currentTotal;
    					if(isNationalSupplier && currentTotal > 0D) {
        					currentSubtotal = currencyToDouble(uploadItem.getConceptSubtotal26());
        					currentTaxCode = getTaxCodeFromValue(uploadItem.getTaxCode26());
        					currentTaxAmount = currentTotal - currentSubtotal;
    					}
    					break;
    				default:
    					break;
    				}
    				
    				currentUuid = fiscalDoc.getUuidFactura();
    				currentCurrencyCode = fiscalDoc.getCurrencyCode();
    				currentCurrencyMode = fiscalDoc.getCurrencyMode();
    				currentAccountingAcc = fiscalDoc.getAccountingAccount();
    				currentInvoiceDate = fiscalDoc.getInvoiceDate();
    				currentExchangeRate = fiscalDoc.getTipoCambio();
    				
					if(isNationalSupplier) {
						if(itemXML != null && itemXML.getSize() > 0 && "text/xml".equals(itemXML.getContentType().trim())) {
							inv = documentsService.getInvoiceXmlFromBytes(itemXML.getBytes());
							if(inv != null) {
								isDocumentType = true;
			    				currentSubtotal = inv.getSubTotal();			    				
			    				currentDiscount = inv.getDescuento();
			    				currentTotal = inv.getTotal();
			    				currentTaxCode = getInvoiceTaxCode(inv);
			    				currentUuid = inv.getUuid();
			    				currentFolio = inv.getFolio();
			    				currentSerie = inv.getSerie();
			    				currentInvoiceDate = inv.getFechaTimbrado();
			    				currentRfcEmisor = inv.getRfcEmisor();
			    				currentRfcReceptor = inv.getRfcReceptor();
			    				currentExchangeRate = inv.getTipoCambio();
			    				currentTaxAmount = inv.getImpuestos();
			    				
			    				//Calcula Total de Impuestos
			    				if(inv.getTotalRetenidos() > 0D) {
			    					currentTaxAmount = currentTaxAmount - inv.getTotalRetenidos();
			    				}
			    				
			    				String invCurrency = inv.getMoneda();
			    				if("MXN".equals(invCurrency)) {			
			    					currentCurrencyCode = "MXP";
			    					currentCurrencyMode = AppConstants.CURRENCY_MODE_DOMESTIC;
			    				} else {
			    					currentCurrencyCode = invCurrency;
			    					currentCurrencyMode = AppConstants.CURRENCY_MODE_FOREIGN;			
			    				}

			    				List<UDC> comUDCList =  udcService.searchBySystem("COMPANYDOMESTIC");
			    				if(comUDCList != null && !comUDCList.isEmpty()) {
			    					for(UDC companyCurrUDC : comUDCList) {
			    						if(companyCurrUDC.getStrValue1().trim().equals(fiscalDoc.getCompanyFD()) && !"".equals(companyCurrUDC.getStrValue2().trim())) {
			    							if(invCurrency.equals(companyCurrUDC.getStrValue2().trim())) {
			    								currentCurrencyMode = AppConstants.CURRENCY_MODE_DOMESTIC;
			    							} else {
			    								currentCurrencyMode = AppConstants.CURRENCY_MODE_FOREIGN;
			    							}
			    							break;
			    						}
			    					}
			    				}
			    				
			    				List<UDC> supDomUDCList =  udcService.searchBySystem("SUPPLIERDOMESTIC");
			    				if(supDomUDCList != null && !supDomUDCList.isEmpty()) {
			    					for(UDC supplier : supDomUDCList) {
			    						if(supplier.getStrValue1().trim().equals(fiscalDoc.getAddressNumber()) && !"".equals(supplier.getStrValue2().trim())) {
			    							if(invCurrency.equals(supplier.getStrValue2().trim())) {
			    								currentCurrencyMode = AppConstants.CURRENCY_MODE_DOMESTIC;
			    							} else {
			    								currentCurrencyMode = AppConstants.CURRENCY_MODE_FOREIGN;
			    							}
			    							break;
			    						}
			    					}
			    				}
			    				
								List<UDC> accountingAccList = udcDao.searchBySystem("RFCACCOUNTINGACC");
								if(accountingAccList != null) {
									for(UDC udcAcc : accountingAccList) {
										if(udcAcc.getUdcKey().equals(fiscalDoc.getCompanyFD()) && udcAcc.getStrValue1().equals(invCurrency)) {
											currentAccountingAcc = udcAcc.getStrValue2().trim();
											break;
										}
									}
								}
							}	
						}
					}
					
    				if(currentTotal > 0D) {
    					totalImport = totalImport + currentTotal;					
    	        		fdConcept = new CustomBrokerConcept();
    	        		fdConcept.setAddressNumber(fiscalDoc.getAddressNumber());
    	        		fdConcept.setGlOffset(fiscalDoc.getGlOffset());
    	        		fdConcept.setAccountingAccount(currentAccountingAcc);    	        		    	        	
    	        		fdConcept.setConceptAccount(currentAccount);
    	        		fdConcept.setConceptName(currentConceptName);
    	        		fdConcept.setCurrencyCode(currentCurrencyCode);
    	        		fdConcept.setCurrencyMode(currentCurrencyMode);    	        		
    	        		fdConcept.setDocumentType(isDocumentType);
    	        		fdConcept.setFolio(currentFolio);
    	        		fdConcept.setSerie(currentSerie);    	        		
    	        		fdConcept.setInvoiceDate(currentInvoiceDate);
    	        		fdConcept.setRfcEmisor(currentRfcEmisor);
    	        		fdConcept.setRfcReceptor(currentRfcReceptor);
    	        		fdConcept.setSubtotal(currentSubtotal);
    	        		fdConcept.setImpuestos(currentTaxAmount);
    	        		fdConcept.setDescuento(currentDiscount);
    	        		fdConcept.setAmount(currentTotal);
    	        		fdConcept.setTaxCode(currentTaxCode);
    	        		fdConcept.setTipoCambio(currentExchangeRate);
    	        		fdConcept.setUuid(currentUuid);
    	        		fdConcept.setStatus(AppConstants.STATUS_INPROCESS);
    	        		fdConceptList.add(fdConcept);
    				}			
    			}
    			
    	    	if(!fdConceptList.isEmpty()) {
    	    		fiscalDoc.setConcepts(fdConceptList);
    	    	}
    		}
    		fiscalDoc.setConceptTotalAmount(totalImport);
    		
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
	}
	
	private String validaComprobanteSAT(InvoiceDTO inv) {
		String payload = PayloadProducer.getCFDIPayload(inv);
		String response = HTTPRequestService.performSoapCall(payload);
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
	
	public String updateDocument(CustomBroker doc, String status, String centroCostos, String conceptoArticulo, String company, String note, String documentType, String currentApprover, String nextApprover, String step, boolean cancelOrder) {
		
		CustomBroker o = customBrokerDao.getById(doc.getId());
		Supplier s = supplierService.searchByAddressNumber(o.getAddressNumber());
		
		if(AppConstants.FISCAL_DOC_APPROVED.equals(status)) {
			
			if(AppConstants.FIRST_STEP.equals(step)){
			      UDC udc = this.udcService.searchBySystemAndStrValue("APPROVERINV", AppConstants.INV_SECOND_APPROVER, nextApprover);
			      String nextApproverEmail = "";
			      if (udc != null) {
			    	nextApproverEmail = udc.getStrValue2();
			    	o.setCurrentApprover(udc.getStrValue1());
			        o.setNextApprover("");			        
			      }
			      
			      o.setStatus(AppConstants.STATUS_INPROCESS);
			      o.setApprovalStatus(AppConstants.STATUS_INPROCESS);
			      o.setApprovalStep(AppConstants.SECOND_STEP);
				  o.setNoteRejected(note);				  
				  if(o.getConcepts() != null) {
					  for(CustomBrokerConcept c : o.getConcepts()) {
						  c.setStatus(AppConstants.STATUS_INPROCESS);
					  }
				  }
				  customBrokerDao.updateDocument(o);
				  
				  try {
					  String emailContent = AppConstants.EMAIL_INV_APPROVAL_MSG_1_NO_OC;
			    		emailContent = emailContent.replace("_UUID_", o.getUuidFactura());
			    		emailContent = emailContent.replace("_SUPPLIER_", s.getAddresNumber());
					  
					  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
					  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_NO_OC, 
					  this.stringUtils.prepareEmailContent(emailContent), 
					  nextApproverEmail);
					  emailAsyncSup.setMailSender(mailSenderObj);
					  Thread emailThreadSup = new Thread(emailAsyncSup);
					  emailThreadSup.start();
				  } catch (Exception e) {	
					  log4j.error("Exception" , e);
				  }
				  
			      return "Succ Update";
			}
			
			if(AppConstants.SECOND_STEP.equals(step)){
				if(AppConstants.INVOICE_FIELD_UDC.equals(documentType) || AppConstants.STATUS_FACT_FOREIGN.equalsIgnoreCase(documentType)) {
					try {
						InvoiceDTO inv = null;
						
						if(AppConstants.INVOICE_FIELD_UDC.equals(documentType)){
							List<UserDocument> docs = documentsDao.searchCriteriaByUuidOnly(o.getUuidFactura());
							if(docs != null) {
								if(docs != null) {
									for(UserDocument u : docs) {
										if(AppConstants.INVOICE_FIELD.equals(u.getFiscalType()) && "text/xml".equals(u.getType())) {
											String xmlStr = new String(u.getContent(), StandardCharsets.UTF_8);
											inv = getInvoiceXmlFromString(xmlStr);								        
									        break;
										}
									}
								}
							}	
						}
						
						if(AppConstants.STATUS_FACT_FOREIGN.equals(documentType)){
							ForeignInvoiceTable foreign = purchaseOrderService.getForeignInvoiceFromUuid(o.getUuidFactura());
							
							if(foreign != null) {
								inv = new InvoiceDTO();
								inv.setTipoComprobante("I");							
								inv.setFechaTimbrado(foreign.getExpeditionDate());
								inv.setTipoCambio(o.getTipoCambio());
								inv.setSubTotal(foreign.getForeignSubtotal());
								inv.setTotalImpuestos(foreign.getForeignTaxes());
								inv.setTotal(foreign.getForeignDebit());
								inv.setFolio(foreign.getInvoiceNumber());
								inv.setSerie("");
								inv.setUuid(foreign.getUuid());								
							}
						}
						
						//Enviar a JDE
						if(inv != null) {
							String resp = eDIService.createNewBatchJournal(o, inv, 0, s, AppConstants.NN_MODULE_BATCHJOURNAL);
							
							if(!"".equals(resp)) {
						    	o.setCurrentApprover("");
						    	o.setNextApprover("");
					        	o.setStatus(AppConstants.FISCAL_DOC_APPROVED);
							    o.setApprovalStatus(AppConstants.FISCAL_DOC_APPROVED);
							    o.setApprovalStep(AppConstants.FINAL_STEP);
								o.setNoteRejected(note);
								if(o.getConcepts() != null) {
									for(CustomBrokerConcept c : o.getConcepts()) {
										c.setStatus(AppConstants.FISCAL_DOC_APPROVED);
									}
								}
								customBrokerDao.updateDocument(o);
								
								//Enviar archivo adjunto a la factura
								try {
									List<UserDocument> listDocument = documentsService.searchCriteriaByRefFiscal(o.getAddressNumber(),
																												o.getUuidFactura());
									if(listDocument != null && !listDocument.isEmpty()) {
										for(UserDocument document : listDocument) {
											if(document.getName() != null && document.getContent() != null
													&& AppConstants.INVOICE_FIELD.equals(document.getFiscalType())
													&& "application/pdf".equals(document.getType())) {
												
												String fileName = document.getUuid() + ".pdf";
							                	File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
							                	Path filePath = Paths.get(file.getAbsolutePath());
							                	Files.write(filePath, document.getContent());
							                	documentsService.sendFileToRemote(file, fileName);
							                	documentsService.update(document, null, null);
							                	log4j.info(documentType + ": Se envía archivo " + fileName);
							                	file.delete();
							                	break;
											}
										}
									}
								} catch (Exception e) {
									log4j.error("Exception" , e);
									e.printStackTrace();
								}
								
					        	try {
									//Email Proveedor
					        		String emailContent = AppConstants.FISCAL_DOC_MAIL_MSJ_INVOICE;
					        		emailContent = emailContent.replace("_UUID_", o.getUuidFactura());

					        		
									String emailSupplier = s.getEmailSupplier();
									EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						            emailAsyncSup.setProperties(AppConstants.FISCAL_DOC_MAIL_SUBJECT_INVOICE, 
						            this.stringUtils.prepareEmailContent(emailContent + AppConstants.EMAIL_PORTAL_LINK), 
						            emailSupplier);
						            emailAsyncSup.setMailSender(mailSenderObj);
						            Thread emailThreadSup = new Thread(emailAsyncSup);
						            emailThreadSup.start();
								} catch (Exception e) {
									log4j.error("Exception" , e);
								}
							} else {
								return "Error JDE";
							}
						}
			            
						return "Success";
				
					}catch(Exception e) {
						log4j.error("Exception" , e);
						e.printStackTrace();
						return "";
					}
				} else {

					try {
				    	o.setCurrentApprover("");
				    	o.setNextApprover("");
			        	o.setStatus(AppConstants.FISCAL_DOC_APPROVED);
					    o.setApprovalStatus(AppConstants.FISCAL_DOC_APPROVED);
					    o.setApprovalStep(AppConstants.FINAL_STEP);
						o.setNoteRejected(note);
						if(o.getConcepts() != null) {
							for(CustomBrokerConcept c : o.getConcepts()) {
								c.setStatus(AppConstants.FISCAL_DOC_APPROVED);
							}
						}
						customBrokerDao.updateDocument(o);
						
						//Email Proveedor
						String emailContent = AppConstants.FISCAL_DOC_MAIL_MSJ_NC;
		        		emailContent = emailContent.replace("_UUID_", o.getUuidNotaCredito());
						
						String emailSupplier = s.getEmailSupplier();
						EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			            emailAsyncSup.setProperties(AppConstants.FISCAL_DOC_MAIL_SUBJECT_NC, 
			            this.stringUtils.prepareEmailContent(emailContent + AppConstants.EMAIL_PORTAL_LINK), 
			            emailSupplier);
			            emailAsyncSup.setMailSender(mailSenderObj);
			            Thread emailThreadSup = new Thread(emailAsyncSup);
			            emailThreadSup.start();
			            
					} catch (Exception e) {
						log4j.error("Exception" , e);
					}
					
					return "Success";
				}				
			}
		}
		
		if(AppConstants.FISCAL_DOC_REJECTED.equals(status)) {
			
			if(AppConstants.INVOICE_FIELD_UDC.equals(documentType) || AppConstants.STATUS_FACT_FOREIGN.equalsIgnoreCase(documentType)) {
				
				try {
					List<UserDocument> docs = documentsDao.searchCriteriaByDescription(o.getAddressNumber(), "MainUUID_".concat(o.getUuidFactura()),true);
					if(docs != null) {
						for(UserDocument u : docs) {
							u.setDescription(u.getDescription().concat("-REJECTED"));
							u.setUuid(u.getUuid().concat("-REJECTED"));
							documentsDao.updateDocuments(u);
						}
					}
					
					if(AppConstants.STATUS_FACT_FOREIGN.equals(documentType)){
						ForeignInvoiceTable foreign = purchaseOrderService.getForeignInvoiceFromUuid(o.getUuidFactura());
						purchaseOrderService.deleteForeignInvoice(foreign);
					}

			    	o.setCurrentApprover("");
			    	o.setNextApprover("");
			    	o.setStatus(AppConstants.FISCAL_DOC_REJECTED);
				    o.setApprovalStatus(AppConstants.FISCAL_DOC_REJECTED);
				    o.setApprovalStep(AppConstants.FINAL_STEP);
					o.setNoteRejected(note);
					o.setUuidFactura(o.getUuidFactura().concat("-REJECTED"));
					if(o.getConcepts() != null) {
						for(CustomBrokerConcept c : o.getConcepts()) {
							c.setStatus(AppConstants.FISCAL_DOC_REJECTED);
						}
					}
					customBrokerDao.updateDocument(o);
					
					try {
						String emailContent = AppConstants.EMAIL_INVOICE_REJECTED_NOTIF;
		        		emailContent = emailContent.replace("_UUID_", o.getUuidFactura());
		        		emailContent = emailContent.replace("_NOTES_", note);
		        		
			        	String emailRecipient = (s.getEmailSupplier());
			        	EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
						emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REJECT_SUP_NO_OC, 
						stringUtils.prepareEmailContent(emailContent),
								emailRecipient);
						emailAsyncSup.setMailSender(mailSenderObj);
						Thread emailThreadSup = new Thread(emailAsyncSup);
						emailThreadSup.start();
					} catch (Exception e) {
						log4j.error("Exception" , e);
					}
					
					return "Rejected";
			
				}catch(Exception e) {
					log4j.error("Exception" , e);
					e.printStackTrace();
					return "";
				}
				
			} else {
				
				try {
					List<UserDocument> docs = documentsDao.searchCriteriaByDescription(o.getAddressNumber(), "MainUUID_".concat(o.getUuidFactura()),true);
					if(docs != null) {
						for(UserDocument u : docs) {
							u.setDescription(u.getDescription().concat("-REJECTED"));
							u.setUuid(u.getUuid().concat("-REJECTED"));
							documentsDao.updateDocuments(u);
						}
					}

			    	o.setCurrentApprover("");
			    	o.setNextApprover("");
			    	o.setStatus(AppConstants.FISCAL_DOC_REJECTED);
				    o.setApprovalStatus(AppConstants.FISCAL_DOC_REJECTED);
				    o.setApprovalStep(AppConstants.FINAL_STEP);
					o.setNoteRejected(note);
					o.setUuidFactura(o.getUuidFactura().concat("-REJECTED"));
					if(o.getConcepts() != null) {
						for(CustomBrokerConcept c : o.getConcepts()) {
							c.setStatus(AppConstants.FISCAL_DOC_REJECTED);
						}
					}
					customBrokerDao.updateDocument(o);
					
					try {
						String emailContent = AppConstants.FISCAL_DOC_MAIL_MSJ_REJECTED;
		        		emailContent = emailContent.replace("_UUID_", o.getUuidNotaCredito());
		        		emailContent = emailContent.replace("_NOTES_", note);
						
						String emailRecipient = (s.getEmailSupplier());
						EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
			            emailAsyncSup.setProperties(AppConstants.EMAIL_NC_REJECT_SUP_NO_OC,
			            this.stringUtils.prepareEmailContent(emailContent + AppConstants.EMAIL_PORTAL_LINK), emailRecipient);
			            emailAsyncSup.setMailSender(mailSenderObj);
			            Thread emailThreadSup = new Thread(emailAsyncSup);
			            emailThreadSup.start();
					} catch (Exception e) {
						log4j.error("Exception" , e);
					}
				} catch (Exception e) {
					log4j.error("Exception" , e);
					e.printStackTrace();
					return "";
				}
			}
            
			return "Rejected";
		}

		return "";
	}
	
	public int getTotalRecords(String addressNumber, String status, String uuid, String documentType, int start, int limit) {
		return customBrokerDao.getTotalRecords(addressNumber, status, uuid, documentType,  start, limit);
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
	
	private String validateInvNull(InvoiceDTO inv) {
		
		if(inv.getSello() == null) {
			return "Sello";
		}
		
		if(inv.getCertificado() == null) {
			return "Certificado";
		}
		
		if(inv.getLugarExpedicion() == null) {
			return "LugarExpedicion";
		}
		
		if(inv.getFecha() == null) {
			return "Fecha";
		}
		
		if(inv.getFechaTimbrado() == null) {
			return "FechaTimbrado";
		}
		
		if(inv.getSubTotal() == 0) {
			return "SubTotal";
		}
		
		if(inv.getMoneda() == null) {
			return "Moneda";
		}
		
		if(inv.getTotal() == 0) {
			return "Total";
		}
		
		if(inv.getRfcEmisor() == null) {
			return "RfcEmisor";
		}
		
		if(inv.getRfcReceptor() == null) {
			return "RfcReceptor";
		}
		
		if(inv.getReceptor() == null) {
			return "Receptor";
		}else {
			if(inv.getReceptor().getUsoCFDI() == null) {
				return "UsoCFDI";
			}
		}
		
		if(inv.getEmisor() == null) {
			return "Emisor";
		}else {
			if(inv.getEmisor().getRegimenFiscal() == null) {
				return "RegimenFiscal";
			}
		}
	
		if(inv.getConcepto() == null) {
			return "Concepto";
		}else {
			List<Concepto> conceptos = inv.getConcepto();
			if(conceptos != null) {
				for(Concepto concepto : conceptos) {
					
					if(concepto.getClaveProdServ() == null) {
						return "ClaveProdServ";
					}
					
					Impuestos impuestos = concepto.getImpuestos();
					if(impuestos != null) {
						Traslados traslados = impuestos.getTraslados();
						if(traslados != null) {
							List<Traslado> traslado = traslados.getTraslado();
							if(traslado != null) {
								for(Traslado t : traslado) {
									
									if(t.getTipoFactor() == null) {
										return "TipoFactor";
									} else {
										if("Tasa".equals(t.getTipoFactor())) {
											if(t.getTasaOCuota() == null) {
												return "TasaOCuota";
											}
										}									
									}
								}
							}
						}
					}					
				}
			}
		}

		return "";
	}

	public String getInvoiceTaxCode(InvoiceDTO inv) {
		String taxCode = AppConstants.INVOICE_TAX0;
		String taxRateTranslated = "NA";
		String taxRateRetained = "NA";

		List<Concepto> conceptos = inv.getConcepto();
		if(conceptos != null) {
			for(Concepto concepto : conceptos) {
				Impuestos impuestos = concepto.getImpuestos();
				if(impuestos != null) {
					
					//Obtener impuesto trasladado
					Traslados traslados = impuestos.getTraslados();
					if(traslados != null) {
						List<Traslado> traslado = traslados.getTraslado();
						if(traslado != null) {
							for(Traslado t : traslado) {
								if(t.getTipoFactor() != null) {
									if("Tasa".equals(t.getTipoFactor())) {
										if(t.getTasaOCuota() != null 
												&& !"".equals(t.getTasaOCuota().trim()) 
												&& !AppConstants.INVOICE_TAX_RATE_TAX0.equals(t.getTasaOCuota().trim())) {
											taxRateTranslated = t.getTasaOCuota().trim();
											break;
										}
									}
								}
							}
						}
					}
					
					//Obtener impuesto retenido
					Retenciones retenciones = impuestos.getRetenciones();
					if(retenciones != null) {
						List<Retencion> retencion = retenciones.getRetencion();
						if(retencion != null) {
							for(Retencion t : retencion) {
								if("002".equals(t.getImpuesto())){
									if(t.getTasaOCuota()!= null
											&& !"".equals(t.getTasaOCuota().trim())
											&& !AppConstants.INVOICE_TAX_RATE_TAX0.equals(t.getTasaOCuota().trim())) {
										taxRateRetained = t.getTasaOCuota().trim(); 
										break;
									}
								}
							}
						}
					}
					
				}
			}
		}
		
		List<UDC> udcTaxUDCList = udcDao.searchBySystem("F43121TXA1");
		if(udcTaxUDCList != null) {
			for(UDC udc : udcTaxUDCList) {
				if(taxRateTranslated.equals(udc.getStrValue2().trim()) && taxRateRetained.equals(udc.getStrValue1().trim())) {
					taxCode = udc.getUdcKey().trim();
					break;
				}
			}	
		}
		
		return taxCode;
	}
	
	public double currencyToDouble(String amount) {
		double dblValue = 0;
		if(amount != null && !"".equals(amount.trim())) {
			dblValue = Double.valueOf(amount.replace("$", "").replace(",", "").replace(" ", "")).doubleValue();
		}
		return dblValue;
	}
	
	public String validateTaxCode(String taxCode) {
		String strValue = "";
		if(taxCode != null && !"".equals(taxCode.trim()) && !taxCode.trim().contains("Selecciona") && !taxCode.trim().contains("Select")) {
			strValue = taxCode;
		}
		return strValue;
	}
	
	public String getTaxCodeFromValue(String taxValue) {
		String strValue = "";
		if(taxValue != null && !"".equals(taxValue.trim()) && !taxValue.trim().contains("Selecciona") && !taxValue.trim().contains("Select")) {
			List<UDC> taxCodeUDCList = udcDao.searchBySystem("INVTAXCODE");
			if(taxCodeUDCList != null) {
				for(UDC udc : taxCodeUDCList) {
					if(taxValue.trim().equals(udc.getStrValue1().trim())) {
						strValue = udc.getUdcKey();
						break;
					}
				}
			}
		}
		return strValue;
	}

	public List<CustomBroker> getPendingPaymentInvoices(String addressNumber, String folio) {
		return customBrokerDao.getPendingPaymentInvoices(addressNumber, folio);
	}

	public List<CustomBroker> getComplPendingInvoice(String addressBook) {
		return customBrokerDao.getComplPendingInvoice(addressBook);
	}
	
	public void saveDocument(CustomBroker doc) {
		customBrokerDao.saveDocument(doc);
	}
	
	public void updateDocument(CustomBroker doc) {
		customBrokerDao.updateDocument(doc);
	}

	//@Scheduled(fixedDelay = 300000, initialDelay = 30000)
	public void sendInvoiceBySelection() {
		List<String> uuids = new ArrayList<String>();
		uuids.add("971e3039-f467-454d-a9b3-d27ec37a8a6c");
		uuids.add("d2bd9d72-df85-4391-8316-3f9dfeff594a");
		
		for(String uuid : uuids) {
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
					        log4j.info("Sent: " + inv.getUuid());
							}catch(Exception e){
								log4j.error("Exception" , e);
							}
						}
					}
				}
				}
		}
				
	}
	
	//@Scheduled(fixedDelay = 9200000, initialDelay = 15000)
	public void sendForeignInvoiceBySelection() {
		List<String> uuids = new ArrayList<String>();
		uuids.add("02176814-2021-0306-1019-008450009583");
		
		for(String uuid : uuids) {
			
			List<Receipt> requestedReceiptList = purchaseOrderDao.getOrderReceiptsByUuid(uuid);
			if(requestedReceiptList != null && requestedReceiptList.size() > 0) {
				Receipt rec = requestedReceiptList.get(0); 
				
				Supplier s = supplierService.searchByAddressNumber(rec.getAddressNumber());
				
				ForeingInvoice o = new ForeingInvoice();
				o.setOrderNumber(rec.getOrderNumber());
				o.setOrderType(rec.getOrderType());
				o.setAddressNumber(rec.getAddressNumber());
				ForeignInvoiceTable fit = purchaseOrderDao.getForeignInvoice(o);
				
				ForeingInvoice fi = new ForeingInvoice();
				fi.setAddressNumber(fit.getAddressNumber());
				fi.setOrderNumber(fit.getOrderNumber());
				fi.setCountry(fit.getCountry());
				fi.setExpeditionDate(fit.getExpeditionDate());
				fi.setForeignCurrency(fit.getForeignCurrency());
				fi.setForeignDebit(fit.getForeignDebit());
				fi.setForeignDescription(fit.getForeignDescription());
				fi.setForeignNotes(fit.getForeignNotes());
				fi.setForeignRetention(fit.getForeignRetention());
				fi.setForeignSubtotal(fit.getForeignSubtotal());
				fi.setForeignTaxes(fit.getForeignTaxes());
				fi.setInvoiceNumber(fit.getInvoiceNumber());
				fi.setTaxId(fit.getTaxId());
				fi.setUsuarioImpuestos(fit.getUsuarioImpuestos());
				fi.setUuid(fit.getUuid());
				fi.setFolio(rec.getFolio());
				
				PurchaseOrder po = purchaseOrderDao.searchbyOrderAndAddressBook(rec.getOrderNumber(), rec.getAddressNumber(), rec.getOrderType());
				
				String resp = "DOC:" + eDIService.createNewForeignVoucher(po, fi, 0, s, requestedReceiptList, AppConstants.NN_MODULE_VOUCHER);
				log4j.info(resp);
			}	
		}
	}
	
	//@Scheduled(fixedDelay = 9200000, initialDelay = 15000)
		public void sendInvoiceJournalBySelection() {
			List<String> uuids = new ArrayList<String>();
			uuids.add("a088eaf0-c081-42a2-b6fe-4ca7ca6781bb");
			uuids.add("ccd32f88-df8a-4f28-8418-bea594aac4d8");

			for(String uuid : uuids) {
				List<UserDocument> docs = documentsDao.searchCriteriaByUuidOnly(uuid);
				if(docs != null) {
					InvoiceDTO inv = null;
					if(docs != null) {
						for(UserDocument u : docs) {
							Supplier s = supplierService.searchByAddressNumber(u.getAddressBook());
							if(AppConstants.INVOICE_FIELD.equals(u.getFiscalType()) && "text/xml".equals(u.getType()) && "Honorarios".equals(u.getDocumentType())) {
								try {
								String xmlStr = new String(u.getContent(), StandardCharsets.UTF_8);
								inv = getInvoiceXmlFromString(xmlStr);
								CustomBroker o = customBrokerDao.getCustomBrokerByUuid(u.getUuid());
								log4j.info(u.getDocumentType());
								if(o != null) {
									eDIService.createNewBatchJournal(o, inv, 0, s,  AppConstants.NN_MODULE_BATCHJOURNAL);
									log4j.info("Sent: " + inv.getUuid());
								}
								}catch(Exception e){
									log4j.error("Exception" , e);
								}
							}
						}
					}
					}
			}					
		}
		
	    //@Scheduled(fixedDelay = 6000000, initialDelay = 10000)
		public void restoreCustomBrokerInvoice() {
			String[] uuidArray = new String[]{};
			/*
			String[] uuidArray = new String[]{
					"02201199-2021-0827-1355-20004042NOOC",
					"02201199-2021-0827-1356-13004042NOOC",
					"02201199-2021-0827-1358-25004042NOOC",
					"01306683-2021-0827-1404-14004043NOOC",
					"01306683-2021-0827-1411-11004043NOOC",
					"409B8000-24B4-4D5B-ADF6-51C87E9C391E",
					"1739404B-14D8-426E-9CBA-37CC5018EF04",
					"AFA59D5F-BA76-444A-AEF0-F28D0F6CFF16"
					};
			*/
			for(String uuid : uuidArray) {
				List<CustomBroker> fdList = this.getCustomBroker("", "", uuid, "", 0, 1);
				
				if(fdList != null && !fdList.isEmpty()) {
					CustomBroker fd = fdList.get(0);
					String documentType = fd.getType();
					String status = AppConstants.FISCAL_DOC_APPROVED;				
					String step = AppConstants.SECOND_STEP;
					//!!Comentar líneas de envío de correo electrónico temporalmente Líneas 1425-1430
					this.updateDocument(fd, status, "", "", "", "", documentType, "", "", step, false);
				}			
			}
			log4j.info("Fin Proceso... ");
		}
		
		
}
