package com.increff.employee.controller;

import com.increff.employee.dto.BrandDto;
import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class BrandControllerApi {

    @Autowired
    private BrandDto dto;

    @ApiOperation(value = "Adds a Brand")
    @RequestMapping(path = "/api/brand", method = RequestMethod.POST)
    public BrandData add(@RequestBody BrandForm brandForm) throws ApiException {
        return dto.add(brandForm);
    }

    @ApiOperation(value = "Add Brands in bulk")
    @RequestMapping(path = "/api/brand/bulk-add-brand", method = RequestMethod.POST)
    public List<BrandData> bulkAddBrand(@RequestBody List<BrandForm> brandFormList) throws ApiException {
        return dto.bulkAddBrand(brandFormList);
    }

    @ApiOperation(value = "Deletes an Brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException {
        dto.delete(id);
    }

    @ApiOperation(value = "Gets a Brand by ID")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Get list of all Brands")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return dto.getAll();
    }

    @ApiOperation(value = "Update a Brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
    public BrandData update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException {
        return dto.update(id, form);
    }

}
