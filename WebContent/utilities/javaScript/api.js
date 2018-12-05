function getAddress(lat, lng) {
		// Makes sure that everything will be loaded before the function is terminated 
		$.ajaxSetup({
			async: false
		});

		
		var returnVals = {
				cityName: "",
				country: "",
				postCode: "",
				streetName: ""
		}
		
		var url = 'http://www.mapquestapi.com/geocoding/v1/reverse';
		
		var key = 'y7O2leMmoJWVGxhiWASiuAOCqUjYrzd6';
		
		// Had to use the get api since the post is buggy
		$.getJSON(url+'?key='+key+'&location='+lat+'%2C'+lng+'&outFormat=json&thumbMaps=false', function(result, status){

			
			returnVals.cityName = result.results[0].locations[0].adminArea5;
			returnVals.country = result.results[0].locations[0].adminArea1;
			returnVals.postCode = result.results[0].locations[0].postalCode;
			returnVals.streetName = result.results[0].locations[0].street;
		});
		
		return returnVals;
}

function calculateTraveltime(tourObj){
	return new Promise(function(resolve, reject) {
		var time;
		var routeObj = getRoutingJsonStructure();
		var routeJsonString;
		
		$.each(tourObj.stops, function(i, v) {
			var newElement = {
				type : "s",
				latLng : {
					lat : v.latitude,
					lng : v.longitude
				}
			};
			routeObj.locations.push(newElement)
		});

		routeObj.options = {
			routeType : "pedestrian"
		};
		
		routeJsonString = JSON.stringify(routeObj);
		
		$.ajax({
			url: "http://www.mapquestapi.com/directions/v2/route?key=y7O2leMmoJWVGxhiWASiuAOCqUjYrzd6",
			type: "POST",
			data: routeJsonString,
			contentType: "application/json",
			success: function(response) {
				resolve(response.route.formattedTime);
			},
			error: function(error) {
				console.log(error);
			}
		});		
	});
	
	
}