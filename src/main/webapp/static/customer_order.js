function getProductUrlForBarcode(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}
var $tbody = $('#customer-order-table').find('tbody');
var $totalItems=$('#totalItems');



function addOrderItem(){
	 var itemId=$totalItems.val();
	 itemId++;
	 addEmptyItemRow(itemId);
	 $totalItems.val(itemId);
}

function updateMrpForItem(data,idForItem){
	$('#inputMrp'+idForItem).val(data.mrp);
	$('#inputQuantity'+idForItem).prop('readonly', false);
	$('#inputQuantity'+idForItem).val(1);
}
function getProductMrp(barcode,idForItem){
	var url = getProductUrlForBarcode()+"/"+"?barcode="+barcode;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		updateMrpForItem(data,idForItem);  
	   },
	   error: handleAjaxError
	});
}
function addEmptyItemRow(itemId){
		var barcodeHtml='<input type="text" class="form-control" name="barcode" id="inputBarcode'+itemId+'" placeholder="Enter Barcode" pattern="[a-z0-9]{8}" maxLength=8 style="margin: auto;width: auto;">';
		var qualityHtml='<input type="number" class="form-control" name="quantity" id="inputQuantity'+itemId+'" readonly style="margin: auto;width: auto;">';
		var mrpHtml='<input type="number" step="0.01" class="form-control" name="mrp" id="inputMrp'+itemId+'" readonly style="margin: auto;width: auto;">';
		var row = '<tr>'
		+ '<td>' + itemId + '</td>'
		+ '<td>' + barcodeHtml + '</td>'
		+ '<td>'  + qualityHtml + '</td>'
		+ '<td>' + mrpHtml + '</td>'
		+ '</tr>';
	$tbody.append(row);
	$("#inputBarcode"+itemId).on('input',function(){
		var valOfItem=$(this).val();
		var len=valOfItem.length;
      if(len==8)
      {
      	var str = $(this).attr('id');
 		var res = str.charAt(str.length-1);
      	getProductMrp($(this).val(),res);	
      }
	});
	}
function createOrder(){
	var j=$totalItems.val();
	var i=1;
	var k;
	var validData=0;
	var orderData=[];
	console.log("I = "+i+" J = "+j);
	for(i=0;i<j;i++){
		k=i+1;
		if($("#inputBarcode"+k).val().length == 8){
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
	console.log("OrderData"+orderData);
	console.log("Json");
	var url=getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(orderData),
	   headers: {
       	'Content-Type': 'application/json; charset=utf-8'
       },	   
	   success: function(response) {
	   		alert("Response is "+response);
	   		init();  
	   },
	   error: handleAjaxError
	});

}
function init(){
	$tbody.empty();
	var i=0;
	for(i=1;i<4;i++){
		addEmptyItemRow(i);
	}
	$('#add-order-item').click(addOrderItem);
	$('#create-order-data').click(createOrder);
}

$(document).ready(init);
