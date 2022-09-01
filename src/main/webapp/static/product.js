function getProductUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getBrandUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addProduct(event) {
	//Set the values to update
	var $form = $("#product-form");
	var json = toJsonProduct($form);
	var url = getProductUrl();

	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			getProductList();
			$.notify("Product added successfully", "success");
		},
		error: handleAjaxErrorProduct
	});

	return false;
}

function updateProduct(event) {
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJsonProduct($form);

	$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			getProductList();
			$.notify("Product updated successfully", "success");
		},
		error: handleAjaxErrorProduct
	});

	return false;
}



function getProductList() {
	var url = getProductUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayProductList(data);
		},
		error: handleAjaxErrorProduct
	});
}

function getBrandCategoryList(event) {
	var url = getBrandUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayBrandCategoryList(data);     //...
		},
		error: handleAjaxErrorProduct
	});
}

/******************************  FILE UPLOAD METHODS: START  ******************************/
var fileDataProduct = [];
var errorDataProduct = [];
var processCountProduct = 0;
var jsonArrayProduct = [];
var rowProduct = [];


function processDataProduct() {
	var file = $('#productFile')[0].files[0];
	var fileName = document.querySelector('#productFile').value;

	// check for TSV extension ...
	if(fileName.substring(fileName.length-3, fileName.length) != "tsv") {
		$.notify("Please select TSV file", "warn");
		return;
	}

	if(document.getElementById("productFile").files.length == 0) {
		$.notify("Please select TSV file", "warn");
		resetUploadDialogProduct();
		return;
	}

	readFileDataProduct(file, readFileDataProductCallback);
	resetUploadDialogProduct();
}

function readFileDataProductCallback(results) {
	fileDataProduct = results.data;
	uploadRowsProduct();
}

/****************************************** CHANGES : TODO ******************************************/
function uploadRowsProduct() {
	//Update progress
	updateUploadDialogProduct();
	//If everything processed then return
	if (processCountProduct == fileDataProduct.length) {
		bulkAddProduct();
		return;
	}

	//Process next row
	var row = fileDataProduct[processCountProduct];
	processCountProduct++;
	rowProduct.push(row);


	var json = {}
	json["barcode"] = row.barcode;
	json["brand"] = row.brand;
	json["category"] = row.category;
	json["product"] = row.product;
	json["mrp"] = row.mrp;

	jsonArrayProduct.push(json);

	uploadRowsProduct();

}

/****************************************** BULK ADD BRAND : TODO ******************************************/
function bulkAddProduct() {
	var url = getProductUrl() + "/bulk-add";
	// Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(jsonArrayProduct),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductList();
			$.notify("Bulk Products added successfully", "success");
	   },
	   error: function(response) {
			var lines = response.responseJSON.message.split("\n");

			$.notify("Error in tsv file, please download errors", {autoHide : false});
			createErrorDataProduct(lines);

			updateUploadDialogProduct();
	   }
	});
}

function createErrorDataProduct(lines) {
	var countRow = 1;
	var countLine = 0;

	for(var i in rowProduct) {
		if(countRow == lines[countLine][0]) {
			rowProduct[i].line_number = lines[countLine][0];
			rowProduct[i].error = lines[countLine].substring(3, lines[countLine].length);;
			errorDataProduct.push(rowProduct[i]);
			countLine++;
		}

		countRow++;
	}
}

function downloadErrorsProduct() {
	if(errorDataProduct.length) {
		writeFileDataProduct(errorDataProduct);
	}
	else {
		$.notify("No errors", "info");
	}
}
/******************************  FILE UPLOAD METHODS: END  ******************************/


//UI DISPLAY METHODS
/*************************************  UPLOAD DATA: START  *************************************/
function displayUploadDataProduct() {
	resetUploadDialogProduct();
	$('#upload-product-modal').modal('toggle');
}

function resetUploadDialogProduct() {
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
	//Reset various counts
	processCountProduct = 0;
	fileDataProduct = [];
	errorDataProduct = [];
	rowProduct = [];
	jsonArrayProduct = [];
	//Update counts	
	updateUploadDialogProduct();
}

function updateUploadDialogProduct() {
	$('#rowCountProduct').html("" + fileDataProduct.length);
	$('#processCountProduct').html("" + processCountProduct);
	$('#errorCountProduct').html("" + errorDataProduct
		.length);
}

function updateFileNameProduct() {
	var $file = $('#productFile');
	var fileName = $file.val();
	fileName = fileName.replace(/^.*[\\\/]/, '');
	$('#productFileName').html(fileName);
}
/*************************************  UPLOAD DATA: END  *************************************/

//UI DISPLAY METHODS

function displayProductList(data) {
	var tbody = $('#product-table').children('tbody');
	tbody.empty();
	var value_count = 1;
	for (var i in data) {
		var e = data[i];
		var buttonHtml = ' <button class="btn btn-primary" onclick="displayEditProduct(' + e.id + ')">Edit</button>'
		var row = '<tr>'
			+ '<td>' + value_count++ + '</td>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.brand + '</td>'
			+ '<td>' + e.category + '</td>'
			+ '<td>' + e.product + '</td>'
			+ '<td>' + e.mrp.toFixed(2) + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		tbody.append(row);
	}
}

function displayEditProduct(id) {
	var url = getProductUrl() + "/" + id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayProduct(data);
		},
		error: handleAjaxErrorProduct
	});
}

function displayProduct(data) {
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=product]").val(data.product);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}

function displayBrandCategoryList(data) {
	var selector = $('#selector');
	selector.empty();

	var selected_option = document.createElement("option");
	selected_option.value = 0;
	selected_option.innerHTML = "Choose...";
	selector.append(selected_option);

	for (var i in data) {
		var val = data[i];
		var option = document.createElement("option");

		option.value = val.id;
		option.innerHTML = val.brand + '-' + val.category;

		selector.append(option);
	}
}

function handleAjaxErrorProduct(response){
	try {
		var response = JSON.parse(response.responseText);
		$.notify(response.message, {autoHide : false});
	}
	catch(e) {
		$.notify("MRP should be a Number", {autoHide : false});
	}
}

var cnt = 0;
//HELPER METHOD
function toJsonProduct($form) {
	var serialized = $form.serializeArray();
	var s = '';
	var data = {};
	for (s in serialized) {
		data[serialized[s]['name']] = serialized[s]['value']
	}

	var json = JSON.stringify(data);
	return json;
}


function readFileDataProduct(file, callback) {
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function (results) {
			callback(results);
		}
	}
	Papa.parse(file, config);
}


function writeFileDataProduct(arr) {
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};

	var data = Papa.unparse(arr, config);
	var blob = new Blob([data], { type: 'text/tsv;charset=utf-8;' });
	var fileUrl = null;

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

function onlyNumberkey(evt) {
	// Only ASCII character in that range allowed
	var ASCIICode = (evt.which) ? evt.which : evt.keyCode
	if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
		return false;
	return true;
}


function onlyNumber(evt) {
	// Only ASCII character in that range allowed
	var ASCIICode = (evt.which) ? evt.which : evt.keyCode
	if(ASCIICode == 46) {
		return true;
	}
	if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
		return false;
	return true;
}

//INITIALIZATION CODE
function init() {
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data-product').click(getProductList);

	$('#upload-data-product').click(displayUploadDataProduct);
	$('#process-data-product').click(processDataProduct);
	$('#download-errors-product').click(downloadErrorsProduct);
	$('#productFile').on('change', updateFileNameProduct)
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandCategoryList);
