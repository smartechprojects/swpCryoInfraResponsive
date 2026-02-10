<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="shortcut icon" href="resources/images/favicon.ico" />
<title>CryoInfra&copy; - Olvidó su contraseña?</title>

<link rel="stylesheet" type="text/css" href="resources/css/style.css">
<link rel="stylesheet" type="text/css"
	href="${url}/resources/css/SupplierApp-all.css">
<link rel="stylesheet" type="text/css"
	href="${url}/resources/css/app.css">
<link rel="stylesheet" href="resources/css/login.css" media="screen"
	type="text/css" />

<script type="text/javascript">
	function submitform() {
		
		var errorMsgDiv = document.getElementById("errorMsg");
		if (errorMsgDiv) {
			errorMsgDiv.style.display = "none";
		}
		
		document.getElementById("sendMsg").innerHTML = "Enviando mensaje. Espere unos segundos...";
		document.getElementById("resetPassword").submit();
	}
</script>
<style>
.newSupplierButton {
        margin: 0 auto;   /* Centrará el botón */
        display: inline-block;
    }
    
    .avatar {
    margin-top: 100px; /* Añade espacio arriba del logo */
    margin-bottom: 20px; /* Añade espacio abajo del logo */
}
</style>
</head>
<body>


	<div class="wrap">
		<div
			style="margin-top: 50px; margin-bottom: 50px; text-align: center;">
		</div>
		<div class="avatar">
    <img src="resources/images/CryoInfra-logo.png"
         style="max-width: 120%; height:auto; display:block; margin: 0 auto;"
         alt="CryoInfra Logo">
</div>

		<div style="text-align: center;">
			<h2>Recuperación de contraseña.</h2>
<br />
			<div style="font-size: 13px; line-height: 1.4;">
        Proporcione su usuario. <br /><br />
        Dentro de los siguientes minutos recibirá un correo con las instrucciones 
        para reestablecer su contraseña. De no ser así comuníquese con su contacto 
        en CryoInfra.
    </div> <br />
			<br />
			<form id="resetPassword" method="POST"
				action="${pageContext.request.contextPath}/resetPassword.action">
				<input id="usrname" name="usrname" placeholder="Usuario"
					style="width: 190px;" autofocus /> <br />
				<button name="buttonLogin" type="submit" class="newSupplierButton"
					onClick="submitform(this.form)">Enviar</button>
			</form>
			<br />
			<div id="sendMsg" style="color: black; font-size: 12px;"></div>

			<c:if test="${not empty msg}">
			    <div id="errorMsg" style="color: red; font-size: 13px;">
			        <strong>${msg}</strong> <br />
			    </div>
			</c:if>
			<br /> <a class="loginPage"
				href="${pageContext.request.contextPath}/login"
				style="font-size: 13px; text-align: center;">Ir
				a la página de login</a>
		</div>

	</div>




</body>
</html>

