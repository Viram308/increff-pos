// get url
function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/inventoryreport";
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
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '</tr>';
		$tbody.append(row);
	}
}

function viewInventoryReport(){
	// hide and view toggle
	if ($(this).val() == "Hide") {
		$(this).html("View");
		$(this).val("View");
		$("#inventoryreport-table").hide();
	}
	else {
		$(this).html("Hide");
		$(this).val("Hide");
		$("#inventoryreport-table").show();
	}
	
}

//INITIALIZATION CODE
function init(){
	$('#refresh-inventoryreport-data').click(getInventoryReportData);
	$('#view-inventoryreport-data').click(viewInventoryReport);
}
$(document).ready(init);
$(document).ready(getInventoryReportData);
