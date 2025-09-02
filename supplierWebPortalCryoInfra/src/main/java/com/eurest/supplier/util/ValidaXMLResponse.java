//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2019.06.10 a las 07:19:31 PM CDT 
//


package com.eurest.supplier.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ValidaXMLResult" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "validaXMLResult"
})
@XmlRootElement(name = "ValidaXMLResponse")
public class ValidaXMLResponse {

    @XmlElement(name = "ValidaXMLResult", required = true)
    protected String validaXMLResult;

    /**
     * Obtiene el valor de la propiedad validaXMLResult.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidaXMLResult() {
        return validaXMLResult;
    }

    /**
     * Define el valor de la propiedad validaXMLResult.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidaXMLResult(String value) {
        this.validaXMLResult = value;
    }

}
