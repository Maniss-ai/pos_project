package com.increff.employee.controller;

import com.increff.employee.dto.InventoryDto;
import com.increff.employee.model.data.InventoryData;
import com.increff.employee.model.form.InventoryForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class InventoryControllerApi {
    @Autowired
    private InventoryDto dto;

    @ApiOperation(value = "Adds an Inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
    public InventoryData add(@RequestBody InventoryForm form) throws ApiException {
        return dto.add(form);
    }

    @ApiOperation(value = "Adds an Inventory")
    @RequestMapping(path = "/api/inventory/bulkAddInventory", method = RequestMethod.POST)
    public List<InventoryData> addBulkInventory(@RequestBody List<InventoryForm> formList) throws ApiException {
        System.out.println("WORKING FINE JAVA CODE::::");
        return dto.bulkAddInventory(formList);
    }

    @ApiOperation(value = "Deletes an Inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        dto.delete(id);
    }

    @ApiOperation(value = "Gets an Inventory by ID")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all Inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAllFromPage() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates an Inventory")
    @RequestMapping(path = "/api/inventory/{barcode}", method = RequestMethod.PUT)
    public void update(@PathVariable String barcode, @RequestBody InventoryForm form) throws ApiException {
        dto.update(barcode, form);
    }
}
