function feedbackValidation() {
	var comment = $("#feedbackForm textarea[name=comment]").val(); 
	var error;
	
	if (comment == "") {
		error = "Bitte geben Sie einen kurzen Kommentar ab."
	}
	
	if (error != ""){
		return error;
	} else {
		return false;
	}
}

function locationValidation(type, operation) {
	var name;
	var description;
	var time;
	var error = "";
	
	switch (operation) {
	case "update": {
		name = $("#updateLocationForm input[name=locationName]").val();
		description = $("#updateLocationForm textarea[name=description]").val();
		time = $("#updateLocationForm input[name=time]").val();
		
		if (name == "") {
			error = error + "<span>Sie können den Namen eines Ortes nicht löschen.</span> <br>";
		}
		
		if (description == "") {
			error = error + "<span>Sie können die Beschreibungs eines Ortes nicht löschen.</span> <br>";
		}
		
		if (time == "00:00:00") {
			if (type == "Kultur") {
				error = error + "<span>Die Zeit einer Sehenwürdigkeit darf nicht 0 sein.</span>";
			}
		}
	};
	break;
	case "createNew": {
		name = $("#createLocationForm input[name=locationName]").val();
		description = $("#createLocationForm textarea[name=description]").val();
		time = $("#createLocationForm input[name=time]").val();
		
		if (name == "") {
			error = error + "<span>Bitte geben Sie einen Namen für den Ort an.</span> <br>";
		}
		
		if (description == "") {
			error = error + "<span>Bitte geben Sie eine kurze Beschreibung ein.</span> <br>";
		}
		
		if (time == "") {
			if (type == "Kultur") {
				error = error + "<span>Die Zeit einer Sehenwürdigkeit darf nicht 0 sein</span>";
			}
		}
	};
	break;	
	};
	
	if (error != "") {
		return error;
	} else {
		return false;
	}
}

function newRouteValidation() {
	var name;
	var description;
	var error = "";
	
	name = $("#newRouteForm input[name=name]").val();
	description = $("#newRouteForm textarea[name=description]").val();
	
	if (name == "") {
		error = error + "<span>Bitte geben Sie einen Namen für die Route ein.</span> <br>";
	}
	
	if (description == "") {
		error = error + "<span>Bitte geben Sie eine Beschreibung der Route ein.</span>";
	}
	
	if (error != "") {
		return error;
	} else {
		return false;
	}
}

function updateRouteValidation() {
	var selection = "";
	var error;
	
	selection = $("#updateRouteForm select[name=routes] option:selected").val();
	
	if (selection == undefined) {
		error = "Bitte wählen Sie eine Route aus zu der hinzugefügt werden soll."
	}
	
	if (error != "") {
		return error;
	} else {
		return false;
	}
}

function manageRouteValidation() {
	var selection = "";
	var error;
	
	selection = $("#manageRouteForm select[name=routes]").val();
	
	if (selection == undefined) {
		error = "Sie haben keine Route ausgewählt.";
	}
	
	if (error != "") {
		return error;
	} else {
		return false;
	}
}