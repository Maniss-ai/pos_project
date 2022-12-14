package com.increff.employee.service;

import com.increff.employee.AbstractUnitTest;
import com.increff.employee.pojo.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;


public class OrderItemServiceTest extends AbstractUnitTest {
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    BrandService brandService;
    @Autowired
    ProductService productService;

    @Test
    public void testAdd() {
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
        orderItemService.add(orderItemPojo);

        Assert.assertEquals(0, orderItemPojo.getOrderId(), 1);
        Assert.assertEquals(100.02, orderItemPojo.getSellingPrice(), 0.2);
        Assert.assertEquals(10, orderItemPojo.getQuantity(), 1);
        Assert.assertEquals(0, orderItemPojo.getOrderId(), 1);
    }

    @Test(expected = ApiException.class)
    public void testGetCheckOrderIdNotExist() throws ApiException {
        String barcode = "barcode";
        orderItemService.getCheckOrderId(12);
    }

    @Test
    public void testGetCheckOrderIdExist() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setBillAmount(397.234);
        orderPojo.setTime(ZonedDateTime.now());

        orderItemService.submit(orderPojo);
    }

    @Test(expected = ApiException.class)
    public void testGetCheck() throws ApiException {
        Integer orderItemId = 0;
        orderItemService.getCheck(orderItemId);
    }
    @Test
    public void testSubmit() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setTime(ZonedDateTime.now());
        orderPojo.setBillAmount(9289.92);

        orderItemService.submit(orderPojo);
    }

    @Test(expected = ApiException.class)
    public void testGetSingleOrderWhenIdNotExists() throws ApiException {
        int orderId = 0;
        orderItemService.getSingleOrder(orderId);
    }


}
