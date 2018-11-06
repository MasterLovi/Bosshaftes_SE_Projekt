<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
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
			<span class="mq-place-search" >
				
				  <input class="mq-input" id="test"/>
				  <button class="mq-input-icon mq-icon-clear" onClick="$('#test').val()=''"><i class="material-icons">clear</i></button>
			</span>
		</div>
		<div id="map"></div>
		
		<script>
  	 		L.mapquest.key = 'y7O2leMmoJWVGxhiWASiuAOCqUjYrzd6';
  	 		
  	 		var mymap = L.mapquest.map('map', {
  	 		  center: [49.4775206, 8.4219807],
  	 		  layers: L.mapquest.tileLayer('map'),
  	 		  zoom: 12
  	 		});
  	 		
  	 		$("#test").keypress(function(e){
  	 			if (e.which == 13){
  	 				getLocation($("#test").val(), mymap);
  	 			}
  	 		});
  	 		

  	 		//Loading all the Markers
  	 		var hs = loadJson();

    			
  	 		//Putting the markers on the map
  	 		for(var i = 0; i < hs.length; i++){
	  	 		var layer = L.marker(hs[i].geometry.coordinates).addTo(mymap);
	  	 		layer.bindPopup(hs[i].properties.popupContent);
  	 		}	
			
  	 	 L.mapquest.directions().route({
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
  	 	});
  	 		
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
  	 		
  	 	</script>
	</body>
</html>