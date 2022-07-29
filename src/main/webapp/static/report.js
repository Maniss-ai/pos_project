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

/**************************  GENERATE REPORTS  **************************/

function generateSalesReport(event) {
	var $form = $("#report-form");
	var json = toJson($form);
	console.log("SALES REPORT: JSON OBJECT: " + json);

    var url = getSalesReportUrl();
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(data) {
            console.log("Getting Sales Reports ....");
            console.log(data);
			writeFileDataReport(data);
        },
        error: handleAjaxError
    });
}

function generateBrandReport(event) {
    var url = getBrandReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Brand Report fetched");
			console.log(data);
	   		writeFileDataReport(data);
	   },
	   error: handleAjaxError
	});
}

function generateInventoryReport(event) {
    var url = getInventoryReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Inventory Report fetched");
	   		console.log(data);
			writeFileDataReport(data);
	   },
	   error: handleAjaxError
	});
}

/**************************  HELPER FUNCTIONS  **************************/

function writeFileDataReport(data) {
	console.log("REPORT DATA : " + data);
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

/**************************  INITIALIZATION CODE  **************************/
function init() {
	$('#generate_brand_report').click(generateBrandReport);
    $('#generate_inventory_report').click(generateInventoryReport);
	$('#generate_sales_report').click(generateSalesReport);
}

$(document).ready(init);