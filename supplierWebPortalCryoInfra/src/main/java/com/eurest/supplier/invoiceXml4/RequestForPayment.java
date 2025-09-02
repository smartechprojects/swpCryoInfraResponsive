//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.10.06 a las 08:55:35 AM CDT 
//


package com.eurest.supplier.invoiceXml4;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

public class RequestForPayment {


    protected RequestForPayment.RequestForPaymentIdentification requestForPaymentIdentification;

    protected RequestForPayment.OrderIdentification orderIdentification;

    protected RequestForPayment.AdditionalInformation additionalInformation;

    protected RequestForPayment.Buyer buyer;

    protected RequestForPayment.Seller seller;

    protected RequestForPayment.Currency currency;
    protected List<RequestForPayment.LineItem> lineItem;

    protected RequestForPayment.BaseAmount baseAmount;
    protected List<RequestForPayment.Tax> tax;

    protected RequestForPayment.PayableAmount payableAmount;

    protected String type;

    protected String contentVersion;

    protected String documentStructureVersion;

    protected String documentStatus;

    protected Integer deliveryDate;

    /**
     * Obtiene el valor de la propiedad requestForPaymentIdentification.
     * 
     * @return
     *     possible object is
     *     {@link RequestForPayment.RequestForPaymentIdentification }
     *     
     */
    @XmlElement(name = "requestForPaymentIdentification")
    public RequestForPayment.RequestForPaymentIdentification getRequestForPaymentIdentification() {
        return requestForPaymentIdentification;
    }

    /**
     * Define el valor de la propiedad requestForPaymentIdentification.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestForPayment.RequestForPaymentIdentification }
     *     
     */
    public void setRequestForPaymentIdentification(RequestForPayment.RequestForPaymentIdentification value) {
        this.requestForPaymentIdentification = value;
    }

    /**
     * Obtiene el valor de la propiedad orderIdentification.
     * 
     * @return
     *     possible object is
     *     {@link RequestForPayment.OrderIdentification }
     *     
     */
    @XmlElement(name = "orderIdentification")
    public RequestForPayment.OrderIdentification getOrderIdentification() {
        return orderIdentification;
    }

    /**
     * Define el valor de la propiedad orderIdentification.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestForPayment.OrderIdentification }
     *     
     */
    public void setOrderIdentification(RequestForPayment.OrderIdentification value) {
        this.orderIdentification = value;
    }

    /**
     * Obtiene el valor de la propiedad additionalInformation.
     * 
     * @return
     *     possible object is
     *     {@link RequestForPayment.AdditionalInformation }
     *     
     */
    public RequestForPayment.AdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * Define el valor de la propiedad additionalInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestForPayment.AdditionalInformation }
     *     
     */
    @XmlElement(name = "AdditionalInformation")
    public void setAdditionalInformation(RequestForPayment.AdditionalInformation value) {
        this.additionalInformation = value;
    }

    /**
     * Obtiene el valor de la propiedad buyer.
     * 
     * @return
     *     possible object is
     *     {@link RequestForPayment.Buyer }
     *     
     */
    @XmlElement(name = "buyer")
    public RequestForPayment.Buyer getBuyer() {
        return buyer;
    }

    /**
     * Define el valor de la propiedad buyer.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestForPayment.Buyer }
     *     
     */
    public void setBuyer(RequestForPayment.Buyer value) {
        this.buyer = value;
    }

    /**
     * Obtiene el valor de la propiedad seller.
     * 
     * @return
     *     possible object is
     *     {@link RequestForPayment.Seller }
     *     
     */
    @XmlElement(name = "seller")
    public RequestForPayment.Seller getSeller() {
        return seller;
    }

    /**
     * Define el valor de la propiedad seller.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestForPayment.Seller }
     *     
     */
    public void setSeller(RequestForPayment.Seller value) {
        this.seller = value;
    }

    /**
     * Obtiene el valor de la propiedad currency.
     * 
     * @return
     *     possible object is
     *     {@link RequestForPayment.Currency }
     *     
     */
    @XmlElement(name = "currency")
    public RequestForPayment.Currency getCurrency() {
        return currency;
    }

    /**
     * Define el valor de la propiedad currency.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestForPayment.Currency }
     *     
     */
    public void setCurrency(RequestForPayment.Currency value) {
        this.currency = value;
    }

    /**
     * Gets the value of the lineItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lineItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLineItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RequestForPayment.LineItem }
     * 
     * 
     */
    public List<RequestForPayment.LineItem> getLineItem() {
        if (lineItem == null) {
            lineItem = new ArrayList<RequestForPayment.LineItem>();
        }
        return this.lineItem;
    }

    /**
     * Obtiene el valor de la propiedad baseAmount.
     * 
     * @return
     *     possible object is
     *     {@link RequestForPayment.BaseAmount }
     *     
     */
    @XmlElement(name = "baseAmount")
    public RequestForPayment.BaseAmount getBaseAmount() {
        return baseAmount;
    }

    /**
     * Define el valor de la propiedad baseAmount.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestForPayment.BaseAmount }
     *     
     */
    public void setBaseAmount(RequestForPayment.BaseAmount value) {
        this.baseAmount = value;
    }

    /**
     * Gets the value of the tax property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tax property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTax().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RequestForPayment.Tax }
     * 
     * 
     */
    public List<RequestForPayment.Tax> getTax() {
        if (tax == null) {
            tax = new ArrayList<RequestForPayment.Tax>();
        }
        return this.tax;
    }

    /**
     * Obtiene el valor de la propiedad payableAmount.
     * 
     * @return
     *     possible object is
     *     {@link RequestForPayment.PayableAmount }
     *     
     */
    @XmlElement(name = "payableAmount")
    public RequestForPayment.PayableAmount getPayableAmount() {
        return payableAmount;
    }

    /**
     * Define el valor de la propiedad payableAmount.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestForPayment.PayableAmount }
     *     
     */
    public void setPayableAmount(RequestForPayment.PayableAmount value) {
        this.payableAmount = value;
    }

    /**
     * Obtiene el valor de la propiedad type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlAttribute(name = "type")
    public String getType() {
        return type;
    }

    /**
     * Define el valor de la propiedad type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Obtiene el valor de la propiedad contentVersion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlAttribute(name = "contentVersion")
    public String getContentVersion() {
        return contentVersion;
    }

    /**
     * Define el valor de la propiedad contentVersion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentVersion(String value) {
        this.contentVersion = value;
    }

    /**
     * Obtiene el valor de la propiedad documentStructureVersion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlAttribute(name = "documentStructureVersion")
    public String getDocumentStructureVersion() {
        return documentStructureVersion;
    }

    /**
     * Define el valor de la propiedad documentStructureVersion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentStructureVersion(String value) {
        this.documentStructureVersion = value;
    }

    /**
     * Obtiene el valor de la propiedad documentStatus.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlAttribute(name = "documentStatus")
    public String getDocumentStatus() {
        return documentStatus;
    }

    /**
     * Define el valor de la propiedad documentStatus.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentStatus(String value) {
        this.documentStatus = value;
    }

    /**
     * Obtiene el valor de la propiedad deliveryDate.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    @XmlAttribute(name = "DeliveryDate")
    public Integer getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Define el valor de la propiedad deliveryDate.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDeliveryDate(Integer value) {
        this.deliveryDate = value;
    }


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
     *         &lt;element name="referenceIdentification">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>int">
     *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "referenceIdentification"
    })
    public static class AdditionalInformation {

        @XmlElement(required = true)
        protected RequestForPayment.AdditionalInformation.ReferenceIdentification referenceIdentification;

        /**
         * Obtiene el valor de la propiedad referenceIdentification.
         * 
         * @return
         *     possible object is
         *     {@link RequestForPayment.AdditionalInformation.ReferenceIdentification }
         *     
         */
        public RequestForPayment.AdditionalInformation.ReferenceIdentification getReferenceIdentification() {
            return referenceIdentification;
        }

        /**
         * Define el valor de la propiedad referenceIdentification.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestForPayment.AdditionalInformation.ReferenceIdentification }
         *     
         */
        public void setReferenceIdentification(RequestForPayment.AdditionalInformation.ReferenceIdentification value) {
            this.referenceIdentification = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>int">
         *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class ReferenceIdentification {

            @XmlValue
            protected int value;
            @XmlAttribute(name = "type")
            protected String type;

            /**
             * Obtiene el valor de la propiedad value.
             * 
             */
            public int getValue() {
                return value;
            }

            /**
             * Define el valor de la propiedad value.
             * 
             */
            public void setValue(int value) {
                this.value = value;
            }

            /**
             * Obtiene el valor de la propiedad type.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getType() {
                return type;
            }

            /**
             * Define el valor de la propiedad type.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setType(String value) {
                this.type = value;
            }

        }

    }


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
     *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
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
        "amount"
    })
    public static class BaseAmount {

        @XmlElement(name = "Amount")
        protected float amount;

        /**
         * Obtiene el valor de la propiedad amount.
         * 
         */
        public float getAmount() {
            return amount;
        }

        /**
         * Define el valor de la propiedad amount.
         * 
         */
        public void setAmount(float value) {
            this.amount = value;
        }

    }


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
     *         &lt;element name="gln" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
        "gln"
    })
    public static class Buyer {

        protected long gln;

        /**
         * Obtiene el valor de la propiedad gln.
         * 
         */
        public long getGln() {
            return gln;
        }

        /**
         * Define el valor de la propiedad gln.
         * 
         */
        public void setGln(long value) {
            this.gln = value;
        }

    }


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
     *         &lt;element name="currencyFunction" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="rateOfChange" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *       &lt;/sequence>
     *       &lt;attribute name="currencyISOCode" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "currencyFunction",
        "rateOfChange"
    })
    public static class Currency {

        @XmlElement(required = true)
        protected String currencyFunction;
        protected float rateOfChange;
        @XmlAttribute(name = "currencyISOCode")
        protected String currencyISOCode;

        /**
         * Obtiene el valor de la propiedad currencyFunction.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCurrencyFunction() {
            return currencyFunction;
        }

        /**
         * Define el valor de la propiedad currencyFunction.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCurrencyFunction(String value) {
            this.currencyFunction = value;
        }

        /**
         * Obtiene el valor de la propiedad rateOfChange.
         * 
         */
        public float getRateOfChange() {
            return rateOfChange;
        }

        /**
         * Define el valor de la propiedad rateOfChange.
         * 
         */
        public void setRateOfChange(float value) {
            this.rateOfChange = value;
        }

        /**
         * Obtiene el valor de la propiedad currencyISOCode.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCurrencyISOCode() {
            return currencyISOCode;
        }

        /**
         * Define el valor de la propiedad currencyISOCode.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCurrencyISOCode(String value) {
            this.currencyISOCode = value;
        }

    }


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
     *         &lt;element name="tradeItemDescriptionInformation">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="longText" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="invoicedQuantity">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>byte">
     *                 &lt;attribute name="unitOfMeasure" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="aditionalQuantity">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>byte">
     *                 &lt;attribute name="QuantityType" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="grossPrice">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="netPrice">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="tradeItemTaxInformation">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="taxTypeDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="tradeItemTaxAmount">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="taxPercentage" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="taxAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="totalLineAmount">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="grossAmount">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="netAmount">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}byte" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tradeItemDescriptionInformation",
        "invoicedQuantity",
        "aditionalQuantity",
        "grossPrice",
        "netPrice",
        "tradeItemTaxInformation",
        "totalLineAmount"
    })
    public static class LineItem {

        @XmlElement(required = true)
        protected RequestForPayment.LineItem.TradeItemDescriptionInformation tradeItemDescriptionInformation;
        @XmlElement(required = true)
        protected RequestForPayment.LineItem.InvoicedQuantity invoicedQuantity;
        @XmlElement(required = true)
        protected RequestForPayment.LineItem.AditionalQuantity aditionalQuantity;
        @XmlElement(required = true)
        protected RequestForPayment.LineItem.GrossPrice grossPrice;
        @XmlElement(required = true)
        protected RequestForPayment.LineItem.NetPrice netPrice;
        @XmlElement(required = true)
        protected RequestForPayment.LineItem.TradeItemTaxInformation tradeItemTaxInformation;
        @XmlElement(required = true)
        protected RequestForPayment.LineItem.TotalLineAmount totalLineAmount;
        @XmlAttribute(name = "type")
        protected String type;
        @XmlAttribute(name = "number")
        protected Byte number;

        /**
         * Obtiene el valor de la propiedad tradeItemDescriptionInformation.
         * 
         * @return
         *     possible object is
         *     {@link RequestForPayment.LineItem.TradeItemDescriptionInformation }
         *     
         */
        public RequestForPayment.LineItem.TradeItemDescriptionInformation getTradeItemDescriptionInformation() {
            return tradeItemDescriptionInformation;
        }

        /**
         * Define el valor de la propiedad tradeItemDescriptionInformation.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestForPayment.LineItem.TradeItemDescriptionInformation }
         *     
         */
        public void setTradeItemDescriptionInformation(RequestForPayment.LineItem.TradeItemDescriptionInformation value) {
            this.tradeItemDescriptionInformation = value;
        }

        /**
         * Obtiene el valor de la propiedad invoicedQuantity.
         * 
         * @return
         *     possible object is
         *     {@link RequestForPayment.LineItem.InvoicedQuantity }
         *     
         */
        public RequestForPayment.LineItem.InvoicedQuantity getInvoicedQuantity() {
            return invoicedQuantity;
        }

        /**
         * Define el valor de la propiedad invoicedQuantity.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestForPayment.LineItem.InvoicedQuantity }
         *     
         */
        public void setInvoicedQuantity(RequestForPayment.LineItem.InvoicedQuantity value) {
            this.invoicedQuantity = value;
        }

        /**
         * Obtiene el valor de la propiedad aditionalQuantity.
         * 
         * @return
         *     possible object is
         *     {@link RequestForPayment.LineItem.AditionalQuantity }
         *     
         */
        public RequestForPayment.LineItem.AditionalQuantity getAditionalQuantity() {
            return aditionalQuantity;
        }

        /**
         * Define el valor de la propiedad aditionalQuantity.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestForPayment.LineItem.AditionalQuantity }
         *     
         */
        public void setAditionalQuantity(RequestForPayment.LineItem.AditionalQuantity value) {
            this.aditionalQuantity = value;
        }

        /**
         * Obtiene el valor de la propiedad grossPrice.
         * 
         * @return
         *     possible object is
         *     {@link RequestForPayment.LineItem.GrossPrice }
         *     
         */
        public RequestForPayment.LineItem.GrossPrice getGrossPrice() {
            return grossPrice;
        }

        /**
         * Define el valor de la propiedad grossPrice.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestForPayment.LineItem.GrossPrice }
         *     
         */
        public void setGrossPrice(RequestForPayment.LineItem.GrossPrice value) {
            this.grossPrice = value;
        }

        /**
         * Obtiene el valor de la propiedad netPrice.
         * 
         * @return
         *     possible object is
         *     {@link RequestForPayment.LineItem.NetPrice }
         *     
         */
        public RequestForPayment.LineItem.NetPrice getNetPrice() {
            return netPrice;
        }

        /**
         * Define el valor de la propiedad netPrice.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestForPayment.LineItem.NetPrice }
         *     
         */
        public void setNetPrice(RequestForPayment.LineItem.NetPrice value) {
            this.netPrice = value;
        }

        /**
         * Obtiene el valor de la propiedad tradeItemTaxInformation.
         * 
         * @return
         *     possible object is
         *     {@link RequestForPayment.LineItem.TradeItemTaxInformation }
         *     
         */
        public RequestForPayment.LineItem.TradeItemTaxInformation getTradeItemTaxInformation() {
            return tradeItemTaxInformation;
        }

        /**
         * Define el valor de la propiedad tradeItemTaxInformation.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestForPayment.LineItem.TradeItemTaxInformation }
         *     
         */
        public void setTradeItemTaxInformation(RequestForPayment.LineItem.TradeItemTaxInformation value) {
            this.tradeItemTaxInformation = value;
        }

        /**
         * Obtiene el valor de la propiedad totalLineAmount.
         * 
         * @return
         *     possible object is
         *     {@link RequestForPayment.LineItem.TotalLineAmount }
         *     
         */
        public RequestForPayment.LineItem.TotalLineAmount getTotalLineAmount() {
            return totalLineAmount;
        }

        /**
         * Define el valor de la propiedad totalLineAmount.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestForPayment.LineItem.TotalLineAmount }
         *     
         */
        public void setTotalLineAmount(RequestForPayment.LineItem.TotalLineAmount value) {
            this.totalLineAmount = value;
        }

        /**
         * Obtiene el valor de la propiedad type.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getType() {
            return type;
        }

        /**
         * Define el valor de la propiedad type.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Obtiene el valor de la propiedad number.
         * 
         * @return
         *     possible object is
         *     {@link Byte }
         *     
         */
        public Byte getNumber() {
            return number;
        }

        /**
         * Define el valor de la propiedad number.
         * 
         * @param value
         *     allowed object is
         *     {@link Byte }
         *     
         */
        public void setNumber(Byte value) {
            this.number = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>byte">
         *       &lt;attribute name="QuantityType" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class AditionalQuantity {

            @XmlValue
            protected byte value;
            @XmlAttribute(name = "QuantityType")
            protected String quantityType;

            /**
             * Obtiene el valor de la propiedad value.
             * 
             */
            public byte getValue() {
                return value;
            }

            /**
             * Define el valor de la propiedad value.
             * 
             */
            public void setValue(byte value) {
                this.value = value;
            }

            /**
             * Obtiene el valor de la propiedad quantityType.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getQuantityType() {
                return quantityType;
            }

            /**
             * Define el valor de la propiedad quantityType.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setQuantityType(String value) {
                this.quantityType = value;
            }

        }


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
         *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
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
            "amount"
        })
        public static class GrossPrice {

            @XmlElement(name = "Amount")
            protected float amount;

            /**
             * Obtiene el valor de la propiedad amount.
             * 
             */
            public float getAmount() {
                return amount;
            }

            /**
             * Define el valor de la propiedad amount.
             * 
             */
            public void setAmount(float value) {
                this.amount = value;
            }

        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>byte">
         *       &lt;attribute name="unitOfMeasure" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class InvoicedQuantity {

            @XmlValue
            protected byte value;
            @XmlAttribute(name = "unitOfMeasure")
            protected String unitOfMeasure;

            /**
             * Obtiene el valor de la propiedad value.
             * 
             */
            public byte getValue() {
                return value;
            }

            /**
             * Define el valor de la propiedad value.
             * 
             */
            public void setValue(byte value) {
                this.value = value;
            }

            /**
             * Obtiene el valor de la propiedad unitOfMeasure.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUnitOfMeasure() {
                return unitOfMeasure;
            }

            /**
             * Define el valor de la propiedad unitOfMeasure.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUnitOfMeasure(String value) {
                this.unitOfMeasure = value;
            }

        }


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
         *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
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
            "amount"
        })
        public static class NetPrice {

            @XmlElement(name = "Amount")
            protected float amount;

            /**
             * Obtiene el valor de la propiedad amount.
             * 
             */
            public float getAmount() {
                return amount;
            }

            /**
             * Define el valor de la propiedad amount.
             * 
             */
            public void setAmount(float value) {
                this.amount = value;
            }

        }


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
         *         &lt;element name="grossAmount">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="netAmount">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
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
            "grossAmount",
            "netAmount"
        })
        public static class TotalLineAmount {

            @XmlElement(required = true)
            protected RequestForPayment.LineItem.TotalLineAmount.GrossAmount grossAmount;
            @XmlElement(required = true)
            protected RequestForPayment.LineItem.TotalLineAmount.NetAmount netAmount;

            /**
             * Obtiene el valor de la propiedad grossAmount.
             * 
             * @return
             *     possible object is
             *     {@link RequestForPayment.LineItem.TotalLineAmount.GrossAmount }
             *     
             */
            public RequestForPayment.LineItem.TotalLineAmount.GrossAmount getGrossAmount() {
                return grossAmount;
            }

            /**
             * Define el valor de la propiedad grossAmount.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestForPayment.LineItem.TotalLineAmount.GrossAmount }
             *     
             */
            public void setGrossAmount(RequestForPayment.LineItem.TotalLineAmount.GrossAmount value) {
                this.grossAmount = value;
            }

            /**
             * Obtiene el valor de la propiedad netAmount.
             * 
             * @return
             *     possible object is
             *     {@link RequestForPayment.LineItem.TotalLineAmount.NetAmount }
             *     
             */
            public RequestForPayment.LineItem.TotalLineAmount.NetAmount getNetAmount() {
                return netAmount;
            }

            /**
             * Define el valor de la propiedad netAmount.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestForPayment.LineItem.TotalLineAmount.NetAmount }
             *     
             */
            public void setNetAmount(RequestForPayment.LineItem.TotalLineAmount.NetAmount value) {
                this.netAmount = value;
            }


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
             *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
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
                "amount"
            })
            public static class GrossAmount {

                @XmlElement(name = "Amount")
                protected float amount;

                /**
                 * Obtiene el valor de la propiedad amount.
                 * 
                 */
                public float getAmount() {
                    return amount;
                }

                /**
                 * Define el valor de la propiedad amount.
                 * 
                 */
                public void setAmount(float value) {
                    this.amount = value;
                }

            }


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
             *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
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
                "amount"
            })
            public static class NetAmount {

                @XmlElement(name = "Amount")
                protected float amount;

                /**
                 * Obtiene el valor de la propiedad amount.
                 * 
                 */
                public float getAmount() {
                    return amount;
                }

                /**
                 * Define el valor de la propiedad amount.
                 * 
                 */
                public void setAmount(float value) {
                    this.amount = value;
                }

            }

        }


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
         *         &lt;element name="longText" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "longText"
        })
        public static class TradeItemDescriptionInformation {

            @XmlElement(required = true)
            protected String longText;
            @XmlAttribute(name = "type")
            protected String type;

            /**
             * Obtiene el valor de la propiedad longText.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLongText() {
                return longText;
            }

            /**
             * Define el valor de la propiedad longText.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLongText(String value) {
                this.longText = value;
            }

            /**
             * Obtiene el valor de la propiedad type.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getType() {
                return type;
            }

            /**
             * Define el valor de la propiedad type.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setType(String value) {
                this.type = value;
            }

        }


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
         *         &lt;element name="taxTypeDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="tradeItemTaxAmount">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="taxPercentage" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="taxAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
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
            "taxTypeDescription",
            "tradeItemTaxAmount"
        })
        public static class TradeItemTaxInformation {

            @XmlElement(required = true)
            protected String taxTypeDescription;
            @XmlElement(required = true)
            protected RequestForPayment.LineItem.TradeItemTaxInformation.TradeItemTaxAmount tradeItemTaxAmount;

            /**
             * Obtiene el valor de la propiedad taxTypeDescription.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTaxTypeDescription() {
                return taxTypeDescription;
            }

            /**
             * Define el valor de la propiedad taxTypeDescription.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTaxTypeDescription(String value) {
                this.taxTypeDescription = value;
            }

            /**
             * Obtiene el valor de la propiedad tradeItemTaxAmount.
             * 
             * @return
             *     possible object is
             *     {@link RequestForPayment.LineItem.TradeItemTaxInformation.TradeItemTaxAmount }
             *     
             */
            public RequestForPayment.LineItem.TradeItemTaxInformation.TradeItemTaxAmount getTradeItemTaxAmount() {
                return tradeItemTaxAmount;
            }

            /**
             * Define el valor de la propiedad tradeItemTaxAmount.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestForPayment.LineItem.TradeItemTaxInformation.TradeItemTaxAmount }
             *     
             */
            public void setTradeItemTaxAmount(RequestForPayment.LineItem.TradeItemTaxInformation.TradeItemTaxAmount value) {
                this.tradeItemTaxAmount = value;
            }


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
             *         &lt;element name="taxPercentage" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="taxAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
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
                "taxPercentage",
                "taxAmount"
            })
            public static class TradeItemTaxAmount {

                @XmlElement(required = true)
                protected String taxPercentage;
                protected float taxAmount;

                /**
                 * Obtiene el valor de la propiedad taxPercentage.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getTaxPercentage() {
                    return taxPercentage;
                }

                /**
                 * Define el valor de la propiedad taxPercentage.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setTaxPercentage(String value) {
                    this.taxPercentage = value;
                }

                /**
                 * Obtiene el valor de la propiedad taxAmount.
                 * 
                 */
                public float getTaxAmount() {
                    return taxAmount;
                }

                /**
                 * Define el valor de la propiedad taxAmount.
                 * 
                 */
                public void setTaxAmount(float value) {
                    this.taxAmount = value;
                }

            }

        }

    }


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
     *         &lt;element name="referenceIdentification">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "referenceIdentification"
    })
    public static class OrderIdentification {

        @XmlElement(required = true)
        protected RequestForPayment.OrderIdentification.ReferenceIdentification referenceIdentification;

        /**
         * Obtiene el valor de la propiedad referenceIdentification.
         * 
         * @return
         *     possible object is
         *     {@link RequestForPayment.OrderIdentification.ReferenceIdentification }
         *     
         */
        public RequestForPayment.OrderIdentification.ReferenceIdentification getReferenceIdentification() {
            return referenceIdentification;
        }

        /**
         * Define el valor de la propiedad referenceIdentification.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestForPayment.OrderIdentification.ReferenceIdentification }
         *     
         */
        public void setReferenceIdentification(RequestForPayment.OrderIdentification.ReferenceIdentification value) {
            this.referenceIdentification = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class ReferenceIdentification {

            @XmlValue
            protected String value;
            @XmlAttribute(name = "type")
            protected String type;

            /**
             * Obtiene el valor de la propiedad value.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Define el valor de la propiedad value.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Obtiene el valor de la propiedad type.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getType() {
                return type;
            }

            /**
             * Define el valor de la propiedad type.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setType(String value) {
                this.type = value;
            }

        }

    }


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
     *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
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
        "amount"
    })
    public static class PayableAmount {

        @XmlElement(name = "Amount")
        protected float amount;

        /**
         * Obtiene el valor de la propiedad amount.
         * 
         */
        public float getAmount() {
            return amount;
        }

        /**
         * Define el valor de la propiedad amount.
         * 
         */
        public void setAmount(float value) {
            this.amount = value;
        }

    }


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
     *         &lt;element name="entityType" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="uniqueCreatorIdentification" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
        "entityType",
        "uniqueCreatorIdentification"
    })
    public static class RequestForPaymentIdentification {

        @XmlElement(required = true)
        protected String entityType;
        protected int uniqueCreatorIdentification;

        /**
         * Obtiene el valor de la propiedad entityType.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEntityType() {
            return entityType;
        }

        /**
         * Define el valor de la propiedad entityType.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEntityType(String value) {
            this.entityType = value;
        }

        /**
         * Obtiene el valor de la propiedad uniqueCreatorIdentification.
         * 
         */
        public int getUniqueCreatorIdentification() {
            return uniqueCreatorIdentification;
        }

        /**
         * Define el valor de la propiedad uniqueCreatorIdentification.
         * 
         */
        public void setUniqueCreatorIdentification(int value) {
            this.uniqueCreatorIdentification = value;
        }

    }


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
     *         &lt;element name="gln" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="alternatePartyIdentification">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>short">
     *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "gln",
        "alternatePartyIdentification"
    })
    public static class Seller {

        protected short gln;
        @XmlElement(required = true)
        protected RequestForPayment.Seller.AlternatePartyIdentification alternatePartyIdentification;

        /**
         * Obtiene el valor de la propiedad gln.
         * 
         */
        public short getGln() {
            return gln;
        }

        /**
         * Define el valor de la propiedad gln.
         * 
         */
        public void setGln(short value) {
            this.gln = value;
        }

        /**
         * Obtiene el valor de la propiedad alternatePartyIdentification.
         * 
         * @return
         *     possible object is
         *     {@link RequestForPayment.Seller.AlternatePartyIdentification }
         *     
         */
        public RequestForPayment.Seller.AlternatePartyIdentification getAlternatePartyIdentification() {
            return alternatePartyIdentification;
        }

        /**
         * Define el valor de la propiedad alternatePartyIdentification.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestForPayment.Seller.AlternatePartyIdentification }
         *     
         */
        public void setAlternatePartyIdentification(RequestForPayment.Seller.AlternatePartyIdentification value) {
            this.alternatePartyIdentification = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>short">
         *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class AlternatePartyIdentification {

            @XmlValue
            protected short value;
            @XmlAttribute(name = "type")
            protected String type;

            /**
             * Obtiene el valor de la propiedad value.
             * 
             */
            public short getValue() {
                return value;
            }

            /**
             * Define el valor de la propiedad value.
             * 
             */
            public void setValue(short value) {
                this.value = value;
            }

            /**
             * Obtiene el valor de la propiedad type.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getType() {
                return type;
            }

            /**
             * Define el valor de la propiedad type.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setType(String value) {
                this.type = value;
            }

        }

    }


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
     *         &lt;element name="taxPercentage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="taxAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *         &lt;element name="taxCategory" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "taxPercentage",
        "taxAmount",
        "taxCategory"
    })
    public static class Tax {

        protected String taxPercentage;
        protected float taxAmount;
        @XmlElement(required = true)
        protected String taxCategory;
        @XmlAttribute(name = "type")
        protected String type;

        /**
         * Obtiene el valor de la propiedad taxPercentage.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTaxPercentage() {
            return taxPercentage;
        }

        /**
         * Define el valor de la propiedad taxPercentage.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTaxPercentage(String value) {
            this.taxPercentage = value;
        }

        /**
         * Obtiene el valor de la propiedad taxAmount.
         * 
         */
        public float getTaxAmount() {
            return taxAmount;
        }

        /**
         * Define el valor de la propiedad taxAmount.
         * 
         */
        public void setTaxAmount(float value) {
            this.taxAmount = value;
        }

        /**
         * Obtiene el valor de la propiedad taxCategory.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTaxCategory() {
            return taxCategory;
        }

        /**
         * Define el valor de la propiedad taxCategory.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTaxCategory(String value) {
            this.taxCategory = value;
        }

        /**
         * Obtiene el valor de la propiedad type.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getType() {
            return type;
        }

        /**
         * Define el valor de la propiedad type.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setType(String value) {
            this.type = value;
        }

    }

}


