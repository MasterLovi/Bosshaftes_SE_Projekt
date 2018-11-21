
function getJsonDatastrucutreLocation() {
	var json = {
			id: "",
			name: "",
			type: "",
			time: {
				time: "",
				hours: 0,
				minutes: 0,
				seconds: 0
			},
			timesReported: 0,
			address: {
				id: "",
				cityName: "",
				country: "",
				houseNumber: "",
				postCode: "",
				streetName: ""
			},
			latitude: "",
			longitude: "",
			feedback: [],
			description: ""
	};
	
	return json;
}

