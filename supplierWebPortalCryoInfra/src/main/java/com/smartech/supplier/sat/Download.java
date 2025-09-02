package com.smartech.supplier.sat;

import org.w3c.dom.Document;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public class Download extends RequestBase {
	private Logger log4j = Logger.getLogger(Download.class);
    /**
     * Constructor of Download class
     *
     * @param url
     * @param SOAPAction
     */
    public Download(String url, String SOAPAction) {
        super(url, SOAPAction);
    }

    @Override
    protected String getResult(String xmlResponse) {
        Document doc = convertStringToXMLDocument(xmlResponse);

        //Verify XML document is build correctly
        if (doc != null) {
            return doc.getElementsByTagName("Paquete")
                    .item(0)
                    .getTextContent();
        }

        return null;
    }

    /**
     * Generate XML to send through SAT's web service
     *
     * @param certificate
     * @param privateKey
     * @param rfcSolicitante
     * @param idPackage
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws CertificateEncodingException
     */
    public void generate(X509Certificate certificate, PrivateKey privateKey, String rfcSolicitante, String idPackage)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
    	
        String canonicalTimestamp = "<des:PeticionDescargaMasivaTercerosEntrada xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\">" +
                "<des:peticionDescarga IdPaquete=\"" + idPackage + "\" RfcSolicitante=\"" + rfcSolicitante + "></des:peticionDescarga>" +
                "</des:PeticionDescargaMasivaTercerosEntrada>";

        String digest = createDigest(canonicalTimestamp);

        String canonicalSignedInfo = "<SignedInfo>" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></CanonicalizationMethod>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod>" +
                "<Reference URI=\"#_0\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"></Transform>" +
                "</Transforms>" +
                "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>" +
                "<DigestValue>" + digest + "</DigestValue>" +
                "</Reference>" +
                "</SignedInfo>";

        String signature = sign(canonicalSignedInfo, privateKey);
        String certificateEncoded = Base64.encodeBase64String(certificate.getEncoded()); 

        this.setXml("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\" xmlns:xd=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<s:Header/>" +
                "<s:Body>" +
                "<des:PeticionDescargaMasivaTercerosEntrada>" +
                "<des:peticionDescarga IdPaquete=\"" + idPackage + "\" RfcSolicitante=\"" + rfcSolicitante + "\">" +
                "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<SignedInfo>" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>" +
                "<Reference URI=\"\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>" +
                "</Transforms>" +
                "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>" +
                "<DigestValue>" + digest + "</DigestValue>" +
                "</Reference>" +
                "</SignedInfo>" +
                "<SignatureValue>" + signature + "</SignatureValue>" +
                "<KeyInfo>" +
                "<X509Data>" +
                "<X509IssuerSerial>" +
                "<X509IssuerName>" + certificate.getIssuerX500Principal() + "</X509IssuerName>" +
                "<X509SerialNumber>" + certificate.getSerialNumber() + "</X509SerialNumber>" +
                "</X509IssuerSerial>" +
                "<X509Certificate>" + certificateEncoded + "</X509Certificate>" +
                "</X509Data>" +
                "</KeyInfo>" +
                "</Signature>" +
                "</des:peticionDescarga>" +
                "</des:PeticionDescargaMasivaTercerosEntrada>" +
                "</s:Body>" +
                "</s:Envelope>");
        
        log4j.info("Doenload: " + this.getXml());
    }
    
    
}
