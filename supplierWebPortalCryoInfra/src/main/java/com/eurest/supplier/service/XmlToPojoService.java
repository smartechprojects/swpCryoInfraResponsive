package com.eurest.supplier.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.eurest.supplier.dto.InvoiceDTO;
import com.eurest.supplier.dto.ResponseGeneral;
import com.eurest.supplier.invoiceXml.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Service("xmlToPojoService")
public class XmlToPojoService {
	
	Logger log4j = Logger.getLogger(XmlToPojoService.class);
	
	public InvoiceDTO convert(String xml){
		
		Comprobante c = new Comprobante();
		InvoiceDTO inv = new InvoiceDTO();
		try {
			
			String source = takeOffBOM(IOUtils.toInputStream(xml,"UTF-8"));
			String xmlString = source.replace("?<?xml", "<?xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Comprobante.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xmlString);
			c = (Comprobante) unmarshaller.unmarshal(reader);
			
			inv.setVersion(c.getVersion());
			inv.setEmisor(c.getEmisor());
			inv.setReceptor(c.getReceptor());			
			inv.setFolio(c.getFolio());
			inv.setSerie(c.getSerie() + "");
			inv.setTotal(Double.parseDouble(c.getTotal()));
			inv.setSubTotal(Double.parseDouble(c.getSubTotal()));
			if(c.getDescuento() != null)
			       inv.setDescuento(Double.parseDouble(c.getDescuento()));
			
			if(c.getTipoCambio() != null)
				inv.setTipoCambio(Double.parseDouble(c.getTipoCambio()));
			
			inv.setTipoComprobante(c.getTipoDeComprobante());
			inv.setMoneda(c.getMoneda());
			inv.setMetodoPago(c.getMetodoPago());
			inv.setFormaPago(c.getFormaPago());
			inv.setRfcEmisor(c.getEmisor().getRfc());
			inv.setRfcReceptor(c.getReceptor().getRfc());
			inv.setComplemento(c.getComplemento());
			if(c.getCfdiRelacionados() != null) {
				if(c.getCfdiRelacionados().getCfdiRelacionado()!= null) {
					inv.setCfdiRelacionado(c.getCfdiRelacionados().getCfdiRelacionado().getCfdiUUID());
				}
			}
			
			if(c.getImpuestos() != null) {
				if(c.getImpuestos().getTotalImpuestosTrasladados() != null) {
					inv.setTotalImpuestos(Double.parseDouble(c.getImpuestos().getTotalImpuestosTrasladados()));
				} else {
					inv.setTotalImpuestos(0);
				}
								
				if(c.getImpuestos().getTotalImpuestosRetenidos() != null) {
					inv.setTotalRetenidos(Double.parseDouble(c.getImpuestos().getTotalImpuestosRetenidos()));
				} else {
					inv.setTotalRetenidos(0);
				}
			} else {
				inv.setTotalImpuestos(0);
				inv.setTotalRetenidos(0);
			}				
				
			if(c.getComplemento() != null){
				inv.setUuid(c.getComplemento().getTimbreFiscalDigital().getUUID());
				inv.setFechaTimbrado(c.getComplemento().getTimbreFiscalDigital().getFechaTimbrado());
			}

			inv.setFecha(c.getFecha());
			inv.setConcepto(c.getConceptos().getConcepto());
			if(c.getImpuestos() != null){
				if(c.getImpuestos().getTotalImpuestosTrasladados() != null) {
					inv.setImpuestos(Double.parseDouble(c.getImpuestos().getTotalImpuestosTrasladados()));
				} else {
					inv.setImpuestos(0);
				}				
			}
			
			inv.setSello(c.getSello());
			inv.setCertificado(c.getCertificado());
			inv.setLugarExpedicion(c.getLugarExpedicion());
			
			
			
			ResponseGeneral responseG=new ResponseGeneral(false, "XmlToPojoService_convert");
			responseG.addMensaje("es", "La factura " + inv.getFolio() + " ha sido validada de forma exitosa.");
			responseG.addMensaje("en", "The invoice " + inv.getFolio() + " has been successfully validated.");
			inv.setResponse(responseG);
			return inv;
			
		} catch (JAXBException e) {
			log4j.error("Exception" , e);
			ResponseGeneral responseG=new ResponseGeneral(true, "XmlToPojoService_convert",e);
			responseG.addMensaje("es", "La factura tiene errores de validación. Verifique que el archivo cargadose a una factura válida respecto a la versión SAT 3.3");
			responseG.addMensaje("en", "The invoice has validation errors. Verify that the file was uploaded to a valid invoice regarding the version SAT 3.3");
			inv.setResponse(responseG);
			e.printStackTrace();
			log4j.error(new Gson().toJson(responseG));
			log4j.error("Factura: "+xml);
			
			return inv;
		}catch(Exception e){
			log4j.error("Exception" , e);
			ResponseGeneral responseG=new ResponseGeneral(true, "XmlToPojoService_convert",e);
			responseG.addMensaje("es", "La factura tiene errores de validación. Verifique que el archivo cargadose a una factura válida respecto a la versión SAT 3.3");
			responseG.addMensaje("en", "The invoice has validation errors. Verify that the file was uploaded to a valid invoice regarding the version SAT 3.3");
			inv.setResponse(responseG);
				e.printStackTrace();
		log4j.error(new Gson().toJson(responseG));
		log4j.error("Factura: "+xml);
				return inv;
		}
	}
	
	public InvoiceDTO convertV4(String xml){
		//Comprobante V4.0 
		com.eurest.supplier.invoiceXml4.Comprobante c4 = new com.eurest.supplier.invoiceXml4.Comprobante();
		Comprobante c = new Comprobante();		
		InvoiceDTO inv = new InvoiceDTO();
		
		try {

			String source = takeOffBOM(IOUtils.toInputStream(xml,"UTF-8"));
			String xmlString = source.replace("?<?xml", "<?xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(com.eurest.supplier.invoiceXml4.Comprobante.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xmlString);
			c4 = (com.eurest.supplier.invoiceXml4.Comprobante) unmarshaller.unmarshal(reader);			
			c = objectCastV4(c4);
			
			inv.setVersion(c.getVersion());
			inv.setEmisor(c.getEmisor());
			inv.setReceptor(c.getReceptor());			
			inv.setFolio(c.getFolio());
			inv.setSerie(c.getSerie() + "");
			inv.setTotal(Double.parseDouble(c.getTotal()));
			inv.setSubTotal(Double.parseDouble(c.getSubTotal()));
			if(c.getDescuento() != null)
			       inv.setDescuento(Double.parseDouble(c.getDescuento()));
			
			if(c.getTipoCambio() != null)
				inv.setTipoCambio(Double.parseDouble(c.getTipoCambio()));
			
			inv.setTipoComprobante(c.getTipoDeComprobante());
			inv.setMoneda(c.getMoneda());
			inv.setMetodoPago(c.getMetodoPago());
			inv.setFormaPago(c.getFormaPago());
			inv.setRfcEmisor(c.getEmisor().getRfc());
			inv.setRfcReceptor(c.getReceptor().getRfc());
			inv.setComplemento(c.getComplemento());
			if(c.getCfdiRelacionados() != null) {
				if(c.getCfdiRelacionados().getCfdiRelacionado()!= null) {
					inv.setCfdiRelacionado(c.getCfdiRelacionados().getCfdiRelacionado().getCfdiUUID());
				}
			}
			
			if(c.getImpuestos() != null) {
				if(c.getImpuestos().getTotalImpuestosTrasladados() != null) {
					inv.setTotalImpuestos(Double.parseDouble(c.getImpuestos().getTotalImpuestosTrasladados()));
				} else {
					inv.setTotalImpuestos(0);
				}
								
				if(c.getImpuestos().getTotalImpuestosRetenidos() != null) {
					inv.setTotalRetenidos(Double.parseDouble(c.getImpuestos().getTotalImpuestosRetenidos()));
				} else {
					inv.setTotalRetenidos(0);
				}
			} else {
				inv.setTotalImpuestos(0);
				inv.setTotalRetenidos(0);
			}				
				
			if(c.getComplemento() != null){
				inv.setUuid(c.getComplemento().getTimbreFiscalDigital().getUUID());
				inv.setFechaTimbrado(c.getComplemento().getTimbreFiscalDigital().getFechaTimbrado());
			}

			inv.setFecha(c.getFecha());
			inv.setConcepto(c.getConceptos().getConcepto());
			if(c.getImpuestos() != null){
				if(c.getImpuestos().getTotalImpuestosTrasladados() != null) {
					inv.setImpuestos(Double.parseDouble(c.getImpuestos().getTotalImpuestosTrasladados()));
				} else {
					inv.setImpuestos(0);
				}				
			}
			
			inv.setSello(c.getSello());
			inv.setCertificado(c.getCertificado());
			inv.setLugarExpedicion(c.getLugarExpedicion());
			
			//Campos nuevos V4.0
			inv.setDomicilioFiscalReceptor(c4.getReceptor().getDomicilioFiscalReceptor());
			inv.setRegimenFiscalReceptor(c4.getReceptor().getRegimenFiscalReceptor());			
			ResponseGeneral responseG=new ResponseGeneral(false, "XmlToPojoService_convert");
			responseG.addMensaje("es", "La factura " + inv.getFolio() + " ha sido validada de forma exitosa.");
			responseG.addMensaje("en", "The invoice " + inv.getFolio() + " has been successfully validated.");
			inv.setResponse(responseG);
			return inv;
			
		} catch (JAXBException e) {
			log4j.error("Exception" , e);
			ResponseGeneral responseG=new ResponseGeneral(true, "XmlToPojoService_convert",e);
			responseG.addMensaje("es", "La factura tiene errores de validación. Verifique que el archivo cargadose a una factura válida respecto a la versión SAT 4.0");
			responseG.addMensaje("en", "The invoice has validation errors. Verify that the file was uploaded to a valid invoice regarding the version SAT 4.0");
			inv.setResponse(responseG);
			e.printStackTrace();
			return inv;
			
		}catch(Exception e){
			log4j.error("Exception" , e);
			ResponseGeneral responseG=new ResponseGeneral(true, "XmlToPojoService_convert",e);
			responseG.addMensaje("es", "La factura tiene errores de validación. Verifique que el archivo cargadose a una factura válida respecto a la versión SAT 4.0");
			responseG.addMensaje("en", "The invoice has validation errors. Verify that the file was uploaded to a valid invoice regarding the version SAT 4.0");
			inv.setResponse(responseG);
			e.printStackTrace();
			return inv;
		}
	}
	
	public static String takeOffBOM(InputStream inputStream) throws IOException {
	    BOMInputStream bomInputStream = new BOMInputStream(inputStream);
	    String res=IOUtils.toString(bomInputStream, "UTF-8");
	    		res=res.replace('�',' ');
	    return res.trim();
	}

	public Comprobante objectCastV4(com.eurest.supplier.invoiceXml4.Comprobante c4) {
		
		Comprobante c = null;		
		try {
			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.setSerializationInclusion(Include.NON_NULL);
			String jsonInString = jsonMapper.writeValueAsString(c4);
			Gson gson = new Gson();
			c =  gson.fromJson(jsonInString, Comprobante.class);
			
			//Variables que no se obtienen con Gson (seteo manual)
			if(c4.getComplementos() != null && !c4.getComplementos().isEmpty()) {
				c.setComplemento(new Complemento());
				
				for(com.eurest.supplier.invoiceXml4.Complemento complemento : c4.getComplementos()) {
					
					//Timbre Fiscal Digital
					if(complemento.getTimbreFiscalDigital() != null) {
						TimbreFiscalDigital timbreFiscal = (TimbreFiscalDigital)objectCastGson(complemento.getTimbreFiscalDigital(), new TimbreFiscalDigital());
						c.getComplemento().setTimbreFiscalDigital(timbreFiscal);
						c.getComplemento().getTimbreFiscalDigital().setUUID(complemento.getTimbreFiscalDigital().getUUID());//No lo obtiene al convertirlo
					}
					
					//Complemento de Pago V2.0
					if(complemento.getPago() != null) {
						Pagos pagos = (Pagos)objectCastGson(complemento.getPago(), new Pagos());
						c.getComplemento().setPago(pagos);
					}
					
					//Impuestos Locales
					if(complemento.getImpuestosLocales() != null) {
						ImpuestosLocales impuestosLocales = (ImpuestosLocales)objectCastGson(complemento.getImpuestosLocales(), new ImpuestosLocales());
						c.getComplemento().setImpuestosLocales(impuestosLocales);
					}
				}
			}
			
			if(c4.getCfdiRelacionados() != null && c4.getCfdiRelacionados().getCfdiRelacionado()!= null) {
				c.getCfdiRelacionados().getCfdiRelacionado().setUUID(c4.getCfdiRelacionados().getCfdiRelacionado().getCfdiUUID());
			}
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			c = null;
		}
		return c;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T objectCastGson(T originalObject, T targetClass) {
		
		Object o = null;		
		try {
			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.setSerializationInclusion(Include.NON_NULL);
			String jsonInString = jsonMapper.writeValueAsString(originalObject);
			Gson gson = new Gson();
			o =  gson.fromJson(jsonInString, targetClass.getClass());
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			o = null;
		}
		return (T)o;
	}
}
