<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<html>

<head>
    
        <!-- The line below must be kept intact for Sencha Cmd to build your application -->

    <link rel="stylesheet" type="text/css" href="${url}/resources/css/SupplierApp-all.css">
	<link rel="stylesheet" type="text/css" href="${url}/resources/css/app.css">    
    <script type="text/javascript" charset="utf-8" src="${url}/ext/build/ext-all.js"></script>
    
    	
	<script>
	var SuppAppMsgLogin = {};
	
	var langu = window.navigator.language;
	var lang = "";
	if(langu.startsWith("es", 0)){
		lang = "es";
	}else{
		lang = "en";
	}
	
	Ext.Ajax.request({
	    url: 'getLocalization.action',
	    method: 'GET',
	    params: {
	    	lang : lang
        },
	    success: function(fp, o) {
	    	var resp = Ext.decode(fp.responseText, true);
	    	SuppAppMsgLogin = resp.data;
	    	
	    	document.getElementsByName('username')[0].placeholder= SuppAppMsgLogin.loginUser;
	    	document.getElementsByName('password')[0].placeholder= SuppAppMsgLogin.loginPass;
	    	document.getElementsByName('buttonLogin')[0].innerHTML = SuppAppMsgLogin.loginButtonAccess;
	    	document.getElementsByClassName("forgetPass")[0].innerHTML = SuppAppMsgLogin.loginForgetPass;
	    	document.getElementsByClassName("downloadDocSupp")[0].innerHTML = SuppAppMsgLogin.downloadDocSupp;
	    }
	}); 
	
	function setupPasswordToggle() {
	    var togglePassword = document.getElementById('togglePassword');
	    var passwordField = document.getElementById('passwordField');
	    
	    if (togglePassword && passwordField) {
	        togglePassword.addEventListener('click', function() {
	            if (passwordField.type === 'password') {
	                passwordField.type = 'text';
	                this.src = '${url}/resources/images/eye-closed.png';
	                this.alt = 'Ocultar contraseña';
	            } else {
	                passwordField.type = 'password';
	                this.src = '${url}/resources/images/eye-open.png';
	                this.alt = 'Mostrar contraseña';
	            }
	        });
	    }
	}
	
	//var x = document.getElementsByClassName("newSupplierButton");
	
	Ext.onReady(function() {
    setupPasswordToggle();
});

	</script>
	

  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  
  <link rel="shortcut icon" href="resources/images/favicon.ico"/>
  <title>CryoInfra&copy;-Login</title>
  <link rel="stylesheet" href="resources/css/reset.css">
  <link rel="stylesheet" href="resources/css/login.css" media="screen" type="text/css" />
  
  <script>
	function login(form){
		document.getElementById("loading").style.display = "block";
	    form.submit();
	}
</script>

<style>
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

td {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
  font-size: 90%;
}

th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
  background-color: #dddddd;
}

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body, html {
    height: 100%;
    width: 100%;
    overflow: hidden;
}

/* ======================= */
/* 📱 RESPONSIVE <= 900px   */
/* ======================= */
@media (max-width: 900px) {
    /* Contenedor principal pasa a una columna */
    body > div[style*="display:flex"] {
        flex-direction: column;
        height: auto;
        min-height: 100vh;
        background-color: #EFF5F9 !important; 
    }

    /* MODIFICADO: Mostrar panel izquierdo pero más pequeño */
    body > div[style*="display:flex"] > div:first-child {
        display: flex;
        height: 200px; /* Altura fija para móvil */
        min-height: 200px;
        flex: none;
    }

    /* Contenedor derecho ocupa el resto */
    body > div[style*="display:flex"] > div:last-child {
        height: auto;
        min-height: calc(100vh - 200px);
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: #EFF5F9 !important;
    }
    
    /* Ajustar imagen para móvil */
    body > div[style*="display:flex"] > div:first-child img {
        object-fit: contain; /* En móvil, mejor contain */
    }
}

form {
    text-align: center;
}

.newSupplierButton {
    margin: 0 auto;   /* Centrará el botón */
    display: inline-block;
}

.input-login {
    width: 100%;
    max-width: 350px;
    height: 40px;
    padding: 6px 12px;
    box-sizing: border-box;
    border: 1px solid #ccc;
    font-weight: 700;
    font-size: 13.3333px;
    font-family: Arial, Helvetica, sans-serif;
}

/* Usuario (superior) */
.input-username {
    border-radius: 7px 7px 0 0;
    margin-bottom: 15px;
}

/* Password (inferior) */
.input-password {
    border-radius: 0 0 7px 7px;
    padding-right: 35px; /* Espacio para el icono */
    margin: 0 auto;
    background: #ffffff !important;
    color: #000 !important;
    border: 1px solid #ccc !important;
    -webkit-appearance: none !important; /* Safari + Chrome */
    -moz-appearance: none !important;    /* Firefox */
    appearance: none !important;         /* Edge */
}

.password-container {
    width: 100%;
    max-width: 350px;
    margin: 0 auto;  /* centra igual que usuario */
    position: relative;
    padding: 0; /* 🔥 evita desplazamientos */
}

.footer {
    white-space: nowrap;         /* ❗ NO permite saltos de línea */
    text-align: center;
    width: 100%;
    font-size: 12px;
}

.error {
    background-color: #EFF5F9 !important;
    padding: 8px;
    border-radius: 4px;
    margin-bottom: 15px;
    color: red;
}

.error span {
    color: red;
    font-weight: bold;
}

/* Para 1366x768 - Reducción moderada */
@media (max-width: 1366px) and (max-height: 800px) {
    /* Solo ajustamos el tamaño de la imagen */
    body > div[style*="display:flex"] > div:first-child img {
        object-fit: contain !important;
        /* 80% es un buen balance - ajusta según necesites */
        /*transform: scale(1) !important;*/
        transform-origin: center !important;
    }
}

.avatar {
    background: transparent !important;
    padding: 0 !important;
    margin: 0 auto !important;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    box-shadow: none !important;
    border: none !important;
}

.avatar img {
    background: transparent !important;
    box-shadow: none !important;
    border: none !important;
    display: block;
}

</style>

</head>

<body style="background-color: #EFF5F9 !important;">

<div style="display:flex; height:100vh; width:100vw; margin:0; padding:0; background-color: #EFF5F9 !important;">

<!-- IZQUIERDA 2/3 -->
    <!-- <div style="flex:2; display:flex;background-color: #EFF5F9 !important;">
        <img src="resources/images/Logo-Oficial-Login.jpg"
             style="max-width:100%; max-height:100%; object-fit:contain;">
    </div> -->

<!-- IZQUIERDA 2/3 -->
<div style="flex:2; display:flex; background-color: #EFF5F9 !important; position: relative; margin:0; padding:0; overflow:hidden;">
    
        <div style="position: relative; width: 100%; height: 100%; margin:0; padding:0;">
        
        <!-- La imagen -->
        <img src="resources/images/Logo-Oficial-Login.jpg"
             style="width:100%; height:100%; object-fit:cover; margin:0; padding:0; display:block;">              
    </div>
</div>

    <!-- DERECHA 1/3  -->
    <div style="flex:1; display:flex; justify-content:center; align-items:center; height:100%;background-color: #EFF5F9 !important;">
     <div class="login-container" style="background-color: #EFF5F9 !important;">
      <div class="login-box">
      <div id="loading" style="display:none;"></div>
      
       <div class="logo" style="text-align:center;">
</div>
		<div class="avatar">
      <img src="resources/images/CryoInfra-logo.png"
     style="max-width:100%; height:auto; display:block;">
		</div>
      

				<c:choose>
	         <c:when test="${param.error == 'true'}">
				<div class="error"><span style="color:red;font-weight:bold;">ERROR: </span>${fn:replace(SPRING_SECURITY_LAST_EXCEPTION.message, 'Credenciales incorrectas', 'Username/Password are incorrect')}</div>
		    </c:when>    
		</c:choose>
		<form action="${pageContext.request.contextPath}/login" method="POST">           
           <input type="text" placeholder="usuario" name="username" id="usernameField" class="input-login input-username" autofocus required>
		
		<div class="password-container" style="position: relative; width: 100%; max-width: 350px;">
    <input type="password" maxlength="12" placeholder="contraseña" name="password" id="passwordField" class="input-login input-password" required>
    <img id="togglePassword" src="${url}/resources/images/eye-open.png" alt="Mostrar contraseña"
         style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); 
                cursor: pointer; width: 20px; height: 20px; z-index: 2; background: white; padding: 2px;">
		</div>
		<br />
		<button name="buttonLogin" type="submit"  class="newSupplierButton" onClick="login(this.form)">Login</button>
		
		</form>
		<br />		
		<%--
		<form action="${pageContext.request.contextPath}/newRegister.action" method="get">
			<input type="submit" value="Click aquí para registrarse como nuevo proveedor" class="newSupplierButton" name="newSupplierText" />
		</form>
		 --%>
		
		<form action="${pageContext.request.contextPath}/requestTicketPage.action" method="get">
			<input style="width:180px;" type="submit" value="Tickets de solicitud" class="newSupplierButton"/>
		</form>
		<br />
		<a class="forgetPass" href="${pageContext.request.contextPath}/requestResetPassword.action" style="font-size:13px;text-align:center;font-weight: bold;"
		                    >¿Olvidó su contraseña?</a>
		
		<br/>	         
		<br/>	
		<a href="resources/Manual Proveedores V_1.1.pdf" download >
  		<p style="font-size:13px" class="downloadDocSupp">Descargar manual de proveedores</p>
		</a> 
		
	<div class="footer" style="background-color: #EFF5F9 !important;">
	    &copy; 2000-2025 Smartech Consulting Group S.A. de C.V.<br>Derechos Reservados.
	    </div>

 </div>

    </div>
</div>
</body>

</html>

