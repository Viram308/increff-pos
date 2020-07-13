//  get url
function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/admin/user";
}

//BUTTON ACTIONS
function addUser(){
	//Set the values to add
	var $form = $("#user-add-form");
	var json = toJson($form);
	var url = getUserUrl();
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
	   		$('#add-user-modal').modal('toggle');
	   		$.notify("User added successfully !!","success");
	   		searchUser();
	   	},
	   	error: handleAjaxError
	   });

	return false;
}

function getUserList(){
	var url = getUserUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display list
	   		displayUserList(data);   
	   	},
	   	error: handleAjaxError
	   });
}

function deleteUser(id){
	var url = getUserUrl() + "/" + id;
	// call api
	$.ajax({
		url: url,
		type: 'DELETE',
		success: function(data) {
	   		// get list
	   		searchUser();    
	   	},
	   	error: handleAjaxError
	   });
}
function updateUser(event){
	
   //Get the ID
   var id = $("#user-edit-form input[name=id]").val(); 
   var url = getUserUrl() + "/" + id;

   //Set the values to update
   var $form = $("#user-edit-form");
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
            $('#edit-user-modal').modal('toggle');
            $.notify("User updated successfully !!","success");
            searchUser(); 
        },
        error: handleAjaxError
    });

   return false;
}

function displayEditUser(id){
	var url = getUserUrl() + "/" + id;
   // call api
   $.ajax({
   	url: url,
   	type: 'GET',
   	success: function(data) {
            // display user
            displayUser(data);   
        },
        error: handleAjaxError
    });   
}

// fill entries
function displayUser(data){
	$("#user-edit-form input[name=email]").val(data.email);      
	$("#user-edit-form input[name=role]").val(data.role);       
	$("#user-edit-form input[name=id]").val(data.id);   
	$('#edit-user-modal').modal('toggle');
}


//UI DISPLAY METHODS

function displayUserList(data){
	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		// dynamic buttons
		var buttonHtml = '<button class="btn btn-outline-danger" onclick="deleteUser(' + e.id + ')">Delete</button>'
		buttonHtml += ' <button class="btn btn-outline-success" onclick="displayEditUser(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.email + '</td>'
		+ '<td>' + e.role + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		$tbody.append(row);
		j++;
	}
}

function searchUser(){
	//Set the values to add
	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	var $form = $("#user-form");
	var json = toJson($form);
	var url = getUserUrl()+"/search";
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
	   		displayUserList(response);  
	   	},
	   	error: handleAjaxError
	   });

	return false;
}

function showAddUserModal(){
	$('#add-user-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#show-add-user-modal').click(showAddUserModal);
	$('#search-user').click(searchUser);
}

$(document).ready(init);
$(document).ready(getUserList);

