var globalRoutes;
var globalLayer; //Will be used to delete and reload marker that are out of the viewport
var userRoutes = [];

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

function refreshLayerData(sType) {
	$.ajax({
		url: "LocationServlet",
		type: "GET",
		data: {
			type: sType,
			boundNorthWestLat: getMap().getBounds().getNorthWest().lat, 
			boundNorthWestLng: getMap().getBounds().getNorthWest().lng,
			boundSouthEastLat: getMap().getBounds().getSouthEast().lat,
			boundSouthEastLng: getMap().getBounds().getSouthEast().lng
		},
		success: function(response) {
			// Reload all Infos in globalLayer
			var json = JSON.parse(response);
			$.each(json, function(i,v) {
				$.each(globalLayer._layers, function(j,w) {
					if (w.info.id == v.id) {
						w.info = v;
					}
				});
			});
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
			
			if(getMap()._zoom < 11) { return; }
			
			
			var json = JSON.parse(response);
			var markerLayer = L.layerGroup();
			
			// Loads the new marker to the map
			for(var i = 0; i < json.length; i++) {
				var marker;
				var spot = "Location";
				
				marker = (L.marker([json[i].latitude, json[i].longitude])
							.addTo(getMap()));
				marker.info = json[i];
				
				if ($("#userId").val() != null) {
				marker.bindPopup("<h4 class=\"centered\">"+json[i].name+"</h4>" +
						"<img class=\"popupImage\" src=\""+ (json[i].images[0].lenght > 30 ? json[i].images[0] : "utilities/pic/Bild1.jpg") +"\" />" + 
						"<p>Bewertung</p>" +
						"<div class=\"ratingWrapper centered\">" +
							"<i class='material-icons " + (json[i].avgRating >= 1 ? "activeStar" : "") + "'>grade</i>" +
							"<i class='material-icons " + (json[i].avgRating >= 2 ? "activeStar" : "") + "'>grade</i>" +
							"<i class='material-icons " + (json[i].avgRating >= 3 ? "activeStar" : "") + "'>grade</i>" +
							"<i class='material-icons " + (json[i].avgRating >= 4 ? "activeStar" : "") + "'>grade</i>" +
							"<i class='material-icons " + (json[i].avgRating >= 5 ? "activeStar" : "") + "'>grade</i>" +
							"<a class=\"feedbackInfo\" onClick=\"showFeedbackPopup({id: " + marker._leaflet_id + ", type: 'Location'})\"><i class='material-icons'>info_outline</i></a>" +
						"</div>" +
						"<p>Beschreibung</p>" +
						"<p>"+json[i].description+"</p>" +
						"<div class=\"buttonWrapper\">" +
							"<br><button class=\"button thirdSpace\" onClick=showUpdatePointPopup("+marker._leaflet_id+")>Ändern</button>" +
							"<button class=\"button thirdSpace\" onClick=reportLocation("+marker._leaflet_id+")>Melden</button>" +
							"<button class=\"button thirdSpace\" onClick=feedbackLocation("+marker._leaflet_id+")>Bewerten</button><br>" +
							"<button class=\"button halfSpace\" onClick=showNewRoutePopup("+marker._leaflet_id+")>Zu neuer Route</button>" +
							"<button class=\"button halfSpace\" onClick=showUpdateRoutePopup("+marker._leaflet_id+")>Hinzufügen zu</button>" +
						"</div>");
						
				} else {
					marker.bindPopup("<h4 class=\"centered\">"+json[i].name+"</h4>" +
							"<img class=\"popupImage\" src=\""+(json[i].images[0].lenght > 30 ? json[i].images[0] : "utilities/pic/Bild1.jpg")+"\">" + 
							"<p>Bewertung</p>" +
							"<div class=\"ratingWrapperPopup centered\">" +
								"<i class='material-icons " + (json[i].avgRating >= 1 ? "activeStar" : "") + "'>grade</i>" +
								"<i class='material-icons " + (json[i].avgRating >= 2 ? "activeStar" : "") + "'>grade</i>" +
								"<i class='material-icons " + (json[i].avgRating >= 3 ? "activeStar" : "") + "'>grade</i>" +
								"<i class='material-icons " + (json[i].avgRating >= 4 ? "activeStar" : "") + "'>grade</i>" +
								"<i class='material-icons " + (json[i].avgRating >= 5 ? "activeStar" : "") + "'>grade</i>" +
								"<a class=\"feedbackInfo\" onClick=\"showFeedbackPopup({id: " + marker._leaflet_id + ", type: 'Location'})\"><i class='material-icons'>info_outline</i></a>" +
							"</div>" +
							"<p>Beschreibung</p>" +
							"<p>"+json[i].description+"</p>");
				}
				
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


function createNewMarker(sType, pImageLoaded) {
	var json = getJsonDatastrucutreLocation("create");
	var addressData = getAddress($("#createLocationForm input[name=lat]").val(), $("#createLocationForm input[name=lng]").val());

	json.name = $("#createLocationForm input[name=locationName]").val();
	json.type = sType;

	// ToDo Split the time
	if(sType == "Kultur") json.time.time = $("#createLocationForm input[name=time]").val()+":00";
;

	json.address.cityName = addressData.cityName;
	json.address.country = addressData.country;
	json.address.postCode = addressData.postCode;
	json.address.streetName = addressData.streetName;

	// Loading the coordinates from the form
	json.latitude = $("#createLocationForm input[name=lat]").val();
	json.longitude = $("#createLocationForm input[name=lng]").val();

	json.description = $("#createLocationForm textarea[name=description]").val();
	

	pImageLoaded.then(function(image) {
	
	json.images = [image];
	var jsonArray = [json];
		
	$.ajax({
		url: "LocationServlet",
		type: "POST",
		data: {
			operation: "create",
			json: JSON.stringify(jsonArray) //Json file

		},
		success: function(response) {
			// Reloading database information;
			getLocationFromDatabase($("#currentAction").val()); 
			
			if(newMarker != null){
				mymap.removeLayer(newMarker);
			}
			
			unloadPopup();
			sendStatusMessage("Neuer Ort erfolgreich erstellt.", "green");
			
		},
		error: function(error) {
			console.log(error);
		}
	});
});
}

function updateMarker(markerId, pImageLoaded){
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
	pImageLoaded.then(function(image) {
		json.images = [image];
		var jsonArray = [json];
		debugger;
		$.ajax({
			url: "LocationServlet",
			type: "POST",
			data: {
				operation: "update",
				json: JSON.stringify(jsonArray) //Json file

			},
			success: function(response) {
				unloadPopup();
				sendStatusMessage("Ort erfolgreich geändert.", "green");
			},
			error: function(error) {
				console.log(error);
			}
		});
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
			sendStatusMessage("Ort erfolgreich gemeldet.", "green");
		},
		error: function(error) {
			
			sendStatusMessage("Sie haben diesen Ort bereits gemeldet", "red");
		}
	});
}



function getRoute(sType){
	var stops = $("#routeForm input[name=spots]").val();
	var rating = $("#routeForm input[name=rating]").val();
	var time = $("#routeForm input[name=time]").val()+":00:00";
	
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

function sendFeedback(type, id) {
	var json = getDatastructureFeedback();
	
	json.rating = $("#feedbackForm input[name=rating]").val();
	json.comment = $("#feedbackForm textarea[name=comment]").val();
	
	var jsonArray = [json];
	
	$.ajax({
		url: "FeedbackServlet",
		type: "POST",
		data: {
			type: type,
			id: id,
			operation: "create",
			json: JSON.stringify(jsonArray)
		}, 
		success: function(response) {
			getLocationFromDatabase($("#currentAction").val());
			sendStatusMessage("Feedback erfolgreich gespeichert.", "green");
			unloadPopup();
		},
		error: function(error) {
			$("#popupError").html("Sie haben diesen Ort oder Route bereits bewertet.");
		}
		
	});
	
}

function deleteFeedback(type, id, feedbackId) {
	var json;
	var feedback;
	var locationId;
	var indexOfFeedback;
	
	if (type == "Location") {
		json = globalLayer.getLayer(id).info;
	} else {
		$.each(globalRoutes, function(i,v){
			if (v.id == id) {
				json = v;
				return;
			}
		});
	}
	
	locationId = json.id;
	
	$.each(json.feedback, function(i,v) {
		if(v.id == feedbackId){
			feedback = v;
			indexOfFeedback = i;
			return;
		}
	});
	
	var jsonArray = [feedback];
	
	$.ajax({
		url: "FeedbackServlet",
		type: "POST",
		data: {
			type: type,
			id: locationId,
			operation: "delete",
			json: JSON.stringify(jsonArray)
		}, 
		success: function(response) {
			// Deletes the recently deleted feedback from the globalLayer
			globalLayer.getLayer(id).info.feedback.splice(indexOfFeedback, 1);
			// Reloads the feedback
			loadFeedbackToPopup({id: id, type: type});
			
			refreshLayerData($("#currentAction").val());
			
			sendStatusMessage("Feedback erfolgreich gelöscht.", "green");
		},
		error: function(error) {
			console.log(error);
		}
		
	});
	
}

function changeFeedback(type, id, feedbackId) {
	var json;
	var feedback;
	var locationId;
	
	if (type == "Location") {
		json = globalLayer.getLayer(id).info;
	} else {
		$.each(globalRoutes, function(i,v){
			if (v.id == id) {
				json = v;
				return;
			}
		});
	}
	
	locationId = json.id;
	
	$.each(json.feedback, function(i,v) {
		if(v.id == feedbackId){
			feedback = v;
		}
	});
	
	//TODO Replace with inbuild textarea and starchagne 
	feedback.comment = $("#feedbackEditArea").val();
	feedback.rating = $("#feedbackRatingValue").val();
	
	var jsonArray = [feedback];
	
	$.ajax({
		url: "FeedbackServlet",
		type: "POST",
		data: {
			type: type,
			id: locationId,
			operation: "update",
			json: JSON.stringify(jsonArray)
		}, 
		success: function(response) {
			loadFeedbackToPopup({id: id, type: type});
			refreshLayerData($("#currentAction").val());
			sendStatusMessage("Feedback erfolgreich geändert.", "green");
		},
		error: function(error) {
			console.log(error);
		}
		
	});
	
}

function createNewRoute(id) {

	var data = globalLayer;
	var location;
	var json = getDatastructureRoute();
		
	$.each(data._layers, function(i,v) {
		if (v._leaflet_id == id) {
			location = v.info;
		}
	});
	
	json.stops.push(location); //Adding the first location to the new tour
	json.firstLong = location.longitude;
	json.firstLat = location.latitude;
	json.numberOfStops = 1;
	json.name = $("#newRouteForm input[name=name]").val();
	json.description = $("#newRouteForm textarea[name=description]").val();
	json.type = $("#currentAction").val();
	json.time.time = "01:00:00";
	json.avgRating = "3";
	json.owner.id = null;
	json.owner.username = null;
	json.owner.email = null;
	json.id = null;
	

	var jsonArray = [json];
	
	$.ajax({
		url: "RouteServlet",
		type: "POST",
		data: {
			operation: "create",
			json: JSON.stringify(jsonArray)
		},
		success: function(response) {
			getUserRoutes($("#currentAction").val(), $("#userId").val());
			unloadPopup();
			sendStatusMessage("Route wurde erfolgreich erstellt.", "green");
		},
		error: function(error) {
			console.log(error);
		}
	});
}
// WICHTIG KEINE LEEREN ROUTEN
function addPointToRoute(locationId, routeId) {
	// Operation 'update'
	// Komplettes Route Object 
	var route;
	var location;
	
	$.each(userRoutes, function(i,v) {
		if(v.id == routeId) {
			route = v;
			return;
		}
	});
	
	location = globalLayer.getLayer(locationId).info;
	
	route.stops.push(location);
	route.numberOfStops = route.numberOfStops + 1;
	pTimeReturned = calculateTraveltime(route);
	
	pTimeReturned.then(function(time) {
		route.time.time = time;
		var jsonArray = [route];
		
		$.ajax({
			url: "RouteServlet",
			type: "POST",
			data: {
				operation: "update",
				json: JSON.stringify(jsonArray)
			},
			success: function(response) {
				unloadPopup();
				sendStatusMessage("Ort wurder erfolgreich der Route hinzufügt.", "green");
			},
			error: function(error) {
				console.log(error);
			}
		});
	});
}

function removeFromRoute(locationId, routeId) {
	// Operation 'update'
	// Komplettes Route Object 
	var removeIndex;
	var route;
	var location;
	var pTimeCalculate;
	
	$.each(userRoutes, function(i,v) {
		if(v.id == routeId) {
			route = v;
			return;
		}
	});
	
	$.each(route.stops, function(i,v) {
		if (v.id == locationId) {
			removeIndex = i;
		}
	});
	
	if (route.numberOfStops-1 == 0) {
		$("#popupError").html("Die Route darf nicht leer sein. Sie können den Punkt nicht löschen!");
		cancelDeletionOrChange('route');
		return;
	}
	
	route.stops.splice(removeIndex, 1); //Removes the element on index 'removeIndex' in the Array
	route.numberOfStops = route.numberOfStops-1;
	pTimeCalculate = calculateTraveltime(route);
	
	pTimeCalculate.then(function(time) {
		
		route.time.time = time;
		
		var jsonArray = [route];
		
		$.ajax({
			url: "RouteServlet",
			type: "POST",
			data: {
				operation: "update",
				json: JSON.stringify(jsonArray)
			},
			success: function(response) {
				changeRouteInformation(routeId);
				sendStatusMessage("Punkt erfolgreich aus Route gelöscht.", "green");
			},
			error: function(error) {
				console.log(error);
			}
		});
	});
}

function deleteRoute(routeId) {
	var route;
	
	$.each(userRoutes, function(i, v) {
		if(v.id == routeId) {
			route = v;
			return;
		}
	});
	
	var jsonArray = [route];
	
	$.ajax({
		url: "RouteServlet",
		type: "POST",
		data: {
			operation: "delete",
			json: JSON.stringify(jsonArray)
		},
		success: function(response) {
			$.each(userRoutes, function(i, v) {
				if(v.id == routeId) {
					userRoutes.splice(i, 1);
					return;
				}
			});
			loadUserRoutes("show");
			changeRouteInformation($("#manageRoutesForm select[name=routes]").val());
			sendStatusMessage("Route wurde erfolgreich gelöscht.", "green");
		},
		error: function(error) {
			console.log(error);
		}
	});
}

function getUserRoutes(sType, userId) {
	$.ajax({
		url: "RouteServlet",
		type: "GET",
		data: {
			type: sType,
			owner: userId,
			time: "200:00:00" //Threshhold for all Routes

		},
		success: function(response) {
			userRoutes = JSON.parse(response);
		},
		error: function(error) {
			console.log(error);
		}
	});
}
