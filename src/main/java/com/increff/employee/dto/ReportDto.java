package com.increff.employee.dto;

import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.data.InventoryData;
import com.increff.employee.model.form.ReportForm;
import com.increff.employee.pojo.*;
import com.increff.employee.service.*;
import com.increff.employee.util.Checks;
import com.mysql.cj.conf.ConnectionUrlParser;
import com.sun.xml.bind.v2.TODO;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class ReportDto {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

    public StringBuilder getAllSales(ReportForm form) throws ApiException, ParseException {
        if(form.getStartDate() != null && form.getEndDate() != null && !form.getStartDate().isEmpty() && !form.getEndDate().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(sdf.parse(form.getStartDate()).compareTo(sdf.parse(form.getEndDate())) > 0) {
                throw new ApiException("Start Date should come before End Date");
            }

            // check weather brand or category exists ...
            if(form.getBrand() != null && form.getCategory() != null && !form.getBrand().isEmpty() && !form.getCategory().isEmpty() && !Checks.checkBrandCategoryExists(form.getBrand(), form.getCategory(), brandService.getAll())) {
                throw new ApiException("Brand or Category doesn't exists");
            }
            if(form.getBrand() != null && !form.getBrand().isEmpty() && !Checks.checkBrandExists(form.getBrand(), brandService.getAll())) {
                throw new ApiException("Brand doesn't exists");
            }
            if(form.getCategory() != null && !form.getCategory().isEmpty() && !Checks.checkCategoryExists(form.getCategory(), brandService.getAll())) {
                throw new ApiException("Category doesn't exists");
            }

            StringBuilder salesData = new StringBuilder("Brand\tCategory\tQuantity\tRevenue\n");
            List<ProductPojo> productPojoList;
//            TODO CHANGE BRAND & Category from productDao Query ........ MAJOR CHANGE ........
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(form.getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(form.getEndDate(), formatter);

            // USING BRAND and CATEGORY + DATE
            if(form.getBrand() != null && !form.getBrand().isEmpty() && form.getCategory() != null && !form.getCategory().isEmpty()) {
                salesData.append(form.getBrand()).append("\t").append(form.getCategory()).append("\t");
                Integer brandCategory = brandService.getBrandCategory(form.getBrand(), form.getCategory()).getId();
                productPojoList = productService.getWithBrandCategory(brandCategory);
                getReportWithBrandCategory(startDate, endDate, salesData, productPojoList);
            }

            // USING BRAND ONLY + DATE
            else if(form.getBrand() != null && !form.getBrand().isEmpty()) {
                List<BrandPojo> brandPojoList = brandService.getBrand(form.getBrand());
                for(BrandPojo brandPojo : brandPojoList) {
                    productPojoList = productService.getWithBrandCategory(brandPojo.getId());
                    getReportWithBrand(startDate, endDate, salesData, productPojoList, form.getBrand());
                }
            }

            // USING CATEGORY ONLY + DATE
            else if(form.getCategory() != null && !form.getCategory().isEmpty()) {
                List<BrandPojo> brandPojoList = brandService.getCategory(form.getCategory());
                for(BrandPojo brandPojo : brandPojoList) {
                    productPojoList = productService.getWithBrandCategory(brandPojo.getId());
                    getReportWithCategory(startDate, endDate, salesData, productPojoList, form);
                }
            }

            // USING DATE ONLY
            else {
                productPojoList = productService.getAll();
                Set<String> brandSet = new HashSet<>();

                for (ProductPojo pojo : productPojoList) {
                    brandSet.add(brandService.get(pojo.getBrandCategoryId()).getBrand());
                }

                for (String brand : brandSet) {
                    List<BrandPojo> brandPojoList = brandService.getBrand(brand);
                    for(BrandPojo brandPojo : brandPojoList) {
                        productPojoList = productService.getWithBrandCategory(brandPojo.getId());
                        getReportWithBrand(startDate, endDate, salesData, productPojoList, brand);
                    }
                }
            }

            return salesData;
        }
        else {
            throw new ApiException("Date can't be empty ....");
        }
    }

    public StringBuilder getAllBrand() throws ApiException {
        StringBuilder dataListString = new StringBuilder("");
        List<BrandPojo> pojoList = brandService.getAll();
        List<BrandData> dataList = new ArrayList<>();

        dataListString.append("Brand").append("\t").append("Category").append("\n");

        for (BrandPojo pojo : pojoList) {
            dataList.add(DtoHelper.convertPojoToDataBrand(pojo));
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
            dataList.add(DtoHelper.convertPojoToDataInventory(pojo));
//            String brand = productService.getWithBarcode(pojo.getBarcode()).getBrand();
            String brand = brandService.get(productService.getWithId(pojo.getId()).getBrandCategoryId()).getBrand();
//            String category = productService.getWithBarcode(pojo.getBarcode()).getCategory();
            String category = brandService.get(productService.getWithId(pojo.getId()).getBrandCategoryId()).getCategory();

            dataListString.append(productService.getWithId(pojo.getId()).getBarcode()).append("\t").append(pojo.getInventory()).append("\t").append(brand).append("\t").append(category).append("\n");
        }

        inventoryJavaObjToTsvFile(dataList);

        return dataListString;
    }

    private void getReportWithBrandCategory(LocalDate startDate, LocalDate endDate, StringBuilder salesData, List<ProductPojo> productPojoList) {
        // GET PlaceOrderPojo using Barcode and Add to DATA LIST .... ....
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (ProductPojo pojo : productPojoList) {
            orderItemPojoList.addAll(orderItemService.getWithProductId(pojo.getId()));
        }
        // Get Order Ids
        List<OrderPojo> orderPojoList = orderService.getSelectedOrdersWithoutId(startDate, endDate);

        // Match Order Ids according to the given Time Range ....
        for(int i = 0; i < orderItemPojoList.size(); i++) {
            boolean equalIds = false;
            for(OrderPojo orderPojo : orderPojoList) {
                if(Objects.equals(orderItemPojoList.get(i).getOrderId(), orderPojo.getOrderId())) {
                    equalIds = true;
                    break;
                }
            }

            if(!equalIds) {
                orderItemPojoList.remove(i);
                i--;
            }
        }

        Double totalRevenue = 0.0;
        Integer totalQuantity = 0;
        for(OrderItemPojo pojo : orderItemPojoList) {
            totalQuantity += pojo.getQuantity();
            totalRevenue += pojo.getQuantity() * pojo.getSellingPrice();
        }

        salesData.append(totalQuantity).append("\t").append(Precision.round(totalRevenue, 2));
    }

    private void getReportWithBrand(LocalDate startDate, LocalDate endDate, StringBuilder salesData, List<ProductPojo> productPojoList, String brand) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (ProductPojo productPojo : productPojoList) {
            orderItemPojoList.addAll(orderItemService.getWithProductId(productPojo.getId()));
        }

        // Get Order Ids
        List<OrderPojo> orderPojoList = orderService.getSelectedOrdersWithoutId(startDate, endDate);

        // Match Order Ids according to the given Time Range ....
        for(int i = 0; i < orderItemPojoList.size(); i++) {
            boolean equalIds = false;
            for(OrderPojo orderPojo : orderPojoList) {
                if(Objects.equals(orderItemPojoList.get(i).getOrderId(), orderPojo.getOrderId())) {
                    equalIds = true;
                    break;
                }
            }

            if(!equalIds) {
                orderItemPojoList.remove(i);
                i--;
            }
        }

        Map<String, ConnectionUrlParser.Pair<Integer, Double>> revenueQuantityMap = new HashMap<>();
        Integer totalQuantity;
        Double totalRevenue;

        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            totalQuantity = orderItemPojo.getQuantity();
            totalRevenue = orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();

            if (revenueQuantityMap.containsKey(brandService.get(productService.getWithId(orderItemPojo.getProductId()).getBrandCategoryId()).getCategory())) {
                totalQuantity += revenueQuantityMap.get(brandService.get(productService.getWithId(orderItemPojo.getProductId()).getBrandCategoryId()).getCategory()).left;
                totalRevenue += revenueQuantityMap.get(brandService.get(productService.getWithId(orderItemPojo.getProductId()).getBrandCategoryId()).getCategory()).right;
            }

            ConnectionUrlParser.Pair<Integer, Double> pair = new ConnectionUrlParser.Pair<>(totalQuantity, Precision.round(totalRevenue, 2));
            revenueQuantityMap.put(
                    brandService.get(productService.getWithId(orderItemPojo.getProductId()).getBrandCategoryId()).getCategory(),
                    pair
            );

        }

        for(Map.Entry<String, ConnectionUrlParser.Pair<Integer, Double>> entry : revenueQuantityMap.entrySet()) {
            salesData.append(brand)
                    .append("\t")
                    .append(entry.getKey())
                    .append("\t")
                    .append(entry.getValue().left)
                    .append("\t")
                    .append(entry.getValue().right)
                    .append("\n");
        }
    }

    private void getReportWithCategory(LocalDate startDate, LocalDate endDate, StringBuilder salesData, List<ProductPojo> productPojoList, ReportForm form) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (ProductPojo pojo : productPojoList) {
            orderItemPojoList.addAll(orderItemService.getWithProductId(pojo.getId()));
        }

        // Get Order Ids
        List<OrderPojo> orderPojoList = orderService.getSelectedOrdersWithoutId(startDate, endDate);

        // Match Order Ids according to the given Time Range ....
        for(int i = 0; i < orderItemPojoList.size(); i++) {
            boolean equalIds = false;
            for(OrderPojo orderPojo : orderPojoList) {
                if(Objects.equals(orderItemPojoList.get(i).getOrderId(), orderPojo.getOrderId())) {
                    equalIds = true;
                    break;
                }
            }

            if(!equalIds) {
                orderItemPojoList.remove(i);
                i--;
            }
        }

        Map<String, ConnectionUrlParser.Pair<Integer, Double>> revenueQuantityMap = new HashMap<>();
        Integer totalQuantity;
        Double totalRevenue;

        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            totalQuantity = orderItemPojo.getQuantity();
            totalRevenue = orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();

            if (revenueQuantityMap.containsKey(brandService.get(productService.getWithId(orderItemPojo.getProductId()).getBrandCategoryId()).getBrand())) {
                totalQuantity += revenueQuantityMap.get(brandService.get(productService.getWithId(orderItemPojo.getProductId()).getBrandCategoryId()).getBrand()).left;
                totalRevenue += revenueQuantityMap.get(brandService.get(productService.getWithId(orderItemPojo.getProductId()).getBrandCategoryId()).getBrand()).right;
            }

            ConnectionUrlParser.Pair<Integer, Double> pair = new ConnectionUrlParser.Pair<>(totalQuantity, Precision.round(totalRevenue, 2));
            revenueQuantityMap.put(
                    brandService.get(productService.getWithId(orderItemPojo.getProductId()).getBrandCategoryId()).getBrand(),
                    pair
            );

        }

        for(Map.Entry<String, ConnectionUrlParser.Pair<Integer, Double>> entry : revenueQuantityMap.entrySet()) {
            salesData.append(entry.getKey())
                    .append("\t")
                    .append(form.getCategory())
                    .append("\t")
                    .append(entry.getValue().left)
                    .append("\t")
                    .append(entry.getValue().right)
                    .append("\n");
        }
    }

    public void brandJavaObjToTsvFile(List<BrandData> dataList) throws ApiException {
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
            throw new ApiException("Error Printing Tab Delimited File");
        }
    }

    public void inventoryJavaObjToTsvFile(List<InventoryData> dataList) throws ApiException {
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
            throw new ApiException("Error Printing Tab Delimited File");
        }
    }

}
