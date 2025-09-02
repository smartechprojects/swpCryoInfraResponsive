package com.eurest.supplier.util;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import java.awt.Rectangle;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class PDFutils2 {
	private org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(PDFutils2.class);
	public String[] getPdfText(byte[] input, int area, int pages){
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
}