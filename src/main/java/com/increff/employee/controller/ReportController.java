package com.increff.employee.controller;

import com.increff.employee.dto.ReportDto;
import com.increff.employee.model.ReportForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class ReportController {
    @Autowired
    private ReportDto dto;

    @ApiOperation(value = "Generate Sales Report")
    @RequestMapping(path = "/api/report/sales", method = RequestMethod.POST)
    public StringBuilder salesReport(@RequestBody ReportForm form) throws ApiException {
        System.out.println(form.getStart_date());
        return dto.getAllSales(form);
    }

    @ApiOperation(value = "Generate Brand Report")
    @RequestMapping(path = "/api/report/brand", method = RequestMethod.GET)
    public StringBuilder brandReport() {
        return dto.getAllBrand();
    }

    @ApiOperation(value = "Generate Inventory Report")
    @RequestMapping(path = "/api/report/inventory", method = RequestMethod.GET)
    public StringBuilder inventoryReport() throws ApiException {
        return dto.getAllInventory();
    }
}
