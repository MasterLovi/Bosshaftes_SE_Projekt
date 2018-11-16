
function getJsonDatastrucutreLocation() {
	var json = {
			id: "",
			name: "",
			location_type: "",
			time: {
				hours: "",
				minutes: "",
				seconds: "0"
			},
			address: {
				cityName: "",
				country: "",
				houseNumber: "",
				postCode: "",
				streetName: ""
			},
			coordinates: [0,0],
			feedback: {
				author: "",
				comment: "",
				rating: ""
			}
	};
	
	return json;
}

