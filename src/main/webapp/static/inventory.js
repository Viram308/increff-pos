function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

//BUTTON ACTIONS
function addInventory(event){
	//Set the values to update
	var $form = $("#inventory-form");
	var json = toJson($form);
	var url = getInventoryUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getInventoryList();  
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateInventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();	
	var url = getInventoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getInventoryList();   
	   },
	   error: handleAjaxError
	});

	return false;
}


function getInventoryList(){
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);  
	   },
	   error: handleAjaxError
	});
}

function deleteInventory(id){
	var url = getInventoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getInventoryList();  
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processDataInventory(){
	var file = $('#inventoryFile')[0].files[0];
	readFileData(file, readFileDataCallbackInventory);
}

function readFileDataCallbackInventory(results){
	fileData = results.data;
	if(fileData.length > 5000)
	{
		alert('File Contains more than 5000 rows !!');
		return;
	}
	uploadRowsInventory();
}

function uploadRowsInventory(){
	//Update progress
	updateUploadDialogInventory();
	//If everything processed then return
	if(processCount==fileData.length){
		getInventoryList();
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getInventoryUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRowsInventory();  
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRowsInventory();
	   }
	});

}

function downloadErrorsInventory(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-outline-danger" onclick="deleteInventory(' + e.id + ')">Delete</button>'
		buttonHtml += ' <button class="btn btn-outline-success" onclick="displayEditInventory(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditInventory(id){
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);   
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialogInventory(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialogInventory();
}

function updateUploadDialogInventory(){
	$('#rowCountInventory').html("" + fileData.length);
	$('#processCountInventory').html("" + processCount);
	$('#errorCountInventory').html("" + errorData.length);
}

function updateFileNameInventory(){
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function displayUploadDataInventory(){
 	resetUploadDialogInventory(); 	
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);		
	$("#inventory-edit-form input[name=id]").val(data.id);	
	$('#edit-inventory-modal').modal('toggle');
}

function viewInventoryList(){
	if ($(this).val() == "Hide") {
      $(this).html("View");
      $(this).val("View");
      $("#inventory-table").hide();
   }
   else {
      $(this).html("Hide");
      $(this).val("Hide");
      $("#inventory-table").show();
   }
	
}

//INITIALIZATION CODE
function init(){
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#view-inventory-data').click(viewInventoryList);
	$('#upload-inventory-data').click(displayUploadDataInventory);
	$('#process-data-inventory').click(processDataInventory);
	$('#download-errors-inventory').click(downloadErrorsInventory);
    $('#inventoryFile').on('change', updateFileNameInventory);
    $('#refresh-inventory-data').click(getInventoryList);

}

$(document).ready(init);
$(document).ready(getInventoryList);

