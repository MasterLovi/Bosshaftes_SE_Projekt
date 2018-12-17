var globalRoutes; // Contains the routes after a user used the search function
var globalLayer; // Will be used to delete and reload marker that are out of the viewport
var userRoutes = []; // Contains all the routes that are owned by the user - Will be loaded on page load

/**
 *Reads the information form the registration page and send them to the backend.
 * @returns NONE
 */
function regUser(){
	// Get all the variables from the user registration form
	var username = $("#regForm input[name=username]").val();
	var email = $("#regForm input[name=email]").val();
	var pw1 = $("#regForm input[name=password]").val();
	var pw2 = $("#regForm input[name=passwordRep]").val();
	
	// Check if the password is equal
	if (pw1 !== pw2) {
		$("#error").html("Die Passwörter stimmen nicht überein.");
		return;
	}
	
	// Make the ajax call and register the user
	$.ajax({
		url: "RegistrationServlet",
		type: "POST",
		data: {
			username: username,
			email: email,
			password: pw1,
			passwordRep: pw2
		},
		success: function(response) {
			// Forward to the index page if the registration was successful
			document.location = "index.jsp";
		},
		error: function(error) {
			// Displays the error that occured in the backend to the user
			$("#error").html(error.responseText);
		}
	});
}

/**
 * Send a login request to the backend
 * @returns {Boolean} - Contains either an error or not
 */
function userLogin(){
	// Get the username and password from the login form
	var loginError = false;
	var username = $("#loginForm input[name=username]").val();
	var pw1 = $("#loginForm input[name=password]").val();
	
	// Make the ajax call 
	$.ajax({
		url: "UserSessionServlet",
		type: "POST",
		async: false,
		data: {
			username: username,
			password: pw1
		},
		success: function(response) {
			// No action needed inhere since the page will be reloaded automatically after form submit
		},
		error: function(error) {
			// If the login failed. Change appearance of the form and make it shake
			loginError = true;
			$("#loginForm input[name=username]").val("");
			$("#loginForm input[name=password]").val("");
			$("#loginForm input[name=username]").css("border", "solid 1px red");
			$("#loginForm input[name=password]").css("border", "solid 1px red");
			$("#loginForm").effect("shake");
		}
	});
	
	return loginError;
}

/**
 * Call the mapquest geocode api. API return a location object.
 * Centers the map based on the give inputs.
 * @param searchString {String} - Contains the searchinput from the user
 * @param map {Object} - Contains the map object where the centering should take place
 * @returns NONE
 */
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
	
	// Identity key for mapquest
	var key = 'y7O2leMmoJWVGxhiWASiuAOCqUjYrzd6';
	
	// API call with GET method
	$.getJSON(url+"?key="+key, data, function(result, status){
		if (result.results.length==0){
			alert("Ihre Eingabe konnte keiner Adresse zugeordnet werden.")
		}
		// centering the map
		map.panTo(result.results[0].locations[0].latLng);
	});
}

/**
 * Reloads the information of the displayed markers without reloading them
 * @param sType {String} - Contains the type of markers that will be loaded from the database
 * @returns NONE
 */
function refreshLayerData(sType) {
	$.ajax({
		url: "LocationServlet",
		type: "GET",
		data: {
			type: sType,
			boundNorthWestLat: getMap().getBounds().getNorthWest().lat, // Border information, so not every marker is loaded
			boundNorthWestLng: getMap().getBounds().getNorthWest().lng,	
			boundSouthEastLat: getMap().getBounds().getSouthEast().lat,
			boundSouthEastLng: getMap().getBounds().getSouthEast().lng
		},
		success: function(response) {
			// Reload all information in globalLayer
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

/**
 * Loads all the markers from the database that are visible to the user and creates these marker on the map
 * @param sType {String} - Contains the type of markers that will be loaded from the database
 * @returns NONE
 */
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
			
			// Defining the default image 
			var defaultImage = sType == "Party" ? "utilities/pic/PartyDefault.png" : "utilities/pic/KulturDefault.png"
			
			// Clears the map before the new markers are loaded
			if(globalLayer != null) {
				globalLayer.eachLayer(function(layer) {
					getMap().removeLayer(layer);
				});
				globalLayer = null;
			}
			
			// If the zoom of the map is to far, no markers will be shown
			if(getMap()._zoom < 11) { return; }
			
			// Get the response into the json format
			var json = JSON.parse(response);
			var markerLayer = L.layerGroup();
			
			// Loads the new marker to the map
			for(var i = 0; i < json.length; i++) {
				var marker;
				var spot = "Location";
				
				// Putting the marker on the map
				marker = (L.marker([json[i].latitude, json[i].longitude])
							.addTo(getMap()));
				marker.info = json[i];
				
				// Check if the user is logged in
				// if yes, put all the function buttons into the popup.
				if ($("#userId").val() != null) {
				marker.bindPopup("<h4 class=\"centered popupMarkerHeader\">"+json[i].name+"</h4>" +
						"<div class=\"popupMarkerImageWrapper\"><img class=\"popupImage\" src=\""+ (json[i].images != null && json[i].images.length > 0 ? json[i].images[0] : defaultImage) +"\" /></div>" + 
						"<p>Bewertung</p>" +
						"<div class=\"ratingWrapper centered\">" +
							"<i class='material-icons " + (json[i].avgRating >= 1 ? "activeStar" : "") + "'>grade</i>" +
							"<i class='material-icons " + (json[i].avgRating >= 2 ? "activeStar" : "") + "'>grade</i>" +
							"<i class='material-icons " + (json[i].avgRating >= 3 ? "activeStar" : "") + "'>grade</i>" +
							"<i class='material-icons " + (json[i].avgRating >= 4 ? "activeStar" : "") + "'>grade</i>" +
							"<i class='material-icons " + (json[i].avgRating >= 5 ? "activeStar" : "") + "'>grade</i>" +
							"<a class=\"feedbackInfo\" onClick=\"showFeedbackPopup(" + marker._leaflet_id + ", 'Location')\"><i class='material-icons'>info_outline</i></a>" +
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
							"<div class=\"popupMarkerImageWrapper\"><img class=\"popupImage\" src=\""+(json[i].images != null && json[i].images.length > 0 ? json[i].images[0] : defaultImage)+"\"></div>" + 
							"<p>Bewertung</p>" +
							"<div class=\"ratingWrapperPopup centered\">" +
								"<i class='material-icons " + (json[i].avgRating >= 1 ? "activeStar" : "") + "'>grade</i>" +
								"<i class='material-icons " + (json[i].avgRating >= 2 ? "activeStar" : "") + "'>grade</i>" +
								"<i class='material-icons " + (json[i].avgRating >= 3 ? "activeStar" : "") + "'>grade</i>" +
								"<i class='material-icons " + (json[i].avgRating >= 4 ? "activeStar" : "") + "'>grade</i>" +
								"<i class='material-icons " + (json[i].avgRating >= 5 ? "activeStar" : "") + "'>grade</i>" +
								"<a class=\"feedbackInfo\" onClick=\"showFeedbackPopup(" + marker._leaflet_id + ", 'Location')\"><i class='material-icons'>info_outline</i></a>" +
							"</div>" +
							"<p>Beschreibung</p>" +
							"<p>"+json[i].description+"</p>");
				}
				
				
				marker._icon.style.zIndex = 50; // Makes sure everything is in front of the default marker 
				markerLayer.addLayer(marker); // Add the marker to a layer. This is needed, so all markers can be removed on a reload
				
			}
			// Makes the local layer globally available 
			globalLayer = markerLayer;
		},
		error: function(error) {
			console.log(error);
		}
	});
}

/**
 * Creates a new marker information object and sends it to the database
 * @param sType {String} - Contains the marker type 
 * @param pImageLoaded {Promise} - Contains a promise that will return the encoded image
 * @returns NONE
 */
function createNewMarker(sType, pImageLoaded) {
	// Load basic location structure
	var json = getJsonDatastrucutreLocation("create");
	// Load address information form the mapquest api
	var addressData = getAddress($("#createLocationForm input[name=lat]").val(), $("#createLocationForm input[name=lng]").val());

	// Inserting information into the json structure
	json.name = $("#createLocationForm input[name=locationName]").val();
	json.type = sType;

	// Time will only be set if the marker type is culture
	if(sType == "Kultur") json.time.time = $("#createLocationForm input[name=time]").val()+":00";

	// Inserting address information 
	json.address.cityName = addressData.cityName;
	json.address.country = addressData.country;
	json.address.postCode = addressData.postCode;
	json.address.streetName = addressData.streetName;

	// Loading the coordinates from the form
	json.latitude = $("#createLocationForm input[name=lat]").val();
	json.longitude = $("#createLocationForm input[name=lng]").val();

	json.description = $("#createLocationForm textarea[name=description]").val();
	
	// Stops the execution until the encoding is done
	pImageLoaded.then(function(image) {
	
		// If image was set, replace image array -> Only one image is loaded
		if (image != null) { json.images = [image] }
		
		// Bring the data in a backend compatible format
		var jsonArray = [json];
			
		// Ajax call
		$.ajax({
			url: "LocationServlet",
			type: "POST",
			data: {
				operation: "create",
				json: JSON.stringify(jsonArray) 
	
			},
			success: function(response) {
				// Reloading location information, so the new marker is shown
				getLocationFromDatabase($("#currentAction").val()); 
				
				// Removes the marker that was used to create the permanent marker
				if(newMarker != null){
					mymap.removeLayer(newMarker);
				}
				
				// Close popup
				unloadPopup();
				
				// Status message
				sendStatusMessage("Neuer Ort erfolgreich erstellt.", "green");
				
			},
			error: function(error) {
				// Close popup
				unloadPopup();
				
				// Error message
				sendStatusMessage("Ort konnte nicht angelegt werden.", "red");
			}
		});
	});
}

/**
 * Changes the marker information and sends the updated marker information to the database
 * @param markerId {Integer} - Contains the leaflet_id of the marker that has to changend
 * @param pImageLoaded {Promise} - Contains a promise that return the encoded image
 * @returns NONE
 */
function updateMarker(markerId, pImageLoaded){
	// Get the required marker
	var marker = globalLayer.getLayer(markerId);
	// Get the update json structure
	var json = getJsonDatastrucutreLocation("update");
	
	// parse existing marker information and changes into the new structure
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
	
	// Stops the execution until the image is ready
	pImageLoaded.then(function(image) {
		
		// Overrides the image array if a image exists 
		if (image != null) { json.images = [image] }
		
		// Bring the data in a backend compatible format
		var jsonArray = [json];
		
		$.ajax({
			url: "LocationServlet",
			type: "POST",
			data: {
				operation: "update",
				json: JSON.stringify(jsonArray) //Json file

			},
			success: function(response) {
				// Close popup
				unloadPopup();
				
				// Status message 
				sendStatusMessage("Ort erfolgreich geändert.", "green");
			},
			error: function(error) {
				console.log(error);
			}
		});
	});
}

/**
 * Send a report of the selected location to the database
 * @param markerId {Integer} - Contains the leaflet_id of the selected marker
 * @returns NONE
 */
function reportLocation(markerId){
	// Get the selected marker information
	var marker = globalLayer.getLayer(markerId);
	// Load the report json structure
	var json = getJsonDatastrucutreLocation("report");
	
	// Insert information into the structure
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
	
	// Bring the data in a backend compatible format
	var jsonArray = [json];
	
	// Send report
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

/**
 * Loads the routes from the database, based on the search criteria of the user
 * @param sType {String} - Contains the route type that is searched for in the database
 * @returns {Boolean} - Returns false, so the page won't be reloaded
 */
function getRoute(sType){
	// Get the criteria from the form
	var stops = $("#routeForm input[name=spots]").val();
	var rating = $("#routeForm input[name=rating]").val();
	var time = $("#routeForm input[name=time]").val()+":00:00";
	
	// Make the ajax call
	$.ajax({
		url: "RouteServlet",
		type: "GET",
		data: {
			type: sType,
			boundNorthWestLat: getMap().getBounds().getNorthWest().lat, // Border, so that only visible routes will be loaded
			boundNorthWestLng: getMap().getBounds().getNorthWest().lng,
			boundSouthEastLat: getMap().getBounds().getSouthEast().lat,
			boundSouthEastLng: getMap().getBounds().getSouthEast().lng,
			stops: stops,
			time: time,
			rating: rating

		},
		success: function(response) {
			// Add all the routes to the route selection panel
			addRoutesToSelection(response);
			
			// Set the global route object to the responded routes 
			globalRoutes = JSON.parse(response);
		},
		error: function(error) {
			console.log(error);
		}
	});
	
	return false;
}

/**
 * Creates and send a feedback object to the database 
 * @param type {String} - Contains the information if it is a route or location feedback
 * @param id {Integer} - Contains the route or location id
 * @returns NONE
 */
function sendFeedback(type, id) {
	// Load the feedback json structure
	var json = getDatastructureFeedback();
	
	// Insert the information into the structure
	json.rating = $("#feedbackForm input[name=rating]").val();
	json.comment = $("#feedbackForm textarea[name=comment]").val();
	
	// Bring the data in a backend compatible format
	var jsonArray = [json];
	
	// Make the ajax call
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
			// Check the type of the feedback and update the information
			if (type == "Location") {
				getLocationFromDatabase($("#currentAction").val());
			} else {
				getRoute($("#currentAction").val());
				loadRouteToPanel(id);
			}
			
			// Status + unload
			sendStatusMessage("Feedback erfolgreich gespeichert.", "green");
			unloadPopup();
		},
		error: function(error) {
			// Error if the user has already rated this location
			$("#popupError").html("Sie haben diesen Ort oder Route bereits bewertet.");
		}
	});
}

/**
 * Deletes a existing feedback from a route or location
 * @param type {String} - Contains the information if it is a location or route feedback
 * @param id {Integer} - Contains the id of the route of location 
 * @param feedbackId {Integer} - Contains the id of the feedback that should be deleted
 * @returns NONE
 */
function deleteFeedback(type, id, feedbackId) {
	var json;
	var feedback;
	var locationId;
	var indexOfFeedback;
	var tourIndex;
	
	// Get right locatin or route based on type
	if (type == "Location") {
		json = globalLayer.getLayer(id).info;
	} else {
		$.each(globalRoutes, function(i,v){
			if (v.id == id) {
				json = v;
				tourIndex = i;
				return;
			}
		});
	}
	
	// Get the location id form the extracted location / route
	locationId = json.id;
	
	// Find the feedback in the location or route object that should be deleted
	$.each(json.feedback, function(i,v) {
		if(v.id == feedbackId){
			feedback = v;
			indexOfFeedback = i;
			return;
		}
	});
	
	// Bring the data in a backend compatible format
	var jsonArray = [feedback];
	
	// Make the ajax call
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
			
			if (type == "Location") {
				// Deletes the recently deleted feedback from the globalLayer
				globalLayer.getLayer(id).info.feedback.splice(indexOfFeedback, 1);
				// recalculate the average rating 
				globalLayer.getLayer(id).info.avgRating = calculateAvgRating(globalLayer.getLayer(id).info);
			} else {
				// Deletes the recently deleted feedback from the globalRoutes object
				globalRoutes[tourIndex].feedback.splice(indexOfFeedback, 1);
				// Recalculate the average rating 
				globalRoutes[tourIndex].avgRating = calculateAvgRating(globalRoutes[tourIndex]);
				// Reload the globalRoutes object and reload the info panel
				getRoute($("#currentAction").val());
				loadRouteToPanel(id);
			}
			// Reloads the feedback
			loadFeedbackToPopup({id: id, type: type});
			
			// Reload the global layer data
			refreshLayerData($("#currentAction").val());
			
			// Status 
			sendStatusMessage("Feedback erfolgreich gelöscht.", "green");
		},
		error: function(error) {
			console.log(error);
		}
	});
}

/**
 * Changes an existing feedback and sends the changed feedback to the database
 * @param type {String} - Contains the information if it is a location or a route
 * @param id {Integer} - Contains the id of the location or route
 * @param feedbackId {Integer} - Contains the id of the feedback that should be changed
 * @returns NONE
 */
function changeFeedback(type, id, feedbackId) {
	var json;
	var feedback;
	var locationId;
	var tourIndex;
	
	// Find the proper route or location based on the type
	if (type == "Location") {
		json = globalLayer.getLayer(id).info;
	} else {
		$.each(globalRoutes, function(i,v){
			if (v.id == id) {
				json = v;
				tourIndex = i;
				return;
			}
		});
	}
	
	// Extracts the id from the found route or location
	locationId = json.id;
	
	// Finds the feedback that has to be changed 
	$.each(json.feedback, function(i,v) {
		if(v.id == feedbackId){
			feedback = v;
		}
	});
	
	// changes the feedback information 
	feedback.comment = $("#feedbackEditArea").val();
	feedback.rating = $("#feedbackRatingValue").val();
	
	// Bring the data in a backend compatible format
	var jsonArray = [feedback];
	
	// Make the ajax call
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
			// reload the feedback popup after the change was done
			if (type == "Location") {
				loadFeedbackToPopup({id: id, type: type});
				refreshLayerData($("#currentAction").val());
			} else {
				loadFeedbackToPopup({id: id, type: type});
				globalRoutes[tourIndex].avgRating = calculateAvgRating(globalRoutes[tourIndex]);
				getRoute($("#currentAction").val());
				loadRouteToPanel(id);
			}
			
			// Status
			sendStatusMessage("Feedback erfolgreich geändert.", "green");
		},
		error: function(error) {
			console.log(error);
		}
	});
}

/**
 * Creates a new route and sends it to the database
 * @param id {Integer} - Contains the leaflet_id of the marker that should be the first location of the route
 * @param pImageLoaded {Promise} - Contains the a promise that return the encoded image
 * @returns NONE
 */
function createNewRoute(id, pImageLoaded) {
	
	var data = globalLayer;
	var location;
	// Load json route structure
	var json = getDatastructureRoute();
	var pConvertedImage;
	
	// Find the location that should be added
	$.each(data._layers, function(i,v) {
		if (v._leaflet_id == id) {
			location = v.info;
		}
	});
	
	// Insert information into the loaded structure
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
	
	// Stop code execution until the image is encoded
	pImageLoaded.then(function(image){
		
		// Override the image property
		if (image != null) { json.images = [image] }
		
		// Bring the data in a backend compatible format
		var jsonArray = [json];
		
		// Ajax call
		$.ajax({
			url: "RouteServlet",
			type: "POST",
			data: {
				operation: "create",
				json: JSON.stringify(jsonArray)
			},
			success: function(response) {
				// Reload the userRoute information
				getUserRoutes($("#currentAction").val(), $("#userId").val());
				
				// Unload + status
				unloadPopup();
				sendStatusMessage("Route wurde erfolgreich erstellt.", "green");
			},
			error: function(error) {
				console.log(error);
			}
		});
	})
}

/**
 * Adds a new location to an existing user route and sends it to the database 
 * @param locationId {Integer} - Contains the leaflet_id of the location that should be added to the route
 * @param routeId {Integer} - Contains the id of the route the location should be added to
 * @returns NONE
 */
function addPointToRoute(locationId, routeId) {

	var route;
	var location;
	
	// Find the user route
	$.each(userRoutes, function(i,v) {
		if(v.id == routeId) {
			route = v;
			return;
		}
	});
	
	// Get the location information
	location = globalLayer.getLayer(locationId).info;
	
	// Add the location to the stops array of the route
	route.stops.push(location);
	
	// Edit the number of stops and recalculate the travel time
	route.numberOfStops = route.numberOfStops + 1;
	pTimeReturned = calculateTraveltime(route);
	
	// Stops code execution until the time was calculated
	pTimeReturned.then(function(time) {
		
		if (route.type == "Kultur"){
			route.time.time = calculateTotalTravelTime(route, time);
		} else {
			route.time.time = time;
		}
		
		// Bring the data in a backend compatible format
		var jsonArray = [route];
		
		$.ajax({
			url: "RouteServlet",
			type: "POST",
			data: {
				operation: "update",
				json: JSON.stringify(jsonArray)
			},
			success: function(response) {
				// Unload + Status
				unloadPopup();
				sendStatusMessage("Ort wurde erfolgreich der Route hinzugefügt.", "green");
			},
			error: function(error) {
				console.log(error);
			}
		});
	});
}

/**
 * Remove a location from a user route. Route can not contain less than one location
 * @param locationId {Integer} - Contains the leaflet_id of the location that should be removed
 * @param routeId {Integer} - Contains the route id of the route the location should be removed from
 * @returns NONE
 */
function removeFromRoute(locationId, routeId) {
	var removeIndex;
	var route;
	var location;
	var pTimeCalculate;
	
	// Find the proper user route 
	$.each(userRoutes, function(i,v) {
		if(v.id == routeId) {
			route = v;
			return;
		}
	});
	
	// Find the proper location in the stops array
	$.each(route.stops, function(i,v) {
		if (v.id == locationId) {
			removeIndex = i;
		}
	});
	
	// Check if that stop is the only stop in the array
	if (route.numberOfStops-1 == 0) {
		$("#popupError").html("Die Route darf nicht leer sein. Sie können den Punkt nicht löschen!");
		cancelDeletionOrChange('route');
		return;
	}
	
	// Remove the location from the stops array
	route.stops.splice(removeIndex, 1); //Removes the element on index 'removeIndex' in the Array
	route.numberOfStops = route.numberOfStops-1;
	// Recalculate travel time
	pTimeCalculate = calculateTraveltime(route);
	
	// Stops code execution until time was calculated 
	pTimeCalculate.then(function(time) {
		
		if (route.type == "Kultur"){
			route.time.time = calculateTotalTravelTime(route, time);
		} else {
			route.time.time = time;
		}
		
		// Bring the data in a backend compatible format
		var jsonArray = [route];
		
		$.ajax({
			url: "RouteServlet",
			type: "POST",
			data: {
				operation: "update",
				json: JSON.stringify(jsonArray)
			},
			success: function(response) {
				// Reload route information + Status
				changeRouteInformation(routeId);
				sendStatusMessage("Spot erfolgreich aus Route gelöscht.", "green");
			},
			error: function(error) {
				console.log(error);
			}
		});
	});
}

/**
 * Deletes a user route
 * @param routeId {Integer} - Contains the id of the route that should be deleted
 * @returns NONE
 */
function deleteRoute(routeId) {
	var route;
	
	// Find the route
	$.each(userRoutes, function(i, v) {
		if(v.id == routeId) {
			route = v;
			return;
		}
	});
	
	// Bring the data in a backend compatible format
	var jsonArray = [route];
	
	// Send delete request
	$.ajax({
		url: "RouteServlet",
		type: "POST",
		data: {
			operation: "delete",
			json: JSON.stringify(jsonArray)
		},
		success: function(response) {
			// Find the route in the userRoutes object and remove it 
			$.each(userRoutes, function(i, v) {
				if(v.id == routeId) {
					userRoutes.splice(i, 1);
					return;
				}
			});
			
			// reload the popup + status
			loadUserRoutes("show");
			changeRouteInformation($("#manageRoutesForm select[name=routes]").val());
			sendStatusMessage("Route wurde erfolgreich gelöscht.", "green");
		},
		error: function(error) {
			console.log(error);
		}
	});
}

/**
 * Loads the user routes from the database and saves them in a global object
 * @param sType {String} - Contains information what userRoutes should be loaded
 * @param userId {Integer} - Contains the user id that have to match the route
 * @returns
 */
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

/**
 * Changes the name of the route and send it to the database
 * @returns NONE
 */
function changeRouteName(){
	var route;
	
	var id = $("#manageRouteForm select[name=routes]").val();
	
	$.each(userRoutes, function(i,v) {
		if (id == v.id) {
			route = v;
			v.name = $("#modifiedRouteName").val()
			return;
		}
	});
	
	if ($("#modifiedRouteName").val().length != 0){
		route.name = $("#modifiedRouteName").val();
	} else {
		$("#popupError").html("Der Name darf nicht leer sein.");
		return;
	}
	
	var jsonArray = [route];
	
	$.ajax({
		url: "RouteServlet",
		type: "POST",
		data: {
			operation: "update",
			json: JSON.stringify(jsonArray)
		},
		success: function(response) {
			// Unload + Status
			getUserRoutes($("#currentAction").val(), $("#userId").val())
			loadUserRoutePopup();
			sendStatusMessage("Name wurde erfolgreich geändert", "green");
		},
		error: function(error) {
			console.log(error);
		}
	});
}

/**
 * Updates the image of a route and sends it to the database
 * @param pImageLoaded {Promise} - Contains the promise for the encoded image 
 * @returns NONE
 */
function updateRouteImage(pImageLoaded) {
	var route;
	
	var id = $("#manageRouteForm select[name=routes]").val();
	
	$.each(userRoutes, function(i,v) {
		if (id == v.id) {
			route = v;
			return;
		}
	});
	
	pImageLoaded.then(function(image){
		
		if (image != null) { route.images = [image] }
		
		var jsonArray = [route];
		
		$.ajax({
			url: "RouteServlet",
			type: "POST",
			data: {
				operation: "update",
				json: JSON.stringify(jsonArray)
			},
			success: function(response) {
				// Unload + Status
				getUserRoutes($("#currentAction").val(), $("#userId").val())
				loadUserRoutePopup();
				sendStatusMessage("Bild wurde erfolgreich geändert", "green");
			},
			error: function(error) {
				console.log(error);
			}
		});
	});
}

function updateRouteDescription() {
	var route;
	
	var id = $("#manageRouteForm select[name=routes]").val();
	
	$.each(userRoutes, function(i,v) {
		if (id == v.id) {
			route = v;
			v.description = $("#modifiedRouteDescription").val();
			return;
		}
	});	
	
	if ($("#modifiedRouteDescription").val().length != 0){
		route.description = $("#modifiedRouteDescription").val();
	} else {
		$("#popupError").html("Die Beschreibung darf nicht leer sein!");
		return;
	}

	var jsonArray = [route];
	
	$.ajax({
		url: "RouteServlet",
		type: "POST",
		data: {
			operation: "update",
			json: JSON.stringify(jsonArray)
		},
		success: function(response) {
			// Unload + Status
			getUserRoutes($("#currentAction").val(), $("#userId").val())<
			loadUserRoutePopup();
			sendStatusMessage("Beschreibung erfolgreich geändert", "green");
		},
		error: function(error) {
			console.log(error);
		}
	});
}