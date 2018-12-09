var permLayer; // Is used in multiple functions thats why it has to be global

// This function clears the searchbar
// Parameter inputId: Contains the ID of the element that should be cleared
function clearInput(inputId) {
	$("#" + inputId).val("");
}

function showNewPointPopup(marker) {
	loadPopupContent("createNew");
	loadMarkerinfoToSubmitForm(marker);
	$("#myModal").css("display", "block");
}

function showUpdatePointPopup(markerId) {
	loadPopupContent("update");
	loadDataToUpdateForm(markerId)
	$("#myUpdateModal").css("display", "block");
}

function loadDataToUpdateForm(markerId) {
	var marker = globalLayer.getLayer(markerId);

	// Load Information to Form
	// TODO implement picture
	$("#updateLocationForm input[name=id]").val(marker._leaflet_id);
	$("#updateLocationForm input[name=locationName]").val(marker.info.name);
	$("#updateLocationForm textarea[name=description]").val(
			marker.info.description);
	$("#updateLocationForm input[name=time]").val(marker.info.time.time);

}

function loadMarkerinfoToSubmitForm(marker) {
	$("#newLat").val(marker.getLatLng()["lat"]);
	$("#newLng").val(marker.getLatLng()["lng"]);
}

// Loads the position of the user as soon as he enters the page
$(document).ready(function getLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(showPosition);
	} else {
		console.log("Is not supported");
	}
})

// Prints out the geo point that was loaded to the map.
var myLocation; // Needed if the user wants to add his location to the database 

function showPosition(position) {
	var coords = '{ "coordinates": ["' + position.coords.latitude + '" ,"'
			+ position.coords.longitude + '"]}';
	var json = JSON.parse(coords);

	myLocation = L.marker(json.coordinates, {
		icon : L.mapquest.icons.marker({
			primaryColor : '#111111',
			secondaryColor : '#0066ff'
		})
	}).addTo(getMap())
	
	if ($("#userId").val() != null) {
		myLocation.bindPopup("<p>Ihr Standort nach IP Adresse</p><p>Möchten Sie den Standort hinzufügen</p><button class=\"fullSize button\" onClick='showNewPointPopup(myLocation)'>Hinzufügen</button>");
	} else {
		myLocation.bindPopup("<p>Ihr Standort nach IP Adresse</p>");
	}
	
	getMap().panTo(json.coordinates);
}

function toureListLeftShift() {
	// Disables the button until the animation is done. Prevents confusing
	// result if the button was clicked too often
	$("#leftArrow").unbind("click");

	$("#tourList").animate({
		"margin-left" : "+=170"
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

function toureListRightShift() {

	// Disables the button until the animation is done. Prevents confusing
	// result if the button was clicked too often
	$("#rightArrow").unbind("click");

	$("#tourList").animate({
		"margin-left" : "-=170"
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

function addRoutesToSelection(data) {
	var json = JSON.parse(data);

	// Clears the List
	$("#tourList").empty();

	for (var i = 0; i < json.length; i++) {
		var listelement = "<li class='inline tourdata'>"
				+ "<input type='hidden' class='startingPoint' value='{\"coordinates\": ["
				+ json[i].firstLat
				+ ", "
				+ json[i].firstLong
				+ "]}'>"
				+ "<input type='hidden' class='tourId' value='"
				+ json[i].id
				+ "'>"
				+ "<p>"
				+ json[i].name
				+ "</p>"
				+ "<div class='centered'>"
				+ "<i class='material-icons "
				+ (json[i].avgRating >= 1 ? "activeStar" : "")
				+ "'>grade</i>"
				+ "<i class='material-icons "
				+ (json[i].avgRating >= 2 ? "activeStar" : "")
				+ "'>grade</i>"
				+ "<i class='material-icons "
				+ (json[i].avgRating >= 3 ? "activeStar" : "")
				+ "'>grade</i>"
				+ "<i class='material-icons "
				+ (json[i].avgRating >= 4 ? "activeStar" : "")
				+ "'>grade</i>"
				+ "<i class='material-icons "
				+ (json[i].avgRating >= 5 ? "activeStar" : "")
				+ "'>grade</i>"
				+ "</div>"
				+ "<div class='iconWrapper'>"
				+ "<img class='tourIcon' src='"
				+ (json[i].images.length > 0 ? json[i].images[i] + "'"
						: "utilities/pic/OP2.jpg'") + ">" + "</div>" + "</li>";

		$("#tourList").append(listelement);
	}

	// Setting all Listeners
	toursSliderRight();
	toursClickEvent();
	toursHoverEvent();

	$("#tours").show();
}

// Checks if there are enough element that the right scoll is needed
function toursSliderRight() {
	width = $("#tours").css("width");
	var viewPortSize = Number(width.substring(0, width.length - 2));

	var toureListSize = ($("#tourList li").length - 1) * 170;

	if (toureListSize <= viewPortSize) {
		$("#rightArrow").css("display", "none");
	}
}

// This function will set a point after clicking on a route. If there is another
// marker
// it will be removed before the new marker is set
function toursClickEvent() {

	$(".tourdata").click(function() {
		var json = $(this).children(".startingPoint").val();
		var obj = JSON.parse(json);

		if (permLayer != null) {
			getMap().removeLayer(permLayer)
		}
		;

		getMap().panTo(obj.coordinates);
		permLayer = L.marker(obj.coordinates, {
			icon : L.mapquest.icons.marker({
				primaryColor : '#009933',
				secondaryColor : '#00cc00'
			})
		}).addTo(getMap());
		permLayer._icon.style.zIndex = 1000;
	});

}

// When hovering over a route
function toursHoverEvent() {
	var layer;
	$(".tourdata").mouseenter(
			function() {
				var json = $(this).children(".startingPoint").val();
				var obj = JSON.parse(json);

				var tour = $(this).children(".tourId").val();
				var tourObj;

				var ratingElement;

				$.each(globalRoutes, function(i, v) {
					if (v.id == tour) {
						tourObj = v;
						return;
					}
				});
				// TODO Data must be loaded before the panel is shown
				
				$("#tourTypeOnPanle").val("global");
				$("#infoTourName").html(tourObj.name);
				$("#infoTourDescription").html(tourObj.description);
				$("#tourIdOnPanle").val(tourObj.id);

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

				$("#tourStops").empty();

				$.each(globalRoutes, function(i, v) {
					if (v.id == tour) {
						$.each(v.stops, function(i, v) {
							$("#tourStops").append(
									"<li class='infotext'>" + v.name
											+ "</li><hr>");
						})
					}
				});

				$("#tourInfoPanel").css("display", "block");

				layer = L.marker(obj.coordinates, {
					icon : L.mapquest.icons.marker({
						primaryColor : '#111111',
						secondaryColor : '#00cc00'
					})
				}).addTo(getMap());
				layer._icon.style.zIndex = 1001;
			});

	$(".tourdata").mouseleave(function() {
		// $("#tourInfoPanel").css("display", "none");
		getMap().removeLayer(layer);
	})
}

function calculateRoute(type) {
	var tour = $("#tourIdOnPanle").val();
	var tourObj;

	removeCurrentRoute();
	
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

	routeObj.options = {
		routeType : "pedestrian"
	};

	L.mapquest.directions().route(routeObj);
	
}

function removeCurrentRoute() {
	// removes the current route based on the draggable property
	getMap().eachLayer(function(layer) {
		if (layer.options.draggable == true) {
			getMap().removeLayer(layer);
		}
	});
}

function feedbackRoute() {
	var tour = $("#tourIdOnPanle").val();

	loadPopupContent("feedback");

	$("#feedbackForm input[name=type]").val("Route");
	$("#feedbackForm input[name=id]").val(tour);

}

function feedbackLocation(markerId) {
	var location = globalLayer.getLayer(markerId);

	loadPopupContent("feedback");

	$("#feedbackForm input[name=type]").val("Location");
	$("#feedbackForm input[name=id]").val(location.info.id);

}

// Checks what the user is looking for
$(document).ready(function() {
	var urlString = window.location.href;
	var url = new URL(urlString);

	var currentAction = url.searchParams.get("type");

	if (currentAction != null) {
		$("#currentAction").val(currentAction);
	} else {
		$("#currentAction").val("Party");
	}

	if (currentAction == "Party") {
		$("#headerIconMiddle").html("chevron_left");
	} else if (currentAction == "Kultur") {
		$("#headerIconMiddle").html("chevron_right");
	} else {
		$("#headerIconMiddle").html("chevron_left");
	}
})

function loadFeedbackToPopup(param) {
	var data;
	var dataElement;
	var htmlElement;

	$("#feedbackList").empty();

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
	// TODO add delete Button if user is logged in and is
	$.each(dataElement.feedback, function(i, v) {
		
		var userDelete = "";
		
		if (v.author.id == $("#userId").val()) {
			userDelete = "<div class=\"userDelete\">" +
					"<i class=\"material-icons editIcon\" onClick=\"editFeedback("+v.id+", this)\">create</i>" +
					"<i class=\"material-icons deleteIcon\" onClick=\"confirmationFeedbackDeletion("+v.id+", this)\">delete_forever</i>" +
					"</div>";
		}
		
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

function showNewRoutePopup(id) {
	loadPopupContent("createRoute");

	$("#newRouteForm input[name=locationId]").val(id);
}

function showUpdateRoutePopup(id) {
	loadPopupContent("updateFromRoute")
	$("#updateRouteForm input[name=locationId]").val(id);
	loadUserRoutes("update");

}

function showFeedbackPopup(param) {
	loadPopupContent("showFeedback");
	$("#typeOfShownFeedback").val(param.type);
	$("#idOfShownFeedback").val(param.id);
	loadFeedbackToPopup(param);
}

function unloadPopup() {
	$("#myModal").hide();
}

function loadUserRoutes(type) {
	
	if (type == "update") {
		$("#updateRouteForm select[name=routes]").empty();
		
	} else if (type == "show") {
		$("#manageRouteForm select[name=routes]").empty();
	}
	
	
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
}

function changeRouteInformation(routeId) {
	$("#tourStopsPopup").empty();

	if (routeId == undefined) {
		return;
	}
	
	$.each(userRoutes, function(i, v) {
		if (v.id == routeId) {
			$.each(v.stops,
					function(i, v) {
				$("#tourStopsPopup").append(
						"<li class=\"tourStopsPopupData\"><div class=\"inline clearfix floatRight heightFix\"><i class=\"material-icons userDelete\" onClick=\"confirmationRoutePartDeletion("+v.id+", this)\">delete_forever</i></div><div class=\"locationDataRoute\"><p class=\"inline locationDataRouteText\">" + v.name
						+ "</p></li>");
			});
		}
	});
}

function convertImageToBase64(input) {

	var error;

	if (!input) {
		console.log("Um, couldn't find the fileinput element.");
	} else if (!input.prop("files")) {
		console
				.log("This browser doesn't seem to support the `files` property of file inputs.");
	} else if (!input.prop("files")[0]) {
		
		return new Promise(function(resolve, reject) {
			var file = new File([""], "./WebContent/utilities/pic/Bild1.jpg", {type: "image/jpeg"});

			var fr = new FileReader();
			var base64;
			fr.onload = function(e) {
				base64 = e.target.result;
				resolve(base64);
			};
			fr.readAsDataURL(file);
			
		});
		
	} else if (input.prop("files")[0].size > 10000000) {
		console.log("Kein 4K bitte")
	} else {

		return new Promise(function(resolve, reject) {
			var file = input.prop("files")[0];

			var fr = new FileReader();
			var base64;
			fr.onload = function(e) {
				base64 = e.target.result;
				resolve(base64);
			};
			fr.readAsDataURL(file);
		});
	}
}

function loadUserRoutePopup() {
	loadPopupContent("manageRoutes");
	loadUserRoutes("show");
}

function showUserRouteOnInfo(id) {
	
	var tour = id;
	var tourObj;

	var ratingElement;

	$.each(userRoutes, function(i, v) {
		if (v.id == tour) {
			tourObj = v;
			return;
		}
	});
	
	$("#tourTypeOnPanle").val("user");
	$("#infoTourName").html(tourObj.name);
	$("#infoTourDescription").html(tourObj.description);
	$("#tourIdOnPanle").val(tourObj.id);

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

	$("#tourStops").empty();


	$.each(tourObj.stops, function(i, v) {
		$("#tourStops").append(
				"<li class='infotext'>" + v.name
						+ "</li><hr>");
	});

	$("#tourInfoPanel").css("display", "block");

}

function isLocationInRoute(locationId, routeId) {
	var location = globalLayer.getLayer(locationId).info;
	var tourObj;
	var error = false;
	
	$.each(userRoutes, function(i,v) {
		if (v.id == routeId) {
			tourObj = v;
			return;
		}
	});
	
	$.each(tourObj.stops, function(i,v) {
		if (v.id == location.id) {
			error = true;
		}
	});
	
	return error;
}


var prevLocationContent;
var prevLocationElement;

function confirmationRoutePartDeletion(locationId, element) {
	var counter = 0;
	var form;
	
	if ($("#manageRouteForm").prop("id") == null) {
		form = "updateFromRoute";
	} else {
		form = "manageRouteForm";
	}
	
	$("#tourStopsPopup li").each(function(i,v) {
		if (v.firstElementChild.childElementCount > 1) {
			counter = counter + 1;
		}
	});
	
	if (counter > 0) {
		prevLocationElement.innerHTML = prevLocationContent;
		
		prevLocationElement = null;
		prevLocationContent = null;
		
		counter = 0
	}
	
	
	var replaceElement = element.parentElement;
	prevLocationContent = replaceElement.innerHTML;
	prevLocationElement = replaceElement;
	
	//TODO Add Delete call
	var confirm = "<i class=\"material-icons green clickable\" onClick=\"removeFromRoute("
		+ locationId+", " 
		+ $("#"+form+" select[name=routes] option:selected").val()+")\">done</i>" 
		+ "<i class=\"material-icons red clickable\" onClick=\"cancelDeletionOrChange('route')\">close</i>"; 
	
	replaceElement.innerHTML = confirm;
}

// Needed if the user decides to not delete the element.
var prevFeedbackContent;
var prevFeedbackElement;

function confirmationFeedbackDeletion(feedbackId, element) {
	var replaceElement = element.parentElement;
	prevFeedbackContent = replaceElement.innerHTML;
	prevFeedbackElement = replaceElement;
	
	var confirm = "<i class=\"material-icons green clickable\" onClick=\"deleteFeedback('"
		+ $("#typeOfShownFeedback").val() 
		+ "', " + $("#idOfShownFeedback").val() 
		+ ", " + feedbackId + ")\">done</i>" 
		+ "<i class=\"material-icons red clickable\" onClick=\"cancelDeletionOrChange('feedback')\">close</i>"; 
	
	replaceElement.innerHTML = confirm;
}

function cancelDeletionOrChange(type) {
	
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

var unchangedFeedbackElement;
var unchangedFeedbackContent;
var unchangedComment;
var unchangedRatingElement;
var unchangedRating; 

function editFeedback(feedbackId, element) {
	var replaceElement = element.parentElement;
	var stars = element.parentElement.parentElement.firstElementChild.children;
	var starArray = [];
	unchangedComment = element.parentElement.parentElement.lastElementChild.innerHTML;
	unchangedFeedbackContent = replaceElement.innerHTML;
	unchangedFeedbackElement = element.parentElement.parentElement.lastElementChild;
	unchangedRatingElement = element.parentElement.parentElement.firstElementChild;
	unchangedRating = element.parentElement.parentElement.firstElementChild.innerHTML;
	
	
	element.parentElement.parentElement.lastElementChild.innerHTML = "<textarea id=\"feedbackEditArea\">"+element.parentElement.parentElement.lastElementChild.lastElementChild.innerHTML+"</textarea>";
	
	// Make the stars editable again
	// Starts with 1 because the first element must be ignored
	stars[0].id = "feedbackRatingValue";
	
	for(var i = 1; i < stars.length; i++) {
		stars[i].id = "feedbackStar"+(i);
		stars[i].className = stars[i].className + " feedbackStar ";
		starArray.push("feedbackStar"+(i));
	}
	
	$(".feedbackStar").click(function(){

		var id = "";
		var starCount = 1;
		var on = true;
		var stars = starArray;
		
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

function confirmationFeedbackChange(feedbackId, element) {
	var replaceElement = element.parentElement;
	prevFeedbackContent = replaceElement.innerHTML;
	prevFeedbackElement = replaceElement;
	
	var confirm = "<i class=\"material-icons green clickable\" onClick=\"changeFeedback('"
		+ $("#typeOfShownFeedback").val() 
		+ "', " + $("#idOfShownFeedback").val() 
		+ ", " + feedbackId + ")\">done</i>" 
		+ "<i class=\"material-icons red clickable\" onClick=\"cancelDeletionOrChange('edit')\">close</i>"; 
	
	replaceElement.innerHTML = confirm;
}

function sendStatusMessage(message, color) {
	$("#statusMessageText").html(message);
	$("#statusMessageText").css("color", color);
	$("#statusMessage").animate({"bottom": "5px"}, "slow").delay(3000).animate({"bottom": "-50px"},"slow");
}
