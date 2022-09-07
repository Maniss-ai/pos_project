package com.increff.employee.dto;

import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.model.form.ProductUpdateForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.Checks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductDto {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public ProductData add(ProductForm form) throws ApiException {
        Checks.nullCheckProduct(form);
        DtoHelper.normalizeProduct(form);
        ProductPojo pojo = DtoHelper.convertFormToPojoProduct(form);
        Checks.checkLength(form);
        boolean unique = Checks.isUnique(pojo, getAll());
        Checks.isMrpNegative(pojo.getMrp());
        BrandPojo brandPojo = brandService.getBrandCategory(form.getBrand(), form.getCategory());

        pojo.setBrandCategoryId(brandPojo.getId());

        if(unique) {
            ProductData productData = DtoHelper.convertPojoToDataProduct(productService.add(pojo));
            productData.setBrand(form.getBrand());
            productData.setCategory(form.getCategory());
            return productData;
        } else {
            throw new ApiException("Barcode should be unique");
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<ProductData> bulkAddProduct(List<ProductForm> formList) throws ApiException {
        List<ProductData> dataList = new ArrayList<>();
        StringBuilder error = new StringBuilder();
        Integer row = 1;

        for(ProductForm productForm : formList) {
            try {
                dataList.add(add(productForm));
            }
            catch (Exception e) {
                error.append(row).append(": ").append(e.getMessage()).append("\n");
            }

            if(row > 5000) {
                throw new ApiException("Can't process, File contains more than 5000 rows");
            }

            row++;
        }

        if(!error.toString().isEmpty()) {
            throw new ApiException(error.toString());
        }

        return dataList;
    }

    public ProductData getWithId(Integer id) throws ApiException {
        ProductPojo productPojo = productService.getWithId(id);

        ProductData productData = DtoHelper.convertPojoToDataProduct(productPojo);
        productData.setBrand(brandService.get(productPojo.getBrandCategoryId()).getBrand());
        productData.setCategory(brandService.get(productPojo.getBrandCategoryId()).getCategory());

        return productData;
    }

    public ProductData get(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getWithBarcode(barcode);

        ProductData productData = DtoHelper.convertPojoToDataProduct(productPojo);
        productData.setBrand(brandService.get(productPojo.getBrandCategoryId()).getBrand());
        productData.setCategory(brandService.get(productPojo.getBrandCategoryId()).getCategory());

        return productData;
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> pojoList = productService.getAll();
        List<ProductData> dataList = new ArrayList<>();
        for (ProductPojo productPojo : pojoList) {
            ProductData productData = DtoHelper.convertPojoToDataProduct(productPojo);
            productData.setBrand(brandService.get(productPojo.getBrandCategoryId()).getBrand());
            productData.setCategory(brandService.get(productPojo.getBrandCategoryId()).getCategory());

            dataList.add(productData);
        }
        return dataList;
    }

    public ProductData update(Integer id, ProductUpdateForm form) throws ApiException {
        Checks.nullCheckForUpdateProduct(form);
        ProductPojo pojo = DtoHelper.convertFormToPojoUpdateProduct(form);
        DtoHelper.normalizeForUpdateProduct(pojo);
        if(form.getBarcode().length() > 20) {
            throw new ApiException("Barcode is too long");
        }

        Checks.isMrpNegative(pojo.getMrp());

        if(Checks.isUnique(id, pojo, getAll())) {
            ProductPojo productPojo = productService.update(id, pojo);

            ProductData productData = DtoHelper.convertPojoToDataProduct(productPojo);
            productData.setBrand(brandService.get(productPojo.getBrandCategoryId()).getBrand());
            productData.setCategory(brandService.get(productPojo.getBrandCategoryId()).getCategory());

            return productData;
        } else {
            throw new ApiException("Barcode should be Unique");
        }
    }

}
