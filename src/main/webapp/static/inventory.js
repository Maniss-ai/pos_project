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
	var url = getInventoryUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getInventoryList();
			$.notify("Inventory added successfully", "success");
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

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getInventoryList();
			$.notify("Inventory updated successfully", "success");
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
				data[index].product_name = product_data.product;
				data[index].mrp = product_data.mrp;
				getBarcode(index+1, data);     //...
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

function processDataInventory() {
	var file = $('#inventoryFile')[0].files[0];
	var fileName = document.querySelector('#inventoryFile').value;

	// check for TSV extension ...
	if(fileName.substring(fileName.length-3, fileName.length) != "tsv") {
		$.notify("select TSV file", "warn");
		return;
	}

	if(document.getElementById("inventoryFile").files.length == 0) {
		$.notify("select TSV file", "warn");
		resetUploadDialogInventory();
		return;
	}
	readFileDataInventory(file, readFileDataInventoryCallback);
	resetUploadDialogInventory();
}

function readFileDataInventoryCallback(results){
	fileDataInventory = results.data;
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


	var json = {}
	json["barcode"] = row.barcode;
	json["inventory"] = row.inventory;
	jsonArrayInventory.push(json);

	uploadRowsInventory();

}

/****************************************** BULK ADD INVENTORY : TODO ******************************************/
function bulkAddInventory() {
	var url = getInventoryUrl() + "/bulk-add";
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
			$.notify("Bulk Inventory added successfully", "success");
	   },
	   error: function(response) {
			var lines = response.responseJSON.message.split("\n");
			$.notify("Error in tsv file, download errors", {autoHide : false});

			createErrorDataInventory(lines);

			updateUploadDialogInventory();
		}
	});
}

function createErrorDataInventory(lines) {
	var countRow = 1;
	var countLine = 0;

	for(var i in rowInventory) {
		if(countRow == lines[countLine][0]) {
			rowInventory[i].line_number = lines[countLine][0];
			rowInventory[i].error = lines[countLine].substring(3, lines[countLine].length);
			errorDataInventory.push(rowInventory[i]);
			countLine++;
		}

		countRow++;
	}
}

function downloadErrorsInventory() {
	if(errorDataInventory.length) {
		writeFileDataInventory(errorDataInventory);
	}
	else {
		$.notify("No errors", "info");
	}
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
	fileName = fileName.replace(/^.*[\\\/]/, '');
	$('#inventoryFileName').html(fileName);
}
/*************************************  UPLOAD DATA: END  *************************************/

//UI DISPLAY METHODS

function displayInventoryList(data) {
	var tbody = $('#inventory-table').children('tbody');
	tbody.empty();
	var value_count = 1;
	for(var i in data) {
		var e = data[i];
		var buttonHtml = ' <button class="btn btn-primary" onclick="displayEditInventory(' + e.id + ',' + e.mrp + ')">Edit</button>'
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
			data.mrp = mrp;
	   		displayInventory(data);
	   },
	   error: handleAjaxErrorInventory
	});
}

function displayInventory(data) {
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=inventory]").val(data.inventory);
	$("#inventory-edit-form input[name=mrp]").val(data.mrp);
	$('#edit-inventory-modal').modal('toggle');
}

function displayBarcodeList(data) {
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
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
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
		$.notify(response.message, {autoHide : false});
	}
	catch(e) {
		$.notify("Quantity should be an Integer", {autoHide : false});
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
