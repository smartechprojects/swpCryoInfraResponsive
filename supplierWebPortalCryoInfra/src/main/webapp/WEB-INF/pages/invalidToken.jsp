<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="shortcut icon" href="resources/images/favicon.ico"/>
  <title>CryoInfra&copy;-Token Inválido </title>

<style>
/* Style all input fields */
body {
  font: 14px Tahoma, Verdana, sans-serif;
  background: #fff;
}

h2{
  font: 24px Tahoma, Verdana, sans-serif;
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
  height:99px;
  background-image: url(${url}/resources/images/topBanner3.png) !important; 
  background-repeat: no-repeat;
  background-size: 100% 100%;
}

.hdrTable tr td{
  color:#000;
  text-align:center;
  font: 14px Tahoma, Verdana, sans-serif;
}

.myButton {
	background-color:#B6985A;
	-moz-border-radius:5px;
	-webkit-border-radius:5px;
	border-radius:5px;
	border:1px solid #B6985A;
	display:inline-block;
	cursor:pointer;
	width:100px;
	color:#ffffff;
	font-family:font: 18px Tahoma, Verdana, sans-serif;
	font-size:13px;
	padding:2px 17px;
	text-decoration:none;
	text-shadow:0px -1px 0px #B6985A;
}
.myButton:hover {
	background-color:#000;
}

</style>
</head>
<body>

               <table height='100%' width='100%' class='hdrTable'>  
            		<tr> 
            		  <td style='width:60%;'>  
            		     <table height='50%' width='100%'> 
            		      <tr><td style='width:200px;'> &nbsp;</td> 
            		          <td rowspan=2 style='font-size:26px;border-right:1px solid #333;color:#000;padding-bottom:7px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
            		          <td  rowspan=2 style='width:400px;text-align:left;color:red;font-size:25px;'>&nbsp;&nbsp; 
            		          Token Inválido <br />&nbsp;&nbsp;&nbsp;</td> 
            		          <td><div style="float:right" >
	<a href='${pageContext.request.contextPath}/login.action'  style='font-size: 14px;padding-right: 50px;color: red;padding-top:15px;font-family: Poppins-Regular, sans-serif !important;'>Salir</a><br />
	</div></td>
            		          </tr> 
            		      </table> 
            		  </td> 
                      <td style='text-align:right;padding-left:150px;vertical-align:middle;font: 16px Tahoma, Verdana, sans-serif; color:#000;'>  
                     
                      </td> 
                      <td style='text-align:right;padding-right:40px;vertical-align:middle;'> 
                      &nbsp; 
                      </td> 
                      </tr></table>

<div class="container">

<p>El enlace utilizado para nuevos registro es inválido o ha expirado. Si desea una renovación o una solicitud de registro, comuníquese con su contacto en CryoInfra para solicitar un nuevo enlace de registro.</p>

</div>


</body>
</html>