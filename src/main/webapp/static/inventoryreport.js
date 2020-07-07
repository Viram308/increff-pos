// get url
function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/admin/inventoryreport";
}

function getInventoryReportData(){
	var url = getInventoryReportUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',	   
		success: function(response) {
	   		// display report
	   		displayInventoryReport(response);
	   	},
	   	error: handleAjaxError
	   });

	return false;
}
// display data
function displayInventoryReport(data){
	var $tbody = $('#inventoryreport-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '</tr>';
		$tbody.append(row);
		j++;
	}
}
function searchInventoryReport(){
	//Set the values to add
	var $tbody = $('#inventoryreport-table').find('tbody');
	$tbody.empty();
	var brand=$('#inputBrand').val().trim();
	var category=$('#inputCategory').val().trim();
		
	if(brand=="" && category==""){
		alert('Enter brand or category');
		return false;
	}

	
	var $form = $("#inventoryreport-form");
	var json = toJson($form);
	var url = getInventoryReportUrl()+"/search";
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
	   		displayInventoryReport(response);  
	   	},
	   	error: handleAjaxError
	   });

	return false;
}

//INITIALIZATION CODE
function init(){
$('#search-inventoryreport').click(searchInventoryReport);
}
$(document).ready(init);
