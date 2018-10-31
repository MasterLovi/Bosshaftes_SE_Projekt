// This function changes the title of the page if the user changes from party to culture or the other way around 
$(document).ready(function(){
	$("#opParty").click(function(){
		$("#mapHeader").text("Party");
	})
})

// This function changes the title of the page if the user changes from party to culture or the other way around 
$(document).ready(function(){
	$("#opCulture").click(function(){
		$("#mapHeader").text("Kultur");
	})
})

//This function opens and closes all the option panels 
$(document).ready(function(){
	$("div.optionPanel").clearQueue().click(function(){
		$(this).stop().children("div.optionDetails").slideToggle('slow', function(){ // Callback function, so that the arrow is only changed after the animation is complete 
			var text = $(this).parent('div.optionPanel').children('.optionHeader').children('.optionArrow').text()
			$(this).parent('div.optionPanel').children('.optionHeader').children('.optionArrow').text(text == "keyboard_arrow_up" ? "keyboard_arrow_down" : "keyboard_arrow_up");
		})
	})
})