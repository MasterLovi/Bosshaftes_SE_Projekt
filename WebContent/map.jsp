<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%! 
	// All variables that we need to use all over the page have to be declared in this section so they have a global scope.
	public String title;
	public String partyTag;
	public String cultureTag;
%>

<%
	// Checking if the get parameter 'type' was set
	if (request.getParameter("type") != null){
		
		// Checking if the get parameter is set to 'party'. If so set all variables to party
		if(request.getParameter("type").equals("party")){
			title = "Party";
			partyTag = "checked";
			cultureTag = "";
			
		// Checking if the get parmater is set to 'culture' If so  set all variables to culture
		} else if (request.getParameter("type").equals("culture")){
			title = "Kultur";
			partyTag = "";
			cultureTag =  "checked";
		
		// If the get parameter is neither 'party' nor 'culture' set all the variables to default (party)
		} else {
			title = "Party";
			partyTag = "checked";
			cultureTag = "";
		}
		
	}
%>

<html>
	<head>
		<meta charset="ISO-8859-1">
		
        <link rel="stylesheet" type="text/css" href="stylesheets/design.css">
        <link rel="stylesheet" type="text/css" href="stylesheets/positioning.css">
        <link rel="stylesheet" type="text/css" href="stylesheets/icon.css">
        
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.4/dist/leaflet.css"
 		integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
   		crossorigin=""/>
    	
    	<script src="utilities/javaScript/jquery-3.3.1.min.js"></script>
    	<script src="utilities/javaScript/modifier.js"></script>
   	    <script src="utilities/javaScript/mapLoader.js"></script>
    	
		<title>Map Demo</title>
	</head>
	<body>
		<div id='headerbar'>
    		<div id='logoWrapper'>
	        	<a href='index.jsp'><img id='mainLogo' src='utilities\pic\testLogo.jpg'></a>
	        </div>
	        <div id='loginArea'>
	        <% 
					if (session.getAttribute("loggedin") != null){
		   				%>
		   					<jsp:include page="utilities\loggedIn.jsp"></jsp:include>
	   					<%
						
					} else {
						
						%>
							<jsp:include page="utilities\logIn.jsp"></jsp:include>
						<%
					}
				
			%>	
			</div>
	        <div id='otherMapHeader'>
	    		<h2 id='mapHeader'><% out.print(title); %></h2>
	    	</div>
	    	
    	</div>
    	<div id='mapWrapper'>
	    	<div id='demoMap'>
	    	
	    	</div>
    	</div>
    	<div class='optionPanel'>
    		<p class='optionHeader'><i class="optionArrow floatLeft material-icons">keyboard_arrow_down </i>This is were the tour details will be shown</p>
    		<div class='optionDetails'>
    			<p>blablablablablablablablablablablablablablablablablablablablabla</p>
    			<p>blablablablablablablablablablablablablablablablablablablablabla</p>
    			<p>blablablablablablablablablablablablablablablablablablablablabla</p>
    		</div>
    	</div>
    	
    	
    	<div class='optionPanel'>
    		<p class='optionHeader'><i class="optionArrow floatLeft material-icons">keyboard_arrow_down </i>This is were the tour details will be shown</p>
    		<div class='optionDetails'>
    			<p>blablablablablablablablablablablablablablablablablablablablabla</p>
    			<p>blablablablablablablablablablablablablablablablablablablablabla</p>
    			<p>blablablablablablablablablablablablablablablablablablablablabla</p>
    		</div>
    	</div>
    	<div id='optionPane'>
    		<h3>Optionen</h3>
    		<form action='#'>
    			<input type="radio" id="opParty" name="searchType" value="party" <%= partyTag %>>
    			<lable for="opParty">Party</lable>
    			<br>
    			<input type="radio" id="opCulture" name="searchType" value="culture" <%= cultureTag %>>
    			<lable for="opCulture">Kultur</lable>
    		<hr>
    		<input type="checkbox" name="demo1" value="demo1">Option1<br>
    		<input type="checkbox" name="demo2" value="demo2">Option2<br>
    		<input type="checkbox" name="demo3" value="demo3">Option3<br>
    		<input type="checkbox" name="demo4" value="demo4">Option4<br>
    		<hr>
    		<p>Anzahl Sehenwürdigkeiten</p>
    		<input type="range" name="demo4" value="demo4"><br>
    		<hr>
    		<p>Zeit</p>
    		<input type="range" name="demo4" value="demo4"><br>
    		<hr>
    		<p>Tours</p>
    		<select>
			  <option value="Tour1">Tour1</option>
			  <option value="Tour2">Tour2</option>
			  <option value="Tour3">Tour3</option>
			  <option value="Tour4">Tour4</option>
			</select>
			<br>
			<input type="submit" value="Suchen">
    		</form>
    	</div>
    	
   	    <%@ include file="/utilities/footer.jsp" %>
  	 	<script src="https://unpkg.com/leaflet@1.3.4/dist/leaflet.js"
  		integrity="sha512-nMMmRyTVoLYqjP9hrbed9S+FzjZHW5gY1TWCHA5ckwXZBadntCNs8kEqAWdrb9O7rxbCaA4lKTIWjDXZxflOcA=="
   		crossorigin="">
  	 	</script>
  	 	
  	 	<script>
  	 		var mymap = L.map('demoMap').setView([49.47, 8.42], 13);
  	 		
  	 		L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
  	 		    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
  	 		    maxZoom: 18,
  	 		    id: 'mapbox.streets',
  	 		    accessToken: 'pk.eyJ1IjoiYW1ubmV5IiwiYSI6ImNqbmVjeDdnZDA2dGYzcm1pYjZienZyMDgifQ.hVVhNmhJ6sM2kUKlSVPN5Q'
  	 		}).addTo(mymap);
  	 		
  	 		var hs = loadJson();
  	 		L.geoJSON(hs).addTo(mymap).bindPopup("Tracked based on IP-Address");
  	 		
  	 	</script>
	</body>
</html>
