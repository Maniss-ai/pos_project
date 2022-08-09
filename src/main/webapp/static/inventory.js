function getInventoryUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function getProductUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

//BUTTON ACTIONS
function addInventory(event) {
	//Set the values to update
	var $form = $("#inventory-form");
	var json = toJson($form);
	console.log("json object: " + json);
	var url = getInventoryUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		console.log("inventory created :: " + response[0]);
	   		getInventoryList();
			toastr.success("Inventory added successfully", "success");
	   },
	   error: handleAjaxErrorInventory
	});

	return false;
}

function updateInventory(event) {
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var barcode = $("#inventory-edit-form input[name=barcode]").val();
	var url = getInventoryUrl() + "/" + barcode;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);
	console.log(json);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		console.log("inventory update");
	   		getInventoryList();
			toastr.success("Inventory updated successfully", "success");
	   },
	   error: handleAjaxErrorInventory
	});

	return false;
}

function getInventoryList() {

	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Inventory data fetched");
			console.log(data);
			getBarcode(0, data);
	   },
	   error: handleAjaxErrorInventory
	});
}

function getBarcode(index, data) {
	if(index == data.length) {
		displayInventoryList(data);
		return;
	}
	
	var url = getProductUrl() + '/barcode/' + data[index].barcode;
	$.ajax({
		url: url,
		type: 'GET',
		success: function(product_data) {
				console.log("Barcode data fetched");
				console.log(product_data);
				data[index].product_name = product_data.product;
				data[index].mrp = product_data.mrp;
				getBarcode(index+1, data);     //...
		},
		error: handleAjaxErrorInventory
	});
}


function deleteInventory(id) {
	var url = getInventoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		console.log("inventory deleted");
	   		getInventoryList();     //...
	   },
	   error: handleAjaxErrorInventory
	});
}

function getBarcodeList(event) {
	var url = getProductUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
				console.log("Barcode List data fetched");
				console.log(data);
				displayBarcodeList(data);     //...
		},
		error: handleAjaxErrorInventory
	});
}


/******************************  FILE UPLOAD METHODS: START  ******************************/
var fileDataInventory = [];
var errorDataInventory = [];
var processCountInventory = 0;
var jsonArrayInventory = [];
var rowInventory = [];

function processDataInventory(){
	var file = $('#inventoryFile')[0].files[0];

	if(document.getElementById("inventoryFile").files.length == 0) {
		alert("please select a TSV file.");
		resetUploadDialogInventory();
		return;
	}
	console.log("WORKING 1.");
	readFileDataInventory(file, readFileDataInventoryCallback);
	resetUploadDialogInventory();
}

function readFileDataInventoryCallback(results){
	fileDataInventory = results.data;
	console.log("WORKING 2.");
	uploadRowsInventory();
}

/****************************************** CHANGES : TODO ******************************************/
function uploadRowsInventory() {
	//Update progress
	updateUploadDialogInventory();
	//If everything processed then return
	if(processCountInventory == fileDataInventory.length) {
		bulkAddInventory();
		return;
	}
	
	//Process next row
	var row = fileDataInventory[processCountInventory];
	processCountInventory++;
	rowInventory.push(row);
	
	// var json = JSON.stringify(row);

	console.log(row);

	var json = {}
	json["barcode"] = row.barcode;
	json["inventory"] = row.inventory;
	jsonArrayInventory.push(json);

	uploadRowsInventory();

}

/****************************************** BULK ADD INVENTORY : TODO ******************************************/
function bulkAddInventory() {
	var url = getInventoryUrl() + "/bulkAddInventory";
	console.log(jsonArrayInventory);
	// Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(jsonArrayInventory),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getInventoryList();
			toastr.success("Bulk Inventory added successfully", "success");
	   },
	   error: function(response) {
			console.log(response);
			var lines = response.responseJSON.message.split("\n");
			console.log("RESPONSE: " + lines);

			createErrorDataInventory(lines);

			console.log(errorDataInventory);
			updateUploadDialogInventory();
		}
	});
}

function createErrorDataInventory(lines) {
	var countRow = 1;
	var countLine = 0;

	for(var i in rowInventory) {
		console.log(i);
		if(countRow == lines[countLine][0]) {
			rowInventory[i].line_number = lines[countLine][0];
			rowInventory[i].error = lines[countLine].substring(3, lines[countLine].length);
			errorDataInventory.push(rowInventory[i]);
			console.log("errorDataBrand : " + errorDataInventory);
			countLine++;
		}

		countRow++;
	}
}

function downloadErrorsInventory(){
	writeFileDataInventory(errorDataInventory);
}
/******************************  FILE UPLOAD METHODS: END  ******************************/


//UI DISPLAY METHODS
/*************************************  UPLOAD DATA: START  *************************************/
function displayUploadDataInventory(){
	resetUploadDialogInventory(); 	
   $('#upload-inventory-modal').modal('toggle');
}

function resetUploadDialogInventory(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	//Reset various counts
	processCountInventory = 0;
	fileDataInventory = [];
	errorDataInventory = [];
	rowInventory = [];
	jsonArrayInventory = [];
	//Update counts	
	updateUploadDialogInventory();
}

function updateUploadDialogInventory(){
	$('#rowCountInventory').html("" + fileDataInventory.length);
	$('#processCountInventory').html("" + processCountInventory);
	$('#errorCountInventory').html("" + errorDataInventory.length);
}

function updateFileNameInventory(){
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}
/*************************************  UPLOAD DATA: END  *************************************/

//UI DISPLAY METHODS

function displayInventoryList(data) {
	console.log('Printing inventory data');
	var tbody = $('#inventory-table').children('tbody');
	tbody.empty();
	var value_count = 1;
	for(var i in data) {
		var e = data[i];
		console.log(e);
		var buttonHtml = '<button class="btn btn-primary" onclick="deleteInventory(' + e.id + ')">Delete</button>'
		buttonHtml += ' <button class="btn btn-primary" onclick="displayEditInventory(' + e.id + ',' + e.mrp + ')">Edit</button>'
		var row = '<tr>'
			+ '<td>' + value_count++ + '</td>'
			+ '<td>' + e.product_name + '</td>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>'  + e.inventory + '</td>'
			+ '<td>'  + e.mrp.toFixed(2) + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
    	tbody.append(row);
	}
}

function displayEditInventory(id, mrp) {
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("inventory data fetched");
			data.mrp = mrp;
	   		displayInventory(data);
	   },
	   error: handleAjaxErrorInventory
	});
}

function displayInventory(data) {
	console.log("BARCODE VALUE:" + data.barcode)
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=inventory]").val(data.inventory);
	$("#inventory-edit-form input[name=mrp]").val(data.mrp);
	$('#edit-inventory-modal').modal('toggle');
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

		option.value = val.id;
		option.innerHTML = val.barcode;

		barcode.append(option);
	}
}


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


function readFileDataInventory(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileDataInventory(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}

function handleAjaxErrorInventory(response) {
	try {
		var response = JSON.parse(response.responseText);
		toastr.error(response.message, "Error");
	}
	catch(e) {
		toastr.error("Quantity should be an Integer", "Error");
	}
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
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data-inventory').click(getInventoryList);

	$('#upload-data-inventory').click(displayUploadDataInventory);
	$('#process-data-inventory').click(processDataInventory);
	$('#download-errors-inventory').click(downloadErrorsInventory);
    $('#inventoryFile').on('change', updateFileNameInventory)
}

$(document).ready(init);
$(document).ready(getInventoryList);
$(document).ready(getBarcodeList);
