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
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
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

    static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    // CRUD ....
    public ProductData add(ProductForm form) throws ApiException {
        nullCheck(form);
        ProductPojo pojo = convertFormToPojo(form);
        normalize(pojo);
        boolean unique = isUnique(pojo);
        boolean brand_category_exists = isBrandCategoryExists(pojo);

        if(unique && brand_category_exists) {
            return convertPojoToData(productService.add(pojo));
        } else {
            if(!unique) {
                throw new ApiException("Barcode should be unique");
            }
            else {
                throw new ApiException("Brand-Category doesn't exists mann");
            }
        }
    }

    private boolean isBrandCategoryExists(ProductPojo pojo) throws ApiException {
        return brandService.getBrandCategory(pojo) != null;
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<ProductData> bulkAddProduct(List<ProductForm> formList) throws ApiException {
        List<ProductData> dataList = new ArrayList<>();
        StringBuilder error = new StringBuilder();
        int row = 1;

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

    public void delete(int id) throws ApiException {
        if(!idExistsInProductPojo(id)) {
            productService.delete(id);
        }
        else {
            throw new ApiException("Unable to delete, Id exists in inventory");
        }
    }

    private boolean idExistsInProductPojo(int id) {
        return false;
    }

    public ProductData getWithId(int id) throws ApiException {
        ProductPojo pojo = productService.getWithId(id);
        return convertPojoToData(pojo);
    }

    public ProductData get(String barcode) throws ApiException {
        ProductPojo pojo = productService.getWithBarcode(barcode);
        return convertPojoToData(pojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> pojoList = productService.getAll();
        List<ProductData> dataList = new ArrayList<>();
        for (ProductPojo pojo : pojoList) {
            dataList.add(convertPojoToData(pojo));
        }
        return dataList;
    }

    public void update(int id, ProductUpdateForm form) throws ApiException {
        nullCheckForUpdate(form);
        ProductPojo pojo = convertFormToPojoUpdate(form);
        checkIfBarcodeExistsInProduct(id);

        if(isUnique(id, pojo)) {
            productService.update(id, pojo);
        } else {
            throw new ApiException("Barcode should be Unique");
        }
    }


    // CHECKS ....
    boolean isUnique(ProductPojo pojo) throws ApiException {
        List<ProductData> dataList = getAll();
        for(ProductData productData : dataList) {
            if(Objects.equals(productData.getBarcode(), pojo.getBarcode())) {
                return false;
            }
        }
        return true;
    }

    boolean isUnique(int current_id, ProductPojo pojo) throws ApiException {
        List<ProductData> dataList = getAll();
        for(ProductData productData : dataList) {
            if(Objects.equals(productData.getBarcode(), pojo.getBarcode()) && productData.getId() != current_id) {
                return false;
            }
        }
        return true;
    }

    public void checkIfBarcodeExistsInProduct(int id) throws ApiException {
        String actualBarcode = productService.getWithId(id).getBarcode();

        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        for(InventoryPojo pojo : inventoryPojoList) {
            if(Objects.equals(actualBarcode, pojo.getBarcode())) {
                throw new ApiException("Unable to edit, Barcode exists in Inventory");
            }
        }
    }

    // MODIFYING ....

    public static void normalize(ProductPojo p) {
        p.setBarcode(p.getBarcode().toLowerCase().trim());
        p.setProduct(p.getProduct().toLowerCase().trim());
        p.setBrand(p.getBrand().toLowerCase().trim());
        p.setCategory(p.getCategory().toLowerCase().trim());
    }

    // CONVERSION ....
    protected static ProductData convertPojoToData(ProductPojo pojo) {
        ProductData data = new ProductData();
        data.setBarcode(pojo.getBarcode());
        data.setBrand(pojo.getBrand());
        data.setCategory(pojo.getCategory());
        data.setProduct(pojo.getProduct());
        data.setMrp(Precision.round(pojo.getMrp(), 2));
        data.setId(pojo.getId());
        return data;
    }
    protected static ProductPojo convertFormToPojo(ProductForm form) {
        ProductPojo pojo = new ProductPojo();
        pojo.setBrand(form.getBrand());
        pojo.setCategory(form.getCategory());
        pojo.setBarcode(form.getBarcode());
        pojo.setProduct(form.getProduct());
        pojo.setMrp(Precision.round(form.getMrp(), 2));
        return pojo;
    }

    protected static ProductPojo convertFormToPojoUpdate(ProductUpdateForm form) {
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode(form.getBarcode());
        pojo.setProduct(form.getProduct());
        pojo.setMrp(Precision.round(form.getMrp(), 2));
        return pojo;
    }

    private void nullCheckForUpdate(ProductUpdateForm form) throws ApiException {
        if(form.getBarcode().isEmpty() || form.getBarcode() == null) {
            throw new ApiException("Barcode can't be empty");
        }
        else if(form.getProduct().isEmpty() || form.getProduct() == null) {
            throw new ApiException("Product name can't be empty");
        }
        else if(form.getMrp() == null || form.getMrp() == 0) {
            throw new ApiException("MRP can't be empty");
        }
    }
    private void nullCheck(ProductForm form) throws ApiException {
        System.out.println("VALUE : " + form.getBarcode());
        if(form.getBarcode().isEmpty() || form.getBarcode() == null) {
            System.out.println("NULL CHECK Wokring!!!");
            throw new ApiException("Barcode can't be empty");
        }
        else if(form.getProduct().isEmpty() || form.getProduct() == null) {
            throw new ApiException("Product name can't be empty");
        }
        else if(form.getMrp() == null || form.getMrp() == 0) {
            throw new ApiException("MRP can't be empty");
        }
    }
}
