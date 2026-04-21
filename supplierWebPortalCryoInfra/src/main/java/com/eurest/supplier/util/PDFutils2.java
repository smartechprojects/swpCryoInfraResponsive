package com.eurest.supplier.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import java.awt.Rectangle;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class PDFutils2 {
	private org.apache.logging.log4j.Logger log4j = org.apache.logging.log4j.LogManager.getLogger(PDFutils2.class);
	public String[] getPdfText_bkp(byte[] input, int area, int pages){
			System.out.println("getPdfText area: " + area + " pages: " + pages);
		PDDocument pdDocument = null;
		try {
		    pdDocument = PDDocument.load(input);		   
		    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
		    stripper.setSortByPosition(true);	
		    
		    if ( area == 5 && pages == 0){
			    int pageNumber = pdDocument.getNumberOfPages();				    			    
			    ArrayList<Integer> validas = new ArrayList<Integer>();
			    ArrayList<Integer> excluir = new ArrayList<Integer>();
			    for (int x=0; x<pageNumber; x++) {
			    	Rectangle cabeceroC = new Rectangle( 10, 10, 800, 183 );
			    	stripper.addRegion("cabecero1", cabeceroC);		    	
			    	Rectangle izquierdaI = new Rectangle( 10, 188, 380, 820 );
			    	stripper.addRegion("izquierda1", izquierdaI);		    	
			    	PDPage page1 = pdDocument.getPage(x);
			    	stripper.extractRegions(page1);
			    	String cabeceroText = "";	
				    cabeceroText = stripper.getTextForRegion("cabecero1");			  
			    	String izquierdaText = "";
			    	izquierdaText = stripper.getTextForRegion("izquierda1");				    
				    if(cabeceroText.contains("SISTEMA ÚNICO DE AUTODETERMINACIÓN")) {
					    if(izquierdaText.contains("Total de Cotizantes:")) {
					    	excluir.add(x);
					    	validas.add(x); // buscar empleados en todas las hojas con encabezado
					    } else {
					    	validas.add(x);
					    }					    
				    } else {
				    	excluir.add(x);
				    }		    
			    }			    		    			    		   
 			    String [] batch = new String[validas.size()];
 		        for (int i = 0; i < validas.size(); i++) {
 		            batch[i] = String.valueOf(validas.get(i));
 		        } 		      				
		    	return batch;   	
		    }
		    
		    Rectangle cabecero = new Rectangle( 10, 10, 800, 183 );		
		    Rectangle izquierda = new Rectangle( 10, 188, 380, 820 );	
		    Rectangle derechoC = new Rectangle( 400, 183, 380, 820 );  
		    stripper.addRegion("cabecero1", cabecero);
		    stripper.addRegion("izquierda1", izquierda);		    
		    stripper.addRegion("derechoC", derechoC);
		    
		    PDPage page = pdDocument.getPage(pages);
		    stripper.extractRegions(page);
		    
		    String cabeceroText = "";	
		    String izquierdaText = "";
		    String derechoCText = "";
		    cabeceroText = stripper.getTextForRegion("cabecero1");
		    izquierdaText = stripper.getTextForRegion("izquierda1");
		    derechoCText = stripper.getTextForRegion("derechoC");
		    
		    if( area == 6 ) {
		    	log4j.info(cabeceroText);
		    	String [] batch = cabeceroText.split("\n");
		    	return batch;
		    } 
		    		    
		    if((cabeceroText.contains("SISTEMA ÚNICO DE AUTODETERMINACIÓN")) && area == 2 ) {
		    	log4j.info(cabeceroText);
		    	String [] batch = cabeceroText.split("\n");
		    	return batch;
		    } 
		    
		    if( area == 3 ) {
		    	log4j.info(izquierdaText);
		    	String [] batch = izquierdaText.split("\n");
		    	return batch;
		    }		    
		    if( area == 4 ) {
		    	log4j.info(derechoCText);
		    	String [] batch = derechoCText.split("\n");
		    	return batch;
		    }   
		    return null;
		} catch (IOException e) {
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
	
	public String[] getPdfText(byte[] input, int area, int pages){
		//System.out.println("getPdfText area: " + area + " pages: " + pages);
	PDDocument pdDocument = null;
	try {
	    pdDocument = PDDocument.load(input);		   
	    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
	    stripper.setSortByPosition(true);	
	    
	    // Detectar versión (solo para saber si agregar región extra)
        String version = detectarVersion(pdDocument);
        //System.out.println("Versión detectada: " + version);
        
        if (version.equals("3.6.5")) {
	    
	    if ( area == 5 && pages == 0){
		    int pageNumber = pdDocument.getNumberOfPages();				    			    
		    ArrayList<Integer> validas = new ArrayList<Integer>();
		    ArrayList<Integer> excluir = new ArrayList<Integer>();
		    for (int x=0; x<pageNumber; x++) {
		    	Rectangle cabeceroC = new Rectangle( 10, 10, 800, 183 );
		    	stripper.addRegion("cabecero1", cabeceroC);		    	
		    	Rectangle izquierdaI = new Rectangle( 10, 188, 380, 820 );
		    	stripper.addRegion("izquierda1", izquierdaI);		    	
		    	PDPage page1 = pdDocument.getPage(x);
		    	stripper.extractRegions(page1);
		    	String cabeceroText = "";	
			    cabeceroText = stripper.getTextForRegion("cabecero1");			  
		    	String izquierdaText = "";
		    	izquierdaText = stripper.getTextForRegion("izquierda1");				    
			    if(cabeceroText.contains("SISTEMA ÚNICO DE AUTODETERMINACIÓN")) {
				    if(izquierdaText.contains("Total de Cotizantes:")) {
				    	excluir.add(x);
				    	validas.add(x); // buscar empleados en todas las hojas con encabezado
				    } else {
				    	validas.add(x);
				    }					    
			    } else {
			    	excluir.add(x);
			    }		    
		    }			    		    			    		   
			    String [] batch = new String[validas.size()];
		        for (int i = 0; i < validas.size(); i++) {
		            batch[i] = String.valueOf(validas.get(i));
		        } 		      				
	    	return batch;   	
	    }
	    
	    Rectangle cabecero = new Rectangle( 10, 10, 800, 183 );		
	    Rectangle izquierda = new Rectangle( 10, 188, 380, 820 );	
	    Rectangle derechoC = new Rectangle( 400, 183, 380, 820 );  
	    stripper.addRegion("cabecero1", cabecero);
	    stripper.addRegion("izquierda1", izquierda);		    
	    stripper.addRegion("derechoC", derechoC);
	    
	    PDPage page = pdDocument.getPage(pages);
	    stripper.extractRegions(page);
	    
	    String cabeceroText = "";	
	    String izquierdaText = "";
	    String derechoCText = "";
	    cabeceroText = stripper.getTextForRegion("cabecero1");
	    izquierdaText = stripper.getTextForRegion("izquierda1");
	    derechoCText = stripper.getTextForRegion("derechoC");
	    
	    if( area == 6 ) {
	    	//log4j.info(cabeceroText);
	    	String [] batch = cabeceroText.split("\n");
	    	return batch;
	    } 
	    		    
	    if((cabeceroText.contains("SISTEMA ÚNICO DE AUTODETERMINACIÓN")) && area == 2 ) {
	    	//log4j.info(cabeceroText);
	    	String [] batch = cabeceroText.split("\n");
	    	return batch;
	    } 
	    
	    if( area == 3 ) {
	    	//log4j.info(izquierdaText);
	    	String [] batch = izquierdaText.split("\n");
	    	return batch;
	    }		    
	    if( area == 4 ) {
	    	//log4j.info(derechoCText);
	    	String [] batch = derechoCText.split("\n");
	    	return batch;
	    }   
        }else {
            // ==================== VERSIÓN 3.7.0 ====================
            
            if (area == 5 && pages == 0) {
                int pageNumber = pdDocument.getNumberOfPages();
                ArrayList<Integer> validas = new ArrayList<Integer>();
                for (int x = 0; x < pageNumber; x++) {
                    Rectangle cabeceroC = new Rectangle(10, 10, 800, 200);
                    stripper.addRegion("cabecero1", cabeceroC);
                    Rectangle izquierdaI = new Rectangle(10, 188, 380, 820);
                    stripper.addRegion("izquierda1", izquierdaI);
                    
                    PDPage page1 = pdDocument.getPage(x);
                    stripper.extractRegions(page1);
                    String cabeceroText = stripper.getTextForRegion("cabecero1");
                    String izquierdaText = stripper.getTextForRegion("izquierda1");
                    
                    if (cabeceroText.contains("SISTEMA ÚNICO DE AUTODETERMINACIÓN") || 
                        izquierdaText.contains("-")) {
                        validas.add(x);
                    }
                }
              //  System.out.println("Páginas válidas 3.7.0: " + validas);
                String[] batch = new String[validas.size()];
                for (int i = 0; i < validas.size(); i++) {
                    batch[i] = String.valueOf(validas.get(i));
                }
                return batch;
            }
            
            // Regiones para extracción en 3.7.0
            Rectangle cabecero = new Rectangle(10, 10, 800, 200);
            Rectangle izquierda = new Rectangle(10, 188, 380, 820);
            Rectangle derechoC = new Rectangle(400, 183, 380, 820);
            
            stripper.addRegion("cabecero1", cabecero);
            stripper.addRegion("izquierda1", izquierda);
            stripper.addRegion("derechoC", derechoC);
            
            PDPage page = pdDocument.getPage(pages);
            stripper.extractRegions(page);
            
            String cabeceroText = stripper.getTextForRegion("cabecero1");
            String izquierdaText = stripper.getTextForRegion("izquierda1");
            String derechoCText = stripper.getTextForRegion("derechoC");
            
            if (area == 2) {
                //log4j.info(cabeceroText);
                String[] batch = cabeceroText.split("\n");
                return batch;
            }
            
            if (area == 3) {
                // UNIFICAR: Tomar datos de cabecero Y izquierda para formar la lista completa de empleados
                StringBuilder empleadosUnificados = new StringBuilder();
                
             // 1. Buscar en cabecero (primer empleado de cada página)
                if (cabeceroText != null && !cabeceroText.isEmpty()) {
                    String[] lineasCabecero = cabeceroText.split("\n");
                    for (String linea : lineasCabecero) {
                        if (linea.contains("-") && org.apache.commons.lang3.StringUtils.countMatches(linea, "-") >= 4) {
                            // Limpiar espacios para buscar el CURP completo
                            String lineaLimpia = linea.replaceAll("\\s", "");
                            java.util.regex.Pattern curpPattern = java.util.regex.Pattern.compile("[A-Z]{4}\\d{6}[A-Z0-9]{6}");
                            java.util.regex.Matcher curpMatcher = curpPattern.matcher(lineaLimpia);
                            
                            if (curpMatcher.find()) {
                                String curpCompleto = curpMatcher.group();
                                // Remover el CURP de la línea original
                                String inicioCurp = curpCompleto.substring(0, 4);
                                int curpStartIdx = linea.indexOf(inicioCurp);
                                if (curpStartIdx >= 0) {
                                    String antesDelCurp = linea.substring(0, curpStartIdx);
                                    String despuesDelCurp = linea.substring(curpStartIdx + curpCompleto.length()).trim();
                                    
                                    // Eliminar residuos al final
                                    // 1. Eliminar números y texto después (ej: "05 MACEDONIA Q")
                                    despuesDelCurp = despuesDelCurp.replaceAll("^\\d+\\s+[A-ZÁÉÍÓÚÑ]+(\\s+[A-ZÁÉÍÓÚÑ]+)*$", "");
                                    // 2. Eliminar códigos de 2 caracteres (ej: "A9")
                                    despuesDelCurp = despuesDelCurp.replaceAll("^[A-Z0-9]{2}$", "");
                                    despuesDelCurp = despuesDelCurp.replaceAll("\\s+[A-Z0-9]{2}$", "");
                                    // 3. Eliminar números sueltos
                                    despuesDelCurp = despuesDelCurp.replaceAll("^\\d+$", "");
                                    despuesDelCurp = despuesDelCurp.replaceAll("\\s+\\d+$", "");
                                    
                                    String lineaProcesada = (antesDelCurp + (despuesDelCurp.isEmpty() ? "" : " " + despuesDelCurp)).trim();
                                    empleadosUnificados.append(lineaProcesada).append("\n");
                                } else {
                                    empleadosUnificados.append(linea).append("\n");
                                }
                            } else {
                                // No es CURP completo - puede ser RFC sin homoclave (ej: BEAS-711111-)
                                // Remover el RFC del final antes de usarlo como nombre
                                String lineaLimpiada = linea.replaceAll("\\s+[A-Z]{4}-\\d{6}-\\s*$", "").trim();
                                empleadosUnificados.append(lineaLimpiada).append("\n");
                            }
                        }
                    }
                }
                
                
                // 2. Agregar datos de izquierda (resto de empleados) - MISMA LÓGICA
                if (izquierdaText != null && !izquierdaText.isEmpty()) {
                    String[] lineasIzquierda = izquierdaText.split("\n");
                    for (String linea : lineasIzquierda) {
                        if (linea.trim().isEmpty()) continue;
                        
                        // Limpiar espacios para buscar el CURP completo
                        String lineaLimpia = linea.replaceAll("\\s", "");
                        java.util.regex.Pattern curpPattern = java.util.regex.Pattern.compile("[A-Z]{4}\\d{6}[A-Z0-9]{6}");
                        java.util.regex.Matcher curpMatcher = curpPattern.matcher(lineaLimpia);
                        
                        if (curpMatcher.find()) {
                            String curpCompleto = curpMatcher.group();
                            // Remover el CURP de la línea original
                            String inicioCurp = curpCompleto.substring(0, 4);
                            int curpStartIdx = linea.indexOf(inicioCurp);
                            if (curpStartIdx >= 0) {
                                String antesDelCurp = linea.substring(0, curpStartIdx);
                                String despuesDelCurp = linea.substring(curpStartIdx + curpCompleto.length()).trim();
                                // Eliminar residuos al final
                                despuesDelCurp = despuesDelCurp.replaceAll("\\s+[A-Z0-9]{2}$", "");
                                despuesDelCurp = despuesDelCurp.replaceAll("\\s+\\d+$", "");
                                String lineaProcesada = (antesDelCurp + (despuesDelCurp.isEmpty() ? "" : " " + despuesDelCurp)).trim();
                                empleadosUnificados.append(lineaProcesada).append("\n");
                            } else {
                                empleadosUnificados.append(linea).append("\n");
                            }
                        } else {
                            empleadosUnificados.append(linea).append("\n");
                        }
                    }
                }
                
                String textoFinal = empleadosUnificados.toString();
               // log4j.info(textoFinal);
                String[] batch = textoFinal.split("\n");
                
                // Filtrar líneas vacías
                List<String> listaFiltrada = new ArrayList<String>();
                for (String s : batch) {
                    if (s != null && !s.trim().isEmpty()) {
                        listaFiltrada.add(s.trim());
                    }
                }
                
                return listaFiltrada.toArray(new String[0]);
            }
            
            if (area == 4) {
                //System.out.println("=== DEBUG area=4 ===");
                
                List<String> listaLineas = new ArrayList<String>();
                
                // Patrón para CURP (18 caracteres exactos)
               // java.util.regex.Pattern curpPattern = java.util.regex.Pattern.compile("[A-Z]{4}\\d{6}[A-Z0-9]{6}");
                
             // 1. Buscar en cabeceroText (primer empleado de cada página) - SOLO EL PRIMERO
                if (cabeceroText != null && !cabeceroText.isEmpty()) {
                    String[] lineas = cabeceroText.split("\n");
                    boolean primerEmpleadoEncontrado = false;
                    for (String linea : lineas) {
                        if (primerEmpleadoEncontrado) break;
                                                
                        // Buscar una línea que contenga NSS (formato con guiones)
                        if (linea.contains("-") && org.apache.commons.lang3.StringUtils.countMatches(linea, "-") >= 4) {
                            // Buscar el inicio del CURP/RFC: 4 letras mayúsculas, opcionalmente un guión, seguidas de 6 dígitos
                        	//java.util.regex.Pattern inicioCurpPattern = java.util.regex.Pattern.compile("[A-Z]{4}\\d{6}");
                            java.util.regex.Pattern inicioCurpPattern = java.util.regex.Pattern.compile("[A-Z]{4}-?\\d{6}");
                            java.util.regex.Matcher inicioMatcher = inicioCurpPattern.matcher(linea);
                            
                            if (inicioMatcher.find()) {
                                int startIdx = inicioMatcher.start();
                                // El CURP tiene 18 caracteres, tomamos desde startIdx hasta el final y limpiamos espacios
                                String posibleCurpConEspacios = linea.substring(startIdx);
                                // Eliminamos espacios para obtener el CURP completo
                                String curpCompleto = posibleCurpConEspacios.replaceAll("\\s", "");
                                // Aseguramos que tenga al menos 18 caracteres
                                if (curpCompleto.length() >= 18) {
                                    curpCompleto = curpCompleto.substring(0, 18);
                                    
                                    // Extraer ubicación después del CURP en la línea original
                                    String ubicacion = "";
                                    if (startIdx + curpCompleto.length() < linea.length()) {
                                        ubicacion = linea.substring(startIdx + curpCompleto.length()).trim();
                                    }
                                    if (ubicacion.matches("\\d+")) {
                                        ubicacion = "";
                                    }
                                    String lineaCompleta = curpCompleto + (ubicacion.isEmpty() ? "" : " " + ubicacion);
                    //                System.out.println("CURP en cabecero (primer empleado): [" + lineaCompleta + "]");
                                    listaLineas.add(lineaCompleta);
                                    primerEmpleadoEncontrado = true;
                                }else {
                                	if(curpCompleto.contains("-")) {
                                		listaLineas.add(curpCompleto);
                                        primerEmpleadoEncontrado = true;
                                	}
                                }
                            }
                        }
                    }
                }
                
                // 2. Buscar en derechoCText (resto de empleados)
                if (derechoCText != null && !derechoCText.isEmpty()) {
                    String[] lineas = derechoCText.split("\n");
                    for (String linea : lineas) {
                        linea = linea.trim();
                        if (linea.isEmpty()) continue;
                        
                        // Buscar inicio de CURP (4 letras mayúsculas)
                        java.util.regex.Pattern inicioCurpPattern = java.util.regex.Pattern.compile("[A-Z]{4}");
                        java.util.regex.Matcher inicioMatcher = inicioCurpPattern.matcher(linea);
                        if (inicioMatcher.find()) {
                            int startIdx = inicioMatcher.start();
                            // Tomar los siguientes 18 caracteres, pero pueden tener espacios
                            String posibleCurp = linea.substring(startIdx);
                            
                            //Valida que no tenga guion
                            if(!posibleCurp.contains("-")) {
                            
                            // Eliminar espacios
                            String curpLimpio = posibleCurp.replaceAll("\\s", "").substring(0, 18);
                            
                            // Extraer ubicación después del CURP
                            String resto = "";
                            int curpEndIdx = startIdx + curpLimpio.length();
                            if (curpEndIdx < linea.length()) {
                                resto = linea.substring(curpEndIdx).trim();
                            }
                            // Si el resto son solo números, no es ubicación
                            if (resto.matches("\\d+")) {
                                resto = "";
                            }
                            String lineaCompleta = curpLimpio + (resto.isEmpty() ? "" : " " + resto);
                      //      System.out.println("Línea completa en derechoCText: [" + lineaCompleta + "]");
                            listaLineas.add(lineaCompleta);
                        }else {
                        	// RFC con guiones: quitar dígitos sueltos al final (campo "Lic.", ej: FALD-781111-3)
                        	listaLineas.add(posibleCurp.trim().replaceAll("\\s*\\d+$", ""));
                        }
                      }
                    }
                }
                
                //System.out.println("Líneas encontradas en área 4: " + listaLineas.size());
                
                StringBuilder resultado = new StringBuilder();
                for (String l : listaLineas) {
                    resultado.append(l).append("\n");
                }
                
                //log4j.info("Líneas encontradas: " + resultado.toString());
                String[] batch = resultado.toString().split("\n");
                List<String> listaFinal = new ArrayList<String>();
                for (String s : batch) {
                    if (s != null && !s.trim().isEmpty()) {
                        listaFinal.add(s.trim());
                    }
                }
                return listaFinal.toArray(new String[0]);
            }
            return null;
        }
	    return null;
	} catch (IOException e) {
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
	
		 
		 public String detectarVersion(PDDocument pdDocument) {
			    try {
			        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			        stripper.setSortByPosition(true);
			        
			        // Ampliar el área de búsqueda para capturar "V 3.7.0"
			        Rectangle versionArea = new Rectangle(10, 10, 800, 250);
			        stripper.addRegion("versionArea", versionArea);
			        
			        PDPage page = pdDocument.getPage(0);
			        stripper.extractRegions(page);
			        String versionText = stripper.getTextForRegion("versionArea");
			        
			        if (versionText != null && versionText.contains("V 3.7.0")) {
			            return "3.7.0";
			        } else if (versionText != null && versionText.contains("V 3.6.5")) {
			            return "3.6.5";
			        }
			    } catch (IOException e) {
			        log4j.error("Error detectando versión", e);
			    }
			    return "3.6.5";
			}

}