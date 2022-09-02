function getViewOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order/view-order";
}

function getSubmitOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order/submit-order";
}

function getPlaceOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order/order-item";	
}

function getFormattedDate(date) {
    var year = date.year;    
	var month = (date.monthValue).toString();
    month = month.length > 1 ? month : '0' + month;  
	var day = date.dayOfMonth.toString();
    day = day.length > 1 ? day : '0' + day;    
	return day + '/' + month + '/' + year;
}

// BUTTON ACTIONS
function getViewOrderList(event) {
    // var $form = $("#view_order-form");
	// var json = toJson($form);

	var id = document.getElementById("inputOrderId").value;
	var startDate = document.getElementById("inputStartDateOrder").value;
	var endDate = document.getElementById("inputEndDateOrder").value;


	var json = { "orderId" : id , "startDate": startDate + 'T00:00:00+00:00', "endDate": endDate + 'T23:59:00+00:00' };
	json = JSON.stringify(json);

    var url = getViewOrderUrl();
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(view_order_data) {
            if(!$.trim(view_order_data)) {
				$.notify("No Orders on selected date", "info");
			}
			else {
				displaySelectedOrders(view_order_data);
			}
        },
        error: function(error) {
			dateSet();
			handleAjaxErrorViewOrder(error);
		}
    });
}

//UI DISPLAY METHODS
function displaySelectedOrders(data) {
	var tbody = $('#view_order-table').children('tbody');
	tbody.empty();
	for(var i in data) {
		var e = data[i];
		var buttonHtml = '<button class="btn btn-primary" onclick="generateInvoice(' + e.id + ')">Generate Invoice</button>'
		buttonHtml += ' <button class="btn btn-primary" onclick="viewSelectedOrders(' + e.id + ')">View Order</button>'
		var row = '<tr>'
			+ '<td>' + e.id + '</td>'
			+ '<td>' + e.time.substring(0, 10) + '</td>'
			+ '<td>'  + e.billAmount.toFixed(2) + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
    	tbody.append(row);
	}
}

function generateInvoice(order_id) {
	var url = getViewOrderUrl() + "/invoice/" + order_id;

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
		var file = new Blob([data], { type: 'application/pdf' });
		var fileURL = URL.createObjectURL(file);
		
		setTimeout(() => {
			window.open(fileURL);
		})

		$.notify("Invoice generated successfully", "success");
    },
	   error: handleAjaxErrorViewOrder
	});

}

function viewSelectedOrders(order_id) {
	var url = getViewOrderUrl() + '/placed/' + order_id;
	
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
				displaySinglePlacedOrder(data);
		},
		error: handleAjaxErrorViewOrder
	 });
}

function displaySinglePlacedOrder(data) {
	var tbody = $('#view_order-table_single').children('tbody');
	tbody.empty();
	var value_count = 1;
	for(var i in data) {
		var e = data[i];
		var row = '<tr>'
			+ '<td>' + value_count++ + '</td>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.quantity + '</td>'
			+ '<td>'  + e.sellingPrice + '</td>'
			+ '<td>'  + (e.quantity * e.sellingPrice).toFixed(2) + '</td>'
			+ '</tr>';
    	tbody.append(row);
	}

	$('#edit-view_order-modal').modal('toggle');
}

//HELPER METHOD
function toJson($form) {
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function handleAjaxErrorViewOrder(response){
	var response = JSON.parse(response.responseText);
	$.notify(response.message, {autoHide : false});
}

function dateSet() {
	var date = new Date();

	document.getElementById("inputEndDateOrder").valueAsDate = date;
	date.setMonth(date.getMonth() - 1);
	document.getElementById("inputStartDateOrder").valueAsDate = date;
}

function dateSetWithViewOrderList() {
	dateSet();
	getViewOrderList();
}

//INITIALIZATION CODE
function init() {
	$('#search_view_order').click(getViewOrderList);
}

$(document).ready(init);
$(document).ready(dateSetWithViewOrderList);