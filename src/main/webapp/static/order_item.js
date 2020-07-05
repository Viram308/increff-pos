// get url
function getOrderItemUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/orderitem";
}

// //BUTTON ACTIONS
// function updateOrderItem(event){
//    $('#edit-orderitem-modal').modal('toggle');
//    //Get the ID
//    var id = $("#orderitem-edit-form input[name=id]").val(); 
//    var url = getOrderItemUrl() + "/" + id;

//    //Set the values to update
//    var $form = $("#orderitem-edit-form");
//    var json = toJson($form);

//    $.ajax({
//       url: url,
//       type: 'PUT',
//       data: json,
//       headers: {
//          'Content-Type': 'application/json'
//       },      
//       success: function(response) {
//          // get list
//          getOrderItemList();   
//       },
//       error: handleAjaxError
//    });

//    return false;
// }

function searchOrderItem(){
   //Set the values to add
   var $tbody = $('#orderitem-table').find('tbody');
   $tbody.empty();
   var $form = $("#orderitem-form");
   var json = toJson($form);
   var url = getOrderItemUrl()+"/search";
   // call api
   $.ajax({
      url: url,
      type: 'POST',
      data: json,
      headers: {
         'Content-Type': 'application/json'
      },    
      success: function(response) {
            displayOrderItemList(response);  
         },
         error: handleAjaxError
      });

   return false;
}

function getOrderItemList(){
   var url = getOrderItemUrl();
   // call api
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
            // display data
            displayOrderItemList(data);  
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
      // dynamic buttons
     var row = '<tr>'
      + '<td>' + e.orderId + '</td>'
      + '<td>' + e.barcode + '</td>'
      + '<td>' + e.name + '</td>'
      + '<td>' + e.quantity + '</td>'
      + '<td>' + e.sellingPrice + '</td>'
      + '</tr>';
      $tbody.append(row);
   }
}

function displayEditOrderItem(id){
   var url = getOrderItemUrl() + "/" + id;
   // call api
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
            // display data
            displayOrderItem(data);   
         },
         error: handleAjaxError
      });   
}

// // fill entries
// function displayOrderItem(data){
//    $("#orderitem-edit-form input[name=barcode]").val(data.barcode);
//    $("#orderitem-edit-form input[name=quantity]").val(data.quantity);
//    $("#orderitem-edit-form input[name=mrp]").val(data.mrp);       
//    $("#orderitem-edit-form input[name=id]").val(data.id);   
//    $('#edit-orderitem-modal').modal('toggle');
// }

//INITIALIZATION CODE
function init(){
   $('#search-orderitem').click(searchOrderItem);
}

$(document).ready(init);