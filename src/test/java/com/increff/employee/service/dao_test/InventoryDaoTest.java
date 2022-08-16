package com.increff.employee.service.dao_test;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class InventoryDaoTest extends AbstractUnitTest {
    @Autowired
    BrandDao brandDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    InventoryDao inventoryDao;

    @Test
    public void testInsert() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandDao.insert(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBrand("puma");
        productPojo.setCategory("shoes");
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productDao.insert(productPojo);

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setBarcode("puma111");
        inventoryPojo.setInventory(38);
        inventoryPojo = inventoryDao.insert(inventoryPojo);

        Assert.assertEquals(productPojo.getBarcode(), inventoryPojo.getBarcode());
        Assert.assertEquals(38, inventoryPojo.getInventory(), 1);
    }

    @Test
    public void testSelect() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandDao.insert(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBrand("puma");
        productPojo.setCategory("shoes");
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productDao.insert(productPojo);

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setBarcode("puma111");
        inventoryPojo.setInventory(38);
        inventoryPojo = inventoryDao.insert(inventoryPojo);

        inventoryPojo = inventoryDao.select(inventoryPojo.getId());
        Assert.assertEquals(productPojo.getBarcode(), inventoryPojo.getBarcode());
        Assert.assertEquals(38, inventoryPojo.getInventory(), 1);
    }

    @Test
    public void testSelectAll() {
        for (int i = 0; i < 5; i++) {
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand" + i + 1);
            brandPojo.setCategory("category" + i + 1);
            brandDao.insert(brandPojo);
        }

        for (int i = 0; i < 5; i++) {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBrand("brand" + i + 1);
            productPojo.setCategory("category" + i + 1);
            productPojo.setBarcode("barcode" + i + 1);
            productPojo.setProduct("product" + i + 1);
            productPojo.setMrp(100.2432 * (i + 1));
            productDao.insert(productPojo);
        }

        for(int i = 0; i < 5; i++) {
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setBarcode("barcode" + i+1);
            inventoryPojo.setInventory((i+1) * 42);
            inventoryDao.insert(inventoryPojo);
        }

        Assert.assertEquals(5, inventoryDao.selectAll().size());
    }
}
