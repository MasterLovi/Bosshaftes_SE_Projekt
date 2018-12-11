<%@ page language="java" pageEncoding="utf-8" contentType="text/html"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
		<title>Registrieren</title>
		<link rel="stylesheet" type="text/css" href="stylesheets/positioning.css">
        <link rel="stylesheet" type="text/css" href="stylesheets/design.css">
        
        <script src="utilities/javaScript/ajax.js"></script>
      	<script src="utilities/javaScript/jquery-3.3.1.min.js"></script>
	</head>
	<body>
		<div id='headerbar'>
    		<div id='logoWrapper'>
	        	<a href='index.jsp'><img id='mainLogo' src='utilities\pic\MainLogo.jpg'></a>
	        </div>
	        <div id='otherMapHeader'>
	    		<h2 id='mapHeader'>Registrieren</h2>
	    	</div>
    	</div>
    	<div class='optionPanel'>
    		<h2>Bitte geben Sie die folgenden Informationen ein</h2>
    		<p id="error"></p>
    		<form id='regForm' autocomplete="off" action='' method='POST'>
    			<input type='text' name='username' class='regText' placeholder='Nutzername' required /><br/>
    			<input type='email' name='email' class='regText' placeholder='E-Mail-Adresse' required /><br/>
    			<input type='password' name='password' class='regText' placeholder='Passwort' required /><br/>
    			<input type='password' name='passwordRep' class='regText' placeholder='Passwort wiederholen' required /><br/>
    			<input type='submit' name='submit' class='regButton button' value='Registrieren' />
    		</form>
    	</div>
		<%@ include file="/utilities/footer.jsp" %>
		<script>
			$(document).ready(function () {
				$("#regForm").submit(function(){
					regUser();
					return false;
				});
			});
		</script>
	</body>
</html>