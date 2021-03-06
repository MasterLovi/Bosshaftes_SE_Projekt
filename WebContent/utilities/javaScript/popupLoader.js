/**
 * Loads the popup content and enables the buttons 
 * @param popupType {String} - Contains the name of the popup that should be loaded
 * @returns
 */
function loadPopupContent(popupType) {
	var content = "<span class=\"close\">&times;</span>";
	
	if (popupType == "") {
		return;
	}
	
	$(".modal-content").html("");
	
	//loading the content of the popup
	switch (popupType) {
	case "feedback": {
		content = content + "<form autocomplete=\"off\" id=\"feedbackForm\" method=\"POST\" action=\"\">" +
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
		"<form autocomplete=\"off\" id=\"updateLocationForm\" method=\"POST\" action=\"\">" +
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
		"<form autocomplete=\"off\" id=\"createLocationForm\" method=\"POST\" action=\"\">" +
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
		"<select id=\"routeSelect\" name=\"routes\">" +
		"</select>" +
		"<p class=\"centered infoHeader\">Beschreibung</p>" +
		"<ul id=\"tourStopsPopup\"></ul>" +
		"<input type=\"submit\" class=\"button buttonStandardSize\" value=\"Hinzufügen\">" +
		"</form>";
	};
	break;
	case "createRoute": {
		content = content + "<form autocomplete=\"off\" id=\"newRouteForm\" method=\"POST\" action=\"\">" +
		"<input type=\"hidden\" name=\"locationId\" value=\"\">" +
		"<h4 class=\"centered\">Neue Route erstellen</h4>" +
		"<p id=\"popupError\"></p>" +
		"<p class=\"centered infoHeader\">Routenname</p>" +
		"<input type=\"text\" class=\"standardInputSize\" name=\"name\" placeholder=\"Routenname\">" +
		"<p class=\"centered infoHeader\">Bild</p>" +
		"<input type=\"file\" class=\"imageSelectorRoute\" accept=\"image/jpeg, image/png, image/jpg\" name=\"tourImage\"><br>" +
		"<p class=\"centered infoHeader\">Beschreibung</p>" +
		"<textarea rows=\"4\" form=\"newRouteForm\" name=\"description\"></textarea>" +
		"<input type=\"submit\" class=\"button buttonStandardSize\" value=\"Erstellen\">" +
		"</form>";
	};
	break;
	case "showFeedback": {
		content = content + "<h4 class=\"centered\">Bewertungen</h4>" +
		"<input type=\"hidden\" id=\"typeOfShownFeedback\" value=\"\">" +
		"<input type=\"hidden\" id=\"idOfShownFeedback\" value=\"\">" +
		"<p class=\"centered infoHeader\" id=\"feedbackHeaderTitle\"></p>" +
		"<ul id=\"feedbackList\"></ul>";
	};
	break;
	case "manageRoutes": {
		content = content + "<form id=\"manageRouteForm\" method=\"POST\" action=\"\">" +
		"<h4 class=\"centered\">Ihre Routen</h4>" +
		"<p id=\"popupError\"></p>" +
		"<p class=\"centered infoHeader\">Route auswählen</p>" +
		"<select id=\"routeSelect\" name=\"routes\">" +
		"</select>" +
		"<div id=\"editNameWrapper\"><i class=\"material-icons\" id=\"editRouteName\">edit</i></div>" +
		"<p class=\"centered infoHeader\">Beschreibung</p>" +
		"<p class=\"infoText\" id=\"userRouteDescription\"></p>"+
		"<div id=\"editDescriptionWrapper\"><i class=\"material-icons\" id=\"editRouteDescription\">edit</i></div>" +
		"<p class=\"centered infoHeader\">Spots</p>" +
		"<ul id=\"tourStopsPopup\"></ul>" +
		"<p class=\"centered infoHeader\">Bild ändern</p>" +
		"<input type=\"file\" class=\"imageSelectorRoute\" accept=\"image/jpeg, image/png, image/jpg\" name=\"tourImage\"><button type=\"button\" id=\"uploadNewRouteImage\" class=\"button buttonStandardSize floatRight\">Hochladen</button><br>" +
		"<input type=\"submit\" class=\"button buttonStandardSize\" value=\"Anzeigen\">" +
		"<button type=\"button\" id=\"deleteRouteBtn\" class=\"button buttonStandardSize\">Löschen</button>" + //type has to be set so that the button does not submit the form
		"</form>";
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
				if (isLocationInRoute($("#updateRouteForm input[name=locationId]").val(), $("#updateRouteForm select[name=routes]").val())) {
					$("#popupError").html("Dieser Ort ist bereits in der Route");
					return false;
				}
				addPointToRoute($("#updateRouteForm input[name=locationId]").val(), $("#updateRouteForm select[name=routes]").val()); 
				unloadPopup();
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
			var pImageLoaded;
			
			if (!error) {
				pImageLoaded = convertImageToBase64($("#newRouteForm input[name=tourImage]"));
				createNewRoute($("#newRouteForm input[name=locationId]").val(), pImageLoaded);
				return false;
			} else {
				$("#popupError").html(error);
				return false;
			}
		});
	};
	break;
	case "showFeedback": {
		
		// No functionality needed
	};
	break;
	case "manageRoutes": {		
		
		$("#manageRouteForm").submit(function() {
			var error = manageRouteValidation();
			if (!error) {
				showUserRouteOnInfo($("#manageRouteForm select[name=routes]").val());
				return false;
			} else {
				$("#popupError").html(error);
				return false;
			}
		});

		$("#deleteRouteBtn").click(function() {
			var error = manageRouteValidation();
			if (!error) {
				deleteRoute($("#manageRouteForm select[name=routes]").val());
			} else {
				$("#popupError").html(error);
				return false;
			}
		});
		
		$("#uploadNewRouteImage").click(function() {
			if (!$("#manageRouteForm input[name=tourImage]").prop("files")[0]) {
				$("#popupError").html("Sie haben keine Datei ausgewählt");
				return false;
			}
			
			var pImageLoaded = convertImageToBase64($("#manageRouteForm input[name=tourImage]"));
			
			updateRouteImage(pImageLoaded);
		});
		
		$("#manageRouteForm select[name=routes]").change(function() {
			changeRouteInformation($("#manageRouteForm select[name=routes] option:selected").val());
		});
		
		$("#editRouteName").click(function(){
			confirmationNameChange($("#manageRouteForm select[name=routes]").val());
		});
		
		$("#editRouteDescription").click(function(){
			editRouteDescription($("#manageRouteForm select[name=routes]").val());
		});
		
	};
	break;
	};
	
	$(".close").click(function(){
		$("#myModal").css("display", "none");
	});
	
	$("#myModal").show();
}