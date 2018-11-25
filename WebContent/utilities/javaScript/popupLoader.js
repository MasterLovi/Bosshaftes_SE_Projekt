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
	    		"<input type=\"submit\" id=\"sendFeedback\" name=\"confirm\" value=\"Senden\"/>" +
	    	"</div>" +
	    "</form>";
	};
	break;
	case "update": {
		content = content +"<form id=\"updateLocationForm\" method=\"POST\" action=\"\">" +
    	"<input type=\"hidden\" name=\"id\" value=\"\"/>" +
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
	    			"<input type=\"file\" name=\"picture\" value=\"\"/>" +
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
	    			"<input type=\"submit\" name=\"confirm\" value=\"Anlegen\"/>" +
	    		"</td>" +
	    	"</tr>" +
	    	"</table>" +
	    "</form>";
	};
	break;
	case "createNew": {
		content = content + "<form id=\"createLocationForm\" method=\"POST\" action=\"\">" +
    	"<input type=\"hidden\" name=\"lat\" id=\"newLat\" value=\"\"/>" +
	    	"<input type=\"hidden\" name=\"lng\" id=\"newLng\" value=\"\"/>" +
	    	"<input type=\"hidden\" name=\"userId\" id=\"userId\" value=\"\"/>" +
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
	    			"<input type=\"file\" name=\"picture\" value=\"\"/>" +
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
	    			"<input type=\"submit\" name=\"confirm\" value=\"Anlegen\"/>" +
	    		"</td>" +
	    	"</tr>" +
	    	"</table>" +
	    "</form>";
	};
	break;
	case "addToRoute": {
		content = content + "<form id=\"addToRouteForm\" method=\"POST\" action=\"\">" +
		"<input type=\"hidden\" name=\"id\" value=\"\"/>" +
		"<h4 class=\"centered\">Zu Route hinzufügen</h4>" +
		"<p class=\"centered infoHeader\">Route auswählen</p>" +
		"<select name=\"routes\">" +
		"</select>" +
		"<p class=\"centered infoHeader\">Beschreibung</p>" +
		"<ul id=\"tourStops\"></ul>" +
		"<input type=\"submit\" value=\"Hinzufügen\">" +
		"</form>";
	};
	break;
	case "createRoute": {
		content = content + "<form id=\"newRouteForm\" method=\"POST\" action=\"\">" +
		"<h4 class=\"centered\">Neue Route erstellen</h4>" +
		"<p class=\"centered infoHeader\">Routenname</p>" +
		"<input type=\"text\" name=\"name\" placeholder=\"Routenname\">" +
		"<p class=\"centered infoHeader\">Beschreibung</p>" +
		"<textarea rows=\"4\" form=\"feedbackForm\" name=\"description\"></textarea>" +
		"<input type=\"submit\" value=\"Erstellen\">" +
		"</form>";
	};
	break;
	case "showFeedback": {
		content = content + "<h4 class=\"centered\">Feedbacks</h4>" +
		"<p class=\"centered infoHeader\">Route-Title</p>" +
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
			sendFeedback($("#ratingForm input[name=type]").val(), $("#ratingForm input[name=id]").val()); //TODO Load right category
			return false;
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
		$('#updateLocationForm').submit(function () {
			updateMarker($("#updateLocationForm input[name=id]").val());
			return false;
		});
	};
	break;
	case "createNew": {
		$('#createLocationForm').submit(function () {
			createNewMarker($("#currentAction").val()); //TODO Load right category
			return false;
		});
	};
	break;
	case "addToRoute": {
		$('#newRouteForm').submit(function () {
			addToRoute($("#updateLocationForm input[name=id]").val()); //TODO Create Function (markerId)
			return false;
		});
	};
	break;
	case "createRoute": {
		$('#newRouteForm').submit(function () {
			createRoute(); //TODO Create Function
			return false;
		});
	};
	break;
	case "showFeedback": {
		loadFeedbackToPopup(); //Parameter 'type' & 'id' needed
		//TODO Whatever has to be done here
	}
	};
	
	$(".close").click(function(){
		$("#myModal").css("display", "none");
	});
	
	$("#myModal").show();
}