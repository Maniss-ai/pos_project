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
    public void testSalesReport() throws ApiException {
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
        reportForm.setStartDate("2022-07-01T12:45:29+05:30");
        reportForm.setEndDate("2022-09-07T12:45:29+05:30");
        reportDto.getAllSales(reportForm);
    }

    @Test
    public void testGetBrandReport() throws ApiException {
        brandDto.add(addBrandForm());
        reportDto.getAllBrand();
    }

    @Test
    public void testGetInventoryReport() throws ApiException {
        brandDto.add(addBrandForm());
        productDto.add(addProductForm());
        inventoryDto.add(addInventoryForm());
        reportDto.getAllInventory();
    }

    @Test(expected = ApiException.class)
    public void testDateCheck() throws ApiException {
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
        reportForm.setEndDate("2022-07-01T12:45:29+05:30");
        reportForm.setStartDate("2022-09-07T12:45:29+05:30");
        reportDto.getAllSales(reportForm);
    }

    @Test
    public void testReportWithBrand() throws ApiException {
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
        reportForm.setStartDate("2022-07-01T12:45:29+05:30");
        reportForm.setEndDate("2022-09-07T12:45:29+05:30");
        reportDto.getAllSales(reportForm);
    }

    @Test
    public void testReportWithCategory() throws ApiException {
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
        reportForm.setCategory("category01");
        reportForm.setStartDate("2022-07-01T12:45:29+05:30");
        reportForm.setEndDate("2022-09-07T12:45:29+05:30");
        reportDto.getAllSales(reportForm);
    }

    @Test
    public void testReportWithBrandCategory() throws ApiException {
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
        reportForm.setCategory("category01");
        reportForm.setStartDate("2022-07-01T12:45:29+05:30");
        reportForm.setEndDate("2022-09-07T12:45:29+05:30");
        reportDto.getAllSales(reportForm);
    }

    @Test(expected = ApiException.class)
    public void testEmptyDate() throws ApiException {
        ReportForm reportForm = new ReportForm();
        reportDto.getAllSales(reportForm);
    }

    @Test(expected = ApiException.class)
    public void testStartDate() throws ApiException {
        ReportForm reportForm = new ReportForm();
        reportForm.setBrand("brand01");
        reportForm.setCategory("category01");
        reportForm.setStartDate("2022-09-09T12:45:29+05:30");
        reportForm.setEndDate("2022-09-09T12:45:29+05:30");
        reportDto.getAllSales(reportForm);
    }

    @Test(expected = ApiException.class)
    public void testReportWithBrandCategoryNotExists() throws ApiException {
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
        reportForm.setBrand("brand");
        reportForm.setCategory("category");
        reportForm.setStartDate("2022-07-01T12:45:29+05:30");
        reportForm.setEndDate("2022-09-07T12:45:29+05:30");
        reportDto.getAllSales(reportForm);
    }

    @Test(expected = ApiException.class)
    public void testReportWithBrandNotExists() throws ApiException {
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
        reportForm.setBrand("brand");
        reportForm.setStartDate("2022-07-01T12:45:29+05:30");
        reportForm.setEndDate("2022-09-07T12:45:29+05:30");
        reportDto.getAllSales(reportForm);
    }

    @Test(expected = ApiException.class)
    public void testReportWithCategoryNotExists() throws ApiException {
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
        reportForm.setCategory("category");
        reportForm.setStartDate("2022-07-01T12:45:29+05:30");
        reportForm.setEndDate("2022-09-07T12:45:29+05:30");
        reportDto.getAllSales(reportForm);
    }

}
