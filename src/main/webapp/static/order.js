	// URLs
	function getProductUrl(){
		var baseUrl = $("meta[name=baseUrl]").attr("content")
		return baseUrl + "/api/admin/product";
	}

	// get url
	function getOrderItemUrl(){
		var baseUrl = $("meta[name=baseUrl]").attr("content")
		return baseUrl + "/api/admin/orderitem";
	}
	function getOrderUrl(){
		var baseUrl = $("meta[name=baseUrl]").attr("content")
		return baseUrl + "/api/admin/order";
	}

	function getInventoryUrl(){
		var baseUrl = $("meta[name=baseUrl]").attr("content")
		return baseUrl + "/api/admin/inventory";
	}

	// Global variables
	var $tbody = $('#customer-order-table').find('tbody');
	var $totalItems=$('#totalItems');
	var $tbodyEdit = $('#customer-order-edit-table').find('tbody');
	var $totalItemsEdit=$('#totalItemsEdit');
	var editOrderItemsJsonArray=[];
	function getOrderList(){
		var url = getOrderUrl();
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
		var url = getOrderUrl() + "/" + id;
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

function viewOrder(id){
	$('#view-order-modal').modal('toggle');
	getOrderItems(id);
}

function editOrder(id){
	$('#edit-order-modal').modal('toggle');
	$("#order-edit-form input[name=id]").val(id);
	getOrderItemsEdit(id);

}


function getOrderItemsEdit(id){
	var url = getOrderItemUrl() + "/" + id;
   // call api
   $.ajax({
   	url: url,
   	type: 'GET',
   	success: function(data) {
            // display data
            displayOrderItemsEdit(data);   
        },
        error: handleAjaxError
    });   
}


function getOrderItems(id){
	var url = getOrderItemUrl() + "/" + id;
   // call api
   $.ajax({
   	url: url,
   	type: 'GET',
   	success: function(data) {
            // display data
            displayOrderItems(data);   
        },
        error: handleAjaxError
    });   
}

function displayOrderItems(data){
	var $tbodyViewOrder = $('#view-order-table').find('tbody');
	$tbodyViewOrder.empty();

	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.sellingPrice + '</td>'
		+ '</tr>';
		$tbodyViewOrder.append(row);
	}
}

function displayOrderItemsEdit(data){
	var $tbodyEditOrder = $('#customer-order-edit-table').find('tbody');
	$tbodyEditOrder.empty();
	var j=0;
	for(var i in data){
		var e = data[i];
		var json={
			"barcode":e.barcode,
			"quantity":e.quantity
		};
		editOrderItemsJsonArray[j]=json;
		var buttonHtml = ' <input type="button" class="btn btn-outline-danger" value="Delete" id="deleteItemButtonEdit">'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.sellingPrice + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		$tbodyEditOrder.append(row);
		j++;
	}
	console.log(editOrderItemsJsonArray);
	$totalItemsEdit.val(j);
}

function searchOrder(){
	//Set the values to add
	var $tbodyOrder = $('#order-table').find('tbody');
	$tbodyOrder.empty();
	// take data
	var startdate = $('#inputStartDate').val().trim();
	var enddate = $('#inputEndDate').val().trim();

	// validate dates
	if(startdate=="" || enddate==""){
		alert('Dates Required!!');
		return false;
	}
	var $form = $("#order-form");
	// form to json
	var json = toJson($form);
	var url = getOrderUrl()+"/search";
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
			displayOrderList(response);
		},
		error: function(response){ 		
			handleAjaxError(response);
		}
	});
}

// updates order
function updateOrder(){
	if($totalItemsEdit.val()==0){
		alert('Add items to create order');
		return false;
	}
	var table = document.getElementById("customer-order-edit-table");
	var orderData=[];
	var j=0;
	for (var i = 1, row; row = table.rows[i]; i++) {
   			// create json
   			var json = {
   				"barcode":row.cells[0].innerHTML,
   				"quantity":row.cells[2].innerHTML,
   				"sellingPrice":row.cells[3].innerHTML	
   			};

   			orderData[j]=json;
   			j++;
   		}

		// call api
		//Get the ID
		var id = $("#order-edit-form input[name=id]").val();	

		var url=getOrderUrl()+"/"+id;
		$.ajax({
			url: url,
			type: 'PUT',
			data: JSON.stringify(orderData),
			headers: {
				'Content-Type': 'application/json; charset=utf-8'
			},	   
			success: function(data) {
				editOrderItemsJsonArray=[];
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
$tbodyEdit.empty();
$totalItemsEdit.val(0);
$('#edit-order-modal').modal('toggle');

},
error: handleAjaxError
});

	}

	function createOrder(){
		if($totalItems.val()==0){
			alert('Add items to create order');
			return false;
		}
		var table = document.getElementById("customer-order-table");
		var orderData=[];
		var j=0;
		for (var i = 1, row; row = table.rows[i]; i++) {
   			// create json
   			var json = {
   				"barcode":row.cells[0].innerHTML,
   				"quantity":row.cells[2].innerHTML,
   				"sellingPrice":row.cells[3].innerHTML	
   			};

   			orderData[j]=json;
   			j++;
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
$tbody.empty();
$totalItems.val(0);
$('#add-order-modal').modal('toggle');

},
error: handleAjaxError
});

	}

	//UI DISPLAY METHODS

	function displayOrderList(data){
		var $tbodyOrder = $('#order-table').find('tbody');
		$tbodyOrder.empty();
		for(var i in data){
			var e = data[i];
			var buttonHtml = '<button class="btn btn-outline-primary" onclick="viewOrder(' + e.id + ')">View</button>'
			buttonHtml+=' <button class="btn btn-outline-success" onclick="editOrder(' + e.id + ')">Edit</button>'
			var row = '<tr>'
			+ '<td>' + e.id + '</td>'
			+ '<td>' + e.datetime + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
			$tbodyOrder.append(row);
		}
	}

	// update fields
	function updateProductDetailsForItem(data){
		$('#inputMrp').val(data.mrp);
		$('#inputName').val(data.name);
		$('#inputQuantity').prop('readonly', false);
		$('#inputQuantity').val(1);
		var table = document.getElementById("customer-order-table");
		var quantity=checkItemExist(data.barcode,table);
		var availableQuantity=data.availableQuantity - quantity;
		$('#availableQuantity').text(availableQuantity);
		$('#availableInventoryRow').show();
	}

	function updateProductDetailsForItemEditOrder(data){
		$('#inputMrpEditOrder').val(data.mrp);
		$('#inputNameEditOrder').val(data.name);
		$('#inputQuantityEditOrder').prop('readonly', false);
		$('#inputQuantityEditOrder').val(1);
		var table = document.getElementById("customer-order-edit-table");
		var quantity=checkItemExist(data.barcode,table);
		var availableQuantity=data.availableQuantity - quantity;
		for(var i = 0; i < editOrderItemsJsonArray.length; i++) {
			var barcodeInArray=editOrderItemsJsonArray[i].barcode;
			var barcode=data.barcode;
			var checkEquality=barcode.localeCompare(barcodeInArray);
			if(checkEquality==0){
				availableQuantity=+availableQuantity + +editOrderItemsJsonArray[i].quantity;
			}
		}
		$('#availableQuantityEditOrder').text(availableQuantity);
		$('#availableInventoryRowEditOrder').show();
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


// get mrp
function getProductDetailsEditOrder(barcode){
	var url = getProductUrl()+"/b/"+barcode;
		// call api
		$.ajax({
			url: url,
			type: 'GET',
			success: function(data) {
		   		//update mrp
		   		updateProductDetailsForItemEditOrder(data);  
		   	},
		   	error: function(response){ 		
		   		handleAjaxError(response);
		   		// nullify barcode
		   		$('#inputBarcodeEditOrder').val('');  
		   	}
		   });
	}

function updateQuantityInTable(barcode,table,finalQuantity){
	
	var checkEquality=9;
	for (var i = 1, row; row = table.rows[i]; i++) {
		var cellBarcode=row.cells[0].innerHTML;
		checkEquality=cellBarcode.localeCompare(barcode);
		if(checkEquality==0)
		{
			row.cells[2].innerHTML=finalQuantity;
		}
   	}
}

	function addItemInTable(){
		var barcode=$("#inputBarcode").val();
		var quantity=$('#inputQuantity').val();
		if(barcode.length == 8){
			if(quantity <= 0){
				alert('Quantity for product can not be negative or zero !!');
				return false;
			}
			var availableQuantity=parseInt($('#availableQuantity').text());
			if(availableQuantity < quantity){
				alert('Available quantity is '+availableQuantity);
				return false;		
			}
			var table = document.getElementById("customer-order-table");
			var quantityInTable=checkItemExist(barcode,table);
			if(quantityInTable == 0){
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
}
else{
	var finalQuantity=+quantityInTable + +quantity;
	updateQuantityInTable(barcode,table,finalQuantity);
}
	$('#inputMrp').val('');
	$('#inputName').val('');
	$('#inputQuantity').prop('readonly', true);
	$('#inputQuantity').val('');
	$('#inputBarcode').val('');
	$('#availableInventoryRow').hide();
	$('#availableQuantity').text('');
}
else{
	alert('Please enter valid barcode');
	return false;
}
}


function addItemInEditTable(){
	var barcode=$("#inputBarcodeEditOrder").val();
	var quantity=$('#inputQuantityEditOrder').val();
	if(barcode.length == 8){
		if(quantity <= 0){
			alert('Quantity for product can not be negative or zero !!');
			return false;
		}
		var availableQuantity=parseInt($('#availableQuantityEditOrder').text());
			if(availableQuantity < quantity){
				alert('Available quantity is '+availableQuantity);
				return false;		
			}
			var table = document.getElementById("customer-order-edit-table");
			var quantityInTable=checkItemExist(barcode,table);
			if(quantityInTable == 0){
			
		var itemId=$totalItemsEdit.val();
		itemId++;
		$totalItemsEdit.val(itemId);
	// dynamic buttons

	var buttonHtml = ' <input type="button" class="btn btn-outline-danger" value="Delete" id="deleteItemButton">'

	var row = '<tr>'
	+ '<td>' + $("#inputBarcodeEditOrder").val() + '</td>'
	+ '<td>' + $("#inputNameEditOrder").val() + '</td>'
	+ '<td>'  + $("#inputQuantityEditOrder").val() + '</td>'
	+ '<td>' + $("#inputMrpEditOrder").val() + '</td>'
	+ '<td>' + buttonHtml + '</td>'
	+ '</tr>';
	$tbodyEdit.append(row);
}
else{
	var finalQuantity=+quantityInTable + +quantity;
	updateQuantityInTable(barcode,table,finalQuantity);
}
	$('#inputMrpEditOrder').val('');
	$('#inputNameEditOrder').val('');
	$('#inputQuantityEditOrder').prop('readonly', true);
	$('#inputQuantityEditOrder').val('');
	$('#inputBarcodeEditOrder').val('');
	$('#availableInventoryRowEditOrder').hide();
	$('#availableQuantityEditOrder').text('');

}
else{
	alert('Please enter valid barcode');
	return false;
}
}

function checkItemExist(barcode,table){
	var quantity=0;
	var checkEquality=9;
	for (var i = 1, row; row = table.rows[i]; i++) {
		var cellBarcode=row.cells[0].innerHTML;
		checkEquality=cellBarcode.localeCompare(barcode);
		if(checkEquality==0)
		{
			quantity=row.cells[2].innerHTML;
		}
   	}
   	return quantity;
}

function showOrderModal(){
	$('#add-order-modal').modal('toggle');
}


	//INITIALIZATION CODE
	function init(){
		$('#show-add-order-modal').click(showOrderModal);
		$('#add-item-button').click(addItemInTable);
		$('#add-item-button-edit').click(addItemInEditTable);
		$("#inputBarcode").on('input',function(){
			var valOfItem=$(this).val();
			var len=valOfItem.length;
			if(len < 8){
				$('#inputQuantity').val('');
				$('#inputQuantity').prop('readonly', true);
				$('#inputName').val('');
				$('#inputMrp').val('');
				$('#availableInventoryRow').hide();
			}
			if(len==8)
			{
	      	// call api
	      	getProductDetails($(this).val());	
	      }
	  });
		$("#inputBarcodeEditOrder").on('input',function(){
			var valOfItem=$(this).val();
			var len=valOfItem.length;
			if(len < 8){
				$('#inputQuantityEditOrder').val('');
				$('#inputQuantityEditOrder').prop('readonly', true);
				$('#inputNameEditOrder').val('');
				$('#inputMrpEditOrder').val('');
				$('#availableInventoryRowEditOrder').hide();
			}
			if(len==8)
			{
	      	// call api
	      	getProductDetailsEditOrder($(this).val());	
	      }
	  });
		$('#customer-order-table').on('click', 'input[type="button"]', function () {
			var closestTr=$(this).closest('tr');
			var barcodeInTable=closestTr.find('td:eq(0)').text();
			var barcode=$('#inputBarcode').val();
			var checkEquality=barcode.localeCompare(barcodeInTable);
			if(checkEquality==0){
				var quantityInTable=closestTr.find('td:eq(2)').text();
				var availableQuantity=parseInt($('#availableQuantity').text());
				var finalQuantity=+quantityInTable + +availableQuantity;
				$('#availableQuantity').text(finalQuantity);
			}
			closestTr.remove();
			var itemId=$totalItems.val();
			itemId--;
			$totalItems.val(itemId);
		});

		$('#customer-order-edit-table').on('click', 'input[type="button"]', function () {
			var closestTr=$(this).closest('tr');
			var barcodeInTable=closestTr.find('td:eq(0)').text();
			var barcode=$('#inputBarcodeEditOrder').val();
			var checkEquality=barcode.localeCompare(barcodeInTable);
			if(checkEquality==0){
				var quantityInTable=closestTr.find('td:eq(2)').text();
				var availableQuantity=parseInt($('#availableQuantityEditOrder').text());
				var finalQuantity=+quantityInTable + +availableQuantity;
				$('#availableQuantityEditOrder').text(finalQuantity);
			}
			closestTr.remove();
			var itemId=$totalItemsEdit.val();
			itemId--;
			$totalItemsEdit.val(itemId);
		});


		$('#create-order').click(createOrder);
		$('#update-order').click(updateOrder);
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

		$('#search-order').click(searchOrder);
		$('.modal').on('hidden.bs.modal', function(){
			editOrderItemsJsonArray=[];
			$('#availableInventoryRow').hide();
			$('#availableInventoryRowEditOrder').hide();
		});
	}

	$(document).ready(init);


