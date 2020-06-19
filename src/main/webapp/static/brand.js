// get url
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addBrand(event){
	//Set the values to add
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandUrl();
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
	   		// get list
	   		getBrandList();  
	   	},
	   	error: handleAjaxError
	   });

	return false;
}

function updateBrand(event){
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();	
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);
	// call api
	$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
	   		// get list
	   		getBrandList();   
	   	},
	   	error: handleAjaxError
	   });

	return false;
}


function getBrandList(){
	var url = getBrandUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display data
	   		displayBrandList(data);  
	   	},
	   	error: handleAjaxError
	   });
}

function deleteBrand(id){
	var url = getBrandUrl() + "/" + id;
	// call api
	$.ajax({
		url: url,
		type: 'DELETE',
		success: function(data) {
	   		// get list
	   		getBrandList();  
	   	},
	   	error: handleAjaxError
	   });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processDataBrand(){
	var file = $('#brandFile')[0].files[0];
	readFileData(file, readFileDataCallbackBrand);
}

function readFileDataCallbackBrand(results){
	fileData = results.data;
	// check no of rows
	if(fileData.length > 5000)
	{
		alert('File Contains more than 5000 rows !!');
		return;
	}
	uploadRowsBrand();
}

function uploadRowsBrand(){
	//Update progress
	updateUploadDialogBrand();
	//If everything processed then return
	if(processCount==fileData.length){
		getBrandList();
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getBrandUrl();

	//Make ajax call
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
			uploadRowsBrand();  
		},
		error: function(response){
			row.error=response.responseText
			errorData.push(row);
			uploadRowsBrand();
		}
	});

}

function downloadErrorsBrand(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		// dynamic buttons
		var buttonHtml = '<button class="btn btn-outline-danger" onclick="deleteBrand(' + e.id + ')">Delete</button>'
		buttonHtml += ' <button class="btn btn-outline-success" onclick="displayEditBrand(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		$tbody.append(row);
	}
}

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display brand for update
	   		displayBrand(data);   
	   	},
	   	error: handleAjaxError
	   });	
}

function resetUploadDialogBrand(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialogBrand();
}

function updateUploadDialogBrand(){
	// update counts
	$('#rowCountBrand').html("" + fileData.length);
	$('#processCountBrand').html("" + processCount);
	$('#errorCountBrand').html("" + errorData.length);
}

function updateFileNameBrand(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function displayUploadDataBrand(){
	resetUploadDialogBrand(); 	
	$('#upload-brand-modal').modal('toggle');
}

function displayBrand(data){
	// fill entries
	$("#brand-edit-form input[name=brand]").val(data.brand);	
	$("#brand-edit-form input[name=category]").val(data.category);	
	$("#brand-edit-form input[name=id]").val(data.id);	
	$('#edit-brand-modal').modal('toggle');
}
function viewBrandList(){
	// hide and view toggle
	if ($(this).val() == "Hide") {
		$(this).html("View");
		$(this).val("View");
		$("#brand-table").hide();
	}
	else {
		$(this).html("Hide");
		$(this).val("Hide");
		$("#brand-table").show();
	}
	
}

//INITIALIZATION CODE
function init(){
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#view-brand-data').click(viewBrandList);
	$('#upload-brand-data').click(displayUploadDataBrand);
	$('#process-data-brand').click(processDataBrand);
	$('#download-errors-brand').click(downloadErrorsBrand);
	$('#brandFile').on('change', updateFileNameBrand);
	$('#refresh-brand-data').click(getBrandList);
}

$(document).ready(init);
$(document).ready(getBrandList);

