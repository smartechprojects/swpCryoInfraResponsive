package com.eurest.supplier.test;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.eurest.supplier.edi.SupplierJdeDTO;
import com.eurest.supplier.service.HTTPRequestService;
import com.eurest.supplier.util.AppConstants;

public class testMdwWS {
	
	static String HOST_PROV="http://localhost:8082";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
//String json="{\"addresNumber\":\"541476\",\"email\":\"\",\"currentApprover\":\"FINAL\",\"nextApprover\":\"FINAL\",\"approvalStatus\":\"APROBADO\",\"approvalStep\":\"THIRD\",\"steps\":2,\"rejectNotes\":\"\",\"approvalNotes\":\"OK\",\"regiones\":\"\",\"categorias\":\"\",\"categoriaJDE\":\"\",\"cuentaBancaria\":\"\",\"diasCredito\":\"\",\"tipoMovimiento\":\"A\",\"compradorAsignado\":\"\",\"name\":\"\",\"razonSocial\":\"PRUEBA FLUJO\",\"giroEmpresa\":\"\",\"rfc\":\"FLUJ870312JK8\",\"emailSupplier\":\"csalinas@smartech.com.mx\",\"industryType\":\"\",\"calleNumero\":\"FRACCIONAMIENTO LAS CRUCES\",\"colonia\":\"CIUDAD SATÃ‰LITE\",\"codigoPostal\":\"53100\",\"delegacionMnicipio\":\"NAUCALPAN DE JUÃ�REZ\",\"estado\":\"EM\",\"country\":\"MX\",\"webSite\":\"\",\"telefonoDF\":\"5436563\",\"faxDF\":\"\",\"nombreContactoCxC\":\"CRISTIAN SALINAS\",\"emailContactoCxC\":\"\",\"apellidoPaternoCxC\":\"\",\"apellidoMaternoCxC\":\"\",\"telefonoContactoCxC\":\"5523432423\",\"faxCxC\":\"\",\"cargoCxC\":\"COMPRAS\",\"searchType\":\"V\",\"creditMessage\":\"\",\"taxAreaCxC\":\"\",\"taxExpl2CxC\":\"\",\"pmtTrmCxC\":\"15R\",\"payInstCxC\":\"\",\"currCodeCxC\":\"\",\"catCode15\":\"\",\"catCode27\":\"\",\"emailCxP01\":\"pruebas@gmail.com\",\"nombreCxP01\":\"SGHGFHDFG\",\"telefonoCxP01\":\"675756757\",\"emailCxP02\":\"pruebas@gmail.com\",\"nombreCxP02\":\"FSGHDFGH\",\"telefonoCxP02\":\"765776786\",\"emailCxP03\":\"\",\"nombreCxP03\":\"\",\"telefonoCxP03\":\"\",\"emailCxP04\":\"\",\"nombreCxP04\":\"\",\"telefonoCxP04\":\"\",\"tipoIdentificacion\":null,\"numeroIdentificacion\":\"\",\"nombreRL\":\"\",\"apellidoPaternoRL\":\"\",\"apellidoMaternoRL\":\"\",\"bankTitleRepName\":\"\",\"bankName\":\"\",\"cuentaClabe\":\"\",\"bankAccountNumber\":\"\",\"swiftCode\":\"\",\"ibanCode\":\"\",\"glClass\":\"NAL\",\"bankTransitNumber\":\"\",\"custBankAcct\":\"12341542354435464\",\"controlDigit\":\"MX\",\"description\":\"BANCOMER\",\"checkingOrSavingAccount\":null,\"rollNumber\":\"\",\"bankAddressNumber\":\"\",\"bankCountryCode\":\"MX\",\"nombreBanco\":\"\",\"formaPago\":\"\",\"nombreContactoPedidos\":\"\",\"emailContactoPedidos\":\"csalinas@smartech.com.mx\",\"telefonoContactoPedidos\":\"\",\"nombreContactoVentas\":\"\",\"emailContactoVentas\":\"\",\"telefonoContactoVentas\":\"\",\"nombreContactoCalidad\":\"\",\"emailContactoCalidad\":\"\",\"telefonoContactoCalidad\":\"\",\"puestoCalidad\":\"\",\"direccionPlanta\":\"\",\"direccionCentroDistribucion\":\"\",\"tipoProductoServicio\":\"\",\"riesgoCategoria\":\"\",\"observaciones\":\"\",\"batchNumber\":\"90259\",\"diasCreditoActual\":\"\",\"diasCreditoAnterior\":\"\",\"tasaIva\":\"\",\"taxRate\":\"\",\"explCode1\":\"\",\"invException\":\"\",\"paymentMethod\":\"D\",\"supplierType\":\"\",\"automaticEmail\":null,\"fileList\":\"\",\"emailComprador\":\"csalinas@smartech.com.mx\",\"currencyCode\":\"MXN\",\"fisicaMoral\":\"1\",\"taxId\":\"\",\"faxNumber\":null,\"legalRepIdType\":null,\"legalRepIdNumber\":null,\"legalRepName\":null,\"industryClass\":null,\"ukuid\":70000217,\"hold\":\"\",\"id\":95,\"catCode01\":\"3AA\",\"catCode20\":\"MER\",\"catCode23\":\"04\",\"catCode24\":\"03\",\"requisitosFiscales\":\"IVAHON10\",\"idFiscal\":null}";
//String json="{\"addresNumber\":\"541476\",\"email\":\"\",\"currentApprover\":\"FINAL\",\"nextApprover\":\"FINAL\",\"approvalStatus\":\"APROBADO\",\"approvalStep\":\"THIRD\",\"steps\":2,\"rejectNotes\":\"\",\"approvalNotes\":\"OK\",\"regiones\":\"\",\"categorias\":\"\",\"categoriaJDE\":\"\",\"cuentaBancaria\":\"\",\"diasCredito\":\"\",\"tipoMovimiento\":\"A\",\"compradorAsignado\":\"\",\"name\":\"\",\"razonSocial\":\"PRUEBA FLUJO\",\"giroEmpresa\":\"\",\"rfc\":\"FLUJ870312JK8\",\"emailSupplier\":\"csalinas@smartech.com.mx\",\"industryType\":\"\",\"calleNumero\":\"FRACCIONAMIENTO LAS CRUCES\",\"colonia\":\"CIUDAD SATÃ‰LITE\",\"codigoPostal\":\"53100\",\"delegacionMnicipio\":\"NAUCALPAN DE JUÃ�REZ\",\"estado\":\"EM\",\"country\":\"MX\",\"webSite\":\"\",\"telefonoDF\":\"5436563\",\"faxDF\":\"\",\"nombreContactoCxC\":\"CRISTIAN SALINAS\",\"emailContactoCxC\":\"\",\"apellidoPaternoCxC\":\"\",\"apellidoMaternoCxC\":\"\",\"telefonoContactoCxC\":\"5523432423\",\"faxCxC\":\"\",\"cargoCxC\":\"COMPRAS\",\"searchType\":\"V\",\"creditMessage\":\"\",\"taxAreaCxC\":\"\",\"taxExpl2CxC\":\"\",\"pmtTrmCxC\":\"15R\",\"payInstCxC\":\"\",\"currCodeCxC\":\"\",\"catCode15\":\"\",\"catCode27\":\"\",\"catCode01\":\"3AA\",\"catCode20\":\"MER\",\"catCode23\":\"04\",\"catCode24\":\"03\",\"requisitosFiscales\":\"IVAHON10\",\"idFiscal\":null,\"emailCxP01\":\"pruebas@gmail.com\",\"nombreCxP01\":\"SGHGFHDFG\",\"telefonoCxP01\":\"675756757\",\"emailCxP02\":\"pruebas@gmail.com\",\"nombreCxP02\":\"FSGHDFGH\",\"telefonoCxP02\":\"765776786\",\"emailCxP03\":\"\",\"nombreCxP03\":\"\",\"telefonoCxP03\":\"\",\"emailCxP04\":\"\",\"nombreCxP04\":\"\",\"telefonoCxP04\":\"\",\"tipoIdentificacion\":null,\"numeroIdentificacion\":\"\",\"nombreRL\":\"\",\"apellidoPaternoRL\":\"\",\"apellidoMaternoRL\":\"\",\"bankTitleRepName\":\"\",\"bankName\":\"\",\"cuentaClabe\":\"\",\"bankAccountNumber\":\"\",\"swiftCode\":\"\",\"ibanCode\":\"\",\"glClass\":\"NAL\",\"bankTransitNumber\":\"\",\"custBankAcct\":\"12341542354435464\",\"controlDigit\":\"MX\",\"description\":\"BANCOMER\",\"checkingOrSavingAccount\":null,\"rollNumber\":\"\",\"bankAddressNumber\":\"\",\"bankCountryCode\":\"MX\",\"nombreBanco\":\"\",\"formaPago\":\"\",\"nombreContactoPedidos\":\"\",\"emailContactoPedidos\":\"csalinas@smartech.com.mx\",\"telefonoContactoPedidos\":\"\",\"nombreContactoVentas\":\"\",\"emailContactoVentas\":\"\",\"telefonoContactoVentas\":\"\",\"nombreContactoCalidad\":\"\",\"emailContactoCalidad\":\"\",\"telefonoContactoCalidad\":\"\",\"puestoCalidad\":\"\",\"direccionPlanta\":\"\",\"direccionCentroDistribucion\":\"\",\"tipoProductoServicio\":\"\",\"riesgoCategoria\":\"\",\"observaciones\":\"\",\"batchNumber\":\"90259\",\"diasCreditoActual\":\"\",\"diasCreditoAnterior\":\"\",\"tasaIva\":\"\",\"taxRate\":\"\",\"explCode1\":\"\",\"invException\":\"\",\"paymentMethod\":\"D\",\"supplierType\":\"\",\"automaticEmail\":null,\"fileList\":\"\",\"emailComprador\":\"csalinas@smartech.com.mx\",\"currencyCode\":\"MXN\",\"fisicaMoral\":\"1\",\"taxId\":\"\",\"faxNumber\":null,\"legalRepIdType\":null,\"legalRepIdNumber\":null,\"legalRepName\":null,\"industryClass\":null,\"ukuid\":70000217,\"hold\":\"\",\"id\":95}";
		String json ="[{\"addresNumber\":\"541476\",\"email\":\"\",\"currentApprover\":\"FINAL\",\"nextApprover\":\"FINAL\",\"approvalStatus\":\"APROBADO\",\"approvalStep\":\"THIRD\",\"steps\":2,\"rejectNotes\":\"\",\"approvalNotes\":\"OK\",\"regiones\":\"\",\"categorias\":\"\",\"categoriaJDE\":\"\",\"cuentaBancaria\":\"\",\"diasCredito\":\"\",\"tipoMovimiento\":\"A\",\"compradorAsignado\":\"\",\"name\":\"\",\"razonSocial\":\"PRUEBA FLUJO\",\"giroEmpresa\":\"\",\"rfc\":\"FLUJ870312JK8\",\"emailSupplier\":\"csalinas@smartech.com.mx\",\"industryType\":\"\",\"calleNumero\":\"FRACCIONAMIENTO LAS CRUCES\",\"colonia\":\"CIUDAD SATÃ‰LITE\",\"codigoPostal\":\"53100\",\"delegacionMnicipio\":\"NAUCALPAN DE JUÃ�REZ\",\"estado\":\"EM\",\"country\":\"MX\",\"webSite\":\"\",\"telefonoDF\":\"5436563\",\"faxDF\":\"\",\"nombreContactoCxC\":\"CRISTIAN SALINAS\",\"emailContactoCxC\":\"\",\"apellidoPaternoCxC\":\"\",\"apellidoMaternoCxC\":\"\",\"telefonoContactoCxC\":\"5523432423\",\"faxCxC\":\"\",\"cargoCxC\":\"COMPRAS\",\"searchType\":\"V\",\"creditMessage\":\"\",\"taxAreaCxC\":\"\",\"taxExpl2CxC\":\"\",\"pmtTrmCxC\":\"15R\",\"payInstCxC\":\"\",\"currCodeCxC\":\"\",\"catCode15\":\"\",\"catCode27\":\"\",\"catCode01\":\"3AA\",\"catCode20\":\"MER\",\"catCode23\":\"04\",\"catCode24\":\"03\",\"requisitosFiscales\":\"IVAHON10\",\"idFiscal\":\"\",\"emailCxP01\":\"pruebas@gmail.com\",\"nombreCxP01\":\"SGHGFHDFG\",\"telefonoCxP01\":\"675756757\",\"emailCxP02\":\"pruebas@gmail.com\",\"nombreCxP02\":\"FSGHDFGH\",\"telefonoCxP02\":\"765776786\",\"emailCxP03\":\"\",\"nombreCxP03\":\"\",\"telefonoCxP03\":\"\",\"emailCxP04\":\"\",\"nombreCxP04\":\"\",\"telefonoCxP04\":\"\",\"tipoIdentificacion\":\"\",\"numeroIdentificacion\":\"\",\"nombreRL\":\"\",\"apellidoPaternoRL\":\"\",\"apellidoMaternoRL\":\"\",\"bankTitleRepName\":\"\",\"bankName\":\"\",\"cuentaClabe\":\"\",\"bankAccountNumber\":\"\",\"swiftCode\":\"\",\"ibanCode\":\"\",\"glClass\":\"NAL\",\"bankTransitNumber\":\"\",\"custBankAcct\":\"12341542354435464\",\"controlDigit\":\"MX\",\"description\":\"BANCOMER\",\"checkingOrSavingAccount\":\"\",\"rollNumber\":\"\",\"bankAddressNumber\":\"\",\"bankCountryCode\":\"MX\",\"nombreBanco\":\"\",\"formaPago\":\"\",\"nombreContactoPedidos\":\"\",\"emailContactoPedidos\":\"csalinas@smartech.com.mx\",\"telefonoContactoPedidos\":\"\",\"nombreContactoVentas\":\"\",\"emailContactoVentas\":\"\",\"telefonoContactoVentas\":\"\",\"nombreContactoCalidad\":\"\",\"emailContactoCalidad\":\"\",\"telefonoContactoCalidad\":\"\",\"puestoCalidad\":\"\",\"direccionPlanta\":\"\",\"direccionCentroDistribucion\":\"\",\"tipoProductoServicio\":\"\",\"riesgoCategoria\":\"\",\"observaciones\":\"\",\"batchNumber\":\"90259\",\"diasCreditoActual\":\"\",\"diasCreditoAnterior\":\"\",\"tasaIva\":\"\",\"taxRate\":\"\",\"explCode1\":\"\",\"invException\":\"\",\"paymentMethod\":\"D\",\"supplierType\":\"\",\"automaticEmail\":\"\",\"fileList\":\"\",\"emailComprador\":\"csalinas@smartech.com.mx\",\"currencyCode\":\"MXN\",\"fisicaMoral\":\"1\",\"taxId\":\"\",\"faxNumber\":\"\",\"legalRepIdType\":\"\",\"legalRepIdNumber\":\"\",\"legalRepName\":\"\",\"industryClass\":\"\",\"ukuid\":70000217,\"hold\":\"\",\"id\":95}]";
//String json="{\"addresNumber\":\"541476\",\"email\":\"\",\"currentApprover\":\"FINAL\",\"nextApprover\":\"FINAL\",\"approvalStatus\":\"APROBADO\",\"approvalStep\":\"THIRD\",\"steps\":2,\"rejectNotes\":\"\",\"approvalNotes\":\"OK\",\"regiones\":\"\",\"categorias\":\"\",\"categoriaJDE\":\"\",\"cuentaBancaria\":\"\",\"diasCredito\":\"\",\"tipoMovimiento\":\"A\",\"compradorAsignado\":\"\",\"name\":\"\",\"razonSocial\":\"PRUEBA FLUJO\",\"giroEmpresa\":\"\",\"rfc\":\"FLUJ870312JK8\",\"emailSupplier\":\"csalinas@smartech.com.mx\",\"industryType\":\"\",\"calleNumero\":\"FRACCIONAMIENTO LAS CRUCES\",\"colonia\":\"CIUDAD SAT\u00C3\u2030LITE\",\"codigoPostal\":\"53100\",\"delegacionMnicipio\":\"NAUCALPAN DE JU\u00C3\uFFFDREZ\",\"estado\":\"EM\",\"country\":\"MX\",\"webSite\":\"\",\"telefonoDF\":\"5436563\",\"faxDF\":\"\",\"nombreContactoCxC\":\"CRISTIAN SALINAS\",\"emailContactoCxC\":\"\",\"apellidoPaternoCxC\":\"\",\"apellidoMaternoCxC\":\"\",\"telefonoContactoCxC\":\"5523432423\",\"faxCxC\":\"\",\"cargoCxC\":\"COMPRAS\",\"searchType\":\"V\",\"creditMessage\":\"\",\"taxAreaCxC\":\"\",\"taxExpl2CxC\":\"\",\"pmtTrmCxC\":\"15R\",\"payInstCxC\":\"\",\"currCodeCxC\":\"\",\"catCode15\":\"\",\"catCode27\":\"\",\"emailCxP01\":\"pruebas@gmail.com\",\"nombreCxP01\":\"SGHGFHDFG\",\"telefonoCxP01\":\"675756757\",\"emailCxP02\":\"pruebas@gmail.com\",\"nombreCxP02\":\"FSGHDFGH\",\"telefonoCxP02\":\"765776786\",\"emailCxP03\":\"\",\"nombreCxP03\":\"\",\"telefonoCxP03\":\"\",\"emailCxP04\":\"\",\"nombreCxP04\":\"\",\"telefonoCxP04\":\"\",\"tipoIdentificacion\":null,\"numeroIdentificacion\":\"\",\"nombreRL\":\"\",\"apellidoPaternoRL\":\"\",\"apellidoMaternoRL\":\"\",\"bankTitleRepName\":\"\",\"bankName\":\"\",\"cuentaClabe\":\"\",\"bankAccountNumber\":\"\",\"swiftCode\":\"\",\"ibanCode\":\"\",\"glClass\":\"NAL\",\"bankTransitNumber\":\"\",\"custBankAcct\":\"12341542354435464\",\"controlDigit\":\"MX\",\"description\":\"BANCOMER\",\"checkingOrSavingAccount\":null,\"rollNumber\":\"\",\"bankAddressNumber\":\"\",\"bankCountryCode\":\"MX\",\"nombreBanco\":\"\",\"formaPago\":\"\",\"nombreContactoPedidos\":\"\",\"emailContactoPedidos\":\"csalinas@smartech.com.mx\",\"telefonoContactoPedidos\":\"\",\"nombreContactoVentas\":\"\",\"emailContactoVentas\":\"\",\"telefonoContactoVentas\":\"\",\"nombreContactoCalidad\":\"\",\"emailContactoCalidad\":\"\",\"telefonoContactoCalidad\":\"\",\"puestoCalidad\":\"\",\"direccionPlanta\":\"\",\"direccionCentroDistribucion\":\"\",\"tipoProductoServicio\":\"\",\"riesgoCategoria\":\"\",\"observaciones\":\"\",\"batchNumber\":\"90259\",\"diasCreditoActual\":\"\",\"diasCreditoAnterior\":\"\",\"tasaIva\":\"\",\"taxRate\":\"\",\"explCode1\":\"\",\"invException\":\"\",\"paymentMethod\":\"D\",\"supplierType\":\"\",\"automaticEmail\":null,\"fileList\":\"\",\"emailComprador\":\"csalinas@smartech.com.mx\",\"currencyCode\":\"MXN\",\"fisicaMoral\":\"1\",\"taxId\":\"\",\"faxNumber\":null,\"legalRepIdType\":null,\"legalRepIdNumber\":null,\"legalRepName\":null,\"industryClass\":null,\"ukuid\":70000217,\"hold\":\"\",\"id\":95,\"catCode01\":\"3AA\",\"catCode20\":\"MER\",\"catCode23\":\"04\",\"catCode24\":\"03\",\"requisitosFiscales\":\"IVAHON10\",\"idFiscal\":null}";

ObjectMapper objectMapper = new ObjectMapper();

SupplierJdeDTO sup = new SupplierJdeDTO();


try {
/*		String json2=objectMapper.writeValueAsString(sup);;

RestTemplate restTemplate = new RestTemplate();

String url = HOST_PROV + "/supplierWebPortalCryoInfra/middleware/updatePostAddressBook";

HttpHeaders headers = new HttpHeaders();


headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
//headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
System.out.println(json);
System.out.println(headers);
HttpEntity<String> entity = new HttpEntity<String>(json,headers);
System.out.println(entity);
String answer = restTemplate.postForObject(url, entity, String.class);
//ResponseEntity<String> answer = restTemplate.postForEntity(url, entity, String.class);
System.out.println(answer);*/
/*
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_JSON);

HttpEntity<String> entity = new HttpEntity<String>(json ,headers);
//restTemplate.put(url, entity);




ResponseEntity<String> responses = restTemplate.postForEntity(url, entity, String.class);

System.out.println(responses);*/

/*	String url = "http://localhost:8082/supplierWebPortalCryoInfra/middleware/updatePostAddressBook";
    ObjectMapper jsonMapper = new ObjectMapper();

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Accept", "application/json");
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);


    Map<String, String> params = new HashMap<>();
    HttpEntity<?> httpEntity = new HttpEntity(json, (MultiValueMap)httpHeaders);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, 
        String.class, params);
    HttpStatus statusCode = responseEntity.getStatusCode();
    if (statusCode.value() == 200) {
      String body = (String)responseEntity.getBody();
      if (body != null) {
        System.out.println(body);
      } 
    } */

	/*String url = "http://localhost:8082/supplierWebPortalCryoInfra/middleware/updatePostAddressBook";
URL uRL = new URL(url);
HttpURLConnection http = (HttpURLConnection)uRL.openConnection();
http.setRequestMethod("POST");
http.setDoOutput(true);
http.setRequestProperty("Accept", "application/json");
http.setRequestProperty("Content-Type", "application/json");

String data = "{\n  \"Id\": 78912,\n  \"Customer\": \"Jason Sweet\",\n  \"Quantity\": 1,\n  \"Price\": 18.00\n}";

byte[] out = json.getBytes(StandardCharsets.UTF_8);

OutputStream stream = http.getOutputStream();
stream.write(out);

System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
http.disconnect();
*/
		
	/*	String url = HOST_PROV + "/supplierWebPortalCryoInfra/middleware/updatePostAddressBook";
		
HttpHeaders httpHeaders = new HttpHeaders();
httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	//final String url = AppConstants.URL_HOST + "/supplierWebPortalRestCryoInfra/payments";
Map<String, String> params = new HashMap<String, String>();
HttpEntity<?> httpEntity = new HttpEntity<>(json, httpHeaders);
RestTemplate restTemplate = new RestTemplate();
ResponseEntity<String>responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity,String.class, params);
HttpStatus statusCode = responseEntity.getStatusCode();
if (statusCode.value() == 200) {
	System.out.println(statusCode.value());
}*/
	
	String respuesta = null;


			respuesta = HTTPRequestService
					.httpPost(HOST_PROV + "/supplierWebPortalCryoInfra/middleware/updatePostAddressBook?id="
							+ 100 + "&addressBook=" + 1000, "");

//WA

 /* respuesta="{\"data\": \"[{\\\"addresNumber\\\":\\\"4040726\\\",\\\"email\\\":\\\"NULL\\\",\\\"currentApprover\\\":\\\"\\\",\\\"nextApprover\\\":\\\"\\\",\\\"approvalStatus\\\":\\\"APROBADO\\\",\\\"approvalStep\\\":\\\"\\\",\\\"steps\\\":0,\\\"rejectNotes\\\":\\\"\\\",\\\"approvalNotes\\\":\\\"CARGA MASIVA\\\",\\\"regiones\\\":\\\"NULL\\\",\\\"categorias\\\":\\\"NULL\\\",\\\"categoriaJDE\\\":\\\"NULL\\\",\\\"cuentaBancaria\\\":\\\"NULL\\\",\\\"diasCredito\\\":\\\"NULL\\\",\\\"tipoMovimiento\\\":\\\"A\\\",\\\"compradorAsignado\\\":\\\"NULL\\\",\\\"name\\\":\\\"MARTHA LETICIA ALCALA DELGADO\\\",\\\"razonSocial\\\":\\\"MARTHA LETICIA ALCALA DELGADO\\\",\\\"giroEmpresa\\\":\\\"NULL\\\",\\\"rfc\\\":\\\"AADM861219CJ2\\\",\\\"emailSupplier\\\":\\\"javila@smartech.com.mx\\\",\\\"industryType\\\":\\\"NULL\\\",\\\"calleNumero\\\":\\\"NULL\\\",\\\"colonia\\\":\\\"NULL\\\",\\\"codigoPostal\\\":\\\"NULL\\\",\\\"delegacionMnicipio\\\":\\\"NULL\\\",\\\"estado\\\":\\\"DF\\\",\\\"country\\\":\\\"MX\\\",\\\"webSite\\\":\\\"NULL\\\",\\\"telefonoDF\\\":\\\"NULL\\\",\\\"faxDF\\\":\\\"NULL\\\",\\\"nombreContactoCxC\\\":\\\"NULL\\\",\\\"emailContactoCxC\\\":\\\"NULL\\\",\\\"apellidoPaternoCxC\\\":\\\"NULL\\\",\\\"apellidoMaternoCxC\\\":\\\"NULL\\\",\\\"telefonoContactoCxC\\\":\\\"NULL\\\",\\\"faxCxC\\\":\\\"NULL\\\",\\\"cargoCxC\\\":\\\"NULL\\\",\\\"searchType\\\":null,\\\"creditMessage\\\":null,\\\"taxAreaCxC\\\":\\\"NULL\\\",\\\"taxExpl2CxC\\\":\\\"NULL\\\",\\\"pmtTrmCxC\\\":\\\"NULL\\\",\\\"payInstCxC\\\":\\\"NULL\\\",\\\"currCodeCxC\\\":\\\"NULL\\\",\\\"catCode15\\\":\\\"NA\\\",\\\"catCode27\\\":\\\"NA\\\",\\\"emailCxP01\\\":\\\"NULL\\\",\\\"nombreCxP01\\\":\\\"NULL\\\",\\\"telefonoCxP01\\\":\\\"NULL\\\",\\\"emailCxP02\\\":\\\"NULL\\\",\\\"nombreCxP02\\\":\\\"NULL\\\",\\\"telefonoCxP02\\\":\\\"NULL\\\",\\\"emailCxP03\\\":\\\"NULL\\\",\\\"nombreCxP03\\\":\\\"NULL\\\",\\\"telefonoCxP03\\\":\\\"NULL\\\",\\\"emailCxP04\\\":\\\"NULL\\\",\\\"nombreCxP04\\\":\\\"NULL\\\",\\\"telefonoCxP04\\\":\\\"NULL\\\",\\\"tipoIdentificacion\\\":\\\"NULL\\\",\\\"numeroIdentificacion\\\":\\\"NULL\\\",\\\"nombreRL\\\":\\\"NULL\\\",\\\"apellidoPaternoRL\\\":\\\"NULL\\\",\\\"apellidoMaternoRL\\\":\\\"NULL\\\",\\\"bankTitleRepName\\\":\\\"NULL\\\",\\\"bankName\\\":\\\"NULL\\\",\\\"cuentaClabe\\\":\\\"NULL\\\",\\\"bankAccountNumber\\\":\\\"NULL\\\",\\\"swiftCode\\\":\\\"NULL\\\",\\\"ibanCode\\\":\\\"NULL\\\",\\\"glClass\\\":\\\"NULL\\\",\\\"bankTransitNumber\\\":\\\"NULL\\\",\\\"custBankAcct\\\":\\\"NULL\\\",\\\"controlDigit\\\":\\\"NULL\\\",\\\"description\\\":\\\"NULL\\\",\\\"checkingOrSavingAccount\\\":\\\"NULL\\\",\\\"rollNumber\\\":\\\"NULL\\\",\\\"bankAddressNumber\\\":null,\\\"bankCountryCode\\\":\\\"NULL\\\",\\\"nombreBanco\\\":\\\"NULL\\\",\\\"formaPago\\\":\\\"NULL\\\",\\\"nombreContactoPedidos\\\":\\\"NULL\\\",\\\"emailContactoPedidos\\\":\\\"javila@smartech.com.mx\\\",\\\"telefonoContactoPedidos\\\":\\\"NULL\\\",\\\"nombreContactoVentas\\\":\\\"NULL\\\",\\\"emailContactoVentas\\\":\\\"NULL\\\",\\\"telefonoContactoVentas\\\":\\\"NULL\\\",\\\"nombreContactoCalidad\\\":\\\"NULL\\\",\\\"emailContactoCalidad\\\":\\\"NULL\\\",\\\"telefonoContactoCalidad\\\":\\\"NULL\\\",\\\"puestoCalidad\\\":\\\"NULL\\\",\\\"direccionPlanta\\\":\\\"NULL\\\",\\\"direccionCentroDistribucion\\\":\\\"NULL\\\",\\\"tipoProductoServicio\\\":\\\"NULL\\\",\\\"riesgoCategoria\\\":\\\"NULL\\\",\\\"observaciones\\\":\\\"\\\",\\\"batchNumber\\\":\\\"90250\\\",\\\"diasCreditoActual\\\":\\\"NULL\\\",\\\"diasCreditoAnterior\\\":\\\"NULL\\\",\\\"tasaIva\\\":\\\"NULL\\\",\\\"taxRate\\\":\\\"NULL\\\",\\\"explCode1\\\":null,\\\"invException\\\":\\\"NULL\\\",\\\"paymentMethod\\\":\\\"NULL\\\",\\\"supplierType\\\":\\\"NULL\\\",\\\"automaticEmail\\\":\\\"NULL\\\",\\\"fileList\\\":\\\"\\\",\\\"emailComprador\\\":\\\"NULL\\\",\\\"currencyCode\\\":\\\"NULL\\\",\\\"fisicaMoral\\\":\\\"NULL\\\",\\\"taxId\\\":\\\"NULL\\\",\\\"faxNumber\\\":\\\"NULL\\\",\\\"legalRepIdType\\\":\\\"NULL\\\",\\\"legalRepIdNumber\\\":\\\"NULL\\\",\\\"legalRepName\\\":\\\"NULL\\\",\\\"industryClass\\\":\\\"NULL\\\",\\\"ukuid\\\":0,\\\"hold\\\":null}]\",\r\n" + 
		"   \"total\": 1," + 
		"   \"success\": true" + 
		"}";*/

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mapResponse = mapper.readValue(respuesta, Map.class);
			String data = (String) mapResponse.get("data");
			int total = (int) mapResponse.get("total");
}catch(Exception ex) {
ex.printStackTrace();
	}
	

	}
}
