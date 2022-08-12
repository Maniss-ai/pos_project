package com.increff.employee.controller;

import com.increff.employee.dto.InventoryDto;
import com.increff.employee.model.data.InventoryData;
import com.increff.employee.model.form.InventoryForm;
import com.increff.employee.model.form.InventoryUpdateForm;
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
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Adds an Inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
    public InventoryData add(@RequestBody InventoryForm inventoryForm) throws ApiException {
        return inventoryDto.add(inventoryForm);
    }

    @ApiOperation(value = "Add Inventory in Bulk")
    @RequestMapping(path = "/api/inventory/bulk-add-inventory", method = RequestMethod.POST)
    public List<InventoryData> addBulkInventory(@RequestBody List<InventoryForm> inventoryFormList) throws ApiException {
        return inventoryDto.bulkAddInventory(inventoryFormList);
    }

    @ApiOperation(value = "Deletes an Inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        inventoryDto.delete(id);
    }

    @ApiOperation(value = "Gets an Inventory by ID")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable int id) throws ApiException {
        return inventoryDto.get(id);
    }

    @ApiOperation(value = "Get list of all Inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() {
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "Updates an Inventory")
    @RequestMapping(path = "/api/inventory/{barcode}", method = RequestMethod.PUT)
    public void update(@PathVariable String barcode, @RequestBody InventoryUpdateForm form) throws ApiException {
        inventoryDto.update(barcode, form);
    }
}
