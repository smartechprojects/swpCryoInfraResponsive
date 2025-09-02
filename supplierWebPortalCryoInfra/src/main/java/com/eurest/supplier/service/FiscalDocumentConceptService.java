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
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eurest.supplier.dao.DocumentsDao;
import com.eurest.supplier.dao.FiscalDocumentConceptDao;
import com.eurest.supplier.dao.FiscalDocumentDao;
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
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.ForeignInvoiceTable;
import com.eurest.supplier.model.InvoiceFreightRequest;
import com.eurest.supplier.model.NonComplianceSupplier;
import com.eurest.supplier.model.PaymentCalendar;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.FileConceptUploadBean;
import com.eurest.supplier.util.FileUploadBean;
import com.eurest.supplier.util.NullValidator;
import com.eurest.supplier.util.PayloadProducer;
import com.eurest.supplier.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service("fiscalDocumentConceptService")
public class FiscalDocumentConceptService {
	
	@Autowired
	FiscalDocumentConceptDao fiscalDocumentConceptDao;
	
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
	
	@Autowired
	UsersService usersService;
	
	static String TIMESTAMP_DATE_PATTERN = "yyyy-MM-dd";
	static String TIMESTAMP_DATE_PATTERN_NEW = "yyyy-MM-dd HH:mm:ss";
	static String DATE_PATTERN = "dd/MM/yyyy";
	
	Logger log4j = Logger.getLogger(FiscalDocumentConceptService.class);
	
	public FiscalDocumentsConcept getById(int id) {
		return fiscalDocumentConceptDao.getById(id);
	}
	
//	public List<FiscalDocumentsConcept> getFiscalDocumentsConcept(String addressNumber, String status, String uuid, String documentType, int start, int limit) {
//		return fiscalDocumentConceptDao.getFiscalDocumentsConcept(addressNumber, status, uuid, documentType,  start, limit);		
//	}
	
	public FiscalDocumentsConcept getByUuid(String uuid) {
		return fiscalDocumentConceptDao.getFiscalDocumentsConceptByUuid(uuid);
	}
	public List<FiscalDocumentsConcept> getListByUuid(String uuid) {
		return fiscalDocumentConceptDao.getFiscalDocumentsConceptListByUuid(uuid);
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
	
	
	
	public void saveDocument(FiscalDocumentsConcept doc) {
		fiscalDocumentConceptDao.saveDocumentConcept(doc);
	}
	
	public void updateDocument(FiscalDocumentsConcept doc) {
		fiscalDocumentConceptDao.updateDocumentConcept(doc);
	}

	
		
		public String saveInvoiceFlete(InvoiceFreightRequest invReq, InvoiceDTO inv, String addressBook, String tipoComprobante,
				boolean sendVoucher, String xmlContent,int idBatch) {

			
			List<Concepto> invConceptos = inv.getConcepto();

			Supplier supplier = supplierService.searchByAddressNumber(addressBook);
			
			int diasCred = 30; // JAVILA: El default es de 30 días de crédito
			String diasCredStr = supplier.getDiasCredito();
			if (diasCredStr != null) {
				if (!"".equals(diasCredStr)) {
					try {
						diasCred = Integer.valueOf(diasCredStr);
					} catch (Exception e) {
						log4j.error("Exception" , e);
					}
				}
			}

			Date estimatedPaymentDate = null;
			Date currentDate = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, diasCred);
			estimatedPaymentDate = c.getTime();
try {
	byte[] contentArrayXml = Base64.decodeBase64(invReq.getXml());
	byte[] contentArrayPdf = Base64.decodeBase64(invReq.getPdf());

			

			for(Concepto concepto: invConceptos)
			{
				FiscalDocumentsConcept o = new FiscalDocumentsConcept();
				
			o.setBatchID(String.valueOf(idBatch));
			o.setAccountingAccount("");
	    	o.setGlOffset("");
			//o.setTaxCode(supplier.getTaxRate()); 
	    	o.setTaxCode("1"); 
			o.setCurrencyMode("D");
			o.setCurrencyCode(supplier.getCurrencyCode());
//			o.setPaymentTerms(supplier.getDiasCredito());
			o.setImpuestos(inv.getTotalImpuestos());
			o.setCtaPresupuestal(invReq.getCtaPresupuestal()); //REvisar que va a ser el centro de costos para fletes
			o.setFolio(inv.getFolio());
			o.setSerie(inv.getSerie());
			o.setUuidFactura(inv.getUuid());
			o.setType("FACTURA");
			o.setAddressNumber(addressBook);
			o.setRfcEmisor(inv.getRfcEmisor());
			o.setStatus("PENDIENTE");
			o.setSubtotal(inv.getSubTotal());
			o.setAmount(inv.getTotal());
			o.setMoneda(inv.getMoneda());
			o.setInvoiceDate(inv.getFecha());
			o.setDescuento(inv.getDescuento());
			o.setImpuestos(inv.getImpuestos());
			o.setRfcReceptor(inv.getRfcReceptor());
			o.setEstimatedPaymentDate(estimatedPaymentDate);
			o.setInvoiceUploadDate(new Date());
			o.setOrderType("FLETE");
			o.setSerieBitacora(invReq.getSerieBitacora());
			o.setNumBitacora(invReq.getNumBitacora());
			o.setConceptName(concepto.getDescripcion());
			o.setCantidad(Double.parseDouble(concepto.getCantidad()));
			o.setPrecioUnitario(Double.parseDouble(concepto.getValorUnitario()));
			
			Double ivaTotal = Double.parseDouble("0");
			for(Traslado traslado : concepto.getImpuestos().getTraslados().getTraslado()){
				ivaTotal = Double.parseDouble(traslado.getImporte());
			}
			o.setIva(ivaTotal);
			

			Double retTotal = Double.parseDouble("0");
			if(!(concepto.getImpuestos()==null||concepto.getImpuestos().getRetenciones()==null||concepto.getImpuestos().getRetenciones().getRetencion()==null))
			{	for(Retencion ret : concepto.getImpuestos().getRetenciones().getRetencion()){
				retTotal =retTotal+  Double.parseDouble(ret.getImporte());
			}}
			o.setRetIva(retTotal);
			
			
	
			
			
			
			fiscalDocumentConceptDao.saveDocumentConcept(o);
			}
			
			
			UserDocument doc = new UserDocument(); 
			
	    	doc.setAddressBook(addressBook);
	    	doc.setDocumentNumber(0);
	    	doc.setDocumentType("I");
	    	doc.setContent(contentArrayXml);
	    	doc.setName("FAC_Flete_" + inv.getUuid()   + ".xml");
	    	doc.setSize(contentArrayXml.length);
	    	doc.setStatus(true);
	    	doc.setAccept(true);
	    	doc.setFiscalType(tipoComprobante);
	    	doc.setType("text/xml");
	    	doc.setFolio(inv.getFolio());
	    	doc.setSerie(inv.getSerie());
	    	doc.setUuid(inv.getUuid());
	    	doc.setUploadDate(new Date());
	    	doc.setFiscalRef(0);
	    	documentsDao.saveDocuments(doc);
	    	
			doc = new UserDocument(); 
	    	doc.setAddressBook(addressBook);
	    	doc.setDocumentNumber(0);
	    	doc.setDocumentType("I");
	    	doc.setContent(contentArrayPdf);
	    	doc.setName("FAC_Flete_" + inv.getUuid()   + ".pdf");
	    	doc.setSize(contentArrayPdf.length);
	    	doc.setStatus(true);
	    	doc.setAccept(true);
	    	doc.setFiscalType(tipoComprobante);
	    	doc.setType("application/pdf");
	    	doc.setFolio(inv.getFolio());
	    	doc.setSerie(inv.getSerie());
	    	doc.setUuid(inv.getUuid());
	    	doc.setUploadDate(new Date());
	    	doc.setFiscalRef(0);
	    	documentsDao.saveDocuments(doc);
} catch (Exception e) {
	log4j.error("Exception" , e);
	e.printStackTrace();
	return e.getMessage();
}	
			return "";
		}

		
		public List<FiscalDocumentsConcept> searchByIdBatch(String idBatch){
			
			return fiscalDocumentConceptDao.searchByIdBatch(idBatch);
		}
}
