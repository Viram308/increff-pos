
function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/admin/user";
}

//BUTTON ACTIONS
function addUser(event){
	//Set the values to update
	var $form = $("#user-form");
	var json = toJson($form);
	var url = getUserUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getUserList();    
	   },
	   error: handleAjaxError
	});

	return false;
}

function getUserList(){
	var url = getUserUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUserList(data);   
	   },
	   error: handleAjaxError
	});
}

function deleteUser(id){
	var url = getUserUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getUserList();    
	   },
	   error: handleAjaxError
	});
}
function updateUser(event){
   $('#edit-user-modal').modal('toggle');
   //Get the ID
   var id = $("#user-edit-form input[name=id]").val(); 
   var url = getUserUrl() + "/" + id;

   //Set the values to update
   var $form = $("#user-edit-form");
   var json = toJson($form);

   $.ajax({
      url: url,
      type: 'PUT',
      data: json,
      headers: {
         'Content-Type': 'application/json'
       },      
      success: function(response) {
            getUserList();   
      },
      error: handleAjaxError
   });

   return false;
}

function displayEditUser(id){
   var url = getUserUrl() + "/" + id;
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
            displayUser(data);   
      },
      error: handleAjaxError
   });   
}

function displayUser(data){
   $("#user-edit-form input[name=email]").val(data.email);
   $("#user-edit-form input[name=role]").val(data.role);       
   $("#user-edit-form input[name=id]").val(data.id);   
   $('#edit-user-modal').modal('toggle');
}


//UI DISPLAY METHODS

function displayUserList(data){
	console.log('Printing user data');
	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="deleteUser(' + e.id + ')">delete</button>'
		buttonHtml += ' <button onclick="displayEditUser(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.email + '</td>'
		+ '<td>' + e.role + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}


//INITIALIZATION CODE
function init(){
	$('#add-user').click(addUser);
	$('#refresh-data').click(getUserList);
	$('#update-user').click(updateUser);

}

$(document).ready(init);
$(document).ready(getUserList);

