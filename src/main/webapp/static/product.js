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
			console.log("Product created");
			getProductList();
			toastr.success("Product added successfully", "Success");
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

	console.log("UPDATE PRODUCT : " + json);

	$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			console.log("Product update");
			getProductList();
			toastr.success("Product updated successfully", "Success");
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
			console.log("Product data fetched");
			console.log(data);
			displayProductList(data);
		},
		error: handleAjaxErrorProduct
	});
}

function deleteProduct(id) {
	var url = getProductUrl() + "/" + id;

	$.ajax({
		url: url,
		type: 'DELETE',
		success: function (data) {
			console.log("Product deleted");
			getProductList();     //...
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
			console.log("Brand-Category data fetched");
			console.log(data);
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

	if(document.getElementById("productFile").files.length == 0) {
		alert("please select a TSV file.");
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

	console.log(row);

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
	var url = getProductUrl() + "/bulkAddProduct";
	console.log(jsonArrayProduct);
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
			toastr.success("Bulk Products added successfully", "success");
	   },
	   error: function(response) {
			var lines = response.responseJSON.message.split("\n");
			console.log("RESPONSE: " + lines);

			createErrorDataProduct(lines);

			console.log(errorDataBrand);
			updateUploadDialogProduct();
	   }
	});
}

function createErrorDataProduct(lines) {
	var countRow = 1;
	var countLine = 0;

	for(var i in rowProduct) {
		console.log(i);
		if(countRow == lines[countLine][0]) {
			rowProduct[i].line_number = lines[countLine][0];
			rowProduct[i].error = lines[countLine].substring(3, lines[countLine].length);;
			errorDataProduct.push(rowProduct[i]);
			console.log("errorDataProduct : " + errorDataProduct);
			countLine++;
		}

		countRow++;
	}
}

function downloadErrorsProduct() {
	writeFileDataProduct(errorDataProduct);
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
	$('#productFileName').html(fileName);
}
/*************************************  UPLOAD DATA: END  *************************************/

//UI DISPLAY METHODS

function displayProductList(data) {
	console.log('Printing Product data');
	var tbody = $('#product-table').children('tbody');
	tbody.empty();
	var value_count = 1;
	for (var i in data) {
		var e = data[i];
		console.log(e);
		var buttonHtml = '<button class="btn btn-primary" onclick="deleteProduct(' + e.id + ')">Delete</button>'
		buttonHtml += ' <button class="btn btn-primary" onclick="displayEditProduct(' + e.id + ')">Edit</button>'
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
	console.log("working fine : " + id);
	var url = getProductUrl() + "/" + id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			console.log("product data fetched");
			console.log(data);
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
	console.log('Printing Brand-Category data');
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
		toastr.error(response.message, "Error");
	}
	catch(e) {
		toastr.error("MRP should be a Number", "Error");
	}
}

var cnt = 0;
//HELPER METHOD
function toJsonProduct($form) {
	var serialized = $form.serializeArray();
	console.log("serialized : " + serialized);
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
	console.log("WORKING FINE!!!!");
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
