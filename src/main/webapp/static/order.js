	// URLs
	function getProductUrl(){
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

	// get url
	function getOrderApiUrl(){
		var baseUrl = $("meta[name=baseUrl]").attr("content")
		return baseUrl + "/api/order";
	}
	// Global variables
	var $tbody = $('#customer-order-table').find('tbody');
	var $totalItems=$('#totalItems');

	function getOrderList(){
		var url = getOrderApiUrl();
		// call api
		$.ajax({
			url: url,
			type: 'GET',
			success: function(data) {
		   	// display list
		   	displayOrderList(data);  
		   },
		   error: handleAjaxError
		});
	}

	function deleteOrder(id){
		var url = getOrderApiUrl() + "/" + id;
		// call api
		$.ajax({
			url: url,
			type: 'DELETE',
			success: function(data) {
		   		// get list
		   		getOrderList();  
		   	},
		   	error: handleAjaxError
		   });
	}



	function createOrder(){
		
	}

	//UI DISPLAY METHODS

	function displayOrderList(data){
		var $tbody = $('#order-table').find('tbody');
		$tbody.empty();
		for(var i in data){
			var e = data[i];
			var buttonHtml = '<button class="btn btn-outline-danger" onclick="deleteOrder(' + e.id + ')">Delete</button>'
			var row = '<tr>'
			+ '<td>' + e.id + '</td>'
			+ '<td>' + e.datetime + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
			$tbody.append(row);
		}
	}

	// update fields
	function updateProductDetailsForItem(data){
		$('#inputMrp').val(data.mrp);
		$('#inputName').val(data.name);
		$('#inputQuantity').prop('readonly', false);
		$('#inputQuantity').val(1);
	}

	// get mrp
	function getProductDetails(barcode){
		var url = getProductUrl()+"/b/"+barcode;
		// call api
		$.ajax({
			url: url,
			type: 'GET',
			success: function(data) {
		   		//update mrp
		   		updateProductDetailsForItem(data);  
		   	},
		   	error: function(response){ 		
		   		handleAjaxError(response);
		   		// nullify barcode
		   		$('#inputBarcode').val('');  
		   	}
		   });
	}

	function addItemInTable(){
		if($("#inputBarcode").val().length == 8){
			if($('#inputQuantity').val() <= 0){
				alert('Quantity for product can not be negative or zero !!');
				return false;
			}
			var itemId=$totalItems.val();
			itemId++;
			$totalItems.val(itemId);
	// dynamic buttons

	var buttonHtml = ' <input type="button" class="btn btn-outline-danger" value="Delete" id="deleteItemButton">'

	var row = '<tr>'
	+ '<td>' + $("#inputBarcode").val() + '</td>'
	+ '<td>' + $("#inputName").val() + '</td>'
	+ '<td>'  + $("#inputQuantity").val() + '</td>'
	+ '<td>' + $("#inputMrp").val() + '</td>'
	+ '<td>' + buttonHtml + '</td>'
	+ '</tr>';
	$tbody.append(row);
	$('#inputMrp').val('');
	$('#inputName').val('');
	$('#inputQuantity').prop('readonly', true);
	$('#inputQuantity').val('');
	$('#inputBarcode').val('');
}
else{
	alert('Please enter valid barcode');
}
}

function showOrderModal(){
	$('#add-order-modal').modal('toggle');
}


	//INITIALIZATION CODE
	function init(){
		$('#show-add-order-modal').click(showOrderModal);
		$('#add-item-button').click(addItemInTable);

		$("#inputBarcode").on('input',function(){
			var valOfItem=$(this).val();
			var len=valOfItem.length;
			if(len < 8){
				$('#inputQuantity').val('');
				$('#inputQuantity').prop('readonly', true);
				$('#inputName').val('');
				$('#inputMrp').val('');
			}
			if(len==8)
			{
	      	// call api
	      	getProductDetails($(this).val());	
	      }
	  });
		$('#customer-order-table').on('click', 'input[type="button"]', function () {
			$(this).closest('tr').remove();
			var itemId=$totalItems.val();
		itemId--;
		$totalItems.val(itemId);
		});
		$('#create-order').click(createOrder);
	}

	$(document).ready(init);


