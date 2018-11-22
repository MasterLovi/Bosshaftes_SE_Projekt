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
//This function will be used if the user wants to search for a spot and not for a address
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

//Will be used to delete and reload marker that are out of the viewport
var globalLayer;

function getLocationFromDatabase(sType) {
	$.ajax({
		url: "LocationServlet",
		type: "GET",
		contentType: "application/x-www-form-urlencoded;charset=ISO-8859-2",
		data: {
			type: sType,
			boundNorthWestLat: getMap().getBounds().getNorthWest().lat, 
			boundNorthWestLng: getMap().getBounds().getNorthWest().lng,
			boundSouthEastLat: getMap().getBounds().getSouthEast().lat,
			boundSouthEastLng: getMap().getBounds().getSouthEast().lng
		},
		success: function(response) {
			
			// Clears the map before the new markers are loaded
			if(globalLayer != null) {
				globalLayer.eachLayer(function(layer) {
					getMap().removeLayer(layer);
				});
				globalLayer = null;
			}
			
			if(getMap()._zoom < 12) { return; }
			
			var json = JSON.parse(response);
			var markerLayer = L.layerGroup();
			
			// Loads the new marker to the map
			for(var i = 0; i < json.length; i++) {
				var marker
				
				marker = (L.marker([json[i].latitude, json[i].longitude])
							.addTo(getMap()));
				marker.info = json[i];
				
				marker.bindPopup(json[i].name + "<br><button onClick=showUpdatePointPopup("+marker._leaflet_id+")>Ändern</button>" +
						"<button onClick=reportLocation("+marker._leaflet_id+")>Melden</button>");
				marker._icon.style.zIndex = 50; // Makes sure everything is in front of the default marker 
				markerLayer.addLayer(marker);
				
			}
			// Makes the local layer globally available 
			globalLayer = markerLayer;
		},
		error: function(error) {
			console.log(error);
		}
	});
}


function createNewMarker(sType) {
	var json = getJsonDatastrucutreLocation("create");
	var addressData = getAddress($("#createLocationForm input[name=lat]").val(), $("#createLocationForm input[name=lng]").val());

	json.name = $("#createLocationForm input[name=locationName]").val();
	json.type = sType;

	// ToDo Split the time
	if(sType == "culture") json.time = $("#createLocationForm input[name=locationName]").val();

	json.address.cityName = addressData.cityName;
	json.address.country = addressData.country;
	json.address.postCode = addressData.postCode;
	json.address.streetName = addressData.streetName;

	// Loading the coordinates from the form
	json.latitude = $("#createLocationForm input[name=lat]").val();
	json.longitude = $("#createLocationForm input[name=lng]").val();

	// Loading the time and split into min and hours
	json.time.time = $("#createLocationForm input[name=time]").val()+":00";

	json.description = $("#createLocationForm textarea[name=description]").val();
	
	var jsonArray = [json];

	$.ajax({
		url: "LocationServlet",
		type: "POST",
		data: {
			operation: "create",
			json: JSON.stringify(jsonArray) //Json file

		},
		success: function(response) {
			//TODO Set Point after success and close popup
			console.log(response);
		},
		error: function(error) {
			console.log(error);
		}
	});
}

function updateMarker(markerId){
	var marker = globalLayer.getLayer(markerId);
	var json = getJsonDatastrucutreLocation("update");
	
	json.id = marker.info.id;
	json.name = $("#updateLocationForm input[name=locationName").val();
	json.description = $("#updateLocationForm textarea[name=description").val();
	json.time.time = $("#updateLocationForm input[name=time").val();
	
	json.type = marker.info.type;
	json.timesReported = marker.info.timesReported;
	json.address = marker.info.address;
	json.latitude = marker.info.latitude;
	json.longitude = marker.info.longitude;
	json.feedback = marker.info.feedback;
	
	// TODO Set images and tranform it

	
	var jsonArray = [json];
	

	
	$.ajax({
		url: "LocationServlet",
		type: "POST",
		data: {
			operation: "update",
			json: JSON.stringify(jsonArray) //Json file

		},
		success: function(response) {
			//TODO Set Point after success and close popup
			console.log(response);
		},
		error: function(error) {
			console.log(error);
		}
	});
}

function reportLocation(markerId){
	var marker = globalLayer.getLayer(markerId);
	var json = getJsonDatastrucutreLocation("report");
	
	json.id = marker.info.id;
	json.name = marker.info.name;
	json.description = marker.info.description;
	json.time.time = $("#updateLocationForm input[name=time").val();
	
	json.type = marker.info.type;
	json.timesReported = marker.info.timesReported;
	json.address = marker.info.address;
	json.latitude = marker.info.latitude;
	json.longitude = marker.info.longitude;
	json.feedback = marker.info.feedback;
	
	var jsonArray = [json];
	
	$.ajax({
		url: "LocationServlet",
		type: "POST",
		data: {
			operation: "report",
			json: JSON.stringify(jsonArray) //Json file

		},
		success: function(response) {
			//TODO Set Point after success and close popup
			console.log(response);
		},
		error: function(error) {
			console.log(error);
		}
	});
}

var globalRoutes;

function getRoute(sType){
	var stops = $("#routeForm input[name=spots]").val();
	var rating = $("#routeForm input[name=rating]").val();
	var time = $("#routeForm input[name=time]").val()+":00";
	
	$.ajax({
		url: "RouteServlet",
		type: "GET",
		data: {
			type: sType,
			boundNorthWestLat: getMap().getBounds().getNorthWest().lat, 
			boundNorthWestLng: getMap().getBounds().getNorthWest().lng,
			boundSouthEastLat: getMap().getBounds().getSouthEast().lat,
			boundSouthEastLng: getMap().getBounds().getSouthEast().lng,
			stops: stops,
			time: time,
			rating: rating

		},
		success: function(response) {
			addRoutesToSelection(response);
			globalRoutes = JSON.parse(response);
		},
		error: function(error) {
			console.log(error);
		}
	});
	
	return false;
}

