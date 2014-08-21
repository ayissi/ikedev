<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
   <title>Bond Parameters</title>    
   <style>
#bondform {
	background: #9AB4D7;
	padding: 20px;
	width: 400px;
	align: center;
}

#bondform fieldset {
  margin-bottom: 30px;
 }

</style>
				
</head>
<body>
	<form:form id="bondform" modelAttribute="bond">
	<fieldset>  
	  <legend> Paramètres de l'obligation </legend>
	     <label for="periodicityInput">Periodicity:</label>
	     <form:input type="number" min="1" max="10" value="5" path="periodicity" id="periodicityInput"/> 
		 <br>		
	     <label for="maturityInput">Maturite (en années):</label>
	     <form:input type="number" min="1" max="30" value="15" path="maturity" id="maturityInput"/> 
	     <br>	
	     <label for="nominalInput">Nominal: </label>
	     <form:input type="range" min="100" max="1000" value="125" path="nominal" id="nominalInput"/> 
	     <br>
	     <label for="emissionDateInput">Date d'emission: </label>
	     <form:input type="date" path="emissionDate" id="emissionDateInput"/> 
	     <br>
	     <label for="dateDebutInput">Début: </label>
	     <form:input type="date" path="dateDebut" id="dateDebutInput"/> 
	     <br>
	     <label for="dateFinInput">Fin: </label>
	     <form:input type="date" path="dateFin" id="dateFinInput"/>  
	     <br>
	 </fieldset> 
	 	   <tr>
	   <input type="submit" value="Valider"/>
	   </tr>
	</form:form>  
    ${message1}      ${message2}

</body>

</html>

