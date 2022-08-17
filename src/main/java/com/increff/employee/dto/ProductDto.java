package com.increff.employee.dto;

import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.model.form.ProductUpdateForm;
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
        ProductPojo pojo = DtoHelper.convertFormToPojoProduct(form);
        DtoHelper.normalizeProduct(pojo);
        boolean unique = Checks.isUnique(pojo, getAll());
        boolean brandCategoryExists = brandService.getBrandCategory(pojo) != null;

        if(unique && brandCategoryExists) {
            return DtoHelper.convertPojoToDataProduct(productService.add(pojo));
        } else {
            if(!unique) {
                throw new ApiException("Barcode should be unique");
            }
            else {
                throw new ApiException("Brand-Category doesn't exists");
            }
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

            row++;
        }

        if(!error.toString().isEmpty()) {
            throw new ApiException(error.toString());
        }

        return dataList;
    }

    public ProductData getWithId(Integer id) throws ApiException {
        ProductPojo pojo = productService.getWithId(id);
        return DtoHelper.convertPojoToDataProduct(pojo);
    }

    public ProductData get(String barcode) throws ApiException {
        ProductPojo pojo = productService.getWithBarcode(barcode);
        return DtoHelper.convertPojoToDataProduct(pojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> pojoList = productService.getAll();
        List<ProductData> dataList = new ArrayList<>();
        for (ProductPojo pojo : pojoList) {
            dataList.add(DtoHelper.convertPojoToDataProduct(pojo));
        }
        return dataList;
    }

    public ProductData update(Integer id, ProductUpdateForm form) throws ApiException {
        Checks.nullCheckForUpdateProduct(form);
        ProductPojo pojo = DtoHelper.convertFormToPojoUpdateProduct(form);
        DtoHelper.normalizeForUpdateProduct(pojo);
        checkIfBarcodeExistsInProduct(id);

        if(Checks.isUnique(id, pojo, getAll())) {
            return DtoHelper.convertPojoToDataProduct(productService.update(id, pojo));
        } else {
            throw new ApiException("Barcode should be Unique");
        }
    }

    public void checkIfBarcodeExistsInProduct(Integer id) throws ApiException {
        String actualBarcode = productService.getWithId(id).getBarcode();

        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        for(InventoryPojo pojo : inventoryPojoList) {
            if(Objects.equals(actualBarcode, pojo.getBarcode())) {
                throw new ApiException("Unable to edit, Barcode exists in Inventory");
            }
        }
    }

}
