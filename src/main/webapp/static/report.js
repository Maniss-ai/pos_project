function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/sales";
}

function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/brand";
}

function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/inventory";
}

function generateSalesReport(event) {
	// var $form = $("#report-form");
	// var json = toJson($form);

	var brand = document.getElementById("inputBrand").value;
	var category = document.getElementById("inputCategory").value;
	var startDate = document.getElementById("inputStartDateReport").value;
	var endDate = document.getElementById("inputEndDateReport").value;

	var json = { "brand" : brand, "category" : category , "startDate": startDate + 'T00:00:00+00:00', "endDate": endDate + 'T23:59:00+00:00' };
	json = JSON.stringify(json);

    var url = getSalesReportUrl();
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(data) {
			// if(data.split("\n").length <= 1) {
			// 	$.notify("Empty sales report for selected date", "info");
			// 	return;
			// }
			writeFileDataReport(data);
			$.notify("Sales Report generated successfully", "success");
        },
        error: handleAjaxErrorReport
    });
}

function generateBrandReport(event) {
    var url = getBrandReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		writeFileDataReport(data);
			$.notify("Brand Report generated successfully", "success");
	   },
	   error: handleAjaxErrorReport
	});
}

function generateInventoryReport(event) {
    var url = getInventoryReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
			writeFileDataReport(data);
			$.notify("Inventory Report generated successfully", "success");
	   },
	   error: handleAjaxErrorReport
	});
}

/**************************  HELPER FUNCTIONS  **************************/

function writeFileDataReport(data) {
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'report.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'report.tsv');
    tempLink.click(); 
}

function handleAjaxErrorReport(response){
	var response = JSON.parse(response.responseText);
	$.notify(response.message, {autoHide : false});
}

function dateSetReport() {
	var date = new Date();
	var year = date.getFullYear();
	var monthStart = String(date.getMonth()).padStart(2,'0');
	var monthEnd = String(date.getMonth()+1).padStart(2,'0');
	var todayDate = String(date.getDate()).padStart(2,'0');

	var datePatternStart = year + '-' + monthStart + '-' + todayDate;
	var datePatternEnd = year + '-' + monthEnd + '-' + todayDate;

	document.getElementById("inputStartDateReport").value = datePatternStart;
	document.getElementById("inputEndDateReport").value = datePatternEnd;
}

/**************************  INITIALIZATION CODE  **************************/
function init() {
	$('#generate_brand_report').click(generateBrandReport);
    $('#generate_inventory_report').click(generateInventoryReport);
	$('#generate_sales_report').click(generateSalesReport);
}

$(document).ready(init);
$(document).ready(dateSetReport);