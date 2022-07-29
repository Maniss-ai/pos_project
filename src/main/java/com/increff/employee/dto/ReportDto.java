package com.increff.employee.dto;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.ReportForm;
import com.increff.employee.pojo.*;
import com.increff.employee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReportDto {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private PlaceOrderService placeOrderService;
    @Autowired
    private ViewOrderService viewOrderService;

    public StringBuilder getAllSales(ReportForm form) throws ApiException {
        if(form.getStart_date() != null && form.getEnd_date() != null && !form.getStart_date().isEmpty() && !form.getEnd_date().isEmpty()) {
            StringBuilder salesData = new StringBuilder("Brand\tCategory\tQuantity\tRevenue\n");
            List<ProductPojo> productPojoList;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start_date = LocalDate.parse(form.getStart_date(), formatter);
            LocalDate end_date = LocalDate.parse(form.getEnd_date(), formatter);

            // USING BRAND and CATEGORY + DATE
            if(!form.getBrand().isEmpty() && !form.getCategory().isEmpty() && form.getBrand() != null && form.getCategory() != null) {
                salesData.append(form.getBrand()).append("\t").append(form.getCategory()).append("\t");
                productPojoList = productService.getProductWithBrandCategory(form);
                getReportWithBrandCategory(start_date, end_date, salesData, productPojoList);
            }

            // USING BRAND ONLY + DATE
            else if(!form.getBrand().isEmpty() && form.getBrand() != null) {
                salesData.append(form.getBrand()).append(form.getCategory());
                productPojoList = productService.getProductWithBrand(form);
                getReportWithBrand(start_date, end_date, salesData, productPojoList);
            }

            // USING CATEGORY ONLY + DATE
            else if(!form.getCategory().isEmpty() && form.getCategory() != null) {
                productPojoList = productService.getProductWithCategory(form);
            }

            // USING DATE ONLY
            else {
                System.out.println("WORKING!!");
                productPojoList = productService.getProductWithDate(form);
            }

            return salesData;
        }
        else {
            throw new ApiException("Date can't be empty ....");
        }
    }

    private void getReportWithBrandCategory(LocalDate start_date, LocalDate end_date, StringBuilder salesData, List<ProductPojo> productPojoList) throws ApiException {
        // GET PlaceOrderPojo using Barcode and Add to DATA LIST .... ....
        List<PlaceOrderPojo> placeOrderPojoList = new ArrayList<>();
        for (ProductPojo pojo : productPojoList) {
            placeOrderPojoList.addAll(placeOrderService.getCheckWithBarcode(pojo.getBarcode()));
        }

        // Get Order Ids
        List<OrderPojo> orderPojoList = viewOrderService.getSelectedOrdersWithoutId(start_date, end_date);

        // Match Order Ids according to the given Time Range ....
        for(int i = 0; i < placeOrderPojoList.size(); i++) {
            boolean equalIds = false;
            for(OrderPojo orderPojo : orderPojoList) {
                if(placeOrderPojoList.get(i).getOrder_id() == orderPojo.getOrder_id()) {
                    equalIds = true;
                    System.out.println(orderPojo.getTime());
                    break;
                }
            }

            if(!equalIds) {
                placeOrderPojoList.remove(i);
                i--;
            }
        }

        int totalRevenue = 0;
        int totalQuantity = 0;
        for(PlaceOrderPojo pojo : placeOrderPojoList) {
            totalQuantity += pojo.getQuantity();
            totalRevenue += pojo.getQuantity() * pojo.getSelling_price();
        }

        salesData.append(totalQuantity).append("\t").append(totalRevenue);
    }

    public StringBuilder getAllBrand() {
        StringBuilder dataListString = new StringBuilder("");
        List<BrandPojo> pojoList = brandService.getAll();
        List<BrandData> dataList = new ArrayList<>();

        dataListString.append("Brand").append("\t").append("Category").append("\n");

        for (BrandPojo pojo : pojoList) {
            dataList.add(BrandDto.convertPojoToData(pojo));
            dataListString.append(pojo.getBrand()).append("\t").append(pojo.getCategory()).append("\n");
        }

        brandJavaObjToTsvFile(dataList);
        return dataListString;
    }

    public StringBuilder getAllInventory() throws ApiException {
        StringBuilder dataListString = new StringBuilder("");
        List<InventoryPojo> pojoList = inventoryService.getAll();
        List<InventoryData> dataList = new ArrayList<>();

        dataListString.append("Barcode").append("\t").append("Inventory").append("\t").append("Brand").append("\t").append("Category").append("\n");

        for (InventoryPojo pojo : pojoList) {
            dataList.add(InventoryDto.convertPojoToData(pojo));
            String brand = productService.get(pojo.getBarcode()).getBrand();
            String category = productService.get(pojo.getBarcode()).getCategory();

            dataListString.append(pojo.getBarcode()).append("\t").append(pojo.getInventory()).append("\t").append(brand).append("\t").append(category).append("\n");
        }

        inventoryJavaObjToTsvFile(dataList);

        return dataListString;
    }

    public void brandJavaObjToTsvFile(List<BrandData> dataList) {
        try {
            FileWriter fos = new FileWriter("src/main/resources/tsv/brand.tsv");
            PrintWriter dos = new PrintWriter(fos);
            dos.println("Brand ID\tBrand\tCategory\t");

            for (BrandData brandData : dataList) {
                dos.print(brandData.getId() + "\t");
                dos.print(brandData.getBrand() + "\t");
                dos.print(brandData.getCategory() + "\t");
                dos.println();
            }

            dos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error Printing Tab Delimited File");
        }
    }

    public void inventoryJavaObjToTsvFile(List<InventoryData> dataList) {
        try {
            FileWriter fos = new FileWriter("src/main/resources/tsv/inventory.tsv");
            PrintWriter dos = new PrintWriter(fos);
            dos.println("Inventory ID\tBarcode\tInventory\t");

            for (InventoryData inventoryData : dataList) {
                dos.print(inventoryData.getId() + "\t");
                dos.print(inventoryData.getBarcode() + "\t");
                dos.print(inventoryData.getInventory() + "\t");
                dos.println();
            }
            dos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error Printing Tab Delimited File");
        }
    }
}
