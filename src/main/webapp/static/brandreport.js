// get url
function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/admin/brandreport";
}

function getBrandReportData(){
	var url = getBrandReportUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',	   
		success: function(response) {
	   	// show report
	   	displayBrandReport(response);
	   },
	   error: handleAjaxError
	});

	return false;
}
// display method
function displayBrandReport(data){
	var $tbody = $('#brandreport-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '</tr>';
		$tbody.append(row);
		j++;
	}
}

function searchBrandReport(){
	//Set the values to add
	var $tbody = $('#brandreport-table').find('tbody');
	$tbody.empty();
	var brand=$('#inputBrand').val().trim();
	var category=$('#inputCategory').val().trim();
		
	if(brand=="" && category==""){
		alert('Enter brand or category');
		return false;
	}
	var $form = $("#brandreport-form");
	var json = toJson($form);
	var url = getBrandReportUrl()+"/search";
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
	   		displayBrandReport(response);  
	   	},
	   	error: handleAjaxError
	   });

	return false;
}

//INITIALIZATION CODE
function init(){
	$('#search-brandreport').click(searchBrandReport);	
}
$(document).ready(init);
