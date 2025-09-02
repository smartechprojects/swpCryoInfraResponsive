<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="shortcut icon" href="resources/images/favicon.ico"/>
  <title>CryoInfra&copy;-Nuevo registro</title>
<link rel="stylesheet" type="text/css"
	href="${url}/ext-4.2.1/resources/css/ext-all-gray.css">
<script type="text/javascript" charset="utf-8"
	src="${url}/ext-4.2.1/ext-all.js"></script>
	<link rel="stylesheet" type="text/css"
	href="${url}/resources/css/app.css">

			<script type="text/javascript" charset="utf-8"
	src="${url}/ext-4.2.1/locale/ext-lang-es.js"></script>
	
	<script type="text/javascript" charset="utf-8"
	src="${url}/resources/js/ActionButtonColumn.js"></script>
<script type="text/javascript" charset="utf-8" src="${url}/app/publicApp.js"></script>
<script type="text/javascript" charset="utf-8"
	src="${url}/resources/js/common.js"></script>
	<script type="text/javascript" charset="utf-8"
	src="${url}/resources/js/MultiSelect.js"></script>
	


<script>
	var SuppAppMsgLogin = {};
	
	//var langu = window.navigator.language;
	var lang = "";
	if(window.navigator.language.startsWith("es", 0)){
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
	    	document.getElementById("new_register").innerHTML =SuppAppMsgLogin.newRegister;
	    	document.getElementById("exit").innerHTML = SuppAppMsgLogin.approvalExit
	    	//document.getElementById('title').innerHTML ='Bic&copy;-' + SuppAppMsgLogin.tabNewRegister;
	    	
	    }
	}); 

	var role = 'ANONYMOUS';
</script>

<input type="hidden" id="ticketAccepted" value="${ticketAccepted}" >

</head>
<body>
<table height='100%' width='100%' class='hdrTable'> 
            		<tr>
            		  <td style='width:60%;'>
            		     <table height='50%' width='100%'>
            		      <tr><td style='width:200px;'> &nbsp;</td>
            		          <td rowspan=2 style='font-size:26px;border-right:1px solid #000;color:#000;padding-bottom:7px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
           		          <td id="new_register"  rowspan=2 style='width:500px;text-align:left;color:#000;font-size:17px;'></td>
            		          </tr>
            		      </table>
            		  </td>
                     <td width=65% style='text-align:right;padding-right:50px;vertical-align:middle;font: 28px Tahoma, Verdana, sans-serif; color:#000;'>
	                      
	                      <a href='${pageContext.request.contextPath}/login.action' id="exit"  style='font-size: 14px;color: red;padding-top:5px;'></a><br /><br />
                      </td>
                     <td width=25% style='text-align:right;padding-right:40px;vertical-align:middle;'>
                      &nbsp;
                      </td>
                      </tr>
                      </table>
                      
<div id="content"></div>

<div style="text-align: center;margin-top:5px;"> 
<span style="font: 10px Tahoma, Verdana, sans-serif;">
                    	
	    &copy; 2000-2020 Smartech Consulting Group S.A. de C.V. Derechos Reservados.</span>
</div>

</body>
</html>