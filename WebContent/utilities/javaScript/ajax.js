function getLocation(searchString, map){
	
	// This function takes the input from the search field and sends it
	// the address api and sets the focus of the map to the most related 
	// result
	
	var url = 'http://www.mapquestapi.com/geocoding/v1/address';
	var data = {
			  "location": searchString,
			  "options": {
			    "thumbMaps": false,
			    "maxResults": 1
			  }
			};
	
	var key = 'y7O2leMmoJWVGxhiWASiuAOCqUjYrzd6';
	
	$.getJSON(url+"?key="+key, data, function(result, status){
		if (result.results.length==0){
			alert("Ihre Eingabe konnte keiner Adresse zugeordnet werden.")
		}
		map.panTo(result.results[0].locations[0].latLng);
	});
	
}

function getMarker(lng, lat, offsetX, offsetY){
	
}

function getLocationFromDatabase(sType) {
	$.ajax({
	    url: "LocationServlet",
	    type: "GET",
	    data: {
	      type: sType
	    },
	    success: function(response) {
	      var json = JSON.parse(response);
	      var marker;
	      
	      for(var i = 0; i < json.length; i++){
	  	 		var layer = L.marker(json[i].geometry.coordinates).addTo(getMap());
	  	 		layer.bindPopup(json[i].properties.popupContent);
	 		}	
	    },
	    error: function(error) {
	      console.log(error);
	    }
  });
}

function createNewMarker(sType) {
	var json = getJsonDatastructureLocation();
	
	json.id = "";
	json.name = $("#createLocationForm input[name=locationName]").val();
	json.location_type = sType;
	if(sType == "culture") json.time = $("#createLocationForm input[name=locationName]").val();
	
	
}

