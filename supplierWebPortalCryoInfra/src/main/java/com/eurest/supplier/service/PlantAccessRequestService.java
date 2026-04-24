package com.eurest.supplier.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.Order;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.eurest.supplier.dao.CDDDCEmployeeDao;
import com.eurest.supplier.dao.LogDataAprovalActionDao;
import com.eurest.supplier.dao.PlantAccessRequestDao;
import com.eurest.supplier.dto.AccesoEmpleadoRequestDTO;
import com.eurest.supplier.dto.AtencionOrdenesRequestDTO;
import com.eurest.supplier.dto.OrdenRequestDTO;
import com.eurest.supplier.dto.PlantAccessRequestDTO;
import com.eurest.supplier.dto.ResponseServiceCryoDTO;
import com.eurest.supplier.model.CDDDCEmployee;
import com.eurest.supplier.model.FileStore;
import com.eurest.supplier.model.PlantAccessRequest;
import com.eurest.supplier.model.PlantAccessWorker;
import com.eurest.supplier.model.PurchaseOrder;
import com.eurest.supplier.model.Receipt;
import com.eurest.supplier.model.UDC;
import com.eurest.supplier.model.Users;
import com.eurest.supplier.util.AppConstants;
import com.eurest.supplier.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

@Service("plantAccessRequestService")
public class PlantAccessRequestService {
	
	
	@Autowired
	PlantAccessRequestDao plantAccessRequestDao;
	
	@Autowired
	CDDDCEmployeeDao cdddcEmployeeDao;

	@Autowired
	UsersService usersService;
	
	@Autowired
	LogDataAprovalActionDao logDataAprovalActionDao;
	
	@Autowired
	FileStoreService fileStoreService;
	
	@Autowired
	PlantAccessWorkerService plantAccessWorkerService;
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	StringUtils stringUtils;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	JavaMailSender mailSenderObj;
	
	@Autowired
	PurchaseOrderService purchaseOrderService;
	
	static String TIMESTAMP_DATE_PATTERN = "yyyy-MM-dd";
	static String TIMESTAMP_DATE_PATTERN_NEW = "yyyy-MM-dd HH:mm:ss";
	static String DATE_PATTERN = "dd/MM/yyyy";
	
	Logger log4j = LogManager.getLogger(PlantAccessRequestService.class);
	
	public PlantAccessRequest getById(int id) {
		return plantAccessRequestDao.getById(id);
	}
	
//	public List<PlantAccessRequest> getPlantAccessRequests( ) {
//		return fiscalDocumentDao.getFiscalDocuments(addressNumber, status, uuid, documentType,  start, limit);		
//	}

	public String save(PlantAccessRequest doc) {
		try {
				plantAccessRequestDao.save(doc);
				return "ok";
		} catch (Exception e) {
			log4j.error("Exception" , e);
			e.printStackTrace();
			return "";
		}
	
	}
	
	public String updatet(PlantAccessRequest doc) {
		
		try {
			plantAccessRequestDao.update(doc);
			return "Success";
	} catch (Exception e) {
		log4j.error("Exception" , e);
		e.printStackTrace();
		return "";
	}
	}


//		public List<FiscalDocuments> getListToSendFletes(int start, int limit) {
//			
//			return fiscalDocumentDao.getListFletesSend(start,limit);
//			
//		}
		
	public PlantAccessRequest getPlantAccessRequests (String uuid) {
		return plantAccessRequestDao.getPlantAccessRequests(uuid);
	}
		
	public List<PlantAccessRequest> getPlantAccessRequests(String rfc, String status,String approver,String addressNumberPA, String dateFrom, int start, int limit) {
		return plantAccessRequestDao.getPlantAccessRequest(rfc, status, approver, addressNumberPA, dateFrom, start, limit);		
	}
	
	public int getPlantAccessRequestsTotal(String rfc, String status,String approver,String addressNumberPA,String dateFrom) {
		return plantAccessRequestDao.getPlantAccessRequestTotal(rfc, status, approver, addressNumberPA, dateFrom);		
	}
	
	public String getAddNewActivity(boolean isAddActivity, String targetString, String currentString) {
		if(isAddActivity) {
			if(!targetString.contains(currentString)) {
				targetString = targetString.concat(",".concat(currentString));
			}
		} else {
			targetString.replace(",".concat(currentString), "");
		}
		return targetString;
	}
	
	public String validatePlantAccessRequest(PlantAccessRequest plantAccessRequest, boolean isSendMail, boolean forceIncludeMissingWorkers) {
		try {
			
			//Valida los archivos de la solicitud
			List<String> fileNameList = new ArrayList<String>();
			List<FileStore> files = fileStoreService.getFilesPlantAccess(plantAccessRequest.getId(), false);//false para no traer el content
			if(files != null && !files.isEmpty()) {
				for(FileStore file : files) {
					if(!fileNameList.contains(file.getDocumentType())) {
						fileNameList.add(file.getDocumentType());
					}
				}
			}
			
			//Valida documentación completa
			List<String> fileNameCompleteList = new ArrayList<String>();
			
			//Documentación por default
			fileNameCompleteList.add("REQUEST_PSUA");
			fileNameCompleteList.add("REQUEST_SUA");
			fileNameCompleteList.add("REQUEST_FPCOPAA");
			
			//Documentación de Equipo Pesado
			if(plantAccessRequest.isHeavyEquipment()) {
				fileNameCompleteList.add("REQUEST_SRC");
				fileNameCompleteList.add("REQUEST_RM");
			}
			
			if(!fileNameList.containsAll(fileNameCompleteList)) {
				return "Es necesario cargar la documentación completa de la solicitud.";
			}
			
			//Valida trabajadores de la solicitud
			List<String> workerNameNoDocsList = new ArrayList<String>();
			List<String> workerNameNoActList = new ArrayList<String>();
			String[] ordenesRequest=plantAccessRequest.getOrdenNumber().split("\\|");
			if(ordenesRequest.length==0) {
				return "Es necesario que la solicitud tenga una orden registrada.";
			}
			
			
			 Set<String> ordenesRequestSet = new HashSet<>(Arrays.asList(ordenesRequest));
			List<PlantAccessWorker> workersList = plantAccessWorkerService.searchWorkersPlantAccessByIdRequest(String.valueOf(plantAccessRequest.getId()));
			if(workersList != null && !workersList.isEmpty()) {
				for(PlantAccessWorker worker : workersList) {
					if(!worker.isDocsActivity1()
						&& !worker.isDocsActivity2()
						&& !worker.isDocsActivity3()
						&& !worker.isDocsActivity4()
						&& !worker.isDocsActivity5()
						&& !worker.isDocsActivity6()
						&& !worker.isDocsActivity7()) {
						workerNameNoActList.add(worker.getEmployeeName() + " " + worker.getEmployeeLastName() + " " + worker.getEmployeeSecondLastName());
					}
					if(!worker.isAllDocuments()) {
						workerNameNoDocsList.add(worker.getEmployeeName() + " " + worker.getEmployeeLastName() + " " + worker.getEmployeeSecondLastName());
					}	
					
					//seccion de validacionde ordenes en trabajador VS ordenes en la solucitud
					
					String[] ordene=worker.getEmployeeOrdenes().split("\\|");
					if(ordene.length==0) {
						return "Es necesario registar una orden en el trabajador: "+worker.getEmployeeName();
					}
					for (String orden : ordene) {
				        if (!ordenesRequestSet.contains(orden)) {
				        	return "Es necesario revisar las ordenes registradas del trabajador: "+worker.getEmployeeName();
				        }
				    }
					
					
				}
				if(!workerNameNoActList.isEmpty()) {
					return "Los siguientes trajadores no tienen actividades registradas:<br><br>" + String.join("<br>", workerNameNoActList);
				}
				if(!workerNameNoDocsList.isEmpty()) {
					return "Los siguientes trajadores tienen documentación pendiente:<br><br>" + String.join("<br>", workerNameNoDocsList);
				}
			
				
				
			} else {
				return "Es necesario registrar por lo menos un trabajador por solicitud.";
			}
			
			// ===== NUEVA VALIDACIÓN: Verificar que trabajadores registrados existan en la cédula =====
			// Si forceIncludeMissingWorkers=true, omitir esta validación
			if(!forceIncludeMissingWorkers) {
			// Buscar el PDF de la cédula (REQUEST_MPDF o REQUEST_SUA)
			FileStore cedulaFile = null;
			if(files != null) {
				for(FileStore file : files) {
					if("REQUEST_MPDF".equals(file.getDocumentType()) || "REQUEST_SUA".equals(file.getDocumentType())) {
						cedulaFile = file;
						break;
					}
				}
			}
			
			if(cedulaFile != null && workersList != null && !workersList.isEmpty()) {
				// Obtener trabajadores de la cédula (ya guardados en CDDDC/CDDDCEmployee cuando se cargó el PDF)
				List<CDDDCEmployee> cedulaEmployees = cdddcEmployeeDao.getEmployeesByFileStoreId(cedulaFile.getId());
				
				if(cedulaEmployees != null && !cedulaEmployees.isEmpty()) {
					// Crear un mapa de empleados de la cédula para búsqueda eficiente
					// Key: nombre normalizado, Value: objeto CDDDCEmployee
					Map<String, CDDDCEmployee> cedulaEmployeesMap = new HashMap<>();
					
					for(CDDDCEmployee emp : cedulaEmployees) {
						String nombre = emp.getNombre();
						if(nombre != null && !nombre.trim().isEmpty()) {
							// Normalizar: quitar espacios extras, convertir a mayúsculas, quitar acentos
							nombre = nombre.trim().toUpperCase()
									.replaceAll("\\s+", " ") // Espacios múltiples a uno solo
									.replace("Á", "A").replace("É", "E").replace("Í", "I")
									.replace("Ó", "O").replace("Ú", "U").replace("Ñ", "N");
							cedulaEmployeesMap.put(nombre, emp);
						}
					}
					
					// Verificar que cada trabajador registrado esté en la cédula con datos válidos
					List<String> workersNotInCedula = new ArrayList<String>();
					List<Integer> workerIdsNotInCedula = new ArrayList<Integer>(); // Para marcar en rojo
					
					for(PlantAccessWorker worker : workersList) {
						// Construir nombre completo del trabajador como se captura
						String workerFullName = (worker.getEmployeeName() + " " + 
												worker.getEmployeeLastName() + " " + 
												worker.getEmployeeSecondLastName()).trim();
						
						// El PDF tiene el orden: Apellido1 Apellido2 Nombre
						// Nosotros capturamos: Nombre Apellido1 Apellido2
						// Reorganizar para comparar con el formato del PDF
						String workerNameForComparison = (worker.getEmployeeLastName() + " " + 
														 worker.getEmployeeSecondLastName() + " " + 
														 worker.getEmployeeName()).trim();
						
						// Normalizar nombre reorganizado del trabajador
						String workerNormalizado = workerNameForComparison.toUpperCase()
								.replaceAll("\\s+", " ")
								.replace("Á", "A").replace("É", "E").replace("Í", "I")
								.replace("Ó", "O").replace("Ú", "U").replace("Ñ", "N");
						
						// Buscar trabajador en la cédula
						CDDDCEmployee cedulaEmployee = cedulaEmployeesMap.get(workerNormalizado);
						
						if(cedulaEmployee == null) {
							// Trabajador NO encontrado por nombre en cédula
							workersNotInCedula.add(workerFullName + " - <b>No encontrado en la cédula (Nombre no coincide)</b>");
							workerIdsNotInCedula.add(worker.getId());
						} else {
							// Trabajador encontrado por nombre, ahora validar NSS y RFC/CURP
							List<String> erroresValidacion = new ArrayList<>();
							
							// 1. Validar NSS (Número de Seguro Social)
							String nssWorker = normalizeForComparison(worker.getMembershipIMSS());
							String nssCedula = normalizeForComparison(cedulaEmployee.getNumSegSoci());
							
							if(nssWorker == null || nssWorker.isEmpty() || nssCedula == null || nssCedula.isEmpty()) {
								erroresValidacion.add("NSS faltante");
							} else if(!nssWorker.equals(nssCedula)) {
								erroresValidacion.add("NSS no coincide (Registrado: " + worker.getMembershipIMSS() + 
													  ", Cédula: " + cedulaEmployee.getNumSegSoci() + ")");
							}
							
							// 2. Validar RFC o CURP (en la cédula puede venir uno u otro en el mismo campo)
							String rfcWorker = normalizeForComparison(worker.getEmployeeRfc());
							String curpWorker = normalizeForComparison(worker.getEmployeeCurp());
							String rfcCurpCedula = normalizeForComparison(cedulaEmployee.getRfcCurp());
							
							boolean rfcCurpValido = false;
							
							if(rfcCurpCedula != null && !rfcCurpCedula.isEmpty()) {
								// Verificar si el RFC del trabajador coincide con el RFC/CURP de la cédula
								if(rfcWorker != null && !rfcWorker.isEmpty() && rfcWorker.equals(rfcCurpCedula)) {
									rfcCurpValido = true;
								}
								// O si el CURP del trabajador coincide con el RFC/CURP de la cédula
								else if(curpWorker != null && !curpWorker.isEmpty() && curpWorker.equals(rfcCurpCedula)) {
									rfcCurpValido = true;
								}
							}
							
							if(!rfcCurpValido) {
								String registradoStr = "";
								if(rfcWorker != null && !rfcWorker.isEmpty()) {
									registradoStr = "RFC: " + worker.getEmployeeRfc();
								}
								if(curpWorker != null && !curpWorker.isEmpty()) {
									if(!registradoStr.isEmpty()) registradoStr += ", ";
									registradoStr += "CURP: " + worker.getEmployeeCurp();
								}
								if(registradoStr.isEmpty()) {
									registradoStr = "No registrado";
								}
								
								erroresValidacion.add("RFC/CURP no coincide (Registrado: " + registradoStr + 
													  ", Cédula: " + (cedulaEmployee.getRfcCurp() != null ? cedulaEmployee.getRfcCurp() : "No disponible") + ")");
							}
							
							// Si hay errores de validación, agregar a la lista
							if(!erroresValidacion.isEmpty()) {
								workersNotInCedula.add(workerFullName + " - <b>" + String.join(", ", erroresValidacion) + "</b>");
								workerIdsNotInCedula.add(worker.getId());
							}
						}
					}
					
					// Si hay trabajadores con validaciones fallidas, retornar mensaje con prefijo CONFIRM:
					if(!workersNotInCedula.isEmpty()) {
						StringBuilder message = new StringBuilder();
						message.append("CONFIRM:");
						message.append("No se encontraron los siguientes empleados en la cédula vigente:");
						message.append("<br><br>");
						for(String workerError : workersNotInCedula) {
							message.append("• ").append(workerError).append("<br>");
						}
						message.append("<br><br><b>¿Desea continuar con el envio de la solicitud?</b>");
						message.append("<br><br><i>Nota: Los trabajadores con errores se han marcado en rojo en la lista.</i>");
						
						// Guardar IDs de trabajadores inválidos para que el frontend pueda marcarlos
						plantAccessRequest.setInvalidWorkerIds(String.join(",", 
							workerIdsNotInCedula.stream().map(String::valueOf).toArray(String[]::new)));
						
						return message.toString();
					}
				}
			}
			} // Cierra if(!forceIncludeMissingWorkers)
			// ===== FIN NUEVA VALIDACIÓN =====
			
			//Si pasa el flujo de aprobación obtiene aprobadores
			String currentApprover ="";
			String emailApprover = "";
			List<UDC> approverUDCList = udcService.advaceSearch("APPROVERPONP", "", plantAccessRequest.getPlantRequest(),"");
			if(approverUDCList != null) {
				for(UDC approver : approverUDCList) {
					if(AppConstants.INV_FIRST_APPROVER.equals(approver.getUdcKey())){
						currentApprover = currentApprover.concat(approver.getStrValue1()).concat(",");;
						emailApprover = emailApprover.concat(approver.getStrValue2().concat(","));
					}
			
				}
			}
			
			//Setea aprobadores
			plantAccessRequest.setAprovUser(currentApprover);
			plantAccessRequest.setAprovUserDef(currentApprover);
			
			//Envía correo a aprobador(es)
			if(isSendMail && emailApprover != null && !emailApprover.isEmpty()) {
				EmailServiceAsync emailAsyncSup = new EmailServiceAsync();
				emailAsyncSup.setProperties("Solicitud de Acceso a Planta", this.stringUtils.prepareEmailContent("Estimado Aprobador:<br />La solicitud de " + plantAccessRequest.getOrdenNumber() + " requiere de su aprobación.<br />Favor de revisar la información en el portal de proveedores."), emailApprover);
				emailAsyncSup.setMailSender(this.mailSenderObj);
				Thread emailThreadSup = new Thread(emailAsyncSup);
				emailThreadSup.start();
			}
			
			return "";
		} catch (Exception e) {
			log4j.error("Exception" , e);
			return "Ocurrió un error al validar la información de la solicitud.";
		}
	}
	
	public String sendPlantAcceseRequestCryo(PlantAccessRequest paRequest, List<PlantAccessWorker> workers,String nombreAprobador) {
	    PlantAccessRequestDTO request = new PlantAccessRequestDTO();

	    try {
	        // Configuración del objeto de solicitud
	        request.setClaveAccesoPlantaPortal(paRequest.getId());
	        request.setNumeroProveedor(Long.parseLong(paRequest.getAddressNumberPA()));
	        request.setRegistroPatronal(paRequest.getEmployerRegistration());
	        request.setNombreSolicitante(paRequest.getNameRequest());
	        request.setRepresentanteProveedor(paRequest.getContractorRepresentative());
	        request.setFechaVigenciaInicio(LocalDateTime.ofInstant(paRequest.getFechaInicio().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE_TIME));
	        request.setFechaVigenciaFinal(LocalDateTime.ofInstant(paRequest.getFechaFin().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE_TIME));
	        request.setFechaAutorizacion(LocalDateTime.ofInstant(paRequest.getFechaAprobacion().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE_TIME));
	        
	        request.setNombreAutorizador(nombreAprobador);
	        
	        request.setRazonSocialSubcontratada(paRequest.getSubContractedCompany() == null ? "" : paRequest.getSubContractedCompany() );
	        request.setRFCSubcontratada(paRequest.getSubContractedCompanyRFC() == null ? "" : paRequest.getSubContractedCompanyRFC());

	        // Manejo de órdenes
	        String[] ordenes = paRequest.getOrdenNumber().split("\\|");
	        ArrayList<AtencionOrdenesRequestDTO> ordenesAtencio = new ArrayList<>();

	        for (String orden : ordenes) {
	            AtencionOrdenesRequestDTO ordenDTO = new AtencionOrdenesRequestDTO();
	            String[] detailOrder = orden.split(",");

	            // 2. Obtener la primera parte (antes de la primera coma)
	            String primeraParte = detailOrder[0]; // "57813"

	            // 3. Reemplazar la primera parte en el texto original con una cadena vacía
	            String restoDelTexto = orden.replace(primeraParte + ",", "");

	            // 4. Reasignar el arreglo detailOrder para que tenga solo dos posiciones
	            detailOrder = new String[] { primeraParte, restoDelTexto };
	            
	            
	            try {
	            	Integer.parseInt(detailOrder[0]);
	                ordenDTO.setFolioOrden(Long.parseLong(detailOrder[0]));
	                ordenDTO.setDescripcionOrden(detailOrder[1]);
	                PurchaseOrder rat = purchaseOrderService.searchbyOrderAdress(Integer.parseInt(detailOrder[0]), paRequest.getAddressNumberPA());
	                ordenDTO.setTipoOrden(rat.getOrderType());
	            	
				} catch (Exception e) {
					ordenDTO.setOrdenTemporal(detailOrder[0]);
		            ordenDTO.setDescripcionOrden(detailOrder[1]);
				}
	            
	          
	            ordenesAtencio.add(ordenDTO);
	        }

	        request.setAtencionOrdenes(ordenesAtencio);

	        // Manejo de empleados
	        ArrayList<AccesoEmpleadoRequestDTO> accesoEmpleados = new ArrayList<>();

	        for (PlantAccessWorker worker : workers) {
	            AccesoEmpleadoRequestDTO empleado = new AccesoEmpleadoRequestDTO();
	            empleado.setNumeroAfiliacionIMSS(worker.getMembershipIMSS());
	            empleado.setNombreCompleto(worker.getEmployeeName() + " " + worker.getEmployeeLastName() + " " + worker.getEmployeeSecondLastName());
	            empleado.setPuesto(worker.getEmployeePuesto());
	            empleado.setFechaInduccion(worker.getDateInduction() + ".000");
	            empleado.setFolioCredencial(worker.getDatefolioIDcard().split("\\|").length == 2 ? worker.getDatefolioIDcard().split("\\|")[1] : worker.getDatefolioIDcard().split("\\|")[0]);
	            empleado.setCURP(worker.getEmployeeCurp());
	            empleado.setRFC(worker.getEmployeeRfc());

	            ArrayList<Integer> listacti = new ArrayList<>();
	            String[] activid = worker.getActivities().split(",");

	            for (String act : activid) {
	                try {
	                    act = act.equals("*") ? "7" : act;
	                    listacti.add(Integer.parseInt(act));
	                } catch (Exception e) {
	                    log4j.error("Error parsing activity ID: " + act, e);
	                }
	            }

	            empleado.setActividades(listacti);

	            String[] ordenesWorker = worker.getEmployeeOrdenes().split("\\|");
	            ArrayList<OrdenRequestDTO> ordenerewor = new ArrayList<>();

	            for (String orden : ordenesWorker) {
	                OrdenRequestDTO ordenrew = new OrdenRequestDTO();
	                String[] detailOrder = orden.split(",");

	                // 2. Obtener la primera parte (antes de la primera coma)
	                String primeraParte = detailOrder[0]; // "57813"

	                // 3. Reemplazar la primera parte en el texto original con una cadena vacía
	                String restoDelTexto = orden.replace(primeraParte + ",", "");

	                // 4. Reasignar el arreglo detailOrder para que tenga solo dos posiciones
	                detailOrder = new String[] { primeraParte, restoDelTexto };
	                
	                
	                try {
						Integer.parseInt(detailOrder[0]);
						 ordenrew.setFolioOrden(Long.parseLong(detailOrder[0]));
						 for (AtencionOrdenesRequestDTO ordenesSolicitud : ordenesAtencio) {
		                        if (ordenesSolicitud.getFolioOrden() == ordenrew.getFolioOrden()) {
		                            ordenrew.setTipoOrden(ordenesSolicitud.getTipoOrden());
		                            break;
		                        }
		                    }
					} catch (Exception e) {
						ordenrew.setOrdenTemporal(detailOrder[0]);
					}
	                ordenerewor.add(ordenrew);
	            }

	            empleado.setOrdenes(ordenerewor);
	            accesoEmpleados.add(empleado);
	        }
	        request.setAccesoEmpleados(accesoEmpleados);

	        // Obtener token
	        String token = getTokenCryoinfra();
	        if(token==null) {
	        	return "Error al conectarse al servicio: contacte al administrador: error(nulltkn)";
	        }
	        log4j.info("Token obtenido: " + token);
	        System.out.println("Token obtenido: " + token);

	        // Obtener URLs de servicio
	        UDC urlservice = udcService.searchBySystemAndKey("URLSERVICE", "CRYOSERVICES");
	        UDC urlserviceSendRequest = udcService.searchBySystemAndKey("CRYOSERVICES", "SENDPLACCREQ");

	        // Log de URLs formadas
	        final String url = urlservice.getStrValue1() + urlserviceSendRequest.getStrValue1();
	        log4j.info("URL de servicio: " + url);
	        System.out.println("URL de servicio: " + url);

	        // Convertir el request a JSON
	        ObjectMapper jsonMapper = new ObjectMapper();
	        String jsonInString = jsonMapper.writeValueAsString(request);
	        log4j.info("JSON de la solicitud: " + jsonInString);
	        System.out.println("JSON de la solicitud: " + jsonInString);

	        // Configurar OkHttpClient
	        OkHttpClient client = new OkHttpClient().newBuilder().build();
	        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
	        RequestBody body = RequestBody.create(mediaType, jsonInString);

	        Request httpRequest = new Request.Builder()
	            .url(url)
	            .method("POST", body)
	            .addHeader("Authorization", "Bearer " + token)
	            .addHeader("Content-Type", "application/json")
	            .build();

	        // Enviar solicitud
	        Response response = client.newCall(httpRequest).execute();
	        String responseBody="";
	        // Manejo de la respuesta
	        if (response.isSuccessful()) {
	             responseBody = response.body().string();
	            log4j.info("Respuesta de envío de solicitud de acceso a planta: " + responseBody);
	            System.out.println("Respuesta de envío de solicitud de acceso a planta: " + responseBody);
	            if(responseBody.equals("Registro guardado correctamente")) {
	            	return "ok";
	            }else {
	            	log4j.error("Error en la respuesta: " + response.code() + " - " + responseBody);
	            }
	        } else {
	        	responseBody = response.body().string();
	            log4j.error("Error en la respuesta: " + response.code() + " - " + responseBody);
	            System.out.println("Error en la respuesta: " + response.code() + " - " + responseBody);
	        }
	        String mensaje=new Gson().fromJson(responseBody, JsonObject.class).get("mensaje").getAsString();
	        
	        response.close();
	        return mensaje;
	    } catch (IOException ex) {
	        log4j.error("Excepción IO: " + ex.getMessage(), ex);
	        System.out.println("Excepción IO: " + ex.getMessage());
	    } catch (Exception e) {
	        log4j.error("Error inesperado: " + e.getMessage(), e);
	        System.out.println("Error inesperado: " + e.getMessage());
	    }

	    return null;
	}
	
	public String getTokenCryoinfra() {
		
		
		try {
		UDC urlservice=udcService.searchBySystemAndKey("URLSERVICE","CRYOSERVICES" );
		
		UDC urlservicegettoken=udcService.searchBySystemAndKey("CRYOSERVICES","GETTOKEN" );
		
		
		// comprobar la duracion de token
		
		Date actu=urlservicegettoken.getUpdatedDate();
		
		
		// Fecha y hora actual
        Date now = new Date();
        
        // Convertir Date a Instant
        Instant startInstant = actu.toInstant();
        Instant endInstant = now.toInstant();
        
        // Calcular la duración entre las dos fechas
        Duration duration = Duration.between(startInstant, endInstant);
        
        // Obtener la duración en minutos
        long minutes = duration.toMinutes();
		
		if(minutes>55) {
			 log4j.info("se obtiene token por tiempo limite");
			 try {
		            // Crear los encabezados HTTP
		            HttpHeaders httpHeaders = new HttpHeaders();
		            httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		            // Crear la cadena JSON
		            String jsonInString = "{\"ClaveUsuario\":\"" + urlservicegettoken.getKeyRef() + "\",\"Password\":\"" + urlservicegettoken.getSystemRef() + "\"}";

		            // Crear la URL de destino
		            final String url = urlservice.getStrValue1() + urlservicegettoken.getStrValue2();

		            // Imprimir URL y JSON para depuración
		            System.out.println("URL: " + url);
		            System.out.println("JSON: " + jsonInString);

		            // Crear la entidad HTTP
		            HttpEntity<String> httpEntity = new HttpEntity<>(jsonInString, httpHeaders);

		            // Crear una instancia de RestTemplate
		            RestTemplate restTemplate = new RestTemplate();

		            // Enviar la solicitud HTTP
		            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
		            HttpStatus statusCode = responseEntity.getStatusCode();

		            // Verificar el código de estado de la respuesta
		            if (statusCode == HttpStatus.OK) {
		                String body = responseEntity.getBody();
		                log4j.info("Respuesta de token: "+ body);
		                if (body != null) {
		                    ObjectMapper mapper = new ObjectMapper();
		                    ResponseServiceCryoDTO response = new Gson().fromJson(body, ResponseServiceCryoDTO.class);

		                    if (response.isOk()) {
		                        urlservicegettoken.setStrValue1(response.getAccesToken().getToken());
		                        udcService.update(urlservicegettoken, new Date(), "OBTGETTOKEN");
		                        System.out.println("Token: " + response.getAccesToken().getToken());
		                        return urlservicegettoken.getStrValue1();
		                    } else {
		                        System.err.println("Response not OK");
		                    }
		                } else {
		                    System.err.println("Response body is null");
		                }
		            } else {
		                System.err.println("Unexpected status code: " + statusCode);
		            }
		        } catch (HttpClientErrorException e) {
		            System.err.println("HTTP error: " + e.getStatusCode());
		            System.err.println("Response body: " + e.getResponseBodyAsString());
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			 
		}else {
			
			return urlservicegettoken.getStrValue1();
		}
		
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
		return null;
	}
	
	/**
	 * Normaliza una cadena para comparación: trim, uppercase, quita espacios extras y guiones
	 * @param value Cadena a normalizar
	 * @return Cadena normalizada o null si es vacía
	 */
	private String normalizeForComparison(String value) {
		if(value == null || value.trim().isEmpty()) {
			return null;
		}
		return value.trim().toUpperCase().replaceAll("\\s+", "").replaceAll("-", "");
	}
	
	
	
}
