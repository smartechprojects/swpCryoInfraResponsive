<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>  
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="shortcut icon" href="resources/images/favicon.ico"/>
  <link href="<c:url value="/resources/css/pikaday.css" />" rel="stylesheet" type="text/css" />
  <link href="<c:url value="/resources/css/app.css" />" rel="stylesheet" type="text/css" />  
  <title>CryoInfra&copy;-Portal de proveedores</title>
<script>
	var displayName = '<c:out value="${name}"/>';
	var userName = '<c:out value="${userName}"/>';
	var message = '<c:out value="${message}"/>';
	var addressNumber = '<c:out value="${addressNumber}"/>';

</script>
<style>
/* Style all input fields */
body {
  font: 12px Tahoma, Verdana, sans-serif;
  background: #fff;
}

h2{
  font: 14px Tahoma, Verdana, sans-serif;
  font-weight:bold
}

h3{
  font: 18px Tahoma, Verdana, sans-serif;
  font-weight:bold
}

input {
  width: 180px;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  margin-top: 6px;
  margin-bottom: 16px;
  font: 12px Tahoma, Verdana, sans-serif;
}

/* Style the container for inputs */
.container {
  background-color: #fff;
  padding: 10px;
}

/* The message box is shown when the user clicks on the password field */
#message {
  display:none;
  background: #fff;
  color: #000;
  position: relative;
  padding: 2px;
  margin-top: 10px;
}

#message p {
  padding: 1px 35px;
  font-size: 11px;
}

/* Add a green text color and a checkmark when the requirements are right */
.valid {
  color: green;
}

.valid:before {
  position: relative;
  left: -35px;
  content: "✔";
}

/* Add a red text color and an "x" when the requirements are wrong */
.invalid {
  color: red;
}

.invalid:before {
  position: relative;
  left: -35px;
  content: "✖";
}

.hdrTable{
  height:9px;
  background-image: url(${url}/resources/images/Left-logo.png) !important; 
  background-repeat: no-repeat;
  background-size: 100% 100%;
}


.hdrTable tr td{
  color:#000;
  text-align:center;
  font: 14px Tahoma, Verdana, sans-serif;
}

.myButton {
	background-color:#03559c;
	-moz-border-radius:5px;
	-webkit-border-radius:5px;
	border-radius:5px;
	border:1px solid #B6985A;
	display:inline-block;
	cursor:pointer;
	width:150px;
	color:#ffffff;
	font-family:font: 18px Tahoma, Verdana, sans-serif;
	font-size:13px;
	padding:4px 17px;
	text-decoration:none;
}
.myButton:hover {
	background-color:#000;
}

table {
  font: 12px Tahoma, Verdana, sans-serif;
  }
  .tooltip-container {
  margin: 0 auto;
  display: inline-block;
}  

/* EMPIEZA AQUÍ */

.tooltip-container {
  position: relative;
  cursor: pointer;
}

.tooltip-one {
  padding: 18px 32px;
  background: #fff;
  position: absolute;
  width: 220px;
  border-radius: 5px;
  text-align: center;
  filter: drop-shadow(0 3px 5px #ccc);
  line-height: 1.5;
  display: none;
  bottom: 40px;
  right: 50%;
  margin-right: -110px;
}

.tooltip-one:after {
  content: "";
  position: absolute;
  bottom: -9px;
  left: 50%;
  margin-left: -9px;
  width: 18px;
  height: 18px;
  background: white;
  transform: rotate(45deg);
}

.tooltip-trigger:hover + .tooltip-one {
  display: block;
}   
</style>
</head>
<body>
               <table height='100%' width='90%' class='hdrTable'>  
            		<tr> 
            		  <td style='width:60%;'>  
            		     <table height='50%' width='100%'> 
            		      <tr><td style='width:200px;'> &nbsp;</td> 
            		          <td rowspan=2 style='font-size:26px;border-right:0px;color:#000;padding-bottom:7px;'>&nbsp;</td> 
            		          <td  rowspan=2 style='text-align:center;color:#000;font-size:17px;'>&nbsp;&nbsp; 
            		          Bienvenido <br />&nbsp;&nbsp;&nbsp;<script  type="text/javascript"> document.write(displayName)</script>  </td> 
            		          </tr> 
            		      </table> 
            		  </td> 
                      <td width:15%; style='text-align:right;padding-left:150px;vertical-align:middle;font: 16px Tahoma, Verdana, sans-serif; color:#000;'>  
                       <a href='j_spring_security_logout'  id='logoutLink' style='font-size: 14px;color: red;padding-top:5px;'>Salir</a> <br /><br /><br /> 
                      </td> 
                      <td width:5%; style='text-align:right;padding-right:40px;vertical-align:middle;'> 
                      &nbsp; 
                      </td> 
                      </tr></table>

<div class="container">

  <c:if test="${message=='_START'}">
  <h2>Solicitud de información adicional</h2>
	 Estimado proveedor. Nuestros registros indican que usted proporciona servicios especializados por lo que lo invitamos a enviarnos la siguiente documentación antes de poder utilizar este sistema. <br /><br />
	  <form:form commandName="multiFileUploadBean" method="post" action="uploadBaseLineFiles.action" enctype="multipart/form-data">
		<input type="hidden" id="addressNumber" name="addressNumber" value='<c:out value="${addressNumber}"/>' />
			<table>	
				<tr>
					<td>1. Constancia de cumplimiento de obligaciones:&nbsp;
					<input type="file" name="uploadedFiles[0]" style="font-size:10px;width:330px;" required="required">
					</td>
				</tr>
				<tr>
					<td>2. Constancia de situación fiscal actualizada:&nbsp;
					<input type="file" name="uploadedFiles[1]" style="font-size:10px;width:330px;" required="required">
					</td>
				</tr>	
				<tr>
					<td>3. Autorización de la STPS y fecha de vigencia:&nbsp;
					<input type="file" name="uploadedFiles[2]" style="font-size:10px;width:330px;" required="required"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					Fecha de vigencia:&nbsp;<input type="text" name="effectiveDate" id="datepicker" required="required">

					</td>
				</tr>
<!--  				<tr>
					<td>4. Listado de los trabajadores que colaboran con nosotros:&nbsp;
					<input type="file" name="uploadedFiles[3]" style="font-size:10px;width:330px;" required="required">
					</td>
				</tr>	
									
				<tr>
					<td>5. Acta protocolizada con detalle de su objeto social:&nbsp;
					<input type="file" name="uploadedFiles[4]" style="font-size:10px;width:330px;" required="required">
					</td>
				</tr>-->
				 						
				<tr>
					<td><input type="submit" value="Enviar archivos" class="myButton" /></td>
				</tr>
			</table>
		</form:form>
		Una vez que cargue los documentos, será dirigido a la página de Log In para ingresar de forma normal. <br /><br />
		
  </c:if>


  <c:if test="${message=='_STARTMONTH'}">
  <h2>La compañía ${relatedCompany} le solicita la siguiente información MENSUAL:  </h2>
  ${monthLoad} 
	 Estimado proveedor. Como proveedor de Servicios Especializados requerimos que porporcione los documentos de tipo MENSUAL por lo que  lo invitamos a enviarnos la siguiente documentación para continuar utilizando este sistema. <br /><br />
	  <form:form commandName="multiFileUploadBean" method="post" action="uploadMonthlyFiles.action" enctype="multipart/form-data">
		<input type="hidden" id="addressNumber" name="addressNumber" value='<c:out value="${addressNumber}"/>' />
			<table>
	<!--  			<tr>
					<td>1. Acuse de declaración informativa mensual del IMSS:&nbsp;
					<input type="file" name="uploadedFiles[0]" style="font-size:10px;width:350px;" required="required"> 
					</td>
				</tr>	-->			
				<tr>
					<td>1. Pagos provisionales de ISR por salarios:&nbsp;
					<input type="file" name="uploadedFiles[0]" style="font-size:10px;width:330px;" required="required">
					
					<div class="tooltip-container"><div class="icon-info tooltip-trigger"></div>
  					<div class="tooltip-one">El formato del archivo Pagos Provisionales de ISR por salarios deberá ser un formato PDF/Texto.</div>
					</div>						
					</td>
				</tr>
				<tr>
					<td>2. Pago de las cuotas obrero-patronales al IMSS:&nbsp;
					<input type="file" name="uploadedFiles[1]" style="font-size:10px;width:330px;" required="required">
					
					<div class="tooltip-container"><div class="icon-info tooltip-trigger"></div>
  					<div class="tooltip-one">El formato del archivo Pago de las cuotas obrero-patronales al IMSS deberá ser un formato PDF/Texto.</div>
					</div>					
					</td>
				</tr>	
				<tr>
					<td>3. Pago de las aportaciones al INFONAVIT:&nbsp;
					<input type="file" name="uploadedFiles[2]" style="font-size:10px;width:330px;" required="required">
					
					<div class="tooltip-container"><div class="icon-info tooltip-trigger"></div>
  					<div class="tooltip-one">El formato del archivo Pago de las aportaciones al INFONAVIT deberá ser un formato PDF/Texto.</div>
					</div>					
					</td>
				</tr>
<!--  			<tr>
					<td>5. Pago de ISN mensuales:&nbsp;
					<input type="file" name="uploadedFiles[4]" style="font-size:10px;width:330px;" required="required">
					</td>
				</tr>	-->	
				<tr>
					<td>4. Evidencia del pago provisional de IVA:&nbsp;
					<input type="file" name="uploadedFiles[3]" style="font-size:10px;width:350px;" required="required">
					
					<div class="tooltip-container"><div class="icon-info tooltip-trigger"></div>
  					<div class="tooltip-one">El formato del archivo Evidencia del Pago Provisional de IVA deberá ser un formato PDF/Texto.</div>
					</div>					 
					</td>
				</tr>
				
				<tr>
					<td>5. Cédula de determinación de cuotas:&nbsp;
					<input type="file" name="uploadedFiles[4]" style="font-size:10px;width:330px;" required="required">
					
					<div class="tooltip-container"><div class="icon-info tooltip-trigger"></div>
  					<div class="tooltip-one">El formato de la Cédula de determinación de cuotas deberá ser un formato PDF/Texto.</div>
					</div>					
					</td>
				</tr>
													
				<tr>
					<td><input type="submit" value="Enviar archivos" class="myButton" /></td>
				</tr>
			</table>
		</form:form>
		Una vez que cargue los documentos, será dirigido a la página de Log In para ingresar de forma normal. <br /><br />
  </c:if>

  <c:if test="${message=='_STARTQUARTER'}">
  <h2>Solicitud de información CUATRIMESTRAL</h2>
	 Estimado proveedor. Como proveedor de servicios especializados requerimos que porporcione los documentos de tipo CUATRIMESTRAL por lo que  lo invitamos a enviarnos la siguiente documentación para continuar utilizando este sistema. <br /><br />
	  <form:form commandName="multiFileUploadBean" method="post" action="uploadQuarterlyFiles.action" enctype="multipart/form-data">
		<input type="hidden" id="addressNumber" name="addressNumber" value='<c:out value="${addressNumber}"/>' />
			<table>
				<tr>
					<td>1. Informativa de Contratos de Servicios u Obras Especializados:&nbsp;
					<input type="file" class="fileUpload" name="uploadedFiles[0]" style="font-size:10px;width:350px;" required="required">
					
					<div class="tooltip-container"><div class="icon-info tooltip-trigger"></div>
  					<div class="tooltip-one">El formato del archivo Informativa de Contratos de Servicios u Obras Especializados deberá ser un formato PDF/Texto.</div>
					</div> 
					</td>
				</tr>			
				<tr>
					<td>2. Sistema de Información de Subcontratación:&nbsp;
					<input type="file" class="fileUpload" name="uploadedFiles[1]" style="font-size:10px;width:330px;" required="required">
					
					<div class="tooltip-container"><div class="icon-info tooltip-trigger"></div>
  					<div class="tooltip-one">El formato del archivo Sistema de Información de Subcontratación deberá ser un formato PDF/Texto.</div>
					</div>					
					</td>
				</tr>
					<td><input type="submit" value="Enviar archivos" class="myButton" /></td>
				</tr>
			</table>
		</form:form>
		Una vez que cargue los documentos, será dirigido a la página de Log In para ingresar de forma normal. <br /><br />
		<b><p class ="MessageSize" style="color:#FF0000;display:none;">El documento cargado supera los 20 MB.</p></b>
  </c:if>

  <c:if test="${message=='_SUCCESS'}">
	  Hemos recibido sus documentos de forma exitosa. En breve la revisaremos y le notificaremos el resultado de la revisión
  </c:if>
  
  <c:if test="${message=='_SUCCESSMONTH' || message=='_SUCCESSQUARTER'}">
	  Hemos recibido sus documentos de forma exitosa.
  </c:if>	

  <c:if test="${message=='_ERROR'}">
	  <h2>Ha ocurrido un error con el envío de los documentos. Por favor, intente más tarde.</h2>
  </c:if>	

  <c:if test="${message=='_APPROVED'}">
	  El proveedor de OutSourcing: ${supplierName} ha sido APROBADO para la utilización del portal y notificado por correo electrónico.
  </c:if>

	<b style='color:red;'>  ${msgstatus}</b>
	
</div>

  <script type="text/javascript" src="<c:url value="/resources/js/pikaday.js" />"></script>
  <script>

    var picker = new Pikaday({
        field: document.getElementById('datepicker'),
        format: 'D/M/YYYY',
        toString(date, format) {
            // you should do formatting based on the passed format,
            // but we will just return 'D/M/YYYY' for simplicity
            const day = date.getDate();
            const month = date.getMonth() + 1;
            const year = date.getFullYear();
            return day + '-' + month + '-' + year;
        }
    });
    
</script>

</body>
</html>