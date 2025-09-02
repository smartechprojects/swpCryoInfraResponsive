<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="shortcut icon" href="resources/images/favicon.ico" />

<link rel="stylesheet" type="text/css" href="resources/css/style.css">
<link rel="stylesheet" type="text/css"
	href="${url}/resources/css/SupplierApp-all.css">
<link rel="stylesheet" type="text/css"
	href="${url}/resources/css/app.css">
<link rel="stylesheet" href="resources/css/login.css" media="screen"
	type="text/css" />

<title>CryoInfra&copy;-Cambio de contraseña</title>
<script>
	var displayName = '<c:out value="${name}"/>';
	var userName = '<c:out value="${userName}"/>';
	var message = '<c:out value="${message}"/>';
</script>

<style>

/* The message box is shown when the user clicks on the password field */
#message {
	display: none;
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

</style>
</head>
<body>
	<div class="wrap">
		<div
			style="margin-top: 50px; margin-bottom: 50px; text-align: center;">
			<p>
				<img src="resources/images/hand-click.png" style="width: 300px;">
			</p>

		</div>
		<div class="avatar">
			<img src="resources/images/CryoInfra-logo-gris.png"
				style="height: 60px; width: 250px;">
		</div>

		<div style="text-align: center;">
			<h2>Cambio de contraseña</h2>

			<form
				action="${pageContext.request.contextPath}/changePassword.action"
				method="post">
				<label for="usrname">Usuario:</label>
				<input type="text" id="usrname" name="usrname" required style="border: none;text-align:center;"> 

					<label for="psw">Nueva
					Contraseña:</label> <input type="password" id="psw" name="psw"
					style="width: 100px;" maxlength="8"
					pattern="(?=.*[!@#&$|_.%])[0-9a-zA-Z!@#&$|_.%0-9]{8}$"
					title="Contraseña de 8 caracteres. Alfanumérica. Debe manejar 1 o mas caracteres especiales: !@#&$|_.%"
					oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"
					required> <br /> 
					
					<button name="buttonLogin" type="submit"
					onClick="validAll()">Enviar</button>

			</form>
		</div>

	</div>


	<div id="message" style="text-align:center;">
		<h3>La contraseña debe cumplir lo siguiente:</h3>
		<p id="letter" class="invalid">
			Una o más <b>letras minúsculas</b>
		</p>
		<p id="capital" class="invalid">
			Una o más <b>letras mayúsculas</b>
		</p>
		<p id="number" class="invalid">
			Uno o más <b>dígitos</b>
		</p>
		<p id="length" class="invalid">8 caracteres exactamente</p>
		<p id="special" class="invalid">
			Mínimo <b>dos de los siguientes caracteres especiales: </b>!@#&$|_.%
		<p>
	</div>

	<script>
		function validAll() {
			var letterFinal = document.getElementById("letter").classList.value;
			var capitalFinal = document.getElementById("capital").classList.value;
			var numberFinal = document.getElementById("number").classList.value;
			var lengthFinal = document.getElementById("length").classList.value;
			var specialFinal = document.getElementById("special").classList.value;

			if (letterFinal == 'invalid' || capitalFinal == 'invalid'
					|| numberFinal == 'invalid' || lengthFinal == 'invalid'
					|| specialFinal == 'invalid') {
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

		document.getElementById('usrname').value = userName;
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
			if (myInput.value.match(lowerCaseLetters)) {
				letter.classList.remove("invalid");
				letter.classList.add("valid");
			} else {
				letter.classList.remove("valid");
				letter.classList.add("invalid");
			}

			// Validate capital letters
			var upperCaseLetters = /[A-Z]/g;
			if (myInput.value.match(upperCaseLetters)) {
				capital.classList.remove("invalid");
				capital.classList.add("valid");
			} else {
				capital.classList.remove("valid");
				capital.classList.add("invalid");
			}

			// Validate numbers
			var numbers = /[0-9]/g;
			if (myInput.value.match(numbers)) {
				number.classList.remove("invalid");
				number.classList.add("valid");
			} else {
				number.classList.remove("valid");
				number.classList.add("invalid");
			}

			// Validate length
			if (myInput.value.length >= 8) {
				length.classList.remove("invalid");
				length.classList.add("valid");
			} else {
				length.classList.remove("valid");
				length.classList.add("invalid");
			}

			// Validate special
			var specialChars = /[!@#&$|_.%]/g;
			if (myInput.value.match(specialChars)) {
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