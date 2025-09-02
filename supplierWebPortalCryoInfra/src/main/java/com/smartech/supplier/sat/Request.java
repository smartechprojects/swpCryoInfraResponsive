package com.smartech.supplier.sat;

import org.w3c.dom.Document;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class Request extends RequestBase {

    private String typeRequest;
    
    private Logger log4j = Logger.getLogger(Request.class);

    /**
     * Constructor of Request class
     *
     * @param url
     * @param SOAPAction
     */
    public Request(String url, String SOAPAction) {
        super(url, SOAPAction);
    }

    public void setTypeRequest(String typeRequest) {
        this.typeRequest = typeRequest;
    }

    @Override
    protected String getResult(String xmlResponse) {
        Document doc = convertStringToXMLDocument(xmlResponse);

        //Verify XML document is build correctly
        if (doc != null) {
        	log4j.info("REQUEST RESULT:" + xmlResponse);
            return doc.getElementsByTagName("SolicitaDescargaRecibidosResult")
                    .item(0)
                    .getAttributes()
                    .getNamedItem("IdSolicitud").getTextContent();
        }

        return null;
    }

    /**
     * Generate XML to send through SAT's web service
     *
     * @param certificate
     * @param privateKey
     * @param rfcEmisor
     * @param rfcReceptor
     * @param rfcSolicitante
     * @param fechaInicial
     * @param fechaFinal
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws CertificateEncodingException
     * @throws UnsupportedEncodingException 
     */
    public void generateReceived_bkp(X509Certificate certificate,
                         PrivateKey privateKey,
                         String rfcEmisor,
                         String rfcReceptor,
                         String rfcSolicitante,
                         String fechaInicial,
                         String fechaFinal,
                         boolean special
    ) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException, UnsupportedEncodingException {
        
    	if(!special) {
    		fechaInicial = fechaInicial + "T00:00:00";
            fechaFinal = fechaFinal + "T23:59:59";
    	}
    	

       // String tipoComprobante = "I";
        String canonicalTimestamp = "<des:SolicitaDescargaRecibidos>" +
                "<des:solicitud EstadoComprobante=\"Vigente\" FechaInicial=\"" + fechaInicial + "\" FechaFinal=\"" + fechaFinal + "\" TipoSolicitud=\"" + this.typeRequest + "\" RfcReceptor=\"" + rfcReceptor + "\">" +
                "</des:solicitud>" +
                "</des:SolicitaDescargaRecibidos>";

        String digest = createDigest(canonicalTimestamp);

        String canonicalSignedInfo_bkp = "<SignedInfo>" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></CanonicalizationMethod>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod>" +
                "<Reference URI=\"\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"></Transform>" +
                "</Transforms>" +
                "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>" +
                "<DigestValue>" + digest + "</DigestValue>" +
                "</Reference>" +
                "</SignedInfo>";
        
        String canonicalSignedInfo = "<SignedInfo>" +
        	    "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>" +
        	    "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>" +
        	    "<Reference URI=\"\">" +
        	    "<Transforms>" +  // <- Nota: "Transforms" con 's' aquí
        	    "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>" +
        	    "</Transforms>" +
        	    "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>" +
        	    "<DigestValue>" + digest + "</DigestValue>" +
        	    "</Reference>" +
        	    "</SignedInfo>";

        String signature = sign(canonicalSignedInfo, privateKey);
        String certificateEncoded = Base64.encodeBase64String(certificate.getEncoded()); 

        this.setXml("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\" xmlns:xd=\"http://www.w3.org/2000/09/xmldsig#\">" +
        		"<s:Header> " +
		        //"<ActivityId CorrelationId=\"806aad0d-ef46-443b-9741-040c8e8e8c7d\" xmlns=\"http://schemas.microsoft.com/2004/09/ServiceModel/Diagnostics\">" +
		        //"e906cfb4-f706-43de-94d0-5cc935be1aaa</ActivityId> " +
		        "</s:Header> " +
                "<s:Body>" +
                "<des:SolicitaDescargaRecibidos>" +
                "<des:solicitud EstadoComprobante=\"Vigente\" FechaInicial=\"" + fechaInicial + "\" FechaFinal=\"" + fechaFinal + "\" TipoSolicitud=\"" + this.typeRequest + "\" RfcReceptor=\"" + rfcReceptor + "\">" +
                // "<RfcReceptores  xmlns=\"http://DescargaMasivaTerceros.sat.gob.mx\"> " +
		        //"<RfcReceptor>" + rfcReceptor + "</RfcReceptor>" +
		       // "</RfcReceptores> " +
                "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<SignedInfo>" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>" +
                //"<Reference URI=\"#_0\">" +
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
                "</des:solicitud>" +
                "</des:SolicitaDescargaRecibidos>" +
                "</s:Body>" +
                "</s:Envelope>");
        
        log4j.info(this.getXml());

    }
    
    public void generateReceived(X509Certificate certificate,
            PrivateKey privateKey,
            String rfcEmisor,
            String rfcReceptor,
            String rfcSolicitante,
            String fechaInicial,
            String fechaFinal,
            boolean special) throws Exception {

    	// 1. Formateo de fechas
		if (!special) {
			fechaInicial = fechaInicial + "T00:00:00";
			fechaFinal = fechaFinal + "T23:59:59";
		}

		// 2. Creación del digest (SHA-1)
		String canonicalTimestamp = "<des:SolicitaDescargaRecibidos xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\">"
				+ "<des:solicitud EstadoComprobante=\"Vigente\" " + "FechaInicial=\"" + fechaInicial + "\" "
				+ "FechaFinal=\"" + fechaFinal + "\" " + "TipoSolicitud=\"" + this.typeRequest + "\" "
				+ "RfcReceptor=\"" + rfcReceptor + "\"/>" + "</des:SolicitaDescargaRecibidos>";

		String digest = createDigest(canonicalTimestamp);

		// 3. Firma digital (XMLDSig)
		String canonicalSignedInfo = "<SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">"
				+ "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>"
				+ "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>" + "<Reference URI=\"\">"
				+ "<Transforms>" + "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>"
				+ "</Transforms>" + "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>"
				+ "<DigestValue>" + digest + "</DigestValue>" + "</Reference>" + "</SignedInfo>";

		String signature = sign(canonicalSignedInfo, privateKey);
		String certificateEncoded = Base64.encodeBase64String(certificate.getEncoded());

		// 4. Construcción del SOAP request
		String xmlRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
				+ "xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\" "
				+ "xmlns:xd=\"http://www.w3.org/2000/09/xmldsig#\">" + "<soapenv:Header/>" + "<soapenv:Body>"
				+ "<des:SolicitaDescargaRecibidos>" + "<des:solicitud EstadoComprobante=\"Vigente\" "
				+ "FechaInicial=\"" + fechaInicial + "\" " + "FechaFinal=\"" + fechaFinal + "\" " + "TipoSolicitud=\""
				+ this.typeRequest + "\" " + "RfcReceptor=\"" + rfcReceptor + "\">"
				+ "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" + "<SignedInfo>"
				+ "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>"
				+ "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>" + "<Reference URI=\"\">"
				+ "<Transforms>" + "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>"
				+ "</Transforms>" + "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>"
				+ "<DigestValue>" + digest + "</DigestValue>" + "</Reference>" + "</SignedInfo>" + "<SignatureValue>"
				+ signature + "</SignatureValue>" + "<KeyInfo>" + "<X509Data>" + "<X509IssuerSerial>"
				+ "<X509IssuerName>" + escapeXml(certificate.getIssuerX500Principal().toString()) + "</X509IssuerName>"
				+ "<X509SerialNumber>" + certificate.getSerialNumber() + "</X509SerialNumber>" + "</X509IssuerSerial>"
				+ "<X509Certificate>" + certificateEncoded + "</X509Certificate>" + "</X509Data>" + "</KeyInfo>"
				+ "</Signature>" + "</des:solicitud>" + "</des:SolicitaDescargaRecibidos>" + "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		this.setXml(xmlRequest);
		log4j.debug("XML generado para SAT:\n" + xmlRequest);
	}

	// Método auxiliar para escape de caracteres XML
	private String escapeXml(String input) {
		return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
				.replace("'", "&apos;");
	}
}
