<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

   <title>Bond Parameters</title>

  
   <link  rel="stylesheet" media="screen" href="reset.css" type="text/css"  />
   <link  rel="stylesheet" media="screen"  href="base.css" type="text/css"  />
   <script src="modernizr-1.7.js"></script>
   
   <style>
#orderform {
	background: #9AB4D7;
	padding: 20px;
	width: 400px;
}

#orderform fieldset {
margin-bottom: 30px;

 }


</style>

</head>
<body>
	<form id="orderform" action="" method="post"> 
	<fieldset>
    <legend> Paramètres de l'obligation </legend> 	
   <tr> 
   <td> Periodicite <input type ="number" min="1" max="10" value="2"> </td>
   <td> Emise Le: <input type="date"> </td>
   </tr> 
   <tr> 
   <td> Maturite(en années) <input type ="number" min="1" max="30" value="10"> </td>
   <td> Debut: <input type="date"> </td>
   </tr> 
   <tr> 
   <td>Nominal <input type="range" min="100" max="1000"  value="125"> </td>
   <td>Fin: <input type="date"> </td>
   </tr>
    </fieldset>
  <input type="submit" value="Valider"> 
</form>
</body>

</html>

