package com.increff.employee.dao;

import com.increff.employee.AbstractUnitTest;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderItemDaoTest extends AbstractUnitTest {
    @Autowired
    BrandDao brandDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    InventoryDao inventoryDao;
    @Autowired
    OrderItemDao orderItemDao;

    @Test
    public void testInsert() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandDao.insert(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productDao.insert(productPojo);

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setInventory(38);
        inventoryPojo.setId(1);
        inventoryDao.insert(inventoryPojo);

        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(0);
        orderItemPojo.setSellingPrice(100.023);
        orderItemPojo.setQuantity(10);

        orderItemPojo = orderItemDao.insert(orderItemPojo);

        Assert.assertEquals(0, orderItemPojo.getOrderId(), 1);
        Assert.assertEquals(100.02, orderItemPojo.getSellingPrice(), 0.2);
        Assert.assertEquals(10, orderItemPojo.getQuantity(), 1);
        Assert.assertEquals(0, orderItemPojo.getOrderId(), 1);
    }

    @Test
    public void testSelect() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandDao.insert(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productDao.insert(productPojo);

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setInventory(38);
        inventoryPojo.setId(1);
        inventoryDao.insert(inventoryPojo);

        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(0);
        orderItemPojo.setSellingPrice(100.023);
        orderItemPojo.setQuantity(10);

        orderItemPojo = orderItemDao.insert(orderItemPojo);

        int id = orderItemPojo.getId();
        orderItemPojo = orderItemDao.select(id);

        Assert.assertEquals(0, orderItemPojo.getOrderId(), 1);
        Assert.assertEquals(100.02, orderItemPojo.getSellingPrice(), 0.2);
        Assert.assertEquals(10, orderItemPojo.getQuantity(), 1);
        Assert.assertEquals(0, orderItemPojo.getOrderId(), 1);
    }

    @Test
    public void testSelect1() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandDao.insert(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productDao.insert(productPojo);

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setInventory(38);
        inventoryPojo.setId(1);
        inventoryDao.insert(inventoryPojo);

        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(0);
        orderItemPojo.setSellingPrice(100.023);
        orderItemPojo.setQuantity(10);

        orderItemPojo = orderItemDao.insert(orderItemPojo);

        int id = orderItemPojo.getId();
        orderItemPojo = orderItemDao.select(id);

        Assert.assertEquals(0, orderItemPojo.getOrderId(), 1);
        Assert.assertEquals(100.02, orderItemPojo.getSellingPrice(), 0.2);
        Assert.assertEquals(10, orderItemPojo.getQuantity(), 1);
        Assert.assertEquals(0, orderItemPojo.getOrderId(), 1);
    }

}
