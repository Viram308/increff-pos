
function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/salesreport";
}

function getSalesReportData(){
	var startdate = $('#inputStartDate').val();
	var enddate = $('#inputEndDate').val();
	var brand = $('#inputBrand').val();
	var category = $('#inputCategory').val();
	if(startdate=="" || enddate==""){
		alert('Dates Required!!');
		return false;
	}
	
	var $form = $("#salesreport-form");
	var json = toJson($form);
	var url = getSalesReportUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		console.log(response);
	   		displaySalesReport(response);
	   },
	   error: function(response){ 		
	   		handleAjaxError(response);
	   		var $tbody = $('#salesreport-table').find('tbody');
	   		$tbody.empty();
		}
	});

	return false;
}

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

function viewSalesReport(){
	if ($(this).val() == "Hide") {
      $(this).html("View");
      $(this).val("View");
      $("#salesreport-table").hide();
   }
   else {
      $(this).html("Hide");
      $(this).val("Hide");
      $("#salesreport-table").show();
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
	$('#submit-salesreport-data').click(getSalesReportData);
	$('#view-salesreport-data').click(viewSalesReport);
        
}
$(document).ready(init);
