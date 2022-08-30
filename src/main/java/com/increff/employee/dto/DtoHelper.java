package com.increff.employee.dto;

import com.increff.employee.model.data.*;
import com.increff.employee.model.form.*;
import com.increff.employee.pojo.*;
import com.increff.employee.service.ApiException;
import org.apache.commons.math3.util.Precision;

import java.util.List;
import java.util.Objects;

public class DtoHelper {
    // Brand Conversions and normalize
    protected static BrandData convertPojoToDataBrand(BrandPojo pojo) {
        BrandData data = new BrandData();
        data.setCategory(pojo.getCategory());
        data.setBrand(pojo.getBrand());
        data.setId(pojo.getId());
        return data;
    }

    protected static BrandPojo convertFormToPojoBrand(BrandForm form) {
        BrandPojo pojo = new BrandPojo();
        pojo.setCategory(form.getCategory());
        pojo.setBrand(form.getBrand());
        return pojo;
    }

    public static void normalizeBrand(BrandPojo p) {
        p.setBrand(p.getBrand().toLowerCase().trim());
        p.setCategory(p.getCategory().toLowerCase().trim());
    }

    // Product Conversions
    protected static ProductData convertPojoToDataProduct(ProductPojo pojo) {
        ProductData data = new ProductData();
        data.setBarcode(pojo.getBarcode());
//        data.setBrand(pojo.getBrand());
//        data.setCategory(pojo.getCategory());
        data.setProduct(pojo.getProduct());
        data.setMrp(Precision.round(pojo.getMrp(), 2));
        data.setId(pojo.getId());
        return data;
    }
    protected static ProductPojo convertFormToPojoProduct(ProductForm form) {
        ProductPojo pojo = new ProductPojo();
//        pojo.setBrand(form.getBrand());
//        pojo.setCategory(form.getCategory());
        pojo.setBarcode(form.getBarcode());
        pojo.setProduct(form.getProduct());
        pojo.setMrp(Precision.round(form.getMrp(), 2));
        return pojo;
    }

    public static void normalizeProduct(ProductPojo p) {
        p.setBarcode(p.getBarcode().toLowerCase().trim());
        p.setProduct(p.getProduct().toLowerCase().trim());
//        p.setBrand(p.getBrand().toLowerCase().trim());
//        p.setCategory(p.getCategory().toLowerCase().trim());
    }

    public static void normalizeForUpdateProduct(ProductPojo p) {
        p.setBarcode(p.getBarcode().toLowerCase().trim());
        p.setProduct(p.getProduct().toLowerCase().trim());
    }

    protected static ProductPojo convertFormToPojoUpdateProduct(ProductUpdateForm form) {
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode(form.getBarcode());
        pojo.setProduct(form.getProduct());
        pojo.setMrp(Precision.round(form.getMrp(), 2));
        return pojo;
    }

    // Inventory Conversions
    protected static InventoryData convertPojoToDataInventory(InventoryPojo pojo) {
        InventoryData data = new InventoryData();
//        data.setBarcode(pojo.getBarcode());
        data.setInventory(pojo.getInventory());
        data.setId(pojo.getId());
        return data;
    }

    protected static InventoryPojo convertFormToPojoInventory(InventoryForm form) {
        InventoryPojo pojo = new InventoryPojo();
//        pojo.setBarcode(form.getBarcode());
        pojo.setInventory(form.getInventory());
        return pojo;
    }

    protected static InventoryPojo convertFormToPojoForUpdateInventory(InventoryUpdateForm form) {
        InventoryPojo pojo = new InventoryPojo();
        pojo.setInventory(form.getInventory());
        return pojo;
    }

    public static void normalizeInventory(InventoryPojo p) {
//        p.setBarcode(p.getBarcode().toLowerCase().trim());
    }

    // OrderItem Conversions
    protected static OrderItemData convertPojoToDataOrderItem(OrderItemPojo pojo) {
        OrderItemData data = new OrderItemData();
        data.setOrderId(pojo.getOrderId());
//        data.setBarcode(pojo.getBarcode());
        data.setQuantity(pojo.getQuantity());
        data.setSellingPrice(Precision.round(pojo.getSellingPrice(), 2));
        data.setId(pojo.getId());
        return data;
    }

    protected static OrderItemPojo convertFormToPojoOrderItem(OrderItemForm form, Integer orderId) {
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setOrderId(orderId);
//        pojo.setBarcode(form.getBarcode());
        pojo.setQuantity(form.getQuantity());
        pojo.setSellingPrice(Precision.round(form.getSellingPrice(), 2));
        return pojo;
    }

    protected static OrderItemPojo convertFormToPojoForUpdateOrderItem(OrderItemUpdateForm form, Integer orderId) {
        OrderItemPojo pojo = new OrderItemPojo();
//        pojo.setBarcode(barcode);
        pojo.setOrderId(orderId);
        pojo.setQuantity(form.getQuantity());
        pojo.setSellingPrice(Precision.round(form.getSellingPrice(), 2));
        return pojo;
    }

    // Order Conversions
    protected static OrderData convertPojoToDataOrder(OrderPojo pojo) {
        OrderData data = new OrderData();
        data.setId(pojo.getOrderId());
        data.setTime(pojo.getTime().toString());
        data.setBillAmount(Precision.round(pojo.getBillAmount(), 2));
        return data;
    }

    protected static OrderItemPojo convertFormToPojoOrder(OrderItemForm form) {
        OrderItemPojo pojo = new OrderItemPojo();
//        pojo.setOrderId(form.getOrderId());
//        pojo.setBarcode(form.getBarcode());
        pojo.setQuantity(form.getQuantity());
        pojo.setSellingPrice(Precision.round(form.getSellingPrice(), 2));
        return pojo;
    }

//    public static void normalizeOrderItem(OrderItemPojo p) {
//        p.setBarcode(p.getBarcode().toLowerCase().trim());
//    }

}
