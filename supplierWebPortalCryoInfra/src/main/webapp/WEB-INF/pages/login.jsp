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

</style>

</head>

<body>
<div id="loading" style="display:none;"></div>
    
	<div class="wrap">
  <div style="margin-top:50px;margin-bottom:100px;text-align:center;">
   <p> <img src="resources/images/hdr-logo.png" style="width:300px;"></p>
</div>
		<div class="avatar">
      <img src="resources/images/CryoInfra-logo-gris.png" style="height:100%;width:100%;">
		</div>
				<c:choose>
	         <c:when test="${param.error == 'true'}">
				<div class="error"><span style="color:red;font-weight:bold;">ERROR: </span>${fn:replace(SPRING_SECURITY_LAST_EXCEPTION.message, 'Credenciales incorrectas', 'Username/Password are incorrect')}</div>
		    </c:when>    
		</c:choose>
		<form action="${pageContext.request.contextPath}/login" method="POST">           
           <input type="text" placeholder="usuario" name="username" id="usernameField" 
           style="width: 350px; padding: 6px 12px; height: 40px; box-sizing: border-box; margin-bottom: 10px; 
           border: 1.11111px solid rgb(204, 204, 204); border-radius: 7px 7px 0px 0px; font-weight: 700; font-size: 13.3333px; 
           font-family: Arial, Helvetica, sans-serif;" autofocus required>
		
		<div class="bar">
			<i></i>
		</div>
		
		<div class="password-container" style="position: relative; width: 350px; margin-bottom: 10px;">
    <input type="password" maxlength="12" placeholder="contraseña" name="password" id="passwordField" 
           style="width: 100%; padding: 6px 35px 6px 12px; height: 40px; box-sizing: border-box; border: 1.11111px solid rgb(204, 204, 204); 
           border-radius: 0px 0px 7px 7px; font-weight: 700; font-size: 13.3333px; font-family: Arial, Helvetica, sans-serif;" required>
    <img id="togglePassword" src="${url}/resources/images/eye-open.png" alt="Mostrar contraseña"
         style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); 
                cursor: pointer; width: 20px; height: 20px; z-index: 2; background: white; padding: 2px;">
		</div>
		<br /><br />
		<button name="buttonLogin" type="submit" onClick="login(this.form)">Login</button>
		
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
		<a class="forgetPass" href="${pageContext.request.contextPath}/requestResetPassword.action" style="font-size:13px;text-align:center;"
		                    >Olvidó su contraseña?</a>
		
		<p></p><p></p><p></p><p></p><br/>	         
		<a href="resources/Manual Proveedores V_1.1.pdf" download >
  		<p style="font-size:13px" class="downloadDocSupp">Descargar manual de proveedores</p>
		</a> 
		                  		
	</div>



	<div class="footer">
	    &copy; 2000-2025 Smartech Consulting Group S.A. de C.V. Derechos Reservados.</div>

</body>

</html>

