package com.increff.employee.controller;

import com.increff.employee.dto.ProductDto;
import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.model.form.ProductUpdateForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ProductController {
    @Autowired
    private ProductDto dto;

    @ApiOperation(value = "Add a product")
    @RequestMapping(path = "/api/product", method = RequestMethod.POST)
    public ProductData add(@RequestBody ProductForm form) throws ApiException {
        return dto.add(form);
    }

    @ApiOperation(value = "Add products in bulk")
    @RequestMapping(path = "/api/product/bulk-add", method = RequestMethod.POST)
    public List<ProductData> bulkAddProduct(@RequestBody List<ProductForm> productFormList) throws ApiException {
        return dto.bulkAddProduct(productFormList);
    }

    @ApiOperation(value = "Get a product by Id")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Integer id) throws ApiException {
        return dto.getWithId(id);
    }

    @ApiOperation(value = "Get a product by barcode")
    @RequestMapping(path = "/api/product/barcode/{barcode}", method = RequestMethod.GET)
    public ProductData getWithBarcode(@PathVariable String barcode) throws ApiException {
        return dto.get(barcode);
    }

    @ApiOperation(value = "Get list of all products")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException {
        return dto.getAll();
    }

    @ApiOperation(value = "Update a Product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
    public ProductData update(@PathVariable Integer id, @RequestBody ProductUpdateForm form) throws ApiException {
        return dto.update(id, form);
    }
}
