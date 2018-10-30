$(document).ready(function(){
	$("#opParty").click(function(){
		$("#mapHeader").text("Party");
	})
})

$(document).ready(function(){
	$("#opCulture").click(function(){
		$("#mapHeader").text("Kultur");
	})
})

$(document).ready(function(){
	$("div.optionPanel").click(function(){
		$(this).children("div.optionDetails").stop().slideToggle('slow', function(){
			var text = $(this).parent('div.optionPanel').children('.optionHeader').children('.optionArrow').stop().text()
			$(this).parent('div.optionPanel').children('.optionHeader').children('.optionArrow').stop().text(text == "keyboard_arrow_up" ? "keyboard_arrow_down" : "keyboard_arrow_up");
		})
	})
})