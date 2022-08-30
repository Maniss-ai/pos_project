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
public class BrandController {

    @Autowired
    private BrandDto dto;

    @ApiOperation(value = "Add a Brand")
    @RequestMapping(path = "/api/brand", method = RequestMethod.POST)
    public BrandData add(@RequestBody BrandForm brandForm) throws ApiException {
        return dto.add(brandForm);
    }

    @ApiOperation(value = "Add Brands in bulk")
    @RequestMapping(path = "/api/brand/bulk-add", method = RequestMethod.POST)
    public List<BrandData> bulkAddBrand(@RequestBody List<BrandForm> brandFormList) throws ApiException {
        return dto.bulkAddBrand(brandFormList);
    }

    @ApiOperation(value = "Get a Brand by ID")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable Integer id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Get list of all Brands")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return dto.getAll();
    }

    @ApiOperation(value = "Update a Brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
    public BrandData update(@PathVariable Integer id, @RequestBody BrandForm form) throws ApiException {
        return dto.update(id, form);
    }

}
