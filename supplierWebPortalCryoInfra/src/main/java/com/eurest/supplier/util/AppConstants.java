package com.eurest.supplier.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public final class AppConstants {

    public enum STATUS{
	   	 STATUS_OC_RECEIVED,
	   	 STATUS_OC_APPROVED,
	   	 STATUS_OC_SENT,
	   	 STATUS_OC_CLOSED,
	   	 STATUS_OC_INVOICED,
	   	 STATUS_OC_PROCESSED,
	   	 STATUS_OC_PAID,
	   	 STATUS_OC_CANCEL;
    }

    EnumMap<STATUS, String> statusMap = new EnumMap<STATUS, String>(STATUS.class);
	
    public static final String USER_SMARTECH = "SMARTECH"; 
    
    public static final String INVOICE_FIELD = "Factura";
    
    public static final String NC_FIELD = "NotaCredito";
    
    public static final String NC_TC = "E";
    
    public static final String PAYMENT_FIELD = "ComplementoPago";
    
    public static final String INVOICE_FIELD_UDC = "FACTURA";
    
    public static final String NC_FIELD_UDC = "NOTACREDITO";
    
    public static final String PAYMENT_FIELD_UDC = "COMPLEMENTOPAGO";
    
    public static final String OTHER_FIELD = "Otros";
    
    public static final String MESSAGE_FIELD = "MESSAGE";
    
    public static final String WELCOME_FIELD = "HOMEPAGE";
    
    public static final String STATUS_LOADINV = "FAC_CARGADA";
    
    public static final String STATUS_LOADINV_VALIDATE = "POR_VALIDAR";
    
    public static final String STATUS_LOADNC = "NC_CARGADA";
    
    public static final String STATUS_LOADCP = "CP_CARGADO";
    
    public static final String STATUS_ACCEPT = "APROBADO";
    
    public static final String STATUS_INPROCESS = "PENDIENTE";
    
    public static final String STATUS_REJECT = "RECHAZADO";
    
    public static final String STATUS_PAID = "PAGADO";
    
    public static final String STATUS_COMPLEMENT = "COMPLEMENTO";
    
    public static final String STATUS_APPROVALFIRSTSTEP = "FIRST";
    
    public static final String STATUS_APPROVALSECONDSTEP = "SECOND";
    
    public static final String STATUS_APPROVALTHIRDSTEP = "THIRD";
    
    public static final String STATUS_APPROVALFOURTHSTEP = "FOURTH";
    
    public static final String STATUS_RECEIVED = "CERRADA";
    
    public static final String STATUS_PARTIAL = "PARCIAL";
    
    public static final String STATUS_UNCOMPLETE = "UNCOMPLETE";
    
    public static final String STATUS_COMPLETE = "COMPLETE";
    
    public static final String STATUS_PENDING_REPLICATION = "PENDING";
    
    public static final String STATUS_ERROR_REPLICATION = "ERROR";
    
    public static final String STATUS_SUCCESS_REPLICATION = "SUCCESS";
    
    public static final String STATUS_NOTSENT_REPLICATION = "NOTSENT";
    
    public static final String EMAIL_FROM = "portal-proveedores@smartechcloud-apps.com";
    
    public static final String CFDI_V3 = "3.3";
    
    public static final String CFDI_V4 = "4.0";
    
    public static final String NAMESPACE_CFDI_V3 = "http://www.sat.gob.mx/cfd/3";
    
    public static final String NAMESPACE_CFDI_V4 = "http://www.sat.gob.mx/cfd/4";
    
    public static final String START_PASS = "NuevoProv.29";
    
    public static final String START_PASS_ENCODED = "==a20$TnVldm9Qcm92LjI5";
    
    public static final String EMAIL_APPROVAL_SENT = "Su solicitud ha sido enviada para revisión. El número de ticket asociado es: ";
    
    public static final String EMAIL_APPROVAL_PURCHASE_MSG = "Su solicitud ha sido aprobada por el área de compras. <br /><br /> Notas: <br /> ";
    
    public static final String EMAIL_APPROVAL_CXP_MSG = "Su solicitud ha sido aprobada por el de CxP. <br /><br /> Notas: <br /> ";
    
    public static final String EMAIL_APPROVAL_CXPMGR_MSG = "Su solicitud ha sido aprobada por la directiva";
    
    public static final String EMAIL_APPROVAL_QA_MSG = "Su solicitud ha sido aprobada por el personal de Calidad";
    
    public static final String EMAIL_APPROVAL_APPROVED = "Su solicitud ha sido APROBADA. En los próximos días recibirá correo con los detalles de su ingreso al portal";
    
    public static final String EMAIL_APPROVAL_REJECTED = "Su solicitud ha sido RECHAZADA. Motivo: ";
    
    public static final String EMAIL_RECEIPT_COMPLETE = "El recibo ha sido completado para la Orden de Compra: ";
    
    public static final String EMAIL_RECEIPT_INCOMPLETE = "El recibo de los productos ha sido rechazado para la Orden de Compra: ";
    
    public static final String EMAIL_PURCHASE_NEW = "Estimado Proveedor. La siguiente orden de compra ha sido liberada. Favor de revisar los detalles en el Portal de Proveedores:<br /><br />_ORDERNUM_-_ORDERTYPE_<br /><br />_PORTALLINK_<br /><br />Cuenta: _ADDNUMBER_<br /> Razon Social: _SOCIALREASON_<br /><br />Estimated provider. The following purchase order has been released. Please review the details in the Supplier Portal:<br /><br />_ORDERNUM_-_ORDERTYPE_<br /><br />_PORTALLINK_<br /><br />Account: _ADDNUMBER_<br /> Company Name: _SOCIALREASON_";
    
    public static final String EMAIL_RECEIPT_NEW = "Estimado Proveedor. Se ha completado un recibo de producto o servicio. Favor de considerar la carga de su factura para el recibo número: <br /><br />";
    
    public static final String EMAIL_INVOICE_ACCEPTED = "Estimado Proveedor: <br /><br />Su factura se ha recibido correctamente.<br />La orden de compra asociada a su factura es:_INVOICE_<br /><br />Dear Supplier: <br /><br />Your invoice has been received correctly.<br />The purchase order associated with your invoice is :_INVOICE_";
    
    public static final String EMAIL_INVOICE_ACCEPTED_NOTIF = "Estimado Proveedor. Su factura ha sido ACEPTADA y enviada a pago. El uuid de la factura aceptada es: ";

    public static final String EMAIL_INVOICE_REJECTED_NOTIF = "Estimado Proveedor. Su factura ha sido RECHAZADA. El uuid de la factura rechazada es: _UUID_<br /><br />Notas:<br />_NOTES_<br /><br />Estimated provider. Your invoice has been REJECTED. The uuid of the rejected invoice is: _UUID_<br /><br />Notes:<br />_NOTES_<br /><br />";
    
    public static final String EMAIL_CN_ACCEPTED = "Estimado Proveedor. La nota de crédito ha sido cargada y enviada a revisión. La Orden de Compra asociada a su nota de crédito es:_DATA_ <br><br>Estimated provider. The credit note has been uploaded and sent for review. The Purchase Order associated with your credit note is:_DATA_ <br><br>";
    
    public static final String EMAIL_INVBATCH_ACCEPTED = "Estimado Proveedor. Las facturas han sido cargadas y enviadas a revisión. La Orden de Compra asociada a sus facturas es:_DATA_ <br /><br />Estimated provider. Invoices have been uploaded and sent for review. The Purchase Order associated with your invoices is:_DATA_";
    
    public static final String EMAIL_CNBATCH_ACCEPTED = "Estimado Proveedor. Las notas de crédito han sido cargadas y enviadas a revisión. La Orden de Compra asociada a sus notas de crédito es:_DATA_ <br /><br />Estimated provider. Credit notes have been uploaded and submitted for review. The Purchase Order associated with your credit notes is:_DATA_";
    
    public static final String EMAIL_FOREIGINVOICE_ACCEPTED = "Estimado colaborador, una factura de extranjeros ha sido cargada y enviada a revisión. La Orden de Compra asociada a su factura es: ";
    
    public static final String EMAIL_FOREIGINVOICE_RECEIVED = "Estimado colaborador, una factura de extranjeros ha sido recibida. La Orden de Compra asociada a su factura es:_DATA_ <br><br>Dear collaborator, an invoice from foreigners has been received. The Purchase Order associated with your invoice is:_DATA_";
    
    public static final String EMAIL_INVOICE_UPLOAD = "Notificación del portal: Una factura ha sido cargada y enviada a revisión. El proveedor y la Orden de Compra asociada es: ";
    
    public static final String EMAIL_INVOICEANDCN_ACCEPTED = "La factura y la nota de crédito han sido aceptadas para la Orden de Compra: ";
    
    public static final String EMAIL_INVOICE_NOPURCHASE_ACCEPTED = "Estimado Proveedor. Su factura ha sido aceptada para pago. La orden de compra asociada a su factura aceptada es:_DATA_ <br /><br />Estimated provider. Your invoice has been accepted for payment. The purchase order associated with your accepted invoice is:_DATA_";
    
    public static final String EMAIL_INVOICE_ACCEPTED_NOPAYMENT = "La factura se ha enviado a validación por el personal de Cuentas por Pagar. Se le notificará por correo en cuanto sea aceptada para pago. La orden de compra relacionada es: ";
    
    public static final String EMAIL_INVOICE_REJECTED = "La factura ha sido RECHAZADA para la Orden de Compra: _ORDERNUM_-_ORDERTYPE_<br /><br />Motivo: _REASON_<br /><br />_PORTALLINK_<br /><br />Póngase en contacto para conocer los detalles: _EMAILSUPPORT_<br /><br />The invoice has been REJECTED for Purchase Order: _ORDERNUM_-_ORDERTYPE_<br /><br />Reason: _REASON_<br /><br />_PORTALLINK_<br /><br />Contact for details: _EMAILSUPPORT_";
    
    public static final String EMAIL_INVBATCH_REJECTED = "Las facturas han sido RECHAZADAS para la Orden de Compra:_DATA_ <br><br>The invoices have been REJECTED for the Purchase Order:_DATA_ <br><br>";
    
    public static final String EMAIL_INVOICE_REJECTED_WITHOUT_OC = "La factura ha sido RECHAZADA (Sin Orden de Compra).";
    
    public static final String EMAIL_CNBATCH_REJECTED = "Las notas de crédito han sido RECHAZADAS para la Orden de Compra:_DATA_ <br /><br />The credit notes have been REJECTED for the Purchase Order:_DATA_ <br /><br />";
    
    public static final String EMAIL_CONTACT_SUPPORT = "<br /><br />Póngase en contacto para conocer los detalles: ";
    
    public static final String EMAIL_INVOICE_SUBJECT = "CryoInfra - Notificación del Portal de Proveedores. No Responder.";
    
    public static final String EMAIL_NEW_SUPPLIER_ACCEPT = "CryoInfra - Notificación de aceptación de portal de proveedores.";
    
    public static final String EMAIL_NEW_SUPPLIER_REJECT = "CryoInfra - Notificación de rechazo de portal de proveedores.";
    
    public static final String EMAIL_ACCEPT_SUPPLIER_NOTIFICATION = "Estimado proveedor: <br /> Su solicitud con número _NUMTICKET_, ha sido ACEPTADA.<br />Ponemos a su disposición las credenciales para poder acceder a nuestro portal _URL_: <br />Usuario: _USER_<br />Contraseña: _PASS_<br /><br />Dear supplier: <br /> Your request with number _NUMTICKET_, has been ACCEPTED.<br />We provide you with the credentials to access our portal _URL_: <br />Username: _USER_<br />Password: _PASS_<br /><br />"; 
    
    public static final String EMAIL_REJECT_SUPPLIER_NOTIFICATION = "Estimado proveedor: <br /> Su solicitud con número _NUMTICKET_, desafortunadamente ha sido RECHAZADA.<br />El motivo es: _REASON_ <br /> Favor de volver a ingresar de nuevo al portal _URL_ en la opción de Click aquí para registrarse como un nuevo proveedor.<br />Posteriormente en el campo de \"Num de ticket\" ingresar el ticket previamente mencionado, dar clic en Buscar y favor de modificar los datos que se han indicado previamente.<br /><br />Dear provider: <br /> Your request with number _NUMTICKET_, unfortunately has been REJECTED.<br />The reason is: _REASON_ <br /> Please re-enter the portal again _URL_ in the Click here option to register as a new supplier.<br />Later, in the \"Ticket number\" field, enter the previously mentioned ticket, click on Search and please modify the data previously indicated.<br /><br />";
    
    public static final String EMAIL_MASS_SUPPLIER_NOTIFICATION = "Estimado Proveedor. <br /><br />Usted ha sido aprobado para utilizar el Portal de Proveedores de la compañía CryoInfra. A continuación encontrará sus credenciales temporales de acceso.<br /><br />Estimated provider. <br /><br />You have been approved to use the CryoInfra Company Provider Portal. Below you will find your temporary access credentials.<br /><br />";
  
    public static final String EMAIL_FIRST_APP_SUBJECT = "CryoInfra - Solicitud de la 1a aprobación de proveedor.";
    
    public static final String EMAIL_FIRST_APP_CONTENT = "Estimado Aprobador:<br />La solicitud con número _NUMTICKET_ requiere de su aprobación.<br />Favor de revisar la información en el portal _URL_";

    public static final String EMAIL_SECOND_APP_SUBJECT = "CryoInfra - Solicitud de la 2da aprobación de proveedor.";
    
    public static final String EMAIL_SECOND_APP_CONTENT = "Estimado Aprobador:<br />La solicitud con número _NUMTICKET_ requiere de su aprobación.<br />Favor de revisar la información en el portal _URL_<br /><br />Dear Approver:<br />The request with number _NUMTICKET_ requires your approval.<br />Please review the information in the portal _URL_<br /><br />";
    
    public static final String EMAIL_THIRD_APP_SUBJECT = "CryoInfra - Solicitud de la 3ra aprobación de proveedor.";
    
    public static final String EMAIL_REQUEST_RECEIVED_SUBJECT = "CryoInfra - Solicitud de alta de proveedor recibida.";
    
    public static final String EMAIL_REQUEST_RECEIVED_CONTENT = "Estimado Proveedor:<br />Su solicitud con número _NUMTICKET_, ha sido RECIBIDA.<br /> En breve analizaremos la información proporcionada y nos contactaremos con usted.<br />Gracias por querer ser nuestro socio comercial.<br /><br />Dear Supplier:<br />Your request with number _NUMTICKET_ has been RECEIVED.<br /> We will analyze the information provided shortly and contact you.<br />Thank you for wanting to be our business partner.";
    
    public static final String EMAIL_REQUEST_UPDATE_SUBJECT = "CryoInfra - Solicitud de actualización de proveedor recibida.";
    
    public static final String EMAIL_REQUEST_UPDATE_CONTENT = "Estimado Proveedor:<br />Su solicitud con número _NUMTICKET_, ha sido RECIBIDA.<br /> En breve analizaremos la información proporcionada y nos contactaremos con usted.<br /><br /><br />Dear Supplier:<br />Your request with number _NUMTICKET_ has been RECEIVED.<br /> We will analyze the information provided shortly and contact you.<br />";
    
    public static final String EMAIL_MASS_SUPPLIER_CHANGE_NOTIFICATION = "Estimado Proveedor. <br /><br />La actualización de datos en el portal de proveedores ha sido revisada y aprobada por la compañía CryoInfra.<br /><br />Estimated provider. <br /><br />The data update in the supplier portal has been reviewed and approved by the CryoInfra company.<br /><br />";
    
    public static final String EMAIL_SUPPPLIER_NOTIFICATION_PURCHASE = "Tiene una solicitud de Alta de Proveedor por revisar y aprobar con ticket: ";
    
    public static final String EMAIL_DRAFT_SUPPPLIER_NOTIFICATION_PURCHASE = "Estimado proveedor <br /><br />Hemos recibido una solicitud de tipo borrador en nuestros sistemas. Su solicitud será procesada una vez que someta el formato de forma definitiva. <br /> <br /> Puede continuar actualizando sus datos utilizando el número de ticket que le enviamos a continuación ";
    
    public static final String EMAIL_MIDDLEWARE_MSG_1 = "Estimado proveedor,<br /><br />Su orden de compra _ORDERNUM_ ha sido marcada como recibida en nuestro sistema.<br /><br />Puede proceder a cargar su factura en el portal utilizando los siguientes datos:<br /><br />- Número de recibo: _DOCNUM_<br />- Orden de compra: _ORDERNUM_-_ORDERTYPE_<br /><br />Le recordamos que nuestro portal de proveedores está disponible en: _PORTALLINK_<br />NOTA: No responda a este correo, ha sido enviado desde una cuenta no monitoreada.<br /><br />---<br />Dear supplier,<br /><br />Your purchase order _ORDERNUM_ has been marked as received in our system.<br /><br />You can proceed to upload your invoice on the portal using the following data:<br / ><br />- Receipt number: _DOCNUM_<br />- Purchase order: _ORDERNUM_-_ORDERTYPE_<br /><br />We remind you that our supplier portal is available at: _PORTALLINK_<br / >NOTE: Do not reply to this email, it has been sent from an unmonitored account.<br /><br />";
    
    public static final String EMAIL_MIDDLEWARE_MSG_2 = "Estimado proveedor,<br /><br />La siguiente orden de compra ha sido marcado como cancelada en nuestro sistema.<br /><br />- Orden de compra: _ORDERNUM_-_ORDERTYPE_<br /><br />Le recordamos que nuestro portal de proveedores está disponible en: _PORTALLINK_<br />NOTA: No responda a este correo, ha sido enviado desde una cuenta no monitoreada.<br /><br />---<br />Dear supplier,<br /><br />The following purchase order has been marked as canceled in our system.<br /><br />- Purchase order: _ORDERNUM_-_ORDERTYPE_<br /><br />We remind you that our supplier portal is available at: _PORTALLINK_<br />NOTE: Do not reply to this email, it has been sent from an unmonitored account.<br /><br />";
    
    public static final String EMAIL_MIDDLEWARE_MSG_3 = "Estimado usuario,<br /><br />La siguiente orden de compra ha sido marcado como cancelada en nuestro sistema.<br /><br />- Proveedor: _SUPNUM_-_SUPNAME_<br />- Orden de compra: _ORDERNUM_-_ORDERTYPE_<br /><br />Le recordamos que nuestro portal de proveedores está disponible en: _PORTALLINK_<br />NOTA: No responda a este correo, ha sido enviado desde una cuenta no monitoreada.<br /><br />---<br />Dear user,<br /><br />The following purchase order has been marked as canceled in our system.<br /><br />- Supplier: _SUPNUM_-_SUPNAME_<br />- Purchase order: _ORDERNUM_-_ORDERTYPE_<br /><br />We remind you that our supplier portal is available at: _PORTALLINK_<br />NOTE: Do not reply to this email, it has been sent from an unmonitored account.<br /><br />";
    
    public static final String EMAIL_MIDDLEWARE_MSG_4 = "Estimado proveedor,<br /><br />El siguiente recibo ha sido marcado como cancelado en nuestro sistema.<br /><br />- Orden de compra: _ORDERNUM_-_ORDERTYPE_<br />- Número de recibo: _DOCNUM_<br /><br />Le recordamos que nuestro portal de proveedores está disponible en: _PORTALLINK_<br />NOTA: No responda a este correo, ha sido enviado desde una cuenta no monitoreada.<br /><br />---<br />Dear supplier,<br /><br />The following receipt has been marked as canceled in our system.<br /><br />- Purchase order: _ORDERNUM_-_ORDERTYPE_<br />- Receipt number: _DOCNUM_<br /><br />We remind you that our supplier portal is available at: _PORTALLINK_<br />NOTE: Do not reply to this email, it has been sent from an unmonitored account.<br /><br />";
    
    public static final String EMAIL_MIDDLEWARE_MSG_5 = "Estimado usuario,<br /><br />El siguiente recibo ha sido marcado como cancelado en nuestro sistema.<br /><br />- Proveedor: _SUPNUM_-_SUPNAME_<br />- Orden de compra: _ORDERNUM_-_ORDERTYPE_<br />- Número de recibo: _DOCNUM_<br /><br />Le recordamos que nuestro portal de proveedores está disponible en: _PORTALLINK_<br />NOTA: No responda a este correo, ha sido enviado desde una cuenta no monitoreada.<br /><br />---<br />Dear user,<br /><br />The following purchase order has been marked as canceled in our system.<br /><br />- Supplier: _SUPNUM_-_SUPNAME_<br />- Purchase order: _ORDERNUM_-_ORDERTYPE_<br />- Receipt number: _DOCNUM_<br /><br />We remind you that our supplier portal is available at: _PORTALLINK_<br />NOTE: Do not reply to this email, it has been sent from an unmonitored account.<br /><br />";
    
    public static final String EMAIL_MIDDLEWARE_MSG_6 = "Estimado usuario,<br /><br />El siguiente recibo ha sido marcado como cancelado en nuestro sistema.<br /><br />- Proveedor: _SUPNUM_-_SUPNAME_<br />- Orden de compra: _ORDERNUM_-_ORDERTYPE_<br />- Número de recibo: _DOCNUM_<br />- Factura Asignada: _UUIDINV_<br /><br />Le recordamos que nuestro portal de proveedores está disponible en: _PORTALLINK_<br />NOTA: No responda a este correo, ha sido enviado desde una cuenta no monitoreada.<br /><br />---<br />Dear user,<br /><br />The following purchase order has been marked as canceled in our system.<br /><br />- Supplier: _SUPNUM_-_SUPNAME_<br />- Purchase order: _ORDERNUM_-_ORDERTYPE_<br />- Receipt number: _DOCNUM_<br />- Assigned Invoice: _UUIDINV_<br /><br />We remind you that our supplier portal is available at: _PORTALLINK_<br />NOTE: Do not reply to this email, it has been sent from an unmonitored account.<br /><br />";
    
    public static final String NN_MODULE_PURCHASE = "PURCHASE";
    
    public static final String NN_MODULE_RECEIPT = "RECEIPT";
    
    public static final String NN_MODULE_VOUCHER = "VOUCHER";
    
    public static final String NN_MODULE_BATCHJOURNAL = "BATCHJOURNAL";
    
    public static final String NN_MODULE_ADDRESSBOOK = "ADDRESSBOOK";
 
    public static final String NN_MODULE_SUPPLIER = "SUPPLIER";
    
    public static final String NN_MODULE_INVBATCH = "INVBATCH";
    
    public static final String ROLE_SUPPLIER = "SUPPLIER";
    
    public static final String ROLE_PURCHASE = "ROLE_PURCHASE";
    
    public static final String ROLE_TAX = "ROLE_TAX";
    
    public static final String ROLE_SFT = "ROLE_SFT";
    
    public static final String ROLE_SUPPLIER_EMAIL = "ROLE_SUPPLIER";
    
    public static final String ROLE_PURCHASE_VALID = "ROLE_SUPPLIER";
    
	 public static final String CFDI_VALIDATION_URL = "https://consultaqr.facturaelectronica.sat.gob.mx/ConsultaCFDIService.svc";
	 public static final String CFDI_VALIDATION_ACTION = "http://tempuri.org/IConsultaCFDIService/Consulta";
    
    public static final String CFDI_SUCCESS_MSG = "S - Comprobante obtenido satisfactoriamente.";
    
    public static final String CFDI_SUCCESS_MSG_ACTIVE = "Vigente";
    
    public static final String STATUS_OC_RECEIVED = "OC RECIBIDA";
    
    public static final String STATUS_OC_APPROVED = "OC APROBADA";
    
    public static final String STATUS_OC_SENT = "OC ENVIADA";
    
    public static final String STATUS_OC_CLOSED = "OC CERRADA";
    
    public static final String STATUS_OC_INVOICED = "OC FACTURADA";
    
    public static final String STATUS_OC_PROCESSED = "OC PROCESADA";
    
    public static final String STATUS_OC_PAID = "OC PAGADA";
    
    public static final String STATUS_GR_PAID = "P";
    
    public static final String STATUS_OC_PAYMENT_COMPL = "OC COMPLEMENTO";
    
    public static final String STATUS_OC_CANCEL = "OC CANCELADA";
    
    public static final String STATUS_OC_FOREIGN = "OC EXTRANJERO";
    
    public static final String STATUS_CANCEL = "CANCELADO";
    
    public static final String STATUS_FACT_FOREIGN = "FACT EXTRANJERO";
    
    public static final String RECEIPT_MASSIVE_UPLOAD = "CARGA MASIVA";
    
    public static final String SAT_CFDIVER = "CFDI33";
    
    public static final String SAT_NONCOMPLANCE_URL = "http://omawww.sat.gob.mx/cifras_sat/Documents/Listado_Completo_69-B.csv";
    
    public static final String REF_METODO_PAGO = "PPD";
    
    public static final String REF_METODO_PAGO_PUE = "PUE";
    
    public static final String REF_FORMA_PAGO = "99";
    
    public static final String USO_CFDI = "P01";
    
    public static final String CANCEL_JDE_STS = "999";
    
    public static final String JDE_RETENTION_CODE = "2171";
    
    public static final String RECEIPT_CODE_INVOICE = "I";
    
    public static final String RECEIPT_CODE_RETENTION = "R";
    
    public static final String RECEIPT_CODE_CREDIT_NOTE = "N";
    
    public static final String URL_HOST = "https://3.222.152.41";
    
    public static final String STATUS_FACT = "FACTURA";
    
    public static final String EVIDENCE_FIELD = "Evidencia";
    
    public static final String URL_UPDATE_ORDERSTATUS = "http://localhost:8081/supplierWebPortalRestCryoInfra/poUpdateOrderStatus";
    
    public static final String EMAIL_PORTAL_LINK = "https://3.222.152.41/supplierWebPortalCryoInfra";
    
    public static final String EMAIL_NEW_SUPPLIER_SUBJECT = "CryoInfra - Solicitud Alta de Proveedor";
    
    public static final String EMAIL_DRAFT_SUPPLIER_SUBJECT = "CryoInfra - Registro como borrador en Alta de Proveedor. Ticket ";
    
    public static final String EMAIL_NEXT_NOTIFICATION = "Tiene una solicitud de aprobación de un nuevo proveedor en el portal. El número de ticket asociado es: ";      
    
    public static final String EMAIL_APPROVED_SUPPLIER_SUBJECT = "CryoInfra - Solicitud Alta de Proveedor Aprobada";
    
    public static final String EMAIL_NEW_ORDER_NOTIF = "CryoInfra - Nueva Orden de Compra No. ";
    
    public static final String EMAIL_NEW_RECEIPT_NOTIF = "CryoInfra - Nuevo Recibo para la Orden de Compra No. ";
    
    public static final String EMAIL_PAYMENT_RECEIPT_NOTIF = "CryoInfra - Notificación de pago de factura. ";
    
    public static final String EMAIL_RECEIPT_CANCELED_NOTIF = "CryoInfra - Notificación de cancelación de recibo. ";
    
    public static final String EMAIL_PO_CANCELED_NOTIF = "CryoInfra - Notificación de cancelación de orden de compra. ";
    
    public static final String EMAIL_PAYMENT_RECEIPT_NOTIF_CONTENT = "Estimado proveedor <br /><br /> A través de este medio le notificamos el pago de la siguiente factura: <br/><br /> Uuid: _UUID_ <br /> Recibo(GR) No.: _GR_ <br /> Orden de Compra No.: _PO_ <br /> Fecha de pago: _DATE_ <br /> Importe: _AMOUNT_<br />Id del pago: _PID_<br /> <br />Dear supplier <br /><br /> Through this means we notify you of the payment of the following invoice: <br/><br /> Uuid: _UUID_ <br /> Receipt (GR) No.: _GR_ <br / > Purchase Order No.: _PO_ <br /> Payment date: _DATE_ <br /> Amount: _AMOUNT_<br />Payment ID: _PID_<br /> <br />";
    
    public static final String EMAIL_PAYMENT_NO_OC_NOTIF_CONTENT = "Estimado proveedor <br /><br /> A través de este medio le notificamos el pago de la siguiente factura: <br/><br /> Uuid: _UUID_ <br /> Fecha de pago: _DATE_ <br /> Importe: _AMOUNT_<br />Id del pago: _PID_<br /> <br />Dear supplier <br /><br /> Through this means we notify you of the payment of the following invoice: <br/><br /> Uuid: _UUID_ <br /> Payment date: _DATE_ <br /> Amount: _AMOUNT_<br />Payment ID: _PID_<br /> <br />";
    
    public static final String EMAIL_NO_COMPLIANCE_INVOICE = "CryoInfra - Proveedor en lista negra. ";
    
    public static final String EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER = "El siguiente proveedor intentó ingresar una factura, sin embargo, se detectó que se encuentra en la lista negra: <br /><br/>Número: _ADDNUMBER_<br /> Nombre: _RAZONSOCIAL_<br />The following supplier tried to enter an invoice, however, it was detected that it is in the blacklist: <br /><br/>Number: _ADDNUMBER_<br /> Name: _RAZONSOCIAL_<br />";
    
    
    public static final String EMAIL_REVOKED_OR_EXPIRED_CERTIFICATE = "Certificado Revocado o Caduco. ";
    
    public static final String EMAIL_REVOKED_OR_EXPIRED_CERTIFICATE_CONTENT = "Servicio de Descarga Masiva - Certificado Revocado o Caduco. ";
    
    public static final String EMAIL_INVALID_CERTIFICATE = "Certificado Inválido. ";
    
    public static final String EMAIL_INVALID_CERTIFICATE_CONTENT = "Servicio de Descarga Masiva - Certificado Inválido. ";
        
    public static final String EMAIL_NO_COMPLIANCE_INVOICE_SUPPLIER_NOTIF = "Estimado proveedor <br /> Su factura ha sido rechazada debido a que hemos detectado que existen problemas con su situación fiscal:  <br/><br/>Dear supplier <br /> Your invoice has been rejected because we have detected that there are problems with your tax situation: <br/><br/>";    
    
    public static final String DEFAULT_CURRENCY = "MXN";
    
    public static final String DEFAULT_CURRENCY_JDE = "MXP";
    
    public static final String EMAIL_INV_ACCPET_BUYER = "CryoInfra - Solicitud de Revisión y Aprobación de Factura para la Orden de Compra No. ";
    
    public static final String EMAIL_INV_ACCEPT_SUP = "CryoInfra - Notificación de aceptación del portal de proveedores para la Orden de Compra No. ";
    
    public static final String EMAIL_NC_ACCEPT_SUP = "CryoInfra - Nota de crédito recibida para la Orden de Compra No. ";
    
    public static final String EMAIL_INV_REJECT_SUP = "CryoInfra - Factura RECHAZADA para la Orden de Compra No. ";
    
    public static final String EMAIL_INV_REQUEST_NO_OC = "CryoInfra - Solicitud de Factura sin Orden de Compra";    
    
    public static final String EMAIL_INV_REQUEST_OC = "CryoInfra - Solicitud de Factura con Orden de Compra";  
    
    public static final String EMAIL_INV_REQUEST_OP = "CryoInfra - Solicitud de Factura con Nota de Credito";  
    
    public static final String EMAIL_FREIGHT_REQUEST = "CryoInfra - Solicitud de Factura de Fletes";  
    
    public static final String EMAIL_INV_APPROVAL_MSG_1_NO_OC = "Tiene una solicitud de aprobación de una factura sin orden de compra en el Portal de Proveedores. Con el UUID: _UUID_ del proveedor:_SUPPLIER_<br /><br />You have a request for approval of an invoice without a purchase order in the Supplier Portal. With the UUID: _UUID_ of the supplier:_SUPPLIER_<br /><br />";
    
    public static final String EMAIL_INV_APPROVAL_MSG_1_OC = "Tiene una solicitud de aprobación de una factura con orden de compra _ORDERNUM_ del proveedor _ADDNUMBER_<br /><br />You have a request for approval of an invoice with purchase order _ORDERNUM_ from supplier _ADDNUMBER_<br /><br />";

    public static final String EMAIL_INV_APPROVAL_MSG_1_OP = "Tiene una solicitud de aprobación de una NOTA DE CREDITO con orden _ORDERNUM_ del proveedor _ADDNUMBER_<br /><br />You have a request for approval of a NOTE CREDIT purchase order _ORDERNUM_ from supplier _ADDNUMBER_<br /><br />";
    
    public static final String EMAIL_INV_APPROVAL_MSG_2_OC = " del proveedor ";
    
    public static final String EMAIL_INV_APPROVAL_MSG_1_FREIGHT = "Tiene una solicitud de aprobación de una factura de fletes. No. batch:<br /><br />You have a request for approval of a freight bill. No. Batch:<br /><br />";
    
    public static final String EMAIL_INV_APPROVAL_MSG_2_NO_OC = " del proveedor ";
    
    public static final String EMAIL_INV_APPROVAL_MSG_2_NO_FREIGHT = " del proveedor ";
    
    public static final String EMAIL_INV_REJECT_SUP_NO_OC = "CryoInfra - Factura RECHAZADA sin Orden de Compra";
    
    public static final String EMAIL_NC_REJECT_SUP_NO_OC = "CryoInfra - Nota de Crédito RECHAZADA ";
    
    public static final String EMAIL_INV_APPROVED_SUP = "CryoInfra - Factura ACEPTADA para la Orden de Compra No. ";
    
    public static final String EMAIL_INV_APPROVED_SUP_NO_OC = "CryoInfra - Factura ACEPTADA sin Orden de Compra";
    
    public static final String EMAIL_INV_PAYMENT_SUP = "CryoInfra - Notificación de Factura PAGADA ";
    
    public static final String EMAIL_INV_BATCH_ACCEPT_SUP = "CryoInfra - Facturas Recibidas para la Orden de Compra No. ";
    
    public static final String EMAIL_NC_BATCH_ACCEPT_SUP = "CryoInfra - Notas de Crédito Recibidas para la Orden de Compra No. ";
    
    public static final String EMAIL_INV_BATCH_REJECT_SUP = "CryoInfra - Facturas RECHAZADAS para la Orden de Compra No. ";
    
    public static final String EMAIL_NC_BATCH_REJECT_SUP = "CryoInfra - Notas de Crédito RECHAZADAS para la Orden de Compra No. ";
  
    public static final String PASS_RESET = "CryoInfra - Notificación de Cambio de Contraseña";
    
    public static final String EMAIL_PASS_RESET_NOTIFICATION = "Estimado Proveedor. <br />Usted ha solicitado un cambio de contraseña. Ingrese al portal con las siguientes credenciales y posteriormente renueve su contraseña<br /><br />Credenciales temporales de acceso: <br /><br />Estimated provider. <br />You have requested a password change. Enter the portal with the following credentials and then renew your password<br /><br />Temporary access credentials: <br />";
    
    public static final String EMAIL_REASIGN_SUBJECT = "CryoInfra - Reasignado:Solicitud de revisión y aprobación de proveedores";
    
    public static final String EMAIL_REASIGN_CONTENT = "Estimado Aprobador:<br />Usted ha sido reasignado para revisar y validar nueva información de proveedores que ha sido registrada en el Portal de Alta de Proveedores. La solicitud con número _NUMTICKET_ requiere de su revisión y aprobación.<br />Consulte los detalles utilizando el siguiente enlace: _URL_";
    
    public static final String EMAIL_REASIGN_SUBJECT_FLETES = "CryoInfra - Reasignado:Solicitud de revisión y aprobación de Fletes";
    
    public static final String EMAIL_REASIGN_CONTENT_FLETE = "Estimado Aprobador:<br />Usted ha sido asignado para revisar y validar nueva información de Fletes que ha sido registrada en el Portal Fletes. La solicitud con número _NUMTICKET_ requiere de su revisión y aprobación.<br />Consulte los detalles utilizando el siguiente enlace: _URL_";
    
    public static final String NO_VALIDATE_DATE = "DATE";
    
    public static final String PROPINA_TEXT = "/PROPINA";
    
    public static final String DOCTYPE_REGALIAS = "RE";
    
    public static final String DOCTYPE_PROD = "GR";
    
    public static final String DOCTYPE_PUB1 = "P1";
    
    public static final String DOCTYPE_PUB2 = "P2";
    
    public static final String DOCTYPE_OPS = "GR";
    
    public static final String FACTURA_PUE = "PUE";
    
    public static final String OFFSET_DAYS = "OFFSET";
    
    public static final String LOG_INVREJECTED_TITLE = "RECHAZO_FACTURA";
    
    public static final String LOG_INVREJECTED_MDG = "La factura de la siguiente orden ha sido rechazada: ";
    
    public static final String LOG_REASIGN_TITLE = "REASIGNAR_ORDEN";
    
    public static final String LOG_REASIGN_MSG = "La orden ORDER_NUMBER ha sido reasignada a la cuenta NEW_ORDER_EMAIL: ";
    
    public static final String LOG_BATCH_PROCESS = "Proceso BATCH";
    
    public static final String LOG_DOCUMENTS = "CARGA_DOCUMENTOS";    
    
    public static final String LOG_BATCH_PROCESS_CODSAT = "El proceso de carga de los códigos de SAT ha finalizado exitosamente. Registros: ";

    public static final String LOG_BATCH_PROCESS_MASS_LOAD = "Carga Masiva BATCH";
    
    public static final String LOG_BATCH_PROCESS_MASS_LOAD_MSG = "Se asigna con éxito la factura INVOICE_NUMBER a la Orden de Compra No. DOCUMENT_NUMBER-DOCUMENT_TYPE del proveedor ADDRESS_NUMBER.";
    
    public static final String LOG_BATCH_PROCESS_MASS_LOAD_ERR =  "Ha ocurrido un error al asignar la factura INVOICE_NUMBER a la Orden de Compra No. DOCUMENT_NUMBER-DOCUMENT_TYPE del proveedor ADDRESS_NUMBER.";
    
    public static final String FTP_FILEPATH = "/PROD/FACTURAS/INPUT/";
    
    public static final String FTP_FILEPATH_OTHERS = "/PROD/FACTURAS/OTROS/";
    
    public static final String FTP_FILEPATH_COMPLETE = "/PROD/FACTURAS/OUTPUT/";
    
    public static final String FTP_FILEPATH_ERROR = "/PROD/FACTURAS/ERROR/";
    
    public static final String LOG_FTP_PROCESS = "FACTURAS_FTP";
    
    public static final String LOCAL_COUNTRY_CODE = "MX";
    
    public static final String NON_COMPLIANCE_ACCEPT = "Sentencia Favorable";
    
    public static final String NON_COMPLIANCE_REJECT_1 = "Definitivo";
    
    public static final String NON_COMPLIANCE_REJECT_2 = "Presunto";
    
    public static final String NON_COMPLIANCE_REJECT_3 = "Desvirtuado";
    
    public static final String LOG_DELETE_DOC = "ELIMINAR_DOCTO";
    
    public static final String LOG_DELDOC_MSG = "El documento DOC_NAME parteneciente a la orden ORDER_NUMBER ha sido eliminado por USER_NAME ";
    	
    public static final String ETHIC_CONTENT = "CÓDIGO DE ÉTICA <br />Estimado Usuario: <br /> " + 
    		"En nombre de CryoInfra agradezco tu interés en nuestro sitio de Tips Anónimos. Para nosotros es muy importante contar <br /> " + 
    		"con medios que nos permitan detectar conductas que vayan en contra de nuestro Código de ética, fortaleciendo nuestros principios y valores. <br /> " +  
    		"Para garantizar la confidencialidad y anonimato de tus denuncias, hemos contratado a Deloitte, una firma con más de 10 años de experiencia  <br /> " + 
    		"en el servicio. Ponemos a tu disposición los siguientes medios:  <br /> " + 
    		"1.Línea telefónica sin costo: 01 800 999 0784   <br />" +
    		"2.Página Web: https://www.tipsanonimos.com/eticaCryoInfra/   <br /> " + 
    		"3.Email: eticaCryoInfra@tipsanonimos.com   <br /> " + 
    		"4.Fax: 01 (55) 5255 1322   <br /> " + 
    		"5.Apartado Postal: Galaz, Yamazaki, Ruiz Urquiza, S.C., A.P. (CON-080), Ciudad de México, CP 06401  <br />" +
    		"Muchas Gracias,<br />" +
    		"Director General<br /><br />CODE OF ETHICS <br />Dear User: <br />" + 
    		"On behalf of CryoInfra, I thank you for your interest in our Tips Anonymous site. For us it is very important to tell <br />" + 
    		"with means that allow us to detect behaviors that go against our Code of Ethics, strengthening our principles and values. <br />" + 
    		"To guarantee the confidentiality and anonymity of your complaints, we have hired Deloitte, a firm with more than 10 years of experience <br />" + 
    		"in the service. We put at your disposal the following means: <br />" + 
    		"1. Toll-free telephone line: 01 800 999 0784 <br />" + 
    		"2. Website: https://www.tipsanonymos.com/eticaCryoInfra/ <br />" + 
    		"3.Email: eticaCryoInfra@tipsanonymos.com <br />" + 
    		"4.Fax: 01 (55) 5255 1322<br /> " + 
    		"5. PO Box: Galaz, Yamazaki, Ruiz Urquiza, S.C., A.P. (CON-080), Mexico City, CP 06401 <br />" + 
    		"Thank you very much,<br />" + 
    		"Managing Director<br /><br />";

	public static final String FILE_EXT_XML = "xml";	
	
	public static final String FILE_EXT_PDF = "pdf";
	
    public static final String FISCAL_DOC_APPROVED = "APROBADO";
    
    public static final String FISCAL_DOC_REJECTED = "RECHAZADO";
    
    public static final String FISCAL_DOC_PENDING = "PENDIENTE";    
    
    public static final String FISCAL_DOC_OTHER = "OTHER";

    public static final String APPROVE_MAIL_SUBJECT_INVOICE = "CryoInfra - Solicitud de Aprobación Factura. OC ";
    
    public static final String FISCAL_DOC_MAIL_SUBJECT_INVOICE = "CryoInfra - Aprobación Factura sin Orden de Compra";
    
    public static final String FISCAL_DOC_MAIL_SUBJECT_INVOICE_OC = "CryoInfra - Aprobación Factura con Orden de Compra";
    
    public static final String FISCAL_DOC_MAIL_SUBJECT_INVOICE_FREIGHT = "CryoInfra - Aprobación Factura de Fletes";
    
    public static final String FISCAL_DOC_MAIL_SUBJECT_NC = "CryoInfra - Aprobación Nota de Credito";
    
    public static final String FISCAL_DOC_MAIL_MSJ_INVOICE = "Su factura sin orden de compra con el UUID: _UUID_ en el Portal de Proveedores fue aprobada con exito.<br /><br />Your invoice without purchase order with the UUID: _UUID_ in the Supplier Portal was successfully approved.<br /><br />";
    
    public static final String FISCAL_DOC_MAIL_MSJ_INVOICE_OC = "Su factura con orden de compra _ORDERNUM_ en el Portal de Proveedores fue aprobada con exito.<br /><br />Your invoice with purchase order _ORDERNUM_ in the Supplier Portal was successfully approved.<br /><br />";
    
    public static final String FISCAL_DOC_MAIL_MSJ_INVOICE_FREIGHT = "Su factura de fletes del proveedor _NAME_ en el Portal de Proveedores fue aprobada con exito.<br /><br />Your freight invoice from supplier _NAME_ in the Supplier Portal was successfully approved.<br /><br />";
    
    public static final String FISCAL_DOC_MAIL_MSJ_NC = "Su Nota de Credito Con el UUID: _UUID_ en el Portal de Proveedores fue aprobada con exito.<br /><br />Your Credit Note without purchase order With the UUID: _UUID_ in the Supplier Portal was successfully approved.<br /><br />";
    
    public static final String FISCAL_DOC_MAIL_MSJ_APPROVED = " en el Portal de Proveedores fue aprobada con exito.<br /><br />";
    
    public static final String FISCAL_DOC_MAIL_MSJ_REJECTED = "Su Nota de Credito Con el UUID: _UUID_ en el Portal de Proveedores fue rechazada con el siguiente motivo.  <br /><br />_NOTES_<br /><br />Your Credit Note With the UUID: _UUID_ in the Supplier Portal was rejected with the following reason. <br /><br />_NOTES_<br /><br />";
    
    public static final String FISCAL_DOC_MAIL_MSJ_INVOICE_SHOP_AREA = "Una factura sin orden de compra con el UUID: ";
    
    public static final String FISCAL_DOC_MAIL_MSJ_NC_SHOP_AREA = "Una Nota de Credito sin orden de compra con el UUID: ";
    
    public static final String FISCAL_DOC_MAIL_MSJ_SHOP_AREA_2 = " del proveedor ";
    
    public static final String FISCAL_DOC_MAIL_MSJ_SHOP_AREA_3 = " fue aprobada con exito, continuar con el proceso para envio a JDE <br /><br />";
    
    public static final String PROGRAM_ID_ZP0411Z1 = "ZP0411Z1";
    
    public static final String WORK_STN_ID_COBOWB04 = "COBOWB04";
    
    public static final String EXPLANATION_REMARK = "PORTAL AGENTES ADUANALES";
    
    public static final String EXPLANATION_REMARK_FREIGHT = "PORTAL PROVEEDORES";
    
    public static final String INVOICE_TAX0 = "MX0";
    
    public static final String INVOICE_TAX_RATE_TAX0 = "0.000000";
    
    public static final String CURRENCY_MODE_DOMESTIC = "D";
    
    public static final String CURRENCY_MODE_FOREIGN = "F";
    
    public static final String GL_OFFSET_DEFAULT = "100";
    
    public static final String GL_OFFSET_FOREIGN = "200";
    
    public static final String INV_FIRST_APPROVER = "FIRST_APPROVER";
    
    public static final String INV_SECOND_APPROVER = "SECOND_APPROVER";
    
    public static final String INV_THIRD_APPROVER = "THIRD_APPROVER";
    
    public static final String INV_FOURTH_APPROVER = "FOURTH_APPROVER";
    
    public static final String FIRST_STEP = "FIRST";
    
    public static final String SECOND_STEP = "SECOND";
    
    public static final String THIRD_STEP = "THIRD";
    
    public static final String FOURTH_STEP = "FOURTH";
    
    public static final String STATUS_APPROVALFINALSTEP = "FINAL";
    
    public static final String FINAL_STEP = "FINAL";
    
    public static final String CONCEPT_001 = "CNT";
    public static final String CONCEPT_002 = "Validation";
    public static final String CONCEPT_003 = "Maneuvers";
    public static final String CONCEPT_004 = "Deconsolidation";
    public static final String CONCEPT_005 = "RedManeuvers";
    public static final String CONCEPT_006 = "Fumigation";
    public static final String CONCEPT_007 = "Docking";
    public static final String CONCEPT_008 = "Storage";
    public static final String CONCEPT_009 = "Delays";
    public static final String CONCEPT_010 = "Dragging";
    public static final String CONCEPT_011 = "Permissions";
    public static final String CONCEPT_012 = "Duties";
    public static final String CONCEPT_013 = "Other1";
    public static final String CONCEPT_014 = "Other2";
    public static final String CONCEPT_015 = "Other3";

    public static final String CONCEPT_016 = "NoPECEAccount";
    public static final String CONCEPT_017 = "DTA";
    public static final String CONCEPT_018 = "IVA";
    public static final String CONCEPT_019 = "IGI";
    public static final String CONCEPT_020 = "PRV";
    public static final String CONCEPT_021 = "IVAPRV";
    public static final String CONCEPT_022 = "ManeuversNoF";
    public static final String CONCEPT_023 = "DeconsolidationNoF";
    public static final String CONCEPT_024 = "Other1NoF";
    public static final String CONCEPT_025 = "Other2NoF";
    public static final String CONCEPT_026 = "Other3NoF";

    public static final String CONCEPT_DESC_001 = "CNT";
    public static final String CONCEPT_DESC_002 = "Validación";
    public static final String CONCEPT_DESC_003 = "Maniobras";
    public static final String CONCEPT_DESC_004 = "Desconsolidación";
    public static final String CONCEPT_DESC_005 = "Maniobras en Rojo";
    public static final String CONCEPT_DESC_006 = "Fumigación";
    public static final String CONCEPT_DESC_007 = "Muellaje";
    public static final String CONCEPT_DESC_008 = "Almacenaje";
    public static final String CONCEPT_DESC_009 = "Demoras";
    public static final String CONCEPT_DESC_010 = "Arrastres";
    public static final String CONCEPT_DESC_011 = "Permisos";
    public static final String CONCEPT_DESC_012 = "Derechos";
    public static final String CONCEPT_DESC_013 = "Otros 1";
    public static final String CONCEPT_DESC_014 = "Otros 2";
    public static final String CONCEPT_DESC_015 = "Otros 3";
	
    public static final String CONCEPT_DESC_016 = "Impuestos no pagados con cuenta PECE";
    public static final String CONCEPT_DESC_017 = "DTA";
    public static final String CONCEPT_DESC_018 = "IVA";
    public static final String CONCEPT_DESC_019 = "IGI";
    public static final String CONCEPT_DESC_020 = "PRV";
    public static final String CONCEPT_DESC_021 = "IVA/PRV";
    public static final String CONCEPT_DESC_022 = "Maniobras (Sin Factura Fiscal)";
    public static final String CONCEPT_DESC_023 = "Desconsolidación (Sin Factura Fiscal)";
    public static final String CONCEPT_DESC_024 = "Otros 1 (Sin Factura Fiscal)";
    public static final String CONCEPT_DESC_025 = "Otros 2 (Sin Factura Fiscal)";
    public static final String CONCEPT_DESC_026 = "Otros 3 (Sin Factura Fiscal)";
    
	public static final String OUTSOURCING_APPROVAL_SUBJECT = "Portal de Proveedores: Aprobación de documentos de Outsourcing del proveedor _SUPPLIER_";
	public static final String OUTSOURCING_APPROVAL_MESSAGE = "Estimado Aprobador. <br /><br />El proveedor _SUPPLIER_ categorizado como OUTSOURCING, ha enviado la documentación que se anexa en este correo para su revisión y aprobación. <br /><br />Para APROBAR esta documentación y notificar al proveedor, haga click en el siguiente enlace:<br /><br /> _LINK_ <br /><br /><br />";
	public static final String OUTSOURCING_APPROVED_MESSAGE = "Estimado Proveedor de OutSourcing. <br /><br />Su documentación ha sido aceptada y a partir de este momento usted podrá utulizar el Portal de Proveedores sin mayor incoveniente. <br /><br />Nota Importante: De conformidad con las disposiciones en materia fiscal, en el momento en que cargue facturas en el Portal de Proveedores y éstas pertenezcan a Servicios de OutSourcing, el sistema le permitirá cargar de forma adicional los comprobantes correspondientes.<br /><br />";
	public static final String EMAIL_PORTAL_LINK_PUBLIC = "https://3.222.152.41/supplierWebPortalCryoInfra/public";
	
	public static final String NEWREGISTER_SUBJECT = "Portal de Alta de Proveedores - CryoInfra. Enlace para nuevo registro. No responder.";
	public static final String NEWREGISTER_RENEW_SUBJECT = "Portal de Alta de Proveedores - CryoInfra. Renovación del enlace para nuevo registro. No responder.";
	public static final String NEWREGISTER_MESSAGE = "Estimado Proveedor. <br /><br />En CryoInfra estamos en búsqueda de aliados comerciales capaces de alcanzar nuestros requerimientos en términos de innovación, seguridad, calidad y servicio. <br /><br />Es un placer notificarle que ha sido seleccionado para ser parte de nuestros proveedores, a continuación, encontrará un nuevo enlace para su registro en nuestro portal.<br /><br />Considere las siguientes condiciones de uso:<br />1.Abra la URL utilizando copiar y pegar en los navegadores Chrome, Mozilla, Safari.<br />2.El enlace podrá ser utilizado por una sola ocasión y es intransferible.<br/>3. El enlace tendrá una vigencia de 3 días a partir de la recepción de este correo. Si el enlace vence, puede solicitar una renovación poniéndose en contacto con CryoInfra.<br />4. Cualquier otro enlace enviado con anterioridad quedará desactivado.<br />5. En caso de existir alguna duda contactar a su representante de adquisicione.<br /><br /> Enlace para su registro: <br /><br />";
	public static final String NEWREGISTER_RENEW_MESSAGE="Estimado Proveedor. <br /><br />En CryoInfra estamos en búsqueda de aliados comerciales capaces de alcanzar nuestros requerimientos en términos de innovación, seguridad, calidad y servicio. <br /><br />Es un placer notificarle que ha sido seleccionado para ser parte de nuestros proveedores, a continuación, encontrará un nuevo enlace para su registro en nuestro portal.<br /><br />Considere las siguientes condiciones de uso:<br />1.Abra la URL utilizando copiar y pegar en los navegadores Chrome, Mozilla, Safari.<br />2.El enlace podrá ser utilizado por una sola ocasión y es intransferible.<br/>3. El enlace tendrá una vigencia de 3 días a partir de la recepción de este correo. Si el enlace vence, puede solicitar una renovación poniéndose en contacto con CryoInfra.<br />4. Cualquier otro enlace enviado con anterioridad quedará desactivado.<br />5. En caso de existir alguna duda contactar a su representante de adquisicione.<br /><br /> Enlace para su registro: <br /><br />";
	
	public static final String LOG_SFTP_PROCESS = "SFTPPROCESS";
	public static final String MSG_PORTAL_INACTIVE = "Lo sentimos, el portal esta en mantenimiento,intentalo mas tarde";
	public static final String LOGGER_JEDWARS_RELOAD  = "ENVIO_JEDR_RELOAD";
	public static final String LOGGER_JEDWARS_ERROR  = "ERROR";
	public static final String LOGGER_JEDWARS_SEND  = "SEND";
	
	public static final String FREIGHT = "FLETE";
	public static final String SFTP_APPROVAL_KEY = "SFTAPROVAL";
	public static final String GENERIC_APPROVAL_SUBJECT = "Portal de Proveedores: Aprobación de documento de _DOCUMETYPE_ del proveedor _SUPPLIER_";
	public static final String GENERIC_APPROVAL_MESSAGE = "Estimado Usuario. <br /><br />El proveedor _SUPPLIER_ ha enviado un(a) _DOCUMETYPE_ con UUID: _UUID_ para su revisión y aprobación. <br /><br />Para APROBAR, ingrese en el siguiente enlace:<br /><br /> _LINK_ <br /><br /><br />Dear user. <br /><br />The supplier _SUPPLIER_ has submitted a _DOCUMETYPE_ with UUID: _UUID_ for review and approval. <br /><br />To APPROVE, enter the following link:<br /><br /> _LINK_ <br /><br /><br />";
	public static final String GENERIC_APPROVED_MESSAGE = "Estimado Proveedor <br /><br />Su documento de  _DOCUMETYPE_ con UUID: _UUID_ ha sido aprovado <br /><br />Para Revisar , ingrese en el siguiente enlace:<br /><br /> _LINK_ <br /><br />Dear Supplier <br /><br />Your document of _DOCUMETYPE_ with UUID: _UUID_ has been approved <br /><br />To Review, enter the following link:<br /><br /> _LINK_ <br / ><br />";
	public static final String GENERIC_REJECTED_MESSAGE = "Estimado Proveedor <br /><br />Su documento de  _DOCUMETYPE_ con UUID: _UUID_ ha sido rechazado <br /><br />Para Revisar , ingrese en el siguiente enlace:<br /><br /> _LINK_ <br /><br />Dear Supplier <br /><br />Your document of _DOCUMETYPE_ with UUID: _UUID_ has been rejected <br /><br />To Review, enter the following link:<br /><br /> _LINK_ <br / ><br />";
	
	public static final String SP2900 = "SP2900";
	public static final String SP2802 = "SP2802";
	
	public static final String LOG_MASSUPLOAD = "CARGA_MASIVA";
	public static final String FISCAL_DOC_NEW = "NUEVO";
	public static final String INVOICE_SAT = "FacturaSAT";
	public static final String BLOCK_ACTION = "BLOCK";
	public static final String UNBLOCK_ACTION = "UNBLOCK";
	public static final String BLOCK_TYPE_CRP = "CRP";
	public static final String USER_PPROVEEDORES = "pproveedores";
	
    public static final String FISCALDOCUMENT_MODULE = "FISCALDOCUMENT";
    public static final String SALESORDER_MODULE = "SALESORDER";
    public static final String SUPPLIER_MODULE = "SUPPLIER";
    public static final String OUTSOURCING_MODULE = "OUTSOURCING";
    public static final String USERS_MODULE = "USERS";
    public static final String SAT_MODULE = "SAT";
    public static final String UDC_MODULE = "UDC";
    public static final String APPROVAL_MODULE = "APPROVAL";
    public static final String APPROVALSEARCH_MODULE = "APPROVALSEARCH";
    public static final String TAXVAULT_MODULE = "TAXVAULT";

	public static final String STATUS_JDE_PENDI_FLETES = "R";
	public static final String STATUS_JDE_APROV_FLETES = "Y";
	public static String truncate(String value, int places) {
		return new BigDecimal(value)
		    .setScale(places, RoundingMode.DOWN)
		    .stripTrailingZeros()
		    .toString();
	}
	
	public static String round(double value) {
		double roundOff = Math.round(value * 100.0) / 100.0;
		return String.valueOf(roundOff);
	}
	
	
	
	@PostConstruct
	public void init() {
	        statusMap.put(STATUS.STATUS_OC_RECEIVED, "OC RECIBIDA");
	        statusMap.put(STATUS.STATUS_OC_APPROVED, "OC APROBADA");
	        statusMap.put(STATUS.STATUS_OC_SENT, "OC ENVIADA");
	        statusMap.put(STATUS.STATUS_OC_CLOSED, "OC CERRADA");
	        statusMap.put(STATUS.STATUS_OC_INVOICED, "OC FACTURADA");
	        statusMap.put(STATUS.STATUS_OC_PROCESSED, "OC PROCESADA");
	        statusMap.put(STATUS.STATUS_OC_PAID, "OC PAGADA");
	        statusMap.put(STATUS.STATUS_OC_CANCEL, "OC CANCELADA");
	}
	 
}
