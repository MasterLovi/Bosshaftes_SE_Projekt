<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
		<title>Registrieren</title>
		<link rel="stylesheet" type="text/css" href="Stylesheets/positioning.css">
        <link rel="stylesheet" type="text/css" href="Stylesheets/design.css">
	</head>
	<body>
		<div id='headerbar'>
    		<div id='logoWrapper'>
	        	<a href='index.jsp'><img id='mainLogo' src='utilities\pic\testLogo.jpg'></a>
	        </div>
	        <div id='rightIcon'>
	    		<a href='index.jsp'><img class='rightIcon' src='utilities\pic\homeIcon.png'></a>
	    	</div>
	        <div id='otherMapHeader'>
	    		<h2 id='mapHeader'>Registrieren</h2>
	    	</div>
	    	
    	</div>
    	<div class='optionPanel'>
    		<h2>Pleas enter the following information</h2>
    		<form id='regForm' action='#'>
    			<input type='text' name='username' class='regText' placeholder='Username' /><br/>
    			<input type='email' name='email' class='regText' placeholder='E-Mail-Address' /><br/>
    			<input type='password' name='password' class='regText' placeholder='Password' /><br/>
    			<input type='password' name='passwordRep' class='regText' placeholder='Repeat Password' /><br/>
    			<input type='submit' name='submit' class='regButton' value='Register' />
    		</form>
    	</div>
		<%@ include file="/utilities/footer.jsp" %>
	</body>
</html>