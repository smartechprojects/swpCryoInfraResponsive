package com.eurest.supplier.util;

import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.stream.FileImageInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.eurest.supplier.dto.BatchReportDetalle;
import com.eurest.supplier.dto.ListImageQR;
import com.eurest.supplier.model.BatchJournal;
import com.eurest.supplier.model.FiscalDocuments;
import com.eurest.supplier.model.FiscalDocumentsConcept;
import com.eurest.supplier.model.PlantAccessRequest;
import com.eurest.supplier.model.PlantAccessWorker;
import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.model.TaxVaultDocument;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.service.EmailService;
import com.eurest.supplier.service.SupplierService;
import com.eurest.supplier.service.UdcService;
import com.google.gson.Gson;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class PDFUtils {
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	SupplierService supplierService;
	
	private Logger log4j = LogManager.getLogger(PDFUtils.class);
	
	@SuppressWarnings("deprecation")
	public  byte[] getFilePDFFleightCover(FiscalDocuments fisDoc, Supplier supplier, List<FiscalDocumentsConcept> fisDocConcept,String approvalMsg,UdcService udcService) {
		byte[] fileByteArray = null;
		HashMap<String,Object> hmParams=new HashMap<String,Object>();
		
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEEE dd MMMMM yyyy");
		String date = simpleDateFormat.format(new Date());
		log4j.info(date);
		
		       
	    
		try {
		String path = this.getClass().getClassLoader().getResource("").getPath();
	    String fullPath = URLDecoder.decode(path, "UTF-8");
	    String pathArr[] = fullPath.split("/WEB-INF/classes/");
	    //System.out.println(fullPath);
	    //System.out.println(pathArr[0]);
	    
	    String basePath = pathArr[0];
		
		File file = new File(basePath + "/resources/images/CryoInfra-logo-gris.png");
		List<UDC> listPlantas =null;
		try {
			listPlantas =udcService.searchListBySystemAndKey("FLETE","PLANTA");
		} catch (Exception e) {
			log4j.error("Exception" , e);
			listPlantas=new ArrayList<>();
		}
		
		Map<String, String> listplant=new HashMap<>();
		
		for (UDC udc : listPlantas) {
			listplant.put(udc.getStrValue1(), udc.getStrValue2());
		}
		
		
		
		//try {    
			JasperReport jasperReport = null;
		hmParams.put("supplierName", supplier.getName());
		hmParams.put("logo", file.toURL());
		if(fisDocConcept.isEmpty()) {
			hmParams.put("zone", "Sin Planta");
		}else {
			String zone=listplant.get(fisDocConcept.get(0).getSerie());
			
			hmParams.put("zone",zone==null?"Sin Planta":zone);
		}
	
		hmParams.put("fechaActual", date);
		hmParams.put("aprovadores", approvalMsg);
		
		
		double totalImporte = Double.parseDouble("0");
		double totalRetIVA = Double.parseDouble("0");
		double totalIVA = Double.parseDouble("0");
		double totalPago = Double.parseDouble("0");
		
		for(FiscalDocumentsConcept fdocConcept : fisDocConcept) {
			
			totalImporte = totalImporte + fdocConcept.getSubtotal();
			totalRetIVA = totalRetIVA + fdocConcept.getRetIva();
			totalIVA = totalIVA + fdocConcept.getIva();
			totalPago = totalPago + fdocConcept.getAmount();
			
		}
	
		hmParams.put("totalImporte", totalImporte);
		hmParams.put("totalRetIVA", totalRetIVA);
		hmParams.put("totalIVA", totalIVA);
		hmParams.put("totalPago", totalPago);
		hmParams.put("payWeek", fisDoc.getSemanaPago());
	
		
		
		JRBeanCollectionDataSource dtlJRBean = new JRBeanCollectionDataSource(fisDocConcept); 
		hmParams.put("detailDataSource", dtlJRBean);
			
	
			
			jasperReport = getCompiledFile(fullPath + "jasperReports/Caratula_Freight.jasper",
					fullPath + "jasperReports/Caratula_Freight.jrxml");
			fileByteArray = generateReportPDF(hmParams, jasperReport, new JREmptyDataSource());

			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
     
		return fileByteArray;	
	}

	public  byte[] getFilePDFBatch(FiscalDocuments fisDoc, Supplier supplier, List<FiscalDocumentsConcept> fisDocConcept,List<BatchJournal> batchList) {
		byte[] fileByteArray = null;
		HashMap<String,Object> hmParams=new HashMap<String,Object>();
		
		ApplicationContext context = ApplicationContextProvider.getApplicationContext();
		try {
		String path = this.getClass().getClassLoader().getResource("").getPath();
	    String fullPath = URLDecoder.decode(path, "UTF-8");
	    String pathArr[] = fullPath.split("/WEB-INF/classes/");
	    
	    String basePath = pathArr[0];
		
		File file = new File(basePath + "/resources/images/CryoInfra-logo-gris.png");
		
		//try {    
			JasperReport jasperReport = null;
		hmParams.put("logo", file.toURL());
	
		
		double totaldebito = Double.parseDouble("0");
		double totalCredito = Double.parseDouble("0");
		
		
		List<BatchReportDetalle> listbatch=new ArrayList<BatchReportDetalle>();
		
		for(FiscalDocumentsConcept fiscalDocumentsConcept :fisDocConcept ) {
			BatchReportDetalle dto=new BatchReportDetalle();
			dto.setTP(fiscalDocumentsConcept.getSerieBitacora());
			dto.setNumDoc(fiscalDocumentsConcept.getFolio());
			dto.setCia(fisDoc.getRfcReceptor());
			dto.setFechaLM(fiscalDocumentsConcept.getInvoiceDate());
			dto.setDescripCuenta(fiscalDocumentsConcept.getConceptAccount());
			dto.setCdMon(fiscalDocumentsConcept.getMoneda());
			dto.setDebito(fiscalDocumentsConcept.getAmount()+"");
			dto.setUnidades(fiscalDocumentsConcept.getCantidad()+"");
			
			listbatch.add(dto);
			
			
			totaldebito=totaldebito+Double.parseDouble(dto.getDebito());
			totalCredito=totalCredito+Double.parseDouble(dto.getCredito()==null?"0":dto.getCredito());
			
		}
	
		
		
		JRBeanCollectionDataSource DsBatchReport = new JRBeanCollectionDataSource(listbatch); 
		hmParams.put("ds", DsBatchReport);
			
		
		
	 //  fullPath = pathArr[0];
	    
		
			
			jasperReport = getCompiledFile(fullPath + "jasperReports/Batch_Report.jasper",
					fullPath + "jasperReports/Batch_Report.jrxml");
			fileByteArray = generateReportPDF(hmParams, jasperReport, new JREmptyDataSource());

			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
     
		return fileByteArray;	
	}
	
	public  byte[] getPlantAccessPDF(PlantAccessRequest request,List<PlantAccessWorker> workers) { 
		byte[] fileByteArray = null;
		HashMap<String,Object> hmParams=new HashMap<String,Object>();
		SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		ApplicationContext context = ApplicationContextProvider.getApplicationContext();
		try {
		String path = this.getClass().getClassLoader().getResource("").getPath();
	    String fullPath = URLDecoder.decode(path, "UTF-8");
	    String pathArr[] = fullPath.split("/WEB-INF/classes/");
	    
	    String basePath = pathArr[0];
	    
	    final String OLD_FORMAT = "dd-MM-yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(OLD_FORMAT);
		
	    
		File file =new File(basePath + "/resources/images/CryoInfra-logo-gris.png");
		/*
		if (new File(basePath + "/resources/images/imagesrfc/"+doc.getRfcReceptor()+".png").exists()) {
			file=new File(basePath + "/resources/images/imagesrfc/"+doc.getRfcReceptor()+".png");
		}else {
			file=new File(basePath + "/resources/images/imagesrfc/blank.png");
		}/*
		File file2=null;
		if(doc.getRfcReceptor().equals("CRY800801222")) {
		 file2 = new File(basePath + "/resources/images/imagesrfc/esr.png");
		}else {
			file2 = new File(basePath + "/resources/images/imagesrfc/blank.png");
		}*/
		 
		JasperReport jasperReport = null;
		hmParams.put("logo", file.toURL());
		hmParams.put("nameRequest",request.getNameRequest());
		
		 String[] groups = request.getOrdenNumber().split("\\|");
	        StringBuilder result = new StringBuilder();

	        // Recorrer cada grupo y obtener el primer valor después de dividir por ','
	        for (String group : groups) {
	            String[] parts = group.split(",");
	            if (parts.length > 0) {
	                if (result.length() > 0) {
	                    result.append(", ");
	                }
	                result.append(parts[0]);
	            }
	        }
		
		hmParams.put("orderNumber",result.toString());
		hmParams.put("contractorCompany",request.getContractorCompany());
		hmParams.put("contractorRepresentative",request.getContractorRepresentative());
		hmParams.put("registroPatron",request.getEmployerRegistration());
		
		hmParams.put("contactEmergency",request.getContactEmergency());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = request.getFechafirmGui() != null ? dateFormat.format(request.getFechafirmGui()) : "";
		hmParams.put("fechafirmGui", formattedDate);
		
		hmParams.put("empresaSub", 
			    request.isSubcontractService() 
			        ? request.getSubContractedCompany().length() > 30 
			            ? request.getSubContractedCompany().substring(0, 30) 
			            : request.getSubContractedCompany() 
			        : "No Aplica");
		hmParams.put("empresaSubRFC",request.isSubcontractService()? request.getSubContractedCompanyRFC():"No Aplica");
		
		String activities = "";
		
//		String workersList = "";
//		String membershipIMSSList ="";
//		String datefolioIDcard = "";
//		for(PlantAccessWorker x : workers) {	
//			workersList = workersList + x.getEmployeeName() + " " + x.getEmployeeLastName() + " " + x.getEmployeeSecondLastName() + "\n";
//			membershipIMSSList = membershipIMSSList + x.getMembershipIMSS() + "\n";
//			datefolioIDcard = datefolioIDcard + x.getDatefolioIDcard() + "\n";
//			activities = activities + x.getActivities();
//		}
//		hmParams.put("nameWorker",workersList);
//		hmParams.put("membershipIMSS", membershipIMSSList);
//		hmParams.put("datefolioIDcard", datefolioIDcard);
		ArrayList<ListImageQR> listaQR=new ArrayList<>();
		
		for(PlantAccessWorker x : workers) {
			activities = activities + x.getActivities();
		}
		
		ArrayList<byte[]> qrs=new QRutils().generateQRCodes(request, workers);
		
		for (int i = 0; i < qrs.size(); i++) {
			
			ListImageQR row=new ListImageQR();
			row.setImagen1(new ByteArrayInputStream(qrs.get(i)));
			try {
				row.setImagen2(new ByteArrayInputStream(qrs.get(i+1)));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			listaQR.add(row);
			i++;
			
			
		}
		
		
		JRBeanCollectionDataSource dtlJRBean = new JRBeanCollectionDataSource(listaQR); 
		hmParams.put("workerDataSource", dtlJRBean);
		
		hmParams.put("approver",request.getNombreAprobador());
		
		if(request.getFechaAprobacion() != null) {
			hmParams.put("dateApprov",sdf.format(request.getFechaAprobacion()));
		}
		
		if(request.getFechaInicio() != null) {
			hmParams.put("fromDatePA",sdf.format(request.getFechaInicio()));
		}
		
		if(request.getFechaFin() != null) {
			hmParams.put("toDatePa",sdf.format(request.getFechaFin()));
		}		
		
		if(activities.contains("1"))hmParams.put("activ1","X");
		if(activities.contains("2"))hmParams.put("activ2","X");
		if(activities.contains("3"))hmParams.put("activ3","X");
		if(activities.contains("4"))hmParams.put("activ4","X");
		if(activities.contains("5"))hmParams.put("activ5","X");
		if(activities.contains("6"))hmParams.put("activ6","X");
		if(activities.contains("*"))hmParams.put("activ7","X");
		/*hmParams.put("Nombre", doc.getEmisor());
		hmParams.put("FechaEmision", dt1.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(doc.getInvoiceDate().replace("T", " "))));
		hmParams.put("Total", doc.getAmount());
		hmParams.put("Serie", doc.getSerie()==null||doc.getSerie().equals("null")?"":doc.getSerie());
		hmParams.put("Folio", doc.getFolio()==null||doc.getFolio().equals("null")?"":doc.getFolio());
		
		
		hmParams.put("FechaRecepcion", dt1.format(doc.getUploadDate()));*/
		
	
	    
		
			
			jasperReport = getCompiledFile(fullPath + "jasperReports/plantAccess_new.jasper",
					fullPath + "jasperReports/plantAccess_new.jrxml");
			fileByteArray = generateReportPDF(hmParams, jasperReport, new JREmptyDataSource());
			
			//Supplier supp = supplierService.searchByAddressNumber(request.getAddressNumberPA());
			
			//emailService.sendEmailWithAttach("PDF Plant Access", "PDF Plant Access", "mgarcia@smartech.com.mx", fileByteArray, "plantAccessRequest");

			
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
     
		return fileByteArray;	
	
	}

	public  byte[] getTaxVaulDocumentPDF(TaxVaultDocument doc) {
		byte[] fileByteArray = null;
		HashMap<String,Object> hmParams=new HashMap<String,Object>();
		SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		ApplicationContext context = ApplicationContextProvider.getApplicationContext();
		try {
		String path = this.getClass().getClassLoader().getResource("").getPath();
	    String fullPath = URLDecoder.decode(path, "UTF-8");
	    String pathArr[] = fullPath.split("/WEB-INF/classes/");
	    
	    String basePath = pathArr[0];
		
		File file =null;
		if (new File(basePath + "/resources/images/imagesrfc/"+doc.getRfcReceptor()+".png").exists()) {
			file=new File(basePath + "/resources/images/imagesrfc/"+doc.getRfcReceptor()+".png");
		}else {
			file=new File(basePath + "/resources/images/imagesrfc/blank.png");
		}
		File file2=null;
		if(doc.getRfcReceptor().equals("CRY800801222")) {
		 file2 = new File(basePath + "/resources/images/imagesrfc/esr.png");
		}else {
			file2 = new File(basePath + "/resources/images/imagesrfc/blank.png");
		}
		 
			JasperReport jasperReport = null;
		hmParams.put("logo", file.toURL());
		hmParams.put("logo2", file2.toURL());
		hmParams.put("UUID",doc.getUuid());
		hmParams.put("RFCEmisor", doc.getRfcEmisor());
		hmParams.put("RFCReceptor", doc.getRfcReceptor());
		hmParams.put("Nombre", doc.getEmisor());
		hmParams.put("FechaEmision", dt1.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(doc.getInvoiceDate().replace("T", " "))));
		hmParams.put("Total", doc.getAmount());
		hmParams.put("Serie", doc.getSerie()==null||doc.getSerie().equals("null")?"":doc.getSerie());
		hmParams.put("Folio", doc.getFolio()==null||doc.getFolio().equals("null")?"":doc.getFolio());
		
		
		hmParams.put("FechaRecepcion", dt1.format(doc.getUploadDate()));
		
	
	    
		
		if(doc.getOrigen().equals("COMPLEMENTO")) {
			jasperReport = getCompiledFile(fullPath + "jasperReports/TaxVaultComplement.jasper",
					fullPath + "jasperReports/TaxVaultComplement.jrxml");
		}else if(doc.getOrigen().equals("NOTA_CREDITO")) {
			jasperReport = getCompiledFile(fullPath + "jasperReports/TaxVaultDocumentNC.jasper",
					fullPath + "jasperReports/TaxVaultDocumentNC.jrxml");
		}else if(doc.getOrigen().equals("FACTURA")) { 
			jasperReport = getCompiledFile(fullPath + "jasperReports/TaxVaultDocument.jasper",
					fullPath + "jasperReports/TaxVaultDocument.jrxml");
		}
			
			fileByteArray = generateReportPDF(hmParams, jasperReport, new JREmptyDataSource());

			 
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
		}
     
		return fileByteArray;	
	}
	
	
	private static JasperReport getCompiledFile(String filePathJasper, String filePathJrxml) throws JRException {
		File reportFile = new File(filePathJasper);
		JasperCompileManager.compileReportToFile(filePathJrxml, filePathJasper);
		JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());
		return jasperReport;
	} 
	
	private static byte[] generateReportPDF (Map<String, Object> parameters, JasperReport jasperReport, JREmptyDataSource conn) throws Exception{		
		byte[] bytes = null;
		bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, conn);
		return bytes;
	}

	
	public String getPdfBatchNumber(byte[] input){
		
		PDDocument pdDocument = null;
		try {
//		    pdDocument = PDDocument.load(new File("C:\\Users\\Dell\\Desktop\\facturas fletes\\R09801_SMARTECFLT_1005_PDF.pdf"));
		    pdDocument = PDDocument.load(input);

		 
		    PDFTextStripper pdfStripper = new PDFTextStripper();
		    pdfStripper.setStartPage(1);
		    pdfStripper.setEndPage(1);
		    String parsedText = pdfStripper.getText(pdDocument);
		    String batch=null;
		    try {
		    	batch=(parsedText.split("\n")[2]).split(" ")[3];
		    } catch (ArrayIndexOutOfBoundsException e) {		    
		    }
		    return batch;
		    
		} catch (IOException e) {
		    // TODO Auto-generated catch block
			log4j.error("Exception" , e);
		    e.printStackTrace();
		} finally {
		    if (pdDocument != null) {
		        try {
		            pdDocument.close();
		        } catch (IOException e) {
		        	log4j.error("Exception" , e);
		            e.printStackTrace();
		        }
		    }
		}
		
		return null;
	}
	
	
	 public String extractTextFromRegion(byte[] pdfBytes,int page, int x, int y, int width, int height) {
	        try (PDDocument document = PDDocument.load(pdfBytes)) {
	            // Configurar la extracción por área
	            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
	            stripper.setSortByPosition(true);

	            // Definir el área de extracción
	            Rectangle region = new Rectangle(x, y, width, height);
	            stripper.addRegion("targetRegion", region);

	            // Extraer texto de la primera página
	            PDPage firstPage = document.getPage(page);
	            stripper.extractRegions(firstPage);

	            // Retornar el texto extraído
	            return stripper.getTextForRegion("targetRegion").trim();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	   
	   
	   public byte[] loadPdfAsBytes(String filePath) {
	        try {
	            return Files.readAllBytes(new File(filePath).toPath());
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	   
	 
	   
	   
	   
	   // Método unificado que recibe el texto y un arreglo de etiquetas
	    public String getJsonText(String texto, String[] etiquetas) {
	        // Limpiar el texto: asegurar que cada etiqueta y valor estén en una línea separada
	        String textoLimpio = limpiarTexto(texto, etiquetas);

	        // Unir líneas que no tienen dos puntos (continuación de valores)
	        textoLimpio = unirLineasSinDosPuntos(textoLimpio);

	        // Procesar el texto limpio y extraer los valores
	        Map<String, String> direccion = extraerValores(textoLimpio);

	        // Convertir el Map a un string JSON usando Gson
	        Gson gson = new Gson();
	        return gson.toJson(direccion);
	    }

	    // Método para limpiar el texto
	    private static String limpiarTexto(String texto, String[] etiquetas) {
	        // Recorrer las etiquetas y asegurar que estén en líneas separadas
	        for (String etiqueta : etiquetas) {
	            texto = texto.replace(etiqueta, "\n" + etiqueta); // Insertar un salto de línea antes de cada etiqueta
	        }

	        // Eliminar el primer salto de línea si existe
	        if (texto.startsWith("\n")) {
	            texto = texto.substring(1);
	        }

	        return texto;
	    }

	    // Método para unir líneas que no tienen dos puntos
	    private static String unirLineasSinDosPuntos(String texto) {
	        StringBuilder textoLimpio = new StringBuilder();
	        String[] lineas = texto.split("\n"); // Dividir el texto por líneas

	        for (String linea : lineas) {
	            if (!linea.contains(":")) {
	                // Si la línea no tiene dos puntos, es una continuación del valor anterior
	                textoLimpio.append(" ").append(linea.trim()); // Unir con la línea anterior
	            } else {
	                // Si tiene dos puntos, es una nueva etiqueta y valor
	                textoLimpio.append("\n").append(linea.trim());
	            }
	        }

	        return textoLimpio.toString().trim(); // Eliminar espacios en blanco al inicio y final
	    }

	    // Método para extraer valores del texto limpio
	    private static Map<String, String> extraerValores(String texto) {
	        Map<String, String> map = new HashMap<>();
	        String[] lineas = texto.split("\n"); // Dividir el texto por líneas

	        // Recorrer cada línea y extraer etiqueta y valor
	        for (String linea : lineas) {
	            int separador = linea.indexOf(":"); // Buscar el separador entre etiqueta y valor
	            if (separador != -1) {
	                String etiquetaOriginal = linea.substring(0, separador).trim(); // Extraer la etiqueta original
	                String valor = linea.substring(separador + 1).trim(); // Extraer el valor

	                // Normalizar la etiqueta: eliminar espacios y acentos
	                String etiquetaNormalizada = normalizarEtiqueta(etiquetaOriginal);

	                // Guardar en el Map usando la etiqueta normalizada
	                map.put(etiquetaNormalizada, valor);
	            }
	        }

	        return map;
	    }

	    // Método para normalizar etiquetas (eliminar espacios y acentos)
	    private static String normalizarEtiqueta(String etiqueta) {
	        // Eliminar espacios y convertir a minúsculas
	        String normalizada = etiqueta.replace(" ", "").toLowerCase();

	        // Eliminar acentos y caracteres especiales
	        normalizada = normalizada.replace("á", "a")
	                                .replace("é", "e")
	                                .replace("í", "i")
	                                .replace("ó", "o")
	                                .replace("ú", "u")
	                                .replace("ñ", "n")
	                                .replace(":", ""); // Eliminar los dos puntos

	        return normalizada;
	    }
	    
		   /**
		    * Normaliza un RFC eliminando caracteres especiales y convirtiéndolo a mayúsculas.
		    * Elimina guiones, puntos, espacios y otros caracteres no alfanuméricos.
		    * @param rfc RFC a normalizar
		    * @return RFC normalizado en mayúsculas sin caracteres especiales
		    */
		   public static String normalizarRFC(String rfc) {
		       if (rfc == null || rfc.trim().isEmpty()) {
		           return rfc;
		       }
		       
		       // Convertir a mayúsculas y eliminar todos los caracteres que no sean letras o números
		       return rfc.toUpperCase().replaceAll("[^A-Z0-9]", "");
		   }
	
}
