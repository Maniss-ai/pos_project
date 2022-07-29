package com.increff.employee.dto;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
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

    // CRUD ....
    public ProductData add(ProductForm form) throws ApiException {
        ProductPojo pojo = convertFormToPojo(form);
        normalize(pojo);
        boolean unique = isUnique(pojo);
        boolean not_empty = isNotEmpty(pojo);
        boolean brand_category_exists = isBrandCategoryExists(pojo);

        if(unique && not_empty && brand_category_exists) {
            return convertPojoToData(productService.add(pojo));
        } else {
            if(!unique) {
                throw new ApiException("Barcode should be unique ....");
            }
            else if(!not_empty) {
                throw new ApiException("Product info can't be empty ....");
            }
            else {
                throw new ApiException("Brand-Category doesn't exists ....");
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
            throw new ApiException("Unable to delete, Id exists in inventory ....");
        }
    }

    private boolean idExistsInProductPojo(int id) {
        List<InventoryPojo> list = inventoryService.getAll();
        for(InventoryPojo pojo : list) {
            return true;
        }
        return false;
    }

    public ProductData get(String barcode) throws ApiException {
        ProductPojo pojo = productService.get(barcode);
        return convertPojoToData(pojo);
    }

    public List<ProductData> getAll() {
        List<ProductPojo> pojoList = productService.getAll();
        List<ProductData> dataList = new ArrayList<>();
        for (ProductPojo pojo : pojoList) {
            dataList.add(convertPojoToData(pojo));
        }
        return dataList;
    }

    public void update(int id, ProductForm form) throws ApiException {
        ProductPojo pojo = convertFormToPojo(form);
        boolean unique = isUnique(id, pojo);
        boolean not_empty = isNotEmpty(pojo);

        if(unique && not_empty) {
            productService.update(id, pojo);
        } else {
            if(!unique) {
                throw new ApiException("Barcode should be Unique");
            }
            else {
                throw new ApiException("Product info can't be Empty");
            }
        }
    }

    // CHECKS ....

    boolean isUnique(ProductPojo pojo) {
        List<ProductData> dataList = getAll();
        for(ProductData productData : dataList) {
            if(Objects.equals(productData.getBarcode(), pojo.getBarcode())) {
                return false;
            }
        }
        return true;
    }

    boolean isUnique(int current_id, ProductPojo pojo) {
        List<ProductData> dataList = getAll();
        for(ProductData productData : dataList) {
            if(Objects.equals(productData.getBarcode(), pojo.getBarcode()) && productData.getId() != current_id) {
                return false;
            }
        }
        return true;
    }

    public boolean isNotEmpty(ProductPojo pojo) {
        return !pojo.getBarcode().isEmpty() && !pojo.getProduct().isEmpty() && pojo.getMrp() != 0;
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
        data.setMrp(pojo.getMrp());
        data.setId(pojo.getId());
        return data;
    }

    protected static ProductPojo convertFormToPojo(ProductForm form) {
        ProductPojo pojo = new ProductPojo();
        pojo.setBrand(form.getBrand());
        pojo.setCategory(form.getCategory());
        pojo.setBarcode(form.getBarcode());
        pojo.setProduct(form.getProduct());
        pojo.setMrp(form.getMrp());
        return pojo;
    }
}
