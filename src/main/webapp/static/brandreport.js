
function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/brandreport";
}

function getBrandReportData(){
	var url = getBrandReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',	   
	   success: function(response) {
	   		displayBrandReport(response);
	   },
	   error: handleAjaxError
	});

	return false;
}

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

function viewBrandReport(){
	if ($(this).val() == "Hide") {
      $(this).html("View");
      $(this).val("View");
      $("#brandreport-table").hide();
   }
   else {
      $(this).html("Hide");
      $(this).val("Hide");
      $("#brandreport-table").show();
   }
	
}

//INITIALIZATION CODE
function init(){
	$('#refresh-brandreport-data').click(getBrandReportData);
	$('#view-brandreport-data').click(viewBrandReport);
        
}
$(document).ready(init);
$(document).ready(getBrandReportData);
