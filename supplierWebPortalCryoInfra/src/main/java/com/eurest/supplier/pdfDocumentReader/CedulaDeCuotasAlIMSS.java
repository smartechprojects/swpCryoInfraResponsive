package com.eurest.supplier.pdfDocumentReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.eurest.supplier.model.Supplier;
import com.eurest.supplier.util.PDFUtils;
import com.eurest.supplier.util.PDFutils2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

public class CedulaDeCuotasAlIMSS {

    // Propiedades
    private String fechaProceso;
    private String periodoProceso;
    private String registroPatronal;
    private String rfc;
    private String areaGeografica;
    private String razonSocial;
    private String delegacionIMSS;
    private String versactividadion;
    private String subdelegacionIMSS;
    private String domicilio;
    private String pobMuniAlcal;
    private String codigoPostal;
    private String entidad;
    private String primaRT;
    private Date fechaVigencia; // Fecha de vigencia calculada
    
    
    


    // Constructor vacío
    public CedulaDeCuotasAlIMSS() {}

    // Constructor que recibe una ruta de archivo PDF
    public CedulaDeCuotasAlIMSS(String rutaPdf) {
        byte[] pdfBytes = new PDFUtils().loadPdfAsBytes(rutaPdf);
        if (pdfBytes != null&&ValidDoc(pdfBytes)) {
        	setDatosPrimarios(pdfBytes);
        	calcularFechaVigencia(); // Calcular fecha de vigencia
        
        }else {
            throw new IllegalArgumentException("El documento NO es un Detalle de la Declaración ISR e IVA por sueldos válida.");
        }
    }
    
    public CedulaDeCuotasAlIMSS(byte[] pdfBytes) {
        if (pdfBytes != null&&ValidDoc(pdfBytes)) {
        	setDatosPrimarios(pdfBytes);
        	calcularFechaVigencia(); // Calcular fecha de vigencia
        }else {
            throw new IllegalArgumentException("El documento NO es un Detalle de la Declaración ISR e IVA por sueldos válida.");
        }
    }

 

    
    
   
    
    public boolean ValidDoc(byte[] pdfBytes) {
      

//        String extractedText = new PDFUtils().extractTextFromRegion(pdfBytes,0, 10, 10, 750, 50);
//        // Más específico: buscar el texto completo sin "OBRERO-PATRONALES" para evitar conflicto con documento 9
//        return extractedText.contains("CÉDULA DE DETERMINACIÓN DE CUOTAS") && 
//               !extractedText.contains("OBRERO-PATRONALES");
        
        
        Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.MONTH, -1);
		 	
		int monthLoad = (calendar.get(Calendar.MONTH) + 1 );
		int yearLoad = calendar.get(Calendar.YEAR);	
		
        
				
				String [] data= new PDFutils2().getPdfText(pdfBytes, 2, 0);
				if(data==null) {
					// return "El formato del archivo Cédula de determinación de cuotas es INVALIDO, favor de cargar el archivo en un formato PDF/texto";
					return false;
				}
				String string="";
				for (String string2 : data) {
					string=string+" "+string2;
			}
				
				if(data==null || !(string.contains("SISTEMA ÚNICO DE AUTODETERMINACIÓN")&&string.contains("Período de Proceso")&&string.contains("CÉDULA DE DETERMINACIÓN DE CUOTAS"))) {
//					 return "El formato del archivo Cédula de determinación de cuotas es INVALIDO, favor de cargar el archivo en un formato PDF/texto";
					return false;
				}
				
				 
				if(data[2].contains("EXTEMPORÁNEO")) {
					 String meses[] = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};			 			 		 			 
					 String aux = ""; 
					 if (data[4].contains("Período de Proceso:")) {
						 aux = ((data[4].replace("Período de Proceso:","")).replace("Calculo Extemporaneo al:", "")).replace("T. D.", "").trim();				 			    	
					 } else {
						if(data[5].contains("Período de Proceso:")) {
		 				aux = (data[5].replace("Período de Proceso:","")).replace("Calculo Extemporaneo al:", ""); 		 			
		 				aux = (aux.replace(aux.substring( aux.indexOf("T. D.", 0), aux.length()),"")).trim();
						}
					 }
					 if (aux.contains("-")) {
						String mes1 = aux.substring(0, aux.indexOf( "-", 0) ); 
 			    	int mespdf = 0;
 			    	for (int v = 0; v < meses.length; v++) {
 			    		if ( (meses[v]).equals(mes1.toUpperCase()) ) {
 			    			mespdf = v;
 			    		}
 			    	}
 			    	int monthLoad2 = (calendar.get(Calendar.MONTH));
					if ( monthLoad2 == mespdf ) {
						} else {
							//Comentar la siguiente linea para no validar periodo -
							//return "El archivo Cédula de determinación de cuotas no corresponde al periodo del mes solicitado, favor de cargar el archivo correcto";
						}
					 }					    						  	 			    	 	 			    				    	 	 			    	
				 } else {
					if(data[2].contains("OBRERO-PATRONALES, APORTACIONES Y AMORTIZACIONES")) {
						if (data[3].contains("Bimestre de Proceso:")) {						
							String aux = (data[3].replace("Bimestre de Proceso:","")).trim();
		 				String replaceString = aux.replace(aux.substring( aux.indexOf("Fecha de Proceso:", 0)+17, aux.length()),"");
	 		    		aux = (replaceString.replace("Fecha de Proceso:","")).trim();
							  							
						String meses[] = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};			 			 		 			 
						String mes1 = aux.substring(0, aux.indexOf( "-", 0) ); 

						int mespdf = 0;
						for (int v = 0; v < meses.length; v++) {
							if ( (meses[v]).equals(mes1.toUpperCase()) ) {
								mespdf = v;
							}
						}
						int monthLoad2 = (calendar.get(Calendar.MONTH));
						if ( monthLoad2 == mespdf ) {
							} else {
							//Comentar la siguiente linea para no validar periodo -
								//return "El archivo Cédula de determinación de cuotas no corresponde al periodo del mes solicitado, favor de cargar el archivo correcto";
							}
						}
					} else {
						if(data[1].contains("CÉDULA DE DETERMINACIÓN DE CUOTAS")) {
		 			    	if (data[2].contains("Período de Proceso:")) {
			 			    	String meses[] = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};			 			 		 			 
			 			    	String aux = (data[2].replace("Período de Proceso:","")).replace("Fecha de Proceso:", "").trim();				 			    	
			 			    	String mes1 = aux.substring(0, aux.indexOf( "-", 0) ); 
			 			    	
			 			    	int mespdf = 0;
			 			    	for (int v = 0; v < meses.length; v++) {
			 			    		if ( (meses[v]).equals(mes1.toUpperCase()) ) {
			 			    			mespdf = v;
			 			    		}
			 			    	}
			 			    	int monthLoad2 = (calendar.get(Calendar.MONTH));
								if ( monthLoad2 == mespdf ) {
								} else {
								//Comentar la siguiente linea para no validar periodo -
						         //return "El archivo Cédula de determinación de cuotas no corresponde al periodo del mes solicitado, favor de cargar el archivo correcto";
								}						 			  	
		 			    	}
						}
					}
					}
					
				return true;
    }

    
    public void setDatosPrimarios(byte[] pdfBytes) {
        // Extraer una región más amplia para asegurar que obtenemos toda la información
        String text = new PDFUtils().extractTextFromRegion(pdfBytes, 0, 10, 50, 750, 200);
        System.out.println("Texto extraído para parsing:\n" + text);
        
        try {
            // Definir las etiquetas que buscamos en el documento
            String[] etiquetas = {
                "Período de Proceso:",
                "Fecha de Proceso:",
                "Registro Patronal:",
                "RFC:",
                "Area Geográfica:",
                "Nombre o Razón Social:",
                "Delegación IMSS:",
                "Actividad:",
                "SubDelegación IMSS:",
                "Domicilio:",
                "Pob., Mun. / Alcaldía:",
                "Código Postal:",
                "Entidad:",
                "Prima de R.T."
            };
            
            // Usar el método utilitario para extraer los datos como JSON
            PDFUtils pdfUtils = new PDFUtils();
            String jsonResult = pdfUtils.getJsonText(text, etiquetas);
            System.out.println("JSON extraído:\n" + jsonResult);
            
            // Parsear el JSON resultado
            Gson gson = new Gson();
            Map<String, String> datosExtraidos = gson.fromJson(jsonResult, Map.class);
            
            // Asignar los valores extraídos a las propiedades usando las claves normalizadas
            this.periodoProceso = datosExtraidos.get("periodoproceso");
            this.fechaProceso = datosExtraidos.get("fechadeproceso");
            this.registroPatronal = datosExtraidos.get("registropatronal");
            this.rfc = PDFUtils.normalizarRFC(datosExtraidos.get("rfc"));
            this.areaGeografica = datosExtraidos.get("areageografica");
            this.razonSocial = datosExtraidos.get("nombreorazonsocial");
            this.delegacionIMSS = datosExtraidos.get("delegacionimss");
            this.versactividadion = datosExtraidos.get("actividad");
            this.subdelegacionIMSS = datosExtraidos.get("subdelegacionimss");
            this.domicilio = datosExtraidos.get("domicilio");
            this.pobMuniAlcal = datosExtraidos.get("pobmun/alcaldia");
            this.codigoPostal = datosExtraidos.get("codigopostal");
            this.entidad = datosExtraidos.get("entidad");
            this.primaRT = datosExtraidos.get("primadert");
            
            // Logging de resultados
            System.out.println("Datos asignados:");
            System.out.println("Período: " + this.periodoProceso);
            System.out.println("Fecha: " + this.fechaProceso);
            System.out.println("Registro Patronal: " + this.registroPatronal);
            System.out.println("RFC: " + this.rfc);
            System.out.println("Área Geográfica: " + this.areaGeografica);
            System.out.println("Razón Social: " + this.razonSocial);
            System.out.println("Delegación IMSS: " + this.delegacionIMSS);
            System.out.println("Actividad: " + this.versactividadion);
            System.out.println("SubDelegación IMSS: " + this.subdelegacionIMSS);
            System.out.println("Domicilio: " + this.domicilio);
            System.out.println("Población/Municipio: " + this.pobMuniAlcal);
            System.out.println("Código Postal: " + this.codigoPostal);
            System.out.println("Entidad: " + this.entidad);
            System.out.println("Prima R.T.: " + this.primaRT);
            
        } catch (Exception e) {
            System.err.println("Error en parsing de datos primarios: " + e.getMessage());
            e.printStackTrace();
        }
    } 
	

	

	

	@Override
	public String toString() {
		return "CedulaDeCuotasAlIMSS [fechaProceso=" + fechaProceso + ", periodoProceso=" + periodoProceso
				+ ", registroPatronal=" + registroPatronal + ", rfc=" + rfc + ", AreaGeografica=" + areaGeografica
				+ ", razonSocial=" + razonSocial + ", delegacionIMSS=" + delegacionIMSS + ", versactividadion="
				+ versactividadion + ", subdelegacionIMSS=" + subdelegacionIMSS + ", domicilio=" + domicilio
				+ ", pobMuniAlcal=" + pobMuniAlcal + ", codigoPostal=" + codigoPostal + ", entidad=" + entidad
				+ ", primaRT=" + primaRT + "]";
	}

	
	public String getFechaProceso() {
		return fechaProceso;
	}

	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public String getPeriodoProceso() {
		return periodoProceso;
	}

	public void setPeriodoProceso(String periodoProceso) {
		this.periodoProceso = periodoProceso;
	}

	public String getRegistroPatronal() {
		return registroPatronal;
	}

	public void setRegistroPatronal(String registroPatronal) {
		this.registroPatronal = registroPatronal;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = PDFUtils.normalizarRFC(rfc);
	}

	

	public String getAreaGeografica() {
		return areaGeografica;
	}

	public void setAreaGeografica(String areaGeografica) {
		this.areaGeografica = areaGeografica;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getDelegacionIMSS() {
		return delegacionIMSS;
	}

	public void setDelegacionIMSS(String delegacionIMSS) {
		this.delegacionIMSS = delegacionIMSS;
	}

	public String getVersactividadion() {
		return versactividadion;
	}

	public void setVersactividadion(String versactividadion) {
		this.versactividadion = versactividadion;
	}

	public String getSubdelegacionIMSS() {
		return subdelegacionIMSS;
	}

	public void setSubdelegacionIMSS(String subdelegacionIMSS) {
		this.subdelegacionIMSS = subdelegacionIMSS;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getPobMuniAlcal() {
		return pobMuniAlcal;
	}

	public void setPobMuniAlcal(String pobMuniAlcal) {
		this.pobMuniAlcal = pobMuniAlcal;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getPrimaRT() {
		return primaRT;
	}

	public void setPrimaRT(String primaRT) {
		this.primaRT = primaRT;
	}

	// Métodos para fechaVigencia
	public Date getFechaVigencia() {
		return fechaVigencia;
	}

	public void setFechaVigencia(Date fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

	/**
	 * Calcula la fecha de vigencia basada en la fechaProceso del documento.
	 * La fecha de vigencia será el último día del mes de la fechaProceso.
	 */
	private void calcularFechaVigencia() {
		if (this.fechaProceso != null && !this.fechaProceso.trim().isEmpty()) {
			try {
				// Formato esperado: "06/may./2025" o variaciones con mes en español
				String fechaLimpia = this.fechaProceso.trim();
				
				// Reemplazar nombres de meses en español por números
				Map<String, String> meses = new HashMap<>();
				meses.put("ene", "01");
				meses.put("feb", "02");
				meses.put("mar", "03");
				meses.put("abr", "04");
				meses.put("may", "05");
				meses.put("jun", "06");
				meses.put("jul", "07");
				meses.put("ago", "08");
				meses.put("sep", "09");
				meses.put("oct", "10");
				meses.put("nov", "11");
				meses.put("dic", "12");
				
				// Convertir formato "06/may./2025" a "06/05/2025"
				for (Map.Entry<String, String> entry : meses.entrySet()) {
					fechaLimpia = fechaLimpia.toLowerCase().replaceAll(entry.getKey() + "\\.?", entry.getValue());
				}
				
				// Formato esperado ahora: "06/05/2025"
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date fechaEmision = sdf.parse(fechaLimpia);
				
				// Crear calendar para obtener el último día del mes
				Calendar cal = Calendar.getInstance();
				cal.setTime(fechaEmision);
				
				// Establecer al último día del mes
				int ultimoDia = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				cal.set(Calendar.DAY_OF_MONTH, ultimoDia);
				
				this.fechaVigencia = cal.getTime();
			} catch (Exception e) {
				// Si hay error al parsear la fecha, no establecer fechaVigencia
				this.fechaVigencia = null;
			}
		}
	}

	// Método main para pruebas
	
	  public void lecturaPorTablas(byte[] pdfBytes) {
	         // Inicializar la lista de actividades económicas
//	         this.actividadesEconomicas = new ArrayList<>();

	         // Cargar el documento PDF
	         PDDocument document = null;
	         try {
	             document = PDDocument.load(new ByteArrayInputStream(pdfBytes));
	         } catch (IOException e) {
	             e.printStackTrace();
	             return;
	         }

	         // Extraer tablas y convertirlas a JSON
	         String jsonOutput = extractTablesToJson(document);

	         // Crear un JsonParser
	         JsonParser parser = new JsonParser();

	         // Parsear el JSON usando un StringReader
	         JsonArray jsonArray = parser.parse(new StringReader(jsonOutput)).getAsJsonArray();

	         // Encabezados esperados (pueden ajustarse según sea necesario)
	         List<String> encabezadosEsperados = Arrays.asList(
	                 "Orden", "Actividad Económica", "Porcentaje", "Fecha Inicio", "Fecha Fin"
	         );

	         // Bandera para identificar si se encontraron los encabezados de columnas
	         boolean foundColumnHeaders = false;

	         // Iterar sobre cada objeto en el JsonArray
	         for (int i = 0; i < jsonArray.size(); i++) {
	             JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
	             JsonArray rows = jsonObject.getAsJsonArray("rows");

	             // Iterar sobre cada fila en "rows"
	             for (int j = 0; j < rows.size(); j++) {
	                 JsonArray row = rows.get(j).getAsJsonArray();

	              System.out.println(row.toString());
	             }

	             // Si se encontraron los encabezados, salir del bucle de bloques
	             if (foundColumnHeaders) {
	                 break;
	             }
	         }

	         // Si no se encontraron los encabezados
	         if (!foundColumnHeaders) {
	             System.out.println("No se encontraron los encabezados de columnas en ningún bloque.");
	         }

	         // Cerrar el documento PDF
	         try {
	             document.close();
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	     }
	  
	  public static String extractTablesToJson(PDDocument document) {
	        // Crear un objeto Tabula
	        ObjectExtractor extractor = new ObjectExtractor(document);
	        SpreadsheetExtractionAlgorithm algorithm = new SpreadsheetExtractionAlgorithm();

	        // Crear un objeto JSON para almacenar todas las tablas
	        JsonArray jsonTables = new JsonArray();

	        // Iterar sobre las páginas del PDF
	        PageIterator iterator = extractor.extract();
	        while (iterator.hasNext()) {
	            Page page = iterator.next();

	            // Extraer tablas de la página actual
	            List<Table> tables = algorithm.extract(page);

	            // Convertir las tablas a JSON
	            for (Table table : tables) {
	                JsonObject jsonTable = new JsonObject();
	                JsonArray jsonRows = new JsonArray();

	                // Recorrer cada fila de la tabla
	                for (List<RectangularTextContainer> row : table.getRows()) {
	                    JsonArray jsonCells = new JsonArray();

	                    // Recorrer cada celda de la fila
	                    for (RectangularTextContainer cell : row) {
	                        // Obtener el texto de la celda
	                        String cellText = cell.getText();

	                        // Reemplazar saltos de línea con espacios
	                        cellText = cellText.replace("\n", " ");

	                        // Agregar la celda al JSON
	                        jsonCells.add(cellText);
	                    }

	                    // Agregar la fila al JSON
	                    jsonRows.add(jsonCells);
	                }

	                // Agregar la tabla al JSON
	                jsonTable.add("rows", jsonRows);
	                jsonTables.add(jsonTable);
	            }
	        }

	        // Convertir el JSON a una cadena formateada
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        return gson.toJson(jsonTables);
	    }

    public static void main(String[] args) {
//        String filePath = "C:\\Users\\gcruz\\Downloads\\pdf contancias\\SCG. CONSTANCIA (03-MZO-2025).pdf"; // Cambia esta ruta por la del archivo PDF
        String filePath = "C:\\Users\\gcruz\\Downloads\\pdf contancias\\pdflist\\8. Cédula de cuotas obrero patronales.pdf"; // Cambia esta ruta por la del archivo PDF

        byte[] pdfBytes = new PDFUtils().loadPdfAsBytes(filePath);
        try {
        	
        	
        	 String extractedText = new PDFUtils().extractTextFromRegion(pdfBytes,0, 10, 50, 1000, 100);
        	 System.out.println(extractedText);
            CedulaDeCuotasAlIMSS csf=new CedulaDeCuotasAlIMSS(pdfBytes);
   System.out.println(new Gson().toJson(csf));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
       
     

        
    }
}