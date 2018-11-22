function getJsonDatastrucutreLocation(type) {
	var json;
	switch (type) {
	case "create":
		json = {
			name : "",
			type : "",
			time : {
				time: ""
			},
			timesReported : 0,
			address : {
				cityName : "",
				country : "",
				postCode : "",
				streetName : ""
			},
			latitude : "",
			longitude : "",
			feedback : [],
			description : "",
			images : [null]
		};
		break;
	case "update":
		json = {
			id : "",
			name : "",
			type : "",
			time : {
				time: ""
			},
			timesReported : 0,
			address : {
				id : "",
				cityName : "",
				country : "",
				houseNumber : "",
				postCode : "",
				streetName : ""
			},
			latitude : "",
			longitude : "",
			feedback : [],
			description : "",
			images : [null]
		};
	}

	return json;
}
