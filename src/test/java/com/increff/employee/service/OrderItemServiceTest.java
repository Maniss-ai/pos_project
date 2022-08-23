package com.increff.employee.service;

import com.increff.employee.AbstractUnitTest;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.ProductPojo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        productPojo.setBrand("puma");
        productPojo.setCategory("shoes");
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productService.add(productPojo);

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setBarcode("puma111");
        inventoryPojo.setInventory(78);
        inventoryService.add(inventoryPojo);

        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(0);
        orderItemPojo.setSellingPrice(100.023);
        orderItemPojo.setBarcode(inventoryPojo.getBarcode());
        orderItemPojo.setQuantity(10);
        orderItemService.add(orderItemPojo);

        Assert.assertEquals(0, orderItemPojo.getOrderId(), 1);
        Assert.assertEquals(100.02, orderItemPojo.getSellingPrice(), 0.2);
        Assert.assertEquals(inventoryPojo.getBarcode(), orderItemPojo.getBarcode());
        Assert.assertEquals(10, orderItemPojo.getQuantity(), 1);
        Assert.assertEquals(0, orderItemPojo.getOrderId(), 1);
    }

}
