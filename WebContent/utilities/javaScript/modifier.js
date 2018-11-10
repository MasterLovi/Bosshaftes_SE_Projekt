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

//This function clears the searchbar
// Parameter inputId: Contains the ID of the element that should be cleared
function clearInput(inputId){
	$("#"+inputId).val("");
}

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

$(document).ready(function(){
	$(".addLocation").click(function(){ 
		$("#myModal").css("display", "block");
	});
})

$(document).ready(function(){
	$(".close").click(function(){
		$("#myModal").css("display", "none");
	});
})

$(document).ready(function(){
	$(window).click(function(e){
		if (e.target.id == $("#myModal").attr("id")){
			$("#myModal").css("display", "none");
		}
	});
})

// When hovering over a 
$(document).ready(function(){
	var layer;
	$(".tourdata").mouseenter(function(){
		var json = $(this).children(".startingPoint").val();
		var obj = JSON.parse(json);
		
		// Todo Data must be loaded before the panel is shown
		
		$("#tourInfoPanel").css("display","block");
		
		layer = L.marker(obj.coordinates, {icon: L.mapquest.icons.marker({primaryColor: '#111111', secondaryColor: '#00cc00'})}).addTo(getMap());
	});
	
	$(".tourdata").mouseleave(function(){
		//$("#tourInfoPanel").css("display", "none");
		getMap().removeLayer(layer);
	})
})

var permLayer; // Is used in multiple functions thats why it has to be global

$(document).ready(function(){

	$(".tourdata").click(function(){
		var json = $(this).children(".startingPoint").val();
		var obj = JSON.parse(json);
		
		if(permLayer != null){getMap().removeLayer(permLayer)};
		
		getMap().panTo(obj.coordinates);
		permLayer = L.marker(obj.coordinates, {icon: L.mapquest.icons.marker({primaryColor: '#009933', secondaryColor: '#00cc00'})}).addTo(getMap());
	});

})

$(document).ready(function(){
	$("#closeTourInfo").click(function(){
		$("#tourInfoPanel").css("display", "none");
		$("#tours").css("display", "none");
		if(permLayer != null){getMap().removeLayer(permLayer)};
	})
})

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

function toureListLeftShift(){
	
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

function toureListRightShift(){
	
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



