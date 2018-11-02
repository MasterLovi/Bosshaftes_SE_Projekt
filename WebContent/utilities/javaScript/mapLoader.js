var file;
//This file will load a json file with the preloaded markers.
function loadJson(){
	
	$.ajax( 'utilities/json/Testdata.json', {
		  type: 'GET',
		  dataType: 'json',
		  async: false,
		  success: function( data ) {
		    file = data;
		  },
		  error: function( req, status, err ) {
		    console.log( 'something went wrong', status, err );
		  }
	});
//	console.log(file);
	return file;
}
	