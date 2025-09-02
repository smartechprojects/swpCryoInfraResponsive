package com.eurest.supplier.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.eurest.supplier.util.AppConstants;

import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.service.XmlToPojoService;

public class FTPTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String server = "dvd.technicolor.com";
        int port = 21;
        String user = "umgmex01";
        String pass = "iniit200";


        FTPClient ftpClient = new FTPClient();
        String fName = "CFDI108.xml";
        
        try {

            ftpClient.connect(server, port);
            showServerReply(ftpClient);
 
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Connect failed");
            }
 
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
 
            if (!success) {
                System.out.println("Could not login to the server");
            }
 
            // Changes working directory
            success = ftpClient.changeWorkingDirectory(AppConstants.FTP_FILEPATH);
            
            FTPFile[] files = ftpClient.listFiles();
            
            InputStream isn = ftpClient.retrieveFileStream("Pagos-PR-00001.xml");
            Scanner scn = new Scanner(isn);
            //Reading the file line by line and printing the same
            while (scn.hasNextLine()) 
            System.out.println(scn.nextLine());
            //Closing the channels
            scn.close();
            isn.close();
            
            List<FTPFile> invoicesXML = new ArrayList<FTPFile>();
            List<FTPFile> invoicesPDF = new ArrayList<FTPFile>();
            List<FTPFile> credNotesXML = new ArrayList<FTPFile>();
            List<FTPFile> paymentsXML = new ArrayList<FTPFile>();
            List<FTPFile> other = new ArrayList<FTPFile>();
             
            for (FTPFile file : files) {
                String fileName = file.getName();
                if (!file.isDirectory()) {
                	
                	if(containsIgnoreCase(fileName,"")) {
                		if(containsIgnoreCase(fileName,"pdf")) {
                			invoicesPDF.add(file);
                		}else {
                			invoicesXML.add(file);
                			InputStream is = ftpClient.retrieveFileStream(fileName);
                            Scanner sc = new Scanner(is);
                            StringBuffer sb = new StringBuffer();
                            while (sc.hasNextLine()) {
                            	sb.append(sc.nextLine());
                            }
                            InvoiceDTO dto = getInvoiceXml(sb.toString());
                            sc.close();
                            is.close();  
                		}
                	}else if(containsIgnoreCase(fileName,"")) {
                		credNotesXML.add(file);
                		InputStream is = ftpClient.retrieveFileStream(fileName);
                        Scanner sc = new Scanner(is);
                        StringBuffer sb = new StringBuffer();
                        while (sc.hasNextLine()) {
                        	sb.append(sc.nextLine());
                        }
                        InvoiceDTO dto = getInvoiceXml(sb.toString());
                        sc.close();
                        is.close();
                	}else if(containsIgnoreCase(fileName,"")) {
                		paymentsXML.add(file);
                		InputStream is = ftpClient.retrieveFileStream(fileName);
                        Scanner sc = new Scanner(is);
                        StringBuffer sb = new StringBuffer();
                        while (sc.hasNextLine()) {
                        	sb.append(sc.nextLine());
                        }
                        InvoiceDTO dto = getInvoiceXml(sb.toString());
                        sc.close();
                        is.close();
                	}else {
                		other.add(file);
                	}
                }
            }
             
            int i = 0;
            
            //Create directories
            /*
            ftpClient.makeDirectory("FACTURAS");
            success = ftpClient.changeWorkingDirectory("/TEST/FACTURAS");
            ftpClient.makeDirectory("ERROR");
            ftpClient.makeDirectory("INPUT");
            ftpClient.makeDirectory("OUTPUT");
            ftpClient.makeDirectory("OTROS");
            */
            
            //Upload test file
            /*
            File firstLocalFile = new File("c:/temp/CFDI108.xml");
            String firstRemoteFile = fName;
            InputStream inputStream = new FileInputStream(firstLocalFile);
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }
            
            
            //Read file
            InputStream is = ftpClient.retrieveFileStream(fName);
            Scanner sc = new Scanner(is);
            //Reading the file line by line and printing the same
            while (sc.hasNextLine()) 
            System.out.println(sc.nextLine());
            //Closing the channels
            sc.close();
            is.close();
            
            
            
            // Rename the remote file (or directory)
            String existingFilepath =  fName;
            String newFilepath = "/TEST/FACTURAS/OUTPUT/txtle.xml";
            success = ftpClient.rename(existingFilepath,newFilepath);
            if (success) {
                System.out.println("Transferido");
                return;
            }
            */

            
            
            //Delete file
           /*
            success = ftpClient.changeWorkingDirectory("/TEST/FACTURAS");
            String filename = "CFDI108.xml";
            boolean exist = ftpClient.deleteFile(filename);
            if (exist) {
             System.out.println("File '"+ filename + "' deleted...");
            }else {
             System.out.println("File '"+ filename + "' doesn't exist...");
            }
            */
                       
            ftpClient.logout();
            ftpClient.disconnect();
 
        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }
    }
 
    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
    
    public static boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }
    
    public static String convertStringToXml(String str) {
		return str;
    }
    
	public static InvoiceDTO getInvoiceXml(String uploadItem){
		XmlToPojoService xmlToPojoService = new XmlToPojoService();
		try{
			ByteArrayInputStream stream = new  ByteArrayInputStream(uploadItem.getBytes());
			String xmlContent = IOUtils.toString(stream, "UTF-8");
			return xmlToPojoService.convert(xmlContent);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
    
    
}
