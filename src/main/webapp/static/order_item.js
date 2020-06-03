
function getOrderItemUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/orderitem";
}

//BUTTON ACTIONS
function updateOrderItem(event){
   $('#edit-orderitem-modal').modal('toggle');
   //Get the ID
   var id = $("#orderitem-edit-form input[name=id]").val(); 
   var url = getOrderItemUrl() + "/" + id;

   //Set the values to update
   var $form = $("#orderitem-edit-form");
   var json = toJson($form);

   $.ajax({
      url: url,
      type: 'PUT',
      data: json,
      headers: {
         'Content-Type': 'application/json'
       },      
      success: function(response) {
            getOrderItemList();   
      },
      error: handleAjaxError
   });

   return false;
}


function getOrderItemList(){
   var url = getOrderItemUrl();
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
            displayOrderItemList(data);  
      },
      error: handleAjaxError
   });
}

function deleteOrderItem(id){
   var url = getOrderItemUrl() + "/" + id;

   $.ajax({
      url: url,
      type: 'DELETE',
      success: function(data) {
            getOrderItemList();  
      },
      error: handleAjaxError
   });
}


//UI DISPLAY METHODS

function displayOrderItemList(data){
   var $tbody = $('#orderitem-table').find('tbody');
   $tbody.empty();
   for(var i in data){
      var e = data[i];
      var buttonHtml = '<button class="btn btn-outline-danger" onclick="deleteOrderItem(' + e.id + ')">Delete</button>'
      buttonHtml += ' <button class="btn btn-outline-success" onclick="displayEditOrderItem(' + e.id + ')">Edit</button>'
      var row = '<tr>'
      + '<td>' + e.id + '</td>'
      + '<td>' + e.orderId + '</td>'
      + '<td>' + e.barcode + '</td>'
      + '<td>' + e.quantity + '</td>'
      + '<td>' + e.mrp + '</td>'
      + '<td>' + buttonHtml + '</td>'
      + '</tr>';
        $tbody.append(row);
   }
}

function displayEditOrderItem(id){
   var url = getOrderItemUrl() + "/" + id;
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
            displayOrderItem(data);   
      },
      error: handleAjaxError
   });   
}

function displayOrderItem(data){
   $("#orderitem-edit-form input[name=barcode]").val(data.barcode);
   $("#orderitem-edit-form input[name=quantity]").val(data.quantity);
   $("#orderitem-edit-form input[name=mrp]").val(data.mrp);       
   $("#orderitem-edit-form input[name=id]").val(data.id);   
   $('#edit-orderitem-modal').modal('toggle');
}

function viewOrderItemList(){
   if ($(this).val() == "Hide") {
      $(this).html("View");
      $(this).val("View");
      $("#orderitem-table").hide();
   }
   else {
      $(this).html("Hide");
      $(this).val("Hide");
      $("#orderitem-table").show();
   }
   
}

//INITIALIZATION CODE
function init(){
   $('#update-orderitem').click(updateOrderItem);
   $('#view-orderitem-data').click(viewOrderItemList);
   $('#refresh-orderitem-data').click(getOrderItemList);

}

$(document).ready(init);
$(document).ready(getOrderItemList);