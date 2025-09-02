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

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eurest.supplier.dao.ApprovalBatchFreightDao;
import com.eurest.supplier.dao.DocumentsDao;
import com.eurest.supplier.dao.FileStoreDao;
import com.eurest.supplier.dao.FiscalDocumentDao;
import com.eurest.supplier.dao.LogDataAprovalActionDao;
import com.eurest.supplier.dao.PlantAccessRequestDao;
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
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.TaxVaultDocument;
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

@Service("fileStoreService")
public class FileStoreService {
	
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	FileStoreDao fileStoreDao;
	
	
	static String TIMESTAMP_DATE_PATTERN = "yyyy-MM-dd";
	static String TIMESTAMP_DATE_PATTERN_NEW = "yyyy-MM-dd HH:mm:ss";
	static String DATE_PATTERN = "dd/MM/yyyy";
	
	public FileStore getById(int id) {
		return fileStoreDao.getById(id);
	}
	
	
	
	
	public void save(FileStore doc) {
		fileStoreDao.save(doc);
	}
	
	public void update(FileStore doc) {
		fileStoreDao.update(doc);
	}
	
	public void deleteFilesPlantAccess(FileStore doc) {
		fileStoreDao.deleteFiscalDocument(doc);
	}
	
	public List<FileStore> getFilesPlantAccess(String uuid, boolean getB64) {
		return fileStoreDao.getFilesPlantAccess(uuid, getB64);
	}
	
	public List<FileStore> getFilesPlantAccessWorker(String idRequest, int idWorker, boolean getB64) {
		return fileStoreDao.getFilesPlantAccessWorker(idRequest, idWorker, getB64);
	}
	
	public List<FileStore> getFilesPlantAccess(int uuidWorkerid, boolean getB64) {
		return fileStoreDao.getFilesPlantAccess(uuidWorkerid+"", getB64);
	}
	public List<FileStore> getFilesPlantAccessRequest(int uuidWorkerid, boolean getB64) {
		return fileStoreDao.getFilesPlantAccessRequest(uuidWorkerid+"", getB64);
	}
	public List<FileStore> deleteFilesPlantAccess(int idWorker, String documentType) {
		return fileStoreDao.deleteFilesPlantAccess(idWorker, documentType);
	}
	public int updateFileRequest(String uuid,String idref) {
		
		return fileStoreDao.updateFileRequest(uuid, idref); 
		
//		List<FileStore> files=fileStoreDao.getFileStores(uuid);
//		
//		
//		for (FileStore fileStore : files) {
//			fileStore.setNamefile(fileStore.getDocumentType()+(fileStore.getDocumentType().contains("WORKER")?"_"+fileStore.getNumRefer():"")+"."+fileStore.getOriginName().split("\\.")[1]);
////			fileStore.setNumRefer(fileStore.getDocumentType().contains("WORKER")?fileStore.getNumRefer():Integer.parseInt(idref));
//			fileStore.setStatus("COMPPLETED");
//			fileStoreDao.update(fileStore);
//		};
//		
//		
//		return 1;
	}
		
}
