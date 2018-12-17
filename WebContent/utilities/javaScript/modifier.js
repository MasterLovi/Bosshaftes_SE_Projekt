var permLayer; // Is used in multiple functions thats why it has to be global

/**
 * Clears the value of a specific input field
 * @param inputId {String} - Contains the name of the element that has to be cleared
 * @returns NONE
 */
function clearInput(inputId) {
	$("#" + inputId).val("");
}
/**
 * Creates and fills the popup that is needed to create a new location on the map
 * @param marker {Object} - Contains a complete marker object
 * @returns NONE
 */
function showNewPointPopup(marker) {
	loadPopupContent("createNew");
	loadMarkerinfoToSubmitForm(marker);
	$("#myModal").css("display", "block");
}

/**
 * Creates and fills  the popup that is needed to change the content of a marker 
 * @param markerId {Integer} - Contains the leaflet_id of the marker the user wants to update
 * @returns NONE
 */
function showUpdatePointPopup(markerId) {
	loadPopupContent("update");
	loadDataToUpdateForm(markerId)
	$("#myUpdateModal").css("display", "block");
}

/**
 * Fills in the content of the specific marker into the update popup
 * @param markerId {Integer} - Contains the leaflet_id of the marker the user want to update
 * @returns NONE
 */
function loadDataToUpdateForm(markerId) {
	var marker = globalLayer.getLayer(markerId);

	// Load Information to Form from JSON file
	$("#updateLocationForm input[name=id]").val(marker._leaflet_id);
	$("#updateLocationForm input[name=locationName]").val(marker.info.name);
	$("#updateLocationForm textarea[name=description]").val(marker.info.description);
	$("#updateLocationForm input[name=time]").val(marker.info.time.time);
}

/**
 * Loads the marker coordinates to the popup that is used to create a new location
 * @param marker {Object} - Contains a complete marker object
 * @returns NONE
 */
function loadMarkerinfoToSubmitForm(marker) {
	$("#newLat").val(marker.getLatLng()["lat"]);
	$("#newLng").val(marker.getLatLng()["lng"]);
}

/**
 * Loads the position of the user as soon as he enters the page.
 * User has to accept this in the first place.
 */
$(document).ready(function getLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(showPosition);
	} else {
		console.log("Is not supported");
	}
})

var myLocation; // Needed if the user wants to add his location to the database 

/**
 * Creates a marker on the map if the user enable location tracking
 * @param position {Object} - Contains a position object that contains the user coordinates
 * @returns NONE
 */
function showPosition(position) {
	var coords = '{ "coordinates": ["' + position.coords.latitude + '" ,"' + position.coords.longitude + '"]}'; //Creating a string that contains the users location information
	var json = JSON.parse(coords); // Change the format of the string to be an object

	// Creating a new marker and saveing it in the global marker object
	myLocation = L.marker(json.coordinates, {
		icon : L.mapquest.icons.marker({
			primaryColor : '#111111',
			secondaryColor : '#0066ff'
		})
	}).addTo(getMap())
	
	// Setting the markers popup. Different popup based on if the user is logged in or not.
	if ($("#userId").val() != null) {
		myLocation.bindPopup("<p>Ihr Standort nach IP Adresse</p><p>Möchten Sie den Standort hinzufügen</p><button class=\"fullSize button\" onClick='showNewPointPopup(myLocation)'>Hinzufügen</button>");
	} else {
		myLocation.bindPopup("<p>Ihr Standort nach IP Adresse</p>");
	}
	
	// Focus the map on the users location
	getMap().panTo(json.coordinates);
}

/**
 * Shifts the route list to the left, if the user clicked a button
 * @returns NONE
 */
function toureListLeftShift() {
	// Disables the button until the animation is done. Prevents confusing
	// result if the button was clicked too often
	$("#leftArrow").unbind("click");

	$("#tourList").animate({
		"margin-left" : "+=170" // 170 is the width of a list element 
	}, "slow", function() {
		var x = $("#tourList").css("margin-left");
		var offSet = Number(x.substring(0, x.length - 2));

		// Checking if the offSet of the List is 0. This is the default
		// situation. If the page loads the offset will be 0
		if (offSet >= 0) {
			if ($("#leftArrow").css("display") != "none") {
				$("#leftArrow").css("display", "none");
			}
		}
		$("#rightArrow").css("display", "block");

		// Rebinds the function to the button, so if it is clicked the next time
		// the animation will start again
		$("#leftArrow").bind("click", toureListLeftShift);
	});
}

/**
 * Shifts the route to the right, if the user clicked a button
 * @returns NONE
 */
function toureListRightShift() {

	// Disables the button until the animation is done. Prevents confusing
	// result if the button was clicked too often
	$("#rightArrow").unbind("click");

	$("#tourList").animate({
		"margin-left" : "-=170" // 170 is the size of a tour list element
	}, "slow", function() {
		var width;
		var toureListSize = ($("#tourList li").length - 1) * 170; 

		width = $("#tours").css("width");
		var viewPortSize = Number(width.substring(0, width.length - 2));

		width = $("#tourList").css("margin-left")
		var offSet = Number(width.substring(0, width.length - 2));

		// Offset is always negative
		// Checks if the totals size (pixel) of the list is smaller than the
		// size of the viewport plus the offset. If that's the case, it
		// indicates that the
		// end of the List was reached and disables the button.
		if (toureListSize <= viewPortSize - offSet) {
			if ($("#rightArrow").css("display") != "none") {
				$("#rightArrow").css("display", "none");
			}
		}
		$("#leftArrow").css("display", "block");

		// Rebinds the function to the button, so if it is clicked the next time
		// the animation will start again
		$("#rightArrow").bind("click", toureListRightShift);
	});
}

/**
 * Creates the route elements that are added to the route list
 * @param data {Object} - Contains route data that was loaded from the server
 * @returns NONE
 */
function addRoutesToSelection(data) {
	var json = JSON.parse(data);
	var defaultImage = $("#currentAction").val() == "Party" ? "utilities/pic/PartyDefault.png" : "utilities/pic/KulturDefault.png";
	

	// Clears the List
	$("#tourList").empty();
	
	// Create the route list element and adds them to the route list
	for (var i = 0; i < json.length; i++) {
		var listelement = "<li class='inline tourdata'>"
				+ "<input type='hidden' class='startingPoint' value='{\"coordinates\": ["+ json[i].firstLat	+ ", " + json[i].firstLong + "]}'>"
				+ "<input type='hidden' class='tourId' value='"	+ json[i].id + "'>"
				+ "<p "+ (json[i].name.length > 15 ? "title='"+ json[i].name + "'" : "") + ">" + (json[i].name.length > 15 ? json[i].name.substring(0,15) + "..." : json[i].name) + "</p>"
				+ "<div class='centered'>"
					+ "<i class='material-icons " + (json[i].avgRating >= 1 ? "activeStar" : "") + "'>grade</i>" // This part set the rating to the element
					+ "<i class='material-icons " + (json[i].avgRating >= 2 ? "activeStar" : "") + "'>grade</i>"
					+ "<i class='material-icons " + (json[i].avgRating >= 3 ? "activeStar" : "") + "'>grade</i>"
					+ "<i class='material-icons " + (json[i].avgRating >= 4 ? "activeStar" : "") + "'>grade</i>"
					+ "<i class='material-icons " + (json[i].avgRating >= 5 ? "activeStar" : "") + "'>grade</i>"
				+ "</div>"
				+ "<div class='iconWrapper'>"
					+ "<img class='tourIcon' src='"	+ (json[i].images[0] != undefined ? json[i].images[0] : defaultImage) + "'>" 
				+ "</div>" 
				+ "</li>";

		$("#tourList").append(listelement);
	}

	// Setting all Listeners
	toursSliderRight();
	toursClickEvent();
	toursHoverEvent();

	if($("#tourList").children().length == 0) {
		sendStatusMessage("Es konnte keine Route gefunden werden", "red");
		$("#tourInfoPanel").css("display", "none");
		$("#tours").css("display", "none");
	} else {
		$("#tours").show();
	}
}

/**
 * Checks if there are enough route element in the list that the right scroll is needed
 * @returns NONE
 */
function toursSliderRight() {
	width = $("#tours").css("width");
	var viewPortSize = Number(width.substring(0, width.length - 2));

	var toureListSize = ($("#tourList li").length - 1) * 170;

	if (toureListSize <= viewPortSize) {
		$("#rightArrow").css("display", "none");
	}
}

/**
 * This function will set a temporary marker on the map after clicking on a route in the route list. If there is another marker
 * of this type, it will be removed before the new marker is set
 * @returns NONE
 */
function toursClickEvent() {

	$(".tourdata").click(function() {
		var json = $(this).children(".startingPoint").val();
		var obj = JSON.parse(json);

		// If there was a click on a prev route, then frist remove the old marker
		if (permLayer != null) {
			getMap().removeLayer(permLayer)
		};
		
		// Center the map to the starting location of the route.
		getMap().panTo(obj.coordinates);
		permLayer = L.marker(obj.coordinates, {
			icon : L.mapquest.icons.marker({
				primaryColor : '#009933',
				secondaryColor : '#00cc00'
			})
		}).addTo(getMap());
		permLayer._icon.style.zIndex = 1000; // make sure the newly set marker is always on top
	});

}

/**
 * Show the starting location of a route from the route list on the map and loads it to the info panel, if the user hovers over it.
 * @returns NONE
 */
function toursHoverEvent() {
	var layer;
	$(".tourdata").mouseenter(function() {
				var json = $(this).children(".startingPoint").val();
				var obj = JSON.parse(json);
				
				// Get the id of the route where the user is hovering over
				var tour = $(this).children(".tourId").val();

				loadRouteToPanel(tour);	

				$("#tourInfoPanel").css("display", "block");

				// Load the marker of the first location of the route 
				// this one is displayed as long as the user has its mouse over the tour element
				layer = L.marker(obj.coordinates, {
					icon : L.mapquest.icons.marker({
						primaryColor : '#111111',
						secondaryColor : '#00cc00'
					})
				}).addTo(getMap());
				layer._icon.style.zIndex = 1001;
			});

	$(".tourdata").mouseleave(function() {
		// Removes the marker when the user is done is not hovering over the element anymore.
		getMap().removeLayer(layer);
	})
}

/**
 * Finds the route in the global route object and loads its information to the info panel. 
 * @param tour {Integer} - Contains the id of the route the user selected
 * @returns
 */
function loadRouteToPanel(tour) {
	var tourObj;
	var ratingElement;
	var defaultImage = $("#currentAction").val() == "Party" ? "utilities/pic/PartyDefault.png" : "utilities/pic/KulturDefault.png";
	
	// Find the route in the globalRoutes object.
	$.each(globalRoutes, function(i, v) {
		if (v.id == tour) {
			tourObj = v;
			return;
		}
	});
	
	// Insert the route information into the info panel
	$("#tourTypeOnPanle").val("global");
	$("#infoTourName").html(tourObj.name);
	$("#infoTourDescription").html(tourObj.description);
	$("#tourIdOnPanle").val(tourObj.id);

	// rating stars will be set based on the avgRating property of route object
	ratingElement = "<div class=\"ratingWrapper centered\"><i class='material-icons "
			+ (tourObj.avgRating >= 1 ? "activeStar" : "")
			+ "'>grade</i>" + "<i class='material-icons "
			+ (tourObj.avgRating >= 2 ? "activeStar" : "")
			+ "'>grade</i>" + "<i class='material-icons "
			+ (tourObj.avgRating >= 3 ? "activeStar" : "")
			+ "'>grade</i>" + "<i class='material-icons "
			+ (tourObj.avgRating >= 4 ? "activeStar" : "")
			+ "'>grade</i>" + "<i class='material-icons "
			+ (tourObj.avgRating >= 5 ? "activeStar" : "")
			+ "'>grade</i>" 
			+ "<a class=\"feedbackInfoOnPanle\" onClick=\"showFeedbackPopup("+tourObj.id+", 'Route')\"><i class='material-icons'>info_outline</i></a>"
			+ "</div>";

	$("#infoTourRating").html(ratingElement);
	
	$("#infoTourTime").html(tourObj.time.time);
	
	$("#infoTourPics").html("<img class='tourIcon' style=\"margin: auto;\" src='"	+ (tourObj.images[0] != undefined ? tourObj.images[0] : defaultImage) + "'>");

	$("#tourStops").empty();

	// Loads all the stops to the list of stops in the info panel
	$.each(globalRoutes, function(i, v) {
		if (v.id == tour) {
			$.each(v.stops, function(i, v) {
				$("#tourStops").append(
						"<li class='infotext stopMarker' value='{\"coords\": ["+v.latitude+", "+ v.longitude+"]}'><i class=\"material-icons centered\">place</i><br><p class=\"inline\">" + v.name
						+ "</p></li><hr>"
				);
			});
		}
	});
	
	stopsHoverEvent();
}

/**
 * Sends the stops information from a specific rounte to the mapquest direction API and draw the route on the map.
 * @param type {String} - Contains the information where to search for the selected route.
 * @returns NONE
 */
function calculateRoute(type) {
	var tour = $("#tourIdOnPanle").val();
	var tourObj;
	
	// makes sure that if a new route is calculated, the old route will be removed frist.
	removeCurrentRoute();
	
	// Checks where to search for the route. If the user call the function from the manage route propup, then search in userRoutes for it.
	if (type == "global") {
		$.each(globalRoutes, function(i, v) {
			if (v.id == tour) {
				tourObj = v;
				return;
			}
		});
	} else if (type == "user") {
		$.each(userRoutes, function(i, v) {
			if (v.id == tour) {
				tourObj = v;
				return;
			}
		});
	}

	// load the basic structure for the required json object
	var routeObj = getRoutingJsonStructure();

	$.each(tourObj.stops, function(i, v) {
		var newElement = {
			type : "s",
			latLng : {
				lat : v.latitude,
				lng : v.longitude
			}
		};
		routeObj.locations.push(newElement)
	});

	// this option makes sure that the api return a route that is optimized for pedestrian
	routeObj.options = {
		routeType : "pedestrian"
	};

	// evokes the route function of the api and draws the route
	L.mapquest.directions().route(routeObj);
	
}

/**
 * Checks all layers on the map if they have something to with the route that was draw on the map. If so they will be removed from the map.
 * @returns NONE
 */
function removeCurrentRoute() {
	// removes the current route based on the draggable property
	getMap().eachLayer(function(layer) {
		if (layer.options.draggable == true) {
			getMap().removeLayer(layer);
		}
	});
}

/**
 * Loads and shows the feedback of the currently selected route in the feedback popup
 * @returns
 */
function feedbackRoute() {
	var tour = $("#tourIdOnPanle").val();

	loadPopupContent("feedback");

	$("#feedbackForm input[name=type]").val("Route");
	$("#feedbackForm input[name=id]").val(tour);

}

/**
 * Loads and shows the feedback of the selected location in the feedback popup
 * @param markerId {Integer} - Contains the leaflet_id of the selected location
 * @returns NONE
 */
function feedbackLocation(markerId) {
	var location = globalLayer.getLayer(markerId);

	loadPopupContent("feedback");

	$("#feedbackForm input[name=type]").val("Location");
	$("#feedbackForm input[name=id]").val(location.info.id);

}

/**
 * Checks the GET parameter for the information, what the user wants to see. If there is no selection. The default "Party" will be set
 * @returns NONE
 */
$(document).ready(function() {
	var urlString = window.location.href;
	var url = new URL(urlString);
	
	// extracts the GET parameter form the url
	var currentAction = url.searchParams.get("type");

	// Sets the current action based on the information from the get parameter
	if (currentAction != null) {
		$("#currentAction").val(currentAction);
	} else {
		$("#currentAction").val("Party");
	}

	// Makes sure that the arrow between the option has the proper direction 
	if (currentAction == "Party") {
		$("#headerIconMiddle").html("chevron_left");
	} else if (currentAction == "Kultur") {
		$("#headerIconMiddle").html("chevron_right");
	} else {
		$("#headerIconMiddle").html("chevron_left");
	}
})

/**
 * Creates the feedback element for a specific location or route and adds them to the feedback list on the popup.
 * If there is no feedback found a default message will be displayed.
 * @param param {Object} - Contains the id of the element that the user wants to see the feedback of and the type of the feedback (route or location)
 * @returns NONE
 */
function loadFeedbackToPopup(param) {
	var data;
	var dataElement;
	var htmlElement;

	$("#feedbackList").empty();

	// Checks based on the information of param.type if the id is found in the location or route object
	if (param.type == "Location") {
		data = globalLayer;

		dataElement = data._layers[param.id].info;

	} else {
		data = globalRoutes;

		$.each(data, function(i, v) {
			if (v.id == param.id) {
				dataElement = v;
				return;
			}
		});
	}

	$("#feedbackHeaderTitle").html(dataElement.name);
	
	// If the object does not contain any feedback, set a default message
	if (dataElement.feedback.length == 0) {$("#feedbackList").append("<li class=\"centered\">Sei der Erste der ein Feedback abgibt!</li>")}
	
	// Creates the feedback elements that will be added to the feedback list 
	$.each(dataElement.feedback, function(i, v) {
		
		var userDelete = "";
		
		// Checks if the feedback was given by the user. If so add the functionality to edit or delete it
		if (v.author.id == $("#userId").val()) {
			userDelete = "<div class=\"userDelete\">" +
					"<i class=\"material-icons editIcon\" onClick=\"editFeedback("+v.id+", this)\">create</i>" +
					"<i class=\"material-icons deleteIcon\" onClick=\"confirmationFeedbackDeletion("+v.id+", this)\">delete_forever</i>" +
					"</div>";
		}
		
		// Making sure that the rating is displayed porperly. 
		htmlElement = "<li class=\"popupFeedback\">"
				+ "<div class=\"feebackRatingWrapper\">"
				+ "<input type=\"hidden\" value="+v.rating+">"
				+ "<i class='material-icons "
				+ (v.rating >= 1 ? "activeStar" : "")
				+ "'>grade</i>" + "<i class='material-icons "
				+ (v.rating >= 2 ? "activeStar" : "")
				+ "'>grade</i>" + "<i class='material-icons "
				+ (v.rating >= 3 ? "activeStar" : "")
				+ "'>grade</i>" + "<i class='material-icons "
				+ (v.rating >= 4 ? "activeStar" : "")
				+ "'>grade</i>" + "<i class='material-icons "
				+ (v.rating >= 5 ? "activeStar" : "")
				+ "'>grade</i>" + "</div>" 
				+ userDelete 
				+ "<div class=\"feebackCommentWrapper\">"
				+ "<p class=\"feedbackComment\">" + v.comment + "</p>"
				+ "</div>" + "</li>";

		$("#feedbackList").append(htmlElement);
	});
}

/**
 * Show the popup window for a new route. 
 * @param id {Integer} - Contains the leaflet_id of the first location that will be added to the route.
 * @returns NONE
 */
function showNewRoutePopup(id) {
	loadPopupContent("createRoute");

	$("#newRouteForm input[name=locationId]").val(id);
}

/**
 * Show the popup window that is used to add new locations to existing routes.
 * @param id {Interger} - Contains the leaflet_id of the location that the user wants to add to his route
 * @returns NONE
 */
function showUpdateRoutePopup(id) {
	loadPopupContent("updateFromRoute")
	$("#updateRouteForm input[name=locationId]").val(id);
	loadUserRoutes("update");

}

/**
 * Manages the popup for feedback. Makes sure that the right information is loaded to the popup
 * @param id {Integer} - Contains either the id of a route or the leaflet_id of a location
 * @param type {String} - Contains the information if the id is form a route or location
 * @returns NONE
 */
function showFeedbackPopup(id, type) {
	loadPopupContent("showFeedback");
	$("#typeOfShownFeedback").val(type);
	$("#idOfShownFeedback").val(id);
	loadFeedbackToPopup({id: id, type: type});
}

/**
 * Unloads the popup
 * @returns NONE
 */
function unloadPopup() {
	$("#myModal").hide();
}

/**
 * Loads the user routes to the select field in the manage route or add to route popup
 * @param type {String} - Contains the information if the selection should be filled in the manage or in the add popup
 * @returns NONE
 */
function loadUserRoutes(type) {
	
	// clears based on the information in type the selection fields values 
	if (type == "update") {
		$("#updateRouteForm select[name=routes]").empty();
		
	} else if (type == "show") {
		$("#manageRouteForm select[name=routes]").empty();
	}
	
	// Loading the new options to the popup based on the information in type.
	$.each(userRoutes, function(i, v) {
		if (i == 0) {
			if ($("#currentAction").val() == v.type) {
				changeRouteInformation(v.id)
			}
		}
		if (type == "update" && $("#currentAction").val() == v.type) {
			$("#updateRouteForm select[name=routes]").append(
					"<option value=" + v.id + ">" + v.name + "</option>");
			
		} else if (type == "show" && $("#currentAction").val() == v.type) {
			$("#manageRouteForm select[name=routes]").append(
					"<option value=" + v.id + ">" + v.name + "</option>");
		}
	});
	
	if ($("#manageRouteForm select[name=routes]").val() == null) {
		$("#editNameWrapper").html("");
		$("#editDescriptionWrapper").html("");
	}
}

/**
 * Loads the stops of a specific route to the stop list on the popup. Adds several buttons to it
 * @param routeId {Integer} - Contains the route id of which the stops should be loaded
 * @returns NONE
 */
function changeRouteInformation(routeId) {
	$("#tourStopsPopup").empty();

	// Makes sure that if the user does not have any routes, it will not do anything
	if (routeId == undefined) {
		return;
	}
	

	// Creates and adds all the stops from the selected user route to the user route stops list.
	// Every element has functionality to delete it. 
	$.each(userRoutes, function(i, v) {
		if (v.id == routeId) {
			$("#userRouteDescription").html(v.description);
			$.each(v.stops,
					function(i, v) {
				$("#tourStopsPopup").append(
						"<li class=\"tourStopsPopupData\"><div class=\"inline clearfix floatRight heightFix\"><i class=\"material-icons userDelete\" onClick=\"confirmationRoutePartDeletion("+v.id+", this)\">delete_forever</i></div><div class=\"locationDataRoute\"><p class=\"inline locationDataRouteText\">" + v.name
						+ "</p></li>");
			});
		}
	});
}

/**
 * Converts the input image of the user into a base 64 encode URL
 * @param input {Object} - Contains the input file 
 * @returns NONE
 */
function convertImageToBase64(input) {

	// Has to be a promise otherwise the convertion won't be done in time and the database record would contain an empty string
	return new Promise(function(resolve, reject){
		var error;
		
		// Checks if the input exists 
		if (!input) {
			console.log("Element existiert nicht.");
			
			resolve(null);
			
		} else if (!input.prop("files")) { // Is the file input type supported
			console.log("Dateiinput wird von Ihrem Browser nicht untersützt.");
			
			resolve(null);
			
		} else if (!input.prop("files")[0]) { // Was a file selected?
				
			resolve(null);
			
		} else if (input.prop("files")[0].size > 10000000) { // Is the file smaller than 10 MB
			console.log("Kein 4K bitte")
			resolve(null);
		} else { 
			var file = input.prop("files")[0];
			
			// Loads the information the file input and converts it
			var fr = new FileReader();
			var base64;
			fr.onload = function(e) {
				base64 = e.target.result;
				resolve(base64);
			};
			fr.readAsDataURL(file);
		}
	});
}

/**
 * Loads the popup for handling the user routes
 * @returns
 */
function loadUserRoutePopup() {
	var url = window.location.href
	if(url.indexOf("index") > 0){
		window.location = "NewMap.jsp?type=Party";
	}
	loadPopupContent("manageRoutes");
	loadUserRoutes("show");
}

/**
 * Loads the selected user route to the info panel.
 * @param id {Integer} - Contains the id of the route the user selected. 
 * @returns NONE
 */
function showUserRouteOnInfo(id) {
	
	var tour = id; // Contains the id of the route we are looking for 
	var tourObj; // Will contain the route object if the one is found
	var defaultImage = $("#currentAction").val() == "Party" ? "utilities/pic/PartyDefault.png" : "utilities/pic/KulturDefault.png";

	var ratingElement;
	
	// Gets the proper route from the userRoutes object
	$.each(userRoutes, function(i, v) {
		if (v.id == tour) {
			tourObj = v;
			return;
		}
	});
	
	// Loads all the information from the user route to the info panel
	$("#tourTypeOnPanle").val("user");
	$("#infoTourName").html(tourObj.name);
	$("#infoTourDescription").html(tourObj.description);
	$("#tourIdOnPanle").val(tourObj.id);

	// Makes sure  that the rating stars are properly displayed
	ratingElement = "<div class=\"ratingWrapper centered\"><i class='material-icons "
			+ (tourObj.avgRating >= 1 ? "activeStar" : "")
			+ "'>grade</i>" + "<i class='material-icons "
			+ (tourObj.avgRating >= 2 ? "activeStar" : "")
			+ "'>grade</i>" + "<i class='material-icons "
			+ (tourObj.avgRating >= 3 ? "activeStar" : "")
			+ "'>grade</i>" + "<i class='material-icons "
			+ (tourObj.avgRating >= 4 ? "activeStar" : "")
			+ "'>grade</i>" + "<i class='material-icons "
			+ (tourObj.avgRating >= 5 ? "activeStar" : "")
			+ "'>grade</i>" 
			+ "<a class=\"feedbackInfoOnPanle\" onClick=\"showFeedbackPopup({id:"+tourObj.id+", type: 'Route'})\"><i class='material-icons'>info_outline</i></a>"
			+ "</div>";

	$("#infoTourRating").html(ratingElement);
	
	$("#infoTourTime").html(tourObj.time.time);
	
	$("#infoTourPics").html("<img class='tourIcon' style=\"margin: auto;\" src='"	+ (tourObj.images[0] != undefined ? tourObj.images[0] : defaultImage) + "'>");

	$("#tourStops").empty();

	// Loads all the stop to the info panel stops list
	$.each(tourObj.stops, function(i, v) {
		$("#tourStops").append(
				"<li class='infotext stopMarker' value='{\"coords\": ["+v.latitude+", "+ v.longitude+"]}'><i class=\"material-icons centered\">place</i><br><p class=\"inline\">" + v.name
				+ "</p></li><hr>");
	});

	stopsHoverEvent();
	
	$("#tourInfoPanel").css("display", "block");

}

/**
 * Checks if a location was already added to a specific route
 * @param locationId {Integer} - Contains the leaflet_id of the location that should be added to the route.
 * @param routeId {Integer} - Contains the id of the route that user wants to the location to.
 * @returns returns an error if the location was already added
 */
function isLocationInRoute(locationId, routeId) {
	var location = globalLayer.getLayer(locationId).info; // Get the information from the location the user wants to add
	var tourObj; // Contains the route object if one was found.
	var error = false; // By default there is no error.
	
	// Search for the route
	$.each(userRoutes, function(i,v) {
		if (v.id == routeId) {
			tourObj = v;
			return;
		}
	});
	
	// search for the location in the route
	$.each(tourObj.stops, function(i,v) {
		if (v.id == location.id) {
			error = true;
		}
	});
	
	return error;
}


var prevLocationContent; // Contains the content of the Location before it was modified
var prevLocationElement; // Contains the original appearance of the location before it was modified

/**
 * Changes the bin icon to a hook and a cross icon in order to give the user the possibility to confirm his deletion
 * @param locationId {Integer} - Contains the leaflet_id of the location
 * @param element {Object} - Contains the HTML Element that is changed
 * @returns NONE
 */
function confirmationRoutePartDeletion(locationId, element) {
	var counter = 0; // Will be used to make sure that only one element can be deleted at once
	var form; // What form called for the confirmation
	
	// Checking if a specific form element exists, if not the call was made form the other form
	if ($("#manageRouteForm").prop("id") == null) {
		form = "updateFromRoute";
	} else {
		form = "manageRouteForm";
	}
	
	// Checks if there is already one confirmation ongoing
	$("#tourStopsPopup li").each(function(i,v) {
		if (v.firstElementChild.childElementCount > 1) {
			counter = counter + 1;
		}
	});
	
	// If there is one, change it back and set the other one.
	if (counter > 0) {
		prevLocationElement.innerHTML = prevLocationContent;
		
		prevLocationElement = null;
		prevLocationContent = null;
		
		counter = 0
	}
	
	
	var replaceElement = element.parentElement;
	prevLocationContent = replaceElement.innerHTML;
	prevLocationElement = replaceElement;
	
	// Add the hook and the cross element
	var confirm = "<i class=\"material-icons green clickable\" onClick=\"removeFromRoute("
		+ locationId+", " 
		+ $("#"+form+" select[name=routes] option:selected").val()+")\">done</i>" 
		+ "<i class=\"material-icons red clickable\" onClick=\"cancelDeletionOrChange('route')\">close</i>"; 
	
	replaceElement.innerHTML = confirm;
}

// Needed if the user decides to not delete the element.
var prevFeedbackContent;
var prevFeedbackElement;

/**
 * Changes the bin icon into a hook and a cross icon in order to give the user the possible to confirm or cancel the deletion of the feedback
 * @param feedbackId {Integer} - Contains the id of the feedback that should be modified
 * @param element {Object} - Contains the unmodified element of the feedback
 * @returns
 */
function confirmationFeedbackDeletion(feedbackId, element) {
	// Saves the content of the existing content of the feedback list
	var replaceElement = element.parentElement;
	prevFeedbackContent = replaceElement.innerHTML;
	prevFeedbackElement = replaceElement;
	
	// Adds the new option buttons
	var confirm = "<i class=\"material-icons green clickable\" onClick=\"deleteFeedback('"
		+ $("#typeOfShownFeedback").val() 
		+ "', " + $("#idOfShownFeedback").val() 
		+ ", " + feedbackId + ")\">done</i>" 
		+ "<i class=\"material-icons red clickable\" onClick=\"cancelDeletionOrChange('feedback')\">close</i>"; 
	
	replaceElement.innerHTML = confirm;
}

/**
 * Reverts all the changes if the user decides to cancel the change or deletion of any element
 * @param type {String} - Contains the information about what the changes have to be reverted 
 * @returns NONE
 */
function cancelDeletionOrChange(type) {
	
	// Checks where the confirmation context was set and removes them
	if (type == "feedback") {
		prevFeedbackElement.innerHTML = prevFeedbackContent;
		
		prevFeedbackElement = null;
		prevFeedbackContent = null;
	} else if (type == "route") {
		prevLocationElement.innerHTML = prevLocationContent;
		
		prevLocationElement = null;
		prevLocationContent = null;
	} else if (type == "edit") {
		// Disable click event
		$(".feedbackStar").click(null);
		
		prevFeedbackElement.innerHTML = prevFeedbackContent;
		
		prevFeedbackElement = null;
		prevFeedbackContent = null;
		unchangedFeedbackElement.innerHTML = unchangedComment;
		unchangedRatingElement.innerHTML = unchangedRating;
		
		unchangedComment = null;
		unchangedFeedbackElement = null;
		unchangedFeedbackContent= null;
		unchangedRatingElement = null;
		unchangedRating = null;
	}
	
}

// Variables that are needed to make the feedback directly editable 
// Contain the prev values and appearances of the elements 
var unchangedFeedbackElement;
var unchangedFeedbackContent;
var unchangedComment;
var unchangedRatingElement;
var unchangedRating; 

/**
 * Changes the pen icon into the hook and cross icon. Also changes the paragraph into a textarea where the user can make direct changes.
 * @param feedbackId {Integer} - Contains the id of the feedback that the user wants to change
 * @param element {Object} - Contains the element from where the call was made
 * @returns NONE
 */
function editFeedback(feedbackId, element) {
	// Save the existing content of the feedback in the global variables.
	var replaceElement = element.parentElement;
	var stars = element.parentElement.parentElement.firstElementChild.children;
	var starArray = []; // Contains the id's of the stars that are editable.
	unchangedComment = element.parentElement.parentElement.lastElementChild.innerHTML;
	unchangedFeedbackContent = replaceElement.innerHTML;
	unchangedFeedbackElement = element.parentElement.parentElement.lastElementChild;
	unchangedRatingElement = element.parentElement.parentElement.firstElementChild;
	unchangedRating = element.parentElement.parentElement.firstElementChild.innerHTML;
	
	// Takes the content of the paragraph and buts it into an editable textarea
	element.parentElement.parentElement.lastElementChild.innerHTML = "<textarea id=\"feedbackEditArea\">"+element.parentElement.parentElement.lastElementChild.lastElementChild.innerHTML+"</textarea>";
	
	// Make the stars editable again
	// Starts with 1 because the first element must be ignored
	stars[0].id = "feedbackRatingValue";
	
	for(var i = 1; i < stars.length; i++) {
		stars[i].id = "feedbackStar"+(i);
		stars[i].className = stars[i].className + " feedbackStar ";
		starArray.push("feedbackStar"+(i));
	}
	
	// Adding the click function to each star of the edited feedback.
	$(".feedbackStar").click(function(){

		var id = "";
		var starCount = 1;
		var on = true;
		var stars = starArray;
		
		// Checks based on the click of the user how many stars should be active
		id = $(this).attr("id");
		for(var i = 0; i < stars.length; i++){
			if(stars[i] == id){
				$("#"+stars[i]).addClass('activeStar');
				on = false;
			} else {
				if(on){
					$("#"+stars[i]).addClass('activeStar');
					starCount++;
				} else {
					$("#"+stars[i]).removeClass('activeStar');
				}
			}
		}
		
		$("#feedbackRatingValue").val(starCount);
	});
	
	confirmationFeedbackChange(feedbackId, element);
}

/**
 * Checks if the user confirmed the change and executes it
 * @param feedbackId {Integer} - Contains the id of the feedback that the user wants to change
 * @param element {Object} - Contains the object from where the function was called
 * @returns NONE
 */
function confirmationFeedbackChange(feedbackId, element) {
	var replaceElement = element.parentElement;
	prevFeedbackContent = replaceElement.innerHTML;
	prevFeedbackElement = replaceElement;
	
	// Adds the confirmation buttons
	var confirm = "<i class=\"material-icons green clickable\" onClick=\"changeFeedback('"
		+ $("#typeOfShownFeedback").val() 
		+ "', " + $("#idOfShownFeedback").val() 
		+ ", " + feedbackId + ")\">done</i>" 
		+ "<i class=\"material-icons red clickable\" onClick=\"cancelDeletionOrChange('edit')\">close</i>"; 
	
	replaceElement.innerHTML = confirm;
}

/**
 * Loads a text to a frame that is shown for a few seconds to confirm an action to the user
 * @param message {String} - Contains the displayed text
 * @param color {String} - Contains the color in which the text will be displayed
 * @returns NONE
 */
function sendStatusMessage(message, color) {
	$("#statusMessageText").html(message);
	$("#statusMessageText").css("color", color);
	$("#statusMessage").animate({"bottom": "5px"}, "slow").delay(3000).animate({"bottom": "-50px"},"slow");
}

/**
 * Calculates the average feedback rating of the object that was given to it
 * @param obj {Object} - Contains the object of which the average rating should be calculated.
 * @returns {Integer} - The calculated rating will be returned
 */
function calculateAvgRating(obj) {
	var elementCount = 0;
	var ratingTotal = 0;
	
	$.each(obj.feedback, function(i,v) {
		ratingTotal += parseInt(v.rating);
		elementCount = i + 1;
	});
	
	if (ratingTotal == 0) {
		return 3;
	} else {
		return ratingTotal / elementCount;
	}
}

var prevRouteContent;

/**
 * Changes the edit icon to the confirmation icons. 
 * @param routeId {Integer} - Contains the id of the route of which the name should be changed
 * @returns NONE
 */
function confirmationNameChange(routeId){
	var element;
	var route;
	
	$.each(userRoutes, function(i,v) {
		if (v.id == routeId) {
			route = v;
			return;
		}
	});
	
	element = $("#editNameWrapper");
	
	prevRouteContent = element.html();
	
	// Disable the select field
	$("#routeSelect").prop("disabled",true);
	
	element.html("<input id=\"modifiedRouteName\">" +
			"<div style=\"position: relative; float: right\">" +
				"<i class=\"material-icons green clickable\" onClick=\"changeRouteName()\">done</i>" +
				"<i class=\"material-icons red clickable\" onClick=\"cancelRouteNameChange()\">close</i>" +
			"</div>");
	
	$("#modifiedRouteName").val(route.name);
}

/**
 * Cancels the name change of the route
 * @returns NONE
 */
function cancelRouteNameChange() {
	var element;
	
	element = $("#editNameWrapper");
	
	$("#routeSelect").prop("disabled",false);
	
	element.html(prevRouteContent);
	
	// Set the click function again
	$("#editRouteName").click(function(){
		confirmationNameChange($("#manageRouteForm select[name=routes]").val());
	});
}

var prevRouteDescription

function editRouteDescription(routeId) {
	var element;
	var route;
	
	$.each(userRoutes, function(i,v) {
		if (v.id == routeId) {
			route = v;
			return;
		}
	});
	
	element = $("#editDescriptionWrapper");
	
	prevRouteDescription = element.html();
	
	
	element.html("<textarea style=\"height: 50px; resize: none\" id=\"modifiedRouteDescription\"></textarea>" +
			"<div style=\"position: relative; float: right\">" +
				"<i class=\"material-icons green clickable\" onClick=\"updateRouteDescription()\">done</i>" +
				"<i class=\"material-icons red clickable\" onClick=\"cancelRouteDescriptionChange()\">close</i>" +
			"</div>");
	
	$("#modifiedRouteDescription").val(route.description);
}

function cancelRouteDescriptionChange() {
	var element;
	
	element = $("#editDescriptionWrapper");

	element.html(prevRouteDescription);
	
	// Set the click function again
	$("#editRouteDescription").click(function(){
		editRouteDescription($("#manageRouteForm select[name=routes]").val());
	});
}

/**
 * Calculates the total travel time of culture routes
 * @param route {Object} - Contains the culture route object
 * @param waytime {String} - Contains the time string of the waytime
 * @returns {String} - Contains the total time needed
 */
function calculateTotalTravelTime(route, waytime) {
	var totalTime = "00:00:00";
	
	$.each(route.stops, function(i,v) {
		totalTime = addTimes(totalTime, v.timeString);
	});
	
	totalTime = addTimes(totalTime, waytime);
	
	return totalTime;
}

/**
 * Adds two time strings together
 * @param startTime {String} - Starting time
 * @param endTime {String} - Time that should be added
 * @returns {String} - Returns the sum of the time strings
 */
function addTimes (startTime, endTime) {
	  var times = [ 0, 0, 0 ]
	  var max = times.length

	  var a = (startTime || '').split(':')
	  var b = (endTime || '').split(':')

	  // normalize time values
	  for (var i = 0; i < max; i++) {
	    a[i] = isNaN(parseInt(a[i])) ? 0 : parseInt(a[i])
	    b[i] = isNaN(parseInt(b[i])) ? 0 : parseInt(b[i])
	  }

	  // store time values
	  for (var i = 0; i < max; i++) {
	    times[i] = a[i] + b[i]
	  }

	  var hours = times[0]
	  var minutes = times[1]
	  var seconds = times[2]

	  if (seconds >= 60) {
	    var m = (seconds / 60) << 0
	    minutes += m
	    seconds -= 60 * m
	  }

	  if (minutes >= 60) {
	    var h = (minutes / 60) << 0
	    hours += h
	    minutes -= 60 * h
	  }

	  return ('0' + hours).slice(-2) + ':' + ('0' + minutes).slice(-2) + ':' + ('0' + seconds).slice(-2)
	}

/**
 * Enables the hover event for the tour stops on the info panel
 * @returns NONE
 */
function stopsHoverEvent() {
	var layer;
	$(".stopMarker").mouseenter(function() {
				var json = $(this).attr("value");
				var obj = JSON.parse(json);
				

				// Load the marker of the first location of the route 
				// this one is displayed as long as the user has its mouse over the tour element
				layer = L.marker(obj.coords, {
					icon : L.mapquest.icons.marker({
						primaryColor : '#111111',
						secondaryColor : '#ff0000'
					})
				}).addTo(getMap());
				layer._icon.style.zIndex = 1001;
			});

	$(".stopMarker").mouseleave(function() {
		// Removes the marker when the user is done is not hovering over the element anymore.
		getMap().removeLayer(layer);
	})
}