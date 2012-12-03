$(document).ready(function(){
	$("#userInput").bind("focus", function(){
		$(this).val("");
		$(this).unbind("focus");
	});
	
	$("#submitInput").bind("click", function(){
		var input = $("#userInput").val();
		$.ajax({
			async: false,
			url: "record",
			type: "POST",
			data: {text : input},
			success : function(data){
				$("#message").html(data);
			}
		});
	});
});