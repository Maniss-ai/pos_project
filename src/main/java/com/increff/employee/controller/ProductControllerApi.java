package com.increff.employee.controller;

import com.increff.employee.dto.ProductDto;
import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ProductControllerApi {
    @Autowired
    private ProductDto dto;

    @ApiOperation(value = "Adds an Product")
    @RequestMapping(path = "/api/product", method = RequestMethod.POST)
    public ProductData add(@RequestBody ProductForm form) throws ApiException {
        System.out.println("working");
        return dto.add(form);
    }

    @ApiOperation(value = "Adds an Brand")
    @RequestMapping(path = "/api/product/bulkAddProduct", method = RequestMethod.POST)
    public List<ProductData> bulkAddProduct(@RequestBody List<ProductForm> formList) throws ApiException {
        return dto.bulkAddProduct(formList);
    }

    @ApiOperation(value = "Deletes an Product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException {
        dto.delete(id);
    }

    @ApiOperation(value = "Gets an Product by ID")
    @RequestMapping(path = "/api/product/{barcode}", method = RequestMethod.GET)
    public ProductData get(@PathVariable String barcode) throws ApiException {
        return dto.get(barcode);
    }

    @ApiOperation(value = "Gets list of all Product")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAllFromPage() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates an Product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody ProductForm form) throws ApiException {
        dto.update(id, form);
        System.out.println(form.getMrp());
    }
}
