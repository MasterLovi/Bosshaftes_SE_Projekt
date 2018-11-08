<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
		<meta charset="ISO-8859-1">
		<title>NewMap Design</title>
		
		<meta charset="ISO-8859-1">
		
        <link rel="stylesheet" type="text/css" href="stylesheets/NewDesign.css">
        <link rel="stylesheet" type="text/css" href="stylesheets/NewPositioning.css">
        <link rel="stylesheet" type="text/css" href="stylesheets/icon.css">
        <link type="text/css" rel="stylesheet" href="https://api.mqcdn.com/sdk/mapquest-js/v1.3.2/mapquest.css"/>
        <link type="text/css" rel="stylesheet" href="https://api.mqcdn.com/sdk/place-search-js/v1.0.0/place-search.css"/>
        
        <!--<link rel="stylesheet" type="text/css" href="utilities/stylesheets/leaflet-routing-machine.css" />-->
        
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.4/dist/leaflet.css"
 		integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
   		crossorigin=""/>
    	
    	<script src="utilities/javaScript/jquery-3.3.1.min.js"></script>
    	<script src="utilities/javaScript/modifier.js"></script>
   	    <script src="utilities/javaScript/mapLoader.js"></script>
   	    <script type="text/javascript" src="utilities/javaScript/ajax.js"></script>
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
	    		<h2 id='mapHeader'><% out.print(title); %></h2>
	    	</div>
		</div>
		<div class="absolute" id="searchbar">
			<span class="mq-place-search" >
				
				  <input class="mq-input" id="input-search" placeholder='Nach Ort suchen...'/>
				  <button class="mq-input-icon mq-icon-clear" onClick="clearInput('input-search')"><i class="material-icons">clear</i></button>
			</span>
		</div>
		<div class="absolute" id="optionpanle">
			<p class="centered">Optionen</p>
			<hr>
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
				<!-- Slider wie von Google -->
				<!-- <div id="sliderOptions">
				<p class="sliderText">Test Text</p>
				<label class="switch"> 
					<input type="checkbox">
					<span class="slider round"></span>
				</label>
				</div> -->
				<input type="submit" value="Tour suchen" id="searchTour" onClick="$('#tours').show(); return false;" class="centered">
			</form>
		</div>
		<div id="map"></div>
		<div class="absolute" id="tours" style="display: none;">
			<div class="absolute" id="leftArrow" style="display: none;">
				<i class="material-icons bigIcon">chevron_left</i>
			</div>
			<ul id="tourList">
			
			<li class="inline tourdata"> 
				<p>Tour-Title 1</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP2.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 2</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP1.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 3</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP2.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 4</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP1.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 5</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP2.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 6</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP1.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 7</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP2.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 8</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP1.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 9</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP2.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 10</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP1.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 11</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP2.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 12</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP1.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 13</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP2.jpg">
				</div>
			</li>
			<li class="inline tourdata">
				<p>Tour-Title 14</p>
				<div class="centered">
					<i class="material-icons activeStar" id="star1">grade</i>
					<i class="material-icons activeStar" id="star2">grade</i>
					<i class="material-icons " id="star3">grade</i>
					<i class="material-icons" id="star4">grade</i>
					<i class="material-icons" id="star5">grade</i>
				</div>
				<div class="iconWrapper">
					<img class="tourIcon" src="utilities/pic/OP1.jpg">
				</div>
			</li>
			</ul> 
			<div class="absolute" id="rightArrow">
				<i class="material-icons bigIcon">chevron_right</i>
			</div>
		</div>
		
		<!-- The Modal -->
		<div id="myModal" class="modal">
		
		  <!-- Modal content -->
		  <div class="modal-content">
		    <span class="close">&times;</span>
		    <p>Some text in the Modal..</p>
		  </div>
		
		</div>
		
		<script>
			// This part have to outsourced
  	 		L.mapquest.key = 'y7O2leMmoJWVGxhiWASiuAOCqUjYrzd6';
  	 		
  	 		var mymap = L.mapquest.map('map', {
  	 		  center: [49.4775206, 8.4219807],
  	 		  layers: L.mapquest.tileLayer('map'),
  	 		  zoom: 12
  	 		});
  	 		
  	 		
  	 		//Loading all the Markers
  	 		var hs = loadJson();

    			
  	 		//Putting the markers on the map
  	 		for(var i = 0; i < hs.length; i++){
	  	 		var layer = L.marker(hs[i].geometry.coordinates).addTo(mymap);
	  	 		layer.bindPopup(hs[i].properties.popupContent);
  	 		}	
			
  	 	/*  L.mapquest.directions().route({
  	 	  "locations": [{
  	          "type": "s",
  	          "latLng": {
  	            "lat": 49.4775206,
  	            "lng": 8.4219807
  	          }
  	        },
  	      {
  	          "type": "s",
  	          "latLng": {
  	            "lat": 49.47303236240146,
  	            "lng": 8.394641872728245
  	          }
  	        },
  	      {
   	          "type": "s",
   	          "latLng": {
   	            "lat": 49.45674385539652,
   	            "lng": 8.41655731201172
   	          }
   	        },
	       {
 	          "type": "s",
 	          "latLng": {
 	            "lat": 49.47035517151213,
 	            "lng": 8.44139098422602
 	          }
 	        }],
  	 	  "options": {
  	 	    "allToAll": true
  	 	  }
  	 	}); */
  	 		
  	 		var marker;
  	 		mymap.on('click', function(e){
  	 			
  	 			//Remove the last set marker if it was not safed 
  	 			if(marker != null){mymap.removeLayer(marker)};
  	 			
  	 			// Creates a new marker and gives it a popup
  	 		    marker = new L.marker(e.latlng, {icon: L.mapquest.icons.marker({primaryColor: '#111111', secondaryColor: '#00cc00'})}).addTo(mymap)
  	 		    	.bindPopup("<button onClick=addMarkerToMap(marker)>Add to Map</button>").openPopup()
  	 		    	.on('click', function(e){
  	 		    		// This has to be checked since the marker will be set to null if it is added to the map.
  	 		    		if(marker != null){
  	 		    			mymap.removeLayer(marker);
  	 		    		}
 		    		});
  	 			
  	  	 		
  	 			// Adds a layer so it can be removed later on
  	 		    mymap.addLayer(marker);
  	 		});
  	 		
  	 		function addMarkerToMap(marker){
  	 			console.log(marker.getLatLng());
  	 		 	marker.setIcon( L.mapquest.icons.marker(
  	 		 					{
  	 		 							primaryColor: '#111111',
  	 		 							secondaryColor: '#222222'
	 								}
	 						)
 		 			); 
  	 		 	marker.closePopup();
  	 		 	marker._popup.setContent("Point set at: " + marker.getLatLng());
  	 		 	this.marker=null;
  	 		}
  	 		
  	 		function getMap(){
  	 			return mymap;
  	 		}
  	 	</script>
	</body>
</html>