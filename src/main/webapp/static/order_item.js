// get url
function getOrderItemUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/admin/orderitem";
}

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


//INITIALIZATION CODE
function init(){
   $('#search-orderitem').click(searchOrderItem);
}

$(document).ready(init);
$(document).ready(searchOrderItem);