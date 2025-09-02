<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">

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
            		          <td rowspan=2 style='font-size:26px;border-right:1px solid #333;color:#000;padding-bottom:7px;'>&nbsp;</td> 
            		          <td  rowspan=2 style='width:700px;text-align:left;color:#B6985A;font-size:17px;'>&nbsp;&nbsp; 
            		          Bienvenido <br />&nbsp;&nbsp;&nbsp;<script  type="text/javascript"> document.write(displayName)</script>  </td> 
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

<div class="container" style="margin-left:100px;margin-right:100px;text-align: justify; text-justify: inter-word;border:0px;padding:20px;">


<h2 style="text-align:center;">AVISO DE CONFIDENCIALIDAD PARA EMPLEADOS CRYOINFRA </h2>
<br />


“EL TRABAJADOR” reconoce que con motivo de su relación de trabajo con la “EMPRESA”
y durante el desempeño de sus funciones tendrá acceso a información, materiales,
sistemas y documentos propiedad exclusiva de la “EMPRESA” y/o de sus clientes,
proveedores, filiales y/o subsidiarias, incluyendo acceso a cualquier información o
documentación contenida en la plataforma conocida como “Portal de Proveedores”, misma
que es considerada como confidencial o reservada (en lo sucesivo la “Información
Confidencial”). 
 <br /><br />
 
 Por tanto, “EL TRABAJADOR” se obliga a (i) manejar la Información Confidencial como
estrictamente reservada; (ii) no divulgar ni proporcionar a persona alguna, incluyendo a
familiares, la Información Confidencial, sin la autorización expresa previa y por escrito por
parte de la “EMPRESA”; (iii) utilizar la Información Confidencial exclusivamente para el
desempeño de sus funciones y cumplimiento específico de las tareas asignadas y para
ningún otro propósito; (iv) Nunca permitir a ningún tercero, incluyendo familiares, el acceso
a la Información Confidencial; (v) a no utilizar dicha Información Confidencial en beneficio
propio o de terceros; (vi) no reproducir, grabar, copiar Información Confidencial, ni
removerla de las instalaciones de la “EMPRESA” o de las instalaciones en donde la
“EMPRESA” preste servicios, excepto cuando se requiera para el desempeño de sus
labores para la “EMPRESA”, en cuyo caso “EL TRABAJADOR” deberá, en todo momento,
tomar las medidas necesarias para preservar su confidencialidad y prevenir su divulgación. 
<br /><br />

“EL TRABAJADOR” se obliga además a devolver la Información Confidencial en el
momento en que la “EMPRESA” así lo requiera, así como al término de su relación laboral,
sin retener copia alguna de dicha información. “EL TRABAJADOR” deberá reportar
inmediatamente a la “EMPRESA” la pérdida de Información Confidencial con la finalidad de
que se tomen las medidas de seguridad correspondientes. 
<br /><br />

El incumplimiento y/o violación por parte de “EL TRABAJADOR” de cualquiera de estas
obligaciones será considerado como causal de rescisión de su contrato de trabajo sin
responsabilidad alguna para la “EMPRESA”, independientemente de la responsabilidad civil
y penal en la que pudiera incurrir “EL TRABAJADOR” de conformidad con la Ley de
Propiedad Industrial, el Código Penal Federal, o cualquier otra legislación aplicable. 
<br /><br />

Ratifico la obligación de confidencialidad que me corresponde frente a la “EMPRESA”, y
acepto que las obligaciones que asumo conforme al Aviso de Confidencialidad anterior
continuarán en vigor indefinidamente al término de mi contrato laboral, aún y cuando la
relación de trabajo termine por cualquier motivo. Asimismo, asumo el compromiso de no
destinar para fines personales la información o documentos mencionados en este Aviso
 <br /><br />


  <form action="${pageContext.request.contextPath}/acceptAgreement.action" method="post">
    <input type="hidden" id="userName" name="userName"  value = "${userName}">
    <input type="submit" value="ACEPTAR EL AVISO DE CONFIDENCIALIDAD" style="width:350px;margin:auto;display:block;">
  </form>
</div>


</body>
</html>