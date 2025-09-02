package com.eurest.supplier.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dao.DocumentsDao;
import com.eurest.supplier.model.Company;
import com.eurest.supplier.model.InvoiceSAT;
import com.eurest.supplier.model.UserDocument;

public class GetDownloadCFDIByUuid extends GetDownloadCFDIByUuidDriver {
		
	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(GetDownloadCFDIByUuid.class);

	public void downloadCFDI(String rfc, String folioFiscal, UserDocument docCert, UserDocument doceKey, Company company, MassiveDownloadService massiveDownloadService){
		
		String downloadDirectory = "C:\\DownloadCFDIByUuid";
		byte[]  content = null;
		InvoiceSAT invoiceSAT=null;
		String statusComp = null;
 	    String statusCancel = null;
 		//int chromeDriverProcessID = 0;
		//int chromeProcesID = 0;
 	    
 	   log4j.info("getDownloadCFDIByUuid: RFC: " + rfc + " - UUID: " + folioFiscal);
 	   
			
		try {
			log4j.info("Inicio: " + new Date());
	
			
			if(docCert == null || doceKey ==null || company ==null) {
				log4j.error("Error el obtener información de la compañia " + rfc);
				// return invoiceSAT;
			}else {
			
			File tempFileCert = File.createTempFile(rfc, ".cer", null);
			FileOutputStream fosCert = new FileOutputStream(tempFileCert);
			fosCert.write(docCert.getContent());
			fosCert.close();

			File tempFileKey = File.createTempFile(rfc, ".key", null);
			FileOutputStream fosKey = new FileOutputStream(tempFileKey);
			fosKey.write(doceKey.getContent());
			fosKey.close();
			
		
			  String elementsSAT = driver.getPageSource();
	            WebDriverWait wait = new WebDriverWait(driver, 10);

	           driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	           WebElement elementHButtonFiel = driver.findElement(By.className("pull-right"));
	            
	            //WebElement elementHButtonFiel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pull-right")));
	            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	            WebElement buttonFiel = elementHButtonFiel.findElement(By.id("buttonFiel"));
	            buttonFiel.click();
	            
	            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	            List<WebElement> elementInputs = driver.findElements(By.className("input-group"));
	            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	            WebElement elementInputCer = elementInputs.get(0).findElement(By.id("fileCertificate"));
	            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	            elementInputCer.sendKeys(tempFileCert.getAbsolutePath());
	            
	            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	            WebElement elementInputKey = elementInputs.get(1).findElement(By.id("filePrivateKey"));
	            elementInputKey.sendKeys(tempFileKey.getAbsolutePath());
	            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	            WebElement elementPrivateKeyPassword = driver.findElement(By.id("privateKeyPassword"));
	            elementPrivateKeyPassword.sendKeys(company.getSecretPass());
	            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	            WebElement elementButtonFiel = driver.findElement(By.className("pull-right"));
	            WebElement buttonSubmitFiel = elementButtonFiel.findElement(By.id("submit"));
	            
	            buttonSubmitFiel.click();
	            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	            List<WebElement> elementRadio = driver.findElements(By.className("radio"));
	            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	            elementRadio.get(1).findElement(By.xpath("//a[@href='ConsultaReceptor.aspx']")).click();
	            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	            
	            WebElement elementInputFolio= driver.findElement(By.id("ctl00_MainContent_PnlConsulta"));
	            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	            WebElement inputFolio = elementInputFolio.findElement(By.id("ctl00_MainContent_TxtUUID"));
	            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	            inputFolio.sendKeys(Keys.HOME + folioFiscal);
	            //inputFolio.sendKeys("54716ADA053A439DB0CA7A213D9B563B");
	            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	            WebElement elementButtonBusqueda = driver.findElement(By.className("clearfix"));
	            WebElement buttonSubmitBusqueda= elementButtonBusqueda.findElement(By.id("ctl00_MainContent_BtnBusqueda"));
	            buttonSubmitBusqueda.click();
	            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	            wait = new WebDriverWait(driver, 10);
	            WebElement elementTablesResult = wait.until(ExpectedConditions.elementToBeClickable(By.id("ctl00_MainContent_tblResult")));

	          List<WebElement> elementResultCFDITD = elementTablesResult.findElements((By.tagName("td")));
	           
	           if (elementResultCFDITD.size() == 0)
	           {
	        	   log4j.info("No se encontró el folio en SAT " + folioFiscal);
	        	 //  return invoiceSAT;
	           }else {
	        	   int x= 0;
	        	   
	        	   statusComp = elementResultCFDITD.get(19).getText();
	        	   statusCancel = elementResultCFDITD.get(18).getText();
	        	   log4j.info(folioFiscal + " - Estado del Comprobante: " + statusComp);
	        	   log4j.info(folioFiscal + " - Estatus de Cancelación: " + statusCancel);
	        	   for(WebElement element : elementResultCFDITD) {
	        		/*   System.out.println(x);
	        		   System.out.println("element:" + element);
	        		   System.out.println("element.getText():" + element.getText());
	        		   System.out.println("element.getTagName():" + element.getTagName());
	        		   System.out.println("element.toString():" + element.toString());
	        		   System.out.println("---------------------");
	        		   x = x+1;*/
	        		   driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	        		 WebElement btnDescarga =  element.findElement(By.id("BtnDescarga"));
	        		   if(btnDescarga != null) {
	        			 /*  System.out.println("btnDescarga");
	        			   System.out.println("element:" + element);
	            		   System.out.println("element.getText():" + element.getText());
	            		   System.out.println("element.getTagName():" + element.getTagName());
	            		   System.out.println("element.toString():" + element.toString());*/
	        			   
	                	   
	        			   if("Vigente".equals(statusComp)) {
	            		   btnDescarga.click();
	        			   }
	            		   
	            		  // driver.switchTo().activeElement();
	            		   driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);		   
	            		   ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
	            		 //  driver.switchTo().window(tabs2.get(1));
	            		  //driver.navigate().refresh();
	            		   try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							log4j.error("InterruptedException" , e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		    break;
	        		   }
	        	   }
	        	  
	        	   
	           }
	            
	           WebElement elementUserCredencials = driver.findElement(By.className("user-credencials"));
	           WebElement cerrarSesion= elementUserCredencials.findElement(By.id("anchorClose"));
	           JavascriptExecutor executor = (JavascriptExecutor)driver;
	           executor.executeScript("arguments[0].click();", cerrarSesion);
	           //SessionId s = ((RemoteWebDriver) driver).getSessionId();
	           //System.out.println("Session Id is: " + s);
	           //browser close
	           
	            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	            driver.close();
	            driver.quit();
	            tempFileCert.delete();
	            tempFileKey.delete();
	            
	            if("Vigente".equals(statusComp)) {
	            content =  massiveDownloadService.searchCFDIPath(downloadDirectory, folioFiscal);
	            
	            if(content != null) {
	            	 invoiceSAT = massiveDownloadService.saveCFDIDownload(content);
	            }
	            
	           
	            }
			}
	            

			}catch(Exception ex) {
				log4j.error("Exception" , ex);
				 ex.printStackTrace();
			}finally {
				log4j.info("Fin: " + new Date());
	            try {
	            	
					//Runtime.getRuntime().exec("taskkill /F /PID " + chromeProcesID);
					//Runtime.getRuntime().exec("taskkill /F /PID " + chromeDriverProcessID);
					
				} catch (Exception e) {
					log4j.error("Exception" , e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		
			
		}
		
		public void tearDown(){
			driver.quit();
		}
		
		public byte[] searchCFDIPath(String path, String folioFiscal) {
			
			File cfdi = new File(path+"\\"+folioFiscal+".xml");
		    byte[] content = null;
			if(cfdi.exists()) {
				  try {
					  content = Files.readAllBytes(cfdi.toPath());
				} catch (IOException e) {
					log4j.error("IOException",e);
					e.printStackTrace();
				}

			}
			
			return content;
		    
		}
		
}
