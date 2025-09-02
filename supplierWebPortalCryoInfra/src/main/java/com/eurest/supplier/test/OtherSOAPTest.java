package com.eurest.supplier.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class OtherSOAPTest {

	public static void main(String[] args) {
		try {
			http_client();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	 public static void http_client() throws Exception {

	        String payload =
	            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://microsoft.com/webservices/\"> " + 
	            "   <SOAP-ENV:Header/> " + 
	            "   <SOAP-ENV:Body> " + 
	            "      <web:ValidaXML> " + 
	            "         <web:Usuario>SERVICIOS UNIVERSAL</web:Usuario> " + 
	            "         <web:Pwd>UNIVERSAL</web:Pwd> " + 
	            "         <web:XML><![CDATA[<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd \" Version=\"3.3\" Folio=\"8250564207\" Fecha=\"2019-03-01T02:33:16\" FormaPago=\"99\" CondicionesDePago=\"090\" SubTotal=\"1468.12\" Moneda=\"MXN\" Total=\"1468.12\" Sello=\"GRuupj8ecx42VBOTPdlP+igGjOozXy2vdzoI5yZkMCIXkbddKFJhXNgPmmCc/QsjdMb/FxMz1MV+yIxXOnVVAQ1R0CngnXw+pVTX2zJb2+bHm2smY0UEjj7iwvsjnq9vpZ0AwaRkg2EdfgqvHrImlSQjB86OlKkQJnGeh6qr3U8LSH/1lPm6XVt8HxcqTL+9fHBYYgeZo5VDbir6XrEulEs8uQdCby94p97R5/HsZdYlyL21vZWuQ+MqvkAd4NvI38RpHaL1gwumddpNN8yxdrm02+ACYRHZXcOG5IJuw92K8Ra311MLoDVyFY1gdH70ngCAvzCbXUj9X1QGkX+1zg==\" TipoDeComprobante=\"I\" MetodoPago=\"PPD\" LugarExpedicion=\"53519\"  Certificado=\"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\"  NoCertificado=\"00001000000402885500\" ><cfdi:Emisor Rfc=\"CNO930113K12\" Nombre=\"SIGMA FOODSERVICE COMERCIAL S DE RL DE CV\" RegimenFiscal=\"623\"/> <cfdi:Receptor Rfc=\"EPM811006HH2\" Nombre=\"EUREST PROPER MEALS DE MEXICO SA DE CV\" UsoCFDI=\"G01\"/> <cfdi:Conceptos> <cfdi:Concepto ClaveProdServ=\"50121500\" NoIdentificacion=\"000000000070070000\" Cantidad=\"5.080\" ClaveUnidad=\"KGM\" Unidad=\"KG\" Descripcion=\"FILETE DE SALMON PREMIUM\" ValorUnitario=\"289.000\" Importe=\"1468.12\" > <cfdi:Impuestos> <cfdi:Traslados> <cfdi:Traslado Base=\"1468.12\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\" /> </cfdi:Traslados> </cfdi:Impuestos> <cfdi:InformacionAduanera NumeroPedimento=\"18  16  3381  8017229\" /></cfdi:Concepto> </cfdi:Conceptos> <cfdi:Impuestos TotalImpuestosTrasladados=\"0.00\"> <cfdi:Traslados> <cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\"/> </cfdi:Traslados> </cfdi:Impuestos> <cfdi:Complemento> <tfd:TimbreFiscalDigital Version=\"1.1\" UUID=\"c0415786-261a-4e09-9926-14d4c95684da\" RfcProvCertif=\"ALL040309DQ0\" FechaTimbrado=\"2019-03-01T02:33:20\" SelloCFD=\"GRuupj8ecx42VBOTPdlP+igGjOozXy2vdzoI5yZkMCIXkbddKFJhXNgPmmCc/QsjdMb/FxMz1MV+yIxXOnVVAQ1R0CngnXw+pVTX2zJb2+bHm2smY0UEjj7iwvsjnq9vpZ0AwaRkg2EdfgqvHrImlSQjB86OlKkQJnGeh6qr3U8LSH/1lPm6XVt8HxcqTL+9fHBYYgeZo5VDbir6XrEulEs8uQdCby94p97R5/HsZdYlyL21vZWuQ+MqvkAd4NvI38RpHaL1gwumddpNN8yxdrm02+ACYRHZXcOG5IJuw92K8Ra311MLoDVyFY1gdH70ngCAvzCbXUj9X1QGkX+1zg==\" NoCertificadoSAT=\"00001000000408344117\" SelloSAT=\"MmGT0RGyHbJpKnqszrSBKAUrNpAClpHR6lQwNrpQlWPNdDq9t8wHIVDt5f8qMrJAM5UjE7ttt1Ehhx4vHSq8webByyUIFkokzC9vgkczK/qv1n4fbGggfp/zLdXpj3ONa4BqHGbZ7lgNmmg4tH2NPV/JiD5QIDEkZHhaLDOMJhJ358CgwEUvoALh8YD6RMvZXFPpmAloFCpYgXqTx+XlmfvJ2rHQt8sYGldGe4whekLOVGWtQHNobnPU3KGWOah4+2urtiDOSyJTclXGpbmR+Qjc43E8hMMfOTk9dTcGwM9/mCOEHNq4ITVMRxDxv6KSwES6+mTY0Mr0aFrd7yVz5w==\" xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\"/> </cfdi:Complemento> </cfdi:Comprobante>]]></web:XML> " + 
	            "      </web:ValidaXML> " + 
	            "   </SOAP-ENV:Body> " + 
	            "</SOAP-ENV:Envelope>"; 

	        httpPost("http://validacfdiv2.buzoncfdi.mx/ValidacionWS.asmx?invoke=", payload,
	                 "username:password");				 
	    }
	 private static String httpPost(String destUrl, String postData,
	                                   String authStr) throws Exception {
	        URL url = new URL(destUrl);
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        if (conn == null) {
	            return null;
	        }
	        conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
	        conn.setDoOutput(true);
	        conn.setDoInput(true);
	        conn.setAllowUserInteraction(false);
	        conn.setRequestMethod("POST"); 
	        conn.setRequestProperty("Accept", "*/*");
	        conn.setRequestProperty("User-Agent", "Java Client");

	        System.out.println("post data size:" + postData.length());
	 

	        OutputStream out = conn.getOutputStream();
	        OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
	        writer.write(postData);
	        writer.close();
	        out.close();

	        System.out.println("connection status: " + conn.getResponseCode() +
	                           "; connection response: " +
	                           conn.getResponseMessage());

	        InputStream in = conn.getInputStream();
	        InputStreamReader iReader = new InputStreamReader(in);
	        BufferedReader bReader = new BufferedReader(iReader);

	        String line;
	        String response = "";
	        System.out.println("==================Service response: ================ ");
	        while ((line = bReader.readLine()) != null) {
	            System.out.println(line);
	            response += line;
	        }
	        iReader.close();
	        bReader.close();
	        in.close();
	        conn.disconnect();
	 

	        return response;
	    }

}
