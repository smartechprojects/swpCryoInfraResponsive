<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<html>

<head>
	<script type="text/javascript" charset="utf-8" src="${url}/ext-4.2.1/ext-all.js"></script>	
	
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
	    	document.getElementById('buttonLogin').value = SuppAppMsgLogin.send;
	    }
	}); 
	
	</script>
	

  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  
  <link rel="shortcut icon" href="resources/images/favicon.ico"/>
  <title>CryoInfra&copy;-Nuevo Registro</title>
  <link rel="stylesheet" href="resources/css/login.css" media="screen" type="text/css" />

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

	<div style="float:right" >
	<a href='${pageContext.request.contextPath}/login.action'  style='font-size: 14px;padding-right: 50px;color: red;padding-top:15px;font-family: Poppins-Regular, sans-serif !important;'>Salir</a><br />
	</div>
	
	<div class="wrap">
  <div style="margin-top:50px;margin-bottom:50px;text-align:center;font:30px Poppins-Bold, sans-serif;color:#333;">
   <p style="font-family: Poppins-Regular, sans-serif !important;">Nuevos Proveedores</p>
</div>

		<div class="avatar">
      <img src="resources/images/CryoInfra-logo-gris.png">
      <br /><br /><br /><br />
		</div>
		<form action="${pageContext.request.contextPath}/openTicketRequest.action" method="POST">
		<input type="text" onkeyup="this.value = this.value.toUpperCase();" placeholder="RFC/TaxId" name="rfc" autofocus required>
		<input type="number" onkeyup="this.value = this.value.toUpperCase();" placeholder="Num. Ticket" name="ticket"   required >
		<br />
		<button id="buttonLogin" type="submit">Enviar</button>
		</form>
		<br />	
		<p style="text-align:center;color:red;">${errorRequest}</p>				
	
	</div>
	

	<div class="footer">
	    &copy; 2000-2021 Smartech Consulting Group S.A. de C.V. Derechos Reservados.</div>

</body>

</html>

