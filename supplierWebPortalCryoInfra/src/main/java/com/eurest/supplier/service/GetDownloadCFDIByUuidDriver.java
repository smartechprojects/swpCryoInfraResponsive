package com.eurest.supplier.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;

import com.eurest.supplier.util.DownloadSATUtils;

public class GetDownloadCFDIByUuidDriver {
	
	public WebDriver driver;
	public int chromeDriverProcessID = 0;
	public int chromeProcesID = 0;
	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(GetDownloadCFDIByUuidDriver.class);
	
	public WebDriver setUp() {
		
			String downloadDirectory = "C:\\DownloadCFDIByUuid";
			String chromeDriverDirectory = "C:\\DownloadCFDIByUuid\\chromedriver.exe";
            String uuidAsString = UUID.randomUUID().toString();
	 	   log4j.info("Inicio: " + new Date());
				
				System.setProperty("webdriver.chrome.driver", chromeDriverDirectory);

				Map<String, Object> prefs = new HashMap<String, Object>();

				prefs.put("download.prompt_for_download", false);
				prefs.put("download.extensions_to_open", "application/xml");
				prefs.put("safebrowsing.enabled", true);
				// indicamos que la notificaciones se bloqueen
				prefs.put("profile.default_content_setting.values.notifications", 2);
				// indicamos que el navegador llame al directorio que hemos indicado
				prefs.put("download.default_directory", downloadDirectory);

				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", prefs);
				options.addArguments("disable-infobars");
				options.addArguments("--app=https://portalcfdi.facturaelectronica.sat.gob.mx/");
				options.addArguments("--window-size=900,900");
				//options.addArguments("--window-position=-2000,0");
				options.addArguments("--safebrowsing-disable-download-protection");
				options.addArguments("safebrowsing-disable-extension-blacklist");
				 options.addArguments("--user-data-dir=C:\\DownloadCFDIByUuid\\ChromeProfiles\\" +
				 uuidAsString); // Custom directory path for first profile
				options.setAcceptInsecureCerts(true);

				ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
	            int port = chromeDriverService.getUrl().getPort();
	            driver = new ChromeDriver(chromeDriverService, options);
	           	   
	            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	            
	            log4j.info("starting chromedriver on port " + port);
	            try {
					chromeDriverProcessID = DownloadSATUtils.GetChromeDriverProcessID(port);
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            log4j.info("detected chromedriver process id " + chromeDriverProcessID);
	            try {
					chromeProcesID = DownloadSATUtils.GetChromeProcesID(chromeDriverProcessID);
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            log4j.info("detected chrome process id " + chromeProcesID);
	                     
	           return driver;
			}
	
}
