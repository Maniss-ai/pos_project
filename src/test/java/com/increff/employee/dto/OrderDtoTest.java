package com.increff.employee.dto;

import com.increff.employee.AbstractUnitTest;
import com.increff.employee.model.data.OrderData;
import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.form.*;
import com.increff.employee.pojo.*;
import com.increff.employee.service.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class OrderDtoTest extends AbstractUnitTest {
    @Autowired
    BrandDto brandDto;
    @Autowired
    ProductDto productDto;
    @Autowired
    InventoryDto inventoryDto;
    @Autowired
    OrderItemDto orderItemDto;
    @Autowired
    OrderDto orderDto;

    @Test(expected = ApiException.class)
    public void testSearchWithOrderId() throws ApiException {
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

        ViewOrderForm viewOrderForm = new ViewOrderForm();
        viewOrderForm.setOrderId(17);
        List<OrderData> orderDataList = orderDto.search(viewOrderForm);

        Assert.assertEquals((inventoryForm.getInventory() - 20) * (productForm.getMrp() - 234.32), orderDataList.get(0).getBillAmount(), 1);
    }

    @Test
    public void testSearchWithDate() throws ApiException {
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

        ViewOrderForm viewOrderForm = new ViewOrderForm();
        viewOrderForm.setStartDate("2022-07-01T12:45:29+05:30");
        viewOrderForm.setEndDate("2022-09-07T12:45:29+05:30");
        List<OrderData> orderDataList = orderDto.search(viewOrderForm);

        Assert.assertEquals((inventoryForm.getInventory() - 20) * (productForm.getMrp() - 234.32), orderDataList.get(0).getBillAmount(), 1);
    }

    @Test(expected = ApiException.class)
    public void testEmptyViewOrderForm() throws ApiException {
        ViewOrderForm viewOrderForm = new ViewOrderForm();
        orderDto.search(viewOrderForm);
    }

    @Test(expected = ApiException.class)
    public void testGetOrder() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("puma");
        brandForm.setCategory("shoes");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand("puma");
        productForm.setCategory("shoes");
        productForm.setBarcode("puma111");
        productForm.setProduct("sports shoes");
        productForm.setMrp(2999.362);
        productDto.add(productForm);

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("puma111");
        inventoryForm.setInventory(78);
        inventoryDto.add(inventoryForm);

        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setSellingPrice(100.023);
        orderItemForm.setBarcode(inventoryForm.getBarcode());
        orderItemForm.setQuantity(10);
        OrderItemData orderItemData = orderItemDto.add(orderItemForm);

        Integer orderId = orderItemData.getOrderId();
        OrderPojo orderPojo = orderDto.getOrder(1);

        Assert.assertEquals(100.023 * 10, orderPojo.getBillAmount(), 1);
    }

    @Test
    public void testGetSingleOrder() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("puma");
        brandForm.setCategory("shoes");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand("puma");
        productForm.setCategory("shoes");
        productForm.setBarcode("puma111");
        productForm.setProduct("sports shoes");
        productForm.setMrp(2999.362);
        productDto.add(productForm);

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("puma111");
        inventoryForm.setInventory(78);
        inventoryDto.add(inventoryForm);

        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setSellingPrice(100.023);
        orderItemForm.setBarcode(inventoryForm.getBarcode());
        orderItemForm.setQuantity(10);
        OrderItemData orderItemData = orderItemDto.add(orderItemForm);

        Integer orderId = orderItemData.getOrderId();
        orderDto.getSingleOrder(orderId);
    }

}
