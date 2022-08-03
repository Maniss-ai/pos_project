function getViewOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order/view_order";
}

function getSubmitOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order/submit_order";
}

function getPlaceOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order/place_order";	
}


// BUTTON ACTIONS
function getViewOrderList(event) {
    var $form = $("#view_order-form");
	var json = toJson($form);
	console.log("VIEW ORDER: JSON OBJECT: " + json);

    var url = getViewOrderUrl();
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(view_order_data) {
            console.log("Getting Placed Orders ....");
            displaySelectedOrders(view_order_data);
        },
        error: handleAjaxError
    });
}

//UI DISPLAY METHODS

function displaySelectedOrders(data) {
	console.log('Printing placed orders data ....');
	var tbody = $('#view_order-table').children('tbody');
	tbody.empty();
	var value_count = 1;
	for(var i in data) {
		var e = data[i];
		var buttonHtml = '<button class="btn btn-primary" onclick="generateInvoice(' + e.id + ')">Generate Invoice</button>'
		buttonHtml += ' <button class="btn btn-primary" onclick="viewSelectedOrders(' + e.id + ')">View Order</button>'
		var row = '<tr>'
			+ '<td>' + value_count++ + '</td>'
			+ '<td>' + e.id + '</td>'
			+ '<td>' + e.time + '</td>'
			+ '<td>'  + e.bill_amount + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
    	tbody.append(row);
	}
}

/**********************************  TODO  **********************************/
/**********************************  TODO  **********************************/
/**********************************  TODO  **********************************/
/**********************************  TODO  **********************************/

function generateInvoice(order_id) {
	var url = getPlaceOrderUrl() + "/invoice/" + order_id;

	$.ajax({
	   url: url,
	   xhr: function () {
		const xhr = new XMLHttpRequest();
		xhr.responseType = 'blob'
		return xhr;
	  	},
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/pdf'
       },
	   success: function (data) {
		var blob = new Blob([data], {
			type: 'application/pdf'
		});

		console.log(data)

		var file = new Blob([data], { type: 'application/pdf' });
		var fileURL = URL.createObjectURL(file);
		
		setTimeout(() => {
		window.open(fileURL);
		})
		console.log(fileURL);
    },
	   error: handleAjaxError
	});

}

function viewSelectedOrders(order_id) {
	var url = getPlaceOrderUrl() + '/placed/' + order_id;
	
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
				console.log("get placed orders using order id");
				displaySinglePlacedOrder(data);
		},
		error: handleAjaxError
	 });
}

function displaySinglePlacedOrder(data) {
	console.log('Printing placed orders data ....');
	var tbody = $('#view_order-table_single').children('tbody');
	tbody.empty();
	var value_count = 1;
	for(var i in data) {
		var e = data[i];
		var row = '<tr>'
			+ '<td>' + value_count++ + '</td>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.quantity + '</td>'
			+ '<td>'  + e.selling_price + '</td>'
			+ '<td>'  + e.quantity * e.selling_price + '</td>'
			+ '</tr>';
    	tbody.append(row);
	}

	$('#edit-view_order-modal').modal('toggle');
}

/**********************************  TODO  **********************************/
/**********************************  TODO  **********************************/
/**********************************  TODO  **********************************/
/**********************************  TODO  **********************************/


//HELPER METHOD
function toJson($form) {
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    console.log(json);
    return json;
}

function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	toastr.error(response.message, "Error");
}

//INITIALIZATION CODE
function init() {
	$('#search_view_order').click(getViewOrderList);
}

$(document).ready(init);
// $(document).ready(getViewOrderList);