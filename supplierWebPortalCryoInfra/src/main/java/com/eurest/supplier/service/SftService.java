package com.eurest.supplier.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.DocumentsDao;
import com.eurest.supplier.dao.FiscalDocumentConceptDao;
import com.eurest.supplier.dao.FiscalDocumentDao;
import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.dto.InvoiceFreightIn;
import com.eurest.supplier.edi.BatchJournalDTO;
import com.eurest.supplier.model.BatchJournal;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.InvoiceFreightRequest;
import com.eurest.supplier.model.InvoiceFreightResponse;
import com.eurest.supplier.model.ReceipInvoiceFreight;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.MiBotTelegram;
import com.eurest.supplier.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Service("SftService")
public class SftService {

	@Autowired
	DocumentsService documentsService;
	@Autowired
	SupplierService supplierService;
	@Autowired
	private FiscalDocumentService fiscalDocumentService;
	@Autowired
	private DocumentsDao documentsDao;
	@Autowired
	EDIService eDIService;
	@Autowired
	FiscalDocumentDao fiscalDocumentDao;
	@Autowired
	FiscalDocumentConceptDao  fiscalDocumentConceptDao;
	@Autowired
	private FiscalDocumentConceptService fiscalDocumentConceptService;
	@Autowired
	private BatchJournalService batchJournalService;
	@Autowired
	private UdcService udcService;
	@Autowired
	StringUtils stringUtils;
	@Autowired
	private JavaMailSender mailSenderObj;
	
	private Logger log4j = Logger.getLogger(SftService.class);
	
	
	@SuppressWarnings("unused")
	public List<InvoiceFreightResponse> validateInvoiceFreight(ReceipInvoiceFreight receip) {
		log4j.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<Inicio de validateInvoiceFreight"+new Date().toLocaleString());
		
//		Limpiando semana pago ya que se ocupa para el nombre del archivo a envia a jde
		
		receip.setSemanaPago(receip.getSemanaPago().replaceAll("[^a-zA-Z0-9 \\-]", "-").trim());
		
		
		List<InvoiceFreightRequest> invoiceFreightList=receip.getBitacoras();
		InvoiceFreightResponse invoiceFreightResponse = new InvoiceFreightResponse();
		List<InvoiceFreightResponse> invoiceFreightResponseList = new ArrayList<InvoiceFreightResponse>();
		List<InvoiceFreightRequest> invoiceFreightListTemp= new ArrayList<InvoiceFreightRequest>();
		//String validateInvoice = null;
		try {
			
			log4j.info("*********** validateInvoiceFreight:invoiceFreightList:");
			List<InvoiceFreightRequest> invoiceFreightValidList = new ArrayList<InvoiceFreightRequest>();
			List<InvoiceFreightRequest> invoiceFreightValidListTemp = new ArrayList<InvoiceFreightRequest>();
			
			/* Agrupacion */
			log4j.info("inicio de agrupacion: "+new Date().toLocaleString());

			Map<String, Map<String, List<InvoiceFreightRequest>>> multipleFieldsMap = invoiceFreightList
					.stream().collect(Collectors.groupingBy(InvoiceFreightRequest::getRfc,
							Collectors.groupingBy(InvoiceFreightRequest::getCtaPresupuestal)));
			log4j.info("fin de agrupacion"+new Date().toLocaleString());

			Supplier supplier;
//			log4j.info(multipleFieldsMap);
			
			for (Map.Entry<String, Map<String, List<InvoiceFreightRequest>>> entryRFC : multipleFieldsMap.entrySet()) {///rfc
				
				String rfc=entryRFC.getKey();
				log4j.info("inicio busqueda de rfc : "+new Date().toLocaleString());
				List<Supplier> supplierList= supplierService.searchByRfc(rfc, "rfc");
				
				if(supplierList==null || supplierList.isEmpty()) {
					invoiceFreightResponse.setRespuesta("El RFC:" +rfc+", aun no esta dado de alta");
					invoiceFreightResponseList.add(invoiceFreightResponse);
		    		continue;
				}else {
					supplier= supplierList.get(0);
				}
				log4j.info("fin de busqeda rfc: "+new Date().toLocaleString());
		    
			    
			    for(Map.Entry<String, List<InvoiceFreightRequest>> entryCuenta:entryRFC.getValue().entrySet()) {//cuenta presupuestal
			    	
			    	FiscalDocuments bitacoraFiscal=new FiscalDocuments();
			    	
			    	
			    	
//			    	sumatoria
			    	log4j.info("inicio de sumatoria: "+new Date().toLocaleString());
			    	double sumTotalFact = entryCuenta.getValue().stream().mapToDouble(o -> documentsService.getInvoiceXmlFromString(new String(Base64.decodeBase64(o.getXml()), StandardCharsets.UTF_8)).getTotal()).sum();
			    	double sumSubTotalFact = entryCuenta.getValue().stream().mapToDouble(o -> documentsService.getInvoiceXmlFromString(new String(Base64.decodeBase64(o.getXml()), StandardCharsets.UTF_8)).getSubTotal()).sum();
			    	log4j.info("fin de sumatoria: "+new Date().toLocaleString());
			    	String cuentraPresupuestal=entryCuenta.getKey();
			    	
			    	List<InvoiceFreightRequest> listinv = entryCuenta.getValue();
			    	bitacoraFiscal.setSemanaPago(receip.getSemanaPago());
			    	bitacoraFiscal.setAmount(sumTotalFact);
			    	bitacoraFiscal.setSubtotal(sumSubTotalFact);
			    	bitacoraFiscal.setRfcEmisor(rfc);
			    	bitacoraFiscal.setAddressNumber(supplier.getAddresNumber());
			    	bitacoraFiscal.setType("Bitacora");
			    	bitacoraFiscal.setStatus(AppConstants.STATUS_INPROCESS);
			    	bitacoraFiscal.setOrderType("FLETE");
			    	bitacoraFiscal.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
			    	bitacoraFiscal.setReplicationDate(new Date());
			    	bitacoraFiscal.setToSend(1);
			    	bitacoraFiscal.setAccountingAccount(cuentraPresupuestal);
			    	bitacoraFiscal.setSerie(listinv.size() > 0 ? listinv.get(0).getSerieBitacora() : "");
			    	bitacoraFiscal.setDocumentNumber(0);
			    	
			    	  List<UDC> udcs = udcService.searchListBySystemAndKey("APPROVALFREIGHT", AppConstants.INV_FIRST_APPROVER);
			    	  List<UDC> udcSeconds = udcService.searchListBySystemAndKey("APPROVALFREIGHT", AppConstants.INV_SECOND_APPROVER);
			    	  
			    	  
			    	  String mailfirts = udcs.stream()
			    			    .filter(UDC::isBooleanValue)    
			    			    .map(UDC::getStrValue2)        
			    			    .filter(Objects::nonNull)       
			    			    .collect(Collectors.joining(","));
			    	  
			    	  
			    	  String userfirts = udcs.stream()
			    			    .filter(UDC::isBooleanValue)    
			    			    .map(UDC::getStrValue1)        
			    			    .filter(Objects::nonNull)       
			    			    .collect(Collectors.joining(",")); 
			    	  
			    	  String mailsecond = udcSeconds.stream()
			    			    .filter(UDC::isBooleanValue)    
			    			    .map(UDC::getStrValue2)        
			    			    .filter(Objects::nonNull)       
			    			    .collect(Collectors.joining(","));
			    	  
			    	  
			    	  String usersecond = udcSeconds.stream()
			    			    .filter(UDC::isBooleanValue)    
			    			    .map(UDC::getStrValue1)        
			    			    .filter(Objects::nonNull)       
			    			    .collect(Collectors.joining(",")); 
			    	  
			    	  
			    	    	  bitacoraFiscal.setNextApprover(usersecond);
					    	  bitacoraFiscal.setApprovalStep(AppConstants.FIRST_STEP);
		                      bitacoraFiscal.setApprovalStatus(AppConstants.STATUS_INPROCESS);
		                      bitacoraFiscal.setCurrentApprover(userfirts);
		                      
			    	  String email = mailfirts;
			    	  
			    	
				    		  
			    	
			   
			     invoiceFreightValidListTemp = new ArrayList<InvoiceFreightRequest>();
			     invoiceFreightListTemp=new ArrayList<InvoiceFreightRequest>();
			     invoiceFreightValidList = new ArrayList<InvoiceFreightRequest>();
			     log4j.info("for guadado de gactura inicio de agrupacion: "+new Date().toLocaleString());
			    	for (InvoiceFreightRequest invReq : entryCuenta.getValue()) {
						invoiceFreightResponse = new InvoiceFreightResponse();

						invoiceFreightResponse.setSerieBitacora(invReq.getSerieBitacora());
						invoiceFreightResponse.setNumBitacora(invReq.getNumBitacora());

						invoiceFreightResponse.setValido(0);
						String validateMsg = validateInputData(invReq);
						if ("".equals(validateMsg)) {
							InvoiceDTO inv = null;
							log4j.info("inicio de conversion de file a string factura: "+new Date().toLocaleString());
							String contentXML = new String(Base64.decodeBase64(invReq.getXml()), StandardCharsets.UTF_8);
							
							inv = documentsService.getInvoiceXmlFromString(contentXML);
							log4j.info("fin de conversion de file a string factura: "+new Date().toLocaleString());
							if (inv != null) {
								invoiceFreightResponse.setFolioFactura(inv.getFolio());
								invoiceFreightResponse.setSerieFactura(inv.getSerie());
								String addressNumber = "";
								log4j.info("inicio busqueda por rfc: "+new Date().toLocaleString());
								List<Supplier> supList = supplierService.searchByRfc(inv.getRfcEmisor(), "rfc");
								log4j.info("fin de busqueda rfc: "+new Date().toLocaleString());
								if (supList != null && supList.size() > 0)
									addressNumber = supList.get(0).getAddresNumber();
								
								String validateInvoice = documentsService.validateInvoiceFromSFT(inv, addressNumber, 0, "",
										inv.getTipoComprobante(),  false, contentXML, null, false);
								log4j.info("fin de validateInvoiceFromSFT "+new Date().toLocaleString());
								if ("".equals(validateInvoice)) {
									log4j.info("inicio de guardado de factura: "+new Date().toLocaleString());
									fiscalDocumentService.saveDocument(bitacoraFiscal);				

									invoiceFreightValidList.add(invReq);
									invoiceFreightListTemp.add(invReq);

									String res = fiscalDocumentConceptService.saveInvoiceFlete(invReq, inv, addressNumber,
											inv.getTipoComprobante(), true, contentXML,bitacoraFiscal.getId());
									log4j.info("fin de guardado: "+new Date().toLocaleString());
									
									  try {
										  EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
										//  emailAsyncSup.setProperties(AppConstants.EMAIL_INV_REQUEST_NO_OC, this.stringUtils.prepareEmailContent(AppConstants.EMAIL_INV_APPROVAL_MSG_1_NO_OC + o.getUuidFactura() + AppConstants.EMAIL_INV_APPROVAL_MSG_2_NO_OC + s.getAddresNumber() +  "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK), nextApproverEmail);
										  emailAsyncSup.setProperties(AppConstants.EMAIL_FREIGHT_REQUEST,
										  this.stringUtils.prepareEmailContent(AppConstants.EMAIL_INV_APPROVAL_MSG_1_FREIGHT + String.valueOf(bitacoraFiscal.getId()) +  "<br /><br />" + AppConstants.EMAIL_PORTAL_LINK), email);
										  emailAsyncSup.setMailSender(mailSenderObj);
										  Thread emailThreadSup = new Thread(emailAsyncSup);
										  emailThreadSup.start();
										 
										
										  
									  } catch (Exception e) {	
										  log4j.error("Exception" , e);
									  }
									
									if("".equals(res)) {
										invoiceFreightResponse.setRespuesta("OK");
										invoiceFreightResponse.setValido(1);																				
										
									}else {
										invoiceFreightResponse.setRespuesta(res);
										invoiceFreightResponse.setValido(0);
									}
									
								}else {
									invoiceFreightResponse.setRespuesta(validateInvoice);
									invoiceFreightResponse.setValido(0);
								}

							} else {
								invoiceFreightResponse.setRespuesta(
										"La factura tiene errores de validación. Verifique que el archivo XML sea válido respecto a la versión SAT 3.3");

							}

							
						} else {

							invoiceFreightResponse.setRespuesta(validateMsg);
							log4j.info("*********** invoiceFreightResponse:" + invoiceFreightResponse.toString());

						}

						invoiceFreightResponseList.add(invoiceFreightResponse);
					}
			    	log4j.info("for de fin de lectura de facturas: "+new Date().toLocaleString());
			    	
			    	if(invoiceFreightListTemp.size()<entryCuenta.getValue().size()) {
			    		log4j.info("inicio de resumatoriade facturas: "+new Date().toLocaleString());
				
				    	 sumTotalFact = invoiceFreightListTemp.stream().mapToDouble(o -> documentsService.getInvoiceXmlFromString(new String(Base64.decodeBase64(o.getXml()), StandardCharsets.UTF_8)).getTotal()).sum();
				    	 sumSubTotalFact = invoiceFreightListTemp.stream().mapToDouble(o -> documentsService.getInvoiceXmlFromString(new String(Base64.decodeBase64(o.getXml()), StandardCharsets.UTF_8)).getSubTotal()).sum();
				    	
				    		bitacoraFiscal.setAmount(sumTotalFact);
					    	bitacoraFiscal.setSubtotal(sumSubTotalFact);
					    	log4j.info("fin de resumatoriade facturas: "+new Date().toLocaleString());
				    	if(bitacoraFiscal.getId() != 0) {
				    		fiscalDocumentService.updateDocument(bitacoraFiscal);
				    	}
				    	
			    	}
                    UDC udcAlert = udcService.searchBySystemAndKey("TELEGRAMALERTS", "FLETES");

                    if(udcAlert!=null&&udcAlert.isBooleanValue()) {
			    	 MiBotTelegram miBot = new MiBotTelegram();
				        miBot.enviarMensaje("Nobatch: "+String.valueOf(bitacoraFiscal.getId())+
				        		"\nSemana Pago: *"+receip.getSemanaPago()+"*"+
				        		"\nCuenta Presupuestal: *"+cuentraPresupuestal+"*"+
				        		"\n subtotal: *"+sumSubTotalFact+"*"+
				        		"\nNo.facturas: *"+invoiceFreightListTemp.size()+"*");
				     
                    }
			    	
			    } 
			}
			
			

			 //invoiceFreightResponseList.add(invoiceFreightResponse);
		} catch (Exception ex) {
			log4j.error("Exception" , ex);
			ex.printStackTrace();
		}
		log4j.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<fin de de validateInvoiceFreight"+new Date().toLocaleString());
		return invoiceFreightResponseList;

	}

	@SuppressWarnings("unused")
	private String getGroupingByKey(InvoiceFreightRequest req) {
		return req.getRfc() + "-" + req.getCtaPresupuestal();
	}

	public String validateInputData(InvoiceFreightRequest invoiceFreight) {

		StringBuilder strBuilder = new StringBuilder();

		if (invoiceFreight.getXml() == null || invoiceFreight.getXml().length() == 0)
			strBuilder.append("El campo XML está vacío | ");
		if (invoiceFreight.getPdf() == null || invoiceFreight.getPdf().length() == 0)
			strBuilder.append("El campo PDF está vacío | ");
		if (invoiceFreight.getCtaPresupuestal() == null || invoiceFreight.getCtaPresupuestal().length() == 0)
			strBuilder.append("El campo CtaPresupuestal está vacío | ");
		if (invoiceFreight.getRfc() == null || invoiceFreight.getRfc().length() == 0)
			strBuilder.append("El campo RFC está vacío | ");
		if (invoiceFreight.getSerieBitacora() == null || invoiceFreight.getSerieBitacora().length() == 0)
			strBuilder.append("El campo SerieBitacora está vacío | ");
//		if (invoiceFreight.getSerieFactura() == null || invoiceFreight.getSerieFactura().length() == 0)
//			strBuilder.append("El campo SerieFactura está vacío | ");
//		if (invoiceFreight.getFolioFactura() == null || invoiceFreight.getFolioFactura().length() == 0)
//			strBuilder.append("El campo FolioFactura está vacío | ");
		if (invoiceFreight.getNumBitacora() == null || invoiceFreight.getNumBitacora().length() == 0)
			strBuilder.append("El campo NumBitacora está vacío | ");

		return strBuilder.toString();
	}

	public List<BatchJournalDTO> createBatchJournalList(int start, int limit) {

		List<BatchJournalDTO> listaSend=new ArrayList<>();
		
		List<FiscalDocuments> listFiscal=fiscalDocumentService.getListToSendFletes(start,limit);
		
for (FiscalDocuments fiscalDocuments : listFiscal) {
	
	Supplier supplier = supplierService.searchByAddressNumber(fiscalDocuments.getAddressNumber());
	List<FiscalDocumentsConcept> listaConcepts=fiscalDocumentConceptService.searchByIdBatch(String.valueOf(fiscalDocuments.getId()));
	int index=0;
	FiscalDocumentsConcept fiscalDocumentsConceptforCabecera=null;
	double sumatoria=0;
	InvoiceDTO invCabecera=null;
	
	for (FiscalDocumentsConcept fiscalDocumentsConcept : listaConcepts) {
	InvoiceDTO inv=null;
if(index==0) {
	fiscalDocumentsConceptforCabecera=fiscalDocumentsConcept;
	List<UserDocument> docs = documentsDao.searchCriteriaByUuidOnly(fiscalDocumentsConcept.getUuidFactura());

	if(docs != null) {
	if(docs != null) {
		for(UserDocument u : docs) {
			if(/*AppConstants.INVOICE_FIELD.equals(u.getFiscalType()) &&*/ "text/xml".equals(u.getType())) {
				String xmlStr = new String(u.getContent(), StandardCharsets.UTF_8);
				 inv = documentsService.getInvoiceXmlFromString(xmlStr);
				 if(index==0) {
					 invCabecera=inv;
					 index++;
				 }
		        break;
			}
		}
	}
}	
	
}

	sumatoria=sumatoria+fiscalDocumentsConcept.getSubtotal();

//	if(docs != null) {
//		if(docs != null) {
//			for(UserDocument u : docs) {
//				if(/*AppConstants.INVOICE_FIELD.equals(u.getFiscalType()) &&*/ "text/xml".equals(u.getType())) {
//					String xmlStr = new String(u.getContent(), StandardCharsets.UTF_8);
//					 inv = documentsService.getInvoiceXmlFromString(xmlStr);
//					 if(index==0) {
//						 invCabecera=inv;
//						 sumatoria=sumatoria+inv.getSubTotal();
//						 index++;
//					 }
//			        break;
//				}
//			}
//		}
//	}	
	}
	
	fiscalDocumentsConceptforCabecera.setSubtotal(sumatoria); 
	invCabecera.setSubTotal(sumatoria);
	listaSend.add(eDIService.createBatchJournalDTO(fiscalDocumentsConceptforCabecera, invCabecera, supplier, AppConstants.NN_MODULE_BATCHJOURNAL,fiscalDocuments));

	}


	
		
		
	return listaSend;
		
	}

	public List<BatchJournalDTO> updateFletesStatus(
			List<BatchJournalDTO>  batchVoucherTransactionsDTO) {
		try {
			for (BatchJournalDTO batchVoucherTransactionsDTO2 : batchVoucherTransactionsDTO) {
				
				FiscalDocuments fis=fiscalDocumentDao.getById(Integer.parseInt( batchVoucherTransactionsDTO2.getVoucherEntries().get(0).getVLEDBT()));
				fis.setReplicationStatus( fis.getReplicationStatus()==null?AppConstants.STATUS_PENDING_REPLICATION:AppConstants.STATUS_SUCCESS_REPLICATION);
				fis.setReplicationDate(new Date());
				fis.setToSend(0);				
				fiscalDocumentDao.updateDocument(fis);

				ObjectMapper jsonmapper=new ObjectMapper();
				jsonmapper.setSerializationInclusion(Include.NON_NULL);			
				String jsonString=jsonmapper.writeValueAsString(batchVoucherTransactionsDTO2);
				
				BatchJournal batchBitacora;
				List<BatchJournal> batchJournalList = batchJournalService.searchByIdBatch(String.valueOf(fis.getId()));
				
				if(batchJournalList != null && !batchJournalList.isEmpty()) {
					batchBitacora = batchJournalList.get(0);
					if (batchVoucherTransactionsDTO2.getVoucherEntries().get(0).getVLPST().equals(AppConstants.STATUS_JDE_APROV_FLETES)) {
						batchBitacora.setApprovalDate(new Date());
						batchJournalService.update(batchBitacora);
					}
				}else {
					batchBitacora=new Gson().fromJson(jsonString, BatchJournal.class);
					batchBitacora.setBatchID(String.valueOf(fis.getId()));
					batchBitacora.setCreationDate(new Date());
					batchBitacora.setContent(jsonString);
					batchJournalService.save(batchBitacora);
				}
				/*
				try {					
					if("V".equals(batchVoucherTransactionsDTO2.getVoucherEntries().get(0).getVLPST())) {
						
						//JSAAVEDRA: Actualiza los documentos de tipo Factura a estatus Pendiente para replicarlos al middleware.
						List<FiscalDocumentsConcept> conceptList = fiscalDocumentConceptService.searchByIdBatch(String.valueOf(fis.getId()));
						if(conceptList != null && !conceptList.isEmpty()) {
							
							for(FiscalDocumentsConcept concept : conceptList) {						
								if(concept.getUuidFactura() != null && !concept.getUuidFactura().trim().isEmpty()) {
									
									List<UserDocument> documents = documentsDao.searchCriteriaByUuidOnly(concept.getUuidFactura());
									if(documents != null && !documents.isEmpty()) {									
										for(UserDocument doc : documents) {
											
											if(!AppConstants.STATUS_SUCCESS_REPLICATION.equals(doc.getReplicationStatus())) {
												doc.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
												this.documentsService.update(doc, null, null);
											}
										}
									}
									
								}						
							}
						}
						
						//JSAAVEDRA: Actualiza los documentos de tipo Batch a estatus Pendiente para replicarlos al middleware.
						UserDocument batchDoc = userDocumentDao.getPdfByBatch(String.valueOf(fis.getId()));
						batchDoc.setReplicationStatus(AppConstants.STATUS_PENDING_REPLICATION);
						userDocumentDao.update(batchDoc);				
						
						//JSAAVEDRA: Guarda carátula de Batch con estatus Pendiente para replicarlo al middleware.
						this.saveBatchCoverDocument(String.valueOf(fis.getId()));	
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				*/
			/*	batchBitacora=new Gson().fromJson(jsonString, BatchJournal.class);
				
				//batchBitacora = batchJournalService.searchByIdBatch(String.valueOf(fis.getId());
				batchBitacora.setBatchID(String.valueOf(fis.getId()));
				batchBitacora.setCreationDate(new Date());
				batchBitacora.setContent(jsonString);
				if (batchVoucherTransactionsDTO2.getVoucherEntries().get(0).getVLPST().equals("A")) {
					batchBitacora.setApprovalDate(new Date());
					batchJournalService.update(batchBitacora);
				}else {
					batchJournalService.save(batchBitacora);
				}*/
				
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
		return batchVoucherTransactionsDTO;
	}
	
	
	
	@SuppressWarnings("unused")
	public InvoiceFreightIn validateInvoiceFreightIn(InvoiceFreightIn factura) {
		log4j.info("||||||||||||||||||||||||||||||||||Inicio de validateInvoiceFreightIn "+new Date().toLocaleString());
		
		String contentXML = new String(Base64.decodeBase64(factura.getXml()), StandardCharsets.UTF_8);
		
		InvoiceDTO inv = documentsService.getInvoiceXmlFromString(contentXML);
		if (inv != null) {
			String addressNumber = "";
			log4j.info("inicio busqueda por rfc: "+new Date().toLocaleString());
			List<Supplier> supList = supplierService.searchByRfc(inv.getRfcEmisor(), "rfc");
			log4j.info("fin de busqueda rfc: "+new Date().toLocaleString());
			if (supList != null && supList.size() > 0) {
				addressNumber = supList.get(0).getAddresNumber();
			
			String validateInvoice = documentsService.validateInvoiceFromSFT(inv, addressNumber, 0, "",
					inv.getTipoComprobante(),  false, contentXML, null, false);
			if ("".equals(validateInvoice)) {
				factura.setValido(1);
				factura.setMensajeError("");
				
			}else {
				factura.setValido(0);
				factura.setMensajeError(validateInvoice);
			}
			
			
			
			}else {
				factura.setValido(0);
			factura.setMensajeError("RFC no permitido en el portal");
			}
		
		}else {
			factura.setValido(0);
			factura.setMensajeError("Estructura XML invalida");
		}
		
		log4j.info("||||||||||||||||||||||||||||||||||fin de de validateInvoiceFreightIn "+new Date().toLocaleString());
		return factura;
	
	}
	
	public byte[] getReportFletes() throws IOException {

		ArrayList<String> fletes = fiscalDocumentDao.getBatchAndSemanaPago();

	    // Crear el Workbook (XSSFWorkbook para archivos .xlsx)
	    Workbook workbook = new XSSFWorkbook();

	    // Crear una hoja
	    Sheet sheet = workbook.createSheet("Fletes Pendientes");

	    // Crear estilos para las celdas
	    // Estilo para el encabezado (fondo azul, letras blancas, negrita)
	    CellStyle headerStyle = workbook.createCellStyle();
	    Font headerFont = workbook.createFont();

	    // Para versiones más antiguas de Apache POI usamos setBoldweight
	    headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    headerFont.setColor(IndexedColors.WHITE.getIndex());
	    headerStyle.setFont(headerFont);
	    headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());

	    // Usar CellStyle.SOLID_FOREGROUND para la compatibilidad con versiones antiguas
	    headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

	    // En lugar de HorizontalAlignment.CENTER, usar CellStyle.ALIGN_CENTER
	    headerStyle.setAlignment(CellStyle.ALIGN_CENTER);

	    // Estilo para las celdas de datos (bordes finos)
	    CellStyle dataStyle = workbook.createCellStyle();
	    dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
	    dataStyle.setBorderTop(CellStyle.BORDER_THIN);
	    dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
	    dataStyle.setBorderRight(CellStyle.BORDER_THIN);
	    
	    // Alinear a la izquierda usando CellStyle.ALIGN_LEFT
	    dataStyle.setAlignment(CellStyle.ALIGN_LEFT);

	    // Crear la primera fila con los nombres de las columnas
	    Row headerRow = sheet.createRow(0);
	    Cell headerCell1 = headerRow.createCell(0);
	    headerCell1.setCellValue("Número de batch");
	    headerCell1.setCellStyle(headerStyle);

	    Cell headerCell2 = headerRow.createCell(1);
	    headerCell2.setCellValue("Nombre del corte");
	    headerCell2.setCellStyle(headerStyle);

	    // Llenar los datos (arreglo de datos)
	    for (int i = 0; i < fletes.size(); i++) {
	        Row row = sheet.createRow(i + 1);
	        String[] data = fletes.get(i).split(",");

	        Cell cell1 = row.createCell(0);
	        cell1.setCellValue(data[0]);  // Columna "numero de batch"
	        cell1.setCellStyle(dataStyle);

	        Cell cell2 = row.createCell(1);
	        cell2.setCellValue(data[1]);  // Columna "nombre del corte"
	        cell2.setCellStyle(dataStyle);
	    }

	    // Autoajustar el tamaño de las columnas
	    sheet.autoSizeColumn(0);
	    sheet.autoSizeColumn(1);

	    // Escribir el archivo en un ByteArrayOutputStream
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    workbook.write(outputStream);

	    // Cerrar el workbook
//	    workbook.close();

	    // Retornar el arreglo de bytes
	    return outputStream.toByteArray();       
	}
	public byte[] getReportFletes(List<String> batches) throws IOException {

		ArrayList<String> fletes = fiscalDocumentDao.getBatchAndSemanaPago(batches);

		if(fletes==null || fletes.isEmpty()||fletes.size()==0) {
			return null;
		}
	    // Crear el Workbook (XSSFWorkbook para archivos .xlsx)
	    Workbook workbook = new XSSFWorkbook();

	    // Crear una hoja
	    Sheet sheet = workbook.createSheet("Fletes Pendientes");

	    // Crear estilos para las celdas
	    // Estilo para el encabezado (fondo azul, letras blancas, negrita)
	    CellStyle headerStyle = workbook.createCellStyle();
	    Font headerFont = workbook.createFont();

	    // Para versiones más antiguas de Apache POI usamos setBoldweight
	    headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    headerFont.setColor(IndexedColors.WHITE.getIndex());
	    headerStyle.setFont(headerFont);
	    headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());

	    // Usar CellStyle.SOLID_FOREGROUND para la compatibilidad con versiones antiguas
	    headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

	    // En lugar de HorizontalAlignment.CENTER, usar CellStyle.ALIGN_CENTER
	    headerStyle.setAlignment(CellStyle.ALIGN_CENTER);

	    // Estilo para las celdas de datos (bordes finos)
	    CellStyle dataStyle = workbook.createCellStyle();
	    dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
	    dataStyle.setBorderTop(CellStyle.BORDER_THIN);
	    dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
	    dataStyle.setBorderRight(CellStyle.BORDER_THIN);
	    
	    // Alinear a la izquierda usando CellStyle.ALIGN_LEFT
	    dataStyle.setAlignment(CellStyle.ALIGN_LEFT);

	    // Crear la primera fila con los nombres de las columnas
	    Row headerRow = sheet.createRow(0);
	    Cell headerCell1 = headerRow.createCell(0);
	    headerCell1.setCellValue("Número de batch");
	    headerCell1.setCellStyle(headerStyle);

	    Cell headerCell2 = headerRow.createCell(1);
	    headerCell2.setCellValue("Nombre del corte");
	    headerCell2.setCellStyle(headerStyle);

	    // Llenar los datos (arreglo de datos)
	    for (int i = 0; i < fletes.size(); i++) {
	        Row row = sheet.createRow(i + 1);
	        String[] data = fletes.get(i).split(",");

	        Cell cell1 = row.createCell(0);
	        cell1.setCellValue(data[0]);  // Columna "numero de batch"
	        cell1.setCellStyle(dataStyle);

	        Cell cell2 = row.createCell(1);
	        cell2.setCellValue(data[1]);  // Columna "nombre del corte"
	        cell2.setCellStyle(dataStyle);
	    }

	    // Autoajustar el tamaño de las columnas
	    sheet.autoSizeColumn(0);
	    sheet.autoSizeColumn(1);

	    // Escribir el archivo en un ByteArrayOutputStream
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    workbook.write(outputStream);

	    // Cerrar el workbook
//	    workbook.close();

	    // Retornar el arreglo de bytes
	    return outputStream.toByteArray();       
	}	    
	

}
