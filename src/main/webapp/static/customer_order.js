// URLs
function getProductUrlForBarcode(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getBillUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/bill/resultPDF.pdf";
}
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}
// Global variables
var $tbody = $('#customer-order-table').find('tbody');
var $totalItems=$('#totalItems');


// add item
function addOrderItem(){
	var itemId=$totalItems.val();
	itemId++;
	addEmptyItemRow(itemId);
	$totalItems.val(itemId);
}
// update fields
function updateMrpForItem(data,idForItem){
	$('#inputMrp'+idForItem).val(data.mrp);
	$('#inputQuantity'+idForItem).prop('readonly', false);
	$('#inputQuantity'+idForItem).val(1);
}

// get mrp
function getProductMrp(barcode,idForItem){
	var url = getProductUrlForBarcode()+"/b/"+barcode;
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		//update mrp
	   		updateMrpForItem(data,idForItem);  
	   	},
	   	error: function(response){ 		
	   		handleAjaxError(response);
	   		// nullify barcode
	   		$('#inputBarcode'+idForItem).val('');  
	   	}
	   });
}

// add row
function addEmptyItemRow(itemId){

	var barcodeHtml='<input type="text" class="form-control" name="barcode" id="inputBarcode'+itemId+'" placeholder="Enter Barcode" pattern="[a-z0-9]{8}" maxLength=8 style="margin: auto;width: auto;">';
	var qualityHtml='<input type="number" class="form-control" name="quantity" id="inputQuantity'+itemId+'" placeholder="Enter Quantity" readonly style="margin: auto;width: auto;" required>';
	var mrpHtml='<input type="number" step="0.01" class="form-control" name="mrp" id="inputMrp'+itemId+'" readonly style="margin: auto;width: auto;">';
	var row = '<tr>'
	+ '<td>' + itemId + '</td>'
	+ '<td>' + barcodeHtml + '</td>'
	+ '<td>'  + qualityHtml + '</td>'
	+ '<td>' + mrpHtml + '</td>'
	+ '</tr>';
	$tbody.append(row);
	// at length 8 call api
	$("#inputBarcode"+itemId).on('input',function(){
		var valOfItem=$(this).val();
		var len=valOfItem.length;
		if(len < 8){
			$('#inputQuantity'+itemId).val('');
			$('#inputQuantity'+itemId).prop('readonly', true);
			$('#inputMrp'+itemId).val('');
		}
		if(len==8)
		{
      	// call api
      	getProductMrp($(this).val(),itemId);	
      }
  });
}

// open pdf
function openBillPdf(blob){
	window.open(window.URL.createObjectURL(blob),"bill.pdf");
}

// download pdf with proper name
function downloadBillPdf(blob){
	let link = document.createElement('a');

	link.href = window.URL.createObjectURL(blob);
	var currentdate = new Date();
	link.download = "bill_"+ currentdate.getDate() + "/"
	+ (currentdate.getMonth()+1)  + "/" 
	+ currentdate.getFullYear() + "@"  
	+ currentdate.getHours() + "h_"  
	+ currentdate.getMinutes() + "m_" 
	+ currentdate.getSeconds()+"s.pdf";
	link.click();
}	

// create order
function createOrder(){
	var j=$totalItems.val();
	var i=1;
	var k;
	var f=0;
	var validData=0;
	var orderData=[];
	var checkData;
	// remove invalid data
	for(i=0;i<j;i++){
		k=i+1;
		if($("#inputBarcode"+k).val().length == 8){
			if($('#inputQuantity'+k).val() <= 0){
				alert('Quantity for id : '+k+' can not be negative or zero !!');
				return false;
			}
			// create json
			var json = {
				"barcode":$("#inputBarcode"+k).val(),
				"quantity":$("#inputQuantity"+k).val(),
				"mrp":$("#inputMrp"+k).val()	
			};
			console.log("JSON for orderItemForm" +i+"is "+JSON.stringify(json));
			orderData[validData]=json;
			validData++;
		}
	}
	if(validData==0){
		alert('Please enter valid data !!!');
		return false;
	}
// call api
var url=getOrderUrl();
$.ajax({
	url: url,
	type: 'POST',
	data: JSON.stringify(orderData),
	headers: {
		'Content-Type': 'application/json; charset=utf-8'
	},	   
	success: function(data) {
	   		// process recieved pdf
	   		let binaryString = window.atob(data);

	   		let binaryLen = binaryString.length;

	   		let bytes = new Uint8Array(binaryLen);

	   		for (let i = 0; i < binaryLen; i++) {
	   			let ascii = binaryString.charCodeAt(i);
	   			bytes[i] = ascii;
	   		}

	   		let blob = new Blob([bytes], {type: "application/pdf"});
//openBillPdf(blob);
downloadBillPdf(blob);
initView();  
},
error: handleAjaxError
});

}

// Initialization code
function initView(){
	$tbody.empty();
	$totalItems.val(3);
	var i=0;
	for(i=1;i<4;i++){
		addEmptyItemRow(i);
	}
}
function init(){
	$('#add-order-item').click(addOrderItem);
	$('#create-order-data').click(createOrder);
	initView();
}

$(document).ready(init);
