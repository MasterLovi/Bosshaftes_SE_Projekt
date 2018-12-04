function loadPopupContent(popupType) {
	var content = "<span class=\"close\">&times;</span>";
	
	if (popupType == "") {
		return;
	}
	
	//loading the content of the popup
	switch (popupType) {
	case "feedback": {
		content = content + "<form id=\"feedbackForm\" method=\"POST\" action=\"\">" +
    	"<input type=\"hidden\" name=\"id\" value=\"\"/>" +
	    	"<input type=\"hidden\" name=\"type\" value=\"\"/>" +
			"<h4 class=\"centered\">Ihre Meinung ist uns wichitg!</h4>" +
			"<p id=\"popupError\"><p>" +
			"<p class=\"centered infoHeader\">Bewertung</p>" +
			"<div class=\"centered\" id=\"ratingForm\">" +
				"<i class=\"ratingStarR material-icons activeStar\" id=\"star1r\">grade</i>" +
				"<i class=\"ratingStarR material-icons activeStar\" id=\"star2r\">grade</i>" +
				"<i class=\"ratingStarR material-icons activeStar\" id=\"star3r\">grade</i>" +
				"<i class=\"ratingStarR material-icons\" id=\"star4r\">grade</i>" +
				"<i class=\"ratingStarR material-icons\" id=\"star5r\">grade</i>" +
			"</div>" +
			"<p class=\"centered infoHeader\">Kommentar</p>" +
			"<input type=\"hidden\" value=\"3\" name=\"rating\" id=\"ratingValueR\">" +
	    	"<textarea rows=\"4\" form=\"feedbackForm\" name=\"comment\"></textarea>" +
	    	"<div class=\"right\">" +
	    		"<input type=\"submit\" id=\"sendFeedback\" class=\"button\" name=\"confirm\" value=\"Senden\"/>" +
	    	"</div>" +
	    "</form>";
	};
	break;
	case "update": {
		content = content + "<h4 class=\"centered\">Ort ändern</h4>" +
		"<form id=\"updateLocationForm\" method=\"POST\" action=\"\">" +
    	"<input type=\"hidden\" name=\"id\" value=\"\"/>" +
    	"<p id=\"popupError\"></p>" +
	    	"<table id=\"popupTable\">" +
	    	"<tr>" +
		    	"<td>" +
		    		"<p>Angezeigter Name:</p>" +
		    	"</td>" +
	    		"<td>" +
	    			"<input type=\"text\" name=\"locationName\" value=\"\"/>" +
	    		"</td>" +
	    	"</tr>" +
	    	"<tr>" +
		    	"<td>" +
		    		"<p>Bild:</p>" +
		    	"</td>" +
	    		"<td>" +
	    			"<input type=\"file\" accept=\"image/jpeg, image/png, image/jpg\" name=\"picture\" value=\"\"/>" +
	    		"</td>" +
	    	"</tr>" +
	    	"<tr>" +
		    	"<td>" +
		    		"<p>Beschreibung:</p>" +
		    	"</td>" +
	    		"<td>" +
	    			"<textarea rows=\"7\" form=\"updateLocationForm\" name=\"description\"></textarea>" +
	    		"</td>" +
	    	"</tr>" +
	    	"<tr>" +
	    		"<td>" +
		    		"<p>Aufenthaltszeit:</p>" +
	    	"</td>" +
	    		"<td>" +
	    			"<input type=\"time\" name=\"time\" value=\"\"/>" +
	    		"</td>" +
	    	"</tr>" +
	    	"<tr>" +
	    		"<td>" +
	    			"<!-- Some button -->" +
	    		"</td>" +
	    		"<td>" +
	    			"<input type=\"submit\" name=\"confirm\" class=\"button\" value=\"Ändern\"/>" +
	    		"</td>" +
	    	"</tr>" +
	    	"</table>" +
	    "</form>";
	};
	break;
	case "createNew": {
		content = content + "<h4 class=\"centered\">Neuen Ort anlegen</h4>" +
		"<form id=\"createLocationForm\" method=\"POST\" action=\"\">" +
    	"<input type=\"hidden\" name=\"lat\" id=\"newLat\" value=\"\"/>" +
	    	"<input type=\"hidden\" name=\"lng\" id=\"newLng\" value=\"\"/>" +
	    	"<input type=\"hidden\" name=\"userId\" id=\"userId\" value=\"\"/>" +
	    	"<p id=\"popupError\"></p>" +
	    	"<table id=\"popupTable\">" +
	    	"<tr>" +
		    	"<td>" +
		    		"<p>Angezeigter Name:</p>" +
		    	"</td>" +
	    		"<td>" +
	    			"<input type=\"text\" name=\"locationName\" value=\"\"/>" +
	    		"</td>" +
	    	"</tr>" +
	    	"<tr>" +
		    	"<td>" +
		    		"<p>Bild:</p>" +
		    	"</td>" +
	    		"<td>" +
	    			"<input type=\"file\" accept=\"image/jpeg, image/png, image/jpg\" name=\"picture\" value=\"\"/>" +
	    		"</td>" +
	    	"</tr>" +
	    	"<tr>" +
		    	"<td>" +
		    		"<p>Beschreibung:</p>" +
		    	"</td>" +
	    		"<td>" +
	    			"<textarea rows=\"7\" form=\"createLocationForm\" name=\"description\"></textarea>" +
	    		"</td>" +
	    	"</tr>" +
	    	"<tr>" +
	    		"<td>" +
		    		"<p>Aufenthaltszeit:</p>" +
		    	"</td>" +
	    		"<td>" +
	    			"<input type=\"time\" name=\"time\" value=\"\"/>" +
	    		"</td>" +
	    	"</tr>" +
	    	"<tr>" +
	    		"<td>" +
	    			"<!-- Some button -->" +
	    		"</td>" +
	    		"<td>" +
	    			"<input type=\"submit\" name=\"confirm\" class=\"button\" value=\"Anlegen\"/>" +
	    		"</td>" +
	    	"</tr>" +
	    	"</table>" +
	    "</form>";
	};
	break;
	case "updateFromRoute": {
		content = content + "<form id=\"updateRouteForm\" method=\"POST\" action=\"\">" +
		"<input type=\"hidden\" name=\"locationId\" value=\"\"/>" +	
		"<h4 class=\"centered\">Zu Route hinzufügen</h4>" +
		"<p id=\"popupError\"></p>" +
		"<p class=\"centered infoHeader\">Route auswählen</p>" +
		"<select name=\"routes\">" +
		"</select>" +
		"<p class=\"centered infoHeader\">Beschreibung</p>" +
		"<ul id=\"tourStopsPopup\"></ul>" +
		"<input type=\"submit\" class=\"button\" value=\"Hinzufügen\">" +
		"</form>";
	};
	break;
	case "createRoute": {
		content = content + "<form id=\"newRouteForm\" method=\"POST\" action=\"\">" +
		"<input type=\"hidden\" name=\"locationId\" value=\"\">" +
		"<h4 class=\"centered\">Neue Route erstellen</h4>" +
		"<p id=\"popupError\"></p>" +
		"<p class=\"centered infoHeader\">Routenname</p>" +
		"<input type=\"text\" name=\"name\" placeholder=\"Routenname\">" +
		"<p class=\"centered infoHeader\">Beschreibung</p>" +
		"<textarea rows=\"4\" form=\"newRouteForm\" name=\"description\"></textarea>" +
		"<input type=\"submit\" class=\"button\" value=\"Erstellen\">" +
		"</form>";
	};
	break;
	case "showFeedback": {
		content = content + "<h4 class=\"centered\">Bewertungen</h4>" +
		"<p class=\"centered infoHeader\" id=\"feedbackHeaderTitle\"></p>" +
		"<ul id=\"feedbackList\"></ul>";
	};
	break;
	};
	
	$(".modal-content").html("");
	$(".modal-content").html(content);
		
	// ############ LOADING POPUP FUNCTIONALITY ###########
	// Load button functionality
	switch (popupType) {
	case "feedback": {
		$('#feedbackForm').submit(function () {
			var error = feedbackValidation();
			
			if(!error){
				sendFeedback($("#feedbackForm input[name=type]").val(), $("#feedbackForm input[name=id]").val());
				return false;
			} else {
				$("#popupError").html(error);
				return false;
			}
			
		});
		
		$(".ratingStarR").click(function(){

			var id = "";
			var starCount = 1;
			var on = true;
			var stars = ["star1r", "star2r", "star3r", "star4r", "star5r"];
			
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
			
			$("#ratingValueR").val(starCount);
			
		});
	};
	break;
	case "update": {
		if ($("#currentAction").val() == "Party") {
			$("#updateLocationForm input[name=time]").prop("disabled", true);
		}
		
		$('#updateLocationForm').submit(function () {
			var error = locationValidation($("#currentAction").val(),"update");
			
			var pImageLoaded = convertImageToBase64($("#updateLocationForm input[name=picture]"));
			
			if (!error){
				updateMarker($("#updateLocationForm input[name=id]").val(), pImageLoaded);
				return false;
			} else {
				$("#popupError").html(error);
				return false;
			}
		});
	};
	break;
	case "createNew": {
		if ($("#currentAction").val() == "Party") {
			$("#createLocationForm input[name=time]").prop("disabled", true);
		}
		
		$('#createLocationForm').submit(function () {
			var error = locationValidation($("#currentAction").val(),"createNew");
			
			var pImageLoaded = convertImageToBase64($("#createLocationForm input[name=picture]"));
			
			if (!error){
				createNewMarker($("#currentAction").val(), pImageLoaded);
				return false;
			} else {
				$("#popupError").html(error);
				return false;
			}
			
		});
	};
	break;
	case "updateFromRoute": {
		
		//This in only the add event. The remove event will be put in each removeable element.
		$('#updateRouteForm').submit(function () {
			var error = updateRouteValidation();
			
			if (!error) {
				addPointToRoute($("#updateRouteForm input[name=id]").val()); //TODO Create Function (markerId)
				return false;
			} else {
				$("#popupError").html(error);
				return false;
			}
		});
		
		$("#updateRouteForm select[name=routes]").change(function() {
			changeRouteInformation($("#updateRouteForm select[name=routes] option:selected").val());
		});
		
	};
	break;
	case "createRoute": {
		$('#newRouteForm').submit(function () {
			var error = newRouteValidation();
			
			if (!error) {
				createNewRoute($("#newRouteForm input[name=locationId]").val());
				return false;
			} else {
				$("#popupError").html(error);
				return false;
			}
		});
	};
	break;
	case "showFeedback": {
		
		//TODO Add Function to the delete button
	}
	break;
	};
	
	$(".close").click(function(){
		$("#myModal").css("display", "none");
	});
	
	$("#myModal").show();
}