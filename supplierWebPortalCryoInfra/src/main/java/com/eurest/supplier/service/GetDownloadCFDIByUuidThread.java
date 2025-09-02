package com.eurest.supplier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eurest.supplier.model.Company;
import com.eurest.supplier.model.UserDocument;

public class GetDownloadCFDIByUuidThread extends Thread  {
	
	public String folioFiscal;
	public String rfc;
	public String satSite;
	public UserDocument docCert;
	public UserDocument doceKey;
	public Company company;
	public MassiveDownloadService massiveDownloadService;
	public volatile int threadsComplete = 0;
	GetDownloadCFDIByUuid getDownloadCFDIByUuid;
	
	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(GetDownloadCFDIByUuidThread.class);
	
	public GetDownloadCFDIByUuidThread(String rfc, String folioFiscal, UserDocument docCert, UserDocument doceKey, Company company, MassiveDownloadService massiveDownloadService) {
		super();
		this.folioFiscal = folioFiscal;
		this.rfc = rfc;
		this.docCert = docCert;
		this.doceKey = doceKey;
		this.company = company;
		this.massiveDownloadService = massiveDownloadService;
		getDownloadCFDIByUuid = new GetDownloadCFDIByUuid();
	}
		
		@Override
		public void run() {

			log4j.info("thread -- started " + Thread.currentThread().getName());

			try {
				Thread.sleep(5000);
				getDownloadCFDIByUuid.setUp();
				getDownloadCFDIByUuid.downloadCFDI(rfc,folioFiscal,docCert,doceKey,company,massiveDownloadService);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				//getDownloadCFDIByUuid.tearDown();
			}
			threadsComplete  = threadsComplete + 1;

			log4j.info("thread - ended " + Thread.currentThread().getName());
		}

		public int getThreadsComplete() {
			return threadsComplete;
		}

		public void setThreadsComplete(int threadsComplete) {
			this.threadsComplete = threadsComplete;
		}
		
		
	
	
}
