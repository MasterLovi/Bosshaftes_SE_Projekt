function getAddress(lng, lat) {
	
	var returnVals = {
			cityName: "",
			country: "",
			houseNumber: "",
			postCode: "",
			streetName: ""
	}
	
	var url = 'http://www.mapquestapi.com/geocoding/v1/reverse';
	
	var data = {
			  "location": { 
				  "latLng": {
					  "lat": lat,
					  "lng": lng
				  },
			  },
			  "options": {
			    "thumbMaps": false,
			    "maxResults": 1
			  }
			};
	
	var key = 'y7O2leMmoJWVGxhiWASiuAOCqUjYrzd6';
	
	$.getJSON(url+"?key="+key, data, function(result, status){
		returnVals.cityName = result.results[0].loacations[0].adminArea5;
		returnVals.country = result.results[0].loacations[0].adminArea1;
		returnVals.houseNumber = "";
		returnVals.postCode = result.results[0].loacations[0].postalCode;
		returnVals.streetName = result.results[0].loacations[0].street;
	});
	
	return returnVals;
}