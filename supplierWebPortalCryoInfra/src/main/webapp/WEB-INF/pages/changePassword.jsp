<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="shortcut icon" href="resources/images/favicon.ico"/>
  <title>CryoInfra&copy;-Cambio de contraseña </title>
<script>
	var displayName = '<c:out value="${name}"/>';
	var userName = '<c:out value="${userName}"/>';
	var message = '<c:out value="${message}"/>';

</script>

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
            		          <td rowspan=2 style='font-size:26px;border:0px;color:#000;padding-bottom:0px;'>&nbsp;</td> 
            		          <td  rowspan=2 style='width:700px;text-align:center;font-size:17px;'>&nbsp;&nbsp; 
            		          <br />&nbsp;&nbsp;&nbsp;<script  type="text/javascript"> document.write(displayName)</script>  </td> 
            		          </tr> 
            		      </table> 
            		  </td> 
                      <td width:15%; style='text-align:right;padding-left:150px;vertical-align:middle;font: 16px Tahoma, Verdana, sans-serif; color:#000;'>  
                       <a href='j_spring_security_logout'  id='logoutLink' style='font-size: 14px;color: red;padding-top:5px;'>Salir</a> <br /><br /><br /> 
                      </td> 
                      <td width:5%; style='text-align:right;padding-right:40px;vertical-align:middle;'> 
                      &nbsp; 
                      </td> 
                      </tr></table>

<div class="container">

<h2>Cambio de contraseña</h2>

  <form action="${pageContext.request.contextPath}/changePassword.action" method="post">
    <label for="usrname">Usuario:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
    <input type="text" id="usrname" name="usrname"  required>
	<br />
    <label for="psw">Nueva Contraseña:</label>
    <input type="password" id="psw" name="psw" style="width:100px;" maxlength="8" pattern="(?=.*[!@#&$|_.%])[0-9a-zA-Z!@#&$|_.%0-9]{8}$" title="Contraseña de 8 caracteres. Alfanumérica. Debe manejar 1 o mas caracteres especiales: !@#&$|_.%" 
     oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" required>
    <br />
    <input type="submit" onclick="validAll()"  value="Enviar" class="myButton">
  </form>
  
  <c:if test="${message=='_SUCCESS'}">
              Su contraseña ha sido modificada de forma exitosa. 
              <a href='j_spring_security_logout'  id='logoutLink' style='font-size: 14px;color: red;padding-top:5px;'>
              Haga click aquí para regresar a la pantalla de registro</a>

  </c:if>
</div>

<div id="message">
  <h3>La contraseña debe cumplir lo siguiente:</h3>
  <p id="letter" class="invalid">Una o más <b>letras minúsculas</b></p>
  <p id="capital" class="invalid">Una o más <b>letras mayúsculas</b></p>
  <p id="number" class="invalid">Uno o más <b>dígitos</b></p>
  <p id="length" class="invalid">8 caracteres exactamente</p>
  <p id="special" class="invalid">Mínimo <b>dos de los siguientes caracteres especiales: </b>!@#&$|_.%<p>
</div>
				
<script>

function validAll() {   
	var letterFinal = document.getElementById("letter").classList.value;
	var capitalFinal = document.getElementById("capital").classList.value;
	var numberFinal = document.getElementById("number").classList.value;
	var lengthFinal = document.getElementById("length").classList.value;
	var specialFinal = document.getElementById("special").classList.value;
	
	if(letterFinal == 'invalid' ||
		capitalFinal == 'invalid' ||
		numberFinal == 'invalid' ||
		lengthFinal == 'invalid' ||
		specialFinal == 'invalid'){
		event.preventDefault();
		alert('Verifique cumplir los requisitos de contraseña solicitados');
	}
}

var myInput = document.getElementById("psw");
var letter = document.getElementById("letter");
var capital = document.getElementById("capital");
var number = document.getElementById("number");
var length = document.getElementById("length");
var special = document.getElementById("special");

document.getElementById('usrname').value=userName; 
document.getElementById("usrname").readOnly = true;

// When the user clicks on the password field, show the message box
myInput.onfocus = function() {
  document.getElementById("message").style.display = "block";
}

// When the user clicks outside of the password field, hide the message box
myInput.onblur = function() {
  document.getElementById("message").style.display = "none";
}

// When the user starts to type something inside the password field
myInput.onkeyup = function() {
  // Validate lowercase letters
  var lowerCaseLetters = /[a-z]/g;
  if(myInput.value.match(lowerCaseLetters)) {  
    letter.classList.remove("invalid");
    letter.classList.add("valid");
  } else {
    letter.classList.remove("valid");
    letter.classList.add("invalid");
  }
  
  // Validate capital letters
  var upperCaseLetters = /[A-Z]/g;
  if(myInput.value.match(upperCaseLetters)) {  
    capital.classList.remove("invalid");
    capital.classList.add("valid");
  } else {
    capital.classList.remove("valid");
    capital.classList.add("invalid");
  }

  // Validate numbers
  var numbers = /[0-9]/g;
  if(myInput.value.match(numbers)) {  
    number.classList.remove("invalid");
    number.classList.add("valid");
  } else {
    number.classList.remove("valid");
    number.classList.add("invalid");
  }
  
  // Validate length
  if(myInput.value.length >= 8) {
    length.classList.remove("invalid");
    length.classList.add("valid");
  } else {
    length.classList.remove("valid");
    length.classList.add("invalid");
  }
  
  // Validate special
  var specialChars = /[!@#&$|_.%]/g;
  if(myInput.value.match(specialChars)) {  
	  special.classList.remove("invalid");
	  special.classList.add("valid");
  } else {
	  special.classList.remove("valid");
	  special.classList.add("invalid");
  }
  
}
</script>

</body>
</html>