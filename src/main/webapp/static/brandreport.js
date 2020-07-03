// get url
function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/brandreport";
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
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '</tr>';
		$tbody.append(row);
	}
}



//INITIALIZATION CODE
function init(){
	$('#refresh-brandreport-data').click(getBrandReportData);

	
}
$(document).ready(init);
