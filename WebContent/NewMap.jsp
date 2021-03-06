<%@ page language="java" pageEncoding="utf-8" contentType="text/html"%>
<!DOCTYPE html>

<html lang="de">
	<head>
		<title>NewMap Design</title>
		
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		
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
   	    <script src="utilities/javaScript/popupLoader.js"></script>
   	    <script src="utilities/javaScript/formValidation.js"></script>
  	    <script src="utilities/javaScript/jquery-ui.min.js"></script>
   	    
   	    <script src="https://unpkg.com/leaflet@1.3.4/dist/leaflet.js"
  		integrity="sha512-nMMmRyTVoLYqjP9hrbed9S+FzjZHW5gY1TWCHA5ckwXZBadntCNs8kEqAWdrb9O7rxbCaA4lKTIWjDXZxflOcA=="
   		crossorigin="">
  	 	</script>
  	 	<script src="https://api.mqcdn.com/sdk/mapquest-js/v1.3.2/mapquest.js"></script>
  	 	
	</head>
	<body>
		<div class="absolute" id="header">
			<div id='logoWrapper'>
	        	<a href='index.jsp'><img id='mainLogo' src='utilities\pic\MainLogo.jpg'></a>
	        </div>
	        
	        <div id='otherMapHeader'>
	    		<div id='mapHeader'>
	    			<input type="hidden" id="currentAction" value="">
    				<i id="partyText" class="clickable">Party</i>
    				<i id="headerIconMiddle" class="material-icons clickable">chevron_left</i>
    				<i id="cultureText" class="clickable">Kultur</i>
   				</div>
	    	</div>
	    	<div  id='loginArea'>
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
		</div>
		<div class="absolute" id="searchbar">
			<span class="mq-place-search" >
				
				  <input class="mq-input" id="input-search" placeholder='Nach Ort suchen...'/>
				  <button class="mq-input-icon mq-icon-clear" onClick="clearInput('input-search')"><i class="material-icons">clear</i></button>
			</span>
		</div>
		<div class="absolute" id="optionpanle">
			<form action="" method="POST" id="routeForm">
				<p>Spots</p>
				<input type="text" id="spotValue" size="1" value="10" min="1" max="20">
			    <input type="range" id="spotRange" name="spots" min="1" max="20" />
				<hr>
				<p>Dauer der Touren (h)</p>
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

				<input type="submit" value="Tour suchen" id="searchTour" class="centered button">
			</form>
		</div>
		<div id="tourInfoPanel">
			<i class="absolute material-icons" id="closeTourInfo">search</i>
			<input type="hidden" id="tourIdOnPanle" value="">
			<input type="hidden" id="tourTypeOnPanle" value ="">
			<table>
				<tr>
					<td class="infoHeader">
						Name
					</td>
				</tr>
				<tr>
					<td id="infoTourName" class="infotext">
					
					</td>
				</tr>
			
				<tr>
					<td class="infoHeader">
						Beschreibung
					</td>
				</tr>
				<tr>
					<td id="infoTourDescription" class="infotext">
					
					</td>
				</tr>
	
				<tr>
					<td class="infoHeader">
						Bewertung
					</td>
				</tr>
				<tr>
					<td id="infoTourRating" class="infotext centered">
					
					</td>
				</tr>

				<tr>
					<td class="infoHeader">
						Bild
					</td>
				</tr>
				
				<tr>
					<td id="infoTourPics" class="infotext">
					
					</td>
				</tr>
				<tr>
					<td class="infoHeader">
						Dauer der Tour
					</td>
				</tr>
				
				<tr>
					<td id="infoTourTime" class="infotext">
					
					</td>
				</tr>
			</table>
			<div id="tourStopsWrapper">
				<p class="infoHeader">Spots</p>
				<ul id="tourStops">
					
					
				</ul>
			</div>
			<button class="absolute button" id="buttonLoad">Laden</button>
			<% if (session.getAttribute("loggedin") != null && (boolean)session.getAttribute("loggedin")) out.println("<button class=\"absolute button\" id=\"buttonRate\">Bewerten</button>"); %>
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
		    <!-- Content will be loaded here -->
		  </div>
		
		</div>
		
		<div id="statusMessage">
			<p id="statusMessageText"></p>
		</div>
	</body>
</html>