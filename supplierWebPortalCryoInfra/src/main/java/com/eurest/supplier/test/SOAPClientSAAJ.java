package com.eurest.supplier.test;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;
import javax.xml.soap.*;

public class SOAPClientSAAJ {

    // SAAJ - SOAP Client Testing
    public static void main(String args[]) {
        String soapEndpointUrl = "http://validacfdiv2.buzoncfdi.mx/ValidacionWS.asmx";
        String soapAction = "http://microsoft.com/webservices/ValidaXML";

        callSoapWebService(soapEndpointUrl, soapAction);
    }

    private static void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String myNamespace = "web";
        String myNamespaceURI = "http://microsoft.com/webservices/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

        String xmlContent = "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd \" Version=\"3.3\" Folio=\"8250564207\" Fecha=\"2019-03-01T02:33:16\" FormaPago=\"99\" CondicionesDePago=\"090\" SubTotal=\"1468.12\" Moneda=\"MXN\" Total=\"1468.12\" Sello=\"GRuupj8ecx42VBOTPdlP+igGjOozXy2vdzoI5yZkMCIXkbddKFJhXNgPmmCc/QsjdMb/FxMz1MV+yIxXOnVVAQ1R0CngnXw+pVTX2zJb2+bHm2smY0UEjj7iwvsjnq9vpZ0AwaRkg2EdfgqvHrImlSQjB86OlKkQJnGeh6qr3U8LSH/1lPm6XVt8HxcqTL+9fHBYYgeZo5VDbir6XrEulEs8uQdCby94p97R5/HsZdYlyL21vZWuQ+MqvkAd4NvI38RpHaL1gwumddpNN8yxdrm02+ACYRHZXcOG5IJuw92K8Ra311MLoDVyFY1gdH70ngCAvzCbXUj9X1QGkX+1zg==\" TipoDeComprobante=\"I\" MetodoPago=\"PPD\" LugarExpedicion=\"53519\"  Certificado=\"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\"  NoCertificado=\"00001000000402885500\" ><cfdi:Emisor Rfc=\"CNO930113K12\" Nombre=\"SIGMA FOODSERVICE COMERCIAL S DE RL DE CV\" RegimenFiscal=\"623\"/> " + 
        		"<cfdi:Receptor Rfc=\"EPM811006HH2\" Nombre=\"EUREST PROPER MEALS DE MEXICO SA DE CV\" UsoCFDI=\"G01\"/> " + 
        		"<cfdi:Conceptos> " + 
        		"<cfdi:Concepto ClaveProdServ=\"50121500\" NoIdentificacion=\"000000000070070000\" Cantidad=\"5.080\" ClaveUnidad=\"KGM\" Unidad=\"KG\" Descripcion=\"FILETE DE SALMON PREMIUM\" ValorUnitario=\"289.000\" Importe=\"1468.12\" > <cfdi:Impuestos> " + 
        		"<cfdi:Traslados> " + 
        		"<cfdi:Traslado Base=\"1468.12\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\" /> " + 
        		"</cfdi:Traslados> " + 
        		"</cfdi:Impuestos> " + 
        		"<cfdi:InformacionAduanera NumeroPedimento=\"18  16  3381  8017229\" /></cfdi:Concepto> " + 
        		"</cfdi:Conceptos> " + 
        		"<cfdi:Impuestos TotalImpuestosTrasladados=\"0.00\"> " + 
        		"<cfdi:Traslados> " + 
        		"<cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\"/> " + 
        		"</cfdi:Traslados> " + 
        		"</cfdi:Impuestos> " + 
        		"<cfdi:Complemento> " + 
        		"<tfd:TimbreFiscalDigital Version=\"1.1\" UUID=\"c0415786-261a-4e09-9926-14d4c95684da\" RfcProvCertif=\"ALL040309DQ0\" FechaTimbrado=\"2019-03-01T02:33:20\" SelloCFD=\"GRuupj8ecx42VBOTPdlP+igGjOozXy2vdzoI5yZkMCIXkbddKFJhXNgPmmCc/QsjdMb/FxMz1MV+yIxXOnVVAQ1R0CngnXw+pVTX2zJb2+bHm2smY0UEjj7iwvsjnq9vpZ0AwaRkg2EdfgqvHrImlSQjB86OlKkQJnGeh6qr3U8LSH/1lPm6XVt8HxcqTL+9fHBYYgeZo5VDbir6XrEulEs8uQdCby94p97R5/HsZdYlyL21vZWuQ+MqvkAd4NvI38RpHaL1gwumddpNN8yxdrm02+ACYRHZXcOG5IJuw92K8Ra311MLoDVyFY1gdH70ngCAvzCbXUj9X1QGkX+1zg==\" NoCertificadoSAT=\"00001000000408344117\" SelloSAT=\"MmGT0RGyHbJpKnqszrSBKAUrNpAClpHR6lQwNrpQlWPNdDq9t8wHIVDt5f8qMrJAM5UjE7ttt1Ehhx4vHSq8webByyUIFkokzC9vgkczK/qv1n4fbGggfp/zLdXpj3ONa4BqHGbZ7lgNmmg4tH2NPV/JiD5QIDEkZHhaLDOMJhJ358CgwEUvoALh8YD6RMvZXFPpmAloFCpYgXqTx+XlmfvJ2rHQt8sYGldGe4whekLOVGWtQHNobnPU3KGWOah4+2urtiDOSyJTclXGpbmR+Qjc43E8hMMfOTk9dTcGwM9/mCOEHNq4ITVMRxDxv6KSwES6+mTY0Mr0aFrd7yVz5w==\" xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\"/> " + 
        		"</cfdi:Complemento> " + 
        		"</cfdi:Comprobante>";
        
        
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("ValidaXML", "web");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("Usuario", "web");
        soapBodyElem1.addTextNode("SERVICIOS UNIVERSAL");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("Pwd", "web");
        soapBodyElem2.addTextNode("UNIVERSAL");
        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("XML", "web");
        soapBodyElem3.addTextNode(xmlContent);
    }

    private static void callSoapWebService(String soapEndpointUrl, String soapAction) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, soapEndpointUrl), soapEndpointUrl);

            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            System.out.println();

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
    }

    private static SOAPMessage createSOAPRequest(String soapAction, String soapEndpointUrl) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();
        
        soapMessage = sendSoapRequest(soapEndpointUrl, soapMessage);

        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println("\n");

        return soapMessage;
    }
    
    public static SOAPMessage sendSoapRequest(String endpointUrl, SOAPMessage request) {
        try {
            final boolean isHttps = endpointUrl.toLowerCase().startsWith("https");
            HttpsURLConnection httpsConnection = null;
            // Open HTTPS connection
            if (isHttps) {
                // Create SSL context and trust all certificates
                SSLContext sslContext = SSLContext.getInstance("SSL");
                TrustManager[] trustAll
                        = new TrustManager[] {new TrustAllCertificates()};
                sslContext.init(null, trustAll, new java.security.SecureRandom());
                // Set trust all certificates context to HttpsURLConnection
                HttpsURLConnection
                        .setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                // Open HTTPS connection
                URL url = new URL(endpointUrl);
                httpsConnection = (HttpsURLConnection) url.openConnection();
                // Trust all hosts
                httpsConnection.setHostnameVerifier(new TrustAllHosts());
                // Connect
                httpsConnection.connect();
            }
            // Send HTTP SOAP request and get response
            SOAPConnection soapConnection
                    = SOAPConnectionFactory.newInstance().createConnection();
            SOAPMessage response = soapConnection.call(request, endpointUrl);
            // Close connection
            soapConnection.close();
            // Close HTTPS connection
            if (isHttps) {
                httpsConnection.disconnect();
            }
            return response;
        } catch (SOAPException | IOException
                | NoSuchAlgorithmException | KeyManagementException ex) {
            // Do Something
        }
        return null;
    }private static class TrustAllCertificates implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
     
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
     
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

		@Override
		public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}
    }
    
    private static class TrustAllHosts implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}

