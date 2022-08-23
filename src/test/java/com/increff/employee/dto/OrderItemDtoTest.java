package com.increff.employee.dto;

import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.form.*;
import com.increff.employee.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class OrderItemDtoTest extends AbstractUnitTest {
    @Autowired
    BrandDto brandDto;
    @Autowired
    ProductDto productDto;
    @Autowired
    InventoryDto inventoryDto;
    @Autowired
    OrderItemDto orderItemDto;

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
        inventoryDto.add(inventoryForm);

        OrderItemForm orderItemForm = new OrderItemData();
        orderItemForm.setBarcode(inventoryForm.getBarcode());
        orderItemForm.setQuantity(inventoryForm.getInventory() - 20);
        orderItemForm.setSellingPrice(productForm.getMrp() - 234.32);

        OrderItemData orderItemData = orderItemDto.add(orderItemForm);

        Assert.assertEquals(inventoryForm.getBarcode(), orderItemData.getBarcode());
        Assert.assertEquals(inventoryForm.getInventory() - 20, orderItemData.getQuantity(), 1);
        Assert.assertEquals(productForm.getMrp() - 234.32, orderItemData.getSellingPrice(), 0.1);
    }

    @Test(expected = ApiException.class)
    public void testAddDuplicate() throws ApiException {
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

        OrderItemForm orderItemForm = new OrderItemData();
        orderItemForm.setBarcode(inventoryForm.getBarcode());
        orderItemForm.setQuantity(inventoryForm.getInventory() - 20);
        orderItemForm.setSellingPrice(productForm.getMrp() - 234.32);

        orderItemDto.add(orderItemForm);
        orderItemDto.add(orderItemForm);
    }

    @Test
    public void testGetWhenIdExists() throws ApiException {
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

        OrderItemForm orderItemForm = new OrderItemData();
        orderItemForm.setBarcode(inventoryForm.getBarcode());
        orderItemForm.setQuantity(inventoryForm.getInventory() - 20);
        orderItemForm.setSellingPrice(productForm.getMrp() - 234.32);

        int orderItemId = orderItemDto.add(orderItemForm).getId();

        OrderItemData orderItemData = orderItemDto.get(orderItemId);

        Assert.assertEquals(inventoryForm.getBarcode(), orderItemData.getBarcode());
        Assert.assertEquals(inventoryForm.getInventory() - 20, orderItemData.getQuantity(), 1);
        Assert.assertEquals(productForm.getMrp() - 234.32, orderItemData.getSellingPrice(), 0.1);
    }

    @Test(expected = ApiException.class)
    public void testGetWhenIdNotExists() throws ApiException {
        int id = 0;
        orderItemDto.get(id);
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

        for(int i = 0; i < 5; i++) {
            OrderItemForm orderItemForm = new OrderItemData();
            orderItemForm.setBarcode("barcode" + i+1);
            orderItemForm.setQuantity((i+1) * 3);
            orderItemForm.setSellingPrice((i+1) * 7.298);
            orderItemDto.add(orderItemForm);
        }

        List<OrderItemData> orderItemDataList = orderItemDto.getAll();

        Assert.assertEquals(5, orderItemDataList.size());
    }

    @Test
    public void testSubmit() throws ApiException {
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

        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            OrderItemForm orderItemForm = new OrderItemData();
            orderItemForm.setBarcode("barcode" + i+1);
            orderItemForm.setQuantity((i+1) * 3);
            orderItemForm.setSellingPrice((i+1) * 7.298);
            orderItemDto.add(orderItemForm);
            orderItemFormList.add(orderItemForm);
        }

        orderItemDto.submit(orderItemFormList);
    }

    @Test
    public void testUpdate() throws ApiException {
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

        OrderItemForm orderItemForm = new OrderItemData();
        orderItemForm.setBarcode(inventoryForm.getBarcode());
        orderItemForm.setQuantity(inventoryForm.getInventory() - 20);
        orderItemForm.setSellingPrice(productForm.getMrp() - 234.32);

        OrderItemData orderItemData = orderItemDto.add(orderItemForm);

        OrderItemUpdateForm orderItemUpdateForm = new OrderItemUpdateForm();
        orderItemUpdateForm.setQuantity(90);
        orderItemUpdateForm.setSellingPrice(90.214442);

        orderItemData = orderItemDto.update(orderItemData.getId(), orderItemUpdateForm);

        Assert.assertEquals(90 ,orderItemData.getQuantity(), 1);
        Assert.assertEquals(90.21,orderItemData.getSellingPrice(), 0.1);
    }

    @Test(expected = ApiException.class)
    public void testUpdateInventoryNotEnough() throws ApiException {
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

        OrderItemForm orderItemForm = new OrderItemData();
        orderItemForm.setBarcode(inventoryForm.getBarcode());
        orderItemForm.setQuantity(inventoryForm.getInventory() - 20);
        orderItemForm.setSellingPrice(productForm.getMrp() - 234.32);

        OrderItemData orderItemData = orderItemDto.add(orderItemForm);

        OrderItemUpdateForm orderItemUpdateForm = new OrderItemUpdateForm();
        orderItemUpdateForm.setQuantity(inventoryForm.getInventory() + 1);
        orderItemUpdateForm.setSellingPrice(90.214442);

        orderItemDto.update(orderItemData.getId(), orderItemUpdateForm);
    }

    @Test
    public void testOrderSubmit() throws ApiException {
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

        OrderItemForm orderItemForm = new OrderItemData();
        orderItemForm.setBarcode(inventoryForm.getBarcode());
        orderItemForm.setQuantity(inventoryForm.getInventory() - 20);
        orderItemForm.setSellingPrice(productForm.getMrp() - 234.32);
        orderItemDto.add(orderItemForm);

        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(orderItemForm);
        orderItemDto.submit(orderItemFormList);
    }

}
