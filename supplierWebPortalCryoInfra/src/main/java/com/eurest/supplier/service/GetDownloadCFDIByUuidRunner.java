package com.eurest.supplier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eurest.supplier.dao.DocumentsDao;
import com.eurest.supplier.dao.InvoiceSATDao;
import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.model.Company;
import com.eurest.supplier.model.InvoiceSAT;
import com.eurest.supplier.model.UserDocument;

@Service("getDownloadCFDIByUuidRunner")
public class GetDownloadCFDIByUuidRunner {
	
	@Autowired
	DocumentsService documentsService;
	
	@Autowired
	InvoiceSATDao invoiceSATDao;
	
	@Autowired
	DocumentsDao documentsDao;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	MassiveDownloadService massiveDownloadService;
	
	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(GetDownloadCFDIByUuidRunner.class);
	
	public void getDownloadCFDI(List<MultipartFile> list) {
		int chromeCount = 1;
		int threadsComplete = 0;
		for (MultipartFile file : list) {
			InvoiceDTO inv = null;
			String ct = file.getContentType();

			if ("text/xml".equals(ct.trim())) {
				inv = documentsService.getInvoiceXml(file);
				if (inv != null) {
					InvoiceSAT invoiceSAT = invoiceSATDao.getByUuid(inv.getUuid());

					if (invoiceSAT == null) {
						//for (int i = 0; i < chromeCount; i++) {
							log4j.info("threads started....." + inv.getUuid());
							UserDocument docCert = documentsDao.searchCriteriaByAddressBookAndType("CERT",
									inv.getRfcReceptor());
							UserDocument doceKey = documentsDao.searchCriteriaByAddressBookAndType("KEY",
									inv.getRfcReceptor());
							Company company = companyService.searchByCompany(inv.getRfcReceptor());

							/*
							 * Foo foo = new Foo(); Thread thread = new Thread(foo); thread.start();
							 * thread.join(); int value = foo.getValue();
							 */

							GetDownloadCFDIByUuidThread getDownloadCFDIByUuidThread = new GetDownloadCFDIByUuidThread(
									inv.getRfcReceptor(), inv.getUuid(), docCert, doceKey, company,massiveDownloadService);

							// new GetDownloadCFDIByUuidThread(inv.getRfcReceptor(), inv.getUuid(), docCert,
							// doceKey , company).start();

							getDownloadCFDIByUuidThread.start();
						

							//threadsComplete = getDownloadCFDIByUuidThread.threadsComplete;

							log4j.info("thread got ended...." + inv.getUuid());
						//}

					} else {

					}
				}
			}
		}
		
	}

}
