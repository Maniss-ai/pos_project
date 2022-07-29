package com.increff.employee.controller;

import com.increff.employee.dto.BrandDto;
import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
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

    @ApiOperation(value = "Adds an Brand")
    @RequestMapping(path = "/api/brand", method = RequestMethod.POST)
    public BrandData add(@RequestBody BrandForm form) throws ApiException {
        return dto.add(form);
    }

    @ApiOperation(value = "Adds an Brand")
    @RequestMapping(path = "/api/brand/bulkAddBrand", method = RequestMethod.POST)
    public List<BrandData> bulkAddBrand(@RequestBody List<BrandForm> formList) throws ApiException {
        return dto.bulkAddBrand(formList);
    }

    @ApiOperation(value = "Deletes an Brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException {
        dto.delete(id);
    }

    @ApiOperation(value = "Gets an Brand by ID")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all Brand")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAllFromPage() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates an Brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
    public BrandData update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException {
        return dto.update(id, form);
    }

}
