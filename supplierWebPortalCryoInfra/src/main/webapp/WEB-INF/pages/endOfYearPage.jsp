<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="shortcut icon" href="resources/images/favicon.ico"/>
  <title>CryoInfra&copy;-Portal de proveedores</title>

<link rel="stylesheet" type="text/css" href="${url}/resources/css/app.css">
<script type="text/javascript" charset="utf-8" src="${url}/app/app.js"></script>
<script type="text/javascript" charset="utf-8" src="${url}/resources/js/common.js"></script>
<!--  <script type="text/javascript" charset="utf-8" src="${url}/resources/js/Currency.js"></script>-->




</head>
<body>

				<table height='100%' width='100%' class='hdrTable'> 
                		<tr> 
                		  <td style='width:60%;'>  
                		     <table height='70%' width='100%'> 
                		      <tr><td style='width:270px;'> &nbsp;</td> 
                		          <td rowspan=2 style='font-size:26px;border-right:1px solid #333;color:#000;padding-bottom:7px;'>&nbsp;</td> 
                		          <td  rowspan=2 style='width:700px;text-align:left;color:#000;font-size:17px;'>&nbsp;&nbsp;      <br />&nbsp;&nbsp;<span style='font-size:14px;color:#666;'>   </span><br />&nbsp;&nbsp;&nbsp;<span style='font-size:11px;color:#666;background-color:#E0DC1B;'>   </span></td> 
                		          </tr> 
                		      </table> 
                		  </td> 
                          <td width:15%; style='text-align:right;padding-left:150px;vertical-align:bottom;font: 16px Tahoma, Verdana, sans-serif; color:#000;'>  
                           <a href='j_spring_security_logout'  id='logoutLink' style='font-size: 14px;color: red;'> Salir   </a><br /><br /> 
                          </td> 
                          <td width:5%; style='text-align:right;padding-right:40px;vertical-align:middle;'> 
                          &nbsp; 
                          </td> 
                          </tr>
                     </table>
                     
<div id="contentEnfOfYear" style="margin-left: auto;
    margin-right: auto;
    width: 60em">

<br />
<h2>Aviso Importante:</h2>

Estimado Proveedor, con motivo del cierre de fin de año, les informamos que la <strong>última fecha de recepción y validación de facturas del año <script>document.write(new Date().getFullYear())</script> ha sido el ${startDate}</strong>,
les recordamos que todas aquellas facturas que no fueron ingresadas en la fecha indicada deberán ser re-facturadas sin excepción con fecha  <script>document.write(new Date().getFullYear() + 1)</script> y podrán ser ingresadas en el portal a
partir del ${endDate}.<br /><br />

Así mismo, les pedimos nos ayuden a subir el complemento de pago a nuestro portal en tiempo y forma, ya que de lo contrario se pueden ver afectadas futuras operaciones. En adición les
solicitamos mantener sus datos de contacto actualizados para asegurar una comunicación efectiva. Cualquier duda o aclaración, les sugerimos revisarlo con su contacto de Compras y/o Cuentas por Pagar.<br /><br />
<br />Muchas gracias. <br />
<strong>Departamento de cuentas por pagar.</strong>



</div>
</body>
</html>