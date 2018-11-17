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

//Always put type as parameter 
function searchLocation(sType) {
	$.ajax({
	    url: "LocationServlet",
	    type: "GET",
	    data: {
	      type: sType,
	      name: "" 
	    },
	    success: function(response) {
	      var json = response;
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

function getLocationFromDatabase(sType) {
	$.ajax({
	    url: "LocationServlet",
	    type: "GET",
	    data: {
	      type: sType,
	      lngLat: [0,0],
	      offSetXY: [0,0] 
	    },
	    success: function(response) {
	      var json = response;
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

//function createNewMarker(marker) {
//	
//	var requestJson = createMarkerJson(marker); // Has to be defined
//	
//	$.ajax({
//	    url: "LocationServlet",
//	    type: "POST",
//	    data: {
//	      operation: "create",
//	      data: requestJson //Json file
//	     
//	    },
//	    success: function(response) {
//	      var json = response;
//	      var marker;
//	      
//	      for(var i = 0; i < json.length; i++){
//	  	 		var layer = L.marker(json[i].geometry.coordinates).addTo(getMap());
//	  	 		layer.bindPopup(json[i].properties.popupContent);
//	 		}	
//	    },
//	    error: function(error) {
//	      console.log(error);
//	    }
//  });
//}


function createNewMarker(sType) {
	var json = getJsonDatastrucutreLocation();
	 var addressData = getAddress($("#createLocationForm input[name=lat]").val(), $("#createLocationForm input[name=lng]").val());
	
	json.id = "";
	json.name = $("#createLocationForm input[name=locationName]").val();
	json.location_type = sType;
	
	// ToDo Split the time
	if(sType == "culture") json.time = $("#createLocationForm input[name=locationName]").val();
	
	json.address.cityName = addressData.cityName;
	json.address.country = addressData.country;
	json.address.houseNumber = addressData.houseNumber;
	json.address.postCode = addressData.postCode;
	json.address.streetName = addressData.streetName;
	
	// Loading the coordinates from the form
	json.coordinates[0] = $("#createLocationForm input[name=lat]").val();
	json.coordinates[1] = $("#createLocationForm input[name=lng]").val();
	
	// Loading the time and split into min and hours
	json.time.hours = $("#createLocationForm input[name=time]").val().substring(0,2);
	json.time.minutes = $("#createLocationForm input[name=time]").val().substring(3,5);
	
	console.log(json);
	
	$.ajax({
	    url: "LocationServlet",
	    type: "POST",
	    data: {
	      operation: "create",
	      data: json.stringify() //Json file
	     
	    },
	    success: function(response) {},
	    error: function(error) {
		      console.log(error);
		 }
	});
}


function getRoute(sType){
	$.ajax({
	    url: "RouteServlet",
	    type: "GET",
	    data: {
	      type: sType,
	      lngLat: [0,0],
	      offSetXY: [0,0],
	      stops: 0,
	      time: {},
	      rating: 0
	     
	    },
	    success: function(response) {
	      var json = response;
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

