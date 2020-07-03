function showBrand(){
	$('#product').hide();
	$('#inventory').hide();
	$('#order').hide();
	$('#orderitem').hide();
	$('#brand').show();
	getBrandList();
}


function showProduct(){
	$('#brand').hide();
	$('#inventory').hide();
	$('#order').hide();
	$('#orderitem').hide();
	$('#product').show();
	getProductList();
}

function showInventory(){
	$('#brand').hide();
	$('#product').hide();
	$('#order').hide();
	$('#orderitem').hide();
	$('#inventory').show();
	getInventoryList();
}

function showOrder(){
	$('#brand').hide();
	$('#product').hide();
	$('#inventory').hide();
	$('#orderitem').hide();
	$('#order').show();
	getOrderList();
}

function showOrderItem(){
	$('#brand').hide();
	$('#product').hide();
	$('#inventory').hide();
	$('#order').hide();
	$('#orderitem').show();
	getOrderItemList();
}


//INITIALIZATION CODE
function init(){
	$('#brand-button').click(showBrand);
	$('#product-button').click(showProduct);
	$('#inventory-button').click(showInventory);
	$('#order-button').click(showOrder);
	$('#orderitem-button').click(showOrderItem);
}

$(document).ready(init);