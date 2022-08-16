function getPlaceOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order/place-order";
}

function getSubmitOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order/submit-order";
}

function getProductUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";	
}

//BUTTON ACTIONS
function addPlaceOrder(event) {
	//Set the values to update
	var $form = $("#place_order-form");
	var json = toJsonPO($form);
	json = JSON.stringify(json);
	console.log("json object: " + json);
	var url = getPlaceOrderUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		console.log("place order created");
			console.log(response);
	   		getPlaceOrderList();
			$.notify("Order added successfully", "success");
	   },
	   error: handleAjaxErrorPlaceOrder
	});

	return false;
}

function updatePlaceOrder(event) {
	$('#edit-place_order-modal').modal('toggle');
	//Get the ID
	var id = $("#place_order-edit-form input[name=id]").val();
	var barcode = $("#place_order-edit-form input[name=barcode]").val();
	console.log(barcode)
	var url = getPlaceOrderUrl() + "/" + id;
	

	//Set the values to update
	var $form = $("#place_order-edit-form");
	let json = toJsonPO($form);
	json["barcode"] = barcode;

	json = JSON.stringify(json);

	console.log("JSON:::: " + json);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		console.log("place order update");
	   		getPlaceOrderList();
			$.notify("Order updated successfully", "success");
	   },
	   error: handleAjaxErrorPlaceOrder
	});

	return false;
}

function getPlaceOrderList() {
	var url = getPlaceOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("GET: Place Order data fetched");
			data_list = data;
			getBarcodePlaceOrder(0, data);
	   },
	   error: handleAjaxErrorPlaceOrder
	});
}

function getBarcodePlaceOrder(index, data) {
	if(index == data.length) {
		displayPlaceOrderList(data);
		return;
	}

	var url = getProductUrl() + '/barcode/' + data[index].barcode;
	$.ajax({
		url: url,
		type: 'GET',
		success: function(product_data) {
				console.log("Barcode data fetched");
				console.log(product_data);
				data[index].barcode = product_data.barcode;
				data[index].product_name = product_data.product;
				getBarcodePlaceOrder(index+1, data);
		},
		error: handleAjaxErrorPlaceOrder
	});
}

function getBarcodeList(event) {
	var url = getProductUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
				console.log("Barcode data fetched");
				console.log(data);
				displayBarcodeList(data);     //...
		},
		error: handleAjaxErrorPlaceOrder
	});
}


// Submit Order ....
var data_list = {};

function submitPlaceOrder(event) {
	var url = getSubmitOrderUrl();
	getPlaceOrderList();

	var data = JSON.stringify(data_list);

	console.log("MY DATA: " + data);

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: data,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		console.log("submit place order created");
	   		getPlaceOrderList();
			$.notify("Order placed successfully", "success");
	   },
	   error: handleAjaxErrorPlaceOrder
	});

	return false;
}

//UI DISPLAY METHODS

function displayPlaceOrderList(data) {
	console.log('Printing place order data');
	var tbody = $('#place_order-table').children('tbody');
	tbody.empty();
	var value_count = 1;
	for(var i in data) {
		var e = data[i];
		var buttonHtml = ' <button class="btn btn-primary" onclick="displayEditPlaceOrder(' + e.id + ')">Edit</button>'
		var row = '<tr>'
			+ '<td>' + value_count++ + '</td>'
			+ '<td>' + e.product_name + '</td>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>'  + e.quantity + '</td>'
			+ '<td>'  + e.selling_price.toFixed(2) + '</td>'
            + '<td>'  +(e.selling_price * e.quantity).toFixed(2) + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
    	tbody.append(row);
	}
}

function displayEditPlaceOrder(id) {
	var url = getPlaceOrderUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("EDIT: PLACE order data fetched");
			console.log("DISPLAY EDIT PLACEORDER : " + data);
	   		displayPlaceOrder(data);
	   },
	   error: handleAjaxErrorPlaceOrder
	});
}

function displayPlaceOrder(data) {
	$("#place_order-edit-form input[name=barcode]").val(data.barcode);
	$("#place_order-edit-form input[name=quantity]").val(data.quantity);
	$("#place_order-edit-form input[name=selling_price]").val(data.selling_price);
	$("#place_order-edit-form input[name=barcode]").val(data.barcode);
	$("#place_order-edit-form input[name=id]").val(data.id);
	$('#edit-place_order-modal').modal('toggle');
}

function displayBarcodeList(data) {
	console.log('Printing Barcode data');
	var barcode = $('#barcode');
    barcode.empty();
	
	var selected_option = document.createElement("option");
	selected_option.value = 0;
	selected_option.innerHTML = "Choose...";
	barcode.append(selected_option);

	for(var i in data) {
		var val = data[i];
		var option = document.createElement("option");

		option.value = val.barcode;
		option.innerHTML = val.barcode;

		barcode.append(option);
	}
}


//HELPER METHOD
function toJsonPO($form) {
    var serialized = $form.serializeArray();
    console.log("serialized : " + serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    
    return data;
}

function handleAjaxErrorPlaceOrder(response){
	var response = JSON.parse(response.responseText);
	$.notify(response.message, {autoHide : false});
}

function onlyNumberKey(evt) {
	// Only ASCII character in that range allowed
	var ASCIICode = (evt.which) ? evt.which : evt.keyCode
	if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
		return false;
	return true;
}

//INITIALIZATION CODE
function init() {
	$('#add-place_order').click(addPlaceOrder);
	$('#update-place_order').click(updatePlaceOrder);
	$('#refresh-data').click(getPlaceOrderList);
	$('#submit-place_order').click(submitPlaceOrder);
}

$(document).ready(init);
$(document).ready(getPlaceOrderList);
