<%@ page language="java" pageEncoding="utf-8" contentType="text/html"%>
<%@page import="java.util.*"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Test JSP</title>
        <link rel="stylesheet" type="text/css" href="stylesheets/positioning.css">
        <link rel="stylesheet" type="text/css" href="stylesheets/design.css">
        <link rel="stylesheet" type="text/css" href="stylesheets/icon.css">
     	
   	    <script src="utilities/javaScript/jquery-ui.min.js"></script>
   	    <script src="utilities/javaScript/modifier.js"></script>
    </head>

    <body>

    	<div id='headerbar'>
	    	<div id='otherHeader'>
	    		<div id='logoWrapper'>
		        	<a href='index.jsp'><img id='mainLogo' src='utilities\pic\MainLogo.jpg'></a>
		        </div>
	    	</div>
	    	<div id='loginArea'>
				<% 
				
					if (session.getAttribute("loggedin") != null && Boolean.valueOf(session.getAttribute("loggedin").toString())){
		   				%>
		   					<jsp:include page="utilities\loggedIn.jsp"></jsp:include>
	   					<%
						
					} else {
						
						%>
							<jsp:include page="utilities\logIn.jsp"></jsp:include>
						<%
					}
				
				%>	
   			</div>
	   	
   		</div>

    	<div id='headMsgWrapper'>
    		<h1 id='headMsg'>Deine Zeit ist kostbar! Nutze sie um ... </h1>
    	</div>
	
        <div class='optionPanel'> 
        	<div class='opWrapper'>
        		<h2>Party zu erleben</h2>
        		<div class='infoWrapper'>
	        		<p class='optionInfo'><img class='optionImg' align='left' src='utilities\pic\Party_round.png' />
	        			Businessmeeting, auf der Durchreise oder die Verwandten besuchen gewesen?
						Du kennst niemanden und bist auch noch nie hier gewesen?
						Wenn du deinen Abend nicht im Hotelzimmer totschlagen willst bist du hier genau richtig!
						Einfach klicken und die besten Clubs, Bars und sonstige Party-Locations in einer Übersicht.
						Egal ob du die Nacht durchfeiern willst oder nur 2 Stunden Zeit hast bis dein Zug kommt.
						Hier kommt jeder auf seine Kosten.
						Die ganz Harten können auch direkt eine Kneipen- oder Discotour machen!
						Viel Spaß und let the party never stop :D
					</p>
	        		<div class='buttonWrapper'>
	        			<a href="NewMap.jsp?type=Party"><button class='actionButton button' >Finde eine Tour die zu deiner Zeit passt</button></a>
	        		</div>
        		</div>
        	</div>
        </div>
         <div class='optionPanel'> 
        	<div class='opWrapper'>
        		<h2>Kultur zu erfahren</h2>
        		<div class='infoWrapper'>
	        		<p class='optionInfo'><img class='optionImg' align='left' src='utilities\pic\Culture_round.png' />
	        			Auf der Durchreise, oder sogar etwas zu erledigen?
						Sie haben noch etwas Zeit zu Verfügung bis ihre Bahn fährt oder das Meeting anfängt?
						Sie sind an den kulturellen Hotspots interessiert die die Region zu bieten hat?
						Dann sind Sie hier genau richtig.
						Einfach Ihre verbleibende Zeit eingeben und schon können Sie sich auf eine von vielen Touren durch Altstadt, Museen, Kulturzentren und sonstigen Sehenswürdigkeiten machen.
						Erleben Sie unvergessliche Stunden, starten Sie gleich mit einer von vielen Kul-touren!
					</p>
	        		<div class='buttonWrapper'>
	        			<a href="NewMap.jsp?type=Kultur"><button class='actionButton button' >Finde eine Tour die zu deiner Zeit passt</button></a>
	        		</div>
        		</div>
        	</div>
        </div>
		<%@ include file="/utilities/footer.jsp" %>
     </body>
 </html>