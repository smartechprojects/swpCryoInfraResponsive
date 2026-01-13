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
    body > div[style*="display:flex"] {
        flex-direction: column;
        height: auto;
        min-height: 100vh;
        background-color: #EFF5F9 !important; 
    }

    /* Ocultar solo la imagen grande */
    body > div[style*="display:flex"] > div:first-child {
        display: none !important; /* 👈 Esto oculta todo el div con la imagen */
    }

    /* Formulario ocupa toda la pantalla */
    body > div[style*="display:flex"] > div:last-child {
        height: 100vh;
        min-height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: #EFF5F9 !important;
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
        max-width: 200px; /* Controlar tamaño máximo del logo */
        height: auto;
    }

    /* Smooth loading para la imagen */
    body > div[style*="display:flex"] > div:first-child img {
        opacity: 0;
        animation: fadeIn 0.5s ease forwards;
    }

    @keyframes fadeIn {
        to {
            opacity: 1;
        }
    }

    html, body {
        margin: 0 !important;
        padding: 0 !important;
        overflow: hidden;
    }

    /* Optimización para diferentes resoluciones - VERSIÓN SIMPLIFICADA */
    @media (min-width: 2560px) {
        /* Para pantallas ultra-wide 2560x1080 */
        body > div[style*="display:flex"] > div:first-child img {
            object-fit: contain !important;
        }
    }

    @media (min-width: 1920px) and (max-width: 2559px) {
        /* Para Full HD */
        body > div[style*="display:flex"] > div:first-child img {
            object-fit: contain !important;
        }
    }

    @media (min-width: 1400px) and (max-width: 1919px) {
        /* Para 1400x900 a 1600x900 */
        body > div[style*="display:flex"] > div:first-child img {
            object-fit: contain !important;
        }
    }

    /* Estilo específico para la imagen grande - elimina márgenes */
    body > div[style*="display:flex"] > div:first-child {
        margin: 0 !important;
        padding: 0 !important;
        border: none !important;
    }
    
    body > div[style*="display:flex"] > div:first-child img {
        margin: 0 !important;
        padding: 0 !important;
        border: none !important;
        outline: none !important;
    }

    </style>

</head>

<body style="background-color: #EFF5F9 !important;">

<div style="display:flex; height:100vh; width:100vw; margin:0; padding:0; background-color: #EFF5F9 !important;">

    <!-- IZQUIERDA 2/3 - IMAGEN PRINCIPAL -->
    <div style="
        flex: 2;
        display: flex;
        background-color: #EFF5F9 !important;
        margin: 0;
        padding: 0;
        overflow: hidden;
    ">
        <img src="resources/images/Logo-Oficial-Login.jpg"
             style="
                width: 100%;
                height: 100%;
                object-fit: contain;
                display: block;
                margin: 0;
                padding: 0;
             "
             alt="CryoInfra Login Background">              
    </div>

    <!-- DERECHA 1/3 - FORMULARIO DE LOGIN -->
    <div style="flex:1; display:flex; justify-content:center; align-items:center; height:100%;background-color: #EFF5F9 !important;">
        <div class="login-container" style="background-color: #EFF5F9 !important;">
            <div class="login-box">
                <div id="loading" style="display:none;"></div>
                
                <div class="logo" style="text-align:center;"></div>
                
                <div class="avatar">
                    <img src="resources/images/CryoInfra-logo.png"
                         style="max-width:100%; height:auto; display:block;"
                         alt="CryoInfra Logo">
                </div>
                
                <c:choose>
                    <c:when test="${param.error == 'true'}">
                        <div class="error">
                            <span style="color:red;font-weight:bold;">ERROR: </span>
                            ${fn:replace(SPRING_SECURITY_LAST_EXCEPTION.message, 'Credenciales incorrectas', 'Username/Password are incorrect')}
                        </div>
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
                    <button name="buttonLogin" type="submit" class="newSupplierButton" onClick="login(this.form)">Login</button>
                </form>
                
                <br />		
                
                <form action="${pageContext.request.contextPath}/requestTicketPage.action" method="get">
                    <input style="width:180px;" type="submit" value="Tickets de solicitud" class="newSupplierButton"/>
                </form>
                
                <br />
                <a class="forgetPass" href="${pageContext.request.contextPath}/requestResetPassword.action" 
                   style="font-size:13px;text-align:center;font-weight: bold;">
                    ¿Olvidó su contraseña?
                </a>
                
                <br/>	         
                <br/>	
                <a href="resources/Manual Proveedores V_1.1.pdf" download>
                    <p style="font-size:13px" class="downloadDocSupp">Descargar manual de proveedores</p>
                </a> 
                
                <!-- <div class="footer" style="background-color: #EFF5F9 !important;">
                    &copy; 2000-2025 Smartech Consulting Group S.A. de C.V.<br>Derechos Reservados.
                </div> -->
                
                <div class="footer" style="background-color: #EFF5F9 !important;">
				    &copy; 2000-<%= java.time.Year.now().getValue() %> Smartech Consulting Group S.A. de C.V.<br>Derechos Reservados.
				</div>
            </div>
        </div>
    </div>
</div>
</body>
</html>