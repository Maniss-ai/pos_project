package com.increff.employee.dto;

import com.increff.employee.generatepdf.ObjectToXml;
import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.form.OrderItemForm;
import com.increff.employee.model.form.OrderItemUpdateForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.Checks;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderItemDto {
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderDto orderDto;

    public OrderItemDto() {

    }

    // CRUD ....
    public OrderItemData add(OrderItemForm form) throws ApiException {
        Checks.nullCheckOrderItem(form);
        OrderItemPojo pojo = DtoHelper.convertFormToPojoOrderItem(form, 0);
        DtoHelper.normalizeOrderItem(pojo);

        // get inventory_id using barcode_id match ....
        int inventory_id = getInventoryIdMatchWithBarcode(form.getBarcode());
        int inventory_items = inventoryService.get(inventory_id).getInventory();

        // check if there exists enough products in inventory to place order ....
        if(inventory_items < form.getQuantity()) {
            throw new ApiException("There's only " + inventory_items + " items available in the Inventory");
        }
        else if(!isSellingPriceLessThanMRP(pojo)) {
            throw new ApiException("Selling Price Can't be More Than MRP");
        }
        else {
            if(alreadyAdded(form)) {
                throw new ApiException("Order already exists, Please Update");
            }
            else {
                System.out.println("NOW");
                return DtoHelper.convertPojoToDataOrderItem(orderItemService.add(pojo));
            }
        }
    }

    public void delete(int id) throws ApiException {
        orderItemService.delete(id);
    }

    public OrderItemData get(int id) throws ApiException {
        OrderItemPojo pojo = orderItemService.get(id);
        return DtoHelper.convertPojoToDataOrderItem(pojo);
    }

    public List<OrderItemData> getSingleOrder(int order_id) {
        List<OrderItemPojo> pojoList = orderItemService.getSingleOrder(order_id);
        List<OrderItemData> dataList = new ArrayList<>();
        for(OrderItemPojo pojo : pojoList) {
            dataList.add(DtoHelper.convertPojoToDataOrderItem(pojo));
        }
        return dataList;
    }

    public List<OrderItemData> getAll() {
        List<OrderItemPojo> pojoList = orderItemService.getAll();
        List<OrderItemData> dataList = new ArrayList<>();
        for (OrderItemPojo pojo : pojoList) {
            // if order is not placed than display it ....
            if(pojo.getOrder_id() == 0) {
                dataList.add(DtoHelper.convertPojoToDataOrderItem(pojo));
            }
        }
        return dataList;
    }

    @Transactional
    public void submit(List<OrderItemForm> orderFormList) throws ApiException {
        if(orderFormList.isEmpty()) {
            throw new ApiException("Please add product to place an order");
        }
        OrderPojo orderPojo = new OrderPojo();

        orderPojo.setTime(LocalDate.now());

        double total_bill_amount = 0.0;
        for(OrderItemForm form : orderFormList) {
            total_bill_amount += (form.getQuantity() * form.getSelling_price());
        }

        orderPojo.setBill_amount(total_bill_amount);

        orderItemService.submit(orderPojo);
        int order_id = orderPojo.getOrder_id();

        // add order_id and update items ....
        for (OrderItemForm form : orderFormList) {
//            form.setOrder_id(order_id);
            OrderItemPojo orderItemPojo = DtoHelper.convertFormToPojoOrderItem(form, order_id);
            orderItemService.updateOrderId(orderItemPojo.getBarcode(), orderItemPojo);

            // update inventory ....
            int current_inventory = inventoryService.getCheck(orderItemPojo.getBarcode()).getInventory();
            current_inventory -= orderItemPojo.getQuantity();

            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setInventory(current_inventory);
            inventoryPojo.setBarcode(orderItemPojo.getBarcode());
            inventoryService.update(orderItemPojo.getBarcode(), inventoryPojo);
        }
    }

    public void update(int place_order_id, OrderItemUpdateForm form) throws ApiException {
        Checks.nullCheckForUpdateOrderItem(form);
        String barcode = get(place_order_id).getBarcode();
        OrderItemPojo pojo = DtoHelper.convertFormToPojoForUpdateOrderItem(form, 0, barcode);

        // get inventory_id using barcode_id match ....
        int inventory_id = getInventoryIdMatchWithBarcode(barcode);
        int inventory_items = inventoryService.get(inventory_id).getInventory();

        if(inventory_items < form.getQuantity()) {
            throw new ApiException("There's only " + inventory_items + " items available in the Inventory");
        }
        else if(!isSellingPriceLessThanMRP(pojo)) {
            throw new ApiException("Selling Price can't be more than MRP");
        }
        else {
            orderItemService.update(place_order_id, pojo);
        }
    }

    public void generatePdfForOrder(HttpServletResponse response, int orderId) throws Exception {
        List<OrderItemData> placeOrderDataList = getSingleOrder(orderId);
        OrderPojo orderPojo = orderDto.getOrder(orderId);
        ObjectToXml.generateXmlString(placeOrderDataList, orderPojo);


        File file = new File("src/main/resources/pdf/invoice.pdf");

        if (file.exists()) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
    }

    private boolean alreadyAdded(OrderItemForm form) {
        List<OrderItemPojo> list = orderItemService.getAll();
        for(OrderItemPojo orderItemPojo : list) {
            if(Objects.equals(orderItemPojo.getBarcode(), form.getBarcode()) && 0 == orderItemPojo.getOrder_id()) {
                return true;
            }
        }
        return false;
    }

    private boolean isSellingPriceLessThanMRP(OrderItemPojo pojo) throws ApiException {
        ProductPojo productPojo = productService.getWithBarcode(pojo.getBarcode());

        if(pojo.getSelling_price() > productPojo.getMrp()) {
            return false;
        }
        else {
            pojo.setBarcode(productService.getWithBarcode(pojo.getBarcode()).getBarcode());
            return true;
        }
    }

    private int getInventoryIdMatchWithBarcode(String barcode) throws ApiException {
        int inventory_id = -1;
        List<InventoryPojo> list = inventoryService.getAll();
        for(InventoryPojo inventoryPojo : list) {
            if(Objects.equals(inventoryPojo.getBarcode(), barcode)) {
                inventory_id = inventoryPojo.getId();
                break;
            }
        }

        if(inventory_id == -1) {
            throw new ApiException("Inventory with barcode: " + barcode + " doesn't exists");
        }
        else {
            return inventory_id;
        }
    }

    private int getProductIdMatchWithBarcode(OrderItemPojo pojo) throws ApiException {
        int product_id = -1;
        List<ProductPojo> list = productService.getAll();
        for(ProductPojo productPojo : list) {
            if(Objects.equals(productPojo.getBarcode(), pojo.getBarcode())) {
                product_id = productPojo.getId();
                break;
            }
        }

        if(product_id == -1) {
            throw new ApiException("Inventory with barcode: " + get(product_id).getBarcode() + " doesn't exists");
        }
        else {
            return product_id;
        }
    }
}
