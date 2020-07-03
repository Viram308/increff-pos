// get url
function getOrderApiUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getOrderList(){
	var url = getOrderApiUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   	// display list
	   	displayOrderList(data);  
	   },
	   error: handleAjaxError
	});
}

function deleteOrder(id){
	var url = getOrderApiUrl() + "/" + id;
	// call api
	$.ajax({
		url: url,
		type: 'DELETE',
		success: function(data) {
	   		// get list
	   		getOrderList();  
	   	},
	   	error: handleAjaxError
	   });
}


//UI DISPLAY METHODS

function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-outline-danger" onclick="deleteOrder(' + e.id + ')">Delete</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.datetime + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		$tbody.append(row);
	}
}



//INITIALIZATION CODE
function init(){
	$('#refresh-order-data').click(getOrderList);

}

$(document).ready(init);


