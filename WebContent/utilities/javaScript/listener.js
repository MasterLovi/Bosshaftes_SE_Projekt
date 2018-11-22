// This function changes the title of the page if the user changes from party to culture or the other way around 
$(document).ready(function(){
	$("#opParty").click(function(){
		$("#mapHeader").text("Party");
	})
})

// This function changes the title of the page if the user changes from party to culture or the other way around 
$(document).ready(function(){
	$("#opCulture").click(function(){
		$("#mapHeader").text("Kultur");
	})
})

//This function opens and closes all the option panels 
$(document).ready(function(){
	$("div.optionPanel").clearQueue().click(function(){
		$(this).stop().children("div.optionDetails").slideToggle('slow', function(){ // Callback function, so that the arrow is only changed after the animation is complete 
			var text = $(this).parent('div.optionPanel').children('.optionHeader').children('.optionArrow').text()
			$(this).parent('div.optionPanel').children('.optionHeader').children('.optionArrow').text(text == "keyboard_arrow_up" ? "keyboard_arrow_down" : "keyboard_arrow_up");
		})
	})
})

//This function is called when a user clicks enter while editing the searchbar.
$(document).ready(function(){
	$("#input-search").keypress(function(e){
		if (e.which == 13){
			getLocation($("#input-search").val(), getMap());
		}
	})
})

//This function changes the color of the rating stars based on the star the user clicked on.
$(document).ready(function(){
	$(".ratingStar").click(function(){

	var id = "";
	var starCount = 1;
	var on = true;
	var stars = ["star1", "star2", "star3", "star4", "star5"];
	
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
	
	$("#ratingValue").val(starCount);
	
	})
})

// Links the spot slider and value box
$(document).ready(function(){
	$("#spotRange").change(function(){
		$("#spotValue").val($(this).val());
	})
})

// Links the time slider and value box
$(document).ready(function(){
	$("#timeRange").change(function(){
		$("#timeValue").val($(this).val());
	})
})

// Links the spot slider and value box
$(document).ready(function(){
	$("#spotValue").change(function(){
		$("#spotRange").val($(this).val());
	})
})

// Links the time slider and value box
$(document).ready(function(){
	$("#timeValue").change(function(){
		$("#timeRange").val($(this).val());
	})
})

// This function bind the shiftfunction to the tour slider
$(document).ready(function(){
	$("#leftArrow").bind("click", toureListLeftShift);
	$("#rightArrow").bind("click", toureListRightShift);
})

// This function will be called when the x on the modular window is clicked
// This will also close all open popups. Avoids stacking
$(document).ready(function(){
	$(".close").click(function(){
		$("#myModal").css("display", "none");
		$("#myUpdateModal").css("display", "none");
	});
})

// This window event makes sure that if the user clicks somewhere that is not the popup, it will close
$(document).ready(function(){
	$(window).click(function(e){
		if (e.target.id == $("#myModal").attr("id")){
			$("#myModal").css("display", "none");
		}
		
		if (e.target.id == $("#myUpdateModal").attr("id")) {
			$("#myUpdateModal").css("display", "none");
		}
	});
})

// Closes the Tour info and removes any set markers 
$(document).ready(function(){
	$("#closeTourInfo").click(function(){
		$("#tourInfoPanel").css("display", "none");
		$("#tours").css("display", "none");
		if(permLayer != null){getMap().removeLayer(permLayer)};
	})
})

// Call the create request for a new marker 
$(document).ready(function() {
	$('#createLocationForm').submit(function () {
		createNewMarker("Party"); //TODO Load right category
		return false;
	});
})

// Call the create request for a new marker 
$(document).ready(function() {
	$('#updateLocationForm').submit(function () {
		updateMarker($("#updateLocationForm input[name=id").val()); //TODO Load right category
		return false;
	});
})


// Is called when the bounds of the map changes 
$(document).ready(function() {
	getMap().on("moveend", function(e) {
		getLocationFromDatabase("Party"); //TODO Load right category
	});
})