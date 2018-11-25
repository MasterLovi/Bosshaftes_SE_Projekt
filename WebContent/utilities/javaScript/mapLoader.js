var mymap;
var newMarker;
// Global map variable

$(document).ready(function() {
	L.mapquest.key = 'y7O2leMmoJWVGxhiWASiuAOCqUjYrzd6';

	 mymap= L.mapquest.map('map', {
		center: [49.4775206, 8.4219807],
		layers: L.mapquest.tileLayer('map'),
		zoom: 12
	});

	getLocationFromDatabase($("#currentAction").val());

//	----- Setting the Map Listener for new Points

	mymap.on('click', function(e){

		//Remove the last set marker if it was not safed 
		if(newMarker != null){mymap.removeLayer(newMarker)};

		// Creates a new marker and gives it a popup
		newMarker = new L.marker(e.latlng, {icon: L.mapquest.icons.marker({primaryColor: '#111111', secondaryColor: '#00cc00'})}).addTo(mymap)
		.bindPopup("<p>Kennen Sie diesen Ort?</p><button class=\"centered\" onClick='showNewPointPopup(newMarker)'>Hinzufügen</button>").openPopup()
		.on('click', function(e){
			// This has to be checked since the marker will be set to null if it is added to the map.
			if(newMarker != null){
				mymap.removeLayer(newMarker);
			}
		});


		// Adds a layer so it can be removed later on
		mymap.addLayer(newMarker);
	});
})

// ----- Setting the getMap function so myMap can be used globaly	
	
function getMap(){
	return mymap;
}
