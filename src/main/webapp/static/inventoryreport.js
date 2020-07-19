// get url
function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/admin/inventory-report";
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
// download
function downloadInventoryReport(){
	var pdf = new jsPDF('p', 'pt', 'letter');
    // source can be HTML-formatted string, or a reference
    // to an actual DOM element from which the text will be scraped.
    source = $('#inventoryreport-div')[0];

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

    pdf.text(40, 40, "Inventory Report");
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
        pdf.save('InventoryReport.pdf');
    }, margins);
}


function searchInventoryReport(){
	//Set the values to add
	var $tbody = $('#inventoryreport-table').find('tbody');
	$tbody.empty();
	
	var $form = $("#inventoryreport-form");
	var json = toJson($form);
	var url = getInventoryReportUrl();
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
$('#download-inventoryreport').click(downloadInventoryReport);	
}
$(document).ready(init);
$(document).ready(searchInventoryReport);