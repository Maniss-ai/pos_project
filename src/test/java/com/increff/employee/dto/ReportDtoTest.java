package com.increff.employee.dto;

import com.increff.employee.AbstractUnitTest;
import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.form.*;
import com.increff.employee.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ReportDtoTest extends AbstractUnitTest {
    @Autowired
    BrandDto brandDto;
    @Autowired
    ProductDto productDto;
    @Autowired
    InventoryDto inventoryDto;
    @Autowired
    OrderItemDto orderItemDto;
    @Autowired
    ReportDto reportDto;

    @Test
    public void testSalesReport() throws ApiException, ParseException {
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

        ReportForm reportForm = new ReportForm();
        reportForm.setBrand("brand01");
        reportForm.setStartDate("2022-07-01");
        reportForm.setEndDate("2022-12-01");
        reportDto.getAllSales(reportForm);
    }
}
