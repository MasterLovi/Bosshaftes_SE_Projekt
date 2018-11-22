<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%! 
	// All variables that we need to use all over the page have to be declared in this section so they have a global scope.
	public String title;
	public String partyTag;
	public String cultureTag;
%>

<%
	// Checking if the get parameter 'type' was set
	if (request.getParameter("type") != null){
		
		// Checking if the get parameter is set to 'party'. If so set all variables to party
		if(request.getParameter("type").equals("party")){
			title = "Party";
			partyTag = "checked";
			cultureTag = "";
			
		// Checking if the get parmater is set to 'culture' If so  set all variables to culture
		} else if (request.getParameter("type").equals("culture")){
			title = "Kultur";
			partyTag = "";
			cultureTag =  "checked";
		
		// If the get parameter is neither 'party' nor 'culture' set all the variables to default (party)
		} else {
			title = "Party";
			partyTag = "checked";
			cultureTag = "";
		}
		
	}
%>

<html>
	<head>
		<title>NewMap Design</title>
		
		<meta charset="UTF-8">
		
        <link rel="stylesheet" type="text/css" href="stylesheets/NewDesign.css">
        <link rel="stylesheet" type="text/css" href="stylesheets/NewPositioning.css">
        <link rel="stylesheet" type="text/css" href="stylesheets/icon.css">
        <link type="text/css" rel="stylesheet" href="https://api.mqcdn.com/sdk/mapquest-js/v1.3.2/mapquest.css"/>
        <link type="text/css" rel="stylesheet" href="https://api.mqcdn.com/sdk/place-search-js/v1.0.0/place-search.css"/>
        
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.4/dist/leaflet.css"
 		integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
   		crossorigin=""/>
    	
    	<script src="utilities/javaScript/jquery-3.3.1.min.js"></script>
    	<script src="utilities/javaScript/modifier.js"></script>
   	    <script src="utilities/javaScript/mapLoader.js"></script>
   	    <script src="utilities/javaScript/json.js"></script>
   	    <script src="utilities/javaScript/api.js"></script>
   	    <script src="utilities/javaScript/listener.js"></script>
   	    <script src="utilities/javaScript/ajax.js"></script>
   	    
   	    <script src="https://unpkg.com/leaflet@1.3.4/dist/leaflet.js"
  		integrity="sha512-nMMmRyTVoLYqjP9hrbed9S+FzjZHW5gY1TWCHA5ckwXZBadntCNs8kEqAWdrb9O7rxbCaA4lKTIWjDXZxflOcA=="
   		crossorigin="">
  	 	</script>
  	 	<script src="https://api.mqcdn.com/sdk/mapquest-js/v1.3.2/mapquest.js"></script>
  	 	
	</head>
	<body>
		<div class="absolute" id="header">
			<div id='logoWrapper'>
	        	<a href='index.jsp'><img id='mainLogo' src='utilities\pic\Title.png'></a>
	        </div>
	        <div class="floatRight" id='loginArea'>
	        <% 		
					if (session.getAttribute("loggedin") != null && (boolean)session.getAttribute("loggedin")){
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
	        <div id='otherMapHeader'>
	    		<div id='mapHeader'>
    				<i id="partyText">Party</i>
    				<i id="headerIconMiddle" class="material-icons">chevron_left</i>
    				<i id="cultureText">Kultur</i>
   				</div>
	    	</div>
		</div>
		<div class="absolute" id="searchbar">
			<span class="mq-place-search" >
				
				  <input class="mq-input" id="input-search" placeholder='Nach Ort suchen...'/>
				  <button class="mq-input-icon mq-icon-clear" onClick="clearInput('input-search')"><i class="material-icons">clear</i></button>
			</span>
		</div>
		<div class="absolute" id="optionpanle">
			<form action="" method="POST">
				<p>Sehenwürdigkeiten</p>
				<input type="text" id="spotValue" size="1" value="10" min="1" max="20">
			    <input type="range" id="spotRange" name="spots" min="1" max="20" />
				<hr>
				<p>Dauer der Touren</p>
				<input type="text" id="timeValue" size="1" value="2" min="1" max="24">
				<input type="range" id="timeRange" name="time" value="2" min="1" max="24" />
				<hr>
				<p>Bewertung</p>
				<div class="centered" id="rating">
					<i class="ratingStar material-icons activeStar" id="star1">grade</i>
					<i class="ratingStar material-icons activeStar" id="star2">grade</i>
					<i class="ratingStar material-icons activeStar" id="star3">grade</i>
					<i class="ratingStar material-icons" id="star4">grade</i>
					<i class="ratingStar material-icons" id="star5">grade</i>
				</div>
				<input type="hidden" value="3" name="rating" id="ratingValue">
				<hr>
				<input type="submit" value="Tour suchen" id="searchTour" onClick="$('#tours').show(); return false;" class="centered">
			</form>
		</div>
		<div id="tourInfoPanel">
			<i class="absolute material-icons" id="closeTourInfo">search</i>
		</div>
		<div id="map"></div>
		<div class="absolute" id="tours" style="display: none;">
			<div class="absolute" id="leftArrow" style="display: none;">
				<i class="material-icons bigIcon">chevron_left</i>
			</div>
			<ul id="tourList">
			
			</ul> 
			<div class="absolute" id="rightArrow">
				<i class="material-icons bigIcon">chevron_right</i>
			</div>
		</div>
		
		<!-- The create Modal -->
		<div id="myModal" class="modal">
		
		  <!-- Modal create content -->
		  <div class="modal-content">
		    <span class="close">&times;</span>
		    <form id="createLocationForm" method="POST" action="">
		    	<input type="hidden" name="lat" id="newLat" value=""/>
		    	<input type="hidden" name="lng" id="newLng" value=""/>
		    	<input type="hidden" name="userId" id="userId" value=""/>
		    	<table id="popupTable">
		    	<tr>
			    	<td>
			    		<p>Angezeigter Name:</p>
			    	</td>
		    		<td>
		    			<input type="text" name="locationName" value=""/>
		    		</td>
		    	</tr>
		    	<tr>
			    	<td>
			    		<p>Bild:</p>
			    	</td>
		    		<td>
		    			<input type="file" name="picture" value=""/>
		    		</td>
		    	</tr>
		    	<tr>
			    	<td>
			    		<p>Beschreibung:</p>
			    	</td>
		    		<td>
		    			<textarea rows="7" form="createLocationForm" name="description"></textarea>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td>
			    		<p>Aufenthaltszeit:</p>
			    	</td>
		    		<td>
		    			<input type="time" name="time" value=""/>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td>
		    			<!-- Some button -->
		    		</td>
		    		<td>
		    			<input type="submit" name="confirm" value="Anlegen"/>
		    		</td>
		    	</tr>
		    	</table>
		    </form>
		  </div>
		
		</div>
		
		<div id="myUpdateModal" class="modal">
		  <!-- Modal update content -->
		  <div class="modal-content">
		    <span class="close">&times;</span>
		    <form id="updateLocationForm" method="POST" action="">
		    	<input type="hidden" name="id" value=""/>
		    	<table id="popupTable">
		    	<tr>
			    	<td>
			    		<p>Angezeigter Name:</p>
			    	</td>
		    		<td>
		    			<input type="text" name="locationName" value=""/>
		    		</td>
		    	</tr>
		    	<tr>
			    	<td>
			    		<p>Bild:</p>
			    	</td>
		    		<td>
		    			<input type="file" name="picture" value=""/>
		    		</td>
		    	</tr>
		    	<tr>
			    	<td>
			    		<p>Beschreibung:</p>
			    	</td>
		    		<td>
		    			<textarea rows="7" form="createLocationForm" name="description"></textarea>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td>
			    		<p>Aufenthaltszeit:</p>
			    	</td>
		    		<td>
		    			<input type="time" name="time" value=""/>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td>
		    			<!-- Some button -->
		    		</td>
		    		<td>
		    			<input type="submit" name="confirm" value="Anlegen"/>
		    		</td>
		    	</tr>
		    	</table>
		    </form>
		  </div>
		</div>
	</body>
</html>