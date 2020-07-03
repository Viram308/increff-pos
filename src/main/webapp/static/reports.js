

function showSalesReport(){
	$('#brandreport').hide();
	$('#inventoryreport').hide();
	$('#salesreport').show();
}


function showBrandReport(){
	$('#salesreport').hide();
	$('#inventoryreport').hide();
	$('#brandreport').show();
	getBrandReportData();
}

function showInventoryReport(){
	$('#salesreport').hide();
	$('#brandreport').hide();
	$('#inventoryreport').show();
	getInventoryReportData();
}


//INITIALIZATION CODE
function init(){
	$('#salesreport-button').click(showSalesReport);
	$('#brandreport-button').click(showBrandReport);
	$('#inventoryreport-button').click(showInventoryReport);
}

$(document).ready(init);