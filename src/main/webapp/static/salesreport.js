// get url
function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/salesreport";
}

function getSalesReportData(){
	// take data
	var startdate = $('#inputStartDate').val();
	var enddate = $('#inputEndDate').val();
	var brand = $('#inputBrand').val();
	var category = $('#inputCategory').val();
	// validate dates
	if(startdate=="" || enddate==""){
		alert('Dates Required!!');
		return false;
	}
	

	var $form = $("#salesreport-form");
	// form to json
	var json = toJson($form);
	var url = getSalesReportUrl();
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
			// display report
			displaySalesReport(response);
		},
		error: function(response){ 		
			handleAjaxError(response);
	   		// empty table
	   		var $tbody = $('#salesreport-table').find('tbody');
	   		$tbody.empty();
	   	}
	   });

	return false;
}

// display method
function displaySalesReport(data){
	var $tbody = $('#salesreport-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + e.revenue + '</td>'
		+ '</tr>';
		$tbody.append(row);
	}
}


//INITIALIZATION CODE
function init(){
	$('#inputStartDate').datepicker({
		uiLibrary: 'bootstrap4',
		iconsLibrary: 'fontawesome',
		format: 'dd-mm-yyyy',
		maxDate: function () {
			return $('#inputEndDate').val();
		}
	});
	$('#inputEndDate').datepicker({
		uiLibrary: 'bootstrap4',
		iconsLibrary: 'fontawesome',
		format: 'dd-mm-yyyy',
		minDate: function () {
			return $('#inputStartDate').val();
		}
	});
}
$(document).ready(init);
