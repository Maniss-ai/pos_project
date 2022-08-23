package com.increff.employee.dto;

import com.increff.employee.AbstractUnitTest;
import com.increff.employee.model.data.OrderData;
import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.form.*;
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

    @Test
    public void testSearchWithOrderId() throws ApiException, ParseException {
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
        viewOrderForm.setOrderId(1);
        List<OrderData> orderDataList = orderDto.search(viewOrderForm);

        Assert.assertEquals((inventoryForm.getInventory() - 20) * (productForm.getMrp() - 234.32), orderDataList.get(0).getBillAmount(), 1);
    }

    @Test
    public void testSearchWithDate() throws ApiException, ParseException {
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
        viewOrderForm.setStartDate("2022-06-22");
        viewOrderForm.setEndDate("2022-12-23");
        List<OrderData> orderDataList = orderDto.search(viewOrderForm);

        Assert.assertEquals((inventoryForm.getInventory() - 20) * (productForm.getMrp() - 234.32), orderDataList.get(0).getBillAmount(), 1);
    }

    @Test(expected = ApiException.class)
    public void testEmptyViewOrderForm() throws ParseException, ApiException {
        ViewOrderForm viewOrderForm = new ViewOrderForm();
        orderDto.search(viewOrderForm);
    }

}
