function getLocation(searchString, map){
	
	// This function takes the input from the search field and sends it
	// the address api and sets the focus of the map to the most related 
	// result
	
	var url = 'http://www.mapquestapi.com/geocoding/v1/address';
	var data = {
			  "location": searchString,
			  "options": {
			    "thumbMaps": false,
			    "maxResults": 1
			  }
			};
	
	var key = 'y7O2leMmoJWVGxhiWASiuAOCqUjYrzd6';
	
	$.getJSON(url+"?key="+key, data, function(result, status){
		if (result.results.length==0){
			alert("Ihre Eingabe konnte keiner Adresse zugeordnet werden.")
		}
		map.panTo(result.results[0].locations[0].latLng);
		//map.setZoom(12);
	});
	
}

function getMarker(lng, lat, offsetX, offsetY){
	
}