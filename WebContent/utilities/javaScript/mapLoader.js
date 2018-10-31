function loadJson(){
	var test = {
		"type": "Feature",
	    "properties": {
	        "name": "Hochschule Ludwigshafen",
	        "popupContent": "Based on your IP-Address"
	    },
	    "geometry": {
	        "type": "Point",
	        "coordinates": [8.4219807, 49.4775206]
	    }
	};
//	$.getJSON("utilities/json/Testdata.json", function(data) {
//		test = data;
//	});
//	console.log(JSON.stringify(test));
	return test;
}
