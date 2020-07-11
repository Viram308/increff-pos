//  get url
function getInitUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/site/init";
}

function getLoginUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/site/login";
}

function initApp(){
	//Set the values to add
	var $form = $("#init-form");
	var json = toJson($form);
	var url = getInitUrl();
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
			 location.href = response;
	   	},
	   	error: handleAjaxError
	   });

	return false;
}