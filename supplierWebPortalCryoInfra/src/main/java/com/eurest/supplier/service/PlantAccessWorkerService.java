package com.eurest.supplier.service;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eurest.supplier.controller.PlantAccessController;
import com.eurest.supplier.dao.ApprovalBatchFreightDao;
import com.eurest.supplier.dao.DocumentsDao;
import com.eurest.supplier.dao.FiscalDocumentDao;
import com.eurest.supplier.dao.LogDataAprovalActionDao;
import com.eurest.supplier.dao.PlantAccessRequestDao;
import com.eurest.supplier.dao.PlantAccessWorkerDao;
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
import com.eurest.supplier.model.ApprovalBatchFreight;
import com.eurest.supplier.model.FileStore;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.ForeignInvoiceTable;
import com.eurest.supplier.model.InvoiceFreightRequest;
import com.eurest.supplier.model.LogDataAprovalAction;
import com.eurest.supplier.model.NonComplianceSupplier;
import com.eurest.supplier.model.PaymentCalendar;
import com.eurest.supplier.model.PlantAccessRequest;
import com.eurest.supplier.model.PlantAccessWorker;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.UserDocument;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.FileConceptUploadBean;
import com.eurest.supplier.util.NullValidator;
import com.eurest.supplier.util.PayloadProducer;
import com.eurest.supplier.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service("plantAccessWorkerService")
public class PlantAccessWorkerService {

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	FiscalDocumentDao fiscalDocumentDao;
	
	@Autowired
	PlantAccessRequestDao plantAccessRequestDao;
	
	
	@Autowired
	UDCDao udcDao;
	
	
	@Autowired
	PlantAccessWorkerDao plantAccessWorkerDao;
	
	@Autowired
	FileStoreService fileStoreService;
	
	@Autowired
	DataAuditService dataAuditService;
	
	@Autowired
	PlantAccessRequestService plantAccessRequestService;
	
	Logger log4j = LogManager.getLogger(PlantAccessController.class);
	
	static String TIMESTAMP_DATE_PATTERN = "yyyy-MM-dd";
	static String TIMESTAMP_DATE_PATTERN_NEW = "yyyy-MM-dd HH:mm:ss";
	static String DATE_PATTERN = "dd/MM/yyyy";
	
	public PlantAccessWorker getById(int id) {
		return plantAccessWorkerDao.getById(id);
	}
	
//	public List<FiscalDocuments> getFiscalDocuments(String addressNumber, String status, String uuid, String documentType, int start, int limit) {
//		return fiscalDocumentDao.getFiscalDocuments(addressNumber, status, uuid, documentType,  start, limit);		
//	}
	
	
	@SuppressWarnings({ "unused"})
	public int getTotalRecords(String addressNumber, String status, String uuid, String documentType, int start, int limit) {
		return fiscalDocumentDao.getTotalRecords(addressNumber, status, uuid, documentType,  start, limit);
	}
	
	public List<PlantAccessWorker> searchWorkersPlantAccessByIdRequest(String id){
		return plantAccessWorkerDao.searchWorkersPlantAccessByIdRequest(id);
	}

	public void save(PlantAccessWorker doc) {
		plantAccessWorkerDao.save(doc);
	}
	
	public void update(PlantAccessWorker doc) {
		plantAccessWorkerDao.update(doc);
	}
	
	public void deleteWorkerPlantAccess(PlantAccessWorker doc) {
		plantAccessWorkerDao.delete(doc);
	}
    
	public int updateWorkerRequest(String uuid,String idref) {
	return plantAccessWorkerDao.updateWorkerRequest(uuid, idref);
	}
	
	public void updatePlantAccessWorkerDocuments(PlantAccessWorker plantAccessWorker) {
		try {			
			List<String> fileNameList = new ArrayList<String>();
			List<FileStore> files = fileStoreService.getFilesPlantAccessWorker(
														plantAccessWorker.getRequestNumber(),
														plantAccessWorker.getId(),
														false);//false para no traer el content
			if(files != null && !files.isEmpty()) {
				for(FileStore file : files) {					
					
					if(!plantAccessWorker.isDocsActivity1()) {
						//Comparte archivo con la actividad 3
						if("WORKER_CM1".equals(file.getDocumentType()) && !plantAccessWorker.isDocsActivity3()) {
							fileStoreService.deleteFilesPlantAccess(file);
							continue;
						}
						if("WORKER_CD3TA".equals(file.getDocumentType())) {
							fileStoreService.deleteFilesPlantAccess(file);
							continue;
						}
					}
					
					if(!plantAccessWorker.isDocsActivity2()) {
						if("WORKER_CD3G".equals(file.getDocumentType())) {
							fileStoreService.deleteFilesPlantAccess(file);
							continue;
						}
					}
					
					if(!plantAccessWorker.isDocsActivity3()) {
						//Comparte archivo con la actividad 1
						if("WORKER_CM1".equals(file.getDocumentType()) && !plantAccessWorker.isDocsActivity1()) {
							fileStoreService.deleteFilesPlantAccess(file);
							continue;
						}
						if("WORKER_CD3TEC".equals(file.getDocumentType())) {
							fileStoreService.deleteFilesPlantAccess(file);
							continue;
						}
					}
					
					if(!plantAccessWorker.isDocsActivity4()) {
						if("WORKER_CD3TE1".equals(file.getDocumentType())) {
							fileStoreService.deleteFilesPlantAccess(file);
							continue;
						}
					}
					
					if(!plantAccessWorker.isDocsActivity5()) {
						if("WORKER_CD3TC".equals(file.getDocumentType())) {
							fileStoreService.deleteFilesPlantAccess(file);
							continue;
						}
					}
					
					if(!plantAccessWorker.isDocsActivity6()) {
						if("WORKER_HS".equals(file.getDocumentType())) {
							fileStoreService.deleteFilesPlantAccess(file);
							continue;
						}
					}
					
					if(!plantAccessWorker.isDocsActivity7()) {
						if("WORKER_AE".equals(file.getDocumentType())) {
							fileStoreService.deleteFilesPlantAccess(file);
							continue;
						}
					}
					
					//Agrega nombre de archivo de trabajador (No se agrega si se elimia en esta ejecución)
					if(!fileNameList.contains(file.getDocumentType())) {
						fileNameList.add(file.getDocumentType());
					}
				}
				
				//Valida documentación completa
				List<String> fileNameCompleteList = new ArrayList<String>();
				
				//Documentación por default
				//fileNameCompleteList.add("WORKER_CMA");
				fileNameCompleteList.add("WORKER_CI");
				
				//Documentación de Actividades
				if(plantAccessWorker.isDocsActivity1()) {
					fileNameCompleteList.add("WORKER_CM1");
					fileNameCompleteList.add("WORKER_CD3TA");
				}
				if(plantAccessWorker.isDocsActivity2()) {
					fileNameCompleteList.add("WORKER_CD3G");
				}				
				if(plantAccessWorker.isDocsActivity3()) {
					if(!fileNameCompleteList.contains("WORKER_CM1")) {
						fileNameCompleteList.add("WORKER_CM1");
					}
					fileNameCompleteList.add("WORKER_CD3TEC");
				}				
				if(plantAccessWorker.isDocsActivity4()) {
					fileNameCompleteList.add("WORKER_CD3TE1");
				}
				if(plantAccessWorker.isDocsActivity5()) {
					fileNameCompleteList.add("WORKER_CD3TC");
				}
				if(plantAccessWorker.isDocsActivity6()) {
					fileNameCompleteList.add("WORKER_HS");
				}
				//if(plantAccessWorker.isDocsActivity7()) {
				//	fileNameCompleteList.add("WORKER_AE");
				//}
				
				//Documentación Completa
				plantAccessWorker.setAllDocuments(fileNameList.containsAll(fileNameCompleteList));

				//Lista de archivos
				plantAccessWorker.setListDocuments(String.join(",", fileNameList));
			}
			
		} catch (Exception e) {
			log4j.error("Exception" , e);
		}
	}
	
	@SuppressWarnings("unused")
	public String validatePlantAccessWorker(PlantAccessWorker plantAccessWorker, boolean isNewWorker) {
		try {
			SimpleDateFormat sdfOld = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfNew = new SimpleDateFormat("dd-MM-yyyy");
			
			//Validación de Fecha de Inducción
			String inductionDate = "";
			try {
				Date newDate = sdfOld.parse(plantAccessWorker.getDateInduction().replace("T", " "));
				inductionDate = sdfNew.format(newDate);
			} catch (Exception e) {
				return "La fecha de inducción no es válida.";
			}
			
			//Validación de Número de Credencial
			if(org.apache.commons.lang.StringUtils.isBlank(plantAccessWorker.getCardNumber())) {
				return "El número de credencial no es válido.";
			}
			
			//Validación de la FechaFolio-NúmeroCredencial
			List<PlantAccessWorker> listWorker = this.searchWorkersPlantAccessByIdRequest(plantAccessWorker.getRequestNumber());
			if(listWorker != null && !listWorker.isEmpty()) {
				for(PlantAccessWorker registeredWorker : listWorker) {
					if(plantAccessWorker.getDatefolioIDcard().equalsIgnoreCase(registeredWorker.getDatefolioIDcard())) {
						if(isNewWorker || (plantAccessWorker.getId() > 0 && plantAccessWorker.getId() != registeredWorker.getId())) {
							return "El folio de la credencial del trabajador ya se encuentra en esta solicitud.";
						}
					}
				}
			}
			return "";
		} catch (Exception e) {
			log4j.error("Exception" , e);
			return "Ocurrió un error al validar la información del trabajador.";
		}
	}

	@Transactional
	public List<PlantAccessWorker> copyWorkersTransactional(String sourceRequestId, String targetRequestId, boolean replaceExisting) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 		HttpServletRequest requestHTTP = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String usr = auth.getName();
		// Si se solicita reemplazar, eliminar trabajadores existentes y sus archivos
		if(replaceExisting) {
			log4j.info("[copyWorkersFromRequest] replaceExisting=true -> eliminando trabajadores existentes en solicitud destino: " + targetRequestId);
			List<PlantAccessWorker> existing = searchWorkersPlantAccessByIdRequest(targetRequestId);
			if(existing != null) {
				log4j.info("[copyWorkersFromRequest] Trabajadores existentes a eliminar: " + existing.size());
				for(PlantAccessWorker w : existing) {
					log4j.info("[copyWorkersFromRequest] Eliminando trabajador id=" + w.getId()
							+ " | nombre=" + w.getEmployeeName() + " " + w.getEmployeeLastName()
							+ " | CURP=" + w.getEmployeeCurp());
					List<FileStore> files = fileStoreService.getFilesPlantAccessWorker(targetRequestId, w.getId(), false);
					if(files != null) {
						log4j.info("[copyWorkersFromRequest] Eliminando " + files.size() + " archivo(s) del trabajador id=" + w.getId());
						for(FileStore f : files) {
							log4j.info("[copyWorkersFromRequest]   - Eliminando archivo id=" + f.getId() + " | tipo=" + f.getDocumentType() + " | nombre=" + f.getOriginName());
							fileStoreService.deleteFilesPlantAccess(f);
						}
					} else {
						log4j.info("[copyWorkersFromRequest] Trabajador id=" + w.getId() + " no tiene archivos asociados.");
					}
					deleteWorkerPlantAccess(w);
					log4j.info("[copyWorkersFromRequest] Trabajador id=" + w.getId() + " eliminado OK.");
				}
			} else {
				log4j.info("[copyWorkersFromRequest] No hay trabajadores existentes en solicitud destino: " + targetRequestId);
			}
		}

		// Leer trabajadores de origen
		log4j.info("[copyWorkersFromRequest] Leyendo trabajadores de solicitud origen: " + sourceRequestId);
		List<PlantAccessWorker> sourceWorkers = searchWorkersPlantAccessByIdRequest(sourceRequestId);
		log4j.info("[copyWorkersFromRequest] Trabajadores encontrados en origen: " + (sourceWorkers != null ? sourceWorkers.size() : 0));

		List<PlantAccessWorker> newWorkers = new ArrayList<>();
		if(sourceWorkers != null) {
			int workerIndex = 1;
			for(PlantAccessWorker sw : sourceWorkers) {
				log4j.info("[copyWorkersFromRequest] --- Procesando trabajador " + workerIndex + "/" + sourceWorkers.size()
						+ " | id origen=" + sw.getId()
						+ " | nombre=" + sw.getEmployeeName() + " " + sw.getEmployeeLastName()
						+ " | CURP=" + sw.getEmployeeCurp()
						+ " | RFC=" + sw.getEmployeeRfc());

				PlantAccessWorker nw = new PlantAccessWorker();
				nw.setActivities(sw.getActivities());
				nw.setAllDocuments(sw.isAllDocuments());
				nw.setCardNumber(sw.getCardNumber());
				nw.setDatefolioIDcard(sw.getDatefolioIDcard());
				nw.setDateInduction(sw.getDateInduction());
				nw.setEmployeeCurp(sw.getEmployeeCurp());
				nw.setEmployeeLastName(sw.getEmployeeLastName());
				nw.setEmployeeSecondLastName(sw.getEmployeeSecondLastName());
				nw.setEmployeeName(sw.getEmployeeName());
				nw.setEmployeeOrdenes(sw.getEmployeeOrdenes());
				nw.setEmployeePuesto(sw.getEmployeePuesto());
				nw.setEmployeeRfc(sw.getEmployeeRfc());
				nw.setFechaRegistro(new Date());
				nw.setListDocuments(sw.getListDocuments());
				nw.setMembershipIMSS(sw.getMembershipIMSS());
				nw.setDocsActivity1(sw.isDocsActivity1());
				nw.setDocsActivity2(sw.isDocsActivity2());
				nw.setDocsActivity3(sw.isDocsActivity3());
				nw.setDocsActivity4(sw.isDocsActivity4());
				nw.setDocsActivity5(sw.isDocsActivity5());
				nw.setDocsActivity6(sw.isDocsActivity6());
				nw.setDocsActivity7(sw.isDocsActivity7());
				nw.setRequestNumber(targetRequestId);

				save(nw);
				log4j.info("[copyWorkersFromRequest] Trabajador guardado en destino con id=" + nw.getId()
						+ " | requestNumber=" + targetRequestId);

				List<FileStore> files = fileStoreService.getFilesPlantAccessWorker(sourceRequestId, sw.getId(), true);
				int filesCopied = 0;
				if(files != null) {
					log4j.info("[copyWorkersFromRequest] Copiando " + files.size() + " archivo(s) del trabajador origen id=" + sw.getId() + " al nuevo id=" + nw.getId());
					for(FileStore f : files) {
						log4j.info("[copyWorkersFromRequest]   - Copiando archivo | tipo=" + f.getDocumentType() + " | nombre=" + f.getOriginName() + " | fileType=" + f.getFileType());
						FileStore copy = new FileStore();
						copy.setContent(f.getContent());
						copy.setFileType(f.getFileType());
						copy.setOriginName(f.getOriginName());
						copy.setDocumentType(f.getDocumentType());
						copy.setStatus(f.getStatus());
						copy.setNumRefer(nw.getId());
						copy.setNamefile(String.valueOf(nw.getId()));
						copy.setUuid(targetRequestId);
						fileStoreService.save(copy);
						filesCopied++;
						log4j.info("[copyWorkersFromRequest]   - Archivo copiado OK con nuevo id=" + copy.getId());
					}
				} else {
					log4j.info("[copyWorkersFromRequest] Trabajador origen id=" + sw.getId() + " no tiene archivos para copiar.");
				}
				log4j.info("[copyWorkersFromRequest] Trabajador " + workerIndex + " completado | archivos copiados: " + filesCopied);
				newWorkers.add(nw);
				workerIndex++;
			}
		}
		log4j.info("[copyWorkersFromRequest] FIN OK - total trabajadores copiados: " + newWorkers.size()
				+ " | source=" + sourceRequestId + " -> target=" + targetRequestId);
		
		PlantAccessRequest paForAudit = plantAccessRequestService.getById(Integer.valueOf(targetRequestId));
		
		dataAuditService.saveDataAudit("CopyWorkers", paForAudit.getAddressNumberPA(), new Date(), requestHTTP.getRemoteAddr(),
		        usr, "Copied workers - source: " + sourceRequestId + " -> target: " + targetRequestId 
		            + " | replaceExisting: " + replaceExisting + " | total: " + newWorkers.size(), 
		        "copyWorkersTransactional",
		        newWorkers.toString(), null, null, null,
		        null, null, AppConstants.PLANTACCESS_MODULE);
		
		return newWorkers;
	}
}
