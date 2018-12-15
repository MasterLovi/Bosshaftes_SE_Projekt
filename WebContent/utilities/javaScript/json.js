/**
 * Returns the valid json structure of the specific action
 * @param type {String} - Contains the name of the action that should be executed
 * @returns {Object} - Required Structure
 */
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
		break;
	case "report":
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
		break;
	}

	return json;
}

/**
 * Returns the valid json structure of the specific action
 * @returns {Object} - Required Structure
 */
function getRoutingJsonStructure(){
	var json = { locations: [] };

	return json;
}

/**
 * Returns the valid json structure of the specific action
 * @returns {Object} - Required Structure
 */
function getDatastructureRoute() {
	var json = {  
			id: "",
			name: "",
			type: "",
			time:{  
				time: "",
			},
			description: "",
			images:[],
			feedback:[],
			avgRating: "",
			stops:[], // Will be filled with location objects
			numberOfStops: "",
			firstLong: "",
			firstLat: "",
			owner:{  
				id: "",
				username: "",
				email: ""
			}
	}

	return json;
}

/**
 * Returns the valid json structure of the specific action
 * @returns {Object} - Required Structure
 */
function getDatastructureFeedback() {
	var json = {
			comment: "",
			rating: "",
	}

	return json;
}
