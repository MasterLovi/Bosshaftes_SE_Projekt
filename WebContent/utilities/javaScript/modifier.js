//This function clears the searchbar
// Parameter inputId: Contains the ID of the element that should be cleared
function clearInput(inputId) {
	$("#"+inputId).val("");
}

function showNewPointPopup(marker) { 
	loadMarkerinfoToSubmitForm(marker);
	$("#myModal").css("display", "block");
}

function showUpdatePointPopup(markerId) { 
	console.log(markerId);
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




