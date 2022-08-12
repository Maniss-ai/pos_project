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
public class ProductControllerApi {
    @Autowired
    private ProductDto dto;

    @ApiOperation(value = "Add a product")
    @RequestMapping(path = "/api/product", method = RequestMethod.POST)
    public ProductData add(@RequestBody ProductForm form) throws ApiException {
        return dto.add(form);
    }

    @ApiOperation(value = "Add products in bulk")
    @RequestMapping(path = "/api/product/bulk-add-product", method = RequestMethod.POST)
    public List<ProductData> bulkAddProduct(@RequestBody List<ProductForm> productFormList) throws ApiException {
        return dto.bulkAddProduct(productFormList);
    }

    @ApiOperation(value = "Deletes a product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException {
        dto.delete(id);
    }

    @ApiOperation(value = "Gets a product by Id")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable int id) throws ApiException {
        return dto.getWithId(id);
    }

    @ApiOperation(value = "Gets a product by barcode")
    @RequestMapping(path = "/api/product/barcode/{barcode}", method = RequestMethod.GET)
    public ProductData getWithBarcode(@PathVariable String barcode) throws ApiException {
        return dto.get(barcode);
    }

    @ApiOperation(value = "Get list of all products")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates a Product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
    public ProductData update(@PathVariable int id, @RequestBody ProductUpdateForm form) throws ApiException {
        return dto.update(id, form);
    }
}
