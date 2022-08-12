package com.increff.employee.dto;

import com.increff.employee.model.data.*;
import com.increff.employee.model.form.*;
import com.increff.employee.pojo.*;
import org.apache.commons.math3.util.Precision;

public class DtoHelper {
    // Brand Conversions
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

    // Product Conversions
    protected static ProductData convertPojoToDataProduct(ProductPojo pojo) {
        ProductData data = new ProductData();
        data.setBarcode(pojo.getBarcode());
        data.setBrand(pojo.getBrand());
        data.setCategory(pojo.getCategory());
        data.setProduct(pojo.getProduct());
        data.setMrp(Precision.round(pojo.getMrp(), 2));
        data.setId(pojo.getId());
        return data;
    }
    protected static ProductPojo convertFormToPojoProduct(ProductForm form) {
        ProductPojo pojo = new ProductPojo();
        pojo.setBrand(form.getBrand());
        pojo.setCategory(form.getCategory());
        pojo.setBarcode(form.getBarcode());
        pojo.setProduct(form.getProduct());
        pojo.setMrp(Precision.round(form.getMrp(), 2));
        return pojo;
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
        data.setBarcode(pojo.getBarcode());
        data.setInventory(pojo.getInventory());
        data.setId(pojo.getId());
        return data;
    }

    protected static InventoryPojo convertFormToPojoInventory(InventoryForm form) {
        InventoryPojo pojo = new InventoryPojo();
        pojo.setBarcode(form.getBarcode());
        pojo.setInventory(form.getInventory());
        return pojo;
    }

    protected static InventoryPojo convertFormToPojoForUpdateInventory(InventoryUpdateForm form) {
        InventoryPojo pojo = new InventoryPojo();
        pojo.setInventory(form.getInventory());
        return pojo;
    }

    // OrderItem Conversions
    protected static OrderItemData convertPojoToDataOrderItem(OrderItemPojo pojo) {
        OrderItemData data = new OrderItemData();
        data.setOrder_id(pojo.getOrder_id());
        data.setBarcode(pojo.getBarcode());
        data.setQuantity(pojo.getQuantity());
        System.out.println("PRECISION DATA" + (Precision.round(pojo.getSelling_price(), 2)));
        System.out.println("DATA" + pojo.getSelling_price());
        data.setSelling_price(Precision.round(pojo.getSelling_price(), 2));
        data.setId(pojo.getId());
        return data;
    }

    protected static OrderItemPojo convertFormToPojoOrderItem(OrderItemForm form, int order_id) {
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setOrder_id(order_id);
        pojo.setBarcode(form.getBarcode());
        pojo.setQuantity(form.getQuantity());
        pojo.setSelling_price(Precision.round(form.getSelling_price(), 2));
        return pojo;
    }

    protected static OrderItemPojo convertFormToPojoForUpdateOrderItem(OrderItemUpdateForm form, int order_id, String barcode) {
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setBarcode(barcode);
        pojo.setOrder_id(order_id);
        pojo.setQuantity(form.getQuantity());
        System.out.println("SELLING PRICE : " + form.getSelling_price());
        System.out.println("SELLING PRICE : " + Precision.round(form.getSelling_price(), 2));
        pojo.setSelling_price(Precision.round(form.getSelling_price(), 2));
        return pojo;
    }

    // Order Conversions
    protected static OrderData convertPojoToDataOrder(OrderPojo pojo) {
        OrderData data = new OrderData();
        data.setId(pojo.getOrder_id());
        data.setTime(pojo.getTime().toString());
        data.setBill_amount(Precision.round(pojo.getBill_amount(), 2));
        return data;
    }

    protected static OrderItemPojo convertFormToPojoOrder(OrderItemForm form) {
        OrderItemPojo pojo = new OrderItemPojo();
//        pojo.setOrder_id(form.getOrder_id());
        pojo.setBarcode(form.getBarcode());
        pojo.setQuantity(form.getQuantity());
        pojo.setSelling_price(Precision.round(form.getSelling_price(), 2));
        return pojo;
    }

}