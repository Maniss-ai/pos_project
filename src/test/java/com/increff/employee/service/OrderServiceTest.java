package com.increff.employee.service;

import com.increff.employee.AbstractUnitTest;
import com.increff.employee.pojo.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrderServiceTest extends AbstractUnitTest {
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    BrandService brandService;
    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;

    @Test(expected = ApiException.class)
    public void testGetSelectedOrderWhenIdExists() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandService.add(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productService.add(productPojo);

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setInventory(78);
        inventoryPojo.setId(1);
        inventoryService.add(inventoryPojo);

        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(0);
        orderItemPojo.setSellingPrice(100.023);
        orderItemPojo.setQuantity(10);

        orderItemPojo = orderItemService.add(orderItemPojo);
        Integer orderId = orderItemPojo.getOrderId();

        List<OrderPojo> orderPojoList = orderService.getSelectedOrdersWithId(orderId);

        Assert.assertEquals(100.023 * 10, orderPojoList.get(0).getBillAmount(), 1);
    }

    @Test(expected = ApiException.class)
    public void testGetSelectedOrderWhenIdNotExists() throws ApiException {
        Integer orderId = 0;
        orderService.getSelectedOrdersWithId(orderId);
    }

    @Test(expected = ApiException.class)
    public void testGetOrderWhenIdExists() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandService.add(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productService.add(productPojo);

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setInventory(78);
        inventoryPojo.setId(1);
        inventoryService.add(inventoryPojo);

        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(0);
        orderItemPojo.setSellingPrice(100.023);
        orderItemPojo.setQuantity(10);

        orderItemPojo = orderItemService.add(orderItemPojo);
        Integer orderId = orderItemPojo.getOrderId();

        OrderPojo orderPojo = orderService.getOrder(orderId);

        Assert.assertEquals(100.023 * 10, orderPojo.getBillAmount(), 1);
    }

    @Test(expected = ApiException.class)
    public void testGetOrderWhenIdNotExists() throws ApiException {
        Integer orderId = 0;
        orderService.getOrder(orderId);
    }

}
