package com.eurest.supplier.test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.eurest.supplier.util.AppConstants;

import net.sf.json.JSON;

public class StrTest {

	public static void main(String[] args) {
		
		String accountingAcc = "60215.111.111".split("\\.")[0];
		
		System.out.println(accountingAcc);
		String str = "Token|SERVICIOS UNIVERSALVersion|3.3EsValido|TrueTransacciones|6329TransaccionesContadas|1721Estructura|FalseCertificadoEmisor|FalseCertificadoEnListas|TrueCertificadoVigencia|TrueSello|TrueSelloTimbre|FalsePAC|PAC No RegistradoSelloPac|El sello del PAC es correctoFechaFacutra|01/03/2019 02:33:16RFCE|CNO930113K12RFCR|EPM811006HH2RazonSocialE|SIGMA FOODSERVICE COMERCIAL S DE RL DE CVRazonSocialR|EUREST PROPER MEALS DE MEXICO SA DE CVUUID|C0415786-261A-4E09-9926-14D4C95684DASubTotal|1468.12Total|1468.12Moneda|MXNCodigoEstatus|S - Comprobante obtenido satisfactoriamente.EstadoSAT|VigenteCFDI33|{\"Acuse\":{\"CodigoEstatus\":\"S - Comprobante obtenido satisfactoriamente.\",\"Estado\":\"Vigente\"},\"EsValido\":true,\"FechaConsulta\":\"\\/Date(1560213121588-0500)\\/\",\"FechaEmision\":\"\\/Date(1551429196000-0600)\\/\",\"Moneda\":\"MXN\",\"Pac\":\"PAC No Registrado\",\"RazonSocialEmisor\":\"SIGMA FOODSERVICE COMERCIAL S DE RL DE CV\",\"RazonSocialReceptor\":\"EUREST PROPER MEALS DE MEXICO SA DE CV\",\"ResultadoValidacionComercial\":null,\"ResultadoValidacionGeneral\":[{\"Codigo\":320001,\"Mensaje\":\"Documento no contiene Bom.\",\"ResultadoValidacion\":1,\"Version\":\"3.2\"},{\"Codigo\":530002,\"Mensaje\":\"Adenda removida.\",\"ResultadoValidacion\":4,\"Version\":\"3.3\"},{\"Codigo\":530003,\"Mensaje\":\"Documento es un archivo Xml valido.\",\"ResultadoValidacion\":1,\"Version\":\"3.3\"},{\"Codigo\":530004,\"Mensaje\":\"Validación de certificados correcta.\",\"ResultadoValidacion\":1,\"Version\":\"3.3\"},{\"Codigo\":530005,\"Mensaje\":\"Sellos correctos.\",\"ResultadoValidacion\":1,\"Version\":\"3.3\"},{\"Codigo\":530006,\"Mensaje\":\"Validación por esquema XSD correcta.\",\"ResultadoValidacion\":4,\"Version\":\"3.3\"},{\"Codigo\":330007,\"Mensaje\":\"S - Comprobante obtenido satisfactoriamente.|Vigente\",\"ResultadoValidacion\":4,\"Version\":\"3.3\"},{\"Codigo\":530008,\"Mensaje\":\"PAC no registrado en el sistema.\",\"ResultadoValidacion\":3,\"Version\":\"3.3\"}],\"ResultadoValidacionParticular\":[{\"Codigo\":3310090,\"Mensaje\":\"Cfdi no contiene complementos obsoletos.\",\"ResultadoValidacion\":1,\"Version\":\"3.3\"}],\"RfcEmisor\":\"CNO930113K12\",\"RfcReceptor\":\"EPM811006HH2\",\"Saldo\":{\"PeticionesContratadas\":8050,\"PeticionesRestantes\":6329,\"PeticionesUltilizadas\":1721},\"SubTotal\":1468.12,\"Token\":\"SERVICIOS UNIVERSAL\",\"Total\":1468.12,\"UUID\":\"c0415786-261a-4e09-9926-14d4c95684da\",\"Version\":\"3.3\"}";
		         
		String result = str.substring(str.indexOf(AppConstants.SAT_CFDIVER), str.length());
		
		result = result.replaceAll(AppConstants.SAT_CFDIVER, "");
    	 result = result.replace("|", "");
    	 System.out.println(result);
    	 
    	 try {
    	     JSONObject jsonObject = new JSONObject(result);
    	     System.out.println(jsonObject.get("EsValido"));
    	}catch (Exception err){
    	     err.printStackTrace();
    	}
    	 
    	 JSONObject jsonObject = new JSONObject(result);


		    
		
		
		// TODO Auto-generated method stub

	}

}
