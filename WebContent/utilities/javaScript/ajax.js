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
		data: {
			type: sType,
			boundNorthWest: [getMap().getBounds().getNorthWest().lat, getMap().getBounds().getNorthWest().lng],
			boundSouthEast: [getMap().getBounds().getSouthEast().lat, getMap().getBounds().getSouthEast().lng] 
		},
		success: function(response) {

			// Clears the map before the new markers are loaded
			if(globalLayer != null) {
				globalLayer.eachLayer(function(layer) {
					getMap().removeLayer(layer);
				});
				globalLayer = null;
			}

			var json = response;
			var markerLayer = L.layerGroup();

			// Loads the new marker to the map
			for(var i = 0; i < json.length; i++) {
				var marker
				
				marker = (L.marker([json[i].latitude, json[i].longitude])
							.addTo(getMap()))
							.bindPopup(json[i].name);
				marker.info = json[i];
				
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
	var json = getJsonDatastrucutreLocation();
	var addressData = getAddress($("#createLocationForm input[name=lat]").val(), $("#createLocationForm input[name=lng]").val());

	json.id = "";
	json.name = $("#createLocationForm input[name=locationName]").val();
	json.type = sType;

	// ToDo Split the time
	if(sType == "culture") json.time = $("#createLocationForm input[name=locationName]").val();

	json.address.cityName = addressData.cityName;
	json.address.country = addressData.country;
	json.address.houseNumber = addressData.houseNumber;
	json.address.postCode = addressData.postCode;
	json.address.streetName = addressData.streetName;

	// Loading the coordinates from the form
	json.latitude = $("#createLocationForm input[name=lat]").val();
	json.longitude = $("#createLocationForm input[name=lng]").val();

	// Loading the time and split into min and hours
	json.time.hours = parseInteger($("#createLocationForm input[name=time]").val().substring(0,2));
	json.time.minutes = parseInteger($("#createLocationForm input[name=time]").val().substring(3,5));
	json.time.time = $("#createLocationForm input[name=time]").val()+":00";

	json.description = "Test";
	
	console.log(json);

	$.ajax({
		url: "LocationServlet",
		type: "POST",
		data: {
			operation: "create",
			data: json //Json file

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

