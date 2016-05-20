
$(function() {

	$("#translateBtn").click(function() {
		
		var textToTrans = $("#txtToTranslate").val();
		
		$.ajax({
			  method: "POST",
			  url: "SimpleServlet",
			  data: { txtToTranslate: textToTrans}
			})
			  .done(function( serverResponse ) {
			    console.log(serverResponse);
				var obj = jQuery.parseJSON(serverResponse);
				var results = obj.results;

				$("#txtTranslated").val(results.translated);
				$("#classificationsData").val(results.topClassification);
			  });
	});
	
	
});

$("textarea#txtToTranslate").keyup(function() {
	var val = $(this).val();
	console.log("val=" + val);
	if (val.length > 0 ) {
		$("#translateBtn").removeAttr('disabled'); 
	} else {
		$("#translateBtn").attr('disabled','disabled');
	}
});


