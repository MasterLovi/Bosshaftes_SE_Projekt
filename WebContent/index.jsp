<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Test JSP</title>
        <link rel="stylesheet" type="text/css" href="Stylesheets/main.css">
        <link rel="stylesheet" type="text/css" href="Stylesheets/footer.css">
    </head>

    <body>
    	<center>
    	<div id='headerbar'>
    	<div id='otherHeader'>
    		<div id='logoWrapper'>
	        	<img id='mainLogo' src='utilities\pic\testLogo.jpg'>
	        </div>
    	</div>
    	<div id='loginArea'>
   			<form id='loginForm'>
   				<table>
   				<tr>
   					<td><input type='text' name='username' class='inputText' placeholder='Username' /></td>
   					<td><input type='password' name='password' class='inputText' placeholder='Password' /></td>
   				</tr>
   				<tr>
   					<td><button id='linkButton' class='inputSubmit'>Registrieren</button></td>
   					<td><input type='submit' class='inputSubmit' name='Login'/></td>
   				</tr>
   				</table>
   			</form>
   		</div>
   		</div>
   		</center>
	    <center>
	    	<div id='headMsgWrapper'>
	    		<h1 id='headMsg'>Your time is precious! Use it to ... </h1>
	    		
	    	</div>
	        
	   	</center>
	   	
   		<center>
        <div id='opOne' class='centered'> 
        	<div id='opOneWrapper' class='centered'>
        		<h2>Parteyyyy</h2>
        		<div class='infoWrapper'>
	        		<p class='optionInfo'><img class='optionImg' align='left' src='utilities\pic\OP1.jpg' />Some bla bla with some other bla bla in order to bla bla that this bla bla will always be considered as the bla bla that it is. Some bla bla with some other bla bla in order to bla bla that this bla bla will always be considered as the bla bla that it is.Some bla bla with some other bla bla in order to bla bla that this bla bla will always be considered as the bla bla that it is.Some bla bla with some other bla bla in order to bla bla that this bla bla will always be considered as the bla bla that it is.</p>
	        		<div class='buttonWrapper'>
	        			<button class='actionButton'>Find a tour that fits your time</button>
	        		</div>
        		</div>
        	</div>
        </div>
         <div id='opTwo' class='centered'> 
        	<div id='opTwoWrapper' class='centered'>
        		<h2>Culture</h2>
        		<div class='infoWrapper'>
	        		<p class='optionInfo'><img class='optionImg' align='left' src='utilities\pic\OP2.jpg' />Some bla bla with some other bla bla in order to bla bla that this bla bla will always be considered as the bla bla that it is. Some bla bla with some other bla bla in order to bla bla that this bla bla will always be considered as the bla bla that it is.Some bla bla with some other bla bla in order to bla bla that this bla bla will always be considered as the bla bla that it is.Some bla bla with some other bla bla in order to bla bla that this bla bla will always be considered as the bla bla that it is.</p>
	        		<div class='buttonWrapper'>
	        			<button class='actionButton'>Find a tour that fits your time</button>
	        		</div>
        		</div>
        	</div>
        </div>
        </center>

         <%@ include file="utilities/footer.jsp" %>

     </body>
 </html>