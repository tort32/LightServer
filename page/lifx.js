function discover() {
	$.get("/api/lifx/discover", function(json) {
		$('#discover_response').text(JSON.stringify(json));
    });
};

function getState() {
	$.ajax({
		type: "POST",
		url: "/api/lifx/state",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: $("#state_request").val(),
		success: function(json) {
			$('#state_response').text(JSON.stringify(json));
		}
    });
};