//This function clears the searchbar
// Parameter inputId: Contains the ID of the element that should be cleared
function clearInput(inputId) {
	$("#"+inputId).val("");
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

function loadDataToUpdateForm(markerId){
	var marker = globalLayer.getLayer(markerId);
	
	// Load Information to Form
	// TODO implement picture
	$("#updateLocationForm input[name=id").val(marker._leaflet_id);
	$("#updateLocationForm input[name=locationName").val(marker.info.name);
	$("#updateLocationForm textarea[name=description").val(marker.info.description);
	$("#updateLocationForm input[name=time").val(marker.info.time.time);
	
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
function showPosition(position) {
	var coords = '{ "coordinates": ["'+position.coords.latitude+'" ,"'+position.coords.longitude+'"]}';
	var json = JSON.parse(coords);
	
	var layer = L.marker(json.coordinates , {icon: L.mapquest.icons.marker({primaryColor: '#111111', secondaryColor: '#0066ff'})}).addTo(getMap())
	layer.bindPopup("Your location");
	
	getMap().panTo(json.coordinates);
}

function toureListLeftShift() {
	// Disables the button until the animation is done. Prevents confusing result if the button was clicked too often
	$("#leftArrow").unbind("click");
	
	$("#tourList").animate({ "margin-left": "+=170"}, "slow", function(){
		var x = $("#tourList").css("margin-left");
		var offSet = Number(x.substring(0, x.length - 2));
		
		// Checking if the offSet of the List is 0. This is the default situation. If the page loads the offset will be 0
		if(offSet >= 0){
			if($("#leftArrow").css("display") != "none"){
				$("#leftArrow").css("display", "none");
			}
		}
		$("#rightArrow").css("display", "block");
		
		// Rebinds the function to the button, so if it is clicked the next time  the animation will start again
		$("#leftArrow").bind("click", toureListLeftShift);
	});
}

function toureListRightShift() {
	
	// Disables the button until the animation is done. Prevents confusing result if the button was clicked too often
	$("#rightArrow").unbind("click");
	
	$("#tourList").animate({ "margin-left": "-=170"}, "slow", function(){
		var width;
		var toureListSize = ($("#tourList li").length - 1)  * 170;

		width = $("#tours").css("width");
		var viewPortSize = Number(width.substring(0, width.length - 2));
		
		width = $("#tourList").css("margin-left")
		var offSet = Number(width.substring(0, width.length - 2));
		
		//Offset is always negative
		//Checks if the totals size (pixel) of the list is smaller than the size of the viewport plus the offset. If that's the case, it indicates that the
		//end of the List was reached and disables the button.
		if(toureListSize <= viewPortSize - offSet){
			if($("#rightArrow").css("display") != "none"){
				$("#rightArrow").css("display", "none");
			}
		}
		$("#leftArrow").css("display", "block");
		
		// Rebinds the function to the button, so if it is clicked the next time  the animation will start again
		$("#rightArrow").bind("click", toureListRightShift);
	});
}

function addRoutesToSelection(data){
	var json = JSON.parse(data);
	
	// Clears the List
	$("#tourList").empty();
	
	for(var i = 0; i < json.length; i++) {
		var listelement = 
			"<li class='inline tourdata'>" +
				"<input type='hidden' class='startingPoint' value='{\"coordinates\": ["+json[i].firstLat+", "+json[i].firstLong+"]}'>" +
				"<input type='hidden' class='tourId' value='" +json[i].id+"'>" +
				"<p>"+json[i].name+"</p>" +
				"<div class='centered'>" +
					"<i class='material-icons " + (json[i].avgRating >= 1 ? "activeStar" : "") + "'>grade</i>" +
					"<i class='material-icons " + (json[i].avgRating >= 2 ? "activeStar" : "") + "'>grade</i>" +
					"<i class='material-icons " + (json[i].avgRating >= 3 ? "activeStar" : "") + "'>grade</i>" +
					"<i class='material-icons " + (json[i].avgRating >= 4 ? "activeStar" : "") + "'>grade</i>" +
					"<i class='material-icons " + (json[i].avgRating >= 5 ? "activeStar" : "") + "'>grade</i>" +
				"</div>" +
				"<div class='iconWrapper'>" +
					"<img class='tourIcon' src='"+ (json[i].images.length > 0 ? json[i].images[i] +"'" : "utilities/pic/OP2.jpg'") + ">" +
				"</div>" +
			"</li>";
		
		$("#tourList").append(listelement);
	}
	
	// Setting all Listeners
	toursSliderRight();
	toursClickEvent();
	toursHoverEvent();
	
	$("#tours").show();
}

//Checks if there are enough element that the right scoll is needed
function toursSliderRight(){
	width = $("#tours").css("width");
	var viewPortSize = Number(width.substring(0, width.length - 2));
	
	var toureListSize = ($("#tourList li").length - 1)  * 170;
	
	if (toureListSize <= viewPortSize){
		$("#rightArrow").css("display", "none");
	}
}

var permLayer; // Is used in multiple functions thats why it has to be global

//This function will set a point after clicking on a route. If there is another marker 
//it will be removed before the new marker is set
function toursClickEvent(){

	$(".tourdata").click(function(){
		var json = $(this).children(".startingPoint").val();
		var obj = JSON.parse(json);
		
		if(permLayer != null){getMap().removeLayer(permLayer)};
		
		getMap().panTo(obj.coordinates);
		permLayer = L.marker(obj.coordinates, {icon: L.mapquest.icons.marker({primaryColor: '#009933', secondaryColor: '#00cc00'})}).addTo(getMap());
		permLayer._icon.style.zIndex = 1000;
	});

}

// When hovering over a route
function toursHoverEvent(){
	var layer;
	$(".tourdata").mouseenter(function(){
		var json = $(this).children(".startingPoint").val();
		var obj = JSON.parse(json);
		
		var tour = $(this).children(".tourId").val();
		var tourObj;
		
		var ratingElement;
		
		$.each(globalRoutes, function(i, v){
			if(v.id == tour) {
				tourObj = v;
				return;
			}
		});
		//TODO Data must be loaded before the panel is shown
		
		$("#infoTourName").html(tourObj.name);
		$("#infoTourDescription").html(tourObj.description);
		$("#tourIdOnPanle").val(tourObj.id);
		
		ratingElement = "<i class='material-icons " + (tourObj.avgRating >= 1 ? "activeStar" : "") + "'>grade</i>" +
						"<i class='material-icons " + (tourObj.avgRating >= 2 ? "activeStar" : "") + "'>grade</i>" +
						"<i class='material-icons " + (tourObj.avgRating >= 3 ? "activeStar" : "") + "'>grade</i>" +
						"<i class='material-icons " + (tourObj.avgRating >= 4 ? "activeStar" : "") + "'>grade</i>" +
						"<i class='material-icons " + (tourObj.avgRating >= 5 ? "activeStar" : "") + "'>grade</i>"; 
		
		$("#infoTourRating").html(ratingElement);
		
		$("#tourStops").empty();
		
		$.each(globalRoutes, function(i, v) {
			if(v.id == tour) {
				$.each(v.stops, function(i, v) {
					$("#tourStops").append("<li class='infotext'>"+v.name+"</li><hr>");
				})
			}
		})
		
		
		
		$("#tourInfoPanel").css("display","block");
		
		layer = L.marker(obj.coordinates, {icon: L.mapquest.icons.marker({primaryColor: '#111111', secondaryColor: '#00cc00'})}).addTo(getMap());
		layer._icon.style.zIndex = 1001;
	});
	
	$(".tourdata").mouseleave(function(){
		//$("#tourInfoPanel").css("display", "none");
		getMap().removeLayer(layer);
	})
}

function calculateRoute() {
	var tour = $("#tourIdOnPanle").val();
	var tourObj;
	
	removeCurrentRoute();
	
	$.each(globalRoutes, function(i, v){
		if(v.id == tour) {
			tourObj = v;
			return;
		}
	});
	
	var routeObj = getRoutingJsonStructure();
	
	$.each(tourObj.stops, function(i,v) {
		var newElement = { type: "s", latLng: { lat: v.latitude, lng: v.longitude } };
		routeObj.locations.push(newElement)
	});
	
	routeObj.options = {routeType: "pedestrian"};
	
	L.mapquest.directions().route(routeObj);

}	

function removeCurrentRoute(){
	//removes the current route based on the draggable property 
	getMap().eachLayer(function (layer) {
		if(layer.options.draggable == true){
			getMap().removeLayer(layer);
		}
	});
}

function feedbackRoute(){
	var tour = $("#tourIdOnPanle").val();
	
	loadPopupContent("feedback");
	
	$("#ratingForm input[name=type]").val("route");
	$("#ratingForm input[name=id]").val(tour);

}

function feedbackLocation(markerId) {
	var location = globalLayer.getLayer(markerId);
	
	loadPopupContent("feedback");
	
	$("#ratingForm input[name=type]").val("location");
	$("#ratingForm input[name=id").val(location.info.id);
	
	$("#myRatingModal").show();
	
}
	
