<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<link rel="stylesheet" type="text/css" href="resources/css/style.css">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="shortcut icon" href="resources/images/favicon.ico"/>
  <title>CryoInfra&copy; - Olvidó su contraseña?</title>

	<script type="text/javascript">
	
	function submitform()
	{
	  document.getElementById("sendMsg").innerHTML = "Enviando mensaje. Espere unos segundos...";
	  document.getElementById("resetPassword").submit();
	}
	</script>
</head>
<body>

<div style="font-size:12px;">
     Proporcione su número de usuario. <br /> <br />Dentro de los siguientes minutos recibirá un correo con las instrucciones para reestablecer su contraseña. De no ser así comuníquese con su contacto en CryoInfra.
	<br /><br />
	<form id="resetPassword" cssStyle="margin: 0px" cssClass="form-horizontal" method="POST"
                      action="${pageContext.request.contextPath}/resetPassword.action">
     
         <label style="margin-top:10px">Usuario:</label>
         <input id="usrname" name="usrname" style="width:150px;" autofocus/>
         <a href="javascript: submitform()">Click aquí para enviar</a>
     </form> 
     <div id="sendMsg" style="font-color:red; font-size:12px;"></div>
     
     <c:if test="${not empty msg}">
       <div style="font-color:red; font-size:13px;">
        	<strong>${msg}</strong>
       </div>
     </c:if>
</div>

</body>
</html>

