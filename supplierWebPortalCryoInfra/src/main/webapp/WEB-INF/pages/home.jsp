<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=10, user-scalable=yes">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="shortcut icon" href="resources/images/favicon.ico" />
<title>CryoInfra&copy;-Portal de proveedores</title>

    <!-- The line below must be kept intact for Sencha Cmd to build your application -->
    <script id="microloader" data-app="04cb71bd-d946-4506-8c66-f4d8d90cb33c" type="text/javascript" src="bootstrap.js"></script>

	<link rel="stylesheet" type="text/css" href="${url}/resources/css/SupplierApp-all.css">
	<link rel="stylesheet" type="text/css" href="${url}/resources/css/app.css">    
    <script type="text/javascript" charset="utf-8" src="${url}/ext/build/ext-all.js"></script>
	<script type="text/javascript" charset="utf-8" src="${url}/resources/js/common.js"></script>

<script>
    var SuppAppMsg='';
	var userId = '<c:out value="${id}"/>';
	var displayName = '<c:out value="${name}"/>';
	var userName = '<c:out value="${userName}"/>';
	var role = '<c:out value="${role}"/>';
	var displayType = '<c:out value="${type}"/>';
	var userType = '<c:out value="${userType}"/>';
	var welcomeMessage = '<c:out value="${welcomeMessage}"/>';
	var addressNumber = '<c:out value="${addressNumber}"/>';
	var userEmail = '<c:out value="${email}"/>';
	var invException = '<c:out value="${invException}"/>';
	var multipleRfc = '<c:out value="${multipleRfc}"/>';
	var approveNotif = '<c:out value="${approveNotif}"/>';
	var pendingDocs = '<c:out value="${pendingDocs}"/>';
	var osSupplier = '<c:out value="${osSupplier}"/>';
	var Flete = '<c:out value="${Flete}"/>';
	var dataSupplier = "";
	var supplierProfile = null;
	var tabChgn = "";
	var uuidPlantAccess = "";
	var uuidPlantAccessWorker = "";
	var statusPlantAccess = "";
	var checkPlantAccess = false;
	var isSupplier = '<c:out value="${isSupplier}" default="false"/>'.toLowerCase() === 'true';//Booleano
	var isSubUser = '<c:out value="${isSubUser}" default="false"/>'.toLowerCase() === 'true';//Booleano
	var isMainSupplierUser = '<c:out value="${isMainSupplierUser}" default="false"/>'.toLowerCase() === 'true';//Booleano
	
	var numeroUsuario = "";
	var telefono = "";	
	var correo = "";
	
	Ext.Ajax.request({
	    url: 'supplier/getByAddressNumber.action',
	    method: 'POST',
	    params: {
	    	addressNumber : addressNumber
        },
	    success: function(fp, o) {
	    	var res = Ext.decode(fp.responseText);
	    		    	
        	supplierProfile = Ext.create('SupplierApp.model.Supplier',res.data);
	    	       	        	
        	numeroUsuario = supplierProfile.data.addresNumber;
        	telefono = supplierProfile.data.telefonoDF;
        	correo = supplierProfile.data.emailSupplier;
	    	
	    }
	}); 
	 
</script>


</head>
<body>
	<div id="page-loader"></div>
	<div id="loading">
	   <span id="loading-message">Loading components. Please wait...</span>
	</div>
	<div id="content"></div>
</body>
</html>