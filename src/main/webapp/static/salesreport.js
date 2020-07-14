// get url
function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/admin/sales-report";
}

function getSalesReportData(){
	// take data
	var $tbody = $('#salesreport-table').find('tbody');
	   		$tbody.empty();
	var startdate = $('#inputStartDate').val().trim();
	var enddate = $('#inputEndDate').val().trim();
	// validate dates
	if(startdate=="" && enddate!=""){
		$.notify("Enter both dates or none !!","error");
		return false;
	}
	
	if(startdate!="" && enddate==""){
		$.notify("Enter both dates or none !!","error");
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
	   	}
	   });

	return false;
}

// display method
function displaySalesReport(data){
	var $tbody = $('#salesreport-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + e.revenue + '</td>'
		+ '</tr>';
		j++;
		$tbody.append(row);
	}
}

function downloadSalesReport(){

	var pdf = new jsPDF('p', 'pt', 'letter');
    // source can be HTML-formatted string, or a reference
    // to an actual DOM element from which the text will be scraped.
    source = $('#salesreport-div')[0];

    // we support special element handlers. Register them with jQuery-style 
    // ID selector for either ID or node name. ("#iAmID", "div", "span" etc.)
    // There is no support for any other type of selectors 
    // (class, of compound) at this time.
    specialElementHandlers = {
        // element with id of "bypass" - jQuery style selector
        '#bypassme': function (element, renderer) {
            // true = "handled elsewhere, bypass text extraction"
            return true
        }
    };
    margins = {
        top: 80,
        bottom: 60,
        left: 40,
        width: 522
    };
    // all coords and widths are in jsPDF instance's declared units
    // 'inches' in this case
    pdf.fromHTML(
    source, // HTML string or DOM elem ref.
    margins.left, // x coord
    margins.top, { // y coord
        'width': margins.width, // max width of content on PDF
        'elementHandlers': specialElementHandlers
    },

    function (dispose) {
        // dispose: object with X, Y of the last line add to the PDF 
        //          this allow the insertion of new lines after html
        pdf.save('SalesReport.pdf');
    }, margins);
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
	$('#download-salesreport').click(downloadSalesReport);
}
$(document).ready(init);
$(document).ready(getSalesReportData);