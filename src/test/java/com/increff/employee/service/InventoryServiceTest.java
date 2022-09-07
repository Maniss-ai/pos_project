package com.increff.employee.service;

import com.increff.employee.AbstractUnitTest;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InventoryServiceTest extends AbstractUnitTest {
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
        inventoryPojo = inventoryService.add(inventoryPojo);

        Assert.assertEquals(inventoryPojo.getInventory(), 78, 1);
    }

    @Test
    public void testGetWithId() throws ApiException {
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
        inventoryPojo = inventoryService.add(inventoryPojo);

        int id = inventoryPojo.getId();
        inventoryPojo = inventoryService.get(id);

        Assert.assertEquals(inventoryPojo.getInventory(), 78, 1);
    }

    @Test
    public void testGetWithBarcode() throws ApiException {
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
        inventoryPojo = inventoryService.add(inventoryPojo);

        Assert.assertEquals(inventoryPojo.getInventory(), 78, 1);
    }

    @Test
    public void testGetAll() {
        for (int i = 0; i < 5; i++) {
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand" + i + 1);
            brandPojo.setCategory("category" + i + 1);
            brandService.add(brandPojo);
        }

        for (int i = 0; i < 5; i++) {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode" + i + 1);
            productPojo.setProduct("products" + i + 1);
            productPojo.setMrp(100.234 * (i + 1));
            productService.add(productPojo);
        }

        for(int i = 0; i < 5; i++) {
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setInventory((i+1) * 18);
            inventoryPojo.setId(i+1);
            inventoryService.add(inventoryPojo);
        }

        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        Assert.assertEquals(5, inventoryPojoList.size());
    }

    @Test
    public void testUpdate() throws ApiException {
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
        inventoryPojo.setInventory(100);
        inventoryPojo.setId(1);
        inventoryService.add(inventoryPojo);

        inventoryPojo.setInventory(120);

        Assert.assertEquals(120, inventoryPojo.getInventory(), 1);
    }

}
