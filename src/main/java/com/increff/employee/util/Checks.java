package com.increff.employee.util;

import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.data.InventoryData;
import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.form.*;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;

import java.util.List;
import java.util.Objects;

public class Checks {
    // Brand Checks
    public static void nullCheckBrand(BrandForm form) throws ApiException {
        if(form.getBrand() == null || form.getBrand().isEmpty()) {
            throw new ApiException("Brand can't be null");
        }
        else if(form.getCategory() == null || form.getCategory().isEmpty()) {
            throw new ApiException("Category can't be null");
        }
    }

    public static boolean isUniqueBrand(int current_id, BrandPojo pojo, List<BrandData> dataList) {
        for(BrandData pojoItem : dataList) {
            if(
                    Objects.equals(pojoItem.getBrand(), pojo.getBrand()) &&
                            Objects.equals(pojoItem.getCategory(), pojo.getCategory()) &&
                            pojoItem.getId() != current_id
            ) {
                return false;
            }
        }

        return true;
    }

    public static boolean isUniqueBrand(BrandPojo pojo, List<BrandData> dataList) {
        for(BrandData pojoItem : dataList) {
            if(
                    Objects.equals(pojoItem.getBrand(), pojo.getBrand()) &&
                            Objects.equals(pojoItem.getCategory(), pojo.getCategory())
            ) {
                return false;
            }
        }

        return true;
    }

    // product Checks
    public static void nullCheckForUpdateProduct(ProductUpdateForm form) throws ApiException {
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

    public static boolean isUnique(ProductPojo pojo, List<ProductData> dataList) throws ApiException {
        for(ProductData productData : dataList) {
            if(Objects.equals(productData.getBarcode(), pojo.getBarcode())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isUnique(int current_id, ProductPojo pojo, List<ProductData> dataList) throws ApiException {
        for(ProductData productData : dataList) {
            if(Objects.equals(productData.getBarcode(), pojo.getBarcode()) && productData.getId() != current_id) {
                return false;
            }
        }
        return true;
    }


    public static void nullCheckProduct(ProductForm form) throws ApiException {
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

    // Inventory Checks
    public static void nullCheckInventory(InventoryForm form) throws ApiException {
        if(form.getBarcode().isEmpty() || form.getBarcode() == null) {
            throw new ApiException("Barcode can't be empty");
        }
        else if(form.getInventory() == 0) {
            throw new ApiException("Inventory can't be empty");
        }
    }

    public static void nullCheckForUpdateInventory(InventoryUpdateForm form) throws ApiException {
        if(form.getInventory() == 0) {
            throw new ApiException("Inventory can't be empty");
        }
    }

    public static boolean doesNotExistInventory(InventoryForm form, List<InventoryData> list) {
        for(InventoryData inventoryData : list) {
            if(Objects.equals(inventoryData.getBarcode(), form.getBarcode())) {
                return false;
            }
        }
        return true;
    }

    // Order Item Checks
    public static void nullCheckOrderItem(OrderItemForm form) throws ApiException {
        if(form.getBarcode().isEmpty() || form.getBarcode() == null) {
            throw new ApiException("Barcode can't be empty");
        }
        else if(form.getQuantity() == 0) {
            throw new ApiException("Quantity can't be empty");
        }
    }

    public static void nullCheckForUpdateOrderItem(OrderItemUpdateForm form) throws ApiException {
        if(form.getQuantity() == 0) {
            throw new ApiException("Quantity can't be empty");
        }
    }

    public  static boolean isUniqueOrderItem(OrderItemPojo pojo, List<OrderItemData> dataList) {
        for(OrderItemData placeOrderData : dataList) {
            if(Objects.equals(placeOrderData.getBarcode(), pojo.getBarcode())) {
                return false;
            }
        }
        return true;
    }
    
    // Report Checks
    public static boolean checkBrandCategoryExists(String brand, String category, List<BrandPojo> brandPojoList) {
        for(BrandPojo brandPojo : brandPojoList) {
            if(Objects.equals(brandPojo.getBrand(), brand) && Objects.equals(brandPojo.getCategory(), category)) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkBrandExists(String brand, List<BrandPojo> brandPojoList) {
        for(BrandPojo brandPojo : brandPojoList) {
            if(Objects.equals(brandPojo.getBrand(), brand)) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkCategoryExists(String category, List<BrandPojo> brandPojoList) {
        for(BrandPojo brandPojo : brandPojoList) {
            if(Objects.equals(brandPojo.getCategory(), category)) {
                return true;
            }
        }

        return false;
    }
}