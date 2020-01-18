function getProductUrlForBarcode(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product/";
}


var $tbody = $('#customer-order-table').find('tbody');
var $totalItems=$('#totalItems');



function addOrderItem(){
	 var itemId=$totalItems.val();
	 itemId++;
	 addEmptyItemRow(itemId);
	 $totalItems.val(itemId)
}

function updateMrpForItem(data,idForItem){
	$('#inputMrp'+idForItem).val(data.mrp);
	$('#inputQuantity'+idForItem).prop('readonly', false);
	$('#inputQuantity'+idForItem).val(1);
}
function getProductMrp(barcode,idForItem){
	var url = getProductUrlForBarcode()+"?barcode="+barcode;
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
		+ '<form class="form-inline" id="orderItemForm'+itemId+'">'
		+ '<td>' + barcodeHtml + '</td>'
		+ '<td>'  + qualityHtml + '</td>'
		+ '<td>' + mrpHtml + '</td>'
		+ '</form>'
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
	alert('Creating Order')
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
