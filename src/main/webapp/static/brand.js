function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		console.log("Brand created");
	   		getBrandList();
			$.notify("Brand added successfully", "success");
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateBrand(event){
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	console.log("BRAND ID : " + id);
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	console.log("json update : " + json);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		console.log("Brand update");
	   		getBrandList();
			$.notify("Brand updated successfully", "success");
	   },
	   error: handleAjaxError
	});

	return false;
}


function getBrandList() {
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Brand data fetched");
	   		console.log(data);
	   		displayBrandList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteBrand(id){
	var url = getBrandUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		console.log("brand deleted");
	   		getBrandList();
	   },
	   error: handleAjaxError
	});
}

/******************************  FILE UPLOAD METHODS: START  ******************************/
var fileDataBrand = [];
var errorDataBrand = [];
var processCountBrand = 0;
var jsonArray = [];
var rowBrand = [];


function processDataBrand() {
	var file = $('#brandFile')[0].files[0];
	var fileName = document.querySelector('#brandFile').value;

	// check for TSV extension ...
	if(fileName.substring(fileName.length-3, fileName.length) != "tsv") {
		$.notify("Please select TSV file", "warn");
		return;
	}

	if(document.getElementById("brandFile").files.length == 0) {
		$.notify("Please select TSV file", "warn");
		resetUploadDialogBrand();
		return;
	}

	readFileDataBrand(file, readFileDataBrandCallback);
	resetUploadDialogBrand();
}

function readFileDataBrandCallback(results) {
	fileDataBrand = results.data;
	uploadRowsBrand();
}


/****************************************** CHANGES : TODO ******************************************/
function uploadRowsBrand() {
	//Update progress
	updateUploadDialogBrand();
	//If everything processed then return
	if(processCountBrand == fileDataBrand.length) {
		bulkAddBrand();
		return;
	}
	
	//Process next row
	var row = fileDataBrand[processCountBrand];
	processCountBrand++;
	rowBrand.push(row);
	console.log(rowBrand);

	var json = {}
	json["brand"] = row.brand;
	json["category"] = row.category;

	jsonArray.push(json);

	uploadRowsBrand();
}

/****************************************** BULK ADD BRAND : TODO ******************************************/
function bulkAddBrand() {
	console.log("8. WORKING FINE!!!");
	var url = getBrandUrl() + "/bulkAddBrand";
	console.log(jsonArray);
	// Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(jsonArray),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getBrandList();
			$.notify("Bulk Brands added successfully", "success");
	   },
	   error: function(response) {
			var lines = response.responseJSON.message.split("\n");
			createErrorDataBrand(lines);
			updateUploadDialogBrand();
		}
	});
}

function createErrorDataBrand(lines) {
	var countRow = 1;
	var countLine = 0;

	for(var i in rowBrand) {
		console.log(i);
		if(countRow == lines[countLine][0]) {
			rowBrand[i].line_number = lines[countLine][0];
			rowBrand[i].error = lines[countLine].substring(3, lines[countLine].length);
			errorDataBrand.push(rowBrand[i]);
			countLine++;
		}

		countRow++;
	}
}

function downloadErrorsBrand() {
	console.log("errorDataBrand   : " + errorDataBrand);
	writeFileDataBrand(errorDataBrand);
}
/******************************  FILE UPLOAD METHODS: END  ******************************/


//UI DISPLAY METHODS
/*************************************  UPLOAD DATA: START  *************************************/
function displayUploadDataBrand() {
	resetUploadDialogBrand(); 	
   $('#upload-brand-modal').modal('toggle');
}

function resetUploadDialogBrand(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	processCountBrand = 0;
	fileDataBrand = [];
	errorDataBrand = [];
	rowBrand = [];
	jsonArray = [];
	//Update counts	
	updateUploadDialogBrand();
}

function updateUploadDialogBrand(){
	$('#rowCountBrand').html("" + fileDataBrand.length);
	$('#processCountBrand').html("" + processCountBrand);
	$('#errorCountBrand').html("" + errorDataBrand.length);
}

function updateFileNameBrand(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	fileName = fileName.replace(/^.*[\\\/]/, '');
	$('#brandFileName').html(fileName);
}
/*************************************  UPLOAD DATA: END  *************************************/

function refreshData() {
	$.notify("Refreshed successfully", "success");
	getBrandList();
}

function displayBrandList(data){
	console.log('Printing brand data');
	var tbody = $('#brand-table').children('tbody');
	tbody.empty();
	var value_count = 1;
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-primary" onclick="deleteBrand(' + e.id + ')">Delete</button>'
		buttonHtml += ' <button class="btn btn-primary" onclick="displayEditBrand(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + value_count++ + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
    tbody.append(row);
	}
}

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Brand data fetched");
	   		console.log(data);
	   		displayBrand(data);
	   },
	   error: handleAjaxError
	});
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
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

function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	$.notify(response.message, {autoHide : false});
}


function readFileDataBrand(file, callback){
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


function writeFileDataBrand(arr) {
	console.log("BRAND LOG : " + arr);
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
	console.log("data : " + data);
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

//INITIALIZATION CODE
function init() {
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#refresh-data-brand').click(refreshData);

	$('#upload-data-brand').click(displayUploadDataBrand);
	$('#process-data-brand').click(processDataBrand);
	$('#download-errors-brand').click(downloadErrorsBrand);
    $('#brandFile').on('change', updateFileNameBrand)
}

$(document).ready(init);
$(document).ready(getBrandList);
