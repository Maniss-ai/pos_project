package com.increff.employee.dto;

import com.increff.employee.model.data.InventoryData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.model.form.InventoryForm;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class InventoryDtoTest extends AbstractUnitTest {
    @Autowired
    BrandDto brandDto;
    @Autowired
    ProductDto productDto;
    @Autowired
    InventoryDto inventoryDto;

    @Test
    public void testAddNormalize() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(" pUMa   ");
        brandForm.setCategory(" ShoES   ");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand(brandForm.getBrand());
        productForm.setCategory(brandForm.getCategory());
        productForm.setBarcode("puma123");
        productForm.setProduct("sports shoes");
        productForm.setMrp(1799.28);
        productDto.add(productForm);

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(productForm.getBarcode());
        inventoryForm.setInventory(120);

        InventoryData inventoryData = inventoryDto.add(inventoryForm);

        Assert.assertEquals(inventoryData.getInventory(), 120, 1);
        Assert.assertEquals(inventoryData.getBarcode(), productForm.getBarcode());
    }

    @Test
    public void testAddUpdate() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(" pUMa   ");
        brandForm.setCategory(" ShoES   ");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand(brandForm.getBrand());
        productForm.setCategory(brandForm.getCategory());
        productForm.setBarcode("puma123");
        productForm.setProduct("sports shoes");
        productForm.setMrp(1799.28);
        productDto.add(productForm);

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(productForm.getBarcode());
        inventoryForm.setInventory(120);

        inventoryDto.add(inventoryForm);

        inventoryForm.setInventory(300);
        inventoryDto.add(inventoryForm);

        Assert.assertEquals(300, inventoryForm.getInventory(), 1);
    }

    @Test(expected = ApiException.class)
    public void testCheckBarcodeExistsOrNot() throws ApiException {
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("barcode");
        inventoryForm.setInventory(200);

        inventoryDto.add(inventoryForm);
    }

    @Test
    public void testBulkAddSize() throws ApiException {
        for(int i = 0; i < 5; i++) {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrand("Brand" + i+1);
            brandForm.setCategory("Category" + i+1);
            brandDto.add(brandForm);
        }

        for(int i = 0; i < 5; i++) {
            ProductForm productForm = new ProductForm();
            productForm.setBrand("Brand" + i+1);
            productForm.setCategory("Category" + i+1);
            productForm.setBarcode("Barcode" + i+1);
            productForm.setProduct("Product" + i+1);
            productForm.setMrp((i+1) * 100.23);
            productDto.add(productForm);
        }

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            InventoryForm inventoryForm = new InventoryForm();
            inventoryForm.setBarcode("Barcode" + i+1);
            inventoryForm.setInventory((i+1) * 10);
            inventoryFormList.add(inventoryForm);
        }

        Assert.assertEquals(5, inventoryDto.bulkAddInventory(inventoryFormList).size());
    }

    @Test(expected = ApiException.class)
    public void testBulkAddRollback() throws ApiException {
        for(int i = 0; i < 5; i++) {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrand("Brand" + i+1);
            brandForm.setCategory("Category" + i+1);
            brandDto.add(brandForm);
        }

        for(int i = 0; i < 5; i++) {
            ProductForm productForm = new ProductForm();
            productForm.setBrand("Brand" + i+1);
            productForm.setCategory("Category" + i+1);
            productForm.setBarcode("Barcode" + i+1);
            productForm.setProduct("Product" + i+1);
            productForm.setMrp((i+1) * 100.23);
            productDto.add(productForm);
        }

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            InventoryForm inventoryForm = new InventoryForm();
            inventoryForm.setBarcode("Barcode" + i+1);
            inventoryForm.setInventory((i+1) * 10);
            inventoryFormList.add(inventoryForm);
        }

        // Add Inventory, When barcode doesn't exists
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("Barcode" + 35);
        inventoryForm.setInventory(127);
        inventoryFormList.add(inventoryForm);

        inventoryDto.bulkAddInventory(inventoryFormList);
    }

    @Test
    public void testGetWithIdWhenIdExists() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("PuMa    ");
        brandForm.setCategory("    SHOES    ");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand("    PUma");
        productForm.setCategory("    shOES   ");
        productForm.setBarcode("    PUmA111   ");
        productForm.setProduct("    SportS ShoES   ");
        productForm.setMrp(2999.362);
        productDto.add(productForm);

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("PUmA111");
        inventoryForm.setInventory(782);
        InventoryData inventoryData = inventoryDto.add(inventoryForm);

        int id = inventoryData.getId();
        inventoryData = inventoryDto.get(id);

        Assert.assertEquals("puma111", inventoryData.getBarcode());
        Assert.assertEquals(782, inventoryData.getInventory(), 1);
    }

    @Test(expected = ApiException.class)
    public void testGetWithIdWhenIdNotExists() throws ApiException {
        int id = 0;
        inventoryDto.get(id);
    }

    @Test
    public void testGetAll() throws ApiException {
        for(int i = 0; i < 5; i++) {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrand("Brand" + i+1);
            brandForm.setCategory("Category" + i+1);
            brandDto.add(brandForm);
        }

        for(int i = 0; i < 5; i++) {
            ProductForm productForm = new ProductForm();
            productForm.setBrand("Brand" + i+1);
            productForm.setCategory("Category" + i+1);
            productForm.setBarcode("Barcode" + i+1);
            productForm.setProduct("Product" + i+1);
            productForm.setMrp((i+1) * 100.23);
            productDto.add(productForm);
        }

        for(int i = 0; i < 5; i++) {
            InventoryForm inventoryForm = new InventoryForm();
            inventoryForm.setBarcode("Barcode" + i+1);
            inventoryForm.setInventory((i+1) * 10);
            inventoryDto.add(inventoryForm);
        }

        List<InventoryData> inventoryFormList = inventoryDto.getAll();

        Assert.assertEquals(5, inventoryFormList.size());
    }

}
