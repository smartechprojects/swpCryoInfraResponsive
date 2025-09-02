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


<h2 style="text-align:center;">AVISO DE PRIVACIDAD INTEGRAL PARA PERSONAL DE
PROVEEDORES Y VISITANTES DE GRUPO UNIVERSAL MUSIC MEXICO. </h2>
<br />

<h3>I.- IDENTIDAD Y DOMICILIO DEL RESPONSABLE 
UNIVERSAL MUSIC MEXICO, S.A. DE C. V. (GRUPO UNIVERSAL)</h3>con domicilio en Río Tigris No.33, Col. Cuauhtémoc, Demarcación Cuauhtémoc, Ciudad de México, C.P. 06500 es responsable de los datos personales que trata de sus proveedores, por lo que en términos de lo dispuesto por la Ley Federal de Protección de Datos Personales en Posesión de los Particulares, su Reglamento y demás disposiciones aplicables y vigentes en la materia (en adelante la Ley), pone a disposición de sus proveedores el presente aviso de privacidad. 
	 
<h3>II.- DATOS PERSONALES SOLICITADOS</h3> 
GRUPO UNIVERSAL podrá solicitarles a sus proveedores y visitantes, las siguientes categorías de datos personales: 
<br />  
A.     Si es persona física: <br />
i)	Datos de identificación (nombre, firma, domicilio particular, y número de teléfono particular). <br />
ii)	Copia de Identificación oficial <br />
iii)	Alta de ante la autoridad fiscal (en caso de ser extranjero formato W9)<br /> 
iv)	Constancia de Situación Fiscal <br />
v)	Comprobante de domicilio <br />
vi)	Estado de cuenta bancario <br />
  <br /><br />
B.     Si es persona moral: <br />
i)	Acta Constitutiva <br />
ii)	Poder del Representante Legal <br />
iii)	Datos de identificación de representante legal (nombre, firma, correo electrónico laboral y teléfono de oficina o celular de trabajo) <br />
iv)	Copia de Identificación del Representante Legal <br />
v)	Comprobante de domicilio <br />
vi)	Constancia de Situación Fiscal <br />
vii)	Estado de cuenta bancario <br />

<h3>III.- FINALIDADES</h3> 
Los datos personales señalados en la fracción anterior serán tratados por GRUPO UNIVERSAL para las siguientes finalidades: 
  <br />
A.     Tramitar su alta como proveedor. <br />
B.     Llevar un registro de datos de los proveedores y sus medios de contacto <br />
C.     Realizar cotizaciones para los productos y/o servicios suministrados <br />
D.     Realizar pagos y transferencias bancarias <br />
E.     Realizar contratos<br /> 
  <br />
Es importante mencionar que las finalidades antes mencionadas son necesarias para brindarle acceso a las instalaciones de GRUPO UNIVERSAL, así como para dar trámite a su solicitud para proveerle algún producto o servicio.   
  <br />
Finalidades No Necesarias para su relación con GRUPO UNIVERSAL. 
Fines de mercadotecnia, publicitarios y/o de prospección comercial de productos y/o servicios propios de GRUPO UNIVERSAL, de las empresas en su mismo grupo corporativo y de otros terceros. 

<h3>IV.	NEGATIVA AL Y LIMITACIÓN DEL TRATAMIENTO DE DATOS PERSONALES. </h3>
Los Titulares podrán manifestar su negativa al tratamiento de sus Datos Personales en cualquier momento, manifestando por escrito la Finalidad No Necesaria respectiva al calce de este Aviso de Privacidad. 
  
<h3>V.- TRANSFERENCIAS DE DATOS PERSONALES</h3> 
GRUPO UNIVERSAL podrá transferir sus datos personales para que éstos sean tratados nacional e internacionalmente por terceros. En este sentido y con fundamento en lo dispuesto en los artículos 36 y 37 de la Ley y 68 de su Reglamento, dichos Datos Personales podrían ser transferidos sin necesidad del consentimiento de los Titulares cuando dicha transferencia sea hecha a su matriz, a sociedades controladas por GRUPO UNIVERSAL o bajo el mismo control común que ésta, afiliadas y/o integrantes del mismo grupo corporativo que GRUPO UNIVERSAL y que operen bajo los mismos procesos y políticas de privacidad internas. 
  
Adicionalmente, GRUPO UNIVERSAL podrá realizar transferencias nacionales o internacionales de los Datos Personales de los Titulares, a terceros que operen en el sector del entretenimiento o corporativo como parte o consecuencia de operaciones o transacciones, presentes o futuras, para la venta o transferencia de acciones o partes sociales, fusión, consolidación, cambio de control, transferencia de activos, reorganización o liquidación. Para realizar estas transferencias, GRUPO UNIVERSAL solicita su consentimiento expreso y por escrito al calce del presente aviso. 
  
Los terceros receptores asumirán las mismas obligaciones que correspondan a GRUPO UNIVERSAL, según resulte aplicable, y pondrán sus propios Avisos de Privacidad a disposición de los Titulares oportunamente. 
  
<h3>VI.- OPCIONES PARA LIMITAR EL USO O DIVULGACION DE SUS DATOS PERSONALES </h3>
Los Titulares podrán limitar el uso o divulgación de sus Datos Personales de manera directa enviando un correo a <a href = "mailto: protecciondedatos@umusic.com">protecciondedatos@umusic.com</a> , indicando por escrito la finalidad materia de la limitación solicitada, y requiriendo su inscripción en el listado de exclusión del Responsable en caso de que tal petición resulte procedente. Dichos Titulares asumen la consecuencia de que la limitación del tratamiento de sus Datos Personales pueda incidir negativamente sobre su relación con el Responsable, o inhibirla completamente. 
  
<h3>VII.- SOLICITUD DE ACCESO, RECTIFICACIÓN, CANCELACIÓN U OPOSICIÓN DE DATOS PERSONALES (SOLICITUD ARCO) </h3>
Conforme a la legislación aplicable y vigente en los Estados Unidos Mexicanos, los Titulares tienen en todo momento el derecho de acceder a, rectificar, cancelar u oponerse a finalidades concretas del tratamiento que GRUPO UNIVERSAL da a sus Datos Personales; derechos que podrán hacer valer mediante petición que para tal efecto dirijan al correo protecciondedatos@umusic.com, o bien mediante correo postal enviado al domicilio en el proemio de este documento, con la siguiente información y documentos adjuntos:
 <br />
i.	Copia simple del anverso y reverso de una identificación oficial vigente con fotografía, firmada al calce. <br />
ii.	Si se ejercen los derechos ARCO a través de un representante, copia de la carta poder original firmada, así como copia firmada de la identificación de ese representante, <br />
iii.	Si la solicitud es para ejercer el derecho de Rectificación, copia de los documentos que acrediten los cambios solicitados, y <br />
iv.	Señalamiento del domicilio o medio por el cual recibirá la respuesta correspondiente, en el entendido que de no indicarlo se podrá omitir la repuesta a su solicitud. <br />
  <br />
La omisión de cualquiera de los requisitos arriba mencionados motivará un requerimiento dentro de los cinco (5) días hábiles siguientes a la recepción de su solicitud. Usted contará con un plazo de diez (10) hábiles para subsanar las omisiones, pues de lo contrario su solicitud será desechada sin responsabilidad para GRUPO UNIVERSAL. Se le dará respuesta dentro de los veinte (20) días hábiles siguientes, y de ser afirmativa el resultado de ésta deberá ser implementado dentro de los quince (15) días posteriores al vencimiento de ese plazo. Estos plazos podrán ser duplicados por el Responsable, a su discreción por una sola vez. La Cancelación de Datos Personales se llevará a cabo una vez transcurridos los plazos legales durante los que debieran ser conservados, antes de lo cual estarán bloqueados. 
  <br />
Para el caso de las solicitudes de acceso y siempre que éstas resulten procedentes conforme a la Ley y demás legislación aplicable y vigente, GRUPO UNIVERSAL entregará al Titular, una copia del registro que tiene de sus Datos Personales (i) mediante formato impreso o (ii) archivo en formato PDF al correo electrónico señalado por el Titular en la solicitud de acceso. 
  
<h3>VIII.- REVOCACIÓN DEL CONSENTIMIENTO </h3>
Los Titulares podrán revocar el consentimiento previamente otorgado para el tratamiento de sus Datos Personales, derecho que podrán hacer valer enviando un correo electrónico a protecciondedatos@umusic.com, siguiendo el procedimiento indicado para una solicitud de oposición. Es importante tener en cuenta que no en todos los casos podremos atender la solicitud o concluir el uso de sus datos de forma inmediata, ya que es posible que por alguna obligación legal requiramos seguir tratándolos, lo cual le será comunicado en su caso 

<h3>IX.- MODIFICACIONES AL AVISO DE PRIVACIDAD </h3>
Este aviso de privacidad podrá ser modificado de tiempo en tiempo por GRUPO UNIVERSAL. Dichas modificaciones serán oportunamente informadas por cualquiera de los medios de contacto que le hubieran proporcionado a dicho Responsable o por cualquier otro medio de comunicación disponible para GRUPO UNIVERSAL. 
  
<h3>X.- CONSENTIMIENTO </h3>
Al aceptar, manifiesto que otorgo el consentimiento expreso y por escrito para que GRUPO UNIVERSAL realice el tratamiento de mis Datos Personales, incluyendo los Datos Personales financieros y sensibles, para las finalidades referidas en el presente aviso. 
 <br /><br />
<strong>Asimismo, otorgo el consentimiento expreso y por escrito para la transferencia de estos a terceros siempre que dicha transferencia se realice conforme al apartado de Transferencia de Datos Personales. </strong>
 <br /><br /> <br /><br />


  <form action="${pageContext.request.contextPath}/acceptAgreement.action" method="post">
    <input type="hidden" id="userName" name="userName"  value = "${userName}">
    <input type="submit" value="ACEPTAR EL AVISO DE PRIVACIDAD" style="width:350px;margin:auto;display:block;">
  </form>
</div>


</body>
</html>